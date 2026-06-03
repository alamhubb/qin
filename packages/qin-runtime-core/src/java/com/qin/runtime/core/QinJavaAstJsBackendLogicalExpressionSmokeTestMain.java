package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrIfExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

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

                    boolean andSkips() {
                        int i = 0;
                        boolean result = false && ((i = i + 1) > 0);
                        i = i;
                        return i == 0;
                    }

                    boolean orSkips() {
                        int i = 0;
                        boolean result = true || ((i = i + 1) > 0);
                        i = i;
                        return i == 0;
                    }
                }
                """);

        QinIrMethodDeclaration both = program.classDeclarations().get(0).methods().get(0);
        require(both.returnExpression() instanceof QinIrIfExpression, "logical expression lowers to if expression");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("if (left)"), "&& short-circuit branch");
        require(generated.contains("if (true)"), "|| short-circuit branch");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-logical-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-logical\" };\n",
                StandardCharsets.UTF_8);
        Object andResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst logic = new Logic(); logic.andSkips();\n",
                "java_ast_js_backend_logical_and");
        if (!Boolean.TRUE.equals(andResult)) {
            throw new IllegalStateException("Expected && short-circuit result true, got: " + andResult);
        }
        Object orResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst logic = new Logic(); logic.orSkips();\n",
                "java_ast_js_backend_logical_or");
        if (!Boolean.TRUE.equals(orResult)) {
            throw new IllegalStateException("Expected || short-circuit result true, got: " + orResult);
        }

        System.out.println("QinJavaAstJsBackendLogicalExpressionSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
