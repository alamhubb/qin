package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrJavaImport;
import com.slime.ast.AstNode;
import com.slime.ast.nodes.misc.ImportSpecifier;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.modules.ImportDeclaration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared java: import lowering helpers.
 *
 * Keeps Java import semantics consistent across Qin frontend services and
 * framework-specific compilers that still operate on raw Slime AST.
 */
public final class QinJavaImportSupport {
    private QinJavaImportSupport() {
    }

    public static List<QinIrJavaImport> lowerJavaImports(Program programAst) {
        List<QinIrJavaImport> imports = new ArrayList<>();
        if (programAst == null || programAst.body() == null) {
            return imports;
        }

        for (AstNode statement : programAst.body()) {
            if (statement instanceof ImportDeclaration importDeclaration) {
                imports.addAll(lowerJavaImportDeclaration(importDeclaration));
            }
        }
        return imports;
    }

    public static List<QinIrJavaImport> lowerJavaImportDeclaration(ImportDeclaration importDeclaration) {
        List<QinIrJavaImport> imports = new ArrayList<>();
        if (importDeclaration == null || importDeclaration.source() == null) {
            return imports;
        }
        if (!(importDeclaration.source().value() instanceof String sourceValue)) {
            throw new IllegalArgumentException("ImportDeclaration.source.value must be a string literal");
        }
        if (!sourceValue.startsWith("java:")) {
            return imports;
        }

        String javaModule = sourceValue.substring("java:".length()).trim();
        if (javaModule.isBlank()) {
            throw new IllegalArgumentException("java: import module cannot be blank");
        }
        if (importDeclaration.specifiers().isEmpty()) {
            throw new IllegalArgumentException("java: import does not support side-effect form: " + sourceValue);
        }

        for (AstNode specifierNode : importDeclaration.specifiers()) {
            if (!(specifierNode instanceof ImportSpecifier specifier)) {
                throw new IllegalArgumentException(
                        "Only named import specifier is supported for java: imports, got: "
                                + specifierNode.getClass().getSimpleName());
            }
            String importedName = specifier.imported().name();
            String localName = specifier.local().name();
            String ownerBinaryName = javaModule + "." + importedName;
            imports.add(new QinIrJavaImport(sourceValue, importedName, localName, ownerBinaryName));
        }
        return imports;
    }

    public static Map<String, String> buildLookup(List<QinIrJavaImport> javaImports) {
        Map<String, String> lookup = new LinkedHashMap<>();
        if (javaImports == null) {
            return lookup;
        }
        for (QinIrJavaImport javaImport : javaImports) {
            lookup.put(javaImport.localName(), javaImport.ownerBinaryName());
        }
        return lookup;
    }
}
