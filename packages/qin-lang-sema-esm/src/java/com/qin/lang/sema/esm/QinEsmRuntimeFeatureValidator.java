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
            "(?m)^\\s*export\\s+(let|var)\\b");
    private static final Pattern DYNAMIC_IMPORT_PATTERN = Pattern.compile("\\bimport\\s*\\(");
    private final boolean allowRuntimeDynamicImport;

    public QinEsmRuntimeFeatureValidator() {
        this(true);
    }

    private QinEsmRuntimeFeatureValidator(boolean allowRuntimeDynamicImport) {
        this.allowRuntimeDynamicImport = allowRuntimeDynamicImport;
    }

    public static QinEsmRuntimeFeatureValidator forBrowserFrontend() {
        return new QinEsmRuntimeFeatureValidator(true);
    }

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
        if (!allowRuntimeDynamicImport) {
            addDynamicImportIfMatched(module, diagnostics);
        }
    }

    private void addDynamicImportIfMatched(QinModuleSource module, List<QinEsmDiagnostic> diagnostics) {
        Matcher matcher = DYNAMIC_IMPORT_PATTERN.matcher(module.source());
        boolean[] code = codeMask(module.source());
        while (matcher.find()) {
            if (!isCodePosition(code, matcher.start())) {
                continue;
            }
            int[] lineCol = lineCol(module.source(), matcher.start());
            diagnostics.add(new QinEsmDiagnostic(
                    "ESM3001",
                    "dynamic import is not implemented for the JVM runtime target yet",
                    module.file(),
                    lineCol[0],
                    lineCol[1]));
            return;
        }
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

    private boolean[] codeMask(String source) {
        boolean[] code = new boolean[source.length()];
        boolean single = false;
        boolean dbl = false;
        boolean template = false;
        boolean lineComment = false;
        boolean blockComment = false;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            char next = i + 1 < source.length() ? source.charAt(i + 1) : '\0';
            char previous = i > 0 ? source.charAt(i - 1) : '\0';

            if (lineComment) {
                if (ch == '\n') {
                    lineComment = false;
                    code[i] = true;
                }
                continue;
            }
            if (blockComment) {
                if (ch == '*' && next == '/') {
                    blockComment = false;
                    i++;
                }
                continue;
            }
            if (single) {
                if (ch == '\'' && previous != '\\') {
                    single = false;
                }
                continue;
            }
            if (dbl) {
                if (ch == '"' && previous != '\\') {
                    dbl = false;
                }
                continue;
            }
            if (template) {
                if (ch == '`' && previous != '\\') {
                    template = false;
                }
                continue;
            }

            if (ch == '/' && next == '/') {
                lineComment = true;
                i++;
            } else if (ch == '/' && next == '*') {
                blockComment = true;
                i++;
            } else if (ch == '\'') {
                single = true;
            } else if (ch == '"') {
                dbl = true;
            } else if (ch == '`') {
                template = true;
            } else {
                code[i] = true;
            }
        }
        return code;
    }

    private boolean isCodePosition(boolean[] code, int index) {
        return index >= 0 && index < code.length && code[index];
    }
}
