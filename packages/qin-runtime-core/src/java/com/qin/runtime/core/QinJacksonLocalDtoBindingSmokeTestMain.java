package com.qin.runtime.core;

import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Smoke test for Jackson binding against a Qin-generated local DTO class.
 */
public final class QinJacksonLocalDtoBindingSmokeTestMain {
    private QinJacksonLocalDtoBindingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Files.createTempFile("qin-jackson-local-dto-", ".qin");
        Files.writeString(source, """
                import { JsonProperty as JP } from "java:com.fasterxml.jackson.annotation"

                class Payload {
                  @JP("user_name")
                  name: string = "guest"
                  active: boolean = true
                }
                """, StandardCharsets.UTF_8);

        QinSpringCompileUnit compileUnit = QinSpringCompileUnit.compile(source);
        Map<String, Class<?>> defined = compileUnit.defineAll(QinJacksonLocalDtoBindingSmokeTestMain.class.getClassLoader());
        Class<?> payloadClass = defined.get("Payload");
        if (payloadClass == null) {
            throw new IllegalStateException("Payload class was not defined");
        }

        ObjectMapper mapper = new ObjectMapper();
        Object fromAllArgs = mapper.readValue("""
                {"user_name":"alice","active":false}
                """, payloadClass);
        requireProperty(payloadClass, fromAllArgs, "getName", "alice");
        requireProperty(payloadClass, fromAllArgs, "isActive", Boolean.FALSE);

        Object fromDefaults = mapper.readValue("""
                {}
                """, payloadClass);
        requireProperty(payloadClass, fromDefaults, "getName", "guest");
        requireProperty(payloadClass, fromDefaults, "isActive", Boolean.TRUE);

        System.out.println("QinJacksonLocalDtoBindingSmokeTestMain passed.");
    }

    private static void requireProperty(
            Class<?> payloadClass,
            Object instance,
            String methodName,
            Object expectedValue) throws Exception {
        Object actualValue = payloadClass.getDeclaredMethod(methodName).invoke(instance);
        if (!expectedValue.equals(actualValue)) {
            throw new IllegalStateException(
                    "Unexpected property value for `" + methodName + "`: " + actualValue);
        }
    }
}
