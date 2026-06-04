package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrCastExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrDoWhileExpression;
import com.qin.lang.ir.QinIrElementAccessExpression;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrForEachExpression;
import com.qin.lang.ir.QinIrForExpression;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrIfExpression;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaClassLiteralExpression;
import com.qin.lang.ir.QinIrJavaInstanceofPatternExpression;
import com.qin.lang.ir.QinIrJavaMethodReferenceExpression;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrLocalVariableDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinIrWhileExpression;
import com.slime.java.ast.JavaAstAssignmentExpression;
import com.slime.java.ast.JavaAstAnnotation;
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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class QinJavaAstIrLowerer {
    private final QinJavaSemanticAnalyzer semanticAnalyzer = new QinJavaSemanticAnalyzer();

    public QinIrProgram lowerSource(String source) {
        return lowerProgram(JavaCstToAst.parse(source));
    }

    public QinIrProgram lowerProgram(JavaAstProgram program) {
        return lowerPrograms(List.of(program));
    }

    public QinIrProgram lowerPrograms(List<JavaAstProgram> programs) {
        QinJavaSemanticModel semanticModel = semanticAnalyzer.analyzePrograms(programs);
        Map<String, QinJavaSemanticClass> semanticClasses = semanticClassesByBinaryName(semanticModel);
        List<QinIrClassDeclaration> classes = new ArrayList<>();
        for (JavaAstProgram program : programs) {
            Map<String, String> importedTypes = semanticAnalyzer.importedTypes(program.imports());
            for (JavaAstClassDeclaration classDeclaration : program.classes()) {
                lowerClassAndNested(
                        program.packageName(),
                        importedTypes,
                        null,
                        classDeclaration,
                        semanticClasses,
                        classes);
            }
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

    public QinIrProgram lowerSingleProgramLegacy(JavaAstProgram program) {
        QinJavaSemanticModel semanticModel = semanticAnalyzer.analyzePrograms(List.of(program));
        Map<String, String> importedTypes = semanticAnalyzer.importedTypes(program.imports());
        Map<String, QinJavaSemanticClass> semanticClasses = semanticClassesByBinaryName(semanticModel);
        List<QinIrClassDeclaration> classes = new ArrayList<>();
        for (JavaAstClassDeclaration classDeclaration : program.classes()) {
            lowerClassAndNested(
                    program.packageName(),
                    importedTypes,
                    null,
                    classDeclaration,
                    semanticClasses,
                    classes);
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

    private void lowerClassAndNested(
            String packageName,
            Map<String, String> importedTypes,
            String ownerSimpleName,
            JavaAstClassDeclaration classDeclaration,
            Map<String, QinJavaSemanticClass> semanticClasses,
            List<QinIrClassDeclaration> classes) {
        String simpleName = ownerSimpleName == null
                ? classDeclaration.name()
                : ownerSimpleName + "$" + classDeclaration.name();
        Map<String, String> classImportedTypes = new LinkedHashMap<>(importedTypes);
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            String nestedBinaryName = packageName == null || packageName.isBlank()
                    ? simpleName + "$" + nestedClass.name()
                    : packageName + "." + simpleName + "$" + nestedClass.name();
            classImportedTypes.put(nestedClass.name(), nestedBinaryName);
        }
        addTypeParameterBounds(classImportedTypes, classDeclaration);
        classes.add(lowerClass(
                packageName,
                classImportedTypes,
                simpleName,
                classDeclaration,
                semanticClasses.get(binaryName(packageName, simpleName))));
        for (JavaAstClassDeclaration nestedClass : classDeclaration.nestedClasses()) {
            lowerClassAndNested(packageName, classImportedTypes, simpleName, nestedClass, semanticClasses, classes);
        }
    }

    private void addTypeParameterBounds(Map<String, String> importedTypes, JavaAstClassDeclaration classDeclaration) {
        for (var typeParameter : classDeclaration.typeParameters()) {
            String boundTypeName = typeParameter.boundTypeName() == null ? "Object" : typeParameter.boundTypeName();
            importedTypes.put(typeParameter.name(), semanticAnalyzer.resolveType(boundTypeName, null, importedTypes).binaryName());
        }
    }

    private QinIrClassDeclaration lowerClass(
            String packageName,
            Map<String, String> importedTypes,
            String simpleName,
            JavaAstClassDeclaration classDeclaration,
            QinJavaSemanticClass semanticClass) {
        if (semanticClass == null) {
            throw new IllegalArgumentException("Missing semantic class for " + simpleName);
        }
        importedTypes = semanticAnalyzer.withInheritedNestedTypes(
                importedTypes,
                classDeclaration.superTypeName(),
                packageName);
        Map<String, QinJavaSemanticField> semanticFields = semanticFieldsByName(semanticClass);
        Map<String, QinJavaSemanticMethod> semanticMethods = semanticMethodsBySignature(semanticClass);
        Set<String> fieldNames = new LinkedHashSet<>(semanticAnalyzer
                .collectInheritedFieldTypes(classDeclaration.superTypeName(), packageName, importedTypes)
                .keySet());
        Map<String, QinIrExpression> fieldLocals = new LinkedHashMap<>();
        fieldLocals.putAll(fieldLocals(fieldNames));
        List<QinIrFieldDeclaration> fields = new ArrayList<>();
        String binaryName = binaryName(packageName, simpleName);
        for (JavaAstFieldDeclaration field : classDeclaration.fields()) {
            fieldNames.add(field.name());
            fieldLocals.put(field.name(), field.staticField()
                    ? new QinIrMemberAccessExpression(binaryName, field.name())
                    : new QinIrPropertyAccessExpression(new QinIrThisExpression(), field.name()));
            QinJavaSemanticField semanticField = semanticFields.get(field.name());
            if (semanticField == null) {
                throw new IllegalArgumentException("Missing semantic field for " + field.name());
            }
            fields.add(new QinIrFieldDeclaration(
                    field.name(),
                    semanticField.type(),
                    lowerAnnotations(packageName, importedTypes, field.annotations()),
                    field.initializer() == null
                            ? null
                            : lowerExpression(
                                    field.initializer(),
                                    packageName,
                                    importedTypes,
                                    fieldLocals,
                                    fieldNames),
                    field.staticField()));
        }

        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        for (JavaAstMethodDeclaration method : classDeclaration.methods()) {
            String key = methodKey(packageName, importedTypes, method);
            QinJavaSemanticMethod semanticMethod = semanticMethods.get(key);
            if (semanticMethod == null) {
                throw new IllegalArgumentException(
                        "Missing semantic method for " + methodSignatureForMessage(packageName, importedTypes, method)
                                + "; available=" + semanticMethods.keySet());
            }
            methods.add(lowerMethod(packageName, importedTypes, fieldNames, fieldLocals, method, semanticMethod));
        }
        if (classDeclaration.recordClass()) {
            addRecordCanonicalConstructor(classDeclaration, fields, methods);
        }

        return new QinIrClassDeclaration(
                packageName,
                simpleName,
                classDeclaration.superTypeName() == null
                        ? null
                        : semanticAnalyzer.resolveType(classDeclaration.superTypeName(), packageName, importedTypes),
                lowerAnnotations(packageName, importedTypes, classDeclaration.annotations()),
                fields,
                methods);
    }

    private void addRecordCanonicalConstructor(
            JavaAstClassDeclaration classDeclaration,
            List<QinIrFieldDeclaration> fields,
            List<QinIrMethodDeclaration> methods) {
        List<QinIrFieldDeclaration> components = recordComponentFields(classDeclaration, fields);
        if (components.isEmpty() || hasConstructorArity(methods, components.size())) {
            return;
        }
        List<QinIrParameter> parameters = new ArrayList<>();
        List<QinIrExpression> assignments = new ArrayList<>();
        for (QinIrFieldDeclaration component : components) {
            parameters.add(new QinIrParameter(component.name(), component.type(), List.of()));
            assignments.add(new QinIrAssignmentExpression(
                    new QinIrPropertyAccessExpression(new QinIrThisExpression(), component.name()),
                    "=",
                    new QinIrIdentifierReference(component.name())));
        }
        methods.add(new QinIrMethodDeclaration(
                "constructor",
                QinIrTypeRef.voidType(),
                parameters,
                List.of(),
                new QinIrSequenceExpression(assignments, new QinIrNullLiteral()),
                List.of(),
                null,
                false));
    }

    private List<QinIrFieldDeclaration> recordComponentFields(
            JavaAstClassDeclaration classDeclaration,
            List<QinIrFieldDeclaration> fields) {
        List<QinIrFieldDeclaration> components = new ArrayList<>();
        for (JavaAstFieldDeclaration sourceField : classDeclaration.fields()) {
            if (!sourceField.recordComponent()) {
                continue;
            }
            for (QinIrFieldDeclaration field : fields) {
                if (field.name().equals(sourceField.name())) {
                    components.add(field);
                    break;
                }
            }
        }
        return components;
    }

    private boolean hasConstructorArity(List<QinIrMethodDeclaration> methods, int arity) {
        for (QinIrMethodDeclaration method : methods) {
            if ("constructor".equals(method.name()) && method.parameters().size() == arity) {
                return true;
            }
        }
        return false;
    }

    private QinIrMethodDeclaration lowerMethod(
            String packageName,
            Map<String, String> importedTypes,
            Set<String> fieldNames,
            Map<String, QinIrExpression> fieldLocals,
            JavaAstMethodDeclaration method,
            QinJavaSemanticMethod semanticMethod) {
        List<QinIrParameter> parameters = new ArrayList<>();
        Set<String> valueNames = new LinkedHashSet<>();
        Map<String, QinJavaSemanticParameter> semanticParameters = semanticParametersByName(semanticMethod);
        for (JavaAstParameter parameter : method.parameters()) {
            valueNames.add(parameter.name());
            QinJavaSemanticParameter semanticParameter = semanticParameters.get(parameter.name());
            if (semanticParameter == null) {
                throw new IllegalArgumentException(
                        "Missing semantic parameter for " + parameter.name()
                                + " in " + methodSignatureForMessage(packageName, importedTypes, method)
                                + "; semantic parameters=" + semanticParameters.keySet());
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
                lowerAnnotations(packageName, importedTypes, method.annotations()),
                lowerMethodReturnExpression(packageName, importedTypes, fieldLocals, valueNames, method),
                lowerExplicitSuperArguments(packageName, importedTypes, fieldLocals, valueNames, method),
                null,
                method.staticMethod());
    }

    private List<QinIrExpression> lowerExplicitSuperArguments(
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> fieldLocals,
            Set<String> valueNames,
            JavaAstMethodDeclaration method) {
        if (!"constructor".equals(method.name()) || method.bodyStatements().isEmpty()) {
            return List.of();
        }
        JavaAstStatement firstStatement = method.bodyStatements().get(0);
        if (!(firstStatement instanceof JavaAstExpressionStatement expressionStatement
                && expressionStatement.expression() instanceof JavaAstMethodCallExpression methodCall
                && methodCall.receiver() instanceof JavaAstIdentifierExpression receiver
                && "super".equals(receiver.name()))) {
            return List.of();
        }
        Map<String, QinIrExpression> locals = new LinkedHashMap<>(fieldLocals);
        List<QinIrExpression> arguments = new ArrayList<>();
        for (JavaAstExpression argument : methodCall.arguments()) {
            arguments.add(lowerExpression(argument, packageName, importedTypes, locals, valueNames));
        }
        return arguments;
    }

    private List<QinIrAnnotation> lowerAnnotations(
            String packageName,
            Map<String, String> importedTypes,
            List<JavaAstAnnotation> annotations) {
        List<QinIrAnnotation> lowered = new ArrayList<>();
        for (JavaAstAnnotation annotation : annotations) {
            lowered.add(new QinIrAnnotation(
                    resolveAnnotationBinaryName(annotation.name(), packageName, importedTypes),
                    List.of()));
        }
        return lowered;
    }

    private String resolveAnnotationBinaryName(
            String annotationName,
            String packageName,
            Map<String, String> importedTypes) {
        String imported = importedTypes.get(annotationName);
        if (imported != null) {
            return imported;
        }
        if (annotationName.contains(".")) {
            return annotationName;
        }
        String javaLangName = "java.lang." + annotationName;
        try {
            Class.forName(javaLangName);
            return javaLangName;
        } catch (ClassNotFoundException ignored) {
            return packageName == null || packageName.isBlank()
                    ? annotationName
                    : packageName + "." + annotationName;
        }
    }

    private QinIrExpression lowerMethodReturnExpression(
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> fieldLocals,
            Set<String> valueNames,
            JavaAstMethodDeclaration method) {
        if (hasStructuredStatement(method)) {
            return lowerMethodBodyExpression(packageName, importedTypes, fieldLocals, valueNames, method);
        }
        Map<String, QinIrExpression> locals = new LinkedHashMap<>(fieldLocals);
        for (JavaAstStatement statement : method.bodyStatements()) {
            if (isExplicitSuperConstructorInvocation(statement)) {
                continue;
            }
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                if (localVariable.initializer() != null) {
                    locals.put(localVariable.name(), lowerExpression(
                            localVariable.initializer(),
                            packageName,
                            importedTypes,
                            locals,
                            valueNames));
                }
                valueNames.add(localVariable.name());
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                return lowerReturnExpression(returnStatement, packageName, importedTypes, locals, valueNames);
            }
        }
        return lowerOptionalExpression(method.returnExpression(), packageName, importedTypes, locals, valueNames);
    }

    private boolean hasStructuredStatement(JavaAstMethodDeclaration method) {
        for (JavaAstStatement statement : method.bodyStatements()) {
            if (isExplicitSuperConstructorInvocation(statement)) {
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement
                    || statement instanceof JavaAstDoWhileStatement
                    || statement instanceof JavaAstEnhancedForStatement
                    || statement instanceof JavaAstForStatement
                    || statement instanceof JavaAstIfStatement
                    || statement instanceof JavaAstWhileStatement) {
                return true;
            }
        }
        return false;
    }

    private QinIrExpression lowerMethodBodyExpression(
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> fieldLocals,
            Set<String> valueNames,
            JavaAstMethodDeclaration method) {
        Set<String> scopedValueNames = new LinkedHashSet<>(valueNames);
        Map<String, QinIrExpression> baseLocals = new LinkedHashMap<>(fieldLocals);
        List<QinIrLocalVariableDeclaration> localDeclarations = new ArrayList<>();
        List<QinIrExpression> leadingExpressions = new ArrayList<>();
        QinIrExpression resultExpression = null;
        addPatternVariableDeclarations(method.bodyStatements(), scopedValueNames, localDeclarations);

        for (JavaAstStatement statement : method.bodyStatements()) {
            if (isExplicitSuperConstructorInvocation(statement)) {
                continue;
            }
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrExpression initializer = localVariable.initializer() == null
                        ? new QinIrNullLiteral()
                        : lowerExpression(
                                localVariable.initializer(),
                                packageName,
                                importedTypes,
                                baseLocals,
                                scopedValueNames);
                localDeclarations.add(new QinIrLocalVariableDeclaration(localVariable.name(), initializer));
                scopedValueNames.add(localVariable.name());
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement expressionStatement) {
                leadingExpressions.add(lowerExpression(
                        expressionStatement.expression(),
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstDoWhileStatement doWhileStatement) {
                leadingExpressions.add(lowerDoWhileExpression(doWhileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstForStatement forStatement) {
                leadingExpressions.add(lowerForExpression(forStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstEnhancedForStatement enhancedForStatement) {
                leadingExpressions.add(lowerEnhancedForExpression(
                        enhancedForStatement,
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                JavaAstReturnStatement fallthroughReturn = nextReturnStatement(method.bodyStatements(), statement);
                QinIrExpression ifExpression = lowerIfExpression(
                        ifStatement,
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames);
                if (ifBranchesReturn(ifStatement)) {
                    resultExpression = ifExpression;
                    break;
                }
                if (fallthroughReturn != null
                        && ifStatement.alternateStatements().isEmpty()
                        && statementsEndInReturn(ifStatement.consequentStatements())) {
                    resultExpression = new QinIrIfExpression(
                            lowerExpression(ifStatement.test(), packageName, importedTypes, baseLocals, scopedValueNames),
                            lowerStatementResult(
                                    ifStatement.consequentStatements(),
                                    packageName,
                                    importedTypes,
                                    baseLocals,
                                    scopedValueNames),
                            lowerReturnExpression(fallthroughReturn, packageName, importedTypes, baseLocals, scopedValueNames));
                    break;
                }
                leadingExpressions.add(ifExpression);
                continue;
            }
            if (statement instanceof JavaAstWhileStatement whileStatement) {
                leadingExpressions.add(lowerWhileExpression(whileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                resultExpression = lowerReturnExpression(returnStatement, packageName, importedTypes, baseLocals, scopedValueNames);
            }
        }

        if (resultExpression == null) {
            resultExpression = method.returnExpression() == null
                    ? new QinIrNullLiteral()
                    : lowerExpression(method.returnExpression(), packageName, importedTypes, baseLocals, scopedValueNames);
        }
        return new QinIrLetExpression(localDeclarations, leadingExpressions, resultExpression);
    }

    private boolean isExplicitSuperConstructorInvocation(JavaAstStatement statement) {
        return statement instanceof JavaAstExpressionStatement expressionStatement
                && expressionStatement.expression() instanceof JavaAstMethodCallExpression methodCall
                && "constructor".equals(methodCall.methodName())
                && methodCall.receiver() instanceof JavaAstIdentifierExpression receiver
                && "super".equals(receiver.name());
    }

    private JavaAstReturnStatement nextReturnStatement(List<JavaAstStatement> statements, JavaAstStatement current) {
        int index = statements.indexOf(current);
        if (index < 0 || index + 1 >= statements.size()) {
            return null;
        }
        JavaAstStatement next = statements.get(index + 1);
        return next instanceof JavaAstReturnStatement returnStatement ? returnStatement : null;
    }

    private void addPatternVariableDeclarations(
            List<JavaAstStatement> statements,
            Set<String> valueNames,
            List<QinIrLocalVariableDeclaration> localDeclarations) {
        for (JavaAstStatement statement : statements) {
            if (statement instanceof JavaAstIfStatement ifStatement) {
                addPatternVariableDeclaration(ifStatement.test(), valueNames, localDeclarations);
            }
        }
    }

    private void addPatternVariableDeclaration(
            JavaAstExpression expression,
            Set<String> valueNames,
            List<QinIrLocalVariableDeclaration> localDeclarations) {
        if (expression instanceof JavaAstInstanceofPatternExpression patternExpression) {
            if (valueNames.add(patternExpression.variableName())) {
                localDeclarations.add(new QinIrLocalVariableDeclaration(patternExpression.variableName(), new QinIrNullLiteral()));
            }
            return;
        }
        if (expression instanceof JavaAstUnaryExpression unaryExpression) {
            addPatternVariableDeclaration(unaryExpression.operand(), valueNames, localDeclarations);
        }
    }

    private QinIrExpression lowerIfExpression(
            JavaAstIfStatement ifStatement,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> baseLocals,
            Set<String> valueNames) {
        return new QinIrIfExpression(
                lowerExpression(ifStatement.test(), packageName, importedTypes, baseLocals, valueNames),
                lowerStatementResult(ifStatement.consequentStatements(), packageName, importedTypes, baseLocals, valueNames),
                lowerStatementResult(ifStatement.alternateStatements(), packageName, importedTypes, baseLocals, valueNames));
    }

    private QinIrExpression lowerStatementResult(
            List<JavaAstStatement> statements,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> baseLocals,
            Set<String> valueNames) {
        List<QinIrLocalVariableDeclaration> localDeclarations = new ArrayList<>();
        List<QinIrExpression> leadingExpressions = new ArrayList<>();
        Set<String> scopedValueNames = new LinkedHashSet<>(valueNames);
        QinIrExpression resultExpression = new QinIrNullLiteral();
        for (JavaAstStatement statement : statements) {
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrExpression initializer = localVariable.initializer() == null
                        ? new QinIrNullLiteral()
                        : lowerExpression(localVariable.initializer(), packageName, importedTypes, baseLocals, scopedValueNames);
                localDeclarations.add(new QinIrLocalVariableDeclaration(localVariable.name(), initializer));
                scopedValueNames.add(localVariable.name());
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement expressionStatement) {
                leadingExpressions.add(lowerExpression(
                        expressionStatement.expression(),
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstDoWhileStatement doWhileStatement) {
                leadingExpressions.add(lowerDoWhileExpression(doWhileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstForStatement forStatement) {
                leadingExpressions.add(lowerForExpression(forStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstEnhancedForStatement enhancedForStatement) {
                leadingExpressions.add(lowerEnhancedForExpression(
                        enhancedForStatement,
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                QinIrExpression ifExpression = lowerIfExpression(
                        ifStatement,
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames);
                if (ifBranchesReturn(ifStatement)) {
                    resultExpression = ifExpression;
                    break;
                }
                leadingExpressions.add(ifExpression);
                continue;
            }
            if (statement instanceof JavaAstWhileStatement whileStatement) {
                leadingExpressions.add(lowerWhileExpression(whileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                resultExpression = lowerReturnExpression(returnStatement, packageName, importedTypes, baseLocals, scopedValueNames);
                break;
            }
        }
        if (localDeclarations.isEmpty() && leadingExpressions.isEmpty()) {
            return resultExpression;
        }
        return new QinIrLetExpression(localDeclarations, leadingExpressions, resultExpression);
    }

    private boolean ifBranchesReturn(JavaAstIfStatement ifStatement) {
        return statementsEndInReturn(ifStatement.consequentStatements())
                && statementsEndInReturn(ifStatement.alternateStatements());
    }

    private boolean statementsEndInReturn(List<JavaAstStatement> statements) {
        if (statements.isEmpty()) {
            return false;
        }
        JavaAstStatement last = statements.get(statements.size() - 1);
        if (last instanceof JavaAstReturnStatement) {
            return true;
        }
        if (last instanceof JavaAstIfStatement nestedIf) {
            return ifBranchesReturn(nestedIf);
        }
        return false;
    }

    private QinIrWhileExpression lowerWhileExpression(
            JavaAstWhileStatement whileStatement,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> baseLocals,
            Set<String> valueNames) {
        List<QinIrLocalVariableDeclaration> localDeclarations = new ArrayList<>();
        List<QinIrExpression> bodyExpressions = new ArrayList<>();
        Set<String> scopedValueNames = new LinkedHashSet<>(valueNames);
        for (JavaAstStatement statement : whileStatement.bodyStatements()) {
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrExpression initializer = localVariable.initializer() == null
                        ? new QinIrNullLiteral()
                        : lowerExpression(localVariable.initializer(), packageName, importedTypes, baseLocals, scopedValueNames);
                localDeclarations.add(new QinIrLocalVariableDeclaration(localVariable.name(), initializer));
                scopedValueNames.add(localVariable.name());
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement expressionStatement) {
                bodyExpressions.add(lowerExpression(
                        expressionStatement.expression(),
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstDoWhileStatement doWhileStatement) {
                bodyExpressions.add(lowerDoWhileExpression(doWhileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstForStatement forStatement) {
                bodyExpressions.add(lowerForExpression(forStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstEnhancedForStatement enhancedForStatement) {
                bodyExpressions.add(lowerEnhancedForExpression(
                        enhancedForStatement,
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                bodyExpressions.add(lowerIfExpression(ifStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstWhileStatement nestedWhileStatement) {
                bodyExpressions.add(lowerWhileExpression(nestedWhileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                bodyExpressions.add(lowerReturnExpression(returnStatement, packageName, importedTypes, baseLocals, scopedValueNames));
            }
        }
        return new QinIrWhileExpression(
                lowerExpression(whileStatement.test(), packageName, importedTypes, baseLocals, valueNames),
                localDeclarations,
                bodyExpressions);
    }

    private QinIrDoWhileExpression lowerDoWhileExpression(
            JavaAstDoWhileStatement doWhileStatement,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> baseLocals,
            Set<String> valueNames) {
        List<QinIrLocalVariableDeclaration> localDeclarations = new ArrayList<>();
        List<QinIrExpression> bodyExpressions = new ArrayList<>();
        Set<String> scopedValueNames = new LinkedHashSet<>(valueNames);
        for (JavaAstStatement statement : doWhileStatement.bodyStatements()) {
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrExpression initializer = localVariable.initializer() == null
                        ? new QinIrNullLiteral()
                        : lowerExpression(localVariable.initializer(), packageName, importedTypes, baseLocals, scopedValueNames);
                localDeclarations.add(new QinIrLocalVariableDeclaration(localVariable.name(), initializer));
                scopedValueNames.add(localVariable.name());
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement expressionStatement) {
                bodyExpressions.add(lowerExpression(
                        expressionStatement.expression(),
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstDoWhileStatement nestedDoWhileStatement) {
                bodyExpressions.add(lowerDoWhileExpression(nestedDoWhileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstForStatement forStatement) {
                bodyExpressions.add(lowerForExpression(forStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstEnhancedForStatement enhancedForStatement) {
                bodyExpressions.add(lowerEnhancedForExpression(
                        enhancedForStatement,
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                bodyExpressions.add(lowerIfExpression(ifStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstWhileStatement whileStatement) {
                bodyExpressions.add(lowerWhileExpression(whileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                bodyExpressions.add(lowerReturnExpression(returnStatement, packageName, importedTypes, baseLocals, scopedValueNames));
            }
        }
        return new QinIrDoWhileExpression(
                lowerExpression(doWhileStatement.test(), packageName, importedTypes, baseLocals, valueNames),
                localDeclarations,
                bodyExpressions);
    }

    private QinIrForExpression lowerForExpression(
            JavaAstForStatement forStatement,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> baseLocals,
            Set<String> valueNames) {
        List<QinIrLocalVariableDeclaration> initializerDeclarations = new ArrayList<>();
        List<QinIrExpression> initializerExpressions = new ArrayList<>();
        Set<String> scopedValueNames = new LinkedHashSet<>(valueNames);
        for (JavaAstStatement initializer : forStatement.initializerStatements()) {
            if (initializer instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrExpression initialValue = localVariable.initializer() == null
                        ? new QinIrNullLiteral()
                        : lowerExpression(localVariable.initializer(), packageName, importedTypes, baseLocals, scopedValueNames);
                initializerDeclarations.add(new QinIrLocalVariableDeclaration(localVariable.name(), initialValue));
                scopedValueNames.add(localVariable.name());
                continue;
            }
            if (initializer instanceof JavaAstExpressionStatement expressionStatement) {
                initializerExpressions.add(lowerExpression(
                        expressionStatement.expression(),
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
            }
        }

        List<QinIrExpression> updateExpressions = new ArrayList<>();
        for (JavaAstExpression updateExpression : forStatement.updateExpressions()) {
            updateExpressions.add(lowerExpression(updateExpression, packageName, importedTypes, baseLocals, scopedValueNames));
        }

        List<QinIrLocalVariableDeclaration> bodyLocalDeclarations = new ArrayList<>();
        List<QinIrExpression> bodyExpressions = new ArrayList<>();
        for (JavaAstStatement statement : forStatement.bodyStatements()) {
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrExpression initializer = localVariable.initializer() == null
                        ? new QinIrNullLiteral()
                        : lowerExpression(localVariable.initializer(), packageName, importedTypes, baseLocals, scopedValueNames);
                bodyLocalDeclarations.add(new QinIrLocalVariableDeclaration(localVariable.name(), initializer));
                scopedValueNames.add(localVariable.name());
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement expressionStatement) {
                bodyExpressions.add(lowerExpression(
                        expressionStatement.expression(),
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstDoWhileStatement doWhileStatement) {
                bodyExpressions.add(lowerDoWhileExpression(doWhileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstForStatement nestedForStatement) {
                bodyExpressions.add(lowerForExpression(nestedForStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstEnhancedForStatement enhancedForStatement) {
                bodyExpressions.add(lowerEnhancedForExpression(
                        enhancedForStatement,
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                bodyExpressions.add(lowerIfExpression(ifStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstWhileStatement whileStatement) {
                bodyExpressions.add(lowerWhileExpression(whileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                bodyExpressions.add(lowerReturnExpression(returnStatement, packageName, importedTypes, baseLocals, scopedValueNames));
            }
        }

        return new QinIrForExpression(
                initializerDeclarations,
                initializerExpressions,
                lowerExpression(forStatement.test(), packageName, importedTypes, baseLocals, scopedValueNames),
                updateExpressions,
                bodyLocalDeclarations,
                bodyExpressions);
    }

    private QinIrForEachExpression lowerEnhancedForExpression(
            JavaAstEnhancedForStatement enhancedForStatement,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> baseLocals,
            Set<String> valueNames) {
        QinIrExpression iterable = lowerExpression(
                enhancedForStatement.iterableExpression(),
                packageName,
                importedTypes,
                baseLocals,
                valueNames);
        List<QinIrLocalVariableDeclaration> bodyLocalDeclarations = new ArrayList<>();
        List<QinIrExpression> bodyExpressions = new ArrayList<>();
        Set<String> scopedValueNames = new LinkedHashSet<>(valueNames);
        scopedValueNames.add(enhancedForStatement.variableName());
        for (JavaAstStatement statement : enhancedForStatement.bodyStatements()) {
            if (statement instanceof JavaAstLocalVariableDeclaration localVariable) {
                QinIrExpression initializer = localVariable.initializer() == null
                        ? new QinIrNullLiteral()
                        : lowerExpression(localVariable.initializer(), packageName, importedTypes, baseLocals, scopedValueNames);
                bodyLocalDeclarations.add(new QinIrLocalVariableDeclaration(localVariable.name(), initializer));
                scopedValueNames.add(localVariable.name());
                continue;
            }
            if (statement instanceof JavaAstExpressionStatement expressionStatement) {
                bodyExpressions.add(lowerExpression(
                        expressionStatement.expression(),
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstDoWhileStatement doWhileStatement) {
                bodyExpressions.add(lowerDoWhileExpression(doWhileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstForStatement forStatement) {
                bodyExpressions.add(lowerForExpression(forStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstEnhancedForStatement nestedEnhancedForStatement) {
                bodyExpressions.add(lowerEnhancedForExpression(
                        nestedEnhancedForStatement,
                        packageName,
                        importedTypes,
                        baseLocals,
                        scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstIfStatement ifStatement) {
                bodyExpressions.add(lowerIfExpression(ifStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstWhileStatement whileStatement) {
                bodyExpressions.add(lowerWhileExpression(whileStatement, packageName, importedTypes, baseLocals, scopedValueNames));
                continue;
            }
            if (statement instanceof JavaAstReturnStatement returnStatement) {
                bodyExpressions.add(lowerReturnExpression(returnStatement, packageName, importedTypes, baseLocals, scopedValueNames));
            }
        }
        return new QinIrForEachExpression(
                enhancedForStatement.variableName(),
                iterable,
                bodyLocalDeclarations,
                bodyExpressions);
    }

    private QinIrExpression lowerExpression(JavaAstExpression expression) {
        return lowerExpression(expression, null, Map.of(), Map.of(), Set.of());
    }

    private QinIrExpression lowerReturnExpression(
            JavaAstReturnStatement returnStatement,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> locals,
            Set<String> valueNames) {
        return lowerOptionalExpression(returnStatement.expression(), packageName, importedTypes, locals, valueNames);
    }

    private QinIrExpression lowerOptionalExpression(
            JavaAstExpression expression,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> locals,
            Set<String> valueNames) {
        return expression == null
                ? new QinIrNullLiteral()
                : lowerExpression(expression, packageName, importedTypes, locals, valueNames);
    }

    private QinIrExpression lowerExpression(
            JavaAstExpression expression,
            String packageName,
            Map<String, String> importedTypes,
            Map<String, QinIrExpression> locals,
            Set<String> valueNames) {
        if (expression == null) {
            return null;
        }
        if (expression instanceof JavaAstIdentifierExpression identifier) {
            if (valueNames.contains(identifier.name())) {
                return new QinIrIdentifierReference(identifier.name());
            }
            QinIrExpression local = locals.get(identifier.name());
            if (local != null) {
                return local;
            }
            return new QinIrIdentifierReference(identifier.name());
        }
        if (expression instanceof JavaAstNumberLiteral number) {
            return new QinIrNumberLiteral(number.value());
        }
        if (expression instanceof JavaAstBooleanLiteral booleanLiteral) {
            return new QinIrBooleanLiteral(booleanLiteral.value());
        }
        if (expression instanceof JavaAstNullLiteral) {
            return new QinIrNullLiteral();
        }
        if (expression instanceof JavaAstStringLiteral string) {
            return new QinIrStringLiteral(string.value());
        }
        if (expression instanceof JavaAstThisExpression) {
            return new QinIrThisExpression();
        }
        if (expression instanceof JavaAstLambdaExpression lambdaExpression) {
            Set<String> lambdaValueNames = new LinkedHashSet<>(valueNames);
            lambdaValueNames.addAll(lambdaExpression.parameterNames());
            QinIrExpression returnExpression;
            if (lambdaExpression.bodyExpression() != null) {
                returnExpression = lowerExpression(
                        lambdaExpression.bodyExpression(),
                        packageName,
                        importedTypes,
                        locals,
                        lambdaValueNames);
            } else {
                returnExpression = lowerStatementResult(
                        lambdaExpression.bodyStatements(),
                        packageName,
                        importedTypes,
                        locals,
                        lambdaValueNames);
            }
            return new QinIrFunctionLiteral(lambdaExpression.parameterNames(), returnExpression);
        }
        if (expression instanceof JavaAstClassLiteralExpression classLiteral) {
            return new QinIrJavaClassLiteralExpression(
                    classLiteral.typeName(),
                    semanticAnalyzer.resolveType(classLiteral.typeName(), packageName, importedTypes).binaryName());
        }
        if (expression instanceof JavaAstArrayLiteralExpression arrayLiteral) {
            List<QinIrExpression> elements = new ArrayList<>();
            for (JavaAstExpression element : arrayLiteral.elements()) {
                elements.add(lowerExpression(element, packageName, importedTypes, locals, valueNames));
            }
            return new QinIrArrayLiteral(elements);
        }
        if (expression instanceof JavaAstMemberAccessExpression memberAccess) {
            QinIrTypeRef ownerType = staticOwnerType(memberAccess.receiver(), packageName, importedTypes, valueNames);
            if (ownerType != null) {
                return new QinIrMemberAccessExpression(
                        semanticAnalyzer.qualifiedName(memberAccess.receiver()),
                        memberAccess.propertyName());
            }
            return new QinIrPropertyAccessExpression(
                    lowerExpression(memberAccess.receiver(), packageName, importedTypes, locals, valueNames),
                    memberAccess.propertyName());
        }
        if (expression instanceof JavaAstArrayAccessExpression arrayAccess) {
            return new QinIrElementAccessExpression(
                    lowerExpression(arrayAccess.receiver(), packageName, importedTypes, locals, valueNames),
                    lowerExpression(arrayAccess.index(), packageName, importedTypes, locals, valueNames));
        }
        if (expression instanceof JavaAstMethodCallExpression methodCall) {
            List<QinIrExpression> arguments = new ArrayList<>();
            for (JavaAstExpression argument : methodCall.arguments()) {
                arguments.add(lowerExpression(argument, packageName, importedTypes, locals, valueNames));
            }
            if (!(methodCall.receiver() instanceof JavaAstIdentifierExpression receiverIdentifier
                    && "super".equals(receiverIdentifier.name()))) {
                QinIrTypeRef ownerType = staticOwnerType(methodCall.receiver(), packageName, importedTypes, valueNames);
                if (ownerType != null) {
                    return new QinIrStaticMethodCallExpression(
                            semanticAnalyzer.qualifiedName(methodCall.receiver()),
                            ownerType.binaryName(),
                            methodCall.methodName(),
                            arguments);
                }
            }
            if (methodCall.receiver() instanceof JavaAstIdentifierExpression receiverIdentifier
                    && !"super".equals(receiverIdentifier.name())
                    && !valueNames.contains(receiverIdentifier.name())) {
                String ownerBinaryName = semanticAnalyzer
                        .resolveType(receiverIdentifier.name(), packageName, importedTypes)
                        .binaryName();
                if (semanticAnalyzer.isLoadableClass(ownerBinaryName)) {
                    return new QinIrStaticMethodCallExpression(
                            receiverIdentifier.name(),
                            ownerBinaryName,
                            methodCall.methodName(),
                            arguments);
                }
            }
            return new QinIrInstanceMethodCallExpression(
                    lowerExpression(methodCall.receiver(), packageName, importedTypes, locals, valueNames),
                    methodCall.methodName(),
                    arguments);
        }
        if (expression instanceof JavaAstMethodReferenceExpression methodReference) {
            String ownerBinaryName = semanticAnalyzer
                    .resolveType(methodReference.ownerName(), packageName, importedTypes)
                    .binaryName();
            return new QinIrJavaMethodReferenceExpression(
                    semanticAnalyzer.rawTypeName(methodReference.ownerName()),
                    ownerBinaryName,
                    methodReference.methodName());
        }
        if (expression instanceof JavaAstNewExpression newExpression) {
            List<QinIrExpression> arguments = new ArrayList<>();
            for (JavaAstExpression argument : newExpression.arguments()) {
                arguments.add(lowerExpression(argument, packageName, importedTypes, locals, valueNames));
            }
            String ownerBinaryName = semanticAnalyzer
                    .resolveType(newExpression.typeName(), packageName, importedTypes)
                    .binaryName();
            return new QinIrJavaNewExpression(semanticAnalyzer.rawTypeName(newExpression.typeName()), ownerBinaryName, arguments);
        }
        if (expression instanceof JavaAstAssignmentExpression assignment) {
            return new QinIrAssignmentExpression(
                    lowerExpression(assignment.target(), packageName, importedTypes, locals, valueNames),
                    assignment.operator(),
                    lowerExpression(assignment.value(), packageName, importedTypes, locals, valueNames));
        }
        if (expression instanceof JavaAstUpdateExpression updateExpression) {
            QinIrExpression target = lowerExpression(updateExpression.target(), packageName, importedTypes, locals, valueNames);
            String binaryOperator = "++".equals(updateExpression.operator()) ? "+" : "-";
            return new QinIrAssignmentExpression(
                    target,
                    "=",
                    new QinIrBuiltinCallExpression(
                            "Global",
                            "__qin_binary__",
                            List.of(
                                    new QinIrStringLiteral(binaryOperator),
                                    target,
                                    new QinIrNumberLiteral(1.0))));
        }
        if (expression instanceof JavaAstUnaryExpression unaryExpression) {
            QinIrExpression operand = lowerExpression(
                    unaryExpression.operand(),
                    packageName,
                    importedTypes,
                    locals,
                    valueNames);
            return switch (unaryExpression.operator()) {
                case "!" -> new QinIrIfExpression(operand, new QinIrBooleanLiteral(false), new QinIrBooleanLiteral(true));
                case "+" -> operand;
                case "-" -> new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_binary__",
                        List.of(new QinIrStringLiteral("-"), new QinIrNumberLiteral(0.0), operand));
                default -> throw new IllegalArgumentException("Unsupported Java unary operator: "
                        + unaryExpression.operator());
            };
        }
        if (expression instanceof JavaAstCastExpression castExpression) {
            return new QinIrCastExpression(
                    castExpression.typeName(),
                    lowerExpression(castExpression.expression(), packageName, importedTypes, locals, valueNames));
        }
        if (expression instanceof JavaAstInstanceofPatternExpression instanceofPattern) {
            QinIrTypeRef ownerType = semanticAnalyzer.resolveType(
                    instanceofPattern.typeName(),
                    packageName,
                    importedTypes);
            return new QinIrJavaInstanceofPatternExpression(
                    lowerExpression(instanceofPattern.value(), packageName, importedTypes, locals, valueNames),
                    semanticAnalyzer.rawTypeName(instanceofPattern.typeName()),
                    ownerType.binaryName(),
                    instanceofPattern.variableName());
        }
        if (expression instanceof JavaAstBinaryExpression binary) {
            if ("&&".equals(binary.operator())) {
                return new QinIrIfExpression(
                        lowerExpression(binary.left(), packageName, importedTypes, locals, valueNames),
                        lowerExpression(binary.right(), packageName, importedTypes, locals, valueNames),
                        new QinIrBooleanLiteral(false));
            }
            if ("||".equals(binary.operator())) {
                return new QinIrIfExpression(
                        lowerExpression(binary.left(), packageName, importedTypes, locals, valueNames),
                        new QinIrBooleanLiteral(true),
                        lowerExpression(binary.right(), packageName, importedTypes, locals, valueNames));
            }
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_binary__",
                    List.of(
                            new QinIrStringLiteral(binary.operator()),
                            lowerExpression(binary.left(), packageName, importedTypes, locals, valueNames),
                            lowerExpression(binary.right(), packageName, importedTypes, locals, valueNames)));
        }
        throw new IllegalArgumentException("Unsupported Java AST expression: " + expression);
    }

    private QinIrTypeRef staticOwnerType(
            JavaAstExpression expression,
            String packageName,
            Map<String, String> importedTypes,
            Set<String> valueNames) {
        String qualifiedName = semanticAnalyzer.qualifiedName(expression);
        if (qualifiedName == null || qualifiedName.isBlank()) {
            return null;
        }
        int dot = qualifiedName.indexOf('.');
        String rootName = dot < 0 ? qualifiedName : qualifiedName.substring(0, dot);
        if (valueNames.contains(rootName) || "this".equals(rootName) || "super".equals(rootName)) {
            return null;
        }
        QinIrTypeRef ownerType = semanticAnalyzer.resolveType(qualifiedName, packageName, importedTypes);
        return semanticAnalyzer.isLoadableClass(ownerType.binaryName()) ? ownerType : null;
    }

    private Map<String, QinIrExpression> fieldLocals(Set<String> fieldNames) {
        Map<String, QinIrExpression> locals = new LinkedHashMap<>();
        for (String fieldName : fieldNames) {
            locals.put(fieldName, new QinIrPropertyAccessExpression(new QinIrThisExpression(), fieldName));
        }
        return locals;
    }

    private Map<String, QinJavaSemanticClass> semanticClassesByBinaryName(QinJavaSemanticModel model) {
        Map<String, QinJavaSemanticClass> classes = new LinkedHashMap<>();
        for (QinJavaSemanticClass semanticClass : model.classes()) {
            classes.put(semanticClass.binaryName(), semanticClass);
        }
        return classes;
    }

    private String binaryName(String packageName, String simpleName) {
        return packageName == null || packageName.isBlank()
                ? simpleName
                : packageName + "." + simpleName;
    }

    private Map<String, QinJavaSemanticField> semanticFieldsByName(QinJavaSemanticClass semanticClass) {
        Map<String, QinJavaSemanticField> fields = new LinkedHashMap<>();
        for (QinJavaSemanticField field : semanticClass.fields()) {
            fields.put(field.name(), field);
        }
        return fields;
    }

    private Map<String, QinJavaSemanticMethod> semanticMethodsBySignature(QinJavaSemanticClass semanticClass) {
        Map<String, QinJavaSemanticMethod> methods = new LinkedHashMap<>();
        for (QinJavaSemanticMethod method : semanticClass.methods()) {
            methods.put(methodKey(method), method);
        }
        return methods;
    }

    private String methodKey(
            String packageName,
            Map<String, String> importedTypes,
            JavaAstMethodDeclaration method) {
        List<String> parameterTypes = new ArrayList<>();
        for (JavaAstParameter parameter : method.parameters()) {
            parameterTypes.add(typeKey(semanticAnalyzer.resolveType(parameter.typeName(), packageName, importedTypes)));
        }
        return method.name() + "(" + String.join(",", parameterTypes) + ")";
    }

    private String methodKey(QinJavaSemanticMethod method) {
        List<String> parameterTypes = new ArrayList<>();
        for (QinJavaSemanticParameter parameter : method.parameters()) {
            parameterTypes.add(typeKey(parameter.type()));
        }
        return method.name() + "(" + String.join(",", parameterTypes) + ")";
    }

    private String typeKey(QinIrTypeRef type) {
        String base = type.kind().name() + ":" + (type.binaryName() == null ? "" : type.binaryName());
        if (type.typeArguments().isEmpty()) {
            return base;
        }
        List<String> arguments = new ArrayList<>();
        for (QinIrTypeRef argument : type.typeArguments()) {
            arguments.add(typeKey(argument));
        }
        return base + "<" + String.join(",", arguments) + ">";
    }

    private String methodSignatureForMessage(
            String packageName,
            Map<String, String> importedTypes,
            JavaAstMethodDeclaration method) {
        List<String> parameters = new ArrayList<>();
        for (JavaAstParameter parameter : method.parameters()) {
            parameters.add(parameter.typeName() + " " + parameter.name());
        }
        return (packageName == null || packageName.isBlank() ? "" : packageName + ".")
                + method.name()
                + "(" + String.join(", ", parameters) + ")";
    }

    private Map<String, QinJavaSemanticParameter> semanticParametersByName(QinJavaSemanticMethod semanticMethod) {
        Map<String, QinJavaSemanticParameter> parameters = new LinkedHashMap<>();
        for (QinJavaSemanticParameter parameter : semanticMethod.parameters()) {
            parameters.put(parameter.name(), parameter);
        }
        return parameters;
    }
}
