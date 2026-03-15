package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleSource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Guards runtime-level ESM features that are not implemented yet.
 */
public final class QinEsmRuntimeFeatureValidator {
    private static final Pattern UNSUPPORTED_EXPORT_DECLARATION_PATTERN = Pattern.compile(
            "(?m)^\\s*export\\s+(let|var|function|class)\\b");

    public void validate(QinModuleGraph graph) {
        List<QinEsmDiagnostic> diagnostics = new ArrayList<>();
        for (QinModuleSource module : graph.modules()) {
            scanOne(module, diagnostics);
        }
        if (!diagnostics.isEmpty()) {
            throw new QinEsmSemanticException(diagnostics);
        }
    }

    private void scanOne(QinModuleSource module, List<QinEsmDiagnostic> diagnostics) {
        addIfMatched(module, diagnostics, UNSUPPORTED_EXPORT_DECLARATION_PATTERN, "ESM3004",
                "Only `export const` / re-export forms are currently executable in Qin runtime.");
    }

    private void addIfMatched(
            QinModuleSource module,
            List<QinEsmDiagnostic> diagnostics,
            Pattern pattern,
            String code,
            String message) {
        Matcher matcher = pattern.matcher(module.source());
        if (!matcher.find()) {
            return;
        }
        int[] lineCol = lineCol(module.source(), matcher.start());
        diagnostics.add(new QinEsmDiagnostic(
                code,
                message,
                module.file(),
                lineCol[0],
                lineCol[1]));
    }

    private int[] lineCol(String source, int index) {
        int line = 1;
        int col = 1;
        for (int i = 0; i < index && i < source.length(); i++) {
            char ch = source.charAt(i);
            if (ch == '\n') {
                line++;
                col = 1;
            } else {
                col++;
            }
        }
        return new int[] {line, col};
    }
}
