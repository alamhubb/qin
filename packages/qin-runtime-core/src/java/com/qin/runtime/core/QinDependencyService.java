package com.qin.runtime.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dependency resolution boundary for runtime builds.
 */
public final class QinDependencyService {
    private static final Pattern CLASS_PATH_ENTRY_PATTERN = Pattern.compile("\"((?:\\\\.|[^\"])*)\"");

    public QinResolvedDependencies resolve(Path projectRoot) {
        if (projectRoot == null) {
            return new QinResolvedDependencies(List.of());
        }
        Path cacheFile = projectRoot.toAbsolutePath().normalize().resolve(".qin").resolve("classpath.json");
        if (!Files.isRegularFile(cacheFile)) {
            return new QinResolvedDependencies(List.of());
        }
        try {
            return new QinResolvedDependencies(readClasspathEntries(cacheFile));
        } catch (IOException error) {
            throw new IllegalStateException("Failed to read Qin dependency classpath cache: " + cacheFile, error);
        }
    }

    static List<String> readClasspathEntries(Path cacheFile) throws IOException {
        String json = Files.readString(cacheFile, StandardCharsets.UTF_8);
        int classpathKey = json.indexOf("\"classpath\"");
        if (classpathKey < 0) {
            return List.of();
        }
        int arrayStart = json.indexOf('[', classpathKey);
        if (arrayStart < 0) {
            return List.of();
        }
        int arrayEnd = findMatchingBracket(json, arrayStart);
        if (arrayEnd < 0) {
            return List.of();
        }
        String array = json.substring(arrayStart + 1, arrayEnd);
        List<String> entries = new ArrayList<>();
        Matcher matcher = CLASS_PATH_ENTRY_PATTERN.matcher(array);
        while (matcher.find()) {
            String entry = matcher.group(1)
                    .replace("\\\\", "\\")
                    .replace("\\\"", "\"");
            if (!entry.isBlank()) {
                entries.add(entry);
            }
        }
        return entries;
    }

    private static int findMatchingBracket(String text, int openBracket) {
        int depth = 0;
        boolean quoted = false;
        boolean escaped = false;
        for (int i = openBracket; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (quoted) {
                if (escaped) {
                    escaped = false;
                    continue;
                }
                if (ch == '\\') {
                    escaped = true;
                    continue;
                }
                if (ch == '"') {
                    quoted = false;
                }
                continue;
            }
            if (ch == '"') {
                quoted = true;
                continue;
            }
            if (ch == '[') {
                depth++;
            } else if (ch == ']') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
}
