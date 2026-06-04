package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendStaticFieldSmokeTestMain {
    private QinJavaAstJsBackendStaticFieldSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class LoggerLike {
                    static String log = "ok";

                    String read() {
                        return log;
                    }

                    static String readStatic() {
                        return log;
                    }
                }
                """);

        require(program.classDeclarations().get(0).fields().get(0).staticField(), "static field IR marker");
        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("LoggerLike.__qin_field_log = \"ok\";"), "static field initializer");
        require(!generated.contains("this.__qin_field_log = \"ok\";"), "no instance static field initializer");
        require(generated.contains("return LoggerLike.__qin_field_log;"), "static field read");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-static-field-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-static-field\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst value = new LoggerLike().read() + \":\" + LoggerLike.readStatic(); value;\n",
                "java_ast_js_backend_static_field");
        if (!"ok:ok".equals(result)) {
            throw new IllegalStateException("Expected static field result ok:ok, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendStaticFieldSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
