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
        QinSourceStructure sourceStructure = QinSourceStructure.parse(file.getText());
        for (QinSourceStructure.ImportDeclaration declaration : sourceStructure.importDeclarations()) {
            for (QinSourceStructure.ImportSpecifier specifier : declaration.specifiers()) {
                bindings.add(new ImportBinding(
                        declaration.moduleSpecifier(),
                        specifier.exportedName(),
                        specifier.localName()));
            }
        }
        return bindings;
    }

    static @Nullable ImportBinding findForSpecifierElement(@NotNull PsiElement element) {
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        int offset = element.getTextRange().getStartOffset();
        QinSourceStructure sourceStructure = QinSourceStructure.parse(file.getText());
        for (QinSourceStructure.ImportDeclaration declaration : sourceStructure.importDeclarations()) {
            for (QinSourceStructure.ImportSpecifier specifier : declaration.specifiers()) {
                if (specifier.exportedNameRange().startOffset() == offset
                        || specifier.localNameRange().startOffset() == offset) {
                    return new ImportBinding(
                            declaration.moduleSpecifier(),
                            specifier.exportedName(),
                            specifier.localName());
                }
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
        QinSourceStructure sourceStructure = QinSourceStructure.parse(file.getText());
        for (QinSourceStructure.ImportDeclaration declaration : sourceStructure.importDeclarations()) {
            for (QinSourceStructure.ImportSpecifier specifier : declaration.specifiers()) {
                if (!specifier.localNameRange().isPresent()
                        || !specifier.localName().equals(localName)) {
                    continue;
                }
                return QinPsiTree.elementAtOrParentOfType(
                        file,
                        specifier.localNameRange().startOffset(),
                        QinTokenTypes.IMPORT_ALIAS_NAME);
            }
        }
        return null;
    }

    record ImportBinding(@NotNull String moduleSpecifier, @NotNull String exportedName, @NotNull String localName) {
    }
}
