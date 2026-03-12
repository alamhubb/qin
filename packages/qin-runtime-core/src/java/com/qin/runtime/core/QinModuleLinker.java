package com.qin.runtime.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves local .qin ESM imports by linking modules into one source string.
 *
 * Current scope:
 * - local named imports from relative .qin files
 * - strips local import lines from final source
 * - rewrites `export const` to `const`
 * - preserves `java:` imports for frontend adapter handling
 */
public final class QinModuleLinker {
    private static final Pattern LOCAL_IMPORT_PATTERN = Pattern.compile(
            "^\\s*import\\s*\\{[^}]*}\\s*from\\s*\"([^\"]+\\.qin)\"\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_CONST_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+const\\s+");

    public QinLinkedSource link(Path entryFile) throws IOException {
        Path normalizedEntry = entryFile.toAbsolutePath().normalize();
        StringBuilder output = new StringBuilder();
        LinkedHashSet<Path> orderedModules = new LinkedHashSet<>();
        linkRecursive(normalizedEntry, orderedModules, new HashSet<>(), output);
        return new QinLinkedSource(normalizedEntry, output.toString(), new ArrayList<>(orderedModules));
    }

    private void linkRecursive(
            Path currentFile,
            LinkedHashSet<Path> orderedModules,
            Set<Path> visiting,
            StringBuilder output) throws IOException {
        if (!visiting.add(currentFile)) {
            throw new IllegalArgumentException("Circular .qin module import detected at: " + currentFile.toAbsolutePath());
        }
        if (orderedModules.contains(currentFile)) {
            visiting.remove(currentFile);
            return;
        }

        String source = Files.readString(currentFile, StandardCharsets.UTF_8);
        List<Path> localImports = extractLocalImports(currentFile, source);
        for (Path imported : localImports) {
            linkRecursive(imported, orderedModules, visiting, output);
        }

        String stripped = stripLocalImports(source);
        String linkedSource = rewriteExports(stripped).trim();
        if (!linkedSource.isEmpty()) {
            if (output.length() > 0) {
                output.append(System.lineSeparator()).append(System.lineSeparator());
            }
            output.append("// module: ").append(currentFile.toAbsolutePath()).append(System.lineSeparator());
            output.append(linkedSource);
        }

        orderedModules.add(currentFile);
        visiting.remove(currentFile);
    }

    private List<Path> extractLocalImports(Path currentFile, String source) {
        List<Path> imports = new ArrayList<>();
        Matcher matcher = LOCAL_IMPORT_PATTERN.matcher(source);
        while (matcher.find()) {
            String spec = matcher.group(1).trim();
            if (spec.startsWith("./") || spec.startsWith("../")) {
                Path resolved = currentFile.getParent().resolve(spec).normalize();
                requireFile(resolved, currentFile, spec);
                imports.add(resolved);
            }
        }
        return imports;
    }

    private String stripLocalImports(String source) {
        Matcher matcher = LOCAL_IMPORT_PATTERN.matcher(source);
        return matcher.replaceAll("");
    }

    private String rewriteExports(String source) {
        return EXPORT_CONST_PATTERN.matcher(source).replaceAll("const ");
    }

    private void requireFile(Path resolved, Path importer, String importSpec) {
        if (!Files.exists(resolved) || !Files.isRegularFile(resolved)) {
            throw new IllegalArgumentException(
                    "Cannot resolve local .qin import \"" + importSpec + "\" from " + importer.toAbsolutePath());
        }
    }
}
