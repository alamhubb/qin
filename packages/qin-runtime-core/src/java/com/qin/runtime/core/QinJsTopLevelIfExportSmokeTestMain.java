package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsTopLevelIfExportSmokeTestMain {
    private QinJsTopLevelIfExportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-top-level-if-export-");
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                class ExportedValue {
                  value() {
                    return "ok";
                  }
                }
                if (typeof globalThis !== "undefined") {
                  globalThis.__qinSmokeExports = globalThis.__qinSmokeExports || {};
                  globalThis.__qinSmokeExports["demo.ExportedValue"] = ExportedValue;
                }
                const Exported = globalThis.__qinSmokeExports["demo.ExportedValue"];
                new Exported().value();
                """, "top_level_if_export");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected top-level if export result, got: " + result);
        }
        System.out.println("QinJsTopLevelIfExportSmokeTestMain OK");
    }
}
