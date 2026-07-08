package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendUpdateExpressionSmokeTestMain {
    private QinJavaAstJsBackendUpdateExpressionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class UpdateExpressionBox {
                    String arrayIndexPostfix(String[] values) {
                        int index = 0;
                        if (values == null) {
                            index = index;
                        }
                        String value = values[index++];
                        return value + ":" + index;
                    }

                    String postfixValue(boolean guard) {
                        int index = 0;
                        if (guard) {
                            index = index;
                        }
                        int value = index++;
                        return value + ":" + index;
                    }

                    String prefixValue(boolean guard) {
                        int index = 0;
                        if (guard) {
                            index = index;
                        }
                        int value = ++index;
                        return value + ":" + index;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("index++"), "postfix update emit");
        require(!generated.contains("index = __qin_binary__(\"+\", index, 1.0)]"),
                "postfix array index must not become pre-increment assignment");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-update-expression-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-update-expression\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst box = new UpdateExpressionBox();"
                        + "box.postfixValue(false) + \":\" + box.prefixValue(false);\n",
                "java_ast_js_backend_update_expression");
        if (!"0:1:1:1".equals(result)) {
            throw new IllegalStateException("Expected update expression result 0:1:1:1, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendUpdateExpressionSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
