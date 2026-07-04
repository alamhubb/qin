package com.qin.debug.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.qin.debug.lsp.QinPsiTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * 外部注解器：在编辑器边栏显示 Qin 编译错误
 * 从 QinCompileErrorService 获取错误信息并在编辑器中高亮显示
 */
public class QinExternalAnnotator extends ExternalAnnotator<
        QinExternalAnnotator.InitialInfo,
        List<QinCompileError>> {

    /**
     * 收集阶段需要的信息
     */
    public static class InitialInfo {
        final PsiFile psiFile;
        final String filePath;
        final Project project;

        InitialInfo(PsiFile psiFile) {
            this.psiFile = psiFile;
            VirtualFile virtualFile = QinPsiTree.virtualFile(psiFile);
            this.filePath = virtualFile != null
                ? virtualFile.getPath()
                : null;
            this.project = QinPsiTree.project(psiFile);
        }
    }

    @Nullable
    @Override
    public InitialInfo collectInformation(@NotNull PsiFile file) {
        if (!file.getName().endsWith(".java")) {
            return null;
        }
        if (QinPsiTree.virtualFile(file) == null) {
            return null;
        }
        return new InitialInfo(file);
    }

    @Nullable
    @Override
    public InitialInfo collectInformation(@NotNull PsiFile file,
                                           @NotNull Editor editor,
                                           boolean hasErrors) {
        return collectInformation(file);
    }

    @Nullable
    @Override
    public List<QinCompileError> doAnnotate(InitialInfo info) {
        if (info == null || info.filePath == null) {
            return Collections.emptyList();
        }

        // 从 QinCompileErrorService 获取当前文件的错误
        QinCompileErrorService service = info.project.getService(QinCompileErrorService.class);
        if (service == null) {
            return Collections.emptyList();
        }

        return service.getErrors(info.filePath);
    }

    @Override
    public void apply(@NotNull PsiFile file,
                      List<QinCompileError> errors,
                      @NotNull AnnotationHolder holder) {
        if (errors == null || errors.isEmpty()) {
            return;
        }

        Document document = file.getViewProvider().getDocument();
        if (document == null) {
            return;
        }

        for (QinCompileError error : errors) {
            try {
                // 计算文本偏移量
                int offset = getOffset(document, error.line, error.column);
                if (offset < 0 || offset >= document.getTextLength()) {
                    continue;
                }

                // 计算要高亮的范围（整行或从列开始到行尾）
                int lineStartOffset = document.getLineStartOffset(error.line - 1);
                int lineEndOffset = document.getLineEndOffset(error.line - 1);

                TextRange range;
                if (error.column > 1) {
                    // 从错误列开始高亮到行尾
                    int startOffset = lineStartOffset + error.column - 1;
                    if (startOffset < lineEndOffset) {
                        range = new TextRange(startOffset, lineEndOffset);
                    } else {
                        range = new TextRange(lineStartOffset, lineEndOffset);
                    }
                } else {
                    // 高亮整行
                    range = new TextRange(lineStartOffset, lineEndOffset);
                }

                // 创建注解
                HighlightSeverity severity = error.isError
                    ? HighlightSeverity.ERROR
                    : HighlightSeverity.WARNING;

                holder.newAnnotation(severity, error.message)
                    .range(range)
                    .create();

            } catch (Exception e) {
                // 忽略单个错误的处理异常
            }
        }
    }

    /**
     * 计算行列对应的文本偏移量
     */
    private int getOffset(Document document, int line, int column) {
        if (line < 1 || line > document.getLineCount()) {
            return -1;
        }

        int lineStartOffset = document.getLineStartOffset(line - 1);
        return lineStartOffset + Math.max(0, column - 1);
    }
}
