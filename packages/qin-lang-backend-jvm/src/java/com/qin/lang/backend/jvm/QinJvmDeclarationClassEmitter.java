package com.qin.lang.backend.jvm;

import com.qin.lang.ir.QinIrAnnotation;
import com.qin.lang.ir.QinIrAnnotationArgument;
import com.qin.lang.ir.QinIrArrayCreationExpression;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBreakStatement;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrBoundMethodReferenceExpression;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrCastExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrContinueStatement;
import com.qin.lang.ir.QinIrDoWhileStatementNode;
import com.qin.lang.ir.QinIrElementAccessExpression;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFieldDeclaration;
import com.qin.lang.ir.QinIrForEachStatement;
import com.qin.lang.ir.QinIrForStatement;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrLetExpression;
import com.qin.lang.ir.QinIrJavaClassLiteralExpression;
import com.qin.lang.ir.QinIrJavaNewExpression;
import com.qin.lang.ir.QinIrLocalVariableDeclaration;
import com.qin.lang.ir.QinIrMemberAccessExpression;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrPropertyAccessExpression;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrReturnStatement;
import com.qin.lang.ir.QinIrSequenceExpression;
import com.qin.lang.ir.QinIrShortCircuitExpression;
import com.qin.lang.ir.QinIrStatement;
import com.qin.lang.ir.QinIrStatementExpression;
import com.qin.lang.ir.QinIrSpreadArgumentExpression;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.ir.QinIrSuperMethodCallExpression;
import com.qin.lang.ir.QinIrSwitchCase;
import com.qin.lang.ir.QinIrSwitchStatement;
import com.qin.lang.ir.QinIrThisExpression;
import com.qin.lang.ir.QinIrThrowStatement;
import com.qin.lang.ir.QinIrTryStatement;
import com.qin.lang.ir.QinIrTryResource;
import com.qin.lang.ir.QinIrIfStatement;
import com.qin.lang.ir.QinIrCatchClause;
import com.qin.lang.ir.QinIrAssignmentExpression;
import com.qin.lang.ir.QinIrLocalDeclarationStatement;
import com.qin.lang.ir.QinIrUpdateExpression;
import com.qin.lang.ir.QinIrTypeKind;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinIrWhileStatementNode;
import com.qin.lang.ir.QinBuiltinRegistry;
import com.qin.lang.ir.QinJavaSdkAliasSupport;

import java.lang.classfile.Annotation;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.AnnotationValue;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassHierarchyResolver;
import java.lang.classfile.TypeKind;
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
import java.util.IdentityHashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
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
    private static final String SUBHUTI_RULE_ANNOTATION = "com.subhuti.parser.SubhutiRule";
    private static final String STRUCTURAL_SLIME_AST_TYPE_PREFIX = "__qin.struct.";
    private static final int DEFAULT_CLASSFILE_MAJOR_VERSION = ClassFile.JAVA_21_VERSION;
    private static final ClassDesc OBJECT_DESC = ClassDesc.of("java.lang.Object");
    private static final ClassDesc NUMBER_DESC = ClassDesc.of("java.lang.Number");
    private static final ClassDesc BOOLEAN_DESC = ClassDesc.of("java.lang.Boolean");
    private static final ClassDesc STRING_DESC = ClassDesc.of("java.lang.String");
    private static final ClassDesc CLASS_DESC = ClassDesc.of("java.lang.Class");
    private static final ClassDesc CLASS_ARRAY_DESC = ClassDesc.ofDescriptor("[Ljava/lang/Class;");
    private static final ClassDesc REFLECT_METHOD_DESC = ClassDesc.of("java.lang.reflect.Method");
    private static final ClassDesc MESSAGE_DIGEST_DESC = ClassDesc.of("java.security.MessageDigest");
    private static final ClassDesc HEX_FORMAT_DESC = ClassDesc.of("java.util.HexFormat");
    private static final ClassDesc RUNTIME_EXCEPTION_DESC = ClassDesc.of("java.lang.RuntimeException");
    private static final ClassDesc OBJECT_ARRAY_DESC = ClassDesc.ofDescriptor("[Ljava/lang/Object;");
    private static final ClassDesc ITERABLE_DESC = ClassDesc.of("java.lang.Iterable");
    private static final ClassDesc ITERATOR_DESC = ClassDesc.of("java.util.Iterator");
    private static final ClassDesc COLLECTION_DESC = ClassDesc.of("java.util.Collection");
    private static final ClassDesc ARRAY_LIST_DESC = ClassDesc.of("java.util.ArrayList");
    private static final ClassDesc MAP_DESC = ClassDesc.of("java.util.Map");
    private static final ClassDesc MAP_ENTRY_DESC = ClassDesc.of("java.util.Map$Entry");
    private static final ClassDesc LINKED_HASH_MAP_DESC = ClassDesc.of("java.util.LinkedHashMap");
    private static final ClassDesc JAVA_ESM_MAP_OBJECT_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmMapObject");
    private static final ClassDesc JAVA_ESM_SET_OBJECT_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmSetObject");
    private static final ClassDesc ESM_GLOBAL_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmGlobal");
    private static final ClassDesc ESM_SYMBOL_DESC = ClassDesc.of("com.qin.lang.runtime.JavaEsmSymbol");
    private static final MethodTypeDesc VOID_INIT = MethodTypeDesc.ofDescriptor("()V");
    private static final MethodTypeDesc MAP_PUT_SIGNATURE =
            MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC);
    private static final MethodTypeDesc LIST_ADD_SIGNATURE =
            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z");

    private Map<QinIrObjectLiteral, String> activeFunctionDefinitionHelpers = Map.of();
    private String activeBinaryClassName;
    private String syntheticThisMethodOwnerBinaryName;
    private boolean syntheticThisMethodCollectThisReceivers = true;
    private Map<String, Map<String, SyntheticThisMethodCall>> syntheticOwnedReceiverMethodCalls;

    private enum FunctionValueShape {
        VALUE_COMPATIBLE,
        VOID_COMPATIBLE,
        UNKNOWN
    }
    private Map<String, QinIrExpression> activeModuleConstantInitializers = Map.of();
    private Map<String, QinIrClassDeclaration> activeDeclarationIndex = Map.of();

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
        return compileAllClasses(program, Map.of());
    }

    public Map<String, byte[]> compileAllClasses(
            QinIrProgram program,
            Map<String, QinIrClassDeclaration> externalDeclarationIndex) {
        Objects.requireNonNull(program, "program cannot be null");
        if (program.classDeclarations().isEmpty()) {
            throw new IllegalArgumentException("Expected at least one class declaration");
        }

        Map<String, QinIrExpression> previousModuleConstants = activeModuleConstantInitializers;
        activeModuleConstantInitializers = moduleConstantInitializers(program.declarations());
        try {
            List<QinIrClassDeclaration> classDeclarations = completeSyntheticAbstractThisMethods(
                    program.classDeclarations(),
                    externalDeclarationIndex);
            Map<String, QinIrClassDeclaration> initialDeclarationIndex = buildDeclarationIndex(
                    classDeclarations,
                    externalDeclarationIndex);
            classDeclarations = completeInheritedOverrideParameterTypes(
                    classDeclarations,
                    initialDeclarationIndex);
            Map<String, QinIrClassDeclaration> declarationIndex = buildDeclarationIndex(
                    classDeclarations,
                    externalDeclarationIndex);
            Map<String, byte[]> compiled = new LinkedHashMap<>();
            for (QinIrClassDeclaration declaration : classDeclarations) {
                String binaryName = declaration.binaryName();
                if (binaryName == null || binaryName.isBlank()) {
                    throw new IllegalArgumentException(
                            "Binary class name cannot be blank for declaration: " + declaration.simpleName());
                }
                compiled.put(binaryName, compileClass(declaration, binaryName, declarationIndex));
            }
            return Map.copyOf(compiled);
        } finally {
            activeModuleConstantInitializers = previousModuleConstants;
        }
    }

    private Map<String, QinIrExpression> moduleConstantInitializers(List<QinIrConstDeclaration> declarations) {
        if (declarations == null || declarations.isEmpty()) {
            return Map.of();
        }
        Map<String, QinIrExpression> constants = new LinkedHashMap<>();
        for (QinIrConstDeclaration declaration : declarations) {
            if (isStaticInlineableModuleConstant(declaration.initializer())) {
                constants.put(declaration.name(), declaration.initializer());
            }
        }
        return Map.copyOf(constants);
    }

    private boolean isStaticInlineableModuleConstant(QinIrExpression initializer) {
        return initializer instanceof QinIrStringLiteral
                || initializer instanceof QinIrNumberLiteral
                || initializer instanceof QinIrBooleanLiteral
                || initializer instanceof QinIrNullLiteral;
    }

    public byte[] compileClass(QinIrClassDeclaration declaration, String binaryClassName) {
        return compileClass(declaration, binaryClassName, buildDeclarationIndex(List.of(declaration)));
    }

    private List<QinIrClassDeclaration> completeSyntheticAbstractThisMethods(
            List<QinIrClassDeclaration> declarations,
            Map<String, QinIrClassDeclaration> externalDeclarationIndex) {
        if (declarations == null || declarations.isEmpty()) {
            return List.of();
        }
        Map<String, QinIrClassDeclaration> originalIndex = buildDeclarationIndex(declarations, externalDeclarationIndex);
        List<QinIrClassDeclaration> completed = new ArrayList<>();
        boolean changed = false;
        for (QinIrClassDeclaration declaration : declarations) {
            QinIrClassDeclaration completedDeclaration =
                    completeSyntheticAbstractThisMethods(declaration, originalIndex);
            completed.add(completedDeclaration);
            changed |= completedDeclaration != declaration;
        }
        List<QinIrClassDeclaration> completedThisMethods = changed ? List.copyOf(completed) : declarations;
        List<QinIrClassDeclaration> completedOwnedReceiverMethods =
                completeSyntheticAbstractOwnedReceiverMethods(completedThisMethods, externalDeclarationIndex);
        return completedOwnedReceiverMethods == completedThisMethods
                ? completedThisMethods
                : completedOwnedReceiverMethods;
    }

    private List<QinIrClassDeclaration> completeInheritedOverrideParameterTypes(
            List<QinIrClassDeclaration> declarations,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declarations == null || declarations.isEmpty() || declarationIndex == null || declarationIndex.isEmpty()) {
            return declarations == null ? List.of() : declarations;
        }
        List<QinIrClassDeclaration> completed = new ArrayList<>();
        boolean changed = false;
        for (QinIrClassDeclaration declaration : declarations) {
            QinIrClassDeclaration completedDeclaration =
                    completeInheritedOverrideParameterTypes(declaration, declarationIndex);
            completed.add(completedDeclaration);
            changed |= completedDeclaration != declaration;
        }
        return changed ? List.copyOf(completed) : declarations;
    }

    private QinIrClassDeclaration completeInheritedOverrideParameterTypes(
            QinIrClassDeclaration declaration,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declaration == null
                || declaration.superType() == null
                || declaration.superType().binaryName() == null
                || declaration.methods().isEmpty()) {
            return declaration;
        }
        QinIrClassDeclaration superDeclaration =
                resolveIndexedDeclaration(declarationIndex, declaration.superType().binaryName());
        if (superDeclaration == null) {
            return declaration;
        }
        List<QinIrMethodDeclaration> methods = new ArrayList<>();
        boolean changed = false;
        for (QinIrMethodDeclaration method : declaration.methods()) {
            QinIrMethodDeclaration completedMethod =
                    completeInheritedOverrideParameterTypes(method, superDeclaration, declarationIndex);
            methods.add(completedMethod);
            changed |= completedMethod != method;
        }
        if (!changed) {
            return declaration;
        }
        return new QinIrClassDeclaration(
                declaration.packageName(),
                declaration.simpleName(),
                declaration.superType(),
                declaration.implementsTypes(),
                declaration.annotations(),
                declaration.fields(),
                methods,
                declaration.staticInitializers(),
                declaration.recordClass(),
                declaration.interfaceClass());
    }

    private QinIrMethodDeclaration completeInheritedOverrideParameterTypes(
            QinIrMethodDeclaration method,
            QinIrClassDeclaration superDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (method == null
                || method.staticMethod()
                || method.abstractMethod()
                || isConstructorMethod(method)
                || method.parameters().isEmpty()) {
            return method;
        }
        QinIrMethodDeclaration inherited = findInheritedDeclarationMethod(
                superDeclaration,
                declarationIndex,
                method.name(),
                method.parameters().size(),
                new LinkedHashSet<>());
        if (inherited == null || inherited.parameters().size() != method.parameters().size()) {
            return method;
        }
        List<QinIrParameter> parameters = new ArrayList<>(method.parameters());
        boolean changed = false;
        for (int i = 0; i < parameters.size(); i++) {
            QinIrParameter parameter = parameters.get(i);
            QinIrParameter inheritedParameter = inherited.parameters().get(i);
            if (parameter == null
                    || inheritedParameter == null
                    || !isJavaLangObjectType(parameter.type())
                    || isJavaLangObjectType(inheritedParameter.type())) {
                continue;
            }
            parameters.set(i, new QinIrParameter(
                    parameter.name(),
                    inheritedParameter.type(),
                    parameter.annotations(),
                    parameter.varargs()));
            changed = true;
        }
        if (!changed) {
            return method;
        }
        return new QinIrMethodDeclaration(
                method.name(),
                method.returnType(),
                List.copyOf(parameters),
                method.annotations(),
                method.returnExpression(),
                method.bodyStatements(),
                method.superArguments(),
                method.explicitSuperConstructorCall(),
                method.runtimeFunctionDefinition(),
                method.staticMethod(),
                method.abstractMethod());
    }

    private List<QinIrClassDeclaration> completeSyntheticAbstractOwnedReceiverMethods(
            List<QinIrClassDeclaration> declarations,
            Map<String, QinIrClassDeclaration> externalDeclarationIndex) {
        if (declarations == null || declarations.isEmpty()) {
            return List.of();
        }
        Map<String, QinIrClassDeclaration> declarationIndex = buildDeclarationIndex(declarations, externalDeclarationIndex);
        Map<String, Map<String, SyntheticThisMethodCall>> ownedCalls = new LinkedHashMap<>();
        String previousSyntheticThisMethodOwnerBinaryName = syntheticThisMethodOwnerBinaryName;
        boolean previousSyntheticThisMethodCollectThisReceivers = syntheticThisMethodCollectThisReceivers;
        Map<String, Map<String, SyntheticThisMethodCall>> previousSyntheticOwnedReceiverMethodCalls =
                syntheticOwnedReceiverMethodCalls;
        syntheticThisMethodOwnerBinaryName = null;
        syntheticThisMethodCollectThisReceivers = false;
        syntheticOwnedReceiverMethodCalls = ownedCalls;
        try {
            for (QinIrClassDeclaration declaration : declarations) {
                if (declaration == null || declaration.methods().isEmpty()) {
                    continue;
                }
                for (QinIrMethodDeclaration method : declaration.methods()) {
                    if (method.staticMethod() || method.abstractMethod()) {
                        continue;
                    }
                    collectSyntheticThisMethodCalls(method.returnExpression(), new LinkedHashMap<>());
                    collectSyntheticThisMethodCallsExpressions(method.superArguments(), new LinkedHashMap<>());
                    collectSyntheticThisMethodCalls(method.bodyStatements(), new LinkedHashMap<>());
                }
            }
        } finally {
            syntheticThisMethodOwnerBinaryName = previousSyntheticThisMethodOwnerBinaryName;
            syntheticThisMethodCollectThisReceivers = previousSyntheticThisMethodCollectThisReceivers;
            syntheticOwnedReceiverMethodCalls = previousSyntheticOwnedReceiverMethodCalls;
        }
        if (ownedCalls.isEmpty()) {
            return declarations;
        }
        Map<String, List<QinIrMethodDeclaration>> additionsByOwner = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, SyntheticThisMethodCall>> ownerEntry : ownedCalls.entrySet()) {
            QinIrClassDeclaration ownerDeclaration = declarationIndex.get(ownerEntry.getKey());
            if (ownerDeclaration == null || ownerDeclaration.interfaceClass()) {
                continue;
            }
            QinIrTypeRef ownerType = QinIrTypeRef.classType(ownerDeclaration.binaryName());
            for (SyntheticThisMethodCall call : ownerEntry.getValue().values()) {
                if (resolveInstanceMethodCall(
                        ownerType,
                        call.methodName(),
                        call.argumentCount(),
                        declarationIndex) != null) {
                    continue;
                }
                QinIrMethodDeclaration subtypeMethod = findSubtypeInstanceMethodSignature(
                        ownerDeclaration,
                        call.methodName(),
                        call.argumentCount(),
                        declarationIndex);
                if (subtypeMethod == null) {
                    continue;
                }
                additionsByOwner.computeIfAbsent(ownerDeclaration.binaryName(), ignored -> new ArrayList<>())
                        .add(new QinIrMethodDeclaration(
                                call.methodName(),
                                subtypeMethod.returnType(),
                                subtypeMethod.parameters(),
                                List.of(),
                                null,
                                List.of(),
                                List.of(),
                                null,
                                false,
                                true));
            }
        }
        if (additionsByOwner.isEmpty()) {
            return declarations;
        }
        List<QinIrClassDeclaration> completed = new ArrayList<>();
        boolean changed = false;
        for (QinIrClassDeclaration declaration : declarations) {
            List<QinIrMethodDeclaration> additions = additionsByOwner.get(declaration.binaryName());
            if (additions == null || additions.isEmpty()) {
                completed.add(declaration);
                continue;
            }
            List<QinIrMethodDeclaration> methods = new ArrayList<>(declaration.methods());
            methods.addAll(additions);
            completed.add(new QinIrClassDeclaration(
                    declaration.packageName(),
                    declaration.simpleName(),
                    declaration.superType(),
                    declaration.implementsTypes(),
                    declaration.annotations(),
                    declaration.fields(),
                    methods,
                    declaration.staticInitializers(),
                    declaration.recordClass(),
                    declaration.interfaceClass()));
            changed = true;
        }
        return changed ? List.copyOf(completed) : declarations;
    }

    private QinIrClassDeclaration completeSyntheticAbstractThisMethods(
            QinIrClassDeclaration declaration,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declaration == null
                || declaration.interfaceClass()
                || declaration.methods().isEmpty()) {
            return declaration;
        }
        Map<String, SyntheticThisMethodCall> missingThisCalls = new LinkedHashMap<>();
        String previousSyntheticThisMethodOwnerBinaryName = syntheticThisMethodOwnerBinaryName;
        syntheticThisMethodOwnerBinaryName = declaration.binaryName();
        try {
            for (QinIrMethodDeclaration method : declaration.methods()) {
                if (method.staticMethod() || method.abstractMethod()) {
                    continue;
                }
                collectSyntheticThisMethodCalls(method.returnExpression(), missingThisCalls);
                collectSyntheticThisMethodCallsExpressions(method.superArguments(), missingThisCalls);
                collectSyntheticThisMethodCalls(method.bodyStatements(), missingThisCalls);
            }
        } finally {
            syntheticThisMethodOwnerBinaryName = previousSyntheticThisMethodOwnerBinaryName;
        }
        if (missingThisCalls.isEmpty()) {
            return declaration;
        }
        QinIrTypeRef currentType = QinIrTypeRef.classType(declaration.binaryName());
        List<QinIrMethodDeclaration> methods = new ArrayList<>(declaration.methods());
        boolean changed = false;
        for (SyntheticThisMethodCall call : missingThisCalls.values()) {
            if (resolveInstanceMethodCall(
                    currentType,
                    call.methodName(),
                    call.argumentCount(),
                    declarationIndex) != null) {
                continue;
            }
            QinIrMethodDeclaration subtypeMethod = findSubtypeInstanceMethodSignature(
                    declaration,
                    call.methodName(),
                    call.argumentCount(),
                    declarationIndex);
            if (subtypeMethod == null) {
                continue;
            }
            methods.add(new QinIrMethodDeclaration(
                    call.methodName(),
                    subtypeMethod.returnType(),
                    subtypeMethod.parameters(),
                    List.of(),
                    null,
                    List.of(),
                    List.of(),
                    null,
                    false,
                    true));
            changed = true;
        }
        if (!changed) {
            return declaration;
        }
        return new QinIrClassDeclaration(
                declaration.packageName(),
                declaration.simpleName(),
                declaration.superType(),
                declaration.implementsTypes(),
                declaration.annotations(),
                declaration.fields(),
                methods,
                declaration.staticInitializers(),
                declaration.recordClass(),
                declaration.interfaceClass());
    }

    private QinIrMethodDeclaration findSubtypeInstanceMethodSignature(
            QinIrClassDeclaration ownerDeclaration,
            String methodName,
            int argumentCount,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        QinIrMethodDeclaration matched = null;
        int matchedScore = Integer.MAX_VALUE;
        for (QinIrClassDeclaration candidateDeclaration : declarationIndex.values()) {
            if (candidateDeclaration == null
                    || candidateDeclaration == ownerDeclaration
                    || !isLocalSubtypeOf(candidateDeclaration, ownerDeclaration.binaryName(), declarationIndex)) {
                continue;
            }
            for (QinIrMethodDeclaration candidate : candidateDeclaration.methods()) {
                if (candidate.staticMethod()
                        || !sameLocalMethodName(candidate.name(), methodName)
                        || !isLocalMethodCountApplicable(candidate.parameters(), argumentCount)) {
                    continue;
                }
                int distance = localSubtypeDistance(candidateDeclaration, ownerDeclaration.binaryName(), declarationIndex);
                int score = syntheticThisMethodSignatureScore(candidate, argumentCount, distance);
                if (score > matchedScore) {
                    continue;
                }
                if (score < matchedScore) {
                    matched = candidate;
                    matchedScore = score;
                    continue;
                }
                if (matched != null && !sameSyntheticMethodSignature(matched, candidate)) {
                    throw new IllegalArgumentException(
                            "Ambiguous generated abstract method signature for "
                                    + ownerDeclaration.binaryName()
                                    + "."
                                    + methodName
                                    + "/"
                                    + argumentCount);
                }
                matched = candidate;
                matchedScore = score;
            }
        }
        return matched;
    }

    private int syntheticThisMethodSignatureScore(QinIrMethodDeclaration candidate, int argumentCount, int subtypeDistance) {
        List<QinIrParameter> parameters = candidate.parameters();
        int distance = subtypeDistance < 0 ? 999 : subtypeDistance;
        if (parameters.isEmpty()) {
            return argumentCount == 0 ? distance : Integer.MAX_VALUE;
        }
        if (parameters.get(parameters.size() - 1).varargs()) {
            int fixedCount = parameters.size() - 1;
            return argumentCount >= fixedCount
                    ? 200_000 + (Math.max(0, argumentCount - fixedCount) * 1_000) + distance
                    : Integer.MAX_VALUE;
        }
        if (parameters.size() == argumentCount) {
            return distance;
        }
        if (argumentCount < parameters.size()) {
            return 100_000 + ((parameters.size() - argumentCount) * 1_000) + distance;
        }
        return Integer.MAX_VALUE;
    }

    private boolean sameSyntheticMethodSignature(
            QinIrMethodDeclaration left,
            QinIrMethodDeclaration right) {
        if (!Objects.equals(left.returnType(), right.returnType())
                || left.parameters().size() != right.parameters().size()) {
            return false;
        }
        for (int i = 0; i < left.parameters().size(); i++) {
            QinIrParameter leftParameter = left.parameters().get(i);
            QinIrParameter rightParameter = right.parameters().get(i);
            if (!Objects.equals(leftParameter.type(), rightParameter.type())
                    || leftParameter.varargs() != rightParameter.varargs()) {
                return false;
            }
        }
        return true;
    }

    private boolean isLocalSubtypeOf(
            QinIrClassDeclaration candidateDeclaration,
            String ancestorBinaryName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return localSubtypeDistance(candidateDeclaration, ancestorBinaryName, declarationIndex) >= 0;
    }

    private int localSubtypeDistance(
            QinIrClassDeclaration candidateDeclaration,
            String ancestorBinaryName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        QinIrTypeRef superType = candidateDeclaration.superType();
        Set<String> visited = new LinkedHashSet<>();
        int distance = 1;
        while (superType != null
                && superType.kind() == QinIrTypeKind.CLASS
                && superType.binaryName() != null
                && visited.add(superType.binaryName())) {
            if (ancestorBinaryName.equals(superType.binaryName())) {
                return distance;
            }
            QinIrClassDeclaration superDeclaration = declarationIndex.get(superType.binaryName());
            superType = superDeclaration == null ? null : superDeclaration.superType();
            distance++;
        }
        return -1;
    }

    private void collectSyntheticThisMethodCalls(
            List<? extends QinIrStatement> statements,
            Map<String, SyntheticThisMethodCall> result) {
        if (statements == null || statements.isEmpty()) {
            return;
        }
        for (QinIrStatement statement : statements) {
            collectSyntheticThisMethodCalls(statement, result);
        }
    }

    private void collectSyntheticThisMethodCalls(
            QinIrStatement statement,
            Map<String, SyntheticThisMethodCall> result) {
        if (statement == null) {
            return;
        }
        if (statement instanceof QinIrStatementExpression statementExpression) {
            collectSyntheticThisMethodCalls(statementExpression.expression(), result);
        } else if (statement instanceof QinIrReturnStatement returnStatement) {
            collectSyntheticThisMethodCalls(returnStatement.value(), result);
        } else if (statement instanceof QinIrLocalDeclarationStatement localDeclarationStatement) {
            collectSyntheticThisMethodCalls(localDeclarationStatement.initializer(), result);
        } else if (statement instanceof QinIrIfStatement ifStatement) {
            collectSyntheticThisMethodCalls(ifStatement.test(), result);
            collectSyntheticThisMethodCalls(ifStatement.consequent(), result);
            collectSyntheticThisMethodCalls(ifStatement.alternate(), result);
        } else if (statement instanceof QinIrForStatement forStatement) {
            for (QinIrLocalVariableDeclaration declaration : forStatement.initializerDeclarations()) {
                collectSyntheticThisMethodCalls(declaration.initializer(), result);
            }
            collectSyntheticThisMethodCallsExpressions(forStatement.initializerExpressions(), result);
            collectSyntheticThisMethodCalls(forStatement.test(), result);
            collectSyntheticThisMethodCallsExpressions(forStatement.updateExpressions(), result);
            collectSyntheticThisMethodCalls(forStatement.body(), result);
        } else if (statement instanceof QinIrForEachStatement forEachStatement) {
            collectSyntheticThisMethodCalls(forEachStatement.iterable(), result);
            collectSyntheticThisMethodCalls(forEachStatement.body(), result);
        } else if (statement instanceof QinIrWhileStatementNode whileStatement) {
            collectSyntheticThisMethodCalls(whileStatement.test(), result);
            collectSyntheticThisMethodCalls(whileStatement.body(), result);
        } else if (statement instanceof QinIrDoWhileStatementNode doWhileStatement) {
            collectSyntheticThisMethodCalls(doWhileStatement.body(), result);
            collectSyntheticThisMethodCalls(doWhileStatement.test(), result);
        } else if (statement instanceof QinIrSwitchStatement switchStatement) {
            collectSyntheticThisMethodCalls(switchStatement.discriminant(), result);
            for (QinIrSwitchCase switchCase : switchStatement.cases()) {
                collectSyntheticThisMethodCalls(switchCase.test(), result);
                collectSyntheticThisMethodCalls(switchCase.consequent(), result);
            }
        } else if (statement instanceof QinIrTryStatement tryStatement) {
            for (QinIrTryResource resource : tryStatement.resources()) {
                collectSyntheticThisMethodCalls(resource.initializer(), result);
                collectSyntheticThisMethodCalls(resource.reference(), result);
            }
            collectSyntheticThisMethodCalls(tryStatement.tryBody(), result);
            for (QinIrCatchClause catchClause : tryStatement.catchClauses()) {
                collectSyntheticThisMethodCalls(catchClause.body(), result);
            }
            collectSyntheticThisMethodCalls(tryStatement.finallyBody(), result);
        } else if (statement instanceof QinIrThrowStatement throwStatement) {
            collectSyntheticThisMethodCalls(throwStatement.value(), result);
        }
    }

    private void collectSyntheticThisMethodCallsExpressions(
            List<? extends QinIrExpression> expressions,
            Map<String, SyntheticThisMethodCall> result) {
        if (expressions == null || expressions.isEmpty()) {
            return;
        }
        for (QinIrExpression expression : expressions) {
            collectSyntheticThisMethodCalls(expression, result);
        }
    }

    private void collectSyntheticThisMethodCalls(
            QinIrExpression expression,
            Map<String, SyntheticThisMethodCall> result) {
        if (expression == null) {
            return;
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            if (syntheticOwnedReceiverMethodCalls != null
                    && methodCallExpression.ownerBinaryName() != null) {
                SyntheticThisMethodCall call = new SyntheticThisMethodCall(
                        methodCallExpression.methodName(),
                        methodCallExpression.arguments().size());
                syntheticOwnedReceiverMethodCalls
                        .computeIfAbsent(methodCallExpression.ownerBinaryName(), ignored -> new LinkedHashMap<>())
                        .putIfAbsent(call.key(), call);
            }
            if ((syntheticThisMethodCollectThisReceivers
                            && methodCallExpression.receiver() instanceof QinIrThisExpression)
                    || (syntheticThisMethodOwnerBinaryName != null
                            && syntheticThisMethodOwnerBinaryName.equals(methodCallExpression.ownerBinaryName()))) {
                SyntheticThisMethodCall call = new SyntheticThisMethodCall(
                        methodCallExpression.methodName(),
                        methodCallExpression.arguments().size());
                result.putIfAbsent(call.key(), call);
            }
            collectSyntheticThisMethodCalls(methodCallExpression.receiver(), result);
            collectSyntheticThisMethodCallsExpressions(methodCallExpression.arguments(), result);
        } else if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            collectSyntheticThisMethodCallsExpressions(staticMethodCallExpression.arguments(), result);
        } else if (expression instanceof QinIrJavaNewExpression newExpression) {
            collectSyntheticThisMethodCallsExpressions(newExpression.arguments(), result);
        } else if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            collectSyntheticThisMethodCalls(propertyAccessExpression.receiver(), result);
        } else if (expression instanceof QinIrElementAccessExpression elementAccessExpression) {
            collectSyntheticThisMethodCalls(elementAccessExpression.receiver(), result);
            collectSyntheticThisMethodCalls(elementAccessExpression.index(), result);
        } else if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            collectSyntheticThisMethodCalls(assignmentExpression.target(), result);
            collectSyntheticThisMethodCalls(assignmentExpression.value(), result);
        } else if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            collectSyntheticThisMethodCallsExpressions(arrayLiteral.elements(), result);
        } else if (expression instanceof QinIrObjectLiteral objectLiteral) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                collectSyntheticThisMethodCalls(property.value(), result);
            }
        } else if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            collectSyntheticThisMethodCallsExpressions(sequenceExpression.leadingExpressions(), result);
            collectSyntheticThisMethodCalls(sequenceExpression.resultExpression(), result);
        } else if (expression instanceof QinIrShortCircuitExpression shortCircuitExpression) {
            collectSyntheticThisMethodCalls(shortCircuitExpression.left(), result);
            collectSyntheticThisMethodCalls(shortCircuitExpression.right(), result);
        } else if (expression instanceof QinIrUpdateExpression updateExpression) {
            collectSyntheticThisMethodCalls(updateExpression.target(), result);
        } else if (expression instanceof QinIrLetExpression letExpression) {
            for (QinIrLocalVariableDeclaration declaration : letExpression.localDeclarations()) {
                collectSyntheticThisMethodCalls(declaration.initializer(), result);
            }
            collectSyntheticThisMethodCallsExpressions(letExpression.leadingExpressions(), result);
            collectSyntheticThisMethodCalls(letExpression.resultExpression(), result);
        } else if (expression instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            collectSyntheticThisMethodCalls(spreadArgumentExpression.expression(), result);
        }
    }

    private boolean hasAbstractMethod(QinIrClassDeclaration declaration) {
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (method.abstractMethod()) {
                return true;
            }
        }
        return false;
    }

    private record SyntheticThisMethodCall(String methodName, int argumentCount) {
        private String key() {
            return methodName + "\u0000" + argumentCount;
        }
    }

    private Map<String, QinIrClassDeclaration> buildDeclarationIndex(
            List<QinIrClassDeclaration> declarations,
            Map<String, QinIrClassDeclaration> externalDeclarationIndex) {
        DeclarationIndexBuilder builder = new DeclarationIndexBuilder();
        return builder.build(declarations, externalDeclarationIndex);
    }

    private final class DeclarationIndexBuilder {
        private final Map<QinIrClassDeclaration, DeclarationIndexFacts> factsByDeclaration =
                new IdentityHashMap<>();

        private Map<String, QinIrClassDeclaration> build(
                List<QinIrClassDeclaration> declarations,
                Map<String, QinIrClassDeclaration> externalDeclarationIndex) {
        Map<String, QinIrClassDeclaration> index = new LinkedHashMap<>();
        if (externalDeclarationIndex != null) {
            for (Map.Entry<String, QinIrClassDeclaration> entry : externalDeclarationIndex.entrySet()) {
                putDeclarationIndexValue(index, entry.getKey(), entry.getValue());
                QinIrClassDeclaration declaration = entry.getValue();
                if (declaration != null
                        && declaration.binaryName() != null
                        && !declaration.binaryName().isBlank()) {
                    putDeclarationIndexValue(index, flattenedBinaryAlias(declaration.binaryName()), declaration);
                    if (declaration.simpleName() != null && !declaration.simpleName().isBlank()) {
                        putDeclarationIndexValue(index, declaration.simpleName(), declaration);
                    }
                }
            }
        }
        for (QinIrClassDeclaration declaration : declarations) {
            String binaryName = declaration.binaryName();
            if (binaryName != null && !binaryName.isBlank()) {
                putLocalDeclarationIndexValue(index, binaryName, declaration);
                putLocalDeclarationIndexValue(index, flattenedBinaryAlias(binaryName), declaration);
                if (declaration.simpleName() != null && !declaration.simpleName().isBlank()) {
                    putLocalDeclarationIndexValue(index, declaration.simpleName(), declaration);
                }
            }
        }
        return Map.copyOf(index);
    }

        private void putDeclarationIndexValue(
            Map<String, QinIrClassDeclaration> index,
            String key,
            QinIrClassDeclaration declaration) {
        if (index == null || key == null || key.isBlank() || declaration == null) {
            return;
        }
        QinIrClassDeclaration selected = chooseDeclarationIndexValue(index.get(key), declaration);
        if (selected != null) {
            index.put(key, selected);
        }
    }

        private void putLocalDeclarationIndexValue(
            Map<String, QinIrClassDeclaration> index,
            String key,
            QinIrClassDeclaration declaration) {
        if (index == null || key == null || key.isBlank() || declaration == null) {
            return;
        }
        QinIrClassDeclaration existing = index.get(key);
        if (existing != null
                && declaration.binaryName() != null
                && !declaration.binaryName().isBlank()
                && !Objects.equals(existing.binaryName(), declaration.binaryName())
                && (key.equals(declaration.binaryName())
                        || key.equals(flattenedBinaryAlias(declaration.binaryName())))) {
            index.put(key, declaration);
            return;
        }
        putDeclarationIndexValue(index, key, declaration);
    }

        private QinIrClassDeclaration chooseDeclarationIndexValue(
            QinIrClassDeclaration existing,
            QinIrClassDeclaration candidate) {
        if (candidate == null) {
            return existing;
        }
        if (existing == null) {
            return candidate;
        }
        if (Objects.equals(existing.binaryName(), candidate.binaryName())) {
            DeclarationIndexFacts existingFacts = facts(existing);
            DeclarationIndexFacts candidateFacts = facts(candidate);
            int existingConstructorCount = existingFacts.constructorCount();
            int candidateConstructorCount = candidateFacts.constructorCount();
            if (candidateConstructorCount > 0 && existingConstructorCount == 0) {
                return candidate;
            }
            if (existingConstructorCount > 0 && candidateConstructorCount == 0) {
                return existing;
            }
            if (candidateFacts.completenessScore() > existingFacts.completenessScore()) {
                return candidate;
            }
        }
        return existing;
    }

        private DeclarationIndexFacts facts(QinIrClassDeclaration declaration) {
            if (declaration == null) {
                return DeclarationIndexFacts.EMPTY;
            }
            return factsByDeclaration.computeIfAbsent(declaration, this::calculateFacts);
        }

        private DeclarationIndexFacts calculateFacts(QinIrClassDeclaration declaration) {
            int constructorCount = 0;
            for (QinIrMethodDeclaration method : declaration.methods()) {
                if ("constructor".equals(method.name())) {
                    constructorCount++;
                }
            }
            int completenessScore = declaration.fields().size()
                    + declaration.methods().size() * 4
                    + constructorCount * 8
                    + declaration.staticInitializers().size();
            return new DeclarationIndexFacts(constructorCount, completenessScore);
        }
    }

    private record DeclarationIndexFacts(int constructorCount, int completenessScore) {
        private static final DeclarationIndexFacts EMPTY = new DeclarationIndexFacts(0, 0);
    }

    private String flattenedBinaryAlias(String binaryName) {
        return binaryName == null ? null : binaryName.replace('.', '_');
    }

    private String canonicalQinHostRuntimeBinaryName(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            return binaryName;
        }
        if (binaryName.startsWith("com.qin.lang.runtime.")) {
            return binaryName;
        }
        String flattenedPrefix = "com_qin_lang_runtime_";
        if (binaryName.startsWith(flattenedPrefix)) {
            return "com.qin.lang.runtime." + binaryName.substring(flattenedPrefix.length());
        }
        return binaryName;
    }

    private boolean isQinHostRuntimeBinaryName(String binaryName) {
        return !Objects.equals(canonicalQinHostRuntimeBinaryName(binaryName), binaryName)
                || (binaryName != null && binaryName.startsWith("com.qin.lang.runtime."));
    }

    private ClassHierarchyResolver localClassHierarchyResolver(
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declarationIndex == null || declarationIndex.isEmpty()) {
            return ClassHierarchyResolver.defaultResolver();
        }
        Set<String> visitedBinaryNames = new LinkedHashSet<>();
        Set<ClassDesc> interfaceDescs = new LinkedHashSet<>();
        Map<ClassDesc, ClassDesc> superClasses = new LinkedHashMap<>();
        for (QinIrClassDeclaration declaration : declarationIndex.values()) {
            if (declaration == null
                    || declaration.binaryName() == null
                    || declaration.binaryName().isBlank()
                    || !visitedBinaryNames.add(declaration.binaryName())) {
                continue;
            }
            ClassDesc classDesc = toReferenceClassDesc(declaration.binaryName());
            if (declaration.interfaceClass()) {
                interfaceDescs.add(classDesc);
            }
            for (QinIrTypeRef implementsType : declaration.implementsTypes()) {
                if (implementsType.kind() == QinIrTypeKind.CLASS
                        && implementsType.binaryName() != null
                        && !implementsType.binaryName().isBlank()) {
                    interfaceDescs.add(toReferenceClassDesc(
                            effectiveLocalReferenceBinaryName(implementsType.binaryName(), declarationIndex)));
                }
            }
            QinIrTypeRef superType = declaration.superType();
            if (superType != null
                    && superType.kind() == QinIrTypeKind.CLASS
                    && superType.binaryName() != null
                    && !superType.binaryName().isBlank()
                    && !"java.lang.Object".equals(effectiveSuperclassBinaryName(superType))) {
                superClasses.put(classDesc, toReferenceClassDesc(effectiveSuperclassBinaryName(superType)));
            }
        }
        return ClassHierarchyResolver.of(interfaceDescs, superClasses)
                .orElse(generatedClassHierarchyFallbackResolver())
                .cached();
    }

    private ClassHierarchyResolver generatedClassHierarchyFallbackResolver() {
        ClassHierarchyResolver defaultResolver = ClassHierarchyResolver.defaultResolver();
        return classDesc -> {
            try {
                ClassHierarchyResolver.ClassHierarchyInfo defaultInfo =
                        defaultResolver.getClassInfo(classDesc);
                if (defaultInfo != null) {
                    return defaultInfo;
                }
            } catch (IllegalArgumentException error) {
                if (!isGeneratedClassDesc(classDesc)) {
                    throw error;
                }
            }
            return isGeneratedClassDesc(classDesc)
                    ? ClassHierarchyResolver.ClassHierarchyInfo.ofClass(OBJECT_DESC)
                    : null;
        };
    }

    private boolean isGeneratedClassDesc(ClassDesc classDesc) {
        if (classDesc == null) {
            return false;
        }
        String descriptor = classDesc.descriptorString();
        return descriptor.startsWith("L")
                && descriptor.endsWith(";")
                && descriptor.indexOf('/') < 0
                && descriptor.contains("_")
                && !descriptor.startsWith("Ljava_")
                && !descriptor.startsWith("Ljavax_")
                && !descriptor.startsWith("Ljdk_")
                && !descriptor.startsWith("Lsun_");
    }

    private byte[] compileClass(
            QinIrClassDeclaration declaration,
            String binaryClassName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        Objects.requireNonNull(declaration, "declaration cannot be null");
        Objects.requireNonNull(binaryClassName, "binaryClassName cannot be null");
        Objects.requireNonNull(declarationIndex, "declarationIndex cannot be null");

        List<FunctionDefinitionHelper> functionDefinitionHelpers =
                collectFunctionDefinitionHelpers(declaration);
        Map<QinIrObjectLiteral, String> previousFunctionDefinitionHelpers =
                activeFunctionDefinitionHelpers;
        String previousBinaryClassName = activeBinaryClassName;
        Map<String, QinIrClassDeclaration> previousDeclarationIndex = activeDeclarationIndex;
        Map<QinIrObjectLiteral, String> helperNames = new LinkedHashMap<>();
        for (FunctionDefinitionHelper helper : functionDefinitionHelpers) {
            helperNames.put(helper.definition(), helper.name());
        }
        activeFunctionDefinitionHelpers = Map.copyOf(helperNames);
        activeBinaryClassName = binaryClassName;
        activeDeclarationIndex = declarationIndex;

        ClassFile classFile = ClassFile.of(
                ClassFile.ClassHierarchyResolverOption.of(localClassHierarchyResolver(declarationIndex)));
        try {
            return classFile.build(ClassDesc.of(binaryClassName), builder -> {
            builder.withVersion(DEFAULT_CLASSFILE_MAJOR_VERSION, 0);
            boolean interfaceClass = declaration.interfaceClass();
            boolean abstractClass = hasAbstractMethod(declaration);
            builder.withFlags(interfaceClass
                    ? ClassFile.ACC_PUBLIC | ClassFile.ACC_INTERFACE | ClassFile.ACC_ABSTRACT
                    : ClassFile.ACC_PUBLIC | ClassFile.ACC_SUPER | (abstractClass ? ClassFile.ACC_ABSTRACT : 0));
            builder.withSuperclass(interfaceClass ? ClassDesc.of("java.lang.Object") : resolveSuperclass(declaration.superType()));
            List<ClassDesc> interfaceSymbols = interfaceSymbols(declaration.implementsTypes(), declarationIndex);
            if (!interfaceSymbols.isEmpty()) {
                builder.withInterfaceSymbols(interfaceSymbols);
            }

            RuntimeVisibleAnnotationsAttribute classAnnotations = createAnnotationsAttribute(declaration.annotations());
            if (classAnnotations != null) {
                builder.with(classAnnotations);
            }
            builder.withMethodBody(
                    "__qin_generated_declaration_class",
                    MethodTypeDesc.ofDescriptor("()Z"),
                    ClassFile.ACC_PUBLIC | ClassFile.ACC_STATIC | ClassFile.ACC_SYNTHETIC,
                    code -> {
                        code.iconst_1();
                        code.ireturn();
                    });

            for (QinIrFieldDeclaration field : declaration.fields()) {
                builder.withField(field.name(), toClassDesc(field.type()), fieldBuilder -> {
                    fieldBuilder.withFlags(interfaceClass
                            ? ClassFile.ACC_PUBLIC | ClassFile.ACC_STATIC | ClassFile.ACC_FINAL
                            : ClassFile.ACC_PUBLIC | (field.staticField() ? ClassFile.ACC_STATIC : 0));
                    RuntimeVisibleAnnotationsAttribute fieldAnnotations =
                            createAnnotationsAttribute(field.annotations());
                    if (fieldAnnotations != null) {
                        fieldBuilder.with(fieldAnnotations);
                    }
                });
                if (!interfaceClass && !hasDeclarationMethod(declaration, getterName(field), 0)) {
                    builder.withMethodBody(
                            getterName(field),
                            MethodTypeDesc.of(toClassDesc(field.type())),
                            ClassFile.ACC_PUBLIC | (field.staticField() ? ClassFile.ACC_STATIC : 0),
                            code -> emitFieldGetterBody(code, binaryClassName, field));
                }
                if (!interfaceClass && !hasDeclarationMethod(declaration, setterName(field), 1)) {
                    builder.withMethodBody(
                            setterName(field),
                            MethodTypeDesc.ofDescriptor("(" + toClassDesc(field.type()).descriptorString() + ")V"),
                            ClassFile.ACC_PUBLIC | (field.staticField() ? ClassFile.ACC_STATIC : 0),
                            code -> emitFieldSetterBody(code, binaryClassName, field));
                }
            }

            List<QinIrFieldDeclaration> staticFields = staticFields(declaration.fields());
            if (!staticFields.isEmpty()) {
                builder.withMethod(
                        "<clinit>",
                        VOID_INIT,
                        ClassFile.ACC_STATIC,
                        methodBuilder -> methodBuilder.withCode(code -> {
                            for (QinIrFieldDeclaration field : staticFields) {
                                if (field.initializer() != null) {
                                    emitFieldInitializer(
                                            code,
                                            declaration,
                                            binaryClassName,
                                            field,
                                            null,
                                            declarationIndex,
                                            LocalFrame.forMethodParameters(null));
                                }
                            }
                            code.return_();
                        }));
            }

            Set<String> emittedConstructorDescriptors = new LinkedHashSet<>();
            List<QinIrMethodDeclaration> explicitConstructors = interfaceClass ? List.of() : explicitConstructors(declaration);
            for (QinIrMethodDeclaration constructor : explicitConstructors) {
                MethodTypeDesc constructorDescriptor = toConstructorDescriptorForParameters(constructor.parameters());
                emittedConstructorDescriptors.add(constructorDescriptor.descriptorString());
                builder.withMethod(
                        "<init>",
                        constructorDescriptor,
                        declarationMethodFlags(constructor, false),
                        methodBuilder -> {
                            MethodParametersAttribute constructorParameters =
                                    createMethodParametersAttribute(constructor);
                            if (constructorParameters != null) {
                                methodBuilder.with(constructorParameters);
                            }
                            methodBuilder.withCode(code -> emitExplicitConstructorBody(
                                    code,
                                    declaration,
                                    binaryClassName,
                                    constructor,
                                    declarationIndex));
                        });
                if (acceptsZeroArgumentsThroughRestParameter(constructor)
                        && emittedConstructorDescriptors.add(VOID_INIT.descriptorString())) {
                    MethodTypeDesc restConstructorDescriptor = constructorDescriptor;
                    builder.withMethodBody("<init>", VOID_INIT, ClassFile.ACC_PUBLIC, code -> {
                        code.aload(0);
                        code.iconst_0();
                        code.anewarray(OBJECT_DESC);
                        code.invokespecial(
                                ClassDesc.of(binaryClassName),
                                "<init>",
                                restConstructorDescriptor);
                        code.return_();
                    });
                }
            }

            if (!interfaceClass
                    && explicitConstructors.isEmpty()
                    && hasNoArgSuperclassConstructor(declaration.superType(), declarationIndex)) {
                emittedConstructorDescriptors.add(VOID_INIT.descriptorString());
                builder.withMethodBody("<init>", VOID_INIT, ClassFile.ACC_PUBLIC, code -> {
                    code.aload(0);
                    code.invokespecial(resolveSuperclass(declaration.superType()), "<init>", VOID_INIT);
                    for (QinIrFieldDeclaration field : declaration.fields()) {
                        if (!field.staticField()) {
                            emitFieldInitializer(
                                    code,
                                    declaration,
                                    binaryClassName,
                                    field,
                                    null,
                                    declarationIndex,
                                    LocalFrame.forMethodParameters(null));
                        }
                    }
                    code.return_();
                });
            }

            if (!interfaceClass && explicitConstructors.isEmpty()) {
                emitJavaSuperclassConstructors(builder, declaration, binaryClassName, declarationIndex);
                emitLocalSuperclassConstructors(
                        builder,
                        declaration,
                        binaryClassName,
                        declarationIndex,
                        emittedConstructorDescriptors);
            }

            List<QinIrFieldDeclaration> instanceFields = instanceFields(declaration.fields());
            if (!interfaceClass
                    && explicitConstructors.isEmpty()
                    && !instanceFields.isEmpty()
                    && hasNoArgSuperclassConstructor(declaration.superType(), declarationIndex)) {
                builder.withMethod(
                        "<init>",
                        toConstructorDescriptor(instanceFields),
                        ClassFile.ACC_PUBLIC,
                        methodBuilder -> {
                            MethodParametersAttribute constructorParameters =
                                    createFieldConstructorParametersAttribute(instanceFields);
                            if (constructorParameters != null) {
                                methodBuilder.with(constructorParameters);
                            }
                            methodBuilder.withCode(code -> emitAllArgsConstructorBody(code, declaration, binaryClassName));
                        });
            }

            for (FunctionDefinitionHelper helper : functionDefinitionHelpers) {
                QinIrMethodDeclaration helperMethod = new QinIrMethodDeclaration(
                        helper.name(),
                        QinIrTypeRef.classType("java.lang.Object"),
                        List.of(),
                        List.of(),
                        null,
                        null,
                        true);
                builder.withMethodBody(
                        helper.name(),
                        MethodTypeDesc.of(OBJECT_DESC),
                        ClassFile.ACC_PRIVATE | ClassFile.ACC_STATIC,
                        code -> {
                            emitObjectLiteral(
                                    code,
                                    declaration,
                                    helperMethod,
                                    declarationIndex,
                                    LocalFrame.forMethodParameters(helperMethod),
                                    helper.definition());
                            code.areturn();
                        });
            }

            Set<String> constructorBackedHelperNames = new LinkedHashSet<>();
            for (QinIrMethodDeclaration constructor : explicitConstructors) {
                if (isGeneratedConstructorHelper(constructor)) {
                    constructorBackedHelperNames.add(constructor.name());
                }
            }
            for (QinIrMethodDeclaration method : declaration.methods()) {
                if (isConstructorMethod(method)
                        || (isGeneratedConstructorHelper(method)
                                && constructorBackedHelperNames.contains(method.name()))) {
                    continue;
                }
                if (requiresSubhutiRuleWrapper(method, interfaceClass)) {
                    emitSubhutiRuleRawMethod(builder, declaration, method, declarationIndex);
                }
                if (method.abstractMethod()) {
                    builder.withMethod(
                            method.name(),
                            toMethodDescriptor(method),
                            declarationMethodFlags(method, interfaceClass),
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
                            });
                    continue;
                }
                builder.withMethod(
                        method.name(),
                        toMethodDescriptor(method),
                        declarationMethodFlags(method, interfaceClass),
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

                            methodBuilder.withCode(code -> {
                                if (requiresSubhutiRuleWrapper(method, interfaceClass)) {
                                    emitSubhutiRuleWrapperBody(code, declaration, declarationIndex, method);
                                } else {
                                    emitMethodBody(code, declaration, method, declarationIndex);
                                }
                            });
                        });
            }
            emitReturnDescriptorOverrideBridges(builder, declaration, binaryClassName, declarationIndex);
            emitInheritedObjectArrayDispatcherBridges(builder, declaration, binaryClassName, declarationIndex);
            });
        } finally {
            activeFunctionDefinitionHelpers = previousFunctionDefinitionHelpers;
            activeBinaryClassName = previousBinaryClassName;
            activeDeclarationIndex = previousDeclarationIndex;
        }
    }

    private List<FunctionDefinitionHelper> collectFunctionDefinitionHelpers(QinIrClassDeclaration declaration) {
        Map<QinIrObjectLiteral, String> helpersByDefinition = new LinkedHashMap<>();
        for (QinIrFieldDeclaration field : declaration.fields()) {
            collectFunctionDefinitionHelpers(field.initializer(), helpersByDefinition);
        }
        for (QinIrMethodDeclaration method : declaration.methods()) {
            collectFunctionDefinitionHelpers(method.returnExpression(), helpersByDefinition);
            collectFunctionDefinitionHelpersFromExpressions(method.superArguments(), helpersByDefinition);
            collectFunctionDefinitionHelpers(method.bodyStatements(), helpersByDefinition);
            if (method.runtimeFunctionDefinition() != null) {
                registerFunctionDefinitionHelper(method.runtimeFunctionDefinition(), helpersByDefinition);
                collectFunctionDefinitionHelpers(method.runtimeFunctionDefinition(), helpersByDefinition);
            }
        }
        List<FunctionDefinitionHelper> helpers = new ArrayList<>();
        for (Map.Entry<QinIrObjectLiteral, String> entry : helpersByDefinition.entrySet()) {
            helpers.add(new FunctionDefinitionHelper(entry.getValue(), entry.getKey()));
        }
        return List.copyOf(helpers);
    }

    private void collectFunctionDefinitionHelpersFromExpressions(
            List<? extends QinIrExpression> expressions,
            Map<QinIrObjectLiteral, String> helpersByDefinition) {
        if (expressions == null) {
            return;
        }
        for (QinIrExpression expression : expressions) {
            collectFunctionDefinitionHelpers(expression, helpersByDefinition);
        }
    }

    private void collectFunctionDefinitionHelpers(
            List<QinIrStatement> statements,
            Map<QinIrObjectLiteral, String> helpersByDefinition) {
        if (statements == null) {
            return;
        }
        for (QinIrStatement statement : statements) {
            collectFunctionDefinitionHelpers(statement, helpersByDefinition);
        }
    }

    private void collectFunctionDefinitionHelpers(
            QinIrStatement statement,
            Map<QinIrObjectLiteral, String> helpersByDefinition) {
        if (statement == null) {
            return;
        }
        if (statement instanceof QinIrLocalDeclarationStatement localDeclaration) {
            collectFunctionDefinitionHelpers(localDeclaration.initializer(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrStatementExpression statementExpression) {
            collectFunctionDefinitionHelpers(statementExpression.expression(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrReturnStatement returnStatement) {
            collectFunctionDefinitionHelpers(returnStatement.value(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrIfStatement ifStatement) {
            collectFunctionDefinitionHelpers(ifStatement.test(), helpersByDefinition);
            collectFunctionDefinitionHelpers(ifStatement.consequent(), helpersByDefinition);
            collectFunctionDefinitionHelpers(ifStatement.alternate(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrForStatement forStatement) {
            for (QinIrLocalVariableDeclaration declaration : forStatement.initializerDeclarations()) {
                collectFunctionDefinitionHelpers(declaration.initializer(), helpersByDefinition);
            }
            collectFunctionDefinitionHelpersFromExpressions(forStatement.initializerExpressions(), helpersByDefinition);
            collectFunctionDefinitionHelpers(forStatement.test(), helpersByDefinition);
            collectFunctionDefinitionHelpersFromExpressions(forStatement.updateExpressions(), helpersByDefinition);
            collectFunctionDefinitionHelpers(forStatement.body(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrForEachStatement forEachStatement) {
            collectFunctionDefinitionHelpers(forEachStatement.iterable(), helpersByDefinition);
            collectFunctionDefinitionHelpers(forEachStatement.body(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrWhileStatementNode whileStatement) {
            collectFunctionDefinitionHelpers(whileStatement.test(), helpersByDefinition);
            collectFunctionDefinitionHelpers(whileStatement.body(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrDoWhileStatementNode doWhileStatement) {
            collectFunctionDefinitionHelpers(doWhileStatement.body(), helpersByDefinition);
            collectFunctionDefinitionHelpers(doWhileStatement.test(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrSwitchStatement switchStatement) {
            collectFunctionDefinitionHelpers(switchStatement.discriminant(), helpersByDefinition);
            for (QinIrSwitchCase switchCase : switchStatement.cases()) {
                collectFunctionDefinitionHelpers(switchCase.test(), helpersByDefinition);
                collectFunctionDefinitionHelpers(switchCase.consequent(), helpersByDefinition);
            }
            return;
        }
        if (statement instanceof QinIrTryStatement tryStatement) {
            for (QinIrTryResource resource : tryStatement.resources()) {
                collectFunctionDefinitionHelpers(resource.initializer(), helpersByDefinition);
                collectFunctionDefinitionHelpers(resource.reference(), helpersByDefinition);
            }
            collectFunctionDefinitionHelpers(tryStatement.tryBody(), helpersByDefinition);
            for (QinIrCatchClause catchClause : tryStatement.catchClauses()) {
                collectFunctionDefinitionHelpers(catchClause.body(), helpersByDefinition);
            }
            collectFunctionDefinitionHelpers(tryStatement.finallyBody(), helpersByDefinition);
            return;
        }
        if (statement instanceof QinIrThrowStatement throwStatement) {
            collectFunctionDefinitionHelpers(throwStatement.value(), helpersByDefinition);
        }
    }

    private void collectFunctionDefinitionHelpers(
            QinIrExpression expression,
            Map<QinIrObjectLiteral, String> helpersByDefinition) {
        if (expression == null) {
            return;
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCall
                && "Global".equals(builtinCall.receiverName())
                && "__qin_make_function__".equals(builtinCall.methodName())
                && builtinCall.arguments().size() == 1
                && builtinCall.arguments().get(0) instanceof QinIrObjectLiteral functionDefinition) {
            if (hasNonEmptyFunctionClosure(functionDefinition)) {
                collectFunctionDefinitionHelpers(functionDefinition, helpersByDefinition);
                return;
            }
            registerFunctionDefinitionHelper(functionDefinition, helpersByDefinition);
            collectFunctionDefinitionHelpers(functionDefinition, helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            for (QinIrObjectProperty property : objectLiteral.properties()) {
                collectFunctionDefinitionHelpers(property.value(), helpersByDefinition);
            }
            return;
        }
        if (expression instanceof QinIrShortCircuitExpression shortCircuitExpression) {
            collectFunctionDefinitionHelpers(shortCircuitExpression.left(), helpersByDefinition);
            collectFunctionDefinitionHelpers(shortCircuitExpression.right(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            collectFunctionDefinitionHelpersFromExpressions(arrayLiteral.elements(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCall) {
            collectFunctionDefinitionHelpersFromExpressions(builtinCall.arguments(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCall) {
            collectFunctionDefinitionHelpers(methodCall.receiver(), helpersByDefinition);
            collectFunctionDefinitionHelpersFromExpressions(methodCall.arguments(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrStaticMethodCallExpression staticMethodCall) {
            collectFunctionDefinitionHelpersFromExpressions(staticMethodCall.arguments(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrSuperMethodCallExpression superMethodCall) {
            collectFunctionDefinitionHelpersFromExpressions(superMethodCall.arguments(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            collectFunctionDefinitionHelpersFromExpressions(javaNewExpression.arguments(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccess) {
            collectFunctionDefinitionHelpers(propertyAccess.receiver(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrElementAccessExpression elementAccess) {
            collectFunctionDefinitionHelpers(elementAccess.receiver(), helpersByDefinition);
            collectFunctionDefinitionHelpers(elementAccess.index(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            collectFunctionDefinitionHelpersFromExpressions(sequenceExpression.leadingExpressions(), helpersByDefinition);
            collectFunctionDefinitionHelpers(sequenceExpression.resultExpression(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrSpreadArgumentExpression spreadArgument) {
            collectFunctionDefinitionHelpers(spreadArgument.expression(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrAssignmentExpression assignmentExpression) {
            collectFunctionDefinitionHelpers(assignmentExpression.target(), helpersByDefinition);
            collectFunctionDefinitionHelpers(assignmentExpression.value(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrUpdateExpression updateExpression) {
            collectFunctionDefinitionHelpers(updateExpression.target(), helpersByDefinition);
            return;
        }
        if (expression instanceof QinIrBoundMethodReferenceExpression boundMethodReferenceExpression) {
            collectFunctionDefinitionHelpers(boundMethodReferenceExpression.receiver(), helpersByDefinition);
        }
    }

    private void registerFunctionDefinitionHelper(
            QinIrObjectLiteral functionDefinition,
            Map<QinIrObjectLiteral, String> helpersByDefinition) {
        helpersByDefinition.computeIfAbsent(
                functionDefinition,
                ignored -> "__qin_function_definition_" + helpersByDefinition.size());
    }

    private Map<String, QinIrClassDeclaration> buildDeclarationIndex(List<QinIrClassDeclaration> declarations) {
        return buildDeclarationIndex(declarations, Map.of());
    }

    private List<QinIrMethodDeclaration> explicitConstructors(QinIrClassDeclaration declaration) {
        List<QinIrMethodDeclaration> constructors = new ArrayList<>();
        List<QinIrMethodDeclaration> generatedHelpers = new ArrayList<>();
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (isConstructorMethod(method)) {
                constructors.add(method);
                continue;
            }
            if (isGeneratedConstructorHelper(method)) {
                generatedHelpers.add(method);
            }
        }
        return constructors.isEmpty()
                ? List.copyOf(generatedHelpers)
                : List.copyOf(constructors);
    }

    private boolean isConstructorMethod(QinIrMethodDeclaration method) {
        return method != null && "constructor".equals(method.name());
    }

    private boolean isGeneratedConstructorHelper(QinIrMethodDeclaration method) {
        return method != null
                && method.name() != null
                && method.name().startsWith("__qin_constructor_");
    }

    private boolean acceptsZeroArgumentsThroughRestParameter(QinIrMethodDeclaration constructor) {
        if (constructor == null || constructor.parameters().isEmpty()) {
            return false;
        }
        return constructor.parameters().size() == 1
                && constructor.parameters().get(0).varargs();
    }

    private ClassDesc resolveSuperclass(QinIrTypeRef superType) {
        if (superType == null || superType.binaryName() == null || superType.binaryName().isBlank()) {
            return OBJECT_DESC;
        }
        return ClassDesc.of(effectiveSuperclassBinaryName(superType));
    }

    private List<ClassDesc> interfaceSymbols(
            List<QinIrTypeRef> interfaceTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (interfaceTypes == null || interfaceTypes.isEmpty()) {
            return List.of();
        }
        List<ClassDesc> symbols = new ArrayList<>();
        for (QinIrTypeRef interfaceType : interfaceTypes) {
            if (interfaceType == null
                    || interfaceType.kind() != QinIrTypeKind.CLASS
                    || interfaceType.binaryName() == null
                    || interfaceType.binaryName().isBlank()) {
                continue;
            }
            symbols.add(toReferenceClassDesc(
                    effectiveLocalReferenceBinaryName(interfaceType.binaryName(), declarationIndex)));
        }
        return List.copyOf(symbols);
    }

    private String effectiveLocalReferenceBinaryName(
            String binaryName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (isQinHostRuntimeBinaryName(binaryName)) {
            return canonicalQinHostRuntimeBinaryName(binaryName);
        }
        if (binaryName == null || binaryName.isBlank()
                || declarationIndex == null || declarationIndex.isEmpty()) {
            return binaryName;
        }
        String flattenedAlias = flattenedBinaryAlias(binaryName);
        QinIrClassDeclaration flattenedLocal = declarationIndex.get(flattenedAlias);
        if (flattenedLocal != null
                && flattenedLocal.binaryName() != null
                && !flattenedLocal.binaryName().isBlank()
                && !Objects.equals(flattenedLocal.binaryName(), binaryName)
                && Objects.equals(flattenedLocal.binaryName(), flattenedAlias)) {
            return flattenedLocal.binaryName();
        }
        QinIrClassDeclaration local = declarationIndex.get(binaryName);
        if (local == null) {
            local = flattenedLocal;
        }
        if (local == null
                || local.binaryName() == null
                || local.binaryName().isBlank()) {
            return binaryName;
        }
        return local.binaryName();
    }

    private String effectiveSuperclassBinaryName(QinIrTypeRef superType) {
        if (superType == null || superType.binaryName() == null || superType.binaryName().isBlank()) {
            return "java.lang.Object";
        }
        if ("java.lang.Enum".equals(superType.binaryName())) {
            return "java.lang.Object";
        }
        return superType.binaryName();
    }

    private boolean hasNoArgSuperclassConstructor(QinIrTypeRef superType) {
        if (superType == null
                || superType.binaryName() == null
                || superType.binaryName().isBlank()
                || "java.lang.Object".equals(effectiveSuperclassBinaryName(superType))) {
            return true;
        }
        try {
            Class.forName(effectiveSuperclassBinaryName(superType)).getConstructor();
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
            String binaryClassName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declaration.superType() == null
                || declaration.superType().binaryName() == null
                || declaration.superType().binaryName().isBlank()
                || "java.lang.Object".equals(effectiveSuperclassBinaryName(declaration.superType()))) {
            return;
        }
        Class<?> superClass;
        try {
            superClass = Class.forName(effectiveSuperclassBinaryName(declaration.superType()));
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
                        for (QinIrFieldDeclaration field : instanceFields(declaration.fields())) {
                            emitFieldInitializer(
                                    code,
                                    declaration,
                                    binaryClassName,
                                    field,
                                    null,
                                    declarationIndex,
                                    LocalFrame.forMethodParameters(null));
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
                        for (QinIrFieldDeclaration field : instanceFields(declaration.fields())) {
                            emitFieldInitializer(
                                    code,
                                    declaration,
                                    binaryClassName,
                                    field,
                                    null,
                                    declarationIndex,
                                    LocalFrame.forMethodParameters(null));
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
        List<QinIrMethodDeclaration> explicitConstructors = explicitConstructors(declaration);
        if (!explicitConstructors.isEmpty()) {
            for (QinIrMethodDeclaration constructor : explicitConstructors) {
                List<QinIrTypeRef> parameterTypes = new ArrayList<>();
                for (QinIrParameter parameter : constructor.parameters()) {
                    parameterTypes.add(parameter.type());
                }
                if (acceptsZeroArgumentsThroughRestParameter(constructor)) {
                    constructors.add(List.of());
                }
                constructors.add(List.copyOf(parameterTypes));
            }
            return List.copyOf(constructors);
        }
        QinIrClassDeclaration localSuperclass = declaration.superType() == null
                ? null
                : declarationIndex.get(declaration.superType().binaryName());
        List<List<QinIrTypeRef>> localSuperclassConstructors = constructorParameterListsForLocalDeclaration(
                localSuperclass,
                declarationIndex,
                visitedLocalTypes);
        boolean superHasNoArgConstructor = hasNoArgSuperclassConstructor(declaration.superType())
                || localSuperclassConstructors.stream().anyMatch(List::isEmpty);
        if (superHasNoArgConstructor) {
            constructors.add(List.of());
        }
        constructors.addAll(javaSuperclassConstructorParameterLists(declaration.superType()));
        constructors.addAll(localSuperclassConstructors);
        List<QinIrFieldDeclaration> instanceFields = instanceFields(declaration.fields());
        if (!instanceFields.isEmpty() && superHasNoArgConstructor) {
            List<QinIrTypeRef> fieldTypes = new ArrayList<>();
            for (QinIrFieldDeclaration field : instanceFields) {
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
                || "java.lang.Object".equals(effectiveSuperclassBinaryName(superType))) {
            return List.of();
        }
        Class<?> superClass;
        try {
            superClass = Class.forName(effectiveSuperclassBinaryName(superType));
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

    private MethodTypeDesc toJavaMethodDescriptor(Class<?> returnType, Class<?>[] parameterTypes) {
        List<ClassDesc> parameterDescs = new ArrayList<>();
        for (Class<?> parameterType : parameterTypes) {
            parameterDescs.add(toClassDesc(parameterType));
        }
        return MethodTypeDesc.of(toClassDesc(returnType), parameterDescs);
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
        if (type == long.class) {
            return ClassDesc.ofDescriptor("J");
        }
        if (type == float.class) {
            return ClassDesc.ofDescriptor("F");
        }
        if (type == short.class) {
            return ClassDesc.ofDescriptor("S");
        }
        if (type == byte.class) {
            return ClassDesc.ofDescriptor("B");
        }
        if (type == char.class) {
            return ClassDesc.ofDescriptor("C");
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

    private void emitReturnDescriptorOverrideBridges(
            ClassBuilder builder,
            QinIrClassDeclaration declaration,
            String binaryClassName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declaration == null || declaration.interfaceClass()) {
            return;
        }
        Set<String> localDescriptors = new LinkedHashSet<>();
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (isConstructorMethod(method) || method.staticMethod()) {
                continue;
            }
            localDescriptors.add(method.name() + toMethodDescriptor(method).descriptorString());
        }
        Set<String> emittedBridgeDescriptors = new LinkedHashSet<>();
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (isConstructorMethod(method) || method.staticMethod() || method.abstractMethod()) {
                continue;
            }
            QinIrMethodDeclaration inherited = findInheritedSameParametersDifferentReturn(
                    declaration.superType(),
                    method,
                    declarationIndex,
                    new LinkedHashSet<>());
            if (inherited == null) {
                continue;
            }
            MethodTypeDesc bridgeDescriptor = methodDescriptorWithReturn(method, inherited.returnType());
            String bridgeKey = method.name() + bridgeDescriptor.descriptorString();
            if (localDescriptors.contains(bridgeKey) || !emittedBridgeDescriptors.add(bridgeKey)) {
                continue;
            }
            MethodTypeDesc targetDescriptor = toMethodDescriptor(method);
            builder.withMethodBody(
                    method.name(),
                    bridgeDescriptor,
                    ClassFile.ACC_PUBLIC | 0x0040 | 0x1000,
                    code -> {
                        code.aload(0);
                        int localSlot = 1;
                        for (QinIrParameter parameter : method.parameters()) {
                            loadLocalForType(code, parameter.type(), localSlot, parameter.name());
                            localSlot += localSlotWidth(parameter.type());
                        }
                        code.invokevirtual(ClassDesc.of(binaryClassName), method.name(), targetDescriptor);
                        emitReturnForType(code, method.returnType(), inherited.returnType());
                    });
        }
    }

    private void emitInheritedObjectArrayDispatcherBridges(
            ClassBuilder builder,
            QinIrClassDeclaration declaration,
            String binaryClassName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declaration == null || declaration.interfaceClass()) {
            return;
        }
        Set<String> localDescriptors = new LinkedHashSet<>();
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (isConstructorMethod(method) || method.staticMethod()) {
                continue;
            }
            localDescriptors.add(method.name() + toMethodDescriptor(method).descriptorString());
        }
        Set<String> emittedBridgeDescriptors = new LinkedHashSet<>();
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (isConstructorMethod(method) || method.staticMethod() || method.abstractMethod()) {
                continue;
            }
            QinIrMethodDeclaration inheritedDispatcher = findInheritedObjectArrayDispatcherMethod(
                    declaration.superType(),
                    method,
                    declarationIndex,
                    new LinkedHashSet<>());
            if (inheritedDispatcher == null) {
                continue;
            }
            MethodTypeDesc bridgeDescriptor = toMethodDescriptor(inheritedDispatcher);
            String bridgeKey = method.name() + bridgeDescriptor.descriptorString();
            if (localDescriptors.contains(bridgeKey) || !emittedBridgeDescriptors.add(bridgeKey)) {
                continue;
            }
            MethodTypeDesc targetDescriptor = toMethodDescriptor(method);
            builder.withMethodBody(
                    method.name(),
                    bridgeDescriptor,
                    ClassFile.ACC_PUBLIC | ClassFile.ACC_VARARGS | 0x0040 | 0x1000,
                    code -> emitObjectArrayDispatcherBridgeBody(
                            code,
                            binaryClassName,
                            method,
                            inheritedDispatcher.returnType(),
                            targetDescriptor));
        }
    }

    private QinIrMethodDeclaration findInheritedObjectArrayDispatcherMethod(
            QinIrTypeRef superType,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            Set<String> visitedBinaryNames) {
        if (superType == null
                || superType.kind() != QinIrTypeKind.CLASS
                || superType.binaryName() == null
                || declarationIndex == null) {
            return null;
        }
        QinIrClassDeclaration superDeclaration = resolveIndexedDeclaration(declarationIndex, superType.binaryName());
        if (superDeclaration == null || !visitedBinaryNames.add(superDeclaration.binaryName())) {
            return null;
        }
        for (QinIrMethodDeclaration candidate : superDeclaration.methods()) {
            if (candidate.staticMethod()
                    || isConstructorMethod(candidate)
                    || !candidate.name().equals(method.name())
                    || !isSingleObjectArrayVarargsMethod(candidate)
                    || parameterDescriptor(candidate).equals(parameterDescriptor(method))) {
                continue;
            }
            if (method.parameters().isEmpty() || canBridgeObjectArrayDispatcher(candidate, method)) {
                return candidate;
            }
        }
        return findInheritedObjectArrayDispatcherMethod(
                superDeclaration.superType(),
                method,
                declarationIndex,
                visitedBinaryNames);
    }

    private boolean isSingleObjectArrayVarargsMethod(QinIrMethodDeclaration method) {
        if (method == null || method.parameters().size() != 1 || !method.parameters().get(0).varargs()) {
            return false;
        }
        QinIrTypeRef parameterType = method.parameters().get(0).type();
        return parameterType != null
                && parameterType.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object[]".equals(parameterType.binaryName());
    }

    private boolean canBridgeObjectArrayDispatcher(
            QinIrMethodDeclaration dispatcher,
            QinIrMethodDeclaration target) {
        return isSingleObjectArrayVarargsMethod(dispatcher)
                && target != null
                && target.parameters().size() == 1
                && target.parameters().get(0).type().kind() == QinIrTypeKind.CLASS;
    }

    private void emitObjectArrayDispatcherBridgeBody(
            java.lang.classfile.CodeBuilder code,
            String binaryClassName,
            QinIrMethodDeclaration targetMethod,
            QinIrTypeRef bridgeReturnType,
            MethodTypeDesc targetDescriptor) {
        if (targetMethod.parameters().size() > 1) {
            throw new IllegalArgumentException(
                    "Object[] dispatcher bridge supports zero or one target parameter: " + targetMethod.name());
        }
        code.aload(0);
        if (targetMethod.parameters().size() == 1) {
            java.lang.classfile.Label unsupportedLabel = code.newLabel();
            code.aload(1);
            code.arraylength();
            code.iconst_1();
            code.if_icmpne(unsupportedLabel);
            code.aload(1);
            code.iconst_0();
            code.aaload();
            QinIrParameter parameter = targetMethod.parameters().get(0);
            coerceValueForTargetType(
                    code,
                    QinIrTypeRef.classType("java.lang.Object"),
                    parameter.type());
            code.invokevirtual(ClassDesc.of(binaryClassName), targetMethod.name(), targetDescriptor);
            emitReturnForType(code, targetMethod.returnType(), bridgeReturnType);

            code.labelBinding(unsupportedLabel);
            code.new_(ClassDesc.of("java.lang.IllegalArgumentException"));
            code.dup();
            code.ldc("Unsupported Java overload: " + targetMethod.name());
            code.invokespecial(
                    ClassDesc.of("java.lang.IllegalArgumentException"),
                    "<init>",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)V"));
            code.athrow();
            return;
        }
        code.invokevirtual(ClassDesc.of(binaryClassName), targetMethod.name(), targetDescriptor);
        emitReturnForType(code, targetMethod.returnType(), bridgeReturnType);
    }

    private QinIrMethodDeclaration findInheritedSameParametersDifferentReturn(
            QinIrTypeRef superType,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            Set<String> visitedBinaryNames) {
        if (superType == null
                || superType.kind() != QinIrTypeKind.CLASS
                || superType.binaryName() == null
                || declarationIndex == null) {
            return null;
        }
        QinIrClassDeclaration superDeclaration = resolveIndexedDeclaration(declarationIndex, superType.binaryName());
        if (superDeclaration == null || !visitedBinaryNames.add(superDeclaration.binaryName())) {
            return null;
        }
        String parameterDescriptor = parameterDescriptor(method);
        for (QinIrMethodDeclaration candidate : superDeclaration.methods()) {
            if (candidate.staticMethod()
                    || isConstructorMethod(candidate)
                    || !candidate.name().equals(method.name())
                    || !parameterDescriptor(candidate).equals(parameterDescriptor)) {
                continue;
            }
            if (!toClassDesc(candidate.returnType()).descriptorString()
                    .equals(toClassDesc(method.returnType()).descriptorString())) {
                return candidate;
            }
            return null;
        }
        return findInheritedSameParametersDifferentReturn(
                superDeclaration.superType(),
                method,
                declarationIndex,
                visitedBinaryNames);
    }

    private QinIrMethodDeclaration findInheritedDeclarationMethod(
            QinIrClassDeclaration declaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            String methodName,
            int parameterCount,
            Set<String> visitedBinaryNames) {
        if (declaration == null || declarationIndex == null || methodName == null) {
            return null;
        }
        if (!visitedBinaryNames.add(declaration.binaryName())) {
            return null;
        }
        for (QinIrMethodDeclaration candidate : declaration.methods()) {
            if (!candidate.staticMethod()
                    && !isConstructorMethod(candidate)
                    && candidate.name().equals(methodName)
                    && candidate.parameters().size() == parameterCount) {
                return candidate;
            }
        }
        if (declaration.superType() == null || declaration.superType().binaryName() == null) {
            return null;
        }
        QinIrClassDeclaration superDeclaration = resolveIndexedDeclaration(declarationIndex, declaration.superType().binaryName());
        if (superDeclaration == null) {
            return null;
        }
        return findInheritedDeclarationMethod(
                superDeclaration,
                declarationIndex,
                methodName,
                parameterCount,
                visitedBinaryNames);
    }

    private MethodTypeDesc methodDescriptorWithReturn(
            QinIrMethodDeclaration method,
            QinIrTypeRef returnType) {
        List<ClassDesc> parameterDescs = new ArrayList<>();
        for (var parameter : method.parameters()) {
            parameterDescs.add(toClassDesc(parameter.type()));
        }
        return MethodTypeDesc.of(toClassDesc(returnType), parameterDescs);
    }

    private String parameterDescriptor(QinIrMethodDeclaration method) {
        StringBuilder descriptor = new StringBuilder("(");
        for (QinIrParameter parameter : method.parameters()) {
            descriptor.append(toClassDesc(parameter.type()).descriptorString());
        }
        descriptor.append(')');
        return descriptor.toString();
    }

    private MethodTypeDesc toConstructorDescriptor(List<QinIrFieldDeclaration> fields) {
        List<ClassDesc> parameterDescs = new ArrayList<>();
        for (QinIrFieldDeclaration field : fields) {
            parameterDescs.add(toClassDesc(field.type()));
        }
        return MethodTypeDesc.ofDescriptor(MethodTypeDesc.of(ClassDesc.ofDescriptor("V"), parameterDescs).descriptorString());
    }

    private List<QinIrFieldDeclaration> instanceFields(List<QinIrFieldDeclaration> fields) {
        if (fields == null || fields.isEmpty()) {
            return List.of();
        }
        List<QinIrFieldDeclaration> instanceFields = new ArrayList<>();
        for (QinIrFieldDeclaration field : fields) {
            if (field != null && !field.staticField()) {
                instanceFields.add(field);
            }
        }
        return List.copyOf(instanceFields);
    }

    private List<QinIrFieldDeclaration> staticFields(List<QinIrFieldDeclaration> fields) {
        if (fields == null || fields.isEmpty()) {
            return List.of();
        }
        List<QinIrFieldDeclaration> staticFields = new ArrayList<>();
        for (QinIrFieldDeclaration field : fields) {
            if (field != null && field.staticField()) {
                staticFields.add(field);
            }
        }
        return List.copyOf(staticFields);
    }

    private MethodTypeDesc toConstructorDescriptorForParameters(List<QinIrParameter> parameters) {
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (QinIrParameter parameter : parameters) {
            parameterTypes.add(parameter.type());
        }
        return toConstructorDescriptorForTypes(parameterTypes);
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
            case STRING, CLASS -> toReferenceClassDesc(type.binaryName());
        };
    }

    private ClassDesc toReferenceClassDesc(String binaryName) {
        if (binaryName == null || binaryName.isBlank()) {
            return OBJECT_DESC;
        }
        binaryName = effectiveLocalReferenceBinaryName(binaryName, activeDeclarationIndex);
        if (binaryName.endsWith("[]")) {
            return ClassDesc.ofDescriptor(arrayDescriptor(binaryName, '/'));
        }
        if (binaryName.startsWith("[")) {
            return ClassDesc.ofDescriptor(binaryName.replace('.', '/'));
        }
        return ClassDesc.of(binaryName);
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

    private int declarationMethodFlags(QinIrMethodDeclaration method, boolean ownerInterface) {
        return ClassFile.ACC_PUBLIC
                | (method.staticMethod() ? ClassFile.ACC_STATIC : 0)
                | (method.abstractMethod() ? ClassFile.ACC_ABSTRACT : 0)
                | (hasVarargsParameter(method) ? ClassFile.ACC_VARARGS : 0);
    }

    private boolean requiresSubhutiRuleWrapper(QinIrMethodDeclaration method, boolean ownerInterface) {
        return method != null
                && !ownerInterface
                && !method.staticMethod()
                && !method.abstractMethod()
                && !isConstructorMethod(method)
                && !isGeneratedConstructorHelper(method)
                && method.name() != null
                && !method.name().startsWith("__qin_subhuti_raw_")
                && hasAnnotation(method.annotations(), SUBHUTI_RULE_ANNOTATION);
    }

    private boolean hasAnnotation(List<QinIrAnnotation> annotations, String ownerBinaryName) {
        if (annotations == null || ownerBinaryName == null) {
            return false;
        }
        for (QinIrAnnotation annotation : annotations) {
            if (ownerBinaryName.equals(annotation.ownerBinaryName())) {
                return true;
            }
        }
        return false;
    }

    private void emitSubhutiRuleRawMethod(
            ClassBuilder builder,
            QinIrClassDeclaration declaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        QinIrMethodDeclaration rawMethod = subhutiRawMethod(method);
        builder.withMethod(
                rawMethod.name(),
                toMethodDescriptor(rawMethod),
                declarationMethodFlags(rawMethod, false),
                rawMethodBuilder -> {
                    MethodParametersAttribute methodParameters = createMethodParametersAttribute(rawMethod);
                    if (methodParameters != null) {
                        rawMethodBuilder.with(methodParameters);
                    }
                    rawMethodBuilder.withCode(code -> emitMethodBody(code, declaration, rawMethod, declarationIndex));
                });
    }

    private QinIrMethodDeclaration subhutiRawMethod(QinIrMethodDeclaration method) {
        return new QinIrMethodDeclaration(
                subhutiRawMethodName(method),
                method.returnType(),
                method.parameters(),
                List.of(),
                method.returnExpression(),
                method.bodyStatements(),
                method.superArguments(),
                method.explicitSuperConstructorCall(),
                method.runtimeFunctionDefinition(),
                method.staticMethod(),
                method.abstractMethod());
    }

    private String subhutiRawMethodName(QinIrMethodDeclaration method) {
        return "__qin_subhuti_raw_" + method.name();
    }

    private void emitSubhutiRuleWrapperBody(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration declaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrMethodDeclaration method) {
        MethodTypeDesc executeRuleWrapperDescriptor = MethodTypeDesc.of(OBJECT_DESC, OBJECT_ARRAY_DESC);
        QinIrMethodDeclaration executeRuleWrapperMethod = findInheritedDeclarationMethod(
                declaration,
                declarationIndex,
                "executeRuleWrapper",
                1,
                new LinkedHashSet<>());
        if (executeRuleWrapperMethod != null) {
            executeRuleWrapperDescriptor = MethodTypeDesc.of(
                    toClassDesc(executeRuleWrapperMethod.returnType()),
                    OBJECT_ARRAY_DESC);
        }

        code.aload(0);
        code.loadConstant(4);
        code.anewarray(OBJECT_DESC);

        code.dup();
        code.loadConstant(0);
        code.aload(0);
        code.ldc(subhutiRawMethodName(method));
        emitObjectArrayFromParameters(code, method);
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_direct_method_function__",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC, OBJECT_DESC));
        code.aastore();

        code.dup();
        code.loadConstant(1);
        code.ldc(method.name());
        code.aastore();

        code.dup();
        code.loadConstant(2);
        code.ldc(declaration.simpleName());
        code.aastore();

        code.dup();
        code.loadConstant(3);
        emitObjectArrayFromParameters(code, method);
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_subhuti_rule_cache_key",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC));
        code.aastore();

        code.invokevirtual(
                ClassDesc.of(declaration.binaryName()),
                "executeRuleWrapper",
                executeRuleWrapperDescriptor);
        coerceObjectResultForType(code, method.returnType());
        emitObjectCoercedReturn(code, method.returnType());
    }

    private boolean hasVarargsParameter(QinIrMethodDeclaration method) {
        if (method == null || method.parameters().isEmpty()) {
            return false;
        }
        return method.parameters().get(method.parameters().size() - 1).varargs();
    }

    private boolean hasDeclarationMethod(
            QinIrClassDeclaration declaration,
            String methodName,
            int parameterCount) {
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (method.name().equals(methodName)
                    && method.parameters().size() == parameterCount
                    && !isConstructorMethod(method)) {
                return true;
            }
        }
        return false;
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
        if (field.staticField()) {
            code.getstatic(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
        } else {
            code.aload(0);
            code.getfield(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
        }
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
        if (!field.staticField()) {
            code.aload(0);
        }
        switch (field.type().kind()) {
            case BOOLEAN, INT -> code.iload(field.staticField() ? 0 : 1);
            case DOUBLE -> code.dload(field.staticField() ? 0 : 1);
            case STRING, CLASS -> code.aload(field.staticField() ? 0 : 1);
            case VOID -> throw new IllegalArgumentException("Field type cannot be void: " + field.name());
        }
        if (field.staticField()) {
            code.putstatic(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
        } else {
            code.putfield(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
        }
        code.return_();
    }

    private void emitFieldInitializer(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            String ownerBinaryName,
            QinIrFieldDeclaration field,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame) {
        QinIrExpression initializer = field.initializer();
        if (initializer == null) {
            return;
        }

        if (!field.staticField()) {
            code.aload(0);
        }
        emitValueForFieldType(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                field.type(),
                initializer,
                field.name());
        if (field.staticField()) {
            code.putstatic(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
        } else {
            code.putfield(ClassDesc.of(ownerBinaryName), field.name(), toClassDesc(field.type()));
        }
    }

    private void emitValueForFieldType(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrTypeRef fieldType,
            QinIrExpression value,
            String fieldName) {
        switch (fieldType.kind()) {
            case STRING, CLASS -> emitReferenceInitializer(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    fieldType,
                    value,
                    fieldName);
            case BOOLEAN -> emitBooleanInitializer(code, value, fieldName);
            case INT -> emitIntInitializer(code, value, fieldName);
            case DOUBLE -> emitDoubleInitializer(code, value, fieldName);
            case VOID -> throw new IllegalArgumentException("Field type cannot be void: " + fieldName);
        }
    }

    private void emitReferenceInitializer(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrTypeRef fieldType,
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
        if (value instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            code.ldc(ClassDesc.of(resolveJavaClassLiteralBinaryName(
                    ownerDeclaration,
                    declarationIndex,
                    classLiteralExpression)));
            return;
        }
        if (value instanceof QinIrJavaNewExpression javaNewExpression
                && javaNewExpression.arguments().isEmpty()) {
            ClassDesc ownerDesc = ClassDesc.of(canonicalJavaSdkAliasBinaryName(javaNewExpression.ownerBinaryName()));
            code.new_(ownerDesc);
            code.dup();
            code.invokespecial(ownerDesc, "<init>", VOID_INIT);
            return;
        }
        if (value instanceof QinIrNumberLiteral numberLiteral) {
            double numericValue = numberLiteral.value();
            if ("java.lang.Double".equals(fieldType.binaryName())
                    || "java.lang.Number".equals(fieldType.binaryName())) {
                code.loadConstant(numericValue);
                code.invokestatic(
                        ClassDesc.of("java.lang.Double"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;"));
                return;
            }
            if ("java.lang.Long".equals(fieldType.binaryName())) {
                code.loadConstant((long) numericValue);
                code.invokestatic(
                        ClassDesc.of("java.lang.Long"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(J)Ljava/lang/Long;"));
                return;
            }
            if (numericValue == Math.rint(numericValue)
                    && numericValue >= Integer.MIN_VALUE
                    && numericValue <= Integer.MAX_VALUE) {
                code.loadConstant((int) numericValue);
                code.invokestatic(
                        ClassDesc.of("java.lang.Integer"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;"));
                return;
            }
            code.loadConstant(numericValue);
            code.invokestatic(
                    ClassDesc.of("java.lang.Double"),
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;"));
            return;
        }
        if (value instanceof QinIrBooleanLiteral booleanLiteral) {
            if (booleanLiteral.value()) {
                code.iconst_1();
            } else {
                code.iconst_0();
            }
            code.invokestatic(
                    BOOLEAN_DESC,
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/Boolean;"));
            return;
        }
        if (ownerDeclaration != null) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex == null ? Map.of() : declarationIndex,
                    localFrame == null ? LocalFrame.forMethodParameters(method) : localFrame,
                    value);
            coerceValueForTargetType(code, actualType, fieldType);
            return;
        }
        throw new IllegalArgumentException(
                "Unsupported reference field initializer for `" + fieldName + "`: " + value.getClass().getSimpleName());
    }

    private void emitBooleanInitializer(
            java.lang.classfile.CodeBuilder code,
            QinIrExpression value,
            String fieldName) {
        if (value instanceof QinIrNullLiteral) {
            code.iconst_0();
            return;
        }
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
        if (value instanceof QinIrNullLiteral) {
            code.iconst_0();
            return;
        }
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
        if (value instanceof QinIrNullLiteral) {
            code.dconst_0();
            return;
        }
        if (value instanceof QinIrNumberLiteral numberLiteral) {
            code.loadConstant(numberLiteral.value());
            return;
        }
        throw new IllegalArgumentException(
                "Unsupported double field initializer for `" + fieldName + "`: " + value.getClass().getSimpleName());
    }

    private boolean isPrimitiveFieldType(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.BOOLEAN
                || type.kind() == QinIrTypeKind.INT
                || type.kind() == QinIrTypeKind.DOUBLE;
    }

    private void emitPrimitiveDefaultValue(java.lang.classfile.CodeBuilder code, QinIrTypeRef type) {
        switch (type.kind()) {
            case BOOLEAN, INT -> code.iconst_0();
            case DOUBLE -> code.dconst_0();
            default -> throw new IllegalArgumentException("Expected primitive field type, got: " + type);
        }
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

    private void emitExplicitConstructorBody(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            String binaryClassName,
            QinIrMethodDeclaration constructor,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        LocalFrame localFrame = LocalFrame.forMethodParameters(constructor);
        code.aload(0);
        emitSuperConstructorArguments(code, ownerDeclaration, constructor, declarationIndex, localFrame);
        for (QinIrFieldDeclaration field : instanceFields(ownerDeclaration.fields())) {
            emitFieldInitializer(
                    code,
                    ownerDeclaration,
                    binaryClassName,
                    field,
                    constructor,
                    declarationIndex,
                    localFrame);
        }
        emitStatements(
                code,
                ownerDeclaration,
                constructor,
                declarationIndex,
                localFrame,
                constructor.bodyStatements());
        code.return_();
    }

    private void emitSuperConstructorArguments(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration constructor,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame) {
        List<QinIrExpression> superArguments = constructor.superArguments();
        if ((superArguments == null || superArguments.isEmpty())
                && !constructor.explicitSuperConstructorCall()
                && isGeneratedConstructorHelper(constructor)) {
            superArguments = inferGeneratedConstructorHelperSuperArguments(
                    ownerDeclaration,
                    constructor,
                    declarationIndex);
        }
        List<QinIrTypeRef> actualTypes = new ArrayList<>();
        for (QinIrExpression argument : superArguments) {
            actualTypes.add(inferDeclarationExpressionType(
                    ownerDeclaration,
                    constructor,
                    declarationIndex,
                    localFrame,
                    argument));
        }
        ResolvedConstructorCall resolvedConstructor = resolveConstructorCall(
                effectiveSuperclassBinaryName(ownerDeclaration.superType()),
                declarationIndex,
                actualTypes,
                true);
        MethodTypeDesc descriptor = resolvedConstructor.descriptor();
        emitConstructorArguments(
                code,
                ownerDeclaration,
                constructor,
                declarationIndex,
                localFrame,
                superArguments,
                resolvedConstructor);
        code.invokespecial(resolveSuperclass(ownerDeclaration.superType()), "<init>", descriptor);
    }

    private List<QinIrExpression> inferGeneratedConstructorHelperSuperArguments(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration constructor,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (ownerDeclaration == null
                || ownerDeclaration.superType() == null
                || ownerDeclaration.superType().binaryName() == null
                || ownerDeclaration.superType().binaryName().isBlank()
                || "java.lang.Object".equals(ownerDeclaration.superType().binaryName())
                || constructor == null
                || constructor.parameters().isEmpty()) {
            return List.of();
        }
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (QinIrParameter parameter : constructor.parameters()) {
            parameterTypes.add(parameter.type());
        }
        for (int count = parameterTypes.size(); count >= 1; count--) {
            List<QinIrTypeRef> candidateTypes = List.copyOf(parameterTypes.subList(0, count));
            try {
                resolveConstructorCall(
                        ownerDeclaration.superType().binaryName(),
                        declarationIndex,
                        candidateTypes,
                        true);
                List<QinIrExpression> arguments = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    arguments.add(new QinIrIdentifierReference(constructor.parameters().get(i).name()));
                }
                return List.copyOf(arguments);
            } catch (RuntimeException ignored) {
                // Generated TypeScript constructor helpers commonly pass a
                // leading argument prefix to super(...), then consume the full
                // parameter list in the helper body. Try shorter prefixes until
                // the superclass constructor contract is proven.
            }
        }
        return List.of();
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
            emitReturnStatement(code, ownerDeclaration, method, declarationIndex, localFrame, returnStatement);
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
            if (ifStatement.test() instanceof QinIrBooleanLiteral booleanLiteral) {
                emitStatements(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame.child(),
                        booleanLiteral.value() ? ifStatement.consequent() : ifStatement.alternate());
                return;
            }
            java.lang.classfile.Label alternateLabel = code.newLabel();
            java.lang.classfile.Label doneLabel = code.newLabel();
            emitConditionExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    ifStatement.test(),
                    "Declaration if statement test");
            code.ifeq(alternateLabel);
            emitStatements(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame.child(),
                    ifStatement.consequent());
            code.goto_(doneLabel);
            code.labelBinding(alternateLabel);
            emitStatements(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame.child(),
                    ifStatement.alternate());
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
            wrapNonThrowableForThrow(code, actualType, declarationIndex);
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
        emitConditionExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                loopFrame,
                whileStatement.test(),
                "Declaration while statement test");
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
        emitConditionExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                loopFrame,
                doWhileStatement.test(),
                "Declaration do-while statement test");
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
            QinIrTypeRef optimizedSwitchCompareType = emitOptimizedGlobalBinaryBuiltinCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    switchFrame,
                    new QinIrBuiltinCallExpression(
                            "Global",
                            "__qin_binary__",
                            List.of(
                                    new QinIrStringLiteral("==="),
                                    new QinIrIdentifierReference(discriminantBinding.name()),
                                    switchCase.test())));
            if (optimizedSwitchCompareType != null
                    && optimizedSwitchCompareType.kind() == QinIrTypeKind.BOOLEAN) {
                code.ifne(caseLabel);
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
            QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_binary__");
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
                    caseFrame.child(),
                    switchStatement.cases().get(i).consequent());
            if (!switchStatement.cases().get(i).fallthroughAllowed()) {
                code.goto_(doneLabel);
            }
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
            QinIrTypeRef localType = localDeclarationStorageType(
                    initializerDeclaration.declaredType(),
                    initializerType);
            coerceValueForTargetType(code, initializerType, localType);
            LocalBinding binding = forFrame.declare(initializerDeclaration.name(), localType);
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
            emitConditionExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    loopFrame,
                    forStatement.test(),
                    "Declaration for statement test");
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
        QinIrTypeRef itemType = forEachItemType(iterableType, forEachStatement.itemType());
        if (iterableType.kind() == QinIrTypeKind.STRING) {
            emitStringForEachStatement(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    forFrame,
                    forEachStatement,
                    itemType);
            return;
        }
        if (isObjectArrayType(iterableType)) {
            emitObjectArrayForEachStatement(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    forFrame,
                    forEachStatement,
                    itemType);
            return;
        }
        if (!isIterableType(iterableType)) {
            QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                    "QinJvmDeclarationClassEmitter"
                            + " owner=" + ownerDeclaration.binaryName()
                            + " method=" + method.name()
                            + " iterableShape=" + forEachStatement.iterable().getClass().getSimpleName()
                            + " iterableType=" + iterableType,
                    "__qin_for_of_iterable__");
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_for_of_iterable__",
                    MethodTypeDesc.of(ITERABLE_DESC, OBJECT_DESC));
        }
        code.invokeinterface(ITERABLE_DESC, "iterator", MethodTypeDesc.of(ITERATOR_DESC));
        storeLocalForType(code, iteratorBinding.type(), iteratorBinding.localSlot(), iteratorBinding.name());

        java.lang.classfile.Label testLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        LocalFrame bodyFrame = forFrame.withLoop(new LoopBinding(testLabel, doneLabel));
        LocalBinding itemBinding = bodyFrame.declare(forEachStatement.itemName(), itemType);
        code.labelBinding(testLabel);
        loadLocalForType(code, iteratorBinding.type(), iteratorBinding.localSlot(), iteratorBinding.name());
        code.invokeinterface(ITERATOR_DESC, "hasNext", MethodTypeDesc.ofDescriptor("()Z"));
        code.ifeq(doneLabel);
        loadLocalForType(code, iteratorBinding.type(), iteratorBinding.localSlot(), iteratorBinding.name());
        code.invokeinterface(ITERATOR_DESC, "next", MethodTypeDesc.of(OBJECT_DESC));
        coerceObjectResultForType(code, itemType);
        storeLocalForType(code, itemBinding.type(), itemBinding.localSlot(), itemBinding.name());
        emitStatements(code, ownerDeclaration, method, declarationIndex, bodyFrame, forEachStatement.body());
        code.goto_(testLabel);
        code.labelBinding(doneLabel);
    }

    private void emitObjectArrayForEachStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame forFrame,
            QinIrForEachStatement forEachStatement,
            QinIrTypeRef itemType) {
        LocalBinding arrayBinding = forFrame.declare(
                forFrame.syntheticLocalName("__qin_array_iter_" + forEachStatement.itemName()),
                QinIrTypeRef.classType("java.lang.Object[]"));
        storeLocalForType(code, arrayBinding.type(), arrayBinding.localSlot(), arrayBinding.name());

        LocalBinding indexBinding = forFrame.declare(
                forFrame.syntheticLocalName("__qin_array_index_" + forEachStatement.itemName()),
                QinIrTypeRef.intType());
        code.iconst_0();
        storeLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());

        java.lang.classfile.Label testLabel = code.newLabel();
        java.lang.classfile.Label incrementLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        LocalFrame bodyFrame = forFrame.withLoop(new LoopBinding(incrementLabel, doneLabel));
        LocalBinding itemBinding = bodyFrame.declare(forEachStatement.itemName(), itemType);

        code.labelBinding(testLabel);
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        loadLocalForType(code, arrayBinding.type(), arrayBinding.localSlot(), arrayBinding.name());
        code.arraylength();
        code.if_icmpge(doneLabel);

        loadLocalForType(code, arrayBinding.type(), arrayBinding.localSlot(), arrayBinding.name());
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        code.aaload();
        coerceObjectResultForType(code, itemType);
        storeLocalForType(code, itemBinding.type(), itemBinding.localSlot(), itemBinding.name());

        emitStatements(code, ownerDeclaration, method, declarationIndex, bodyFrame, forEachStatement.body());
        code.labelBinding(incrementLabel);
        code.iinc(indexBinding.localSlot(), 1);
        code.goto_(testLabel);
        code.labelBinding(doneLabel);
    }

    private void emitStringForEachStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame forFrame,
            QinIrForEachStatement forEachStatement,
            QinIrTypeRef itemType) {
        LocalBinding stringBinding = forFrame.declare(
                forFrame.syntheticLocalName("__qin_string_iter_" + forEachStatement.itemName()),
                QinIrTypeRef.stringType());
        storeLocalForType(code, stringBinding.type(), stringBinding.localSlot(), stringBinding.name());

        LocalBinding indexBinding = forFrame.declare(
                forFrame.syntheticLocalName("__qin_string_index_" + forEachStatement.itemName()),
                QinIrTypeRef.intType());
        code.iconst_0();
        storeLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());

        java.lang.classfile.Label testLabel = code.newLabel();
        java.lang.classfile.Label incrementLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        LocalFrame bodyFrame = forFrame.withLoop(new LoopBinding(incrementLabel, doneLabel));
        LocalBinding itemBinding = bodyFrame.declare(forEachStatement.itemName(), itemType);

        code.labelBinding(testLabel);
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        loadLocalForType(code, stringBinding.type(), stringBinding.localSlot(), stringBinding.name());
        code.invokevirtual(STRING_DESC, "length", MethodTypeDesc.ofDescriptor("()I"));
        code.if_icmpge(doneLabel);

        loadLocalForType(code, stringBinding.type(), stringBinding.localSlot(), stringBinding.name());
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        code.invokevirtual(STRING_DESC, "charAt", MethodTypeDesc.ofDescriptor("(I)C"));
        code.invokestatic(STRING_DESC, "valueOf", MethodTypeDesc.ofDescriptor("(C)Ljava/lang/String;"));
        storeLocalForType(code, itemBinding.type(), itemBinding.localSlot(), itemBinding.name());

        emitStatements(code, ownerDeclaration, method, declarationIndex, bodyFrame, forEachStatement.body());
        code.labelBinding(incrementLabel);
        code.iinc(indexBinding.localSlot(), 1);
        code.goto_(testLabel);
        code.labelBinding(doneLabel);
    }

    private QinIrTypeRef forEachItemType(QinIrTypeRef iterableType, QinIrTypeRef declaredItemType) {
        if (iterableType == null) {
            return boxForObjectStorage(declaredItemType == null ? QinIrTypeRef.classType("java.lang.Object") : declaredItemType);
        }
        if (iterableType.kind() == QinIrTypeKind.STRING) {
            return QinIrTypeRef.stringType();
        }
        if (isObjectArrayType(iterableType)) {
            return boxForObjectStorage(staticArrayElementType(iterableType));
        }
        if (isIterableType(iterableType)) {
            if (!iterableType.typeArguments().isEmpty()) {
                return boxForObjectStorage(iterableType.typeArguments().get(0));
            }
        }
        return boxForObjectStorage(declaredItemType == null ? QinIrTypeRef.classType("java.lang.Object") : declaredItemType);
    }

    private QinIrTypeRef boxForObjectStorage(QinIrTypeRef type) {
        if (type == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return switch (type.kind()) {
            case BOOLEAN -> QinIrTypeRef.classType("java.lang.Boolean");
            case INT -> QinIrTypeRef.classType("java.lang.Integer");
            case DOUBLE -> QinIrTypeRef.classType("java.lang.Double");
            default -> type;
        };
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
        if (localDeclaration.declaredType() != null
                && emitJavaLangStringCharAtForNumericTarget(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        localDeclaration.initializer(),
                        localDeclaration.declaredType())) {
            LocalBinding binding = localFrame.declare(localDeclaration.name(), localDeclaration.declaredType());
            storeLocalForType(code, binding.type(), binding.localSlot(), binding.name());
            return;
        }
        QinIrTypeRef initializerType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                localDeclaration.initializer());
        QinIrTypeRef localType = localDeclarationStorageType(localDeclaration.declaredType(), initializerType);
        try {
            coerceValueForTargetType(code, initializerType, localType);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Failed to coerce local declaration '" + localDeclaration.name()
                            + "' in " + ownerDeclaration.binaryName() + "." + method.name()
                            + ": " + initializerType.kind() + " -> " + localType.kind()
                            + "; initializer=" + localDeclaration.initializer(),
                    ex);
        }
        LocalBinding binding = localFrame.declare(localDeclaration.name(), localType);
        storeLocalForType(code, binding.type(), binding.localSlot(), binding.name());
    }

    private QinIrTypeRef localDeclarationStorageType(QinIrTypeRef declaredType, QinIrTypeRef initializerType) {
        if (declaredType == null) {
            return initializerType;
        }
        if (isPrimitiveStorageType(declaredType) && isBoxedPrimitiveStorageType(initializerType)) {
            return initializerType;
        }
        if (isJavaLangObjectType(declaredType) && isStaticInitializerStorageType(initializerType)) {
            return initializerType;
        }
        return declaredType;
    }

    private boolean isPrimitiveStorageType(QinIrTypeRef type) {
        return type != null
                && (type.kind() == QinIrTypeKind.BOOLEAN
                || type.kind() == QinIrTypeKind.INT
                || type.kind() == QinIrTypeKind.DOUBLE);
    }

    private boolean isBoxedPrimitiveStorageType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && ("java.lang.Boolean".equals(type.binaryName())
                || "java.lang.Integer".equals(type.binaryName())
                || "java.lang.Long".equals(type.binaryName())
                || "java.lang.Double".equals(type.binaryName())
                || "java.lang.Number".equals(type.binaryName()));
    }

    private boolean isStaticInitializerStorageType(QinIrTypeRef type) {
        return isStaticStructuralInitializerType(type)
                || isJavaSdkReferenceInitializerType(type)
                || isStaticPreciseInitializerType(type);
    }

    private boolean isStaticPreciseInitializerType(QinIrTypeRef type) {
        return type != null
                && !(type.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object".equals(type.binaryName()));
    }

    private boolean isStaticStructuralInitializerType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        String binaryName = canonicalJavaSdkAliasBinaryName(type.binaryName());
        return "java.util.Map".equals(binaryName)
                || "java.util.LinkedHashMap".equals(binaryName)
                || "java.util.ArrayList".equals(binaryName)
                || "java.lang.Object[]".equals(binaryName);
    }

    private boolean isJavaSdkReferenceInitializerType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        String binaryName = canonicalJavaSdkAliasBinaryName(type.binaryName());
        return binaryName != null
                && binaryName.startsWith("java.")
                && !"java.lang.Object".equals(binaryName);
    }

    private boolean isJavaLangObjectType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object".equals(type.binaryName());
    }

    private void emitTryStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrTryStatement tryStatement) {
        if (tryStatement.catchClauses().size() > 1) {
            throw new IllegalArgumentException("Declaration try statement supports one catch clause");
        }

        java.lang.classfile.Label tryStart = code.newLabel();
        java.lang.classfile.Label tryEnd = code.newLabel();
        java.lang.classfile.Label done = code.newLabel();
        LocalFrame tryFrame = tryStatement.finallyBody().isEmpty()
                ? localFrame
                : localFrame.withFinally(tryStatement.finallyBody());
        code.labelBinding(tryStart);
        emitStatements(code, ownerDeclaration, method, declarationIndex, tryFrame, tryStatement.tryBody());
        code.labelBinding(tryEnd);
        emitFinallyBody(code, ownerDeclaration, method, declarationIndex, localFrame, tryStatement.finallyBody());
        code.goto_(done);

        if (!tryStatement.catchClauses().isEmpty()) {
            QinIrCatchClause catchClause = tryStatement.catchClauses().get(0);
            java.lang.classfile.Label handler = code.newLabel();
            code.labelBinding(handler);
            LocalFrame catchFrame = tryStatement.finallyBody().isEmpty()
                    ? localFrame.child()
                    : localFrame.withFinally(tryStatement.finallyBody()).child();
            LocalBinding catchBinding = catchFrame.declare(
                    catchClause.parameterName(),
                    catchClause.parameterType());
            storeLocalForType(code, catchBinding.type(), catchBinding.localSlot(), catchBinding.name());
            emitStatements(code, ownerDeclaration, method, declarationIndex, catchFrame, catchClause.body());
            emitFinallyBody(code, ownerDeclaration, method, declarationIndex, localFrame, tryStatement.finallyBody());
            code.goto_(done);
            code.exceptionCatch(tryStart, tryEnd, handler, toCatchClassDesc(catchClause.parameterType()));
        }
        if (!tryStatement.finallyBody().isEmpty()) {
            java.lang.classfile.Label finallyHandler = code.newLabel();
            code.labelBinding(finallyHandler);
            LocalFrame finallyHandlerFrame = localFrame.child();
            LocalBinding throwableBinding = finallyHandlerFrame.declare(
                    localFrame.syntheticLocalName("__qin_finally_throwable"),
                    QinIrTypeRef.classType("java.lang.Throwable"));
            storeLocalForType(code, throwableBinding.type(), throwableBinding.localSlot(), throwableBinding.name());
            emitFinallyBody(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    finallyHandlerFrame,
                    tryStatement.finallyBody());
            loadLocalForType(code, throwableBinding.type(), throwableBinding.localSlot(), throwableBinding.name());
            code.athrow();
            code.exceptionCatch(tryStart, tryEnd, finallyHandler, ClassDesc.of("java.lang.Throwable"));
        }
        code.labelBinding(done);
    }

    private void emitReturnStatement(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrReturnStatement returnStatement) {
        QinIrExpression value = returnStatement.value();
        if (localFrame.finallyBlocks().isEmpty()) {
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

        if (value == null || value instanceof QinIrNullLiteral) {
            emitActiveFinallyBlocks(code, ownerDeclaration, method, declarationIndex, localFrame);
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
        if (method.returnType().kind() == QinIrTypeKind.VOID) {
            discardExpressionResult(code, actualType);
            emitActiveFinallyBlocks(code, ownerDeclaration, method, declarationIndex, localFrame);
            code.return_();
            return;
        }
        if (actualType.kind() == QinIrTypeKind.VOID) {
            emitDefaultArgumentValue(code, method.returnType());
        } else {
            coerceValueForTargetType(code, actualType, method.returnType());
        }
        LocalBinding returnBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_finally_return"),
                method.returnType());
        storeLocalForType(code, returnBinding.type(), returnBinding.localSlot(), returnBinding.name());
        emitActiveFinallyBlocks(code, ownerDeclaration, method, declarationIndex, localFrame);
        loadLocalForType(code, returnBinding.type(), returnBinding.localSlot(), returnBinding.name());
        emitRawReturnForType(code, method.returnType());
    }

    private void emitActiveFinallyBlocks(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame) {
        List<List<QinIrStatement>> finallyBlocks = localFrame.finallyBlocks();
        for (int i = finallyBlocks.size() - 1; i >= 0; i--) {
            emitFinallyBody(code, ownerDeclaration, method, declarationIndex, localFrame, finallyBlocks.get(i));
        }
    }

    private void emitFinallyBody(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            List<QinIrStatement> finallyBody) {
        if (!finallyBody.isEmpty()) {
            emitStatements(code, ownerDeclaration, method, declarationIndex, localFrame.child(), finallyBody);
        }
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
        String functionDefinitionHelper = activeFunctionDefinitionHelpers.get(method.runtimeFunctionDefinition());
        if (functionDefinitionHelper != null) {
            code.invokestatic(
                    ClassDesc.of(activeBinaryClassName),
                    functionDefinitionHelper,
                    MethodTypeDesc.of(OBJECT_DESC));
        } else {
            emitObjectLiteral(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    LocalFrame.forMethodParameters(method),
                    method.runtimeFunctionDefinition());
        }
        code.aload(0);
        emitObjectArrayFromParameters(code, method);
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                "QinJvmDeclarationClassEmitter",
                "__qin_call_function_definition__"
                        + " owner=" + ownerDeclaration.binaryName()
                        + " method=" + method.name()
                        + " closure=" + functionClosureKeys(method.runtimeFunctionDefinition()));
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
        int localSlot = parameterSlotStart(method);
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

    private void emitVarargsArrayFromExpressions(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            List<QinIrExpression> expressions,
            int startIndex,
            int count,
            QinIrTypeRef arrayType,
            Class<?> reflectedArrayType) {
        Class<?> reflectedComponentType = reflectedArrayType != null && reflectedArrayType.isArray()
                ? reflectedArrayType.getComponentType()
                : null;
        QinIrTypeRef componentType = reflectedComponentType == null
                ? varargsElementType(arrayType)
                : toQinTypeRef(reflectedComponentType);
        TypeKind primitiveArrayKind = reflectedComponentType != null && reflectedComponentType.isPrimitive()
                ? primitiveTypeKind(reflectedComponentType)
                : primitiveArrayKind(componentType);

        code.loadConstant(count);
        if (primitiveArrayKind == null) {
            code.anewarray(toReferenceClassDesc(componentType.binaryName()));
        } else {
            code.newarray(primitiveArrayKind);
        }
        for (int i = 0; i < count; i++) {
            code.dup();
            code.loadConstant(i);
            if (emitNullLiteralForReferenceParameter(
                    code,
                    expressions.get(startIndex + i),
                    componentType,
                    reflectedComponentType)) {
                code.aastore();
                continue;
            }
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expressions.get(startIndex + i));
            if (reflectedComponentType != null) {
                coerceValueForJavaParameterType(code, actualType, reflectedComponentType);
            } else {
                coerceValueForTargetType(code, actualType, componentType);
            }
            if (primitiveArrayKind == null) {
                code.aastore();
            } else {
                code.arrayStore(primitiveArrayKind);
            }
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
        if (expression instanceof QinIrCastExpression castExpression) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    castExpression.expression());
            QinIrTypeRef targetType = castTypeRef(castExpression.typeName());
            coerceValueForTargetType(code, actualType, targetType);
            return targetType;
        }
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
        if (expression instanceof QinIrBoundMethodReferenceExpression boundMethodReferenceExpression) {
            return emitBoundMethodReferenceExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    boundMethodReferenceExpression);
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            return emitSequenceExpression(code, ownerDeclaration, method, declarationIndex, localFrame, sequenceExpression);
        }
        if (expression instanceof QinIrShortCircuitExpression shortCircuitExpression) {
            return emitShortCircuitExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    shortCircuitExpression);
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            return emitIdentifierReference(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    identifierReference);
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
        if (expression instanceof QinIrElementAccessExpression elementAccessExpression) {
            return emitElementAccess(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression);
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
        if (expression instanceof QinIrSuperMethodCallExpression superMethodCallExpression) {
            return emitSuperMethodCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    superMethodCallExpression);
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            return emitJavaNewExpression(code, ownerDeclaration, method, declarationIndex, localFrame, javaNewExpression);
        }
        if (expression instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            code.ldc(ClassDesc.of(resolveJavaClassLiteralBinaryName(
                    ownerDeclaration,
                    declarationIndex,
                    classLiteralExpression)));
            return QinIrTypeRef.classType("java.lang.Class");
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            emitObjectLiteral(code, ownerDeclaration, method, declarationIndex, localFrame, objectLiteral);
            return QinIrTypeRef.classType("java.util.Map");
        }
        if (expression instanceof QinIrArrayCreationExpression arrayCreationExpression) {
            return emitArrayCreationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arrayCreationExpression);
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            emitArrayLiteral(code, ownerDeclaration, method, declarationIndex, localFrame, arrayLiteral);
            return QinIrTypeRef.classType("java.util.ArrayList");
        }
        if (expression instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            return emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    spreadArgumentExpression.expression());
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
        if (expression instanceof QinIrUpdateExpression updateExpression) {
            return emitUpdateExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    updateExpression);
        }
        throw new IllegalArgumentException(
                "Unsupported declaration method return expression: " + expression.getClass().getSimpleName());
    }

    private QinIrTypeRef emitSuperMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrSuperMethodCallExpression superMethodCallExpression) {
        if (ownerDeclaration.superType() == null || ownerDeclaration.superType().binaryName() == null) {
            throw new IllegalArgumentException(
                    "Cannot emit super method call without superclass: "
                            + ownerDeclaration.binaryName() + "." + superMethodCallExpression.methodName());
        }
        ResolvedInstanceMethodCall resolvedMethod = resolveInstanceMethodCall(
                ownerDeclaration.superType(),
                superMethodCallExpression.methodName(),
                superMethodCallExpression.arguments().size(),
                declarationIndex);
        boolean omitSpreadArgumentsForZeroArityMethod = false;
        if (resolvedMethod == null
                && superMethodCallExpression.arguments().size() == 1
                && superMethodCallExpression.arguments().get(0) instanceof QinIrSpreadArgumentExpression) {
            resolvedMethod = resolveInstanceMethodCall(
                    ownerDeclaration.superType(),
                    superMethodCallExpression.methodName(),
                    0,
                    declarationIndex);
            omitSpreadArgumentsForZeroArityMethod = resolvedMethod != null
                    && resolvedMethod.parameterTypes().isEmpty();
        }
        if (resolvedMethod == null) {
            throw new IllegalArgumentException(
                    "Unknown declaration super method: "
                            + ownerDeclaration.superType().binaryName()
                            + "." + superMethodCallExpression.methodName()
                            + "/" + superMethodCallExpression.arguments().size()
                            + "; owner=" + ownerDeclaration.binaryName()
                            + "; method=" + method.name());
        }
        code.aload(0);
        QinIrInstanceMethodCallExpression argumentCarrier = new QinIrInstanceMethodCallExpression(
                new QinIrThisExpression(),
                resolvedMethod.ownerBinaryName(),
                resolvedMethod.methodName(),
                omitSpreadArgumentsForZeroArityMethod
                        ? List.of()
                        : superMethodCallExpression.arguments());
        emitInstanceMethodArguments(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                argumentCarrier,
                resolvedMethod);
        code.invokespecial(
                ClassDesc.of(resolvedMethod.ownerBinaryName()),
                resolvedMethod.methodName(),
                methodDescriptor(resolvedMethod));
        boxReflectedPrimitiveReturnIfNeeded(code, resolvedMethod.returnType(), methodDescriptor(resolvedMethod));
        return resolvedMethod.returnType();
    }

    private QinIrTypeRef emitBoundMethodReferenceExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBoundMethodReferenceExpression boundMethodReferenceExpression) {
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                boundMethodReferenceExpression.receiver());
        boxValueForObjectTarget(code, receiverType);
        code.ldc(boundMethodReferenceExpression.methodName());
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_bound_method__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef emitIdentifierReference(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrIdentifierReference identifierReference) {
        if ("Infinity".equals(identifierReference.name())) {
            code.loadConstant(Double.POSITIVE_INFINITY);
            return QinIrTypeRef.doubleType();
        }
        if ("undefined".equals(identifierReference.name())) {
            code.aconst_null();
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if ("arguments".equals(identifierReference.name())) {
            emitCurrentMethodArgumentsArray(code, method);
            return QinIrTypeRef.classType("java.lang.Object[]");
        }
        QinIrExpression moduleConstantInitializer = activeModuleConstantInitializers.get(identifierReference.name());
        if (moduleConstantInitializer != null) {
            return emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    moduleConstantInitializer);
        }
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

        ResolvedFieldAccess fieldAccess = resolveFieldAccess(ownerDeclaration, identifierReference.name(), declarationIndex);
        if (fieldAccess != null) {
            emitResolvedFieldGet(code, fieldAccess);
            return fieldAccess.field().type();
        }

        ResolvedFieldAccess enclosingStaticFieldAccess = resolveEnclosingStaticFieldAccess(
                ownerDeclaration,
                identifierReference.name(),
                declarationIndex);
        if (enclosingStaticFieldAccess != null) {
            emitResolvedFieldGet(code, enclosingStaticFieldAccess);
            return enclosingStaticFieldAccess.field().type();
        }

        QinIrClassDeclaration declaredClassReference = resolveDeclaredClassReference(
                ownerDeclaration,
                declarationIndex,
                identifierReference.name());
        if (declaredClassReference != null) {
            code.ldc(ClassDesc.of(declaredClassReference.binaryName()));
            return QinIrTypeRef.classType("java.lang.Class");
        }

        if (isQinRuntimeGlobalName(identifierReference.name())) {
            code.ldc(identifierReference.name());
            QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                    "QinJvmDeclarationClassEmitter"
                            + " owner=" + ownerDeclaration.binaryName()
                            + " method=" + method.name()
                            + " identifier=" + identifierReference.name(),
                    "__qin_global__");
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_global__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"));
            return QinIrTypeRef.classType("java.lang.Object");
        }

        throw new IllegalArgumentException(
                "Unknown declaration identifier: "
                        + identifierReference.name()
                        + "; owner=" + ownerDeclaration.binaryName()
                        + "; method=" + method.name());
    }

    private void wrapNonThrowableForThrow(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef actualType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (isThrowableType(actualType, declarationIndex)) {
            return;
        }
        boxValueForObjectTarget(code, actualType);
        code.invokestatic(
                STRING_DESC,
                "valueOf",
                MethodTypeDesc.of(STRING_DESC, OBJECT_DESC));
        code.new_(RUNTIME_EXCEPTION_DESC);
        code.dup_x1();
        code.swap();
        code.invokespecial(
                RUNTIME_EXCEPTION_DESC,
                "<init>",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)V"));
    }

    private boolean isThrowableType(
            QinIrTypeRef type,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        QinIrClassDeclaration localDeclaration = resolveIndexedDeclaration(declarationIndex, type.binaryName());
        if (localDeclaration != null) {
            return isThrowableType(localDeclaration.superType(), declarationIndex);
        }
        return Throwable.class.isAssignableFrom(resolveClass(type.binaryName()));
    }

    private QinIrTypeRef emitElementAccess(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrElementAccessExpression elementAccessExpression) {
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                elementAccessExpression.receiver());
        if (isStaticCollectionElementAccessType(receiverType)) {
            boxValueForObjectTarget(code, receiverType);
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression.index());
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_collection_get__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
            QinIrTypeRef elementType = staticCollectionElementType(receiverType);
            coerceObjectResultForType(code, elementType);
            return elementType;
        }
        if (isPrimitiveArrayType(receiverType)) {
            QinIrTypeRef elementType = staticArrayElementType(receiverType);
            TypeKind primitiveArrayKind = primitiveArrayKind(elementType);
            if (primitiveArrayKind == null) {
                throw new IllegalArgumentException(
                        "Unsupported primitive array element access type: "
                                + receiverType
                                + "; owner=" + ownerDeclaration.binaryName()
                                + "; method=" + method.name()
                                + "; expression=" + elementAccessExpression);
            }
            LocalBinding arrayBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_element_array"),
                    receiverType);
            storeLocalForType(code, arrayBinding.type(), arrayBinding.localSlot(), arrayBinding.name());
            QinIrTypeRef indexType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression.index());
            coerceValueForTargetType(code, indexType, QinIrTypeRef.intType());
            LocalBinding indexBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_element_index"),
                    QinIrTypeRef.intType());
            storeLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
            loadLocalForType(code, arrayBinding.type(), arrayBinding.localSlot(), arrayBinding.name());
            loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
            code.arrayLoad(primitiveArrayKind);
            return elementType;
        }
        if (!isObjectArrayType(receiverType)) {
            if (receiverType.kind() == QinIrTypeKind.CLASS || receiverType.kind() == QinIrTypeKind.STRING) {
                if (isJavaIoByteArrayOutputStreamStaticIoHelper(ownerDeclaration, method)) {
                    boxValueForObjectTarget(code, receiverType);
                    emitDeclarationExpressionAsObject(
                            code,
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            elementAccessExpression.index());
                    code.invokestatic(
                            ESM_GLOBAL_DESC,
                            "__qin_collection_get__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                throw new IllegalStateException(
                        "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter would emit JavaEsmGlobal.__qin_member_get__ "
                                + "while compiling unresolved element access to JVM .class. Element access requires a "
                                + "statically admitted array, string, collection, or explicit Map/Dict receiver. "
                                + "owner="
                                + ownerDeclaration.binaryName()
                                + " method="
                                + method.name()
                                + " receiverType="
                                + receiverType
                                + " expression="
                                + elementAccessExpression);
            }
            throw new IllegalArgumentException(
                    "Declaration element access currently requires a reference receiver: "
                            + receiverType
                            + "; owner=" + ownerDeclaration.binaryName()
                            + "; method=" + method.name()
                            + "; expression=" + elementAccessExpression);
        }
        LocalBinding arrayBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_element_array"),
                QinIrTypeRef.classType("java.lang.Object[]"));
        storeLocalForType(code, arrayBinding.type(), arrayBinding.localSlot(), arrayBinding.name());
        QinIrTypeRef indexType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                elementAccessExpression.index());
        coerceValueForTargetType(code, indexType, QinIrTypeRef.intType());
        LocalBinding indexBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_element_index"),
                QinIrTypeRef.intType());
        storeLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        java.lang.classfile.Label outOfRangeLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        code.iflt(outOfRangeLabel);
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        loadLocalForType(code, arrayBinding.type(), arrayBinding.localSlot(), arrayBinding.name());
        code.arraylength();
        code.if_icmpge(outOfRangeLabel);
        loadLocalForType(code, arrayBinding.type(), arrayBinding.localSlot(), arrayBinding.name());
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        code.aaload();
        code.goto_(doneLabel);
        code.labelBinding(outOfRangeLabel);
        code.aconst_null();
        code.labelBinding(doneLabel);
        QinIrTypeRef elementType = staticArrayElementType(receiverType);
        coerceObjectResultForType(code, elementType);
        return elementType;
    }

    private boolean isStaticCollectionElementAccessType(QinIrTypeRef type) {
        if (type == null) {
            return false;
        }
        if (type.kind() == QinIrTypeKind.STRING) {
            return true;
        }
        if (type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        try {
            Class<?> ownerClass = resolveClass(type.binaryName());
            return java.util.List.class.isAssignableFrom(ownerClass)
                    || java.util.Map.class.isAssignableFrom(ownerClass)
                    || CharSequence.class.isAssignableFrom(ownerClass);
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private QinIrTypeRef staticCollectionElementType(QinIrTypeRef collectionType) {
        if (collectionType == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (collectionType.kind() == QinIrTypeKind.STRING) {
            return QinIrTypeRef.stringType();
        }
        if (collectionType.kind() == QinIrTypeKind.CLASS
                && collectionType.typeArguments() != null
                && !collectionType.typeArguments().isEmpty()) {
            if (isJavaUtilMapType(collectionType) && collectionType.typeArguments().size() >= 2) {
                return boxForObjectStorage(collectionType.typeArguments().get(1));
            }
            return boxForObjectStorage(collectionType.typeArguments().get(0));
        }
        return QinIrTypeRef.classType("java.lang.Object");
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
        if (isJavaEsmSymbolIteratorAccess(receiverExpression, propertyName)) {
            code.invokestatic(
                    ESM_SYMBOL_DESC,
                    "iterator",
                    MethodTypeDesc.of(OBJECT_DESC));
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (receiverExpression instanceof QinIrThisExpression) {
            ResolvedFieldAccess fieldAccess = resolveFieldAccess(
                    ownerDeclaration,
                    propertyName,
                    declarationIndex);
            if (fieldAccess != null) {
                emitResolvedFieldGet(code, fieldAccess);
                return fieldAccess.field().type();
            }
        }
        if (receiverExpression instanceof QinIrIdentifierReference identifierReference) {
            ResolvedFieldAccess staticFieldAccess = resolveDeclaredStaticFieldAccess(
                    ownerDeclaration,
                    declarationIndex,
                    identifierReference.name(),
                    propertyName);
            if (staticFieldAccess != null) {
                emitResolvedFieldGet(code, staticFieldAccess);
                return staticFieldAccess.field().type();
            }
            ResolvedStaticMethodCall staticGetterAccess = resolveDeclaredStaticGetterAccess(
                    ownerDeclaration,
                    declarationIndex,
                    identifierReference.name(),
                    propertyName);
            if (staticGetterAccess != null) {
                invokeStaticMethod(code, staticGetterAccess);
                return staticGetterAccess.returnType();
            }
        }
        if (receiverExpression instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            ResolvedFieldAccess staticFieldAccess = resolveDeclaredStaticFieldAccess(
                    ownerDeclaration,
                    declarationIndex,
                    classLiteralExpression.binaryName(),
                    propertyName);
            if (staticFieldAccess == null) {
                staticFieldAccess = resolveDeclaredStaticFieldAccess(
                        ownerDeclaration,
                        declarationIndex,
                        classLiteralExpression.typeName(),
                        propertyName);
            }
            if (staticFieldAccess != null) {
                emitResolvedFieldGet(code, staticFieldAccess);
                return staticFieldAccess.field().type();
            }
            ResolvedStaticMethodCall staticGetterAccess = resolveDeclaredStaticGetterAccess(
                    ownerDeclaration,
                    declarationIndex,
                    classLiteralExpression.binaryName(),
                    propertyName);
            if (staticGetterAccess == null) {
                staticGetterAccess = resolveDeclaredStaticGetterAccess(
                        ownerDeclaration,
                        declarationIndex,
                        classLiteralExpression.typeName(),
                        propertyName);
            }
            if (staticGetterAccess != null) {
                invokeStaticMethod(code, staticGetterAccess);
                return staticGetterAccess.returnType();
            }
        }
        ResolvedFieldAccess javaStaticFieldAccess = resolveJavaStaticFieldAccess(
                ownerDeclaration,
                declarationIndex,
                receiverExpression,
                propertyName);
        if (javaStaticFieldAccess != null) {
            emitResolvedFieldGet(code, javaStaticFieldAccess);
            return javaStaticFieldAccess.field().type();
        }
        String staticExportSlotName = staticExportSlotName(receiverExpression);
        if (staticExportSlotName != null) {
            code.ldc(staticExportSlotName);
            code.ldc(propertyName);
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_static_export_member_get__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;"));
            return QinIrTypeRef.classType("java.lang.Object");
        }
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                receiverExpression);
        Integer arrayPropertyIndex = numericArrayPropertyIndex(propertyName);
        if (arrayPropertyIndex != null && isObjectArrayType(receiverType)) {
            code.loadConstant(arrayPropertyIndex);
            code.aaload();
            QinIrTypeRef elementType = staticArrayElementType(receiverType);
            coerceObjectResultForType(code, elementType);
            return elementType;
        }
        ResolvedPropertyAccess propertyAccess = resolvePropertyAccess(receiverType, propertyName, declarationIndex);
        if (propertyAccess == null) {
            if (receiverType.kind() == QinIrTypeKind.CLASS
                    && "java.lang.Class".equals(receiverType.binaryName())) {
                code.ldc(propertyName);
                QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                        "QinJvmDeclarationClassEmitter",
                        "__qin_member_get__");
                code.invokestatic(
                        ESM_GLOBAL_DESC,
                        "__qin_member_get__",
                        MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
                return QinIrTypeRef.classType("java.lang.Object");
            }
            if (hasLocalInstanceMethodNamed(receiverType, propertyName, declarationIndex)) {
                boxValueForObjectTarget(code, receiverType);
                code.ldc(propertyName);
                code.invokestatic(
                        ESM_GLOBAL_DESC,
                        "__qin_bound_method__",
                        MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
                return QinIrTypeRef.classType("java.lang.Object");
            }
            if ("length".equals(propertyName)) {
                if (isJavaIoByteArrayOutputStreamStaticIoHelper(ownerDeclaration, method)) {
                    boxValueForObjectTarget(code, receiverType);
                    code.invokestatic(
                            ESM_GLOBAL_DESC,
                            "__qin_static_length__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)I"));
                    return QinIrTypeRef.intType();
                }
                throw new IllegalStateException(
                        "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter would emit JavaEsmGlobal.__qin_member_get__ "
                                + "while compiling unresolved .length to JVM .class. Length access requires a statically admitted "
                                + "string, array, collection, or Qin-owned fixed field. "
                                + "debugName="
                                + debugName
                                + " owner="
                                + ownerDeclaration.binaryName()
                                + " method="
                                + method.name()
                                + " receiverType="
                                + receiverType
                                + " property="
                                + propertyName);
            }
            if (isJavaUtilMapType(receiverType)) {
                code.checkcast(MAP_DESC);
                code.ldc(propertyName);
                code.invokeinterface(MAP_DESC, "get", MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC));
                QinIrTypeRef structuralPropertyType = resolveStructuralSlimeAstPropertyType(receiverType, propertyName);
                if (structuralPropertyType != null && !isJavaLangObjectType(structuralPropertyType)) {
                    coerceValueForTargetType(code, QinIrTypeRef.classType("java.lang.Object"), structuralPropertyType);
                    return structuralPropertyType;
                }
                return QinIrTypeRef.classType("java.lang.Object");
            }
            if (isDynamicObjectType(receiverType) || isQinRuntimeMetadataProperty(propertyName)) {
                throw new IllegalStateException(
                        "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter would emit JavaEsmGlobal.__qin_member_get__ "
                                + "while compiling to JVM .class. Dynamic member lookup requires a statically admitted receiver. "
                                + "debugName="
                                + debugName
                                + " owner="
                                + ownerDeclaration.binaryName()
                                + " method="
                                + method.name()
                                + " receiverType="
                                + receiverType
                                + " property="
                                + propertyName);
            }
            if (isCurrentOrLocalDeclarationReceiver(receiverType, declarationIndex)) {
                throw new IllegalStateException(
                        "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter would emit JavaEsmGlobal.__qin_member_get__ "
                                + "while compiling local declaration member access to JVM .class. "
                                + "The receiver is a Qin/local declaration, but the requested member was not resolved as a fixed field, method, or accessor. "
                                + "debugName="
                                + debugName
                                + " owner="
                                + ownerDeclaration.binaryName()
                                + " method="
                                + method.name()
                                + " receiverType="
                                + receiverType
                                + " property="
                                + propertyName);
            }
            throw new IllegalArgumentException(
                    "Unknown declaration member access: "
                            + debugName
                            + " owner="
                            + ownerDeclaration.binaryName()
                            + " method="
                            + method.name()
                            + " receiverType="
                            + receiverType
                            + " property="
                            + propertyName);
        }
        invokeAccessor(code, propertyAccess);
        return propertyAccess.propertyType();
    }

    private boolean hasLocalInstanceMethodNamed(
            QinIrTypeRef ownerType,
            String methodName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return hasLocalInstanceMethodNamed(ownerType, methodName, declarationIndex, new java.util.LinkedHashSet<>());
    }

    private boolean hasLocalInstanceMethodNamed(
            QinIrTypeRef ownerType,
            String methodName,
            Map<String, QinIrClassDeclaration> declarationIndex,
            java.util.Set<String> visitedLocalTypes) {
        if (ownerType == null
                || ownerType.kind() != QinIrTypeKind.CLASS
                || methodName == null
                || methodName.isBlank()
                || declarationIndex == null) {
            return false;
        }
        QinIrClassDeclaration localDeclaration = declarationIndex.get(ownerType.binaryName());
        if (localDeclaration == null || !visitedLocalTypes.add(ownerType.binaryName())) {
            return false;
        }
        for (QinIrMethodDeclaration candidate : localDeclaration.methods()) {
            if (!candidate.staticMethod() && sameLocalMethodName(candidate.name(), methodName)) {
                return true;
            }
        }
        return hasLocalInstanceMethodNamed(
                localDeclaration.superType(),
                methodName,
                declarationIndex,
                visitedLocalTypes);
    }

    private QinIrTypeRef emitInstanceMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef javaLangStringHelperType = inferJavaLangStringSdkHelperReturnType(methodCallExpression);
        if (javaLangStringHelperType != null) {
            return emitJavaLangStringSdkHelperCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression,
                    javaLangStringHelperType);
        }
        QinIrTypeRef javaLangNumberHelperType = inferJavaLangNumberSdkHelperReturnType(methodCallExpression);
        if (javaLangNumberHelperType != null) {
            return emitJavaLangNumberSdkHelperCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression,
                    javaLangNumberHelperType);
        }
        if (isQinArrayFromCall(methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_to_object_array__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)[Ljava/lang/Object;"));
            return QinIrTypeRef.classType("java.lang.Object[]");
        }
        Integer staticArrayFromNullLength = staticArrayFromNullFactoryLength(methodCallExpression);
        if (staticArrayFromNullLength != null) {
            code.loadConstant(staticArrayFromNullLength);
            code.anewarray(OBJECT_DESC);
            return QinIrTypeRef.classType("java.lang.Object[]");
        }
        StaticArrayFromFactory staticArrayFromFactory = staticArrayFromFactory(methodCallExpression);
        if (staticArrayFromFactory != null) {
            return emitStaticArrayFromFactory(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    staticArrayFromFactory);
        }
        if (method.staticMethod() && methodCallExpression.receiver() instanceof QinIrThisExpression) {
            List<QinIrTypeRef> argumentTypes = inferDeclarationArgumentTypes(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments());
            ResolvedStaticMethodCall staticThisMethod = resolveStaticMethodCall(
                    ownerDeclaration.binaryName(),
                    methodCallExpression.methodName(),
                    argumentTypes,
                    declarationIndex);
            if (staticThisMethod != null) {
                emitStaticMethodArguments(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments(),
                        staticThisMethod);
                invokeStaticMethod(code, staticThisMethod);
                return staticThisMethod.returnType();
            }
        }
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.receiver());
        QinIrTypeRef ascribedReceiverType =
                ascribedInstanceMethodReceiverType(methodCallExpression, receiverType, declarationIndex);
        if (ascribedReceiverType != null && !sameIrType(receiverType, ascribedReceiverType)) {
            coerceValueForTargetType(code, receiverType, ascribedReceiverType);
            receiverType = ascribedReceiverType;
        }
        if (isJavaObjectGetClassCall(receiverType, methodCallExpression)) {
            code.invokevirtual(OBJECT_DESC, "getClass", MethodTypeDesc.of(CLASS_DESC));
            return QinIrTypeRef.classType("java.lang.Class");
        }
        QinIrTypeRef generatedEnumMetadataReturnType =
                generatedEnumMetadataMethodReturnType(methodCallExpression);
        if (generatedEnumMetadataReturnType != null
                && isGeneratedEnumLikeType(receiverType, declarationIndex)) {
            return emitGeneratedEnumMetadataMethodCall(
                    code,
                    methodCallExpression,
                    generatedEnumMetadataReturnType);
        }
        QinIrTypeRef javaNumberValueReturnType = javaNumberValueInstanceMethodReturnType(receiverType, methodCallExpression);
        if (javaNumberValueReturnType != null) {
            return emitJavaNumberValueInstanceMethodCall(code, receiverType, methodCallExpression, javaNumberValueReturnType);
        }
        QinIrTypeRef javaDequeElementReturnType = javaDequeElementReturnType(
                receiverType,
                methodCallExpression.methodName(),
                methodCallExpression.arguments().size());
        if (javaDequeElementReturnType != null) {
            return emitJavaDequeElementCall(
                    code,
                    receiverType,
                    methodCallExpression,
                    javaDequeElementReturnType);
        }
        if (isStringIndexOfCall(receiverType, methodCallExpression)) {
            return emitStringIndexOfCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression);
        }
        if (isStringCharCodeAtCall(receiverType, methodCallExpression)) {
            return emitStringCharCodeAtCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression);
        }
        if (isStringCompareToCall(receiverType, methodCallExpression)) {
            return emitStringCompareToCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression);
        }
        if (isStringReplaceCall(receiverType, methodCallExpression)) {
            return emitStringReplaceCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression);
        }
        QinIrTypeRef javaStringInstanceReturnType = inferJavaStringInstanceMethodReturnType(
                receiverType,
                methodCallExpression);
        if (javaStringInstanceReturnType != null) {
            return emitJavaStringInstanceMethodCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression,
                    javaStringInstanceReturnType);
        }
        if (isJavaClassToStringCall(receiverType, methodCallExpression)) {
            code.invokevirtual(CLASS_DESC, "toString", MethodTypeDesc.of(STRING_DESC));
            return QinIrTypeRef.stringType();
        }
        if (isJavaClassReflectMethodLookupCall(receiverType, methodCallExpression)) {
            return emitJavaClassReflectMethodLookupCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression);
        }
        if (isJavaLangLongRuntimeToStringCall(ownerDeclaration, method, methodCallExpression)) {
            coerceValueForJavaParameterType(code, receiverType, long.class);
            if (methodCallExpression.arguments().isEmpty()) {
                code.invokestatic(
                        ClassDesc.of("java.lang.Long"),
                        "toString",
                        MethodTypeDesc.ofDescriptor("(J)Ljava/lang/String;"));
            } else {
                QinIrTypeRef radixType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                coerceValueForJavaParameterType(code, radixType, int.class);
                code.invokestatic(
                        ClassDesc.of("java.lang.Long"),
                        "toString",
                        MethodTypeDesc.ofDescriptor("(JI)Ljava/lang/String;"));
            }
            return QinIrTypeRef.stringType();
        }
        if (isJavaStreamSumCall(receiverType, methodCallExpression)) {
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_stream_sum__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)D"));
            return QinIrTypeRef.doubleType();
        }
        if (isJavaStreamBoxedCall(receiverType, methodCallExpression)) {
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_stream_boxed__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/util/stream/Stream;"));
            return QinIrTypeRef.classType("java.util.stream.Stream");
        }
        if (isJavaStreamMatchCall(receiverType, methodCallExpression)) {
            return emitJavaStreamMatchCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    receiverType,
                    methodCallExpression);
        }
        if (isJavaSecurityMessageDigestUpdateFacadeCall(receiverType, methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_security_message_digest_update__",
                    MethodTypeDesc.ofDescriptor("(Ljava/security/MessageDigest;Ljava/lang/Object;)V"));
            return QinIrTypeRef.voidType();
        }
        if (isJavaSecurityMessageDigestDigestFacadeCall(receiverType, methodCallExpression)) {
            if (methodCallExpression.arguments().isEmpty()) {
                code.invokevirtual(
                        MESSAGE_DIGEST_DESC,
                        "digest",
                        MethodTypeDesc.ofDescriptor("()[B"));
            } else {
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                code.invokestatic(
                        ESM_GLOBAL_DESC,
                        "__qin_java_security_message_digest_digest__",
                        MethodTypeDesc.ofDescriptor("(Ljava/security/MessageDigest;Ljava/lang/Object;)[B"));
            }
            return QinIrTypeRef.classType("[B");
        }
        if (isJavaUtilHexFormatFormatHexFacadeCall(receiverType, methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_util_hex_format_format_hex__",
                    MethodTypeDesc.ofDescriptor("(Ljava/util/HexFormat;Ljava/lang/Object;)Ljava/lang/String;"));
            return QinIrTypeRef.stringType();
        }
        List<QinIrTypeRef> argumentTypes = inferDeclarationArgumentTypes(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments());
        QinIrTypeRef javaMapMethodReturnType = inferJavaMapMethodReturnType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                receiverType,
                methodCallExpression);
        if (javaMapMethodReturnType != null) {
            return emitJavaMapMethodCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    receiverType,
                    methodCallExpression,
                    javaMapMethodReturnType);
        }
        QinIrTypeRef javaMapEntryMethodReturnType = javaMapEntryMethodReturnType(receiverType, methodCallExpression);
        if (javaMapEntryMethodReturnType != null) {
            return emitJavaMapEntryMethodCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression,
                    javaMapEntryMethodReturnType);
        }
        QinIrTypeRef javaOptionalIntMethodReturnType =
                javaOptionalIntMethodReturnType(receiverType, methodCallExpression);
        if (javaOptionalIntMethodReturnType != null) {
            return emitJavaOptionalIntMethodCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression,
                    javaOptionalIntMethodReturnType);
        }
        if (isJavaUtilArraysStreamFacadeCall(methodCallExpression)) {
            QinIrTypeRef returnType = javaUtilArraysStreamReturnType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            discardExpressionResult(code, receiverType);
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_stream__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/util/stream/Stream;"));
            return returnType;
        }
        if (isJavaUtilArraysToStringFacadeCall(methodCallExpression)) {
            discardExpressionResult(code, receiverType);
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_to_string__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/String;"));
            return QinIrTypeRef.stringType();
        }
        if (isJavaUtilArraysCopyOfFacadeCall(methodCallExpression)) {
            discardExpressionResult(code, receiverType);
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_copy_of__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)[Ljava/lang/Object;"));
            return QinIrTypeRef.classType("java.lang.Object[]");
        }
        if (isJavaUtilArraysFillFacadeCall(methodCallExpression)) {
            discardExpressionResult(code, receiverType);
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_fill__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)V"));
            return QinIrTypeRef.voidType();
        }
        if (isJavaUtilArraysSortRangeFacadeCall(methodCallExpression)) {
            discardExpressionResult(code, receiverType);
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(2));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_sort__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"));
            return QinIrTypeRef.voidType();
        }
        ResolvedInstanceMethodCall typedCollectionToArrayMethod = resolveJavaCollectionTypedToArrayMethodCall(
                receiverType,
                methodCallExpression,
                argumentTypes);
        if (typedCollectionToArrayMethod != null) {
            emitInstanceMethodArguments(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression,
                    typedCollectionToArrayMethod);
            invokeMethod(code, typedCollectionToArrayMethod);
            return typedCollectionToArrayMethod.returnType();
        }
        ResolvedInstanceMethodCall resolvedMethod = resolveInstanceMethodCall(
                receiverType,
                methodCallExpression.methodName(),
                argumentTypes,
                methodCallExpression.arguments(),
                declarationIndex);
        if (resolvedMethod == null) {
            if (isCurrentDeclarationReceiver(ownerDeclaration, receiverType)) {
                resolvedMethod = resolveCurrentDeclarationExactMethodCall(
                        ownerDeclaration,
                        methodCallExpression.methodName());
                if (resolvedMethod == null) {
                    resolvedMethod = resolveLocalDeclarationInstanceMethodCall(
                            ownerDeclaration,
                            methodCallExpression.methodName(),
                            methodCallExpression.arguments().size(),
                            declarationIndex);
                }
                if (resolvedMethod != null) {
                    emitInstanceMethodArguments(
                            code,
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            methodCallExpression,
                            resolvedMethod);
                    invokeMethod(code, resolvedMethod);
                    return resolvedMethod.returnType();
                }
            }
            if (methodCallExpression.receiver() instanceof QinIrThisExpression) {
                ResolvedStaticMethodCall staticMethod = resolveStaticMethodCall(
                        ownerDeclaration.binaryName(),
                        methodCallExpression.methodName(),
                        argumentTypes,
                        declarationIndex);
                if (staticMethod != null) {
                    discardExpressionResult(code, receiverType);
                    emitStaticMethodArguments(
                            code,
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            methodCallExpression.arguments(),
                            staticMethod);
                    invokeStaticMethod(code, staticMethod);
                    return staticMethod.returnType();
                }
            }
            if (methodCallExpression.receiver() instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
                String classLiteralBinaryName = resolveJavaClassLiteralBinaryName(
                        ownerDeclaration,
                        declarationIndex,
                        classLiteralExpression);
                ResolvedStaticMethodCall staticMethod = resolveStaticMethodCall(
                        classLiteralBinaryName,
                        methodCallExpression.methodName(),
                        argumentTypes,
                        declarationIndex);
                if (Boolean.getBoolean("qin.declarationClass.trace")) {
                    System.err.println("[QinJvmDeclarationClassEmitter] class-literal static lookup"
                            + " literal=" + classLiteralExpression
                            + " method=" + methodCallExpression.methodName()
                            + " args=" + methodCallExpression.arguments().size()
                            + " direct=" + (staticMethod == null ? "<null>" : staticMethod.ownerBinaryName())
                            + " indexHit=" + declarationIndex.get(classLiteralBinaryName)
                            + " keys=" + nearbyDeclarationIndexKeys(declarationIndex, classLiteralBinaryName));
                }
                if (staticMethod == null) {
                    QinIrClassDeclaration declaredClassReference = resolveDeclaredClassReference(
                            ownerDeclaration,
                            declarationIndex,
                            classLiteralBinaryName);
                    if (Boolean.getBoolean("qin.declarationClass.trace")) {
                        System.err.println("[QinJvmDeclarationClassEmitter] class-literal declared reference"
                                + " literal=" + classLiteralExpression
                                + " resolved=" + (declaredClassReference == null ? "<null>" : declaredClassReference.binaryName()));
                    }
                    if (declaredClassReference != null) {
                        staticMethod = resolveStaticMethodCall(
                                declaredClassReference.binaryName(),
                                methodCallExpression.methodName(),
                                argumentTypes,
                                declarationIndex);
                    }
                }
                if (staticMethod != null) {
                    discardExpressionResult(code, receiverType);
                    emitStaticMethodArguments(
                            code,
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            methodCallExpression.arguments(),
                            staticMethod);
                    invokeStaticMethod(code, staticMethod);
                    return staticMethod.returnType();
                }
                QinIrClassDeclaration generatedEnumReference = declarationIndex.get(classLiteralBinaryName);
                if (generatedEnumReference != null
                        && isGeneratedEnumLikeType(
                                QinIrTypeRef.classType(generatedEnumReference.binaryName()),
                                declarationIndex)) {
                    if (isGeneratedEnumValueOfCall(methodCallExpression)) {
                        return emitGeneratedEnumValueOfCall(
                                code,
                                ownerDeclaration,
                                method,
                                declarationIndex,
                                localFrame,
                                generatedEnumReference,
                                methodCallExpression);
                    }
                    if (isGeneratedEnumValuesCall(methodCallExpression)) {
                        return emitGeneratedEnumValuesCall(code);
                    }
                }
            }
            if (methodCallExpression.receiver() instanceof QinIrIdentifierReference identifierReference) {
                QinIrClassDeclaration declaredClassReference = resolveDeclaredClassReference(
                        ownerDeclaration,
                        declarationIndex,
                        identifierReference.name());
                if (declaredClassReference != null) {
                    ResolvedStaticMethodCall staticMethod = resolveStaticMethodCall(
                            declaredClassReference.binaryName(),
                            methodCallExpression.methodName(),
                            argumentTypes,
                            declarationIndex);
                    if (staticMethod != null) {
                        discardExpressionResult(code, receiverType);
                        emitStaticMethodArguments(
                                code,
                                ownerDeclaration,
                                method,
                                declarationIndex,
                                localFrame,
                                methodCallExpression.arguments(),
                                staticMethod);
                        invokeStaticMethod(code, staticMethod);
                        return staticMethod.returnType();
                    }
                    if (isGeneratedEnumValueOfCall(methodCallExpression)) {
                        return emitGeneratedEnumValueOfCall(
                                code,
                                ownerDeclaration,
                                method,
                                declarationIndex,
                                localFrame,
                                declaredClassReference,
                                methodCallExpression);
                    }
                    if (isGeneratedEnumValuesCall(methodCallExpression)) {
                        return emitGeneratedEnumValuesCall(code);
                    }
                }
            }
            ResolvedStaticMethodCall enclosingStaticMethod = resolveEnclosingStaticMethodCall(
                    receiverType,
                    methodCallExpression.methodName(),
                    methodCallExpression.arguments().size(),
                    declarationIndex);
            if (enclosingStaticMethod != null) {
                discardExpressionResult(code, receiverType);
                emitStaticMethodArguments(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments(),
                        enclosingStaticMethod);
                invokeStaticMethod(code, enclosingStaticMethod);
                return enclosingStaticMethod.returnType();
            }
            if (canUseDynamicEnclosingStaticMethod(receiverType)) {
                discardExpressionResult(code, receiverType);
                code.ldc(enclosingBinaryName(receiverType.binaryName()));
                QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                        "QinJvmDeclarationClassEmitter"
                                + " owner=" + ownerDeclaration.binaryName()
                                + " method=" + method.name()
                                + " receiverShape=" + methodCallExpression.receiver().getClass().getSimpleName()
                                + " receiverType=" + receiverType
                                + " call=" + methodCallExpression.methodName()
                                + "/" + methodCallExpression.arguments().size()
                                + " enclosing=" + enclosingBinaryName(receiverType.binaryName()),
                        "__qin_global__");
                code.invokestatic(
                        ESM_GLOBAL_DESC,
                        "__qin_global__",
                        MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"));
                return emitDynamicObjectMethodCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            if (isStringIndexOfCall(receiverType, methodCallExpression)) {
                return emitStringIndexOfCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            if (isStringCharCodeAtCall(receiverType, methodCallExpression)) {
                return emitStringCharCodeAtCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            if (isStringCompareToCall(receiverType, methodCallExpression)) {
                return emitStringCompareToCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            if (isStringReplaceCall(receiverType, methodCallExpression)) {
                return emitStringReplaceCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            QinIrTypeRef fallbackJavaStringInstanceReturnType = inferJavaStringInstanceMethodReturnType(
                    receiverType,
                    methodCallExpression);
            if (fallbackJavaStringInstanceReturnType != null) {
                return emitJavaStringInstanceMethodCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression,
                        fallbackJavaStringInstanceReturnType);
            }
            if (generatedEnumMetadataReturnType != null
                    && isGeneratedEnumLikeType(receiverType, declarationIndex)) {
                return emitGeneratedEnumMetadataMethodCall(
                        code,
                        methodCallExpression,
                        generatedEnumMetadataReturnType);
            }
            if (isNumberPrototypeMethodCall(receiverType, methodCallExpression)) {
                return emitNumberPrototypeMethodCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        receiverType,
                        methodCallExpression);
            }
        if (isObjectArrayCloneCall(receiverType, methodCallExpression)) {
            return emitObjectArrayCloneCall(code);
        }
        if (isObjectArraySliceCall(receiverType, methodCallExpression)) {
            return emitObjectArraySliceCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression);
        }
        if (isObjectArraySortCall(receiverType, methodCallExpression)) {
            return emitObjectArraySortCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression);
        }
        if (isJavaListPushCall(receiverType, methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokevirtual(ARRAY_LIST_DESC, "add", LIST_ADD_SIGNATURE);
            return QinIrTypeRef.booleanType();
        }
        if (isJavaListJoinCall(receiverType, methodCallExpression)) {
            return emitJavaListJoinCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression);
        }
        if (isObjectArrayType(receiverType)) {
            return emitDynamicObjectMethodCall(
                    code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            if (isJavaListDynamicArrayMethodCall(receiverType, methodCallExpression)) {
                return emitDynamicObjectMethodCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            if (isDynamicObjectType(receiverType)) {
                return emitDynamicObjectMethodCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            if (isCurrentOrLocalDeclarationReceiver(receiverType, declarationIndex)) {
                QinIrClassDeclaration localDeclaration = resolveIndexedDeclaration(declarationIndex, receiverType.binaryName());
                ResolvedInstanceMethodCall localResolvedMethod = resolveLocalDeclarationInstanceMethodCall(
                        localDeclaration,
                        methodCallExpression.methodName(),
                        methodCallExpression.arguments().size(),
                        declarationIndex);
                if (localResolvedMethod != null) {
                    emitInstanceMethodArguments(
                            code,
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            methodCallExpression,
                            localResolvedMethod);
                    invokeMethod(code, localResolvedMethod);
                    return localResolvedMethod.returnType();
                }
            }
            if (canUseDynamicGeneratedInstanceMethod(receiverType)) {
                return emitDynamicObjectMethodCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression);
            }
            if (isCurrentOrLocalDeclarationReceiver(receiverType, declarationIndex)) {
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
                            + receiverType.binaryName() + "." + methodCallExpression.methodName()
                            + "; owner=" + ownerDeclaration.binaryName()
                            + "; method=" + method.name()
                            + "; receiverExpression=" + methodCallExpression.receiver()
                            + "; expression=" + methodCallExpression
                            + "; currentReceiver=" + isCurrentDeclarationReceiver(ownerDeclaration, receiverType)
                            + "; receiverKind=" + receiverType.kind()
                            + "; receiverBinaryName=" + receiverType.binaryName()
                            + "; ownerMethods=" + ownerDeclaration.methods().stream()
                                    .map(candidate -> candidate.name()
                                            + ":static=" + candidate.staticMethod()
                                            + ":params=" + candidate.parameters().size())
                                    .toList());
        }

        emitInstanceMethodArguments(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression,
                resolvedMethod);

        QinIrTypeRef genericJavaListReturnType = javaListMethodReturnType(receiverType, methodCallExpression);
        QinIrTypeRef genericJavaIterableReturnType = javaIterableMethodReturnType(receiverType, methodCallExpression);
        QinIrTypeRef genericJavaIteratorReturnType = javaIteratorMethodReturnType(receiverType, methodCallExpression);
        QinIrTypeRef genericJavaStreamReturnType = javaStreamMethodReturnType(receiverType, methodCallExpression);
        QinIrTypeRef genericJavaOptionalReturnType = javaOptionalMethodReturnType(receiverType, methodCallExpression);
        invokeMethod(code, resolvedMethod);
        if (genericJavaListReturnType != null) {
            if (isReferenceType(genericJavaListReturnType)) {
                coerceObjectResultForType(code, genericJavaListReturnType);
            }
            return genericJavaListReturnType;
        }
        if (genericJavaIterableReturnType != null) {
            if (isReferenceType(genericJavaIterableReturnType)) {
                coerceObjectResultForType(code, genericJavaIterableReturnType);
            }
            return genericJavaIterableReturnType;
        }
        if (genericJavaIteratorReturnType != null) {
            if (isReferenceType(genericJavaIteratorReturnType)) {
                coerceObjectResultForType(code, genericJavaIteratorReturnType);
            }
            return genericJavaIteratorReturnType;
        }
        if (genericJavaOptionalReturnType != null) {
            if (isReferenceType(genericJavaOptionalReturnType)) {
                coerceObjectResultForType(code, genericJavaOptionalReturnType);
            }
            return genericJavaOptionalReturnType;
        }
        if (genericJavaStreamReturnType != null) {
            if (isReferenceType(genericJavaStreamReturnType)) {
                coerceObjectResultForType(code, genericJavaStreamReturnType);
            }
            return genericJavaStreamReturnType;
        }
        return resolvedMethod.returnType();
    }

    private QinIrTypeRef ascribedInstanceMethodReceiverType(
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef receiverType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (methodCallExpression == null
                || methodCallExpression.ownerBinaryName() == null
                || methodCallExpression.ownerBinaryName().isBlank()) {
            return null;
        }
        String ownerBinaryName = canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName());
        if (ownerBinaryName == null
                || ownerBinaryName.isBlank()
                || "java.lang.Object".equals(ownerBinaryName)) {
            return null;
        }
        if (receiverType != null
                && receiverType.kind() == QinIrTypeKind.CLASS
                && ownerBinaryName.equals(canonicalJavaSdkAliasBinaryName(receiverType.binaryName()))) {
            return receiverType;
        }
        QinIrClassDeclaration localDeclaration = resolveIndexedDeclaration(declarationIndex, ownerBinaryName);
        if (localDeclaration != null) {
            return QinIrTypeRef.classType(localDeclaration.binaryName());
        }
        return QinIrTypeRef.classType(ownerBinaryName);
    }

    private boolean isJavaUtilArraysStreamFacadeCall(QinIrInstanceMethodCallExpression methodCallExpression) {
        if (methodCallExpression == null
                || !"stream".equals(methodCallExpression.methodName())
                || methodCallExpression.arguments().size() != 1
                || !(methodCallExpression.receiver() instanceof QinIrJavaClassLiteralExpression classLiteralExpression)) {
            return false;
        }
        return "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(classLiteralExpression.binaryName()));
    }

    private boolean isJavaUtilArraysStreamStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "stream".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1
                && "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private QinIrTypeRef javaUtilArraysStreamReturnType(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression sourceExpression) {
        QinIrTypeRef sourceType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                sourceExpression);
        QinIrTypeRef elementType;
        if (isObjectArrayType(sourceType)) {
            elementType = staticArrayElementType(sourceType);
        } else if (isStaticCollectionElementAccessType(sourceType) || isIterableType(sourceType)) {
            elementType = staticCollectionElementType(sourceType);
        } else {
            elementType = QinIrTypeRef.classType("java.lang.Object");
        }
        return QinIrTypeRef.classType("java.util.stream.Stream", List.of(boxForObjectStorage(elementType)));
    }

    private boolean isJavaUtilArraysToStringStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "toString".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1
                && "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private boolean isJavaUtilArraysAsListArrayStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "asListArray".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1
                && "__QinJavaUtilArrays".equals(methodCallExpression.classLocalName())
                && "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private boolean isJavaUtilArraysCopyOfStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "copyOf".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2
                && "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private boolean isJavaUtilArraysFillStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "fill".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2
                && "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private boolean isJavaUtilArraysSortRangeStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "sort".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 3
                && "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private boolean isJavaUtilCollectionsAddAllStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "addAll".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2
                && "java.util.Collections".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private boolean isJavaUtilCollectionsNCopiesStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "nCopies".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2
                && "java.util.Collections".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private boolean isJavaSecurityMessageDigestGetInstanceStaticFacadeCall(
            QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "getInstance".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1
                && isJavaSecurityMessageDigestFacadeOwner(methodCallExpression.ownerBinaryName());
    }

    private boolean isJavaUtilHexFormatOfStaticFacadeCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "of".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().isEmpty()
                && isJavaUtilHexFormatFacadeOwner(methodCallExpression.ownerBinaryName());
    }

    private boolean isJavaSecurityMessageDigestFacadeOwner(String ownerBinaryName) {
        return "java.security.MessageDigest".equals(canonicalJavaSdkAliasBinaryName(ownerBinaryName))
                || "__QinJavaSecurityMessageDigestRuntime".equals(ownerBinaryName);
    }

    private boolean isJavaUtilHexFormatFacadeOwner(String ownerBinaryName) {
        return "java.util.HexFormat".equals(canonicalJavaSdkAliasBinaryName(ownerBinaryName))
                || "__QinJavaUtilHexFormatRuntime".equals(ownerBinaryName);
    }

    private boolean isJavaUtilArraysToStringFacadeCall(QinIrInstanceMethodCallExpression methodCallExpression) {
        if (methodCallExpression == null
                || !"toString".equals(methodCallExpression.methodName())
                || methodCallExpression.arguments().size() != 1
                || !(methodCallExpression.receiver() instanceof QinIrJavaClassLiteralExpression classLiteralExpression)) {
            return false;
        }
        return "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(classLiteralExpression.binaryName()));
    }

    private boolean isJavaUtilArraysCopyOfFacadeCall(QinIrInstanceMethodCallExpression methodCallExpression) {
        if (methodCallExpression == null
                || !"copyOf".equals(methodCallExpression.methodName())
                || methodCallExpression.arguments().size() != 2
                || !(methodCallExpression.receiver() instanceof QinIrJavaClassLiteralExpression classLiteralExpression)) {
            return false;
        }
        return "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(classLiteralExpression.binaryName()));
    }

    private boolean isJavaUtilArraysFillFacadeCall(QinIrInstanceMethodCallExpression methodCallExpression) {
        if (methodCallExpression == null
                || !"fill".equals(methodCallExpression.methodName())
                || methodCallExpression.arguments().size() != 2
                || !(methodCallExpression.receiver() instanceof QinIrJavaClassLiteralExpression classLiteralExpression)) {
            return false;
        }
        return "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(classLiteralExpression.binaryName()));
    }

    private boolean isJavaUtilArraysSortRangeFacadeCall(QinIrInstanceMethodCallExpression methodCallExpression) {
        if (methodCallExpression == null
                || !"sort".equals(methodCallExpression.methodName())
                || methodCallExpression.arguments().size() != 3
                || !(methodCallExpression.receiver() instanceof QinIrJavaClassLiteralExpression classLiteralExpression)) {
            return false;
        }
        return "java.util.Arrays".equals(canonicalJavaSdkAliasBinaryName(classLiteralExpression.binaryName()));
    }

    private boolean isJavaSecurityMessageDigestUpdateFacadeCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isJavaSecurityMessageDigestType(receiverType)
                && methodCallExpression != null
                && "update".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1;
    }

    private boolean isJavaSecurityMessageDigestDigestFacadeCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isJavaSecurityMessageDigestType(receiverType)
                && methodCallExpression != null
                && "digest".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() <= 1;
    }

    private boolean isJavaUtilHexFormatFormatHexFacadeCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isJavaUtilHexFormatType(receiverType)
                && methodCallExpression != null
                && "formatHex".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1;
    }

    private boolean isJavaSecurityMessageDigestType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && "java.security.MessageDigest".equals(canonicalJavaSdkAliasBinaryName(type.binaryName()));
    }

    private boolean isJavaUtilHexFormatType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && "java.util.HexFormat".equals(canonicalJavaSdkAliasBinaryName(type.binaryName()));
    }

    private boolean isJavaStreamSumCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (receiverType == null
                || methodCallExpression == null
                || !"sum".equals(methodCallExpression.methodName())
                || !methodCallExpression.arguments().isEmpty()) {
            return false;
        }
        return "java.util.stream.Stream".equals(receiverType.binaryName())
                || "java.util.stream.IntStream".equals(receiverType.binaryName())
                || "java.util.stream.LongStream".equals(receiverType.binaryName())
                || "java.util.stream.DoubleStream".equals(receiverType.binaryName());
    }

    private boolean isJavaStreamBoxedCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (receiverType == null
                || methodCallExpression == null
                || !"boxed".equals(methodCallExpression.methodName())
                || !methodCallExpression.arguments().isEmpty()) {
            return false;
        }
        return "java.util.stream.Stream".equals(receiverType.binaryName())
                || "java.util.stream.IntStream".equals(receiverType.binaryName())
                || "java.util.stream.LongStream".equals(receiverType.binaryName())
                || "java.util.stream.DoubleStream".equals(receiverType.binaryName());
    }

    private boolean isJavaStreamMatchCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return receiverType != null
                && receiverType.kind() == QinIrTypeKind.CLASS
                && receiverType.binaryName() != null
                && methodCallExpression.arguments().size() == 1
                && ("anyMatch".equals(methodCallExpression.methodName())
                || "allMatch".equals(methodCallExpression.methodName())
                || "noneMatch".equals(methodCallExpression.methodName()))
                && javaStreamPredicateClass(receiverType.binaryName()) != null;
    }

    private QinIrTypeRef javaStreamMethodReturnType(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (receiverType == null
                || receiverType.kind() != QinIrTypeKind.CLASS
                || receiverType.binaryName() == null
                || methodCallExpression == null) {
            return null;
        }
        if (!"java.util.stream.Stream".equals(receiverType.binaryName())) {
            return primitiveJavaStreamMethodReturnType(receiverType, methodCallExpression);
        }
        QinIrTypeRef elementType = receiverType.typeArguments().isEmpty()
                ? QinIrTypeRef.classType("java.lang.Object")
                : boxForObjectStorage(receiverType.typeArguments().get(0));
        int argumentCount = methodCallExpression.arguments().size();
        return switch (methodCallExpression.methodName()) {
            case "filter", "peek" -> argumentCount == 1
                    ? QinIrTypeRef.classType("java.util.stream.Stream", List.of(elementType))
                    : null;
            case "sorted", "distinct", "sequential", "parallel", "unordered" -> argumentCount <= 1
                    ? QinIrTypeRef.classType("java.util.stream.Stream", List.of(elementType))
                    : null;
            case "map", "flatMap" -> argumentCount == 1
                    ? QinIrTypeRef.classType("java.util.stream.Stream", List.of(QinIrTypeRef.classType("java.lang.Object")))
                    : null;
            case "anyMatch", "allMatch", "noneMatch" -> argumentCount == 1
                    ? QinIrTypeRef.booleanType()
                    : null;
            case "mapToInt" -> argumentCount == 1
                    ? QinIrTypeRef.classType("java.util.stream.IntStream")
                    : null;
            case "toList" -> argumentCount == 0
                    ? QinIrTypeRef.classType("java.util.List", List.of(elementType))
                    : null;
            case "collect" -> argumentCount == 1
                    ? javaStreamCollectReturnType(methodCallExpression.arguments().get(0), elementType)
                    : null;
            case "min", "max", "findFirst", "findAny" -> argumentCount <= 1
                    ? QinIrTypeRef.classType("java.util.Optional", List.of(elementType))
                    : null;
            default -> null;
        };
    }

    private QinIrTypeRef javaStreamCollectReturnType(QinIrExpression collectorExpression, QinIrTypeRef elementType) {
        if (!(collectorExpression instanceof QinIrStaticMethodCallExpression staticMethodCall)
                || !"java.util.stream.Collectors".equals(canonicalJavaSdkAliasBinaryName(staticMethodCall.ownerBinaryName()))) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return switch (staticMethodCall.methodName()) {
            case "toList", "toUnmodifiableList" ->
                    QinIrTypeRef.classType("java.util.List", List.of(elementType));
            case "toSet", "toUnmodifiableSet" ->
                    QinIrTypeRef.classType("java.util.Set", List.of(elementType));
            case "toMap", "toUnmodifiableMap", "groupingBy" ->
                    QinIrTypeRef.classType("java.util.Map");
            default -> QinIrTypeRef.classType("java.lang.Object");
        };
    }

    private QinIrTypeRef javaOptionalMethodReturnType(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (receiverType == null
                || receiverType.kind() != QinIrTypeKind.CLASS
                || receiverType.binaryName() == null
                || methodCallExpression == null
                || !"java.util.Optional".equals(receiverType.binaryName())) {
            return null;
        }
        QinIrTypeRef elementType = receiverType.typeArguments().isEmpty()
                ? QinIrTypeRef.classType("java.lang.Object")
                : boxForObjectStorage(receiverType.typeArguments().get(0));
        int argumentCount = methodCallExpression.arguments().size();
        return switch (methodCallExpression.methodName()) {
            case "orElseThrow" -> argumentCount <= 1 ? elementType : null;
            case "get" -> argumentCount == 0 ? elementType : null;
            case "orElse" -> argumentCount == 1 ? elementType : null;
            case "isPresent", "isEmpty" -> argumentCount == 0 ? QinIrTypeRef.booleanType() : null;
            case "stream" -> argumentCount == 0
                    ? QinIrTypeRef.classType("java.util.stream.Stream", List.of(elementType))
                    : null;
            case "map", "flatMap" -> argumentCount == 1
                    ? QinIrTypeRef.classType("java.util.Optional", List.of(QinIrTypeRef.classType("java.lang.Object")))
                    : null;
            default -> null;
        };
    }

    private QinIrTypeRef javaOptionalIntMethodReturnType(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (receiverType == null
                || receiverType.kind() != QinIrTypeKind.CLASS
                || receiverType.binaryName() == null
                || methodCallExpression == null
                || !"java.util.OptionalInt".equals(receiverType.binaryName())) {
            return null;
        }
        int argumentCount = methodCallExpression.arguments().size();
        return switch (methodCallExpression.methodName()) {
            case "orElse" -> argumentCount == 1 ? QinIrTypeRef.intType() : null;
            case "orElseThrow", "getAsInt" -> argumentCount == 0 ? QinIrTypeRef.intType() : null;
            case "isPresent", "isEmpty" -> argumentCount == 0 ? QinIrTypeRef.booleanType() : null;
            default -> null;
        };
    }

    private QinIrTypeRef emitJavaOptionalIntMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        ClassDesc optionalIntDesc = ClassDesc.of("java.util.OptionalInt");
        switch (methodCallExpression.methodName()) {
            case "orElse" -> {
                QinIrTypeRef argumentType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                coerceValueForTargetType(code, argumentType, QinIrTypeRef.intType());
                code.invokevirtual(
                        optionalIntDesc,
                        "orElse",
                        MethodTypeDesc.ofDescriptor("(I)I"));
            }
            case "orElseThrow" -> code.invokevirtual(
                    optionalIntDesc,
                    "orElseThrow",
                    MethodTypeDesc.ofDescriptor("()I"));
            case "getAsInt" -> code.invokevirtual(
                    optionalIntDesc,
                    "getAsInt",
                    MethodTypeDesc.ofDescriptor("()I"));
            case "isPresent" -> code.invokevirtual(
                    optionalIntDesc,
                    "isPresent",
                    MethodTypeDesc.ofDescriptor("()Z"));
            case "isEmpty" -> code.invokevirtual(
                    optionalIntDesc,
                    "isEmpty",
                    MethodTypeDesc.ofDescriptor("()Z"));
            default -> throw new IllegalArgumentException(
                    "Unsupported OptionalInt method: " + methodCallExpression.methodName());
        }
        return returnType;
    }

    private boolean isReferenceType(QinIrTypeRef type) {
        return type != null
                && (type.kind() == QinIrTypeKind.CLASS || type.kind() == QinIrTypeKind.STRING);
    }

    private QinIrTypeRef emitJavaStreamMatchCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        Class<?> predicateClass = javaStreamPredicateClass(receiverType.binaryName());
        if (predicateClass == null) {
            throw new IllegalArgumentException("Unsupported Java stream match receiver: " + receiverType);
        }
        code.checkcast(ClassDesc.of(receiverType.binaryName()));
        emitExpressionAsJavaFunctionalInterfaceParameter(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(0),
                QinIrTypeRef.classType(predicateClass.getName()),
                predicateClass);
        code.invokeinterface(
                ClassDesc.of(receiverType.binaryName()),
                methodCallExpression.methodName(),
                MethodTypeDesc.ofDescriptor("(" + javaStreamPredicateDescriptor(predicateClass) + ")Z"));
        return QinIrTypeRef.booleanType();
    }

    private Class<?> javaStreamPredicateClass(String streamBinaryName) {
        return switch (streamBinaryName) {
            case "java.util.stream.Stream" -> java.util.function.Predicate.class;
            case "java.util.stream.IntStream" -> java.util.function.IntPredicate.class;
            case "java.util.stream.LongStream" -> java.util.function.LongPredicate.class;
            case "java.util.stream.DoubleStream" -> java.util.function.DoublePredicate.class;
            default -> null;
        };
    }

    private String javaStreamPredicateDescriptor(Class<?> predicateClass) {
        if (predicateClass == java.util.function.Predicate.class) {
            return "Ljava/util/function/Predicate;";
        }
        if (predicateClass == java.util.function.IntPredicate.class) {
            return "Ljava/util/function/IntPredicate;";
        }
        if (predicateClass == java.util.function.LongPredicate.class) {
            return "Ljava/util/function/LongPredicate;";
        }
        if (predicateClass == java.util.function.DoublePredicate.class) {
            return "Ljava/util/function/DoublePredicate;";
        }
        throw new IllegalArgumentException("Unsupported Java stream predicate: " + predicateClass.getName());
    }

    private boolean isJavaClassToStringCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return receiverType != null
                && "java.lang.Class".equals(receiverType.binaryName())
                && methodCallExpression != null
                && "toString".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().isEmpty();
    }

    private boolean isJavaClassReflectMethodLookupCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return receiverType != null
                && "java.lang.Class".equals(receiverType.binaryName())
                && methodCallExpression != null
                && ("getDeclaredMethod".equals(methodCallExpression.methodName())
                        || "getMethod".equals(methodCallExpression.methodName()))
                && !methodCallExpression.arguments().isEmpty();
    }

    private QinIrTypeRef emitJavaClassReflectMethodLookupCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef nameType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(0));
        coerceValueForJavaParameterType(code, nameType, String.class);
        emitJavaClassArrayVarargs(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().subList(1, methodCallExpression.arguments().size()));
        code.invokevirtual(
                CLASS_DESC,
                methodCallExpression.methodName(),
                MethodTypeDesc.of(REFLECT_METHOD_DESC, STRING_DESC, CLASS_ARRAY_DESC));
        return QinIrTypeRef.classType("java.lang.reflect.Method");
    }

    private void emitJavaClassArrayVarargs(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            List<QinIrExpression> arguments) {
        if (arguments.isEmpty()) {
            code.loadConstant(0);
            code.anewarray(CLASS_DESC);
            return;
        }
        if (arguments.size() == 1 && arguments.get(0) instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    spreadArgumentExpression.expression());
            coerceValueForJavaParameterType(code, actualType, Class[].class);
            return;
        }
        code.loadConstant(arguments.size());
        code.anewarray(CLASS_DESC);
        for (int i = 0; i < arguments.size(); i++) {
            code.dup();
            code.loadConstant(i);
            if (emitNullLiteralForReferenceParameter(
                    code,
                    arguments.get(i),
                    QinIrTypeRef.classType("java.lang.Class"),
                    Class.class)) {
                code.aastore();
                continue;
            }
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i));
            coerceValueForJavaParameterType(code, actualType, Class.class);
            code.aastore();
        }
    }

    private boolean isJavaObjectGetClassCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isReferenceLike(receiverType)
                && methodCallExpression != null
                && "getClass".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().isEmpty();
    }

    private boolean isCurrentDeclarationReceiver(
            QinIrClassDeclaration ownerDeclaration,
            QinIrTypeRef receiverType) {
        return receiverType != null
                && ownerDeclaration.binaryName().equals(receiverType.binaryName());
    }

    private boolean isCurrentOrLocalDeclarationReceiver(
            QinIrTypeRef receiverType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return receiverType != null
                && receiverType.kind() == QinIrTypeKind.CLASS
                && declarationIndex != null
                && declarationIndex.containsKey(receiverType.binaryName());
    }

    private ResolvedInstanceMethodCall resolveCurrentDeclarationExactMethodCall(
            QinIrClassDeclaration declaration,
            String methodName) {
        boolean debugCurrentMethod = "buildStaticRuleInvocationPlans".equals(methodName);
        if (debugCurrentMethod) {
            System.err.println("[QinJvmDebug] exact current method lookup owner="
                    + declaration.binaryName()
                    + " methods=" + declaration.methods().stream()
                            .map(candidate -> candidate.name()
                                    + ":static=" + candidate.staticMethod()
                                    + ":params=" + candidate.parameters().size())
                            .toList());
        }
        QinIrMethodDeclaration matched = null;
        for (QinIrMethodDeclaration candidate : declaration.methods()) {
            if (candidate.staticMethod() || !candidate.name().equals(methodName)) {
                continue;
            }
            if (matched != null) {
                return null;
            }
            matched = candidate;
        }
        if (matched == null) {
            if (debugCurrentMethod) {
                System.err.println("[QinJvmDebug] exact current method lookup missed owner="
                        + declaration.binaryName()
                        + " requested=" + methodName);
            }
            return null;
        }
        if (debugCurrentMethod) {
            System.err.println("[QinJvmDebug] exact current method lookup hit owner="
                    + declaration.binaryName()
                    + " requested=" + methodName
                    + " params=" + matched.parameters().size());
        }
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (QinIrParameter parameter : matched.parameters()) {
            parameterTypes.add(parameter.type());
        }
        return new ResolvedInstanceMethodCall(
                declaration.binaryName(),
                matched.name(),
                List.copyOf(parameterTypes),
                matched.returnType(),
                false,
                null,
                List.of(),
                hasVarargsParameter(matched));
    }

    private boolean isStringReplaceCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return receiverType != null
                && isStringLike(receiverType)
                && "replace".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2;
    }

    private boolean isStringIndexOfCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return receiverType != null
                && isStringLike(receiverType)
                && ("indexOf".equals(methodCallExpression.methodName())
                        || "lastIndexOf".equals(methodCallExpression.methodName()))
                && (methodCallExpression.arguments().size() == 1
                        || methodCallExpression.arguments().size() == 2);
    }

    private boolean isStringCharCodeAtCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return receiverType != null
                && isStringLike(receiverType)
                && "charCodeAt".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1;
    }

    private boolean isStringCompareToCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return receiverType != null
                && isStringLike(receiverType)
                && "compareTo".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1;
    }

    private QinIrTypeRef emitStringReplaceCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        for (QinIrExpression argument : methodCallExpression.arguments()) {
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
                "__qin_string_replace__",
                MethodTypeDesc.ofDescriptor(
                        "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/String;"));
        return QinIrTypeRef.stringType();
    }

    private QinIrTypeRef emitStringCharCodeAtCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef indexType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(0));
        coerceValueForTargetType(code, indexType, QinIrTypeRef.intType());
        code.invokevirtual(
                STRING_DESC,
                "charAt",
                MethodTypeDesc.ofDescriptor("(I)C"));
        code.i2d();
        return QinIrTypeRef.doubleType();
    }

    private QinIrTypeRef emitStringCompareToCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef argumentType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(0));
        coerceValueForTargetType(code, argumentType, QinIrTypeRef.stringType());
        code.invokevirtual(
                STRING_DESC,
                "compareTo",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)I"));
        return QinIrTypeRef.intType();
    }

    private QinIrTypeRef inferJavaStringInstanceMethodReturnType(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (receiverType == null || !isStringLike(receiverType)) {
            return null;
        }
        int argumentCount = methodCallExpression.arguments().size();
        return switch (methodCallExpression.methodName()) {
            case "includes", "startsWith", "endsWith" ->
                    argumentCount == 1 || argumentCount == 2 ? QinIrTypeRef.booleanType() : null;
            case "trim", "toUpperCase", "toLowerCase", "slice", "substring", "substr", "replace" ->
                    argumentCount == 0 || argumentCount == 1 || argumentCount == 2 || argumentCount == 3
                            ? QinIrTypeRef.stringType()
                            : null;
            case "repeat" -> argumentCount == 1 ? QinIrTypeRef.stringType() : null;
            case "split", "match" -> argumentCount <= 2 ? QinIrTypeRef.classType("java.lang.Object") : null;
            case "charAt" -> argumentCount == 1 ? QinIrTypeRef.stringType() : null;
            case "charCodeAt" -> argumentCount == 1 ? QinIrTypeRef.doubleType() : null;
            case "compareTo" -> argumentCount == 1 ? QinIrTypeRef.intType() : null;
            case "indexOf", "lastIndexOf", "search", "hashCode", "length" ->
                    argumentCount <= 2 ? QinIrTypeRef.doubleType() : null;
            case "valueOf", "toString" -> argumentCount == 0 ? QinIrTypeRef.stringType() : null;
            default -> null;
        };
    }

    private QinIrTypeRef emitJavaStringInstanceMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        emitObjectArrayFromExpressions(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments());
        code.ldc(methodCallExpression.methodName());
        code.swap();
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_java_lang_string_method__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;"));
        coerceObjectResultForType(code, returnType);
        return returnType;
    }

    private String toSnakeCase(String methodName) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < methodName.length(); i++) {
            char ch = methodName.charAt(i);
            if (Character.isUpperCase(ch)) {
                out.append('_').append(Character.toLowerCase(ch));
            } else {
                out.append(ch);
            }
        }
        return out.toString();
    }

    private QinIrTypeRef inferJavaLangStringSdkHelperReturnType(
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (!(methodCallExpression.receiver() instanceof QinIrIdentifierReference receiver)
                || !"__QinJavaLangString".equals(receiver.name())) {
            return null;
        }
        int argumentCount = methodCallExpression.arguments().size();
        return switch (methodCallExpression.methodName()) {
            case "length", "hashCode" -> argumentCount == 1 ? QinIrTypeRef.doubleType() : null;
            case "valueOf" -> argumentCount == 1 ? QinIrTypeRef.stringType() : null;
            case "equals", "contains", "startsWith", "endsWith" ->
                    argumentCount == 2 ? QinIrTypeRef.booleanType() : null;
            case "isEmpty", "isBlank" -> argumentCount == 1 ? QinIrTypeRef.booleanType() : null;
            case "charAt" -> argumentCount == 2 ? QinIrTypeRef.stringType() : null;
            case "substring" -> argumentCount == 2 || argumentCount == 3 ? QinIrTypeRef.stringType() : null;
            case "format" -> argumentCount >= 1 ? QinIrTypeRef.stringType() : null;
            case "regionMatches" -> argumentCount == 5 ? QinIrTypeRef.booleanType() : null;
            default -> null;
        };
    }

    private QinIrTypeRef emitJavaLangStringSdkHelperCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        if ("format".equals(methodCallExpression.methodName())) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            int restCount = methodCallExpression.arguments().size() - 1;
            code.loadConstant(restCount);
            code.anewarray(OBJECT_DESC);
            for (int i = 0; i < restCount; i++) {
                code.dup();
                code.loadConstant(i);
                QinIrTypeRef actualType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(i + 1));
                boxValueForObjectTarget(code, actualType);
                code.aastore();
            }
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_lang_string_format__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/String;"));
            return returnType;
        }

        for (QinIrExpression argument : methodCallExpression.arguments()) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    argument);
        }
        String descriptor = switch (methodCallExpression.methodName()) {
            case "length" -> "(Ljava/lang/Object;)D";
            case "hashCode" -> "(Ljava/lang/Object;)D";
            case "valueOf" -> "(Ljava/lang/Object;)Ljava/lang/String;";
            case "equals" -> "(Ljava/lang/Object;Ljava/lang/Object;)Z";
            case "contains" -> "(Ljava/lang/Object;Ljava/lang/Object;)Z";
            case "isEmpty" -> "(Ljava/lang/Object;)Z";
            case "isBlank" -> "(Ljava/lang/Object;)Z";
            case "startsWith" -> "(Ljava/lang/Object;Ljava/lang/Object;)Z";
            case "endsWith" -> "(Ljava/lang/Object;Ljava/lang/Object;)Z";
            case "charAt" -> "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/String;";
            case "substring" -> "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/String;";
            case "regionMatches" -> "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Z";
            default -> throw new IllegalArgumentException(
                    "Unsupported __QinJavaLangString helper: " + methodCallExpression.methodName());
        };
        if ("substring".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2) {
            code.aconst_null();
        }
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_java_lang_string_" + toSnakeCase(methodCallExpression.methodName()) + "__",
                MethodTypeDesc.ofDescriptor(descriptor));
        return returnType;
    }

    private QinIrTypeRef inferJavaLangNumberSdkHelperReturnType(
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (!isJavaLangNumberSdkHelperReceiver(methodCallExpression.receiver())
                || methodCallExpression.arguments().size() != 1) {
            return null;
        }
        return switch (methodCallExpression.methodName()) {
            case "doubleValue", "floatValue", "longValue", "intValue", "shortValue", "byteValue", "valueOf" ->
                    QinIrTypeRef.doubleType();
            default -> null;
        };
    }

    private QinIrTypeRef inferJavaLangNumberStaticSdkHelperReturnType(
            QinIrStaticMethodCallExpression methodCallExpression) {
        if (methodCallExpression == null
                || methodCallExpression.arguments().size() != 1
                || !"java.lang.Number".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()))) {
            return null;
        }
        return switch (methodCallExpression.methodName()) {
            case "doubleValue", "floatValue", "longValue", "intValue", "shortValue", "byteValue", "valueOf" ->
                    QinIrTypeRef.doubleType();
            default -> null;
        };
    }

    private boolean isJavaLangNumberSdkHelperReceiver(QinIrExpression receiver) {
        if (receiver instanceof QinIrIdentifierReference identifierReference) {
            return "__QinJavaLangNumber".equals(identifierReference.name());
        }
        if (receiver instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            return "java.lang.Number".equals(canonicalJavaSdkAliasBinaryName(classLiteralExpression.binaryName()));
        }
        return false;
    }

    private QinIrTypeRef emitJavaLangNumberSdkHelperCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        emitDeclarationExpressionAsObject(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(0));
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_java_lang_number_" + toSnakeCase(methodCallExpression.methodName()) + "__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)D"));
        return returnType;
    }

    private QinIrTypeRef emitJavaLangNumberStaticSdkHelperCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrStaticMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        emitDeclarationExpressionAsObject(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(0));
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_java_lang_number_" + toSnakeCase(methodCallExpression.methodName()) + "__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)D"));
        return returnType;
    }

    private QinIrTypeRef emitJavaDequeElementCall(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        String ownerBinaryName = canonicalJavaSdkAliasBinaryName(receiverType.binaryName());
        MethodTypeDesc descriptor = MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;");
        if ("java.util.Deque".equals(ownerBinaryName)) {
            code.invokeinterface(
                    ClassDesc.of(ownerBinaryName),
                    methodCallExpression.methodName(),
                    descriptor);
        } else {
            code.invokevirtual(
                    ClassDesc.of(ownerBinaryName),
                    methodCallExpression.methodName(),
                    descriptor);
        }
        coerceObjectResultForType(code, returnType);
        return returnType;
    }

    private QinIrTypeRef emitStringIndexOfCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef needleType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(0));
        coerceValueForTargetType(code, needleType, QinIrTypeRef.stringType());
        if (methodCallExpression.arguments().size() == 1) {
            code.invokevirtual(
                    STRING_DESC,
                    methodCallExpression.methodName(),
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)I"));
        } else {
            QinIrTypeRef fromIndexType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            coerceValueForTargetType(code, fromIndexType, QinIrTypeRef.intType());
            code.invokevirtual(
                    STRING_DESC,
                    methodCallExpression.methodName(),
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/String;I)I"));
        }
        code.i2d();
        return QinIrTypeRef.doubleType();
    }

    private void emitInstanceMethodArguments(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression,
            ResolvedInstanceMethodCall resolvedMethod) {
        if (!resolvedMethod.varargs()) {
            if (methodCallExpression.arguments().size() == 1
                    && methodCallExpression.arguments().get(0) instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
                emitFixedAritySpreadInstanceMethodArguments(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        spreadArgumentExpression,
                        resolvedMethod);
                return;
            }
            for (int i = 0; i < methodCallExpression.arguments().size(); i++) {
                emitInstanceMethodArgument(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(i),
                        resolvedMethod.parameterTypes().get(i),
                        reflectedParameterType(resolvedMethod.reflectedParameterTypes(), i));
            }
            for (int i = methodCallExpression.arguments().size(); i < resolvedMethod.parameterTypes().size(); i++) {
                emitDefaultArgumentValue(code, resolvedMethod.parameterTypes().get(i));
            }
            return;
        }

        int fixedParameterCount = resolvedMethod.parameterTypes().size() - 1;
        for (int i = 0; i < fixedParameterCount; i++) {
            emitInstanceMethodArgument(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(i),
                    resolvedMethod.parameterTypes().get(i),
                    reflectedParameterType(resolvedMethod.reflectedParameterTypes(), i));
        }

        int varargCount = methodCallExpression.arguments().size() - fixedParameterCount;
        if (varargCount == 1
                && methodCallExpression.arguments().get(fixedParameterCount) instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    spreadArgumentExpression.expression());
            coerceValueForTargetType(code, actualType, resolvedMethod.parameterTypes().get(fixedParameterCount));
            return;
        }
        emitVarargsArrayFromExpressions(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments(),
                fixedParameterCount,
                varargCount,
                resolvedMethod.parameterTypes().get(fixedParameterCount),
                reflectedParameterType(resolvedMethod.reflectedParameterTypes(), fixedParameterCount));
    }

    private void emitInstanceMethodArgument(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression argumentExpression,
            QinIrTypeRef targetType,
            Class<?> reflectedTargetType) {
        if (emitExpressionAsJavaFunctionalInterfaceParameter(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                argumentExpression,
                targetType,
                reflectedTargetType)) {
            return;
        }
        if (emitExpressionAsJavaArrayParameter(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                argumentExpression,
                reflectedTargetType)) {
            return;
        }
        if (emitNullLiteralForReferenceParameter(
                code,
                argumentExpression,
                targetType,
                reflectedTargetType)) {
            return;
        }
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                argumentExpression);
        try {
            coerceInstanceMethodArgumentValue(code, declarationIndex, actualType, targetType, reflectedTargetType);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException(
                    "Failed to coerce declaration instance method argument"
                            + " owner=" + ownerDeclaration.binaryName()
                            + " method=" + method.name()
                            + " argument=" + argumentExpression
                            + " actualType=" + actualType
                            + " targetType=" + targetType
                            + " reflectedTargetType=" + (reflectedTargetType == null
                                    ? "<none>"
                            : reflectedTargetType.getName()),
                    error);
        }
    }

    private boolean emitNullLiteralForReferenceParameter(
            java.lang.classfile.CodeBuilder code,
            QinIrExpression argumentExpression,
            QinIrTypeRef targetType,
            Class<?> reflectedTargetType) {
        if (!(argumentExpression instanceof QinIrNullLiteral)) {
            return false;
        }
        if (reflectedTargetType != null) {
            if (reflectedTargetType.isPrimitive()) {
                return false;
            }
            code.aconst_null();
            return true;
        }
        if (!isReferenceLikeParameter(targetType)) {
            return false;
        }
        code.aconst_null();
        return true;
    }

    private boolean emitExpressionAsJavaFunctionalInterfaceParameter(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression argumentExpression,
            QinIrTypeRef targetType,
            Class<?> reflectedTargetType) {
        Class<?> targetClass = javaFunctionalInterfaceTargetClass(
                targetType,
                reflectedTargetType,
                declarationIndex);
        if (targetClass == null) {
            return false;
        }
        QinIrExpression callableExpression = javaFunctionalCallableExpression(argumentExpression);
        if (callableExpression == null) {
            return false;
        }
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                callableExpression);
        boxValueForObjectTarget(code, actualType);
        if (method != null
                && !method.staticMethod()
                && callableExpression instanceof QinIrObjectLiteral functionDefinition
                && isArrowLexicalThisFunctionDefinition(functionDefinition)) {
            code.aload(0);
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_make_function_with_lexical_this__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        }
        code.ldc(ClassDesc.of(targetClass.getName()));
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_java_functional_interface__",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, CLASS_DESC));
        code.checkcast(toReferenceClassDesc(targetClass.getName()));
        return true;
    }

    private Class<?> javaFunctionalInterfaceTargetClass(
            QinIrTypeRef targetType,
            Class<?> reflectedTargetType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (reflectedTargetType != null && isJavaFunctionalInterfaceClass(reflectedTargetType)) {
            return reflectedTargetType;
        }
        if (isJavaFunctionalInterfaceTarget(targetType, declarationIndex)) {
            return resolveClass(targetType.binaryName());
        }
        return null;
    }

    private QinIrExpression javaFunctionalCallableExpression(QinIrExpression argumentExpression) {
        if (!(argumentExpression instanceof QinIrBuiltinCallExpression builtinCall)
                || !"Global".equals(builtinCall.receiverName())
                || builtinCall.arguments().size() != 1) {
            return null;
        }
        if ("__qin_make_function__".equals(builtinCall.methodName())) {
            return builtinCall.arguments().get(0);
        }
        if ("__qin_java_functional".equals(builtinCall.methodName())) {
            QinIrExpression inner = builtinCall.arguments().get(0);
            QinIrExpression unwrapped = javaFunctionalCallableExpression(inner);
            return unwrapped == null ? inner : unwrapped;
        }
        return null;
    }

    private String declarationExpressionSummary(QinIrExpression expression) {
        if (expression == null) {
            return "<null>";
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return "BuiltinCall("
                    + builtinCallExpression.receiverName()
                    + "."
                    + builtinCallExpression.methodName()
                    + "/"
                    + builtinCallExpression.arguments().size()
                    + ")";
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            return "ObjectLiteral(properties=" + objectLiteral.properties().size() + ")";
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            return "Identifier(" + identifierReference.name() + ")";
        }
        return expression.getClass().getSimpleName();
    }

    private void emitFixedAritySpreadInstanceMethodArguments(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrSpreadArgumentExpression spreadArgumentExpression,
            ResolvedInstanceMethodCall resolvedMethod) {
        QinIrTypeRef spreadType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                spreadArgumentExpression.expression());
        if (!isObjectArrayType(spreadType)) {
            throw new IllegalArgumentException(
                    "Fixed-arity declaration method spread requires Object[]: "
                            + resolvedMethod.ownerBinaryName()
                            + "."
                            + resolvedMethod.methodName()
                            + "; actual="
                            + spreadType
                            + "; expression="
                            + spreadArgumentExpression.expression());
        }
        LocalBinding spreadArrayBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_fixed_arity_spread"),
                QinIrTypeRef.classType("java.lang.Object[]"));
        storeLocalForType(
                code,
                spreadArrayBinding.type(),
                spreadArrayBinding.localSlot(),
                spreadArrayBinding.name());
        for (int i = 0; i < resolvedMethod.parameterTypes().size(); i++) {
            loadLocalForType(
                    code,
                    spreadArrayBinding.type(),
                    spreadArrayBinding.localSlot(),
                    spreadArrayBinding.name());
            code.loadConstant(i);
            code.aaload();
            coerceInstanceMethodArgumentValue(
                    code,
                    declarationIndex,
                    QinIrTypeRef.classType("java.lang.Object"),
                    resolvedMethod.parameterTypes().get(i),
                    reflectedParameterType(resolvedMethod.reflectedParameterTypes(), i));
        }
    }

    private void coerceInstanceMethodArgumentValue(
            java.lang.classfile.CodeBuilder code,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrTypeRef actualType,
            QinIrTypeRef targetType,
            Class<?> reflectedTargetType) {
        if (isJavaFunctionalInterfaceTarget(targetType, declarationIndex)) {
            boxValueForObjectTarget(code, actualType);
            code.ldc(ClassDesc.of(targetType.binaryName()));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_functional_interface__",
                    MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, CLASS_DESC));
            code.checkcast(toReferenceClassDesc(targetType.binaryName()));
            return;
        }
        if (reflectedTargetType == null) {
            coerceValueForTargetType(code, actualType, targetType);
        } else {
            coerceValueForJavaParameterType(code, actualType, reflectedTargetType);
        }
    }

    private boolean isJavaFunctionalInterfaceTarget(
            QinIrTypeRef targetType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (targetType == null
                || targetType.kind() != QinIrTypeKind.CLASS
                || targetType.binaryName() == null
                || targetType.binaryName().isBlank()) {
            return false;
        }
        if (resolveIndexedDeclaration(declarationIndex, targetType.binaryName()) != null) {
            return false;
        }
        try {
            return isJavaFunctionalInterfaceClass(resolveClass(targetType.binaryName()));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private boolean isJavaFunctionalInterfaceType(QinIrTypeRef targetType) {
        if (targetType == null
                || targetType.kind() != QinIrTypeKind.CLASS
                || targetType.binaryName() == null
                || targetType.binaryName().isBlank()) {
            return false;
        }
        try {
            return isJavaFunctionalInterfaceClass(resolveClass(targetType.binaryName()));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
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
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                "QinJvmDeclarationClassEmitter"
                        + " owner=" + (ownerDeclaration == null ? "<unknown-owner>" : ownerDeclaration.binaryName())
                        + " method=" + (method == null ? "<unknown-method>" : method.name())
                        + " receiverShape=" + methodCallExpression.receiver().getClass().getSimpleName()
                        + " receiverType=" + inferDeclarationExpressionType(
                                ownerDeclaration,
                                method,
                                declarationIndex,
                                localFrame,
                                methodCallExpression.receiver())
                        + " call=" + methodCallExpression.methodName()
                        + "/" + methodCallExpression.arguments().size(),
                "__qin_call_method_array__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_call_method_array__",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC, OBJECT_ARRAY_DESC));
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef emitNumberPrototypeMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        boxValueForObjectTarget(code, receiverType);
        code.ldc(methodCallExpression.methodName());
        emitObjectArrayFromExpressions(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments());
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                "QinJvmDeclarationClassEmitter",
                "__qin_call_method_array__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_call_method_array__",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC, OBJECT_ARRAY_DESC));
        code.checkcast(STRING_DESC);
        return QinIrTypeRef.stringType();
    }

    private boolean isNumberPrototypeMethodCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isJavaScriptNumberReceiverType(receiverType)
                && methodCallExpression.arguments().size() <= 1
                && ("toString".equals(methodCallExpression.methodName())
                        || "toFixed".equals(methodCallExpression.methodName()));
    }

    private QinIrTypeRef javaNumberValueInstanceMethodReturnType(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (!isJavaScriptNumberReceiverType(receiverType) || !methodCallExpression.arguments().isEmpty()) {
            return null;
        }
        return switch (methodCallExpression.methodName()) {
            case "intValue", "shortValue", "byteValue" -> QinIrTypeRef.intType();
            case "longValue", "floatValue", "doubleValue" -> QinIrTypeRef.doubleType();
            default -> null;
        };
    }

    private QinIrTypeRef emitJavaNumberValueInstanceMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        switch (methodCallExpression.methodName()) {
            case "intValue", "shortValue", "byteValue" -> coerceValueForTargetType(code, receiverType, QinIrTypeRef.intType());
            case "longValue", "floatValue", "doubleValue" -> coerceValueForTargetType(code, receiverType, QinIrTypeRef.doubleType());
            default -> throw new IllegalArgumentException(
                    "Unsupported Java Number value method: " + methodCallExpression.methodName());
        }
        return returnType;
    }

    private boolean isJavaScriptNumberReceiverType(QinIrTypeRef receiverType) {
        if (receiverType == null) {
            return false;
        }
        if (receiverType.kind() == QinIrTypeKind.INT || receiverType.kind() == QinIrTypeKind.DOUBLE) {
            return true;
        }
        return receiverType.kind() == QinIrTypeKind.CLASS
                && ("java.lang.Number".equals(receiverType.binaryName())
                        || "java.lang.Integer".equals(receiverType.binaryName())
                        || "java.lang.Long".equals(receiverType.binaryName())
                        || "java.lang.Double".equals(receiverType.binaryName())
                        || "java.lang.Float".equals(receiverType.binaryName())
                        || "java.lang.Short".equals(receiverType.binaryName())
                        || "java.lang.Byte".equals(receiverType.binaryName()));
    }

    private boolean isDynamicObjectType(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.CLASS
                && ("java.lang.Object".equals(type.binaryName())
                        || ("java.util.Map".equals(type.binaryName()) && !isStructuralSlimeAstMapType(type))
                        || "java.util.LinkedHashMap".equals(type.binaryName()));
    }

    private boolean isStructuralSlimeAstMapType(QinIrTypeRef type) {
        return structuralSlimeAstTypeName(type) != null;
    }

    private String structuralSlimeAstTypeName(QinIrTypeRef type) {
        if (type == null
                || type.kind() != QinIrTypeKind.CLASS
                || !"java.util.Map".equals(type.binaryName())
                || type.typeArguments() == null
                || type.typeArguments().isEmpty()) {
            return null;
        }
        QinIrTypeRef marker = type.typeArguments().get(0);
        if (marker.binaryName() == null
                || !marker.binaryName().startsWith(STRUCTURAL_SLIME_AST_TYPE_PREFIX)) {
            return null;
        }
        return marker.binaryName().substring(STRUCTURAL_SLIME_AST_TYPE_PREFIX.length());
    }

    private QinIrTypeRef structuralSlimeAstType(String typeName) {
        return QinIrTypeRef.classType(
                "java.util.Map",
                List.of(QinIrTypeRef.classType(STRUCTURAL_SLIME_AST_TYPE_PREFIX + typeName)));
    }

    private QinIrTypeRef objectArrayType(QinIrTypeRef elementType) {
        if (elementType == null || isJavaLangObjectType(elementType)) {
            return QinIrTypeRef.classType("java.lang.Object[]");
        }
        return QinIrTypeRef.classType("java.lang.Object[]", List.of(elementType));
    }

    private QinIrTypeRef resolveStructuralSlimeAstPropertyType(QinIrTypeRef ownerType, String propertyName) {
        String typeName = structuralSlimeAstTypeName(ownerType);
        if (typeName == null || propertyName == null || propertyName.isBlank()) {
            return null;
        }
        if ("type".equals(propertyName)) {
            return QinIrTypeRef.stringType();
        }
        if ("loc".equals(propertyName)) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return switch (typeName) {
            case "SlimeProgram", "SlimeJavascriptProgram" -> switch (propertyName) {
                case "body" -> objectArrayType(structuralSlimeAstType("SlimeModuleDeclaration"));
                default -> null;
            };
            case "SlimeImportDeclaration", "SlimeJavascriptImportDeclaration" -> switch (propertyName) {
                case "source" -> structuralSlimeAstType("SlimeLiteral");
                case "specifiers" -> objectArrayType(structuralSlimeAstType("SlimeImportSpecifierItem"));
                default -> null;
            };
            case "SlimeImportSpecifierItem", "SlimeJavascriptImportSpecifierItem" -> switch (propertyName) {
                case "specifier" -> structuralSlimeAstType("SlimeModuleSpecifier");
                default -> null;
            };
            case "SlimeVariableDeclarator", "SlimeJavascriptVariableDeclarator" -> switch (propertyName) {
                case "init" -> structuralSlimeAstType("SlimeExpression");
                case "id" -> structuralSlimeAstType("SlimePattern");
                default -> null;
            };
            case "SlimeCallExpression", "SlimeSimpleCallExpression", "SlimeJavascriptCallExpression",
                    "SlimeJavascriptSimpleCallExpression" -> switch (propertyName) {
                case "callee" -> structuralSlimeAstType("SlimeExpression");
                case "arguments" -> objectArrayType(structuralSlimeAstType("SlimeCallArgument"));
                default -> null;
            };
            case "SlimeCallArgument", "SlimeJavascriptCallArgument" -> switch (propertyName) {
                case "argument" -> structuralSlimeAstType("SlimeExpression");
                default -> null;
            };
            case "SlimeMemberExpression", "SlimeJavascriptMemberExpression" -> switch (propertyName) {
                case "object", "property" -> structuralSlimeAstType("SlimeExpression");
                default -> null;
            };
            case "SlimeImportSpecifier", "SlimeJavascriptImportSpecifier" -> switch (propertyName) {
                case "imported", "local" -> structuralSlimeAstType("SlimeIdentifier");
                default -> null;
            };
            case "SlimeImportDefaultSpecifier", "SlimeJavascriptImportDefaultSpecifier",
                    "SlimeImportNamespaceSpecifier", "SlimeJavascriptImportNamespaceSpecifier" -> switch (propertyName) {
                case "local" -> structuralSlimeAstType("SlimeIdentifier");
                default -> null;
            };
            case "SlimeModuleSpecifier" -> switch (propertyName) {
                case "imported", "local", "exported" -> structuralSlimeAstType("SlimeIdentifier");
                default -> null;
            };
            case "SlimeIdentifier", "SlimeJavascriptIdentifier" -> switch (propertyName) {
                case "name" -> QinIrTypeRef.stringType();
                default -> null;
            };
            case "SlimeLiteral", "SlimeSimpleLiteral", "SlimeStringLiteral",
                    "SlimeJavascriptLiteral", "SlimeJavascriptSimpleLiteral", "SlimeJavascriptStringLiteral" ->
                    switch (propertyName) {
                        case "raw" -> QinIrTypeRef.stringType();
                        default -> null;
                    };
            default -> null;
        };
    }

    private boolean isJavaUtilMapType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        String binaryName = canonicalJavaSdkAliasBinaryName(type.binaryName());
        try {
            return java.util.Map.class.isAssignableFrom(resolveClass(binaryName));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private QinIrTypeRef javaMapMethodReturnType(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (!isJavaUtilMapType(receiverType)) {
            return null;
        }
        int argumentCount = methodCallExpression.arguments().size();
        return switch (methodCallExpression.methodName()) {
            case "get" -> argumentCount == 1 ? javaMapValueType(receiverType) : null;
            case "put", "getOrDefault", "computeIfAbsent" -> argumentCount == 2 ? javaMapValueType(receiverType) : null;
            case "containsKey" -> argumentCount == 1 ? QinIrTypeRef.booleanType() : null;
            case "clear" -> argumentCount == 0 ? QinIrTypeRef.voidType() : null;
            case "size" -> argumentCount == 0 ? QinIrTypeRef.intType() : null;
            case "isEmpty" -> argumentCount == 0 ? QinIrTypeRef.booleanType() : null;
            case "values" -> argumentCount == 0
                    ? QinIrTypeRef.classType("java.util.Collection", List.of(javaMapValueType(receiverType)))
                    : null;
            case "keys", "keySet" -> argumentCount == 0
                    ? QinIrTypeRef.classType("java.util.Set", List.of(javaMapKeyType(receiverType)))
                    : null;
            case "entries", "entrySet" -> argumentCount == 0
                    ? QinIrTypeRef.classType("java.util.Set", List.of(QinIrTypeRef.classType(
                            "java.util.Map$Entry",
                            List.of(javaMapKeyType(receiverType), javaMapValueType(receiverType)))))
                    : null;
            default -> null;
        };
    }

    private QinIrTypeRef inferJavaMapMethodReturnType(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef returnType = javaMapMethodReturnType(receiverType, methodCallExpression);
        if (returnType == null) {
            return null;
        }
        if ("getOrDefault".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2
                && isJavaLangObjectType(returnType)) {
            QinIrTypeRef defaultValueType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            if (defaultValueType != null && !isJavaLangObjectType(defaultValueType)) {
                return defaultValueType;
            }
        }
        return returnType;
    }

    private QinIrTypeRef javaMapEntryMethodReturnType(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (!isJavaMapEntryType(receiverType)) {
            return null;
        }
        int argumentCount = methodCallExpression.arguments().size();
        return switch (methodCallExpression.methodName()) {
            case "getKey" -> argumentCount == 0 ? javaMapEntryKeyType(receiverType) : null;
            case "getValue" -> argumentCount == 0 ? javaMapEntryValueType(receiverType) : null;
            case "setValue" -> argumentCount == 1 ? javaMapEntryValueType(receiverType) : null;
            default -> null;
        };
    }

    private boolean isJavaMapEntryType(QinIrTypeRef receiverType) {
        if (receiverType == null || receiverType.kind() != QinIrTypeKind.CLASS || receiverType.binaryName() == null) {
            return false;
        }
        if ("java.util.Map$Entry".equals(receiverType.binaryName())
                || "java.util.Map.Entry".equals(receiverType.binaryName())) {
            return true;
        }
        try {
            return java.util.Map.Entry.class.isAssignableFrom(resolveClass(receiverType.binaryName()));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private QinIrTypeRef javaMapEntryKeyType(QinIrTypeRef receiverType) {
        if (receiverType != null
                && receiverType.typeArguments() != null
                && !receiverType.typeArguments().isEmpty()) {
            return receiverType.typeArguments().get(0);
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef javaMapEntryValueType(QinIrTypeRef receiverType) {
        if (receiverType != null
                && receiverType.typeArguments() != null
                && receiverType.typeArguments().size() >= 2) {
            return boxForObjectStorage(receiverType.typeArguments().get(1));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef javaMapKeyType(QinIrTypeRef receiverType) {
        if (receiverType != null
                && receiverType.typeArguments() != null
                && !receiverType.typeArguments().isEmpty()) {
            return receiverType.typeArguments().get(0);
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef javaMapValueType(QinIrTypeRef receiverType) {
        if (receiverType != null
                && receiverType.typeArguments() != null
                && receiverType.typeArguments().size() >= 2) {
            return boxForObjectStorage(receiverType.typeArguments().get(1));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef emitJavaMapMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        code.checkcast(MAP_DESC);
        switch (methodCallExpression.methodName()) {
            case "get" -> {
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                code.invokeinterface(MAP_DESC, "get", MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC));
                coerceObjectResultForType(code, returnType);
                return returnType;
            }
            case "put" -> {
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(1));
                code.invokeinterface(MAP_DESC, "put", MAP_PUT_SIGNATURE);
                coerceObjectResultForType(code, returnType);
                return returnType;
            }
            case "getOrDefault" -> {
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(1));
                code.invokeinterface(
                        MAP_DESC,
                        "getOrDefault",
                        MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC));
                coerceObjectResultForType(code, returnType);
                return returnType;
            }
            case "computeIfAbsent" -> {
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                if (!emitExpressionAsJavaFunctionalInterfaceParameter(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(1),
                        QinIrTypeRef.classType("java.util.function.Function"),
                        java.util.function.Function.class)) {
                    throw new IllegalArgumentException(
                            "Map.computeIfAbsent requires a static Java functional argument: "
                                    + declarationExpressionSummary(methodCallExpression.arguments().get(1)));
                }
                code.invokeinterface(
                        MAP_DESC,
                        "computeIfAbsent",
                        MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, ClassDesc.of("java.util.function.Function")));
                coerceObjectResultForType(code, returnType);
                return returnType;
            }
            case "containsKey" -> {
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                code.invokeinterface(MAP_DESC, "containsKey", MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z"));
                return returnType;
            }
            case "clear" -> {
                code.invokeinterface(MAP_DESC, "clear", MethodTypeDesc.ofDescriptor("()V"));
                return returnType;
            }
            case "size" -> {
                code.invokeinterface(MAP_DESC, "size", MethodTypeDesc.ofDescriptor("()I"));
                return returnType;
            }
            case "isEmpty" -> {
                code.invokeinterface(MAP_DESC, "isEmpty", MethodTypeDesc.ofDescriptor("()Z"));
                return returnType;
            }
            case "values" -> {
                code.invokeinterface(MAP_DESC, "values", MethodTypeDesc.of(COLLECTION_DESC));
                return returnType;
            }
            case "keys", "keySet" -> {
                code.invokeinterface(MAP_DESC, "keySet", MethodTypeDesc.of(ClassDesc.of("java.util.Set")));
                return returnType;
            }
            case "entries", "entrySet" -> {
                code.invokeinterface(MAP_DESC, "entrySet", MethodTypeDesc.of(ClassDesc.of("java.util.Set")));
                return returnType;
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported java.util.Map method: "
                            + receiverType
                            + "."
                            + methodCallExpression.methodName());
        }
    }

    private QinIrTypeRef emitJavaMapEntryMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        code.checkcast(MAP_ENTRY_DESC);
        switch (methodCallExpression.methodName()) {
            case "getKey" -> {
                code.invokeinterface(MAP_ENTRY_DESC, "getKey", MethodTypeDesc.of(OBJECT_DESC));
                coerceObjectResultForType(code, returnType);
                return returnType;
            }
            case "getValue" -> {
                code.invokeinterface(MAP_ENTRY_DESC, "getValue", MethodTypeDesc.of(OBJECT_DESC));
                coerceObjectResultForType(code, returnType);
                return returnType;
            }
            case "setValue" -> {
                emitDeclarationExpressionAsObject(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
                code.invokeinterface(MAP_ENTRY_DESC, "setValue", MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC));
                coerceObjectResultForType(code, returnType);
                return returnType;
            }
            default -> throw new IllegalArgumentException(
                    "Unsupported java.util.Map.Entry method: "
                            + methodCallExpression.methodName() + "/" + methodCallExpression.arguments().size());
        }
    }

    private boolean canUseDynamicGeneratedInstanceMethod(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && isGeneratedBinaryName(type.binaryName());
    }

    private boolean isGeneratedBinaryName(String binaryName) {
        return binaryName != null
                && !binaryName.isBlank()
                && !binaryName.startsWith("[")
                && binaryName.contains("_")
                && !binaryName.startsWith("java.")
                && !binaryName.startsWith("javax.")
                && !binaryName.startsWith("jdk.")
                && !binaryName.startsWith("sun.");
    }

    private boolean isQinRuntimeMetadataProperty(String name) {
        return name != null && (name.startsWith("__qin") || name.startsWith("__qesm"));
    }

    private boolean isQinRuntimeGlobalName(String name) {
        return name != null && (name.startsWith("__Qin")
                || name.startsWith("__qin_")
                || name.startsWith("__qesm_")
                || "globalThis".equals(name)
                || "global".equals(name)
                || "window".equals(name)
                || "self".equals(name)
                || "console".equals(name)
                || "performance".equals(name)
                || "Buffer".equals(name)
                || "parseInt".equals(name)
                || "parseFloat".equals(name)
                || "isNaN".equals(name)
                || "isFinite".equals(name)
                || "Math".equals(name)
                || "JSON".equals(name)
                || "Number".equals(name)
                || "Object".equals(name)
                || "Array".equals(name)
                || "Map".equals(name)
                || "Set".equals(name)
                || "Proxy".equals(name)
                || "Promise".equals(name)
                || "Symbol".equals(name)
                || "WeakMap".equals(name)
                || "WeakSet".equals(name)
                || "Date".equals(name)
                || "String".equals(name)
                || "Boolean".equals(name)
                || "Uint8Array".equals(name)
                || "Uint16Array".equals(name)
                || "Uint32Array".equals(name)
                || "TextDecoder".equals(name)
                || "URLSearchParams".equals(name)
                || "RegExp".equals(name)
                || "Error".equals(name)
                || "TypeError".equals(name)
                || "RangeError".equals(name)
                || "ReferenceError".equals(name)
                || "SyntaxError".equals(name)
                || isUppercaseModuleConstantName(name)
                || name.startsWith("com_")
                || name.startsWith("java_")
                || name.startsWith("org_"));
    }

    private boolean isUppercaseModuleConstantName(String name) {
        if (name == null || name.isBlank() || !Character.isUpperCase(name.charAt(0))) {
            return false;
        }
        boolean hasUnderscore = false;
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (ch == '_') {
                hasUnderscore = true;
                continue;
            }
            if (!Character.isUpperCase(ch) && !Character.isDigit(ch)) {
                return false;
            }
        }
        return hasUnderscore;
    }

    private boolean canUseDynamicEnclosingStaticMethod(QinIrTypeRef receiverType) {
        if (receiverType == null
                || receiverType.kind() != QinIrTypeKind.CLASS
                || receiverType.binaryName() == null) {
            return false;
        }
        String enclosingBinaryName = enclosingBinaryName(receiverType.binaryName());
        return enclosingBinaryName != null && isQinRuntimeGlobalName(enclosingBinaryName);
    }

    private boolean isObjectArrayType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        String binaryName = type.binaryName();
        if ("java.lang.Object[]".equals(binaryName)
                || "[Ljava.lang.Object;".equals(binaryName)
                || "[Ljava/lang/Object;".equals(binaryName)
                || "java.lang.String[]".equals(binaryName)
                || "[Ljava.lang.String;".equals(binaryName)
                || "[Ljava/lang/String;".equals(binaryName)) {
            return true;
        }
        if (binaryName.endsWith("[]")) {
            String componentName = binaryName.substring(0, binaryName.length() - 2);
            return !isPrimitiveBinaryName(componentName);
        }
        return binaryName.startsWith("[L")
                || binaryName.startsWith("[[");
    }

    private boolean isPrimitiveArrayType(QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || type.binaryName() == null) {
            return false;
        }
        String binaryName = type.binaryName();
        if (binaryName.endsWith("[]")) {
            String componentName = binaryName.substring(0, binaryName.length() - 2);
            return isPrimitiveBinaryName(componentName);
        }
        return switch (binaryName) {
            case "[Z", "[I", "[D" -> true;
            default -> false;
        };
    }

    private boolean isPrimitiveBinaryName(String binaryName) {
        return switch (binaryName) {
            case "boolean", "int", "double", "long", "float", "short", "byte", "char", "void" -> true;
            default -> false;
        };
    }

    private QinIrTypeRef staticArrayElementType(QinIrTypeRef arrayType) {
        if (arrayType != null
                && isObjectArrayType(arrayType)
                && arrayType.typeArguments() != null
                && !arrayType.typeArguments().isEmpty()) {
            return arrayType.typeArguments().get(0);
        }
        return varargsElementType(arrayType);
    }

    private QinIrTypeRef staticArrayParameterElementType(QinIrTypeRef arrayType) {
        return isAnyArrayType(arrayType) ? staticArrayElementType(arrayType) : null;
    }

    private QinIrTypeRef staticArrayStorageElementType(
            QinIrTypeRef arrayType,
            QinIrTypeRef semanticElementType) {
        if (arrayType != null
                && arrayType.kind() == QinIrTypeKind.CLASS
                && isJavaLangObjectArrayBinaryName(arrayType.binaryName())) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return semanticElementType;
    }

    private boolean isObjectArrayCloneCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isObjectArrayType(receiverType)
                && "clone".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().isEmpty();
    }

    private boolean isObjectArraySliceCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isObjectArrayType(receiverType)
                && "slice".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2;
    }

    private boolean isObjectArraySortCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isObjectArrayType(receiverType)
                && "sort".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() <= 1;
    }

    private boolean isQinArrayFromCall(QinIrInstanceMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "from".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1
                && methodCallExpression.receiver() instanceof QinIrIdentifierReference identifierReference
                && "Array".equals(identifierReference.name());
    }

    private Integer staticArrayFromNullFactoryLength(QinIrInstanceMethodCallExpression methodCallExpression) {
        StaticArrayFromFactory factory = staticArrayFromFactory(methodCallExpression);
        if (factory == null || !(factory.valueExpression() instanceof QinIrNullLiteral)) {
            return null;
        }
        if (factory.lengthExpression() instanceof QinIrNumberLiteral numberLiteral) {
            double value = numberLiteral.value();
            if (value < 0 || value != Math.rint(value) || value > Integer.MAX_VALUE) {
                return null;
            }
            return (int) value;
        }
        return null;
    }

    private StaticArrayFromFactory staticArrayFromFactory(QinIrInstanceMethodCallExpression methodCallExpression) {
        if (methodCallExpression == null
                || !"from".equals(methodCallExpression.methodName())
                || methodCallExpression.arguments().size() != 2
                || !(methodCallExpression.receiver() instanceof QinIrIdentifierReference identifierReference)
                || !"Array".equals(identifierReference.name())) {
            return null;
        }
        QinIrExpression source = methodCallExpression.arguments().get(0);
        QinIrExpression factory = methodCallExpression.arguments().get(1);
        if (!(source instanceof QinIrObjectLiteral objectLiteral)
                || !(factory instanceof QinIrFunctionLiteral functionLiteral)
                || functionLiteral.returnExpression() == null
                || !functionLiteral.bodyStatements().isEmpty()) {
            return null;
        }
        QinIrExpression lengthExpression = staticLengthPropertyExpression(objectLiteral);
        return lengthExpression == null ? null : new StaticArrayFromFactory(lengthExpression, functionLiteral.returnExpression());
    }

    private QinIrTypeRef emitStaticArrayFromFactory(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            StaticArrayFromFactory factory) {
        QinIrTypeRef lengthType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                factory.lengthExpression());
        coerceValueForTargetType(code, lengthType, QinIrTypeRef.intType());
        code.anewarray(OBJECT_DESC);
        if (!(factory.valueExpression() instanceof QinIrNullLiteral)) {
            code.dup();
            QinIrTypeRef valueType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    factory.valueExpression());
            boxValueForObjectTarget(code, valueType);
            code.invokestatic(
                    ClassDesc.of("java.util.Arrays"),
                    "fill",
                    MethodTypeDesc.ofDescriptor("([Ljava/lang/Object;Ljava/lang/Object;)V"));
        }
        return QinIrTypeRef.classType("java.lang.Object[]");
    }

    private Integer staticLengthProperty(QinIrObjectLiteral objectLiteral) {
        QinIrExpression lengthExpression = staticLengthPropertyExpression(objectLiteral);
        if (!(lengthExpression instanceof QinIrNumberLiteral numberLiteral)) {
            return null;
        }
        double value = numberLiteral.value();
        if (value < 0 || value != Math.rint(value) || value > Integer.MAX_VALUE) {
            return null;
        }
        return (int) value;
    }

    private QinIrExpression staticLengthPropertyExpression(QinIrObjectLiteral objectLiteral) {
        if (objectLiteral.properties().size() != 1) {
            return null;
        }
        QinIrObjectProperty property = objectLiteral.properties().get(0);
        if (!"length".equals(property.key())) {
            return null;
        }
        return property.value();
    }

    private record StaticArrayFromFactory(QinIrExpression lengthExpression, QinIrExpression valueExpression) {
    }

    private boolean isJavaListDynamicArrayMethodCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isJavaListType(receiverType) && isSupportedDynamicArrayMethod(methodCallExpression.methodName());
    }

    private ResolvedInstanceMethodCall resolveJavaCollectionTypedToArrayMethodCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression,
            List<QinIrTypeRef> argumentTypes) {
        if (!"toArray".equals(methodCallExpression.methodName())
                || methodCallExpression.arguments().size() != 1
                || !isJavaCollectionType(receiverType)) {
            return null;
        }
        QinIrExpression argument = methodCallExpression.arguments().get(0);
        QinIrTypeRef arrayType = null;
        if (argument instanceof QinIrArrayCreationExpression arrayCreationExpression) {
            arrayType = arrayTypeRef(
                    arrayCreationExpression.componentType(),
                    arrayCreationExpression.dimensions().size() + arrayCreationExpression.trailingEmptyDimensions());
        } else if (argument instanceof QinIrArrayLiteral) {
            arrayType = QinIrTypeRef.classType("java.lang.Object[]");
        } else if (argument instanceof QinIrInstanceMethodCallExpression argumentMethodCall
                && staticArrayFromNullFactoryLength(argumentMethodCall) != null) {
            arrayType = QinIrTypeRef.classType("java.lang.Object[]");
        } else if (argumentTypes != null && argumentTypes.size() == 1 && isAnyArrayType(argumentTypes.get(0))) {
            arrayType = argumentTypes.get(0);
        }
        if (arrayType == null) {
            return null;
        }
        Class<?> ownerClass = resolveClass(receiverType.binaryName());
        try {
            Method reflectedMethod = java.util.Collection.class.getMethod("toArray", Object[].class);
            return new ResolvedInstanceMethodCall(
                    ownerClass.getName(),
                    "toArray",
                    List.of(toQinTypeRef(reflectedMethod.getParameterTypes()[0])),
                    arrayType,
                    ownerClass.isInterface(),
                    MethodTypeDesc.ofDescriptor("([Ljava/lang/Object;)[Ljava/lang/Object;"),
                    List.of(reflectedMethod.getParameterTypes()),
                    false);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing java.util.Collection.toArray(Object[])", exception);
        }
    }

    private boolean isJavaCollectionType(QinIrTypeRef type) {
        if (type == null
                || type.kind() != QinIrTypeKind.CLASS
                || type.binaryName() == null
                || type.binaryName().isBlank()) {
            return false;
        }
        try {
            return java.util.Collection.class.isAssignableFrom(resolveClass(type.binaryName()));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private boolean isJavaListType(QinIrTypeRef type) {
        if (type == null
                || type.kind() != QinIrTypeKind.CLASS
                || type.binaryName() == null
                || type.binaryName().isBlank()) {
            return false;
        }
        try {
            return java.util.List.class.isAssignableFrom(resolveClass(type.binaryName()));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private boolean isSupportedDynamicArrayMethod(String methodName) {
        return switch (methodName) {
            case "push", "pop", "unshift", "shift", "concat", "map", "forEach", "at", "filter", "fill",
                    "join", "slice", "splice", "includes", "indexOf", "find", "findIndex", "some", "every",
                    "reduce", "flat", "flatMap", "sort", "add", "addAll", "size", "isEmpty", "clear", "get",
                    "set", "remove", "subList", "toArray" -> true;
            default -> false;
        };
    }

    private QinIrTypeRef emitObjectArrayCloneCall(java.lang.classfile.CodeBuilder code) {
        code.dup();
        code.arraylength();
        code.invokestatic(
                ClassDesc.of("java.util.Arrays"),
                "copyOf",
                MethodTypeDesc.ofDescriptor("([Ljava/lang/Object;I)[Ljava/lang/Object;"));
        return QinIrTypeRef.classType("java.lang.Object[]");
    }

    private QinIrTypeRef emitObjectArraySliceCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        code.checkcast(OBJECT_ARRAY_DESC);
        QinIrTypeRef startType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(0));
        coerceValueForTargetType(code, startType, QinIrTypeRef.intType());
        QinIrTypeRef endType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().get(1));
        coerceValueForTargetType(code, endType, QinIrTypeRef.intType());
        code.invokestatic(
                ClassDesc.of("java.util.Arrays"),
                "copyOfRange",
                MethodTypeDesc.ofDescriptor("([Ljava/lang/Object;II)[Ljava/lang/Object;"));
        return QinIrTypeRef.classType("java.lang.Object[]");
    }

    private QinIrTypeRef emitObjectArraySortCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        emitDeclarationExpressionAsObject(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments().isEmpty()
                        ? new QinIrNullLiteral()
                        : methodCallExpression.arguments().get(0));
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_array_sort__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)[Ljava/lang/Object;"));
        return QinIrTypeRef.classType("java.lang.Object[]");
    }

    private Integer numericArrayPropertyIndex(String propertyName) {
        if (propertyName == null || propertyName.isBlank()) {
            return null;
        }
        try {
            double value = Double.parseDouble(propertyName);
            if (!Double.isFinite(value)
                    || value < 0
                    || value > Integer.MAX_VALUE
                    || value != Math.rint(value)) {
                return null;
            }
            return (int) value;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private boolean isAnyArrayType(QinIrTypeRef type) {
        return isObjectArrayType(type)
                || (type != null
                        && type.kind() == QinIrTypeKind.CLASS
                        && type.binaryName() != null
                        && (type.binaryName().startsWith("[") || type.binaryName().endsWith("[]")));
    }

    private QinIrTypeRef emitStaticMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrStaticMethodCallExpression methodCallExpression) {
        if (isJavaSecurityMessageDigestGetInstanceStaticFacadeCall(methodCallExpression)) {
            QinIrTypeRef algorithmType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            coerceValueForTargetType(code, algorithmType, QinIrTypeRef.stringType());
            code.invokestatic(
                    MESSAGE_DIGEST_DESC,
                    "getInstance",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)Ljava/security/MessageDigest;"));
            return QinIrTypeRef.classType("java.security.MessageDigest");
        }
        if (isJavaUtilHexFormatOfStaticFacadeCall(methodCallExpression)) {
            code.invokestatic(
                    HEX_FORMAT_DESC,
                    "of",
                    MethodTypeDesc.ofDescriptor("()Ljava/util/HexFormat;"));
            return QinIrTypeRef.classType("java.util.HexFormat");
        }
        if (isJavaLangIntegerGetIntegerStaticFacadeCall(methodCallExpression)) {
            QinIrTypeRef keyType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            coerceValueForTargetType(code, keyType, QinIrTypeRef.stringType());
            QinIrTypeRef defaultType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            coerceValueForTargetType(code, defaultType, QinIrTypeRef.intType());
            code.invokestatic(
                    ClassDesc.of("java.lang.Integer"),
                    "getInteger",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/String;I)Ljava/lang/Integer;"));
            code.invokevirtual(
                    ClassDesc.of("java.lang.Integer"),
                    "intValue",
                    MethodTypeDesc.ofDescriptor("()I"));
            return QinIrTypeRef.intType();
        }
        QinIrTypeRef javaLangNumberHelperType = inferJavaLangNumberStaticSdkHelperReturnType(methodCallExpression);
        if (javaLangNumberHelperType != null) {
            return emitJavaLangNumberStaticSdkHelperCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression,
                    javaLangNumberHelperType);
        }
        if (isJavaUtilArraysStreamStaticFacadeCall(methodCallExpression)) {
            QinIrTypeRef returnType = javaUtilArraysStreamReturnType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_stream__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/util/stream/Stream;"));
            return returnType;
        }
        if (isJavaUtilArraysToStringStaticFacadeCall(methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_to_string__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/String;"));
            return QinIrTypeRef.stringType();
        }
        if (isJavaUtilArraysAsListArrayStaticFacadeCall(methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_as_list_array__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/util/ArrayList;"));
            return QinIrTypeRef.classType("java.util.ArrayList");
        }
        if (isJavaUtilArraysCopyOfStaticFacadeCall(methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_copy_of__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)[Ljava/lang/Object;"));
            return QinIrTypeRef.classType("java.lang.Object[]");
        }
        if (isJavaUtilArraysFillStaticFacadeCall(methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_fill__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)V"));
            return QinIrTypeRef.voidType();
        }
        if (isJavaUtilArraysSortRangeStaticFacadeCall(methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(2));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_arrays_sort__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V"));
            return QinIrTypeRef.voidType();
        }
        if (isJavaUtilCollectionsAddAllStaticFacadeCall(methodCallExpression)) {
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_collections_add_all__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Z"));
            return QinIrTypeRef.booleanType();
        }
        if (isJavaUtilCollectionsNCopiesStaticFacadeCall(methodCallExpression)) {
            QinIrTypeRef countType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            coerceValueForTargetType(code, countType, QinIrTypeRef.intType());
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            code.invokestatic(
                    ClassDesc.of("java.util.Collections"),
                    "nCopies",
                    MethodTypeDesc.ofDescriptor("(ILjava/lang/Object;)Ljava/util/List;"));
            QinIrTypeRef valueType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(1));
            return QinIrTypeRef.classType("java.util.List", List.of(boxForObjectStorage(valueType)));
        }
        if (isJavaNioFileFilesExistsStaticFacadeCall(methodCallExpression)) {
            QinIrTypeRef pathType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            discardExpressionResult(code, pathType);
            code.iconst_0();
            return QinIrTypeRef.booleanType();
        }
        if (isJavaNioFileFilesCreateDirectoriesStaticFacadeCall(methodCallExpression)) {
            QinIrTypeRef pathType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            boxValueForObjectTarget(code, pathType);
            return QinIrTypeRef.classType("java.lang.Object");
        }
        String effectiveOwnerBinaryName = effectiveLocalReferenceBinaryName(
                methodCallExpression.ownerBinaryName(),
                declarationIndex);
        QinIrClassDeclaration generatedEnumReference = declarationIndex.get(effectiveOwnerBinaryName);
        if (generatedEnumReference != null
                && isGeneratedEnumLikeType(
                        QinIrTypeRef.classType(generatedEnumReference.binaryName()),
                        declarationIndex)) {
            if (isGeneratedEnumValueOfCall(methodCallExpression)) {
                emitClassObject(code, generatedEnumReference.binaryName());
                return emitGeneratedEnumValueOfCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        generatedEnumReference,
                        methodCallExpression.arguments());
            }
            if (isGeneratedEnumValuesCall(methodCallExpression)) {
                emitClassObject(code, generatedEnumReference.binaryName());
                return emitGeneratedEnumValuesCall(code);
            }
        }
        List<QinIrTypeRef> argumentTypes = inferDeclarationArgumentTypes(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments());
        ResolvedStaticMethodCall resolvedMethod = resolveStaticMethodCall(
                methodCallExpression.ownerBinaryName(),
                methodCallExpression.methodName(),
                argumentTypes,
                methodCallExpression.arguments(),
                declarationIndex);
        if (resolvedMethod == null) {
            throw new IllegalArgumentException(
                    "Unknown declaration static method: "
                            + methodCallExpression.ownerBinaryName() + "." + methodCallExpression.methodName()
                            + "; " + staticMethodResolutionDiagnostic(
                                    methodCallExpression.ownerBinaryName(),
                                    methodCallExpression.methodName(),
                                    argumentTypes,
                                    declarationIndex));
        }
        resolvedMethod = effectiveGeneratedLocalStaticMethodCall(
                methodCallExpression,
                resolvedMethod);

        emitStaticMethodArguments(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.arguments(),
                resolvedMethod);

        invokeStaticMethod(code, resolvedMethod);
        return resolvedMethod.returnType();
    }

    private boolean isJavaNioFileFilesExistsStaticFacadeCall(
            QinIrStaticMethodCallExpression methodCallExpression) {
        return isJavaNioFileFilesStaticFacadeCall(methodCallExpression, "exists", 1);
    }

    private boolean isJavaLangIntegerGetIntegerStaticFacadeCall(
            QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "getInteger".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 2
                && "java.lang.Integer".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()));
    }

    private boolean isJavaNioFileFilesCreateDirectoriesStaticFacadeCall(
            QinIrStaticMethodCallExpression methodCallExpression) {
        return isJavaNioFileFilesStaticFacadeCall(methodCallExpression, "createDirectories", 1);
    }

    private boolean isJavaNioFileFilesStaticFacadeCall(
            QinIrStaticMethodCallExpression methodCallExpression,
            String methodName,
            int argumentCount) {
        return "__QinJavaNioFileFiles".equals(methodCallExpression.classLocalName())
                && "java.nio.file.Files".equals(canonicalJavaSdkAliasBinaryName(methodCallExpression.ownerBinaryName()))
                && methodName.equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == argumentCount;
    }

    private ResolvedStaticMethodCall effectiveGeneratedLocalStaticMethodCall(
            QinIrStaticMethodCallExpression methodCallExpression,
            ResolvedStaticMethodCall resolvedMethod) {
        if (methodCallExpression == null || resolvedMethod == null) {
            return resolvedMethod;
        }
        String ownerBinaryName = resolvedMethod.ownerBinaryName();
        if (ownerBinaryName == null
                || ownerBinaryName.isBlank()
                || isQinHostRuntimeBinaryName(ownerBinaryName)
                || !ownerBinaryName.contains(".")
                || isJavaPlatformBinaryName(ownerBinaryName)) {
            return resolvedMethod;
        }
        String localOwnerBinaryName = flattenedBinaryAlias(ownerBinaryName);
        if (!isGeneratedLocalBinaryName(localOwnerBinaryName)) {
            return resolvedMethod;
        }
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (QinIrTypeRef parameterType : resolvedMethod.parameterTypes()) {
            parameterTypes.add(effectiveGeneratedLocalReflectedType(localOwnerBinaryName, parameterType));
        }
        return new ResolvedStaticMethodCall(
                localOwnerBinaryName,
                resolvedMethod.methodName(),
                List.copyOf(parameterTypes),
                effectiveGeneratedLocalReflectedType(localOwnerBinaryName, resolvedMethod.returnType()),
                resolvedMethod.ownerInterface(),
                null,
                List.of(),
                resolvedMethod.varargs());
    }

    private QinIrTypeRef effectiveGeneratedLocalReflectedType(String localOwnerBinaryName, QinIrTypeRef type) {
        if (type == null || type.kind() != QinIrTypeKind.CLASS || isJavaPlatformBinaryName(type.binaryName())) {
            return type;
        }
        String flattenedBinaryName = flattenedBinaryAlias(type.binaryName());
        if (isGeneratedLocalBinaryName(localOwnerBinaryName) && isGeneratedLocalBinaryName(flattenedBinaryName)) {
            return QinIrTypeRef.classType(flattenedBinaryName, type.typeArguments());
        }
        return type;
    }

    private void emitStaticMethodArguments(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            List<QinIrExpression> arguments,
            ResolvedStaticMethodCall resolvedMethod) {
        if (!resolvedMethod.varargs()) {
            for (int i = 0; i < arguments.size(); i++) {
                QinIrTypeRef targetType = resolvedMethod.parameterTypes().get(i);
                Class<?> reflectedTargetType = reflectedParameterType(resolvedMethod.reflectedParameterTypes(), i);
                if (emitExpressionAsJavaFunctionalInterfaceParameter(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        arguments.get(i),
                        targetType,
                        reflectedTargetType)) {
                    continue;
                }
                if (emitJavaLangStringCharAtForNumericTarget(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        arguments.get(i),
                        targetType)) {
                    continue;
                }
                if (emitExpressionAsStaticArrayParameter(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        arguments.get(i),
                        targetType)) {
                    continue;
                }
                if (emitExpressionAsJavaArrayParameter(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        arguments.get(i),
                        reflectedTargetType)) {
                    continue;
                }
                if (emitNullLiteralForReferenceParameter(
                        code,
                        arguments.get(i),
                        targetType,
                        reflectedTargetType)) {
                    continue;
                }
                QinIrTypeRef actualType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        arguments.get(i));
                try {
                    if (reflectedTargetType == null) {
                        coerceValueForTargetType(code, actualType, targetType);
                    } else {
                        coerceValueForJavaParameterType(code, actualType, reflectedTargetType);
                    }
                } catch (IllegalArgumentException exception) {
                    throw new IllegalArgumentException(
                            "Static method argument coercion failed for "
                                    + resolvedMethod.ownerBinaryName()
                                    + "."
                                    + resolvedMethod.methodName()
                                    + "/"
                                    + arguments.size()
                                    + " arg#"
                                    + i
                                    + " actual="
                                    + actualType
                                    + " target="
                                    + targetType
                                    + " expression="
                                    + arguments.get(i),
                            exception);
                }
            }
            for (int i = arguments.size(); i < resolvedMethod.parameterTypes().size(); i++) {
                emitDefaultArgumentValue(code, resolvedMethod.parameterTypes().get(i));
            }
            return;
        }

        int fixedParameterCount = resolvedMethod.parameterTypes().size() - 1;
        for (int i = 0; i < fixedParameterCount; i++) {
            QinIrTypeRef targetType = resolvedMethod.parameterTypes().get(i);
            Class<?> reflectedTargetType = reflectedParameterType(resolvedMethod.reflectedParameterTypes(), i);
            if (emitExpressionAsJavaFunctionalInterfaceParameter(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i),
                    targetType,
                    reflectedTargetType)) {
                continue;
            }
            if (emitJavaLangStringCharAtForNumericTarget(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i),
                    targetType)) {
                continue;
            }
            if (emitExpressionAsStaticArrayParameter(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i),
                    targetType)) {
                continue;
            }
            if (emitExpressionAsJavaArrayParameter(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i),
                        reflectedTargetType)) {
                continue;
            }
            if (emitNullLiteralForReferenceParameter(
                    code,
                    arguments.get(i),
                    targetType,
                    reflectedTargetType)) {
                continue;
            }
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i));
            try {
                if (reflectedTargetType == null) {
                    coerceValueForTargetType(code, actualType, targetType);
                } else {
                    coerceValueForJavaParameterType(code, actualType, reflectedTargetType);
                }
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        "Static varargs method argument coercion failed for "
                                + resolvedMethod.ownerBinaryName()
                                + "."
                                + resolvedMethod.methodName()
                                + "/"
                                + arguments.size()
                                + " arg#"
                                + i
                                + " actual="
                                + actualType
                                + " target="
                                + targetType
                                + " expression="
                                + arguments.get(i),
                        exception);
            }
        }
        int varargCount = arguments.size() - fixedParameterCount;
        if (varargCount == 1
                && arguments.get(fixedParameterCount) instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    spreadArgumentExpression.expression());
            QinIrTypeRef targetType = resolvedMethod.parameterTypes().get(fixedParameterCount);
            try {
                coerceValueForTargetType(code, actualType, targetType);
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        "Static varargs spread coercion failed for "
                                + resolvedMethod.ownerBinaryName()
                                + "."
                                + resolvedMethod.methodName()
                                + "/"
                                + arguments.size()
                                + " arg#"
                                + fixedParameterCount
                                + " actual="
                                + actualType
                                + " target="
                                + targetType
                                + " expression="
                                + spreadArgumentExpression.expression(),
                        exception);
            }
            return;
        }
        emitVarargsArrayFromExpressions(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                arguments,
                fixedParameterCount,
                varargCount,
                resolvedMethod.parameterTypes().get(fixedParameterCount),
                reflectedParameterType(resolvedMethod.reflectedParameterTypes(), fixedParameterCount));
    }

    private boolean emitJavaLangStringCharAtForNumericTarget(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression expression,
            QinIrTypeRef targetType) {
        if (targetType == null
                || (targetType.kind() != QinIrTypeKind.INT && targetType.kind() != QinIrTypeKind.DOUBLE)
                || !isJavaLangStringCharAtCall(expression)) {
            return false;
        }
        List<QinIrExpression> arguments = expression instanceof QinIrInstanceMethodCallExpression methodCallExpression
                ? methodCallExpression.arguments()
                : ((QinIrBuiltinCallExpression) expression).arguments();
        QinIrTypeRef textType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                arguments.get(0));
        coerceValueForTargetType(code, textType, QinIrTypeRef.stringType());
        QinIrTypeRef indexType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                arguments.get(1));
        coerceValueForTargetType(code, indexType, QinIrTypeRef.intType());
        code.invokevirtual(STRING_DESC, "charAt", MethodTypeDesc.ofDescriptor("(I)C"));
        if (targetType.kind() == QinIrTypeKind.DOUBLE) {
            code.i2d();
        }
        return true;
    }

    private boolean isJavaLangStringCharAtCall(QinIrExpression expression) {
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            return "charAt".equals(methodCallExpression.methodName())
                    && methodCallExpression.arguments().size() == 2
                    && methodCallExpression.receiver() instanceof QinIrIdentifierReference receiver
                    && "__QinJavaLangString".equals(receiver.name());
        }
        return expression instanceof QinIrBuiltinCallExpression builtinCallExpression
                && "__QinJavaLangString".equals(builtinCallExpression.receiverName())
                && "charAt".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2;
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
        String requestedOwnerBinaryName = javaNewExpression.ownerBinaryName();
        String ownerBinaryName = resolveIndexedDeclaration(declarationIndex, requestedOwnerBinaryName) != null
                ? requestedOwnerBinaryName
                : isGeneratedSourceClassBinaryName(requestedOwnerBinaryName)
                ? requestedOwnerBinaryName
                : canonicalJavaSdkAliasBinaryName(requestedOwnerBinaryName);
        if (isJavaSdkHashMapAccessOrderConstructor(
                requestedOwnerBinaryName,
                ownerBinaryName,
                argumentTypes)) {
            return emitJavaSdkHashMapAccessOrderConstructor(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    javaNewExpression);
        }
        if (isJavaNioPathStringFactoryConstructor(ownerBinaryName, argumentTypes)) {
            return emitJavaNioPathStringFactoryConstructor(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    javaNewExpression);
        }
        if (isJavaSdkArrayListObjectArrayConstructor(ownerBinaryName, argumentTypes)) {
            return emitJavaSdkArrayListObjectArrayConstructor(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    javaNewExpression);
        }
        ResolvedConstructorCall resolvedConstructor;
        try {
            resolvedConstructor = resolveConstructorCall(
                    ownerBinaryName,
                    declarationIndex,
                    argumentTypes);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException(
                    error.getMessage()
                            + "; requestedOwner=" + requestedOwnerBinaryName
                            + "; canonicalOwner=" + ownerBinaryName
                            + "; argumentTypes=" + argumentTypes,
                    error);
        }

        ClassDesc ownerDesc = ClassDesc.of(resolvedConstructor.ownerBinaryName());
        code.new_(ownerDesc);
        code.dup();
        emitConstructorArguments(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                javaNewExpression.arguments(),
                resolvedConstructor);
        code.invokespecial(ownerDesc, "<init>", resolvedConstructor.descriptor());
        return QinIrTypeRef.classType(resolvedConstructor.ownerBinaryName());
    }

    private boolean isJavaNioPathStringFactoryConstructor(
            String ownerBinaryName,
            List<QinIrTypeRef> argumentTypes) {
        return "java.nio.file.Path".equals(ownerBinaryName)
                && argumentTypes.size() == 1
                && argumentTypes.get(0) != null
                && argumentTypes.get(0).kind() != QinIrTypeKind.VOID;
    }

    private QinIrTypeRef emitJavaNioPathStringFactoryConstructor(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrJavaNewExpression javaNewExpression) {
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                javaNewExpression.arguments().get(0));
        coerceValueForTargetType(code, actualType, QinIrTypeRef.stringType());
        code.loadConstant(0);
        code.anewarray(STRING_DESC);
        code.invokestatic(
                ClassDesc.of("java.nio.file.Path"),
                "of",
                MethodTypeDesc.of(
                        ClassDesc.of("java.nio.file.Path"),
                        STRING_DESC,
                        ClassDesc.ofDescriptor("[Ljava/lang/String;")),
                true);
        return QinIrTypeRef.classType("java.nio.file.Path");
    }

    private boolean isJavaSdkArrayListObjectArrayConstructor(
            String ownerBinaryName,
            List<QinIrTypeRef> argumentTypes) {
        return "java.util.ArrayList".equals(ownerBinaryName)
                && argumentTypes.size() == 1
                && isObjectArrayType(argumentTypes.get(0));
    }

    private QinIrTypeRef emitJavaSdkArrayListObjectArrayConstructor(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrJavaNewExpression javaNewExpression) {
        ClassDesc arrayListDesc = ClassDesc.of("java.util.ArrayList");
        code.new_(arrayListDesc);
        code.dup();
        code.invokespecial(arrayListDesc, "<init>", VOID_INIT);
        code.dup();
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                javaNewExpression.arguments().get(0));
        coerceValueForTargetType(code, actualType, QinIrTypeRef.classType("java.lang.Object[]"));
        code.invokestatic(
                ClassDesc.of("java.util.Arrays"),
                "asList",
                MethodTypeDesc.ofDescriptor("([Ljava/lang/Object;)Ljava/util/List;"));
        code.invokevirtual(
                arrayListDesc,
                "addAll",
                MethodTypeDesc.ofDescriptor("(Ljava/util/Collection;)Z"));
        code.pop();
        return QinIrTypeRef.classType("java.util.ArrayList");
    }

    private boolean isJavaSdkHashMapAccessOrderConstructor(
            String requestedOwnerBinaryName,
            String canonicalOwnerBinaryName,
            List<QinIrTypeRef> argumentTypes) {
        return ("__QinJavaUtilHashMap".equals(requestedOwnerBinaryName)
                        || "java.util.HashMap".equals(requestedOwnerBinaryName)
                        || "java.util.HashMap".equals(canonicalOwnerBinaryName))
                && argumentTypes.size() == 3
                && isNumericLike(argumentTypes.get(0))
                && isNumericLike(argumentTypes.get(1))
                && argumentTypes.get(2).kind() == QinIrTypeKind.BOOLEAN;
    }

    private QinIrTypeRef emitJavaSdkHashMapAccessOrderConstructor(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrJavaNewExpression javaNewExpression) {
        ClassDesc ownerDesc = ClassDesc.of("java.util.LinkedHashMap");
        code.new_(ownerDesc);
        code.dup();
        QinIrTypeRef capacityType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                javaNewExpression.arguments().get(0));
        coerceValueForTargetType(code, capacityType, QinIrTypeRef.intType());
        QinIrTypeRef loadFactorType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                javaNewExpression.arguments().get(1));
        coerceNumericValueForFloatTarget(code, loadFactorType);
        QinIrTypeRef accessOrderType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                javaNewExpression.arguments().get(2));
        coerceValueForTargetType(code, accessOrderType, QinIrTypeRef.booleanType());
        code.invokespecial(
                ownerDesc,
                "<init>",
                MethodTypeDesc.ofDescriptor("(IFZ)V"));
        return QinIrTypeRef.classType("java.util.LinkedHashMap");
    }

    private void coerceNumericValueForFloatTarget(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef actualType) {
        if (actualType.kind() == QinIrTypeKind.DOUBLE) {
            code.d2f();
            return;
        }
        if (actualType.kind() == QinIrTypeKind.INT) {
            code.i2f();
            return;
        }
        if (actualType.kind() == QinIrTypeKind.CLASS) {
            code.checkcast(NUMBER_DESC);
            code.invokevirtual(NUMBER_DESC, "floatValue", MethodTypeDesc.ofDescriptor("()F"));
            return;
        }
        throw new IllegalArgumentException("Unsupported float constructor argument type: " + actualType.kind());
    }

    private void emitConstructorArguments(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            List<QinIrExpression> arguments,
            ResolvedConstructorCall resolvedConstructor) {
        if (!resolvedConstructor.varargs()) {
            for (int i = 0; i < arguments.size(); i++) {
                Class<?> reflectedTargetType = reflectedParameterType(resolvedConstructor.reflectedParameterTypes(), i);
                if (emitExpressionAsJavaFunctionalInterfaceParameter(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        arguments.get(i),
                        resolvedConstructor.parameterTypes().get(i),
                        reflectedTargetType)) {
                    continue;
                }
                if (emitExpressionAsJavaArrayParameter(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        arguments.get(i),
                        reflectedTargetType)) {
                    continue;
                }
                if (emitNullLiteralForReferenceParameter(
                        code,
                        arguments.get(i),
                        resolvedConstructor.parameterTypes().get(i),
                        reflectedTargetType)) {
                    continue;
                }
                QinIrTypeRef actualType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        arguments.get(i));
                if (resolvedConstructor.reflectedParameterTypes().isEmpty()) {
                    coerceValueForTargetType(code, actualType, resolvedConstructor.parameterTypes().get(i));
                } else {
                    coerceValueForJavaParameterType(
                            code,
                            actualType,
                            reflectedTargetType);
                }
            }
            for (int i = arguments.size(); i < resolvedConstructor.parameterTypes().size(); i++) {
                emitDefaultArgumentValue(code, resolvedConstructor.parameterTypes().get(i));
            }
            return;
        }

        int fixedParameterCount = resolvedConstructor.parameterTypes().size() - 1;
        for (int i = 0; i < fixedParameterCount; i++) {
            Class<?> reflectedTargetType = reflectedParameterType(resolvedConstructor.reflectedParameterTypes(), i);
            if (emitExpressionAsJavaFunctionalInterfaceParameter(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i),
                    resolvedConstructor.parameterTypes().get(i),
                    reflectedTargetType)) {
                continue;
            }
            if (emitExpressionAsJavaArrayParameter(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i),
                        reflectedTargetType)) {
                continue;
            }
            if (emitNullLiteralForReferenceParameter(
                    code,
                    arguments.get(i),
                    resolvedConstructor.parameterTypes().get(i),
                    reflectedTargetType)) {
                continue;
            }
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arguments.get(i));
            coerceValueForTargetType(code, actualType, resolvedConstructor.parameterTypes().get(i));
        }

        int varargCount = arguments.size() - fixedParameterCount;
        if (varargCount == 1
                && arguments.get(fixedParameterCount) instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    spreadArgumentExpression.expression());
            if (resolvedConstructor.reflectedParameterTypes().isEmpty()) {
                coerceValueForTargetType(code, actualType, resolvedConstructor.parameterTypes().get(fixedParameterCount));
            } else {
                coerceValueForJavaParameterType(
                        code,
                        actualType,
                        resolvedConstructor.reflectedParameterTypes().get(fixedParameterCount));
            }
            return;
        }
        emitVarargsArrayFromExpressions(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                arguments,
                fixedParameterCount,
                varargCount,
                resolvedConstructor.parameterTypes().get(fixedParameterCount),
                reflectedParameterType(resolvedConstructor.reflectedParameterTypes(), fixedParameterCount));
    }

    private void emitDefaultArgumentValue(java.lang.classfile.CodeBuilder code, QinIrTypeRef type) {
        if (type == null) {
            code.aconst_null();
            return;
        }
        switch (type.kind()) {
            case BOOLEAN, INT -> code.iconst_0();
            case DOUBLE -> code.dconst_0();
            case STRING, CLASS, VOID -> code.aconst_null();
        }
    }

    private QinIrTypeRef emitBuiltinCallExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        String functionDefinitionHelper = functionDefinitionHelperName(builtinCallExpression);
        if (functionDefinitionHelper != null) {
            QinIrObjectLiteral functionDefinition =
                    (QinIrObjectLiteral) builtinCallExpression.arguments().get(0);
            code.invokestatic(
                    ClassDesc.of(activeBinaryClassName),
                    functionDefinitionHelper,
                    MethodTypeDesc.of(OBJECT_DESC));
            if (method != null && !method.staticMethod()) {
                code.aload(0);
                code.invokestatic(
                        ESM_GLOBAL_DESC,
                        "__qin_make_function_with_lexical_this__",
                        MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
            } else {
                code.invokestatic(
                        ESM_GLOBAL_DESC,
                        "__qin_make_function__",
                        MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"));
            }
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_binary__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            QinIrTypeRef optimizedType = emitOptimizedGlobalBinaryBuiltinCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
            if (optimizedType != null) {
                return optimizedType;
            }
            return emitGlobalBuiltinObjectCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression,
                    "__qin_binary__",
                    binaryBuiltinFallbackDiagnostic(
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            builtinCallExpression));
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_logical__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            QinIrTypeRef resultType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
            if (resultType.kind() == QinIrTypeKind.BOOLEAN) {
                return emitBooleanLogicalBuiltinCall(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        builtinCallExpression);
            }
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
            return emitConditionalBuiltinCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_init_enum_value".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            return emitStaticInitEnumValueCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_java_class_info__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() <= 2) {
            return emitStaticJavaClassInfoCall(
                    code,
                    ownerDeclaration,
                    declarationIndex,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_array_from_constant__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            return emitArrayFromConstantBuiltin(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_instanceof__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            return emitStaticInstanceofCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_structural_object__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            return emitStaticStructuralObjectCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_java_implements".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            return emitStaticJavaImplementsCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_string__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            return emitGlobalStringCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_number__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() <= 1) {
            return emitGlobalNumberCall(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_collection_get__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            return emitCollectionGetBuiltin(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_module_ref_get__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            return emitModuleRefGetBuiltin(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        QinIrTypeRef javaLangParseRuntimeCall = emitJavaLangParseRuntimeCall(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression);
        if (javaLangParseRuntimeCall != null) {
            return javaLangParseRuntimeCall;
        }
        if ("Global".equals(builtinCallExpression.receiverName())
                && "__qin_make_function__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1
                && builtinCallExpression.arguments().get(0) instanceof QinIrObjectLiteral functionDefinition
                && method != null
                && !method.staticMethod()) {
            QinIrTypeRef definitionType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    functionDefinition);
            boxValueForObjectTarget(code, definitionType);
            code.aload(0);
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_make_function_with_lexical_this__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
            return QinIrTypeRef.classType("java.lang.Object");
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

    private QinIrTypeRef emitModuleRefGetBuiltin(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        QinIrTypeRef nameType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(0));
        boxValueForObjectTarget(code, nameType);
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_module_ref_get__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"));
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef emitCollectionGetBuiltin(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(0));
        boxValueForObjectTarget(code, receiverType);
        emitDeclarationExpressionAsObject(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(1));
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_collection_get__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        QinIrTypeRef elementType = staticCollectionElementType(receiverType);
        coerceObjectResultForType(code, elementType);
        return elementType;
    }

    private QinIrTypeRef emitArrayFromConstantBuiltin(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        QinIrTypeRef lengthType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(0));
        coerceValueForTargetType(code, lengthType, QinIrTypeRef.intType());
        code.anewarray(OBJECT_DESC);
        QinIrExpression valueExpression = builtinCallExpression.arguments().get(1);
        if (!(valueExpression instanceof QinIrNullLiteral)) {
            code.dup();
            QinIrTypeRef valueType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    valueExpression);
            boxValueForObjectTarget(code, valueType);
            code.invokestatic(
                    ClassDesc.of("java.util.Arrays"),
                    "fill",
                    MethodTypeDesc.ofDescriptor("([Ljava/lang/Object;Ljava/lang/Object;)V"));
        }
        return QinIrTypeRef.classType("java.lang.Object[]");
    }

    private QinIrTypeRef emitStaticStructuralObjectCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        QinIrTypeRef valueType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(0));
        boxValueForObjectTarget(code, valueType);
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_structural_object__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z"));
        return QinIrTypeRef.booleanType();
    }

    private String functionDefinitionHelperName(QinIrBuiltinCallExpression builtinCallExpression) {
        if (activeBinaryClassName == null
                || !"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_make_function__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() != 1
                || !(builtinCallExpression.arguments().get(0) instanceof QinIrObjectLiteral functionDefinition)) {
            return null;
        }
        if (hasNonEmptyFunctionClosure(functionDefinition)) {
            return null;
        }
        return activeFunctionDefinitionHelpers.get(functionDefinition);
    }

    private boolean hasNonEmptyFunctionClosure(QinIrObjectLiteral functionDefinition) {
        for (QinIrObjectProperty property : functionDefinition.properties()) {
            if ("closure".equals(property.key())
                    && property.value() instanceof QinIrObjectLiteral closureObject
                    && !closureObject.properties().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private String functionClosureKeys(QinIrObjectLiteral functionDefinition) {
        for (QinIrObjectProperty property : functionDefinition.properties()) {
            if ("closure".equals(property.key())
                    && property.value() instanceof QinIrObjectLiteral closureObject) {
                return closureObject.properties().stream()
                        .map(QinIrObjectProperty::key)
                        .toList()
                        .toString();
            }
        }
        return "[]";
    }

    private boolean isJavaIoByteArrayOutputStreamStaticIoHelper(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method) {
        return ownerDeclaration != null
                && method != null
                && "__QinJavaIoByteArrayOutputStream".equals(ownerDeclaration.binaryName())
                && ("__qin_bytes".equals(method.name())
                        || "__qin_concat".equals(method.name())
                        || "__qin_concat_bytes".equals(method.name())
                        || "__qin_count".equals(method.name()));
    }

    private boolean isJavaListPushCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return receiverType != null
                && receiverType.kind() == QinIrTypeKind.CLASS
                && "java.util.ArrayList".equals(receiverType.binaryName())
                && "push".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1;
    }

    private boolean isJavaListJoinCall(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return isJavaListType(receiverType)
                && "join".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() <= 1;
    }

    private QinIrTypeRef emitJavaListJoinCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        QinIrTypeRef receiverActualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                methodCallExpression.receiver());
        boxValueForObjectTarget(code, receiverActualType);
        if (methodCallExpression.arguments().isEmpty()) {
            code.ldc(",");
        } else {
            QinIrTypeRef separatorType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments().get(0));
            boxValueForObjectTarget(code, separatorType);
        }
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_array_join__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/String;"));
        return QinIrTypeRef.stringType();
    }

    private boolean isArrowLexicalThisFunctionDefinition(QinIrObjectLiteral functionDefinition) {
        for (QinIrObjectProperty property : functionDefinition.properties()) {
            if ("__qin_arrow_lexical_this".equals(property.key())
                    && property.value() instanceof QinIrBooleanLiteral literal) {
                return literal.value();
            }
            if ("ast".equals(property.key()) && isEncodedAstType(property.value(), "ArrowFunctionExpression")) {
                return true;
            }
        }
        return false;
    }

    private boolean isEncodedAstType(QinIrExpression expression, String expectedType) {
        if (!(expression instanceof QinIrObjectLiteral objectLiteral)) {
            return false;
        }
        for (QinIrObjectProperty property : objectLiteral.properties()) {
            if ("type".equals(property.key())
                    && property.value() instanceof QinIrStringLiteral literal
                    && expectedType.equals(literal.value())) {
                return true;
            }
        }
        return false;
    }

    private QinIrTypeRef emitOptimizedGlobalBinaryBuiltinCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        if (!(builtinCallExpression.arguments().get(0) instanceof QinIrStringLiteral operatorLiteral)) {
            return null;
        }
        String operator = operatorLiteral.value();
        QinIrExpression left = builtinCallExpression.arguments().get(1);
        QinIrExpression right = builtinCallExpression.arguments().get(2);
        QinIrTypeRef leftType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                left);
        QinIrTypeRef rightType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                right);
        if (isNullEqualityOperator(operator)
                && (isNullOrUndefinedLiteral(left) || isNullOrUndefinedLiteral(right))) {
            boolean leftIsNullish = isNullOrUndefinedLiteral(left);
            QinIrExpression value = leftIsNullish ? right : left;
            QinIrTypeRef valueType = leftIsNullish ? rightType : leftType;
            if (!isReferenceLike(valueType)) {
                return null;
            }
            emitDeclarationExpression(code, ownerDeclaration, method, declarationIndex, localFrame, value);
            java.lang.classfile.Label trueLabel = code.newLabel();
            java.lang.classfile.Label doneLabel = code.newLabel();
            if ("==".equals(operator) || "===".equals(operator)) {
                code.ifnull(trueLabel);
            } else {
                code.ifnonnull(trueLabel);
            }
            code.iconst_0();
            code.goto_(doneLabel);
            code.labelBinding(trueLabel);
            code.iconst_1();
            code.labelBinding(doneLabel);
            return QinIrTypeRef.booleanType();
        }
        if (isStringEqualityOperator(operator)
                && isBooleanLike(leftType)
                && isBooleanLike(rightType)) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            coerceValueForTargetType(code, actualLeftType, QinIrTypeRef.booleanType());
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            coerceValueForTargetType(code, actualRightType, QinIrTypeRef.booleanType());
            java.lang.classfile.Label trueLabel = code.newLabel();
            java.lang.classfile.Label doneLabel = code.newLabel();
            if ("==".equals(operator) || "===".equals(operator)) {
                code.if_icmpeq(trueLabel);
            } else {
                code.if_icmpne(trueLabel);
            }
            code.iconst_0();
            code.goto_(doneLabel);
            code.labelBinding(trueLabel);
            code.iconst_1();
            code.labelBinding(doneLabel);
            return QinIrTypeRef.booleanType();
        }
        if (isBooleanBitwiseOperator(operator)
                && isBooleanLike(leftType)
                && isBooleanLike(rightType)) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            coerceValueForTargetType(code, actualLeftType, QinIrTypeRef.booleanType());
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            coerceValueForTargetType(code, actualRightType, QinIrTypeRef.booleanType());
            switch (operator) {
                case "|" -> code.ior();
                case "&" -> code.iand();
                case "^" -> code.ixor();
                default -> throw new IllegalArgumentException("Unsupported optimized boolean bitwise operator: " + operator);
            }
            return QinIrTypeRef.booleanType();
        }
        if (isStringEqualityOperator(operator)
                && ((isStringLike(leftType) && isStringLike(rightType))
                        || isJavaLangThrowableToStringStringComparison(ownerDeclaration, method, left, right))) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            boxValueForObjectTarget(code, actualLeftType);
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            boxValueForObjectTarget(code, actualRightType);
            code.invokestatic(
                    ClassDesc.of("java.util.Objects"),
                    "equals",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Z"));
            if ("!=".equals(operator) || "!==".equals(operator)) {
                java.lang.classfile.Label trueLabel = code.newLabel();
                java.lang.classfile.Label doneLabel = code.newLabel();
                code.ifeq(trueLabel);
                code.iconst_0();
                code.goto_(doneLabel);
                code.labelBinding(trueLabel);
                code.iconst_1();
                code.labelBinding(doneLabel);
            }
            return QinIrTypeRef.booleanType();
        }
        if (isReferenceEqualityOperator(operator)
                && isReferenceLike(leftType)
                && isReferenceLike(rightType)
                && !isStringLike(leftType)
                && !isStringLike(rightType)) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            boxValueForObjectTarget(code, actualLeftType);
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            boxValueForObjectTarget(code, actualRightType);
            java.lang.classfile.Label trueLabel = code.newLabel();
            java.lang.classfile.Label doneLabel = code.newLabel();
            if ("==".equals(operator) || "===".equals(operator)) {
                code.if_acmpeq(trueLabel);
            } else {
                code.if_acmpne(trueLabel);
            }
            code.iconst_0();
            code.goto_(doneLabel);
            code.labelBinding(trueLabel);
            code.iconst_1();
            code.labelBinding(doneLabel);
            return QinIrTypeRef.booleanType();
        }
        if ((isStringEqualityOperator(operator) || isNumericComparisonOperator(operator))
                && isNumericLike(leftType)
                && right instanceof QinIrStringLiteral rightLiteral
                && rightLiteral.value().length() == 1) {
            return emitNumericCharStringLiteralComparison(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    operator,
                    left,
                    leftType,
                    rightLiteral.value().charAt(0));
        }
        if ((isStringEqualityOperator(operator) || isNumericComparisonOperator(operator))
                && isNumericLike(rightType)
                && left instanceof QinIrStringLiteral leftLiteral
                && leftLiteral.value().length() == 1) {
            return emitNumericCharStringLiteralComparison(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    invertedComparisonOperator(operator),
                    right,
                    rightType,
                    leftLiteral.value().charAt(0));
        }
        if ("+".equals(operator) && (isStringLike(leftType) || isStringLike(rightType))) {
            emitStaticStringOperand(code, ownerDeclaration, method, declarationIndex, localFrame, left);
            emitStaticStringOperand(code, ownerDeclaration, method, declarationIndex, localFrame, right);
            code.invokevirtual(STRING_DESC, "concat", MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)Ljava/lang/String;"));
            return QinIrTypeRef.stringType();
        }
        if (isJavaLangIntegralSumRuntimeMethod(ownerDeclaration, method, operator)) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            coerceValueForTargetType(code, actualLeftType, QinIrTypeRef.doubleType());
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            coerceValueForTargetType(code, actualRightType, QinIrTypeRef.doubleType());
            code.dadd();
            return QinIrTypeRef.doubleType();
        }
        if (isJavaLangNumberCompareRuntimeMethod(ownerDeclaration, method, operator)) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            coerceValueForTargetType(code, actualLeftType, QinIrTypeRef.doubleType());
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            coerceValueForTargetType(code, actualRightType, QinIrTypeRef.doubleType());
            code.dcmpg();
            java.lang.classfile.Label trueLabel = code.newLabel();
            java.lang.classfile.Label doneLabel = code.newLabel();
            switch (operator) {
                case "==", "===" -> code.ifeq(trueLabel);
                case "!=", "!==" -> code.ifne(trueLabel);
                case "<" -> code.iflt(trueLabel);
                case "<=" -> code.ifle(trueLabel);
                case ">" -> code.ifgt(trueLabel);
                case ">=" -> code.ifge(trueLabel);
                default -> throw new IllegalArgumentException("Unsupported Java number compare operator: " + operator);
            }
            code.iconst_0();
            code.goto_(doneLabel);
            code.labelBinding(trueLabel);
            code.iconst_1();
            code.labelBinding(doneLabel);
            return QinIrTypeRef.booleanType();
        }
        if (!isNumericLike(leftType) || !isNumericLike(rightType)) {
            return null;
        }
        if (isNumericArithmeticOperator(operator)) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            coerceValueForTargetType(code, actualLeftType, QinIrTypeRef.doubleType());
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            coerceValueForTargetType(code, actualRightType, QinIrTypeRef.doubleType());
            switch (operator) {
                case "+" -> code.dadd();
                case "-" -> code.dsub();
                case "*" -> code.dmul();
                case "/" -> code.ddiv();
                case "%" -> code.drem();
                default -> throw new IllegalArgumentException("Unsupported optimized numeric operator: " + operator);
            }
            return QinIrTypeRef.doubleType();
        }
        if (isNumericBitwiseOperator(operator)) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            coerceValueForTargetType(code, actualLeftType, QinIrTypeRef.intType());
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            coerceValueForTargetType(code, actualRightType, QinIrTypeRef.intType());
            switch (operator) {
                case "|" -> code.ior();
                case "&" -> code.iand();
                case "^" -> code.ixor();
                case "<<" -> code.ishl();
                case ">>" -> code.ishr();
                case ">>>" -> code.iushr();
                default -> throw new IllegalArgumentException("Unsupported optimized bitwise operator: " + operator);
            }
            code.i2d();
            return QinIrTypeRef.doubleType();
        }
        if (isNumericComparisonOperator(operator)) {
            QinIrTypeRef actualLeftType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            coerceValueForTargetType(code, actualLeftType, QinIrTypeRef.doubleType());
            QinIrTypeRef actualRightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            coerceValueForTargetType(code, actualRightType, QinIrTypeRef.doubleType());
            code.dcmpg();
            java.lang.classfile.Label trueLabel = code.newLabel();
            java.lang.classfile.Label doneLabel = code.newLabel();
            switch (operator) {
                case "==", "===" -> code.ifeq(trueLabel);
                case "!=", "!==" -> code.ifne(trueLabel);
                case "<" -> code.iflt(trueLabel);
                case "<=" -> code.ifle(trueLabel);
                case ">" -> code.ifgt(trueLabel);
                case ">=" -> code.ifge(trueLabel);
                default -> throw new IllegalArgumentException("Unsupported optimized comparison operator: " + operator);
            }
            code.iconst_0();
            code.goto_(doneLabel);
            code.labelBinding(trueLabel);
            code.iconst_1();
            code.labelBinding(doneLabel);
            return QinIrTypeRef.booleanType();
        }
        return null;
    }

    private QinIrTypeRef emitNumericCharStringLiteralComparison(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            String operator,
            QinIrExpression numericExpression,
            QinIrTypeRef numericType,
            char expected) {
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                numericExpression);
        coerceValueForTargetType(code, actualType, QinIrTypeRef.doubleType());
        code.loadConstant((double) expected);
        code.dcmpg();
        java.lang.classfile.Label trueLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        switch (operator) {
            case "==", "===" -> code.ifeq(trueLabel);
            case "!=", "!==" -> code.ifne(trueLabel);
            case "<" -> code.iflt(trueLabel);
            case "<=" -> code.ifle(trueLabel);
            case ">" -> code.ifgt(trueLabel);
            case ">=" -> code.ifge(trueLabel);
            default -> throw new IllegalArgumentException("Unsupported numeric char comparison operator: " + operator);
        }
        code.iconst_0();
        code.goto_(doneLabel);
        code.labelBinding(trueLabel);
        code.iconst_1();
        code.labelBinding(doneLabel);
        return QinIrTypeRef.booleanType();
    }

    private String invertedComparisonOperator(String operator) {
        return switch (operator) {
            case "<" -> ">";
            case "<=" -> ">=";
            case ">" -> "<";
            case ">=" -> "<=";
            default -> operator;
        };
    }

    private void emitStaticStringOperand(
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
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_string__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/String;"));
    }

    private boolean isNullOrUndefinedLiteral(QinIrExpression expression) {
        return expression instanceof QinIrNullLiteral
                || (expression instanceof QinIrIdentifierReference identifierReference
                        && "undefined".equals(identifierReference.name()));
    }

    private boolean isJavaLangThrowableToStringStringComparison(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            QinIrExpression left,
            QinIrExpression right) {
        return isJavaLangThrowableToStringMethod(ownerDeclaration, method)
                && (isThrowableToStringStringOperand(left) || isThrowableToStringStringOperand(right));
    }

    private boolean isJavaLangThrowableToStringMethod(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method) {
        return ownerDeclaration != null
                && method != null
                && "__QinJavaLangThrowable".equals(ownerDeclaration.binaryName())
                && "toString".equals(method.name());
    }

    private boolean isThrowableToStringStringOperand(QinIrExpression expression) {
        if (expression instanceof QinIrStringLiteral) {
            return true;
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression
                && propertyAccessExpression.receiver() instanceof QinIrThisExpression) {
            return "message".equals(propertyAccessExpression.propertyName())
                    || "name".equals(propertyAccessExpression.propertyName());
        }
        return false;
    }

    private QinIrTypeRef emitJavaLangParseRuntimeCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        String expectedCallee = javaLangParseRuntimeGlobalCallee(ownerDeclaration, method);
        if (expectedCallee == null
                || !"Global".equals(builtinCallExpression.receiverName())
                || !"__qin_call__".equals(builtinCallExpression.methodName())
                || builtinCallExpression.arguments().size() < 2
                || builtinCallExpression.arguments().size() > 3
                || !(builtinCallExpression.arguments().get(0) instanceof QinIrIdentifierReference callee)
                || !expectedCallee.equals(callee.name())) {
            return null;
        }
        if ("parseFloat".equals(expectedCallee) && builtinCallExpression.arguments().size() != 2) {
            return null;
        }
        QinIrTypeRef valueType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(1));
        boxValueForObjectTarget(code, valueType);
        if (builtinCallExpression.arguments().size() == 2) {
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    expectedCallee,
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/Object;"));
            return QinIrTypeRef.classType("java.lang.Object");
        }
        QinIrTypeRef radixType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(2));
        boxValueForObjectTarget(code, radixType);
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "parseInt",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private String javaLangParseRuntimeGlobalCallee(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method) {
        if (ownerDeclaration == null || method == null) {
            return null;
        }
        String ownerName = ownerDeclaration.binaryName();
        if ("__QinJavaLangIntegerRuntime".equals(ownerName) && "parseInt".equals(method.name())) {
            return "parseInt";
        }
        if ("__QinJavaLangLongRuntime".equals(ownerName) && "parseLong".equals(method.name())) {
            return "parseInt";
        }
        if ("__QinJavaLangDoubleRuntime".equals(ownerName) && "parseDouble".equals(method.name())) {
            return "parseFloat";
        }
        return null;
    }

    private boolean isJavaLangLongRuntimeToStringCall(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return ownerDeclaration != null
                && method != null
                && "__QinJavaLangLongRuntime".equals(ownerDeclaration.binaryName())
                && "toString".equals(method.name())
                && "toString".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() <= 1;
    }

    private boolean isReferenceLike(QinIrTypeRef type) {
        return type != null && (type.kind() == QinIrTypeKind.CLASS || type.kind() == QinIrTypeKind.STRING);
    }

    private boolean isNullEqualityOperator(String operator) {
        return "==".equals(operator)
                || "!=".equals(operator)
                || "===".equals(operator)
                || "!==".equals(operator);
    }

    private boolean isStringEqualityOperator(String operator) {
        return isNullEqualityOperator(operator);
    }

    private boolean isReferenceEqualityOperator(String operator) {
        return isNullEqualityOperator(operator);
    }

    private boolean isJavaLangIntegralSumRuntimeMethod(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            String operator) {
        if (!"+".equals(operator) || ownerDeclaration == null || method == null || !"sum".equals(method.name())) {
            return false;
        }
        String ownerName = ownerDeclaration.binaryName();
        return "__QinJavaLangIntegerRuntime".equals(ownerName)
                || "__QinJavaLangLongRuntime".equals(ownerName);
    }

    private boolean isJavaLangNumberCompareRuntimeMethod(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            String operator) {
        if (!isNumericComparisonOperator(operator)
                || ownerDeclaration == null
                || method == null
                || !"compare".equals(method.name())) {
            return false;
        }
        String ownerName = ownerDeclaration.binaryName();
        return "__QinJavaLangIntegerRuntime".equals(ownerName)
                || "__QinJavaLangLongRuntime".equals(ownerName)
                || "__QinJavaLangDoubleRuntime".equals(ownerName);
    }

    private boolean isNumericArithmeticOperator(String operator) {
        return "+".equals(operator)
                || "-".equals(operator)
                || "*".equals(operator)
                || "/".equals(operator)
                || "%".equals(operator);
    }

    private boolean isNumericComparisonOperator(String operator) {
        return "==".equals(operator)
                || "!=".equals(operator)
                || "===".equals(operator)
                || "!==".equals(operator)
                || "<".equals(operator)
                || "<=".equals(operator)
                || ">".equals(operator)
                || ">=".equals(operator);
    }

    private boolean isNumericBitwiseOperator(String operator) {
        return "|".equals(operator)
                || "&".equals(operator)
                || "^".equals(operator)
                || "<<".equals(operator)
                || ">>".equals(operator)
                || ">>>".equals(operator);
    }

    private boolean isBooleanBitwiseOperator(String operator) {
        return "|".equals(operator)
                || "&".equals(operator)
                || "^".equals(operator);
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
        int sourceArgumentIndex = 0;
        for (int i = 0; i < argumentKinds.size(); i++) {
            QinBuiltinRegistry.BuiltinArgKind argumentKind = argumentKinds.get(i);
            if (argumentKind == QinBuiltinRegistry.BuiltinArgKind.ARRAY_REST) {
                int restCount = builtinCallExpression.arguments().size() - sourceArgumentIndex;
                code.loadConstant(restCount);
                code.anewarray(OBJECT_DESC);
                for (int restIndex = 0; restIndex < restCount; restIndex++) {
                    code.dup();
                    code.loadConstant(restIndex);
                    QinIrTypeRef restActualType = emitDeclarationExpression(
                            code,
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            builtinCallExpression.arguments().get(sourceArgumentIndex + restIndex));
                    boxValueForObjectTarget(code, restActualType);
                    code.aastore();
                }
                sourceArgumentIndex = builtinCallExpression.arguments().size();
                continue;
            }
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(sourceArgumentIndex));
            if (argumentKind == QinBuiltinRegistry.BuiltinArgKind.STRING) {
                coerceValueForTargetType(code, actualType, QinIrTypeRef.stringType());
            } else {
                boxValueForObjectTarget(code, actualType);
            }
            sourceArgumentIndex++;
        }
        if (sourceArgumentIndex != builtinCallExpression.arguments().size()) {
            throw new IllegalArgumentException(
                    "Builtin argument arity mismatch: "
                            + builtinCallExpression.receiverName() + "."
                            + builtinCallExpression.methodName() + "/"
                            + builtinCallExpression.arguments().size());
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
            QinIrTypeRef actualResultType = inferBuiltinMethodReturnType(builtinMethod);
            coerceValueForTargetType(code, actualResultType, resultType);
        }
        return resultType;
    }

    private QinIrTypeRef emitStaticInstanceofCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        String targetBinaryName = resolveStaticInstanceofTarget(
                builtinCallExpression.arguments().get(1),
                ownerDeclaration,
                declarationIndex);
        String generatedJavaSdkAliasName = generatedJavaSdkRuntimeInstanceofAliasName(targetBinaryName);
        if (generatedJavaSdkAliasName != null) {
            QinIrTypeRef valueType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(0));
            boxValueForObjectTarget(code, valueType);
            code.ldc(generatedJavaSdkAliasName);
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_instanceof__",
                    MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC));
            code.checkcast(BOOLEAN_DESC);
            code.invokevirtual(BOOLEAN_DESC, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"));
            return QinIrTypeRef.booleanType();
        }
        targetBinaryName = QinJavaSdkAliasSupport.canonicalBinaryName(targetBinaryName);
        if (targetBinaryName == null || targetBinaryName.isBlank()) {
            throw new IllegalArgumentException(
                    "Unsupported static __qin_instanceof__ target: "
                            + builtinCallExpression.arguments().get(1));
        }
        QinIrTypeRef valueType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(0));
        boxValueForObjectTarget(code, valueType);
        if (isJsErrorConstructorName(targetBinaryName)) {
            code.ldc(targetBinaryName);
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_instanceof__",
                    MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC));
            code.checkcast(BOOLEAN_DESC);
            code.invokevirtual(BOOLEAN_DESC, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"));
            return QinIrTypeRef.booleanType();
        }
        if (isSubhutiMatchTokenBinaryName(targetBinaryName)) {
            code.ldc(toReferenceClassDesc(targetBinaryName));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_instanceof__",
                    MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC));
            code.checkcast(BOOLEAN_DESC);
            code.invokevirtual(BOOLEAN_DESC, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"));
            return QinIrTypeRef.booleanType();
        }
        code.instanceOf(toReferenceClassDesc(targetBinaryName));
        return QinIrTypeRef.booleanType();
    }

    private QinIrTypeRef emitStaticJavaClassInfoCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrBuiltinCallExpression builtinCallExpression) {
        String targetBinaryName = resolveStaticJavaClassInfoBinaryName(
                ownerDeclaration,
                declarationIndex,
                builtinCallExpression);
        if (targetBinaryName == null || targetBinaryName.isBlank()) {
            throw new IllegalArgumentException(
                    "Static __qin_java_class_info__ target cannot be resolved: " + builtinCallExpression);
        }
        emitClassObject(code, targetBinaryName);
        return QinIrTypeRef.classType("java.lang.Class");
    }

    private String resolveStaticJavaClassInfoBinaryName(
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrBuiltinCallExpression builtinCallExpression) {
        if (!builtinCallExpression.arguments().isEmpty()) {
            QinIrExpression classArgument = builtinCallExpression.arguments().get(0);
            if (classArgument instanceof QinIrJavaClassLiteralExpression classLiteral) {
                return resolveJavaClassLiteralBinaryName(ownerDeclaration, declarationIndex, classLiteral);
            }
            if (classArgument instanceof QinIrIdentifierReference identifierReference) {
                QinIrClassDeclaration declaredClass = resolveDeclaredClassReference(
                        ownerDeclaration,
                        declarationIndex,
                        identifierReference.name());
                if (declaredClass != null) {
                    return declaredClass.binaryName();
                }
            }
        }
        String metaName = javaClassInfoMetaString(builtinCallExpression, "name");
        if (metaName == null || metaName.isBlank()) {
            metaName = javaClassInfoMetaString(builtinCallExpression, "interfaceName");
        }
        String localBinaryName = effectiveLocalReferenceBinaryName(metaName, declarationIndex);
        if (!Objects.equals(localBinaryName, metaName)) {
            return localBinaryName;
        }
        return canonicalJavaSdkAliasBinaryName(metaName);
    }

    private String javaClassInfoMetaString(
            QinIrBuiltinCallExpression builtinCallExpression,
            String key) {
        if (builtinCallExpression.arguments().size() < 2
                || !(builtinCallExpression.arguments().get(1) instanceof QinIrObjectLiteral meta)) {
            return null;
        }
        for (QinIrObjectProperty property : meta.properties()) {
            if (key.equals(property.key()) && property.value() instanceof QinIrStringLiteral literal) {
                return literal.value();
            }
        }
        return null;
    }

    private void emitClassObject(
            java.lang.classfile.CodeBuilder code,
            String binaryName) {
        switch (binaryName) {
            case "boolean" -> code.getstatic(ClassDesc.of("java.lang.Boolean"), "TYPE", CLASS_DESC);
            case "int" -> code.getstatic(ClassDesc.of("java.lang.Integer"), "TYPE", CLASS_DESC);
            case "double" -> code.getstatic(ClassDesc.of("java.lang.Double"), "TYPE", CLASS_DESC);
            case "long" -> code.getstatic(ClassDesc.of("java.lang.Long"), "TYPE", CLASS_DESC);
            case "float" -> code.getstatic(ClassDesc.of("java.lang.Float"), "TYPE", CLASS_DESC);
            case "short" -> code.getstatic(ClassDesc.of("java.lang.Short"), "TYPE", CLASS_DESC);
            case "byte" -> code.getstatic(ClassDesc.of("java.lang.Byte"), "TYPE", CLASS_DESC);
            case "char" -> code.getstatic(ClassDesc.of("java.lang.Character"), "TYPE", CLASS_DESC);
            case "void" -> code.getstatic(ClassDesc.of("java.lang.Void"), "TYPE", CLASS_DESC);
            default -> code.ldc(toReferenceClassDesc(binaryName));
        }
    }

    private QinIrTypeRef emitStaticJavaImplementsCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        if (!(builtinCallExpression.arguments().get(1) instanceof QinIrStringLiteral interfaceNameLiteral)) {
            throw new IllegalArgumentException(
                    "Static __qin_java_implements target must be a string literal: "
                            + builtinCallExpression.arguments().get(1));
        }
        String targetBinaryName = effectiveLocalReferenceBinaryName(
                interfaceNameLiteral.value(),
                declarationIndex);
        if (targetBinaryName == null || targetBinaryName.isBlank()) {
            throw new IllegalArgumentException("Static __qin_java_implements target cannot be blank");
        }
        QinIrTypeRef valueType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(0));
        boxValueForObjectTarget(code, valueType);
        code.instanceOf(toReferenceClassDesc(targetBinaryName));
        return QinIrTypeRef.booleanType();
    }

    private String resolveStaticInstanceofTarget(
            QinIrExpression targetExpression,
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (targetExpression instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            return classLiteralExpression.binaryName();
        }
        if (targetExpression instanceof QinIrIdentifierReference identifierReference) {
            String name = identifierReference.name();
            if ("Object".equals(name)) {
                return "java.lang.Object";
            }
            if (isJsErrorConstructorName(name)) {
                return name;
            }
            if (ownerDeclaration != null
                    && (ownerDeclaration.simpleName().equals(name) || ownerDeclaration.binaryName().equals(name))) {
                return ownerDeclaration.binaryName();
            }
            QinIrClassDeclaration declaration = declarationIndex.get(name);
            if (declaration != null) {
                return declaration.binaryName();
            }
            String staticClassName = resolveStaticInstanceofClassName(name, declarationIndex, false);
            if (staticClassName != null) {
                return staticClassName;
            }
            String canonicalAlias = QinJavaSdkAliasSupport.canonicalBinaryName(name);
            if (!canonicalAlias.equals(name)) {
                return name;
            }
        }
        if (targetExpression instanceof QinIrBuiltinCallExpression builtinCallExpression
                && "Global".equals(builtinCallExpression.receiverName())
                && "__qin_export_get__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1
                && builtinCallExpression.arguments().get(0) instanceof QinIrIdentifierReference slotIdentifier) {
            String className = classNameFromModuleExportSlot(slotIdentifier.name());
            return resolveStaticInstanceofClassName(className, declarationIndex, true);
        }
        if (targetExpression instanceof QinIrBuiltinCallExpression builtinCallExpression
                && "Global".equals(builtinCallExpression.receiverName())
                && "__qin_export_get__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1
                && builtinCallExpression.arguments().get(0) instanceof QinIrBuiltinCallExpression globalCall
                && "Global".equals(globalCall.receiverName())
                && "__qin_global__".equals(globalCall.methodName())
                && globalCall.arguments().size() == 1
                && globalCall.arguments().get(0) instanceof QinIrStringLiteral slotNameLiteral) {
            String className = classNameFromModuleExportSlot(slotNameLiteral.value());
            return resolveStaticInstanceofClassName(className, declarationIndex, true);
        }
        return null;
    }

    private String generatedJavaSdkRuntimeInstanceofAliasName(String targetBinaryName) {
        if (targetBinaryName == null) {
            return null;
        }
        return switch (targetBinaryName) {
            case "__QinJavaUtilArrayList",
                    "__QinJavaUtilUnmodifiableList",
                    "__QinJavaUtilHashSet",
                    "__QinJavaUtilTreeSet",
                    "__QinJavaUtilUnmodifiableSet",
                    "__QinJavaUtilHashMap",
                    "__QinJavaUtilLinkedHashMap",
                    "__QinJavaUtilIdentityHashMap",
                    "__QinJavaUtilUnmodifiableMap",
                    "__QinJavaUtilArrayDeque" -> targetBinaryName;
            default -> null;
        };
    }

    private boolean isJsErrorConstructorName(String name) {
        return "Error".equals(name)
                || "TypeError".equals(name)
                || "RangeError".equals(name)
                || "ReferenceError".equals(name)
                || "SyntaxError".equals(name);
    }

    private String resolveStaticInstanceofClassName(
            String className,
            Map<String, QinIrClassDeclaration> declarationIndex,
            boolean allowGeneratedExportSlotName) {
        if (className == null || className.isBlank()) {
            return null;
        }
        QinIrClassDeclaration declaration = declarationIndex.get(className);
        if (declaration != null) {
            return declaration.binaryName();
        }
        for (QinIrClassDeclaration candidate : declarationIndex.values()) {
            String flattenedBinaryName = candidate.binaryName().replace('.', '_').replace('$', '_');
            if (candidate.simpleName().equals(className)
                    || candidate.simpleName().endsWith(className)
                    || candidate.binaryName().endsWith("." + className)
                    || candidate.binaryName().endsWith("$" + className)
                    || flattenedBinaryName.equals(className)
                    || flattenedBinaryName.endsWith("_" + className)) {
                return candidate.binaryName();
            }
        }
        String canonicalAlias = QinJavaSdkAliasSupport.canonicalBinaryName(className);
        if (!canonicalAlias.equals(className)) {
            return className;
        }
        String flattenedJavaBinaryName = flattenedIdentifierJavaBinaryName(className);
        if (flattenedJavaBinaryName != null) {
            return flattenedJavaBinaryName;
        }
        if (allowGeneratedExportSlotName && isGeneratedExportSlotClassName(className)) {
            return className;
        }
        return null;
    }

    private String flattenedIdentifierJavaBinaryName(String className) {
        if (className == null
                || className.indexOf('_') < 0
                || className.startsWith("_")
                || className.contains("__")) {
            return null;
        }
        String candidate = className.replace('_', '.');
        if (candidate.contains("..") || !canResolveJavaClass(candidate)) {
            return null;
        }
        return candidate;
    }

    private boolean canResolveJavaClass(String binaryName) {
        try {
            Class.forName(
                    binaryName,
                    false,
                    Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException error) {
            return false;
        }
    }

    private String resolveJavaClassLiteralBinaryName(
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrJavaClassLiteralExpression classLiteralExpression) {
        String binaryName = classLiteralExpression.binaryName();
        String localBinaryName = effectiveLocalReferenceBinaryName(binaryName, declarationIndex);
        if (!Objects.equals(localBinaryName, binaryName)) {
            return localBinaryName;
        }
        QinIrClassDeclaration declaredClassReference = resolveDeclaredClassReference(
                ownerDeclaration,
                declarationIndex,
                binaryName);
        if (declaredClassReference != null) {
            return declaredClassReference.binaryName();
        }
        return canonicalJavaSdkAliasBinaryName(binaryName);
    }

    private boolean isGeneratedExportSlotClassName(String className) {
        if (className == null || className.isBlank()) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(className.charAt(0))) {
            return false;
        }
        for (int i = 1; i < className.length(); i++) {
            char ch = className.charAt(i);
            if (!Character.isJavaIdentifierPart(ch)) {
                return false;
            }
        }
        return true;
    }

    private String classNameFromModuleExportSlot(String slotName) {
        if (slotName == null || slotName.isBlank()) {
            return "";
        }
        int marker = slotName.indexOf("_e_");
        if (marker < 0 || marker + 3 >= slotName.length()) {
            return slotName;
        }
        String className = slotName.substring(marker + 3);
        while (className.startsWith("_")) {
            className = className.substring(1);
        }
        return className;
    }

    private String staticExportSlotName(QinIrExpression receiverExpression) {
        if (!(receiverExpression instanceof QinIrBuiltinCallExpression exportGet)
                || !"Global".equals(exportGet.receiverName())
                || !"__qin_export_get__".equals(exportGet.methodName())
                || exportGet.arguments().size() != 1) {
            return null;
        }
        QinIrExpression exportSlotExpression = exportGet.arguments().get(0);
        if (!(exportSlotExpression instanceof QinIrBuiltinCallExpression globalGet)
                || !"Global".equals(globalGet.receiverName())
                || !"__qin_global__".equals(globalGet.methodName())
                || globalGet.arguments().size() != 1
                || !(globalGet.arguments().get(0) instanceof QinIrStringLiteral slotNameLiteral)) {
            return null;
        }
        String slotName = slotNameLiteral.value();
        if (slotName == null || !slotName.startsWith("__qesm_m") || !slotName.contains("_e_")) {
            return null;
        }
        return slotName;
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

    private QinIrTypeRef emitShortCircuitExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrShortCircuitExpression expression) {
        QinIrTypeRef inferredLeftType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression.left());
        QinIrTypeRef inferredRightType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression.right());
        if (("&&".equals(expression.operator()) || "||".equals(expression.operator()))
                && isBooleanLike(inferredLeftType)
                && isBooleanLike(inferredRightType)) {
            return emitBooleanShortCircuitExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expression);
        }
        if ("||".equals(expression.operator())
                && isStaticReferenceTruthinessType(inferredLeftType)
                && isReferenceLike(inferredRightType)) {
            return emitStaticReferenceOrExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expression,
                    inferredLeftType,
                    inferredRightType);
        }
        java.lang.classfile.Label useLeftLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        QinIrTypeRef leftType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression.left());
        boxValueForObjectTarget(code, leftType);
        code.dup();
        if ("??".equals(expression.operator())) {
            code.ifnonnull(useLeftLabel);
            code.pop();
            QinIrTypeRef rightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expression.right());
            boxValueForObjectTarget(code, rightType);
            code.goto_(doneLabel);
        } else if ("||".equals(expression.operator())) {
            QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                    shortCircuitFallbackDiagnostic(
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            expression,
                            inferredLeftType,
                            inferredRightType),
                    "__qin_truthy__");
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_truthy__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z"));
            code.ifne(useLeftLabel);
            code.pop();
            QinIrTypeRef rightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expression.right());
            boxValueForObjectTarget(code, rightType);
            code.goto_(doneLabel);
        } else if ("&&".equals(expression.operator())) {
            QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                    shortCircuitFallbackDiagnostic(
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            expression,
                            inferredLeftType,
                            inferredRightType),
                    "__qin_truthy__");
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_truthy__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z"));
            code.ifeq(useLeftLabel);
            code.pop();
            QinIrTypeRef rightType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expression.right());
            boxValueForObjectTarget(code, rightType);
            code.goto_(doneLabel);
        } else {
            throw new IllegalArgumentException("Unsupported short-circuit operator: " + expression.operator());
        }
        code.labelBinding(useLeftLabel);
        code.labelBinding(doneLabel);
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef emitStaticReferenceOrExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrShortCircuitExpression expression,
            QinIrTypeRef inferredLeftType,
            QinIrTypeRef inferredRightType) {
        QinIrTypeRef resultType = staticReferenceOrResultType(
                inferredLeftType,
                inferredRightType,
                expression.right());
        java.lang.classfile.Label useLeftLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        QinIrTypeRef leftType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression.left());
        coerceValueForTargetType(code, leftType, resultType);
        code.dup();
        code.ifnonnull(useLeftLabel);
        code.pop();
        QinIrTypeRef rightType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression.right());
        coerceValueForTargetType(code, rightType, resultType);
        code.goto_(doneLabel);
        code.labelBinding(useLeftLabel);
        code.labelBinding(doneLabel);
        return resultType;
    }

    private QinIrTypeRef staticReferenceOrResultType(
            QinIrTypeRef leftType,
            QinIrTypeRef rightType,
            QinIrExpression rightExpression) {
        if (isObjectArrayType(leftType)
                && rightExpression instanceof QinIrArrayLiteral arrayLiteral
                && arrayLiteral.elements().isEmpty()) {
            return leftType;
        }
        return mergeBranchTypes(leftType, rightType);
    }

    private String shortCircuitFallbackDiagnostic(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrShortCircuitExpression expression,
            QinIrTypeRef inferredLeftType,
            QinIrTypeRef inferredRightType) {
        String ownerName = ownerDeclaration == null ? "<unknown-owner>" : ownerDeclaration.binaryName();
        String methodName = method == null ? "<unknown-method>" : method.name();
        QinIrTypeRef leftType = inferredLeftType == null
                ? inferDeclarationExpressionType(ownerDeclaration, method, declarationIndex, localFrame, expression.left())
                : inferredLeftType;
        QinIrTypeRef rightType = inferredRightType == null
                ? inferDeclarationExpressionType(ownerDeclaration, method, declarationIndex, localFrame, expression.right())
                : inferredRightType;
        return "QinJvmDeclarationClassEmitter"
                + " owner=" + ownerName
                + " method=" + methodName
                + " operator=" + expression.operator()
                + " leftType=" + leftType
                + " rightType=" + rightType
                + " leftShape=" + expression.left().getClass().getSimpleName()
                + " rightShape=" + expression.right().getClass().getSimpleName()
                + " rightExpression=" + expression.right();
    }

    private QinIrTypeRef emitBooleanShortCircuitExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrShortCircuitExpression expression) {
        java.lang.classfile.Label trueLabel = code.newLabel();
        java.lang.classfile.Label falseLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        emitBooleanExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression.left(),
                expression.operator() + " left");
        if ("||".equals(expression.operator())) {
            code.ifne(trueLabel);
            emitBooleanExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expression.right(),
                    expression.operator() + " right");
            code.ifne(trueLabel);
            code.goto_(falseLabel);
        } else if ("&&".equals(expression.operator())) {
            code.ifeq(falseLabel);
            emitBooleanExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    expression.right(),
                    expression.operator() + " right");
            code.ifeq(falseLabel);
            code.goto_(trueLabel);
        } else {
            throw new IllegalArgumentException("Unsupported boolean short-circuit operator: " + expression.operator());
        }
        code.labelBinding(trueLabel);
        code.iconst_1();
        code.goto_(doneLabel);
        code.labelBinding(falseLabel);
        code.iconst_0();
        code.labelBinding(doneLabel);
        return QinIrTypeRef.booleanType();
    }

    private QinIrTypeRef emitBooleanLogicalBuiltinCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        if (!(builtinCallExpression.arguments().get(0) instanceof QinIrStringLiteral operatorLiteral)) {
            throw new IllegalArgumentException("Boolean logical builtin must use a literal operator");
        }
        String operator = operatorLiteral.value();
        if (!"&&".equals(operator) && !"||".equals(operator)) {
            throw new IllegalArgumentException("Unsupported boolean logical operator: " + operator);
        }

        java.lang.classfile.Label trueLabel = code.newLabel();
        java.lang.classfile.Label falseLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        emitConditionExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(1),
                "__qin_logical__ left");
        if ("||".equals(operator)) {
            code.ifne(trueLabel);
            emitBooleanExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(2),
                    "__qin_logical__ right");
            code.ifne(trueLabel);
            code.goto_(falseLabel);
        } else {
            code.ifeq(falseLabel);
            emitBooleanExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(2),
                    "__qin_logical__ right");
            code.ifeq(falseLabel);
            code.goto_(trueLabel);
        }
        code.labelBinding(trueLabel);
        code.iconst_1();
        code.goto_(doneLabel);
        code.labelBinding(falseLabel);
        code.iconst_0();
        code.labelBinding(doneLabel);
        return QinIrTypeRef.booleanType();
    }

    private QinIrTypeRef emitConditionalBuiltinCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        java.lang.classfile.Label alternateLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        QinIrTypeRef resultType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression);

        emitConditionExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(0),
                "__qin_conditional__ test");
        code.ifeq(alternateLabel);
        QinIrTypeRef consequentType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(1));
        coerceConditionalBranchForTarget(
                code,
                builtinCallExpression.arguments().get(1),
                consequentType,
                resultType);
        code.goto_(doneLabel);

        code.labelBinding(alternateLabel);
        QinIrTypeRef alternateType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(2));
        coerceConditionalBranchForTarget(
                code,
                builtinCallExpression.arguments().get(2),
                alternateType,
                resultType);
        code.labelBinding(doneLabel);
        return resultType;
    }

    private void coerceConditionalBranchForTarget(
            java.lang.classfile.CodeBuilder code,
            QinIrExpression branchExpression,
            QinIrTypeRef actualType,
            QinIrTypeRef resultType) {
        if (isNullOrUndefinedLiteral(branchExpression) && isReferenceStorageType(resultType)) {
            return;
        }
        coerceValueForTargetType(code, actualType, resultType);
    }

    private void emitBooleanExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression expression,
            String debugName) {
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression);
        if (actualType.kind() == QinIrTypeKind.BOOLEAN) {
            return;
        }
        if (isBoxedBooleanType(actualType)) {
            code.checkcast(BOOLEAN_DESC);
            code.invokevirtual(BOOLEAN_DESC, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"));
            return;
        }
        throw new IllegalArgumentException(debugName + " must be boolean: type=" + actualType);
    }

    private void emitConditionExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression expression,
            String debugName) {
        QinIrTypeRef actualType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                expression);
        if (actualType.kind() == QinIrTypeKind.BOOLEAN) {
            return;
        }
        if (isBoxedBooleanType(actualType)) {
            code.checkcast(BOOLEAN_DESC);
            code.invokevirtual(BOOLEAN_DESC, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"));
            return;
        }
        if (isStaticReferenceTruthinessType(actualType)) {
            emitReferenceNonNullBoolean(code);
            return;
        }
        boxValueForObjectTarget(code, actualType);
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(
                "QinJvmDeclarationClassEmitter"
                        + " owner=" + ownerDeclaration.binaryName()
                        + " method=" + method.name()
                        + " debugName=" + debugName
                        + " conditionType=" + actualType
                        + " conditionShape=" + expression.getClass().getSimpleName()
                        + " expression=" + expression,
                "__qin_truthy__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_truthy__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z"));
    }

    private boolean isStaticReferenceTruthinessType(QinIrTypeRef type) {
        if (type == null
                || type.kind() != QinIrTypeKind.CLASS
                || type.binaryName() == null
                || type.binaryName().isBlank()) {
            return false;
        }
        String binaryName = QinJavaSdkAliasSupport.canonicalBinaryName(type.binaryName());
        return !"java.lang.Object".equals(binaryName)
                && !"java.lang.String".equals(binaryName)
                && !"java.lang.Boolean".equals(binaryName)
                && !"java.lang.Number".equals(binaryName)
                && !"java.lang.Integer".equals(binaryName)
                && !"java.lang.Long".equals(binaryName)
                && !"java.lang.Double".equals(binaryName)
                && !"java.lang.Float".equals(binaryName)
                && !"java.lang.Short".equals(binaryName)
                && !"java.lang.Byte".equals(binaryName)
                && !"java.math.BigInteger".equals(binaryName)
                && !"java.math.BigDecimal".equals(binaryName);
    }

    private void emitReferenceNonNullBoolean(java.lang.classfile.CodeBuilder code) {
        java.lang.classfile.Label trueLabel = code.newLabel();
        java.lang.classfile.Label doneLabel = code.newLabel();
        code.ifnonnull(trueLabel);
        code.iconst_0();
        code.goto_(doneLabel);
        code.labelBinding(trueLabel);
        code.iconst_1();
        code.labelBinding(doneLabel);
    }

    private QinIrTypeRef emitGlobalBuiltinObjectCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression,
            String runtimeMethodName) {
        return emitGlobalBuiltinObjectCall(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression,
                runtimeMethodName,
                "QinJvmDeclarationClassEmitter");
    }

    private QinIrTypeRef emitGlobalBuiltinObjectCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression,
            String runtimeMethodName,
            String diagnosticSource) {
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
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall(diagnosticSource, runtimeMethodName);
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

    private QinIrTypeRef emitStaticInitEnumValueCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        QinIrExpression valueExpression = builtinCallExpression.arguments().get(0);
        QinIrTypeRef valueType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                valueExpression);
        boxValueForObjectTarget(code, valueType);
        for (int i = 1; i < builtinCallExpression.arguments().size(); i++) {
            QinIrTypeRef argumentType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(i));
            boxValueForObjectTarget(code, argumentType);
        }
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_init_enum_value",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        coerceObjectResultForType(code, valueType);
        return valueType;
    }

    private String binaryBuiltinFallbackDiagnostic(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        String ownerName = ownerDeclaration == null ? "<unknown-owner>" : ownerDeclaration.binaryName();
        String methodName = method == null ? "<unknown-method>" : method.name();
        String operator = "<non-literal>";
        QinIrTypeRef leftType = QinIrTypeRef.classType("java.lang.Object");
        QinIrTypeRef rightType = QinIrTypeRef.classType("java.lang.Object");
        String leftShape = "<missing-left>";
        String rightShape = "<missing-right>";
        if (builtinCallExpression.arguments().size() == 3) {
            if (builtinCallExpression.arguments().get(0) instanceof QinIrStringLiteral operatorLiteral) {
                operator = operatorLiteral.value();
            }
            QinIrExpression left = builtinCallExpression.arguments().get(1);
            QinIrExpression right = builtinCallExpression.arguments().get(2);
            leftType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    left);
            rightType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    right);
            leftShape = left.getClass().getSimpleName();
            rightShape = right.getClass().getSimpleName();
        }
        return "QinJvmDeclarationClassEmitter"
                + " owner=" + ownerName
                + " method=" + methodName
                + " operator=" + operator
                + " leftType=" + leftType
                + " rightType=" + rightType
                + " leftShape=" + leftShape
                + " rightShape=" + rightShape;
    }

    private QinIrTypeRef emitGlobalStringCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        QinIrTypeRef argumentType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                builtinCallExpression.arguments().get(0));
        boxValueForObjectTarget(code, argumentType);
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_string__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/String;"));
        return QinIrTypeRef.stringType();
    }

    private QinIrTypeRef emitGlobalNumberCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrBuiltinCallExpression builtinCallExpression) {
        if (builtinCallExpression.arguments().isEmpty()) {
            code.dconst_0();
        } else {
            QinIrTypeRef argumentType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(0));
            boxValueForObjectTarget(code, argumentType);
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_number__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)D"));
        }
        return QinIrTypeRef.doubleType();
    }

    private QinIrTypeRef emitAssignmentExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrAssignmentExpression assignmentExpression) {
        QinIrExpression assignmentValue = assignmentExpression.value();
        String compoundBinaryOperator = compoundAssignmentBinaryOperator(assignmentExpression.operator());
        if (compoundBinaryOperator != null) {
            assignmentValue = new QinIrBuiltinCallExpression(
                    "Global",
                    "__qin_binary__",
                    List.of(
                            new QinIrStringLiteral(compoundBinaryOperator),
                            assignmentExpression.target(),
                            assignmentExpression.value()));
        } else if (!"=".equals(assignmentExpression.operator())) {
            throw new IllegalArgumentException(
                    "Declaration assignment expression supports only '=' for now: "
                            + assignmentExpression.operator());
        }
        if (assignmentExpression.target() instanceof QinIrPropertyAccessExpression propertyAccessExpression
                && propertyAccessExpression.receiver() instanceof QinIrThisExpression) {
            ResolvedFieldAccess fieldAccess = resolveFieldAccess(
                    ownerDeclaration,
                    propertyAccessExpression.propertyName(),
                    declarationIndex);
            if (fieldAccess == null) {
                throw new IllegalArgumentException(
                        "Unknown declaration field assignment target: "
                                + ownerDeclaration.binaryName()
                                + "."
                                + propertyAccessExpression.propertyName()
                                + " fields="
                                + ownerDeclaration.fields().stream()
                                .map(QinIrFieldDeclaration::name)
                                .toList()
                                + " super="
                                + ownerDeclaration.superType());
            }
            if (!fieldAccess.field().staticField()) {
                code.aload(0);
            }
            QinIrTypeRef actualType;
            if (assignmentValue instanceof QinIrNullLiteral && isPrimitiveFieldType(fieldAccess.field().type())) {
                emitPrimitiveDefaultValue(code, fieldAccess.field().type());
                actualType = fieldAccess.field().type();
            } else if (emitNullLiteralForReferenceParameter(
                    code,
                    assignmentValue,
                    fieldAccess.field().type(),
                    null)) {
                actualType = fieldAccess.field().type();
            } else {
                actualType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        assignmentValue);
                coerceValueForTargetType(code, actualType, fieldAccess.field().type());
            }
            if (fieldAccess.field().staticField()) {
                code.putstatic(
                        ClassDesc.of(fieldAccess.ownerBinaryName()),
                        fieldAccess.field().name(),
                        toClassDesc(fieldAccess.field().type()));
                code.getstatic(
                        ClassDesc.of(fieldAccess.ownerBinaryName()),
                        fieldAccess.field().name(),
                        toClassDesc(fieldAccess.field().type()));
            } else {
                code.putfield(
                        ClassDesc.of(fieldAccess.ownerBinaryName()),
                        fieldAccess.field().name(),
                        toClassDesc(fieldAccess.field().type()));
                code.aload(0);
                code.getfield(
                        ClassDesc.of(fieldAccess.ownerBinaryName()),
                        fieldAccess.field().name(),
                        toClassDesc(fieldAccess.field().type()));
            }
            return fieldAccess.field().type();
        }
        if (assignmentExpression.target() instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    propertyAccessExpression.receiver());
            ResolvedMutablePropertyAccess propertyAccess = resolveMutablePropertyAccess(
                    receiverType,
                    propertyAccessExpression.propertyName(),
                    declarationIndex);
            if (propertyAccess != null) {
                return emitMutablePropertyAssignmentExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        propertyAccessExpression.receiver(),
                        propertyAccess,
                        assignmentValue);
            }
        }
        if (assignmentExpression.target() instanceof QinIrMemberAccessExpression memberAccessExpression) {
            ResolvedFieldAccess staticFieldAccess = resolveDeclaredStaticFieldAccess(
                    ownerDeclaration,
                    declarationIndex,
                    memberAccessExpression.objectName(),
                    memberAccessExpression.propertyName());
            if (staticFieldAccess != null) {
                QinIrTypeRef actualType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        assignmentValue);
                coerceValueForTargetType(code, actualType, staticFieldAccess.field().type());
                code.putstatic(
                        ClassDesc.of(staticFieldAccess.ownerBinaryName()),
                        staticFieldAccess.field().name(),
                        toClassDesc(staticFieldAccess.field().type()));
                code.getstatic(
                        ClassDesc.of(staticFieldAccess.ownerBinaryName()),
                        staticFieldAccess.field().name(),
                        toClassDesc(staticFieldAccess.field().type()));
                return staticFieldAccess.field().type();
            }
            QinIrIdentifierReference receiverExpression =
                    new QinIrIdentifierReference(memberAccessExpression.objectName());
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    receiverExpression);
            ResolvedMutablePropertyAccess propertyAccess = resolveMutablePropertyAccess(
                    receiverType,
                    memberAccessExpression.propertyName(),
                    declarationIndex);
            if (propertyAccess != null) {
                return emitMutablePropertyAssignmentExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        receiverExpression,
                        propertyAccess,
                        assignmentValue);
            }
            return emitDynamicMemberSet(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    new QinIrIdentifierReference(memberAccessExpression.objectName()),
                    new QinIrStringLiteral(memberAccessExpression.propertyName()),
                    assignmentValue);
        }
        if (assignmentExpression.target() instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            return emitDynamicMemberSet(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    propertyAccessExpression.receiver(),
                    new QinIrStringLiteral(propertyAccessExpression.propertyName()),
                    assignmentValue);
        }
        if (assignmentExpression.target() instanceof QinIrElementAccessExpression elementAccessExpression) {
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression.receiver());
            if (isPrimitiveArrayType(receiverType)) {
                QinIrTypeRef elementType = staticArrayElementType(receiverType);
                TypeKind primitiveArrayKind = primitiveArrayKind(elementType);
                if (primitiveArrayKind == null) {
                    throw new IllegalArgumentException(
                            "Unsupported primitive array assignment type: "
                                    + receiverType
                                    + "; owner=" + ownerDeclaration.binaryName()
                                    + "; method=" + method.name()
                                    + "; expression=" + assignmentExpression);
                }
                emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        elementAccessExpression.receiver());
                QinIrTypeRef indexType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        elementAccessExpression.index());
                coerceValueForTargetType(code, indexType, QinIrTypeRef.intType());
                QinIrTypeRef actualType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        assignmentValue);
                coerceValueForTargetType(code, actualType, elementType);
                if (elementType.kind() == QinIrTypeKind.DOUBLE) {
                    code.dup2_x2();
                } else {
                    code.dup_x2();
                }
                code.arrayStore(primitiveArrayKind);
                return elementType;
            }
            if (isObjectArrayType(receiverType)) {
                emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        elementAccessExpression.receiver());
                QinIrTypeRef indexType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        elementAccessExpression.index());
                coerceValueForTargetType(code, indexType, QinIrTypeRef.intType());
                QinIrTypeRef actualType = emitDeclarationExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        assignmentValue);
                boxValueForObjectTarget(code, actualType);
                code.dup_x2();
                code.aastore();
                return QinIrTypeRef.classType("java.lang.Object");
            }
            return emitDynamicMemberSet(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression.receiver(),
                    elementAccessExpression.index(),
                    assignmentValue);
        }
        if (!(assignmentExpression.target() instanceof QinIrIdentifierReference identifierReference)) {
            throw new IllegalArgumentException(
                    "Declaration assignment target must be a local identifier or supported member/element target: "
                            + assignmentExpression.target());
        }
        LocalBinding binding = localFrame.resolve(identifierReference.name());
        if (binding != null) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    assignmentValue);
            coerceValueForTargetType(code, actualType, binding.type());
            storeLocalForType(code, binding.type(), binding.localSlot(), binding.name());
            loadLocalForType(code, binding.type(), binding.localSlot(), binding.name());
            return binding.type();
        }
        ParameterBinding parameterBinding = resolveParameterBinding(method, identifierReference.name());
        if (parameterBinding != null) {
            QinIrTypeRef actualType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    assignmentValue);
            coerceValueForTargetType(code, actualType, parameterBinding.parameter().type());
            storeLocalForType(
                    code,
                    parameterBinding.parameter().type(),
                    parameterBinding.localSlot(),
                    parameterBinding.parameter().name());
            loadLocalForType(
                    code,
                    parameterBinding.parameter().type(),
                    parameterBinding.localSlot(),
                    parameterBinding.parameter().name());
            return parameterBinding.parameter().type();
        }
        throw new IllegalArgumentException("Unknown declaration local assignment target: " + identifierReference.name());
    }

    private String compoundAssignmentBinaryOperator(String operator) {
        return switch (operator) {
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
            default -> null;
        };
    }

    private QinIrTypeRef generatedEnumMetadataMethodReturnType(
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (!methodCallExpression.arguments().isEmpty()) {
            return null;
        }
        return switch (methodCallExpression.methodName()) {
            case "ordinal" -> QinIrTypeRef.doubleType();
            case "name", "toString" -> QinIrTypeRef.stringType();
            default -> null;
        };
    }

    private boolean isGeneratedEnumLikeType(
            QinIrTypeRef type,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (type == null
                || type.kind() != QinIrTypeKind.CLASS
                || type.binaryName() == null
                || declarationIndex == null) {
            return false;
        }
        QinIrClassDeclaration declaration = resolveIndexedDeclaration(declarationIndex, type.binaryName());
        return isGeneratedEnumLikeDeclaration(declaration);
    }

    private boolean isGeneratedEnumLikeDeclaration(QinIrClassDeclaration declaration) {
        return declaration != null
                && declaration.superType() != null
                && "java.lang.Enum".equals(declaration.superType().binaryName());
    }

    private boolean isGeneratedEnumSyntheticStaticCall(String methodName, int argumentCount) {
        return ("values".equals(methodName) && argumentCount == 0)
                || ("valueOf".equals(methodName) && argumentCount == 1);
    }

    private boolean isGeneratedEnumValueOfCall(QinIrInstanceMethodCallExpression methodCallExpression) {
        return "valueOf".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1;
    }

    private boolean isGeneratedEnumValuesCall(QinIrInstanceMethodCallExpression methodCallExpression) {
        return "values".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().isEmpty();
    }

    private boolean isGeneratedEnumValueOfCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "valueOf".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().size() == 1;
    }

    private boolean isGeneratedEnumValuesCall(QinIrStaticMethodCallExpression methodCallExpression) {
        return methodCallExpression != null
                && "values".equals(methodCallExpression.methodName())
                && methodCallExpression.arguments().isEmpty();
    }

    private QinIrTypeRef emitGeneratedEnumValueOfCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrClassDeclaration enumDeclaration,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return emitGeneratedEnumValueOfCall(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                enumDeclaration,
                methodCallExpression.arguments());
    }

    private QinIrTypeRef emitGeneratedEnumValueOfCall(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrClassDeclaration enumDeclaration,
            List<QinIrExpression> arguments) {
        QinIrTypeRef argumentType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                arguments.get(0));
        boxValueForObjectTarget(code, argumentType);
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_enum_value_of__",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, OBJECT_DESC));
        QinIrTypeRef resultType = QinIrTypeRef.classType(enumDeclaration.binaryName());
        coerceObjectResultForType(code, resultType);
        return resultType;
    }

    private QinIrTypeRef emitGeneratedEnumValuesCall(java.lang.classfile.CodeBuilder code) {
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_enum_values__",
                MethodTypeDesc.of(OBJECT_ARRAY_DESC, OBJECT_DESC));
        return QinIrTypeRef.classType("java.lang.Object[]");
    }

    private QinIrTypeRef emitGeneratedEnumMetadataMethodCall(
            java.lang.classfile.CodeBuilder code,
            QinIrInstanceMethodCallExpression methodCallExpression,
            QinIrTypeRef returnType) {
        if ("ordinal".equals(methodCallExpression.methodName())) {
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_enum_ordinal__",
                    MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)D"));
            return returnType;
        }
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_enum_name__",
                MethodTypeDesc.of(STRING_DESC, OBJECT_DESC));
        return returnType;
    }

    private QinIrTypeRef emitUpdateExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrUpdateExpression updateExpression) {
        int delta = "++".equals(updateExpression.operator()) ? 1 : -1;
        if (updateExpression.target() instanceof QinIrIdentifierReference identifierReference) {
            LocalBinding binding = localFrame.resolve(identifierReference.name());
            if (binding != null) {
                emitLocalUpdateExpression(code, localFrame, binding, delta, updateExpression.prefix());
                return binding.type();
            }
            ParameterBinding parameterBinding = resolveParameterBinding(method, identifierReference.name());
            if (parameterBinding != null) {
                LocalBinding parameterLocal = new LocalBinding(
                        parameterBinding.parameter().name(),
                        parameterBinding.parameter().type(),
                        parameterBinding.localSlot());
                emitLocalUpdateExpression(code, localFrame, parameterLocal, delta, updateExpression.prefix());
                return parameterBinding.parameter().type();
            }
            throw new IllegalArgumentException(
                    "Unknown declaration local update target: " + identifierReference.name());
        }
        if (updateExpression.target() instanceof QinIrPropertyAccessExpression propertyAccessExpression
                && propertyAccessExpression.receiver() instanceof QinIrThisExpression) {
            ResolvedFieldAccess fieldAccess = resolveFieldAccess(
                    ownerDeclaration,
                    propertyAccessExpression.propertyName(),
                    declarationIndex);
            if (fieldAccess == null) {
                emitDynamicPropertyUpdateExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        propertyAccessExpression,
                        QinIrTypeRef.classType(ownerDeclaration.binaryName()),
                        delta,
                        updateExpression.prefix());
                return QinIrTypeRef.classType("java.lang.Object");
            }
            emitFieldUpdateExpression(code, localFrame, fieldAccess, delta, updateExpression.prefix());
            return fieldAccess.field().type();
        }
        if (updateExpression.target() instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    propertyAccessExpression.receiver());
            ResolvedMutablePropertyAccess propertyAccess = resolveMutablePropertyAccess(
                    receiverType,
                    propertyAccessExpression.propertyName(),
                    declarationIndex);
            if (propertyAccess != null) {
                emitMutablePropertyUpdateExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        propertyAccessExpression.receiver(),
                        propertyAccess,
                        delta,
                        updateExpression.prefix());
                return propertyAccess.propertyType();
            }
            emitDynamicPropertyUpdateExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    propertyAccessExpression,
                    receiverType,
                    delta,
                    updateExpression.prefix());
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (updateExpression.target() instanceof QinIrMemberAccessExpression memberAccessExpression) {
            ResolvedFieldAccess staticFieldAccess = resolveDeclaredStaticFieldAccess(
                    ownerDeclaration,
                    declarationIndex,
                    memberAccessExpression.objectName(),
                    memberAccessExpression.propertyName());
            if (staticFieldAccess != null) {
                emitFieldUpdateExpression(code, localFrame, staticFieldAccess, delta, updateExpression.prefix());
                return staticFieldAccess.field().type();
            }
            QinIrPropertyAccessExpression propertyAccessExpression = new QinIrPropertyAccessExpression(
                    new QinIrIdentifierReference(memberAccessExpression.objectName()),
                    memberAccessExpression.propertyName());
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    propertyAccessExpression.receiver());
            ResolvedMutablePropertyAccess propertyAccess = resolveMutablePropertyAccess(
                    receiverType,
                    memberAccessExpression.propertyName(),
                    declarationIndex);
            if (propertyAccess != null) {
                emitMutablePropertyUpdateExpression(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        propertyAccessExpression.receiver(),
                        propertyAccess,
                        delta,
                        updateExpression.prefix());
                return propertyAccess.propertyType();
            }
            emitDynamicPropertyUpdateExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    propertyAccessExpression,
                    receiverType,
                    delta,
                    updateExpression.prefix());
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (updateExpression.target() instanceof QinIrElementAccessExpression elementAccessExpression) {
            return emitElementUpdateExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression,
                    delta,
                    updateExpression.prefix());
        }
        throw new IllegalArgumentException(
                "Declaration update target must be a local identifier or this-field: "
                        + updateExpression.target());
    }

    private void emitDynamicPropertyUpdateExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrPropertyAccessExpression propertyAccessExpression,
            QinIrTypeRef receiverType,
            int delta,
            boolean prefix) {
        LocalBinding receiverBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_receiver"),
                QinIrTypeRef.classType("java.lang.Object"));
        QinIrTypeRef actualReceiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                propertyAccessExpression.receiver());
        boxValueForObjectTarget(code, actualReceiverType);
        storeLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());

        LocalBinding oldBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_old_" + propertyAccessExpression.propertyName()),
                QinIrTypeRef.classType("java.lang.Object"));
        loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
        code.ldc(propertyAccessExpression.propertyName());
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_member_get__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_member_get__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());

        LocalBinding nextBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_next_" + propertyAccessExpression.propertyName()),
                QinIrTypeRef.classType("java.lang.Object"));
        emitDynamicIncrementFromStoredObject(code, oldBinding, delta);
        storeLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
        loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
        code.ldc(propertyAccessExpression.propertyName());
        loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_member_set__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_member_set__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        code.pop();
        emitDynamicUpdateResult(code, oldBinding, nextBinding, prefix);
    }

    private QinIrTypeRef emitMutablePropertyAssignmentExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression receiverExpression,
            ResolvedMutablePropertyAccess propertyAccess,
            QinIrExpression assignmentValue) {
        LocalBinding receiverBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_property_receiver"),
                propertyAccess.receiverType());
        QinIrTypeRef actualReceiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                receiverExpression);
        coerceValueForTargetType(code, actualReceiverType, propertyAccess.receiverType());
        storeLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());

        LocalBinding valueBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_property_value"),
                propertyAccess.propertyType());
        QinIrTypeRef actualValueType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                assignmentValue);
        coerceValueForTargetType(code, actualValueType, propertyAccess.propertyType());
        storeLocalForType(code, valueBinding.type(), valueBinding.localSlot(), valueBinding.name());

        loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
        loadLocalForType(code, valueBinding.type(), valueBinding.localSlot(), valueBinding.name());
        invokeMutablePropertySetter(code, propertyAccess);
        loadLocalForType(code, valueBinding.type(), valueBinding.localSlot(), valueBinding.name());
        return propertyAccess.propertyType();
    }

    private void emitLocalUpdateExpression(
            java.lang.classfile.CodeBuilder code,
            LocalFrame localFrame,
            LocalBinding binding,
            int delta,
            boolean prefix) {
        QinIrTypeRef type = binding.type();
        if (type.kind() == QinIrTypeKind.INT) {
            if (!prefix) {
                loadLocalForType(code, type, binding.localSlot(), binding.name());
                code.iinc(binding.localSlot(), delta);
                return;
            }
            code.iinc(binding.localSlot(), delta);
            loadLocalForType(code, type, binding.localSlot(), binding.name());
            return;
        }
        if (type.kind() == QinIrTypeKind.DOUBLE) {
            LocalBinding resultBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_" + binding.name()),
                    QinIrTypeRef.doubleType());
            loadLocalForType(code, type, binding.localSlot(), binding.name());
            code.loadConstant(1.0d);
            if (delta > 0) {
                code.dadd();
            } else {
                code.dsub();
            }
            storeLocalForType(code, type, binding.localSlot(), binding.name());
            if (prefix) {
                loadLocalForType(code, type, binding.localSlot(), binding.name());
                return;
            }
            loadLocalForType(code, type, binding.localSlot(), binding.name());
            code.loadConstant(1.0d);
            if (delta > 0) {
                code.dsub();
            } else {
                code.dadd();
            }
            storeLocalForType(code, resultBinding.type(), resultBinding.localSlot(), resultBinding.name());
            loadLocalForType(code, resultBinding.type(), resultBinding.localSlot(), resultBinding.name());
            return;
        }
        if (isNumericLike(type)) {
            LocalBinding oldBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_old_" + binding.name()),
                    type);
            LocalBinding nextBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_next_" + binding.name()),
                    type);
            loadLocalForType(code, type, binding.localSlot(), binding.name());
            storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
            emitStaticNumericIncrementFromStoredValue(code, oldBinding, nextBinding, delta);
            loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            coerceValueForTargetType(code, nextBinding.type(), type);
            storeLocalForType(code, type, binding.localSlot(), binding.name());
            loadLocalForType(
                    code,
                    prefix ? nextBinding.type() : oldBinding.type(),
                    prefix ? nextBinding.localSlot() : oldBinding.localSlot(),
                    prefix ? nextBinding.name() : oldBinding.name());
            return;
        }
        if (isDynamicObjectType(type)) {
            LocalBinding oldBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_old_" + binding.name()),
                    QinIrTypeRef.classType("java.lang.Object"));
            LocalBinding nextBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_next_" + binding.name()),
                    QinIrTypeRef.classType("java.lang.Object"));
            loadLocalForType(code, type, binding.localSlot(), binding.name());
            boxValueForObjectTarget(code, type);
            storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
            emitDynamicIncrementFromStoredObject(code, oldBinding, delta);
            storeLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            coerceValueForTargetType(code, nextBinding.type(), type);
            storeLocalForType(code, type, binding.localSlot(), binding.name());
            emitDynamicUpdateResult(code, oldBinding, nextBinding, prefix);
            return;
        }
        throw new IllegalArgumentException(
                "Declaration update target must be numeric or dynamic object: "
                        + binding.name()
                        + " type="
                        + type);
    }

    private void emitFieldUpdateExpression(
            java.lang.classfile.CodeBuilder code,
            LocalFrame localFrame,
            ResolvedFieldAccess fieldAccess,
            int delta,
            boolean prefix) {
        QinIrTypeRef type = fieldAccess.field().type();
        LocalBinding oldBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_old_" + fieldAccess.field().name()),
                type);
        emitResolvedFieldGet(code, fieldAccess);
        storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());

        if (isNumericLike(type)) {
            LocalBinding nextBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_next_" + fieldAccess.field().name()),
                    type);
            emitStaticNumericIncrementFromStoredValue(code, oldBinding, nextBinding, delta);
            emitResolvedFieldSetFromLocal(code, fieldAccess, nextBinding);
            loadLocalForType(
                    code,
                    prefix ? nextBinding.type() : oldBinding.type(),
                    prefix ? nextBinding.localSlot() : oldBinding.localSlot(),
                    prefix ? nextBinding.name() : oldBinding.name());
            return;
        }

        if (isDynamicObjectType(type)) {
            LocalBinding nextBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_next_" + fieldAccess.field().name()),
                    QinIrTypeRef.classType("java.lang.Object"));
            emitDynamicIncrementFromStoredObject(code, oldBinding, delta);
            storeLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            emitResolvedFieldSetFromLocal(code, fieldAccess, nextBinding);
            emitDynamicUpdateResult(code, oldBinding, nextBinding, prefix);
            return;
        }

        throw new IllegalArgumentException(
                "Declaration field update target must be numeric or dynamic object: "
                        + fieldAccess.ownerBinaryName()
                        + "."
                        + fieldAccess.field().name()
                        + " type="
                        + type);
    }

    private void emitMutablePropertyUpdateExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression receiverExpression,
            ResolvedMutablePropertyAccess propertyAccess,
            int delta,
            boolean prefix) {
        QinIrTypeRef type = propertyAccess.propertyType();
        LocalBinding receiverBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_property_receiver"),
                propertyAccess.receiverType());
        QinIrTypeRef actualReceiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                receiverExpression);
        coerceValueForTargetType(code, actualReceiverType, propertyAccess.receiverType());
        storeLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());

        LocalBinding oldBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_old_" + propertyAccess.propertyName()),
                type);
        loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
        invokeAccessor(
                code,
                new ResolvedPropertyAccess(
                        propertyAccess.ownerBinaryName(),
                        propertyAccess.getterName(),
                        propertyAccess.propertyType(),
                        propertyAccess.ownerInterface()));
        storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());

        if (isNumericLike(type)) {
            LocalBinding nextBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_next_" + propertyAccess.propertyName()),
                    type);
            emitStaticNumericIncrementFromStoredValue(code, oldBinding, nextBinding, delta);
            loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
            loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            invokeMutablePropertySetter(code, propertyAccess);
            loadLocalForType(
                    code,
                    prefix ? nextBinding.type() : oldBinding.type(),
                    prefix ? nextBinding.localSlot() : oldBinding.localSlot(),
                    prefix ? nextBinding.name() : oldBinding.name());
            return;
        }

        if (isDynamicObjectType(type)) {
            LocalBinding nextBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_next_" + propertyAccess.propertyName()),
                    QinIrTypeRef.classType("java.lang.Object"));
            emitDynamicIncrementFromStoredObject(code, oldBinding, delta);
            storeLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
            loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            invokeMutablePropertySetter(code, propertyAccess);
            loadLocalForType(
                    code,
                    prefix ? nextBinding.type() : oldBinding.type(),
                    prefix ? nextBinding.localSlot() : oldBinding.localSlot(),
                    prefix ? nextBinding.name() : oldBinding.name());
            return;
        }

        throw new IllegalArgumentException(
                "Declaration property update target must be numeric or dynamic object: "
                        + propertyAccess.ownerBinaryName()
                        + "."
                        + propertyAccess.propertyName()
                        + " type="
                        + type);
    }

    private void emitStaticNumericIncrementFromStoredValue(
            java.lang.classfile.CodeBuilder code,
            LocalBinding oldBinding,
            LocalBinding nextBinding,
            int delta) {
        QinIrTypeRef arithmeticType = numericUpdateArithmeticType(nextBinding.type());
        loadLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
        coerceValueForTargetType(code, oldBinding.type(), arithmeticType);
        if (arithmeticType.kind() == QinIrTypeKind.INT) {
            code.loadConstant(1);
            if (delta > 0) {
                code.iadd();
            } else {
                code.isub();
            }
        } else {
            code.loadConstant(1.0d);
            if (delta > 0) {
                code.dadd();
            } else {
                code.dsub();
            }
        }
        coerceValueForTargetType(code, arithmeticType, nextBinding.type());
        storeLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
    }

    private QinIrTypeRef numericUpdateArithmeticType(QinIrTypeRef type) {
        if (type.kind() == QinIrTypeKind.INT
                || (type.kind() == QinIrTypeKind.CLASS
                && "java.lang.Integer".equals(type.binaryName()))) {
            return QinIrTypeRef.intType();
        }
        return QinIrTypeRef.doubleType();
    }

    private void emitDynamicIncrementFromStoredObject(
            java.lang.classfile.CodeBuilder code,
            LocalBinding oldBinding,
            int delta) {
        code.ldc(delta > 0 ? "+" : "-");
        loadLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
        boxValueForObjectTarget(code, oldBinding.type());
        code.loadConstant(1.0d);
        boxValueForObjectTarget(code, QinIrTypeRef.doubleType());
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_binary__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_binary__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
    }

    private void emitDynamicUpdateResult(
            java.lang.classfile.CodeBuilder code,
            LocalBinding oldBinding,
            LocalBinding nextBinding,
            boolean prefix) {
        if (prefix) {
            loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            return;
        }
        code.ldc("+");
        loadLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
        boxValueForObjectTarget(code, oldBinding.type());
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_unary__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_unary__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
    }

    private QinIrTypeRef emitElementUpdateExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrElementAccessExpression elementAccessExpression,
            int delta,
            boolean prefix) {
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                elementAccessExpression.receiver());
        if (isPrimitiveArrayType(receiverType)) {
            QinIrTypeRef elementType = staticArrayElementType(receiverType);
            TypeKind primitiveArrayKind = primitiveArrayKind(elementType);
            if (primitiveArrayKind == null || !isNumericLike(elementType)) {
                throw new IllegalArgumentException(
                        "Primitive array update requires numeric element type: "
                                + receiverType
                                + "; owner=" + ownerDeclaration.binaryName()
                                + "; method=" + method.name()
                                + "; expression=" + elementAccessExpression);
            }
            LocalBinding receiverBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_receiver"),
                    receiverType);
            storeLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
            QinIrTypeRef indexType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression.index());
            coerceValueForTargetType(code, indexType, QinIrTypeRef.intType());
            LocalBinding indexBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_index"),
                    QinIrTypeRef.intType());
            storeLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
            LocalBinding oldBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_old_element"),
                    elementType);
            loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
            loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
            code.arrayLoad(primitiveArrayKind);
            storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
            LocalBinding nextBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_next_element"),
                    elementType);
            emitStaticNumericIncrementFromStoredValue(code, oldBinding, nextBinding, delta);
            loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
            loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
            loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            code.arrayStore(primitiveArrayKind);
            loadLocalForType(
                    code,
                    prefix ? nextBinding.type() : oldBinding.type(),
                    prefix ? nextBinding.localSlot() : oldBinding.localSlot(),
                    prefix ? nextBinding.name() : oldBinding.name());
            return prefix ? nextBinding.type() : oldBinding.type();
        }
        if (isObjectArrayType(receiverType)) {
            LocalBinding receiverBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_receiver"),
                    receiverType);
            storeLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
            QinIrTypeRef indexType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression.index());
            coerceValueForTargetType(code, indexType, QinIrTypeRef.intType());
            LocalBinding indexBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_index"),
                    QinIrTypeRef.intType());
            storeLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
            QinIrTypeRef elementType = staticArrayElementType(receiverType);
            if (isNumericLike(elementType)) {
                LocalBinding oldBinding = localFrame.declare(
                        localFrame.syntheticLocalName("__qin_update_old_element"),
                        elementType);
                loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
                loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
                code.aaload();
                coerceObjectResultForType(code, elementType);
                storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
                LocalBinding nextBinding = localFrame.declare(
                        localFrame.syntheticLocalName("__qin_update_next_element"),
                        elementType);
                emitStaticNumericIncrementFromStoredValue(code, oldBinding, nextBinding, delta);
                loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
                loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
                loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
                boxValueForObjectTarget(code, nextBinding.type());
                code.aastore();
                loadLocalForType(
                        code,
                        prefix ? nextBinding.type() : oldBinding.type(),
                        prefix ? nextBinding.localSlot() : oldBinding.localSlot(),
                        prefix ? nextBinding.name() : oldBinding.name());
                return prefix ? nextBinding.type() : oldBinding.type();
            }
            LocalBinding oldBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_old_element"),
                    QinIrTypeRef.classType("java.lang.Object"));
            loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
            loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
            code.aaload();
            storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
            LocalBinding nextBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_update_next_element"),
                    QinIrTypeRef.classType("java.lang.Object"));
            emitDynamicIncrementFromStoredObject(code, oldBinding, delta);
            storeLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
            loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
            loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
            code.aastore();
            emitDynamicUpdateResult(code, oldBinding, nextBinding, prefix);
            return QinIrTypeRef.classType("java.lang.Object");
        }

        LocalBinding receiverBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_receiver"),
                QinIrTypeRef.classType("java.lang.Object"));
        boxValueForObjectTarget(code, receiverType);
        storeLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
        QinIrTypeRef indexType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                elementAccessExpression.index());
        boxValueForObjectTarget(code, indexType);
        LocalBinding indexBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_index"),
                QinIrTypeRef.classType("java.lang.Object"));
        storeLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        LocalBinding oldBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_old_element"),
                QinIrTypeRef.classType("java.lang.Object"));
        loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_member_get__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_member_get__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        storeLocalForType(code, oldBinding.type(), oldBinding.localSlot(), oldBinding.name());
        LocalBinding nextBinding = localFrame.declare(
                localFrame.syntheticLocalName("__qin_update_next_element"),
                QinIrTypeRef.classType("java.lang.Object"));
        emitDynamicIncrementFromStoredObject(code, oldBinding, delta);
        storeLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
        loadLocalForType(code, receiverBinding.type(), receiverBinding.localSlot(), receiverBinding.name());
        loadLocalForType(code, indexBinding.type(), indexBinding.localSlot(), indexBinding.name());
        loadLocalForType(code, nextBinding.type(), nextBinding.localSlot(), nextBinding.name());
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_member_set__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_member_set__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        code.pop();
        emitDynamicUpdateResult(code, oldBinding, nextBinding, prefix);
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private void emitResolvedFieldSetFromLocal(
            java.lang.classfile.CodeBuilder code,
            ResolvedFieldAccess fieldAccess,
            LocalBinding valueBinding) {
        if (!fieldAccess.field().staticField()) {
            code.aload(0);
        }
        loadLocalForType(code, valueBinding.type(), valueBinding.localSlot(), valueBinding.name());
        coerceValueForTargetType(code, valueBinding.type(), fieldAccess.field().type());
        if (fieldAccess.field().staticField()) {
            code.putstatic(
                    ClassDesc.of(fieldAccess.ownerBinaryName()),
                    fieldAccess.field().name(),
                    toClassDesc(fieldAccess.field().type()));
            return;
        }
        code.putfield(
                ClassDesc.of(fieldAccess.ownerBinaryName()),
                fieldAccess.field().name(),
                toClassDesc(fieldAccess.field().type()));
    }

    private QinIrTypeRef emitDynamicMemberSet(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression receiver,
            QinIrExpression property,
            QinIrExpression value) {
        QinIrTypeRef inferredReceiverType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                receiver);
        QinIrTypeRef inferredPropertyType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                property);
        QinIrTypeRef inferredValueType = inferDeclarationExpressionType(
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                value);
        if (isJavaUtilMapType(inferredReceiverType)) {
            QinIrTypeRef actualValueType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    value);
            boxValueForObjectTarget(code, actualValueType);
            LocalBinding valueBinding = localFrame.declare(
                    localFrame.syntheticLocalName("__qin_map_set_value"),
                    QinIrTypeRef.classType("java.lang.Object"));
            storeLocalForType(code, valueBinding.type(), valueBinding.localSlot(), valueBinding.name());
            emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    receiver);
            code.checkcast(MAP_DESC);
            emitDeclarationExpressionAsObject(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    property);
            loadLocalForType(code, valueBinding.type(), valueBinding.localSlot(), valueBinding.name());
            code.invokeinterface(MAP_DESC, "put", MAP_PUT_SIGNATURE);
            code.pop();
            loadLocalForType(code, valueBinding.type(), valueBinding.localSlot(), valueBinding.name());
            return QinIrTypeRef.classType("java.lang.Object");
        }
        throw new IllegalStateException(
                "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter would emit JavaEsmGlobal.__qin_member_set__ "
                        + "while compiling unresolved member assignment to JVM .class. Member assignment requires a "
                        + "statically admitted field, mutable property, array element, or explicit Map/Dict receiver. "
                        + "owner="
                        + ownerDeclaration.binaryName()
                        + " method="
                        + method.name()
                        + " receiverType="
                        + inferredReceiverType
                        + " propertyType="
                        + inferredPropertyType
                        + " valueType="
                        + inferredValueType
                        + " receiver="
                        + receiver
                        + " property="
                        + property
                        + " value="
                        + value);
        /*
        QinIrTypeRef receiverType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                receiver);
        boxValueForObjectTarget(code, receiverType);
        QinIrTypeRef propertyType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                property);
        boxValueForObjectTarget(code, propertyType);
        QinIrTypeRef valueType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                value);
        boxValueForObjectTarget(code, valueType);
        QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_member_set__");
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_member_set__",
                MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"));
        return QinIrTypeRef.classType("java.lang.Object");
        */
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

    private void emitArrayLiteral(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrArrayLiteral arrayLiteral) {
        code.new_(ARRAY_LIST_DESC);
        code.dup();
        code.invokespecial(ARRAY_LIST_DESC, "<init>", VOID_INIT);
        for (QinIrExpression element : arrayLiteral.elements()) {
            if (element instanceof QinIrSpreadArgumentExpression) {
                throw new IllegalStateException(
                        "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter cannot statically emit array spread "
                                + "inside a JVM .class array literal yet. Use an explicit List/array construction path "
                                + "or add an owned static spread lowering before admitting this source.");
            }
            code.dup();
            emitDeclarationExpressionAsObject(code, ownerDeclaration, method, declarationIndex, localFrame, element);
            code.invokevirtual(ARRAY_LIST_DESC, "add", LIST_ADD_SIGNATURE);
            code.pop();
        }
    }

    private QinIrTypeRef emitArrayCreationExpression(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrArrayCreationExpression arrayCreationExpression) {
        QinIrTypeRef arrayType = arrayTypeRef(
                arrayCreationExpression.componentType(),
                arrayCreationExpression.dimensions().size() + arrayCreationExpression.trailingEmptyDimensions());
        if (arrayCreationExpression.dimensions().size() != 1 || arrayCreationExpression.trailingEmptyDimensions() != 0) {
            throw new IllegalStateException(
                    "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter cannot statically emit multi-dimensional "
                            + "Java array creation yet: " + arrayCreationExpression);
        }
        QinIrTypeRef dimensionType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                arrayCreationExpression.dimensions().get(0));
        coerceValueForTargetType(code, dimensionType, QinIrTypeRef.intType());
        emitNewArray(code, arrayCreationExpression.componentType());
        return arrayType;
    }

    private boolean emitExpressionAsJavaArrayParameter(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression expression,
            Class<?> reflectedTargetType) {
        if (reflectedTargetType == null || !reflectedTargetType.isArray()) {
            return false;
        }
        Class<?> reflectedComponentType = reflectedTargetType.getComponentType();
        if (expression instanceof QinIrArrayCreationExpression arrayCreationExpression) {
            QinIrTypeRef actualType = emitArrayCreationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arrayCreationExpression);
            coerceValueForJavaParameterType(code, actualType, reflectedTargetType);
            return true;
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            emitArrayLiteralAsJavaArray(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arrayLiteral,
                    reflectedComponentType);
            return true;
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            Integer length = staticArrayFromNullFactoryLength(methodCallExpression);
            if (length != null) {
                code.loadConstant(length);
                TypeKind primitiveArrayKind = reflectedComponentType.isPrimitive()
                        ? primitiveTypeKind(reflectedComponentType)
                        : null;
                if (primitiveArrayKind == null) {
                    code.anewarray(toReferenceClassDesc(reflectedComponentType.getName()));
                } else {
                    code.newarray(primitiveArrayKind);
                }
                return true;
            }
        }
        return false;
    }

    private boolean emitExpressionAsStaticArrayParameter(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression expression,
            QinIrTypeRef targetType) {
        QinIrTypeRef componentType = staticArrayParameterElementType(targetType);
        if (componentType == null) {
            return false;
        }
        QinIrTypeRef storageComponentType = staticArrayStorageElementType(targetType, componentType);
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            emitArrayLiteralAsTypedArray(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    arrayLiteral,
                    storageComponentType,
                    componentType);
            return true;
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            StaticArrayFromFactory factory = staticArrayFromFactory(methodCallExpression);
            if (canEmitStaticArrayFromFactoryAsTypedArray(factory, componentType)) {
                emitStaticArrayFromFactoryAsTypedArray(
                        code,
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        factory,
                        storageComponentType);
                return true;
            }
        }
        return false;
    }

    private void emitArrayLiteralAsJavaArray(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrArrayLiteral arrayLiteral,
            Class<?> reflectedComponentType) {
        TypeKind primitiveArrayKind = reflectedComponentType.isPrimitive()
                ? primitiveTypeKind(reflectedComponentType)
                : null;
        code.loadConstant(arrayLiteral.elements().size());
        if (primitiveArrayKind == null) {
            code.anewarray(toReferenceClassDesc(reflectedComponentType.getName()));
        } else {
            code.newarray(primitiveArrayKind);
        }
        for (int i = 0; i < arrayLiteral.elements().size(); i++) {
            QinIrExpression element = arrayLiteral.elements().get(i);
            if (element instanceof QinIrSpreadArgumentExpression) {
                throw new IllegalStateException(
                        "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter cannot statically emit array spread "
                                + "inside a Java array parameter literal yet: " + arrayLiteral);
            }
            code.dup();
            code.loadConstant(i);
            QinIrTypeRef actualElementType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    element);
            coerceValueForJavaParameterType(code, actualElementType, reflectedComponentType);
            if (primitiveArrayKind == null) {
                code.aastore();
            } else {
                code.arrayStore(primitiveArrayKind);
            }
        }
    }

    private void emitArrayLiteralAsTypedArray(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrArrayLiteral arrayLiteral,
            QinIrTypeRef storageComponentType,
            QinIrTypeRef semanticComponentType) {
        TypeKind primitiveArrayKind = primitiveArrayKind(storageComponentType);
        code.loadConstant(arrayLiteral.elements().size());
        emitNewArray(code, storageComponentType);
        for (int i = 0; i < arrayLiteral.elements().size(); i++) {
            QinIrExpression element = arrayLiteral.elements().get(i);
            if (element instanceof QinIrSpreadArgumentExpression) {
                throw new IllegalStateException(
                        "[QinDynamicSemanticError] QinJvmDeclarationClassEmitter cannot statically emit array spread "
                                + "inside a typed JVM array parameter literal yet: " + arrayLiteral);
            }
            code.dup();
            code.loadConstant(i);
            QinIrTypeRef actualElementType = emitDeclarationExpression(
                    code,
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    element);
            QinIrTypeRef targetElementType = primitiveArrayKind == null ? storageComponentType : semanticComponentType;
            coerceValueForTargetType(code, actualElementType, targetElementType);
            if (primitiveArrayKind == null) {
                code.aastore();
            } else {
                code.arrayStore(primitiveArrayKind);
            }
        }
    }

    private boolean canEmitStaticArrayFromFactoryAsTypedArray(
            StaticArrayFromFactory factory,
            QinIrTypeRef componentType) {
        if (factory == null || componentType == null) {
            return false;
        }
        Integer length = staticArrayFactoryLength(factory);
        if (length != null && length == 0) {
            return true;
        }
        return isDefaultArrayValueExpression(factory.valueExpression(), componentType);
    }

    private Integer staticArrayFactoryLength(StaticArrayFromFactory factory) {
        if (factory == null || !(factory.lengthExpression() instanceof QinIrNumberLiteral numberLiteral)) {
            return null;
        }
        double value = numberLiteral.value();
        if (value < 0 || value != Math.rint(value) || value > Integer.MAX_VALUE) {
            return null;
        }
        return (int) value;
    }

    private boolean isDefaultArrayValueExpression(QinIrExpression expression, QinIrTypeRef componentType) {
        if (expression instanceof QinIrNullLiteral) {
            return componentType != null
                    && (componentType.kind() == QinIrTypeKind.CLASS || componentType.kind() == QinIrTypeKind.STRING);
        }
        if (expression instanceof QinIrNumberLiteral numberLiteral
                && componentType != null
                && (componentType.kind() == QinIrTypeKind.INT || componentType.kind() == QinIrTypeKind.DOUBLE)) {
            return numberLiteral.value() == 0.0d;
        }
        return false;
    }

    private void emitStaticArrayFromFactoryAsTypedArray(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            StaticArrayFromFactory factory,
            QinIrTypeRef storageComponentType) {
        QinIrTypeRef lengthType = emitDeclarationExpression(
                code,
                ownerDeclaration,
                method,
                declarationIndex,
                localFrame,
                factory.lengthExpression());
        coerceValueForTargetType(code, lengthType, QinIrTypeRef.intType());
        emitNewArray(code, storageComponentType);
    }

    private void emitNewArray(java.lang.classfile.CodeBuilder code, QinIrTypeRef componentType) {
        TypeKind primitiveArrayKind = primitiveArrayKind(componentType);
        if (primitiveArrayKind == null) {
            code.anewarray(toReferenceClassDesc(componentType.binaryName()));
        } else {
            code.newarray(primitiveArrayKind);
        }
    }

    private QinIrTypeRef arrayTypeRef(QinIrTypeRef componentType, int dimensions) {
        if (dimensions <= 0) {
            return componentType;
        }
        return QinIrTypeRef.classType(typeBinaryName(componentType) + "[]".repeat(dimensions));
    }

    private String typeBinaryName(QinIrTypeRef type) {
        return switch (type.kind()) {
            case BOOLEAN -> "boolean";
            case INT -> "int";
            case DOUBLE -> "double";
            case STRING -> "java.lang.String";
            case CLASS -> type.binaryName();
            case VOID -> "void";
        };
    }

    private void emitReturnForType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef actualType,
            QinIrTypeRef declaredType) {
        if (declaredType.kind() == QinIrTypeKind.VOID) {
            discardExpressionResult(code, actualType);
            code.return_();
            return;
        }
        if (actualType.kind() == QinIrTypeKind.VOID) {
            emitDefaultArgumentValue(code, declaredType);
            emitRawReturnForType(code, declaredType);
            return;
        }
        coerceValueForTargetType(code, actualType, declaredType);
        emitRawReturnForType(code, declaredType);
    }

    private void emitRawReturnForType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef declaredType) {
        switch (declaredType.kind()) {
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
        int localSlot = parameterSlotStart(method);
        for (var parameter : method.parameters()) {
            if (parameter.name().equals(parameterName)) {
                return new ParameterBinding(parameter, localSlot);
            }
            localSlot += localSlotWidth(parameter.type());
        }
        return null;
    }

    private List<QinIrTypeRef> inferDeclarationArgumentTypes(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            List<QinIrExpression> arguments) {
        List<QinIrTypeRef> types = new ArrayList<>();
        for (QinIrExpression argument : arguments) {
            types.add(inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    argument));
        }
        return List.copyOf(types);
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
        if (expression instanceof QinIrBoundMethodReferenceExpression) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (expression instanceof QinIrCastExpression castExpression) {
            return castTypeRef(castExpression.typeName());
        }
        if (expression instanceof QinIrSequenceExpression sequenceExpression) {
            return inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    sequenceExpression.resultExpression());
        }
        if (expression instanceof QinIrShortCircuitExpression shortCircuitExpression) {
            QinIrTypeRef leftType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    shortCircuitExpression.left());
            QinIrTypeRef rightType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    shortCircuitExpression.right());
            return inferLogicalBuiltinResultType(shortCircuitExpression.operator(), leftType, rightType);
        }
        if (expression instanceof QinIrIdentifierReference identifierReference) {
            if ("Infinity".equals(identifierReference.name())) {
                return QinIrTypeRef.doubleType();
            }
        if ("undefined".equals(identifierReference.name())) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if ("arguments".equals(identifierReference.name())) {
            return QinIrTypeRef.classType("java.lang.Object[]");
        }
        LocalBinding localBinding = localFrame.resolve(identifierReference.name());
        if (localBinding != null) {
            return localBinding.type();
        }
            ParameterBinding parameterBinding = resolveParameterBinding(method, identifierReference.name());
            if (parameterBinding != null) {
                return parameterBinding.parameter().type();
            }
            ResolvedFieldAccess fieldAccess = resolveFieldAccess(
                    ownerDeclaration,
                    identifierReference.name(),
                    declarationIndex);
            if (fieldAccess != null) {
                return fieldAccess.field().type();
            }
            ResolvedFieldAccess enclosingStaticFieldAccess = resolveEnclosingStaticFieldAccess(
                    ownerDeclaration,
                    identifierReference.name(),
                    declarationIndex);
            if (enclosingStaticFieldAccess != null) {
                return enclosingStaticFieldAccess.field().type();
            }
            QinIrClassDeclaration declaredClassReference = resolveDeclaredClassReference(
                    ownerDeclaration,
                    declarationIndex,
                    identifierReference.name());
            if (declaredClassReference != null) {
                return QinIrTypeRef.classType("java.lang.Class");
            }
            if (isQinRuntimeGlobalName(identifierReference.name())) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            throw new IllegalArgumentException(
                    "Unknown declaration identifier: "
                            + identifierReference.name()
                            + "; owner=" + ownerDeclaration.binaryName()
                            + "; method=" + method.name());
        }
        if (expression instanceof QinIrMemberAccessExpression memberAccessExpression) {
            if (isJavaEsmSymbolIteratorAccess(
                    new QinIrIdentifierReference(memberAccessExpression.objectName()),
                    memberAccessExpression.propertyName())) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    new QinIrIdentifierReference(memberAccessExpression.objectName()));
            ResolvedPropertyAccess propertyAccess = resolvePropertyAccess(
                    receiverType,
                    memberAccessExpression.propertyName(),
                    declarationIndex);
            if (propertyAccess == null) {
                ResolvedFieldAccess staticFieldAccess = resolveDeclaredStaticFieldAccess(
                        ownerDeclaration,
                        declarationIndex,
                        memberAccessExpression.objectName(),
                        memberAccessExpression.propertyName());
                if (staticFieldAccess != null) {
                    return staticFieldAccess.field().type();
                }
                ResolvedStaticMethodCall staticGetterAccess = resolveDeclaredStaticGetterAccess(
                        ownerDeclaration,
                        declarationIndex,
                        memberAccessExpression.objectName(),
                        memberAccessExpression.propertyName());
                if (staticGetterAccess != null) {
                    return staticGetterAccess.returnType();
                }
                ResolvedFieldAccess javaStaticFieldAccess = resolveJavaStaticFieldAccess(
                        ownerDeclaration,
                        declarationIndex,
                        new QinIrIdentifierReference(memberAccessExpression.objectName()),
                        memberAccessExpression.propertyName());
                if (javaStaticFieldAccess != null) {
                    return javaStaticFieldAccess.field().type();
                }
                QinIrTypeRef structuralPropertyType =
                        resolveStructuralSlimeAstPropertyType(receiverType, memberAccessExpression.propertyName());
                if (structuralPropertyType != null) {
                    return structuralPropertyType;
                }
                if ("length".equals(memberAccessExpression.propertyName())) {
                    return QinIrTypeRef.doubleType();
                }
                if (isDynamicObjectType(receiverType)
                        || isQinRuntimeMetadataProperty(memberAccessExpression.propertyName())) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                if (receiverType.kind() == QinIrTypeKind.CLASS
                        && "java.lang.Class".equals(receiverType.binaryName())) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                throw new IllegalArgumentException(
                        "Unknown declaration member access type: "
                                + memberAccessExpression.objectName() + "." + memberAccessExpression.propertyName());
            }
            return propertyAccess.propertyType();
        }
        if (expression instanceof QinIrPropertyAccessExpression propertyAccessExpression) {
            if (isJavaEsmSymbolIteratorAccess(
                    propertyAccessExpression.receiver(),
                    propertyAccessExpression.propertyName())) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            if (propertyAccessExpression.receiver() instanceof QinIrIdentifierReference identifierReference) {
                ResolvedFieldAccess staticFieldAccess = resolveDeclaredStaticFieldAccess(
                        ownerDeclaration,
                        declarationIndex,
                        identifierReference.name(),
                        propertyAccessExpression.propertyName());
                if (staticFieldAccess != null) {
                    return staticFieldAccess.field().type();
                }
                ResolvedStaticMethodCall staticGetterAccess = resolveDeclaredStaticGetterAccess(
                        ownerDeclaration,
                        declarationIndex,
                        identifierReference.name(),
                        propertyAccessExpression.propertyName());
                if (staticGetterAccess != null) {
                    return staticGetterAccess.returnType();
                }
            }
            if (propertyAccessExpression.receiver() instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
                ResolvedFieldAccess staticFieldAccess = resolveDeclaredStaticFieldAccess(
                        ownerDeclaration,
                        declarationIndex,
                        classLiteralExpression.binaryName(),
                        propertyAccessExpression.propertyName());
                if (staticFieldAccess == null) {
                    staticFieldAccess = resolveDeclaredStaticFieldAccess(
                            ownerDeclaration,
                            declarationIndex,
                            classLiteralExpression.typeName(),
                            propertyAccessExpression.propertyName());
                }
                if (staticFieldAccess != null) {
                    return staticFieldAccess.field().type();
                }
                ResolvedStaticMethodCall staticGetterAccess = resolveDeclaredStaticGetterAccess(
                        ownerDeclaration,
                        declarationIndex,
                        classLiteralExpression.binaryName(),
                        propertyAccessExpression.propertyName());
                if (staticGetterAccess == null) {
                    staticGetterAccess = resolveDeclaredStaticGetterAccess(
                            ownerDeclaration,
                            declarationIndex,
                            classLiteralExpression.typeName(),
                            propertyAccessExpression.propertyName());
                }
                if (staticGetterAccess != null) {
                    return staticGetterAccess.returnType();
                }
            }
            ResolvedFieldAccess javaStaticFieldAccess = resolveJavaStaticFieldAccess(
                    ownerDeclaration,
                    declarationIndex,
                    propertyAccessExpression.receiver(),
                    propertyAccessExpression.propertyName());
            if (javaStaticFieldAccess != null) {
                return javaStaticFieldAccess.field().type();
            }
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
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    propertyAccessExpression.receiver());
            QinIrTypeRef structuralPropertyType =
                    resolveStructuralSlimeAstPropertyType(receiverType, propertyAccessExpression.propertyName());
            if (structuralPropertyType != null) {
                return structuralPropertyType;
            }
            if (isDynamicObjectType(receiverType)
                    || isQinRuntimeMetadataProperty(propertyAccessExpression.propertyName())) {
                return QinIrTypeRef.classType("java.lang.Object");
                }
                if (hasLocalInstanceMethodNamed(receiverType, propertyAccessExpression.propertyName(), declarationIndex)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                if (isCurrentOrLocalDeclarationReceiver(receiverType, declarationIndex)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                if ("length".equals(propertyAccessExpression.propertyName())) {
                    return QinIrTypeRef.doubleType();
                }
                throw new IllegalArgumentException(
                        "Unknown declaration property access type: " + propertyAccessExpression.propertyName());
            }
            return propertyAccess.propertyType();
        }
        if (expression instanceof QinIrElementAccessExpression elementAccessExpression) {
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    elementAccessExpression.receiver());
            if (isStaticCollectionElementAccessType(receiverType)) {
                return staticCollectionElementType(receiverType);
            }
            if (isPrimitiveArrayType(receiverType)) {
                return staticArrayElementType(receiverType);
            }
            if (isObjectArrayType(receiverType)) {
                return staticArrayElementType(receiverType);
            }
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if (expression instanceof QinIrArrayLiteral) {
            return QinIrTypeRef.classType("java.util.ArrayList");
        }
        if (expression instanceof QinIrArrayCreationExpression arrayCreationExpression) {
            return arrayTypeRef(
                    arrayCreationExpression.componentType(),
                    arrayCreationExpression.dimensions().size() + arrayCreationExpression.trailingEmptyDimensions());
        }
        if (expression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            QinIrTypeRef javaLangStringHelperType = inferJavaLangStringSdkHelperReturnType(methodCallExpression);
            if (javaLangStringHelperType != null) {
                return javaLangStringHelperType;
            }
            QinIrTypeRef javaLangNumberHelperType = inferJavaLangNumberSdkHelperReturnType(methodCallExpression);
            if (javaLangNumberHelperType != null) {
                return javaLangNumberHelperType;
            }
            if (isQinArrayFromCall(methodCallExpression)) {
                return QinIrTypeRef.classType("java.lang.Object[]");
            }
            if (staticArrayFromNullFactoryLength(methodCallExpression) != null) {
                return QinIrTypeRef.classType("java.lang.Object[]");
            }
            if (staticArrayFromFactory(methodCallExpression) != null) {
                return QinIrTypeRef.classType("java.lang.Object[]");
            }
            if (isJavaUtilArraysStreamFacadeCall(methodCallExpression)) {
                return javaUtilArraysStreamReturnType(
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        methodCallExpression.arguments().get(0));
            }
            if (isJavaUtilArraysToStringFacadeCall(methodCallExpression)) {
                return QinIrTypeRef.stringType();
            }
            if (isJavaUtilArraysCopyOfFacadeCall(methodCallExpression)) {
                return QinIrTypeRef.classType("java.lang.Object[]");
            }
            if (isJavaUtilArraysFillFacadeCall(methodCallExpression)) {
                return QinIrTypeRef.voidType();
            }
            if (isJavaUtilArraysSortRangeFacadeCall(methodCallExpression)) {
                return QinIrTypeRef.voidType();
            }
            QinIrTypeRef receiverType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.receiver());
            QinIrTypeRef ascribedReceiverType =
                    ascribedInstanceMethodReceiverType(methodCallExpression, receiverType, declarationIndex);
            if (ascribedReceiverType != null) {
                receiverType = ascribedReceiverType;
            }
            if (isObjectArraySortCall(receiverType, methodCallExpression)) {
                return QinIrTypeRef.classType("java.lang.Object[]");
            }
            List<QinIrTypeRef> argumentTypes = inferDeclarationArgumentTypes(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    methodCallExpression.arguments());
            QinIrTypeRef javaMapMethodReturnType = inferJavaMapMethodReturnType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    receiverType,
                    methodCallExpression);
            if (javaMapMethodReturnType != null) {
                return javaMapMethodReturnType;
            }
            QinIrTypeRef javaMapEntryMethodReturnType =
                    javaMapEntryMethodReturnType(receiverType, methodCallExpression);
            if (javaMapEntryMethodReturnType != null) {
                return javaMapEntryMethodReturnType;
            }
            ResolvedInstanceMethodCall resolvedMethod = resolveInstanceMethodCall(
                    receiverType,
                    methodCallExpression.methodName(),
                    argumentTypes,
                    methodCallExpression.arguments(),
                    declarationIndex);
            if (resolvedMethod == null) {
                if (methodCallExpression.receiver() instanceof QinIrThisExpression) {
                    ResolvedStaticMethodCall staticMethod = resolveStaticMethodCall(
                            ownerDeclaration.binaryName(),
                            methodCallExpression.methodName(),
                            argumentTypes,
                            declarationIndex);
                    if (staticMethod != null) {
                        return staticMethod.returnType();
                    }
                }
                if (methodCallExpression.receiver() instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
                    String classLiteralBinaryName = resolveJavaClassLiteralBinaryName(
                            ownerDeclaration,
                            declarationIndex,
                            classLiteralExpression);
                    ResolvedStaticMethodCall staticMethod = resolveStaticMethodCall(
                            classLiteralBinaryName,
                            methodCallExpression.methodName(),
                            argumentTypes,
                            declarationIndex);
                    if (staticMethod != null) {
                        return staticMethod.returnType();
                    }
                    QinIrClassDeclaration generatedEnumReference = declarationIndex.get(classLiteralBinaryName);
                    if (generatedEnumReference != null
                            && isGeneratedEnumLikeType(
                                    QinIrTypeRef.classType(generatedEnumReference.binaryName()),
                                    declarationIndex)) {
                        if (isGeneratedEnumValueOfCall(methodCallExpression)) {
                            return QinIrTypeRef.classType(generatedEnumReference.binaryName());
                        }
                        if (isGeneratedEnumValuesCall(methodCallExpression)) {
                            return QinIrTypeRef.classType("java.lang.Object[]");
                        }
                    }
                }
                if (methodCallExpression.receiver() instanceof QinIrIdentifierReference identifierReference) {
                    QinIrClassDeclaration declaredClassReference = resolveDeclaredClassReference(
                            ownerDeclaration,
                            declarationIndex,
                            identifierReference.name());
                    if (declaredClassReference != null) {
                        ResolvedStaticMethodCall staticMethod = resolveStaticMethodCall(
                                declaredClassReference.binaryName(),
                                methodCallExpression.methodName(),
                                argumentTypes,
                                declarationIndex);
                        if (staticMethod != null) {
                            return staticMethod.returnType();
                        }
                        if (isGeneratedEnumValueOfCall(methodCallExpression)) {
                            return QinIrTypeRef.classType(declaredClassReference.binaryName());
                        }
                        if (isGeneratedEnumValuesCall(methodCallExpression)) {
                            return QinIrTypeRef.classType("java.lang.Object[]");
                        }
                    }
                }
                ResolvedStaticMethodCall enclosingStaticMethod = resolveEnclosingStaticMethodCall(
                        receiverType,
                        methodCallExpression.methodName(),
                        methodCallExpression.arguments().size(),
                        declarationIndex);
                if (enclosingStaticMethod != null) {
                    return enclosingStaticMethod.returnType();
                }
                if (canUseDynamicEnclosingStaticMethod(receiverType)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                if (isStringIndexOfCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.doubleType();
                }
                if (isStringReplaceCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.stringType();
                }
                QinIrTypeRef javaStringInstanceReturnType = inferJavaStringInstanceMethodReturnType(
                        receiverType,
                        methodCallExpression);
                if (javaStringInstanceReturnType != null) {
                    return javaStringInstanceReturnType;
                }
                QinIrTypeRef javaNumberValueReturnType = javaNumberValueInstanceMethodReturnType(
                        receiverType,
                        methodCallExpression);
                if (javaNumberValueReturnType != null) {
                    return javaNumberValueReturnType;
                }
                if (isJavaStreamSumCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.doubleType();
                }
                if (isJavaStreamBoxedCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.classType("java.util.stream.Stream");
                }
                if (isJavaStreamMatchCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.booleanType();
                }
                QinIrTypeRef javaStreamMethodReturnType = javaStreamMethodReturnType(receiverType, methodCallExpression);
                if (javaStreamMethodReturnType != null) {
                    return javaStreamMethodReturnType;
                }
                QinIrTypeRef javaIterableMethodReturnType = javaIterableMethodReturnType(receiverType, methodCallExpression);
                if (javaIterableMethodReturnType != null) {
                    return javaIterableMethodReturnType;
                }
                QinIrTypeRef javaIteratorMethodReturnType = javaIteratorMethodReturnType(receiverType, methodCallExpression);
                if (javaIteratorMethodReturnType != null) {
                    return javaIteratorMethodReturnType;
                }
                QinIrTypeRef javaOptionalMethodReturnType = javaOptionalMethodReturnType(receiverType, methodCallExpression);
                if (javaOptionalMethodReturnType != null) {
                    return javaOptionalMethodReturnType;
                }
                QinIrTypeRef javaOptionalIntMethodReturnType =
                        javaOptionalIntMethodReturnType(receiverType, methodCallExpression);
                if (javaOptionalIntMethodReturnType != null) {
                    return javaOptionalIntMethodReturnType;
                }
                if (isJavaSecurityMessageDigestUpdateFacadeCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.voidType();
                }
                if (isJavaSecurityMessageDigestDigestFacadeCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.classType("[B");
                }
                if (isJavaUtilHexFormatFormatHexFacadeCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.stringType();
                }
                if (isJavaClassToStringCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.stringType();
                }
                if (isJavaClassReflectMethodLookupCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.classType("java.lang.reflect.Method");
                }
                QinIrTypeRef generatedEnumMetadataReturnType =
                        generatedEnumMetadataMethodReturnType(methodCallExpression);
                if (generatedEnumMetadataReturnType != null
                        && isGeneratedEnumLikeType(receiverType, declarationIndex)) {
                    return generatedEnumMetadataReturnType;
                }
                if (isNumberPrototypeMethodCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.stringType();
                }
                if (isObjectArrayType(receiverType)
                        && "join".equals(methodCallExpression.methodName())
                        && methodCallExpression.arguments().size() == 1) {
                    return QinIrTypeRef.stringType();
                }
                if (isObjectArrayCloneCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.classType("java.lang.Object[]");
                }
                if (isObjectArraySliceCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.classType("java.lang.Object[]");
                }
                if (isObjectArrayType(receiverType)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                if (isJavaListDynamicArrayMethodCall(receiverType, methodCallExpression)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                if (isDynamicObjectType(receiverType)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                if (canUseDynamicGeneratedInstanceMethod(receiverType)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                if (isCurrentOrLocalDeclarationReceiver(receiverType, declarationIndex)) {
                    return QinIrTypeRef.classType("java.lang.Object");
                }
                throw new IllegalArgumentException(
                        "Unknown declaration instance method type: "
                                + receiverType.binaryName() + "." + methodCallExpression.methodName()
                                + "; owner=" + ownerDeclaration.binaryName()
                                + "; method=" + method.name()
                                + "; receiverExpression=" + methodCallExpression.receiver()
                                + "; expression=" + methodCallExpression
                                + "; argumentCount=" + methodCallExpression.arguments().size()
                                + "; currentReceiver=" + isCurrentDeclarationReceiver(ownerDeclaration, receiverType)
                                + "; receiverKind=" + receiverType.kind()
                                + "; receiverBinaryName=" + receiverType.binaryName()
                                + "; ownerMethods=" + ownerDeclaration.methods().stream()
                                        .map(candidate -> candidate.name()
                                                + ":static=" + candidate.staticMethod()
                                                + ":params=" + candidate.parameters().size())
                                        .toList());
            }
            QinIrTypeRef javaStreamMethodReturnType = javaStreamMethodReturnType(receiverType, methodCallExpression);
            if (javaStreamMethodReturnType != null) {
                return javaStreamMethodReturnType;
            }
            QinIrTypeRef javaIterableMethodReturnType = javaIterableMethodReturnType(receiverType, methodCallExpression);
            if (javaIterableMethodReturnType != null) {
                return javaIterableMethodReturnType;
            }
            QinIrTypeRef javaIteratorMethodReturnType = javaIteratorMethodReturnType(receiverType, methodCallExpression);
            if (javaIteratorMethodReturnType != null) {
                return javaIteratorMethodReturnType;
            }
            QinIrTypeRef javaOptionalMethodReturnType = javaOptionalMethodReturnType(receiverType, methodCallExpression);
            if (javaOptionalMethodReturnType != null) {
                return javaOptionalMethodReturnType;
            }
            QinIrTypeRef javaOptionalIntMethodReturnType =
                    javaOptionalIntMethodReturnType(receiverType, methodCallExpression);
            return javaOptionalIntMethodReturnType == null ? resolvedMethod.returnType() : javaOptionalIntMethodReturnType;
        }
        if (expression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            if (isJavaSecurityMessageDigestGetInstanceStaticFacadeCall(staticMethodCallExpression)) {
                return QinIrTypeRef.classType("java.security.MessageDigest");
            }
            if (isJavaUtilHexFormatOfStaticFacadeCall(staticMethodCallExpression)) {
                return QinIrTypeRef.classType("java.util.HexFormat");
            }
            QinIrTypeRef javaLangNumberHelperType =
                    inferJavaLangNumberStaticSdkHelperReturnType(staticMethodCallExpression);
            if (javaLangNumberHelperType != null) {
                return javaLangNumberHelperType;
            }
            if (isJavaUtilArraysStreamStaticFacadeCall(staticMethodCallExpression)) {
                return javaUtilArraysStreamReturnType(
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        staticMethodCallExpression.arguments().get(0));
            }
            if (isJavaUtilArraysToStringStaticFacadeCall(staticMethodCallExpression)) {
                return QinIrTypeRef.stringType();
            }
            if (isJavaUtilArraysAsListArrayStaticFacadeCall(staticMethodCallExpression)) {
                return QinIrTypeRef.classType("java.util.ArrayList");
            }
            if (isJavaUtilArraysCopyOfStaticFacadeCall(staticMethodCallExpression)) {
                return QinIrTypeRef.classType("java.lang.Object[]");
            }
            if (isJavaUtilArraysFillStaticFacadeCall(staticMethodCallExpression)
                    || isJavaUtilArraysSortRangeStaticFacadeCall(staticMethodCallExpression)) {
                return QinIrTypeRef.voidType();
            }
            if (isJavaUtilCollectionsNCopiesStaticFacadeCall(staticMethodCallExpression)) {
                QinIrTypeRef valueType = inferDeclarationExpressionType(
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        staticMethodCallExpression.arguments().get(1));
                return QinIrTypeRef.classType("java.util.List", List.of(boxForObjectStorage(valueType)));
            }
            String effectiveOwnerBinaryName = effectiveLocalReferenceBinaryName(
                    staticMethodCallExpression.ownerBinaryName(),
                    declarationIndex);
            QinIrClassDeclaration generatedEnumReference = declarationIndex.get(effectiveOwnerBinaryName);
            if (generatedEnumReference != null
                    && isGeneratedEnumLikeType(
                            QinIrTypeRef.classType(generatedEnumReference.binaryName()),
                            declarationIndex)) {
                if (isGeneratedEnumValueOfCall(staticMethodCallExpression)) {
                    return QinIrTypeRef.classType(generatedEnumReference.binaryName());
                }
                if (isGeneratedEnumValuesCall(staticMethodCallExpression)) {
                    return QinIrTypeRef.classType("java.lang.Object[]");
                }
            }
            ResolvedStaticMethodCall resolvedMethod = resolveStaticMethodCall(
                    staticMethodCallExpression.ownerBinaryName(),
                    staticMethodCallExpression.methodName(),
                    inferDeclarationArgumentTypes(
                            ownerDeclaration,
                            method,
                            declarationIndex,
                            localFrame,
                            staticMethodCallExpression.arguments()),
                    staticMethodCallExpression.arguments(),
                    declarationIndex);
            if (resolvedMethod == null) {
                throw new IllegalArgumentException(
                        "Unknown declaration static method type: "
                                + staticMethodCallExpression.ownerBinaryName()
                                + "." + staticMethodCallExpression.methodName()
                                + "; " + staticMethodResolutionDiagnostic(
                                        staticMethodCallExpression.ownerBinaryName(),
                                        staticMethodCallExpression.methodName(),
                                        inferDeclarationArgumentTypes(
                                                ownerDeclaration,
                                                method,
                                                declarationIndex,
                                                localFrame,
                                                staticMethodCallExpression.arguments()),
                                        declarationIndex));
            }
            return resolvedMethod.returnType();
        }
        if (expression instanceof QinIrSuperMethodCallExpression superMethodCallExpression) {
            if (ownerDeclaration.superType() == null || ownerDeclaration.superType().binaryName() == null) {
                return QinIrTypeRef.classType("java.lang.Object");
            }
            ResolvedInstanceMethodCall resolvedMethod = resolveInstanceMethodCall(
                    ownerDeclaration.superType(),
                    superMethodCallExpression.methodName(),
                    superMethodCallExpression.arguments().size(),
                    declarationIndex);
            return resolvedMethod == null ? QinIrTypeRef.classType("java.lang.Object") : resolvedMethod.returnType();
        }
        if (expression instanceof QinIrJavaNewExpression javaNewExpression) {
            return QinIrTypeRef.classType(canonicalJavaSdkAliasBinaryName(javaNewExpression.ownerBinaryName()));
        }
        if (expression instanceof QinIrJavaClassLiteralExpression) {
            return QinIrTypeRef.classType("java.lang.Class");
        }
        if (expression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return inferBuiltinCallResultType(ownerDeclaration, method, declarationIndex, localFrame, builtinCallExpression);
        }
        if (expression instanceof QinIrSpreadArgumentExpression spreadArgumentExpression) {
            return inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    spreadArgumentExpression.expression());
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
        if (expression instanceof QinIrUpdateExpression updateExpression) {
            if (updateExpression.target() instanceof QinIrIdentifierReference identifierReference) {
                LocalBinding binding = localFrame.resolve(identifierReference.name());
                if (binding != null) {
                    return binding.type();
                }
            }
            if (updateExpression.target() instanceof QinIrPropertyAccessExpression propertyAccessExpression
                    && propertyAccessExpression.receiver() instanceof QinIrThisExpression) {
                ResolvedFieldAccess fieldAccess = resolveFieldAccess(
                        ownerDeclaration,
                        propertyAccessExpression.propertyName(),
                        declarationIndex);
                if (fieldAccess != null) {
                    return fieldAccess.field().type();
                }
            }
            if (updateExpression.target() instanceof QinIrElementAccessExpression elementAccessExpression) {
                QinIrTypeRef receiverType = inferDeclarationExpressionType(
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        elementAccessExpression.receiver());
                if (isPrimitiveArrayType(receiverType) || isObjectArrayType(receiverType)) {
                    return staticArrayElementType(receiverType);
                }
                if (isStaticCollectionElementAccessType(receiverType)) {
                    return staticCollectionElementType(receiverType);
                }
            }
            return QinIrTypeRef.classType("java.lang.Object");
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
                case "-", "*", "/" , "%" -> QinIrTypeRef.doubleType();
                case "|", "&", "^" -> isBooleanLike(leftType) && isBooleanLike(rightType)
                        ? QinIrTypeRef.booleanType()
                        : QinIrTypeRef.doubleType();
                case "<<", ">>", ">>>" -> QinIrTypeRef.doubleType();
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
        if ("__qin_unary__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2
                && builtinCallExpression.arguments().get(0) instanceof QinIrStringLiteral operatorLiteral) {
            return switch (operatorLiteral.value()) {
                case "!" -> QinIrTypeRef.booleanType();
                case "+", "-", "~" -> QinIrTypeRef.doubleType();
                case "typeof" -> QinIrTypeRef.stringType();
                case "void" -> QinIrTypeRef.classType("java.lang.Object");
                default -> QinIrTypeRef.classType("java.lang.Object");
            };
        }
        if ("__qin_instanceof__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            return QinIrTypeRef.booleanType();
        }
        if ("__qin_structural_object__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            return QinIrTypeRef.booleanType();
        }
        if ("__qin_conditional__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 3) {
            QinIrExpression consequent = builtinCallExpression.arguments().get(1);
            QinIrExpression alternate = builtinCallExpression.arguments().get(2);
            if (isNullOrUndefinedLiteral(consequent)) {
                return nullableConditionalBranchType(inferDeclarationExpressionType(
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        alternate));
            }
            if (isNullOrUndefinedLiteral(alternate)) {
                return nullableConditionalBranchType(inferDeclarationExpressionType(
                        ownerDeclaration,
                        method,
                        declarationIndex,
                        localFrame,
                        consequent));
            }
            QinIrTypeRef consequentType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    consequent);
            QinIrTypeRef alternateType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    alternate);
            return mergeBranchTypes(consequentType, alternateType);
        }
        if ("__qin_string__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            return QinIrTypeRef.stringType();
        }
        if ("__qin_number__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() <= 1) {
            return QinIrTypeRef.doubleType();
        }
        if ("__qin_java_new_array__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            return inferQinJavaNewArrayType(builtinCallExpression);
        }
        if ("__qin_call__".equals(builtinCallExpression.methodName())
                && !builtinCallExpression.arguments().isEmpty()) {
            QinIrTypeRef callableType = inferDeclarationCallableType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(0));
            if (callableType != null) {
                return callableType;
            }
            return QinIrTypeRef.classType("java.lang.Object");
        }
        if ("__qin_collection_get__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2) {
            QinIrTypeRef collectionType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(0));
            return staticCollectionElementType(collectionType);
        }
        if ("__qin_collection_to_array__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 1) {
            QinIrTypeRef collectionType = inferDeclarationExpressionType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression.arguments().get(0));
            QinIrTypeRef elementType = staticCollectionElementType(collectionType);
            return isJavaLangObjectType(elementType)
                    ? QinIrTypeRef.classType("java.lang.Object[]")
                    : QinIrTypeRef.classType("java.lang.Object[]", List.of(elementType));
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

    private QinIrTypeRef inferQinJavaNewArrayType(QinIrBuiltinCallExpression builtinCallExpression) {
        QinIrExpression typeExpression = builtinCallExpression.arguments().get(0);
        if (!(typeExpression instanceof QinIrStringLiteral typeLiteral)) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return staticArrayTypeFromName(typeLiteral.value());
    }

    private QinIrTypeRef staticArrayTypeFromName(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        String name = rawName.trim();
        if (name.startsWith("[")) {
            return QinIrTypeRef.classType(name.replace('/', '.'));
        }
        if (name.endsWith("[]")) {
            return QinIrTypeRef.classType(name);
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef inferDeclarationCallableType(
            QinIrClassDeclaration ownerDeclaration,
            QinIrMethodDeclaration method,
            Map<String, QinIrClassDeclaration> declarationIndex,
            LocalFrame localFrame,
            QinIrExpression callableExpression) {
        if (callableExpression instanceof QinIrIdentifierReference identifierReference) {
            return inferDeclarationCallableType(identifierReference.name());
        }
        if (callableExpression instanceof QinIrBuiltinCallExpression builtinCallExpression) {
            return inferBuiltinCallResultType(
                    ownerDeclaration,
                    method,
                    declarationIndex,
                    localFrame,
                    builtinCallExpression);
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef inferDeclarationCallableType(String callableName) {
        if (callableName == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return switch (callableName) {
            case "__qin_java_hash_key_equals__",
                    "__qin_java_tree_set_is_comparator__",
                    "__qin_java_implements" -> QinIrTypeRef.booleanType();
            case "__qin_java_tree_set_compare__" -> QinIrTypeRef.intType();
            case "__qin_java_hash_key__" -> QinIrTypeRef.stringType();
            default -> QinIrTypeRef.classType("java.lang.Object");
        };
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
                  case "__qin_java_string_hash_code__",
                          "__qin_java_identity_hash_code__",
                          "__qin_java_value_hash_code__",
                          "__qin_java_long_hash_code__" -> QinIrTypeRef.doubleType();
                  case "__qin_java_values_equal__",
                          "__qin_java_hash_key_equals__",
                          "__qin_java_implements" -> QinIrTypeRef.booleanType();
                  case "__qin_java_class_info__" -> QinIrTypeRef.classType("java.lang.Class");
                  case "__qin_java_hash_key__" -> QinIrTypeRef.stringType();
                  case "__qin_java_regex_pattern_compile__",
                          "__qin_java_regex_matcher__" -> QinIrTypeRef.classType("java.lang.Object");
                  case "__qin_subhuti_identity_rule_cache_id",
                          "__qin_subhuti_value_rule_cache_id" -> QinIrTypeRef.doubleType();
                  case "__qin_subhuti_rule_cache_key" -> QinIrTypeRef.stringType();
                  case "__qin_direct_method_function__" -> QinIrTypeRef.classType("java.lang.Object");
                  case "__qin_java_functional" ->
                          QinIrTypeRef.classType("com.qin.lang.runtime.JavaEsmGlobal$JavaFunctionalObject");
                  case "__qin_array_from_constant__" -> QinIrTypeRef.classType("java.lang.Object[]");
                  case "__qin_token_name_of__",
                          "__qin_token_value_of__",
                          "__qin_token_index_of__" -> QinIrTypeRef.stringType();
      case "__qin_token_has_line_break_before__" -> QinIrTypeRef.booleanType();
      case "__qin_static_length__" -> QinIrTypeRef.intType();
      case "__qin_collection_size__" -> QinIrTypeRef.doubleType();
                  case "__qin_collection_is_empty__", "__qin_collection_contains__", "__qin_collection_add__" ->
                          QinIrTypeRef.booleanType();
                  case "__qin_collection_get__" -> QinIrTypeRef.classType("java.lang.Object");
                  case "__qin_array_append__", "__qin_array_prepend__", "__qin_array_remove_at__",
                          "__qin_array_slice__", "__qin_array_sort__", "__qin_collection_to_array__" ->
                          QinIrTypeRef.classType("java.lang.Object[]");
                  default -> null;
              };
            case "Object" -> switch (methodName) {
                case "keys", "values", "entries" -> QinIrTypeRef.classType("java.lang.Object[]");
                case "hasOwn" -> QinIrTypeRef.booleanType();
                default -> null;
            };
            case "Array" -> "isArray".equals(methodName) ? QinIrTypeRef.booleanType() : null;
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
                if (returnDescriptor.startsWith("[")) {
                    yield QinIrTypeRef.classType(returnDescriptor.replace('/', '.'));
                }
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
            case "&&", "||" -> isBooleanLike(leftType) && isBooleanLike(rightType)
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
        QinIrTypeRef genericBranchType = mergeGenericErasedBranchType(leftType, rightType);
        if (genericBranchType != null) {
            return genericBranchType;
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef nullableConditionalBranchType(QinIrTypeRef nonNullType) {
        if (nonNullType == null) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return switch (nonNullType.kind()) {
            case BOOLEAN -> QinIrTypeRef.classType("java.lang.Boolean");
            case INT -> QinIrTypeRef.classType("java.lang.Integer");
            case DOUBLE -> QinIrTypeRef.classType("java.lang.Double");
            case VOID -> QinIrTypeRef.classType("java.lang.Object");
            default -> nonNullType;
        };
    }

    private boolean isReferenceStorageType(QinIrTypeRef type) {
        return type != null
                && (type.kind() == QinIrTypeKind.CLASS
                || type.kind() == QinIrTypeKind.STRING);
    }

    private QinIrTypeRef mergeGenericErasedBranchType(QinIrTypeRef leftType, QinIrTypeRef rightType) {
        if (leftType == null || rightType == null) {
            return null;
        }
        if (leftType.kind() != QinIrTypeKind.CLASS || rightType.kind() != QinIrTypeKind.CLASS) {
            return null;
        }
        String leftName = QinJavaSdkAliasSupport.canonicalBinaryName(leftType.binaryName());
        String rightName = QinJavaSdkAliasSupport.canonicalBinaryName(rightType.binaryName());
        if (!Objects.equals(leftName, rightName)) {
            return null;
        }
        if (leftType.typeArguments().isEmpty() && !rightType.typeArguments().isEmpty()) {
            return QinIrTypeRef.classType(rightName, rightType.typeArguments());
        }
        if (rightType.typeArguments().isEmpty() && !leftType.typeArguments().isEmpty()) {
            return QinIrTypeRef.classType(leftName, leftType.typeArguments());
        }
        return null;
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
        if (ownerType.kind() == QinIrTypeKind.STRING && "length".equals(propertyName)) {
            return new ResolvedPropertyAccess(
                    "java.lang.String",
                    "length",
                    QinIrTypeRef.intType(),
                    false);
        }
        if ("length".equals(propertyName) && isAnyArrayType(ownerType)) {
            return new ResolvedPropertyAccess(null, null, QinIrTypeRef.intType(), false);
        }

        QinIrClassDeclaration localDeclaration = declarationIndex.get(ownerType.binaryName());
        if (localDeclaration != null) {
            if (!visitedLocalTypes.add(ownerType.binaryName())) {
                return null;
            }
            for (QinIrFieldDeclaration field : localDeclaration.fields()) {
                if (fieldNameMatches(field.name(), propertyName)) {
                    return new ResolvedPropertyAccess(
                            ownerType.binaryName(),
                            getterName(field),
                            field.type(),
                            localDeclaration.interfaceClass());
                }
            }
            for (QinIrMethodDeclaration declarationMethod : localDeclaration.methods()) {
                if (!declarationMethod.staticMethod()
                        && declarationMethod.name().equals(propertyName)
                        && declarationMethod.parameters().isEmpty()) {
                    return new ResolvedPropertyAccess(
                            ownerType.binaryName(),
                            declarationMethod.name(),
                            declarationMethod.returnType(),
                            localDeclaration.interfaceClass());
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

        ResolvedPropertyAccess javaLengthAccess = resolveJavaLengthPropertyAccessOrNull(ownerType, propertyName);
        if (javaLengthAccess != null) {
            return javaLengthAccess;
        }

        try {
            Class<?> ownerClass = Class.forName(ownerType.binaryName());
            String capitalized = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
            try {
                var getter = ownerClass.getMethod("get" + capitalized);
                return new ResolvedPropertyAccess(
                        ownerClass.getName(),
                        getter.getName(),
                        toQinReflectedFieldTypeRef(getter.getReturnType()),
                        ownerClass.isInterface());
            } catch (NoSuchMethodException ignored) {
                var getter = ownerClass.getMethod("is" + capitalized);
                return new ResolvedPropertyAccess(
                        ownerClass.getName(),
                        getter.getName(),
                        toQinReflectedFieldTypeRef(getter.getReturnType()),
                        ownerClass.isInterface());
            }
        } catch (Throwable ignored) {
            return null;
        }
    }

    private ResolvedMutablePropertyAccess resolveMutablePropertyAccess(
            QinIrTypeRef ownerType,
            String propertyName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveMutablePropertyAccess(ownerType, propertyName, declarationIndex, new java.util.LinkedHashSet<>());
    }

    private ResolvedMutablePropertyAccess resolveMutablePropertyAccess(
            QinIrTypeRef ownerType,
            String propertyName,
            Map<String, QinIrClassDeclaration> declarationIndex,
            java.util.Set<String> visitedLocalTypes) {
        if (ownerType.kind() != QinIrTypeKind.CLASS) {
            return null;
        }
        QinIrClassDeclaration localDeclaration = declarationIndex.get(ownerType.binaryName());
        if (localDeclaration != null) {
            if (!visitedLocalTypes.add(ownerType.binaryName())) {
                return null;
            }
            for (QinIrFieldDeclaration field : localDeclaration.fields()) {
                if (fieldNameMatches(field.name(), propertyName)) {
                    return new ResolvedMutablePropertyAccess(
                            ownerType,
                            ownerType.binaryName(),
                            propertyName,
                            getterName(field),
                            setterName(field),
                            field.type(),
                            localDeclaration.interfaceClass());
                }
            }
            return localDeclaration.superType() == null
                    ? null
                    : resolveMutablePropertyAccess(
                            localDeclaration.superType(),
                            propertyName,
                            declarationIndex,
                            visitedLocalTypes);
        }

        try {
            Class<?> ownerClass = Class.forName(ownerType.binaryName());
            String capitalized = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
            java.lang.reflect.Method getter = null;
            try {
                getter = ownerClass.getMethod("get" + capitalized);
            } catch (NoSuchMethodException ignored) {
                try {
                    getter = ownerClass.getMethod("is" + capitalized);
                } catch (NoSuchMethodException ignoredAgain) {
                    getter = null;
                }
            }
            if (getter == null) {
                return null;
            }
            String setterName = "set" + capitalized;
            for (java.lang.reflect.Method setter : ownerClass.getMethods()) {
                if (!setter.getName().equals(setterName)
                        || setter.getParameterCount() != 1
                        || setter.getReturnType() != void.class) {
                    continue;
                }
                QinIrTypeRef setterType = toQinTypeRef(setter.getParameterTypes()[0]);
                return new ResolvedMutablePropertyAccess(
                        ownerType,
                        ownerClass.getName(),
                        propertyName,
                        getter.getName(),
                        setter.getName(),
                        setterType,
                        ownerClass.isInterface());
            }
            return null;
        } catch (Throwable error) {
            if (Boolean.getBoolean("qin.declarationInstanceMethod.trace")) {
                error.printStackTrace(System.err);
            }
            return null;
        }
    }

    private ResolvedPropertyAccess resolveJavaLengthPropertyAccessOrNull(
            QinIrTypeRef ownerType,
            String propertyName) {
        if (!"length".equals(propertyName)
                && !"size".equals(propertyName)
                || ownerType.kind() != QinIrTypeKind.CLASS
                || ownerType.binaryName() == null) {
            return null;
        }
        try {
            Class<?> ownerClass = Class.forName(ownerType.binaryName());
            if (java.util.Collection.class.isAssignableFrom(ownerClass)
                    || java.util.Map.class.isAssignableFrom(ownerClass)) {
                var sizeMethod = ownerClass.getMethod("size");
                return new ResolvedPropertyAccess(
                        ownerClass.getName(),
                        sizeMethod.getName(),
                        QinIrTypeRef.intType(),
                        ownerClass.isInterface());
            }
        } catch (Throwable ignored) {
            return null;
        }
        return null;
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
                null,
                null,
                declarationIndex,
                new java.util.LinkedHashSet<>());
    }

    private ResolvedInstanceMethodCall resolveInstanceMethodCall(
            QinIrTypeRef ownerType,
            String methodName,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveInstanceMethodCall(
                ownerType,
                methodName,
                argumentTypes == null ? 0 : argumentTypes.size(),
                argumentTypes,
                null,
                declarationIndex,
                new java.util.LinkedHashSet<>());
    }

    private ResolvedInstanceMethodCall resolveInstanceMethodCall(
            QinIrTypeRef ownerType,
            String methodName,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveInstanceMethodCall(
                ownerType,
                methodName,
                argumentTypes == null ? 0 : argumentTypes.size(),
                argumentTypes,
                argumentExpressions,
                declarationIndex,
                new java.util.LinkedHashSet<>());
    }

    private ResolvedInstanceMethodCall resolveInstanceMethodCall(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions,
            Map<String, QinIrClassDeclaration> declarationIndex,
            java.util.Set<String> visitedLocalTypes) {
        if (ownerType.kind() != QinIrTypeKind.CLASS && ownerType.kind() != QinIrTypeKind.STRING) {
            return null;
        }

        if (ownerType.kind() == QinIrTypeKind.STRING
                && "getBytes".equals(methodName)
                && argumentCount == 1) {
            return new ResolvedInstanceMethodCall(
                    String.class.getName(),
                    "getBytes",
                    List.of(QinIrTypeRef.classType("java.nio.charset.Charset")),
                    QinIrTypeRef.classType("[B"),
                    false,
                    MethodTypeDesc.ofDescriptor("(Ljava/nio/charset/Charset;)[B"),
                    List.of(java.nio.charset.Charset.class),
                    false);
        }
        ResolvedInstanceMethodCall runtimeBuiltinMethod =
                resolveJavaEsmBuiltinInstanceMethodCall(ownerType, methodName, argumentCount);
        if (runtimeBuiltinMethod != null) {
            return runtimeBuiltinMethod;
        }
        boolean traceInstanceMethodResolution = traceDeclarationInstanceMethod(ownerType, methodName);
        QinIrClassDeclaration localDeclaration = resolveIndexedDeclaration(declarationIndex, ownerType.binaryName());
        if (localDeclaration != null) {
            String localOwnerBinaryName = localDeclaration.binaryName();
            if (traceInstanceMethodResolution) {
                System.err.println("[QinJvmDeclarationClassEmitter] local instance lookup owner="
                        + ownerType.binaryName()
                        + " localOwner=" + localOwnerBinaryName
                        + " method=" + methodName
                        + "/" + argumentCount
                        + " args=" + argumentTypes
                        + " declarations=" + localDeclaration.methods().size()
                        + " super=" + localDeclaration.superType());
            }
            if (!visitedLocalTypes.add(localOwnerBinaryName)) {
                return null;
            }
            QinIrMethodDeclaration generatedOverload = hasExactLocalInstanceMethod(
                    localDeclaration,
                    methodName,
                    argumentCount,
                    argumentTypes,
                    declarationIndex)
                            ? null
                            : resolveGeneratedOverloadImplementation(
                                    localDeclaration,
                                    methodName,
                                    argumentCount,
                                    argumentTypes,
                                    argumentExpressions,
                                    declarationIndex,
                                    false);
            if (generatedOverload != null) {
                List<QinIrTypeRef> parameterTypes = new ArrayList<>();
                for (var parameter : generatedOverload.parameters()) {
                    parameterTypes.add(parameter.type());
                }
                if (traceInstanceMethodResolution) {
                    System.err.println("[QinJvmDeclarationClassEmitter] local generated overload result="
                            + localOwnerBinaryName + "." + generatedOverload.name()
                            + "/" + generatedOverload.parameters().size());
                }
                return new ResolvedInstanceMethodCall(
                        localOwnerBinaryName,
                        generatedOverload.name(),
                        List.copyOf(parameterTypes),
                        generatedOverload.returnType(),
                        localDeclaration.interfaceClass(),
                        null,
                        List.of(),
                        hasVarargsParameter(generatedOverload));
            }
            generatedOverload = hasExactLocalInstanceMethod(
                    localDeclaration,
                    methodName,
                    argumentCount,
                    argumentTypes,
                    declarationIndex)
                            ? null
                            : resolveGeneratedOverloadImplementation(
                                    localDeclaration,
                                    methodName,
                                    argumentCount,
                                    argumentTypes,
                                    false);
            if (generatedOverload != null) {
                List<QinIrTypeRef> parameterTypes = new ArrayList<>();
                for (var parameter : generatedOverload.parameters()) {
                    parameterTypes.add(parameter.type());
                }
                if (traceInstanceMethodResolution) {
                    System.err.println("[QinJvmDeclarationClassEmitter] local generated overload result="
                            + localOwnerBinaryName + "." + generatedOverload.name()
                            + "/" + generatedOverload.parameters().size());
                }
                return new ResolvedInstanceMethodCall(
                        localOwnerBinaryName,
                        generatedOverload.name(),
                        List.copyOf(parameterTypes),
                        generatedOverload.returnType(),
                        localDeclaration.interfaceClass(),
                        null,
                        List.of(),
                        hasVarargsParameter(generatedOverload));
            }
            QinIrMethodDeclaration matched = null;
            QinIrMethodDeclaration paddedMatch = null;
            int matchedScore = Integer.MIN_VALUE;
            int paddedMatchScore = Integer.MIN_VALUE;
            for (QinIrMethodDeclaration candidate : localDeclaration.methods()) {
                if (candidate.staticMethod()
                        || !candidate.name().equals(methodName)
                        || !isLocalMethodCountApplicable(candidate.parameters(), argumentCount)) {
                    continue;
                }
                int matchScore = argumentTypes == null
                        ? 0
                        : localMethodMatchScore(
                                candidate.parameters(),
                                argumentTypes,
                                argumentExpressions,
                                declarationIndex);
                if (matchScore < 0) {
                    continue;
                }
                if (candidate.parameters().size() == argumentCount) {
                    if (matched != null && matchScore == matchedScore) {
                        throw new IllegalArgumentException(
                                "Ambiguous declaration method overload: " + ownerType.binaryName() + "." + methodName);
                    }
                    if (matched != null && matchScore < matchedScore) {
                        continue;
                    }
                    matched = candidate;
                    matchedScore = matchScore;
                    continue;
                }
                if (paddedMatch != null && matchScore == paddedMatchScore) {
                    throw new IllegalArgumentException(
                            "Ambiguous declaration method overload: " + ownerType.binaryName() + "." + methodName);
                }
                if (paddedMatch != null && matchScore < paddedMatchScore) {
                    continue;
                }
                paddedMatch = candidate;
                paddedMatchScore = matchScore;
            }
            if (matched == null) {
                matched = paddedMatch;
            }
            if (matched == null) {
                matched = resolveGeneratedOverloadImplementation(
                        localDeclaration,
                        methodName,
                        argumentCount,
                        argumentTypes,
                        false);
            }
            if (matched == null) {
                matched = resolveExactGeneratedOverloadImplementation(
                        localDeclaration,
                        methodName,
                        argumentCount,
                        false);
            }
            if (matched != null) {
                List<QinIrTypeRef> parameterTypes = new ArrayList<>();
                for (var parameter : matched.parameters()) {
                    parameterTypes.add(parameter.type());
                }
                QinIrTypeRef semanticReturnType = matched.returnType();
                MethodTypeDesc descriptor = null;
                if (isSubhutiCstType(ownerType)
                        && "getChildren".equals(methodName)
                        && (argumentCount == 0 || argumentCount == 1)) {
                    semanticReturnType = QinIrTypeRef.classType(
                            "java.util.List",
                            List.of(QinIrTypeRef.classType(localOwnerBinaryName)));
                    descriptor = toMethodDescriptor(matched);
                }
                QinIrTypeRef stringBuilderReturnType =
                        javaLangStringBuilderFacadeReturnType(localOwnerBinaryName, methodName, argumentCount);
                if (stringBuilderReturnType != null) {
                    semanticReturnType = stringBuilderReturnType;
                    descriptor = toMethodDescriptor(matched);
                }
                if (traceInstanceMethodResolution) {
                    System.err.println("[QinJvmDeclarationClassEmitter] local instance lookup result="
                            + localOwnerBinaryName + "." + matched.name()
                            + "/" + matched.parameters().size());
                }
                return new ResolvedInstanceMethodCall(
                            localOwnerBinaryName,
                            matched.name(),
                            List.copyOf(parameterTypes),
                            semanticReturnType,
                            localDeclaration.interfaceClass(),
                            descriptor,
                            List.of(),
                            hasVarargsParameter(matched));
            }
            if (traceInstanceMethodResolution) {
                System.err.println("[QinJvmDeclarationClassEmitter] local instance lookup inherited="
                        + localOwnerBinaryName + " -> " + localDeclaration.superType());
            }
            return localDeclaration.superType() == null
                    ? null
                    : resolveInstanceMethodCall(
                            localDeclaration.superType(),
                            methodName,
                            argumentCount,
                            argumentTypes,
                            argumentExpressions,
                            declarationIndex,
                            visitedLocalTypes);
        }

        if (isSubhutiCstType(ownerType)
                && "getChildren".equals(methodName)
                && (argumentCount == 0 || argumentCount == 1)) {
            return new ResolvedInstanceMethodCall(
                    "com.subhuti.struct.SubhutiCst",
                    "getChildren",
                    argumentCount == 0 ? List.of() : List.of(QinIrTypeRef.stringType()),
                    QinIrTypeRef.classType(
                            "java.util.List",
                            List.of(QinIrTypeRef.classType("com.subhuti.struct.SubhutiCst"))),
                    false,
                    argumentCount == 0
                            ? MethodTypeDesc.ofDescriptor("()Ljava/util/List;")
                            : MethodTypeDesc.ofDescriptor("(Ljava/lang/String;)Ljava/util/List;"),
                    argumentCount == 0 ? List.of() : List.of(String.class),
                    false);
        }
        ResolvedInstanceMethodCall javaListMethod =
                resolveJavaListInstanceMethodCall(ownerType, methodName, argumentCount);
        if (javaListMethod != null) {
            return javaListMethod;
        }
        QinIrTypeRef dequeElementReturnType = javaDequeElementReturnType(ownerType, methodName, argumentCount);
        if (dequeElementReturnType != null) {
            String dequeOwner = canonicalJavaSdkAliasBinaryName(ownerType.binaryName());
            boolean interfaceOwner = "java.util.Deque".equals(dequeOwner);
            return new ResolvedInstanceMethodCall(
                    dequeOwner,
                    methodName,
                    List.of(),
                    dequeElementReturnType,
                    interfaceOwner,
                    MethodTypeDesc.ofDescriptor("()Ljava/lang/Object;"),
                    List.of(),
                    false);
        }
        QinIrTypeRef stringBuilderReturnType =
                javaLangStringBuilderFacadeReturnType(ownerType.binaryName(), methodName, argumentCount);
        if (stringBuilderReturnType != null) {
            List<QinIrTypeRef> parameterTypes = switch (methodName) {
                case "append" -> List.of(QinIrTypeRef.classType("java.lang.Object"));
                case "setLength" -> List.of(QinIrTypeRef.intType());
                default -> List.of();
            };
            List<Class<?>> parameterClasses = switch (methodName) {
                case "append" -> List.of(Object.class);
                case "setLength" -> List.of(int.class);
                default -> List.of();
            };
            return new ResolvedInstanceMethodCall(
                    "java.lang.StringBuilder",
                    methodName,
                    parameterTypes,
                    stringBuilderReturnType,
                    false,
                    switch (methodName) {
                        case "length" -> MethodTypeDesc.ofDescriptor("()I");
                        case "toString" -> MethodTypeDesc.ofDescriptor("()Ljava/lang/String;");
                        case "append" -> MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
                        case "setLength" -> MethodTypeDesc.ofDescriptor("(I)V");
                        default -> null;
                    },
                    parameterClasses,
                    false);
        }

        try {
            Class<?> ownerClass = Class.forName(canonicalJavaSdkAliasBinaryName(ownerType.binaryName()));
            if (traceInstanceMethodResolution) {
                System.err.println("[QinJvmDeclarationClassEmitter] reflected instance lookup owner="
                        + ownerClass.getName()
                        + " method=" + methodName
                        + " args=" + argumentTypes);
            }
            java.lang.reflect.Method matched = argumentTypes == null
                    ? findReflectedMethod(ownerClass, methodName, argumentCount)
                    : findReflectedMethod(ownerClass, methodName, argumentTypes);
            if (traceInstanceMethodResolution) {
                System.err.println("[QinJvmDeclarationClassEmitter] reflected instance lookup result="
                        + (matched == null ? "<null>" : matched.toString()));
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
                    toQinReflectedFieldTypeRef(matched.getReturnType()),
                    ownerClass.isInterface(),
                    toJavaMethodDescriptor(matched.getReturnType(), matched.getParameterTypes()),
                    List.of(matched.getParameterTypes()),
                    matched.isVarArgs());
        } catch (Throwable ignored) {
            if (traceInstanceMethodResolution) {
                System.err.println("[QinJvmDeclarationClassEmitter] reflected instance lookup failed owner="
                        + ownerType.binaryName()
                        + " method=" + methodName
                        + " args=" + argumentTypes
                        + " error=" + ignored);
            }
            return null;
        }
    }

    private ResolvedInstanceMethodCall resolveJavaListInstanceMethodCall(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount) {
        QinIrTypeRef returnType = javaListMethodReturnType(ownerType, methodName, argumentCount);
        if (returnType == null) {
            return null;
        }
        return new ResolvedInstanceMethodCall(
                "java.util.List",
                methodName,
                List.of(QinIrTypeRef.intType(), QinIrTypeRef.classType("java.lang.Object")),
                returnType,
                true,
                MethodTypeDesc.ofDescriptor("(ILjava/lang/Object;)Ljava/lang/Object;"),
                List.of(int.class, Object.class),
                false);
    }

    private QinIrTypeRef javaListMethodReturnType(
            QinIrTypeRef ownerType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        return javaListMethodReturnType(
                ownerType,
                methodCallExpression.methodName(),
                methodCallExpression.arguments().size());
    }

    private QinIrTypeRef javaListMethodReturnType(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount) {
        if (!"set".equals(methodName) || argumentCount != 2 || !isJavaListType(ownerType)) {
            return null;
        }
        return staticCollectionElementType(ownerType);
    }

    private QinIrTypeRef javaIterableMethodReturnType(
            QinIrTypeRef ownerType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (!"iterator".equals(methodCallExpression.methodName())
                || !methodCallExpression.arguments().isEmpty()
                || !isJavaIterableType(ownerType)) {
            return null;
        }
        return QinIrTypeRef.classType(
                "java.util.Iterator",
                List.of(javaIterableElementType(ownerType)));
    }

    private QinIrTypeRef javaIteratorMethodReturnType(
            QinIrTypeRef ownerType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (ownerType == null
                || ownerType.kind() != QinIrTypeKind.CLASS
                || !"java.util.Iterator".equals(canonicalJavaSdkAliasBinaryName(ownerType.binaryName()))) {
            return null;
        }
        int argumentCount = methodCallExpression.arguments().size();
        return switch (methodCallExpression.methodName()) {
            case "next" -> argumentCount == 0 ? javaIterableElementType(ownerType) : null;
            case "hasNext" -> argumentCount == 0 ? QinIrTypeRef.booleanType() : null;
            default -> null;
        };
    }

    private QinIrTypeRef javaIterableElementType(QinIrTypeRef ownerType) {
        if (ownerType != null
                && ownerType.typeArguments() != null
                && !ownerType.typeArguments().isEmpty()) {
            return boxForObjectStorage(ownerType.typeArguments().get(0));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private boolean isJavaIterableType(QinIrTypeRef type) {
        if (type == null
                || type.kind() != QinIrTypeKind.CLASS
                || type.binaryName() == null
                || type.binaryName().isBlank()) {
            return false;
        }
        try {
            return java.lang.Iterable.class.isAssignableFrom(resolveClass(type.binaryName()));
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    private QinIrTypeRef javaDequeElementReturnType(QinIrTypeRef ownerType, String methodName, int argumentCount) {
        if (ownerType == null
                || ownerType.kind() != QinIrTypeKind.CLASS
                || ownerType.binaryName() == null
                || ownerType.typeArguments() == null
                || ownerType.typeArguments().isEmpty()
                || argumentCount != 0) {
            return null;
        }
        String ownerBinaryName = canonicalJavaSdkAliasBinaryName(ownerType.binaryName());
        if (!"java.util.Deque".equals(ownerBinaryName)
                && !"java.util.ArrayDeque".equals(ownerBinaryName)) {
            return null;
        }
        return switch (methodName) {
            case "peek", "pop", "poll", "remove", "element" -> ownerType.typeArguments().get(0);
            default -> null;
        };
    }

    private boolean traceDeclarationInstanceMethod(QinIrTypeRef ownerType, String methodName) {
        if (!Boolean.getBoolean("qin.declarationInstanceMethod.trace")) {
            return false;
        }
        String target = System.getProperty("qin.declarationInstanceMethod.traceTarget");
        if (target == null || target.isBlank()) {
            return true;
        }
        String ownerName = ownerType == null ? "" : String.valueOf(ownerType.binaryName());
        return (ownerName + "." + methodName).contains(target);
    }

    private ResolvedInstanceMethodCall resolveJavaEsmBuiltinInstanceMethodCall(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount) {
        if (ownerType == null
                || ownerType.kind() != QinIrTypeKind.CLASS
                || ownerType.binaryName() == null) {
            return null;
        }
        return switch (ownerType.binaryName()) {
            case "com.qin.lang.runtime.JavaEsmMapObject" -> resolveJavaEsmMapObjectInstanceMethodCall(
                    ownerType,
                    methodName,
                    argumentCount);
            case "com.qin.lang.runtime.JavaEsmSetObject" -> resolveJavaEsmSetObjectInstanceMethodCall(
                    methodName,
                    argumentCount);
            default -> null;
        };
    }

    private ResolvedInstanceMethodCall resolveJavaEsmMapObjectInstanceMethodCall(
            QinIrTypeRef ownerType,
            String methodName,
            int argumentCount) {
        return switch (methodName) {
            case "set" -> argumentCount == 2
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmMapObject",
                            "set",
                            QinIrTypeRef.classType("com.qin.lang.runtime.JavaEsmMapObject"),
                            List.of(QinIrTypeRef.classType("java.lang.Object"), QinIrTypeRef.classType("java.lang.Object")),
                            List.of(Object.class, Object.class))
                    : null;
            case "get" -> argumentCount == 1
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmMapObject",
                            "get",
                            QinIrTypeRef.classType("java.lang.Object"),
                            List.of(QinIrTypeRef.classType("java.lang.Object")),
                            List.of(Object.class))
                    : null;
            case "has", "delete" -> argumentCount == 1
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmMapObject",
                            methodName,
                            QinIrTypeRef.booleanType(),
                            List.of(QinIrTypeRef.classType("java.lang.Object")),
                            List.of(Object.class))
                    : null;
            case "clear" -> argumentCount == 0
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmMapObject",
                            "clear",
                            QinIrTypeRef.voidType(),
                            List.of(),
                            List.of())
                    : null;
            case "getSize" -> argumentCount == 0
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmMapObject",
                            "getSize",
                            QinIrTypeRef.intType(),
                            List.of(),
                            List.of())
                    : null;
            case "keys", "values", "entries" -> argumentCount == 0
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmMapObject",
                            methodName,
                            javaEsmMapObjectIterationReturnType(ownerType, methodName),
                            List.of(),
                            List.of())
                    : null;
            default -> null;
        };
    }

    private QinIrTypeRef javaEsmMapObjectIterationReturnType(QinIrTypeRef ownerType, String methodName) {
        QinIrTypeRef keyType = javaEsmMapObjectKeyType(ownerType);
        QinIrTypeRef valueType = javaEsmMapObjectValueType(ownerType);
        return switch (methodName) {
            case "keys" -> QinIrTypeRef.classType("java.util.List", List.of(keyType));
            case "values" -> QinIrTypeRef.classType("java.util.List", List.of(valueType));
            case "entries" -> QinIrTypeRef.classType(
                    "java.util.List",
                    List.of(QinIrTypeRef.classType("java.lang.Object[]", List.of(keyType, valueType))));
            default -> QinIrTypeRef.classType("java.util.List");
        };
    }

    private QinIrTypeRef javaEsmMapObjectKeyType(QinIrTypeRef ownerType) {
        if (ownerType != null && ownerType.typeArguments() != null && !ownerType.typeArguments().isEmpty()) {
            return boxForObjectStorage(ownerType.typeArguments().get(0));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef javaEsmMapObjectValueType(QinIrTypeRef ownerType) {
        if (ownerType != null && ownerType.typeArguments() != null && ownerType.typeArguments().size() >= 2) {
            return boxForObjectStorage(ownerType.typeArguments().get(1));
        }
        return QinIrTypeRef.classType("java.lang.Object");
    }

    private QinIrTypeRef javaLangStringBuilderFacadeReturnType(
            String ownerBinaryName,
            String methodName,
            int argumentCount) {
        if (!"__QinJavaLangStringBuilder".equals(ownerBinaryName)) {
            String canonicalOwner = canonicalJavaSdkAliasBinaryName(ownerBinaryName);
            if (!"java.lang.StringBuilder".equals(canonicalOwner)) {
                return null;
            }
        }
        return switch (methodName) {
            case "append" -> argumentCount == 1 ? QinIrTypeRef.classType(ownerBinaryName) : null;
            case "length" -> argumentCount == 0 ? QinIrTypeRef.intType() : null;
            case "setLength" -> argumentCount == 1 ? QinIrTypeRef.voidType() : null;
            case "toString" -> argumentCount == 0 ? QinIrTypeRef.stringType() : null;
            default -> null;
        };
    }

    private ResolvedInstanceMethodCall resolveJavaEsmSetObjectInstanceMethodCall(
            String methodName,
            int argumentCount) {
        return switch (methodName) {
            case "add" -> argumentCount == 1
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmSetObject",
                            "add",
                            QinIrTypeRef.classType("com.qin.lang.runtime.JavaEsmSetObject"),
                            List.of(QinIrTypeRef.classType("java.lang.Object")),
                            List.of(Object.class))
                    : null;
            case "has", "delete" -> argumentCount == 1
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmSetObject",
                            methodName,
                            QinIrTypeRef.booleanType(),
                            List.of(QinIrTypeRef.classType("java.lang.Object")),
                            List.of(Object.class))
                    : null;
            case "clear" -> argumentCount == 0
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmSetObject",
                            "clear",
                            QinIrTypeRef.voidType(),
                            List.of(),
                            List.of())
                    : null;
            case "getSize" -> argumentCount == 0
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmSetObject",
                            "getSize",
                            QinIrTypeRef.intType(),
                            List.of(),
                            List.of())
                    : null;
            case "values" -> argumentCount == 0
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmSetObject",
                            "values",
                            QinIrTypeRef.classType("java.util.List"),
                            List.of(),
                            List.of())
                    : null;
            case "forEach" -> argumentCount == 1
                    ? runtimeInstanceMethodCall(
                            "com.qin.lang.runtime.JavaEsmSetObject",
                            "forEach",
                            QinIrTypeRef.classType("java.lang.Object"),
                            List.of(QinIrTypeRef.classType("java.lang.Object")),
                            List.of(Object.class))
                    : null;
            default -> null;
        };
    }

    private ResolvedInstanceMethodCall runtimeInstanceMethodCall(
            String ownerBinaryName,
            String methodName,
            QinIrTypeRef returnType,
            List<QinIrTypeRef> parameterTypes,
            List<Class<?>> reflectedParameterTypes) {
        return new ResolvedInstanceMethodCall(
                ownerBinaryName,
                methodName,
                List.copyOf(parameterTypes),
                returnType,
                false,
                MethodTypeDesc.of(
                        toClassDesc(returnType),
                        parameterTypes.stream().map(this::toClassDesc).toList()),
                List.copyOf(reflectedParameterTypes),
                false);
    }

    private ResolvedInstanceMethodCall resolveLocalDeclarationInstanceMethodCall(
            QinIrClassDeclaration declaration,
            String methodName,
            int argumentCount) {
        return resolveLocalDeclarationInstanceMethodCall(
                declaration,
                methodName,
                argumentCount,
                null,
                new java.util.LinkedHashSet<>());
    }

    private ResolvedInstanceMethodCall resolveLocalDeclarationInstanceMethodCall(
            QinIrClassDeclaration declaration,
            String methodName,
            int argumentCount,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveLocalDeclarationInstanceMethodCall(
                declaration,
                methodName,
                argumentCount,
                declarationIndex,
                new java.util.LinkedHashSet<>());
    }

    private ResolvedInstanceMethodCall resolveLocalDeclarationInstanceMethodCall(
            QinIrClassDeclaration declaration,
            String methodName,
            int argumentCount,
            Map<String, QinIrClassDeclaration> declarationIndex,
            Set<String> visitedBinaryNames) {
        if (declaration == null) {
            return null;
        }
        if (visitedBinaryNames != null && !visitedBinaryNames.add(declaration.binaryName())) {
            return null;
        }
        QinIrMethodDeclaration matched = null;
        QinIrMethodDeclaration paddedMatch = null;
        for (QinIrMethodDeclaration candidate : declaration.methods()) {
            if (candidate.staticMethod()
                    || !sameLocalMethodName(candidate.name(), methodName)
                    || !isLocalMethodCountApplicable(candidate.parameters(), argumentCount)) {
                continue;
            }
            if (candidate.parameters().size() == argumentCount) {
                if (matched != null) {
                    throw new IllegalArgumentException(
                            "Ambiguous declaration method overload: "
                                    + declaration.binaryName()
                                    + "."
                                    + methodName);
                }
                matched = candidate;
                continue;
            }
            if (paddedMatch != null) {
                throw new IllegalArgumentException(
                        "Ambiguous declaration method overload: "
                                + declaration.binaryName()
                                + "."
                                + methodName);
            }
            paddedMatch = candidate;
        }
        if (matched == null) {
            matched = paddedMatch;
        }
        if (matched == null) {
            QinIrMethodDeclaration uniqueNameMatch = null;
            for (QinIrMethodDeclaration candidate : declaration.methods()) {
                if (candidate.staticMethod() || !sameLocalMethodName(candidate.name(), methodName)) {
                    continue;
                }
                if (uniqueNameMatch != null) {
                    return null;
                }
                uniqueNameMatch = candidate;
            }
            matched = uniqueNameMatch;
        }
        if (matched == null) {
            QinIrClassDeclaration superDeclaration = declaration.superType() == null
                    ? null
                    : resolveIndexedDeclaration(declarationIndex, declaration.superType().binaryName());
            return superDeclaration == null
                    ? null
                    : resolveLocalDeclarationInstanceMethodCall(
                            superDeclaration,
                            methodName,
                            argumentCount,
                            declarationIndex,
                            visitedBinaryNames);
        }
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (QinIrParameter parameter : matched.parameters()) {
            parameterTypes.add(parameter.type());
        }
        return new ResolvedInstanceMethodCall(
                declaration.binaryName(),
                matched.name(),
                List.copyOf(parameterTypes),
                matched.returnType(),
                declaration.interfaceClass(),
                null,
                List.of(),
                hasVarargsParameter(matched));
    }

    private boolean hasExactLocalInstanceMethod(
            QinIrClassDeclaration declaration,
            String methodName,
            int argumentCount,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declaration == null || methodName == null) {
            return false;
        }
        for (QinIrMethodDeclaration candidate : declaration.methods()) {
            if (!candidate.staticMethod()
                    && candidate.name().equals(methodName)
                    && candidate.parameters().size() == argumentCount
                    && (argumentTypes == null
                            || localMethodMatchScore(candidate.parameters(), argumentTypes, null, declarationIndex) >= 0)) {
                return true;
            }
        }
        return false;
    }

    private QinIrMethodDeclaration resolveGeneratedOverloadImplementation(
            QinIrClassDeclaration declaration,
            String methodName,
            int argumentCount,
            List<QinIrTypeRef> argumentTypes,
            boolean staticMethod) {
        return resolveGeneratedOverloadImplementation(
                declaration,
                methodName,
                argumentCount,
                argumentTypes,
                null,
                null,
                staticMethod);
    }

    private QinIrMethodDeclaration resolveGeneratedOverloadImplementation(
            QinIrClassDeclaration declaration,
            String methodName,
            int argumentCount,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions,
            Map<String, QinIrClassDeclaration> declarationIndex,
            boolean staticMethod) {
        if (declaration == null || methodName == null || methodName.isBlank()) {
            return null;
        }
        String prefix = "__qin_overload_" + methodName + "_" + argumentCount + "_";
        QinIrMethodDeclaration arityMatch = null;
        QinIrMethodDeclaration typedMatch = null;
        int typedMatchScore = Integer.MIN_VALUE;
        for (QinIrMethodDeclaration candidate : declaration.methods()) {
            if (candidate.staticMethod() != staticMethod
                    || !candidate.name().startsWith(prefix)
                    || candidate.parameters().size() != argumentCount) {
                continue;
            }
            if (arityMatch != null) {
                arityMatch = null;
            } else {
                arityMatch = candidate;
            }
            if (argumentTypes != null) {
                int matchScore = localMethodMatchScore(
                        candidate.parameters(),
                        argumentTypes,
                        argumentExpressions,
                        declarationIndex);
                if (matchScore < 0) {
                    continue;
                }
                if (typedMatch != null && matchScore == typedMatchScore) {
                    int tieBreak = generatedOverloadTieBreak(
                            candidate,
                            typedMatch,
                            argumentTypes,
                            argumentExpressions,
                            declarationIndex);
                    if (tieBreak > 0) {
                        typedMatch = candidate;
                        typedMatchScore = matchScore;
                    } else if (tieBreak == 0 && generatedOverloadNullTieKeepsIncumbent(
                            candidate,
                            typedMatch,
                            argumentExpressions)) {
                        // Generated TS overload dispatchers test guards in source order; null may satisfy
                        // unrelated reference overload guards, so keep the earlier generated overload.
                    } else if (tieBreak == 0) {
                        // Generated overload dispatcher guards are ordered. If static evidence cannot
                        // distinguish same-score overloads, preserve that generated source order.
                    }
                    continue;
                }
                if (typedMatch == null || matchScore > typedMatchScore) {
                    typedMatch = candidate;
                    typedMatchScore = matchScore;
                }
            }
        }
        if (typedMatch != null) {
            return typedMatch;
        }
        return arityMatch;
    }

    private boolean generatedOverloadNullTieKeepsIncumbent(
            QinIrMethodDeclaration candidate,
            QinIrMethodDeclaration incumbent,
            List<QinIrExpression> argumentExpressions) {
        if (candidate == null
                || incumbent == null
                || candidate.parameters() == null
                || incumbent.parameters() == null
                || candidate.parameters().size() != incumbent.parameters().size()
                || argumentExpressions == null) {
            return false;
        }
        boolean differingReferenceSlot = false;
        for (int i = 0; i < candidate.parameters().size(); i++) {
            QinIrTypeRef candidateType = candidate.parameters().get(i).type();
            QinIrTypeRef incumbentType = incumbent.parameters().get(i).type();
            if (sameIrType(candidateType, incumbentType)) {
                continue;
            }
            differingReferenceSlot = true;
            QinIrExpression argumentExpression = localArgumentExpression(argumentExpressions, i);
            if (!(argumentExpression instanceof QinIrNullLiteral)
                    || !isReferenceLikeParameter(candidateType)
                    || !isReferenceLikeParameter(incumbentType)) {
                return false;
            }
        }
        return differingReferenceSlot;
    }

    private int generatedOverloadTieBreak(
            QinIrMethodDeclaration candidate,
            QinIrMethodDeclaration incumbent,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        int candidateScore = generatedOverloadSpecificityScore(
                candidate,
                argumentTypes,
                argumentExpressions,
                declarationIndex);
        int incumbentScore = generatedOverloadSpecificityScore(
                incumbent,
                argumentTypes,
                argumentExpressions,
                declarationIndex);
        return Integer.compare(candidateScore, incumbentScore);
    }

    private int generatedOverloadSpecificityScore(
            QinIrMethodDeclaration overload,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (overload == null || overload.parameters() == null) {
            return 0;
        }
        int score = 0;
        for (int i = 0; i < overload.parameters().size(); i++) {
            QinIrTypeRef parameterType = overload.parameters().get(i).type();
            QinIrTypeRef argumentType = argumentTypes == null || i >= argumentTypes.size()
                    ? null
                    : argumentTypes.get(i);
            QinIrExpression argumentExpression = localArgumentExpression(argumentExpressions, i);
            QinIrTypeRef effectiveArgumentType = generatedOverloadEffectiveArgumentType(
                    argumentType,
                    argumentExpression,
                    declarationIndex);
            if (sameIrType(parameterType, effectiveArgumentType)
                    && !sameIrType(argumentType, effectiveArgumentType)) {
                score += 100;
                continue;
            }
            if (sameIrType(parameterType, argumentType)) {
                score += 50;
                continue;
            }
            if (isMoreSpecificLocalType(parameterType, argumentType, declarationIndex)) {
                score += 10;
            }
        }
        return score;
    }

    private QinIrTypeRef generatedOverloadEffectiveArgumentType(
            QinIrTypeRef argumentType,
            QinIrExpression argumentExpression,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (argumentExpression instanceof QinIrCastExpression castExpression) {
            return castTypeRef(castExpression.typeName());
        }
        if (argumentExpression instanceof QinIrStaticMethodCallExpression staticMethodCallExpression) {
            ResolvedStaticMethodCall staticMethod = resolveStaticMethodCall(
                    staticMethodCallExpression.ownerBinaryName(),
                    staticMethodCallExpression.methodName(),
                    staticMethodCallExpression.arguments().size(),
                    null,
                    declarationIndex);
            if (staticMethod != null) {
                return staticMethod.returnType();
            }
        }
        if (argumentExpression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            ResolvedInstanceMethodCall instanceMethod = methodCallExpression.ownerBinaryName() == null
                    ? null
                    : resolveInstanceMethodCall(
                            QinIrTypeRef.classType(methodCallExpression.ownerBinaryName()),
                            methodCallExpression.methodName(),
                            methodCallExpression.arguments().size(),
                            null,
                            null,
                            declarationIndex,
                            new java.util.LinkedHashSet<>());
            if (instanceMethod != null) {
                return instanceMethod.returnType();
            }
            if (methodCallExpression.receiver() instanceof QinIrIdentifierReference identifierReference) {
                QinIrClassDeclaration receiverDeclaration = resolveIndexedDeclaration(
                        declarationIndex,
                        identifierReference.name());
                String ownerBinaryName = receiverDeclaration == null
                        ? identifierReference.name()
                        : receiverDeclaration.binaryName();
                ResolvedStaticMethodCall staticMethod = resolveStaticMethodCall(
                        ownerBinaryName,
                        methodCallExpression.methodName(),
                        methodCallExpression.arguments().size(),
                        null,
                        declarationIndex);
                if (staticMethod != null) {
                    return staticMethod.returnType();
                }
            }
        }
        return argumentType;
    }

    private boolean sameIrType(QinIrTypeRef left, QinIrTypeRef right) {
        if (left == null || right == null || left.kind() != right.kind()) {
            return false;
        }
        if (left.kind() != QinIrTypeKind.CLASS && left.kind() != QinIrTypeKind.STRING) {
            return left.equals(right);
        }
        return Objects.equals(left.binaryName(), right.binaryName())
                || Objects.equals(flattenedBinaryAlias(left.binaryName()), flattenedBinaryAlias(right.binaryName()));
    }

    private boolean isMoreSpecificLocalType(
            QinIrTypeRef candidateType,
            QinIrTypeRef argumentType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (candidateType == null
                || argumentType == null
                || candidateType.kind() != QinIrTypeKind.CLASS
                || argumentType.kind() != QinIrTypeKind.CLASS
                || sameIrType(candidateType, argumentType)) {
            return false;
        }
        return isLocalClassAssignableTo(argumentType.binaryName(), candidateType.binaryName(), declarationIndex);
    }

    private QinIrMethodDeclaration resolveExactGeneratedOverloadImplementation(
            QinIrClassDeclaration declaration,
            String methodName,
            int argumentCount,
            boolean staticMethod) {
        if (declaration == null
                || methodName == null
                || !methodName.startsWith("__qin_overload_")) {
            return null;
        }
        QinIrMethodDeclaration matched = null;
        for (QinIrMethodDeclaration candidate : declaration.methods()) {
            if (candidate.staticMethod() != staticMethod
                    || !candidate.name().equals(methodName)
                    || candidate.parameters().size() != argumentCount) {
                continue;
            }
            if (matched != null) {
                throw new IllegalArgumentException(
                        "Ambiguous exact generated overload implementation: "
                                + declaration.binaryName()
                                + "."
                                + methodName
                                + "/"
                                + argumentCount);
            }
            matched = candidate;
        }
        return matched;
    }

    private boolean sameLocalMethodName(String candidateName, String requestedName) {
        if (candidateName == null || requestedName == null) {
            return false;
        }
        if (candidateName.equals(requestedName)) {
            return true;
        }
        String candidate = candidateName.trim();
        String requested = requestedName.trim();
        return candidate.equals(requested)
                || candidate.endsWith("." + requested)
                || requested.endsWith("." + candidate);
    }

    private boolean isLocalMethodCountApplicable(List<QinIrParameter> parameters, int argumentCount) {
        if (parameters.isEmpty()) {
            return argumentCount == 0;
        }
        if (parameters.get(parameters.size() - 1).varargs()) {
            return argumentCount >= parameters.size() - 1;
        }
        return argumentCount <= parameters.size();
    }

    private boolean isReflectedParameterCountApplicable(Method method, int argumentCount) {
        if (!method.isVarArgs()) {
            return method.getParameterCount() == argumentCount;
        }
        return argumentCount >= method.getParameterCount() - 1;
    }

    private boolean isMoreSpecificReflectedMethod(Method candidate, Method incumbent) {
        if (candidate == null || incumbent == null || candidate.equals(incumbent)) {
            return false;
        }
        Class<?> candidateDeclaringClass = candidate.getDeclaringClass();
        Class<?> incumbentDeclaringClass = incumbent.getDeclaringClass();
        if (!candidateDeclaringClass.equals(incumbentDeclaringClass)) {
            if (incumbentDeclaringClass.isAssignableFrom(candidateDeclaringClass)
                    && !candidateDeclaringClass.isAssignableFrom(incumbentDeclaringClass)) {
                return true;
            }
            if (candidateDeclaringClass.isAssignableFrom(incumbentDeclaringClass)
                    && !incumbentDeclaringClass.isAssignableFrom(candidateDeclaringClass)) {
                return false;
            }
        }
        Class<?> candidateReturnType = candidate.getReturnType();
        Class<?> incumbentReturnType = incumbent.getReturnType();
        if (!candidateReturnType.equals(incumbentReturnType)) {
            if (incumbentReturnType.isAssignableFrom(candidateReturnType)
                    && !candidateReturnType.isAssignableFrom(incumbentReturnType)) {
                return true;
            }
            if (candidateReturnType.isAssignableFrom(incumbentReturnType)
                    && !incumbentReturnType.isAssignableFrom(candidateReturnType)) {
                return false;
            }
        }
        return false;
    }

    private int reflectedDeclarationDistance(Class<?> ownerClass, Class<?> declaringClass) {
        int distance = 0;
        for (Class<?> current = ownerClass; current != null; current = current.getSuperclass()) {
            if (current.equals(declaringClass)) {
                return distance;
            }
            distance++;
        }
        if (declaringClass != null && declaringClass.isInterface() && declaringClass.isAssignableFrom(ownerClass)) {
            return 100 + distance;
        }
        return Integer.MAX_VALUE;
    }

    private Method findReflectedMethod(Class<?> ownerClass, String methodName, List<QinIrTypeRef> argumentTypes) {
        Method javaMapFunctionalFacadeMethod = findJavaMapFunctionalFacadeMethod(ownerClass, methodName, argumentTypes);
        if (javaMapFunctionalFacadeMethod != null) {
            return javaMapFunctionalFacadeMethod;
        }
        Method javaOptionalFunctionalFacadeMethod =
                findJavaOptionalFunctionalFacadeMethod(ownerClass, methodName, argumentTypes);
        if (javaOptionalFunctionalFacadeMethod != null) {
            return javaOptionalFunctionalFacadeMethod;
        }
        Method javaIterableFunctionalFacadeMethod =
                findJavaIterableFunctionalFacadeMethod(ownerClass, methodName, argumentTypes);
        if (javaIterableFunctionalFacadeMethod != null) {
            return javaIterableFunctionalFacadeMethod;
        }
        Method javaStreamFunctionalFacadeMethod =
                findJavaStreamFunctionalFacadeMethod(ownerClass, methodName, argumentTypes);
        if (javaStreamFunctionalFacadeMethod != null) {
            return javaStreamFunctionalFacadeMethod;
        }
        Method javaListSortFacadeMethod = findJavaListSortComparatorFacadeMethod(ownerClass, methodName, argumentTypes);
        if (javaListSortFacadeMethod != null) {
            return javaListSortFacadeMethod;
        }
        Method comparatorFacadeMethod = findJavaComparatorFacadeMethod(ownerClass, methodName, argumentTypes);
        if (comparatorFacadeMethod != null) {
            return comparatorFacadeMethod;
        }
        Method matched = null;
        int matchedScore = Integer.MIN_VALUE;
        int matchedDistance = Integer.MAX_VALUE;
        boolean ambiguousBest = false;
        for (Method candidate : ownerClass.getMethods()) {
            if (Modifier.isStatic(candidate.getModifiers())
                    || candidate.isBridge()
                    || candidate.isSynthetic()
                    || !candidate.getName().equals(methodName)
                    || !isExecutableApplicable(
                            candidate.getParameterTypes(),
                            candidate.isVarArgs(),
                            argumentTypes)) {
                continue;
            }
            int score = executableMatchScore(candidate.getParameterTypes(), candidate.isVarArgs(), argumentTypes);
            int candidateDistance = reflectedDeclarationDistance(ownerClass, candidate.getDeclaringClass());
            if (matched != null && score == matchedScore && candidateDistance == matchedDistance) {
                if (isMoreSpecificReflectedMethod(candidate, matched)) {
                    matched = candidate;
                    ambiguousBest = false;
                } else if (!isMoreSpecificReflectedMethod(matched, candidate)) {
                    ambiguousBest = true;
                }
                continue;
            }
            if (matched == null
                    || score > matchedScore
                    || (score == matchedScore && candidateDistance < matchedDistance)
                    || (score == matchedScore
                            && candidateDistance == matchedDistance
                            && isMoreSpecificReflectedMethod(candidate, matched))) {
                matched = candidate;
                matchedScore = score;
                matchedDistance = candidateDistance;
                ambiguousBest = false;
            }
        }
        if (ambiguousBest) {
            throw new IllegalArgumentException(
                    "Ambiguous reflected method overload: " + ownerClass.getName() + "." + methodName);
        }
        if (matched == null) {
            return findUniqueReferenceMethodForDynamicArguments(ownerClass, methodName, argumentTypes);
        }
        return matched;
    }

    private Method findJavaMapFunctionalFacadeMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (!java.util.Map.class.isAssignableFrom(ownerClass)
                || argumentTypes == null
                || argumentTypes.isEmpty()) {
            return null;
        }
        try {
            if ("computeIfAbsent".equals(methodName)
                    && argumentTypes.size() == 2
                    && isJavaFunctionalObjectType(argumentTypes.get(1))) {
                return java.util.Map.class.getMethod(
                        "computeIfAbsent",
                        Object.class,
                        java.util.function.Function.class);
            }
            if ("forEach".equals(methodName)
                    && argumentTypes.size() == 1
                    && isJavaFunctionalObjectType(argumentTypes.get(0))) {
                return java.util.Map.class.getMethod(
                        "forEach",
                        java.util.function.BiConsumer.class);
            }
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing JDK Map functional method: " + methodName, exception);
        }
        return null;
    }

    private Method findJavaOptionalFunctionalFacadeMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (!java.util.Optional.class.isAssignableFrom(ownerClass)
                || argumentTypes == null
                || argumentTypes.size() != 1
                || !"ifPresent".equals(methodName)
                || !isJavaFunctionalObjectType(argumentTypes.get(0))) {
            return null;
        }
        try {
            return java.util.Optional.class.getMethod("ifPresent", java.util.function.Consumer.class);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing JDK Optional.ifPresent(Consumer)", exception);
        }
    }

    private Method findJavaIterableFunctionalFacadeMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (!java.lang.Iterable.class.isAssignableFrom(ownerClass)
                || argumentTypes == null
                || argumentTypes.size() != 1
                || !"forEach".equals(methodName)
                || !isJavaFunctionalObjectType(argumentTypes.get(0))) {
            return null;
        }
        try {
            return java.lang.Iterable.class.getMethod("forEach", java.util.function.Consumer.class);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing JDK Iterable.forEach(Consumer)", exception);
        }
    }

    private Method findJavaStreamFunctionalFacadeMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (argumentTypes == null
                || argumentTypes.size() != 1
                || !isJavaFunctionalObjectType(argumentTypes.get(0))) {
            return null;
        }
        try {
            if (ownerClass == java.util.stream.IntStream.class) {
                return switch (methodName) {
                    case "mapToObj" -> java.util.stream.IntStream.class.getMethod(
                            "mapToObj",
                            java.util.function.IntFunction.class);
                    default -> null;
                };
            }
            if (ownerClass == java.util.stream.LongStream.class) {
                return switch (methodName) {
                    case "mapToObj" -> java.util.stream.LongStream.class.getMethod(
                            "mapToObj",
                            java.util.function.LongFunction.class);
                    default -> null;
                };
            }
            if (ownerClass == java.util.stream.DoubleStream.class) {
                return switch (methodName) {
                    case "mapToObj" -> java.util.stream.DoubleStream.class.getMethod(
                            "mapToObj",
                            java.util.function.DoubleFunction.class);
                    default -> null;
                };
            }
            if (!java.util.stream.Stream.class.isAssignableFrom(ownerClass)) {
                return null;
            }
            return switch (methodName) {
                case "filter" -> java.util.stream.Stream.class.getMethod(
                        "filter",
                        java.util.function.Predicate.class);
                case "map" -> java.util.stream.Stream.class.getMethod(
                        "map",
                        java.util.function.Function.class);
                case "mapToInt" -> java.util.stream.Stream.class.getMethod(
                        "mapToInt",
                        java.util.function.ToIntFunction.class);
                default -> null;
            };
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing JDK Stream functional method: " + methodName, exception);
        }
    }

    private Method findJavaListSortComparatorFacadeMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (!java.util.List.class.isAssignableFrom(ownerClass)
                || !"sort".equals(methodName)
                || argumentTypes == null
                || argumentTypes.size() != 1
                || !isJavaFunctionalObjectType(argumentTypes.get(0))) {
            return null;
        }
        try {
            return java.util.List.class.getMethod("sort", java.util.Comparator.class);
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing JDK List.sort(Comparator)", exception);
        }
    }

    private boolean isJavaFunctionalObjectType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && "com.qin.lang.runtime.JavaEsmGlobal$JavaFunctionalObject".equals(type.binaryName());
    }

    private Method findJavaComparatorFacadeMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (ownerClass != java.util.Comparator.class || argumentTypes == null) {
            return null;
        }
        try {
            if ("thenComparing".equals(methodName) && argumentTypes.size() == 1) {
                QinIrTypeRef argumentType = argumentTypes.get(0);
                if (argumentType != null
                        && argumentType.kind() == QinIrTypeKind.CLASS
                        && "java.util.Comparator".equals(argumentType.binaryName())) {
                    return java.util.Comparator.class.getMethod("thenComparing", java.util.Comparator.class);
                }
                return java.util.Comparator.class.getMethod("thenComparing", java.util.function.Function.class);
            }
            if ("thenComparingInt".equals(methodName) && argumentTypes.size() == 1) {
                return java.util.Comparator.class.getMethod("thenComparingInt", java.util.function.ToIntFunction.class);
            }
            if ("thenComparingLong".equals(methodName) && argumentTypes.size() == 1) {
                return java.util.Comparator.class.getMethod("thenComparingLong", java.util.function.ToLongFunction.class);
            }
            if ("thenComparingDouble".equals(methodName) && argumentTypes.size() == 1) {
                return java.util.Comparator.class.getMethod(
                        "thenComparingDouble",
                        java.util.function.ToDoubleFunction.class);
            }
            if ("reversed".equals(methodName) && argumentTypes.isEmpty()) {
                return java.util.Comparator.class.getMethod("reversed");
            }
        } catch (NoSuchMethodException exception) {
            throw new IllegalStateException("Missing JDK Comparator method: " + methodName, exception);
        }
        return null;
    }

    private Method findUniqueReferenceMethodForDynamicArguments(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (argumentTypes == null || argumentTypes.isEmpty()) {
            return null;
        }
        Method matched = null;
        for (Method candidate : ownerClass.getMethods()) {
            if (Modifier.isStatic(candidate.getModifiers())
                    || candidate.isBridge()
                    || candidate.isSynthetic()
                    || !candidate.getName().equals(methodName)
                    || candidate.isVarArgs()
                    || candidate.getParameterCount() != argumentTypes.size()) {
                continue;
            }
            Class<?>[] parameterTypes = candidate.getParameterTypes();
            boolean dynamicSlotSeen = false;
            boolean applicable = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                QinIrTypeRef argumentType = argumentTypes.get(i);
                if (isDynamicUnknownReferenceArgument(argumentType)) {
                    dynamicSlotSeen = true;
                    if (parameterTypes[i] == void.class) {
                        applicable = false;
                        break;
                    }
                    continue;
                }
                if (!isArgumentApplicable(parameterTypes[i], argumentType)) {
                    applicable = false;
                    break;
                }
            }
            if (!dynamicSlotSeen || !applicable) {
                continue;
            }
            if (matched != null) {
                return null;
            }
            matched = candidate;
        }
        return matched;
    }

    private Method findReflectedMethod(Class<?> ownerClass, String methodName, int argumentCount) {
        for (Class<?> current = ownerClass; current != null; current = current.getSuperclass()) {
            Method matched = null;
            for (Method candidate : current.getDeclaredMethods()) {
                if (Modifier.isStatic(candidate.getModifiers())
                        || candidate.isBridge()
                        || candidate.isSynthetic()
                        || !candidate.getName().equals(methodName)
                        || !isReflectedParameterCountApplicable(candidate, argumentCount)) {
                    continue;
                }
                if (matched != null) {
                    throw new IllegalArgumentException(
                            "Ambiguous reflected method overload: " + ownerClass.getName() + "." + methodName);
                }
                matched = candidate;
            }
            if (matched != null) {
                return matched;
            }
        }
        Method publicInterfaceMatch = null;
        for (Method candidate : ownerClass.getMethods()) {
            if (Modifier.isStatic(candidate.getModifiers())
                    || candidate.isBridge()
                    || candidate.isSynthetic()
                    || !candidate.getName().equals(methodName)
                    || candidate.getDeclaringClass() == Object.class
                    || !candidate.getDeclaringClass().isInterface()
                    || !isReflectedParameterCountApplicable(candidate, argumentCount)) {
                continue;
            }
            if (publicInterfaceMatch != null) {
                throw new IllegalArgumentException(
                        "Ambiguous reflected interface method overload: " + ownerClass.getName() + "." + methodName);
            }
            publicInterfaceMatch = candidate;
        }
        if (publicInterfaceMatch != null) {
            return publicInterfaceMatch;
        }
        return null;
    }

    private ResolvedStaticMethodCall resolveStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            int argumentCount) {
        return resolveStaticMethodCall(ownerBinaryName, methodName, argumentCount, Map.of());
    }

    private ResolvedStaticMethodCall resolveStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            int argumentCount,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveStaticMethodCall(ownerBinaryName, methodName, argumentCount, null, declarationIndex);
    }

    private ResolvedStaticMethodCall resolveStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveStaticMethodCall(ownerBinaryName, methodName, argumentTypes, null, declarationIndex);
    }

    private ResolvedStaticMethodCall resolveStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveStaticMethodCall(
                ownerBinaryName,
                methodName,
                argumentTypes == null ? 0 : argumentTypes.size(),
                argumentTypes,
                argumentExpressions,
                declarationIndex);
    }

    private ResolvedStaticMethodCall resolveStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            int argumentCount,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveStaticMethodCall(ownerBinaryName, methodName, argumentCount, argumentTypes, null, declarationIndex);
    }

    private ResolvedStaticMethodCall resolveStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            int argumentCount,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions,
        Map<String, QinIrClassDeclaration> declarationIndex) {
        ownerBinaryName = canonicalQinHostRuntimeBinaryName(ownerBinaryName);
        QinIrClassDeclaration localDeclaration = resolveIndexedDeclaration(declarationIndex, ownerBinaryName);
        if (localDeclaration != null) {
            QinIrMethodDeclaration generatedOverload = resolveGeneratedOverloadImplementation(
                    localDeclaration,
                    methodName,
                    argumentCount,
                    argumentTypes,
                    argumentExpressions,
                    declarationIndex,
                    true);
            if (generatedOverload != null) {
                List<QinIrTypeRef> parameterTypes = effectiveLocalStaticParameterTypes(
                        localDeclaration.binaryName(),
                        generatedOverload.parameters(),
                        argumentTypes,
                        declarationIndex);
                return new ResolvedStaticMethodCall(
                        localDeclaration.binaryName(),
                        generatedOverload.name(),
                        List.copyOf(parameterTypes),
                        generatedOverload.returnType(),
                        localDeclaration.interfaceClass(),
                        null,
                        List.of(),
                        !generatedOverload.parameters().isEmpty()
                                && generatedOverload.parameters().get(generatedOverload.parameters().size() - 1).varargs());
            }
            QinIrMethodDeclaration matched = null;
            QinIrMethodDeclaration paddedMatch = null;
            int matchedScore = Integer.MIN_VALUE;
            int paddedMatchScore = Integer.MIN_VALUE;
            for (QinIrMethodDeclaration candidate : localDeclaration.methods()) {
                if (!candidate.staticMethod()
                        || !candidate.name().equals(methodName)
                        || !isLocalMethodCountApplicable(candidate.parameters(), argumentCount)) {
                    continue;
                }
                int matchScore = argumentTypes == null
                        ? 0
                        : localMethodMatchScore(
                                candidate.parameters(),
                                argumentTypes,
                                argumentExpressions,
                                declarationIndex);
                if (matchScore < 0) {
                    continue;
                }
                if (candidate.parameters().size() == argumentCount) {
                    if (matched != null && matchScore == matchedScore) {
                        throw new IllegalArgumentException(
                                "Ambiguous declaration static method overload: " + ownerBinaryName + "." + methodName);
                    }
                    if (matched != null && matchScore < matchedScore) {
                        continue;
                    }
                    matched = candidate;
                    matchedScore = matchScore;
                    continue;
                }
                if (paddedMatch != null && matchScore == paddedMatchScore) {
                    throw new IllegalArgumentException(
                            "Ambiguous declaration static method overload: " + ownerBinaryName + "." + methodName);
                }
                if (paddedMatch != null && matchScore < paddedMatchScore) {
                    continue;
                }
                paddedMatch = candidate;
                paddedMatchScore = matchScore;
            }
            if (matched == null) {
                matched = paddedMatch;
            }
            if (matched == null) {
                matched = resolveGeneratedOverloadImplementation(
                        localDeclaration,
                        methodName,
                        argumentCount,
                        argumentTypes,
                        true);
            }
            if (matched == null) {
                matched = resolveExactGeneratedOverloadImplementation(
                        localDeclaration,
                        methodName,
                        argumentCount,
                        true);
            }
            if (matched != null) {
                List<QinIrTypeRef> parameterTypes = effectiveLocalStaticParameterTypes(
                        localDeclaration.binaryName(),
                        matched.parameters(),
                        argumentTypes,
                        declarationIndex);
                return new ResolvedStaticMethodCall(
                        localDeclaration.binaryName(),
                        matched.name(),
                        List.copyOf(parameterTypes),
                        matched.returnType(),
                        localDeclaration.interfaceClass(),
                        null,
                        List.of(),
                        !matched.parameters().isEmpty()
                                && matched.parameters().get(matched.parameters().size() - 1).varargs());
            }
        }
        if (isGeneratedEnumLikeDeclaration(localDeclaration)
                && isGeneratedEnumSyntheticStaticCall(methodName, argumentCount)) {
            return null;
        }
        try {
            ownerBinaryName = reflectedStaticMethodOwnerBinaryName(ownerBinaryName, methodName);
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            if (argumentTypes != null) {
                Method matched = findReflectedStaticMethod(ownerClass, methodName, argumentTypes);
                if (matched == null) {
                    matched = findBoxedPrimitiveValueOfStaticMethod(ownerClass, methodName, argumentTypes);
                }
                if (matched == null) {
                    return null;
                }
                return toResolvedStaticMethodCall(ownerClass, matched);
            }
            Method exactArityMethod = null;
            Method varargsMethod = null;
            for (Method candidate : ownerClass.getMethods()) {
                if (!candidate.getName().equals(methodName)
                        || !Modifier.isStatic(candidate.getModifiers())) {
                    continue;
                }
                if (!candidate.isVarArgs()) {
                    if (candidate.getParameterCount() != argumentCount) {
                        continue;
                    }
                    if (exactArityMethod != null) {
                        throw new IllegalArgumentException(
                                "Ambiguous reflected static method overload: " + ownerBinaryName + "." + methodName);
                    }
                    exactArityMethod = candidate;
                    continue;
                }
                if (!isReflectedParameterCountApplicable(candidate, argumentCount)) {
                    continue;
                }
                if (varargsMethod != null) {
                    throw new IllegalArgumentException(
                            "Ambiguous reflected static varargs method overload: " + ownerBinaryName + "." + methodName);
                }
                varargsMethod = candidate;
            }
            Method matched = exactArityMethod == null ? varargsMethod : exactArityMethod;
            if (matched == null) {
                return null;
            }
            return toResolvedStaticMethodCall(ownerClass, matched);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private List<QinIrTypeRef> effectiveLocalStaticParameterTypes(
            String localOwnerBinaryName,
            List<QinIrParameter> parameters,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (int i = 0; i < parameters.size(); i++) {
            QinIrTypeRef argumentType = argumentTypes == null || i >= argumentTypes.size() ? null : argumentTypes.get(i);
            parameterTypes.add(effectiveLocalStaticParameterType(
                    localOwnerBinaryName,
                    parameters.get(i).type(),
                    argumentType,
                    declarationIndex));
        }
        return List.copyOf(parameterTypes);
    }

    private QinIrTypeRef effectiveLocalStaticParameterType(
            String localOwnerBinaryName,
            QinIrTypeRef parameterType,
            QinIrTypeRef argumentType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (parameterType == null || parameterType.kind() != QinIrTypeKind.CLASS) {
            return parameterType;
        }
        String parameterBinaryName = parameterType.binaryName();
        String effectiveParameterBinaryName = effectiveLocalReferenceBinaryName(parameterBinaryName, declarationIndex);
        if (argumentType != null && argumentType.kind() == QinIrTypeKind.CLASS) {
            String argumentBinaryName = argumentType.binaryName();
            String effectiveArgumentBinaryName = effectiveLocalReferenceBinaryName(argumentBinaryName, declarationIndex);
            if (Objects.equals(effectiveParameterBinaryName, effectiveArgumentBinaryName)
                    || Objects.equals(flattenedBinaryAlias(parameterBinaryName), effectiveArgumentBinaryName)
                    || Objects.equals(flattenedBinaryAlias(parameterBinaryName), argumentBinaryName)) {
                return QinIrTypeRef.classType(effectiveArgumentBinaryName, argumentType.typeArguments());
            }
        }
        if (!Objects.equals(parameterBinaryName, effectiveParameterBinaryName)) {
            return QinIrTypeRef.classType(effectiveParameterBinaryName, parameterType.typeArguments());
        }
        String flattenedParameterBinaryName = flattenedBinaryAlias(parameterBinaryName);
        if (isGeneratedLocalBinaryName(localOwnerBinaryName)
                && flattenedParameterBinaryName != null
                && !isJavaPlatformBinaryName(parameterBinaryName)
                && isGeneratedLocalBinaryName(flattenedParameterBinaryName)) {
            return QinIrTypeRef.classType(flattenedParameterBinaryName, parameterType.typeArguments());
        }
        return parameterType;
    }

    private boolean isJavaPlatformBinaryName(String binaryName) {
        return binaryName != null
                && (binaryName.startsWith("java.")
                        || binaryName.startsWith("javax.")
                        || binaryName.startsWith("jdk.")
                        || binaryName.startsWith("sun."));
    }

    private boolean isGeneratedLocalBinaryName(String binaryName) {
        return binaryName != null
                && !binaryName.isBlank()
                && !binaryName.contains(".")
                && binaryName.contains("_");
    }

    private String staticMethodResolutionDiagnostic(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        QinIrClassDeclaration localDeclaration = resolveIndexedDeclaration(declarationIndex, ownerBinaryName);
        StringBuilder diagnostic = new StringBuilder();
        diagnostic.append("argumentTypes=").append(typeListShape(argumentTypes));
        if (localDeclaration == null) {
            diagnostic.append("; localDeclaration=<none>");
            return diagnostic.toString();
        }
        diagnostic.append("; localDeclaration=").append(localDeclaration.binaryName());
        List<String> candidateShapes = new ArrayList<>();
        for (QinIrMethodDeclaration candidate : localDeclaration.methods()) {
            if (candidate.staticMethod() && candidate.name().equals(methodName)) {
                candidateShapes.add(methodShape(candidate));
            }
        }
        diagnostic.append("; candidateCount=").append(candidateShapes.size());
        if (!candidateShapes.isEmpty()) {
            diagnostic.append("; candidates=").append(candidateShapes);
        }
        return diagnostic.toString();
    }

    private String methodShape(QinIrMethodDeclaration method) {
        List<String> parameterShapes = new ArrayList<>();
        for (QinIrParameter parameter : method.parameters()) {
            parameterShapes.add(parameter.name()
                    + ":"
                    + typeShape(parameter.type())
                    + (parameter.varargs() ? "..." : ""));
        }
        return method.name() + "(" + String.join(",", parameterShapes) + "):" + typeShape(method.returnType());
    }

    private String typeListShape(List<QinIrTypeRef> types) {
        if (types == null) {
            return "<none>";
        }
        List<String> shapes = new ArrayList<>();
        for (QinIrTypeRef type : types) {
            shapes.add(typeShape(type));
        }
        return shapes.toString();
    }

    private String typeShape(QinIrTypeRef type) {
        if (type == null) {
            return "<null>";
        }
        if (type.kind() != QinIrTypeKind.CLASS) {
            return type.kind().name();
        }
        if (type.typeArguments().isEmpty()) {
            return type.binaryName();
        }
        return type.binaryName() + "<" + typeListShape(type.typeArguments()) + ">";
    }

    private Method findReflectedStaticMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        Method matched = null;
        int matchedScore = Integer.MIN_VALUE;
        int matchedDistance = Integer.MAX_VALUE;
        boolean ambiguousBest = false;
        for (Method candidate : ownerClass.getMethods()) {
            if (!Modifier.isStatic(candidate.getModifiers())
                    || candidate.isBridge()
                    || candidate.isSynthetic()
                    || !candidate.getName().equals(methodName)
                    || !isExecutableApplicable(
                            candidate.getParameterTypes(),
                            candidate.isVarArgs(),
                            argumentTypes)) {
                continue;
            }
            int score = executableMatchScore(candidate.getParameterTypes(), candidate.isVarArgs(), argumentTypes);
            int candidateDistance = reflectedDeclarationDistance(ownerClass, candidate.getDeclaringClass());
            if (matched != null && score == matchedScore && candidateDistance == matchedDistance) {
                if (isMoreSpecificReflectedMethod(candidate, matched)) {
                    matched = candidate;
                    ambiguousBest = false;
                } else if (!isMoreSpecificReflectedMethod(matched, candidate)) {
                    ambiguousBest = true;
                }
                continue;
            }
            if (matched == null
                    || score > matchedScore
                    || (score == matchedScore && candidateDistance < matchedDistance)
                    || (score == matchedScore
                            && candidateDistance == matchedDistance
                            && isMoreSpecificReflectedMethod(candidate, matched))) {
                matched = candidate;
                matchedScore = score;
                matchedDistance = candidateDistance;
                ambiguousBest = false;
            }
        }
        if (ambiguousBest) {
            throw new IllegalArgumentException(
                    "Ambiguous reflected static method overload: " + ownerClass.getName() + "." + methodName);
        }
        if (matched == null) {
            return findUniqueReferenceStaticMethodForDynamicArguments(ownerClass, methodName, argumentTypes);
        }
        return matched;
    }

    private Method findBoxedPrimitiveValueOfStaticMethod(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (argumentTypes == null
                || argumentTypes.size() != 1
                || !"valueOf".equals(methodName)) {
            return null;
        }
        Class<?> primitiveType = boxedPrimitiveValueOfParameterType(ownerClass);
        if (primitiveType == null) {
            return null;
        }
        QinIrTypeRef argumentType = argumentTypes.get(0);
        if (!(isDynamicUnknownReferenceArgument(argumentType)
                || argumentType.kind() == QinIrTypeKind.INT
                || argumentType.kind() == QinIrTypeKind.DOUBLE)) {
            return null;
        }
        Method primitiveCandidate = null;
        Method stringCandidate = null;
        for (Method candidate : ownerClass.getMethods()) {
            if (!Modifier.isStatic(candidate.getModifiers())
                    || candidate.isBridge()
                    || candidate.isSynthetic()
                    || !candidate.getName().equals(methodName)
                    || candidate.getParameterCount() != 1) {
                continue;
            }
            Class<?> parameterType = candidate.getParameterTypes()[0];
            if (parameterType == primitiveType) {
                primitiveCandidate = candidate;
            } else if (parameterType == String.class) {
                stringCandidate = candidate;
            }
        }
        return primitiveCandidate != null ? primitiveCandidate : stringCandidate;
    }

    private Class<?> boxedPrimitiveValueOfParameterType(Class<?> ownerClass) {
        if (ownerClass == Integer.class) {
            return int.class;
        }
        if (ownerClass == Long.class) {
            return long.class;
        }
        if (ownerClass == Double.class) {
            return double.class;
        }
        if (ownerClass == Float.class) {
            return float.class;
        }
        if (ownerClass == Short.class) {
            return short.class;
        }
        if (ownerClass == Byte.class) {
            return byte.class;
        }
        if (ownerClass == Boolean.class) {
            return boolean.class;
        }
        if (ownerClass == Character.class) {
            return char.class;
        }
        return null;
    }

    private Method findUniqueReferenceStaticMethodForDynamicArguments(
            Class<?> ownerClass,
            String methodName,
            List<QinIrTypeRef> argumentTypes) {
        if (argumentTypes == null || argumentTypes.isEmpty()) {
            return null;
        }
        Method matched = null;
        for (Method candidate : ownerClass.getMethods()) {
            if (!Modifier.isStatic(candidate.getModifiers())
                    || candidate.isBridge()
                    || candidate.isSynthetic()
                    || !candidate.getName().equals(methodName)
                    || candidate.isVarArgs()
                    || candidate.getParameterCount() != argumentTypes.size()) {
                continue;
            }
            Class<?>[] parameterTypes = candidate.getParameterTypes();
            boolean dynamicSlotSeen = false;
            boolean applicable = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                QinIrTypeRef argumentType = argumentTypes.get(i);
                if (isDynamicUnknownReferenceArgument(argumentType)) {
                    dynamicSlotSeen = true;
                    if (parameterTypes[i] == void.class) {
                        applicable = false;
                        break;
                    }
                    continue;
                }
                if (!isArgumentApplicable(parameterTypes[i], argumentType)) {
                    applicable = false;
                    break;
                }
            }
            if (!dynamicSlotSeen || !applicable) {
                continue;
            }
            if (matched != null) {
                return null;
            }
            matched = candidate;
        }
        return matched;
    }

    private ResolvedStaticMethodCall toResolvedStaticMethodCall(Class<?> ownerClass, Method matched) {
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (Class<?> parameterType : matched.getParameterTypes()) {
            parameterTypes.add(toQinTypeRef(parameterType));
        }
        return new ResolvedStaticMethodCall(
                ownerClass.getName(),
                matched.getName(),
                List.copyOf(parameterTypes),
                toQinReflectedFieldTypeRef(matched.getReturnType()),
                ownerClass.isInterface(),
                toJavaMethodDescriptor(matched.getReturnType(), matched.getParameterTypes()),
                List.of(matched.getParameterTypes()),
                matched.isVarArgs());
    }

    private String reflectedStaticMethodOwnerBinaryName(String ownerBinaryName, String methodName) {
        ownerBinaryName = canonicalJavaSdkAliasBinaryName(ownerBinaryName);
        if (methodName == null || methodName.isBlank()) {
            return ownerBinaryName;
        }
        if (("of".equals(methodName)
                || "copyOf".equals(methodName)
                || "ofEntries".equals(methodName)
                || "entry".equals(methodName))
                && ("java.util.HashMap".equals(ownerBinaryName)
                || "java.util.LinkedHashMap".equals(ownerBinaryName)
                || "java.util.IdentityHashMap".equals(ownerBinaryName)
                || "java.util.Map".equals(ownerBinaryName))) {
            return "java.util.Map";
        }
        if (("of".equals(methodName) || "copyOf".equals(methodName))
                && ("java.util.ArrayList".equals(ownerBinaryName)
                || "java.util.List".equals(ownerBinaryName))) {
            return "java.util.List";
        }
        if (("of".equals(methodName) || "copyOf".equals(methodName))
                && ("java.util.HashSet".equals(ownerBinaryName)
                || "java.util.TreeSet".equals(ownerBinaryName)
                || "java.util.Set".equals(ownerBinaryName))) {
            return "java.util.Set";
        }
        return ownerBinaryName;
    }

    private ResolvedStaticMethodCall resolveEnclosingStaticMethodCall(
            QinIrTypeRef receiverType,
            String methodName,
            int argumentCount,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (receiverType == null
                || receiverType.kind() != QinIrTypeKind.CLASS
                || receiverType.binaryName() == null) {
            return null;
        }
        String enclosingBinaryName = enclosingBinaryName(receiverType.binaryName());
        if (enclosingBinaryName == null) {
            return null;
        }
        QinIrClassDeclaration enclosingDeclaration = declarationIndex.get(enclosingBinaryName);
        if (enclosingDeclaration == null) {
            return null;
        }
        QinIrMethodDeclaration matched = null;
        for (QinIrMethodDeclaration candidate : enclosingDeclaration.methods()) {
            if (!candidate.staticMethod()
                    || !candidate.name().equals(methodName)
                    || !isLocalMethodCountApplicable(candidate.parameters(), argumentCount)) {
                continue;
            }
            if (matched != null) {
                throw new IllegalArgumentException(
                        "Ambiguous enclosing static method: " + enclosingBinaryName + "." + methodName);
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
        return new ResolvedStaticMethodCall(
                enclosingDeclaration.binaryName(),
                matched.name(),
                List.copyOf(parameterTypes),
                matched.returnType(),
                false,
                null,
                List.of(),
                !matched.parameters().isEmpty()
                        && matched.parameters().get(matched.parameters().size() - 1).varargs());
    }

    private String enclosingBinaryName(String binaryName) {
        int nestedSeparator = binaryName.lastIndexOf('$');
        return nestedSeparator <= 0 ? null : binaryName.substring(0, nestedSeparator);
    }

    private ResolvedConstructorCall resolveConstructorCall(
            String ownerBinaryName,
            Map<String, QinIrClassDeclaration> declarationIndex,
            List<QinIrTypeRef> argumentTypes) {
        return resolveConstructorCall(ownerBinaryName, declarationIndex, argumentTypes, false);
    }

    private ResolvedConstructorCall resolveConstructorCall(
            String ownerBinaryName,
            Map<String, QinIrClassDeclaration> declarationIndex,
            List<QinIrTypeRef> argumentTypes,
            boolean superCallContext) {
        QinIrClassDeclaration localDeclaration = resolveIndexedDeclaration(declarationIndex, ownerBinaryName);
        if (localDeclaration == null) {
            String generatedFacadeBinaryName = generatedJavaSdkFacadeBinaryNameOrNull(ownerBinaryName);
            if (generatedFacadeBinaryName != null && !generatedFacadeBinaryName.equals(ownerBinaryName)) {
                localDeclaration = resolveIndexedDeclaration(declarationIndex, generatedFacadeBinaryName);
                if (localDeclaration != null) {
                    ownerBinaryName = generatedFacadeBinaryName;
                }
            }
        }
        if (localDeclaration != null) {
            String localOwnerBinaryName = localDeclaration.binaryName();
            if (Boolean.getBoolean("qin.declarationConstructor.trace")) {
                System.err.println("[QinJvmDeclarationClassEmitter] local constructor owner="
                        + ownerBinaryName
                        + " declaration=" + localDeclaration.binaryName()
                        + " args=" + argumentTypes
                        + " constructors=" + constructorTraceShape(localDeclaration));
            }
            ResolvedConstructorCall localConstructor =
                    resolveLocalConstructorCall(localOwnerBinaryName, localDeclaration, declarationIndex, argumentTypes);
            if (localConstructor != null) {
                return localConstructor;
            }
            if (Boolean.getBoolean("qin.declarationConstructor.trace")) {
                System.err.println("[QinJvmDeclarationClassEmitter] no local constructor match owner="
                        + ownerBinaryName
                        + " methods=" + localDeclaration.methods());
            }
        } else if (Boolean.getBoolean("qin.declarationConstructor.trace")) {
            System.err.println("[QinJvmDeclarationClassEmitter] missing local constructor owner="
                    + ownerBinaryName
                    + " declarationIndexSize=" + (declarationIndex == null ? 0 : declarationIndex.size())
                    + " nearKeys=" + nearbyDeclarationIndexKeys(declarationIndex, ownerBinaryName));
        }
        if (isGeneratedSourceClassBinaryName(ownerBinaryName) && argumentTypes.isEmpty()) {
            return new ResolvedConstructorCall(
                    ownerBinaryName,
                    List.of(),
                    VOID_INIT,
                    false,
                    List.of());
        }
        if (isGeneratedSourceClassBinaryName(ownerBinaryName)) {
            List<QinIrTypeRef> parameterTypes = generatedSourceConstructorParameterTypes(argumentTypes);
            return new ResolvedConstructorCall(
                    ownerBinaryName,
                    parameterTypes,
                    toConstructorDescriptorForTypes(parameterTypes),
                    false,
                    List.of());
        }
        ownerBinaryName = canonicalJavaSdkAliasBinaryName(ownerBinaryName);
        try {
            Class<?> ownerClass = Class.forName(ownerBinaryName);
            List<Constructor<?>> constructorCandidates = reflectedConstructorCandidates(ownerClass, superCallContext);
            Constructor<?> matched = null;
            int matchedScore = Integer.MIN_VALUE;
            for (Constructor<?> candidate : constructorCandidates) {
                if (candidate.getParameterCount() != argumentTypes.size()
                        || !isReflectedExecutableCountApplicable(
                                candidate.getParameterTypes(),
                                candidate.isVarArgs(),
                                argumentTypes.size())
                        || !isExecutableApplicable(
                                candidate.getParameterTypes(),
                                candidate.isVarArgs(),
                                argumentTypes)) {
                    continue;
                }
                int score = executableMatchScore(candidate.getParameterTypes(), candidate.isVarArgs(), argumentTypes);
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
                matched = findUniqueReferenceConstructorForDynamicArguments(constructorCandidates, argumentTypes);
            }
            if (matched == null) {
                matched = findUniqueStringConstructorForDynamicArguments(constructorCandidates, argumentTypes);
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
                    ownerBinaryName,
                    List.copyOf(parameterTypes),
                    toJavaConstructorDescriptor(matched.getParameterTypes()),
                    matched.isVarArgs(),
                    List.of(matched.getParameterTypes()));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown Java constructor owner: " + ownerBinaryName, e);
        }
    }

    private Constructor<?> findUniqueReferenceConstructorForDynamicArguments(
            List<Constructor<?>> candidates,
            List<QinIrTypeRef> argumentTypes) {
        if (candidates == null || candidates.isEmpty() || argumentTypes == null || argumentTypes.isEmpty()) {
            return null;
        }
        Constructor<?> matched = null;
        for (Constructor<?> candidate : candidates) {
            if (candidate.isVarArgs() || candidate.getParameterCount() != argumentTypes.size()) {
                continue;
            }
            Class<?>[] parameterTypes = candidate.getParameterTypes();
            boolean dynamicSlotSeen = false;
            boolean applicable = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                QinIrTypeRef argumentType = argumentTypes.get(i);
                if (isDynamicUnknownReferenceArgument(argumentType)) {
                    dynamicSlotSeen = true;
                    if (parameterTypes[i].isPrimitive()) {
                        applicable = false;
                        break;
                    }
                    continue;
                }
                if (!isArgumentApplicable(parameterTypes[i], argumentType)) {
                    applicable = false;
                    break;
                }
            }
            if (!dynamicSlotSeen || !applicable) {
                continue;
            }
            if (matched != null) {
                return null;
            }
            matched = candidate;
        }
        return matched;
    }

    private Constructor<?> findUniqueStringConstructorForDynamicArguments(
            List<Constructor<?>> candidates,
            List<QinIrTypeRef> argumentTypes) {
        if (candidates == null || candidates.isEmpty() || argumentTypes == null || argumentTypes.isEmpty()) {
            return null;
        }
        Constructor<?> matched = null;
        for (Constructor<?> candidate : candidates) {
            if (candidate.isVarArgs() || candidate.getParameterCount() != argumentTypes.size()) {
                continue;
            }
            Class<?>[] parameterTypes = candidate.getParameterTypes();
            boolean dynamicSlotSeen = false;
            boolean applicable = true;
            for (int i = 0; i < parameterTypes.length; i++) {
                QinIrTypeRef argumentType = argumentTypes.get(i);
                if (isDynamicUnknownReferenceArgument(argumentType)) {
                    dynamicSlotSeen = true;
                    if (parameterTypes[i] != String.class) {
                        applicable = false;
                        break;
                    }
                    continue;
                }
                if (!isArgumentApplicable(parameterTypes[i], argumentType)) {
                    applicable = false;
                    break;
                }
            }
            if (!dynamicSlotSeen || !applicable) {
                continue;
            }
            if (matched != null) {
                return null;
            }
            matched = candidate;
        }
        return matched;
    }

    private boolean isDynamicUnknownReferenceArgument(QinIrTypeRef argumentType) {
        return argumentType == null
                || (argumentType.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object".equals(argumentType.binaryName()));
    }

    private boolean isGeneratedSourceClassBinaryName(String ownerBinaryName) {
        return ownerBinaryName != null
                && ownerBinaryName.startsWith("com_")
                && !ownerBinaryName.contains(".");
    }

    private List<QinIrTypeRef> generatedSourceConstructorParameterTypes(List<QinIrTypeRef> argumentTypes) {
        if (argumentTypes == null || argumentTypes.isEmpty()) {
            return List.of();
        }
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (QinIrTypeRef argumentType : argumentTypes) {
            parameterTypes.add(generatedSourceConstructorParameterType(argumentType));
        }
        return List.copyOf(parameterTypes);
    }

    private QinIrTypeRef generatedSourceConstructorParameterType(QinIrTypeRef argumentType) {
        if (argumentType == null || argumentType.kind() != QinIrTypeKind.CLASS || argumentType.binaryName() == null) {
            return argumentType == null ? QinIrTypeRef.classType("java.lang.Object") : argumentType;
        }
        return switch (argumentType.binaryName()) {
            case "java.lang.Boolean" -> QinIrTypeRef.booleanType();
            case "java.lang.Integer" -> QinIrTypeRef.intType();
            case "java.lang.Double", "java.lang.Float", "java.lang.Long",
                    "java.lang.Short", "java.lang.Byte", "java.lang.Number" -> QinIrTypeRef.doubleType();
            default -> argumentType;
        };
    }

    private List<Constructor<?>> reflectedConstructorCandidates(Class<?> ownerClass, boolean superCallContext) {
        if (!superCallContext) {
            return List.of(ownerClass.getConstructors());
        }
        List<Constructor<?>> constructors = new ArrayList<>();
        for (Constructor<?> constructor : ownerClass.getDeclaredConstructors()) {
            int modifiers = constructor.getModifiers();
            if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                constructors.add(constructor);
            }
        }
        return List.copyOf(constructors);
    }

    private List<String> nearbyDeclarationIndexKeys(
            Map<String, QinIrClassDeclaration> declarationIndex,
            String ownerBinaryName) {
        if (declarationIndex == null || declarationIndex.isEmpty() || ownerBinaryName == null) {
            return List.of();
        }
        String foldedOwner = flattenedBinaryAlias(ownerBinaryName).toLowerCase(Locale.ROOT);
        List<String> keys = new ArrayList<>();
        for (String key : declarationIndex.keySet()) {
            if (key == null) {
                continue;
            }
            String foldedKey = flattenedBinaryAlias(key).toLowerCase(Locale.ROOT);
            if (foldedKey.contains(foldedOwner) || foldedOwner.contains(foldedKey)) {
                keys.add(key);
            }
            if (keys.size() >= 8) {
                break;
            }
        }
        return List.copyOf(keys);
    }

    private ResolvedConstructorCall resolveLocalConstructorCall(
            String ownerBinaryName,
            QinIrClassDeclaration declaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            List<QinIrTypeRef> argumentTypes) {
        List<QinIrMethodDeclaration> explicitConstructors = explicitConstructors(declaration);
        if (explicitConstructors.isEmpty()) {
            ResolvedConstructorCall synthesizedConstructor = resolveSynthesizedLocalConstructorCall(
                    ownerBinaryName,
                    declaration,
                    declarationIndex,
                    argumentTypes);
            if (synthesizedConstructor != null) {
                return synthesizedConstructor;
            }
            if (argumentTypes.isEmpty()) {
                return new ResolvedConstructorCall(ownerBinaryName, List.of(), VOID_INIT, false, List.of());
            }
            return null;
        }
        QinIrMethodDeclaration matched = null;
        QinIrMethodDeclaration paddedMatch = null;
        for (QinIrMethodDeclaration constructor : explicitConstructors) {
            if (!isLocalMethodCountApplicable(constructor.parameters(), argumentTypes.size())
                    || !isLocalConstructorApplicable(constructor.parameters(), argumentTypes, declarationIndex)) {
                continue;
            }
            if (constructor.parameters().size() == argumentTypes.size()) {
                if (matched != null) {
                    throw new IllegalArgumentException(
                            "Ambiguous local constructor overload: " + ownerBinaryName + "/" + argumentTypes.size());
                }
                matched = constructor;
                continue;
            }
            if (paddedMatch != null) {
                throw new IllegalArgumentException(
                        "Ambiguous local constructor overload: " + ownerBinaryName + "/" + argumentTypes.size());
            }
            paddedMatch = constructor;
        }
        if (matched == null) {
            matched = paddedMatch;
        }
        if (matched == null) {
            return null;
        }
        List<QinIrTypeRef> parameterTypes = new ArrayList<>();
        for (QinIrParameter parameter : matched.parameters()) {
            parameterTypes.add(parameter.type());
        }
        return new ResolvedConstructorCall(
                ownerBinaryName,
                List.copyOf(parameterTypes),
                toConstructorDescriptorForTypes(parameterTypes),
                hasVarargsParameter(matched),
                List.of());
    }

    private ResolvedConstructorCall resolveSynthesizedLocalConstructorCall(
            String ownerBinaryName,
            QinIrClassDeclaration declaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            List<QinIrTypeRef> argumentTypes) {
        List<List<QinIrTypeRef>> constructorParameterLists = constructorParameterListsForLocalDeclaration(
                declaration,
                declarationIndex,
                new java.util.LinkedHashSet<>());
        List<QinIrTypeRef> matched = null;
        String matchedDescriptor = null;
        boolean matchedVarargs = false;
        for (List<QinIrTypeRef> parameterTypes : constructorParameterLists) {
            boolean varargs = isSyntheticObjectArrayVarargsConstructor(parameterTypes, argumentTypes.size());
            if (!isLocalConstructorParameterListApplicable(parameterTypes, varargs, argumentTypes, declarationIndex)) {
                continue;
            }
            String descriptor = toConstructorDescriptorForTypes(parameterTypes).descriptorString();
            if (matched != null) {
                if (descriptor.equals(matchedDescriptor)) {
                    continue;
                }
                throw new IllegalArgumentException(
                        "Ambiguous synthesized local constructor overload: "
                                + ownerBinaryName
                                + "/"
                                + argumentTypes.size());
            }
            matched = parameterTypes;
            matchedDescriptor = descriptor;
            matchedVarargs = varargs;
        }
        if (matched == null) {
            return null;
        }
        return new ResolvedConstructorCall(
                ownerBinaryName,
                List.copyOf(matched),
                toConstructorDescriptorForTypes(matched),
                matchedVarargs,
                List.of());
    }

    private boolean isSyntheticObjectArrayVarargsConstructor(
            List<QinIrTypeRef> parameterTypes,
            int argumentCount) {
        if (parameterTypes == null || parameterTypes.isEmpty() || argumentCount > parameterTypes.size()) {
            return false;
        }
        QinIrTypeRef last = parameterTypes.get(parameterTypes.size() - 1);
        return last != null
                && last.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object[]".equals(last.binaryName());
    }

    private boolean isLocalConstructorParameterListApplicable(
            List<QinIrTypeRef> parameterTypes,
            boolean varargs,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (parameterTypes == null || argumentTypes == null) {
            return false;
        }
        if (!varargs && argumentTypes.size() > parameterTypes.size()) {
            return false;
        }
        int fixedParameterCount = varargs ? parameterTypes.size() - 1 : parameterTypes.size();
        if (argumentTypes.size() < fixedParameterCount) {
            return false;
        }
        int checkedParameterCount = varargs ? fixedParameterCount : argumentTypes.size();
        for (int i = 0; i < checkedParameterCount; i++) {
            if (!isLocalIrArgumentApplicable(parameterTypes.get(i), argumentTypes.get(i), declarationIndex)) {
                return false;
            }
        }
        if (!varargs) {
            return true;
        }
        QinIrTypeRef elementType = localVarargsElementType(parameterTypes.get(parameterTypes.size() - 1));
        for (int i = fixedParameterCount; i < argumentTypes.size(); i++) {
            if (!isLocalIrArgumentApplicable(elementType, argumentTypes.get(i), declarationIndex)) {
                return false;
            }
        }
        return true;
    }

    private String constructorTraceShape(QinIrClassDeclaration declaration) {
        if (declaration == null) {
            return "[]";
        }
        List<String> shapes = new ArrayList<>();
        for (QinIrMethodDeclaration method : explicitConstructors(declaration)) {
            List<String> parameters = new ArrayList<>();
            for (QinIrParameter parameter : method.parameters()) {
                parameters.add(parameter.type().binaryName() + (parameter.varargs() ? "..." : ""));
            }
            shapes.add(method.name() + "(" + String.join(",", parameters) + ")");
        }
        return shapes.toString();
    }

    private QinIrClassDeclaration resolveIndexedDeclaration(
            Map<String, QinIrClassDeclaration> declarationIndex,
            String ownerBinaryName) {
        if (isQinHostRuntimeBinaryName(ownerBinaryName)) {
            return null;
        }
        if (declarationIndex == null || ownerBinaryName == null || ownerBinaryName.isBlank()) {
            return null;
        }
        QinIrClassDeclaration declaration = declarationIndex.get(ownerBinaryName);
        if (declaration != null) {
            QinIrClassDeclaration generatedLocalDeclaration =
                    generatedLocalDeclarationForOriginalOwner(declarationIndex, declaration);
            if (generatedLocalDeclaration == null) {
                generatedLocalDeclaration =
                        uniqueGeneratedLocalDeclarationForSimpleOriginalOwner(declarationIndex, ownerBinaryName);
            }
            return generatedLocalDeclaration == null ? declaration : generatedLocalDeclaration;
        }
        declaration = uniqueGeneratedLocalDeclarationForSimpleOriginalOwner(declarationIndex, ownerBinaryName);
        if (declaration != null) {
            return declaration;
        }
        String flattenedOwner = flattenedBinaryAlias(ownerBinaryName);
        declaration = declarationIndex.get(flattenedOwner);
        if (declaration != null) {
            return declaration;
        }
        return null;
    }

    private QinIrClassDeclaration generatedLocalDeclarationForOriginalOwner(
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrClassDeclaration declaration) {
        if (declarationIndex == null
                || declaration == null
                || declaration.binaryName() == null
                || declaration.binaryName().isBlank()
                || !declaration.binaryName().contains(".")) {
            return null;
        }
        String flattenedOwner = flattenedBinaryAlias(declaration.binaryName());
        QinIrClassDeclaration localDeclaration = declarationIndex.get(flattenedOwner);
        if (localDeclaration == null
                || localDeclaration.binaryName() == null
                || localDeclaration.binaryName().isBlank()
                || Objects.equals(localDeclaration.binaryName(), declaration.binaryName())
                || !Objects.equals(localDeclaration.binaryName(), flattenedOwner)) {
            return null;
        }
        return localDeclaration;
    }

    private QinIrClassDeclaration uniqueGeneratedLocalDeclarationForSimpleOriginalOwner(
            Map<String, QinIrClassDeclaration> declarationIndex,
            String ownerBinaryName) {
        if (declarationIndex == null
                || declarationIndex.isEmpty()
                || ownerBinaryName == null
                || ownerBinaryName.isBlank()
                || ownerBinaryName.contains(".")
                || ownerBinaryName.contains("_")) {
            return null;
        }
        QinIrClassDeclaration matched = null;
        for (QinIrClassDeclaration candidate : declarationIndex.values()) {
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

    private boolean isLocalConstructorApplicable(
            List<QinIrParameter> parameters,
            List<QinIrTypeRef> argumentTypes,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (parameters.isEmpty()) {
            return argumentTypes.isEmpty();
        }
        boolean varargs = parameters.get(parameters.size() - 1).varargs();
        int fixedParameterCount = varargs ? parameters.size() - 1 : parameters.size();
        if (!varargs && argumentTypes.size() > parameters.size()) {
            return false;
        }
        if (varargs && argumentTypes.size() < fixedParameterCount) {
            return false;
        }
        int checkedParameterCount = varargs ? fixedParameterCount : argumentTypes.size();
        for (int i = 0; i < checkedParameterCount; i++) {
            if (!isLocalIrArgumentApplicable(parameters.get(i).type(), argumentTypes.get(i), declarationIndex)) {
                return false;
            }
        }
        if (!varargs) {
            return true;
        }
        QinIrTypeRef varargsArrayType = parameters.get(parameters.size() - 1).type();
        QinIrTypeRef varargsElementType = localVarargsElementType(varargsArrayType);
        if (argumentTypes.size() == parameters.size()
                && isLocalIrArgumentApplicable(varargsArrayType, argumentTypes.get(fixedParameterCount), declarationIndex)) {
            return true;
        }
        for (int i = fixedParameterCount; i < argumentTypes.size(); i++) {
            if (!isLocalIrArgumentApplicable(varargsElementType, argumentTypes.get(i), declarationIndex)) {
                return false;
            }
        }
        return true;
    }

    private int localMethodMatchScore(
            List<QinIrParameter> parameters,
            List<QinIrTypeRef> argumentTypes) {
        return localMethodMatchScore(parameters, argumentTypes, null);
    }

    private int localMethodMatchScore(
            List<QinIrParameter> parameters,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions) {
        return localMethodMatchScore(parameters, argumentTypes, argumentExpressions, null);
    }

    private int localMethodMatchScore(
            List<QinIrParameter> parameters,
            List<QinIrTypeRef> argumentTypes,
            List<QinIrExpression> argumentExpressions,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (parameters == null || argumentTypes == null) {
            return -1;
        }
        if (parameters.isEmpty()) {
            return argumentTypes.isEmpty() ? 0 : -1;
        }
        boolean varargs = parameters.get(parameters.size() - 1).varargs();
        int fixedParameterCount = varargs ? parameters.size() - 1 : parameters.size();
        if (!varargs && argumentTypes.size() > parameters.size()) {
            return -1;
        }
        if (varargs && argumentTypes.size() < fixedParameterCount) {
            return -1;
        }
        int score = 0;
        int checkedParameterCount = varargs ? fixedParameterCount : argumentTypes.size();
        for (int i = 0; i < checkedParameterCount; i++) {
            int parameterScore = localIrArgumentMatchScore(
                    parameters.get(i).type(),
                    argumentTypes.get(i),
                    localArgumentExpression(argumentExpressions, i),
                    declarationIndex);
            if (parameterScore < 0) {
                return -1;
            }
            score += parameterScore;
        }
        if (!varargs) {
            return score;
        }
        QinIrTypeRef varargsArrayType = parameters.get(parameters.size() - 1).type();
        if (argumentTypes.size() == parameters.size()) {
            int packedScore = localIrArgumentMatchScore(
                    varargsArrayType,
                    argumentTypes.get(fixedParameterCount),
                    localArgumentExpression(argumentExpressions, fixedParameterCount),
                    declarationIndex);
            if (packedScore >= 0) {
                return score + packedScore;
            }
        }
        QinIrTypeRef varargsElementType = localVarargsElementType(varargsArrayType);
        for (int i = fixedParameterCount; i < argumentTypes.size(); i++) {
            int parameterScore = localIrArgumentMatchScore(
                    varargsElementType,
                    argumentTypes.get(i),
                    localArgumentExpression(argumentExpressions, i),
                    declarationIndex);
            if (parameterScore < 0) {
                return -1;
            }
            score += parameterScore;
        }
        return score - 1;
    }

    private QinIrExpression localArgumentExpression(List<QinIrExpression> argumentExpressions, int index) {
        return argumentExpressions == null || index < 0 || index >= argumentExpressions.size()
                ? null
                : argumentExpressions.get(index);
    }

    private QinIrTypeRef localVarargsElementType(QinIrTypeRef arrayType) {
        return varargsElementType(arrayType);
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

    private TypeKind primitiveArrayKind(QinIrTypeRef componentType) {
        if (componentType == null) {
            return null;
        }
        return switch (componentType.kind()) {
            case BOOLEAN -> TypeKind.BOOLEAN;
            case INT -> TypeKind.INT;
            case DOUBLE -> TypeKind.DOUBLE;
            default -> null;
        };
    }

    private TypeKind primitiveTypeKind(Class<?> type) {
        if (type == boolean.class) {
            return TypeKind.BOOLEAN;
        }
        if (type == byte.class) {
            return TypeKind.BYTE;
        }
        if (type == char.class) {
            return TypeKind.CHAR;
        }
        if (type == short.class) {
            return TypeKind.SHORT;
        }
        if (type == int.class) {
            return TypeKind.INT;
        }
        if (type == long.class) {
            return TypeKind.LONG;
        }
        if (type == float.class) {
            return TypeKind.FLOAT;
        }
        if (type == double.class) {
            return TypeKind.DOUBLE;
        }
        return null;
    }

    private Class<?> reflectedParameterType(List<Class<?>> parameterTypes, int index) {
        if (parameterTypes == null || index < 0 || index >= parameterTypes.size()) {
            return null;
        }
        return parameterTypes.get(index);
    }

    private boolean isIrArgumentApplicable(QinIrTypeRef parameterType, QinIrTypeRef argumentType) {
        if (parameterType == null || argumentType == null) {
            return false;
        }
        if (parameterType.equals(argumentType)) {
            return true;
        }
        if (argumentType.kind() == QinIrTypeKind.CLASS
                && "java.lang.Object".equals(argumentType.binaryName())) {
            return parameterType.kind() == QinIrTypeKind.CLASS
                    && "java.lang.Object".equals(parameterType.binaryName());
        }
        if (parameterType.kind() == QinIrTypeKind.DOUBLE && isNumericLike(argumentType)) {
            return true;
        }
        if (parameterType.kind() == QinIrTypeKind.INT && isNumericLike(argumentType)) {
            return true;
        }
        if (parameterType.kind() == QinIrTypeKind.BOOLEAN && isBooleanLike(argumentType)) {
            return true;
        }
        if (parameterType.kind() == QinIrTypeKind.CLASS
                && isBoxedNumericBinaryName(parameterType.binaryName())
                && isNumericLike(argumentType)) {
            return true;
        }
        if (parameterType.kind() == QinIrTypeKind.CLASS
                && "java.lang.Boolean".equals(parameterType.binaryName())
                && isBooleanLike(argumentType)) {
            return true;
        }
        if (parameterType.kind() == QinIrTypeKind.CLASS
                && argumentType.kind() == QinIrTypeKind.CLASS
                && isReflectedClassAssignableTo(argumentType.binaryName(), parameterType.binaryName())) {
            return true;
        }
        return parameterType.kind() == QinIrTypeKind.CLASS
                && argumentType.kind() == QinIrTypeKind.CLASS
                && Objects.equals(parameterType.binaryName(), argumentType.binaryName());
    }

    private boolean isReflectedClassAssignableTo(String argumentBinaryName, String parameterBinaryName) {
        if (argumentBinaryName == null || parameterBinaryName == null) {
            return false;
        }
        try {
            Class<?> argumentClass = Class.forName(QinJavaSdkAliasSupport.canonicalBinaryName(argumentBinaryName));
            Class<?> parameterClass = Class.forName(QinJavaSdkAliasSupport.canonicalBinaryName(parameterBinaryName));
            return parameterClass.isAssignableFrom(argumentClass);
        } catch (ClassNotFoundException | LinkageError ignored) {
            return false;
        }
    }

    private boolean isLocalIrArgumentApplicable(QinIrTypeRef parameterType, QinIrTypeRef argumentType) {
        return localIrArgumentMatchScore(parameterType, argumentType) >= 0;
    }

    private boolean isLocalIrArgumentApplicable(
            QinIrTypeRef parameterType,
            QinIrTypeRef argumentType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return localIrArgumentMatchScore(parameterType, argumentType, declarationIndex) >= 0;
    }

    private int localIrArgumentMatchScore(QinIrTypeRef parameterType, QinIrTypeRef argumentType) {
        return localIrArgumentMatchScore(parameterType, argumentType, (Map<String, QinIrClassDeclaration>) null);
    }

    private int localIrArgumentMatchScore(
            QinIrTypeRef parameterType,
            QinIrTypeRef argumentType,
            QinIrExpression argumentExpression) {
        return localIrArgumentMatchScore(parameterType, argumentType, argumentExpression, null);
    }

    private int localIrArgumentMatchScore(
            QinIrTypeRef parameterType,
            QinIrTypeRef argumentType,
            QinIrExpression argumentExpression,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (parameterType != null
                && parameterType.kind() == QinIrTypeKind.CLASS
                && isJavaFunctionalObjectType(argumentType)
                && isJavaFunctionalInterfaceType(parameterType)) {
            return localJavaFunctionalArgumentMatchScore(parameterType, argumentExpression);
        }
        QinIrTypeRef effectiveArgumentType = generatedOverloadEffectiveArgumentType(
                argumentType,
                argumentExpression,
                declarationIndex);
        if (effectiveArgumentType != null && !sameIrType(argumentType, effectiveArgumentType)) {
            int effectiveScore = localIrArgumentMatchScore(parameterType, effectiveArgumentType, declarationIndex);
            if (effectiveScore >= 0) {
                return effectiveScore + 10;
            }
        }
        int score = localIrArgumentMatchScore(parameterType, argumentType, declarationIndex);
        if (score >= 0) {
            return score;
        }
        if (isStaticArrayArgumentExpressionForTarget(parameterType, argumentExpression)) {
            return 3;
        }
        if (argumentExpression instanceof QinIrNullLiteral && isReferenceLikeParameter(parameterType)) {
            return 3;
        }
        if (isCollectionGetBuiltinCall(argumentExpression) && isReferenceLikeParameter(parameterType)) {
            return 2;
        }
        if (isJavaLangStringCharAtCall(argumentExpression)
                && parameterType != null
                && (parameterType.kind() == QinIrTypeKind.INT || parameterType.kind() == QinIrTypeKind.DOUBLE)) {
            return 3;
        }
        return -1;
    }

    private boolean isStaticArrayArgumentExpressionForTarget(
            QinIrTypeRef parameterType,
            QinIrExpression argumentExpression) {
        QinIrTypeRef componentType = staticArrayParameterElementType(parameterType);
        if (componentType == null || argumentExpression == null) {
            return false;
        }
        if (argumentExpression instanceof QinIrArrayLiteral) {
            return true;
        }
        if (argumentExpression instanceof QinIrInstanceMethodCallExpression methodCallExpression) {
            return canEmitStaticArrayFromFactoryAsTypedArray(
                    staticArrayFromFactory(methodCallExpression),
                    componentType);
        }
        return false;
    }

    private boolean isReferenceLikeParameter(QinIrTypeRef parameterType) {
        return parameterType != null
                && (parameterType.kind() == QinIrTypeKind.CLASS || parameterType.kind() == QinIrTypeKind.STRING);
    }

    private boolean isCollectionGetBuiltinCall(QinIrExpression expression) {
        return expression instanceof QinIrBuiltinCallExpression builtinCallExpression
                && "Global".equals(builtinCallExpression.receiverName())
                && "__qin_collection_get__".equals(builtinCallExpression.methodName())
                && builtinCallExpression.arguments().size() == 2;
    }

    private int localIrArgumentMatchScore(
            QinIrTypeRef parameterType,
            QinIrTypeRef argumentType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (parameterType == null || argumentType == null || argumentType.kind() == QinIrTypeKind.VOID) {
            return -1;
        }
        if (parameterType.equals(argumentType)) {
            return 8;
        }
        if (parameterType.kind() == QinIrTypeKind.DOUBLE && isNumericLike(argumentType)) {
            return 3;
        }
        if (parameterType.kind() == QinIrTypeKind.INT && isNumericLike(argumentType)) {
            return 3;
        }
        if (parameterType.kind() == QinIrTypeKind.BOOLEAN && isBooleanLike(argumentType)) {
            return 3;
        }
        if (parameterType.kind() == QinIrTypeKind.CLASS) {
            if (isJavaFunctionalObjectType(argumentType) && isJavaFunctionalInterfaceType(parameterType)) {
                return 2;
            }
            int boxedNumericScore = boxedNumericArgumentMatchScore(parameterType.binaryName(), argumentType);
            if (boxedNumericScore >= 0) {
                return boxedNumericScore;
            }
            int boxedBooleanScore = boxedBooleanArgumentMatchScore(parameterType.binaryName(), argumentType);
            if (boxedBooleanScore >= 0) {
                return boxedBooleanScore;
            }
        }
        if (isJavaLangObjectType(parameterType)) {
            return 1;
        }
        if (parameterType.kind() == QinIrTypeKind.CLASS
                && argumentType.kind() == QinIrTypeKind.CLASS) {
            if (isAnyArrayType(parameterType) && isAnyArrayType(argumentType)) {
                return localArrayArgumentMatchScore(parameterType, argumentType, declarationIndex);
            }
            if (Objects.equals(parameterType.binaryName(), argumentType.binaryName())) {
                int typeArgumentScore = localTypeArgumentMatchScore(parameterType, argumentType, declarationIndex);
                return typeArgumentScore < 0 ? -1 : 4 + typeArgumentScore;
            }
            if (isLocalClassAssignableTo(argumentType.binaryName(), parameterType.binaryName(), declarationIndex)) {
                return 2;
            }
        }
        return isIrArgumentApplicable(parameterType, argumentType) ? 1 : -1;
    }

    private int localJavaFunctionalArgumentMatchScore(
            QinIrTypeRef parameterType,
            QinIrExpression argumentExpression) {
        Class<?> returnType = javaFunctionalInterfaceReturnType(parameterType);
        if (returnType == null) {
            return 2;
        }
        FunctionValueShape valueShape = javaFunctionalArgumentValueShape(argumentExpression);
        if (returnType == void.class) {
            return switch (valueShape) {
                case VOID_COMPATIBLE -> 7;
                case UNKNOWN -> 2;
                case VALUE_COMPATIBLE -> -1;
            };
        }
        return switch (valueShape) {
            case VALUE_COMPATIBLE -> 7;
            case UNKNOWN -> 2;
            case VOID_COMPATIBLE -> -1;
        };
    }

    private QinIrTypeRef primitiveJavaStreamMethodReturnType(
            QinIrTypeRef receiverType,
            QinIrInstanceMethodCallExpression methodCallExpression) {
        if (receiverType == null
                || methodCallExpression == null
                || methodCallExpression.arguments().size() != 1
                || !"mapToObj".equals(methodCallExpression.methodName())) {
            return null;
        }
        return switch (receiverType.binaryName()) {
            case "java.util.stream.IntStream",
                    "java.util.stream.LongStream",
                    "java.util.stream.DoubleStream" ->
                    QinIrTypeRef.classType("java.util.stream.Stream", List.of(QinIrTypeRef.classType("java.lang.Object")));
            default -> null;
        };
    }

    private Class<?> javaFunctionalInterfaceReturnType(QinIrTypeRef targetType) {
        if (targetType == null
                || targetType.kind() != QinIrTypeKind.CLASS
                || targetType.binaryName() == null
                || targetType.binaryName().isBlank()) {
            return null;
        }
        try {
            Method method = javaFunctionalInterfaceMethod(resolveClass(targetType.binaryName()));
            return method == null ? null : method.getReturnType();
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private Method javaFunctionalInterfaceMethod(Class<?> targetClass) {
        if (targetClass == null || !targetClass.isInterface()) {
            return null;
        }
        Method matched = null;
        for (Method candidate : targetClass.getMethods()) {
            if (candidate.getDeclaringClass() == Object.class || isObjectMethodSignature(candidate)) {
                continue;
            }
            int modifiers = candidate.getModifiers();
            if (!Modifier.isAbstract(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            if (matched != null) {
                return null;
            }
            matched = candidate;
        }
        return matched;
    }

    private FunctionValueShape javaFunctionalArgumentValueShape(QinIrExpression argumentExpression) {
        QinIrExpression callableExpression = javaFunctionalCallableExpression(argumentExpression);
        if (!(callableExpression instanceof QinIrObjectLiteral functionDefinition)) {
            return FunctionValueShape.UNKNOWN;
        }
        QinIrExpression valueShape = objectProperty(functionDefinition, "__qin_function_value_shape");
        if (valueShape instanceof QinIrStringLiteral literal) {
            if ("value".equals(literal.value())) {
                return FunctionValueShape.VALUE_COMPATIBLE;
            }
            if ("void".equals(literal.value())) {
                return FunctionValueShape.VOID_COMPATIBLE;
            }
        }
        QinIrExpression ast = objectProperty(functionDefinition, "ast");
        if (ast == null) {
            return FunctionValueShape.UNKNOWN;
        }
        return functionAstValueShape(ast);
    }

    private FunctionValueShape functionAstValueShape(QinIrExpression ast) {
        if (!(ast instanceof QinIrObjectLiteral objectLiteral)) {
            return FunctionValueShape.UNKNOWN;
        }
        String type = encodedAstType(objectLiteral);
        if ("ArrowFunctionExpression".equals(type)) {
            QinIrExpression expression = objectProperty(objectLiteral, "expression");
            if (expression instanceof QinIrBooleanLiteral literal && literal.value()) {
                return FunctionValueShape.VALUE_COMPATIBLE;
            }
            return functionBodyValueShape(objectProperty(objectLiteral, "body"));
        }
        if ("FunctionExpression".equals(type) || "FunctionDeclaration".equals(type)) {
            return functionBodyValueShape(objectProperty(objectLiteral, "body"));
        }
        return functionBodyValueShape(ast);
    }

    private FunctionValueShape functionBodyValueShape(QinIrExpression body) {
        if (body == null) {
            return FunctionValueShape.UNKNOWN;
        }
        if (!(body instanceof QinIrObjectLiteral objectLiteral)) {
            return FunctionValueShape.VALUE_COMPATIBLE;
        }
        if (!"BlockStatement".equals(encodedAstType(objectLiteral))) {
            return FunctionValueShape.VALUE_COMPATIBLE;
        }
        return encodedAstContainsValueReturn(objectProperty(objectLiteral, "body"), 0)
                ? FunctionValueShape.VALUE_COMPATIBLE
                : FunctionValueShape.VOID_COMPATIBLE;
    }

    private boolean encodedAstContainsValueReturn(QinIrExpression expression, int depth) {
        if (expression == null || depth > 100) {
            return false;
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            for (QinIrExpression element : arrayLiteral.elements()) {
                if (encodedAstContainsValueReturn(element, depth + 1)) {
                    return true;
                }
            }
            return false;
        }
        if (!(expression instanceof QinIrObjectLiteral objectLiteral)) {
            return false;
        }
        String type = encodedAstType(objectLiteral);
        if ("FunctionExpression".equals(type)
                || "FunctionDeclaration".equals(type)
                || "ArrowFunctionExpression".equals(type)) {
            return false;
        }
        if ("ReturnStatement".equals(type)) {
            QinIrExpression argument = objectProperty(objectLiteral, "argument");
            return argument != null && !(argument instanceof QinIrNullLiteral);
        }
        for (QinIrObjectProperty property : objectLiteral.properties()) {
            if (encodedAstContainsValueReturn(property.value(), depth + 1)) {
                return true;
            }
        }
        return false;
    }

    private QinIrExpression objectProperty(QinIrObjectLiteral objectLiteral, String key) {
        if (objectLiteral == null || key == null) {
            return null;
        }
        for (QinIrObjectProperty property : objectLiteral.properties()) {
            if (key.equals(property.key())) {
                return property.value();
            }
        }
        return null;
    }

    private String encodedAstType(QinIrObjectLiteral objectLiteral) {
        QinIrExpression type = objectProperty(objectLiteral, "type");
        return type instanceof QinIrStringLiteral literal ? literal.value() : null;
    }

    private QinIrTypeRef castTypeRef(String typeName) {
        if (typeName == null || typeName.isBlank()) {
            return QinIrTypeRef.classType("java.lang.Object");
        }
        return switch (typeName) {
            case "boolean" -> QinIrTypeRef.booleanType();
            case "byte", "short", "int", "char" -> QinIrTypeRef.intType();
            case "float", "double" -> QinIrTypeRef.doubleType();
            case "String", "java.lang.String" -> QinIrTypeRef.stringType();
            default -> QinIrTypeRef.classType(QinJavaSdkAliasSupport.canonicalBinaryName(typeName));
        };
    }

    private int boxedNumericArgumentMatchScore(String parameterBinaryName, QinIrTypeRef argumentType) {
        if (!isBoxedNumericBinaryName(parameterBinaryName) || !isNumericLike(argumentType)) {
            return -1;
        }
        if (argumentType.kind() == QinIrTypeKind.INT) {
            return "java.lang.Integer".equals(parameterBinaryName) ? 4 : 2;
        }
        if (argumentType.kind() == QinIrTypeKind.DOUBLE) {
            return "java.lang.Double".equals(parameterBinaryName) ? 4 : 2;
        }
        if (argumentType.kind() == QinIrTypeKind.CLASS
                && Objects.equals(parameterBinaryName, argumentType.binaryName())) {
            return 4;
        }
        return "java.lang.Number".equals(parameterBinaryName) ? 2 : 1;
    }

    private int boxedBooleanArgumentMatchScore(String parameterBinaryName, QinIrTypeRef argumentType) {
        if (!"java.lang.Boolean".equals(parameterBinaryName) || !isBooleanLike(argumentType)) {
            return -1;
        }
        return argumentType.kind() == QinIrTypeKind.CLASS ? 4 : 3;
    }

    private int localArrayArgumentMatchScore(
            QinIrTypeRef parameterType,
            QinIrTypeRef argumentType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        QinIrTypeRef parameterElementType = staticArrayElementType(parameterType);
        QinIrTypeRef argumentElementType = staticArrayElementType(argumentType);
        if (isObjectArrayType(parameterType)
                && "java.lang.Object[]".equals(argumentType.binaryName())
                && (argumentType.typeArguments() == null || argumentType.typeArguments().isEmpty())) {
            return 2;
        }
        if (isJavaLangObjectType(parameterElementType)) {
            return 2;
        }
        int elementScore = localIrArgumentMatchScore(parameterElementType, argumentElementType, declarationIndex);
        return elementScore < 0 ? -1 : 4 + elementScore;
    }

    private int localTypeArgumentMatchScore(
            QinIrTypeRef parameterType,
            QinIrTypeRef argumentType,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        List<QinIrTypeRef> parameterArguments = parameterType.typeArguments();
        List<QinIrTypeRef> argumentArguments = argumentType.typeArguments();
        if (parameterArguments.isEmpty()) {
            return 0;
        }
        if (argumentArguments.isEmpty() || parameterArguments.size() != argumentArguments.size()) {
            return 0;
        }
        int score = 0;
        for (int i = 0; i < parameterArguments.size(); i++) {
            int argumentScore = localIrArgumentMatchScore(
                    parameterArguments.get(i),
                    argumentArguments.get(i),
                    declarationIndex);
            if (argumentScore < 0) {
                return 0;
            }
            score += argumentScore;
        }
        return score;
    }

    private boolean isLocalClassAssignableTo(
            String argumentBinaryName,
            String parameterBinaryName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return isLocalClassAssignableTo(
                argumentBinaryName,
                parameterBinaryName,
                declarationIndex,
                new LinkedHashSet<>());
    }

    private boolean isLocalClassAssignableTo(
            String argumentBinaryName,
            String parameterBinaryName,
            Map<String, QinIrClassDeclaration> declarationIndex,
            Set<String> seen) {
        if (argumentBinaryName == null || parameterBinaryName == null || declarationIndex == null) {
            return false;
        }
        if (Objects.equals(argumentBinaryName, parameterBinaryName)
                || Objects.equals(flattenedBinaryAlias(argumentBinaryName), flattenedBinaryAlias(parameterBinaryName))) {
            return true;
        }
        if (!seen.add(argumentBinaryName)) {
            return false;
        }
        QinIrClassDeclaration argumentDeclaration = resolveIndexedDeclaration(declarationIndex, argumentBinaryName);
        if (argumentDeclaration == null) {
            return false;
        }
        QinIrTypeRef superType = argumentDeclaration.superType();
        if (superType != null
                && superType.kind() == QinIrTypeKind.CLASS
                && isLocalClassAssignableTo(superType.binaryName(), parameterBinaryName, declarationIndex, seen)) {
            return true;
        }
        for (QinIrTypeRef interfaceType : argumentDeclaration.implementsTypes()) {
            if (interfaceType != null
                    && interfaceType.kind() == QinIrTypeKind.CLASS
                    && isLocalClassAssignableTo(interfaceType.binaryName(), parameterBinaryName, declarationIndex, seen)) {
                return true;
            }
        }
        return false;
    }

    private String canonicalJavaSdkAliasBinaryName(String ownerBinaryName) {
        String canonicalHostRuntime = canonicalQinHostRuntimeBinaryName(ownerBinaryName);
        if (!Objects.equals(canonicalHostRuntime, ownerBinaryName)) {
            return canonicalHostRuntime;
        }
        String canonical = QinJavaSdkAliasSupport.canonicalBinaryName(ownerBinaryName);
        if (!Objects.equals(canonical, ownerBinaryName)) {
            return canonical;
        }
        String generatedJavaClass = generatedJavaClassBinaryNameOrNull(ownerBinaryName);
        return generatedJavaClass == null ? ownerBinaryName : generatedJavaClass;
    }

    private String generatedJavaSdkFacadeBinaryNameOrNull(String ownerBinaryName) {
        if (ownerBinaryName == null) {
            return null;
        }
        return switch (ownerBinaryName) {
            case "java.time.format.DateTimeFormatter" -> "__QinJavaTimeFormatDateTimeFormatter";
            case "java.util.stream.Stream" -> "__QinJavaUtilStream";
            case "java.util.stream.Collectors" -> "__QinJavaUtilStreamCollectors";
            default -> ownerBinaryName;
        };
    }

    private String generatedJavaClassBinaryNameOrNull(String ownerBinaryName) {
        if (ownerBinaryName == null || !ownerBinaryName.startsWith("com_")) {
            return null;
        }
        int classSeparator = generatedJavaClassSeparator(ownerBinaryName);
        if (classSeparator <= 0 || classSeparator + 1 >= ownerBinaryName.length()) {
            return null;
        }
        String candidate = ownerBinaryName.substring(0, classSeparator).replace('_', '.')
                + "."
                + ownerBinaryName.substring(classSeparator + 1);
        try {
            Class.forName(candidate);
            return candidate;
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    private int generatedJavaClassSeparator(String ownerBinaryName) {
        int separator = -1;
        for (int i = 0; i + 1 < ownerBinaryName.length(); i++) {
            if (ownerBinaryName.charAt(i) == '_'
                    && Character.isUpperCase(ownerBinaryName.charAt(i + 1))) {
                separator = i;
            }
        }
        return separator;
    }

    private boolean isReflectedExecutableCountApplicable(
            Class<?>[] parameterTypes,
            boolean varargs,
            int argumentCount) {
        if (!varargs) {
            return parameterTypes.length == argumentCount;
        }
        return argumentCount >= parameterTypes.length - 1;
    }

    private boolean isExecutableApplicable(
            Class<?>[] parameterTypes,
            boolean varargs,
            List<QinIrTypeRef> argumentTypes) {
        if (!isReflectedExecutableCountApplicable(parameterTypes, varargs, argumentTypes.size())) {
            return false;
        }
        int fixedParameterCount = varargs ? parameterTypes.length - 1 : parameterTypes.length;
        for (int i = 0; i < fixedParameterCount; i++) {
            if (!isArgumentApplicable(parameterTypes[i], argumentTypes.get(i))) {
                return false;
            }
        }
        if (!varargs) {
            return true;
        }
        Class<?> arrayType = parameterTypes[parameterTypes.length - 1];
        if (argumentTypes.size() == parameterTypes.length
                && isArgumentApplicable(arrayType, argumentTypes.get(fixedParameterCount))) {
            return true;
        }
        Class<?> componentType = arrayType.isArray() ? arrayType.getComponentType() : Object.class;
        for (int i = fixedParameterCount; i < argumentTypes.size(); i++) {
            if (!isArgumentApplicable(componentType, argumentTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    private int executableMatchScore(
            Class<?>[] parameterTypes,
            boolean varargs,
            List<QinIrTypeRef> argumentTypes) {
        int score = 0;
        int fixedParameterCount = varargs ? parameterTypes.length - 1 : parameterTypes.length;
        for (int i = 0; i < fixedParameterCount; i++) {
            score += argumentMatchScore(parameterTypes[i], argumentTypes.get(i));
        }
        if (varargs) {
            Class<?> arrayType = parameterTypes[parameterTypes.length - 1];
            if (argumentTypes.size() == parameterTypes.length
                    && isArgumentApplicable(arrayType, argumentTypes.get(fixedParameterCount))) {
                score += argumentMatchScore(arrayType, argumentTypes.get(fixedParameterCount));
            } else {
                Class<?> componentType = arrayType.isArray() ? arrayType.getComponentType() : Object.class;
                for (int i = fixedParameterCount; i < argumentTypes.size(); i++) {
                    score += argumentMatchScore(componentType, argumentTypes.get(i));
                }
            }
            score -= 1;
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
            return parameterType == boolean.class
                    ? 5
                    : parameterType == Boolean.class
                    ? 4
                    : parameterType.isAssignableFrom(Boolean.class) ? 1 : -1;
        }
        if (argumentType.kind() == QinIrTypeKind.INT) {
            return parameterType == int.class
                    ? 5
                    : parameterType == Integer.class
                    ? 4
                    : parameterType == long.class || parameterType == Long.class
                    || parameterType == short.class || parameterType == Short.class
                    || parameterType == byte.class || parameterType == Byte.class
                    || parameterType == char.class || parameterType == Character.class
                    || parameterType == double.class || parameterType == Double.class
                    || parameterType == float.class || parameterType == Float.class
                    || parameterType == Number.class
                    ? 2
                    : parameterType.isAssignableFrom(Integer.class) ? 1 : -1;
        }
        if (argumentType.kind() == QinIrTypeKind.DOUBLE) {
            return parameterType == double.class
                    ? 5
                    : parameterType == Double.class
                    ? 4
                    : parameterType == int.class
                    || parameterType == long.class
                    || parameterType == float.class
                    ? 3
                    : parameterType == Integer.class
                    || parameterType == Long.class
                    || parameterType == Float.class
                    ? 2
                    : parameterType == Number.class || parameterType.isAssignableFrom(Double.class) ? 1 : -1;
        }
        if (argumentType.kind() == QinIrTypeKind.CLASS) {
            if (isJavaFunctionalObjectType(argumentType) && isJavaFunctionalInterfaceClass(parameterType)) {
                return 2;
            }
            if (isNumericLike(argumentType)) {
                if (parameterType == int.class
                        || parameterType == long.class
                        || parameterType == float.class
                        || parameterType == double.class
                        || parameterType == short.class
                        || parameterType == byte.class
                        || parameterType == char.class) {
                    return 2;
                }
                if (Number.class.isAssignableFrom(parameterType)) {
                    return 2;
                }
            }
            if ("java.lang.Object".equals(argumentType.binaryName())) {
                return parameterType == Object.class
                        ? 4
                        : isJavaFunctionalInterfaceClass(parameterType) ? 1 : -1;
            }
            if (parameterType == Object.class) {
                return 1;
            }
            Class<?> argumentClass = resolveClass(argumentType.binaryName());
            if (parameterType.equals(argumentClass)) {
                return 4;
            }
            return parameterType.isAssignableFrom(argumentClass) ? 1 : -1;
        }
        return -1;
    }

    private void coerceValueForJavaParameterType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef actualType,
            Class<?> targetType) {
        if (targetType == null) {
            return;
        }
        if (actualType != null
                && isJavaFunctionalObjectType(actualType)
                && isJavaFunctionalInterfaceClass(targetType)) {
            code.ldc(ClassDesc.of(targetType.getName()));
            code.invokestatic(
                    ESM_GLOBAL_DESC,
                    "__qin_java_functional_interface__",
                    MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, CLASS_DESC));
            code.checkcast(ClassDesc.of(targetType.getName()));
            return;
        }
        if (targetType == long.class) {
            if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.d2l();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.INT) {
                code.i2l();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.CLASS) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "longValue", MethodTypeDesc.ofDescriptor("()J"));
                return;
            }
        }
        if (targetType == int.class) {
            if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.d2i();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.CLASS) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "intValue", MethodTypeDesc.ofDescriptor("()I"));
                return;
            }
        }
        if (targetType == double.class) {
            if (actualType.kind() == QinIrTypeKind.INT) {
                code.i2d();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.CLASS) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "doubleValue", MethodTypeDesc.ofDescriptor("()D"));
                return;
            }
        }
        if (targetType == float.class) {
            if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.d2f();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.INT) {
                code.i2f();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.CLASS) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "floatValue", MethodTypeDesc.ofDescriptor("()F"));
                return;
            }
        }
        if (targetType == short.class) {
            if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.d2i();
                code.i2s();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.INT) {
                code.i2s();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.CLASS) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "intValue", MethodTypeDesc.ofDescriptor("()I"));
                code.i2s();
                return;
            }
        }
        if (targetType == byte.class) {
            if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.d2i();
                code.i2b();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.INT) {
                code.i2b();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.CLASS) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "intValue", MethodTypeDesc.ofDescriptor("()I"));
                code.i2b();
                return;
            }
        }
        if (targetType == char.class) {
            if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.d2i();
                code.i2c();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.INT) {
                code.i2c();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.CLASS) {
                code.checkcast(NUMBER_DESC);
                code.invokevirtual(NUMBER_DESC, "intValue", MethodTypeDesc.ofDescriptor("()I"));
                code.i2c();
                return;
            }
        }
        coerceValueForTargetType(code, actualType, toQinTypeRef(targetType));
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
        if (type == long.class || type == Long.class) {
            return QinIrTypeRef.classType("java.lang.Long");
        }
        if (type == float.class || type == Float.class) {
            return QinIrTypeRef.classType("java.lang.Float");
        }
        if (type == short.class || type == Short.class) {
            return QinIrTypeRef.classType("java.lang.Short");
        }
        if (type == byte.class || type == Byte.class) {
            return QinIrTypeRef.classType("java.lang.Byte");
        }
        if (type == char.class || type == Character.class) {
            return QinIrTypeRef.classType("java.lang.Character");
        }
        if (type == String.class) {
            return QinIrTypeRef.stringType();
        }
        if (type == Object[].class) {
            return QinIrTypeRef.classType("java.lang.Object[]");
        }
        return QinIrTypeRef.classType(type.getName());
    }

    private QinIrTypeRef toQinReflectedFieldTypeRef(Class<?> type) {
        if (type != null && !type.isPrimitive()) {
            if (type == String.class) {
                return QinIrTypeRef.stringType();
            }
            if (type == Object[].class) {
                return QinIrTypeRef.classType("java.lang.Object[]");
            }
            return QinIrTypeRef.classType(type.getName());
        }
        return toQinTypeRef(type);
    }

    private Class<?> resolveClass(String binaryName) {
        String resolvedBinaryName = canonicalJavaSdkAliasBinaryName(binaryName);
        try {
            if (resolvedBinaryName != null && resolvedBinaryName.endsWith("[]")) {
                return Class.forName(arrayDescriptor(resolvedBinaryName, '.'));
            }
            return Class.forName(resolvedBinaryName);
        } catch (ClassNotFoundException error) {
            throw new IllegalArgumentException("Cannot resolve JVM class: " + binaryName, error);
        }
    }

    private ResolvedFieldAccess resolveFieldAccess(
            QinIrClassDeclaration declaration,
            String fieldName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        return resolveFieldAccess(declaration, fieldName, declarationIndex, new java.util.LinkedHashSet<>());
    }

    private QinIrClassDeclaration resolveDeclaredClassReference(
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            String name) {
        if (name == null || name.isBlank() || declarationIndex == null) {
            return null;
        }
        QinIrClassDeclaration declaration = declarationIndex.get(name);
        if (matchesDeclaredClassReferenceName(declaration, name)) {
            return declaration;
        }
        if (matchesDeclaredClassReferenceName(ownerDeclaration, name)) {
            return ownerDeclaration;
        }
        for (QinIrClassDeclaration candidate : declarationIndex.values()) {
            if (candidate != declaration && matchesDeclaredClassReferenceName(candidate, name)) {
                return candidate;
            }
        }
        return null;
    }

    private boolean matchesDeclaredClassReferenceName(QinIrClassDeclaration declaration, String name) {
        if (declaration == null || name == null || name.isBlank()) {
            return false;
        }
        if (declaration.simpleName() != null
                && (declaration.simpleName().equals(name) || declaration.simpleName().endsWith(name))) {
            return true;
        }
        if (declaration.binaryName() != null) {
            if (declaration.binaryName().equals(name)
                    || declaration.binaryName().endsWith("." + name)
                    || declaration.binaryName().endsWith("$" + name)) {
                return true;
            }
            String flattenedBinaryName = declaration.binaryName().replace('.', '_').replace('$', '_');
            if (flattenedBinaryName.equals(name) || flattenedBinaryName.endsWith("_" + name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isJavaEsmSymbolIteratorAccess(QinIrExpression receiverExpression, String propertyName) {
        return "iterator".equals(propertyName)
                && receiverExpression instanceof QinIrIdentifierReference identifierReference
                && "Symbol".equals(identifierReference.name());
    }

    private ResolvedFieldAccess resolveJavaStaticFieldAccess(
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrExpression receiverExpression,
            String fieldName) {
        String ownerBinaryName = javaStaticFieldOwnerBinaryNameOrNull(
                ownerDeclaration,
                declarationIndex,
                receiverExpression);
        return resolveJavaStaticFieldAccess(ownerBinaryName, fieldName);
    }

    private String javaStaticFieldOwnerBinaryNameOrNull(
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            QinIrExpression receiverExpression) {
        if (receiverExpression instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            return classLiteralExpression.binaryName();
        }
        if (!(receiverExpression instanceof QinIrIdentifierReference)) {
            String resolvedTarget = resolveStaticInstanceofTarget(
                    receiverExpression,
                    ownerDeclaration,
                    declarationIndex);
            return resolvedTarget == null ? null : canonicalJavaSdkAliasBinaryName(resolvedTarget);
        }
        if (!(receiverExpression instanceof QinIrIdentifierReference identifierReference)) {
            return null;
        }
        String canonicalAlias = QinJavaSdkAliasSupport.canonicalBinaryName(identifierReference.name());
        if (!Objects.equals(canonicalAlias, identifierReference.name())) {
            return canonicalAlias;
        }
        ResolvedFieldAccess fieldAccess = resolveFieldAccess(
                ownerDeclaration,
                identifierReference.name(),
                declarationIndex);
        if (fieldAccess != null && fieldAccess.field().initializer() instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            return classLiteralExpression.binaryName();
        }
        ResolvedFieldAccess enclosingStaticFieldAccess = resolveEnclosingStaticFieldAccess(
                ownerDeclaration,
                identifierReference.name(),
                declarationIndex);
        if (enclosingStaticFieldAccess != null
                && enclosingStaticFieldAccess.field().initializer() instanceof QinIrJavaClassLiteralExpression classLiteralExpression) {
            return classLiteralExpression.binaryName();
        }
        return null;
    }

    private ResolvedFieldAccess resolveJavaStaticFieldAccess(String ownerBinaryName, String fieldName) {
        if (ownerBinaryName == null || ownerBinaryName.isBlank()
                || fieldName == null || fieldName.isBlank()) {
            return null;
        }
        try {
            Class<?> ownerClass = resolveClass(ownerBinaryName);
            java.lang.reflect.Field field = ownerClass.getField(fieldName);
            if (!Modifier.isStatic(field.getModifiers())) {
                return null;
            }
            return new ResolvedFieldAccess(
                    field.getDeclaringClass().getName(),
                    new QinIrFieldDeclaration(
                            field.getName(),
                            toQinReflectedFieldTypeRef(field.getType()),
                            List.of(),
                            null,
                            true),
                    toClassDesc(field.getType()));
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException ignored) {
            return null;
        }
    }

    private ResolvedFieldAccess resolveDeclaredStaticFieldAccess(
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            String ownerName,
            String fieldName) {
        QinIrClassDeclaration declaredClassReference = resolveDeclaredClassReference(
                ownerDeclaration,
                declarationIndex,
                ownerName);
        if (declaredClassReference == null) {
            return null;
        }
        ResolvedFieldAccess fieldAccess = resolveFieldAccess(
                declaredClassReference,
                fieldName,
                declarationIndex);
        if (fieldAccess == null || !fieldAccess.field().staticField()) {
            return null;
        }
        return fieldAccess;
    }

    private ResolvedStaticMethodCall resolveDeclaredStaticGetterAccess(
            QinIrClassDeclaration ownerDeclaration,
            Map<String, QinIrClassDeclaration> declarationIndex,
            String ownerName,
            String propertyName) {
        QinIrClassDeclaration declaredClassReference = resolveDeclaredClassReference(
                ownerDeclaration,
                declarationIndex,
                ownerName);
        if (declaredClassReference == null) {
            return null;
        }
        ResolvedStaticMethodCall resolved = resolveStaticMethodCall(
                declaredClassReference.binaryName(),
                propertyName,
                List.of(),
                declarationIndex);
        if (resolved == null) {
            return null;
        }
        QinIrMethodDeclaration method = findLocalStaticNoArgMethod(
                resolveIndexedDeclaration(declarationIndex, resolved.ownerBinaryName()),
                resolved.methodName());
        return method == null || method.abstractMethod() ? null : resolved;
    }

    private QinIrMethodDeclaration findLocalStaticNoArgMethod(
            QinIrClassDeclaration declaration,
            String methodName) {
        if (declaration == null || methodName == null) {
            return null;
        }
        for (QinIrMethodDeclaration method : declaration.methods()) {
            if (method.staticMethod()
                    && method.name().equals(methodName)
                    && method.parameters().isEmpty()) {
                return method;
            }
        }
        return null;
    }

    private ResolvedFieldAccess resolveEnclosingStaticFieldAccess(
            QinIrClassDeclaration declaration,
            String fieldName,
            Map<String, QinIrClassDeclaration> declarationIndex) {
        if (declaration == null || declaration.binaryName() == null) {
            return null;
        }
        String binaryName = declaration.binaryName();
        int dollar = binaryName.lastIndexOf('$');
        while (dollar > 0) {
            String enclosingName = binaryName.substring(0, dollar);
            QinIrClassDeclaration enclosingDeclaration = declarationIndex.get(enclosingName);
            if (enclosingDeclaration != null) {
                for (QinIrFieldDeclaration field : enclosingDeclaration.fields()) {
                    if (field.staticField() && fieldNameMatches(field.name(), fieldName)) {
                        return new ResolvedFieldAccess(enclosingDeclaration.binaryName(), field);
                    }
                }
            }
            dollar = enclosingName.lastIndexOf('$');
        }
        return null;
    }

    private int parameterSlotStart(QinIrMethodDeclaration method) {
        return method != null && method.staticMethod() ? 0 : 1;
    }

    private void emitResolvedFieldGet(java.lang.classfile.CodeBuilder code, ResolvedFieldAccess fieldAccess) {
        ClassDesc fieldDescriptor = fieldAccess.fieldDescriptor() == null
                ? toClassDesc(fieldAccess.field().type())
                : fieldAccess.fieldDescriptor();
        if (fieldAccess.field().staticField()) {
            code.getstatic(
                    ClassDesc.of(fieldAccess.ownerBinaryName()),
                    fieldAccess.field().name(),
                    fieldDescriptor);
            boxReflectedPrimitiveReturnIfNeeded(
                    code,
                    fieldAccess.field().type(),
                    MethodTypeDesc.of(fieldDescriptor));
            return;
        }
        code.aload(0);
        code.getfield(
                ClassDesc.of(fieldAccess.ownerBinaryName()),
                fieldAccess.field().name(),
                fieldDescriptor);
        boxReflectedPrimitiveReturnIfNeeded(
                code,
                fieldAccess.field().type(),
                MethodTypeDesc.of(fieldDescriptor));
    }

    private void emitCurrentMethodArgumentsArray(
            java.lang.classfile.CodeBuilder code,
            QinIrMethodDeclaration method) {
        List<QinIrParameter> parameters = method == null ? List.of() : method.parameters();
        code.loadConstant(parameters.size());
        code.anewarray(OBJECT_DESC);
        int localSlot = method != null && method.staticMethod() ? 0 : 1;
        for (int i = 0; i < parameters.size(); i++) {
            QinIrParameter parameter = parameters.get(i);
            code.dup();
            code.loadConstant(i);
            loadLocalForType(code, parameter.type(), localSlot, parameter.name());
            boxValueForObjectTarget(code, parameter.type());
            code.aastore();
            localSlot += localSlotWidth(parameter.type());
        }
    }

    private boolean isJavaFunctionalInterfaceClass(Class<?> targetClass) {
        if (targetClass == null || !targetClass.isInterface()) {
            return false;
        }
        int abstractMethodCount = 0;
        for (Method candidate : targetClass.getMethods()) {
            if (candidate.getDeclaringClass() == Object.class || isObjectMethodSignature(candidate)) {
                continue;
            }
            int modifiers = candidate.getModifiers();
            if (!Modifier.isAbstract(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            abstractMethodCount++;
            if (abstractMethodCount > 1) {
                return false;
            }
        }
        return abstractMethodCount == 1;
    }

    private boolean isObjectMethodSignature(Method candidate) {
        try {
            Object.class.getMethod(candidate.getName(), candidate.getParameterTypes());
            return true;
        } catch (NoSuchMethodException ignored) {
            return false;
        }
    }

    private ResolvedFieldAccess resolveFieldAccess(
            QinIrClassDeclaration declaration,
            String fieldName,
            Map<String, QinIrClassDeclaration> declarationIndex,
            java.util.Set<String> visitedLocalTypes) {
        if (declaration == null || !visitedLocalTypes.add(declaration.binaryName())) {
            return null;
        }
        for (QinIrFieldDeclaration field : declaration.fields()) {
            if (fieldNameEquals(field.name(), fieldName)) {
                return new ResolvedFieldAccess(declaration.binaryName(), field);
            }
        }
        for (QinIrFieldDeclaration field : declaration.fields()) {
            if (fieldNameAliasMatches(field.name(), fieldName)) {
                return new ResolvedFieldAccess(declaration.binaryName(), field);
            }
        }
        if (declaration.superType() == null || declaration.superType().kind() != QinIrTypeKind.CLASS) {
            return null;
        }
        QinIrClassDeclaration superDeclaration = declarationIndex.get(declaration.superType().binaryName());
        return superDeclaration == null
                ? null
                : resolveFieldAccess(superDeclaration, fieldName, declarationIndex, visitedLocalTypes);
    }

    private boolean fieldNameMatches(String actualFieldName, String requestedFieldName) {
        return fieldNameEquals(actualFieldName, requestedFieldName)
                || fieldNameAliasMatches(actualFieldName, requestedFieldName);
    }

    private boolean fieldNameEquals(String actualFieldName, String requestedFieldName) {
        return actualFieldName != null
                && requestedFieldName != null
                && actualFieldName.equals(requestedFieldName);
    }

    private boolean fieldNameAliasMatches(String actualFieldName, String requestedFieldName) {
        if (actualFieldName == null || requestedFieldName == null) {
            return false;
        }
        return actualFieldName.equals("__qin_field_" + requestedFieldName)
                || requestedFieldName.equals("__qin_field_" + actualFieldName);
    }

    private void emitAllArgsConstructorBody(
            java.lang.classfile.CodeBuilder code,
            QinIrClassDeclaration declaration,
            String ownerBinaryName) {
        code.aload(0);
        code.invokespecial(resolveSuperclass(declaration.superType()), "<init>", VOID_INIT);

        int localIndex = 1;
        for (QinIrFieldDeclaration field : instanceFields(declaration.fields())) {
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

    private record FunctionDefinitionHelper(
            String name,
            QinIrObjectLiteral definition) {
    }

    private static final class LocalFrame {
        private final Map<String, LocalBinding> bindings = new LinkedHashMap<>();
        private final Set<String> declaredNames = new LinkedHashSet<>();
        private final LoopBinding loop;
        private final java.lang.classfile.Label breakLabel;
        private final List<List<QinIrStatement>> finallyBlocks;
        private int nextSlot;

        private LocalFrame(
                int nextSlot,
                LoopBinding loop,
                java.lang.classfile.Label breakLabel,
                List<List<QinIrStatement>> finallyBlocks) {
            this.nextSlot = nextSlot;
            this.loop = loop;
            this.breakLabel = breakLabel;
            this.finallyBlocks = List.copyOf(finallyBlocks);
        }

        static LocalFrame forMethodParameters(QinIrMethodDeclaration method) {
            int localSlot = method != null && method.staticMethod() ? 0 : 1;
            if (method == null) {
                return new LocalFrame(localSlot, null, null, List.of());
            }
            for (var parameter : method.parameters()) {
                localSlot += parameter.type().kind() == QinIrTypeKind.DOUBLE ? 2 : 1;
            }
            return new LocalFrame(localSlot, null, null, List.of());
        }

        LocalFrame child() {
            LocalFrame child = new LocalFrame(nextSlot, loop, breakLabel, finallyBlocks);
            child.bindings.putAll(bindings);
            return child;
        }

        LocalFrame withLoop(LoopBinding loopBinding) {
            LocalFrame child = new LocalFrame(nextSlot, loopBinding, loopBinding.breakLabel(), finallyBlocks);
            child.bindings.putAll(bindings);
            return child;
        }

        LocalFrame withSwitchBreak(java.lang.classfile.Label breakLabel) {
            LocalFrame child = new LocalFrame(nextSlot, loop, breakLabel, finallyBlocks);
            child.bindings.putAll(bindings);
            return child;
        }

        LocalFrame withFinally(List<QinIrStatement> finallyBody) {
            List<List<QinIrStatement>> nextFinallyBlocks = new ArrayList<>(finallyBlocks);
            nextFinallyBlocks.add(List.copyOf(finallyBody));
            LocalFrame child = new LocalFrame(nextSlot, loop, breakLabel, nextFinallyBlocks);
            child.bindings.putAll(bindings);
            return child;
        }

        LocalBinding declare(String name, QinIrTypeRef type) {
            if (declaredNames.contains(name)) {
                throw new IllegalArgumentException("Duplicate declaration local: " + name);
            }
            if (type.kind() == QinIrTypeKind.VOID) {
                throw new IllegalArgumentException("Declaration local cannot be void: " + name);
            }
            LocalBinding binding = new LocalBinding(name, type, nextSlot);
            bindings.put(name, binding);
            declaredNames.add(name);
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

        List<List<QinIrStatement>> finallyBlocks() {
            return finallyBlocks;
        }
    }

    private record ResolvedPropertyAccess(
            String ownerBinaryName,
            String accessorName,
            QinIrTypeRef propertyType,
            boolean ownerInterface) {
    }

    private record ResolvedMutablePropertyAccess(
            QinIrTypeRef receiverType,
            String ownerBinaryName,
            String propertyName,
            String getterName,
            String setterName,
            QinIrTypeRef propertyType,
            boolean ownerInterface) {
    }

    private record ResolvedFieldAccess(
            String ownerBinaryName,
            QinIrFieldDeclaration field,
            ClassDesc fieldDescriptor) {
        ResolvedFieldAccess(String ownerBinaryName, QinIrFieldDeclaration field) {
            this(ownerBinaryName, field, null);
        }
    }

    private record ResolvedInstanceMethodCall(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> parameterTypes,
            QinIrTypeRef returnType,
            boolean ownerInterface,
            MethodTypeDesc descriptor,
            List<Class<?>> reflectedParameterTypes,
            boolean varargs) {
    }

    private record ResolvedStaticMethodCall(
            String ownerBinaryName,
            String methodName,
            List<QinIrTypeRef> parameterTypes,
            QinIrTypeRef returnType,
            boolean ownerInterface,
            MethodTypeDesc descriptor,
            List<Class<?>> reflectedParameterTypes,
            boolean varargs) {
    }

    private record ResolvedConstructorCall(
            String ownerBinaryName,
            List<QinIrTypeRef> parameterTypes,
            MethodTypeDesc descriptor,
            boolean varargs,
            List<Class<?>> reflectedParameterTypes) {
    }

    private void invokeAccessor(java.lang.classfile.CodeBuilder code, ResolvedPropertyAccess propertyAccess) {
        if (propertyAccess.ownerBinaryName() == null && propertyAccess.accessorName() == null) {
            code.arraylength();
            return;
        }
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

    private void invokeMutablePropertySetter(
            java.lang.classfile.CodeBuilder code,
            ResolvedMutablePropertyAccess propertyAccess) {
        MethodTypeDesc descriptor = MethodTypeDesc.ofDescriptor(
                "(" + toClassDesc(propertyAccess.propertyType()).descriptorString() + ")V");
        if (propertyAccess.ownerInterface()) {
            code.invokeinterface(
                    ClassDesc.of(propertyAccess.ownerBinaryName()),
                    propertyAccess.setterName(),
                    descriptor);
            return;
        }
        code.invokevirtual(
                ClassDesc.of(propertyAccess.ownerBinaryName()),
                propertyAccess.setterName(),
                descriptor);
    }

    private void invokeMethod(java.lang.classfile.CodeBuilder code, ResolvedInstanceMethodCall methodCall) {
        MethodTypeDesc descriptor = methodDescriptor(methodCall);
        if (methodCall.ownerInterface()) {
            code.invokeinterface(ClassDesc.of(methodCall.ownerBinaryName()), methodCall.methodName(), descriptor);
            boxReflectedPrimitiveReturnIfNeeded(code, methodCall.returnType(), descriptor);
            return;
        }
        code.invokevirtual(ClassDesc.of(methodCall.ownerBinaryName()), methodCall.methodName(), descriptor);
        boxReflectedPrimitiveReturnIfNeeded(code, methodCall.returnType(), descriptor);
    }

    private void invokeStaticMethod(java.lang.classfile.CodeBuilder code, ResolvedStaticMethodCall methodCall) {
        MethodTypeDesc descriptor = methodDescriptor(methodCall);
        boolean ownerInterface = methodCall.ownerInterface() || isInterfaceBinaryName(methodCall.ownerBinaryName());
        code.invokestatic(
                ClassDesc.of(methodCall.ownerBinaryName()),
                methodCall.methodName(),
                descriptor,
                ownerInterface);
        boxReflectedPrimitiveReturnIfNeeded(code, methodCall.returnType(), descriptor);
    }

    private boolean isInterfaceBinaryName(String ownerBinaryName) {
        if (ownerBinaryName == null || ownerBinaryName.isBlank()) {
            return false;
        }
        try {
            return Class.forName(canonicalJavaSdkAliasBinaryName(ownerBinaryName)).isInterface();
        } catch (ClassNotFoundException | LinkageError ignored) {
            return false;
        }
    }

    private MethodTypeDesc methodDescriptor(ResolvedInstanceMethodCall methodCall) {
        if (methodCall.descriptor() != null) {
            return methodCall.descriptor();
        }
        return MethodTypeDesc.of(
                toClassDesc(methodCall.returnType()),
                methodCall.parameterTypes().stream().map(this::toClassDesc).toList());
    }

    private void boxReflectedPrimitiveReturnIfNeeded(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef returnType,
            MethodTypeDesc descriptor) {
        if (returnType == null
                || (returnType.kind() != QinIrTypeKind.CLASS && returnType.kind() != QinIrTypeKind.STRING)
                || descriptor == null
                || descriptor.returnType() == null) {
            return;
        }
        switch (descriptor.returnType().descriptorString()) {
            case "J" -> code.invokestatic(
                    ClassDesc.of("java.lang.Long"),
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(J)Ljava/lang/Long;"));
            case "F" -> code.invokestatic(
                    ClassDesc.of("java.lang.Float"),
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(F)Ljava/lang/Float;"));
            case "S" -> code.invokestatic(
                    ClassDesc.of("java.lang.Short"),
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(S)Ljava/lang/Short;"));
            case "B" -> code.invokestatic(
                    ClassDesc.of("java.lang.Byte"),
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(B)Ljava/lang/Byte;"));
            case "C" -> code.invokestatic(
                    ClassDesc.of("java.lang.Character"),
                    "valueOf",
                    MethodTypeDesc.ofDescriptor("(C)Ljava/lang/Character;"));
            default -> {
                String targetBinaryName = returnType.kind() == QinIrTypeKind.STRING
                        ? "java.lang.String"
                        : returnType.binaryName();
                String descriptorString = descriptor.returnType().descriptorString();
                if (targetBinaryName != null
                        && !"java.lang.Object".equals(targetBinaryName)
                        && (descriptorString.startsWith("L") || descriptorString.startsWith("["))
                        && !descriptorString.equals(toClassDesc(returnType).descriptorString())) {
                    code.checkcast(toReferenceClassDesc(targetBinaryName));
                }
            }
        }
    }

    private MethodTypeDesc methodDescriptor(ResolvedStaticMethodCall methodCall) {
        if (methodCall.descriptor() != null) {
            return methodCall.descriptor();
        }
        return MethodTypeDesc.of(
                toClassDesc(methodCall.returnType()),
                methodCall.parameterTypes().stream().map(this::toClassDesc).toList());
    }

    private void coerceValueForTargetType(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef actualType,
            QinIrTypeRef targetType) {
        if (targetType.kind() == QinIrTypeKind.STRING) {
            if (actualType.kind() == QinIrTypeKind.BOOLEAN) {
                code.invokestatic(
                        STRING_DESC,
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/String;"));
            } else if (actualType.kind() == QinIrTypeKind.INT) {
                code.invokestatic(
                        STRING_DESC,
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(I)Ljava/lang/String;"));
            } else if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                code.invokestatic(
                        STRING_DESC,
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(D)Ljava/lang/String;"));
            } else if (actualType.kind() == QinIrTypeKind.CLASS || actualType.kind() == QinIrTypeKind.STRING) {
                String actualBinaryName = actualType.kind() == QinIrTypeKind.STRING
                        ? "java.lang.String"
                        : actualType.binaryName();
                if (!"java.lang.String".equals(actualBinaryName)) {
                    code.checkcast(ClassDesc.of("java.lang.String"));
                }
            }
            return;
        }

        if (targetType.kind() == QinIrTypeKind.CLASS) {
            String targetBinaryName = targetType.binaryName();
            if (actualType.kind() == QinIrTypeKind.BOOLEAN) {
                code.invokestatic(
                        ClassDesc.of("java.lang.Boolean"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(Z)Ljava/lang/Boolean;"));
            } else if (actualType.kind() == QinIrTypeKind.INT) {
                boxPrimitiveNumericValueForTarget(code, actualType, targetBinaryName);
            } else if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                boxPrimitiveNumericValueForTarget(code, actualType, targetBinaryName);
            } else if (actualType.kind() == QinIrTypeKind.CLASS || actualType.kind() == QinIrTypeKind.STRING) {
                String actualBinaryName = actualType.kind() == QinIrTypeKind.STRING
                        ? "java.lang.String"
                        : actualType.binaryName();
                if (isJavaFunctionalObjectType(actualType) && isJavaFunctionalInterfaceType(targetType)) {
                    code.ldc(toReferenceClassDesc(targetBinaryName));
                    code.invokestatic(
                            ESM_GLOBAL_DESC,
                            "__qin_java_functional_interface__",
                            MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, CLASS_DESC));
                    code.checkcast(toReferenceClassDesc(targetBinaryName));
                    return;
                }
                if (isSlimeParserParamsBinaryName(targetBinaryName)
                        && targetBinaryName != null
                        && !targetBinaryName.equals(actualBinaryName)) {
                    code.ldc(toReferenceClassDesc(targetBinaryName));
                    code.invokestatic(
                            ESM_GLOBAL_DESC,
                            "__qin_coerce_slime_parser_params__",
                            MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, CLASS_DESC));
                    code.checkcast(toReferenceClassDesc(targetBinaryName));
                    return;
                }
                if (isJavaLangObjectArrayBinaryName(targetBinaryName)
                        && !isJavaLangObjectArrayBinaryName(actualBinaryName)) {
                    code.invokestatic(
                            ESM_GLOBAL_DESC,
                            "__qin_to_object_array__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)[Ljava/lang/Object;"));
                    return;
                }
                if (isBoxedNumericBinaryName(actualBinaryName) && isBoxedNumericBinaryName(targetBinaryName)
                        && !targetBinaryName.equals(actualBinaryName)) {
                    coerceBoxedNumericValueForTarget(code, targetBinaryName);
                    return;
                }
                if ("java.lang.Object".equals(actualBinaryName) && isBoxedNumericBinaryName(targetBinaryName)) {
                    coerceBoxedNumericValueForTarget(code, targetBinaryName);
                    return;
                }
                if (requiresRuntimeJavaValueCoercion(targetBinaryName)) {
                    emitRuntimeJavaValueCoercion(code, targetBinaryName);
                    code.checkcast(toReferenceClassDesc(targetBinaryName));
                    return;
                }
                if (targetBinaryName != null
                        && actualBinaryName != null
                        && !targetBinaryName.equals(actualBinaryName)
                        && !"java.lang.Object".equals(targetBinaryName)) {
                    code.checkcast(toReferenceClassDesc(targetBinaryName));
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
            if (actualType.kind() == QinIrTypeKind.CLASS && targetType.kind() == QinIrTypeKind.BOOLEAN) {
                code.checkcast(BOOLEAN_DESC);
                code.invokevirtual(BOOLEAN_DESC, "booleanValue", MethodTypeDesc.ofDescriptor("()Z"));
                return;
            }
            if (actualType.kind() == QinIrTypeKind.DOUBLE && targetType.kind() == QinIrTypeKind.INT) {
                code.d2i();
                return;
            }
            if (actualType.kind() == QinIrTypeKind.INT && targetType.kind() == QinIrTypeKind.DOUBLE) {
                code.i2d();
                return;
            }
            if ((actualType.kind() == QinIrTypeKind.DOUBLE || actualType.kind() == QinIrTypeKind.INT)
                    && targetType.kind() == QinIrTypeKind.BOOLEAN) {
                boxValueForObjectTarget(code, actualType);
                QinJvmDynamicSemanticWarnings.warnJavaEsmGlobalCall("QinJvmDeclarationClassEmitter", "__qin_truthy__");
                code.invokestatic(
                        ESM_GLOBAL_DESC,
                        "__qin_truthy__",
                        MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)Z"));
                return;
            }
            throw new IllegalArgumentException(
                    "Unsupported declaration argument coercion: " + actualType.kind() + " -> " + targetType.kind());
        }
    }

    private boolean isSlimeParserParamsBinaryName(String binaryName) {
        return binaryName != null
                && (binaryName.endsWith("$ExpressionParams")
                || binaryName.endsWith("$StatementParams")
                || binaryName.endsWith("$DeclarationParams")
                || binaryName.endsWith("$TemplateLiteralParams"));
    }

    private boolean isSubhutiCstType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && ("com.subhuti.struct.SubhutiCst".equals(type.binaryName())
                        || "com_subhuti_struct_SubhutiCst".equals(type.binaryName()));
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
                if (isJavaLangObjectArrayBinaryName(resultType.binaryName())) {
                    code.invokestatic(
                            ESM_GLOBAL_DESC,
                            "__qin_to_object_array__",
                            MethodTypeDesc.ofDescriptor("(Ljava/lang/Object;)[Ljava/lang/Object;"));
                    return;
                }
                if (isBoxedNumericBinaryName(resultType.binaryName())) {
                    coerceBoxedNumericValueForTarget(code, resultType.binaryName());
                    return;
                }
                if (!"java.lang.Object".equals(resultType.binaryName())) {
                    if (requiresRuntimeJavaValueCoercion(resultType.binaryName())) {
                        emitRuntimeJavaValueCoercion(code, resultType.binaryName());
                    }
                    code.checkcast(toReferenceClassDesc(resultType.binaryName()));
                }
            }
            case VOID -> {
            }
        }
    }

    private boolean isJavaLangObjectArrayBinaryName(String binaryName) {
        return "java.lang.Object[]".equals(binaryName)
                || "[Ljava.lang.Object;".equals(binaryName)
                || "[Ljava/lang/Object;".equals(binaryName);
    }

    private boolean isBoxedNumericBinaryName(String binaryName) {
        return "java.lang.Number".equals(binaryName)
                || "java.lang.Double".equals(binaryName)
                || "java.lang.Float".equals(binaryName)
                || "java.lang.Long".equals(binaryName)
                || "java.lang.Integer".equals(binaryName)
                || "java.lang.Short".equals(binaryName)
                || "java.lang.Byte".equals(binaryName);
    }

    private void boxPrimitiveNumericValueForTarget(
            java.lang.classfile.CodeBuilder code,
            QinIrTypeRef actualType,
            String targetBinaryName) {
        if (!isBoxedNumericBinaryName(targetBinaryName)) {
            boxValueForObjectTarget(code, actualType);
            return;
        }
        switch (targetBinaryName) {
            case "java.lang.Number" -> boxValueForObjectTarget(code, actualType);
            case "java.lang.Double" -> {
                if (actualType.kind() == QinIrTypeKind.INT) {
                    code.i2d();
                }
                code.invokestatic(
                        ClassDesc.of("java.lang.Double"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;"));
            }
            case "java.lang.Float" -> {
                if (actualType.kind() == QinIrTypeKind.INT) {
                    code.i2f();
                } else {
                    code.d2f();
                }
                code.invokestatic(
                        ClassDesc.of("java.lang.Float"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(F)Ljava/lang/Float;"));
            }
            case "java.lang.Long" -> {
                if (actualType.kind() == QinIrTypeKind.INT) {
                    code.i2l();
                } else {
                    code.d2l();
                }
                code.invokestatic(
                        ClassDesc.of("java.lang.Long"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(J)Ljava/lang/Long;"));
            }
            case "java.lang.Integer" -> {
                if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                    code.d2i();
                }
                code.invokestatic(
                        ClassDesc.of("java.lang.Integer"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;"));
            }
            case "java.lang.Short" -> {
                if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                    code.d2i();
                }
                code.i2s();
                code.invokestatic(
                        ClassDesc.of("java.lang.Short"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(S)Ljava/lang/Short;"));
            }
            case "java.lang.Byte" -> {
                if (actualType.kind() == QinIrTypeKind.DOUBLE) {
                    code.d2i();
                }
                code.i2b();
                code.invokestatic(
                        ClassDesc.of("java.lang.Byte"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(B)Ljava/lang/Byte;"));
            }
            default -> throw new IllegalArgumentException("Unsupported boxed numeric target: " + targetBinaryName);
        }
    }

    private void coerceBoxedNumericValueForTarget(java.lang.classfile.CodeBuilder code, String targetBinaryName) {
        code.checkcast(NUMBER_DESC);
        switch (targetBinaryName) {
            case "java.lang.Number" -> {
            }
            case "java.lang.Double" -> {
                code.invokevirtual(NUMBER_DESC, "doubleValue", MethodTypeDesc.ofDescriptor("()D"));
                code.invokestatic(
                        ClassDesc.of("java.lang.Double"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(D)Ljava/lang/Double;"));
            }
            case "java.lang.Float" -> {
                code.invokevirtual(NUMBER_DESC, "floatValue", MethodTypeDesc.ofDescriptor("()F"));
                code.invokestatic(
                        ClassDesc.of("java.lang.Float"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(F)Ljava/lang/Float;"));
            }
            case "java.lang.Long" -> {
                code.invokevirtual(NUMBER_DESC, "longValue", MethodTypeDesc.ofDescriptor("()J"));
                code.invokestatic(
                        ClassDesc.of("java.lang.Long"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(J)Ljava/lang/Long;"));
            }
            case "java.lang.Integer" -> {
                code.invokevirtual(NUMBER_DESC, "intValue", MethodTypeDesc.ofDescriptor("()I"));
                code.invokestatic(
                        ClassDesc.of("java.lang.Integer"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(I)Ljava/lang/Integer;"));
            }
            case "java.lang.Short" -> {
                code.invokevirtual(NUMBER_DESC, "shortValue", MethodTypeDesc.ofDescriptor("()S"));
                code.invokestatic(
                        ClassDesc.of("java.lang.Short"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(S)Ljava/lang/Short;"));
            }
            case "java.lang.Byte" -> {
                code.invokevirtual(NUMBER_DESC, "byteValue", MethodTypeDesc.ofDescriptor("()B"));
                code.invokestatic(
                        ClassDesc.of("java.lang.Byte"),
                        "valueOf",
                        MethodTypeDesc.ofDescriptor("(B)Ljava/lang/Byte;"));
            }
            default -> throw new IllegalArgumentException("Unsupported boxed numeric target: " + targetBinaryName);
        }
    }

    private boolean requiresRuntimeJavaValueCoercion(String binaryName) {
        return "com.subhuti.struct.SubhutiMatchToken".equals(binaryName);
    }

    private boolean isSubhutiMatchTokenBinaryName(String binaryName) {
        return "com.subhuti.struct.SubhutiMatchToken".equals(binaryName)
                || "com_subhuti_struct_SubhutiMatchToken".equals(binaryName)
                || "SubhutiMatchToken".equals(binaryName);
    }

    private void emitRuntimeJavaValueCoercion(java.lang.classfile.CodeBuilder code, String targetBinaryName) {
        code.ldc(toReferenceClassDesc(targetBinaryName));
        code.invokestatic(
                ESM_GLOBAL_DESC,
                "__qin_coerce_java_value__",
                MethodTypeDesc.of(OBJECT_DESC, OBJECT_DESC, CLASS_DESC));
    }

    private boolean isNumericLike(QinIrTypeRef type) {
        return type != null
                && (type.kind() == QinIrTypeKind.INT
                || type.kind() == QinIrTypeKind.DOUBLE
                || (type.kind() == QinIrTypeKind.CLASS
                && ("java.lang.Integer".equals(type.binaryName())
                || "java.lang.Long".equals(type.binaryName())
                || "java.lang.Double".equals(type.binaryName())
                || "java.lang.Number".equals(type.binaryName()))));
    }

    private boolean isBooleanLike(QinIrTypeRef type) {
        return type != null
                && (type.kind() == QinIrTypeKind.BOOLEAN || isBoxedBooleanType(type));
    }

    private boolean isBoxedBooleanType(QinIrTypeRef type) {
        return type != null
                && type.kind() == QinIrTypeKind.CLASS
                && "java.lang.Boolean".equals(type.binaryName());
    }

    private boolean isStringLike(QinIrTypeRef type) {
        return type.kind() == QinIrTypeKind.STRING
                || (type.kind() == QinIrTypeKind.CLASS && "java.lang.String".equals(type.binaryName()));
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
