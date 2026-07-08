package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QinOvsParserOnlyHotComparisonProbeMain {
    private QinOvsParserOnlyHotComparisonProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0])
                : Path.of("D:/project/qkyproject/qinall/balance-monitoring");
        root = root.toAbsolutePath().normalize();
        Path sourceRoot = args.length > 1 && !args[1].isBlank() && !args[1].startsWith("--")
                ? root.resolve(args[1]).toAbsolutePath().normalize()
                : root.resolve("app").toAbsolutePath().normalize();
        int rounds = readIntArg(args, "--rounds", 5);
        String rule = readStringArg(args, "--rule", "Program");
        boolean noPrediction = hasArg(args, "--no-prediction");

        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected qin.config.js at " + root);
        }

        List<Map<String, Object>> files;
        try (var stream = Files.walk(sourceRoot)) {
            files = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".ovs"))
                    .map(path -> path.toAbsolutePath().normalize())
                    .sorted(Comparator.comparing(Path::toString))
                    .map(path -> sourceEntry(sourceRoot, path))
                    .toList();
        }
        if (files.isEmpty()) {
            throw new IllegalStateException("No .ovs files found under " + sourceRoot);
        }

        String wrapper = """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";

                const __qinFiles = %s;
                const __qinRounds = %d;
                const __qinRule = %s;
                const __qinNoPrediction = %s;
                const now = () => Date.now();
                const cstNameOf = (cst) => cst == null ? "" : (cst.getName ? cst.getName() : cst.name);
                const tokenNameOf = (token) => token == null ? "EOF" : (token.tokenName ? token.tokenName() : token.tokenName);
                const tokenValueOf = (token) => token == null ? "" : (token.value ? token.value() : token.tokenValue);

                function parseWithRule(parser) {
                  if (__qinRule === "OvsProgram") return parser.OvsProgram();
                  return parser.Program();
                }

                function measureLa(item) {
                  const parser = new OvsParser(item.source);
                  if (__qinNoPrediction) parser.__qin_field_enableOrPrediction = false;
                  const started = now();
                  const token = parser.LA(1);
                  const elapsed = now() - started;
                  return {
                    file: item.file,
                    elapsed,
                    tokenName: tokenNameOf(token),
                    tokenValue: tokenValueOf(token),
                    tokenCount: parser.parsedTokens.length
                  };
                }

                function measureParse(item) {
                  const constructStarted = now();
                  const parser = new OvsParser(item.source);
                  if (__qinNoPrediction) parser.__qin_field_enableOrPrediction = false;
                  const constructed = now();
                  const parseStarted = now();
                  const cst = parseWithRule(parser);
                  const parsed = now();
                  const ok = !!cst && !parser.parserFail && !!parser.isEof();
                  if (!ok) {
                    throw new Error("OVS parser-only probe failed: " + item.file);
                  }
                  return {
                    file: item.file,
                    chars: item.source.length,
                    constructMs: constructed - constructStarted,
                    parseMs: parsed - parseStarted,
                    totalMs: parsed - constructStarted,
                    tokenCount: parser.parsedTokens.length,
                    cstName: cstNameOf(cst),
                    eof: parser.isEof()
                  };
                }

                const laResults = __qinFiles.map(measureLa);
                const rounds = [];
                for (let round = 1; round <= __qinRounds; round++) {
                  const started = now();
                  const files = __qinFiles.map(measureParse);
                  const totalMs = now() - started;
                  rounds.push({ round, totalMs, files });
                }
                const totals = rounds.map((round) => round.totalMs);
                const warmTotals = totals.slice(1);
                function average(values) {
                  let sum = 0;
                  for (const value of values) {
                    sum += value;
                  }
                  return sum / values.length;
                }
                function minValue(values) {
                  let best = values[0];
                  for (const value of values) {
                    if (value < best) best = value;
                  }
                  return best;
                }
                ({
                  rule: __qinRule,
                  noPrediction: __qinNoPrediction,
                  rounds: __qinRounds,
                  files: __qinFiles.length,
                  laResults,
                  totals,
                  best: minValue(totals),
                  avg: average(totals),
                  warmAvg: warmTotals.length > 0 ? average(warmTotals) : average(totals),
                  roundResults: rounds
                });
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(files),
                rounds,
                QinJsPackageRunner.renderJsLiteral(rule),
                noPrediction ? "true" : "false");

        long started = System.nanoTime();
        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "ovs_parser_only_hot_comparison_probe");
        long wallMs = (System.nanoTime() - started) / 1_000_000L;
        System.out.println("[QinOvsParserOnlyHotComparisonProbe] wallMs=" + wallMs + " result=" + result);
    }

    private static Map<String, Object> sourceEntry(Path sourceRoot, Path path) {
        try {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("file", sourceRoot.relativize(path).toString().replace('\\', '/'));
            entry.put("source", Files.readString(path, StandardCharsets.UTF_8));
            return entry;
        } catch (Exception error) {
            throw new IllegalStateException("Failed to read OVS source: " + path, error);
        }
    }

    private static int readIntArg(String[] args, String name, int defaultValue) {
        String text = readStringArg(args, name, null);
        return text == null ? defaultValue : Integer.parseInt(text);
    }

    private static String readStringArg(String[] args, String name, String defaultValue) {
        for (int i = 0; i < args.length - 1; i++) {
            if (name.equals(args[i])) {
                return args[i + 1];
            }
        }
        return defaultValue;
    }

    private static boolean hasArg(String[] args, String name) {
        for (String arg : args) {
            if (name.equals(arg)) {
                return true;
            }
        }
        return false;
    }
}
