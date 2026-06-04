package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendInheritedFieldSmokeTestMain {
    private QinJavaAstJsBackendInheritedFieldSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class BaseParser {
                    String cstStack = "ready";
                }

                class ChildParser extends BaseParser {
                    String read() {
                        return cstStack;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("this.__qin_field_cstStack = \"ready\";"), "base field initializer");
        require(generated.contains("return this.__qin_field_cstStack;"), "inherited field read");
        require(!generated.contains("return this.cstStack;"), "no raw inherited field read");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-inherited-field-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-inherited-field\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst child = new ChildParser(); child.read();\n",
                "java_ast_js_backend_inherited_field");
        if (!"ready".equals(result)) {
            throw new IllegalStateException("Expected inherited field result ready, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendInheritedFieldSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
