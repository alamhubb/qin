package com.qin.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class QinCliFullstackInProcessProfileSmokeTestMain {
    private QinCliFullstackInProcessProfileSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path repoRoot = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
        Path smokeRoot = repoRoot.resolve("smoke-workspaces");
        Files.createDirectories(smokeRoot);
        Path root = Files.createTempDirectory(smokeRoot, "qin-cli-fullstack-inprocess-");
        Path app = root.resolve("app");
        Path lib = root.resolve("lib");
        Files.createDirectories(app.resolve("main"));
        Files.createDirectories(lib.resolve("build").resolve("classes").resolve("example"));
        Files.write(lib.resolve("build").resolve("classes").resolve("example").resolve("Lib.class"), new byte[] {0});
        Files.writeString(lib.resolve("qin.config.js"), """
                export default {
                  name: "lib",
                  version: "1.0.0"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(app.resolve("qin.config.js"), """
                export default {
                  name: "app",
                  version: "1.0.0",
                  backend: {
                    entry: "main/Main.java"
                  },
                  dependencies: {
                    lib: "file:../lib"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(app.resolve("main").resolve("Main.java"), """
                public class Main {
                    public static Object run() {
                        return "ok";
                    }
                }
                """, StandardCharsets.UTF_8);

        String previousUserDir = System.getProperty("user.dir");
        String previousClasspath = System.getProperty("java.class.path");
        try {
            System.setProperty("user.dir", app.toString());
            String output = captureCliDevBuildOnlyProfile();
            require(output.contains("[QinProfile] fullstack-build start"), "CLI forwards --profile to fullstack runtime");
            require(output.contains("Build only mode finished."), "CLI fullstack dev build-only completes");
            require(output.contains("[OK] Qin dev runtime stopped"), "CLI returns from in-process dev runtime");
            require(previousClasspath.equals(System.getProperty("java.class.path")),
                    "in-process runtime restores java.class.path");
        } finally {
            System.setProperty("user.dir", previousUserDir);
            System.setProperty("java.class.path", previousClasspath);
            deleteTree(root);
        }

        System.out.println("QinCliFullstackInProcessProfileSmokeTestMain OK");
    }

    private static String captureCliDevBuildOnlyProfile() throws Exception {
        PrintStream previousOut = System.out;
        PrintStream previousErr = System.err;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            System.setErr(capture);
            QinCli.main(new String[] {"dev", "--build-only", "--profile", "--port", "19129"});
        } finally {
            System.setOut(previousOut);
            System.setErr(previousErr);
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
