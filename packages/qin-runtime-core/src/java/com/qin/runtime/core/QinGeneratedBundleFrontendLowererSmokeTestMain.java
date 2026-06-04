package com.qin.runtime.core;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public final class QinGeneratedBundleFrontendLowererSmokeTestMain {
    private QinGeneratedBundleFrontendLowererSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path bundle = Path.of("").toAbsolutePath()
                .resolve(".qin/generated/slime-parser/slime-parser.bundle.js");
        assertContains(bundle);
        Path parserInputProbe = Path.of("").toAbsolutePath()
                .resolve(".qin/generated/slime-parser/parser-input-probe.js");
        if (Files.exists(parserInputProbe)) {
            assertContains(parserInputProbe);
        }
        System.out.println("QinGeneratedBundleFrontendLowererSmokeTestMain OK");
    }

    private static void assertContains(Path sourceFile) throws Exception {
        String source = Files.readString(sourceFile);
        QinIrProgram program = new QinFrontendLowerer().lowerSource(source + "\nconst __qin_bundle_done = true;\n");
        Set<String> names = program.declarations().stream()
                .map(declaration -> declaration.name())
                .collect(Collectors.toSet());
        require(names.contains("com_subhuti_struct_SubhutiTokenContextConstraint"), "context constraint declaration");
        require(names.contains("com_subhuti_struct_SubhutiTokenLookahead"), "token lookahead declaration");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
