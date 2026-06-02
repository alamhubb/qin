package com.qin.types;

public record BackendConfig(
        String sourceDir,
        String entry) {

    public BackendConfig {
        sourceDir = sourceDir != null && !sourceDir.isBlank() ? sourceDir : "main";
        entry = entry != null && !entry.isBlank() ? entry : null;
    }

    public BackendConfig() {
        this(null, null);
    }
}
