package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

final class JavaEsmArrayObject extends ArrayList<Object> {
    private final Map<String, Object> properties = new LinkedHashMap<>();

    JavaEsmArrayObject() {
        super();
    }

    JavaEsmArrayObject(Iterable<?> values) {
        for (Object value : values) {
            add(value);
        }
    }

    Object memberGet(Object property) {
        String key = String.valueOf(property);
        if (properties.containsKey(key)) {
            return properties.get(key);
        }
        return JavaEsmArray.memberGet(this, property);
    }

    Object memberSet(Object property, Object value) {
        String key = String.valueOf(property);
        int index = toIndex(property);
        if ("length".equals(key) || index >= 0) {
            JavaEsmArray.memberSet(this, property, value);
            return value;
        }
        properties.put(key, value);
        return value;
    }

    boolean memberDelete(Object property) {
        String key = String.valueOf(property);
        int index = toIndex(property);
        if (index >= 0 && index < size()) {
            set(index, null);
            return true;
        }
        properties.remove(key);
        return true;
    }

    private static int toIndex(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return (int) Double.parseDouble(text.trim());
            } catch (NumberFormatException ignored) {
                return -1;
            }
        }
        return -1;
    }
}
