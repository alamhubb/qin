package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendAssignmentSmokeTestMain {
    private QinJavaAstJsBackendAssignmentSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class CounterBox {
                    int next() {
                        int count = 1;
                        count = count + 1;
                        return count;
                    }
                }
                """);

        QinIrMethodDeclaration next = program.classDeclarations().get(0).methods().get(0);
        require(next.returnExpression() instanceof QinIrLetExpression, "let expression return");
        QinIrLetExpression letExpression = (QinIrLetExpression) next.returnExpression();
        require(letExpression.localDeclarations().size() == 1, "local declaration count");
        require(letExpression.leadingExpressions().size() == 1, "leading expression count");
        require(letExpression.leadingExpressions().get(0) instanceof QinIrAssignmentExpression, "assignment expression");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("let count = 1.0;"), "count local declaration");
        require(generated.contains("count = __qin_binary__(\"+\", count, 1.0);"), "count assignment");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-assignment-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-assignment\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new CounterBox(); box.next();\n",
                "java_ast_js_backend_assignment");
        if (!Double.valueOf(2.0).equals(result)) {
            throw new IllegalStateException("Expected assignment result 2.0, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendAssignmentSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
