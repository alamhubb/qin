package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinLruCacheNamedExportSmokeTestMain {
    private QinLruCacheNamedExportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-lru-cache-named-export-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-lru-cache-named-export\" }\n", StandardCharsets.UTF_8);
        String wrapper = """
                import { LRUCache } from "lru-cache";
                const cache = new LRUCache({ max: 2 });
                cache.set("a", 1);
                cache.get("a");
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "lru_cache_named_export");
        if (!Double.valueOf(1.0d).equals(result)) {
            throw new IllegalStateException("Expected lru-cache get result 1, got: " + result);
        }
        System.out.println("QinLruCacheNamedExportSmokeTestMain OK");
    }
}
