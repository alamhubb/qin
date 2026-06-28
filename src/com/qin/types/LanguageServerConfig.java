package com.qin.types;

/**
 * Shared LSP language-server metadata declared in qin.config.js.
 */
public record LanguageServerConfig(
        String sourceExtension,
        String serviceExtension,
        String generatedParserTarget,
        String parserPackage,
        String compilerPackage) {
}
