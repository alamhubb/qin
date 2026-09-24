package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrShortCircuitExpression;
import com.qin.lang.ir.QinIrUnaryExpression;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendLogicalExpressionSmokeTestMain {
    private QinJavaAstJsBackendLogicalExpressionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class Logic {
                    boolean both(boolean left, boolean right) {
                        return left && right;
                    }

                    boolean either(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
                        return a || b || c || d || e || f;
                    }

                    boolean not(boolean value) {
                        return !value;
                    }

                    boolean nonEmpty(java.util.List<String> values) {
                        return !values.isEmpty() && true;
                    }

                    String choose(boolean value) {
                        return value ? "yes" : "no";
                    }
                }
                """);

        QinIrMethodDeclaration both = program.classDeclarations().get(0).methods().get(0);
        require(both.returnExpression() instanceof QinIrShortCircuitExpression,
                "logical && expression lowers to short-circuit expression");
        QinIrShortCircuitExpression bothExpression = (QinIrShortCircuitExpression) both.returnExpression();
        require("&&".equals(bothExpression.operator()), "logical && operator is preserved");
        QinIrMethodDeclaration either = program.classDeclarations().get(0).methods().get(1);
        require(either.returnExpression() instanceof QinIrShortCircuitExpression,
                "logical || expression lowers to short-circuit expression");
        QinIrShortCircuitExpression eitherExpression = (QinIrShortCircuitExpression) either.returnExpression();
        require("||".equals(eitherExpression.operator()), "logical || operator is preserved");
        QinIrMethodDeclaration not = program.classDeclarations().get(0).methods().get(2);
        require(not.returnExpression() instanceof QinIrUnaryExpression,
                "standalone Java ! expression lowers to unary IR");
        QinIrMethodDeclaration nonEmpty = program.classDeclarations().get(0).methods().get(3);
        require(nonEmpty.returnExpression() instanceof QinIrShortCircuitExpression,
                "logical expression with unary operand lowers to short-circuit expression");
        QinIrShortCircuitExpression nonEmptyExpression = (QinIrShortCircuitExpression) nonEmpty.returnExpression();
        require(nonEmptyExpression.left() instanceof QinIrUnaryExpression,
                "Java ! expression lowers to unary IR");
        QinIrUnaryExpression unaryExpression = (QinIrUnaryExpression) nonEmptyExpression.left();
        require("!".equals(unaryExpression.operator()), "Java ! operator is preserved");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains(" && "), "backend emits native && short-circuit expression");
        require(generated.contains(" || "), "backend emits native || short-circuit expression");
        require(generated.contains("(!"), "backend emits native ! unary expression");
        require(generated.contains("!values.isEmpty()"),
                "backend emits unary short-circuit operands without redundant parentheses");
        require(!generated.contains("(!values.isEmpty()) && true"),
                "backend does not wrap unary short-circuit operands in redundant parentheses");
        require(!generated.contains("if ((() =>"),
                "backend must not lower Java logical chains into nested IIFE if expressions");
        require(!generated.contains("return ((() => {"),
                "backend must not lower Java unary ! into an IIFE if expression");
        require(generated.contains(" ? "),
                "backend emits Java conditional expressions as native JS ternary expressions");
        require(!generated.contains("if (value)"),
                "backend does not lower simple conditional expressions into IIFE if expressions");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-logical-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-logical\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst logic = new Logic(); logic.both(true, true) && logic.either(false, false, false, true, false, false) && logic.not(false) && logic.choose(true) === 'yes';\n",
                "java_ast_js_backend_logical");
        if (!Boolean.TRUE.equals(result)) {
            throw new IllegalStateException("Expected logical expression result true, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendLogicalExpressionSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
