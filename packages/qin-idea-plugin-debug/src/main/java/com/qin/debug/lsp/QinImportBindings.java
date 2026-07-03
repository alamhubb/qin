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
        PsiElement specifier = QinPsiTree.parentOfType(element, QinTokenTypes.IMPORT_SPECIFIER);
        if (specifier == null) {
            return null;
        }
        PsiElement declaration = QinPsiTree.parentOfType(specifier, QinTokenTypes.IMPORT_DECLARATION);
        if (declaration == null) {
            return null;
        }
        String moduleSpecifier = readModuleSpecifier(declaration);
        if (moduleSpecifier == null) {
            return null;
        }
        ImportBinding binding = parseImportBinding(specifier, moduleSpecifier);
        if (binding != null
                && (binding.exportedName().equals(element.getText())
                || binding.localName().equals(element.getText()))) {
            return binding;
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
        String moduleSpecifier = readModuleSpecifier(importDeclaration);
        if (moduleSpecifier == null) {
            return List.of();
        }

        List<ImportBinding> bindings = new ArrayList<>();
        for (PsiElement specifier : QinPsiTree.descendantsOfType(importDeclaration, QinTokenTypes.IMPORT_SPECIFIER)) {
            ImportBinding binding = parseImportBinding(specifier, moduleSpecifier);
            if (binding != null) {
                bindings.add(binding);
            }
        }
        return bindings;
    }

    private static @Nullable ImportBinding parseImportBinding(
            @NotNull PsiElement specifier,
            @NotNull String moduleSpecifier) {
        PsiElement exportedName = QinPsiTree.firstDescendantOfType(specifier, QinTokenTypes.REFERENCE_IDENTIFIER);
        if (exportedName == null) {
            return null;
        }
        PsiElement localAlias = QinPsiTree.firstDescendantOfType(specifier, QinTokenTypes.IMPORT_ALIAS_NAME);
        String localName = localAlias == null ? exportedName.getText() : localAlias.getText();
        return new ImportBinding(moduleSpecifier, exportedName.getText(), localName);
    }

    private static @Nullable String readModuleSpecifier(@NotNull PsiElement importDeclaration) {
        PsiElement moduleString = QinPsiTree.firstDescendantOfType(importDeclaration, QinTokenTypes.STRING);
        if (moduleString == null) {
            return null;
        }
        String text = moduleString.getText();
        if (text.length() < 2) {
            return null;
        }
        return text.substring(1, text.length() - 1).trim();
    }

    record ImportBinding(@NotNull String moduleSpecifier, @NotNull String exportedName, @NotNull String localName) {
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
