package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsInterpretedJavaFieldAliasSmokeTestMain {
    private QinJsInterpretedJavaFieldAliasSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-interpreted-java-field-alias-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-js-interpreted-java-field-alias\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                class JavaLikeBox {
                  constructor() {
                    this.__qin_field_items = [];
                  }

                  add(value) {
                    this.items.push(value);
                    return this.items.length;
                  }
                }

                const box = new JavaLikeBox();
                box.add("a") + ":" + box.items.length;
                """, "js_interpreted_java_field_alias");
        if (!"1:1".equals(result)) {
            throw new IllegalStateException("Expected interpreted Java field alias result 1:1, got: " + result);
        }
        System.out.println("QinJsInterpretedJavaFieldAliasSmokeTestMain OK");
    }
}
