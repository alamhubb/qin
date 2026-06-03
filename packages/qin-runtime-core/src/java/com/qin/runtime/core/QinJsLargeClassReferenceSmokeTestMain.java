package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsLargeClassReferenceSmokeTestMain {
    private QinJsLargeClassReferenceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        StringBuilder source = new StringBuilder();
        source.append("""
                class LargeExportedValue {
                  value() {
                    return "ok";
                  }
                }
                """);
        source.append("const filler = \"");
        while (source.length() <= 210_000) {
            source.append("x");
        }
        source.append("\";\n");
        source.append("""
                globalThis.__qinLargeExports = globalThis.__qinLargeExports || {};
                globalThis.__qinLargeExports["demo.LargeExportedValue"] = LargeExportedValue;
                const Exported = globalThis.__qinLargeExports["demo.LargeExportedValue"];
                new Exported().value();
                """);

        Path root = Files.createTempDirectory("qin-js-large-class-reference-");
        Object result = new QinJsPackageRunner().runModuleSource(root, source.toString(), "large_class_reference");
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected large class reference result, got: " + result);
        }
        System.out.println("QinJsLargeClassReferenceSmokeTestMain OK");
    }
}
