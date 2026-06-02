package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendBinarySmokeTestMain {
    private QinJavaAstJsBackendBinarySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                class Calculator {
                    int add(int a, int b) { return a + b; }
                    String greet(String name) { String prefix = "hello "; return prefix + name; }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("function __qin_binary__"), "binary helper");
        require(generated.contains("return __qin_binary__(\"+\", a, b);"), "numeric add method");
        require(generated.contains("return __qin_binary__(\"+\", \"hello \", name);"), "string concat method");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-binary-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-binary\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nconst calculator = new Calculator(); calculator.add(2, 3) + \":\" + calculator.greet(\"qin\");\n",
                "java_ast_js_backend_binary");
        if (!"5:hello qin".equals(result)) {
            throw new IllegalStateException("Expected generated Java binary result 5:hello qin, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendBinarySmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
