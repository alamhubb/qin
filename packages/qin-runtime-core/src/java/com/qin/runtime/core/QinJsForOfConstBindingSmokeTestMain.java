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
                  count(items) {
                    let total = 0;
                    for (const item of items) {
                      total += item;
                    }
                    return total;
                  }
                }
                "sum=" + sum + ";method=" + new Counter().count(values);
                """,
                "js_for_of_const_binding");
        if (!"sum=6;method=6".equals(result)) {
            throw new IllegalStateException("Expected for-of const binding sum=6;method=6, got: " + result);
        }
        System.out.println("QinJsForOfConstBindingSmokeTestMain OK");
    }
}
