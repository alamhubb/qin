package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsBitwiseAssignmentSmokeTestMain {
    private QinJsBitwiseAssignmentSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                let value = -1;
                value >>>= 1;
                let mask = 1;
                mask <<= 3;
                value === 2147483647 && mask === 8 ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-bitwise-assignment-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-bitwise-assignment\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "bitwise_assignment");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsBitwiseAssignmentSmokeTestMain OK");
    }
}
