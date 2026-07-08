package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrLocalVariableDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.parser.QinParsedSource;
import com.qin.parser.QinParserFacade;
import com.qin.parser.QinParserRuntimeNames;
import com.slime.ast.AstNode;
import com.slime.ast.Expression;
import com.slime.ast.Statement;
import com.slime.ast.nodes.declarations.ClassDeclaration;
import com.slime.ast.nodes.declarations.FunctionDeclaration;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.expressions.ArrayExpression;
import com.slime.ast.nodes.expressions.ArrowFunctionExpression;
import com.slime.ast.nodes.expressions.AssignmentExpression;
import com.slime.ast.nodes.expressions.BinaryExpression;
import com.slime.ast.nodes.expressions.CallExpression;
import com.slime.ast.nodes.expressions.ClassExpression;
import com.slime.ast.nodes.expressions.ConditionalExpression;
import com.slime.ast.nodes.expressions.FunctionExpression;
import com.slime.ast.nodes.expressions.Identifier;
import com.slime.ast.nodes.expressions.ImportExpression;
import com.slime.ast.nodes.expressions.Literal;
import com.slime.ast.nodes.expressions.LogicalExpression;
import com.slime.ast.nodes.expressions.MemberExpression;
import com.slime.ast.nodes.expressions.NewExpression;
import com.slime.ast.nodes.expressions.ObjectExpression;
import com.slime.ast.nodes.expressions.ParenthesizedExpression;
import com.slime.ast.nodes.expressions.TaggedTemplateExpression;
import com.slime.ast.nodes.expressions.ThisExpression;
import com.slime.ast.nodes.expressions.TemplateLiteral;
import com.slime.ast.nodes.expressions.UnaryExpression;
import com.slime.ast.nodes.expressions.AwaitExpression;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.misc.Property;
import com.slime.ast.nodes.misc.Decorator;
import com.slime.ast.nodes.misc.FunctionParameter;
import com.slime.ast.nodes.misc.MethodDefinition;
import com.slime.ast.nodes.misc.PropertyDefinition;
import com.slime.ast.nodes.misc.SpreadElement;
import com.slime.ast.nodes.misc.TemplateElement;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.statements.BlockStatement;
import com.slime.ast.nodes.statements.ExpressionStatement;
import com.slime.ast.nodes.statements.IfStatement;
import com.slime.ast.nodes.statements.ReturnStatement;
import com.slime.ast.nodes.typescript.TSKeywordType;
import com.slime.ast.nodes.typescript.TSTypeAnnotation;
import com.slime.ast.nodes.typescript.TSTypeReference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Compatibility façade for legacy Slime-named frontend entrypoints.
 *
 * <p>New code should prefer {@link QinFrontendLowerer} and {@link QinIrLowerer}.
 * This class remains to preserve older `parseProgram` / `parseAst` call sites
 * while the underlying lowering implementation is migrated behind Qin-owned
 * names.
 */
public final class QinSlimeFrontendAdapter extends QinSlimeIrLoweringSupport {
    private final QinParserFacade qinParserFacade = new QinParserFacade();

    public QinIrProgram parseProgram(String source) {
        Objects.requireNonNull(source, "source cannot be null");
        String sourceForSlime = source.trim();
        if (sourceForSlime.isEmpty()) {
            return createEmptyProgram();
        }
        QinParsedSource parsed = qinParserFacade.parseSource(sourceForSlime);
        if (!parsed.hasProgram()) {
            return createImportOnlyProgram(parsed);
        }
        return Holder.IR_LOWERER.lowerParsedSource(parsed);
    }

    public String parseAst(String source) {
        Objects.requireNonNull(source, "source cannot be null");
        String sourceForSlime = source.trim();
        if (sourceForSlime.isEmpty()) {
            return "Program(empty)";
        }
        QinParsedSource parsed = qinParserFacade.parseSource(sourceForSlime);
        if (!parsed.hasProgram()) {
            return "Program(import-only)";
        }
        return Holder.IR_LOWERER.renderParsedAst(parsed);
    }

    public QinIrProgram parseConstObjectDeclaration(String source) {
        return parseProgram(source);
    }

    String renderParsedAst(QinParsedSource parsed) {
        if (parsed == null || !parsed.hasProgram()) {
            return "Program(import-only)";
        }
        return renderProgramAst(parsed.requireProgram(), parsed.effectiveSource());
    }

    static String renderProgramAst(Object programAst, String sourceText) {
        if (programAst == null) {
            return "Program(import-only)";
        }
        return AstJsonEncoder.toJson(programAst, sourceText);
    }

