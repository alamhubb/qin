package com.qin.subhutijson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.Set;
import java.util.regex.Pattern;

public final class QinJsonModule {
    private static final Gson GSON = new Gson();
    private static final Pattern ECMASCRIPT_IDENTIFIER = Pattern.compile("[$_\\p{L}][$_\\p{L}\\p{N}\\p{M}]*");
    private static final Set<String> RESERVED_EXPORTS = Set.of(
            "await", "break", "case", "catch", "class", "const", "continue", "debugger", "default",
            "delete", "do", "else", "enum", "export", "extends", "false", "finally", "for", "function",
            "if", "implements", "import", "in", "instanceof", "interface", "let", "new", "null", "package",
            "private", "protected", "public", "return", "static", "super", "switch", "this", "throw",
            "true", "try", "typeof", "var", "void", "while", "with", "yield", "arguments", "eval");

    private QinJsonModule() {
    }

    public static String toEsm(String source) {
        if (source == null) {
            throw new IllegalArgumentException("JSON module source cannot be null");
        }
        SubhutiJsonParser parser = new SubhutiJsonParser(source);
        parser.parseJsonDocument();
        if (parser.isParserFail() || !parser.isAtEnd()) {
            throw new IllegalArgumentException("Invalid JSON module syntax");
        }

        JsonElement value;
        try {
            JsonReader reader = new JsonReader(new StringReader(source));
            reader.setLenient(false);
            value = GSON.getAdapter(JsonElement.class).read(reader);
            if (reader.peek() != com.google.gson.stream.JsonToken.END_DOCUMENT) {
                throw new IllegalArgumentException("Unexpected trailing data in JSON module");
            }
        } catch (RuntimeException error) {
            throw new IllegalArgumentException("Invalid JSON module: " + error.getMessage(), error);
        } catch (java.io.IOException error) {
            throw new IllegalArgumentException("Invalid JSON module: " + error.getMessage(), error);
        }

        String serialized = GSON.toJson(source);
        StringBuilder module = new StringBuilder("const __qin_json = JSON.parse(")
                .append(serialized)
                .append(");\nexport default __qin_json;\n");
        if (value.isJsonObject()) {
            JsonObject object = value.getAsJsonObject();
            for (String key : object.keySet()) {
                if (isExportName(key)) {
                    module.append("export const ").append(key).append(" = __qin_json[")
                            .append(GSON.toJson(key)).append("];\n");
                }
            }
        }
        return module.toString();
    }

    private static boolean isExportName(String key) {
        return ECMASCRIPT_IDENTIFIER.matcher(key).matches() && !RESERVED_EXPORTS.contains(key);
    }
}
