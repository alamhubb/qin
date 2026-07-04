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
        return QinPsiTree.sourceStructure(file).importSpecifierMatches().stream()
                .map(match -> new ImportBinding(
                        match.declaration().moduleSpecifier(),
                        match.specifier().exportedName(),
                        match.specifier().localName()))
                .toList();
    }

    static @Nullable ImportBinding findForSpecifierElement(@NotNull PsiElement element) {
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        int offset = element.getTextRange().getStartOffset();
        QinSourceStructure sourceStructure = QinPsiTree.sourceStructure(file);
        QinSourceStructure.ImportSpecifierMatch match = sourceStructure.importSpecifierAtNameOffset(offset);
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
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        QinSourceStructure sourceStructure = QinPsiTree.sourceStructure(file);
        QinSourceStructure.ImportSpecifier specifier = sourceStructure.importAliasSpecifierNamed(localName);
        if (specifier == null) {
            return null;
        }
        return QinPsiTree.importAliasNameElement(file, specifier);
    }

    static @Nullable PsiElement findExportedName(@NotNull PsiElement element, @NotNull String localName) {
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        QinSourceStructure sourceStructure = QinPsiTree.sourceStructure(file);
        QinSourceStructure.ImportSpecifier specifier = sourceStructure.importAliasSpecifierNamed(localName);
        if (specifier == null) {
            return null;
        }
        return QinPsiTree.importExportedNameElement(file, specifier);
    }

    record ImportBinding(@NotNull String moduleSpecifier, @NotNull String exportedName, @NotNull String localName) {
    }
}
