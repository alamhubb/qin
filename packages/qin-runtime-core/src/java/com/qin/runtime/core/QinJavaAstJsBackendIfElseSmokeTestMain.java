package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrIfExpression;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendIfElseSmokeTestMain {
    private QinJavaAstJsBackendIfElseSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class ChoiceBox {
                    String choose(boolean left) {
                        if (left) {
                            return "left";
                        } else {
                            return "right";
                        }
                    }
                }
                """);

        QinIrMethodDeclaration choose = program.classDeclarations().get(0).methods().get(0);
        require(choose.returnExpression() instanceof QinIrLetExpression, "method body let expression");
        QinIrLetExpression body = (QinIrLetExpression) choose.returnExpression();
        require(body.resultExpression() instanceof QinIrIfExpression, "if expression");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("if (left)"), "if condition");
        require(generated.contains("return \"left\";"), "consequent return");
        require(generated.contains("return \"right\";"), "alternate return");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-if-else-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-if-else\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new ChoiceBox(); [box.choose(true), box.choose(false)].join('|');\n",
                "java_ast_js_backend_if_else");
        if (!"left|right".equals(result)) {
            throw new IllegalStateException("Expected if/else result left|right, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendIfElseSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
