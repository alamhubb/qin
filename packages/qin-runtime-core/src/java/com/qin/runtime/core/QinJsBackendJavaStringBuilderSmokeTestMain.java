package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaStringBuilderSmokeTestMain {
    private QinJsBackendJavaStringBuilderSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "builder",
                        new QinIrJavaNewExpression(
                                "StringBuilder",
                                "java.lang.StringBuilder",
                                List.of(new QinIrStringLiteral("qin"))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.lang",
                        "StringBuilder",
                        "StringBuilder",
                        "java.lang.StringBuilder")),
                List.of(),
                List.of(),
                List.of(new QinIrJavaInstanceMethodCall(
                        "builder",
                        "java.lang.StringBuilder",
                        "append",
                        List.of(new QinIrStringLiteral("-js")))),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaLangStringBuilder"), "StringBuilder runtime shim");
        require(generated.contains("const StringBuilder = __QinJavaLangStringBuilder;"), "StringBuilder import alias");
        require(generated.contains("new StringBuilder(\"qin\")"), "StringBuilder constructor");
        require(generated.contains("builder.append(\"-js\");"), "StringBuilder append call");

        Path root = Files.createTempDirectory("qin-js-backend-string-builder-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-string-builder\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nglobalThis.__qinResult.toString() + \":\" + globalThis.__qinResult.length();\n",
                "js_backend_string_builder");
        if (!"qin-js:6".equals(result)) {
            throw new IllegalStateException("Expected generated StringBuilder qin-js:6, got: " + result);
        }
        System.out.println("QinJsBackendJavaStringBuilderSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
