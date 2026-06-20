package com.qin.runtime.http;

import java.util.Map;

public final class QinJson {
    private QinJson() {
    }

    @SuppressWarnings("unchecked")
    public static String stringify(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof String s) {
            return "\"" + escape(s) + "\"";
        }
        if (value instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : ((Map<Object, Object>) map).entrySet()) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append("\"").append(escape(String.valueOf(entry.getKey()))).append("\":")
                        .append(stringify(entry.getValue()));
            }
            sb.append("}");
            return sb.toString();
        }
        if (value instanceof Iterable<?> iterable) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            boolean first = true;
            for (Object item : iterable) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(stringify(item));
            }
            sb.append("]");
            return sb.toString();
        }
        return "\"" + escape(String.valueOf(value)) + "\"";
    }

    public static String stringField(String json, String name) {
        String raw = fieldRawValue(json, name);
        if (raw == null || raw.isBlank() || raw.charAt(0) != '"') {
            return null;
        }
        return unescapeQuoted(raw);
    }

    public static String numberField(String json, String name) {
        String raw = fieldRawValue(json, name);
        if (raw == null) {
            return null;
        }
        int end = 0;
        while (end < raw.length()) {
            char ch = raw.charAt(end);
            if ((ch >= '0' && ch <= '9') || ch == '-' || ch == '+') {
                end++;
                continue;
            }
            break;
        }
        return end == 0 ? null : raw.substring(0, end);
    }

    public static String escape(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String fieldRawValue(String json, String name) {
        if (json == null || name == null) {
            return null;
        }
        String needle = "\"" + escape(name) + "\"";
        int key = json.indexOf(needle);
        if (key < 0) {
            return null;
        }
        int colon = json.indexOf(':', key + needle.length());
        if (colon < 0) {
            return null;
        }
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }
        return start < json.length() ? json.substring(start) : null;
    }

    private static String unescapeQuoted(String raw) {
        StringBuilder result = new StringBuilder();
        boolean escaping = false;
        for (int i = 1; i < raw.length(); i++) {
            char ch = raw.charAt(i);
            if (escaping) {
                result.append(switch (ch) {
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case '"', '\\', '/' -> ch;
                    default -> ch;
                });
                escaping = false;
                continue;
            }
            if (ch == '\\') {
                escaping = true;
                continue;
            }
            if (ch == '"') {
                return result.toString();
            }
            result.append(ch);
        }
        return result.toString();
    }
}
