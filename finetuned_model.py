import os
import json
import yaml
import datetime
import argparse
from openai import AzureOpenAI

# Azure OpenAI Configuration (FT Model)
endpoint = os.getenv("ENDPOINT_URL", "https://gongz-m82drk07-eastus2.openai.azure.com/")  
deployment = os.getenv("DEPLOYMENT_NAME", "gpt-4o-2024-08-06-ft-c1933f4a82bd4d5f8132bb22e643a204")  
subscription_key = os.getenv("AZURE_OPENAI_API_KEY", "")  

client = AzureOpenAI(
    azure_endpoint=endpoint,
    api_key=subscription_key,
    api_version="2024-05-01-preview",
)

def find_files(repo_path: str):
    """
    Automatically maps each feature folder to:
    - 1 test file (any .ts file in the root of the folder)
    - source files in `source_files/`
    - dependency files in `dependent_files/`
    """
    test_source_map = {}

    # Iterate over all subfolders (e.g., addWeeks, addDays)
    for item in os.listdir(repo_path):
        feature_path = os.path.join(repo_path, item)

        if not os.path.isdir(feature_path):
            continue  # Skip if not a directory

        # Find test file: any .ts file directly inside the folder (not in subfolders)
        test_files = [
            os.path.join(feature_path, f)
            for f in os.listdir(feature_path)
            if f.endswith(".java") and os.path.isfile(os.path.join(feature_path, f))
        ]

        if not test_files:
            continue  # Skip if no test file found

        test_file = test_files[0]  # Assume only one test file per folder

        # Locate source and dependency folders
        source_folder = os.path.join(feature_path, "source_files")
        dependency_folder = os.path.join(feature_path, "dependent_files")

        source_files = [
            os.path.join(source_folder, f)
            for f in os.listdir(source_folder)
            if os.path.isfile(os.path.join(source_folder, f))
        ] if os.path.exists(source_folder) else []

        dependency_files = [
            os.path.join(dependency_folder, f)
            for f in os.listdir(dependency_folder)
            if os.path.isfile(os.path.join(dependency_folder, f))
        ] if os.path.exists(dependency_folder) else ["empty.txt"]

        test_source_map[test_file] = {
            "sources": source_files,
            "dependencies": dependency_files
        }

    return test_source_map

# Function to read file contents
def read_files(file_paths):
    contents = []
    for path in file_paths:
        if path == "empty.txt":
            contents.append("")
        else:
            with open(path, "r", encoding="utf-8") as f:
                contents.append(f.read())
    return contents

# Function to generate a structured prompt
def generate_prompt(repository: str, source_file_contents: list, source_file_path: str, test_file_path: str, 
                     language: str, framework: str, dependencies_file_names: list, dependencies_file_contents: list):

    current_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    system_message = "You are an AI agent expert in writing unit tests. Your task is to write unit tests for the given code files of the repository. Make sure the tests can be executed without lint or compile errors."

    source_content = "\n\n".join([f"### Source File Content:\n{content}" for content in source_file_contents])
    dependencies_content = "\n\n".join([f"### Dependency File: {os.path.basename(name)}\n{content}" for name, content in zip(dependencies_file_names, dependencies_file_contents)])

    user_message = f"""### Task Information
Based on the source code, write/rewrite tests to cover the source code.
Repository: {repository}
Source File Path: {source_file_path}
Test File Path: {test_file_path}
Project Programming Language: {language}
Testing Framework: {framework}
### Source File Content
{source_content}
### Source File Dependency Files Content
{dependencies_content}
Output the complete test file, code only, no explanations.
### Time
Current time: {current_time}
"""

    messages = [
        {"role": "system", "content": system_message},
        {"role": "user", "content": user_message},
    ]
    
    return {"messages": messages}

# Function to call Azure OpenAI base model
def call_openai_model(messages):
    completion = client.chat.completions.create(
        model=deployment,
        messages=messages,
        max_tokens=16000,
        temperature=0.7,
        top_p=0.95,
        frequency_penalty=0,
        presence_penalty=0,
        stop=None,
        stream=False
    )
    return completion.choices[0].message.content

# Function to save response as a Typescript file
def save_response_as_java(repo_name, source_file, response):
    source_file_name = os.path.basename(source_file).replace(".java", "")
    response_filename = f"{repo_name}_pyft_{source_file_name}.java"

    if response.startswith("```java"):
        response = response.split("\n", 1)[1].strip()
    if response.endswith("```"):
        response = response.rsplit("```", 1)[0].strip()
    
    with open(response_filename, "w", encoding="utf-8") as f:
        f.write(response)
    
    print(f"Saved Response as Java File: {response_filename}")

# Function to save the prompt as a YAML file
def save_prompt_as_yaml(repo_name, source_file, prompt_data):
    source_file_name = os.path.basename(source_file).replace(".java", "")
    prompt_filename = f"{repo_name}_{source_file_name}_prompt.yaml"
    
    with open(prompt_filename, "w", encoding="utf-8") as f:
        yaml.dump(prompt_data, f, allow_unicode=True)
    
    print(f"Saved Prompt as YAML File: {prompt_filename}")


    
# load the display path config (yaml file)
def load_display_path_map(file="path\\lucene_path.yaml"):
    with open(file, "r", encoding="utf-8") as f:
        data = yaml.safe_load(f)["entries"]
        return {
            entry["test_file"]: {
                "test_display": entry["test_display"],
                "source_display": entry["source_display"]
            }
            for entry in data
        }

# Main execution function
def main():
    parser = argparse.ArgumentParser(description="Generate prompt & get response from Azure OpenAI (Base Model)")
    parser.add_argument("--repository", type=str, required=True, help="Repository name or path")
    parser.add_argument("--repo_path", type=str, required=True, help="Path to the repository directory")
    parser.add_argument("--language", type=str, required=True, help="Programming language")
    parser.add_argument("--framework", type=str, required=True, help="Testing framework")

    args = parser.parse_args()

    # Auto-detect files
    test_source_map = find_files(args.repo_path)
    print(test_source_map)


    for test_file, file_mappings in test_source_map.items():
        related_sources = file_mappings["sources"]
        related_dependencies = file_mappings["dependencies"]

        # Read files
        source_file_contents = read_files(related_sources)
        dependencies_file_contents = read_files(related_dependencies)
        # file path 
        display_path_map = load_display_path_map()
        # real test_file and source_file still used for reading
        test_display = display_path_map[test_file]["test_display"]
        source_display = display_path_map[test_file]["source_display"]
        print(test_display, source_display)

        # Generate prompt
        prompt_data = generate_prompt(
            repository=args.repository,
            source_file_contents=source_file_contents,
            source_file_path=source_display,
            test_file_path=test_display, 
            language=args.language,
            framework=args.framework,
            dependencies_file_names=related_dependencies,
            dependencies_file_contents=dependencies_file_contents
        )

        # Call OpenAI base model
        response = call_openai_model(prompt_data["messages"])

        # Save results
        save_response_as_java(args.repository, test_file, response)
        save_prompt_as_yaml(args.repository, test_file, prompt_data)

if __name__ == "__main__":
    main()