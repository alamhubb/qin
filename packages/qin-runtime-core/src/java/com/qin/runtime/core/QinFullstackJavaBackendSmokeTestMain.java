package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFullstackJavaBackendSmokeTestMain {
    private QinFullstackJavaBackendSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-fullstack-java-backend-");
        Files.createDirectories(root.resolve("main"));
        Files.createDirectories(root.resolve("app"));
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: 'java-backend-smoke',
                  backend: { entry: 'main/Main.java' },
                  frontend: { entry: 'app/main.js', staticDir: 'app' }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/Main.java"), """
                package demo;

                import java.util.LinkedHashMap;
                import java.util.Map;

                public final class Main {
                    private Main() {
                    }

                    public static Object run() {
                        Map<String, Object> result = new LinkedHashMap<>();
                        result.put("message", "hello from Java backend");
                        result.put("language", "java8");
                        return result;
                    }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("app/main.js"), """
                document.body.dataset.qinJavaBackendSmoke = 'ok';
                """, StandardCharsets.UTF_8);

        QinFullstackMain.main(new String[] {
                "--root", root.toString(),
                "--backend-file", root.resolve("main/Main.java").toString(),
                "--frontend-file", root.resolve("app/main.js").toString(),
                "--static-dir", root.resolve("app").toString(),
                "--class-out", root.resolve("build/server-classes").toString(),
                "--build-only"
        });

        Path classFile = root.resolve("build/server-classes/demo/Main.class");
        if (!Files.isRegularFile(classFile)) {
            throw new IllegalStateException("Expected Java backend class file: " + classFile);
        }

        System.out.println("QinFullstackJavaBackendSmokeTestMain OK");
    }
}
