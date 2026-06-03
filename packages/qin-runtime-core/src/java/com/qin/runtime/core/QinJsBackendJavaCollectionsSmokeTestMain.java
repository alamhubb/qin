package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaCollectionsSmokeTestMain {
    private QinJsBackendJavaCollectionsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "list",
                                new QinIrJavaNewExpression(
                                        "ArrayList",
                                        "java.util.ArrayList",
                                        List.of())),
                        new QinIrConstDeclaration(
                                "readonlyList",
                                new QinIrStaticMethodCallExpression(
                                        "Collections",
                                        "java.util.Collections",
                                        "unmodifiableList",
                                        List.of(new com.qin.lang.ir.QinIrIdentifierReference("list")))),
                        new QinIrConstDeclaration(
                                "map",
                                new QinIrJavaNewExpression(
                                        "HashMap",
                                        "java.util.HashMap",
                                        List.of())),
                        new QinIrConstDeclaration(
                                "readonlyMap",
                                new QinIrStaticMethodCallExpression(
                                        "Collections",
                                        "java.util.Collections",
                                        "unmodifiableMap",
                                        List.of(new com.qin.lang.ir.QinIrIdentifierReference("map"))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new QinIrJavaImport(
                                "java:java.util",
                                "ArrayList",
                                "ArrayList",
                                "java.util.ArrayList"),
                        new QinIrJavaImport(
                                "java:java.util",
                                "HashMap",
                                "HashMap",
                                "java.util.HashMap"),
                        new QinIrJavaImport(
                                "java:java.util",
                                "Collections",
                                "Collections",
                                "java.util.Collections")),
                List.of(),
                List.of(),
                List.of(
                        new QinIrJavaInstanceMethodCall(
                                "list",
                                "java.util.ArrayList",
                                "add",
                                List.of(new QinIrStringLiteral("alpha"))),
                        new QinIrJavaInstanceMethodCall(
                                "map",
                                "java.util.HashMap",
                                "put",
                                List.of(new QinIrStringLiteral("name"), new QinIrStringLiteral("qin")))),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaUtilCollections"), "Collections runtime facade");
        require(generated.contains("const Collections = __QinJavaUtilCollections;"), "Collections import alias");
        require(generated.contains("Collections.unmodifiableList(list)"), "unmodifiableList call");
        require(generated.contains("Collections.unmodifiableMap(map)"), "unmodifiableMap call");

        Path root = Files.createTempDirectory("qin-js-backend-collections-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-collections\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nlet listMutationFailed = false;\n"
                        + "let mapMutationFailed = false;\n"
                        + "try { readonlyList.add(\"beta\"); } catch (error) { listMutationFailed = true; }\n"
                        + "try { readonlyMap.put(\"other\", \"value\"); } catch (error) { mapMutationFailed = true; }\n"
                        + "readonlyList.get(0) + \":\" + readonlyMap.get(\"name\") + \":\" + listMutationFailed + \":\" + mapMutationFailed;\n",
                "js_backend_collections");
        if (!"alpha:qin:true:true".equals(result)) {
            throw new IllegalStateException("Expected generated Collections result, got: " + result);
        }
        System.out.println("QinJsBackendJavaCollectionsSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
