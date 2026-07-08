package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsOvsArrowBodyProbeMain {
    private QinGeneratedTsOvsArrowBodyProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        String source = """
                const ImportedGrid = () => {
                  return section(class = "summary-grid") {
                    span { "1" }
                  }
                }
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { OvsParser } from "ovs-compiler";

                const source = %s;
                const parser = new OvsParser(source);
                const first = parser.LA(1);
                const firstName = first == null ? "EOF" : first.tokenName();
                const firstValue = first == null ? "" : first.value();
                const beforeIndex = parser.currentTokenIndex();
                parser.OvsProgram();
                const next = parser.LA(1);
                const nextName = next == null ? "EOF" : next.tokenName();
                const nextValue = next == null ? "" : next.value();
                ({
                  firstName,
                  firstValue,
                  beforeIndex,
                  afterIndex: parser.currentTokenIndex(),
                  afterEof: parser.isEof(),
                  nextName,
                  nextValue,
                  parsedTokens: parser.parsedTokens.length
                });
                """.formatted(QinJsPackageRunner.renderJsLiteral(source)), "generated_ts_ovs_arrow_body_probe");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        System.out.println("QinGeneratedTsOvsArrowBodyProbeMain " + QinObjectJsonEncoder.toJson(map));
    }
}
