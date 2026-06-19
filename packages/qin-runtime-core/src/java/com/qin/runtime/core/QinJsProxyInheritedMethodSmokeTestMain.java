package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsProxyInheritedMethodSmokeTestMain {
    private QinJsProxyInheritedMethodSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-proxy-inherited-method-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-js-proxy-inherited-method\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                class Base {
                  constructor() {
                    this.usedAtoms = new Set(["btn"]);
                  }
                  clearUsedAtoms() {
                    this.usedAtoms.clear();
                    return this.usedAtoms.size;
                  }
                }
                abstract class Mid extends Base {
                }
                class Child extends Mid {
                }
                let util;
                util = new Child();
                function registerUtil(instance) {
                  util = instance;
                }
                registerUtil(util);
                const proxied = new Proxy({}, {
                  get(_, prop) {
                    const val = (util as any)[prop];
                    return typeof val === "function" ? val.bind(util) : val;
                  }
                });
                proxied.clearUsedAtoms();
                """, "js_proxy_inherited_method");
        if (!(result instanceof Number number) || number.intValue() != 0) {
            throw new IllegalStateException("Expected inherited proxy method to clear set, got: " + result);
        }
        System.out.println("QinJsProxyInheritedMethodSmokeTestMain OK");
    }
}
