package com.qin.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qin.constants.QinConstants;
import com.qin.core.ConfigLoader;
import com.qin.npm.NpmPackageManager;
import com.qin.types.QinConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal install command that wires existing npm and qin.config.js support
 * back into the CLI.
 */
public final class QinInstallCommand {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private QinInstallCommand() {
    }

    public static void execute(String[] args) throws Exception {
        boolean dev = false;
        Map<String, String> requested = new LinkedHashMap<>();

        for (String arg : args) {
            if ("--dev".equals(arg) || "-D".equals(arg)) {
                dev = true;
                continue;
            }
            DependencySpec spec = parseDependencySpec(arg);
            requested.put(spec.name(), spec.version());
        }

        if (requested.isEmpty()) {
            installDeclaredDependencies();
            return;
        }

        NpmPackageManager npm = new NpmPackageManager(QinConstants.getCwd());
        for (Map.Entry<String, String> entry : requested.entrySet()) {
            String name = entry.getKey();
            String version = entry.getValue();
            if (isNpmDependency(name)) {
                boolean installed = npm.install(name, version);
                if (!installed) {
                    throw new IOException("Failed to install npm dependency: " + name);
                }
            }
            writeDependency(name, version, dev);
            System.out.println("Added dependency: " + name + " -> " + version);
        }

        if (requested.keySet().stream().anyMatch(name -> !isNpmDependency(name))) {
            System.out.println("Maven dependencies were recorded to qin.config.js. Run `qin sync` to resolve them.");
        }
    }

    private static void installDeclaredDependencies() throws Exception {
        QinConfig config = new ConfigLoader().load();
        NpmPackageManager npm = new NpmPackageManager(QinConstants.getCwd());
        int npmCount = 0;
        int mavenCount = 0;

        for (Map.Entry<String, String> entry : config.dependencies().entrySet()) {
            if (isNpmDependency(entry.getKey())) {
                boolean installed = npm.install(entry.getKey(), entry.getValue());
                if (!installed) {
                    throw new IOException("Failed to install npm dependency: " + entry.getKey());
                }
                npmCount++;
            } else {
                mavenCount++;
            }
        }

        for (Map.Entry<String, String> entry : config.devDependencies().entrySet()) {
            if (isNpmDependency(entry.getKey())) {
                boolean installed = npm.install(entry.getKey(), entry.getValue());
                if (!installed) {
                    throw new IOException("Failed to install npm dependency: " + entry.getKey());
                }
                npmCount++;
            } else {
                mavenCount++;
            }
        }

        System.out.println("Installed npm dependencies: " + npmCount);
        if (mavenCount > 0) {
            System.out.println("Maven dependencies remain managed by `qin sync`: " + mavenCount);
        }
    }

    private static void writeDependency(String name, String version, boolean dev) throws IOException {
        Path configPath = Path.of(QinConstants.getCwd(), QinConstants.CONFIG_FILE);
        JsonObject root;
        if (Files.exists(configPath)) {
            root = JsonParser.parseString(Files.readString(configPath)).getAsJsonObject();
        } else {
            root = new JsonObject();
            root.addProperty("name", Path.of(QinConstants.getCwd()).getFileName().toString());
            root.addProperty("version", QinConstants.DEFAULT_VERSION);
        }

        String bucketName = dev ? "devDependencies" : "dependencies";
        JsonObject bucket = root.has(bucketName) && root.get(bucketName).isJsonObject()
                ? root.getAsJsonObject(bucketName)
                : new JsonObject();
        bucket.addProperty(name, version);
        root.add(bucketName, bucket);

        Files.writeString(configPath, GSON.toJson(root));
    }

    private static boolean isNpmDependency(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        if (name.startsWith("@")) {
            return name.contains("/");
        }
        return !name.contains(":") && !name.contains("@");
    }

    private static DependencySpec parseDependencySpec(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Missing dependency name");
        }

        String value = raw.trim();
        if (value.startsWith("@")) {
            int lastAt = value.lastIndexOf('@');
            if (lastAt > 0 && lastAt != value.indexOf('@')) {
                return new DependencySpec(value.substring(0, lastAt), value.substring(lastAt + 1));
            }
            return new DependencySpec(value, "latest");
        }

        int colonCount = value.length() - value.replace(":", "").length();
        if (colonCount >= 2) {
            int lastColon = value.lastIndexOf(':');
            return new DependencySpec(
                    QinConstants.toQinCoordinate(value.substring(0, lastColon)),
                    value.substring(lastColon + 1));
        }

        if (colonCount == 1) {
            return new DependencySpec(QinConstants.toQinCoordinate(value), "latest");
        }

        int lastAt = value.lastIndexOf('@');
        if (lastAt > 0) {
            return new DependencySpec(value.substring(0, lastAt), value.substring(lastAt + 1));
        }

        return new DependencySpec(value, "latest");
    }

    private record DependencySpec(String name, String version) {
    }
}

