package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFullstackTsBackendSmokeTestMain {
    private QinFullstackTsBackendSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-fullstack-ts-backend-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-fullstack-ts-backend-smoke",
                  backend: { entry: "main/main.ts" },
                  frontend: { entry: "app/main.js", staticDir: "app" }
                }
                """, StandardCharsets.UTF_8);
        Files.createDirectories(root.resolve("main"));
        Files.writeString(root.resolve("main/main.ts"), """
                import { QinHttpApp, QinHttpResponse } from "java:com.qin.runtime.core"

                export const app = QinHttpApp.create()
                  .get("/api/hello", request => QinHttpResponse.json("{\\"message\\":\\"hello from Qin TS backend\\"}"))

                "qin-ts-backend-smoke"
                """, StandardCharsets.UTF_8);
        Files.createDirectories(root.resolve("app"));
        Files.writeString(root.resolve("app/main.js"), "console.log('frontend smoke')\n", StandardCharsets.UTF_8);

        Path classOutput = root.resolve("build/fullstack/server-classes");
        QinFullstackMain.main(new String[] {
                "--root", root.toString(),
                "--backend-file", root.resolve("main/main.ts").toString(),
                "--frontend-file", root.resolve("app/main.js").toString(),
                "--static-dir", root.resolve("app").toString(),
                "--class-out", classOutput.toString(),
                "--build-only"
        });

        Path adapterClass = classOutput.resolve("com/qin/runtime/generated/ServerAppFullstackAdapter.class");
        if (!Files.isRegularFile(adapterClass)) {
            throw new IllegalStateException("Expected generated fullstack adapter class: " + adapterClass);
        }
        System.out.println("QinFullstackTsBackendSmokeTestMain OK");
    }
}
