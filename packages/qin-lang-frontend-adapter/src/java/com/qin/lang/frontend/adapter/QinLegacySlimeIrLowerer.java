package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrFunctionModelArtifact;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.parser.QinParsedSource;
import com.qin.parser.QinParserRuntimeNames;
import com.slime.ast.AstNode;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.modules.ExportAllDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;

import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Transitional lowering host for the existing Slime-backed IR lowering logic.
 *
 * <p>This keeps legacy implementation details out of the compatibility parse
 * façade while we incrementally move lowering logic toward Qin-owned types.
 */
final class QinLegacySlimeIrLowerer extends QinSlimeIrLoweringSupport {
    private final QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
    private final QinDeclarationIrLowerer declarationIrLowerer = new QinDeclarationIrLowerer(adapter);
    private final QinRuntimeIrLowerer runtimeIrLowerer = new QinRuntimeIrLowerer(adapter);

    void setCurrentSourceLength(int sourceLength) {
        loweringContext.setSourceLength(sourceLength);
        this.currentSourceLength = sourceLength;
        adapter.loweringContext.setSourceLength(sourceLength);
        adapter.currentSourceLength = sourceLength;
    }

    void setFunctionModelBudget(int budget) {
        loweringContext.setFunctionModelBudgetRemaining(budget);
        this.functionModelBudgetRemaining = budget;
        adapter.loweringContext.setFunctionModelBudgetRemaining(budget);
        adapter.functionModelBudgetRemaining = budget;
    }

    void resetFunctionModelArtifacts() {
        loweringContext.resetFunctionModelArtifacts();
        adapter.loweringContext.resetFunctionModelArtifacts();
    }

    List<QinIrFunctionModelArtifact> functionModelArtifacts() {
        return adapter.loweringContext.functionModelArtifacts();
    }

    QinIrClassDeclaration lowerClassDeclarationOrNull(
            com.slime.ast.nodes.declarations.ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        return declarationIrLowerer.lowerClassDeclarationOrNull(classDeclaration, javaImportLookup, localDeclarationNames);
    }

    List<QinIrConstDeclaration> lowerVariableDeclaration(
            com.slime.ast.nodes.declarations.VariableDeclaration variableDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return declarationIrLowerer.lowerVariableDeclaration(variableDeclaration, javaImportLookup, declarationLookup);
    }

    QinIrConstDeclaration lowerFunctionDeclaration(
            com.slime.ast.nodes.declarations.FunctionDeclaration functionDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return declarationIrLowerer.lowerFunctionDeclaration(functionDeclaration, javaImportLookup, declarationLookup);
    }

    QinIrConstDeclaration lowerClassDeclarationValue(
            com.slime.ast.nodes.declarations.ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return declarationIrLowerer.lowerClassDeclarationValue(classDeclaration, javaImportLookup, declarationLookup);
    }

    QinTopLevelIrAssembler.LoweredImports lowerImportDeclaration(
            ImportDeclaration importDeclaration) {
        List<QinIrJavaImport> javaImports = QinJavaImportSupport.lowerJavaImportDeclaration(importDeclaration);
        if (!javaImports.isEmpty()) {
            return new QinTopLevelIrAssembler.LoweredImports(javaImports, List.of());
        }
        return lowerNonJavaImportDeclaration(importDeclaration);
    }

    QinTopLevelIrAssembler.LoweredStatement lowerExpressionStatement(
            com.slime.ast.nodes.statements.ExpressionStatement expressionStatement,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return runtimeIrLowerer.lowerExpressionStatement(expressionStatement, javaImportLookup, declarationLookup);
    }

    boolean isTopLevelControlStatement(String nodeType) {
        return "ForStatement".equals(nodeType)
                || "ForOfStatement".equals(nodeType)
                || "WhileStatement".equals(nodeType)
                || "DoWhileStatement".equals(nodeType)
                || "SwitchStatement".equals(nodeType)
                || "TryStatement".equals(nodeType)
                || "BlockStatement".equals(nodeType);
    }

    QinIrExpressionStatement lowerTopLevelControlStatement(
            AstNode statementAst,
            String nodeType,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return runtimeIrLowerer.lowerTopLevelControlStatement(
                statementAst,
                nodeType,
                javaImportLookup,
                declarationLookup);
    }

