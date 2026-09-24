package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrFunctionModelArtifact;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaClassLiteralExpression;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinJavaSdkAliasSupport;
import com.slime.ast.AstNode;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.misc.VariableDeclarator;
import com.slime.ast.nodes.modules.ExportAllDeclaration;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.statements.ExpressionStatement;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Comparator;
import java.util.stream.Collectors;

final class QinTopLevelIrAssembler {
    private final QinLegacySlimeIrLowerer legacyLowerer;

    QinTopLevelIrAssembler(QinLegacySlimeIrLowerer legacyLowerer) {
        this.legacyLowerer = legacyLowerer;
    }

    QinIrProgram assembleProgram(
            Program programAst,
            List<QinIrJavaImport> preImports,
            List<QinIrJsImport> preJsImports,
            Map<String, String> declarationClassExportSlots,
            Map<String, QinIrExpression> staticExportSlotValues,
            int sourceLength,
            String sourceText) {
        int functionModelBudget = legacyLowerer.computeFunctionModelBudget(sourceLength);
        legacyLowerer.resetFunctionModelArtifacts();
        legacyLowerer.setFunctionModelBudget(functionModelBudget);
        legacyLowerer.setCurrentSource(sourceText);

        List<AstNode> body = programAst.body();
        if (body.isEmpty()) {
            throw new IllegalArgumentException("Program body cannot be empty");
        }

        MutableProgramAssembly assembly = new MutableProgramAssembly();
        Map<String, String> javaImportLookup = new HashMap<>();
        Map<String, QinIrExpression> declarationLookup = new HashMap<>();
        Set<String> localDeclarationNames = new LinkedHashSet<>(legacyLowerer.collectTopLevelClassNames(body));
        Map<String, String> jsDeclarationClassLookup = new LinkedHashMap<>();
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
            registerJavaSdkAliasJsImports(preJsImports, javaImportLookup);
            registerJsDeclarationClassLookup(
                    preJsImports,
                    jsDeclarationClassLookup,
                    declarationClassExportSlots == null ? Map.of() : declarationClassExportSlots,
                    declarationLookup,
                    javaImportLookup);
            registerJsImportNames(preJsImports, localDeclarationNames);
        }
        registerModuleClassExportSlotImports(
                body,
                declarationClassExportSlots == null ? Map.of() : declarationClassExportSlots,
                jsDeclarationClassLookup,
                localDeclarationNames,
                declarationLookup,
                javaImportLookup);
        registerDeclarationClassQualifiedAliases(
                jsDeclarationClassLookup,
                declarationClassExportSlots == null ? Map.of() : declarationClassExportSlots);
        Set<AstNode> hoistedFunctionDeclarations = hoistTopLevelFunctionDeclarations(
                body,
                assembly,
                enableGlobalBinding,
                javaImportLookup,
                declarationLookup);
        boolean traceDeclarationLookup = Boolean.getBoolean("qin.declarationClass.trace");

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
                registerJavaSdkAliasJsImports(loweredImports.jsImports(), javaImportLookup);
                registerJsDeclarationClassLookup(
                        loweredImports.jsImports(),
                        jsDeclarationClassLookup,
                        declarationClassExportSlots,
                        declarationLookup,
                        javaImportLookup);
                registerJsImportNames(loweredImports.jsImports(), localDeclarationNames);
                continue;
            }
            if (statement instanceof VariableDeclaration variableDeclaration) {
                registerJavaSdkAliasBindings(variableDeclaration, javaImportLookup);
                List<QinIrConstDeclaration> loweredDeclarations = legacyLowerer.lowerVariableDeclaration(
                        variableDeclaration,
                        javaImportLookup,
                        declarationLookup);
                for (QinIrConstDeclaration declaration : loweredDeclarations) {
                    assembly.declarations().add(declaration);
                    assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                            QinIrProgram.TopLevelStatementKind.DECLARATION,
                            assembly.declarations().size() - 1));
                    QinIrExpression declarationBinding = declaration.initializer();
                    if (declarationLookup.get(declaration.name()) instanceof QinIrJavaClassLiteralExpression) {
                        declarationBinding = declarationLookup.get(declaration.name());
                    }
                    declarationLookup.put(declaration.name(), declarationBinding);
                    if (traceDeclarationLookup) {
                        System.err.println("[QinTopLevelIrAssembler] declaration lookup "
                                + declaration.name() + " -> " + declarationBinding.getClass().getSimpleName());
                    }
                    registerJavaSdkAliasBinding(declaration, javaImportLookup);
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
                lowerTopLevelClassDeclaration(
                        classDeclaration,
                        assembly,
                        enableGlobalBinding,
                        javaImportLookup,
                        jsDeclarationClassLookup,
                        declarationLookup,
                        localDeclarationNames,
                        localJvmDeclarations,
                        staticExportSlotValues);
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
                    refineStaticFieldTypeFromTopLevelAssignment(
                            lowered.expressionStatement(),
                            assembly,
                            jsDeclarationClassLookup,
                            localJvmDeclarations);
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
                Object exportedDeclaration = QinSlimeFrontendAdapter.invokeByName(statement, "declaration");
                if (exportedDeclaration instanceof ClassDeclaration classDeclaration) {
                    lowerTopLevelClassDeclaration(
                            classDeclaration,
                            assembly,
                            enableGlobalBinding,
                            javaImportLookup,
                            jsDeclarationClassLookup,
                            declarationLookup,
                            localDeclarationNames,
                            localJvmDeclarations,
                            staticExportSlotValues);
                    continue;
                }
                legacyLowerer.lowerExportNamedDeclaration(
                        statement,
                        assembly,
                        enableGlobalBinding,
                        javaImportLookup,
                        declarationLookup);
                continue;
            }
            if (statement instanceof ExportDefaultDeclaration) {
                Object exportedDeclaration = QinSlimeFrontendAdapter.invokeByName(statement, "declaration");
                if (exportedDeclaration instanceof ClassDeclaration classDeclaration) {
                    lowerTopLevelClassDeclaration(
                            classDeclaration,
                            assembly,
                            enableGlobalBinding,
                            javaImportLookup,
                            jsDeclarationClassLookup,
                            declarationLookup,
                            localDeclarationNames,
                            localJvmDeclarations,
                            staticExportSlotValues);
                    continue;
                }
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

    private void lowerTopLevelClassDeclaration(
            ClassDeclaration classDeclaration,
            MutableProgramAssembly assembly,
            boolean enableGlobalBinding,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrExpression> declarationLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrClassDeclaration> localJvmDeclarations,
            Map<String, QinIrExpression> staticExportSlotValues) {
        QinIrClassDeclaration loweredClass = legacyLowerer.lowerClassDeclarationOrNull(
                classDeclaration,
                javaImportLookup,
                jsDeclarationClassLookup,
                localDeclarationNames,
                declarationLookup,
                localJvmDeclarations,
                staticExportSlotValues);
        if (loweredClass != null) {
            assembly.classDeclarations().add(loweredClass);
            localJvmDeclarations.put(loweredClass.simpleName(), loweredClass);
        }
        String jvmClassBinaryName = null;
        if (jsDeclarationClassLookup != null) {
            jvmClassBinaryName = jsDeclarationClassLookup.get(classDeclaration.id().name());
        }
        if ((jvmClassBinaryName == null || jvmClassBinaryName.isBlank()) && loweredClass != null) {
            jvmClassBinaryName = loweredClass.binaryName();
        }
        QinIrConstDeclaration declaration = legacyLowerer.lowerClassDeclarationValue(
                classDeclaration,
                javaImportLookup,
                declarationLookup,
                loweredClass != null,
                jvmClassBinaryName);
        assembly.declarations().add(declaration);
        assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                QinIrProgram.TopLevelStatementKind.DECLARATION,
                assembly.declarations().size() - 1));
        declarationLookup.put(declaration.name(), declaration.initializer());
        if (!declaration.name().startsWith("__Qin")) {
            registerJavaSdkAliasBinding(declaration, javaImportLookup);
        }
        if (enableGlobalBinding) {
            assembly.expressionStatements().add(legacyLowerer.createGlobalBindingStatement(declaration.name()));
            assembly.executionSteps().add(new QinIrProgram.TopLevelExecutionStep(
                    QinIrProgram.TopLevelStatementKind.EXPRESSION_STATEMENT,
                    assembly.expressionStatements().size() - 1));
        }
    }

    private void refineStaticFieldTypeFromTopLevelAssignment(
            QinIrExpressionStatement statement,
            MutableProgramAssembly assembly,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        StaticFieldAssignment assignment = staticFieldAssignmentOrNull(statement);
        if (assignment == null) {
            return;
        }
        if (refineJavaMetadataFromTopLevelAssignment(
                assignment,
                assembly,
                jsDeclarationClassLookup,
                localJvmDeclarations)) {
            return;
        }
        String ownerName = assignment.ownerName();
        String fieldName = assignment.fieldName();
        if (ownerName == null || ownerName.isBlank() || fieldName == null || fieldName.isBlank()) {
            return;
        }
        QinIrTypeRef assignedType = inferAssignedStaticFieldTypeOrNull(assignment.value());
        if (assignedType == null) {
            return;
        }
        for (int i = 0; i < assembly.classDeclarations().size(); i++) {
            QinIrClassDeclaration declaration = assembly.classDeclarations().get(i);
            if (!ownerName.equals(declaration.simpleName()) && !ownerName.equals(declaration.binaryName())) {
                continue;
            }
            QinIrClassDeclaration refined = refineStaticFieldTypeOrSame(declaration, fieldName, assignedType);
            if (refined == declaration) {
                return;
            }
            assembly.classDeclarations().set(i, refined);
            if (localJvmDeclarations != null) {
                localJvmDeclarations.put(refined.simpleName(), refined);
            }
            return;
        }
    }

    private boolean refineJavaMetadataFromTopLevelAssignment(
            StaticFieldAssignment assignment,
            MutableProgramAssembly assembly,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (assignment.ownerName() == null
                || assignment.ownerName().isBlank()
                || assignment.fieldName() == null
                || assignment.fieldName().isBlank()) {
            return false;
        }
        for (int i = 0; i < assembly.classDeclarations().size(); i++) {
            QinIrClassDeclaration declaration = assembly.classDeclarations().get(i);
            if (!assignment.ownerName().equals(declaration.simpleName())
                    && !assignment.ownerName().equals(declaration.binaryName())) {
                continue;
            }
            QinIrClassDeclaration refined = null;
            if ("__qin_java_interfaces".equals(assignment.fieldName())) {
                List<QinIrTypeRef> interfaces = javaInterfaceTypeRefsOrNull(assignment.value());
                if (interfaces == null) {
                    return false;
                }
                refined = withJavaInterfaces(declaration, interfaces, localJvmDeclarations);
            } else if ("__qin_java_interface".equals(assignment.fieldName())
                    && assignment.value() instanceof QinIrBooleanLiteral booleanLiteral
                    && booleanLiteral.value()) {
                refined = asJavaInterface(declaration, jsDeclarationClassLookup, localJvmDeclarations);
            }
            if (refined == null || refined == declaration) {
                return refined != null;
            }
            assembly.classDeclarations().set(i, refined);
            if (localJvmDeclarations != null) {
                localJvmDeclarations.put(refined.simpleName(), refined);
            }
            return true;
        }
        return false;
    }

    private List<QinIrTypeRef> javaInterfaceTypeRefsOrNull(QinIrExpression value) {
        if (!(value instanceof QinIrArrayLiteral arrayLiteral)) {
            return null;
        }
        List<QinIrTypeRef> interfaces = new ArrayList<>();
        for (QinIrExpression element : arrayLiteral.elements()) {
            if (!(element instanceof QinIrStringLiteral stringLiteral)
                    || stringLiteral.value() == null
                    || stringLiteral.value().isBlank()) {
                return null;
            }
            interfaces.add(QinIrTypeRef.classType(stringLiteral.value()));
        }
        return List.copyOf(interfaces);
    }

    private QinIrClassDeclaration withJavaInterfaces(
            QinIrClassDeclaration declaration,
            List<QinIrTypeRef> interfaces,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (interfaces.isEmpty()) {
            return declaration;
        }
        List<QinIrTypeRef> merged = new ArrayList<>(declaration.implementsTypes());
        for (QinIrTypeRef interfaceType : interfaces) {
            boolean exists = false;
            for (QinIrTypeRef existing : merged) {
                if (Objects.equals(existing.binaryName(), interfaceType.binaryName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                merged.add(interfaceType);
            }
        }
        if (merged.equals(declaration.implementsTypes())) {
            return declaration;
        }
        QinIrClassDeclaration withInterfaces = new QinIrClassDeclaration(
                declaration.packageName(),
                declaration.simpleName(),
                declaration.superType(),
                merged,
                declaration.annotations(),
                declaration.fields(),
                declaration.methods(),
                declaration.staticInitializers(),
                declaration.recordClass(),
                declaration.interfaceClass());
        return refineImplementedInterfaceMethodReturnTypes(withInterfaces, interfaces, localJvmDeclarations);
    }

    private QinIrClassDeclaration refineImplementedInterfaceMethodReturnTypes(
            QinIrClassDeclaration declaration,
            List<QinIrTypeRef> interfaces,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (declaration == null || interfaces == null || interfaces.isEmpty() || declaration.methods().isEmpty()) {
            return declaration;
        }
        List<QinIrMethodDeclaration> methods = new ArrayList<>(declaration.methods());
        boolean changed = false;
        for (int i = 0; i < methods.size(); i++) {
            QinIrMethodDeclaration method = methods.get(i);
            if (!isObjectReturnType(method.returnType())) {
                continue;
            }
            QinIrTypeRef interfaceReturnType = implementedInterfaceReturnTypeOrNull(
                    interfaces,
                    method.name(),
                    method.parameters().size(),
                    localJvmDeclarations);
            if (interfaceReturnType == null
                    || interfaceReturnType.equals(method.returnType())
                    || !shouldRefineGeneratedInterfaceReturnType(interfaceReturnType)) {
                continue;
            }
            methods.set(i, new QinIrMethodDeclaration(
                    method.name(),
                    interfaceReturnType,
                    method.parameters(),
                    method.annotations(),
                    method.returnExpression(),
                    method.bodyStatements(),
                    method.superArguments(),
                    method.explicitSuperConstructorCall(),
                    method.runtimeFunctionDefinition(),
                    method.staticMethod(),
                    method.abstractMethod()));
            changed = true;
        }
        changed |= addImplementedInterfaceOverloadBridges(methods, declaration.binaryName(), interfaces);
        changed |= addLocalImplementedInterfaceOverloadBridges(
                methods,
                declaration.binaryName(),
                interfaces,
                localJvmDeclarations);
        if (!changed) {
            return declaration;
        }
        return new QinIrClassDeclaration(
                declaration.packageName(),
                declaration.simpleName(),
                declaration.superType(),
                declaration.implementsTypes(),
                declaration.annotations(),
                declaration.fields(),
                methods,
                declaration.staticInitializers(),
                declaration.recordClass(),
                declaration.interfaceClass());
    }

    private boolean isObjectReturnType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object".equals(type.binaryName());
    }

    private QinIrTypeRef implementedInterfaceReturnTypeOrNull(
            List<QinIrTypeRef> interfaces,
            String methodName,
            int parameterCount,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        for (QinIrTypeRef interfaceType : interfaces) {
            if (interfaceType == null || interfaceType.binaryName() == null || interfaceType.binaryName().isBlank()) {
                continue;
            }
            QinIrClassDeclaration localInterface = localInterfaceDeclarationOrNull(interfaceType, localJvmDeclarations);
            if (localInterface != null && localInterface.interfaceClass()) {
                for (QinIrMethodDeclaration method : localInterface.methods()) {
                    if (methodName.equals(method.name()) && method.parameters().size() == parameterCount) {
                        return method.returnType();
                    }
                }
            }
            try {
                Class<?> interfaceClass = Class.forName(interfaceType.binaryName());
                for (java.lang.reflect.Method method : interfaceClass.getMethods()) {
                    if (!methodName.equals(method.getName())
                            || method.getParameterCount() != parameterCount) {
                        continue;
                    }
                    QinIrTypeRef returnType = localGeneratedTypeOrSame(
                            javaReturnType(method.getReturnType()),
                            localJvmDeclarations);
                    if (returnType != null) {
                        return returnType;
                    }
                }
            } catch (Throwable ignored) {
                // Interface metadata can also refer to generated declarations
                // in the same module. Reflection refinement is best-effort;
                // unresolved generated interfaces keep the lowered signature.
            }
        }
        return null;
    }

    private boolean addLocalImplementedInterfaceOverloadBridges(
            List<QinIrMethodDeclaration> methods,
            String ownerBinaryName,
            List<QinIrTypeRef> interfaces,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (localJvmDeclarations == null || localJvmDeclarations.isEmpty()) {
            return false;
        }
        boolean changed = false;
        for (QinIrTypeRef interfaceType : interfaces) {
            QinIrClassDeclaration localInterface = localInterfaceDeclarationOrNull(interfaceType, localJvmDeclarations);
            if (localInterface == null || !localInterface.interfaceClass()) {
                continue;
            }
            for (QinIrMethodDeclaration interfaceMethod : localInterface.methods()) {
                OverloadBridgeName bridge = overloadBridgeNameOrNull(interfaceMethod.name());
                if (bridge == null) {
                    continue;
                }
                QinIrMethodDeclaration target = methodByNameAndArity(methods, bridge.originalName(), bridge.parameterCount());
                if (target == null || target.staticMethod() || target.abstractMethod()) {
                    continue;
                }
                if (methodByNameAndArity(methods, interfaceMethod.name(), target.parameters().size()) != null) {
                    continue;
                }
                methods.add(createInterfaceOverloadBridge(
                        interfaceMethod.name(),
                        target,
                        interfaceMethod.returnType(),
                        ownerBinaryName));
                changed = true;
            }
        }
        return changed;
    }

    private QinIrClassDeclaration localInterfaceDeclarationOrNull(
            QinIrTypeRef interfaceType,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (interfaceType == null || interfaceType.binaryName() == null || interfaceType.binaryName().isBlank()) {
            return null;
        }
        String binaryName = interfaceType.binaryName();
        String generatedBinaryName = generatedJavaBinaryName(binaryName);
        for (QinIrClassDeclaration declaration : localJvmDeclarations.values()) {
            if (binaryName.equals(declaration.simpleName())
                    || binaryName.equals(declaration.binaryName())
                    || generatedBinaryName.equals(declaration.simpleName())
                    || generatedBinaryName.equals(declaration.binaryName())) {
                return declaration;
            }
        }
        return null;
    }

    private String generatedJavaBinaryName(String binaryName) {
        return binaryName == null ? "" : binaryName.replace('.', '_');
    }

    private OverloadBridgeName overloadBridgeNameOrNull(String methodName) {
        String prefix = "__qin_overload_";
        if (methodName == null || !methodName.startsWith(prefix)) {
            return null;
        }
        int lastUnderscore = methodName.lastIndexOf('_');
        if (lastUnderscore <= prefix.length()) {
            return null;
        }
        int previousUnderscore = methodName.lastIndexOf('_', lastUnderscore - 1);
        if (previousUnderscore <= prefix.length()) {
            return null;
        }
        try {
            int parameterCount = Integer.parseInt(methodName.substring(previousUnderscore + 1, lastUnderscore));
            String originalName = methodName.substring(prefix.length(), previousUnderscore);
            if (originalName.isBlank()) {
                return null;
            }
            return new OverloadBridgeName(originalName, parameterCount);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private boolean addImplementedInterfaceOverloadBridges(
            List<QinIrMethodDeclaration> methods,
            String ownerBinaryName,
            List<QinIrTypeRef> interfaces) {
        boolean changed = false;
        for (QinIrTypeRef interfaceType : interfaces) {
            if (interfaceType == null || interfaceType.binaryName() == null || interfaceType.binaryName().isBlank()) {
                continue;
            }
            try {
                Class<?> interfaceClass = Class.forName(interfaceType.binaryName());
                if (!interfaceClass.isInterface()) {
                    continue;
                }
                Map<String, List<java.lang.reflect.Method>> overloads = java.util.Arrays.stream(interfaceClass.getMethods())
                        .filter(method -> !method.isSynthetic())
                        .collect(Collectors.groupingBy(
                                java.lang.reflect.Method::getName,
                                LinkedHashMap::new,
                                Collectors.toCollection(ArrayList::new)));
                for (Map.Entry<String, List<java.lang.reflect.Method>> entry : overloads.entrySet()) {
                    List<java.lang.reflect.Method> candidates = entry.getValue();
                    if (candidates.size() <= 1) {
                        continue;
                    }
                    candidates.sort(Comparator
                            .comparingInt(java.lang.reflect.Method::getParameterCount)
                            .thenComparing(method -> java.util.Arrays.stream(method.getParameterTypes())
                                    .map(Class::getName)
                                    .collect(Collectors.joining(","))));
                    for (int overloadIndex = 0; overloadIndex < candidates.size(); overloadIndex++) {
                        java.lang.reflect.Method interfaceMethod = candidates.get(overloadIndex);
                        String originalName = interfaceMethod.getName();
                        int parameterCount = interfaceMethod.getParameterCount();
                        QinIrMethodDeclaration target = methodByNameAndArity(methods, originalName, parameterCount);
                        if (target == null || target.staticMethod() || target.abstractMethod()) {
                            continue;
                        }
                        String bridgeName = "__qin_overload_" + originalName + "_" + parameterCount + "_" + overloadIndex;
                        if (methodByNameAndArity(methods, bridgeName, parameterCount) != null) {
                            continue;
                        }
                        methods.add(createInterfaceOverloadBridge(
                                bridgeName,
                                target,
                                javaReturnType(interfaceMethod.getReturnType()),
                                ownerBinaryName));
                        changed = true;
                    }
                }
            } catch (Throwable ignored) {
                // Interface metadata may also point at generated declarations in
                // the same module. Reflection-based bridge synthesis is
                // best-effort; unresolved generated interfaces keep their
                // existing methods.
            }
        }
        return changed;
    }

    private QinIrMethodDeclaration methodByNameAndArity(
            List<QinIrMethodDeclaration> methods,
            String name,
            int parameterCount) {
        for (QinIrMethodDeclaration method : methods) {
            if (method.name().equals(name) && method.parameters().size() == parameterCount) {
                return method;
            }
        }
        return null;
    }

    private QinIrMethodDeclaration createInterfaceOverloadBridge(
            String bridgeName,
            QinIrMethodDeclaration target,
            QinIrTypeRef bridgeReturnType,
            String ownerBinaryName) {
        List<QinIrExpression> arguments = new ArrayList<>(target.parameters().size());
        for (var parameter : target.parameters()) {
            arguments.add(new QinIrIdentifierReference(parameter.name()));
        }
        return new QinIrMethodDeclaration(
                bridgeName,
                bridgeReturnType == null ? target.returnType() : bridgeReturnType,
                target.parameters(),
                List.of(),
                null,
                List.of(new QinIrReturnStatement(new QinIrInstanceMethodCallExpression(
                        new QinIrThisExpression(),
                        ownerBinaryName,
                        target.name(),
                        arguments))),
                List.of(),
                false,
                null,
                false,
                false);
    }

    private record OverloadBridgeName(String originalName, int parameterCount) {
    }

    private QinIrTypeRef javaReturnType(Class<?> returnClass) {
        if (returnClass == null) {
            return null;
        }
        if (returnClass == void.class || returnClass == Void.class) {
            return QinIrTypeRef.voidType();
        }
        if (returnClass == boolean.class || returnClass == Boolean.class) {
            return QinIrTypeRef.booleanType();
        }
        if (returnClass == int.class || returnClass == Integer.class) {
            return QinIrTypeRef.intType();
        }
        if (returnClass == double.class || returnClass == float.class
                || returnClass == Double.class || returnClass == Float.class) {
            return QinIrTypeRef.doubleType();
        }
        if (returnClass == String.class) {
            return QinIrTypeRef.stringType();
        }
        return QinIrTypeRef.classType(returnClass.getName());
    }

    private QinIrClassDeclaration asJavaInterface(
            QinIrClassDeclaration declaration,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (declaration.interfaceClass()) {
            return refineJavaInterfaceDeclarationMethodReturnTypes(
                    withInterfaceStaticFields(declaration),
                    jsDeclarationClassLookup,
                    localJvmDeclarations);
        }
        QinIrClassDeclaration asInterface = new QinIrClassDeclaration(
                declaration.packageName(),
                declaration.simpleName(),
                null,
                declaration.implementsTypes(),
                declaration.annotations(),
                interfaceStaticFields(declaration.fields()),
                declaration.methods(),
                declaration.staticInitializers(),
                declaration.recordClass(),
                true);
        return refineJavaInterfaceDeclarationMethodReturnTypes(
                asInterface,
                jsDeclarationClassLookup,
                localJvmDeclarations);
    }

    private QinIrClassDeclaration withInterfaceStaticFields(QinIrClassDeclaration declaration) {
        if (declaration == null || !declaration.interfaceClass()) {
            return declaration;
        }
        List<QinIrFieldDeclaration> fields = interfaceStaticFields(declaration.fields());
        if (fields == declaration.fields()) {
            return declaration;
        }
        return new QinIrClassDeclaration(
                declaration.packageName(),
                declaration.simpleName(),
                declaration.superType(),
                declaration.implementsTypes(),
                declaration.annotations(),
                fields,
                declaration.methods(),
                declaration.staticInitializers(),
                declaration.recordClass(),
                declaration.interfaceClass());
    }

    private List<QinIrFieldDeclaration> interfaceStaticFields(List<QinIrFieldDeclaration> fields) {
        if (fields == null || fields.isEmpty()) {
            return fields;
        }
        List<QinIrFieldDeclaration> staticFields = new ArrayList<>(fields.size());
        boolean changed = false;
        for (QinIrFieldDeclaration field : fields) {
            if (field == null || field.staticField()) {
                staticFields.add(field);
                continue;
            }
            staticFields.add(new QinIrFieldDeclaration(
                    field.name(),
                    field.type(),
                    field.annotations(),
                    field.initializer(),
                    true));
            changed = true;
        }
        return changed ? List.copyOf(staticFields) : fields;
    }

    private QinIrClassDeclaration refineJavaInterfaceDeclarationMethodReturnTypes(
            QinIrClassDeclaration declaration,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (declaration == null) {
            return declaration;
        }
        String originalBinaryName = inferredOriginalJavaBinaryName(declaration.binaryName());
        if (originalBinaryName == null) {
            return declaration;
        }
        try {
            Class<?> interfaceClass = Class.forName(originalBinaryName);
            if (!interfaceClass.isInterface()) {
                return declaration;
            }
            List<QinIrMethodDeclaration> methods = new ArrayList<>(declaration.methods());
            boolean changed = addMissingReflectedJavaInterfaceMethods(
                    methods,
                    interfaceClass,
                    jsDeclarationClassLookup,
                    localJvmDeclarations);
            for (int i = 0; i < methods.size(); i++) {
                QinIrMethodDeclaration method = methods.get(i);
                if (!isObjectReturnType(method.returnType())) {
                    continue;
                }
                QinIrTypeRef returnType = reflectedInterfaceReturnTypeOrNull(
                        interfaceClass,
                        method.name(),
                        method.parameters().size(),
                        localJvmDeclarations);
                if (returnType == null
                        || returnType.equals(method.returnType())
                        || !shouldRefineGeneratedInterfaceReturnType(returnType)) {
                    continue;
                }
                methods.set(i, new QinIrMethodDeclaration(
                        method.name(),
                        returnType,
                        method.parameters(),
                        method.annotations(),
                        method.returnExpression(),
                        method.bodyStatements(),
                        method.superArguments(),
                        method.explicitSuperConstructorCall(),
                        method.runtimeFunctionDefinition(),
                        method.staticMethod(),
                        method.abstractMethod()));
                changed = true;
            }
            if (!changed) {
                return declaration;
            }
            return new QinIrClassDeclaration(
                    declaration.packageName(),
                    declaration.simpleName(),
                    declaration.superType(),
                    declaration.implementsTypes(),
                    declaration.annotations(),
                    declaration.fields(),
                    methods,
                    declaration.staticInitializers(),
                    declaration.recordClass(),
                    declaration.interfaceClass());
        } catch (Throwable ignored) {
            return declaration;
        }
    }

    private boolean addMissingReflectedJavaInterfaceMethods(
            List<QinIrMethodDeclaration> methods,
            Class<?> interfaceClass,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        boolean changed = false;
        for (java.lang.reflect.Method reflectedMethod : interfaceClass.getMethods()) {
            int modifiers = reflectedMethod.getModifiers();
            if (reflectedMethod.isSynthetic()
                    || reflectedMethod.getDeclaringClass() == Object.class
                    || Modifier.isStatic(modifiers)
                    || !Modifier.isAbstract(modifiers)
                    || methodByNameAndArity(
                    methods,
                    reflectedMethod.getName(),
                    reflectedMethod.getParameterCount()) != null) {
                continue;
            }
            methods.add(new QinIrMethodDeclaration(
                    reflectedMethod.getName(),
                    reflectedJavaType(
                            reflectedMethod.getReturnType(),
                            jsDeclarationClassLookup,
                            localJvmDeclarations),
                    reflectedInterfaceParameters(
                            reflectedMethod,
                            jsDeclarationClassLookup,
                            localJvmDeclarations),
                    List.of(),
                    null,
                    List.of(),
                    List.of(),
                    false,
                    null,
                    false,
                    true));
            changed = true;
        }
        return changed;
    }

    private List<QinIrParameter> reflectedInterfaceParameters(
            java.lang.reflect.Method reflectedMethod,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        List<QinIrParameter> parameters = new ArrayList<>();
        Class<?>[] parameterTypes = reflectedMethod.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters.add(new QinIrParameter(
                    "arg" + i,
                    reflectedJavaType(parameterTypes[i], jsDeclarationClassLookup, localJvmDeclarations),
                    List.of(),
                    reflectedMethod.isVarArgs() && i == parameterTypes.length - 1));
        }
        return List.copyOf(parameters);
    }

    private QinIrTypeRef reflectedJavaType(
            Class<?> javaType,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        return localGeneratedTypeOrSame(
                javaReturnType(javaType),
                jsDeclarationClassLookup,
                localJvmDeclarations);
    }

    private QinIrTypeRef reflectedInterfaceReturnTypeOrNull(
            Class<?> interfaceClass,
            String methodName,
            int parameterCount,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        for (java.lang.reflect.Method method : interfaceClass.getMethods()) {
            if (methodName.equals(method.getName()) && method.getParameterCount() == parameterCount) {
                return localGeneratedTypeOrSame(javaReturnType(method.getReturnType()), localJvmDeclarations);
            }
        }
        return null;
    }

    private boolean shouldRefineGeneratedInterfaceReturnType(QinIrTypeRef reflectedReturnType) {
        if (reflectedReturnType == null || reflectedReturnType.kind() != QinIrTypeKind.CLASS) {
            return true;
        }
        String binaryName = reflectedReturnType.binaryName();
        return "java.lang.Object".equals(binaryName) || "java.lang.String".equals(binaryName);
    }

    private QinIrTypeRef localGeneratedTypeOrSame(
            QinIrTypeRef reflectedType,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        return localGeneratedTypeOrSame(reflectedType, null, localJvmDeclarations);
    }

    private QinIrTypeRef localGeneratedTypeOrSame(
            QinIrTypeRef reflectedType,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (reflectedType == null
                || reflectedType.kind() != QinIrTypeKind.CLASS
                || reflectedType.binaryName() == null) {
            return reflectedType;
        }
        String generatedBinaryName = generatedJavaBinaryName(reflectedType.binaryName());
        if (localJvmDeclarations != null && !localJvmDeclarations.isEmpty()) {
            QinIrClassDeclaration localDeclaration = localJvmDeclarations.get(generatedBinaryName);
            if (localDeclaration != null) {
                return QinIrTypeRef.classType(localDeclaration.binaryName());
            }
            for (QinIrClassDeclaration candidate : localJvmDeclarations.values()) {
                if (generatedBinaryName.equals(candidate.simpleName())
                        || generatedBinaryName.equals(candidate.binaryName())) {
                    return QinIrTypeRef.classType(candidate.binaryName());
                }
            }
        }
        if (jsDeclarationClassLookup != null && !jsDeclarationClassLookup.isEmpty()) {
            for (String importedBinaryName : jsDeclarationClassLookup.values()) {
                if (generatedBinaryName.equals(importedBinaryName)) {
                    return QinIrTypeRef.classType(importedBinaryName);
                }
            }
        }
        return reflectedType;
    }

    private String inferredOriginalJavaBinaryName(String generatedBinaryName) {
        if (generatedBinaryName == null || generatedBinaryName.isBlank()) {
            return null;
        }
        String candidate = generatedBinaryName.replace('_', '.');
        if (candidate.contains("..")) {
            return null;
        }
        return candidate.equals(generatedBinaryName) ? null : candidate;
    }

    private StaticFieldAssignment staticFieldAssignmentOrNull(QinIrExpressionStatement statement) {
        if (statement == null) {
            return null;
        }
        QinIrExpression expression = statement.expression();
        if (expression instanceof QinIrAssignmentExpression assignmentExpression
                && "=".equals(assignmentExpression.operator())
                && assignmentExpression.target() instanceof QinIrMemberAccessExpression memberAccessExpression) {
            return new StaticFieldAssignment(
                    memberAccessExpression.objectName(),
                    memberAccessExpression.propertyName(),
                    assignmentExpression.value());
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression
                && "Global".equals(builtinCallExpression.receiverName())
                && "__qin_member_set__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3
                && builtinCallExpression.arguments().get(0) instanceof QinIrJavaClassLiteralExpression classLiteral
                && builtinCallExpression.arguments().get(1) instanceof QinIrStringLiteral fieldNameLiteral) {
            return new StaticFieldAssignment(
                    classLiteral.binaryName() == null ? classLiteral.typeName() : classLiteral.binaryName(),
                    fieldNameLiteral.value(),
                    builtinCallExpression.arguments().get(2));
        }
        return null;
    }

    private QinIrClassDeclaration refineStaticFieldTypeOrSame(
            QinIrClassDeclaration declaration,
            String fieldName,
            QinIrTypeRef assignedType) {
        List<QinIrFieldDeclaration> fields = declaration.fields();
        boolean hasStaticField = false;
        for (int i = 0; i < fields.size(); i++) {
            QinIrFieldDeclaration field = fields.get(i);
            if (!field.staticField() || !fieldName.equals(field.name())) {
                continue;
            }
            hasStaticField = true;
            if (!shouldRefineTopLevelAssignedStaticField(field, assignedType)) {
                continue;
            }
            List<QinIrFieldDeclaration> refinedFields = new ArrayList<>(fields);
            refinedFields.set(i, new QinIrFieldDeclaration(
                    field.name(),
                    assignedType,
                    field.annotations(),
                    field.initializer(),
                    field.staticField()));
            return new QinIrClassDeclaration(
                    declaration.packageName(),
                    declaration.simpleName(),
                    declaration.superType(),
                    declaration.implementsTypes(),
                    declaration.annotations(),
                    refinedFields,
                    declaration.methods(),
                    declaration.staticInitializers(),
                    declaration.recordClass(),
                    declaration.interfaceClass());
        }
        if (hasStaticField) {
            return declaration;
        }
        List<QinIrFieldDeclaration> refinedFields = new ArrayList<>(fields);
        refinedFields.add(new QinIrFieldDeclaration(
                fieldName,
                assignedType,
                List.of(),
                null,
                true));
        return new QinIrClassDeclaration(
                declaration.packageName(),
                declaration.simpleName(),
                declaration.superType(),
                declaration.implementsTypes(),
                declaration.annotations(),
                refinedFields,
                declaration.methods(),
                declaration.staticInitializers(),
                declaration.recordClass(),
                declaration.interfaceClass());
    }

    private boolean shouldRefineTopLevelAssignedStaticField(
            QinIrFieldDeclaration field,
            QinIrTypeRef assignedType) {
        if (field == null || assignedType == null) {
            return false;
        }
        QinIrExpression initializer = field.initializer();
        if (initializer != null && !(initializer instanceof QinIrNullLiteral)) {
            return false;
        }
        QinIrTypeRef existingType = field.type();
        if (existingType == null || existingType.kind() == QinIrTypeKind.CLASS) {
            return existingType == null
                    || "java.lang.Object".equals(existingType.binaryName())
                    || Objects.equals(existingType.binaryName(), assignedType.binaryName());
        }
        return assignedType.kind() == QinIrTypeKind.CLASS;
    }

    private QinIrTypeRef inferAssignedStaticFieldTypeOrNull(QinIrExpression value) {
        if (value instanceof QinIrJavaNewExpression javaNewExpression) {
            return QinIrTypeRef.classType(javaNewExpression.ownerBinaryName());
        }
        String javaSdkNewOwner = javaSdkFacadeNewOwnerOrNull(value);
        if (javaSdkNewOwner != null) {
            return QinIrTypeRef.classType(javaSdkNewOwner);
        }
        if (value instanceof QinIrStringLiteral) {
            return QinIrTypeRef.stringType();
        }
        if (value instanceof QinIrBooleanLiteral) {
            return QinIrTypeRef.booleanType();
        }
        if (value instanceof QinIrNumberLiteral) {
            return QinIrTypeRef.doubleType();
        }
        return null;
    }

    private String javaSdkFacadeNewOwnerOrNull(QinIrExpression value) {
        if (!(value instanceof QinIrBuiltinCallExpression newCall)
                || !"Global".equals(newCall.receiverName())
                || !"__qin_new__".equals(newCall.methodName())
                || newCall.arguments().isEmpty()) {
            return null;
        }
        QinIrExpression callee = newCall.arguments().get(0);
        String facadeName = javaSdkFacadeNameOrNull(callee);
        if (!QinJavaSdkAliasSupport.isKnownAlias(facadeName)) {
            return null;
        }
        return QinJavaSdkAliasSupport.canonicalBinaryName(facadeName);
    }

    private String javaSdkFacadeNameOrNull(QinIrExpression callee) {
        if (callee instanceof QinIrIdentifierReference identifierReference) {
            return identifierReference.name();
        }
        if (callee instanceof QinIrStringLiteral stringLiteral) {
            return stringLiteral.value();
        }
        if (callee instanceof QinIrBuiltinCallExpression globalLookup
                && "Global".equals(globalLookup.receiverName())
                && "__qin_global__".equals(globalLookup.methodName())
                && globalLookup.arguments().size() == 1
                && globalLookup.arguments().get(0) instanceof QinIrStringLiteral stringLiteral) {
            return stringLiteral.value();
        }
        return null;
    }

    private record StaticFieldAssignment(String ownerName, String fieldName, QinIrExpression value) {
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

    private static void registerJavaSdkAliasBinding(
            QinIrConstDeclaration declaration,
            Map<String, String> javaImportLookup) {
        if (declaration == null
                || declaration.name() == null
                || declaration.name().isBlank()
                || javaImportLookup == null) {
            return;
        }
        if (declaration.name().startsWith("__Qin")
                && !QinJavaSdkAliasSupport.isKnownAlias(declaration.name())) {
            return;
        }
        String canonicalName = null;
        if (QinJavaSdkAliasSupport.isKnownAlias(declaration.name())) {
            canonicalName = QinJavaSdkAliasSupport.canonicalBinaryName(declaration.name());
        } else if (declaration.initializer() instanceof QinIrIdentifierReference identifierReference) {
            canonicalName = javaImportLookup.get(identifierReference.name());
            if (canonicalName == null && QinJavaSdkAliasSupport.isKnownAlias(identifierReference.name())) {
                canonicalName = QinJavaSdkAliasSupport.canonicalBinaryName(identifierReference.name());
            }
        } else if (declaration.initializer() instanceof QinIrJavaClassLiteralExpression classLiteral) {
            canonicalName = classLiteral.binaryName();
        }
        if (canonicalName == null || canonicalName.isBlank()) {
            return;
        }
        javaImportLookup.putIfAbsent(declaration.name(), canonicalName);
    }

    private static void registerJavaSdkAliasBindings(
            VariableDeclaration variableDeclaration,
            Map<String, String> javaImportLookup) {
        if (variableDeclaration == null
                || variableDeclaration.declarations() == null
                || variableDeclaration.declarations().isEmpty()
                || javaImportLookup == null) {
            return;
        }
        for (VariableDeclarator declarator : variableDeclaration.declarations()) {
            if (declarator == null || !(declarator.id() instanceof Identifier targetIdentifier)) {
                continue;
            }
            String targetName = targetIdentifier.name();
            if (targetName == null || targetName.isBlank()) {
                continue;
            }
            if (targetName.startsWith("__Qin")
                    && !QinJavaSdkAliasSupport.isKnownAlias(targetName)) {
                continue;
            }
            String canonicalName = null;
            if (QinJavaSdkAliasSupport.isKnownAlias(targetName)) {
                canonicalName = QinJavaSdkAliasSupport.canonicalBinaryName(targetName);
            } else if (declarator.init() instanceof Identifier sourceIdentifier) {
                canonicalName = javaImportLookup.get(sourceIdentifier.name());
                if (canonicalName == null && QinJavaSdkAliasSupport.isKnownAlias(sourceIdentifier.name())) {
                    canonicalName = QinJavaSdkAliasSupport.canonicalBinaryName(sourceIdentifier.name());
                }
            }
            if (canonicalName != null && !canonicalName.isBlank()) {
                javaImportLookup.putIfAbsent(targetName, canonicalName);
            }
        }
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

    private static void registerJavaSdkAliasJsImports(
            List<QinIrJsImport> jsImports,
            Map<String, String> javaImportLookup) {
        if (jsImports == null || jsImports.isEmpty() || javaImportLookup == null) {
            return;
        }
        for (QinIrJsImport jsImport : jsImports) {
            if (!"@qin/java-sdk-js".equals(jsImport.moduleName())) {
                continue;
            }
            String localName = jsImport.localName();
            if (localName == null || localName.isBlank()) {
                continue;
            }
            String canonicalName = null;
            if (QinJavaSdkAliasSupport.isKnownAlias(localName)) {
                canonicalName = QinJavaSdkAliasSupport.canonicalBinaryName(localName);
            } else if (QinJavaSdkAliasSupport.isKnownAlias(jsImport.importedName())) {
                canonicalName = QinJavaSdkAliasSupport.canonicalBinaryName(jsImport.importedName());
            }
            if (canonicalName != null && !canonicalName.isBlank()) {
                javaImportLookup.putIfAbsent(localName, canonicalName);
            }
        }
    }

    private static void registerJsImportNames(
            List<QinIrJsImport> jsImports,
            Set<String> localDeclarationNames) {
        if (jsImports == null || jsImports.isEmpty() || localDeclarationNames == null) {
            return;
        }
        for (QinIrJsImport jsImport : jsImports) {
            String localName = jsImport.localName();
            if (localName == null || localName.isBlank()) {
                continue;
            }
            localDeclarationNames.add(localName);
        }
    }

    private static void registerJsDeclarationClassLookup(
            List<QinIrJsImport> jsImports,
            Map<String, String> jsDeclarationClassLookup,
            Map<String, String> declarationClassExportSlots,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        if (jsImports == null || jsImports.isEmpty() || jsDeclarationClassLookup == null) {
            return;
        }
        boolean trace = Boolean.getBoolean("qin.declarationClass.trace");
        for (QinIrJsImport jsImport : jsImports) {
            if (trace) {
                System.err.println("[QinTopLevelIrAssembler] js import raw " + jsImport);
            }
            String importedName = jsImport.importedName();
            String localName = jsImport.localName();
            if (importedName == null
                    || importedName.isBlank()
                    || localName == null
                    || localName.isBlank()
                    || "default".equals(importedName)
                    || "*".equals(importedName)) {
                continue;
            }
            String declarationClassName =
                    resolveImportedDeclarationClassName(importedName, declarationClassExportSlots);
            boolean provenDeclarationClass = declarationClassName != null && !declarationClassName.isBlank();
            if (declarationClassName == null || declarationClassName.isBlank()) {
                declarationClassName = importedName;
            }
            String canonicalDeclarationClassName = canonicalDeclarationBinaryName(declarationClassName);
            jsDeclarationClassLookup.putIfAbsent(localName, canonicalDeclarationClassName);
            registerDeclarationClassAlias(jsDeclarationClassLookup, canonicalDeclarationClassName);
            if (provenDeclarationClass) {
                if (declarationLookup != null) {
                    declarationLookup.put(
                            localName,
                            new QinIrJavaClassLiteralExpression(localName, canonicalDeclarationClassName));
                }
                if (javaImportLookup != null) {
                    javaImportLookup.putIfAbsent(localName, canonicalDeclarationClassName);
                }
            }
            if (trace) {
                System.err.println("[QinTopLevelIrAssembler] js import class lookup "
                        + localName + " -> " + canonicalDeclarationClassName);
            }
        }
    }

    private static String resolveImportedDeclarationClassName(
            String importedName,
            Map<String, String> declarationClassExportSlots) {
        if (importedName == null
                || importedName.isBlank()
                || declarationClassExportSlots == null
                || declarationClassExportSlots.isEmpty()) {
            return null;
        }
        String matched = null;
        for (String declarationClassName : declarationClassExportSlots.values()) {
            if (declarationClassName == null || declarationClassName.isBlank()) {
                continue;
            }
            if (!matchesDeclarationClassAlias(importedName, declarationClassName)) {
                continue;
            }
            if (matched != null && !matched.equals(declarationClassName)) {
                return null;
            }
            matched = declarationClassName;
        }
        return matched == null ? null : canonicalDeclarationBinaryName(matched);
    }

    private static String simpleDeclarationClassName(String declarationClassName) {
        if (declarationClassName == null || declarationClassName.isBlank()) {
            return declarationClassName;
        }
        int split = Math.max(declarationClassName.lastIndexOf('.'), declarationClassName.lastIndexOf('$'));
        if (split < 0) {
            split = declarationClassName.lastIndexOf('_');
        }
        return split < 0 || split + 1 >= declarationClassName.length()
                ? declarationClassName
                : declarationClassName.substring(split + 1);
    }

    private static boolean matchesDeclarationClassAlias(String name, String declarationClassName) {
        if (name == null || name.isBlank() || declarationClassName == null || declarationClassName.isBlank()) {
            return false;
        }
        if (name.equals(declarationClassName)
                || name.equals(flattenDeclarationClassName(declarationClassName))
                || name.equals(simpleDeclarationClassName(declarationClassName))) {
            return true;
        }
        String flattenedName = flattenDeclarationClassName(declarationClassName);
        return name.equals(simpleDeclarationClassName(flattenedName));
    }

    private static String flattenDeclarationClassName(String declarationClassName) {
        return declarationClassName == null ? null : declarationClassName.replace('.', '_');
    }

    private static void registerModuleClassExportSlotImports(
            List<AstNode> body,
            Map<String, String> declarationClassExportSlots,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        if (body == null || body.isEmpty() || declarationClassExportSlots == null
                || declarationClassExportSlots.isEmpty()) {
            return;
        }
        boolean trace = Boolean.getBoolean("qin.declarationClass.trace");
        for (AstNode statement : body) {
            if (!(statement instanceof VariableDeclaration variableDeclaration)
                    || variableDeclaration.declarations() == null) {
                continue;
            }
            for (Object declarator : variableDeclaration.declarations()) {
                Object id = QinSlimeFrontendAdapter.invokeByName(declarator, "id");
                if (!(id instanceof com.slime.ast.nodes.expressions.Identifier identifier)) {
                    continue;
                }
                String exportSlot = moduleExportGetSlotName(
                        QinSlimeFrontendAdapter.invokeByName(declarator, "init"));
                if (exportSlot == null) {
                    continue;
                }
                String declarationClassName = declarationClassExportSlots.get(exportSlot);
                if (declarationClassName == null || declarationClassName.isBlank()) {
                    continue;
                }
                String canonicalDeclarationClassName = canonicalDeclarationBinaryName(declarationClassName);
                if (declarationLookup != null) {
                    declarationLookup.put(
                            identifier.name(),
                            new QinIrJavaClassLiteralExpression(identifier.name(), canonicalDeclarationClassName));
                }
                if (javaImportLookup != null) {
                    javaImportLookup.putIfAbsent(identifier.name(), canonicalDeclarationClassName);
                }
                jsDeclarationClassLookup.putIfAbsent(identifier.name(), canonicalDeclarationClassName);
                registerDeclarationClassAlias(jsDeclarationClassLookup, canonicalDeclarationClassName);
                if (trace) {
                    System.err.println("[QinTopLevelIrAssembler] module class alias "
                            + identifier.name() + " -> " + canonicalDeclarationClassName);
                }
                if (localDeclarationNames != null) {
                    localDeclarationNames.add(identifier.name());
                }
            }
        }
    }

    private static void registerDeclarationClassAlias(
            Map<String, String> jsDeclarationClassLookup,
            String declarationClassName) {
        if (jsDeclarationClassLookup == null
                || declarationClassName == null
                || declarationClassName.isBlank()) {
            return;
        }
        jsDeclarationClassLookup.putIfAbsent(declarationClassName, declarationClassName);
        String flattenedName = flattenDeclarationClassName(declarationClassName);
        if (flattenedName != null && !flattenedName.isBlank()) {
            jsDeclarationClassLookup.putIfAbsent(flattenedName, declarationClassName);
        }
        int split = Math.max(declarationClassName.lastIndexOf('.'), declarationClassName.lastIndexOf('$'));
        if (split < 0) {
            split = declarationClassName.lastIndexOf('_');
        }
        if (split < 0 || split + 1 >= declarationClassName.length()) {
            return;
        }
        String simpleName = declarationClassName.substring(split + 1);
        if (!simpleName.isBlank()) {
            jsDeclarationClassLookup.putIfAbsent(simpleName, declarationClassName);
        }
    }

    private static void registerDeclarationClassQualifiedAliases(
            Map<String, String> jsDeclarationClassLookup,
            Map<String, String> declarationClassExportSlots) {
        if (jsDeclarationClassLookup == null
                || declarationClassExportSlots == null
                || declarationClassExportSlots.isEmpty()) {
            return;
        }
        for (String declarationClassName : declarationClassExportSlots.values()) {
            if (declarationClassName == null || declarationClassName.isBlank()) {
                continue;
            }
            String canonicalDeclarationClassName = canonicalDeclarationBinaryName(declarationClassName);
            jsDeclarationClassLookup.putIfAbsent(canonicalDeclarationClassName, canonicalDeclarationClassName);
            String flattenedName = flattenDeclarationClassName(canonicalDeclarationClassName);
            if (flattenedName != null && !flattenedName.isBlank()) {
                jsDeclarationClassLookup.putIfAbsent(flattenedName, canonicalDeclarationClassName);
            }
        }
    }

    private static String canonicalDeclarationBinaryName(String declarationClassName) {
        if (declarationClassName == null || declarationClassName.isBlank()) {
            return declarationClassName;
        }
        if (declarationClassName.startsWith("__Qin")) {
            return declarationClassName;
        }
        String canonical = QinJavaSdkAliasSupport.canonicalBinaryName(declarationClassName);
        if (!declarationClassName.equals(canonical)) {
            return canonical;
        }
        if (declarationClassName.indexOf('_') < 0) {
            return canonical;
        }
        String dottedCandidate = declarationClassName.replace('_', '.');
        if (dottedCandidate.contains("..")) {
            return declarationClassName;
        }
        String dottedCanonical = QinJavaSdkAliasSupport.canonicalBinaryName(dottedCandidate);
        return Objects.equals(dottedCandidate, dottedCanonical) ? declarationClassName : dottedCanonical;
    }

    private static String moduleExportGetSlotName(Object init) {
        if (init == null || !"CallExpression".equals(QinSlimeFrontendAdapter.simpleName(init))) {
            return null;
        }
        Object callee = QinSlimeFrontendAdapter.invokeByName(init, "callee");
        if (!(callee instanceof com.slime.ast.nodes.expressions.Identifier identifier)
                || !"__qin_export_get__".equals(identifier.name())) {
            return null;
        }
        List<?> arguments = QinSlimeFrontendAdapter.asListStatic(
                QinSlimeFrontendAdapter.invokeByName(init, "arguments"),
                "CallExpression.arguments");
        if (arguments.size() != 1
                || !(arguments.get(0) instanceof com.slime.ast.nodes.expressions.Identifier slotIdentifier)) {
            if (arguments.size() == 1 && "CallExpression".equals(QinSlimeFrontendAdapter.simpleName(arguments.get(0)))) {
                Object globalCall = arguments.get(0);
                Object globalCallee = QinSlimeFrontendAdapter.invokeByName(globalCall, "callee");
                if (globalCallee instanceof com.slime.ast.nodes.expressions.Identifier globalIdentifier
                        && "__qin_global__".equals(globalIdentifier.name())) {
                    List<?> globalArguments = QinSlimeFrontendAdapter.asListStatic(
                            QinSlimeFrontendAdapter.invokeByName(globalCall, "arguments"),
                            "CallExpression.arguments");
                    if (globalArguments.size() == 1) {
                        Object globalArgument = globalArguments.get(0);
                        if (globalArgument instanceof com.slime.ast.nodes.expressions.Identifier globalSlotIdentifier) {
                            return globalSlotIdentifier.name();
                        }
                        if (globalArgument instanceof com.slime.ast.nodes.expressions.Literal literal
                                && literal.value() != null) {
                            return String.valueOf(literal.value());
                        }
                    }
                }
            }
            return null;
        }
        return slotIdentifier.name();
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
