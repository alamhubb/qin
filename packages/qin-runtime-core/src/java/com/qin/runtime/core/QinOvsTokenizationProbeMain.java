package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsTokenizationProbeMain {
    private QinOvsTokenizationProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { OvsParser } from "ovs-compiler/src/parser/OvsParser.ts";

                const parser = new OvsParser(`section(class = "x") { span { "y" } }`);
                const names = [];
                for (let i = 1; i <= 12; i++) {
                  const token = parser.LA(i);
                  names.push(token == null ? "EOF" : `${token.tokenName()}:${token.value()}`);
                }
                names.join("|");
                """, "ovs_tokenization_probe");

        System.out.println("QinOvsTokenizationProbeMain " + result);
    }
}
