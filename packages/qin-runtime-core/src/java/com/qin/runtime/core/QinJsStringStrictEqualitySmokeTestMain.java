package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsStringStrictEqualitySmokeTestMain {
    private QinJsStringStrictEqualitySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-string-strict-equality-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-string-strict-equality\" }\n",
                StandardCharsets.UTF_8);

        String wrapper = """
                class Parser {
                  StatementListItem() {}
                }
                const cst = { name: "StatementListItem" };
                const expected = Parser.prototype.StatementListItem.name;
                ({
                  cstName: cst.name,
                  expected,
                  strictEqual: cst.name === expected,
                  looseEqual: cst.name == expected,
                  stringStrictEqual: String(cst.name) === String(expected),
                  cstType: typeof cst.name,
                  expectedType: typeof expected
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "js_string_strict_equality");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
