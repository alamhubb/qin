package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsReExportAllSmokeTestMain {
    private QinJsReExportAllSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-re-export-all-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-re-export-all\" }\n",
                StandardCharsets.UTF_8);
        Path packageDir = root.resolve("node_modules").resolve("local-barrel");
        Files.createDirectories(packageDir.resolve("src"));
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "local-barrel",
                  "type": "module",
                  "main": "./src/index.ts",
                  "exports": {
                    ".": {
                      "import": "./src/index.ts",
                      "default": "./src/index.ts"
                    }
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("src").resolve("value.ts"), """
                export const x = 42;
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("src").resolve("index.ts"), """
                export * from "./value.ts";
                """, StandardCharsets.UTF_8);

        String wrapper = """
                import { x } from "local-barrel";
                ({ x });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "js_re_export_all");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
