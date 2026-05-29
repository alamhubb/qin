package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Java-backed JSON builtin subset for Qin.
 */
public final class JavaEsmJson {
    private JavaEsmJson() {
    }

    public static String stringify(Object value) {
        return stringify(value, null, null);
    }

    public static String stringify(Object value, Object replacer) {
        return stringify(value, replacer, null);
    }

    public static String stringify(Object value, Object replacer, Object space) {
        StringBuilder out = new StringBuilder();
        writeJson(applyReplacer(replacer, "", value), out, replacer);
        return out.toString();
    }

    public static Object parse(String json) {
        if (json == null) {
            return null;
        }
        Parser parser = new Parser(json);
        Object value = parser.parseValue();
        parser.skipWhitespace();
        if (!parser.isDone()) {
            throw new IllegalArgumentException("Unexpected trailing JSON input");
        }
        return value;
    }

    private static void writeJson(Object value, StringBuilder out) {
        writeJson(value, out, null);
    }

    private static void writeJson(Object value, StringBuilder out, Object replacer) {
        if (value == null) {
            out.append("null");
            return;
        }
        if (value instanceof String text) {
            writeString(text, out);
            return;
        }
        if (value instanceof Number number) {
            writeNumber(number, out);
            return;
        }
        if (value instanceof Boolean) {
            out.append(value);
            return;
        }
        if (value instanceof JavaEsmMapObject mapObject) {
            writeMap(JavaEsmObject.enumerableEntries(mapObject), out, replacer);
            return;
        }
        if (value instanceof JavaEsmSetObject setObject) {
            writeIterable(setObject.values(), out, replacer);
            return;
        }
        if (value instanceof Map<?, ?> map) {
            LinkedHashMap<String, Object> normalized = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                normalized.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            writeMap(normalized, out, replacer);
            return;
        }
        if (value instanceof Iterable<?> iterable) {
            writeIterable(iterable, out, replacer);
            return;
        }
        if (value.getClass().isArray()) {
            out.append('[');
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    out.append(',');
                }
                Object item = java.lang.reflect.Array.get(value, i);
                writeJson(applyReplacer(replacer, String.valueOf(i), item), out, replacer);
            }
            out.append(']');
            return;
        }
        writeMap(JavaEsmObject.enumerableEntries(value), out, replacer);
    }

    private static void writeIterable(Iterable<?> iterable, StringBuilder out, Object replacer) {
        out.append('[');
        boolean first = true;
        int index = 0;
        for (Object item : iterable) {
            if (!first) {
                out.append(',');
            }
            first = false;
            writeJson(applyReplacer(replacer, String.valueOf(index++), item), out, replacer);
        }
        out.append(']');
    }

    private static void writeMap(Map<String, Object> map, StringBuilder out, Object replacer) {
        out.append('{');
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = applyReplacer(replacer, entry.getKey(), entry.getValue());
            if (!first) {
                out.append(',');
            }
            first = false;
            writeString(entry.getKey(), out);
            out.append(':');
            writeJson(value, out, replacer);
        }
        out.append('}');
    }

    private static Object applyReplacer(Object replacer, String key, Object value) {
        if (replacer == null) {
            return value;
        }
        if (JavaEsmGlobal.isRuntimeCallable(replacer)) {
            return JavaEsmGlobal.callRuntimeCallable(replacer, key, value);
        }
        return value;
    }

    private static void writeString(String text, StringBuilder out) {
        out.append('"');
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\b' -> out.append("\\b");
                case '\f' -> out.append("\\f");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        out.append('"');
    }

    private static void writeNumber(Number number, StringBuilder out) {
        if (number instanceof Double doubleValue
                && Double.isFinite(doubleValue)
                && doubleValue == Math.rint(doubleValue)) {
            long whole = doubleValue.longValue();
            out.append(whole);
            return;
        }
        if (number instanceof Float floatValue
                && Float.isFinite(floatValue)
                && floatValue == Math.rint(floatValue)) {
            long whole = floatValue.longValue();
            out.append(whole);
            return;
        }
        out.append(number);
    }

    private static final class Parser {
        private final String source;
        private int index;

        private Parser(String source) {
            this.source = source;
        }

        private Object parseValue() {
            skipWhitespace();
            if (isDone()) {
                throw new IllegalArgumentException("Unexpected end of JSON input");
            }
            char current = source.charAt(index);
            return switch (current) {
                case '{' -> parseObject();
                case '[' -> parseArray();
                case '"' -> parseString();
                case 't' -> parseLiteral("true", Boolean.TRUE);
                case 'f' -> parseLiteral("false", Boolean.FALSE);
                case 'n' -> parseLiteral("null", null);
                default -> parseNumber();
            };
        }

        private Map<String, Object> parseObject() {
            index++;
            LinkedHashMap<String, Object> object = new LinkedHashMap<>();
            skipWhitespace();
            if (peek('}')) {
                index++;
                return object;
            }
            while (true) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                expect(':');
                Object value = parseValue();
                object.put(key, value);
                skipWhitespace();
                if (peek('}')) {
                    index++;
                    return object;
                }
                expect(',');
            }
        }

        private List<Object> parseArray() {
            index++;
            ArrayList<Object> array = new ArrayList<>();
            skipWhitespace();
            if (peek(']')) {
                index++;
                return array;
            }
            while (true) {
                array.add(parseValue());
                skipWhitespace();
                if (peek(']')) {
                    index++;
                    return array;
                }
                expect(',');
            }
        }

        private String parseString() {
            expect('"');
            StringBuilder out = new StringBuilder();
            while (!isDone()) {
                char current = source.charAt(index++);
                if (current == '"') {
                    return out.toString();
                }
                if (current == '\\') {
                    if (isDone()) {
                        throw new IllegalArgumentException("Invalid JSON escape");
                    }
                    char escaped = source.charAt(index++);
                    switch (escaped) {
                        case '"', '\\', '/' -> out.append(escaped);
                        case 'b' -> out.append('\b');
                        case 'f' -> out.append('\f');
                        case 'n' -> out.append('\n');
                        case 'r' -> out.append('\r');
                        case 't' -> out.append('\t');
                        case 'u' -> {
                            if (index + 4 > source.length()) {
                                throw new IllegalArgumentException("Invalid unicode escape");
                            }
                            String hex = source.substring(index, index + 4);
                            out.append((char) Integer.parseInt(hex, 16));
                            index += 4;
                        }
                        default -> throw new IllegalArgumentException("Unsupported escape: \\" + escaped);
                    }
                    continue;
                }
                out.append(current);
            }
            throw new IllegalArgumentException("Unterminated JSON string");
        }

        private Object parseLiteral(String token, Object value) {
            if (!source.startsWith(token, index)) {
                throw new IllegalArgumentException("Invalid JSON literal at index " + index);
            }
            index += token.length();
            return value;
        }

        private Double parseNumber() {
            int start = index;
            if (peek('-')) {
                index++;
            }
            consumeDigits();
            if (peek('.')) {
                index++;
                consumeDigits();
            }
            if (peek('e') || peek('E')) {
                index++;
                if (peek('+') || peek('-')) {
                    index++;
                }
                consumeDigits();
            }
            return Double.parseDouble(source.substring(start, index));
        }

        private void consumeDigits() {
            int start = index;
            while (!isDone() && Character.isDigit(source.charAt(index))) {
                index++;
            }
            if (start == index) {
                throw new IllegalArgumentException("Expected digit at index " + index);
            }
        }

        private void expect(char expected) {
            skipWhitespace();
            if (isDone() || source.charAt(index) != expected) {
                throw new IllegalArgumentException("Expected `" + expected + "` at index " + index);
            }
            index++;
        }

        private boolean peek(char c) {
            return !isDone() && source.charAt(index) == c;
        }

        private void skipWhitespace() {
            while (!isDone() && Character.isWhitespace(source.charAt(index))) {
                index++;
            }
        }

        private boolean isDone() {
            return index >= source.length();
        }
    }
}
