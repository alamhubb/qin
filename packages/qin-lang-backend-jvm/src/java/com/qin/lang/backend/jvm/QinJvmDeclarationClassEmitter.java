package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrContinueStatement;
import com.qin.lang.ir.QinIrDoWhileStatementNode;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrForEachStatement;
import com.qin.lang.ir.QinIrForStatement;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrLocalVariableDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrStatement;
import com.qin.lang.ir.QinIrStatementExpression;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrSwitchCase;
import com.qin.lang.ir.QinIrSwitchStatement;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrThrowStatement;
import com.qin.lang.ir.QinIrTryStatement;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrCatchClause;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrLocalDeclarationStatement;
import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinIrWhileStatementNode;
import com.qin.lang.ir.QinBuiltinRegistry;

import java.lang.classfile.Annotation;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.AnnotationValue;
import java.lang.classfile.ClassFile;
import java.lang.classfile.attribute.MethodParameterInfo;
import java.lang.classfile.attribute.MethodParametersAttribute;
import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.lang.classfile.attribute.RuntimeVisibleParameterAnnotationsAttribute;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
    private static final ClassDesc OBJECT_ARRAY_DESC = ClassDesc.ofDescriptor("[Ljava/lang/Object;");
    private static final ClassDesc ITERABLE_DESC = ClassDesc.of("java.lang.Iterable");
    private static final ClassDesc ITERATOR_DESC = ClassDesc.of("java.util.Iterator");
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

            Set<String> emittedConstructorDescriptors = new LinkedHashSet<>();
            if (hasNoArgSuperclassConstructor(declaration.superType(), declarationIndex)) {
                emittedConstructorDescriptors.add(VOID_INIT.descriptorString());
                builder.withMethodBody("<init>", VOID_INIT, ClassFile.ACC_PUBLIC, code -> {
                    code.aload(0);
                    code.invokespecial(resolveSuperclass(declaration.superType()), "<init>", VOID_INIT);
                    for (QinIrFieldDeclaration field : declaration.fields()) {
                        emitFieldInitializer(code, binaryClassName, field);
                    }
                    code.return_();
                });
            }

            emitJavaSuperclassConstructors(builder, declaration, binaryClassName);
            emitLocalSuperclassConstructors(
                    builder,
                    declaration,
                    binaryClassName,
                    declarationIndex,
                    emittedConstructorDescriptors);

            if (!declaration.fields().isEmpty() && hasNoArgSuperclassConstructor(declaration.superType(), declarationIndex)) {
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

    private boolean hasNoArgSuperclassConstructor(QinIrTypeRef superType) {
        if (superType == null
                || superType.binaryName() == null
                || superType.binaryName().isBlank()
                || "java.lang.Object".equals(superType.binaryName())) {
            return true;
        }
        try {
            Class.forName(superType.binaryName()).getConstructor();
            return true;
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    private boolean hasNoArgSuperclassConstructor(
            QinIrTypeRef superType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (hasNoArgSuperclassConstructor(superType)) {
            return true;
        }
        QinIrClassDeclaration localSuperclass = superType == null ? null : declarationIndex.get(superType.binaryName());
        if (localSuperclass == null) {
            return false;
        }
        return constructorParameterListsForLocalDeclaration(
                localSuperclass,
                declarationIndex,
                new java.util.LinkedHashSet<>()).stream().anyMatch(List::isEmpty);
    }

    private void emitJavaSuperclassConstructors(
            ClassBuilder builder,
            QinIrClassDeclaration declaration,
            String binaryClassName) {
        if (declaration.superType() == null
                || declaration.superType().binaryName() == null
                || declaration.superType().binaryName().isBlank()
                || "java.lang.Object".equals(declaration.superType().binaryName())) {
            return;
        }
        Class<?> superClass;
        try {
            superClass = Class.forName(declaration.superType().binaryName());
        } catch (ReflectiveOperationException ignored) {
            return;
        }
        for (Constructor<?> constructor : superClass.getConstructors()) {
            if (constructor.getParameterCount() == 0 || !isSupportedPassThroughConstructor(constructor)) {
                continue;
            }
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            builder.withMethodBody(
                    "<init>",
                    toJavaConstructorDescriptor(parameterTypes),
                    ClassFile.ACC_PUBLIC,
                    code -> {
                        code.aload(0);
                        int localSlot = 1;
                        for (Class<?> parameterType : parameterTypes) {
                            loadJavaLocal(code, parameterType, localSlot);
                            localSlot += javaLocalSlotWidth(parameterType);
                        }
                        code.invokespecial(
                                ClassDesc.of(superClass.getName()),
                                "<init>",
                                toJavaConstructorDescriptor(parameterTypes));
                        for (QinIrFieldDeclaration field : declaration.fields()) {
                            emitFieldInitializer(code, binaryClassName, field);
                        }
                        code.return_();
            });
        }
    }

    private void emitLocalSuperclassConstructors(
            ClassBuilder builder,
            QinIrClassDeclaration declaration,
            String binaryClassName,
            Map<String, QinIrClassDeclaration> declarationIndex,
            Set<String> emittedDescriptors) {
        QinIrClassDeclaration localSuperclass = declaration.superType() == null
                ? null
                : declarationIndex.get(declaration.superType().binaryName());
        if (localSuperclass == null) {
            return;
        }
        for (List<QinIrTypeRef> parameterTypes : constructorParameterListsForLocalDeclaration(
                localSuperclass,
                declarationIndex,
                new java.util.LinkedHashSet<>())) {
            MethodTypeDesc descriptor = toConstructorDescriptorForTypes(parameterTypes);
            if (!emittedDescriptors.add(descriptor.descriptorString())) {
                continue;
            }
            builder.withMethodBody(
                    "<init>",
                    descriptor,
                    ClassFile.ACC_PUBLIC,
                    code -> {
                        code.aload(0);
                        int localSlot = 1;
                        for (QinIrTypeRef parameterType : parameterTypes) {
                            loadLocalForType(code, parameterType, localSlot, "super");
                            localSlot += localSlotWidth(parameterType);
                        }
                        code.invokespecial(
                                ClassDesc.of(localSuperclass.binaryName()),
                                "<init>",
                                descriptor);
                        for (QinIrFieldDeclaration field : declaration.fields()) {
                            emitFieldInitializer(code, binaryClassName, field);
                        }
                        code.return_();
                    });
        }
    }

    private List<List<QinIrTypeRef>> constructorParameterListsForLocalDeclaration(
            QinIrClassDeclaration declaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            java.util.Set<String> visitedLocalTypes) {
        if (declaration == null || !visitedLocalTypes.add(declaration.binaryName())) {
            return List.of();
        }
        List<List<QinIrTypeRef>> constructors = new ArrayList<>();
        if (hasNoArgSuperclassConstructor(declaration.superType(), declarationIndex)) {
            constructors.add(List.of());
        }
        constructors.addAll(javaSuperclassConstructorParameterLists(declaration.superType()));
        QinIrClassDeclaration localSuperclass = declaration.superType() == null
                ? null
                : declarationIndex.get(declaration.superType().binaryName());
        constructors.addAll(constructorParameterListsForLocalDeclaration(
                localSuperclass,
                declarationIndex,
                visitedLocalTypes));
        if (!declaration.fields().isEmpty() && hasNoArgSuperclassConstructor(declaration.superType(), declarationIndex)) {
            List<QinIrTypeRef> fieldTypes = new ArrayList<>();
            for (QinIrFieldDeclaration field : declaration.fields()) {
                fieldTypes.add(field.type());
            }
            constructors.add(List.copyOf(fieldTypes));
        }
        return List.copyOf(constructors);
    }

    private List<List<QinIrTypeRef>> javaSuperclassConstructorParameterLists(QinIrTypeRef superType) {
        if (superType == null
                || superType.binaryName() == null
                || superType.binaryName().isBlank()
                || "java.lang.Object".equals(superType.binaryName())) {
            return List.of();
        }
        Class<?> superClass;
        try {
            superClass = Class.forName(superType.binaryName());
        } catch (ReflectiveOperationException ignored) {
            return List.of();
        }
        List<List<QinIrTypeRef>> constructors = new ArrayList<>();
        for (Constructor<?> constructor : superClass.getConstructors()) {
            if (constructor.getParameterCount() == 0 || !isSupportedPassThroughConstructor(constructor)) {
                continue;
            }
            List<QinIrTypeRef> parameterTypes = new ArrayList<>();
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                parameterTypes.add(toQinTypeRef(parameterType));
            }
            constructors.add(List.copyOf(parameterTypes));
        }
        return List.copyOf(constructors);
    }

    private boolean isSupportedPassThroughConstructor(Constructor<?> constructor) {
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            if (parameterType == long.class || parameterType == float.class || parameterType == short.class
                    || parameterType == byte.class || parameterType == char.class) {
                return false;
            }
        }
        return true;
    }

    private MethodTypeDesc toJavaConstructorDescriptor(Class<?>[] parameterTypes) {
        List<ClassDesc> parameterDescs = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            parameterDescs.add(toClassDesc(parameterType));
        }
        return MethodTypeDesc.ofDescriptor(
                MethodTypeDesc.of(ClassDesc.ofDescriptor("V"), parameterDescs).descriptorString());
    }

    private ClassDesc toClassDesc(Class<?> type) {
        if (type == void.class) {
            return ClassDesc.ofDescriptor("V");
        }
        if (type == boolean.class) {
            return ClassDesc.ofDescriptor("Z");
        }
        if (type == int.class) {
            return ClassDesc.ofDescriptor("I");
        }
        if (type == double.class) {
            return ClassDesc.ofDescriptor("D");
        }
        if (type.isArray()) {
            return ClassDesc.ofDescriptor(type.descriptorString());
        }
        return ClassDesc.of(type.getName());
    }

    private void loadJavaLocal(java.lang.classfile.CodeBuilder code, Class<?> type, int localSlot) {
        if (type == boolean.class || type == int.class) {
            code.iload(localSlot);
        } else if (type == double.class) {
            code.dload(localSlot);
        } else {
            code.aload(localSlot);
        }
    }

    private int javaLocalSlotWidth(Class<?> type) {
        return type == double.class || type == long.class ? 2 : 1;
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

    private MethodTypeDesc toConstructorDescriptorForTypes(List<QinIrTypeRef> parameterTypes) {
        List<ClassDesc> parameterDescs = new ArrayList<>();
        for (QinIrTypeRef parameterType : parameterTypes) {
            parameterDescs.add(toClassDesc(parameterType));
        }
        return MethodTypeDesc.ofDescriptor(
                MethodTypeDesc.of(ClassDesc.ofDescriptor("V"), parameterDescs).descriptorString());
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
        if (method.runtimeFunctionDefinition() != null) {
            emitRuntimeFunctionMethodBody(code, ownerDeclaration, method, declarationIndex);
            return;
        }
        if (!method.bodyStatements().isEmpty()) {
            emitStatements(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    LocalFrame.forMethodParameters(method),
                    method.bodyStatements());
            emitDefaultReturn(code, method.returnType());
            return;
        }
        QinIrExpression returnExpression = method.returnExpression();
        if (returnExpression == null || returnExpression instanceof QinIrNullLiteral) {
            emitDefaultReturn(code, method.returnType());
            return;
        }

        QinIrTypeRef actualType = emitDeclarationExpression(code, ownerDeclaration, method, declarationIndex, returnExpression);
        emitReturnForType(code, actualType, method.returnType());
    }

    private void emitStatements(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            List<QinIrStatement> statements) {
        for (QinIrStatement statement : statements) {
            emitStatement(code, ownerDeclaration, method, declarationIndex, localFrame, statement);
        }
    }

    private void emitStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrStatement statement) {
        if (statement instanceof QinIrLocalDeclarationStatement localDeclaration) {
            emitLocalDeclarationStatement(code, ownerDeclaration, method, declarationIndex, localFrame, localDeclaration);
            return;
        }
        if (statement instanceof QinIrReturnStatement returnStatement) {
            QinIrExpression value = returnStatement.value();
            if (value == null || value instanceof QinIrNullLiteral) {
                emitDefaultReturn(code, method.returnType());
                return;
            }
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    value);
            emitReturnForType(code, actualType, method.returnType());
            return;
        }
        if (statement instanceof QinIrStatementExpression statementExpression) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    statementExpression.expression());
            discardExpressionResult(code, actualType);
            return;
        }
        if (statement instanceof QinIrIfStatement ifStatement) {
            java.lang.classfile.Label alternateLabel = code.newLabel();
            java.lang.classfile.Label doneLabel = code.newLabel();
            QinIrTypeRef testType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    ifStatement.test());
            if (testType.kind() != QinIrTypeKind.BOOLEAN) {
                throw new IllegalArgumentException("Declaration if statement test must be boolean");
            }
            code.ifeq(alternateLabel);
            emitStatements(code, ownerDeclaration, method, declarationIndex, localFrame, ifStatement.consequent());
            code.goto_(doneLabel);
            code.labelBinding(alternateLabel);
            emitStatements(code, ownerDeclaration, method, declarationIndex, localFrame, ifStatement.alternate());
            code.labelBinding(doneLabel);
            return;
        }
        if (statement instanceof QinIrThrowStatement throwStatement) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    throwStatement.value());
            if (actualType.kind() != QinIrTypeKind.CLASS
                    || actualType.binaryName() == null
                    || !Throwable.class.isAssignableFrom(resolveClass(actualType.binaryName()))) {
                throw new IllegalArgumentException("Declaration throw statement requires a java.lang.Throwable value");
            }
            code.athrow();
            return;
        }
        if (statement instanceof QinIrTryStatement tryStatement) {
            emitTryStatement(code, ownerDeclaration, method, declarationIndex, localFrame, tryStatement);
            return;
        }
        if (statement instanceof QinIrSwitchStatement switchStatement) {
            emitSwitchStatement(code, ownerDeclaration, method, declarationIndex, localFrame, switchStatement);
            return;
        }
        if (statement instanceof QinIrWhileStatementNode whileStatement) {
            emitWhileStatement(code, ownerDeclaration, method, declarationIndex, localFrame, whileStatement);
            return;
        }
        if (statement instanceof QinIrForStatement forStatement) {
            emitForStatement(code, ownerDeclaration, method, declarationIndex, localFrame, forStatement);
            return;
        }
        if (statement instanceof QinIrForEachStatement forEachStatement) {
            emitForEachStatement(code, ownerDeclaration, method, declarationIndex, localFrame, forEachStatement);
            return;
        }
        if (statement instanceof QinIrDoWhileStatementNode doWhileStatement) {
            emitDoWhileStatement(code, ownerDeclaration, method, declarationIndex, localFrame, doWhileStatement);
            return;
        }
        if (statement instanceof QinIrBreakStatement breakStatement) {
            emitBreakStatement(code, localFrame, breakStatement);
            return;
        }
        if (statement instanceof QinIrContinueStatement continueStatement) {
            emitContinueStatement(code, localFrame, continueStatement);
            return;
        }
        throw new IllegalArgumentException(
                "Unsupported declaration method statement: " + statement.getClass().getSimpleName());
    }

    private void emitWhileStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrWhileStatementNode whileStatement) {
        java.lang.classfile.Label startLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        LocalFrame loopFrame = localFrame.withLoop(new LoopBinding(startLabel, doneLabel));
        code.labelBinding(startLabel);
        QinIrTypeRef testType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                loopFrame,
                whileStatement.test());
        if (testType.kind() != QinIrTypeKind.BOOLEAN) {
            throw new IllegalArgumentException("Declaration while statement test must be boolean");
        }
        code.ifeq(doneLabel);
        emitStatements(code, ownerDeclaration, method, declarationIndex, loopFrame, whileStatement.body());
        code.goto_(startLabel);
        code.labelBinding(doneLabel);
    }

    private void emitDoWhileStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrDoWhileStatementNode doWhileStatement) {
        java.lang.classfile.Label startLabel = code.newLabel();
        java.lang.classfile.Label testLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        LocalFrame loopFrame = localFrame.withLoop(new LoopBinding(testLabel, doneLabel));
        code.labelBinding(startLabel);
        emitStatements(code, ownerDeclaration, method, declarationIndex, loopFrame, doWhileStatement.body());
        code.labelBinding(testLabel);
        QinIrTypeRef testType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                loopFrame,
                doWhileStatement.test());
        if (testType.kind() != QinIrTypeKind.BOOLEAN) {
            throw new IllegalArgumentException("Declaration do-while statement test must be boolean");
        }
        code.ifne(startLabel);
        code.labelBinding(doneLabel);
    }

    private void emitSwitchStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrSwitchStatement switchStatement) {
        LocalFrame switchFrame = localFrame.child();
        QinIrTypeRef discriminantType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                switchFrame,
                switchStatement.discriminant());
        LocalBinding discriminantBinding = switchFrame.declare(
                switchFrame.syntheticLocalName("__qin_switch_discriminant"),
                discriminantType);
        storeLocalForType(
                code,
                discriminantBinding.type(),
                discriminantBinding.localSlot(),
                discriminantBinding.name());

        java.lang.classfile.Label doneLabel = code.newLabel();
        java.lang.classfile.Label defaultLabel = null;
        List<java.lang.classfile.Label> caseLabels = new ArrayList<>();
        for (QinIrSwitchCase switchCase : switchStatement.cases()) {
            java.lang.classfile.Label caseLabel = code.newLabel();
            caseLabels.add(caseLabel);
            if (switchCase.isDefault()) {
                if (defaultLabel != null) {
                    throw new IllegalArgumentException("Declaration switch statement cannot contain multiple defaults");
                }
                defaultLabel = caseLabel;
                continue;
            }
            code.ldc("===");
            loadLocalForType(
                    code,
                    discriminantBinding.type(),
                    discriminantBinding.localSlot(),
                    discriminantBinding.name());
            boxValueForObjectTarget(code, discriminantBinding.type());
            QinIrTypeRef testType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    switchFrame,
                    switchCase.test());
            boxValueForObjectTarget(code, testType);
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_binary__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
            coerceObjectResultForType(code, QinIrTypeRef.booleanType());
            code.ifne(caseLabel);
        }
        code.goto_(defaultLabel == null ? doneLabel : defaultLabel);

        LocalFrame caseFrame = switchFrame.withSwitchBreak(doneLabel);
        for (int i = 0; i < switchStatement.cases().size(); i++) {
            code.labelBinding(caseLabels.get(i));
            emitStatements(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    caseFrame,
                    switchStatement.cases().get(i).consequent());
        }
        code.labelBinding(doneLabel);
    }

    private void emitForStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrForStatement forStatement) {
        LocalFrame forFrame = localFrame.child();
        for (QinIrLocalVariableDeclaration initializerDeclaration : forStatement.initializerDeclarations()) {
            QinIrTypeRef initializerType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    forFrame,
                    initializerDeclaration.initializer());
            LocalBinding binding = forFrame.declare(initializerDeclaration.name(), initializerType);
            storeLocalForType(code, binding.type(), binding.localSlot(), binding.name());
        }
        for (QinIrExpression initializerExpression : forStatement.initializerExpressions()) {
            QinIrTypeRef initializerType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    forFrame,
                    initializerExpression);
            discardExpressionResult(code, initializerType);
        }

        java.lang.classfile.Label startLabel = code.newLabel();
        java.lang.classfile.Label updateLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        LocalFrame loopFrame = forFrame.withLoop(new LoopBinding(updateLabel, doneLabel));
        code.labelBinding(startLabel);
        if (forStatement.test() != null) {
            QinIrTypeRef testType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    loopFrame,
                    forStatement.test());
            if (testType.kind() != QinIrTypeKind.BOOLEAN) {
                throw new IllegalArgumentException("Declaration for statement test must be boolean");
            }
            code.ifeq(doneLabel);
        }
        emitStatements(code, ownerDeclaration, method, declarationIndex, loopFrame, forStatement.body());
        code.labelBinding(updateLabel);
        for (QinIrExpression updateExpression : forStatement.updateExpressions()) {
            QinIrTypeRef updateType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    loopFrame,
                    updateExpression);
            discardExpressionResult(code, updateType);
        }
        code.goto_(startLabel);
        code.labelBinding(doneLabel);
    }

    private void emitForEachStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrForEachStatement forEachStatement) {
        LocalFrame forFrame = localFrame.child();
        LocalBinding iteratorBinding = forFrame.declare(
                "__qin_iter_" + forEachStatement.itemName(),
                QinIrTypeRef.classType("java.util.Iterator"));
        QinIrTypeRef iterableType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                forFrame,
                forEachStatement.iterable());
        if (!isIterableType(iterableType)) {
            throw new IllegalArgumentException(
                    "Declaration for...of iterable must implement java.lang.Iterable: " + iterableType);
        }
        code.invokeinterface(ITERABLE_DESC, "iterator", MethodTypeDesc.of(ITERATOR_DESC));
        storeLocalForType(code, iteratorBinding.type(), iteratorBinding.localSlot(), iteratorBinding.name());

        java.lang.classfile.Label testLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        LocalFrame bodyFrame = forFrame.withLoop(new LoopBinding(testLabel, doneLabel));
        LocalBinding itemBinding = bodyFrame.declare(forEachStatement.itemName(), QinIrTypeRef.classType("java.lang.Object"));
        code.labelBinding(testLabel);
        loadLocalForType(code, iteratorBinding.type(), iteratorBinding.localSlot(), iteratorBinding.name());
        code.invokeinterface(ITERATOR_DESC, "hasNext", MethodTypeDesc.ofDescriptor("()Z"));
        code.ifeq(doneLabel);
        loadLocalForType(code, iteratorBinding.type(), iteratorBinding.localSlot(), iteratorBinding.name());
        code.invokeinterface(ITERATOR_DESC, "next", MethodTypeDesc.of(OBJECT_DESC));
        storeLocalForType(code, itemBinding.type(), itemBinding.localSlot(), itemBinding.name());
        emitStatements(code, ownerDeclaration, method, declarationIndex, bodyFrame, forEachStatement.body());
        code.goto_(testLabel);
        code.labelBinding(doneLabel);
    }

    private boolean isIterableType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        try {
            return Iterable.class.isAssignableFrom(resolveClass(type.binaryName()));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private void emitBreakStatement(
            java.lang.classfile.CodeBuilder code,
            LocalFrame localFrame,
            QinIrBreakStatement breakStatement) {
        if (breakStatement.label() != null && !breakStatement.label().isBlank()) {
            throw new IllegalArgumentException("Declaration labeled break is not supported yet: " + breakStatement.label());
        }
        java.lang.classfile.Label breakLabel = localFrame.breakLabel();
        if (breakLabel == null) {
            throw new IllegalArgumentException("Declaration break statement must be inside a loop or switch");
        }
        code.goto_(breakLabel);
    }

    private void emitContinueStatement(
            java.lang.classfile.CodeBuilder code,
            LocalFrame localFrame,
            QinIrContinueStatement continueStatement) {
        if (continueStatement.label() != null && !continueStatement.label().isBlank()) {
            throw new IllegalArgumentException(
                    "Declaration labeled continue is not supported yet: " + continueStatement.label());
        }
        LoopBinding loopBinding = localFrame.loop();
        if (loopBinding == null) {
            throw new IllegalArgumentException("Declaration continue statement must be inside a loop");
        }
        code.goto_(loopBinding.continueLabel());
    }

    private void emitLocalDeclarationStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrLocalDeclarationStatement localDeclaration) {
        QinIrTypeRef initializerType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                localDeclaration.initializer());
        LocalBinding binding = localFrame.declare(localDeclaration.name(), initializerType);
        storeLocalForType(code, binding.type(), binding.localSlot(), binding.name());
    }

    private void emitTryStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrTryStatement tryStatement) {
        if (!tryStatement.finallyBody().isEmpty()) {
            throw new IllegalArgumentException("Declaration try statement with non-empty finally is not supported yet");
        }
        if (tryStatement.catchClauses().size() > 1) {
            throw new IllegalArgumentException("Declaration try statement supports one catch clause");
        }

        java.lang.classfile.Label tryStart = code.newLabel();
        java.lang.classfile.Label tryEnd = code.newLabel();
        java.lang.classfile.Label done = code.newLabel();
        code.labelBinding(tryStart);
        emitStatements(code, ownerDeclaration, method, declarationIndex, localFrame, tryStatement.tryBody());
        code.labelBinding(tryEnd);
        code.goto_(done);

        if (!tryStatement.catchClauses().isEmpty()) {
            QinIrCatchClause catchClause = tryStatement.catchClauses().get(0);
            java.lang.classfile.Label handler = code.newLabel();
            code.labelBinding(handler);
            code.pop();
            emitStatements(code, ownerDeclaration, method, declarationIndex, localFrame, catchClause.body());
            code.exceptionCatch(tryStart, tryEnd, handler, toCatchClassDesc(catchClause.parameterType()));
        }
        code.labelBinding(done);
    }

    private ClassDesc toCatchClassDesc(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return ClassDesc.of("java.lang.Throwable");
        }
        Class<?> catchClass = resolveClass(type.binaryName());
        if (!Throwable.class.isAssignableFrom(catchClass)) {
            throw new IllegalArgumentException("Catch type must extend java.lang.Throwable: " + type.binaryName());
        }
        return ClassDesc.of(type.binaryName());
    }

    private void emitDefaultReturn(java.lang.classfile.CodeBuilder code, QinIrTypeRef returnType) {
        switch (returnType.kind()) {
            case VOID -> code.return_();
            case BOOLEAN, INT -> {
                code.iconst_0();
                code.ireturn();
            }
            case DOUBLE -> {
                code.dconst_0();
                code.dreturn();
            }
            case STRING, CLASS -> {
                code.aconst_null();
                code.areturn();
            }
        }
    }

    private void emitRuntimeFunctionMethodBody(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        emitObjectLiteral(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                LocalFrame.forMethodParameters(method),
                method.runtimeFunctionDefinition());
        code.aload(0);
        emitObjectArrayFromParameters(code, method);
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_call_function_definition__",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC, OBJECT_ARRAY_DESC));
        coerceObjectResultForType(code, method.returnType());
        emitObjectCoercedReturn(code, method.returnType());
    }

    private void emitObjectArrayFromParameters(
            java.lang.classfile.CodeBuilder code,
            QinIrMethodDeclaration method) {
        code.loadConstant(method.parameters().size());
        code.anewarray(OBJECT_DESC);
        int localSlot = 1;
        for (int i = 0; i < method.parameters().size(); i++) {
            var parameter = method.parameters().get(i);
            code.dup();
            code.loadConstant(i);
            loadLocalForType(code, parameter.type(), localSlot, parameter.name());
            boxValueForObjectTarget(code, parameter.type());
            code.aastore();
            localSlot += localSlotWidth(parameter.type());
        }
    }

    private void emitObjectArrayFromExpressions(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            List<QinIrExpression> expressions) {
        code.loadConstant(expressions.size());
        code.anewarray(OBJECT_DESC);
        for (int i = 0; i < expressions.size(); i++) {
            code.dup();
            code.loadConstant(i);
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expressions.get(i));
            boxValueForObjectTarget(code, actualType);
            code.aastore();
        }
    }

    private QinIrTypeRef emitDeclarationExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrExpression expression) {
        return emitDeclarationExpression(code, ownerDeclaration, method, declarationIndex, LocalFrame.forMethodParameters(method), expression);
    }

    private QinIrTypeRef emitDeclarationExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
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
            return emitSequenceExpression(code, ownerDeclaration, method, declarationIndex, localFrame, sequenceExpression);
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            return emitIdentifierReference(code, ownerDeclaration, method, localFrame, identifierReference);
        }
        if (expression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            return emitPropertyAccess(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
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
                    localFrame,
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
                    localFrame,
                    methodCallExpression);
        }
        if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            return emitStaticMethodCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    staticMethodCallExpression);
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            return emitJavaNewExpression(code, ownerDeclaration, method, declarationIndex, localFrame, javaNewExpression);
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            emitObjectLiteral(code, ownerDeclaration, method, declarationIndex, localFrame, objectLiteral);
            return QinIrTypeRef.classType("java.util.Map");
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return emitBuiltinCallExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            return emitAssignmentExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    assignmentExpression);
        }
        throw new IllegalArgumentException(
                "Unsupported declaration method return expression: " + expression.getClass().getSimpleName());
    }

    private QinIrTypeRef emitIdentifierReference(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            LocalFrame localFrame,
            QinIrIdentifierReference identifierReference) {
        LocalBinding localBinding = localFrame.resolve(identifierReference.name());
        if (localBinding != null) {
            loadLocalForType(code, localBinding.type(), localBinding.localSlot(), identifierReference.name());
            return localBinding.type();
        }
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
            LocalFrame localFrame,
            QinIrExpression receiverExpression,
            String propertyName,
            String debugName) {
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
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
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.receiver());
        ResolvedInstanceMethodCall resolvedMethod = resolveInstanceMethodCall(
                receiverType,
                methodCallExpression.methodName(),
                methodCallExpression.arguments().size(),
                declarationIndex);
        if (resolvedMethod == null) {
            if (isDynamicObjectType(receiverType)) {
                return emitDynamicObjectMethodCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
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
                    localFrame,
                    methodCallExpression.arguments().get(i));
            coerceValueForTargetType(code, actualType, resolvedMethod.parameterTypes().get(i));
        }

        invokeMethod(code, resolvedMethod);
        return resolvedMethod.returnType();
    }

    private QinIrTypeRef emitDynamicObjectMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        code.ldc(methodCallExpression.methodName());
        emitObjectArrayFromExpressions(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments());
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_call_method_array__",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC, OBJECT_ARRAY_DESC));
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private boolean isDynamicObjectType(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.CLASS && "java.lang.Object".equals(type.binaryName());
    }

    private QinIrTypeRef emitStaticMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrStaticMethodCallExpression methodCallExpression) {
        ResolvedStaticMethodCall resolvedMethod = resolveStaticMethodCall(
                methodCallExpression.ownerBinaryName(),
                methodCallExpression.methodName(),
                methodCallExpression.arguments().size());
        if (resolvedMethod == null) {
            throw new IllegalArgumentException(
                    "Unknown declaration static method: "
                            + methodCallExpression.ownerBinaryName() + "." + methodCallExpression.methodName());
        }

        for (int i = 0; i < methodCallExpression.arguments().size(); i++) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(i));
            coerceValueForTargetType(code, actualType, resolvedMethod.parameterTypes().get(i));
        }

        invokeStaticMethod(code, resolvedMethod);
        return resolvedMethod.returnType();
    }

    private QinIrTypeRef emitJavaNewExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrJavaNewExpression javaNewExpression) {
        List<QinIrTypeRef> argumentTypes = new ArrayList<>();
        for (QinIrExpression argument : javaNewExpression.arguments()) {
            argumentTypes.add(inferDeclarationExpressionType(ownerDeclaration, method, declarationIndex, localFrame, argument));
        }
        ResolvedConstructorCall resolvedConstructor = resolveConstructorCall(
                javaNewExpression.ownerBinaryName(),
                argumentTypes);

        ClassDesc ownerDesc = ClassDesc.of(javaNewExpression.ownerBinaryName());
        code.new_(ownerDesc);
        code.dup();
        for (int i = 0; i < javaNewExpression.arguments().size(); i++) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    javaNewExpression.arguments().get(i));
            coerceValueForTargetType(code, actualType, resolvedConstructor.parameterTypes().get(i));
        }
        code.invokespecial(ownerDesc, "<init>", resolvedConstructor.descriptor());
        return QinIrTypeRef.classType(javaNewExpression.ownerBinaryName());
    }

    private QinIrTypeRef emitBuiltinCallExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_binary__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            return emitGlobalBuiltinObjectCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
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
                    localFrame,
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
                    localFrame,
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
                    localFrame,
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
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression,
            QinBuiltinRegistry.BuiltinMethod builtinMethod) {
        List<QinBuiltinRegistry.BuiltinArgKind> argumentKinds = builtinMethod.argumentKinds();
        for (int i = 0; i < builtinCallExpression.arguments().size(); i++) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
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
                localFrame,
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
            LocalFrame localFrame,
            QinIrSequenceExpression sequenceExpression) {
        for (QinIrExpression leadingExpression : sequenceExpression.leadingExpressions()) {
            QinIrTypeRef leadingType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    leadingExpression);
            discardExpressionResult(code, leadingType);
        }
        return emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                sequenceExpression.resultExpression());
    }

    private QinIrTypeRef emitGlobalBuiltinObjectCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression,
            String runtimeMethodName) {
        for (QinIrExpression argument : builtinCallExpression.arguments()) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
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
                localFrame,
                builtinCallExpression);
        coerceObjectResultForType(code, resultType);
        return resultType;
    }

    private QinIrTypeRef emitAssignmentExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrAssignmentExpression assignmentExpression) {
        if (!"=".equals(assignmentExpression.operator())) {
            throw new IllegalArgumentException(
                    "Declaration assignment expression supports only '=' for now: "
                            + assignmentExpression.operator());
        }
        if (!(assignmentExpression.target() instanceof QinIrIdentifierReference identifierReference)) {
            throw new IllegalArgumentException("Declaration assignment target must be a local identifier");
        }
        LocalBinding binding = localFrame.resolve(identifierReference.name());
        if (binding == null) {
            throw new IllegalArgumentException("Unknown declaration local assignment target: " + identifierReference.name());
        }
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                assignmentExpression.value());
        coerceValueForTargetType(code, actualType, binding.type());
        storeLocalForType(code, binding.type(), binding.localSlot(), binding.name());
        loadLocalForType(code, binding.type(), binding.localSlot(), binding.name());
        return binding.type();
    }

    private void emitObjectLiteral(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrObjectLiteral objectLiteral) {
        code.new_(LINKED_HASH_MAP_DESC);
        code.dup();
        code.invokespecial(LINKED_HASH_MAP_DESC, "<init>", VOID_INIT);

        for (QinIrObjectProperty property : objectLiteral.properties()) {
            code.dup();
            code.ldc(property.key());
            emitDeclarationExpressionAsObject(code, ownerDeclaration, method, declarationIndex, localFrame, property.value());
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

    private void emitObjectCoercedReturn(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef returnType) {
        switch (returnType.kind()) {
            case BOOLEAN, INT -> code.ireturn();
            case DOUBLE -> code.dreturn();
            case STRING, CLASS -> code.areturn();
            case VOID -> {
                code.pop();
                code.return_();
            }
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
        return inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                LocalFrame.forMethodParameters(method),
                expression);
    }

    private QinIrTypeRef inferDeclarationExpressionType(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
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
                    localFrame,
                    sequenceExpression.resultExpression());
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            LocalBinding localBinding = localFrame.resolve(identifierReference.name());
            if (localBinding != null) {
                return localBinding.type();
            }
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
                            localFrame,
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
                            localFrame,
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
                    localFrame,
                    methodCallExpression.receiver());
            ResolvedInstanceMethodCall resolvedMethod = resolveInstanceMethodCall(
                    receiverType,
                    methodCallExpression.methodName(),
                    methodCallExpression.arguments().size(),
                    declarationIndex);
            if (resolvedMethod == null) {
                if (isDynamicObjectType(receiverType)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                throw new IllegalArgumentException(
                        "Unknown declaration instance method type: "
                                + receiverType.binaryName() + "." + methodCallExpression.methodName());
            }
            return resolvedMethod.returnType();
        }
        if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            ResolvedStaticMethodCall resolvedMethod = resolveStaticMethodCall(
                    staticMethodCallExpression.ownerBinaryName(),
                    staticMethodCallExpression.methodName(),
                    staticMethodCallExpression.arguments().size());
            if (resolvedMethod == null) {
                throw new IllegalArgumentException(
                        "Unknown declaration static method type: "
                                + staticMethodCallExpression.ownerBinaryName()
                                + "." + staticMethodCallExpression.methodName());
            }
            return resolvedMethod.returnType();
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            return QinIrTypeRef.classType(javaNewExpression.ownerBinaryName());
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return inferBuiltinCallResultType(ownerDeclaration, method, declarationIndex, localFrame, builtinCallExpression);
        }
        if (expression instanceof QinIrObjectLiteral) {
            return QinIrTypeRef.classType("java.util.Map");
        }
        if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            return inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    assignmentExpression.value());
        }
        throw new IllegalArgumentException(
                "Unsupported declaration expression type inference: " + expression.getClass().getSimpleName());
    }

    private QinIrTypeRef inferBuiltinCallResultType(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
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
                    localFrame,
                    builtinCallExpression.arguments().get(1));
            QinIrTypeRef rightType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
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
                    localFrame,
                    builtinCallExpression.arguments().get(1));
            QinIrTypeRef rightType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(2));
            return inferLogicalBuiltinResultType(operatorLiteral.value(), leftType, rightType);
        }
        if ("__qin_conditional__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            QinIrTypeRef consequentType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(1));
            QinIrTypeRef alternateType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
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
        return resolvePropertyAccess(ownerType, propertyName, declarationIndex, new java.util.LinkedHashSet<>());
    }

    private ResolvedPropertyAccess resolvePropertyAccess(
            QinIrTypeRef ownerType,
            String propertyName,
            Map<String, QinIrClassDeclaration> declarationIndex,
            java.util.Set<String> visitedLocalTypes) {
        if (ownerType.kind() != QinIrTypeKind.CLASS && ownerType.kind() != QinIrTypeKind.STRING) {
            return null;
        }

        QinIrClassDeclaration localDeclaration = declarationIndex.get(ownerType.binaryName());
        if (localDeclaration != null) {
            if (!visitedLocalTypes.add(ownerType.binaryName())) {
                return null;
            }
            for (QinIrFieldDeclaration field : localDeclaration.fields()) {
                if (field.name().equals(propertyName)) {
                    return new ResolvedPropertyAccess(ownerType.binaryName(), getterName(field), field.type(), false);
                }
            }
            return localDeclaration.superType() == null
                    ? null
                    : resolvePropertyAccess(
                            localDeclaration.superType(),
                            propertyName,
                            declarationIndex,
                            visitedLocalTypes);
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
        return resolveInstanceMethodCall(
                ownerType,
                methodName,
                argumentCount,
                declarationIndex,
                new java.util.LinkedHashSet<>());
    }

    private ResolvedInstanceMethodCall resolveInstanceMethodCall(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount,
            Map<String, QinIrClassDeclaration> declarationIndex,
            java.util.Set<String> visitedLocalTypes) {
        if (ownerType.kind() != QinIrTypeKind.CLASS && ownerType.kind() != QinIrTypeKind.STRING) {
            return null;
        }

        QinIrClassDeclaration localDeclaration = declarationIndex.get(ownerType.binaryName());
        if (localDeclaration != null) {
            if (!visitedLocalTypes.add(ownerType.binaryName())) {
                return null;
            }
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
            if (matched != null) {
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
            return localDeclaration.superType() == null
                    ? null
                    : resolveInstanceMethodCall(
                            localDeclaration.superType(),
                            methodName,
                            argumentCount,
                            declarationIndex,
                            visitedLocalTypes);
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

    private ResolvedStaticMethodCall resolveStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            int argumentCount) {
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Method matched = null;
            for (Method candidate : ownerClass.getMethods()) {
                if (!candidate.getName().equals(methodName)
                        || candidate.getParameterCount() != argumentCount
                        || !Modifier.isStatic(candidate.getModifiers())) {
                    continue;
                }
                if (matched != null) {
                    throw new IllegalArgumentException(
                            "Ambiguous reflected static method overload: " + ownerBinaryName + "." + methodName);
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
            return new ResolvedStaticMethodCall(
                    ownerClass.getName(),
                    matched.getName(),
                    List.copyOf(parameterTypes),
                    toQinTypeRef(matched.getReturnType()));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private ResolvedConstructorCall resolveConstructorCall(String ownerBinaryName, List<QinIrTypeRef> argumentTypes) {
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            Constructor<?> matched = null;
            int matchedScore = Integer.MIN_VALUE;
            for (Constructor<?> candidate : ownerClass.getConstructors()) {
                if (candidate.getParameterCount() != argumentTypes.size()
                        || !isExecutableApplicable(candidate.getParameterTypes(), argumentTypes)) {
                    continue;
                }
                int score = executableMatchScore(candidate.getParameterTypes(), argumentTypes);
                if (matched != null && score == matchedScore) {
                    throw new IllegalArgumentException(
                            "Ambiguous reflected constructor overload: " + ownerBinaryName + "/" + argumentTypes.size());
                }
                if (matched == null || score > matchedScore) {
                    matched = candidate;
                    matchedScore = score;
                }
            }
            if (matched == null) {
                throw new IllegalArgumentException(
                        "Unknown Java constructor: " + ownerBinaryName + "/" + argumentTypes.size());
            }
            List<QinIrTypeRef> parameterTypes = new ArrayList<>();
            for (Class<?> parameterType : matched.getParameterTypes()) {
                parameterTypes.add(toQinTypeRef(parameterType));
            }
            return new ResolvedConstructorCall(
                    List.copyOf(parameterTypes),
                    toConstructorDescriptorForTypes(parameterTypes));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java constructor owner: " + ownerBinaryName, e);
        }
    }

    private boolean isExecutableApplicable(Class<?>[] parameterTypes, List<QinIrTypeRef> argumentTypes) {
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!isArgumentApplicable(parameterTypes[i], argumentTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    private int executableMatchScore(Class<?>[] parameterTypes, List<QinIrTypeRef> argumentTypes) {
        int score = 0;
        for (int i = 0; i < parameterTypes.length; i++) {
            score += argumentMatchScore(parameterTypes[i], argumentTypes.get(i));
        }
        return score;
    }

    private boolean isArgumentApplicable(Class<?> parameterType, QinIrTypeRef argumentType) {
        return argumentMatchScore(parameterType, argumentType) >= 0;
    }

    private int argumentMatchScore(Class<?> parameterType, QinIrTypeRef argumentType) {
        if (argumentType == null || argumentType.kind() == QinIrTypeKind.VOID) {
            return -1;
        }
        if (argumentType.kind() == QinIrTypeKind.STRING) {
            return parameterType == String.class
                    ? 4
                    : parameterType.isAssignableFrom(String.class) ? 1 : -1;
        }
        if (argumentType.kind() == QinIrTypeKind.BOOLEAN) {
            return parameterType == boolean.class || parameterType == Boolean.class
                    ? 4
                    : parameterType.isAssignableFrom(Boolean.class) ? 1 : -1;
        }
        if (argumentType.kind() == QinIrTypeKind.INT) {
            return parameterType == int.class || parameterType == Integer.class
                    ? 4
                    : parameterType == double.class || parameterType == Double.class || parameterType == Number.class
                    ? 2
                    : parameterType.isAssignableFrom(Integer.class) ? 1 : -1;
        }
        if (argumentType.kind() == QinIrTypeKind.DOUBLE) {
            return parameterType == double.class || parameterType == Double.class
                    ? 4
                    : parameterType == Number.class || parameterType.isAssignableFrom(Double.class) ? 1 : -1;
        }
        if (argumentType.kind() == QinIrTypeKind.CLASS) {
            if ("java.lang.Object".equals(argumentType.binaryName())) {
                return parameterType.isPrimitive() ? -1 : 0;
            }
            Class<?> argumentClass = resolveClass(argumentType.binaryName());
            if (parameterType.equals(argumentClass)) {
                return 4;
            }
            return parameterType.isAssignableFrom(argumentClass) ? 1 : -1;
        }
        return -1;
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

    private Class<?> resolveClass(String binaryName) {
        try {
            return Class.forName(binaryName);
        } catch (ClassNotFoundException error) {
            throw new IllegalArgumentException("Cannot resolve JVM class: " + binaryName, error);
        }
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

    private void storeLocalForType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef type,
            int localIndex,
            String localName) {
        switch (type.kind()) {
            case BOOLEAN, INT -> code.istore(localIndex);
            case DOUBLE -> code.dstore(localIndex);
            case STRING, CLASS -> code.astore(localIndex);
            case VOID -> throw new IllegalArgumentException("Local type cannot be void: " + localName);
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

    private record LocalBinding(String name, QinIrTypeRef type, int localSlot) {
    }

    private record LoopBinding(
            java.lang.classfile.Label continueLabel,
            java.lang.classfile.Label breakLabel) {
    }

    private static final class LocalFrame {
        private final Map<String, LocalBinding> bindings = new LinkedHashMap<>();
        private final LoopBinding loop;
        private final java.lang.classfile.Label breakLabel;
        private int nextSlot;

        private LocalFrame(int nextSlot, LoopBinding loop, java.lang.classfile.Label breakLabel) {
            this.nextSlot = nextSlot;
            this.loop = loop;
            this.breakLabel = breakLabel;
        }

        static LocalFrame forMethodParameters(QinIrMethodDeclaration method) {
            int localSlot = 1;
            for (var parameter : method.parameters()) {
                localSlot += parameter.type().kind() == QinIrTypeKind.DOUBLE ? 2 : 1;
            }
            return new LocalFrame(localSlot, null, null);
        }

        LocalFrame child() {
            LocalFrame child = new LocalFrame(nextSlot, loop, breakLabel);
            child.bindings.putAll(bindings);
            return child;
        }

        LocalFrame withLoop(LoopBinding loopBinding) {
            LocalFrame child = new LocalFrame(nextSlot, loopBinding, loopBinding.breakLabel());
            child.bindings.putAll(bindings);
            return child;
        }

        LocalFrame withSwitchBreak(java.lang.classfile.Label breakLabel) {
            LocalFrame child = new LocalFrame(nextSlot, loop, breakLabel);
            child.bindings.putAll(bindings);
            return child;
        }

        LocalBinding declare(String name, QinIrTypeRef type) {
            if (bindings.containsKey(name)) {
                throw new IllegalArgumentException("Duplicate declaration local: " + name);
            }
            if (type.kind() == QinIrTypeKind.VOID) {
                throw new IllegalArgumentException("Declaration local cannot be void: " + name);
            }
            LocalBinding binding = new LocalBinding(name, type, nextSlot);
            bindings.put(name, binding);
            nextSlot += type.kind() == QinIrTypeKind.DOUBLE ? 2 : 1;
            return binding;
        }

        LocalBinding resolve(String name) {
            return bindings.get(name);
        }

        String syntheticLocalName(String prefix) {
            String candidate = prefix;
            int suffix = 0;
            while (bindings.containsKey(candidate)) {
                suffix++;
                candidate = prefix + "_" + suffix;
            }
            return candidate;
        }

        LoopBinding loop() {
            return loop;
        }

        java.lang.classfile.Label breakLabel() {
            return breakLabel;
        }
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

    private record ResolvedStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> parameterTypes,
            QinIrTypeRef returnType) {
    }

    private record ResolvedConstructorCall(
            List<QinIrTypeRef> parameterTypes,
            MethodTypeDesc descriptor) {
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

    private void invokeStaticMethod(java.lang.classfile.CodeBuilder code, ResolvedStaticMethodCall methodCall) {
        MethodTypeDesc descriptor = MethodTypeDesc.of(
                toClassDesc(methodCall.returnType()),
                methodCall.parameterTypes().stream().map(this::toClassDesc).toList());
        code.invokestatic(ClassDesc.of(methodCall.ownerBinaryName()), methodCall.methodName(), descriptor);
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
            if (actualType.kind() == QinIrTypeKind.CLASS && targetType.kind() == QinIrTypeKind.DOUBLE) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "doubleValue", MethodTypeDesc.ofDescriptor("()D"));
                return;
            }
            if (actualType.kind() == QinIrTypeKind.CLASS && targetType.kind() == QinIrTypeKind.INT) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "intValue", MethodTypeDesc.ofDescriptor("()I"));
                return;
            }
            if (actualType.kind() == QinIrTypeKind.DOUBLE && targetType.kind() == QinIrTypeKind.INT) {
                code.d2i();
                return;
            }
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
        emitDeclarationExpressionAsObject(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                LocalFrame.forMethodParameters(method),
                expression);
    }

    private void emitDeclarationExpressionAsObject(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression expression) {
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression);
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
