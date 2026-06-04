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

                    java.lang.Object makeTry() {
                        return () -> {
                            try {
                                return 4;
                            } catch (Exception e) {
                                return 5;
                            }
                        };
                    }

                    java.lang.Object makeEmpty() {
                        return () -> {};
                    }
                }

                class DerivedLambdaBox extends LambdaBox {
                }

                class ParentSuperLambdaBox {
                    String Declaration() {
                        return "parent";
                    }
                }

                class ChildSuperLambdaBox extends ParentSuperLambdaBox {
                    java.lang.Object makeSuper() {
                        return () -> super.Declaration();
                    }
                }
                """);

        QinIrMethodDeclaration make = program.classDeclarations().get(0).methods().get(0);
        require(make.returnExpression() instanceof QinIrFunctionLiteral, "lambda lowers to function literal");
        QinIrMethodDeclaration makeBlock = program.classDeclarations().get(0).methods().get(1);
        require(makeBlock.returnExpression() instanceof QinIrFunctionLiteral, "block lambda lowers to function literal");
        QinIrMethodDeclaration makeParameter = program.classDeclarations().get(0).methods().get(2);
        require(makeParameter.returnExpression() instanceof QinIrFunctionLiteral, "parameter lambda lowers to function literal");
        QinIrMethodDeclaration makeTry = program.classDeclarations().get(0).methods().get(3);
        require(makeTry.returnExpression() instanceof QinIrFunctionLiteral, "try lambda lowers to function literal");
        QinIrMethodDeclaration makeEmpty = program.classDeclarations().get(0).methods().get(4);
        require(makeEmpty.returnExpression() instanceof QinIrFunctionLiteral, "empty lambda lowers to function literal");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("__qin_java_functional(() =>"), "generated Java functional wrapper");
        require(generated.contains("() =>"), "generated arrow function");
        require(generated.contains("(value) =>"), "generated parameter arrow function");
        require(generated.contains("class DerivedLambdaBox extends LambdaBox"), "generated class extends");
        require(generated.contains("super.Declaration()"), "generated super method call");

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
        Object supplierResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new LambdaBox().make(); fn.get();\n",
                "java_ast_js_backend_lambda_supplier_get");
        if (!Double.valueOf(1.0).equals(supplierResult)) {
            throw new IllegalStateException("Expected supplier get lambda result 1.0, got: " + supplierResult);
        }
        Object executeResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new LambdaBox().make(); fn.execute();\n",
                "java_ast_js_backend_lambda_execute");
        if (!Double.valueOf(1.0).equals(executeResult)) {
            throw new IllegalStateException("Expected execute lambda result 1.0, got: " + executeResult);
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
        Object tryResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new LambdaBox().makeTry(); fn();\n",
                "java_ast_js_backend_lambda_try");
        if (!Double.valueOf(4.0).equals(tryResult)) {
            throw new IllegalStateException("Expected try lambda result 4.0, got: " + tryResult);
        }
        Object emptyResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new LambdaBox().makeEmpty(); fn();\n",
                "java_ast_js_backend_lambda_empty");
        if (emptyResult != null) {
            throw new IllegalStateException("Expected empty lambda result null, got: " + emptyResult);
        }
        Object inheritedResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new DerivedLambdaBox().make(); fn();\n",
                "java_ast_js_backend_lambda_extends");
        if (!Double.valueOf(1.0).equals(inheritedResult)) {
            throw new IllegalStateException("Expected inherited lambda result 1.0, got: " + inheritedResult);
        }
        Object superResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst fn = new ChildSuperLambdaBox().makeSuper(); fn.execute();\n",
                "java_ast_js_backend_lambda_super");
        if (!"parent".equals(superResult)) {
            throw new IllegalStateException("Expected super lambda result parent, got: " + superResult);
        }

        System.out.println("QinJavaAstJsBackendLambdaSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
