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

public final class QinJavaAstJsBackendForUpdateSmokeTestMain {
    private QinJavaAstJsBackendForUpdateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class CounterBox {
                    int sum() {
                        int total = 0;
                        for (int i = 0; i < 3; i++) {
                            total = total + i;
                        }
                        return total;
                    }
                }
                """);

        QinIrMethodDeclaration sum = program.classDeclarations().get(0).methods().get(0);
        require(sum.returnExpression() instanceof QinIrLetExpression, "method body let expression");
        QinIrLetExpression body = (QinIrLetExpression) sum.returnExpression();
        require(body.leadingExpressions().get(0) instanceof QinIrForExpression, "for expression");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("i = __qin_binary__(\"+\", i, 1.0)"), "postfix increment lowering");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-for-update-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-for-update\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new CounterBox(); box.sum();\n",
                "java_ast_js_backend_for_update");
        if (!Double.valueOf(3.0).equals(result)) {
            throw new IllegalStateException("Expected for update result 3.0, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendForUpdateSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
