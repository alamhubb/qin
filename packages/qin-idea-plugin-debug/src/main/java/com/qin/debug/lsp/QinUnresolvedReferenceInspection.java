package com.qin.debug.lsp;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public final class QinUnresolvedReferenceInspection extends LocalInspectionTool {
    @Override
    public @NotNull String getDisplayName() {
        return "Unresolved Qin reference";
    }

    @Override
    public @NotNull String getGroupDisplayName() {
        return "Qin";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(
            @NotNull ProblemsHolder holder,
            boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                String message = QinUnresolvedReferenceMessages.messageFor(element);
                if (message != null) {
                    holder.registerProblem(element, message);
                }
            }
        };
    }
}
