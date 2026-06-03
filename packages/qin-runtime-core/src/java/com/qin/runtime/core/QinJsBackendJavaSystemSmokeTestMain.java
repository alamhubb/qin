package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaSystemSmokeTestMain {
    private QinJsBackendJavaSystemSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "millis",
                                new QinIrStaticMethodCallExpression(
                                        "System",
                                        "java.lang.System",
                                        "currentTimeMillis",
                                        List.of())),
                        new QinIrConstDeclaration(
                                "nanos",
                                new QinIrStaticMethodCallExpression(
                                        "System",
                                        "java.lang.System",
                                        "nanoTime",
                                        List.of())),
                        new QinIrConstDeclaration(
                                "javaVersion",
                                new QinIrStaticMethodCallExpression(
                                        "System",
                                        "java.lang.System",
                                        "getProperty",
                                        List.of(new QinIrStringLiteral("java.version")))),
                        new QinIrConstDeclaration(
                                "identity",
                                new QinIrStaticMethodCallExpression(
                                        "System",
                                        "java.lang.System",
                                        "identityHashCode",
                                        List.of(new QinIrIdentifierReference("System"))))),
                List.of(new QinIrExpressionStatement(
                        new QinIrInstanceMethodCallExpression(
                                new QinIrMemberAccessExpression("System", "out"),
                                "println",
                                List.of(new QinIrStringLiteral("hello from System.out"))))),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.lang",
                        "System",
                        "System",
                        "java.lang.System")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaLangSystem"), "System runtime shim");
        require(generated.contains("const System = __QinJavaLangSystem;"), "System import alias");
        require(generated.contains("System.currentTimeMillis()"), "System.currentTimeMillis call");
        require(generated.contains("System.nanoTime()"), "System.nanoTime call");
        require(generated.contains("System.out.println(\"hello from System.out\")"), "System.out.println call");

        Path root = Files.createTempDirectory("qin-js-backend-system-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-system\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                "globalThis.__qinJavaVersion = \"25-test\";\n"
                        + generated
                        + "\n[(millis > 0), (nanos > 0), javaVersion, (identity > 0)].join(\":\");\n",
                "js_backend_system");
        if (!"true:true:25-test:true".equals(result)) {
            throw new IllegalStateException("Expected generated System result, got: " + result);
        }
        System.out.println("QinJsBackendJavaSystemSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
