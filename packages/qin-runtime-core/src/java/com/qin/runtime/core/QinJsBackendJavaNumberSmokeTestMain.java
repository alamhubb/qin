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
                                        List.of(new QinIrStringLiteral("ff"), new QinIrNumberLiteral(16)))),
                        new QinIrConstDeclaration(
                                "parsedLong",
                                new QinIrStaticMethodCallExpression(
                                        "Long",
                                        "java.lang.Long",
                                        "parseLong",
                                        List.of(new QinIrStringLiteral("ff"), new QinIrNumberLiteral(16)))),
                        new QinIrConstDeclaration(
                                "longCompare",
                                new QinIrStaticMethodCallExpression(
                                        "Long",
                                        "java.lang.Long",
                                        "compare",
                                        List.of(new QinIrNumberLiteral(3), new QinIrNumberLiteral(8)))),
                        new QinIrConstDeclaration(
                                "integerHash",
                                new QinIrStaticMethodCallExpression(
                                        "Integer",
                                        "java.lang.Integer",
                                        "hashCode",
                                        List.of(new QinIrNumberLiteral(255)))),
                        new QinIrConstDeclaration(
                                "longHash",
                                new QinIrStaticMethodCallExpression(
                                        "Long",
                                        "java.lang.Long",
                                        "hashCode",
                                        List.of(new QinIrNumberLiteral(3))))),
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
                                "java.lang.Integer"),
                        new QinIrJavaImport(
                                "java:java.lang",
                                "Long",
                                "Long",
                                "java.lang.Long")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaLangInteger = {"), "Integer runtime shim");
        require(generated.contains("const __QinJavaLangDouble = {"), "Double runtime shim");
        require(generated.contains("const Double = __QinJavaLangDouble;"), "Double import alias");
        require(generated.contains("const __QinJavaLangLong = {"), "Long runtime shim");
        require(generated.contains("const Long = __QinJavaLangLong;"), "Long import alias");
        require(generated.contains("Double.parseDouble(\"12.5\")"), "Double.parseDouble call");
        require(generated.contains("Integer.parseInt(\"ff\", "), "Integer.parseInt radix call");
        require(generated.contains("Long.parseLong(\"ff\", "), "Long.parseLong radix call");
        require(generated.contains("Number(255.0) | 0"), "Integer.hashCode static lowering");
        require(generated.contains("__qin_long_hash_input"), "Long.hashCode static lowering");

        Path root = Files.createTempDirectory("qin-js-backend-number-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-number\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nparsedDouble + \":\" + doubleCompare + \":\" + hexInt + \":\" + parsedLong + \":\" + longCompare"
                        + " + \":\" + integerHash + \":\" + longHash;\n",
                "js_backend_number");
        if (!"12.5:1:255:255:-1:255:3".equals(result)) {
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
