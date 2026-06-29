package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinCsstsCompilerNoFallbackSmokeTestMain {
    private QinCsstsCompilerNoFallbackSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path source = Path.of("src/java/com/qin/runtime/core/QinCsstsCompiler.java")
                .toAbsolutePath()
                .normalize();
        if (!Files.isRegularFile(source)) {
            throw new IllegalStateException("QinCsstsCompiler source not found: " + source);
        }

        String text = Files.readString(source, StandardCharsets.UTF_8);
        for (String forbidden : List.of(
                "__qin_extract_atoms__",
                "__qin_fallback_atoms__",
                "mergePattern = /cssts")) {
            if (text.contains(forbidden)) {
                throw new IllegalStateException("QinCsstsCompiler must use RuntimeStore usedStyles, not atom extraction fallback: "
                        + forbidden);
            }
        }

        System.out.println("QinCsstsCompilerNoFallbackSmokeTestMain OK");
    }
}
