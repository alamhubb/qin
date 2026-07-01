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

public final class QinFullstackHttpAppSmokeTestMain {
    private static final int PORT = 18116;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    private QinFullstackHttpAppSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-fullstack-http-app-");
        writeProject(root);
        Process process = startServer(root);
        try {
            waitForServer();
            verifyApi();
            System.out.println("QinFullstackHttpAppSmokeTestMain passed.");
        } finally {
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private static void writeProject(Path root) throws Exception {
        Files.createDirectories(root.resolve("main"));
        Files.createDirectories(root.resolve("app"));
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                    name: "qin-fullstack-http-app-smoke",
                    port: 18116,
                    backend: { entry: "main/Main.java" },
                    frontend: { srcDir: "app", staticDir: "app" },
                    dependencies: { "com.qin:qin-runtime-core": "0.1.0" }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("app/index.html"), """
                <!doctype html>
                <html><body><script type="module" src="/app.js"></script></body></html>
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/Main.java"), """
                import com.qin.runtime.core.QinHttpApp;
                import com.qin.runtime.core.QinHttpResponse;

                public final class Main {
                    private static final QinHttpApp APP = QinHttpApp.create()
                            .get("/", request -> QinHttpResponse.text("hello"))
                            .get("/api/users", request -> QinHttpResponse.json("{\\"users\\":[]}"))
                            .post("/api/users", request -> QinHttpResponse.json(201, "{\\"created\\":" + request.bodyText() + "}"))
                            .delete("/api/users/{id}", request -> QinHttpResponse.json("{\\"deleted\\":\\"" + request.param("id") + "\\"}"));

                    public static Object run() {
                        return "ok";
                    }

                    public static QinHttpApp app() {
                        return APP;
                    }
                }
                """, StandardCharsets.UTF_8);
    }

    private static Process startServer(Path root) throws Exception {
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
        command.add(String.valueOf(PORT));
        command.add("--dev");

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(root.toFile());
        pb.inheritIO();
        return pb.start();
    }

    private static void waitForServer() throws Exception {
        Instant deadline = Instant.now().plus(Duration.ofSeconds(25));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/api/health");
        while (Instant.now().isBefore(deadline)) {
            try {
                HttpRequest request = HttpRequest.newBuilder(uri).GET().timeout(Duration.ofSeconds(2)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return;
                }
            } catch (Exception ignored) {
                Thread.sleep(250);
            }
            Thread.sleep(250);
        }
        throw new IllegalStateException("Server did not become healthy in time: " + uri);
    }

    private static void verifyApi() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        requireResponse(client, "GET", "/", null, 200, "hello");
        requireResponse(client, "GET", "/api/users", null, 200, "\"users\"");
        requireResponse(client, "POST", "/api/users", "{\"name\":\"Ada\"}", 201, "\"Ada\"");
        requireResponse(client, "DELETE", "/api/users/7", null, 200, "\"7\"");
    }

    private static void requireResponse(
            HttpClient client,
            String method,
            String path,
            String body,
            int status,
            String expected) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:" + PORT + path))
                .timeout(REQUEST_TIMEOUT);
        if (body == null) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json");
        }
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != status || !response.body().contains(expected)) {
            throw new IllegalStateException("Unexpected " + method + " " + path + " response: "
                    + response.statusCode() + " " + response.body());
        }
    }
}
