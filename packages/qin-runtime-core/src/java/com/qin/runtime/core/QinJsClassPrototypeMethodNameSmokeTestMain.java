package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsClassPrototypeMethodNameSmokeTestMain {
    private QinJsClassPrototypeMethodNameSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-class-prototype-method-name-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-class-prototype-method-name\" }\n",
                StandardCharsets.UTF_8);

        String wrapper = """
                class Parser {
                  StatementListItem() {
                    return 1;
                  }
                }
                ({
                  methodType: typeof Parser.prototype.StatementListItem,
                  methodName: Parser.prototype.StatementListItem.name,
                  instanceResult: new Parser().StatementListItem()
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "js_class_prototype_method_name");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}

