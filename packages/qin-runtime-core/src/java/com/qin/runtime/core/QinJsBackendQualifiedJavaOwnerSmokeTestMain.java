package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendQualifiedJavaOwnerSmokeTestMain {
    private QinJsBackendQualifiedJavaOwnerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "map",
                        new QinIrJavaNewExpression(
                                "java.util.HashMap",
                                "java.util.HashMap",
                                List.of()))),
                List.<QinIrExpressionStatement>of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util",
                        "HashMap",
                        "java.util.HashMap",
                        "java.util.HashMap")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(!generated.contains("const java.util.HashMap"), "no dotted Java alias");
        require(generated.contains("new __QinJavaUtilHashMap()"), "qualified owner runtime reference");

        Path root = Files.createTempDirectory("qin-js-backend-qualified-java-owner-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-qualified-java-owner\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nglobalThis.__qinResult.size();\n",
                "qualified_java_owner");
        if (!"0".equals(String.valueOf(result))) {
            throw new IllegalStateException("Expected generated qualified HashMap to run, got: " + result);
        }
        System.out.println("QinJsBackendQualifiedJavaOwnerSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
