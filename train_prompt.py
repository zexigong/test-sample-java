import json
import datetime
import argparse
import os
import yaml


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

def read_files(file_paths):
    """
    Reads and returns content from multiple files.
    """
    contents = []
    for path in file_paths:
        if path == "empty.txt":
            contents.append("")  # Empty string for missing dependencies
        else:
            with open(path, "r", encoding="utf-8") as f:
                contents.append(f.read())
    return contents

# load the display path config (yaml file)
def load_display_path_map(file="path\\kafka_path.yaml"):
    with open(file, "r", encoding="utf-8") as f:
        data = yaml.safe_load(f)["entries"]
        return {
            entry["test_file"]: {
                "test_display": entry["test_display"],
                "source_display": entry["source_display"]
            }
            for entry in data
        }

def generate_messages(repository: str,
                      source_file_contents: list,
                      display_path_map: dict,
                      test_file_path: str,
                      language: str,
                      framework: str,
                      dependencies_file_names: list,
                      dependencies_file_contents: list,
                      test_example_content: str) -> dict:
    """
    Generate a JSON object in conversation format for fine-tuning.
    """
    current_time = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    system_message = (
        "You are an AI agent expert in writing unit tests. "
        "Your task is to write unit tests for the given code files of the repository. "
        "Make sure the tests can be executed without lint or compile errors."
    )

    # Combine multiple source files
    source_content = "\n\n".join(
        [f"### Source File Content:\n{content}" for content in source_file_contents]
    )

    # Combine dependencies using file names
    dependencies_content = "\n\n".join(
        [f"### Dependency File: {os.path.basename(name)}\n{content}" for name, content in zip(dependencies_file_names, dependencies_file_contents)]
    )

    # file path 
    display_path_map = load_display_path_map()
    # real test_file and source_file still used for reading
    test_display = display_path_map[test_file_path]["test_display"]
    source_display = display_path_map[test_file_path]["source_display"]
    print(test_display, source_display)

    user_message = (
        "### Task Information\n"
        "Based on the source code, write/rewrite tests to cover the source code.\n"
        f"Repository: {repository}\n"
        f"Source File Path: {source_display}\n"
        f"Test File Path: {test_display}\n"
        f"Project Programming Language: {language}\n"
        f"Testing Framework: {framework}\n"
        "### Source File Content\n"
        f"{source_content}\n"
        "### Source File Dependency Files Content\n"
        f"{dependencies_content}\n"
        "Output the complete test file, code only, no explanations.\n"
        f"```{language}\n<complete test code>\n```\n"
        "### Time\n"
        f"Current time: {current_time}"
    )

    # Format the assistant message to include the test example within a Python code block
    assistant_message = f"```java\n{test_example_content}\n```"

    messages = [
        {"role": "system", "content": system_message},
        {"role": "user", "content": user_message},
        {"role": "assistant", "content": assistant_message}
    ]
    return {"messages": messages}


def main():
    parser = argparse.ArgumentParser(
        description="Auto-scan repo and generate fine-tuning JSONL document"
    )
    parser.add_argument("--repository", type=str, required=True, help="Repository name or path")
    parser.add_argument("--repo_path", type=str, required=True, help="Path to the repository directory")
    parser.add_argument("--language", type=str, required=True, help="Programming language")
    parser.add_argument("--framework", type=str, required=True, help="Testing framework")
    parser.add_argument("--output", type=str, default="fine_tuning.jsonl", help="Output JSONL file")

    args = parser.parse_args()

    # Auto-map test files to corresponding source and dependency files
    test_source_map = find_files(args.repo_path)

    display_path_map = load_display_path_map()

    # Process each test file separately
    for test_file, file_mappings in test_source_map.items():
        related_sources = file_mappings["sources"]
        related_dependencies = file_mappings["dependencies"]

        # Read content of source and dependency files
        source_file_contents = read_files(related_sources)
        dependencies_file_contents = read_files(related_dependencies)

        # Read test file content
        with open(test_file, "r", encoding="utf-8") as te:
            test_example_content = te.read()

        conversation = generate_messages(
            repository=args.repository,
            source_file_contents=source_file_contents,
            test_file_path=test_file,
            language=args.language,
            framework=args.framework,
            dependencies_file_names=related_dependencies,
            dependencies_file_contents=dependencies_file_contents,
            test_example_content=test_example_content,
            display_path_map=display_path_map
        )

        # Append to the JSONL file
        with open(args.output, "a", encoding="utf-8") as out_file:
            out_file.write(json.dumps(conversation) + "\n")


if __name__ == "__main__":
    main()
