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

                    java.lang.Object makeBlock() {
                        return () -> {
                            int value = 1;
                            return value + 1;
                        };
                    }

                    java.lang.Object makeParameter() {
                        return (int value) -> value + 1;
                    }
                }

                class DerivedLambdaBox extends LambdaBox {
                }
                """);

        QinIrMethodDeclaration make = program.classDeclarations().get(0).methods().get(0);
        require(make.returnExpression() instanceof QinIrFunctionLiteral, "lambda lowers to function literal");
        QinIrMethodDeclaration makeBlock = program.classDeclarations().get(0).methods().get(1);
        require(makeBlock.returnExpression() instanceof QinIrFunctionLiteral, "block lambda lowers to function literal");
        QinIrMethodDeclaration makeParameter = program.classDeclarations().get(0).methods().get(2);
        require(makeParameter.returnExpression() instanceof QinIrFunctionLiteral, "parameter lambda lowers to function literal");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("() =>"), "generated arrow function");
        require(generated.contains("(value) =>"), "generated parameter arrow function");
        require(generated.contains("class DerivedLambdaBox extends LambdaBox"), "generated class extends");

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
        Object blockResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new LambdaBox().makeBlock(); fn();\n",
                "java_ast_js_backend_lambda_block");
        if (!Double.valueOf(2.0).equals(blockResult)) {
            throw new IllegalStateException("Expected block lambda result 2.0, got: " + blockResult);
        }
        Object parameterResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new LambdaBox().makeParameter(); fn(2);\n",
                "java_ast_js_backend_lambda_parameter");
        if (!Double.valueOf(3.0).equals(parameterResult)) {
            throw new IllegalStateException("Expected parameter lambda result 3.0, got: " + parameterResult);
        }
        Object inheritedResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new DerivedLambdaBox().make(); fn();\n",
                "java_ast_js_backend_lambda_extends");
        if (!Double.valueOf(1.0).equals(inheritedResult)) {
            throw new IllegalStateException("Expected inherited lambda result 1.0, got: " + inheritedResult);
        }

        System.out.println("QinJavaAstJsBackendLambdaSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
