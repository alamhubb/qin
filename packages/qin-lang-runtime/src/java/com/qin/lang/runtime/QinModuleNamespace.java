package com.qin.lang.runtime;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Runtime ESM module namespace for Qin-on-JVM.
 *
 * <p>Values are supplied lazily so exported bindings can stay live instead of
 * being copied at namespace creation time.
 */
public final class QinModuleNamespace {
    private final String moduleId;
    private final Map<String, Supplier<Object>> exports;

    QinModuleNamespace(String moduleId, Map<String, Supplier<Object>> exports) {
        this.moduleId = Objects.requireNonNull(moduleId, "moduleId cannot be null");
        Objects.requireNonNull(exports, "exports cannot be null");
        this.exports = Map.copyOf(exports);
    }

    public String moduleId() {
        return moduleId;
    }

    public Set<String> exportedNames() {
        return exports.keySet();
    }

    public Object get(Object name) {
        Supplier<Object> supplier = exports.get(String.valueOf(name));
        if (supplier == null) {
            return null;
        }
        return supplier.get();
    }

    public Map<String, Object> snapshot() {
        Map<String, Object> snapshot = new LinkedHashMap<>();
        for (Map.Entry<String, Supplier<Object>> entry : exports.entrySet()) {
            snapshot.put(entry.getKey(), entry.getValue().get());
        }
        return snapshot;
    }
}
