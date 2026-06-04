package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsRuntimeIifeBlockSmokeTestMain {
    private QinJsRuntimeIifeBlockSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-runtime-iife-block-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-runtime-iife-block\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                let value = 0;
                class TokenBuilder {
                  static builder() {
                    return {
                      value(text) {
                        return "token:" + text;
                      }
                    };
                  }
                }
                (() => {
                  value = 7;
                })();
                (() => {
                  const next = value + 5;
                  value = next;
                  return null;
                })();
                (() => {
                  value = value + TokenBuilder.builder().value("ok").length;
                })();
                value;
                """, "js_runtime_iife_block");
        if (!(result instanceof Number number) || number.intValue() != 20) {
            throw new IllegalStateException("Expected IIFE block side effects to produce 20, got: " + result);
        }
        System.out.println("QinJsRuntimeIifeBlockSmokeTestMain OK");
    }
}
