package com.qin.debug.lsp;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

final class QinPsiTree {
    private QinPsiTree() {
    }

    static @Nullable IElementType elementType(@NotNull PsiElement element) {
        return element.getNode() == null ? null : element.getNode().getElementType();
    }

    static boolean isType(@NotNull PsiElement element, @NotNull IElementType type) {
        return elementType(element) == type;
    }

    static @Nullable PsiElement firstChild(@NotNull PsiElement element) {
        return element.getFirstChild();
    }

    static @Nullable PsiElement parent(@NotNull PsiElement element) {
        return element.getParent();
    }

    static boolean isLeaf(@NotNull PsiElement element) {
        return firstChild(element) == null;
    }

    static @NotNull TextRange elementRange(@NotNull PsiElement element) {
        return element.getTextRange();
    }

    static @NotNull String elementText(@NotNull PsiElement element) {
        return element.getText();
    }

    static int elementTextLength(@NotNull PsiElement element) {
        return element.getTextLength();
    }

    static @Nullable PsiElement elementAt(@NotNull PsiFile file, int offset) {
        return file.findElementAt(offset);
    }

    static @Nullable PsiFile psiFile(@NotNull Project project, @NotNull VirtualFile file) {
        return PsiManager.getInstance(project).findFile(file);
    }

    static @NotNull TextRange referenceRangeInFile(@NotNull PsiReference reference) {
        return reference.getRangeInElement()
                .shiftRight(reference.getElement().getTextRange().getStartOffset());
    }

    static @Nullable PsiFile containingFile(@NotNull PsiElement element) {
        return element.getContainingFile();
    }

    static boolean isQinFile(@NotNull PsiElement element) {
        return containingFile(element) instanceof QinPsiFile;
    }

    static @NotNull QinSourceStructure sourceStructure(@NotNull PsiFile file) {
        return QinSourceStructure.parse(file.getText());
    }

    static @Nullable PsiElement parentOfType(@NotNull PsiElement element, @NotNull IElementType type) {
        PsiElement current = parent(element);
        while (current != null) {
            if (isType(current, type)) {
                return current;
            }
            current = parent(current);
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
        PsiFile file = containingFile(element);
        if (file == null) {
            return null;
        }
        int offset = element.getTextRange().getStartOffset();
        return sourceStructure(file).importSpecifierAtNameOffset(offset);
    }

    static @NotNull List<QinSourceStructure.ImportSpecifierMatch> importSpecifierMatches(@NotNull PsiFile file) {
        return sourceStructure(file).importSpecifierMatches();
    }

    static @Nullable QinSourceStructure.ImportSpecifier importAliasSpecifierNamed(
            @NotNull PsiFile file,
            @NotNull String localName) {
        return sourceStructure(file).importAliasSpecifierNamed(localName);
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
        PsiFile file = containingFile(objectDeclaration);
        if (file == null) {
            return null;
        }
        int startOffset = objectDeclaration.getTextRange().getStartOffset();
        return sourceStructure(file).objectDeclarationAtKeywordOffset(startOffset);
    }

    static @Nullable QinSourceStructure.ObjectDeclaration sourceObjectDeclarationNamed(
            @NotNull PsiFile file,
            @NotNull String name) {
        return sourceStructure(file).objectDeclarationNamed(name);
    }

    static @NotNull List<QinSourceStructure.ObjectMemberDeclaration> sourceObjectMemberDeclarations(
            @NotNull QinSourceStructure.ObjectDeclaration declaration) {
        return declaration.memberDeclarations();
    }

    static @Nullable QinSourceStructure.MemberDeclaration sourceObjectMemberDeclarationNamed(
            @NotNull QinSourceStructure.ObjectDeclaration declaration,
            @NotNull String memberName,
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        return declaration.memberDeclarationNamed(memberName, kind);
    }

    private static @NotNull IElementType objectMemberNameType(
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        return kind == QinSourceStructure.ObjectMemberKind.FIELD
                ? QinTokenTypes.FIELD_NAME
                : QinTokenTypes.METHOD_NAME;
    }
}
