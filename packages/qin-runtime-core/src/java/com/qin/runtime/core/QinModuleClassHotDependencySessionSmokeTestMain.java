package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinModuleClassHotDependencySessionSmokeTestMain {
    private QinModuleClassHotDependencySessionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-module-class-hot-session-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"module-class-hot-session\" }\n",
                StandardCharsets.UTF_8);
        Path events = root.resolve("events.txt");
        String eventsLiteral = QinJsPackageRunner.renderJsLiteral(events.toString());
        Files.writeString(root.resolve("dep.js"), """
                import { appendFileSync } from "node:fs";
                appendFileSync(%s, "D");
                export const value = 41;
                """.formatted(eventsLiteral), StandardCharsets.UTF_8);
        Path entry = root.resolve("entry.js");
        Files.writeString(entry, """
                import { appendFileSync } from "node:fs";
                import { value } from "./dep.js";
                appendFileSync(%s, "E");
                value + 1;
                """.formatted(eventsLiteral), StandardCharsets.UTF_8);

        QinInMemoryJvmRunner runner = new QinInMemoryJvmRunner();
        Object first = runner.compileAndRunModuleClasses(
                entry,
                root,
                "probe.QinModuleClassHotDependencySessionSmoke",
                "",
                root.resolve(".qin").resolve("module-class-cache"),
                "");
        requireResult(first, "first");
        requireEvents(events, "DE", "first");

        Object second = runner.compileAndRunModuleClasses(
                entry,
                root,
                "probe.QinModuleClassHotDependencySessionSmoke",
                "",
                root.resolve(".qin").resolve("module-class-cache"),
                "");
        requireResult(second, "second");
        requireEvents(events, "DEE", "second");

        System.out.println("QinModuleClassHotDependencySessionSmokeTestMain OK");
    }

    private static void requireResult(Object value, String label) {
        if (!(value instanceof Number number) || number.intValue() != 42) {
            throw new IllegalStateException("Unexpected " + label + " result: " + value);
        }
    }

    private static void requireEvents(Path events, String expected, String label) throws Exception {
        String actual = Files.readString(events, StandardCharsets.UTF_8);
        if (!expected.equals(actual)) {
            throw new IllegalStateException(
                    "Expected " + label + " module events " + expected + ", got " + actual);
        }
    }
}
