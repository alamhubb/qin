package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrForExpression;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendForSmokeTestMain {
    private QinJavaAstJsBackendForSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class CounterBox {
                    int sum() {
                        int total = 0;
                        for (int i = 0; i < 3; i = i + 1) {
                            total = total + i;
                        }
                        return total;
                    }
                }
                """);

        QinIrMethodDeclaration sum = program.classDeclarations().get(0).methods().get(0);
        require(sum.returnExpression() instanceof QinIrLetExpression, "method body let expression");
        QinIrLetExpression body = (QinIrLetExpression) sum.returnExpression();
        require(body.leadingExpressions().size() == 1, "for leading expression count");
        require(body.leadingExpressions().get(0) instanceof QinIrForExpression, "for expression");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("for (let i = 0.0; __qin_binary__(\"<\", i, 3.0); i = __qin_binary__(\"+\", i, 1.0))"),
                "for header");
        require(generated.contains("total = __qin_binary__(\"+\", total, i);"), "for body assignment");
        require(generated.contains("return total;"), "method result");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-for-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-for\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new CounterBox(); box.sum();\n",
                "java_ast_js_backend_for");
        if (!Double.valueOf(3.0).equals(result)) {
            throw new IllegalStateException("Expected for result 3.0, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendForSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
