package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsStaticPrivateFieldSmokeTestMain {
    private QinJsStaticPrivateFieldSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-static-private-field-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-static-private-field\" }\n", StandardCharsets.UTF_8);
        String source = """
                class Stack {
                  static #constructing = false;
                  static create() {
                    Stack.#constructing = true;
                    return Stack.#constructing;
                  }
                }
                const Named = class u {
                  static #constructing = false;
                  static create() {
                    u.#constructing = true;
                    return new u();
                  }
                  constructor() {
                    if (!u.#constructing) {
                      throw new Error("bad construct flag");
                    }
                    this.ok = true;
                  }
                };
                Stack.create() && Named.create().ok;
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_static_private_field");
        if (!Boolean.TRUE.equals(result)) {
            throw new IllegalStateException("Expected static private field update to return true, got: " + result);
        }
        System.out.println("QinJsStaticPrivateFieldSmokeTestMain OK");
    }
}

