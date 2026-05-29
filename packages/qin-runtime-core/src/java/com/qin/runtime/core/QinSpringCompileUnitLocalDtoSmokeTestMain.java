package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Smoke test for compiling and defining local Qin DTO + controller classes together.
 */
public final class QinSpringCompileUnitLocalDtoSmokeTestMain {
    private QinSpringCompileUnitLocalDtoSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Files.createTempFile("qin-compile-unit-local-dto-", ".qin");
        Files.writeString(source, """
                import { RestController as RC, PostMapping as POST, RequestBody as Body } from "java:org.springframework.web.bind.annotation"

                class Payload {
                  name: string = "guest"
                  active: boolean = true
                }

                @RC
                class PostController {
                  @POST("/api/create")
                  create(@Body payload: Payload) {
                    return "created"
                  }
                }
                """, StandardCharsets.UTF_8);

        QinSpringCompileUnit compileUnit = QinSpringCompileUnit.compile(source);
        if (compileUnit.compiledClasses().size() != 2) {
            throw new IllegalStateException(
                    "Expected two compiled classes, got " + compileUnit.compiledClasses().size());
        }
        if (!compileUnit.compiledClasses().containsKey("Payload")) {
            throw new IllegalStateException("Missing compiled local DTO class Payload");
        }
        if (!compileUnit.compiledClasses().containsKey("PostController")) {
            throw new IllegalStateException("Missing compiled controller class PostController");
        }

        Map<String, Class<?>> defined = compileUnit.defineAll(QinSpringCompileUnitLocalDtoSmokeTestMain.class.getClassLoader());
        Class<?> payloadClass = defined.get("Payload");
        if (payloadClass == null) {
            throw new IllegalStateException("Payload class was not defined");
        }
        payloadClass.getDeclaredField("name");
        payloadClass.getDeclaredField("active");
        payloadClass.getDeclaredMethod("getName");
        payloadClass.getDeclaredMethod("isActive");
        payloadClass.getDeclaredMethod("setName", String.class);
        payloadClass.getDeclaredMethod("setActive", boolean.class);
        Object payload = payloadClass.getDeclaredConstructor().newInstance();
        Object defaultName = payloadClass.getDeclaredMethod("getName").invoke(payload);
        Object defaultActive = payloadClass.getDeclaredMethod("isActive").invoke(payload);
        if (!"guest".equals(defaultName)) {
            throw new IllegalStateException("Unexpected default Payload.name: " + defaultName);
        }
        if (!Boolean.TRUE.equals(defaultActive)) {
            throw new IllegalStateException("Unexpected default Payload.active: " + defaultActive);
        }
        Object allArgsPayload = payloadClass.getDeclaredConstructor(String.class, boolean.class)
                .newInstance("alice", false);
        Object allArgsName = payloadClass.getDeclaredMethod("getName").invoke(allArgsPayload);
        Object allArgsActive = payloadClass.getDeclaredMethod("isActive").invoke(allArgsPayload);
        if (!"alice".equals(allArgsName)) {
            throw new IllegalStateException("Unexpected all-args Payload.name: " + allArgsName);
        }
        if (!Boolean.FALSE.equals(allArgsActive)) {
            throw new IllegalStateException("Unexpected all-args Payload.active: " + allArgsActive);
        }

        System.out.println("QinSpringCompileUnitLocalDtoSmokeTestMain passed.");
    }
}
