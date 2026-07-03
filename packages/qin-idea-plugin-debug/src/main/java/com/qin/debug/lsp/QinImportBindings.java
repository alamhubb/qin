package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class QinImportBindings {
    private QinImportBindings() {
    }

    static @NotNull List<ImportBinding> collect(@NotNull PsiFile file) {
        List<ImportBinding> bindings = new ArrayList<>();
        file.accept(new com.intellij.psi.PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element.getNode() != null
                        && element.getNode().getElementType() == QinTokenTypes.IMPORT_DECLARATION) {
                    bindings.addAll(collectFromDeclaration(element));
                    return;
                }
                super.visitElement(element);
            }
        });
        return bindings;
    }

    static @Nullable ImportBinding findForSpecifierElement(@NotNull PsiElement element) {
        PsiElement specifier = parentOfType(element, QinTokenTypes.IMPORT_SPECIFIER);
        if (specifier == null) {
            return null;
        }
        PsiElement declaration = parentOfType(specifier, QinTokenTypes.IMPORT_DECLARATION);
        if (declaration == null) {
            return null;
        }
        for (ImportBinding binding : collectFromDeclaration(declaration)) {
            if (binding.exportedName().equals(element.getText())
                    || binding.localName().equals(element.getText())) {
                return binding;
            }
        }
        return null;
    }

    static boolean isAliasedLocalSpecifierElement(@NotNull PsiElement element) {
        ImportBinding binding = findForSpecifierElement(element);
        return binding != null
                && binding.localName().equals(element.getText())
                && !binding.exportedName().equals(binding.localName());
    }

    static @Nullable PsiElement findAliasName(@NotNull PsiElement element, @NotNull String localName) {
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        AliasNameVisitor visitor = new AliasNameVisitor(localName);
        file.accept(visitor);
        return visitor.aliasName;
    }

    private static @NotNull List<ImportBinding> collectFromDeclaration(@NotNull PsiElement importDeclaration) {
        List<QinPsiToken> tokens = QinPsiTokenStream.collect(importDeclaration);
        int fromIndex = nextKeywordIndex(tokens, 0, "from");
        if (fromIndex < 0 || fromIndex + 1 >= tokens.size()) {
            return List.of();
        }
        String moduleSpecifier = readStringLiteralValue(tokens.get(fromIndex + 1));
        if (moduleSpecifier == null) {
            return List.of();
        }

        List<ImportBinding> bindings = new ArrayList<>();
        importDeclaration.accept(new com.intellij.psi.PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element.getNode() != null
                        && element.getNode().getElementType() == QinTokenTypes.IMPORT_SPECIFIER) {
                    NamedImport namedImport = parseNamedImport(element);
                    if (namedImport != null) {
                        bindings.add(new ImportBinding(
                                moduleSpecifier,
                                namedImport.exportedName(),
                                namedImport.localName()));
                    }
                    return;
                }
                super.visitElement(element);
            }
        });
        return bindings;
    }

    private static @Nullable NamedImport parseNamedImport(@NotNull PsiElement specifier) {
        List<QinPsiToken> tokens = QinPsiTokenStream.collect(specifier);
        for (int index = 0; index < tokens.size(); index++) {
            QinPsiToken exported = tokens.get(index);
            if (!exported.isIdentifier()) {
                continue;
            }
            String exportedName = exported.text();
            String localName = exportedName;
            if (index + 2 < tokens.size()
                    && tokens.get(index + 1).isKeyword("as")
                    && tokens.get(index + 2).isIdentifier()) {
                localName = tokens.get(index + 2).text();
            }
            return new NamedImport(exportedName, localName);
        }
        return null;
    }

    private static int nextKeywordIndex(@NotNull List<QinPsiToken> tokens, int startIndex, @NotNull String text) {
        for (int index = startIndex; index < tokens.size(); index++) {
            if (tokens.get(index).isKeyword(text)) {
                return index;
            }
        }
        return -1;
    }

    private static @Nullable String readStringLiteralValue(@NotNull QinPsiToken token) {
        if (token.type() != QinTokenTypes.STRING) {
            return null;
        }
        String text = token.text();
        if (text.length() < 2) {
            return null;
        }
        return text.substring(1, text.length() - 1).trim();
    }

    private static @Nullable PsiElement parentOfType(@NotNull PsiElement element, @NotNull com.intellij.psi.tree.IElementType type) {
        PsiElement current = element.getParent();
        while (current != null) {
            if (current.getNode() != null && current.getNode().getElementType() == type) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    record ImportBinding(@NotNull String moduleSpecifier, @NotNull String exportedName, @NotNull String localName) {
    }

    private record NamedImport(@NotNull String exportedName, @NotNull String localName) {
    }

    private static final class AliasNameVisitor extends com.intellij.psi.PsiRecursiveElementWalkingVisitor {
        private final String localName;
        private PsiElement aliasName;

        private AliasNameVisitor(@NotNull String localName) {
            this.localName = localName;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (aliasName != null) {
                return;
            }
            if (element.getNode() != null
                    && element.getNode().getElementType() == QinTokenTypes.IMPORT_ALIAS_NAME
                    && localName.equals(element.getText())) {
                aliasName = element;
                return;
            }
            super.visitElement(element);
        }
    }
}
