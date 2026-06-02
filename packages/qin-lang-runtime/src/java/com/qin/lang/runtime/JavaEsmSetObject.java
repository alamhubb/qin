package com.qin.lang.runtime;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Java-backed Set builtin object for Qin.
 */
public final class JavaEsmSetObject {
    public int size;

    private final LinkedHashSet<JavaEsmIdentityKey> values = new LinkedHashSet<>();

    public JavaEsmSetObject() {
    }

    public JavaEsmSetObject(Object initialValues) {
        if (initialValues == null) {
            return;
        }
        if (initialValues instanceof JavaEsmSetObject setObject) {
            for (Object value : setObject.values()) {
                add(value);
            }
            return;
        }
        if (initialValues instanceof Iterable<?> iterable) {
            for (Object value : iterable) {
                add(value);
            }
            return;
        }
        throw new IllegalArgumentException("Set constructor expects iterable value, got: "
                + initialValues.getClass().getName());
    }

    public JavaEsmSetObject add(Object value) {
        values.add(JavaEsmIdentityKey.of(value));
        size = values.size();
        return this;
    }

    public boolean has(Object value) {
        return values.contains(JavaEsmIdentityKey.of(value));
    }

    public boolean delete(Object value) {
        boolean removed = values.remove(JavaEsmIdentityKey.of(value));
        size = values.size();
        return removed;
    }

    public void clear() {
        values.clear();
        size = 0;
    }

    public Object forEach(Object callback) {
        for (JavaEsmIdentityKey value : values) {
            JavaEsmGlobal.callRuntimeCallable(callback, value.value(), value.value(), this);
        }
        return null;
    }

    public List<Object> values() {
        List<Object> result = new ArrayList<>();
        for (JavaEsmIdentityKey value : values) {
            result.add(value.value());
        }
        return result;
    }
}
