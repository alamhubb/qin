package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsObjectValuesSpreadSmokeTestMain {
    private QinJsObjectValuesSpreadSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-object-values-spread-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-object-values-spread\" }\n",
                StandardCharsets.UTF_8);

        String wrapper = """
                const source = { a: 1, b: 2 };
                const values = Object.values(source);
                const spread = [...values];
                ({
                  valuesLength: values.length,
                  values0: values[0],
                  values1: values[1],
                  spreadLength: spread.length,
                  spread0: spread[0],
                  spread1: spread[1]
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "js_object_values_spread");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
