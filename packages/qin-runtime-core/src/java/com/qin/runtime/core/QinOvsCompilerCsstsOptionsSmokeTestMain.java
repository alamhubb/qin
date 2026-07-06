package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsCompilerCsstsOptionsSmokeTestMain {
    private QinOvsCompilerCsstsOptionsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-cssts-options-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  plugins: [
                    {
                      cssts: {
                        classPrefix: "unit"
                      }
                    }
                  ]
                }
                """, StandardCharsets.UTF_8);
        Path appDir = root.resolve("app");
        Files.createDirectories(appDir);
        Path file = appDir.resolve("Options.ovs");
        String source = """
                div(class = css { colorRed }) {
                  "options"
                }
                """;
        Files.writeString(file, source, StandardCharsets.UTF_8);

        QinOvsCompiler.QinOvsCompileResult result = new QinOvsCompiler().compile(root, file, source);
        String combined = result.code() + "\n" + result.css() + "\n" + result.atomModule();
        if (!combined.contains("unit_color_red")) {
            throw new IllegalStateException("Expected OVS standard transform to honor CSSTS classPrefix, got:\n"
                    + combined);
        }
        if (combined.contains("cssts_color_red")) {
            throw new IllegalStateException("OVS standard transform leaked default CSSTS prefix:\n" + combined);
        }

        System.out.println("QinOvsCompilerCsstsOptionsSmokeTestMain OK");
    }
}
