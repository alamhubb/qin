package com.qin.lang.module.policy;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts import declarations for policy checks.
 * This parser is intentionally lightweight and line-oriented.
 */
public final class QinImportParser {
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "^\\s*import\\s+[^;\\n]*?\\s+from\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);

    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "^\\s*import\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_FROM_NAMED_PATTERN = Pattern.compile(
            "^\\s*export\\s*\\{[^}\\n]*}\\s*from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_FROM_ALL_PATTERN = Pattern.compile(
            "^\\s*export\\s*\\*\\s*(?:as\\s+[A-Za-z_$][\\w$]*\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);

    public List<QinImportDescriptor> parse(Path sourceFile, String source) {
        List<QinImportDescriptor> descriptors = new ArrayList<>();
        if (source == null || source.isEmpty()) {
            return descriptors;
        }

        collect(descriptors, sourceFile, source, IMPORT_FROM_PATTERN);
        collect(descriptors, sourceFile, source, IMPORT_SIDE_EFFECT_PATTERN);
        collect(descriptors, sourceFile, source, EXPORT_FROM_NAMED_PATTERN);
        collect(descriptors, sourceFile, source, EXPORT_FROM_ALL_PATTERN);
        return descriptors;
    }

    private void collect(
            List<QinImportDescriptor> out,
            Path sourceFile,
            String source,
            Pattern pattern) {
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            String specifier = matcher.group(1);
            int[] lineCol = lineCol(source, matcher.start(1));
            out.add(new QinImportDescriptor(
                    sourceFile.toAbsolutePath().normalize(),
                    specifier,
                    QinImportKind.fromSpecifier(specifier),
                    lineCol[0],
                    lineCol[1]));
        }
    }

    private int[] lineCol(String source, int index) {
        int line = 1;
        int col = 1;
        for (int i = 0; i < index && i < source.length(); i++) {
            char ch = source.charAt(i);
            if (ch == '\n') {
                line++;
                col = 1;
            } else {
                col++;
            }
        }
        return new int[] {line, col};
    }
}
