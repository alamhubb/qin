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
                  port: 19114,
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
                  generated: {
                    source: 'java',
                    entryBinaryName: 'com.qin.demo.Parser',
                    sourceRoots: ['src/main/java', '../slime/java-slime/slime-parser/src/main/java'],
                    outputDir: 'generated/parser-ts'
                  },
                  languageServer: {
                    sourceExtension: '.qin',
                    serviceExtension: '.ts',
                    generatedParserTarget: '@qin/generated-qin-parser-ts',
                    parserPackage: 'com.qin:qin-parser'
                  },
                  qinLanguage: {
                    sourceExtension: '.qin',
                    serviceExtension: '.ts',
                    parserPackage: 'com.qin:qin-parser',
                    generatedParserTarget: '@qin/generated-qin-parser-ts'
                  },
                  plugins: [vue()]
                }
                """, StandardCharsets.UTF_8);

        QinConfig config = new ConfigLoader(root.toString()).load();
        require("com.qin.demo:config-parser".equals(config.name()), "name");
        require("0.2.0".equals(config.version()), "version");
        require(config.port() != null && config.port() == 19114, "port");
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
        require(config.generated() != null && "java".equals(config.generated().source()), "generated.source");
        require("com.qin.demo.Parser".equals(config.generated().entryBinaryName()), "generated.entryBinaryName");
        require(config.generated().sourceRoots().size() == 2, "generated.sourceRoots");
        require("generated/parser-ts".equals(config.generated().outputDir()), "generated.outputDir");
        require(config.languageServer() != null, "languageServer");
        require(".qin".equals(config.languageServer().sourceExtension()), "languageServer.sourceExtension");
        require(".ts".equals(config.languageServer().serviceExtension()), "languageServer.serviceExtension");
        require("@qin/generated-qin-parser-ts".equals(config.languageServer().generatedParserTarget()),
                "languageServer.generatedParserTarget");
        require("com.qin:qin-parser".equals(config.languageServer().parserPackage()), "languageServer.parserPackage");
        require(config.qinLanguage() != null, "qinLanguage");
        require(".qin".equals(config.qinLanguage().sourceExtension()), "qinLanguage.sourceExtension");
        require(".ts".equals(config.qinLanguage().serviceExtension()), "qinLanguage.serviceExtension");
        require("com.qin:qin-parser".equals(config.qinLanguage().parserPackage()), "qinLanguage.parserPackage");
        require("@qin/generated-qin-parser-ts".equals(config.qinLanguage().generatedParserTarget()),
                "qinLanguage.generatedParserTarget");
        require(config.packages().size() == 1 && "packages/*".equals(config.packages().getFirst()), "packages");

        Path workspaceRoot = Files.createTempDirectory("qin-config-js-workspaces-");
        Files.writeString(workspaceRoot.resolve("qin.config.js"), """
                export default {
                  name: 'com.qin.demo:workspace-root',
                  version: '0.1.0',
                  workspaces: [
                    'packages/a',
                    'packages/b'
                  ]
                }
                """, StandardCharsets.UTF_8);
        QinConfig workspaceConfig = new ConfigLoader(workspaceRoot.toString()).load();
        require(workspaceConfig.packages().size() == 2, "workspaces size");
        require("packages/a".equals(workspaceConfig.packages().get(0)), "workspaces first item");
        require("packages/b".equals(workspaceConfig.packages().get(1)), "workspaces second item");

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
