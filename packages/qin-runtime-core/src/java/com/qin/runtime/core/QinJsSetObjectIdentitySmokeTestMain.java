package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSetObjectIdentitySmokeTestMain {
    private QinJsSetObjectIdentitySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const a = {};
                a.self = a;
                const b = {};
                b.self = b;
                const set = new Set();
                set.add(a);
                set.add(b);
                set.has(a) && set.has(b) && set.size === 2 ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-set-object-identity-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-set-object-identity\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "set_object_identity");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsSetObjectIdentitySmokeTestMain OK");
    }
}

