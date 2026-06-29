package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrLocalDeclarationStatement;
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
import com.qin.lang.ir.QinIrStatement;
import com.qin.lang.ir.QinIrStatementExpression;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrThrowStatement;
import com.qin.lang.ir.QinIrTryStatement;
import com.qin.lang.ir.QinIrCatchClause;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinIrWhileStatementNode;
import com.slime.ast.AstNode;
import com.slime.ast.Expression;
import com.slime.ast.Statement;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.expressions.AssignmentExpression;
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
import com.slime.ast.nodes.expressions.ThisExpression;
import com.slime.ast.nodes.misc.CatchClause;
import com.slime.ast.nodes.misc.Decorator;
import com.slime.ast.nodes.misc.FunctionParameter;
import com.slime.ast.nodes.misc.MethodDefinition;
import com.slime.ast.nodes.misc.Property;
import com.slime.ast.nodes.misc.PropertyDefinition;
import com.slime.ast.nodes.patterns.ArrayPattern;
import com.slime.ast.nodes.patterns.AssignmentPattern;
import com.slime.ast.nodes.patterns.ObjectPattern;
import com.slime.ast.nodes.patterns.RestElement;
import com.slime.ast.nodes.statements.BlockStatement;
import com.slime.ast.nodes.statements.ExpressionStatement;
import com.slime.ast.nodes.statements.IfStatement;
import com.slime.ast.nodes.statements.ReturnStatement;
import com.slime.ast.nodes.statements.ThrowStatement;
import com.slime.ast.nodes.statements.TryStatement;
import com.slime.ast.nodes.statements.WhileStatement;
import com.slime.ast.nodes.typescript.TSKeywordType;
import com.slime.ast.nodes.typescript.TSTypeAnnotation;
import com.slime.ast.nodes.typescript.TSTypeReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.lang.reflect.Method;

/**
 * Qin-owned boundary for declaration-oriented IR lowering.
 *
 * <p>This component now owns the declaration subset lowering path directly.
 * It still reuses a narrow set of legacy helpers for generic runtime
 * expression/literal behavior where that logic has not been migrated yet.
 */
final class QinDeclarationIrLowerer {
    private final QinSlimeFrontendAdapter adapter;

    QinDeclarationIrLowerer(QinSlimeFrontendAdapter adapter) {
        this.adapter = Objects.requireNonNull(adapter, "adapter cannot be null");
    }

    QinIrClassDeclaration lowerClassDeclarationOrNull(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        boolean jvmSuperclass = classDeclaration.superClass() != null
                && isResolvableJvmSuperClass(classDeclaration.superClass(), javaImportLookup, localJvmDeclarations);
        if (!isDeclarationCompatibleClass(
                classDeclaration,
                javaImportLookup,
                localDeclarationNames,
                localJvmDeclarations)) {
            if (jvmSuperclass) {
                return lowerJavaSuperclassClassWithRuntimeMethods(
                        classDeclaration,
                        javaImportLookup,
                        localDeclarationNames,
                        localJvmDeclarations);
            }
            return null;
        }
        try {
            return lowerClassDeclarationAsDeclaration(classDeclaration, javaImportLookup, localDeclarationNames);
        } catch (IllegalArgumentException error) {
            if (isDeclarationSubsetError(error)) {
                if (jvmSuperclass) {
                    throw error;
                }
                return null;
            }
            throw error;
        }
    }

