package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrayShiftUnshiftSmokeTestMain {
    private QinJsArrayShiftUnshiftSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const items = [2, 3];
                const len = items.unshift(0, 1);
                const first = items.shift();
                len === 4 && first === 0 && items.join(",") === "1.0,2.0,3.0" ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-array-shift-unshift-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-array-shift-unshift\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "array_shift_unshift");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsArrayShiftUnshiftSmokeTestMain OK");
    }
}

