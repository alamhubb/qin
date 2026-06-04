package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendTypedCatchSmokeTestMain {
    private QinJavaAstJsBackendTypedCatchSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class CatchBox {
                    java.lang.String stack() {
                        try {
                            throw new StackOverflowError("stack");
                        } catch (StackOverflowError error) {
                            return error.getMessage();
                        }
                    }

                    java.lang.String miss() {
                        try {
                            throw new RuntimeException("runtime");
                        } catch (StackOverflowError error) {
                            return "wrong";
                        }
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaLangStackOverflowError"), "StackOverflowError runtime shim");
        require(
                generated.contains("if (!(error instanceof __QinJavaLangStackOverflowError"),
                "typed catch guard");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-typed-catch-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-typed-catch\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + """

                        const box = new CatchBox();
                        let miss;
                        try {
                          box.miss();
                          miss = "wrong";
                        } catch (error) {
                          miss = error instanceof RuntimeException ? error.getMessage() : String(error);
                        }
                        box.stack() + ":" + miss;
                        """,
                "java_ast_js_backend_typed_catch");
        if (!"stack:runtime".equals(result)) {
            throw new IllegalStateException("Expected typed catch result stack:runtime, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendTypedCatchSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
