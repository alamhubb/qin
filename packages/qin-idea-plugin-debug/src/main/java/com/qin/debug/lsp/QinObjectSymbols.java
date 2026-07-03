package com.qin.debug.lsp;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class QinObjectSymbols {
    private QinObjectSymbols() {
    }

    static @Nullable PsiElement findObjectName(@NotNull PsiElement element, @NotNull String name) {
        PsiFile file = element.getContainingFile();
        if (file == null) {
            return null;
        }
        PsiElement sameFileObject = findObjectNameInFile(file, name);
        if (sameFileObject != null) {
            return sameFileObject;
        }

        QinModuleImportTable importTable = QinModuleImportTable.fromFile(file);
        QinModuleImportTable.QinImport qinImport = importTable.find(name);
        if (qinImport == null) {
            return null;
        }
        VirtualFile importedFile = importTable.resolveFile(qinImport);
        if (importedFile == null) {
            return null;
        }
        GlobalSearchScope importedFileScope = GlobalSearchScope.fileScope(element.getProject(), importedFile);
        if (!FileBasedIndex.getInstance().getContainingFiles(
                QinObjectNameIndex.NAME,
                qinImport.exportedName(),
                importedFileScope).contains(importedFile)) {
            return null;
        }
        PsiFile importedPsiFile = PsiManager.getInstance(element.getProject()).findFile(importedFile);
        return importedPsiFile == null ? null : findObjectNameInFile(importedPsiFile, qinImport.exportedName());
    }

    private static @Nullable PsiElement findObjectNameInFile(@NotNull PsiFile file, @NotNull String name) {
        ObjectNameVisitor visitor = new ObjectNameVisitor(name);
        file.accept(visitor);
        return visitor.objectName;
    }

    static @Nullable PsiElement findMethodName(
            @NotNull PsiElement element,
            @NotNull String objectName,
            @NotNull String methodName) {
        PsiElement objectNameElement = findObjectName(element, objectName);
        if (objectNameElement == null) {
            return null;
        }
        PsiElement objectDeclaration = parentOfType(objectNameElement, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return null;
        }
        MethodNameVisitor visitor = new MethodNameVisitor(methodName);
        objectDeclaration.accept(visitor);
        return visitor.methodName;
    }

    static @Nullable PsiElement findMethodNameForThis(@NotNull PsiElement element, @NotNull String methodName) {
        PsiElement objectDeclaration = parentOfType(element, QinTokenTypes.OBJECT_DECLARATION);
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
        PsiElement objectNameElement = findObjectName(element, objectName);
        if (objectNameElement == null) {
            return List.of();
        }
        PsiElement objectDeclaration = parentOfType(objectNameElement, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return List.of();
        }
        return memberElementsInObjectDeclaration(objectDeclaration);
    }

    static @NotNull List<String> memberNamesForThis(@NotNull PsiElement element) {
        return memberElementsForThis(element).stream()
                .map(PsiElement::getText)
                .toList();
    }

    static @NotNull List<PsiElement> memberElementsForThis(@NotNull PsiElement element) {
        PsiElement objectDeclaration = parentOfType(element, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return List.of();
        }
        return memberElementsInObjectDeclaration(objectDeclaration);
    }

    private static @NotNull List<PsiElement> memberElementsInObjectDeclaration(@NotNull PsiElement objectDeclaration) {
        MemberNameVisitor visitor = new MemberNameVisitor();
        objectDeclaration.accept(visitor);
        return visitor.members;
    }

    private static @Nullable PsiElement findMethodNameInObjectDeclaration(
            @NotNull PsiElement objectDeclaration,
            @NotNull String methodName) {
        MethodNameVisitor visitor = new MethodNameVisitor(methodName);
        objectDeclaration.accept(visitor);
        return visitor.methodName;
    }

    static @Nullable PsiElement findFieldName(
            @NotNull PsiElement element,
            @NotNull String objectName,
            @NotNull String fieldName) {
        PsiElement objectNameElement = findObjectName(element, objectName);
        if (objectNameElement == null) {
            return null;
        }
        PsiElement objectDeclaration = parentOfType(objectNameElement, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return null;
        }
        return findFieldNameInObjectDeclaration(objectDeclaration, fieldName);
    }

    static @Nullable PsiElement findFieldNameForThis(@NotNull PsiElement element, @NotNull String fieldName) {
        PsiElement objectDeclaration = parentOfType(element, QinTokenTypes.OBJECT_DECLARATION);
        if (objectDeclaration == null) {
            return null;
        }
        return findFieldNameInObjectDeclaration(objectDeclaration, fieldName);
    }

    private static @Nullable PsiElement findFieldNameInObjectDeclaration(
            @NotNull PsiElement objectDeclaration,
            @NotNull String fieldName) {
        FieldNameVisitor visitor = new FieldNameVisitor(fieldName);
        objectDeclaration.accept(visitor);
        return visitor.fieldName;
    }

    private static @Nullable PsiElement parentOfType(@NotNull PsiElement element, @NotNull IElementType type) {
        PsiElement current = element.getParent();
        while (current != null) {
            if (current.getNode() != null && current.getNode().getElementType() == type) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private static final class ObjectNameVisitor extends PsiRecursiveElementWalkingVisitor {
        private final String name;
        private PsiElement objectName;

        private ObjectNameVisitor(@NotNull String name) {
            this.name = name;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (objectName != null) {
                return;
            }
            if (element.getNode() != null
                    && element.getNode().getElementType() == QinTokenTypes.OBJECT_NAME
                    && name.equals(element.getText())) {
                objectName = element;
                return;
            }
            super.visitElement(element);
        }
    }

    private static final class MethodNameVisitor extends PsiRecursiveElementWalkingVisitor {
        private final String name;
        private PsiElement methodName;

        private MethodNameVisitor(@NotNull String name) {
            this.name = name;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (methodName != null) {
                return;
            }
            if (element.getNode() != null
                    && element.getNode().getElementType() == QinTokenTypes.METHOD_NAME
                    && name.equals(element.getText())) {
                methodName = element;
                return;
            }
            super.visitElement(element);
        }
    }

    private static final class FieldNameVisitor extends PsiRecursiveElementWalkingVisitor {
        private final String name;
        private PsiElement fieldName;

        private FieldNameVisitor(@NotNull String name) {
            this.name = name;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (fieldName != null) {
                return;
            }
            if (element.getNode() != null
                    && element.getNode().getElementType() == QinTokenTypes.FIELD_NAME
                    && name.equals(element.getText())) {
                fieldName = element;
                return;
            }
            super.visitElement(element);
        }
    }

    private static final class MemberNameVisitor extends PsiRecursiveElementWalkingVisitor {
        private final List<PsiElement> members = new ArrayList<>();

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (element.getNode() != null
                    && (element.getNode().getElementType() == QinTokenTypes.FIELD_NAME
                    || element.getNode().getElementType() == QinTokenTypes.METHOD_NAME)
                    && members.stream().noneMatch(member -> member.getText().equals(element.getText()))) {
                members.add(element);
                return;
            }
            super.visitElement(element);
        }
    }
}
