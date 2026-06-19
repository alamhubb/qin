package com.qin.lang.runtime;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Java-backed Object builtin subset for Qin.
 */
public final class JavaEsmObject {
    private static final String ACCESSOR_DESCRIPTOR_MARKER = "__qin_accessor_descriptor";

    private JavaEsmObject() {
    }

    public static Object keys(Object value) {
        return new ArrayList<>(enumerableEntries(value).keySet());
    }

    public static Object values(Object value) {
        return new ArrayList<>(enumerableEntries(value).values());
    }

    public static Object entries(Object value) {
        List<Object> entries = new ArrayList<>();
        for (Map.Entry<String, Object> entry : enumerableEntries(value).entrySet()) {
            entries.add(new ArrayList<>(List.of(entry.getKey(), entry.getValue())));
        }
        return entries;
    }

    public static Object fromEntries(Object value) {
        LinkedHashMap<String, Object> object = new LinkedHashMap<>();
        if (value == null) {
            return object;
        }
        Iterable<?> iterable;
        if (value instanceof Iterable<?> rawIterable) {
            iterable = rawIterable;
        } else if (value.getClass().isArray()) {
            List<Object> items = new ArrayList<>();
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                items.add(java.lang.reflect.Array.get(value, i));
            }
            iterable = items;
        } else {
            iterable = enumerableEntries(value).values();
        }
        for (Object item : iterable) {
            Object key = null;
            Object entryValue = null;
            if (item instanceof List<?> list) {
                key = list.isEmpty() ? null : list.get(0);
                entryValue = list.size() < 2 ? null : list.get(1);
            } else if (item instanceof Object[] array) {
                key = array.length == 0 ? null : array[0];
                entryValue = array.length < 2 ? null : array[1];
            } else if (item instanceof Map<?, ?> map) {
                key = map.get("0");
                entryValue = map.get("1");
            }
            if (key != null) {
                object.put(String.valueOf(key), entryValue);
            }
        }
        return object;
    }

    public static Object hasOwn(Object value, Object key) {
        return enumerableEntries(value).containsKey(String.valueOf(key));
    }

    public static Object getOwnPropertyDescriptor(Object value, Object key) {
        Map<String, Object> entries = enumerableEntries(value);
        String property = String.valueOf(key);
        if (!entries.containsKey(property)) {
            return null;
        }
        return descriptor(entries.get(property));
    }

    public static Object getOwnPropertyDescriptors(Object value) {
        LinkedHashMap<String, Object> descriptors = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : enumerableEntries(value).entrySet()) {
            descriptors.put(entry.getKey(), descriptor(entry.getValue()));
        }
        return descriptors;
    }

    public static Object getOwnPropertySymbols(Object value) {
        List<Object> symbols = new ArrayList<>();
        for (String key : enumerableEntries(value).keySet()) {
            Object symbol = JavaEsmSymbol.fromInternalPropertyKey(key);
            if (symbol != null) {
                symbols.add(symbol);
            }
        }
        return symbols;
    }

    public static Object assign(Object target, Object... sources) {
        if (target == null) {
            throw new IllegalArgumentException("Object.assign target cannot be null");
        }
        for (Object source : sources) {
            for (Map.Entry<String, Object> entry : enumerableEntries(source).entrySet()) {
                JavaEsmGlobal.__qin_member_set__(target, entry.getKey(), entry.getValue());
            }
        }
        return target;
    }

    public static Object assign(Object target, Object source) {
        return assign(target, new Object[] {source});
    }

    public static Object freeze(Object value) {
        return value;
    }

    public static Object defineProperties(Object target, Object descriptors) {
        if (target == null) {
            throw new IllegalArgumentException("Object.defineProperties target cannot be null");
        }
        for (Map.Entry<String, Object> entry : enumerableEntries(descriptors).entrySet()) {
            defineProperty(target, entry.getKey(), entry.getValue());
        }
        return target;
    }

    public static Object create(Object prototype) {
        return new LinkedHashMap<String, Object>();
    }

    public static Object fromEntry(Object key, Object value) {
        LinkedHashMap<String, Object> object = new LinkedHashMap<>();
        object.put(String.valueOf(key), value);
        return object;
    }

    public static Object defineProperty(Object target, Object key, Object descriptor) {
        if (target == null) {
            throw new IllegalArgumentException("Object.defineProperty target cannot be null");
        }
        if (!(descriptor instanceof Map<?, ?> descriptorMap)) {
            return target;
        }
        if (descriptorMap.containsKey("value")) {
            JavaEsmGlobal.__qin_member_set__(target, key, descriptorMap.get("value"));
            return target;
        }
        if (descriptorMap.containsKey("get")) {
            JavaEsmGlobal.__qin_member_set__(target, key, accessorDescriptor(descriptorMap));
        }
        return target;
    }

    private static Map<String, Object> descriptor(Object value) {
        if (isAccessorDescriptor(value)) {
            return accessorDescriptorMetadata(castMap(value));
        }
        LinkedHashMap<String, Object> descriptor = new LinkedHashMap<>();
        descriptor.put("value", value);
        descriptor.put("writable", true);
        descriptor.put("enumerable", true);
        descriptor.put("configurable", true);
        return descriptor;
    }

    static Object resolveStoredPropertyValue(Object value) {
        if (!isAccessorDescriptor(value)) {
            return value;
        }
        Object getter = castMap(value).get("get");
        return getter == null ? null : JavaEsmGlobal.callRuntimeCallable(getter);
    }

    private static Map<String, Object> accessorDescriptor(Map<?, ?> descriptorMap) {
        LinkedHashMap<String, Object> stored = new LinkedHashMap<>();
        stored.put(ACCESSOR_DESCRIPTOR_MARKER, true);
        stored.put("get", descriptorMap.get("get"));
        stored.put("set", descriptorMap.get("set"));
        stored.put("enumerable", descriptorMap.containsKey("enumerable")
                ? descriptorMap.get("enumerable")
                : true);
        stored.put("configurable", descriptorMap.containsKey("configurable")
                ? descriptorMap.get("configurable")
                : true);
        return stored;
    }

    private static Map<String, Object> accessorDescriptorMetadata(Map<String, Object> stored) {
        LinkedHashMap<String, Object> descriptor = new LinkedHashMap<>();
        descriptor.put("get", stored.get("get"));
        descriptor.put("set", stored.get("set"));
        descriptor.put("enumerable", stored.getOrDefault("enumerable", true));
        descriptor.put("configurable", stored.getOrDefault("configurable", true));
        return descriptor;
    }

    private static boolean isAccessorDescriptor(Object value) {
        return value instanceof Map<?, ?> map && Boolean.TRUE.equals(map.get(ACCESSOR_DESCRIPTOR_MARKER));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castMap(Object value) {
        return (Map<String, Object>) value;
    }

    static Map<String, Object> enumerableEntries(Object value) {
        value = JavaEsmGlobal.__qin_value__(value);
        LinkedHashMap<String, Object> entries = new LinkedHashMap<>();
        if (value == null) {
            return entries;
        }
        if (value instanceof Number || value instanceof Boolean || value instanceof Character) {
            return entries;
        }
        Map<String, Object> runtimeEntries = JavaEsmGlobal.__qin_own_enumerable_entries__(value);
        if (runtimeEntries != null) {
            entries.putAll(runtimeEntries);
            return entries;
        }
        if (value instanceof JavaEsmMapObject mapObject) {
            for (Map.Entry<Object, Object> entry : mapObject.rawEntryList()) {
                entries.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            return entries;
        }
        if (value instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                if (JavaEsmGlobal.isRuntimeHiddenObjectKey(key)) {
                    continue;
                }
                entries.put(key, resolveStoredPropertyValue(entry.getValue()));
            }
            return entries;
        }
        if (value instanceof Iterable<?> iterable) {
            int index = 0;
            for (Object item : iterable) {
                entries.put(String.valueOf(index++), item);
            }
            return entries;
        }
        if (value.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            for (int i = 0; i < length; i++) {
                entries.put(String.valueOf(i), java.lang.reflect.Array.get(value, i));
            }
            return entries;
        }
        Class<?> current = value.getClass();
        while (current != null && current != Object.class) {
            for (java.lang.reflect.Field field : current.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                if (entries.containsKey(field.getName())) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    entries.put(field.getName(), field.get(value));
                } catch (IllegalAccessException ignored) {
                    entries.put(field.getName(), null);
                }
            }
            current = current.getSuperclass();
        }
        return entries;
    }
}
