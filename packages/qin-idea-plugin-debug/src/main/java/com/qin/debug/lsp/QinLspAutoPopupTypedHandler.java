package com.qin.debug.lsp;

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.qin.debug.QinLogger;
import org.jetbrains.annotations.NotNull;

public final class QinLspAutoPopupTypedHandler extends TypedHandlerDelegate {
    @Override
    public @NotNull Result checkAutoPopup(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        return Result.CONTINUE;
    }

    @Override
    public @NotNull Result charTyped(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (!isQinLspFile(file)
                || !isCompletionTrigger(charTyped)
                || !isAfterCompletionPrefix(editor)
                || LookupManager.getActiveLookup(editor) != null) {
            return Result.CONTINUE;
        }

        scheduleAutoPopupAfterTyping(charTyped, project, editor, file);
        return Result.CONTINUE;
    }

    private static void scheduleAutoPopupAfterTyping(char charTyped, Project project, Editor editor, PsiFile file) {
        QinLogger.ensureInitialized(project, project.getBasePath());
        QinLogger.debug("[LSP-AUTOPOPUP] char=" + printable(charTyped)
                + " fileType=" + file.getFileType().getName()
                + " completionPrefix=true"
                + " caretOffset=" + editor.getCaretModel().getOffset());
        ApplicationManager.getApplication().invokeLater(() -> {
            if (project.isDisposed()
                    || editor.isDisposed()
                    || LookupManager.getActiveLookup(editor) != null
                    || !isAfterCompletionPrefix(editor)) {
                return;
            }
            CodeCompletionHandlerBase
                    .createHandler(CompletionType.BASIC, false, true, true)
                    .invokeCompletion(project, editor);
        }, ModalityState.current());
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

    static boolean isAfterCompletionPrefix(Editor editor) {
        int offset = editor.getCaretModel().getOffset();
        CharSequence text = editor.getDocument().getImmutableCharSequence();
        int index = Math.min(offset, text.length()) - 1;
        if (index < 0 || !isIdentifierPart(text.charAt(index))) {
            return isAfterMemberAccess(editor);
        }
        while (index >= 0 && isIdentifierPart(text.charAt(index))) {
            index--;
        }
        return true;
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
