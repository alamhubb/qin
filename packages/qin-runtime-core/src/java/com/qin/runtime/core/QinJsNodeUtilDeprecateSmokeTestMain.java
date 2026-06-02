package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsNodeUtilDeprecateSmokeTestMain {
    private QinJsNodeUtilDeprecateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                import { deprecate } from "node:util";
                const wrapped = deprecate(function(value) { return value + 1; }, "old");
                wrapped(41);
                """;
        Path root = Files.createTempDirectory("qin-js-node-util-deprecate-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-node-util-deprecate\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "node_util_deprecate");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsNodeUtilDeprecateSmokeTestMain OK");
    }
}

