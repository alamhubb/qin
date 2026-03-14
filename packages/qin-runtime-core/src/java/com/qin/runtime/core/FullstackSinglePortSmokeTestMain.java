package com.qin.runtime.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Single-port fullstack smoke test.
 */
public final class FullstackSinglePortSmokeTestMain {
    private static final int PORT = 18081;

    private FullstackSinglePortSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = resolveFullstackMvpRoot();
        Process process = startServer(root, PORT);
        try {
            waitForServer(PORT, Duration.ofSeconds(25));
            verifyEndpoints(PORT);
            System.out.println("FullstackSinglePortSmokeTestMain passed.");
            System.out.println("url: http://localhost:" + PORT);
        } finally {
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private static Process startServer(Path root, int port) throws Exception {
        String javaBin = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String classpath = System.getProperty("java.class.path");
        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add("com.qin.runtime.core.QinFullstackMain");
        command.add("--root");
        command.add(root.toAbsolutePath().normalize().toString());
        command.add("--port");
        command.add(String.valueOf(port));
        command.add("--dev");

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(root.toFile());
        pb.inheritIO();
        return pb.start();
    }

    private static void waitForServer(int port, Duration timeout) throws Exception {
        Instant deadline = Instant.now().plus(timeout);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + port + "/api/health");
        while (Instant.now().isBefore(deadline)) {
            try {
                HttpRequest request = HttpRequest.newBuilder(uri).GET().timeout(Duration.ofSeconds(2)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return;
                }
            } catch (Exception ignored) {
                Thread.sleep(300);
            }
            Thread.sleep(200);
        }
        throw new IllegalStateException("Server did not become healthy in time: " + uri);
    }

    private static void verifyEndpoints(int port) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> health = client.send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/health"))
                        .GET()
                        .timeout(Duration.ofSeconds(3))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (health.statusCode() != 200 || !health.body().contains("\"ok\":true")) {
            throw new IllegalStateException("Unexpected /api/health response: " + health.statusCode() + " " + health.body());
        }

        HttpResponse<String> result = client.send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/result"))
                        .GET()
                        .timeout(Duration.ofSeconds(3))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (result.statusCode() != 200) {
            throw new IllegalStateException("Unexpected /api/result status: " + result.statusCode());
        }

        HttpResponse<String> index = client.send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/"))
                        .GET()
                        .timeout(Duration.ofSeconds(3))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (index.statusCode() != 200) {
            throw new IllegalStateException("Unexpected / status: " + index.statusCode());
        }

        HttpResponse<String> appJs = client.send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/app.js"))
                        .GET()
                        .timeout(Duration.ofSeconds(3))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (appJs.statusCode() != 200 || !appJs.body().contains("/@qin-mod/")) {
            throw new IllegalStateException("Unexpected /app.js response");
        }

        HttpResponse<String> frontModule = client.send(
                HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/@qin-mod/app/main.js"))
                        .GET()
                        .timeout(Duration.ofSeconds(3))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        if (frontModule.statusCode() != 200 || !frontModule.body().contains("console.log")) {
            throw new IllegalStateException("Unexpected frontend module response");
        }
    }

    private static Path resolveFullstackMvpRoot() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve("packages/qin-runtime-core/examples/fullstack-mvp"),
                cwd.resolve("qin/packages/qin-runtime-core/examples/fullstack-mvp"),
                cwd.resolve("examples/fullstack-mvp")
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate examples/fullstack-mvp directory.");
    }
}
