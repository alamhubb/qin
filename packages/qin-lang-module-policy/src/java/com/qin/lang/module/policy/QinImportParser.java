package com.qin.lang.module.policy;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts import declarations for policy checks.
 * This parser is intentionally lightweight but supports multiline ESM imports.
 */
public final class QinImportParser {
    private static final Pattern IMPORT_FROM_PATTERN = Pattern.compile(
            "^\\s*import\\s+(?!type\\b)[\\s\\S]*?\\s+from\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern IMPORT_SIDE_EFFECT_PATTERN = Pattern.compile(
            "^\\s*import\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_FROM_NAMED_PATTERN = Pattern.compile(
            "^\\s*export\\s+(?!type\\b)\\{[\\s\\S]*?}\\s*from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern EXPORT_FROM_ALL_PATTERN = Pattern.compile(
            "^\\s*export\\s+(?!type\\b)\\*\\s*(?:as\\s+[A-Za-z_$][\\w$]*\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern TYPE_ONLY_IMPORT_PATTERN = Pattern.compile(
            "^\\s*import\\s+type\\b[\\s\\S]*?\\s+from\\s+[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern TYPE_ONLY_NAMED_EXPORT_PATTERN = Pattern.compile(
            "^\\s*export\\s+type\\s*\\{[\\s\\S]*?}\\s*from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);
    private static final Pattern TYPE_ONLY_EXPORT_ALL_PATTERN = Pattern.compile(
            "^\\s*export\\s+type\\s*\\*\\s*(?:as\\s+[A-Za-z_$][\\w$]*\\s*)?from\\s*[\"']([^\"']+)[\"']\\s*;?\\s*$",
            Pattern.MULTILINE);

    public List<QinImportDescriptor> parse(Path sourceFile, String source) {
        List<QinImportDescriptor> descriptors = new ArrayList<>();
        if (source == null || source.isEmpty()) {
            return descriptors;
        }

        boolean[] code = codeMask(source);
        collect(descriptors, sourceFile, source, code, TYPE_ONLY_IMPORT_PATTERN, true);
        collect(descriptors, sourceFile, source, code, TYPE_ONLY_NAMED_EXPORT_PATTERN, true);
        collect(descriptors, sourceFile, source, code, TYPE_ONLY_EXPORT_ALL_PATTERN, true);
        collect(descriptors, sourceFile, source, code, IMPORT_FROM_PATTERN, false);
        collect(descriptors, sourceFile, source, code, IMPORT_SIDE_EFFECT_PATTERN, false);
        collect(descriptors, sourceFile, source, code, EXPORT_FROM_NAMED_PATTERN, false);
        collect(descriptors, sourceFile, source, code, EXPORT_FROM_ALL_PATTERN, false);
        return descriptors;
    }

    private void collect(
            List<QinImportDescriptor> out,
            Path sourceFile,
            String source,
            boolean[] code,
            Pattern pattern,
            boolean typeOnly) {
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            String specifier = matcher.group(1);
            int[] lineCol = lineCol(source, matcher.start(1));
            out.add(new QinImportDescriptor(
                    sourceFile.toAbsolutePath().normalize(),
                    specifier,
                    QinImportKind.fromSpecifier(specifier),
                    typeOnly,
                    lineCol[0],
                    lineCol[1]));
        }
    }

    private boolean[] codeMask(String source) {
        boolean[] code = new boolean[source.length()];
        boolean single = false;
        boolean dbl = false;
        boolean template = false;
        int templateExpressionDepth = 0;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            char previous = i > 0 ? source.charAt(i - 1) : '\0';

            if (lineComment) {
                if (ch == '\n') {
                    lineComment = false;
                    code[i] = true;
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
            if (single) {
                if (ch == '\'' && previous != '\\') {
                    single = false;
                }
                continue;
            }
            if (dbl) {
                if (ch == '"' && previous != '\\') {
                    dbl = false;
                }
                continue;
            }
            if (template) {
                if (ch == '$' && next == '{' && previous != '\\') {
                    code[i] = true;
                    code[i + 1] = true;
                    templateExpressionDepth = 1;
                    template = false;
                    i++;
                    continue;
                }
                if (ch == '`' && previous != '\\') {
                    template = false;
                }
                continue;
            }

            if (ch == '/' && next == '/') {
                lineComment = true;
                i++;
            } else if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
            } else if (ch == '/' && startsRegexLiteral(source, i)) {
                i = skipRegexLiteral(source, i);
            } else if (ch == '\'') {
                single = true;
            } else if (ch == '"') {
                dbl = true;
            } else if (ch == '`') {
                template = true;
            } else {
                code[i] = true;
                if (templateExpressionDepth > 0) {
                    if (ch == '{') {
                        templateExpressionDepth++;
                    } else if (ch == '}') {
                        templateExpressionDepth--;
                        if (templateExpressionDepth == 0) {
                            template = true;
                        }
                    }
                }
            }
        }
        return code;
    }

    private boolean isCodePosition(boolean[] code, int index) {
        return index >= 0 && index < code.length && code[index];
    }

    private boolean startsRegexLiteral(String source, int slashIndex) {
        int previous = slashIndex - 1;
        while (previous >= 0 && Character.isWhitespace(source.charAt(previous))) {
            previous--;
        }
        if (previous < 0) {
            return true;
        }
        char ch = source.charAt(previous);
        return "([{:;,=!?&|+-*~^<>%".indexOf(ch) >= 0;
    }

    private int skipRegexLiteral(String source, int slashIndex) {
        boolean inClass = false;
        for (int i = slashIndex + 1; i < source.length(); i++) {
            char ch = source.charAt(i);
            char previous = i > 0 ? source.charAt(i - 1) : '\0';
            if (ch == '\n' || ch == '\r') {
                return i - 1;
            }
            if (ch == '[' && previous != '\\') {
                inClass = true;
            } else if (ch == ']' && previous != '\\') {
                inClass = false;
            } else if (ch == '/' && previous != '\\' && !inClass) {
                while (i + 1 < source.length() && Character.isLetter(source.charAt(i + 1))) {
                    i++;
                }
                return i;
            }
        }
        return slashIndex;
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
