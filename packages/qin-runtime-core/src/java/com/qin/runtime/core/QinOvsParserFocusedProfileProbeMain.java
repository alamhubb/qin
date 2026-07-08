package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class QinOvsParserFocusedProfileProbeMain {
    private QinOvsParserFocusedProfileProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        List<String> positional = new ArrayList<>();
        boolean profile = false;
        for (String arg : args) {
            if ("--profile".equals(arg)) {
                profile = true;
            } else {
                positional.add(arg);
            }
        }
        Path root = !positional.isEmpty() && !positional.get(0).isBlank()
                ? Path.of(positional.get(0)).toAbsolutePath().normalize()
                : Path.of("D:/project/qkyproject/qinall/balance-monitoring").toAbsolutePath().normalize();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected qin.config.js at " + root);
        }

        String source = positional.size() > 1 && !positional.get(1).isBlank()
                ? positional.get(1)
                : "export const X = () => { return div { \"x\" } }";
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";

                const parser = new OvsParser(%s);
                const __qin_profile__ = %s;
                if (__qin_profile__) {
                  parser.enableProfile();
                }
                const first = parser.LA(1);
                const started = Date.now();
                const cst = parser.OvsProgram();
                const elapsed = Date.now() - started;
                const next = parser.LA(1);
                ({
                  sourceLength: %s.length,
                  firstName: first == null ? "EOF" : first.tokenName(),
                  firstValue: first == null ? "" : first.value(),
                  cstName: cst == null ? "" : (cst.getName ? cst.getName() : cst.name),
                  tokenCount: parser.parsedTokens.length,
                  elapsed,
                  afterEof: parser.isEof(),
                  nextName: next == null ? "EOF" : next.tokenName(),
                  nextValue: next == null ? "" : next.value(),
                  orPredictionStats: typeof parser.getOrPredictionStats === "function" ? parser.getOrPredictionStats() : "",
                  orPredictionGrammar: typeof parser.getLastOrPredictionGrammar === "function" ? String(parser.getLastOrPredictionGrammar()) : "",
                  orPredictionAmbiguityDiagnostics: typeof parser.getLastOrPredictionAmbiguityDiagnostics === "function" ? String(parser.getLastOrPredictionAmbiguityDiagnostics()) : "",
                  cacheStats: typeof parser.getCacheStats === "function" ? parser.getCacheStats() : "",
                  profileEnabled: __qin_profile__,
                  profile: __qin_profile__ ? parser.getProfileReport() : ""
                });
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(source),
                profile ? "true" : "false",
                QinJsPackageRunner.renderJsLiteral(source)),
                "ovs_parser_focused_profile_probe");

        System.out.println("QinOvsParserFocusedProfileProbeMain " + result);
    }
}
