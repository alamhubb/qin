package com.qin.types;

import java.util.List;

/**
 * Generated package metadata declared in qin.config.js.
 */
public record GeneratedConfig(
        String source,
        String entryBinaryName,
        List<String> additionalEntryBinaryNames,
        List<String> sourceRoots,
        String outputDir) {
    public GeneratedConfig {
        additionalEntryBinaryNames = additionalEntryBinaryNames != null
                ? List.copyOf(additionalEntryBinaryNames)
                : List.of();
        sourceRoots = sourceRoots != null ? List.copyOf(sourceRoots) : List.of();
    }
}
