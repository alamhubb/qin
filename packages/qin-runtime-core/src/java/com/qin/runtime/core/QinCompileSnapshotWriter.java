package com.qin.runtime.core;

import com.qin.lang.ir.QinIrProgram;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Writes compile snapshots to .qin/temp for easier inspection.
 */
public final class QinCompileSnapshotWriter {
    private static final DateTimeFormatter HOUR_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHH");
    private static final Pattern ILLEGAL_CHARS = Pattern.compile("[^A-Za-z0-9._-]");
    private static final int SNAPSHOT_JSON_MAX_CHARS = 4 * 1024 * 1024;

    public Path writeSnapshot(
            Path sourceFile,
            String originalSource,
            String parserSource,
            String astText,
            QinIrProgram irProgram,
            QinIrProgram loweredProgram,
            String className,
            byte[] classBytes) throws IOException {
        return writeSnapshot(
                sourceFile,
                originalSource,
                parserSource,
                astText,
                irProgram,
                loweredProgram,
                null,
                className,
                classBytes);
    }

    public Path writeSnapshot(
            Path sourceFile,
            String originalSource,
            String parserSource,
            String astText,
            QinIrProgram irProgram,
            QinIrProgram loweredProgram,
            Object cfaIrProgram,
            String className,
            byte[] classBytes) throws IOException {
        Path source = sourceFile.toAbsolutePath().normalize();
        String sourceBase = source.getFileName().toString();
        int dot = sourceBase.lastIndexOf('.');
        if (dot > 0) {
            sourceBase = sourceBase.substring(0, dot);
        }
        sourceBase = sanitize(sourceBase);

        String hour = LocalDateTime.now().format(HOUR_FORMAT);
        Path projectRoot = findProjectRoot(source.getParent());
        Path tempRoot = projectRoot.resolve(".qin").resolve("temp");
        Files.createDirectories(tempRoot);

        int seq = nextSequence(tempRoot, sourceBase, hour);
        String seqText = String.format("%03d", seq);
        String runId = sourceBase + "-" + hour + "-" + seqText;
        Path runDir = tempRoot.resolve(runId);
        Files.createDirectories(runDir);

        String prefix = sourceBase + "-" + hour + "-" + seqText;
        writeUtf8(runDir.resolve(prefix + "-source.js"), defaultString(originalSource));
        writeUtf8(runDir.resolve(prefix + "-parser-input.js"), defaultString(parserSource));
        writeJsonText(runDir.resolve(prefix + "-ast.json"), defaultString(astText));
        writeJsonObject(runDir.resolve(prefix + "-ir.json"), irProgram);
        writeJsonObject(runDir.resolve(prefix + "-lowerer.json"), loweredProgram);
        if (cfaIrProgram != null) {
            writeJsonObject(runDir.resolve(prefix + "-cfa-ir.json"), cfaIrProgram);
        }
        writeJsonObject(
                runDir.resolve(prefix + "-class-name.json"),
                Map.of("className", className == null ? "" : className));

        if (classBytes != null && classBytes.length > 0) {
            String classFileName = prefix + "-" + simpleClassName(className) + ".class";
            Files.write(runDir.resolve(classFileName), classBytes);
        }
        return runDir;
    }

    private Path findProjectRoot(Path start) {
        Path current = start == null ? Path.of("").toAbsolutePath().normalize() : start;
        while (current != null) {
            if (Files.exists(current.resolve("qin.config.json")) || Files.isDirectory(current.resolve(".qin"))) {
                return current;
            }
            current = current.getParent();
        }
        return Path.of("").toAbsolutePath().normalize();
    }

    private int nextSequence(Path tempRoot, String sourceBase, String hour) throws IOException {
        String escapedBase = Pattern.quote(sourceBase);
        String escapedHour = Pattern.quote(hour);
        Pattern namePattern = Pattern.compile("^" + escapedBase + "-" + escapedHour + "-(\\d{3})$");

        int max = 0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempRoot)) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    continue;
                }
                String name = path.getFileName().toString();
                Matcher matcher = namePattern.matcher(name);
                if (!matcher.matches()) {
                    continue;
                }
                int value = Integer.parseInt(matcher.group(1));
                if (value > max) {
                    max = value;
                }
            }
        }
        return max + 1;
    }

    private void writeUtf8(Path file, String text) throws IOException {
        Files.writeString(file, text, StandardCharsets.UTF_8);
    }

    private void writeJsonText(Path file, String jsonText) throws IOException {
        String normalized = jsonText == null ? "" : jsonText.trim();
        if (looksLikeJson(normalized)) {
            writeUtf8(file, normalized + System.lineSeparator());
            return;
        }
        writeJsonObject(file, Map.of("text", normalized));
    }

    private void writeJsonObject(Path file, Object value) throws IOException {
        writeUtf8(file, QinObjectJsonEncoder.toJson(value, SNAPSHOT_JSON_MAX_CHARS) + System.lineSeparator());
    }

    private boolean looksLikeJson(String text) {
        return (!text.isEmpty())
                && ((text.charAt(0) == '{' && text.charAt(text.length() - 1) == '}')
                || (text.charAt(0) == '[' && text.charAt(text.length() - 1) == ']'));
    }

    private String sanitize(String value) {
        String normalized = ILLEGAL_CHARS.matcher(value).replaceAll("_");
        if (normalized.isBlank()) {
            return "source";
        }
        return normalized;
    }

    private String defaultString(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private String simpleClassName(String className) {
        if (className == null || className.isBlank()) {
            return "Generated";
        }
        int idx = className.lastIndexOf('.');
        if (idx < 0 || idx == className.length() - 1) {
            return sanitize(className);
        }
        return sanitize(className.substring(idx + 1));
    }
}
