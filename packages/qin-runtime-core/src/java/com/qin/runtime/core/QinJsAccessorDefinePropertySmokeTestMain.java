package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsAccessorDefinePropertySmokeTestMain {
    private QinJsAccessorDefinePropertySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const n = {
                  default: {},
                  deprecate: function(fn, msg) { return fn; }
                };
                const a = {};
                Object.defineProperty(a, "deprecate", {
                  enumerable: true,
                  get: function() {
                    return n.deprecate;
                  }
                });
                const wrapped = a.deprecate(function(value) { return value + 1; }, "old");
                class Box {
                  constructor(value) {
                    this.raw = value;
                  }
                  getRaw() {
                    return this.raw;
                  }
                }
                const box = new Box(41);
                Object.defineProperties(box, {
                  computed: {
                    enumerable: true,
                    configurable: true,
                    get: function() {
                      return this.getRaw() + 1;
                    }
                  }
                });
                wrapped(41) + box.computed;
                """;
        Path root = Files.createTempDirectory("qin-js-accessor-define-property-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-accessor-define-property\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "accessor_define_property");
        if (!Double.valueOf(84.0d).equals(result)) {
            throw new IllegalStateException("Expected 84, got: " + result);
        }
        System.out.println("QinJsAccessorDefinePropertySmokeTestMain OK");
    }
}

