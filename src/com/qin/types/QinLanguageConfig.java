package com.qin.types;

/**
 * Qin language server metadata declared in qin.config.js.
 */
public record QinLanguageConfig(
        String sourceExtension,
        String serviceExtension,
        String parserPackage,
        String generatedParserTarget) {
}
