package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrConsoleLogJavaStaticCall;
import com.qin.lang.ir.QinIrConsoleLogStatement;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;

import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.TypeKind;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
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

        QinIrConstDeclaration declaration = null;
        QinIrObjectLiteral objectLiteral = null;
        if (!program.declarations().isEmpty()) {
            declaration = program.declarations().get(0);
            if (!(declaration.initializer() instanceof QinIrObjectLiteral literal)) {
                throw new IllegalArgumentException("Current backend supports only const object literal initializer");
            }
            objectLiteral = literal;
        }

        if (declaration == null
                && program.consoleLogs().isEmpty()
                && program.javaStaticConsoleLogs().isEmpty()) {
            throw new IllegalArgumentException("Program has no compilable statements");
        }

        QinIrConstDeclaration finalDeclaration = declaration;
        QinIrObjectLiteral finalObjectLiteral = objectLiteral;
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
                            finalDeclaration,
                            finalObjectLiteral,
                            program.consoleLogs(),
                            program.javaStaticConsoleLogs()));
        });
    }

    private void emitRunMethod(
            CodeBuilder code,
            QinIrConstDeclaration declaration,
            QinIrObjectLiteral objectLiteral,
            List<QinIrConsoleLogStatement> consoleLogs,
            List<QinIrConsoleLogJavaStaticCall> javaStaticConsoleLogs) {
        int objectSlot = -1;
        if (declaration != null && objectLiteral != null) {
            objectSlot = code.allocateLocal(TypeKind.REFERENCE);
            code.new_(LINKED_HASH_MAP_DESC);
            code.dup();
            code.invokespecial(LINKED_HASH_MAP_DESC, "<init>", VOID_INIT);
            code.astore(objectSlot);

            for (QinIrObjectProperty property : objectLiteral.properties()) {
                code.aload(objectSlot);
                code.ldc(property.key());
                emitExpression(code, property.value());
                code.invokevirtual(LINKED_HASH_MAP_DESC, "put", MAP_PUT_SIGNATURE);
                code.pop();
            }
        }

        for (QinIrConsoleLogStatement consoleLog : consoleLogs) {
            if (objectSlot < 0 || declaration == null) {
                throw new IllegalArgumentException("console.log(object.property) requires const object declaration");
            }
            emitConsoleLog(code, objectSlot, declaration, consoleLog);
        }

        for (QinIrConsoleLogJavaStaticCall javaStaticCall : javaStaticConsoleLogs) {
            emitJavaStaticConsoleLog(code, javaStaticCall);
        }

        if (objectSlot >= 0) {
            code.aload(objectSlot);
            code.areturn();
        } else {
            code.aconst_null();
            code.areturn();
        }
    }

    private void emitConsoleLog(
            CodeBuilder code,
            int objectSlot,
            QinIrConstDeclaration declaration,
            QinIrConsoleLogStatement consoleLog) {
        if (!declaration.name().equals(consoleLog.objectName())) {
            throw new IllegalArgumentException(
                    "Unknown object in console.log: " + consoleLog.objectName());
        }

        if (!(declaration.initializer() instanceof QinIrObjectLiteral objectLiteral)) {
            throw new IllegalArgumentException(
                    "console.log currently requires const object literal declaration");
        }

        boolean propertyExists = objectLiteral.properties().stream()
                .anyMatch(property -> property.key().equals(consoleLog.propertyName()));
        if (!propertyExists) {
            throw new IllegalArgumentException(
                    "Unknown property in console.log: " + consoleLog.propertyName());
        }

        code.aload(objectSlot);
        code.ldc(consoleLog.propertyName());
        code.invokevirtual(LINKED_HASH_MAP_DESC, "get", MAP_GET_SIGNATURE);
        code.invokestatic(QIN_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
    }

    private void emitExpression(CodeBuilder code, QinIrExpression expression) {
        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            code.loadConstant(numberLiteral.value());
            code.invokestatic(INTEGER_DESC, "valueOf", INTEGER_VALUE_OF_SIGNATURE);
            return;
        }

        throw new IllegalArgumentException("Unsupported expression type: " + expression.getClass().getSimpleName());
    }

    private void emitJavaStaticConsoleLog(CodeBuilder code, QinIrConsoleLogJavaStaticCall javaStaticCall) {
        try {
            Class<?> ownerClass = Class.forName(javaStaticCall.ownerBinaryName());
            Method method = ownerClass.getMethod(javaStaticCall.methodName());
            if (!Modifier.isStatic(method.getModifiers())) {
                throw new IllegalArgumentException("Method is not static: " + javaStaticCall.ownerBinaryName()
                        + "." + javaStaticCall.methodName());
            }
            if (method.getParameterCount() != 0) {
                throw new IllegalArgumentException("Only zero-arg Java static call is supported: "
                        + javaStaticCall.ownerBinaryName() + "." + javaStaticCall.methodName());
            }

            String descriptor = MethodType.methodType(method.getReturnType()).toMethodDescriptorString();
            code.invokestatic(
                    ClassDesc.of(javaStaticCall.ownerBinaryName()),
                    javaStaticCall.methodName(),
                    MethodTypeDesc.ofDescriptor(descriptor));

            emitBoxIfNeeded(code, method.getReturnType());
            code.invokestatic(QIN_CONSOLE_DESC, "log", CONSOLE_LOG_SIGNATURE);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot resolve Java static method: "
                    + javaStaticCall.ownerBinaryName() + "." + javaStaticCall.methodName(), e);
        }
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
}
