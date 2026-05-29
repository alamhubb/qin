package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSuperOptionsPropertySmokeTestMain {
    private QinJsSuperOptionsPropertySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-super-options-property-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-super-options-property\" }\n", StandardCharsets.UTF_8);
        String source = """
                class Lexer {
                  constructor(tokens) {
                    this.first = tokens.map(token => token.name).join(",");
                  }
                }
                class Base {
                  constructor(source, options) {
                    this.lexer = new Lexer(options.tokenDefinitions);
                  }
                }
                class Child extends Base {
                  constructor(source = "", options) {
                    super(source, options ?? {
                      tokenDefinitions: [{ name: "A" }, { name: "B" }]
                    });
                  }
                }
                const child = new Child("");
                child.lexer.first;
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_super_options_property");
        if (!"A,B".equals(result)) {
            throw new IllegalStateException("Expected super options property array to map as A,B, got: " + result);
        }
        System.out.println("QinJsSuperOptionsPropertySmokeTestMain OK");
    }
}
