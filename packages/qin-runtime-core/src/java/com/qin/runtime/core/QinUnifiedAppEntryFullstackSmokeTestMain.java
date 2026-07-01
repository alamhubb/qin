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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class QinUnifiedAppEntryFullstackSmokeTestMain {
    private static final int PORT = 18132;

    private QinUnifiedAppEntryFullstackSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-unified-app-entry-");
        writeProject(root);
            Process process = startServer(root);
        try {
            waitForServer();
            requireResponse("GET", "/", 200, "<script");
            requireResponse("GET", "/app.js", 200, "/@qin-mod/src/app.js");
            requireResponse("GET", "/@qin-mod/src/app.js", 200, "root.textContent = \"hello\"");
            requireNoResponseBodyPart("GET", "/@qin-mod/src/app.js", "export object App");
            requireResponse("GET", "/api/hello", 200, "hello");
            requireResponse("GET", "/api/health", 200, "\"ok\":true");
            System.out.println("QinUnifiedAppEntryFullstackSmokeTestMain passed.");
        } finally {
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private static void writeProject(Path root) throws Exception {
        Files.createDirectories(root.resolve("src/main/controllers"));
        Files.writeString(root.resolve("src/app.qin"), """
                import { WebRoot } from "qin"
                import { HelloController } from "./main/controllers/HelloController.qin"

                @WebRoot("/api")
                export object App {
                  controllers = [
                    HelloController
                  ]
                }

                const root = document.getElementById("app") || document.body
                root.textContent = "hello"
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("src/main/controllers/HelloController.qin"), """
                import { Controller, RequestMapping, GetMapping } from "qin"
                import { QinWeb } from "java:com.qin.web"

                @Controller
                @RequestMapping("/hello")
                export object HelloController {
                  controllerName = "HelloController"

                  @GetMapping("")
                  hello(request) {
                    return QinWeb.text("hello")
                  }
                }
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
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(root.toFile());
        pb.inheritIO();
        return pb.start();
    }

    private static String absoluteClasspath() {
        String separator = System.getProperty("path.separator");
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Set<String> entries = new LinkedHashSet<>();
        for (String entry : readCachedClasspathEntries(cwd.resolve("packages/qin-runtime-core/.qin/classpath.json"))) {
            entries.add(entry);
        }
        for (String entry : System.getProperty("java.class.path").split(java.util.regex.Pattern.quote(separator))) {
            if (entry == null || entry.isBlank()) {
                continue;
            }
            if (entry.endsWith("/*") || entry.endsWith("\\*")) {
                String prefix = entry.substring(0, entry.length() - 2);
                Path prefixPath = Path.of(prefix);
                entries.add((prefixPath.isAbsolute() ? prefixPath : cwd.resolve(prefixPath)).normalize() + entry.substring(entry.length() - 2));
                continue;
            }
            Path path = Path.of(entry);
            entries.add((path.isAbsolute() ? path : cwd.resolve(path)).normalize().toString());
        }
        return String.join(separator, entries);
    }

    private static List<String> readCachedClasspathEntries(Path cacheFile) {
        if (!Files.isRegularFile(cacheFile)) {
            return List.of();
        }
        try {
            String json = Files.readString(cacheFile, StandardCharsets.UTF_8);
            int arrayStart = json.indexOf('[');
            int arrayEnd = json.indexOf(']', arrayStart);
            if (arrayStart < 0 || arrayEnd < 0) {
                throw new IllegalStateException("Invalid Qin classpath cache: " + cacheFile);
            }
            List<String> entries = new ArrayList<>();
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\"((?:\\\\.|[^\"])*)\"")
                    .matcher(json.substring(arrayStart + 1, arrayEnd));
            while (matcher.find()) {
                String entry = matcher.group(1)
                        .replace("\\\\", "\\")
                        .replace("\\\"", "\"");
                if (!entry.isBlank()) {
                    entries.add(entry);
                }
            }
            return entries;
        } catch (Exception error) {
            throw new IllegalStateException("Failed to read Qin classpath cache: " + cacheFile, error);
        }
    }

    private static void waitForServer() throws Exception {
        Instant deadline = Instant.now().plus(Duration.ofSeconds(25));
        while (Instant.now().isBefore(deadline)) {
            try {
                HttpResponse<String> response = send("GET", "/api/health");
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

    private static void requireResponse(String method, String path, int status, String expectedBodyPart) throws Exception {
        HttpResponse<String> response = send(method, path);
        if (response.statusCode() != status || !response.body().contains(expectedBodyPart)) {
            throw new IllegalStateException("Unexpected response for " + path + ": "
                    + response.statusCode() + " " + response.body());
        }
    }

    private static void requireNoResponseBodyPart(String method, String path, String forbiddenBodyPart) throws Exception {
        HttpResponse<String> response = send(method, path);
        if (response.body().contains(forbiddenBodyPart)) {
            throw new IllegalStateException("Unexpected response body part for " + path + ": " + forbiddenBodyPart);
        }
    }

    private static HttpResponse<String> send(String method, String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + PORT + path))
                .method(method, HttpRequest.BodyPublishers.noBody())
                .timeout(Duration.ofSeconds(5))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
