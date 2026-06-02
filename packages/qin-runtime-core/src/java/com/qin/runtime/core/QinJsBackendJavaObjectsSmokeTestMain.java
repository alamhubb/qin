package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaObjectsSmokeTestMain {
    private QinJsBackendJavaObjectsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "value",
                                new QinIrStaticMethodCallExpression(
                                        "Objects",
                                        "java.util.Objects",
                                        "toString",
                                        List.of(new QinIrNullLiteral()))),
                        new QinIrConstDeclaration(
                                "fallback",
                                new QinIrStaticMethodCallExpression(
                                        "Objects",
                                        "java.util.Objects",
                                        "toString",
                                        List.of(new QinIrNullLiteral(), new QinIrStringLiteral("empty"))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util",
                        "Objects",
                        "Objects",
                        "java.util.Objects")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaUtilObjects"), "Objects runtime shim");
        require(generated.contains("const Objects = __QinJavaUtilObjects;"), "Objects import alias");
        require(generated.contains("Objects.toString(null)"), "Objects.toString null call");
        require(generated.contains("Objects.toString(null, \"empty\")"), "Objects.toString fallback call");

        Path root = Files.createTempDirectory("qin-js-backend-objects-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-objects\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nglobalThis.__qinResult;\n",
                "js_backend_objects");
        if (!"empty".equals(result)) {
            throw new IllegalStateException("Expected generated Objects.toString fallback empty, got: " + result);
        }
        System.out.println("QinJsBackendJavaObjectsSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
