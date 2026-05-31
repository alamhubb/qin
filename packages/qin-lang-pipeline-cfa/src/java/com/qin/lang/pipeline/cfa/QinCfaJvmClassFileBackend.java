package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;
import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeBuilder;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Minimal JVM backend based on JDK Class-File API.
 */
public final class QinCfaJvmClassFileBackend {
    private static final ClassDesc OBJECT_DESC = ClassDesc.of("java.lang.Object");
    private static final ClassDesc OBJECT_ARRAY_DESC = ClassDesc.ofDescriptor("[Ljava/lang/Object;");
    private static final String LAST_VALUE_FIELD_NAME = "__qin_last_value";
    private static final ClassDesc LINKED_HASH_MAP_DESC = ClassDesc.of("java.util.LinkedHashMap");
    private static final ClassDesc ARRAY_LIST_DESC = ClassDesc.of("java.util.ArrayList");
    private static final ClassDesc JS_SDK_CONSOLE_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmConsole");
    private static final ClassDesc JS_SDK_GLOBAL_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmGlobal");
    private static final ClassDesc JS_SDK_JSON_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmJson");
    private static final ClassDesc INTEGER_DESC = ClassDesc.of("java.lang.Integer");
    private static final ClassDesc DOUBLE_DESC = ClassDesc.of("java.lang.Double");
    private static final ClassDesc BOOLEAN_DESC = ClassDesc.of("java.lang.Boolean");
    private static final ClassDesc STRING_BUILDER_DESC = ClassDesc.of("java.lang.StringBuilder");

