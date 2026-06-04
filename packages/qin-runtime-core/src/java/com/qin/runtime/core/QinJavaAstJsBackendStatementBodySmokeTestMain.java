package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrLocalDeclarationExpression;
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
        require(letExpression.localDeclarations().isEmpty(), "predeclared local declaration count");
        require(letExpression.leadingExpressions().size() == 2, "ordered leading expression count");
        require(letExpression.leadingExpressions().get(0) instanceof QinIrLocalDeclarationExpression,
                "ordered local declaration expression");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("let builder = new __QinJavaLangStringBuilder(\"qin\");"),
                "builder local declaration");
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
        require(chooseExpression.leadingExpressions().size() == 2, "if side-effect leading expression count");
        String ifThenReturnGenerated = new QinJsBackend().compileProgram(ifThenReturnProgram);
        require(ifThenReturnGenerated.contains("builder.append(\"-left\");"), "if consequent side effect");
        require(ifThenReturnGenerated.contains("return builder.toString();"), "return after if");

        QinIrProgram guardProgram = new QinJavaAstIrLowerer().lowerSource("""
                class GuardBox {
                    String read() {
                        String matched = "qin";
                        if (matched == null) {
                            throw new RuntimeException("guard");
                        }
                        int valueLength = matched.length();
                        return "length-" + valueLength;
                    }
                }
                """);
        String guardGenerated = new QinJsBackend().compileProgram(guardProgram);
        int matchedIndex = guardGenerated.indexOf("let matched = \"qin\";");
        int guardIndex = guardGenerated.indexOf("if (__qin_binary__(\"==\", matched, null))");
        int valueLengthIndex = guardGenerated.indexOf("let valueLength = matched.length();");
        int throwIndex = guardGenerated.indexOf("throw new __QinJavaLangRuntimeException(\"guard\")");
        require(matchedIndex >= 0, "matched declaration");
        require(guardIndex > matchedIndex, "guard after matched declaration");
        require(throwIndex > guardIndex, "throw inside guard");
        require(valueLengthIndex > guardIndex, "value length after guard");

        QinIrProgram continueProgram = new QinJavaAstIrLowerer().lowerSource("""
                class ContinueBox {
                    String scan() {
                        String result = "";
                        for (int index = 0; index < 4; index++) {
                            if (index == 1) continue;
                            result = result + index;
                        }
                        return result;
                    }
                }
                """);
        String continueGenerated = new QinJsBackend().compileProgram(continueProgram);
        require(continueGenerated.contains("continue;"), "real continue statement");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-statement-body-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-statement-body\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\n"
                        + ifThenReturnGenerated
                        + "\n"
                        + guardGenerated
                        + "\n"
                        + continueGenerated
                        + "\nconst box = new BuilderBox(); const choice = new IfThenReturnBox();"
                        + " const guard = new GuardBox();"
                        + " const continueBox = new ContinueBox();"
                        + " box.build() + '|' + choice.choose(true) + '|' + choice.choose(false) + '|' + guard.read()"
                        + " + '|' + continueBox.scan();\n",
                "java_ast_js_backend_statement_body");
        if (!"qin-js|qin-left|qin-right|length-3|023".equals(result)) {
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
