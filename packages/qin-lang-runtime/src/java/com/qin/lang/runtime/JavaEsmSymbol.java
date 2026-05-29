package com.qin.lang.runtime;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Phase-1 Symbol builtin for Qin's Java-backed ESM runtime.
 */
public final class JavaEsmSymbol {
    private static final AtomicLong SEQUENCE = new AtomicLong();
    private static final Map<String, JavaSymbol> REGISTRY = new ConcurrentHashMap<>();
    private static final Map<Long, JavaSymbol> BY_ID = new ConcurrentHashMap<>();

    private JavaEsmSymbol() {
    }

    public static Object create(Object description) {
        String text = description == null ? null : String.valueOf(description);
        JavaSymbol symbol = new JavaSymbol(SEQUENCE.incrementAndGet(), text, false);
        BY_ID.put(symbol.id(), symbol);
        return symbol;
    }

    public static Object for_(Object key) {
        String text = String.valueOf(key);
        return REGISTRY.computeIfAbsent(text, ignored -> {
            JavaSymbol symbol = new JavaSymbol(SEQUENCE.incrementAndGet(), text, true);
            BY_ID.put(symbol.id(), symbol);
            return symbol;
        });
    }

    public static Object keyFor(Object symbol) {
        if (symbol instanceof JavaSymbol javaSymbol && javaSymbol.global()) {
            return javaSymbol.description();
        }
        return null;
    }

    static Object fromInternalPropertyKey(String key) {
        if (key == null || !key.startsWith("\u0000symbol:")) {
            return null;
        }
        try {
            return BY_ID.get(Long.parseLong(key.substring("\u0000symbol:".length())));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    public record JavaSymbol(long id, String description, boolean global) {
        @Override
        public String toString() {
            return description == null ? "Symbol()" : "Symbol(" + description + ")";
        }
    }
}