    private static final MethodTypeDesc VOID_INIT = MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc RUN_SIGNATURE = MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;");
    private static final MethodTypeDesc RUN_CHUNK_SIGNATURE = MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc MAIN_SIGNATURE = MethodTypeDesc.ofDescriptor("([Ljava/lang/String;)V");
    private static final MethodTypeDesc CONSOLE_LOG_SIGNATURE = MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)V");
    private static final MethodTypeDesc INTEGER_VALUE_OF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;");
    private static final MethodTypeDesc DOUBLE_VALUE_OF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;");
    private static final MethodTypeDesc BOOLEAN_VALUE_OF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/Boolean;");
    private static final MethodTypeDesc FUNCTION_CONSTANT_RETURN_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc EXPORT_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc GLOBAL_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc VALUE_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MEMBER_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc BIND_GLOBAL_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc BIND_MODULE_REF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MARK_MODULE_REF_INITIALIZED_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc REGISTER_JS_IMPORT_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MAP_PUT_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MAP_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc LIST_ADD_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z");
    private static final MethodTypeDesc JSON_PARSE_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)Ljava/lang/Object;");
    private static final MethodTypeDesc STRING_BUILDER_INIT_SIGNATURE =
            MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc STRING_BUILDER_APPEND_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)Ljava/lang/StringBuilder;");
    private static final MethodTypeDesc STRING_BUILDER_TO_STRING_SIGNATURE =
            MethodTypeDesc.ofDescriptor("()Ljava/lang/String;");
    private static final int DECLARATION_CHUNK_SIZE = 16;
    private static final int RUNTIME_CHUNK_SIZE = 64;
    private static final int JSON_LITERAL_EMIT_THRESHOLD = 1024;
    private static final int JSON_LITERAL_SERIALIZE_LIMIT = 2_000_000;
    private static final int STRING_CONSTANT_CHUNK_SIZE = 12_000;
    private static final List<ChunkSizing> CHUNK_SIZING_FALLBACKS = List.of(
            new ChunkSizing(DECLARATION_CHUNK_SIZE, RUNTIME_CHUNK_SIZE),
            new ChunkSizing(8, 32),
            new ChunkSizing(4, 16),
            new ChunkSizing(2, 8),
            new ChunkSizing(1, 4),
            new ChunkSizing(1, 2),
            new ChunkSizing(1, 1));
    private static final boolean DEBUG_BACKEND = Boolean.getBoolean("qin.debug.backend");
    private Set<String> runtimeImportedGlobalNames = Set.of();

    public byte[] compileProgram(QinCfaProgram program, String className) {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(className, "className cannot be null");

        long startNanos = System.nanoTime();
        logBackendPhase("compile start", startNanos, className);
        validateExecutionPlan(program);
        BindingPlan bindingPlan = buildBindingPlan(program.declarations());
        logLargestDeclarationInitializers(program, startNanos);
        runtimeImportedGlobalNames = collectRuntimeImportedGlobalNames(program);
        ClassDesc generatedClassDesc = ClassDesc.of(className);
        IllegalArgumentException lastTooLargeError = null;
        try {
            for (ChunkSizing sizing : CHUNK_SIZING_FALLBACKS) {
                try {
                    logBackendPhase("build attempt start", startNanos, sizing.toString());
                    byte[] bytes = buildClassBytes(program, bindingPlan, generatedClassDesc, sizing);
                    logBackendPhase("build attempt done", startNanos, "bytes=" + bytes.length + ", " + sizing);
                    return bytes;
                } catch (IllegalArgumentException error) {
                    if (!isMethodTooLargeError(error)) {
                        throw error;
                    }
                    lastTooLargeError = error;
                    logBackendPhase("build attempt too large", startNanos, sizing.toString());
                }
            }
            if (lastTooLargeError != null) {
                throw lastTooLargeError;
            }
            throw new IllegalStateException("Failed to compile program with chunk fallbacks");
        } finally {
            runtimeImportedGlobalNames = Set.of();
        }
    }

    private byte[] buildClassBytes(
            QinCfaProgram program,
            BindingPlan bindingPlan,
            ClassDesc generatedClassDesc,
            ChunkSizing sizing) {
        long startNanos = System.nanoTime();
        Set<String> eagerGlobalBindingNames = collectEagerGlobalBindingNames(program);
        List<ChunkMethodSpec> chunkMethods = buildChunkMethods(
                bindingPlan,
                program,
                sizing.runtimeChunkSize());
        logBackendPhase(
                "classfile build start",
                startNanos,
                "chunks=" + chunkMethods.size() + ", declarations=" + program.declarations().size()
                        + ", steps=" + program.executionSteps().size() + ", " + sizing);
        ClassFile classFile = ClassFile.of();
        byte[] bytes = classFile.build(generatedClassDesc, builder -> {
            builder.withFlags(ClassFile.ACC_PUBLIC | ClassFile.ACC_SUPER);

            builder.withMethodBody("<init>", VOID_INIT, ClassFile.ACC_PUBLIC, code -> {
                code.aload(0);
                code.invokespecial(OBJECT_DESC, "<init>", VOID_INIT);
                code.return_();
            });

            for (String fieldName : bindingPlan.fieldNamesByIndex()) {
                builder.withField(
                        fieldName,
                        OBJECT_DESC,
                        ClassFile.ACC_PRIVATE | ClassFile.ACC_STATIC);
            }
            builder.withField(
                    LAST_VALUE_FIELD_NAME,
                    OBJECT_DESC,
                    ClassFile.ACC_PRIVATE | ClassFile.ACC_STATIC);

            for (ChunkMethodSpec chunkMethod : chunkMethods) {
                builder.withMethodBody(
                        chunkMethod.methodName(),
                        RUN_CHUNK_SIGNATURE,
                        ClassFile.ACC_PRIVATE | ClassFile.ACC_STATIC,
                        code -> emitChunkMethod(
                                code,
                                generatedClassDesc,
                                chunkMethod,
                                bindingPlan,
                                program,
                                eagerGlobalBindingNames));
            }

            builder.withMethodBody("run", RUN_SIGNATURE, ClassFile.ACC_PUBLIC | ClassFile.ACC_STATIC,
                    code -> emitRunMethod(code, generatedClassDesc, chunkMethods, bindingPlan, program));

            builder.withMethodBody("main", MAIN_SIGNATURE, ClassFile.ACC_PUBLIC | ClassFile.ACC_STATIC,
                    code -> emitMainMethod(code, generatedClassDesc));
        });
        logBackendPhase("classfile build done", startNanos, "bytes=" + bytes.length);
        return bytes;
    }

    private static void logBackendPhase(String phase, long startNanos, String detail) {
        if (!DEBUG_BACKEND) {
            return;
        }
        long elapsedMillis = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinCfaJvmClassFileBackend] " + phase + " +" + elapsedMillis + "ms :: " + detail);
    }

    private void logLargestDeclarationInitializers(QinCfaProgram program, long startNanos) {
        if (!DEBUG_BACKEND) {
            return;
        }
        List<String> largest = new ArrayList<>();
        for (QinCfaProgram.ConstDeclaration declaration : program.declarations()) {
            int size = expressionSize(declaration.initializer(), 20_000);
            largest.add(size + " :: " + declaration.name() + " :: "
                    + declaration.initializer().getClass().getSimpleName());
        }
        largest.sort((left, right) -> Integer.compare(parseLeadingInt(right), parseLeadingInt(left)));
        int limit = Math.min(10, largest.size());
        for (int i = 0; i < limit; i++) {
            logBackendPhase("large initializer", startNanos, largest.get(i));
        }
    }

    private int expressionSize(QinCfaProgram.Expression expression, int limit) {
        if (expression == null) {
            return 0;
        }
        if (limit <= 0) {
            return 1;
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            int size = 1;
            for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
                size += 1 + expressionSize(property.value(), limit - size);
                if (size >= limit) {
                    return size;
                }
            }
            return size;
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            int size = 1;
            for (QinCfaProgram.Expression element : arrayLiteral.elements()) {
                size += expressionSize(element, limit - size);
                if (size >= limit) {
                    return size;
                }
            }
            return size;
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
            int size = 1;
            for (QinCfaProgram.Expression argument : builtinCallExpression.arguments()) {
                size += expressionSize(argument, limit - size);
                if (size >= limit) {
                    return size;
                }
            }
            return size;
        }
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            return 1 + expressionSize(functionLiteral.returnExpression(), limit - 1);
        }
        return 1;
    }

    private static int parseLeadingInt(String text) {
        int end = 0;
        while (end < text.length() && Character.isDigit(text.charAt(end))) {
            end++;
        }
        return end == 0 ? 0 : Integer.parseInt(text.substring(0, end));
    }

    private boolean isMethodTooLargeError(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            String message = current.getMessage();
            if (message != null
                    && message.contains("Code length")
                    && message.contains("outside the allowed range")) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    private void emitMainMethod(CodeBuilder code, ClassDesc generatedClassDesc) {
        code.invokestatic(generatedClassDesc, "run", RUN_SIGNATURE);
        code.pop();
        code.return_();
    }

    private void emitRunMethod(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            List<ChunkMethodSpec> chunkMethods,
            BindingPlan bindingPlan,
            QinCfaProgram program) {
        emitModuleRefRegistrations(code, bindingPlan);
        emitRuntimeJsImportRegistrations(code, program);
        for (ChunkMethodSpec chunkMethod : chunkMethods) {
            code.invokestatic(generatedClassDesc, chunkMethod.methodName(), RUN_CHUNK_SIGNATURE);
        }
        code.getstatic(generatedClassDesc, LAST_VALUE_FIELD_NAME, OBJECT_DESC);
        code.areturn();
    }

    private void emitModuleRefRegistrations(CodeBuilder code, BindingPlan bindingPlan) {
        for (int i = 0; i < bindingPlan.declarationSteps().size(); i++) {
            DeclarationStep declarationStep = bindingPlan.declarationSteps().get(i);
            DeclarationBinding declarationBinding =
                    bindingPlan.declarationBindingsByStep().get(i);
            code.ldc(declarationStep.name());
            code.ldc(declarationBinding.fieldName());
            code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_bind_module_ref__", BIND_MODULE_REF_SIGNATURE);
            code.pop();
        }
    }

    private void emitRuntimeJsImportRegistrations(CodeBuilder code, QinCfaProgram program) {
        for (QinCfaProgram.JsImport jsImport : program.jsImports()) {
            String moduleName = jsImport.moduleName();
            String localName = jsImport.localName();
            if (moduleName == null || moduleName.isBlank()
                    || localName == null || localName.isBlank()
                    || !isRuntimeJsImportModule(moduleName)) {
                continue;
            }
            code.ldc(localName);
            code.ldc(moduleName);
            code.ldc(jsImport.importedName() == null ? "" : jsImport.importedName());
            code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_register_js_import__", REGISTER_JS_IMPORT_SIGNATURE);
            code.pop();
        }
    }

    private void emitChunkMethod(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            ChunkMethodSpec chunkMethod,
            BindingPlan bindingPlan,
            QinCfaProgram program,
            Set<String> eagerGlobalBindingNames) {
        Map<String, DeclarationBinding> activeBindings =
                new LinkedHashMap<>(chunkMethod.bindingsBeforeChunk());
        for (int i = chunkMethod.fromInclusive(); i < chunkMethod.toExclusive(); i++) {
            QinCfaProgram.TopLevelExecutionStep step = program.executionSteps().get(i);
            switch (step.kind()) {
                case DECLARATION -> {
                    DeclarationStep declarationStep = bindingPlan.declarationSteps().get(step.index());
                    DeclarationBinding declarationBinding =
                            bindingPlan.declarationBindingsByStep().get(step.index());
                    emitDeclarationInitializer(
                            code,
                            generatedClassDesc,
                            activeBindings,
                            declarationStep.name(),
                            declarationStep.initializer(),
                            declarationBinding.fieldName());
                    activeBindings.put(declarationStep.name(), declarationBinding);
                    if (eagerGlobalBindingNames.contains(declarationStep.name())) {
                        code.ldc(declarationStep.name());
                        code.getstatic(generatedClassDesc, declarationBinding.fieldName(), OBJECT_DESC);
                        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_bind_global__", BIND_GLOBAL_SIGNATURE);
                        code.pop();
                    }
                }
                case EXPRESSION_STATEMENT -> {
                    emitExpressionAsObject(
                            code,
                            generatedClassDesc,
                            activeBindings,
                            program.expressionStatements().get(step.index()).expression());
                    code.dup();
                    code.putstatic(generatedClassDesc, LAST_VALUE_FIELD_NAME, OBJECT_DESC);
                    code.pop();
                }
                case CONSOLE_VALUE -> emitConsoleValueLog(
                        code,
                        generatedClassDesc,
                        activeBindings,
                        program.consoleValueLogs().get(step.index()));
                case CONSOLE_OBJECT -> emitObjectConsoleLog(
                        code,
                        generatedClassDesc,
                        activeBindings,
                        program.consoleLogs().get(step.index()));
                case JAVA_STATIC_CONSOLE -> emitJavaStaticConsoleLog(
                        code,
                        program.javaStaticConsoleLogs().get(step.index()));
                case JAVA_INSTANCE_CALL -> emitJavaInstanceMethodCall(
                        code,
                        generatedClassDesc,
                        activeBindings,
                        program.javaInstanceMethodCalls().get(step.index()));
                case JAVA_INSTANCE_CONSOLE -> emitJavaInstanceConsoleLog(
                        code,
                        generatedClassDesc,
                        activeBindings,
                        program.javaInstanceConsoleLogs().get(step.index()));
            }
        }
        code.return_();
    }

    private Set<String> collectEagerGlobalBindingNames(QinCfaProgram program) {
        Set<String> names = new LinkedHashSet<>();
        for (QinCfaProgram.ExpressionStatement statement : program.expressionStatements()) {
            QinCfaProgram.Expression expression = statement.expression();
            if (!(expression instanceof QinCfaProgram.BuiltinCallExpression builtinCall)) {
                continue;
            }
            if (!"Global".equals(builtinCall.receiverName())
                    || !"__qin_bind_global__".equals(builtinCall.methodName())
                    || builtinCall.arguments().isEmpty()) {
                continue;
            }
            QinCfaProgram.Expression firstArgument = builtinCall.arguments().get(0);
            if (firstArgument instanceof QinCfaProgram.StringLiteral stringLiteral
                    && stringLiteral.value() != null
                    && !stringLiteral.value().isBlank()) {
                names.add(stringLiteral.value());
            }
        }
        return names;
    }

    private BindingPlan buildBindingPlan(List<QinCfaProgram.ConstDeclaration> declarations) {
        List<DeclarationStep> declarationSteps = new ArrayList<>();
        List<DeclarationBinding> declarationBindingsByStep = new ArrayList<>();
        List<String> fieldNamesByIndex = new ArrayList<>();
        int nextIndex = 0;
        int lastDeclarationIndex = -1;

        for (QinCfaProgram.ConstDeclaration declaration : declarations) {
            int bindingIndex = nextIndex++;
            String fieldName = "__qin_decl_" + bindingIndex;
            fieldNamesByIndex.add(fieldName);
            declarationSteps.add(new DeclarationStep(declaration.name(), declaration.initializer()));
            declarationBindingsByStep.add(new DeclarationBinding(bindingIndex, fieldName, declaration.initializer()));
            lastDeclarationIndex = bindingIndex;
        }

        return new BindingPlan(declarationSteps, declarationBindingsByStep, fieldNamesByIndex, lastDeclarationIndex);
    }

    private void validateExecutionPlan(QinCfaProgram program) {
        for (QinCfaProgram.TopLevelExecutionStep step : program.executionSteps()) {
            int index = step.index();
            int maxExclusive = switch (step.kind()) {
                case DECLARATION -> program.declarations().size();
                case EXPRESSION_STATEMENT -> program.expressionStatements().size();
                case CONSOLE_VALUE -> program.consoleValueLogs().size();
                case CONSOLE_OBJECT -> program.consoleLogs().size();
                case JAVA_STATIC_CONSOLE -> program.javaStaticConsoleLogs().size();
                case JAVA_INSTANCE_CALL -> program.javaInstanceMethodCalls().size();
                case JAVA_INSTANCE_CONSOLE -> program.javaInstanceConsoleLogs().size();
            };
            if (index < 0 || index >= maxExclusive) {
                throw new IllegalArgumentException(
                        "Invalid execution step index: kind=" + step.kind() + ", index=" + index);
            }
        }
    }

    private List<ChunkMethodSpec> buildChunkMethods(
            BindingPlan bindingPlan,
            QinCfaProgram program,
            int runtimeChunkSize) {
        List<ChunkMethodSpec> chunkMethods = new ArrayList<>();
        int safeChunkSize = Math.max(1, runtimeChunkSize);
        int methodIndex = 0;
        int statementCount = program.executionSteps().size();
        Map<String, DeclarationBinding> currentBindings = new LinkedHashMap<>();
        for (int from = 0; from < statementCount; from += safeChunkSize) {
            int to = Math.min(statementCount, from + safeChunkSize);
            Map<String, DeclarationBinding> bindingsBeforeChunk = new LinkedHashMap<>(currentBindings);
            chunkMethods.add(new ChunkMethodSpec(
                    "__qin_chunk_" + methodIndex,
                    from,
                    to,
                    bindingsBeforeChunk));
            for (int stepIndex = from; stepIndex < to; stepIndex++) {
                QinCfaProgram.TopLevelExecutionStep step = program.executionSteps().get(stepIndex);
                if (step.kind() == QinCfaProgram.TopLevelStatementKind.DECLARATION) {
                    DeclarationStep declarationStep = bindingPlan.declarationSteps().get(step.index());
                    DeclarationBinding declarationBinding =
                            bindingPlan.declarationBindingsByStep().get(step.index());
                    currentBindings.put(declarationStep.name(), declarationBinding);
                }
            }
            methodIndex++;
        }
        return chunkMethods;
    }

    private void emitDeclarationInitializer(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            String declarationName,
            QinCfaProgram.Expression initializer,
            String fieldName) {
        if (initializer instanceof QinCfaProgram.JavaNewExpression javaNewExpression) {
            emitJavaNewInitializer(code, generatedClassDesc, javaNewExpression, declarationName, fieldName);
            return;
        }
        try {
            emitExpressionAsObject(code, generatedClassDesc, bindings, initializer);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException(
                    error.getMessage()
                            + " while emitting declaration `" + declarationName + "`"
                            + "; initializer=" + initializer,
                    error);
        }
        code.putstatic(generatedClassDesc, fieldName, OBJECT_DESC);
        emitMarkModuleRefInitialized(code, declarationName, fieldName);
    }

    private void emitJavaNewInitializer(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            QinCfaProgram.JavaNewExpression javaNewExpression,
            String declarationName,
            String fieldName) {
        ResolvedConstructor resolvedConstructor = resolveConstructor(
                javaNewExpression.ownerBinaryName(),
                javaNewExpression.arguments());

        ClassDesc ownerDesc = ClassDesc.of(javaNewExpression.ownerBinaryName());
        code.new_(ownerDesc);
        code.dup();
        emitArgumentsForParameters(code, javaNewExpression.arguments(), resolvedConstructor.parameterTypes());
        code.invokespecial(ownerDesc, "<init>", resolvedConstructor.descriptor());
        code.putstatic(generatedClassDesc, fieldName, OBJECT_DESC);
        emitMarkModuleRefInitialized(code, declarationName, fieldName);
    }

    private void emitMarkModuleRefInitialized(CodeBuilder code, String declarationName, String fieldName) {
        code.ldc(declarationName);
        code.ldc(fieldName);
        code.invokestatic(
                JS_SDK_GLOBAL_DESC,
                "__qin_mark_module_ref_initialized__",
                MARK_MODULE_REF_INITIALIZED_SIGNATURE);
        code.pop();
    }

    private void emitObjectConsoleLog(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.ConsoleLogStatement consoleLog) {
        DeclarationBinding binding = bindings.get(consoleLog.objectName());
        if (binding == null) {
            throw new IllegalArgumentException("Unknown object in console.log: " + consoleLog.objectName());
        }
        if (!(binding.initializer() instanceof QinCfaProgram.ObjectLiteral objectLiteral)) {
            throw new IllegalArgumentException(
                    "console.log(object.property) currently supports only object literal declarations: "
                            + consoleLog.objectName());
        }

        boolean propertyExists = objectLiteral.properties().stream()
                .anyMatch(property -> property.key().equals(consoleLog.propertyName()));
        if (!propertyExists) {
            throw new IllegalArgumentException(
                    "Unknown property in console.log: " + consoleLog.propertyName());
        }

        code.getstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_export_get__", EXPORT_GET_SIGNATURE);
        code.checkcast(LINKED_HASH_MAP_DESC);
        code.ldc(consoleLog.propertyName());
        code.invokevirtual(LINKED_HASH_MAP_DESC, "get", MAP_GET_SIGNATURE);
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_export_get__", EXPORT_GET_SIGNATURE);
        code.invokestatic(JS_SDK_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private void emitConsoleValueLog(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.ConsoleLogValue consoleValueLog) {
        emitExpressionAsObject(code, generatedClassDesc, bindings, consoleValueLog.value());
        code.invokestatic(JS_SDK_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private void emitJavaInstanceMethodCall(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.JavaInstanceMethodCall javaInstanceMethodCall) {
        DeclarationBinding binding = requireJavaBinding(bindings, javaInstanceMethodCall.receiverName());
        ResolvedMethod resolvedMethod = resolveInstanceMethod(
                javaInstanceMethodCall.ownerBinaryName(),
                javaInstanceMethodCall.methodName(),
                javaInstanceMethodCall.arguments());

        code.getstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
        emitJavaReceiverCast(code, resolvedMethod);
        emitArgumentsForParameters(code, javaInstanceMethodCall.arguments(), resolvedMethod.parameterTypes());
        invokeInstanceMethod(code, resolvedMethod);
        discardReturnValue(code, resolvedMethod.returnType());
    }

    private void emitJavaStaticConsoleLog(CodeBuilder code, QinCfaProgram.ConsoleLogJavaStaticCall javaStaticCall) {
        ResolvedMethod resolvedMethod = resolveStaticMethod(
                javaStaticCall.ownerBinaryName(),
                javaStaticCall.methodName(),
                javaStaticCall.arguments());

        emitArgumentsForParameters(code, javaStaticCall.arguments(), resolvedMethod.parameterTypes());
        code.invokestatic(
                ClassDesc.of(javaStaticCall.ownerBinaryName()),
                javaStaticCall.methodName(),
                resolvedMethod.descriptor());

        emitBoxIfNeeded(code, resolvedMethod.returnType());
        code.invokestatic(JS_SDK_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private void emitJavaInstanceConsoleLog(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.ConsoleLogJavaInstanceCall javaInstanceConsoleLog) {
        DeclarationBinding binding = requireJavaBinding(bindings, javaInstanceConsoleLog.receiverName());
        ResolvedMethod resolvedMethod = resolveInstanceMethod(
                javaInstanceConsoleLog.ownerBinaryName(),
                javaInstanceConsoleLog.methodName(),
                javaInstanceConsoleLog.arguments());

        code.getstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
        emitJavaReceiverCast(code, resolvedMethod);
        emitArgumentsForParameters(code, javaInstanceConsoleLog.arguments(), resolvedMethod.parameterTypes());
        invokeInstanceMethod(code, resolvedMethod);
        emitBoxIfNeeded(code, resolvedMethod.returnType());
        code.invokestatic(JS_SDK_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private DeclarationBinding requireJavaBinding(Map<String, DeclarationBinding> bindings, String receiverName) {
        DeclarationBinding binding = bindings.get(receiverName);
        if (binding == null) {
            throw new IllegalArgumentException("Unknown Java instance receiver: " + receiverName);
        }
        if (!(binding.initializer() instanceof QinCfaProgram.JavaNewExpression)) {
            throw new IllegalArgumentException("Receiver is not a Java instance binding: " + receiverName);
        }
        return binding;
    }

    private void emitArgumentsForParameters(
            CodeBuilder code,
            List<QinCfaProgram.Expression> arguments,
            Class<?>[] parameterTypes) {
        if (arguments.size() != parameterTypes.length) {
            throw new IllegalArgumentException("Argument count mismatch");
        }
        for (int i = 0; i < arguments.size(); i++) {
            emitExpressionForParameter(code, arguments.get(i), parameterTypes[i]);
        }
    }

    private void emitExpressionAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.Expression expression) {
        if (expression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            code.loadConstant(numberLiteral.value());
            code.invokestatic(DOUBLE_DESC, "valueOf", DOUBLE_VALUE_OF_SIGNATURE);
            return;
        }
        if (expression instanceof QinCfaProgram.StringLiteral stringLiteral) {
            emitStringConstant(code, stringLiteral.value());
            return;
        }
        if (expression instanceof QinCfaProgram.BooleanLiteral booleanLiteral) {
            code.loadConstant(booleanLiteral.value() ? 1 : 0);
            code.invokestatic(BOOLEAN_DESC, "valueOf", BOOLEAN_VALUE_OF_SIGNATURE);
            return;
        }
        if (expression instanceof QinCfaProgram.NullLiteral) {
            code.aconst_null();
            return;
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            if (emitLargeStaticJsonLiteralIfPossible(code, expression)) {
                return;
            }
            emitObjectLiteralAsObject(code, generatedClassDesc, bindings, objectLiteral);
            return;
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            if (emitLargeStaticJsonLiteralIfPossible(code, expression)) {
                return;
            }
            emitArrayLiteralAsObject(code, generatedClassDesc, bindings, arrayLiteral);
            return;
        }
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            emitExpressionAsObject(code, generatedClassDesc, bindings, functionLiteral.returnExpression());
            code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_constant_return_function__", FUNCTION_CONSTANT_RETURN_SIGNATURE);
            return;
        }
        if (expression instanceof QinCfaProgram.MemberAccessExpression memberAccessExpression) {
            emitMemberAccessAsObject(code, generatedClassDesc, bindings, memberAccessExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            DeclarationBinding binding = bindings.get(identifierReference.name());
            if (binding == null) {
                if (emitKnownGlobalIdentifier(code, identifierReference.name())) {
                    return;
                }
                throw new IllegalArgumentException("QJS2008 unknown identifier: " + identifierReference.name());
            }
            code.getstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
            return;
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
            emitBuiltinCallAsObject(code, generatedClassDesc, bindings, builtinCallExpression);
            return;
        }

        throw new IllegalArgumentException("Unsupported object expression type: "
                + expression.getClass().getSimpleName());
    }

    private boolean emitKnownGlobalIdentifier(CodeBuilder code, String name) {
        if ("undefined".equals(name)) {
            code.aconst_null();
            return true;
        }
        if (runtimeImportedGlobalNames.contains(name)) {
            code.ldc(name);
            code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_global__", GLOBAL_GET_SIGNATURE);
            return true;
        }
        if ("process".equals(name)
                || "window".equals(name)
                || "self".equals(name)
                || "global".equals(name)
                || "globalThis".equals(name)
                || "Math".equals(name)
                || "JSON".equals(name)
                || "Number".equals(name)
                || "Object".equals(name)
                || "Array".equals(name)
                || "Date".equals(name)
                || "Uint8Array".equals(name)
                || "String".equals(name)
                || "Boolean".equals(name)
                || "RegExp".equals(name)
                || "Function".equals(name)
                || "Map".equals(name)
                || "Set".equals(name)
                || "WeakMap".equals(name)
                || "WeakSet".equals(name)
                || "Error".equals(name)
                || "TypeError".equals(name)
                || "RangeError".equals(name)
                || "Promise".equals(name)
                || "Symbol".equals(name)
                || "parseInt".equals(name)
                || "parseFloat".equals(name)
                || "isNaN".equals(name)
                || "isFinite".equals(name)
                || "Infinity".equals(name)
                || "NaN".equals(name)
                || "module".equals(name)
                || "exports".equals(name)
                || "require".equals(name)
                || "crypto".equals(name)
                || "performance".equals(name)
                || "this".equals(name)) {
            code.ldc(name);
            code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_global__", GLOBAL_GET_SIGNATURE);
            return true;
        }
        return false;
    }

    private Set<String> collectRuntimeImportedGlobalNames(QinCfaProgram program) {
        Set<String> names = new LinkedHashSet<>();
        for (QinCfaProgram.JsImport jsImport : program.jsImports()) {
            String moduleName = jsImport.moduleName();
            if (moduleName == null || moduleName.isBlank()) {
                continue;
            }
            if (isRuntimeJsImportModule(moduleName)) {
                names.add(jsImport.localName());
            }
        }
        return names;
    }

    private static boolean isRuntimeJsImportModule(String moduleName) {
        return moduleName.startsWith("node:")
                || moduleName.startsWith("js:")
                || moduleName.startsWith("http://")
                || moduleName.startsWith("https://");
    }

    private boolean emitLargeStaticJsonLiteralIfPossible(CodeBuilder code, QinCfaProgram.Expression expression) {
        String json = trySerializeJsonLiteral(expression);
        if (json == null || json.length() < JSON_LITERAL_EMIT_THRESHOLD) {
            return false;
        }
        emitStringConstant(code, json);
        code.invokestatic(JS_SDK_JSON_DESC, "parse", JSON_PARSE_SIGNATURE);
        return true;
    }

    private void emitStringConstant(CodeBuilder code, String value) {
        if (value.length() <= STRING_CONSTANT_CHUNK_SIZE) {
            code.ldc(value);
            return;
        }
        code.new_(STRING_BUILDER_DESC);
        code.dup();
        code.invokespecial(STRING_BUILDER_DESC, "<init>", STRING_BUILDER_INIT_SIGNATURE);
        int cursor = 0;
        while (cursor < value.length()) {
            int next = Math.min(value.length(), cursor + STRING_CONSTANT_CHUNK_SIZE);
            code.ldc(value.substring(cursor, next));
            code.invokevirtual(STRING_BUILDER_DESC, "append", STRING_BUILDER_APPEND_SIGNATURE);
            cursor = next;
        }
        code.invokevirtual(STRING_BUILDER_DESC, "toString", STRING_BUILDER_TO_STRING_SIGNATURE);
    }

    private String trySerializeJsonLiteral(QinCfaProgram.Expression expression) {
        StringBuilder out = new StringBuilder(Math.min(JSON_LITERAL_SERIALIZE_LIMIT, 4096));
        if (!appendJsonLiteral(out, expression, JSON_LITERAL_SERIALIZE_LIMIT)) {
            return null;
        }
        return out.toString();
    }

    private boolean appendJsonLiteral(StringBuilder out, QinCfaProgram.Expression expression, int limit) {
        if (expression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            return appendJsonText(out, String.valueOf(numberLiteral.value()), limit);
        }
        if (expression instanceof QinCfaProgram.StringLiteral stringLiteral) {
            return appendJsonStringLiteral(out, stringLiteral.value(), limit);
        }
        if (expression instanceof QinCfaProgram.BooleanLiteral booleanLiteral) {
            return appendJsonText(out, booleanLiteral.value() ? "true" : "false", limit);
        }
        if (expression instanceof QinCfaProgram.NullLiteral) {
            return appendJsonText(out, "null", limit);
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            if (!appendJsonChar(out, '[', limit)) {
                return false;
            }
            for (int i = 0; i < arrayLiteral.elements().size(); i++) {
                if (i > 0) {
                    if (!appendJsonChar(out, ',', limit)) {
                        return false;
                    }
                }
                if (!appendJsonLiteral(out, arrayLiteral.elements().get(i), limit)) {
                    return false;
                }
            }
            return appendJsonChar(out, ']', limit);
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            if (!appendJsonChar(out, '{', limit)) {
                return false;
            }
            for (int i = 0; i < objectLiteral.properties().size(); i++) {
                QinCfaProgram.ObjectProperty property = objectLiteral.properties().get(i);
                if (i > 0) {
                    if (!appendJsonChar(out, ',', limit)) {
                        return false;
                    }
                }
                String key = property.key();
                if (key == null) {
                    return false;
                }
                if (!appendJsonStringLiteral(out, key, limit) || !appendJsonChar(out, ':', limit)) {
                    return false;
                }
                if (!appendJsonLiteral(out, property.value(), limit)) {
                    return false;
                }
            }
            return appendJsonChar(out, '}', limit);
        }
        return false;
    }

    private boolean appendJsonChar(StringBuilder out, char ch, int limit) {
        if (out.length() + 1 > limit) {
            return false;
        }
        out.append(ch);
        return true;
    }

    private boolean appendJsonText(StringBuilder out, String text, int limit) {
        if (text == null) {
            return true;
        }
        if (out.length() + text.length() > limit) {
            return false;
        }
        out.append(text);
        return true;
    }

    private boolean appendJsonStringLiteral(StringBuilder out, String text, int limit) {
        if (!appendJsonChar(out, '"', limit)) {
            return false;
        }
        if (text != null && !text.isEmpty()) {
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                String escaped = switch (ch) {
                    case '"' -> "\\\"";
                    case '\\' -> "\\\\";
                    case '\b' -> "\\b";
                    case '\f' -> "\\f";
                    case '\n' -> "\\n";
                    case '\r' -> "\\r";
                    case '\t' -> "\\t";
                    default -> {
                        if (ch < 0x20) {
                            String hex = Integer.toHexString(ch);
                            yield "\\u" + "0".repeat(4 - hex.length()) + hex;
                        }
                        yield String.valueOf(ch);
                    }
                };
                if (!appendJsonText(out, escaped, limit)) {
                    return false;
                }
            }
        }
        return appendJsonChar(out, '"', limit);
    }

    private String escapeJsonString(String text) {
        if (text == null || text.isEmpty()) {
            return text == null ? "" : text;
        }
        StringBuilder escaped = new StringBuilder(text.length() + 16);
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '"' -> escaped.append("\\\"");
                case '\\' -> escaped.append("\\\\");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                default -> {
                    if (ch < 0x20) {
                        escaped.append("\\u");
                        String hex = Integer.toHexString(ch);
                        for (int pad = hex.length(); pad < 4; pad++) {
                            escaped.append('0');
                        }
                        escaped.append(hex);
                    } else {
                        escaped.append(ch);
                    }
                }
            }
        }
        return escaped.toString();
    }

    private void emitObjectLiteralAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.ObjectLiteral objectLiteral) {
        code.new_(LINKED_HASH_MAP_DESC);
        code.dup();
        code.invokespecial(LINKED_HASH_MAP_DESC, "<init>", VOID_INIT);

        for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
            code.dup();
            code.ldc(property.key());
            try {
                emitExpressionAsObject(code, generatedClassDesc, bindings, property.value());
            } catch (IllegalArgumentException error) {
                throw new IllegalArgumentException(
                        error.getMessage()
                                + " while emitting object property `" + property.key() + "`"
                                + "; property value=" + property.value()
                                + "; visible bindings=" + bindings.keySet(),
                        error);
            }
            emitRuntimeValueUnwrap(code);
            code.invokevirtual(LINKED_HASH_MAP_DESC, "put", MAP_PUT_SIGNATURE);
            code.pop();
        }
    }

    private void emitArrayLiteralAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.ArrayLiteral arrayLiteral) {
        code.new_(ARRAY_LIST_DESC);
        code.dup();
        code.invokespecial(ARRAY_LIST_DESC, "<init>", VOID_INIT);
        for (QinCfaProgram.Expression element : arrayLiteral.elements()) {
            code.dup();
            emitExpressionAsObject(code, generatedClassDesc, bindings, element);
            emitRuntimeValueUnwrap(code);
            code.invokevirtual(ARRAY_LIST_DESC, "add", LIST_ADD_SIGNATURE);
            code.pop();
        }
    }

    private void emitRuntimeValueUnwrap(CodeBuilder code) {
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_value__", VALUE_SIGNATURE);
    }

    private void emitMemberAccessAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.MemberAccessExpression memberAccessExpression) {
        DeclarationBinding binding = bindings.get(memberAccessExpression.objectName());
        if (binding != null) {
            code.getstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
        } else if (!emitKnownGlobalIdentifier(code, memberAccessExpression.objectName())) {
            throw new IllegalArgumentException("QJS2008 unknown identifier in member access: "
                    + memberAccessExpression.objectName());
        }
        code.ldc(memberAccessExpression.propertyName());
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_member_get__", MEMBER_GET_SIGNATURE);
    }

    private void emitBuiltinCallAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (emitLocalAssignmentBuiltinAsObject(code, generatedClassDesc, bindings, builtinCallExpression)) {
            return;
        }
        if (emitUnaryTypeofBuiltinAsObject(code, generatedClassDesc, bindings, builtinCallExpression)) {
            return;
        }
        QinBuiltinRegistry.BuiltinMethod method = QinBuiltinRegistry
                .resolve(
                        builtinCallExpression.receiverName(),
                        builtinCallExpression.methodName(),
                        builtinCallExpression.arguments().size())
                .orElseThrow(() -> new IllegalArgumentException(
                        "QJS1001 unknown builtin call: "
                                + builtinCallExpression.receiverName() + "."
                                + builtinCallExpression.methodName() + "/"
                                + builtinCallExpression.arguments().size()));

        int argumentIndex = 0;
        for (int i = 0; i < method.argumentKinds().size(); i++) {
            QinBuiltinRegistry.BuiltinArgKind argKind = method.argumentKinds().get(i);
            if (argKind == QinBuiltinRegistry.BuiltinArgKind.ARRAY_REST) {
                emitRestArgumentsArray(code, generatedClassDesc, bindings, builtinCallExpression.arguments(), argumentIndex);
                argumentIndex = builtinCallExpression.arguments().size();
                continue;
            }
            QinCfaProgram.Expression argument = builtinCallExpression.arguments().get(argumentIndex);
            if (argKind == QinBuiltinRegistry.BuiltinArgKind.STRING) {
                if (argument instanceof QinCfaProgram.StringLiteral stringLiteral) {
                    code.ldc(stringLiteral.value());
                    argumentIndex++;
                    continue;
                }
                throw new IllegalArgumentException("QJS1003 builtin argument type mismatch at index "
                        + argumentIndex + " for " + builtinCallExpression.receiverName()
                        + "." + builtinCallExpression.methodName() + ": expected string");
            }
            emitExpressionAsObject(code, generatedClassDesc, bindings, argument);
            argumentIndex++;
        }

        code.invokestatic(
                ClassDesc.of(method.ownerBinaryName()),
                method.jvmMethodName(),
                method.descriptor());
    }

    private boolean emitUnaryTypeofBuiltinAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (!"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_unary__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 2
                || !(builtinCallExpression.arguments().get(0) instanceof QinCfaProgram.StringLiteral operator)
                || !"typeof".equals(operator.value())) {
            return false;
        }

        code.ldc("typeof");
        QinCfaProgram.Expression argument = builtinCallExpression.arguments().get(1);
        if (argument instanceof QinCfaProgram.IdentifierReference identifierReference) {
            DeclarationBinding binding = bindings.get(identifierReference.name());
            if (binding != null) {
                code.getstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
            } else if (!emitKnownGlobalIdentifier(code, identifierReference.name())) {
                // JavaScript's `typeof missingIdentifier` evaluates to
                // "undefined" instead of throwing a ReferenceError.
                code.aconst_null();
            }
        } else {
            emitExpressionAsObject(code, generatedClassDesc, bindings, argument);
        }
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_unary__", MethodTypeDesc.ofDescriptor(
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        return true;
    }

    private void emitRestArgumentsArray(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            List<QinCfaProgram.Expression> arguments,
            int startIndex) {
        int restCount = arguments.size() - startIndex;
        code.loadConstant(restCount);
        code.anewarray(OBJECT_DESC);
        for (int i = 0; i < restCount; i++) {
            code.dup();
            code.loadConstant(i);
            emitExpressionAsObject(code, generatedClassDesc, bindings, arguments.get(startIndex + i));
            code.aastore();
        }
    }

    private boolean emitLocalAssignmentBuiltinAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (!"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_assign__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 2) {
            return false;
        }
        QinCfaProgram.Expression bindingNameExpression = builtinCallExpression.arguments().get(0);
        if (!(bindingNameExpression instanceof QinCfaProgram.StringLiteral bindingNameLiteral)) {
            throw new IllegalArgumentException("QJS1003 __qin_assign__ expects string binding name");
        }
        DeclarationBinding binding = bindings.get(bindingNameLiteral.value());
        if (binding == null) {
            throw new IllegalArgumentException("QJS2008 unknown assignment target: " + bindingNameLiteral.value());
        }
        emitExpressionAsObject(code, generatedClassDesc, bindings, builtinCallExpression.arguments().get(1));
        code.dup();
        code.putstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
        return true;
    }

    private void emitExpressionForParameter(CodeBuilder code, QinCfaProgram.Expression expression, Class<?> parameterType) {
        if (expression instanceof QinCfaProgram.StringLiteral stringLiteral) {
            if (parameterType.isPrimitive()) {
                throw new IllegalArgumentException("String literal cannot target primitive parameter: "
                        + parameterType.getName());
            }
            code.ldc(stringLiteral.value());
            return;
        }

        if (expression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            double numberValue = numberLiteral.value();
            if (parameterType == int.class) {
                code.loadConstant((int) numberValue);
                return;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(Integer.class)) {
                code.loadConstant((int) numberValue);
                code.invokestatic(INTEGER_DESC, "valueOf", INTEGER_VALUE_OF_SIGNATURE);
                return;
            }
            if (parameterType == double.class) {
                code.loadConstant(numberValue);
                return;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(Double.class)) {
                code.loadConstant(numberValue);
                code.invokestatic(DOUBLE_DESC, "valueOf", DOUBLE_VALUE_OF_SIGNATURE);
                return;
            }
            throw new IllegalArgumentException("Unsupported numeric parameter type: " + parameterType.getName());
        }

        throw new IllegalArgumentException("Unsupported call argument expression: "
                + expression.getClass().getSimpleName());
    }

    private ResolvedConstructor resolveConstructor(String ownerBinaryName, List<QinCfaProgram.Expression> arguments) {
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Constructor<?> best = null;
            int bestScore = Integer.MAX_VALUE;
            for (Constructor<?> constructor : ownerClass.getConstructors()) {
                int score = compatibilityScore(arguments, constructor.getParameterTypes());
                if (score < 0) {
                    continue;
                }
                if (score < bestScore) {
                    best = constructor;
                    bestScore = score;
                } else if (score == bestScore) {
                    throw new IllegalArgumentException("Ambiguous constructor for " + ownerBinaryName);
                }
            }
            if (best == null) {
                throw new IllegalArgumentException("No compatible constructor for " + ownerBinaryName);
            }
            return new ResolvedConstructor(
                    best.getParameterTypes(),
                    MethodTypeDesc.ofDescriptor(MethodType.methodType(void.class, best.getParameterTypes())
                            .toMethodDescriptorString()));
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot resolve Java constructor: " + ownerBinaryName, e);
        }
    }

    private ResolvedMethod resolveStaticMethod(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        ResolvedMethod resolvedMethod = resolveMethod(ownerBinaryName, methodName, arguments);
        if (!Modifier.isStatic(resolvedMethod.method().getModifiers())) {
            throw new IllegalArgumentException("Method is not static: " + ownerBinaryName + "." + methodName);
        }
        return resolvedMethod;
    }

    private ResolvedMethod resolveInstanceMethod(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        ResolvedMethod resolvedMethod = resolveMethod(ownerBinaryName, methodName, arguments);
        if (Modifier.isStatic(resolvedMethod.method().getModifiers())) {
            throw new IllegalArgumentException("Method is static, expected instance method: "
                    + ownerBinaryName + "." + methodName);
        }
        return resolvedMethod;
    }

    private ResolvedMethod resolveMethod(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Method best = null;
            int bestScore = Integer.MAX_VALUE;
            for (Method method : ownerClass.getMethods()) {
                if (!method.getName().equals(methodName)) {
                    continue;
                }
                int score = compatibilityScore(arguments, method.getParameterTypes());
                if (score < 0) {
                    continue;
                }
                if (score < bestScore) {
                    best = method;
                    bestScore = score;
                } else if (score == bestScore) {
                    throw new IllegalArgumentException("Ambiguous method: " + ownerBinaryName + "." + methodName);
                }
            }
            if (best == null) {
                throw new IllegalArgumentException("No compatible method: " + ownerBinaryName + "." + methodName);
            }
            return new ResolvedMethod(
                    best,
                    best.getParameterTypes(),
                    best.getReturnType(),
                    MethodTypeDesc.ofDescriptor(MethodType.methodType(best.getReturnType(), best.getParameterTypes())
                            .toMethodDescriptorString()));
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot resolve Java method: "
                    + ownerBinaryName + "." + methodName, e);
        }
    }

    private int compatibilityScore(List<QinCfaProgram.Expression> arguments, Class<?>[] parameterTypes) {
        if (arguments.size() != parameterTypes.length) {
            return -1;
        }
        int total = 0;
        for (int i = 0; i < arguments.size(); i++) {
            int parameterScore = compatibilityScore(arguments.get(i), parameterTypes[i]);
            if (parameterScore < 0) {
                return -1;
            }
            total += parameterScore;
        }
        return total;
    }

    private int compatibilityScore(QinCfaProgram.Expression argument, Class<?> parameterType) {
        if (argument instanceof QinCfaProgram.StringLiteral) {
            if (parameterType == String.class) {
                return 0;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(String.class)) {
                return 1;
            }
            return -1;
        }
        if (argument instanceof QinCfaProgram.NumberLiteral) {
            if (parameterType == int.class) {
                return 0;
            }
            if (parameterType == Integer.class) {
                return 1;
            }
            if (parameterType == double.class) {
                return 0;
            }
            if (parameterType == Double.class) {
                return 1;
            }
            if (!parameterType.isPrimitive() && Number.class.isAssignableFrom(parameterType)) {
                return 2;
            }
            return -1;
        }
        return -1;
    }

    private void invokeInstanceMethod(CodeBuilder code, ResolvedMethod resolvedMethod) {
        if (resolvedMethod.method().getDeclaringClass().isInterface()) {
            code.invokeinterface(
                    ClassDesc.of(resolvedMethod.method().getDeclaringClass().getName()),
                    resolvedMethod.method().getName(),
                    resolvedMethod.descriptor());
            return;
        }
        code.invokevirtual(
                ClassDesc.of(resolvedMethod.method().getDeclaringClass().getName()),
                resolvedMethod.method().getName(),
                resolvedMethod.descriptor());
    }

    private void emitJavaReceiverCast(CodeBuilder code, ResolvedMethod resolvedMethod) {
        code.checkcast(ClassDesc.of(resolvedMethod.method().getDeclaringClass().getName()));
    }

    private void discardReturnValue(CodeBuilder code, Class<?> returnType) {
        if (returnType == void.class) {
            return;
        }
        emitBoxIfNeeded(code, returnType);
        code.pop();
    }

    private void emitBoxIfNeeded(CodeBuilder code, Class<?> returnType) {
        if (!returnType.isPrimitive()) {
            return;
        }
        if (returnType == int.class) {
            code.invokestatic(ClassDesc.of("java.lang.Integer"), "valueOf",
                    MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;"));
            return;
        }
        if (returnType == long.class) {
            code.invokestatic(ClassDesc.of("java.lang.Long"), "valueOf",
                    MethodTypeDesc.ofDescriptor("(J)Ljava/lang/Long;"));
            return;
        }
        if (returnType == double.class) {
            code.invokestatic(ClassDesc.of("java.lang.Double"), "valueOf",
                    MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;"));
            return;
        }
        if (returnType == float.class) {
            code.invokestatic(ClassDesc.of("java.lang.Float"), "valueOf",
                    MethodTypeDesc.ofDescriptor("(F)Ljava/lang/Float;"));
            return;
        }
        if (returnType == boolean.class) {
            code.invokestatic(ClassDesc.of("java.lang.Boolean"), "valueOf",
                    MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/Boolean;"));
            return;
        }
        if (returnType == byte.class) {
            code.invokestatic(ClassDesc.of("java.lang.Byte"), "valueOf",
                    MethodTypeDesc.ofDescriptor("(B)Ljava/lang/Byte;"));
            return;
        }
        if (returnType == short.class) {
            code.invokestatic(ClassDesc.of("java.lang.Short"), "valueOf",
                    MethodTypeDesc.ofDescriptor("(S)Ljava/lang/Short;"));
            return;
        }
        if (returnType == char.class) {
            code.invokestatic(ClassDesc.of("java.lang.Character"), "valueOf",
                    MethodTypeDesc.ofDescriptor("(C)Ljava/lang/Character;"));
            return;
        }
        if (returnType == void.class) {
            code.aconst_null();
            return;
        }
        throw new IllegalArgumentException("Unsupported primitive return type: " + returnType.getName());
    }

    private record ChunkMethodSpec(
            String methodName,
            int fromInclusive,
            int toExclusive,
            Map<String, DeclarationBinding> bindingsBeforeChunk) {
    }

    private record ChunkSizing(
            int declarationChunkSize,
            int runtimeChunkSize) {
    }

    private record DeclarationStep(
            String name,
            QinCfaProgram.Expression initializer) {
    }

    private record BindingPlan(
            List<DeclarationStep> declarationSteps,
            List<DeclarationBinding> declarationBindingsByStep,
            List<String> fieldNamesByIndex,
            int lastDeclarationIndex) {
    }

    private record DeclarationBinding(
            int index,
            String fieldName,
            QinCfaProgram.Expression initializer) {
    }

    private record ResolvedConstructor(Class<?>[] parameterTypes, MethodTypeDesc descriptor) {
    }

    private record ResolvedMethod(
            Method method,
            Class<?>[] parameterTypes,
            Class<?> returnType,
            MethodTypeDesc descriptor) {
    }
}

