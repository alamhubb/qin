package com.qin.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bootstrap parser for qin.config.js object literals.
 *
 * <p>This intentionally lives in the root CLI so a freshly installed Qin can
 * read its own manifest before project dependencies have been synced.
 */
final class QinConfigJsParser {
    private final String source;
    private int index;

    private QinConfigJsParser(String source) {
        this.source = source == null ? "" : stripBom(source);
    }

    static Map<String, Object> parseConfigObject(String source) throws IOException {
        QinConfigJsParser parser = new QinConfigJsParser(source);
        return parser.parseConfigObject();
    }

    private Map<String, Object> parseConfigObject() throws IOException {
        skipSpaceAndComments();
        if (startsWith("export")) {
            skipExportDefault();
        } else {
            skipLeadingImports();
            skipSpaceAndComments();
            if (startsWith("export")) {
                skipExportDefault();
            }
        }
        skipSpaceAndComments();
        if (!peek('{')) {
            throw error("Expected qin.config.js to export an object literal");
        }
        Map<String, Object> object = parseObject();
        skipSpaceAndComments();
        if (peek(';')) {
            index++;
        }
        return object;
    }

    private void skipLeadingImports() throws IOException {
        while (true) {
            skipSpaceAndComments();
            if (!startsWith("import")) {
                return;
            }
            skipStatement();
        }
    }

    private void skipExportDefault() throws IOException {
        expectIdentifier("export");
        skipSpaceAndComments();
        expectIdentifier("default");
    }

    private Map<String, Object> parseObject() throws IOException {
        expect('{');
        Map<String, Object> out = new LinkedHashMap<>();
        skipSpaceAndComments();
        while (!eof() && !peek('}')) {
            String key = parseKey();
            skipSpaceAndComments();
            expect(':');
            Object value = parseValue();
            out.put(key, value);
            skipSpaceAndComments();
            if (peek(',')) {
                index++;
                skipSpaceAndComments();
                continue;
            }
            if (!peek('}')) {
                throw error("Expected ',' or '}' in object literal");
            }
        }
        expect('}');
        return out;
    }

    private List<Object> parseArray() throws IOException {
        expect('[');
        List<Object> out = new ArrayList<>();
        skipSpaceAndComments();
        while (!eof() && !peek(']')) {
            out.add(parseValue());
            skipSpaceAndComments();
            if (peek(',')) {
                index++;
                skipSpaceAndComments();
                continue;
            }
            if (!peek(']')) {
                throw error("Expected ',' or ']' in array literal");
            }
        }
        expect(']');
        return out;
    }

    private Object parseValue() throws IOException {
        skipSpaceAndComments();
        if (eof()) {
            throw error("Expected value");
        }
        char ch = source.charAt(index);
        if (ch == '\'' || ch == '"') {
            return parseString();
        }
        if (ch == '{') {
            return parseObject();
        }
        if (ch == '[') {
            return parseArray();
        }
        if (startsWith("true")) {
            index += 4;
            return Boolean.TRUE;
        }
        if (startsWith("false")) {
            index += 5;
            return Boolean.FALSE;
        }
        if (startsWith("null")) {
            index += 4;
            return null;
        }
        if (ch == '-' || Character.isDigit(ch)) {
            return parseNumber();
        }
        return skipRawExpressionValue();
    }

    private Object skipRawExpressionValue() {
        int start = index;
        int paren = 0;
        int bracket = 0;
        int brace = 0;
        while (!eof()) {
            char ch = source.charAt(index);
            if (ch == '\'' || ch == '"') {
                skipString();
                continue;
            }
            if (ch == '(') {
                paren++;
            } else if (ch == ')') {
                if (paren > 0) {
                    paren--;
                }
            } else if (ch == '[') {
                bracket++;
            } else if (ch == ']') {
                if (bracket == 0 && paren == 0 && brace == 0) {
                    break;
                }
                bracket--;
            } else if (ch == '{') {
                brace++;
            } else if (ch == '}') {
                if (brace == 0 && paren == 0 && bracket == 0) {
                    break;
                }
                brace--;
            } else if (ch == ',' && paren == 0 && bracket == 0 && brace == 0) {
                break;
            }
            index++;
        }
        return new RawExpression(source.substring(start, index).trim());
    }

