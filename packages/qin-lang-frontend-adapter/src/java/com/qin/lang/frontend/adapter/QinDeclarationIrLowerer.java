package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrArrayCreationExpression;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBoundMethodReferenceExpression;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrCastExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrContinueStatement;
import com.qin.lang.ir.QinIrDoWhileStatementNode;
import com.qin.lang.ir.QinIrElementAccessExpression;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrForEachStatement;
import com.qin.lang.ir.QinIrForStatement;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrJavaClassLiteralExpression;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrLocalDeclarationStatement;
import com.qin.lang.ir.QinIrLocalVariableDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrShortCircuitExpression;
import com.qin.lang.ir.QinIrStatement;
import com.qin.lang.ir.QinIrStatementExpression;
import com.qin.lang.ir.QinIrSpreadArgumentExpression;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrSuperMethodCallExpression;
import com.qin.lang.ir.QinIrSwitchCase;
import com.qin.lang.ir.QinIrSwitchStatement;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrThrowStatement;
import com.qin.lang.ir.QinIrTryStatement;
import com.qin.lang.ir.QinIrCatchClause;
import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinIrUpdateExpression;
import com.qin.lang.ir.QinIrWhileStatementNode;
import com.qin.lang.ir.QinJavaSdkAliasSupport;
import com.qin.parser.QinParserRuntimeNames;
import com.slime.ast.AstNode;
import com.slime.ast.Expression;
import com.slime.ast.Statement;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.expressions.AssignmentExpression;
import com.slime.ast.nodes.expressions.ArrowFunctionExpression;
import com.slime.ast.nodes.expressions.BinaryExpression;
import com.slime.ast.nodes.expressions.CallExpression;
import com.slime.ast.nodes.expressions.ConditionalExpression;
import com.slime.ast.nodes.expressions.FunctionExpression;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.Literal;
import com.slime.ast.nodes.expressions.LogicalExpression;
import com.slime.ast.nodes.expressions.MemberExpression;
import com.slime.ast.nodes.expressions.NewExpression;
import com.slime.ast.nodes.expressions.ObjectExpression;
import com.slime.ast.nodes.expressions.ParenthesizedExpression;
import com.slime.ast.nodes.expressions.SequenceExpression;
import com.slime.ast.nodes.expressions.ThisExpression;
import com.slime.ast.nodes.misc.CatchClause;
import com.slime.ast.nodes.misc.Decorator;
import com.slime.ast.nodes.misc.FunctionParameter;
import com.slime.ast.nodes.misc.MethodDefinition;
import com.slime.ast.nodes.misc.Property;
import com.slime.ast.nodes.misc.PropertyDefinition;
import com.slime.ast.nodes.misc.SpreadElement;
import com.slime.ast.nodes.misc.SwitchCase;
import com.slime.ast.nodes.patterns.ArrayPattern;
import com.slime.ast.nodes.patterns.AssignmentPattern;
import com.slime.ast.nodes.patterns.ObjectPattern;
import com.slime.ast.nodes.patterns.RestElement;
import com.slime.ast.nodes.statements.BlockStatement;
import com.slime.ast.nodes.statements.BreakStatement;
import com.slime.ast.nodes.statements.ContinueStatement;
import com.slime.ast.nodes.statements.DoWhileStatement;
import com.slime.ast.nodes.statements.EmptyStatement;
import com.slime.ast.nodes.statements.ExpressionStatement;
import com.slime.ast.nodes.statements.ForStatement;
import com.slime.ast.nodes.statements.IfStatement;
import com.slime.ast.nodes.statements.ReturnStatement;
import com.slime.ast.nodes.statements.SwitchStatement;
import com.slime.ast.nodes.statements.ThrowStatement;
import com.slime.ast.nodes.statements.TryStatement;
import com.slime.ast.nodes.statements.WhileStatement;
import com.slime.ast.nodes.typescript.TSKeywordType;
import com.slime.ast.nodes.typescript.TSTypeAnnotation;
import com.slime.ast.nodes.typescript.TSTypeParameter;
import com.slime.ast.nodes.typescript.TSTypeParameterDeclaration;
import com.slime.ast.nodes.typescript.TSTypeParameterInstantiation;
import com.slime.ast.nodes.typescript.TSTypeReference;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Method;

/**
 * Qin-owned boundary for declaration-oriented IR lowering.
 *
 * <p>This component now owns the declaration subset lowering path directly.
 * It still reuses a narrow set of legacy helpers for generic runtime
 * expression/literal behavior where that logic has not been migrated yet.
 */
final class QinDeclarationIrLowerer {
    private static final String SUBHUTI_RULE_ANNOTATION = "com.subhuti.parser.SubhutiRule";
    private static final String STRUCTURAL_SLIME_AST_TYPE_PREFIX = "__qin.struct.";
    private static final Map<Class<?>, Method[]> THIS_MEMBER_RECORD_ACCESSORS = new ConcurrentHashMap<>();
    private static final Set<String> THIS_MEMBER_SCAN_IGNORED_COMPONENTS =
            Set.of("loc", "location", "range", "raw", "comments", "tokens");

    private final QinSlimeFrontendAdapter adapter;
    private boolean currentDeclarationStatic;
    private Map<String, QinIrTypeRef> currentDeclarationValueTypes = Map.of();
    private int syntheticForOfItemCounter;

    QinDeclarationIrLowerer(QinSlimeFrontendAdapter adapter) {
        this.adapter = Objects.requireNonNull(adapter, "adapter cannot be null");
    }

    QinIrClassDeclaration lowerClassDeclarationOrNull(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations,
            Map<String, QinIrExpression> staticExportSlotValues) {
        boolean jvmSuperclass = classDeclaration.superClass() != null
                && isResolvableDeclarationSuperClass(
                classDeclaration.superClass(),
                javaImportLookup,
                jsDeclarationClassLookup,
                localDeclarationNames,
                localJvmDeclarations);
        if (!isDeclarationCompatibleClass(
                classDeclaration,
                javaImportLookup,
                jsDeclarationClassLookup,
                localDeclarationNames,
                localJvmDeclarations)) {
            if (jvmSuperclass) {
                try {
                    return lowerJavaSuperclassClassWithRuntimeMethods(
                            classDeclaration,
                            javaImportLookup,
                            jsDeclarationClassLookup,
                            localDeclarationNames,
                            declarationLookup,
                            localJvmDeclarations);
                } catch (IllegalArgumentException error) {
                    if (isUnsupportedDecoratorError(error)) {
                        throw error;
                    }
                    if (isDeclarationSubsetError(error)) {
                        traceDeclarationClassReject(classDeclaration, "runtime-backed lowering error " + error.getMessage());
                        return null;
                    }
                    throw error;
                }
            }
            QinIrClassDeclaration staticFieldFacade = lowerStaticFieldFacadeClassOrNull(
                    classDeclaration,
                    javaImportLookup,
                    jsDeclarationClassLookup,
                    localDeclarationNames,
                    declarationLookup);
            if (staticFieldFacade != null) {
                return staticFieldFacade;
            }
            return null;
        }
        try {
            return lowerClassDeclarationAsDeclaration(
                    classDeclaration,
                    javaImportLookup,
                    jsDeclarationClassLookup,
                    localDeclarationNames,
                    declarationLookup,
                    localJvmDeclarations,
                    staticExportSlotValues);
        } catch (IllegalArgumentException error) {
            if (isUnsupportedDecoratorError(error)) {
                throw error;
            }
            if (isDeclarationSubsetError(error)) {
                traceDeclarationClassReject(classDeclaration, "lowering error " + error.getMessage());
                if (jvmSuperclass) {
                    try {
                        return lowerJavaSuperclassClassWithRuntimeMethods(
                                classDeclaration,
                                javaImportLookup,
                                jsDeclarationClassLookup,
                                localDeclarationNames,
                                declarationLookup,
                                localJvmDeclarations);
                    } catch (IllegalArgumentException runtimeBackedError) {
                        if (isUnsupportedDecoratorError(runtimeBackedError)) {
                            throw runtimeBackedError;
                        }
                        if (!isDeclarationSubsetError(runtimeBackedError)) {
                            throw runtimeBackedError;
                        }
                        traceDeclarationClassReject(
                                classDeclaration,
                                "runtime-backed lowering error " + runtimeBackedError.getMessage());
                    }
                }
                QinIrClassDeclaration staticFieldFacade = lowerStaticFieldFacadeClassOrNull(
                        classDeclaration,
                        javaImportLookup,
                        jsDeclarationClassLookup,
                        localDeclarationNames,
                        declarationLookup);
                if (staticFieldFacade != null) {
                    return staticFieldFacade;
                }
                return null;
            }
            throw error;
        }
    }

    private QinIrClassDeclaration lowerStaticFieldFacadeClassOrNull(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrExpression> declarationLookup) {
        if (classDeclaration == null || classDeclaration.id() == null
                || classDeclaration.body() == null || classDeclaration.body().body() == null) {
            return null;
        }
        List<QinIrFieldDeclaration> fields = new ArrayList<>();
        List<QinIrMethodDeclaration> methodSignatures = new ArrayList<>();
        for (AstNode member : classDeclaration.body().body()) {
            if (member instanceof PropertyDefinition propertyDefinition) {
                if (isDeclareOnlyField(propertyDefinition)
                        || !propertyDefinition.isStatic()
                        || propertyDefinition.computed()) {
                    continue;
                }
                QinIrFieldDeclaration loweredField = lowerFieldDeclarationOrNull(
                        propertyDefinition,
                        javaImportLookup,
                        jsDeclarationClassLookup,
                        localDeclarationNames,
                        classDeclaration.id().name(),
                        declarationLookup);
                if (loweredField != null && loweredField.staticField()) {
                    fields.add(loweredField);
                }
                continue;
            }
            if (member instanceof MethodDefinition methodDefinition
                    && methodDefinition.isStatic()
                    && !methodDefinition.computed()
                    && !isConstructorMethod(methodDefinition)) {
                QinIrMethodDeclaration methodSignature = lowerMethodSignature(
                        methodDefinition,
                        javaImportLookup,
                        localDeclarationNames);
                if (methodSignature != null) {
                    methodSignature = applyGeneratedJavaSdkFacadeSignatureTypes(
                            classDeclaration.id().name(),
                            methodSignature);
                    methodSignatures.add(methodSignature);
                }
            }
        }
        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        if (!methodSignatures.isEmpty()) {
            DeclarationClassContext classContext = new DeclarationClassContext(
                    classDeclaration.id().name(),
                    fields,
                    methodSignatures,
                    declarationLookup,
                    Map.of(),
                    jsDeclarationClassLookup,
                    localDeclarationNames);
            for (AstNode member : classDeclaration.body().body()) {
                if (!(member instanceof MethodDefinition methodDefinition)
                        || !methodDefinition.isStatic()
                        || methodDefinition.computed()
                        || isConstructorMethod(methodDefinition)) {
                    continue;
                }
                try {
                    QinIrMethodDeclaration loweredMethod = lowerMethodDeclarationOrNull(
                            methodDefinition,
                            javaImportLookup,
                            localDeclarationNames,
                            classContext);
                    if (loweredMethod != null && loweredMethod.staticMethod()) {
                        methods.add(loweredMethod);
                    }
                } catch (IllegalArgumentException error) {
                    if (isUnsupportedDecoratorError(error)) {
                        throw error;
                    }
                    if (!isDeclarationSubsetError(error)) {
                        throw error;
                    }
                    traceDeclarationClassReject(
                            classDeclaration,
                            "skip static facade method "
                                    + methodNameDiagnostic(methodDefinition) + " " + error.getMessage());
                }
            }
        }
        if (fields.isEmpty() && methods.isEmpty()) {
            return null;
        }
        return new QinIrClassDeclaration(
                null,
                classDeclaration.id().name(),
                QinIrTypeRef.classType("java.lang.Object"),
                lowerAnnotations(classDeclaration.decorators(), javaImportLookup),
                fields,
                methods);
    }

    private QinIrClassDeclaration lowerClassDeclarationAsDeclaration(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations,
            Map<String, QinIrExpression> staticExportSlotValues) {

        List<QinIrFieldDeclaration> fields = new ArrayList<>();
        if (classDeclaration.body() != null && classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof PropertyDefinition propertyDefinition) {
                    if (isDeclareOnlyField(propertyDefinition)) {
                        continue;
                    }
                    QinIrFieldDeclaration loweredField = lowerFieldDeclarationOrNull(
                            propertyDefinition,
                            javaImportLookup,
                            jsDeclarationClassLookup,
                            localDeclarationNames,
                            classDeclaration.id().name(),
                            declarationLookup);
                    if (loweredField != null) {
                        fields.add(loweredField);
                    }
                }
            }
            collectConstructorAssignedFields(
                    classDeclaration,
                    javaImportLookup,
                    jsDeclarationClassLookup,
                    localDeclarationNames,
                    classDeclaration.id().name(),
                    declarationLookup,
                    fields);
        }
        Set<String> constructorParameterFieldNames = collectConstructorParameterBackedFields(
                classDeclaration,
                javaImportLookup,
                localDeclarationNames,
                fields);
        Map<String, Set<Integer>> nullableGeneratedConstructorParameters =
                collectNullableGeneratedConstructorDelegateParameters(classDeclaration);
        widenFieldsAssignedFromNullableGeneratedConstructorParameters(
                fields,
                classDeclaration,
                javaImportLookup,
                localDeclarationNames,
                nullableGeneratedConstructorParameters);

        QinIrTypeRef superType =
                lowerSuperType(classDeclaration, javaImportLookup, jsDeclarationClassLookup, localDeclarationNames);
        QinIrTypeRef inheritedOverrideSuperType = classDeclaration.superClass() == null ? null : superType;

        List<QinIrMethodDeclaration> methodSignatures = new ArrayList<>();
        if (classDeclaration.body() != null && classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof MethodDefinition methodDefinition && !isConstructorMethod(methodDefinition)) {
                    QinIrMethodDeclaration methodSignature = lowerMethodSignature(
                            methodDefinition,
                            javaImportLookup,
                            localDeclarationNames);
                    if (methodSignature != null) {
                        methodSignature = widenNullableGeneratedConstructorParameters(
                                methodSignature,
                                nullableGeneratedConstructorParameters);
                        methodSignature = applyGeneratedJavaSdkFacadeSignatureTypes(
                                classDeclaration.id().name(),
                                methodSignature);
                        methodSignature = applyInheritedOverrideSignatureParameterTypes(
                                methodSignature,
                                inheritedOverrideSuperType,
                                localJvmDeclarations);
                        methodSignatures.add(methodSignature);
                    }
                }
            }
        }

        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        DeclarationClassContext classContext =
                new DeclarationClassContext(
                        classDeclaration.id().name(),
                        fields,
                        methodSignatures,
                        declarationLookup,
                        staticExportSlotValues,
                        jsDeclarationClassLookup,
                        localDeclarationNames,
                        inheritedOverrideSuperType,
                        localJvmDeclarations);
        Map<String, ConstructorSuperCall> generatedHelperSuperCalls =
                collectGeneratedHelperSuperCalls(classDeclaration, javaImportLookup, classContext);
        if (classDeclaration.body() != null && classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof MethodDefinition methodDefinition) {
                    if (isConstructorMethod(methodDefinition)) {
                        QinIrMethodDeclaration loweredConstructor = null;
                        try {
                            loweredConstructor = lowerConstructorDeclarationOrNull(
                                    methodDefinition,
                                    javaImportLookup,
                                    localDeclarationNames,
                                    classContext,
                                    constructorParameterFieldNames);
                        } catch (IllegalArgumentException error) {
                            if (isUnsupportedDecoratorError(error)) {
                                throw error;
                            }
                            if (!isDeclarationSubsetError(error)) {
                                throw error;
                            }
                            traceDeclarationClassReject(
                                    classDeclaration,
                                    "skip constructor " + error.getMessage());
                        }
                        if (loweredConstructor != null) {
                            methods.add(loweredConstructor);
                        }
                        continue;
                        }
                        QinIrMethodDeclaration loweredMethod = null;
                        try {
                            loweredMethod = lowerMethodDeclarationOrNull(
                                    methodDefinition,
                                    javaImportLookup,
                                    localDeclarationNames,
                                    classContext);
                        } catch (IllegalArgumentException error) {
                            if (isUnsupportedDecoratorError(error)) {
                                throw error;
                            }
                            if (!isDeclarationSubsetError(error)) {
                                throw error;
                            }
                            traceDeclarationClassReject(
                                    classDeclaration,
                                    "skip method " + methodNameDiagnostic(methodDefinition) + " " + error.getMessage());
                        }
                        if (loweredMethod != null) {
                            loweredMethod = widenNullableGeneratedConstructorParameters(
                                    loweredMethod,
                                    nullableGeneratedConstructorParameters);
                            loweredMethod = applyGeneratedHelperSuperCall(
                                    loweredMethod,
                                    generatedHelperSuperCalls.get(loweredMethod.name()));
                            methods.add(loweredMethod);
                        }
                    }
                }
            }

        return new QinIrClassDeclaration(
                null,
                classDeclaration.id().name(),
                superType,
                lowerAnnotations(classDeclaration.decorators(), javaImportLookup),
                fields,
                methods);
    }

    private Map<String, Set<Integer>> collectNullableGeneratedConstructorDelegateParameters(
            ClassDeclaration classDeclaration) {
        Map<String, Set<Integer>> result = new LinkedHashMap<>();
        if (classDeclaration == null || classDeclaration.body() == null || classDeclaration.body().body() == null) {
            return result;
        }
        for (AstNode member : classDeclaration.body().body()) {
            if (!(member instanceof MethodDefinition methodDefinition)
                    || !(methodDefinition.key() instanceof Identifier identifier)
                    || !identifier.name().startsWith("__qin_constructor_")) {
                continue;
            }
            FunctionExpression function = methodDefinition.value();
            if (function == null || function.body() == null || function.body().body() == null) {
                continue;
            }
            collectNullableGeneratedConstructorDelegateParameters(function.body().body(), result);
        }
        return result;
    }

    private void collectNullableGeneratedConstructorDelegateParameters(
            List<? extends Statement> statements,
            Map<String, Set<Integer>> result) {
        if (statements == null || statements.isEmpty()) {
            return;
        }
        for (Statement statement : statements) {
            if (statement instanceof ExpressionStatement expressionStatement) {
                collectNullableGeneratedConstructorDelegateParameters(expressionStatement.expression(), result);
                continue;
            }
            if (statement instanceof BlockStatement blockStatement) {
                collectNullableGeneratedConstructorDelegateParameters(blockStatement.body(), result);
                continue;
            }
            if (statement instanceof IfStatement ifStatement) {
                collectNullableGeneratedConstructorDelegateParameters(statementList(ifStatement.consequent()), result);
                collectNullableGeneratedConstructorDelegateParameters(statementList(ifStatement.alternate()), result);
            }
        }
    }

    private void collectNullableGeneratedConstructorDelegateParameters(
            Expression expression,
            Map<String, Set<Integer>> result) {
        if (!(expression instanceof CallExpression callExpression)
                || callExpression.arguments() == null
                || callExpression.arguments().isEmpty()) {
            return;
        }
        Object callee = unwrapParenthesized(callExpression.callee());
        if (!(callee instanceof ArrowFunctionExpression arrowFunction)) {
            return;
        }
        String restName = singleRestParameterName(arrowFunction);
        if (restName == null) {
            return;
        }
        List<CallExpression> delegateCalls = generatedConstructorDelegateReturnCallsFromBody(arrowFunction.body(), restName);
        if (delegateCalls.isEmpty()) {
            return;
        }
        List<Expression> arguments = List.copyOf(callExpression.arguments());
        for (CallExpression delegateCall : delegateCalls) {
            String methodName = generatedConstructorDelegateMethodName(delegateCall);
            if (methodName == null) {
                continue;
            }
            for (int i = 0; i < arguments.size(); i++) {
                if (isNullLiteralAst(arguments.get(i))) {
                    result.computeIfAbsent(methodName, ignored -> new LinkedHashSet<>()).add(i);
                }
            }
        }
    }

    private QinIrMethodDeclaration widenNullableGeneratedConstructorParameters(
            QinIrMethodDeclaration method,
            Map<String, Set<Integer>> nullableParameters) {
        if (method == null || nullableParameters == null || nullableParameters.isEmpty()) {
            return method;
        }
        Set<Integer> indexes = nullableParameters.get(method.name());
        if (indexes == null || indexes.isEmpty()) {
            return method;
        }
        List<QinIrParameter> parameters = new ArrayList<>(method.parameters());
        boolean changed = false;
        for (Integer index : indexes) {
            if (index == null || index < 0 || index >= parameters.size()) {
                continue;
            }
            QinIrParameter parameter = parameters.get(index);
            if (!isPrimitiveType(parameter.type())) {
                continue;
            }
            parameters.set(index, new QinIrParameter(
                    parameter.name(),
                    QinIrTypeRef.classType("java.lang.Object"),
                    parameter.annotations(),
                    parameter.varargs()));
            changed = true;
        }
        if (!changed) {
            return method;
        }
        return new QinIrMethodDeclaration(
                method.name(),
                method.returnType(),
                parameters,
                method.annotations(),
                method.returnExpression(),
                method.bodyStatements(),
                method.superArguments(),
                method.explicitSuperConstructorCall(),
                method.runtimeFunctionDefinition(),
                method.staticMethod(),
                method.abstractMethod());
    }

    private void widenFieldsAssignedFromNullableGeneratedConstructorParameters(
            List<QinIrFieldDeclaration> fields,
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, Set<Integer>> nullableParameters) {
        if (fields == null || fields.isEmpty()
                || classDeclaration == null
                || classDeclaration.body() == null
                || classDeclaration.body().body() == null
                || nullableParameters == null
                || nullableParameters.isEmpty()) {
            return;
        }
        Set<String> nullableParameterNames = new LinkedHashSet<>();
        for (AstNode member : classDeclaration.body().body()) {
            if (!(member instanceof MethodDefinition methodDefinition)
                    || !(methodDefinition.key() instanceof Identifier identifier)) {
                continue;
            }
            Set<Integer> indexes = nullableParameters.get(identifier.name());
            if (indexes == null || indexes.isEmpty()) {
                continue;
            }
            List<QinIrParameter> parameters = lowerParameters(
                    methodDefinition.value(),
                    javaImportLookup,
                    localDeclarationNames);
            for (Integer index : indexes) {
                if (index != null && index >= 0 && index < parameters.size()) {
                    nullableParameterNames.add(parameters.get(index).name());
                }
            }
        }
        if (nullableParameterNames.isEmpty()) {
            return;
        }
        for (int i = 0; i < fields.size(); i++) {
            QinIrFieldDeclaration field = fields.get(i);
            if (!matchesNullableGeneratedConstructorParameterField(field.name(), nullableParameterNames)
                    || !isPrimitiveType(field.type())) {
                continue;
            }
            fields.set(i, new QinIrFieldDeclaration(
                    field.name(),
                    QinIrTypeRef.classType("java.lang.Object"),
                    field.annotations(),
                    field.initializer(),
                    field.staticField()));
        }
    }

    private boolean matchesNullableGeneratedConstructorParameterField(
            String fieldName,
            Set<String> nullableParameterNames) {
        if (fieldName == null || nullableParameterNames == null || nullableParameterNames.isEmpty()) {
            return false;
        }
        if (nullableParameterNames.contains(fieldName)) {
            return true;
        }
        if (fieldName.startsWith("__qin_field_")) {
            String sourceName = fieldName.substring("__qin_field_".length());
            return nullableParameterNames.contains(sourceName);
        }
        for (String parameterName : nullableParameterNames) {
            if (fieldName.equals("__qin_field_" + parameterName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPrimitiveType(QinIrTypeRef type) {
        return type != null
                && (type.kind() == com.qin.lang.ir.QinIrTypeKind.BOOLEAN
                || type.kind() == com.qin.lang.ir.QinIrTypeKind.INT
                || type.kind() == com.qin.lang.ir.QinIrTypeKind.DOUBLE);
    }

    private QinIrMethodDeclaration lowerMethodSignature(
            MethodDefinition methodDefinition,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        if (!(methodDefinition.key() instanceof Identifier identifier)) {
            return null;
        }

        FunctionExpression function = methodDefinition.value();
        Map<String, QinIrTypeRef> typeParameterBounds =
                lowerTypeParameterBounds(function, javaImportLookup, localDeclarationNames);
        List<QinIrParameter> parameters =
                lowerParameters(function, javaImportLookup, localDeclarationNames, typeParameterBounds);
        QinIrTypeRef returnType = function != null && function.returnType() != null
                ? lowerParameterType(function.returnType(), javaImportLookup, localDeclarationNames, typeParameterBounds)
                : QinIrTypeRef.classType("java.lang.Object");
        if (function != null && isObjectPlaceholderType(returnType)) {
            QinIrTypeRef inferredReturnType = inferFunctionReturnTypeFromAstOrNull(
                    function,
                    parameters,
                    javaImportLookup,
                    localDeclarationNames);
            if (inferredReturnType != null && !isObjectPlaceholderType(inferredReturnType)) {
                returnType = inferredReturnType;
            }
        }
        return new QinIrMethodDeclaration(
                identifier.name(),
                returnType,
                parameters,
                lowerAnnotations(methodDefinition.decorators(), javaImportLookup),
                null,
                List.of(),
                List.of(),
                null,
                methodDefinition.isStatic());
    }

    private QinIrTypeRef inferFunctionReturnTypeFromAstOrNull(
            FunctionExpression function,
            List<QinIrParameter> parameters,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        if (function == null || function.body() == null) {
            return null;
        }
        List<QinIrTypeRef> returnTypes = new ArrayList<>();
        collectReturnTypesFromAst(
                function.body(),
                parameters,
                javaImportLookup,
                localDeclarationNames,
                returnTypes);
        return singleSpecificReturnTypeOrNull(returnTypes);
    }

    private void collectReturnTypesFromAst(
            Object node,
            List<QinIrParameter> parameters,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            List<QinIrTypeRef> returnTypes) {
        if (node == null) {
            return;
        }
        if (node instanceof ReturnStatement returnStatement) {
            QinIrTypeRef returnType = inferReturnExpressionTypeFromAstOrObject(
                    returnStatement.argument(),
                    parameters,
                    javaImportLookup,
                    localDeclarationNames);
            if (returnType != null) {
                returnTypes.add(returnType);
            }
            return;
        }
        if ("ReturnStatement".equals(QinSlimeFrontendAdapter.simpleName(node))) {
            QinIrTypeRef returnType = inferReturnExpressionTypeFromAstOrObject(
                    QinSlimeFrontendAdapter.invokeByName(node, "argument"),
                    parameters,
                    javaImportLookup,
                    localDeclarationNames);
            if (returnType != null) {
                returnTypes.add(returnType);
            }
            return;
        }
        if (node instanceof BlockStatement blockStatement) {
            for (Statement statement : blockStatement.body()) {
                collectReturnTypesFromAst(statement, parameters, javaImportLookup, localDeclarationNames, returnTypes);
            }
            return;
        }
        if ("BlockStatement".equals(QinSlimeFrontendAdapter.simpleName(node))) {
            for (Object statement : QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(node, "body"),
                    "BlockStatement.body")) {
                collectReturnTypesFromAst(statement, parameters, javaImportLookup, localDeclarationNames, returnTypes);
            }
            return;
        }
        if (node instanceof IfStatement ifStatement) {
            collectReturnTypesFromAst(
                    ifStatement.consequent(),
                    parameters,
                    javaImportLookup,
                    localDeclarationNames,
                    returnTypes);
            collectReturnTypesFromAst(
                    ifStatement.alternate(),
                    parameters,
                    javaImportLookup,
                    localDeclarationNames,
                    returnTypes);
            return;
        }
        if ("IfStatement".equals(QinSlimeFrontendAdapter.simpleName(node))) {
            for (Object statement : QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(node, "consequent"),
                    "IfStatement.consequent")) {
                collectReturnTypesFromAst(statement, parameters, javaImportLookup, localDeclarationNames, returnTypes);
            }
            for (Object statement : QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(node, "alternate"),
                    "IfStatement.alternate")) {
                collectReturnTypesFromAst(statement, parameters, javaImportLookup, localDeclarationNames, returnTypes);
            }
        }
    }

    private QinIrTypeRef inferReturnExpressionTypeFromAstOrObject(
            Object expressionAst,
            List<QinIrParameter> parameters,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        if (expressionAst == null || isNullLiteralAst(expressionAst)) {
            return null;
        }
        QinIrTypeRef literalType = inferLiteralTypeOrNull(expressionAst);
        if (literalType != null) {
            return literalType;
        }
        if (expressionAst instanceof Identifier identifier) {
            QinIrParameter parameter = resolveDeclarationParameter(parameters, identifier.name());
            return parameter == null ? QinIrTypeRef.classType("java.lang.Object") : parameter.type();
        }
        if (expressionAst instanceof ConditionalExpression conditionalExpression) {
            return singleSpecificReturnTypeOrNull(
                    inferReturnExpressionTypeFromAstOrObject(
                            conditionalExpression.consequent(),
                            parameters,
                            javaImportLookup,
                            localDeclarationNames),
                    inferReturnExpressionTypeFromAstOrObject(
                            conditionalExpression.alternate(),
                            parameters,
                            javaImportLookup,
                            localDeclarationNames));
        }
        if ("Identifier".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            String identifierName = QinSlimeFrontendAdapter.extractIdentifierNameStatic(expressionAst, "Identifier");
            QinIrParameter parameter = resolveDeclarationParameter(parameters, identifierName);
            return parameter == null ? QinIrTypeRef.classType("java.lang.Object") : parameter.type();
        }
        if (expressionAst instanceof NewExpression newExpression) {
            return inferNewExpressionTypeFromCalleeOrNull(newExpression.callee(), javaImportLookup, localDeclarationNames);
        }
        String nodeType = QinSlimeFrontendAdapter.simpleName(expressionAst);
        if ("ConditionalExpression".equals(nodeType)) {
            return singleSpecificReturnTypeOrNull(
                    inferReturnExpressionTypeFromAstOrObject(
                            QinSlimeFrontendAdapter.invokeByName(expressionAst, "consequent"),
                            parameters,
                            javaImportLookup,
                            localDeclarationNames),
                    inferReturnExpressionTypeFromAstOrObject(
                            QinSlimeFrontendAdapter.invokeByName(expressionAst, "alternate"),
                            parameters,
                            javaImportLookup,
                            localDeclarationNames));
        }
        if ("NewExpression".equals(nodeType)) {
            return inferNewExpressionTypeFromCalleeOrNull(
                    QinSlimeFrontendAdapter.invokeByName(expressionAst, "callee"),
                    javaImportLookup,
                    localDeclarationNames);
        }
        QinIrTypeRef javaOptionalStaticReturnType = inferJavaOptionalStaticReturnTypeFromAstOrNull(expressionAst);
        if (javaOptionalStaticReturnType != null) {
            return javaOptionalStaticReturnType;
        }
        QinIrTypeRef javaLangStringStaticReturnType = inferJavaLangStringStaticReturnTypeFromAstOrNull(expressionAst);
        if (javaLangStringStaticReturnType != null) {
            return javaLangStringStaticReturnType;
        }
        if ("ParenthesizedExpression".equals(nodeType)
                || "TSAsExpression".equals(nodeType)
                || "TSSatisfiesExpression".equals(nodeType)
                || "TSNonNullExpression".equals(nodeType)) {
            return inferReturnExpressionTypeFromAstOrObject(
                    QinSlimeFrontendAdapter.invokeByName(expressionAst, "expression"),
                    parameters,
                    javaImportLookup,
                    localDeclarationNames);
        }
        if (isMemberCallNamed(expressionAst, "toString")) {
            return QinIrTypeRef.stringType();
        }
        String calleeIdentifier = callExpressionIdentifierNameOrNull(expressionAst);
        if ("String".equals(calleeIdentifier)) {
            return QinIrTypeRef.stringType();
        }
        if ("Number".equals(calleeIdentifier)) {
            return QinIrTypeRef.doubleType();
        }
        if ("__qin_collection_to_array__".equals(calleeIdentifier)
                || "__qin_array_append__".equals(calleeIdentifier)
                || "__qin_array_prepend__".equals(calleeIdentifier)
                || "__qin_array_remove_at__".equals(calleeIdentifier)
                || "__qin_array_slice__".equals(calleeIdentifier)) {
            return objectArrayType();
        }
        if ("__qin_collection_size__".equals(calleeIdentifier)) {
            return QinIrTypeRef.doubleType();
        }
        if ("__qin_collection_is_empty__".equals(calleeIdentifier)
                || "__qin_collection_add__".equals(calleeIdentifier)
                || "__qin_collection_contains__".equals(calleeIdentifier)) {
            return QinIrTypeRef.booleanType();
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef inferJavaOptionalStaticReturnTypeFromAstOrNull(Object expressionAst) {
        if (!"CallExpression".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            return null;
        }
        Object callee = QinSlimeFrontendAdapter.invokeByName(expressionAst, "callee");
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(callee))
                || Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(callee, "computed"))) {
            return null;
        }
        String receiverName = declarationIdentifierName(QinSlimeFrontendAdapter.invokeByName(callee, "object"));
        if (!"__QinJavaUtilOptional".equals(receiverName) && !"Optional".equals(receiverName)) {
            return null;
        }
        String methodName = adapter.extractMemberPropertyName(QinSlimeFrontendAdapter.invokeByName(callee, "property"));
        if (!"empty".equals(methodName) && !"of".equals(methodName) && !"ofNullable".equals(methodName)) {
            return null;
        }
        return QinIrTypeRef.classType("java.util.Optional");
    }

    private QinIrTypeRef inferJavaLangStringStaticReturnTypeFromAstOrNull(Object expressionAst) {
        if (!"CallExpression".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            return null;
        }
        Object callee = QinSlimeFrontendAdapter.invokeByName(expressionAst, "callee");
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(callee))
                || Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(callee, "computed"))) {
            return null;
        }
        String receiverName = declarationIdentifierName(QinSlimeFrontendAdapter.invokeByName(callee, "object"));
        if (!"__QinJavaLangString".equals(receiverName)) {
            return null;
        }
        String methodName = adapter.extractMemberPropertyName(QinSlimeFrontendAdapter.invokeByName(callee, "property"));
        if (!"join".equals(methodName) && !"valueOf".equals(methodName)) {
            return null;
        }
        return QinIrTypeRef.stringType();
    }

    private QinIrTypeRef inferNewExpressionTypeFromCalleeOrNull(
            Object callee,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        String className = declarationIdentifierName(callee);
        if (className == null || className.isBlank()) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        String nativeJavaSdkOwner = nativeJavaSdkCollectionOwnerBinaryName(className);
        if (nativeJavaSdkOwner != null) {
            return QinIrTypeRef.classType(nativeJavaSdkOwner);
        }
        if (localDeclarationNames.contains(className)) {
            return QinIrTypeRef.classType(className);
        }
        String javaName = javaImportLookup.get(className);
        return javaName == null || javaName.isBlank()
                ? QinIrTypeRef.classType("java.lang.Object")
                : QinIrTypeRef.classType(javaName);
    }

    private boolean isMemberCallNamed(Object expressionAst, String methodName) {
        if (!"CallExpression".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            return false;
        }
        Object callee = QinSlimeFrontendAdapter.invokeByName(expressionAst, "callee");
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(callee))
                || Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(callee, "computed"))) {
            return false;
        }
        String propertyName = adapter.extractMemberPropertyName(
                QinSlimeFrontendAdapter.invokeByName(callee, "property"));
        return methodName.equals(propertyName);
    }

    private String callExpressionIdentifierNameOrNull(Object expressionAst) {
        if (expressionAst instanceof CallExpression callExpression) {
            return declarationIdentifierName(callExpression.callee());
        }
        if (!"CallExpression".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            return null;
        }
        return declarationIdentifierName(QinSlimeFrontendAdapter.invokeByName(expressionAst, "callee"));
    }

    private QinIrClassDeclaration lowerJavaSuperclassClassWithRuntimeMethods(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (classDeclaration == null || classDeclaration.id() == null || classDeclaration.body() == null) {
            throw qjsError("QJS2030", "java: superclass class declaration is missing id/body");
        }
        QinIrTypeRef superType = lowerSuperType(
                classDeclaration,
                javaImportLookup,
                jsDeclarationClassLookup,
                localDeclarationNames);
        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        if (classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof MethodDefinition methodDefinition) {
                    if (methodDefinition.isStatic() || methodDefinition.computed()) {
                        throw qjsError("QJS2030", "Static/computed java: subclass methods are not supported yet");
                    }
                    if (isConstructorMethod(methodDefinition)) {
                        continue;
                    }
                    methods.add(lowerRuntimeBackedJavaSubclassMethod(
                            classDeclaration.id().name(),
                            superType,
                            methodDefinition,
                            javaImportLookup,
                            localJvmDeclarations));
                    continue;
                }
                if (member instanceof PropertyDefinition propertyDefinition) {
                    if (isDeclareOnlyField(propertyDefinition)) {
                        continue;
                    }
                    QinIrFieldDeclaration loweredField = lowerFieldDeclarationOrNull(
                            propertyDefinition,
                            javaImportLookup,
                            jsDeclarationClassLookup,
                            localDeclarationNames,
                            classDeclaration.id().name(),
                            declarationLookup);
                    if (loweredField == null) {
                        throw qjsError("QJS2030", "Unsupported java: subclass field: " + propertyDefinition);
                    }
                    continue;
                }
                throw qjsError("QJS2030", "Unsupported java: subclass member: "
                        + QinSlimeFrontendAdapter.simpleName(member));
            }
        }
        return new QinIrClassDeclaration(
                null,
                classDeclaration.id().name(),
                superType,
                lowerAnnotations(classDeclaration.decorators(), javaImportLookup),
                List.of(),
                methods);
    }

    private QinIrMethodDeclaration lowerRuntimeBackedJavaSubclassMethod(
            String className,
            QinIrTypeRef superType,
            MethodDefinition methodDefinition,
            Map<String, String> javaImportLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (!(methodDefinition.key() instanceof Identifier identifier)) {
            throw qjsError("QJS2030", "Only identifier java: subclass method names are supported");
        }
        FunctionExpression function = methodDefinition.value();
        List<String> parameterNames = runtimeMethodParameterNames(function);
        ResolvedJvmMethodSignature inherited = findInheritedJvmMethod(
                superType.binaryName(),
                identifier.name(),
                parameterNames.size(),
                localJvmDeclarations);
        List<QinIrParameter> parameters = new ArrayList<>();
        for (int i = 0; i < parameterNames.size(); i++) {
            QinIrTypeRef parameterType = inherited == null
                    ? QinIrTypeRef.classType("java.lang.Object")
                    : inherited.parameterTypes().get(i);
            parameters.add(new QinIrParameter(parameterNames.get(i), parameterType, List.of()));
        }
        QinIrTypeRef returnType = inherited == null
                ? QinIrTypeRef.classType("java.lang.Object")
                : inherited.returnType();
        QinIrObjectLiteral runtimeDefinition = adapter.lowerRequiredFunctionRuntimeDefinition(
                function,
                className + "." + identifier.name(),
                javaImportLookup,
                Map.of());
        return new QinIrMethodDeclaration(
                identifier.name(),
                returnType,
                List.copyOf(parameters),
                withSubhutiRuleAnnotation(lowerAnnotations(methodDefinition.decorators(), javaImportLookup)),
                null,
                runtimeDefinition);
    }

    private List<String> runtimeMethodParameterNames(FunctionExpression function) {
        if (function == null || function.params() == null || function.params().isEmpty()) {
            return List.of();
        }
        List<String> names = new ArrayList<>();
        for (com.slime.ast.Pattern pattern : function.params()) {
            if (pattern instanceof Identifier identifier) {
                names.add(identifier.name());
                continue;
            }
            if (pattern instanceof AssignmentPattern assignmentPattern
                    && assignmentPattern.left() instanceof Identifier identifier) {
                names.add(identifier.name());
                continue;
            }
            if (pattern instanceof RestElement) {
                continue;
            }
            throw qjsError("QJS2030", "Unsupported java: subclass runtime method parameter: "
                    + QinSlimeFrontendAdapter.simpleName(pattern));
        }
        return List.copyOf(names);
    }

    private ResolvedJvmMethodSignature findInheritedJvmMethod(
            String ownerBinaryName,
            String methodName,
            int parameterCount,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        return findInheritedJvmMethod(
                ownerBinaryName,
                methodName,
                parameterCount,
                localJvmDeclarations,
                new java.util.LinkedHashSet<>());
    }

    private ResolvedJvmMethodSignature findInheritedJvmMethod(
            String ownerBinaryName,
            String methodName,
            int parameterCount,
            Map<String, QinIrClassDeclaration> localJvmDeclarations,
            Set<String> visitedLocalTypes) {
        if (ownerBinaryName == null || ownerBinaryName.isBlank()) {
            return null;
        }
        QinIrClassDeclaration localDeclaration = localJvmDeclarations == null
                ? null
                : localJvmDeclarations.get(ownerBinaryName);
        if (localDeclaration != null) {
            if (!visitedLocalTypes.add(ownerBinaryName)) {
                return null;
            }
            QinIrMethodDeclaration matched = null;
            for (QinIrMethodDeclaration candidate : localDeclaration.methods()) {
                if (!candidate.name().equals(methodName) || candidate.parameters().size() != parameterCount) {
                    continue;
                }
                if (matched != null) {
                    throw qjsError("QJS2030", "Ambiguous local java: superclass method: "
                            + ownerBinaryName + "." + methodName + "/" + parameterCount);
                }
                matched = candidate;
            }
            if (matched != null) {
                List<QinIrTypeRef> parameterTypes = new ArrayList<>();
                for (QinIrParameter parameter : matched.parameters()) {
                    parameterTypes.add(parameter.type());
                }
                return new ResolvedJvmMethodSignature(List.copyOf(parameterTypes), matched.returnType());
            }
            return localDeclaration.superType() == null
                    ? null
                    : findInheritedJvmMethod(
                            localDeclaration.superType().binaryName(),
                            methodName,
                            parameterCount,
                            localJvmDeclarations,
                            visitedLocalTypes);
        }
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Method matched = null;
            for (Method method : ownerClass.getMethods()) {
                if (!method.getName().equals(methodName) || method.getParameterCount() != parameterCount) {
                    continue;
                }
                if (matched != null) {
                    throw qjsError("QJS2030", "Ambiguous java: superclass method: "
                            + ownerBinaryName + "." + methodName + "/" + parameterCount);
                }
                matched = method;
            }
            if (matched == null) {
                return null;
            }
            List<QinIrTypeRef> parameterTypes = new ArrayList<>();
            for (Class<?> parameterType : matched.getParameterTypes()) {
                parameterTypes.add(toQinTypeRef(parameterType));
            }
            return new ResolvedJvmMethodSignature(
                    List.copyOf(parameterTypes),
                    toQinTypeRef(matched.getReturnType()));
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    private record ResolvedJvmMethodSignature(
            List<QinIrTypeRef> parameterTypes,
            QinIrTypeRef returnType) {
    }

    private QinIrTypeRef toQinTypeRef(Class<?> type) {
        if (type == void.class || type == Void.class) {
            return QinIrTypeRef.voidType();
        }
        if (type == boolean.class || type == Boolean.class) {
            return QinIrTypeRef.booleanType();
        }
        if (type == int.class || type == Integer.class) {
            return QinIrTypeRef.intType();
        }
        if (type == double.class || type == Double.class) {
            return QinIrTypeRef.doubleType();
        }
        if (type == String.class) {
            return QinIrTypeRef.stringType();
        }
        return QinIrTypeRef.classType(type.getName());
    }

    private List<QinIrAnnotation> withSubhutiRuleAnnotation(List<QinIrAnnotation> annotations) {
        List<QinIrAnnotation> result = new ArrayList<>(annotations == null ? List.of() : annotations);
        boolean hasRule = result.stream()
                .anyMatch(annotation -> SUBHUTI_RULE_ANNOTATION.equals(annotation.ownerBinaryName()));
        if (!hasRule) {
            result.add(new QinIrAnnotation(SUBHUTI_RULE_ANNOTATION, List.of()));
        }
        return List.copyOf(result);
    }

    private QinIrTypeRef lowerSuperType(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames) {
        if (classDeclaration.superClass() == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (classDeclaration.superClass() instanceof Identifier identifier) {
            String importedBinaryName = javaImportLookup.get(identifier.name());
            if (importedBinaryName != null) {
                return QinIrTypeRef.classType(importedBinaryName);
            }
            String jsDeclarationBinaryName = jsDeclarationClassLookup == null
                    ? null
                    : jsDeclarationClassLookup.get(identifier.name());
            if (jsDeclarationBinaryName != null && !jsDeclarationBinaryName.isBlank()) {
                return QinIrTypeRef.classType(jsDeclarationBinaryName);
            }
            if (localDeclarationNames != null && localDeclarationNames.contains(identifier.name())) {
                return QinIrTypeRef.classType(identifier.name());
            }
        }
        throw qjsError("QJS2011", "Unsupported class extends target: " + classDeclaration.superClass());
    }

    List<QinIrConstDeclaration> lowerVariableDeclaration(
            VariableDeclaration variableDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (variableDeclaration == null) {
            throw qjsError("QJS2002", "Variable declaration cannot be null");
        }
        String kind = variableDeclaration.kind();
        if (!"const".equals(kind) && !"let".equals(kind) && !"var".equals(kind)) {
            throw qjsError("QJS2002", "Only const/let/var declaration is supported, but got: " + kind);
        }
        if (variableDeclaration.declarations() == null || variableDeclaration.declarations().isEmpty()) {
            throw qjsError(
                    "QJS2002",
                    "Variable declaration must contain at least one declarator"
                            + " kind=" + kind
                            + " location=" + sourceLocationDiagnostic(variableDeclaration));
        }

        List<QinIrConstDeclaration> lowered = new ArrayList<>();
        int destructureIndex = 0;
        for (var declarator : variableDeclaration.declarations()) {
            QinIrExpression initializer = declarator.init() == null
                    ? new QinIrNullLiteral()
                    : lowerDeclarationInitializer(declarator.init(), javaImportLookup, declarationLookup);
            if (declarator.id() instanceof Identifier identifier) {
                lowered.add(new QinIrConstDeclaration(identifier.name(), initializer));
                continue;
            }
            String tempName = "__qin_destructure_" + destructureIndex++;
            lowered.add(new QinIrConstDeclaration(tempName, initializer));
            lowerBindingPatternDeclarations(declarator.id(), new QinIrIdentifierReference(tempName), lowered);
        }
        return lowered;
    }

    private void lowerBindingPatternDeclarations(
            AstNode pattern,
            QinIrExpression source,
            List<QinIrConstDeclaration> lowered) {
        if (pattern instanceof Identifier identifier) {
            lowered.add(new QinIrConstDeclaration(identifier.name(), source));
            return;
        }
        if (pattern instanceof ObjectPattern objectPattern) {
            for (AstNode propertyNode : objectPattern.properties()) {
                if (propertyNode == null) {
                    continue;
                }
                if (!(propertyNode instanceof Property property)) {
                    throw qjsError("QJS2022", "Unsupported object destructuring element: " + propertyNode.getClass().getName());
                }
                if (property.computed()) {
                    throw qjsError("QJS2022", "Computed object destructuring keys are not supported yet");
                }
                String key = adapter.extractPropertyKey(property.key());
                lowerBindingPatternDeclarations(
                        property.value(),
                        memberGet(source, new QinIrStringLiteral(key)),
                        lowered);
            }
            return;
        }
        if (pattern instanceof ArrayPattern arrayPattern) {
            int index = 0;
            for (AstNode element : arrayPattern.elements()) {
                if (element != null) {
                    lowerBindingPatternDeclarations(
                            element,
                            memberGet(source, new QinIrNumberLiteral(index)),
                            lowered);
                }
                index++;
            }
            return;
        }
        if (pattern instanceof AssignmentPattern) {
            throw qjsError("QJS2022", "Default destructuring values are not supported yet");
        }
        if (pattern instanceof RestElement) {
            throw qjsError("QJS2022", "Rest destructuring bindings are not supported yet");
        }
        throw qjsError("QJS2022", "Unsupported destructuring binding pattern: "
                + QinSlimeFrontendAdapter.simpleName(pattern));
    }

    private QinIrExpression memberGet(QinIrExpression source, QinIrExpression property) {
        return new QinIrBuiltinCallExpression("Global", "__qin_member_get__", List.of(source, property));
    }

    QinIrConstDeclaration lowerFunctionDeclaration(
            FunctionDeclaration functionDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Identifier id = functionDeclaration.id();
        if (id == null) {
            throw qjsError("QJS2010", "Anonymous FunctionDeclaration is not supported in Qin subset");
        }
        return new QinIrConstDeclaration(
                id.name(),
                adapter.lowerFunctionDeclarationOrNull(functionDeclaration, javaImportLookup, declarationLookup));
    }

    QinIrConstDeclaration lowerClassDeclarationValue(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            boolean useJvmClassValue,
            String jvmClassBinaryName) {
        if (classDeclaration == null || classDeclaration.id() == null) {
            throw qjsError("QJS2010", "Anonymous ClassDeclaration is not supported in Qin subset");
        }
        if (useJvmClassValue) {
            String canonicalBinaryName = jvmClassBinaryName == null || jvmClassBinaryName.isBlank()
                    ? classDeclaration.id().name()
                    : jvmClassBinaryName.startsWith("__Qin")
                            ? jvmClassBinaryName
                            : QinJavaSdkAliasSupport.canonicalBinaryName(jvmClassBinaryName);
            return new QinIrConstDeclaration(
                    classDeclaration.id().name(),
                    new QinIrJavaClassLiteralExpression(classDeclaration.id().name(), canonicalBinaryName));
        }
        if (hasRuntimeDecorators(classDeclaration)) {
            throw qjsError(
                    "QJS2013",
                    "Decorated classes must lower through the static declaration subset; dynamic decorator runtime fallback is not supported");
        }
        return new QinIrConstDeclaration(
                classDeclaration.id().name(),
                adapter.lowerClassDeclarationRuntimeValue(classDeclaration, javaImportLookup, declarationLookup));
    }

    private boolean hasRuntimeDecorators(ClassDeclaration classDeclaration) {
        if (classDeclaration == null) {
            return false;
        }
        if (hasRuntimeDecorators(classDeclaration.decorators())) {
            return true;
        }
        if (classDeclaration.body() == null || classDeclaration.body().body() == null) {
            return false;
        }
        for (AstNode member : classDeclaration.body().body()) {
            if (member instanceof MethodDefinition methodDefinition
                    && hasRuntimeDecorators(methodDefinition.decorators())) {
                return true;
            }
            if (member instanceof PropertyDefinition propertyDefinition
                    && hasRuntimeDecorators(propertyDefinition.decorators())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRuntimeDecorators(List<Decorator> decorators) {
        if (decorators == null || decorators.isEmpty()) {
            return false;
        }
        for (Decorator decorator : decorators) {
            if (!isQinOwnedStaticDecorator(decorator)) {
                return true;
            }
        }
        return false;
    }

    boolean isDeclarationCompatibleClass(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (classDeclaration == null || classDeclaration.id() == null || classDeclaration.body() == null) {
            traceDeclarationClassReject(classDeclaration, "missing id/body");
            return false;
        }
        if (classDeclaration.superClass() != null
                && !isResolvableDeclarationSuperClass(
                classDeclaration.superClass(),
                javaImportLookup,
                jsDeclarationClassLookup,
                localDeclarationNames,
                localJvmDeclarations)) {
            traceDeclarationClassReject(classDeclaration, "unresolved superclass "
                    + QinSlimeFrontendAdapter.simpleName(classDeclaration.superClass()));
            return false;
        }
        if (classDeclaration.body().body() == null || classDeclaration.body().body().isEmpty()) {
            traceDeclarationClassReject(classDeclaration, "empty body");
            return false;
        }
        boolean hasDeclarationSurface = false;
        for (AstNode member : classDeclaration.body().body()) {
            if (member instanceof PropertyDefinition propertyDefinition) {
                if (isDeclareOnlyField(propertyDefinition)) {
                    continue;
                }
                if (propertyDefinition.computed()) {
                    traceDeclarationClassReject(classDeclaration, "skip computed property");
                    continue;
                }
                hasDeclarationSurface = true;
                continue;
            }
            if (member instanceof MethodDefinition methodDefinition) {
                if (methodDefinition.computed()) {
                    traceDeclarationClassReject(classDeclaration, "skip computed method");
                    continue;
                }
                hasDeclarationSurface = true;
                continue;
            }
            traceDeclarationClassReject(classDeclaration, "unsupported member "
                    + QinSlimeFrontendAdapter.simpleName(member));
        }
        if (!hasDeclarationSurface) {
            traceDeclarationClassReject(classDeclaration, "no declaration-compatible members");
        }
        return hasDeclarationSurface;
    }

    private String methodNameDiagnostic(MethodDefinition methodDefinition) {
        if (methodDefinition == null) {
            return "<unknown>";
        }
        if (methodDefinition.key() instanceof Identifier identifier) {
            return identifier.name();
        }
        return QinSlimeFrontendAdapter.simpleName(methodDefinition.key());
    }

    private void traceDeclarationClassReject(ClassDeclaration classDeclaration, String reason) {
        if (!Boolean.getBoolean("qin.declarationClass.trace")) {
            return;
        }
        String name = classDeclaration != null && classDeclaration.id() != null
                ? classDeclaration.id().name()
                : "<anonymous>";
        System.err.println("[QinDeclarationIrLowerer] reject class " + name + ": " + reason);
    }

    private boolean isConstructorMethod(MethodDefinition methodDefinition) {
        if (methodDefinition == null) {
            return false;
        }
        if ("constructor".equals(methodDefinition.kind())) {
            return true;
        }
        return methodDefinition.key() instanceof Identifier identifier
                && "constructor".equals(identifier.name());
    }

    private boolean isDeclarationCompatibleConstructorBody(FunctionExpression function) {
        if (function == null || function.body() == null || function.body().body() == null) {
            return true;
        }
        return isDeclarationCompatibleStatements(constructorBodyWithoutLeadingSuper(function));
    }

    private boolean isResolvableSuperClass(
            Expression superClass,
            Map<String, String> javaImportLookup) {
        return superClass instanceof Identifier identifier
                && javaImportLookup != null
                && javaImportLookup.containsKey(identifier.name());
    }

    private boolean isResolvableJvmSuperClass(
            Expression superClass,
            Map<String, String> javaImportLookup,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (isResolvableSuperClass(superClass, javaImportLookup)) {
            return true;
        }
        return superClass instanceof Identifier identifier
                && localJvmDeclarations != null
                && localJvmDeclarations.containsKey(identifier.name());
    }

    private boolean isResolvableDeclarationSuperClass(
            Expression superClass,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (isResolvableSuperClass(superClass, javaImportLookup)) {
            return true;
        }
        if (!(superClass instanceof Identifier identifier)) {
            return false;
        }
        String name = identifier.name();
        if (jsDeclarationClassLookup != null && jsDeclarationClassLookup.containsKey(name)) {
            return true;
        }
        return localJvmDeclarations != null && localJvmDeclarations.containsKey(name)
                || localDeclarationNames != null && localDeclarationNames.contains(name);
    }

    private boolean isDeclarationCompatibleMethodBody(FunctionExpression function) {
        if (function == null || function.body() == null || function.body().body() == null) {
            return true;
        }
        return isDeclarationCompatibleStatements(function.body().body());
    }

    private boolean isDeclarationCompatibleStatements(List<? extends Statement> statements) {
        if (statements == null) {
            return true;
        }
        for (Statement statement : statements) {
            if (statement instanceof EmptyStatement) {
                continue;
            }
            if (statement instanceof BlockStatement blockStatement) {
                if (!isDeclarationCompatibleStatements(blockStatement.body())) {
                    return false;
                }
                continue;
            }
            if (statement instanceof VariableDeclaration variableDeclaration) {
                if (!hasIdentifierOnlyVariableDeclarators(variableDeclaration)) {
                    return false;
                }
                continue;
            }
            if (statement instanceof ReturnStatement || statement instanceof ExpressionStatement) {
                continue;
            }
            if (statement instanceof ThrowStatement) {
                continue;
            }
            if (statement instanceof IfStatement ifStatement) {
                if (!isDeclarationCompatibleBranch(ifStatement.consequent())
                        || !isDeclarationCompatibleBranch(ifStatement.alternate())) {
                    return false;
                }
                continue;
            }
            if (statement instanceof TryStatement tryStatement) {
                if (!isDeclarationCompatibleTryStatement(tryStatement)) {
                    return false;
                }
                continue;
            }
            if (statement instanceof SwitchStatement switchStatement) {
                if (!isDeclarationCompatibleSwitchStatement(switchStatement)) {
                    return false;
                }
                continue;
            }
            if (statement instanceof ForStatement forStatement) {
                if (!isDeclarationCompatibleForStatement(forStatement)) {
                    return false;
                }
                continue;
            }
            if ("ForOfStatement".equals(QinSlimeFrontendAdapter.simpleName(statement))) {
                if (!isDeclarationCompatibleForOfStatement(statement)) {
                    return false;
                }
                continue;
            }
            if (statement instanceof DoWhileStatement doWhileStatement) {
                if (!isDeclarationCompatibleBranch(doWhileStatement.body())) {
                    return false;
                }
                continue;
            }
            if (statement instanceof WhileStatement whileStatement) {
                if (!isDeclarationCompatibleBranch(whileStatement.body())) {
                    return false;
                }
                continue;
            }
            if (statement instanceof BreakStatement || statement instanceof ContinueStatement) {
                continue;
            }
            return false;
        }
        return true;
    }

    private boolean isDeclarationCompatibleBranch(Statement statement) {
        if (statement == null) {
            return true;
        }
        if (statement instanceof BlockStatement blockStatement) {
            return isDeclarationCompatibleStatements(blockStatement.body());
        }
        if (statement instanceof EmptyStatement) {
            return true;
        }
        if (statement instanceof ReturnStatement || statement instanceof ExpressionStatement) {
            return true;
        }
        if (statement instanceof ThrowStatement) {
            return true;
        }
        if (statement instanceof IfStatement ifStatement) {
            return isDeclarationCompatibleBranch(ifStatement.consequent())
                    && isDeclarationCompatibleBranch(ifStatement.alternate());
        }
        if (statement instanceof TryStatement tryStatement) {
            return isDeclarationCompatibleTryStatement(tryStatement);
        }
        if (statement instanceof SwitchStatement switchStatement) {
            return isDeclarationCompatibleSwitchStatement(switchStatement);
        }
        if (statement instanceof ForStatement forStatement) {
            return isDeclarationCompatibleForStatement(forStatement);
        }
        if ("ForOfStatement".equals(QinSlimeFrontendAdapter.simpleName(statement))) {
            return isDeclarationCompatibleForOfStatement(statement);
        }
        if (statement instanceof DoWhileStatement doWhileStatement) {
            return isDeclarationCompatibleBranch(doWhileStatement.body());
        }
        if (statement instanceof WhileStatement whileStatement) {
            return isDeclarationCompatibleBranch(whileStatement.body());
        }
        if (statement instanceof BreakStatement || statement instanceof ContinueStatement) {
            return true;
        }
        return false;
    }

    private boolean isDeclarationCompatibleTryStatement(TryStatement tryStatement) {
        if (tryStatement == null || tryStatement.block() == null) {
            return false;
        }
        if (!isDeclarationCompatibleBranch(tryStatement.block())) {
            return false;
        }
        CatchClause handler = tryStatement.handler();
        if (handler != null && !isDeclarationCompatibleBranch(handler.body())) {
            return false;
        }
        return tryStatement.finalizer() == null || isDeclarationCompatibleBranch(tryStatement.finalizer());
    }

    private boolean isDeclarationCompatibleSwitchStatement(SwitchStatement switchStatement) {
        if (switchStatement == null || switchStatement.discriminant() == null || switchStatement.cases() == null) {
            return false;
        }
        for (SwitchCase switchCase : switchStatement.cases()) {
            if (switchCase == null || !isDeclarationCompatibleStatements(switchCase.consequent())) {
                return false;
            }
        }
        return true;
    }

    private boolean isDeclarationCompatibleForStatement(ForStatement forStatement) {
        if (forStatement == null || !isDeclarationCompatibleBranch(forStatement.body())) {
            return false;
        }
        AstNode init = forStatement.init();
        return init == null
                || init instanceof Expression
                || (init instanceof VariableDeclaration variableDeclaration
                && hasIdentifierOnlyVariableDeclarators(variableDeclaration));
    }

    private boolean isDeclarationCompatibleForOfStatement(Object forOfStatement) {
        if (forOfStatement == null) {
            return false;
        }
        Object left = QinSlimeFrontendAdapter.invokeByName(forOfStatement, "left");
        if (!isSupportedForOfBindingPattern(forOfBindingPatternOrNull(left))) {
            return false;
        }
        Object right = QinSlimeFrontendAdapter.invokeByName(forOfStatement, "right");
        if (!(right instanceof Expression)) {
            return false;
        }
        Object body = QinSlimeFrontendAdapter.invokeByName(forOfStatement, "body");
        return body == null || body instanceof Statement statement && isDeclarationCompatibleBranch(statement);
    }

    private boolean hasIdentifierOnlyVariableDeclarators(VariableDeclaration variableDeclaration) {
        if (variableDeclaration == null
                || variableDeclaration.declarations() == null
                || variableDeclaration.declarations().isEmpty()) {
            return false;
        }
        for (var declarator : variableDeclaration.declarations()) {
            if (declarator == null || !(declarator.id() instanceof Identifier)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasResolvableJavaDecorator(
            List<Decorator> decorators,
            Map<String, String> javaImportLookup) {
        if (decorators == null || decorators.isEmpty() || javaImportLookup == null || javaImportLookup.isEmpty()) {
            return false;
        }
        for (Decorator decorator : decorators) {
            if (decorator == null || decorator.expression() == null) {
                continue;
            }
            Expression expression = decorator.expression();
            if (expression instanceof Identifier identifier
                    && javaImportLookup.containsKey(identifier.name())) {
                return true;
            }
            if (expression instanceof CallExpression callExpression
                    && callExpression.callee() instanceof Identifier identifier
                    && javaImportLookup.containsKey(identifier.name())) {
                return true;
            }
        }
        return false;
    }

    private boolean isUnsupportedDecoratorError(IllegalArgumentException error) {
        return error != null
                && error.getMessage() != null
                && error.getMessage().startsWith("QJS2013 Unsupported decorator");
    }

    private boolean isDeclarationSubsetError(IllegalArgumentException error) {
        if (error == null || error.getMessage() == null) {
            return false;
        }
        return error.getMessage().startsWith("QJS");
    }

    private QinIrFieldDeclaration lowerFieldDeclarationOrNull(
            PropertyDefinition propertyDefinition,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            String className,
            Map<String, QinIrExpression> declarationLookup) {
        if (isDeclareOnlyField(propertyDefinition)) {
            return null;
        }
        if (propertyDefinition == null || !(propertyDefinition.key() instanceof Identifier identifier)) {
            return null;
        }
        boolean previousDeclarationStatic = currentDeclarationStatic;
        currentDeclarationStatic = propertyDefinition.isStatic();
        try {
            QinIrTypeRef declaredType =
                    lowerParameterType(propertyDefinition.typeAnnotation(), javaImportLookup, localDeclarationNames);
            QinIrExpression initializer = lowerFieldInitializer(
                    propertyDefinition.value(),
                    javaImportLookup,
                    jsDeclarationClassLookup,
                    className,
                    identifier.name(),
                    declarationLookup,
                    localDeclarationNames);
            QinIrTypeRef fieldType = refineObjectFieldTypeFromInitializer(declaredType, initializer);
            return new QinIrFieldDeclaration(
                    identifier.name(),
                    fieldType,
                    lowerAnnotations(propertyDefinition.decorators(), javaImportLookup),
                    initializer,
                    propertyDefinition.isStatic());
        } finally {
            currentDeclarationStatic = previousDeclarationStatic;
        }
    }

    private QinIrTypeRef refineObjectFieldTypeFromInitializer(
            QinIrTypeRef declaredType,
            QinIrExpression initializer) {
        if (!isJavaLangObjectType(declaredType) || initializer == null) {
            return declaredType;
        }
        if (initializer instanceof QinIrJavaNewExpression javaNewExpression
                && javaNewExpression.ownerBinaryName() != null
                && !javaNewExpression.ownerBinaryName().isBlank()) {
            return QinIrTypeRef.classType(javaNewExpression.ownerBinaryName());
        }
        if (initializer instanceof QinIrCastExpression castExpression) {
            QinIrTypeRef castType = QinIrTypeRef.classType(castExpression.typeName());
            if (!isJavaLangObjectType(castType)) {
                return castType;
            }
        }
        return declaredType;
    }

    private boolean isDeclareOnlyField(PropertyDefinition propertyDefinition) {
        return propertyDefinition != null
                && "declare".equals(propertyDefinition.accessibility());
    }

    private QinIrExpression lowerFieldInitializer(
            AstNode valueAst,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            String className,
            String fieldName,
            Map<String, QinIrExpression> declarationLookup,
            Set<String> localDeclarationNames) {
        if (valueAst == null) {
            return null;
        }
        return lowerDeclarationExpression(
                valueAst,
                javaImportLookup,
                new DeclarationClassContext(
                        className,
                        List.of(),
                        List.of(),
                        declarationLookup,
                        Map.of(),
                        jsDeclarationClassLookup,
                        localDeclarationNames),
                Map.of());
    }

    private void collectConstructorAssignedFields(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            String className,
            Map<String, QinIrExpression> declarationLookup,
            List<QinIrFieldDeclaration> fields) {
        if (classDeclaration == null || classDeclaration.body() == null || classDeclaration.body().body() == null) {
            return;
        }
        Map<String, QinIrFieldDeclaration> existingFields = new LinkedHashMap<>();
        Map<String, Integer> existingFieldIndexes = new LinkedHashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            QinIrFieldDeclaration field = fields.get(i);
            existingFields.put(field.name(), field);
            existingFieldIndexes.put(field.name(), i);
        }
        for (AstNode member : classDeclaration.body().body()) {
            if (!(member instanceof MethodDefinition methodDefinition) || !isConstructorMethod(methodDefinition)) {
                continue;
            }
            List<QinIrParameter> parameters =
                    lowerParameters(methodDefinition.value(), javaImportLookup, localDeclarationNames);
            for (Statement statement : constructorBodyWithoutLeadingSuper(methodDefinition.value())) {
                String fieldName = constructorAssignedFieldNameOrNull(statement);
                if (fieldName == null) {
                    continue;
                }
                QinIrTypeRef fieldType = inferConstructorAssignedFieldType(
                        statement,
                        parameters,
                        javaImportLookup,
                        jsDeclarationClassLookup,
                        localDeclarationNames,
                        className,
                        declarationLookup,
                        fields);
                QinIrFieldDeclaration existingField = existingFields.get(fieldName);
                if (existingField != null) {
                    if (shouldRefineConstructorAssignedFieldType(existingField, fieldType)) {
                        QinIrFieldDeclaration refinedField = new QinIrFieldDeclaration(
                                existingField.name(),
                                fieldType,
                                existingField.annotations(),
                                existingField.initializer(),
                                existingField.staticField());
                        int existingIndex = existingFieldIndexes.get(fieldName);
                        fields.set(existingIndex, refinedField);
                        existingFields.put(fieldName, refinedField);
                    }
                    continue;
                }
                QinIrFieldDeclaration field = new QinIrFieldDeclaration(
                        fieldName,
                        fieldType,
                        List.of(),
                        null);
                fields.add(field);
                existingFields.put(fieldName, field);
                existingFieldIndexes.put(fieldName, fields.size() - 1);
            }
        }
    }

    private boolean shouldRefineConstructorAssignedFieldType(
            QinIrFieldDeclaration existingField,
            QinIrTypeRef constructorAssignedType) {
        if (existingField == null
                || constructorAssignedType == null
                || constructorAssignedType.kind() != com.qin.lang.ir.QinIrTypeKind.CLASS) {
            return false;
        }
        QinIrExpression initializer = existingField.initializer();
        if (initializer != null && !(initializer instanceof QinIrNullLiteral)) {
            return false;
        }
        QinIrTypeRef existingType = existingField.type();
        if (existingType == null || existingType.kind() != com.qin.lang.ir.QinIrTypeKind.CLASS) {
            return true;
        }
        return "java.lang.Object".equals(existingType.binaryName())
                && !Objects.equals(existingType.binaryName(), constructorAssignedType.binaryName());
    }

    private Set<String> collectConstructorParameterBackedFields(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            List<QinIrFieldDeclaration> fields) {
        if (classDeclaration == null || classDeclaration.body() == null || classDeclaration.body().body() == null) {
            return Set.of();
        }
        MethodDefinition constructor = null;
        for (AstNode member : classDeclaration.body().body()) {
            if (member instanceof MethodDefinition methodDefinition && isConstructorMethod(methodDefinition)) {
                constructor = methodDefinition;
                break;
            }
        }
        if (constructor == null) {
            return Set.of();
        }
        List<QinIrParameter> parameters = lowerParameters(constructor.value(), javaImportLookup, localDeclarationNames);
        if (parameters.isEmpty()) {
            return Set.of();
        }
        Set<String> parameterNames = new LinkedHashSet<>();
        Map<String, QinIrParameter> parametersByName = new LinkedHashMap<>();
        for (QinIrParameter parameter : parameters) {
            parameterNames.add(parameter.name());
            parametersByName.put(parameter.name(), parameter);
        }
        Set<String> thisMemberNames = new LinkedHashSet<>();
        collectThisMemberNames(classDeclaration.body().body(), thisMemberNames);
        if (Boolean.getBoolean("qin.declaration.paramField.trace")) {
            System.out.println("[QinDeclarationIrLowerer] constructorParameterFieldScan class="
                    + classDeclaration.id().name()
                    + " parameters=" + parameterNames
                    + " thisMembers=" + thisMemberNames);
        }
        if (thisMemberNames.isEmpty()) {
            return Set.of();
        }
        Map<String, QinIrFieldDeclaration> existingFields = new LinkedHashMap<>();
        for (QinIrFieldDeclaration field : fields) {
            existingFields.put(field.name(), field);
        }
        Set<String> constructorParameterFieldNames = new LinkedHashSet<>();
        for (String name : parameterNames) {
            if (!thisMemberNames.contains(name) || existingFields.containsKey(name)) {
                continue;
            }
            QinIrParameter parameter = parametersByName.get(name);
            QinIrFieldDeclaration field = new QinIrFieldDeclaration(
                    name,
                    parameter.type(),
                    List.of(),
                    null);
            fields.add(field);
            existingFields.put(name, field);
            constructorParameterFieldNames.add(name);
        }
        if (Boolean.getBoolean("qin.declaration.paramField.trace")) {
            System.out.println("[QinDeclarationIrLowerer] constructorParameterFields class="
                    + classDeclaration.id().name()
                    + " fields=" + constructorParameterFieldNames);
        }
        return Set.copyOf(constructorParameterFieldNames);
    }

    private void collectThisMemberNames(Object root, Set<String> names) {
        collectThisMemberNames(
                root,
                names,
                Collections.newSetFromMap(new IdentityHashMap<>()));
    }

    private void collectThisMemberNames(Object value, Set<String> names, Set<Object> seen) {
        if (value == null) {
            return;
        }
        if (value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Character
                || value instanceof Enum<?>) {
            return;
        }
        if (!seen.add(value)) {
            return;
        }
        if (value instanceof MemberExpression memberExpression
                && !memberExpression.computed()
                && isThisExpressionLike(memberExpression.object())) {
            String propertyName = adapter.extractMemberPropertyName(memberExpression.property());
            if (propertyName != null && !propertyName.isBlank()) {
                names.add(propertyName);
            }
        }
        if (value instanceof Iterable<?> iterable) {
            for (Object element : iterable) {
                collectThisMemberNames(element, names, seen);
            }
            return;
        }
        if (value instanceof Map<?, ?> map) {
            for (Object element : map.values()) {
                collectThisMemberNames(element, names, seen);
            }
            return;
        }
        Class<?> valueClass = value.getClass();
        if (!valueClass.isRecord()
                || valueClass.getPackageName() == null
                || !valueClass.getPackageName().startsWith("com.slime.ast")) {
            return;
        }
        for (Method accessor : thisMemberRecordAccessors(valueClass)) {
            try {
                collectThisMemberNames(accessor.invoke(value), names, seen);
            } catch (ReflectiveOperationException error) {
                throw new IllegalStateException(
                        "Unable to inspect AST component "
                                + valueClass.getName()
                                + "."
                                + accessor.getName(),
                        error);
            }
        }
    }

    private static Method[] thisMemberRecordAccessors(Class<?> valueClass) {
        return THIS_MEMBER_RECORD_ACCESSORS.computeIfAbsent(valueClass, ignored -> {
            List<Method> accessors = new ArrayList<>();
            for (RecordComponent component : valueClass.getRecordComponents()) {
                if (THIS_MEMBER_SCAN_IGNORED_COMPONENTS.contains(component.getName())) {
                    continue;
                }
                accessors.add(component.getAccessor());
            }
            return accessors.toArray(Method[]::new);
        });
    }

    private String constructorAssignedFieldNameOrNull(Statement statement) {
        if (!(statement instanceof ExpressionStatement expressionStatement)) {
            return null;
        }
        Expression expression = expressionStatement.expression();
        if (expression instanceof AssignmentExpression assignmentExpression) {
            return thisMemberNameOrNull(assignmentExpression.left());
        }
        if ("AssignmentExpression".equals(QinSlimeFrontendAdapter.simpleName(expression))) {
            return thisMemberNameOrNull(QinSlimeFrontendAdapter.invokeByName(expression, "left"));
        }
        return null;
    }

    private String thisMemberNameOrNull(Object expressionAst) {
        if (expressionAst instanceof MemberExpression memberExpression) {
            if (memberExpression.computed() || !isThisExpressionLike(memberExpression.object())) {
                return null;
            }
            return adapter.extractMemberPropertyName(memberExpression.property());
        }
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            return null;
        }
        if (Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(expressionAst, "computed"))) {
            return null;
        }
        Object object = QinSlimeFrontendAdapter.invokeByName(expressionAst, "object");
        if (!isThisExpressionLike(object)) {
            return null;
        }
        return adapter.extractMemberPropertyName(QinSlimeFrontendAdapter.invokeByName(expressionAst, "property"));
    }

    private boolean isThisExpressionLike(Object expressionAst) {
        if (expressionAst == null) {
            return false;
        }
        if (expressionAst instanceof ThisExpression) {
            return true;
        }
        String nodeType = QinSlimeFrontendAdapter.simpleName(expressionAst);
        if ("ThisExpression".equals(nodeType)) {
            return true;
        }
        if ("ParenthesizedExpression".equals(nodeType)
                || "TSAsExpression".equals(nodeType)
                || "TSSatisfiesExpression".equals(nodeType)
                || "TSNonNullExpression".equals(nodeType)) {
            try {
                return isThisExpressionLike(QinSlimeFrontendAdapter.invokeByName(expressionAst, "expression"));
            } catch (IllegalArgumentException ignored) {
                return false;
            }
        }
        return false;
    }

    private QinIrTypeRef inferConstructorAssignedFieldType(
            Statement statement,
            List<QinIrParameter> parameters,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            String className,
            Map<String, QinIrExpression> declarationLookup,
            List<QinIrFieldDeclaration> fields) {
        Expression expression = ((ExpressionStatement) statement).expression();
        Object rightAst = expression instanceof AssignmentExpression assignmentExpression
                ? assignmentExpression.right()
                : QinSlimeFrontendAdapter.invokeByName(expression, "right");
        QinIrTypeRef constructorExpressionType = inferConstructorAssignedExpressionTypeOrNull(
                rightAst,
                javaImportLookup,
                jsDeclarationClassLookup,
                localDeclarationNames,
                className,
                declarationLookup,
                fields);
        if (constructorExpressionType != null) {
            return constructorExpressionType;
        }
        String assignedParameterName = rightAst instanceof Identifier identifier
                ? identifier.name()
                : "Identifier".equals(QinSlimeFrontendAdapter.simpleName(rightAst))
                ? QinSlimeFrontendAdapter.extractIdentifierNameStatic(rightAst, "Identifier")
                : null;
        QinIrParameter parameter = resolveDeclarationParameter(parameters, assignedParameterName);
        return parameter == null ? QinIrTypeRef.classType("java.lang.Object") : parameter.type();
    }

    private QinIrTypeRef inferConstructorAssignedExpressionTypeOrNull(
            Object rightAst,
            Map<String, String> javaImportLookup,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            String className,
            Map<String, QinIrExpression> declarationLookup,
            List<QinIrFieldDeclaration> fields) {
        QinIrTypeRef literalType = inferLiteralTypeOrNull(rightAst);
        if (literalType != null) {
            return literalType;
        }
        QinIrTypeRef arrayType = inferConstructorAssignedArrayExpressionTypeOrNull(rightAst);
        if (arrayType != null) {
            return arrayType;
        }
        if (!(rightAst instanceof NewExpression)
                && !"NewExpression".equals(QinSlimeFrontendAdapter.simpleName(rightAst))) {
            return null;
        }
        QinIrExpression loweredRight = lowerDeclarationExpression(
                rightAst,
                javaImportLookup,
                new DeclarationClassContext(
                        className,
                        fields,
                        List.of(),
                        declarationLookup,
                        Map.of(),
                        jsDeclarationClassLookup,
                        localDeclarationNames),
                Map.of());
        if (loweredRight instanceof QinIrJavaNewExpression javaNewExpression) {
            return QinIrTypeRef.classType(javaNewExpression.ownerBinaryName());
        }
        return null;
    }

    private QinIrTypeRef inferConstructorAssignedArrayExpressionTypeOrNull(Object expressionAst) {
        Object expression = unwrapParenthesized(expressionAst);
        if (expression == null) {
            return null;
        }
        String nodeType = QinSlimeFrontendAdapter.simpleName(expression);
        if (expression instanceof com.slime.ast.nodes.expressions.ArrayExpression
                || "ArrayExpression".equals(nodeType)) {
            return objectArrayType();
        }
        if (expression instanceof ConditionalExpression conditionalExpression) {
            return commonObjectArrayTypeOrNull(
                    inferConstructorAssignedArrayExpressionTypeOrNull(conditionalExpression.consequent()),
                    inferConstructorAssignedArrayExpressionTypeOrNull(conditionalExpression.alternate()));
        }
        if ("ConditionalExpression".equals(nodeType)) {
            return commonObjectArrayTypeOrNull(
                    inferConstructorAssignedArrayExpressionTypeOrNull(
                            QinSlimeFrontendAdapter.invokeByName(expression, "consequent")),
                    inferConstructorAssignedArrayExpressionTypeOrNull(
                            QinSlimeFrontendAdapter.invokeByName(expression, "alternate")));
        }
        if (expression instanceof CallExpression callExpression
                && isConstructorAssignedArrayProducingCall(callExpression.callee())) {
            return objectArrayType();
        }
        if ("CallExpression".equals(nodeType)
                && isConstructorAssignedArrayProducingCall(
                QinSlimeFrontendAdapter.invokeByName(expression, "callee"))) {
            return objectArrayType();
        }
        return null;
    }

    private QinIrTypeRef commonObjectArrayTypeOrNull(QinIrTypeRef left, QinIrTypeRef right) {
        return isObjectArrayType(left) && isObjectArrayType(right) ? objectArrayType() : null;
    }

    private boolean isConstructorAssignedArrayProducingCall(Object calleeAst) {
        Object callee = unwrapParenthesized(calleeAst);
        if (callee instanceof MemberExpression memberExpression) {
            return isConstructorAssignedArrayProducingMemberCall(memberExpression.object(), memberExpression.property());
        }
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(callee))) {
            return false;
        }
        return isConstructorAssignedArrayProducingMemberCall(
                QinSlimeFrontendAdapter.invokeByName(callee, "object"),
                QinSlimeFrontendAdapter.invokeByName(callee, "property"));
    }

    private boolean isConstructorAssignedArrayProducingMemberCall(Object receiverAst, Object propertyAst) {
        String methodName = adapter.extractMemberPropertyName(propertyAst);
        if (methodName == null || methodName.isBlank()) {
            return false;
        }
        Object receiver = unwrapParenthesized(receiverAst);
        if ("from".equals(methodName) && "Array".equals(declarationIdentifierName(receiver))) {
            return true;
        }
        return switch (methodName) {
            case "map", "filter", "slice", "flat" ->
                    isObjectArrayType(inferConstructorAssignedArrayExpressionTypeOrNull(receiver));
            default -> false;
        };
    }

    private QinIrTypeRef inferLiteralTypeOrNull(Object expressionAst) {
        Object literalValue;
        if (expressionAst instanceof Literal literal) {
            literalValue = literal.value();
        } else if ("Literal".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            literalValue = QinSlimeFrontendAdapter.invokeByName(expressionAst, "value");
        } else {
            return null;
        }
        if (literalValue instanceof String) {
            return QinIrTypeRef.stringType();
        }
        if (literalValue instanceof Boolean) {
            return QinIrTypeRef.booleanType();
        }
        if (literalValue instanceof Number) {
            return QinIrTypeRef.doubleType();
        }
        return null;
    }

    private QinIrMethodDeclaration lowerMethodDeclarationOrNull(
            MethodDefinition methodDefinition,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            DeclarationClassContext classContext) {
        if (!(methodDefinition.key() instanceof Identifier identifier)) {
            return null;
        }
        boolean previousDeclarationStatic = currentDeclarationStatic;
        Map<String, QinIrTypeRef> previousDeclarationValueTypes = currentDeclarationValueTypes;
        currentDeclarationStatic = methodDefinition.isStatic();
        try {
            FunctionExpression function = methodDefinition.value();
            Map<String, QinIrTypeRef> typeParameterBounds =
                    lowerTypeParameterBounds(function, javaImportLookup, localDeclarationNames);
            List<QinIrParameter> parameters =
                    lowerParameters(function, javaImportLookup, localDeclarationNames, typeParameterBounds);
            parameters = applyGeneratedJavaSdkFacadeParameterTypes(
                    classContext.className(),
                    identifier.name(),
                    parameters);
            parameters = applyInheritedOverrideParameterTypes(
                    identifier.name(),
                    parameters,
                    methodDefinition.isStatic(),
                    classContext);
            currentDeclarationValueTypes = declarationValueTypes(parameters);
            Map<String, QinIrExpression> parameterLocals = parameterLocals(parameters);
            QinIrTypeRef declaredReturnType = function != null && function.returnType() != null
                    ? lowerParameterType(function.returnType(), javaImportLookup, localDeclarationNames, typeParameterBounds)
                    : null;
            QinIrTypeRef inheritedReturnType = declaredReturnType == null
                    ? inheritedOverrideReturnType(
                    identifier.name(),
                    parameters.size(),
                    methodDefinition.isStatic(),
                    classContext)
                    : null;
            List<QinIrStatement> bodyStatements = lowerMethodBodyStatementsOrEmpty(
                    function,
                    javaImportLookup,
                    classContext,
                    parameterLocals);
            List<QinIrStatement> defaultParameterInitializers = lowerDefaultParameterInitializers(
                    function,
                    javaImportLookup,
                    classContext,
                    parameters);
            if (!defaultParameterInitializers.isEmpty()) {
                List<QinIrStatement> mergedBodyStatements = new ArrayList<>(defaultParameterInitializers);
                mergedBodyStatements.addAll(bodyStatements);
                bodyStatements = List.copyOf(mergedBodyStatements);
            }
            QinIrExpression returnExpression = bodyStatements.isEmpty()
                    ? lowerMethodReturnExpression(function, javaImportLookup, classContext, parameterLocals)
                    : singleReturnExpressionOrNull(bodyStatements);
            QinIrTypeRef returnType = declaredReturnType != null
                    ? declaredReturnType
                    : inheritedReturnType != null
                    ? inheritedReturnType
                    : inferDeclarationReturnType(returnExpression, parameters, classContext);
            if (inheritedReturnType == null && isObjectPlaceholderType(returnType)) {
                QinIrTypeRef inferredBodyReturnType = inferDeclarationBodyReturnType(
                        bodyStatements,
                        parameters,
                        classContext);
                if (inferredBodyReturnType != null && !isObjectPlaceholderType(inferredBodyReturnType)) {
                    returnType = inferredBodyReturnType;
                }
            }
            return new QinIrMethodDeclaration(
                    identifier.name(),
                    returnType,
                    parameters,
                    lowerAnnotations(methodDefinition.decorators(), javaImportLookup),
                    returnExpression,
                    bodyStatements,
                    List.of(),
                    null,
                    methodDefinition.isStatic());
        } finally {
            currentDeclarationStatic = previousDeclarationStatic;
            currentDeclarationValueTypes = previousDeclarationValueTypes;
        }
    }

    private QinIrTypeRef inheritedOverrideReturnType(
            String methodName,
            int parameterCount,
            boolean staticMethod,
            DeclarationClassContext classContext) {
        if (classContext == null || staticMethod) {
            return null;
        }
        ResolvedJvmMethodSignature inherited = findInheritedJvmMethod(
                classContext.inheritedOverrideSuperType() == null
                        ? null
                        : classContext.inheritedOverrideSuperType().binaryName(),
                methodName,
                parameterCount,
                classContext.localJvmDeclarations());
        return inherited == null ? null : inherited.returnType();
    }

    private QinIrMethodDeclaration applyInheritedOverrideSignatureParameterTypes(
            QinIrMethodDeclaration method,
            QinIrTypeRef inheritedOverrideSuperType,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (method == null || method.staticMethod()) {
            return method;
        }
        List<QinIrParameter> parameters = applyInheritedOverrideParameterTypes(
                method.name(),
                method.parameters(),
                inheritedOverrideSuperType,
                localJvmDeclarations);
        if (parameters == method.parameters()) {
            return method;
        }
        return new QinIrMethodDeclaration(
                method.name(),
                method.returnType(),
                parameters,
                method.annotations(),
                method.returnExpression(),
                method.bodyStatements(),
                method.superArguments(),
                method.explicitSuperConstructorCall(),
                method.runtimeFunctionDefinition(),
                method.staticMethod(),
                method.abstractMethod());
    }

    private List<QinIrParameter> applyInheritedOverrideParameterTypes(
            String methodName,
            List<QinIrParameter> parameters,
            boolean staticMethod,
            DeclarationClassContext classContext) {
        if (classContext == null || staticMethod) {
            return parameters;
        }
        return applyInheritedOverrideParameterTypes(
                methodName,
                parameters,
                classContext.inheritedOverrideSuperType(),
                classContext.localJvmDeclarations());
    }

    private List<QinIrParameter> applyInheritedOverrideParameterTypes(
            String methodName,
            List<QinIrParameter> parameters,
            QinIrTypeRef inheritedOverrideSuperType,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (methodName == null
                || methodName.isBlank()
                || parameters == null
                || parameters.isEmpty()
                || inheritedOverrideSuperType == null
                || inheritedOverrideSuperType.binaryName() == null
                || inheritedOverrideSuperType.binaryName().isBlank()) {
            return parameters;
        }
        ResolvedJvmMethodSignature inherited = findInheritedJvmMethod(
                inheritedOverrideSuperType.binaryName(),
                methodName,
                parameters.size(),
                localJvmDeclarations);
        if (inherited == null || inherited.parameterTypes().size() != parameters.size()) {
            return parameters;
        }
        List<QinIrParameter> refined = new ArrayList<>(parameters);
        boolean changed = false;
        for (int i = 0; i < refined.size(); i++) {
            QinIrParameter parameter = refined.get(i);
            QinIrTypeRef inheritedType = inherited.parameterTypes().get(i);
            if (parameter == null
                    || inheritedType == null
                    || !isObjectPlaceholderType(parameter.type())
                    || isObjectPlaceholderType(inheritedType)) {
                continue;
            }
            refined.set(i, new QinIrParameter(
                    parameter.name(),
                    inheritedType,
                    parameter.annotations(),
                    parameter.varargs()));
            changed = true;
        }
        return changed ? List.copyOf(refined) : parameters;
    }

    private List<QinIrStatement> lowerDefaultParameterInitializers(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            List<QinIrParameter> parameters) {
        if (function == null) {
            return List.of();
        }
        List<QinIrStatement> initializers = new ArrayList<>();
        if (function.parameterMetadata() != null && !function.parameterMetadata().isEmpty()) {
            for (FunctionParameter parameter : function.parameterMetadata()) {
                if (parameter != null) {
                    addDefaultParameterInitializer(
                            parameter.pattern(),
                            javaImportLookup,
                            classContext,
                            parameters,
                            initializers);
                }
            }
            return List.copyOf(initializers);
        }
        if (function.params() != null) {
            for (com.slime.ast.Pattern pattern : function.params()) {
                addDefaultParameterInitializer(pattern, javaImportLookup, classContext, parameters, initializers);
            }
        }
        return List.copyOf(initializers);
    }

    private void addDefaultParameterInitializer(
            com.slime.ast.Pattern pattern,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            List<QinIrParameter> parameters,
            List<QinIrStatement> initializers) {
        if (!(pattern instanceof AssignmentPattern assignmentPattern)
                || !(assignmentPattern.left() instanceof Identifier identifier)
                || assignmentPattern.right() == null) {
            return;
        }
        QinIrTypeRef parameterType = parameterTypeByName(parameters, identifier.name());
        if (isPrimitiveType(parameterType)) {
            return;
        }
        QinIrExpression parameterReference = new QinIrIdentifierReference(identifier.name());
        QinIrExpression isMissing = new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral("=="), parameterReference, new QinIrNullLiteral()));
        QinIrExpression defaultValue = lowerDeclarationExpression(
                assignmentPattern.right(),
                javaImportLookup,
                classContext,
                Map.of());
        initializers.add(new QinIrIfStatement(
                isMissing,
                List.of(new QinIrStatementExpression(new QinIrAssignmentExpression(
                        new QinIrIdentifierReference(identifier.name()),
                        "=",
                        defaultValue))),
                List.of()));
    }

    private QinIrTypeRef parameterTypeByName(List<QinIrParameter> parameters, String name) {
        if (parameters == null || name == null || name.isBlank()) {
            return null;
        }
        for (QinIrParameter parameter : parameters) {
            if (parameter != null && name.equals(parameter.name())) {
                return parameter.type();
            }
        }
        return null;
    }

    private boolean shouldExternalizeLargeDeclarationMethod(String methodName, FunctionExpression function) {
        if (methodName != null && methodName.startsWith("__qin_subhuti_raw_")) {
            return true;
        }
        String source = sourceTextForNode(function);
        return source != null && source.length() > 120_000;
    }

    private boolean containsJvmUnsupportedFunctionLiteral(List<QinIrStatement> statements) {
        if (statements == null || statements.isEmpty()) {
            return false;
        }
        for (QinIrStatement statement : statements) {
            if (containsJvmUnsupportedFunctionLiteral(statement)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsJvmUnsupportedFunctionLiteral(QinIrStatement statement) {
        if (statement == null) {
            return false;
        }
        if (statement instanceof QinIrLocalDeclarationStatement localDeclaration) {
            return containsJvmUnsupportedFunctionLiteral(localDeclaration.initializer());
        }
        if (statement instanceof QinIrStatementExpression statementExpression) {
            return containsJvmUnsupportedFunctionLiteral(statementExpression.expression());
        }
        if (statement instanceof QinIrReturnStatement returnStatement) {
            return containsJvmUnsupportedFunctionLiteral(returnStatement.value());
        }
        if (statement instanceof QinIrIfStatement ifStatement) {
            return containsJvmUnsupportedFunctionLiteral(ifStatement.test())
                    || containsJvmUnsupportedFunctionLiteral(ifStatement.consequent())
                    || containsJvmUnsupportedFunctionLiteral(ifStatement.alternate());
        }
        if (statement instanceof QinIrForStatement forStatement) {
            for (QinIrLocalVariableDeclaration declaration : forStatement.initializerDeclarations()) {
                if (containsJvmUnsupportedFunctionLiteral(declaration.initializer())) {
                    return true;
                }
            }
            return containsJvmUnsupportedFunctionLiteralExpressions(forStatement.initializerExpressions())
                    || containsJvmUnsupportedFunctionLiteral(forStatement.test())
                    || containsJvmUnsupportedFunctionLiteralExpressions(forStatement.updateExpressions())
                    || containsJvmUnsupportedFunctionLiteral(forStatement.body());
        }
        if (statement instanceof QinIrForEachStatement forEachStatement) {
            return containsJvmUnsupportedFunctionLiteral(forEachStatement.iterable())
                    || containsJvmUnsupportedFunctionLiteral(forEachStatement.body());
        }
        if (statement instanceof QinIrWhileStatementNode whileStatement) {
            return containsJvmUnsupportedFunctionLiteral(whileStatement.test())
                    || containsJvmUnsupportedFunctionLiteral(whileStatement.body());
        }
        if (statement instanceof QinIrDoWhileStatementNode doWhileStatement) {
            return containsJvmUnsupportedFunctionLiteral(doWhileStatement.body())
                    || containsJvmUnsupportedFunctionLiteral(doWhileStatement.test());
        }
        if (statement instanceof QinIrSwitchStatement switchStatement) {
            if (containsJvmUnsupportedFunctionLiteral(switchStatement.discriminant())) {
                return true;
            }
            for (QinIrSwitchCase switchCase : switchStatement.cases()) {
                if (containsJvmUnsupportedFunctionLiteral(switchCase.test())
                        || containsJvmUnsupportedFunctionLiteral(switchCase.consequent())) {
                    return true;
                }
            }
            return false;
        }
        if (statement instanceof QinIrTryStatement tryStatement) {
            for (var resource : tryStatement.resources()) {
                if (containsJvmUnsupportedFunctionLiteral(resource.initializer())
                        || containsJvmUnsupportedFunctionLiteral(resource.reference())) {
                    return true;
                }
            }
            if (containsJvmUnsupportedFunctionLiteral(tryStatement.tryBody())
                    || containsJvmUnsupportedFunctionLiteral(tryStatement.finallyBody())) {
                return true;
            }
            for (QinIrCatchClause catchClause : tryStatement.catchClauses()) {
                if (containsJvmUnsupportedFunctionLiteral(catchClause.body())) {
                    return true;
                }
            }
            return false;
        }
        if (statement instanceof QinIrThrowStatement throwStatement) {
            return containsJvmUnsupportedFunctionLiteral(throwStatement.value());
        }
        return false;
    }

    private boolean containsJvmUnsupportedFunctionLiteralExpressions(List<? extends QinIrExpression> expressions) {
        if (expressions == null || expressions.isEmpty()) {
            return false;
        }
        for (QinIrExpression expression : expressions) {
            if (containsJvmUnsupportedFunctionLiteral(expression)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsJvmUnsupportedFunctionLiteral(QinIrExpression expression) {
        if (expression == null) {
            return false;
        }
        if (expression instanceof com.qin.lang.ir.QinIrFunctionLiteral) {
            return true;
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return containsJvmUnsupportedFunctionLiteralExpressions(builtinCallExpression.arguments());
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            return containsJvmUnsupportedFunctionLiteral(methodCallExpression.receiver())
                    || containsJvmUnsupportedFunctionLiteralExpressions(methodCallExpression.arguments());
        }
        if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            return containsJvmUnsupportedFunctionLiteralExpressions(staticMethodCallExpression.arguments());
        }
        if (expression instanceof QinIrSuperMethodCallExpression superMethodCallExpression) {
            return containsJvmUnsupportedFunctionLiteralExpressions(superMethodCallExpression.arguments());
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                if (containsJvmUnsupportedFunctionLiteral(property.value())) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            return containsJvmUnsupportedFunctionLiteralExpressions(arrayLiteral.elements());
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            return containsJvmUnsupportedFunctionLiteral(propertyAccessExpression.receiver());
        }
        if (expression instanceof QinIrElementAccessExpression elementAccessExpression) {
            return containsJvmUnsupportedFunctionLiteral(elementAccessExpression.receiver())
                    || containsJvmUnsupportedFunctionLiteral(elementAccessExpression.index());
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            return containsJvmUnsupportedFunctionLiteralExpressions(sequenceExpression.leadingExpressions())
                    || containsJvmUnsupportedFunctionLiteral(sequenceExpression.resultExpression());
        }
        if (expression instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            return containsJvmUnsupportedFunctionLiteral(spreadArgumentExpression.expression());
        }
        if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            return containsJvmUnsupportedFunctionLiteral(assignmentExpression.target())
                    || containsJvmUnsupportedFunctionLiteral(assignmentExpression.value());
        }
        if (expression instanceof QinIrUpdateExpression updateExpression) {
            return containsJvmUnsupportedFunctionLiteral(updateExpression.target());
        }
        if (expression instanceof QinIrBoundMethodReferenceExpression boundMethodReferenceExpression) {
            return containsJvmUnsupportedFunctionLiteral(boundMethodReferenceExpression.receiver());
        }
        return false;
    }

    private QinIrMethodDeclaration lowerConstructorDeclarationOrNull(
            MethodDefinition methodDefinition,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            DeclarationClassContext classContext,
            Set<String> constructorParameterFieldNames) {
        FunctionExpression function = methodDefinition.value();
        List<QinIrParameter> parameters = lowerParameters(function, javaImportLookup, localDeclarationNames);
        List<QinIrExpression> superArguments = lowerExplicitSuperArguments(function, javaImportLookup, classContext);
        List<QinIrStatement> bodyStatements = lowerDeclarationStatements(
                constructorBodyWithoutLeadingSuper(function),
                javaImportLookup,
                classContext,
                Map.of());
        List<QinIrStatement> defaultParameterInitializers = lowerDefaultParameterInitializers(
                function,
                javaImportLookup,
                classContext,
                parameters);
        if (!defaultParameterInitializers.isEmpty()) {
            List<QinIrStatement> mergedBodyStatements = new ArrayList<>(defaultParameterInitializers);
            mergedBodyStatements.addAll(bodyStatements);
            bodyStatements = List.copyOf(mergedBodyStatements);
        }
        if (constructorParameterFieldNames != null && !constructorParameterFieldNames.isEmpty()) {
            List<QinIrStatement> constructorParameterAssignments = new ArrayList<>();
            Set<String> parameterNames = new LinkedHashSet<>();
            for (QinIrParameter parameter : parameters) {
                parameterNames.add(parameter.name());
            }
            for (String fieldName : constructorParameterFieldNames) {
                if (!parameterNames.contains(fieldName)) {
                    continue;
                }
                constructorParameterAssignments.add(new QinIrStatementExpression(new QinIrAssignmentExpression(
                        new QinIrPropertyAccessExpression(new QinIrThisExpression(), fieldName),
                        "=",
                        new QinIrIdentifierReference(fieldName))));
            }
            if (!constructorParameterAssignments.isEmpty()) {
                List<QinIrStatement> mergedBodyStatements = new ArrayList<>(constructorParameterAssignments);
                mergedBodyStatements.addAll(bodyStatements);
                bodyStatements = List.copyOf(mergedBodyStatements);
            }
        }
        return new QinIrMethodDeclaration(
                "constructor",
                QinIrTypeRef.voidType(),
                parameters,
                lowerAnnotations(methodDefinition.decorators(), javaImportLookup),
                null,
                bodyStatements,
                superArguments,
                hasLeadingSuperCall(function),
                null,
                false);
    }

    private List<QinIrParameter> lowerParameters(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        return lowerParameters(
                function,
                javaImportLookup,
                localDeclarationNames,
                lowerTypeParameterBounds(function, javaImportLookup, localDeclarationNames));
    }

    private List<QinIrParameter> lowerParameters(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        if (function == null) {
            return List.of();
        }

        if (function.parameterMetadata() != null && !function.parameterMetadata().isEmpty()) {
            List<QinIrParameter> parameters = new ArrayList<>();
            for (FunctionParameter parameter : function.parameterMetadata()) {
                if (parameter == null) {
                    throw qjsError("QJS2011", "Only identifier parameters are supported in declaration subset");
                }
                if (parameter.pattern() instanceof RestElement restElement) {
                    parameters.add(new QinIrParameter(
                            lowerRestParameterName(restElement),
                            lowerRestParameterType(
                                    restParameterTypeAnnotation(parameter.typeAnnotation(), restElement),
                                    javaImportLookup,
                                    localDeclarationNames,
                                    typeParameterBounds),
                            lowerAnnotations(parameter.decorators(), javaImportLookup),
                            true));
                    continue;
                }
                if (parameter.pattern() instanceof AssignmentPattern assignmentPattern
                        && assignmentPattern.left() instanceof Identifier identifier) {
                    parameters.add(new QinIrParameter(
                            identifier.name(),
                            lowerParameterType(
                                    parameter.typeAnnotation(),
                                    javaImportLookup,
                                    localDeclarationNames,
                                    typeParameterBounds),
                            lowerAnnotations(parameter.decorators(), javaImportLookup)));
                    continue;
                }
                if (!(parameter.pattern() instanceof Identifier identifier)) {
                    throw qjsError("QJS2011", "Only identifier parameters are supported in declaration subset");
                }
                parameters.add(new QinIrParameter(
                        identifier.name(),
                        lowerParameterType(
                                parameter.typeAnnotation(),
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds),
                        lowerAnnotations(parameter.decorators(), javaImportLookup)));
            }
            return List.copyOf(parameters);
        }

        if (function.params() == null || function.params().isEmpty()) {
            return List.of();
        }

        List<QinIrParameter> parameters = new ArrayList<>();
        for (com.slime.ast.Pattern pattern : function.params()) {
            if (pattern instanceof RestElement restElement) {
                parameters.add(new QinIrParameter(
                        lowerRestParameterName(restElement),
                        lowerRestParameterType(
                                restParameterTypeAnnotation(null, restElement),
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds),
                        List.of(),
                        true));
                continue;
            }
            if (pattern instanceof AssignmentPattern assignmentPattern
                    && assignmentPattern.left() instanceof Identifier identifier) {
                parameters.add(new QinIrParameter(
                        identifier.name(),
                        lowerParameterType(
                                parameterPatternTypeAnnotation(assignmentPattern.left(), assignmentPattern),
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds),
                        List.of()));
                continue;
            }
            if (!(pattern instanceof Identifier identifier)) {
                throw qjsError("QJS2011", "Only identifier parameters are supported in declaration subset");
            }
            parameters.add(new QinIrParameter(
                    identifier.name(),
                    lowerParameterType(
                            parameterPatternTypeAnnotation(identifier, null),
                            javaImportLookup,
                            localDeclarationNames,
                            typeParameterBounds),
                    List.of()));
        }
        return List.copyOf(parameters);
    }

    private AstNode parameterPatternTypeAnnotation(Object primaryPattern, Object fallbackPattern) {
        Object primaryTypeAnnotation = invokeByNameOrNull(primaryPattern, "typeAnnotation");
        if (primaryTypeAnnotation instanceof AstNode astNode) {
            return astNode;
        }
        Object fallbackTypeAnnotation = invokeByNameOrNull(fallbackPattern, "typeAnnotation");
        return fallbackTypeAnnotation instanceof AstNode astNode ? astNode : null;
    }

    private QinIrMethodDeclaration applyGeneratedJavaSdkFacadeSignatureTypes(
            String className,
            QinIrMethodDeclaration method) {
        if (method == null) {
            return null;
        }
        List<QinIrParameter> parameters = applyGeneratedJavaSdkFacadeParameterTypes(
                className,
                method.name(),
                method.parameters());
        if (parameters == method.parameters()) {
            return method;
        }
        return new QinIrMethodDeclaration(
                method.name(),
                method.returnType(),
                parameters,
                method.annotations(),
                method.returnExpression(),
                method.bodyStatements(),
                method.superArguments(),
                method.explicitSuperConstructorCall(),
                method.runtimeFunctionDefinition(),
                method.staticMethod(),
                method.abstractMethod());
    }

    private List<QinIrParameter> applyGeneratedJavaSdkFacadeParameterTypes(
            String className,
            String methodName,
            List<QinIrParameter> parameters) {
        if (parameters == null || parameters.size() != 1) {
            return parameters;
        }
        String canonicalClassName = QinJavaSdkAliasSupport.canonicalBinaryName(className);
        if (!"writeBoolean".equals(methodName)
                || (!"__QinJavaIoDataOutputStream".equals(className)
                && !"java.io.DataOutputStream".equals(canonicalClassName))) {
            return parameters;
        }
        QinIrParameter parameter = parameters.get(0);
        if (parameter == null || QinIrTypeRef.booleanType().equals(parameter.type())) {
            return parameters;
        }
        List<QinIrParameter> refined = new ArrayList<>(parameters);
        refined.set(0, new QinIrParameter(
                parameter.name(),
                QinIrTypeRef.booleanType(),
                parameter.annotations(),
                parameter.varargs()));
        return List.copyOf(refined);
    }

    private String lowerRestParameterName(RestElement restElement) {
        if (restElement.argument() instanceof Identifier identifier && !identifier.name().isBlank()) {
            return identifier.name();
        }
        throw qjsError("QJS2011", "Only identifier rest parameters are supported in declaration subset");
    }

    private QinIrTypeRef lowerRestParameterType(
            AstNode typeAnnotation,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        QinIrTypeRef lowered = lowerParameterType(
                typeAnnotation,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        if (!isObjectArrayType(lowered)) {
            return objectArrayType(boxForObjectStorage(lowered));
        }
        return lowered;
    }

    private AstNode restParameterTypeAnnotation(AstNode parameterTypeAnnotation, RestElement restElement) {
        if (parameterTypeAnnotation != null) {
            return parameterTypeAnnotation;
        }
        Object restTypeAnnotation = invokeByNameOrNull(restElement, "typeAnnotation");
        if (restTypeAnnotation instanceof AstNode astNode) {
            return astNode;
        }
        Object argument = restElement == null ? null : restElement.argument();
        Object argumentTypeAnnotation = invokeByNameOrNull(argument, "typeAnnotation");
        return argumentTypeAnnotation instanceof AstNode astNode ? astNode : null;
    }

    private Object invokeByNameOrNull(Object target, String methodName) {
        if (target == null || methodName == null || methodName.isBlank()) {
            return null;
        }
        try {
            return QinSlimeFrontendAdapter.invokeByName(target, methodName);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private QinIrTypeRef objectArrayType() {
        return QinIrTypeRef.classType("java.lang.Object[]");
    }

    private QinIrTypeRef objectArrayType(QinIrTypeRef elementType) {
        if (elementType == null || isJavaLangObjectType(elementType)) {
            return objectArrayType();
        }
        return QinIrTypeRef.classType("java.lang.Object[]", List.of(elementType));
    }

    private QinIrTypeRef lowerParameterType(
            AstNode typeAnnotationAst,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        return lowerParameterType(typeAnnotationAst, javaImportLookup, localDeclarationNames, Map.of());
    }

    private QinIrTypeRef lowerParameterType(
            AstNode typeAnnotationAst,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        if (!(typeAnnotationAst instanceof TSTypeAnnotation annotation) || annotation.typeAnnotation() == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        AstNode typeAst = annotation.typeAnnotation();
        if ("TSArrayType".equals(QinSlimeFrontendAdapter.simpleName(typeAst))) {
            return lowerArrayType(
                    typeAst,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds);
        }
        int arraySuffixDimensions = typeAnnotationArraySuffixDimensions(annotation, typeAst);
        if (arraySuffixDimensions > 0) {
            return wrapArrayType(lowerTypeNodeRef(
                    typeAst,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds),
                    arraySuffixDimensions);
        }
        String typeDiscriminator = typeNodeDiscriminator(typeAst);
        if (QinSlimeFrontendAdapter.simpleName(typeAst).contains("Union")
                || (typeDiscriminator != null && typeDiscriminator.contains("Union"))) {
            return lowerUnionOrIntersectionTypeOrNull(
                    typeAst,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds);
        }
        if (typeAst instanceof TSKeywordType keywordType) {
            QinIrTypeRef keywordRef = switch (keywordType.keyword()) {
                case "string" -> QinIrTypeRef.stringType();
                case "boolean" -> QinIrTypeRef.booleanType();
                case "number" -> QinIrTypeRef.doubleType();
                case "void" -> QinIrTypeRef.voidType();
                default -> QinIrTypeRef.classType("java.lang.Object");
            };
            return typeAnnotationHasNullableUnion(annotation, typeAst)
                    ? boxForObjectStorage(keywordRef)
                    : keywordRef;
        }
        if (typeAst instanceof TSTypeReference typeReference) {
            String typeName = typeReferenceName(typeReference.typeName());
            if (typeName == null || typeName.isBlank()) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            QinIrTypeRef typeParameterBound = typeParameterBounds.get(typeName);
            if (typeParameterBound != null) {
                return typeParameterBound;
            }
            if ("Record".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.Map");
            }
            String importedBinaryName = javaImportLookup.get(typeName);
            if (importedBinaryName != null && !importedBinaryName.isBlank()) {
                QinIrTypeRef importedGenericType = importedGenericTypeOrNull(
                        importedBinaryName,
                        typeReference,
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds);
                return importedGenericType == null
                        ? QinIrTypeRef.classType(importedBinaryName)
                        : importedGenericType;
            }
            if (localDeclarationNames.contains(typeName)) {
                return QinIrTypeRef.classType(typeName);
            }
            QinIrTypeRef javaSdkFacadeType = javaSdkFacadeTypeReferenceOrNull(
                    typeName,
                    typeReference,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds);
            if (javaSdkFacadeType != null) {
                return javaSdkFacadeType;
            }
            if (QinJavaSdkAliasSupport.isKnownAlias(typeName)) {
                QinIrTypeRef importedGenericType = importedGenericTypeOrNull(
                        QinJavaSdkAliasSupport.canonicalBinaryName(typeName),
                        typeReference,
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds);
                return importedGenericType == null
                        ? QinIrTypeRef.classType(QinJavaSdkAliasSupport.canonicalBinaryName(typeName))
                        : importedGenericType;
            }
            if ("Iterable".equals(typeName) || "java.lang.Iterable".equals(typeName)) {
                return QinIrTypeRef.classType(
                        "java.lang.Iterable",
                        List.of(firstTypeArgumentOrObject(
                                typeReference.typeParameters(),
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds)));
            }
            if (typeName.startsWith("java.")) {
                return QinIrTypeRef.classType(typeName);
            }
            if (isStructuralSlimeAstTypeName(typeName)) {
                return structuralSlimeAstType(typeName);
            }
            if (isGeneratedFlattenedDeclarationTypeName(typeName)) {
                return QinIrTypeRef.classType(typeName);
            }
            return switch (typeName) {
                case "__QinJavaUtilList", "__QinJavaUtilArrayList", "java.util.List", "java.util.ArrayList" ->
                        QinIrTypeRef.classType(
                                "java.util.List",
                                List.of(firstTypeArgumentOrObject(
                                        typeReference.typeParameters(),
                                        javaImportLookup,
                                        localDeclarationNames,
                                        typeParameterBounds)));
                case "__QinJavaUtilArrayDeque", "__QinJavaUtilDeque", "java.util.Deque", "java.util.ArrayDeque" ->
                        QinIrTypeRef.classType(
                                "java.util.ArrayDeque",
                                List.of(firstTypeArgumentOrObject(
                                        typeReference.typeParameters(),
                                        javaImportLookup,
                                        localDeclarationNames,
                                        typeParameterBounds)));
                case "__QinJavaUtilSet", "__QinJavaUtilHashSet", "__QinJavaUtilTreeSet",
                        "java.util.Set", "java.util.HashSet", "java.util.LinkedHashSet", "java.util.TreeSet" ->
                        QinIrTypeRef.classType(
                                "java.util.Set",
                                List.of(firstTypeArgumentOrObject(
                                        typeReference.typeParameters(),
                                        javaImportLookup,
                                        localDeclarationNames,
                                        typeParameterBounds)));
                case "__QinJavaUtilHashMap",
                        "__QinJavaUtilLinkedHashMap",
                        "__QinJavaUtilIdentityHashMap",
                        "java.util.Map",
                        "java.util.HashMap",
                        "java.util.LinkedHashMap",
                        "java.util.IdentityHashMap",
                        "java.util.concurrent.ConcurrentHashMap",
                        "java.util.concurrent.ConcurrentMap" ->
                        javaSdkMapType(
                                typeReference,
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds);
                case "__QinJavaUtilMapEntry", "java.util.Map$Entry", "java.util.Map.Entry" ->
                        javaSdkMapEntryType(
                                typeReference,
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds);
                case "__QinJavaUtilStream", "java.util.stream.Stream" ->
                        QinIrTypeRef.classType(
                                "java.util.stream.Stream",
                                List.of(firstTypeArgumentOrObject(
                                        typeReference.typeParameters(),
                                        javaImportLookup,
                                        localDeclarationNames,
                                        typeParameterBounds)));
                case "Array" -> objectArrayType(lowerFirstTypeArgumentOrNull(
                        typeReference.typeParameters(),
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds));
                case "Set" -> QinIrTypeRef.classType("com.qin.lang.runtime.JavaEsmSetObject");
                case "Map" -> javaScriptMapType(
                        typeReference,
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds);
                case "String" -> QinIrTypeRef.stringType();
                case "Boolean" -> QinIrTypeRef.booleanType();
                case "Double", "Number" -> QinIrTypeRef.doubleType();
                case "SubhutiCst" -> QinIrTypeRef.classType("com.subhuti.struct.SubhutiCst");
                case "QinJavaRunnable" -> QinIrTypeRef.classType("java.lang.Runnable");
                case "QinJavaCallable" -> QinIrTypeRef.classType("java.util.concurrent.Callable");
                case "QinJavaPredicate" -> QinIrTypeRef.classType("java.util.function.Predicate");
                case "QinJavaFunction" -> QinIrTypeRef.classType("java.util.function.Function");
                case "QinJavaConsumer" -> QinIrTypeRef.classType("java.util.function.Consumer");
                case "QinJavaBiPredicate" -> QinIrTypeRef.classType("java.util.function.BiPredicate");
                case "QinJavaBiFunction" -> QinIrTypeRef.classType("java.util.function.BiFunction");
                case "QinJavaBiConsumer" -> QinIrTypeRef.classType("java.util.function.BiConsumer");
                case "QinJavaSupplier" -> QinIrTypeRef.classType("java.util.function.Supplier");
                case "QinJavaComparator" -> QinIrTypeRef.classType("java.util.Comparator");
                default -> QinIrTypeRef.classType("java.lang.Object");
            };
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private Map<String, QinIrTypeRef> lowerTypeParameterBounds(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        if (function == null || !(function.typeParameters() instanceof TSTypeParameterDeclaration declaration)) {
            return Map.of();
        }
        if (declaration.params() == null || declaration.params().isEmpty()) {
            return Map.of();
        }
        Map<String, QinIrTypeRef> bounds = new LinkedHashMap<>();
        for (AstNode parameterAst : declaration.params()) {
            if (!(parameterAst instanceof TSTypeParameter parameter)
                    || parameter.name() == null
                    || parameter.name().name() == null
                    || parameter.name().name().isBlank()
                    || parameter.constraint() == null) {
                continue;
            }
            QinIrTypeRef bound = lowerTypeNodeRef(
                    parameter.constraint(),
                    javaImportLookup,
                    localDeclarationNames,
                    bounds);
            if (bound != null && !isJavaLangObjectType(bound)) {
                bounds.put(parameter.name().name(), bound);
            }
        }
        return bounds.isEmpty() ? Map.of() : Map.copyOf(bounds);
    }

    private QinIrTypeRef lowerTypeNodeRef(
            Object typeAst,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        if ("TSArrayType".equals(QinSlimeFrontendAdapter.simpleName(typeAst))) {
            return lowerArrayType(
                    typeAst,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds);
        }
        if (typeAst instanceof TSTypeParameterInstantiation typeParameters) {
            return lowerFirstTypeArgumentOrNull(
                    typeParameters,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds);
        }
        if (typeAst instanceof TSTypeReference typeReference) {
            String typeName = typeReferenceName(typeReference.typeName());
            if (typeName == null || typeName.isBlank()) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            QinIrTypeRef typeParameterBound = typeParameterBounds.get(typeName);
            if (typeParameterBound != null) {
                return typeParameterBound;
            }
            if ("Record".equals(typeName)) {
                return QinIrTypeRef.classType("com.qin.lang.runtime.JavaEsmMapObject");
            }
            String importedBinaryName = javaImportLookup.get(typeName);
            if (importedBinaryName != null && !importedBinaryName.isBlank()) {
                QinIrTypeRef importedGenericType = importedGenericTypeOrNull(
                        importedBinaryName,
                        typeReference,
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds);
                return importedGenericType == null
                        ? QinIrTypeRef.classType(importedBinaryName)
                        : importedGenericType;
            }
            if (localDeclarationNames.contains(typeName)) {
                return QinIrTypeRef.classType(typeName);
            }
            QinIrTypeRef javaSdkFacadeType = javaSdkFacadeTypeReferenceOrNull(
                    typeName,
                    typeReference,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds);
            if (javaSdkFacadeType != null) {
                return javaSdkFacadeType;
            }
            if (QinJavaSdkAliasSupport.isKnownAlias(typeName)) {
                QinIrTypeRef importedGenericType = importedGenericTypeOrNull(
                        QinJavaSdkAliasSupport.canonicalBinaryName(typeName),
                        typeReference,
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds);
                return importedGenericType == null
                        ? QinIrTypeRef.classType(QinJavaSdkAliasSupport.canonicalBinaryName(typeName))
                        : importedGenericType;
            }
            if ("Iterable".equals(typeName) || "java.lang.Iterable".equals(typeName)) {
                return QinIrTypeRef.classType(
                        "java.lang.Iterable",
                        List.of(firstTypeArgumentOrObject(
                                typeReference.typeParameters(),
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds)));
            }
            if (typeName.startsWith("java.")) {
                return QinIrTypeRef.classType(typeName);
            }
            if (isStructuralSlimeAstTypeName(typeName)) {
                return structuralSlimeAstType(typeName);
            }
            if (isGeneratedFlattenedDeclarationTypeName(typeName)) {
                return QinIrTypeRef.classType(typeName);
            }
            if ("Set".equals(typeName)) {
                return QinIrTypeRef.classType("com.qin.lang.runtime.JavaEsmSetObject");
            }
            if ("Map".equals(typeName)) {
                return javaScriptMapType(
                        typeReference,
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds);
            }
            if ("__QinJavaUtilList".equals(typeName)
                    || "__QinJavaUtilArrayList".equals(typeName)
                    || "java.util.List".equals(typeName)
                    || "java.util.ArrayList".equals(typeName)) {
                return QinIrTypeRef.classType(
                        "java.util.List",
                        List.of(firstTypeArgumentOrObject(
                                typeReference.typeParameters(),
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds)));
            }
            if ("__QinJavaUtilArrayDeque".equals(typeName)
                    || "__QinJavaUtilDeque".equals(typeName)
                    || "java.util.Deque".equals(typeName)
                    || "java.util.ArrayDeque".equals(typeName)) {
                return QinIrTypeRef.classType(
                        "java.util.ArrayDeque",
                        List.of(firstTypeArgumentOrObject(
                                typeReference.typeParameters(),
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds)));
            }
            if ("__QinJavaUtilHashMap".equals(typeName)
                    || "__QinJavaUtilLinkedHashMap".equals(typeName)
                    || "__QinJavaUtilIdentityHashMap".equals(typeName)
                    || "java.util.Map".equals(typeName)
                    || "java.util.HashMap".equals(typeName)
                    || "java.util.LinkedHashMap".equals(typeName)
                    || "java.util.IdentityHashMap".equals(typeName)
                    || "java.util.concurrent.ConcurrentHashMap".equals(typeName)
                    || "java.util.concurrent.ConcurrentMap".equals(typeName)) {
                return javaSdkMapType(
                        typeReference,
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds);
            }
            if ("__QinJavaUtilMapEntry".equals(typeName)
                    || "java.util.Map$Entry".equals(typeName)
                    || "java.util.Map.Entry".equals(typeName)) {
                return javaSdkMapEntryType(
                        typeReference,
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds);
            }
            if ("__QinJavaUtilStream".equals(typeName) || "java.util.stream.Stream".equals(typeName)) {
                return QinIrTypeRef.classType(
                        "java.util.stream.Stream",
                        List.of(firstTypeArgumentOrObject(
                                typeReference.typeParameters(),
                                javaImportLookup,
                                localDeclarationNames,
                                typeParameterBounds)));
            }
            if ("Array".equals(typeName)) {
                return objectArrayType(lowerFirstTypeArgumentOrNull(
                        typeReference.typeParameters(),
                        javaImportLookup,
                        localDeclarationNames,
                        typeParameterBounds));
            }
            if ("QinJavaRunnable".equals(typeName)) {
                return QinIrTypeRef.classType("java.lang.Runnable");
            }
            if ("QinJavaCallable".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.concurrent.Callable");
            }
            if ("QinJavaPredicate".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.function.Predicate");
            }
            if ("QinJavaFunction".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.function.Function");
            }
            if ("QinJavaConsumer".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.function.Consumer");
            }
            if ("QinJavaBiPredicate".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.function.BiPredicate");
            }
            if ("QinJavaBiFunction".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.function.BiFunction");
            }
            if ("QinJavaBiConsumer".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.function.BiConsumer");
            }
            if ("QinJavaSupplier".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.function.Supplier");
            }
            if ("QinJavaComparator".equals(typeName)) {
                return QinIrTypeRef.classType("java.util.Comparator");
            }
        }
        if (typeAst instanceof com.slime.ast.nodes.typescript.TSKeywordType keywordType) {
            return switch (keywordType.keyword()) {
                case "string" -> QinIrTypeRef.stringType();
                case "boolean" -> QinIrTypeRef.booleanType();
                case "number" -> QinIrTypeRef.doubleType();
                default -> QinIrTypeRef.classType("java.lang.Object");
            };
        }
        String typeDiscriminator = typeNodeDiscriminator(typeAst);
        if (QinSlimeFrontendAdapter.simpleName(typeAst).contains("Union")
                || (typeDiscriminator != null && typeDiscriminator.contains("Union"))) {
            return lowerUnionOrIntersectionTypeOrNull(
                    typeAst,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds);
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef javaSdkFacadeTypeReferenceOrNull(
            String typeName,
            TSTypeReference typeReference,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        return switch (typeName) {
            case "__QinJavaUtilList", "__QinJavaUtilArrayList", "__QinJavaUtilUnmodifiableList" ->
                    QinIrTypeRef.classType(
                            "java.util.List",
                            List.of(firstTypeArgumentOrObject(
                                    typeReference.typeParameters(),
                                    javaImportLookup,
                                    localDeclarationNames,
                                    typeParameterBounds)));
            case "__QinJavaUtilSet", "__QinJavaUtilHashSet", "__QinJavaUtilTreeSet",
                    "__QinJavaUtilUnmodifiableSet" ->
                    QinIrTypeRef.classType(
                            "java.util.Set",
                            List.of(firstTypeArgumentOrObject(
                                    typeReference.typeParameters(),
                                    javaImportLookup,
                                    localDeclarationNames,
                                    typeParameterBounds)));
            case "__QinJavaUtilMap", "__QinJavaUtilHashMap", "__QinJavaUtilLinkedHashMap",
                    "__QinJavaUtilIdentityHashMap", "__QinJavaUtilUnmodifiableMap" ->
                    javaSdkMapType(
                            typeReference,
                            javaImportLookup,
                            localDeclarationNames,
                            typeParameterBounds);
            case "__QinJavaUtilMapEntry" ->
                    javaSdkMapEntryType(
                            typeReference,
                            javaImportLookup,
                            localDeclarationNames,
                            typeParameterBounds);
            default -> null;
        };
    }

    private QinIrTypeRef importedGenericTypeOrNull(
            String importedBinaryName,
            TSTypeReference typeReference,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        if (importedBinaryName == null || importedBinaryName.isBlank()
                || typeReference == null) {
            return null;
        }
        String canonicalName = QinJavaSdkAliasSupport.canonicalBinaryName(importedBinaryName);
        return switch (canonicalName) {
            case "java.lang.Iterable",
                    "java.util.Collection",
                    "java.util.List",
                    "java.util.ArrayList",
                    "java.util.Deque",
                    "java.util.ArrayDeque",
                    "java.util.Set",
                    "java.util.HashSet",
                    "java.util.LinkedHashSet" ->
                    QinIrTypeRef.classType(
                            canonicalName,
                            List.of(firstTypeArgumentOrObject(
                                    typeReference.typeParameters(),
                                    javaImportLookup,
                                    localDeclarationNames,
                                    typeParameterBounds)));
            case "java.util.Map",
                    "java.util.HashMap",
                    "java.util.LinkedHashMap",
                    "java.util.IdentityHashMap",
                    "java.util.concurrent.ConcurrentHashMap",
                    "java.util.concurrent.ConcurrentMap" ->
                    javaSdkMapType(
                            typeReference,
                            javaImportLookup,
                            localDeclarationNames,
                            typeParameterBounds);
            case "java.util.Map$Entry", "java.util.Map.Entry" ->
                    javaSdkMapEntryType(
                            typeReference,
                            javaImportLookup,
                            localDeclarationNames,
                            typeParameterBounds);
            default -> null;
        };
    }

    private QinIrTypeRef javaSdkMapType(
            TSTypeReference typeReference,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        Object typeParameters = typeReference == null ? null : typeReference.typeParameters();
        QinIrTypeRef keyType = typeArgumentOrObject(
                typeParameters,
                0,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        QinIrTypeRef valueType = typeArgumentOrObject(
                typeParameters,
                1,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        return QinIrTypeRef.classType("java.util.Map", List.of(keyType, valueType));
    }

    private QinIrTypeRef javaScriptMapType(
            TSTypeReference typeReference,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        Object typeParameters = typeReference == null ? null : typeReference.typeParameters();
        QinIrTypeRef keyType = typeArgumentOrObject(
                typeParameters,
                0,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        QinIrTypeRef valueType = typeArgumentOrObject(
                typeParameters,
                1,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        return QinIrTypeRef.classType(
                "com.qin.lang.runtime.JavaEsmMapObject",
                List.of(keyType, valueType));
    }

    private QinIrTypeRef javaSdkMapEntryType(
            TSTypeReference typeReference,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        Object typeParameters = typeReference == null ? null : typeReference.typeParameters();
        QinIrTypeRef keyType = typeArgumentOrObject(
                typeParameters,
                0,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        QinIrTypeRef valueType = typeArgumentOrObject(
                typeParameters,
                1,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        return QinIrTypeRef.classType("java.util.Map$Entry", List.of(keyType, valueType));
    }

    private QinIrTypeRef typeArgumentOrObject(
            Object typeParameters,
            int index,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        if (!(typeParameters instanceof TSTypeParameterInstantiation instantiation)
                || instantiation.params() == null
                || instantiation.params().size() <= index) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        QinIrTypeRef type = lowerTypeNodeRef(
                instantiation.params().get(index),
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        return type == null ? QinIrTypeRef.classType("java.lang.Object") : type;
    }

    private QinIrTypeRef firstTypeArgumentOrObject(
            Object typeParameters,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        QinIrTypeRef elementType = lowerFirstTypeArgumentOrNull(
                typeParameters,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        return elementType == null ? QinIrTypeRef.classType("java.lang.Object") : elementType;
    }

    private QinIrTypeRef lowerFirstTypeArgumentOrNull(
            Object typeParameters,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        if (!(typeParameters instanceof TSTypeParameterInstantiation instantiation)
                || instantiation.params() == null
                || instantiation.params().isEmpty()) {
            return null;
        }
        return lowerTypeNodeRef(
                instantiation.params().get(0),
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
    }

    private QinIrTypeRef lowerArrayElementTypeOrNull(
            Object typeAst,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        Object elementType = typeNodePropertyOrNull(typeAst, "elementType");
        if (elementType == null) {
            elementType = typeNodePropertyOrNull(typeAst, "element");
        }
        return elementType == null
                ? null
                : lowerTypeNodeRef(elementType, javaImportLookup, localDeclarationNames, typeParameterBounds);
    }

    private QinIrTypeRef lowerArrayType(
            Object typeAst,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        QinIrTypeRef elementType = lowerArrayElementTypeOrNull(
                typeAst,
                javaImportLookup,
                localDeclarationNames,
                typeParameterBounds);
        return objectArrayType(elementType);
    }

    private QinIrTypeRef wrapArrayType(QinIrTypeRef elementType, int dimensions) {
        QinIrTypeRef current = elementType;
        for (int i = 0; i < dimensions; i++) {
            current = objectArrayType(current);
        }
        return current;
    }

    private QinIrTypeRef lowerUnionOrIntersectionTypeOrNull(
            Object typeAst,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrTypeRef> typeParameterBounds) {
        Object typesObject = typeNodePropertyOrNull(typeAst, "types");
        if (typesObject == null) {
            typesObject = typeNodePropertyOrNull(typeAst, "params");
        }
        if (!(typesObject instanceof List<?> types) || types.isEmpty()) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        QinIrTypeRef selected = null;
        boolean nullable = false;
        for (Object memberTypeAst : types) {
            if (isNullishTypeNode(memberTypeAst)) {
                nullable = true;
                continue;
            }
            QinIrTypeRef memberType = lowerTypeNodeRef(
                    memberTypeAst,
                    javaImportLookup,
                    localDeclarationNames,
                    typeParameterBounds);
            if (memberType == null || isJavaLangObjectType(memberType)) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            if (selected == null) {
                selected = memberType;
                continue;
            }
            if (isStructuralSlimeAstMapType(selected) && isStructuralSlimeAstMapType(memberType)) {
                selected = QinIrTypeRef.classType("java.util.Map");
                continue;
            }
            if (!selected.equals(memberType)) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
        }
        if (selected == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return nullable ? boxForObjectStorage(selected) : selected;
    }

    private boolean isNullishTypeNode(Object typeAst) {
        if (typeAst instanceof TSKeywordType keywordType) {
            return "null".equals(keywordType.keyword()) || "undefined".equals(keywordType.keyword());
        }
        String nodeName = QinSlimeFrontendAdapter.simpleName(typeAst);
        String discriminator = typeNodeDiscriminator(typeAst);
        return containsNullishTypeName(nodeName) || containsNullishTypeName(discriminator);
    }

    private boolean containsNullishTypeName(String value) {
        return value != null
                && (value.contains("Null")
                || value.contains("Undefined")
                || value.contains("Void"));
    }

    private Object typeNodePropertyOrNull(Object typeAst, String propertyName) {
        if (typeAst == null || propertyName == null || propertyName.isBlank()) {
            return null;
        }
        if (typeAst instanceof Map<?, ?> map) {
            return map.get(propertyName);
        }
        try {
            return QinSlimeFrontendAdapter.invokeByName(typeAst, propertyName);
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private String typeNodeDiscriminator(Object typeAst) {
        Object type = typeNodePropertyOrNull(typeAst, "type");
        return type == null ? null : String.valueOf(type);
    }

    private boolean isStructuralSlimeAstTypeName(String typeName) {
        if (typeName == null || !typeName.startsWith("Slime")) {
            return false;
        }
        if (typeName.startsWith("SlimeJavascript")) {
            return true;
        }
        return !"SlimeAstTypeName".equals(typeName)
                && !typeName.contains("CreateUtils")
                && !typeName.contains("CreateFactory")
                && !typeName.contains("CstToAst")
                && !typeName.contains("Parser")
                && !typeName.contains("TokenConsumer");
    }

    private boolean isGeneratedFlattenedDeclarationTypeName(String typeName) {
        return typeName != null
                && typeName.startsWith("com_")
                && typeName.indexOf('_', "com_".length()) > "com_".length();
    }

    private QinIrTypeRef structuralSlimeAstType(String typeName) {
        return QinIrTypeRef.classType(
                "java.util.Map",
                List.of(QinIrTypeRef.classType(STRUCTURAL_SLIME_AST_TYPE_PREFIX + typeName)));
    }

    private boolean isStructuralSlimeAstMapType(QinIrTypeRef type) {
        return structuralSlimeAstTypeName(type) != null;
    }

    private String structuralSlimeAstTypeName(QinIrTypeRef type) {
        if (type == null
                || type.kind() != QinIrTypeKind.CLASS
                || !"java.util.Map".equals(type.binaryName())
                || type.typeArguments() == null
                || type.typeArguments().isEmpty()) {
            return null;
        }
        QinIrTypeRef marker = type.typeArguments().get(0);
        if (marker.binaryName() == null
                || !marker.binaryName().startsWith(STRUCTURAL_SLIME_AST_TYPE_PREFIX)) {
            return null;
        }
        return marker.binaryName().substring(STRUCTURAL_SLIME_AST_TYPE_PREFIX.length());
    }

    private String typeReferenceName(Object typeNameAst) {
        String nodeType = QinSlimeFrontendAdapter.simpleName(typeNameAst);
        if (typeNameAst instanceof Identifier identifier) {
            return identifier.name();
        }
        if ("TSQualifiedName".equals(nodeType)) {
            String left = typeReferenceName(QinSlimeFrontendAdapter.invokeByName(typeNameAst, "left"));
            String right = typeReferenceName(QinSlimeFrontendAdapter.invokeByName(typeNameAst, "right"));
            if (left == null || left.isBlank() || right == null || right.isBlank()) {
                return null;
            }
            return left + "." + right;
        }
        return null;
    }

    private int typeAnnotationArraySuffixDimensions(TSTypeAnnotation annotation, AstNode typeAst) {
        return Math.max(
                sourceLocationArraySuffixDimensions(annotation.location()),
                sourceLocationArraySuffixDimensions(typeAst == null ? null : typeAst.location()));
    }

    private boolean typeAnnotationHasNullableUnion(TSTypeAnnotation annotation, AstNode typeAst) {
        return sourceLocationHasNullableUnion(annotation.location())
                || sourceLocationHasNullableUnion(typeAst == null ? null : typeAst.location());
    }

    private boolean sourceLocationHasNullableUnion(com.slime.ast.SourceLocation location) {
        if (location == null) {
            return false;
        }
        if (containsNullableUnion(location.value())) {
            return true;
        }
        String sourceText = adapter.currentSourceText;
        if (sourceText == null || sourceText.isBlank() || location.start() == null || location.end() == null) {
            return false;
        }
        int start = Math.max(0, Math.min(location.start().index(), sourceText.length()));
        int scanEnd = Math.max(start, Math.min(location.end().index() + 80, sourceText.length()));
        for (int i = location.end().index(); i < scanEnd; i++) {
            char ch = sourceText.charAt(i);
            if (ch == '\n' || ch == '\r' || ch == '=' || ch == '{') {
                scanEnd = i;
                break;
            }
        }
        return containsNullableUnion(sourceText.substring(start, scanEnd));
    }

    private boolean containsNullableUnion(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        String compact = text.replaceAll("\\s+", "");
        return compact.contains("|null")
                || compact.contains("null|")
                || compact.contains("|undefined")
                || compact.contains("undefined|");
    }

    private int sourceLocationArraySuffixDimensions(com.slime.ast.SourceLocation location) {
        if (location == null) {
            return 0;
        }
        if (location.value() != null && location.value().trim().endsWith("[]")) {
            return trailingArraySuffixDimensions(location.value());
        }
        String sourceText = adapter.currentSourceText;
        if (sourceText == null || sourceText.isBlank() || location.start() == null || location.end() == null) {
            return 0;
        }
        int start = Math.max(0, Math.min(location.start().index(), sourceText.length()));
        int end = Math.max(start, Math.min(location.end().index(), sourceText.length()));
        int scanEnd = end;
        while (scanEnd + 2 <= sourceText.length()
                && sourceText.substring(scanEnd, scanEnd + 2).equals("[]")) {
            scanEnd += 2;
        }
        return trailingArraySuffixDimensions(sourceText.substring(start, scanEnd));
    }

    private int trailingArraySuffixDimensions(String text) {
        if (text == null) {
            return 0;
        }
        String trimmed = text.trim();
        int dimensions = 0;
        while (trimmed.endsWith("[]")) {
            dimensions++;
            trimmed = trimmed.substring(0, trimmed.length() - 2).trim();
        }
        return dimensions;
    }

    private List<QinIrExpression> lowerExplicitSuperArguments(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (function == null || function.body() == null || function.body().body() == null
                || function.body().body().isEmpty()) {
            return List.of();
        }
        Statement firstStatement = function.body().body().get(0);
        if (!(firstStatement instanceof ExpressionStatement expressionStatement)
                || !(expressionStatement.expression() instanceof CallExpression callExpression)
                || !"Super".equals(QinSlimeFrontendAdapter.simpleName(callExpression.callee()))) {
            return List.of();
        }
        List<QinIrExpression> arguments = new ArrayList<>();
        for (Expression argument : callExpression.arguments()) {
            arguments.add(lowerDeclarationExpression(argument, javaImportLookup, classContext, Map.of()));
        }
        return List.copyOf(arguments);
    }

    private boolean hasLeadingSuperCall(FunctionExpression function) {
        if (function == null || function.body() == null || function.body().body() == null
                || function.body().body().isEmpty()) {
            return false;
        }
        Statement firstStatement = function.body().body().get(0);
        return firstStatement instanceof ExpressionStatement expressionStatement
                && expressionStatement.expression() instanceof CallExpression callExpression
                && "Super".equals(QinSlimeFrontendAdapter.simpleName(callExpression.callee()));
    }

    private Map<String, ConstructorSuperCall> collectGeneratedHelperSuperCalls(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (classDeclaration == null || classDeclaration.body() == null || classDeclaration.body().body() == null) {
            return Map.of();
        }
        Map<String, ConstructorSuperCall> result = new LinkedHashMap<>();
        for (AstNode member : classDeclaration.body().body()) {
            if (!(member instanceof MethodDefinition methodDefinition) || !isConstructorMethod(methodDefinition)) {
                continue;
            }
            FunctionExpression function = methodDefinition.value();
            if (function == null || function.body() == null || function.body().body() == null) {
                continue;
            }
            collectGeneratedHelperSuperCallsFromStatements(
                    function.body().body(),
                    javaImportLookup,
                    classContext,
                    result);
        }
        return result.isEmpty() ? Map.of() : Map.copyOf(result);
    }

    private void collectGeneratedHelperSuperCallsFromStatements(
            List<? extends Statement> statements,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, ConstructorSuperCall> result) {
        if (statements == null || statements.isEmpty()) {
            return;
        }
        collectGeneratedHelperSuperCallFromLinearStatements(statements, javaImportLookup, classContext, result);
        for (Statement statement : statements) {
            if (statement instanceof IfStatement ifStatement) {
                collectGeneratedHelperSuperCallsFromBranch(ifStatement.consequent(), javaImportLookup, classContext, result);
                collectGeneratedHelperSuperCallsFromBranch(ifStatement.alternate(), javaImportLookup, classContext, result);
            } else if (statement instanceof BlockStatement blockStatement) {
                collectGeneratedHelperSuperCallsFromStatements(
                        blockStatement.body(),
                        javaImportLookup,
                        classContext,
                        result);
            }
        }
    }

    private void collectGeneratedHelperSuperCallsFromBranch(
            Statement statement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, ConstructorSuperCall> result) {
        if (statement instanceof BlockStatement blockStatement) {
            collectGeneratedHelperSuperCallsFromStatements(blockStatement.body(), javaImportLookup, classContext, result);
            return;
        }
        if (statement != null) {
            collectGeneratedHelperSuperCallsFromStatements(List.of(statement), javaImportLookup, classContext, result);
        }
    }

    private void collectGeneratedHelperSuperCallFromLinearStatements(
            List<? extends Statement> statements,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, ConstructorSuperCall> result) {
        List<QinIrExpression> superArguments = null;
        for (Statement statement : statements) {
            if (!(statement instanceof ExpressionStatement expressionStatement)) {
                continue;
            }
            Expression expression = expressionStatement.expression();
            if (expression instanceof CallExpression callExpression
                    && "Super".equals(QinSlimeFrontendAdapter.simpleName(callExpression.callee()))) {
                List<QinIrExpression> lowered = new ArrayList<>();
                for (Expression argument : callExpression.arguments()) {
                    lowered.add(lowerDeclarationExpression(argument, javaImportLookup, classContext, Map.of()));
                }
                superArguments = List.copyOf(lowered);
                continue;
            }
            String helperName = generatedConstructorHelperCallName(expression);
            if (helperName != null && superArguments != null) {
                result.put(helperName, new ConstructorSuperCall(superArguments, true));
            }
        }
    }

    private String generatedConstructorHelperCallName(Expression expression) {
        if (!(expression instanceof CallExpression callExpression)
                || !(callExpression.callee() instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof ThisExpression)
                || memberExpression.computed()) {
            return null;
        }
        String methodName = adapter.extractMemberPropertyName(memberExpression.property());
        return methodName != null && methodName.startsWith("__qin_constructor_") ? methodName : null;
    }

    private QinIrMethodDeclaration applyGeneratedHelperSuperCall(
            QinIrMethodDeclaration method,
            ConstructorSuperCall superCall) {
        if (method == null || superCall == null) {
            return method;
        }
        return new QinIrMethodDeclaration(
                method.name(),
                method.returnType(),
                method.parameters(),
                method.annotations(),
                method.returnExpression(),
                method.bodyStatements(),
                superCall.arguments(),
                superCall.explicit(),
                method.runtimeFunctionDefinition(),
                method.staticMethod(),
                method.abstractMethod());
    }

    private List<? extends Statement> constructorBodyWithoutLeadingSuper(FunctionExpression function) {
        if (function == null || function.body() == null || function.body().body() == null
                || function.body().body().isEmpty()) {
            return List.of();
        }
        List<? extends Statement> statements = function.body().body();
        Statement firstStatement = statements.get(0);
        if (firstStatement instanceof ExpressionStatement expressionStatement
                && expressionStatement.expression() instanceof CallExpression callExpression
                && "Super".equals(QinSlimeFrontendAdapter.simpleName(callExpression.callee()))) {
            return statements.subList(1, statements.size());
        }
        return statements;
    }

    private List<QinIrStatement> lowerMethodBodyStatementsOrEmpty(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> parameterLocals) {
        if (function == null || function.body() == null || function.body().body() == null) {
            return List.of();
        }
        if (!containsStatementBodySignal(function.body().body())) {
            return List.of();
        }
        return lowerDeclarationStatements(function.body().body(), javaImportLookup, classContext, parameterLocals);
    }

    private QinIrExpression singleReturnExpressionOrNull(List<QinIrStatement> bodyStatements) {
        if (bodyStatements == null
                || bodyStatements.size() != 1
                || !(bodyStatements.get(0) instanceof QinIrReturnStatement returnStatement)) {
            return null;
        }
        return returnStatement.value();
    }

    private boolean containsStatementBodySignal(List<? extends Statement> statements) {
        if (statements == null || statements.isEmpty()) {
            return false;
        }
        for (Statement statement : statements) {
            if (statement instanceof ReturnStatement
                    || statement instanceof ThrowStatement
                    || statement instanceof TryStatement
                    || statement instanceof SwitchStatement
                    || statement instanceof ForStatement
                    || "ForOfStatement".equals(QinSlimeFrontendAdapter.simpleName(statement))
                    || statement instanceof DoWhileStatement
                    || statement instanceof BreakStatement
                    || statement instanceof ContinueStatement
                    || statement instanceof WhileStatement) {
                return true;
            }
            if (statement instanceof ExpressionStatement expressionStatement
                    && expressionStatement.expression() instanceof AssignmentExpression) {
                return true;
            }
            if (statement instanceof ExpressionStatement expressionStatement
                    && isGeneratedConstructorDelegationIife(expressionStatement.expression())) {
                return true;
            }
            if (statement instanceof IfStatement ifStatement
                    && (containsStatementBodySignal(statementList(ifStatement.consequent()))
                    || containsStatementBodySignal(statementList(ifStatement.alternate())))) {
                return true;
            }
            if (statement instanceof BlockStatement blockStatement
                    && containsStatementBodySignal(blockStatement.body())) {
                return true;
            }
        }
        return false;
    }

    private boolean isGeneratedConstructorDelegationIife(Expression expression) {
        if (!(expression instanceof CallExpression callExpression)
                || callExpression.arguments() == null
                || callExpression.arguments().isEmpty()) {
            return false;
        }
        Object callee = unwrapParenthesized(callExpression.callee());
        if (!(callee instanceof ArrowFunctionExpression arrowFunction)) {
            return false;
        }
        String restName = singleRestParameterName(arrowFunction);
        if (restName == null) {
            return false;
        }
        return !generatedConstructorDelegateReturnCallsFromBody(arrowFunction.body(), restName).isEmpty();
    }

    private List<QinIrStatement> lowerDeclarationStatements(
            List<? extends Statement> statements,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (statements == null || statements.isEmpty()) {
            return List.of();
        }
        Map<String, QinIrTypeRef> previousValueTypes = currentDeclarationValueTypes;
        currentDeclarationValueTypes = new LinkedHashMap<>(currentDeclarationValueTypes);
        Map<String, QinIrExpression> scopedLocals = new LinkedHashMap<>(locals);
        try {
            List<QinIrStatement> lowered = new ArrayList<>();
            for (Statement statement : statements) {
                if (statement instanceof EmptyStatement) {
                    continue;
                }
                if (statement instanceof BlockStatement blockStatement) {
                    lowered.addAll(lowerDeclarationStatements(
                            blockStatement.body(),
                            javaImportLookup,
                            classContext,
                            new LinkedHashMap<>(scopedLocals)));
                    continue;
                }
                if (statement instanceof VariableDeclaration variableDeclaration) {
                    List<QinIrLocalDeclarationStatement> localDeclarations = lowerDeclarationLocalDeclarationStatements(
                            variableDeclaration,
                            javaImportLookup,
                            classContext,
                            scopedLocals);
                    if (localDeclarations.isEmpty()) {
                        throw qjsError("QJS2023", "Unsupported declaration statement-body local variable");
                    }
                    lowered.addAll(localDeclarations);
                    continue;
                }
                if (statement instanceof ReturnStatement returnStatement) {
                    List<QinIrStatement> inlinedStaticIife = lowerStaticZeroArgumentIifeReturnStatementsOrNull(
                            returnStatement.argument(),
                            javaImportLookup,
                            classContext,
                            scopedLocals);
                    if (inlinedStaticIife != null) {
                        lowered.addAll(inlinedStaticIife);
                        continue;
                    }
                    lowered.add(new QinIrReturnStatement(returnStatement.argument() == null
                            ? new QinIrNullLiteral()
                            : lowerDeclarationExpression(returnStatement.argument(), javaImportLookup, classContext, scopedLocals)));
                    continue;
                }
                if (statement instanceof ExpressionStatement expressionStatement) {
                    lowered.add(new QinIrStatementExpression(lowerDeclarationStatementExpression(
                            expressionStatement.expression(),
                            javaImportLookup,
                            classContext,
                            scopedLocals)));
                    continue;
                }
                if (statement instanceof ThrowStatement throwStatement) {
                    lowered.add(new QinIrThrowStatement(lowerDeclarationExpression(
                            throwStatement.argument(),
                            javaImportLookup,
                            classContext,
                            scopedLocals)));
                    continue;
                }
                if (statement instanceof IfStatement ifStatement) {
                    QinIrExpression test = lowerDeclarationExpression(
                            ifStatement.test(),
                            javaImportLookup,
                            classContext,
                            scopedLocals);
                    lowered.add(new QinIrIfStatement(
                            test,
                            lowerDeclarationStatementsWithTestNarrowing(
                                    statementList(ifStatement.consequent()),
                                    test,
                                    javaImportLookup,
                                    classContext,
                                    new LinkedHashMap<>(scopedLocals)),
                            lowerDeclarationStatements(
                                    statementList(ifStatement.alternate()),
                                    javaImportLookup,
                                    classContext,
                                    new LinkedHashMap<>(scopedLocals))));
                    continue;
                }
                if (statement instanceof TryStatement tryStatement) {
                    List<QinIrCatchClause> catchClauses = new ArrayList<>();
                    CatchClause handler = tryStatement.handler();
                    if (handler != null) {
                        String parameterName = handler.param() instanceof Identifier identifier
                                ? identifier.name()
                                : "__qin_error";
                        catchClauses.add(new QinIrCatchClause(
                                parameterName,
                                QinIrTypeRef.classType("java.lang.Throwable"),
                                lowerDeclarationStatements(
                                        statementList(handler.body()),
                                        javaImportLookup,
                                        classContext,
                                        new LinkedHashMap<>(scopedLocals))));
                    }
                    lowered.add(new QinIrTryStatement(
                            lowerDeclarationStatements(
                                    statementList(tryStatement.block()),
                                    javaImportLookup,
                                    classContext,
                                    new LinkedHashMap<>(scopedLocals)),
                            catchClauses,
                            lowerDeclarationStatements(
                                    statementList(tryStatement.finalizer()),
                                    javaImportLookup,
                                    classContext,
                                    new LinkedHashMap<>(scopedLocals))));
                    continue;
                }
                if (statement instanceof SwitchStatement switchStatement) {
                    lowered.add(lowerDeclarationSwitchStatement(
                            switchStatement,
                            javaImportLookup,
                            classContext,
                            scopedLocals));
                    continue;
                }
                if (statement instanceof ForStatement forStatement) {
                    lowered.add(lowerDeclarationForStatement(
                            forStatement,
                            javaImportLookup,
                            classContext,
                            scopedLocals));
                    continue;
                }
                if ("ForOfStatement".equals(QinSlimeFrontendAdapter.simpleName(statement))) {
                    lowered.add(lowerDeclarationForOfStatement(
                            statement,
                            javaImportLookup,
                            classContext,
                            scopedLocals));
                    continue;
                }
                if (statement instanceof DoWhileStatement doWhileStatement) {
                    lowered.add(new QinIrDoWhileStatementNode(
                            lowerDeclarationStatements(
                                    statementList(doWhileStatement.body()),
                                    javaImportLookup,
                                    classContext,
                                    new LinkedHashMap<>(scopedLocals)),
                            lowerDeclarationExpression(doWhileStatement.test(), javaImportLookup, classContext, scopedLocals)));
                    continue;
                }
                if (statement instanceof WhileStatement whileStatement) {
                    lowered.add(new QinIrWhileStatementNode(
                            lowerDeclarationExpression(whileStatement.test(), javaImportLookup, classContext, scopedLocals),
                            lowerDeclarationStatements(
                                    statementList(whileStatement.body()),
                                    javaImportLookup,
                                    classContext,
                                    new LinkedHashMap<>(scopedLocals))));
                    continue;
                }
                if (statement instanceof BreakStatement breakStatement) {
                    lowered.add(new QinIrBreakStatement(lowerOptionalLabel(breakStatement.label())));
                    continue;
                }
                if (statement instanceof ContinueStatement continueStatement) {
                    lowered.add(new QinIrContinueStatement(lowerOptionalLabel(continueStatement.label())));
                    continue;
                }
                throw qjsError("QJS2024", "Unsupported declaration statement body node: "
                        + QinSlimeFrontendAdapter.simpleName(statement));
            }
            return List.copyOf(lowered);
        } finally {
            currentDeclarationValueTypes = previousValueTypes;
        }
    }

    private List<QinIrStatement> lowerStaticZeroArgumentIifeReturnStatementsOrNull(
            Object returnArgument,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object argument = unwrapParenthesized(returnArgument);
        if (!(argument instanceof CallExpression callExpression)
                || callExpression.arguments() == null
                || !callExpression.arguments().isEmpty()) {
            return null;
        }
        Object function = unwrapParenthesized(callExpression.callee());
        if (!isZeroArgumentArrowFunction(function)) {
            return null;
        }
        Object body = function instanceof ArrowFunctionExpression arrowFunction
                ? arrowFunction.body()
                : QinSlimeFrontendAdapter.invokeByName(function, "body");
        List<? extends Statement> bodyStatements;
        if (body instanceof BlockStatement blockStatement) {
            bodyStatements = blockStatement.body();
        } else if ("BlockStatement".equals(QinSlimeFrontendAdapter.simpleName(body))) {
            bodyStatements = asStatementList(
                    QinSlimeFrontendAdapter.invokeByName(body, "body"),
                    "ArrowFunctionExpression.body.body");
        } else {
            return null;
        }
        List<QinIrStatement> lowered = lowerDeclarationStatements(
                bodyStatements,
                javaImportLookup,
                classContext,
                new LinkedHashMap<>(locals));
        if (lowered.isEmpty() || !staticIifeStatementListCanReturnValue(lowered)) {
            List<QinIrStatement> withDefaultReturn = new ArrayList<>(lowered);
            withDefaultReturn.add(new QinIrReturnStatement(new QinIrNullLiteral()));
            return List.copyOf(withDefaultReturn);
        }
        return lowered;
    }

    private boolean staticIifeStatementListCanReturnValue(List<QinIrStatement> statements) {
        if (statements == null || statements.isEmpty()) {
            return false;
        }
        for (QinIrStatement statement : statements) {
            if (statement instanceof QinIrReturnStatement) {
                return true;
            }
            if (statement instanceof QinIrIfStatement ifStatement
                    && (staticIifeStatementListCanReturnValue(ifStatement.consequent())
                    || staticIifeStatementListCanReturnValue(ifStatement.alternate()))) {
                return true;
            }
            if (statement instanceof QinIrSwitchStatement switchStatement) {
                for (QinIrSwitchCase switchCase : switchStatement.cases()) {
                    if (staticIifeStatementListCanReturnValue(switchCase.consequent())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<QinIrStatement> lowerDeclarationStatementsWithTestNarrowing(
            List<? extends Statement> statements,
            QinIrExpression testExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Map<String, QinIrTypeRef> narrowedTypes =
                declarationBranchNarrowedValueTypes(testExpression);
        if (narrowedTypes.isEmpty()) {
            return lowerDeclarationStatements(statements, javaImportLookup, classContext, locals);
        }
        Map<String, QinIrTypeRef> previousValueTypes = currentDeclarationValueTypes;
        currentDeclarationValueTypes = new LinkedHashMap<>(currentDeclarationValueTypes);
        currentDeclarationValueTypes.putAll(narrowedTypes);
        try {
            return lowerDeclarationStatements(statements, javaImportLookup, classContext, locals);
        } finally {
            currentDeclarationValueTypes = previousValueTypes;
        }
    }

    private QinIrSwitchStatement lowerDeclarationSwitchStatement(
            SwitchStatement switchStatement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression discriminant = lowerDeclarationExpression(
                switchStatement.discriminant(),
                javaImportLookup,
                classContext,
                locals);
        List<QinIrSwitchCase> cases = new ArrayList<>();
        for (SwitchCase switchCase : switchStatement.cases()) {
            QinIrExpression test = switchCase.test() == null
                    ? null
                    : lowerDeclarationExpression(switchCase.test(), javaImportLookup, classContext, locals);
            cases.add(new QinIrSwitchCase(
                    test,
                    lowerDeclarationStatements(
                            switchCase.consequent(),
                            javaImportLookup,
                            classContext,
                            new LinkedHashMap<>(locals))));
        }
        return new QinIrSwitchStatement(discriminant, cases);
    }

    private QinIrForStatement lowerDeclarationForStatement(
            ForStatement forStatement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Map<String, QinIrExpression> forLocals = new LinkedHashMap<>(locals);
        List<QinIrLocalVariableDeclaration> initializerDeclarations = new ArrayList<>();
        List<QinIrExpression> initializerExpressions = new ArrayList<>();
        AstNode init = forStatement.init();
        if (init instanceof VariableDeclaration variableDeclaration) {
            initializerDeclarations.addAll(lowerDeclarationForInitializerDeclarations(
                    variableDeclaration,
                    javaImportLookup,
                    classContext,
                    forLocals));
        } else if (init instanceof Expression expression) {
            initializerExpressions.add(lowerDeclarationExpression(
                    expression,
                    javaImportLookup,
                    classContext,
                    forLocals));
        } else if (init != null) {
            throw qjsError("QJS2025", "Unsupported declaration for initializer: "
                    + QinSlimeFrontendAdapter.simpleName(init));
        }

        QinIrExpression test = forStatement.test() == null
                ? new QinIrBooleanLiteral(true)
                : lowerDeclarationExpression(forStatement.test(), javaImportLookup, classContext, forLocals);
        List<QinIrExpression> updateExpressions = forStatement.update() == null
                ? List.of()
                : List.of(lowerDeclarationExpression(
                forStatement.update(),
                javaImportLookup,
                classContext,
                forLocals));
        return new QinIrForStatement(
                initializerDeclarations,
                initializerExpressions,
                test,
                updateExpressions,
                lowerDeclarationStatements(
                        statementList(forStatement.body()),
                        javaImportLookup,
                        classContext,
                        new LinkedHashMap<>(forLocals)));
    }

    private QinIrForEachStatement lowerDeclarationForOfStatement(
            Object forOfStatement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object left = QinSlimeFrontendAdapter.invokeByName(forOfStatement, "left");
        AstNode bindingPattern = forOfBindingPatternOrNull(left);
        if (!isSupportedForOfBindingPattern(bindingPattern)) {
            throw qjsError("QJS2026", "Declaration for...of supports only identifier/object/array bindings");
        }
        Object right = QinSlimeFrontendAdapter.invokeByName(forOfStatement, "right");
        if (!(right instanceof Expression rightExpression)) {
            throw qjsError("QJS2026", "Declaration for...of iterable must be an expression");
        }
        Object body = QinSlimeFrontendAdapter.invokeByName(forOfStatement, "body");
        Map<String, QinIrExpression> forLocals = new LinkedHashMap<>(locals);
        String itemName = identifierBindingNameOrNull(bindingPattern);
        QinIrTypeRef itemType = inferForOfItemType(rightExpression, classContext);
        if (itemName == null) {
            itemName = "__qin_forof_item_" + syntheticForOfItemCounter++;
            lowerBindingPatternLocals(bindingPattern, new QinIrIdentifierReference(itemName), forLocals);
        } else {
            forLocals.put(itemName, new QinIrIdentifierReference(itemName));
        }
        return new QinIrForEachStatement(
                itemName,
                lowerDeclarationExpression(rightExpression, javaImportLookup, classContext, locals),
                itemType,
                body instanceof Statement bodyStatement
                        ? lowerDeclarationStatements(
                                statementList(bodyStatement),
                                javaImportLookup,
                                classContext,
                                forLocals)
                                : List.of());
    }

    private QinIrTypeRef inferForOfItemType(Expression iterableExpression, DeclarationClassContext classContext) {
        QinIrTypeRef valueType = inferForOfIterableType(iterableExpression, classContext);
        if (isObjectArrayType(valueType)) {
            return boxForObjectStorage(valueType.typeArguments().isEmpty()
                    ? QinIrTypeRef.classType("java.lang.Object")
                    : valueType.typeArguments().get(0));
        }
        if (isIterableType(valueType)) {
            return boxForObjectStorage(valueType.typeArguments().isEmpty()
                    ? QinIrTypeRef.classType("java.lang.Object")
                    : valueType.typeArguments().get(0));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef inferForOfIterableType(Object iterableExpression, DeclarationClassContext classContext) {
        if (iterableExpression instanceof ParenthesizedExpression parenthesizedExpression) {
            return inferForOfIterableType(parenthesizedExpression.expression(), classContext);
        }
        String nodeType = QinSlimeFrontendAdapter.simpleName(iterableExpression);
        if ("ParenthesizedExpression".equals(nodeType)
                || "TSAsExpression".equals(nodeType)
                || "TSSatisfiesExpression".equals(nodeType)
                || "TSNonNullExpression".equals(nodeType)) {
            return inferForOfIterableType(
                    QinSlimeFrontendAdapter.invokeByName(iterableExpression, "expression"),
                    classContext);
        }
        if (iterableExpression instanceof Identifier identifier) {
            QinIrTypeRef valueType = currentDeclarationValueTypes.get(identifier.name());
            return valueType == null ? QinIrTypeRef.classType("java.lang.Object") : valueType;
        }
        if (iterableExpression instanceof ThisExpression || "ThisExpression".equals(nodeType)) {
            return QinIrTypeRef.classType(classContext.className());
        }
        if (iterableExpression instanceof MemberExpression || "MemberExpression".equals(nodeType)) {
            Object objectAst = QinSlimeFrontendAdapter.invokeByName(iterableExpression, "object");
            Object propertyAst = QinSlimeFrontendAdapter.invokeByName(iterableExpression, "property");
            if (Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(iterableExpression, "computed"))) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            QinIrTypeRef ownerType = inferForOfIterableType(objectAst, classContext);
            return resolveDeclarationPropertyType(ownerType, adapter.extractMemberPropertyName(propertyAst), classContext);
        }
        if (iterableExpression instanceof CallExpression || "CallExpression".equals(nodeType)) {
            Object calleeAst = QinSlimeFrontendAdapter.invokeByName(iterableExpression, "callee");
            if (calleeAst instanceof MemberExpression || "MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(calleeAst))) {
                Object objectAst = QinSlimeFrontendAdapter.invokeByName(calleeAst, "object");
                Object propertyAst = QinSlimeFrontendAdapter.invokeByName(calleeAst, "property");
                if (Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(calleeAst, "computed"))) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                List<?> arguments = QinSlimeFrontendAdapter.asListStatic(
                        QinSlimeFrontendAdapter.invokeByName(iterableExpression, "arguments"),
                        "CallExpression.arguments");
                QinIrTypeRef ownerType = inferForOfIterableType(objectAst, classContext);
                return resolveDeclarationMethodReturnType(
                        ownerType,
                        adapter.extractMemberPropertyName(propertyAst),
                        arguments.size(),
                        classContext);
            }
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef boxForObjectStorage(QinIrTypeRef type) {
        if (type == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return switch (type.kind()) {
            case BOOLEAN -> QinIrTypeRef.classType("java.lang.Boolean");
            case INT -> QinIrTypeRef.classType("java.lang.Integer");
            case DOUBLE -> QinIrTypeRef.classType("java.lang.Double");
            default -> type;
        };
    }

    private AstNode forOfBindingPatternOrNull(Object left) {
        if (left instanceof AstNode astNode && !(left instanceof VariableDeclaration)) {
            return astNode;
        }
        if ("VariableDeclaration".equals(QinSlimeFrontendAdapter.simpleName(left))) {
            List<?> declarations = QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(left, "declarations"),
                    "ForOfStatement.left.declarations");
            if (declarations.size() != 1) {
                return null;
            }
            Object declarator = declarations.get(0);
            Object id = QinSlimeFrontendAdapter.invokeByName(declarator, "id");
            return id instanceof AstNode astNode ? astNode : null;
        }
        return null;
    }

    private String identifierBindingNameOrNull(AstNode pattern) {
        if (!(pattern instanceof Identifier identifier)
                || identifier.name() == null
                || identifier.name().isBlank()) {
            return null;
        }
        return identifier.name();
    }

    private boolean isSupportedForOfBindingPattern(AstNode pattern) {
        if (pattern instanceof Identifier identifier) {
            return identifier.name() != null && !identifier.name().isBlank();
        }
        if (pattern instanceof ObjectPattern objectPattern) {
            if (objectPattern.properties() == null) {
                return false;
            }
            for (AstNode propertyNode : objectPattern.properties()) {
                if (propertyNode == null) {
                    continue;
                }
                if (!(propertyNode instanceof Property property)
                        || property.computed()
                        || !isSupportedForOfBindingPattern(property.value() instanceof AstNode astNode ? astNode : null)) {
                    return false;
                }
            }
            return true;
        }
        if (pattern instanceof ArrayPattern arrayPattern) {
            if (arrayPattern.elements() == null) {
                return false;
            }
            for (AstNode element : arrayPattern.elements()) {
                if (element != null && !isSupportedForOfBindingPattern(element)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private List<QinIrLocalVariableDeclaration> lowerDeclarationForInitializerDeclarations(
            VariableDeclaration variableDeclaration,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (variableDeclaration.declarations() == null || variableDeclaration.declarations().isEmpty()) {
            return List.of();
        }
        List<QinIrLocalVariableDeclaration> lowered = new ArrayList<>();
        for (var declarator : variableDeclaration.declarations()) {
            if (declarator == null
                    || declarator.id() == null
                    || !(declarator.id() instanceof Identifier identifier)
                    || identifier.name() == null
                    || identifier.name().isBlank()) {
                return List.of();
            }
            QinIrExpression initializer = declarator.init() == null
                    ? new QinIrNullLiteral()
                    : lowerDeclarationExpression(
                            declarator.init(),
                            javaImportLookup,
                            classContext,
                            locals);
            lowered.add(new QinIrLocalVariableDeclaration(
                    identifier.name(),
                    initializer,
                    lowerLocalDeclaredType(declarator, javaImportLookup, classContext)));
            locals.put(identifier.name(), new QinIrIdentifierReference(identifier.name()));
        }
        return List.copyOf(lowered);
    }

    private String lowerOptionalLabel(Identifier label) {
        return label == null ? null : label.name();
    }

    private List<? extends Statement> statementList(Statement statement) {
        if (statement == null) {
            return List.of();
        }
        if (statement instanceof BlockStatement blockStatement) {
            return blockStatement.body() == null ? List.of() : blockStatement.body();
        }
        return List.of(statement);
    }

    private QinIrExpression lowerMethodReturnExpression(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> parameterLocals) {
        if (function == null || function.body() == null || function.body().body() == null) {
            return new QinIrNullLiteral();
        }
        return lowerDeclarationMethodBody(function.body().body(), javaImportLookup, classContext, parameterLocals);
    }

    private Map<String, QinIrExpression> parameterLocals(List<QinIrParameter> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return Map.of();
        }
        Map<String, QinIrExpression> locals = new LinkedHashMap<>();
        for (QinIrParameter parameter : parameters) {
            if (parameter == null || parameter.name() == null || parameter.name().isBlank()) {
                continue;
            }
            locals.put(parameter.name(), new QinIrIdentifierReference(parameter.name()));
        }
        return locals;
    }

    private Map<String, QinIrTypeRef> declarationValueTypes(List<QinIrParameter> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return Map.of();
        }
        Map<String, QinIrTypeRef> types = new LinkedHashMap<>();
        for (QinIrParameter parameter : parameters) {
            if (parameter == null || parameter.name() == null || parameter.name().isBlank()) {
                continue;
            }
            types.put(parameter.name(), parameter.type());
        }
        return types;
    }

    private QinIrExpression lowerDeclarationMethodBody(
            List<? extends Statement> statements,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (statements == null || statements.isEmpty()) {
            return new QinIrNullLiteral();
        }

        Map<String, QinIrExpression> scopedLocals = new LinkedHashMap<>(locals);
        List<QinIrExpression> leadingExpressions = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            Statement statement = statements.get(i);
            boolean last = i == statements.size() - 1;
            if (statement instanceof VariableDeclaration variableDeclaration) {
                if (!lowerDeclarationLocalVariableDeclarations(variableDeclaration, javaImportLookup, classContext, scopedLocals)) {
                    return new QinIrNullLiteral();
                }
                continue;
            }
            if (statement instanceof ReturnStatement returnStatement) {
                if (!last) {
                    return null;
                }
                QinIrExpression result = returnStatement.argument() == null
                        ? new QinIrNullLiteral()
                        : lowerDeclarationExpression(returnStatement.argument(), javaImportLookup, classContext, scopedLocals);
                return wrapSequenceExpression(leadingExpressions, result);
            }
            if (statement instanceof IfStatement ifStatement) {
                if (last) {
                    QinIrExpression result =
                            lowerDeclarationIfReturnExpression(ifStatement, javaImportLookup, classContext, scopedLocals);
                    return wrapSequenceExpression(leadingExpressions, result);
                }
                QinIrExpression fallthrough = lowerDeclarationMethodBody(
                        statements.subList(i + 1, statements.size()),
                        javaImportLookup,
                        classContext,
                        scopedLocals);
                if (fallthrough == null) {
                    return new QinIrNullLiteral();
                }
                QinIrExpression result = lowerDeclarationEarlyReturnIfExpression(
                        ifStatement,
                        javaImportLookup,
                        classContext,
                        scopedLocals,
                        fallthrough);
                return wrapSequenceExpression(leadingExpressions, result);
            }
            if (statement instanceof SwitchStatement switchStatement) {
                QinIrExpression fallthrough = last
                        ? new QinIrNullLiteral()
                        : lowerDeclarationMethodBody(
                                statements.subList(i + 1, statements.size()),
                                javaImportLookup,
                                classContext,
                                scopedLocals);
                if (fallthrough == null) {
                    return new QinIrNullLiteral();
                }
                QinIrExpression result = lowerDeclarationSwitchReturnExpression(
                        switchStatement,
                        javaImportLookup,
                        classContext,
                        scopedLocals,
                        fallthrough);
                if (result == null) {
                    return new QinIrNullLiteral();
                }
                return wrapSequenceExpression(leadingExpressions, result);
            }
            if (statement instanceof BlockStatement blockStatement) {
                QinIrExpression result = lowerDeclarationMethodBody(
                        blockStatement.body(),
                        javaImportLookup,
                        classContext,
                        new LinkedHashMap<>(scopedLocals));
                if (result == null) {
                    return new QinIrNullLiteral();
                }
                return wrapSequenceExpression(leadingExpressions, result);
            }
            if (statement instanceof ExpressionStatement expressionStatement) {
                QinIrExpression lowered = lowerDeclarationStatementExpression(
                        expressionStatement.expression(),
                        javaImportLookup,
                        classContext,
                        scopedLocals);
                leadingExpressions.add(lowered);
                continue;
            }
            return new QinIrNullLiteral();
        }
        return wrapSequenceExpression(leadingExpressions, new QinIrNullLiteral());
    }

    private QinIrExpression lowerDeclarationSwitchReturnExpression(
            SwitchStatement switchStatement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals,
            QinIrExpression fallthrough) {
        if (switchStatement == null || switchStatement.discriminant() == null || switchStatement.cases() == null) {
            return null;
        }
        QinIrExpression discriminant = lowerDeclarationExpression(
                switchStatement.discriminant(),
                javaImportLookup,
                classContext,
                locals);
        List<SwitchReturnGroup> groups = new ArrayList<>();
        List<QinIrExpression> pendingTests = new ArrayList<>();
        QinIrExpression defaultExpression = fallthrough;
        for (SwitchCase switchCase : switchStatement.cases()) {
            if (switchCase == null) {
                continue;
            }
            boolean isDefault = switchCase.test() == null;
            if (!isDefault) {
                pendingTests.add(lowerDeclarationExpression(
                        switchCase.test(),
                        javaImportLookup,
                        classContext,
                        locals));
            }
            List<Statement> consequent = switchCase.consequent() == null
                    ? List.of()
                    : switchCase.consequent();
            if (isSwitchFallthroughOnlyConsequent(consequent)) {
                continue;
            }
            QinIrExpression consequentExpression = lowerDeclarationMethodBody(
                    consequent,
                    javaImportLookup,
                    classContext,
                    new LinkedHashMap<>(locals));
            if (consequentExpression == null) {
                return null;
            }
            if (!pendingTests.isEmpty()) {
                groups.add(new SwitchReturnGroup(List.copyOf(pendingTests), consequentExpression));
                pendingTests.clear();
            }
            if (isDefault) {
                defaultExpression = consequentExpression;
            }
        }
        QinIrExpression result = defaultExpression;
        for (int i = groups.size() - 1; i >= 0; i--) {
            SwitchReturnGroup group = groups.get(i);
            result = new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_conditional__",
                    List.of(
                            switchReturnGroupCondition(discriminant, group.tests()),
                            group.result(),
                            result));
        }
        return result;
    }

    private QinIrExpression switchReturnGroupCondition(
            QinIrExpression discriminant,
            List<QinIrExpression> tests) {
        QinIrExpression condition = null;
        for (QinIrExpression test : tests) {
            QinIrExpression equality = new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_binary__",
                    List.of(new QinIrStringLiteral("==="), discriminant, test));
            condition = condition == null
                    ? equality
                    : new QinIrBuiltinCallExpression(
                            "Global",
                            "__qin_logical__",
                            List.of(new QinIrStringLiteral("||"), condition, equality));
        }
        return condition == null ? new QinIrNullLiteral() : condition;
    }

    private boolean isSwitchFallthroughOnlyConsequent(List<Statement> consequent) {
        if (consequent == null || consequent.isEmpty()) {
            return true;
        }
        for (Statement statement : consequent) {
            if (statement instanceof EmptyStatement) {
                continue;
            }
            if (statement instanceof BlockStatement blockStatement) {
                List<Statement> body = blockStatement.body();
                if (body == null || isSwitchFallthroughOnlyConsequent(body)) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    private record SwitchReturnGroup(
            List<QinIrExpression> tests,
            QinIrExpression result) {
    }

    private record InstanceofNarrowing(
            String sourceKey,
            QinIrTypeRef targetType) {
    }

    private record AssignmentNarrowing(
            String targetName,
            String sourceKey) {
    }

    private QinIrExpression wrapSequenceExpression(
            List<QinIrExpression> leadingExpressions,
            QinIrExpression result) {
        if (leadingExpressions == null || leadingExpressions.isEmpty()) {
            return result;
        }
        return new QinIrSequenceExpression(leadingExpressions, result);
    }

    private boolean lowerDeclarationLocalVariableDeclarations(
            VariableDeclaration variableDeclaration,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (variableDeclaration.declarations() == null || variableDeclaration.declarations().isEmpty()) {
            return false;
        }
        for (var declarator : variableDeclaration.declarations()) {
            if (declarator == null || declarator.id() == null || declarator.init() == null) {
                return false;
            }
            QinIrExpression initializer = lowerDeclarationExpression(
                    declarator.init(),
                    javaImportLookup,
                    classContext,
                    locals);
            QinIrTypeRef declaredType = lowerLocalDeclaredType(declarator, javaImportLookup, classContext);
            QinIrTypeRef localType = localDeclarationStorageType(declaredType, inferDeclarationReturnType(
                    initializer,
                    List.of(),
                    classContext));
            lowerBindingPatternLocals(declarator.id(), initializer, locals);
            if (declarator.id() instanceof Identifier identifier
                    && identifier.name() != null
                    && !identifier.name().isBlank()) {
                recordCurrentDeclarationValueType(identifier.name(), localType);
            }
        }
        return true;
    }

    private List<QinIrLocalDeclarationStatement> lowerDeclarationLocalDeclarationStatements(
            VariableDeclaration variableDeclaration,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (variableDeclaration.declarations() == null || variableDeclaration.declarations().isEmpty()) {
            return List.of();
        }
        List<QinIrLocalDeclarationStatement> lowered = new ArrayList<>();
        for (var declarator : variableDeclaration.declarations()) {
            if (declarator == null
                    || declarator.id() == null
                    || !(declarator.id() instanceof Identifier identifier)
                    || identifier.name() == null
                    || identifier.name().isBlank()) {
                return List.of();
            }
            QinIrExpression initializer = declarator.init() == null
                    ? new QinIrNullLiteral()
                    : lowerDeclarationExpression(
                            declarator.init(),
                            javaImportLookup,
                            classContext,
                            locals);
            QinIrTypeRef declaredType = lowerLocalDeclaredType(declarator, javaImportLookup, classContext);
            QinIrTypeRef localType = localDeclarationStorageType(declaredType, inferDeclarationReturnType(
                    initializer,
                    List.of(),
                    classContext));
            QinIrLocalDeclarationStatement statement =
                    new QinIrLocalDeclarationStatement(
                            identifier.name(),
                            initializer,
                            declaredType);
            lowered.add(statement);
            recordCurrentDeclarationValueType(identifier.name(), localType);
            locals.put(identifier.name(), new QinIrIdentifierReference(identifier.name()));
        }
        return List.copyOf(lowered);
    }

    private void recordCurrentDeclarationValueType(String name, QinIrTypeRef type) {
        if (name == null || name.isBlank()) {
            return;
        }
        currentDeclarationValueTypes = new LinkedHashMap<>(currentDeclarationValueTypes);
        currentDeclarationValueTypes.put(name, type);
    }

    private QinIrTypeRef lowerLocalDeclaredType(
            Object declarator,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        Object annotation = QinSlimeFrontendAdapter.invokeByName(declarator, "typeAnnotation");
        if (!(annotation instanceof AstNode typeAnnotation)) {
            return null;
        }
        QinIrTypeRef loweredType = lowerParameterType(
                typeAnnotation,
                javaImportLookup,
                classContext == null ? Set.of() : classContext.localDeclarationNames());
        if (!isJavaLangObjectType(loweredType)) {
            return loweredType;
        }
        String localTypeName = typeAnnotationReferenceName(typeAnnotation);
        String resolvedLocalClassName = resolveLocalDeclaredClassTypeName(localTypeName, javaImportLookup, classContext);
        return resolvedLocalClassName == null ? loweredType : QinIrTypeRef.classType(resolvedLocalClassName);
    }

    private boolean isJavaLangObjectType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object".equals(type.binaryName());
    }

    private QinIrTypeRef localDeclarationStorageType(QinIrTypeRef declaredType, QinIrTypeRef initializerType) {
        if (declaredType == null) {
            return initializerType;
        }
        if (isPrimitiveStorageType(declaredType) && isBoxedPrimitiveStorageType(initializerType)) {
            return initializerType;
        }
        if (isJavaLangObjectType(declaredType) && isStaticStructuralInitializerType(initializerType)) {
            return initializerType;
        }
        if (isJavaLangObjectType(declaredType) && isStaticPreciseInitializerType(initializerType)) {
            return initializerType;
        }
        return declaredType;
    }

    private boolean isStaticPreciseInitializerType(QinIrTypeRef type) {
        return type != null
                && !(type.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object".equals(type.binaryName()));
    }

    private boolean isPrimitiveStorageType(QinIrTypeRef type) {
        return type != null
                && (type.kind() == QinIrTypeKind.BOOLEAN
                || type.kind() == QinIrTypeKind.INT
                || type.kind() == QinIrTypeKind.DOUBLE);
    }

    private boolean isBoxedPrimitiveStorageType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && ("java.lang.Boolean".equals(type.binaryName())
                || "java.lang.Integer".equals(type.binaryName())
                || "java.lang.Long".equals(type.binaryName())
                || "java.lang.Double".equals(type.binaryName())
                || "java.lang.Number".equals(type.binaryName()));
    }

    private boolean isStaticStructuralInitializerType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        String binaryName = QinJavaSdkAliasSupport.canonicalBinaryName(type.binaryName());
        return "java.util.Map".equals(binaryName)
                || "java.util.LinkedHashMap".equals(binaryName)
                || "java.util.ArrayList".equals(binaryName)
                || "java.lang.Object[]".equals(binaryName);
    }

    private boolean isIterableType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        return switch (type.binaryName()) {
            case "java.lang.Iterable",
                    "java.util.Collection",
                    "java.util.List",
                    "java.util.ArrayList",
                    "java.util.Set",
                    "java.util.HashSet",
                    "java.util.LinkedHashSet" -> true;
            default -> false;
        };
    }

    private String typeAnnotationReferenceName(AstNode typeAnnotationAst) {
        if (!(typeAnnotationAst instanceof TSTypeAnnotation annotation)
                || !(annotation.typeAnnotation() instanceof TSTypeReference typeReference)) {
            return null;
        }
        return typeReferenceName(typeReference.typeName());
    }

    private String resolveLocalDeclaredClassTypeName(
            String typeName,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (typeName == null || typeName.isBlank()) {
            return null;
        }
        String importedBinaryName = javaImportLookup == null ? null : javaImportLookup.get(typeName);
        if (importedBinaryName != null && !importedBinaryName.isBlank()) {
            return importedBinaryName;
        }
        if (classContext == null) {
            return null;
        }
        String jsDeclarationBinaryName = classContext.jsDeclarationClass(typeName);
        if (jsDeclarationBinaryName != null && !jsDeclarationBinaryName.isBlank()) {
            return jsDeclarationBinaryName;
        }
        if (typeName.equals(classContext.className()) || classContext.isLocalDeclarationName(typeName)) {
            return typeName;
        }
        return null;
    }

    private void lowerBindingPatternLocals(
            AstNode pattern,
            QinIrExpression source,
            Map<String, QinIrExpression> locals) {
        if (pattern instanceof Identifier identifier) {
            if (identifier.name() == null || identifier.name().isBlank()) {
                throw qjsError("QJS2022", "Blank destructuring binding identifier is not supported");
            }
            locals.put(identifier.name(), source);
            return;
        }
        if (pattern instanceof ObjectPattern objectPattern) {
            for (AstNode propertyNode : objectPattern.properties()) {
                if (propertyNode == null) {
                    continue;
                }
                if (!(propertyNode instanceof Property property)) {
                    throw qjsError("QJS2022", "Unsupported object destructuring element: " + propertyNode.getClass().getName());
                }
                if (property.computed()) {
                    throw qjsError("QJS2022", "Computed object destructuring keys are not supported yet");
                }
                String key = adapter.extractPropertyKey(property.key());
                lowerBindingPatternLocals(
                        property.value(),
                        memberGet(source, new QinIrStringLiteral(key)),
                        locals);
            }
            return;
        }
        if (pattern instanceof ArrayPattern arrayPattern) {
            int index = 0;
            for (AstNode element : arrayPattern.elements()) {
                if (element != null) {
                    lowerBindingPatternLocals(
                            element,
                            memberGet(source, new QinIrNumberLiteral(index)),
                            locals);
                }
                index++;
            }
            return;
        }
        if (pattern instanceof AssignmentPattern) {
            throw qjsError("QJS2022", "Default destructuring values are not supported yet");
        }
        if (pattern instanceof RestElement) {
            throw qjsError("QJS2022", "Rest destructuring bindings are not supported yet");
        }
        throw qjsError("QJS2022", "Unsupported destructuring binding pattern: "
                + QinSlimeFrontendAdapter.simpleName(pattern));
    }

    private QinIrExpression lowerDeclarationIfReturnExpression(
            IfStatement ifStatement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (ifStatement.test() == null || ifStatement.consequent() == null || ifStatement.alternate() == null) {
            return new QinIrNullLiteral();
        }
        QinIrExpression test = lowerDeclarationExpression(ifStatement.test(), javaImportLookup, classContext, locals);
        QinIrExpression consequent = extractDeclarationBranchReturnExpressionWithTestNarrowing(
                ifStatement.consequent(),
                test,
                javaImportLookup,
                classContext,
                locals);
        QinIrExpression alternate = extractDeclarationBranchReturnOrFallthroughExpression(
                ifStatement.alternate(),
                javaImportLookup,
                classContext,
                locals,
                new QinIrNullLiteral());
        if (consequent == null || alternate == null) {
            return new QinIrNullLiteral();
        }
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
    }

    private QinIrExpression lowerDeclarationEarlyReturnIfExpression(
            IfStatement ifStatement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals,
            QinIrExpression fallthrough) {
        if (ifStatement.test() == null || ifStatement.consequent() == null) {
            return new QinIrNullLiteral();
        }
        QinIrExpression test = lowerDeclarationExpression(ifStatement.test(), javaImportLookup, classContext, locals);
        QinIrExpression consequent = extractDeclarationBranchReturnExpressionWithTestNarrowing(
                ifStatement.consequent(),
                test,
                javaImportLookup,
                classContext,
                locals);
        if (consequent == null) {
            return new QinIrNullLiteral();
        }
        QinIrExpression alternate = ifStatement.alternate() == null
                ? fallthrough
                : extractDeclarationBranchReturnOrFallthroughExpression(
                        ifStatement.alternate(),
                        javaImportLookup,
                        classContext,
                        locals,
                        fallthrough);
        if (alternate == null) {
            return new QinIrNullLiteral();
        }
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
    }

    private QinIrExpression extractDeclarationBranchReturnOrFallthroughExpression(
            Statement statement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals,
            QinIrExpression fallthrough) {
        if (statement instanceof IfStatement nestedIfStatement) {
            return lowerDeclarationEarlyReturnIfExpression(
                    nestedIfStatement,
                    javaImportLookup,
                    classContext,
                    locals,
                    fallthrough);
        }
        QinIrExpression branchReturn = extractDeclarationBranchReturnExpression(
                statement,
                javaImportLookup,
                classContext,
                locals);
        return branchReturn == null ? fallthrough : branchReturn;
    }

    private QinIrExpression extractDeclarationBranchReturnExpressionWithTestNarrowing(
            Statement statement,
            QinIrExpression testExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Map<String, QinIrTypeRef> narrowedTypes =
                declarationBranchNarrowedValueTypes(testExpression);
        if (narrowedTypes.isEmpty()) {
            return extractDeclarationBranchReturnExpression(statement, javaImportLookup, classContext, locals);
        }
        Map<String, QinIrTypeRef> previousValueTypes = currentDeclarationValueTypes;
        currentDeclarationValueTypes = new LinkedHashMap<>(currentDeclarationValueTypes);
        currentDeclarationValueTypes.putAll(narrowedTypes);
        try {
            return extractDeclarationBranchReturnExpression(statement, javaImportLookup, classContext, locals);
        } finally {
            currentDeclarationValueTypes = previousValueTypes;
        }
    }

    private Map<String, QinIrTypeRef> declarationBranchNarrowedValueTypes(QinIrExpression testExpression) {
        Map<String, QinIrTypeRef> instanceofFacts = new LinkedHashMap<>();
        List<AssignmentNarrowing> assignments = new ArrayList<>();
        collectDeclarationBranchNarrowingFacts(testExpression, instanceofFacts, assignments);
        if (instanceofFacts.isEmpty()) {
            return Map.of();
        }
        Map<String, QinIrTypeRef> narrowed = new LinkedHashMap<>();
        for (Map.Entry<String, QinIrTypeRef> entry : instanceofFacts.entrySet()) {
            if (QinParserRuntimeNames.IDENTIFIER_PATTERN.matcher(entry.getKey()).matches()) {
                narrowed.put(entry.getKey(), entry.getValue());
            }
        }
        for (AssignmentNarrowing assignment : assignments) {
            QinIrTypeRef targetType = instanceofFacts.get(assignment.sourceKey());
            if (targetType != null
                    && assignment.targetName() != null
                    && QinParserRuntimeNames.IDENTIFIER_PATTERN.matcher(assignment.targetName()).matches()) {
                narrowed.put(assignment.targetName(), targetType);
            }
        }
        return narrowed.isEmpty() ? Map.of() : Map.copyOf(narrowed);
    }

    private void collectDeclarationBranchNarrowingFacts(
            QinIrExpression expression,
            Map<String, QinIrTypeRef> instanceofFacts,
            List<AssignmentNarrowing> assignments) {
        if (expression instanceof QinIrShortCircuitExpression shortCircuitExpression) {
            if ("&&".equals(shortCircuitExpression.operator())) {
                collectDeclarationBranchNarrowingFacts(
                        shortCircuitExpression.left(),
                        instanceofFacts,
                        assignments);
                collectDeclarationBranchNarrowingFacts(
                        shortCircuitExpression.right(),
                        instanceofFacts,
                        assignments);
            }
            return;
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            for (QinIrExpression leading : sequenceExpression.leadingExpressions()) {
                collectDeclarationBranchNarrowingFacts(leading, instanceofFacts, assignments);
            }
            collectDeclarationBranchNarrowingFacts(
                    sequenceExpression.resultExpression(),
                    instanceofFacts,
                    assignments);
            return;
        }
        if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            if (!"=".equals(assignmentExpression.operator())) {
                return;
            }
            String targetName = declarationNarrowingSourceKey(assignmentExpression.target());
            String sourceKey = declarationNarrowingSourceKey(assignmentExpression.value());
            if (targetName != null && sourceKey != null) {
                assignments.add(new AssignmentNarrowing(targetName, sourceKey));
            }
            return;
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression
                && "Global".equals(builtinCallExpression.receiverName())
                && "__qin_instanceof__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            String sourceKey = declarationNarrowingSourceKey(builtinCallExpression.arguments().get(0));
            QinIrExpression typeExpression = builtinCallExpression.arguments().get(1);
            if (sourceKey != null && typeExpression instanceof QinIrJavaClassLiteralExpression classLiteral) {
                instanceofFacts.put(sourceKey, QinIrTypeRef.classType(classLiteral.binaryName()));
            }
        }
    }

    private String declarationNarrowingSourceKey(QinIrExpression expression) {
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            return identifierReference.name();
        }
        return null;
    }

    private Map<String, QinIrTypeRef> declarationBranchNarrowedValueTypes(
            Object testAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        Map<String, QinIrTypeRef> instanceofFacts = new LinkedHashMap<>();
        List<AssignmentNarrowing> assignments = new ArrayList<>();
        collectDeclarationBranchNarrowingFacts(
                testAst,
                javaImportLookup,
                classContext,
                instanceofFacts,
                assignments);
        if (instanceofFacts.isEmpty()) {
            return Map.of();
        }
        Map<String, QinIrTypeRef> narrowed = new LinkedHashMap<>();
        for (Map.Entry<String, QinIrTypeRef> entry : instanceofFacts.entrySet()) {
            if (QinParserRuntimeNames.IDENTIFIER_PATTERN.matcher(entry.getKey()).matches()) {
                narrowed.put(entry.getKey(), entry.getValue());
            }
        }
        for (AssignmentNarrowing assignment : assignments) {
            QinIrTypeRef targetType = instanceofFacts.get(assignment.sourceKey());
            if (targetType != null
                    && assignment.targetName() != null
                    && QinParserRuntimeNames.IDENTIFIER_PATTERN.matcher(assignment.targetName()).matches()) {
                narrowed.put(assignment.targetName(), targetType);
            }
        }
        return narrowed.isEmpty() ? Map.of() : Map.copyOf(narrowed);
    }

    private void collectDeclarationBranchNarrowingFacts(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrTypeRef> instanceofFacts,
            List<AssignmentNarrowing> assignments) {
        Object expression = unwrapParenthesized(expressionAst);
        if (expression == null) {
            return;
        }
        if (expression instanceof LogicalExpression logicalExpression) {
            if ("&&".equals(logicalExpression.operator())) {
                collectDeclarationBranchNarrowingFacts(
                        logicalExpression.left(),
                        javaImportLookup,
                        classContext,
                        instanceofFacts,
                        assignments);
                collectDeclarationBranchNarrowingFacts(
                        logicalExpression.right(),
                        javaImportLookup,
                        classContext,
                        instanceofFacts,
                        assignments);
            }
            return;
        }
        if ("LogicalExpression".equals(QinSlimeFrontendAdapter.simpleName(expression))) {
            String operator = QinSlimeFrontendAdapter.asStringStatic(
                    QinSlimeFrontendAdapter.invokeByName(expression, "operator"),
                    "LogicalExpression.operator");
            if ("&&".equals(operator)) {
                collectDeclarationBranchNarrowingFacts(
                        QinSlimeFrontendAdapter.invokeByName(expression, "left"),
                        javaImportLookup,
                        classContext,
                        instanceofFacts,
                        assignments);
                collectDeclarationBranchNarrowingFacts(
                        QinSlimeFrontendAdapter.invokeByName(expression, "right"),
                        javaImportLookup,
                        classContext,
                        instanceofFacts,
                        assignments);
            }
            return;
        }
        if (expression instanceof SequenceExpression sequenceExpression) {
            if (sequenceExpression.expressions() != null) {
                for (Object item : sequenceExpression.expressions()) {
                    collectDeclarationBranchNarrowingFacts(
                            item,
                            javaImportLookup,
                            classContext,
                            instanceofFacts,
                            assignments);
                }
            }
            return;
        }
        if ("SequenceExpression".equals(QinSlimeFrontendAdapter.simpleName(expression))) {
            for (Object item : QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(expression, "expressions"),
                    "SequenceExpression.expressions")) {
                collectDeclarationBranchNarrowingFacts(
                        item,
                        javaImportLookup,
                        classContext,
                        instanceofFacts,
                        assignments);
            }
            return;
        }
        AssignmentNarrowing assignment = declarationAssignmentNarrowingOrNull(expression);
        if (assignment != null) {
            assignments.add(assignment);
            return;
        }
        InstanceofNarrowing narrowing =
                declarationInstanceofNarrowingOrNull(expression, javaImportLookup, classContext);
        if (narrowing != null) {
            instanceofFacts.put(narrowing.sourceKey(), narrowing.targetType());
        }
    }

    private AssignmentNarrowing declarationAssignmentNarrowingOrNull(Object expressionAst) {
        Object expression = unwrapParenthesized(expressionAst);
        if (expression instanceof AssignmentExpression assignmentExpression) {
            if (!"=".equals(assignmentExpression.operator())) {
                return null;
            }
            String targetName = declarationIdentifierName(assignmentExpression.left());
            String sourceKey = declarationNarrowingSourceKey(assignmentExpression.right());
            return targetName == null || sourceKey == null ? null : new AssignmentNarrowing(targetName, sourceKey);
        }
        if (!"AssignmentExpression".equals(QinSlimeFrontendAdapter.simpleName(expression))) {
            return null;
        }
        String operator = QinSlimeFrontendAdapter.asStringStatic(
                QinSlimeFrontendAdapter.invokeByName(expression, "operator"),
                "AssignmentExpression.operator");
        if (!"=".equals(operator)) {
            return null;
        }
        String targetName = declarationIdentifierName(QinSlimeFrontendAdapter.invokeByName(expression, "left"));
        String sourceKey = declarationNarrowingSourceKey(QinSlimeFrontendAdapter.invokeByName(expression, "right"));
        return targetName == null || sourceKey == null ? null : new AssignmentNarrowing(targetName, sourceKey);
    }

    private InstanceofNarrowing declarationInstanceofNarrowingOrNull(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        Object expression = unwrapParenthesized(expressionAst);
        if (expression instanceof CallExpression callExpression
                && "__qin_instanceof__".equals(declarationIdentifierName(callExpression.callee()))
                && callExpression.arguments() != null
                && callExpression.arguments().size() == 2) {
            return declarationInstanceofNarrowingOrNull(
                    callExpression.arguments().get(0),
                    callExpression.arguments().get(1),
                    javaImportLookup,
                    classContext);
        }
        if ("CallExpression".equals(QinSlimeFrontendAdapter.simpleName(expression))
                && "__qin_instanceof__".equals(declarationIdentifierName(
                        QinSlimeFrontendAdapter.invokeByName(expression, "callee")))) {
            List<?> arguments = QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(expression, "arguments"),
                    "CallExpression.arguments");
            if (arguments.size() == 2) {
                return declarationInstanceofNarrowingOrNull(
                        arguments.get(0),
                        arguments.get(1),
                        javaImportLookup,
                        classContext);
            }
        }
        return null;
    }

    private InstanceofNarrowing declarationInstanceofNarrowingOrNull(
            Object valueAst,
            Object typeAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        String sourceKey = declarationNarrowingSourceKey(valueAst);
        if (sourceKey == null) {
            return null;
        }
        QinIrExpression classLiteral = lowerDeclarationExpression(typeAst, javaImportLookup, classContext, Map.of());
        if (!(classLiteral instanceof QinIrJavaClassLiteralExpression javaClassLiteral)
                || javaClassLiteral.binaryName() == null
                || javaClassLiteral.binaryName().isBlank()) {
            return null;
        }
        return new InstanceofNarrowing(sourceKey, QinIrTypeRef.classType(javaClassLiteral.binaryName()));
    }

    private String declarationNarrowingSourceKey(Object expressionAst) {
        Object expression = unwrapParenthesized(expressionAst);
        if (expression instanceof Identifier identifier) {
            return identifier.name();
        }
        if ("Identifier".equals(QinSlimeFrontendAdapter.simpleName(expression))) {
            return QinSlimeFrontendAdapter.extractIdentifierNameStatic(expression, "Identifier");
        }
        return null;
    }

    private QinIrExpression extractDeclarationBranchReturnExpression(
            Statement statement,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (statement instanceof ReturnStatement returnStatement) {
            return returnStatement.argument() == null
                    ? new QinIrNullLiteral()
                    : lowerDeclarationExpression(returnStatement.argument(), javaImportLookup, classContext, locals);
        }
        if (statement instanceof BlockStatement blockStatement) {
            if (blockStatement.body() == null || blockStatement.body().isEmpty()) {
                return null;
            }
            return lowerDeclarationMethodBody(blockStatement.body(), javaImportLookup, classContext, locals);
        }
        return null;
    }

    private QinIrTypeRef inferDeclarationReturnType(
            QinIrExpression returnExpression,
            List<QinIrParameter> parameters,
            DeclarationClassContext classContext) {
        if (returnExpression instanceof QinIrSequenceExpression sequenceExpression) {
            return inferDeclarationReturnType(sequenceExpression.resultExpression(), parameters, classContext);
        }
        if (returnExpression instanceof QinIrStringLiteral) {
            return QinIrTypeRef.stringType();
        }
        if (returnExpression instanceof QinIrBooleanLiteral) {
            return QinIrTypeRef.booleanType();
        }
        if (returnExpression instanceof QinIrNumberLiteral) {
            return QinIrTypeRef.doubleType();
        }
        if (returnExpression instanceof QinIrNullLiteral) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (returnExpression instanceof QinIrThisExpression) {
            return QinIrTypeRef.classType(classContext.className());
        }
        if (returnExpression instanceof QinIrJavaNewExpression javaNewExpression) {
            return QinIrTypeRef.classType(javaNewExpression.ownerBinaryName());
        }
        if (returnExpression instanceof QinIrIdentifierReference identifierReference) {
            QinIrParameter parameter = resolveDeclarationParameter(parameters, identifierReference.name());
            if (parameter != null) {
                return parameter.type();
            }
            QinIrTypeRef localType = currentDeclarationValueTypes.get(identifierReference.name());
            if (localType != null) {
                return localType;
            }
            QinIrFieldDeclaration field = classContext.fields().get(identifierReference.name());
            if (field != null) {
                return field.type();
            }
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (returnExpression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            QinIrTypeRef ownerType = inferDeclarationReturnType(
                    new QinIrIdentifierReference(memberAccessExpression.objectName()),
                    parameters,
                    classContext);
            return resolveDeclarationPropertyType(ownerType, memberAccessExpression.propertyName(), classContext);
        }
        if (returnExpression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            QinIrTypeRef ownerType = inferDeclarationReturnType(
                    propertyAccessExpression.receiver(),
                    parameters,
                    classContext);
            return resolveDeclarationPropertyType(ownerType, propertyAccessExpression.propertyName(), classContext);
        }
        if (returnExpression instanceof QinIrElementAccessExpression) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (returnExpression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            if ("toString".equals(methodCallExpression.methodName())
                    && methodCallExpression.arguments().isEmpty()) {
                return QinIrTypeRef.stringType();
            }
            QinIrTypeRef ownerType = inferDeclarationReturnType(
                    methodCallExpression.receiver(),
                    parameters,
                    classContext);
            return resolveDeclarationMethodReturnType(
                    ownerType,
                    methodCallExpression.methodName(),
                    methodCallExpression.arguments().size(),
                    classContext);
        }
        if (returnExpression instanceof QinIrSuperMethodCallExpression) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (returnExpression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            QinIrTypeRef staticReturnType = inferDeclarationStaticMethodReturnType(staticMethodCallExpression);
            if (staticReturnType != null) {
                return staticReturnType;
            }
        }
        if (returnExpression instanceof QinIrObjectLiteral) {
            return QinIrTypeRef.classType("java.util.Map");
        }
        if (returnExpression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return inferDeclarationBuiltinCallReturnType(builtinCallExpression, parameters, classContext);
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef inferDeclarationStaticMethodReturnType(
            QinIrStaticMethodCallExpression staticMethodCallExpression) {
        String ownerBinaryName = QinJavaSdkAliasSupport.canonicalBinaryName(
                staticMethodCallExpression.ownerBinaryName());
        if ("java.util.Optional".equals(ownerBinaryName)) {
            return switch (staticMethodCallExpression.methodName()) {
                case "empty", "of", "ofNullable" -> QinIrTypeRef.classType("java.util.Optional");
                default -> null;
            };
        }
        if ("java.lang.String".equals(ownerBinaryName)) {
            return switch (staticMethodCallExpression.methodName()) {
                case "join", "valueOf", "format" -> QinIrTypeRef.stringType();
                default -> null;
            };
        }
        return null;
    }

    private QinIrTypeRef inferDeclarationBodyReturnType(
            List<QinIrStatement> bodyStatements,
            List<QinIrParameter> parameters,
            DeclarationClassContext classContext) {
        if (bodyStatements == null || bodyStatements.isEmpty()) {
            return null;
        }
        List<QinIrTypeRef> returnTypes = new ArrayList<>();
        collectDeclarationReturnTypes(bodyStatements, parameters, classContext, returnTypes);
        return singleSpecificReturnTypeOrNull(returnTypes);
    }

    private void collectDeclarationReturnTypes(
            List<QinIrStatement> statements,
            List<QinIrParameter> parameters,
            DeclarationClassContext classContext,
            List<QinIrTypeRef> returnTypes) {
        for (QinIrStatement statement : statements) {
            if (statement instanceof QinIrReturnStatement returnStatement) {
                if (returnStatement.value() != null && !(returnStatement.value() instanceof QinIrNullLiteral)) {
                    returnTypes.add(inferDeclarationReturnType(returnStatement.value(), parameters, classContext));
                }
                continue;
            }
            if (statement instanceof QinIrIfStatement ifStatement) {
                collectDeclarationReturnTypes(ifStatement.consequent(), parameters, classContext, returnTypes);
                collectDeclarationReturnTypes(ifStatement.alternate(), parameters, classContext, returnTypes);
            }
        }
    }

    private QinIrTypeRef singleSpecificReturnTypeOrNull(List<QinIrTypeRef> returnTypes) {
        if (returnTypes == null || returnTypes.isEmpty()) {
            return null;
        }
        QinIrTypeRef selected = null;
        boolean hasObjectPlaceholder = false;
        for (QinIrTypeRef returnType : returnTypes) {
            if (returnType == null) {
                continue;
            }
            if (isObjectPlaceholderType(returnType)) {
                hasObjectPlaceholder = true;
                continue;
            }
            if (selected == null) {
                selected = returnType;
                continue;
            }
            if (!selected.equals(returnType)) {
                return null;
            }
        }
        if (selected != null && hasObjectPlaceholder) {
            return null;
        }
        return selected;
    }

    private QinIrTypeRef singleSpecificReturnTypeOrNull(QinIrTypeRef first, QinIrTypeRef second) {
        List<QinIrTypeRef> returnTypes = new ArrayList<>();
        if (first != null) {
            returnTypes.add(first);
        }
        if (second != null) {
            returnTypes.add(second);
        }
        return singleSpecificReturnTypeOrNull(returnTypes);
    }

    private boolean isObjectPlaceholderType(QinIrTypeRef type) {
        return type != null
                && type.kind() == com.qin.lang.ir.QinIrTypeKind.CLASS
                && "java.lang.Object".equals(type.binaryName());
    }

    private QinIrExpression lowerDeclarationStatementExpression(
            Expression expression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression arrayPushStatement = lowerStaticArrayPushStatementOrNull(
                expression,
                javaImportLookup,
                classContext,
                locals);
        if (arrayPushStatement != null) {
            return arrayPushStatement;
        }
        QinIrExpression lowered = lowerDeclarationExpression(expression, javaImportLookup, classContext, locals);
        if (lowered instanceof QinIrBuiltinCallExpression
                || lowered instanceof QinIrAssignmentExpression
                || lowered instanceof QinIrInstanceMethodCallExpression
                || lowered instanceof QinIrStaticMethodCallExpression
                || lowered instanceof QinIrSuperMethodCallExpression
                || lowered instanceof QinIrUpdateExpression
                || lowered instanceof QinIrSequenceExpression
                || lowered instanceof QinIrNullLiteral) {
            return lowered;
        }
        throw qjsError("QJS2021", "Unsupported declaration expression statement");
    }

    private QinIrExpression lowerStaticArrayPushStatementOrNull(
            Expression expression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (!(expression instanceof CallExpression callExpression)
                || !(callExpression.callee() instanceof MemberExpression memberExpression)
                || memberExpression.computed()
                || callExpression.arguments().size() != 1
                || !"push".equals(adapter.extractMemberPropertyName(memberExpression.property()))) {
            return null;
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                memberExpression.object(),
                javaImportLookup,
                classContext,
                locals);
        QinIrTypeRef receiverType = null;
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            receiverType = currentDeclarationValueTypes.get(identifierReference.name());
        } else if (receiver instanceof QinIrPropertyAccessExpression propertyAccessExpression
                && propertyAccessExpression.receiver() instanceof QinIrThisExpression) {
            receiverType = inferDeclarationReturnType(receiver, List.of(), classContext);
        }
        if (!isObjectArrayType(receiverType)) {
            return null;
        }
        QinIrExpression value = lowerDeclarationExpression(
                callExpression.arguments().get(0),
                javaImportLookup,
                classContext,
                locals);
        return new QinIrAssignmentExpression(
                receiver,
                "=",
                new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_array_append__",
                        List.of(receiver, value)));
    }

    private QinIrTypeRef inferDeclarationBuiltinCallReturnType(
            QinIrBuiltinCallExpression builtinCallExpression,
            List<QinIrParameter> parameters,
            DeclarationClassContext classContext) {
        if (!"Global".equals(builtinCallExpression.receiverName())) {
            QinIrTypeRef semanticType = inferDeclarationBuiltinSemanticReturnType(
                    builtinCallExpression.receiverName(),
                    builtinCallExpression.methodName());
            if (semanticType != null) {
                return semanticType;
            }
            return inferRegisteredDeclarationBuiltinReturnType(builtinCallExpression);
        }
        if ("__qin_binary__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3
                && builtinCallExpression.arguments().get(0) instanceof QinIrStringLiteral operatorLiteral) {
            QinIrTypeRef leftType = inferDeclarationReturnType(
                    builtinCallExpression.arguments().get(1),
                    parameters,
                    classContext);
            QinIrTypeRef rightType = inferDeclarationReturnType(
                    builtinCallExpression.arguments().get(2),
                    parameters,
                    classContext);
            return switch (operatorLiteral.value()) {
                case "+" -> isDeclarationStringLike(leftType) || isDeclarationStringLike(rightType)
                        ? QinIrTypeRef.stringType()
                        : isDeclarationNumericLike(leftType) && isDeclarationNumericLike(rightType)
                        ? QinIrTypeRef.doubleType()
                        : QinIrTypeRef.classType("java.lang.Object");
                case "-", "*", "/", "%" -> QinIrTypeRef.doubleType();
                case "==", "!=", "===", "!==", "<", "<=", ">", ">=" -> QinIrTypeRef.booleanType();
                default -> QinIrTypeRef.classType("java.lang.Object");
            };
        }
        if ("__qin_logical__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3
                && builtinCallExpression.arguments().get(0) instanceof QinIrStringLiteral operatorLiteral) {
            QinIrTypeRef leftType = inferDeclarationReturnType(
                    builtinCallExpression.arguments().get(1),
                    parameters,
                    classContext);
            QinIrTypeRef rightType = inferDeclarationReturnType(
                    builtinCallExpression.arguments().get(2),
                    parameters,
                    classContext);
            return inferDeclarationLogicalResultType(operatorLiteral.value(), leftType, rightType);
        }
        if ("__qin_conditional__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            QinIrExpression consequent = builtinCallExpression.arguments().get(1);
            QinIrExpression alternate = builtinCallExpression.arguments().get(2);
            if (consequent instanceof QinIrNullLiteral && !(alternate instanceof QinIrNullLiteral)) {
                return inferDeclarationReturnType(alternate, parameters, classContext);
            }
            if (alternate instanceof QinIrNullLiteral && !(consequent instanceof QinIrNullLiteral)) {
                return inferDeclarationReturnType(consequent, parameters, classContext);
            }
            QinIrTypeRef consequentType = inferDeclarationReturnType(
                    consequent,
                    parameters,
                    classContext);
            QinIrTypeRef alternateType = inferDeclarationReturnType(
                    alternate,
                    parameters,
                    classContext);
            return mergeDeclarationBranchTypes(consequentType, alternateType);
        }
        if ("__qin_string__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            return QinIrTypeRef.stringType();
        }
        if ("__qin_number__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() <= 1) {
            return QinIrTypeRef.doubleType();
        }
        if ("__qin_collection_get__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            QinIrTypeRef collectionType = inferDeclarationReturnType(
                    builtinCallExpression.arguments().get(0),
                    parameters,
                    classContext);
            return staticDeclarationCollectionElementType(collectionType);
        }
        QinIrTypeRef semanticType = inferDeclarationBuiltinSemanticReturnType(
                builtinCallExpression.receiverName(),
                builtinCallExpression.methodName());
        if (semanticType != null) {
            return semanticType;
        }
        return inferRegisteredDeclarationBuiltinReturnType(builtinCallExpression);
    }

    private QinIrTypeRef staticDeclarationCollectionElementType(QinIrTypeRef collectionType) {
        if (collectionType == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (collectionType.kind() == QinIrTypeKind.STRING) {
            return QinIrTypeRef.stringType();
        }
        if (isObjectArrayType(collectionType)) {
            return boxForObjectStorage(collectionType.typeArguments().isEmpty()
                    ? QinIrTypeRef.classType("java.lang.Object")
                    : collectionType.typeArguments().get(0));
        }
        if (collectionType.kind() == QinIrTypeKind.CLASS
                && collectionType.typeArguments() != null
                && !collectionType.typeArguments().isEmpty()) {
            String binaryName = QinJavaSdkAliasSupport.canonicalBinaryName(collectionType.binaryName());
            if (isDeclarationJavaUtilMapType(binaryName) && collectionType.typeArguments().size() >= 2) {
                return boxForObjectStorage(collectionType.typeArguments().get(1));
            }
            return boxForObjectStorage(collectionType.typeArguments().get(0));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private boolean isDeclarationJavaUtilMapType(String binaryName) {
        return "java.util.Map".equals(binaryName)
                || "java.util.HashMap".equals(binaryName)
                || "java.util.LinkedHashMap".equals(binaryName)
                || "java.util.IdentityHashMap".equals(binaryName)
                || "java.util.concurrent.ConcurrentHashMap".equals(binaryName)
                || "java.util.concurrent.ConcurrentMap".equals(binaryName);
    }

    private QinIrTypeRef inferRegisteredDeclarationBuiltinReturnType(
            QinIrBuiltinCallExpression builtinCallExpression) {
        QinBuiltinRegistry.BuiltinMethod builtinMethod = QinBuiltinRegistry.resolve(
                builtinCallExpression.receiverName(),
                builtinCallExpression.methodName(),
                builtinCallExpression.arguments().size()).orElse(null);
        if (builtinMethod == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return toDeclarationBuiltinReturnType(builtinMethod);
    }

    private QinIrTypeRef toDeclarationBuiltinReturnType(QinBuiltinRegistry.BuiltinMethod builtinMethod) {
        String descriptor = builtinMethod.descriptor().returnType().descriptorString();
        return switch (descriptor) {
            case "V" -> QinIrTypeRef.voidType();
            case "Z" -> QinIrTypeRef.booleanType();
            case "I" -> QinIrTypeRef.intType();
            case "D" -> QinIrTypeRef.doubleType();
            case "[Ljava/lang/Object;" -> objectArrayType();
            case "Ljava/lang/String;" -> QinIrTypeRef.stringType();
            default -> {
                if (descriptor.startsWith("L") && descriptor.endsWith(";")) {
                    yield QinIrTypeRef.classType(
                            descriptor.substring(1, descriptor.length() - 1).replace('/', '.'));
                }
                yield QinIrTypeRef.classType("java.lang.Object");
            }
        };
    }

    private QinIrTypeRef inferDeclarationBuiltinSemanticReturnType(
            String receiverName,
            String methodName) {
        if (receiverName == null || methodName == null) {
            return null;
        }
        return switch (receiverName) {
            case "console" -> "log".equals(methodName) ? QinIrTypeRef.voidType() : null;
            case "Math" -> switch (methodName) {
                case "random", "abs", "floor", "ceil", "max", "min",
                        "round", "trunc", "pow", "sqrt", "sin", "cos", "tan", "log", "exp" ->
                        QinIrTypeRef.doubleType();
                default -> null;
            };
            case "JSON" -> switch (methodName) {
                case "stringify" -> QinIrTypeRef.stringType();
                case "parse" -> QinIrTypeRef.classType("java.lang.Object");
                default -> null;
            };
            case "Number", "Global" -> switch (methodName) {
                case "parseInt", "parseFloat" -> QinIrTypeRef.doubleType();
                case "isNaN", "isFinite", "isInteger", "isSafeInteger" -> QinIrTypeRef.booleanType();
                case "__qin_java_string_hash_code__",
                        "__qin_java_identity_hash_code__",
                        "__qin_java_value_hash_code__",
                        "__qin_java_long_hash_code__" -> QinIrTypeRef.doubleType();
                case "__qin_java_values_equal__",
                        "__qin_java_hash_key_equals__",
                        "__qin_java_implements" -> QinIrTypeRef.booleanType();
                case "__qin_java_hash_key__" -> QinIrTypeRef.stringType();
                case "__qin_java_regex_pattern_compile__",
                        "__qin_java_regex_pattern_exec__",
                        "__qin_java_regex_matcher__",
                        "__qin_java_regex_matcher_region__",
                        "__qin_java_regex_matcher_reset__",
                        "__qin_java_regex_matcher_append_replacement__",
                        "__qin_java_regex_matcher_append_tail__" -> QinIrTypeRef.classType("java.lang.Object");
                case "__qin_java_regex_matcher_looking_at__",
                        "__qin_java_regex_matcher_matches__",
                        "__qin_java_regex_matcher_find__" -> QinIrTypeRef.booleanType();
                case "__qin_java_regex_matcher_group_count__",
                        "__qin_java_regex_matcher_start__",
                        "__qin_java_regex_matcher_end__" -> QinIrTypeRef.doubleType();
                case "__qin_java_regex_matcher_group__",
                        "__qin_java_regex_matcher_replace_all__" -> QinIrTypeRef.classType("java.lang.Object");
                case "__qin_subhuti_identity_rule_cache_id",
                        "__qin_subhuti_value_rule_cache_id" -> QinIrTypeRef.doubleType();
                case "__qin_subhuti_rule_cache_key" -> QinIrTypeRef.stringType();
                  case "__qin_direct_method_function__" -> QinIrTypeRef.classType("java.lang.Object");
                  case "__qin_java_functional" ->
                          QinIrTypeRef.classType("com.qin.lang.runtime.JavaEsmGlobal$JavaFunctionalObject");
                case "__qin_token_name_of__",
                        "__qin_token_value_of__",
                        "__qin_token_index_of__" -> QinIrTypeRef.stringType();
                case "__qin_token_has_line_break_before__" -> QinIrTypeRef.booleanType();
                default -> null;
            };
            case "Object" -> "hasOwn".equals(methodName) ? QinIrTypeRef.booleanType() : null;
            case "String" -> switch (methodName) {
                case "trim", "toUpperCase", "toLowerCase", "slice", "substring", "charAt" ->
                        QinIrTypeRef.stringType();
                case "includes", "startsWith", "endsWith" -> QinIrTypeRef.booleanType();
                case "split" -> QinIrTypeRef.classType("java.util.List");
                default -> null;
            };
            case "Array" -> switch (methodName) {
                case "join" -> QinIrTypeRef.stringType();
                case "isArray", "includes", "some", "every" -> QinIrTypeRef.booleanType();
                case "indexOf" -> QinIrTypeRef.intType();
                case "push", "pop", "at", "find", "map", "filter", "forEach" ->
                        QinIrTypeRef.classType("java.lang.Object");
                default -> null;
            };
            case "Map", "Set" -> switch (methodName) {
                case "has" -> QinIrTypeRef.booleanType();
                case "size" -> QinIrTypeRef.intType();
                default -> QinIrTypeRef.classType("java.lang.Object");
            };
            case "Date" -> "now".equals(methodName) ? QinIrTypeRef.doubleType() : null;
            default -> null;
        };
    }

    private QinIrTypeRef inferDeclarationLogicalResultType(
            String operator,
            QinIrTypeRef leftType,
            QinIrTypeRef rightType) {
        if ("&&".equals(operator) || "||".equals(operator) || "??".equals(operator)) {
            if (leftType.equals(rightType)) {
                return leftType;
            }
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return QinIrTypeRef.booleanType();
    }

    private QinIrTypeRef mergeDeclarationBranchTypes(
            QinIrTypeRef consequentType,
            QinIrTypeRef alternateType) {
        if (Objects.equals(consequentType, alternateType)) {
            return consequentType;
        }
        QinIrTypeRef genericBranchType = mergeGenericErasedBranchType(consequentType, alternateType);
        if (genericBranchType != null) {
            return genericBranchType;
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef mergeGenericErasedBranchType(QinIrTypeRef leftType, QinIrTypeRef rightType) {
        if (leftType == null || rightType == null) {
            return null;
        }
        if (leftType.kind() != QinIrTypeKind.CLASS || rightType.kind() != QinIrTypeKind.CLASS) {
            return null;
        }
        String leftName = QinJavaSdkAliasSupport.canonicalBinaryName(leftType.binaryName());
        String rightName = QinJavaSdkAliasSupport.canonicalBinaryName(rightType.binaryName());
        if (!Objects.equals(leftName, rightName)) {
            return null;
        }
        if (leftType.typeArguments().isEmpty() && !rightType.typeArguments().isEmpty()) {
            return QinIrTypeRef.classType(rightName, rightType.typeArguments());
        }
        if (rightType.typeArguments().isEmpty() && !leftType.typeArguments().isEmpty()) {
            return QinIrTypeRef.classType(leftName, leftType.typeArguments());
        }
        return null;
    }

    private boolean isDeclarationNumericLike(QinIrTypeRef typeRef) {
        return typeRef != null && (
                typeRef.equals(QinIrTypeRef.intType())
                        || typeRef.equals(QinIrTypeRef.doubleType())
                        || "java.lang.Integer".equals(typeRef.binaryName())
                        || "java.lang.Long".equals(typeRef.binaryName())
                        || "java.lang.Double".equals(typeRef.binaryName())
                        || "java.lang.Number".equals(typeRef.binaryName()));
    }

    private boolean isDeclarationStringLike(QinIrTypeRef typeRef) {
        return typeRef != null && (
                typeRef.equals(QinIrTypeRef.stringType())
                        || "java.lang.String".equals(typeRef.binaryName()));
    }

    private QinIrParameter resolveDeclarationParameter(List<QinIrParameter> parameters, String parameterName) {
        if (parameters == null || parameterName == null || parameterName.isBlank()) {
            return null;
        }
        for (QinIrParameter parameter : parameters) {
            if (parameter.name().equals(parameterName)) {
                return parameter;
            }
        }
        return null;
    }

    private QinIrTypeRef resolveDeclarationPropertyType(
            QinIrTypeRef ownerType,
            String propertyName,
            DeclarationClassContext classContext) {
        if (ownerType == null || propertyName == null || propertyName.isBlank()) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (ownerType.binaryName() != null && ownerType.binaryName().equals(classContext.className())) {
            QinIrFieldDeclaration field = classContext.fields().get(propertyName);
            if (field != null) {
                return field.type();
            }
        }
        if ("length".equals(propertyName) && isObjectArrayType(ownerType)) {
            return QinIrTypeRef.intType();
        }
        QinIrTypeRef structuralPropertyType = resolveStructuralSlimeAstPropertyType(ownerType, propertyName);
        if (structuralPropertyType != null) {
            return structuralPropertyType;
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef resolveStructuralSlimeAstPropertyType(QinIrTypeRef ownerType, String propertyName) {
        String typeName = structuralSlimeAstTypeName(ownerType);
        if (typeName == null || propertyName == null || propertyName.isBlank()) {
            return null;
        }
        if ("type".equals(propertyName)) {
            return QinIrTypeRef.stringType();
        }
        if ("loc".equals(propertyName)) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return switch (typeName) {
            case "SlimeProgram", "SlimeJavascriptProgram" -> switch (propertyName) {
                case "body" -> objectArrayType(structuralSlimeAstType("SlimeModuleDeclaration"));
                default -> null;
            };
            case "SlimeImportDeclaration", "SlimeJavascriptImportDeclaration" -> switch (propertyName) {
                case "source" -> structuralSlimeAstType("SlimeLiteral");
                case "specifiers" -> objectArrayType(structuralSlimeAstType("SlimeImportSpecifierItem"));
                default -> null;
            };
            case "SlimeImportSpecifierItem", "SlimeJavascriptImportSpecifierItem" -> switch (propertyName) {
                case "specifier" -> structuralSlimeAstType("SlimeModuleSpecifier");
                default -> null;
            };
            case "SlimeVariableDeclarator", "SlimeJavascriptVariableDeclarator" -> switch (propertyName) {
                case "init" -> structuralSlimeAstType("SlimeExpression");
                case "id" -> structuralSlimeAstType("SlimePattern");
                default -> null;
            };
            case "SlimeCallExpression", "SlimeSimpleCallExpression", "SlimeJavascriptCallExpression",
                    "SlimeJavascriptSimpleCallExpression" -> switch (propertyName) {
                case "callee" -> structuralSlimeAstType("SlimeExpression");
                case "arguments" -> objectArrayType(structuralSlimeAstType("SlimeCallArgument"));
                default -> null;
            };
            case "SlimeCallArgument", "SlimeJavascriptCallArgument" -> switch (propertyName) {
                case "argument" -> structuralSlimeAstType("SlimeExpression");
                default -> null;
            };
            case "SlimeMemberExpression", "SlimeJavascriptMemberExpression" -> switch (propertyName) {
                case "object", "property" -> structuralSlimeAstType("SlimeExpression");
                default -> null;
            };
            case "SlimeImportSpecifier", "SlimeJavascriptImportSpecifier" -> switch (propertyName) {
                case "imported", "local" -> structuralSlimeAstType("SlimeIdentifier");
                default -> null;
            };
            case "SlimeImportDefaultSpecifier", "SlimeJavascriptImportDefaultSpecifier",
                    "SlimeImportNamespaceSpecifier", "SlimeJavascriptImportNamespaceSpecifier" -> switch (propertyName) {
                case "local" -> structuralSlimeAstType("SlimeIdentifier");
                default -> null;
            };
            case "SlimeModuleSpecifier" -> switch (propertyName) {
                case "imported", "local", "exported" -> structuralSlimeAstType("SlimeIdentifier");
                default -> null;
            };
            case "SlimeIdentifier", "SlimeJavascriptIdentifier" -> switch (propertyName) {
                case "name" -> QinIrTypeRef.stringType();
                default -> null;
            };
            case "SlimeLiteral", "SlimeSimpleLiteral", "SlimeStringLiteral",
                    "SlimeJavascriptLiteral", "SlimeJavascriptSimpleLiteral", "SlimeJavascriptStringLiteral" ->
                    switch (propertyName) {
                        case "raw" -> QinIrTypeRef.stringType();
                        default -> null;
                    };
            default -> null;
        };
    }

    private boolean isObjectArrayType(QinIrTypeRef type) {
        return type != null
                && type.kind() == com.qin.lang.ir.QinIrTypeKind.CLASS
                && "java.lang.Object[]".equals(type.binaryName());
    }

    private QinIrTypeRef resolveDeclarationMethodReturnType(
            QinIrTypeRef ownerType,
            String methodName,
            int parameterCount,
            DeclarationClassContext classContext) {
        if (ownerType == null || methodName == null || methodName.isBlank()) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (ownerType.binaryName() != null && ownerType.binaryName().equals(classContext.className())) {
            QinIrMethodDeclaration method = classContext.findMethod(methodName, parameterCount);
            if (method != null) {
                return method.returnType();
            }
        }
        QinIrTypeRef mapMethodReturnType = resolveDeclarationMapMethodReturnType(ownerType, methodName, parameterCount);
        if (mapMethodReturnType != null) {
            return mapMethodReturnType;
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef resolveDeclarationMapMethodReturnType(
            QinIrTypeRef ownerType,
            String methodName,
            int parameterCount) {
        if (ownerType == null
                || methodName == null
                || parameterCount != 0
                || ownerType.binaryName() == null
                || (!isDeclarationJavaUtilMapType(QinJavaSdkAliasSupport.canonicalBinaryName(ownerType.binaryName()))
                        && !"com.qin.lang.runtime.JavaEsmMapObject".equals(ownerType.binaryName()))) {
            return null;
        }
        return switch (methodName) {
            case "keys", "keySet" -> QinIrTypeRef.classType(
                    "java.util.Set",
                    List.of(declarationMapKeyType(ownerType)));
            case "values" -> QinIrTypeRef.classType(
                    "java.util.Collection",
                    List.of(declarationMapValueType(ownerType)));
            case "entries", "entrySet" -> QinIrTypeRef.classType(
                    "java.util.Set",
                    List.of(QinIrTypeRef.classType(
                            "java.util.Map$Entry",
                            List.of(declarationMapKeyType(ownerType), declarationMapValueType(ownerType)))));
            default -> null;
        };
    }

    private QinIrTypeRef declarationMapKeyType(QinIrTypeRef ownerType) {
        if (ownerType != null && ownerType.typeArguments() != null && !ownerType.typeArguments().isEmpty()) {
            return boxForObjectStorage(ownerType.typeArguments().get(0));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef declarationMapValueType(QinIrTypeRef ownerType) {
        if (ownerType != null && ownerType.typeArguments() != null && ownerType.typeArguments().size() >= 2) {
            return boxForObjectStorage(ownerType.typeArguments().get(1));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrExpression lowerDeclarationExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        return lowerDeclarationExpression(expressionAst, javaImportLookup, classContext, Map.of());
    }

    private QinIrExpression lowerDeclarationExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (expressionAst instanceof ParenthesizedExpression parenthesizedExpression) {
            return lowerDeclarationExpression(parenthesizedExpression.expression(), javaImportLookup, classContext, locals);
        }
        if (expressionAst instanceof Literal literal) {
            return adapter.lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof Identifier identifier) {
            QinIrExpression local = locals.get(identifier.name());
            if (local != null) {
                if (local instanceof QinIrIdentifierReference alias
                        && QinJavaSdkAliasSupport.isKnownAlias(alias.name())) {
                    return new QinIrJavaClassLiteralExpression(
                            identifier.name(),
                            QinJavaSdkAliasSupport.canonicalBinaryName(alias.name()));
                }
                return local;
            }
            QinIrExpression localDeclarationClass = localDeclarationClassLiteralOrNull(identifier.name(), classContext);
            if (localDeclarationClass != null) {
                return localDeclarationClass;
            }
            String importedBinaryName = javaImportLookup.get(identifier.name());
            if (importedBinaryName != null && QinJavaSdkAliasSupport.isKnownAlias(identifier.name())) {
                return new QinIrJavaClassLiteralExpression(identifier.name(), importedBinaryName);
            }
            QinIrExpression moduleBinding = classContext.moduleBinding(identifier.name());
            if (moduleBinding != null) {
                QinIrExpression javaSdkClassLiteral =
                        javaSdkModuleClassLiteralOrNull(identifier.name(), moduleBinding);
                if (javaSdkClassLiteral != null) {
                    return javaSdkClassLiteral;
                }
                return moduleBindingReferenceExpression(identifier.name(), moduleBinding);
            }
            if (isUnsupportedDeclarationGlobalIdentifier(identifier.name())) {
                throw qjsError("QJS2016", "Unsupported declaration global identifier: " + identifier.name());
            }
            return adapter.lowerIdentifierExpression(identifier);
        }
        if (expressionAst instanceof ThisExpression) {
            return new QinIrThisExpression();
        }
        if (expressionAst instanceof MemberExpression memberExpression) {
            return lowerDeclarationMemberAccessExpression(memberExpression, javaImportLookup, classContext, locals);
        }
        if (expressionAst instanceof CallExpression callExpression) {
            return lowerDeclarationCallExpression(callExpression, javaImportLookup, classContext, locals);
        }
        if (expressionAst instanceof ObjectExpression objectExpression) {
            return lowerDeclarationObjectLiteral(objectExpression, javaImportLookup, classContext, locals);
        }
        if (expressionAst instanceof BinaryExpression binaryExpression) {
            return lowerDeclarationBinaryExpression(binaryExpression, javaImportLookup, classContext, locals);
        }
        if (expressionAst instanceof LogicalExpression logicalExpression) {
            return lowerDeclarationLogicalExpression(logicalExpression, javaImportLookup, classContext, locals);
        }
        if (expressionAst instanceof ConditionalExpression conditionalExpression) {
            return lowerDeclarationConditionalExpression(conditionalExpression, javaImportLookup, classContext, locals);
        }
        if (expressionAst instanceof AssignmentExpression assignmentExpression) {
            return lowerDeclarationAssignmentExpression(assignmentExpression, javaImportLookup, classContext, locals);
        }
        if (expressionAst instanceof SpreadElement spreadElement) {
            return new QinIrSpreadArgumentExpression(lowerDeclarationExpression(
                    spreadElement.argument(),
                    javaImportLookup,
                    classContext,
                    locals));
        }
        if (expressionAst instanceof NewExpression newExpression) {
            Object callee = newExpression.callee();
            if (callee instanceof Identifier identifier && isUnsupportedDeclarationGlobalIdentifier(identifier.name())) {
                throw qjsError("QJS2016", "Unsupported declaration new target: " + identifier.name());
            }
            QinIrExpression staticNewExpression =
                    lowerDeclarationNewExpressionOrNull(newExpression, javaImportLookup, classContext, locals);
            if (staticNewExpression != null) {
                return staticNewExpression;
            }
            return adapter.lowerRuntimeExpression(expressionAst, javaImportLookup, locals);
        }
        if (expressionAst instanceof FunctionExpression || expressionAst instanceof ArrowFunctionExpression) {
            return lowerDeclarationRuntimeFunctionExpression(
                    expressionAst,
                    javaImportLookup,
                    locals);
        }
        if (expressionAst instanceof com.slime.ast.nodes.expressions.ArrayExpression arrayExpression) {
            return lowerDeclarationArrayLiteral(arrayExpression, javaImportLookup, classContext, locals);
        }

        String nodeType = QinSlimeFrontendAdapter.simpleName(expressionAst);
        if ("ParenthesizedExpression".equals(nodeType)) {
            return lowerDeclarationExpression(
                    QinSlimeFrontendAdapter.invokeByName(expressionAst, "expression"),
                    javaImportLookup,
                    classContext,
                    locals);
        }
        if ("Literal".equals(nodeType) || "Identifier".equals(nodeType)) {
            if ("Identifier".equals(nodeType)) {
                String identifierName = QinSlimeFrontendAdapter.extractIdentifierNameStatic(expressionAst, "Identifier");
                QinIrExpression local = locals.get(identifierName);
                if (local != null) {
                    if (local instanceof QinIrIdentifierReference alias
                            && QinJavaSdkAliasSupport.isKnownAlias(alias.name())) {
                        return new QinIrJavaClassLiteralExpression(
                                identifierName,
                                QinJavaSdkAliasSupport.canonicalBinaryName(alias.name()));
                    }
                    return local;
                }
                QinIrExpression localDeclarationClass = localDeclarationClassLiteralOrNull(identifierName, classContext);
                if (localDeclarationClass != null) {
                    return localDeclarationClass;
                }
                String importedBinaryName = javaImportLookup.get(identifierName);
                if (importedBinaryName != null && QinJavaSdkAliasSupport.isKnownAlias(identifierName)) {
                    return new QinIrJavaClassLiteralExpression(identifierName, importedBinaryName);
                }
                QinIrExpression moduleBinding = classContext.moduleBinding(identifierName);
                if (moduleBinding != null) {
                    QinIrExpression javaSdkClassLiteral =
                            javaSdkModuleClassLiteralOrNull(identifierName, moduleBinding);
                    if (javaSdkClassLiteral != null) {
                        return javaSdkClassLiteral;
                    }
                    return moduleBindingReferenceExpression(identifierName, moduleBinding);
                }
                return adapter.lowerExpression(expressionAst, javaImportLookup);
            }
            return adapter.lowerExpression(expressionAst, javaImportLookup);
        }
        if ("ThisExpression".equals(nodeType)) {
            return new QinIrThisExpression();
        }
        if ("MemberExpression".equals(nodeType)) {
            return lowerDeclarationMemberAccessExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("CallExpression".equals(nodeType)) {
            return lowerDeclarationCallExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("ObjectExpression".equals(nodeType)) {
            return lowerDeclarationObjectLiteral(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("BinaryExpression".equals(nodeType)) {
            return lowerDeclarationBinaryExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("LogicalExpression".equals(nodeType)) {
            return lowerDeclarationLogicalExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("ConditionalExpression".equals(nodeType)) {
            return lowerDeclarationConditionalExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("SequenceExpression".equals(nodeType)) {
            return lowerDeclarationSequenceExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("AssignmentExpression".equals(nodeType)) {
            return lowerDeclarationAssignmentExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("SpreadElement".equals(nodeType)) {
            Object argument = QinSlimeFrontendAdapter.invokeByName(expressionAst, "argument");
            return new QinIrSpreadArgumentExpression(lowerDeclarationExpression(
                    argument,
                    javaImportLookup,
                    classContext,
                    locals));
        }
        if ("UnaryExpression".equals(nodeType)) {
            return lowerDeclarationUnaryExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("UpdateExpression".equals(nodeType)) {
            return lowerDeclarationUpdateExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("ArrayExpression".equals(nodeType)) {
            return lowerDeclarationArrayLiteral(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("TSAsExpression".equals(nodeType)) {
            Object typeAnnotation = QinSlimeFrontendAdapter.invokeByName(expressionAst, "typeAnnotation");
            QinIrTypeRef type = lowerTypeNodeRef(
                    typeAnnotation,
                    javaImportLookup,
                    classContext == null ? Set.of() : classContext.localDeclarationNames(),
                    Map.of());
            QinIrExpression lowered = lowerDeclarationExpression(
                    QinSlimeFrontendAdapter.invokeByName(expressionAst, "expression"),
                    javaImportLookup,
                    classContext,
                    locals);
            if (type == null
                    || type.kind() != QinIrTypeKind.CLASS
                    || type.binaryName() == null
                    || type.binaryName().isBlank()
                    || "java.lang.Object".equals(QinJavaSdkAliasSupport.canonicalBinaryName(type.binaryName()))) {
                return lowered;
            }
            return new QinIrCastExpression(type.binaryName(), lowered);
        }
        if ("TemplateLiteral".equals(nodeType) || "TaggedTemplateExpression".equals(nodeType)) {
            return adapter.lowerRuntimeExpression(expressionAst, javaImportLookup, locals);
        }
        if ("NewExpression".equals(nodeType)) {
            QinIrExpression staticNewExpression =
                    lowerDeclarationNewExpressionOrNull(expressionAst, javaImportLookup, classContext, locals);
            if (staticNewExpression != null) {
                return staticNewExpression;
            }
            return adapter.lowerRuntimeExpression(expressionAst, javaImportLookup, locals);
        }
        if ("FunctionExpression".equals(nodeType) || "ArrowFunctionExpression".equals(nodeType)) {
            return adapter.lowerRuntimeExpression(expressionAst, javaImportLookup, locals);
        }
        throw qjsError("QJS2014", "Unsupported declaration return expression: " + nodeType);
    }

    private QinIrExpression javaSdkModuleClassLiteralOrNull(
            String localName,
            QinIrExpression moduleBinding) {
        if (moduleBinding instanceof QinIrJavaClassLiteralExpression) {
            return moduleBinding;
        }
        if (localName != null && QinJavaSdkAliasSupport.isKnownAlias(localName)) {
            return new QinIrJavaClassLiteralExpression(
                    localName,
                    QinJavaSdkAliasSupport.canonicalBinaryName(localName));
        }
        if (moduleBinding instanceof QinIrIdentifierReference identifierReference
                && QinJavaSdkAliasSupport.isKnownAlias(identifierReference.name())) {
            return new QinIrJavaClassLiteralExpression(
                    identifierReference.name(),
                    QinJavaSdkAliasSupport.canonicalBinaryName(identifierReference.name()));
        }
        return null;
    }

    private QinIrExpression moduleBindingReferenceExpression(
            String localName,
            QinIrExpression moduleBinding) {
        if (isStaticExportSlotGetExpression(moduleBinding)) {
            return moduleBinding;
        }
        QinIrExpression refGet = new QinIrBuiltinCallExpression(
                "Global",
                "__qin_module_ref_get__",
                List.of(new QinIrStringLiteral(localName)));
        String staticType = moduleBindingStaticTypeNameOrNull(moduleBinding);
        if (staticType == null) {
            return refGet;
        }
        return new QinIrCastExpression(staticType, refGet);
    }

    private String moduleBindingStaticTypeNameOrNull(QinIrExpression moduleBinding) {
        if (moduleBinding instanceof QinIrJavaNewExpression javaNewExpression) {
            return javaNewExpression.ownerBinaryName();
        }
        if (moduleBinding instanceof QinIrCastExpression castExpression) {
            return castExpression.typeName();
        }
        return null;
    }

    private QinIrExpression localDeclarationClassLiteralOrNull(
            String className,
            DeclarationClassContext classContext) {
        if (className == null || className.isBlank()
                || classContext == null
                || !classContext.isLocalDeclarationName(className)) {
            return null;
        }
        String binaryName = classContext.jsDeclarationClass(className);
        if (binaryName == null || binaryName.isBlank()) {
            binaryName = classContext.jsDeclarationClass(generatedSimpleClassName(className));
        }
        if (binaryName == null || binaryName.isBlank()) {
            binaryName = canonicalDeclarationBinaryName(className);
        }
        return new QinIrJavaClassLiteralExpression(className, binaryName);
    }

    private QinIrExpression lowerDeclarationNewExpressionOrNull(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        boolean preserveOwnerBinaryName = false;
        Object callee = expressionAst instanceof NewExpression newExpression
                ? newExpression.callee()
                : QinSlimeFrontendAdapter.invokeByName(expressionAst, "callee");
        String className = declarationIdentifierName(callee);
        if (className == null || className.isBlank()) {
            return null;
        }
        List<?> arguments = expressionAst instanceof NewExpression newExpression
                ? newExpression.arguments()
                : QinSlimeFrontendAdapter.asListStatic(
                        QinSlimeFrontendAdapter.invokeByName(expressionAst, "arguments"),
                        "NewExpression.arguments");
        if ("Array".equals(className) && arguments.size() == 1) {
            return new QinIrArrayCreationExpression(
                    QinIrTypeRef.classType("java.lang.Object"),
                    List.of(lowerDeclarationExpression(arguments.get(0), javaImportLookup, classContext, locals)),
                    0);
        }
        String ownerBinaryName = null;
        String nativeJavaSdkOwner = nativeJavaSdkCollectionOwnerBinaryName(className);
        if (nativeJavaSdkOwner != null) {
            ownerBinaryName = nativeJavaSdkOwner;
        }
        if (ownerBinaryName == null) {
            String jsDeclarationBinaryName = classContext.jsDeclarationClass(className);
            if (jsDeclarationBinaryName != null && !jsDeclarationBinaryName.isBlank()) {
                ownerBinaryName = jsDeclarationBinaryName;
                preserveOwnerBinaryName = true;
            }
        }
        if (ownerBinaryName == null && classContext.isLocalDeclarationName(className)) {
            String localDeclarationBinaryName = classContext.jsDeclarationClass(className);
            if (localDeclarationBinaryName == null || localDeclarationBinaryName.isBlank()) {
                localDeclarationBinaryName = classContext.jsDeclarationClass(generatedSimpleClassName(className));
            }
            ownerBinaryName = localDeclarationBinaryName == null || localDeclarationBinaryName.isBlank()
                    ? canonicalDeclarationBinaryName(className)
                    : localDeclarationBinaryName;
            preserveOwnerBinaryName = true;
        }
        if (ownerBinaryName == null && QinJavaSdkAliasSupport.isKnownAlias(className)) {
            ownerBinaryName = QinJavaSdkAliasSupport.canonicalBinaryName(className);
        }
        if (ownerBinaryName == null) {
            ownerBinaryName = javaImportLookup.get(className);
        }
        if (ownerBinaryName == null || ownerBinaryName.isBlank()) {
            return null;
        }
        if (!preserveOwnerBinaryName) {
            ownerBinaryName = canonicalDeclarationBinaryName(ownerBinaryName);
        }
        return new com.qin.lang.ir.QinIrJavaNewExpression(
                className,
                ownerBinaryName,
                lowerDeclarationCallArguments(arguments, javaImportLookup, classContext, locals));
    }

    private String nativeJavaSdkCollectionOwnerBinaryName(String className) {
        return switch (className) {
            case "Set" -> "com.qin.lang.runtime.JavaEsmSetObject";
            case "Map" -> "com.qin.lang.runtime.JavaEsmMapObject";
            case "__QinJavaUtilHashMap" -> "java.util.HashMap";
            case "__QinJavaUtilLinkedHashMap" -> "java.util.LinkedHashMap";
            case "__QinJavaUtilIdentityHashMap" -> "java.util.IdentityHashMap";
            default -> null;
        };
    }

    private static String generatedSimpleClassName(String className) {
        if (className == null || className.isBlank()) {
            return className;
        }
        int underscore = className.lastIndexOf('_');
        int dot = className.lastIndexOf('.');
        int split = Math.max(underscore, dot);
        if (split < 0 || split + 1 >= className.length()) {
            return className;
        }
        return className.substring(split + 1);
    }

    private static String canonicalDeclarationBinaryName(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            return binaryName;
        }
        if (binaryName.startsWith("__Qin")) {
            return binaryName;
        }
        String canonical = QinJavaSdkAliasSupport.canonicalBinaryName(binaryName);
        if (!binaryName.equals(canonical)) {
            return canonical;
        }
        if (binaryName.indexOf('_') < 0) {
            return canonical;
        }
        String dottedCandidate = binaryName.replace('_', '.');
        if (dottedCandidate.contains("..")) {
            return binaryName;
        }
        canonical = QinJavaSdkAliasSupport.canonicalBinaryName(dottedCandidate);
        return Objects.equals(dottedCandidate, canonical) ? binaryName : canonical;
    }

    private QinIrExpression lowerDeclarationSequenceExpression(
            Object sequenceExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<?> expressions;
        if (sequenceExpressionAst instanceof SequenceExpression sequenceExpression) {
            expressions = sequenceExpression.expressions();
        } else {
            expressions = QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(sequenceExpressionAst, "expressions"),
                    "SequenceExpression.expressions");
        }
        if (expressions == null || expressions.isEmpty()) {
            throw qjsError("QJS2001", "SequenceExpression must contain at least one expression");
        }
        List<QinIrExpression> lowered = new ArrayList<>(expressions.size());
        for (Object expression : expressions) {
            lowered.add(lowerDeclarationExpression(expression, javaImportLookup, classContext, locals));
        }
        return sequenceFromLoweredExpressions(lowered);
    }

    private QinIrExpression sequenceFromLoweredExpressions(List<QinIrExpression> lowered) {
        if (lowered.size() == 1) {
            return lowered.get(0);
        }
        return new QinIrSequenceExpression(
                lowered.subList(0, lowered.size() - 1),
                lowered.get(lowered.size() - 1));
    }

    private QinIrArrayLiteral lowerDeclarationArrayLiteral(
            com.slime.ast.nodes.expressions.ArrayExpression arrayExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<QinIrExpression> elements = new ArrayList<>();
        for (Object element : arrayExpression.elements()) {
            elements.add(element == null
                    ? new QinIrNullLiteral()
                    : lowerDeclarationExpression(element, javaImportLookup, classContext, locals));
        }
        return new QinIrArrayLiteral(elements);
    }

    private QinIrArrayLiteral lowerDeclarationArrayLiteral(
            Object arrayExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<?> elementsAst = QinSlimeFrontendAdapter.asListStatic(
                QinSlimeFrontendAdapter.invokeByName(arrayExpressionAst, "elements"),
                "ArrayExpression.elements");
        List<QinIrExpression> elements = new ArrayList<>();
        for (Object element : elementsAst) {
            elements.add(element == null
                    ? new QinIrNullLiteral()
                    : lowerDeclarationExpression(element, javaImportLookup, classContext, locals));
        }
        return new QinIrArrayLiteral(elements);
    }

    private QinIrBuiltinCallExpression lowerDeclarationUnaryExpression(
            Object unaryExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        String operator = QinSlimeFrontendAdapter.asStringStatic(
                QinSlimeFrontendAdapter.invokeByName(unaryExpression, "operator"),
                "UnaryExpression.operator");
        Object argumentAst = QinSlimeFrontendAdapter.invokeByName(unaryExpression, "argument");
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_unary__",
                List.of(
                        new QinIrStringLiteral(operator),
                        lowerDeclarationExpression(argumentAst, javaImportLookup, classContext, locals)));
    }

    private QinIrAssignmentExpression lowerDeclarationAssignmentExpression(
            AssignmentExpression assignmentExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        return new QinIrAssignmentExpression(
                lowerDeclarationAssignableExpression(assignmentExpression.left(), javaImportLookup, classContext, locals),
                assignmentExpression.operator(),
                lowerDeclarationExpression(assignmentExpression.right(), javaImportLookup, classContext, locals));
    }

    private QinIrAssignmentExpression lowerDeclarationAssignmentExpression(
            Object assignmentExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object leftAst = QinSlimeFrontendAdapter.invokeByName(assignmentExpression, "left");
        Object rightAst = QinSlimeFrontendAdapter.invokeByName(assignmentExpression, "right");
        String operator = QinSlimeFrontendAdapter.asStringStatic(
                QinSlimeFrontendAdapter.invokeByName(assignmentExpression, "operator"),
                "AssignmentExpression.operator");
        return new QinIrAssignmentExpression(
                lowerDeclarationAssignableExpression(leftAst, javaImportLookup, classContext, locals),
                operator,
                lowerDeclarationExpression(rightAst, javaImportLookup, classContext, locals));
    }

    private QinIrUpdateExpression lowerDeclarationUpdateExpression(
            Object updateExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object argumentAst = QinSlimeFrontendAdapter.invokeByName(updateExpression, "argument");
        String operator = QinSlimeFrontendAdapter.asStringStatic(
                QinSlimeFrontendAdapter.invokeByName(updateExpression, "operator"),
                "UpdateExpression.operator");
        boolean prefix = Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(updateExpression, "prefix"));
        return new QinIrUpdateExpression(
                lowerDeclarationAssignableExpression(argumentAst, javaImportLookup, classContext, locals),
                operator,
                prefix);
    }

    private QinIrExpression lowerDeclarationAssignableExpression(
            Object expression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (expression instanceof ParenthesizedExpression parenthesizedExpression) {
            return lowerDeclarationAssignableExpression(
                    parenthesizedExpression.expression(),
                    javaImportLookup,
                    classContext,
                    locals);
        }
        String nodeType = QinSlimeFrontendAdapter.simpleName(expression);
        if ("ParenthesizedExpression".equals(nodeType)) {
            return lowerDeclarationAssignableExpression(
                    QinSlimeFrontendAdapter.invokeByName(expression, "expression"),
                    javaImportLookup,
                    classContext,
                    locals);
        }
        if ("TSAsExpression".equals(nodeType)
                || "TSSatisfiesExpression".equals(nodeType)
                || "TSNonNullExpression".equals(nodeType)) {
            return lowerDeclarationAssignableExpression(
                    QinSlimeFrontendAdapter.invokeByName(expression, "expression"),
                    javaImportLookup,
                    classContext,
                    locals);
        }
        if ("Identifier".equals(nodeType)) {
            String identifierName = QinSlimeFrontendAdapter.extractIdentifierNameStatic(expression, "Identifier");
            return new QinIrIdentifierReference(identifierName);
        }
        if ("ThisExpression".equals(nodeType)) {
            return new QinIrThisExpression();
        }
        if ("MemberExpression".equals(nodeType)) {
            Object objectAst = QinSlimeFrontendAdapter.invokeByName(expression, "object");
            Object propertyAst = QinSlimeFrontendAdapter.invokeByName(expression, "property");
            if (Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(expression, "computed"))) {
                return new QinIrElementAccessExpression(
                        lowerDeclarationExpression(objectAst, javaImportLookup, classContext, locals),
                        lowerDeclarationExpression(propertyAst, javaImportLookup, classContext, locals));
            }
            String propertyName = adapter.extractMemberPropertyName(propertyAst);
            if ("Identifier".equals(QinSlimeFrontendAdapter.simpleName(objectAst))) {
                return new QinIrMemberAccessExpression(
                        QinSlimeFrontendAdapter.extractIdentifierNameStatic(objectAst, "Identifier"),
                        propertyName);
            }
            return new QinIrPropertyAccessExpression(
                    lowerDeclarationAssignableExpression(objectAst, javaImportLookup, classContext, locals),
                    propertyName);
        }
        if ("ArrayExpression".equals(nodeType)) {
            return lowerDeclarationExpression(expression, javaImportLookup, classContext, locals);
        }
        throw qjsError("QJS2022", "Unsupported declaration assignment/update target");
    }

    private QinIrExpression lowerDeclarationObjectLiteral(
            Object objectExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<?> properties = QinSlimeFrontendAdapter.asListStatic(
                QinSlimeFrontendAdapter.invokeByName(objectExpressionAst, "properties"),
                "ObjectExpression.properties");
        List<QinIrExpression> segments = new ArrayList<>();
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (Object property : properties) {
            if ("SpreadElement".equals(QinSlimeFrontendAdapter.simpleName(property))) {
                flushDeclarationObjectLiteralSegment(segments, irProperties);
                QinIrExpression spreadValue = lowerDeclarationExpression(
                        QinSlimeFrontendAdapter.invokeByName(property, "argument"),
                        javaImportLookup,
                        classContext,
                        locals);
                validateDeclarationObjectPropertyValue(spreadValue);
                segments.add(spreadValue);
                continue;
            }
            if (!"Property".equals(QinSlimeFrontendAdapter.simpleName(property))) {
                throw qjsError("QJS2019", "Only normal object property is supported in declaration subset");
            }
            String key = adapter.extractPropertyKey(QinSlimeFrontendAdapter.invokeByName(property, "key"));
            QinIrExpression value = lowerDeclarationExpression(
                    QinSlimeFrontendAdapter.invokeByName(property, "value"),
                    javaImportLookup,
                    classContext,
                    locals);
            validateDeclarationObjectPropertyValue(value);
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        flushDeclarationObjectLiteralSegment(segments, irProperties);
        return buildDeclarationObjectSpreadExpression(segments);
    }

    private QinIrExpression lowerDeclarationObjectLiteral(
            ObjectExpression objectExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<QinIrExpression> segments = new ArrayList<>();
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (AstNode propertyNode : objectExpressionAst.properties()) {
            if (propertyNode instanceof SpreadElement spreadElement) {
                flushDeclarationObjectLiteralSegment(segments, irProperties);
                QinIrExpression spreadValue =
                        lowerDeclarationExpression(spreadElement.argument(), javaImportLookup, classContext, locals);
                validateDeclarationObjectPropertyValue(spreadValue);
                segments.add(spreadValue);
                continue;
            }
            if (!(propertyNode instanceof Property property)) {
                throw qjsError("QJS2019", "Only normal object property is supported in declaration subset");
            }
            String key = adapter.extractPropertyKey(property.key());
            QinIrExpression value = lowerDeclarationExpression(property.value(), javaImportLookup, classContext, locals);
            validateDeclarationObjectPropertyValue(value);
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        flushDeclarationObjectLiteralSegment(segments, irProperties);
        return buildDeclarationObjectSpreadExpression(segments);
    }

    private void flushDeclarationObjectLiteralSegment(
            List<QinIrExpression> segments,
            List<QinIrObjectProperty> currentProperties) {
        if (currentProperties.isEmpty()) {
            return;
        }
        segments.add(new QinIrObjectLiteral(new ArrayList<>(currentProperties)));
        currentProperties.clear();
    }

    private QinIrExpression buildDeclarationObjectSpreadExpression(List<QinIrExpression> segments) {
        if (segments.isEmpty()) {
            return new QinIrObjectLiteral(List.of());
        }
        if (segments.size() == 1 && segments.get(0) instanceof QinIrObjectLiteral objectLiteral) {
            return objectLiteral;
        }
        QinIrExpression merged = new QinIrObjectLiteral(List.of());
        for (QinIrExpression segment : segments) {
            merged = new QinIrBuiltinCallExpression("Object", "assign", List.of(merged, segment));
        }
        return merged;
    }

    private void validateDeclarationObjectPropertyValue(QinIrExpression value) {
        if (value instanceof QinIrNumberLiteral
                || value instanceof QinIrStringLiteral
                || value instanceof QinIrBooleanLiteral
                || value instanceof QinIrNullLiteral
                || value instanceof QinIrIdentifierReference
                || value instanceof QinIrJavaClassLiteralExpression
                || value instanceof QinIrMemberAccessExpression
                || value instanceof QinIrPropertyAccessExpression
                || value instanceof QinIrElementAccessExpression
                || value instanceof QinIrInstanceMethodCallExpression
                || value instanceof QinIrStaticMethodCallExpression
                || value instanceof QinIrBuiltinCallExpression
                || value instanceof QinIrShortCircuitExpression
                || value instanceof QinIrArrayLiteral
                || value instanceof QinIrObjectLiteral) {
            return;
        }
        throw qjsError("QJS2020", "Unsupported declaration object property value expression");
    }

    private QinIrExpression lowerDeclarationBinaryExpression(
            BinaryExpression expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression left = lowerDeclarationExpression(expressionAst.left(), javaImportLookup, classContext, locals);
        QinIrExpression right = lowerDeclarationExpression(expressionAst.right(), javaImportLookup, classContext, locals);
        if ("instanceof".equals(expressionAst.operator())) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_instanceof__",
                    List.of(left, right));
        }
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), left, right));
    }

    private QinIrExpression lowerDeclarationBinaryExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        String operator = QinSlimeFrontendAdapter.asStringStatic(
                QinSlimeFrontendAdapter.invokeByName(expressionAst, "operator"),
                "BinaryExpression.operator");
        Object leftAst = QinSlimeFrontendAdapter.invokeByName(expressionAst, "left");
        Object rightAst = QinSlimeFrontendAdapter.invokeByName(expressionAst, "right");
        QinIrExpression left = lowerDeclarationExpression(leftAst, javaImportLookup, classContext, locals);
        QinIrExpression right = lowerDeclarationExpression(rightAst, javaImportLookup, classContext, locals);
        if ("instanceof".equals(operator)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_instanceof__",
                    List.of(left, right));
        }
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(operator), left, right));
    }

    private QinIrExpression lowerDeclarationLogicalExpression(
            LogicalExpression expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression left = lowerDeclarationExpression(expressionAst.left(), javaImportLookup, classContext, locals);
        QinIrExpression right = lowerDeclarationExpression(expressionAst.right(), javaImportLookup, classContext, locals);
        return new QinIrShortCircuitExpression(left, expressionAst.operator(), right);
    }

    private QinIrExpression lowerDeclarationLogicalExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        String operator = QinSlimeFrontendAdapter.asStringStatic(
                QinSlimeFrontendAdapter.invokeByName(expressionAst, "operator"),
                "LogicalExpression.operator");
        Object leftAst = QinSlimeFrontendAdapter.invokeByName(expressionAst, "left");
        Object rightAst = QinSlimeFrontendAdapter.invokeByName(expressionAst, "right");
        QinIrExpression left = lowerDeclarationExpression(leftAst, javaImportLookup, classContext, locals);
        QinIrExpression right = lowerDeclarationExpression(rightAst, javaImportLookup, classContext, locals);
        return new QinIrShortCircuitExpression(left, operator, right);
    }

    private QinIrBuiltinCallExpression lowerDeclarationConditionalExpression(
            ConditionalExpression expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression test = lowerDeclarationExpression(expressionAst.test(), javaImportLookup, classContext, locals);
        QinIrExpression consequent =
                lowerDeclarationExpression(expressionAst.consequent(), javaImportLookup, classContext, locals);
        QinIrExpression alternate =
                lowerDeclarationExpression(expressionAst.alternate(), javaImportLookup, classContext, locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
    }

    private QinIrBuiltinCallExpression lowerDeclarationConditionalExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object testAst = QinSlimeFrontendAdapter.invokeByName(expressionAst, "test");
        Object consequentAst = QinSlimeFrontendAdapter.invokeByName(expressionAst, "consequent");
        Object alternateAst = QinSlimeFrontendAdapter.invokeByName(expressionAst, "alternate");
        QinIrExpression test = lowerDeclarationExpression(testAst, javaImportLookup, classContext, locals);
        QinIrExpression consequent = lowerDeclarationExpression(consequentAst, javaImportLookup, classContext, locals);
        QinIrExpression alternate = lowerDeclarationExpression(alternateAst, javaImportLookup, classContext, locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
    }

    private QinIrExpression lowerDeclarationMemberAccessExpression(
            MemberExpression memberExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (isThisConstructorNameAccess(memberExpressionAst)) {
            return new QinIrStringLiteral(classContext.className());
        }
        if (memberExpressionAst.computed()) {
            QinIrExpression receiver = lowerDeclarationReceiver(
                    memberExpressionAst.object(),
                    javaImportLookup,
                    classContext,
                    locals);
            QinIrExpression property = lowerDeclarationExpression(
                    memberExpressionAst.property(),
                    javaImportLookup,
                    classContext,
                    locals);
            return new QinIrElementAccessExpression(receiver, property);
        }
        if (currentDeclarationStatic && isThisExpressionLike(memberExpressionAst.object())) {
            String propertyName = adapter.extractMemberPropertyName(memberExpressionAst.property());
            QinIrFieldDeclaration staticField = classContext.fields().get(propertyName);
            if (staticField != null && staticField.staticField()) {
                return new QinIrMemberAccessExpression(classContext.className(), propertyName);
            }
            throw qjsError("QJS2018", "Static declaration this.member access only supports static fields");
        }
        String propertyName = adapter.extractMemberPropertyName(memberExpressionAst.property());
        QinIrExpression fixedObjectProperty = fixedObjectPropertyAccessOrNull(
                memberExpressionAst.object(),
                propertyName,
                javaImportLookup,
                classContext,
                locals);
        if (fixedObjectProperty != null) {
            return fixedObjectProperty;
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                memberExpressionAst.object(),
                javaImportLookup,
                classContext,
                locals);
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            return new QinIrMemberAccessExpression(identifierReference.name(), propertyName);
        }
        return new QinIrPropertyAccessExpression(receiver, propertyName);
    }

    private QinIrExpression lowerDeclarationMemberAccessExpression(
            Object memberExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (isThisConstructorNameAccess(memberExpressionAst)) {
            return new QinIrStringLiteral(classContext.className());
        }
        boolean computed = Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "computed"));
        if (computed) {
            QinIrExpression receiver = lowerDeclarationReceiver(
                    QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "object"),
                    javaImportLookup,
                    classContext,
                    locals);
            QinIrExpression property = lowerDeclarationExpression(
                    QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "property"),
                    javaImportLookup,
                    classContext,
                    locals);
            return new QinIrElementAccessExpression(receiver, property);
        }
        if (currentDeclarationStatic && isThisExpressionLike(QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "object"))) {
            String propertyName = adapter.extractMemberPropertyName(
                    QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "property"));
            QinIrFieldDeclaration staticField = classContext.fields().get(propertyName);
            if (staticField != null && staticField.staticField()) {
                return new QinIrMemberAccessExpression(classContext.className(), propertyName);
            }
            throw qjsError("QJS2018", "Static declaration this.member access only supports static fields");
        }
        Object objectAst = QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "object");
        String propertyName = adapter.extractMemberPropertyName(
                QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "property"));
        QinIrExpression fixedObjectProperty = fixedObjectPropertyAccessOrNull(
                objectAst,
                propertyName,
                javaImportLookup,
                classContext,
                locals);
        if (fixedObjectProperty != null) {
            return fixedObjectProperty;
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                objectAst,
                javaImportLookup,
                classContext,
                locals);
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            return new QinIrMemberAccessExpression(identifierReference.name(), propertyName);
        }
        return new QinIrPropertyAccessExpression(receiver, propertyName);
    }

    private QinIrExpression fixedObjectPropertyAccessOrNull(
            Object receiverAst,
            String propertyName,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (propertyName == null || propertyName.isBlank()) {
            return null;
        }
        String receiverName = declarationIdentifierName(unwrapStaticExpressionEnvelope(receiverAst));
        if (receiverName != null && QinJavaSdkAliasSupport.isKnownAlias(receiverName)) {
            return null;
        }
        QinIrObjectLiteral objectLiteral = fixedObjectLiteralExpressionOrNull(
                receiverAst,
                javaImportLookup,
                classContext,
                locals);
        if (objectLiteral != null) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                if (propertyName.equals(property.key())) {
                    return property.value();
                }
            }
        }
        QinIrExpression exportSlotReceiver = staticExportSlotModuleBindingOrNull(receiverAst, classContext);
        if (exportSlotReceiver != null) {
            return new QinIrPropertyAccessExpression(exportSlotReceiver, propertyName);
        }
        return null;
    }

    private QinIrExpression staticExportSlotModuleBindingOrNull(
            Object receiverAst,
            DeclarationClassContext classContext) {
        Object receiver = unwrapStaticExpressionEnvelope(receiverAst);
        String identifierName = declarationIdentifierName(receiver);
        if (identifierName == null) {
            return null;
        }
        QinIrExpression moduleBinding = classContext.moduleBinding(identifierName);
        return isStaticExportSlotGetExpression(moduleBinding) ? moduleBinding : null;
    }

    private boolean isStaticExportSlotGetExpression(QinIrExpression expression) {
        if (!(expression instanceof QinIrBuiltinCallExpression exportGet)
                || !"Global".equals(exportGet.receiverName())
                || !"__qin_export_get__".equals(exportGet.methodName())
                || exportGet.arguments().size() != 1) {
            return false;
        }
        QinIrExpression slot = exportGet.arguments().get(0);
        return slot instanceof QinIrBuiltinCallExpression globalGet
                && "Global".equals(globalGet.receiverName())
                && "__qin_global__".equals(globalGet.methodName())
                && globalGet.arguments().size() == 1
                && globalGet.arguments().get(0) instanceof QinIrStringLiteral slotName
                && slotName.value() != null
                && slotName.value().startsWith("__qesm_m")
                && slotName.value().contains("_e_");
    }

    private QinIrObjectLiteral fixedObjectLiteralExpressionOrNull(
            Object receiverAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object receiver = unwrapStaticExpressionEnvelope(receiverAst);
        String identifierName = declarationIdentifierName(receiver);
        QinIrExpression candidate = null;
        if (identifierName != null) {
            candidate = locals.get(identifierName);
            if (candidate == null) {
                candidate = classContext.moduleBinding(identifierName);
            }
        } else if (receiver instanceof ObjectExpression
                || "ObjectExpression".equals(QinSlimeFrontendAdapter.simpleName(receiver))) {
            candidate = lowerDeclarationExpression(receiver, javaImportLookup, classContext, locals);
        }
        String exportSlotName = staticExportSlotNameOrNull(candidate);
        if (exportSlotName != null) {
            QinIrExpression staticExportValue = classContext.staticExportSlotValue(exportSlotName);
            if (staticExportValue instanceof QinIrObjectLiteral objectLiteral) {
                return objectLiteral;
            }
        }
        return candidate instanceof QinIrObjectLiteral objectLiteral ? objectLiteral : null;
    }

    private String staticExportSlotNameOrNull(QinIrExpression expression) {
        if (!(expression instanceof QinIrBuiltinCallExpression exportGet)
                || !"Global".equals(exportGet.receiverName())
                || !"__qin_export_get__".equals(exportGet.methodName())
                || exportGet.arguments().size() != 1) {
            return null;
        }
        return exportSlotNameOrNull(exportGet.arguments().get(0));
    }

    private String exportSlotNameOrNull(QinIrExpression slot) {
        if (slot instanceof QinIrBuiltinCallExpression globalGet
                && "Global".equals(globalGet.receiverName())
                && "__qin_global__".equals(globalGet.methodName())
                && globalGet.arguments().size() == 1
                && globalGet.arguments().get(0) instanceof QinIrStringLiteral slotName
                && slotName.value() != null
                && slotName.value().startsWith("__qesm_m")
                && slotName.value().contains("_e_")) {
            return slotName.value();
        }
        if (slot instanceof QinIrStringLiteral slotName
                && slotName.value() != null
                && slotName.value().startsWith("__qesm_m")
                && slotName.value().contains("_e_")) {
            return slotName.value();
        }
        return null;
    }

    private Object unwrapStaticExpressionEnvelope(Object expression) {
        Object current = unwrapParenthesized(expression);
        while ("TSAsExpression".equals(QinSlimeFrontendAdapter.simpleName(current))
                || "TSSatisfiesExpression".equals(QinSlimeFrontendAdapter.simpleName(current))
                || "TSNonNullExpression".equals(QinSlimeFrontendAdapter.simpleName(current))) {
            current = unwrapParenthesized(QinSlimeFrontendAdapter.invokeByName(current, "expression"));
        }
        return current;
    }

    private boolean isThisConstructorNameAccess(Object expressionAst) {
        if (expressionAst instanceof MemberExpression memberExpression) {
            if (memberExpression.computed()
                    || !"name".equals(adapter.extractMemberPropertyName(memberExpression.property()))
                    || !(memberExpression.object() instanceof MemberExpression constructorAccess)) {
                return false;
            }
            return !constructorAccess.computed()
                    && constructorAccess.object() instanceof ThisExpression
                    && "constructor".equals(adapter.extractMemberPropertyName(constructorAccess.property()));
        }
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))
                || Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(expressionAst, "computed"))) {
            return false;
        }
        String propertyName = adapter.extractMemberPropertyName(
                QinSlimeFrontendAdapter.invokeByName(expressionAst, "property"));
        if (!"name".equals(propertyName)) {
            return false;
        }
        Object receiver = QinSlimeFrontendAdapter.invokeByName(expressionAst, "object");
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(receiver))
                || Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(receiver, "computed"))) {
            return false;
        }
        Object receiverObject = QinSlimeFrontendAdapter.invokeByName(receiver, "object");
        String receiverPropertyName = adapter.extractMemberPropertyName(
                QinSlimeFrontendAdapter.invokeByName(receiver, "property"));
        return "ThisExpression".equals(QinSlimeFrontendAdapter.simpleName(receiverObject))
                && "constructor".equals(receiverPropertyName);
    }

    private QinIrExpression lowerDeclarationCallExpression(
            CallExpression callExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression staticIife = lowerStaticZeroArgumentIifeOrNull(
                callExpression.callee(),
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
        if (staticIife != null) {
            return staticIife;
        }
        QinIrExpression expressionIife = lowerStaticExpressionIifeOrNull(
                callExpression.callee(),
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
        if (expressionIife != null) {
            return expressionIife;
        }
        QinIrExpression generatedDelegation = lowerGeneratedConstructorDelegationIifeOrNull(
                callExpression.callee(),
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
        if (generatedDelegation != null) {
            return generatedDelegation;
        }
        QinIrExpression staticArrayFromNullFactory = lowerStaticArrayFromConstantFactoryCallOrNull(
                callExpression.callee(),
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
        if (staticArrayFromNullFactory != null) {
            return staticArrayFromNullFactory;
        }
        String calleeIdentifierName = declarationIdentifierName(callExpression.callee());
        if ("String".equals(calleeIdentifierName)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_string__",
                    lowerDeclarationCallArguments(
                            List.copyOf(callExpression.arguments()),
                            javaImportLookup,
                            classContext,
                            locals));
        }
        if ("Number".equals(calleeIdentifierName)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_number__",
                    lowerDeclarationCallArguments(
                            List.copyOf(callExpression.arguments()),
                            javaImportLookup,
                            classContext,
                            locals));
        }
        QinIrExpression directJavaFunctionalCall = lowerDirectJavaFunctionalCallOrNull(
                calleeIdentifierName,
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
        if (directJavaFunctionalCall != null) {
            return directJavaFunctionalCall;
        }
        if (calleeIdentifierName != null
                && isQinGlobalBuiltinFunction(calleeIdentifierName)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    calleeIdentifierName,
                    lowerDeclarationCallArguments(
                            List.copyOf(callExpression.arguments()),
                            javaImportLookup,
                            classContext,
                            locals));
        }
        QinIrExpression nativeModuleHelperCall = lowerNativeGeneratedModuleHelperCallOrNull(
                calleeIdentifierName,
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
        if (nativeModuleHelperCall != null) {
            return nativeModuleHelperCall;
        }
        if (calleeIdentifierName != null && !isUnsupportedDeclarationGlobalIdentifier(calleeIdentifierName)) {
            List<QinIrExpression> arguments = new ArrayList<>();
            arguments.add(lowerDeclarationExpression(callExpression.callee(), javaImportLookup, classContext, locals));
            arguments.addAll(lowerDeclarationCallArguments(
                    List.copyOf(callExpression.arguments()),
                    javaImportLookup,
                    classContext,
                    locals));
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_call__",
                    arguments);
        }
        if (!(callExpression.callee() instanceof MemberExpression memberExpression)) {
            throw qjsError(
                    "QJS2016",
                    "Only member call expressions are supported in declaration subset; callee="
                            + describeAstNode(callExpression.callee()));
        }
        if (memberExpression.computed()) {
            throw qjsError("QJS2017", "Computed method calls are not supported in declaration subset");
        }
        String methodName = adapter.extractMemberPropertyName(memberExpression.property());
        List<QinIrExpression> arguments = lowerDeclarationCallArguments(
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
        String builtinReceiverName = declarationIdentifierName(memberExpression.object());
        String nativeJavaSdkStaticMethod = nativeJavaSdkStaticMethodName(builtinReceiverName, methodName);
        if (nativeJavaSdkStaticMethod != null) {
            return new QinIrStaticMethodCallExpression(
                    builtinReceiverName,
                    "com.qin.lang.runtime.JavaEsmGlobal",
                    nativeJavaSdkStaticMethod,
                    arguments);
        }
        String javaSdkAliasStaticOwner = javaSdkAliasStaticOwnerOrNull(builtinReceiverName, methodName);
        if (javaSdkAliasStaticOwner != null) {
            return new QinIrStaticMethodCallExpression(
                    builtinReceiverName,
                    javaSdkAliasStaticOwner,
                    methodName,
                    arguments);
        }
        if (builtinReceiverName != null
                && QinBuiltinRegistry.resolve(builtinReceiverName, methodName, arguments.size()).isPresent()) {
            return new QinIrBuiltinCallExpression(builtinReceiverName, methodName, arguments);
        }
        String nullStaticImportOwner = nullStaticImportOwnerOrNull(memberExpression.object(), methodName);
        if (nullStaticImportOwner != null) {
            return new QinIrStaticMethodCallExpression(
                    nullStaticImportOwner,
                    nullStaticImportOwner,
                    methodName,
                    arguments);
        }
        if ("Super".equals(QinSlimeFrontendAdapter.simpleName(memberExpression.object()))) {
            return new QinIrSuperMethodCallExpression(methodName, arguments);
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                memberExpression.object(),
                javaImportLookup,
                classContext,
                locals);
        if (receiver instanceof QinIrJavaClassLiteralExpression classLiteral) {
            return new QinIrStaticMethodCallExpression(
                    classLiteral.typeName(),
                    classLiteral.binaryName(),
                    methodName,
                    arguments);
        }
        if (receiver instanceof QinIrIdentifierReference identifierReference
                && javaImportLookup.containsKey(identifierReference.name())) {
            return new QinIrStaticMethodCallExpression(
                    identifierReference.name(),
                    javaImportLookup.get(identifierReference.name()),
                    methodName,
                    arguments);
        }
        if (receiver instanceof QinIrIdentifierReference identifierReference
                && QinBuiltinRegistry.resolve(identifierReference.name(), methodName, arguments.size()).isPresent()) {
            return new QinIrBuiltinCallExpression(identifierReference.name(), methodName, arguments);
        }
        if (receiver instanceof QinIrThisExpression
                && classContext.fields().containsKey(methodName)
                && classContext.findMethod(methodName, arguments.size()) == null) {
            List<QinIrExpression> callableArguments = new ArrayList<>();
            callableArguments.add(new QinIrPropertyAccessExpression(receiver, methodName));
            callableArguments.addAll(arguments);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_call__",
                    callableArguments);
        }
        String ownerBinaryName = declarationKnownThisMethodOwnerBinaryName(
                receiver,
                methodName,
                arguments.size(),
                classContext);
        if (ownerBinaryName == null) {
            ownerBinaryName = declarationInstanceMethodOwnerBinaryName(receiver, classContext);
        }
        String ascribedOwnerBinaryName = typeAscriptionOwnerBinaryNameOrNull(
                memberExpression.object(),
                javaImportLookup,
                classContext);
        ownerBinaryName = ascribedOwnerBinaryName == null ? ownerBinaryName : ascribedOwnerBinaryName;
        return ownerBinaryName == null
                ? new QinIrInstanceMethodCallExpression(receiver, methodName, arguments)
                : new QinIrInstanceMethodCallExpression(receiver, ownerBinaryName, methodName, arguments);
    }

    private QinIrExpression lowerDeclarationCallExpression(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object callee = QinSlimeFrontendAdapter.invokeByName(callExpressionAst, "callee");
        List<?> rawArguments = QinSlimeFrontendAdapter.asListStatic(
                QinSlimeFrontendAdapter.invokeByName(callExpressionAst, "arguments"),
                "CallExpression.arguments");
        QinIrExpression staticIife = lowerStaticZeroArgumentIifeOrNull(
                callee,
                rawArguments,
                javaImportLookup,
                classContext,
                locals);
        if (staticIife != null) {
            return staticIife;
        }
        QinIrExpression expressionIife = lowerStaticExpressionIifeOrNull(
                callee,
                rawArguments,
                javaImportLookup,
                classContext,
                locals);
        if (expressionIife != null) {
            return expressionIife;
        }
        QinIrExpression generatedDelegation = lowerGeneratedConstructorDelegationIifeOrNull(
                callee,
                rawArguments,
                javaImportLookup,
                classContext,
                locals);
        if (generatedDelegation != null) {
            return generatedDelegation;
        }
        QinIrExpression staticArrayFromNullFactory = lowerStaticArrayFromConstantFactoryCallOrNull(
                callee,
                rawArguments,
                javaImportLookup,
                classContext,
                locals);
        if (staticArrayFromNullFactory != null) {
            return staticArrayFromNullFactory;
        }
        String calleeIdentifierName = declarationIdentifierName(callee);
        if ("String".equals(calleeIdentifierName)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_string__",
                    lowerDeclarationCallArguments(
                            rawArguments,
                            javaImportLookup,
                            classContext,
                            locals));
        }
        if ("Number".equals(calleeIdentifierName)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_number__",
                    lowerDeclarationCallArguments(
                            rawArguments,
                            javaImportLookup,
                            classContext,
                            locals));
        }
        QinIrExpression directJavaFunctionalCall = lowerDirectJavaFunctionalCallOrNull(
                calleeIdentifierName,
                rawArguments,
                javaImportLookup,
                classContext,
                locals);
        if (directJavaFunctionalCall != null) {
            return directJavaFunctionalCall;
        }
        if (calleeIdentifierName != null && isQinGlobalBuiltinFunction(calleeIdentifierName)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    calleeIdentifierName,
                    lowerDeclarationCallArguments(
                            rawArguments,
                            javaImportLookup,
                            classContext,
                            locals));
        }
        QinIrExpression nativeModuleHelperCall = lowerNativeGeneratedModuleHelperCallOrNull(
                calleeIdentifierName,
                rawArguments,
                javaImportLookup,
                classContext,
                locals);
        if (nativeModuleHelperCall != null) {
            return nativeModuleHelperCall;
        }
        if (calleeIdentifierName != null && !isUnsupportedDeclarationGlobalIdentifier(calleeIdentifierName)) {
            List<QinIrExpression> arguments = new ArrayList<>();
            arguments.add(lowerDeclarationExpression(callee, javaImportLookup, classContext, locals));
            arguments.addAll(lowerDeclarationCallArguments(
                    rawArguments,
                    javaImportLookup,
                    classContext,
                    locals));
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_call__",
                    arguments);
        }
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(callee))) {
            throw qjsError(
                    "QJS2016",
                    "Only member call expressions are supported in declaration subset; callee="
                            + describeAstNode(callee));
        }
        boolean computed = Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(callee, "computed"));
        if (computed) {
            throw qjsError("QJS2017", "Computed method calls are not supported in declaration subset");
        }
        Object receiverAst = QinSlimeFrontendAdapter.invokeByName(callee, "object");
        String methodName = adapter.extractMemberPropertyName(
                QinSlimeFrontendAdapter.invokeByName(callee, "property"));
        List<QinIrExpression> arguments = lowerDeclarationCallArguments(
                rawArguments,
                javaImportLookup,
                classContext,
                locals);
        String builtinReceiverName = declarationIdentifierName(receiverAst);
        String nativeJavaSdkStaticMethod = nativeJavaSdkStaticMethodName(builtinReceiverName, methodName);
        if (nativeJavaSdkStaticMethod != null) {
            return new QinIrStaticMethodCallExpression(
                    builtinReceiverName,
                    "com.qin.lang.runtime.JavaEsmGlobal",
                    nativeJavaSdkStaticMethod,
                    arguments);
        }
        String javaSdkAliasStaticOwner = javaSdkAliasStaticOwnerOrNull(builtinReceiverName, methodName);
        if (javaSdkAliasStaticOwner != null) {
            return new QinIrStaticMethodCallExpression(
                    builtinReceiverName,
                    javaSdkAliasStaticOwner,
                    methodName,
                    arguments);
        }
        if (builtinReceiverName != null
                && QinBuiltinRegistry.resolve(builtinReceiverName, methodName, arguments.size()).isPresent()) {
            return new QinIrBuiltinCallExpression(builtinReceiverName, methodName, arguments);
        }
        String nullStaticImportOwner = nullStaticImportOwnerOrNull(receiverAst, methodName);
        if (nullStaticImportOwner != null) {
            return new QinIrStaticMethodCallExpression(
                    nullStaticImportOwner,
                    nullStaticImportOwner,
                    methodName,
                    arguments);
        }
        if ("Super".equals(QinSlimeFrontendAdapter.simpleName(receiverAst))) {
            return new QinIrSuperMethodCallExpression(methodName, arguments);
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                receiverAst,
                javaImportLookup,
                classContext,
                locals);
        if (receiver instanceof QinIrJavaClassLiteralExpression classLiteral) {
            return new QinIrStaticMethodCallExpression(
                    classLiteral.typeName(),
                    classLiteral.binaryName(),
                    methodName,
                    arguments);
        }
        if (receiver instanceof QinIrIdentifierReference identifierReference
                && javaImportLookup.containsKey(identifierReference.name())) {
            return new QinIrStaticMethodCallExpression(
                    identifierReference.name(),
                    javaImportLookup.get(identifierReference.name()),
                    methodName,
                    arguments);
        }
        if (receiver instanceof QinIrIdentifierReference identifierReference
                && QinBuiltinRegistry.resolve(identifierReference.name(), methodName, arguments.size()).isPresent()) {
            return new QinIrBuiltinCallExpression(identifierReference.name(), methodName, arguments);
        }
        if (receiver instanceof QinIrThisExpression
                && classContext.fields().containsKey(methodName)
                && classContext.findMethod(methodName, arguments.size()) == null) {
            List<QinIrExpression> callableArguments = new ArrayList<>();
            callableArguments.add(new QinIrPropertyAccessExpression(receiver, methodName));
            callableArguments.addAll(arguments);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_call__",
                    callableArguments);
        }
        String ownerBinaryName = declarationKnownThisMethodOwnerBinaryName(
                receiver,
                methodName,
                arguments.size(),
                classContext);
        if (ownerBinaryName == null) {
            ownerBinaryName = declarationInstanceMethodOwnerBinaryName(receiver, classContext);
        }
        String ascribedOwnerBinaryName = typeAscriptionOwnerBinaryNameOrNull(
                receiverAst,
                javaImportLookup,
                classContext);
        ownerBinaryName = ascribedOwnerBinaryName == null ? ownerBinaryName : ascribedOwnerBinaryName;
        return ownerBinaryName == null
                ? new QinIrInstanceMethodCallExpression(receiver, methodName, arguments)
                : new QinIrInstanceMethodCallExpression(receiver, ownerBinaryName, methodName, arguments);
    }

    private String typeAscriptionOwnerBinaryNameOrNull(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (!"TSAsExpression".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            return null;
        }
        Object typeAnnotation = QinSlimeFrontendAdapter.invokeByName(expressionAst, "typeAnnotation");
        QinIrTypeRef type = lowerTypeNodeRef(
                typeAnnotation,
                javaImportLookup,
                classContext == null ? Set.of() : classContext.localDeclarationNames(),
                Map.of());
        if (type == null
                || type.kind() != com.qin.lang.ir.QinIrTypeKind.CLASS
                || type.binaryName() == null
                || type.binaryName().isBlank()) {
            return null;
        }
        String binaryName = QinJavaSdkAliasSupport.canonicalBinaryName(type.binaryName());
        return "java.lang.Object".equals(binaryName) ? null : binaryName;
    }

    private String declarationInstanceMethodOwnerBinaryName(
            QinIrExpression receiver,
            DeclarationClassContext classContext) {
        QinIrTypeRef ownerType = null;
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            ownerType = currentDeclarationValueTypes.get(identifierReference.name());
        } else if (receiver instanceof QinIrJavaNewExpression javaNewExpression) {
            ownerType = QinIrTypeRef.classType(javaNewExpression.ownerBinaryName());
        } else if (receiver instanceof QinIrThisExpression && classContext != null) {
            ownerType = QinIrTypeRef.classType(classContext.className());
        }
        if (ownerType == null
                || ownerType.kind() != com.qin.lang.ir.QinIrTypeKind.CLASS
                || ownerType.binaryName() == null
                || ownerType.binaryName().isBlank()) {
            return null;
        }
        String binaryName = QinJavaSdkAliasSupport.canonicalBinaryName(ownerType.binaryName());
        return "java.lang.Object".equals(binaryName) ? null : binaryName;
    }

    private String declarationKnownThisMethodOwnerBinaryName(
            QinIrExpression receiver,
            String methodName,
            int argumentCount,
            DeclarationClassContext classContext) {
        if (!(receiver instanceof QinIrThisExpression)
                || classContext == null) {
            return null;
        }
        String ownerBinaryName = classContext.findMethod(methodName, argumentCount) == null
                ? declarationInheritedThisMethodOwnerBinaryName(
                        classContext.inheritedOverrideSuperType(),
                        methodName,
                        argumentCount,
                        classContext.localJvmDeclarations(),
                        new java.util.LinkedHashSet<>())
                : classContext.className();
        return ownerBinaryName == null || ownerBinaryName.isBlank() ? null : ownerBinaryName;
    }

    private String declarationInheritedThisMethodOwnerBinaryName(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount,
            Map<String, QinIrClassDeclaration> localJvmDeclarations,
            Set<String> visitedLocalTypes) {
        if (ownerType == null
                || ownerType.kind() != com.qin.lang.ir.QinIrTypeKind.CLASS
                || ownerType.binaryName() == null
                || ownerType.binaryName().isBlank()
                || methodName == null
                || methodName.isBlank()) {
            return null;
        }
        String ownerBinaryName = QinJavaSdkAliasSupport.canonicalBinaryName(ownerType.binaryName());
        QinIrClassDeclaration localDeclaration = localJvmDeclarations == null
                ? null
                : localJvmDeclarations.get(ownerBinaryName);
        if (localDeclaration != null) {
            if (!visitedLocalTypes.add(localDeclaration.binaryName())) {
                return null;
            }
            QinIrMethodDeclaration matched = null;
            for (QinIrMethodDeclaration candidate : localDeclaration.methods()) {
                if (candidate.staticMethod()
                        || !candidate.name().equals(methodName)
                        || candidate.parameters().size() != argumentCount) {
                    continue;
                }
                if (matched != null) {
                    throw qjsError("QJS2030", "Ambiguous inherited declaration method: "
                            + localDeclaration.binaryName() + "." + methodName + "/" + argumentCount);
                }
                matched = candidate;
            }
            if (matched != null) {
                return localDeclaration.binaryName();
            }
            return declarationInheritedThisMethodOwnerBinaryName(
                    localDeclaration.superType(),
                    methodName,
                    argumentCount,
                    localJvmDeclarations,
                    visitedLocalTypes);
        }
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Method matched = null;
            for (Method method : ownerClass.getMethods()) {
                if (!method.getName().equals(methodName) || method.getParameterCount() != argumentCount) {
                    continue;
                }
                if (matched != null) {
                    throw qjsError("QJS2030", "Ambiguous inherited Java method: "
                            + ownerBinaryName + "." + methodName + "/" + argumentCount);
                }
                matched = method;
            }
            return matched == null ? null : matched.getDeclaringClass().getName();
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    private QinIrExpression lowerDeclarationRuntimeFunctionExpression(
            Object functionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> locals) {
        QinIrObjectLiteral runtimeDefinition = adapter.lowerRequiredFunctionRuntimeDefinition(
                functionAst,
                QinSlimeFrontendAdapter.simpleName(functionAst),
                javaImportLookup,
                locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                List.of(withDeclarationValueClosure(runtimeDefinition, locals)));
    }

    private QinIrObjectLiteral withDeclarationValueClosure(
            QinIrObjectLiteral runtimeDefinition,
            Map<String, QinIrExpression> locals) {
        if (locals == null || locals.isEmpty()) {
            return runtimeDefinition;
        }
        List<QinIrObjectProperty> properties = new ArrayList<>();
        boolean replaced = false;
        for (QinIrObjectProperty property : runtimeDefinition.properties()) {
            if ("closure".equals(property.key())) {
                properties.add(new QinIrObjectProperty("closure", declarationValueClosure(locals)));
                replaced = true;
            } else {
                properties.add(property);
            }
        }
        if (!replaced) {
            properties.add(new QinIrObjectProperty("closure", declarationValueClosure(locals)));
        }
        return new QinIrObjectLiteral(properties);
    }

    private QinIrObjectLiteral declarationValueClosure(Map<String, QinIrExpression> locals) {
        List<String> names = new ArrayList<>(locals.keySet());
        names.sort(String::compareTo);
        List<QinIrObjectProperty> properties = new ArrayList<>();
        for (String name : names) {
            if (name == null
                    || name.isBlank()
                    || !QinParserRuntimeNames.IDENTIFIER_PATTERN.matcher(name).matches()) {
                continue;
            }
            properties.add(new QinIrObjectProperty(name, locals.get(name)));
        }
        return new QinIrObjectLiteral(properties);
    }

    private String nativeJavaSdkStaticMethodName(String receiverName, String methodName) {
        if (!"__QinJavaLangCharacter".equals(receiverName) || methodName == null) {
            return null;
        }
        return switch (methodName) {
            case "__char" -> "__qin_java_character_char__";
            case "isWhitespace" -> "__qin_java_character_is_whitespace__";
            case "isLetter" -> "__qin_java_character_is_letter__";
            case "isLetterOrDigit" -> "__qin_java_character_is_letter_or_digit__";
            case "isJavaIdentifierStart" -> "__qin_java_character_is_java_identifier_start__";
            case "toUpperCase" -> "__qin_java_character_to_upper_case__";
            case "toLowerCase" -> "__qin_java_character_to_lower_case__";
            case "charCount" -> "__qin_java_character_char_count__";
            default -> null;
        };
    }

    private String javaSdkAliasStaticOwnerOrNull(String receiverName, String methodName) {
        if (receiverName == null || methodName == null) {
            return null;
        }
        if ("__QinJavaUtilList".equals(receiverName) && "of".equals(methodName)) {
            return "java.util.List";
        }
        if ("__QinJavaUtilArrays".equals(receiverName) && "stream".equals(methodName)) {
            return "java.util.Arrays";
        }
        if ("__QinJavaLangInteger".equals(receiverName) && "getInteger".equals(methodName)) {
            return "java.lang.Integer";
        }
        if ("__QinJavaUtilOptional".equals(receiverName)
                && ("empty".equals(methodName) || "of".equals(methodName) || "ofNullable".equals(methodName))) {
            return "java.util.Optional";
        }
        if ("__QinJavaLangString".equals(receiverName)
                && ("join".equals(methodName) || "valueOf".equals(methodName))) {
            return "java.lang.String";
        }
        return null;
    }

    private QinIrExpression lowerNativeGeneratedModuleHelperCallOrNull(
            String helperName,
            List<?> rawArguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        String builtinName = nativeGeneratedModuleHelperBuiltinName(helperName);
        if (builtinName == null
                || rawArguments == null
                || classContext == null
                || !classContext.moduleBindings().containsKey(helperName)) {
            return null;
        }
        return new QinIrBuiltinCallExpression(
                "Global",
                builtinName,
                lowerDeclarationCallArguments(rawArguments, javaImportLookup, classContext, locals));
    }

    private String nativeGeneratedModuleHelperBuiltinName(String helperName) {
        if (helperName == null) {
            return null;
        }
        return switch (helperName) {
            case "tokenNameOf" -> "__qin_token_name_of__";
            case "tokenValueOf" -> "__qin_token_value_of__";
            case "tokenHasLineBreakBefore" -> "__qin_token_has_line_break_before__";
            case "tokenIndexOf" -> "__qin_token_index_of__";
            case "__qin_java_io_file_separator" -> "__qin_java_io_file_separator__";
            case "__qin_java_io_file_separator_char_code" -> "__qin_java_io_file_separator_char_code__";
            case "__qin_java_io_file_is_separator_code" -> "__qin_java_io_file_is_separator_code__";
            case "__qin_java_io_file_drive_prefix" -> "__qin_java_io_file_drive_prefix__";
            case "__qin_java_io_file_join" -> "__qin_java_io_file_join__";
            case "__qin_java_io_file_normalize" -> "__qin_java_io_file_normalize__";
            case "__qin_java_io_file_configured_exists" -> "__qin_java_io_file_configured_exists__";
            case "__qin_bytes" -> "__qin_java_io_bytes__";
            case "__qin_concat" -> "__qin_java_io_concat__";
            case "__qin_count" -> "__qin_java_io_count__";
            default -> null;
        };
    }

    private QinIrExpression lowerDirectJavaFunctionalCallOrNull(
            String calleeIdentifierName,
            List<?> rawArguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (!"__qin_java_functional".equals(calleeIdentifierName)
                || rawArguments == null
                || rawArguments.size() != 1) {
            return null;
        }
        QinIrExpression staticFunctionModel = lowerStaticJavaFunctionalFunctionModelOrNull(
                rawArguments.get(0),
                javaImportLookup,
                locals);
        if (staticFunctionModel != null) {
            return staticFunctionModel;
        }
        Object returnedExpression = directFunctionalReturnedExpression(rawArguments.get(0));
        if (!(returnedExpression instanceof CallExpression callExpression)
                || !(unwrapParenthesized(callExpression.callee()) instanceof MemberExpression memberExpression)
                || memberExpression.computed()
                || !(unwrapParenthesized(memberExpression.object()) instanceof ThisExpression)) {
            return null;
        }
        String methodName = adapter.extractMemberPropertyName(memberExpression.property());
        if (methodName == null || methodName.isBlank()) {
            return null;
        }
        List<QinIrExpression> boundArguments = lowerDeclarationCallArguments(
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_direct_method_function__",
                List.of(
                        new QinIrThisExpression(),
                        new QinIrStringLiteral(methodName),
                        new QinIrArrayLiteral(boundArguments)));
    }

    private QinIrExpression lowerStaticJavaFunctionalFunctionModelOrNull(
            Object functionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> locals) {
        Object function = unwrapParenthesized(functionAst);
        String nodeName = QinSlimeFrontendAdapter.simpleName(function);
        if (!(function instanceof FunctionExpression)
                && !(function instanceof ArrowFunctionExpression)
                && !"FunctionExpression".equals(nodeName)
                && !"ArrowFunctionExpression".equals(nodeName)) {
            return null;
        }
        Map<String, QinIrExpression> closureLocals = staticFunctionClosureLocals(function, locals);
        QinIrObjectLiteral runtimeDefinition = adapter.lowerRequiredFunctionRuntimeDefinition(
                function,
                nodeName,
                javaImportLookup,
                closureLocals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_java_functional",
                List.of(withDeclarationValueClosure(runtimeDefinition, closureLocals)));
    }

    private Map<String, QinIrExpression> staticFunctionClosureLocals(
            Object functionAst,
            Map<String, QinIrExpression> locals) {
        if (locals == null || locals.isEmpty()) {
            return Map.of();
        }
        Set<String> declared = new LinkedHashSet<>(functionParameterNames(functionAst));
        Set<String> referenced = new LinkedHashSet<>();
        Object body = functionBody(functionAst);
        if (!collectStaticFunctionReferences(body, declared, referenced)) {
            return locals;
        }
        Map<String, QinIrExpression> closureLocals = new LinkedHashMap<>();
        for (Map.Entry<String, QinIrExpression> entry : locals.entrySet()) {
            if (referenced.contains(entry.getKey())) {
                closureLocals.put(entry.getKey(), entry.getValue());
            }
        }
        return closureLocals;
    }

    private List<String> functionParameterNames(Object functionAst) {
        Object paramsObject = QinSlimeFrontendAdapter.invokeByName(functionAst, "params");
        List<?> params = QinSlimeFrontendAdapter.asListStatic(paramsObject, "Function.params");
        if (params.isEmpty()) {
            return List.of();
        }
        List<String> names = new ArrayList<>();
        for (Object param : params) {
            collectPatternBindingNames(param, names);
        }
        return names;
    }

    private Object functionBody(Object functionAst) {
        return QinSlimeFrontendAdapter.invokeByName(functionAst, "body");
    }

    private boolean collectStaticFunctionReferences(
            Object node,
            Set<String> declared,
            Set<String> referenced) {
        if (node == null) {
            return true;
        }
        Object unwrapped = unwrapParenthesized(node);
        if (unwrapped instanceof Identifier identifier) {
            if (!declared.contains(identifier.name())) {
                referenced.add(identifier.name());
            }
            return true;
        }
        String nodeName = QinSlimeFrontendAdapter.simpleName(unwrapped);
        if ("Identifier".equals(nodeName)) {
            String name = QinSlimeFrontendAdapter.extractIdentifierNameStatic(unwrapped, "Identifier");
            if (!declared.contains(name)) {
                referenced.add(name);
            }
            return true;
        }
        if (unwrapped instanceof Literal || unwrapped instanceof ThisExpression) {
            return true;
        }
        if (unwrapped instanceof FunctionExpression
                || unwrapped instanceof ArrowFunctionExpression
                || "FunctionExpression".equals(nodeName)
                || "ArrowFunctionExpression".equals(nodeName)) {
            return false;
        }
        if (unwrapped instanceof BlockStatement blockStatement) {
            return collectStaticFunctionReferencesFromList(blockStatement.body(), declared, referenced);
        }
        if (unwrapped instanceof ExpressionStatement expressionStatement) {
            return collectStaticFunctionReferences(expressionStatement.expression(), declared, referenced);
        }
        if (unwrapped instanceof ReturnStatement returnStatement) {
            return collectStaticFunctionReferences(returnStatement.argument(), declared, referenced);
        }
        if (unwrapped instanceof IfStatement ifStatement) {
            return collectStaticFunctionReferences(ifStatement.test(), declared, referenced)
                    && collectStaticFunctionReferences(ifStatement.consequent(), new LinkedHashSet<>(declared), referenced)
                    && collectStaticFunctionReferences(ifStatement.alternate(), new LinkedHashSet<>(declared), referenced);
        }
        if (unwrapped instanceof VariableDeclaration variableDeclaration) {
            return collectStaticVariableDeclarationReferences(variableDeclaration, declared, referenced);
        }
        if (unwrapped instanceof CallExpression callExpression) {
            return collectStaticFunctionReferences(callExpression.callee(), declared, referenced)
                    && collectStaticFunctionReferencesFromList(callExpression.arguments(), declared, referenced);
        }
        if (unwrapped instanceof MemberExpression memberExpression) {
            boolean ok = collectStaticFunctionReferences(memberExpression.object(), declared, referenced);
            if (memberExpression.computed()) {
                ok = ok && collectStaticFunctionReferences(memberExpression.property(), declared, referenced);
            }
            return ok;
        }
        if (unwrapped instanceof BinaryExpression binaryExpression) {
            return collectStaticFunctionReferences(binaryExpression.left(), declared, referenced)
                    && collectStaticFunctionReferences(binaryExpression.right(), declared, referenced);
        }
        if (unwrapped instanceof LogicalExpression logicalExpression) {
            return collectStaticFunctionReferences(logicalExpression.left(), declared, referenced)
                    && collectStaticFunctionReferences(logicalExpression.right(), declared, referenced);
        }
        if (unwrapped instanceof ConditionalExpression conditionalExpression) {
            return collectStaticFunctionReferences(conditionalExpression.test(), declared, referenced)
                    && collectStaticFunctionReferences(conditionalExpression.consequent(), declared, referenced)
                    && collectStaticFunctionReferences(conditionalExpression.alternate(), declared, referenced);
        }
        if (unwrapped instanceof AssignmentExpression assignmentExpression) {
            return collectStaticFunctionReferences(assignmentExpression.left(), declared, referenced)
                    && collectStaticFunctionReferences(assignmentExpression.right(), declared, referenced);
        }
        if (unwrapped instanceof com.slime.ast.nodes.expressions.ArrayExpression arrayExpression) {
            return collectStaticFunctionReferencesFromList(arrayExpression.elements(), declared, referenced);
        }
        if (unwrapped instanceof ObjectExpression objectExpression) {
            for (AstNode propertyNode : objectExpression.properties()) {
                if (!(propertyNode instanceof Property property)) {
                    return false;
                }
                if (property.computed()
                        && !collectStaticFunctionReferences(property.key(), declared, referenced)) {
                    return false;
                }
                if (!collectStaticFunctionReferences(property.value(), declared, referenced)) {
                    return false;
                }
            }
            return true;
        }
        return switch (nodeName) {
            case "BlockStatement" -> collectStaticFunctionReferencesFromList(
                    QinSlimeFrontendAdapter.asListStatic(
                            QinSlimeFrontendAdapter.invokeByName(unwrapped, "body"),
                            "BlockStatement.body"),
                    declared,
                    referenced);
            case "ParenthesizedExpression" -> collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "expression"),
                    declared,
                    referenced);
            case "ExpressionStatement" -> collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "expression"),
                    declared,
                    referenced);
            case "ReturnStatement" -> collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "argument"),
                    declared,
                    referenced);
            case "CallExpression" -> collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "callee"),
                    declared,
                    referenced)
                    && collectStaticFunctionReferencesFromList(
                    QinSlimeFrontendAdapter.asListStatic(
                            QinSlimeFrontendAdapter.invokeByName(unwrapped, "arguments"),
                            "CallExpression.arguments"),
                    declared,
                    referenced);
            case "VariableDeclaration" -> collectStaticVariableDeclarationReferences(unwrapped, declared, referenced);
            case "VariableDeclarator" -> collectStaticVariableDeclaratorReferences(unwrapped, declared, referenced);
            case "MemberExpression" -> collectStaticMemberReferences(unwrapped, declared, referenced);
            case "BinaryExpression", "LogicalExpression" -> collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "left"),
                    declared,
                    referenced)
                    && collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "right"),
                    declared,
                    referenced);
            case "ConditionalExpression" -> collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "test"),
                    declared,
                    referenced)
                    && collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "consequent"),
                    declared,
                    referenced)
                    && collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "alternate"),
                    declared,
                    referenced);
            case "IfStatement" -> collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "test"),
                    declared,
                    referenced)
                    && collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "consequent"),
                    new LinkedHashSet<>(declared),
                    referenced)
                    && collectStaticFunctionReferences(
                    QinSlimeFrontendAdapter.invokeByName(unwrapped, "alternate"),
                    new LinkedHashSet<>(declared),
                    referenced);
            default -> false;
        };
    }

    private boolean collectStaticVariableDeclarationReferences(
            Object variableDeclaration,
            Set<String> declared,
            Set<String> referenced) {
        List<?> declarations = variableDeclaration instanceof VariableDeclaration typed
                ? typed.declarations()
                : QinSlimeFrontendAdapter.asListStatic(
                        QinSlimeFrontendAdapter.invokeByName(variableDeclaration, "declarations"),
                        "VariableDeclaration.declarations");
        for (Object declarator : declarations) {
            if (!collectStaticVariableDeclaratorReferences(declarator, declared, referenced)) {
                return false;
            }
        }
        return true;
    }

    private boolean collectStaticVariableDeclaratorReferences(
            Object declarator,
            Set<String> declared,
            Set<String> referenced) {
        Object id = QinSlimeFrontendAdapter.invokeByName(declarator, "id");
        Object init = QinSlimeFrontendAdapter.invokeByName(declarator, "init");
        if (!collectStaticFunctionReferences(init, declared, referenced)) {
            return false;
        }
        List<String> names = new ArrayList<>();
        collectPatternBindingNames(id, names);
        declared.addAll(names);
        return true;
    }

    private boolean collectStaticMemberReferences(
            Object memberExpression,
            Set<String> declared,
            Set<String> referenced) {
        Object object = QinSlimeFrontendAdapter.invokeByName(memberExpression, "object");
        Object property = QinSlimeFrontendAdapter.invokeByName(memberExpression, "property");
        boolean computed = Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(memberExpression, "computed"));
        return collectStaticFunctionReferences(object, declared, referenced)
                && (!computed || collectStaticFunctionReferences(property, declared, referenced));
    }

    private boolean collectStaticFunctionReferencesFromList(
            List<?> nodes,
            Set<String> declared,
            Set<String> referenced) {
        if (nodes == null || nodes.isEmpty()) {
            return true;
        }
        for (Object node : nodes) {
            if (!collectStaticFunctionReferences(node, declared, referenced)) {
                return false;
            }
        }
        return true;
    }

    private void collectPatternBindingNames(Object pattern, List<String> names) {
        Object unwrapped = unwrapParenthesized(pattern);
        if (unwrapped instanceof Identifier identifier) {
            names.add(identifier.name());
            return;
        }
        String nodeName = QinSlimeFrontendAdapter.simpleName(unwrapped);
        if ("Identifier".equals(nodeName)) {
            names.add(QinSlimeFrontendAdapter.extractIdentifierNameStatic(unwrapped, "Identifier"));
            return;
        }
        if (unwrapped instanceof AssignmentPattern assignmentPattern) {
            collectPatternBindingNames(assignmentPattern.left(), names);
            return;
        }
        if (unwrapped instanceof RestElement restElement) {
            collectPatternBindingNames(restElement.argument(), names);
            return;
        }
        if ("AssignmentPattern".equals(nodeName)) {
            collectPatternBindingNames(QinSlimeFrontendAdapter.invokeByName(unwrapped, "left"), names);
        } else if ("RestElement".equals(nodeName)) {
            collectPatternBindingNames(QinSlimeFrontendAdapter.invokeByName(unwrapped, "argument"), names);
        }
    }

    private Object directFunctionalReturnedExpression(Object functionAst) {
        Object function = unwrapParenthesized(functionAst);
        if (!isZeroArgumentFunction(function)) {
            return null;
        }
        Object body = function instanceof ArrowFunctionExpression arrowFunction
                ? arrowFunction.body()
                : QinSlimeFrontendAdapter.invokeByName(function, "body");
        body = unwrapParenthesized(body);
        if (body instanceof CallExpression) {
            return body;
        }
        if (body instanceof BlockStatement blockStatement) {
            return singleReturnedExpression(blockStatement.body());
        }
        if ("BlockStatement".equals(QinSlimeFrontendAdapter.simpleName(body))) {
            return singleReturnedExpression(QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(body, "body"),
                    "BlockStatement.body"));
        }
        return null;
    }

    private Object singleReturnedExpression(List<?> statements) {
        if (statements == null || statements.size() != 1) {
            return null;
        }
        Object statement = statements.get(0);
        if (statement instanceof ReturnStatement returnStatement) {
            return unwrapParenthesized(returnStatement.argument());
        }
        if (statement instanceof ExpressionStatement expressionStatement) {
            return unwrapParenthesized(expressionStatement.expression());
        }
        if ("ReturnStatement".equals(QinSlimeFrontendAdapter.simpleName(statement))) {
            return unwrapParenthesized(QinSlimeFrontendAdapter.invokeByName(statement, "argument"));
        }
        if ("ExpressionStatement".equals(QinSlimeFrontendAdapter.simpleName(statement))) {
            return unwrapParenthesized(QinSlimeFrontendAdapter.invokeByName(statement, "expression"));
        }
        return null;
    }

    private boolean isZeroArgumentFunction(Object expression) {
        if (isZeroArgumentArrowFunction(expression)) {
            return true;
        }
        if (expression instanceof FunctionExpression functionExpression) {
            return functionExpression.params() == null || functionExpression.params().isEmpty();
        }
        if (!"FunctionExpression".equals(QinSlimeFrontendAdapter.simpleName(expression))) {
            return false;
        }
        List<?> params = QinSlimeFrontendAdapter.asListStatic(
                QinSlimeFrontendAdapter.invokeByName(expression, "params"),
                "FunctionExpression.params");
        return params.isEmpty();
    }

    private QinIrExpression lowerStaticZeroArgumentIifeOrNull(
            Object callee,
            List<?> arguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object function = unwrapParenthesized(callee);
        if (!isZeroArgumentArrowFunction(function) || arguments == null || !arguments.isEmpty()) {
            return null;
        }
        Object body = function instanceof ArrowFunctionExpression arrowFunction
                ? arrowFunction.body()
                : QinSlimeFrontendAdapter.invokeByName(function, "body");
        QinIrExpression generatedBoundMethodReference = lowerGeneratedBoundMethodReferenceIifeBodyOrNull(
                body,
                javaImportLookup,
                classContext,
                locals);
        if (generatedBoundMethodReference != null) {
            return generatedBoundMethodReference;
        }
        QinIrExpression lowered;
        if (body instanceof BlockStatement blockStatement) {
            lowered = lowerDeclarationMethodBody(
                    blockStatement.body(),
                    javaImportLookup,
                    classContext,
                    new LinkedHashMap<>(locals));
        } else if (body instanceof Expression expression) {
            lowered = lowerDeclarationExpression(expression, javaImportLookup, classContext, locals);
        } else if ("BlockStatement".equals(QinSlimeFrontendAdapter.simpleName(body))) {
            lowered = lowerDeclarationMethodBody(
                    asStatementList(
                            QinSlimeFrontendAdapter.invokeByName(body, "body"),
                            "ArrowFunctionExpression.body.body"),
                    javaImportLookup,
                    classContext,
                    new LinkedHashMap<>(locals));
        } else {
            throw qjsError(
                    "QJS2027",
                    "Unsupported static zero-argument IIFE body: " + describeAstNode(body));
        }
        if (lowered == null) {
            throw qjsError("QJS2027", "Static zero-argument IIFE body must produce a value");
        }
        return lowered;
    }

    private QinIrExpression lowerStaticExpressionIifeOrNull(
            Object callee,
            List<?> arguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object function = unwrapParenthesized(callee);
        if (!(function instanceof ArrowFunctionExpression arrowFunction)
                || arguments == null) {
            return null;
        }
        Object body = arrowFunction.body();
        List<?> params = arrowFunction.params() == null ? List.of() : arrowFunction.params();
        if (params.size() != arguments.size()) {
            return null;
        }
        QinIrExpression generatedLongHashHelper = lowerGeneratedLongHashIifeOrNull(
                body,
                params,
                arguments,
                javaImportLookup,
                classContext,
                locals);
        if (generatedLongHashHelper != null) {
            return generatedLongHashHelper;
        }
        if (body instanceof BlockStatement) {
            return null;
        }
        QinIrExpression generatedCollectionHelper = lowerGeneratedCollectionHelperIifeOrNull(
                body,
                params,
                arguments,
                javaImportLookup,
                classContext,
                locals);
        if (generatedCollectionHelper != null) {
            return generatedCollectionHelper;
        }
        Map<String, QinIrExpression> scopedLocals = new LinkedHashMap<>(locals);
        for (int i = 0; i < params.size(); i++) {
            if (!(params.get(i) instanceof Identifier identifier)) {
                return null;
            }
            scopedLocals.put(
                    identifier.name(),
                    lowerDeclarationExpression(arguments.get(i), javaImportLookup, classContext, locals));
        }
        return lowerDeclarationExpression(body, javaImportLookup, classContext, scopedLocals);
    }

    private QinIrExpression lowerGeneratedBoundMethodReferenceIifeBodyOrNull(
            Object body,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<?> statements;
        if (body instanceof BlockStatement blockStatement) {
            statements = blockStatement.body();
        } else if ("BlockStatement".equals(QinSlimeFrontendAdapter.simpleName(body))) {
            statements = QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(body, "body"),
                    "ArrowFunctionExpression.body.body");
        } else {
            return null;
        }
        if (statements == null || statements.size() != 2) {
            return null;
        }
        Object receiverInitializer = singleIdentifierVariableInitializer(statements.get(0), "__qin_bound_receiver");
        if (receiverInitializer == null) {
            return null;
        }
        Object returnArgument = returnArgumentOrNull(statements.get(1));
        if (returnArgument == null) {
            return null;
        }
        BoundMethodPattern boundMethodPattern =
                generatedBoundMethodPatternOrNull(returnArgument, "__qin_bound_receiver");
        if (boundMethodPattern == null) {
            return null;
        }
        QinIrExpression receiver =
                lowerDeclarationExpression(receiverInitializer, javaImportLookup, classContext, locals);
        return new QinIrBoundMethodReferenceExpression(receiver, boundMethodPattern.methodName());
    }

    private Object singleIdentifierVariableInitializer(Object statement, String expectedName) {
        if (!(statement instanceof VariableDeclaration variableDeclaration)
                || variableDeclaration.declarations() == null
                || variableDeclaration.declarations().size() != 1) {
            return null;
        }
        Object declarator = variableDeclaration.declarations().get(0);
        Object id = QinSlimeFrontendAdapter.invokeByName(declarator, "id");
        if (!(id instanceof Identifier identifier) || !expectedName.equals(identifier.name())) {
            return null;
        }
        return QinSlimeFrontendAdapter.invokeByName(declarator, "init");
    }

    private Object returnArgumentOrNull(Object statement) {
        if (statement instanceof ReturnStatement returnStatement) {
            return returnStatement.argument();
        }
        if (!"ReturnStatement".equals(QinSlimeFrontendAdapter.simpleName(statement))) {
            return null;
        }
        return QinSlimeFrontendAdapter.invokeByName(statement, "argument");
    }

    private BoundMethodPattern generatedBoundMethodPatternOrNull(Object returnArgument, String receiverName) {
        if (!(returnArgument instanceof CallExpression bindCall)
                || bindCall.arguments() == null
                || bindCall.arguments().size() != 1
                || !receiverName.equals(declarationIdentifierName(bindCall.arguments().get(0)))
                || !(bindCall.callee() instanceof MemberExpression bindMember)
                || bindMember.computed()
                || !"bind".equals(adapter.extractMemberPropertyName(bindMember.property()))
                || !(bindMember.object() instanceof MemberExpression methodMember)
                || methodMember.computed()
                || !receiverName.equals(declarationIdentifierName(methodMember.object()))) {
            return null;
        }
        String methodName = adapter.extractMemberPropertyName(methodMember.property());
        if (methodName == null || methodName.isBlank()) {
            return null;
        }
        return new BoundMethodPattern(methodName);
    }

    private record BoundMethodPattern(String methodName) {
    }

    private QinIrExpression lowerGeneratedLongHashIifeOrNull(
            Object body,
            List<?> params,
            List<?> arguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (params.size() != 1 || arguments.size() != 1) {
            return null;
        }
        String bodySource = sourceTextForNode(body);
        if (bodySource == null
                || !bodySource.contains("__qin_long_hash_number")
                || !bodySource.contains("4294967296")
                || !bodySource.contains("Math.trunc")) {
            return null;
        }
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_java_long_hash_code__",
                List.of(lowerDeclarationExpression(arguments.get(0), javaImportLookup, classContext, locals)));
    }

    private QinIrExpression lowerGeneratedCollectionHelperIifeOrNull(
            Object body,
            List<?> params,
            List<?> arguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        String bodySource = sourceTextForNode(body);
        if (bodySource == null || !bodySource.contains("Array.isArray")) {
            return null;
        }
        if (params.size() == 1
                && bodySource.contains(".length")
                && bodySource.contains(".size()")) {
            String method = bodySource.contains("? true") && bodySource.contains(".isEmpty()")
                    ? "__qin_collection_is_empty__"
                    : "__qin_collection_size__";
            return new QinIrBuiltinCallExpression(
                    "Global",
                    method,
                    List.of(lowerDeclarationExpression(arguments.get(0), javaImportLookup, classContext, locals)));
        }
        if (params.size() == 2
                && bodySource.contains("[Number(")
                && bodySource.contains(".get(")) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_collection_get__",
                    List.of(
                            lowerDeclarationExpression(arguments.get(0), javaImportLookup, classContext, locals),
                            lowerDeclarationExpression(arguments.get(1), javaImportLookup, classContext, locals)));
        }
        if (params.size() == 2
                && bodySource.contains(".some(")
                && bodySource.contains(".contains(")) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_collection_contains__",
                    List.of(
                            lowerDeclarationExpression(arguments.get(0), javaImportLookup, classContext, locals),
                            lowerDeclarationExpression(arguments.get(1), javaImportLookup, classContext, locals)));
        }
        return null;
    }

    private String sourceTextForNode(Object node) {
        if (!(node instanceof AstNode astNode)
                || astNode.location() == null
                || astNode.location().start() == null
                || astNode.location().end() == null
                || adapter.currentSourceText == null) {
            return null;
        }
        int start = Math.max(0, Math.min(astNode.location().start().index(), adapter.currentSourceText.length()));
        int end = Math.max(start, Math.min(astNode.location().end().index(), adapter.currentSourceText.length()));
        return adapter.currentSourceText.substring(start, end);
    }

    private QinIrExpression lowerGeneratedConstructorDelegationIifeOrNull(
            Object callee,
            List<?> arguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object function = unwrapParenthesized(callee);
        if (!(function instanceof ArrowFunctionExpression arrowFunction)
                || arguments == null
                || arguments.isEmpty()) {
            return null;
        }
        String restName = singleRestParameterName(arrowFunction);
        if (restName == null) {
            return null;
        }
        List<QinIrExpression> loweredArguments =
                lowerDeclarationCallArguments(arguments, javaImportLookup, classContext, locals);
        CallExpression delegateCall = selectGeneratedConstructorDelegateCall(
                arrowFunction.body(),
                restName,
                loweredArguments,
                classContext,
                parametersFromLocals(locals));
        if (delegateCall == null || !(delegateCall.callee() instanceof MemberExpression memberExpression)
                || memberExpression.computed()
                || !(memberExpression.object() instanceof ThisExpression)) {
            return null;
        }
        String methodName = adapter.extractMemberPropertyName(memberExpression.property());
        if (methodName == null || !methodName.startsWith("__qin_constructor_")) {
            return null;
        }
        QinIrExpression receiver = new QinIrThisExpression();
        String ownerBinaryName = declarationKnownThisMethodOwnerBinaryName(
                receiver,
                methodName,
                loweredArguments.size(),
                classContext);
        return ownerBinaryName == null
                ? new QinIrInstanceMethodCallExpression(receiver, methodName, loweredArguments)
                : new QinIrInstanceMethodCallExpression(receiver, ownerBinaryName, methodName, loweredArguments);
    }

    private QinIrExpression lowerStaticArrayFromConstantFactoryCallOrNull(
            Object callee,
            List<?> arguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (arguments == null || arguments.size() != 2 || !isArrayFromMemberCall(callee)) {
            return null;
        }
        Object constantFactoryBody = staticConstantFactoryBodyOrNull(arguments.get(1));
        if (constantFactoryBody == null) {
            return null;
        }
        QinIrExpression source = lowerDeclarationExpression(arguments.get(0), javaImportLookup, classContext, locals);
        QinIrExpression length = source instanceof QinIrObjectLiteral objectLiteral
                ? staticLengthExpressionOrNull(objectLiteral)
                : null;
        if (length == null) {
            return null;
        }
        QinIrExpression value = lowerDeclarationExpression(constantFactoryBody, javaImportLookup, classContext, locals);
        if (value instanceof QinIrNullLiteral) {
            return new QinIrArrayCreationExpression(
                    QinIrTypeRef.classType("java.lang.Object"),
                    List.of(length),
                    0);
        }
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_array_from_constant__",
                List.of(length, value));
    }

    private boolean isArrayFromMemberCall(Object callee) {
        Object unwrapped = unwrapParenthesized(callee);
        if (unwrapped instanceof MemberExpression memberExpression) {
            return !memberExpression.computed()
                    && "from".equals(adapter.extractMemberPropertyName(memberExpression.property()))
                    && "Array".equals(declarationIdentifierName(memberExpression.object()));
        }
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(unwrapped))
                || Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(unwrapped, "computed"))) {
            return false;
        }
        String propertyName = adapter.extractMemberPropertyName(
                QinSlimeFrontendAdapter.invokeByName(unwrapped, "property"));
        return "from".equals(propertyName)
                && "Array".equals(declarationIdentifierName(
                        QinSlimeFrontendAdapter.invokeByName(unwrapped, "object")));
    }

    private QinIrFunctionLiteral staticNullFactoryFunctionLiteralOrNull(Object functionAst) {
        Object body = staticConstantFactoryBodyOrNull(functionAst);
        return isNullLiteralExpression(body) ? new QinIrFunctionLiteral(new QinIrNullLiteral()) : null;
    }

    private Object staticConstantFactoryBodyOrNull(Object functionAst) {
        Object function = unwrapParenthesized(functionAst);
        Object body;
        if (function instanceof ArrowFunctionExpression arrowFunction) {
            body = arrowFunction.body();
        } else if (function instanceof FunctionExpression functionExpression) {
            List<Statement> statements = functionExpression.body() == null
                    ? List.of()
                    : functionExpression.body().body();
            body = statements.size() == 1 ? returnArgumentOrNull(statements.get(0)) : null;
        } else if ("ArrowFunctionExpression".equals(QinSlimeFrontendAdapter.simpleName(function))) {
            body = QinSlimeFrontendAdapter.invokeByName(function, "body");
        } else if ("FunctionExpression".equals(QinSlimeFrontendAdapter.simpleName(function))) {
            List<?> statements = QinSlimeFrontendAdapter.asListStatic(
                    QinSlimeFrontendAdapter.invokeByName(function, "body"),
                    "FunctionExpression.body");
            body = statements.size() == 1 ? returnArgumentOrNull(statements.get(0)) : null;
        } else {
            return null;
        }
        Object unwrappedBody = unwrapParenthesized(body);
        if (isNullLiteralExpression(unwrappedBody)
                || unwrappedBody instanceof Literal
                || "Literal".equals(QinSlimeFrontendAdapter.simpleName(unwrappedBody))) {
            return unwrappedBody;
        }
        return null;
    }

    private boolean isNullLiteralExpression(Object expressionAst) {
        Object expression = unwrapParenthesized(expressionAst);
        if (expression instanceof Literal literal) {
            return literal.value() == null;
        }
        return "Literal".equals(QinSlimeFrontendAdapter.simpleName(expression))
                && QinSlimeFrontendAdapter.invokeByName(expression, "value") == null;
    }

    private Integer staticLengthPropertyOrNull(QinIrObjectLiteral objectLiteral) {
        if (objectLiteral == null || objectLiteral.properties().size() != 1) {
            return null;
        }
        QinIrObjectProperty property = objectLiteral.properties().get(0);
        if (!"length".equals(property.key()) || !(property.value() instanceof QinIrNumberLiteral numberLiteral)) {
            return null;
        }
        double value = numberLiteral.value();
        if (value < 0 || value != Math.rint(value) || value > Integer.MAX_VALUE) {
            return null;
        }
        return (int) value;
    }

    private QinIrExpression staticLengthExpressionOrNull(QinIrObjectLiteral objectLiteral) {
        if (objectLiteral == null || objectLiteral.properties().size() != 1) {
            return null;
        }
        QinIrObjectProperty property = objectLiteral.properties().get(0);
        if (!"length".equals(property.key()) || property.value() == null) {
            return null;
        }
        Integer staticLength = staticLengthPropertyOrNull(objectLiteral);
        return staticLength == null ? property.value() : new QinIrNumberLiteral(staticLength);
    }

    private String singleRestParameterName(ArrowFunctionExpression arrowFunction) {
        if (arrowFunction.params() == null || arrowFunction.params().size() != 1
                || !(arrowFunction.params().get(0) instanceof RestElement restElement)
                || !(restElement.argument() instanceof Identifier identifier)) {
            return null;
        }
        return identifier.name();
    }

    private CallExpression selectGeneratedConstructorDelegateCall(
            Object body,
            String restName,
            List<QinIrExpression> loweredArguments,
            DeclarationClassContext classContext,
            List<QinIrParameter> parameters) {
        List<CallExpression> candidates = generatedConstructorDelegateReturnCallsFromBody(body, restName);
        if (candidates.isEmpty()) {
            return null;
        }
        CallExpression best = null;
        int bestScore = Integer.MIN_VALUE;
        for (CallExpression candidate : candidates) {
            String methodName = generatedConstructorDelegateMethodName(candidate);
            QinIrMethodDeclaration method = methodName == null
                    ? null
                    : classContext.findMethod(methodName, loweredArguments.size());
            if (method == null) {
                continue;
            }
            int score = generatedConstructorDelegateScore(method, loweredArguments, parameters, classContext);
            if (score > bestScore) {
                best = candidate;
                bestScore = score;
            }
        }
        return best != null ? best : candidates.get(0);
    }

    private int generatedConstructorDelegateScore(
            QinIrMethodDeclaration method,
            List<QinIrExpression> loweredArguments,
            List<QinIrParameter> parameters,
            DeclarationClassContext classContext) {
        int score = 0;
        for (int i = 0; i < loweredArguments.size(); i++) {
            QinIrTypeRef actualType = inferGeneratedDelegationArgumentType(
                    loweredArguments.get(i),
                    parameters,
                    classContext);
            QinIrTypeRef targetType = method.parameters().get(i).type();
            int argumentScore = generatedConstructorDelegateArgumentScore(actualType, targetType);
            if (argumentScore == Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            }
            score += argumentScore;
        }
        return score;
    }

    private QinIrTypeRef inferGeneratedDelegationArgumentType(
            QinIrExpression argument,
            List<QinIrParameter> parameters,
            DeclarationClassContext classContext) {
        if (argument instanceof QinIrStaticMethodCallExpression staticMethodCallExpression
                && staticMethodCallExpression.ownerBinaryName() != null
                && !staticMethodCallExpression.ownerBinaryName().isBlank()) {
            return QinIrTypeRef.classType(staticMethodCallExpression.ownerBinaryName());
        }
        return inferDeclarationReturnType(argument, parameters, classContext);
    }

    private int generatedConstructorDelegateArgumentScore(QinIrTypeRef actualType, QinIrTypeRef targetType) {
        if (targetType == null || isObjectPlaceholderType(targetType)) {
            return 1;
        }
        if (actualType == null || isObjectPlaceholderType(actualType)) {
            return 0;
        }
        if (actualType.equals(targetType)) {
            return 8;
        }
        if (actualType.kind() == targetType.kind()
                && Objects.equals(actualType.binaryName(), targetType.binaryName())) {
            return 8;
        }
        if (targetType.kind() == com.qin.lang.ir.QinIrTypeKind.CLASS
                && "java.lang.Object".equals(targetType.binaryName())) {
            return 1;
        }
        return Integer.MIN_VALUE;
    }

    private String generatedConstructorDelegateMethodName(CallExpression delegateCall) {
        if (delegateCall == null
                || !(delegateCall.callee() instanceof MemberExpression memberExpression)
                || memberExpression.computed()
                || !(memberExpression.object() instanceof ThisExpression)) {
            return null;
        }
        String methodName = adapter.extractMemberPropertyName(memberExpression.property());
        return methodName != null && methodName.startsWith("__qin_constructor_") ? methodName : null;
    }

    private List<QinIrParameter> parametersFromLocals(Map<String, QinIrExpression> locals) {
        if (locals == null || locals.isEmpty()) {
            return List.of();
        }
        List<QinIrParameter> parameters = new ArrayList<>();
        for (Map.Entry<String, QinIrExpression> entry : locals.entrySet()) {
            QinIrTypeRef type = inferDeclarationReturnType(
                    entry.getValue(),
                    List.of(),
                    new DeclarationClassContext("<iife>", List.of(), List.of()));
            parameters.add(new QinIrParameter(entry.getKey(), type, List.of()));
        }
        return List.copyOf(parameters);
    }

    private List<CallExpression> generatedConstructorDelegateReturnCallsFromBody(Object body, String restName) {
        List<CallExpression> calls = new ArrayList<>();
        collectGeneratedConstructorDelegateReturnCallsFromBody(body, restName, calls);
        return List.copyOf(calls);
    }

    private void collectGeneratedConstructorDelegateReturnCallsFromBody(
            Object body,
            String restName,
            List<CallExpression> calls) {
        if (!(body instanceof BlockStatement blockStatement) || blockStatement.body() == null) {
            return;
        }
        for (Statement statement : blockStatement.body()) {
            collectGeneratedConstructorDelegateReturnCallsFromStatement(statement, restName, calls);
        }
    }

    private void collectGeneratedConstructorDelegateReturnCallsFromStatement(
            Statement statement,
            String restName,
            List<CallExpression> calls) {
        if (statement instanceof ReturnStatement returnStatement) {
            CallExpression call = isGeneratedConstructorDelegateSpreadCall(returnStatement.argument(), restName);
            if (call != null) {
                calls.add(call);
            }
            return;
        }
        if (statement instanceof IfStatement ifStatement) {
            collectGeneratedConstructorDelegateReturnCallsFromStatement(ifStatement.consequent(), restName, calls);
            collectGeneratedConstructorDelegateReturnCallsFromStatement(ifStatement.alternate(), restName, calls);
            return;
        }
        if (statement instanceof BlockStatement) {
            collectGeneratedConstructorDelegateReturnCallsFromBody(statement, restName, calls);
        }
    }

    private CallExpression isGeneratedConstructorDelegateSpreadCall(Expression expression, String restName) {
        if (!(expression instanceof CallExpression callExpression)
                || callExpression.arguments() == null
                || callExpression.arguments().size() != 1
                || !(callExpression.arguments().get(0) instanceof SpreadElement spreadElement)
                || !(spreadElement.argument() instanceof Identifier identifier)
                || !restName.equals(identifier.name())) {
            return null;
        }
        return callExpression;
    }

    private Object unwrapParenthesized(Object expression) {
        Object current = expression;
        while (current instanceof ParenthesizedExpression parenthesizedExpression) {
            current = parenthesizedExpression.expression();
        }
        while ("ParenthesizedExpression".equals(QinSlimeFrontendAdapter.simpleName(current))) {
            current = QinSlimeFrontendAdapter.invokeByName(current, "expression");
        }
        return current;
    }

    private boolean isZeroArgumentArrowFunction(Object expression) {
        if (expression instanceof ArrowFunctionExpression arrowFunctionExpression) {
            return arrowFunctionExpression.params() == null || arrowFunctionExpression.params().isEmpty();
        }
        if (!"ArrowFunctionExpression".equals(QinSlimeFrontendAdapter.simpleName(expression))) {
            return false;
        }
        List<?> params = QinSlimeFrontendAdapter.asListStatic(
                QinSlimeFrontendAdapter.invokeByName(expression, "params"),
                "ArrowFunctionExpression.params");
        return params.isEmpty();
    }

    private List<? extends Statement> asStatementList(Object value, String context) {
        List<?> rawStatements = QinSlimeFrontendAdapter.asListStatic(value, context);
        List<Statement> statements = new ArrayList<>();
        for (Object rawStatement : rawStatements) {
            if (!(rawStatement instanceof Statement statement)) {
                throw qjsError(
                        "QJS2024",
                        "Unsupported declaration statement body node in " + context + ": "
                                + describeAstNode(rawStatement));
            }
            statements.add(statement);
        }
        return List.copyOf(statements);
    }

    private boolean isQinGlobalBuiltinFunction(String name) {
        return "__qin_binary__".equals(name)
                || "__qin_logical__".equals(name)
           || "__qin_instanceof__".equals(name)
           || "__qin_structural_object__".equals(name)
           || "__qin_string__".equals(name)
           || "__qin_init_enum_value".equals(name)
           || "__qin_collection_size__".equals(name)
           || "__qin_collection_is_empty__".equals(name)
           || "__qin_collection_get__".equals(name)
           || "__qin_collection_add__".equals(name)
           || "__qin_array_append__".equals(name)
           || "__qin_array_prepend__".equals(name)
           || "__qin_array_remove_at__".equals(name)
           || "__qin_array_slice__".equals(name)
           || "__qin_collection_contains__".equals(name)
           || "__qin_collection_to_array__".equals(name)
           || "__qin_java_utf8_decode__".equals(name)
           || "__qin_java_time_now__".equals(name)
           || "__qin_java_time_from__".equals(name)
           || "__qin_java_time_format__".equals(name)
           || "__qin_java_pattern_regexp__".equals(name)
           || "__qin_java_string_hash_code__".equals(name)
           || "__qin_java_identity_hash_code__".equals(name)
           || "__qin_java_value_hash_code__".equals(name)
           || "__qin_java_long_hash_code__".equals(name)
           || "__qin_java_values_equal__".equals(name)
           || "__qin_java_hash_key__".equals(name)
           || "__qin_java_hash_key_equals__".equals(name)
           || "__qin_java_new_array__".equals(name)
           || "__qin_java_class_info__".equals(name)
           || "__qin_java_implements".equals(name)
           || "__qin_java_regex_pattern_compile__".equals(name)
           || "__qin_java_regex_pattern_exec__".equals(name)
           || "__qin_java_regex_matcher__".equals(name)
           || "__qin_java_regex_matcher_region__".equals(name)
           || "__qin_java_regex_matcher_reset__".equals(name)
           || "__qin_java_regex_matcher_looking_at__".equals(name)
           || "__qin_java_regex_matcher_matches__".equals(name)
           || "__qin_java_regex_matcher_find__".equals(name)
           || "__qin_java_regex_matcher_group__".equals(name)
           || "__qin_java_regex_matcher_group_count__".equals(name)
           || "__qin_java_regex_matcher_start__".equals(name)
           || "__qin_java_regex_matcher_end__".equals(name)
           || "__qin_java_regex_matcher_replace_all__".equals(name)
           || "__qin_java_regex_matcher_append_replacement__".equals(name)
           || "__qin_java_regex_matcher_append_tail__".equals(name)
            || "__qin_subhuti_identity_rule_cache_id".equals(name)
            || "__qin_subhuti_value_rule_cache_id".equals(name)
            || "__qin_subhuti_rule_cache_key".equals(name)
            || "__qin_cst_name_of__".equals(name)
            || "__qin_cst_value_of__".equals(name)
            || "__qin_cst_children_of__".equals(name)
            || "__qin_cst_loc_of__".equals(name)
            || "__qin_ast_type_of__".equals(name)
            || "__qin_ast_body_of__".equals(name)
            || "__qin_ast_expression_of__".equals(name)
            || "__qin_ast_name_of__".equals(name)
            || "__qin_array_from_constant__".equals(name)
            || "__qin_java_functional".equals(name);
    }

    private String declarationIdentifierName(Object expressionAst) {
        if (expressionAst instanceof Identifier identifier) {
            return identifier.name();
        }
        if ("Identifier".equals(QinSlimeFrontendAdapter.simpleName(expressionAst))) {
            return QinSlimeFrontendAdapter.extractIdentifierNameStatic(expressionAst, "Identifier");
        }
        return null;
    }

    private boolean isUnsupportedDeclarationGlobalIdentifier(String name) {
        return "Date".equals(name) || "BigInt".equals(name);
    }

    private List<QinIrExpression> lowerDeclarationCallArguments(
            List<?> arguments,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (arguments == null || arguments.isEmpty()) {
            return List.of();
        }
        List<QinIrExpression> lowered = new ArrayList<>();
        for (Object argument : arguments) {
            lowered.add(lowerDeclarationExpression(argument, javaImportLookup, classContext, locals));
        }
        return List.copyOf(lowered);
    }

    private QinIrExpression lowerDeclarationReceiver(
            Object receiverAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression receiver = lowerDeclarationExpression(receiverAst, javaImportLookup, classContext, locals);
        if (receiver instanceof QinIrBooleanLiteral
                || receiver instanceof QinIrNumberLiteral
                || receiver instanceof QinIrNullLiteral) {
            throw qjsError(
                    "QJS2018",
                    "Unsupported declaration receiver expression: " + receiver.getClass().getSimpleName()
                            + " from " + describeAstNode(receiverAst));
        }
        return receiver;
    }

    private String nullStaticImportOwnerOrNull(Object receiverAst, String methodName) {
        if (!isNullLiteralAst(receiverAst) || methodName == null || methodName.isBlank()) {
            return null;
        }
        return switch (methodName) {
            case "range", "of" -> "java.util.stream.IntStream";
            case "identity" -> "java.util.function.Function";
            case "comparingByKey", "comparingByValue" -> "java.util.Map$Entry";
            default -> null;
        };
    }

    private boolean isNullLiteralAst(Object ast) {
        if (ast instanceof Literal literal) {
            return literal.value() == null && "null".equals(literal.raw());
        }
        if (!"Literal".equals(QinSlimeFrontendAdapter.simpleName(ast))) {
            return false;
        }
        Object value = QinSlimeFrontendAdapter.invokeByName(ast, "value");
        Object raw = QinSlimeFrontendAdapter.invokeByName(ast, "raw");
        return value == null && "null".equals(raw);
    }

    private QinIrExpression lowerDeclarationInitializer(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression initializer = adapter.lowerRuntimeExpression(expressionAst, javaImportLookup, declarationLookup);
        if (initializer instanceof QinIrObjectLiteral
                || initializer instanceof com.qin.lang.ir.QinIrJavaNewExpression
                || initializer instanceof QinIrJavaClassLiteralExpression
                || initializer instanceof QinIrIdentifierReference
                || initializer instanceof QinIrMemberAccessExpression
                || initializer instanceof QinIrBuiltinCallExpression
                || initializer instanceof QinIrStaticMethodCallExpression
                || initializer instanceof QinIrInstanceMethodCallExpression
                || initializer instanceof com.qin.lang.ir.QinIrFunctionLiteral
                || initializer instanceof QinIrNumberLiteral
                || initializer instanceof QinIrStringLiteral
                || initializer instanceof QinIrBooleanLiteral
                || initializer instanceof QinIrArrayLiteral
                || initializer instanceof QinIrNullLiteral) {
            return initializer;
        }
        throw qjsError(
                "QJS2002",
                "Unsupported const initializer expression"
                        + " ast=" + describeAstNode(expressionAst)
                        + " ir=" + initializer.getClass().getSimpleName());
    }

    private List<QinIrAnnotation> lowerAnnotations(
            List<Decorator> decorators,
            Map<String, String> javaImportLookup) {
        if (decorators == null || decorators.isEmpty()) {
            return List.of();
        }

        List<QinIrAnnotation> annotations = new ArrayList<>();
        for (Decorator decorator : decorators) {
            if (isQinOwnedCompileTimeOnlyDecorator(decorator)) {
                continue;
            }
            QinIrAnnotation annotation = lowerAnnotationOrNull(decorator, javaImportLookup);
            if (annotation == null) {
                throw qjsError(
                        "QJS2013",
                        "Unsupported decorator in declaration subset; import a Java annotation or add a Qin-owned static decorator lowerer");
            }
            annotations.add(annotation);
        }
        return List.copyOf(annotations);
    }

    private QinIrAnnotation lowerAnnotationOrNull(
            Decorator decorator,
            Map<String, String> javaImportLookup) {
        if (decorator == null || decorator.expression() == null) {
            return null;
        }

        Expression expression = decorator.expression();
        if (expression instanceof Identifier identifier) {
            QinIrAnnotation qinOwned = lowerQinOwnedStaticDecoratorOrNull(identifier.name());
            if (qinOwned != null) {
                return qinOwned;
            }
            String binaryName = javaImportLookup.get(identifier.name());
            if (binaryName == null) {
                throw unsupportedDecorator(identifier.name());
            }
            return new QinIrAnnotation(binaryName, List.of());
        }

        if (expression instanceof CallExpression callExpression && callExpression.callee() instanceof Identifier identifier) {
            String binaryName = javaImportLookup.get(identifier.name());
            if (binaryName == null) {
                throw unsupportedDecorator(identifier.name());
            }

            List<QinIrAnnotationArgument> arguments = new ArrayList<>();
            if (!callExpression.arguments().isEmpty()) {
                if (callExpression.arguments().size() == 1) {
                    arguments.add(new QinIrAnnotationArgument(
                            "value",
                            lowerAnnotationLiteralExpression(callExpression.arguments().get(0))));
                } else {
                    List<QinIrExpression> values = new ArrayList<>();
                    for (Expression argument : callExpression.arguments()) {
                        values.add(lowerAnnotationLiteralExpression(argument));
                    }
                    arguments.add(new QinIrAnnotationArgument("value", new QinIrArrayLiteral(values)));
                }
            }
            return new QinIrAnnotation(binaryName, arguments);
        }

        return null;
    }

    private boolean isQinOwnedCompileTimeOnlyDecorator(Decorator decorator) {
        return decorator != null
                && decorator.expression() instanceof Identifier identifier
                && ("Subhuti".equals(identifier.name())
                        || "SubhutiClass".equals(identifier.name()));
    }

    private boolean isQinOwnedStaticDecorator(Decorator decorator) {
        if (isQinOwnedCompileTimeOnlyDecorator(decorator)) {
            return true;
        }
        return decorator != null
                && decorator.expression() instanceof Identifier identifier
                && lowerQinOwnedStaticDecoratorOrNull(identifier.name()) != null;
    }

    private QinIrAnnotation lowerQinOwnedStaticDecoratorOrNull(String name) {
        if ("SubhutiRule".equals(name)) {
            return new QinIrAnnotation(SUBHUTI_RULE_ANNOTATION, List.of());
        }
        return null;
    }

    private IllegalArgumentException unsupportedDecorator(String name) {
        return qjsError(
                "QJS2013",
                "Unsupported decorator `" + name
                        + "` in declaration subset; import a Java annotation or add a Qin-owned static decorator lowerer");
    }

    private QinIrExpression lowerAnnotationLiteralExpression(Expression expression) {
        if (expression instanceof Literal literal) {
            Object value = literal.value();
            if (value == null) {
                return new QinIrNullLiteral();
            }
            if (value instanceof String text) {
                return new QinIrStringLiteral(adapter.normalizeStringLiteral(text));
            }
            if (value instanceof Boolean boolValue) {
                return new QinIrBooleanLiteral(boolValue);
            }
            if (value instanceof Number number) {
                return new QinIrNumberLiteral(number.doubleValue());
            }
        }
        throw qjsError("QJS2012", "Only literal annotation arguments are supported in declaration subset");
    }

    private IllegalArgumentException qjsError(String code, String message) {
        return new IllegalArgumentException(code + " " + message);
    }

    private String sourceLocationDiagnostic(com.slime.ast.AstNode node) {
        if (node == null || node.location() == null) {
            return "<unknown>";
        }
        com.slime.ast.SourceLocation location = node.location();
        return "{type=" + location.type()
                + ", value=" + (location.value() == null ? "<null>" : location.value())
                + ", start=" + positionDiagnostic(location.start())
                + ", end=" + positionDiagnostic(location.end())
                + "}";
    }

    private String positionDiagnostic(com.slime.ast.Position position) {
        if (position == null) {
            return "<unknown>";
        }
        return position.line() + ":" + position.column() + "@" + position.index();
    }

    private String describeAstNode(Object node) {
        if (node == null) {
            return "<null>";
        }
        if (node instanceof Identifier identifier) {
            return "Identifier(" + identifier.name() + ")";
        }
        return QinSlimeFrontendAdapter.simpleName(node) + "(" + node + ")";
    }

    private record ConstructorSuperCall(List<QinIrExpression> arguments, boolean explicit) {
        private ConstructorSuperCall {
            arguments = arguments == null ? List.of() : List.copyOf(arguments);
        }
    }

    private record DeclarationClassContext(
            String className,
            Map<String, QinIrFieldDeclaration> fields,
            List<QinIrMethodDeclaration> methods,
            Map<String, QinIrExpression> moduleBindings,
            Map<String, QinIrExpression> staticExportSlotValues,
            Map<String, String> jsDeclarationClassLookup,
            Set<String> localDeclarationNames,
            QinIrTypeRef inheritedOverrideSuperType,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        private DeclarationClassContext(
                String className,
                List<QinIrFieldDeclaration> fields,
                List<QinIrMethodDeclaration> methods) {
            this(className, fields, methods, Map.of(), Map.of(), Map.of(), Set.of(), null, Map.of());
        }

        private DeclarationClassContext(
                String className,
                List<QinIrFieldDeclaration> fields,
                List<QinIrMethodDeclaration> methods,
                Map<String, QinIrExpression> moduleBindings) {
            this(className, fields, methods, moduleBindings, Map.of(), Map.of(), Set.of(), null, Map.of());
        }

        private DeclarationClassContext(
                String className,
                List<QinIrFieldDeclaration> fields,
                List<QinIrMethodDeclaration> methods,
                Map<String, QinIrExpression> moduleBindings,
                Map<String, String> jsDeclarationClassLookup) {
            this(className, fields, methods, moduleBindings, Map.of(), jsDeclarationClassLookup, Set.of(), null, Map.of());
        }

        private DeclarationClassContext(
                String className,
                List<QinIrFieldDeclaration> fields,
                List<QinIrMethodDeclaration> methods,
                Map<String, QinIrExpression> moduleBindings,
                Map<String, String> jsDeclarationClassLookup,
                Set<String> localDeclarationNames) {
            this(className, fields, methods, moduleBindings, Map.of(), jsDeclarationClassLookup, localDeclarationNames, null, Map.of());
        }

        private DeclarationClassContext(
                String className,
                List<QinIrFieldDeclaration> fields,
                List<QinIrMethodDeclaration> methods,
                Map<String, QinIrExpression> moduleBindings,
                Map<String, QinIrExpression> staticExportSlotValues,
                Map<String, String> jsDeclarationClassLookup,
                Set<String> localDeclarationNames) {
            this(className, fields, methods, moduleBindings, staticExportSlotValues, jsDeclarationClassLookup, localDeclarationNames, null, Map.of());
        }

        private DeclarationClassContext(
                String className,
                List<QinIrFieldDeclaration> fields,
                List<QinIrMethodDeclaration> methods,
                Map<String, QinIrExpression> moduleBindings,
                Map<String, QinIrExpression> staticExportSlotValues,
                Map<String, String> jsDeclarationClassLookup,
                Set<String> localDeclarationNames,
                QinIrTypeRef inheritedOverrideSuperType,
                Map<String, QinIrClassDeclaration> localJvmDeclarations) {
            this(
                    Objects.requireNonNull(className, "className cannot be null"),
                    indexFields(fields),
                    methods == null ? List.of() : List.copyOf(methods),
                    moduleBindings == null ? Map.of() : Map.copyOf(moduleBindings),
                    staticExportSlotValues == null ? Map.of() : Map.copyOf(staticExportSlotValues),
                    jsDeclarationClassLookup == null ? Map.of() : Map.copyOf(jsDeclarationClassLookup),
                    localDeclarationNames == null ? Set.of() : Set.copyOf(localDeclarationNames),
                    inheritedOverrideSuperType,
                    localJvmDeclarations == null ? Map.of() : Map.copyOf(localJvmDeclarations));
        }

        private static Map<String, QinIrFieldDeclaration> indexFields(List<QinIrFieldDeclaration> fields) {
            Map<String, QinIrFieldDeclaration> indexed = new LinkedHashMap<>();
            if (fields != null) {
                for (QinIrFieldDeclaration field : fields) {
                    indexed.put(field.name(), field);
                }
            }
            return Map.copyOf(indexed);
        }

        private QinIrMethodDeclaration findMethod(String methodName, int parameterCount) {
            for (QinIrMethodDeclaration method : methods) {
                if (method.name().equals(methodName) && method.parameters().size() == parameterCount) {
                    return method;
                }
            }
            return null;
        }

        private QinIrExpression moduleBinding(String name) {
            if (name == null || name.isBlank()) {
                return null;
            }
            return moduleBindings.get(name);
        }

        private QinIrExpression staticExportSlotValue(String slotName) {
            if (slotName == null || slotName.isBlank()) {
                return null;
            }
            return staticExportSlotValues.get(slotName);
        }

        private String jsDeclarationClass(String name) {
            if (name == null || name.isBlank()) {
                return null;
            }
            return jsDeclarationClassLookup.get(name);
        }

        private boolean isLocalDeclarationName(String name) {
            return name != null && localDeclarationNames.contains(name);
        }
    }
}
