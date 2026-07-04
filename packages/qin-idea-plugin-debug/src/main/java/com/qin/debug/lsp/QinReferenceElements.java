package com.qin.debug.lsp;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

final class QinReferenceElements {
    private QinReferenceElements() {
    }

    static @Nullable PsiElement referenceElement(@NotNull PsiElement element) {
        IElementType elementType = element.getNode() == null ? null : element.getNode().getElementType();
        if (isReferenceIdentifier(element)) {
            return element;
        }
        if (QinTokenFacts.isReferenceLeafToken(elementType)) {
            PsiElement parent = element.getParent();
            if (parent != null && isReferenceIdentifier(parent)) {
                return parent;
            }
        }
        return null;
    }

    static boolean isReferenceIdentifier(@NotNull PsiElement element) {
        return QinPsiTree.isType(element, QinTokenTypes.REFERENCE_IDENTIFIER);
    }

    static boolean isImportAliasDeclaration(@NotNull PsiElement element) {
        return QinPsiTree.isType(element, QinTokenTypes.IMPORT_ALIAS_NAME);
    }

    static void registerReferenceProvider(
            @NotNull PsiReferenceRegistrar registrar,
            @NotNull PsiReferenceProvider provider) {
        registerReferenceProvider(registrar, provider, QinTokenTypes.REFERENCE_IDENTIFIER);
        registerReferenceProvider(registrar, provider, QinTokenTypes.IDENTIFIER);
        registerReferenceProvider(registrar, provider, QinTokenTypes.CLASS_NAME);
        registerReferenceProvider(registrar, provider, QinTokenTypes.MEMBER_IDENTIFIER);
    }

    static void registerMemberReferenceProvider(
            @NotNull PsiReferenceRegistrar registrar,
            @NotNull PsiReferenceProvider provider) {
        registerReferenceProvider(registrar, provider, QinTokenTypes.REFERENCE_IDENTIFIER);
        registerReferenceProvider(registrar, provider, QinTokenTypes.MEMBER_IDENTIFIER);
    }

    static @NotNull PsiReferenceProvider referenceProvider(
            @NotNull Predicate<PsiElement> candidate,
            @NotNull Function<PsiElement, PsiReference> factory) {
        return new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(
                    @NotNull PsiElement element,
                    @NotNull ProcessingContext context) {
                if (!(element.getContainingFile() instanceof QinPsiFile)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                PsiElement referenceElement = referenceElement(element);
                if (referenceElement == null || !candidate.test(referenceElement)) {
                    return PsiReference.EMPTY_ARRAY;
                }
                return new PsiReference[]{factory.apply(referenceElement)};
            }
        };
    }

    static @Nullable String previousQualifierName(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent != null && QinPsiTree.isType(parent, QinTokenTypes.MEMBER_ACCESS)) {
            return QinPsiTokenStream.previousQualifierName(parent, element);
        }
        return null;
    }

    static @Nullable PsiElement previousQualifierElement(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent != null && QinPsiTree.isType(parent, QinTokenTypes.MEMBER_ACCESS)) {
            return QinPsiTokenStream.previousQualifierElement(parent, element);
        }
        return null;
    }

    static boolean isFollowedByCallParenthesis(@NotNull PsiElement element) {
        return QinPsiTokenStream.isFollowedByCallParenthesis(element);
    }

    private static void registerReferenceProvider(
            @NotNull PsiReferenceRegistrar registrar,
            @NotNull PsiReferenceProvider provider,
            @NotNull IElementType type) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(type), provider);
    }
}
