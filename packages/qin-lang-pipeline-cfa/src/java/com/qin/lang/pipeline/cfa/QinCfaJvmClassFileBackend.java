package com.qin.lang.pipeline.cfa;

import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinJavaSdkAliasSupport;
import com.qin.lang.pipeline.cfa.ir.QinCfaProgram;
import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;
import java.lang.classfile.TypeKind;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
    private static final ClassDesc PRINT_STREAM_DESC = ClassDesc.of("java.io.PrintStream");
    private static final ClassDesc SYSTEM_DESC = ClassDesc.of("java.lang.System");
    private static final ClassDesc JS_SDK_CONSOLE_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmConsole");
    private static final ClassDesc JS_SDK_GLOBAL_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmGlobal");
    private static final ClassDesc JS_SDK_JSON_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmJson");
    private static final ClassDesc STRING_DESC = ClassDesc.of("java.lang.String");
    private static final ClassDesc STRING_ARRAY_DESC = ClassDesc.ofDescriptor("[Ljava/lang/String;");
    private static final ClassDesc CLASS_DESC = ClassDesc.of("java.lang.Class");
    private static final ClassDesc INTEGER_DESC = ClassDesc.of("java.lang.Integer");
    private static final ClassDesc DOUBLE_DESC = ClassDesc.of("java.lang.Double");
    private static final ClassDesc BOOLEAN_DESC = ClassDesc.of("java.lang.Boolean");
    private static final ClassDesc STRING_BUILDER_DESC = ClassDesc.of("java.lang.StringBuilder");

    private static final MethodTypeDesc VOID_INIT = MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc RUN_SIGNATURE = MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;");
    private static final MethodTypeDesc RUN_CHUNK_SIGNATURE = MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc MAIN_SIGNATURE = MethodTypeDesc.ofDescriptor("([Ljava/lang/String;)V");
    private static final MethodTypeDesc PRINTLN_STRING_SIGNATURE = MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)V");
    private static final MethodTypeDesc CONSOLE_LOG_SIGNATURE = MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)V");
    private static final MethodTypeDesc INTEGER_VALUE_OF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;");
    private static final MethodTypeDesc DOUBLE_VALUE_OF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;");
    private static final MethodTypeDesc BOOLEAN_VALUE_OF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/Boolean;");
    private static final MethodTypeDesc FUNCTION_CONSTANT_RETURN_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc FUNCTIONAL_INTERFACE_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;");
    private static final MethodTypeDesc JAVA_ARRAY_INT_FUNCTION_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/util/function/IntFunction;");
    private static final MethodTypeDesc EXPORT_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc GLOBAL_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MODULE_REF_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc VALUE_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MEMBER_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc BIND_GLOBAL_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc BIND_MODULE_REF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc REF_DESCRIPTOR_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MARK_MODULE_REF_INITIALIZED_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc REGISTER_JS_IMPORT_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc JAVA_ARRAYS_STREAM_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/util/stream/Stream;");
    private static final MethodTypeDesc TRUTHY_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z");
    private static final MethodTypeDesc CLASS_FOR_NAME_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)Ljava/lang/Class;");
    private static final MethodTypeDesc MAP_PUT_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MAP_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc LIST_ADD_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z");
    private static final MethodTypeDesc JSON_PARSE_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)Ljava/lang/Object;");
    private static final MethodTypeDesc JSON_PARSE_CHUNKS_SIGNATURE =
            MethodTypeDesc.ofDescriptor("([Ljava/lang/String;)Ljava/lang/Object;");
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
    private static final String REF_DESCRIPTOR_NAME_KEY = "__qin_ref_name";
    private static final String REF_DESCRIPTOR_OWNER_KEY = "__qin_ref_owner";
    private static final String REF_DESCRIPTOR_FIELD_KEY = "__qin_ref_field";
    private Set<String> runtimeImportedGlobalNames = Set.of();
    private Map<String, DeclarationBinding> allDeclarationBindings = Map.of();
    private Map<String, QinIrClassDeclaration> staticDeclarationIndex = Map.of();
    private String currentGeneratedClassName = "";

    public byte[] compileProgram(QinCfaProgram program, String className) {
        return compileProgram(program, className, Map.of());
    }

    public byte[] compileProgram(
            QinCfaProgram program,
            String className,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(className, "className cannot be null");

        long startNanos = System.nanoTime();
        logBackendPhase("compile start", startNanos, className);
        validateExecutionPlan(program);
        BindingPlan bindingPlan = buildBindingPlan(program.declarations());
        logLargestDeclarationInitializers(program, startNanos);
        runtimeImportedGlobalNames = collectRuntimeImportedGlobalNames(program);
        allDeclarationBindings = allDeclarationBindings(bindingPlan);
        staticDeclarationIndex = normalizeStaticDeclarationIndex(declarationIndex);
        currentGeneratedClassName = className;
        ClassDesc generatedClassDesc = ClassDesc.of(className);
        IllegalArgumentException lastTooLargeError = null;
        try {
            for (ChunkSizing sizing : CHUNK_SIZING_FALLBACKS) {
                try {
                    logBackendPhase("build attempt start", startNanos, sizing.toString());
                    byte[] bytes = buildClassBytes(program, bindingPlan, generatedClassDesc, className, sizing);
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
            allDeclarationBindings = Map.of();
            staticDeclarationIndex = Map.of();
            currentGeneratedClassName = "";
        }
    }

    private byte[] buildClassBytes(
            QinCfaProgram program,
            BindingPlan bindingPlan,
            ClassDesc generatedClassDesc,
            String generatedClassName,
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
                    code -> emitRunMethod(code, generatedClassDesc, generatedClassName,
                            chunkMethods, bindingPlan, program));

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
        if (expression instanceof QinCfaProgram.StaticMethodCallExpression staticMethodCallExpression) {
            int size = 1;
            for (QinCfaProgram.Expression argument : staticMethodCallExpression.arguments()) {
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
        if (expression instanceof QinCfaProgram.LetExpression letExpression) {
            int size = 1;
            for (QinCfaProgram.LocalVariableDeclaration declaration : letExpression.localDeclarations()) {
                size += expressionSize(declaration.initializer(), limit - size);
                if (size >= limit) {
                    return size;
                }
            }
            for (QinCfaProgram.Expression leadingExpression : letExpression.leadingExpressions()) {
                size += expressionSize(leadingExpression, limit - size);
                if (size >= limit) {
                    return size;
                }
            }
            return size + expressionSize(letExpression.resultExpression(), limit - size);
        }
        if (expression instanceof QinCfaProgram.SequenceExpression sequenceExpression) {
            int size = 1;
            for (QinCfaProgram.Expression leadingExpression : sequenceExpression.leadingExpressions()) {
                size += expressionSize(leadingExpression, limit - size);
                if (size >= limit) {
                    return size;
                }
            }
            return size + expressionSize(sequenceExpression.resultExpression(), limit - size);
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
            String generatedClassName,
            List<ChunkMethodSpec> chunkMethods,
            BindingPlan bindingPlan,
            QinCfaProgram program) {
        emitModuleRefRegistrations(code, bindingPlan, generatedClassName);
        emitRuntimeJavaImportRegistrations(code, program);
        emitRuntimeJsImportRegistrations(code, program);
        for (ChunkMethodSpec chunkMethod : chunkMethods) {
            code.invokestatic(generatedClassDesc, chunkMethod.methodName(), RUN_CHUNK_SIGNATURE);
        }
        code.getstatic(generatedClassDesc, LAST_VALUE_FIELD_NAME, OBJECT_DESC);
        code.areturn();
    }

    private void emitModuleRefRegistrations(CodeBuilder code, BindingPlan bindingPlan, String generatedClassName) {
        String moduleExportAliasPrefix = moduleExportAliasPrefix(generatedClassName);
        for (int i = 0; i < bindingPlan.declarationSteps().size(); i++) {
            DeclarationStep declarationStep = bindingPlan.declarationSteps().get(i);
            DeclarationBinding declarationBinding =
                    bindingPlan.declarationBindingsByStep().get(i);
            emitModuleRefRegistration(code, declarationStep.name(), declarationBinding.fieldName());
            if (moduleExportAliasPrefix != null && !declarationStep.name().startsWith(moduleExportAliasPrefix)) {
                emitModuleRefRegistration(
                        code,
                        moduleExportAliasPrefix + declarationStep.name(),
                        declarationBinding.fieldName());
            }
        }
    }

    private void emitModuleRefRegistration(CodeBuilder code, String declarationName, String fieldName) {
        code.ldc(declarationName);
        code.ldc(fieldName);
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_bind_module_ref__", BIND_MODULE_REF_SIGNATURE);
        code.pop();
    }

    private String moduleExportAliasPrefix(String generatedClassName) {
        int index = generatedClassName == null ? -1 : generatedClassName.lastIndexOf("$QinModule");
        if (index < 0) {
            return null;
        }
        int start = index + "$QinModule".length();
        int end = start;
        while (end < generatedClassName.length() && Character.isDigit(generatedClassName.charAt(end))) {
            end++;
        }
        if (end == start) {
            return null;
        }
        return "__qesm_m" + generatedClassName.substring(start, end) + "_e_";
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

    private void emitRuntimeJavaImportRegistrations(CodeBuilder code, QinCfaProgram program) {
        for (QinCfaProgram.JavaImport javaImport : program.javaImports()) {
            String localName = javaImport.localName();
            String ownerBinaryName = javaImport.ownerBinaryName();
            if (localName == null || localName.isBlank()
                    || ownerBinaryName == null || ownerBinaryName.isBlank()) {
                continue;
            }
            code.ldc(localName);
            code.ldc(ownerBinaryName);
            code.invokestatic(CLASS_DESC, "forName", CLASS_FOR_NAME_SIGNATURE);
            code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_bind_global__", BIND_GLOBAL_SIGNATURE);
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
            emitRuntimeStepDebugLog(code, i, step, program);
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

    private void emitRuntimeStepDebugLog(
            CodeBuilder code,
            int stepIndex,
            QinCfaProgram.TopLevelExecutionStep step,
            QinCfaProgram program) {
        if (!DEBUG_BACKEND) {
            return;
        }
        code.getstatic(SYSTEM_DESC, "out", PRINT_STREAM_DESC);
        code.ldc(runtimeStepLabel(stepIndex, step, program));
        code.invokevirtual(PRINT_STREAM_DESC, "println", PRINTLN_STRING_SIGNATURE);
    }

    private String runtimeStepLabel(
            int stepIndex,
            QinCfaProgram.TopLevelExecutionStep step,
            QinCfaProgram program) {
        String detail = switch (step.kind()) {
            case DECLARATION -> program.declarations().get(step.index()).name();
            case EXPRESSION_STATEMENT -> "expression#" + step.index();
            case CONSOLE_VALUE -> "consoleValue#" + step.index();
            case CONSOLE_OBJECT -> "consoleObject#" + step.index();
            case JAVA_STATIC_CONSOLE -> "javaStaticConsole#" + step.index();
            case JAVA_INSTANCE_CALL -> "javaInstanceCall#" + step.index();
            case JAVA_INSTANCE_CONSOLE -> "javaInstanceConsole#" + step.index();
        };
        return "[QinCfaJvmClassFileBackend] runtime step "
                + stepIndex
                + " "
                + step.kind()
                + "#"
                + step.index()
                + " "
                + detail;
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

    private Map<String, DeclarationBinding> allDeclarationBindings(BindingPlan bindingPlan) {
        Map<String, DeclarationBinding> bindings = new LinkedHashMap<>();
        for (int i = 0; i < bindingPlan.declarationSteps().size(); i++) {
            bindings.put(
                    bindingPlan.declarationSteps().get(i).name(),
                    bindingPlan.declarationBindingsByStep().get(i));
        }
        return bindings;
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
            emitJavaNewExpression(code, javaNewExpression);
            code.putstatic(generatedClassDesc, fieldName, OBJECT_DESC);
            emitMarkModuleRefInitialized(code, declarationName, fieldName);
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

    private void emitJavaNewExpression(CodeBuilder code, QinCfaProgram.JavaNewExpression javaNewExpression) {
        emitJavaNewExpression(
                code,
                ClassDesc.of(currentGeneratedClassName),
                allDeclarationBindings,
                Map.of(),
                1,
                javaNewExpression);
    }

    private void emitJavaNewExpression(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.JavaNewExpression javaNewExpression) {
        String ownerBinaryName = canonicalJavaBinaryName(javaNewExpression.ownerBinaryName());
        ResolvedConstructor resolvedConstructor = resolveConstructor(
                ownerBinaryName,
                javaNewExpression.arguments());

        ClassDesc ownerDesc = ClassDesc.of(ownerBinaryName);
        code.new_(ownerDesc);
        code.dup();
        emitArgumentsForParameters(
                code,
                javaNewExpression.arguments(),
                resolvedConstructor.parameterTypes(),
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                resolvedConstructor.varArgs());
        code.invokespecial(ownerDesc, "<init>", resolvedConstructor.descriptor());
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
        String ownerBinaryName = canonicalJavaBinaryName(javaStaticCall.ownerBinaryName());
        ResolvedMethod resolvedMethod = resolveStaticMethod(
                ownerBinaryName,
                javaStaticCall.methodName(),
                javaStaticCall.arguments());

        emitArgumentsForParameters(code, javaStaticCall.arguments(), resolvedMethod.parameterTypes());
        emitStaticMethodInvoke(code, ownerBinaryName, javaStaticCall.methodName(), resolvedMethod);

        emitBoxIfNeeded(code, resolvedMethod.returnType());
        code.invokestatic(JS_SDK_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private void emitJavaInstanceMethodCallExpression(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.JavaInstanceMethodCallExpression javaInstanceMethodCallExpression) {
        ResolvedMethod resolvedMethod = resolveInstanceMethod(
                javaInstanceMethodCallExpression.ownerBinaryName(),
                javaInstanceMethodCallExpression.methodName(),
                javaInstanceMethodCallExpression.arguments());

        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                javaInstanceMethodCallExpression.receiver());
        emitJavaReceiverCast(code, resolvedMethod);
        emitArgumentsForParameters(
                code,
                javaInstanceMethodCallExpression.arguments(),
                resolvedMethod.parameterTypes(),
                resolvedMethod.parameterDescs(),
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                resolvedMethod.varArgs());
        invokeInstanceMethod(code, resolvedMethod);
        emitBoxIfNeeded(code, resolvedMethod.returnType());
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
        emitArgumentsForParameters(
                code,
                arguments,
                parameterTypes,
                ClassDesc.of(currentGeneratedClassName),
                allDeclarationBindings,
                Map.of(),
                1,
                false);
    }

    private void emitArgumentsForParameters(
            CodeBuilder code,
            List<QinCfaProgram.Expression> arguments,
            Class<?>[] parameterTypes,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot) {
        emitArgumentsForParameters(
                code,
                arguments,
                parameterTypes,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                false);
    }

    private void emitArgumentsForParameters(
            CodeBuilder code,
            List<QinCfaProgram.Expression> arguments,
            Class<?>[] parameterTypes,
            ClassDesc[] targetParameterDescs,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot) {
        emitArgumentsForParameters(
                code,
                arguments,
                parameterTypes,
                targetParameterDescs,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                false);
    }

    private void emitArgumentsForParameters(
            CodeBuilder code,
            List<QinCfaProgram.Expression> arguments,
            Class<?>[] parameterTypes,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            boolean varArgs) {
        emitArgumentsForParameters(
                code,
                arguments,
                parameterTypes,
                null,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                varArgs);
    }

    private void emitArgumentsForParameters(
            CodeBuilder code,
            List<QinCfaProgram.Expression> arguments,
            Class<?>[] parameterTypes,
            ClassDesc[] targetParameterDescs,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            boolean varArgs) {
        if (varArgs) {
            emitArgumentsForVarArgs(
                    code,
                    arguments,
                    parameterTypes,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot);
            return;
        }
        if (arguments.size() == 1
                && arguments.get(0) instanceof QinCfaProgram.SpreadArgumentExpression spreadArgumentExpression) {
            emitFixedAritySpreadArguments(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    spreadArgumentExpression,
                    parameterTypes);
            return;
        }
        if (arguments.size() != parameterTypes.length) {
            throw new IllegalArgumentException("Argument count mismatch");
        }
        for (int i = 0; i < arguments.size(); i++) {
            emitExpressionForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    arguments.get(i),
                    parameterTypes[i]);
            emitTargetParameterCastIfNeeded(code, parameterTypes[i], targetParameterDescs, i);
        }
    }

    private void emitTargetParameterCastIfNeeded(
            CodeBuilder code,
            Class<?> emittedParameterType,
            ClassDesc[] targetParameterDescs,
            int parameterIndex) {
        if (targetParameterDescs == null
                || parameterIndex < 0
                || parameterIndex >= targetParameterDescs.length
                || emittedParameterType != Object.class) {
            return;
        }
        ClassDesc targetDesc = targetParameterDescs[parameterIndex];
        if (!isReferenceCastTarget(targetDesc)) {
            return;
        }
        code.checkcast(targetDesc);
    }

    private boolean isReferenceCastTarget(ClassDesc targetDesc) {
        if (targetDesc == null || targetDesc.equals(OBJECT_DESC)) {
            return false;
        }
        String descriptor = targetDesc.descriptorString();
        return descriptor.startsWith("L") || descriptor.startsWith("[");
    }

    private void emitArgumentsForVarArgs(
            CodeBuilder code,
            List<QinCfaProgram.Expression> arguments,
            Class<?>[] parameterTypes,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot) {
        if (parameterTypes.length == 0) {
            throw new IllegalArgumentException("Varargs method has no array parameter");
        }
        int fixedCount = parameterTypes.length - 1;
        if (arguments.size() < fixedCount) {
            throw new IllegalArgumentException("Argument count mismatch");
        }
        for (int i = 0; i < fixedCount; i++) {
            emitExpressionForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    arguments.get(i),
                    parameterTypes[i]);
        }
        Class<?> arrayType = parameterTypes[fixedCount];
        if (!arrayType.isArray()) {
            throw new IllegalArgumentException("Varargs parameter is not an array: " + arrayType.getName());
        }
        if (arguments.size() == parameterTypes.length
                && arguments.get(fixedCount) instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            emitArrayLiteralForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    arrayLiteral,
                    arrayType);
            return;
        }
        if (arguments.size() == parameterTypes.length
                && arguments.get(fixedCount) instanceof QinCfaProgram.SpreadArgumentExpression spreadArgumentExpression) {
            emitSpreadExpressionAsObjectArray(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    spreadArgumentExpression.expression());
            if (arrayType == Object[].class) {
                return;
            }
            code.checkcast(ClassDesc.of(arrayType.getName()));
            return;
        }
        Class<?> componentType = arrayType.getComponentType();
        int tailCount = arguments.size() - fixedCount;
        code.loadConstant(tailCount);
        code.anewarray(ClassDesc.of(componentType.getName()));
        for (int i = 0; i < tailCount; i++) {
            code.dup();
            code.loadConstant(i);
            emitExpressionForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    arguments.get(fixedCount + i),
                    componentType);
            code.aastore();
        }
    }

    private void emitExpressionAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            QinCfaProgram.Expression expression) {
        emitExpressionAsObject(code, generatedClassDesc, bindings, Map.of(), 1, expression);
    }

    private void emitExpressionAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
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
            emitObjectLiteralAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, objectLiteral);
            return;
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            if (emitLargeStaticJsonLiteralIfPossible(code, expression)) {
                return;
            }
            emitArrayLiteralAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, arrayLiteral);
            return;
        }
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            emitExpressionAsObject(code, generatedClassDesc, bindings, Map.of(), 1, functionLiteral.returnExpression());
            code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_constant_return_function__", FUNCTION_CONSTANT_RETURN_SIGNATURE);
            return;
        }
        if (expression instanceof QinCfaProgram.LetExpression letExpression) {
            emitLetExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, letExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.SequenceExpression sequenceExpression) {
            for (QinCfaProgram.Expression leadingExpression : sequenceExpression.leadingExpressions()) {
                emitExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, leadingExpression);
                code.pop();
            }
            emitExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, sequenceExpression.resultExpression());
            return;
        }
        if (expression instanceof QinCfaProgram.MemberAccessExpression memberAccessExpression) {
            emitMemberAccessAsObject(code, generatedClassDesc, bindings, memberAccessExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            LocalBinding localBinding = localBindings.get(identifierReference.name());
            if (localBinding != null) {
                code.aload(localBinding.slot());
                return;
            }
            DeclarationBinding binding = bindings.get(identifierReference.name());
            if (binding == null) {
                if (isQesmModuleExportReference(identifierReference.name())) {
                    code.ldc(identifierReference.name());
                    code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_module_ref_get__", MODULE_REF_GET_SIGNATURE);
                    return;
                }
                if (emitKnownGlobalIdentifier(code, identifierReference.name())) {
                    return;
                }
                throw new IllegalArgumentException("QJS2008 unknown identifier: " + identifierReference.name());
            }
            code.getstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
            return;
        }
        if (expression instanceof QinCfaProgram.JavaClassLiteralExpression classLiteralExpression) {
            String binaryName = classLiteralExpression.binaryName() == null
                    ? classLiteralExpression.typeName()
                    : classLiteralExpression.binaryName();
            code.ldc(binaryName);
            code.invokestatic(CLASS_DESC, "forName", CLASS_FOR_NAME_SIGNATURE);
            return;
        }
        if (expression instanceof QinCfaProgram.JavaNewExpression javaNewExpression) {
            emitJavaNewExpression(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, javaNewExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.JavaInstanceMethodCallExpression instanceMethodCallExpression) {
            emitJavaInstanceMethodCallExpression(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    instanceMethodCallExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.StaticMethodCallExpression staticMethodCallExpression) {
            emitStaticMethodCallExpression(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    staticMethodCallExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
            if (emitArrayFromFactoryForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression,
                    Object.class)) {
                return;
            }
            emitBuiltinCallAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, builtinCallExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.SpreadArgumentExpression) {
            throw new IllegalArgumentException("Spread argument can only appear inside a static call argument list");
        }

        throw new IllegalArgumentException("Unsupported object expression type: "
                + expression.getClass().getSimpleName());
    }

    private void emitFixedAritySpreadArguments(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.SpreadArgumentExpression spreadArgumentExpression,
            Class<?>[] parameterTypes) {
        emitSpreadExpressionAsObjectArray(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                spreadArgumentExpression.expression());
        code.astore(nextLocalSlot);
        for (int i = 0; i < parameterTypes.length; i++) {
            code.aload(nextLocalSlot);
            code.loadConstant(i);
            code.aaload();
            coerceObjectStackForParameter(code, parameterTypes[i]);
        }
    }

    private void emitSpreadExpressionAsObjectArray(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.Expression spreadExpression) {
        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                spreadExpression);
        code.invokestatic(
                JS_SDK_GLOBAL_DESC,
                "__qin_to_object_array__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)[Ljava/lang/Object;"));
    }

    private void coerceObjectStackForParameter(CodeBuilder code, Class<?> parameterType) {
        if (parameterType == Object.class) {
            return;
        }
        if (parameterType == int.class) {
            code.checkcast(ClassDesc.of("java.lang.Number"));
            code.invokevirtual(ClassDesc.of("java.lang.Number"), "intValue", MethodTypeDesc.ofDescriptor("()I"));
            return;
        }
        if (parameterType == double.class) {
            code.checkcast(ClassDesc.of("java.lang.Number"));
            code.invokevirtual(ClassDesc.of("java.lang.Number"), "doubleValue", MethodTypeDesc.ofDescriptor("()D"));
            return;
        }
        if (parameterType == boolean.class) {
            code.checkcast(BOOLEAN_DESC);
            code.invokevirtual(BOOLEAN_DESC, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"));
            return;
        }
        if (parameterType.isArray() && parameterType == Object[].class) {
            code.invokestatic(
                    JS_SDK_GLOBAL_DESC,
                    "__qin_to_object_array__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)[Ljava/lang/Object;"));
            return;
        }
        code.checkcast(ClassDesc.of(parameterType.getName()));
    }

    private void emitStaticMethodCallExpression(
            CodeBuilder code,
            QinCfaProgram.StaticMethodCallExpression staticMethodCallExpression) {
        emitStaticMethodCallExpression(
                code,
                ClassDesc.of(currentGeneratedClassName),
                allDeclarationBindings,
                Map.of(),
                1,
                staticMethodCallExpression);
    }

    private void emitStaticMethodCallExpression(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.StaticMethodCallExpression staticMethodCallExpression) {
        String ownerBinaryName = staticMethodCallExpression.ownerBinaryName();
        ResolvedMethod resolvedMethod = resolveStaticMethod(
                ownerBinaryName,
                staticMethodCallExpression.methodName(),
                staticMethodCallExpression.arguments());

        emitArgumentsForParameters(
                code,
                staticMethodCallExpression.arguments(),
                resolvedMethod.parameterTypes(),
                resolvedMethod.parameterDescs(),
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                resolvedMethod.varArgs());
        emitStaticMethodInvoke(code, resolvedMethod.ownerBinaryName(), resolvedMethod.methodName(), resolvedMethod);
        emitBoxIfNeeded(code, resolvedMethod.returnType());
    }

    private void emitStaticMethodInvoke(
            CodeBuilder code,
            String ownerBinaryName,
            String methodName,
            ResolvedMethod resolvedMethod) {
        boolean ownerInterface = resolvedMethod.ownerInterface()
                || isInterfaceBinaryName(ownerBinaryName);
        code.invokestatic(
                ClassDesc.of(ownerBinaryName),
                methodName,
                resolvedMethod.descriptor(),
                ownerInterface);
    }

    private boolean isInterfaceBinaryName(String ownerBinaryName) {
        if (ownerBinaryName == null || ownerBinaryName.isBlank()) {
            return false;
        }
        try {
            return Class.forName(canonicalJavaBinaryName(ownerBinaryName)).isInterface();
        } catch (ClassNotFoundException | LinkageError ignored) {
            return false;
        }
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

    private boolean isQesmModuleExportReference(String name) {
        return name != null && name.startsWith("__qesm_m") && name.contains("_e_");
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
        if (json.length() > STRING_CONSTANT_CHUNK_SIZE) {
            emitStringArrayConstant(code, json);
            code.invokestatic(JS_SDK_JSON_DESC, "parseChunks", JSON_PARSE_CHUNKS_SIGNATURE);
        } else {
            emitStringConstant(code, json);
            code.invokestatic(JS_SDK_JSON_DESC, "parse", JSON_PARSE_SIGNATURE);
        }
        return true;
    }

    private void emitStringArrayConstant(CodeBuilder code, String value) {
        int chunkCount = (value.length() + STRING_CONSTANT_CHUNK_SIZE - 1) / STRING_CONSTANT_CHUNK_SIZE;
        code.ldc(chunkCount);
        code.anewarray(STRING_DESC);
        for (int i = 0; i < chunkCount; i++) {
            int start = i * STRING_CONSTANT_CHUNK_SIZE;
            int end = Math.min(value.length(), start + STRING_CONSTANT_CHUNK_SIZE);
            code.dup();
            code.ldc(i);
            code.ldc(value.substring(start, end));
            code.aastore();
        }
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
            if (appendScopedReferenceDescriptorJsonIfPossible(out, objectLiteral, limit)) {
                return true;
            }
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

    private boolean appendScopedReferenceDescriptorJsonIfPossible(
            StringBuilder out,
            QinCfaProgram.ObjectLiteral objectLiteral,
            int limit) {
        if (objectLiteral.properties().size() != 1) {
            return false;
        }
        QinCfaProgram.ObjectProperty property = objectLiteral.properties().get(0);
        if (!REF_DESCRIPTOR_NAME_KEY.equals(property.key())
                || !(property.value() instanceof QinCfaProgram.StringLiteral stringLiteral)) {
            return false;
        }
        DeclarationBinding binding = allDeclarationBindings.get(stringLiteral.value());
        if (binding == null || currentGeneratedClassName == null || currentGeneratedClassName.isBlank()) {
            return false;
        }
        return appendJsonChar(out, '{', limit)
                && appendJsonStringLiteral(out, REF_DESCRIPTOR_NAME_KEY, limit)
                && appendJsonChar(out, ':', limit)
                && appendJsonStringLiteral(out, stringLiteral.value(), limit)
                && appendJsonChar(out, ',', limit)
                && appendJsonStringLiteral(out, REF_DESCRIPTOR_OWNER_KEY, limit)
                && appendJsonChar(out, ':', limit)
                && appendJsonStringLiteral(out, currentGeneratedClassName, limit)
                && appendJsonChar(out, ',', limit)
                && appendJsonStringLiteral(out, REF_DESCRIPTOR_FIELD_KEY, limit)
                && appendJsonChar(out, ':', limit)
                && appendJsonStringLiteral(out, binding.fieldName(), limit)
                && appendJsonChar(out, '}', limit);
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
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.ObjectLiteral objectLiteral) {
        if (emitScopedReferenceDescriptorIfPossible(code, objectLiteral)) {
            return;
        }
        code.new_(LINKED_HASH_MAP_DESC);
        code.dup();
        code.invokespecial(LINKED_HASH_MAP_DESC, "<init>", VOID_INIT);

        for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
            code.dup();
            code.ldc(property.key());
            try {
                emitExpressionAsObject(
                        code,
                        generatedClassDesc,
                        bindings,
                        localBindings,
                        nextLocalSlot,
                        property.value());
            } catch (IllegalArgumentException error) {
                throw new IllegalArgumentException(
                        error.getMessage()
                                + " while emitting object property `" + property.key() + "`"
                                + "; property value=" + expressionSummary(property.value())
                                + "; spreadPath=" + spreadArgumentPath(property.value())
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
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.ArrayLiteral arrayLiteral) {
        code.new_(ARRAY_LIST_DESC);
        code.dup();
        code.invokespecial(ARRAY_LIST_DESC, "<init>", VOID_INIT);
        for (QinCfaProgram.Expression element : arrayLiteral.elements()) {
            code.dup();
            emitExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, element);
            emitRuntimeValueUnwrap(code);
            code.invokevirtual(ARRAY_LIST_DESC, "add", LIST_ADD_SIGNATURE);
            code.pop();
        }
    }

    private boolean emitScopedReferenceDescriptorIfPossible(
            CodeBuilder code,
            QinCfaProgram.ObjectLiteral objectLiteral) {
        if (objectLiteral.properties().size() != 1) {
            return false;
        }
        QinCfaProgram.ObjectProperty property = objectLiteral.properties().get(0);
        if (!REF_DESCRIPTOR_NAME_KEY.equals(property.key())
                || !(property.value() instanceof QinCfaProgram.StringLiteral stringLiteral)) {
            return false;
        }
        DeclarationBinding binding = allDeclarationBindings.get(stringLiteral.value());
        if (binding == null) {
            return false;
        }
        code.ldc(stringLiteral.value());
        code.ldc(binding.fieldName());
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_ref_descriptor__", REF_DESCRIPTOR_SIGNATURE);
        return true;
    }

    private void emitLetExpressionAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> parentLocalBindings,
            int nextLocalSlot,
            QinCfaProgram.LetExpression letExpression) {
        Map<String, LocalBinding> scopedLocalBindings = new LinkedHashMap<>(parentLocalBindings);
        int slot = nextLocalSlot;
        for (QinCfaProgram.LocalVariableDeclaration declaration : letExpression.localDeclarations()) {
            Class<?> staticType = staticExpressionType(declaration.initializer(), scopedLocalBindings);
            emitExpressionAsObject(code, generatedClassDesc, bindings, scopedLocalBindings, slot, declaration.initializer());
            code.astore(slot);
            scopedLocalBindings.put(declaration.name(), new LocalBinding(slot, staticType));
            slot++;
        }
        for (QinCfaProgram.Expression leadingExpression : letExpression.leadingExpressions()) {
            emitExpressionAsObject(code, generatedClassDesc, bindings, scopedLocalBindings, slot, leadingExpression);
            code.pop();
        }
        emitExpressionAsObject(code, generatedClassDesc, bindings, scopedLocalBindings, slot, letExpression.resultExpression());
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
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        Class<?> staticArrayType = staticArrayProducingBuiltinReturnType(builtinCallExpression, localBindings);
        if (staticArrayType != null
                && (emitArrayFromFactoryForParameter(
                        code,
                        generatedClassDesc,
                        bindings,
                        localBindings,
                        nextLocalSlot,
                        builtinCallExpression,
                        staticArrayType)
                || emitJavaArraysCopyForParameter(
                        code,
                        generatedClassDesc,
                        bindings,
                        localBindings,
                        nextLocalSlot,
                        builtinCallExpression,
                        staticArrayType))) {
            return;
        }
        if (emitStaticCallMethodBuiltinAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression)) {
            return;
        }
        if (emitStaticJavaSdkFacadeBuiltinAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression)) {
            return;
        }
        if (emitLocalAssignmentBuiltinAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression)) {
            return;
        }
        if (emitStaticFieldAssignmentBuiltinAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression)) {
            return;
        }
        if (emitUnaryTypeofBuiltinAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression)) {
            return;
        }
        if (emitConditionalBuiltinAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression)) {
            return;
        }
        QinBuiltinRegistry.BuiltinMethod method = resolveBuiltinMethod(builtinCallExpression)
                .orElseThrow(() -> new IllegalArgumentException(
                        "QJS1001 unknown builtin call: "
                                + builtinCallExpression.receiverName() + "."
                                + builtinCallExpression.methodName() + "/"
                                + builtinCallExpression.arguments().size()));

        int argumentIndex = 0;
        for (int i = 0; i < method.argumentKinds().size(); i++) {
            QinBuiltinRegistry.BuiltinArgKind argKind = method.argumentKinds().get(i);
            if (argKind == QinBuiltinRegistry.BuiltinArgKind.ARRAY_REST) {
                emitRestArgumentsArray(
                        code,
                        generatedClassDesc,
                        bindings,
                        localBindings,
                        nextLocalSlot,
                        builtinCallExpression.arguments(),
                        argumentIndex);
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
            try {
                emitExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, argument);
            } catch (IllegalArgumentException error) {
                throw new IllegalArgumentException(
                        error.getMessage()
                                + " while emitting builtin argument "
                                + builtinCallExpression.receiverName()
                                + "."
                                + builtinCallExpression.methodName()
                                + "/"
                                + builtinCallExpression.arguments().size()
                                + " arg#"
                                + argumentIndex
                                + "="
                                + expressionSummary(argument)
                                + "; spreadPath="
                                + spreadArgumentPath(argument),
                        error);
            }
            argumentIndex++;
        }

        code.invokestatic(
                ClassDesc.of(canonicalJavaBinaryName(method.ownerBinaryName())),
                method.jvmMethodName(),
                method.descriptor());
    }

    private boolean emitStaticCallMethodBuiltinAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        StaticCallMethod staticCallMethod = staticCallMethodOrNull(builtinCallExpression);
        if (staticCallMethod == null) {
            return false;
        }
        ResolvedMethod resolvedMethod = resolveInstanceMethod(
                staticCallMethod.ownerClass().getName(),
                staticCallMethod.methodName(),
                staticCallMethod.arguments());
        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                staticCallMethod.receiver());
        emitJavaReceiverCast(code, resolvedMethod);
        emitArgumentsForParameters(
                code,
                staticCallMethod.arguments(),
                resolvedMethod.parameterTypes(),
                resolvedMethod.parameterDescs(),
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                resolvedMethod.varArgs());
        invokeInstanceMethod(code, resolvedMethod);
        emitBoxIfNeeded(code, resolvedMethod.returnType());
        return true;
    }

    private boolean emitStaticJavaSdkFacadeBuiltinAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (!"__QinJavaUtilArrays".equals(builtinCallExpression.receiverName())
                || !"stream".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 1) {
            return false;
        }
        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression.arguments().get(0));
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_java_arrays_stream__", JAVA_ARRAYS_STREAM_SIGNATURE);
        return true;
    }

    private Optional<QinBuiltinRegistry.BuiltinMethod> resolveBuiltinMethod(
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (containsSpreadArgument(builtinCallExpression.arguments(), 0)) {
            return QinBuiltinRegistry.resolveSpreadArguments(
                    builtinCallExpression.receiverName(),
                    builtinCallExpression.methodName(),
                    builtinCallExpression.arguments().size());
        }
        return QinBuiltinRegistry.resolve(
                builtinCallExpression.receiverName(),
                builtinCallExpression.methodName(),
                builtinCallExpression.arguments().size());
    }

    private boolean emitConditionalBuiltinAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (!"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_conditional__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 3) {
            return false;
        }

        Label alternateLabel = code.newLabel();
        Label doneLabel = code.newLabel();
        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression.arguments().get(0));
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_truthy__", TRUTHY_SIGNATURE);
        code.ifeq(alternateLabel);
        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression.arguments().get(1));
        code.goto_(doneLabel);
        code.labelBinding(alternateLabel);
        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression.arguments().get(2));
        code.labelBinding(doneLabel);
        return true;
    }

    private boolean emitUnaryTypeofBuiltinAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
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
            LocalBinding localBinding = localBindings.get(identifierReference.name());
            if (localBinding != null) {
                code.aload(localBinding.slot());
            } else if (bindings.get(identifierReference.name()) != null) {
                DeclarationBinding binding = bindings.get(identifierReference.name());
                code.getstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
            } else if (!emitKnownGlobalIdentifier(code, identifierReference.name())) {
                // JavaScript's `typeof missingIdentifier` evaluates to
                // "undefined" instead of throwing a ReferenceError.
                code.aconst_null();
            }
        } else {
            emitExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, argument);
        }
        code.invokestatic(JS_SDK_GLOBAL_DESC, "__qin_unary__", MethodTypeDesc.ofDescriptor(
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        return true;
    }

    private void emitRestArgumentsArray(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            List<QinCfaProgram.Expression> arguments,
            int startIndex) {
        int restCount = arguments.size() - startIndex;
        if (containsSpreadArgument(arguments, startIndex)) {
            code.loadConstant(restCount);
            code.anewarray(OBJECT_DESC);
            for (int i = 0; i < restCount; i++) {
                QinCfaProgram.Expression argument = arguments.get(startIndex + i);
                code.dup();
                code.loadConstant(i);
                if (argument instanceof QinCfaProgram.SpreadArgumentExpression spreadArgumentExpression) {
                    emitExpressionAsObject(
                            code,
                            generatedClassDesc,
                            bindings,
                            localBindings,
                            nextLocalSlot,
                            spreadArgumentExpression.expression());
                    code.invokestatic(
                            JS_SDK_GLOBAL_DESC,
                            "__qin_array_spread__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"));
                } else {
                    emitExpressionAsObject(
                            code,
                            generatedClassDesc,
                            bindings,
                            localBindings,
                            nextLocalSlot,
                            argument);
                    code.invokestatic(
                            JS_SDK_GLOBAL_DESC,
                            "__qin_array_item__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"));
                }
                code.aastore();
            }
            code.invokestatic(
                    JS_SDK_GLOBAL_DESC,
                    "__qin_array_literal_array__",
                    MethodTypeDesc.ofDescriptor("([Ljava/lang/Object;)Ljava/lang/Object;"));
            code.invokestatic(
                    JS_SDK_GLOBAL_DESC,
                    "__qin_to_object_array__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)[Ljava/lang/Object;"));
            return;
        }
        code.loadConstant(restCount);
        code.anewarray(OBJECT_DESC);
        for (int i = 0; i < restCount; i++) {
            code.dup();
            code.loadConstant(i);
            emitExpressionAsObject(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    arguments.get(startIndex + i));
            code.aastore();
        }
    }

    private boolean containsSpreadArgument(List<QinCfaProgram.Expression> arguments, int startIndex) {
        for (int i = startIndex; i < arguments.size(); i++) {
            if (arguments.get(i) instanceof QinCfaProgram.SpreadArgumentExpression) {
                return true;
            }
        }
        return false;
    }

    private String expressionSummary(QinCfaProgram.Expression expression) {
        return expressionSummary(expression, 0);
    }

    private String expressionSummary(QinCfaProgram.Expression expression, int depth) {
        if (expression == null) {
            return "<null>";
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
            String summary = "BuiltinCall("
                    + builtinCallExpression.receiverName()
                    + "."
                    + builtinCallExpression.methodName()
                    + "/"
                    + builtinCallExpression.arguments().size()
                    + ")";
            if (depth >= 2 || builtinCallExpression.arguments().isEmpty()) {
                return summary;
            }
            List<String> arguments = new ArrayList<>();
            for (QinCfaProgram.Expression argument : builtinCallExpression.arguments()) {
                arguments.add(expressionSummary(argument, depth + 1));
            }
            return summary + arguments;
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            return "ObjectLiteral(properties=" + objectLiteral.properties().size() + ")";
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            return "ArrayLiteral(elements=" + arrayLiteral.elements().size() + ")";
        }
        if (expression instanceof QinCfaProgram.SpreadArgumentExpression) {
            return "SpreadArgumentExpression";
        }
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            return "IdentifierReference(" + identifierReference.name() + ")";
        }
        if (expression instanceof QinCfaProgram.MemberAccessExpression memberAccessExpression) {
            return "MemberAccess(" + memberAccessExpression.objectName() + "." + memberAccessExpression.propertyName() + ")";
        }
        if (expression instanceof QinCfaProgram.StringLiteral stringLiteral) {
            return "StringLiteral(" + stringLiteral.value() + ")";
        }
        if (expression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            return "NumberLiteral(" + numberLiteral.value() + ")";
        }
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            return "FunctionLiteral(" + expressionSummary(functionLiteral.returnExpression(), depth + 1) + ")";
        }
        return expression.getClass().getSimpleName();
    }

    private String spreadArgumentPath(QinCfaProgram.Expression expression) {
        String path = spreadArgumentPath(expression, "$", 0);
        return path == null ? "<none>" : path;
    }

    private String spreadArgumentPath(QinCfaProgram.Expression expression, String path, int depth) {
        if (expression == null || depth > 40) {
            return null;
        }
        if (expression instanceof QinCfaProgram.SpreadArgumentExpression) {
            return path;
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
            for (int i = 0; i < builtinCallExpression.arguments().size(); i++) {
                String nested = spreadArgumentPath(
                        builtinCallExpression.arguments().get(i),
                        path + ".args[" + i + "]",
                        depth + 1);
                if (nested != null) {
                    return nested;
                }
            }
            return null;
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
                String nested = spreadArgumentPath(
                        property.value(),
                        path + "." + property.key(),
                        depth + 1);
                if (nested != null) {
                    return nested;
                }
            }
            return null;
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            for (int i = 0; i < arrayLiteral.elements().size(); i++) {
                String nested = spreadArgumentPath(arrayLiteral.elements().get(i), path + "[" + i + "]", depth + 1);
                if (nested != null) {
                    return nested;
                }
            }
            return null;
        }
        if (expression instanceof QinCfaProgram.LetExpression letExpression) {
            for (int i = 0; i < letExpression.localDeclarations().size(); i++) {
                String nested = spreadArgumentPath(
                        letExpression.localDeclarations().get(i).initializer(),
                        path + ".locals[" + i + "]",
                        depth + 1);
                if (nested != null) {
                    return nested;
                }
            }
            for (int i = 0; i < letExpression.leadingExpressions().size(); i++) {
                String nested = spreadArgumentPath(
                        letExpression.leadingExpressions().get(i),
                        path + ".leading[" + i + "]",
                        depth + 1);
                if (nested != null) {
                    return nested;
                }
            }
            return spreadArgumentPath(letExpression.resultExpression(), path + ".result", depth + 1);
        }
        if (expression instanceof QinCfaProgram.SequenceExpression sequenceExpression) {
            for (int i = 0; i < sequenceExpression.leadingExpressions().size(); i++) {
                String nested = spreadArgumentPath(
                        sequenceExpression.leadingExpressions().get(i),
                        path + ".leading[" + i + "]",
                        depth + 1);
                if (nested != null) {
                    return nested;
                }
            }
            return spreadArgumentPath(sequenceExpression.resultExpression(), path + ".result", depth + 1);
        }
        if (expression instanceof QinCfaProgram.StaticMethodCallExpression staticMethodCallExpression) {
            for (int i = 0; i < staticMethodCallExpression.arguments().size(); i++) {
                String nested = spreadArgumentPath(
                        staticMethodCallExpression.arguments().get(i),
                        path + ".staticArgs[" + i + "]",
                        depth + 1);
                if (nested != null) {
                    return nested;
                }
            }
        }
        if (expression instanceof QinCfaProgram.JavaInstanceMethodCallExpression instanceMethodCallExpression) {
            String receiver = spreadArgumentPath(
                    instanceMethodCallExpression.receiver(),
                    path + ".instanceReceiver",
                    depth + 1);
            if (receiver != null) {
                return receiver;
            }
            for (int i = 0; i < instanceMethodCallExpression.arguments().size(); i++) {
                String nested = spreadArgumentPath(
                        instanceMethodCallExpression.arguments().get(i),
                        path + ".instanceArgs[" + i + "]",
                        depth + 1);
                if (nested != null) {
                    return nested;
                }
            }
        }
        return null;
    }

    private boolean emitLocalAssignmentBuiltinAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
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
        LocalBinding localBinding = localBindings.get(bindingNameLiteral.value());
        if (localBinding != null) {
            QinCfaProgram.Expression valueExpression = builtinCallExpression.arguments().get(1);
            if (localBinding.staticType() != null
                    && localBinding.staticType().isArray()
                    && valueExpression instanceof QinCfaProgram.BuiltinCallExpression valueBuiltin
                    && emitJavaArraysCopyForParameter(
                            code,
                            generatedClassDesc,
                            bindings,
                            localBindings,
                            nextLocalSlot,
                            valueBuiltin,
                            localBinding.staticType())) {
                code.dup();
                code.astore(localBinding.slot());
                return true;
            }
            emitExpressionAsObject(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    valueExpression);
            code.dup();
            code.astore(localBinding.slot());
            return true;
        }
        DeclarationBinding binding = bindings.get(bindingNameLiteral.value());
        if (binding == null) {
            throw new IllegalArgumentException("QJS2008 unknown assignment target: " + bindingNameLiteral.value());
        }
        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                builtinCallExpression.arguments().get(1));
        code.dup();
        code.putstatic(generatedClassDesc, binding.fieldName(), OBJECT_DESC);
        return true;
    }

    private boolean emitStaticFieldAssignmentBuiltinAsObject(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (!"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_member_set__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 3) {
            return false;
        }
        String ownerBinaryName = staticFieldAssignmentOwnerOrNull(
                builtinCallExpression.arguments().get(0),
                bindings);
        if (ownerBinaryName == null || ownerBinaryName.isBlank()
                || !(builtinCallExpression.arguments().get(1) instanceof QinCfaProgram.StringLiteral propertyName)) {
            return false;
        }
        QinIrClassDeclaration declaration = lookupStaticDeclaration(ownerBinaryName);
        QinIrFieldDeclaration field = staticFieldOrNull(declaration, propertyName.value());
        if (declaration == null || field == null || !isReferenceStaticField(field)) {
            return false;
        }
        if (!emitRegexPatternDefaultCompileForStaticField(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                field,
                builtinCallExpression.arguments().get(2))) {
            emitExpressionAsObject(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression.arguments().get(2));
            coerceStaticFieldAssignmentValue(code, field.type());
        }
        code.putstatic(
                ClassDesc.of(declaration.binaryName()),
                field.name(),
                classDescForType(field.type()));
        code.getstatic(
                ClassDesc.of(declaration.binaryName()),
                field.name(),
                classDescForType(field.type()));
        return true;
    }

    private void coerceStaticFieldAssignmentValue(CodeBuilder code, QinIrTypeRef fieldType) {
        if (isJavaLangObjectArrayType(fieldType)) {
            code.invokestatic(
                    JS_SDK_GLOBAL_DESC,
                    "__qin_to_object_array__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)[Ljava/lang/Object;"));
            return;
        }
        code.checkcast(classDescForType(fieldType));
    }

    private boolean isJavaLangObjectArrayType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS) {
            return false;
        }
        String binaryName = type.binaryName();
        return "java.lang.Object[]".equals(binaryName)
                || "[Ljava.lang.Object;".equals(binaryName)
                || "[Ljava/lang/Object;".equals(binaryName);
    }

    private boolean emitRegexPatternDefaultCompileForStaticField(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinIrFieldDeclaration field,
            QinCfaProgram.Expression valueExpression) {
        if (field == null
                || field.type() == null
                || !"__QinJavaUtilRegexPattern".equals(field.type().binaryName())
                || !(valueExpression instanceof QinCfaProgram.StaticMethodCallExpression callExpression)
                || !"__QinJavaUtilRegexPattern".equals(callExpression.ownerBinaryName())
                || !"compile".equals(callExpression.methodName())
                || callExpression.arguments().size() != 1) {
            return false;
        }
        emitExpressionForParameter(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                callExpression.arguments().get(0),
                String.class);
        code.loadConstant(0.0d);
        code.invokestatic(
                ClassDesc.of("__QinJavaUtilRegexPattern"),
                "compile",
                MethodTypeDesc.of(
                        ClassDesc.of("__QinJavaUtilRegexPattern"),
                        STRING_DESC,
                        ClassDesc.ofDescriptor("D")));
        return true;
    }

    private String staticFieldAssignmentOwnerOrNull(
            QinCfaProgram.Expression targetExpression,
            Map<String, DeclarationBinding> bindings) {
        if (targetExpression instanceof QinCfaProgram.JavaClassLiteralExpression classLiteral) {
            return classLiteral.binaryName() == null || classLiteral.binaryName().isBlank()
                    ? classLiteral.typeName()
                    : classLiteral.binaryName();
        }
        if (targetExpression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            DeclarationBinding binding = bindings.get(identifierReference.name());
            if (binding == null) {
                binding = allDeclarationBindings.get(identifierReference.name());
            }
            if (binding != null
                    && binding.initializer() instanceof QinCfaProgram.JavaClassLiteralExpression classLiteral) {
                return classLiteral.binaryName() == null || classLiteral.binaryName().isBlank()
                        ? classLiteral.typeName()
                        : classLiteral.binaryName();
            }
        }
        return null;
    }

    private QinIrFieldDeclaration staticFieldOrNull(QinIrClassDeclaration declaration, String requestedFieldName) {
        if (declaration == null || requestedFieldName == null || requestedFieldName.isBlank()) {
            return null;
        }
        for (QinIrFieldDeclaration field : declaration.fields()) {
            if (field.staticField() && runtimeFieldNameMatches(field.name(), requestedFieldName)) {
                return field;
            }
        }
        return null;
    }

    private boolean isReferenceStaticField(QinIrFieldDeclaration field) {
        if (field == null || !field.staticField() || field.type() == null) {
            return false;
        }
        return field.type().kind() == QinIrTypeKind.CLASS
                || field.type().kind() == QinIrTypeKind.STRING;
    }

    private boolean runtimeFieldNameMatches(String actualFieldName, String requestedFieldName) {
        if (actualFieldName == null || requestedFieldName == null) {
            return false;
        }
        return actualFieldName.equals(requestedFieldName)
                || actualFieldName.equals("__qin_field_" + requestedFieldName)
                || requestedFieldName.equals("__qin_field_" + actualFieldName);
    }

    private void emitExpressionForParameter(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.Expression expression,
            Class<?> parameterType) {
        if (parameterType.isArray() && expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            emitArrayLiteralForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    arrayLiteral,
                    parameterType);
            return;
        }
        if (parameterType.isArray()
                && expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression
                && isArrayProducingBuiltinCall(builtinCallExpression)) {
            if (emitArrayFromFactoryForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression,
                    parameterType)) {
                return;
            }
            if (emitJavaArraysCopyForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression,
                    parameterType)) {
                return;
            }
            emitBuiltinCallAsObject(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression);
            if (parameterType == Object[].class) {
                code.invokestatic(
                        JS_SDK_GLOBAL_DESC,
                        "__qin_to_object_array__",
                        MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)[Ljava/lang/Object;"));
                return;
            }
            emitCheckcastRuntimeClass(code, parameterType);
            return;
        }
        if (parameterType.isArray()
                && expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression
                && emitStreamToArrayForParameter(
                        code,
                        generatedClassDesc,
                        bindings,
                        localBindings,
                        nextLocalSlot,
                        builtinCallExpression,
                        parameterType)) {
            return;
        }
        if (parameterType == Object.class
                && expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression
                && emitArrayFromFactoryForParameter(
                        code,
                        generatedClassDesc,
                        bindings,
                        localBindings,
                        nextLocalSlot,
                        builtinCallExpression,
                        Object.class)) {
            return;
        }
        if (parameterType == java.util.function.IntFunction.class) {
            String arrayDescriptor = staticJavaArrayConstructorFunctionDescriptor(expression);
            if (arrayDescriptor != null) {
                code.ldc(arrayDescriptor);
                code.invokestatic(
                        JS_SDK_GLOBAL_DESC,
                        "__qin_java_array_int_function__",
                        JAVA_ARRAY_INT_FUNCTION_SIGNATURE);
                return;
            }
        }
        if (isJavaFunctionalExpression(expression) && isJavaFunctionalInterface(parameterType)) {
            emitExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, expression);
            code.ldc(ClassDesc.of(parameterType.getName()));
            code.invokestatic(
                    JS_SDK_GLOBAL_DESC,
                    "__qin_java_functional_interface__",
                    FUNCTIONAL_INTERFACE_SIGNATURE);
            code.checkcast(ClassDesc.of(parameterType.getName()));
            return;
        }
        if (expression instanceof QinCfaProgram.NullLiteral) {
            if (parameterType.isPrimitive()) {
                throw new IllegalArgumentException("Null literal cannot target primitive parameter: "
                        + parameterType.getName());
            }
            code.aconst_null();
            return;
        }
        if (expression instanceof QinCfaProgram.StringLiteral stringLiteral) {
            if (parameterType.isPrimitive()) {
                throw new IllegalArgumentException("String literal cannot target primitive parameter: "
                        + parameterType.getName());
            }
            code.ldc(stringLiteral.value());
            return;
        }
        if (expression instanceof QinCfaProgram.BooleanLiteral booleanLiteral) {
            if (parameterType == boolean.class) {
                code.loadConstant(booleanLiteral.value() ? 1 : 0);
                return;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(Boolean.class)) {
                code.loadConstant(booleanLiteral.value() ? 1 : 0);
                code.invokestatic(BOOLEAN_DESC, "valueOf", BOOLEAN_VALUE_OF_SIGNATURE);
                return;
            }
            throw new IllegalArgumentException("Boolean literal cannot target parameter: "
                    + parameterType.getName());
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

        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            if (parameterType.isPrimitive()) {
                throw new IllegalArgumentException("Object literal cannot target primitive parameter: "
                        + parameterType.getName());
            }
            if (parameterType != Object.class && !parameterType.isAssignableFrom(LinkedHashMap.class)) {
                throw new IllegalArgumentException("Object literal cannot target parameter: "
                        + parameterType.getName());
            }
            emitObjectLiteralAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, objectLiteral);
            if (parameterType != Object.class) {
                code.checkcast(ClassDesc.of(parameterType.getName()));
            }
            return;
        }
        if (expression instanceof QinCfaProgram.IdentifierReference) {
            if (parameterType.isPrimitive()) {
                throw new IllegalArgumentException("Identifier reference cannot target primitive parameter: "
                        + parameterType.getName());
            }
            emitExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, expression);
            if (parameterType != Object.class) {
                emitCheckcastRuntimeClass(code, parameterType);
            }
            return;
        }

        if (expression instanceof QinCfaProgram.JavaNewExpression javaNewExpression
                && isStaticExpressionAssignableToParameter(expression, parameterType, localBindings)) {
            emitJavaNewExpression(code, javaNewExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.StaticMethodCallExpression staticMethodCallExpression
                && isStaticExpressionAssignableToParameter(expression, parameterType, localBindings)) {
            emitStaticMethodCallExpression(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    staticMethodCallExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.JavaInstanceMethodCallExpression instanceMethodCallExpression
                && isStaticExpressionAssignableToParameter(expression, parameterType, localBindings)) {
            emitJavaInstanceMethodCallExpression(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    instanceMethodCallExpression);
            return;
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression
                && isStaticExpressionAssignableToParameter(expression, parameterType, localBindings)) {
            emitBuiltinCallAsObject(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression);
            if (parameterType != Object.class) {
                emitCheckcastRuntimeClass(code, parameterType);
            }
            return;
        }

        throw new IllegalArgumentException("Unsupported call argument expression: "
                + expressionSummary(expression)
                + "; parameterType="
                + parameterType.getTypeName()
                + "; inferredStaticType="
                + staticTypeNameOrNull(staticExpressionType(expression, localBindings)));
        }

    private void emitCheckcastRuntimeClass(CodeBuilder code, Class<?> runtimeClass) {
        code.checkcast(classDescForRuntimeClass(runtimeClass));
    }

    private boolean emitArrayFromFactoryForParameter(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression,
            Class<?> parameterType) {
        QinCfaProgram.BuiltinCallExpression arrayFromCall = arrayFromBuiltinCallOrNull(builtinCallExpression);
        if (arrayFromCall == null) {
            return false;
        }
        if (!(arrayFromCall.arguments().get(0) instanceof QinCfaProgram.ObjectLiteral source)) {
            return false;
        }
        QinCfaProgram.Expression factoryReturnExpression =
                staticArrayFactoryReturnExpression(arrayFromCall.arguments().get(1));
        if (factoryReturnExpression == null) {
            return false;
        }
        QinCfaProgram.Expression lengthExpression = staticLengthExpression(source);
        if (lengthExpression == null) {
            return false;
        }
        Class<?> arrayType = arrayTypeForArrayFromFactory(parameterType, factoryReturnExpression, localBindings);
        if (arrayType == null) {
            return false;
        }
        emitStaticArrayFromFactory(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                lengthExpression,
                factoryReturnExpression,
                arrayType);
        return true;
    }

    private Class<?> arrayTypeForArrayFromFactory(
            Class<?> parameterType,
            QinCfaProgram.Expression factoryReturnExpression,
            Map<String, LocalBinding> localBindings) {
        if (parameterType == null || parameterType.isPrimitive()) {
            return null;
        }
        if (parameterType.isArray()) {
            Class<?> componentType = parameterType.getComponentType();
            return arrayFactoryExpressionCompatibleWithComponent(factoryReturnExpression, componentType)
                    ? parameterType
                    : null;
        }
        if (parameterType == Object.class) {
            Class<?> componentType = staticArrayFactoryComponentType(factoryReturnExpression, localBindings);
            return componentType == null ? null : Array.newInstance(componentType, 0).getClass();
        }
        return null;
    }

    private boolean emitStreamToArrayForParameter(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression,
            Class<?> parameterType) {
        Class<?> arrayType = staticStreamToArrayIntFunctionArrayType(builtinCallExpression, localBindings);
        if (arrayType == null || !parameterType.isAssignableFrom(arrayType)) {
            return false;
        }
        StaticCallMethod staticCallMethod = staticCallMethodOrNull(builtinCallExpression);
        if (staticCallMethod == null) {
            return false;
        }
        ResolvedMethod resolvedMethod = resolveInstanceMethod(
                staticCallMethod.ownerClass().getName(),
                staticCallMethod.methodName(),
                staticCallMethod.arguments());
        emitExpressionAsObject(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                staticCallMethod.receiver());
        emitJavaReceiverCast(code, resolvedMethod);
        emitArgumentsForParameters(
                code,
                staticCallMethod.arguments(),
                resolvedMethod.parameterTypes(),
                resolvedMethod.parameterDescs(),
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                resolvedMethod.varArgs());
        invokeInstanceMethod(code, resolvedMethod);
        emitCheckcastRuntimeClass(code, parameterType);
        return true;
    }

    private boolean arrayFactoryExpressionCompatibleWithComponent(
            QinCfaProgram.Expression factoryReturnExpression,
            Class<?> componentType) {
        if (componentType == null) {
            return false;
        }
        if (factoryReturnExpression instanceof QinCfaProgram.NullLiteral) {
            return !componentType.isPrimitive();
        }
        if (componentType == Object.class) {
            return true;
        }
        return compatibilityScore(factoryReturnExpression, componentType) >= 0;
    }

    private void emitStaticArrayFromFactory(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.Expression lengthExpression,
            QinCfaProgram.Expression factoryReturnExpression,
            Class<?> arrayType) {
        Class<?> componentType = arrayType.getComponentType();
        emitExpressionAsInt(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, lengthExpression);
        emitNewArrayWithLengthOnStack(code, componentType);
        if (isJvmArrayDefaultValue(factoryReturnExpression, componentType)) {
            return;
        }

        int arraySlot = nextLocalSlot;
        int indexSlot = nextLocalSlot + 1;
        code.astore(arraySlot);
        code.loadConstant(0);
        code.istore(indexSlot);
        Label testLabel = code.newLabel();
        Label doneLabel = code.newLabel();
        code.labelBinding(testLabel);
        code.iload(indexSlot);
        code.aload(arraySlot);
        code.arraylength();
        code.if_icmpge(doneLabel);
        code.aload(arraySlot);
        code.iload(indexSlot);
        emitArrayFactoryElement(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot + 2,
                factoryReturnExpression,
                componentType);
        emitArrayStore(code, componentType);
        code.iinc(indexSlot, 1);
        code.goto_(testLabel);
        code.labelBinding(doneLabel);
        code.aload(arraySlot);
    }

    private void emitNewArrayWithLengthOnStack(CodeBuilder code, Class<?> componentType) {
        if (componentType == int.class) {
            code.newarray(TypeKind.INT);
            return;
        }
        if (componentType == double.class) {
            code.newarray(TypeKind.DOUBLE);
            return;
        }
        if (componentType == boolean.class) {
            code.newarray(TypeKind.BOOLEAN);
            return;
        }
        code.anewarray(classDescForRuntimeClass(componentType));
    }

    private boolean isJvmArrayDefaultValue(
            QinCfaProgram.Expression factoryReturnExpression,
            Class<?> componentType) {
        if (!componentType.isPrimitive()) {
            return factoryReturnExpression instanceof QinCfaProgram.NullLiteral;
        }
        if (componentType == int.class && factoryReturnExpression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            return (int) numberLiteral.value() == 0;
        }
        if (componentType == double.class && factoryReturnExpression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            return numberLiteral.value() == 0.0;
        }
        if (componentType == boolean.class && factoryReturnExpression instanceof QinCfaProgram.BooleanLiteral booleanLiteral) {
            return !booleanLiteral.value();
        }
        return false;
    }

    private void emitArrayFactoryElement(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.Expression factoryReturnExpression,
            Class<?> componentType) {
        if (componentType == Object.class) {
            emitExpressionAsObject(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    factoryReturnExpression);
            return;
        }
        emitExpressionForParameter(
                code,
                generatedClassDesc,
                bindings,
                localBindings,
                nextLocalSlot,
                factoryReturnExpression,
                componentType);
    }

    private void emitArrayStore(CodeBuilder code, Class<?> componentType) {
        if (componentType == int.class) {
            code.iastore();
            return;
        }
        if (componentType == double.class) {
            code.dastore();
            return;
        }
        if (componentType == boolean.class) {
            code.bastore();
            return;
        }
        code.aastore();
    }

    private boolean emitJavaArraysCopyForParameter(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.BuiltinCallExpression builtinCallExpression,
            Class<?> arrayType) {
        if (!isJavaArraysCopyBuiltinCall(builtinCallExpression) || arrayType == null || !arrayType.isArray()) {
            return false;
        }
        int argumentCount = builtinCallExpression.arguments().size();
        if ("copyOf".equals(builtinCallExpression.methodName()) && argumentCount == 2) {
            ClassDesc arrayDesc = classDescForRuntimeClass(arrayType);
            emitExpressionForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression.arguments().get(0),
                    arrayType);
            emitExpressionAsInt(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression.arguments().get(1));
            code.invokestatic(
                    ClassDesc.of("java.util.Arrays"),
                    "copyOf",
                    MethodTypeDesc.of(arrayDesc, arrayDesc, ClassDesc.ofDescriptor("I")));
            return true;
        }
        if ("copyOfRange".equals(builtinCallExpression.methodName()) && argumentCount == 3) {
            ClassDesc arrayDesc = classDescForRuntimeClass(arrayType);
            emitExpressionForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression.arguments().get(0),
                    arrayType);
            emitExpressionAsInt(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression.arguments().get(1));
            emitExpressionAsInt(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    builtinCallExpression.arguments().get(2));
            code.invokestatic(
                    ClassDesc.of("java.util.Arrays"),
                    "copyOfRange",
                    MethodTypeDesc.of(arrayDesc, arrayDesc, ClassDesc.ofDescriptor("I"), ClassDesc.ofDescriptor("I")));
            return true;
        }
        return false;
    }

    private void emitExpressionAsInt(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.Expression expression) {
        if (expression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            code.loadConstant((int) numberLiteral.value());
            return;
        }
        emitExpressionAsObject(code, generatedClassDesc, bindings, localBindings, nextLocalSlot, expression);
        coerceObjectStackForParameter(code, int.class);
    }

    private Integer staticLengthProperty(QinCfaProgram.ObjectLiteral objectLiteral) {
        QinCfaProgram.Expression expression = staticLengthExpression(objectLiteral);
        if (!(expression instanceof QinCfaProgram.NumberLiteral numberLiteral)) {
            return null;
        }
        double value = numberLiteral.value();
        if (value < 0 || value != Math.rint(value) || value > Integer.MAX_VALUE) {
            return null;
        }
        return (int) value;
    }

    private QinCfaProgram.Expression staticLengthExpression(QinCfaProgram.ObjectLiteral objectLiteral) {
        if (objectLiteral.properties().size() != 1) {
            return null;
        }
        QinCfaProgram.ObjectProperty property = objectLiteral.properties().get(0);
        if (!"length".equals(property.key())) {
            return null;
        }
        return property.value();
    }

    private boolean isStaticExpressionAssignableToParameter(
            QinCfaProgram.Expression expression,
            Class<?> parameterType) {
        return isStaticExpressionAssignableToParameter(expression, parameterType, Map.of());
    }

    private boolean isStaticExpressionAssignableToParameter(
            QinCfaProgram.Expression expression,
            Class<?> parameterType,
            Map<String, LocalBinding> localBindings) {
        if (parameterType == Object.class
                && expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression
                && resolveBuiltinMethod(builtinCallExpression).isPresent()) {
            return true;
        }
        Class<?> staticType = staticExpressionType(expression, localBindings);
        return staticType != null
                && !staticType.isPrimitive()
                && !parameterType.isPrimitive()
                && parameterType.isAssignableFrom(staticType);
    }

    private String staticTypeNameOrNull(Class<?> staticType) {
        return staticType == null ? "<null>" : staticType.getTypeName();
    }

    private void emitArrayLiteralForParameter(
            CodeBuilder code,
            ClassDesc generatedClassDesc,
            Map<String, DeclarationBinding> bindings,
            Map<String, LocalBinding> localBindings,
            int nextLocalSlot,
            QinCfaProgram.ArrayLiteral arrayLiteral,
            Class<?> arrayType) {
        Class<?> componentType = arrayType.getComponentType();
        if (componentType == null) {
            throw new IllegalArgumentException("Array parameter type has no component: " + arrayType.getName());
        }
        List<QinCfaProgram.Expression> elements = arrayLiteral.elements();
        code.loadConstant(elements.size());
        if (componentType == int.class) {
            code.newarray(TypeKind.INT);
            for (int i = 0; i < elements.size(); i++) {
                code.dup();
                code.loadConstant(i);
                emitNumericArrayElement(code, elements.get(i), componentType);
                code.iastore();
            }
            return;
        }
        if (componentType == double.class) {
            code.newarray(TypeKind.DOUBLE);
            for (int i = 0; i < elements.size(); i++) {
                code.dup();
                code.loadConstant(i);
                emitNumericArrayElement(code, elements.get(i), componentType);
                code.dastore();
            }
            return;
        }
        if (componentType == boolean.class) {
            code.newarray(TypeKind.BOOLEAN);
            for (int i = 0; i < elements.size(); i++) {
                code.dup();
                code.loadConstant(i);
                emitBooleanArrayElement(code, elements.get(i));
                code.bastore();
            }
            return;
        }
        code.anewarray(ClassDesc.of(componentType.getName()));
        for (int i = 0; i < elements.size(); i++) {
            code.dup();
            code.loadConstant(i);
            emitExpressionForParameter(
                    code,
                    generatedClassDesc,
                    bindings,
                    localBindings,
                    nextLocalSlot,
                    elements.get(i),
                    componentType);
            code.aastore();
        }
    }

    private void emitNumericArrayElement(
            CodeBuilder code,
            QinCfaProgram.Expression element,
            Class<?> componentType) {
        if (!(element instanceof QinCfaProgram.NumberLiteral numberLiteral)) {
            throw new IllegalArgumentException("Unsupported numeric array element expression: "
                    + element.getClass().getSimpleName());
        }
        if (componentType == int.class) {
            code.loadConstant((int) numberLiteral.value());
            return;
        }
        if (componentType == double.class) {
            code.loadConstant(numberLiteral.value());
            return;
        }
        throw new IllegalArgumentException("Unsupported numeric array component type: " + componentType.getName());
    }

    private void emitBooleanArrayElement(CodeBuilder code, QinCfaProgram.Expression element) {
        if (!(element instanceof QinCfaProgram.BooleanLiteral booleanLiteral)) {
            throw new IllegalArgumentException("Unsupported boolean array element expression: "
                    + element.getClass().getSimpleName());
        }
        code.loadConstant(booleanLiteral.value() ? 1 : 0);
    }

    private ResolvedConstructor resolveConstructor(String ownerBinaryName, List<QinCfaProgram.Expression> arguments) {
        ResolvedConstructor generatedConstructor = resolveGeneratedConstructor(ownerBinaryName, arguments);
        if (generatedConstructor != null) {
            return generatedConstructor;
        }
        try {
            Class<?> ownerClass = resolveJavaClass(ownerBinaryName);
            boolean enumOwner = ownerClass.isEnum();
            Constructor<?> best = null;
            Class<?>[] bestParameterTypes = null;
            int bestScore = Integer.MAX_VALUE;
            Constructor<?>[] constructors = ownerClass.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (enumOwner) {
                    if (parameterTypes.length < 2
                            || parameterTypes[0] != String.class
                            || parameterTypes[1] != int.class) {
                        continue;
                    }
                    parameterTypes = Arrays.copyOfRange(parameterTypes, 2, parameterTypes.length);
                }
                int score = compatibilityScore(arguments, parameterTypes);
                if (score < 0) {
                    continue;
                }
                if (score < bestScore) {
                    best = constructor;
                    bestParameterTypes = parameterTypes;
                    bestScore = score;
                } else if (score == bestScore) {
                    throw new IllegalArgumentException("Ambiguous constructor for " + ownerBinaryName);
                }
            }
            if (best == null) {
                throw new IllegalArgumentException("No compatible constructor for "
                        + ownerBinaryName
                        + " args="
                        + describeArgumentTypes(arguments));
            }
            return new ResolvedConstructor(
                    bestParameterTypes,
                    MethodTypeDesc.ofDescriptor(MethodType.methodType(void.class, bestParameterTypes)
                            .toMethodDescriptorString()),
                    best.isVarArgs());
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot resolve Java constructor: " + ownerBinaryName, e);
        }
    }

    private ResolvedConstructor resolveGeneratedConstructor(
            String ownerBinaryName,
            List<QinCfaProgram.Expression> arguments) {
        QinIrClassDeclaration declaration = lookupStaticDeclaration(ownerBinaryName);
        if (declaration == null) {
            return null;
        }
        List<QinIrMethodDeclaration> constructors = generatedConstructors(declaration);
        if (constructors.isEmpty()) {
            if (arguments == null || arguments.isEmpty()) {
                return new ResolvedConstructor(new Class<?>[0], VOID_INIT, false);
            }
            return null;
        }
        QinIrMethodDeclaration selected = null;
        int bestScore = Integer.MAX_VALUE;
        for (QinIrMethodDeclaration constructor : constructors) {
            Class<?>[] runtimeParameterTypes = constructor.parameters().stream()
                    .map(parameter -> runtimeClassForType(parameter.type()))
                    .toArray(Class<?>[]::new);
            int score = compatibilityScore(
                    arguments,
                    runtimeParameterTypes,
                    hasVarArgsParameter(constructor));
            if (score < 0) {
                continue;
            }
            if (score < bestScore) {
                selected = constructor;
                bestScore = score;
                continue;
            }
            if (score == bestScore) {
                throw new IllegalArgumentException("Ambiguous generated constructor: "
                        + ownerBinaryName + "/" + (arguments == null ? 0 : arguments.size()));
            }
        }
        if (selected == null) {
            throw new IllegalArgumentException("No compatible generated constructor: "
                    + ownerBinaryName
                    + " args="
                    + describeArgumentTypes(arguments)
                    + " candidates="
                    + describeGeneratedConstructors(constructors)
                    + " runtimeCandidates="
                    + describeGeneratedConstructorRuntimeCompatibility(constructors, arguments));
        }
        Class<?>[] runtimeParameterTypes = selected.parameters().stream()
                .map(parameter -> runtimeClassForType(parameter.type()))
                .toArray(Class<?>[]::new);
        ClassDesc[] parameterDescs = selected.parameters().stream()
                .map(parameter -> classDescForType(parameter.type()))
                .toArray(ClassDesc[]::new);
        return new ResolvedConstructor(
                runtimeParameterTypes,
                MethodTypeDesc.of(ClassDesc.ofDescriptor("V"), parameterDescs),
                hasVarArgsParameter(selected));
    }

    private List<QinIrMethodDeclaration> generatedConstructors(QinIrClassDeclaration declaration) {
        List<QinIrMethodDeclaration> constructors = new ArrayList<>();
        List<QinIrMethodDeclaration> generatedHelpers = new ArrayList<>();
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (method == null || method.staticMethod()) {
                continue;
            }
            if ("constructor".equals(method.name())) {
                constructors.add(method);
                continue;
            }
            if (method.name().startsWith("__qin_constructor_")) {
                generatedHelpers.add(method);
            }
        }
        return constructors.isEmpty()
                ? List.copyOf(generatedHelpers)
                : List.copyOf(constructors);
    }

    private ResolvedMethod resolveStaticMethod(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        ResolvedMethod generatedMethod = resolveGeneratedStaticMethod(ownerBinaryName, methodName, arguments);
        if (generatedMethod != null) {
            return generatedMethod;
        }
        ResolvedMethod resolvedMethod = resolveMethod(ownerBinaryName, methodName, arguments);
        if (!resolvedMethod.staticMethod()) {
            throw new IllegalArgumentException("Method is not static: " + ownerBinaryName + "." + methodName);
        }
        return resolvedMethod;
    }

    private ResolvedMethod resolveGeneratedStaticMethod(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        List<QinIrClassDeclaration> ownerCandidates = generatedStaticOwnerCandidates(ownerBinaryName);
        if (ownerCandidates.isEmpty()) {
            return null;
        }
        GeneratedMethodSelection selected =
                selectGeneratedStaticMethod(ownerBinaryName, methodName, arguments, ownerCandidates, true);
        if (selected == null) {
            throw new IllegalArgumentException("No compatible generated static method: "
                    + ownerBinaryName + "." + methodName
                    + " candidates="
                    + describeGeneratedStaticMethodCompatibility(ownerCandidates, methodName, arguments));
        }
        Class<?>[] runtimeParameterTypes = selected.method().parameters().stream()
                .map(parameter -> runtimeClassForType(parameter.type()))
                .toArray(Class<?>[]::new);
        ClassDesc[] parameterDescs = selected.method().parameters().stream()
                .map(parameter -> classDescForType(parameter.type()))
                .toArray(ClassDesc[]::new);
        return new ResolvedMethod(
                null,
                selected.owner().binaryName(),
                selected.method().name(),
                runtimeParameterTypes,
                parameterDescs,
                runtimeClassForType(selected.method().returnType()),
                methodDescriptor(selected.method()),
                true,
                hasVarArgsParameter(selected.method()),
                selected.owner().interfaceClass());
    }

    private int generatedLocalMethodMatchScore(
            QinIrMethodDeclaration method,
            List<QinCfaProgram.Expression> arguments) {
        if (method == null || arguments == null) {
            return -1;
        }
        List<QinIrParameter> parameters = method.parameters();
        boolean varArgs = hasVarArgsParameter(method);
        int fixedParameterCount = varArgs ? parameters.size() - 1 : parameters.size();
        if (!varArgs && arguments.size() != parameters.size()) {
            return -1;
        }
        if (varArgs && arguments.size() < fixedParameterCount) {
            return -1;
        }
        int score = 0;
        int checkedParameterCount = varArgs ? fixedParameterCount : arguments.size();
        for (int i = 0; i < checkedParameterCount; i++) {
            int parameterScore = generatedLocalArgumentMatchScore(
                    parameters.get(i).type(),
                    arguments.get(i),
                    new LinkedHashSet<>());
            if (parameterScore < 0) {
                return -1;
            }
            score += parameterScore;
        }
        if (!varArgs) {
            return score;
        }
        QinIrTypeRef varargsArrayType = parameters.get(parameters.size() - 1).type();
        if (arguments.size() == parameters.size()) {
            int packedScore = generatedLocalArgumentMatchScore(
                    varargsArrayType,
                    arguments.get(fixedParameterCount),
                    new LinkedHashSet<>());
            if (packedScore >= 0) {
                return score + packedScore;
            }
        }
        QinIrTypeRef elementType = varargsElementType(varargsArrayType);
        for (int i = fixedParameterCount; i < arguments.size(); i++) {
            int parameterScore = generatedLocalArgumentMatchScore(
                    elementType,
                    arguments.get(i),
                    new LinkedHashSet<>());
            if (parameterScore < 0) {
                return -1;
            }
            score += parameterScore;
        }
        return score + 1;
    }

    private ResolvedMethod resolveGeneratedInstanceMethod(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        QinIrClassDeclaration declaration = lookupStaticDeclaration(ownerBinaryName);
        if (declaration == null) {
            return null;
        }
        List<GeneratedMethodCandidate> candidates = new ArrayList<>();
        collectGeneratedMethodCandidates(
                declaration,
                methodName,
                arguments,
                false,
                0,
                new LinkedHashSet<>(),
                candidates);
        if (candidates.isEmpty()) {
            return null;
        }
        GeneratedMethodCandidate selected = null;
        for (GeneratedMethodCandidate candidate : candidates) {
            if (selected == null
                    || candidate.score() < selected.score()
                    || (candidate.score() == selected.score() && candidate.depth() < selected.depth())) {
                selected = candidate;
                continue;
            }
            if (candidate.score() == selected.score() && candidate.depth() == selected.depth()) {
                throw new IllegalArgumentException("Ambiguous generated instance method: "
                        + ownerBinaryName + "." + methodName);
            }
        }
        return resolvedGeneratedMethod(selected.owner(), selected.method());
    }

    private void collectGeneratedMethodCandidates(
            QinIrClassDeclaration declaration,
            String methodName,
            List<QinCfaProgram.Expression> arguments,
            boolean staticMethod,
            int depth,
            Set<String> visited,
            List<GeneratedMethodCandidate> candidates) {
        if (declaration == null || !visited.add(declaration.binaryName())) {
            return;
        }
        int argumentCount = arguments == null ? 0 : arguments.size();
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (method.staticMethod() != staticMethod || !method.name().equals(methodName)) {
                continue;
            }
            Class<?>[] runtimeParameterTypes = method.parameters().stream()
                    .map(parameter -> runtimeClassForType(parameter.type()))
                    .toArray(Class<?>[]::new);
            int score = compatibilityScore(arguments, runtimeParameterTypes, hasVarArgsParameter(method));
            if (score >= 0) {
                candidates.add(new GeneratedMethodCandidate(declaration, method, score, depth));
            }
        }
        if (staticMethod) {
            return;
        }
        collectGeneratedMethodCandidates(
                lookupStaticDeclaration(binaryNameOfType(declaration.superType())),
                methodName,
                arguments,
                false,
                depth + 1,
                visited,
                candidates);
        for (QinIrTypeRef interfaceType : declaration.implementsTypes()) {
            collectGeneratedMethodCandidates(
                    lookupStaticDeclaration(binaryNameOfType(interfaceType)),
                    methodName,
                    arguments,
                    false,
                    depth + 1,
                    visited,
                    candidates);
        }
    }

    private String binaryNameOfType(QinIrTypeRef type) {
        return type == null || type.kind() != QinIrTypeKind.CLASS ? null : type.binaryName();
    }

    private ResolvedMethod resolvedGeneratedMethod(
            QinIrClassDeclaration owner,
            QinIrMethodDeclaration method) {
        Class<?>[] runtimeParameterTypes = method.parameters().stream()
                .map(parameter -> runtimeClassForType(parameter.type()))
                .toArray(Class<?>[]::new);
        ClassDesc[] parameterDescs = method.parameters().stream()
                .map(parameter -> classDescForType(parameter.type()))
                .toArray(ClassDesc[]::new);
        return new ResolvedMethod(
                null,
                owner.binaryName(),
                method.name(),
                runtimeParameterTypes,
                parameterDescs,
                runtimeClassForType(method.returnType()),
                methodDescriptor(method),
                method.staticMethod(),
                hasVarArgsParameter(method),
                owner.interfaceClass());
    }

    private QinIrClassDeclaration lookupStaticDeclaration(String ownerBinaryName) {
        if (ownerBinaryName == null || ownerBinaryName.isBlank() || staticDeclarationIndex.isEmpty()) {
            return null;
        }
        if (isQinHostRuntimeBinaryName(ownerBinaryName)) {
            return null;
        }
        QinIrClassDeclaration declaration = staticDeclarationIndex.get(ownerBinaryName);
        if (declaration != null) {
            QinIrClassDeclaration generatedLocalDeclaration = generatedLocalDeclarationForOriginalOwner(declaration);
            if (generatedLocalDeclaration == null) {
                generatedLocalDeclaration = uniqueGeneratedLocalDeclarationForSimpleOriginalOwner(ownerBinaryName);
            }
            return generatedLocalDeclaration == null ? declaration : generatedLocalDeclaration;
        }
        declaration = uniqueGeneratedLocalDeclarationForSimpleOriginalOwner(ownerBinaryName);
        if (declaration != null) {
            return declaration;
        }
        String flattened = flattenedBinaryAlias(ownerBinaryName);
        declaration = staticDeclarationIndex.get(flattened);
        if (declaration != null) {
            return declaration;
        }
        String canonical = canonicalJavaBinaryName(ownerBinaryName);
        return staticDeclarationIndex.get(canonical);
    }

    private QinIrClassDeclaration generatedLocalDeclarationForOriginalOwner(QinIrClassDeclaration declaration) {
        if (declaration == null
                || declaration.binaryName() == null
                || declaration.binaryName().isBlank()
                || !declaration.binaryName().contains(".")
                || staticDeclarationIndex == null
                || staticDeclarationIndex.isEmpty()) {
            return null;
        }
        String flattenedOwner = flattenedBinaryAlias(declaration.binaryName());
        QinIrClassDeclaration localDeclaration = staticDeclarationIndex.get(flattenedOwner);
        if (localDeclaration == null
                || localDeclaration.binaryName() == null
                || localDeclaration.binaryName().isBlank()
                || localDeclaration.binaryName().equals(declaration.binaryName())
                || !localDeclaration.binaryName().equals(flattenedOwner)) {
            return null;
        }
        return localDeclaration;
    }

    private QinIrClassDeclaration uniqueGeneratedLocalDeclarationForSimpleOriginalOwner(String ownerBinaryName) {
        if (staticDeclarationIndex == null
                || staticDeclarationIndex.isEmpty()
                || ownerBinaryName == null
                || ownerBinaryName.isBlank()
                || ownerBinaryName.contains(".")
                || ownerBinaryName.contains("_")) {
            return null;
        }
        QinIrClassDeclaration matched = null;
        for (QinIrClassDeclaration candidate : staticDeclarationIndex.values()) {
            String candidateBinaryName = candidate == null ? null : candidate.binaryName();
            if (candidateBinaryName == null
                    || candidateBinaryName.isBlank()
                    || candidateBinaryName.contains(".")
                    || !candidateBinaryName.contains("_")) {
                continue;
            }
            String originalBinaryName = inferredOriginalJavaBinaryName(candidateBinaryName);
            if (originalBinaryName == null
                    || !candidateBinaryName.equals(flattenedBinaryAlias(originalBinaryName))
                    || !ownerBinaryName.equals(simpleBinaryName(originalBinaryName))) {
                continue;
            }
            if (matched != null && !matched.binaryName().equals(candidateBinaryName)) {
                return null;
            }
            matched = candidate;
        }
        return matched;
    }

    private String inferredOriginalJavaBinaryName(String generatedBinaryName) {
        if (generatedBinaryName == null || generatedBinaryName.isBlank() || !generatedBinaryName.contains("_")) {
            return null;
        }
        String candidate = generatedBinaryName.replace('_', '.');
        return candidate.equals(generatedBinaryName) || candidate.contains("..") ? null : candidate;
    }

    private String simpleBinaryName(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            return binaryName;
        }
        int split = Math.max(binaryName.lastIndexOf('.'), binaryName.lastIndexOf('$'));
        return split < 0 || split + 1 >= binaryName.length() ? binaryName : binaryName.substring(split + 1);
    }

    private ResolvedMethod resolveInstanceMethod(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        ResolvedMethod generatedMethod = resolveGeneratedInstanceMethod(ownerBinaryName, methodName, arguments);
        if (generatedMethod != null) {
            return generatedMethod;
        }
        ResolvedMethod resolvedMethod = resolveMethod(ownerBinaryName, methodName, arguments);
        if (resolvedMethod.staticMethod()) {
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
            Class<?> ownerClass = resolveJavaClass(ownerBinaryName);
            Method comparatorFacadeMethod = findJavaComparatorFacadeMethod(ownerClass, methodName, arguments);
            if (comparatorFacadeMethod != null) {
                return resolvedMethod(comparatorFacadeMethod);
            }
            Method collectionToArrayMethod = findJavaCollectionTypedToArrayMethod(ownerClass, methodName, arguments);
            if (collectionToArrayMethod != null) {
                return resolvedMethod(collectionToArrayMethod);
            }
            Method best = null;
            int bestScore = Integer.MAX_VALUE;
            for (Method method : publicMethodsInClassfileOrder(ownerClass)) {
                if (!method.getName().equals(methodName)) {
                    continue;
                }
                int score = compatibilityScore(arguments, method.getParameterTypes(), method.isVarArgs());
                if (score < 0) {
                    continue;
                }
                if (score < bestScore) {
                    best = method;
                    bestScore = score;
                } else if (score == bestScore) {
                    if (isStrictlyMoreSpecific(method.getParameterTypes(), best.getParameterTypes())) {
                        best = method;
                    } else if (isStrictlyMoreSpecific(best.getParameterTypes(), method.getParameterTypes())) {
                        continue;
                    } else if (hasNullArgument(arguments) && isQinOwnedStaticFacadeOwner(ownerBinaryName)) {
                        continue;
                    } else {
                        throw new IllegalArgumentException("Ambiguous method: " + ownerBinaryName + "." + methodName);
                    }
                }
            }
            if (best == null) {
                throw new IllegalArgumentException("No compatible method: " + ownerBinaryName + "." + methodName);
            }
            return resolvedMethod(best);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot resolve Java method: "
                    + ownerBinaryName + "." + methodName, e);
        }
    }

    private ResolvedMethod resolvedMethod(Method method) {
        return new ResolvedMethod(
                method,
                method.getDeclaringClass().getName(),
                method.getName(),
                method.getParameterTypes(),
                parameterDescsForTypes(method.getParameterTypes()),
                method.getReturnType(),
                MethodTypeDesc.ofDescriptor(MethodType.methodType(method.getReturnType(), method.getParameterTypes())
                        .toMethodDescriptorString()),
                Modifier.isStatic(method.getModifiers()),
                method.isVarArgs(),
                method.getDeclaringClass().isInterface());
    }

    private ClassDesc[] parameterDescsForTypes(Class<?>[] parameterTypes) {
        ClassDesc[] result = new ClassDesc[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            result[i] = classDescForRuntimeClass(parameterTypes[i]);
        }
        return result;
    }

    private ClassDesc classDescForRuntimeClass(Class<?> type) {
        if (type.isArray()) {
            return ClassDesc.ofDescriptor(type.descriptorString());
        }
        if (type.isPrimitive()) {
            return ClassDesc.ofDescriptor(MethodType.methodType(type).returnType().descriptorString());
        }
        return ClassDesc.of(type.getName());
    }

    private Method findJavaComparatorFacadeMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        if (ownerClass != java.util.Comparator.class || arguments == null) {
            return null;
        }
        try {
            int argumentCount = arguments.size();
            if ("comparing".equals(methodName) && argumentCount == 1) {
                return java.util.Comparator.class.getMethod("comparing", java.util.function.Function.class);
            }
            if ("comparing".equals(methodName) && argumentCount == 2) {
                return java.util.Comparator.class.getMethod(
                        "comparing",
                        java.util.function.Function.class,
                        java.util.Comparator.class);
            }
            if ("comparingInt".equals(methodName) && argumentCount == 1) {
                return java.util.Comparator.class.getMethod("comparingInt", java.util.function.ToIntFunction.class);
            }
            if ("comparingLong".equals(methodName) && argumentCount == 1) {
                return java.util.Comparator.class.getMethod("comparingLong", java.util.function.ToLongFunction.class);
            }
            if ("comparingDouble".equals(methodName) && argumentCount == 1) {
                return java.util.Comparator.class.getMethod(
                        "comparingDouble",
                        java.util.function.ToDoubleFunction.class);
            }
            if ("thenComparing".equals(methodName) && argumentCount == 1) {
                QinCfaProgram.Expression argument = arguments.get(0);
                Class<?> staticType = staticExpressionType(argument);
                if (staticType != null && java.util.Comparator.class.isAssignableFrom(staticType)) {
                    return java.util.Comparator.class.getMethod("thenComparing", java.util.Comparator.class);
                }
                return java.util.Comparator.class.getMethod("thenComparing", java.util.function.Function.class);
            }
            if ("thenComparingInt".equals(methodName) && argumentCount == 1) {
                return java.util.Comparator.class.getMethod("thenComparingInt", java.util.function.ToIntFunction.class);
            }
            if ("thenComparingLong".equals(methodName) && argumentCount == 1) {
                return java.util.Comparator.class.getMethod("thenComparingLong", java.util.function.ToLongFunction.class);
            }
            if ("thenComparingDouble".equals(methodName) && argumentCount == 1) {
                return java.util.Comparator.class.getMethod(
                        "thenComparingDouble",
                        java.util.function.ToDoubleFunction.class);
            }
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing JDK Comparator method: " + methodName, exception);
        }
        return null;
    }

    private Method findJavaCollectionTypedToArrayMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        if (ownerClass == null
                || arguments == null
                || arguments.size() != 1
                || !"toArray".equals(methodName)
                || !java.util.Collection.class.isAssignableFrom(ownerClass)
                || isJavaFunctionalExpression(arguments.get(0))) {
            return null;
        }
        try {
            return java.util.Collection.class.getMethod("toArray", Object[].class);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing java.util.Collection.toArray(Object[])", exception);
        }
    }

    private Class<?> resolveJavaClass(String ownerBinaryName) throws ClassNotFoundException {
        String hostRuntimeBinaryName = canonicalQinHostRuntimeBinaryName(ownerBinaryName);
        ownerBinaryName = hostRuntimeBinaryName == null
                ? canonicalJavaBinaryName(ownerBinaryName)
                : hostRuntimeBinaryName;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null) {
            return Class.forName(ownerBinaryName, false, contextClassLoader);
        }
        return Class.forName(ownerBinaryName);
    }

    private String canonicalJavaBinaryName(String ownerBinaryName) {
        if ("__QinJavaUtilRegexPattern".equals(ownerBinaryName)) {
            return "java.util.regex.Pattern";
        }
        return QinJavaSdkAliasSupport.canonicalBinaryName(ownerBinaryName);
    }

    private List<Method> publicMethodsInClassfileOrder(Class<?> ownerClass) {
        List<Method> methods = new ArrayList<>();
        Set<String> seen = new LinkedHashSet<>();
        appendDeclaredPublicMethodsInClassfileOrder(ownerClass, methods, seen);
        for (Class<?> current = ownerClass.getSuperclass(); current != null; current = current.getSuperclass()) {
            appendDeclaredPublicMethodsInClassfileOrder(current, methods, seen);
        }
        for (Method method : ownerClass.getMethods()) {
            if (seen.add(methodKey(method))) {
                methods.add(method);
            }
        }
        return List.copyOf(methods);
    }

    private void appendDeclaredPublicMethodsInClassfileOrder(
            Class<?> ownerClass,
            List<Method> methods,
            Set<String> seen) {
        Map<String, Method> declaredMethods = new LinkedHashMap<>();
        for (Method method : ownerClass.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                declaredMethods.put(methodKey(method), method);
            }
        }
        for (String methodKey : classfileDeclaredMethodKeys(ownerClass)) {
            Method method = declaredMethods.get(methodKey);
            if (method != null && seen.add(methodKey)) {
                methods.add(method);
            }
        }
        for (Method method : ownerClass.getDeclaredMethods()) {
            String methodKey = methodKey(method);
            if (Modifier.isPublic(method.getModifiers()) && seen.add(methodKey)) {
                methods.add(method);
            }
        }
    }

    private List<String> classfileDeclaredMethodKeys(Class<?> ownerClass) {
        String resourceName = "/" + ownerClass.getName().replace('.', '/') + ".class";
        try (InputStream input = ownerClass.getResourceAsStream(resourceName)) {
            if (input == null) {
                return List.of();
            }
            return parseClassfileDeclaredMethodKeys(input.readAllBytes());
        } catch (IOException | RuntimeException ignored) {
            return List.of();
        }
    }

    private List<String> parseClassfileDeclaredMethodKeys(byte[] bytes) {
        ClassfileCursor cursor = new ClassfileCursor(bytes);
        if (cursor.readU4() != 0xCAFEBABE) {
            return List.of();
        }
        cursor.skip(4);
        String[] utf8Constants = readClassfileUtf8Constants(cursor);
        cursor.skip(6);
        cursor.skip(2 * cursor.readU2());
        skipClassfileMembers(cursor);
        int methodCount = cursor.readU2();
        List<String> methodKeys = new ArrayList<>();
        for (int i = 0; i < methodCount; i++) {
            int access = cursor.readU2();
            String name = utf8Constants[cursor.readU2()];
            String descriptor = utf8Constants[cursor.readU2()];
            skipClassfileAttributes(cursor);
            if ((access & Modifier.PUBLIC) != 0 && name != null && descriptor != null) {
                methodKeys.add(name + descriptor);
            }
        }
        return List.copyOf(methodKeys);
    }

    private String[] readClassfileUtf8Constants(ClassfileCursor cursor) {
        int constantPoolCount = cursor.readU2();
        String[] utf8Constants = new String[constantPoolCount];
        for (int i = 1; i < constantPoolCount; i++) {
            int tag = cursor.readU1();
            switch (tag) {
                case 1 -> {
                    int length = cursor.readU2();
                    utf8Constants[i] = new String(cursor.bytes(), cursor.offset(), length, StandardCharsets.UTF_8);
                    cursor.skip(length);
                }
                case 3, 4 -> cursor.skip(4);
                case 5, 6 -> {
                    cursor.skip(8);
                    i++;
                }
                case 7, 8, 16, 19, 20 -> cursor.skip(2);
                case 9, 10, 11, 12, 17, 18 -> cursor.skip(4);
                case 15 -> cursor.skip(3);
                default -> throw new IllegalArgumentException("Unsupported classfile constant tag: " + tag);
            }
        }
        return utf8Constants;
    }

    private void skipClassfileMembers(ClassfileCursor cursor) {
        int fieldCount = cursor.readU2();
        for (int i = 0; i < fieldCount; i++) {
            cursor.skip(6);
            skipClassfileAttributes(cursor);
        }
    }

    private void skipClassfileAttributes(ClassfileCursor cursor) {
        int attributeCount = cursor.readU2();
        for (int i = 0; i < attributeCount; i++) {
            cursor.skip(2);
            cursor.skip(cursor.readU4());
        }
    }

    private boolean isStrictlyMoreSpecific(Class<?>[] candidateParameterTypes, Class<?>[] currentParameterTypes) {
        if (candidateParameterTypes.length != currentParameterTypes.length) {
            return false;
        }
        boolean strictlyMoreSpecific = false;
        for (int i = 0; i < candidateParameterTypes.length; i++) {
            Class<?> candidateType = candidateParameterTypes[i];
            Class<?> currentType = currentParameterTypes[i];
            if (candidateType == currentType) {
                continue;
            }
            if (candidateType.isPrimitive() || currentType.isPrimitive()) {
                return false;
            }
            if (!currentType.isAssignableFrom(candidateType)) {
                return false;
            }
            strictlyMoreSpecific = true;
        }
        return strictlyMoreSpecific;
    }

    private boolean hasNullArgument(List<QinCfaProgram.Expression> arguments) {
        for (QinCfaProgram.Expression argument : arguments) {
            if (argument instanceof QinCfaProgram.NullLiteral) {
                return true;
            }
        }
        return false;
    }

    private boolean isQinOwnedStaticFacadeOwner(String ownerBinaryName) {
        String canonicalName = canonicalJavaBinaryName(ownerBinaryName);
        return canonicalName.startsWith("com.qin.")
                || canonicalName.startsWith("com.slime.")
                || canonicalName.startsWith("com.subhuti.");
    }

    private String methodKey(Method method) {
        return method.getName()
                + MethodType.methodType(method.getReturnType(), method.getParameterTypes()).toMethodDescriptorString();
    }

    private int compatibilityScore(List<QinCfaProgram.Expression> arguments, Class<?>[] parameterTypes) {
        return compatibilityScore(arguments, parameterTypes, false);
    }

    private int compatibilityScore(
            List<QinCfaProgram.Expression> arguments,
            Class<?>[] parameterTypes,
            boolean varArgs) {
        if (varArgs) {
            return varArgsCompatibilityScore(arguments, parameterTypes);
        }
        if (arguments.size() == 1
                && arguments.get(0) instanceof QinCfaProgram.SpreadArgumentExpression) {
            return 20 + parameterTypes.length;
        }
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
        if (argument instanceof QinCfaProgram.BooleanLiteral) {
            if (parameterType == boolean.class) {
                return 0;
            }
            if (parameterType == Boolean.class) {
                return 1;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(Boolean.class)) {
                return 2;
            }
            return -1;
        }
        if (argument instanceof QinCfaProgram.NullLiteral) {
            return parameterType.isPrimitive() ? -1 : 0;
        }
        if (argument instanceof QinCfaProgram.NumberLiteral) {
            if (parameterType == Object.class) {
                return 3;
            }
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
        if (argument instanceof QinCfaProgram.ArrayLiteral arrayLiteral && parameterType.isArray()) {
            return arrayCompatibilityScore(arrayLiteral, parameterType.getComponentType());
        }
        if (argument instanceof QinCfaProgram.SpreadArgumentExpression) {
            return -1;
        }
        if (argument instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression
                && parameterType.isArray()
                && isArrayProducingBuiltinCall(builtinCallExpression)) {
            return 0;
        }
        if (argument instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression
                && parameterType == Object.class
                && resolveBuiltinMethod(builtinCallExpression).isPresent()) {
            return 4;
        }
        if (argument instanceof QinCfaProgram.ObjectLiteral) {
            if (parameterType.isPrimitive()) {
                return -1;
            }
            if (parameterType == LinkedHashMap.class) {
                return 0;
            }
            if (parameterType.isAssignableFrom(LinkedHashMap.class)) {
                return parameterType == Object.class ? 2 : 1;
            }
            return -1;
        }
        if (argument instanceof QinCfaProgram.IdentifierReference) {
            return parameterType.isPrimitive() ? -1 : (parameterType == Object.class ? 2 : 3);
        }
        if (isJavaFunctionalExpression(argument) && isJavaFunctionalInterface(parameterType)) {
            return 0;
        }
        Class<?> staticType = staticExpressionType(argument);
        if (staticType != null
                && !staticType.isPrimitive()
                && !parameterType.isPrimitive()
                && parameterType.isAssignableFrom(staticType)) {
            return staticType == parameterType ? 0 : 1;
        }
        return -1;
    }

    private int generatedLocalArgumentMatchScore(
            QinIrTypeRef parameterType,
            QinCfaProgram.Expression argument,
            Set<String> resolvingNames) {
        if (parameterType == null || argument == null) {
            return -1;
        }
        if (argument instanceof QinCfaProgram.NullLiteral) {
            return parameterType.kind() == QinIrTypeKind.CLASS || parameterType.kind() == QinIrTypeKind.STRING
                    ? 1
                    : -1;
        }
        QinIrTypeRef argumentType = staticIrTypeForExpression(argument, resolvingNames);
        if (argumentType == null) {
            Class<?> runtimeParameterType = runtimeClassForType(parameterType);
            int runtimeScore = compatibilityScore(argument, runtimeParameterType);
            return runtimeScore < 0 ? 20 : runtimeScore + 10;
        }
        return generatedLocalIrTypeMatchScore(parameterType, argumentType);
    }

    private int generatedLocalIrTypeMatchScore(QinIrTypeRef parameterType, QinIrTypeRef argumentType) {
        if (parameterType == null || argumentType == null) {
            return -1;
        }
        if (sameIrType(parameterType, argumentType)) {
            return 0;
        }
        if (parameterType.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object".equals(parameterType.binaryName())
                && isReferenceLikeIrType(argumentType)) {
            return 8;
        }
        if (parameterType.kind() == QinIrTypeKind.STRING && argumentType.kind() == QinIrTypeKind.STRING) {
            return 0;
        }
        if ((parameterType.kind() == QinIrTypeKind.DOUBLE || parameterType.kind() == QinIrTypeKind.INT)
                && isNumericLikeIrType(argumentType)) {
            return parameterType.kind() == argumentType.kind() ? 0 : 1;
        }
        if (parameterType.kind() == QinIrTypeKind.BOOLEAN && isBooleanLikeIrType(argumentType)) {
            return parameterType.kind() == argumentType.kind() ? 0 : 1;
        }
        if (parameterType.kind() == QinIrTypeKind.CLASS && argumentType.kind() == QinIrTypeKind.CLASS) {
            String parameterBinaryName = effectiveStaticReferenceBinaryName(parameterType.binaryName());
            String argumentBinaryName = effectiveStaticReferenceBinaryName(argumentType.binaryName());
            if (Objects.equals(parameterBinaryName, argumentBinaryName)) {
                return Objects.equals(parameterType.binaryName(), argumentType.binaryName()) ? 0 : 1;
            }
            if (isReflectedAssignable(argumentBinaryName, parameterBinaryName)) {
                return 4;
            }
        }
        return -1;
    }

    private QinIrTypeRef staticIrTypeForExpression(
            QinCfaProgram.Expression expression,
            Set<String> resolvingNames) {
        if (expression instanceof QinCfaProgram.StringLiteral) {
            return QinIrTypeRef.stringType();
        }
        if (expression instanceof QinCfaProgram.BooleanLiteral) {
            return QinIrTypeRef.booleanType();
        }
        if (expression instanceof QinCfaProgram.NumberLiteral) {
            return QinIrTypeRef.doubleType();
        }
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            if (!resolvingNames.add(identifierReference.name())) {
                return null;
            }
            DeclarationBinding binding = allDeclarationBindings.get(identifierReference.name());
            QinIrTypeRef result = binding == null
                    ? null
                    : staticIrTypeForExpression(binding.initializer(), resolvingNames);
            resolvingNames.remove(identifierReference.name());
            return result;
        }
        if (expression instanceof QinCfaProgram.JavaNewExpression javaNewExpression) {
            return QinIrTypeRef.classType(effectiveStaticReferenceBinaryName(
                    canonicalJavaBinaryName(javaNewExpression.ownerBinaryName())));
        }
        if (expression instanceof QinCfaProgram.StaticMethodCallExpression staticMethodCallExpression) {
            QinIrMethodDeclaration method = selectedGeneratedStaticMethodOrNull(
                    staticMethodCallExpression.ownerBinaryName(),
                    staticMethodCallExpression.methodName(),
                    staticMethodCallExpression.arguments());
            if (method != null) {
                return method.returnType();
            }
        }
        return null;
    }

    private QinIrMethodDeclaration selectedGeneratedStaticMethodOrNull(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        List<QinIrClassDeclaration> ownerCandidates = generatedStaticOwnerCandidates(ownerBinaryName);
        if (ownerCandidates.isEmpty()) {
            return null;
        }
        GeneratedMethodSelection selected =
                selectGeneratedStaticMethod(ownerBinaryName, methodName, arguments, ownerCandidates, false);
        return selected == null ? null : selected.method();
    }

    private boolean sameIrType(QinIrTypeRef left, QinIrTypeRef right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null || left.kind() != right.kind()) {
            return false;
        }
        return switch (left.kind()) {
            case CLASS, STRING -> Objects.equals(left.binaryName(), right.binaryName());
            default -> true;
        };
    }

    private boolean isReferenceLikeIrType(QinIrTypeRef type) {
        return type != null && (type.kind() == QinIrTypeKind.CLASS || type.kind() == QinIrTypeKind.STRING);
    }

    private boolean isNumericLikeIrType(QinIrTypeRef type) {
        if (type == null) {
            return false;
        }
        if (type.kind() == QinIrTypeKind.INT || type.kind() == QinIrTypeKind.DOUBLE) {
            return true;
        }
        return type.kind() == QinIrTypeKind.CLASS
                && (isBoxedNumericBinaryName(type.binaryName()) || "java.lang.Number".equals(type.binaryName()));
    }

    private boolean isBooleanLikeIrType(QinIrTypeRef type) {
        return type != null
                && (type.kind() == QinIrTypeKind.BOOLEAN
                        || (type.kind() == QinIrTypeKind.CLASS
                                && "java.lang.Boolean".equals(type.binaryName())));
    }

    private boolean isBoxedNumericBinaryName(String binaryName) {
        return "java.lang.Byte".equals(binaryName)
                || "java.lang.Short".equals(binaryName)
                || "java.lang.Integer".equals(binaryName)
                || "java.lang.Long".equals(binaryName)
                || "java.lang.Float".equals(binaryName)
                || "java.lang.Double".equals(binaryName);
    }

    private boolean isReflectedAssignable(String argumentBinaryName, String parameterBinaryName) {
        if (argumentBinaryName == null || parameterBinaryName == null) {
            return false;
        }
        try {
            Class<?> argumentClass = resolveJavaClass(argumentBinaryName);
            Class<?> parameterClass = resolveJavaClass(parameterBinaryName);
            return parameterClass.isAssignableFrom(argumentClass);
        } catch (ClassNotFoundException | LinkageError ignored) {
            return false;
        }
    }

    private QinIrTypeRef varargsElementType(QinIrTypeRef arrayType) {
        if (arrayType != null
                && arrayType.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object[]".equals(arrayType.binaryName())) {
            if (arrayType.typeArguments() != null && !arrayType.typeArguments().isEmpty()) {
                return arrayType.typeArguments().get(0);
            }
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (arrayType != null
                && arrayType.kind() == QinIrTypeKind.CLASS
                && arrayType.binaryName() != null) {
            String binaryName = arrayType.binaryName();
            if (binaryName.endsWith("[]")) {
                return arrayElementTypeFromBracketName(binaryName);
            }
            if (binaryName.startsWith("[")) {
                return arrayElementTypeFromDescriptor(binaryName);
            }
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef arrayElementTypeFromBracketName(String binaryName) {
        String componentName = binaryName.substring(0, binaryName.length() - 2);
        if (componentName.endsWith("[]")) {
            return QinIrTypeRef.classType(componentName);
        }
        return switch (componentName) {
            case "boolean" -> QinIrTypeRef.booleanType();
            case "int" -> QinIrTypeRef.intType();
            case "double", "number" -> QinIrTypeRef.doubleType();
            case "java.lang.String", "String" -> QinIrTypeRef.stringType();
            default -> QinIrTypeRef.classType(componentName);
        };
    }

    private QinIrTypeRef arrayElementTypeFromDescriptor(String descriptor) {
        if (descriptor.length() < 2) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        String componentDescriptor = descriptor.substring(1);
        if (componentDescriptor.startsWith("[")) {
            return QinIrTypeRef.classType(componentDescriptor);
        }
        return switch (componentDescriptor.charAt(0)) {
            case 'Z' -> QinIrTypeRef.booleanType();
            case 'I' -> QinIrTypeRef.intType();
            case 'D' -> QinIrTypeRef.doubleType();
            case 'B' -> QinIrTypeRef.classType("java.lang.Byte");
            case 'C' -> QinIrTypeRef.classType("java.lang.Character");
            case 'F' -> QinIrTypeRef.classType("java.lang.Float");
            case 'J' -> QinIrTypeRef.classType("java.lang.Long");
            case 'S' -> QinIrTypeRef.classType("java.lang.Short");
            case 'L' -> {
                int end = componentDescriptor.endsWith(";")
                        ? componentDescriptor.length() - 1
                        : componentDescriptor.length();
                String componentName = componentDescriptor.substring(1, end).replace('/', '.');
                yield "java.lang.String".equals(componentName)
                        ? QinIrTypeRef.stringType()
                        : QinIrTypeRef.classType(componentName);
            }
            default -> QinIrTypeRef.classType("java.lang.Object");
        };
    }

    private Class<?> staticExpressionType(QinCfaProgram.Expression expression) {
        return staticExpressionType(expression, Map.of());
    }

    private Class<?> staticExpressionType(
            QinCfaProgram.Expression expression,
            Map<String, LocalBinding> localBindings) {
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            LocalBinding localBinding = localBindings.get(identifierReference.name());
            return localBinding == null ? null : localBinding.staticType();
        }
        if (expression instanceof QinCfaProgram.JavaNewExpression javaNewExpression) {
            try {
                return resolveJavaClass(javaNewExpression.ownerBinaryName());
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        if (expression instanceof QinCfaProgram.StaticMethodCallExpression staticMethodCallExpression) {
            try {
                return resolveStaticMethod(
                        staticMethodCallExpression.ownerBinaryName(),
                        staticMethodCallExpression.methodName(),
                        staticMethodCallExpression.arguments()).returnType();
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        if (expression instanceof QinCfaProgram.JavaInstanceMethodCallExpression instanceMethodCallExpression) {
            try {
                return resolveInstanceMethod(
                        instanceMethodCallExpression.ownerBinaryName(),
                        instanceMethodCallExpression.methodName(),
                        instanceMethodCallExpression.arguments()).returnType();
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
            Class<?> arrayType = staticArrayProducingBuiltinReturnType(builtinCallExpression, localBindings);
            if (arrayType != null) {
                return arrayType;
            }
            Class<?> staticFacadeReturnType = staticJavaSdkFacadeBuiltinReturnType(builtinCallExpression);
            if (staticFacadeReturnType != null) {
                return staticFacadeReturnType;
            }
            StaticCallMethod staticCallMethod = staticCallMethodOrNull(builtinCallExpression);
            if (staticCallMethod != null) {
                try {
                    return resolveInstanceMethod(
                            staticCallMethod.ownerClass().getName(),
                            staticCallMethod.methodName(),
                            staticCallMethod.arguments()).returnType();
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
            return builtinMethodReturnType(builtinCallExpression).orElse(null);
        }
        return null;
    }

    private Class<?> staticArrayProducingBuiltinReturnType(
            QinCfaProgram.BuiltinCallExpression builtinCallExpression,
            Map<String, LocalBinding> localBindings) {
        Class<?> streamArrayType = staticStreamToArrayIntFunctionArrayType(builtinCallExpression, localBindings);
        if (streamArrayType != null) {
            return streamArrayType;
        }
        if ("Array".equals(builtinCallExpression.receiverName())
                && "from".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2
                && builtinCallExpression.arguments().get(0) instanceof QinCfaProgram.ObjectLiteral source
                && staticLengthProperty(source) != null) {
            QinCfaProgram.Expression factoryReturnExpression =
                    staticArrayFactoryReturnExpression(builtinCallExpression.arguments().get(1));
            Class<?> componentType = staticArrayFactoryComponentType(factoryReturnExpression);
            return componentType == null ? null : Array.newInstance(componentType, 0).getClass();
        }
        QinCfaProgram.BuiltinCallExpression arrayFromCall = arrayFromBuiltinCallOrNull(builtinCallExpression);
        if (arrayFromCall != null
                && arrayFromCall.arguments().get(0) instanceof QinCfaProgram.ObjectLiteral source
                && staticLengthProperty(source) != null) {
            QinCfaProgram.Expression factoryReturnExpression =
                    staticArrayFactoryReturnExpression(arrayFromCall.arguments().get(1));
            Class<?> componentType = staticArrayFactoryComponentType(factoryReturnExpression);
            return componentType == null ? null : Array.newInstance(componentType, 0).getClass();
        }
        if (isJavaArraysCopyBuiltinCall(builtinCallExpression)
                && !builtinCallExpression.arguments().isEmpty()) {
            Class<?> sourceType = staticExpressionType(builtinCallExpression.arguments().get(0), localBindings);
            return sourceType != null && sourceType.isArray() ? sourceType : null;
        }
        return null;
    }

    private QinCfaProgram.Expression staticArrayFactoryReturnExpression(QinCfaProgram.Expression expression) {
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            return functionLiteral.returnExpression();
        }
        if (!(expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression)
                || !"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_make_function__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 1
                || !(builtinCallExpression.arguments().get(0) instanceof QinCfaProgram.ObjectLiteral definition)) {
            return null;
        }
        QinCfaProgram.Expression valueShape = objectPropertyValue(definition, "__qin_function_value_shape");
        if (!(valueShape instanceof QinCfaProgram.StringLiteral valueShapeLiteral)
                || !"value".equals(valueShapeLiteral.value())) {
            return null;
        }
        QinCfaProgram.Expression constantReturn = objectPropertyValue(definition, "__qin_function_constant_return");
        if (constantReturn != null) {
            return constantReturn;
        }
        QinCfaProgram.Expression ast = objectPropertyValue(definition, "ast");
        if (!(ast instanceof QinCfaProgram.ObjectLiteral astObject)
                || !"ArrowFunctionExpression".equals(objectStringProperty(astObject, "type"))
                || !(objectPropertyValue(astObject, "expression") instanceof QinCfaProgram.BooleanLiteral expressionFlag)
                || !expressionFlag.value()) {
            return null;
        }
        QinCfaProgram.Expression body = objectPropertyValue(astObject, "body");
        if (!(body instanceof QinCfaProgram.ObjectLiteral bodyObject)
                || !"Literal".equals(objectStringProperty(bodyObject, "type"))) {
            return null;
        }
        return objectPropertyValue(bodyObject, "value");
    }

    private Class<?> staticStreamToArrayIntFunctionArrayType(
            QinCfaProgram.BuiltinCallExpression builtinCallExpression,
            Map<String, LocalBinding> localBindings) {
        if (!"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_call_method__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 3
                || !(builtinCallExpression.arguments().get(1) instanceof QinCfaProgram.StringLiteral methodName)
                || !"toArray".equals(methodName.value())) {
            return null;
        }
        Class<?> receiverType = staticExpressionType(builtinCallExpression.arguments().get(0), localBindings);
        if (receiverType == null || !java.util.stream.Stream.class.isAssignableFrom(receiverType)) {
            return null;
        }
        String descriptor = staticJavaArrayConstructorFunctionDescriptor(builtinCallExpression.arguments().get(2));
        return descriptor == null ? null : javaArrayClassForDescriptor(descriptor);
    }

    private Class<?> javaArrayClassForDescriptor(String arrayType) {
        if (arrayType == null || arrayType.isBlank()) {
            return null;
        }
        String trimmed = arrayType.trim();
        if (trimmed.startsWith("[")) {
            try {
                return Class.forName(trimmed, false, QinCfaJvmClassFileBackend.class.getClassLoader());
            } catch (ClassNotFoundException exception) {
                return null;
            }
        }
        int dimensions = 0;
        String componentName = trimmed;
        while (componentName.endsWith("[]")) {
            dimensions++;
            componentName = componentName.substring(0, componentName.length() - 2);
        }
        if (dimensions == 0) {
            return null;
        }
        Class<?> componentType = switch (componentName) {
            case "boolean" -> boolean.class;
            case "byte" -> byte.class;
            case "short" -> short.class;
            case "char" -> char.class;
            case "int" -> int.class;
            case "long" -> long.class;
            case "float" -> float.class;
            case "double" -> double.class;
            default -> {
                try {
                    yield resolveJavaClass(componentName);
                } catch (ClassNotFoundException exception) {
                    yield null;
                }
            }
        };
        return componentType == null
                ? null
                : Array.newInstance(componentType, new int[dimensions]).getClass();
    }

    private String staticJavaArrayConstructorFunctionDescriptor(QinCfaProgram.Expression expression) {
        if (expression instanceof QinCfaProgram.FunctionLiteral functionLiteral) {
            return staticJavaArrayConstructorReturnDescriptor(
                    functionLiteral.returnExpression(),
                    Set.copyOf(functionLiteral.parameterNames()));
        }
        if (!(expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression)
                || !"Global".equals(builtinCallExpression.receiverName())) {
            return null;
        }
        if ("__qin_call__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2
                && "__qin_java_functional".equals(
                        staticGlobalIdentifierNameOrNull(builtinCallExpression.arguments().get(0)))) {
            return staticJavaArrayConstructorFunctionDescriptor(builtinCallExpression.arguments().get(1));
        }
        if ("__qin_java_functional".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            return staticJavaArrayConstructorFunctionDescriptor(builtinCallExpression.arguments().get(0));
        }
        if (!"__qin_make_function__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 1
                || !(builtinCallExpression.arguments().get(0) instanceof QinCfaProgram.ObjectLiteral definition)) {
            return null;
        }
        QinCfaProgram.Expression ast = objectPropertyValue(definition, "ast");
        if (!(ast instanceof QinCfaProgram.ObjectLiteral astObject)
                || !"ArrowFunctionExpression".equals(objectStringProperty(astObject, "type"))) {
            return null;
        }
        Set<String> parameterNames = astFunctionParameterNames(astObject);
        if (parameterNames.isEmpty()) {
            return null;
        }
        return staticJavaArrayConstructorAstCallDescriptor(
                objectPropertyValue(astObject, "body"),
                parameterNames);
    }

    private String staticJavaArrayConstructorReturnDescriptor(
            QinCfaProgram.Expression expression,
            Set<String> parameterNames) {
        if (parameterNames == null || parameterNames.isEmpty()) {
            return null;
        }
        if (!(expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression)
                || !"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_java_new_array__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 2
                || !(builtinCallExpression.arguments().get(0) instanceof QinCfaProgram.StringLiteral descriptor)
                || !expressionReferencesAnyIdentifier(builtinCallExpression.arguments().get(1), parameterNames)) {
            return null;
        }
        return descriptor.value();
    }

    private Set<String> astFunctionParameterNames(QinCfaProgram.ObjectLiteral functionAst) {
        QinCfaProgram.Expression params = objectPropertyValue(functionAst, "params");
        if (!(params instanceof QinCfaProgram.ArrayLiteral arrayLiteral)) {
            return Set.of();
        }
        Set<String> names = new LinkedHashSet<>();
        for (QinCfaProgram.Expression param : arrayLiteral.elements()) {
            String name = astBindingName(param);
            if (name != null) {
                names.add(name);
            }
        }
        return Set.copyOf(names);
    }

    private String astBindingName(QinCfaProgram.Expression expression) {
        if (!(expression instanceof QinCfaProgram.ObjectLiteral objectLiteral)) {
            return null;
        }
        String type = objectStringProperty(objectLiteral, "type");
        if ("Identifier".equals(type)) {
            return objectStringProperty(objectLiteral, "name");
        }
        if ("RestElement".equals(type)) {
            return astBindingName(objectPropertyValue(objectLiteral, "argument"));
        }
        return null;
    }

    private String staticJavaArrayConstructorAstCallDescriptor(
            QinCfaProgram.Expression expression,
            Set<String> parameterNames) {
        if (parameterNames == null
                || parameterNames.isEmpty()
                || !(expression instanceof QinCfaProgram.ObjectLiteral objectLiteral)
                || !"CallExpression".equals(objectStringProperty(objectLiteral, "type"))
                || !"__qin_java_new_array__".equals(astCalleeName(objectPropertyValue(objectLiteral, "callee")))
                || !(objectPropertyValue(objectLiteral, "arguments") instanceof QinCfaProgram.ArrayLiteral arguments)
                || arguments.elements().size() != 2) {
            return null;
        }
        String descriptor = astLiteralString(arguments.elements().get(0));
        if (descriptor == null || !astExpressionReferencesAnyIdentifier(arguments.elements().get(1), parameterNames)) {
            return null;
        }
        return descriptor;
    }

    private String astCalleeName(QinCfaProgram.Expression expression) {
        if (!(expression instanceof QinCfaProgram.ObjectLiteral objectLiteral)) {
            return null;
        }
        String type = objectStringProperty(objectLiteral, "type");
        if ("Identifier".equals(type)) {
            return objectStringProperty(objectLiteral, "name");
        }
        if ("MemberExpression".equals(type)) {
            return astCalleeName(objectPropertyValue(objectLiteral, "property"));
        }
        return null;
    }

    private String astLiteralString(QinCfaProgram.Expression expression) {
        if (!(expression instanceof QinCfaProgram.ObjectLiteral objectLiteral)
                || !"Literal".equals(objectStringProperty(objectLiteral, "type"))) {
            return null;
        }
        QinCfaProgram.Expression value = objectPropertyValue(objectLiteral, "value");
        return value instanceof QinCfaProgram.StringLiteral stringLiteral ? stringLiteral.value() : null;
    }

    private boolean expressionReferencesAnyIdentifier(
            QinCfaProgram.Expression expression,
            Set<String> names) {
        if (expression == null || names == null || names.isEmpty()) {
            return false;
        }
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            return names.contains(identifierReference.name());
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
            for (QinCfaProgram.Expression argument : builtinCallExpression.arguments()) {
                if (expressionReferencesAnyIdentifier(argument, names)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
                if (expressionReferencesAnyIdentifier(property.value(), names)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            for (QinCfaProgram.Expression element : arrayLiteral.elements()) {
                if (expressionReferencesAnyIdentifier(element, names)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinCfaProgram.MemberAccessExpression memberAccessExpression) {
            return names.contains(memberAccessExpression.objectName());
        }
        if (expression instanceof QinCfaProgram.SequenceExpression sequenceExpression) {
            for (QinCfaProgram.Expression leadingExpression : sequenceExpression.leadingExpressions()) {
                if (expressionReferencesAnyIdentifier(leadingExpression, names)) {
                    return true;
                }
            }
            return expressionReferencesAnyIdentifier(sequenceExpression.resultExpression(), names);
        }
        if (expression instanceof QinCfaProgram.LetExpression letExpression) {
            for (QinCfaProgram.LocalVariableDeclaration declaration : letExpression.localDeclarations()) {
                if (expressionReferencesAnyIdentifier(declaration.initializer(), names)) {
                    return true;
                }
            }
            for (QinCfaProgram.Expression leadingExpression : letExpression.leadingExpressions()) {
                if (expressionReferencesAnyIdentifier(leadingExpression, names)) {
                    return true;
                }
            }
            return expressionReferencesAnyIdentifier(letExpression.resultExpression(), names);
        }
        return false;
    }

    private boolean astExpressionReferencesAnyIdentifier(
            QinCfaProgram.Expression expression,
            Set<String> names) {
        if (expression == null || names == null || names.isEmpty()) {
            return false;
        }
        if (expression instanceof QinCfaProgram.ObjectLiteral objectLiteral) {
            if ("Identifier".equals(objectStringProperty(objectLiteral, "type"))
                    && names.contains(objectStringProperty(objectLiteral, "name"))) {
                return true;
            }
            for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
                if (astExpressionReferencesAnyIdentifier(property.value(), names)) {
                    return true;
                }
            }
            return false;
        }
        if (expression instanceof QinCfaProgram.ArrayLiteral arrayLiteral) {
            for (QinCfaProgram.Expression element : arrayLiteral.elements()) {
                if (astExpressionReferencesAnyIdentifier(element, names)) {
                    return true;
                }
            }
        }
        return false;
    }

    private QinCfaProgram.Expression objectPropertyValue(QinCfaProgram.ObjectLiteral objectLiteral, String key) {
        if (objectLiteral == null || key == null) {
            return null;
        }
        for (QinCfaProgram.ObjectProperty property : objectLiteral.properties()) {
            if (key.equals(property.key())) {
                return property.value();
            }
        }
        return null;
    }

    private String objectStringProperty(QinCfaProgram.ObjectLiteral objectLiteral, String key) {
        QinCfaProgram.Expression value = objectPropertyValue(objectLiteral, key);
        return value instanceof QinCfaProgram.StringLiteral stringLiteral ? stringLiteral.value() : null;
    }

    private Class<?> staticArrayFactoryComponentType(QinCfaProgram.Expression expression) {
        return staticArrayFactoryComponentType(expression, Map.of());
    }

    private Class<?> staticArrayFactoryComponentType(
            QinCfaProgram.Expression expression,
            Map<String, LocalBinding> localBindings) {
        if (expression instanceof QinCfaProgram.NullLiteral) {
            return Object.class;
        }
        if (expression instanceof QinCfaProgram.BooleanLiteral) {
            return boolean.class;
        }
        if (expression instanceof QinCfaProgram.StringLiteral) {
            return String.class;
        }
        if (expression instanceof QinCfaProgram.NumberLiteral numberLiteral) {
            return numberLiteral.value() == Math.rint(numberLiteral.value()) ? int.class : double.class;
        }
        Class<?> staticType = staticExpressionType(expression, localBindings);
        return staticType == null || staticType.isPrimitive() ? null : staticType;
    }

    private StaticCallMethod staticCallMethodOrNull(QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (!"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_call_method__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() < 2
                || !(builtinCallExpression.arguments().get(1) instanceof QinCfaProgram.StringLiteral methodLiteral)) {
            return null;
        }
        QinCfaProgram.Expression receiver = builtinCallExpression.arguments().get(0);
        Class<?> ownerClass = staticExpressionType(receiver);
        if (ownerClass == null || ownerClass.isPrimitive() || ownerClass == Object.class) {
            return null;
        }
        return new StaticCallMethod(
                receiver,
                ownerClass,
                methodLiteral.value(),
                List.copyOf(builtinCallExpression.arguments().subList(2, builtinCallExpression.arguments().size())));
    }

    private Class<?> staticJavaSdkFacadeBuiltinReturnType(QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if ("__QinJavaUtilArrays".equals(builtinCallExpression.receiverName())
                && "stream".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            return java.util.stream.Stream.class;
        }
        return null;
    }

    private Optional<Class<?>> builtinMethodReturnType(QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        Optional<QinBuiltinRegistry.BuiltinMethod> method = resolveBuiltinMethod(builtinCallExpression);
        if (method.isEmpty()) {
            return Optional.empty();
        }
        try {
            MethodType methodType = MethodType.fromMethodDescriptorString(
                    method.get().descriptor().descriptorString(),
                    QinCfaJvmClassFileBackend.class.getClassLoader());
            Class<?> returnType = methodType.returnType();
            return returnType == Object.class ? Optional.empty() : Optional.of(returnType);
        } catch (IllegalArgumentException | TypeNotPresentException exception) {
            return Optional.empty();
        }
    }

    private boolean isJavaFunctionalExpression(QinCfaProgram.Expression expression) {
        if (expression instanceof QinCfaProgram.FunctionLiteral) {
            return true;
        }
        if (!(expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression)) {
            return false;
        }
        if (!"Global".equals(builtinCallExpression.receiverName())) {
            return false;
        }
        if ("__qin_call__".equals(builtinCallExpression.methodName())) {
            return builtinCallExpression.arguments().size() == 2
                    && "__qin_java_functional".equals(
                            staticGlobalIdentifierNameOrNull(builtinCallExpression.arguments().get(0)))
                    && isJavaFunctionalExpression(builtinCallExpression.arguments().get(1));
        }
        return "__qin_make_function__".equals(builtinCallExpression.methodName())
                || "__qin_make_function_bound__".equals(builtinCallExpression.methodName())
                || "__qin_make_function_with_lexical_this__".equals(builtinCallExpression.methodName())
                || "__qin_java_functional".equals(builtinCallExpression.methodName())
                || "__qin_direct_method_function__".equals(builtinCallExpression.methodName());
    }

    private boolean isJavaFunctionalInterface(Class<?> type) {
        return functionalInterfaceMethod(type) != null;
    }

    private Method functionalInterfaceMethod(Class<?> type) {
        if (type == null || !type.isInterface()) {
            return null;
        }
        Method functionalMethod = null;
        for (Method method : type.getMethods()) {
            if (method.getDeclaringClass() == Object.class
                    || method.isDefault()
                    || Modifier.isStatic(method.getModifiers())
                    || !Modifier.isAbstract(method.getModifiers())) {
                continue;
            }
            if (functionalMethod != null
                    && !functionalMethod.getName().equals(method.getName())) {
                return null;
            }
            functionalMethod = method;
        }
        return functionalMethod;
    }

    private int arrayCompatibilityScore(QinCfaProgram.ArrayLiteral arrayLiteral, Class<?> componentType) {
        if (componentType == null) {
            return -1;
        }
        int total = 0;
        for (QinCfaProgram.Expression element : arrayLiteral.elements()) {
            int score = compatibilityScore(element, componentType);
            if (score < 0) {
                return -1;
            }
            total += score;
        }
        return total;
    }

    private GeneratedMethodSelection selectGeneratedStaticMethod(
            String ownerBinaryName,
            String methodName,
            List<QinCfaProgram.Expression> arguments,
            List<QinIrClassDeclaration> ownerCandidates,
            boolean throwOnAmbiguous) {
        GeneratedMethodSelection selected = null;
        int bestScore = Integer.MAX_VALUE;
        for (QinIrClassDeclaration ownerCandidate : ownerCandidates) {
            if (ownerCandidate == null) {
                continue;
            }
            int ownerScore = generatedStaticOwnerAliasScore(ownerBinaryName, ownerCandidate);
            for (QinIrMethodDeclaration method : ownerCandidate.methods()) {
                if (!method.staticMethod() || !method.name().equals(methodName)) {
                    continue;
                }
                int methodScore = generatedLocalMethodMatchScore(method, arguments);
                if (methodScore < 0) {
                    continue;
                }
                int score = ownerScore * 100_000 + methodScore;
                if (score < bestScore) {
                    selected = new GeneratedMethodSelection(ownerCandidate, method);
                    bestScore = score;
                    continue;
                }
                if (score == bestScore) {
                    if (throwOnAmbiguous) {
                        throw new IllegalArgumentException(
                                "Ambiguous generated static method: " + ownerBinaryName + "." + methodName);
                    }
                    return null;
                }
            }
        }
        return selected;
    }

    private List<QinIrClassDeclaration> generatedStaticOwnerCandidates(String ownerBinaryName) {
        if (ownerBinaryName == null || ownerBinaryName.isBlank()
                || staticDeclarationIndex == null || staticDeclarationIndex.isEmpty()) {
            return List.of();
        }
        if (isQinHostRuntimeBinaryName(ownerBinaryName)) {
            return List.of();
        }
        Map<String, QinIrClassDeclaration> candidates = new LinkedHashMap<>();
        addGeneratedStaticOwnerCandidate(candidates, staticDeclarationIndex.get(ownerBinaryName));
        addGeneratedStaticOwnerCandidate(candidates, lookupStaticDeclaration(ownerBinaryName));
        addGeneratedStaticOwnerCandidate(candidates, staticDeclarationIndex.get(flattenedBinaryAlias(ownerBinaryName)));
        addGeneratedStaticOwnerCandidate(candidates, staticDeclarationIndex.get(canonicalJavaBinaryName(ownerBinaryName)));
        for (QinIrClassDeclaration declaration : staticDeclarationIndex.values()) {
            if (generatedStaticOwnerAliasScore(ownerBinaryName, declaration) < 100) {
                addGeneratedStaticOwnerCandidate(candidates, declaration);
            }
        }
        List<QinIrClassDeclaration> sorted = new ArrayList<>(candidates.values());
        sorted.sort((left, right) -> {
            int leftScore = generatedStaticOwnerAliasScore(ownerBinaryName, left);
            int rightScore = generatedStaticOwnerAliasScore(ownerBinaryName, right);
            if (leftScore != rightScore) {
                return Integer.compare(leftScore, rightScore);
            }
            return safeBinaryName(left).compareTo(safeBinaryName(right));
        });
        return List.copyOf(sorted);
    }

    private void addGeneratedStaticOwnerCandidate(
            Map<String, QinIrClassDeclaration> candidates,
            QinIrClassDeclaration declaration) {
        if (candidates == null
                || declaration == null
                || declaration.binaryName() == null
                || declaration.binaryName().isBlank()) {
            return;
        }
        candidates.putIfAbsent(declaration.binaryName(), declaration);
    }

    private int generatedStaticOwnerAliasScore(String ownerBinaryName, QinIrClassDeclaration declaration) {
        if (ownerBinaryName == null
                || ownerBinaryName.isBlank()
                || declaration == null
                || declaration.binaryName() == null
                || declaration.binaryName().isBlank()) {
            return 100;
        }
        String declarationBinaryName = declaration.binaryName();
        if (ownerBinaryName.equals(declarationBinaryName)) {
            return 0;
        }
        if (ownerBinaryName.equals(declaration.simpleName())) {
            return 1;
        }
        if (ownerBinaryName.equals(flattenedBinaryAlias(declarationBinaryName))) {
            return 2;
        }
        String originalBinaryName = inferredOriginalJavaBinaryName(declarationBinaryName);
        if (originalBinaryName != null && ownerBinaryName.equals(simpleBinaryName(originalBinaryName))) {
            return 3;
        }
        String canonicalOwner = canonicalJavaBinaryName(ownerBinaryName);
        if (canonicalOwner != null && canonicalOwner.equals(declarationBinaryName)) {
            return 4;
        }
        return 100;
    }

    private String safeBinaryName(QinIrClassDeclaration declaration) {
        return declaration == null || declaration.binaryName() == null ? "" : declaration.binaryName();
    }

    private int varArgsCompatibilityScore(List<QinCfaProgram.Expression> arguments, Class<?>[] parameterTypes) {
        if (parameterTypes.length == 0) {
            return -1;
        }
        int fixedCount = parameterTypes.length - 1;
        if (arguments.size() < fixedCount || !parameterTypes[fixedCount].isArray()) {
            return -1;
        }
        int total = 10;
        for (int i = 0; i < fixedCount; i++) {
            int parameterScore = compatibilityScore(arguments.get(i), parameterTypes[i]);
            if (parameterScore < 0) {
                return -1;
            }
            total += parameterScore;
        }
        Class<?> arrayType = parameterTypes[fixedCount];
        if (arguments.size() == parameterTypes.length) {
            int arrayScore = compatibilityScore(arguments.get(fixedCount), arrayType);
            if (arrayScore >= 0) {
                return total + arrayScore;
            }
        }
        if (arguments.size() == parameterTypes.length
                && arguments.get(fixedCount) instanceof QinCfaProgram.SpreadArgumentExpression) {
            return total + 1;
        }
        Class<?> componentType = arrayType.getComponentType();
        for (int i = fixedCount; i < arguments.size(); i++) {
            int parameterScore = compatibilityScore(arguments.get(i), componentType);
            if (parameterScore < 0) {
                return -1;
            }
            total += parameterScore;
        }
        return total;
    }

    private boolean isArrayProducingBuiltinCall(QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if (arrayFromBuiltinCallOrNull(builtinCallExpression) != null) {
            return true;
        }
        return isJavaArraysCopyBuiltinCall(builtinCallExpression);
    }

    private QinCfaProgram.BuiltinCallExpression arrayFromBuiltinCallOrNull(
            QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        if ("Array".equals(builtinCallExpression.receiverName())
                && "from".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            return builtinCallExpression;
        }
        if (!"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_call_method__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 4
                || !"Array".equals(staticGlobalIdentifierNameOrNull(builtinCallExpression.arguments().get(0)))
                || !(builtinCallExpression.arguments().get(1) instanceof QinCfaProgram.StringLiteral methodName)
                || !"from".equals(methodName.value())) {
            return null;
        }
        return new QinCfaProgram.BuiltinCallExpression(
                "Array",
                "from",
                List.of(
                        builtinCallExpression.arguments().get(2),
                        builtinCallExpression.arguments().get(3)));
    }

    private String staticGlobalIdentifierNameOrNull(QinCfaProgram.Expression expression) {
        if (expression instanceof QinCfaProgram.IdentifierReference identifierReference) {
            return identifierReference.name();
        }
        if (expression instanceof QinCfaProgram.BuiltinCallExpression builtinCallExpression
                && "Global".equals(builtinCallExpression.receiverName())
                && "__qin_global__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1
                && builtinCallExpression.arguments().get(0) instanceof QinCfaProgram.StringLiteral nameLiteral) {
            return nameLiteral.value();
        }
        return null;
    }

    private boolean isJavaArraysCopyBuiltinCall(QinCfaProgram.BuiltinCallExpression builtinCallExpression) {
        String receiverName = builtinCallExpression.receiverName();
        if (receiverName == null || !receiverName.endsWith("Arrays")) {
            return false;
        }
        return "copyOf".equals(builtinCallExpression.methodName())
                || "copyOfRange".equals(builtinCallExpression.methodName());
    }

    private Map<String, QinIrClassDeclaration> normalizeStaticDeclarationIndex(
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declarationIndex == null || declarationIndex.isEmpty()) {
            return Map.of();
        }
        Map<String, QinIrClassDeclaration> normalized = new LinkedHashMap<>();
        for (Map.Entry<String, QinIrClassDeclaration> entry : declarationIndex.entrySet()) {
            putStaticDeclarationIndex(normalized, entry.getKey(), entry.getValue());
        }
        for (QinIrClassDeclaration declaration : declarationIndex.values()) {
            if (declaration == null) {
                continue;
            }
            putStaticDeclarationIndex(normalized, declaration.binaryName(), declaration);
            putStaticDeclarationIndex(normalized, flattenedBinaryAlias(declaration.binaryName()), declaration);
            putStaticDeclarationIndex(normalized, declaration.simpleName(), declaration);
        }
        return normalized.isEmpty() ? Map.of() : Map.copyOf(normalized);
    }

    private void putStaticDeclarationIndex(
            Map<String, QinIrClassDeclaration> index,
            String key,
            QinIrClassDeclaration declaration) {
        if (index == null || key == null || key.isBlank() || declaration == null) {
            return;
        }
        putStaticDeclarationIndexEntry(index, key, declaration);
        String flattened = flattenedBinaryAlias(key);
        if (flattened != null && !flattened.isBlank()) {
            putStaticDeclarationIndexEntry(index, flattened, declaration);
        }
        if (key.startsWith("__Qin") || declaration.simpleName().startsWith("__Qin")) {
            return;
        }
        String canonical = canonicalJavaBinaryName(key);
        if (canonical != null && !canonical.isBlank()) {
            putStaticDeclarationIndexEntry(index, canonical, declaration);
        }
    }

    private void putStaticDeclarationIndexEntry(
            Map<String, QinIrClassDeclaration> index,
            String key,
            QinIrClassDeclaration declaration) {
        QinIrClassDeclaration existing = index.get(key);
        if (existing == null
                || (Objects.equals(declaration.binaryName(), key)
                        && !Objects.equals(existing.binaryName(), key))) {
            index.put(key, declaration);
        }
    }

    private MethodTypeDesc methodDescriptor(QinIrMethodDeclaration method) {
        List<ClassDesc> parameterDescs = new ArrayList<>();
        for (QinIrParameter parameter : method.parameters()) {
            parameterDescs.add(classDescForType(parameter.type()));
        }
        return MethodTypeDesc.of(classDescForType(method.returnType()), parameterDescs);
    }

    private ClassDesc classDescForType(QinIrTypeRef type) {
        if (type == null) {
            return OBJECT_DESC;
        }
        return switch (type.kind()) {
            case VOID -> ClassDesc.ofDescriptor("V");
            case BOOLEAN -> ClassDesc.ofDescriptor("Z");
            case INT -> ClassDesc.ofDescriptor("I");
            case DOUBLE -> ClassDesc.ofDescriptor("D");
            case STRING, CLASS -> referenceClassDesc(type.binaryName());
        };
    }

    private ClassDesc referenceClassDesc(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            return OBJECT_DESC;
        }
        binaryName = effectiveStaticReferenceBinaryName(binaryName);
        if (binaryName.endsWith("[]")) {
            return ClassDesc.ofDescriptor(arrayDescriptor(binaryName, '/'));
        }
        if (binaryName.startsWith("[")) {
            return ClassDesc.ofDescriptor(binaryName.replace('.', '/'));
        }
        return ClassDesc.of(binaryName);
    }

    private String effectiveStaticReferenceBinaryName(String binaryName) {
        if (binaryName == null || binaryName.isBlank()
                || staticDeclarationIndex == null || staticDeclarationIndex.isEmpty()) {
            return binaryName;
        }
        String hostRuntimeBinaryName = canonicalQinHostRuntimeBinaryName(binaryName);
        if (hostRuntimeBinaryName != null) {
            return hostRuntimeBinaryName;
        }
        QinIrClassDeclaration declaration = lookupStaticDeclaration(binaryName);
        if (declaration == null
                || declaration.binaryName() == null
                || declaration.binaryName().isBlank()) {
            return binaryName;
        }
        return declaration.binaryName();
    }

    private boolean isQinHostRuntimeBinaryName(String binaryName) {
        return canonicalQinHostRuntimeBinaryName(binaryName) != null;
    }

    private String canonicalQinHostRuntimeBinaryName(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            return null;
        }
        String canonical = canonicalJavaBinaryName(binaryName);
        if (canonical != null && canonical.startsWith("com.qin.lang.runtime.")) {
            return canonical;
        }
        String flattenedPrefix = "com_qin_lang_runtime_";
        if (binaryName.startsWith(flattenedPrefix) && binaryName.length() > flattenedPrefix.length()) {
            return "com.qin.lang.runtime." + binaryName.substring(flattenedPrefix.length());
        }
        return null;
    }

    private String flattenedBinaryAlias(String binaryName) {
        return binaryName == null ? null : binaryName.replace('.', '_');
    }

    private String arrayDescriptor(String binaryName, char separator) {
        int dimensions = 0;
        String baseName = binaryName;
        while (baseName.endsWith("[]")) {
            dimensions++;
            baseName = baseName.substring(0, baseName.length() - 2);
        }
        StringBuilder descriptor = new StringBuilder();
        descriptor.append("[".repeat(dimensions));
        descriptor.append(switch (baseName) {
            case "boolean" -> "Z";
            case "int" -> "I";
            case "double" -> "D";
            case "long" -> "J";
            case "float" -> "F";
            case "short" -> "S";
            case "byte" -> "B";
            case "char" -> "C";
            default -> "L" + baseName.replace('.', separator) + ";";
        });
        return descriptor.toString();
    }

    private Class<?> runtimeClassForType(QinIrTypeRef type) {
        if (type == null) {
            return Object.class;
        }
        if (type.kind() == QinIrTypeKind.CLASS && type.binaryName() != null) {
            String binaryName = type.binaryName();
            if (binaryName.endsWith("[]") || binaryName.startsWith("[")) {
                try {
                    return Class.forName(runtimeArrayClassName(binaryName));
                } catch (ClassNotFoundException | LinkageError ignored) {
                    return Object[].class;
                }
            }
        }
        if (type.kind() == QinIrTypeKind.BOOLEAN) {
            return boolean.class;
        }
        if (type.kind() == QinIrTypeKind.INT) {
            return int.class;
        }
        if (type.kind() == QinIrTypeKind.DOUBLE) {
            return double.class;
        }
        if (type.kind() == QinIrTypeKind.STRING) {
            return String.class;
        }
        if (type.kind() == QinIrTypeKind.VOID) {
            return void.class;
        }
        return Object.class;
    }

    private String runtimeArrayClassName(String binaryName) {
        if (binaryName.startsWith("[")) {
            return binaryName.replace('/', '.');
        }
        return arrayDescriptor(binaryName, '.');
    }

    private boolean hasVarArgsParameter(QinIrMethodDeclaration method) {
        return method != null
                && !method.parameters().isEmpty()
                && method.parameters().get(method.parameters().size() - 1).varargs();
    }

    private String describeArgumentTypes(List<QinCfaProgram.Expression> arguments) {
        List<String> names = new ArrayList<>(arguments.size());
        for (QinCfaProgram.Expression argument : arguments) {
            names.add(expressionSummary(argument));
        }
        return names.toString();
    }

    private String describeGeneratedConstructors(List<QinIrMethodDeclaration> constructors) {
        List<String> descriptions = new ArrayList<>();
        for (QinIrMethodDeclaration constructor : constructors) {
            List<String> parameterTypes = new ArrayList<>();
            for (QinIrParameter parameter : constructor.parameters()) {
                parameterTypes.add(parameter.type() == null ? "<null>" : parameter.type().toString());
            }
            descriptions.add(constructor.name() + parameterTypes);
        }
        return descriptions.toString();
    }

    private String describeGeneratedStaticMethodCompatibility(
            QinIrClassDeclaration declaration,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        if (declaration == null) {
            return "[]";
        }
        return describeGeneratedStaticMethodCompatibility(List.of(declaration), methodName, arguments);
    }

    private String describeGeneratedStaticMethodCompatibility(
            List<QinIrClassDeclaration> declarations,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
        if (declarations == null || declarations.isEmpty()) {
            return "[]";
        }
        List<String> descriptions = new ArrayList<>();
        for (QinIrClassDeclaration declaration : declarations) {
            if (declaration == null) {
                continue;
            }
            boolean matchedName = false;
            for (QinIrMethodDeclaration method : declaration.methods()) {
                if (!method.name().equals(methodName)) {
                    continue;
                }
                matchedName = true;
                List<String> parameterTypes = new ArrayList<>();
                for (QinIrParameter parameter : method.parameters()) {
                    parameterTypes.add(parameter.type() == null ? "<null>" : parameter.type().toString());
                }
                List<String> argumentTypes = new ArrayList<>();
                for (QinCfaProgram.Expression argument : arguments) {
                    QinIrTypeRef argumentType = staticIrTypeForExpression(argument, new LinkedHashSet<>());
                    argumentTypes.add(argumentType == null ? expressionSummary(argument) : argumentType.toString());
                }
                descriptions.add(declaration.binaryName()
                        + "." + method.name()
                        + "{static=" + method.staticMethod()
                        + ", params=" + parameterTypes
                        + ", args=" + argumentTypes
                        + ", score=" + (method.staticMethod()
                        ? generatedLocalMethodMatchScore(method, arguments)
                        : "non-static")
                        + "}");
            }
            if (!matchedName) {
                descriptions.add(declaration.binaryName() + "{methods=" + declaration.methods().stream()
                        .map(method -> (method.staticMethod() ? "static " : "") + method.name())
                        .limit(8)
                        .toList()
                        + "}");
            }
        }
        return descriptions.toString();
    }

    private String describeGeneratedConstructorRuntimeCompatibility(
            List<QinIrMethodDeclaration> constructors,
            List<QinCfaProgram.Expression> arguments) {
        List<String> descriptions = new ArrayList<>();
        for (QinIrMethodDeclaration constructor : constructors) {
            Class<?>[] runtimeParameterTypes = constructor.parameters().stream()
                    .map(parameter -> runtimeClassForType(parameter.type()))
                    .toArray(Class<?>[]::new);
            List<String> parameterTypes = new ArrayList<>();
            for (Class<?> runtimeParameterType : runtimeParameterTypes) {
                parameterTypes.add(runtimeParameterType.getTypeName()
                        + "{array=" + runtimeParameterType.isArray() + "}");
            }
            List<String> scores = new ArrayList<>();
            int parameterCount = Math.min(arguments.size(), runtimeParameterTypes.length);
            for (int i = 0; i < parameterCount; i++) {
                scores.add(String.valueOf(compatibilityScore(arguments.get(i), runtimeParameterTypes[i])));
            }
            descriptions.add(constructor.name()
                    + "{params=" + parameterTypes
                    + ", arityScore=" + compatibilityScore(
                            arguments,
                            runtimeParameterTypes,
                            hasVarArgsParameter(constructor))
                    + ", parameterScores=" + scores
                    + "}");
        }
        return descriptions.toString();
    }

    private void invokeInstanceMethod(CodeBuilder code, ResolvedMethod resolvedMethod) {
        if (resolvedMethod.ownerInterface()) {
            code.invokeinterface(
                    ClassDesc.of(resolvedMethod.ownerBinaryName()),
                    resolvedMethod.methodName(),
                    resolvedMethod.descriptor());
            return;
        }
        code.invokevirtual(
                ClassDesc.of(resolvedMethod.ownerBinaryName()),
                resolvedMethod.methodName(),
                resolvedMethod.descriptor());
    }

    private void emitJavaReceiverCast(CodeBuilder code, ResolvedMethod resolvedMethod) {
        code.checkcast(ClassDesc.of(resolvedMethod.ownerBinaryName()));
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

    private static final class ClassfileCursor {
        private final byte[] bytes;
        private int offset;

        private ClassfileCursor(byte[] bytes) {
            this.bytes = bytes;
        }

        private byte[] bytes() {
            return bytes;
        }

        private int offset() {
            return offset;
        }

        private int readU1() {
            int value = bytes[offset] & 0xFF;
            offset += 1;
            return value;
        }

        private int readU2() {
            int value = ((bytes[offset] & 0xFF) << 8) | (bytes[offset + 1] & 0xFF);
            offset += 2;
            return value;
        }

        private int readU4() {
            int value = (readU2() << 16) | readU2();
            return value;
        }

        private void skip(int length) {
            offset += length;
        }
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

    private record LocalBinding(int slot, Class<?> staticType) {
    }

    private record GeneratedMethodSelection(
            QinIrClassDeclaration owner,
            QinIrMethodDeclaration method) {
    }

    private record ResolvedConstructor(Class<?>[] parameterTypes, MethodTypeDesc descriptor, boolean varArgs) {
    }

    private record StaticCallMethod(
            QinCfaProgram.Expression receiver,
            Class<?> ownerClass,
            String methodName,
            List<QinCfaProgram.Expression> arguments) {
    }

    private record ResolvedMethod(
            Method method,
            String ownerBinaryName,
            String methodName,
            Class<?>[] parameterTypes,
            ClassDesc[] parameterDescs,
            Class<?> returnType,
            MethodTypeDesc descriptor,
            boolean staticMethod,
            boolean varArgs,
            boolean ownerInterface) {
    }

    private record GeneratedMethodCandidate(
            QinIrClassDeclaration owner,
            QinIrMethodDeclaration method,
            int score,
            int depth) {
    }
}

