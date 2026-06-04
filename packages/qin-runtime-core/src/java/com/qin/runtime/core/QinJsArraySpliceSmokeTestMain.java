package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArraySpliceSmokeTestMain {
    private QinJsArraySpliceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const items = ["a", "b", "c", "d"];
                const removed = items.splice(1, 2, "x", "y");
                const tail = items.splice(-1, 1);
                removed.join(",") + "|" + items.join(",") + "|" + tail.join(",");
                """;
        Path root = Files.createTempDirectory("qin-js-array-splice-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-array-splice\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "array_splice");
        if (!"b,c|a,x,y|d".equals(result)) {
            throw new IllegalStateException("Expected splice result b,c|a,x,y|d, got: " + result);
        }
        System.out.println("QinJsArraySpliceSmokeTestMain OK");
    }
}
