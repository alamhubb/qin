package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        file.accept(new com.intellij.psi.PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element.getNode() != null
                        && element.getNode().getElementType() == QinTokenTypes.IMPORT_DECLARATION) {
                    collectJavaImports(element, imports);
                    return;
                }
                super.visitElement(element);
            }
        });
        return new QinJavaImportTable(imports);
    }

    private static void collectJavaImports(PsiElement importDeclaration, Map<String, JavaImport> imports) {
        List<QinPsiToken> tokens = QinPsiTokenStream.collect(importDeclaration);
        int fromIndex = nextKeywordIndex(tokens, 0, "from");
        if (fromIndex < 0 || fromIndex + 1 >= tokens.size()) {
            return;
        }
        String moduleName = readJavaModuleName(tokens.get(fromIndex + 1));
        if (moduleName == null) {
            return;
        }
        importDeclaration.accept(new com.intellij.psi.PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element.getNode() != null
                        && element.getNode().getElementType() == QinTokenTypes.JAVA_IMPORT_SPECIFIER) {
                    ImportBinding binding = parseBinding(element);
                    if (binding != null) {
                        imports.put(binding.localName(),
                                new JavaImport(moduleName, binding.exportedName(), binding.localName()));
                    }
                    return;
                }
                super.visitElement(element);
            }
        });
    }

    private static @Nullable ImportBinding parseBinding(PsiElement specifier) {
        List<QinPsiToken> tokens = QinPsiTokenStream.collect(specifier);
        for (int index = 0; index < tokens.size(); index++) {
            QinPsiToken exported = tokens.get(index);
            if (!exported.isIdentifier()) {
                continue;
            }
            String exportedName = exported.text();
            String localName = exportedName;
            if (index + 2 < tokens.size() && tokens.get(index + 1).isKeyword("as") && tokens.get(index + 2).isIdentifier()) {
                localName = tokens.get(index + 2).text();
            }
            return new ImportBinding(exportedName, localName);
        }
        return null;
    }

    @Nullable JavaImport find(String localName) {
        return importsByLocalName.get(localName);
    }

    private static int nextKeywordIndex(List<QinPsiToken> tokens, int startIndex, String text) {
        for (int index = startIndex; index < tokens.size(); index++) {
            if (tokens.get(index).isKeyword(text)) {
                return index;
            }
        }
        return -1;
    }

    private static @Nullable String readJavaModuleName(QinPsiToken token) {
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
}
