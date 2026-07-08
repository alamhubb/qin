package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsParserOnlyProbeMain {
    private QinOvsParserOnlyProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        String source = """
                section {
                  span { "ok" }
                }
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import OvsParser from "ovs-compiler/src/parser/OvsParser.ts";

                try {
                  const parser = new OvsParser(%s);
                  const first = parser.LA(1);
                  const cst = parser.OvsProgram();
                  const next = parser.LA(1);
                  ({
                    ok: true,
                    firstName: first == null ? "EOF" : first.tokenName(),
                    firstValue: first == null ? "" : first.value(),
                    cstName: cst == null ? "" : (cst.getName ? cst.getName() : cst.name),
                    tokenCount: parser.parsedTokens.length,
                    afterEof: parser.isEof(),
                    nextName: next == null ? "EOF" : next.tokenName(),
                    nextValue: next == null ? "" : next.value()
                  });
                } catch (error) {
                  const hasGetMessage = error != null && error.getMessage != null;
                  const hasToString = error != null && error.toString != null;
                  ({
                    ok: false,
                    name: error == null ? "" : error.name,
                    message: hasGetMessage ? error.getMessage() : "",
                    text: String(error),
                    detail: hasToString ? error.toString() : "",
                    stack: error == null ? "" : error.stack
                  });
                }
                """.formatted(QinJsPackageRunner.renderJsLiteral(source)), "ovs_parser_only_probe");

        System.out.println("QinOvsParserOnlyProbeMain " + result);
    }
}
