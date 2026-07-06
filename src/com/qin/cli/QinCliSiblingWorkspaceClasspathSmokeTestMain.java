package com.qin.cli;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class QinCliSiblingWorkspaceClasspathSmokeTestMain {
    private QinCliSiblingWorkspaceClasspathSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path smokeRoot = Path.of(System.getProperty("user.dir"), "smoke-workspaces");
        Files.createDirectories(smokeRoot);
        Path root = Files.createTempDirectory(smokeRoot, "qin-cli-sibling-classpath-");
        Path javaSlime = root.resolve("slime").resolve("java-slime");
        Path rootClasses = javaSlime.resolve("build").resolve("classes");
        Path packageClasses = javaSlime.resolve("slime-parser").resolve("build").resolve("classes");
        Path hiddenClasses = javaSlime.resolve(".hidden").resolve("build").resolve("classes");
        Path nodeModulesClasses = javaSlime.resolve("node_modules").resolve("build").resolve("classes");
        Path nestedClasses = javaSlime.resolve("slime-parser").resolve("nested").resolve("build").resolve("classes");
        Files.createDirectories(rootClasses);
        Files.createDirectories(packageClasses);
        Files.createDirectories(hiddenClasses);
        Files.createDirectories(nodeModulesClasses);
        Files.createDirectories(nestedClasses);

        try {
            List<String> entries = new ArrayList<>();
            Method method = QinCli.class.getDeclaredMethod(
                    "addSiblingWorkspaceRuntimeClasspath",
                    List.class,
                    Path.class);
            method.setAccessible(true);
            method.invoke(null, entries, root);

            requireContains(entries, rootClasses, "sibling root build/classes");
            requireContains(entries, packageClasses, "direct sibling package build/classes");
            requireNotContains(entries, hiddenClasses, "hidden directories are not sibling packages");
            requireNotContains(entries, nodeModulesClasses, "node_modules is not a sibling package");
            requireNotContains(entries, nestedClasses, "nested recursive package scan is not allowed");
        } finally {
            deleteTree(root);
        }

        System.out.println("QinCliSiblingWorkspaceClasspathSmokeTestMain OK");
    }

    private static void requireContains(List<String> entries, Path path, String label) {
        String value = path.toAbsolutePath().normalize().toString();
        if (!entries.contains(value)) {
            throw new IllegalStateException("Expected " + label + ": " + value + " in " + entries);
        }
    }

    private static void requireNotContains(List<String> entries, Path path, String label) {
        String value = path.toAbsolutePath().normalize().toString();
        if (entries.contains(value)) {
            throw new IllegalStateException("Expected " + label + " to be excluded: " + value);
        }
    }

    private static void deleteTree(Path root) throws Exception {
        if (!Files.exists(root)) {
            return;
        }
        try (var stream = Files.walk(root)) {
            for (Path path : stream.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }
}
