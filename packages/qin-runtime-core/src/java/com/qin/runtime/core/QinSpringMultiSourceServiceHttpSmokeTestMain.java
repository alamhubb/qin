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
 * End-to-end smoke test for multi-source Qin Spring beans with service/controller collaboration.
 */
public final class QinSpringMultiSourceServiceHttpSmokeTestMain {
    private QinSpringMultiSourceServiceHttpSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path serviceSource = Files.createTempFile("qin-spring-service-", ".qin");
        Path controllerSource = Files.createTempFile("qin-spring-controller-", ".qin");

        Files.writeString(serviceSource, """
                import { Service as S } from "java:org.springframework.stereotype"

                @S
                class HelloService {
                  message() {
                    return "hello from qin service"
                  }
                }
                """, StandardCharsets.UTF_8);

        Files.writeString(controllerSource, """
                import { Autowired as A } from "java:org.springframework.beans.factory.annotation"
                import { RestController as RC, GetMapping as GET } from "java:org.springframework.web.bind.annotation"

                @RC
                class HelloController {
                  @A
                  service: HelloService

                  @GET("/api/hello")
                  hello() {
                    return this.service.message()
                  }
                }
                """, StandardCharsets.UTF_8);

        QinSpringHostSupport.QinSpringCompiledSources compiled =
                QinSpringHostSupport.compileSources(
                        QinSpringMultiSourceServiceHttpSmokeTestMain.class.getClassLoader(),
                        serviceSource,
                        controllerSource);

        if (compiled.definedClasses().size() != 2) {
            throw new IllegalStateException("Expected two defined Qin classes, got " + compiled.definedClasses().size());
        }
        if (compiled.springBeanClasses().size() != 2) {
            throw new IllegalStateException("Expected two Spring bean classes, got " + compiled.springBeanClasses().size());
        }

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
                    .uri(URI.create("http://127.0.0.1:" + port + "/api/hello"))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Unexpected HTTP status: " + response.statusCode() + ", body=" + response.body());
            }
            if (!"hello from qin service".equals(response.body())) {
                throw new IllegalStateException("Unexpected HTTP body: " + response.body());
            }
        } finally {
            context.close();
        }

        System.out.println("QinSpringMultiSourceServiceHttpSmokeTestMain passed.");
    }
}
