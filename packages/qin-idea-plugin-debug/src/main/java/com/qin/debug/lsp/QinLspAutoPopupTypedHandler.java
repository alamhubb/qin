package com.qin.debug.lsp;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.qin.debug.QinLogger;
import org.jetbrains.annotations.NotNull;

public final class QinLspAutoPopupTypedHandler extends TypedHandlerDelegate {
    @Override
    public @NotNull Result checkAutoPopup(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (!isQinLspFile(file) || !isCompletionTrigger(charTyped)) {
            return Result.CONTINUE;
        }

        QinLogger.ensureInitialized(project, project.getBasePath());
        boolean memberAccess = charTyped == '.' || isAfterMemberAccess(editor);
        QinLogger.debug("[LSP-AUTOPOPUP] char=" + printable(charTyped)
                + " fileType=" + file.getFileType().getName()
                + " memberAccess=" + memberAccess
                + " caretOffset=" + editor.getCaretModel().getOffset());
        if (memberAccess) {
            AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, QinLspAutoPopupTypedHandler::isQinLspFile);
        } else {
            AutoPopupController.getInstance(project).scheduleAutoPopup(editor, QinLspAutoPopupTypedHandler::isQinLspFile);
        }
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

    static boolean isAfterMemberAccess(Editor editor) {
        int offset = editor.getCaretModel().getOffset();
        CharSequence text = editor.getDocument().getImmutableCharSequence();
        int index = Math.min(offset, text.length()) - 1;
        while (index >= 0 && isIdentifierPart(text.charAt(index))) {
            index--;
        }
        return index >= 0 && text.charAt(index) == '.';
    }

    private static boolean isIdentifierPart(char value) {
        return Character.isLetterOrDigit(value) || value == '_' || value == '$';
    }

    private static String printable(char value) {
        if (value == '\n') {
            return "\\n";
        }
        if (value == '\r') {
            return "\\r";
        }
        if (value == '\t') {
            return "\\t";
        }
        return Character.toString(value);
    }
}
