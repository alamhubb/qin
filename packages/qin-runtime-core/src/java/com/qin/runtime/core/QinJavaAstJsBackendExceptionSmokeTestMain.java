package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendExceptionSmokeTestMain {
    private QinJavaAstJsBackendExceptionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                package com.example;
                class ParserError extends RuntimeException {
                    ParserError(String message) {
                        super(message);
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaLangRuntimeException"), "RuntimeException shim");
        require(generated.contains("class ParserError extends RuntimeException"), "generated exception inheritance");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-exception-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-exception\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst error = new ParserError(\"bad token\");\n"
                        + "(error instanceof ParserError) + \":\""
                        + " + (error instanceof RuntimeException) + \":\""
                        + " + error.getMessage();\n",
                "java_ast_js_backend_exception");
        if (!"true:true:bad token".equals(result)) {
            throw new IllegalStateException("Expected generated Java exception runtime value, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendExceptionSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
