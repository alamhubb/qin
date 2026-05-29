package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsCjsRequireUtilDeprecateSmokeTestMain {
    private QinJsCjsRequireUtilDeprecateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-cjs-require-util-deprecate-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-cjs-require-util-deprecate\" }\n", StandardCharsets.UTF_8);
        String wrapper = """
                var util = require("util");
                var wrapped = util.deprecate(function(value) { return value + 1; }, "old");
                wrapped(41);
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "cjs_require_util_deprecate");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsCjsRequireUtilDeprecateSmokeTestMain OK");
    }
}
