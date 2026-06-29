package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.file.Path;
import java.util.List;

public final class QinEsmRuntimeFeatureParserScanSmokeTestMain {
    private QinEsmRuntimeFeatureParserScanSmokeTestMain() {
    }

    public static void main(String[] args) {
        expectDiagnostic(
                "ESM3001",
                """
                const demo = { age: 1 };
                import("./dep.js");
                """);
        expectDiagnostic(
                "QIN_JS_UNSUPPORTED_IMPORT_META",
                """
                const url = import.meta.url;
                """);
        expectNoDiagnostic("""
                const text = "import('./not-code.js') import.meta.url";
                // import("./comment.js")
                /* import.meta.url */
                export const ok = text;
                """);
        System.out.println("QinEsmRuntimeFeatureParserScanSmokeTestMain passed.");
    }

    private static void expectDiagnostic(String code, String source) {
        try {
            new QinEsmRuntimeFeatureValidator().validate(graph(source));
        } catch (QinEsmSemanticException error) {
            boolean found = error.diagnostics().stream().anyMatch(diagnostic -> code.equals(diagnostic.code()));
            if (!found) {
                throw new IllegalStateException("Expected diagnostic " + code + " but got: "
                        + error.diagnostics(), error);
            }
            return;
        }
        throw new IllegalStateException("Expected diagnostic " + code);
    }

    private static void expectNoDiagnostic(String source) {
        new QinEsmRuntimeFeatureValidator().validate(graph(source));
    }

    private static QinModuleGraph graph(String source) {
        Path file = Path.of("memory-runtime-feature-smoke.js").toAbsolutePath().normalize();
        return new QinModuleGraph(file, List.of(new QinModuleSource(file, source, List.of())));
    }
}
