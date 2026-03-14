package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Runtime Object built-ins for Qin JS-style Object object.
 */
public final class QinObject {
    private QinObject() {
    }

    public static Object keys(Object value) {
        Map<String, Object> map = asObject(value);
        return new ArrayList<>(map.keySet());
    }

    public static Object values(Object value) {
        Map<String, Object> map = asObject(value);
        return new ArrayList<>(map.values());
    }

    public static Object entries(Object value) {
        Map<String, Object> map = asObject(value);
        List<List<Object>> entries = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            entries.add(List.of(entry.getKey(), entry.getValue()));
        }
        return entries;
    }

    public static Object hasOwn(Object value, Object key) {
        Map<String, Object> map = asObject(value);
        return map.containsKey(String.valueOf(key));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asObject(Object value) {
        if (value instanceof LinkedHashMap<?, ?> linked) {
            return (Map<String, Object>) linked;
        }
        if (value instanceof Map<?, ?> map) {
            LinkedHashMap<String, Object> copy = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                copy.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return copy;
        }
        throw new IllegalArgumentException("QJS2006 Object built-in expects object/map, got: "
                + (value == null ? "null" : value.getClass().getName()));
    }
}
