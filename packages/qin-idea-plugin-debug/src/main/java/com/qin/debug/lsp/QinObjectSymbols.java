package com.qin.debug.lsp;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class QinObjectSymbols {
    private QinObjectSymbols() {
    }

    static @Nullable PsiElement findObjectName(@NotNull PsiElement element, @NotNull String name) {
        ResolvedObject resolvedObject = resolveObjectName(element, name);
        return resolvedObject == null ? null : resolvedObject.objectName();
    }

    private static @Nullable ResolvedObject resolveObjectName(@NotNull PsiElement element, @NotNull String name) {
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        PsiElement sameFileObject = findObjectNameInFile(file, name);
        if (sameFileObject != null) {
            return new ResolvedObject(sameFileObject, name, null);
        }

        QinModuleImportTable importTable = QinModuleImportTable.fromFile(file);
        QinImportBindings.ImportBinding importBinding = QinImportBindings.findForSpecifierElement(element);
        if (importBinding != null
                && importBinding.exportedName().equals(name)
                && QinModuleImportTable.isQinModuleImportSpecifier(importBinding)) {
            VirtualFile importedFile = importTable.resolveFile(new QinModuleImportTable.QinImport(
                    importBinding.moduleSpecifier(),
                    importBinding.exportedName(),
                    importBinding.localName()));
            return importedFile == null ? null : resolveImportedObjectName(
                    element,
                    importedFile,
                    importBinding.exportedName());
        }

        QinModuleImportTable.QinImport qinImport = importTable.find(name);
        if (qinImport == null) {
            return null;
        }
        VirtualFile importedFile = importTable.resolveFile(qinImport);
        if (importedFile == null) {
            return null;
        }
        return resolveImportedObjectName(element, importedFile, qinImport.exportedName());
    }

    private static @Nullable ResolvedObject resolveImportedObjectName(
            @NotNull PsiElement element,
            @NotNull VirtualFile importedFile,
            @NotNull String exportedName) {
        GlobalSearchScope importedFileScope = GlobalSearchScope.fileScope(element.getProject(), importedFile);
        Collection<QinPsiFile> indexedFiles = StubIndex.getElements(
                QinObjectNameStubIndex.KEY,
                exportedName,
                element.getProject(),
                importedFileScope,
                QinPsiFile.class);
        if (indexedFiles.stream().noneMatch(indexedFile -> importedFile.equals(indexedFile.getVirtualFile()))) {
            return null;
        }
        PsiFile importedPsiFile = PsiManager.getInstance(element.getProject()).findFile(importedFile);
        PsiElement objectName = importedPsiFile == null ? null : findObjectNameInFile(importedPsiFile, exportedName);
        return objectName == null ? null : new ResolvedObject(objectName, exportedName, importedFile);
    }

    private static @Nullable PsiElement findObjectNameInFile(@NotNull PsiFile file, @NotNull String name) {
        return QinPsiTree.firstDescendantOfType(file, QinTokenTypes.OBJECT_NAME, name);
    }

    static @Nullable PsiElement findMethodName(
            @NotNull PsiElement element,
            @NotNull String objectName,
            @NotNull String methodName) {
        ResolvedObject resolvedObject = resolveObjectName(element, objectName);
        if (resolvedObject == null || !hasIndexedMember(element, resolvedObject, methodName, MemberKind.METHOD)) {
            return null;
        }
        PsiElement objectDeclaration = QinPsiTree.parentOfType(resolvedObject.objectName(), QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return null;
        }
        return findMethodNameInObjectDeclaration(objectDeclaration, methodName);
    }

    static @Nullable PsiElement findMethodNameForThis(@NotNull PsiElement element, @NotNull String methodName) {
        PsiElement objectDeclaration = QinPsiTree.parentOfType(element, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return null;
        }
        return findMethodNameInObjectDeclaration(objectDeclaration, methodName);
    }

    static @NotNull List<String> memberNamesForObject(@NotNull PsiElement element, @NotNull String objectName) {
        return memberElementsForObject(element, objectName).stream()
                .map(PsiElement::getText)
                .toList();
    }

    static @NotNull List<PsiElement> memberElementsForObject(@NotNull PsiElement element, @NotNull String objectName) {
        ResolvedObject resolvedObject = resolveObjectName(element, objectName);
        if (resolvedObject == null) {
            return List.of();
        }
        PsiElement objectDeclaration = QinPsiTree.parentOfType(resolvedObject.objectName(), QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return List.of();
        }
        return memberElementsInObjectDeclaration(objectDeclaration).stream()
                .filter(member -> hasIndexedMember(element, resolvedObject, member.getText(), memberKind(member)))
                .toList();
    }

    static @NotNull List<String> memberNamesForThis(@NotNull PsiElement element) {
        return memberElementsForThis(element).stream()
                .map(PsiElement::getText)
                .toList();
    }

    static @NotNull List<PsiElement> memberElementsForThis(@NotNull PsiElement element) {
        PsiElement objectDeclaration = QinPsiTree.parentOfType(element, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return List.of();
        }
        return memberElementsInObjectDeclaration(objectDeclaration);
    }

    private static @NotNull List<PsiElement> memberElementsInObjectDeclaration(@NotNull PsiElement objectDeclaration) {
        List<PsiElement> members = new ArrayList<>();
        for (PsiElement member : QinPsiTree.descendantsOfAnyType(
                objectDeclaration,
                QinTokenTypes.FIELD_NAME,
                QinTokenTypes.METHOD_NAME)) {
            if (members.stream().noneMatch(item -> item.getText().equals(member.getText()))) {
                members.add(member);
            }
        }
        return members;
    }

    private static @Nullable PsiElement findMethodNameInObjectDeclaration(
            @NotNull PsiElement objectDeclaration,
            @NotNull String methodName) {
        return QinPsiTree.firstDescendantOfType(objectDeclaration, QinTokenTypes.METHOD_NAME, methodName);
    }

    static @Nullable PsiElement findFieldName(
            @NotNull PsiElement element,
            @NotNull String objectName,
            @NotNull String fieldName) {
        ResolvedObject resolvedObject = resolveObjectName(element, objectName);
        if (resolvedObject == null || !hasIndexedMember(element, resolvedObject, fieldName, MemberKind.FIELD)) {
            return null;
        }
        PsiElement objectDeclaration = QinPsiTree.parentOfType(resolvedObject.objectName(), QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return null;
        }
        return findFieldNameInObjectDeclaration(objectDeclaration, fieldName);
    }

    static @Nullable PsiElement findFieldNameForThis(@NotNull PsiElement element, @NotNull String fieldName) {
        PsiElement objectDeclaration = QinPsiTree.parentOfType(element, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return null;
        }
        return findFieldNameInObjectDeclaration(objectDeclaration, fieldName);
    }

    private static @Nullable PsiElement findFieldNameInObjectDeclaration(
            @NotNull PsiElement objectDeclaration,
            @NotNull String fieldName) {
        return QinPsiTree.firstDescendantOfType(objectDeclaration, QinTokenTypes.FIELD_NAME, fieldName);
    }

    private static boolean hasIndexedMember(
            @NotNull PsiElement element,
            @NotNull ResolvedObject resolvedObject,
            @NotNull String memberName,
            @NotNull MemberKind kind) {
        VirtualFile indexedFile = resolvedObject.importedFile();
        if (indexedFile == null) {
            return true;
        }
        GlobalSearchScope importedFileScope = GlobalSearchScope.fileScope(element.getProject(), indexedFile);
        String key = QinFileElementType.memberKey(resolvedObject.indexedObjectName(), memberName);
        Collection<QinPsiFile> indexedFiles = StubIndex.getElements(
                kind == MemberKind.FIELD ? QinObjectFieldNameStubIndex.KEY : QinObjectMethodNameStubIndex.KEY,
                key,
                element.getProject(),
                importedFileScope,
                QinPsiFile.class);
        return indexedFiles.stream().anyMatch(file -> indexedFile.equals(file.getVirtualFile()));
    }

    private record ResolvedObject(
            @NotNull PsiElement objectName,
            @NotNull String indexedObjectName,
            @Nullable VirtualFile importedFile) {
    }

    private enum MemberKind {
        FIELD,
        METHOD
    }

    private static @NotNull MemberKind memberKind(@NotNull PsiElement member) {
        IElementType type = member.getNode() == null ? null : member.getNode().getElementType();
        return type == QinTokenTypes.FIELD_NAME ? MemberKind.FIELD : MemberKind.METHOD;
    }

}
