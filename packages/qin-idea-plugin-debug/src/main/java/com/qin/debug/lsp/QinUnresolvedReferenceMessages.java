package com.qin.debug.lsp;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class QinUnresolvedReferenceMessages {
    private QinUnresolvedReferenceMessages() {
    }

    static @Nullable String messageFor(@NotNull PsiElement element) {
        if (!isReferenceIdentifier(element)) {
            return null;
        }
        String javaMessage = javaMessageFor(element);
        if (javaMessage != null) {
            return javaMessage;
        }
        String methodMessage = objectMethodMessageFor(element);
        if (methodMessage != null) {
            return methodMessage;
        }
        return objectFieldMessageFor(element);
    }

    static @Nullable String javaMessageFor(@NotNull PsiElement element) {
        if (!isReferenceIdentifier(element)) {
            return null;
        }
        QinJavaImportTable importTable = QinJavaImportTable.fromFile(element.getContainingFile());
        String qualifier = QinReferenceElements.previousQualifierName(element);
        if (qualifier != null) {
            QinJavaImportTable.JavaImport importedClass = importTable.find(qualifier);
            if (importedClass == null
                    || QinPsiReferences.unresolvedReferenceOfType(element, QinJavaReference.class) == null) {
                return null;
            }
            return "Unresolved static Java member " + importedClass.qualifiedClassName() + "." + element.getText();
        }

        QinJavaImportTable.JavaImport importedClass = importTable.find(element.getText());
        if (importedClass == null
                || QinPsiReferences.unresolvedReferenceOfType(element, QinJavaReference.class) == null) {
            return null;
        }
        return "Unresolved Java class " + importedClass.qualifiedClassName();
    }

    static @Nullable String objectMethodMessageFor(@NotNull PsiElement element) {
        if (!isReferenceIdentifier(element)
                || QinJavaReference.isJavaReferenceCandidate(element)
                || QinPsiReferences.unresolvedReferenceOfType(element, QinObjectMethodReference.class) == null) {
            return null;
        }
        String objectName = QinReferenceElements.previousQualifierName(element);
        return "Unresolved Qin object method " + objectName + "." + element.getText();
    }

    static @Nullable String objectFieldMessageFor(@NotNull PsiElement element) {
        if (!isReferenceIdentifier(element)
                || QinJavaReference.isJavaReferenceCandidate(element)
                || QinPsiTokenStream.isFollowedByCallParenthesis(element)) {
            return null;
        }
        String qualifier = QinReferenceElements.previousQualifierName(element);
        if (qualifier == null
                || QinPsiReferences.unresolvedReferenceOfType(element, QinObjectFieldReference.class) == null) {
            return null;
        }
        return "Unresolved Qin object field " + qualifier + "." + element.getText();
    }

    private static boolean isReferenceIdentifier(@NotNull PsiElement element) {
        return element.getContainingFile() instanceof QinPsiFile
                && QinReferenceElements.isReferenceIdentifier(element);
    }
}
