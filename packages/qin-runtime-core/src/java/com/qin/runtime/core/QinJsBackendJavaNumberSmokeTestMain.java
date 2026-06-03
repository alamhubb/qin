package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaNumberSmokeTestMain {
    private QinJsBackendJavaNumberSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "parsedDouble",
                                new QinIrStaticMethodCallExpression(
                                        "Double",
                                        "java.lang.Double",
                                        "parseDouble",
                                        List.of(new QinIrStringLiteral("12.5")))),
                        new QinIrConstDeclaration(
                                "doubleCompare",
                                new QinIrStaticMethodCallExpression(
                                        "Double",
                                        "java.lang.Double",
                                        "compare",
                                        List.of(new QinIrNumberLiteral(8), new QinIrNumberLiteral(3)))),
                        new QinIrConstDeclaration(
                                "hexInt",
                                new QinIrStaticMethodCallExpression(
                                        "Integer",
                                        "java.lang.Integer",
                                        "parseInt",
                                        List.of(new QinIrStringLiteral("ff"), new QinIrNumberLiteral(16))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new QinIrJavaImport(
                                "java:java.lang",
                                "Double",
                                "Double",
                                "java.lang.Double"),
                        new QinIrJavaImport(
                                "java:java.lang",
                                "Integer",
                                "Integer",
                                "java.lang.Integer")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaLangDouble"), "Double runtime shim");
        require(generated.contains("const Double = __QinJavaLangDouble;"), "Double import alias");
        require(generated.contains("Double.parseDouble(\"12.5\")"), "Double.parseDouble call");
        require(generated.contains("Integer.parseInt(\"ff\", "), "Integer.parseInt radix call");

        Path root = Files.createTempDirectory("qin-js-backend-number-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-number\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nparsedDouble + \":\" + doubleCompare + \":\" + hexInt;\n",
                "js_backend_number");
        if (!"12.5:1:255".equals(result)) {
            throw new IllegalStateException("Expected generated number result, got: " + result);
        }
        System.out.println("QinJsBackendJavaNumberSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
