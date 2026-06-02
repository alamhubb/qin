package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsImportSmokeTestMain {
    private QinCsstsImportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-import-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-cssts-import\" }\n", StandardCharsets.UTF_8);
        String source = """
                import "./OvsDemo.ovs"

                const titleStyle = css { colorBlue, fontWeight700 }
                """;
        QinCsstsCompiler.QinCsstsCompileResult result = new QinCsstsCompiler().compile(root, source);
        if (!result.code().contains("import \"./OvsDemo.ovs\"")) {
            throw new IllegalStateException("CSSTS static import was not preserved:\n" + result.code());
        }
        if (!result.code().contains("cssts.merge(")
                || !result.code().contains("colorBlue")
                || !result.code().contains("fontWeight700")) {
            throw new IllegalStateException("CSSTS atoms were not rewritten:\n" + result.code());
        }
        System.out.println("QinCsstsImportSmokeTestMain passed.");
    }
}

