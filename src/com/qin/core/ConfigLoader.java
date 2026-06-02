package com.qin.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    private QinConfig parseJavaScriptConfig(String content) {
        String source = content
                .replaceFirst("(?s)^\\s*(?:import\\s+[^\\n]+\\n\\s*)*", "")
                .replaceFirst("(?s)^\\s*export\\s+default\\s+", "")
                .trim();
        if (source.endsWith(";")) {
            source = source.substring(0, source.length() - 1).trim();
        }
        if (source.startsWith("{") && source.endsWith("}") && isJsonLike(source)) {
            return gson.fromJson(source, QinConfig.class);
        }
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
                null,
                null,
                javaConfigField(source),
                null,
                frontendConfigField(source),
                stringMapField(source, "scripts"),
                null);
    }

    private boolean isJsonLike(String source) {
        return source.contains("\"name\"") || source.contains("\"dependencies\"") || source.contains("\"frontend\"");
    }

    private String stringField(String source, String key, String fallback) {
        Matcher matcher = Pattern.compile("(?:\"%s\"|%s)\\s*:\\s*[\"']([^\"']*)[\"']".formatted(key, key))
                .matcher(source);
        return matcher.find() ? matcher.group(1) : fallback;
    }

    private int intField(String source, String key, int fallback) {
        Matcher matcher = Pattern.compile("(?:\"%s\"|%s)\\s*:\\s*(\\d+)".formatted(key, key))
                .matcher(source);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : fallback;
    }

    private Map<String, String> stringMapField(String source, String key) {
        String block = objectBlock(source, key);
        if (block == null) {
            return Map.of();
        }
        Map<String, String> out = new LinkedHashMap<>();
        Matcher matcher = Pattern.compile("[\"']([^\"']+)[\"']\\s*:\\s*[\"']([^\"']*)[\"']")
                .matcher(block);
        while (matcher.find()) {
            out.put(matcher.group(1), matcher.group(2));
        }
        return out;
    }

    private JavaConfig javaConfigField(String source) {
        String block = objectBlock(source, "java");
        if (block == null) {
            return null;
        }
        return new JavaConfig(
                stringField(block, "version", null),
                stringField(block, "release", null),
                stringField(block, "source", null),
                stringField(block, "target", null),
                stringField(block, "sourceDir", null),
                stringField(block, "testDir", null),
                stringField(block, "outputDir", null),
                stringField(block, "encoding", null));
    }

    private FrontendConfig frontendConfigField(String source) {
        String block = objectBlock(source, "frontend");
        if (block == null) {
            return null;
        }
        return new FrontendConfig(
                stringField(block, "srcDir", null),
                stringField(block, "outDir", null),
                intField(block, "devPort", 0));
    }

    private String objectBlock(String source, String key) {
        Matcher matcher = Pattern.compile("(?:\"%s\"|%s)\\s*:\\s*\\{".formatted(key, key)).matcher(source);
        if (!matcher.find()) {
            return null;
        }
        int start = matcher.end();
        int depth = 1;
        for (int index = start; index < source.length(); index++) {
            char ch = source.charAt(index);
            if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0) {
                    return source.substring(start, index);
                }
            }
        }
        return null;
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
