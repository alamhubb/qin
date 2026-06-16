package com.qin.lang.backend.js;

import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrCastExpression;
import com.qin.lang.ir.QinIrCatchClause;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrContinueStatement;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrConsoleLogValue;
import com.qin.lang.ir.QinIrDoWhileExpression;
import com.qin.lang.ir.QinIrDoWhileStatementNode;
import com.qin.lang.ir.QinIrElementAccessExpression;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrExpressionStatement;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrForEachExpression;
import com.qin.lang.ir.QinIrForEachStatement;
import com.qin.lang.ir.QinIrForExpression;
import com.qin.lang.ir.QinIrForStatement;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrIfExpression;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaClassLiteralExpression;
import com.qin.lang.ir.QinIrJavaInstanceofPatternExpression;
import com.qin.lang.ir.QinIrJavaMethodReferenceExpression;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrLocalDeclarationStatement;
import com.qin.lang.ir.QinIrLocalDeclarationExpression;
import com.qin.lang.ir.QinIrLocalVariableDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrSpreadArgumentExpression;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrStatement;
import com.qin.lang.ir.QinIrStatementExpression;
import com.qin.lang.ir.QinIrThrowExpression;
import com.qin.lang.ir.QinIrThrowStatement;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrTryStatement;
import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinIrWhileExpression;
import com.qin.lang.ir.QinIrWhileStatementNode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Minimal JavaScript backend for Qin IR.
 */
public class QinJsBackend implements QinIrCodeBackend {
    private static final String JAVA_SDK_JS_PACKAGE = "@qin/java-sdk-js";
    private static final String JAVA_SDK_TOOLING_JS_PACKAGE = "@qin/java-sdk-js/tooling";
    private static final List<String> JAVA_SDK_RUNTIME_EXPORTS = List.of(
            "__qin_builtin_constructor__",
            "__qin_java_pattern_regexp__",
            "__QinJavaLangString",
            "__QinJavaLangBoolean",
            "__QinSlf4jLogger",
            "__QinSlf4jLoggerFactory",
            "__QinJavaLangInteger",
            "__QinJavaLangDouble",
            "__QinJavaLangEnum",
            "__QinJavaLangThrowable",
            "__QinJavaLangException",
            "__QinJavaLangRuntimeException",
            "__QinJavaLangReflectiveOperationException",
            "__QinJavaLangClassNotFoundException",
            "__QinJavaLangNoSuchMethodException",
            "__QinJavaLangError",
            "__QinJavaLangStackOverflowError",
            "__QinJavaLangIllegalArgumentException",
            "__QinJavaLangNumberFormatException",
            "__QinJavaLangIllegalStateException",
            "__QinJavaLangUnsupportedOperationException",
            "__QinJavaIoIOException",
            "__QinJavaLangSystem",
            "__QinJavaLangStringBuilder",
            "__QinJavaIoFile",
            "__QinJavaNioFilePath",
            "__QinJavaNioFilePaths",
            "__QinJavaNioFileFiles",
            "__QinJavaIoFileWriter",
            "__QinJavaIoBufferedWriter",
            "__QinJavaTimeFormatDateTimeFormatter",
            "__QinJavaTimeLocalDateTime",
            "__QinJavaUtilArrayList",
            "__QinJavaUtilUnmodifiableList",
            "__QinJavaUtilList",
            "__QinJavaUtilHashSet",
            "__QinJavaUtilUnmodifiableSet",
            "__QinJavaUtilSet",
            "__qin_java_string_hash_code__",
            "__qin_java_identity_hash_code__",
            "__qin_java_value_hash_code__",
            "__qin_java_values_equal__",
            "__qin_java_hash_key__",
            "__qin_java_hash_key_equals__",
            "__QinJavaUtilArrays",
            "__QinJavaUtilUnmodifiableMap",
            "__QinJavaUtilCollections",
            "__QinJavaUtilHashMap",
            "__QinJavaUtilObjects",
            "__QinJavaUtilOptionalValue",
            "__QinJavaUtilOptional",
            "__QinJavaUtilStream",
            "__QinJavaUtilStreamCollectors",
            "__QinJavaUtilConcurrentAtomicLong",
            "__QinCaffeineRemovalCause",
            "__QinCaffeineCache",
            "__QinCaffeineBuilder",
            "__QinCaffeine",
            "__QinJavaUtilRegexPattern",
            "__QinJavaUtilRegexMatcher",
            "__qin_java_functional",
            "__qin_java_class_info__",
            "__qin_binary__",
            "__qin_logical__",
            "__qin_subhuti_rule_cache_key",
            "__qin_init_enum_value");
    private Set<String> generatedClassBinaryNames = Set.of();
    private Map<String, QinIrClassDeclaration> generatedClassesByBinaryName = Map.of();
    private Map<String, String> generatedClassReferencesBySimpleName = Map.of();
    private Map<String, String> generatedJavaFieldAliases = Map.of();
    private Set<String> externallyBoundJavaBinaryNames = Set.of();
    private boolean externalJavaSdkRuntime;
    private Set<String> externalJavaSdkRuntimeImports = new LinkedHashSet<>();
    private Map<String, String> bindingAliases = new LinkedHashMap<>();
    private Map<String, String> currentJavaFieldAliases = Map.of();
    private QinIrClassDeclaration currentJavaClassDeclaration;
    private final QinIrCodegenOptions options;

    public QinJsBackend() {
        this(QinIrCodegenOptions.javaScript());
    }

    public QinJsBackend(QinIrCodegenOptions options) {
        this.options = Objects.requireNonNull(options, "options cannot be null");
    }

    @Override
    public QinIrCodegenOptions options() {
        return options;
    }

    @Override
    public String compileProgram(QinIrProgram program) {
        return compileProgram(program, Set.of());
    }

    @Override
    public String compileProgram(QinIrProgram program, Set<String> externallyBoundJavaBinaryNames) {
        return compileProgram(program, externallyBoundJavaBinaryNames, false);
    }

    @Override
    public String compileProgramWithExternalJavaSdk(
            QinIrProgram program,
            Set<String> externallyBoundJavaBinaryNames) {
        return compileProgram(program, externallyBoundJavaBinaryNames, true, program.classDeclarations());
    }

    @Override
    public String compileProgramWithExternalJavaSdk(
            QinIrProgram program,
            Set<String> externallyBoundJavaBinaryNames,
            List<QinIrClassDeclaration> contextClassDeclarations) {
        return compileProgram(program, externallyBoundJavaBinaryNames, true, contextClassDeclarations);
    }

    private String compileProgram(
            QinIrProgram program,
            Set<String> externallyBoundJavaBinaryNames,
            boolean externalJavaSdkRuntime) {
        return compileProgram(program, externallyBoundJavaBinaryNames, externalJavaSdkRuntime, program.classDeclarations());
    }

    private String compileProgram(
            QinIrProgram program,
            Set<String> externallyBoundJavaBinaryNames,
            boolean externalJavaSdkRuntime,
            List<QinIrClassDeclaration> contextClassDeclarations) {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(externallyBoundJavaBinaryNames, "externallyBoundJavaBinaryNames cannot be null");
        generatedClassBinaryNames = generatedClassBinaryNames(program.classDeclarations());
        generatedClassesByBinaryName = generatedClassesByBinaryName(contextClassDeclarations);
        generatedClassReferencesBySimpleName = generatedClassReferencesBySimpleName(contextClassDeclarations);
        generatedJavaFieldAliases = javaFieldAliases(contextClassDeclarations);
        this.externallyBoundJavaBinaryNames = Set.copyOf(externallyBoundJavaBinaryNames);
        this.externalJavaSdkRuntime = externalJavaSdkRuntime;
        externalJavaSdkRuntimeImports = new LinkedHashSet<>();
        bindingAliases = new LinkedHashMap<>();

        StringBuilder js = new StringBuilder();
        js.append(options.target() == QinIrCodegenTarget.TYPESCRIPT
                ? "// Generated by Qin TypeScript backend\n"
                : "// Generated by Qin JS backend\n");
        int externalJavaSdkImportOffset = js.length();
        Map<String, String> javaAliases = new LinkedHashMap<>();
        emitBuiltinRuntimeHelpers(js, program);
        emitJavaAliases(js, program.javaImports(), javaAliases);
        emitJavaRuntimeAliasesForProgram(js, program, javaAliases);
        emitSubhutiRuleRuntimeHelpers(js, program);
        emitJsImports(js, program.jsImports());
        if (hasJavaRecordClass(program.classDeclarations())) {
            emitJavaHashRuntimeHelpers(js);
        }
        emitClassDeclarations(js, program.classDeclarations());

        Map<String, QinIrExpression> declarationMap = new LinkedHashMap<>();
        for (QinIrConstDeclaration declaration : program.declarations()) {
            js.append("const ").append(declareBindingName(declaration.name()));
            emitAnyTypeAnnotation(js);
            js.append(" = ");
            emitExpression(js, declaration.initializer());
            js.append(";\n");
            declarationMap.put(declaration.name(), declaration.initializer());
        }
        if (!declarationMap.isEmpty()) {
            js.append("\n");
        }

        js.append("function run() {\n");
        emitRuntimeStatements(js, program, declarationMap);
        String returnName = lastDeclarationName(program.declarations());
        if (returnName != null) {
            js.append("  return ").append(jsBindingName(returnName)).append(";\n");
        } else {
            js.append("  return null;\n");
        }
        js.append("}\n\n");
        js.append("const __qinResult = run();\n");
        if (emitTypeAnnotations()) {
            js.append("if (typeof globalThis !== 'undefined') {\n");
            js.append("  const __qinGlobal = globalThis as any;\n");
            js.append("  __qinGlobal.__qinResult = __qinResult;\n");
            js.append("}\n");
        } else {
            js.append("if (typeof globalThis !== 'undefined') {\n");
            js.append("  globalThis.__qinResult = __qinResult;\n");
            js.append("}\n");
        }

        if (externalJavaSdkRuntime) {
            js.insert(externalJavaSdkImportOffset, externalJavaSdkImportStatement());
        }

        return js.toString();
    }

    public static String javaSdkJsPackageName() {
        return JAVA_SDK_JS_PACKAGE;
    }

    public static String javaSdkJsModule() {
        QinJsBackend backend = new QinJsBackend();
        StringBuilder js = new StringBuilder();
        js.append("// Qin Java SDK for generated JavaScript\n");
        backend.emitBuiltinRuntimeHelpers(js, null);
        backend.emitJavaLangBooleanRuntime(js);
        backend.emitSlf4jRuntime(js);
        backend.emitJavaLangIntegerRuntime(js);
        backend.emitJavaLangDoubleRuntime(js);
        backend.emitJavaLangEnumRuntime(js);
        backend.emitJavaLangExceptionRuntime(js);
        backend.emitJavaLangSystemRuntime(js);
        backend.emitJavaLangStringBuilderRuntime(js);
        backend.emitJavaIoFileRuntime(js);
        backend.emitJavaNioFileRuntime(js);
        backend.emitJavaIoWriterRuntime(js);
        backend.emitJavaTimeRuntime(js);
        backend.emitJavaUtilArrayListRuntime(js);
        backend.emitJavaUtilListRuntime(js);
        backend.emitJavaUtilHashSetRuntime(js);
        backend.emitJavaUtilSetRuntime(js);
        backend.emitJavaHashRuntimeHelpers(js);
        backend.emitJavaUtilArraysRuntime(js);
        backend.emitJavaUtilCollectionsRuntime(js);
        backend.emitJavaUtilHashMapRuntime(js);
        backend.emitJavaUtilObjectsRuntime(js);
        backend.emitJavaUtilOptionalRuntime(js);
        backend.emitJavaUtilStreamRuntime(js);
        backend.emitJavaUtilConcurrentAtomicLongRuntime(js);
        backend.emitCaffeineRuntime(js);
        backend.emitJavaUtilRegexRuntime(js);
        backend.emitSubhutiRuleRuntimeHelpers(js, null);
        backend.emitJavaEnumValueRuntimeHelper(js);
        js.append("\nexport {\n");
        for (int i = 0; i < JAVA_SDK_RUNTIME_EXPORTS.size(); i++) {
            js.append("  ").append(JAVA_SDK_RUNTIME_EXPORTS.get(i));
            js.append(i == JAVA_SDK_RUNTIME_EXPORTS.size() - 1 ? "\n" : ",\n");
        }
        js.append("};\n");
        return js.toString();
    }

    private void emitJavaAliases(
            StringBuilder js,
            List<QinIrJavaImport> javaImports,
            Map<String, String> javaAliases) {
        for (QinIrJavaImport javaImport : javaImports) {
            emitJavaRuntimeAlias(js, javaImport.localName(), javaImport.ownerBinaryName(), javaAliases);
        }
        if (!javaImports.isEmpty()) {
            js.append("\n");
        }
    }

    private void emitJavaRuntimeAliasesForProgram(
            StringBuilder js,
            QinIrProgram program,
            Map<String, String> javaAliases) {
        for (QinIrConstDeclaration declaration : program.declarations()) {
            emitJavaRuntimeAliasesForExpression(js, declaration.initializer(), javaAliases);
        }
        for (QinIrExpressionStatement expressionStatement : program.expressionStatements()) {
            emitJavaRuntimeAliasesForExpression(js, expressionStatement.expression(), javaAliases);
        }
        for (QinIrConsoleLogValue consoleValueLog : program.consoleValueLogs()) {
            emitJavaRuntimeAliasesForExpression(js, consoleValueLog.value(), javaAliases);
        }
        for (QinIrConsoleLogJavaStaticCall call : program.javaStaticConsoleLogs()) {
            emitJavaRuntimeAlias(js, call.receiverName(), call.ownerBinaryName(), javaAliases);
            for (QinIrExpression argument : call.arguments()) {
                emitJavaRuntimeAliasesForExpression(js, argument, javaAliases);
            }
        }
        for (QinIrJavaInstanceMethodCall call : program.javaInstanceMethodCalls()) {
            for (QinIrExpression argument : call.arguments()) {
                emitJavaRuntimeAliasesForExpression(js, argument, javaAliases);
            }
        }
        for (QinIrConsoleLogJavaInstanceCall call : program.javaInstanceConsoleLogs()) {
            for (QinIrExpression argument : call.arguments()) {
                emitJavaRuntimeAliasesForExpression(js, argument, javaAliases);
            }
        }
        for (QinIrClassDeclaration classDeclaration : program.classDeclarations()) {
            if (classDeclaration.superType() != null) {
                emitJavaRuntimeAlias(
                        js,
                        jsClassReference(classDeclaration.superType().binaryName()),
                        classDeclaration.superType().binaryName(),
                        javaAliases);
            }
            for (QinIrFieldDeclaration field : classDeclaration.fields()) {
                emitJavaRuntimeAliasesForExpression(js, field.initializer(), javaAliases);
            }
            for (QinIrMethodDeclaration method : classDeclaration.methods()) {
                emitJavaRuntimeAliasesForExpression(js, method.returnExpression(), javaAliases);
                emitJavaRuntimeAliasesForStatements(js, method.bodyStatements(), javaAliases);
                for (QinIrExpression argument : method.superArguments()) {
                    emitJavaRuntimeAliasesForExpression(js, argument, javaAliases);
                }
            }
        }
    }

    private void emitJavaRuntimeAliasesForStatements(
            StringBuilder js,
            List<QinIrStatement> statements,
            Map<String, String> javaAliases) {
        for (QinIrStatement statement : statements) {
            emitJavaRuntimeAliasesForStatement(js, statement, javaAliases);
        }
    }

