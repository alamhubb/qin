package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaConcurrentHashMapSmokeTestMain {
    private QinJsBackendJavaConcurrentHashMapSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "cache",
                        new QinIrJavaNewExpression(
                                "ConcurrentHashMap",
                                "java.util.concurrent.ConcurrentHashMap",
                                List.of()))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util.concurrent",
                        "ConcurrentHashMap",
                        "ConcurrentHashMap",
                        "java.util.concurrent.ConcurrentHashMap")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaUtilHashMap"), "ConcurrentHashMap runtime shim");
        require(generated.contains("const ConcurrentHashMap = __QinJavaUtilHashMap;"),
                "ConcurrentHashMap import alias");

        Path root = Files.createTempDirectory("qin-js-backend-concurrent-hash-map-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "export default { name: \"qin-js-backend-concurrent-hash-map\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst created = globalThis.__qinResult.computeIfAbsent(\"parser\", key => key + \"-cache\");\n"
                        + "created + \":\" + globalThis.__qinResult.get(\"parser\") + \":\" + globalThis.__qinResult.size();\n",
                "js_backend_concurrent_hash_map");
        if (!"parser-cache:parser-cache:1".equals(result)) {
            throw new IllegalStateException("Expected generated ConcurrentHashMap computeIfAbsent result, got: " + result);
        }
        System.out.println("QinJsBackendJavaConcurrentHashMapSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
