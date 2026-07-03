package com.qin.debug.lsp;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

final class QinJavaReference extends PsiPolyVariantReferenceBase<PsiElement> {
    private final String identifier;

    QinJavaReference(@NotNull PsiElement element) {
        super(element, TextRange.from(0, element.getTextLength()));
        this.identifier = element.getText();
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return ResolveCache.getInstance(myElement.getProject()).resolveWithCaching(
                this,
                QinJavaReference::resolveInner,
                false,
                incompleteCode);
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 0 ? null : results[0].getElement();
    }

    @Override
    public @NotNull PsiElement handleElementRename(@NotNull @NlsSafe String newElementName)
            throws IncorrectOperationException {
        ASTNode leaf = myElement.getNode().getFirstChildNode();
        if (leaf instanceof LeafElement leafElement) {
            leafElement.replaceWithText(newElementName);
            return myElement;
        }
        throw new IncorrectOperationException("Cannot rename Qin Java reference without a leaf token: " + myElement);
    }

    private static ResolveResult @NotNull [] resolveInner(
            @NotNull QinJavaReference reference,
            boolean incompleteCode) {
        PsiElement element = reference.getElement();
        QinJavaImportTable importTable = QinJavaImportTable.fromFile(element.getContainingFile());
        String qualifier = previousQualifierName(element);
        if (qualifier != null) {
            QinJavaImportTable.JavaImport importedClass = importTable.find(qualifier);
            if (importedClass == null) {
                return ResolveResult.EMPTY_ARRAY;
            }
            PsiClass psiClass = reference.findClass(importedClass.qualifiedClassName());
            if (psiClass == null) {
                return ResolveResult.EMPTY_ARRAY;
            }
            return reference.resolveClassMember(psiClass);
        }

        QinJavaImportTable.JavaImport specifierImport = QinJavaImportTable.findForSpecifierElement(element);
        if (specifierImport != null) {
            PsiClass psiClass = reference.findClass(specifierImport.qualifiedClassName());
            return psiClass == null
                    ? ResolveResult.EMPTY_ARRAY
                    : new ResolveResult[]{new QinPsiResolveResult(psiClass)};
        }

        QinJavaImportTable.JavaImport importedClass = importTable.find(reference.identifier);
        if (importedClass == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        PsiClass psiClass = reference.findClass(importedClass.qualifiedClassName());
        return psiClass == null
                ? ResolveResult.EMPTY_ARRAY
                : new ResolveResult[]{new QinPsiResolveResult(psiClass)};
    }

    private ResolveResult @NotNull [] resolveClassMember(@NotNull PsiClass psiClass) {
        List<ResolveResult> results = new ArrayList<>();
        for (PsiMethod method : psiClass.findMethodsByName(identifier, true)) {
            if (method.hasModifierProperty(PsiModifier.STATIC)) {
                results.add(new QinPsiResolveResult(method));
            }
        }
        PsiField field = psiClass.findFieldByName(identifier, true);
        if (field != null && field.hasModifierProperty(PsiModifier.STATIC)) {
            results.add(new QinPsiResolveResult(field));
        }
        return results.toArray(ResolveResult.EMPTY_ARRAY);
    }

    @Nullable
    private PsiClass findClass(@NotNull String qualifiedName) {
        Project project = myElement.getProject();
        return JavaPsiFacade.getInstance(project).findClass(
                qualifiedName,
                GlobalSearchScope.allScope(project));
    }

    static @Nullable String previousQualifierName(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent != null
                && parent.getNode() != null
                && parent.getNode().getElementType() == QinTokenTypes.MEMBER_ACCESS) {
            return QinPsiTokenStream.previousQualifierName(parent, element);
        }
        return null;
    }

    static boolean isJavaReferenceCandidate(@NotNull PsiElement element) {
        QinJavaImportTable importTable = QinJavaImportTable.fromFile(element.getContainingFile());
        String qualifier = previousQualifierName(element);
        if (qualifier != null) {
            return importTable.find(qualifier) != null;
        }
        if (QinJavaImportTable.findForSpecifierElement(element) != null) {
            return true;
        }
        return importTable.find(element.getText()) != null;
    }
}
