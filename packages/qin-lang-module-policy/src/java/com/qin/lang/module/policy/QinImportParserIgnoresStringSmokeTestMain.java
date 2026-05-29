package com.qin.lang.module.policy;

import java.nio.file.Path;

public final class QinImportParserIgnoresStringSmokeTestMain {
    private QinImportParserIgnoresStringSmokeTestMain() {
    }

    public static void main(String[] args) {
        String source = """
                const generated = `
                import { ${CSS_VARS_HELPER} as _${CSS_VARS_HELPER} } from 'vue'
                `;
                import realValue from "./real.js";
                """;
        var imports = new QinImportParser().parse(Path.of("main.js"), source);
        if (imports.size() != 1 || !"./real.js".equals(imports.getFirst().moduleSpecifier())) {
            throw new IllegalStateException("Unexpected imports: " + imports);
        }

        System.out.println("QinImportParserIgnoresStringSmokeTestMain passed.");
    }
}
