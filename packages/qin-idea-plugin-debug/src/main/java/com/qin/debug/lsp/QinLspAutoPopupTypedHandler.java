package com.qin.debug.lsp;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public final class QinLspAutoPopupTypedHandler extends TypedHandlerDelegate {
    @Override
    public @NotNull Result checkAutoPopup(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (!isQinLspFile(file) || !isCompletionTrigger(charTyped)) {
            return Result.CONTINUE;
        }

        AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
        return Result.STOP;
    }

    private static boolean isQinLspFile(PsiFile file) {
        return file.getFileType() == QinLspFileType.INSTANCE
                || file.getFileType() == OvsLspFileType.INSTANCE
                || file.getFileType() == CsstsLspFileType.INSTANCE;
    }

    private static boolean isCompletionTrigger(char value) {
        return value == '.'
                || value == '_'
                || value == '$'
                || Character.isLetterOrDigit(value);
    }
}
