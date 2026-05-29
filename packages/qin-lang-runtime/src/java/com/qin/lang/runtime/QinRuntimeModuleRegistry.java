package com.qin.lang.runtime;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * JVM runtime registry for dynamically loadable Qin modules.
 *
 * <p>This is the first stable runtime boundary for future module-level class
 * cache support. The registry intentionally does not compile modules by itself;
 * compilers/package caches register loadable modules here.
 */
public final class QinRuntimeModuleRegistry {
    private static final Map<String, Supplier<QinModuleNamespace>> LOADERS = new ConcurrentHashMap<>();
    private static final Map<String, QinModuleNamespace> CACHE = new ConcurrentHashMap<>();

    private QinRuntimeModuleRegistry() {
    }

    public static void register(String moduleId, Supplier<QinModuleNamespace> loader) {
        String key = normalizeModuleId(moduleId);
        LOADERS.put(key, Objects.requireNonNull(loader, "loader cannot be null"));
        CACHE.remove(key);
    }

    public static QinModuleNamespace namespace(String moduleId, Map<String, Supplier<Object>> exports) {
        return new QinModuleNamespace(normalizeModuleId(moduleId), new LinkedHashMap<>(exports));
    }

    public static QinModuleNamespace importModule(Object specifier) {
        String key = normalizeModuleId(String.valueOf(specifier));
        Supplier<QinModuleNamespace> loader = LOADERS.get(key);
        if (loader == null) {
            throw new UnsupportedOperationException(
                    "dynamic import module is not registered for JVM runtime: " + key
                            + ". Compile this dependency through module-level/class-cache pipeline first.");
        }
        return CACHE.computeIfAbsent(key, ignored -> {
            QinModuleNamespace namespace = loader.get();
            if (namespace == null) {
                throw new IllegalStateException("Dynamic module loader returned null namespace: " + key);
            }
            return namespace;
        });
    }

    public static void clear() {
        LOADERS.clear();
        CACHE.clear();
    }

    private static String normalizeModuleId(String moduleId) {
        String key = moduleId == null ? "" : moduleId.trim().replace('\\', '/');
        if (key.isBlank()) {
            throw new IllegalArgumentException("moduleId cannot be blank");
        }
        return key;
    }
}
