package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;
import com.qin.lang.runtime.QinModuleNamespace;
import com.qin.lang.runtime.QinRuntimeModuleRegistry;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class QinRuntimeDynamicImportRegistrySmokeTestMain {
    private static final AtomicReference<Object> IMPORTED_VALUE = new AtomicReference<>();

    private QinRuntimeDynamicImportRegistrySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinRuntimeModuleRegistry.clear();
        AtomicInteger value = new AtomicInteger(41);
        AtomicInteger loadCount = new AtomicInteger();
        QinRuntimeModuleRegistry.register("./demo.js", () -> {
            loadCount.incrementAndGet();
            return QinRuntimeModuleRegistry.namespace("./demo.js", Map.of(
                    "value", value::get,
                    "next", () -> value.incrementAndGet()));
        });

        Method captureImported = QinRuntimeDynamicImportRegistrySmokeTestMain.class
                .getDeclaredMethod("captureImported", Object.class);

        Object promise = JavaEsmGlobal.__qin_dynamic_import__("./demo.js");
        JavaEsmGlobal.__qin_call_method__(promise, "then", captureImported);
        Object imported = IMPORTED_VALUE.get();
        if (!(imported instanceof QinModuleNamespace namespace)) {
            throw new IllegalStateException("Expected dynamic import promise to fulfill QinModuleNamespace, got: "
                    + imported);
        }
        requireEquals(41, namespace.get("value"), "initial live export");
        requireEquals(42, namespace.get("next"), "next export call");
        requireEquals(42, namespace.get("value"), "updated live export");

        IMPORTED_VALUE.set(null);
        Object importedAgainPromise = JavaEsmGlobal.__qin_dynamic_import__("./demo.js");
        JavaEsmGlobal.__qin_call_method__(importedAgainPromise, "then", captureImported);
        Object importedAgain = IMPORTED_VALUE.get();
        if (importedAgain != namespace) {
            throw new IllegalStateException("Dynamic import should reuse cached namespace");
        }
        requireEquals(1, loadCount.get(), "loader invocation count");

        IMPORTED_VALUE.set(null);
        Object hostPromise = JavaEsmGlobal.__qin_dynamic_import__("node:diagnostics_channel");
        JavaEsmGlobal.__qin_call_method__(hostPromise, "then", captureImported);
        requireEquals("node:diagnostics_channel", IMPORTED_VALUE.get(), "host namespace fulfillment");

        QinRuntimeModuleRegistry.clear();
        System.out.println("QinRuntimeDynamicImportRegistrySmokeTestMain passed.");
    }

    public static Object captureImported(Object value) {
        IMPORTED_VALUE.set(value);
        return value;
    }

    private static void requireEquals(Object expected, Object actual, String label) {
        if (!expected.equals(actual)) {
            throw new IllegalStateException(label + ": expected=" + expected + ", actual=" + actual);
        }
    }
}
