package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleSource;
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
import com.slime.ast.nodes.misc.VariableDeclarator;
import com.slime.ast.nodes.modules.ExportAllDeclaration;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
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
        if (!parsed.hasProgram() || parsed.hasAnyPreExtractedImport()) {
            return new Result(List.of(), List.of());
        }
        Program program = parsed.requireProgram();
        List<QinEsmImportBinding> imports = new ArrayList<>();
        List<QinEsmExportBinding> exports = new ArrayList<>();
        for (AstNode statement : program.body()) {
            if (statement instanceof ImportDeclaration importDeclaration) {
                collectImport(module, importDeclaration, imports);
                continue;
            }
            if (statement instanceof ExportNamedDeclaration exportNamedDeclaration) {
                collectNamedExport(module, exportNamedDeclaration, exports);
                continue;
            }
            if (statement instanceof ExportDefaultDeclaration exportDefaultDeclaration) {
                collectDefaultExport(module, exportDefaultDeclaration, exports);
                continue;
            }
            if (statement instanceof ExportAllDeclaration exportAllDeclaration) {
                collectExportAll(module, exportAllDeclaration, exports);
            }
        }
        return new Result(List.copyOf(imports), List.copyOf(exports));
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

    private void collectImport(
            QinModuleSource module,
            ImportDeclaration importDeclaration,
            List<QinEsmImportBinding> out) {
        if (importDeclaration.typeOnly()) {
            return;
        }
        String moduleSpecifier = sourceValue(importDeclaration.source());
        if (moduleSpecifier.isBlank() || moduleSpecifier.startsWith("java:")) {
            return;
        }
        Path resolvedModule = QinEsmSemanticAnalyzer.resolveTargetModule(module, moduleSpecifier);
        int[] lineCol = lineCol(importDeclaration.location());
        if (importDeclaration.specifiers() == null || importDeclaration.specifiers().isEmpty()) {
            out.add(new QinEsmImportBinding(
                    module.file(),
                    moduleSpecifier,
                    QinEsmImportKind.SIDE_EFFECT,
                    "",
                    "",
                    lineCol[0],
                    lineCol[1],
                    resolvedModule));
            return;
        }
        for (AstNode specifier : importDeclaration.specifiers()) {
            if (specifier instanceof ImportDefaultSpecifier defaultSpecifier) {
                out.add(new QinEsmImportBinding(
                        module.file(),
                        moduleSpecifier,
                        QinEsmImportKind.DEFAULT,
                        "default",
                        identifierName(defaultSpecifier.local()),
                        lineCol[0],
                        lineCol[1],
                        resolvedModule));
                continue;
            }
            if (specifier instanceof ImportNamespaceSpecifier namespaceSpecifier) {
                out.add(new QinEsmImportBinding(
                        module.file(),
                        moduleSpecifier,
                        QinEsmImportKind.NAMESPACE,
                        "*",
                        identifierName(namespaceSpecifier.local()),
                        lineCol[0],
                        lineCol[1],
                        resolvedModule));
                continue;
            }
            if (specifier instanceof ImportSpecifier importSpecifier) {
                out.add(new QinEsmImportBinding(
                        module.file(),
                        moduleSpecifier,
                        QinEsmImportKind.NAMED,
                        identifierName(importSpecifier.imported()),
                        identifierName(importSpecifier.local()),
                        lineCol[0],
                        lineCol[1],
                        resolvedModule));
            }
        }
    }

    private void collectNamedExport(
            QinModuleSource module,
            ExportNamedDeclaration exportNamedDeclaration,
            List<QinEsmExportBinding> out) {
        if (exportNamedDeclaration.declaration() != null) {
            collectDeclarationExport(module, exportNamedDeclaration.declaration(), out);
        }
        String moduleSpecifier = sourceValue(exportNamedDeclaration.source());
        Path resolvedModule = moduleSpecifier.isBlank()
                ? null
                : QinEsmSemanticAnalyzer.resolveTargetModule(module, moduleSpecifier);
        int[] lineCol = lineCol(exportNamedDeclaration.location());
        if (exportNamedDeclaration.specifiers() == null) {
            return;
        }
        for (ExportSpecifier specifier : exportNamedDeclaration.specifiers()) {
            String localName = identifierName(specifier.local());
            String exportName = identifierName(specifier.exported());
            boolean reExport = !moduleSpecifier.isBlank();
            boolean typeOnly = exportNamedDeclaration.typeOnly();
            out.add(new QinEsmExportBinding(
                    module.file(),
                    reExport ? QinEsmExportKind.RE_EXPORT_NAMED : QinEsmExportKind.LOCAL_NAMED,
                    exportName,
                    localName,
                    typeOnly,
                    reExport ? moduleSpecifier : null,
                    reExport ? resolvedModule : null,
                    lineCol[0],
                    lineCol[1]));
        }
    }

    private void collectDeclarationExport(
            QinModuleSource module,
            Declaration declaration,
            List<QinEsmExportBinding> out) {
        int[] lineCol = lineCol(nodeLocation(declaration));
        if (declaration instanceof VariableDeclaration variableDeclaration) {
            for (VariableDeclarator declarator : variableDeclaration.declarations()) {
                if (declarator.id() instanceof Identifier identifier) {
                    addLocalExport(module, identifierName(identifier), false, lineCol, out);
                }
            }
            return;
        }
        String name = declarationName(declaration);
        if (!name.isBlank()) {
            addLocalExport(module, name, isTypeOnlyDeclaration(declaration), lineCol, out);
        }
    }

    private void collectDefaultExport(
            QinModuleSource module,
            ExportDefaultDeclaration exportDefaultDeclaration,
            List<QinEsmExportBinding> out) {
        int[] lineCol = lineCol(exportDefaultDeclaration.location());
        out.add(new QinEsmExportBinding(
                module.file(),
                QinEsmExportKind.LOCAL_DEFAULT,
                "default",
                "default",
                false,
                null,
                null,
                lineCol[0],
                lineCol[1]));
        AstNode declaration = exportDefaultDeclaration.declaration();
        String namedDeclaration = declarationName(declaration);
        if (!namedDeclaration.isBlank()) {
            addLocalExport(module, namedDeclaration, false, lineCol, out);
        }
    }

    private void collectExportAll(
            QinModuleSource module,
            ExportAllDeclaration exportAllDeclaration,
            List<QinEsmExportBinding> out) {
        String moduleSpecifier = sourceValue(exportAllDeclaration.source());
        if (moduleSpecifier.isBlank()) {
            return;
        }
        Path resolvedModule = QinEsmSemanticAnalyzer.resolveTargetModule(module, moduleSpecifier);
        int[] lineCol = lineCol(exportAllDeclaration.location());
        Identifier exported = exportAllDeclaration.exported();
        if (exported == null || identifierName(exported).isBlank()) {
            out.add(new QinEsmExportBinding(
                    module.file(),
                    QinEsmExportKind.RE_EXPORT_ALL,
                    "*",
                    "*",
                    false,
                    moduleSpecifier,
                    resolvedModule,
                    lineCol[0],
                    lineCol[1]));
            return;
        }
        out.add(new QinEsmExportBinding(
                module.file(),
                QinEsmExportKind.RE_EXPORT_NAMESPACE,
                identifierName(exported),
                "*",
                false,
                moduleSpecifier,
                resolvedModule,
                lineCol[0],
                lineCol[1]));
    }

    private void addLocalExport(
            QinModuleSource module,
            String name,
            boolean typeOnly,
            int[] lineCol,
            List<QinEsmExportBinding> out) {
        out.add(new QinEsmExportBinding(
                module.file(),
                QinEsmExportKind.LOCAL_NAMED,
                name,
                name,
                typeOnly,
                null,
                null,
                lineCol[0],
                lineCol[1]));
    }

    private boolean isTypeOnlyDeclaration(Declaration declaration) {
        return declaration instanceof TSTypeAliasDeclaration || declaration instanceof TSInterfaceDeclaration;
    }

    private String declarationName(AstNode declaration) {
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

    private SourceLocation nodeLocation(AstNode node) {
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

    private String sourceValue(Literal literal) {
        return literal == null || literal.value() == null ? "" : String.valueOf(literal.value()).trim();
    }

    private String identifierName(Identifier identifier) {
        return identifier == null || identifier.name() == null ? "" : identifier.name().trim();
    }

    private int[] lineCol(SourceLocation location) {
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
