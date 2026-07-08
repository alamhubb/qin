package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QinJavaProjectStringInstanceMethodEsmSmokeTestMain {
    private QinJavaProjectStringInstanceMethodEsmSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = findQinRoot();
        Path workspaceRoot = qinRoot.getParent();
        List<Path> sourceRoots = List.of(
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("subhuti-java")
                        .resolve("src").resolve("main").resolve("java"));
        Path root = Files.createTempDirectory("qin-java-string-instance-esm-");
        Path outputRoot = root.resolve("generated");
        List<QinJavaProjectJsCompiler.EsmFileOutput> outputs = new QinJavaProjectJsCompiler()
                .compileSuperclassClosureEsmTsFiles(
                        sourceRoots,
                        "com.subhuti.struct.SubhutiCreateToken",
                        outputRoot);
        Map<String, QinJavaProjectJsCompiler.EsmFileOutput> byBinaryName = outputs.stream()
                .collect(Collectors.toMap(QinJavaProjectJsCompiler.EsmFileOutput::binaryName, output -> output));
        QinJavaProjectJsCompiler.EsmFileOutput output = byBinaryName.get("com.subhuti.struct.SubhutiCreateToken");
        require(output != null, "SubhutiCreateToken output in " + byBinaryName.keySet());
        String code = output.code();
        require(code.contains("__QinJavaLangString.isBlank(name)"),
                "String.isBlank instance call must use java-sdk-js");
        require(!code.contains("name.isBlank()"),
                "String.isBlank must not emit a non-standard JS string method");
        System.out.println("QinJavaProjectStringInstanceMethodEsmSmokeTestMain OK");
    }

    private static Path findQinRoot() {
        Path search = Path.of("").toAbsolutePath().normalize();
        while (search != null) {
            if (Files.isDirectory(search.resolve("packages").resolve("qin-runtime-core"))
                    && Files.isDirectory(search.resolve("packages").resolve("qin-language"))
                    && Files.isRegularFile(search.resolve("qin.config.js"))) {
                return search;
            }
            search = search.getParent();
        }
        throw new IllegalStateException("Cannot find Qin repo root");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
