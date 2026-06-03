package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.slime.java.ast.JavaAstAssignmentExpression;
import com.slime.java.ast.JavaAstBinaryExpression;
import com.slime.java.ast.JavaAstClassDeclaration;
import com.slime.java.ast.JavaAstExpression;
import com.slime.java.ast.JavaAstExpressionStatement;
import com.slime.java.ast.JavaAstFieldDeclaration;
import com.slime.java.ast.JavaAstForStatement;
import com.slime.java.ast.JavaAstIdentifierExpression;
import com.slime.java.ast.JavaAstIfStatement;
import com.slime.java.ast.JavaAstImportDeclaration;
import com.slime.java.ast.JavaAstLocalVariableDeclaration;
import com.slime.java.ast.JavaAstMemberAccessExpression;
import com.slime.java.ast.JavaAstMethodCallExpression;
import com.slime.java.ast.JavaAstMethodDeclaration;
import com.slime.java.ast.JavaAstNewExpression;
import com.slime.java.ast.JavaAstNumberLiteral;
import com.slime.java.ast.JavaAstParameter;
import com.slime.java.ast.JavaAstProgram;
import com.slime.java.ast.JavaAstReturnStatement;
import com.slime.java.ast.JavaAstStatement;
import com.slime.java.ast.JavaAstStringLiteral;
import com.slime.java.ast.JavaAstThisExpression;
import com.slime.java.ast.JavaAstWhileStatement;
import com.slime.java.ast.JavaCstToAst;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class QinJavaSemanticAnalyzer {
    public QinJavaSemanticModel analyzeSource(String source) {
        return analyzeProgram(JavaCstToAst.parse(source));
    }

    public QinJavaSemanticModel analyzeProgram(JavaAstProgram program) {
        Map<String, String> importedTypes = importedTypes(program.imports());
        List<QinJavaSemanticClass> classes = new ArrayList<>();
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            classes.add(analyzeClass(program.packageName(), importedTypes, classDeclaration));
        }
        return new QinJavaSemanticModel(classes);
    }

    QinIrTypeRef resolveType(String typeName, String packageName, Map<String, String> importedTypes) {
        return switch (typeName) {
            case "void" -> QinIrTypeRef.voidType();
            case "boolean" -> QinIrTypeRef.booleanType();
            case "byte", "short", "int", "long", "char" -> QinIrTypeRef.intType();
            case "float", "double" -> QinIrTypeRef.doubleType();
            case "String", "java.lang.String" -> QinIrTypeRef.stringType();
            default -> QinIrTypeRef.classType(resolveClassName(typeName, packageName, importedTypes));
        };
    }

    Map<String, String> importedTypes(List<JavaAstImportDeclaration> imports) {
        Map<String, String> importedTypes = new LinkedHashMap<>();
        for (JavaAstImportDeclaration importDeclaration : imports) {
            if (importDeclaration.onDemand()) {
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
            JavaAstClassDeclaration classDeclaration) {
        List<QinJavaSemanticField> fields = new ArrayList<>();
        Map<String, QinIrTypeRef> fieldTypes = new LinkedHashMap<>();
        for (JavaAstFieldDeclaration field : classDeclaration.fields()) {
            QinIrTypeRef type = resolveType(field.typeName(), packageName, importedTypes);
            fields.add(new QinJavaSemanticField(field.name(), type));
            fieldTypes.put(field.name(), type);
        }

        List<QinJavaSemanticMethod> methods = new ArrayList<>();
        String binaryName = packageName == null || packageName.isBlank()
                ? classDeclaration.name()
                : packageName + "." + classDeclaration.name();
        Map<String, QinIrTypeRef> methodReturnTypes = new LinkedHashMap<>();
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            methodReturnTypes.put(method.name(), resolveType(method.returnTypeName(), packageName, importedTypes));
        }
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            methods.add(analyzeMethod(method, packageName, importedTypes, fieldTypes, methodReturnTypes, binaryName));
        }

        return new QinJavaSemanticClass(packageName, classDeclaration.name(), binaryName, fields, methods);
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
            if (statement instanceof JavaAstForStatement forStatement) {
                analyzeForStatement(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        forStatement);
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
                analyzeStatements(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        ifStatement.consequentStatements());
                analyzeStatements(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        ifStatement.alternateStatements());
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
                analyzeStatements(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
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
                throw new IllegalArgumentException("Unknown Java identifier: " + identifier.name());
            }
            return type;
        }
        if (expression instanceof JavaAstNumberLiteral) {
            return QinIrTypeRef.doubleType();
        }
        if (expression instanceof JavaAstStringLiteral) {
            return QinIrTypeRef.stringType();
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
        if (expression instanceof JavaAstMemberAccessExpression memberAccess) {
            if (memberAccess.receiver() instanceof JavaAstThisExpression) {
                QinIrTypeRef fieldType = fieldTypes.get(memberAccess.propertyName());
                if (fieldType == null) {
                    throw new IllegalArgumentException("Unknown Java field: " + memberAccess.propertyName());
                }
                return fieldType;
            }
            throw new IllegalArgumentException("Unsupported Java member receiver for semantics: " + memberAccess.receiver());
        }
        if (expression instanceof JavaAstMethodCallExpression methodCall) {
            for (JavaAstExpression argument : methodCall.arguments()) {
                expressionType(argument, packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName);
            }
            if (methodCall.receiver() instanceof JavaAstThisExpression) {
                QinIrTypeRef returnType = methodReturnTypes.get(methodCall.methodName());
                if (returnType == null) {
                    throw new IllegalArgumentException("Unknown Java method: " + methodCall.methodName());
                }
                return returnType;
            }
            if (methodCall.receiver() instanceof JavaAstIdentifierExpression receiverIdentifier
                    && !locals.containsKey(receiverIdentifier.name())) {
                QinIrTypeRef ownerType = resolveType(receiverIdentifier.name(), packageName, importedTypes);
                if (ownerType.kind() == QinIrTypeKind.CLASS && ownerType.binaryName() != null) {
                    return resolveStaticMethodReturnType(
                            ownerType.binaryName(),
                            methodCall.methodName(),
                            methodCall.arguments().size());
                }
            }
            QinIrTypeRef receiverType = expressionType(
                    methodCall.receiver(),
                    packageName,
                    importedTypes,
                    locals,
                    fieldTypes,
                    methodReturnTypes,
                    classBinaryName);
            return resolveInstanceMethodReturnType(
                    receiverType,
                    methodCall.methodName(),
                    methodCall.arguments().size(),
                    methodReturnTypes,
                    classBinaryName);
        }
        if (expression instanceof JavaAstBinaryExpression binary) {
            return binaryExpressionType(
                    binary.operator(),
                    expressionType(binary.left(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName),
                    expressionType(binary.right(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName));
        }
        throw new IllegalArgumentException("Unsupported Java expression for semantics: " + expression);
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
                QinIrTypeRef localType = localVariable.initializer() == null
                        ? resolveType(localVariable.typeName(), packageName, importedTypes)
                        : expressionType(
                                localVariable.initializer(),
                                packageName,
                                importedTypes,
                                locals,
                                fieldTypes,
                                methodReturnTypes,
                                classBinaryName);
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
            if (statement instanceof JavaAstForStatement forStatement) {
                analyzeForStatement(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        forStatement);
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                expressionType(ifStatement.test(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                        classBinaryName);
                analyzeStatements(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        ifStatement.consequentStatements());
                analyzeStatements(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        ifStatement.alternateStatements());
                continue;
            }
            if (statement instanceof JavaAstWhileStatement whileStatement) {
                expressionType(whileStatement.test(), packageName, importedTypes, locals, fieldTypes, methodReturnTypes,
                        classBinaryName);
                analyzeStatements(packageName, importedTypes, locals, fieldTypes, methodReturnTypes, classBinaryName,
                        whileStatement.bodyStatements());
            }
        }
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
                QinIrTypeRef localType = localVariable.initializer() == null
                        ? resolveType(localVariable.typeName(), packageName, importedTypes)
                        : expressionType(
                                localVariable.initializer(),
                                packageName,
                                importedTypes,
                                scopedLocals,
                                fieldTypes,
                                methodReturnTypes,
                                classBinaryName);
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

    private QinIrTypeRef binaryExpressionType(String operator, QinIrTypeRef left, QinIrTypeRef right) {
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
            int argumentCount) {
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Method matched = null;
            for (Method method : ownerClass.getMethods()) {
                if (!method.getName().equals(methodName)
                        || method.getParameterCount() != argumentCount
                        || !Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                if (matched != null) {
                    throw new IllegalArgumentException("Ambiguous Java static method: "
                            + ownerBinaryName + "." + methodName + "/" + argumentCount);
                }
                matched = method;
            }
            if (matched == null) {
                throw new IllegalArgumentException("Unknown Java static method: "
                        + ownerBinaryName + "." + methodName + "/" + argumentCount);
            }
            return typeRefFromClass(matched.getReturnType());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java static method owner: " + ownerBinaryName, e);
        }
    }

    private QinIrTypeRef resolveInstanceMethodReturnType(
            QinIrTypeRef receiverType,
            String methodName,
            int argumentCount,
            Map<String, QinIrTypeRef> methodReturnTypes,
            String classBinaryName) {
        if (receiverType.kind() == QinIrTypeKind.STRING) {
            return resolveReflectiveInstanceMethodReturnType(
                    String.class.getName(),
                    methodName,
                    argumentCount);
        }
        if (receiverType.kind() != QinIrTypeKind.CLASS || receiverType.binaryName() == null) {
            throw new IllegalArgumentException(
                    "Unsupported Java instance method receiver type: " + receiverType);
        }
        if (receiverType.binaryName().equals(classBinaryName)) {
            QinIrTypeRef localReturnType = methodReturnTypes.get(methodName);
            if (localReturnType != null) {
                return localReturnType;
            }
        }
        return resolveReflectiveInstanceMethodReturnType(
                receiverType.binaryName(),
                methodName,
                argumentCount);
    }

    private QinIrTypeRef resolveReflectiveInstanceMethodReturnType(
            String ownerBinaryName,
            String methodName,
            int argumentCount) {
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Method matched = null;
            for (Method method : ownerClass.getMethods()) {
                if (!method.getName().equals(methodName)
                        || method.getParameterCount() != argumentCount
                        || Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                if (matched != null) {
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
                matched = method;
            }
            if (matched == null) {
                throw new IllegalArgumentException("Unknown Java instance method: "
                        + ownerBinaryName + "." + methodName + "/" + argumentCount);
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
        String imported = importedTypes.get(typeName);
        if (imported != null) {
            return imported;
        }
        if (typeName.contains(".")) {
            return typeName;
        }
        if (packageName != null && !packageName.isBlank()) {
            return packageName + "." + typeName;
        }
        return typeName;
    }
}