    private void emitJavaRuntimeAliasesForStatement(
            StringBuilder js,
            QinIrStatement statement,
            Map<String, String> javaAliases) {
        if (statement instanceof QinIrLocalDeclarationStatement localDeclarationStatement) {
            emitJavaRuntimeAliasesForExpression(js, localDeclarationStatement.initializer(), javaAliases);
            return;
        }
        if (statement instanceof QinIrStatementExpression statementExpression) {
            emitJavaRuntimeAliasesForExpression(js, statementExpression.expression(), javaAliases);
            return;
        }
        if (statement instanceof QinIrReturnStatement returnStatement) {
            emitJavaRuntimeAliasesForExpression(js, returnStatement.value(), javaAliases);
            return;
        }
        if (statement instanceof QinIrThrowStatement throwStatement) {
            emitJavaRuntimeAliasesForExpression(js, throwStatement.value(), javaAliases);
            return;
        }
        if (statement instanceof QinIrIfStatement ifStatement) {
            emitJavaRuntimeAliasesForExpression(js, ifStatement.test(), javaAliases);
            emitJavaRuntimeAliasesForStatements(js, ifStatement.consequent(), javaAliases);
            emitJavaRuntimeAliasesForStatements(js, ifStatement.alternate(), javaAliases);
            return;
        }
        if (statement instanceof QinIrWhileStatementNode whileStatement) {
            emitJavaRuntimeAliasesForExpression(js, whileStatement.test(), javaAliases);
            emitJavaRuntimeAliasesForStatements(js, whileStatement.body(), javaAliases);
            return;
        }
        if (statement instanceof QinIrDoWhileStatementNode doWhileStatement) {
            emitJavaRuntimeAliasesForStatements(js, doWhileStatement.body(), javaAliases);
            emitJavaRuntimeAliasesForExpression(js, doWhileStatement.test(), javaAliases);
            return;
        }
        if (statement instanceof QinIrForStatement forStatement) {
            for (QinIrLocalVariableDeclaration declaration : forStatement.initializerDeclarations()) {
                emitJavaRuntimeAliasesForExpression(js, declaration.initializer(), javaAliases);
            }
            for (QinIrExpression expression : forStatement.initializerExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, expression, javaAliases);
            }
            emitJavaRuntimeAliasesForExpression(js, forStatement.test(), javaAliases);
            for (QinIrExpression expression : forStatement.updateExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, expression, javaAliases);
            }
            emitJavaRuntimeAliasesForStatements(js, forStatement.body(), javaAliases);
            return;
        }
        if (statement instanceof QinIrForEachStatement forEachStatement) {
            emitJavaRuntimeAliasesForExpression(js, forEachStatement.iterable(), javaAliases);
            emitJavaRuntimeAliasesForStatements(js, forEachStatement.body(), javaAliases);
            return;
        }
        if (statement instanceof QinIrTryStatement tryStatement) {
            emitJavaRuntimeAliasesForStatements(js, tryStatement.tryBody(), javaAliases);
            for (QinIrCatchClause catchClause : tryStatement.catchClauses()) {
                emitJavaRuntimeAliasForType(js, catchClause.parameterType(), javaAliases);
                emitJavaRuntimeAliasesForStatements(js, catchClause.body(), javaAliases);
            }
            emitJavaRuntimeAliasesForStatements(js, tryStatement.finallyBody(), javaAliases);
        }
    }

    private void emitJavaRuntimeAliasForType(
            StringBuilder js,
            QinIrTypeRef type,
            Map<String, String> javaAliases) {
        if (type == null || type.binaryName() == null || isGeneratedClassOwner(type.binaryName())) {
            return;
        }
        emitJavaRuntimeAlias(js, simpleClassName(type.binaryName()), type.binaryName(), javaAliases);
    }

    private void emitJavaRuntimeAliasesForExpression(
            StringBuilder js,
            QinIrExpression expression,
            Map<String, String> javaAliases) {
        if (expression == null) {
            return;
        }
        if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            emitJavaRuntimeAliasesForExpression(js, assignmentExpression.target(), javaAliases);
            emitJavaRuntimeAliasesForExpression(js, assignmentExpression.value(), javaAliases);
            return;
        }
        if (expression instanceof QinIrLocalDeclarationExpression localDeclarationExpression) {
            emitJavaRuntimeAliasesForExpression(js, localDeclarationExpression.initializer(), javaAliases);
            return;
        }
        if (expression instanceof QinIrThrowExpression throwExpression) {
            emitJavaRuntimeAliasesForExpression(js, throwExpression.value(), javaAliases);
            return;
        }
        if (expression instanceof QinIrCastExpression castExpression) {
            emitJavaRuntimeAliasesForExpression(js, castExpression.expression(), javaAliases);
            return;
        }
        if (expression instanceof QinIrIfExpression ifExpression) {
            emitJavaRuntimeAliasesForExpression(js, ifExpression.test(), javaAliases);
            emitJavaRuntimeAliasesForExpression(js, ifExpression.consequent(), javaAliases);
            emitJavaRuntimeAliasesForExpression(js, ifExpression.alternate(), javaAliases);
            return;
        }
        if (expression instanceof QinIrFunctionLiteral functionLiteral) {
            if (functionLiteral.returnExpression() != null) {
                emitJavaRuntimeAliasesForExpression(js, functionLiteral.returnExpression(), javaAliases);
            }
            emitJavaRuntimeAliasesForStatements(js, functionLiteral.bodyStatements(), javaAliases);
            return;
        }
        if (expression instanceof QinIrForExpression forExpression) {
            for (QinIrLocalVariableDeclaration declaration : forExpression.initializerDeclarations()) {
                emitJavaRuntimeAliasesForExpression(js, declaration.initializer(), javaAliases);
            }
            for (QinIrExpression initializerExpression : forExpression.initializerExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, initializerExpression, javaAliases);
            }
            emitJavaRuntimeAliasesForExpression(js, forExpression.test(), javaAliases);
            for (QinIrExpression updateExpression : forExpression.updateExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, updateExpression, javaAliases);
            }
            for (QinIrLocalVariableDeclaration declaration : forExpression.bodyLocalDeclarations()) {
                emitJavaRuntimeAliasesForExpression(js, declaration.initializer(), javaAliases);
            }
            for (QinIrExpression bodyExpression : forExpression.bodyExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, bodyExpression, javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrForEachExpression forEachExpression) {
            emitJavaRuntimeAliasesForExpression(js, forEachExpression.iterable(), javaAliases);
            for (QinIrLocalVariableDeclaration declaration : forEachExpression.bodyLocalDeclarations()) {
                emitJavaRuntimeAliasesForExpression(js, declaration.initializer(), javaAliases);
            }
            for (QinIrExpression bodyExpression : forEachExpression.bodyExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, bodyExpression, javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrDoWhileExpression doWhileExpression) {
            for (QinIrLocalVariableDeclaration declaration : doWhileExpression.localDeclarations()) {
                emitJavaRuntimeAliasesForExpression(js, declaration.initializer(), javaAliases);
            }
            for (QinIrExpression bodyExpression : doWhileExpression.bodyExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, bodyExpression, javaAliases);
            }
            emitJavaRuntimeAliasesForExpression(js, doWhileExpression.test(), javaAliases);
            return;
        }
        if (expression instanceof QinIrWhileExpression whileExpression) {
            emitJavaRuntimeAliasesForExpression(js, whileExpression.test(), javaAliases);
            for (QinIrLocalVariableDeclaration declaration : whileExpression.localDeclarations()) {
                emitJavaRuntimeAliasesForExpression(js, declaration.initializer(), javaAliases);
            }
            for (QinIrExpression bodyExpression : whileExpression.bodyExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, bodyExpression, javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            emitJavaRuntimeAlias(
                    js,
                    javaNewExpression.classLocalName(),
                    javaNewExpression.ownerBinaryName(),
                    javaAliases);
            for (QinIrExpression argument : javaNewExpression.arguments()) {
                emitJavaRuntimeAliasesForExpression(js, argument, javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrJavaMethodReferenceExpression methodReferenceExpression) {
            emitJavaRuntimeAlias(
                    js,
                    methodReferenceExpression.classLocalName(),
                    methodReferenceExpression.ownerBinaryName(),
                    javaAliases);
            return;
        }
        if (expression instanceof QinIrJavaInstanceofPatternExpression instanceofPatternExpression) {
            emitJavaRuntimeAlias(
                    js,
                    instanceofPatternExpression.classLocalName(),
                    instanceofPatternExpression.ownerBinaryName(),
                    javaAliases);
            emitJavaRuntimeAliasesForExpression(js, instanceofPatternExpression.value(), javaAliases);
            return;
        }
        if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            emitJavaRuntimeAlias(
                    js,
                    staticMethodCallExpression.classLocalName(),
                    staticMethodCallExpression.ownerBinaryName(),
                    javaAliases);
            for (QinIrExpression argument : staticMethodCallExpression.arguments()) {
                emitJavaRuntimeAliasesForExpression(js, argument, javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrInstanceMethodCallExpression instanceMethodCallExpression) {
            emitJavaRuntimeAliasesForExpression(js, instanceMethodCallExpression.receiver(), javaAliases);
            for (QinIrExpression argument : instanceMethodCallExpression.arguments()) {
                emitJavaRuntimeAliasesForExpression(js, argument, javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrLetExpression letExpression) {
            for (QinIrLocalVariableDeclaration declaration : letExpression.localDeclarations()) {
                emitJavaRuntimeAliasesForExpression(js, declaration.initializer(), javaAliases);
            }
            for (QinIrExpression leadingExpression : letExpression.leadingExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, leadingExpression, javaAliases);
            }
            emitJavaRuntimeAliasesForExpression(js, letExpression.resultExpression(), javaAliases);
            return;
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            for (QinIrExpression leadingExpression : sequenceExpression.leadingExpressions()) {
                emitJavaRuntimeAliasesForExpression(js, leadingExpression, javaAliases);
            }
            emitJavaRuntimeAliasesForExpression(js, sequenceExpression.resultExpression(), javaAliases);
            return;
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            for (QinIrExpression argument : builtinCallExpression.arguments()) {
                emitJavaRuntimeAliasesForExpression(js, argument, javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                emitJavaRuntimeAliasesForExpression(js, property.value(), javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            if ("System".equals(memberAccessExpression.objectName())
                    || "java.lang.System".equals(memberAccessExpression.objectName())) {
                emitJavaRuntimeAlias(js, "System", "java.lang.System", javaAliases);
            }
            if ("Boolean".equals(memberAccessExpression.objectName())
                    || "java.lang.Boolean".equals(memberAccessExpression.objectName())) {
                emitJavaRuntimeAlias(js, "Boolean", "java.lang.Boolean", javaAliases);
            }
            return;
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            emitJavaRuntimeAliasesForExpression(js, propertyAccessExpression.receiver(), javaAliases);
            return;
        }
        if (expression instanceof QinIrElementAccessExpression elementAccessExpression) {
            emitJavaRuntimeAliasesForExpression(js, elementAccessExpression.receiver(), javaAliases);
            emitJavaRuntimeAliasesForExpression(js, elementAccessExpression.index(), javaAliases);
        }
    }

    private void emitJavaRuntimeAlias(
            StringBuilder js,
            String localName,
            String ownerBinaryName,
            Map<String, String> javaAliases) {
        if (isGeneratedClassOwner(ownerBinaryName)) {
            return;
        }
        if (externallyBoundJavaBinaryNames.contains(ownerBinaryName)) {
            return;
        }
        String aliasKey = localName + "\u0000" + ownerBinaryName;
        if (javaAliases.containsKey(aliasKey)) {
            return;
        }
        String aliasName = isJsIdentifier(localName) ? localName : null;
        switch (ownerBinaryName) {
            case "java.lang.Math" -> {
                if (aliasName != null && !"Math".equals(aliasName)) {
                    js.append("const ").append(aliasName).append(" = Math;\n");
                }
            }
            case "java.lang.StringBuilder" -> {
                emitJavaLangStringBuilderRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangStringBuilder");
            }
            case "java.lang.String" -> {
                emitJavaLangStringRuntime(js);
                if (!"String".equals(aliasName)) {
                    emitJavaAliasBinding(js, aliasName, "__QinJavaLangString");
                }
            }
            case "java.lang.Boolean" -> {
                emitJavaLangBooleanRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangBoolean");
            }
            case "java.lang.Integer" -> {
                emitJavaLangIntegerRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangInteger");
            }
            case "java.lang.Double" -> {
                emitJavaLangDoubleRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangDouble");
            }
            case "java.lang.Enum" -> {
                emitJavaLangEnumRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangEnum");
            }
            case "java.lang.Throwable" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangThrowable");
            }
            case "java.lang.Exception" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangException");
            }
            case "java.lang.RuntimeException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangRuntimeException");
            }
            case "java.lang.ReflectiveOperationException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangReflectiveOperationException");
            }
            case "java.lang.ClassNotFoundException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangClassNotFoundException");
            }
            case "java.lang.NoSuchMethodException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangNoSuchMethodException");
            }
            case "java.lang.NumberFormatException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangNumberFormatException");
            }
            case "java.lang.UnsupportedOperationException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangUnsupportedOperationException");
            }
            case "java.lang.Error" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangError");
            }
            case "java.lang.StackOverflowError" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangStackOverflowError");
            }
            case "java.lang.IllegalArgumentException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangIllegalArgumentException");
            }
            case "java.lang.IllegalStateException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangIllegalStateException");
            }
            case "java.io.IOException" -> {
                emitJavaLangExceptionRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaIoIOException");
            }
            case "java.lang.System" -> {
                emitJavaLangSystemRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaLangSystem");
            }
            case "java.io.File" -> {
                emitJavaIoFileRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaIoFile");
            }
            case "java.io.FileWriter" -> {
                emitJavaIoWriterRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaIoFileWriter");
            }
            case "java.io.BufferedWriter" -> {
                emitJavaIoWriterRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaIoBufferedWriter");
            }
            case "java.nio.file.Path" -> {
                emitJavaNioFileRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaNioFilePath");
            }
            case "java.nio.file.Paths" -> {
                emitJavaNioFileRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaNioFilePaths");
            }
            case "java.nio.file.Files" -> {
                emitJavaNioFileRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaNioFileFiles");
            }
            case "java.time.LocalDateTime" -> {
                emitJavaTimeRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaTimeLocalDateTime");
            }
            case "java.time.format.DateTimeFormatter" -> {
                emitJavaTimeRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaTimeFormatDateTimeFormatter");
            }
            case "java.util.List" -> {
                emitJavaUtilListRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilList");
            }
            case "java.util.Set" -> {
                emitJavaUtilSetRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilSet");
            }
            case "java.util.Arrays" -> {
                emitJavaUtilArraysRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilArrays");
            }
            case "java.util.Collections" -> {
                emitJavaUtilCollectionsRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilCollections");
            }
            case "java.util.ArrayList" -> {
                emitJavaUtilArrayListRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilArrayList");
            }
            case "java.util.HashSet", "java.util.LinkedHashSet" -> {
                emitJavaUtilHashSetRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilHashSet");
            }
            case "java.util.HashMap", "java.util.LinkedHashMap",
                    "java.util.concurrent.ConcurrentHashMap", "java.util.concurrent.ConcurrentMap" -> {
                emitJavaUtilHashMapRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilHashMap");
            }
            case "java.util.Objects" -> {
                emitJavaUtilObjectsRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilObjects");
            }
            case "java.util.Optional" -> {
                emitJavaUtilOptionalRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilOptional");
            }
            case "java.util.stream.Collectors" -> {
                emitJavaUtilStreamRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilStreamCollectors");
            }
            case "java.util.regex.Pattern" -> {
                emitJavaUtilRegexRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilRegexPattern");
            }
            case "java.util.regex.Matcher" -> {
                emitJavaUtilRegexRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilRegexMatcher");
            }
            case "java.util.concurrent.atomic.AtomicLong" -> {
                emitJavaUtilConcurrentAtomicLongRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinJavaUtilConcurrentAtomicLong");
            }
            case "com.github.benmanes.caffeine.cache.Caffeine" -> {
                emitCaffeineRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinCaffeine");
            }
            case "com.github.benmanes.caffeine.cache.Cache" -> {
                emitCaffeineRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinCaffeineCache");
            }
            case "com.github.benmanes.caffeine.cache.RemovalCause" -> {
                emitCaffeineRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinCaffeineRemovalCause");
            }
            case "org.slf4j.LoggerFactory" -> {
                emitSlf4jRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinSlf4jLoggerFactory");
            }
            case "org.slf4j.Logger" -> {
                emitSlf4jRuntime(js);
                emitJavaAliasBinding(js, aliasName, "__QinSlf4jLogger");
            }
            default -> {
                Class<?> ownerClass = loadJavaOwner(ownerBinaryName);
                if (!ownerClass.isRecord()) {
                    throw new IllegalArgumentException(
                            "JS backend does not support Java interop owner yet: " + ownerBinaryName);
                }
                emitJavaRecordRuntime(js, localName, ownerClass);
            }
        }
        javaAliases.put(aliasKey, ownerBinaryName);
    }

    private void emitJavaAliasBinding(StringBuilder js, String aliasName, String runtimeReference) {
        if (aliasName != null && !aliasName.equals(runtimeReference)) {
            js.append("const ").append(aliasName).append(" = ").append(runtimeReference).append(";\n");
        }
    }

    private void requireExternalJavaSdkRuntime(String name) {
        if ("Math".equals(name)) {
            return;
        }
        if (externalJavaSdkRuntime) {
            externalJavaSdkRuntimeImports.add(name);
            if ("__QinJavaUtilArrayList".equals(name) || "__QinJavaUtilList".equals(name)) {
                externalJavaSdkRuntimeImports.add("__QinJavaUtilUnmodifiableList");
            }
            if ("__QinJavaUtilHashSet".equals(name) || "__QinJavaUtilSet".equals(name)) {
                externalJavaSdkRuntimeImports.add("__QinJavaUtilUnmodifiableSet");
            }
            if ("__QinJavaUtilHashMap".equals(name) || "__QinJavaUtilCollections".equals(name)) {
                externalJavaSdkRuntimeImports.add("__QinJavaUtilUnmodifiableMap");
            }
        }
    }

    private void requireExternalJavaSdkRuntime(String... names) {
        if (!externalJavaSdkRuntime) {
            return;
        }
        for (String name : names) {
            externalJavaSdkRuntimeImports.add(name);
        }
    }

    private String externalJavaSdkImportStatement() {
        Set<String> sdkImports = new LinkedHashSet<>();
        Set<String> toolingImports = new LinkedHashSet<>();
        for (String runtimeImport : externalJavaSdkRuntimeImports) {
            if (isJavaSdkToolingRuntimeImport(runtimeImport)) {
                toolingImports.add(runtimeImport);
            } else {
                sdkImports.add(runtimeImport);
            }
        }
        StringBuilder imports = new StringBuilder();
        if (!sdkImports.isEmpty()) {
            imports.append("import { ").append(String.join(", ", sdkImports))
                    .append(" } from \"").append(JAVA_SDK_JS_PACKAGE).append("\";\n");
        }
        if (!toolingImports.isEmpty()) {
            imports.append("import { ").append(String.join(", ", toolingImports))
                    .append(" } from \"").append(JAVA_SDK_TOOLING_JS_PACKAGE).append("\";\n");
        }
        if (imports.length() > 0) {
            imports.append('\n');
        }
        return imports.toString();
    }

    private boolean isJavaSdkToolingRuntimeImport(String name) {
        return "__qin_subhuti_rule_cache_key".equals(name)
                || "__QinJavaIoFile".equals(name)
                || "__QinJavaNioFilePath".equals(name)
                || "__QinJavaNioFilePaths".equals(name)
                || "__QinJavaNioFileFiles".equals(name)
                || "__QinJavaIoFileWriter".equals(name)
                || "__QinJavaIoBufferedWriter".equals(name);
    }

    private boolean isJsIdentifier(String value) {
        return value != null && value.matches("[A-Za-z_$][A-Za-z0-9_$]*");
    }

    private boolean emitTypeAnnotations() {
        return options.emitTypeAnnotations();
    }

    private void emitAnyTypeAnnotation(StringBuilder js) {
        if (emitTypeAnnotations()) {
            js.append(": any");
        }
    }

    private void emitTypeAnnotation(StringBuilder js, QinIrTypeRef type) {
        if (emitTypeAnnotations()) {
            js.append(": ").append(tsTypeName(type));
        }
    }

    private void emitNullableTypeAnnotation(StringBuilder js, QinIrTypeRef type) {
        if (!emitTypeAnnotations()) {
            return;
        }
        String typeName = tsTypeName(type);
        js.append(": ");
        if ("any".equals(typeName) || typeName.contains("null")) {
            js.append(typeName);
        } else {
            js.append(typeName).append(" | null");
        }
    }

    private String tsTypeName(QinIrTypeRef type) {
        if (type == null) {
            return "any";
        }
        return switch (type.kind()) {
            case VOID -> "any";
            case BOOLEAN -> "boolean";
            case INT, DOUBLE -> "number";
            case STRING -> "string";
            case CLASS -> tsClassTypeName(type);
        };
    }

    private String tsClassTypeName(QinIrTypeRef type) {
        String binaryName = type.binaryName();
        if (binaryName == null || binaryName.isBlank()) {
            return "any";
        }
        if (binaryName.startsWith("[")) {
            return tsArrayTypeName(binaryName);
        }
        if (String.class.getName().equals(binaryName)) {
            return "string";
        }
        if (Object.class.getName().equals(binaryName)) {
            return "any";
        }
        if (Boolean.class.getName().equals(binaryName)) {
            return "boolean";
        }
        if (Number.class.isAssignableFrom(loadClassOrObject(binaryName))) {
            return "number";
        }
        if (isGeneratedClassOwner(binaryName) || generatedClassesByBinaryName.containsKey(binaryName)) {
            return jsClassReference(binaryName);
        }
        return "any";
    }

    private Class<?> loadClassOrObject(String binaryName) {
        try {
            return Class.forName(binaryName);
        } catch (Throwable ignored) {
            return Object.class;
        }
    }

    private String tsArrayTypeName(String binaryName) {
        QinIrTypeRef element = varargsElementType(QinIrTypeRef.classType(binaryName));
        String elementType = tsTypeName(element);
        return "any".equals(elementType) ? "any[]" : elementType + "[]";
    }

    private String declareBindingName(String sourceName) {
        String alias = jsIdentifier(sourceName);
        bindingAliases.put(sourceName, alias);
        return alias;
    }

    private String jsBindingName(String sourceName) {
        return bindingAliases.getOrDefault(sourceName, jsIdentifier(sourceName));
    }

    private String jsJavaFieldName(String sourceName) {
        return "__qin_field_" + jsIdentifier(sourceName);
    }

    private String jsCurrentJavaFieldName(String sourceName) {
        return currentJavaFieldAliases.getOrDefault(sourceName, sourceName);
    }

    private String jsIdentifier(String sourceName) {
        String value = sourceName == null || sourceName.isBlank() ? "__qin_empty" : sourceName;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            boolean valid = i == 0
                    ? (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || ch == '_' || ch == '$'
                    : (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')
                    || (ch >= '0' && ch <= '9') || ch == '_' || ch == '$';
            result.append(valid ? ch : '_');
        }
        String candidate = result.toString();
        if (candidate.isBlank()
                || Character.isDigit(candidate.charAt(0))
                || isJsReservedWord(candidate)) {
            candidate = "__qin_" + candidate;
        }
        return candidate;
    }

    private boolean isJsReservedWord(String value) {
        return Set.of(
                "await", "break", "case", "catch", "class", "const", "continue", "debugger", "default", "delete",
                "do", "else", "export", "extends", "finally", "for", "function", "if", "import", "in",
                "instanceof", "let", "new", "return", "super", "switch", "this", "throw", "try", "typeof",
                "var", "void", "while", "with", "yield")
                .contains(value);
    }

    private void emitJavaRecordRuntime(StringBuilder js, String localName, Class<?> ownerClass) {
        if (js.indexOf("class " + localName + " ") >= 0) {
            return;
        }
        RecordComponent[] components = ownerClass.getRecordComponents();
        js.append("class ").append(localName).append(" {\n");
        js.append("  constructor(");
        for (int i = 0; i < components.length; i++) {
            js.append("__recordValue").append(i);
            if (i < components.length - 1) {
                js.append(", ");
            }
        }
        js.append(") {\n");
        for (int i = 0; i < components.length; i++) {
            js.append("    this.__").append(components[i].getName()).append(" = __recordValue").append(i).append(";\n");
        }
        js.append("  }\n");
        for (RecordComponent component : components) {
            js.append("  [\"").append(escapeJs(component.getName())).append("\"]() { return this.__")
                    .append(component.getName())
                    .append("; }\n");
        }
        js.append("}\n");
        emitJavaRecordStaticFields(js, localName, ownerClass, components);
    }

    private void emitJavaRecordStaticFields(
            StringBuilder js,
            String localName,
            Class<?> ownerClass,
            RecordComponent[] components) {
        for (Field field : ownerClass.getFields()) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) || field.getType() != ownerClass) {
                continue;
            }
            try {
                Object value = field.get(null);
                js.append(localName).append(".").append(field.getName()).append(" = new ")
                        .append(localName)
                        .append("(");
                for (int i = 0; i < components.length; i++) {
                    Object componentValue = components[i].getAccessor().invoke(value);
                    emitJavaRecordLiteralValue(js, componentValue);
                    if (i < components.length - 1) {
                        js.append(", ");
                    }
                }
                js.append(");\n");
            } catch (ReflectiveOperationException e) {
                throw new IllegalArgumentException(
                        "Could not emit Java record static field: " + ownerClass.getName() + "." + field.getName(), e);
            }
        }
    }

    private void emitJavaRecordLiteralValue(StringBuilder js, Object value) {
        if (value == null) {
            js.append("null");
            return;
        }
        if (value instanceof Boolean || value instanceof Number) {
            js.append(value);
            return;
        }
        if (value instanceof String text) {
            js.append("\"").append(escapeJs(text)).append("\"");
            return;
        }
        throw new IllegalArgumentException("Unsupported Java record static literal value: " + value.getClass().getName());
    }

    private void emitJavaLangStringRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinJavaLangString");
            return;
        }
        if (js.indexOf("const __QinJavaLangString") >= 0) {
            return;
        }
        js.append("""
                const __QinJavaLangString = {
                  __hashCode(value) {
                    const text = String(value);
                    let hash = 0;
                    for (let index = 0; index < text.length; index++) {
                      hash = ((hash * 31) + text.charCodeAt(index)) | 0;
                    }
                    return hash;
                  },
                  __objectMethod(value, methodName) {
                    if (value == null) {
                      throw new Error("NullPointerException: " + methodName + "()");
                    }
                    const method = value[methodName];
                    if ((typeof value === "object" || typeof value === "function") && typeof method === "function") {
                      return method.bind(value);
                    }
                    return null;
                  },
                  length(value) {
                    if (value == null) {
                      throw new Error("NullPointerException: length()");
                    }
                    if (typeof value.length === "function") {
                      return value.length();
                    }
                    if (typeof value.length === "number") {
                      return value.length;
                    }
                    return String(value).length;
                  },
                  equals(left, right) {
                    const method = this.__objectMethod(left, "equals");
                    if (method != null) {
                      return method(right) === true;
                    }
                    return String(left) === String(right);
                  },
                  contains(value, part) {
                    const method = this.__objectMethod(value, "contains");
                    if (method != null) {
                      return method(part) === true;
                    }
                    return String(value).includes(String(part));
                  },
                  isEmpty(value) {
                    const method = this.__objectMethod(value, "isEmpty");
                    if (method != null) {
                      return method() === true;
                    }
                    return String(value).length === 0;
                  },
                  isBlank(value) {
                    const method = this.__objectMethod(value, "isBlank");
                    if (method != null) {
                      return method() === true;
                    }
                    return String(value).trim().length === 0;
                  },
                  hashCode(value) {
                    const method = this.__objectMethod(value, "hashCode");
                    if (method != null) {
                      return method();
                    }
                    return this.__hashCode(value);
                  },
                  startsWith(value, prefix) {
                    return String(value).startsWith(String(prefix));
                  },
                  endsWith(value, suffix) {
                    return String(value).endsWith(String(suffix));
                  },
                  charAt(value, index) {
                    return String(value).charAt(Number(index));
                  },
                  substring(value, start, end) {
                    return String(value).substring(Number(start), end == null ? undefined : Number(end));
                  },
                  format(formatText, ...values) {
                    let valueIndex = 0;
                    return ("" + formatText).replace(/%([csd])/g, (_match, kind) => {
                      const value = values[valueIndex++];
                      if (kind === "c") {
                        if (typeof value === "number") {
                          return String.fromCharCode(value);
                        }
                        return ("" + value).charAt(0);
                      }
                      if (kind === "d") {
                        return "" + Math.trunc(value - 0);
                      }
                      return "" + value;
                    });
                  }
                };
                """);
    }

    private void emitJavaLangBooleanRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinJavaLangBoolean");
            return;
        }
        if (js.indexOf("const __QinJavaLangBoolean") >= 0) {
            return;
        }
        js.append("""
                const __QinJavaLangBoolean = {
                  TRUE: Object.freeze({
                    equals(value) { return value === true || (value != null && typeof value.valueOf === "function" && value.valueOf() === true); },
                    valueOf() { return true; },
                    toString() { return "true"; }
                  }),
                  FALSE: Object.freeze({
                    equals(value) { return value === false || (value != null && typeof value.valueOf === "function" && value.valueOf() === false); },
                    valueOf() { return false; },
                    toString() { return "false"; }
                  }),
                  valueOf(value) {
                    return value === true || String(value).toLowerCase() === "true";
                  },
                  parseBoolean(value) {
                    return String(value).toLowerCase() === "true";
                  }
                };
                """);
    }

    private void emitSlf4jRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinSlf4jLogger", "__QinSlf4jLoggerFactory");
            return;
        }
        if (js.indexOf("const __QinSlf4jLoggerFactory") >= 0) {
            return;
        }
        js.append("""
                const __QinSlf4jLogger = {
                  warn(_message, ..._args) { return null; },
                  info(_message, ..._args) { return null; },
                  debug(_message, ..._args) { return null; },
                  error(_message, ..._args) { return null; },
                  trace(_message, ..._args) { return null; }
                };
                const __QinSlf4jLoggerFactory = {
                  getLogger(_owner) { return __QinSlf4jLogger; }
                };
                """);
    }

    private void emitJavaLangIntegerRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinJavaLangInteger");
            return;
        }
        if (js.indexOf("const __QinJavaLangInteger") >= 0) {
            return;
        }
        js.append("""
                const __QinJavaLangInteger = {
                  sum(a, b) { return (a | 0) + (b | 0); },
                  compare(a, b) { return (a | 0) === (b | 0) ? 0 : ((a | 0) < (b | 0) ? -1 : 1); },
                  valueOf(value) { return value | 0; },
                  parseInt(value, radix = 10) { return Number.parseInt(String(value), radix); }
                };
                """);
    }

    private void emitJavaLangDoubleRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinJavaLangDouble");
            return;
        }
        if (js.indexOf("const __QinJavaLangDouble") >= 0) {
            return;
        }
        js.append("""
                const __QinJavaLangDouble = {
                  compare(a, b) {
                    const left = Number(a);
                    const right = Number(b);
                    if (Number.isNaN(left) && Number.isNaN(right)) {
                      return 0;
                    }
                    if (Number.isNaN(left)) {
                      return 1;
                    }
                    if (Number.isNaN(right)) {
                      return -1;
                    }
                    return left === right ? 0 : (left < right ? -1 : 1);
                  },
                  parseDouble(value) { return Number.parseFloat(String(value)); },
                  valueOf(value) { return Number(value); }
                };
                """);
    }

    private void emitJavaLangEnumRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinJavaLangEnum");
            return;
        }
        if (js.indexOf("class __QinJavaLangEnum") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaLangEnum {
                  constructor() {
                  }
                  name() {
                    return this.__qinEnumName == null ? "" : this.__qinEnumName;
                  }
                  ordinal() {
                    return this.__qinEnumOrdinal == null ? -1 : this.__qinEnumOrdinal;
                  }
                  toString() {
                    return this.name();
                  }
                }
                """);
    }

    private void emitJavaLangExceptionRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime(
                    "__QinJavaLangThrowable",
                    "__QinJavaLangException",
                    "__QinJavaLangRuntimeException",
                    "__QinJavaLangReflectiveOperationException",
                    "__QinJavaLangClassNotFoundException",
                    "__QinJavaLangNoSuchMethodException",
                    "__QinJavaLangError",
                    "__QinJavaLangStackOverflowError",
                    "__QinJavaLangIllegalArgumentException",
                    "__QinJavaLangNumberFormatException",
                    "__QinJavaLangIllegalStateException",
                    "__QinJavaLangUnsupportedOperationException",
                    "__QinJavaIoIOException");
            return;
        }
        if (js.indexOf("class __QinJavaLangThrowable") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaLangThrowable {
                  constructor(message, cause) {
                    this.name = this.constructor.name;
                    this.message = message == null ? null : String(message);
                    this.__cause = cause == null ? null : cause;
                  }
                  getMessage() {
                    return this.message;
                  }
                  getCause() {
                    return this.__cause;
                  }
                  toString() {
                    return this.message == null || this.message === ""
                      ? this.name
                      : this.name + ": " + this.message;
                  }
                }
                class __QinJavaLangException extends __QinJavaLangThrowable {
                }
                class __QinJavaLangRuntimeException extends __QinJavaLangException {
                }
                class __QinJavaLangReflectiveOperationException extends __QinJavaLangException {
                }
                class __QinJavaLangClassNotFoundException extends __QinJavaLangException {
                }
                class __QinJavaLangNoSuchMethodException extends __QinJavaLangReflectiveOperationException {
                }
                class __QinJavaLangError extends __QinJavaLangThrowable {
                }
                class __QinJavaLangStackOverflowError extends __QinJavaLangError {
                }
                class __QinJavaLangIllegalArgumentException extends __QinJavaLangRuntimeException {
                }
                class __QinJavaLangNumberFormatException extends __QinJavaLangIllegalArgumentException {
                }
                class __QinJavaLangIllegalStateException extends __QinJavaLangRuntimeException {
                }
                class __QinJavaLangUnsupportedOperationException extends __QinJavaLangRuntimeException {
                }
                class __QinJavaIoIOException extends __QinJavaLangException {
                }
                """);
    }

    private void emitJavaLangSystemRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinJavaLangSystem");
            return;
        }
        if (js.indexOf("const __QinJavaLangSystem") >= 0) {
            return;
        }
        js.append("""
                const __QinJavaLangSystem = (() => {
                  let nextIdentityHashCode = 1;
                  const __QinJsMap = __qin_builtin_constructor__("Map");
                  const objectIdentityHashCodes = new __QinJsMap();
                  const primitiveIdentityHashCodes = new __QinJsMap();
                  function identityHashCode(value) {
                    if ((typeof value === "object" && value !== null) || typeof value === "function") {
                      if (!objectIdentityHashCodes.has(value)) {
                        objectIdentityHashCodes.set(value, nextIdentityHashCode++);
                      }
                      return objectIdentityHashCodes.get(value);
                    }
                    const key = typeof value + ":" + String(value);
                    if (!primitiveIdentityHashCodes.has(key)) {
                      primitiveIdentityHashCodes.set(key, nextIdentityHashCode++);
                    }
                    return primitiveIdentityHashCodes.get(key);
                  }
                  function property(name) {
                    const key = String(name);
                    if (key === "user.dir") {
                      return globalThis.__qinJavaUserDir == null ? "" : String(globalThis.__qinJavaUserDir);
                    }
                    if (key === "java.version") {
                      return globalThis.__qinJavaVersion == null ? "25" : String(globalThis.__qinJavaVersion);
                    }
                    return null;
                  }
                  function printTo(methodName, value) {
                    const target = typeof console === "undefined" ? null : console;
                    if (target != null && typeof target[methodName] === "function") {
                      target[methodName](value == null ? "null" : value);
                    }
                  }
                  return {
                    out: { println(value) { printTo("log", value); } },
                    err: { println(value) { printTo("error", value); } },
                    currentTimeMillis() { return Date.now(); },
                    nanoTime() {
                      const now = typeof performance !== "undefined" && performance != null
                        && typeof performance.now === "function"
                        ? performance.now()
                        : Date.now();
                      return Math.floor(now * 1000000);
                    },
                    getProperty: property,
                    identityHashCode
                  };
                })();
                """);
    }

    private void emitJavaLangStringBuilderRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinJavaLangStringBuilder");
            return;
        }
        if (js.indexOf("class __QinJavaLangStringBuilder") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaLangStringBuilder {
                  constructor(initialValue) {
                    this.__text = "";
                    if (initialValue != null) {
                      this.__text += String(initialValue);
                    }
                  }
                  append(value) {
                    this.__text += String(value);
                    return this;
                  }
                  length() {
                    return this.__text.length;
                  }
                  charAt(index) {
                    return this.__text.charAt(index);
                  }
                  setLength(length) {
                    this.__text = this.__text.slice(0, length);
                  }
                  toString() {
                    return this.__text;
                  }
                }
                """);
    }

    private void emitJavaIoFileRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__QinJavaIoFile");
            return;
        }
        if (js.indexOf("class __QinJavaIoFile") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaIoFile {
                  constructor(pathOrParent, child) {
                    if (arguments.length >= 2) {
                      const parentPath = pathOrParent instanceof __QinJavaIoFile
                        ? pathOrParent.getPath()
                        : String(pathOrParent == null ? "" : pathOrParent);
                      this.__path = __QinJavaIoFile.__join(parentPath, child);
                    } else {
                      this.__path = String(pathOrParent == null ? "" : pathOrParent);
                    }
                    this.__path = __QinJavaIoFile.__normalize(this.__path);
                  }
                  static __separator() {
                    return globalThis.__qinJavaFileSeparator == null
                      ? "/"
                      : String(globalThis.__qinJavaFileSeparator);
                  }
                  static __separatorCharCode() {
                    const sep = __QinJavaIoFile.__separator();
                    return sep.length === 0 ? 47 : sep.charCodeAt(0);
                  }
                  static __isSeparatorCode(code) {
                    return code === 47 || code === 92;
                  }
                  static __normalize(path) {
                    const text = String(path == null ? "" : path);
                    if (text.length === 0) {
                      return text;
                    }
                    const sep = __QinJavaIoFile.__separator();
                    const sepCode = __QinJavaIoFile.__separatorCharCode();
                    let normalized = "";
                    for (let i = 0; i < text.length; i++) {
                      const code = text.charCodeAt(i);
                      normalized += __QinJavaIoFile.__isSeparatorCode(code)
                        ? String.fromCharCode(sepCode)
                        : text.charAt(i);
                    }
                    const prefix = __QinJavaIoFile.__drivePrefix(normalized);
                    let rest = prefix.length === 0 ? normalized : normalized.slice(prefix.length);
                    const absolute = rest.startsWith(sep);
                    const parts = [];
                    let part = "";
                    for (let i = 0; i <= rest.length; i++) {
                      const atEnd = i === rest.length;
                      const ch = atEnd ? "" : rest.charAt(i);
                      if (!atEnd && ch !== sep) {
                        part += ch;
                        continue;
                      }
                      if (part.length === 0 || part === ".") {
                        part = "";
                        continue;
                      }
                      if (part === "..") {
                        if (parts.length > 0 && parts[parts.length - 1] !== "..") {
                          parts.pop();
                        } else if (!absolute) {
                          parts.push(part);
                        }
                        part = "";
                        continue;
                      }
                      parts.push(part);
                      part = "";
                    }
                    const body = parts.join(sep);
                    return prefix + (absolute ? sep : "") + body;
                  }
                  static __drivePrefix(path) {
                    const text = String(path == null ? "" : path);
                    if (text.length < 2 || text.charAt(1) !== ":") {
                      return "";
                    }
                    const code = text.charCodeAt(0);
                    const isUpper = code >= 65 && code <= 90;
                    const isLower = code >= 97 && code <= 122;
                    return isUpper || isLower ? text.slice(0, 2) : "";
                  }
                  static __join(parent, child) {
                    const sep = __QinJavaIoFile.__separator();
                    const left = String(parent == null ? "" : parent);
                    const right = String(child == null ? "" : child);
                    if (left.length === 0) {
                      return right;
                    }
                    if (right.length === 0) {
                      return left;
                    }
                    const lastCode = left.charCodeAt(left.length - 1);
                    return __QinJavaIoFile.__isSeparatorCode(lastCode) ? left + right : left + sep + right;
                  }
                  static __configuredExists(path) {
                    const normalized = __QinJavaIoFile.__normalize(path);
                    const hook = globalThis.__qinJavaFileExists;
                    if (typeof hook === "function") {
                      return !!hook(normalized);
                    }
                    const files = globalThis.__qinJavaExistingFiles;
                    if (files == null) {
                      return false;
                    }
                    if (typeof files.has === "function") {
                      return !!(files.has(normalized) || files.has(String(path)));
                    }
                    if (Array.isArray(files)) {
                      return files.indexOf(normalized) >= 0 || files.indexOf(String(path)) >= 0;
                    }
                    if (typeof files === "object") {
                      return !!(files[normalized] || files[String(path)]);
                    }
                    return false;
                  }
                  getPath() {
                    return this.__path;
                  }
                  getAbsolutePath() {
                    return this.__path;
                  }
                  getParentFile() {
                    const sep = __QinJavaIoFile.__separator();
                    const path = this.__path;
                    if (path == null || path.length === 0) {
                      return null;
                    }
                    let end = path.length;
                    while (end > 1 && __QinJavaIoFile.__isSeparatorCode(path.charCodeAt(end - 1))) {
                      end--;
                    }
                    const trimmed = path.slice(0, end);
                    let index = -1;
                    for (let i = trimmed.length - 1; i >= 0; i--) {
                      if (__QinJavaIoFile.__isSeparatorCode(trimmed.charCodeAt(i))) {
                        index = i;
                        break;
                      }
                    }
                    if (index < 0) {
                      return null;
                    }
                    if (index === 0) {
                      return new __QinJavaIoFile(trimmed.charAt(0));
                    }
                    if (index === 2 && __QinJavaIoFile.__drivePrefix(trimmed).length > 0) {
                      return new __QinJavaIoFile(trimmed.slice(0, 3));
                    }
                    return new __QinJavaIoFile(trimmed.slice(0, index));
                  }
                  exists() {
                    return __QinJavaIoFile.__configuredExists(this.__path);
                  }
                  equals(other) {
                    return other instanceof __QinJavaIoFile && other.getPath() === this.__path;
                  }
                  toString() {
                    return this.__path;
                  }
                }
                __QinJavaIoFile.separator = __QinJavaIoFile.__separator();
                """);
    }

    private void emitJavaNioFileRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        emitJavaIoFileRuntime(js);
        if (js.indexOf("class __QinJavaNioFilePath") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaNioFilePath {
                  constructor(path) {
                    this.__path = path == null ? "" : String(path);
                  }
                  getParent() {
                    const parent = new __QinJavaIoFile(this.__path).getParentFile();
                    return parent == null ? null : new __QinJavaNioFilePath(parent.getPath());
                  }
                  toString() {
                    return this.__path;
                  }
                }
                class __QinJavaNioFilePaths {
                  static get(first, ...more) {
                    let path = first == null ? "" : String(first);
                    for (const part of more) {
                      path = __QinJavaIoFile.__join(path, part == null ? "" : String(part));
                    }
                    return new __QinJavaNioFilePath(path);
                  }
                }
                class __QinJavaNioFileFiles {
                  static exists(path) {
                    return false;
                  }
                  static createDirectories(path) {
                    return path;
                  }
                }
                """);
    }

    private void emitJavaIoWriterRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaIoBufferedWriter") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaIoFileWriter {
                  constructor(filePath, append = false) {
                    this.filePath = filePath;
                    this.append = append;
                    this.buffer = "";
                  }
                  write(message) {
                    this.buffer += message == null ? "null" : String(message);
                  }
                  flush() {}
                  close() {}
                }
                class __QinJavaIoBufferedWriter {
                  constructor(writer) {
                    this.writer = writer;
                  }
                  write(message) {
                    if (this.writer && typeof this.writer.write === "function") {
                      this.writer.write(message);
                    }
                  }
                  newLine() {
                    this.write("\\n");
                  }
                  flush() {
                    if (this.writer && typeof this.writer.flush === "function") {
                      this.writer.flush();
                    }
                  }
                  close() {
                    if (this.writer && typeof this.writer.close === "function") {
                      this.writer.close();
                    }
                  }
                }
                """);
    }

    private void emitJavaTimeRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaTimeLocalDateTime") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaTimeFormatDateTimeFormatter {
                  constructor(pattern) {
                    this.__pattern = String(pattern == null ? "" : pattern);
                  }
                  static ofPattern(pattern) {
                    return new __QinJavaTimeFormatDateTimeFormatter(pattern);
                  }
                  format(value) {
                    if (value != null && typeof value.format === "function") {
                      return value.format(this);
                    }
                    return __QinJavaTimeLocalDateTime.__formatDate(new Date(), this.__pattern);
                  }
                }
                class __QinJavaTimeLocalDateTime {
                  constructor(date) {
                    this.__date = date instanceof Date ? date : new Date(date == null ? Date.now() : date);
                  }
                  static now() {
                    const fixed = globalThis.__qinJavaFixedNow;
                    return new __QinJavaTimeLocalDateTime(fixed == null ? new Date() : new Date(fixed));
                  }
                  format(formatter) {
                    const pattern = formatter == null ? "" : formatter.__pattern;
                    return __QinJavaTimeLocalDateTime.__formatDate(this.__date, pattern);
                  }
                  static __pad(value, width) {
                    let text = String(value);
                    while (text.length < width) {
                      text = "0" + text;
                    }
                    return text;
                  }
                  static __formatDate(date, pattern) {
                    const tokens = {
                      yyyy: String(date.getFullYear()),
                      MM: __QinJavaTimeLocalDateTime.__pad(date.getMonth() + 1, 2),
                      dd: __QinJavaTimeLocalDateTime.__pad(date.getDate(), 2),
                      HH: __QinJavaTimeLocalDateTime.__pad(date.getHours(), 2),
                      mm: __QinJavaTimeLocalDateTime.__pad(date.getMinutes(), 2),
                      ss: __QinJavaTimeLocalDateTime.__pad(date.getSeconds(), 2)
                    };
                    let out = String(pattern == null || pattern.length === 0 ? "yyyy-MM-ddTHH:mm:ss" : pattern);
                    out = out.split("yyyy").join(tokens.yyyy);
                    out = out.split("MM").join(tokens.MM);
                    out = out.split("dd").join(tokens.dd);
                    out = out.split("HH").join(tokens.HH);
                    out = out.split("mm").join(tokens.mm);
                    out = out.split("ss").join(tokens.ss);
                    return out;
                  }
                }
                """);
    }

    private void emitJavaUtilArrayListRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaUtilArrayList") >= 0) {
            return;
        }
        emitJavaHashRuntimeHelpers(js);
        js.append("""
                class __QinJavaUtilArrayList {
                  constructor(initialValues) {
                    this.__items = [];
                    if (initialValues != null) {
                      const values = initialValues instanceof __QinJavaUtilArrayList
                        ? initialValues.__items
                        : initialValues;
                      for (const item of values) {
                        this.__items.push(item);
                      }
                    }
                  }
                  add(value) {
                    this.__items.push(value);
                    return true;
                  }
                  addAll(values) {
                    const source = values instanceof __QinJavaUtilArrayList
                      ? values.__items
                      : Array.from(values);
                    for (const item of source) {
                      this.__items.push(item);
                    }
                    return source.length > 0;
                  }
                  get(index) {
                    return this.__items[index];
                  }
                  set(index, value) {
                    const previous = this.__items[index];
                    this.__items[index] = value;
                    return previous;
                  }
                  remove(index) {
                    return this.__items.splice(index, 1)[0];
                  }
                  size() {
                    return this.__items.length;
                  }
                  indexOf(value) {
                    for (let index = 0; index < this.__items.length; index++) {
                      if (__qin_java_hash_key_equals__(this.__items[index], value)) return index;
                    }
                    return -1;
                  }
                  isEmpty() {
                    return this.__items.length === 0;
                  }
                  clear() {
                    this.__items.length = 0;
                  }
                  sort(comparator) {
                    this.__items.sort(comparator);
                  }
                  subList(fromIndex, toIndex) {
                    return new __QinJavaUtilUnmodifiableList(this.__items.slice(fromIndex, toIndex));
                  }
                  toArray() {
                    return this.__items.slice();
                  }
                  stream() {
                    return new __QinJavaUtilStream(this.__items);
                  }
                  [Symbol.iterator]() {
                    return this.__items[Symbol.iterator]();
                  }
                }
                class __QinJavaUtilUnmodifiableList {
                  constructor(source) {
                    this.__source = source == null ? [] : source;
                  }
                  __values() {
                    return this.__source instanceof __QinJavaUtilArrayList
                      ? this.__source.__items
                      : Array.from(this.__source);
                  }
                  add() {
                    throw new TypeError("java.util.List is unmodifiable");
                  }
                  addAll() {
                    throw new TypeError("java.util.List is unmodifiable");
                  }
                  get(index) {
                    return this.__values()[index];
                  }
                  remove() {
                    throw new TypeError("java.util.List is unmodifiable");
                  }
                  set() {
                    throw new TypeError("java.util.List is unmodifiable");
                  }
                  subList(fromIndex, toIndex) {
                    return new __QinJavaUtilUnmodifiableList(this.__values().slice(fromIndex, toIndex));
                  }
                  size() {
                    return this.__values().length;
                  }
                  indexOf(value) {
                    const values = this.__values();
                    for (let index = 0; index < values.length; index++) {
                      if (__qin_java_hash_key_equals__(values[index], value)) return index;
                    }
                    return -1;
                  }
                  isEmpty() {
                    return this.__values().length === 0;
                  }
                  clear() {
                    throw new TypeError("java.util.List is unmodifiable");
                  }
                  sort() {
                    throw new TypeError("java.util.List is unmodifiable");
                  }
                  toArray() {
                    return this.__values().slice();
                  }
                  stream() {
                    return new __QinJavaUtilStream(this.__values());
                  }
                  [Symbol.iterator]() {
                    return this.__values()[Symbol.iterator]();
                  }
                }
                """);
    }

    private void emitJavaUtilListRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("const __QinJavaUtilList") >= 0) {
            return;
        }
        emitJavaUtilArrayListRuntime(js);
        js.append("""
                const __QinJavaUtilList = {
                  of(...values) {
                    return new __QinJavaUtilUnmodifiableList(values);
                  }
                };
                """);
    }

    private void emitJavaUtilHashSetRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaUtilHashSet") >= 0) {
            return;
        }
        emitJavaHashRuntimeHelpers(js);
        js.append("""
                class __QinJavaUtilHashSet {
                  constructor(initialValues) {
                    const __QinJsMap = __qin_builtin_constructor__("Map");
                    this.__buckets = new __QinJsMap();
                    this.__size = 0;
                    if (initialValues != null) {
                      for (const value of initialValues) {
                        this.add(value);
                      }
                    }
                  }
                  __bucket(value, create) {
                    const hash = __qin_java_hash_key__(value);
                    let bucket = this.__buckets.get(hash);
                    if (bucket == null && create) {
                      bucket = [];
                      this.__buckets.set(hash, bucket);
                    }
                    return bucket;
                  }
                  __findEntry(value) {
                    const bucket = this.__bucket(value, false);
                    if (bucket == null) {
                      return null;
                    }
                    for (let index = 0; index < bucket.length; index++) {
                      if (__qin_java_hash_key_equals__(bucket[index], value)) {
                        return { bucket, index, value: bucket[index] };
                      }
                    }
                    return null;
                  }
                  add(value) {
                    if (this.__findEntry(value) != null) {
                      return false;
                    }
                    this.__bucket(value, true).push(value);
                    this.__size++;
                    return true;
                  }
                  contains(value) {
                    return this.__findEntry(value) != null;
                  }
                  remove(value) {
                    const found = this.__findEntry(value);
                    if (found == null) {
                      return false;
                    }
                    found.bucket.splice(found.index, 1);
                    this.__size--;
                    return true;
                  }
                  size() {
                    return this.__size;
                  }
                  isEmpty() {
                    return this.__size === 0;
                  }
                  clear() {
                    this.__buckets.clear();
                    this.__size = 0;
                  }
                  toArray() {
                    const values = [];
                    for (const bucket of this.__buckets.values()) {
                      for (const value of bucket) {
                        values.push(value);
                      }
                    }
                    return values;
                  }
                  [Symbol.iterator]() {
                    return this.toArray()[Symbol.iterator]();
                  }
                }
                class __QinJavaUtilUnmodifiableSet {
                  constructor(source) {
                    this.__source = source == null ? [] : source;
                  }
                  __values() {
                    const __QinJsSet = __qin_builtin_constructor__("Set");
                    if (this.__source instanceof __QinJsSet) {
                      return this.__source;
                    }
                    return new __QinJsSet(this.__source);
                  }
                  add() {
                    throw new TypeError("java.util.Set is unmodifiable");
                  }
                  contains(value) {
                    if (this.__source instanceof __QinJavaUtilHashSet) {
                      return this.__source.contains(value);
                    }
                    return this.__values().has(value);
                  }
                  remove() {
                    throw new TypeError("java.util.Set is unmodifiable");
                  }
                  size() {
                    if (this.__source instanceof __QinJavaUtilHashSet) {
                      return this.__source.size();
                    }
                    return this.__values().size;
                  }
                  isEmpty() {
                    if (this.__source instanceof __QinJavaUtilHashSet) {
                      return this.__source.isEmpty();
                    }
                    return this.__values().size === 0;
                  }
                  clear() {
                    throw new TypeError("java.util.Set is unmodifiable");
                  }
                  toArray() {
                    if (this.__source instanceof __QinJavaUtilHashSet) {
                      return this.__source.toArray();
                    }
                    return Array.from(this.__values());
                  }
                  [Symbol.iterator]() {
                    if (this.__source instanceof __QinJavaUtilHashSet) {
                      return this.__source[Symbol.iterator]();
                    }
                    return this.__values()[Symbol.iterator]();
                  }
                }
                """);
    }

    private void emitJavaUtilSetRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("const __QinJavaUtilSet") >= 0) {
            return;
        }
        emitJavaUtilHashSetRuntime(js);
        js.append("""
                const __QinJavaUtilSet = {
                  of(...values) {
                    return new __QinJavaUtilUnmodifiableSet(values);
                  }
                };
                """);
    }

    private void emitJavaHashRuntimeHelpers(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime(
                    "__qin_java_string_hash_code__",
                    "__qin_java_identity_hash_code__",
                    "__qin_java_value_hash_code__",
                    "__qin_java_values_equal__",
                    "__qin_java_hash_key__",
                    "__qin_java_hash_key_equals__");
            return;
        }

        if (js.indexOf("function __qin_java_value_hash_code__") >= 0) {
            return;
        }
        js.append("""
                const __qin_java_hash_identity_ids__ = new (__qin_builtin_constructor__("WeakMap"))();
                let __qin_java_hash_identity_next__ = 1;
                function __qin_java_string_hash_code__(value) {
                  let hash = 0;
                  for (let index = 0; index < value.length; index++) {
                    hash = hash * 31 + value.charCodeAt(index);
                  }
                  return hash;
                }
                function __qin_java_identity_hash_code__(value) {
                  if (!__qin_java_hash_identity_ids__.has(value)) {
                    __qin_java_hash_identity_ids__.set(value, __qin_java_hash_identity_next__++);
                  }
                  return __qin_java_hash_identity_ids__.get(value);
                }
                function __qin_java_value_hash_code__(value) {
                  if (value == null) {
                    return 0;
                  }
                  const valueType = typeof value;
                  if (valueType === "string") {
                    return __qin_java_string_hash_code__(value);
                  }
                  if (valueType === "boolean") {
                    return value ? 1231 : 1237;
                  }
                  if (valueType === "number") {
                    return value;
                  }
                  if (valueType === "object" || valueType === "function") {
                    if (typeof value.hashCode === "function") {
                      return value.hashCode();
                    }
                    return __qin_java_identity_hash_code__(value);
                  }
                  return __qin_java_string_hash_code__(String(value));
                }
                function __qin_java_values_equal__(left, right) {
                  if (left === right || (left !== left && right !== right)) {
                    return true;
                  }
                  if (left == null || right == null) {
                    return false;
                  }
                  if ((typeof left === "object" || typeof left === "function")
                      && typeof left.equals === "function") {
                    return left.equals(right) === true;
                  }
                  return false;
                }
                function __qin_java_hash_key__(key) {
                  return "hash:" + String(__qin_java_value_hash_code__(key));
                }
                function __qin_java_hash_key_equals__(left, right) {
                  return __qin_java_values_equal__(left, right);
                }
                """);
    }

    private void emitJavaUtilArraysRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("const __QinJavaUtilArrays") >= 0) {
            return;
        }
        emitJavaUtilArrayListRuntime(js);
        emitJavaHashRuntimeHelpers(js);
        js.append("""
                const __QinJavaUtilArrays = {
                  asList(...values) {
                    if (values.length === 1 && Array.isArray(values[0])) {
                      return new __QinJavaUtilArrayList(values[0]);
                    }
                    return new __QinJavaUtilArrayList(values);
                  },
                  __items(value) {
                    if (value == null) {
                      return null;
                    }
                    if (value instanceof __QinJavaUtilArrayList) {
                      return value.__items;
                    }
                    if (Array.isArray(value)) {
                      return value;
                    }
                    if (typeof value[Symbol.iterator] === "function" && typeof value !== "string") {
                      return Array.from(value);
                    }
                    return [value];
                  },
                  equals(left, right) {
                    if (left === right) {
                      return true;
                    }
                    const leftItems = this.__items(left);
                    const rightItems = this.__items(right);
                    if (leftItems == null || rightItems == null || leftItems.length !== rightItems.length) {
                      return false;
                    }
                    for (let index = 0; index < leftItems.length; index++) {
                      if (!__qin_java_values_equal__(leftItems[index], rightItems[index])) {
                        return false;
                      }
                    }
                    return true;
                  },
                  hashCode(value) {
                    const items = this.__items(value);
                    if (items == null) {
                      return 0;
                    }
                    let result = 1;
                    for (const item of items) {
                      result = result * 31 + __qin_java_value_hash_code__(item);
                    }
                    return result;
                  },
                  deepToString(value) {
                    const format = (item, seen) => {
                      if (item == null) {
                        return "null";
                      }
                      const arrayListItems = item instanceof __QinJavaUtilArrayList ? item.__items : null;
                      if (Array.isArray(item) || arrayListItems != null
                          || (item != null && typeof item[Symbol.iterator] === "function"
                          && typeof item !== "string")) {
                        if (seen.indexOf(item) >= 0) {
                          return "[...]";
                        }
                        seen.push(item);
                        const parts = [];
                        const iterable = arrayListItems == null ? item : arrayListItems;
                        for (const child of iterable) {
                          parts.push(format(child, seen));
                        }
                        seen.pop();
                        return "[" + parts.join(", ") + "]";
                      }
                      return String(item);
                    };
                    return format(value, []);
                  }
                };
                """);
    }

    private void emitJavaUtilCollectionsRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("const __QinJavaUtilCollections") >= 0) {
            return;
        }
        emitJavaUtilArrayListRuntime(js);
        emitJavaUtilHashSetRuntime(js);
        emitJavaUtilHashMapRuntime(js);
        js.append("""
                class __QinJavaUtilUnmodifiableMap {
                  constructor(source) {
                    const __QinJsMap = __qin_builtin_constructor__("Map");
                    this.__source = source == null ? new __QinJsMap() : source;
                  }
                  __values() {
                    const __QinJsMap = __qin_builtin_constructor__("Map");
                    if (this.__source instanceof __QinJsMap) {
                      return this.__source;
                    }
                    const entries = new __QinJsMap();
                    if (this.__source != null && typeof this.__source[Symbol.iterator] === "function") {
                      for (const entry of this.__source) {
                        entries.set(entry[0], entry[1]);
                      }
                    }
                    return entries;
                  }
                  put() {
                    throw new TypeError("java.util.Map is unmodifiable");
                  }
                  get(key) {
                    if (this.__source instanceof __QinJavaUtilHashMap) {
                      return this.__source.get(key);
                    }
                    const entries = this.__values();
                    return entries.has(key) ? entries.get(key) : null;
                  }
                  getOrDefault(key, defaultValue) {
                    if (this.__source instanceof __QinJavaUtilHashMap) {
                      return this.__source.getOrDefault(key, defaultValue);
                    }
                    const entries = this.__values();
                    return entries.has(key) ? entries.get(key) : defaultValue;
                  }
                  putIfAbsent() {
                    throw new TypeError("java.util.Map is unmodifiable");
                  }
                  values() {
                    if (this.__source instanceof __QinJavaUtilHashMap) {
                      return new __QinJavaUtilUnmodifiableList(this.__source.values());
                    }
                    return new __QinJavaUtilUnmodifiableList(Array.from(this.__values().values()));
                  }
                  computeIfAbsent() {
                    throw new TypeError("java.util.Map is unmodifiable");
                  }
                  merge() {
                    throw new TypeError("java.util.Map is unmodifiable");
                  }
                  containsKey(key) {
                    if (this.__source instanceof __QinJavaUtilHashMap) {
                      return this.__source.containsKey(key);
                    }
                    return this.__values().has(key);
                  }
                  remove() {
                    throw new TypeError("java.util.Map is unmodifiable");
                  }
                  size() {
                    if (this.__source instanceof __QinJavaUtilHashMap) {
                      return this.__source.size();
                    }
                    return this.__values().size;
                  }
                  isEmpty() {
                    if (this.__source instanceof __QinJavaUtilHashMap) {
                      return this.__source.isEmpty();
                    }
                    return this.__values().size === 0;
                  }
                  clear() {
                    throw new TypeError("java.util.Map is unmodifiable");
                  }
                }
                const __QinJavaUtilCollections = {
                  unmodifiableList(value) {
                    return new __QinJavaUtilUnmodifiableList(value);
                  },
                  emptySet() {
                    return new __QinJavaUtilUnmodifiableSet([]);
                  },
                  unmodifiableSet(value) {
                    return new __QinJavaUtilUnmodifiableSet(value);
                  },
                  unmodifiableMap(value) {
                    return new __QinJavaUtilUnmodifiableMap(value);
                  }
                };
                """);
    }

    private void emitJavaUtilHashMapRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaUtilHashMap") >= 0) {
            return;
        }
        emitJavaUtilArrayListRuntime(js);
        emitJavaHashRuntimeHelpers(js);
        js.append("""
                class __QinJavaUtilHashMap {
                  constructor(initialEntries) {
                    const __QinJsMap = __qin_builtin_constructor__("Map");
                    this.__buckets = new __QinJsMap();
                    this.__size = 0;
                    if (initialEntries != null) {
                      for (const entry of initialEntries) {
                        this.put(entry[0], entry[1]);
                      }
                    }
                  }
                  __bucket(key, create) {
                    const hash = __qin_java_hash_key__(key);
                    let bucket = this.__buckets.get(hash);
                    if (bucket == null && create) {
                      bucket = [];
                      this.__buckets.set(hash, bucket);
                    }
                    return bucket;
                  }
                  __findEntry(key) {
                    const bucket = this.__bucket(key, false);
                    if (bucket == null) {
                      return null;
                    }
                    for (let index = 0; index < bucket.length; index++) {
                      const entry = bucket[index];
                      if (__qin_java_hash_key_equals__(entry.key, key)) {
                        return { bucket, index, entry };
                      }
                    }
                    return null;
                  }
                  put(key, value) {
                    const found = this.__findEntry(key);
                    if (found != null) {
                      const previous = found.entry.value;
                      found.entry.value = value;
                      return previous;
                    }
                    this.__bucket(key, true).push({ key, value });
                    this.__size++;
                    return null;
                  }
                  get(key) {
                    const found = this.__findEntry(key);
                    return found == null ? null : found.entry.value;
                  }
                  getOrDefault(key, defaultValue) {
                    const found = this.__findEntry(key);
                    return found == null ? defaultValue : found.entry.value;
                  }
                  putIfAbsent(key, value) {
                    const found = this.__findEntry(key);
                    if (found == null) {
                      this.__bucket(key, true).push({ key, value });
                      this.__size++;
                      return null;
                    }
                    const previous = found.entry.value;
                    if (previous == null) {
                      found.entry.value = value;
                    }
                    return previous;
                  }
                  values() {
                    const values = [];
                    for (const bucket of this.__buckets.values()) {
                      for (const entry of bucket) {
                        values.push(entry.value);
                      }
                    }
                    return new __QinJavaUtilArrayList(values);
                  }
                  computeIfAbsent(key, mappingFunction) {
                    const found = this.__findEntry(key);
                    if (found == null || found.entry.value == null) {
                      const value = mappingFunction(key);
                      if (found == null) {
                        this.__bucket(key, true).push({ key, value });
                        this.__size++;
                      } else {
                        found.entry.value = value;
                      }
                      return value;
                    }
                    return found.entry.value;
                  }
                  merge(key, value, remappingFunction) {
                    const found = this.__findEntry(key);
                    if (found == null) {
                      this.__bucket(key, true).push({ key, value });
                      this.__size++;
                      return value;
                    }
                    if (found.entry.value == null) {
                      found.entry.value = value;
                      return value;
                    }
                    const nextValue = remappingFunction(found.entry.value, value);
                    if (nextValue == null) {
                      found.bucket.splice(found.index, 1);
                      this.__size--;
                      return null;
                    }
                    found.entry.value = nextValue;
                    return nextValue;
                  }
                  containsKey(key) {
                    return this.__findEntry(key) != null;
                  }
                  remove(key) {
                    const found = this.__findEntry(key);
                    if (found == null) {
                      return null;
                    }
                    const previous = found.entry.value;
                    found.bucket.splice(found.index, 1);
                    this.__size--;
                    return previous;
                  }
                  size() {
                    return this.__size;
                  }
                  isEmpty() {
                    return this.__size === 0;
                  }
                  clear() {
                    this.__buckets.clear();
                    this.__size = 0;
                  }
                }
                """);
    }

    private void emitJavaUtilObjectsRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("const __QinJavaUtilObjects") >= 0) {
            return;
        }
        js.append("""
                const __QinJavaUtilObjects = {
                  toString(value, nullDefault) {
                    if (value == null) {
                      return arguments.length >= 2 ? nullDefault : "null";
                    }
                    return String(value);
                  }
                };
                """);
    }

    private void emitJavaUtilOptionalRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaUtilOptionalValue") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaUtilOptionalValue {
                  constructor(present, value) {
                    this.__present = present;
                    this.__value = value;
                  }
                  isPresent() {
                    return this.__present;
                  }
                  isEmpty() {
                    return !this.__present;
                  }
                  get() {
                    if (!this.__present) {
                      throw new Error("No value present");
                    }
                    return this.__value;
                  }
                  orElse(other) {
                    return this.__present ? this.__value : other;
                  }
                  orElseGet(supplier) {
                    return this.__present ? this.__value : supplier();
                  }
                }
                const __QinJavaUtilOptional = {
                  empty() {
                    return new __QinJavaUtilOptionalValue(false, null);
                  },
                  of(value) {
                    if (value == null) {
                      throw new Error("Optional.of requires a non-null value");
                    }
                    return new __QinJavaUtilOptionalValue(true, value);
                  },
                  ofNullable(value) {
                    return value == null
                      ? new __QinJavaUtilOptionalValue(false, null)
                      : new __QinJavaUtilOptionalValue(true, value);
                  }
                };
                """);
    }

    private void emitJavaUtilStreamRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaUtilStream") >= 0) {
            return;
        }
        emitJavaUtilArrayListRuntime(js);
        emitJavaUtilOptionalRuntime(js);
        js.append("""
                class __QinJavaUtilStream {
                  constructor(source) {
                    this.__items = source == null ? [] : Array.from(source);
                  }
                  filter(predicate) {
                    return new __QinJavaUtilStream(this.__items.filter(item => predicate(item)));
                  }
                  map(mapper) {
                    return new __QinJavaUtilStream(this.__items.map(item => mapper(item)));
                  }
                  anyMatch(predicate) {
                    return this.__items.some(item => predicate(item));
                  }
                  findFirst() {
                    return this.__items.length === 0
                      ? __QinJavaUtilOptional.empty()
                      : __QinJavaUtilOptional.ofNullable(this.__items[0]);
                  }
                  collect(collector) {
                    if (collector == null || typeof collector.__collect !== "function") {
                      throw new TypeError("java.util.stream.Stream.collect requires a Qin collector");
                    }
                    return collector.__collect(this.__items);
                  }
                  toArray() {
                    return this.__items.slice();
                  }
                  [Symbol.iterator]() {
                    return this.__items[Symbol.iterator]();
                  }
                }
                const __QinJavaUtilStreamCollectors = {
                  toList() {
                    return {
                      __collect(items) {
                        return new __QinJavaUtilArrayList(items);
                      }
                    };
                  },
                  joining(delimiter = "") {
                    return {
                      __collect(items) {
                        return items.map(item => String(item)).join(String(delimiter));
                      }
                    };
                  }
                };
                """);
    }

    private void emitJavaUtilConcurrentAtomicLongRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaUtilConcurrentAtomicLong") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaUtilConcurrentAtomicLong {
                  constructor(initialValue = 0) {
                    this.__value = Number(initialValue);
                  }
                  get() {
                    return this.__value;
                  }
                  set(value) {
                    this.__value = Number(value);
                  }
                  incrementAndGet() {
                    this.__value += 1;
                    return this.__value;
                  }
                  getAndIncrement() {
                    const previous = this.__value;
                    this.__value += 1;
                    return previous;
                  }
                  addAndGet(delta) {
                    this.__value += Number(delta);
                    return this.__value;
                  }
                  getAndAdd(delta) {
                    const previous = this.__value;
                    this.__value += Number(delta);
                    return previous;
                  }
                  compareAndSet(expectedValue, newValue) {
                    if (this.__value !== Number(expectedValue)) {
                      return false;
                    }
                    this.__value = Number(newValue);
                    return true;
                  }
                }
                """);
    }

    private void emitCaffeineRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinCaffeineCache") >= 0) {
            return;
        }
        js.append("""
                const __QinCaffeineRemovalCause = {
                  SIZE: {
                    wasEvicted() {
                      return true;
                    }
                  },
                  EXPLICIT: {
                    wasEvicted() {
                      return false;
                    }
                  }
                };
                class __QinCaffeineCache {
                  constructor(maximumSize, removalListener) {
                    this.__maximumSize = maximumSize == null ? Infinity : maximumSize;
                    this.__removalListener = removalListener;
                    this.__entries = new Map();
                  }
                  getIfPresent(key) {
                    if (!this.__entries.has(key)) {
                      return null;
                    }
                    const value = this.__entries.get(key);
                    this.__entries.delete(key);
                    this.__entries.set(key, value);
                    return value;
                  }
                  put(key, value) {
                    if (this.__entries.has(key)) {
                      this.__entries.delete(key);
                    }
                    this.__entries.set(key, value);
                    while (this.__entries.size > this.__maximumSize) {
                      let oldestKey = null;
                      for (const entryKey of this.__entries.keys()) {
                        oldestKey = entryKey;
                        break;
                      }
                      const oldestValue = this.__entries.get(oldestKey);
                      this.__entries.delete(oldestKey);
                      if (this.__removalListener != null) {
                        this.__removalListener(oldestKey, oldestValue, __QinCaffeineRemovalCause.SIZE);
                      }
                    }
                  }
                  invalidate(key) {
                    if (!this.__entries.has(key)) {
                      return;
                    }
                    const value = this.__entries.get(key);
                    this.__entries.delete(key);
                    if (this.__removalListener != null) {
                      this.__removalListener(key, value, __QinCaffeineRemovalCause.EXPLICIT);
                    }
                  }
                  invalidateAll() {
                    for (const [key, value] of Array.from(this.__entries.entries())) {
                      this.__entries.delete(key);
                      if (this.__removalListener != null) {
                        this.__removalListener(key, value, __QinCaffeineRemovalCause.EXPLICIT);
                      }
                    }
                  }
                  estimatedSize() {
                    return this.__entries.size;
                  }
                  stats() {
                    return {};
                  }
                }
                class __QinCaffeineBuilder {
                  constructor() {
                    this.__maximumSize = Infinity;
                    this.__removalListener = null;
                  }
                  maximumSize(value) {
                    this.__maximumSize = value;
                    return this;
                  }
                  expireAfterAccess() {
                    return this;
                  }
                  expireAfterWrite() {
                    return this;
                  }
                  removalListener(listener) {
                    this.__removalListener = listener;
                    return this;
                  }
                  recordStats() {
                    return this;
                  }
                  build() {
                    return new __QinCaffeineCache(this.__maximumSize, this.__removalListener);
                  }
                }
                const __QinCaffeine = {
                  newBuilder() {
                    return new __QinCaffeineBuilder();
                  }
                };
                """);
    }

    private void emitJavaUtilRegexRuntime(StringBuilder js) {
        if (externalJavaSdkRuntime) {
            return;
        }

        if (js.indexOf("class __QinJavaUtilRegexPattern") >= 0) {
            return;
        }
        js.append("""
                class __QinJavaUtilRegexPattern {
                  constructor(source, flags) {
                    this.__source = String(source);
                    this.__flags = flags == null ? 0 : (flags | 0);
                  }
                  static compile(source, flags = 0) {
                    return new __QinJavaUtilRegexPattern(source, flags);
                  }
                  static quote(literal) {
                    const text = String(literal);
                    return "\\\\Q" + text.replace(/\\\\E/g, "\\\\E\\\\\\\\E\\\\Q") + "\\\\E";
                  }
                  matcher(input) {
                    return new __QinJavaUtilRegexMatcher(this, String(input));
                  }
                  pattern() {
                    return this.__source;
                  }
                  flags() {
                    return this.__flags;
                  }
                  __jsFlags(extraFlags = "") {
                    let flags = "";
                    if ((this.__flags & __QinJavaUtilRegexPattern.CASE_INSENSITIVE) !== 0) flags += "i";
                    if ((this.__flags & __QinJavaUtilRegexPattern.MULTILINE) !== 0) flags += "m";
                    if ((this.__flags & __QinJavaUtilRegexPattern.DOTALL) !== 0) flags += "s";
                    for (const ch of String(extraFlags)) {
                      if (flags.indexOf(ch) < 0) flags += ch;
                    }
                    return flags;
                  }
                  __regexp(extraFlags = "") {
                    return __qin_java_pattern_regexp__(this.__source, this.__jsFlags(extraFlags));
                  }
                }
                __QinJavaUtilRegexPattern.UNIX_LINES = 1;
                __QinJavaUtilRegexPattern.CASE_INSENSITIVE = 2;
                __QinJavaUtilRegexPattern.COMMENTS = 4;
                __QinJavaUtilRegexPattern.MULTILINE = 8;
                __QinJavaUtilRegexPattern.LITERAL = 16;
                __QinJavaUtilRegexPattern.DOTALL = 32;
                __QinJavaUtilRegexPattern.UNICODE_CASE = 64;
                __QinJavaUtilRegexPattern.CANON_EQ = 128;
                __QinJavaUtilRegexPattern.UNICODE_CHARACTER_CLASS = 256;
                class __QinJavaUtilRegexMatcher {
                  constructor(pattern, input) {
                    this.__pattern = pattern;
                    this.__input = String(input);
                    this.__regionStart = 0;
                    this.__regionEnd = this.__input.length;
                    this.__searchIndex = 0;
                    this.__lastMatch = null;
                    this.__appendPosition = 0;
                  }
                  static quoteReplacement(text) {
                    return String(text).replace(/\\\\/g, "\\\\\\\\").replace(/\\$/g, "\\\\\\$");
                  }
                  region(start, end) {
                    this.__regionStart = Math.max(0, start | 0);
                    this.__regionEnd = Math.min(this.__input.length, Math.max(this.__regionStart, end | 0));
                    this.__searchIndex = this.__regionStart;
                    this.__lastMatch = null;
                    return this;
                  }
                  lookingAt() {
                    return this.__matchAtRegionStart(false);
                  }
                  matches() {
                    return this.__matchAtRegionStart(true);
                  }
                  find(start) {
                    const from = arguments.length > 0 ? Math.max(this.__regionStart, start | 0) : this.__searchIndex;
                    const boundedFrom = Math.min(Math.max(from, this.__regionStart), this.__regionEnd);
                    const re = this.__pattern.__regexp();
                    const text = this.__input.slice(boundedFrom, this.__regionEnd);
                    const match = re.exec(text);
                    if (match == null) {
                      this.__lastMatch = null;
                      this.__searchIndex = this.__regionEnd;
                      return false;
                    }
                    this.__storeMatch(match, boundedFrom + (match.index == null ? 0 : match.index));
                    this.__searchIndex = this.__lastMatch.end === this.__lastMatch.start
                      ? Math.min(this.__lastMatch.end + 1, this.__regionEnd)
                      : this.__lastMatch.end;
                    return true;
                  }
                  group(index = 0) {
                    if (this.__lastMatch == null) {
                      throw new Error("No match available");
                    }
                    const value = this.__lastMatch.groups[index | 0];
                    return value == null ? null : value;
                  }
                  groupCount() {
                    return this.__lastMatch == null ? 0 : Math.max(0, this.__lastMatch.groups.length - 1);
                  }
                  start() {
                    if (this.__lastMatch == null) throw new Error("No match available");
                    return this.__lastMatch.start;
                  }
                  end() {
                    if (this.__lastMatch == null) throw new Error("No match available");
                    return this.__lastMatch.end;
                  }
                  replaceAll(replacement) {
                    return this.__input.replace(this.__pattern.__regexp("g"), String(replacement));
                  }
                  appendReplacement(buffer, replacement) {
                    if (this.__lastMatch == null) {
                      throw new Error("No match available");
                    }
                    const text = this.__input.slice(this.__appendPosition, this.__lastMatch.start) + String(replacement);
                    this.__append(buffer, text);
                    this.__appendPosition = this.__lastMatch.end;
                    return this;
                  }
                  appendTail(buffer) {
                    this.__append(buffer, this.__input.slice(this.__appendPosition));
                    this.__appendPosition = this.__input.length;
                    return buffer;
                  }
                  __matchAtRegionStart(requireFullRegion) {
                    const re = this.__pattern.__regexp("y");
                    const text = this.__input.slice(this.__regionStart, this.__regionEnd);
                    const match = re.exec(text);
                    if (match == null) {
                      this.__lastMatch = null;
                      return false;
                    }
                    this.__storeMatch(match, this.__regionStart);
                    return !requireFullRegion || this.__lastMatch.end === this.__regionEnd;
                  }
                  __storeMatch(match, absoluteStart) {
                    const groups = [];
                    for (let index = 0; index < match.length; index++) {
                      groups.push(match[index] == null ? null : match[index]);
                    }
                    this.__lastMatch = {
                      groups,
                      start: absoluteStart,
                      end: absoluteStart + String(match[0] == null ? "" : match[0]).length
                    };
                  }
                  __append(buffer, text) {
                    if (buffer != null && typeof buffer.append === "function") {
                      buffer.append(text);
                      return;
                    }
                    throw new TypeError("Matcher append target must support append(value)");
                  }
                }
                """);
    }

    private void emitJsImports(StringBuilder js, List<QinIrJsImport> jsImports) {
        if (jsImports == null || jsImports.isEmpty()) {
            return;
        }

        Map<String, List<QinIrJsImport>> grouped = new LinkedHashMap<>();
        for (QinIrJsImport jsImport : jsImports) {
            grouped.computeIfAbsent(jsImport.moduleName(), ignored -> new java.util.ArrayList<>())
                    .add(jsImport);
        }

        for (Map.Entry<String, List<QinIrJsImport>> entry : grouped.entrySet()) {
            List<QinIrJsImport> specs = entry.getValue();
            boolean sideEffectOnly = specs.stream().allMatch(spec -> spec.importedName().isBlank());
            if (sideEffectOnly) {
                js.append("import \"").append(escapeJs(entry.getKey())).append("\";\n");
                continue;
            }

            String defaultLocal = null;
            String namespaceLocal = null;
            List<QinIrJsImport> named = new java.util.ArrayList<>();
            for (QinIrJsImport spec : specs) {
                if ("default".equals(spec.importedName())) {
                    defaultLocal = spec.localName();
                } else if ("*".equals(spec.importedName())) {
                    namespaceLocal = spec.localName();
                } else if (!spec.importedName().isBlank()) {
                    named.add(spec);
                }
            }

            js.append("import ");
            boolean hasPrefix = false;
            if (defaultLocal != null && !defaultLocal.isBlank()) {
                js.append(defaultLocal);
                hasPrefix = true;
            }
            if (namespaceLocal != null && !namespaceLocal.isBlank()) {
                if (hasPrefix) {
                    js.append(", ");
                }
                js.append("* as ").append(namespaceLocal);
                hasPrefix = true;
            }
            if (!named.isEmpty()) {
                if (hasPrefix) {
                    js.append(", ");
                }
                js.append("{ ");
                for (int i = 0; i < named.size(); i++) {
                    QinIrJsImport spec = named.get(i);
                    js.append(spec.importedName());
                    if (!spec.importedName().equals(spec.localName())) {
                        js.append(" as ").append(spec.localName());
                    }
                    if (i < named.size() - 1) {
                        js.append(", ");
                    }
                }
                js.append(" }");
            }

            if (!hasPrefix && named.isEmpty()) {
                js.append("\"").append(escapeJs(entry.getKey())).append("\";\n");
                continue;
            }

            js.append(" from \"").append(escapeJs(entry.getKey())).append("\";\n");
        }
        js.append("\n");
    }

    private void emitBuiltinRuntimeHelpers(StringBuilder js, QinIrProgram program) {
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime(
                    "__qin_builtin_constructor__",
                    "__qin_java_pattern_regexp__",
                    "__QinJavaLangString",
                    "__qin_java_functional",
                    "__qin_java_class_info__",
                    "__qin_binary__",
                    "__qin_logical__");
            return;
        }
        js.append("""
                const __qin_builtin_constructor__ = globalThis.__qin_builtin_constructor__ || ((name) => {
                  const ctor = globalThis[name];
                  if (typeof ctor !== "function") {
                    throw new Error("Missing host constructor for Qin generated JS: " + name);
                  }
                  return ctor;
                });
                const __qin_java_pattern_regexp__ = globalThis.__qin_java_pattern_regexp__ || ((source, flags = "") => {
                  const jsSource = String(source).replace(/\\\\Q([\\s\\S]*?)\\\\E/g, (_match, literal) => {
                    return literal.replace(/[.*+?^${}()|[\\]\\\\]/g, "\\\\$&");
                  });
                  let jsFlags = String(flags);
                  if (/\\\\[pP]\\{/.test(jsSource) && !jsFlags.includes("u")) {
                    jsFlags += "u";
                  }
                  return new RegExp(jsSource, jsFlags);
                });
                """);
        emitJavaLangStringRuntime(js);
        js.append("""
                function __qin_java_functional(fn) {
                  if (fn == null || fn.__qinJavaFunctional) return fn;
                  Object.defineProperty(fn, "__qinJavaFunctional", { value: true });
                  fn.get = () => fn();
                  fn.run = () => fn();
                  fn.execute = () => fn();
                  fn.apply = (...args) => fn(...args);
                  fn.accept = (...args) => {
                    fn(...args);
                    return null;
                  };
                  fn.test = (...args) => !!fn(...args);
                  fn.compare = (...args) => fn(...args);
                  return fn;
                }
                function __qin_java_class_info__(ctor) {
                  const className = ctor && ctor.name ? ctor.name : "Object";
                  const simpleName = className.split(".").pop().split("_").pop() || className;
                  let hash = 0;
                  for (let index = 0; index < className.length; index++) {
                    hash = ((hash * 31) + className.charCodeAt(index)) | 0;
                  }
                  const findMethod = (name) => {
                    const candidates = name === "_markParseFail" ? ["_markParseFail", "setParseFail"] : [name];
                    let prototype = ctor == null ? null : ctor.prototype;
                    while (prototype != null) {
                      for (const candidate of candidates) {
                        if (typeof prototype[candidate] === "function") {
                          return {
                            setAccessible() {},
                            invoke(target, ...args) {
                              return target[candidate](...args);
                            }
                          };
                        }
                      }
                      prototype = Object.getPrototypeOf(prototype);
                    }
                    throw new Error("NoSuchMethod: " + className + "." + name);
                  };
                  return {
                    getName() { return className; },
                    getSimpleName() { return simpleName; },
                    getDeclaredConstructor(...__qin_types) {
                      const __qin_ctor = ctor == null ? Object : ctor;
                      return {
                        newInstance(...__qin_args) {
                          return new __qin_ctor(...__qin_args);
                        }
                      };
                    },
                    getConstructor(...__qin_types) {
                      return this.getDeclaredConstructor(...__qin_types);
                    },
                    getMethod(name, ...params) { return findMethod(name); },
                    getDeclaredMethod(name, ...params) { return findMethod(name); },
                    getSuperclass() {
                      const parent = ctor == null || ctor.prototype == null ? null : Object.getPrototypeOf(ctor.prototype);
                      return parent != null && parent.constructor != null && parent.constructor !== Object
                        ? __qin_java_class_info__(parent.constructor)
                        : null;
                    },
                    getField(name) {
                      return {
                        get(target) {
                          const qinField = "__qin_field_" + name;
                          if (target != null && qinField in target) return target[qinField];
                          if (target != null && name in target && typeof target[name] !== "function") return target[name];
                          throw new Error("NoSuchField: " + className + "." + name);
                        }
                      };
                    },
                    equals(other) { return other != null && typeof other.getName === "function" && other.getName() === className; },
                    hashCode() { return hash; },
                    toString() { return "class " + className; }
                  };
                }
                if (Object.prototype.getClass == null) {
                  Object.defineProperty(Object.prototype, "getClass", {
                    value() { return __qin_java_class_info__(this == null ? Object : this.constructor); },
                    configurable: true
                  });
                }
                """);
        boolean usesBinary = program == null || usesBuiltin(program, "__qin_binary__");
        boolean usesLogical = program == null || usesBuiltin(program, "__qin_logical__");
        if (!usesBinary && !usesLogical) {
            return;
        }
        if (usesBinary) {
            js.append("""
                    function __qin_binary__(operator, left, right) {
                      switch (operator) {
                        case "+": return left + right;
                        case "-": return left - right;
                        case "*": return left * right;
                        case "/": return left / right;
                        case "%": return left % right;
                        case "==": return left == right;
                        case "!=": return left != right;
                        case "===": return left === right;
                        case "!==": return left !== right;
                        case "<": return left < right;
                        case "<=": return left <= right;
                        case ">": return left > right;
                        case ">=": return left >= right;
                        case "instanceof": return left instanceof right;
                        case "&&": return left && right;
                        case "||": return left || right;
                        default: throw new Error("Unsupported Qin binary operator: " + operator);
                      }
                    }
                    """);
        }
        if (usesLogical) {
            js.append("""
                    function __qin_logical__(operator, left, right) {
                      switch (operator) {
                        case "&&": return left && right;
                        case "||": return left || right;
                        default: throw new Error("Unsupported Qin logical operator: " + operator);
                      }
                    }
                    """);
        }
    }

    private void emitSubhutiRuleRuntimeHelpers(StringBuilder js, QinIrProgram program) {
        if (externalJavaSdkRuntime) {
            if (program == null || usesSubhutiRuleMethod(program)) {
                requireExternalJavaSdkRuntime("__qin_subhuti_rule_cache_key");
            }
            return;
        }
        if (program != null && !usesSubhutiRuleMethod(program)) {
            return;
        }
        emitJavaHashRuntimeHelpers(js);
        js.append("""
                let __qin_subhuti_next_rule_cache_id = 1;
                const __qin_subhuti_rule_cache_identity_ids = new (__qin_builtin_constructor__("WeakMap"))();
                const __qin_subhuti_rule_cache_value_buckets = new (__qin_builtin_constructor__("Map"))();
                function __qin_subhuti_identity_rule_cache_id(value) {
                  if (!__qin_subhuti_rule_cache_identity_ids.has(value)) {
                    __qin_subhuti_rule_cache_identity_ids.set(value, __qin_subhuti_next_rule_cache_id++);
                  }
                  return __qin_subhuti_rule_cache_identity_ids.get(value);
                }
                function __qin_subhuti_value_rule_cache_id(value) {
                  const hash = __qin_java_hash_key__(value);
                  let bucket = __qin_subhuti_rule_cache_value_buckets.get(hash);
                  if (bucket == null) {
                    bucket = [];
                    __qin_subhuti_rule_cache_value_buckets.set(hash, bucket);
                  }
                  for (const entry of bucket) {
                    if (__qin_java_hash_key_equals__(entry.value, value)) {
                      return entry.id;
                    }
                  }
                  const id = __qin_subhuti_next_rule_cache_id++;
                  bucket.push({ value, id });
                  return id;
                }
                function __qin_subhuti_rule_cache_key(args) {
                  if (args == null || args.length === 0) return "";
                  const format = (value) => {
                    if (value == null) return "null";
                    const type = typeof value;
                    if (type === "string") return value;
                    if (type === "number" || type === "boolean" || type === "bigint") return "" + value;
                    if (Array.isArray(value)) return "[" + value.map(format).join(", ") + "]";
                    if (type === "object" || type === "function") {
                      if (typeof value.hashCode === "function" || typeof value.equals === "function") {
                        return type + "#value:" + __qin_subhuti_value_rule_cache_id(value);
                      }
                      return type + "#identity:" + __qin_subhuti_identity_rule_cache_id(value);
                    }
                    return "" + value;
                  };
                  const parts = [];
                  for (let i = 0; i < args.length; i++) {
                    parts.push(format(args[i]));
                  }
                  return "[" + parts.join(", ") + "]";
                }
                """);
    }

    private boolean usesSubhutiRuleMethod(QinIrProgram program) {
        for (QinIrClassDeclaration classDeclaration : program.classDeclarations()) {
            for (QinIrMethodDeclaration method : classDeclaration.methods()) {
                if (isSubhutiRuleMethod(method)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean usesBuiltin(QinIrProgram program, String methodName) {
        for (QinIrConstDeclaration declaration : program.declarations()) {
            if (usesBuiltin(declaration.initializer(), methodName)) {
                return true;
            }
        }
        for (QinIrExpressionStatement expressionStatement : program.expressionStatements()) {
            if (usesBuiltin(expressionStatement.expression(), methodName)) {
                return true;
            }
        }
        for (QinIrConsoleLogValue consoleValueLog : program.consoleValueLogs()) {
            if (usesBuiltin(consoleValueLog.value(), methodName)) {
                return true;
            }
        }
        for (QinIrConsoleLogJavaStaticCall call : program.javaStaticConsoleLogs()) {
            for (QinIrExpression argument : call.arguments()) {
                if (usesBuiltin(argument, methodName)) {
                    return true;
                }
            }
        }
        for (QinIrJavaInstanceMethodCall call : program.javaInstanceMethodCalls()) {
            for (QinIrExpression argument : call.arguments()) {
                if (usesBuiltin(argument, methodName)) {
                    return true;
                }
            }
        }
        for (QinIrConsoleLogJavaInstanceCall call : program.javaInstanceConsoleLogs()) {
            for (QinIrExpression argument : call.arguments()) {
                if (usesBuiltin(argument, methodName)) {
                    return true;
                }
            }
        }
        for (QinIrClassDeclaration classDeclaration : program.classDeclarations()) {
            for (QinIrFieldDeclaration field : classDeclaration.fields()) {
                if (usesBuiltin(field.initializer(), methodName)) {
                    return true;
                }
            }
            for (QinIrMethodDeclaration method : classDeclaration.methods()) {
                if (usesBuiltin(method.returnExpression(), methodName)) {
                    return true;
                }
                if (usesBuiltin(method.bodyStatements(), methodName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean usesBuiltin(List<QinIrStatement> statements, String methodName) {
        for (QinIrStatement statement : statements) {
            if (statement instanceof QinIrLocalDeclarationStatement localDeclarationStatement) {
                if (usesBuiltin(localDeclarationStatement.initializer(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrStatementExpression statementExpression) {
                if (usesBuiltin(statementExpression.expression(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrReturnStatement returnStatement) {
                if (usesBuiltin(returnStatement.value(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrThrowStatement throwStatement) {
                if (usesBuiltin(throwStatement.value(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrIfStatement ifStatement) {
                if (usesBuiltin(ifStatement.test(), methodName)
                        || usesBuiltin(ifStatement.consequent(), methodName)
                        || usesBuiltin(ifStatement.alternate(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrWhileStatementNode whileStatement) {
                if (usesBuiltin(whileStatement.test(), methodName)
                        || usesBuiltin(whileStatement.body(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrDoWhileStatementNode doWhileStatement) {
                if (usesBuiltin(doWhileStatement.body(), methodName)
                        || usesBuiltin(doWhileStatement.test(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrForStatement forStatement) {
                for (QinIrLocalVariableDeclaration declaration : forStatement.initializerDeclarations()) {
                    if (usesBuiltin(declaration.initializer(), methodName)) {
                        return true;
                    }
                }
                for (QinIrExpression expression : forStatement.initializerExpressions()) {
                    if (usesBuiltin(expression, methodName)) {
                        return true;
                    }
                }
                if (usesBuiltin(forStatement.test(), methodName)) {
                    return true;
                }
                for (QinIrExpression expression : forStatement.updateExpressions()) {
                    if (usesBuiltin(expression, methodName)) {
                        return true;
                    }
                }
                if (usesBuiltin(forStatement.body(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrForEachStatement forEachStatement) {
                if (usesBuiltin(forEachStatement.iterable(), methodName)
                        || usesBuiltin(forEachStatement.body(), methodName)) {
                    return true;
                }
                continue;
            }
            if (statement instanceof QinIrTryStatement tryStatement) {
                if (usesBuiltin(tryStatement.tryBody(), methodName)
                        || usesBuiltin(tryStatement.finallyBody(), methodName)) {
                    return true;
                }
                for (QinIrCatchClause catchClause : tryStatement.catchClauses()) {
                    if (usesBuiltin(catchClause.body(), methodName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean usesBuiltin(QinIrExpression expression, String methodName) {
        if (expression == null) {
            return false;
        }
        if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            return usesBuiltin(assignmentExpression.target(), methodName)
                    || usesBuiltin(assignmentExpression.value(), methodName);
        }
        if (expression instanceof QinIrLocalDeclarationExpression localDeclarationExpression) {
            return usesBuiltin(localDeclarationExpression.initializer(), methodName);
        }
        if (expression instanceof QinIrThrowExpression throwExpression) {
            return usesBuiltin(throwExpression.value(), methodName);
        }
        if (expression instanceof QinIrCastExpression castExpression) {
            return usesBuiltin(castExpression.expression(), methodName);
        }
        if (expression instanceof QinIrIfExpression ifExpression) {
            return usesBuiltin(ifExpression.test(), methodName)
                    || usesBuiltin(ifExpression.consequent(), methodName)
                    || usesBuiltin(ifExpression.alternate(), methodName);
        }
        if (expression instanceof QinIrFunctionLiteral functionLiteral) {
            return (functionLiteral.returnExpression() != null && usesBuiltin(functionLiteral.returnExpression(), methodName))
                    || usesBuiltin(functionLiteral.bodyStatements(), methodName);
        }
        if (expression instanceof QinIrForExpression forExpression) {
            for (QinIrLocalVariableDeclaration declaration : forExpression.initializerDeclarations()) {
                if (usesBuiltin(declaration.initializer(), methodName)) {
                    return true;
                }
            }
            for (QinIrExpression initializerExpression : forExpression.initializerExpressions()) {
                if (usesBuiltin(initializerExpression, methodName)) {
                    return true;
                }
            }
            if (usesBuiltin(forExpression.test(), methodName)) {
                return true;
            }
            for (QinIrExpression updateExpression : forExpression.updateExpressions()) {
                if (usesBuiltin(updateExpression, methodName)) {
                    return true;
                }
            }
            for (QinIrLocalVariableDeclaration declaration : forExpression.bodyLocalDeclarations()) {
                if (usesBuiltin(declaration.initializer(), methodName)) {
                    return true;
                }
            }
            for (QinIrExpression bodyExpression : forExpression.bodyExpressions()) {
                if (usesBuiltin(bodyExpression, methodName)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrForEachExpression forEachExpression) {
            if (usesBuiltin(forEachExpression.iterable(), methodName)) {
                return true;
            }
            for (QinIrLocalVariableDeclaration declaration : forEachExpression.bodyLocalDeclarations()) {
                if (usesBuiltin(declaration.initializer(), methodName)) {
                    return true;
                }
            }
            for (QinIrExpression bodyExpression : forEachExpression.bodyExpressions()) {
                if (usesBuiltin(bodyExpression, methodName)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrDoWhileExpression doWhileExpression) {
            for (QinIrLocalVariableDeclaration declaration : doWhileExpression.localDeclarations()) {
                if (usesBuiltin(declaration.initializer(), methodName)) {
                    return true;
                }
            }
            for (QinIrExpression bodyExpression : doWhileExpression.bodyExpressions()) {
                if (usesBuiltin(bodyExpression, methodName)) {
                    return true;
                }
            }
            return usesBuiltin(doWhileExpression.test(), methodName);
        }
        if (expression instanceof QinIrWhileExpression whileExpression) {
            if (usesBuiltin(whileExpression.test(), methodName)) {
                return true;
            }
            for (QinIrLocalVariableDeclaration declaration : whileExpression.localDeclarations()) {
                if (usesBuiltin(declaration.initializer(), methodName)) {
                    return true;
                }
            }
            for (QinIrExpression bodyExpression : whileExpression.bodyExpressions()) {
                if (usesBuiltin(bodyExpression, methodName)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            if (methodName.equals(builtinCallExpression.methodName())) {
                return true;
            }
            for (QinIrExpression argument : builtinCallExpression.arguments()) {
                if (usesBuiltin(argument, methodName)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            for (QinIrExpression argument : javaNewExpression.arguments()) {
                if (usesBuiltin(argument, methodName)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrJavaMethodReferenceExpression) {
            return false;
        }
        if (expression instanceof QinIrJavaInstanceofPatternExpression instanceofPatternExpression) {
            return usesBuiltin(instanceofPatternExpression.value(), methodName);
        }
        if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            for (QinIrExpression argument : staticMethodCallExpression.arguments()) {
                if (usesBuiltin(argument, methodName)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrInstanceMethodCallExpression instanceMethodCallExpression) {
            if (usesBuiltin(instanceMethodCallExpression.receiver(), methodName)) {
                return true;
            }
            for (QinIrExpression argument : instanceMethodCallExpression.arguments()) {
                if (usesBuiltin(argument, methodName)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                if (usesBuiltin(property.value(), methodName)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinIrLetExpression letExpression) {
            for (QinIrLocalVariableDeclaration declaration : letExpression.localDeclarations()) {
                if (usesBuiltin(declaration.initializer(), methodName)) {
                    return true;
                }
            }
            for (QinIrExpression leadingExpression : letExpression.leadingExpressions()) {
                if (usesBuiltin(leadingExpression, methodName)) {
                    return true;
                }
            }
            return usesBuiltin(letExpression.resultExpression(), methodName);
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            for (QinIrExpression leadingExpression : sequenceExpression.leadingExpressions()) {
                if (usesBuiltin(leadingExpression, methodName)) {
                    return true;
                }
            }
            return usesBuiltin(sequenceExpression.resultExpression(), methodName);
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            return usesBuiltin(propertyAccessExpression.receiver(), methodName);
        }
        return false;
    }

    private void emitObjectLiteral(StringBuilder js, QinIrObjectLiteral objectLiteral) {
        js.append("{ ");
        List<QinIrObjectProperty> properties = objectLiteral.properties();
        for (int i = 0; i < properties.size(); i++) {
            QinIrObjectProperty property = properties.get(i);
            js.append(property.key()).append(": ");
            emitExpression(js, property.value());
            if (i < properties.size() - 1) {
                js.append(", ");
            }
        }
        js.append(" }");
    }

    private void emitArrayLiteral(StringBuilder js, QinIrArrayLiteral arrayLiteral) {
        js.append("[");
        List<QinIrExpression> elements = arrayLiteral.elements();
        for (int i = 0; i < elements.size(); i++) {
            emitExpression(js, elements.get(i));
            if (i < elements.size() - 1) {
                js.append(", ");
            }
        }
        js.append("]");
    }

    private void emitJavaClassLiteral(StringBuilder js, QinIrJavaClassLiteralExpression classLiteralExpression) {
        String displayName = classLiteralExpression.binaryName() == null
                ? classLiteralExpression.typeName()
                : classLiteralExpression.binaryName();
        String constructorReference = classLiteralExpression.binaryName() != null
                && (isGeneratedClassOwner(classLiteralExpression.binaryName())
                        || externallyBoundJavaBinaryNames.contains(classLiteralExpression.binaryName()))
                        ? jsClassReference(classLiteralExpression.binaryName())
                        : null;
        String simpleName = displayName;
        int dot = simpleName.lastIndexOf('.');
        if (dot >= 0) {
            simpleName = simpleName.substring(dot + 1);
        }
        int nested = simpleName.lastIndexOf('$');
        if (nested >= 0) {
            simpleName = simpleName.substring(nested + 1);
        }
        js.append("({ getName() { return \"")
                .append(escapeJs(displayName))
                .append("\"; }, getSimpleName() { return \"")
                .append(escapeJs(simpleName))
                .append("\"; }, ");
        if (constructorReference != null) {
            js.append("getDeclaredConstructor(...__qin_types) { const __qin_ctor = ")
                    .append(constructorReference)
                    .append("; return { newInstance(...__qin_args) { return new __qin_ctor(...__qin_args); } }; }, ")
                    .append("getConstructor(...__qin_types) { return this.getDeclaredConstructor(...__qin_types); }, ");
        }
        js.append("equals(other) { return other != null && typeof other.getName === \"function\" && other.getName() === \"")
                .append(escapeJs(displayName))
                .append("\"; }, hashCode() { return ")
                .append(displayName.hashCode())
                .append("; }, ");
        js.append("toString() { return \"class ")
                .append(escapeJs(displayName))
                .append("\"; } })");
    }

    private void emitClassDeclarations(StringBuilder js, List<QinIrClassDeclaration> classDeclarations) {
        Map<String, Integer> simpleNameCounts = javaClassSimpleNameCounts(classDeclarations);
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            js.append("class ").append(jsClassReference(classDeclaration.binaryName()));
            if (classDeclaration.superType() != null) {
                js.append(" extends ").append(jsClassReference(classDeclaration.superType().binaryName()));
            }
            js.append(" {\n");
            Map<String, String> previousFieldAliases = currentJavaFieldAliases;
            QinIrClassDeclaration previousClassDeclaration = currentJavaClassDeclaration;
            currentJavaFieldAliases = javaFieldAliases(classDeclaration);
            currentJavaClassDeclaration = classDeclaration;
            emitTypeScriptFieldDeclarations(js, classDeclaration);
            List<QinIrMethodDeclaration> explicitConstructors = explicitConstructors(classDeclaration);
            emitClassConstructor(js, classDeclaration, explicitConstructors);
            emitClassConstructorInitializers(js, classDeclaration, explicitConstructors);
            emitClassMethods(js, classDeclaration);
            emitJavaRecordDefaultMethods(js, classDeclaration);
            currentJavaFieldAliases = previousFieldAliases;
            currentJavaClassDeclaration = previousClassDeclaration;
            js.append("}\n");
            emitJavaClassSimpleNameAlias(js, classDeclaration, simpleNameCounts);
        }
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            emitStaticFieldInitializers(js, classDeclaration);
            emitStaticInitializers(js, classDeclaration);
        }
        emitJavaEnumStaticValues(js, classDeclarations);
        if (!classDeclarations.isEmpty()) {
            js.append("\n");
        }
    }

    private void emitTypeScriptFieldDeclarations(StringBuilder js, QinIrClassDeclaration classDeclaration) {
        if (!emitTypeAnnotations()) {
            return;
        }
        for (QinIrFieldDeclaration field : classDeclaration.fields()) {
            js.append("  ");
            if (field.staticField() || isJavaEnumConstant(classDeclaration, field)) {
                js.append("static ");
            }
            js.append(jsCurrentJavaFieldName(field.name()));
            emitNullableTypeAnnotation(js, field.type());
            js.append(" = null as any;\n");
        }
    }

    private Map<String, Integer> javaClassSimpleNameCounts(List<QinIrClassDeclaration> classDeclarations) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            counts.merge(classDeclaration.simpleName(), 1, Integer::sum);
        }
        return counts;
    }

    private boolean hasJavaRecordClass(List<QinIrClassDeclaration> classDeclarations) {
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            if (classDeclaration.recordClass()) {
                return true;
            }
        }
        return false;
    }

    private void emitJavaClassSimpleNameAlias(
            StringBuilder js,
            QinIrClassDeclaration classDeclaration,
            Map<String, Integer> simpleNameCounts) {
        String simpleName = classDeclaration.simpleName();
        String classReference = jsClassReference(classDeclaration.binaryName());
        if (!isJsIdentifier(simpleName)
                || simpleName.equals(classReference)
                || simpleNameCounts.getOrDefault(simpleName, 0) != 1) {
            return;
        }
        js.append("const ").append(simpleName).append(" = ").append(classReference).append(";\n");
    }

    private void emitJavaEnumStaticValues(StringBuilder js, List<QinIrClassDeclaration> classDeclarations) {
        if (!hasJavaEnumStaticValues(classDeclarations)) {
            return;
        }
        if (externalJavaSdkRuntime) {
            requireExternalJavaSdkRuntime("__qin_init_enum_value");
        } else {
            emitJavaEnumValueRuntimeHelper(js);
        }
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            if (!isJavaEnumClass(classDeclaration)) {
                continue;
            }
            int ordinal = 0;
            for (QinIrFieldDeclaration field : classDeclaration.fields()) {
                if (!isJavaEnumConstant(classDeclaration, field)) {
                    continue;
                }
                js.append(jsClassReference(classDeclaration.binaryName()))
                        .append(".")
                        .append(jsJavaFieldName(field.name()))
                        .append(" = __qin_init_enum_value(");
                if (field.initializer() == null) {
                    js.append("new ").append(jsClassReference(classDeclaration.binaryName())).append("()");
                } else {
                    emitExpression(js, field.initializer());
                }
                js.append(", \"")
                        .append(escapeJs(field.name()))
                        .append("\", ")
                        .append(ordinal)
                        .append(");\n");
                ordinal++;
            }
        }
    }

    private void emitJavaEnumValueRuntimeHelper(StringBuilder js) {
        if (js.indexOf("function __qin_init_enum_value") >= 0) {
            return;
        }
        js.append("""
                function __qin_init_enum_value(value, name, ordinal) {
                  Object.defineProperty(value, "__qinEnumName", {
                    value: "" + name,
                    configurable: true
                  });
                  Object.defineProperty(value, "__qinEnumOrdinal", {
                    value: ordinal,
                    configurable: true
                  });
                  return value;
                }
                """);
    }

    private void emitJavaRecordDefaultMethods(StringBuilder js, QinIrClassDeclaration classDeclaration) {
        if (!classDeclaration.recordClass()) {
            return;
        }
        List<QinIrFieldDeclaration> components = javaRecordComponentFields(classDeclaration);
        String classReference = jsClassReference(classDeclaration.binaryName());
        Set<String> instanceMethods = instanceMethodNames(classDeclaration);
        if (!instanceMethods.contains("equals")) {
            js.append("  equals(other) {\n")
                    .append("    if (this === other) return true;\n")
                    .append("    if (!(other instanceof ").append(classReference).append(")) return false;\n");
            if (components.isEmpty()) {
                js.append("    return true;\n");
            } else {
                js.append("    return ");
                for (int i = 0; i < components.size(); i++) {
                    QinIrFieldDeclaration component = components.get(i);
                    if (i > 0) {
                        js.append("\n      && ");
                    }
                    js.append("__qin_java_values_equal__(this.")
                            .append(jsJavaFieldName(component.name()))
                            .append(", other.")
                            .append(jsJavaFieldName(component.name()))
                            .append(")");
                }
                js.append(";\n");
            }
            js.append("  }\n");
        }
        if (!instanceMethods.contains("hashCode")) {
            js.append("  hashCode() {\n")
                    .append("    let result = 1;\n");
            for (QinIrFieldDeclaration component : components) {
                js.append("    result = result * 31 + __qin_java_value_hash_code__(this.")
                        .append(jsJavaFieldName(component.name()))
                        .append(");\n");
            }
            js.append("    return result;\n")
                    .append("  }\n");
        }
        if (!instanceMethods.contains("toString")) {
            js.append("  toString() {\n");
            if (components.isEmpty()) {
                js.append("    return \"").append(escapeJs(classDeclaration.simpleName())).append("[]\";\n");
            } else {
                js.append("    return \"").append(escapeJs(classDeclaration.simpleName())).append("[\" + ");
                for (int i = 0; i < components.size(); i++) {
                    QinIrFieldDeclaration component = components.get(i);
                    if (i > 0) {
                        js.append(" + \", \" + ");
                    }
                    js.append("\"").append(escapeJs(component.name())).append("=\" + this.")
                            .append(jsJavaFieldName(component.name()));
                }
                js.append(" + \"]\";\n");
            }
            js.append("  }\n");
        }
    }

    private List<QinIrFieldDeclaration> javaRecordComponentFields(QinIrClassDeclaration classDeclaration) {
        return classDeclaration.fields().stream()
                .filter(field -> !field.staticField())
                .toList();
    }

    private Set<String> instanceMethodNames(QinIrClassDeclaration classDeclaration) {
        Set<String> names = new LinkedHashSet<>();
        for (QinIrMethodDeclaration method : classDeclaration.methods()) {
            if (!method.staticMethod() && !"constructor".equals(method.name())) {
                names.add(method.name());
            }
        }
        return names;
    }

    private boolean hasJavaEnumStaticValues(List<QinIrClassDeclaration> classDeclarations) {
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            if (!isJavaEnumClass(classDeclaration)) {
                continue;
            }
            for (QinIrFieldDeclaration field : classDeclaration.fields()) {
                if (isJavaEnumConstant(classDeclaration, field)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isJavaEnumClass(QinIrClassDeclaration classDeclaration) {
        return classDeclaration.superType() != null
                && "java.lang.Enum".equals(classDeclaration.superType().binaryName());
    }

    private boolean isJavaEnumConstant(QinIrClassDeclaration classDeclaration, QinIrFieldDeclaration field) {
        return isJavaEnumClass(classDeclaration)
                && field.type().binaryName() != null
                && field.type().binaryName().equals(classDeclaration.binaryName());
    }

    private List<QinIrMethodDeclaration> explicitConstructors(QinIrClassDeclaration classDeclaration) {
        List<QinIrMethodDeclaration> constructors = new java.util.ArrayList<>();
        for (QinIrMethodDeclaration method : classDeclaration.methods()) {
            if ("constructor".equals(method.name())) {
                constructors.add(method);
            }
        }
        return List.copyOf(constructors);
    }

    private void emitClassConstructor(
            StringBuilder js,
            QinIrClassDeclaration classDeclaration,
            List<QinIrMethodDeclaration> explicitConstructors) {
        Map<String, String> previousAliases = bindingAliases;
        bindingAliases = new LinkedHashMap<>(previousAliases);
        js.append("  constructor(...__qin_args");
        if (emitTypeAnnotations()) {
            js.append(": any[]");
        }
        js.append(") {\n");
        QinIrMethodDeclaration selectedConstructor = explicitConstructors.isEmpty()
                ? null
                : explicitConstructors.get(0);
        List<QinIrParameter> parameters = selectedConstructor == null
                ? List.of()
                : selectedConstructor.parameters();
        for (QinIrParameter parameter : parameters) {
            declareBindingName(parameter.name());
        }
        if (!explicitConstructors.isEmpty()) {
            js.append("    switch (__qin_args.length) {\n");
            Set<Integer> emittedArities = new LinkedHashSet<>();
            for (QinIrMethodDeclaration constructor : explicitConstructors) {
                int arity = constructor.parameters().size();
                if (!emittedArities.add(arity)) {
                    continue;
                }
                js.append("      case ").append(arity).append(": {\n");
                List<QinIrParameter> constructorParameters = constructor.parameters();
                for (int i = 0; i < constructorParameters.size(); i++) {
                    js.append("        const ")
                            .append(declareBindingName(constructorParameters.get(i).name()));
                    emitAnyTypeAnnotation(js);
                    js.append(" = __qin_args[")
                            .append(i)
                            .append("];\n");
                }
                emitSuperCall(js, classDeclaration, constructor, constructorParameters, "        ");
                js.append("        this.")
                        .append(constructorInitializerName(classDeclaration, arity))
                        .append("(");
                for (int i = 0; i < constructorParameters.size(); i++) {
                    js.append(jsBindingName(constructorParameters.get(i).name()));
                    if (i < constructorParameters.size() - 1) {
                        js.append(", ");
                    }
                }
                js.append(");\n");
                js.append("        return;\n");
                js.append("      }\n");
            }
            js.append("      default: throw new Error(\"Unsupported Java constructor arity: ")
                    .append(escapeJs(classDeclaration.simpleName()))
                    .append("/\" + __qin_args.length);\n");
            js.append("    }\n");
            js.append("  }\n");
            bindingAliases = previousAliases;
            return;
        }
        js.append("    if (__qin_args.length !== 0) {\n");
        js.append("      throw new Error(\"Unsupported Java constructor arity: ")
                .append(escapeJs(classDeclaration.simpleName()))
                .append("/\" + __qin_args.length);\n");
        js.append("    }\n");
        emitSuperCall(js, classDeclaration, selectedConstructor, parameters, "    ");
        emitFieldInitializers(js, classDeclaration);
        js.append("  }\n");
        bindingAliases = previousAliases;
    }

    private void emitClassConstructorInitializers(
            StringBuilder js,
            QinIrClassDeclaration classDeclaration,
            List<QinIrMethodDeclaration> explicitConstructors) {
        if (explicitConstructors.isEmpty()) {
            return;
        }
        for (QinIrMethodDeclaration constructor : explicitConstructors) {
            emitClassConstructorInitializer(js, classDeclaration, constructor);
        }
    }

    private void emitClassConstructorInitializer(
            StringBuilder js,
            QinIrClassDeclaration classDeclaration,
            QinIrMethodDeclaration constructor) {
        Map<String, String> previousAliases = bindingAliases;
        bindingAliases = new LinkedHashMap<>(previousAliases);
        List<QinIrParameter> parameters = constructor.parameters();
        js.append("  ").append(constructorInitializerName(classDeclaration, parameters.size())).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            QinIrParameter parameter = parameters.get(i);
            js.append(declareBindingName(parameter.name()));
            emitTypeAnnotation(js, parameter.type());
            if (i < parameters.size() - 1) {
                js.append(", ");
            }
        }
        js.append(")");
        if (emitTypeAnnotations()) {
            js.append(": void");
        }
        js.append(" {\n");
        emitPatternVariableDeclarations(js, constructor.bodyStatements(), constructor.returnExpression(), "    ");
        if (!hasThisConstructorDelegation(constructor)) {
            emitFieldInitializers(js, classDeclaration);
        }
        if (!constructor.bodyStatements().isEmpty()) {
            emitStatements(js, constructor.bodyStatements(), "    ");
        } else if (constructor.returnExpression() != null) {
            js.append("    ");
            emitExpression(js, constructor.returnExpression());
            js.append(";\n");
        }
        js.append("  }\n");
        bindingAliases = previousAliases;
    }

    private void emitSuperCall(
            StringBuilder js,
            QinIrClassDeclaration classDeclaration,
            QinIrMethodDeclaration constructor,
            List<QinIrParameter> parameters,
            String indent) {
        if (classDeclaration.superType() != null) {
            js.append(indent).append("super(");
            List<QinIrExpression> superArguments = constructor == null
                    ? List.of()
                    : constructor.superArguments();
            if (!superArguments.isEmpty()) {
                for (int i = 0; i < superArguments.size(); i++) {
                    emitExpression(js, superArguments.get(i));
                    if (i < superArguments.size() - 1) {
                        js.append(", ");
                    }
                }
            }
            js.append(");\n");
        }
    }

    private boolean hasThisConstructorDelegation(QinIrMethodDeclaration constructor) {
        return usesThisConstructorDelegation(constructor.returnExpression());
    }

    private boolean usesThisConstructorDelegation(QinIrExpression expression) {
        if (expression == null) {
            return false;
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            return "constructor".equals(methodCallExpression.methodName())
                    && methodCallExpression.receiver() instanceof QinIrThisExpression;
        }
        if (expression instanceof QinIrLetExpression letExpression) {
            for (QinIrExpression leadingExpression : letExpression.leadingExpressions()) {
                if (usesThisConstructorDelegation(leadingExpression)) {
                    return true;
                }
            }
            return usesThisConstructorDelegation(letExpression.resultExpression());
        }
        if (expression instanceof QinIrIfExpression ifExpression) {
            return usesThisConstructorDelegation(ifExpression.consequent())
                    || usesThisConstructorDelegation(ifExpression.alternate());
        }
        return false;
    }

    private void emitFieldInitializers(StringBuilder js, QinIrClassDeclaration classDeclaration) {
        for (QinIrFieldDeclaration field : classDeclaration.fields()) {
            if (field.staticField() || isJavaEnumConstant(classDeclaration, field)) {
                continue;
            }
            js.append("    this.").append(jsCurrentJavaFieldName(field.name())).append(" = ");
            if (field.initializer() == null) {
                js.append("null");
            } else {
                emitExpression(js, field.initializer());
            }
            js.append(";\n");
        }
    }

    private void emitStaticFieldInitializers(StringBuilder js, QinIrClassDeclaration classDeclaration) {
        Map<String, String> previousFieldAliases = currentJavaFieldAliases;
        currentJavaFieldAliases = javaFieldAliases(classDeclaration);
        for (QinIrFieldDeclaration field : classDeclaration.fields()) {
            if (!field.staticField() || isJavaEnumConstant(classDeclaration, field)) {
                continue;
            }
            js.append(jsClassReference(classDeclaration.binaryName()))
                    .append(".")
                    .append(jsCurrentJavaFieldName(field.name()))
                    .append(" = ");
            if (field.initializer() == null) {
                js.append("null");
            } else {
                emitExpression(js, field.initializer());
            }
            js.append(";\n");
        }
        currentJavaFieldAliases = previousFieldAliases;
    }

    private void emitStaticInitializers(StringBuilder js, QinIrClassDeclaration classDeclaration) {
        if (classDeclaration.staticInitializers().isEmpty()) {
            return;
        }
        Map<String, String> previousFieldAliases = currentJavaFieldAliases;
        QinIrClassDeclaration previousClassDeclaration = currentJavaClassDeclaration;
        currentJavaFieldAliases = javaFieldAliases(classDeclaration);
        currentJavaClassDeclaration = classDeclaration;
        for (QinIrExpression staticInitializer : classDeclaration.staticInitializers()) {
            js.append("(() => {\n");
            js.append("  ");
            emitExpression(js, staticInitializer);
            js.append(";\n");
            js.append("})();\n");
        }
        currentJavaFieldAliases = previousFieldAliases;
        currentJavaClassDeclaration = previousClassDeclaration;
    }

    private String constructorInitializerName(QinIrClassDeclaration classDeclaration, int arity) {
        return "__qin_constructor_" + jsClassReference(classDeclaration.binaryName()) + "_" + arity;
    }

    public static String generatedJavaClassIdentifier(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            throw new IllegalArgumentException("Missing JS class reference");
        }
        StringBuilder identifier = new StringBuilder(binaryName.length());
        for (int i = 0; i < binaryName.length(); i++) {
            char ch = binaryName.charAt(i);
            if (i == 0) {
                identifier.append(isJsIdentifierStart(ch) ? ch : '_');
            } else {
                identifier.append(isJsIdentifierPart(ch) ? ch : '_');
            }
        }
        return identifier.toString();
    }

    private String jsClassReference(String binaryName) {
        return generatedJavaClassIdentifier(binaryName);
    }

    private static boolean isJsIdentifierStart(char ch) {
        return ch == '_' || ch == '$'
                || (ch >= 'A' && ch <= 'Z')
                || (ch >= 'a' && ch <= 'z');
    }

    private static boolean isJsIdentifierPart(char ch) {
        return isJsIdentifierStart(ch) || (ch >= '0' && ch <= '9');
    }

    private Set<String> generatedClassBinaryNames(List<QinIrClassDeclaration> classDeclarations) {
        Set<String> names = new LinkedHashSet<>();
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            names.add(classDeclaration.binaryName());
        }
        return Set.copyOf(names);
    }

    private Map<String, QinIrClassDeclaration> generatedClassesByBinaryName(
            List<QinIrClassDeclaration> classDeclarations) {
        Map<String, QinIrClassDeclaration> classes = new LinkedHashMap<>();
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            classes.put(classDeclaration.binaryName(), classDeclaration);
        }
        return Map.copyOf(classes);
    }

    private Map<String, String> generatedClassReferencesBySimpleName(
            List<QinIrClassDeclaration> classDeclarations) {
        Map<String, String> references = new LinkedHashMap<>();
        Set<String> ambiguousNames = new LinkedHashSet<>();
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            String reference = jsClassReference(classDeclaration.binaryName());
            addGeneratedClassReference(references, ambiguousNames, classDeclaration.simpleName(), reference);
            addGeneratedClassReference(
                    references,
                    ambiguousNames,
                    simpleClassName(classDeclaration.binaryName()),
                    reference);
            addGeneratedClassReference(
                    references,
                    ambiguousNames,
                    nestedSimpleClassName(classDeclaration.binaryName()),
                    reference);
        }
        for (String ambiguousName : ambiguousNames) {
            references.remove(ambiguousName);
        }
        return Map.copyOf(references);
    }

    private void addGeneratedClassReference(
            Map<String, String> references,
            Set<String> ambiguousNames,
            String name,
            String reference) {
        if (name == null || name.isBlank() || ambiguousNames.contains(name)) {
            return;
        }
        String previous = references.putIfAbsent(name, reference);
        if (previous != null && !previous.equals(reference)) {
            ambiguousNames.add(name);
            references.remove(name);
        }
    }

    private boolean isGeneratedClassOwner(String ownerBinaryName) {
        return ownerBinaryName != null && generatedClassBinaryNames.contains(ownerBinaryName);
    }

    private Map<String, String> javaFieldAliases(QinIrClassDeclaration classDeclaration) {
        LinkedHashMap<String, String> aliases = new LinkedHashMap<>();
        if (classDeclaration.superType() != null) {
            QinIrClassDeclaration superClass =
                    generatedClassesByBinaryName.get(classDeclaration.superType().binaryName());
            if (superClass != null) {
                aliases.putAll(javaFieldAliases(superClass));
            }
        }
        for (QinIrFieldDeclaration field : classDeclaration.fields()) {
            aliases.put(field.name(), jsJavaFieldName(field.name()));
        }
        return Map.copyOf(aliases);
    }

    private Map<String, String> javaFieldAliases(List<QinIrClassDeclaration> classDeclarations) {
        LinkedHashMap<String, String> aliases = new LinkedHashMap<>();
        for (QinIrClassDeclaration classDeclaration : classDeclarations) {
            for (QinIrFieldDeclaration field : classDeclaration.fields()) {
                aliases.putIfAbsent(field.name(), jsJavaFieldName(field.name()));
            }
        }
        return Map.copyOf(aliases);
    }

    private void emitClassMethods(StringBuilder js, QinIrClassDeclaration classDeclaration) {
        Map<String, List<QinIrMethodDeclaration>> methodsByName = new LinkedHashMap<>();
        for (QinIrMethodDeclaration method : classDeclaration.methods()) {
            if ("constructor".equals(method.name())) {
                continue;
            }
            String groupKey = method.name() + "\u0000" + method.staticMethod();
            methodsByName.computeIfAbsent(groupKey, ignored -> new java.util.ArrayList<>()).add(method);
        }
        for (List<QinIrMethodDeclaration> overloads : methodsByName.values()) {
            if (overloads.size() == 1) {
                emitMethodDeclaration(js, classDeclaration.simpleName(), overloads.get(0), overloads.get(0).name());
                continue;
            }
            emitOverloadedMethodDispatcher(js, overloads.get(0).name(), overloads);
            for (int i = 0; i < overloads.size(); i++) {
                QinIrMethodDeclaration overload = overloads.get(i);
                emitMethodDeclaration(
                        js,
                        classDeclaration.simpleName(),
                        overload,
                        overloadedMethodImplementationName(overload.name(), overload.parameters().size(), i));
            }
        }
    }

    private void emitOverloadedMethodDispatcher(
            StringBuilder js,
            String methodName,
            List<QinIrMethodDeclaration> overloads) {
        js.append("  ");
        if (overloads.get(0).staticMethod()) {
            js.append("static ");
        }
        js.append(methodName).append("(...__qin_args");
        if (emitTypeAnnotations()) {
            js.append(": any[]");
        }
        js.append(")");
        emitAnyTypeAnnotation(js);
        js.append(" {\n");
        for (int i = 0; i < overloads.size(); i++) {
            QinIrMethodDeclaration overload = overloads.get(i);
            if (isVarargsMethod(overload)) {
                continue;
            }
            int arity = overload.parameters().size();
            js.append("    if (__qin_args.length === ")
                    .append(arity)
                    .append(" && ")
                    .append(overloadTypeGuard(overload))
                    .append(") return this.")
                    .append(overloadedMethodImplementationName(methodName, arity, i))
                    .append("(...__qin_args);\n");
        }
        for (int i = 0; i < overloads.size(); i++) {
            QinIrMethodDeclaration overload = overloads.get(i);
            if (!isVarargsMethod(overload)) {
                continue;
            }
            int arity = overload.parameters().size();
            js.append("    if (")
                    .append(overloadVarargsGuard(overload))
                    .append(") return this.")
                    .append(overloadedMethodImplementationName(methodName, arity, i))
                    .append("(...__qin_args);\n");
        }
        js.append("    throw new Error(\"Unsupported Java overload: ")
                .append(escapeJs(methodName))
                .append("/\" + __qin_args.length);\n");
        js.append("  }\n");
    }

    private boolean isVarargsMethod(QinIrMethodDeclaration method) {
        List<QinIrParameter> parameters = method.parameters();
        return !parameters.isEmpty() && parameters.get(parameters.size() - 1).varargs();
    }

    private String overloadTypeGuard(QinIrMethodDeclaration overload) {
        List<String> checks = new java.util.ArrayList<>();
        List<QinIrParameter> parameters = overload.parameters();
        for (int i = 0; i < parameters.size(); i++) {
            checks.add(overloadArgumentTypeGuard("__qin_args[" + i + "]", parameters.get(i).type()));
        }
        return checks.isEmpty() ? "true" : String.join(" && ", checks);
    }

    private String overloadVarargsGuard(QinIrMethodDeclaration overload) {
        List<QinIrParameter> parameters = overload.parameters();
        int fixedArity = parameters.size() - 1;
        List<String> checks = new java.util.ArrayList<>();
        checks.add("__qin_args.length >= " + fixedArity);
        for (int i = 0; i < fixedArity; i++) {
            checks.add(overloadArgumentTypeGuard("__qin_args[" + i + "]", parameters.get(i).type()));
        }
        checks.add("__qin_args.slice(" + fixedArity + ").every((__qin_arg) => "
                + overloadArgumentTypeGuard("__qin_arg", varargsElementType(parameters.get(parameters.size() - 1).type()))
                + ")");
        return String.join(" && ", checks);
    }

    private QinIrTypeRef varargsElementType(QinIrTypeRef varargsType) {
        if (varargsType.kind() != QinIrTypeKind.CLASS || varargsType.binaryName() == null
                || !varargsType.binaryName().startsWith("[")) {
            return varargsType;
        }
        String elementBinaryName = varargsType.binaryName().substring(1);
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
                yield QinIrTypeRef.classType(Object.class.getName());
            }
        };
    }

    private String overloadArgumentTypeGuard(String argumentExpression, QinIrTypeRef type) {
        return switch (type.kind()) {
            case BOOLEAN -> "typeof " + argumentExpression + " === \"boolean\"";
            case INT, DOUBLE -> "typeof " + argumentExpression + " === \"number\"";
            case STRING -> "(" + argumentExpression + " === null || typeof " + argumentExpression + " === \"string\")";
            case CLASS -> overloadClassTypeGuard(argumentExpression, type.binaryName());
            case VOID -> "false";
        };
    }

    private String overloadClassTypeGuard(String argumentExpression, String binaryName) {
        if ("java.lang.String".equals(binaryName)) {
            return "(" + argumentExpression + " === null || typeof " + argumentExpression + " === \"string\")";
        }
        if ("java.lang.Boolean".equals(binaryName)) {
            return "(" + argumentExpression + " === null || typeof " + argumentExpression + " === \"boolean\")";
        }
        if ("java.lang.Integer".equals(binaryName) || "java.lang.Double".equals(binaryName)
                || "java.lang.Number".equals(binaryName)) {
            return "(" + argumentExpression + " === null || typeof " + argumentExpression + " === \"number\")";
        }
        if (isJavaFunctionalInterface(binaryName)) {
            return "(" + argumentExpression + " === null"
                    + " || typeof " + argumentExpression + " === \"function\""
                    + " || " + argumentExpression + ".__qinJavaFunctional === true)";
        }
        if ("com.subhuti.parser.Alternative".equals(binaryName)) {
            return "(" + argumentExpression + " === null || "
                    + argumentExpression + ".__qinSubhutiAlternative === true)";
        }
        if ("java.util.Collection".equals(binaryName)) {
            return "true";
        }
        if ("java.util.List".equals(binaryName)) {
            return "(" + argumentExpression + " === null"
                    + " || " + argumentExpression + " instanceof __QinJavaUtilArrayList"
                    + " || " + argumentExpression + " instanceof __QinJavaUtilUnmodifiableList)";
        }
        if ("java.util.Set".equals(binaryName)) {
            return "(" + argumentExpression + " === null"
                    + " || " + argumentExpression + " instanceof __QinJavaUtilHashSet"
                    + " || " + argumentExpression + " instanceof __QinJavaUtilUnmodifiableSet)";
        }
        if ("java.util.Map".equals(binaryName)) {
            return "(" + argumentExpression + " === null"
                    + " || " + argumentExpression + " instanceof __QinJavaUtilHashMap"
                    + " || " + argumentExpression + " instanceof __QinJavaUtilUnmodifiableMap)";
        }
        String ownerReference = javaOwnerReference(binaryName, simpleClassName(binaryName));
        if (ownerReference != null && isJsIdentifier(ownerReference)) {
            return "(" + argumentExpression + " === null || " + argumentExpression + " instanceof " + ownerReference + ")";
        }
        return "true";
    }

    private boolean isJavaFunctionalInterface(String binaryName) {
        return switch (binaryName) {
            case "java.lang.Runnable",
                    "java.util.Comparator",
                    "java.util.concurrent.Callable",
                    "java.util.function.BiConsumer",
                    "java.util.function.BiFunction",
                    "java.util.function.BiPredicate",
                    "java.util.function.Consumer",
                    "java.util.function.Function",
                    "java.util.function.Predicate",
                    "java.util.function.Supplier",
                    "java.util.function.UnaryOperator" -> true;
            default -> false;
        };
    }

    private String simpleClassName(String binaryName) {
        int dot = binaryName.lastIndexOf('.');
        return dot < 0 ? binaryName : binaryName.substring(dot + 1);
    }

    private String nestedSimpleClassName(String binaryName) {
        String simpleName = simpleClassName(binaryName);
        int nestedSeparator = simpleName.lastIndexOf('$');
        return nestedSeparator < 0 ? simpleName : simpleName.substring(nestedSeparator + 1);
    }

    private String overloadedMethodImplementationName(String methodName, int arity, int overloadIndex) {
        return "__qin_overload_" + methodName + "_" + arity + "_" + overloadIndex;
    }

    private String subhutiRawMethodName(String jsMethodName) {
        return "__qin_subhuti_raw_" + jsMethodName;
    }

    private void emitMethodDeclaration(
            StringBuilder js,
            String className,
            QinIrMethodDeclaration method,
            String jsMethodName) {
        Map<String, String> previousAliases = bindingAliases;
        bindingAliases = new LinkedHashMap<>(previousAliases);
        js.append("  ");
        if (method.staticMethod()) {
            js.append("static ");
        }
        js.append(jsMethodName).append("(");
        List<QinIrParameter> parameters = method.parameters();
        List<String> jsParameterNames = new java.util.ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).varargs() && i == parameters.size() - 1) {
                js.append("...");
            }
            QinIrParameter parameter = parameters.get(i);
            String parameterName = declareBindingName(parameters.get(i).name());
            jsParameterNames.add(parameterName);
            js.append(parameterName);
            if (parameter.varargs() && i == parameters.size() - 1) {
                if (emitTypeAnnotations()) {
                    js.append(": ").append(tsTypeName(varargsElementType(parameter.type()))).append("[]");
                }
            } else {
                emitTypeAnnotation(js, parameter.type());
            }
            if (i < parameters.size() - 1) {
                js.append(", ");
            }
        }
        js.append(")");
        emitAnyTypeAnnotation(js);
        js.append(" {\n");
        if (isSubhutiRuleMethod(method)) {
            js.append("    return this.executeRuleWrapper(__qin_java_functional(() => {\n");
            js.append("      return this.")
                    .append(subhutiRawMethodName(jsMethodName))
                    .append("(")
                    .append(String.join(", ", jsParameterNames))
                    .append(");\n");
            js.append("    }), \"")
                    .append(escapeJs(subhutiRuleName(method)))
                    .append("\", \"")
                    .append(escapeJs(className))
                    .append("\", __qin_subhuti_rule_cache_key(arguments));\n");
            js.append("  }\n");
            js.append("  ");
            if (method.staticMethod()) {
                js.append("static ");
            }
            js.append(subhutiRawMethodName(jsMethodName)).append("(");
            for (int i = 0; i < parameters.size(); i++) {
                if (parameters.get(i).varargs() && i == parameters.size() - 1) {
                    js.append("...");
                }
                js.append(jsParameterNames.get(i));
                if (parameters.get(i).varargs() && i == parameters.size() - 1) {
                    if (emitTypeAnnotations()) {
                        js.append(": ").append(tsTypeName(varargsElementType(parameters.get(i).type()))).append("[]");
                    }
                } else {
                    emitTypeAnnotation(js, parameters.get(i).type());
                }
                if (i < parameters.size() - 1) {
                    js.append(", ");
                }
            }
            js.append(")");
            emitAnyTypeAnnotation(js);
            js.append(" {\n");
            emitPatternVariableDeclarations(js, method.bodyStatements(), method.returnExpression(), "    ");
            if (!method.bodyStatements().isEmpty()) {
                emitStatements(js, method.bodyStatements(), "    ");
                if (!endsWithTerminalStatement(method.bodyStatements())) {
                    js.append("    return null;\n");
                }
            } else {
                js.append("    return ");
                if (method.returnExpression() == null) {
                    js.append("null");
                } else {
                    emitExpression(js, method.returnExpression());
                }
                js.append(";\n");
            }
        } else {
            emitPatternVariableDeclarations(js, method.bodyStatements(), method.returnExpression(), "    ");
            if (!method.bodyStatements().isEmpty()) {
                emitStatements(js, method.bodyStatements(), "    ");
                if (!endsWithTerminalStatement(method.bodyStatements())) {
                    js.append("    return null;\n");
                }
            } else {
                js.append("    return ");
                if (method.returnExpression() == null) {
                    js.append("null");
                } else {
                    emitExpression(js, method.returnExpression());
                }
                js.append(";\n");
            }
        }
        js.append("  }\n");
        bindingAliases = previousAliases;
    }

    private void emitPatternVariableDeclarations(
            StringBuilder js,
            List<QinIrStatement> statements,
            QinIrExpression returnExpression,
            String indent) {
        Set<String> patternVariables = new LinkedHashSet<>();
        collectPatternVariablesFromStatements(statements, patternVariables);
        collectPatternVariables(returnExpression, patternVariables);
        for (String variableName : patternVariables) {
            js.append(indent).append("let ").append(declareBindingName(variableName));
            emitAnyTypeAnnotation(js);
            js.append(" = null;\n");
        }
    }

    private void collectPatternVariablesFromStatements(List<QinIrStatement> statements, Set<String> patternVariables) {
        for (QinIrStatement statement : statements) {
            collectPatternVariables(statement, patternVariables);
        }
    }

    private void collectPatternVariables(QinIrStatement statement, Set<String> patternVariables) {
        if (statement instanceof QinIrReturnStatement returnStatement) {
            collectPatternVariables(returnStatement.value(), patternVariables);
        } else if (statement instanceof QinIrThrowStatement throwStatement) {
            collectPatternVariables(throwStatement.value(), patternVariables);
        } else if (statement instanceof QinIrStatementExpression statementExpression) {
            collectPatternVariables(statementExpression.expression(), patternVariables);
        } else if (statement instanceof QinIrLocalDeclarationStatement localDeclarationStatement) {
            collectPatternVariables(localDeclarationStatement.initializer(), patternVariables);
        } else if (statement instanceof QinIrIfStatement ifStatement) {
            collectPatternVariables(ifStatement.test(), patternVariables);
            collectPatternVariablesFromStatements(ifStatement.consequent(), patternVariables);
            collectPatternVariablesFromStatements(ifStatement.alternate(), patternVariables);
        } else if (statement instanceof QinIrForStatement forStatement) {
            collectPatternVariablesFromLocalDeclarations(forStatement.initializerDeclarations(), patternVariables);
            collectPatternVariablesFromExpressions(forStatement.initializerExpressions(), patternVariables);
            collectPatternVariables(forStatement.test(), patternVariables);
            collectPatternVariablesFromExpressions(forStatement.updateExpressions(), patternVariables);
            collectPatternVariablesFromStatements(forStatement.body(), patternVariables);
        } else if (statement instanceof QinIrForEachStatement forEachStatement) {
            collectPatternVariables(forEachStatement.iterable(), patternVariables);
            collectPatternVariablesFromStatements(forEachStatement.body(), patternVariables);
        } else if (statement instanceof QinIrWhileStatementNode whileStatement) {
            collectPatternVariables(whileStatement.test(), patternVariables);
            collectPatternVariablesFromStatements(whileStatement.body(), patternVariables);
        } else if (statement instanceof QinIrDoWhileStatementNode doWhileStatement) {
            collectPatternVariablesFromStatements(doWhileStatement.body(), patternVariables);
            collectPatternVariables(doWhileStatement.test(), patternVariables);
        } else if (statement instanceof QinIrTryStatement tryStatement) {
            collectPatternVariablesFromStatements(tryStatement.tryBody(), patternVariables);
            for (QinIrCatchClause catchClause : tryStatement.catchClauses()) {
                collectPatternVariablesFromStatements(catchClause.body(), patternVariables);
            }
            collectPatternVariablesFromStatements(tryStatement.finallyBody(), patternVariables);
        }
    }

    private void collectPatternVariablesFromLocalDeclarations(
            List<QinIrLocalVariableDeclaration> declarations,
            Set<String> patternVariables) {
        for (QinIrLocalVariableDeclaration declaration : declarations) {
            collectPatternVariables(declaration.initializer(), patternVariables);
        }
    }

    private void collectPatternVariables(QinIrExpression expression, Set<String> patternVariables) {
        if (expression == null) {
            return;
        }
        if (expression instanceof QinIrJavaInstanceofPatternExpression instanceofPatternExpression) {
            patternVariables.add(instanceofPatternExpression.variableName());
            collectPatternVariables(instanceofPatternExpression.value(), patternVariables);
        } else if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            collectPatternVariables(assignmentExpression.target(), patternVariables);
            collectPatternVariables(assignmentExpression.value(), patternVariables);
        } else if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            collectPatternVariablesFromExpressions(arrayLiteral.elements(), patternVariables);
        } else if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            collectPatternVariablesFromExpressions(builtinCallExpression.arguments(), patternVariables);
        } else if (expression instanceof QinIrCastExpression castExpression) {
            collectPatternVariables(castExpression.expression(), patternVariables);
        } else if (expression instanceof QinIrElementAccessExpression elementAccessExpression) {
            collectPatternVariables(elementAccessExpression.receiver(), patternVariables);
            collectPatternVariables(elementAccessExpression.index(), patternVariables);
        } else if (expression instanceof QinIrForEachExpression forEachExpression) {
            collectPatternVariables(forEachExpression.iterable(), patternVariables);
            collectPatternVariablesFromLocalDeclarations(forEachExpression.bodyLocalDeclarations(), patternVariables);
            collectPatternVariablesFromExpressions(forEachExpression.bodyExpressions(), patternVariables);
        } else if (expression instanceof QinIrForExpression forExpression) {
            collectPatternVariablesFromLocalDeclarations(forExpression.initializerDeclarations(), patternVariables);
            collectPatternVariablesFromExpressions(forExpression.initializerExpressions(), patternVariables);
            collectPatternVariables(forExpression.test(), patternVariables);
            collectPatternVariablesFromExpressions(forExpression.updateExpressions(), patternVariables);
            collectPatternVariablesFromLocalDeclarations(forExpression.bodyLocalDeclarations(), patternVariables);
            collectPatternVariablesFromExpressions(forExpression.bodyExpressions(), patternVariables);
        } else if (expression instanceof QinIrIfExpression ifExpression) {
            collectPatternVariables(ifExpression.test(), patternVariables);
            collectPatternVariables(ifExpression.consequent(), patternVariables);
            collectPatternVariables(ifExpression.alternate(), patternVariables);
        } else if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            collectPatternVariables(methodCallExpression.receiver(), patternVariables);
            collectPatternVariablesFromExpressions(methodCallExpression.arguments(), patternVariables);
        } else if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            collectPatternVariablesFromExpressions(javaNewExpression.arguments(), patternVariables);
        } else if (expression instanceof QinIrLetExpression letExpression) {
            collectPatternVariablesFromLocalDeclarations(letExpression.localDeclarations(), patternVariables);
            collectPatternVariablesFromExpressions(letExpression.leadingExpressions(), patternVariables);
            collectPatternVariables(letExpression.resultExpression(), patternVariables);
        } else if (expression instanceof QinIrLocalDeclarationExpression localDeclarationExpression) {
            collectPatternVariables(localDeclarationExpression.initializer(), patternVariables);
        } else if (expression instanceof QinIrObjectLiteral objectLiteral) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                collectPatternVariables(property.value(), patternVariables);
            }
        } else if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            collectPatternVariables(propertyAccessExpression.receiver(), patternVariables);
        } else if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            collectPatternVariablesFromExpressions(sequenceExpression.leadingExpressions(), patternVariables);
            collectPatternVariables(sequenceExpression.resultExpression(), patternVariables);
        } else if (expression instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            collectPatternVariables(spreadArgumentExpression.expression(), patternVariables);
        } else if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            collectPatternVariablesFromExpressions(staticMethodCallExpression.arguments(), patternVariables);
        } else if (expression instanceof QinIrThrowExpression throwExpression) {
            collectPatternVariables(throwExpression.value(), patternVariables);
        } else if (expression instanceof QinIrWhileExpression whileExpression) {
            collectPatternVariables(whileExpression.test(), patternVariables);
            collectPatternVariablesFromLocalDeclarations(whileExpression.localDeclarations(), patternVariables);
            collectPatternVariablesFromExpressions(whileExpression.bodyExpressions(), patternVariables);
        }
    }

    private void collectPatternVariablesFromExpressions(List<QinIrExpression> expressions, Set<String> patternVariables) {
        for (QinIrExpression expression : expressions) {
            collectPatternVariables(expression, patternVariables);
        }
    }

    private boolean endsWithTerminalStatement(List<QinIrStatement> statements) {
        if (statements.isEmpty()) {
            return false;
        }
        QinIrStatement last = statements.get(statements.size() - 1);
        return last instanceof QinIrReturnStatement
                || last instanceof QinIrThrowStatement
                || last instanceof QinIrBreakStatement
                || last instanceof QinIrContinueStatement;
    }

    private void emitStatements(StringBuilder js, List<QinIrStatement> statements, String indent) {
        for (QinIrStatement statement : statements) {
            emitStatement(js, statement, indent);
        }
    }

    private void emitCatchTypeGuard(StringBuilder js, QinIrCatchClause catchClause, String indent) {
        String catchType = javaCatchTypeReference(catchClause.parameterType());
        if (catchType == null || "__QinJavaLangThrowable".equals(catchType)) {
            return;
        }
        String errorName = declareBindingName(catchClause.parameterName());
        js.append(indent)
                .append("if (!(")
                .append(errorName)
                .append(" instanceof ")
                .append(catchType);
        if ("__QinJavaLangStackOverflowError".equals(catchType)) {
            js.append(" || ").append(errorName).append(" instanceof RangeError");
        }
        js.append(")) {\n")
                .append(indent)
                .append("  throw ")
                .append(errorName)
                .append(";\n")
                .append(indent)
                .append("}\n");
    }

    private String javaCatchTypeReference(QinIrTypeRef type) {
        if (type == null || type.binaryName() == null) {
            return null;
        }
        if (isGeneratedClassOwner(type.binaryName())) {
            return jsClassReference(type.binaryName());
        }
        return javaOwnerReference(type.binaryName(), simpleClassName(type.binaryName()));
    }

    private void emitStatement(StringBuilder js, QinIrStatement statement, String indent) {
        if (statement instanceof QinIrLocalDeclarationStatement localDeclarationStatement) {
            js.append(indent)
                    .append("let ")
                    .append(declareBindingName(localDeclarationStatement.name()));
            emitAnyTypeAnnotation(js);
            js.append(" = ");
            emitExpression(js, localDeclarationStatement.initializer());
            js.append(";\n");
            return;
        }
        if (statement instanceof QinIrStatementExpression statementExpression) {
            js.append(indent);
            emitExpression(js, statementExpression.expression());
            js.append(";\n");
            return;
        }
        if (statement instanceof QinIrReturnStatement returnStatement) {
            js.append(indent).append("return ");
            if (returnStatement.value() == null) {
                js.append("null");
            } else {
                emitExpression(js, returnStatement.value());
            }
            js.append(";\n");
            return;
        }
        if (statement instanceof QinIrThrowStatement throwStatement) {
            js.append(indent).append("throw ");
            emitExpression(js, throwStatement.value());
            js.append(";\n");
            return;
        }
        if (statement instanceof QinIrBreakStatement breakStatement) {
            js.append(indent).append("break");
            emitOptionalLabel(js, breakStatement.label());
            js.append(";\n");
            return;
        }
        if (statement instanceof QinIrContinueStatement continueStatement) {
            js.append(indent).append("continue");
            emitOptionalLabel(js, continueStatement.label());
            js.append(";\n");
            return;
        }
        if (statement instanceof QinIrIfStatement ifStatement) {
            js.append(indent).append("if (");
            emitExpression(js, ifStatement.test());
            js.append(") {\n");
            emitStatements(js, ifStatement.consequent(), indent + "  ");
            if (ifStatement.alternate().isEmpty()) {
                js.append(indent).append("}\n");
            } else {
                js.append(indent).append("} else {\n");
                emitStatements(js, ifStatement.alternate(), indent + "  ");
                js.append(indent).append("}\n");
            }
            return;
        }
        if (statement instanceof QinIrWhileStatementNode whileStatement) {
            js.append(indent).append("while (");
            emitExpression(js, whileStatement.test());
            js.append(") {\n");
            emitStatements(js, whileStatement.body(), indent + "  ");
            js.append(indent).append("}\n");
            return;
        }
        if (statement instanceof QinIrDoWhileStatementNode doWhileStatement) {
            js.append(indent).append("do {\n");
            emitStatements(js, doWhileStatement.body(), indent + "  ");
            js.append(indent).append("} while (");
            emitExpression(js, doWhileStatement.test());
            js.append(");\n");
            return;
        }
        if (statement instanceof QinIrTryStatement tryStatement) {
            js.append(indent).append("try {\n");
            emitStatements(js, tryStatement.tryBody(), indent + "  ");
            js.append(indent).append("}");
            if (tryStatement.catchClauses().isEmpty()) {
                js.append("\n");
            } else {
                QinIrCatchClause catchClause = tryStatement.catchClauses().get(0);
                js.append(" catch (")
                        .append(declareBindingName(catchClause.parameterName()))
                        .append(") {\n");
                emitCatchTypeGuard(js, catchClause, indent + "  ");
                emitStatements(js, catchClause.body(), indent + "  ");
                js.append(indent).append("}\n");
            }
            if (!tryStatement.finallyBody().isEmpty()) {
                js.setLength(js.length() - 1);
                js.append(" finally {\n");
                emitStatements(js, tryStatement.finallyBody(), indent + "  ");
                js.append(indent).append("}\n");
            }
            return;
        }
        if (statement instanceof QinIrForStatement forStatement) {
            js.append(indent).append("for (");
            emitForStatementInitializers(js, forStatement);
            js.append("; ");
            if (forStatement.test() != null) {
                emitExpression(js, forStatement.test());
            }
            js.append("; ");
            emitCommaSeparatedExpressions(js, forStatement.updateExpressions());
            js.append(") {\n");
            emitStatements(js, forStatement.body(), indent + "  ");
            js.append(indent).append("}\n");
            return;
        }
        if (statement instanceof QinIrForEachStatement forEachStatement) {
            js.append(indent)
                    .append("for (const ")
                    .append(declareBindingName(forEachStatement.itemName()))
                    .append(" of ");
            emitExpression(js, forEachStatement.iterable());
            js.append(") {\n");
            emitStatements(js, forEachStatement.body(), indent + "  ");
            js.append(indent).append("}\n");
            return;
        }
        throw new IllegalArgumentException("Unsupported statement: " + statement.getClass().getSimpleName());
    }

    private void emitOptionalLabel(StringBuilder js, String label) {
        if (label != null && !label.isBlank()) {
            js.append(" ").append(jsBindingName(label));
        }
    }

    private void emitForStatementInitializers(StringBuilder js, QinIrForStatement forStatement) {
        boolean emitted = false;
        if (!forStatement.initializerDeclarations().isEmpty()) {
            js.append("let ");
            for (int i = 0; i < forStatement.initializerDeclarations().size(); i++) {
                if (i > 0) {
                    js.append(", ");
                }
                QinIrLocalVariableDeclaration declaration = forStatement.initializerDeclarations().get(i);
                js.append(declareBindingName(declaration.name()));
                emitAnyTypeAnnotation(js);
                js.append(" = ");
                emitExpression(js, declaration.initializer());
            }
            emitted = true;
        }
        if (!forStatement.initializerExpressions().isEmpty()) {
            if (emitted) {
                js.append(", ");
            }
            emitCommaSeparatedExpressions(js, forStatement.initializerExpressions());
        }
    }

    private boolean isSubhutiRuleMethod(QinIrMethodDeclaration method) {
        for (QinIrAnnotation annotation : method.annotations()) {
            if ("com.subhuti.parser.SubhutiRule".equals(annotation.ownerBinaryName())) {
                return true;
            }
        }
        return false;
    }

    private String subhutiRuleName(QinIrMethodDeclaration method) {
        return method.name();
    }

    private void emitConsoleLogs(
            StringBuilder js,
            Map<String, QinIrExpression> declarations,
            List<QinIrConsoleLogStatement> consoleLogs) {
        for (QinIrConsoleLogStatement consoleLog : consoleLogs) {
            QinIrExpression declaration = declarations.get(consoleLog.objectName());
            if (!(declaration instanceof QinIrObjectLiteral objectLiteral)) {
                throw new IllegalArgumentException(
                        "console.log(object.property) requires object literal declaration: " + consoleLog.objectName());
            }
            boolean propertyExists = objectLiteral.properties().stream()
                    .anyMatch(property -> property.key().equals(consoleLog.propertyName()));
            if (!propertyExists) {
                throw new IllegalArgumentException(
                        "Unknown property in console.log: " + consoleLog.propertyName());
            }

            js.append("  console.log(")
                    .append(consoleLog.objectName())
                    .append(".")
                    .append(consoleLog.propertyName())
                    .append(");\n");
        }
    }

    private void emitConsoleValueLogs(
            StringBuilder js,
            List<QinIrConsoleLogValue> consoleValueLogs) {
        for (QinIrConsoleLogValue consoleValueLog : consoleValueLogs) {
            js.append("  console.log(");
            emitExpression(js, consoleValueLog.value());
            js.append(");\n");
        }
    }

    private void emitExpressionStatements(
            StringBuilder js,
            List<QinIrExpressionStatement> expressionStatements) {
        for (QinIrExpressionStatement expressionStatement : expressionStatements) {
            js.append("  ");
            emitExpression(js, expressionStatement.expression());
            js.append(";\n");
        }
    }

    private void emitJavaStaticConsoleLogs(
            StringBuilder js,
            List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs) {
        for (QinIrConsoleLogJavaStaticCall call : javaStaticConsoleLogs) {
            emitJavaStaticConsoleLog(js, call);
        }
    }

    private void emitJavaStaticConsoleLog(StringBuilder js, QinIrConsoleLogJavaStaticCall call) {
        js.append("  console.log(")
                .append(call.receiverName())
                .append(".")
                .append(call.methodName())
                .append("(");
        emitArguments(js, call.arguments());
        js.append("));\n");
    }

    private void emitJavaInstanceMethodCall(StringBuilder js, QinIrJavaInstanceMethodCall call) {
        ensureSupportedJavaOwner(call.ownerBinaryName());
        js.append("  ")
                .append(call.receiverName())
                .append(".")
                .append(call.methodName())
                .append("(");
        emitArguments(js, call.arguments());
        js.append(");\n");
    }

    private void emitJavaInstanceConsoleLog(StringBuilder js, QinIrConsoleLogJavaInstanceCall call) {
        ensureSupportedJavaOwner(call.ownerBinaryName());
        js.append("  console.log(")
                .append(call.receiverName())
                .append(".")
                .append(call.methodName())
                .append("(");
        emitArguments(js, call.arguments());
        js.append("));\n");
    }

    private void emitRuntimeStatements(
            StringBuilder js,
            QinIrProgram program,
            Map<String, QinIrExpression> declarationMap) {
        if (program.executionSteps().isEmpty()) {
            emitExpressionStatements(js, program.expressionStatements());
            emitConsoleValueLogs(js, program.consoleValueLogs());
            emitConsoleLogs(js, declarationMap, program.consoleLogs());
            emitJavaStaticConsoleLogs(js, program.javaStaticConsoleLogs());
            for (QinIrJavaInstanceMethodCall call : program.javaInstanceMethodCalls()) {
                emitJavaInstanceMethodCall(js, call);
            }
            for (QinIrConsoleLogJavaInstanceCall call : program.javaInstanceConsoleLogs()) {
                emitJavaInstanceConsoleLog(js, call);
            }
            return;
        }

        for (QinIrProgram.TopLevelExecutionStep step : program.executionSteps()) {
            switch (step.kind()) {
                case DECLARATION -> {
                    // Declarations are emitted as module-level const bindings above run().
                }
                case EXPRESSION_STATEMENT -> emitExpressionStatement(
                        js,
                        program.expressionStatements().get(step.index()));
                case CONSOLE_VALUE -> emitConsoleValueLog(
                        js,
                        program.consoleValueLogs().get(step.index()));
                case CONSOLE_OBJECT -> emitConsoleLog(
                        js,
                        declarationMap,
                        program.consoleLogs().get(step.index()));
                case JAVA_STATIC_CONSOLE -> emitJavaStaticConsoleLog(
                        js,
                        program.javaStaticConsoleLogs().get(step.index()));
                case JAVA_INSTANCE_CALL -> emitJavaInstanceMethodCall(
                        js,
                        program.javaInstanceMethodCalls().get(step.index()));
                case JAVA_INSTANCE_CONSOLE -> emitJavaInstanceConsoleLog(
                        js,
                        program.javaInstanceConsoleLogs().get(step.index()));
            }
        }
    }

    private void emitExpressionStatement(StringBuilder js, QinIrExpressionStatement expressionStatement) {
        js.append("  ");
        emitExpression(js, expressionStatement.expression());
        js.append(";\n");
    }

    private void emitConsoleValueLog(StringBuilder js, QinIrConsoleLogValue consoleValueLog) {
        js.append("  console.log(");
        emitExpression(js, consoleValueLog.value());
        js.append(");\n");
    }

    private void emitConsoleLog(
            StringBuilder js,
            Map<String, QinIrExpression> declarations,
            QinIrConsoleLogStatement consoleLog) {
        QinIrExpression declaration = declarations.get(consoleLog.objectName());
        if (!(declaration instanceof QinIrObjectLiteral objectLiteral)) {
            throw new IllegalArgumentException(
                    "console.log(object.property) requires object literal declaration: " + consoleLog.objectName());
        }
        boolean propertyExists = objectLiteral.properties().stream()
                .anyMatch(property -> property.key().equals(consoleLog.propertyName()));
        if (!propertyExists) {
            throw new IllegalArgumentException(
                    "Unknown property in console.log: " + consoleLog.propertyName());
        }

        js.append("  console.log(")
                .append(consoleLog.objectName())
                .append(".")
                .append(consoleLog.propertyName())
                .append(");\n");
    }

    private void emitExpression(StringBuilder js, QinIrExpression expression) {
        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            js.append(numberLiteral.value());
            return;
        }
        if (expression instanceof QinIrStringLiteral stringLiteral) {
            js.append("\"").append(escapeJs(stringLiteral.value())).append("\"");
            return;
        }
        if (expression instanceof QinIrBooleanLiteral booleanLiteral) {
            js.append(booleanLiteral.value() ? "true" : "false");
            return;
        }
        if (expression instanceof QinIrNullLiteral) {
            js.append("null");
            return;
        }
        if (expression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            js.append(memberAccessObjectReference(memberAccessExpression.objectName()))
                    .append(".")
                    .append(javaMemberAccessPropertyName(memberAccessExpression));
            return;
        }
        if (expression instanceof QinIrThisExpression) {
            js.append("this");
            return;
        }
        if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            emitExpression(js, assignmentExpression.target());
            js.append(" ").append(assignmentExpression.operator()).append(" ");
            emitExpression(js, assignmentExpression.value());
            return;
        }
        if (expression instanceof QinIrLocalDeclarationExpression localDeclarationExpression) {
            js.append("let ")
                    .append(declareBindingName(localDeclarationExpression.name()));
            emitAnyTypeAnnotation(js);
            js.append(" = ");
            emitExpression(js, localDeclarationExpression.initializer());
            return;
        }
        if (expression instanceof QinIrThrowExpression throwExpression) {
            js.append("(() => { throw ");
            emitExpression(js, throwExpression.value());
            js.append("; })()");
            return;
        }
        if (expression instanceof QinIrCastExpression castExpression) {
            emitCastExpression(js, castExpression);
            return;
        }
        if (expression instanceof QinIrIfExpression ifExpression) {
            emitIfExpression(js, ifExpression);
            return;
        }
        if (expression instanceof QinIrForExpression forExpression) {
            emitForExpression(js, forExpression);
            return;
        }
        if (expression instanceof QinIrForEachExpression forEachExpression) {
            emitForEachExpression(js, forEachExpression);
            return;
        }
        if (expression instanceof QinIrDoWhileExpression doWhileExpression) {
            emitDoWhileExpression(js, doWhileExpression);
            return;
        }
        if (expression instanceof QinIrWhileExpression whileExpression) {
            emitWhileExpression(js, whileExpression);
            return;
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            emitExpression(js, propertyAccessExpression.receiver());
            js.append(".").append(javaFieldAwarePropertyName(propertyAccessExpression));
            return;
        }
        if (expression instanceof QinIrElementAccessExpression elementAccessExpression) {
            emitExpression(js, elementAccessExpression.receiver());
            js.append("[");
            emitExpression(js, elementAccessExpression.index());
            js.append("]");
            return;
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            emitArrayLiteral(js, arrayLiteral);
            return;
        }
        if (expression instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            emitJavaClassLiteral(js, classLiteralExpression);
            return;
        }
        if (expression instanceof QinIrInstanceMethodCallExpression instanceMethodCallExpression) {
            if (emitJavaLangStringInstanceMethodCall(js, instanceMethodCallExpression)) {
                return;
            }
            String superSubhutiRawMethodName = isSuperReference(instanceMethodCallExpression.receiver())
                    ? superSubhutiRawMethodName(
                            instanceMethodCallExpression.methodName(),
                            instanceMethodCallExpression.arguments().size())
                    : null;
            if (isSuperReference(instanceMethodCallExpression.receiver())) {
                js.append("super.");
            } else {
                emitExpression(js, instanceMethodCallExpression.receiver());
                js.append(".");
            }
            if ("constructor".equals(instanceMethodCallExpression.methodName())
                    && instanceMethodCallExpression.receiver() instanceof QinIrThisExpression) {
                if (currentJavaClassDeclaration == null) {
                    throw new IllegalStateException("Java constructor delegation requires current class context");
                }
                js.append(constructorInitializerName(
                        currentJavaClassDeclaration,
                        instanceMethodCallExpression.arguments().size()));
            } else {
                js.append(superSubhutiRawMethodName == null
                        ? instanceMethodCallExpression.methodName()
                        : superSubhutiRawMethodName);
            }
            js.append("(");
            emitArguments(js, instanceMethodCallExpression.arguments());
            js.append(")");
            return;
        }
        if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            ensureSupportedJavaOwner(staticMethodCallExpression.ownerBinaryName());
            js.append(javaOwnerReference(
                            staticMethodCallExpression.ownerBinaryName(),
                            staticMethodCallExpression.classLocalName()))
                    .append(".")
                    .append(staticMethodCallExpression.methodName())
                    .append("(");
            emitArguments(js, staticMethodCallExpression.arguments());
            js.append(")");
            return;
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            ensureSupportedJavaOwner(javaNewExpression.ownerBinaryName());
            js.append("new ")
                    .append(javaOwnerReference(
                            javaNewExpression.ownerBinaryName(),
                            javaNewExpression.classLocalName()))
                    .append("(");
            emitArguments(js, javaNewExpression.arguments());
            js.append(")");
            return;
        }
        if (expression instanceof QinIrJavaMethodReferenceExpression methodReferenceExpression) {
            ensureSupportedJavaOwner(methodReferenceExpression.ownerBinaryName());
            String ownerReference = javaOwnerReference(
                    methodReferenceExpression.ownerBinaryName(),
                    methodReferenceExpression.classLocalName());
            js.append(ownerReference)
                    .append(".")
                    .append(methodReferenceExpression.methodName())
                    .append(".bind(")
                    .append(ownerReference)
                    .append(")");
            return;
        }
        if (expression instanceof QinIrJavaInstanceofPatternExpression instanceofPatternExpression) {
            emitJavaInstanceofPatternExpression(js, instanceofPatternExpression);
            return;
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            js.append(jsBindingName(identifierReference.name()));
            return;
        }
        if (expression instanceof QinIrFunctionLiteral functionLiteral) {
            Map<String, String> previousAliases = bindingAliases;
            bindingAliases = new LinkedHashMap<>(previousAliases);
            js.append("__qin_java_functional((");
            for (int i = 0; i < functionLiteral.parameterNames().size(); i++) {
                js.append(declareBindingName(functionLiteral.parameterNames().get(i)));
                if (i < functionLiteral.parameterNames().size() - 1) {
                    js.append(", ");
                }
            }
            js.append(") => {\n");
            if (!functionLiteral.bodyStatements().isEmpty()) {
                emitStatements(js, functionLiteral.bodyStatements(), "      ");
                if (!endsWithTerminalStatement(functionLiteral.bodyStatements())) {
                    js.append("      return null;\n");
                }
            } else {
                js.append("      return ");
                if (functionLiteral.returnExpression() == null) {
                    js.append("null");
                } else {
                    emitExpression(js, functionLiteral.returnExpression());
                }
                js.append(";\n");
            }
            js.append("    })");
            bindingAliases = previousAliases;
            return;
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            if ("Global".equals(builtinCallExpression.receiverName())) {
                js.append(builtinCallExpression.methodName()).append("(");
            } else {
                js.append(builtinCallExpression.receiverName())
                        .append(".")
                        .append(builtinCallExpression.methodName())
                        .append("(");
            }
            emitArguments(js, builtinCallExpression.arguments());
            js.append(")");
            return;
        }
        if (expression instanceof QinIrLetExpression letExpression) {
            emitLetExpression(js, letExpression);
            return;
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            emitSequenceExpression(js, sequenceExpression);
            return;
        }
        if (expression instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            js.append("...");
            emitExpression(js, spreadArgumentExpression.expression());
            return;
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            emitObjectLiteral(js, objectLiteral);
            return;
        }
        throw new IllegalArgumentException("Unsupported expression: " + expression.getClass().getSimpleName());
    }

    private void emitCastExpression(StringBuilder js, QinIrCastExpression castExpression) {
        switch (castExpression.typeName()) {
            case "byte", "short", "int", "char" -> {
                js.append("(");
                emitExpression(js, castExpression.expression());
                js.append(" | 0)");
            }
            case "long" -> {
                js.append("Math.trunc(");
                emitExpression(js, castExpression.expression());
                js.append(")");
            }
            case "float", "double" -> {
                js.append("Number(");
                emitExpression(js, castExpression.expression());
                js.append(")");
            }
            case "boolean", "String", "java.lang.String" -> {
                js.append("(");
                emitExpression(js, castExpression.expression());
                js.append(")");
            }
            default -> {
                js.append("(");
                emitExpression(js, castExpression.expression());
                js.append(")");
            }
        }
    }

    private void emitJavaInstanceofPatternExpression(
            StringBuilder js,
            QinIrJavaInstanceofPatternExpression expression) {
        ensureSupportedJavaOwner(expression.ownerBinaryName());
        String ownerReference = javaOwnerReference(expression.ownerBinaryName(), expression.classLocalName());
        js.append("(() => { const __qin_pattern_value = ");
        emitExpression(js, expression.value());
        js.append("; return __qin_pattern_value instanceof ")
                .append(ownerReference)
                .append(" && (")
                .append(declareBindingName(expression.variableName()))
                .append(" = __qin_pattern_value, true)");
        js.append("; })()");
    }

    private void emitLetExpression(StringBuilder js, QinIrLetExpression letExpression) {
        Map<String, String> previousAliases = bindingAliases;
        bindingAliases = new LinkedHashMap<>(previousAliases);
        js.append("(() => {\n");
        for (QinIrLocalVariableDeclaration declaration : letExpression.localDeclarations()) {
            js.append("      let ").append(declareBindingName(declaration.name()));
            emitAnyTypeAnnotation(js);
            js.append(" = ");
            emitExpression(js, declaration.initializer());
            js.append(";\n");
        }
        for (QinIrExpression leadingExpression : letExpression.leadingExpressions()) {
            js.append("      ");
            emitExpression(js, leadingExpression);
            js.append(";\n");
        }
        js.append("      return ");
        emitExpression(js, letExpression.resultExpression());
        js.append(";\n");
        js.append("    })()");
        bindingAliases = previousAliases;
    }

    private void emitIfExpression(StringBuilder js, QinIrIfExpression ifExpression) {
        js.append("(() => {\n");
        js.append("      if (");
        emitExpression(js, ifExpression.test());
        js.append(") {\n");
        js.append("        return ");
        emitExpression(js, ifExpression.consequent());
        js.append(";\n");
        js.append("      }\n");
        js.append("      return ");
        emitExpression(js, ifExpression.alternate());
        js.append(";\n");
        js.append("    })()");
    }

    private void emitWhileExpression(StringBuilder js, QinIrWhileExpression whileExpression) {
        Map<String, String> previousAliases = bindingAliases;
        bindingAliases = new LinkedHashMap<>(previousAliases);
        js.append("(() => {\n");
        js.append("      while (");
        emitExpression(js, whileExpression.test());
        js.append(") {\n");
        for (QinIrLocalVariableDeclaration declaration : whileExpression.localDeclarations()) {
            js.append("        let ").append(declareBindingName(declaration.name()));
            emitAnyTypeAnnotation(js);
            js.append(" = ");
            emitExpression(js, declaration.initializer());
            js.append(";\n");
        }
        for (QinIrExpression bodyExpression : whileExpression.bodyExpressions()) {
            js.append("        ");
            emitExpression(js, bodyExpression);
            js.append(";\n");
        }
        js.append("      }\n");
        js.append("      return null;\n");
        js.append("    })()");
        bindingAliases = previousAliases;
    }

    private void emitDoWhileExpression(StringBuilder js, QinIrDoWhileExpression doWhileExpression) {
        Map<String, String> previousAliases = bindingAliases;
        bindingAliases = new LinkedHashMap<>(previousAliases);
        js.append("(() => {\n");
        js.append("      do {\n");
        for (QinIrLocalVariableDeclaration declaration : doWhileExpression.localDeclarations()) {
            js.append("        let ").append(declareBindingName(declaration.name()));
            emitAnyTypeAnnotation(js);
            js.append(" = ");
            emitExpression(js, declaration.initializer());
            js.append(";\n");
        }
        for (QinIrExpression bodyExpression : doWhileExpression.bodyExpressions()) {
            js.append("        ");
            emitExpression(js, bodyExpression);
            js.append(";\n");
        }
        js.append("      } while (");
        emitExpression(js, doWhileExpression.test());
        js.append(");\n");
        js.append("      return null;\n");
        js.append("    })()");
        bindingAliases = previousAliases;
    }

    private void emitForExpression(StringBuilder js, QinIrForExpression forExpression) {
        Map<String, String> previousAliases = bindingAliases;
        bindingAliases = new LinkedHashMap<>(previousAliases);
        js.append("(() => {\n");
        js.append("      for (");
        emitForInitializers(js, forExpression);
        js.append("; ");
        if (forExpression.test() != null) {
            emitExpression(js, forExpression.test());
        }
        js.append("; ");
        emitCommaSeparatedExpressions(js, forExpression.updateExpressions());
        js.append(") {\n");
        for (QinIrLocalVariableDeclaration declaration : forExpression.bodyLocalDeclarations()) {
            js.append("        let ").append(declareBindingName(declaration.name()));
            emitAnyTypeAnnotation(js);
            js.append(" = ");
            emitExpression(js, declaration.initializer());
            js.append(";\n");
        }
        for (QinIrExpression bodyExpression : forExpression.bodyExpressions()) {
            js.append("        ");
            emitExpression(js, bodyExpression);
            js.append(";\n");
        }
        js.append("      }\n");
        js.append("      return null;\n");
        js.append("    })()");
        bindingAliases = previousAliases;
    }

    private void emitForEachExpression(StringBuilder js, QinIrForEachExpression forEachExpression) {
        Map<String, String> previousAliases = bindingAliases;
        bindingAliases = new LinkedHashMap<>(previousAliases);
        js.append("(() => {\n");
        js.append("      for (const ").append(declareBindingName(forEachExpression.itemName()));
        emitAnyTypeAnnotation(js);
        js.append(" of ");
        emitExpression(js, forEachExpression.iterable());
        js.append(") {\n");
        for (QinIrLocalVariableDeclaration declaration : forEachExpression.bodyLocalDeclarations()) {
            js.append("        let ").append(declareBindingName(declaration.name()));
            emitAnyTypeAnnotation(js);
            js.append(" = ");
            emitExpression(js, declaration.initializer());
            js.append(";\n");
        }
        for (QinIrExpression bodyExpression : forEachExpression.bodyExpressions()) {
            js.append("        ");
            emitExpression(js, bodyExpression);
            js.append(";\n");
        }
        js.append("      }\n");
        js.append("      return null;\n");
        js.append("    })()");
        bindingAliases = previousAliases;
    }

    private void emitForInitializers(StringBuilder js, QinIrForExpression forExpression) {
        boolean emitted = false;
        if (!forExpression.initializerDeclarations().isEmpty()) {
            js.append("let ");
            for (int i = 0; i < forExpression.initializerDeclarations().size(); i++) {
                if (i > 0) {
                    js.append(", ");
                }
                QinIrLocalVariableDeclaration declaration = forExpression.initializerDeclarations().get(i);
                js.append(declareBindingName(declaration.name())).append(" = ");
                emitExpression(js, declaration.initializer());
            }
            emitted = true;
        }
        if (!forExpression.initializerExpressions().isEmpty()) {
            if (emitted) {
                js.append(", ");
            }
            emitCommaSeparatedExpressions(js, forExpression.initializerExpressions());
        }
    }

    private void emitCommaSeparatedExpressions(StringBuilder js, List<QinIrExpression> expressions) {
        for (int i = 0; i < expressions.size(); i++) {
            if (i > 0) {
                js.append(", ");
            }
            emitExpression(js, expressions.get(i));
        }
    }

    private void emitSequenceExpression(StringBuilder js, QinIrSequenceExpression sequenceExpression) {
        js.append("(() => {\n");
        for (QinIrExpression leadingExpression : sequenceExpression.leadingExpressions()) {
            js.append("      ");
            emitExpression(js, leadingExpression);
            js.append(";\n");
        }
        js.append("      return ");
        emitExpression(js, sequenceExpression.resultExpression());
        js.append(";\n");
        js.append("    })()");
    }

    private void ensureSupportedJavaOwner(String ownerBinaryName) {
        if (isGeneratedClassOwner(ownerBinaryName)) {
            return;
        }
        if (externallyBoundJavaBinaryNames.contains(ownerBinaryName)) {
            return;
        }
        if ("java.util.ArrayList".equals(ownerBinaryName)
                || "java.util.List".equals(ownerBinaryName)
                || "java.util.Set".equals(ownerBinaryName)
                || "java.util.Arrays".equals(ownerBinaryName)
                || "java.util.Collections".equals(ownerBinaryName)
                || "java.util.HashSet".equals(ownerBinaryName)
                || "java.util.LinkedHashSet".equals(ownerBinaryName)
                || "java.util.HashMap".equals(ownerBinaryName)
                || "java.util.LinkedHashMap".equals(ownerBinaryName)
                || "java.util.concurrent.ConcurrentHashMap".equals(ownerBinaryName)
                || "java.util.concurrent.ConcurrentMap".equals(ownerBinaryName)
                || "java.util.Objects".equals(ownerBinaryName)
                || "java.util.Optional".equals(ownerBinaryName)
                || "java.util.stream.Collectors".equals(ownerBinaryName)
                || "java.lang.String".equals(ownerBinaryName)
                || "java.lang.StringBuilder".equals(ownerBinaryName)
                || "java.lang.Integer".equals(ownerBinaryName)
                || "java.lang.Double".equals(ownerBinaryName)
                || "java.lang.Throwable".equals(ownerBinaryName)
                || "java.lang.Exception".equals(ownerBinaryName)
                || "java.lang.RuntimeException".equals(ownerBinaryName)
                || "java.lang.ReflectiveOperationException".equals(ownerBinaryName)
                || "java.lang.ClassNotFoundException".equals(ownerBinaryName)
                || "java.lang.NoSuchMethodException".equals(ownerBinaryName)
                || "java.lang.NumberFormatException".equals(ownerBinaryName)
                || "java.lang.UnsupportedOperationException".equals(ownerBinaryName)
                || "java.lang.Error".equals(ownerBinaryName)
                || "java.lang.StackOverflowError".equals(ownerBinaryName)
                || "java.lang.IllegalArgumentException".equals(ownerBinaryName)
                || "java.lang.IllegalStateException".equals(ownerBinaryName)
                || "java.lang.System".equals(ownerBinaryName)
                || "java.io.IOException".equals(ownerBinaryName)
                || "java.lang.Math".equals(ownerBinaryName)
                || "java.io.File".equals(ownerBinaryName)
                || "java.io.FileWriter".equals(ownerBinaryName)
                || "java.io.BufferedWriter".equals(ownerBinaryName)
                || "java.nio.file.Path".equals(ownerBinaryName)
                || "java.nio.file.Paths".equals(ownerBinaryName)
                || "java.nio.file.Files".equals(ownerBinaryName)
                || "java.time.LocalDateTime".equals(ownerBinaryName)
                || "java.time.format.DateTimeFormatter".equals(ownerBinaryName)
                || "java.util.regex.Pattern".equals(ownerBinaryName)
                || "java.util.regex.Matcher".equals(ownerBinaryName)
                || "java.util.concurrent.atomic.AtomicLong".equals(ownerBinaryName)
                || "com.github.benmanes.caffeine.cache.Caffeine".equals(ownerBinaryName)
                || "com.github.benmanes.caffeine.cache.Cache".equals(ownerBinaryName)
                || "com.github.benmanes.caffeine.cache.RemovalCause".equals(ownerBinaryName)
                || "org.slf4j.LoggerFactory".equals(ownerBinaryName)
                || "org.slf4j.Logger".equals(ownerBinaryName)) {
            return;
        }
        if (loadJavaOwner(ownerBinaryName).isRecord()) {
            return;
        }
        throw new IllegalArgumentException("JS backend does not support Java interop owner yet: " + ownerBinaryName);
    }

    private String javaOwnerReference(String ownerBinaryName, String classLocalName) {
        if (isGeneratedClassOwner(ownerBinaryName)) {
            return jsClassReference(ownerBinaryName);
        }
        if (externallyBoundJavaBinaryNames.contains(ownerBinaryName)) {
            return jsClassReference(ownerBinaryName);
        }
        String runtimeOwner = switch (ownerBinaryName) {
            case "java.lang.Math" -> "Math";
            case "java.lang.String" -> "__QinJavaLangString";
            case "java.lang.Boolean" -> "__QinJavaLangBoolean";
            case "java.lang.StringBuilder" -> "__QinJavaLangStringBuilder";
            case "java.lang.Integer" -> "__QinJavaLangInteger";
            case "java.lang.Double" -> "__QinJavaLangDouble";
            case "java.lang.Throwable" -> "__QinJavaLangThrowable";
            case "java.lang.Exception" -> "__QinJavaLangException";
            case "java.lang.RuntimeException" -> "__QinJavaLangRuntimeException";
            case "java.lang.ReflectiveOperationException" -> "__QinJavaLangReflectiveOperationException";
            case "java.lang.ClassNotFoundException" -> "__QinJavaLangClassNotFoundException";
            case "java.lang.NoSuchMethodException" -> "__QinJavaLangNoSuchMethodException";
            case "java.lang.NumberFormatException" -> "__QinJavaLangNumberFormatException";
            case "java.lang.UnsupportedOperationException" -> "__QinJavaLangUnsupportedOperationException";
            case "java.lang.Error" -> "__QinJavaLangError";
            case "java.lang.StackOverflowError" -> "__QinJavaLangStackOverflowError";
            case "java.lang.IllegalArgumentException" -> "__QinJavaLangIllegalArgumentException";
            case "java.lang.IllegalStateException" -> "__QinJavaLangIllegalStateException";
            case "java.lang.System" -> "__QinJavaLangSystem";
            case "java.io.IOException" -> "__QinJavaIoIOException";
            case "java.io.File" -> "__QinJavaIoFile";
            case "java.io.FileWriter" -> "__QinJavaIoFileWriter";
            case "java.io.BufferedWriter" -> "__QinJavaIoBufferedWriter";
            case "java.nio.file.Path" -> "__QinJavaNioFilePath";
            case "java.nio.file.Paths" -> "__QinJavaNioFilePaths";
            case "java.nio.file.Files" -> "__QinJavaNioFileFiles";
            case "java.time.LocalDateTime" -> "__QinJavaTimeLocalDateTime";
            case "java.time.format.DateTimeFormatter" -> "__QinJavaTimeFormatDateTimeFormatter";
            case "java.util.List" -> "__QinJavaUtilList";
            case "java.util.Set" -> "__QinJavaUtilSet";
            case "java.util.Arrays" -> "__QinJavaUtilArrays";
            case "java.util.Collections" -> "__QinJavaUtilCollections";
            case "java.util.ArrayList" -> "__QinJavaUtilArrayList";
            case "java.util.HashSet", "java.util.LinkedHashSet" -> "__QinJavaUtilHashSet";
            case "java.util.HashMap", "java.util.LinkedHashMap",
                    "java.util.concurrent.ConcurrentHashMap", "java.util.concurrent.ConcurrentMap" ->
                    "__QinJavaUtilHashMap";
            case "java.util.Objects" -> "__QinJavaUtilObjects";
            case "java.util.Optional" -> "__QinJavaUtilOptional";
            case "java.util.stream.Collectors" -> "__QinJavaUtilStreamCollectors";
            case "java.util.regex.Pattern" -> "__QinJavaUtilRegexPattern";
            case "java.util.regex.Matcher" -> "__QinJavaUtilRegexMatcher";
            case "java.util.concurrent.atomic.AtomicLong" -> "__QinJavaUtilConcurrentAtomicLong";
            case "com.github.benmanes.caffeine.cache.Caffeine" -> "__QinCaffeine";
            case "com.github.benmanes.caffeine.cache.Cache" -> "__QinCaffeineCache";
            case "com.github.benmanes.caffeine.cache.RemovalCause" -> "__QinCaffeineRemovalCause";
            case "org.slf4j.LoggerFactory" -> "__QinSlf4jLoggerFactory";
            case "org.slf4j.Logger" -> "__QinSlf4jLogger";
            default -> null;
        };
        if (runtimeOwner != null) {
            requireExternalJavaSdkRuntime(runtimeOwner);
            return runtimeOwner;
        }
        if (isJsIdentifier(classLocalName)) {
            return classLocalName;
        }
        return null;
    }

    private String memberAccessObjectReference(String objectName) {
        if (generatedClassBinaryNames.contains(objectName)) {
            return jsClassReference(objectName);
        }
        String generatedReference = generatedClassReferencesBySimpleName.get(objectName);
        if (generatedReference != null) {
            return generatedReference;
        }
        return jsBindingName(objectName);
    }

    private String javaFieldAwarePropertyName(QinIrPropertyAccessExpression propertyAccessExpression) {
        if (propertyAccessExpression.receiver() instanceof QinIrThisExpression) {
            return jsCurrentJavaFieldName(propertyAccessExpression.propertyName());
        }
        return generatedJavaFieldAliases.getOrDefault(
                propertyAccessExpression.propertyName(),
                propertyAccessExpression.propertyName());
    }

    private String javaMemberAccessPropertyName(QinIrMemberAccessExpression memberAccessExpression) {
        if (generatedClassBinaryNames.contains(memberAccessExpression.objectName())
                && currentJavaFieldAliases.containsKey(memberAccessExpression.propertyName())) {
            return jsCurrentJavaFieldName(memberAccessExpression.propertyName());
        }
        return generatedJavaFieldAliases.getOrDefault(
                memberAccessExpression.propertyName(),
                memberAccessExpression.propertyName());
    }

    private Class<?> loadJavaOwner(String ownerBinaryName) {
        try {
            return Class.forName(ownerBinaryName);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java interop owner: " + ownerBinaryName, e);
        }
    }

    private void emitArguments(StringBuilder js, List<QinIrExpression> arguments) {
        for (int i = 0; i < arguments.size(); i++) {
            emitExpression(js, arguments.get(i));
            if (i < arguments.size() - 1) {
                js.append(", ");
            }
        }
    }

    private boolean emitJavaLangStringInstanceMethodCall(
            StringBuilder js,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        String sdkMethod = switch (methodCallExpression.methodName()) {
            case "length" -> methodCallExpression.arguments().isEmpty() ? "length" : null;
            case "equals" -> methodCallExpression.arguments().size() == 1 ? "equals" : null;
            case "contains" -> methodCallExpression.arguments().size() == 1 ? "contains" : null;
            case "isEmpty" -> methodCallExpression.arguments().isEmpty() ? "isEmpty" : null;
            case "isBlank" -> methodCallExpression.arguments().isEmpty() ? "isBlank" : null;
            case "hashCode" -> methodCallExpression.arguments().isEmpty() ? "hashCode" : null;
            case "startsWith" -> methodCallExpression.arguments().size() == 1 ? "startsWith" : null;
            case "endsWith" -> methodCallExpression.arguments().size() == 1 ? "endsWith" : null;
            case "charAt" -> methodCallExpression.arguments().size() == 1 ? "charAt" : null;
            case "substring" -> methodCallExpression.arguments().size() == 1
                    || methodCallExpression.arguments().size() == 2 ? "substring" : null;
            default -> null;
        };
        if (sdkMethod == null) {
            return false;
        }
        js.append("__QinJavaLangString.").append(sdkMethod).append("(");
        emitExpression(js, methodCallExpression.receiver());
        if (!methodCallExpression.arguments().isEmpty()) {
            js.append(", ");
            emitArguments(js, methodCallExpression.arguments());
        }
        js.append(")");
        return true;
    }

    private boolean isSuperReference(QinIrExpression expression) {
        return expression instanceof QinIrIdentifierReference identifierReference
                && "super".equals(identifierReference.name());
    }

    private String superSubhutiRawMethodName(String methodName, int arity) {
        if (currentJavaClassDeclaration == null || currentJavaClassDeclaration.superType() == null) {
            return null;
        }
        QinIrClassDeclaration superClass =
                generatedClassesByBinaryName.get(currentJavaClassDeclaration.superType().binaryName());
        return superSubhutiRawMethodName(superClass, methodName, arity);
    }

    private String superSubhutiRawMethodName(QinIrClassDeclaration classDeclaration, String methodName, int arity) {
        if (classDeclaration == null) {
            return null;
        }
        List<QinIrMethodDeclaration> overloads = new java.util.ArrayList<>();
        for (QinIrMethodDeclaration method : classDeclaration.methods()) {
            if (!method.staticMethod() && method.name().equals(methodName) && !"constructor".equals(method.name())) {
                overloads.add(method);
            }
        }
        if (!overloads.isEmpty()) {
            for (int i = 0; i < overloads.size(); i++) {
                QinIrMethodDeclaration overload = overloads.get(i);
                if (overload.parameters().size() == arity && isSubhutiRuleMethod(overload)) {
                    String jsMethodName = overloads.size() == 1
                            ? overload.name()
                            : overloadedMethodImplementationName(overload.name(), arity, i);
                    return subhutiRawMethodName(jsMethodName);
                }
            }
            return null;
        }
        if (classDeclaration.superType() == null) {
            return null;
        }
        return superSubhutiRawMethodName(
                generatedClassesByBinaryName.get(classDeclaration.superType().binaryName()),
                methodName,
                arity);
    }

    private String escapeJs(String value) {
        StringBuilder out = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '\\' -> out.append("\\\\");
                case '"' -> out.append("\\\"");
                case '\b' -> out.append("\\b");
                case '\t' -> out.append("\\t");
                case '\n' -> out.append("\\n");
                case '\f' -> out.append("\\f");
                case '\r' -> out.append("\\r");
                default -> {
                    if (ch < 0x20) {
                        out.append(String.format("\\u%04x", (int) ch));
                    } else {
                        out.append(ch);
                    }
                }
            }
        }
        return out.toString();
    }

    private String lastDeclarationName(List<QinIrConstDeclaration> declarations) {
        if (declarations.isEmpty()) {
            return null;
        }
        return declarations.get(declarations.size() - 1).name();
    }

}
