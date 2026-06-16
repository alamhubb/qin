package com.qin.lang.module.resolver;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmSymlinkedPackageDependencySmokeTestMain {
    private QinEsmSymlinkedPackageDependencySmokeTestMain() {
    }

    public static void main(String[] args) {
        Path workspaceRoot = findWorkspaceRoot();
        Path importer = workspaceRoot
                .resolve("ovsjs")
                .resolve("node_modules")
                .resolve("cssts-compiler")
                .resolve("dist")
                .resolve("index.mjs");
        Path expectedPackageRoot = workspaceRoot
                .resolve("cssts")
                .resolve("node_modules")
                .resolve("slime-parser");
        require(Files.isRegularFile(importer), "workspace symlinked cssts-compiler dist importer exists");
        require(Files.isDirectory(expectedPackageRoot), "real cssts workspace dependency exists");

        Path resolved = new QinEsmSpecifierResolver().resolveModule(importer, "slime-parser");
        require(resolved != null, "slime-parser resolves from symlinked package real path");
        require(resolved.toAbsolutePath().normalize().startsWith(expectedPackageRoot.toAbsolutePath().normalize()),
                "resolved dependency comes from real package node_modules: " + resolved);

        System.out.println("Resolved symlinked package dependency: " + resolved);
        System.out.println("QinEsmSymlinkedPackageDependencySmokeTestMain OK");
    }

    private static Path findWorkspaceRoot() {
        Path search = Path.of("").toAbsolutePath().normalize();
        while (search != null) {
            if (Files.isDirectory(search.resolve("ovsjs")) && Files.isDirectory(search.resolve("cssts"))) {
                return search;
            }
            search = search.getParent();
        }
        throw new IllegalStateException("Cannot find qinall workspace root containing ovsjs and cssts");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