    private QinIrClassDeclaration lowerClassDeclarationAsDeclaration(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {

        List<QinIrFieldDeclaration> fields = new ArrayList<>();
        if (classDeclaration.body() != null && classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof PropertyDefinition propertyDefinition) {
                    QinIrFieldDeclaration loweredField = lowerFieldDeclarationOrNull(
                            propertyDefinition,
                            javaImportLookup,
                            localDeclarationNames,
                            classDeclaration.id().name());
                    if (loweredField != null) {
                        fields.add(loweredField);
                    }
                }
            }
        }

        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        DeclarationClassContext classContext = new DeclarationClassContext(classDeclaration.id().name(), fields, methods);
        if (classDeclaration.body() != null && classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof MethodDefinition methodDefinition) {
                    if (isConstructorMethod(methodDefinition)) {
                        continue;
                    }
                    QinIrMethodDeclaration loweredMethod = lowerMethodDeclarationOrNull(
                            methodDefinition,
                            javaImportLookup,
                            localDeclarationNames,
                            classContext);
                    if (loweredMethod != null) {
                        methods.add(loweredMethod);
                        classContext = new DeclarationClassContext(classDeclaration.id().name(), fields, methods);
                    }
                }
            }
        }

        return new QinIrClassDeclaration(
                null,
                classDeclaration.id().name(),
                lowerSuperType(classDeclaration, javaImportLookup, localDeclarationNames),
                lowerAnnotations(classDeclaration.decorators(), javaImportLookup),
                fields,
                methods);
    }

    private QinIrClassDeclaration lowerJavaSuperclassClassWithRuntimeMethods(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (classDeclaration == null || classDeclaration.id() == null || classDeclaration.body() == null) {
            throw qjsError("QJS2030", "java: superclass class declaration is missing id/body");
        }
        QinIrTypeRef superType = lowerSuperType(classDeclaration, javaImportLookup, localDeclarationNames);
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
                    QinIrFieldDeclaration loweredField = lowerFieldDeclarationOrNull(
                            propertyDefinition,
                            javaImportLookup,
                            localDeclarationNames,
                            classDeclaration.id().name());
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
                .anyMatch(annotation -> "com.subhuti.parser.SubhutiRule".equals(annotation.ownerBinaryName()));
        if (!hasRule) {
            result.add(new QinIrAnnotation("com.subhuti.parser.SubhutiRule", List.of()));
        }
        return List.copyOf(result);
    }

    private QinIrTypeRef lowerSuperType(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        if (classDeclaration.superClass() == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (classDeclaration.superClass() instanceof Identifier identifier) {
            String importedBinaryName = javaImportLookup.get(identifier.name());
            if (importedBinaryName != null) {
                return QinIrTypeRef.classType(importedBinaryName);
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
            throw qjsError("QJS2002", "Variable declaration must contain at least one declarator");
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
            boolean useJvmClassValue) {
        if (classDeclaration == null || classDeclaration.id() == null) {
            throw qjsError("QJS2010", "Anonymous ClassDeclaration is not supported in Qin subset");
        }
        return new QinIrConstDeclaration(
                classDeclaration.id().name(),
                adapter.lowerClassDeclarationRuntimeValue(classDeclaration, javaImportLookup, declarationLookup));
    }

    boolean isDeclarationCompatibleClass(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (classDeclaration == null || classDeclaration.id() == null || classDeclaration.body() == null) {
            return false;
        }
        if (classDeclaration.superClass() != null
                && !isResolvableDeclarationSuperClass(
                classDeclaration.superClass(),
                javaImportLookup,
                localDeclarationNames,
                localJvmDeclarations)) {
            return false;
        }
        if (classDeclaration.body().body() == null || classDeclaration.body().body().isEmpty()) {
            return false;
        }
        boolean hasJvmSignal = classDeclaration.superClass() != null;
        for (AstNode member : classDeclaration.body().body()) {
            if (member instanceof PropertyDefinition propertyDefinition) {
                if (propertyDefinition.isStatic() || propertyDefinition.computed()) {
                    return false;
                }
                if (propertyDefinition.typeAnnotation() != null
                        || (propertyDefinition.decorators() != null && !propertyDefinition.decorators().isEmpty())) {
                    hasJvmSignal = true;
                }
                continue;
            }
            if (member instanceof MethodDefinition methodDefinition) {
                if (methodDefinition.isStatic() || methodDefinition.computed()) {
                    return false;
                }
                if (isConstructorMethod(methodDefinition)) {
                    if (!isDeclarationCompatibleConstructorBody(methodDefinition.value())) {
                        return false;
                    }
                    continue;
                }
                if (methodDefinition.value() != null
                        && ((methodDefinition.value().parameterMetadata() != null
                        && !methodDefinition.value().parameterMetadata().isEmpty())
                        || methodDefinition.value().returnType() != null)) {
                    hasJvmSignal = true;
                }
                if (methodDefinition.decorators() != null && !methodDefinition.decorators().isEmpty()) {
                    hasJvmSignal = true;
                }
                if (!isDeclarationCompatibleMethodBody(methodDefinition.value())) {
                    return false;
                }
                continue;
            }
            return false;
        }
        if (classDeclaration.decorators() != null && !classDeclaration.decorators().isEmpty()) {
            hasJvmSignal = hasJvmSignal || hasResolvableJavaDecorator(classDeclaration.decorators(), javaImportLookup);
        }
        return hasJvmSignal;
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
        List<? extends Statement> statements = function.body().body();
        if (statements.isEmpty()) {
            return true;
        }
        if (statements.size() != 1 || !(statements.get(0) instanceof ExpressionStatement expressionStatement)) {
            return false;
        }
        Object expression = expressionStatement.expression();
        if (!(expression instanceof CallExpression callExpression)) {
            return false;
        }
        return "Super".equals(callExpression.callee().getClass().getSimpleName())
                && callExpression.arguments().isEmpty();
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
            Set<String> localDeclarationNames,
            Map<String, QinIrClassDeclaration> localJvmDeclarations) {
        if (isResolvableSuperClass(superClass, javaImportLookup)) {
            return true;
        }
        if (!(superClass instanceof Identifier identifier)) {
            return false;
        }
        String name = identifier.name();
        return localJvmDeclarations != null && localJvmDeclarations.containsKey(name);
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
            if (statement instanceof WhileStatement whileStatement) {
                if (!isDeclarationCompatibleBranch(whileStatement.body())) {
                    return false;
                }
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
        if (statement instanceof WhileStatement whileStatement) {
            return isDeclarationCompatibleBranch(whileStatement.body());
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

    private boolean hasIdentifierOnlyVariableDeclarators(VariableDeclaration variableDeclaration) {
        if (variableDeclaration == null
                || variableDeclaration.declarations() == null
                || variableDeclaration.declarations().isEmpty()) {
            return false;
        }
        for (var declarator : variableDeclaration.declarations()) {
            if (declarator == null || !(declarator.id() instanceof Identifier) || declarator.init() == null) {
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

    private boolean isDeclarationSubsetError(IllegalArgumentException error) {
        if (error == null || error.getMessage() == null) {
            return false;
        }
        return error.getMessage().startsWith("QJS");
    }

    private QinIrFieldDeclaration lowerFieldDeclarationOrNull(
            PropertyDefinition propertyDefinition,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            String className) {
        if (propertyDefinition == null || !(propertyDefinition.key() instanceof Identifier identifier)) {
            return null;
        }
        return new QinIrFieldDeclaration(
                identifier.name(),
                lowerParameterType(propertyDefinition.typeAnnotation(), javaImportLookup, localDeclarationNames),
                lowerAnnotations(propertyDefinition.decorators(), javaImportLookup),
                lowerFieldInitializer(propertyDefinition.value(), javaImportLookup, className, identifier.name()));
    }

    private QinIrExpression lowerFieldInitializer(
            AstNode valueAst,
            Map<String, String> javaImportLookup,
            String className,
            String fieldName) {
        if (valueAst == null) {
            return null;
        }
        QinIrExpression initializer = adapter.lowerExpression(valueAst, javaImportLookup);
        if (initializer instanceof QinIrStringLiteral
                || initializer instanceof QinIrBooleanLiteral
                || initializer instanceof QinIrNumberLiteral
                || initializer instanceof QinIrNullLiteral) {
            return initializer;
        }
        throw qjsError("QJS2013", "Only literal field initializers are supported in declaration subset: "
                + className + "." + fieldName + " uses " + QinSlimeFrontendAdapter.simpleName(valueAst));
    }

    private QinIrMethodDeclaration lowerMethodDeclarationOrNull(
            MethodDefinition methodDefinition,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames,
            DeclarationClassContext classContext) {
        if (!(methodDefinition.key() instanceof Identifier identifier)) {
            return null;
        }

        FunctionExpression function = methodDefinition.value();
        List<QinIrParameter> parameters = lowerParameters(function, javaImportLookup, localDeclarationNames);
        List<QinIrStatement> bodyStatements = lowerMethodBodyStatementsOrEmpty(
                function,
                javaImportLookup,
                classContext);
        QinIrExpression returnExpression = bodyStatements.isEmpty()
                ? lowerMethodReturnExpression(function, javaImportLookup, classContext)
                : null;
        QinIrTypeRef returnType = function != null && function.returnType() != null
                ? lowerParameterType(function.returnType(), javaImportLookup, localDeclarationNames)
                : inferDeclarationReturnType(returnExpression, parameters, classContext);

        return new QinIrMethodDeclaration(
                identifier.name(),
                returnType,
                parameters,
                lowerAnnotations(methodDefinition.decorators(), javaImportLookup),
                returnExpression,
                bodyStatements,
                List.of(),
                null,
                false);
    }

    private List<QinIrParameter> lowerParameters(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        if (function == null) {
            return List.of();
        }

        if (function.parameterMetadata() != null && !function.parameterMetadata().isEmpty()) {
            List<QinIrParameter> parameters = new ArrayList<>();
            for (FunctionParameter parameter : function.parameterMetadata()) {
                if (parameter == null || !(parameter.pattern() instanceof Identifier identifier)) {
                    throw qjsError("QJS2011", "Only identifier parameters are supported in declaration subset");
                }
                parameters.add(new QinIrParameter(
                        identifier.name(),
                        lowerParameterType(parameter.typeAnnotation(), javaImportLookup, localDeclarationNames),
                        lowerAnnotations(parameter.decorators(), javaImportLookup)));
            }
            return List.copyOf(parameters);
        }

        if (function.params() == null || function.params().isEmpty()) {
            return List.of();
        }

        List<QinIrParameter> parameters = new ArrayList<>();
        for (com.slime.ast.Pattern pattern : function.params()) {
            if (!(pattern instanceof Identifier identifier)) {
                throw qjsError("QJS2011", "Only identifier parameters are supported in declaration subset");
            }
            parameters.add(new QinIrParameter(
                    identifier.name(),
                    QinIrTypeRef.classType("java.lang.Object"),
                    List.of()));
        }
        return List.copyOf(parameters);
    }

    private QinIrTypeRef lowerParameterType(
            AstNode typeAnnotationAst,
            Map<String, String> javaImportLookup,
            Set<String> localDeclarationNames) {
        if (!(typeAnnotationAst instanceof TSTypeAnnotation annotation) || annotation.typeAnnotation() == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        AstNode typeAst = annotation.typeAnnotation();
        if (typeAst instanceof TSKeywordType keywordType) {
            return switch (keywordType.keyword()) {
                case "string" -> QinIrTypeRef.stringType();
                case "boolean" -> QinIrTypeRef.booleanType();
                case "number" -> QinIrTypeRef.doubleType();
                default -> QinIrTypeRef.classType("java.lang.Object");
            };
        }
        if (typeAst instanceof TSTypeReference typeReference && typeReference.typeName() instanceof Identifier identifier) {
            String importedBinaryName = javaImportLookup.get(identifier.name());
            if (importedBinaryName != null && !importedBinaryName.isBlank()) {
                return QinIrTypeRef.classType(importedBinaryName);
            }
            if (localDeclarationNames.contains(identifier.name())) {
                return QinIrTypeRef.classType(identifier.name());
            }
            return switch (identifier.name()) {
                case "String" -> QinIrTypeRef.stringType();
                case "Boolean" -> QinIrTypeRef.booleanType();
                case "Double", "Number" -> QinIrTypeRef.doubleType();
                default -> QinIrTypeRef.classType(identifier.name());
            };
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private List<QinIrStatement> lowerMethodBodyStatementsOrEmpty(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (function == null || function.body() == null || function.body().body() == null) {
            return List.of();
        }
        if (!containsStatementBodySignal(function.body().body())) {
            return List.of();
        }
        return lowerDeclarationStatements(function.body().body(), javaImportLookup, classContext, Map.of());
    }

    private boolean containsStatementBodySignal(List<? extends Statement> statements) {
        if (statements == null || statements.isEmpty()) {
            return false;
        }
        for (Statement statement : statements) {
            if (statement instanceof ThrowStatement
                    || statement instanceof TryStatement
                    || statement instanceof WhileStatement) {
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

    private List<QinIrStatement> lowerDeclarationStatements(
            List<? extends Statement> statements,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (statements == null || statements.isEmpty()) {
            return List.of();
        }
        Map<String, QinIrExpression> scopedLocals = new LinkedHashMap<>(locals);
        List<QinIrStatement> lowered = new ArrayList<>();
        for (Statement statement : statements) {
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
                lowered.add(new QinIrIfStatement(
                        lowerDeclarationExpression(ifStatement.test(), javaImportLookup, classContext, scopedLocals),
                        lowerDeclarationStatements(
                                statementList(ifStatement.consequent()),
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
            throw qjsError("QJS2024", "Unsupported declaration statement body node: "
                    + QinSlimeFrontendAdapter.simpleName(statement));
        }
        return List.copyOf(lowered);
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
            DeclarationClassContext classContext) {
        if (function == null || function.body() == null || function.body().body() == null) {
            return new QinIrNullLiteral();
        }
        return lowerDeclarationMethodBody(function.body().body(), javaImportLookup, classContext, Map.of());
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
        return new QinIrNullLiteral();
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
            lowerBindingPatternLocals(declarator.id(), initializer, locals);
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
                    || declarator.init() == null
                    || !(declarator.id() instanceof Identifier identifier)
                    || identifier.name() == null
                    || identifier.name().isBlank()) {
                return List.of();
            }
            QinIrExpression initializer = lowerDeclarationExpression(
                    declarator.init(),
                    javaImportLookup,
                    classContext,
                    locals);
            QinIrLocalDeclarationStatement statement =
                    new QinIrLocalDeclarationStatement(identifier.name(), initializer);
            lowered.add(statement);
            locals.put(identifier.name(), new QinIrIdentifierReference(identifier.name()));
        }
        return List.copyOf(lowered);
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
        QinIrExpression consequent = extractDeclarationBranchReturnExpression(
                ifStatement.consequent(),
                javaImportLookup,
                classContext,
                locals);
        QinIrExpression alternate = extractDeclarationBranchReturnExpression(
                ifStatement.alternate(),
                javaImportLookup,
                classContext,
                locals);
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
        QinIrExpression consequent = extractDeclarationBranchReturnExpression(
                ifStatement.consequent(),
                javaImportLookup,
                classContext,
                locals);
        if (consequent == null) {
            return new QinIrNullLiteral();
        }
        if (ifStatement.alternate() != null) {
            return lowerDeclarationIfReturnExpression(ifStatement, javaImportLookup, classContext, locals);
        }
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, fallthrough));
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
        if (returnExpression instanceof QinIrIdentifierReference identifierReference) {
            QinIrParameter parameter = resolveDeclarationParameter(parameters, identifierReference.name());
            if (parameter != null) {
                return parameter.type();
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
        if (returnExpression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
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
        if (returnExpression instanceof QinIrObjectLiteral) {
            return QinIrTypeRef.classType("java.util.Map");
        }
        if (returnExpression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return inferDeclarationBuiltinCallReturnType(builtinCallExpression, parameters, classContext);
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrExpression lowerDeclarationStatementExpression(
            Expression expression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression lowered = lowerDeclarationExpression(expression, javaImportLookup, classContext, locals);
        if (lowered instanceof QinIrBuiltinCallExpression
                || lowered instanceof QinIrAssignmentExpression
                || lowered instanceof QinIrInstanceMethodCallExpression) {
            return lowered;
        }
        throw qjsError("QJS2021", "Unsupported declaration expression statement");
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
            QinIrTypeRef consequentType = inferDeclarationReturnType(
                    builtinCallExpression.arguments().get(1),
                    parameters,
                    classContext);
            QinIrTypeRef alternateType = inferDeclarationReturnType(
                    builtinCallExpression.arguments().get(2),
                    parameters,
                    classContext);
            return mergeDeclarationBranchTypes(consequentType, alternateType);
        }
        QinIrTypeRef semanticType = inferDeclarationBuiltinSemanticReturnType(
                builtinCallExpression.receiverName(),
                builtinCallExpression.methodName());
        if (semanticType != null) {
            return semanticType;
        }
        return inferRegisteredDeclarationBuiltinReturnType(builtinCallExpression);
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
                case "includes", "some", "every" -> QinIrTypeRef.booleanType();
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
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private boolean isDeclarationNumericLike(QinIrTypeRef typeRef) {
        return typeRef != null && (
                typeRef.equals(QinIrTypeRef.intType())
                        || typeRef.equals(QinIrTypeRef.doubleType())
                        || "java.lang.Integer".equals(typeRef.binaryName())
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
        return QinIrTypeRef.classType("java.lang.Object");
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
            return local != null ? local : adapter.lowerIdentifierExpression(identifier);
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
        if (expressionAst instanceof NewExpression) {
            return adapter.lowerRuntimeExpression(expressionAst, javaImportLookup, Map.of());
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
                return local != null ? local : adapter.lowerExpression(expressionAst, javaImportLookup);
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
        if ("AssignmentExpression".equals(nodeType)) {
            return lowerDeclarationAssignmentExpression(expressionAst, javaImportLookup, classContext, locals);
        }
        if ("NewExpression".equals(nodeType)) {
            return adapter.lowerRuntimeExpression(expressionAst, javaImportLookup, Map.of());
        }
        throw qjsError("QJS2014", "Unsupported declaration return expression: " + nodeType);
    }

    private QinIrAssignmentExpression lowerDeclarationAssignmentExpression(
            AssignmentExpression assignmentExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        return new QinIrAssignmentExpression(
                lowerDeclarationExpression(assignmentExpression.left(), javaImportLookup, classContext, locals),
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
                lowerDeclarationExpression(leftAst, javaImportLookup, classContext, locals),
                operator,
                lowerDeclarationExpression(rightAst, javaImportLookup, classContext, locals));
    }

    private QinIrObjectLiteral lowerDeclarationObjectLiteral(
            Object objectExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<?> properties = QinSlimeFrontendAdapter.asListStatic(
                QinSlimeFrontendAdapter.invokeByName(objectExpressionAst, "properties"),
                "ObjectExpression.properties");
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (Object property : properties) {
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
        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrObjectLiteral lowerDeclarationObjectLiteral(
            ObjectExpression objectExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (AstNode propertyNode : objectExpressionAst.properties()) {
            if (!(propertyNode instanceof Property property)) {
                throw qjsError("QJS2019", "Only normal object property is supported in declaration subset");
            }
            String key = adapter.extractPropertyKey(property.key());
            QinIrExpression value = lowerDeclarationExpression(property.value(), javaImportLookup, classContext, locals);
            validateDeclarationObjectPropertyValue(value);
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    private void validateDeclarationObjectPropertyValue(QinIrExpression value) {
        if (value instanceof QinIrNumberLiteral
                || value instanceof QinIrStringLiteral
                || value instanceof QinIrBooleanLiteral
                || value instanceof QinIrNullLiteral
                || value instanceof QinIrIdentifierReference
                || value instanceof QinIrMemberAccessExpression
                || value instanceof QinIrPropertyAccessExpression
                || value instanceof QinIrInstanceMethodCallExpression
                || value instanceof QinIrBuiltinCallExpression
                || value instanceof QinIrObjectLiteral) {
            return;
        }
        throw qjsError("QJS2020", "Unsupported declaration object property value expression");
    }

    private QinIrBuiltinCallExpression lowerDeclarationBinaryExpression(
            BinaryExpression expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression left = lowerDeclarationExpression(expressionAst.left(), javaImportLookup, classContext, locals);
        QinIrExpression right = lowerDeclarationExpression(expressionAst.right(), javaImportLookup, classContext, locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), left, right));
    }

    private QinIrBuiltinCallExpression lowerDeclarationBinaryExpression(
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
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(operator), left, right));
    }

    private QinIrBuiltinCallExpression lowerDeclarationLogicalExpression(
            LogicalExpression expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        QinIrExpression left = lowerDeclarationExpression(expressionAst.left(), javaImportLookup, classContext, locals);
        QinIrExpression right = lowerDeclarationExpression(expressionAst.right(), javaImportLookup, classContext, locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_logical__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), left, right));
    }

    private QinIrBuiltinCallExpression lowerDeclarationLogicalExpression(
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
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_logical__",
                List.of(new QinIrStringLiteral(operator), left, right));
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
        if (memberExpressionAst.computed()) {
            throw qjsError("QJS2015", "Computed member access is not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                memberExpressionAst.object(),
                javaImportLookup,
                classContext,
                locals);
        String propertyName = adapter.extractMemberPropertyName(memberExpressionAst.property());
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
        boolean computed = Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "computed"));
        if (computed) {
            throw qjsError("QJS2015", "Computed member access is not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "object"),
                javaImportLookup,
                classContext,
                locals);
        String propertyName = adapter.extractMemberPropertyName(
                QinSlimeFrontendAdapter.invokeByName(memberExpressionAst, "property"));
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            return new QinIrMemberAccessExpression(identifierReference.name(), propertyName);
        }
        return new QinIrPropertyAccessExpression(receiver, propertyName);
    }

    private QinIrExpression lowerDeclarationCallExpression(
            CallExpression callExpression,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        if (!(callExpression.callee() instanceof MemberExpression memberExpression)) {
            throw qjsError("QJS2016", "Only member call expressions are supported in declaration subset");
        }
        if (memberExpression.computed()) {
            throw qjsError("QJS2017", "Computed method calls are not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                memberExpression.object(),
                javaImportLookup,
                classContext,
                locals);
        String methodName = adapter.extractMemberPropertyName(memberExpression.property());
        List<QinIrExpression> arguments = lowerDeclarationCallArguments(
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
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
        return new QinIrInstanceMethodCallExpression(receiver, methodName, arguments);
    }

    private QinIrExpression lowerDeclarationCallExpression(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object callee = QinSlimeFrontendAdapter.invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(QinSlimeFrontendAdapter.simpleName(callee))) {
            throw qjsError("QJS2016", "Only member call expressions are supported in declaration subset");
        }
        boolean computed = Boolean.TRUE.equals(QinSlimeFrontendAdapter.invokeByName(callee, "computed"));
        if (computed) {
            throw qjsError("QJS2017", "Computed method calls are not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                QinSlimeFrontendAdapter.invokeByName(callee, "object"),
                javaImportLookup,
                classContext,
                locals);
        String methodName = adapter.extractMemberPropertyName(
                QinSlimeFrontendAdapter.invokeByName(callee, "property"));
        List<QinIrExpression> arguments = lowerDeclarationCallArguments(
                QinSlimeFrontendAdapter.asListStatic(
                        QinSlimeFrontendAdapter.invokeByName(callExpressionAst, "arguments"),
                        "CallExpression.arguments"),
                javaImportLookup,
                classContext,
                locals);
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
        return new QinIrInstanceMethodCallExpression(receiver, methodName, arguments);
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
        if (receiver instanceof QinIrStringLiteral
                || receiver instanceof QinIrBooleanLiteral
                || receiver instanceof QinIrNumberLiteral
                || receiver instanceof QinIrNullLiteral
                || receiver instanceof QinIrInstanceMethodCallExpression) {
            throw qjsError("QJS2018", "Unsupported declaration receiver expression");
        }
        return receiver;
    }

    private QinIrExpression lowerDeclarationInitializer(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression initializer = adapter.lowerRuntimeExpression(expressionAst, javaImportLookup, declarationLookup);
        if (initializer instanceof QinIrObjectLiteral
                || initializer instanceof com.qin.lang.ir.QinIrJavaNewExpression
                || initializer instanceof QinIrIdentifierReference
                || initializer instanceof QinIrMemberAccessExpression
                || initializer instanceof QinIrBuiltinCallExpression
                || initializer instanceof com.qin.lang.ir.QinIrFunctionLiteral
                || initializer instanceof QinIrNumberLiteral
                || initializer instanceof QinIrStringLiteral
                || initializer instanceof QinIrBooleanLiteral
                || initializer instanceof QinIrArrayLiteral
                || initializer instanceof QinIrNullLiteral) {
            return initializer;
        }
        throw qjsError("QJS2002", "Unsupported const initializer expression");
    }

    private List<QinIrAnnotation> lowerAnnotations(
            List<Decorator> decorators,
            Map<String, String> javaImportLookup) {
        if (decorators == null || decorators.isEmpty()) {
            return List.of();
        }

        List<QinIrAnnotation> annotations = new ArrayList<>();
        for (Decorator decorator : decorators) {
            QinIrAnnotation annotation = lowerAnnotationOrNull(decorator, javaImportLookup);
            if (annotation != null) {
                annotations.add(annotation);
            }
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
            String binaryName = javaImportLookup.get(identifier.name());
            if (binaryName == null) {
                return null;
            }
            return new QinIrAnnotation(binaryName, List.of());
        }

        if (expression instanceof CallExpression callExpression && callExpression.callee() instanceof Identifier identifier) {
            String binaryName = javaImportLookup.get(identifier.name());
            if (binaryName == null) {
                return null;
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

    private record DeclarationClassContext(
            String className,
            Map<String, QinIrFieldDeclaration> fields,
            List<QinIrMethodDeclaration> methods) {
        private DeclarationClassContext(
                String className,
                List<QinIrFieldDeclaration> fields,
                List<QinIrMethodDeclaration> methods) {
            this(
                    Objects.requireNonNull(className, "className cannot be null"),
                    indexFields(fields),
                    methods == null ? List.of() : List.copyOf(methods));
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
    }
}
