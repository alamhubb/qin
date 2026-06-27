package com.qin.parser;

import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJsImport;
import com.slime.ast.nodes.misc.Program;
import com.subhuti.struct.SubhutiCst;

import java.util.List;
import java.util.Objects;

/**
 * Qin parser output for the current migration stage.
 *
 * <p>Parser output keeps both the Qin CST and the Slime-compatible Program AST
 * so downstream compiler stages can consume structured syntax without source
 * string rewrites.
 */
public record QinParsedSource(
        String originalSource,
        String effectiveSource,
        SubhutiCst cst,
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

    public boolean hasCst() {
        return cst != null;
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
