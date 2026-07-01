package com.qin.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;

public final class LocalProjectResolverNonJvmDependencySmokeTestMain {
    private LocalProjectResolverNonJvmDependencySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path smokeRoot = Path.of(System.getProperty("user.dir"), "smoke-workspaces");
        Files.createDirectories(smokeRoot);
        Path root = Files.createTempDirectory(smokeRoot, "qin-local-non-jvm-deps-");
        Path app = root.resolve("app");
        Path token = root.resolve("token");
        Path generated = root.resolve("generated").resolve("qin-parser-ts");
        Files.createDirectories(app);
        Files.createDirectories(token.resolve("src"));
        Files.createDirectories(generated);
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "workspace-root",
                  version: "1.0.0",
                  workspaces: ["app", "token", "generated/qin-parser-ts"]
                }
                """, StandardCharsets.UTF_8);

        Files.writeString(token.resolve("src").resolve("index.ts"), "export const token = 1;\n", StandardCharsets.UTF_8);
        Files.writeString(token.resolve("qin.config.js"), """
                export default {
                  name: "token",
                  version: "1.0.0",
                  type: "library",
                  entry: "src/index.ts",
                  scripts: {
                    build: "tsdown"
                  }
                }
                """, StandardCharsets.UTF_8);

        Files.writeString(generated.resolve("index.ts"), "export default {};\n", StandardCharsets.UTF_8);
        Files.writeString(generated.resolve("qin.config.js"), """
                export default {
                  name: "@qin/generated-qin-parser-ts",
                  version: "1.0.0",
                  type: "library",
                  entry: "./index.ts",
                  generated: {
                    source: "java",
                    entryBinaryName: "com.qin.parser.QinParser"
                  }
                }
                """, StandardCharsets.UTF_8);

        Files.writeString(app.resolve("qin.config.js"), """
                export default {
                  name: "app",
                  version: "1.0.0",
                  dependencies: {
                    token: "file:../token",
                    "@qin/generated-qin-parser-ts": "file:../generated/qin-parser-ts"
                  }
                }
                """, StandardCharsets.UTF_8);

        LocalProjectResolverEnhanced resolver = new LocalProjectResolverEnhanced(app.toString());
        LocalProjectResolverEnhanced.ResolutionResult result = resolver.resolveDependencies(Map.of(
                "token", "file:../token",
                "@qin/generated-qin-parser-ts", "file:../generated/qin-parser-ts"));

        require(result.localCount == 2, "both local non-JVM dependencies are discovered");
        require(result.localClasspath.isBlank(), "non-JVM dependencies stay out of JVM classpath");
        require(result.autoCompiledCount == 0, "non-JVM dependencies are not Java-compiled");
        require(!Files.exists(token.resolve("build").resolve("classes")), "TS library build/classes not created");
        require(!Files.exists(generated.resolve("build").resolve("classes")), "generated TS build/classes not created");
        deleteTree(root);

        System.out.println("LocalProjectResolverNonJvmDependencySmokeTestMain OK");
    }

    private static void deleteTree(Path root) throws Exception {
        if (!Files.exists(root)) {
            return;
        }
        try (var stream = Files.walk(root)) {
            for (Path path : stream.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
