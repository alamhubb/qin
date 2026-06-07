package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsImportedConstructorAssignedMethodPropertySmokeTestMain {
    private QinJsImportedConstructorAssignedMethodPropertySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-imported-constructor-assigned-method-property-");
        Path packageDir = root.resolve("node_modules").resolve("mini-alt");
        Files.createDirectories(packageDir);
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "mini-alt",
                  "version": "1.0.0",
                  "type": "module",
                  "exports": "./index.js",
                  "main": "./index.js",
                  "module": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageDir.resolve("index.js"), """
                export class Alternative {
                  constructor(alt) {
                    this.alt = alt;
                  }
                  static of(supplier) {
                    return new Alternative(supplier);
                  }
                }
                """, StandardCharsets.UTF_8);
        Path source = root.resolve("main.js");
        Files.writeString(source, """
                import { Alternative } from "mini-alt";
                const alt = Alternative.of(() => 7);
                alt.alt();
                """, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                source,
                root,
                "com.qin.runtime.generated.ImportedConstructorAssignedMethodPropertySmoke");
        if (!(result instanceof Number number) || number.intValue() != 7) {
            throw new IllegalStateException("Expected imported constructor-assigned method property to return 7, got: " + result);
        }
        System.out.println("QinJsImportedConstructorAssignedMethodPropertySmokeTestMain OK");
    }
}
