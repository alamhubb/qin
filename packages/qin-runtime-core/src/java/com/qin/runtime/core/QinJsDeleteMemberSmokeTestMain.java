package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsDeleteMemberSmokeTestMain {
    private QinJsDeleteMemberSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const obj = { a: 1, b: 2 };
                const key = "b";
                const first = delete obj.a;
                const second = delete obj[key];
                first && second && obj.a === undefined && obj.b === undefined ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-delete-member-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-delete-member\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "delete_member");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsDeleteMemberSmokeTestMain OK");
    }
}
