package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrConstDeclaration;
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
import java.util.Objects;

/**
 * Minimal JVM backend based on JDK Class-File API.
 */
public final class QinJvmClassFileBackend {
    private static final ClassDesc OBJECT_DESC = ClassDesc.of("java.lang.Object");
    private static final ClassDesc LINKED_HASH_MAP_DESC = ClassDesc.of("java.util.LinkedHashMap");
    private static final ClassDesc INTEGER_DESC = ClassDesc.of("java.lang.Integer");

    private static final MethodTypeDesc VOID_INIT = MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc RUN_SIGNATURE = MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;");
    private static final MethodTypeDesc INTEGER_VALUE_OF_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;");
    private static final MethodTypeDesc MAP_PUT_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    public byte[] compileProgram(QinIrProgram program, String className) {
        Objects.requireNonNull(program, "program cannot be null");
        Objects.requireNonNull(className, "className cannot be null");

        if (program.declarations().isEmpty()) {
            throw new IllegalArgumentException("Program must contain at least one declaration");
        }

        QinIrConstDeclaration declaration = program.declarations().get(0);
        if (!(declaration.initializer() instanceof QinIrObjectLiteral objectLiteral)) {
            throw new IllegalArgumentException("Current backend POC supports only const object literal initializer");
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
                    code -> emitRunMethod(code, objectLiteral));
        });
    }

    private void emitRunMethod(CodeBuilder code, QinIrObjectLiteral objectLiteral) {
        int objectSlot = code.allocateLocal(TypeKind.REFERENCE);

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

        code.aload(objectSlot);
        code.areturn();
    }

    private void emitExpression(CodeBuilder code, QinIrExpression expression) {
        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            code.loadConstant(numberLiteral.value());
            code.invokestatic(INTEGER_DESC, "valueOf", INTEGER_VALUE_OF_SIGNATURE);
            return;
        }

        throw new IllegalArgumentException("Unsupported expression type: " + expression.getClass().getSimpleName());
    }
}

