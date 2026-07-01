package com.qin.debug.lsp;

import com.qin.debug.QinConfigSupport;
import com.qin.types.QinConfig;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinToolWindowConfigTreeSmokeTestMain {
    private QinToolWindowConfigTreeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-tool-window-config-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "tool-window-smoke",
                  version: "1.0.0",
                  scripts: {
                    dev: "qin dev",
                    "type-check": "qin language run type-check"
                  },
                  dependencies: {
                    "com.qin:qin-runtime-core": "0.1.0"
                  },
                  devDependencies: {
                    "tsdown": "^0.20.0"
                  }
                }
                """, StandardCharsets.UTF_8);

        QinConfig config = QinConfigSupport.load(root);
        require(config != null, "config loaded");

        Map<String, String> scripts = QinConfigSupport.scripts(config);
        require("qin dev".equals(scripts.get("dev")), "scripts.dev exposed to tool window");
        require(scripts.containsKey("type-check"), "scripts.type-check exposed to tool window");

        Map<String, String> dependencies = QinConfigSupport.dependencies(config);
        require("0.1.0".equals(dependencies.get("com.qin:qin-runtime-core")),
                "dependencies exposed to tool window");

        Map<String, String> devDependencies = QinConfigSupport.devDependencies(config);
        require("^0.20.0".equals(devDependencies.get("tsdown")),
                "devDependencies exposed to tool window");

        System.out.println("Qin tool window config tree smoke passed");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
