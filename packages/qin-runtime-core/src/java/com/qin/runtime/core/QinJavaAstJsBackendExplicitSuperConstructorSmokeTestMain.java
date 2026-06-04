package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendExplicitSuperConstructorSmokeTestMain {
    private QinJavaAstJsBackendExplicitSuperConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class ParentBox {
                    String text;

                    ParentBox(String value, String suffix) {
                        this.text = value + suffix;
                    }

                    String read() {
                        return this.text;
                    }
                }

                class ChildBox extends ParentBox {
                    ChildBox(String value) {
                        super(value, "!");
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        if (!generated.contains("super(value, \"!\")")) {
            throw new IllegalStateException("Expected generated JS to preserve explicit super arguments:\n" + generated);
        }
        Path root = Files.createTempDirectory("qin-java-ast-js-backend-explicit-super-constructor-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-explicit-super-constructor\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nnew ChildBox(\"qin\").read();\n",
                "java_ast_js_backend_explicit_super_constructor");
        if (!"qin!".equals(result)) {
            throw new IllegalStateException("Expected explicit super constructor result qin!, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendExplicitSuperConstructorSmokeTestMain OK");
    }
}
