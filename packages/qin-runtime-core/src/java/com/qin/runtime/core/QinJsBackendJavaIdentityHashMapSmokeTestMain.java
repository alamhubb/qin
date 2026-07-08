package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaIdentityHashMapSmokeTestMain {
    private QinJsBackendJavaIdentityHashMapSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "identityMap",
                                new QinIrJavaNewExpression(
                                        "IdentityHashMap",
                                        "java.util.IdentityHashMap",
                                        List.of())),
                        new QinIrConstDeclaration(
                                "identitySet",
                                new QinIrStaticMethodCallExpression(
                                        "Collections",
                                        "java.util.Collections",
                                        "newSetFromMap",
                                        List.of(new QinIrIdentifierReference("identityMap"))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new QinIrJavaImport(
                                "java:java.util",
                                "IdentityHashMap",
                                "IdentityHashMap",
                                "java.util.IdentityHashMap"),
                        new QinIrJavaImport(
                                "java:java.util",
                                "Collections",
                                "Collections",
                                "java.util.Collections")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaUtilIdentityHashMap"), "IdentityHashMap runtime shim");
        require(generated.contains("class __QinJavaUtilMapBackedSet"), "Collections.newSetFromMap set runtime");
        require(generated.contains("const IdentityHashMap = __QinJavaUtilIdentityHashMap;"),
                "IdentityHashMap import alias");
        require(generated.contains("const Collections = __QinJavaUtilCollections;"), "Collections import alias");
        require(generated.contains("Collections.newSetFromMap(identityMap)"), "newSetFromMap call");

        Path root = Files.createTempDirectory("qin-js-backend-identity-hash-map-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-identity-hash-map\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nclass Key {\n"
                        + "  constructor(name) { this.name = name; }\n"
                        + "  hashCode() { return 1; }\n"
                        + "  equals(other) { return other instanceof Key && other.name === this.name; }\n"
                        + "}\n"
                        + "const keyA = new Key(\"same\");\n"
                        + "const keyB = new Key(\"same\");\n"
                        + "const first = identitySet.add(keyA);\n"
                        + "const duplicate = identitySet.add(keyA);\n"
                        + "const distinctEqualObject = identitySet.add(keyB);\n"
                        + "const hasA = identitySet.contains(keyA);\n"
                        + "const hasFreshEqualObject = identitySet.contains(new Key(\"same\"));\n"
                        + "const removedA = identitySet.remove(keyA);\n"
                        + "let remaining = \"\";\n"
                        + "for (const key of identitySet) { remaining = remaining + key.name; }\n"
                        + "first + \":\" + duplicate + \":\" + distinctEqualObject + \":\""
                        + " + hasA + \":\" + hasFreshEqualObject + \":\" + removedA + \":\""
                        + " + identityMap.size() + \":\" + identityMap.containsKey(keyA) + \":\""
                        + " + identityMap.containsKey(keyB) + \":\" + identityMap.get(keyB) + \":\" + remaining;\n",
                "js_backend_identity_hash_map");
        if (!"true:false:true:true:false:true:1:false:true:true:same".equals(result)) {
            throw new IllegalStateException("Expected generated IdentityHashMap result, got: " + result);
        }
        System.out.println("QinJsBackendJavaIdentityHashMapSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
