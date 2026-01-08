package com.qin.java;

import com.qin.constants.QinConstants;
import com.qin.types.QinConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Qin Package Manager - manages dependencies
 */
public class PackageManager {
    private final String projectRoot;
    private final String libDir;
    private final Gson gson;
    private PackageJson pkg;

    public PackageManager() {
        this(QinConstants.getCwd());
    }

    public PackageManager(String projectRoot) {
        this.projectRoot = projectRoot;
        this.libDir = Paths.get(projectRoot, "lib").toString();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.pkg = loadOrCreatePackageJson();
    }

    private PackageJson loadOrCreatePackageJson() {
        Path pkgPath = Paths.get(projectRoot, QinConstants.PACKAGE_JSON);
        if (Files.exists(pkgPath)) {
            try {
                String content = Files.readString(pkgPath);
                return gson.fromJson(content, PackageJson.class);
            } catch (IOException e) {
                System.err.println("Warning: Could not load package.json");
            }
        }

        // Create default
        PackageJson pkg = new PackageJson();
        pkg.name = "unnamed";
        pkg.version = "1.0.0";
        pkg.dependencies = new HashMap<>();
        pkg.devDependencies = new HashMap<>();
        return pkg;
    }

    public PackageJson getPackageJson() {
        return pkg;
    }

    public void savePackageJson() throws IOException {
        Path pkgPath = Paths.get(projectRoot, QinConstants.PACKAGE_JSON);
        Files.writeString(pkgPath, gson.toJson(pkg));
    }

    /**
     * Add a dependency
     */
    public boolean add(String dep, boolean isDev) {
        DependencyInfo info = parseDependency(dep);
        if (info == null) {
            System.err.println("Invalid dependency format. Use: name@version");
            return false;
        }

        Map<String, String> deps = isDev ? pkg.devDependencies : pkg.dependencies;
        if (deps == null) {
            deps = new HashMap<>();
            if (isDev) {
                pkg.devDependencies = deps;
            } else {
                pkg.dependencies = deps;
            }
        }

        String existing = deps.get(info.name);
        if (existing != null) {
            System.out.println("✓ Updated " + info.name + " from " + existing + " to " + info.version);
        } else {
            System.out.println("✓ Added " + info.name + "@" + info.version);
        }

        deps.put(info.name, info.version);

        try {
            savePackageJson();
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save package.json: " + e.getMessage());
            return false;
        }
    }

    /**
     * List dependencies
     */
    public void list() {
        System.out.println("\n" + pkg.name + "@" + pkg.version + "\n");

        if ((pkg.dependencies == null || pkg.dependencies.isEmpty()) &&
                (pkg.devDependencies == null || pkg.devDependencies.isEmpty())) {
            System.out.println("No dependencies");
            return;
        }

        if (pkg.dependencies != null && !pkg.dependencies.isEmpty()) {
            System.out.println("Dependencies:");
            for (Map.Entry<String, String> entry : pkg.dependencies.entrySet()) {
                System.out.println("  " + entry.getKey() + "@" + entry.getValue());
            }
        }

        if (pkg.devDependencies != null && !pkg.devDependencies.isEmpty()) {
            System.out.println("\nDev Dependencies:");
            for (Map.Entry<String, String> entry : pkg.devDependencies.entrySet()) {
                System.out.println("  " + entry.getKey() + "@" + entry.getValue());
            }
        }
    }

    /**
     * Get classpath from lib directory
     */
    public List<String> getClasspath() {
        List<String> jars = new ArrayList<>();
        Path libPath = Paths.get(libDir);

        if (!Files.exists(libPath)) {
            return jars;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(libPath, "*.jar")) {
            for (Path entry : stream) {
                jars.add(entry.toString());
            }
        } catch (IOException e) {
            // Ignore
        }

        return jars;
    }

    /**
     * Parse dependency string: name@version or just name
     */
    public static DependencyInfo parseDependency(String dep) {
        if (dep == null || dep.trim().isEmpty()) {
            return null;
        }

        String trimmed = dep.trim();
        int atIndex = trimmed.lastIndexOf("@");

        String name;
        String version;

        if (atIndex > 0) {
            name = trimmed.substring(0, atIndex).trim();
            version = trimmed.substring(atIndex + 1).trim();
        } else {
            name = trimmed;
            version = "latest";
        }

        if (name.isEmpty() || version.isEmpty()) {
            return null;
        }

        return new DependencyInfo(name, version);
    }

    public static class DependencyInfo {
        public final String name;
        public final String version;

        public DependencyInfo(String name, String version) {
            this.name = name;
            this.version = version;
        }
    }

    public static class PackageJson {
        public String name;
        public String version;
        public String description;
        public String main;
        public Map<String, String> scripts;
        public Map<String, String> dependencies;
        public Map<String, String> devDependencies;
    }
}
