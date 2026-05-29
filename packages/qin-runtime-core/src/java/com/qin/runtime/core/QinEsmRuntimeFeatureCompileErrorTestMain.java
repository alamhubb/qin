package com.qin.runtime.core;

import java.nio.file.Path;

/**
 * Compile-time diagnostics for target-specific ESM runtime features.
 */
public final class QinEsmRuntimeFeatureCompileErrorTestMain {
    private QinEsmRuntimeFeatureCompileErrorTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinEsmTestPaths.resolveStage1Root();
        expectJvmError(root.resolve("main/invalid-dynamic-import.js"), "ESM3001");
        System.out.println("QinEsmRuntimeFeatureCompileErrorTestMain passed.");
    }

    private static void expectJvmError(Path sourceFile, String code) throws Exception {
        Path root = sourceFile.getParent().getParent().normalize();
        QinBuildRequest request = new QinBuildRequest(
                root,
                sourceFile,
                QinBuildTarget.JVM,
                "com.qin.runtime.generated.esm.FeatureError",
                root.resolve("build/esm-feature-errors/classes"),
                root.resolve("build/esm-feature-errors/app.js"),
                false);
        try {
            new QinBuildCoordinator().build(request);
        } catch (Exception ex) {
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            if (!message.contains(code)) {
                throw new IllegalStateException("Expected error code " + code + " but got: " + message, ex);
            }
            return;
        }
        throw new IllegalStateException("Expected compilation error " + code + " for " + sourceFile.toAbsolutePath());
    }
}
