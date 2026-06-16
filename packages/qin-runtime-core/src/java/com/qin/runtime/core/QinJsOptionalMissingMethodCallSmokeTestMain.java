package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsOptionalMissingMethodCallSmokeTestMain {
    private QinJsOptionalMissingMethodCallSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-optional-missing-method-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-js-optional-missing-method\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                const target = {
                  present(value) {
                    return value + 1
                  }
                }
                const missing = target.missing?.(41);
                const present = target.present?.(41);
                const fallback = target.missing?.(41) || 7;
                ({
                  missing,
                  present,
                  fallback
                });
                """, "js_optional_missing_method_call");
        if (!(result instanceof Map<?, ?> map)
                || map.get("missing") != null
                || !(map.get("present") instanceof Number present)
                || present.intValue() != 42
                || !(map.get("fallback") instanceof Number fallback)
                || fallback.intValue() != 7) {
            throw new IllegalStateException("Optional missing method call mismatch: " + result);
        }
        System.out.println("QinJsOptionalMissingMethodCallSmokeTestMain OK");
    }
}
