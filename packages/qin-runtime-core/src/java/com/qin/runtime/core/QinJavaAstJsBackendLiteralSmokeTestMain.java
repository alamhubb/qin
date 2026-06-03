package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendLiteralSmokeTestMain {
    private QinJavaAstJsBackendLiteralSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class Literals {
                    boolean enabled() {
                        return true;
                    }

                    String missing() {
                        return null;
                    }
                }
                """);

        QinIrMethodDeclaration enabled = program.classDeclarations().get(0).methods().get(0);
        require(enabled.returnExpression() instanceof QinIrBooleanLiteral, "boolean literal IR");
        QinIrBooleanLiteral booleanLiteral = (QinIrBooleanLiteral) enabled.returnExpression();
        require(booleanLiteral.value(), "true literal value");

        QinIrMethodDeclaration missing = program.classDeclarations().get(0).methods().get(1);
        require(missing.returnExpression() instanceof QinIrNullLiteral, "null literal IR");

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("return true;"), "boolean literal JS");
        require(generated.contains("return null;"), "null literal JS");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-literal-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-literal\" };\n",
                StandardCharsets.UTF_8);
        Object enabledResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new Literals(); box.enabled();\n",
                "java_ast_js_backend_literal_enabled");
        if (!Boolean.TRUE.equals(enabledResult)) {
            throw new IllegalStateException("Expected enabled result true, got: " + enabledResult);
        }
        Object missingResult = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst box = new Literals(); box.missing();\n",
                "java_ast_js_backend_literal_missing");
        if (missingResult != null) {
            throw new IllegalStateException("Expected missing result null, got: " + missingResult);
        }
        System.out.println("QinJavaAstJsBackendLiteralSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
