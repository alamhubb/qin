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
                const astName = "IDENTIFIER".split("_").filter(Boolean).map(part => {
                  const lower = part.toLowerCase();
                  return lower.slice(0, 1).toUpperCase() + lower.slice(1);
                }).join("");
                const fromMapped = Array.from({ length: 3 }, (_, i) => i + 1).join(",");
                filtered.join(",") + "|" + astName + "|" + fromMapped;
                """;
        Path root = Files.createTempDirectory("qin-js-array-filter-includes-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "{ \"name\": \"qin-js-array-filter-includes\" }\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "array_filter_includes");
        if (!"pixel,fontRelative|Identifier|1.0,2.0,3.0".equals(result)) {
            throw new IllegalStateException("Expected pixel,fontRelative|Identifier|1.0,2.0,3.0, got: " + result);
        }
        System.out.println("QinJsArrayFilterIncludesSmokeTestMain OK");
    }
}

