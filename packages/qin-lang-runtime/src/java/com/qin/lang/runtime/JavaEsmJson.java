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
        return parseSource(json);
    }

    public static Object parseChunks(String[] chunks) {
        if (chunks == null) {
            return null;
        }
        return parseSource(new ChunkedJsonSource(chunks));
    }

    private static Object parseSource(CharSequence json) {
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

    private static final class ChunkedJsonSource implements CharSequence {
        private final String[] chunks;
        private final int[] starts;
        private final int[] lengths;
        private final int length;
        private int discardCursor;

        private ChunkedJsonSource(String[] chunks) {
            this.chunks = chunks.clone();
            this.starts = new int[chunks.length];
            this.lengths = new int[chunks.length];
            int cursor = 0;
            for (int i = 0; i < chunks.length; i++) {
                starts[i] = cursor;
                String chunk = chunks[i] == null ? "" : chunks[i];
                this.chunks[i] = chunk;
                lengths[i] = chunk.length();
                cursor += lengths[i];
            }
            this.length = cursor;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public char charAt(int index) {
            if (index < 0 || index >= length) {
                throw new IndexOutOfBoundsException(index);
            }
            int chunkIndex = chunkIndex(index);
            return chunks[chunkIndex].charAt(index - starts[chunkIndex]);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return slice(start, end);
        }

        private String slice(int start, int end) {
            if (start < 0 || end < start || end > length) {
                throw new IndexOutOfBoundsException(start + ".." + end);
            }
            if (start == end) {
                return "";
            }
            int startChunk = chunkIndex(start);
            int endChunk = chunkIndex(end - 1);
            if (startChunk == endChunk) {
                return chunks[startChunk].substring(start - starts[startChunk], end - starts[startChunk]);
            }
            StringBuilder out = new StringBuilder(end - start);
            int firstOffset = start - starts[startChunk];
            out.append(chunks[startChunk], firstOffset, chunks[startChunk].length());
            for (int chunk = startChunk + 1; chunk < endChunk; chunk++) {
                out.append(chunks[chunk]);
            }
            out.append(chunks[endChunk], 0, end - starts[endChunk]);
            return out.toString();
        }

        private void discardBefore(int index) {
            while (discardCursor < chunks.length && starts[discardCursor] + lengths[discardCursor] <= index) {
                chunks[discardCursor] = "";
                discardCursor++;
            }
        }

        private int chunkIndex(int index) {
            int low = 0;
            int high = starts.length - 1;
            while (low <= high) {
                int mid = (low + high) >>> 1;
                int start = starts[mid];
                int end = start + lengths[mid];
                if (index < start) {
                    high = mid - 1;
                } else if (index >= end) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            }
            throw new IndexOutOfBoundsException(index);
        }
    }

    private static final class Parser {
        private final CharSequence source;
        private int index;

        private Parser(CharSequence source) {
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
                discardConsumedChunks();
                skipWhitespace();
                if (peek('}')) {
                    index++;
                    discardConsumedChunks();
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
                discardConsumedChunks();
                skipWhitespace();
                if (peek(']')) {
                    index++;
                    discardConsumedChunks();
                    return array;
                }
                expect(',');
            }
        }

        private String parseString() {
            expect('"');
            int start = index;
            while (!isDone()) {
                char current = source.charAt(index);
                if (current == '"') {
                    String text = slice(start, index);
                    index++;
                    return text;
                }
                if (current == '\\' || current < 0x20) {
                    index = start;
                    break;
                }
                index++;
            }
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
                            String hex = slice(index, index + 4);
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
            if (!startsWith(token, index)) {
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
            return Double.parseDouble(slice(start, index));
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

        private boolean startsWith(String token, int offset) {
            if (offset < 0 || offset + token.length() > source.length()) {
                return false;
            }
            for (int i = 0; i < token.length(); i++) {
                if (source.charAt(offset + i) != token.charAt(i)) {
                    return false;
                }
            }
            return true;
        }

        private void discardConsumedChunks() {
            if (source instanceof ChunkedJsonSource chunkedSource) {
                chunkedSource.discardBefore(index);
            }
        }

        private String slice(int start, int end) {
            if (source instanceof ChunkedJsonSource chunkedSource) {
                return chunkedSource.slice(start, end);
            }
            return source.subSequence(start, end).toString();
        }
    }
}
