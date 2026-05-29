package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsStringRawTaggedTemplateSmokeTestMain {
    private QinJsStringRawTaggedTemplateSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-string-raw-tagged-template-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-string-raw-tagged-template\" }\n",
                StandardCharsets.UTF_8);

        String wrapper = """
                const raw = String.raw`[\\p{ID_Start}$_]|\\\\u[0-9a-fA-F]{4}`;
                const pattern = new RegExp(String.raw`(?:${raw})(?:${raw})*`, "u");
                ({
                  raw,
                  source: pattern.source,
                  match0: "css".match(pattern) ? "css".match(pattern)[0] : null
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "js_string_raw_tagged_template");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
