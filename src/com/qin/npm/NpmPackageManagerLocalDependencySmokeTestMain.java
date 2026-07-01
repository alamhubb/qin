package com.qin.npm;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class NpmPackageManagerLocalDependencySmokeTestMain {
    private NpmPackageManagerLocalDependencySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-npm-local-deps-");
        Path helper = root.resolve("helper");
        Path provider = root.resolve("provider");
        Path app = root.resolve("app");

        Files.createDirectories(helper);
        Files.createDirectories(provider);
        Files.createDirectories(app);

        Files.writeString(helper.resolve("package.json"), """
                {
                  "name": "helper",
                  "version": "1.0.0",
                  "bin": {
                    "helper-cli": "bin/helper.js"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.createDirectories(helper.resolve("bin"));
        Files.writeString(helper.resolve("bin").resolve("helper.js"), "console.log('helper')\n", StandardCharsets.UTF_8);

        Files.writeString(provider.resolve("package.json"), """
                {
                  "name": "provider",
                  "version": "1.0.0",
                  "dependencies": {
                    "helper": "file:../helper"
                  }
                }
                """, StandardCharsets.UTF_8);

        Files.writeString(app.resolve("package.json"), """
                {
                  "name": "app",
                  "version": "1.0.0",
                  "dependencies": {
                    "helper": "file:../helper",
                    "provider": "file:../provider"
                  }
                }
                """, StandardCharsets.UTF_8);

        NpmPackageManager npm = new NpmPackageManager(app.toString());
        require(npm.installAll(), "installAll succeeds");
        require(Files.exists(app.resolve("node_modules").resolve("helper").resolve("package.json")),
                "top-level helper installed");
        require(Files.exists(app.resolve("node_modules").resolve("provider").resolve("package.json")),
                "provider installed");
        require(Files.exists(provider.resolve("node_modules").resolve("helper").resolve("package.json")),
                "provider runtime helper installed under provider node_modules");
        require(Files.exists(provider.resolve("node_modules").resolve(".bin").resolve("helper-cli.cmd"))
                        || Files.exists(provider.resolve("node_modules").resolve(".bin").resolve("helper-cli")),
                "provider dependency bin installed under provider node_modules");

        System.out.println("NpmPackageManagerLocalDependencySmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
