package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendThisConstructorSmokeTestMain {
    private QinJavaAstJsBackendThisConstructorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class TokenBox {
                    String name;
                    String value;

                    TokenBox(String name) {
                        this(name, null);
                    }

                    TokenBox(String name, String value) {
                        this.name = name;
                        this.value = value;
                    }

                    String read() {
                        return this.name + ":" + this.value;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        if (!generated.contains("this.__qin_constructor_TokenBox_2(name, null)")) {
            throw new IllegalStateException("Expected generated JS to route this(...) to constructor initializer:\n" + generated);
        }
        if (generated.contains("return this.__qin_constructor_TokenBox_")) {
            throw new IllegalStateException("Java constructor dispatch must not return initializer values:\n" + generated);
        }
        QinIrProgram objectAssignmentProgram = new QinJavaAstIrLowerer().lowerSource("""
                import java.lang.StringBuilder;
                class ConstructorObjectAssignmentBox {
                    StringBuilder builder;

                    ConstructorObjectAssignmentBox() {
                        this.builder = new StringBuilder("qin");
                    }

                    String read() {
                        return this.builder.toString();
                    }
                }
                """);
        String objectAssignmentGenerated = new QinJsBackend().compileProgram(objectAssignmentProgram);
        Path root = Files.createTempDirectory("qin-java-ast-js-backend-this-constructor-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-this-constructor\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\n"
                        + objectAssignmentGenerated
                        + "\nnew TokenBox(\"IDENTIFIER\").read() + \"|\" + new ConstructorObjectAssignmentBox().read();\n",
                "java_ast_js_backend_this_constructor");
        if (!"IDENTIFIER:null|qin".equals(result)) {
            throw new IllegalStateException("Expected this constructor result IDENTIFIER:null|qin, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendThisConstructorSmokeTestMain OK");
    }
}
