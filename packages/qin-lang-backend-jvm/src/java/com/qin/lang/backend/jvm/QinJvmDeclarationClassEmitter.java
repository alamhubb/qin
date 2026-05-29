package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinBuiltinRegistry;

import java.lang.classfile.Annotation;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.AnnotationValue;
import java.lang.classfile.ClassFile;
import java.lang.classfile.attribute.MethodParameterInfo;
import java.lang.classfile.attribute.MethodParametersAttribute;
import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.lang.classfile.attribute.RuntimeVisibleParameterAnnotationsAttribute;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * First-phase declaration emitter for Qin class declarations.
 *
 * This is the first step toward moving JVM declaration emission into the shared
 * backend rather than leaving it in framework-specific bridge compilers.
 */
public final class QinJvmDeclarationClassEmitter {
    private static final int DEFAULT_CLASSFILE_MAJOR_VERSION = ClassFile.JAVA_21_VERSION;
    private static final ClassDesc OBJECT_DESC = ClassDesc.of("java.lang.Object");
    private static final ClassDesc NUMBER_DESC = ClassDesc.of("java.lang.Number");
    private static final ClassDesc BOOLEAN_DESC = ClassDesc.of("java.lang.Boolean");
    private static final ClassDesc STRING_DESC = ClassDesc.of("java.lang.String");
    private static final ClassDesc LINKED_HASH_MAP_DESC = ClassDesc.of("java.util.LinkedHashMap");
    private static final ClassDesc ESM_GLOBAL_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmGlobal");
    private static final MethodTypeDesc VOID_INIT = MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc MAP_PUT_SIGNATURE =
            MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC);

    public byte[] compileSingleClass(QinIrProgram program, String fallbackBinaryName) {
        Objects.requireNonNull(program, "program cannot be null");
        if (program.classDeclarations().size() != 1) {
            throw new IllegalArgumentException(
                    "Expected exactly one class declaration, got " + program.classDeclarations().size());
        }
        QinIrClassDeclaration declaration = program.classDeclarations().get(0);
        String binaryName = declaration.binaryName();
        if (binaryName == null || binaryName.isBlank()) {
            binaryName = fallbackBinaryName;
        }
        if (binaryName == null || binaryName.isBlank()) {
            throw new IllegalArgumentException("Binary class name cannot be blank");
        }
        return compileClass(declaration, binaryName, buildDeclarationIndex(program.classDeclarations()));
    }

    public Map<String, byte[]> compileAllClasses(QinIrProgram program) {
        Objects.requireNonNull(program, "program cannot be null");
        if (program.classDeclarations().isEmpty()) {
            throw new IllegalArgumentException("Expected at least one class declaration");
        }

        Map<String, QinIrClassDeclaration> declarationIndex = buildDeclarationIndex(program.classDeclarations());
        Map<String, byte[]> compiled = new LinkedHashMap<>();
        for (QinIrClassDeclaration declaration : program.classDeclarations()) {
            String binaryName = declaration.binaryName();
            if (binaryName == null || binaryName.isBlank()) {
                throw new IllegalArgumentException(
                        "Binary class name cannot be blank for declaration: " + declaration.simpleName());
            }
            compiled.put(binaryName, compileClass(declaration, binaryName, declarationIndex));
        }
        return Map.copyOf(compiled);
    }

    public byte[] compileClass(QinIrClassDeclaration declaration, String binaryClassName) {
        return compileClass(declaration, binaryClassName, buildDeclarationIndex(List.of(declaration)));
    }

    private byte[] compileClass(
            QinIrClassDeclaration declaration,
            String binaryClassName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        Objects.requireNonNull(declaration, "declaration cannot be null");
        Objects.requireNonNull(binaryClassName, "binaryClassName cannot be null");
        Objects.requireNonNull(declarationIndex, "declarationIndex cannot be null");

        ClassFile classFile = ClassFile.of();
        return classFile.build(ClassDesc.of(binaryClassName), builder -> {
            builder.withVersion(DEFAULT_CLASSFILE_MAJOR_VERSION, 0);
            builder.withFlags(ClassFile.ACC_PUBLIC | ClassFile.ACC_SUPER);
            builder.withSuperclass(resolveSuperclass(declaration.superType()));

            RuntimeVisibleAnnotationsAttribute classAnnotations = createAnnotationsAttribute(declaration.annotations());
            if (classAnnotations != null) {
                builder.with(classAnnotations);
            }

            for (QinIrFieldDeclaration field : declaration.fields()) {
                builder.withField(field.name(), toClassDesc(field.type()), fieldBuilder -> {
                    fieldBuilder.withFlags(ClassFile.ACC_PRIVATE);
                    RuntimeVisibleAnnotationsAttribute fieldAnnotations =
                            createAnnotationsAttribute(field.annotations());
                    if (fieldAnnotations != null) {
                        fieldBuilder.with(fieldAnnotations);
                    }
                });
                builder.withMethodBody(
                        getterName(field),
                        MethodTypeDesc.of(toClassDesc(field.type())),
                        ClassFile.ACC_PUBLIC,
                        code -> emitFieldGetterBody(code, binaryClassName, field));
                builder.withMethodBody(
                        setterName(field),
                        MethodTypeDesc.ofDescriptor("(" + toClassDesc(field.type()).descriptorString() + ")V"),
                        ClassFile.ACC_PUBLIC,
                        code -> emitFieldSetterBody(code, binaryClassName, field));
            }

            builder.withMethodBody("<init>", VOID_INIT, ClassFile.ACC_PUBLIC, code -> {
                code.aload(0);
                code.invokespecial(resolveSuperclass(declaration.superType()), "<init>", VOID_INIT);
                for (QinIrFieldDeclaration field : declaration.fields()) {
                    emitFieldInitializer(code, binaryClassName, field);
                }
                code.return_();
            });

            if (!declaration.fields().isEmpty()) {
                builder.withMethod(
                        "<init>",
                        toConstructorDescriptor(declaration.fields()),
                        ClassFile.ACC_PUBLIC,
                        methodBuilder -> {
                            MethodParametersAttribute constructorParameters =
                                    createFieldConstructorParametersAttribute(declaration.fields());
                            if (constructorParameters != null) {
                                methodBuilder.with(constructorParameters);
                            }
                            methodBuilder.withCode(code -> emitAllArgsConstructorBody(code, declaration, binaryClassName));
                        });
            }

            for (QinIrMethodDeclaration method : declaration.methods()) {
                builder.withMethod(
                        method.name(),
                        toMethodDescriptor(method),
                        ClassFile.ACC_PUBLIC,
                        methodBuilder -> {
                            MethodParametersAttribute methodParameters = createMethodParametersAttribute(method);
                            if (methodParameters != null) {
                                methodBuilder.with(methodParameters);
                            }

                            RuntimeVisibleAnnotationsAttribute methodAnnotations =
                                    createAnnotationsAttribute(method.annotations());
                            if (methodAnnotations != null) {
                                methodBuilder.with(methodAnnotations);
                            }

                            RuntimeVisibleParameterAnnotationsAttribute parameterAnnotations =
                                    createParameterAnnotationsAttribute(method);
                            if (parameterAnnotations != null) {
                                methodBuilder.with(parameterAnnotations);
                            }

                            methodBuilder.withCode(code -> emitMethodBody(code, declaration, method, declarationIndex));
                        });
            }
        });
    }

    private Map<String, QinIrClassDeclaration> buildDeclarationIndex(List<QinIrClassDeclaration> declarations) {
        Map<String, QinIrClassDeclaration> index = new LinkedHashMap<>();
        for (QinIrClassDeclaration declaration : declarations) {
            String binaryName = declaration.binaryName();
            if (binaryName != null && !binaryName.isBlank()) {
                index.put(binaryName, declaration);
            }
        }
        return Map.copyOf(index);
    }

    private ClassDesc resolveSuperclass(QinIrTypeRef superType) {
        if (superType == null || superType.binaryName() == null || superType.binaryName().isBlank()) {
            return OBJECT_DESC;
        }
        return ClassDesc.of(superType.binaryName());
    }

    private MethodTypeDesc toMethodDescriptor(QinIrMethodDeclaration method) {
        List<ClassDesc> parameterDescs = new ArrayList<>();
        for (var parameter : method.parameters()) {
            parameterDescs.add(toClassDesc(parameter.type()));
        }
        return MethodTypeDesc.of(toClassDesc(method.returnType()), parameterDescs);
    }

    private MethodTypeDesc toConstructorDescriptor(List<QinIrFieldDeclaration> fields) {
        List<ClassDesc> parameterDescs = new ArrayList<>();
        for (QinIrFieldDeclaration field : fields) {
            parameterDescs.add(toClassDesc(field.type()));
        }
        return MethodTypeDesc.ofDescriptor(MethodTypeDesc.of(ClassDesc.ofDescriptor("V"), parameterDescs).descriptorString());
    }

    private ClassDesc toClassDesc(QinIrTypeRef type) {
        if (type == null) {
            return ClassDesc.of("java.lang.Object");
        }
        return switch (type.kind()) {
            case VOID -> ClassDesc.ofDescriptor("V");
            case BOOLEAN -> ClassDesc.ofDescriptor("Z");
            case INT -> ClassDesc.ofDescriptor("I");
            case DOUBLE -> ClassDesc.ofDescriptor("D");
            case STRING, CLASS -> ClassDesc.of(type.binaryName());
        };
    }

    private String getterName(QinIrFieldDeclaration field) {
        String fieldName = field.name();
        if (field.type().kind() == QinIrTypeKind.BOOLEAN) {
            return "is" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        }
        return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private String setterName(QinIrFieldDeclaration field) {
        String fieldName = field.name();
        return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private void emitFieldGetterBody(
            java.lang.classfile.CodeBuilder code,
            String ownerBinaryName,
            QinIrFieldDeclaration field) {
        code.aload(0);
        code.getfield(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
        switch (field.type().kind()) {
            case BOOLEAN -> code.ireturn();
            case INT -> code.ireturn();
            case DOUBLE -> code.dreturn();
            case VOID -> code.return_();
            case STRING, CLASS -> code.areturn();
        }
    }

    private void emitFieldSetterBody(
            java.lang.classfile.CodeBuilder code,
            String ownerBinaryName,
            QinIrFieldDeclaration field) {
        code.aload(0);
        switch (field.type().kind()) {
            case BOOLEAN, INT -> code.iload(1);
            case DOUBLE -> code.dload(1);
            case STRING, CLASS -> code.aload(1);
            case VOID -> throw new IllegalArgumentException("Field type cannot be void: " + field.name());
        }
        code.putfield(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
        code.return_();
    }

    private void emitFieldInitializer(
            java.lang.classfile.CodeBuilder code,
            String ownerBinaryName,
            QinIrFieldDeclaration field) {
        QinIrExpression initializer = field.initializer();
        if (initializer == null) {
            return;
        }

        code.aload(0);
        emitValueForFieldType(code, field.type(), initializer, field.name());
        code.putfield(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
    }

    private void emitValueForFieldType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef fieldType,
            QinIrExpression value,
            String fieldName) {
        switch (fieldType.kind()) {
            case STRING, CLASS -> emitReferenceInitializer(code, value, fieldName);
            case BOOLEAN -> emitBooleanInitializer(code, value, fieldName);
            case INT -> emitIntInitializer(code, value, fieldName);
            case DOUBLE -> emitDoubleInitializer(code, value, fieldName);
            case VOID -> throw new IllegalArgumentException("Field type cannot be void: " + fieldName);
        }
    }

    private void emitReferenceInitializer(
            java.lang.classfile.CodeBuilder code,
            QinIrExpression value,
            String fieldName) {
        if (value instanceof QinIrNullLiteral) {
            code.aconst_null();
            return;
        }
        if (value instanceof QinIrStringLiteral stringLiteral) {
            code.ldc(stringLiteral.value());
            return;
        }
        throw new IllegalArgumentException(
                "Unsupported reference field initializer for `" + fieldName + "`: " + value.getClass().getSimpleName());
    }

    private void emitBooleanInitializer(
            java.lang.classfile.CodeBuilder code,
            QinIrExpression value,
            String fieldName) {
        if (value instanceof QinIrBooleanLiteral booleanLiteral) {
            if (booleanLiteral.value()) {
                code.iconst_1();
            } else {
                code.iconst_0();
            }
            return;
        }
        throw new IllegalArgumentException(
                "Unsupported boolean field initializer for `" + fieldName + "`: " + value.getClass().getSimpleName());
    }

    private void emitIntInitializer(
            java.lang.classfile.CodeBuilder code,
            QinIrExpression value,
            String fieldName) {
        if (value instanceof QinIrNumberLiteral numberLiteral) {
            if (Math.rint(numberLiteral.value()) != numberLiteral.value()) {
                throw new IllegalArgumentException(
                        "Non-integer initializer for int field `" + fieldName + "`: " + numberLiteral.value());
            }
            code.loadConstant((int) numberLiteral.value());
            return;
        }
        throw new IllegalArgumentException(
                "Unsupported int field initializer for `" + fieldName + "`: " + value.getClass().getSimpleName());
    }

    private void emitDoubleInitializer(
            java.lang.classfile.CodeBuilder code,
            QinIrExpression value,
            String fieldName) {
        if (value instanceof QinIrNumberLiteral numberLiteral) {
            code.loadConstant(numberLiteral.value());
            return;
        }
        throw new IllegalArgumentException(
                "Unsupported double field initializer for `" + fieldName + "`: " + value.getClass().getSimpleName());
    }

    private void emitMethodBody(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        QinIrExpression returnExpression = method.returnExpression();
        if (returnExpression == null || returnExpression instanceof QinIrNullLiteral) {
            if (method.returnType().kind() == QinIrTypeKind.VOID) {
                code.return_();
            } else {
                code.aconst_null();
                code.areturn();
            }
            return;
        }

        QinIrTypeRef actualType = emitDeclarationExpression(code, ownerDeclaration, method, declarationIndex, returnExpression);
        emitReturnForType(code, actualType, method.returnType());
    }

    private QinIrTypeRef emitDeclarationExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrExpression expression) {
        if (expression instanceof QinIrStringLiteral stringLiteral) {
            code.ldc(stringLiteral.value());
            return QinIrTypeRef.stringType();
        }
        if (expression instanceof QinIrNullLiteral) {
            code.aconst_null();
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (expression instanceof QinIrBooleanLiteral booleanLiteral) {
            if (booleanLiteral.value()) {
                code.iconst_1();
            } else {
                code.iconst_0();
            }
            return QinIrTypeRef.booleanType();
        }
        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            code.loadConstant(numberLiteral.value());
            return QinIrTypeRef.doubleType();
        }
        if (expression instanceof QinIrThisExpression) {
            code.aload(0);
            return QinIrTypeRef.classType(ownerDeclaration.binaryName());
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            return emitSequenceExpression(code, ownerDeclaration, method, declarationIndex, sequenceExpression);
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            return emitIdentifierReference(code, ownerDeclaration, method, identifierReference);
        }
        if (expression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            return emitPropertyAccess(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    new QinIrIdentifierReference(memberAccessExpression.objectName()),
                    memberAccessExpression.propertyName(),
                    memberAccessExpression.objectName() + "." + memberAccessExpression.propertyName());
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            return emitPropertyAccess(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    propertyAccessExpression.receiver(),
                    propertyAccessExpression.propertyName(),
                    propertyAccessExpression.receiver() + "." + propertyAccessExpression.propertyName());
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            return emitInstanceMethodCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    methodCallExpression);
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            emitObjectLiteral(code, ownerDeclaration, method, declarationIndex, objectLiteral);
            return QinIrTypeRef.classType("java.util.Map");
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return emitBuiltinCallExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression);
        }
        throw new IllegalArgumentException(
                "Unsupported declaration method return expression: " + expression.getClass().getSimpleName());
    }

    private QinIrTypeRef emitIdentifierReference(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            QinIrIdentifierReference identifierReference) {
        ParameterBinding parameterBinding = resolveParameterBinding(method, identifierReference.name());
        if (parameterBinding != null) {
            loadLocalForType(code, parameterBinding.parameter().type(), parameterBinding.localSlot(), identifierReference.name());
            return parameterBinding.parameter().type();
        }

        QinIrFieldDeclaration field = resolveField(ownerDeclaration, identifierReference.name());
        if (field != null) {
            code.aload(0);
            code.getfield(ClassDesc.of(ownerDeclaration.binaryName()), field.name(), toClassDesc(field.type()));
            return field.type();
        }

        throw new IllegalArgumentException("Unknown declaration identifier: " + identifierReference.name());
    }

    private QinIrTypeRef emitPropertyAccess(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrExpression receiverExpression,
            String propertyName,
            String debugName) {
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                receiverExpression);
        ResolvedPropertyAccess propertyAccess = resolvePropertyAccess(receiverType, propertyName, declarationIndex);
        if (propertyAccess == null) {
            throw new IllegalArgumentException("Unknown declaration member access: " + debugName);
        }
        invokeAccessor(code, propertyAccess);
        return propertyAccess.propertyType();
    }

    private QinIrTypeRef emitInstanceMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                methodCallExpression.receiver());
        ResolvedInstanceMethodCall resolvedMethod = resolveInstanceMethodCall(
                receiverType,
                methodCallExpression.methodName(),
                methodCallExpression.arguments().size(),
                declarationIndex);
        if (resolvedMethod == null) {
            throw new IllegalArgumentException(
                    "Unknown declaration instance method: "
                            + receiverType.binaryName() + "." + methodCallExpression.methodName());
        }

        for (int i = 0; i < methodCallExpression.arguments().size(); i++) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    methodCallExpression.arguments().get(i));
            coerceValueForTargetType(code, actualType, resolvedMethod.parameterTypes().get(i));
        }

        invokeMethod(code, resolvedMethod);
        return resolvedMethod.returnType();
    }

    private QinIrTypeRef emitBuiltinCallExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrBuiltinCallExpression builtinCallExpression) {
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_binary__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            return emitGlobalBuiltinObjectCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression,
                    "__qin_binary__");
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_logical__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            return emitGlobalBuiltinObjectCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression,
                    "__qin_logical__");
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_conditional__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            return emitGlobalBuiltinObjectCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression,
                    "__qin_conditional__");
        }
        QinBuiltinRegistry.BuiltinMethod builtinMethod = QinBuiltinRegistry.resolve(
                builtinCallExpression.receiverName(),
                builtinCallExpression.methodName(),
                builtinCallExpression.arguments().size()).orElse(null);
        if (builtinMethod != null) {
            return emitRegisteredBuiltinCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression,
                    builtinMethod);
        }
        throw new IllegalArgumentException(
                "Unsupported declaration builtin call: "
                        + builtinCallExpression.receiverName() + "." + builtinCallExpression.methodName());
    }

    private QinIrTypeRef emitRegisteredBuiltinCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrBuiltinCallExpression builtinCallExpression,
            QinBuiltinRegistry.BuiltinMethod builtinMethod) {
        List<QinBuiltinRegistry.BuiltinArgKind> argumentKinds = builtinMethod.argumentKinds();
        for (int i = 0; i < builtinCallExpression.arguments().size(); i++) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression.arguments().get(i));
            QinBuiltinRegistry.BuiltinArgKind argumentKind = argumentKinds.get(i);
            if (argumentKind == QinBuiltinRegistry.BuiltinArgKind.STRING) {
                coerceValueForTargetType(code, actualType, QinIrTypeRef.stringType());
            } else {
                boxValueForObjectTarget(code, actualType);
            }
        }
        code.invokestatic(
                ClassDesc.of(builtinMethod.ownerBinaryName()),
                builtinMethod.jvmMethodName(),
                builtinMethod.descriptor());
        QinIrTypeRef resultType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                builtinCallExpression);
        if (resultType.kind() != QinIrTypeKind.VOID) {
            coerceObjectResultForType(code, resultType);
        }
        return resultType;
    }

    private QinIrTypeRef emitSequenceExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrSequenceExpression sequenceExpression) {
        for (QinIrExpression leadingExpression : sequenceExpression.leadingExpressions()) {
            QinIrTypeRef leadingType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    leadingExpression);
            discardExpressionResult(code, leadingType);
        }
        return emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                sequenceExpression.resultExpression());
    }

    private QinIrTypeRef emitGlobalBuiltinObjectCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrBuiltinCallExpression builtinCallExpression,
            String runtimeMethodName) {
        for (QinIrExpression argument : builtinCallExpression.arguments()) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    argument);
            boxValueForObjectTarget(code, actualType);
        }
        code.invokestatic(
                ESM_GLOBAL_DESC,
                runtimeMethodName,
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        QinIrTypeRef resultType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                builtinCallExpression);
        coerceObjectResultForType(code, resultType);
        return resultType;
    }

    private void emitObjectLiteral(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrObjectLiteral objectLiteral) {
        code.new_(LINKED_HASH_MAP_DESC);
        code.dup();
        code.invokespecial(LINKED_HASH_MAP_DESC, "<init>", VOID_INIT);

        for (QinIrObjectProperty property : objectLiteral.properties()) {
            code.dup();
            code.ldc(property.key());
            emitDeclarationExpressionAsObject(code, ownerDeclaration, method, declarationIndex, property.value());
            code.invokevirtual(LINKED_HASH_MAP_DESC, "put", MAP_PUT_SIGNATURE);
            code.pop();
        }
    }

    private void emitReturnForType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef actualType,
            QinIrTypeRef declaredType) {
        if (declaredType.kind() == QinIrTypeKind.CLASS || declaredType.kind() == QinIrTypeKind.STRING) {
            if (actualType.kind() == QinIrTypeKind.BOOLEAN) {
                code.invokestatic(
                        ClassDesc.of("java.lang.Boolean"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/Boolean;"));
            } else if (actualType.kind() == QinIrTypeKind.INT) {
                code.invokestatic(
                        ClassDesc.of("java.lang.Integer"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;"));
            } else if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.invokestatic(
                        ClassDesc.of("java.lang.Double"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;"));
            }
            code.areturn();
            return;
        }

        switch (actualType.kind()) {
            case BOOLEAN, INT -> code.ireturn();
            case DOUBLE -> code.dreturn();
            case STRING, CLASS -> code.areturn();
            case VOID -> code.return_();
        }
    }

    private ParameterBinding resolveParameterBinding(QinIrMethodDeclaration method, String parameterName) {
        int localSlot = 1;
        for (var parameter : method.parameters()) {
            if (parameter.name().equals(parameterName)) {
                return new ParameterBinding(parameter, localSlot);
            }
            localSlot += localSlotWidth(parameter.type());
        }
        return null;
    }

    private QinIrTypeRef inferDeclarationExpressionType(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrExpression expression) {
        if (expression instanceof QinIrStringLiteral) {
            return QinIrTypeRef.stringType();
        }
        if (expression instanceof QinIrNullLiteral) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (expression instanceof QinIrBooleanLiteral) {
            return QinIrTypeRef.booleanType();
        }
        if (expression instanceof QinIrNumberLiteral) {
            return QinIrTypeRef.doubleType();
        }
        if (expression instanceof QinIrThisExpression) {
            return QinIrTypeRef.classType(ownerDeclaration.binaryName());
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            return inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    sequenceExpression.resultExpression());
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            ParameterBinding parameterBinding = resolveParameterBinding(method, identifierReference.name());
            if (parameterBinding != null) {
                return parameterBinding.parameter().type();
            }
            QinIrFieldDeclaration field = resolveField(ownerDeclaration, identifierReference.name());
            if (field != null) {
                return field.type();
            }
            throw new IllegalArgumentException("Unknown declaration identifier: " + identifierReference.name());
        }
        if (expression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            ResolvedPropertyAccess propertyAccess = resolvePropertyAccess(
                    inferDeclarationExpressionType(
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            new QinIrIdentifierReference(memberAccessExpression.objectName())),
                    memberAccessExpression.propertyName(),
                    declarationIndex);
            if (propertyAccess == null) {
                throw new IllegalArgumentException(
                        "Unknown declaration member access type: "
                                + memberAccessExpression.objectName() + "." + memberAccessExpression.propertyName());
            }
            return propertyAccess.propertyType();
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            ResolvedPropertyAccess propertyAccess = resolvePropertyAccess(
                    inferDeclarationExpressionType(
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            propertyAccessExpression.receiver()),
                    propertyAccessExpression.propertyName(),
                    declarationIndex);
            if (propertyAccess == null) {
                throw new IllegalArgumentException(
                        "Unknown declaration property access type: " + propertyAccessExpression.propertyName());
            }
            return propertyAccess.propertyType();
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    methodCallExpression.receiver());
            ResolvedInstanceMethodCall resolvedMethod = resolveInstanceMethodCall(
                    receiverType,
                    methodCallExpression.methodName(),
                    methodCallExpression.arguments().size(),
                    declarationIndex);
            if (resolvedMethod == null) {
                throw new IllegalArgumentException(
                        "Unknown declaration instance method type: "
                                + receiverType.binaryName() + "." + methodCallExpression.methodName());
            }
            return resolvedMethod.returnType();
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return inferBuiltinCallResultType(ownerDeclaration, method, declarationIndex, builtinCallExpression);
        }
        if (expression instanceof QinIrObjectLiteral) {
            return QinIrTypeRef.classType("java.util.Map");
        }
        throw new IllegalArgumentException(
                "Unsupported declaration expression type inference: " + expression.getClass().getSimpleName());
    }

    private QinIrTypeRef inferBuiltinCallResultType(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrBuiltinCallExpression builtinCallExpression) {
        if (!"Global".equals(builtinCallExpression.receiverName())) {
            QinIrTypeRef semanticType = inferBuiltinSemanticReturnType(
                    builtinCallExpression.receiverName(),
                    builtinCallExpression.methodName());
            if (semanticType != null) {
                return semanticType;
            }
            QinBuiltinRegistry.BuiltinMethod builtinMethod = QinBuiltinRegistry.resolve(
                    builtinCallExpression.receiverName(),
                    builtinCallExpression.methodName(),
                    builtinCallExpression.arguments().size()).orElse(null);
            if (builtinMethod != null) {
                return inferBuiltinMethodReturnType(builtinMethod);
            }
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if ("__qin_binary__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3
                && builtinCallExpression.arguments().get(0) instanceof QinIrStringLiteral operatorLiteral) {
            QinIrTypeRef leftType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression.arguments().get(1));
            QinIrTypeRef rightType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression.arguments().get(2));
            return switch (operatorLiteral.value()) {
                case "+" -> isStringLike(leftType) || isStringLike(rightType)
                        ? QinIrTypeRef.stringType()
                        : isNumericLike(leftType) && isNumericLike(rightType)
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
            QinIrTypeRef leftType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression.arguments().get(1));
            QinIrTypeRef rightType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression.arguments().get(2));
            return inferLogicalBuiltinResultType(operatorLiteral.value(), leftType, rightType);
        }
        if ("__qin_conditional__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            QinIrTypeRef consequentType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression.arguments().get(1));
            QinIrTypeRef alternateType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    builtinCallExpression.arguments().get(2));
            return mergeBranchTypes(consequentType, alternateType);
        }
        QinIrTypeRef semanticType = inferBuiltinSemanticReturnType(
                builtinCallExpression.receiverName(),
                builtinCallExpression.methodName());
        if (semanticType != null) {
            return semanticType;
        }
        QinBuiltinRegistry.BuiltinMethod builtinMethod = QinBuiltinRegistry.resolve(
                builtinCallExpression.receiverName(),
                builtinCallExpression.methodName(),
                builtinCallExpression.arguments().size()).orElse(null);
        if (builtinMethod != null) {
            return inferBuiltinMethodReturnType(builtinMethod);
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef inferBuiltinSemanticReturnType(
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

    private QinIrTypeRef inferBuiltinMethodReturnType(QinBuiltinRegistry.BuiltinMethod builtinMethod) {
        String returnDescriptor = builtinMethod.descriptor().returnType().descriptorString();
        return switch (returnDescriptor) {
            case "V" -> QinIrTypeRef.voidType();
            case "Z" -> QinIrTypeRef.booleanType();
            case "I" -> QinIrTypeRef.intType();
            case "D" -> QinIrTypeRef.doubleType();
            case "Ljava/lang/String;" -> QinIrTypeRef.stringType();
            default -> {
                if (returnDescriptor.startsWith("L") && returnDescriptor.endsWith(";")) {
                    String binaryName = returnDescriptor.substring(1, returnDescriptor.length() - 1).replace('/', '.');
                    yield QinIrTypeRef.classType(binaryName);
                }
                yield QinIrTypeRef.classType("java.lang.Object");
            }
        };
    }

    private QinIrTypeRef inferLogicalBuiltinResultType(
            String operator,
            QinIrTypeRef leftType,
            QinIrTypeRef rightType) {
        return switch (operator) {
            case "&&", "||" -> leftType.kind() == QinIrTypeKind.BOOLEAN && rightType.kind() == QinIrTypeKind.BOOLEAN
                    ? QinIrTypeRef.booleanType()
                    : mergeBranchTypes(leftType, rightType);
            case "??" -> mergeBranchTypes(leftType, rightType);
            default -> QinIrTypeRef.classType("java.lang.Object");
        };
    }

    private QinIrTypeRef mergeBranchTypes(QinIrTypeRef leftType, QinIrTypeRef rightType) {
        if (leftType.equals(rightType)) {
            return leftType;
        }
        if (isNumericLike(leftType) && isNumericLike(rightType)) {
            return QinIrTypeRef.doubleType();
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private ResolvedPropertyAccess resolvePropertyAccess(
            QinIrTypeRef ownerType,
            String propertyName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (ownerType.kind() != QinIrTypeKind.CLASS && ownerType.kind() != QinIrTypeKind.STRING) {
            return null;
        }

        QinIrClassDeclaration localDeclaration = declarationIndex.get(ownerType.binaryName());
        if (localDeclaration != null) {
            for (QinIrFieldDeclaration field : localDeclaration.fields()) {
                if (field.name().equals(propertyName)) {
                    return new ResolvedPropertyAccess(ownerType.binaryName(), getterName(field), field.type(), false);
                }
            }
            return null;
        }

        try {
            Class<?> ownerClass = Class.forName(ownerType.binaryName());
            String capitalized = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
            try {
                var getter = ownerClass.getMethod("get" + capitalized);
                return new ResolvedPropertyAccess(
                        ownerClass.getName(),
                        getter.getName(),
                        toQinTypeRef(getter.getReturnType()),
                        ownerClass.isInterface());
            } catch (NoSuchMethodException ignored) {
                var getter = ownerClass.getMethod("is" + capitalized);
                return new ResolvedPropertyAccess(
                        ownerClass.getName(),
                        getter.getName(),
                        toQinTypeRef(getter.getReturnType()),
                        ownerClass.isInterface());
            }
        } catch (Throwable ignored) {
            return null;
        }
    }

    private ResolvedInstanceMethodCall resolveInstanceMethodCall(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (ownerType.kind() != QinIrTypeKind.CLASS && ownerType.kind() != QinIrTypeKind.STRING) {
            return null;
        }

        QinIrClassDeclaration localDeclaration = declarationIndex.get(ownerType.binaryName());
        if (localDeclaration != null) {
            QinIrMethodDeclaration matched = null;
            for (QinIrMethodDeclaration candidate : localDeclaration.methods()) {
                if (!candidate.name().equals(methodName) || candidate.parameters().size() != argumentCount) {
                    continue;
                }
                if (matched != null) {
                    throw new IllegalArgumentException(
                            "Ambiguous declaration method overload: " + ownerType.binaryName() + "." + methodName);
                }
                matched = candidate;
            }
            if (matched == null) {
                return null;
            }
            List<QinIrTypeRef> parameterTypes = new ArrayList<>();
            for (var parameter : matched.parameters()) {
                parameterTypes.add(parameter.type());
            }
            return new ResolvedInstanceMethodCall(
                    ownerType.binaryName(),
                    matched.name(),
                    List.copyOf(parameterTypes),
                    matched.returnType(),
                    false);
        }

        try {
            Class<?> ownerClass = Class.forName(ownerType.binaryName());
            java.lang.reflect.Method matched = null;
            for (java.lang.reflect.Method candidate : ownerClass.getMethods()) {
                if (!candidate.getName().equals(methodName) || candidate.getParameterCount() != argumentCount) {
                    continue;
                }
                if (matched != null) {
                    throw new IllegalArgumentException(
                            "Ambiguous reflected method overload: " + ownerType.binaryName() + "." + methodName);
                }
                matched = candidate;
            }
            if (matched == null) {
                return null;
            }
            List<QinIrTypeRef> parameterTypes = new ArrayList<>();
            for (Class<?> parameterType : matched.getParameterTypes()) {
                parameterTypes.add(toQinTypeRef(parameterType));
            }
            return new ResolvedInstanceMethodCall(
                    ownerClass.getName(),
                    matched.getName(),
                    List.copyOf(parameterTypes),
                    toQinTypeRef(matched.getReturnType()),
                    ownerClass.isInterface());
        } catch (Throwable ignored) {
            return null;
        }
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

    private QinIrFieldDeclaration resolveField(QinIrClassDeclaration declaration, String fieldName) {
        for (QinIrFieldDeclaration field : declaration.fields()) {
            if (field.name().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    private void emitAllArgsConstructorBody(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration declaration,
            String ownerBinaryName) {
        code.aload(0);
        code.invokespecial(resolveSuperclass(declaration.superType()), "<init>", VOID_INIT);

        int localIndex = 1;
        for (QinIrFieldDeclaration field : declaration.fields()) {
            code.aload(0);
            loadLocalForType(code, field.type(), localIndex, field.name());
            code.putfield(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
            localIndex += localSlotWidth(field.type());
        }
        code.return_();
    }

    private void loadLocalForType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef type,
            int localIndex,
            String fieldName) {
        switch (type.kind()) {
            case BOOLEAN, INT -> code.iload(localIndex);
            case DOUBLE -> code.dload(localIndex);
            case STRING, CLASS -> code.aload(localIndex);
            case VOID -> throw new IllegalArgumentException("Field type cannot be void: " + fieldName);
        }
    }

    private int localSlotWidth(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.DOUBLE ? 2 : 1;
    }

    private MethodParametersAttribute createMethodParametersAttribute(QinIrMethodDeclaration method) {
        if (method.parameters().isEmpty()) {
            return null;
        }
        List<MethodParameterInfo> parameters = new ArrayList<>();
        for (var parameter : method.parameters()) {
            parameters.add(MethodParameterInfo.ofParameter(java.util.Optional.of(parameter.name()), 0));
        }
        return MethodParametersAttribute.of(parameters);
    }

    private MethodParametersAttribute createFieldConstructorParametersAttribute(List<QinIrFieldDeclaration> fields) {
        if (fields.isEmpty()) {
            return null;
        }
        List<MethodParameterInfo> parameters = new ArrayList<>();
        for (QinIrFieldDeclaration field : fields) {
            parameters.add(MethodParameterInfo.ofParameter(java.util.Optional.of(field.name()), 0));
        }
        return MethodParametersAttribute.of(parameters);
    }

    private RuntimeVisibleAnnotationsAttribute createAnnotationsAttribute(List<QinIrAnnotation> annotations) {
        if (annotations == null || annotations.isEmpty()) {
            return null;
        }
        List<Annotation> compiled = new ArrayList<>();
        for (QinIrAnnotation annotation : annotations) {
            compiled.add(toAnnotation(annotation));
        }
        return RuntimeVisibleAnnotationsAttribute.of(compiled);
    }

    private RuntimeVisibleParameterAnnotationsAttribute createParameterAnnotationsAttribute(
            QinIrMethodDeclaration method) {
        if (method.parameters().isEmpty()) {
            return null;
        }

        List<List<Annotation>> parameterAnnotations = new ArrayList<>();
        boolean hasAnyAnnotation = false;
        for (var parameter : method.parameters()) {
            List<Annotation> compiled = new ArrayList<>();
            for (QinIrAnnotation annotation : parameter.annotations()) {
                compiled.add(toAnnotation(annotation));
            }
            if (!compiled.isEmpty()) {
                hasAnyAnnotation = true;
            }
            parameterAnnotations.add(List.copyOf(compiled));
        }
        return hasAnyAnnotation
                ? RuntimeVisibleParameterAnnotationsAttribute.of(parameterAnnotations)
                : null;
    }

    private Annotation toAnnotation(QinIrAnnotation annotation) {
        List<AnnotationElement> elements = new ArrayList<>();
        for (QinIrAnnotationArgument argument : annotation.arguments()) {
            elements.add(toAnnotationElement(annotation.ownerBinaryName(), argument));
        }
        return Annotation.of(ClassDesc.of(annotation.ownerBinaryName()), elements);
    }

    private AnnotationElement toAnnotationElement(String annotationOwnerBinaryName, QinIrAnnotationArgument argument) {
        QinIrExpression value = argument.value();
        if (value instanceof QinIrArrayLiteral arrayLiteral) {
            List<AnnotationValue> values = new ArrayList<>();
            for (QinIrExpression element : arrayLiteral.elements()) {
                values.add(toAnnotationValue(element));
            }
            return AnnotationElement.ofArray(argument.name(), values.toArray(AnnotationValue[]::new));
        }
        Class<?> elementType = resolveAnnotationElementType(annotationOwnerBinaryName, argument.name());
        if (elementType != null && elementType.isArray()) {
            return AnnotationElement.ofArray(argument.name(), new AnnotationValue[]{toAnnotationValue(value)});
        }
        return AnnotationElement.of(argument.name(), toAnnotationValue(value));
    }

    private Class<?> resolveAnnotationElementType(String annotationOwnerBinaryName, String elementName) {
        try {
            Class<?> annotationClass = Class.forName(annotationOwnerBinaryName);
            if (!annotationClass.isAnnotation()) {
                return null;
            }
            return annotationClass.getMethod(elementName).getReturnType();
        } catch (Throwable ignored) {
            return null;
        }
    }

    private record ParameterBinding(
            com.qin.lang.ir.QinIrParameter parameter,
            int localSlot) {
    }

    private record ResolvedPropertyAccess(
            String ownerBinaryName,
            String accessorName,
            QinIrTypeRef propertyType,
            boolean ownerInterface) {
    }

    private record ResolvedInstanceMethodCall(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> parameterTypes,
            QinIrTypeRef returnType,
            boolean ownerInterface) {
    }

    private void invokeAccessor(java.lang.classfile.CodeBuilder code, ResolvedPropertyAccess propertyAccess) {
        if (propertyAccess.ownerInterface()) {
            code.invokeinterface(
                    ClassDesc.of(propertyAccess.ownerBinaryName()),
                    propertyAccess.accessorName(),
                    MethodTypeDesc.of(toClassDesc(propertyAccess.propertyType())));
            return;
        }
        code.invokevirtual(
                ClassDesc.of(propertyAccess.ownerBinaryName()),
                propertyAccess.accessorName(),
                MethodTypeDesc.of(toClassDesc(propertyAccess.propertyType())));
    }

    private void invokeMethod(java.lang.classfile.CodeBuilder code, ResolvedInstanceMethodCall methodCall) {
        MethodTypeDesc descriptor = MethodTypeDesc.of(
                toClassDesc(methodCall.returnType()),
                methodCall.parameterTypes().stream().map(this::toClassDesc).toList());
        if (methodCall.ownerInterface()) {
            code.invokeinterface(ClassDesc.of(methodCall.ownerBinaryName()), methodCall.methodName(), descriptor);
            return;
        }
        code.invokevirtual(ClassDesc.of(methodCall.ownerBinaryName()), methodCall.methodName(), descriptor);
    }

    private void coerceValueForTargetType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef actualType,
            QinIrTypeRef targetType) {
        if (targetType.kind() == QinIrTypeKind.CLASS || targetType.kind() == QinIrTypeKind.STRING) {
            if (actualType.kind() == QinIrTypeKind.BOOLEAN) {
                code.invokestatic(
                        ClassDesc.of("java.lang.Boolean"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/Boolean;"));
            } else if (actualType.kind() == QinIrTypeKind.INT) {
                code.invokestatic(
                        ClassDesc.of("java.lang.Integer"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;"));
            } else if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.invokestatic(
                        ClassDesc.of("java.lang.Double"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;"));
            } else if (actualType.kind() == QinIrTypeKind.CLASS || actualType.kind() == QinIrTypeKind.STRING) {
                String targetBinaryName = targetType.kind() == QinIrTypeKind.STRING
                        ? "java.lang.String"
                        : targetType.binaryName();
                String actualBinaryName = actualType.kind() == QinIrTypeKind.STRING
                        ? "java.lang.String"
                        : actualType.binaryName();
                if (targetBinaryName != null
                        && actualBinaryName != null
                        && !targetBinaryName.equals(actualBinaryName)
                        && !"java.lang.Object".equals(targetBinaryName)) {
                    code.checkcast(ClassDesc.of(targetBinaryName));
                } else if (targetType.kind() == QinIrTypeKind.STRING
                        && !"java.lang.String".equals(actualBinaryName)) {
                    code.checkcast(ClassDesc.of("java.lang.String"));
                }
            }
            return;
        }

        if (actualType.kind() != targetType.kind()) {
            throw new IllegalArgumentException(
                    "Unsupported declaration argument coercion: " + actualType.kind() + " -> " + targetType.kind());
        }
    }

    private void boxValueForObjectTarget(java.lang.classfile.CodeBuilder code, QinIrTypeRef actualType) {
        if (actualType.kind() == QinIrTypeKind.BOOLEAN) {
            code.invokestatic(
                    BOOLEAN_DESC,
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/Boolean;"));
            return;
        }
        if (actualType.kind() == QinIrTypeKind.INT) {
            code.invokestatic(
                    ClassDesc.of("java.lang.Integer"),
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;"));
            return;
        }
        if (actualType.kind() == QinIrTypeKind.DOUBLE) {
            code.invokestatic(
                    ClassDesc.of("java.lang.Double"),
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;"));
        }
    }

    private void discardExpressionResult(java.lang.classfile.CodeBuilder code, QinIrTypeRef actualType) {
        if (actualType.kind() == QinIrTypeKind.VOID) {
            return;
        }
        if (actualType.kind() == QinIrTypeKind.DOUBLE) {
            code.pop2();
            return;
        }
        code.pop();
    }

    private void emitDeclarationExpressionAsObject(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrExpression expression) {
        QinIrTypeRef actualType = emitDeclarationExpression(code, ownerDeclaration, method, declarationIndex, expression);
        boxValueForObjectTarget(code, actualType);
    }

    private void coerceObjectResultForType(java.lang.classfile.CodeBuilder code, QinIrTypeRef resultType) {
        switch (resultType.kind()) {
            case STRING -> code.checkcast(STRING_DESC);
            case BOOLEAN -> {
                code.checkcast(BOOLEAN_DESC);
                code.invokevirtual(BOOLEAN_DESC, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"));
            }
            case INT -> {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "intValue", MethodTypeDesc.ofDescriptor("()I"));
            }
            case DOUBLE -> {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "doubleValue", MethodTypeDesc.ofDescriptor("()D"));
            }
            case CLASS -> {
                if (!"java.lang.Object".equals(resultType.binaryName())) {
                    code.checkcast(ClassDesc.of(resultType.binaryName()));
                }
            }
            case VOID -> {
            }
        }
    }

    private boolean isNumericLike(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.INT || type.kind() == QinIrTypeKind.DOUBLE;
    }

    private boolean isStringLike(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.STRING;
    }

    private AnnotationValue toAnnotationValue(QinIrExpression value) {
        if (value instanceof QinIrStringLiteral stringLiteral) {
            return AnnotationValue.ofString(stringLiteral.value());
        }
        if (value instanceof QinIrBooleanLiteral booleanLiteral) {
            return AnnotationValue.ofBoolean(booleanLiteral.value());
        }
        if (value instanceof QinIrNumberLiteral numberLiteral) {
            if (Math.rint(numberLiteral.value()) == numberLiteral.value()) {
                return AnnotationValue.ofInt((int) numberLiteral.value());
            }
            return AnnotationValue.ofDouble(numberLiteral.value());
        }
        throw new IllegalArgumentException(
                "Unsupported annotation value expression: " + (value == null ? "null" : value.getClass().getSimpleName()));
    }
}
