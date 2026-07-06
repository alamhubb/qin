package com.qin.cli;

import com.qin.constants.QinConstants;
import com.qin.core.ConfigLoader;
import com.qin.types.QinConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;

public final class QinCliLocalDependencyCacheSmokeTestMain {
    private QinCliLocalDependencyCacheSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path smokeRoot = Path.of(System.getProperty("user.dir"), "smoke-workspaces");
        Files.createDirectories(smokeRoot);
        Path root = Files.createTempDirectory(smokeRoot, "qin-cli-local-cache-");
        Path app = root.resolve("app");
        Path lib = root.resolve("lib");
        Files.createDirectories(app);
        Files.createDirectories(lib.resolve("build").resolve("classes").resolve("example"));
        Files.write(lib.resolve("build").resolve("classes").resolve("example").resolve("Lib.class"), new byte[] {0});
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "workspace-root",
                  version: "1.0.0",
                  workspaces: ["app", "lib"]
                }
                """, StandardCharsets.UTF_8);
        writeLibConfig(lib, "1.0.0");
        Files.writeString(app.resolve("qin.config.js"), """
                export default {
                  name: "app",
                  version: "1.0.0",
                  dependencies: {
                    lib: "file:../lib"
                  }
                }
                """, StandardCharsets.UTF_8);

        String previousUserDir = System.getProperty("user.dir");
        try {
            System.setProperty("user.dir", app.toString());
            QinConfig config = new ConfigLoader(app.toString()).load();
            String first = captureEnsureDependencies(config);
            Path cacheFile = QinConstants.getProjectClasspathCache(app.toString());
            String cacheJson = Files.readString(cacheFile);
            require(first.contains("[local] lib -> lib"), "first run resolves local dependency");
            require(cacheJson.contains("\"localProjects\""), "classpath cache records local projects");
            require(cacheJson.contains("\"fullName\": \"lib\""), "classpath cache records lib identity");

            String second = captureEnsureDependencies(config);
            require(!second.contains("[local]"), "unchanged cache hit avoids workspace local scan");
            require(second.contains("Using cached dependencies"), "unchanged cache uses cached classpath");

            Thread.sleep(1100);
            writeLibConfig(lib, "1.0.1");
            Files.setLastModifiedTime(lib.resolve("qin.config.js"),
                    FileTime.fromMillis(System.currentTimeMillis() + 2000));
            String third = captureEnsureDependencies(config);
            require(third.contains("[local] lib -> lib"), "local config change invalidates cached project index");
            require(Files.readString(cacheFile).contains("\"fullName\": \"lib\""), "refreshed cache keeps local project metadata");
        } finally {
            System.setProperty("user.dir", previousUserDir);
            deleteTree(root);
        }

        System.out.println("QinCliLocalDependencyCacheSmokeTestMain OK");
    }

    private static void writeLibConfig(Path lib, String version) throws Exception {
        Files.writeString(lib.resolve("qin.config.js"), """
                export default {
                  name: "lib",
                  version: "%s"
                }
                """.formatted(version), StandardCharsets.UTF_8);
    }

    private static String captureEnsureDependencies(QinConfig config) throws Exception {
        Method method = QinCli.class.getDeclaredMethod("ensureDependenciesSynced", QinConfig.class);
        method.setAccessible(true);
        PrintStream previousOut = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            method.invoke(null, config);
        } finally {
            System.setOut(previousOut);
        }
        return bytes.toString(StandardCharsets.UTF_8);
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
