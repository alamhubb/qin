package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsDateIsoLocalSmokeTestMain {
    private QinJsDateIsoLocalSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-date-iso-local-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-date-iso-local\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                """
                const date = new Date("2026-06-04T03:26:00");
                [date.getFullYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes()].join(":");
                """,
                "js_date_iso_local");
        if (!"2026.0:5.0:4.0:3.0:26.0".equals(result)) {
            throw new IllegalStateException("Expected ISO local Date result, got: " + result);
        }
        System.out.println("QinJsDateIsoLocalSmokeTestMain OK");
    }
}