    private Number parseNumber() {
        int start = index;
        if (peek('-')) {
            index++;
        }
        while (!eof() && Character.isDigit(source.charAt(index))) {
            index++;
        }
        boolean floating = false;
        if (!eof() && source.charAt(index) == '.') {
            floating = true;
            index++;
            while (!eof() && Character.isDigit(source.charAt(index))) {
                index++;
            }
        }
        String text = source.substring(start, index);
        return floating ? Double.parseDouble(text) : Integer.parseInt(text);
    }

    private String parseKey() throws IOException {
        skipSpaceAndComments();
        char ch = source.charAt(index);
        if (ch == '\'' || ch == '"') {
            return parseString();
        }
        if (!isIdentifierStart(ch)) {
            throw error("Expected object key");
        }
        int start = index++;
        while (!eof() && isIdentifierPart(source.charAt(index))) {
            index++;
        }
        return source.substring(start, index);
    }

    private String parseString() throws IOException {
        char quote = source.charAt(index++);
        StringBuilder out = new StringBuilder();
        while (!eof()) {
            char ch = source.charAt(index++);
            if (ch == quote) {
                return out.toString();
            }
            if (ch == '\\') {
                if (eof()) {
                    throw error("Unterminated string escape");
                }
                char escaped = source.charAt(index++);
                out.append(switch (escaped) {
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case 'b' -> '\b';
                    case 'f' -> '\f';
                    case '\\' -> '\\';
                    case '\'' -> '\'';
                    case '"' -> '"';
                    default -> escaped;
                });
            } else {
                out.append(ch);
            }
        }
        throw error("Unterminated string literal");
    }

    private void skipString() {
        char quote = source.charAt(index++);
        while (!eof()) {
            char ch = source.charAt(index++);
            if (ch == '\\' && !eof()) {
                index++;
            } else if (ch == quote) {
                return;
            }
        }
    }

    private void skipStatement() {
        while (!eof()) {
            char ch = source.charAt(index++);
            if (ch == '\'' || ch == '"') {
                index--;
                skipString();
                continue;
            }
            if (ch == ';' || ch == '\n' || ch == '\r') {
                return;
            }
        }
    }

    private void skipSpaceAndComments() {
        while (!eof()) {
            char ch = source.charAt(index);
            if (Character.isWhitespace(ch)) {
                index++;
                continue;
            }
            if (ch == '/' && index + 1 < source.length()) {
                char next = source.charAt(index + 1);
                if (next == '/') {
                    index += 2;
                    while (!eof() && source.charAt(index) != '\n') {
                        index++;
                    }
                    continue;
                }
                if (next == '*') {
                    index += 2;
                    while (index + 1 < source.length()
                            && !(source.charAt(index) == '*' && source.charAt(index + 1) == '/')) {
                        index++;
                    }
                    if (index + 1 < source.length()) {
                        index += 2;
                    }
                    continue;
                }
            }
            return;
        }
    }

    private void expectIdentifier(String identifier) throws IOException {
        skipSpaceAndComments();
        if (!startsWith(identifier)) {
            throw error("Expected '" + identifier + "'");
        }
        index += identifier.length();
    }

    private void expect(char expected) throws IOException {
        skipSpaceAndComments();
        if (eof() || source.charAt(index) != expected) {
            throw error("Expected '" + expected + "'");
        }
        index++;
    }

    private boolean startsWith(String text) {
        if (!source.startsWith(text, index)) {
            return false;
        }
        int end = index + text.length();
        return end >= source.length() || !isIdentifierPart(source.charAt(end));
    }

    private boolean peek(char ch) {
        return !eof() && source.charAt(index) == ch;
    }

    private boolean eof() {
        return index >= source.length();
    }

    private IOException error(String message) {
        return new IOException(message + " at qin.config.js offset " + index);
    }

    private static boolean isIdentifierStart(char ch) {
        return Character.isLetter(ch) || ch == '_' || ch == '$';
    }

    private static boolean isIdentifierPart(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_' || ch == '$';
    }

    private static String stripBom(String value) {
        return !value.isEmpty() && value.charAt(0) == '\uFEFF' ? value.substring(1) : value;
    }

    record RawExpression(String source) {
    }
}
