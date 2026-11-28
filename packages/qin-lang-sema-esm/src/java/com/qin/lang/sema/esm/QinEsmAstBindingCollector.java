package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleSource;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.parser.QinParsedSource;
import com.qin.parser.QinParserFacade;
import com.slime.ast.AstNode;
import com.slime.ast.Declaration;
import com.slime.ast.SourceLocation;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.Literal;
import com.slime.ast.nodes.misc.ExportSpecifier;
import com.slime.ast.nodes.misc.ImportDefaultSpecifier;
import com.slime.ast.nodes.misc.ImportNamespaceSpecifier;
import com.slime.ast.nodes.misc.ImportSpecifier;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.misc.Property;
import com.slime.ast.nodes.misc.VariableDeclarator;
import com.slime.ast.nodes.modules.ExportAllDeclaration;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.patterns.ArrayPattern;
import com.slime.ast.nodes.patterns.AssignmentPattern;
import com.slime.ast.nodes.patterns.ObjectPattern;
import com.slime.ast.nodes.patterns.RestElement;
import com.slime.ast.nodes.typescript.TSEnumDeclaration;
import com.slime.ast.nodes.typescript.TSInterfaceDeclaration;
import com.slime.ast.nodes.typescript.TSTypeAliasDeclaration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * AST-first ESM binding extraction.
 *
 * <p>The semantic layer reads the same parser facts as the IR lowerer; import
 * and export bindings come from the Slime AST rather than a parallel text
 * scanner.
 */
final class QinEsmAstBindingCollector {
    private final QinParserFacade parserFacade = new QinParserFacade();

    Result collect(QinModuleSource module) {
        if (module == null || module.source() == null) {
            return new Result(List.of(), List.of());
        }
        QinParsedSource parsed;
        try {
            parsed = parserFacade.parseSource(module.source());
        } catch (IllegalArgumentException ex) {
            if (isVirtualDefaultExportModule(module)) {
                return new Result(List.of(), List.of());
            }
            throw ex;
        }
        if (!parsed.hasProgram()) {
            return new Result(List.of(), List.of());
        }
        var bindings = describe(parsed.requireProgram(), !parsed.hasAnyPreExtractedImport())
                .resolve(module.file(), specifier -> QinEsmSemanticAnalyzer.resolveTargetModule(module, specifier));
        if (parsed.hasAnyPreExtractedImport()) {
            List<QinEsmImportBinding> imports = new ArrayList<>();
            collectPreExtractedJsImports(module, parsed.jsImports(), imports);
            return new Result(imports, bindings.exports());
        }
        return new Result(bindings.imports(), bindings.exports());
    }

    static QinEsmModuleBindings describe(Program program, boolean includeImports) {
        List<QinEsmModuleBindings.Import> imports = new ArrayList<>();
        List<QinEsmModuleBindings.Export> exports = new ArrayList<>();
        java.util.Set<String> exportedEnums = null;
        for (AstNode statement : program.body()) {
            if (statement instanceof ImportDeclaration importDeclaration) {
                if (includeImports) collectImport(importDeclaration, imports);
            } else if (statement instanceof ExportNamedDeclaration declaration) {
                if (declaration.declaration() instanceof TSEnumDeclaration enumeration) {
                    if (exportedEnums == null) exportedEnums = new java.util.HashSet<>();
                    if (!exportedEnums.add(enumeration.id().name())) continue;
                }
                collectNamedExport(declaration, exports);
            } else if (statement instanceof ExportDefaultDeclaration declaration) {
                int[] position = lineCol(declaration.location());
                exports.add(new QinEsmModuleBindings.Export(QinEsmExportKind.LOCAL_DEFAULT, "default", "default",
                        false, null, position[0], position[1]));
            } else if (statement instanceof ExportAllDeclaration declaration) {
                collectExportAll(declaration, exports);
            }
        }
        return new QinEsmModuleBindings(imports, exports);
    }

    private void collectPreExtractedJsImports(
            QinModuleSource module,
            List<QinIrJsImport> jsImports,
            List<QinEsmImportBinding> out) {
        if (jsImports == null || jsImports.isEmpty()) {
            return;
        }
        for (QinIrJsImport jsImport : jsImports) {
            String moduleSpecifier = jsImport.moduleName();
            if (moduleSpecifier == null || moduleSpecifier.isBlank()) {
                continue;
            }
            Path resolvedModule = QinEsmSemanticAnalyzer.resolveTargetModule(module, moduleSpecifier);
            String importedName = jsImport.importedName() == null ? "" : jsImport.importedName();
            String localName = jsImport.localName() == null ? "" : jsImport.localName();
            QinEsmImportKind kind;
            if (importedName.isBlank() && localName.isBlank()) {
                kind = QinEsmImportKind.SIDE_EFFECT;
            } else if ("default".equals(importedName)) {
                kind = QinEsmImportKind.DEFAULT;
            } else if ("*".equals(importedName)) {
                kind = QinEsmImportKind.NAMESPACE;
            } else {
                kind = QinEsmImportKind.NAMED;
            }
            out.add(new QinEsmImportBinding(
                    module.file(),
                    moduleSpecifier,
                    kind,
                    importedName,
                    localName,
                    1,
                    1,
                    resolvedModule));
        }
    }

