package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsOptionalMemberNullishSmokeTestMain {
    private QinJsOptionalMemberNullishSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-optional-member-nullish-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-optional-member-nullish\" }\n", StandardCharsets.UTF_8);
        String source = """
                const options = { tokenConsumer: "consumer", tokenDefinitions: ["A", "B"] };
                const fallback = ["X"];
                ({
                  consumer: options?.tokenConsumer ?? "fallbackConsumer",
                  definitions: options?.tokenDefinitions ?? fallback,
                  missing: options?.missing ?? fallback
                });
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_optional_member_nullish");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        Object consumer = map.get("consumer");
        Object definitions = map.get("definitions");
        Object missing = map.get("missing");
        if (!"consumer".equals(consumer)
                || !(definitions instanceof java.util.List<?> definitionsList)
                || definitionsList.size() != 2
                || !(missing instanceof java.util.List<?> missingList)
                || missingList.size() != 1) {
            throw new IllegalStateException("Optional member/nullish result mismatch: " + result);
        }
        System.out.println("QinJsOptionalMemberNullishSmokeTestMain OK");
    }
}

