package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.QinConfig;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Workspace Loader for Qin
 * Handles monorepo/multi-project configurations
 */
public class WorkspaceLoader {
    private final String cwd;
    private final Map<String, WorkspacePackage> packages = new HashMap<>();
    private String workspaceRoot = null;
    private final Gson gson = new Gson();

    public WorkspaceLoader() {
        this(System.getProperty("user.dir"));
    }

    public WorkspaceLoader(String cwd) {
        this.cwd = cwd;
    }

    /**
     * Find workspace root by looking for qin.config.json with packages field
     */
    public String findWorkspaceRoot() throws IOException {
        Path current = Paths.get(cwd);

        while (current != null) {
            Path configPath = current.resolve(QinConstants.CONFIG_FILE);
            if (Files.exists(configPath)) {
                String content = Files.readString(configPath);
                QinConfig config = gson.fromJson(content, QinConfig.class);
                if (config.packages() != null && !config.packages().isEmpty()) {
                    workspaceRoot = current.toString();
                    return workspaceRoot;
                }
            }
            current = current.getParent();
        }

        return null;
    }

    /**
     * Load workspace packages
     */
    public Map<String, WorkspacePackage> loadPackages(QinConfig projectConfig) throws IOException {
        // Check if current config has packages
        if (projectConfig.packages() != null && !projectConfig.packages().isEmpty()) {
            workspaceRoot = cwd;
            return loadPackagesFromRoot(projectConfig, cwd);
        }

        // Search for workspace root
        String root = findWorkspaceRoot();
        if (root == null) {
            return packages;
        }

        // Load workspace root config
        Path configPath = Paths.get(root, QinConstants.CONFIG_FILE);
        String content = Files.readString(configPath);
        QinConfig rootConfig = gson.fromJson(content, QinConfig.class);

        return loadPackagesFromRoot(rootConfig, root);
    }

    private Map<String, WorkspacePackage> loadPackagesFromRoot(QinConfig rootConfig, String rootDir)
            throws IOException {
        if (rootConfig.packages() == null || rootConfig.packages().isEmpty()) {
            return packages;
        }

        for (String pattern : rootConfig.packages()) {
            // Simple glob matching
            Path basePath = Paths.get(rootDir);

            if (pattern.endsWith("/*")) {
                // Match all subdirectories
                String baseDir = pattern.substring(0, pattern.length() - 2);
                Path searchDir = basePath.resolve(baseDir);

                if (Files.exists(searchDir) && Files.isDirectory(searchDir)) {
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(searchDir)) {
                        for (Path entry : stream) {
                            if (Files.isDirectory(entry)) {
                                loadPackage(entry.toString());
                            }
                        }
                    }
                }
            } else {
                // Direct path
                Path pkgPath = basePath.resolve(pattern);
                if (Files.exists(pkgPath) && Files.isDirectory(pkgPath)) {
                    loadPackage(pkgPath.toString());
                }
            }
        }

        return packages;
    }

    private void loadPackage(String pkgPath) {
        Path configPath = Paths.get(pkgPath, QinConstants.CONFIG_FILE);

        try {
            if (Files.exists(configPath)) {
                String content = Files.readString(configPath);
                QinConfig config = gson.fromJson(content, QinConfig.class);

                if (config.name() != null) {
                    String classesDir = Paths.get(pkgPath, "build", "classes").toString();
                    packages.put(config.name(), new WorkspacePackage(
                            config.name(),
                            pkgPath,
                            config,
                            classesDir));
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Failed to load package at " + pkgPath);
        }
    }

    public boolean isLocalPackage(String name) {
        return packages.containsKey(name);
    }

    public WorkspacePackage getPackage(String name) {
        return packages.get(name);
    }

    public Collection<WorkspacePackage> getAllPackages() {
        return packages.values();
    }

    public String getWorkspaceRoot() {
        return workspaceRoot;
    }
}
