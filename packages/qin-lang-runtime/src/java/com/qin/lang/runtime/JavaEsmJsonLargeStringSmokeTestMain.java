package com.qin.lang.runtime;

import java.util.Map;

public final class JavaEsmJsonLargeStringSmokeTestMain {
    private JavaEsmJsonLargeStringSmokeTestMain() {
    }

    public static void main(String[] args) {
        String text = "x".repeat(300_000);
        Object parsed = JavaEsmJson.parse("{\"text\":\"" + text + "\"}");
        if (!(parsed instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected JSON object, got " + parsed);
        }
        Object parsedText = map.get("text");
        if (!(parsedText instanceof String value) || value.length() != text.length()) {
            throw new IllegalStateException("Large JSON string was not parsed correctly");
        }
        String json = "{\"text\":\"" + text + "\"}";
        Object parsedChunks = JavaEsmJson.parseChunks(new String[]{
                json.substring(0, 17_000),
                json.substring(17_000, 230_000),
                json.substring(230_000)
        });
        if (!(parsedChunks instanceof Map<?, ?> chunkMap)
                || !(chunkMap.get("text") instanceof String chunkValue)
                || chunkValue.length() != text.length()) {
            throw new IllegalStateException("Chunked large JSON string was not parsed correctly");
        }

        System.out.println("JavaEsmJsonLargeStringSmokeTestMain OK");
    }
}
