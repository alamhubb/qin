package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendEnumConstructorSmokeTestMain {
    private QinJavaAstJsBackendEnumConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                enum Status {
                    READY("ready");

                    private final String label;

                    Status(String label) {
                        this.label = label;
                    }

                    public String toString() {
                        return this.label;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        if (!generated.contains("Status.READY = __qin_init_enum_value(new Status(\"ready\")")) {
            throw new IllegalStateException("Expected enum constant constructor arguments in generated JS:\n" + generated);
        }
        Path root = Files.createTempDirectory("qin-java-ast-js-backend-enum-constructor-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-enum-constructor\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nStatus.READY.toString();\n",
                "java_ast_js_backend_enum_constructor");
        if (!"ready".equals(result)) {
            throw new IllegalStateException("Expected enum constructor result ready, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendEnumConstructorSmokeTestMain OK");
    }
}
