package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsMapObjectIdentitySmokeTestMain {
    private QinJsMapObjectIdentitySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const a = {};
                a.self = a;
                const b = {};
                b.self = b;
                const map = new Map();
                map.set(a, 1);
                map.set(b, 2);
                map.get(a) === 1 && map.get(b) === 2 && map.size === 2 ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-map-object-identity-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-map-object-identity\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "map_object_identity");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsMapObjectIdentitySmokeTestMain OK");
    }
}
