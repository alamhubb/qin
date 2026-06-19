package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.module.resolver.QinLinkedModuleSourceEmitter;
import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinLinkedSourceDiagnosticMain {
    private QinLinkedSourceDiagnosticMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException(
                    "Usage: QinLinkedSourceDiagnosticMain <entry-file> <output-file> [section-output-dir]");
        }
        Path entryFile = Path.of(args[0]).toAbsolutePath().normalize();
        Path outputFile = Path.of(args[1]).toAbsolutePath().normalize();
        Path sectionOutputDir = args.length >= 3 ? Path.of(args[2]).toAbsolutePath().normalize() : null;
        QinModuleGraph graph = new QinModuleGraphBuilder().build(entryFile);
        QinLinkedModuleSource linkedSource = new QinLinkedModuleSourceEmitter().emit(graph);
        if (outputFile.getParent() != null) {
            Files.createDirectories(outputFile.getParent());
        }
        Files.writeString(outputFile, linkedSource.source(), StandardCharsets.UTF_8);
        if (sectionOutputDir != null) {
            Files.createDirectories(sectionOutputDir);
            for (var section : linkedSource.moduleSections()) {
                Path sectionFile = sectionOutputDir.resolve(String.format(
                        "%03d-%s.js",
                        section.index(),
                        sanitizeSectionName(section.file().getFileName().toString())));
                Files.writeString(sectionFile, section.classSource(), StandardCharsets.UTF_8);
            }
        }
        System.out.println("entry=" + entryFile);
        System.out.println("modules=" + linkedSource.modules().size());
        for (var module : linkedSource.modules()) {
            System.out.println("module=" + module);
        }
        System.out.println("length=" + linkedSource.source().length());
        System.out.println("output=" + outputFile);
        if (sectionOutputDir != null) {
            System.out.println("sections=" + sectionOutputDir);
        }
    }

    private static String sanitizeSectionName(String value) {
        String sanitized = value == null ? "module" : value.replaceAll("[^A-Za-z0-9._-]", "_");
        return sanitized.isBlank() ? "module" : sanitized;
    }
}
