package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticModel;
import com.qin.parser.QinParsedSource;
import com.qin.parser.QinParserFacade;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinMinimalEsmSemanticTimingProbeMain {
    private static final String SOURCE = "export const result = 1\n";

    private QinMinimalEsmSemanticTimingProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        String mode = args.length == 0 ? "all" : args[0];
        if ("parser".equals(mode) || "all".equals(mode)) {
            runParserRounds();
        }
        if ("semantic".equals(mode) || "all".equals(mode)) {
            runSemanticRounds();
        }
        System.out.println("QinMinimalEsmSemanticTimingProbeMain OK mode=" + mode);
    }

    private static void runParserRounds() {
        QinParserFacade parser = new QinParserFacade();
        parseRound(parser, "parser-cold");
        parseRound(parser, "parser-warm");
    }

    private static void parseRound(QinParserFacade parser, String label) {
        long started = System.nanoTime();
        QinParsedSource parsed = parser.parseSource(SOURCE);
        long elapsedMs = elapsedMs(started);
        if (!parsed.hasProgram()) {
            throw new IllegalStateException("Expected parser Program for " + label);
        }
        System.out.println("QinMinimalEsmSemanticTimingProbeMain " + label + " elapsedMs=" + elapsedMs);
    }

    private static void runSemanticRounds() throws Exception {
        Path root = Files.createTempDirectory("qin-minimal-esm-sema-");
        Path entry = root.resolve("entry.ts");
        Files.writeString(entry, SOURCE, StandardCharsets.UTF_8);
        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticAnalyzer analyzer = new QinEsmSemanticAnalyzer();
        semanticRound(analyzer, graph, "semantic-cold");
        semanticRound(analyzer, graph, "semantic-warm");
    }

    private static void semanticRound(QinEsmSemanticAnalyzer analyzer, QinModuleGraph graph, String label) {
        long started = System.nanoTime();
        QinEsmSemanticModel model = analyzer.analyze(graph);
        long elapsedMs = elapsedMs(started);
        if (model.modules().size() != 1) {
            throw new IllegalStateException("Expected one semantic module for " + label + ", got "
                    + model.modules().size());
        }
        System.out.println("QinMinimalEsmSemanticTimingProbeMain " + label + " elapsedMs=" + elapsedMs);
    }

    private static long elapsedMs(long started) {
        return (System.nanoTime() - started) / 1_000_000L;
    }
}
