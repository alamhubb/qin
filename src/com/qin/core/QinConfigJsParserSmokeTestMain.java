package com.qin.core;

import com.qin.types.QinConfig;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinConfigJsParserSmokeTestMain {
    private QinConfigJsParserSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-config-js-parser-");
        Files.writeString(root.resolve("qin.config.js"), """
                import vue from '@vitejs/plugin-vue'

                export default {
                  name: 'com.qin.demo:config-parser',
                  version: '0.2.0',
                  description: 'bootstrap parser smoke',
                  entry: 'src/main.js',
                  packages: ['packages/*'],
                  frontend: {
                    srcDir: 'src',
                    outDir: 'dist',
                    devPort: 19097,
                    entry: 'src/main.vue',
                    staticDir: 'public'
                  },
                  backend: {
                    sourceDir: 'server',
                    entry: 'server/Main.java'
                  },
                  dependencies: {
                    'com.qin:qin-runtime-core': '0.1.0',
                    '@vue/compiler-sfc': '^3.5.34'
                  },
                  devDependencies: {
                    '@vitejs/plugin-vue': '^6.0.7',
                    vite: '^8.0.13'
                  },
                  java: {
                    version: '25',
                    outputDir: 'build/classes',
                    encoding: 'UTF-8'
                  },
                  scripts: {
                    dev: 'qin dev --port 19097'
                  },
                  plugins: [vue()]
                }
                """, StandardCharsets.UTF_8);

        QinConfig config = new ConfigLoader(root.toString()).load();
        require("com.qin.demo:config-parser".equals(config.name()), "name");
        require("0.2.0".equals(config.version()), "version");
        require("src/main.js".equals(config.entry()), "entry");
        require(config.frontend() != null && config.frontend().devPort() == 19097, "frontend.devPort");
        require("src/main.vue".equals(config.frontend().entry()), "frontend.entry");
        require("public".equals(config.frontend().staticDir()), "frontend.staticDir");
        require(config.backend() != null && "server".equals(config.backend().sourceDir()), "backend.sourceDir");
        require("server/Main.java".equals(config.backend().entry()), "backend.entry");
        require("^3.5.34".equals(config.dependencies().get("@vue/compiler-sfc")), "scoped dependency");
        require("^6.0.7".equals(config.devDependencies().get("@vitejs/plugin-vue")), "scoped devDependency");
        require("25".equals(config.java().version()), "java.version");
        require("qin dev --port 19097".equals(config.scripts().get("dev")), "scripts.dev");
        require(config.packages().size() == 1 && "packages/*".equals(config.packages().getFirst()), "packages");

        Path frontendOnlyRoot = Files.createTempDirectory("qin-config-js-frontend-only-");
        Files.createDirectories(frontendOnlyRoot.resolve("src"));
        Files.writeString(frontendOnlyRoot.resolve("src").resolve("main.ts"), "console.log('frontend only')\n", StandardCharsets.UTF_8);
        Files.writeString(frontendOnlyRoot.resolve("qin.config.js"), """
                export default {
                  name: 'com.qin.demo:frontend-only',
                  frontend: {
                    srcDir: 'src',
                    entry: 'src/main.ts'
                  }
                }
                """, StandardCharsets.UTF_8);
        QinConfig frontendOnlyConfig = new ConfigLoader(frontendOnlyRoot.toString()).load();
        require(frontendOnlyConfig.entry() == null, "frontend-only top-level entry");
        require("src/main.ts".equals(frontendOnlyConfig.frontend().entry()), "frontend-only frontend.entry");

        System.out.println("QinConfigJsParserSmokeTestMain OK");
    }

    private static void require(boolean condition, String field) {
        if (!condition) {
            throw new IllegalStateException("Failed to parse " + field + " from qin.config.js");
        }
    }
}
