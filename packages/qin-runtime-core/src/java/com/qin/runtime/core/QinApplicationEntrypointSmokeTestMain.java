package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.tools.ToolProvider;

public final class QinApplicationEntrypointSmokeTestMain {
    private QinApplicationEntrypointSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-application-entrypoint-");
        writeProject(root);
        verifyResolvedConfigArgs(root);
        compileApplication(root);
        verifyApplicationLaunch(root);
        System.out.println("QinApplicationEntrypointSmokeTestMain passed.");
    }

    private static void writeProject(Path root) throws Exception {
        Files.createDirectories(root.resolve("main"));
        Files.createDirectories(root.resolve("app"));
        Files.createDirectories(root.resolve("public"));
        Files.writeString(root.resolve("qin.config.js"), """
                import vue from "@vitejs/plugin-vue"

                export default {
                  name: "qin-application-entrypoint-smoke",
                  port: 19118,
                  backend: {
                    entry: "main/Main.java"
                  },
                  frontend: {
                    srcDir: "app",
                    entry: "app/main.js",
                    staticDir: "public"
                  },
                  plugins: [vue()],
                  dependencies: { "com.qin:qin-runtime-core": "0.1.0" }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("public/index.html"), """
                <!doctype html>
                <html><body><script type="module" src="/app.js"></script></body></html>
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("app/main.js"), """
                export const marker = "qin-application-entrypoint-smoke"
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/Main.java"), """
                public final class Main {
                    public static Object run() {
                        return "entrypoint-ok";
                    }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/Application.java"), """
                import com.qin.runtime.core.QinApplication;

                public final class Application {
                    public static void main(String[] args) throws Exception {
                        QinApplication.run(Application.class, args);
                    }
                }
                """, StandardCharsets.UTF_8);
    }

    private static void verifyResolvedConfigArgs(Path root) throws Exception {
        List<String> args = QinApplication.resolveFullstackArgs(
                QinApplicationEntrypointSmokeTestMain.class,
                new String[]{"--build-only"},
                root);
        requireContainsPair(args, "--root", root.toAbsolutePath().normalize().toString());
        requireContainsPair(args, "--port", "19118");
        requireContainsPair(args, "--backend-file", root.resolve("main/Main.java").toAbsolutePath().normalize().toString());
        requireContainsPair(args, "--frontend-file", root.resolve("app/main.js").toAbsolutePath().normalize().toString());
        requireContainsPair(args, "--static-dir", root.resolve("public").toAbsolutePath().normalize().toString());
    }

    private static void compileApplication(Path root) throws Exception {
        Path output = root.resolve("build/entry-classes").normalize();
        Files.createDirectories(output);
        List<String> args = List.of(
                "-encoding", "UTF-8",
                "-cp", System.getProperty("java.class.path"),
                "-d", output.toString(),
                root.resolve("main/Application.java").toString());
        int exit = ToolProvider.getSystemJavaCompiler().run(null, null, null, args.toArray(String[]::new));
        if (exit != 0) {
            throw new IllegalStateException("Application javac failed with exit code " + exit);
        }
    }

    private static void verifyApplicationLaunch(Path root) throws Exception {
        String javaBin = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String separator = System.getProperty("path.separator");
        String classpath = System.getProperty("java.class.path")
                + separator
                + root.resolve("build/entry-classes").toAbsolutePath().normalize();
        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add("Application");
        command.add("--build-only");

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(root.toFile());
        pb.redirectErrorStream(true);
        Process process = pb.start();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        boolean finished;
        try {
            finished = process.waitFor(Duration.ofSeconds(30).toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
            process.getInputStream().transferTo(output);
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
        String text = output.toString(StandardCharsets.UTF_8);
        if (!finished) {
            throw new IllegalStateException("Application launch timed out:\n" + text);
        }
        if (process.exitValue() != 0) {
            throw new IllegalStateException("Application launch failed with exit code "
                    + process.exitValue() + ":\n" + text);
        }
        if (!text.contains("Backend source: " + root.resolve("main/Main.java").toAbsolutePath().normalize())
                || !text.contains("Frontend source: " + root.resolve("app/main.js").toAbsolutePath().normalize())
                || !text.contains("Static root: " + root.resolve("public").toAbsolutePath().normalize())) {
            throw new IllegalStateException("Application launch did not use qin.config.js paths:\n" + text);
        }
    }

    private static void requireContainsPair(List<String> args, String option, String value) {
        for (int i = 0; i + 1 < args.size(); i++) {
            if (option.equals(args.get(i)) && value.equals(args.get(i + 1))) {
                return;
            }
        }
        throw new IllegalStateException("Missing option pair " + option + " " + value + " in " + args);
    }
}
