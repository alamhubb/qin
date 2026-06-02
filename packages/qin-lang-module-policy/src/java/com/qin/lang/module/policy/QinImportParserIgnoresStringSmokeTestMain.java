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

        String viteModuleRunnerSnippet = """
                throw SyntaxError(moduleType === "module" ? `[vite] missing '${rawId}'` : `\\
                CommonJS modules can always be imported via the default export, for example using:

                import pkg from '${rawId}';
                const {${missingBindings.join(", ")}} = pkg;
                `);
                import actual from "./actual.js";
                """;
        var viteImports = new QinImportParser().parse(Path.of("module-runner.js"), viteModuleRunnerSnippet);
        if (viteImports.size() != 1 || !"./actual.js".equals(viteImports.getFirst().moduleSpecifier())) {
            throw new IllegalStateException("Unexpected Vite module-runner imports: " + viteImports);
        }

        System.out.println("QinImportParserIgnoresStringSmokeTestMain passed.");
    }
}
