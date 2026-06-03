package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaSetSmokeTestMain {
    private QinJsBackendJavaSetSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "fixed",
                                new QinIrStaticMethodCallExpression(
                                        "Set",
                                        "java.util.Set",
                                        "of",
                                        List.of(new QinIrStringLiteral("alpha"), new QinIrStringLiteral("beta")))),
                        new QinIrConstDeclaration(
                                "mutable",
                                new QinIrJavaNewExpression(
                                        "HashSet",
                                        "java.util.HashSet",
                                        List.of(new QinIrIdentifierReference("fixed")))),
                        new QinIrConstDeclaration(
                                "added",
                                new QinIrInstanceMethodCallExpression(
                                        new QinIrIdentifierReference("mutable"),
                                        "add",
                                        List.of(new QinIrStringLiteral("gamma")))),
                        new QinIrConstDeclaration(
                                "containsBeta",
                                new QinIrInstanceMethodCallExpression(
                                        new QinIrIdentifierReference("mutable"),
                                        "contains",
                                        List.of(new QinIrStringLiteral("beta")))),
                        new QinIrConstDeclaration(
                                "removedAlpha",
                                new QinIrInstanceMethodCallExpression(
                                        new QinIrIdentifierReference("mutable"),
                                        "remove",
                                        List.of(new QinIrStringLiteral("alpha")))),
                        new QinIrConstDeclaration(
                                "empty",
                                new QinIrStaticMethodCallExpression(
                                        "Collections",
                                        "java.util.Collections",
                                        "emptySet",
                                        List.of()))),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new QinIrJavaImport("java:java.util", "Set", "Set", "java.util.Set"),
                        new QinIrJavaImport("java:java.util", "HashSet", "HashSet", "java.util.HashSet"),
                        new QinIrJavaImport("java:java.util", "Collections", "Collections", "java.util.Collections")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaUtilHashSet"), "HashSet runtime shim");
        require(generated.contains("const __QinJavaUtilSet"), "Set runtime shim");
        require(generated.contains("const Set = __QinJavaUtilSet;"), "Set alias");
        require(generated.contains("const HashSet = __QinJavaUtilHashSet;"), "HashSet alias");
        require(generated.contains("const Collections = __QinJavaUtilCollections;"), "Collections alias");
        require(generated.contains("Set.of(\"alpha\", \"beta\")"), "Set.of call");
        require(generated.contains("new HashSet(fixed)"), "HashSet constructor");
        require(generated.contains("Collections.emptySet()"), "Collections.emptySet call");

        Path root = Files.createTempDirectory("qin-js-backend-set-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-set\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + """
                        
                        [
                          added,
                          containsBeta,
                          removedAlpha,
                          mutable.size(),
                          Array.from(mutable).join(","),
                          empty.isEmpty()
                        ].join(":");
                        """,
                "js_backend_set");
        if (!"true:true:true:2:beta,gamma:true".equals(result)) {
            throw new IllegalStateException("Expected generated Set result, got: " + result);
        }
        System.out.println("QinJsBackendJavaSetSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
