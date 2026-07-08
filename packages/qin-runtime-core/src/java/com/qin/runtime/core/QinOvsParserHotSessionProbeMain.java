package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsParserHotSessionProbeMain {
    private QinOvsParserHotSessionProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("D:/project/qkyproject/qinall/balance-monitoring").toAbsolutePath().normalize();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected qin.config.js at " + root);
        }

        String source = "export const X = () => { return div { \"x\" } }";
        String wrapper = """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";

                const input = %s;
                const started = Date.now();
                const parser = new OvsParser(input);
                const cst = parser.OvsProgram();
                const elapsed = Date.now() - started;
                ({
                  elapsed,
                  eof: parser.isEof(),
                  cstName: cst == null ? "" : (cst.getName ? cst.getName() : cst.name),
                  tokenCount: parser.parsedTokens.length
                });
                """.formatted(QinJsPackageRunner.renderJsLiteral(source));

        QinJsPackageRunner runner = new QinJsPackageRunner();
        for (int round = 1; round <= 2; round++) {
            long started = System.nanoTime();
            Object result = runner.runModuleSource(root, wrapper, "ovs_parser_hot_session_probe");
            long wallMs = (System.nanoTime() - started) / 1_000_000L;
            System.out.println("[QinOvsParserHotSessionProbe] round=" + round
                    + " wallMs=" + wallMs
                    + " result=" + result);
        }
    }
}
