package com.qin.runtime.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

final class QinFrontendTransformDiskCache {
    private static final String CACHE_VERSION = "v1";

    private QinFrontendTransformDiskCache() {
    }

    static Optional<Map<String, String>> read(Path projectRoot, String namespace, String keyMaterial) {
        Path file = cacheFile(projectRoot, namespace, keyMaterial);
        if (!Files.isRegularFile(file)) {
            return Optional.empty();
        }
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(file)) {
            properties.load(input);
        } catch (IOException error) {
            return Optional.empty();
        }
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        for (String name : properties.stringPropertyNames()) {
            if (!name.startsWith("value.")) {
                continue;
            }
            String key = name.substring("value.".length());
            values.put(key, decode(properties.getProperty(name)));
        }
        return Optional.of(values);
    }

    static void write(Path projectRoot, String namespace, String keyMaterial, Map<String, String> values) {
        Path file = cacheFile(projectRoot, namespace, keyMaterial);
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            properties.setProperty("value." + entry.getKey(), encode(entry.getValue()));
        }
        try {
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                properties.store(output, "Qin frontend transform cache");
            }
        } catch (IOException error) {
            System.err.println("[WARN] failed to write Qin frontend transform cache: " + error.getMessage());
        }
    }

    static String keyMaterial(Path projectRoot, String source, String configSource) {
        return CACHE_VERSION
                + "\nroot=" + projectRoot.toAbsolutePath().normalize()
                + "\nconfig=" + (configSource == null ? "" : configSource)
                + "\nsource=" + (source == null ? "" : source);
    }

    private static Path cacheFile(Path projectRoot, String namespace, String keyMaterial) {
        return projectRoot.toAbsolutePath().normalize()
                .resolve(".qin")
                .resolve("cache")
                .resolve("frontend-transform")
                .resolve(namespace)
                .resolve(sha256(keyMaterial) + ".properties");
    }

    private static String encode(String text) {
        return Base64.getEncoder().encodeToString((text == null ? "" : text).getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        return new String(Base64.getDecoder().decode(text), StandardCharsets.UTF_8);
    }

    private static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((text == null ? "" : text).getBytes(StandardCharsets.UTF_8));
            StringBuilder out = new StringBuilder(hash.length * 2);
            for (byte item : hash) {
                out.append(String.format("%02x", item));
            }
            return out.toString();
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException("SHA-256 is not available", error);
        }
    }
}
