package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsParserCostMatrixProbeMain {
    private QinOvsParserCostMatrixProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("D:/project/qkyproject/qinall/balance-monitoring").toAbsolutePath().normalize();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected qin.config.js at " + root);
        }
        String mode = args.length > 1 && !args[1].isBlank() ? args[1] : "all";

        QinJsPackageRunner runner = new QinJsPackageRunner();
        if ("all".equals(mode) || "noop".equals(mode)) {
            runCase(runner, root, "noop", """
                ({ ok: true });
                """);
        }
        if ("all".equals(mode) || "ovs_import_compiler".equals(mode)) {
            runCase(runner, root, "ovs_import_compiler", """
                import { vitePluginOvsTransform } from "ovs-compiler";
                ({ type: typeof vitePluginOvsTransform });
                """);
        }
        if ("all".equals(mode) || "ovs_import_parser".equals(mode)) {
            runCase(runner, root, "ovs_import_parser", """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                ({ type: typeof OvsParser });
                """);
        }
        if ("all".equals(mode) || "ovs_construct_parser".equals(mode)) {
            runCase(runner, root, "ovs_construct_parser", """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                const started = Date.now();
                const parser = new OvsParser("div { \\"x\\" }");
                ({ elapsed: Date.now() - started, tokenCount: parser.parsedTokens.length });
                """);
        }
        if ("all".equals(mode) || "ovs_la1".equals(mode)) {
            runCase(runner, root, "ovs_la1", """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                const parser = new OvsParser("div { \\"x\\" }");
                const started = Date.now();
                const token = parser.LA(1);
                ({ elapsed: Date.now() - started, token: token == null ? "" : token.tokenName() });
                """);
        }
        if ("all".equals(mode) || "ovs_parse_tiny".equals(mode)) {
            runCase(runner, root, "ovs_parse_tiny", """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                const parser = new OvsParser("div { \\"x\\" }");
                const started = Date.now();
                const cst = parser.OvsProgram();
                ({ elapsed: Date.now() - started, eof: parser.isEof(), cst: cst == null ? "" : cst.getName() });
                """);
        }
        if ("all".equals(mode) || "parser_matrix".equals(mode)) {
            runCase(runner, root, "parser_matrix", """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                const cases = [
                  ["empty", ""],
                  ["import", "import x from \\"y\\""],
                  ["export_const", "export const x = 1"],
                  ["expr", "1"],
                  ["bare_div_empty", "div { }"],
                  ["bare_div_text", "div { \\"x\\" }"],
                  ["bare_div_number", "div { 1 }"]
                ];
                const results = [];
                for (const item of cases) {
                  const name = item[0];
                  const source = item[1];
                  const parser = new OvsParser(source);
                  const started = Date.now();
                  const cst = parser.OvsProgram();
                  results.push({
                    name,
                    elapsed: Date.now() - started,
                    eof: parser.isEof(),
                    tokenCount: parser.parsedTokens.length,
                    cst: cst == null ? "" : cst.getName(),
                    stats: typeof parser.getOrPredictionStats === "function" ? parser.getOrPredictionStats() : ""
                  });
                }
                results;
                """);
        }
        if ("all".equals(mode) || "parser_matrix_no_prediction".equals(mode)) {
            runCase(runner, root, "parser_matrix_no_prediction", """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";
                const cases = [
                  ["empty", ""],
                  ["import", "import x from \\"y\\""],
                  ["export_const", "export const x = 1"],
                  ["expr", "1"],
                  ["bare_div_empty", "div { }"],
                  ["bare_div_text", "div { \\"x\\" }"],
                  ["bare_div_number", "div { 1 }"]
                ];
                const results = [];
                for (const item of cases) {
                  const name = item[0];
                  const source = item[1];
                  const parser = new OvsParser(source);
                  parser.__qin_field_enableOrPrediction = false;
                  const started = Date.now();
                  const cst = parser.OvsProgram();
                  results.push({
                    name,
                    elapsed: Date.now() - started,
                    eof: parser.isEof(),
                    tokenCount: parser.parsedTokens.length,
                    cst: cst == null ? "" : cst.getName(),
                    stats: typeof parser.getOrPredictionStats === "function" ? parser.getOrPredictionStats() : ""
                  });
                }
                results;
                """);
        }
    }

    private static void runCase(QinJsPackageRunner runner, Path root, String name, String source) throws Exception {
        long started = System.nanoTime();
        Object result = runner.runModuleSource(root, source, "ovs_cost_matrix_" + name);
        long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
        System.out.println("[QinOvsParserCostMatrixProbe] " + name
                + " wallMs=" + elapsedMs
                + " result=" + result);
    }
}
