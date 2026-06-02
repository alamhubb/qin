package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.FrontendConfig;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration loader for Qin.
 */
public class ConfigLoader {
    private final String cwd;

    public ConfigLoader() {
        this(QinConstants.getCwd());
    }

    public ConfigLoader(String cwd) {
        this.cwd = cwd;
    }

    /**
     * Load configuration from qin.config.js.
     */
    public QinConfig load() throws IOException {
        Path configFile = Paths.get(cwd, QinConstants.CONFIG_FILE);
        if (Files.exists(configFile)) {
            String content = Files.readString(configFile);
            if (content == null || content.isBlank()) {
                throw new IOException("qin.config.js is empty");
            }
            QinConfig config = parseJavaScriptConfig(content);
            if (config == null) {
                throw new IOException("qin.config.js parsed to null");
            }
            return applyDefaults(config);
        }

        PluginDetector detector = new PluginDetector(cwd);
        DetectionResult detection = detector.detect();
        if (detection.getLanguages().isEmpty() && detection.getFeatures().isEmpty()) {
            throw new IOException("No project detected. Create " + QinConstants.DEFAULT_ENTRY + " or create qin.config.js");
        }

        return applyDefaults(new QinConfig(detectProjectName(), "1.0.0"));
    }

    private QinConfig parseJavaScriptConfig(String content) throws IOException {
        Map<String, Object> source = QinConfigJsParser.parseConfigObject(content);
        return new QinConfig(
                stringField(source, "name", detectProjectName()),
                stringField(source, "version", "1.0.0"),
                stringField(source, "description", null),
                null,
                null,
                false,
                null,
                null,
                stringField(source, "entry", null),
                stringMapField(source, "dependencies"),
                stringMapField(source, "devDependencies"),
                stringListField(source, "packages"),
                null,
                javaConfigField(source),
                null,
                frontendConfigField(source),
                stringMapField(source, "scripts"),
                null);
    }

    private String stringField(Map<String, Object> source, String key, String fallback) {
        Object value = source.get(key);
        return value instanceof String text ? text : fallback;
    }

    private int intField(Map<String, Object> source, String key, int fallback) {
        Object value = source.get(key);
        return value instanceof Number number ? number.intValue() : fallback;
    }

    private Map<String, String> stringMapField(Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (!(value instanceof Map<?, ?> map)) {
            return Map.of();
        }
        java.util.LinkedHashMap<String, String> out = new java.util.LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getKey() instanceof String mapKey && entry.getValue() instanceof String mapValue) {
                out.put(mapKey, mapValue);
            }
        }
        return out;
    }

    private List<String> stringListField(Map<String, Object> source, String key) {
        Object value = source.get(key);
        if (!(value instanceof List<?> list)) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof String text) {
                out.add(text);
            }
        }
        return out;
    }

    private JavaConfig javaConfigField(Map<String, Object> source) {
        Object value = source.get("java");
        if (!(value instanceof Map<?, ?> block)) {
            return null;
        }
        Map<String, Object> map = objectMap(block);
        return new JavaConfig(
                stringField(map, "version", null),
                stringField(map, "release", null),
                stringField(map, "source", null),
                stringField(map, "target", null),
                stringField(map, "sourceDir", null),
                stringField(map, "testDir", null),
                stringField(map, "outputDir", null),
                stringField(map, "encoding", null));
    }

    private FrontendConfig frontendConfigField(Map<String, Object> source) {
        Object value = source.get("frontend");
        if (!(value instanceof Map<?, ?> block)) {
            return null;
        }
        Map<String, Object> map = objectMap(block);
        return new FrontendConfig(
                stringField(map, "srcDir", null),
                stringField(map, "outDir", null),
                intField(map, "devPort", 0));
    }

    private Map<String, Object> objectMap(Map<?, ?> source) {
        java.util.LinkedHashMap<String, Object> out = new java.util.LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : source.entrySet()) {
            if (entry.getKey() instanceof String key) {
                out.put(key, entry.getValue());
            }
        }
        return out;
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
                !config.entry().endsWith(".qin") &&
                !config.entry().endsWith(".js") &&
                !config.entry().endsWith(".mjs") &&
                !config.entry().endsWith(".ts")) {
            errors.add("'entry' must be a .java, .qin, .js, .mjs, or .ts file");
        }

        return errors.isEmpty() ? ValidationResult.success() : ValidationResult.failure(errors);
    }

    /**
     * Apply default values and return a new immutable config instance.
     */
    private QinConfig applyDefaults(QinConfig config) {
        if (config == null) {
            config = new QinConfig(detectProjectName(), "1.0.0");
        }
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
