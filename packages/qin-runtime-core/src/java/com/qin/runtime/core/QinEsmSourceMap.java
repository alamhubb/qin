package com.qin.runtime.core;

import com.slime.generator.SlimeGenerator.MappedCode;
import com.slime.generator.SlimeGenerator.SourceMapping;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Source Map v3 for ESM emitted by the Java compilers. */
public final class QinEsmSourceMap {
    private static final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    private QinEsmSourceMap() {}

    public record Result<T>(T output, String sourceMap) {}

    static String inline(MappedCode generated, String sourceName, String source) {
        String json = encode(generated, sourceName, source);
        return generated.code() + "\n//# sourceMappingURL=data:application/json;base64,"
                + Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8)) + "\n";
    }

    static String cacheMappings(List<SourceMapping> mappings) {
        var text = new StringBuilder();
        for (var mapping : mappings) {
            text.append(mapping.generatedOffset()).append(':').append(mapping.originalOffset()).append('\n');
        }
        return text.toString();
    }

    static List<SourceMapping> cachedMappings(String text, int codeLength) {
        var mappings = new ArrayList<SourceMapping>();
        int previous = -1;
        var lines = text.lines().iterator();
        while (lines.hasNext()) {
            String line = lines.next();
            int colon = line.indexOf(':');
            if (colon < 1) return null;
            int generated;
            int original;
            try {
                generated = Integer.parseInt(line, 0, colon, 10);
                original = Integer.parseInt(line, colon + 1, line.length(), 10);
            } catch (NumberFormatException invalidCache) {
                return null;
            }
            if (generated <= previous || generated >= codeLength || original < 0) return null;
            mappings.add(new SourceMapping(generated, original));
            previous = generated;
        }
        return List.copyOf(mappings);
    }

    /** Copies slices in output order, keeping offsets aligned with the copied text. */
    static final class Builder {
        private final StringBuilder code = new StringBuilder();
        private final List<SourceMapping> mappings = new ArrayList<>();

        Builder append(String synthetic) {
            if (!synthetic.isEmpty()) {
                add(new SourceMapping(code.length(), -1));
                code.append(synthetic);
            }
            return this;
        }

        Builder append(MappedCode source) {
            return append(source, 0, source.code().length());
        }

        Builder append(MappedCode source, int start, int end) {
            if (start == end) return this;
            int offset = code.length() - start;
            List<SourceMapping> entries = source.mappings();
            int low = 0;
            int high = entries.size();
            while (low < high) {
                int middle = (low + high) >>> 1;
                if (entries.get(middle).generatedOffset() < start) low = middle + 1;
                else high = middle;
            }
            if (low == entries.size() || entries.get(low).generatedOffset() != start)
                add(new SourceMapping(code.length(), -1));
            for (int index = low; index < entries.size(); index++) {
                var mapping = entries.get(index);
                if (mapping.generatedOffset() >= end) break;
                add(new SourceMapping(mapping.generatedOffset() + offset, mapping.originalOffset()));
            }
            code.append(source.code(), start, end);
            return this;
        }

        private void add(SourceMapping mapping) {
            if (mapping.originalOffset() < 0 && (mappings.isEmpty() || mappings.getLast().originalOffset() < 0))
                return;
            mappings.add(mapping);
        }

        MappedCode build() {
            return new MappedCode(code.toString(), List.copyOf(mappings));
        }
    }

    static String encode(MappedCode generated, String sourceName, String source) {
        Objects.requireNonNull(sourceName, "sourceName");
        int[] generatedLines = lineStarts(generated.code());
        int[] originalLines = lineStarts(source);
        var mappings = new StringBuilder();
        int generatedLine = 0;
        int generatedColumn = 0;
        int originalLine = 0;
        int originalColumn = 0;
        boolean firstOnLine = true;
        for (var entry : generated.mappings()) {
            if (entry.originalOffset() >= source.length()
                    || entry.generatedOffset() >= generated.code().length()) continue;
            int line = lineAt(generatedLines, entry.generatedOffset());
            while (generatedLine < line) {
                mappings.append(';');
                generatedLine++;
                generatedColumn = 0;
                firstOnLine = true;
            }
            if (!firstOnLine) mappings.append(',');
            firstOnLine = false;
            int column = entry.generatedOffset() - generatedLines[line];
            writeVlq(mappings, column - generatedColumn);
            generatedColumn = column;
            if (entry.originalOffset() < 0) continue;
            int sourceLine = lineAt(originalLines, entry.originalOffset());
            int sourceColumn = entry.originalOffset() - originalLines[sourceLine];
            writeVlq(mappings, 0);
            writeVlq(mappings, sourceLine - originalLine);
            writeVlq(mappings, sourceColumn - originalColumn);
            originalLine = sourceLine;
            originalColumn = sourceColumn;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("version", 3);
        map.put("sources", List.of(sourceName));
        map.put("sourcesContent", List.of(source));
        map.put("names", List.of());
        map.put("mappings", mappings.toString());
        return QinObjectJsonEncoder.toJson(map);
    }

    private static int[] lineStarts(String source) {
        int[] starts = new int[16];
        int size = 1;
        for (int index = 0; index < source.length(); index++) {
            char character = source.charAt(index);
            if (character == '\r' && index + 1 < source.length() && source.charAt(index + 1) == '\n') index++;
            if (character == '\r' || character == '\n' || character == '\u2028' || character == '\u2029') {
                if (size == starts.length) starts = Arrays.copyOf(starts, size * 2);
                starts[size++] = index + 1;
            }
        }
        return Arrays.copyOf(starts, size);
    }

    private static int lineAt(int[] starts, int offset) {
        int index = Arrays.binarySearch(starts, offset);
        return index >= 0 ? index : -index - 2;
    }

    private static void writeVlq(StringBuilder output, int delta) {
        long value = (Math.abs((long) delta) << 1) | (delta < 0 ? 1 : 0);
        do {
            int digit = (int) (value & 31);
            value >>>= 5;
            output.append(BASE64.charAt(value == 0 ? digit : digit | 32));
        } while (value != 0);
    }
}
