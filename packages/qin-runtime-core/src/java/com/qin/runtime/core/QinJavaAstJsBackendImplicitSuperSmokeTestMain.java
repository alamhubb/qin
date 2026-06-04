package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendImplicitSuperSmokeTestMain {
    private QinJavaAstJsBackendImplicitSuperSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class Parent {
                    String tag = "parent";
                }

                class Child extends Parent {
                    String name;

                    Child(String name) {
                        this.name = name;
                    }

                    String read() {
                        return this.tag + ":" + this.name;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("super();"), "implicit super without forwarded arguments");
        require(!generated.contains("super(name);"), "no implicit argument forwarding");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-implicit-super-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-implicit-super\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nnew Child(\"qin\").read();\n",
                "java_ast_js_backend_implicit_super");
        if (!"parent:qin".equals(result)) {
            throw new IllegalStateException("Expected implicit super result parent:qin, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendImplicitSuperSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
