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
        expectJvmError(root.resolve("main/invalid-top-level-await.js"), "QIN_JS_UNSUPPORTED_TOP_LEVEL_AWAIT");
        expectJvmError(root.resolve("main/invalid-import-meta.js"), "QIN_JS_UNSUPPORTED_IMPORT_META");
        expectJvmError(root.resolve("main/invalid-eval.js"), "QIN_JS_UNSUPPORTED_EVAL");
        expectJvmError(root.resolve("main/invalid-new-function.js"), "QIN_JS_UNSUPPORTED_NEW_FUNCTION");
        expectJvmError(root.resolve("main/invalid-with.js"), "QIN_JS_UNSUPPORTED_WITH");
        expectJvmError(root.resolve("main/invalid-proxy.js"), "QIN_JS_UNSUPPORTED_PROXY");
        expectJvmError(root.resolve("main/invalid-reflect.js"), "QIN_JS_UNSUPPORTED_REFLECT");
        expectJvmError(root.resolve("main/invalid-require.js"), "QIN_JS_UNSUPPORTED_REQUIRE");
        expectJvmError(root.resolve("main/invalid-arguments-object.js"), "QIN_JS_UNSUPPORTED_ARGUMENTS_OBJECT");
        expectJvmError(root.resolve("main/invalid-object-define-property.js"),
                "QIN_JS_UNSUPPORTED_OBJECT_DEFINE_PROPERTY");
        expectJvmError(root.resolve("main/invalid-builtin-prototype-mutation.js"),
                "QIN_JS_UNSUPPORTED_BUILTIN_PROTOTYPE_MUTATION");
        expectJvmError(root.resolve("main/invalid-weakmap.js"), "QIN_JS_UNSUPPORTED_WEAK_REF");
        expectJvmError(root.resolve("main/invalid-generator.js"), "QIN_JS_UNSUPPORTED_GENERATOR");
        expectJvmError(root.resolve("main/invalid-symbol.js"), "QIN_JS_UNSUPPORTED_SYMBOL");
        expectJvmError(root.resolve("main/invalid-intl.js"), "QIN_JS_UNSUPPORTED_INTL");
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
