package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaInstanceCall;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrJavaInstanceMethodCall;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStringLiteral;

import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.TypeKind;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Minimal JVM backend based on JDK Class-File API.
 */
public final class QinJvmClassFileBackend {
    private static final ClassDesc OBJECT_DESC = ClassDesc.of("java.lang.Object");
    private static final ClassDesc LINKED_HASH_MAP_DESC = ClassDesc.of("java.util.LinkedHashMap");
    private static final ClassDesc QIN_CONSOLE_DESC = ClassDesc.of("com.qin.lang.runtime.QinConsole");
    private static final ClassDesc INTEGER_DESC = ClassDesc.of("java.lang.Integer");

    private static final MethodTypeDesc VOID_INIT = MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc RUN_SIGNATURE = MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;");
    private static final MethodTypeDesc CONSOLE_LOG_SIGNATURE = MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)V");
    private static final MethodTypeDesc INTEGER_VALUE_OF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;");
    private static final MethodTypeDesc MAP_PUT_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
    private static final MethodTypeDesc MAP_GET_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;");

    public byte[] compileProgram(QinIrProgram program, String className) {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(className, "className cannot be null");

        if (program.declarations().isEmpty()
                && program.consoleLogs().isEmpty()
                && program.javaStaticConsoleLogs().isEmpty()
                && program.javaInstanceMethodCalls().isEmpty()
                && program.javaInstanceConsoleLogs().isEmpty()) {
            throw new IllegalArgumentException("Program has no compilable statements");
        }

        ClassFile classFile = ClassFile.of();
        return classFile.build(ClassDesc.of(className), builder -> {
            builder.withFlags(ClassFile.ACC_PUBLIC | ClassFile.ACC_SUPER);

            builder.withMethodBody("<init>", VOID_INIT, ClassFile.ACC_PUBLIC, code -> {
                code.aload(0);
                code.invokespecial(OBJECT_DESC, "<init>", VOID_INIT);
                code.return_();
            });

            builder.withMethodBody("run", RUN_SIGNATURE, ClassFile.ACC_PUBLIC | ClassFile.ACC_STATIC,
                    code -> emitRunMethod(
                            code,
                            program.declarations(),
                            program.consoleLogs(),
                            program.javaStaticConsoleLogs(),
                            program.javaInstanceMethodCalls(),
                            program.javaInstanceConsoleLogs()));
        });
    }

    private void emitRunMethod(
            CodeBuilder code,
            List<QinIrConstDeclaration> declarations,
            List<QinIrConsoleLogStatement> consoleLogs,
            List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs,
            List<QinIrJavaInstanceMethodCall> javaInstanceMethodCalls,
            List<QinIrConsoleLogJavaInstanceCall> javaInstanceConsoleLogs) {
        Map<String, DeclarationBinding> bindings = new LinkedHashMap<>();

        for (QinIrConstDeclaration declaration : declarations) {
            int slot = code.allocateLocal(TypeKind.REFERENCE);
            emitDeclarationInitializer(code, declaration.initializer(), slot);
            bindings.put(declaration.name(), new DeclarationBinding(slot, declaration.initializer()));
        }

        for (QinIrConsoleLogStatement consoleLog : consoleLogs) {
            emitObjectConsoleLog(code, bindings, consoleLog);
        }

        for (QinIrJavaInstanceMethodCall javaInstanceMethodCall : javaInstanceMethodCalls) {
            emitJavaInstanceMethodCall(code, bindings, javaInstanceMethodCall);
        }

        for (QinIrConsoleLogJavaStaticCall javaStaticCall : javaStaticConsoleLogs) {
            emitJavaStaticConsoleLog(code, javaStaticCall);
        }

        for (QinIrConsoleLogJavaInstanceCall javaInstanceConsoleLog : javaInstanceConsoleLogs) {
            emitJavaInstanceConsoleLog(code, bindings, javaInstanceConsoleLog);
        }

        Integer returnSlot = lastDeclarationSlot(bindings);
        if (returnSlot != null) {
            code.aload(returnSlot);
            code.areturn();
        } else {
            code.aconst_null();
            code.areturn();
        }
    }

    private void emitDeclarationInitializer(CodeBuilder code, QinIrExpression initializer, int slot) {
        if (initializer instanceof QinIrObjectLiteral objectLiteral) {
            emitObjectLiteralInitializer(code, objectLiteral, slot);
            return;
        }
        if (initializer instanceof QinIrJavaNewExpression javaNewExpression) {
            emitJavaNewInitializer(code, javaNewExpression, slot);
            return;
        }
        throw new IllegalArgumentException(
                "Unsupported const initializer: " + initializer.getClass().getSimpleName());
    }

    private void emitObjectLiteralInitializer(CodeBuilder code, QinIrObjectLiteral objectLiteral, int slot) {
        code.new_(LINKED_HASH_MAP_DESC);
        code.dup();
        code.invokespecial(LINKED_HASH_MAP_DESC, "<init>", VOID_INIT);
        code.astore(slot);

        for (QinIrObjectProperty property : objectLiteral.properties()) {
            code.aload(slot);
            code.ldc(property.key());
            emitExpressionAsObject(code, property.value());
            code.invokevirtual(LINKED_HASH_MAP_DESC, "put", MAP_PUT_SIGNATURE);
            code.pop();
        }
    }

    private void emitJavaNewInitializer(CodeBuilder code, QinIrJavaNewExpression javaNewExpression, int slot) {
        ResolvedConstructor resolvedConstructor = resolveConstructor(
                javaNewExpression.ownerBinaryName(),
                javaNewExpression.arguments());

        ClassDesc ownerDesc = ClassDesc.of(javaNewExpression.ownerBinaryName());
        code.new_(ownerDesc);
        code.dup();
        emitArgumentsForParameters(code, javaNewExpression.arguments(), resolvedConstructor.parameterTypes());
        code.invokespecial(ownerDesc, "<init>", resolvedConstructor.descriptor());
        code.astore(slot);
    }

    private void emitObjectConsoleLog(
            CodeBuilder code,
            Map<String, DeclarationBinding> bindings,
            QinIrConsoleLogStatement consoleLog) {
        DeclarationBinding binding = bindings.get(consoleLog.objectName());
        if (binding == null) {
            throw new IllegalArgumentException("Unknown object in console.log: " + consoleLog.objectName());
        }
        if (!(binding.initializer() instanceof QinIrObjectLiteral objectLiteral)) {
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

        code.aload(binding.slot());
        code.ldc(consoleLog.propertyName());
        code.invokevirtual(LINKED_HASH_MAP_DESC, "get", MAP_GET_SIGNATURE);
        code.invokestatic(QIN_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private void emitJavaInstanceMethodCall(
            CodeBuilder code,
            Map<String, DeclarationBinding> bindings,
            QinIrJavaInstanceMethodCall javaInstanceMethodCall) {
        DeclarationBinding binding = requireJavaBinding(bindings, javaInstanceMethodCall.receiverName());
        ResolvedMethod resolvedMethod = resolveInstanceMethod(
                javaInstanceMethodCall.ownerBinaryName(),
                javaInstanceMethodCall.methodName(),
                javaInstanceMethodCall.arguments());

        code.aload(binding.slot());
        emitArgumentsForParameters(code, javaInstanceMethodCall.arguments(), resolvedMethod.parameterTypes());
        invokeInstanceMethod(code, resolvedMethod);
        discardReturnValue(code, resolvedMethod.returnType());
    }

    private void emitJavaStaticConsoleLog(CodeBuilder code, QinIrConsoleLogJavaStaticCall javaStaticCall) {
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
        code.invokestatic(QIN_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private void emitJavaInstanceConsoleLog(
            CodeBuilder code,
            Map<String, DeclarationBinding> bindings,
            QinIrConsoleLogJavaInstanceCall javaInstanceConsoleLog) {
        DeclarationBinding binding = requireJavaBinding(bindings, javaInstanceConsoleLog.receiverName());
        ResolvedMethod resolvedMethod = resolveInstanceMethod(
                javaInstanceConsoleLog.ownerBinaryName(),
                javaInstanceConsoleLog.methodName(),
                javaInstanceConsoleLog.arguments());

        code.aload(binding.slot());
        emitArgumentsForParameters(code, javaInstanceConsoleLog.arguments(), resolvedMethod.parameterTypes());
        invokeInstanceMethod(code, resolvedMethod);
        emitBoxIfNeeded(code, resolvedMethod.returnType());
        code.invokestatic(QIN_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private DeclarationBinding requireJavaBinding(Map<String, DeclarationBinding> bindings, String receiverName) {
        DeclarationBinding binding = bindings.get(receiverName);
        if (binding == null) {
            throw new IllegalArgumentException("Unknown Java instance receiver: " + receiverName);
        }
        if (!(binding.initializer() instanceof QinIrJavaNewExpression)) {
            throw new IllegalArgumentException("Receiver is not a Java instance binding: " + receiverName);
        }
        return binding;
    }

    private void emitArgumentsForParameters(
            CodeBuilder code,
            List<QinIrExpression> arguments,
            Class<?>[] parameterTypes) {
        if (arguments.size() != parameterTypes.length) {
            throw new IllegalArgumentException("Argument count mismatch");
        }
        for (int i = 0; i < arguments.size(); i++) {
            emitExpressionForParameter(code, arguments.get(i), parameterTypes[i]);
        }
    }

    private void emitExpressionAsObject(CodeBuilder code, QinIrExpression expression) {
        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            code.loadConstant(numberLiteral.value());
            code.invokestatic(INTEGER_DESC, "valueOf", INTEGER_VALUE_OF_SIGNATURE);
            return;
        }
        if (expression instanceof QinIrStringLiteral stringLiteral) {
            code.ldc(stringLiteral.value());
            return;
        }

        throw new IllegalArgumentException("Unsupported object expression type: "
                + expression.getClass().getSimpleName());
    }

    private void emitExpressionForParameter(CodeBuilder code, QinIrExpression expression, Class<?> parameterType) {
        if (expression instanceof QinIrStringLiteral stringLiteral) {
            if (parameterType.isPrimitive()) {
                throw new IllegalArgumentException("String literal cannot target primitive parameter: "
                        + parameterType.getName());
            }
            code.ldc(stringLiteral.value());
            return;
        }

        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            if (parameterType == int.class) {
                code.loadConstant(numberLiteral.value());
                return;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(Integer.class)) {
                code.loadConstant(numberLiteral.value());
                code.invokestatic(INTEGER_DESC, "valueOf", INTEGER_VALUE_OF_SIGNATURE);
                return;
            }
            throw new IllegalArgumentException("Unsupported numeric parameter type: " + parameterType.getName());
        }

        throw new IllegalArgumentException("Unsupported call argument expression: "
                + expression.getClass().getSimpleName());
    }

    private ResolvedConstructor resolveConstructor(String ownerBinaryName, List<QinIrExpression> arguments) {
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
            List<QinIrExpression> arguments) {
        ResolvedMethod resolvedMethod = resolveMethod(ownerBinaryName, methodName, arguments);
        if (!Modifier.isStatic(resolvedMethod.method().getModifiers())) {
            throw new IllegalArgumentException("Method is not static: " + ownerBinaryName + "." + methodName);
        }
        return resolvedMethod;
    }

    private ResolvedMethod resolveInstanceMethod(
            String ownerBinaryName,
            String methodName,
            List<QinIrExpression> arguments) {
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
            List<QinIrExpression> arguments) {
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

    private int compatibilityScore(List<QinIrExpression> arguments, Class<?>[] parameterTypes) {
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

    private int compatibilityScore(QinIrExpression argument, Class<?> parameterType) {
        if (argument instanceof QinIrStringLiteral) {
            if (parameterType == String.class) {
                return 0;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(String.class)) {
                return 1;
            }
            return -1;
        }
        if (argument instanceof QinIrNumberLiteral) {
            if (parameterType == int.class) {
                return 0;
            }
            if (parameterType == Integer.class) {
                return 1;
            }
            if (!parameterType.isPrimitive() && parameterType.isAssignableFrom(Integer.class)) {
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

    private Integer lastDeclarationSlot(Map<String, DeclarationBinding> bindings) {
        Integer slot = null;
        for (DeclarationBinding binding : bindings.values()) {
            slot = binding.slot();
        }
        return slot;
    }

    private record DeclarationBinding(int slot, QinIrExpression initializer) {
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
