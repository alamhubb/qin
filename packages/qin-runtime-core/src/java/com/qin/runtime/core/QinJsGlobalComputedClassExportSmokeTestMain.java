package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsGlobalComputedClassExportSmokeTestMain {
    private QinJsGlobalComputedClassExportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-global-computed-class-export-");
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                class ExportedValue {
                  value() {
                    return "ok";
                  }
                }
                globalThis.__qinSmokeExports = globalThis.__qinSmokeExports || {};
                globalThis.__qinSmokeExports["demo.ExportedValue"] = ExportedValue;
                const Exported = globalThis.__qinSmokeExports["demo.ExportedValue"];
                new Exported().value();
                """, "global_computed_class_export");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected computed global class export result, got: " + result);
        }
        System.out.println("QinJsGlobalComputedClassExportSmokeTestMain OK");
    }
}
