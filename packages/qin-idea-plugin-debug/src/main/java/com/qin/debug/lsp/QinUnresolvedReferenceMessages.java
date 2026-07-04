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
            if (javaClassReferenceIsUnresolved(element, qualifier)) {
                return null;
            }
            return "Unresolved static Java member "
                    + importedClass.qualifiedClassName()
                    + "."
                    + QinReferenceElements.referenceName(element);
        }

        QinJavaImportTable.JavaImport importedClass = QinImportBindings.isAliasedLocalSpecifierElement(element)
                ? null
                : QinJavaImportTable.findForSpecifierElement(element);
        if (importedClass == null) {
            importedClass = importTable.find(QinReferenceElements.referenceName(element));
        }
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
        return "Unresolved Qin object method "
                + objectName
                + "."
                + QinReferenceElements.referenceName(element);
    }

    static @Nullable String objectFieldMessageFor(@NotNull PsiElement element) {
        if (!isReferenceIdentifier(element)
                || QinJavaReference.isJavaReferenceCandidate(element)
                || QinReferenceElements.isFollowedByCallParenthesis(element)) {
            return null;
        }
        String qualifier = QinReferenceElements.previousQualifierName(element);
        if (qualifier == null
                || QinPsiReferences.unresolvedReferenceOfType(element, QinObjectFieldReference.class) == null) {
            return null;
        }
        return "Unresolved Qin object field "
                + qualifier
                + "."
                + QinReferenceElements.referenceName(element);
    }

    private static boolean javaClassReferenceIsUnresolved(@NotNull PsiElement memberElement, @NotNull String qualifier) {
        PsiElement qualifierElement = QinReferenceElements.previousQualifierElement(memberElement);
        if (qualifierElement == null) {
            return false;
        }
        PsiElement exportedName = QinImportBindings.findExportedName(qualifierElement, qualifier);
        PsiElement classElement = exportedName == null ? qualifierElement : exportedName;
        return QinPsiReferences.unresolvedReferenceOfType(classElement, QinJavaReference.class) != null;
    }

    private static boolean isReferenceIdentifier(@NotNull PsiElement element) {
        return QinPsiTree.isQinFile(element)
                && QinReferenceElements.isReferenceIdentifier(element);
    }
}
