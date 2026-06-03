package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaFileSmokeTestMain {
    private QinJsBackendJavaFileSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "root",
                                new QinIrJavaNewExpression(
                                        "File",
                                        "java.io.File",
                                        List.of(new QinIrStringLiteral("/workspace/app")))),
                        new QinIrConstDeclaration(
                                "config",
                                new QinIrJavaNewExpression(
                                        "File",
                                        "java.io.File",
                                        List.of(new QinIrIdentifierReference("root"), new QinIrStringLiteral("qin.config.js")))),
                        new QinIrConstDeclaration(
                                "separator",
                                new QinIrMemberAccessExpression("File", "separator")),
                        new QinIrConstDeclaration(
                                "parent",
                                new QinIrInstanceMethodCallExpression(
                                        new QinIrIdentifierReference("config"),
                                        "getParentFile",
                                        List.of()))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.io",
                        "File",
                        "File",
                        "java.io.File")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaIoFile"), "File runtime shim");
        require(generated.contains("const File = __QinJavaIoFile;"), "File import alias");
        require(generated.contains("new File(\"/workspace/app\")"), "File string constructor");
        require(generated.contains("new File(root, \"qin.config.js\")"), "File parent/child constructor");
        require(generated.contains("File.separator"), "File.separator field");

        Path root = Files.createTempDirectory("qin-js-backend-file-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-file\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                "globalThis.__qinJavaFileSeparator = \"/\";\n"
                        + "globalThis.__qinJavaExistingFiles = { \"/workspace/app/qin.config.js\": true };\n"
                        + generated
                        + "\n[config.getPath(), config.getAbsolutePath(), parent.getPath(), config.exists(), separator].join(\":\");\n",
                "js_backend_file");
        if (!"/workspace/app/qin.config.js:/workspace/app/qin.config.js:/workspace/app:true:/".equals(result)) {
            throw new IllegalStateException("Expected generated File result, got: " + result);
        }
        System.out.println("QinJsBackendJavaFileSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
