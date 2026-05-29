package com.qin.parser;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJsImport;
import com.slime.ast.nodes.misc.Program;

import java.util.List;
import java.util.Objects;

/**
 * Qin parser output for the current migration stage.
 *
 * <p>Phase-1 parser output still uses Slime Program AST as the main syntax tree,
 * while allowing Qin-owned parser preprocessing/fallback/import extraction to live
 * outside the frontend adapter.
 */
public record QinParsedSource(
        String originalSource,
        String effectiveSource,
        Program programAst,
        List<QinIrJavaImport> javaImports,
        List<QinIrJsImport> jsImports) {

    public QinParsedSource {
        originalSource = defaultString(originalSource);
        effectiveSource = defaultString(effectiveSource);
        javaImports = javaImports == null ? List.of() : List.copyOf(javaImports);
        jsImports = jsImports == null ? List.of() : List.copyOf(jsImports);
    }

    public boolean hasProgram() {
        return programAst != null;
    }

    public Program requireProgram() {
        return Objects.requireNonNull(programAst, "programAst cannot be null");
    }

    public boolean hasAnyPreExtractedImport() {
        return !javaImports.isEmpty() || !jsImports.isEmpty();
    }

    private static String defaultString(String value) {
        return value == null ? "" : value;
    }
}
