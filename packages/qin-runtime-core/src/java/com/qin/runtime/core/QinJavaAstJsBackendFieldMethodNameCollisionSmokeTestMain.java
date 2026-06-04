package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrMethodDeclaration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendFieldMethodNameCollisionSmokeTestMain {
    private QinJavaAstJsBackendFieldMethodNameCollisionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class PositionBox {
                    int line = 7;

                    int line() {
                        return this.line;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        QinIrMethodDeclaration lineMethod = program.classDeclarations().get(0).methods().stream()
                .filter(method -> "line".equals(method.name()))
                .findFirst()
                .orElseThrow();
        require(lineMethod.returnExpression() != null, "line method return expression");
        require(generated.contains("return this.__qin_field_line;"), "internal field read");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-field-method-name-collision-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-ast-js-backend-field-method-name-collision\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new PositionBox(); box.line();\n",
                "java_ast_js_backend_field_method_name_collision");
        if (!Double.valueOf(7.0).equals(result)) {
            throw new IllegalStateException("Expected field/method collision result 7.0, got: " + result);
        }

        System.out.println("QinJavaAstJsBackendFieldMethodNameCollisionSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
