package com.qin.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qin.constants.QinConstants;
import com.qin.types.JavaConfig;
import com.qin.types.OutputConfig;
import com.qin.types.ParsedEntry;
import com.qin.types.QinConfig;
import com.qin.types.ValidationResult;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration loader for Qin.
 */
public class ConfigLoader {
    private final String cwd;
    private final Gson gson;

    public ConfigLoader() {
        this(QinConstants.getCwd());
    }

    public ConfigLoader(String cwd) {
        this.cwd = cwd;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Load configuration from qin.config.json.
     */
    public QinConfig load() throws IOException {
        Path jsonConfig = Paths.get(cwd, QinConstants.CONFIG_FILE);
        if (Files.exists(jsonConfig)) {
            String content = Files.readString(jsonConfig);
            QinConfig config = gson.fromJson(content, QinConfig.class);
            return applyDefaults(config);
        }

        PluginDetector detector = new PluginDetector(cwd);
        DetectionResult detection = detector.detect();
        if (detection.getLanguages().isEmpty() && detection.getFeatures().isEmpty()) {
            throw new IOException("No project detected. Create " + QinConstants.DEFAULT_ENTRY + " or create qin.config.json");
        }

        return applyDefaults(new QinConfig(detectProjectName(), "1.0.0"));
    }

    private String detectProjectName() {
        return Paths.get(cwd).getFileName().toString();
    }

    /**
     * Auto-detect entry file.
     */
    public String findEntry() {
        for (String candidate : QinConstants.DEFAULT_ENTRY_CANDIDATES) {
            if (Files.exists(Paths.get(cwd, candidate))) {
                return candidate;
            }
        }

        for (String candidate : QinConstants.DEFAULT_QIN_ENTRY_CANDIDATES) {
            if (Files.exists(Paths.get(cwd, candidate))) {
                return candidate;
            }
        }

        Path srcDir = Paths.get(cwd, "src");
        if (Files.exists(srcDir) && Files.isDirectory(srcDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(srcDir)) {
                for (Path entry : stream) {
                    if (Files.isDirectory(entry)) {
                        Path mainJava = entry.resolve("Main.java");
                        if (Files.exists(mainJava)) {
                            return "src/" + entry.getFileName() + "/Main.java";
                        }
                    }
                }
            } catch (IOException ignored) {
                // ignore
            }
        }

        return null;
    }

    /**
     * Validate configuration.
     */
    public ValidationResult validate(QinConfig config) {
        List<String> errors = new ArrayList<>();

        if (config == null) {
            errors.add("Configuration is empty");
            return ValidationResult.failure(errors);
        }

        if (config.entry() != null &&
                !config.entry().endsWith(".java") &&
                !config.entry().endsWith(".qin")) {
            errors.add("'entry' must be a .java or .qin file");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    /**
     * Apply default values and return a new immutable config instance.
     */
    private QinConfig applyDefaults(QinConfig config) {
        if (config.entry() != null && config.output() != null && config.java() != null) {
            return config;
        }

        String entry = config.entry() != null ? config.entry() : findEntry();
        OutputConfig output = config.output() != null ? config.output() : new OutputConfig();
        JavaConfig java = config.java() != null ? config.java() : new JavaConfig(QinConstants.DEFAULT_JAVA_VERSION);

        return new QinConfig(
                config.name(),
                config.version(),
                config.description(),
                config.scope(),
                config.port(),
                config.localRep(),
                config.client(),
                config.plugins(),
                entry,
                config.dependencies(),
                config.devDependencies(),
                config.packages(),
                output,
                java,
                config.graalvm(),
                config.frontend(),
                config.scripts(),
                config.repositories());
    }

    /**
     * Parse entry path to source directory and class name.
     */
    public ParsedEntry parseEntry(String entry) {
        if (entry == null) {
            String detectedEntry = findEntry();
            String fallbackEntry = detectedEntry != null ? detectedEntry : QinConstants.DEFAULT_ENTRY;
            return parseEntry(fallbackEntry);
        }

        String normalized = entry.replace("\\", "/");
        int lastSlash = normalized.lastIndexOf('/');

        String fileName = lastSlash >= 0 ? normalized.substring(lastSlash + 1) : normalized;
        String srcDir = lastSlash >= 0 ? normalized.substring(0, lastSlash) : ".";

        String simpleClassName;
        if (fileName.endsWith(".java")) {
            simpleClassName = fileName.substring(0, fileName.length() - ".java".length());
        } else if (fileName.endsWith(".qin")) {
            simpleClassName = fileName.substring(0, fileName.length() - ".qin".length());
        } else {
            simpleClassName = fileName;
        }

        String className = simpleClassName;
        try {
            Path filePath = Paths.get(cwd, entry);
            if (Files.exists(filePath) && fileName.endsWith(".java")) {
                String content = Files.readString(filePath);
                Pattern pattern = Pattern.compile("^\\s*package\\s+([\\w.]+)\\s*;", Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    className = matcher.group(1) + "." + simpleClassName;
                }
            }
        } catch (IOException ignored) {
            // use simple class name
        }

        return new ParsedEntry(srcDir, className, entry);
    }
}