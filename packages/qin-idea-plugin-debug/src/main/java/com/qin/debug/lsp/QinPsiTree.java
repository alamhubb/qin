package com.qin.debug.lsp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinPsiTree {
    private QinPsiTree() {
    }

    static boolean isType(@NotNull PsiElement element, @NotNull IElementType type) {
        return element.getNode() != null && element.getNode().getElementType() == type;
    }

    static @Nullable PsiElement elementAt(@NotNull PsiFile file, int offset) {
        return file.findElementAt(offset);
    }

    static @Nullable PsiFile psiFile(@NotNull Project project, @NotNull VirtualFile file) {
        return PsiManager.getInstance(project).findFile(file);
    }

    static @NotNull QinSourceStructure sourceStructure(@NotNull PsiFile file) {
        return QinSourceStructure.parse(file.getText());
    }

    static @Nullable PsiElement parentOfType(@NotNull PsiElement element, @NotNull IElementType type) {
        PsiElement current = element.getParent();
        while (current != null) {
            if (isType(current, type)) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    static @Nullable PsiElement elementAtOrParentOfType(
            @NotNull PsiFile file,
            int offset,
            @NotNull IElementType type) {
        PsiElement element = elementAt(file, offset);
        if (element == null) {
            return null;
        }
        return isType(element, type) ? element : parentOfType(element, type);
    }

    static @Nullable PsiElement elementAtRangeOrParentOfType(
            @NotNull PsiFile file,
            @NotNull QinSourceStructure.SourceRange range,
            @NotNull IElementType type) {
        return range.isPresent() ? elementAtOrParentOfType(file, range.startOffset(), type) : null;
    }

    static @Nullable PsiElement importAliasNameElement(
            @NotNull PsiFile file,
            @NotNull QinSourceStructure.ImportSpecifier specifier) {
        return elementAtRangeOrParentOfType(file, specifier.localNameRange(), QinTokenTypes.IMPORT_ALIAS_NAME);
    }

    static @Nullable PsiElement importExportedNameElement(
            @NotNull PsiFile file,
            @NotNull QinSourceStructure.ImportSpecifier specifier) {
        return elementAtRangeOrParentOfType(file, specifier.exportedNameRange(), QinTokenTypes.REFERENCE_IDENTIFIER);
    }

    static @Nullable QinSourceStructure.ImportSpecifierMatch importSpecifierMatchAtNameElement(
            @NotNull PsiElement element) {
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        int offset = element.getTextRange().getStartOffset();
        return sourceStructure(file).importSpecifierAtNameOffset(offset);
    }

    static @Nullable PsiElement objectNameElement(
            @NotNull PsiFile file,
            @NotNull QinSourceStructure.ObjectDeclaration declaration) {
        return elementAtRangeOrParentOfType(file, declaration.nameRange(), QinTokenTypes.OBJECT_NAME);
    }

    static @Nullable PsiElement containingObjectDeclaration(@NotNull PsiElement element) {
        return parentOfType(element, QinTokenTypes.OBJECT_DECLARATION);
    }

    static @Nullable PsiElement objectMemberNameElement(
            @NotNull PsiFile file,
            @NotNull QinSourceStructure.MemberDeclaration member,
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        return elementAtRangeOrParentOfType(file, member.nameRange(), objectMemberNameType(kind));
    }

    static @Nullable QinSourceStructure.ObjectDeclaration sourceObjectDeclaration(
            @NotNull PsiElement objectDeclaration) {
        PsiFile file = objectDeclaration.getContainingFile();
        if (file == null) {
            return null;
        }
        int startOffset = objectDeclaration.getTextRange().getStartOffset();
        return sourceStructure(file).objectDeclarationAtKeywordOffset(startOffset);
    }

    private static @NotNull IElementType objectMemberNameType(
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        return kind == QinSourceStructure.ObjectMemberKind.FIELD
                ? QinTokenTypes.FIELD_NAME
                : QinTokenTypes.METHOD_NAME;
    }
}
