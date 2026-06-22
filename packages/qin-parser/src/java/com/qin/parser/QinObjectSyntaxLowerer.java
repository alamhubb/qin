package com.qin.parser;

/**
 * Lowers Qin-owned object declarations into the existing class pipeline.
 *
 * <p>Qin object declarations intentionally share TypeScript class body syntax,
 * but bind the public declaration name to one eagerly-created singleton:
 *
 * <pre>
 * object Store { value = 1 }
 * </pre>
 *
 * becomes:
 *
 * <pre>
 * class __QinObject_Store { value = 1 }
 * const Store = new __QinObject_Store();
 * </pre>
 */
final class QinObjectSyntaxLowerer {
    private static final String OBJECT_KEYWORD = "object";
    private static final String EXPORT_KEYWORD = "export";
    private static final String DEFAULT_KEYWORD = "default";
    private static final String INTERNAL_PREFIX = "__QinObject_";

    private QinObjectSyntaxLowerer() {
    }

    static String lower(String source) {
        if (source == null || source.indexOf(OBJECT_KEYWORD) < 0) {
            return source;
        }
        StringBuilder out = new StringBuilder(source.length() + 64);
        int cursor = 0;
        int i = 0;
        while (i < source.length()) {
            int skipped = skipNonCode(source, i);
            if (skipped != i) {
                i = skipped;
                continue;
            }
            ObjectDeclaration declaration = tryParseObjectDeclaration(source, i);
            if (declaration == null) {
                i++;
                continue;
            }
            out.append(source, cursor, declaration.declarationStart());
            out.append(rewrite(declaration));
            cursor = declaration.declarationEnd();
            i = cursor;
        }
        if (cursor == 0) {
            return source;
        }
        out.append(source, cursor, source.length());
        return out.toString();
    }

    private static String rewrite(ObjectDeclaration declaration) {
        String internalName = INTERNAL_PREFIX + declaration.name();
        String classTail = declaration.source().substring(declaration.nameEnd(), declaration.declarationEnd());
        StringBuilder out = new StringBuilder(classTail.length() + declaration.name().length() * 3 + 96);
        out.append("class ").append(internalName).append(classTail);
        if (!classTail.endsWith("\n")) {
            out.append('\n');
        }
        if (declaration.defaultExport()) {
            out.append("const ").append(declaration.name()).append(" = new ").append(internalName).append("();\n");
            out.append("export default ").append(declaration.name()).append(';');
            return out.toString();
        }
        if (declaration.namedExport()) {
            out.append("export ");
        }
        out.append("const ").append(declaration.name()).append(" = new ").append(internalName).append("();");
        return out.toString();
    }

    private static ObjectDeclaration tryParseObjectDeclaration(String source, int objectIndex) {
        if (!isWordAt(source, objectIndex, OBJECT_KEYWORD)) {
            return null;
        }
        Prefix prefix = parsePrefix(source, objectIndex);
        if (prefix == null || !isStatementBoundaryBefore(source, prefix.start())) {
            return null;
        }
        int nameStart = skipWhitespace(source, objectIndex + OBJECT_KEYWORD.length());
        if (!isIdentifierStart(charAt(source, nameStart))) {
            return null;
        }
        int nameEnd = nameStart + 1;
        while (nameEnd < source.length() && isIdentifierPart(source.charAt(nameEnd))) {
            nameEnd++;
        }
        int bodyStart = findClassBodyStart(source, nameEnd);
        if (bodyStart < 0) {
            return null;
        }
        int declarationEnd = findMatchingBrace(source, bodyStart);
        if (declarationEnd < 0) {
            return null;
        }
        return new ObjectDeclaration(
                source,
                prefix.start(),
                objectIndex,
                source.substring(nameStart, nameEnd),
                nameEnd,
                declarationEnd + 1,
                prefix.namedExport(),
                prefix.defaultExport());
    }

    private static Prefix parsePrefix(String source, int objectIndex) {
        int cursor = skipWhitespaceBack(source, objectIndex - 1) + 1;
        boolean defaultExport = false;
        boolean namedExport = false;
        int start = objectIndex;

        Word previous = previousWord(source, cursor);
        if (previous != null && DEFAULT_KEYWORD.equals(previous.word())) {
            defaultExport = true;
            start = previous.start();
            previous = previousWord(source, previous.start());
        }
        if (previous != null && EXPORT_KEYWORD.equals(previous.word())) {
            namedExport = true;
            start = previous.start();
        }
        if (defaultExport && !namedExport) {
            return null;
        }
        return new Prefix(start, namedExport, defaultExport);
    }

