package com.qin.runtime.core;

import java.net.ServerSocket;
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

public final class QinHttpAppSinglePortSmokeTestMain {
    private QinHttpAppSinglePortSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        int port = freePort();
        Path root = Files.createTempDirectory("qin-http-app-single-port-");
        Files.createDirectories(root.resolve("main"));
        Files.createDirectories(root.resolve("app"));
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: 'qin-http-app-single-port-smoke',
                  backend: { entry: 'main/Main.java' },
                  frontend: { entry: 'app/main.js', staticDir: 'app' }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/Main.java"), """
                package demo;

                import com.qin.runtime.http.QinHttpApp;
                import java.util.ArrayList;
                import java.util.LinkedHashMap;
                import java.util.List;
                import java.util.Map;
                import java.util.concurrent.atomic.AtomicLong;

                public final class Main {
                    private static final AtomicLong nextId = new AtomicLong(2);
                    private static final List<Map<String, Object>> users = new ArrayList<>();

                    static {
                        users.add(user(1, "Qin Test", "qin-test@example.com"));
                    }

                    private Main() {
                    }

                    public static Object run() {
                        return QinHttpApp.create()
                                .get("/api/users", c -> c.json(snapshot()))
                                .post("/api/users", c -> {
                                    String name = c.jsonString("name");
                                    String email = c.jsonString("email");
                                    if (name == null || email == null) {
                                        return c.json(Map.of("error", "name and email are required"), 400);
                                    }
                                    Map<String, Object> created = user(nextId.getAndIncrement(), name, email);
                                    synchronized (users) {
                                        users.add(created);
                                    }
                                    return c.json(created, 201);
                                })
                                .delete("/api/users/:id", c -> {
                                    long id = Long.parseLong(c.param("id"));
                                    synchronized (users) {
                                        users.removeIf(user -> ((Number) user.get("id")).longValue() == id);
                                    }
                                    return c.noContent();
                                });
                    }

                    private static List<Map<String, Object>> snapshot() {
                        synchronized (users) {
                            return new ArrayList<>(users);
                        }
                    }

                    private static Map<String, Object> user(long id, String name, String email) {
                        Map<String, Object> user = new LinkedHashMap<>();
                        user.put("id", id);
                        user.put("name", name);
                        user.put("email", email);
                        return user;
                    }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("app/index.html"), """
                <!doctype html>
                <html><body><div id="app">Qin HTTP app smoke</div><script type="module" src="/app.js"></script></body></html>
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("app/main.js"), """
                document.body.dataset.qinHttpAppSmoke = 'ok';
                """, StandardCharsets.UTF_8);

        Process process = startServer(root, port);
        try {
            waitForServer(port, Duration.ofSeconds(30));
            verifyCrud(port);
            System.out.println("QinHttpAppSinglePortSmokeTestMain OK");
            System.out.println("url: http://localhost:" + port);
        } finally {
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private static Process startServer(Path root, int port) throws Exception {
        String javaBin = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add("com.qin.runtime.core.QinFullstackMain");
        command.add("--root");
        command.add(root.toString());
        command.add("--backend-file");
        command.add(root.resolve("main/Main.java").toString());
        command.add("--frontend-file");
        command.add(root.resolve("app/main.js").toString());
        command.add("--static-dir");
        command.add(root.resolve("app").toString());
        command.add("--class-out");
        command.add(root.resolve("build/server-classes").toString());
        command.add("--port");
        command.add(Integer.toString(port));
        command.add("--dev");
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(root.toFile());
        builder.inheritIO();
        return builder.start();
    }

    private static void waitForServer(int port, Duration timeout) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://127.0.0.1:" + port + "/api/health");
        Instant deadline = Instant.now().plus(timeout);
        while (Instant.now().isBefore(deadline)) {
            try {
                HttpResponse<String> response = client.send(
                        HttpRequest.newBuilder(uri).GET().timeout(Duration.ofSeconds(2)).build(),
                        HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return;
                }
            } catch (Exception ignored) {
                Thread.sleep(250);
            }
            Thread.sleep(250);
        }
        throw new IllegalStateException("Server did not become healthy: " + uri);
    }

    private static void verifyCrud(int port) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String base = "http://127.0.0.1:" + port;
        HttpResponse<String> list = client.send(
                HttpRequest.newBuilder(URI.create(base + "/api/users")).GET().timeout(Duration.ofSeconds(5)).build(),
                HttpResponse.BodyHandlers.ofString());
        require(list.statusCode() == 200 && list.body().contains("qin-test@example.com"), "initial users", list);

        HttpResponse<String> created = client.send(
                HttpRequest.newBuilder(URI.create(base + "/api/users"))
                        .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"Qin UI\",\"email\":\"qin-ui@example.com\"}"))
                        .header("Content-Type", "application/json")
                        .timeout(Duration.ofSeconds(5))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        require(created.statusCode() == 201 && created.body().contains("\"id\":2"), "create user", created);

        HttpResponse<String> deleted = client.send(
                HttpRequest.newBuilder(URI.create(base + "/api/users/2"))
                        .DELETE()
                        .timeout(Duration.ofSeconds(5))
                        .build(),
                HttpResponse.BodyHandlers.ofString());
        require(deleted.statusCode() == 204, "delete user", deleted);

        HttpResponse<String> after = client.send(
                HttpRequest.newBuilder(URI.create(base + "/api/users")).GET().timeout(Duration.ofSeconds(5)).build(),
                HttpResponse.BodyHandlers.ofString());
        require(after.statusCode() == 200 && !after.body().contains("qin-ui@example.com"), "users after delete", after);
    }

    private static void require(boolean condition, String label, HttpResponse<String> response) {
        if (!condition) {
            throw new IllegalStateException("Unexpected " + label + ": " + response.statusCode() + " " + response.body());
        }
    }

    private static int freePort() throws Exception {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }
}
