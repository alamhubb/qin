package com.qin.runtime.core;

import java.nio.file.Path;

/**
 * Compile-time diagnostics for JS built-in mapping.
 */
public final class JsSdkBuiltinCompileErrorTestMain {
    private JsSdkBuiltinCompileErrorTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinJsSdkTestPaths.resolveJsSdkRoot();

        expectError(root.resolve("main/invalid-unknown-builtin.js"), "QJS1001");
        expectError(root.resolve("main/invalid-json-parse-arg.js"), "QJS1003");

        System.out.println("JsSdkBuiltinCompileErrorTestMain passed.");
    }

    private static void expectError(Path sourceFile, String code) throws Exception {
        Path root = sourceFile.getParent().getParent().normalize();
        QinBuildRequest request = new QinBuildRequest(
                root,
                sourceFile,
                QinBuildTarget.JVM,
                "com.qin.runtime.generated.JsSdkError",
                root.resolve("build/js-sdk-errors/classes"),
                root.resolve("build/js-sdk-errors/app.js"),
                false);
        try {
            new QinBuildCoordinator().build(request);
        } catch (Exception ex) {
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            if (!message.contains(code)) {
                throw new IllegalStateException(
                        "Expected error code " + code + " but got: " + message, ex);
            }
            return;
        }
        throw new IllegalStateException("Expected compilation error " + code + " for " + sourceFile.toAbsolutePath());
    }
}
