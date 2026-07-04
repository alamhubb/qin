package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

final class QinImportBindings {
    private QinImportBindings() {
    }

    static @NotNull List<ImportBinding> collect(@NotNull PsiFile file) {
        return QinPsiTree.importSpecifierMatches(file).stream()
                .map(match -> new ImportBinding(
                        match.declaration().moduleSpecifier(),
                        match.specifier().exportedName(),
                        match.specifier().localName()))
                .toList();
    }

    static @Nullable ImportBinding findForSpecifierElement(@NotNull PsiElement element) {
        QinSourceStructure.ImportSpecifierMatch match = QinPsiTree.importSpecifierMatchAtNameElement(element);
        if (match == null) {
            return null;
        }
        return new ImportBinding(
                match.declaration().moduleSpecifier(),
                match.specifier().exportedName(),
                match.specifier().localName());
    }

    static boolean isAliasedLocalSpecifierElement(@NotNull PsiElement element) {
        ImportBinding binding = findForSpecifierElement(element);
        return binding != null
                && binding.localName().equals(QinReferenceElements.referenceName(element))
                && !binding.exportedName().equals(binding.localName());
    }

    static @Nullable PsiElement findAliasName(@NotNull PsiElement element, @NotNull String localName) {
        PsiFile file = QinPsiTree.containingFile(element);
        if (file == null) {
            return null;
        }
        QinSourceStructure.ImportSpecifier specifier = QinPsiTree.importAliasSpecifierNamed(file, localName);
        if (specifier == null) {
            return null;
        }
        return QinPsiTree.importAliasNameElement(file, specifier);
    }

    static @Nullable PsiElement findExportedName(@NotNull PsiElement element, @NotNull String localName) {
        PsiFile file = QinPsiTree.containingFile(element);
        if (file == null) {
            return null;
        }
        QinSourceStructure.ImportSpecifier specifier = QinPsiTree.importAliasSpecifierNamed(file, localName);
        if (specifier == null) {
            return null;
        }
        return QinPsiTree.importExportedNameElement(file, specifier);
    }

    record ImportBinding(@NotNull String moduleSpecifier, @NotNull String exportedName, @NotNull String localName) {
    }
}
