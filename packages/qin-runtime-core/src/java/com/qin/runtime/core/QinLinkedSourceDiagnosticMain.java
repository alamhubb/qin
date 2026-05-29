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
            throw new IllegalArgumentException("Usage: QinLinkedSourceDiagnosticMain <entry-file> <output-file>");
        }
        Path entryFile = Path.of(args[0]).toAbsolutePath().normalize();
        Path outputFile = Path.of(args[1]).toAbsolutePath().normalize();
        QinModuleGraph graph = new QinModuleGraphBuilder().build(entryFile);
        QinLinkedModuleSource linkedSource = new QinLinkedModuleSourceEmitter().emit(graph);
        if (outputFile.getParent() != null) {
            Files.createDirectories(outputFile.getParent());
        }
        Files.writeString(outputFile, linkedSource.source(), StandardCharsets.UTF_8);
        System.out.println("entry=" + entryFile);
        System.out.println("modules=" + linkedSource.modules().size());
        System.out.println("length=" + linkedSource.source().length());
        System.out.println("output=" + outputFile);
    }
}
