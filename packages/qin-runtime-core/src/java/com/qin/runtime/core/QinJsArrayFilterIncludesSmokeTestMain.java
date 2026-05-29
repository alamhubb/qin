package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArrayFilterIncludesSmokeTestMain {
    private QinJsArrayFilterIncludesSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const categories = ["pixel", "physical", "fontRelative"];
                const excluded = ["physical"];
                const filtered = categories.filter(c => !excluded.includes(c));
                filtered.join(",");
                """;
        Path root = Files.createTempDirectory("qin-js-array-filter-includes-");
        Files.writeString(
                root.resolve("qin.config.json"),
                "{ \"name\": \"qin-js-array-filter-includes\" }\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "array_filter_includes");
        if (!"pixel,fontRelative".equals(result)) {
            throw new IllegalStateException("Expected pixel,fontRelative, got: " + result);
        }
        System.out.println("QinJsArrayFilterIncludesSmokeTestMain OK");
    }
}
