package com.qin.debug.lsp;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.codeInsight.lookup.impl.LookupImpl;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiFile;
import com.qin.debug.QinLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class QinLspLookupEnterHandler extends EnterHandlerDelegateAdapter {
    @Override
    public @NotNull Result preprocessEnter(
            @Nullable PsiFile file,
            @NotNull Editor editor,
            @NotNull Ref<Integer> caretOffset,
            @NotNull Ref<Integer> caretAdvance,
            @NotNull DataContext dataContext,
            @Nullable EditorActionHandler originalHandler) {
        if (file == null || !isQinLspFile(file)) {
            return Result.Continue;
        }

        LookupImpl lookup = (LookupImpl) LookupManager.getActiveLookup(editor);
        if (lookup == null || !lookup.isCompletion()) {
            return Result.Continue;
        }

        LookupElement item = lookup.getCurrentItem();
        if (item == null) {
            return Result.Continue;
        }

        QinLogger.ensureInitialized(file.getProject(), file.getProject().getBasePath());
        QinLogger.debug("[LSP-LOOKUP-ENTER] fileType=" + file.getFileType().getName()
                + " item=" + item.getLookupString()
                + " caretOffset=" + editor.getCaretModel().getOffset());
        ApplicationManager.getApplication().invokeLater(() -> {
            LookupImpl activeLookup = (LookupImpl) LookupManager.getActiveLookup(editor);
            if (file.getProject().isDisposed()
                    || editor.isDisposed()
                    || activeLookup == null
                    || activeLookup != lookup) {
                return;
            }
            activeLookup.finishLookup('\n', item);
        }, ModalityState.current());
        return EnterHandlerDelegate.Result.Stop;
    }

    private static boolean isQinLspFile(PsiFile file) {
        return file.getFileType() == QinLspFileType.INSTANCE
                || file.getFileType() == OvsLspFileType.INSTANCE
                || file.getFileType() == CsstsLspFileType.INSTANCE;
    }
}
