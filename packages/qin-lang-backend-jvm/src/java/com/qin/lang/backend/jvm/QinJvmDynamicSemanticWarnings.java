package com.qin.lang.backend.jvm;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Emits compile-time diagnostics when JVM .class generation falls back to the
 * dynamic JavaEsmGlobal runtime surface.
 */
final class QinJvmDynamicSemanticWarnings {
    private static final Set<String> WARNED_KEYS = ConcurrentHashMap.newKeySet();

    private QinJvmDynamicSemanticWarnings() {
    }

    static void warnJavaEsmGlobalCall(String backend, String methodName) {
        if (!warningsEnabled()) {
            return;
        }
        String key = backend + "::" + methodName;
        if (!WARNED_KEYS.add(key)) {
            return;
        }
        System.err.println("[QinDynamicSemanticWarning] " + backend
                + " emits JavaEsmGlobal." + methodName
                + " while compiling to JVM .class. This means the source or lowering path used dynamic JS semantics: "
                + reason(methodName)
                + " Prefer fixed class fields/methods, explicit interfaces, Map/List operations, or Qin-owned static builtins.");
    }

    static void resetForTest() {
        WARNED_KEYS.clear();
    }

    private static boolean warningsEnabled() {
        String value = System.getProperty("qin.dynamicSemanticWarnings");
        return value == null || !"false".equalsIgnoreCase(value);
    }

    private static String reason(String methodName) {
        return switch (methodName) {
            case "__qin_member_get__" -> "dynamic member lookup on a runtime object shape.";
            case "__qin_global__" -> "dynamic global-object lookup.";
            case "__qin_bind_global__" -> "dynamic global-object binding.";
            case "__qin_value__" -> "runtime wrapper/value unwrapping.";
            case "__qin_call__", "__qin_call_method_array__", "__qin_call_function_definition__" ->
                    "dynamic function or method invocation.";
            case "__qin_binary__", "__qin_logical__", "__qin_conditional__" ->
                    "runtime JS operator semantics instead of statically typed JVM operations.";
            case "__qin_array_literal_array__" -> "runtime JS array literal wrapper construction.";
            default -> "dynamic JavaEsmGlobal runtime helper call.";
        };
    }
}
