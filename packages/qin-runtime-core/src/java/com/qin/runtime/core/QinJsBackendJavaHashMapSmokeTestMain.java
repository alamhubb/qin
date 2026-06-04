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
                        "java.util.HashMap"),
                        new QinIrJavaImport(
                                "java:java.util",
                                "Arrays",
                                "Arrays",
                                "java.util.Arrays")),
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
        require(generated.contains("getOrDefault(key, defaultValue)"), "HashMap getOrDefault runtime");
        require(generated.contains("values()"), "HashMap values runtime");
        require(generated.contains("function __qin_java_hash_key__"), "HashMap Java hash key runtime");
        require(generated.contains("function __qin_java_hash_key_equals__"), "HashMap Java equals runtime");
        require(generated.contains("const __QinJavaUtilArrays"), "Arrays runtime shim");
        require(generated.contains("hashCode(value)"), "Arrays hashCode runtime");
        require(generated.contains("equals(left, right)"), "Arrays equals runtime");

        Path root = Files.createTempDirectory("qin-js-backend-hash-map-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-hash-map\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst created = globalThis.__qinResult.computeIfAbsent(\"missing\", key => key + \"-value\");\n"
                        + "globalThis.__qinResult.put(\"nullish\", null);\n"
                        + "const existing = globalThis.__qinResult.putIfAbsent(\"name\", \"changed\");\n"
                        + "const absent = globalThis.__qinResult.putIfAbsent(\"new\", \"inserted\");\n"
                        + "let valueSum = \"\";\n"
                        + "for (const value of globalThis.__qinResult.values()) { valueSum = valueSum + value + \",\"; }\n"
                        + "class Key {\n"
                        + "  constructor(name) { this.name = name; }\n"
                        + "  hashCode() { return this.name === \"same\" ? 7 : 7; }\n"
                        + "  equals(other) { return other instanceof Key && other.name === this.name; }\n"
                        + "}\n"
                        + "const objectMap = new __QinJavaUtilHashMap();\n"
                        + "objectMap.put(new Key(\"same\"), \"first\");\n"
                        + "const equalGet = objectMap.get(new Key(\"same\"));\n"
                        + "const equalExisting = objectMap.putIfAbsent(new Key(\"same\"), \"changed\");\n"
                        + "objectMap.put(new Key(\"other\"), \"collision\");\n"
                        + "const hasEqual = objectMap.containsKey(new Key(\"same\"));\n"
                        + "const removed = objectMap.remove(new Key(\"same\"));\n"
                        + "const hasAfterRemove = objectMap.containsKey(new Key(\"same\"));\n"
                        + "const arraysEqual = __QinJavaUtilArrays.equals([new Key(\"same\")], [new Key(\"same\")]);\n"
                        + "const hashesEqual = __QinJavaUtilArrays.hashCode([new Key(\"same\")])"
                        + " === __QinJavaUtilArrays.hashCode([new Key(\"same\")]);\n"
                        + "created + \":\" + existing + \":\" + absent + \":\" + globalThis.__qinResult.get(\"name\") + \":\""
                        + " + globalThis.__qinResult.getOrDefault(\"absent\", \"fallback\") + \":\""
                        + " + globalThis.__qinResult.getOrDefault(\"nullish\", \"fallback\") + \":\""
                        + " + globalThis.__qinResult.size() + \":\" + valueSum + \":\""
                        + " + equalGet + \":\" + equalExisting + \":\" + objectMap.size() + \":\""
                        + " + hasEqual + \":\" + removed + \":\" + hasAfterRemove + \":\""
                        + " + objectMap.get(new Key(\"other\")) + \":\" + arraysEqual + \":\" + hashesEqual;\n",
                "js_backend_hash_map");
        if (!"missing-value:qin:null:qin:fallback:null:4:qin,missing-value,null,inserted,:first:first:1:true:first:false:collision:true:true"
                .equals(result)) {
            throw new IllegalStateException("Expected generated HashMap computeIfAbsent result, got: " + result);
        }
        System.out.println("QinJsBackendJavaHashMapSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
