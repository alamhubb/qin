package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSubhutiParseFailAliasSmokeTestMain {
    private QinJsSubhutiParseFailAliasSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-subhuti-parse-fail-alias-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-subhuti-parse-fail-alias\" }\n",
                StandardCharsets.UTF_8);

        String source = """
                class ParserLike {
                  constructor() {
                    this.parserSuccess = true;
                  }
                  setParseFail() {
                    this.parserSuccess = false;
                  }
                  run() {
                    this._markParseFail();
                    return this.parserSuccess;
                  }
                }
                new ParserLike().run();
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_subhuti_parse_fail_alias");
        if (!Boolean.FALSE.equals(result)) {
            throw new IllegalStateException("Expected _markParseFail alias to mark parser failure, got: " + result);
        }
        System.out.println("QinJsSubhutiParseFailAliasSmokeTestMain OK");
    }
}
