package com.qin.debug.annotator;

/**
 * 编译错误数据类
 * 存储单个编译错误的详细信息
 */
public class QinCompileError {

    public final String filePath;
    public final int line;
    public final int column;
    public final String message;
    public final boolean isError;

    public QinCompileError(String filePath, int line, int column,
                           String message, boolean isError) {
        this.filePath = filePath;
        this.line = line;
        this.column = column;
        this.message = message;
        this.isError = isError;
    }

    @Override
    public String toString() {
        return String.format("%s:%d:%d: %s: %s",
            filePath, line, column,
            isError ? "error" : "warning",
            message);
    }
}
