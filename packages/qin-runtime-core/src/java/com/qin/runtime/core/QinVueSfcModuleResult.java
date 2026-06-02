package com.qin.runtime.core;

import java.util.Map;

public record QinVueSfcModuleResult(
        String moduleCode,
        String csstsCss,
        String csstsAtomModule,
        Map<String, String> virtualModules) {
    public QinVueSfcModuleResult(String moduleCode, String csstsCss, String csstsAtomModule) {
        this(moduleCode, csstsCss, csstsAtomModule, Map.of());
    }
}
