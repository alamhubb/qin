package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsMultiLevelSuperOptionsSmokeTestMain {
    private QinJsMultiLevelSuperOptionsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-multi-super-options-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-multi-super-options\" }\n", StandardCharsets.UTF_8);
        String source = """
                class Lexer {
                  constructor(tokens) {
                    this.first = tokens.map(token => token.name).join(",");
                  }
                }
                class Base {
                  constructor(source = "", options) {
                    this.lexer = new Lexer(options.tokenDefinitions);
                  }
                }
                class JsParser extends Base {
                  constructor(source = "", options) {
                    const defaultTokenConsumer = "js";
                    super(source, {
                      tokenConsumer: options?.tokenConsumer ?? defaultTokenConsumer,
                      tokenDefinitions: options?.tokenDefinitions ?? [{ name: "JS" }]
                    });
                  }
                }
                class SlimeParser extends JsParser {
                  constructor(source = "", options) {
                    const defaultTokenConsumer = "slime";
                    super(source, {
                      tokenConsumer: options?.tokenConsumer ?? defaultTokenConsumer,
                      tokenDefinitions: options?.tokenDefinitions ?? [{ name: "SLIME" }]
                    });
                  }
                }
                class CssParser extends SlimeParser {
                  constructor(source = "", options) {
                    super(source, options ?? {
                      tokenConsumer: "css",
                      tokenDefinitions: [{ name: "CSS_A" }, { name: "CSS_B" }]
                    });
                  }
                }
                const parser = new CssParser("abc");
                parser.lexer.first;
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_multi_level_super_options");
        if (!"CSS_A,CSS_B".equals(result)) {
            throw new IllegalStateException("Expected CSS_A,CSS_B, got: " + result);
        }
        System.out.println("QinJsMultiLevelSuperOptionsSmokeTestMain OK");
    }
}

