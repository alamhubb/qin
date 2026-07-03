package com.qin.debug.lsp;

import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class QinJavaImportTable {
    private final Map<String, JavaImport> importsByLocalName;

    private QinJavaImportTable(Map<String, JavaImport> importsByLocalName) {
        this.importsByLocalName = importsByLocalName;
    }

    static QinJavaImportTable fromFile(@NotNull PsiFile file) {
        Map<String, JavaImport> imports = new LinkedHashMap<>();
        List<QinToken> tokens = collectTokens(file);
        for (int index = 0; index < tokens.size(); index++) {
            QinToken token = tokens.get(index);
            if (!token.isKeyword("import")) {
                continue;
            }

            int openBraceIndex = nextTokenIndex(tokens, index + 1, QinTokenTypes.BRACE, "{");
            if (openBraceIndex < 0) {
                continue;
            }
            int closeBraceIndex = nextTokenIndex(tokens, openBraceIndex + 1, QinTokenTypes.BRACE, "}");
            if (closeBraceIndex < 0) {
                continue;
            }
            int fromIndex = nextKeywordIndex(tokens, closeBraceIndex + 1, "from");
            if (fromIndex < 0 || fromIndex + 1 >= tokens.size()) {
                continue;
            }
            String moduleName = readJavaModuleName(tokens.get(fromIndex + 1));
            if (moduleName == null) {
                continue;
            }
            for (ImportBinding binding : parseBindings(tokens.subList(openBraceIndex + 1, closeBraceIndex))) {
                imports.put(binding.localName(), new JavaImport(moduleName, binding.exportedName(), binding.localName()));
            }
        }
        return new QinJavaImportTable(imports);
    }

    @Nullable JavaImport find(String localName) {
        return importsByLocalName.get(localName);
    }

    private static List<QinToken> collectTokens(PsiFile file) {
        List<QinToken> tokens = new ArrayList<>();
        file.accept(new com.intellij.psi.PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull com.intellij.psi.PsiElement element) {
                if (element.getFirstChild() == null && element.getNode() != null) {
                    IElementType type = element.getNode().getElementType();
                    if (type != com.intellij.psi.TokenType.WHITE_SPACE
                            && type != QinTokenTypes.LINE_COMMENT
                            && type != QinTokenTypes.BLOCK_COMMENT) {
                        tokens.add(new QinToken(type, element.getText()));
                    }
                }
                super.visitElement(element);
            }
        });
        return tokens;
    }

    private static List<ImportBinding> parseBindings(List<QinToken> tokens) {
        List<ImportBinding> bindings = new ArrayList<>();
        int index = 0;
        while (index < tokens.size()) {
            QinToken exported = tokens.get(index);
            if (!exported.isIdentifier()) {
                index++;
                continue;
            }
            String exportedName = exported.text();
            String localName = exportedName;
            if (index + 2 < tokens.size() && tokens.get(index + 1).isKeyword("as") && tokens.get(index + 2).isIdentifier()) {
                localName = tokens.get(index + 2).text();
                index += 3;
            } else {
                index++;
            }
            bindings.add(new ImportBinding(exportedName, localName));
            if (index < tokens.size() && tokens.get(index).is(QinTokenTypes.COMMA, ",")) {
                index++;
            }
        }
        return bindings;
    }

    private static int nextTokenIndex(List<QinToken> tokens, int startIndex, IElementType type, String text) {
        for (int index = startIndex; index < tokens.size(); index++) {
            if (tokens.get(index).is(type, text)) {
                return index;
            }
        }
        return -1;
    }

    private static int nextKeywordIndex(List<QinToken> tokens, int startIndex, String text) {
        for (int index = startIndex; index < tokens.size(); index++) {
            if (tokens.get(index).isKeyword(text)) {
                return index;
            }
        }
        return -1;
    }

    private static @Nullable String readJavaModuleName(QinToken token) {
        if (token.type() != QinTokenTypes.STRING) {
            return null;
        }
        String text = token.text();
        if (text.length() < 2) {
            return null;
        }
        String value = text.substring(1, text.length() - 1);
        return value.startsWith("java:") ? value.substring("java:".length()).trim() : null;
    }

    record JavaImport(String moduleName, String exportedName, String localName) {
        String qualifiedClassName() {
            return moduleName.isBlank() ? exportedName : moduleName + "." + exportedName;
        }
    }

    private record ImportBinding(String exportedName, String localName) {
    }

    private record QinToken(IElementType type, String text) {
        boolean isKeyword(String expectedText) {
            return type == QinTokenTypes.KEYWORD && expectedText.equals(text);
        }

        boolean isIdentifier() {
            return type == QinTokenTypes.IDENTIFIER
                    || type == QinTokenTypes.CLASS_NAME
                    || type == QinTokenTypes.FUNCTION_IDENTIFIER
                    || type == QinTokenTypes.MEMBER_IDENTIFIER;
        }

        boolean is(IElementType expectedType, String expectedText) {
            return type == expectedType && expectedText.equals(text);
        }
    }
}
