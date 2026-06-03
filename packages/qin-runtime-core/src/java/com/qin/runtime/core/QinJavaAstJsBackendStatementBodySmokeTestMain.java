package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendStatementBodySmokeTestMain {
    private QinJavaAstJsBackendStatementBodySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import java.lang.StringBuilder;
                class BuilderBox {
                    String build() {
                        StringBuilder builder = new StringBuilder("qin");
                        builder.append("-js");
                        return builder.toString();
                    }
                }
                """);

        QinIrMethodDeclaration build = program.classDeclarations().get(0).methods().get(0);
        require(build.returnExpression() instanceof QinIrLetExpression, "let expression return");
        QinIrLetExpression letExpression = (QinIrLetExpression) build.returnExpression();
        require(letExpression.localDeclarations().size() == 1, "local declaration count");
        require(letExpression.leadingExpressions().size() == 1, "leading expression count");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("let builder = new StringBuilder(\"qin\");"), "builder local declaration");
        require(generated.contains("builder.append(\"-js\");"), "builder append side effect");
        require(generated.contains("return builder.toString();"), "builder return expression");

        QinIrProgram ifThenReturnProgram = new QinJavaAstIrLowerer().lowerSource("""
                class IfThenReturnBox {
                    String choose(boolean left) {
                        StringBuilder builder = new StringBuilder("qin");
                        if (left) {
                            builder.append("-left");
                        } else {
                            builder.append("-right");
                        }
                        return builder.toString();
                    }
                }
                """);
        QinIrMethodDeclaration choose = ifThenReturnProgram.classDeclarations().get(0).methods().get(0);
        require(choose.returnExpression() instanceof QinIrLetExpression, "if-then-return let expression");
        QinIrLetExpression chooseExpression = (QinIrLetExpression) choose.returnExpression();
        require(chooseExpression.leadingExpressions().size() == 1, "if side-effect leading expression count");
        String ifThenReturnGenerated = new QinJsBackend().compileProgram(ifThenReturnProgram);
        require(ifThenReturnGenerated.contains("builder.append(\"-left\");"), "if consequent side effect");
        require(ifThenReturnGenerated.contains("return builder.toString();"), "return after if");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-statement-body-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-statement-body\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\n"
                        + ifThenReturnGenerated
                        + "\nconst box = new BuilderBox(); const choice = new IfThenReturnBox();"
                        + " box.build() + '|' + choice.choose(true) + '|' + choice.choose(false);\n",
                "java_ast_js_backend_statement_body");
        if (!"qin-js|qin-left|qin-right".equals(result)) {
            throw new IllegalStateException("Expected statement body result qin-js, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendStatementBodySmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
