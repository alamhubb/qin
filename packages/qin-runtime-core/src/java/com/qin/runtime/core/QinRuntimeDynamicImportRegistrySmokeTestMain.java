package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;
import com.qin.lang.runtime.QinModuleNamespace;
import com.qin.lang.runtime.QinRuntimeModuleRegistry;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public final class QinRuntimeDynamicImportRegistrySmokeTestMain {
    private QinRuntimeDynamicImportRegistrySmokeTestMain() {
    }

    public static void main(String[] args) {
        QinRuntimeModuleRegistry.clear();
        AtomicInteger value = new AtomicInteger(41);
        AtomicInteger loadCount = new AtomicInteger();
        QinRuntimeModuleRegistry.register("./demo.js", () -> {
            loadCount.incrementAndGet();
            return QinRuntimeModuleRegistry.namespace("./demo.js", Map.of(
                    "value", value::get,
                    "next", () -> value.incrementAndGet()));
        });

        Object imported = JavaEsmGlobal.__qin_dynamic_import__("./demo.js");
        if (!(imported instanceof QinModuleNamespace namespace)) {
            throw new IllegalStateException("Expected QinModuleNamespace, got: " + imported);
        }
        requireEquals(41, namespace.get("value"), "initial live export");
        requireEquals(42, namespace.get("next"), "next export call");
        requireEquals(42, namespace.get("value"), "updated live export");

        Object importedAgain = JavaEsmGlobal.__qin_dynamic_import__("./demo.js");
        if (importedAgain != namespace) {
            throw new IllegalStateException("Dynamic import should reuse cached namespace");
        }
        requireEquals(1, loadCount.get(), "loader invocation count");

        QinRuntimeModuleRegistry.clear();
        System.out.println("QinRuntimeDynamicImportRegistrySmokeTestMain passed.");
    }

    private static void requireEquals(Object expected, Object actual, String label) {
        if (!expected.equals(actual)) {
            throw new IllegalStateException(label + ": expected=" + expected + ", actual=" + actual);
        }
    }
}
