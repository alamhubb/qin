package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.slime.java.ast.JavaAstBinaryExpression;
import com.slime.java.ast.JavaAstClassDeclaration;
import com.slime.java.ast.JavaAstExpression;
import com.slime.java.ast.JavaAstFieldDeclaration;
import com.slime.java.ast.JavaAstIdentifierExpression;
import com.slime.java.ast.JavaAstImportDeclaration;
import com.slime.java.ast.JavaAstMethodDeclaration;
import com.slime.java.ast.JavaAstNumberLiteral;
import com.slime.java.ast.JavaAstParameter;
import com.slime.java.ast.JavaAstProgram;
import com.slime.java.ast.JavaAstStringLiteral;
import com.slime.java.ast.JavaCstToAst;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            methods.add(analyzeMethod(method, packageName, importedTypes, fieldTypes));
        }

        String binaryName = packageName == null || packageName.isBlank()
                ? classDeclaration.name()
                : packageName + "." + classDeclaration.name();
        return new QinJavaSemanticClass(packageName, classDeclaration.name(), binaryName, fields, methods);
    }

    private QinJavaSemanticMethod analyzeMethod(
            JavaAstMethodDeclaration method,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrTypeRef> fieldTypes) {
        List<QinJavaSemanticParameter> parameters = new ArrayList<>();
        Map<String, QinIrTypeRef> locals = new LinkedHashMap<>(fieldTypes);
        for (JavaAstParameter parameter : method.parameters()) {
            QinIrTypeRef type = resolveType(parameter.typeName(), packageName, importedTypes);
            parameters.add(new QinJavaSemanticParameter(parameter.name(), type));
            locals.put(parameter.name(), type);
        }
        QinIrTypeRef returnType = resolveType(method.returnTypeName(), packageName, importedTypes);
        QinIrTypeRef returnExpressionType = expressionType(method.returnExpression(), locals);
        return new QinJavaSemanticMethod(method.name(), returnType, parameters, returnExpressionType);
    }

    private QinIrTypeRef expressionType(JavaAstExpression expression, Map<String, QinIrTypeRef> locals) {
        if (expression == null) {
            return QinIrTypeRef.voidType();
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
        if (expression instanceof JavaAstBinaryExpression binary) {
            return binaryExpressionType(binary.operator(), expressionType(binary.left(), locals), expressionType(binary.right(), locals));
        }
        throw new IllegalArgumentException("Unsupported Java expression for semantics: " + expression);
    }

    private QinIrTypeRef binaryExpressionType(String operator, QinIrTypeRef left, QinIrTypeRef right) {
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
