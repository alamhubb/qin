package com.qin.lang.pipeline.cfa;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Request for Slime AST -> JVM/CFA pipeline compilation.
 */
public record QinCfaCompileRequest(
        Path sourceFile,
        Path projectRoot,
        String className,
        boolean emitClassBytes) {
    public QinCfaCompileRequest {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        Objects.requireNonNull(projectRoot, "projectRoot cannot be null");
        if (emitClassBytes && (className == null || className.isBlank())) {
            throw new IllegalArgumentException("className cannot be blank when emitClassBytes=true");
        }
    }

    public static QinCfaCompileRequest forJvm(Path sourceFile, Path projectRoot, String className) {
        return new QinCfaCompileRequest(sourceFile, projectRoot, className, true);
    }

    public static QinCfaCompileRequest forAnalysis(Path sourceFile, Path projectRoot) {
        return new QinCfaCompileRequest(sourceFile, projectRoot, "", false);
    }
}