    Set<String> collectTopLevelClassNames(List<? extends AstNode> body) {
        if (body == null || body.isEmpty()) {
            return Set.of();
        }
        Set<String> names = new LinkedHashSet<>();
        for (AstNode statement : body) {
            if (!(statement instanceof ClassDeclaration classDeclaration)
                    || classDeclaration.id() == null
                    || classDeclaration.id().name() == null
                    || classDeclaration.id().name().isBlank()) {
                continue;
            }
            names.add(classDeclaration.id().name());
        }
        return Set.copyOf(names);
    }

    void predeclareTopLevelBindings(List<? extends AstNode> body, Map<String, QinIrExpression> declarationLookup) {
        for (AstNode statement : body) {
            String nodeType = statement.getClass().getSimpleName();
            if (statement instanceof VariableDeclaration) {
                List<?> declarators = asList(
                        QinSlimeFrontendAdapter.invokeByName(statement, "declarations"),
                        "VariableDeclaration.declarations");
                for (Object declarator : declarators) {
                    for (String name : extractBindingNames(
                            QinSlimeFrontendAdapter.invokeByName(declarator, "id"),
                            "VariableDeclarator.id")) {
                        declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                    }
                }
                continue;
            }
            if (statement instanceof FunctionDeclaration || statement instanceof ClassDeclaration) {
                String name = extractIdentifierName(
                        QinSlimeFrontendAdapter.invokeByName(statement, "id"),
                        nodeType + ".id");
                if (!name.isBlank()) {
                    declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                }
                continue;
            }
            if (statement instanceof ExportNamedDeclaration) {
                Object declaration = QinSlimeFrontendAdapter.invokeByName(statement, "declaration");
                if (declaration == null) {
                    continue;
                }
                String declarationType = QinSlimeFrontendAdapter.simpleName(declaration);
                if ("VariableDeclaration".equals(declarationType)) {
                    List<?> declarators = asList(
                            QinSlimeFrontendAdapter.invokeByName(declaration, "declarations"),
                            "VariableDeclaration.declarations");
                    for (Object declarator : declarators) {
                        for (String name : extractBindingNames(
                                QinSlimeFrontendAdapter.invokeByName(declarator, "id"),
                                "VariableDeclarator.id")) {
                            declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                        }
                    }
                    continue;
                }
                if ("FunctionDeclaration".equals(declarationType) || "ClassDeclaration".equals(declarationType)) {
                    String name = extractIdentifierName(
                            QinSlimeFrontendAdapter.invokeByName(declaration, "id"),
                            declarationType + ".id");
                    if (!name.isBlank()) {
                        declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                    }
                }
            }
        }
    }

    QinIrExpressionStatement createGlobalBindingStatement(String name) {
        Objects.requireNonNull(name, "name cannot be null");
        QinIrBuiltinCallExpression bind = new QinIrBuiltinCallExpression(
                "Global",
                "__qin_bind_global__",
                List.of(new QinIrStringLiteral(name), new QinIrIdentifierReference(name)));
        return new QinIrExpressionStatement(bind);
    }

    QinIrExpressionStatement createGlobalDeclarationStatement(String name) {
        Objects.requireNonNull(name, "name cannot be null");
        QinIrBuiltinCallExpression bind = new QinIrBuiltinCallExpression(
                "Global",
                "__qin_declare_global__",
                List.of(new QinIrStringLiteral(name)));
        return new QinIrExpressionStatement(bind);
    }

    int computeFunctionModelBudget(int sourceLength) {
        return QinFunctionModelBudget.compute(sourceLength);
    }

    void registerJavaImportLookup(Map<String, String> lookup, QinIrJavaImport javaImport) {
        lookup.put(javaImport.localName(), javaImport.ownerBinaryName());
    }

