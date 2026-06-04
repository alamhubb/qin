package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaStringFormatSmokeTestMain {
    private QinJsBackendJavaStringFormatSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "message",
                        new QinIrStaticMethodCallExpression(
                                "String",
                                "java.lang.String",
                                "format",
                                List.of(
                                        new QinIrStringLiteral("token %s\n%c\tat %d"),
                                        new QinIrStringLiteral("IDENT"),
                                        new QinIrStringLiteral("x"),
                                        new QinIrNumberLiteral(7))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.lang",
                        "String",
                        "String",
                        "java.lang.String")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaLangString"), "String runtime shim");
        require(generated.contains("__QinJavaLangString.format(\"token %s\\n%c\\tat %d\", \"IDENT\", \"x\", 7.0)"),
                "String.format call");

        Path root = Files.createTempDirectory("qin-js-backend-string-format-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-string-format\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nglobalThis.__qinResult;\n",
                "js_backend_string_format");
        if (!"token IDENT\nx\tat 7".equals(result)) {
            throw new IllegalStateException("Expected generated String.format result, got: " + result);
        }
        System.out.println("QinJsBackendJavaStringFormatSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
