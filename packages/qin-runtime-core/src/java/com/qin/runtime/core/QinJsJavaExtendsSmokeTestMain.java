package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsJavaExtendsSmokeTestMain {
    private QinJsJavaExtendsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-java-extends-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-java-extends\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { ArrayList } from "java:java.util";

                class JsList extends ArrayList {
                  constructor() {
                    super();
                  }
                }

                const list = new JsList();
                list.add("ok");
                list.size();
                """, "js_java_extends");
        if (!Integer.valueOf(1).equals(result)) {
            throw new IllegalStateException("Expected inherited java list size 1, got: " + result);
        }
        System.out.println("QinJsJavaExtendsSmokeTestMain passed.");
    }
}

