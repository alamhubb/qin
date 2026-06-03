package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendLambdaSmokeTestMain {
    private QinJavaAstJsBackendLambdaSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class LambdaBox {
                    java.lang.Object make() {
                        return () -> 1;
                    }
                }
                """);

        QinIrMethodDeclaration make = program.classDeclarations().get(0).methods().get(0);
        require(make.returnExpression() instanceof QinIrFunctionLiteral, "lambda lowers to function literal");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("() =>"), "generated arrow function");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-lambda-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-lambda\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new LambdaBox().make(); fn();\n",
                "java_ast_js_backend_lambda");
        if (!Double.valueOf(1.0).equals(result)) {
            throw new IllegalStateException("Expected lambda result 1.0, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendLambdaSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
