package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
import com.slime.java.ast.JavaAstBinaryExpression;
import com.slime.java.ast.JavaAstClassDeclaration;
import com.slime.java.ast.JavaAstExpression;
import com.slime.java.ast.JavaAstFieldDeclaration;
import com.slime.java.ast.JavaAstIdentifierExpression;
import com.slime.java.ast.JavaAstLocalVariableDeclaration;
import com.slime.java.ast.JavaAstMemberAccessExpression;
import com.slime.java.ast.JavaAstMethodCallExpression;
import com.slime.java.ast.JavaAstMethodDeclaration;
import com.slime.java.ast.JavaAstNumberLiteral;
import com.slime.java.ast.JavaAstParameter;
import com.slime.java.ast.JavaAstProgram;
import com.slime.java.ast.JavaAstReturnStatement;
import com.slime.java.ast.JavaAstStatement;
import com.slime.java.ast.JavaAstStringLiteral;
import com.slime.java.ast.JavaAstThisExpression;
import com.slime.java.ast.JavaCstToAst;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QinJavaAstIrLowerer {
    private final QinJavaSemanticAnalyzer semanticAnalyzer = new QinJavaSemanticAnalyzer();

    public QinIrProgram lowerSource(String source) {
        return lowerProgram(JavaCstToAst.parse(source));
    }

    public QinIrProgram lowerProgram(JavaAstProgram program) {
        QinJavaSemanticModel semanticModel = semanticAnalyzer.analyzeProgram(program);
        Map<String, QinJavaSemanticClass> semanticClasses = semanticClassesBySimpleName(semanticModel);
        List<QinIrClassDeclaration> classes = new ArrayList<>();
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            classes.add(lowerClass(program.packageName(), classDeclaration, semanticClasses.get(classDeclaration.name())));
        }
        return new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                classes);
    }

    private QinIrClassDeclaration lowerClass(
            String packageName,
            JavaAstClassDeclaration classDeclaration,
            QinJavaSemanticClass semanticClass) {
        if (semanticClass == null) {
            throw new IllegalArgumentException("Missing semantic class for " + classDeclaration.name());
        }
        Map<String, QinJavaSemanticField> semanticFields = semanticFieldsByName(semanticClass);
        Map<String, QinJavaSemanticMethod> semanticMethods = semanticMethodsByName(semanticClass);
        List<QinIrFieldDeclaration> fields = new ArrayList<>();
        for (JavaAstFieldDeclaration field : classDeclaration.fields()) {
            QinJavaSemanticField semanticField = semanticFields.get(field.name());
            if (semanticField == null) {
                throw new IllegalArgumentException("Missing semantic field for " + field.name());
            }
            fields.add(new QinIrFieldDeclaration(
                    field.name(),
                    semanticField.type(),
                    List.of(),
                    null));
        }

        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            QinJavaSemanticMethod semanticMethod = semanticMethods.get(method.name());
            if (semanticMethod == null) {
                throw new IllegalArgumentException("Missing semantic method for " + method.name());
            }
            methods.add(lowerMethod(method, semanticMethod));
        }

        return new QinIrClassDeclaration(
                packageName,
                classDeclaration.name(),
                null,
                List.<QinIrAnnotation>of(),
                fields,
                methods);
    }

    private QinIrMethodDeclaration lowerMethod(
            JavaAstMethodDeclaration method,
            QinJavaSemanticMethod semanticMethod) {
        List<QinIrParameter> parameters = new ArrayList<>();
        Map<String, QinJavaSemanticParameter> semanticParameters = semanticParametersByName(semanticMethod);
        for (JavaAstParameter parameter : method.parameters()) {
            QinJavaSemanticParameter semanticParameter = semanticParameters.get(parameter.name());
            if (semanticParameter == null) {
                throw new IllegalArgumentException("Missing semantic parameter for " + parameter.name());
            }
            parameters.add(new QinIrParameter(
                    parameter.name(),
                    semanticParameter.type(),
                    List.of()));
        }
        return new QinIrMethodDeclaration(
                method.name(),
                semanticMethod.returnType(),
                parameters,
                List.of(),
                lowerMethodReturnExpression(method));
    }

    private QinIrExpression lowerMethodReturnExpression(JavaAstMethodDeclaration method) {
        Map<String, QinIrExpression> locals = new LinkedHashMap<>();
        for (JavaAstStatement statement : method.bodyStatements()) {
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                if (localVariable.initializer() != null) {
                    locals.put(localVariable.name(), lowerExpression(localVariable.initializer(), locals));
                }
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                return lowerExpression(returnStatement.expression(), locals);
            }
        }
        return lowerExpression(method.returnExpression(), locals);
    }

    private QinIrExpression lowerExpression(JavaAstExpression expression) {
        return lowerExpression(expression, Map.of());
    }

    private QinIrExpression lowerExpression(JavaAstExpression expression, Map<String, QinIrExpression> locals) {
        if (expression == null) {
            return null;
        }
        if (expression instanceof JavaAstIdentifierExpression identifier) {
            QinIrExpression local = locals.get(identifier.name());
            if (local != null) {
                return local;
            }
            return new QinIrIdentifierReference(identifier.name());
        }
        if (expression instanceof JavaAstNumberLiteral number) {
            return new QinIrNumberLiteral(number.value());
        }
        if (expression instanceof JavaAstStringLiteral string) {
            return new QinIrStringLiteral(string.value());
        }
        if (expression instanceof JavaAstThisExpression) {
            return new QinIrThisExpression();
        }
        if (expression instanceof JavaAstMemberAccessExpression memberAccess) {
            return new QinIrPropertyAccessExpression(
                    lowerExpression(memberAccess.receiver(), locals),
                    memberAccess.propertyName());
        }
        if (expression instanceof JavaAstMethodCallExpression methodCall) {
            List<QinIrExpression> arguments = new ArrayList<>();
            for (JavaAstExpression argument : methodCall.arguments()) {
                arguments.add(lowerExpression(argument, locals));
            }
            return new QinIrInstanceMethodCallExpression(
                    lowerExpression(methodCall.receiver(), locals),
                    methodCall.methodName(),
                    arguments);
        }
        if (expression instanceof JavaAstBinaryExpression binary) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_binary__",
                    List.of(
                            new QinIrStringLiteral(binary.operator()),
                            lowerExpression(binary.left(), locals),
                            lowerExpression(binary.right(), locals)));
        }
        throw new IllegalArgumentException("Unsupported Java AST expression: " + expression);
    }

    private Map<String, QinJavaSemanticClass> semanticClassesBySimpleName(QinJavaSemanticModel model) {
        Map<String, QinJavaSemanticClass> classes = new LinkedHashMap<>();
        for (QinJavaSemanticClass semanticClass : model.classes()) {
            classes.put(semanticClass.simpleName(), semanticClass);
        }
        return classes;
    }

    private Map<String, QinJavaSemanticField> semanticFieldsByName(QinJavaSemanticClass semanticClass) {
        Map<String, QinJavaSemanticField> fields = new LinkedHashMap<>();
        for (QinJavaSemanticField field : semanticClass.fields()) {
            fields.put(field.name(), field);
        }
        return fields;
    }

    private Map<String, QinJavaSemanticMethod> semanticMethodsByName(QinJavaSemanticClass semanticClass) {
        Map<String, QinJavaSemanticMethod> methods = new LinkedHashMap<>();
        for (QinJavaSemanticMethod method : semanticClass.methods()) {
            methods.put(method.name(), method);
        }
        return methods;
    }

    private Map<String, QinJavaSemanticParameter> semanticParametersByName(QinJavaSemanticMethod semanticMethod) {
        Map<String, QinJavaSemanticParameter> parameters = new LinkedHashMap<>();
        for (QinJavaSemanticParameter parameter : semanticMethod.parameters()) {
            parameters.put(parameter.name(), parameter);
        }
        return parameters;
    }
}
