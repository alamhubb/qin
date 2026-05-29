package com.qin.lang.runtime;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Registry for externalized Qin runtime function AST models.
 *
 * <p>Large npm/compiler packages should not force every function AST model into
 * one generated class. This registry is the runtime boundary for future
 * function-model artifacts emitted beside compiled module classes.
 */
public final class QinFunctionModelRegistry {
    private static final Map<String, Supplier<Map<String, Object>>> MODELS = new ConcurrentHashMap<>();

    private QinFunctionModelRegistry() {
    }

    public static void register(String modelId, Supplier<Map<String, Object>> supplier) {
        MODELS.put(normalizeModelId(modelId), Objects.requireNonNull(supplier, "supplier cannot be null"));
    }

    public static Map<String, Object> resolve(Object modelId) {
        String key = normalizeModelId(String.valueOf(modelId));
        Supplier<Map<String, Object>> supplier = MODELS.get(key);
        if (supplier == null) {
            throw new IllegalStateException(
                    "Qin function model artifact is not registered: " + key
                            + ". Compile this module through the function-model artifact pipeline first.");
        }
        Map<String, Object> model = supplier.get();
        if (model == null) {
            throw new IllegalStateException("Qin function model artifact returned null: " + key);
        }
        return model;
    }

    public static void clear() {
        MODELS.clear();
    }

    private static String normalizeModelId(String modelId) {
        String key = modelId == null ? "" : modelId.trim();
        if (key.isBlank()) {
            throw new IllegalArgumentException("modelId cannot be blank");
        }
        return key;
    }
}
