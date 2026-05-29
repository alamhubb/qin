package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinVueCompilerSfcPackageLocatorTestMain {
    private QinVueCompilerSfcPackageLocatorTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-vue-compiler-sfc-");
        Path appDir = Files.createDirectories(root.resolve("app"));
        Path packageDir = Files.createDirectories(root.resolve("node_modules")
                .resolve("@vue")
                .resolve("compiler-sfc")
                .resolve("dist"));

        Files.writeString(root.resolve("node_modules").resolve("@vue").resolve("compiler-sfc").resolve("package.json"), """
                {
                  "name": "@vue/compiler-sfc",
                  "version": "3.5.34",
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
        Files.writeString(packageDir.resolve("compiler-sfc.esm-browser.js"), "export const parse = () => ({});\n", StandardCharsets.UTF_8);
        Files.writeString(appDir.resolve("main.js"), "console.log('qin');\n", StandardCharsets.UTF_8);

        QinVueCompilerSfcPackageLocator locator = new QinVueCompilerSfcPackageLocator();
        QinVueCompilerSfcPackageLocator.QinVueCompilerSfcPackageLocation location =
                locator.locate(appDir.resolve("main.js"));

        if (!location.found()) {
            throw new IllegalStateException("Expected @vue/compiler-sfc to be found.");
        }
        String entry = location.entryFile().toString().replace('\\', '/');
        if (!entry.endsWith("/node_modules/@vue/compiler-sfc/dist/compiler-sfc.esm-browser.js")) {
            throw new IllegalStateException("Unexpected compiler-sfc entry: " + entry);
        }

        System.out.println("QinVueCompilerSfcPackageLocatorTestMain passed.");
        System.out.println("entry: " + entry);
    }
}
