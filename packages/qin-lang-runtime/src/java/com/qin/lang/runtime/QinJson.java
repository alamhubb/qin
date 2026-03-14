package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Runtime JSON built-ins for Qin JS-style global JSON object.
 */
public final class QinJson {
    private QinJson() {
    }

    public static String stringify(Object value) {
        StringBuilder out = new StringBuilder();
        writeJson(value, out);
        return out.toString();
    }

    public static Object parse(String json) {
        if (json == null) {
            throw new IllegalArgumentException("QJS2007 JSON.parse expects non-null string");
        }
        Parser parser = new Parser(json);
        Object value = parser.parseValue();
        parser.skipWhitespace();
        if (!parser.isEnd()) {
            throw new IllegalArgumentException("QJS2007 JSON.parse trailing content at index " + parser.index());
        }
        return value;
    }

    private static void writeJson(Object value, StringBuilder out) {
        if (value == null) {
            out.append("null");
            return;
        }
        if (value instanceof String text) {
            out.append('"').append(escapeString(text)).append('"');
            return;
        }
        if (value instanceof Number || value instanceof Boolean) {
            out.append(value);
            return;
        }
        if (value instanceof Map<?, ?> map) {
            out.append('{');
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    out.append(',');
                }
                first = false;
                out.append('"').append(escapeString(String.valueOf(entry.getKey()))).append('"').append(':');
                writeJson(entry.getValue(), out);
            }
            out.append('}');
            return;
        }
        if (value instanceof List<?> list) {
            out.append('[');
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) {
                    out.append(',');
                }
                writeJson(list.get(i), out);
            }
            out.append(']');
            return;
        }
        out.append('"').append(escapeString(String.valueOf(value))).append('"');
    }

    private static String escapeString(String text) {
        StringBuilder escaped = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '"' -> escaped.append("\\\"");
                case '\\' -> escaped.append("\\\\");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (ch < 0x20) {
                        String hex = Integer.toHexString(ch);
                        escaped.append("\\u");
                        for (int j = hex.length(); j < 4; j++) {
                            escaped.append('0');
                        }
                        escaped.append(hex);
                    } else {
                        escaped.append(ch);
                    }
                }
            }
        }
        return escaped.toString();
    }

    private static final class Parser {
        private final String text;
        private int index;

        private Parser(String text) {
            this.text = text;
            this.index = 0;
        }

        private int index() {
            return index;
        }

        private boolean isEnd() {
            return index >= text.length();
        }

        private void skipWhitespace() {
            while (!isEnd()) {
                char ch = text.charAt(index);
                if (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') {
                    index++;
                    continue;
                }
                break;
            }
        }

        private Object parseValue() {
            skipWhitespace();
            if (isEnd()) {
                throw error("unexpected end of input");
            }
            char ch = text.charAt(index);
            return switch (ch) {
                case '{' -> parseObject();
                case '[' -> parseArray();
                case '"' -> parseString();
                case 't' -> parseTrue();
                case 'f' -> parseFalse();
                case 'n' -> parseNull();
                default -> parseNumber();
            };
        }

        private Object parseObject() {
            expect('{');
            skipWhitespace();
            Map<String, Object> result = new LinkedHashMap<>();
            if (peek('}')) {
                expect('}');
                return result;
            }

            while (true) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                result.put(key, value);
                skipWhitespace();
                if (peek('}')) {
                    expect('}');
                    return result;
                }
                expect(',');
            }
        }

        private Object parseArray() {
            expect('[');
            skipWhitespace();
            List<Object> result = new ArrayList<>();
            if (peek(']')) {
                expect(']');
                return result;
            }
            while (true) {
                result.add(parseValue());
                skipWhitespace();
                if (peek(']')) {
                    expect(']');
                    return result;
                }
                expect(',');
            }
        }

        private String parseString() {
            expect('"');
            StringBuilder out = new StringBuilder();
            while (!isEnd()) {
                char ch = text.charAt(index++);
                if (ch == '"') {
                    return out.toString();
                }
                if (ch == '\\') {
                    if (isEnd()) {
                        throw error("invalid escape sequence");
                    }
                    char esc = text.charAt(index++);
                    switch (esc) {
                        case '"' -> out.append('"');
                        case '\\' -> out.append('\\');
                        case '/' -> out.append('/');
                        case 'b' -> out.append('\b');
                        case 'f' -> out.append('\f');
                        case 'n' -> out.append('\n');
                        case 'r' -> out.append('\r');
                        case 't' -> out.append('\t');
                        case 'u' -> out.append(parseUnicode());
                        default -> throw error("invalid escape char: " + esc);
                    }
                    continue;
                }
                out.append(ch);
            }
            throw error("unterminated string");
        }

        private char parseUnicode() {
            if (index + 4 > text.length()) {
                throw error("invalid unicode escape");
            }
            String hex = text.substring(index, index + 4);
            index += 4;
            try {
                return (char) Integer.parseInt(hex, 16);
            } catch (NumberFormatException e) {
                throw error("invalid unicode escape: " + hex);
            }
        }

        private Boolean parseTrue() {
            expectKeyword("true");
            return Boolean.TRUE;
        }

        private Boolean parseFalse() {
            expectKeyword("false");
            return Boolean.FALSE;
        }

        private Object parseNull() {
            expectKeyword("null");
            return null;
        }

        private Number parseNumber() {
            int start = index;
            if (peek('-')) {
                index++;
            }
            parseDigits();
            boolean floating = false;
            if (peek('.')) {
                floating = true;
                index++;
                parseDigits();
            }
            if (peek('e') || peek('E')) {
                floating = true;
                index++;
                if (peek('+') || peek('-')) {
                    index++;
                }
                parseDigits();
            }
            String raw = text.substring(start, index);
            try {
                if (floating) {
                    return Double.parseDouble(raw);
                }
                long value = Long.parseLong(raw);
                if (value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE) {
                    return (int) value;
                }
                return value;
            } catch (NumberFormatException e) {
                throw error("invalid number: " + raw);
            }
        }

        private void parseDigits() {
            if (isEnd() || !Character.isDigit(text.charAt(index))) {
                throw error("expected digit");
            }
            while (!isEnd() && Character.isDigit(text.charAt(index))) {
                index++;
            }
        }

        private void expectKeyword(String keyword) {
            if (index + keyword.length() > text.length()) {
                throw error("expected " + keyword);
            }
            if (!text.regionMatches(index, keyword, 0, keyword.length())) {
                throw error("expected " + keyword);
            }
            index += keyword.length();
        }

        private void expect(char expected) {
            skipWhitespace();
            if (isEnd() || text.charAt(index) != expected) {
                throw error("expected '" + expected + "'");
            }
            index++;
        }

        private boolean peek(char expected) {
            return !isEnd() && text.charAt(index) == expected;
        }

        private IllegalArgumentException error(String message) {
            return new IllegalArgumentException("QJS2007 JSON.parse error at index " + index + ": " + message);
        }
    }
}