    void lowerExportNamedDeclaration(
            AstNode exportNamedDeclarationAst,
            QinTopLevelIrAssembler.MutableProgramAssembly assembly,
            boolean enableGlobalBinding,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object declaration = QinSlimeFrontendAdapter.invokeByName(exportNamedDeclarationAst, "declaration");
        if (declaration == null) {
            return;
        }

        String declarationType = QinSlimeFrontendAdapter.simpleName(declaration);
        if ("VariableDeclaration".equals(declarationType)
                && declaration instanceof com.slime.ast.nodes.declarations.VariableDeclaration variableDeclaration) {
            List<QinIrConstDeclaration> loweredDeclarations = lowerVariableDeclaration(
                    variableDeclaration,
                    javaImportLookup,
                    declarationLookup);
            for (QinIrConstDeclaration lowered : loweredDeclarations) {
                assembly.declarations().add(lowered);
                assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.DECLARATION,
                        assembly.declarations().size() - 1));
                declarationLookup.put(lowered.name(), lowered.initializer());
                if (enableGlobalBinding) {
                    assembly.expressionStatements().add(createGlobalBindingStatement(lowered.name()));
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            assembly.expressionStatements().size() - 1));
                }
            }
            return;
        }

        if ("FunctionDeclaration".equals(declarationType)
                && declaration instanceof com.slime.ast.nodes.declarations.FunctionDeclaration functionDeclaration) {
            QinIrConstDeclaration lowered = lowerFunctionDeclaration(
                    functionDeclaration,
                    javaImportLookup,
                    declarationLookup);
            assembly.declarations().add(lowered);
            assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                    QinIrProgram.TopLevelStatementKind.DECLARATION,
                    assembly.declarations().size() - 1));
            declarationLookup.put(lowered.name(), lowered.initializer());
            if (enableGlobalBinding) {
                assembly.expressionStatements().add(createGlobalBindingStatement(lowered.name()));
                assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                        assembly.expressionStatements().size() - 1));
            }
            return;
        }

        if ("ClassDeclaration".equals(declarationType)
                && declaration instanceof com.slime.ast.nodes.declarations.ClassDeclaration classDeclaration) {
            QinIrConstDeclaration lowered = lowerClassDeclarationValue(
                    classDeclaration,
                    javaImportLookup,
                    declarationLookup);
            assembly.declarations().add(lowered);
            assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                    QinIrProgram.TopLevelStatementKind.DECLARATION,
                    assembly.declarations().size() - 1));
            declarationLookup.put(lowered.name(), lowered.initializer());
            if (enableGlobalBinding) {
                assembly.expressionStatements().add(createGlobalBindingStatement(lowered.name()));
                assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                        assembly.expressionStatements().size() - 1));
            }
            return;
        }

        throw new IllegalArgumentException("Unsupported export declaration type: " + declarationType);
    }

    QinTopLevelIrAssembler.LoweredImports lowerExportAllDeclaration(
            ExportAllDeclaration exportAllDeclaration) {
        if (exportAllDeclaration == null || exportAllDeclaration.source() == null) {
            throw new IllegalArgumentException("ExportAllDeclaration.source cannot be null");
        }
        Object sourceValueNode = QinSlimeFrontendAdapter.invokeByName(exportAllDeclaration.source(), "value");
        String sourceValue = asString(sourceValueNode, "ExportAllDeclaration.source.value");
        if (!isJsModule(sourceValue)) {
            throw new IllegalArgumentException("Unsupported export-all module: " + sourceValue);
        }
        return new QinTopLevelIrAssembler.LoweredImports(
                List.of(),
                List.of(new QinIrJsImport(sourceValue, "", "")));
    }

    String renderLegacyParsedAst(QinParsedSource parsed) {
        if (parsed == null || !parsed.hasProgram()) {
            return "Program(import-only)";
        }
        return QinSlimeFrontendAdapter.renderProgramAst(parsed.requireProgram(), parsed.effectiveSource());
    }

    private QinTopLevelIrAssembler.LoweredImports lowerNonJavaImportDeclaration(Object importDeclarationAst) {
        Object sourceNode = QinSlimeFrontendAdapter.invokeByName(importDeclarationAst, "source");
        String sourceValue = asString(
                QinSlimeFrontendAdapter.invokeByName(sourceNode, "value"),
                "ImportDeclaration.source.value");
        List<?> specifiers = asList(
                QinSlimeFrontendAdapter.invokeByName(importDeclarationAst, "specifiers"),
                "ImportDeclaration.specifiers");

        if (isJsModule(sourceValue)) {
            List<QinIrJsImport> imports = new ArrayList<>();
            if (specifiers.isEmpty()) {
                imports.add(new QinIrJsImport(sourceValue, "", ""));
                return new QinTopLevelIrAssembler.LoweredImports(List.of(), imports);
            }
            for (Object specifier : specifiers) {
                String nodeType = QinSlimeFrontendAdapter.simpleName(specifier);
                if ("ImportSpecifier".equals(nodeType)) {
                    String importedName = extractIdentifierName(
                            QinSlimeFrontendAdapter.invokeByName(specifier, "imported"),
                            "ImportSpecifier.imported");
                    String localName = extractIdentifierName(
                            QinSlimeFrontendAdapter.invokeByName(specifier, "local"),
                            "ImportSpecifier.local");
                    imports.add(new QinIrJsImport(sourceValue, importedName, localName));
                    continue;
                }
                if ("ImportDefaultSpecifier".equals(nodeType)) {
                    String localName = extractIdentifierName(
                            QinSlimeFrontendAdapter.invokeByName(specifier, "local"),
                            "ImportDefaultSpecifier.local");
                    imports.add(new QinIrJsImport(sourceValue, "default", localName));
                    continue;
                }
                if ("ImportNamespaceSpecifier".equals(nodeType)) {
                    String localName = extractIdentifierName(
                            QinSlimeFrontendAdapter.invokeByName(specifier, "local"),
                            "ImportNamespaceSpecifier.local");
                    imports.add(new QinIrJsImport(sourceValue, "*", localName));
                    continue;
                }
                throw new IllegalArgumentException("Unsupported js import specifier type: " + nodeType);
            }
            return new QinTopLevelIrAssembler.LoweredImports(List.of(), imports);
        }

        throw new IllegalArgumentException("Unsupported import module: " + sourceValue);
    }

    private static List<?> asList(Object value, String where) {
        if (value == null) {
            return List.of();
        }
        if (value instanceof List<?> list) {
            return list;
        }
        throw new IllegalArgumentException(where + " must be a list");
    }

    private static String extractIdentifierName(Object astNode, String where) {
        if (astNode == null) {
            return "";
        }
        Object name = QinSlimeFrontendAdapter.invokeByName(astNode, "name");
        if (name instanceof String text) {
            return text;
        }
        throw new IllegalArgumentException(where + " must expose identifier name()");
    }

    private static List<String> extractBindingNames(Object astNode, String where) {
        if (astNode == null) {
            return List.of();
        }
        String nodeType = QinSlimeFrontendAdapter.simpleName(astNode);
        if ("Identifier".equals(nodeType)) {
            String name = extractIdentifierName(astNode, where);
            return name.isBlank() ? List.of() : List.of(name);
        }
        if ("ObjectPattern".equals(nodeType)) {
            List<String> names = new ArrayList<>();
            for (Object property : asList(
                    QinSlimeFrontendAdapter.invokeByName(astNode, "properties"),
                    where + ".properties")) {
                if (property == null) {
                    continue;
                }
                String propertyType = QinSlimeFrontendAdapter.simpleName(property);
                if ("RestElement".equals(propertyType)) {
                    names.addAll(extractBindingNames(
                            QinSlimeFrontendAdapter.invokeByName(property, "argument"),
                            where + ".rest"));
                    continue;
                }
                if (!"Property".equals(propertyType)) {
                    continue;
                }
                Object value = QinSlimeFrontendAdapter.invokeByName(property, "value");
                names.addAll(extractBindingNames(value, where + ".property"));
            }
            return names;
        }
        if ("ArrayPattern".equals(nodeType)) {
            List<String> names = new ArrayList<>();
            for (Object element : asList(
                    QinSlimeFrontendAdapter.invokeByName(astNode, "elements"),
                    where + ".elements")) {
                names.addAll(extractBindingNames(element, where + ".element"));
            }
            return names;
        }
        if ("RestElement".equals(nodeType)) {
            return extractBindingNames(
                    QinSlimeFrontendAdapter.invokeByName(astNode, "argument"),
                    where + ".argument");
        }
        if ("AssignmentPattern".equals(nodeType)) {
            return extractBindingNames(
                    QinSlimeFrontendAdapter.invokeByName(astNode, "left"),
                    where + ".left");
        }
        return List.of();
    }

    private static String asString(Object value, String where) {
        if (value instanceof String text) {
            return text;
        }
        throw new IllegalArgumentException(where + " must be a string");
    }

    private static boolean isJsModule(String moduleName) {
        if (moduleName == null || moduleName.isBlank()) {
            return false;
        }
        return moduleName.startsWith("js:")
                || moduleName.endsWith(".js")
                || moduleName.endsWith(".mjs")
                || !moduleName.startsWith("java:");
    }
}
