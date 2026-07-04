package com.qin.debug.lsp;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
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
        QinSourceStructure.ObjectDeclaration declaration = QinSourceStructure.parse(file.getText()).objectDeclarationNamed(name);
        if (declaration == null || !declaration.nameRange().isPresent()) {
            return null;
        }
        return QinPsiTree.elementAtRangeOrParentOfType(
                file,
                declaration.nameRange(),
                QinTokenTypes.OBJECT_NAME);
    }

    static @Nullable PsiElement findMethodName(
            @NotNull PsiElement element,
            @NotNull String objectName,
            @NotNull String methodName) {
        ResolvedObject resolvedObject = resolveObjectName(element, objectName);
        if (resolvedObject == null
                || !hasIndexedMember(element, resolvedObject, methodName, QinSourceStructure.ObjectMemberKind.METHOD)) {
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
                .filter(member -> hasIndexedMember(
                        element,
                        resolvedObject,
                        member.element().getText(),
                        member.kind()))
                .map(ObjectMemberElement::element)
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
        return memberElementsInObjectDeclaration(objectDeclaration).stream()
                .map(ObjectMemberElement::element)
                .toList();
    }

    private static @NotNull List<ObjectMemberElement> memberElementsInObjectDeclaration(
            @NotNull PsiElement objectDeclaration) {
        PsiFile file = objectDeclaration.getContainingFile();
        QinSourceStructure.ObjectDeclaration declaration = sourceObjectDeclaration(objectDeclaration);
        if (file == null || declaration == null) {
            return List.of();
        }

        List<ObjectMemberElement> members = new ArrayList<>();
        for (QinSourceStructure.ObjectMemberDeclaration member : declaration.memberDeclarations()) {
            PsiElement memberElement = QinPsiTree.objectMemberNameElement(
                    file, member.declaration(), member.kind());
            if (memberElement != null) {
                members.add(new ObjectMemberElement(memberElement, member.kind()));
            }
        }
        return members;
    }

    private static @Nullable QinSourceStructure.ObjectDeclaration sourceObjectDeclaration(
            @NotNull PsiElement objectDeclaration) {
        PsiFile file = objectDeclaration.getContainingFile();
        if (file == null) {
            return null;
        }
        int startOffset = objectDeclaration.getTextRange().getStartOffset();
        return QinSourceStructure.parse(file.getText()).objectDeclarationAtKeywordOffset(startOffset);
    }

    private static @Nullable PsiElement findMemberNameInObjectDeclaration(
            @NotNull PsiElement objectDeclaration,
            @NotNull String memberName,
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        PsiFile file = objectDeclaration.getContainingFile();
        QinSourceStructure.ObjectDeclaration declaration = sourceObjectDeclaration(objectDeclaration);
        if (file == null || declaration == null) {
            return null;
        }
        QinSourceStructure.MemberDeclaration member = declaration.memberDeclarationNamed(memberName, kind);
        if (member == null) {
            return null;
        }
        return QinPsiTree.objectMemberNameElement(file, member, kind);
    }

    private static @Nullable PsiElement findMethodNameInObjectDeclaration(
            @NotNull PsiElement objectDeclaration,
            @NotNull String methodName) {
        return findMemberNameInObjectDeclaration(
                objectDeclaration,
                methodName,
                QinSourceStructure.ObjectMemberKind.METHOD);
    }

    static @Nullable PsiElement findFieldName(
            @NotNull PsiElement element,
            @NotNull String objectName,
            @NotNull String fieldName) {
        ResolvedObject resolvedObject = resolveObjectName(element, objectName);
        if (resolvedObject == null
                || !hasIndexedMember(element, resolvedObject, fieldName, QinSourceStructure.ObjectMemberKind.FIELD)) {
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
        return findMemberNameInObjectDeclaration(
                objectDeclaration,
                fieldName,
                QinSourceStructure.ObjectMemberKind.FIELD);
    }

    private static boolean hasIndexedMember(
            @NotNull PsiElement element,
            @NotNull ResolvedObject resolvedObject,
            @NotNull String memberName,
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
        VirtualFile indexedFile = resolvedObject.importedFile();
        if (indexedFile == null) {
            return true;
        }
        GlobalSearchScope importedFileScope = GlobalSearchScope.fileScope(element.getProject(), indexedFile);
        String key = QinSourceStructure.objectMemberKey(resolvedObject.indexedObjectName(), memberName);
        Collection<QinPsiFile> indexedFiles = StubIndex.getElements(
                QinObjectMemberStubIndexes.keyFor(kind),
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

    private record ObjectMemberElement(
            @NotNull PsiElement element,
            @NotNull QinSourceStructure.ObjectMemberKind kind) {
    }

}
