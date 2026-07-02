package com.qin.core;

import com.qin.types.CompileResult;
import com.qin.types.QinConfig;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JavaRunnerIncrementalApiChangeSmokeTestMain {
    private JavaRunnerIncrementalApiChangeSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-java-incremental-api-change-");
        Path sourceDir = root.resolve("src");
        Files.createDirectories(sourceDir);

        Path api = sourceDir.resolve("Api.java");
        Path caller = sourceDir.resolve("Caller.java");
        Files.writeString(api, """
                public final class Api {
                  public String value() {
                    return "old";
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(caller, """
                public final class Caller {
                  public static String call() {
                    return new Api().value();
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "incremental-api-change-smoke",
                  version: "0.1.0",
                  entry: "src/Caller.java",
                  java: {
                    release: "21",
                    sourceDir: "src",
                    outputDir: "build/classes",
                    encoding: "UTF-8"
                  }
                }
                """, StandardCharsets.UTF_8);

        QinConfig config = new ConfigLoader(root.toString()).load();
        CompileResult first = new JavaRunner(config, "", root.toString()).compile();
        if (!first.isSuccess()) {
            throw new IllegalStateException("Initial compile failed: " + first.getError());
        }

        Files.writeString(api, """
                public final class Api {
                  public static String value() {
                    return "new";
                  }
                }
                """, StandardCharsets.UTF_8);

        CompileResult second = new JavaRunner(config, "", root.toString()).compile();
        if (!second.isSuccess()) {
            throw new IllegalStateException("API-change compile failed: " + second.getError());
        }
        if (second.getCompiledFiles() < 2) {
            throw new IllegalStateException("API-change compile must recompile caller and callee");
        }

        String value = invokeCaller(root.resolve("build/classes"));
        if (!"new".equals(value)) {
            throw new IllegalStateException("Expected recompiled caller to return new but got " + value);
        }

        System.out.println("JavaRunnerIncrementalApiChangeSmokeTestMain OK");
    }

    private static String invokeCaller(Path outputDir) throws Exception {
        try (URLClassLoader loader = new URLClassLoader(new URL[] { outputDir.toUri().toURL() }, null)) {
            Class<?> callerClass = Class.forName("Caller", true, loader);
            Method call = callerClass.getMethod("call");
            return (String) call.invoke(null);
        }
    }
}