    private boolean isVirtualDefaultExportModule(QinModuleSource module) {
        if (module == null || module.file() == null || module.file().getFileName() == null) {
            return false;
        }
        String fileName = module.file().getFileName().toString().toLowerCase();
        return fileName.endsWith(".vue")
                || fileName.endsWith(".ovs")
                || fileName.endsWith(".svg")
                || fileName.endsWith(".png")
                || fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif")
                || fileName.endsWith(".webp")
                || fileName.endsWith(".ico")
                || fileName.endsWith(".avif");
    }

    private static void collectImport(ImportDeclaration importDeclaration, List<QinEsmModuleBindings.Import> out) {
        if (importDeclaration.typeOnly()) {
            return;
        }
        String moduleSpecifier = sourceValue(importDeclaration.source());
        if (moduleSpecifier.isBlank() || moduleSpecifier.startsWith("java:")) {
            return;
        }
        int[] lineCol = lineCol(importDeclaration.location());
        if (importDeclaration.specifiers() == null || importDeclaration.specifiers().isEmpty()) {
            out.add(new QinEsmModuleBindings.Import(moduleSpecifier, QinEsmImportKind.SIDE_EFFECT,
                    "", "", lineCol[0], lineCol[1]));
            return;
        }
        for (AstNode specifier : importDeclaration.specifiers()) {
            if (specifier instanceof ImportDefaultSpecifier defaultSpecifier) {
                out.add(new QinEsmModuleBindings.Import(
                        moduleSpecifier,
                        QinEsmImportKind.DEFAULT,
                        "default",
                        identifierName(defaultSpecifier.local()),
                        lineCol[0],
                        lineCol[1]));
                continue;
            }
            if (specifier instanceof ImportNamespaceSpecifier namespaceSpecifier) {
                out.add(new QinEsmModuleBindings.Import(
                        moduleSpecifier,
                        QinEsmImportKind.NAMESPACE,
                        "*",
                        identifierName(namespaceSpecifier.local()),
                        lineCol[0],
                        lineCol[1]));
                continue;
            }
            if (specifier instanceof ImportSpecifier importSpecifier) {
                if (importSpecifier.typeOnly()) {
                    continue;
                }
                out.add(new QinEsmModuleBindings.Import(
                        moduleSpecifier,
                        QinEsmImportKind.NAMED,
                        identifierName(importSpecifier.imported()),
                        identifierName(importSpecifier.local()),
                        lineCol[0],
                        lineCol[1]));
            }
        }
    }

    private static void collectNamedExport(
            ExportNamedDeclaration exportNamedDeclaration, List<QinEsmModuleBindings.Export> out) {
        if (exportNamedDeclaration.declaration() != null) {
            collectDeclarationExport(exportNamedDeclaration.declaration(), out);
        }
        String moduleSpecifier = sourceValue(exportNamedDeclaration.source());
        int[] lineCol = lineCol(exportNamedDeclaration.location());
        if (exportNamedDeclaration.specifiers() == null) {
            return;
        }
        for (ExportSpecifier specifier : exportNamedDeclaration.specifiers()) {
            String localName = identifierName(specifier.local());
            String exportName = identifierName(specifier.exported());
            boolean reExport = !moduleSpecifier.isBlank();
            boolean typeOnly = exportNamedDeclaration.typeOnly() || specifier.typeOnly();
            out.add(new QinEsmModuleBindings.Export(
                    reExport ? QinEsmExportKind.RE_EXPORT_NAMED : QinEsmExportKind.LOCAL_NAMED,
                    exportName,
                    localName,
                    typeOnly,
                    reExport ? moduleSpecifier : null,
                    lineCol[0],
                    lineCol[1]));
        }
    }

    private static void collectDeclarationExport(Declaration declaration, List<QinEsmModuleBindings.Export> out) {
        int[] lineCol = lineCol(nodeLocation(declaration));
        if (declaration instanceof VariableDeclaration variableDeclaration) {
            for (VariableDeclarator declarator : variableDeclaration.declarations()) {
                collectExportedPattern(declarator.id(), lineCol, out);
            }
            return;
        }
        String name = declarationName(declaration);
        if (!name.isBlank()) {
            addLocalExport(name, isTypeOnlyDeclaration(declaration), lineCol, out);
        }
    }

