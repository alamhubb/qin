package com.qin.runtime.core;

import com.qin.runtime.http.QinSpringHttpSmokeTestApp;
import com.qin.runtime.spring.QinSpringHostSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;

/**
 * End-to-end smoke test for Spring MVC JSON RequestBody binding against Qin-generated DTO/controller classes.
 */
public final class QinSpringHttpJsonRoundTripSmokeTestMain {
    private QinSpringHttpJsonRoundTripSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Files.createTempFile("qin-spring-http-json-", ".qin");
        Files.writeString(source, """
                import { RestController as RC, PostMapping as POST, RequestBody as Body } from "java:org.springframework.web.bind.annotation"
                import { JsonProperty as JP } from "java:com.fasterxml.jackson.annotation"

                class Payload {
                  @JP("user_name")
                  name: string = "guest"
                  active: boolean = true
                }

                @RC
                class PostController {
                  @POST("/api/create")
                  create(@Body payload: Payload) {
                    return payload.name
                  }
                }
                """, StandardCharsets.UTF_8);

        QinSpringHostSupport.QinSpringCompiledSources compiled =
                QinSpringHostSupport.compileSources(QinSpringHttpJsonRoundTripSmokeTestMain.class.getClassLoader(), source);

        SpringApplication application = new SpringApplication(QinSpringHttpSmokeTestApp.class);
        application.setDefaultProperties(Map.of("server.port", "0"));
        application.addInitializers(context -> QinSpringHostSupport.registerSpringBeans(context, compiled.springBeanClasses()));

        ConfigurableApplicationContext context = application.run();
        try {
            int port = QinSpringHostSupport.resolvePort(context);
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:" + port + "/api/create"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("""
                            {"user_name":"alice","active":false}
                            """))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Unexpected HTTP status: " + response.statusCode() + ", body=" + response.body());
            }
            if (!"alice".equals(response.body())) {
                throw new IllegalStateException("Unexpected HTTP body: " + response.body());
            }
        } finally {
            context.close();
        }

        System.out.println("QinSpringHttpJsonRoundTripSmokeTestMain passed.");
    }
}
