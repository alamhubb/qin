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

public final class QinJsBackendJavaHashMapSmokeTestMain {
    private QinJsBackendJavaHashMapSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(new QinIrConstDeclaration(
                        "map",
                        new QinIrJavaNewExpression(
                                "HashMap",
                                "java.util.HashMap",
                                List.of()))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util",
                        "HashMap",
                        "HashMap",
                        "java.util.HashMap")),
                List.of(),
                List.of(),
                List.of(new QinIrJavaInstanceMethodCall(
                        "map",
                        "java.util.HashMap",
                        "put",
                        List.of(new QinIrStringLiteral("name"), new QinIrStringLiteral("qin")))),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaUtilHashMap"), "HashMap runtime shim");
        require(generated.contains("const HashMap = __QinJavaUtilHashMap;"), "HashMap import alias");
        require(generated.contains("map.put(\"name\", \"qin\");"), "HashMap put call");

        Path root = Files.createTempDirectory("qin-js-backend-hash-map-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-hash-map\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nglobalThis.__qinResult.get(\"name\") + \":\" + globalThis.__qinResult.size();\n",
                "js_backend_hash_map");
        if (!"qin:1".equals(result)) {
            throw new IllegalStateException("Expected generated HashMap qin:1, got: " + result);
        }
        System.out.println("QinJsBackendJavaHashMapSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
