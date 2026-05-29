package com.qin.runtime.spring;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies Spring host support only selects actual Spring component classes
 * from a mixed Qin compile unit.
 */
public final class QinSpringHostSupportComponentSelectionSmokeTestMain {
    private QinSpringHostSupportComponentSelectionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Files.createTempFile("qin-spring-host-support-", ".qin");
        Files.writeString(source, """
                import { RestController as RC, PostMapping as POST, RequestBody as Body } from "java:org.springframework.web.bind.annotation"
                import { JsonProperty as JP } from "java:com.fasterxml.jackson.annotation"

                class Payload {
                  @JP("user_name")
                  name: string = "guest"
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
                QinSpringHostSupport.compileSources(
                        QinSpringHostSupportComponentSelectionSmokeTestMain.class.getClassLoader(),
                        source);

        if (compiled.definedClasses().size() != 2) {
            throw new IllegalStateException("Expected two defined classes, got " + compiled.definedClasses().size());
        }
        if (compiled.springBeanClasses().size() != 1) {
            throw new IllegalStateException("Expected exactly one Spring bean class, got " + compiled.springBeanClasses().size());
        }

        Class<?> beanClass = compiled.springBeanClasses().get(0);
        if (!"PostController".equals(beanClass.getName())) {
            throw new IllegalStateException("Unexpected Spring bean class: " + beanClass.getName());
        }
        if (QinSpringHostSupport.isSpringBeanClass(compiled.definedClasses().get("Payload"))) {
            throw new IllegalStateException("Payload DTO must not be treated as a Spring bean");
        }

        System.out.println("QinSpringHostSupportComponentSelectionSmokeTestMain passed.");
    }
}
