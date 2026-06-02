package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaArrayListSmokeTestMain {
    private QinJsBackendJavaArrayListSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "list",
                        new QinIrJavaNewExpression(
                                "ArrayList",
                                "java.util.ArrayList",
                                List.of()))),
                List.<QinIrExpressionStatement>of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util",
                        "ArrayList",
                        "ArrayList",
                        "java.util.ArrayList")),
                List.of(),
                List.of(),
                List.of(new QinIrJavaInstanceMethodCall(
                        "list",
                        "java.util.ArrayList",
                        "add",
                        List.of(new QinIrStringLiteral("hello")))),
                List.of(new QinIrConsoleLogJavaInstanceCall(
                        "list",
                        "java.util.ArrayList",
                        "size",
                        List.of())),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaUtilArrayList"), "ArrayList runtime shim");
        require(generated.contains("const ArrayList = __QinJavaUtilArrayList;"), "ArrayList import alias");
        require(generated.contains("list.add(\"hello\");"), "instance add call");
        require(generated.contains("console.log(list.size());"), "instance console call");

        Path root = Files.createTempDirectory("qin-js-backend-array-list-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-array-list\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nglobalThis.__qinResult.size();\n",
                "js_backend_array_list");
        if (!Integer.valueOf(1).equals(result)) {
            throw new IllegalStateException("Expected generated ArrayList size 1, got: " + result);
        }
        System.out.println("QinJsBackendJavaArrayListSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
