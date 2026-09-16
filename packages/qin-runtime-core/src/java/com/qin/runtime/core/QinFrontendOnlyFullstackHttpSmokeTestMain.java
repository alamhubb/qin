package com.qin.runtime.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class QinFrontendOnlyFullstackHttpSmokeTestMain {
    private static final int PORT = 18134;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    private QinFrontendOnlyFullstackHttpSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-only-fullstack-");
        writeProject(root);
        Process process = startServer(root);
        try {
            waitForServer();
            requireResponse("/", 200, "Qin frontend-only smoke");
            requireResponse("/app.js", 200, "/@qin-mod/app/main.js");
            requireResponse("/@qin-mod/app/main.js", 200, "frontend-only");
            requireResponse("/api/health", 200, "{\"ok\":true}");
            requireResponse("/api/result", 200, "hello from Qin generated frontend-only backend");
            System.out.println("QinFrontendOnlyFullstackHttpSmokeTestMain passed.");
        } finally {
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private static void writeProject(Path root) throws Exception {
        Path app = Files.createDirectories(root.resolve("app"));
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                    name: "qin-frontend-only-fullstack-smoke",
                    port: 18134,
                    frontend: { srcDir: "app", entry: "app/main.js", staticDir: "app" },
                    dependencies: { "com.qin:qin-runtime-core": "0.1.0" }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(app.resolve("index.html"), """
                <!doctype html>
                <html>
                  <body>
                    <div id="app">Qin frontend-only smoke</div>
                    <script type="module" src="/app.js"></script>
                  </body>
                </html>
                """, StandardCharsets.UTF_8);
        Files.writeString(app.resolve("main.js"), """
                export const message = "frontend-only";
                """, StandardCharsets.UTF_8);
    }

    private static Process startServer(Path root) throws Exception {
        String javaBin = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(absoluteClasspath());
        command.add("com.qin.runtime.core.QinFullstackMain");
        command.add("--root");
        command.add(root.toString());
        command.add("--port");
        command.add(String.valueOf(PORT));
        command.add("--dev");
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(root.toFile());
        processBuilder.inheritIO();
        return processBuilder.start();
    }

    private static String absoluteClasspath() {
        String separator = System.getProperty("path.separator");
        Path cwd = Path.of("").toAbsolutePath().normalize();
        List<String> entries = new ArrayList<>();
        for (String entry : System.getProperty("java.class.path").split(java.util.regex.Pattern.quote(separator))) {
            if (entry == null || entry.isBlank()) {
                continue;
            }
            Path path = Path.of(entry);
            entries.add((path.isAbsolute() ? path : cwd.resolve(path)).normalize().toString());
        }
        return String.join(separator, entries);
    }

    private static void waitForServer() throws Exception {
        Instant deadline = Instant.now().plus(Duration.ofSeconds(25));
        while (Instant.now().isBefore(deadline)) {
            try {
                HttpResponse<String> response = send("/api/health");
                if (response.statusCode() == 200) {
                    return;
                }
            } catch (Exception ignored) {
                Thread.sleep(250);
            }
            Thread.sleep(250);
        }
        throw new IllegalStateException("Server did not become healthy in time");
    }

    private static void requireResponse(String path, int status, String expectedBodyPart) throws Exception {
        HttpResponse<String> response = send(path);
        if (response.statusCode() != status || !response.body().contains(expectedBodyPart)) {
            throw new IllegalStateException("Unexpected response for " + path + ": "
                    + response.statusCode() + " " + response.body());
        }
    }

    private static HttpResponse<String> send(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + PORT + path))
                .GET()
                .timeout(REQUEST_TIMEOUT)
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
