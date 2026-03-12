package com.qin.runtime.core;

import java.util.Set;

/**
 * Policy boundary for JDK interop access control.
 */
public final class QinJdkInteropPolicy {
    private final Set<String> allowedModulePrefixes;

    public QinJdkInteropPolicy() {
        this(Set.of("java.lang", "java.util", "java.nio.file"));
    }

    public QinJdkInteropPolicy(Set<String> allowedModulePrefixes) {
        this.allowedModulePrefixes = Set.copyOf(allowedModulePrefixes);
    }

    public boolean isModuleAllowed(String javaModule) {
        return allowedModulePrefixes.stream()
                .anyMatch(prefix -> javaModule.equals(prefix) || javaModule.startsWith(prefix + "."));
    }
}