    static Object createSyntheticTopLevelFunctionAstStatic(Object statementAst) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "BlockStatement");
        body.put("body", List.of(statementAst));

        Map<String, Object> functionAst = new LinkedHashMap<>();
        functionAst.put("type", "FunctionExpression");
        functionAst.put("id", null);
        functionAst.put("params", List.of());
        functionAst.put("body", body);
        functionAst.put("generator", false);
        functionAst.put("async", false);
        return functionAst;
    }

    static QinIrProgram createEmptyProgram() {
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
                List.of());
    }

    static QinIrProgram createImportOnlyProgram(QinParsedSource parsed) {
        Objects.requireNonNull(parsed, "parsed cannot be null");
        return new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                parsed.javaImports(),
                parsed.jsImports(),
                List.of(),
                List.of(),
                List.of(),
                List.of());
    }

    private static final class Holder {
        private static final QinIrLowerer IR_LOWERER = new QinIrLowerer();
    }

    QinIrClassDeclaration lowerClassDeclarationOrNull(
            ClassDeclaration classDeclaration,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames) {
        if (classDeclaration == null || classDeclaration.id() == null) {
            return null;
        }

        List<QinIrFieldDeclaration> fields = new ArrayList<>();
        if (classDeclaration.body() != null && classDeclaration.body().body() != null) {
            for (AstNode member : classDeclaration.body().body()) {
                if (member instanceof PropertyDefinition propertyDefinition) {
                    QinIrFieldDeclaration loweredField = lowerFieldDeclarationOrNull(
                            propertyDefinition,
                            javaImportLookup,
                            localDeclarationNames);
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
                QinIrTypeRef.classType("java.lang.Object"),
                lowerAnnotations(classDeclaration.decorators(), javaImportLookup),
                fields,
                methods);
    }

    private QinIrFieldDeclaration lowerFieldDeclarationOrNull(
            PropertyDefinition propertyDefinition,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames) {
        if (propertyDefinition == null || !(propertyDefinition.key() instanceof Identifier identifier)) {
            return null;
        }
        return new QinIrFieldDeclaration(
                identifier.name(),
                lowerParameterType(propertyDefinition.typeAnnotation(), javaImportLookup, localDeclarationNames),
                lowerAnnotations(propertyDefinition.decorators(), javaImportLookup),
                lowerFieldInitializer(propertyDefinition.value(), javaImportLookup));
    }

    private QinIrExpression lowerFieldInitializer(
            AstNode valueAst,
            Map<String, String> javaImportLookup) {
        if (valueAst == null) {
            return null;
        }

        QinIrExpression initializer = lowerExpression(valueAst, javaImportLookup);
        if (initializer instanceof QinIrStringLiteral
                || initializer instanceof QinIrBooleanLiteral
                || initializer instanceof QinIrNumberLiteral
                || initializer instanceof QinIrNullLiteral) {
            return initializer;
        }
        throw qjsError("QJS2013", "Only literal field initializers are supported in declaration subset");
    }

    private QinIrMethodDeclaration lowerMethodDeclarationOrNull(
            MethodDefinition methodDefinition,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames,
            DeclarationClassContext classContext) {
        if (!(methodDefinition.key() instanceof Identifier identifier)) {
            return null;
        }

        FunctionExpression function = methodDefinition.value();
        List<QinIrParameter> parameters = lowerParameters(function, javaImportLookup, localDeclarationNames);
        QinIrExpression returnExpression = lowerMethodReturnExpression(function, javaImportLookup, classContext);
        QinIrTypeRef returnType = inferDeclarationReturnType(returnExpression, parameters, classContext);

        return new QinIrMethodDeclaration(
                identifier.name(),
                returnType,
                parameters,
                lowerAnnotations(methodDefinition.decorators(), javaImportLookup),
                returnExpression);
    }

    private List<QinIrParameter> lowerParameters(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            java.util.Set<String> localDeclarationNames) {
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
            java.util.Set<String> localDeclarationNames) {
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
        if (typeAst instanceof TSTypeReference typeReference) {
            String typeName = typeReferenceName(typeReference.typeName());
            if (typeName == null || typeName.isBlank()) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            String importedBinaryName = javaImportLookup.get(typeName);
            if (importedBinaryName != null && !importedBinaryName.isBlank()) {
                return QinIrTypeRef.classType(importedBinaryName);
            }
            if (localDeclarationNames.contains(typeName)) {
                return QinIrTypeRef.classType(typeName);
            }
            return switch (typeName) {
                case "String" -> QinIrTypeRef.stringType();
                case "Boolean" -> QinIrTypeRef.booleanType();
                case "Double", "Number" -> QinIrTypeRef.doubleType();
                default -> QinIrTypeRef.classType(typeName);
            };
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private String typeReferenceName(Object typeNameAst) {
        String nodeType = simpleName(typeNameAst);
        if (typeNameAst instanceof Identifier identifier) {
            return identifier.name();
        }
        if ("TSQualifiedName".equals(nodeType)) {
            String left = typeReferenceName(invokeByName(typeNameAst, "left"));
            String right = typeReferenceName(invokeByName(typeNameAst, "right"));
            if (left == null || left.isBlank() || right == null || right.isBlank()) {
                return null;
            }
            return left + "." + right;
        }
        return null;
    }

    java.util.Set<String> collectTopLevelClassNames(List<? extends AstNode> body) {
        if (body == null || body.isEmpty()) {
            return java.util.Set.of();
        }
        java.util.Set<String> names = new java.util.LinkedHashSet<>();
        for (AstNode statement : body) {
            if (!(statement instanceof ClassDeclaration classDeclaration)
                    || classDeclaration.id() == null
                    || classDeclaration.id().name() == null
                    || classDeclaration.id().name().isBlank()) {
                continue;
            }
            names.add(classDeclaration.id().name());
        }
        return java.util.Set.copyOf(names);
    }

    private QinIrExpression lowerMethodReturnExpression(
            FunctionExpression function,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext) {
        if (function == null || function.body() == null || function.body().body() == null) {
            return new QinIrNullLiteral();
        }
        List<Statement> statements = function.body().body();
        return lowerDeclarationMethodBody(statements, javaImportLookup, classContext, Map.of());
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
            String name = extractIdentifierName(declarator.id(), "VariableDeclarator.id");
            if (name == null || name.isBlank()) {
                return false;
            }
            QinIrExpression initializer = lowerDeclarationExpression(
                    declarator.init(),
                    javaImportLookup,
                    classContext,
                    locals);
            locals.put(name, initializer);
        }
        return true;
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
            case "Date" -> "now".equals(methodName) ? QinIrTypeRef.doubleType() : null;
            default -> null;
        };
    }

    private boolean isDeclarationNumericLike(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.INT || type.kind() == QinIrTypeKind.DOUBLE;
    }

    private boolean isDeclarationStringLike(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.STRING;
    }

    private QinIrTypeRef inferDeclarationLogicalResultType(
            String operator,
            QinIrTypeRef leftType,
            QinIrTypeRef rightType) {
        return switch (operator) {
            case "&&", "||" -> leftType.kind() == QinIrTypeKind.BOOLEAN && rightType.kind() == QinIrTypeKind.BOOLEAN
                    ? QinIrTypeRef.booleanType()
                    : mergeDeclarationBranchTypes(leftType, rightType);
            case "??" -> mergeDeclarationBranchTypes(leftType, rightType);
            default -> QinIrTypeRef.classType("java.lang.Object");
        };
    }

    private QinIrTypeRef mergeDeclarationBranchTypes(QinIrTypeRef leftType, QinIrTypeRef rightType) {
        if (leftType.equals(rightType)) {
            return leftType;
        }
        if (isDeclarationNumericLike(leftType) && isDeclarationNumericLike(rightType)) {
            return QinIrTypeRef.doubleType();
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef resolveDeclarationPropertyType(
            QinIrTypeRef ownerType,
            String propertyName,
            DeclarationClassContext classContext) {
        if (ownerType.kind() == QinIrTypeKind.STRING && "length".equals(propertyName)) {
            return QinIrTypeRef.intType();
        }
        if (ownerType.kind() != QinIrTypeKind.CLASS && ownerType.kind() != QinIrTypeKind.STRING) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (ownerType.binaryName().equals(classContext.className())) {
            QinIrFieldDeclaration field = classContext.fields().get(propertyName);
            return field != null ? field.type() : QinIrTypeRef.classType("java.lang.Object");
        }
        try {
            Class<?> ownerClass = Class.forName(ownerType.binaryName());
            String capitalized = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
            try {
                return toQinTypeRef(ownerClass.getMethod("get" + capitalized).getReturnType());
            } catch (NoSuchMethodException ignored) {
                try {
                    return toQinTypeRef(ownerClass.getMethod("is" + capitalized).getReturnType());
                } catch (NoSuchMethodException ignoredAgain) {
                    try {
                        return toQinTypeRef(ownerClass.getField(propertyName).getType());
                    } catch (NoSuchFieldException ignoredField) {
                        return QinIrTypeRef.classType("java.lang.Object");
                    }
                }
            }
        } catch (Throwable ignored) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
    }

    private QinIrTypeRef resolveDeclarationMethodReturnType(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount,
            DeclarationClassContext classContext) {
        if (ownerType.kind() != QinIrTypeKind.CLASS && ownerType.kind() != QinIrTypeKind.STRING) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (ownerType.binaryName().equals(classContext.className())) {
            QinIrMethodDeclaration method = classContext.findMethod(methodName, argumentCount);
            return method != null ? method.returnType() : QinIrTypeRef.classType("java.lang.Object");
        }
        try {
            Class<?> ownerClass = Class.forName(ownerType.binaryName());
            java.lang.reflect.Method matched = null;
            for (java.lang.reflect.Method candidate : ownerClass.getMethods()) {
                if (!candidate.getName().equals(methodName) || candidate.getParameterCount() != argumentCount) {
                    continue;
                }
                if (matched != null) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                matched = candidate;
            }
            return matched != null ? toQinTypeRef(matched.getReturnType()) : QinIrTypeRef.classType("java.lang.Object");
        } catch (Throwable ignored) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
    }

    private QinIrParameter resolveDeclarationParameter(List<QinIrParameter> parameters, String parameterName) {
        if (parameters == null || parameters.isEmpty()) {
            return null;
        }
        for (QinIrParameter parameter : parameters) {
            if (parameter.name().equals(parameterName)) {
                return parameter;
            }
        }
        return null;
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
            return lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof Identifier identifier) {
            QinIrExpression local = locals.get(identifier.name());
            return local != null ? local : lowerIdentifierExpression(identifier);
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

        String nodeType = simpleName(expressionAst);
        if ("ParenthesizedExpression".equals(nodeType)) {
            return lowerDeclarationExpression(invokeByName(expressionAst, "expression"), javaImportLookup, classContext, locals);
        }
        if ("Literal".equals(nodeType) || "Identifier".equals(nodeType)) {
            if ("Identifier".equals(nodeType)) {
                String identifierName = extractIdentifierName(expressionAst, "Identifier");
                QinIrExpression local = locals.get(identifierName);
                return local != null ? local : lowerExpression(expressionAst, javaImportLookup);
            }
            return lowerExpression(expressionAst, javaImportLookup);
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
        throw qjsError("QJS2014", "Unsupported declaration return expression: " + nodeType);
    }

    private QinIrObjectLiteral lowerDeclarationObjectLiteral(
            Object objectExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        List<?> properties = asList(invokeByName(objectExpressionAst, "properties"), "ObjectExpression.properties");
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (Object property : properties) {
            if (!"Property".equals(simpleName(property))) {
                throw qjsError("QJS2019", "Only normal object property is supported in declaration subset");
            }
            String key = extractPropertyKey(invokeByName(property, "key"));
            QinIrExpression value = lowerDeclarationExpression(
                    invokeByName(property, "value"),
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
            String key = extractPropertyKey(property.key());
            QinIrExpression value = lowerDeclarationExpression(propertyValue(property), javaImportLookup, classContext, locals);
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
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        String operator = asString(invokeByName(expressionAst, "operator"), "BinaryExpression.operator");
        Object leftAst = invokeByName(expressionAst, "left");
        Object rightAst = invokeByName(expressionAst, "right");
        QinIrExpression left = lowerDeclarationExpression(leftAst, javaImportLookup, classContext, locals);
        QinIrExpression right = lowerDeclarationExpression(rightAst, javaImportLookup, classContext, locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(operator), left, right));
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

    private QinIrBuiltinCallExpression lowerDeclarationLogicalExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        String operator = asString(invokeByName(expressionAst, "operator"), "LogicalExpression.operator");
        Object leftAst = invokeByName(expressionAst, "left");
        Object rightAst = invokeByName(expressionAst, "right");
        QinIrExpression left = lowerDeclarationExpression(leftAst, javaImportLookup, classContext, locals);
        QinIrExpression right = lowerDeclarationExpression(rightAst, javaImportLookup, classContext, locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_logical__",
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

    private QinIrBuiltinCallExpression lowerDeclarationConditionalExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object testAst = invokeByName(expressionAst, "test");
        Object consequentAst = invokeByName(expressionAst, "consequent");
        Object alternateAst = invokeByName(expressionAst, "alternate");
        QinIrExpression test = lowerDeclarationExpression(testAst, javaImportLookup, classContext, locals);
        QinIrExpression consequent = lowerDeclarationExpression(consequentAst, javaImportLookup, classContext, locals);
        QinIrExpression alternate = lowerDeclarationExpression(alternateAst, javaImportLookup, classContext, locals);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
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

    private QinIrExpression lowerDeclarationMemberAccessExpression(
            Object memberExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        boolean computed = Boolean.TRUE.equals(invokeByName(memberExpressionAst, "computed"));
        if (computed) {
            throw qjsError("QJS2015", "Computed member access is not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                invokeByName(memberExpressionAst, "object"),
                javaImportLookup,
                classContext,
                locals);
        String propertyName = extractMemberPropertyName(invokeByName(memberExpressionAst, "property"));
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            return new QinIrMemberAccessExpression(identifierReference.name(), propertyName);
        }
        return new QinIrPropertyAccessExpression(receiver, propertyName);
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
        String propertyName = extractMemberPropertyName(memberExpressionAst.property());
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            return new QinIrMemberAccessExpression(identifierReference.name(), propertyName);
        }
        return new QinIrPropertyAccessExpression(receiver, propertyName);
    }

    private QinIrExpression lowerDeclarationCallExpression(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            DeclarationClassContext classContext,
            Map<String, QinIrExpression> locals) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw qjsError("QJS2016", "Only member call expressions are supported in declaration subset");
        }
        boolean computed = Boolean.TRUE.equals(invokeByName(callee, "computed"));
        if (computed) {
            throw qjsError("QJS2017", "Computed method calls are not supported in declaration subset");
        }
        QinIrExpression receiver = lowerDeclarationReceiver(
                invokeByName(callee, "object"),
                javaImportLookup,
                classContext,
                locals);
        String methodName = extractMemberPropertyName(invokeByName(callee, "property"));
        List<QinIrExpression> arguments = lowerDeclarationCallArguments(
                asList(invokeByName(callExpressionAst, "arguments"), "CallExpression.arguments"),
                javaImportLookup,
                classContext,
                locals);
        if (receiver instanceof QinIrIdentifierReference identifierReference
                && QinBuiltinRegistry.resolve(identifierReference.name(), methodName, arguments.size()).isPresent()) {
            return new QinIrBuiltinCallExpression(identifierReference.name(), methodName, arguments);
        }
        return new QinIrInstanceMethodCallExpression(receiver, methodName, arguments);
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
        String methodName = extractMemberPropertyName(memberExpression.property());
        List<QinIrExpression> arguments = lowerDeclarationCallArguments(
                List.copyOf(callExpression.arguments()),
                javaImportLookup,
                classContext,
                locals);
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

    private List<QinIrAnnotation> lowerAnnotations(
            List<Decorator> decorators,
            Map<String, String> javaImportLookup) {
        if (decorators == null || decorators.isEmpty()) {
            return List.of();
        }

        List<QinIrAnnotation> annotations = new ArrayList<>();
        for (Decorator decorator : decorators) {
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
                return new QinIrStringLiteral(normalizeStringLiteral(text));
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

    boolean isTopLevelControlStatement(String nodeType) {
        return "ForStatement".equals(nodeType)
                || "ForOfStatement".equals(nodeType)
                || "WhileStatement".equals(nodeType)
                || "DoWhileStatement".equals(nodeType)
                || "SwitchStatement".equals(nodeType)
                || "TryStatement".equals(nodeType)
                || "BlockStatement".equals(nodeType);
    }

    void predeclareTopLevelBindings(List<? extends AstNode> body, Map<String, QinIrExpression> declarationLookup) {
        for (AstNode statement : body) {
            String nodeType = statement.getClass().getSimpleName();
            if (statement instanceof VariableDeclaration) {
                List<?> declarators = asList(invokeByName(statement, "declarations"), "VariableDeclaration.declarations");
                for (Object declarator : declarators) {
                    String name = extractIdentifierName(invokeByName(declarator, "id"), "VariableDeclarator.id");
                    if (!name.isBlank()) {
                        declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                    }
                }
                continue;
            }
            if (statement instanceof FunctionDeclaration || statement instanceof ClassDeclaration) {
                String name = extractIdentifierName(invokeByName(statement, "id"), nodeType + ".id");
                if (!name.isBlank()) {
                    declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                }
                continue;
            }
            if (statement instanceof ExportNamedDeclaration) {
                Object declaration = invokeByName(statement, "declaration");
                if (declaration == null) {
                    continue;
                }
                String declarationType = simpleName(declaration);
                if ("VariableDeclaration".equals(declarationType)) {
                    List<?> declarators = asList(invokeByName(declaration, "declarations"), "VariableDeclaration.declarations");
                    for (Object declarator : declarators) {
                        String name = extractIdentifierName(invokeByName(declarator, "id"), "VariableDeclarator.id");
                        if (!name.isBlank()) {
                            declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                        }
                    }
                    continue;
                }
                if ("FunctionDeclaration".equals(declarationType) || "ClassDeclaration".equals(declarationType)) {
                    String name = extractIdentifierName(invokeByName(declaration, "id"), declarationType + ".id");
                    if (!name.isBlank()) {
                        declarationLookup.putIfAbsent(name, new QinIrIdentifierReference(name));
                    }
                }
            }
        }
    }

    static int computeFunctionModelBudget(int sourceLength) {
        return QinFunctionModelBudget.compute(sourceLength);
    }

    List<QinIrConstDeclaration> lowerVariableDeclaration(
            Object variableDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String kind = asString(invokeByName(variableDeclarationAst, "kind"), "VariableDeclaration.kind");
        if (!"const".equals(kind) && !"let".equals(kind) && !"var".equals(kind)) {
            throw qjsError("QJS2002", "Only const/let/var declaration is supported, but got: " + kind);
        }

        List<?> declarators = asList(invokeByName(variableDeclarationAst, "declarations"),
                "VariableDeclaration.declarations");
        if (declarators.isEmpty()) {
            throw qjsError("QJS2002", "Variable declaration must contain at least one declarator");
        }

        List<QinIrConstDeclaration> lowered = new ArrayList<>();
        for (Object declarator : declarators) {
            Object id = invokeByName(declarator, "id");
            Object init = invokeByName(declarator, "init");
            String name = extractIdentifierName(id, "VariableDeclarator.id");
            QinIrExpression initializer = init == null
                    ? new QinIrNullLiteral()
                    : lowerDeclarationInitializer(init, javaImportLookup, declarationLookup);
            lowered.add(new QinIrConstDeclaration(name, initializer));
        }
        return lowered;
    }

    List<QinIrConstDeclaration> lowerVariableDeclaration(
            VariableDeclaration variableDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerVariableDeclaration((Object) variableDeclarationAst, javaImportLookup, declarationLookup);
    }

    QinIrConstDeclaration lowerCallableDeclaration(
            Object declarationAst,
            String nodeType,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object id = invokeByName(declarationAst, "id");
        if (id == null) {
            throw qjsError("QJS2010", "Anonymous " + nodeType + " is not supported in Qin subset");
        }
        String name = extractIdentifierName(id, nodeType + ".id");
        if ("FunctionDeclaration".equals(nodeType)) {
            return new QinIrConstDeclaration(
                    name,
                    lowerFunctionDeclarationOrNull(declarationAst, javaImportLookup, declarationLookup));
        }
        if ("ClassDeclaration".equals(nodeType)) {
            return new QinIrConstDeclaration(
                    name,
                    lowerClassDeclarationRuntimeValue(declarationAst, javaImportLookup, declarationLookup));
        }
        throw qjsError("QJS2010", "Unsupported callable declaration type: " + nodeType);
    }

    QinIrConstDeclaration lowerCallableDeclaration(
            FunctionDeclaration declarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Identifier id = declarationAst.id();
        if (id == null) {
            throw qjsError("QJS2010", "Anonymous FunctionDeclaration is not supported in Qin subset");
        }
        return new QinIrConstDeclaration(
                id.name(),
                lowerFunctionDeclarationOrNull(declarationAst, javaImportLookup, declarationLookup));
    }

    QinIrConstDeclaration lowerCallableDeclaration(
            ClassDeclaration declarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerCallableDeclaration((Object) declarationAst, "ClassDeclaration", javaImportLookup, declarationLookup);
    }

    private QinIrExpression lowerDeclarationInitializer(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression initializer = lowerRuntimeExpression(expressionAst, javaImportLookup, declarationLookup);
        if (initializer instanceof QinIrObjectLiteral
                || initializer instanceof QinIrJavaNewExpression
                || initializer instanceof QinIrIdentifierReference
                || initializer instanceof QinIrMemberAccessExpression
                || initializer instanceof QinIrBuiltinCallExpression
                || initializer instanceof QinIrFunctionLiteral
                || initializer instanceof QinIrNumberLiteral
                || initializer instanceof QinIrStringLiteral
                || initializer instanceof QinIrBooleanLiteral
                || initializer instanceof QinIrArrayLiteral
                || initializer instanceof QinIrNullLiteral) {
            return initializer;
        }
        throw qjsError("QJS2002", "Unsupported const initializer expression");
    }

    private QinIrObjectLiteral lowerObjectLiteral(Object objectExpressionAst) {
        String nodeType = simpleName(objectExpressionAst);
        if (!"ObjectExpression".equals(nodeType)) {
            throw qjsError("QJS2002", "Only object literal initializer is supported, got: " + nodeType);
        }

        List<?> properties = asList(invokeByName(objectExpressionAst, "properties"), "ObjectExpression.properties");
        List<QinIrObjectProperty> irProperties = new ArrayList<>();

        for (Object property : properties) {
            if (!"Property".equals(simpleName(property))) {
                throw qjsError("QJS2002", "Only normal object property is supported, got: " + simpleName(property));
            }
            Object keyNode = invokeByName(property, "key");
            Object valueNode = invokeByName(property, "value");

            String key = extractPropertyKey(keyNode);
            QinIrExpression value = lowerObjectPropertyValue(valueNode);
            irProperties.add(new QinIrObjectProperty(key, value));
        }

        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrObjectLiteral lowerObjectLiteral(ObjectExpression objectExpressionAst) {
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (AstNode propertyNode : objectExpressionAst.properties()) {
            if (!(propertyNode instanceof Property property)) {
                throw qjsError(
                        "QJS2002",
                        "Only normal object property is supported, got: "
                                + propertyNode.getClass().getSimpleName());
            }
            String key = extractPropertyKey(property.key());
            QinIrExpression value = lowerObjectPropertyValue(propertyValue(property));
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrExpression lowerArrayLiteral(
            Object arrayExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            boolean runtimeMode) {
        List<?> elements = asList(invokeByName(arrayExpressionAst, "elements"), "ArrayExpression.elements");
        List<QinIrExpression> irElements = new ArrayList<>();
        boolean hasSpread = elements.stream().anyMatch(element -> "SpreadElement".equals(simpleName(element)));
        for (Object element : elements) {
            if (element == null) {
                QinIrExpression lowered = new QinIrNullLiteral();
                irElements.add(hasSpread
                        ? new QinIrBuiltinCallExpression("Global", "__qin_array_item__", List.of(lowered))
                        : lowered);
                continue;
            }
            if ("SpreadElement".equals(simpleName(element))) {
                QinIrExpression lowered = runtimeMode
                        ? lowerRuntimeExpression(invokeByName(element, "argument"), javaImportLookup, declarationLookup)
                        : lowerExpression(invokeByName(element, "argument"), javaImportLookup);
                irElements.add(new QinIrBuiltinCallExpression("Global", "__qin_array_spread__", List.of(lowered)));
                continue;
            }
            QinIrExpression lowered = runtimeMode
                    ? lowerRuntimeExpression(element, javaImportLookup, declarationLookup)
                    : lowerExpression(element, javaImportLookup);
            irElements.add(hasSpread
                    ? new QinIrBuiltinCallExpression("Global", "__qin_array_item__", List.of(lowered))
                    : lowered);
        }
        if (hasSpread) {
            return new QinIrBuiltinCallExpression("Global", "__qin_array_literal__", irElements);
        }
        return new QinIrArrayLiteral(irElements);
    }

    private QinIrExpression lowerArrayLiteral(
            ArrayExpression arrayExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            boolean runtimeMode) {
        List<QinIrExpression> irElements = new ArrayList<>();
        boolean hasSpread = arrayExpressionAst.elements().stream().anyMatch(element -> element instanceof SpreadElement);
        for (com.slime.ast.Expression element : arrayExpressionAst.elements()) {
            if (element == null) {
                QinIrExpression lowered = new QinIrNullLiteral();
                irElements.add(hasSpread
                        ? new QinIrBuiltinCallExpression("Global", "__qin_array_item__", List.of(lowered))
                        : lowered);
                continue;
            }
            if (element instanceof SpreadElement spreadElement) {
                QinIrExpression lowered = runtimeMode
                        ? lowerRuntimeExpression(spreadElement.argument(), javaImportLookup, declarationLookup)
                        : lowerExpression(spreadElement.argument(), javaImportLookup);
                irElements.add(new QinIrBuiltinCallExpression("Global", "__qin_array_spread__", List.of(lowered)));
                continue;
            }
            QinIrExpression lowered = runtimeMode
                    ? lowerRuntimeExpression(element, javaImportLookup, declarationLookup)
                    : lowerExpression(element, javaImportLookup);
            irElements.add(hasSpread
                    ? new QinIrBuiltinCallExpression("Global", "__qin_array_item__", List.of(lowered))
                    : lowered);
        }
        if (hasSpread) {
            return new QinIrBuiltinCallExpression("Global", "__qin_array_literal__", irElements);
        }
        return new QinIrArrayLiteral(irElements);
    }

    QinIrExpression lowerFunctionDeclarationOrNull(
            Object functionDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionLikeOrNull(
                functionDeclarationAst,
                "FunctionDeclaration",
                javaImportLookup,
                declarationLookup);
    }

    QinIrExpression lowerFunctionDeclarationOrNull(
            FunctionDeclaration functionDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionLikeOrNull(functionDeclarationAst, "FunctionDeclaration", javaImportLookup, declarationLookup);
    }

    QinIrExpression lowerClassDeclarationRuntimeValue(
            ClassDeclaration classDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerClassDeclarationRuntimeValue((Object) classDeclarationAst, javaImportLookup, declarationLookup);
    }

    QinIrExpression lowerClassDeclarationRuntimeValue(
            Object classDeclarationAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrObjectLiteral runtimeDefinition = lowerRequiredFunctionRuntimeDefinition(
                classDeclarationAst,
                "ClassDeclaration",
                javaImportLookup,
                declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                List.of(runtimeDefinition));
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            com.slime.ast.Expression functionExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (functionExpressionAst instanceof FunctionExpression) {
            return lowerFunctionLikeOrNull(
                    (FunctionExpression) functionExpressionAst,
                    javaImportLookup,
                    declarationLookup);
        }
        if (functionExpressionAst instanceof ArrowFunctionExpression) {
            return lowerFunctionLikeOrNull(
                    (ArrowFunctionExpression) functionExpressionAst,
                    javaImportLookup,
                    declarationLookup);
        }
        if (functionExpressionAst instanceof ClassExpression) {
            return lowerFunctionLikeOrNull(
                    (ClassExpression) functionExpressionAst,
                    javaImportLookup,
                    declarationLookup);
        }
        throw qjsError(
                "QJS2001",
                "Unsupported function-like expression type: " + functionExpressionAst.getClass().getSimpleName());
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            FunctionExpression functionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionLikeOrNull(functionAst, "FunctionExpression", javaImportLookup, declarationLookup);
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            ArrowFunctionExpression functionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        boolean requiresRuntimeModel = hasFunctionParameters(functionAst);
        QinIrObjectLiteral runtimeDefinition = requiresRuntimeModel
                ? lowerRequiredFunctionRuntimeDefinition(
                        functionAst,
                        "ArrowFunctionExpression",
                        javaImportLookup,
                        declarationLookup)
                : lowerFunctionRuntimeDefinition(
                        functionAst,
                        "ArrowFunctionExpression",
                        javaImportLookup,
                        declarationLookup);
        if (runtimeDefinition != null) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                    List.of(runtimeDefinition));
        }
        if (functionAst.body() instanceof BlockStatement blockStatement) {
            return lowerFunctionLiteralFromBlock(blockStatement, javaImportLookup, declarationLookup, true);
        }
        if (functionAst.body() instanceof com.slime.ast.Expression expressionBody) {
            QinIrExpression returnExpression = lowerFunctionReturnExpression(expressionBody, true);
            if (returnExpression != null) {
                return new QinIrFunctionLiteral(returnExpression);
            }
        }
        return new QinIrFunctionLiteral(new QinIrNullLiteral());
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            ClassExpression functionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrObjectLiteral runtimeDefinition = lowerRequiredFunctionRuntimeDefinition(
                functionAst,
                "ClassExpression",
                javaImportLookup,
                declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                List.of(runtimeDefinition));
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            FunctionDeclaration functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrObjectLiteral runtimeDefinition = lowerRequiredFunctionRuntimeDefinition(
                functionAst,
                debugNodeName,
                javaImportLookup,
                declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                List.of(runtimeDefinition));
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            FunctionExpression functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        boolean requiresRuntimeModel = hasFunctionParameters(functionAst);
        QinIrObjectLiteral runtimeDefinition = requiresRuntimeModel
                ? lowerRequiredFunctionRuntimeDefinition(
                        functionAst,
                        debugNodeName,
                        javaImportLookup,
                        declarationLookup)
                : lowerFunctionRuntimeDefinition(
                        functionAst,
                        debugNodeName,
                        javaImportLookup,
                        declarationLookup);
        if (runtimeDefinition != null) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                    List.of(runtimeDefinition));
        }
        return lowerFunctionLiteralFromBlock(functionAst.body(), javaImportLookup, declarationLookup, true);
    }

    private QinIrExpression lowerFunctionLikeOrNull(
            Object functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        boolean requiresRuntimeModel = hasFunctionParameters(functionAst);
        QinIrObjectLiteral runtimeDefinition = requiresRuntimeModel
                ? lowerRequiredFunctionRuntimeDefinition(
                        functionAst,
                        debugNodeName,
                        javaImportLookup,
                        declarationLookup)
                : lowerFunctionRuntimeDefinition(
                        functionAst,
                        debugNodeName,
                        javaImportLookup,
                        declarationLookup);
        if (runtimeDefinition != null) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                    List.of(runtimeDefinition));
        }

        // Backward-compatible fallback for extremely unsupported shapes.
        Object body = invokeByName(functionAst, "body");
        if (!"BlockStatement".equals(simpleName(body))) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        List<?> statements = asList(invokeByName(body, "body"), debugNodeName + ".body.body");
        if (statements.size() != 1) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }

        Object onlyStatement = statements.get(0);
        if (!"ReturnStatement".equals(simpleName(onlyStatement))) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        Object argument = invokeByName(onlyStatement, "argument");
        if (argument == null) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        QinIrExpression returnExpression = lowerFunctionReturnExpression(argument, true);
        if (returnExpression == null) {
            return new QinIrFunctionLiteral(new QinIrNullLiteral());
        }
        return new QinIrFunctionLiteral(returnExpression);
    }

    private QinIrExpression lowerFunctionLiteralFromBlock(
            BlockStatement body,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            boolean permissive) {
        List<com.slime.ast.Statement> statements = body.body();
        QinIrExpression returnExpression = lowerRuntimeFunctionBlockBody(
                statements,
                javaImportLookup == null ? Map.of() : javaImportLookup,
                declarationLookup == null ? Map.of() : declarationLookup,
                permissive);
        return new QinIrFunctionLiteral(returnExpression);
    }

    private QinIrExpression lowerRuntimeFunctionBlockBody(
            List<? extends com.slime.ast.Statement> statements,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            boolean permissive) {
        if (statements == null || statements.isEmpty()) {
            return new QinIrNullLiteral();
        }

        Map<String, QinIrExpression> scopedLookup = new LinkedHashMap<>(declarationLookup);
        List<QinIrLocalVariableDeclaration> localDeclarations = new ArrayList<>();
        List<QinIrExpression> leadingExpressions = new ArrayList<>();
        for (int i = 0; i < statements.size(); i++) {
            com.slime.ast.Statement statement = statements.get(i);
            boolean last = i == statements.size() - 1;
            if (statement instanceof VariableDeclaration variableDeclaration) {
                if (!lowerRuntimeLocalVariableDeclarations(
                        variableDeclaration,
                        javaImportLookup,
                        scopedLookup,
                        localDeclarations)) {
                    return new QinIrNullLiteral();
                }
                continue;
            }
            if (statement instanceof ExpressionStatement expressionStatement) {
                leadingExpressions.add(lowerRuntimeExpression(
                        expressionStatement.expression(),
                        javaImportLookup,
                        scopedLookup));
                continue;
            }
            if (statement instanceof ReturnStatement returnStatement) {
                if (!last) {
                    return new QinIrNullLiteral();
                }
                QinIrExpression result = returnStatement.argument() == null
                        ? new QinIrNullLiteral()
                        : lowerRuntimeExpression(returnStatement.argument(), javaImportLookup, scopedLookup);
                return wrapRuntimeFunctionBlock(localDeclarations, leadingExpressions, result);
            }
            if (!permissive) {
                return null;
            }
            return new QinIrNullLiteral();
        }
        return wrapRuntimeFunctionBlock(localDeclarations, leadingExpressions, new QinIrNullLiteral());
    }

    private boolean lowerRuntimeLocalVariableDeclarations(
            VariableDeclaration variableDeclaration,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> scopedLookup,
            List<QinIrLocalVariableDeclaration> localDeclarations) {
        if (variableDeclaration.declarations() == null || variableDeclaration.declarations().isEmpty()) {
            return false;
        }
        for (var declarator : variableDeclaration.declarations()) {
            if (declarator == null || declarator.id() == null) {
                return false;
            }
            String name = extractIdentifierName(declarator.id(), "VariableDeclarator.id");
            if (name == null || name.isBlank()) {
                return false;
            }
            QinIrExpression initializer = declarator.init() == null
                    ? new QinIrNullLiteral()
                    : lowerRuntimeExpression(declarator.init(), javaImportLookup, scopedLookup);
            localDeclarations.add(new QinIrLocalVariableDeclaration(name, initializer));
            scopedLookup.put(name, new QinIrIdentifierReference(name));
        }
        return true;
    }

    private QinIrExpression wrapRuntimeFunctionBlock(
            List<QinIrLocalVariableDeclaration> localDeclarations,
            List<QinIrExpression> leadingExpressions,
            QinIrExpression resultExpression) {
        if (localDeclarations.isEmpty()) {
            return wrapSequenceExpression(leadingExpressions, resultExpression);
        }
        return new QinIrLetExpression(localDeclarations, leadingExpressions, resultExpression);
    }

    private boolean hasFunctionParameters(ArrowFunctionExpression functionAst) {
        return functionAst.params() != null && !functionAst.params().isEmpty();
    }

    private boolean hasFunctionParameters(FunctionDeclaration functionAst) {
        return functionAst.params() != null && !functionAst.params().isEmpty();
    }

    private boolean hasFunctionParameters(FunctionExpression functionAst) {
        return functionAst.params() != null && !functionAst.params().isEmpty();
    }

    private boolean hasFunctionParameters(Object functionAst) {
        if (functionAst == null) {
            return false;
        }
        try {
            Object params = invokeByName(functionAst, "params");
            return params instanceof List<?> list && !list.isEmpty();
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    private QinIrExpression lowerFunctionReturnExpression(Object expressionAst) {
        return lowerFunctionReturnExpression(expressionAst, false);
    }

    private QinIrExpression lowerFunctionReturnExpression(Object expressionAst, boolean permissive) {
        if (expressionAst instanceof Literal literal) {
            return lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof FunctionExpression functionExpression) {
            return lowerFunctionLikeOrNull(functionExpression, Map.of(), Map.of());
        }
        if (expressionAst instanceof ArrowFunctionExpression arrowFunctionExpression) {
            return lowerFunctionLikeOrNull(arrowFunctionExpression, Map.of(), Map.of());
        }
        if (expressionAst instanceof ClassExpression classExpression) {
            return lowerFunctionLikeOrNull(classExpression, Map.of(), Map.of());
        }
        if (expressionAst instanceof ObjectExpression objectExpression) {
            return lowerFunctionReturnObjectLiteral(objectExpression, permissive);
        }
        if (expressionAst instanceof ArrayExpression arrayExpression) {
            return lowerArrayLiteral(arrayExpression, Map.of(), Map.of(), false);
        }

        String nodeType = simpleName(expressionAst);
        if ("Literal".equals(nodeType)) {
            QinIrExpression loweredRegexLiteral = lowerRegexLiteralOrNull(expressionAst);
            if (loweredRegexLiteral != null) {
                return loweredRegexLiteral;
            }
            Object value = invokeByName(expressionAst, "value");
            if (value == null) {
                return new QinIrNullLiteral();
            }
            if (value instanceof Number number) {
                return new QinIrNumberLiteral(number.doubleValue());
            }
            if (value instanceof String text) {
                return new QinIrStringLiteral(normalizeStringLiteral(text));
            }
            if (value instanceof Boolean boolValue) {
                return new QinIrBooleanLiteral(boolValue);
            }
            return null;
        }
        if ("FunctionExpression".equals(nodeType)
                || "ArrowFunctionExpression".equals(nodeType)
                || "ClassExpression".equals(nodeType)) {
            return lowerFunctionLikeOrNull(expressionAst, nodeType, Map.of(), Map.of());
        }
        if ("ObjectExpression".equals(nodeType)) {
            return lowerFunctionReturnObjectLiteral(expressionAst, permissive);
        }
        if ("ArrayExpression".equals(nodeType)) {
            return lowerArrayLiteral(expressionAst, Map.of(), Map.of(), false);
        }
        if (permissive) {
            return new QinIrNullLiteral();
        }
        return null;
    }

    private QinIrObjectLiteral lowerFunctionReturnObjectLiteral(Object objectExpressionAst, boolean permissive) {
        List<?> properties = asList(invokeByName(objectExpressionAst, "properties"), "ObjectExpression.properties");
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (Object property : properties) {
            if (!"Property".equals(simpleName(property))) {
                if (permissive) {
                    return new QinIrObjectLiteral(List.of());
                }
                return null;
            }
            String key = extractPropertyKey(invokeByName(property, "key"));
            QinIrExpression value = lowerFunctionReturnExpression(invokeByName(property, "value"), permissive);
            if (value == null) {
                if (permissive) {
                    value = new QinIrNullLiteral();
                } else {
                    return null;
                }
            }
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    private QinIrObjectLiteral lowerFunctionReturnObjectLiteral(
            ObjectExpression objectExpressionAst,
            boolean permissive) {
        List<QinIrObjectProperty> irProperties = new ArrayList<>();
        for (AstNode propertyNode : objectExpressionAst.properties()) {
            if (!(propertyNode instanceof Property property)) {
                if (permissive) {
                    return new QinIrObjectLiteral(List.of());
                }
                return null;
            }
            String key = extractPropertyKey(property.key());
            QinIrExpression value = lowerFunctionReturnExpression(propertyValue(property), permissive);
            if (value == null) {
                if (permissive) {
                    value = new QinIrNullLiteral();
                } else {
                    return null;
                }
            }
            irProperties.add(new QinIrObjectProperty(key, value));
        }
        return new QinIrObjectLiteral(irProperties);
    }

    QinIrObjectLiteral lowerFunctionRuntimeDefinition(
            Object functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionRuntimeDefinition(
                functionAst,
                debugNodeName,
                javaImportLookup,
                declarationLookup,
                false);
    }

    QinIrObjectLiteral lowerRequiredFunctionRuntimeDefinition(
            Object functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        return lowerFunctionRuntimeDefinition(
                functionAst,
                debugNodeName,
                javaImportLookup,
                declarationLookup,
                true);
    }

    private QinIrObjectLiteral lowerFunctionRuntimeDefinition(
            Object functionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup,
            boolean required) {
        int effectiveBudget = loweringContext.functionModelBudgetRemaining() > 0
                ? loweringContext.functionModelBudgetRemaining()
                : functionModelBudgetRemaining;
        if (effectiveBudget <= 0 && !required) {
            return null;
        }
        IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        int[] encodedNodeCount = new int[] {0};
        boolean[] overflow = new boolean[] {false};
        int effectiveSourceLength = loweringContext.sourceLength() > 0
                ? loweringContext.sourceLength()
                : currentSourceLength;
        int perFunctionNodeLimit = required
                ? computeRequiredFunctionAstNodeLimit(effectiveSourceLength)
                : computeFunctionAstNodeLimit(effectiveSourceLength);
        QinIrExpression astExpression = encodeFunctionAstNode(
                functionAst,
                0,
                seen,
                encodedNodeCount,
                perFunctionNodeLimit,
                overflow);
        if (overflow[0]) {
            if (required) {
                throw qjsError(
                        "QJS2030",
                        "Required runtime function model is too large: "
                                + debugNodeName
                                + "; id=" + extractFunctionLikeDebugName(functionAst)
                                + "; encodedNodes=" + encodedNodeCount[0]
                                + "; nodeLimit=" + perFunctionNodeLimit);
            }
            return null;
        }
        if (!(astExpression instanceof QinIrObjectLiteral astObject)) {
            if (required) {
                throw qjsError("QJS2031", "Required runtime function model must encode to object: " + debugNodeName);
            }
            return null;
        }
        if (encodedNodeCount[0] > effectiveBudget && !required) {
            return null;
        }
        effectiveBudget = Math.max(0, effectiveBudget - encodedNodeCount[0]);
        functionModelBudgetRemaining = effectiveBudget;
        loweringContext.setFunctionModelBudgetRemaining(effectiveBudget);

        QinIrObjectLiteral closureObject = buildFunctionClosureObject(declarationLookup);
        List<QinIrObjectProperty> properties = new ArrayList<>();
        properties.add(new QinIrObjectProperty("__qin_function_model", new QinIrStringLiteral("slime-ast-v1")));
        properties.add(new QinIrObjectProperty("debugNode", new QinIrStringLiteral(debugNodeName)));
        if (shouldExternalizeFunctionModel(effectiveSourceLength, encodedNodeCount[0], required)) {
            String modelId = loweringContext.addFunctionModelArtifact(astObject);
            properties.add(new QinIrObjectProperty("astRef", new QinIrStringLiteral(modelId)));
        } else {
            properties.add(new QinIrObjectProperty("ast", astObject));
        }
        properties.add(new QinIrObjectProperty("closure", closureObject));
        properties.add(new QinIrObjectProperty(
                "javaImportCount",
                new QinIrNumberLiteral(javaImportLookup == null ? 0 : javaImportLookup.size())));
        return new QinIrObjectLiteral(properties);
    }

    private boolean shouldExternalizeFunctionModel(int sourceLength, int encodedNodeCount, boolean required) {
        if (sourceLength > 50_000) {
            return true;
        }
        return required && encodedNodeCount > 8_000;
    }

    private static int computeFunctionAstNodeLimit(int sourceLength) {
        if (sourceLength <= 0) {
            return 14000;
        }
        if (sourceLength <= 25_000) {
            return 18000;
        }
        if (sourceLength <= 80_000) {
            return 14000;
        }
        if (sourceLength <= 300_000) {
            return 10000;
        }
        return 12000;
    }

    private static int computeRequiredFunctionAstNodeLimit(int sourceLength) {
        if (sourceLength <= 0) {
            return 60_000;
        }
        if (sourceLength <= 300_000) {
            return 80_000;
        }
        return 240_000;
    }

    private QinIrObjectLiteral buildFunctionClosureObject(Map<String, QinIrExpression> declarationLookup) {
        if (declarationLookup == null || declarationLookup.isEmpty()) {
            return new QinIrObjectLiteral(List.of());
        }
        List<String> names = new ArrayList<>(declarationLookup.keySet());
        names.sort(String::compareTo);
        List<QinIrObjectProperty> properties = new ArrayList<>();
        for (String name : names) {
            if (name == null
                    || name.isBlank()
                    || !QinParserRuntimeNames.IDENTIFIER_PATTERN.matcher(name).matches()) {
                continue;
            }
            // Captured top-level declarations must remain live bindings. A
            // runtime-created closure may outlive the current top-level value,
            // so storing the value snapshot here breaks module-level mutable
            // state patterns such as parser registries.
            QinIrObjectLiteral refDescriptor = new QinIrObjectLiteral(List.of(
                    new QinIrObjectProperty("__qin_ref_name", new QinIrStringLiteral(name))));
            properties.add(new QinIrObjectProperty(name, refDescriptor));
        }
        return new QinIrObjectLiteral(properties);
    }

    private String extractFunctionLikeDebugName(Object functionAst) {
        try {
            Object id = invokeByName(functionAst, "id");
            if (id == null) {
                return "<anonymous>";
            }
            return extractIdentifierName(id, "function-like.id");
        } catch (RuntimeException ignored) {
            return "<unknown>";
        }
    }

    private static boolean isUninitializedForwardReference(String name, QinIrExpression declaration) {
        return declaration instanceof QinIrIdentifierReference identifierReference
                && name.equals(identifierReference.name());
    }

    private QinIrExpression encodeFunctionAstNode(
            Object node,
            int depth,
            IdentityHashMap<Object, Boolean> seen,
            int[] encodedNodeCount,
            int maxNodes,
            boolean[] overflow) {
        if (node == null) {
            return new QinIrNullLiteral();
        }
        if (depth > 180 || encodedNodeCount[0] > maxNodes) {
            overflow[0] = true;
            return new QinIrNullLiteral();
        }
        encodedNodeCount[0] += 1;
        if (node instanceof String text) {
            return new QinIrStringLiteral(text);
        }
        if (node instanceof Boolean boolValue) {
            return new QinIrBooleanLiteral(boolValue);
        }
        if (node instanceof Number number) {
            return new QinIrNumberLiteral(number.doubleValue());
        }
        if (node instanceof Character character) {
            return new QinIrStringLiteral(String.valueOf(character));
        }
        if (node.getClass().isEnum()) {
            return new QinIrStringLiteral(String.valueOf(node));
        }
        if (node instanceof Collection<?> collection) {
            List<QinIrExpression> elements = new ArrayList<>();
            for (Object element : collection) {
                elements.add(encodeFunctionAstNode(element, depth + 1, seen, encodedNodeCount, maxNodes, overflow));
            }
            return new QinIrArrayLiteral(elements);
        }
        if (node.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(node);
            List<QinIrExpression> elements = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                Object element = java.lang.reflect.Array.get(node, i);
                elements.add(encodeFunctionAstNode(element, depth + 1, seen, encodedNodeCount, maxNodes, overflow));
            }
            return new QinIrArrayLiteral(elements);
        }
        if (node instanceof Map<?, ?> map) {
            List<QinIrObjectProperty> properties = new ArrayList<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = String.valueOf(entry.getKey());
                properties.add(new QinIrObjectProperty(
                        key,
                        encodeFunctionAstNode(entry.getValue(), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
            }
            return new QinIrObjectLiteral(properties);
        }
        if (seen.containsKey(node)) {
            return new QinIrNullLiteral();
        }
        seen.put(node, Boolean.TRUE);
        try {
            QinIrObjectLiteral knownAstNode = encodeKnownAstNode(node, depth, seen, encodedNodeCount, maxNodes, overflow);
            if (knownAstNode != null) {
                return knownAstNode;
            }
            if (node.getClass().isRecord()) {
                List<QinIrObjectProperty> properties = new ArrayList<>();
                properties.add(new QinIrObjectProperty("type", new QinIrStringLiteral(node.getClass().getSimpleName())));
                for (RecordComponent component : node.getClass().getRecordComponents()) {
                    if ("location".equals(component.getName())) {
                        continue;
                    }
                    Object value = invokeRecordComponent(node, component);
                    properties.add(new QinIrObjectProperty(
                            component.getName(),
                            encodeFunctionAstNode(value, depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                }
                return new QinIrObjectLiteral(properties);
            }
            List<QinIrObjectProperty> beanProperties = new ArrayList<>();
            beanProperties.add(new QinIrObjectProperty("type", new QinIrStringLiteral(node.getClass().getSimpleName())));
            Method[] methods = node.getClass().getMethods();
            for (Method method : methods) {
                if (method.getParameterCount() != 0) {
                    continue;
                }
                if (Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                if (method.getDeclaringClass() == Object.class) {
                    continue;
                }
                String propertyName = toBeanPropertyName(method.getName());
                if (propertyName == null || propertyName.isBlank()) {
                    continue;
                }
                if ("class".equals(propertyName) || "location".equals(propertyName)) {
                    continue;
                }
                Object value;
                try {
                    value = method.invoke(node);
                } catch (ReflectiveOperationException ignored) {
                    continue;
                }
                beanProperties.add(new QinIrObjectProperty(
                        propertyName,
                        encodeFunctionAstNode(value, depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
            }
            if (beanProperties.size() > 1) {
                return new QinIrObjectLiteral(beanProperties);
            }
            return new QinIrStringLiteral(String.valueOf(node));
        } finally {
            seen.remove(node);
        }
    }

    private QinIrObjectLiteral encodeKnownAstNode(
            Object node,
            int depth,
            IdentityHashMap<Object, Boolean> seen,
            int[] encodedNodeCount,
            int maxNodes,
            boolean[] overflow) {
        if (node instanceof ArrowFunctionExpression arrowFunctionExpression) {
            List<QinIrObjectProperty> props = new ArrayList<>();
            props.add(new QinIrObjectProperty("type", new QinIrStringLiteral("ArrowFunctionExpression")));
            props.add(new QinIrObjectProperty("id", new QinIrNullLiteral()));
            props.add(new QinIrObjectProperty("params", encodeFunctionAstNode(
                    arrowFunctionExpression.params(),
                    depth + 1,
                    seen,
                    encodedNodeCount,
                    maxNodes,
                    overflow)));
            props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                    arrowFunctionExpression.body(),
                    depth + 1,
                    seen,
                    encodedNodeCount,
                    maxNodes,
                    overflow)));
            props.add(new QinIrObjectProperty("async", encodeFunctionAstNode(
                    arrowFunctionExpression.async(),
                    depth + 1,
                    seen,
                    encodedNodeCount,
                    maxNodes,
                    overflow)));
            props.add(new QinIrObjectProperty("expression", encodeFunctionAstNode(
                    arrowFunctionExpression.expression(),
                    depth + 1,
                    seen,
                    encodedNodeCount,
                    maxNodes,
                    overflow)));
            return new QinIrObjectLiteral(props);
        }
        String type = simpleName(node);
        if (type == null || type.isBlank()) {
            return null;
        }
        List<QinIrObjectProperty> props = new ArrayList<>();
        props.add(new QinIrObjectProperty("type", new QinIrStringLiteral(type)));
        switch (type) {
            case "Identifier" -> {
                props.add(new QinIrObjectProperty("name", encodeFunctionAstNode(
                        invokeByName(node, "name"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "Literal" -> {
                props.add(new QinIrObjectProperty("value", encodeFunctionAstNode(
                        invokeByName(node, "value"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("raw", encodeFunctionAstNode(
                        invokeByName(node, "raw"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("regex", encodeFunctionAstNode(
                        invokeByName(node, "regex"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("bigint", encodeFunctionAstNode(
                        invokeByName(node, "bigint"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ThisExpression", "Super", "BreakStatement", "ContinueStatement", "EmptyStatement" -> {
                return new QinIrObjectLiteral(props);
            }
            case "ParenthesizedExpression" -> {
                props.add(new QinIrObjectProperty("expression", encodeFunctionAstNode(
                        invokeByName(node, "expression"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ArrayExpression" -> {
                props.add(new QinIrObjectProperty("elements", encodeFunctionAstNode(
                        invokeByName(node, "elements"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ObjectExpression" -> {
                props.add(new QinIrObjectProperty("properties", encodeFunctionAstNode(
                        invokeByName(node, "properties"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "Property" -> {
                props.add(new QinIrObjectProperty("key", encodeFunctionAstNode(
                        invokeByName(node, "key"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("value", encodeFunctionAstNode(
                        invokeByName(node, "value"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("computed", encodeFunctionAstNode(
                        invokeByName(node, "computed"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "MemberExpression" -> {
                props.add(new QinIrObjectProperty("object", encodeFunctionAstNode(
                        invokeByName(node, "object"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("property", encodeFunctionAstNode(
                        invokeByName(node, "property"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("computed", encodeFunctionAstNode(
                        invokeByName(node, "computed"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("optional", encodeFunctionAstNode(
                        invokeByName(node, "optional"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "CallExpression", "NewExpression" -> {
                props.add(new QinIrObjectProperty("callee", encodeFunctionAstNode(
                        invokeByName(node, "callee"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("arguments", encodeFunctionAstNode(
                        invokeByName(node, "arguments"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                if ("CallExpression".equals(type)) {
                    props.add(new QinIrObjectProperty("optional", encodeFunctionAstNode(
                            invokeByName(node, "optional"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                }
                return new QinIrObjectLiteral(props);
            }
            case "UnaryExpression" -> {
                props.add(new QinIrObjectProperty("operator", encodeFunctionAstNode(
                        invokeByName(node, "operator"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("argument", encodeFunctionAstNode(
                        invokeByName(node, "argument"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("prefix", encodeFunctionAstNode(
                        invokeByName(node, "prefix"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> {
                props.add(new QinIrObjectProperty("operator", encodeFunctionAstNode(
                        invokeByName(node, "operator"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("left", encodeFunctionAstNode(
                        invokeByName(node, "left"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("right", encodeFunctionAstNode(
                        invokeByName(node, "right"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "UpdateExpression" -> {
                props.add(new QinIrObjectProperty("operator", encodeFunctionAstNode(
                        invokeByName(node, "operator"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("argument", encodeFunctionAstNode(
                        invokeByName(node, "argument"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("prefix", encodeFunctionAstNode(
                        invokeByName(node, "prefix"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ConditionalExpression" -> {
                props.add(new QinIrObjectProperty("test", encodeFunctionAstNode(
                        invokeByName(node, "test"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("consequent", encodeFunctionAstNode(
                        invokeByName(node, "consequent"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("alternate", encodeFunctionAstNode(
                        invokeByName(node, "alternate"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "TaggedTemplateExpression" -> {
                props.add(new QinIrObjectProperty("tag", encodeFunctionAstNode(
                        invokeByName(node, "tag"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("quasi", encodeFunctionAstNode(
                        invokeByName(node, "quasi"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "TemplateLiteral" -> {
                props.add(new QinIrObjectProperty("quasis", encodeFunctionAstNode(
                        invokeByName(node, "quasis"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("expressions", encodeFunctionAstNode(
                        invokeByName(node, "expressions"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "TemplateElement" -> {
                List<QinIrObjectProperty> valueProps = new ArrayList<>();
                valueProps.add(new QinIrObjectProperty("raw", new QinIrStringLiteral(resolveTemplateElementRawText(node))));
                valueProps.add(new QinIrObjectProperty("cooked", new QinIrStringLiteral(resolveTemplateElementText(node))));
                props.add(new QinIrObjectProperty("value", new QinIrObjectLiteral(valueProps)));
                props.add(new QinIrObjectProperty("tail", encodeFunctionAstNode(
                        invokeByName(node, "tail"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ExpressionStatement" -> {
                props.add(new QinIrObjectProperty("expression", encodeFunctionAstNode(
                        invokeByName(node, "expression"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ReturnStatement" -> {
                props.add(new QinIrObjectProperty("argument", encodeFunctionAstNode(
                        invokeByName(node, "argument"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "VariableDeclaration" -> {
                props.add(new QinIrObjectProperty("kind", encodeFunctionAstNode(
                        invokeByName(node, "kind"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("declarations", encodeFunctionAstNode(
                        invokeByName(node, "declarations"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "VariableDeclarator" -> {
                props.add(new QinIrObjectProperty("id", encodeFunctionAstNode(
                        invokeByName(node, "id"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("init", encodeFunctionAstNode(
                        invokeByName(node, "init"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "BlockStatement" -> {
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "IfStatement" -> {
                props.add(new QinIrObjectProperty("test", encodeFunctionAstNode(
                        invokeByName(node, "test"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("consequent", encodeFunctionAstNode(
                        invokeByName(node, "consequent"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("alternate", encodeFunctionAstNode(
                        invokeByName(node, "alternate"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "TryStatement" -> {
                props.add(new QinIrObjectProperty("block", encodeFunctionAstNode(
                        invokeByName(node, "block"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("handler", encodeFunctionAstNode(
                        invokeByName(node, "handler"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("finalizer", encodeFunctionAstNode(
                        invokeByName(node, "finalizer"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "CatchClause" -> {
                props.add(new QinIrObjectProperty("param", encodeFunctionAstNode(
                        invokeByName(node, "param"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ForStatement" -> {
                props.add(new QinIrObjectProperty("init", encodeFunctionAstNode(
                        invokeByName(node, "init"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("test", encodeFunctionAstNode(
                        invokeByName(node, "test"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("update", encodeFunctionAstNode(
                        invokeByName(node, "update"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ForOfStatement" -> {
                props.add(new QinIrObjectProperty("left", encodeFunctionAstNode(
                        invokeByName(node, "left"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("right", encodeFunctionAstNode(
                        invokeByName(node, "right"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "SwitchStatement" -> {
                props.add(new QinIrObjectProperty("discriminant", encodeFunctionAstNode(
                        invokeByName(node, "discriminant"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("cases", encodeFunctionAstNode(
                        invokeByName(node, "cases"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "SwitchCase" -> {
                props.add(new QinIrObjectProperty("test", encodeFunctionAstNode(
                        invokeByName(node, "test"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("consequent", encodeFunctionAstNode(
                        invokeByName(node, "consequent"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "FunctionExpression", "FunctionDeclaration" -> {
                props.add(new QinIrObjectProperty("id", encodeFunctionAstNode(
                        invokeByName(node, "id"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("params", encodeFunctionAstNode(
                        invokeByName(node, "params"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("generator", encodeFunctionAstNode(
                        invokeByName(node, "generator"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("async", encodeFunctionAstNode(
                        invokeByName(node, "async"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ClassDeclaration", "ClassExpression" -> {
                props.add(new QinIrObjectProperty("id", encodeFunctionAstNode(
                        invokeByName(node, "id"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("superClass", encodeFunctionAstNode(
                        invokeByName(node, "superClass"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("decorators", encodeFunctionAstNode(
                        invokeByName(node, "decorators"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ClassBody" -> {
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "MethodDefinition" -> {
                props.add(new QinIrObjectProperty("key", encodeFunctionAstNode(
                        invokeByName(node, "key"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("value", encodeFunctionAstNode(
                        invokeByName(node, "value"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("kind", encodeFunctionAstNode(
                        invokeByName(node, "kind"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("computed", encodeFunctionAstNode(
                        invokeByName(node, "computed"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("static", encodeFunctionAstNode(
                        invokeByName(node, "isStatic"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("isStatic", encodeFunctionAstNode(
                        invokeByName(node, "isStatic"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("decorators", encodeFunctionAstNode(
                        invokeByName(node, "decorators"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "PropertyDefinition", "FieldDefinition" -> {
                props.add(new QinIrObjectProperty("key", encodeFunctionAstNode(
                        invokeByName(node, "key"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("value", encodeFunctionAstNode(
                        invokeByName(node, "value"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("computed", encodeFunctionAstNode(
                        invokeByName(node, "computed"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("static", encodeFunctionAstNode(
                        invokeByName(node, "isStatic"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("isStatic", encodeFunctionAstNode(
                        invokeByName(node, "isStatic"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("decorators", encodeFunctionAstNode(
                        invokeByName(node, "decorators"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "ArrowFunctionExpression" -> {
                props.add(new QinIrObjectProperty("id", new QinIrNullLiteral()));
                props.add(new QinIrObjectProperty("params", encodeFunctionAstNode(
                        invokeByName(node, "params"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("body", encodeFunctionAstNode(
                        invokeByName(node, "body"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("async", encodeFunctionAstNode(
                        invokeByName(node, "async"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                props.add(new QinIrObjectProperty("expression", encodeFunctionAstNode(
                        invokeByName(node, "expression"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            case "SequenceExpression" -> {
                props.add(new QinIrObjectProperty("expressions", encodeFunctionAstNode(
                        invokeByName(node, "expressions"), depth + 1, seen, encodedNodeCount, maxNodes, overflow)));
                return new QinIrObjectLiteral(props);
            }
            default -> {
                return null;
            }
        }
    }

    private static String toBeanPropertyName(String methodName) {
        if (methodName == null || methodName.isBlank()) {
            return null;
        }
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
        }
        if (methodName.startsWith("is") && methodName.length() > 2) {
            return Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
        }
        return null;
    }

    private Object invokeRecordComponent(Object node, RecordComponent component) {
        try {
            return component.getAccessor().invoke(node);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private QinIrExpression lowerObjectPropertyValue(Object expressionAst) {
        QinIrExpression value = lowerExpression(expressionAst, Map.of());
        if (value instanceof QinIrNumberLiteral
                || value instanceof QinIrStringLiteral
                || value instanceof QinIrBooleanLiteral
                || value instanceof QinIrNullLiteral
                || value instanceof QinIrArrayLiteral
                || value instanceof QinIrIdentifierReference
                || value instanceof QinIrMemberAccessExpression) {
            return value;
        }
        throw qjsError("QJS2002", "Unsupported object property value expression");
    }

    private QinIrExpression lowerObjectPropertyValue(com.slime.ast.Expression expressionAst) {
        QinIrExpression value = lowerExpression(expressionAst, Map.of());
        if (value instanceof QinIrNumberLiteral
                || value instanceof QinIrStringLiteral
                || value instanceof QinIrBooleanLiteral
                || value instanceof QinIrNullLiteral
                || value instanceof QinIrArrayLiteral
                || value instanceof QinIrIdentifierReference
                || value instanceof QinIrMemberAccessExpression) {
            return value;
        }
        throw qjsError("QJS2002", "Unsupported object property value expression");
    }

    LoweredStatement lowerExpressionStatement(
            Object expressionStatementAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object expression = invokeByName(expressionStatementAst, "expression");
        String expressionNodeType = simpleName(expression);
        if ("AwaitExpression".equals(expressionNodeType)) {
            return lowerAwaitExpressionStatement(expression, javaImportLookup, declarationLookup);
        }
        if ("ImportExpression".equals(expressionNodeType)) {
            return lowerImportExpressionStatement(expression, javaImportLookup, declarationLookup);
        }
        if ("Identifier".equals(expressionNodeType)) {
            String name = asString(invokeByName(expression, "name"), "ExpressionStatement.expression.name");
            if ("import".equals(name) || "await".equals(name)) {
                return new LoweredStatement(null, null, null, null, null, null);
            }
        }
        if (!"CallExpression".equals(expressionNodeType)) {
            QinIrExpression runtimeExpression = lowerRuntimeExpression(expression, javaImportLookup, declarationLookup);
            return new LoweredStatement(null, new QinIrExpressionStatement(runtimeExpression), null, null, null, null);
        }

        Object callee = invokeByName(expression, "callee");
        if (isRuntimeShimCall(callee)) {
            QinIrBuiltinCallExpression shim = lowerGlobalBuiltinCallExpression(
                    expression,
                    javaImportLookup,
                    declarationLookup);
            return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
        }
        if (isConsoleLogCallee(callee)) {
            return lowerConsoleLogCall(expression, javaImportLookup, declarationLookup);
        }
        if (isDynamicImportCallee(callee)) {
            return lowerDynamicImportCalleeStatement(expression, javaImportLookup, declarationLookup);
        }
        if (isJavaInstanceMethodCallee(callee, declarationLookup)) {
            return lowerJavaInstanceMethodStatement(expression, declarationLookup, javaImportLookup);
        }

        QinIrExpression runtimeExpression = lowerRuntimeExpression(expression, javaImportLookup, declarationLookup);
        return new LoweredStatement(null, new QinIrExpressionStatement(runtimeExpression), null, null, null, null);
    }

    // Legacy typed-AST helper body retained for migration compatibility.
    // The live runtime-statement dispatch path now routes through QinRuntimeIrLowerer.
    LoweredStatement lowerExpressionStatement(
            ExpressionStatement expressionStatementAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        com.slime.ast.Expression expression = expressionStatementAst.expression();
        if (expression instanceof AwaitExpression awaitExpression) {
            return lowerAwaitExpressionStatement(awaitExpression, javaImportLookup, declarationLookup);
        }
        if (expression instanceof ImportExpression importExpression) {
            return lowerImportExpressionStatement(importExpression, javaImportLookup, declarationLookup);
        }
        if (expression instanceof Identifier identifier) {
            String name = identifier.name();
            if ("import".equals(name) || "await".equals(name)) {
                return new LoweredStatement(null, null, null, null, null, null);
            }
        }
        if (expression instanceof CallExpression callExpression) {
            com.slime.ast.Expression callee = callExpression.callee();
            if (isRuntimeShimCall(callee)) {
                QinIrBuiltinCallExpression shim = lowerGlobalBuiltinCallExpression(
                        callExpression,
                        javaImportLookup,
                        declarationLookup);
                return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
            }
            if (isConsoleLogCallee(callee)) {
                return lowerConsoleLogCall(callExpression, javaImportLookup, declarationLookup);
            }
            if (isDynamicImportCallee(callee)) {
                return lowerDynamicImportCalleeStatement(callExpression, javaImportLookup, declarationLookup);
            }
            if (isJavaInstanceMethodCallee(callee, declarationLookup)) {
                return lowerJavaInstanceMethodStatement(callExpression, declarationLookup, javaImportLookup);
            }
        }

        QinIrExpression runtimeExpression = lowerRuntimeExpression(expression, javaImportLookup, declarationLookup);
        return new LoweredStatement(null, new QinIrExpressionStatement(runtimeExpression), null, null, null, null);
    }

    private LoweredStatement lowerAwaitExpressionStatement(
            Object awaitExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object argumentAst = invokeByName(awaitExpressionAst, "argument");
        QinIrExpression argument = lowerRuntimeExpression(argumentAst, javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.TOP_LEVEL_AWAIT_SHIM,
                List.of(argument));
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerAwaitExpressionStatement(
            AwaitExpression awaitExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression argument =
                lowerRuntimeExpression(awaitExpressionAst.argument(), javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.TOP_LEVEL_AWAIT_SHIM,
                List.of(argument));
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerImportExpressionStatement(
            Object importExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object sourceAst = invokeByName(importExpressionAst, "source");
        QinIrExpression argument = lowerRuntimeExpression(sourceAst, javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                List.of(argument));
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerImportExpressionStatement(
            ImportExpression importExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression argument =
                lowerRuntimeExpression(importExpressionAst.source(), javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                List.of(argument));
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerDynamicImportCalleeStatement(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> arguments = lowerRuntimeArguments(callExpressionAst, javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                arguments);
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerDynamicImportCalleeStatement(
            CallExpression callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> arguments =
                lowerRuntimeArguments(callExpressionAst.arguments(), javaImportLookup, declarationLookup);
        QinIrBuiltinCallExpression shim = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                arguments);
        return new LoweredStatement(null, new QinIrExpressionStatement(shim), null, null, null, null);
    }

    private LoweredStatement lowerConsoleLogCall(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!"CallExpression".equals(simpleName(expressionAst))) {
            throw qjsError("QJS2001", "Only console.log(...) expression statement is supported");
        }

        Object callee = invokeByName(expressionAst, "callee");
        if (!isConsoleLogCallee(callee)) {
            throw qjsError("QJS2001", "Only console.log(...) call is supported");
        }

        List<?> arguments = asList(invokeByName(expressionAst, "arguments"), "CallExpression.arguments");
        if (arguments.size() != 1) {
            throw qjsError("QJS2002", "console.log(...) must have exactly one argument");
        }

        Object firstArgument = arguments.get(0);
        if ("CallExpression".equals(simpleName(firstArgument))) {
            Object nestedCallee = invokeByName(firstArgument, "callee");
            if ("MemberExpression".equals(simpleName(nestedCallee))) {
                String receiverName = extractIdentifierName(
                        invokeByName(nestedCallee, "object"),
                        "CallExpression.callee.object");
                if (javaImportLookup.containsKey(receiverName)
                        || declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                    return lowerConsoleLogJavaCall(firstArgument, javaImportLookup, declarationLookup);
                }
            }
        }

        QinIrExpression value = lowerRuntimeExpression(firstArgument, javaImportLookup, declarationLookup);
        return new LoweredStatement(new QinIrConsoleLogValue(value), null, null, null, null, null);
    }

    private LoweredStatement lowerConsoleLogCall(
            CallExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!isConsoleLogCallee(expressionAst.callee())) {
            throw qjsError("QJS2001", "Only console.log(...) call is supported");
        }

        List<com.slime.ast.Expression> arguments = expressionAst.arguments();
        if (arguments.size() != 1) {
            throw qjsError("QJS2002", "console.log(...) must have exactly one argument");
        }

        com.slime.ast.Expression firstArgument = arguments.get(0);
        if (firstArgument instanceof CallExpression nestedCallExpression
                && nestedCallExpression.callee() instanceof MemberExpression nestedMemberCallee
                && nestedMemberCallee.object() instanceof Identifier objectIdentifier) {
            String receiverName = objectIdentifier.name();
            if (javaImportLookup.containsKey(receiverName)
                    || declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                return lowerConsoleLogJavaCall(nestedCallExpression, javaImportLookup, declarationLookup);
            }
        }

        QinIrExpression value = lowerRuntimeExpression(firstArgument, javaImportLookup, declarationLookup);
        return new LoweredStatement(new QinIrConsoleLogValue(value), null, null, null, null, null);
    }

    private LoweredStatement lowerConsoleLogJavaCall(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw qjsError("QJS2001", "console.log call argument must be member call like Math.random()");
        }

        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");

        String ownerBinaryName = javaImportLookup.get(receiverName);
        if (ownerBinaryName != null) {
            List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst, javaImportLookup);
            return new LoweredStatement(
                    null,
                    null,
                    null,
                    new QinIrConsoleLogJavaStaticCall(receiverName, ownerBinaryName, methodName, arguments),
                    null,
                    null);
        }

        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (declaration instanceof QinIrJavaNewExpression javaNewExpression) {
            List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst, javaImportLookup);
            return new LoweredStatement(
                    null,
                    null,
                    null,
                    null,
                    null,
                    new QinIrConsoleLogJavaInstanceCall(
                            receiverName,
                            javaNewExpression.ownerBinaryName(),
                            methodName,
                            arguments));
        }

        QinIrBuiltinCallExpression builtin =
                lowerBuiltinCallExpression(callExpressionAst, javaImportLookup, declarationLookup);
        return new LoweredStatement(new QinIrConsoleLogValue(builtin), null, null, null, null, null);
    }

    private LoweredStatement lowerConsoleLogJavaCall(
            CallExpression callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(callExpressionAst.callee() instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)
                || !(memberExpression.property() instanceof Identifier propertyIdentifier)) {
            throw qjsError("QJS2001", "console.log call argument must be member call like Math.random()");
        }

        String receiverName = objectIdentifier.name();
        String methodName = propertyIdentifier.name();

        String ownerBinaryName = javaImportLookup.get(receiverName);
        if (ownerBinaryName != null) {
            List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst.arguments(), javaImportLookup);
            return new LoweredStatement(
                    null,
                    null,
                    null,
                    new QinIrConsoleLogJavaStaticCall(receiverName, ownerBinaryName, methodName, arguments),
                    null,
                    null);
        }

        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (declaration instanceof QinIrJavaNewExpression javaNewExpression) {
            List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst.arguments(), javaImportLookup);
            return new LoweredStatement(
                    null,
                    null,
                    null,
                    null,
                    null,
                    new QinIrConsoleLogJavaInstanceCall(
                            receiverName,
                            javaNewExpression.ownerBinaryName(),
                            methodName,
                            arguments));
        }

        QinIrBuiltinCallExpression builtin =
                lowerBuiltinCallExpression(callExpressionAst, javaImportLookup, declarationLookup);
        return new LoweredStatement(new QinIrConsoleLogValue(builtin), null, null, null, null, null);
    }

    private LoweredStatement lowerJavaInstanceMethodStatement(
            Object callExpressionAst,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        if (isNoOpRuntimeShimCall(callExpressionAst)) {
            return new LoweredStatement(null, null, null, null, null, null);
        }
        Object callee = invokeByName(callExpressionAst, "callee");
        if (isDynamicImportCallee(callee)) {
            return new LoweredStatement(null, null, null, null, null, null);
        }
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw qjsError("QJS2001", "Only member call expression statement is supported");
        }

        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");
        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (!(declaration instanceof QinIrJavaNewExpression javaNewExpression)) {
            throw qjsError("QJS2003", "Only Java instance method call statement is supported: " + receiverName);
        }

        List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst, javaImportLookup);
        return new LoweredStatement(
                null,
                null,
                null,
                null,
                new QinIrJavaInstanceMethodCall(
                        receiverName,
                        javaNewExpression.ownerBinaryName(),
                        methodName,
                        arguments),
                null);
    }

    private LoweredStatement lowerJavaInstanceMethodStatement(
            CallExpression callExpressionAst,
            Map<String, QinIrExpression> declarationLookup,
            Map<String, String> javaImportLookup) {
        if (isNoOpRuntimeShimCall(callExpressionAst)) {
            return new LoweredStatement(null, null, null, null, null, null);
        }
        com.slime.ast.Expression callee = callExpressionAst.callee();
        if (isDynamicImportCallee(callee)) {
            return new LoweredStatement(null, null, null, null, null, null);
        }
        if (!(callee instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)
                || !(memberExpression.property() instanceof Identifier propertyIdentifier)) {
            throw qjsError("QJS2001", "Only member call expression statement is supported");
        }

        String receiverName = objectIdentifier.name();
        String methodName = propertyIdentifier.name();
        QinIrExpression declaration = declarationLookup.get(receiverName);
        if (!(declaration instanceof QinIrJavaNewExpression javaNewExpression)) {
            throw qjsError("QJS2003", "Only Java instance method call statement is supported: " + receiverName);
        }

        List<QinIrExpression> arguments = lowerCallArguments(callExpressionAst.arguments(), javaImportLookup);
        return new LoweredStatement(
                null,
                null,
                null,
                null,
                new QinIrJavaInstanceMethodCall(
                        receiverName,
                        javaNewExpression.ownerBinaryName(),
                        methodName,
                        arguments),
                null);
    }

    private boolean isConsoleLogCallee(Object calleeAst) {
        if (!"MemberExpression".equals(simpleName(calleeAst))) {
            return false;
        }
        Object objectAst = invokeByName(calleeAst, "object");
        Object propertyAst = invokeByName(calleeAst, "property");
        if (!"Identifier".equals(simpleName(objectAst)) || !"Identifier".equals(simpleName(propertyAst))) {
            return false;
        }
        String objectName = extractIdentifierName(objectAst, "callee.object");
        String propertyName = extractIdentifierName(propertyAst, "callee.property");
        return "console".equals(objectName) && "log".equals(propertyName);
    }

    private boolean isDynamicImportCallee(Object calleeAst) {
        return "Import".equals(simpleName(calleeAst));
    }

    private boolean isDynamicImportCallee(com.slime.ast.Expression calleeAst) {
        return calleeAst instanceof ImportExpression;
    }

    private boolean isNoOpRuntimeShimCall(Object callExpressionAst) {
        if (!"CallExpression".equals(simpleName(callExpressionAst))) {
            return false;
        }
        Object callee = invokeByName(callExpressionAst, "callee");
        return isRuntimeShimCall(callee);
    }

    private boolean isNoOpRuntimeShimCall(CallExpression callExpressionAst) {
        return isRuntimeShimCall(callExpressionAst.callee());
    }

    private boolean isRuntimeShimCall(Object calleeAst) {
        if (!"Identifier".equals(simpleName(calleeAst))) {
            return false;
        }
        String name = asString(invokeByName(calleeAst, "name"), "CallExpression.callee.name");
        return QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM.equals(name)
                || QinParserRuntimeNames.TOP_LEVEL_AWAIT_SHIM.equals(name);
    }

    private boolean isRuntimeShimCall(com.slime.ast.Expression calleeAst) {
        if (!(calleeAst instanceof Identifier identifier)) {
            return false;
        }
        String name = identifier.name();
        return QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM.equals(name)
                || QinParserRuntimeNames.TOP_LEVEL_AWAIT_SHIM.equals(name);
    }

    private boolean isJavaInstanceMethodCallee(Object calleeAst, Map<String, QinIrExpression> declarationLookup) {
        if (!"MemberExpression".equals(simpleName(calleeAst))) {
            return false;
        }
        Object objectAst = invokeByName(calleeAst, "object");
        if (!"Identifier".equals(simpleName(objectAst))) {
            return false;
        }
        String receiverName = extractIdentifierName(objectAst, "CallExpression.callee.object");
        return declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression;
    }

    private boolean isJavaInstanceMethodCallee(
            com.slime.ast.Expression calleeAst,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(calleeAst instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)) {
            return false;
        }
        return declarationLookup.get(objectIdentifier.name()) instanceof QinIrJavaNewExpression;
    }

    private boolean isImportMetaUrlExpression(Object expressionAst) {
        if (!"MemberExpression".equals(simpleName(expressionAst))) {
            return false;
        }
        Object object = invokeByName(expressionAst, "object");
        Object property = invokeByName(expressionAst, "property");
        if (!isImportMeta(object)) {
            return false;
        }
        if (!"Identifier".equals(simpleName(property))) {
            return false;
        }
        return "url".equals(asString(invokeByName(property, "name"), "MemberExpression.property.name"));
    }

    private boolean isImportMeta(Object astNode) {
        if (!"MetaProperty".equals(simpleName(astNode))) {
            return false;
        }
        Object meta = invokeByName(astNode, "meta");
        Object property = invokeByName(astNode, "property");
        if (!"Identifier".equals(simpleName(meta)) || !"Identifier".equals(simpleName(property))) {
            return false;
        }
        String metaName = asString(invokeByName(meta, "name"), "MetaProperty.meta.name");
        String propertyName = asString(invokeByName(property, "name"), "MetaProperty.property.name");
        return "import".equals(metaName) && "meta".equals(propertyName);
    }

    String extractPropertyKey(Object keyNode) {
        String nodeType = simpleName(keyNode);
        if ("Identifier".equals(nodeType)) {
            return asString(invokeByName(keyNode, "name"), "Identifier.name");
        }
        if ("Literal".equals(nodeType)) {
            Object value = invokeByName(keyNode, "value");
            if (value instanceof String text) {
                return normalizeStringLiteral(text);
            }
            return String.valueOf(value);
        }
        throw new IllegalArgumentException("Unsupported object key node type: " + nodeType);
    }

    String extractPropertyKey(AstNode keyNode) {
        if (keyNode instanceof Identifier identifier) {
            return identifier.name();
        }
        if (keyNode instanceof Literal literal) {
            Object value = literal.value();
            if (value instanceof String text) {
                return normalizeStringLiteral(text);
            }
            return String.valueOf(value);
        }
        throw new IllegalArgumentException(
                "Unsupported object key node type: "
                        + (keyNode == null ? "null" : keyNode.getClass().getSimpleName()));
    }

    static String extractIdentifierNameStatic(Object astNode, String where) {
        String nodeType = simpleName(astNode);
        if (!"Identifier".equals(nodeType)) {
            throw new IllegalArgumentException(where + " must be Identifier, got: " + nodeType);
        }
        return asStringStatic(invokeByName(astNode, "name"), where + ".name");
    }

    private String extractIdentifierName(Object astNode, String where) {
        return extractIdentifierNameStatic(astNode, where);
    }

    private List<QinIrExpression> lowerCallArguments(Object callExpressionAst, Map<String, String> javaImportLookup) {
        List<?> arguments = asList(invokeByName(callExpressionAst, "arguments"), "CallExpression.arguments");
        List<QinIrExpression> lowered = new ArrayList<>();
        for (Object argument : arguments) {
            lowered.add(lowerCallArgument(argument, javaImportLookup));
        }
        return lowered;
    }

    List<QinIrExpression> lowerCallArguments(
            List<? extends com.slime.ast.Expression> arguments,
            Map<String, String> javaImportLookup) {
        List<QinIrExpression> lowered = new ArrayList<>();
        for (com.slime.ast.Expression argument : arguments) {
            lowered.add(lowerCallArgument(argument, javaImportLookup));
        }
        return lowered;
    }

    private QinIrExpression lowerCallArgument(Object expressionAst, Map<String, String> javaImportLookup) {
        QinIrExpression expression = lowerExpression(expressionAst, javaImportLookup);
        if (expression instanceof QinIrNumberLiteral || expression instanceof QinIrStringLiteral) {
            return expression;
        }
        throw qjsError("QJS2002", "Only integer and string call arguments are supported");
    }

    QinIrExpression lowerExpression(Object expressionAst, Map<String, String> javaImportLookup) {
        if (expressionAst instanceof ParenthesizedExpression parenthesizedExpression) {
            return lowerExpression(parenthesizedExpression.expression(), javaImportLookup);
        }
        if (expressionAst instanceof ObjectExpression objectExpression) {
            return lowerObjectLiteral(objectExpression);
        }
        if (expressionAst instanceof ArrayExpression arrayExpression) {
            return lowerArrayLiteral(arrayExpression, javaImportLookup, Map.of(), false);
        }
        if (expressionAst instanceof Literal literal) {
            return lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof Identifier identifier) {
            return lowerIdentifierExpression(identifier);
        }
        if (expressionAst instanceof ThisExpression) {
            return globalThisExpression();
        }
        if (expressionAst instanceof MemberExpression memberExpression) {
            return lowerMemberAccessExpression(memberExpression);
        }
        if (expressionAst instanceof NewExpression newExpression) {
            return lowerJavaNewExpression(newExpression, javaImportLookup);
        }

        String nodeType = simpleName(expressionAst);
        if ("ParenthesizedExpression".equals(nodeType)) {
            return lowerExpression(invokeByName(expressionAst, "expression"), javaImportLookup);
        }
        if ("ObjectExpression".equals(nodeType)) {
            return lowerObjectLiteral(expressionAst);
        }
        if ("ArrayExpression".equals(nodeType)) {
            return lowerArrayLiteral(expressionAst, javaImportLookup, Map.of(), false);
        }
        if ("NewExpression".equals(nodeType)) {
            return lowerJavaNewExpression(expressionAst, javaImportLookup);
        }
        if ("Literal".equals(nodeType)) {
            QinIrExpression loweredRegexLiteral = lowerRegexLiteralOrNull(expressionAst);
            if (loweredRegexLiteral != null) {
                return loweredRegexLiteral;
            }
            Object value = invokeByName(expressionAst, "value");
            if (value == null) {
                return new QinIrNullLiteral();
            }
            if (value instanceof Number number) {
                return new QinIrNumberLiteral(number.doubleValue());
            }
            if (value instanceof String text) {
                return new QinIrStringLiteral(normalizeStringLiteral(text));
            }
            if (value instanceof Boolean boolValue) {
                return new QinIrBooleanLiteral(boolValue);
            }
        }
        if ("Identifier".equals(nodeType)) {
            String name = extractIdentifierName(expressionAst, "Identifier");
            if (isRegexLiteralIdentifier(name)) {
                ParsedRegexLiteral regexLiteral = parseRegexLiteral(name);
                if (regexLiteral != null) {
                    return createRegexLiteralExpression(regexLiteral);
                }
                return new QinIrStringLiteral(name);
            }
            return new QinIrIdentifierReference(name);
        }
        if ("ThisExpression".equals(nodeType)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_global__",
                    List.of(new QinIrStringLiteral("globalThis")));
        }
        if ("MemberExpression".equals(nodeType)) {
            return lowerMemberAccessExpression(expressionAst);
        }
        throw qjsError("QJS2001", "Unsupported expression type: " + nodeType);
    }

    QinIrExpression lowerLiteralExpression(Literal literal) {
        QinIrExpression loweredRegexLiteral = lowerRegexLiteralOrNull(literal);
        if (loweredRegexLiteral != null) {
            return loweredRegexLiteral;
        }
        Object value = literal.value();
        if (value == null) {
            return new QinIrNullLiteral();
        }
        if (value instanceof Number number) {
            return new QinIrNumberLiteral(number.doubleValue());
        }
        if (value instanceof String text) {
            return new QinIrStringLiteral(normalizeStringLiteral(text));
        }
        if (value instanceof Boolean boolValue) {
            return new QinIrBooleanLiteral(boolValue);
        }
        throw qjsError("QJS2001", "Unsupported literal value type: " + value.getClass().getSimpleName());
    }

    private QinIrExpression lowerRegexLiteralOrNull(Object literal) {
        Object regex = invokeByName(literal, "regex");
        if (regex == null) {
            return null;
        }
        Object raw = invokeByName(literal, "raw");
        if (raw instanceof String rawText) {
            ParsedRegexLiteral regexLiteral = parseRegexLiteral(rawText);
            if (regexLiteral != null) {
                return createRegexLiteralExpression(regexLiteral);
            }
        }
        return null;
    }

    QinIrExpression lowerIdentifierExpression(Identifier identifier) {
        String name = identifier.name();
        if (isRegexLiteralIdentifier(name)) {
            ParsedRegexLiteral regexLiteral = parseRegexLiteral(name);
            if (regexLiteral != null) {
                return createRegexLiteralExpression(regexLiteral);
            }
            return new QinIrStringLiteral(name);
        }
        return new QinIrIdentifierReference(name);
    }

    private QinIrExpression globalThisExpression() {
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_global__",
                List.of(new QinIrStringLiteral("globalThis")));
    }

    private QinIrMemberAccessExpression lowerMemberAccessExpression(Object memberExpressionAst) {
        String objectName = extractIdentifierName(invokeByName(memberExpressionAst, "object"), "MemberExpression.object");
        String propertyName = extractMemberPropertyName(invokeByName(memberExpressionAst, "property"));
        return new QinIrMemberAccessExpression(objectName, propertyName);
    }

    private QinIrMemberAccessExpression lowerMemberAccessExpression(MemberExpression memberExpressionAst) {
        if (!(memberExpressionAst.object() instanceof Identifier objectIdentifier)) {
            throw qjsError("QJS2001", "Only identifier-based member access is supported");
        }
        return new QinIrMemberAccessExpression(
                objectIdentifier.name(),
                extractMemberPropertyName(memberExpressionAst.property()));
    }

    private QinIrExpression lowerRuntimeMemberAccessExpression(
            Object memberExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object objectAst = invokeByName(memberExpressionAst, "object");
        Object propertyAst = invokeByName(memberExpressionAst, "property");
        boolean computed = Boolean.TRUE.equals(invokeByName(memberExpressionAst, "computed"));

        if ("Identifier".equals(simpleName(objectAst))) {
            String objectName = extractIdentifierName(objectAst, "MemberExpression.object");
            if (!computed || "Literal".equals(simpleName(propertyAst))) {
                String propertyName = extractMemberPropertyName(propertyAst);
                return new QinIrMemberAccessExpression(objectName, propertyName);
            }
        }

        QinIrExpression targetExpression = lowerRuntimeExpression(objectAst, javaImportLookup, declarationLookup);
        QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                memberExpressionAst,
                propertyAst,
                javaImportLookup,
                declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_member_get__",
                List.of(targetExpression, propertyExpression));
    }

    private QinIrExpression lowerRuntimeMemberAccessExpression(
            MemberExpression memberExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (memberExpressionAst.object() instanceof Identifier objectIdentifier) {
            if (!memberExpressionAst.computed() || memberExpressionAst.property() instanceof Literal) {
                return new QinIrMemberAccessExpression(
                        objectIdentifier.name(),
                        extractMemberPropertyName(memberExpressionAst.property()));
            }
        }

        QinIrExpression targetExpression = lowerRuntimeExpression(
                memberExpressionAst.object(),
                javaImportLookup,
                declarationLookup);
        QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                memberExpressionAst,
                memberExpressionAst.property(),
                javaImportLookup,
                declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_member_get__",
                List.of(targetExpression, propertyExpression));
    }

    private QinIrExpression lowerRuntimeMemberPropertyExpression(
            Object memberExpressionAst,
            Object propertyAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        boolean computed = Boolean.TRUE.equals(invokeByName(memberExpressionAst, "computed"));
        if ("Identifier".equals(simpleName(propertyAst)) && !computed) {
            return new QinIrStringLiteral(extractIdentifierName(propertyAst, "MemberExpression.property"));
        }
        return lowerRuntimeExpression(propertyAst, javaImportLookup, declarationLookup);
    }

    String extractMemberPropertyName(Object propertyNode) {
        String nodeType = simpleName(propertyNode);
        if ("Identifier".equals(nodeType)) {
            return extractIdentifierName(propertyNode, "MemberExpression.property");
        }
        if ("Literal".equals(nodeType)) {
            Object value = invokeByName(propertyNode, "value");
            if (value instanceof String text) {
                return normalizeStringLiteral(text);
            }
            return String.valueOf(value);
        }
        throw new IllegalArgumentException("MemberExpression.property must be Identifier or Literal, got: " + nodeType);
    }

    String extractMemberPropertyName(com.slime.ast.Expression propertyNode) {
        if (propertyNode instanceof Identifier identifier) {
            return identifier.name();
        }
        if (propertyNode instanceof Literal literal) {
            Object value = literal.value();
            if (value instanceof String text) {
                return normalizeStringLiteral(text);
            }
            return String.valueOf(value);
        }
        throw new IllegalArgumentException(
                "MemberExpression.property must be Identifier or Literal, got: "
                        + propertyNode.getClass().getSimpleName());
    }

    QinIrExpression lowerRuntimeExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (expressionAst instanceof ParenthesizedExpression parenthesizedExpression) {
            return lowerRuntimeExpression(parenthesizedExpression.expression(), javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof Literal literal) {
            return lowerLiteralExpression(literal);
        }
        if (expressionAst instanceof ObjectExpression objectExpression) {
            return lowerRuntimeObjectLiteral(objectExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ArrayExpression arrayExpression) {
            return lowerArrayLiteral(arrayExpression, javaImportLookup, declarationLookup, true);
        }
        if (expressionAst instanceof TemplateLiteral templateLiteral) {
            return lowerRuntimeTemplateLiteral(templateLiteral, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof TaggedTemplateExpression taggedTemplateExpression) {
            return lowerRuntimeTaggedTemplateExpression(taggedTemplateExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof MemberExpression memberExpression) {
            if (isImportMetaUrlExpression(memberExpression)) {
                return new QinIrStringLiteral("import.meta.url");
            }
            return lowerRuntimeMemberAccessExpression(memberExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof Identifier identifier) {
            return lowerIdentifierExpression(identifier);
        }
        if (expressionAst instanceof ThisExpression) {
            return globalThisExpression();
        }
        if (expressionAst instanceof CallExpression callExpression) {
            return lowerRuntimeCallExpression(callExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ImportExpression importExpression) {
            QinIrExpression source =
                    lowerRuntimeExpression(importExpression.source(), javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                    List.of(source));
        }
        if (expressionAst instanceof AwaitExpression awaitExpression) {
            QinIrExpression argument =
                    lowerRuntimeExpression(awaitExpression.argument(), javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.TOP_LEVEL_AWAIT_SHIM,
                    List.of(argument));
        }
        if (expressionAst instanceof BinaryExpression binaryExpression) {
            return lowerRuntimeBinaryExpression(binaryExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof LogicalExpression logicalExpression) {
            return lowerRuntimeLogicalExpression(logicalExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof UnaryExpression unaryExpression) {
            return lowerRuntimeUnaryExpression(unaryExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ConditionalExpression conditionalExpression) {
            return lowerRuntimeConditionalExpression(conditionalExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof AssignmentExpression assignmentExpression) {
            return lowerRuntimeAssignmentExpression(assignmentExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof NewExpression newExpression) {
            return lowerRuntimeNewExpression(newExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof FunctionExpression functionExpression) {
            return lowerFunctionLikeOrNull(functionExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ArrowFunctionExpression arrowFunctionExpression) {
            return lowerFunctionLikeOrNull(arrowFunctionExpression, javaImportLookup, declarationLookup);
        }
        if (expressionAst instanceof ClassExpression classExpression) {
            return lowerFunctionLikeOrNull(classExpression, javaImportLookup, declarationLookup);
        }

        String nodeType = simpleName(expressionAst);
        if ("ParenthesizedExpression".equals(nodeType)) {
            return lowerRuntimeExpression(invokeByName(expressionAst, "expression"), javaImportLookup, declarationLookup);
        }
        if ("Literal".equals(nodeType)) {
            return lowerExpression(expressionAst, javaImportLookup);
        }
        if ("ObjectExpression".equals(nodeType)) {
            return lowerRuntimeObjectLiteral(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("ArrayExpression".equals(nodeType)) {
            return lowerArrayLiteral(expressionAst, javaImportLookup, declarationLookup, true);
        }
        if ("TemplateLiteral".equals(nodeType)) {
            return lowerRuntimeTemplateLiteral(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("TaggedTemplateExpression".equals(nodeType)) {
            return lowerRuntimeTaggedTemplateExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("MemberExpression".equals(nodeType) || "OptionalMemberExpression".equals(nodeType)) {
            if (isImportMetaUrlExpression(expressionAst)) {
                return new QinIrStringLiteral("import.meta.url");
            }
            return lowerRuntimeMemberAccessExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("MetaProperty".equals(nodeType) && isImportMeta(expressionAst)) {
            return new QinIrStringLiteral("import.meta");
        }
        if ("Identifier".equals(nodeType)) {
            String name = extractIdentifierName(expressionAst, "Identifier");
            if (isRegexLiteralIdentifier(name)) {
                ParsedRegexLiteral regexLiteral = parseRegexLiteral(name);
                if (regexLiteral != null) {
                    return createRegexLiteralExpression(regexLiteral);
                }
                return new QinIrStringLiteral(name);
            }
            return new QinIrIdentifierReference(name);
        }
        if ("ThisExpression".equals(nodeType)) {
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_global__",
                    List.of(new QinIrStringLiteral("globalThis")));
        }
        if ("ImportExpression".equals(nodeType)) {
            Object sourceAst = invokeByName(expressionAst, "source");
            QinIrExpression source = lowerRuntimeExpression(sourceAst, javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                    List.of(source));
        }
        if ("AwaitExpression".equals(nodeType)) {
            Object argumentAst = invokeByName(expressionAst, "argument");
            QinIrExpression argument = lowerRuntimeExpression(argumentAst, javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.TOP_LEVEL_AWAIT_SHIM,
                    List.of(argument));
        }
        if ("BinaryExpression".equals(nodeType)) {
            return lowerRuntimeBinaryExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("LogicalExpression".equals(nodeType)) {
            return lowerRuntimeLogicalExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("UnaryExpression".equals(nodeType)) {
            return lowerRuntimeUnaryExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("ConditionalExpression".equals(nodeType)) {
            return lowerRuntimeConditionalExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("AssignmentExpression".equals(nodeType)) {
            return lowerRuntimeAssignmentExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("NewExpression".equals(nodeType)) {
            return lowerRuntimeNewExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if ("FunctionExpression".equals(nodeType)
                || "ClassExpression".equals(nodeType)
                || "ArrowFunctionExpression".equals(nodeType)) {
            return lowerFunctionLikeOrNull(expressionAst, nodeType, javaImportLookup, declarationLookup);
        }
        if ("CallExpression".equals(nodeType) || "OptionalCallExpression".equals(nodeType)) {
            if (isNoOpRuntimeShimCall(expressionAst)) {
                return lowerGlobalBuiltinCallExpression(expressionAst, javaImportLookup, declarationLookup);
            }
            Object callee = invokeByName(expressionAst, "callee");
            boolean optionalCall = "OptionalCallExpression".equals(nodeType)
                    || Boolean.TRUE.equals(invokeByName(expressionAst, "optional"));
            if (isDynamicImportCallee(callee)) {
                List<QinIrExpression> args = lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup);
                return new QinIrBuiltinCallExpression(
                        "Global",
                        QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                        args);
            }
            if ("MemberExpression".equals(simpleName(callee))) {
                Object objectAst = invokeByName(callee, "object");
                Object propertyAst = invokeByName(callee, "property");
                if ("Identifier".equals(simpleName(objectAst))) {
                    String receiverName = extractIdentifierName(objectAst, "CallExpression.callee.object");
                    if (javaImportLookup.containsKey(receiverName)
                            && "Identifier".equals(simpleName(propertyAst))) {
                        String methodName = extractIdentifierName(
                                propertyAst,
                                "CallExpression.callee.property");
                        return lowerRuntimeJavaStaticCall(
                                receiverName,
                                methodName,
                                expressionAst,
                                optionalCall,
                                javaImportLookup,
                                declarationLookup);
                    }
                    if (declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                        throw qjsError("QJS2005", "Java instance call must be statement form");
                    }
                    if (declarationLookup.containsKey(receiverName) && "Identifier".equals(simpleName(propertyAst))) {
                        String methodName = extractIdentifierName(
                                propertyAst,
                                "CallExpression.callee.property");
                        List<QinIrExpression> arguments = new ArrayList<>();
                        arguments.add(new QinIrIdentifierReference(receiverName));
                        arguments.add(new QinIrStringLiteral(methodName));
                        arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
                        return new QinIrBuiltinCallExpression(
                                "Global",
                                optionalCall ? "__qin_optional_call_method__" : "__qin_call_method__",
                                arguments);
                    }
                    if (!declarationLookup.containsKey(receiverName) && "Identifier".equals(simpleName(propertyAst))) {
                        String methodName = extractIdentifierName(propertyAst, "CallExpression.callee.property");
                        List<QinIrExpression> runtimeArguments = lowerRuntimeArguments(
                                expressionAst,
                                javaImportLookup,
                                declarationLookup);
                        if (QinBuiltinRegistry.resolve(receiverName, methodName, runtimeArguments.size()).isPresent()) {
                            return new QinIrBuiltinCallExpression(receiverName, methodName, runtimeArguments);
                        }
                        List<QinIrExpression> arguments = new ArrayList<>();
                        arguments.add(new QinIrBuiltinCallExpression(
                                "Global",
                                "__qin_global__",
                                List.of(new QinIrStringLiteral(receiverName))));
                        arguments.add(new QinIrStringLiteral(methodName));
                        arguments.addAll(runtimeArguments);
                        return new QinIrBuiltinCallExpression(
                                "Global",
                                optionalCall ? "__qin_optional_call_method__" : "__qin_call_method__",
                                arguments);
                    }
                }
                QinIrExpression targetExpression =
                        lowerRuntimeExpression(objectAst, javaImportLookup, declarationLookup);
                QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                        callee,
                        propertyAst,
                        javaImportLookup,
                        declarationLookup);
                List<QinIrExpression> arguments = new ArrayList<>();
                arguments.add(targetExpression);
                arguments.add(propertyExpression);
                arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
                return new QinIrBuiltinCallExpression(
                        "Global",
                        optionalCall ? "__qin_optional_call_method__" : "__qin_call_method__",
                        arguments);
            }
            if ("Identifier".equals(simpleName(callee))) {
                String calleeName = extractIdentifierName(callee, "CallExpression.callee");
                List<QinIrExpression> runtimeArguments = lowerRuntimeArgumentsForCallee(
                        calleeName,
                        expressionAst,
                        javaImportLookup,
                        declarationLookup);
                if (declarationLookup.containsKey(calleeName)) {
                    List<QinIrExpression> arguments = new ArrayList<>();
                    arguments.add(new QinIrIdentifierReference(calleeName));
                    arguments.addAll(runtimeArguments);
                    return new QinIrBuiltinCallExpression(
                            "Global",
                            QinParserRuntimeNames.FUNCTION_CALL_SHIM,
                            arguments);
                }
                if (QinBuiltinRegistry.resolve("Global", calleeName, runtimeArguments.size()).isPresent()) {
                    return new QinIrBuiltinCallExpression("Global", calleeName, runtimeArguments);
                }
                List<QinIrExpression> arguments = new ArrayList<>();
                arguments.add(new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_global__",
                        List.of(new QinIrStringLiteral(calleeName))));
                arguments.addAll(runtimeArguments);
                return new QinIrBuiltinCallExpression(
                        "Global",
                        QinParserRuntimeNames.FUNCTION_CALL_SHIM,
                        arguments);
            }
            QinIrExpression calleeExpression = lowerRuntimeExpression(callee, javaImportLookup, declarationLookup);
            List<QinIrExpression> arguments = new ArrayList<>();
            arguments.add(calleeExpression);
            arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.FUNCTION_CALL_SHIM,
                    arguments);
        }
        throw qjsError("QJS2001", "Unsupported runtime expression type: " + nodeType);
    }

    private QinIrExpression lowerRuntimeTemplateLiteral(
            TemplateLiteral templateLiteral,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> parts = new ArrayList<>();
        List<TemplateElement> quasis = templateLiteral.quasis();
        List<Expression> expressions = templateLiteral.expressions();
        for (int i = 0; i < quasis.size(); i++) {
            parts.add(new QinIrStringLiteral(resolveTemplateElementText(quasis.get(i))));
            if (i < expressions.size()) {
                parts.add(lowerRuntimeExpression(expressions.get(i), javaImportLookup, declarationLookup));
            }
        }
        return buildRuntimeTemplateConcat(parts);
    }

    private QinIrExpression lowerRuntimeTemplateLiteral(
            Object templateLiteralAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<?> quasis = asList(invokeByName(templateLiteralAst, "quasis"), "TemplateLiteral.quasis");
        List<?> expressions = asList(invokeByName(templateLiteralAst, "expressions"), "TemplateLiteral.expressions");
        List<QinIrExpression> parts = new ArrayList<>();
        for (int i = 0; i < quasis.size(); i++) {
            parts.add(new QinIrStringLiteral(resolveTemplateElementText(quasis.get(i))));
            if (i < expressions.size()) {
                parts.add(lowerRuntimeExpression(expressions.get(i), javaImportLookup, declarationLookup));
            }
        }
        return buildRuntimeTemplateConcat(parts);
    }

    private QinIrExpression lowerRuntimeTaggedTemplateExpression(
            TaggedTemplateExpression taggedTemplateExpression,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (isStringRawTag(taggedTemplateExpression.tag())) {
            return lowerRuntimeRawTemplateLiteral(taggedTemplateExpression.quasi(), javaImportLookup, declarationLookup);
        }
        return lowerRuntimeExpressionViaFunctionModel(
                taggedTemplateExpression,
                "TaggedTemplateExpression",
                javaImportLookup,
                declarationLookup);
    }

    private QinIrExpression lowerRuntimeTaggedTemplateExpression(
            Object taggedTemplateExpression,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object tag = invokeByName(taggedTemplateExpression, "tag");
        if (isStringRawTag(tag)) {
            Object quasi = invokeByName(taggedTemplateExpression, "quasi");
            return lowerRuntimeRawTemplateLiteral(quasi, javaImportLookup, declarationLookup);
        }
        return lowerRuntimeExpressionViaFunctionModel(
                taggedTemplateExpression,
                "TaggedTemplateExpression",
                javaImportLookup,
                declarationLookup);
    }

    private QinIrExpression lowerRuntimeExpressionViaFunctionModel(
            Object expressionAst,
            String debugNodeName,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        LinkedHashMap<String, Object> wrapperAst = new LinkedHashMap<>();
        wrapperAst.put("type", "ArrowFunctionExpression");
        wrapperAst.put("id", null);
        wrapperAst.put("params", List.of());
        wrapperAst.put("body", expressionAst);
        wrapperAst.put("async", false);
        wrapperAst.put("expression", true);
        QinIrObjectLiteral runtimeDefinition = lowerRequiredFunctionRuntimeDefinition(
                wrapperAst,
                debugNodeName,
                javaImportLookup,
                declarationLookup);
        QinIrExpression functionValue = new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                List.of(runtimeDefinition));
        return new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_CALL_SHIM,
                List.of(functionValue));
    }

    private QinIrExpression lowerRuntimeRawTemplateLiteral(
            TemplateLiteral templateLiteral,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> parts = new ArrayList<>();
        List<TemplateElement> quasis = templateLiteral.quasis();
        List<Expression> expressions = templateLiteral.expressions();
        for (int i = 0; i < quasis.size(); i++) {
            parts.add(new QinIrStringLiteral(resolveTemplateElementRawText(quasis.get(i))));
            if (i < expressions.size()) {
                parts.add(lowerRuntimeExpression(expressions.get(i), javaImportLookup, declarationLookup));
            }
        }
        return buildRuntimeTemplateConcat(parts);
    }

    private QinIrExpression lowerRuntimeRawTemplateLiteral(
            Object templateLiteralAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<?> quasis = asList(invokeByName(templateLiteralAst, "quasis"), "TemplateLiteral.quasis");
        List<?> expressions = asList(invokeByName(templateLiteralAst, "expressions"), "TemplateLiteral.expressions");
        List<QinIrExpression> parts = new ArrayList<>();
        for (int i = 0; i < quasis.size(); i++) {
            parts.add(new QinIrStringLiteral(resolveTemplateElementRawText(quasis.get(i))));
            if (i < expressions.size()) {
                parts.add(lowerRuntimeExpression(expressions.get(i), javaImportLookup, declarationLookup));
            }
        }
        return buildRuntimeTemplateConcat(parts);
    }

    private QinIrExpression buildRuntimeTemplateConcat(List<QinIrExpression> parts) {
        if (parts.isEmpty()) {
            return new QinIrStringLiteral("");
        }
        QinIrExpression current = parts.get(0);
        for (int i = 1; i < parts.size(); i++) {
            current = new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_binary__",
                    List.of(new QinIrStringLiteral("+"), current, parts.get(i)));
        }
        return current;
    }

    private String resolveTemplateElementText(TemplateElement element) {
        if (element == null) {
            return "";
        }
        if (element.cooked() != null) {
            return element.cooked();
        }
        if (element.raw() != null) {
            return element.raw();
        }
        return "";
    }

    private String resolveTemplateElementRawText(TemplateElement element) {
        if (element == null || element.raw() == null) {
            return "";
        }
        return element.raw();
    }

    private String resolveTemplateElementRawText(Object element) {
        if (element == null) {
            return "";
        }
        Object raw = invokeByName(element, "raw");
        if (raw != null) {
            return String.valueOf(raw);
        }
        return "";
    }

    private boolean isStringRawTag(Object tag) {
        if (tag instanceof MemberExpression memberExpression) {
            return isIdentifierNamed(memberExpression.object(), "String")
                    && isIdentifierNamed(memberExpression.property(), "raw");
        }
        if ("MemberExpression".equals(simpleName(tag))) {
            Object object = invokeByName(tag, "object");
            Object property = invokeByName(tag, "property");
            return isIdentifierNamed(object, "String") && isIdentifierNamed(property, "raw");
        }
        return false;
    }

    private boolean isIdentifierNamed(Object node, String expectedName) {
        if (node instanceof Identifier identifier) {
            return expectedName.equals(identifier.name());
        }
        if ("Identifier".equals(simpleName(node))) {
            return expectedName.equals(extractIdentifierName(node, "Identifier"));
        }
        return false;
    }

    private String resolveTemplateElementText(Object element) {
        if (element == null) {
            return "";
        }
        Object cooked = invokeByName(element, "cooked");
        if (cooked != null) {
            return String.valueOf(cooked);
        }
        Object raw = invokeByName(element, "raw");
        if (raw != null) {
            return String.valueOf(raw);
        }
        return "";
    }

    private QinIrExpression lowerRuntimeCallExpression(
            CallExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        com.slime.ast.Expression callee = expressionAst.callee();
        boolean optionalCall = Boolean.TRUE.equals(invokeByName(expressionAst, "optional"));
        if (isNoOpRuntimeShimCall(expressionAst)) {
            return lowerGlobalBuiltinCallExpression(expressionAst, javaImportLookup, declarationLookup);
        }
        if (isDynamicImportCallee(callee)) {
            List<QinIrExpression> args = lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.DYNAMIC_IMPORT_SHIM,
                    args);
        }
        if (callee instanceof MemberExpression memberCallee) {
            com.slime.ast.Expression objectAst = memberCallee.object();
            com.slime.ast.Expression propertyAst = memberCallee.property();
            if (objectAst instanceof Identifier objectIdentifier) {
                String receiverName = objectIdentifier.name();
                if (javaImportLookup.containsKey(receiverName) && propertyAst instanceof Identifier propertyIdentifier) {
                    return lowerRuntimeJavaStaticCall(
                            receiverName,
                            propertyIdentifier.name(),
                            expressionAst,
                            optionalCall,
                            javaImportLookup,
                            declarationLookup);
                }
                if (declarationLookup.get(receiverName) instanceof QinIrJavaNewExpression) {
                    throw qjsError("QJS2005", "Java instance call must be statement form");
                }
                if (declarationLookup.containsKey(receiverName) && propertyAst instanceof Identifier propertyIdentifier) {
                    String methodName = propertyIdentifier.name();
                    List<QinIrExpression> arguments = new ArrayList<>();
                    arguments.add(new QinIrIdentifierReference(receiverName));
                    arguments.add(new QinIrStringLiteral(methodName));
                    arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
                    return new QinIrBuiltinCallExpression(
                            "Global",
                            optionalCall ? "__qin_optional_call_method__" : "__qin_call_method__",
                            arguments);
                }
                if (!declarationLookup.containsKey(receiverName) && propertyAst instanceof Identifier propertyIdentifier) {
                    String methodName = propertyIdentifier.name();
                    List<QinIrExpression> runtimeArguments = lowerRuntimeArguments(
                            expressionAst,
                            javaImportLookup,
                            declarationLookup);
                    if (QinBuiltinRegistry.resolve(receiverName, methodName, runtimeArguments.size()).isPresent()) {
                        return new QinIrBuiltinCallExpression(receiverName, methodName, runtimeArguments);
                    }
                    List<QinIrExpression> arguments = new ArrayList<>();
                    arguments.add(new QinIrBuiltinCallExpression(
                            "Global",
                            "__qin_global__",
                            List.of(new QinIrStringLiteral(receiverName))));
                    arguments.add(new QinIrStringLiteral(methodName));
                    arguments.addAll(runtimeArguments);
                    return new QinIrBuiltinCallExpression(
                            "Global",
                            optionalCall ? "__qin_optional_call_method__" : "__qin_call_method__",
                            arguments);
                }
            }
            QinIrExpression targetExpression =
                    lowerRuntimeExpression(objectAst, javaImportLookup, declarationLookup);
            QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                    memberCallee,
                    propertyAst,
                    javaImportLookup,
                    declarationLookup);
            List<QinIrExpression> arguments = new ArrayList<>();
            arguments.add(targetExpression);
            arguments.add(propertyExpression);
            arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
            return new QinIrBuiltinCallExpression(
                    "Global",
                    optionalCall ? "__qin_optional_call_method__" : "__qin_call_method__",
                    arguments);
        }
        if (callee instanceof Identifier identifierCallee) {
            String calleeName = identifierCallee.name();
            List<QinIrExpression> runtimeArguments = lowerRuntimeArgumentsForCallee(
                    calleeName,
                    expressionAst,
                    javaImportLookup,
                    declarationLookup);
            if (declarationLookup.containsKey(calleeName)) {
                List<QinIrExpression> arguments = new ArrayList<>();
                arguments.add(new QinIrIdentifierReference(calleeName));
                arguments.addAll(runtimeArguments);
                return new QinIrBuiltinCallExpression(
                        "Global",
                        QinParserRuntimeNames.FUNCTION_CALL_SHIM,
                        arguments);
            }
            if (QinBuiltinRegistry.resolve("Global", calleeName, runtimeArguments.size()).isPresent()) {
                return new QinIrBuiltinCallExpression("Global", calleeName, runtimeArguments);
            }
            List<QinIrExpression> arguments = new ArrayList<>();
            arguments.add(new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_global__",
                    List.of(new QinIrStringLiteral(calleeName))));
            arguments.addAll(runtimeArguments);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.FUNCTION_CALL_SHIM,
                    arguments);
        }
        QinIrExpression calleeExpression = lowerRuntimeExpression(callee, javaImportLookup, declarationLookup);
        List<QinIrExpression> arguments = new ArrayList<>();
        arguments.add(calleeExpression);
        arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
        return new QinIrBuiltinCallExpression(
                "Global",
                QinParserRuntimeNames.FUNCTION_CALL_SHIM,
                arguments);
    }

    private QinIrExpression lowerRuntimeJavaStaticCall(
            String receiverName,
            String methodName,
            Object expressionAst,
            boolean optionalCall,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> arguments = new ArrayList<>();
        arguments.add(new QinIrBuiltinCallExpression(
                "Global",
                "__qin_global__",
                List.of(new QinIrStringLiteral(receiverName))));
        arguments.add(new QinIrStringLiteral(methodName));
        arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
        return new QinIrBuiltinCallExpression(
                "Global",
                optionalCall ? "__qin_optional_call_method__" : "__qin_call_method__",
                arguments);
    }

    private QinIrExpression lowerRuntimeJavaStaticCall(
            String receiverName,
            String methodName,
            CallExpression expressionAst,
            boolean optionalCall,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> arguments = new ArrayList<>();
        arguments.add(new QinIrBuiltinCallExpression(
                "Global",
                "__qin_global__",
                List.of(new QinIrStringLiteral(receiverName))));
        arguments.add(new QinIrStringLiteral(methodName));
        arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
        return new QinIrBuiltinCallExpression(
                "Global",
                optionalCall ? "__qin_optional_call_method__" : "__qin_call_method__",
                arguments);
    }

    private QinIrExpression lowerRuntimeObjectLiteral(
            Object objectExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<?> properties = asList(invokeByName(objectExpressionAst, "properties"), "ObjectExpression.properties");
        List<QinIrExpression> segments = new ArrayList<>();
        List<QinIrObjectProperty> currentProperties = new ArrayList<>();
        for (Object property : properties) {
            if ("SpreadElement".equals(simpleName(property))) {
                flushRuntimeObjectLiteralSegment(segments, currentProperties);
                QinIrExpression spreadValue = lowerRuntimeExpression(
                        invokeByName(property, "argument"),
                        javaImportLookup,
                        declarationLookup);
                validateRuntimeObjectPropertyValue(spreadValue);
                segments.add(spreadValue);
                continue;
            }
            if (!"Property".equals(simpleName(property))) {
                throw qjsError("QJS2002", "Only normal object property is supported, got: " + simpleName(property));
            }
            boolean method = Boolean.TRUE.equals(invokeByName(property, "method"));
            boolean computed = Boolean.TRUE.equals(invokeByName(property, "computed"));
            QinIrExpression value = method
                    ? lowerRequiredFunctionRuntimeDefinition(
                            invokeByName(property, "value"),
                            computed ? "ObjectMethod:[computed]" : "ObjectMethod:" + extractPropertyKey(invokeByName(property, "key")),
                            javaImportLookup,
                            declarationLookup)
                    : lowerRuntimeExpression(invokeByName(property, "value"), javaImportLookup, declarationLookup);
            validateRuntimeObjectPropertyValue(value);
            if (computed) {
                flushRuntimeObjectLiteralSegment(segments, currentProperties);
                QinIrExpression keyExpression =
                        lowerRuntimeExpression(invokeByName(property, "key"), javaImportLookup, declarationLookup);
                validateRuntimeObjectPropertyValue(keyExpression);
                segments.add(new QinIrBuiltinCallExpression(
                        "Object",
                        "fromEntry",
                        List.of(keyExpression, value)));
                continue;
            }
            String key = extractPropertyKey(invokeByName(property, "key"));
            currentProperties.add(new QinIrObjectProperty(key, value));
        }
        flushRuntimeObjectLiteralSegment(segments, currentProperties);
        return buildRuntimeObjectSpreadExpression(segments);
    }

    private QinIrExpression lowerRuntimeObjectLiteral(
            ObjectExpression objectExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> segments = new ArrayList<>();
        List<QinIrObjectProperty> currentProperties = new ArrayList<>();
        for (AstNode propertyNode : objectExpressionAst.properties()) {
            if (propertyNode instanceof SpreadElement spreadElement) {
                flushRuntimeObjectLiteralSegment(segments, currentProperties);
                QinIrExpression spreadValue =
                        lowerRuntimeExpression(spreadElement.argument(), javaImportLookup, declarationLookup);
                validateRuntimeObjectPropertyValue(spreadValue);
                segments.add(spreadValue);
                continue;
            }
            if (!(propertyNode instanceof Property property)) {
                throw qjsError(
                        "QJS2002",
                        "Only normal object property is supported, got: "
                                + propertyNode.getClass().getSimpleName());
            }
            QinIrExpression value = property.method()
                    ? lowerRequiredFunctionRuntimeDefinition(
                            propertyValue(property),
                            property.computed() ? "ObjectMethod:[computed]" : "ObjectMethod:" + extractPropertyKey(property.key()),
                            javaImportLookup,
                            declarationLookup)
                    : lowerRuntimeExpression(propertyValue(property), javaImportLookup, declarationLookup);
            validateRuntimeObjectPropertyValue(value);
            if (property.computed()) {
                flushRuntimeObjectLiteralSegment(segments, currentProperties);
                QinIrExpression keyExpression =
                        lowerRuntimeExpression(property.key(), javaImportLookup, declarationLookup);
                validateRuntimeObjectPropertyValue(keyExpression);
                segments.add(new QinIrBuiltinCallExpression(
                        "Object",
                        "fromEntry",
                        List.of(keyExpression, value)));
                continue;
            }
            String key = extractPropertyKey(property.key());
            currentProperties.add(new QinIrObjectProperty(key, value));
        }
        flushRuntimeObjectLiteralSegment(segments, currentProperties);
        return buildRuntimeObjectSpreadExpression(segments);
    }

    private void validateRuntimeObjectPropertyValue(QinIrExpression value) {
        if (value instanceof QinIrNumberLiteral
                || value instanceof QinIrStringLiteral
                || value instanceof QinIrBooleanLiteral
                || value instanceof QinIrNullLiteral
                || value instanceof QinIrFunctionLiteral
                || value instanceof QinIrIdentifierReference
                || value instanceof QinIrMemberAccessExpression
                || value instanceof QinIrBuiltinCallExpression
                || value instanceof QinIrObjectLiteral
                || value instanceof QinIrArrayLiteral) {
            return;
        }
        throw qjsError("QJS2002", "Unsupported runtime object property value expression");
    }

    private AstNode propertyValue(Property property) {
        return property.value();
    }

    private void flushRuntimeObjectLiteralSegment(
            List<QinIrExpression> segments,
            List<QinIrObjectProperty> currentProperties) {
        if (currentProperties.isEmpty()) {
            return;
        }
        segments.add(new QinIrObjectLiteral(new ArrayList<>(currentProperties)));
        currentProperties.clear();
    }

    private QinIrExpression buildRuntimeObjectSpreadExpression(List<QinIrExpression> segments) {
        if (segments.isEmpty()) {
            return new QinIrObjectLiteral(List.of());
        }
        if (segments.size() == 1 && segments.get(0) instanceof QinIrObjectLiteral objectLiteral) {
            return objectLiteral;
        }
        QinIrExpression merged = new QinIrObjectLiteral(List.of());
        for (QinIrExpression segment : segments) {
            merged = new QinIrBuiltinCallExpression(
                    "Object",
                    "assign",
                    List.of(merged, segment));
        }
        return merged;
    }

    private QinIrExpression lowerRuntimeAssignmentExpression(
            Object assignmentExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = asString(invokeByName(assignmentExpressionAst, "operator"), "AssignmentExpression.operator");

        Object leftAst = invokeByName(assignmentExpressionAst, "left");
        Object rightAst = invokeByName(assignmentExpressionAst, "right");

        if ("Identifier".equals(simpleName(leftAst))) {
            String bindingName = asString(invokeByName(leftAst, "name"), "AssignmentExpression.left.name");
            QinIrExpression currentExpression = new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_global__",
                    List.of(new QinIrStringLiteral(bindingName)));
            QinIrExpression valueExpression = lowerRuntimeAssignmentValue(
                    operator,
                    currentExpression,
                    rightAst,
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_assign__",
                    List.of(new QinIrStringLiteral(bindingName), valueExpression));
        }

        if ("MemberExpression".equals(simpleName(leftAst))) {
            Object targetAst = invokeByName(leftAst, "object");
            Object propertyAst = invokeByName(leftAst, "property");
            QinIrExpression targetExpression = lowerRuntimeExpression(targetAst, javaImportLookup, declarationLookup);
            QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                    leftAst,
                    propertyAst,
                    javaImportLookup,
                    declarationLookup);
            QinIrExpression currentExpression = new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_member_get__",
                    List.of(targetExpression, propertyExpression));
            QinIrExpression valueExpression = lowerRuntimeAssignmentValue(
                    operator,
                    currentExpression,
                    rightAst,
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_member_set__",
                    List.of(targetExpression, propertyExpression, valueExpression));
        }

        throw qjsError("QJS2001", "Only member assignment is supported in expression statement");
    }

    private QinIrExpression lowerRuntimeAssignmentExpression(
            AssignmentExpression assignmentExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = assignmentExpressionAst.operator();

        Object leftAst = assignmentExpressionAst.left();

        if (leftAst instanceof Identifier identifier) {
            QinIrExpression currentExpression = new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_global__",
                    List.of(new QinIrStringLiteral(identifier.name())));
            QinIrExpression valueExpression = lowerRuntimeAssignmentValue(
                    operator,
                    currentExpression,
                    assignmentExpressionAst.right(),
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_assign__",
                    List.of(new QinIrStringLiteral(identifier.name()), valueExpression));
        }

        if (leftAst instanceof MemberExpression memberExpression) {
            QinIrExpression targetExpression =
                    lowerRuntimeExpression(memberExpression.object(), javaImportLookup, declarationLookup);
            QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                    memberExpression,
                    memberExpression.property(),
                    javaImportLookup,
                    declarationLookup);
            QinIrExpression currentExpression = new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_member_get__",
                    List.of(targetExpression, propertyExpression));
            QinIrExpression valueExpression = lowerRuntimeAssignmentValue(
                    operator,
                    currentExpression,
                    assignmentExpressionAst.right(),
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_member_set__",
                    List.of(targetExpression, propertyExpression, valueExpression));
        }

        throw qjsError("QJS2001", "Only member assignment is supported in expression statement");
    }

    private QinIrExpression lowerRuntimeAssignmentValue(
            String operator,
            QinIrExpression currentExpression,
            Object rightAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression rightExpression = lowerRuntimeExpression(rightAst, javaImportLookup, declarationLookup);
        if ("=".equals(operator)) {
            return rightExpression;
        }
        String binaryOperator = switch (operator) {
            case "+=" -> "+";
            case "-=" -> "-";
            case "*=" -> "*";
            case "/=" -> "/";
            case "%=" -> "%";
            case "|=" -> "|";
            case "&=" -> "&";
            case "^=" -> "^";
            case "<<=" -> "<<";
            case ">>=" -> ">>";
            case ">>>=" -> ">>>";
            default -> throw qjsError("QJS2001", "Unsupported assignment operator: " + operator);
        };
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(binaryOperator), currentExpression, rightExpression));
    }

    private QinIrBuiltinCallExpression lowerRuntimeBinaryExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = asString(invokeByName(expressionAst, "operator"), "BinaryExpression.operator");
        Object leftAst = invokeByName(expressionAst, "left");
        Object rightAst = invokeByName(expressionAst, "right");
        QinIrExpression left = lowerRuntimeExpression(leftAst, javaImportLookup, declarationLookup);
        QinIrExpression right = lowerRuntimeExpression(rightAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(operator), left, right));
    }

    private QinIrBuiltinCallExpression lowerRuntimeBinaryExpression(
            BinaryExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression left = lowerRuntimeExpression(expressionAst.left(), javaImportLookup, declarationLookup);
        QinIrExpression right = lowerRuntimeExpression(expressionAst.right(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_binary__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), left, right));
    }

    private QinIrBuiltinCallExpression lowerRuntimeLogicalExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = asString(invokeByName(expressionAst, "operator"), "LogicalExpression.operator");
        Object leftAst = invokeByName(expressionAst, "left");
        Object rightAst = invokeByName(expressionAst, "right");
        QinIrExpression left = lowerRuntimeExpression(leftAst, javaImportLookup, declarationLookup);
        QinIrExpression right = lowerRuntimeExpression(rightAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_logical__",
                List.of(new QinIrStringLiteral(operator), left, right));
    }

    private QinIrBuiltinCallExpression lowerRuntimeLogicalExpression(
            LogicalExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression left = lowerRuntimeExpression(expressionAst.left(), javaImportLookup, declarationLookup);
        QinIrExpression right = lowerRuntimeExpression(expressionAst.right(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_logical__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), left, right));
    }

    private QinIrBuiltinCallExpression lowerRuntimeUnaryExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        String operator = asString(invokeByName(expressionAst, "operator"), "UnaryExpression.operator");
        Object argumentAst = invokeByName(expressionAst, "argument");
        if ("delete".equals(operator)
                && ("MemberExpression".equals(simpleName(argumentAst))
                || "OptionalMemberExpression".equals(simpleName(argumentAst)))) {
            Object targetAst = invokeByName(argumentAst, "object");
            Object propertyAst = invokeByName(argumentAst, "property");
            QinIrExpression targetExpression = lowerRuntimeExpression(targetAst, javaImportLookup, declarationLookup);
            QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                    argumentAst,
                    propertyAst,
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_delete_member__",
                    List.of(targetExpression, propertyExpression));
        }
        QinIrExpression argument = lowerRuntimeExpression(argumentAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_unary__",
                List.of(new QinIrStringLiteral(operator), argument));
    }

    private QinIrBuiltinCallExpression lowerRuntimeUnaryExpression(
            UnaryExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if ("delete".equals(expressionAst.operator()) && expressionAst.argument() instanceof MemberExpression memberExpression) {
            QinIrExpression targetExpression = lowerRuntimeExpression(
                    memberExpression.object(),
                    javaImportLookup,
                    declarationLookup);
            QinIrExpression propertyExpression = lowerRuntimeMemberPropertyExpression(
                    memberExpression,
                    memberExpression.property(),
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_delete_member__",
                    List.of(targetExpression, propertyExpression));
        }
        QinIrExpression argument =
                lowerRuntimeExpression(expressionAst.argument(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_unary__",
                List.of(new QinIrStringLiteral(expressionAst.operator()), argument));
    }

    private QinIrBuiltinCallExpression lowerRuntimeConditionalExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object testAst = invokeByName(expressionAst, "test");
        Object consequentAst = invokeByName(expressionAst, "consequent");
        Object alternateAst = invokeByName(expressionAst, "alternate");
        QinIrExpression test = lowerRuntimeExpression(testAst, javaImportLookup, declarationLookup);
        QinIrExpression consequent = lowerRuntimeExpression(consequentAst, javaImportLookup, declarationLookup);
        QinIrExpression alternate = lowerRuntimeExpression(alternateAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
    }

    private QinIrBuiltinCallExpression lowerRuntimeConditionalExpression(
            ConditionalExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression test = lowerRuntimeExpression(expressionAst.test(), javaImportLookup, declarationLookup);
        QinIrExpression consequent =
                lowerRuntimeExpression(expressionAst.consequent(), javaImportLookup, declarationLookup);
        QinIrExpression alternate =
                lowerRuntimeExpression(expressionAst.alternate(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_conditional__",
                List.of(test, consequent, alternate));
    }

    private QinIrExpression lowerRuntimeNewExpression(
            Object expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object calleeAst = invokeByName(expressionAst, "callee");
        QinIrExpression callee;
        if ("Identifier".equals(simpleName(calleeAst))) {
            String calleeName = extractIdentifierName(calleeAst, "NewExpression.callee");
            if (declarationLookup.containsKey(calleeName)) {
                callee = new QinIrIdentifierReference(calleeName);
            } else if (javaImportLookup.containsKey(calleeName)) {
                return lowerJavaNewExpression(expressionAst, javaImportLookup);
            } else if (isKnownGlobalConstructor(calleeName)) {
                callee = new QinIrStringLiteral(calleeName);
            } else {
                callee = new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_global__",
                        List.of(new QinIrStringLiteral(calleeName)));
            }
        } else {
            callee = lowerRuntimeExpression(calleeAst, javaImportLookup, declarationLookup);
        }
        List<QinIrExpression> arguments = new ArrayList<>();
        arguments.add(callee);
        arguments.addAll(lowerRuntimeArguments(expressionAst, javaImportLookup, declarationLookup));
        return new QinIrBuiltinCallExpression("Global", "__qin_new__", arguments);
    }

    private QinIrExpression lowerRuntimeNewExpression(
            NewExpression expressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        QinIrExpression callee;
        if (expressionAst.callee() instanceof Identifier calleeIdentifier) {
            String calleeName = calleeIdentifier.name();
            if (declarationLookup.containsKey(calleeName)) {
                callee = new QinIrIdentifierReference(calleeName);
            } else if (javaImportLookup.containsKey(calleeName)) {
                return lowerJavaNewExpression(expressionAst, javaImportLookup);
            } else if (isKnownGlobalConstructor(calleeName)) {
                callee = new QinIrStringLiteral(calleeName);
            } else {
                callee = new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_global__",
                        List.of(new QinIrStringLiteral(calleeName)));
            }
        } else {
            callee = lowerRuntimeExpression(expressionAst.callee(), javaImportLookup, declarationLookup);
        }
        List<QinIrExpression> arguments = new ArrayList<>();
        arguments.add(callee);
        arguments.addAll(lowerRuntimeArguments(expressionAst.arguments(), javaImportLookup, declarationLookup));
        return new QinIrBuiltinCallExpression("Global", "__qin_new__", arguments);
    }

    private static boolean isKnownGlobalConstructor(String name) {
        return switch (name) {
            case "Date",
                    "Array",
                    "Object",
                    "Map",
                    "WeakMap",
                    "Set",
                    "WeakSet",
                    "Uint8Array",
                    "String",
                    "Boolean",
                    "Number",
                    "RegExp",
                    "Error",
                    "TypeError",
                    "RangeError" -> true;
            default -> false;
        };
    }

    private QinIrBuiltinCallExpression lowerBuiltinCallExpression(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        if (!"MemberExpression".equals(simpleName(callee))) {
            throw qjsError("QJS2001", "Built-in call must be member call");
        }
        String receiverName = extractIdentifierName(invokeByName(callee, "object"), "CallExpression.callee.object");
        String methodName = extractIdentifierName(invokeByName(callee, "property"), "CallExpression.callee.property");
        List<QinIrExpression> arguments = lowerRuntimeArguments(callExpressionAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(receiverName, methodName, arguments);
    }

    QinIrBuiltinCallExpression lowerBuiltinCallExpression(
            CallExpression callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(callExpressionAst.callee() instanceof MemberExpression memberExpression)
                || !(memberExpression.object() instanceof Identifier objectIdentifier)
                || !(memberExpression.property() instanceof Identifier propertyIdentifier)) {
            throw qjsError("QJS2001", "Built-in call must be member call");
        }
        List<QinIrExpression> arguments =
                lowerRuntimeArguments(callExpressionAst.arguments(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression(
                objectIdentifier.name(),
                propertyIdentifier.name(),
                arguments);
    }

    private QinIrBuiltinCallExpression lowerGlobalBuiltinCallExpression(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        Object callee = invokeByName(callExpressionAst, "callee");
        String methodName = extractIdentifierName(callee, "CallExpression.callee");
        List<QinIrExpression> arguments = lowerRuntimeArguments(callExpressionAst, javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression("Global", methodName, arguments);
    }

    QinIrBuiltinCallExpression lowerGlobalBuiltinCallExpression(
            CallExpression callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if (!(callExpressionAst.callee() instanceof Identifier identifier)) {
            throw qjsError("QJS2001", "Global built-in call must use identifier callee");
        }
        List<QinIrExpression> arguments =
                lowerRuntimeArguments(callExpressionAst.arguments(), javaImportLookup, declarationLookup);
        return new QinIrBuiltinCallExpression("Global", identifier.name(), arguments);
    }

    private List<QinIrExpression> lowerRuntimeArguments(
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<?> arguments = asList(invokeByName(callExpressionAst, "arguments"), "CallExpression.arguments");
        List<QinIrExpression> lowered = new ArrayList<>();
        for (Object argument : arguments) {
            lowered.add(lowerRuntimeExpression(argument, javaImportLookup, declarationLookup));
        }
        return lowered;
    }

    private List<QinIrExpression> lowerRuntimeArgumentsForCallee(
            String calleeName,
            Object callExpressionAst,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<?> arguments = asList(invokeByName(callExpressionAst, "arguments"), "CallExpression.arguments");
        List<QinIrExpression> lowered = new ArrayList<>();
        for (Object argument : arguments) {
            lowered.add(lowerRuntimeArgumentForCallee(calleeName, argument, javaImportLookup, declarationLookup));
        }
        return lowered;
    }

    private QinIrExpression lowerRuntimeArgumentForCallee(
            String calleeName,
            Object argument,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        if ("__qin_java_functional".equals(calleeName) && isRuntimeFunctionLike(argument)) {
            QinIrObjectLiteral runtimeDefinition = lowerRequiredFunctionRuntimeDefinition(
                    argument,
                    "JavaFunctionalArgument",
                    javaImportLookup,
                    declarationLookup);
            return new QinIrBuiltinCallExpression(
                    "Global",
                    QinParserRuntimeNames.FUNCTION_MAKE_SHIM,
                    List.of(runtimeDefinition));
        }
        return lowerRuntimeExpression(argument, javaImportLookup, declarationLookup);
    }

    private boolean isRuntimeFunctionLike(Object value) {
        if (value instanceof FunctionExpression
                || value instanceof ArrowFunctionExpression
                || value instanceof ClassExpression) {
            return true;
        }
        String nodeType = simpleName(value);
        return "FunctionExpression".equals(nodeType)
                || "ArrowFunctionExpression".equals(nodeType)
                || "ClassExpression".equals(nodeType);
    }

    List<QinIrExpression> lowerRuntimeArguments(
            List<? extends com.slime.ast.Expression> arguments,
            Map<String, String> javaImportLookup,
            Map<String, QinIrExpression> declarationLookup) {
        List<QinIrExpression> lowered = new ArrayList<>();
        for (com.slime.ast.Expression argument : arguments) {
            lowered.add(lowerRuntimeExpression(argument, javaImportLookup, declarationLookup));
        }
        return lowered;
    }

    private QinIrJavaNewExpression lowerJavaNewExpression(
            Object newExpressionAst,
            Map<String, String> javaImportLookup) {
        Object callee = invokeByName(newExpressionAst, "callee");
        String classLocalName = extractIdentifierName(callee, "NewExpression.callee");
        String ownerBinaryName = javaImportLookup.get(classLocalName);
        if (ownerBinaryName == null) {
            throw qjsError("QJS2003", "Unknown java class in constructor call: " + classLocalName);
        }
        return new QinIrJavaNewExpression(
                classLocalName,
                ownerBinaryName,
                lowerCallArguments(newExpressionAst, javaImportLookup));
    }

    private QinIrJavaNewExpression lowerJavaNewExpression(
            NewExpression newExpressionAst,
            Map<String, String> javaImportLookup) {
        if (!(newExpressionAst.callee() instanceof Identifier calleeIdentifier)) {
            throw qjsError("QJS2001", "Java constructor callee must be identifier");
        }
        String classLocalName = calleeIdentifier.name();
        String ownerBinaryName = javaImportLookup.get(classLocalName);
        if (ownerBinaryName == null) {
            throw qjsError("QJS2003", "Unknown java class in constructor call: " + classLocalName);
        }
        return new QinIrJavaNewExpression(
                classLocalName,
                ownerBinaryName,
                lowerCallArguments(newExpressionAst.arguments(), javaImportLookup));
    }

    static String simpleName(Object value) {
        if (value == null) {
            return "null";
        }
        return value.getClass().getSimpleName();
    }

    static Object invokeByName(Object target, String methodName, Object... args) {
        try {
            Method method = findMethod(target.getClass(), methodName, args.length);
            return invoke(method, target, args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Failed to invoke " + target.getClass().getName() + "." + methodName, e);
        }
    }

    private static Method findMethod(Class<?> type, String methodName, int parameterCount)
            throws NoSuchMethodException {
        for (Method method : type.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == parameterCount) {
                return method;
            }
        }
        throw new NoSuchMethodException(type.getName() + "." + methodName + "/" + parameterCount);
    }

    private static Object invoke(Method method, Object target, Object... args) throws ReflectiveOperationException {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ReflectiveOperationException reflectiveCause) {
                throw reflectiveCause;
            }
            if (cause instanceof RuntimeException runtimeCause) {
                throw runtimeCause;
            }
            throw e;
        }
    }

    static List<?> asListStatic(Object value, String where) {
        if (value instanceof List<?> list) {
            return list;
        }
        throw new IllegalArgumentException(where + " must be List, got: " + simpleName(value));
    }

    static String asStringStatic(Object value, String where) {
        if (value instanceof String text) {
            return text;
        }
        throw new IllegalArgumentException(where + " must be String, got: " + simpleName(value));
    }

    private static List<?> asList(Object value, String where) {
        return asListStatic(value, where);
    }

    private static String asString(Object value, String where) {
        return asStringStatic(value, where);
    }

    private static String safeMessage(Throwable throwable) {
        if (throwable == null) {
            return "<none>";
        }
        String msg = throwable.getMessage();
        if (msg == null || msg.isBlank()) {
            return throwable.getClass().getSimpleName();
        }
        return msg;
    }

    private boolean isRegexLiteralIdentifier(String name) {
        if (name == null || name.length() < 2 || name.charAt(0) != '/') {
            return false;
        }
        int lastSlash = name.lastIndexOf('/');
        return lastSlash > 0;
    }

    String normalizeStringLiteral(String text) {
        String candidate = text == null ? "" : text;
        if (candidate.length() >= 2) {
            char first = candidate.charAt(0);
            char last = candidate.charAt(candidate.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return unescapeJsString(candidate.substring(1, candidate.length() - 1));
            }
        }
        return candidate;
    }

    private ParsedRegexLiteral parseRegexLiteral(String text) {
        if (text == null) {
            return null;
        }
        String candidate = text.strip();
        if (candidate.length() >= 2) {
            char first = candidate.charAt(0);
            char last = candidate.charAt(candidate.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                candidate = unescapeJsString(candidate.substring(1, candidate.length() - 1));
            }
        }
        if (candidate.length() < 2 || candidate.charAt(0) != '/') {
            return null;
        }
        int endSlash = -1;
        boolean escaping = false;
        for (int i = 1; i < candidate.length(); i++) {
            char ch = candidate.charAt(i);
            if (escaping) {
                escaping = false;
                continue;
            }
            if (ch == '\\') {
                escaping = true;
                continue;
            }
            if (ch == '/') {
                endSlash = i;
            }
        }
        if (endSlash <= 0) {
            return null;
        }
        String pattern = candidate.substring(1, endSlash);
        String flags = candidate.substring(endSlash + 1);
        if (!flags.chars().allMatch(ch -> Character.isLetter(ch))) {
            return null;
        }
        return new ParsedRegexLiteral(pattern, flags);
    }

    private QinIrExpression createRegexLiteralExpression(ParsedRegexLiteral regexLiteral) {
        return new QinIrBuiltinCallExpression(
                "Global",
                "__qin_new__",
                List.of(
                        new QinIrStringLiteral("RegExp"),
                        new QinIrStringLiteral(regexLiteral.pattern()),
                        new QinIrStringLiteral(regexLiteral.flags())));
    }

    private String unescapeJsString(String text) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch != '\\' || i == text.length() - 1) {
                out.append(ch);
                continue;
            }

            char esc = text.charAt(++i);
            switch (esc) {
                case '"' -> out.append('"');
                case '\'' -> out.append('\'');
                case '\\' -> out.append('\\');
                case 'n' -> out.append('\n');
                case 'r' -> out.append('\r');
                case 't' -> out.append('\t');
                case 'b' -> out.append('\b');
                case 'f' -> out.append('\f');
                case 'u' -> {
                    if (i + 4 >= text.length()) {
                        throw qjsError("QJS2002", "Invalid unicode escape in string literal");
                    }
                    String hex = text.substring(i + 1, i + 5);
                    try {
                        out.append((char) Integer.parseInt(hex, 16));
                    } catch (NumberFormatException ex) {
                        throw qjsError("QJS2002", "Invalid unicode escape in string literal");
                    }
                    i += 4;
                }
                default -> out.append(esc);
            }
        }
        return out.toString();
    }

    private IllegalArgumentException qjsError(String code, String message) {
        return new IllegalArgumentException(code + " " + message);
    }

    record LoweredStatement(
            QinIrConsoleLogValue consoleValueLog,
            QinIrExpressionStatement expressionStatement,
            QinIrConsoleLogStatement objectLog,
            QinIrConsoleLogJavaStaticCall javaStaticCall,
            QinIrJavaInstanceMethodCall javaInstanceMethodCall,
            QinIrConsoleLogJavaInstanceCall javaInstanceConsoleLog) {
    }

    record LoweredImports(
            List<QinIrJavaImport> javaImports,
            List<QinIrJsImport> jsImports) {
    }

    private record ParsedRegexLiteral(
            String pattern,
            String flags) {
    }

    private static final class AstJsonEncoder {
        private static final int MAX_DEPTH = 128;
        private static final int MAX_OUTPUT_CHARS = 2_000_000;
        private static final int MAX_COLLECTION_ITEMS = 10_000;
        private static final int MAX_WRAPPED_LIST_ITEMS = 2_000;
        private static final int MAX_TOKEN_SEARCH_SPAN = 512;
        private static final int MAX_REFLECTIVE_FIELDS = 128;
        private static final Pattern REGEX_LITERAL_PATTERN = Pattern.compile("^/(.*)/([a-z]*)$");

        private final IdentityHashMap<Object, Boolean> seen = new IdentityHashMap<>();
        private final StringBuilder out = new StringBuilder();
        private final String sourceText;
        private final int[] lineStartIndexes;

        private AstJsonEncoder(String sourceText) {
            this.sourceText = sourceText == null ? "" : sourceText;
            this.lineStartIndexes = computeLineStartIndexes(this.sourceText);
        }

        private static String toJson(Object value, String sourceText) {
            return new AstJsonEncoder(sourceText).encode(value);
        }

        private String encode(Object value) {
            writeValue(value, 0);
            return out.toString();
        }

        private void writeValue(Object value, int depth) {
            if (out.length() > MAX_OUTPUT_CHARS) {
                out.append("\"<max-output>\"");
                return;
            }
            if (depth > MAX_DEPTH) {
                out.append("\"<max-depth>\"");
                return;
            }
            if (value == null) {
                out.append("null");
                return;
            }
            if (value instanceof String s) {
                writeString(s);
                return;
            }
            if (value instanceof Number || value instanceof Boolean) {
                out.append(value);
                return;
            }
            if (value instanceof Enum<?> e) {
                writeString(e.name());
                return;
            }
            if (value instanceof Class<?> c) {
                writeString(c.getName());
                return;
            }
            if (isRuntimeReflectionObject(value)) {
                writeString("<" + value.getClass().getName() + ">");
                return;
            }
            if (value instanceof Collection<?> collection) {
                writeCollection(collection, depth + 1);
                return;
            }
            if (value instanceof Map<?, ?> map) {
                writeMap(map, depth + 1);
                return;
            }
            if (value.getClass().isArray()) {
                int len = java.lang.reflect.Array.getLength(value);
                out.append('[');
                for (int i = 0; i < len; i++) {
                    if (i > 0) {
                        out.append(',');
                    }
                    writeValue(java.lang.reflect.Array.get(value, i), depth + 1);
                }
                out.append(']');
                return;
            }
            writeObject(value, depth + 1);
        }

        private void writeCollection(Collection<?> collection, int depth) {
            out.append('[');
            boolean first = true;
            int index = 0;
            for (Object item : collection) {
                if (index >= MAX_COLLECTION_ITEMS) {
                    if (!first) {
                        out.append(',');
                    }
                    writeString("<truncated:" + (collection.size() - index) + ">");
                    break;
                }
                if (!first) {
                    out.append(',');
                }
                first = false;
                writeValue(item, depth);
                index++;
            }
            out.append(']');
        }

        private void writeMap(Map<?, ?> map, int depth) {
            out.append('{');
            boolean first = true;
            int index = 0;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (index >= MAX_COLLECTION_ITEMS) {
                    if (!first) {
                        out.append(',');
                    }
                    writeString("<truncated>");
                    out.append(':');
                    writeValue(map.size() - index, depth);
                    break;
                }
                if (!first) {
                    out.append(',');
                }
                first = false;
                writeString(String.valueOf(entry.getKey()));
                out.append(':');
                writeValue(entry.getValue(), depth);
                index++;
            }
            out.append('}');
        }

        private void writeObject(Object value, int depth) {
            if (seen.put(value, Boolean.TRUE) != null) {
                out.append("null");
                return;
            }
            try {
                out.append('{');
                Map<String, Object> fields = extractFields(value);
                boolean first = true;
                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                    if (!first) {
                        out.append(',');
                    }
                    first = false;
                    writeString(entry.getKey());
                    out.append(':');
                    writeValue(entry.getValue(), depth);
                }
                out.append('}');
            } finally {
                seen.remove(value);
            }
        }

        private Map<String, Object> extractFields(Object value) {
            Map<String, Object> special = extractSpecialFields(value);
            if (special != null) {
                return special;
            }

            Class<?> type = value.getClass();
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            if (value instanceof com.slime.ast.AstNode astNode) {
                fields.put("type", toAstTypeName(astNode.type()));
            }
            if (type.isRecord()) {
                RecordComponent[] components = type.getRecordComponents();
                if (components != null) {
                    for (RecordComponent component : components) {
                        String originalName = component.getName();
                        String normalizedName = normalizeFieldName(originalName);
                        try {
                            Object rawFieldValue = component.getAccessor().invoke(value);
                            putFieldIfVisible(fields, normalizedName, normalizeFieldValue(value, originalName, rawFieldValue));
                        } catch (Exception e) {
                            putFieldIfVisible(fields, normalizedName, "<error:" + e.getClass().getSimpleName() + ">");
                        }
                    }
                    return fields;
                }
            }

            Set<String> visitedNames = new java.util.HashSet<>();
            Class<?> current = type;
            int reflectiveFieldCount = 0;
            while (current != null && current != Object.class) {
                java.lang.reflect.Field[] declared = current.getDeclaredFields();
                for (java.lang.reflect.Field field : declared) {
                    if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                        continue;
                    }
                    String originalName = field.getName();
                    String normalizedName = normalizeFieldName(originalName);
                    if (!visitedNames.add(normalizedName)) {
                        continue;
                    }
                    if (!(value instanceof com.slime.ast.AstNode) && reflectiveFieldCount >= MAX_REFLECTIVE_FIELDS) {
                        putFieldIfVisible(fields, "<truncatedFields>", true);
                        return fields;
                    }
                    try {
                        field.setAccessible(true);
                        Object rawFieldValue = field.get(value);
                        putFieldIfVisible(fields, normalizedName, normalizeFieldValue(value, originalName, rawFieldValue));
                    } catch (Exception e) {
                        putFieldIfVisible(fields, normalizedName, "<error:" + e.getClass().getSimpleName() + ">");
                    }
                    reflectiveFieldCount++;
                }
                current = current.getSuperclass();
            }
            return fields;
        }

        private boolean isRuntimeReflectionObject(Object value) {
            String name = value.getClass().getName();
            return name.startsWith("java.lang.reflect.")
                    || name.startsWith("jdk.")
                    || name.startsWith("sun.")
                    || value instanceof ClassLoader
                    || value instanceof Module
                    || value instanceof Package
                    || value instanceof Method
                    || value instanceof java.lang.reflect.Field
                    || value instanceof RecordComponent;
        }

        private void putFieldIfVisible(LinkedHashMap<String, Object> fields, String fieldName, Object value) {
            if (value == null && (fieldName.endsWith("Token") || fieldName.endsWith("Tokens"))) {
                return;
            }
            fields.put(fieldName, value);
        }

        private void putIfNotNull(LinkedHashMap<String, Object> fields, String fieldName, Object value) {
            if (value != null) {
                fields.put(fieldName, value);
            }
        }

        private Map<String, Object> extractSpecialFields(Object value) {
            String simpleName = value.getClass().getSimpleName();

            if ("SourceLocation".equals(simpleName)) {
                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                Object type = readProperty(value, "type");
                Object locValue = readProperty(value, "value");
                Object start = readProperty(value, "start");
                Object end = readProperty(value, "end");
                putLocationFields(fields, type, locValue, start, end);
                return fields;
            }

            if ("Position".equals(simpleName)) {
                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                int index = asInt(readProperty(value, "index"));
                int rawLine = asInt(readProperty(value, "line"));
                int rawColumn = asInt(readProperty(value, "column"));
                fields.put("index", index);
                if (index == 0 && rawLine == 0 && rawColumn == 0) {
                    fields.put("line", 0);
                    fields.put("column", 0);
                } else {
                    PositionInfo positionInfo = resolvePositionInfo(index, rawLine, rawColumn);
                    fields.put("line", positionInfo.line());
                    fields.put("column", positionInfo.column());
                }
                return fields;
            }

            if ("MemberExpression".equals(simpleName)) {
                return extractMemberExpressionFields(value);
            }

            if ("CallExpression".equals(simpleName)) {
                return extractCallExpressionFields(value);
            }

            if ("SequenceExpression".equals(simpleName)) {
                return extractSequenceExpressionFields(value);
            }

            if ("ForOfStatement".equals(simpleName)) {
                return extractForOfStatementFields(value);
            }

            if ("ParenthesizedExpression".equals(simpleName)) {
                return extractParenthesizedExpressionFields(value);
            }

            if ("BlockStatement".equals(simpleName)) {
                return extractBlockStatementFields(value);
            }

            if ("ExpressionStatement".equals(simpleName)) {
                return extractExpressionStatementFields(value);
            }

            if ("VariableDeclaration".equals(simpleName)) {
                return extractVariableDeclarationFields(value);
            }

            if ("VariableDeclarator".equals(simpleName)) {
                return extractVariableDeclaratorFields(value);
            }

            if ("AssignmentExpression".equals(simpleName)) {
                return extractAssignmentExpressionFields(value);
            }

            if ("UnaryExpression".equals(simpleName) || "UpdateExpression".equals(simpleName)) {
                return extractUnaryLikeExpressionFields(value, simpleName);
            }

            if ("BinaryExpression".equals(simpleName) || "LogicalExpression".equals(simpleName)) {
                return extractBinaryLikeExpressionFields(value, simpleName);
            }

            if ("ReturnStatement".equals(simpleName)) {
                return extractReturnStatementFields(value);
            }

            if ("IfStatement".equals(simpleName)) {
                return extractIfStatementFields(value);
            }

            if ("FunctionDeclaration".equals(simpleName)) {
                return extractFunctionDeclarationFields(value);
            }

            if ("NewExpression".equals(simpleName)) {
                return extractNewExpressionFields(value);
            }

        if ("ThrowStatement".equals(simpleName)) {
            return extractThrowStatementFields(value);
        }

        if ("BreakStatement".equals(simpleName)) {
            return extractBreakStatementFields(value);
        }

        if ("ContinueStatement".equals(simpleName)) {
            return extractContinueStatementFields(value);
        }

        if ("ConditionalExpression".equals(simpleName)) {
            return extractConditionalExpressionFields(value);
        }

            if ("ObjectExpression".equals(simpleName)) {
                return extractObjectExpressionFields(value);
            }

            if ("ArrayExpression".equals(simpleName)) {
                return extractArrayExpressionFields(value);
            }

            if ("ObjectPattern".equals(simpleName)) {
                return extractObjectPatternFields(value);
            }

            if ("ArrayPattern".equals(simpleName)) {
                return extractArrayPatternFields(value);
            }

            if ("ForStatement".equals(simpleName)) {
                return extractForStatementFields(value);
            }

            if ("SwitchCase".equals(simpleName)) {
                return extractSwitchCaseFields(value);
            }

            if ("SwitchStatement".equals(simpleName)) {
                return extractSwitchStatementFields(value);
            }

            if ("ImportNamespaceSpecifier".equals(simpleName)) {
                return extractImportNamespaceSpecifierFields(value);
            }

            if ("ExportSpecifier".equals(simpleName)) {
                return extractExportSpecifierFields(value);
            }

            if ("ExportNamedDeclaration".equals(simpleName)) {
                return extractExportNamedDeclarationFields(value);
            }

            if ("ExportDefaultDeclaration".equals(simpleName)) {
                return extractExportDefaultDeclarationFields(value);
            }

            if ("SpreadElement".equals(simpleName)) {
                return extractSpreadElementFields(value);
            }

            if ("Property".equals(simpleName)) {
                return extractPropertyFields(value);
            }

            if ("AssignmentPattern".equals(simpleName)) {
                return extractAssignmentPatternFields(value);
            }

            if ("Literal".equals(simpleName)) {
                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                String raw = asString(readProperty(value, "raw"));
                Object literalValue = readProperty(value, "value");
                fields.put("type", "Literal");
                if (looksLikeRegexLiteral(raw) && literalValue == null) {
                    fields.put("value", new LinkedHashMap<String, Object>());
                    fields.put("raw", raw);
                    fields.put("regex", parseRegexLiteral(raw));
                } else {
                    fields.put("value", literalValue);
                    if (raw != null && literalValue != null && !(literalValue instanceof Boolean)) {
                        fields.put("raw", raw);
                    }
                    Object regex = readProperty(value, "regex");
                    if (regex != null) {
                        fields.put("regex", regex);
                    }
                    Object bigint = readProperty(value, "bigint");
                    if (bigint != null) {
                        fields.put("bigint", bigint);
                    }
                }
                fields.put("loc", firstNonNull(
                        createLiteralLocation(raw, literalValue, value),
                        copyLocation(readProperty(value, "location"), null)));
                return fields;
            }

            if ("TemplateElement".equals(simpleName)) {
                boolean tail = asBoolean(readProperty(value, "tail"));
                LinkedHashMap<String, Object> valueMap = new LinkedHashMap<>();
                valueMap.put("raw", normalizeTemplateChunk(asString(readProperty(value, "raw")), tail));
                valueMap.put("cooked", normalizeTemplateChunk(asString(readProperty(value, "cooked")), tail));

                LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
                fields.put("type", "TemplateElement");
                fields.put("tail", tail);
                fields.put("value", valueMap);
                fields.put("loc", readProperty(value, "location"));
                return fields;
            }

            return null;
        }

        private String normalizeFieldName(String fieldName) {
            if ("location".equals(fieldName)) {
                return "loc";
            }
            return fieldName;
        }

        private Object normalizeFieldValue(Object owner, String originalName, Object value) {
            if (value == null) {
                return null;
            }
            if ("operator".equals(originalName)) {
                Object token = createOperatorToken(owner, value);
                if (token != null) {
                    return token;
                }
            }
            if (value instanceof List<?> list) {
                String wrapperKey = wrapperKey(owner, originalName);
                if (wrapperKey != null) {
                    return wrapListItems(list, wrapperKey);
                }
            }
            return value;
        }

        private Map<String, Object> extractMemberExpressionFields(Object value) {
            Object object = readProperty(value, "object");
            Object property = readProperty(value, "property");
            boolean computed = asBoolean(readProperty(value, "computed"));
            boolean optional = asBoolean(readProperty(value, "optional"));
            Object rawLocation = readProperty(value, "location");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", optional ? "OptionalMemberExpression" : "MemberExpression");
            fields.put("object", object);
            if (!computed && !optional) {
                putIfNotNull(fields, "dot", createMemberDotToken(object, property, value));
            }
            fields.put("property", property);
            fields.put("computed", computed);
            fields.put("optional", optional);
            if (optional) {
                putIfNotNull(fields, "optionalChainingToken", createGapToken("OptionalChaining", "?.", object, property, false));
            }
            if (computed) {
                putIfNotNull(fields, "lBracketToken", firstNonNull(
                        createComputedMemberLBracketToken(property),
                        createGapToken("LBracket", "[", object, property, false)));
                putIfNotNull(fields, "rBracketToken", firstNonNull(
                        createComputedMemberRBracketToken(property),
                        createClosingDelimiterToken("RBracket", "]", property, value)));
            }
            fields.put("loc", optional
                    ? copyLocationWithoutValue(rawLocation, "OptionalChain")
                    : normalizeMemberExpressionLocation(object, rawLocation));
            return fields;
        }

        private Object createMemberDotToken(Object object, Object property, Object owner) {
            Object direct = createGapToken("Dot", ".", object, property, false);
            if (direct != null) {
                return direct;
            }
            int propertyStart = startIndex(property);
            if (propertyStart >= 0) {
                return createTokenBetween("Dot", ".", Math.max(0, propertyStart - 8), propertyStart, true);
            }
            return createGapToken("Dot", ".", object, owner, true);
        }

        private Map<String, Object> extractCallExpressionFields(Object value) {
            Object callee = readProperty(value, "callee");
            List<?> arguments = asList(readProperty(value, "arguments"));
            boolean optional = asBoolean(readProperty(value, "optional"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", optional ? "OptionalCallExpression" : "CallExpression");
            fields.put("callee", callee);
            fields.put("arguments", wrapArgumentListItems(arguments));
            fields.put("optional", optional);
            if (optional) {
                putIfNotNull(fields, "optionalChainingToken", createOptionalCallToken(callee, arguments, value));
            }
            putIfNotNull(fields, "lParenToken", createCallLParenToken(callee, arguments, value));
            putIfNotNull(fields, "rParenToken", createCallRParenToken(arguments, value));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractSequenceExpressionFields(Object value) {
            List<?> expressions = asList(readProperty(value, "expressions"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "SequenceExpression");
            fields.put("expressions", expressions);
            if (expressions.size() > 1) {
                putIfNotNull(fields, "commaTokens", createCommaTokens(expressions));
            }
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractForOfStatementFields(Object value) {
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ForOfStatement");
            fields.put("left", readProperty(value, "left"));
            fields.put("right", readProperty(value, "right"));
            fields.put("body", readProperty(value, "body"));
            if (asBoolean(readProperty(value, "await"))) {
                fields.put("await", true);
            }
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractParenthesizedExpressionFields(Object value) {
            Object expression = readProperty(value, "expression");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ParenthesizedExpression");
            fields.put("expression", expression);
            fields.put("loc", readProperty(value, "location"));
            putIfNotNull(fields, "lParenToken", createLeadingToken("LParen", "(", value, expression));
            putIfNotNull(fields, "rParenToken", createClosingDelimiterToken("RParen", ")", expression, value));
            return fields;
        }

        private Map<String, Object> extractBlockStatementFields(Object value) {
            List<?> body = asList(readProperty(value, "body"));
            Object rawLocation = readProperty(value, "location");
            Object rawType = readProperty(rawLocation, "type");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "BlockStatement");
            fields.put("body", body);
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", value, body, false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", value, body, true));
            fields.put("loc", copyLocation(rawLocation, "BlockStatement".equals(rawType) ? "Block" : null));
            return fields;
        }

        private Map<String, Object> extractExpressionStatementFields(Object value) {
            Object expression = readProperty(value, "expression");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ExpressionStatement");
            fields.put("expression", expression);
            putIfNotNull(fields, "semicolonToken", createTrailingToken("Semicolon", ";", expression, value));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractVariableDeclarationFields(Object value) {
            List<?> declarations = asList(readProperty(value, "declarations"));
            String kind = asString(readProperty(value, "kind"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "VariableDeclaration");
            fields.put("declarations", declarations);
            Object kindToken = createVariableKindToken(kind, value, firstItem(declarations));
            fields.put("kind", kindToken != null ? kindToken : kind);
            fields.put("loc", readProperty(value, "location"));
            putIfNotNull(fields, "semicolonToken", createTokenBetween("Semicolon", ";", startIndex(value), endIndex(value), true));
            return fields;
        }

        private Map<String, Object> extractVariableDeclaratorFields(Object value) {
            Object id = readProperty(value, "id");
            Object init = readProperty(value, "init");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "VariableDeclarator");
            fields.put("id", id);
            if (init != null) {
                putIfNotNull(fields, "eqToken", createGapToken("Assign", "=", id, init, false));
            }
            fields.put("init", init);
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractAssignmentExpressionFields(Object value) {
            Object left = readProperty(value, "left");
            Object right = readProperty(value, "right");
            Object operator = readProperty(value, "operator");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "AssignmentExpression");
            Object operatorToken = createOperatorToken(value, operator);
            fields.put("operator", operatorToken != null ? operatorToken : operator);
            fields.put("left", left);
            fields.put("right", right);
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractUnaryLikeExpressionFields(Object value, String type) {
            Object operator = readProperty(value, "operator");
            Object argument = readProperty(value, "argument");
            boolean prefix = asBoolean(readProperty(value, "prefix"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", type);
            Object operatorToken = createOperatorToken(value, operator);
            fields.put("operator", operatorToken != null ? operatorToken : operator);
            if ("UpdateExpression".equals(type)) {
                fields.put("argument", argument);
                fields.put("prefix", prefix);
            } else {
                fields.put("prefix", prefix);
                fields.put("argument", argument);
            }
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractBinaryLikeExpressionFields(Object value, String type) {
            Object left = readProperty(value, "left");
            Object right = readProperty(value, "right");
            Object operator = readProperty(value, "operator");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", type);
            Object operatorToken = createOperatorToken(value, operator);
            fields.put("operator", operatorToken != null ? operatorToken : operator);
            fields.put("left", left);
            fields.put("right", right);
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractReturnStatementFields(Object value) {
            Object argument = readProperty(value, "argument");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ReturnStatement");
            fields.put("argument", argument);
            putIfNotNull(fields, "returnToken", createLeadingKeywordToken("Return", "return", value, argument));
            putIfNotNull(fields, "semicolonToken", createTrailingToken("Semicolon", ";", argument, value));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractIfStatementFields(Object value) {
            Object test = readProperty(value, "test");
            Object consequent = readProperty(value, "consequent");
            Object alternate = readProperty(value, "alternate");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "IfStatement");
            fields.put("test", test);
            fields.put("consequent", consequent);
            fields.put("alternate", alternate);
            Object ifToken = createLeadingKeywordToken("If", "if", value, test);
            putIfNotNull(fields, "ifToken", ifToken);
            if (alternate != null) {
                putIfNotNull(fields, "elseToken", createGapToken("Else", "else", consequent, alternate, true));
            }
            putIfNotNull(fields, "lParenToken", createGapToken("LParen", "(", ifToken, test, false));
            putIfNotNull(fields, "rParenToken", createGapToken("RParen", ")", test, consequent, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractFunctionDeclarationFields(Object value) {
            Object id = readProperty(value, "id");
            List<?> params = asList(readProperty(value, "params"));
            Object body = readProperty(value, "body");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "FunctionDeclaration");
            fields.put("id", id);
            fields.put("params", wrapListItems(params, "param"));
            fields.put("body", body);
            fields.put("generator", asBoolean(readProperty(value, "generator")));
            fields.put("async", asBoolean(readProperty(value, "async")));
            Object functionToken = createLeadingKeywordToken("Function", "function", value, firstNonNull(id, firstItem(params), body));
            putIfNotNull(fields, "functionToken", functionToken);
            putIfNotNull(fields, "lParenToken", createFunctionLParenToken(id, params, body, value));
            putIfNotNull(fields, "rParenToken", createFunctionRParenToken(params, body, value));
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", body, asList(readProperty(body, "body")), false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", body, asList(readProperty(body, "body")), true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractNewExpressionFields(Object value) {
            Object callee = readProperty(value, "callee");
            List<?> arguments = asList(readProperty(value, "arguments"));
            Object rawLocation = readProperty(value, "location");
            Object rawType = readProperty(rawLocation, "type");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "NewExpression");
            fields.put("callee", callee);
            fields.put("arguments", wrapArgumentListItems(arguments));
            putIfNotNull(fields, "newToken", createLeadingKeywordToken("New", "new", value, callee));
            putIfNotNull(fields, "lParenToken", createCallLParenToken(callee, arguments, value));
            putIfNotNull(fields, "rParenToken", createCallRParenToken(arguments, value));
            fields.put("loc", copyLocation(rawLocation, "NewExpression".equals(rawType) ? "MemberExpression" : null));
            return fields;
        }

    private Map<String, Object> extractThrowStatementFields(Object value) {
        Object argument = readProperty(value, "argument");
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("type", "ThrowStatement");
        fields.put("argument", argument);
            putIfNotNull(fields, "throwToken", createLeadingKeywordToken("Throw", "throw", value, argument));
            putIfNotNull(fields, "semicolonToken", createTrailingToken("Semicolon", ";", argument, value));
        fields.put("loc", readProperty(value, "location"));
        return fields;
    }

    private Map<String, Object> extractBreakStatementFields(Object value) {
        Object label = readProperty(value, "label");
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("type", "BreakStatement");
        fields.put("label", label);
        Object semicolonToken = createTokenBetween("Semicolon", ";", startIndex(value), endIndex(value), true);
        Object breakToken = createLeadingKeywordToken("Break", "break", value, firstNonNull(label, semicolonToken, value));
        putIfNotNull(fields, "breakToken", breakToken);
        putIfNotNull(fields, "semicolonToken", semicolonToken);
        fields.put("loc", readProperty(value, "location"));
        return fields;
    }

    private Map<String, Object> extractContinueStatementFields(Object value) {
        Object label = readProperty(value, "label");
        LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
        fields.put("type", "ContinueStatement");
        fields.put("label", label);
        Object semicolonToken = createTokenBetween("Semicolon", ";", startIndex(value), endIndex(value), true);
        Object continueToken = createLeadingKeywordToken("Continue", "continue", value, firstNonNull(label, semicolonToken, value));
        putIfNotNull(fields, "continueToken", continueToken);
        putIfNotNull(fields, "semicolonToken", semicolonToken);
        fields.put("loc", readProperty(value, "location"));
        return fields;
    }

    private Map<String, Object> extractConditionalExpressionFields(Object value) {
        Object test = readProperty(value, "test");
        Object consequent = readProperty(value, "consequent");
        Object alternate = readProperty(value, "alternate");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ConditionalExpression");
            fields.put("test", test);
            fields.put("consequent", consequent);
            fields.put("alternate", alternate);
            putIfNotNull(fields, "questionToken", createGapToken("Question", "?", test, consequent, false));
            putIfNotNull(fields, "colonToken", createGapToken("Colon", ":", consequent, alternate, false));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractObjectExpressionFields(Object value) {
            List<?> properties = asList(readProperty(value, "properties"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ObjectExpression");
            fields.put("properties", wrapListItems(properties, "property"));
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", value, properties, false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", value, properties, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractArrayExpressionFields(Object value) {
            List<?> elements = asList(readProperty(value, "elements"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ArrayExpression");
            fields.put("elements", wrapListItems(elements, "element"));
            putIfNotNull(fields, "lBracketToken", createEnclosingToken("LBracket", "[", value, elements, false));
            putIfNotNull(fields, "rBracketToken", createEnclosingToken("RBracket", "]", value, elements, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractObjectPatternFields(Object value) {
            List<?> properties = asList(readProperty(value, "properties"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ObjectPattern");
            fields.put("properties", wrapListItems(properties, "property"));
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", value, properties, false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", value, properties, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractArrayPatternFields(Object value) {
            List<?> elements = asList(readProperty(value, "elements"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ArrayPattern");
            fields.put("elements", wrapListItems(elements, "element"));
            putIfNotNull(fields, "lBracketToken", createEnclosingToken("LBracket", "[", value, elements, false));
            putIfNotNull(fields, "rBracketToken", createEnclosingToken("RBracket", "]", value, elements, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractForStatementFields(Object value) {
            Object init = readProperty(value, "init");
            Object test = readProperty(value, "test");
            Object update = readProperty(value, "update");
            Object body = readProperty(value, "body");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ForStatement");
            fields.put("init", init);
            fields.put("test", test);
            fields.put("update", update);
            fields.put("body", body);
            Object forToken = createLeadingKeywordToken("For", "for", value, firstNonNull(init, test, update, body));
            putIfNotNull(fields, "forToken", forToken);
            putIfNotNull(fields, "lParenToken", createGapToken("LParen", "(", forToken, firstNonNull(init, test, update, body), false));
            putIfNotNull(fields, "rParenToken", createGapToken("RParen", ")", lastNonNull(update, test, init), body, true));
            putIfNotNull(fields, "semicolon1Token", createHeaderSemicolonToken(init, test, firstNonNull(update, body), value, 1));
            putIfNotNull(fields, "semicolon2Token", createHeaderSemicolonToken(test, firstNonNull(update, body), value, 2));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractSwitchCaseFields(Object value) {
            Object test = readProperty(value, "test");
            List<?> consequent = asList(readProperty(value, "consequent"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "SwitchCase");
            fields.put("test", test);
            fields.put("consequent", consequent);
            Object leadingToken = test != null
                    ? createLeadingKeywordToken("Case", "case", value, test)
                    : createLeadingKeywordToken("Default", "default", value, firstItem(consequent));
            if (test != null) {
                putIfNotNull(fields, "caseToken", leadingToken);
            } else {
                putIfNotNull(fields, "defaultToken", leadingToken);
            }
            putIfNotNull(fields, "colonToken", createGapToken("Colon", ":", test != null ? test : leadingToken, firstItem(consequent), false));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractSwitchStatementFields(Object value) {
            Object discriminant = readProperty(value, "discriminant");
            List<?> cases = asList(readProperty(value, "cases"));
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "SwitchStatement");
            fields.put("discriminant", discriminant);
            fields.put("cases", cases);
            Object switchToken = createLeadingKeywordToken("Switch", "switch", value, discriminant);
            putIfNotNull(fields, "switchToken", switchToken);
            putIfNotNull(fields, "lParenToken", createGapToken("LParen", "(", switchToken, discriminant, false));
            putIfNotNull(fields, "rParenToken", createGapToken("RParen", ")", discriminant, firstItem(cases), true));
            putIfNotNull(fields, "lBraceToken", createEnclosingToken("LBrace", "{", value, cases, false));
            putIfNotNull(fields, "rBraceToken", createEnclosingToken("RBrace", "}", value, cases, true));
            fields.put("loc", readProperty(value, "location"));
            return fields;
        }

        private Map<String, Object> extractImportNamespaceSpecifierFields(Object value) {
            Object local = readProperty(value, "local");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ImportNamespaceSpecifier");
            fields.put("local", local);
            Object asteriskToken = createLeadingToken("Asterisk", "*", value, local);
            putIfNotNull(fields, "asteriskToken", asteriskToken);
            putIfNotNull(fields, "asToken",
                    rewriteTokenLocationType(createGapToken("as", "as", asteriskToken, local, false), "IdentifierName"));
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractExportSpecifierFields(Object value) {
            Object local = readProperty(value, "local");
            Object exported = readProperty(value, "exported");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ExportSpecifier");
            fields.put("local", local);
            fields.put("exported", exported);
            if (!sameNodeSpan(local, exported) || !Objects.equals(readProperty(local, "name"), readProperty(exported, "name"))) {
                putIfNotNull(fields, "asToken",
                        rewriteTokenLocationType(createGapToken("as", "as", local, exported, false), "IdentifierName"));
            }
            fields.put("loc", firstNonNull(
                    createSyntheticLocation("ExportSpecifier", local, exported),
                    copyLocation(readProperty(value, "location"), "ExportSpecifier")));
            return fields;
        }

        private Map<String, Object> extractExportDefaultDeclarationFields(Object value) {
            Object declaration = readProperty(value, "declaration");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ExportDefaultDeclaration");
            fields.put("declaration", declaration);
            Object exportToken = createLeadingKeywordToken("Export", "export", value, declaration);
            putIfNotNull(fields, "exportToken", exportToken);
            putIfNotNull(fields, "defaultToken", createGapToken("Default", "default", exportToken, declaration, false));
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            String declarationType = declaration == null ? null : declaration.getClass().getSimpleName();
            if (!"FunctionDeclaration".equals(declarationType) && !"ClassDeclaration".equals(declarationType)) {
                putIfNotNull(fields, "semicolonToken", createTrailingToken("Semicolon", ";", declaration, value));
            }
            return fields;
        }

        private Map<String, Object> extractExportNamedDeclarationFields(Object value) {
            Object declaration = readProperty(value, "declaration");
            List<?> specifiers = asList(readProperty(value, "specifiers"));
            Object source = readProperty(value, "source");
            String declarationType = declaration == null ? null : declaration.getClass().getSimpleName();
            Object semicolonToken = null;
            if (declaration == null
                    || (!"FunctionDeclaration".equals(declarationType) && !"ClassDeclaration".equals(declarationType))) {
                semicolonToken = createTrailingToken(
                        "Semicolon",
                        ";",
                        firstNonNull(source, lastItem(specifiers), declaration),
                        value);
            }
            Object exportToken = createLeadingKeywordToken(
                    "Export",
                    "export",
                    value,
                    firstNonNull(declaration, firstItem(specifiers), source, semicolonToken, value));
            Object fromToken = null;
            if (source != null) {
                Object fromAnchor = !specifiers.isEmpty() ? lastItem(specifiers) : firstNonNull(declaration, exportToken);
                fromToken = rewriteTokenLocationType(
                        createGapToken("from", "from", fromAnchor, source, false),
                        "IdentifierName");
            }
            boolean hasBraceTokens = declaration == null;
            Object lBraceToken = null;
            Object rBraceToken = null;
            if (hasBraceTokens) {
                if (!specifiers.isEmpty()) {
                    lBraceToken = createGapToken("LBrace", "{", exportToken, firstItem(specifiers), false);
                    rBraceToken = createGapToken("RBrace", "}", lastItem(specifiers), firstNonNull(source, semicolonToken, value), true);
                } else {
                    lBraceToken = createGapToken("LBrace", "{", exportToken, firstNonNull(fromToken, semicolonToken, value), false);
                    rBraceToken = createGapToken("RBrace", "}", firstNonNull(lBraceToken, exportToken), firstNonNull(fromToken, semicolonToken, value), true);
                }
            }
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "ExportNamedDeclaration");
            fields.put("declaration", declaration);
            fields.put("specifiers", wrapListItems(specifiers, "specifier"));
            fields.put("source", source);
            putIfNotNull(fields, "exportToken", exportToken);
            putIfNotNull(fields, "fromToken", fromToken);
            putIfNotNull(fields, "lBraceToken", lBraceToken);
            putIfNotNull(fields, "rBraceToken", rBraceToken);
            putIfNotNull(fields, "semicolonToken", semicolonToken);
            fields.put("loc", copyLocation(readProperty(value, "location"), null));
            return fields;
        }

        private Map<String, Object> extractSpreadElementFields(Object value) {
            Object argument = readProperty(value, "argument");
            Object rawLocation = readProperty(value, "location");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "SpreadElement");
            fields.put("argument", argument);
            Object ellipsisToken = createLeadingToken("Ellipsis", "...", value, argument);
            putIfNotNull(fields, "ellipsisToken", ellipsisToken);
            fields.put("loc", firstNonNull(
                    copyLocation(rawLocation, null),
                    createSyntheticLocation("PropertyDefinition", ellipsisToken, argument)));
            return fields;
        }

        private Map<String, Object> extractPropertyFields(Object value) {
            Object key = readProperty(value, "key");
            Object propertyValue = readProperty(value, "value");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "Property");
            fields.put("key", key);
            fields.put("value", propertyValue);
            fields.put("kind", readProperty(value, "kind"));
            fields.put("method", asBoolean(readProperty(value, "method")));
            fields.put("shorthand", asBoolean(readProperty(value, "shorthand")));
            fields.put("computed", asBoolean(readProperty(value, "computed")));
            fields.put("loc", createZeroLocation("Property"));
            if (!asBoolean(readProperty(value, "shorthand")) && !asBoolean(readProperty(value, "method"))) {
                putIfNotNull(fields, "colonToken", createGapToken("Colon", ":", key, propertyValue, false));
            }
            return fields;
        }

        private Map<String, Object> extractAssignmentPatternFields(Object value) {
            Object left = readProperty(value, "left");
            Object right = readProperty(value, "right");
            LinkedHashMap<String, Object> fields = new LinkedHashMap<>();
            fields.put("type", "AssignmentPattern");
            fields.put("left", left);
            fields.put("right", right);
            putIfNotNull(fields, "equalToken", createGapToken("Assign", "=", left, right, false));
            fields.put("loc", firstNonNull(
                    createSyntheticLocation("SingleNameBinding", left, right),
                    copyLocation(readProperty(value, "location"), "SingleNameBinding")));
            return fields;
        }

        private Object firstNonNull(Object... values) {
            if (values == null) {
                return null;
            }
            for (Object value : values) {
                if (value != null) {
                    return value;
                }
            }
            return null;
        }

        private Object lastNonNull(Object... values) {
            if (values == null) {
                return null;
            }
            for (int index = values.length - 1; index >= 0; index--) {
                if (values[index] != null) {
                    return values[index];
                }
            }
            return null;
        }

        private Object firstItem(List<?> items) {
            return items == null || items.isEmpty() ? null : items.get(0);
        }

        private Object lastItem(List<?> items) {
            return items == null || items.isEmpty() ? null : items.get(items.size() - 1);
        }

        private Object createFunctionLParenToken(Object id, List<?> params, Object body, Object owner) {
            Object anchor = id != null ? id : createLeadingKeywordToken("Function", "function", owner, firstNonNull(firstItem(params), body));
            return createGapToken("LParen", "(", anchor, firstNonNull(firstItem(params), body, owner), false);
        }

        private Object createFunctionRParenToken(List<?> params, Object body, Object owner) {
            Object anchor = lastItem(params);
            if (anchor == null) {
                anchor = createFunctionLParenToken(null, params, body, owner);
            }
            return createGapToken("RParen", ")", anchor, body != null ? body : owner, true);
        }

        private Object createOptionalCallToken(Object callee, List<?> arguments, Object owner) {
            Object firstArgument = firstItem(arguments);
            Object direct = createGapToken("OptionalChaining", "?.", callee, firstNonNull(firstArgument, owner), false);
            if (direct != null) {
                return direct;
            }
            int from = endIndex(callee);
            int to = endIndex(owner);
            if (from < 0 && to < 0) {
                return null;
            }
            if (from < 0) {
                from = Math.max(0, to - 32);
            }
            if (to < 0) {
                to = Math.min(sourceText.length(), from + 32);
            }
            return createTokenBetween("OptionalChaining", "?.", from, to, false);
        }

        private Object createCallLParenToken(Object callee, List<?> arguments, Object owner) {
            Object firstArgument = firstItem(arguments);
            Object direct = firstArgument != null
                    ? createGapToken("LParen", "(", callee, firstArgument, false)
                    : null;
            if (direct != null) {
                return direct;
            }
            if (firstArgument == null) {
                int calleeEnd = contentEndIndex(callee);
                int ownerEnd = contentEndIndex(owner);
                if (calleeEnd >= 0 && ownerEnd >= 0) {
                    Object afterCallee = createTokenBetween("LParen", "(", calleeEnd, ownerEnd, false);
                    if (afterCallee != null) {
                        return afterCallee;
                    }
                }
            }
            int ownerStart = contentStartIndex(owner);
            int beforeArgument = firstArgument != null ? contentStartIndex(firstArgument) : contentEndIndex(owner);
            if (ownerStart >= 0 && beforeArgument >= 0) {
                Object nearestInOwner = createTokenBetween("LParen", "(", ownerStart, beforeArgument, true);
                if (nearestInOwner != null) {
                    return nearestInOwner;
                }
            }
            int from = contentEndIndex(callee);
            int to = contentEndIndex(owner);
            if (from < 0 && to < 0) {
                return null;
            }
            if (from < 0) {
                from = Math.max(0, to - 32);
            }
            if (to < 0) {
                to = Math.min(sourceText.length(), from + 32);
            }
            return createTokenBetween("LParen", "(", from, to, false);
        }

        private Object createCallRParenToken(List<?> arguments, Object owner) {
            Object anchor = lastItem(arguments);
            if (anchor == null) {
                anchor = createCallLParenToken(readProperty(owner, "callee"), arguments, owner);
            }
            return createClosingDelimiterToken("RParen", ")", anchor, owner);
        }

        private Object createClosingDelimiterToken(String type, String tokenValue, Object anchorNode, Object owner) {
            int from = contentEndIndex(anchorNode);
            int to = contentEndIndex(owner);
            if (from < 0 && to < 0) {
                return null;
            }
            if (from < 0) {
                from = Math.max(0, to - 32);
            }
            if (to < 0) {
                to = Math.min(sourceText.length(), from + 32);
            }
            return createTokenBetween(type, tokenValue, from, to, true);
        }

        private Object createHeaderSemicolonToken(Object left, Object right, Object fallback, Object owner, int order) {
            if (order == 1) {
                Object next = right != null ? right : fallback;
                int from = startIndex(left);
                int to = startIndex(next);
                if (from >= 0 && to >= 0) {
                    Object firstHeaderSemicolon = createTokenBetween("Semicolon", ";", from, to, true);
                    if (firstHeaderSemicolon != null) {
                        return firstHeaderSemicolon;
                    }
                }
            }
            Object next = right != null ? right : fallback;
            Object anchor = left != null ? left : createGapToken("LParen", "(", createLeadingKeywordToken("For", "for", owner, next), next != null ? next : owner, false);
            return createGapToken("Semicolon", ";", anchor, next != null ? next : owner, false);
        }

        private Object createHeaderSemicolonToken(Object left, Object right, Object owner, int order) {
            return createGapToken("Semicolon", ";", left != null ? left : owner, right != null ? right : owner, false);
        }

        private Object createLeadingKeywordToken(String type, String tokenValue, Object owner, Object nextNode) {
            return createLeadingToken(type, tokenValue, owner, nextNode);
        }

        private Object createLeadingToken(String type, String tokenValue, Object owner, Object nextNode) {
            int ownerStart = contentStartIndex(owner);
            int nextStart = contentStartIndex(nextNode);
            if (ownerStart < 0 && nextStart < 0) {
                return null;
            }
            int from = ownerStart >= 0 ? ownerStart : Math.max(0, nextStart - 32);
            int to = nextStart >= 0 ? nextStart : Math.min(sourceText.length(), from + 32);
            return createTokenBetween(type, tokenValue, from, to, false);
        }

        private Object createTrailingToken(String type, String tokenValue, Object anchorNode, Object owner) {
            int anchorEnd = contentEndIndex(anchorNode);
            int ownerEnd = contentEndIndex(owner);
            if (anchorEnd < 0 && ownerEnd < 0) {
                return null;
            }
            int from = anchorEnd >= 0 ? anchorEnd : Math.max(0, ownerEnd - 32);
            int to = ownerEnd >= 0 ? Math.min(sourceText.length(), ownerEnd + tokenValue.length() + 8) : Math.min(sourceText.length(), from + 32);
            return createTokenBetween(type, tokenValue, from, to, true);
        }

        private Object createGapToken(String type, String tokenValue, Object leftNode, Object rightNode, boolean preferLast) {
            int from = contentEndIndex(leftNode);
            int to = contentStartIndex(rightNode);
            if (from < 0 && to < 0) {
                return null;
            }
            if (from < 0) {
                from = Math.max(0, to - 32);
            }
            if (to < 0) {
                to = Math.min(sourceText.length(), from + 32);
            }
            return createTokenBetween(type, tokenValue, from, to, preferLast);
        }

        private Object createEnclosingToken(String type, String tokenValue, Object owner, List<?> items, boolean trailing) {
            int ownerStart = contentStartIndex(owner);
            int ownerEnd = contentEndIndex(owner);
            Object first = firstItem(items);
            Object last = lastItem(items);
            Object boundaryToken = createBoundaryToken(type, tokenValue, owner, trailing);
            if (!trailing) {
                if (boundaryToken != null) {
                    return boundaryToken;
                }
                int to = first != null ? contentStartIndex(first) : Math.min(sourceText.length(), ownerStart + 32);
                return createTokenBetween(type, tokenValue, Math.max(0, ownerStart - 32), to, true);
            }
            int from = last != null ? contentEndIndex(last) : Math.max(0, ownerEnd - 32);
            Object trailingToken = createTokenBetween(type, tokenValue, from, Math.min(sourceText.length(), ownerEnd + 32), false);
            if (trailingToken != null) {
                return trailingToken;
            }
            return boundaryToken;
        }

        private Map<String, Object> createTokenBetween(String type, String tokenValue, int from, int to, boolean preferLast) {
            if (sourceText.isEmpty()) {
                return null;
            }
            int normalizedFrom = clampIndex(Math.min(from, to));
            int normalizedTo = clampIndex(Math.max(from, to));
            if (normalizedTo < normalizedFrom) {
                return null;
            }
            if (normalizedTo - normalizedFrom > MAX_TOKEN_SEARCH_SPAN) {
                return null;
            }
            int tokenIndex = preferLast
                    ? findLastTokenIndex(tokenValue, normalizedFrom, normalizedTo)
                    : findTokenIndex(tokenValue, normalizedFrom, normalizedTo);
            if (tokenIndex < 0) {
                return null;
            }
            return createToken(type, tokenValue, tokenIndex, tokenIndex + tokenValue.length());
        }

        private List<Map<String, Object>> createCommaTokens(List<?> items) {
            List<Map<String, Object>> tokens = new ArrayList<>();
            if (items == null) {
                return tokens;
            }
            int limit = Math.min(items.size() - 1, MAX_WRAPPED_LIST_ITEMS);
            for (int index = 0; index < limit; index++) {
                Map<String, Object> commaToken = createCommaToken(items.get(index), items.get(index + 1));
                if (commaToken != null) {
                    tokens.add(commaToken);
                }
            }
            return tokens;
        }

        private Map<String, Object> createCommaToken(Object current, Object next) {
            int from = contentEndIndex(current);
            int to = contentStartIndex(next);
            if (from >= 0 && to >= 0) {
                return createTokenBetween("Comma", ",", from, to, false);
            }
            if (from >= 0) {
                return createTokenBetween("Comma", ",", from, Math.min(sourceText.length(), from + 32), false);
            }
            if (to >= 0) {
                return createTokenBetween("Comma", ",", Math.max(0, to - 32), to, true);
            }
            return null;
        }

        private int findTokenIndex(String tokenValue, int from, int to) {
            if (tokenValue == null || tokenValue.isEmpty() || sourceText.isEmpty()) {
                return -1;
            }
            int start = clampIndex(from);
            int end = clampIndex(to);
            if (end < start) {
                return -1;
            }
            int index = sourceText.indexOf(tokenValue, start);
            if (index < 0) {
                return -1;
            }
            return index + tokenValue.length() <= end ? index : -1;
        }

        private int findLastTokenIndex(String tokenValue, int from, int to) {
            if (tokenValue == null || tokenValue.isEmpty() || sourceText.isEmpty()) {
                return -1;
            }
            int start = clampIndex(from);
            int end = clampIndex(to);
            if (end < start) {
                return -1;
            }
            int searchFrom = Math.max(start, end - tokenValue.length());
            int index = sourceText.lastIndexOf(tokenValue, searchFrom);
            if (index < start) {
                return -1;
            }
            return index + tokenValue.length() <= end ? index : -1;
        }

        private int clampIndex(int index) {
            return Math.max(0, Math.min(index, sourceText.length()));
        }

        private int contentStartIndex(Object value) {
            int direct = startIndex(value);
            return switch (simpleName(value)) {
                case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> {
                    int infixStart = contentStartIndex(readProperty(value, "left"));
                    yield infixStart >= 0 ? infixStart : direct;
                }
                case "ConditionalExpression" -> {
                    int conditionalStart = contentStartIndex(readProperty(value, "test"));
                    yield conditionalStart >= 0 ? conditionalStart : direct;
                }
                case "SequenceExpression" -> {
                    int sequenceStart = contentStartIndex(firstItem(asList(readProperty(value, "expressions"))));
                    yield sequenceStart >= 0 ? sequenceStart : direct;
                }
                case "Property" -> {
                    int propertyStart = firstNonNegative(
                            contentStartIndex(readProperty(value, "key")),
                            contentStartIndex(readProperty(value, "value")));
                    yield propertyStart >= 0 ? propertyStart : direct;
                }
                case "SpreadElement" -> {
                    int argumentStart = contentStartIndex(readProperty(value, "argument"));
                    if (argumentStart >= 3 && sourceMatches(argumentStart - 3, "...")) {
                        yield argumentStart - 3;
                    }
                    yield argumentStart;
                }
                case "AssignmentPattern" -> {
                    int assignmentStart = firstNonNegative(
                            contentStartIndex(readProperty(value, "left")),
                            contentStartIndex(readProperty(value, "right")));
                    yield assignmentStart >= 0 ? assignmentStart : direct;
                }
                case "ChainExpression" -> {
                    int chainStart = contentStartIndex(readProperty(value, "expression"));
                    yield chainStart >= 0 ? chainStart : direct;
                }
                default -> direct;
            };
        }

        private int contentEndIndex(Object value) {
            int direct = endIndex(value);
            return switch (simpleName(value)) {
                case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> {
                    int infixEnd = contentEndIndex(readProperty(value, "right"));
                    yield infixEnd >= 0 ? infixEnd : direct;
                }
                case "ConditionalExpression" -> {
                    int conditionalEnd = contentEndIndex(readProperty(value, "alternate"));
                    yield conditionalEnd >= 0 ? conditionalEnd : direct;
                }
                case "SequenceExpression" -> {
                    int sequenceEnd = contentEndIndex(lastItem(asList(readProperty(value, "expressions"))));
                    yield sequenceEnd >= 0 ? sequenceEnd : direct;
                }
                case "Property" -> {
                    int propertyEnd = firstNonNegative(
                            contentEndIndex(readProperty(value, "value")),
                            contentEndIndex(readProperty(value, "key")));
                    yield propertyEnd >= 0 ? propertyEnd : direct;
                }
                case "SpreadElement" -> {
                    int spreadEnd = contentEndIndex(readProperty(value, "argument"));
                    yield spreadEnd >= 0 ? spreadEnd : direct;
                }
                case "AssignmentPattern" -> {
                    int assignmentEnd = firstNonNegative(
                            contentEndIndex(readProperty(value, "right")),
                            contentEndIndex(readProperty(value, "left")));
                    yield assignmentEnd >= 0 ? assignmentEnd : direct;
                }
                case "CallExpression", "NewExpression" -> {
                    Object callee = readProperty(value, "callee");
                    List<?> arguments = asList(readProperty(value, "arguments"));
                    Object lastArgument = lastItem(arguments);
                    int calleeEnd = contentEndIndex(callee);
                    if (lastArgument != null) {
                        int lastArgumentEnd = contentEndIndex(lastArgument);
                        if (lastArgumentEnd >= 0 && sourceMatches(lastArgumentEnd, ")")) {
                            yield lastArgumentEnd + 1;
                        }
                    } else if (calleeEnd >= 0 && sourceMatches(calleeEnd, "(") && sourceMatches(calleeEnd + 1, ")")) {
                        yield calleeEnd + 2;
                    }
                    if (calleeEnd >= 0) {
                        int closingParen = findTokenIndex(")", calleeEnd, Math.min(sourceText.length(), calleeEnd + 32));
                        if (closingParen >= 0) {
                            yield closingParen + 1;
                        }
                    }
                    yield direct;
                }
                case "MemberExpression" -> {
                    Object property = readProperty(value, "property");
                    boolean computed = asBoolean(readProperty(value, "computed"));
                    int propertyEnd = contentEndIndex(property);
                    if (computed && propertyEnd >= 0 && sourceMatches(propertyEnd, "]")) {
                        yield propertyEnd + 1;
                    }
                    yield propertyEnd >= 0 ? propertyEnd : direct;
                }
                case "ChainExpression" -> contentEndIndex(readProperty(value, "expression"));
                default -> direct;
            };
        }

        private int firstNonNegative(int... candidates) {
            if (candidates == null) {
                return -1;
            }
            for (int candidate : candidates) {
                if (candidate >= 0) {
                    return candidate;
                }
            }
            return -1;
        }

        private int startIndex(Object value) {
            Object location = locationOf(value);
            if (location == null) {
                return -1;
            }
            Object start = readProperty(location, "start");
            return asInt(readProperty(start, "index"));
        }

        private int endIndex(Object value) {
            Object location = locationOf(value);
            if (location == null) {
                return -1;
            }
            Object end = readProperty(location, "end");
            return asInt(readProperty(end, "index"));
        }

        private Object locationOf(Object value) {
            if (value == null) {
                return null;
            }
            if (value instanceof Map<?, ?> map) {
                if (map.containsKey("loc")) {
                    return map.get("loc");
                }
                if (map.containsKey("location")) {
                    return map.get("location");
                }
                return value;
            }
            if ("SourceLocation".equals(value.getClass().getSimpleName())) {
                return value;
            }
            return readProperty(value, "location");
        }

        private Object createComputedMemberLBracketToken(Object property) {
            int propertyStart = contentStartIndex(property);
            if (propertyStart > 0 && sourceMatches(propertyStart - 1, "[")) {
                return createToken("LBracket", "[", propertyStart - 1, propertyStart);
            }
            return null;
        }

        private Object createComputedMemberRBracketToken(Object property) {
            int propertyEnd = contentEndIndex(property);
            if (propertyEnd >= 0 && sourceMatches(propertyEnd, "]")) {
                return createToken("RBracket", "]", propertyEnd, propertyEnd + 1);
            }
            return null;
        }

        private Object createBoundaryToken(String type, String tokenValue, Object owner, boolean trailing) {
            int index = trailing
                    ? contentEndIndex(owner) - tokenValue.length()
                    : contentStartIndex(owner);
            if (index < 0 || !sourceMatches(index, tokenValue)) {
                return null;
            }
            return createToken(type, tokenValue, index, index + tokenValue.length());
        }

        private boolean sourceMatches(int index, String tokenValue) {
            if (tokenValue == null || tokenValue.isEmpty() || sourceText.isEmpty()) {
                return false;
            }
            if (index < 0 || index + tokenValue.length() > sourceText.length()) {
                return false;
            }
            return sourceText.startsWith(tokenValue, index);
        }

        private Map<String, Object> createToken(String type, String tokenValue, int startIndex, int endIndex) {
            LinkedHashMap<String, Object> token = new LinkedHashMap<>();
            token.put("type", type);
            token.put("value", tokenValue);
            token.put("loc", createLocation(type, tokenValue, startIndex, endIndex));
            return token;
        }

        private Map<String, Object> createLocation(String type, String value, int startIndex, int endIndex) {
            LinkedHashMap<String, Object> location = new LinkedHashMap<>();
            if (type != null) {
                location.put("type", type);
            }
            if (value != null) {
                location.put("value", value);
            }
            location.put("start", createPosition(startIndex));
            location.put("end", createPosition(endIndex));
            return location;
        }

        private Object createSyntheticLocation(String type, Object startNode, Object endNode) {
            int start = startIndex(startNode);
            int end = endIndex(endNode);
            if (start < 0 || end < 0 || end < start) {
                return null;
            }
            return createLocation(type, null, start, end);
        }

        private Object createLiteralLocation(String raw, Object literalValue, Object owner) {
            int start = startIndex(owner);
            int end = endIndex(owner);
            if (start < 0 || end < 0 || end < start) {
                return null;
            }
            if (literalValue == null) {
                return createLocation("NullLiteral", "null", start, end);
            }
            if (literalValue == null && raw != null && raw.startsWith("/") && raw.lastIndexOf('/') > 0) {
                return createLocation("RegularExpressionLiteral", raw, start, end);
            }
            if (literalValue instanceof String) {
                return createLocation("StringLiteral", raw, start, end);
            }
            if (literalValue instanceof Number) {
                return createLocation("NumericLiteral", raw, start, end);
            }
            if (literalValue instanceof Boolean) {
                String booleanValue = raw != null ? raw : literalValue.toString();
                return createLocation(Boolean.TRUE.equals(literalValue) ? "True" : "False", booleanValue, start, end);
            }
            return null;
        }

        private Map<String, Object> createPosition(int index) {
            LinkedHashMap<String, Object> position = new LinkedHashMap<>();
            int safeIndex = clampIndex(index);
            PositionInfo positionInfo = resolvePositionInfo(safeIndex, 0, 0);
            position.put("index", safeIndex);
            position.put("line", positionInfo.line());
            position.put("column", positionInfo.column());
            return position;
        }

        private String wrapperKey(Object owner, String originalName) {
            String simpleName = owner.getClass().getSimpleName();
            if ("specifiers".equals(originalName)
                    && ("ImportDeclaration".equals(simpleName) || "ExportNamedDeclaration".equals(simpleName))) {
                return "specifier";
            }
            if ("arguments".equals(originalName)
                    && ("CallExpression".equals(simpleName) || "NewExpression".equals(simpleName))) {
                return "argument";
            }
            if ("params".equals(originalName)
                    && ("FunctionDeclaration".equals(simpleName)
                    || "FunctionExpression".equals(simpleName)
                    || "ArrowFunctionExpression".equals(simpleName))) {
                return "param";
            }
            if ("elements".equals(originalName)
                    && ("ArrayExpression".equals(simpleName) || "ArrayPattern".equals(simpleName))) {
                return "element";
            }
            if ("properties".equals(originalName)
                    && ("ObjectExpression".equals(simpleName) || "ObjectPattern".equals(simpleName))) {
                return "property";
            }
            return null;
        }

        private String toAstTypeName(Object astType) {
            if (astType == null) {
                return "Unknown";
            }
            String rawName = astType instanceof Enum<?> e ? e.name() : String.valueOf(astType);
            String[] parts = rawName.toLowerCase().split("_+");
            StringBuilder out = new StringBuilder();
            for (String part : parts) {
                if (part.isEmpty()) {
                    continue;
                }
                out.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    out.append(part.substring(1));
                }
            }
            return out.length() == 0 ? rawName : out.toString();
        }

        private List<Map<String, Object>> wrapListItems(List<?> list, String key) {
            List<Map<String, Object>> wrapped = new ArrayList<>();
            int limit = Math.min(list.size(), MAX_WRAPPED_LIST_ITEMS);
            for (int index = 0; index < limit; index++) {
                Object item = list.get(index);
                LinkedHashMap<String, Object> itemObject = new LinkedHashMap<>();
                itemObject.put(key, item);
                if (index + 1 < list.size()) {
                    Map<String, Object> commaToken = createCommaToken(item, list.get(index + 1));
                    if (commaToken != null) {
                        itemObject.put("commaToken", commaToken);
                    }
                }
                wrapped.add(itemObject);
            }
            if (list.size() > limit) {
                LinkedHashMap<String, Object> truncated = new LinkedHashMap<>();
                truncated.put(key, "<truncated:" + (list.size() - limit) + ">");
                wrapped.add(truncated);
            }
            return wrapped;
        }

        private List<Object> wrapArgumentListItems(List<?> list) {
            List<Object> wrapped = new ArrayList<>();
            int limit = Math.min(list.size(), MAX_WRAPPED_LIST_ITEMS);
            for (int index = 0; index < limit; index++) {
                Object item = list.get(index);
                if ("SpreadElement".equals(simpleName(item))) {
                    LinkedHashMap<String, Object> spread = new LinkedHashMap<>(extractSpreadElementFields(item));
                    if (index + 1 < list.size()) {
                        Map<String, Object> commaToken = createCommaToken(item, list.get(index + 1));
                        if (commaToken != null) {
                            spread.put("commaToken", commaToken);
                        }
                    }
                    wrapped.add(spread);
                    continue;
                }
                LinkedHashMap<String, Object> itemObject = new LinkedHashMap<>();
                itemObject.put("argument", item);
                if (index + 1 < list.size()) {
                    Map<String, Object> commaToken = createCommaToken(item, list.get(index + 1));
                    if (commaToken != null) {
                        itemObject.put("commaToken", commaToken);
                    }
                }
                wrapped.add(itemObject);
            }
            if (list.size() > limit) {
                wrapped.add("<truncated:" + (list.size() - limit) + ">");
            }
            return wrapped;
        }

        private List<?> asList(Object value) {
            if (value instanceof List<?> list) {
                return list;
            }
            if (value instanceof Collection<?> collection) {
                return new ArrayList<>(collection);
            }
            return List.of();
        }

        private Object readProperty(Object value, String propertyName) {
            if (value instanceof Map<?, ?> map) {
                return map.get(propertyName);
            }
            try {
                Method method = value.getClass().getMethod(propertyName);
                return method.invoke(value);
            } catch (Exception ignored) {
                return null;
            }
        }

        private boolean asBoolean(Object value) {
            return value instanceof Boolean bool && bool;
        }

        private int asInt(Object value) {
            return value instanceof Number number ? number.intValue() : 0;
        }

        private String asString(Object value) {
            return value instanceof String text ? text : null;
        }

        private boolean shouldPreserveNullLocationValue(Object type, Object start, Object end) {
            if (!(type instanceof String textType)) {
                return false;
            }
            if ("ImportSpecifier".equals(textType)
                    || "ImportDefaultSpecifier".equals(textType)
                    || "ImportNamespaceSpecifier".equals(textType)) {
                return true;
            }
            int startIndex = asInt(readProperty(start, "index"));
            int endIndex = asInt(readProperty(end, "index"));
            int startLine = asInt(readProperty(start, "line"));
            int startColumn = asInt(readProperty(start, "column"));
            int endLine = asInt(readProperty(end, "line"));
            int endColumn = asInt(readProperty(end, "column"));
            return startIndex == 0
                    && endIndex == 0
                    && startLine == 0
                    && startColumn == 0
                    && endLine == 0
                    && endColumn == 0;
        }

        private Object createOperatorToken(Object owner, Object rawOperator) {
            String operatorValue = asString(rawOperator);
            if (operatorValue == null || operatorValue.isBlank() || owner == null) {
                return null;
            }
            String simpleName = owner.getClass().getSimpleName();
            return switch (simpleName) {
                case "BinaryExpression", "LogicalExpression", "AssignmentExpression" -> {
                    Object left = readProperty(owner, "left");
                    Object right = readProperty(owner, "right");
                    Object direct = createGapToken(operatorTokenType(operatorValue), operatorValue, left, right, false);
                    if (direct != null) {
                        yield direct;
                    }
                    int ownerStart = contentStartIndex(owner);
                    int rightStart = contentStartIndex(right);
                    if (ownerStart >= 0 && rightStart >= 0) {
                        Object nearestInOwner = createTokenBetween(
                                operatorTokenType(operatorValue),
                                operatorValue,
                                ownerStart,
                                rightStart,
                                true);
                        if (nearestInOwner != null) {
                            yield nearestInOwner;
                        }
                    }
                    yield null;
                }
                case "UnaryExpression" -> {
                    Object argument = readProperty(owner, "argument");
                    yield createLeadingToken(operatorTokenType(operatorValue), operatorValue, owner, argument);
                }
                case "UpdateExpression" -> {
                    Object argument = readProperty(owner, "argument");
                    boolean prefix = asBoolean(readProperty(owner, "prefix"));
                    yield prefix
                            ? createLeadingToken(operatorTokenType(operatorValue), operatorValue, owner, argument)
                            : createTrailingToken(operatorTokenType(operatorValue), operatorValue, argument, owner);
                }
                default -> null;
            };
        }

        private Object createVariableKindToken(String kind, Object owner, Object firstDeclaration) {
            if (kind == null || kind.isBlank()) {
                return null;
            }
            if ("let".equals(kind)) {
                return rewriteTokenLocationType(createLeadingKeywordToken("let", kind, owner, firstDeclaration), "IdentifierName");
            }
            String tokenType = switch (kind) {
                case "const" -> "Const";
                case "var" -> "Var";
                default -> toAstTypeName(kind);
            };
            return createLeadingKeywordToken(tokenType, kind, owner, firstDeclaration);
        }

        private String operatorTokenType(String operatorValue) {
            return switch (operatorValue) {
                case "&&" -> "LogicalAnd";
                case "||" -> "LogicalOr";
                case "??" -> "NullishCoalescing";
                case "===" -> "StrictEqual";
                case "!==" -> "StrictNotEqual";
                case "==" -> "Equal";
                case "!=" -> "NotEqual";
                case "<" -> "Less";
                case ">" -> "Greater";
                case "<=" -> "LessEqual";
                case ">=" -> "GreaterEqual";
                case "instanceof" -> "Instanceof";
                case "in" -> "In";
                case "<<" -> "LeftShift";
                case ">>" -> "RightShift";
                case ">>>" -> "UnsignedRightShift";
                case "+" -> "Plus";
                case "-" -> "Minus";
                case "*" -> "Asterisk";
                case "/" -> "Slash";
                case "%" -> "Modulo";
                case "&" -> "BitwiseAnd";
                case "^" -> "BitwiseXor";
                case "|" -> "BitwiseOr";
                case "**" -> "Exponentiation";
                case "=" -> "Assign";
                case "+=" -> "PlusAssign";
                case "-=" -> "MinusAssign";
                case "*=" -> "MultiplyAssign";
                case "/=" -> "DivideAssign";
                case "%=" -> "ModuloAssign";
                case "<<=" -> "LeftShiftAssign";
                case ">>=" -> "RightShiftAssign";
                case ">>>=" -> "UnsignedRightShiftAssign";
                case "&=" -> "BitwiseAndAssign";
                case "^=" -> "BitwiseXorAssign";
                case "|=" -> "BitwiseOrAssign";
                case "&&=" -> "LogicalAndAssign";
                case "||=" -> "LogicalOrAssign";
                case "??=" -> "NullishCoalescingAssign";
                case "!" -> "LogicalNot";
                case "~" -> "BitwiseNot";
                case "typeof" -> "Typeof";
                case "void" -> "Void";
                case "delete" -> "Delete";
                case "++" -> "Increment";
                case "--" -> "Decrement";
                default -> toAstTypeName(operatorValue);
            };
        }

        private Object normalizeMemberExpressionLocation(Object object, Object rawLocation) {
            Object current = object;
            while (current != null && "MemberExpression".equals(simpleName(current))) {
                Object next = readProperty(current, "object");
                if (next == null) {
                    break;
                }
                current = next;
            }
            Object objectLocation = locationOf(current);
            if (objectLocation != null) {
                return copyLocation(objectLocation, null);
            }
            return copyLocation(rawLocation, null);
        }

        private Object copyLocation(Object rawLocation, String overrideType) {
            if (rawLocation == null) {
                return rawLocation;
            }
            LinkedHashMap<String, Object> location = new LinkedHashMap<>();
            Object currentType = readProperty(rawLocation, "type");
            Object type = overrideType != null ? overrideType : currentType;
            Object start = readProperty(rawLocation, "start");
            Object end = readProperty(rawLocation, "end");
            Object value = readProperty(rawLocation, "value");
            putLocationFields(location, type, value, start, end);
            return location;
        }

        private Object copyLocationWithoutValue(Object rawLocation, String overrideType) {
            if (rawLocation == null) {
                return null;
            }
            LinkedHashMap<String, Object> location = new LinkedHashMap<>();
            Object currentType = readProperty(rawLocation, "type");
            Object type = overrideType != null ? overrideType : currentType;
            Object start = readProperty(rawLocation, "start");
            Object end = readProperty(rawLocation, "end");
            if (type != null) {
                location.put("type", type);
            }
            if (start != null) {
                location.put("start", start);
            }
            if (end != null) {
                location.put("end", end);
            }
            return location;
        }

        private void putLocationFields(
                LinkedHashMap<String, Object> location,
                Object type,
                Object value,
                Object start,
                Object end) {
            boolean preserveNullValue = shouldPreserveNullLocationValue(type, start, end);
            if (preserveNullValue) {
                location.put("value", value);
            }
            if (type != null) {
                location.put("type", type);
            }
            if (!preserveNullValue && value != null) {
                location.put("value", value);
            }
            if (start != null) {
                location.put("start", start);
            }
            if (end != null) {
                location.put("end", end);
            }
        }

        private Object createZeroLocation(String type) {
            LinkedHashMap<String, Object> location = new LinkedHashMap<>();
            putLocationFields(location, type, null, createZeroPosition(), createZeroPosition());
            return location;
        }

        private Map<String, Object> createZeroPosition() {
            LinkedHashMap<String, Object> position = new LinkedHashMap<>();
            position.put("index", 0);
            position.put("line", 0);
            position.put("column", 0);
            return position;
        }

        private Object rewriteTokenLocationType(Object rawToken, String locationType) {
            if (!(rawToken instanceof Map<?, ?> tokenMap)) {
                return rawToken;
            }
            @SuppressWarnings("unchecked")
            LinkedHashMap<String, Object> token = new LinkedHashMap<>((Map<String, Object>) tokenMap);
            Object rawLoc = token.get("loc");
            if (rawLoc instanceof Map<?, ?> locMap) {
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> loc = new LinkedHashMap<>((Map<String, Object>) locMap);
                loc.put("type", locationType);
                token.put("loc", loc);
            }
            return token;
        }

        private boolean sameNodeSpan(Object left, Object right) {
            if (left == null || right == null) {
                return false;
            }
            return startIndex(left) == startIndex(right) && endIndex(left) == endIndex(right);
        }

        private PositionInfo resolvePositionInfo(int index, int fallbackLine, int fallbackColumn) {
            if (index < 0 || sourceText.isEmpty()) {
                return new PositionInfo(fallbackLine, fallbackColumn);
            }
            int safeIndex = Math.min(index, sourceText.length());
            int lineIndex = Arrays.binarySearch(lineStartIndexes, safeIndex);
            if (lineIndex < 0) {
                lineIndex = -lineIndex - 2;
            }
            if (lineIndex < 0) {
                lineIndex = 0;
            }
            return new PositionInfo(lineIndex + 1, safeIndex - lineStartIndexes[lineIndex] + 1);
        }

        private static int[] computeLineStartIndexes(String sourceText) {
            if (sourceText == null || sourceText.isEmpty()) {
                return new int[] { 0 };
            }
            ArrayList<Integer> starts = new ArrayList<>();
            starts.add(0);
            int i = 0;
            while (i < sourceText.length()) {
                char ch = sourceText.charAt(i);
                if (ch == '\r') {
                    if (i + 1 < sourceText.length() && sourceText.charAt(i + 1) == '\n') {
                        i++;
                    }
                    starts.add(i + 1);
                } else if (ch == '\n' || ch == '\u2028' || ch == '\u2029') {
                    starts.add(i + 1);
                }
                i++;
            }
            int[] indexes = new int[starts.size()];
            for (int j = 0; j < starts.size(); j++) {
                indexes[j] = starts.get(j);
            }
            return indexes;
        }

        private record PositionInfo(int line, int column) {
        }

        private Map<String, Object> createToken(String type, String tokenValue) {
            LinkedHashMap<String, Object> token = new LinkedHashMap<>();
            token.put("type", type);
            token.put("value", tokenValue);
            return token;
        }

        private List<Map<String, Object>> createRepeatedTokens(String type, String tokenValue, int count) {
            List<Map<String, Object>> tokens = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                tokens.add(createToken(type, tokenValue));
            }
            return tokens;
        }

        private boolean looksLikeRegexLiteral(String raw) {
            return raw != null && REGEX_LITERAL_PATTERN.matcher(raw).matches();
        }

        private boolean looksLikeQuotedString(String raw) {
            if (raw == null || raw.length() < 2) {
                return false;
            }
            char first = raw.charAt(0);
            char last = raw.charAt(raw.length() - 1);
            return (first == '"' && last == '"') || (first == '\'' && last == '\'');
        }

        private Map<String, Object> parseRegexLiteral(String raw) {
            LinkedHashMap<String, Object> regex = new LinkedHashMap<>();
            Matcher matcher = REGEX_LITERAL_PATTERN.matcher(raw == null ? "" : raw);
            if (matcher.matches()) {
                regex.put("pattern", matcher.group(1));
                regex.put("flags", matcher.group(2));
            }
            return regex;
        }

        private String normalizeTemplateChunk(String text, boolean tail) {
            if (text == null) {
                return null;
            }
            String normalized = text;
            if (!normalized.isEmpty() && (normalized.charAt(0) == '`' || normalized.charAt(0) == '}')) {
                normalized = normalized.substring(1);
            }
            if (tail) {
                if (!normalized.isEmpty() && normalized.charAt(normalized.length() - 1) == '`') {
                    normalized = normalized.substring(0, normalized.length() - 1);
                }
            } else if (normalized.endsWith("${")) {
                normalized = normalized.substring(0, normalized.length() - 2);
            }
            return normalized;
        }

        private void writeString(String text) {
            out.append('"');
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                switch (c) {
                    case '"' -> out.append("\\\"");
                    case '\\' -> out.append("\\\\");
                    case '\b' -> out.append("\\b");
                    case '\f' -> out.append("\\f");
                    case '\n' -> out.append("\\n");
                    case '\r' -> out.append("\\r");
                    case '\t' -> out.append("\\t");
                    default -> {
                        if (c < 0x20) {
                            out.append(String.format("\\u%04x", (int) c));
                        } else {
                            out.append(c);
                        }
                    }
                }
            }
            out.append('"');
        }
    }
}
