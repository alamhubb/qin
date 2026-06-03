package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsSlashStringLiteralSmokeTestMain {
    private QinJsSlashStringLiteralSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-slash-string-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-slash-string\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                """
                const path = "/workspace/app";
                const regex = /workspace\\/app/;
                [path, typeof path, regex.test(path)].join(":");
                """,
                "js_slash_string");
        if (!"/workspace/app:string:true".equals(result)) {
            throw new IllegalStateException("Expected slash string literal to stay a string, got: " + result);
        }
        System.out.println("QinJsSlashStringLiteralSmokeTestMain OK");
    }
}
