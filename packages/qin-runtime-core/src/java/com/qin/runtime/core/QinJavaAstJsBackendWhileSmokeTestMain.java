package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrWhileExpression;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendWhileSmokeTestMain {
    private QinJavaAstJsBackendWhileSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class CounterBox {
                    int count() {
                        int i = 0;
                        while (i < 3) {
                            i = i + 1;
                        }
                        return i;
                    }
                }
                """);

        QinIrMethodDeclaration count = program.classDeclarations().get(0).methods().get(0);
        require(count.returnExpression() instanceof QinIrLetExpression, "method body let expression");
        QinIrLetExpression body = (QinIrLetExpression) count.returnExpression();
        require(body.leadingExpressions().size() == 1, "while leading expression count");
        require(body.leadingExpressions().get(0) instanceof QinIrWhileExpression, "while expression");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("while (__qin_binary__(\"<\", i, 3.0))"), "while condition");
        require(generated.contains("i = __qin_binary__(\"+\", i, 1.0);"), "while body assignment");
        require(generated.contains("return i;"), "method result");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-while-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-while\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new CounterBox(); box.count();\n",
                "java_ast_js_backend_while");
        if (!Double.valueOf(3.0).equals(result)) {
            throw new IllegalStateException("Expected while result 3.0, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendWhileSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
