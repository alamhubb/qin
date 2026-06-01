package com.qin.runtime.core;

import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Encodes Java object graphs into structured JSON for diagnostics output.
 */
public final class QinObjectJsonEncoder {
    private static final int MAX_DEPTH = 128;

    private final IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
    private final StringBuilder out = new StringBuilder();
    private final int maxChars;
    private boolean truncated;

    private QinObjectJsonEncoder(int maxChars) {
        this.maxChars = maxChars;
    }

    public static String toJson(Object value) {
        return new QinObjectJsonEncoder(-1).encode(value);
    }

    public static String toJson(Object value, int maxChars) {
        if (maxChars <= 0) {
            return toJson(value);
        }
        return new QinObjectJsonEncoder(maxChars).encode(value);
    }

    private String encode(Object value) {
        writeValue(value, 0);
        return out.toString();
    }

    private void writeValue(Object value, int depth) {
        if (isAtBudget()) {
            writeTruncatedString();
            return;
        }
        if (depth > MAX_DEPTH) {
            writeString("<max-depth>");
            return;
        }
        if (value == null) {
            out.append("null");
            return;
        }
        if (value instanceof String text) {
            writeString(text);
            return;
        }
        if (value instanceof Number || value instanceof Boolean) {
            out.append(value);
            return;
        }
        if (value instanceof Enum<?> enumValue) {
            writeString(enumValue.name());
            return;
        }
        if (value instanceof Class<?> clazz) {
            writeString(clazz.getName());
            return;
        }
        if (value instanceof Collection<?> collection) {
            if (markSeen(value)) {
                return;
            }
            writeCollection(collection, depth + 1);
            return;
        }
        if (value instanceof Map<?, ?> map) {
            if (markSeen(value)) {
                return;
            }
            writeMap(map, depth + 1);
            return;
        }
        if (value.getClass().isArray()) {
            if (markSeen(value)) {
                return;
            }
            int len = java.lang.reflect.Array.getLength(value);
            out.append('[');
            for (int i = 0; i < len; i++) {
                if (isAtBudget()) {
                    if (i > 0) {
                        out.append(',');
                    }
                    writeTruncatedString();
                    break;
                }
                if (i > 0) {
                    out.append(',');
                }
                writeValue(java.lang.reflect.Array.get(value, i), depth + 1);
            }
            out.append(']');
            return;
        }
        writeObject(value, depth + 1);
    }

    private void writeCollection(Collection<?> collection, int depth) {
        out.append('[');
        boolean first = true;
        for (Object item : collection) {
            if (isAtBudget()) {
                if (!first) {
                    out.append(',');
                }
                writeTruncatedString();
                break;
            }
            if (!first) {
                out.append(',');
            }
            first = false;
            writeValue(item, depth);
        }
        out.append(']');
    }

    private void writeMap(Map<?, ?> map, int depth) {
        out.append('{');
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (isAtBudget()) {
                if (!first) {
                    out.append(',');
                }
                writeString("__qin_truncated__");
                out.append(':');
                out.append("true");
                break;
            }
            if (!first) {
                out.append(',');
            }
            first = false;
            writeString(String.valueOf(entry.getKey()));
            out.append(':');
            writeValue(entry.getValue(), depth);
        }
        out.append('}');
    }

    private void writeObject(Object value, int depth) {
        if (markSeen(value)) {
            return;
        }

        out.append('{');
        Map<String, Object> fields = extractFields(value);
        boolean first = true;
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (isAtBudget()) {
                if (!first) {
                    out.append(',');
                }
                writeString("__qin_truncated__");
                out.append(':');
                out.append("true");
                break;
            }
            if (!first) {
                out.append(',');
            }
            first = false;
            writeString(entry.getKey());
            out.append(':');
            writeValue(entry.getValue(), depth);
        }
        out.append('}');
    }

    private boolean markSeen(Object value) {
        if (seen.containsKey(value)) {
            writeString("<cycle>");
            return true;
        }
        seen.put(value, Boolean.TRUE);
        return false;
    }

    private Map<String, Object> extractFields(Object value) {
        Class<?> type = value.getClass();
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        if (type.isRecord()) {
            RecordComponent[] components = type.getRecordComponents();
            if (components != null) {
                for (RecordComponent component : components) {
                    try {
                        fields.put(component.getName(), component.getAccessor().invoke(value));
                    } catch (Exception e) {
                        fields.put(component.getName(), "<error:" + e.getClass().getSimpleName() + ">");
                    }
                }
                return fields;
            }
        }

        Set<String> visitedNames = new java.util.HashSet<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            java.lang.reflect.Field[] declared = current.getDeclaredFields();
            for (java.lang.reflect.Field field : declared) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                if (!visitedNames.add(field.getName())) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    fields.put(field.getName(), field.get(value));
                } catch (Exception e) {
                    fields.put(field.getName(), "<error:" + e.getClass().getSimpleName() + ">");
                }
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    private void writeString(String text) {
        if (isAtBudget() && !truncated) {
            text = "<truncated>";
            truncated = true;
        } else if (maxChars > 0) {
            int remaining = maxChars - out.length();
            if (remaining < text.length() + 16) {
                int keep = Math.max(0, remaining - 32);
                if (keep < text.length()) {
                    text = text.substring(0, keep) + "<truncated>";
                    truncated = true;
                }
            }
        }
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

    private boolean isAtBudget() {
        return maxChars > 0 && out.length() >= maxChars;
    }

    private void writeTruncatedString() {
        truncated = true;
        out.append("\"<truncated>\"");
    }
}
