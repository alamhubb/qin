package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsConstructorObjectAssignmentSmokeTestMain {
    private QinJsConstructorObjectAssignmentSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-constructor-object-assignment-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-constructor-object-assignment\" }\n",
                StandardCharsets.UTF_8);

        String wrapper = """
                class Token {
                  constructor(options) {
                    this.name = options.name;
                    this.type = options.type || options.name;
                    this.value = options.value;
                  }
                }
                const token = new Token({ name: "IdentifierName", value: "abc" });
                ({
                  name: token.name,
                  type: token.type,
                  value: token.value,
                  keys: Object.keys(token),
                  values: Object.values(token)
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "js_constructor_object_assignment");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
