package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrFunctionModelArtifact;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrProgram;
import com.slime.ast.AstNode;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.modules.ExportAllDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.statements.ExpressionStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class QinTopLevelIrAssembler {
    private final QinLegacySlimeIrLowerer legacyLowerer;

    QinTopLevelIrAssembler(QinLegacySlimeIrLowerer legacyLowerer) {
        this.legacyLowerer = legacyLowerer;
    }

    QinIrProgram assembleProgram(
            Program programAst,
            List<QinIrJavaImport> preImports,
            List<QinIrJsImport> preJsImports,
            int sourceLength) {
        int functionModelBudget = legacyLowerer.computeFunctionModelBudget(sourceLength);
        legacyLowerer.resetFunctionModelArtifacts();
        legacyLowerer.setFunctionModelBudget(functionModelBudget);
        legacyLowerer.setCurrentSourceLength(sourceLength);

        List<AstNode> body = programAst.body();
        if (body.isEmpty()) {
            throw new IllegalArgumentException("Program body cannot be empty");
        }

        MutableProgramAssembly assembly = new MutableProgramAssembly();
        Map<String, String> javaImportLookup = new HashMap<>();
        Map<String, QinIrExpression> declarationLookup = new HashMap<>();
        Set<String> localDeclarationNames = legacyLowerer.collectTopLevelClassNames(body);
        Map<String, QinIrClassDeclaration> localJvmDeclarations = new LinkedHashMap<>();
        boolean enableGlobalBinding = sourceLength <= 200_000;
        legacyLowerer.predeclareTopLevelBindings(body, declarationLookup);
        if (enableGlobalBinding) {
            predeclareGlobalBindingSlots(assembly, declarationLookup.keySet());
        }
        if (preImports != null) {
            assembly.javaImports().addAll(preImports);
            for (QinIrJavaImport javaImport : preImports) {
                legacyLowerer.registerJavaImportLookup(javaImportLookup, javaImport);
            }
        }
        if (preJsImports != null) {
            assembly.jsImports().addAll(preJsImports);
            registerJsImportBindings(preJsImports, declarationLookup);
        }
        Set<AstNode> hoistedFunctionDeclarations = hoistTopLevelFunctionDeclarations(
                body,
                assembly,
                enableGlobalBinding,
                javaImportLookup,
                declarationLookup);

        for (AstNode statement : body) {
            String nodeType = statement.getClass().getSimpleName();
            if (hoistedFunctionDeclarations.contains(statement)) {
                continue;
            }
            if (statement instanceof ImportDeclaration importDeclaration) {
                LoweredImports loweredImports = legacyLowerer.lowerImportDeclaration(importDeclaration);
                assembly.javaImports().addAll(loweredImports.javaImports());
                assembly.jsImports().addAll(loweredImports.jsImports());
                for (QinIrJavaImport javaImport : loweredImports.javaImports()) {
                    legacyLowerer.registerJavaImportLookup(javaImportLookup, javaImport);
                }
                registerJsImportBindings(loweredImports.jsImports(), declarationLookup);
                continue;
            }
            if (statement instanceof VariableDeclaration variableDeclaration) {
                List<QinIrConstDeclaration> loweredDeclarations = legacyLowerer.lowerVariableDeclaration(
                        variableDeclaration,
                        javaImportLookup,
                        declarationLookup);
                for (QinIrConstDeclaration declaration : loweredDeclarations) {
                    assembly.declarations().add(declaration);
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.DECLARATION,
                            assembly.declarations().size() - 1));
                    declarationLookup.put(declaration.name(), declaration.initializer());
                    if (enableGlobalBinding) {
                        assembly.expressionStatements().add(legacyLowerer.createGlobalBindingStatement(declaration.name()));
                        assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                                QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                                assembly.expressionStatements().size() - 1));
                    }
                }
                continue;
            }
            if (statement instanceof FunctionDeclaration functionDeclaration) {
                QinIrConstDeclaration declaration = legacyLowerer.lowerFunctionDeclaration(
                        functionDeclaration,
                        javaImportLookup,
                        declarationLookup);
                assembly.declarations().add(declaration);
                assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.DECLARATION,
                        assembly.declarations().size() - 1));
                declarationLookup.put(declaration.name(), declaration.initializer());
                if (enableGlobalBinding) {
                    assembly.expressionStatements().add(legacyLowerer.createGlobalBindingStatement(declaration.name()));
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            assembly.expressionStatements().size() - 1));
                }
                continue;
            }
            if (statement instanceof ClassDeclaration classDeclaration) {
                QinIrClassDeclaration loweredClass = legacyLowerer.lowerClassDeclarationOrNull(
                        classDeclaration,
                        javaImportLookup,
                        localDeclarationNames,
                        localJvmDeclarations);
                if (loweredClass != null) {
                    assembly.classDeclarations().add(loweredClass);
                    localJvmDeclarations.put(loweredClass.simpleName(), loweredClass);
                }
                QinIrConstDeclaration declaration = legacyLowerer.lowerClassDeclarationValue(
                        classDeclaration,
                        javaImportLookup,
                        declarationLookup,
                        loweredClass != null);
                assembly.declarations().add(declaration);
                assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                        QinIrProgram.TopLevelStatementKind.DECLARATION,
                        assembly.declarations().size() - 1));
                declarationLookup.put(declaration.name(), declaration.initializer());
                if (enableGlobalBinding) {
                    assembly.expressionStatements().add(legacyLowerer.createGlobalBindingStatement(declaration.name()));
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            assembly.expressionStatements().size() - 1));
                }
                continue;
            }
            if (statement instanceof ExpressionStatement expressionStatement) {
                LoweredStatement lowered = legacyLowerer.lowerExpressionStatement(
                        expressionStatement,
                        javaImportLookup,
                        declarationLookup);
                if (lowered.consoleValueLog() != null) {
                    assembly.consoleValueLogs().add(lowered.consoleValueLog());
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.CONSOLE_VALUE,
                            assembly.consoleValueLogs().size() - 1));
                }
                if (lowered.expressionStatement() != null) {
                    assembly.expressionStatements().add(lowered.expressionStatement());
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            assembly.expressionStatements().size() - 1));
                }
                if (lowered.objectLog() != null) {
                    assembly.consoleLogs().add(lowered.objectLog());
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.CONSOLE_OBJECT,
                            assembly.consoleLogs().size() - 1));
                }
                if (lowered.javaStaticCall() != null) {
                    assembly.javaStaticConsoleLogs().add(lowered.javaStaticCall());
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.JAVA_STATIC_CONSOLE,
                            assembly.javaStaticConsoleLogs().size() - 1));
                }
                if (lowered.javaInstanceMethodCall() != null) {
                    assembly.javaInstanceMethodCalls().add(lowered.javaInstanceMethodCall());
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.JAVA_INSTANCE_CALL,
                            assembly.javaInstanceMethodCalls().size() - 1));
                }
                if (lowered.javaInstanceConsoleLog() != null) {
                    assembly.javaInstanceConsoleLogs().add(lowered.javaInstanceConsoleLog());
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.JAVA_INSTANCE_CONSOLE,
                            assembly.javaInstanceConsoleLogs().size() - 1));
                }
                continue;
            }
            if (legacyLowerer.isTopLevelControlStatement(nodeType)) {
                QinIrExpressionStatement loweredControl = legacyLowerer.lowerTopLevelControlStatement(
                        statement,
                        nodeType,
                        javaImportLookup,
                        declarationLookup);
                if (loweredControl != null) {
                    assembly.expressionStatements().add(loweredControl);
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                            assembly.expressionStatements().size() - 1));
                    continue;
                }
            }
            if (statement instanceof ExportNamedDeclaration) {
                legacyLowerer.lowerExportNamedDeclaration(
                        statement,
                        assembly,
                        enableGlobalBinding,
                        javaImportLookup,
                        declarationLookup);
                continue;
            }
            if (statement instanceof ExportAllDeclaration exportAllDeclaration) {
                LoweredImports loweredImports = legacyLowerer.lowerExportAllDeclaration(exportAllDeclaration);
                assembly.javaImports().addAll(loweredImports.javaImports());
                assembly.jsImports().addAll(loweredImports.jsImports());
                for (QinIrJavaImport javaImport : loweredImports.javaImports()) {
                    legacyLowerer.registerJavaImportLookup(javaImportLookup, javaImport);
                }
                registerJsImportBindings(loweredImports.jsImports(), declarationLookup);
                continue;
            }
            throw new IllegalArgumentException("Unsupported top-level statement type: " + nodeType);
        }

        if (assembly.declarations().isEmpty()
                && assembly.expressionStatements().isEmpty()
                && assembly.consoleValueLogs().isEmpty()
                && assembly.consoleLogs().isEmpty()
                && assembly.javaStaticConsoleLogs().isEmpty()
                && assembly.javaInstanceMethodCalls().isEmpty()
                && assembly.javaInstanceConsoleLogs().isEmpty()) {
            throw new IllegalArgumentException("Program must contain at least one supported statement");
        }

        List<QinIrFunctionModelArtifact> functionModelArtifacts = legacyLowerer.functionModelArtifacts();
        return new QinIrProgram(
                assembly.declarations(),
                assembly.expressionStatements(),
                assembly.consoleValueLogs(),
                assembly.consoleLogs(),
                assembly.javaImports(),
                assembly.jsImports(),
                assembly.javaStaticConsoleLogs(),
                assembly.javaInstanceMethodCalls(),
                assembly.javaInstanceConsoleLogs(),
                assembly.classDeclarations(),
                assembly.executionSteps(),
                functionModelArtifacts);
    }

    private void predeclareGlobalBindingSlots(
            MutableProgramAssembly assembly,
            Set<String> declarationNames) {
        if (declarationNames == null || declarationNames.isEmpty()) {
            return;
        }
        for (String name : new LinkedHashSet<>(declarationNames)) {
            if (name == null || name.isBlank()) {
                continue;
            }
            assembly.expressionStatements().add(legacyLowerer.createGlobalDeclarationStatement(name));
            assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                    QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                    assembly.expressionStatements().size() - 1));
        }
    }

    private Set<AstNode> hoistTopLevelFunctionDeclarations(
            List<AstNode> body,
            MutableProgramAssembly assembly,
            boolean enableGlobalBinding,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        java.util.Set<AstNode> hoisted = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
        for (AstNode statement : body) {
            if (statement instanceof FunctionDeclaration functionDeclaration) {
                addHoistedFunctionDeclaration(
                        functionDeclaration,
                        statement,
                        assembly,
                        enableGlobalBinding,
                        javaImportLookup,
                        declarationLookup,
                        hoisted);
                continue;
            }
            if (statement instanceof ExportNamedDeclaration) {
                Object declaration = QinSlimeFrontendAdapter.invokeByName(statement, "declaration");
                if (declaration instanceof FunctionDeclaration functionDeclaration) {
                    addHoistedFunctionDeclaration(
                            functionDeclaration,
                            statement,
                            assembly,
                            enableGlobalBinding,
                            javaImportLookup,
                            declarationLookup,
                            hoisted);
                }
            }
        }
        return hoisted;
    }

    private void addHoistedFunctionDeclaration(
            FunctionDeclaration functionDeclaration,
            AstNode originalStatement,
            MutableProgramAssembly assembly,
            boolean enableGlobalBinding,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            Set<AstNode> hoisted) {
        QinIrConstDeclaration declaration = legacyLowerer.lowerFunctionDeclaration(
                functionDeclaration,
                javaImportLookup,
                declarationLookup);
        assembly.declarations().add(declaration);
        assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                QinIrProgram.TopLevelStatementKind.DECLARATION,
                assembly.declarations().size() - 1));
        declarationLookup.put(declaration.name(), declaration.initializer());
        if (enableGlobalBinding) {
            assembly.expressionStatements().add(legacyLowerer.createGlobalBindingStatement(declaration.name()));
            assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                    QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                    assembly.expressionStatements().size() - 1));
        }
        hoisted.add(originalStatement);
    }

    private static void registerJsImportBindings(
            List<QinIrJsImport> jsImports,
            Map<String, QinIrExpression> declarationLookup) {
        if (jsImports == null || jsImports.isEmpty()) {
            return;
        }
        for (QinIrJsImport jsImport : jsImports) {
            String localName = jsImport.localName();
            if (localName == null || localName.isBlank()) {
                continue;
            }
            declarationLookup.putIfAbsent(localName, new com.qin.lang.ir.QinIrIdentifierReference(localName));
        }
    }

    record LoweredStatement(
            QinIrConsoleLogValue consoleValueLog,
            QinIrExpressionStatement expressionStatement,
            QinIrConsoleLogStatement objectLog,
            QinIrConsoleLogJavaStaticCall javaStaticCall,
            QinIrJavaInstanceMethodCall javaInstanceMethodCall,
            QinIrConsoleLogJavaInstanceCall javaInstanceConsoleLog) {
    }

    record LoweredImports(List<QinIrJavaImport> javaImports, List<QinIrJsImport> jsImports) {
    }

    static final class MutableProgramAssembly {
        private final List<QinIrConstDeclaration> declarations = new ArrayList<>();
        private final List<QinIrExpressionStatement> expressionStatements = new ArrayList<>();
        private final List<QinIrConsoleLogValue> consoleValueLogs = new ArrayList<>();
        private final List<QinIrConsoleLogStatement> consoleLogs = new ArrayList<>();
        private final List<QinIrJavaImport> javaImports = new ArrayList<>();
        private final List<QinIrJsImport> jsImports = new ArrayList<>();
        private final List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs = new ArrayList<>();
        private final List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls = new ArrayList<>();
        private final List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs = new ArrayList<>();
        private final List<QinIrClassDeclaration> classDeclarations = new ArrayList<>();
        private final List<QinIrProgram.TopLevelExecutionStep> executionSteps = new ArrayList<>();

        List<QinIrConstDeclaration> declarations() {
            return declarations;
        }

        List<QinIrExpressionStatement> expressionStatements() {
            return expressionStatements;
        }

        List<QinIrConsoleLogValue> consoleValueLogs() {
            return consoleValueLogs;
        }

        List<QinIrConsoleLogStatement> consoleLogs() {
            return consoleLogs;
        }

        List<QinIrJavaImport> javaImports() {
            return javaImports;
        }

        List<QinIrJsImport> jsImports() {
            return jsImports;
        }

        List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs() {
            return javaStaticConsoleLogs;
        }

        List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls() {
            return javaInstanceMethodCalls;
        }

        List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs() {
            return javaInstanceConsoleLogs;
        }

        List<QinIrClassDeclaration> classDeclarations() {
            return classDeclarations;
        }

        List<QinIrProgram.TopLevelExecutionStep> executionSteps() {
            return executionSteps;
        }
    }
}
