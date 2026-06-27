package com.qin.types;

/**
 * Language-tooling metadata declared in qin.config.js.
 */
public record LanguageConfig(
        String id,
        String extension,
        String server,
        String serverBundle,
        String parser,
        String compiler,
        String ideaLspClient) {
}
