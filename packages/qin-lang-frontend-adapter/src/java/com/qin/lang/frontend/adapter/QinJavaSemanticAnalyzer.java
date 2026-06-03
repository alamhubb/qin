package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.slime.java.ast.JavaAstAssignmentExpression;
import com.slime.java.ast.JavaAstArrayAccessExpression;
import com.slime.java.ast.JavaAstArrayLiteralExpression;
import com.slime.java.ast.JavaAstBinaryExpression;
import com.slime.java.ast.JavaAstBooleanLiteral;
import com.slime.java.ast.JavaAstCastExpression;
import com.slime.java.ast.JavaAstClassLiteralExpression;
import com.slime.java.ast.JavaAstClassDeclaration;
import com.slime.java.ast.JavaAstDoWhileStatement;
import com.slime.java.ast.JavaAstEnhancedForStatement;
import com.slime.java.ast.JavaAstExpression;
import com.slime.java.ast.JavaAstExpressionStatement;
import com.slime.java.ast.JavaAstFieldDeclaration;
import com.slime.java.ast.JavaAstForStatement;
import com.slime.java.ast.JavaAstIdentifierExpression;
import com.slime.java.ast.JavaAstIfStatement;
import com.slime.java.ast.JavaAstImportDeclaration;
import com.slime.java.ast.JavaAstInstanceofPatternExpression;
import com.slime.java.ast.JavaAstLambdaExpression;
import com.slime.java.ast.JavaAstLocalVariableDeclaration;
import com.slime.java.ast.JavaAstMemberAccessExpression;
import com.slime.java.ast.JavaAstMethodCallExpression;
import com.slime.java.ast.JavaAstMethodDeclaration;
import com.slime.java.ast.JavaAstMethodReferenceExpression;
import com.slime.java.ast.JavaAstNewExpression;
import com.slime.java.ast.JavaAstNullLiteral;
import com.slime.java.ast.JavaAstNumberLiteral;
import com.slime.java.ast.JavaAstParameter;
import com.slime.java.ast.JavaAstProgram;
import com.slime.java.ast.JavaAstReturnStatement;
import com.slime.java.ast.JavaAstStatement;
import com.slime.java.ast.JavaAstStringLiteral;
import com.slime.java.ast.JavaAstThisExpression;
import com.slime.java.ast.JavaAstUnaryExpression;
import com.slime.java.ast.JavaAstUpdateExpression;
import com.slime.java.ast.JavaAstWhileStatement;
import com.slime.java.ast.JavaCstToAst;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class QinJavaSemanticAnalyzer {
    private static final String LAMBDA_BINARY_NAME = "__qin.java.Lambda";
    private static final String NULL_BINARY_NAME = "__qin.java.Null";
    private Map<String, JavaAstClassDeclaration> sourceClassesByBinaryName = Map.of();
    private Map<String, Map<String, String>> sourceImportedTypesByBinaryName = Map.of();

    public QinJavaSemanticModel analyzeSource(String source) {
        return analyzeProgram(JavaCstToAst.parse(source));
    }

    public QinJavaSemanticModel analyzeProgram(JavaAstProgram program) {
        return analyzePrograms(List.of(program));
    }

    public QinJavaSemanticModel analyzePrograms(List<JavaAstProgram> programs) {
        Map<String, JavaAstClassDeclaration> sourceClasses = new LinkedHashMap<>();
        for (JavaAstProgram program : programs) {
            for (JavaAstClassDeclaration classDeclaration : program.classes()) {
                collectSourceClasses(program.packageName(), null, classDeclaration, sourceClasses);
            }
        }
        sourceClassesByBinaryName = sourceClasses;
        Map<String, Map<String, String>> importedTypesByClass = new LinkedHashMap<>();
        sourceImportedTypesByBinaryName = importedTypesByClass;
        for (JavaAstProgram program : programs) {
            Map<String, String> importedTypes = importedTypes(program.imports());
            for (JavaAstClassDeclaration classDeclaration : program.classes()) {
                collectSemanticClassScopes(program.packageName(), importedTypes, null, classDeclaration, importedTypesByClass);
            }
        }
        List<QinJavaSemanticClass> classes = new ArrayList<>();
        for (JavaAstProgram program : programs) {
            for (JavaAstClassDeclaration classDeclaration : program.classes()) {
                collectSemanticClasses(program.packageName(), null, classDeclaration, classes);
            }
        }
        return new QinJavaSemanticModel(classes);
    }

    public QinJavaSemanticModel analyzeProgramLegacy(JavaAstProgram program) {
        Map<String, String> importedTypes = importedTypes(program.imports());
        Map<String, JavaAstClassDeclaration> sourceClasses = new LinkedHashMap<>();
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            collectSourceClasses(program.packageName(), null, classDeclaration, sourceClasses);
        }
        sourceClassesByBinaryName = sourceClasses;
        Map<String, Map<String, String>> importedTypesByClass = new LinkedHashMap<>();
        sourceImportedTypesByBinaryName = importedTypesByClass;
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            collectSemanticClassScopes(program.packageName(), importedTypes, null, classDeclaration, importedTypesByClass);
        }
        List<QinJavaSemanticClass> classes = new ArrayList<>();
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            collectSemanticClasses(program.packageName(), null, classDeclaration, classes);
        }
        return new QinJavaSemanticModel(classes);
    }

    private void collectSourceClasses(
            String packageName,
            String ownerSimpleName,
            JavaAstClassDeclaration classDeclaration,
            Map<String, JavaAstClassDeclaration> classes) {
        String simpleName = ownerSimpleName == null
                ? classDeclaration.name()
                : ownerSimpleName + "$" + classDeclaration.name();
        String binaryName = packageName == null || packageName.isBlank()
                ? simpleName
                : packageName + "." + simpleName;
        classes.put(binaryName, classDeclaration);
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            collectSourceClasses(packageName, simpleName, nestedClass, classes);
        }
    }

    private void collectSemanticClassScopes(
            String packageName,
            Map<String, String> importedTypes,
            String ownerSimpleName,
            JavaAstClassDeclaration classDeclaration,
            Map<String, Map<String, String>> importedTypesByClass) {
        String simpleName = ownerSimpleName == null
                ? classDeclaration.name()
                : ownerSimpleName + "$" + classDeclaration.name();
        String binaryName = packageName == null || packageName.isBlank()
                ? simpleName
                : packageName + "." + simpleName;
        Map<String, String> classImportedTypes = new LinkedHashMap<>(importedTypes);
        classImportedTypes.put(classDeclaration.name(), binaryName);
        addOwnerNestedTypes(classImportedTypes, packageName, simpleName);
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            String nestedSimpleName = nestedClass.name();
            String nestedBinaryName = packageName == null || packageName.isBlank()
                    ? simpleName + "$" + nestedSimpleName
                    : packageName + "." + simpleName + "$" + nestedSimpleName;
            classImportedTypes.put(nestedSimpleName, nestedBinaryName);
        }
        addTypeParameterBounds(classImportedTypes, classDeclaration);
        importedTypesByClass.put(binaryName, classImportedTypes);
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            collectSemanticClassScopes(packageName, classImportedTypes, simpleName, nestedClass, importedTypesByClass);
        }
    }

    private void collectSemanticClasses(
            String packageName,
            String ownerSimpleName,
            JavaAstClassDeclaration classDeclaration,
            List<QinJavaSemanticClass> classes) {
        String simpleName = ownerSimpleName == null
                ? classDeclaration.name()
                : ownerSimpleName + "$" + classDeclaration.name();
        String binaryName = packageName == null || packageName.isBlank()
                ? simpleName
                : packageName + "." + simpleName;
        Map<String, String> importedTypes = sourceImportedTypesByBinaryName.getOrDefault(binaryName, Map.of());
        classes.add(analyzeClass(packageName, importedTypes, simpleName, classDeclaration));
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            collectSemanticClasses(packageName, simpleName, nestedClass, classes);
        }
    }

    private void addOwnerNestedTypes(Map<String, String> importedTypes, String packageName, String simpleName) {
        String ownerPrefix = (packageName == null || packageName.isBlank() ? "" : packageName + ".") + simpleName + "$";
        for (String binaryName : sourceClassesByBinaryName.keySet()) {
            if (!binaryName.startsWith(ownerPrefix)) {
                continue;
            }
            String suffix = binaryName.substring(ownerPrefix.length());
            if (!suffix.contains("$")) {
                importedTypes.putIfAbsent(suffix, binaryName);
            }
        }
    }

    private void addTypeParameterBounds(Map<String, String> importedTypes, JavaAstClassDeclaration classDeclaration) {
        for (var typeParameter : classDeclaration.typeParameters()) {
            String boundTypeName = typeParameter.boundTypeName() == null ? "Object" : typeParameter.boundTypeName();
            importedTypes.put(typeParameter.name(), resolveType(boundTypeName, null, importedTypes).binaryName());
        }
    }

    QinIrTypeRef resolveType(String typeName, String packageName, Map<String, String> importedTypes) {
        String normalizedTypeName = normalizeTypeName(typeName);
        int arrayDimensions = arrayDimensions(normalizedTypeName);
        String componentTypeName = stripArrayDimensions(normalizedTypeName);
        String rawTypeName = rawTypeName(componentTypeName);
        if (arrayDimensions > 0) {
            return QinIrTypeRef.classType(arrayBinaryName(rawTypeName, arrayDimensions, packageName, importedTypes));
        }
        return switch (rawTypeName) {
            case "void" -> QinIrTypeRef.voidType();
            case "boolean" -> QinIrTypeRef.booleanType();
            case "byte", "short", "int", "long", "char" -> QinIrTypeRef.intType();
            case "float", "double" -> QinIrTypeRef.doubleType();
            case "String", "java.lang.String" -> QinIrTypeRef.stringType();
            default -> QinIrTypeRef.classType(
                    resolveClassName(rawTypeName, packageName, importedTypes),
                    resolveTypeArguments(componentTypeName, packageName, importedTypes));
        };
    }

    String rawTypeName(String typeName) {
        String normalizedTypeName = normalizeTypeName(typeName);
        StringBuilder builder = new StringBuilder();
        int genericDepth = 0;
        for (int i = 0; i < normalizedTypeName.length(); i++) {
            char current = normalizedTypeName.charAt(i);
            if (current == '<') {
                genericDepth++;
                continue;
            }
            if (current == '>') {
                genericDepth--;
                if (genericDepth < 0) {
                    throw new IllegalArgumentException("Unbalanced Java generic type name: " + typeName);
                }
                continue;
            }
            if (genericDepth == 0) {
                builder.append(current);
            }
        }
        if (genericDepth != 0) {
            throw new IllegalArgumentException("Unbalanced Java generic type name: " + typeName);
        }
        return builder.toString();
    }

    Map<String, String> importedTypes(List<JavaAstImportDeclaration> imports) {
        Map<String, String> importedTypes = new LinkedHashMap<>();
        for (JavaAstImportDeclaration importDeclaration : imports) {
            if (importDeclaration.onDemand()) {
                importedTypes.put("*" + importDeclaration.name(), importDeclaration.name());
                continue;
            }
            String name = importDeclaration.name();
            int dot = name.lastIndexOf('.');
            if (dot >= 0 && dot < name.length() - 1) {
                importedTypes.put(name.substring(dot + 1), name);
            }
        }
        return importedTypes;
    }

    private QinJavaSemanticClass analyzeClass(
            String packageName,
            Map<String, String> importedTypes,
            String simpleName,
            JavaAstClassDeclaration classDeclaration) {
        importedTypes = withInheritedNestedTypes(
                importedTypes,
                classDeclaration.superTypeName(),
                packageName);
        List<QinJavaSemanticField> fields = new ArrayList<>();
        Map<String, QinIrTypeRef> fieldTypes = collectInheritedFieldTypes(
                classDeclaration.superTypeName(),
                packageName,
                importedTypes);
        for (JavaAstFieldDeclaration field : classDeclaration.fields()) {
            QinIrTypeRef type = resolveType(field.typeName(), packageName, importedTypes);
            fields.add(new QinJavaSemanticField(field.name(), type));
            fieldTypes.put(field.name(), type);
        }

        List<QinJavaSemanticMethod> methods = new ArrayList<>();
        String binaryName = packageName == null || packageName.isBlank()
                ? simpleName
                : packageName + "." + simpleName;
        Map<String, QinIrTypeRef> methodReturnTypes = collectInheritedMethodReturnTypes(
                classDeclaration.superTypeName(),
                packageName,
                importedTypes);
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            methodReturnTypes.put(method.name(), resolveType(method.returnTypeName(), packageName, importedTypes));
        }
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            try {
                methods.add(analyzeMethod(method, packageName, importedTypes, fieldTypes, methodReturnTypes, binaryName));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException(
                        "Could not analyze Java method: " + binaryName + "." + method.name(),
                        e);
            }
        }

        return new QinJavaSemanticClass(packageName, simpleName, binaryName, fields, methods);
    }

    private QinJavaSemanticMethod analyzeMethod(
            JavaAstMethodDeclaration method,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> fieldTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName) {
        List<QinJavaSemanticParameter> parameters = new ArrayList<>();
        Map<String, QinIrTypeRef> locals = new LinkedHashMap<>(fieldTypes);
        for (JavaAstParameter parameter : method.parameters()) {
            QinIrTypeRef type = resolveType(parameter.typeName(), packageName, importedTypes);
            parameters.add(new QinJavaSemanticParameter(parameter.name(), type));
            locals.put(parameter.name(), type);
        }
        QinIrTypeRef returnType = resolveType(method.returnTypeName(), packageName, importedTypes);
        QinIrTypeRef returnExpressionType = QinIrTypeRef.voidType();
        for (JavaAstStatement statement : method.bodyStatements()) {
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                if (localVariable.initializer() != null) {
                    expressionType(
                            localVariable.initializer(),
                            packageName,
                            importedTypes,
                            locals,
                            fieldTypes,
                            methodReturnTypes,
                            classBinaryName);
                }
                QinIrTypeRef localType = "var".equals(localVariable.typeName())
                        ? expressionType(
                                localVariable.initializer(),
                                packageName,
                                importedTypes,
                                locals,
                                fieldTypes,
                                methodReturnTypes,
                                classBinaryName)
                        : resolveType(localVariable.typeName(), packageName, importedTypes);
                locals.put(localVariable.name(), localType);
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                returnExpressionType = expressionType(
                        returnStatement.expression(),
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName);
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement expressionStatement) {
                expressionType(
                        expressionStatement.expression(),
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName);
                continue;
            }
            if (statement instanceof JavaAstDoWhileStatement doWhileStatement) {
                analyzeDoWhileStatement(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        doWhileStatement);
                continue;
            }
            if (statement instanceof JavaAstForStatement forStatement) {
                analyzeForStatement(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        forStatement);
                continue;
            }
            if (statement instanceof JavaAstEnhancedForStatement enhancedForStatement) {
                analyzeEnhancedForStatement(
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName,
                        enhancedForStatement);
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                expressionType(
                        ifStatement.test(),
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName);
                Map<String, QinIrTypeRef> consequentLocals = new LinkedHashMap<>(locals);
                Map<String, QinIrTypeRef> alternateLocals = new LinkedHashMap<>(locals);
                addInstanceofPatternLocals(
                        ifStatement.test(),
                        packageName,
                        importedTypes,
                        consequentLocals,
                        alternateLocals);
                analyzeStatements(packageName, importedTypes, consequentLocals, fieldTypes, methodReturnTypes, classBinaryName,
                        ifStatement.consequentStatements());
                analyzeStatements(packageName, importedTypes, alternateLocals, fieldTypes, methodReturnTypes, classBinaryName,
                        ifStatement.alternateStatements());
                if (isGuardReturnFalse(ifStatement)) {
                    locals.putAll(alternateLocals);
                }
                continue;
            }
            if (statement instanceof JavaAstWhileStatement whileStatement) {
                expressionType(
                        whileStatement.test(),
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName);
                analyzeStatements(packageName, importedTypes, new LinkedHashMap<>(locals), fieldTypes, methodReturnTypes, classBinaryName,
                        whileStatement.bodyStatements());
            }
        }
        if (method.bodyStatements().isEmpty()) {
            returnExpressionType = expressionType(
                    method.returnExpression(),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName);
        }
        return new QinJavaSemanticMethod(method.name(), returnType, parameters, returnExpressionType);
    }

    private QinIrTypeRef expressionType(
            JavaAstExpression expression,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> locals,
            Map<String, QinIrTypeRef> fieldTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName) {
        if (expression == null) {
            return QinIrTypeRef.voidType();
        }
        if (expression instanceof JavaAstThisExpression) {
            return QinIrTypeRef.classType(classBinaryName);
        }
        if (expression instanceof JavaAstIdentifierExpression identifier) {
            QinIrTypeRef type = locals.get(identifier.name());
            if (type == null) {
                QinIrTypeRef ownerType = staticOwnerType(identifier, packageName, importedTypes, locals);
                if (ownerType != null) {
                    return ownerType;
                }
            }
            if (type == null) {
                throw new IllegalArgumentException("Unknown Java identifier: " + identifier.name());
            }
            return type;
        }
        if (expression instanceof JavaAstNumberLiteral numberLiteral) {
            return numberLiteral.integral() ? QinIrTypeRef.intType() : QinIrTypeRef.doubleType();
        }
        if (expression instanceof JavaAstBooleanLiteral) {
            return QinIrTypeRef.booleanType();
        }
        if (expression instanceof JavaAstNullLiteral) {
            return QinIrTypeRef.classType(NULL_BINARY_NAME);
        }
        if (expression instanceof JavaAstStringLiteral) {
            return QinIrTypeRef.stringType();
        }
        if (expression instanceof JavaAstLambdaExpression lambdaExpression) {
            return QinIrTypeRef.classType(LAMBDA_BINARY_NAME);
        }
        if (expression instanceof JavaAstMethodReferenceExpression methodReference) {
            resolveType(methodReference.ownerName(), packageName, importedTypes);
            return QinIrTypeRef.classType(LAMBDA_BINARY_NAME);
        }
        if (expression instanceof JavaAstClassLiteralExpression) {
            return QinIrTypeRef.classType(Class.class.getName());
        }
        if (expression instanceof JavaAstInstanceofPatternExpression instanceofPattern) {
            expressionType(
                    instanceofPattern.value(),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName);
            return QinIrTypeRef.booleanType();
        }
        if (expression instanceof JavaAstCastExpression castExpression) {
            expressionType(
                    castExpression.expression(),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName);
            return resolveType(castExpression.typeName(), packageName, importedTypes);
        }
        if (expression instanceof JavaAstArrayLiteralExpression arrayLiteral) {
            for (JavaAstExpression element : arrayLiteral.elements()) {
                expressionType(element, packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName);
            }
            return resolveType(arrayLiteral.typeName(), packageName, importedTypes);
        }
        if (expression instanceof JavaAstNewExpression newExpression) {
            for (JavaAstExpression argument : newExpression.arguments()) {
                expressionType(argument, packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName);
            }
            return resolveType(newExpression.typeName(), packageName, importedTypes);
        }
        if (expression instanceof JavaAstAssignmentExpression assignment) {
            expressionType(assignment.target(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName);
            return expressionType(assignment.value(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName);
        }
        if (expression instanceof JavaAstUpdateExpression updateExpression) {
            return expressionType(updateExpression.target(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                    classBinaryName);
        }
        if (expression instanceof JavaAstUnaryExpression unaryExpression) {
            expressionType(unaryExpression.operand(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                    classBinaryName);
            return "!".equals(unaryExpression.operator()) ? QinIrTypeRef.booleanType() : QinIrTypeRef.doubleType();
        }
        if (expression instanceof JavaAstMemberAccessExpression memberAccess) {
            if (memberAccess.receiver() instanceof JavaAstThisExpression) {
                QinIrTypeRef fieldType = fieldTypes.get(memberAccess.propertyName());
                if (fieldType == null) {
                    throw new IllegalArgumentException("Unknown Java field: " + memberAccess.propertyName());
                }
                return fieldType;
            }
            QinIrTypeRef ownerType = staticOwnerType(memberAccess.receiver(), packageName, importedTypes, locals);
            if (ownerType != null) {
                return resolveStaticFieldType(ownerType.binaryName(), memberAccess.propertyName());
            }
            QinIrTypeRef receiverType = expressionType(
                    memberAccess.receiver(),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName);
            if ("length".equals(memberAccess.propertyName()) && isArrayBinaryName(receiverType.binaryName())) {
                return QinIrTypeRef.intType();
            }
            return resolveInstanceFieldType(receiverType, memberAccess.propertyName(), classBinaryName, importedTypes);
        }
        if (expression instanceof JavaAstArrayAccessExpression arrayAccess) {
            QinIrTypeRef receiverType = expressionType(
                    arrayAccess.receiver(),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName);
            expressionType(arrayAccess.index(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                    classBinaryName);
            QinIrTypeRef elementType = arrayElementType(receiverType.binaryName());
            if (elementType != null) {
                return elementType;
            }
            return QinIrTypeRef.classType(Object.class.getName());
        }
        if (expression instanceof JavaAstMethodCallExpression methodCall) {
            if (methodCall.receiver() instanceof JavaAstThisExpression) {
                List<QinIrTypeRef> argumentTypes = expressionTypes(
                        methodCall.arguments(),
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName,
                        List.of());
                QinIrTypeRef returnType = methodReturnTypes.get(methodCall.methodName());
                if (returnType != null) {
                    return returnType;
                }
                return resolveInstanceMethodReturnType(
                        QinIrTypeRef.classType(classBinaryName),
                        methodCall.methodName(),
                        argumentTypes,
                        methodReturnTypes,
                        classBinaryName);
            }
            if (!(methodCall.receiver() instanceof JavaAstIdentifierExpression receiverIdentifier
                    && "super".equals(receiverIdentifier.name()))) {
                QinIrTypeRef ownerType = staticOwnerType(methodCall.receiver(), packageName, importedTypes, locals);
                if (ownerType != null) {
                    List<QinIrTypeRef> argumentTypes = expressionTypes(
                            methodCall.arguments(),
                            packageName,
                            importedTypes,
                            locals,
                            fieldTypes,
                            methodReturnTypes,
                            classBinaryName,
                            List.of());
                    return resolveStaticMethodReturnType(
                            ownerType.binaryName(),
                            methodCall.methodName(),
                            argumentTypes);
                }
            }
            if (methodCall.receiver() instanceof JavaAstIdentifierExpression receiverIdentifier
                    && !"super".equals(receiverIdentifier.name())
                    && !locals.containsKey(receiverIdentifier.name())) {
                List<QinIrTypeRef> argumentTypes = expressionTypes(
                        methodCall.arguments(),
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName,
                        List.of());
                QinIrTypeRef ownerType = resolveType(receiverIdentifier.name(), packageName, importedTypes);
                if (isStaticOwnerType(ownerType)) {
                    return resolveStaticMethodReturnType(
                            ownerType.binaryName(),
                            methodCall.methodName(),
                            argumentTypes);
                }
            }
            if (methodCall.receiver() instanceof JavaAstIdentifierExpression receiverIdentifier
                    && "super".equals(receiverIdentifier.name())) {
                expressionTypes(
                        methodCall.arguments(),
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName,
                        List.of());
                QinIrTypeRef returnType = methodReturnTypes.get(methodCall.methodName());
                return returnType == null ? QinIrTypeRef.classType(Object.class.getName()) : returnType;
            }
            QinIrTypeRef receiverType = expressionType(
                    methodCall.receiver(),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName);
            List<QinIrTypeRef> argumentTypes = expressionTypes(
                    methodCall.arguments(),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName,
                    targetParameterTypes(receiverType, methodCall.methodName(), methodCall.arguments().size()));
            try {
                return resolveInstanceMethodReturnType(
                        receiverType,
                        methodCall.methodName(),
                        argumentTypes,
                        methodReturnTypes,
                        classBinaryName);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException(
                        "Could not resolve Java instance call: "
                                + receiverType.binaryName() + "." + methodCall.methodName()
                                + "/" + argumentTypes.size()
                                + " in " + classBinaryName,
                        e);
            }
        }
        if (expression instanceof JavaAstBinaryExpression binary) {
            return binaryExpressionType(
                    binary.operator(),
                    expressionType(binary.left(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName),
                    expressionType(binary.right(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName));
        }
        throw new IllegalArgumentException("Unsupported Java expression for semantics: " + expression);
    }

    QinIrTypeRef staticOwnerType(
            JavaAstExpression expression,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> locals) {
        String qualifiedName = qualifiedName(expression);
        if (qualifiedName == null || qualifiedName.isBlank()) {
            return null;
        }
        int dot = qualifiedName.indexOf('.');
        String rootName = dot < 0 ? qualifiedName : qualifiedName.substring(0, dot);
        if (locals.containsKey(rootName) || "this".equals(rootName) || "super".equals(rootName)) {
            return null;
        }
        QinIrTypeRef ownerType = resolveType(qualifiedName, packageName, importedTypes);
        return isStaticOwnerType(ownerType) || sourceClassesByBinaryName.containsKey(ownerType.binaryName()) ? ownerType : null;
    }

    String qualifiedName(JavaAstExpression expression) {
        if (expression instanceof JavaAstIdentifierExpression identifier) {
            return identifier.name();
        }
        if (expression instanceof JavaAstMemberAccessExpression memberAccess) {
            String receiverName = qualifiedName(memberAccess.receiver());
            return receiverName == null
                    ? null
                    : receiverName + "." + memberAccess.propertyName();
        }
        return null;
    }

    private List<QinIrTypeRef> expressionTypes(
            List<JavaAstExpression> expressions,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> locals,
            Map<String, QinIrTypeRef> fieldTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName,
            List<QinIrTypeRef> targetParameterTypes) {
        List<QinIrTypeRef> types = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            QinIrTypeRef targetParameterType = i < targetParameterTypes.size() ? targetParameterTypes.get(i) : null;
            types.add(expressionTypeWithTarget(
                    expressions.get(i),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName,
                    targetParameterType));
        }
        return types;
    }

    private QinIrTypeRef expressionTypeWithTarget(
            JavaAstExpression expression,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> locals,
            Map<String, QinIrTypeRef> fieldTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName,
            QinIrTypeRef targetParameterType) {
        if (expression instanceof JavaAstLambdaExpression lambdaExpression) {
            Map<String, QinIrTypeRef> lambdaLocals = new LinkedHashMap<>(locals);
            List<QinIrTypeRef> lambdaParameterTypes = lambdaParameterTypes(targetParameterType, lambdaExpression.parameterNames().size());
            for (int i = 0; i < lambdaExpression.parameterNames().size(); i++) {
                QinIrTypeRef parameterType = i < lambdaParameterTypes.size()
                        ? lambdaParameterTypes.get(i)
                        : QinIrTypeRef.classType(Object.class.getName());
                lambdaLocals.put(lambdaExpression.parameterNames().get(i), parameterType);
            }
            if (lambdaExpression.bodyExpression() != null) {
                expressionType(
                        lambdaExpression.bodyExpression(),
                        packageName,
                        importedTypes,
                        lambdaLocals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName);
            }
            if (!lambdaExpression.bodyStatements().isEmpty()) {
                analyzeStatements(
                        packageName,
                        importedTypes,
                        lambdaLocals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName,
                        lambdaExpression.bodyStatements());
            }
            return QinIrTypeRef.classType(LAMBDA_BINARY_NAME);
        }
        if (expression instanceof JavaAstMethodReferenceExpression methodReference) {
            resolveType(methodReference.ownerName(), packageName, importedTypes);
            return QinIrTypeRef.classType(LAMBDA_BINARY_NAME);
        }
        return expressionType(
                expression,
                packageName,
                importedTypes,
                locals,
                fieldTypes,
                methodReturnTypes,
                classBinaryName);
    }

    private void analyzeStatements(
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> inheritedLocals,
            Map<String, QinIrTypeRef> fieldTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName,
            List<JavaAstStatement> statements) {
        Map<String, QinIrTypeRef> locals = new LinkedHashMap<>(inheritedLocals);
        for (JavaAstStatement statement : statements) {
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrTypeRef initializerType = localVariable.initializer() == null
                        ? null
                        : expressionType(
                                localVariable.initializer(),
                                packageName,
                                importedTypes,
                                locals,
                                fieldTypes,
                                methodReturnTypes,
                                classBinaryName);
                QinIrTypeRef localType = "var".equals(localVariable.typeName())
                        ? initializerType
                        : resolveType(localVariable.typeName(), packageName, importedTypes);
                locals.put(localVariable.name(), localType);
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                expressionType(returnStatement.expression(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                        classBinaryName);
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement expressionStatement) {
                expressionType(expressionStatement.expression(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                        classBinaryName);
                continue;
            }
            if (statement instanceof JavaAstDoWhileStatement doWhileStatement) {
                analyzeDoWhileStatement(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        doWhileStatement);
                continue;
            }
            if (statement instanceof JavaAstForStatement forStatement) {
                analyzeForStatement(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        forStatement);
                continue;
            }
            if (statement instanceof JavaAstEnhancedForStatement enhancedForStatement) {
                analyzeEnhancedForStatement(
                        packageName,
                        importedTypes,
                        locals,
                        fieldTypes,
                        methodReturnTypes,
                        classBinaryName,
                        enhancedForStatement);
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                expressionType(ifStatement.test(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                        classBinaryName);
                Map<String, QinIrTypeRef> consequentLocals = new LinkedHashMap<>(locals);
                Map<String, QinIrTypeRef> alternateLocals = new LinkedHashMap<>(locals);
                addInstanceofPatternLocals(
                        ifStatement.test(),
                        packageName,
                        importedTypes,
                        consequentLocals,
                        alternateLocals);
                analyzeStatements(packageName, importedTypes, consequentLocals, fieldTypes, methodReturnTypes, classBinaryName,
                        ifStatement.consequentStatements());
                analyzeStatements(packageName, importedTypes, alternateLocals, fieldTypes, methodReturnTypes, classBinaryName,
                        ifStatement.alternateStatements());
                if (isGuardReturnFalse(ifStatement)) {
                    locals.putAll(alternateLocals);
                }
                continue;
            }
            if (statement instanceof JavaAstWhileStatement whileStatement) {
                expressionType(whileStatement.test(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                        classBinaryName);
                analyzeStatements(packageName, importedTypes, new LinkedHashMap<>(locals), fieldTypes, methodReturnTypes, classBinaryName,
                        whileStatement.bodyStatements());
            }
        }
    }

    private void addInstanceofPatternLocals(
            JavaAstExpression test,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> consequentLocals,
            Map<String, QinIrTypeRef> alternateLocals) {
        if (test instanceof JavaAstInstanceofPatternExpression pattern) {
            consequentLocals.put(
                    pattern.variableName(),
                    resolveType(pattern.typeName(), packageName, importedTypes));
            return;
        }
        if (test instanceof JavaAstUnaryExpression unaryExpression
                && "!".equals(unaryExpression.operator())
                && unaryExpression.operand() instanceof JavaAstInstanceofPatternExpression pattern) {
            alternateLocals.put(
                    pattern.variableName(),
                    resolveType(pattern.typeName(), packageName, importedTypes));
        }
    }

    private boolean isGuardReturnFalse(JavaAstIfStatement ifStatement) {
        return ifStatement.alternateStatements().isEmpty()
                && ifStatement.consequentStatements().size() == 1
                && ifStatement.consequentStatements().get(0) instanceof JavaAstReturnStatement returnStatement
                && returnStatement.expression() instanceof JavaAstBooleanLiteral booleanLiteral
                && !booleanLiteral.value();
    }

    private void analyzeDoWhileStatement(
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> locals,
            Map<String, QinIrTypeRef> fieldTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName,
            JavaAstDoWhileStatement doWhileStatement) {
        analyzeStatements(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                doWhileStatement.bodyStatements());
        expressionType(doWhileStatement.test(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                classBinaryName);
    }

    private void analyzeForStatement(
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> inheritedLocals,
            Map<String, QinIrTypeRef> fieldTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName,
            JavaAstForStatement forStatement) {
        Map<String, QinIrTypeRef> scopedLocals = new LinkedHashMap<>(inheritedLocals);
        for (JavaAstStatement initializer : forStatement.initializerStatements()) {
            if (initializer instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrTypeRef initializerType = localVariable.initializer() == null
                        ? null
                        : expressionType(
                                localVariable.initializer(),
                                packageName,
                                importedTypes,
                                scopedLocals,
                                fieldTypes,
                                methodReturnTypes,
                                classBinaryName);
                QinIrTypeRef localType = "var".equals(localVariable.typeName())
                        ? initializerType
                        : resolveType(localVariable.typeName(), packageName, importedTypes);
                scopedLocals.put(localVariable.name(), localType);
                continue;
            }
            if (initializer instanceof JavaAstExpressionStatement expressionStatement) {
                expressionType(expressionStatement.expression(), packageName, importedTypes, scopedLocals, fieldTypes,
                        methodReturnTypes, classBinaryName);
            }
        }
        expressionType(forStatement.test(), packageName, importedTypes, scopedLocals, fieldTypes, methodReturnTypes,
                classBinaryName);
        for (JavaAstExpression updateExpression : forStatement.updateExpressions()) {
            expressionType(updateExpression, packageName, importedTypes, scopedLocals, fieldTypes, methodReturnTypes,
                    classBinaryName);
        }
        analyzeStatements(packageName, importedTypes, scopedLocals, fieldTypes, methodReturnTypes, classBinaryName,
                forStatement.bodyStatements());
    }

    private void analyzeEnhancedForStatement(
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> inheritedLocals,
            Map<String, QinIrTypeRef> fieldTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName,
            JavaAstEnhancedForStatement enhancedForStatement) {
        expressionType(
                enhancedForStatement.iterableExpression(),
                packageName,
                importedTypes,
                inheritedLocals,
                fieldTypes,
                methodReturnTypes,
                classBinaryName);
        Map<String, QinIrTypeRef> scopedLocals = new LinkedHashMap<>(inheritedLocals);
        scopedLocals.put(
                enhancedForStatement.variableName(),
                resolveType(enhancedForStatement.variableTypeName(), packageName, importedTypes));
        analyzeStatements(
                packageName,
                importedTypes,
                scopedLocals,
                fieldTypes,
                methodReturnTypes,
                classBinaryName,
                enhancedForStatement.bodyStatements());
    }

    private QinIrTypeRef binaryExpressionType(String operator, QinIrTypeRef left, QinIrTypeRef right) {
        if ("&&".equals(operator) || "||".equals(operator)) {
            return QinIrTypeRef.booleanType();
        }
        if ("<".equals(operator)
                || ">".equals(operator)
                || "<=".equals(operator)
                || ">=".equals(operator)
                || "==".equals(operator)
                || "!=".equals(operator)) {
            return QinIrTypeRef.booleanType();
        }
        if ("+".equals(operator) && (left.kind() == QinIrTypeKind.STRING || right.kind() == QinIrTypeKind.STRING)) {
            return QinIrTypeRef.stringType();
        }
        if (left.kind() == QinIrTypeKind.DOUBLE || right.kind() == QinIrTypeKind.DOUBLE) {
            return QinIrTypeRef.doubleType();
        }
        if (left.kind() == QinIrTypeKind.INT && right.kind() == QinIrTypeKind.INT) {
            return QinIrTypeRef.intType();
        }
        return left;
    }

    private QinIrTypeRef resolveStaticMethodReturnType(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if ("com.github.benmanes.caffeine.cache.Caffeine".equals(ownerBinaryName)
                && "newBuilder".equals(methodName)
                && argumentTypes.isEmpty()) {
            return QinIrTypeRef.classType(
                    "com.github.benmanes.caffeine.cache.Caffeine",
                    List.of(
                            QinIrTypeRef.classType(Object.class.getName()),
                            QinIrTypeRef.classType(Object.class.getName())));
        }
        JavaAstClassDeclaration sourceClass = sourceClassesByBinaryName.get(ownerBinaryName);
        if (sourceClass != null) {
            Map<String, String> importedTypes = sourceImportedTypesByBinaryName.getOrDefault(ownerBinaryName, Map.of());
            for (JavaAstMethodDeclaration method : sourceClass.methods()) {
                if (method.name().equals(methodName) && method.parameters().size() == argumentTypes.size()) {
                    return resolveType(method.returnTypeName(), packageName(ownerBinaryName), importedTypes);
                }
            }
        }
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Method matched = null;
            int matchedScore = Integer.MAX_VALUE;
            for (Method method : ownerClass.getMethods()) {
                if (!method.getName().equals(methodName)
                        || !Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                int score = methodParameterScore(method, argumentTypes);
                if (score < 0) {
                    continue;
                }
                if (matched != null) {
                    if (score > matchedScore) {
                        continue;
                    }
                    if (score == matchedScore) {
                        Class<?> selectedReturnType = moreSpecificReturnType(
                                matched.getReturnType(),
                                method.getReturnType(),
                                ownerBinaryName,
                                methodName,
                                argumentTypes.size());
                        if (!Objects.equals(selectedReturnType, matched.getReturnType())) {
                            matched = method;
                        }
                        continue;
                    }
                }
                matched = method;
                matchedScore = score;
            }
            if (matched == null) {
                throw new IllegalArgumentException("Unknown Java static method: "
                        + ownerBinaryName + "." + methodName + "/" + argumentTypes.size());
            }
            return typeRefFromClass(matched.getReturnType());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java static method owner: " + ownerBinaryName, e);
        }
    }

    private int methodParameterScore(Method method, List<QinIrTypeRef> argumentTypes) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (!method.isVarArgs()) {
            if (parameterTypes.length != argumentTypes.size()) {
                return -1;
            }
            return methodParameterScore(parameterTypes, argumentTypes);
        }
        int fixedParameterCount = parameterTypes.length - 1;
        if (argumentTypes.size() < fixedParameterCount) {
            return -1;
        }
        int score = 20;
        for (int i = 0; i < fixedParameterCount; i++) {
            int parameterScore = methodParameterScore(parameterTypes[i], argumentTypes.get(i));
            if (parameterScore < 0) {
                return -1;
            }
            score += parameterScore;
        }
        Class<?> varargArrayType = parameterTypes[parameterTypes.length - 1];
        Class<?> varargComponentType = varargArrayType.getComponentType();
        if (varargComponentType == null) {
            return -1;
        }
        for (int i = fixedParameterCount; i < argumentTypes.size(); i++) {
            int parameterScore = methodParameterScore(varargComponentType, argumentTypes.get(i));
            if (parameterScore < 0) {
                return -1;
            }
            score += parameterScore + 1;
        }
        return score;
    }

    private int methodParameterScore(Class<?>[] parameterTypes, List<QinIrTypeRef> argumentTypes) {
        int score = 0;
        for (int i = 0; i < parameterTypes.length; i++) {
            int parameterScore = methodParameterScore(parameterTypes[i], argumentTypes.get(i));
            if (parameterScore < 0) {
                return -1;
            }
            score += parameterScore;
        }
        return score;
    }

    private int methodParameterScore(Class<?> parameterType, QinIrTypeRef argumentType) {
        return switch (argumentType.kind()) {
            case BOOLEAN -> parameterType == boolean.class || parameterType == Boolean.class
                    ? 0
                    : assignableObjectScore(parameterType, Boolean.class);
            case INT -> intParameterScore(parameterType);
            case DOUBLE -> doubleParameterScore(parameterType);
            case STRING -> parameterType == String.class
                    ? 0
                    : assignableObjectScore(parameterType, String.class);
            case CLASS -> classParameterScore(parameterType, argumentType.binaryName());
            case VOID -> -1;
        };
    }

    private int intParameterScore(Class<?> parameterType) {
        if (parameterType == int.class || parameterType == Integer.class) {
            return 0;
        }
        if (parameterType == long.class || parameterType == Long.class) {
            return 1;
        }
        if (parameterType == float.class || parameterType == Float.class) {
            return 2;
        }
        if (parameterType == double.class || parameterType == Double.class) {
            return 3;
        }
        if (parameterType == short.class || parameterType == Short.class
                || parameterType == byte.class || parameterType == Byte.class
                || parameterType == char.class || parameterType == Character.class) {
            return 4;
        }
        return assignableObjectScore(parameterType, Integer.class);
    }

    private int doubleParameterScore(Class<?> parameterType) {
        if (parameterType == double.class || parameterType == Double.class) {
            return 0;
        }
        if (parameterType == float.class || parameterType == Float.class) {
            return 1;
        }
        return assignableObjectScore(parameterType, Double.class);
    }

    private int classParameterScore(Class<?> parameterType, String argumentBinaryName) {
        if (LAMBDA_BINARY_NAME.equals(argumentBinaryName)) {
            return isFunctionalInterface(parameterType) ? 0 : -1;
        }
        if (NULL_BINARY_NAME.equals(argumentBinaryName)) {
            return parameterType.isPrimitive() ? -1 : 1;
        }
        if (argumentBinaryName == null || argumentBinaryName.isBlank()) {
            return parameterType == Object.class ? 10 : -1;
        }
        try {
            Class<?> argumentClass = Class.forName(argumentBinaryName);
            if (parameterType.equals(argumentClass)) {
                return 0;
            }
            int numericScore = boxedNumericParameterScore(parameterType, argumentClass);
            if (numericScore >= 0) {
                return numericScore;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(argumentClass)) {
                return 3;
            }
            return -1;
        } catch (ClassNotFoundException e) {
            return parameterType == Object.class ? 10 : -1;
        }
    }

    private int boxedNumericParameterScore(Class<?> parameterType, Class<?> argumentClass) {
        if (argumentClass == Integer.class
                || argumentClass == Long.class
                || argumentClass == Short.class
                || argumentClass == Byte.class
                || argumentClass == Character.class) {
            if (parameterType == int.class || parameterType == Integer.class) {
                return argumentClass == Integer.class ? 0 : 4;
            }
            if (parameterType == long.class || parameterType == Long.class) {
                return argumentClass == Long.class ? 0 : 1;
            }
            if (parameterType == float.class || parameterType == Float.class) {
                return 2;
            }
            if (parameterType == double.class || parameterType == Double.class) {
                return 3;
            }
        }
        if (argumentClass == Float.class || argumentClass == Double.class) {
            if (parameterType == double.class || parameterType == Double.class) {
                return argumentClass == Double.class ? 0 : 1;
            }
            if (parameterType == float.class || parameterType == Float.class) {
                return argumentClass == Float.class ? 0 : -1;
            }
        }
        return -1;
    }

    private int assignableObjectScore(Class<?> parameterType, Class<?> boxedArgumentType) {
        if (parameterType.isPrimitive()) {
            return -1;
        }
        if (parameterType.equals(boxedArgumentType)) {
            return 0;
        }
        return parameterType.isAssignableFrom(boxedArgumentType) ? 10 : -1;
    }

    private boolean isFunctionalInterface(Class<?> type) {
        if (!type.isInterface()) {
            return false;
        }
        int abstractMethodCount = 0;
        for (Method method : type.getMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                continue;
            }
            if (method.getDeclaringClass() == Object.class || isObjectMethodSignature(method)) {
                continue;
            }
            abstractMethodCount++;
        }
        return abstractMethodCount == 1;
    }

    private boolean isObjectMethodSignature(Method method) {
        try {
            Object.class.getMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private QinIrTypeRef resolveStaticFieldType(String ownerBinaryName, String fieldName) {
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Field field = ownerClass.getField(fieldName);
            if (!Modifier.isStatic(field.getModifiers())) {
                throw new IllegalArgumentException("Java field is not static: " + ownerBinaryName + "." + fieldName);
            }
            return typeRefFromType(field.getGenericType(), Map.of());
        } catch (NoSuchFieldException e) {
            JavaAstClassDeclaration sourceClass = sourceClassesByBinaryName.get(ownerBinaryName);
            if (sourceClass != null) {
                for (JavaAstFieldDeclaration field : sourceClass.fields()) {
                    if (field.name().equals(fieldName)) {
                        Map<String, String> importedTypes = sourceImportedTypesByBinaryName.getOrDefault(ownerBinaryName, Map.of());
                        return resolveType(field.typeName(), packageName(ownerBinaryName), importedTypes);
                    }
                }
            }
            throw new IllegalArgumentException("Unknown Java static field: " + ownerBinaryName + "." + fieldName, e);
        } catch (ClassNotFoundException e) {
            JavaAstClassDeclaration sourceClass = sourceClassesByBinaryName.get(ownerBinaryName);
            if (sourceClass != null) {
                for (JavaAstFieldDeclaration field : sourceClass.fields()) {
                    if (field.name().equals(fieldName)) {
                        Map<String, String> importedTypes = sourceImportedTypesByBinaryName.getOrDefault(ownerBinaryName, Map.of());
                        return resolveType(field.typeName(), packageName(ownerBinaryName), importedTypes);
                    }
                }
            }
            throw new IllegalArgumentException("Unknown Java static field owner: " + ownerBinaryName, e);
        }
    }

    private QinIrTypeRef resolveInstanceFieldType(
            QinIrTypeRef receiverType,
            String fieldName,
            String classBinaryName,
            Map<String, String> currentImportedTypes) {
        if (receiverType.kind() != QinIrTypeKind.CLASS || receiverType.binaryName() == null) {
            throw new IllegalArgumentException("Unsupported Java member receiver type: " + receiverType);
        }
        JavaAstClassDeclaration sourceClass = sourceClassesByBinaryName.get(receiverType.binaryName());
        if (sourceClass != null) {
            Map<String, String> importedTypes = sourceImportedTypesByBinaryName.get(receiverType.binaryName());
            if (importedTypes == null || importedTypes.isEmpty()) {
                importedTypes = currentImportedTypes;
            }
            for (JavaAstFieldDeclaration field : sourceClass.fields()) {
                if (field.name().equals(fieldName)) {
                    return resolveType(field.typeName(), packageName(receiverType.binaryName()), importedTypes);
                }
            }
        }
        if (receiverType.binaryName().equals(classBinaryName)) {
            throw new IllegalArgumentException("Unknown Java field: " + receiverType.binaryName() + "." + fieldName);
        }
        if (isLoadableClass(receiverType.binaryName())) {
            return resolveReflectiveInstanceFieldType(receiverType.binaryName(), fieldName);
        }
        throw new IllegalArgumentException("Unsupported Java member receiver for semantics: " + receiverType.binaryName());
    }

    private QinIrTypeRef resolveReflectiveInstanceFieldType(String ownerBinaryName, String fieldName) {
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Field field = ownerClass.getDeclaredField(fieldName);
            if (Modifier.isStatic(field.getModifiers())) {
                throw new IllegalArgumentException("Java field is static: " + ownerBinaryName + "." + fieldName);
            }
            return typeRefFromType(field.getGenericType(), Map.of());
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Unknown Java instance field: " + ownerBinaryName + "." + fieldName, e);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java instance field owner: " + ownerBinaryName, e);
        }
    }

    private String packageName(String binaryName) {
        int dot = binaryName.lastIndexOf('.');
        return dot < 0 ? null : binaryName.substring(0, dot);
    }

    boolean isLoadableClass(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            return false;
        }
        try {
            Class.forName(binaryName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean isStaticOwnerType(QinIrTypeRef type) {
        return type.binaryName() != null && isLoadableClass(type.binaryName());
    }

    Map<String, QinIrTypeRef> collectInheritedFieldTypes(
            String superTypeName,
            String packageName,
            Map<String, String> importedTypes) {
        Map<String, QinIrTypeRef> fields = new LinkedHashMap<>();
        if (superTypeName == null || superTypeName.isBlank()) {
            return fields;
        }
        QinIrTypeRef superType = resolveType(superTypeName, packageName, importedTypes);
        if (superType.binaryName() == null) {
            return fields;
        }
        JavaAstClassDeclaration sourceSuperClass = sourceClassesByBinaryName.get(superType.binaryName());
        if (sourceSuperClass != null) {
            Map<String, String> superImportedTypes = sourceImportedTypesByBinaryName.getOrDefault(
                    superType.binaryName(),
                    importedTypes);
            superImportedTypes = withTypeArgumentBindings(superImportedTypes, sourceSuperClass, superType);
            fields.putAll(collectInheritedFieldTypes(
                    sourceSuperClass.superTypeName(),
                    packageName(superType.binaryName()),
                    superImportedTypes));
            for (JavaAstFieldDeclaration field : sourceSuperClass.fields()) {
                fields.putIfAbsent(
                        field.name(),
                        resolveType(field.typeName(), packageName(superType.binaryName()), superImportedTypes));
            }
            return fields;
        }
        if (!isLoadableClass(superType.binaryName())) {
            return fields;
        }
        try {
            Class<?> owner = Class.forName(superType.binaryName());
            Map<TypeVariable<?>, Type> typeBindings = new LinkedHashMap<>();
            String accessingPackage = packageName == null ? "" : packageName;
            while (owner != null && owner != Object.class) {
                String ownerPackage = owner.getPackageName() == null ? "" : owner.getPackageName();
                for (Field field : owner.getDeclaredFields()) {
                    int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers) || Modifier.isPrivate(modifiers)) {
                        continue;
                    }
                    if (!Modifier.isPublic(modifiers)
                            && !Modifier.isProtected(modifiers)
                            && !ownerPackage.equals(accessingPackage)) {
                        continue;
                    }
                    fields.putIfAbsent(field.getName(), typeRefFromType(field.getGenericType(), typeBindings, new HashSet<>()));
                }
                Type genericSuperclass = owner.getGenericSuperclass();
                Class<?> rawSuperclass = owner.getSuperclass();
                typeBindings = nextTypeBindings(genericSuperclass, typeBindings);
                owner = rawSuperclass;
            }
            return fields;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java superclass: " + superType.binaryName(), e);
        }
    }

    private Map<String, String> withTypeArgumentBindings(
            Map<String, String> importedTypes,
            JavaAstClassDeclaration classDeclaration,
            QinIrTypeRef classType) {
        if (classDeclaration.typeParameters().isEmpty() || classType.typeArguments().isEmpty()) {
            return importedTypes;
        }
        Map<String, String> result = new LinkedHashMap<>(importedTypes);
        int count = Math.min(classDeclaration.typeParameters().size(), classType.typeArguments().size());
        for (int i = 0; i < count; i++) {
            QinIrTypeRef typeArgument = classType.typeArguments().get(i);
            if (typeArgument.binaryName() != null) {
                result.put(classDeclaration.typeParameters().get(i).name(), typeArgument.binaryName());
            }
        }
        return result;
    }

    Map<String, QinIrTypeRef> collectInheritedMethodReturnTypes(
            String superTypeName,
            String packageName,
            Map<String, String> importedTypes) {
        Map<String, QinIrTypeRef> methods = new LinkedHashMap<>();
        if (superTypeName == null || superTypeName.isBlank()) {
            return methods;
        }
        QinIrTypeRef superType = resolveType(superTypeName, packageName, importedTypes);
        if (superType.binaryName() == null || !isLoadableClass(superType.binaryName())) {
            return methods;
        }
        try {
            Class<?> owner = Class.forName(superType.binaryName());
            Map<TypeVariable<?>, Type> typeBindings = new LinkedHashMap<>();
            String accessingPackage = packageName == null ? "" : packageName;
            while (owner != null && owner != Object.class) {
                String ownerPackage = owner.getPackageName() == null ? "" : owner.getPackageName();
                for (Method method : owner.getDeclaredMethods()) {
                    int modifiers = method.getModifiers();
                    if (Modifier.isStatic(modifiers) || Modifier.isPrivate(modifiers)) {
                        continue;
                    }
                    if (!Modifier.isPublic(modifiers)
                            && !Modifier.isProtected(modifiers)
                            && !ownerPackage.equals(accessingPackage)) {
                        continue;
                    }
                    methods.putIfAbsent(method.getName(), typeRefFromType(method.getGenericReturnType(), typeBindings, new HashSet<>()));
                }
                Type genericSuperclass = owner.getGenericSuperclass();
                Class<?> rawSuperclass = owner.getSuperclass();
                typeBindings = nextTypeBindings(genericSuperclass, typeBindings);
                owner = rawSuperclass;
            }
            return methods;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java superclass: " + superType.binaryName(), e);
        }
    }

    Map<String, String> withInheritedNestedTypes(
            Map<String, String> importedTypes,
            String superTypeName,
            String packageName) {
        Map<String, String> resolved = new LinkedHashMap<>(importedTypes);
        if (superTypeName == null || superTypeName.isBlank()) {
            return resolved;
        }
        QinIrTypeRef superType = resolveType(superTypeName, packageName, importedTypes);
        if (superType.binaryName() == null || !isLoadableClass(superType.binaryName())) {
            return resolved;
        }
        try {
            Class<?> owner = Class.forName(superType.binaryName());
            String accessingPackage = packageName == null ? "" : packageName;
            while (owner != null && owner != Object.class) {
                String ownerPackage = owner.getPackageName() == null ? "" : owner.getPackageName();
                for (Class<?> nestedClass : owner.getDeclaredClasses()) {
                    int modifiers = nestedClass.getModifiers();
                    if (Modifier.isPrivate(modifiers)) {
                        continue;
                    }
                    if (!Modifier.isPublic(modifiers)
                            && !Modifier.isProtected(modifiers)
                            && !ownerPackage.equals(accessingPackage)) {
                        continue;
                    }
                    resolved.putIfAbsent(nestedClass.getSimpleName(), nestedClass.getName());
                }
                owner = owner.getSuperclass();
            }
            return resolved;
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java superclass: " + superType.binaryName(), e);
        }
    }

    private Map<TypeVariable<?>, Type> nextTypeBindings(
            Type genericSuperclass,
            Map<TypeVariable<?>, Type> currentBindings) {
        Map<TypeVariable<?>, Type> next = new LinkedHashMap<>();
        if (genericSuperclass instanceof ParameterizedType parameterizedType
                && parameterizedType.getRawType() instanceof Class<?> rawClass) {
            TypeVariable<?>[] variables = rawClass.getTypeParameters();
            Type[] arguments = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < variables.length && i < arguments.length; i++) {
                next.put(variables[i], resolveBoundType(arguments[i], currentBindings));
            }
        }
        return next;
    }

    private QinIrTypeRef typeRefFromType(Type type, Map<TypeVariable<?>, Type> typeBindings) {
        return typeRefFromType(type, typeBindings, new HashSet<>());
    }

    private QinIrTypeRef typeRefFromType(
            Type type,
            Map<TypeVariable<?>, Type> typeBindings,
            Set<Type> visiting) {
        Type resolved = resolveBoundType(type, typeBindings);
        if (!visiting.add(resolved)) {
            return QinIrTypeRef.classType(Object.class.getName());
        }
        if (resolved instanceof Class<?> resolvedClass) {
            return typeRefFromClass(resolvedClass);
        }
        if (resolved instanceof ParameterizedType parameterizedType
                && parameterizedType.getRawType() instanceof Class<?> rawClass) {
            List<QinIrTypeRef> typeArguments = new ArrayList<>();
            for (Type argument : parameterizedType.getActualTypeArguments()) {
                typeArguments.add(typeRefFromType(argument, typeBindings, visiting));
            }
            QinIrTypeRef rawTypeRef = typeRefFromClass(rawClass);
            if (rawTypeRef.kind() == QinIrTypeKind.CLASS) {
                return QinIrTypeRef.classType(rawTypeRef.binaryName(), typeArguments);
            }
            return rawTypeRef;
        }
        if (resolved instanceof TypeVariable<?> variable && variable.getBounds().length > 0) {
            return typeRefFromType(variable.getBounds()[0], typeBindings, visiting);
        }
        if (resolved instanceof WildcardType wildcardType) {
            Type[] upperBounds = wildcardType.getUpperBounds();
            if (upperBounds.length > 0) {
                return typeRefFromType(upperBounds[0], typeBindings, visiting);
            }
        }
        return QinIrTypeRef.classType(Object.class.getName());
    }

    private Type resolveBoundType(Type type, Map<TypeVariable<?>, Type> typeBindings) {
        Type current = type;
        while (current instanceof TypeVariable<?> variable && typeBindings.containsKey(variable)) {
            current = typeBindings.get(variable);
        }
        return current;
    }

    private QinIrTypeRef resolveInstanceMethodReturnType(
            QinIrTypeRef receiverType,
            String methodName,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName) {
        int argumentCount = argumentTypes.size();
        if (receiverType.kind() == QinIrTypeKind.STRING) {
            return resolveReflectiveInstanceMethodReturnType(
                    String.class.getName(),
                    methodName,
                    argumentTypes);
        }
        if (receiverType.kind() == QinIrTypeKind.BOOLEAN && "equals".equals(methodName) && argumentTypes.size() == 1) {
            return QinIrTypeRef.booleanType();
        }
        if (receiverType.kind() != QinIrTypeKind.CLASS || receiverType.binaryName() == null) {
            throw new IllegalArgumentException(
                    "Unsupported Java instance method receiver type: " + receiverType);
        }
        QinIrTypeRef genericCollectionReturnType = genericCollectionReturnType(receiverType, methodName, argumentTypes);
        if (genericCollectionReturnType != null) {
            return genericCollectionReturnType;
        }
        if (receiverType.binaryName().equals(classBinaryName)) {
            QinIrTypeRef localReturnType = methodReturnTypes.get(methodName);
            if (localReturnType != null) {
                return localReturnType;
            }
        }
        JavaAstClassDeclaration sourceClass = sourceClassesByBinaryName.get(receiverType.binaryName());
        if (sourceClass != null) {
            Map<String, String> importedTypes = sourceImportedTypesByBinaryName.getOrDefault(receiverType.binaryName(), Map.of());
            for (JavaAstMethodDeclaration method : sourceClass.methods()) {
                if (method.name().equals(methodName) && method.parameters().size() == argumentTypes.size()) {
                    return resolveType(method.returnTypeName(), packageName(receiverType.binaryName()), importedTypes);
                }
            }
            if (argumentTypes.isEmpty()) {
                for (JavaAstFieldDeclaration field : sourceClass.fields()) {
                    if (field.name().equals(methodName)) {
                        return resolveType(field.typeName(), packageName(receiverType.binaryName()), importedTypes);
                    }
                }
            }
            QinIrTypeRef sourceSuperType = sourceClass.superTypeName() == null
                    ? QinIrTypeRef.classType(Object.class.getName())
                    : resolveType(sourceClass.superTypeName(), packageName(receiverType.binaryName()), importedTypes);
            if (sourceSuperType.binaryName() != null && isLoadableClass(sourceSuperType.binaryName())) {
                return resolveReflectiveInstanceMethodReturnType(
                        sourceSuperType.binaryName(),
                        methodName,
                        argumentTypes);
            }
            throw new IllegalArgumentException("Unknown Java source instance method: "
                    + receiverType.binaryName() + "." + methodName + "/" + argumentCount);
        }
        return resolveReflectiveInstanceMethodReturnType(
                receiverType.binaryName(),
                methodName,
                argumentTypes);
    }

    private QinIrTypeRef resolveReflectiveInstanceMethodReturnType(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        int argumentCount = argumentTypes.size();
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Method matched = null;
            int matchedScore = Integer.MAX_VALUE;
            for (Method method : ownerClass.getMethods()) {
                if (!method.getName().equals(methodName)
                        || Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                int score = methodParameterScore(method, argumentTypes);
                if (score < 0) {
                    continue;
                }
                if (matched != null) {
                    if (score > matchedScore) {
                        continue;
                    }
                    if (score == matchedScore) {
                        Class<?> selectedReturnType = moreSpecificReturnType(
                                matched.getReturnType(),
                                method.getReturnType(),
                                ownerBinaryName,
                                methodName,
                                argumentCount);
                        if (!Objects.equals(selectedReturnType, matched.getReturnType())) {
                            matched = method;
                        }
                        continue;
                    }
                }
                matched = method;
                matchedScore = score;
            }
            if (matched == null) {
                throw new IllegalArgumentException("Unknown Java instance method: "
                        + ownerBinaryName + "." + methodName + "/" + argumentCount
                        + " arguments=" + argumentTypes);
            }
            return typeRefFromClass(matched.getReturnType());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java instance method owner: " + ownerBinaryName, e);
        }
    }

    private Class<?> moreSpecificReturnType(
            Class<?> first,
            Class<?> second,
            String ownerBinaryName,
            String methodName,
            int argumentCount) {
        if (Objects.equals(first, second)) {
            return first;
        }
        if (!first.isPrimitive() && !second.isPrimitive()) {
            if (first.isAssignableFrom(second)) {
                return second;
            }
            if (second.isAssignableFrom(first)) {
                return first;
            }
        }
        throw new IllegalArgumentException("Ambiguous Java instance method: "
                + ownerBinaryName + "." + methodName + "/" + argumentCount);
    }

    private QinIrTypeRef typeRefFromClass(Class<?> type) {
        if (type == void.class || type == Void.class) {
            return QinIrTypeRef.voidType();
        }
        if (type == boolean.class || type == Boolean.class) {
            return QinIrTypeRef.booleanType();
        }
        if (type == byte.class
                || type == short.class
                || type == int.class
                || type == long.class
                || type == char.class
                || type == Byte.class
                || type == Short.class
                || type == Integer.class
                || type == Long.class
                || type == Character.class) {
            return QinIrTypeRef.intType();
        }
        if (type == float.class || type == double.class || type == Float.class || type == Double.class) {
            return QinIrTypeRef.doubleType();
        }
        if (type == String.class) {
            return QinIrTypeRef.stringType();
        }
        return QinIrTypeRef.classType(type.getName());
    }

    private String resolveClassName(String typeName, String packageName, Map<String, String> importedTypes) {
        String rawTypeName = rawTypeName(typeName);
        String imported = importedTypes.get(rawTypeName);
        if (imported != null) {
            return imported;
        }
        if ("String".equals(rawTypeName)) {
            return String.class.getName();
        }
        if (rawTypeName.contains(".")) {
            return resolveDottedClassName(rawTypeName, packageName, importedTypes);
        }
        String javaLangClassName = "java.lang." + rawTypeName;
        if (isLoadableClass(javaLangClassName)) {
            return javaLangClassName;
        }
        for (String key : importedTypes.keySet()) {
            if (!key.startsWith("*")) {
                continue;
            }
            String className = key.substring(1) + "." + rawTypeName;
            if (isLoadableClass(className) || sourceClassesByBinaryName.containsKey(className)) {
                return className;
            }
        }
        String sourceNestedClassName = resolveSourceNestedClassName(rawTypeName, packageName);
        if (sourceNestedClassName != null) {
            return sourceNestedClassName;
        }
        if (packageName != null && !packageName.isBlank()) {
            return packageName + "." + rawTypeName;
        }
        return rawTypeName;
    }

    private String resolveSourceNestedClassName(String rawTypeName, String packageName) {
        if (rawTypeName == null || rawTypeName.isBlank() || rawTypeName.contains(".")) {
            return null;
        }
        String packagePrefix = packageName == null || packageName.isBlank() ? "" : packageName + ".";
        String suffix = "$" + rawTypeName;
        String matched = null;
        for (String binaryName : sourceClassesByBinaryName.keySet()) {
            if (!packagePrefix.isEmpty() && !binaryName.startsWith(packagePrefix)) {
                continue;
            }
            if (packagePrefix.isEmpty() && binaryName.contains(".")) {
                continue;
            }
            if (!binaryName.endsWith(suffix)) {
                continue;
            }
            if (matched != null && !matched.equals(binaryName)) {
                return null;
            }
            matched = binaryName;
        }
        return matched;
    }

    private String resolveDottedClassName(String rawTypeName, String packageName, Map<String, String> importedTypes) {
        if (isLoadableClass(rawTypeName)) {
            return rawTypeName;
        }
        String nestedCandidate = nestedBinaryName(rawTypeName);
        if (isLoadableClass(nestedCandidate)) {
            return nestedCandidate;
        }
        int firstDot = rawTypeName.indexOf('.');
        String rootName = rawTypeName.substring(0, firstDot);
        String nestedSuffix = rawTypeName.substring(firstDot);
        String importedRoot = importedTypes.get(rootName);
        if (importedRoot != null) {
            String importedNested = importedRoot + nestedSuffix.replace('.', '$');
            if (isLoadableClass(importedNested) || sourceClassesByBinaryName.containsKey(importedNested)) {
                return importedNested;
            }
        }
        for (String key : importedTypes.keySet()) {
            if (!key.startsWith("*")) {
                continue;
            }
            String importedNested = key.substring(1) + "." + rootName + nestedSuffix.replace('.', '$');
            if (isLoadableClass(importedNested) || sourceClassesByBinaryName.containsKey(importedNested)) {
                return importedNested;
            }
        }
        if (packageName != null && !packageName.isBlank()) {
            String packageNested = packageName + "." + rootName + nestedSuffix.replace('.', '$');
            if (isLoadableClass(packageNested) || sourceClassesByBinaryName.containsKey(packageNested)) {
                return packageNested;
            }
        }
        return rawTypeName;
    }

    private String nestedBinaryName(String rawTypeName) {
        String candidate = rawTypeName;
        int dot = candidate.lastIndexOf('.');
        while (dot > 0) {
            candidate = candidate.substring(0, dot) + "$" + candidate.substring(dot + 1);
            if (isLoadableClass(candidate)) {
                return candidate;
            }
            dot = candidate.lastIndexOf('.', dot - 1);
        }
        return candidate;
    }

    private static String normalizeTypeName(String typeName) {
        if (typeName == null) {
            throw new IllegalArgumentException("Java type name cannot be null");
        }
        return typeName.replaceAll("\\s+", "");
    }

    private static int arrayDimensions(String typeName) {
        int dimensions = 0;
        int index = typeName.length();
        while (index >= 2 && typeName.substring(index - 2, index).equals("[]")) {
            dimensions++;
            index -= 2;
        }
        return dimensions;
    }

    private static String stripArrayDimensions(String typeName) {
        int dimensions = arrayDimensions(typeName);
        return dimensions == 0 ? typeName : typeName.substring(0, typeName.length() - dimensions * 2);
    }

    private String arrayBinaryName(
            String rawComponentTypeName,
            int dimensions,
            String packageName,
            Map<String, String> importedTypes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dimensions; i++) {
            builder.append("[");
        }
        builder.append(switch (rawComponentTypeName) {
            case "boolean" -> "Z";
            case "byte" -> "B";
            case "short" -> "S";
            case "int" -> "I";
            case "long" -> "J";
            case "char" -> "C";
            case "float" -> "F";
            case "double" -> "D";
            case "void" -> throw new IllegalArgumentException("Java void array type is invalid");
            default -> "L" + resolveClassName(rawComponentTypeName, packageName, importedTypes) + ";";
        });
        return builder.toString();
    }

    private List<QinIrTypeRef> resolveTypeArguments(
            String typeName,
            String packageName,
            Map<String, String> importedTypes) {
        int start = typeName.indexOf('<');
        if (start < 0) {
            return List.of();
        }
        int end = matchingGenericEnd(typeName, start);
        String text = typeName.substring(start + 1, end);
        if (text.isBlank()) {
            return List.of();
        }
        List<QinIrTypeRef> typeArguments = new ArrayList<>();
        for (String argument : splitGenericArguments(text)) {
            if (argument.equals("?") || argument.startsWith("?extends") || argument.startsWith("?super")) {
                typeArguments.add(QinIrTypeRef.classType(Object.class.getName()));
                continue;
            }
            typeArguments.add(resolveType(argument, packageName, importedTypes));
        }
        return typeArguments;
    }

    private static int matchingGenericEnd(String typeName, int start) {
        int depth = 0;
        for (int i = start; i < typeName.length(); i++) {
            char current = typeName.charAt(i);
            if (current == '<') {
                depth++;
                continue;
            }
            if (current == '>') {
                depth--;
                if (depth == 0) {
                    return i;
                }
                if (depth < 0) {
                    break;
                }
            }
        }
        throw new IllegalArgumentException("Unbalanced Java generic type name: " + typeName);
    }

    private static List<String> splitGenericArguments(String text) {
        List<String> arguments = new ArrayList<>();
        int depth = 0;
        int start = 0;
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '<') {
                depth++;
                continue;
            }
            if (current == '>') {
                depth--;
                continue;
            }
            if (current == ',' && depth == 0) {
                arguments.add(text.substring(start, i));
                start = i + 1;
            }
        }
        arguments.add(text.substring(start));
        return arguments.stream().map(String::trim).filter(argument -> !argument.isBlank()).toList();
    }

    private QinIrTypeRef genericCollectionReturnType(
            QinIrTypeRef receiverType,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (receiverType.typeArguments().isEmpty()) {
            return null;
        }
        if ("get".equals(methodName)) {
            if (argumentTypes.size() != 1) {
                return null;
            }
            return switch (receiverType.binaryName()) {
                case "java.util.List", "java.util.ArrayList" -> receiverType.typeArguments().get(0);
                case "java.util.Map", "java.util.HashMap", "java.util.LinkedHashMap" ->
                    receiverType.typeArguments().size() > 1 ? receiverType.typeArguments().get(1) : null;
                default -> null;
            };
        }
        if ("remove".equals(methodName)
                && argumentTypes.size() == 1
                && ("java.util.Map".equals(receiverType.binaryName())
                || "java.util.HashMap".equals(receiverType.binaryName())
                || "java.util.LinkedHashMap".equals(receiverType.binaryName()))) {
            return receiverType.typeArguments().size() > 1 ? receiverType.typeArguments().get(1) : null;
        }
        if ("put".equals(methodName)
                && argumentTypes.size() == 2
                && ("java.util.Map".equals(receiverType.binaryName())
                || "java.util.HashMap".equals(receiverType.binaryName())
                || "java.util.LinkedHashMap".equals(receiverType.binaryName()))) {
            return receiverType.typeArguments().size() > 1 ? receiverType.typeArguments().get(1) : null;
        }
        if ("remove".equals(methodName) && argumentTypes.get(0).kind() == QinIrTypeKind.INT) {
            return switch (receiverType.binaryName()) {
                case "java.util.List", "java.util.ArrayList" -> receiverType.typeArguments().get(0);
                default -> null;
            };
        }
        if ("computeIfAbsent".equals(methodName)
                && argumentTypes.size() == 2
                && ("java.util.Map".equals(receiverType.binaryName())
                || "java.util.HashMap".equals(receiverType.binaryName())
                || "java.util.LinkedHashMap".equals(receiverType.binaryName()))) {
            return receiverType.typeArguments().size() > 1 ? receiverType.typeArguments().get(1) : null;
        }
        if ("merge".equals(methodName)
                && argumentTypes.size() == 3
                && ("java.util.Map".equals(receiverType.binaryName())
                || "java.util.HashMap".equals(receiverType.binaryName())
                || "java.util.LinkedHashMap".equals(receiverType.binaryName()))) {
            return receiverType.typeArguments().size() > 1 ? receiverType.typeArguments().get(1) : null;
        }
        if ("stream".equals(methodName)
                && argumentTypes.isEmpty()
                && ("java.util.Collection".equals(receiverType.binaryName())
                || "java.util.List".equals(receiverType.binaryName())
                || "java.util.ArrayList".equals(receiverType.binaryName()))) {
            return QinIrTypeRef.classType("java.util.stream.Stream", List.of(receiverType.typeArguments().get(0)));
        }
        if ("anyMatch".equals(methodName)
                && argumentTypes.size() == 1
                && "java.util.stream.Stream".equals(receiverType.binaryName())) {
            return QinIrTypeRef.booleanType();
        }
        if ("filter".equals(methodName)
                && argumentTypes.size() == 1
                && "java.util.stream.Stream".equals(receiverType.binaryName())) {
            return receiverType;
        }
        if ("collect".equals(methodName)
                && argumentTypes.size() == 1
                && "java.util.stream.Stream".equals(receiverType.binaryName())) {
            return QinIrTypeRef.classType("java.util.List", List.of(receiverType.typeArguments().get(0)));
        }
        if ("findFirst".equals(methodName)
                && argumentTypes.isEmpty()
                && "java.util.stream.Stream".equals(receiverType.binaryName())) {
            return QinIrTypeRef.classType("java.util.Optional", List.of(receiverType.typeArguments().get(0)));
        }
        if ("orElse".equals(methodName)
                && argumentTypes.size() == 1
                && "java.util.Optional".equals(receiverType.binaryName())) {
            return receiverType.typeArguments().get(0);
        }
        return null;
    }

    private List<QinIrTypeRef> targetParameterTypes(
            QinIrTypeRef receiverType,
            String methodName,
            int argumentCount) {
        if ("removalListener".equals(methodName)
                && argumentCount == 1
                && "com.github.benmanes.caffeine.cache.Caffeine".equals(receiverType.binaryName())) {
            QinIrTypeRef keyType = receiverType.typeArguments().size() > 0
                    ? receiverType.typeArguments().get(0)
                    : QinIrTypeRef.classType(Object.class.getName());
            QinIrTypeRef valueType = receiverType.typeArguments().size() > 1
                    ? receiverType.typeArguments().get(1)
                    : QinIrTypeRef.classType(Object.class.getName());
            return List.of(functionalTargetType(
                    "com.github.benmanes.caffeine.cache.RemovalListener",
                    List.of(
                            keyType,
                            valueType,
                            QinIrTypeRef.classType("com.github.benmanes.caffeine.cache.RemovalCause"))));
        }
        if (receiverType.typeArguments().isEmpty()) {
            return List.of();
        }
        if ("computeIfAbsent".equals(methodName)
                && argumentCount == 2
                && ("java.util.Map".equals(receiverType.binaryName())
                || "java.util.HashMap".equals(receiverType.binaryName())
                || "java.util.LinkedHashMap".equals(receiverType.binaryName()))) {
            return List.of(QinIrTypeRef.classType(Object.class.getName()), functionalTargetType(
                    "java.util.function.Function",
                    List.of(receiverType.typeArguments().get(0), QinIrTypeRef.classType(Object.class.getName()))));
        }
        if ("merge".equals(methodName)
                && argumentCount == 3
                && ("java.util.Map".equals(receiverType.binaryName())
                || "java.util.HashMap".equals(receiverType.binaryName())
                || "java.util.LinkedHashMap".equals(receiverType.binaryName()))) {
            QinIrTypeRef keyType = receiverType.typeArguments().get(0);
            QinIrTypeRef valueType = receiverType.typeArguments().size() > 1
                    ? receiverType.typeArguments().get(1)
                    : QinIrTypeRef.classType(Object.class.getName());
            return List.of(
                    keyType,
                    valueType,
                    functionalTargetType("java.util.function.BiFunction", List.of(valueType, valueType, valueType)));
        }
        if ("sort".equals(methodName)
                && argumentCount == 1
                && ("java.util.List".equals(receiverType.binaryName())
                || "java.util.ArrayList".equals(receiverType.binaryName()))) {
            QinIrTypeRef elementType = receiverType.typeArguments().get(0);
            return List.of(functionalTargetType("java.util.Comparator", List.of(elementType, elementType)));
        }
        if ("anyMatch".equals(methodName)
                && argumentCount == 1
                && "java.util.stream.Stream".equals(receiverType.binaryName())) {
            return List.of(functionalTargetType(
                    "java.util.function.Predicate",
                    List.of(receiverType.typeArguments().get(0))));
        }
        if ("filter".equals(methodName)
                && argumentCount == 1
                && "java.util.stream.Stream".equals(receiverType.binaryName())) {
            return List.of(functionalTargetType(
                    "java.util.function.Predicate",
                    List.of(receiverType.typeArguments().get(0))));
        }
        return List.of();
    }

    private QinIrTypeRef functionalTargetType(String binaryName, List<QinIrTypeRef> parameterTypes) {
        return QinIrTypeRef.classType(binaryName, parameterTypes);
    }

    private List<QinIrTypeRef> lambdaParameterTypes(QinIrTypeRef targetParameterType, int parameterCount) {
        if (targetParameterType == null || targetParameterType.typeArguments().isEmpty()) {
            return List.of();
        }
        if ("java.util.function.Function".equals(targetParameterType.binaryName()) && parameterCount == 1) {
            return List.of(targetParameterType.typeArguments().get(0));
        }
        if ("java.util.function.BiFunction".equals(targetParameterType.binaryName()) && parameterCount == 2) {
            return List.of(targetParameterType.typeArguments().get(0), targetParameterType.typeArguments().get(1));
        }
        if ("java.util.Comparator".equals(targetParameterType.binaryName()) && parameterCount == 2) {
            return List.of(targetParameterType.typeArguments().get(0), targetParameterType.typeArguments().get(1));
        }
        if ("java.util.function.Predicate".equals(targetParameterType.binaryName()) && parameterCount == 1) {
            return List.of(targetParameterType.typeArguments().get(0));
        }
        if ("com.github.benmanes.caffeine.cache.RemovalListener".equals(targetParameterType.binaryName())
                && parameterCount == 3
                && targetParameterType.typeArguments().size() >= 3) {
            return List.of(
                    targetParameterType.typeArguments().get(0),
                    targetParameterType.typeArguments().get(1),
                    targetParameterType.typeArguments().get(2));
        }
        return List.of();
    }

    private QinIrTypeRef genericCollectionReturnType(
            QinIrTypeRef receiverType,
            String methodName,
            int argumentCount) {
        if (!"get".equals(methodName) || argumentCount != 1 || receiverType.typeArguments().isEmpty()) {
            return null;
        }
        return switch (receiverType.binaryName()) {
            case "java.util.List", "java.util.ArrayList" -> receiverType.typeArguments().get(0);
            case "java.util.Map", "java.util.HashMap", "java.util.LinkedHashMap" ->
                receiverType.typeArguments().size() > 1 ? receiverType.typeArguments().get(1) : null;
            default -> null;
        };
    }

    private QinIrTypeRef arrayElementType(String binaryName) {
        if (!isArrayBinaryName(binaryName)) {
            return null;
        }
        String elementBinaryName = binaryName.substring(1);
        return switch (elementBinaryName) {
            case "Z" -> QinIrTypeRef.booleanType();
            case "B", "S", "I", "J", "C" -> QinIrTypeRef.intType();
            case "F", "D" -> QinIrTypeRef.doubleType();
            default -> {
                if (elementBinaryName.startsWith("L") && elementBinaryName.endsWith(";")) {
                    String className = elementBinaryName.substring(1, elementBinaryName.length() - 1);
                    if (String.class.getName().equals(className)) {
                        yield QinIrTypeRef.stringType();
                    }
                    yield QinIrTypeRef.classType(className);
                }
                if (elementBinaryName.startsWith("[")) {
                    yield QinIrTypeRef.classType(elementBinaryName);
                }
                yield QinIrTypeRef.classType(Object.class.getName());
            }
        };
    }

    private static boolean isArrayBinaryName(String binaryName) {
        return binaryName != null && binaryName.startsWith("[");
    }
}
