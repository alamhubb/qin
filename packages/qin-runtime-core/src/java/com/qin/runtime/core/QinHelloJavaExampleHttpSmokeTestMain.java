package com.qin.runtime.core;

import com.qin.runtime.http.QinSpringHttpSmokeTestApp;
import com.qin.runtime.spring.QinSpringHostSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;

/**
 * End-to-end smoke test for the real hello-java example sources.
 */
public final class QinHelloJavaExampleHttpSmokeTestMain {
    private QinHelloJavaExampleHttpSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = resolveHelloJavaRoot();
        Path serviceSource = root.resolve("src/server/HelloService.qin").normalize();
        Path controllerSource = root.resolve("src/server/HelloController.qin").normalize();

        QinSpringHostSupport.QinSpringCompiledSources compiled =
                QinSpringHostSupport.compileSources(
                        QinHelloJavaExampleHttpSmokeTestMain.class.getClassLoader(),
                        serviceSource,
                        controllerSource);

        SpringApplication application = new SpringApplication(QinSpringHttpSmokeTestApp.class);
        application.setDefaultProperties(Map.of("server.port", "0"));
        application.addInitializers(context -> QinSpringHostSupport.registerSpringBeans(context, compiled.springBeanClasses()));

        ConfigurableApplicationContext context = application.run();
        try {
            int port = QinSpringHostSupport.resolvePort(context);
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            requireGet(client, port, "/api/hello", "hello from qin service");
            requireGet(client, port, "/api/hello/detail", "{\"message\":\"hello from qin service\",\"ok\":true}");
            requireGet(client, port, "/api/hello/detail/json", "{\"message\":\"hello from qin service\",\"ok\":true}");
            requireGet(client, port, "/api/ping", "pong");
            requirePost(client, port, "/api/greet", """
                    {"name":"  qin  "}
                    """, "hello qin");
            requirePost(client, port, "/api/greet/loud", """
                    {"name":"qin"}
                    """, "HELLO QIN");
        } finally {
            context.close();
        }

        System.out.println("QinHelloJavaExampleHttpSmokeTestMain passed.");
        System.out.println("root: " + root.toAbsolutePath());
    }

    private static Path resolveHelloJavaRoot() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[]{
                cwd.resolve("examples/apps/hello-java"),
                cwd.resolve("qin/examples/apps/hello-java"),
                cwd.resolve("../examples/apps/hello-java").normalize()
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate.resolve("src/server/HelloService.qin"))) {
                return candidate.normalize();
            }
        }
        StringBuilder message = new StringBuilder("Cannot locate hello-java example root. Tried:\n");
        for (Path candidate : candidates) {
            message.append("  - ").append(candidate.normalize()).append('\n');
        }
        throw new IllegalArgumentException(message.toString().trim());
    }

    private static void requireGet(HttpClient client, int port, String path, String expectedBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + port + path))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        requireResponse(path, response, expectedBody);
    }

    private static void requirePost(
            HttpClient client,
            int port,
            String path,
            String jsonBody,
            String expectedBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + port + path))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        requireResponse(path, response, expectedBody);
    }

    private static void requireResponse(String path, HttpResponse<String> response, String expectedBody) {
        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "Unexpected HTTP status for `" + path + "`: " + response.statusCode() + ", body=" + response.body());
        }
        if (!expectedBody.equals(response.body())) {
            throw new IllegalStateException(
                    "Unexpected HTTP body for `" + path + "`: " + response.body());
        }
    }
}
