package com.qin.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCliDevScriptDispatchSmokeTestMain {
    private QinCliDevScriptDispatchSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cli-dev-script-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-cli-dev-script",
                  scripts: {
                    dev: "echo DEV_SCRIPT_OK"
                  }
                }
                """, StandardCharsets.UTF_8);

        String previousUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", root.toString());
        try {
            String output = captureDevMode("--dry-run");
            require(output.contains("DEV_SCRIPT_OK"), "qin dev --dry-run must dispatch to scripts.dev: " + output);

            Files.writeString(root.resolve("qin.config.js"), """
                    export default {
                      name: "qin-cli-dev-self",
                      scripts: {
                        dev: "qin dev"
                      }
                    }
                    """, StandardCharsets.UTF_8);
            String selfOutput = captureSelfReferential("qin dev");
            require(Boolean.parseBoolean(selfOutput), "self-referential scripts.dev must not be executed as a script");
            System.out.println("QinCliDevScriptDispatchSmokeTestMain OK");
        } finally {
            System.setProperty("user.dir", previousUserDir);
        }
    }

    private static String captureDevMode(String... args) throws Exception {
        Method method = QinCli.class.getDeclaredMethod("devMode", String[].class);
        method.setAccessible(true);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream previousOut = System.out;
        try (PrintStream stream = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(stream);
            method.invoke(null, (Object) args);
        } finally {
            System.setOut(previousOut);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }

    private static String captureSelfReferential(String script) throws Exception {
        Method method = QinCli.class.getDeclaredMethod("isSelfReferentialQinDevScript", String.class);
        method.setAccessible(true);
        return String.valueOf(method.invoke(null, script));
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
