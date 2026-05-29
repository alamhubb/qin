package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsCompilerNumericAtomSmokeTestMain {
    private QinCsstsCompilerNumericAtomSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-numeric-atoms-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-cssts-numeric-atoms\" }\n", StandardCharsets.UTF_8);
        String source = """
                const panel = css { padding32px, borderRadius16px, padding24px }
                """;
        QinCsstsCompiler.QinCsstsCompileResult result = new QinCsstsCompiler().compile(root, source);
        if (!result.code().contains("csstsAtom.padding32px")
                || !result.code().contains("csstsAtom.borderRadius16px")
                || !result.code().contains("csstsAtom.padding24px")) {
            throw new IllegalStateException("Numeric atoms were not rewritten through csstsAtom:\n" + result.code());
        }
        if (!result.atomModule().contains("padding32px")
                || !result.atomModule().contains("borderRadius16px")
                || !result.atomModule().contains("padding24px")) {
            throw new IllegalStateException("Numeric atoms were not emitted in csstsAtom module:\n" + result.atomModule());
        }
        if (!result.css().contains("padding: 32px")
                || !result.css().contains("border-radius: 16px")
                || !result.css().contains("padding: 24px")) {
            throw new IllegalStateException("Numeric atoms were not emitted in CSS:\n" + result.css());
        }
        System.out.println("QinCsstsCompilerNumericAtomSmokeTestMain passed.");
    }
}
