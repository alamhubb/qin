package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsCompilerArrowBlockBodySmokeTestMain {
    private QinOvsCompilerArrowBlockBodySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-arrow-block-body-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-ovs-arrow-block-body-smoke\" }\n", StandardCharsets.UTF_8);
        Path sourceFile = root.resolve("app").resolve("ArrowBlockBody.ovs");
        Files.createDirectories(sourceFile.getParent());

        String source = """
                const normalizeRootUrl = (value) => {
                  const text = String(value || "").trim()
                  if (!text) {
                    return ""
                  }
                  return text
                }

                div(onClick() { console.log(normalizeRootUrl("https://example.test")) }) {
                  "Open"
                }
                """;
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);

        QinOvsCompiler.QinOvsCompileResult result = new QinOvsCompiler().compile(root, sourceFile, source);
        String code = result.code();
        if (code.contains("const normalizeRootUrl = (value) => ;") || code.contains("=> ;")) {
            throw new IllegalStateException("OVS compiler dropped arrow block body:\n" + code);
        }
        if (!code.contains("const text = String")
                || !code.contains("return text")
                || !code.contains("console.log(normalizeRootUrl")) {
            throw new IllegalStateException("OVS compiler failed to preserve arrow/method block statements:\n" + code);
        }

        System.out.println("QinOvsCompilerArrowBlockBodySmokeTestMain OK");
    }
}
