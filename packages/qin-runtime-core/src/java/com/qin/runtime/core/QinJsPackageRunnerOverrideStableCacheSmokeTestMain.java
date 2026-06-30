package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsPackageRunnerOverrideStableCacheSmokeTestMain {
    private QinJsPackageRunnerOverrideStableCacheSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path firstPackage = createOverridePackage("first");
        Path secondPackage = createOverridePackage("second");

        Object first = new QinJsPackageRunner().runModuleSource(
                createTempProjectRoot("first", firstPackage),
                "import { answer } from \"stable-override-package\";\nanswer + 1\n",
                "stable_override_package_cache_smoke");
        if (!(first instanceof Number firstNumber) || firstNumber.intValue() != 43) {
            throw new IllegalStateException("Unexpected first result: " + first);
        }

        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        Object second;
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            second = new QinJsPackageRunner().runModuleSource(
                    createTempProjectRoot("second", secondPackage),
                    "import { answer } from \"stable-override-package\";\nanswer + 1\n",
                    "stable_override_package_cache_smoke");
        } finally {
            System.setOut(originalOut);
        }

        if (!(second instanceof Number secondNumber) || secondNumber.intValue() != 43) {
            throw new IllegalStateException("Unexpected second result: " + second);
        }
        String log = captured.toString(StandardCharsets.UTF_8);
        if (!log.contains("module-class disk cache hit")) {
            throw new IllegalStateException("Expected stable module-class disk cache hit across override roots, got:\n" + log);
        }
        System.out.println("QinJsPackageRunnerOverrideStableCacheSmokeTestMain OK");
    }

    private static Path createTempProjectRoot(String label, Path overridePackage) throws Exception {
        Path root = Files.createTempDirectory("qin-js-package-runner-override-cache-" + label + "-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "stable-override-cache-smoke",
                  packageOverrides: {
                    "stable-override-package": "%s"
                  }
                }
                """.formatted(jsPath(overridePackage)), StandardCharsets.UTF_8);
        return root;
    }

    private static Path createOverridePackage(String label) throws Exception {
        Path packageRoot = Files.createTempDirectory("qin-stable-override-package-" + label + "-");
        Files.writeString(packageRoot.resolve("package.json"), """
                {
                  "name": "stable-override-package",
                  "type": "module",
                  "main": "./index.js",
                  "module": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(packageRoot.resolve("index.js"), """
                export const answer = 42;
                """, StandardCharsets.UTF_8);
        return packageRoot;
    }

    private static String jsPath(Path path) {
        return path.toAbsolutePath().normalize().toString().replace("\\", "\\\\");
    }
}
