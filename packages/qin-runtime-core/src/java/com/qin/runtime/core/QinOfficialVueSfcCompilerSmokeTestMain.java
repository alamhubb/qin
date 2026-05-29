package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinOfficialVueSfcCompilerSmokeTestMain {
    private QinOfficialVueSfcCompilerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-official-vue-sfc-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-official-vue-smoke\" }\n", StandardCharsets.UTF_8);

        Path packageDir = Files.createDirectories(
                root.resolve("node_modules").resolve("@vue").resolve("compiler-sfc").resolve("dist"));
        Files.writeString(packageDir.getParent().resolve("package.json"), """
                {
                  "name": "@vue/compiler-sfc",
                  "exports": {
                    ".": {
                      "import": "./dist/compiler-sfc.esm-browser.js",
                      "default": "./dist/compiler-sfc.esm-browser.js"
                    }
                  },
                  "module": "./dist/compiler-sfc.esm-browser.js",
                  "main": "./dist/compiler-sfc.cjs.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("compiler-sfc.esm-browser.js"), """
                export function parse(source, options) {
                  return {
                    descriptor: {
                      filename: options.filename,
                      source: source,
                      template: {
                        kind: "template",
                        tag: "template",
                        attrs: {},
                        rawAttrs: [],
                        content: "<div>Hello Official Qin Vue</div>",
                        loc: {},
                        openTagLoc: {},
                        closeTagLoc: {},
                        lang: null,
                        setup: false,
                        scoped: false,
                        module: null,
                        src: null
                      },
                      script: null,
                      scriptSetup: null,
                      styles: [],
                      customBlocks: [],
                      errors: []
                    }
                  };
                }
                """, StandardCharsets.UTF_8);

        Path appDir = Files.createDirectories(root.resolve("app"));
        Path vueFile = appDir.resolve("App.vue");
        String source = """
                <template>
                  <div>Hello Official Qin Vue</div>
                </template>
                """;
        Files.writeString(vueFile, source, StandardCharsets.UTF_8);

        QinOfficialVueSfcCompiler compiler = new QinOfficialVueSfcCompiler();
        QinVueSfcModuleResult result = compiler.transpileVueModule(
                vueFile,
                source,
                new QinModuleSource(vueFile.toAbsolutePath().normalize(), source, List.of()),
                specifier -> specifier);
        String transpiled = result.moduleCode();

        if (!transpiled.contains("__qin_vue_descriptor")) {
            throw new IllegalStateException("Expected descriptor export in transpiled Vue module.");
        }
        if (!transpiled.contains("Hello Official Qin Vue")) {
            throw new IllegalStateException("Expected official compiler descriptor content in transpiled module.");
        }
        if (!transpiled.contains("export default")) {
            throw new IllegalStateException("Expected default export in transpiled Vue module.");
        }

        System.out.println("QinOfficialVueSfcCompilerSmokeTestMain passed.");
    }
}
