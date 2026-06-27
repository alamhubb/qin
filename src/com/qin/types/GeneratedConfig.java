package com.qin.types;

import java.util.List;

/**
 * Generated package metadata declared in qin.config.js.
 */
public record GeneratedConfig(
        String source,
        String entryBinaryName,
        List<String> sourceRoots,
        String outputDir) {
    public GeneratedConfig {
        sourceRoots = sourceRoots != null ? List.copyOf(sourceRoots) : List.of();
    }
}