    private static Word previousWord(String source, int before) {
        int end = skipWhitespaceBack(source, before - 1) + 1;
        if (end <= 0) {
            return null;
        }
        int start = end - 1;
        while (start >= 0 && isIdentifierPart(source.charAt(start))) {
            start--;
        }
        start++;
        if (start >= end || !isIdentifierStart(source.charAt(start))) {
            return null;
        }
        return new Word(start, end, source.substring(start, end));
    }

    private static boolean isStatementBoundaryBefore(String source, int start) {
        int previous = skipWhitespaceBack(source, start - 1);
        if (previous < 0) {
            return true;
        }
        char c = source.charAt(previous);
        return c == ';' || c == '{' || c == '}' || c == '\n' || c == '\r';
    }

    private static int findClassBodyStart(String source, int from) {
        int i = from;
        while (i < source.length()) {
            int skipped = skipNonCode(source, i);
            if (skipped != i) {
                i = skipped;
                continue;
            }
            char c = source.charAt(i);
            if (c == '{') {
                return i;
            }
            if (c == ';' || c == '\n' || c == '\r') {
                return -1;
            }
            i++;
        }
        return -1;
    }

    private static int findMatchingBrace(String source, int openBrace) {
        int depth = 0;
        int i = openBrace;
        while (i < source.length()) {
            int skipped = skipNonCode(source, i);
            if (skipped != i) {
                i = skipped;
                continue;
            }
            char c = source.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }

    private static int skipNonCode(String source, int index) {
        if (index < 0 || index >= source.length()) {
            return index;
        }
        char c = source.charAt(index);
        if (c == '"' || c == '\'') {
            return skipQuoted(source, index, c);
        }
        if (c == '`') {
            return skipTemplate(source, index);
        }
        if (c == '/' && index + 1 < source.length()) {
            char next = source.charAt(index + 1);
            if (next == '/') {
                return skipLineComment(source, index);
            }
            if (next == '*') {
                return skipBlockComment(source, index);
            }
        }
        return index;
    }

    private static int skipQuoted(String source, int index, char quote) {
        int i = index + 1;
        while (i < source.length()) {
            char c = source.charAt(i);
            if (c == '\\') {
                i += 2;
                continue;
            }
            if (c == quote) {
                return i + 1;
            }
            i++;
        }
        return source.length();
    }

    private static int skipTemplate(String source, int index) {
        int i = index + 1;
        while (i < source.length()) {
            char c = source.charAt(i);
            if (c == '\\') {
                i += 2;
                continue;
            }
            if (c == '`') {
                return i + 1;
            }
            i++;
        }
        return source.length();
    }

    private static int skipLineComment(String source, int index) {
        int i = index + 2;
        while (i < source.length()) {
            char c = source.charAt(i);
            if (c == '\n' || c == '\r') {
                return i;
            }
            i++;
        }
        return source.length();
    }

    private static int skipBlockComment(String source, int index) {
        int end = source.indexOf("*/", index + 2);
        return end < 0 ? source.length() : end + 2;
    }

    private static int skipWhitespace(String source, int index) {
        int i = Math.max(0, index);
        while (i < source.length() && Character.isWhitespace(source.charAt(i))) {
            i++;
        }
        return i;
    }

    private static int skipWhitespaceBack(String source, int index) {
        int i = Math.min(index, source.length() - 1);
        while (i >= 0 && Character.isWhitespace(source.charAt(i))) {
            i--;
        }
        return i;
    }

    private static boolean isWordAt(String source, int index, String word) {
        if (index < 0 || index + word.length() > source.length()) {
            return false;
        }
        if (!source.regionMatches(index, word, 0, word.length())) {
            return false;
        }
        char before = charAt(source, index - 1);
        char after = charAt(source, index + word.length());
        return !isIdentifierPart(before) && !isIdentifierPart(after);
    }

    private static char charAt(String source, int index) {
        return index < 0 || index >= source.length() ? '\0' : source.charAt(index);
    }

    private static boolean isIdentifierStart(char c) {
        return c == '_' || c == '$' || Character.isLetter(c);
    }

    private static boolean isIdentifierPart(char c) {
        return isIdentifierStart(c) || Character.isDigit(c);
    }

    private record Prefix(int start, boolean namedExport, boolean defaultExport) {
    }

    private record Word(int start, int end, String word) {
    }

    private record ObjectDeclaration(
            String source,
            int declarationStart,
            int objectKeywordStart,
            String name,
            int nameEnd,
            int declarationEnd,
            boolean namedExport,
            boolean defaultExport) {
    }
}
