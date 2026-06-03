package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaArraysSmokeTestMain {
    private QinJsBackendJavaArraysSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "list",
                                new QinIrStaticMethodCallExpression(
                                        "Arrays",
                                        "java.util.Arrays",
                                        "asList",
                                        List.of(new QinIrStringLiteral("a"), new QinIrStringLiteral("b")))),
                        new QinIrConstDeclaration(
                                "text",
                                new QinIrStaticMethodCallExpression(
                                        "Arrays",
                                        "java.util.Arrays",
                                        "deepToString",
                                        List.of(new QinIrIdentifierReference("list"))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util",
                        "Arrays",
                        "Arrays",
                        "java.util.Arrays")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaUtilArrays"), "Arrays runtime shim");
        require(generated.contains("const Arrays = __QinJavaUtilArrays;"), "Arrays alias");
        require(generated.contains("Arrays.asList(\"a\", \"b\")"), "Arrays.asList call");
        require(generated.contains("Arrays.deepToString(list)"), "Arrays.deepToString call");

        Path root = Files.createTempDirectory("qin-js-backend-arrays-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-arrays\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\n[list.size(), list.get(0), text].join(\":\");\n",
                "js_backend_arrays");
        if (!"2:a:[a, b]".equals(result)) {
            throw new IllegalStateException("Expected generated Arrays result, got: " + result);
        }
        System.out.println("QinJsBackendJavaArraysSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
