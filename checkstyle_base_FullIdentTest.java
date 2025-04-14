package com.puppycrawl.tools.checkstyle.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class FullIdentTest {

    @Test
    public void testCreateFullIdent() {
        DetailAST ast = createMockAst(TokenTypes.IDENT, "java", 1, 0);
        ast.addChild(createMockAst(TokenTypes.DOT, ".", 1, 4));
        ast.getFirstChild().addChild(createMockAst(TokenTypes.IDENT, "util", 1, 5));

        FullIdent fullIdent = FullIdent.createFullIdent(ast);

        assertEquals("java.util", fullIdent.getText());
        assertNotNull(fullIdent.getDetailAst());
        assertEquals(1, fullIdent.getLineNo());
        assertEquals(0, fullIdent.getColumnNo());
    }

    @Test
    public void testCreateFullIdentBelow() {
        DetailAST ast = createMockAst(TokenTypes.IDENT, "java", 1, 0);
        ast.addChild(createMockAst(TokenTypes.DOT, ".", 1, 4));
        ast.getFirstChild().addChild(createMockAst(TokenTypes.IDENT, "util", 1, 5));

        FullIdent fullIdent = FullIdent.createFullIdentBelow(ast);

        assertEquals("java.util", fullIdent.getText());
        assertNotNull(fullIdent.getDetailAst());
        assertEquals(1, fullIdent.getLineNo());
        assertEquals(4, fullIdent.getColumnNo());
    }

    @Test
    public void testFullIdentWithArrayDeclarator() {
        DetailAST ast = createMockAst(TokenTypes.IDENT, "int", 1, 0);
        ast.addChild(createMockAst(TokenTypes.ARRAY_DECLARATOR, "[]", 1, 3));

        FullIdent fullIdent = FullIdent.createFullIdent(ast);

        assertEquals("int[]", fullIdent.getText());
    }

    @Test
    public void testToString() {
        DetailAST ast = createMockAst(TokenTypes.IDENT, "java", 1, 0);
        ast.addChild(createMockAst(TokenTypes.DOT, ".", 1, 4));
        ast.getFirstChild().addChild(createMockAst(TokenTypes.IDENT, "util", 1, 5));

        FullIdent fullIdent = FullIdent.createFullIdent(ast);

        assertEquals("java.util[1x0]", fullIdent.toString());
    }

    private DetailAST createMockAst(int type, String text, int lineNo, int columnNo) {
        DetailAST ast = Mockito.mock(DetailAST.class);
        Mockito.when(ast.getType()).thenReturn(type);
        Mockito.when(ast.getText()).thenReturn(text);
        Mockito.when(ast.getLineNo()).thenReturn(lineNo);
        Mockito.when(ast.getColumnNo()).thenReturn(columnNo);
        return ast;
    }
}