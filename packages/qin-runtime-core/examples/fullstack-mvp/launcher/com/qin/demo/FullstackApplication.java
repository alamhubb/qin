package com.qin.demo;

import com.qin.runtime.core.QinRuntimeApi;

import java.nio.file.Path;
import java.util.Map;

/**
 * Convention startup class for the fullstack MVP project.
 * Run this class directly, no command-line args required.
 */
public final class FullstackApplication {
    private FullstackApplication() {
    }

    public static Object run() {
        return Map.of(
                "age", 18,
                "message", "hello from Qin backend",
                "source", "FullstackApplication.java");
    }

    public static void main(String[] args) throws Exception {
        QinRuntimeApi.runFullstack(Path.of("").toAbsolutePath().normalize(), 8080, true);
    }
}
