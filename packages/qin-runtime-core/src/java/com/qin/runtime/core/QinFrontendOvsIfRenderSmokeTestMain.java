package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFrontendOvsIfRenderSmokeTestMain {
    private QinFrontendOvsIfRenderSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-ovs-if-render-");
        Path app = root.resolve("app");
        Files.createDirectories(app);
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-frontend-ovs-if-render\" }\n",
                StandardCharsets.UTF_8);
        Path sourceFile = app.resolve("Sparkline.ovs");
        String source = """
                export const Sparkline = (points) => {
                  return div(class = "spark-cell") {
                    if (points) {
                      svg(class = "sparkline") {
                        polyline(points = points; fill = "none") {}
                      }
                    } else {
                      span(class = "muted") { "-" }
                    }
                  }
                }
                """;
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);

        QinOvsCompiler.QinOvsCompileResult result = new QinOvsCompiler().compile(root, sourceFile, source);
        String module = result.code();
        if (module == null
                || !module.contains("export const Sparkline")
                || !module.contains("if (points)")
                || !module.contains("polyline")
                || !module.contains("sparkline")
                || !module.contains("muted")
                || module.contains("Unsupported Java overload")
                || module.contains("type=Const")) {
            throw new IllegalStateException("OVS if render branch was not preserved in browser module:\n" + module);
        }

        System.out.println("QinFrontendOvsIfRenderSmokeTestMain passed.");
    }
}
