package com.qin.runtime.core;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SpringBoot-style Qin project entrypoint.
 */
public final class QinApplication {
    private static final String CONFIG_FILE = "qin.config.js";

    private QinApplication() {
    }

    public static void run(Class<?> source, String[] args) throws Exception {
        QinFullstackMain.main(resolveFullstackArgs(source, args, Path.of("").toAbsolutePath().normalize())
                .toArray(String[]::new));
    }

    public static void dev(Class<?> source, String[] args) throws Exception {
        List<String> adjusted = new ArrayList<>();
        if (!hasArg(args, "--dev")) {
            adjusted.add("--dev");
        }
        adjusted.addAll(List.of(args == null ? new String[0] : args));
        run(source, adjusted.toArray(String[]::new));
    }

    static List<String> resolveFullstackArgs(Class<?> source, String[] args, Path workingDirectory) throws Exception {
        String[] safeArgs = args == null ? new String[0] : args;
        if (hasArg(safeArgs, "--help")) {
            return new ArrayList<>(List.of(safeArgs));
        }

        Path root = explicitRoot(safeArgs)
                .or(() -> findProjectRoot(workingDirectory))
                .or(() -> findProjectRoot(source))
                .orElse(workingDirectory.toAbsolutePath().normalize());
        ProjectConfig config = ProjectConfig.read(root);

        List<String> resolved = new ArrayList<>(List.of(safeArgs));
        appendOptionIfMissing(resolved, "--root", root.toString());
        if (config.port() != null) {
            appendOptionIfMissing(resolved, "--port", String.valueOf(config.port()));
        }
        if (config.backendEntry() != null) {
            appendPathOptionIfMissing(resolved, "--backend-file", root, config.backendEntry());
        }
        if (config.frontendEntry() != null) {
            appendPathOptionIfMissing(resolved, "--frontend-file", root, config.frontendEntry());
        }
        if (config.staticDir() != null) {
            appendPathOptionIfMissing(resolved, "--static-dir", root, config.staticDir());
        }
        return resolved;
    }

    private static Optional<Path> explicitRoot(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("--root".equals(args[i])) {
                if (i + 1 >= args.length) {
                    throw new IllegalArgumentException("Missing value for --root");
                }
                return Optional.of(Path.of(args[i + 1]).toAbsolutePath().normalize());
            }
        }
        return Optional.empty();
    }

    private static Optional<Path> findProjectRoot(Path start) {
        if (start == null) {
            return Optional.empty();
        }
        Path current = Files.isRegularFile(start) ? start.getParent() : start;
        current = current.toAbsolutePath().normalize();
        while (current != null) {
            if (Files.isRegularFile(current.resolve(CONFIG_FILE))) {
                return Optional.of(current);
            }
            current = current.getParent();
        }
        return Optional.empty();
    }

    private static Optional<Path> findProjectRoot(Class<?> source) {
        if (source == null || source.getProtectionDomain() == null
                || source.getProtectionDomain().getCodeSource() == null) {
            return Optional.empty();
        }
        try {
            URL location = source.getProtectionDomain().getCodeSource().getLocation();
            Path path = Path.of(location.toURI());
            return findProjectRoot(path);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    private static void appendPathOptionIfMissing(List<String> args, String option, Path root, String value) {
        Path path = Path.of(value);
        if (!path.isAbsolute()) {
            path = root.resolve(path);
        }
        appendOptionIfMissing(args, option, path.toAbsolutePath().normalize().toString());
    }

    private static void appendOptionIfMissing(List<String> args, String option, String value) {
        if (hasArg(args.toArray(String[]::new), option)) {
            return;
        }
        args.add(option);
        args.add(value);
    }

    private static boolean hasArg(String[] args, String option) {
        if (args == null) {
            return false;
        }
        for (String arg : args) {
            if (option.equals(arg)) {
                return true;
            }
        }
        return false;
    }

    private record ProjectConfig(Integer port, String backendEntry, String frontendEntry, String staticDir) {
        private static ProjectConfig read(Path root) throws IOException {
            Path config = root.resolve(CONFIG_FILE);
            if (!Files.isRegularFile(config)) {
                return new ProjectConfig(null, null, null, null);
            }
            String source = Files.readString(config, StandardCharsets.UTF_8);
            String backend = extractObjectBody(source, "backend");
            String frontend = extractObjectBody(source, "frontend");
            return new ProjectConfig(
                    extractIntField(source, "port"),
                    extractStringField(backend, "entry"),
                    extractStringField(frontend, "entry"),
                    extractStringField(frontend, "staticDir"));
        }
    }

    private static Integer extractIntField(String source, String field) {
        if (source == null) {
            return null;
        }
        Matcher matcher = fieldPattern(field, "([0-9]+)").matcher(source);
        if (!matcher.find()) {
            return null;
        }
        return Integer.parseInt(matcher.group(1));
    }

    private static String extractStringField(String source, String field) {
        if (source == null) {
            return null;
        }
        Matcher matcher = fieldPattern(field, "\"((?:\\\\.|[^\"])*)\"|'((?:\\\\.|[^'])*)'").matcher(source);
        if (!matcher.find()) {
            return null;
        }
        String value = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
        return unescapeString(value);
    }

    private static Pattern fieldPattern(String field, String valuePattern) {
        return Pattern.compile("(?m)(?:^|[,{\\s])(?:\"" + Pattern.quote(field) + "\"|" + Pattern.quote(field)
                + ")\\s*:\\s*" + valuePattern);
    }

    private static String extractObjectBody(String source, String field) {
        if (source == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("(?m)(?:^|[,{\\s])(?:\"" + Pattern.quote(field) + "\"|" + Pattern.quote(field)
                + ")\\s*:\\s*\\{");
        Matcher matcher = pattern.matcher(source);
        if (!matcher.find()) {
            return null;
        }
        int openBrace = source.indexOf('{', matcher.end() - 1);
        int closeBrace = matchingBrace(source, openBrace);
        if (openBrace < 0 || closeBrace <= openBrace) {
            throw new IllegalArgumentException("Malformed object in " + CONFIG_FILE + " field: " + field);
        }
        return source.substring(openBrace + 1, closeBrace);
    }

    private static int matchingBrace(String source, int openBrace) {
        int depth = 0;
        char quote = 0;
        boolean escaped = false;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = openBrace; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : 0;
            if (lineComment) {
                if (ch == '\n' || ch == '\r') {
                    lineComment = false;
                }
                continue;
            }
            if (blockComment) {
                if (ch == '*' && next == '/') {
                    blockComment = false;
                    i++;
                }
                continue;
            }
            if (quote != 0) {
                if (escaped) {
                    escaped = false;
                } else if (ch == '\\') {
                    escaped = true;
                } else if (ch == quote) {
                    quote = 0;
                }
                continue;
            }
            if (ch == '/' && next == '/') {
                lineComment = true;
                i++;
                continue;
            }
            if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
                continue;
            }
            if (ch == '"' || ch == '\'' || ch == '`') {
                quote = ch;
                continue;
            }
            if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static String unescapeString(String value) {
        return value
                .replace("\\\"", "\"")
                .replace("\\'", "'")
                .replace("\\\\", "\\");
    }
}
