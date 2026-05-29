package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Java-backed Map builtin object for Qin.
 */
public final class JavaEsmMapObject {
    public int size;

    private final LinkedHashMap<JavaEsmIdentityKey, Object> entries = new LinkedHashMap<>();

    public JavaEsmMapObject set(Object key, Object value) {
        entries.put(JavaEsmIdentityKey.of(key), value);
        size = entries.size();
        return this;
    }

    public Object get(Object key) {
        return entries.get(JavaEsmIdentityKey.of(key));
    }

    public boolean has(Object key) {
        return entries.containsKey(JavaEsmIdentityKey.of(key));
    }

    public boolean delete(Object key) {
        Object removed = entries.remove(JavaEsmIdentityKey.of(key));
        size = entries.size();
        return removed != null;
    }

    public void clear() {
        entries.clear();
        size = 0;
    }

    public List<Object> keys() {
        List<Object> keys = new ArrayList<>();
        for (JavaEsmIdentityKey key : entries.keySet()) {
            keys.add(key.value());
        }
        return keys;
    }

    public List<Object> values() {
        return new ArrayList<>(entries.values());
    }

    public List<Object> entries() {
        List<Object> rows = new ArrayList<>();
        for (Map.Entry<JavaEsmIdentityKey, Object> entry : entries.entrySet()) {
            rows.add(new ArrayList<>(List.of(entry.getKey().value(), entry.getValue())));
        }
        return rows;
    }

    List<Map.Entry<Object, Object>> rawEntryList() {
        List<Map.Entry<Object, Object>> raw = new ArrayList<>();
        for (Map.Entry<JavaEsmIdentityKey, Object> entry : entries.entrySet()) {
            raw.add(new AbstractMap.SimpleImmutableEntry<>(entry.getKey().value(), entry.getValue()));
        }
        return raw;
    }
}
