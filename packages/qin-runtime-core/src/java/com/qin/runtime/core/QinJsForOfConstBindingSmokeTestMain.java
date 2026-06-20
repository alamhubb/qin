package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsForOfConstBindingSmokeTestMain {
    private QinJsForOfConstBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-for-of-const-binding-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-js-for-of-const-binding\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                """
                const values = [1, 2, 3];
                let sum = 0;
                for (const item of values) {
                  sum += item;
                }
                class Counter {
                  constructor(items) {
                    this.items = [];
                    for (const item of items) {
                      this.items.push(item);
                    }
                  }
                  count(items) {
                    let total = 0;
                    for (const item of items) {
                      total += item;
                    }
                    return total;
                  }
                }
                const fns = [];
                for (const item of values) {
                  fns.push(() => item);
                }
                const counter = new Counter(values);
                "sum=" + sum + ";method=" + counter.count(values)
                  + ";ctor=" + counter.items.join(",")
                  + ";closures=" + fns.map(fn => fn()).join(",");
                """,
                "js_for_of_const_binding");
        if (!"sum=6;method=6;ctor=1.0,2.0,3.0;closures=1.0,2.0,3.0".equals(result)) {
            throw new IllegalStateException("Expected for-of const binding sum=6;method=6, got: " + result);
        }
        System.out.println("QinJsForOfConstBindingSmokeTestMain OK");
    }
}