    private static void collectExportedPattern(AstNode node, int[] lineCol, List<QinEsmModuleBindings.Export> out) {
        if (node == null) return;
        switch (node) {
            case Identifier identifier -> addLocalExport(identifierName(identifier), false, lineCol, out);
            case ObjectPattern pattern -> {
                for (AstNode property : pattern.properties()) collectExportedPattern(property, lineCol, out);
            }
            case Property property -> collectExportedPattern(property.value(), lineCol, out);
            case ArrayPattern pattern -> {
                for (AstNode element : pattern.elements()) collectExportedPattern(element, lineCol, out);
            }
            case AssignmentPattern pattern -> collectExportedPattern(pattern.left(), lineCol, out);
            case RestElement element -> collectExportedPattern(element.argument(), lineCol, out);
            default -> { }
        }
    }

    private static void collectExportAll(ExportAllDeclaration exportAllDeclaration, List<QinEsmModuleBindings.Export> out) {
        String moduleSpecifier = sourceValue(exportAllDeclaration.source());
        if (moduleSpecifier.isBlank()) {
            return;
        }
        int[] lineCol = lineCol(exportAllDeclaration.location());
        String exported = identifierName(exportAllDeclaration.exported());
        boolean all = exported.isBlank();
        out.add(new QinEsmModuleBindings.Export(all ? QinEsmExportKind.RE_EXPORT_ALL : QinEsmExportKind.RE_EXPORT_NAMESPACE,
                all ? "*" : exported, "*", exportAllDeclaration.typeOnly(), moduleSpecifier, lineCol[0], lineCol[1]));
    }

    private static void addLocalExport(
            String name,
            boolean typeOnly,
            int[] lineCol,
            List<QinEsmModuleBindings.Export> out) {
        out.add(new QinEsmModuleBindings.Export(
                QinEsmExportKind.LOCAL_NAMED,
                name,
                name,
                typeOnly,
                null,
                lineCol[0],
                lineCol[1]));
    }

    private static boolean isTypeOnlyDeclaration(Declaration declaration) {
        return declaration instanceof TSTypeAliasDeclaration || declaration instanceof TSInterfaceDeclaration;
    }

    private static String declarationName(AstNode declaration) {
        if (declaration instanceof FunctionDeclaration functionDeclaration) {
            return identifierName(functionDeclaration.id());
        }
        if (declaration instanceof ClassDeclaration classDeclaration) {
            return identifierName(classDeclaration.id());
        }
        if (declaration instanceof TSTypeAliasDeclaration typeAliasDeclaration) {
            return identifierName(typeAliasDeclaration.id());
        }
        if (declaration instanceof TSInterfaceDeclaration interfaceDeclaration) {
            return identifierName(interfaceDeclaration.id());
        }
        if (declaration instanceof TSEnumDeclaration enumDeclaration) {
            return identifierName(enumDeclaration.id());
        }
        return "";
    }

    private static SourceLocation nodeLocation(AstNode node) {
        if (node instanceof VariableDeclaration variableDeclaration) {
            return variableDeclaration.location();
        }
        if (node instanceof FunctionDeclaration functionDeclaration) {
            return functionDeclaration.location();
        }
        if (node instanceof ClassDeclaration classDeclaration) {
            return classDeclaration.location();
        }
        if (node instanceof TSTypeAliasDeclaration typeAliasDeclaration) {
            return typeAliasDeclaration.location();
        }
        if (node instanceof TSInterfaceDeclaration interfaceDeclaration) {
            return interfaceDeclaration.location();
        }
        if (node instanceof TSEnumDeclaration enumDeclaration) {
            return enumDeclaration.location();
        }
        return null;
    }

    private static String sourceValue(Literal literal) {
        return literal == null || literal.value() == null ? "" : String.valueOf(literal.value()).trim();
    }

    private static String identifierName(Identifier identifier) {
        return identifier == null || identifier.name() == null ? "" : identifier.name().trim();
    }

    private static int[] lineCol(SourceLocation location) {
        if (location == null || location.start() == null) {
            return new int[] {1, 1};
        }
        int line = Math.max(1, location.start().line());
        int column = Math.max(1, location.start().column());
        return new int[] {line, column};
    }

    record Result(List<QinEsmImportBinding> imports, List<QinEsmExportBinding> exports) {
        Result {
            imports = imports == null ? List.of() : List.copyOf(imports);
            exports = exports == null ? List.of() : List.copyOf(exports);
        }
    }
}
