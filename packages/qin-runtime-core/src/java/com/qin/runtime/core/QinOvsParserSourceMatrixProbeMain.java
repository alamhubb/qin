package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsParserSourceMatrixProbeMain {
    private QinOvsParserSourceMatrixProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        String[] sources = {
                "export const ImportedGrid = () => {}\n",
                "export const ImportedGrid = () => { return 1 }\n",
                "export const ImportedGrid = () => { return section(class = \"summary-grid\") { span { \"x\" } } }\n",
                """
                import { ref } from "vue"
                export const ImportedGrid = () => {
                  const count = ref(1)
                  return section(class = "summary-grid") {
                    span { String(count.value) }
                  }
                }
                """
        };

        JavaEsmGlobal.setInterpretedCallCountLimit(20_000);
        try {
            for (int i = 0; i < sources.length; i++) {
                Object result = new QinJsPackageRunner().runModuleSource(root, """
                        import { OvsParser } from "ovs-compiler/src/parser/OvsParser.ts";

                        const source = %s;
                        const parser = new OvsParser(source);
                        const first = parser.LA(1);
                        let status = "ok";
                        let message = "";
                        try {
                          parser.OvsProgram();
                        } catch (error) {
                          status = "error";
                          message = error && error.message ? error.message : String(error);
                        }
                        const next = parser.LA(1);
                        ({
                          status,
                          message,
                          firstName: first == null ? "EOF" : first.tokenName(),
                          firstValue: first == null ? "" : first.value(),
                          afterIndex: parser.currentTokenIndex(),
                          afterEof: parser.isEof(),
                          nextName: next == null ? "EOF" : next.tokenName(),
                          nextValue: next == null ? "" : next.value(),
                          parsedTokens: parser.parsedTokens.length
                        });
                        """.formatted(QinJsPackageRunner.renderJsLiteral(sources[i])),
                        "ovs_parser_source_matrix_" + i);
                System.out.println("source[" + i + "] " + result);
            }
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }
    }
}
