package com.qin.runtime.core;

import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassFile;
import com.qin.lang.pipeline.cfa.QinCfaPipeline;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;
import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.ir.QinIrArrayLiteral;
import com.qin.lang.ir.QinIrBooleanLiteral;
import com.qin.lang.ir.QinIrExpression;
import com.qin.lang.ir.QinIrFunctionLiteral;
import com.qin.lang.ir.QinIrFunctionModelArtifact;
import com.qin.lang.ir.QinIrNullLiteral;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrStringLiteral;
import com.qin.lang.runtime.JavaEsmGlobal;
import com.qin.lang.runtime.QinFunctionModelRegistry;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Compiles Qin source to JVM bytecode and executes the generated run() method in-memory.
 */
public final class QinInMemoryJvmRunner {
    private static final long DEFAULT_JS_RUN_STACK_BYTES = 32L * 1024L * 1024L;

    private final QinCfaPipeline cfaPipeline;
    private final QinCompileSnapshotWriter snapshotWriter;

    public QinInMemoryJvmRunner() {
        this(new QinSlimeCfaCompiler());
    }

    public QinInMemoryJvmRunner(QinCfaPipeline cfaPipeline) {
        this.cfaPipeline = cfaPipeline;
        this.snapshotWriter = new QinCompileSnapshotWriter();
    }

    public Object compileAndRun(Path sourceFile, String className) throws Exception {
        Path projectRoot = sourceFile.getParent() == null
                ? Path.of("").toAbsolutePath().normalize()
                : sourceFile.getParent().toAbsolutePath().normalize();
        return compileAndRun(sourceFile, projectRoot, className);
    }

    public Object compileAndRun(Path sourceFile, Path projectRoot, String className) throws Exception {
        long startNanos = System.nanoTime();
        sourceFile = requireFile(sourceFile);
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        Path normalizedProjectRoot = projectRoot == null
                ? (sourceFile.getParent() == null
                ? Path.of("").toAbsolutePath().normalize()
                : sourceFile.getParent().toAbsolutePath().normalize())
                : projectRoot.toAbsolutePath().normalize();
        logPhase("compile start", startNanos, sourceFile.toAbsolutePath().toString());
        QinCfaCompileResult compileResult = cfaPipeline.compile(
                QinCfaCompileRequest.forJvm(sourceFile, normalizedProjectRoot, className));
        logPhase("compile done", startNanos, className);
        byte[] classBytes = compileResult.classBytes();
        if (classBytes == null || classBytes.length == 0) {
            throw new IllegalStateException("CFA compiler returned empty class bytes");
        }
        snapshotWriter.writeSnapshot(
                sourceFile,
                source,
                compileResult.linkedSource().source(),
                compileResult.astText(),
                compileResult.irBeforeLowering(),
                compileResult.loweredProgram(),
                compileResult.cfaProgram(),
                className,
                classBytes);
        logPhase("snapshot done", startNanos, className);
        ByteArrayClassLoader classLoader = new ByteArrayClassLoader(getClass().getClassLoader());
        Map<String, byte[]> declarationClassBytes = compileDeclarationClassBytes(compileResult);
        Map<String, Class<?>> declarationClasses = classLoader.defineAll(declarationClassBytes);
        bindDeclarationClasses(declarationClasses);
        Class<?> generatedClass = classLoader.define(className, classBytes);
        registerFunctionModelArtifacts(compileResult);
        logPhase("run start", startNanos, className);
        Object result = invokeRunWithRuntimeStack(generatedClass, className);
        logPhase("run done", startNanos, className);
        return result;
    }

    public Object compileAndRunModuleClasses(Path sourceFile, Path projectRoot, String className) throws Exception {
        long startNanos = System.nanoTime();
        sourceFile = requireFile(sourceFile);
        Path normalizedProjectRoot = projectRoot == null
                ? (sourceFile.getParent() == null
                ? Path.of("").toAbsolutePath().normalize()
                : sourceFile.getParent().toAbsolutePath().normalize())
                : projectRoot.toAbsolutePath().normalize();
        if (!(cfaPipeline instanceof QinSlimeCfaCompiler compiler)) {
            throw new IllegalStateException("Module-class execution requires QinSlimeCfaCompiler");
        }

        logPhase("module-class compile start", startNanos, sourceFile.toAbsolutePath().toString());
        QinCfaModuleClassCompileResult compileResult = compiler.compileModuleClasses(
                QinCfaCompileRequest.forJvm(sourceFile, normalizedProjectRoot, className));
        logPhase("module-class compile done", startNanos, className);

        ByteArrayClassLoader classLoader = new ByteArrayClassLoader(getClass().getClassLoader());
        bindModuleDeclarationClasses(classLoader, compileResult);

        Object result = null;
        QinCfaModuleClassFile initializerClassFile = compileResult.initializerClass();
        if (initializerClassFile != null) {
            Class<?> initializerClass = classLoader.define(
                    initializerClassFile.className(),
                    initializerClassFile.classBytes());
            registerFunctionModelArtifacts(initializerClassFile);
            logPhase("module-class initializer run start", startNanos, initializerClassFile.className());
            result = invokeRunWithRuntimeStack(initializerClass, initializerClassFile.className());
            logPhase("module-class initializer run done", startNanos, initializerClassFile.className());
        }

        for (QinCfaModuleClassFile moduleClassFile : compileResult.moduleClasses()) {
            Class<?> moduleClass = classLoader.define(moduleClassFile.className(), moduleClassFile.classBytes());
            registerFunctionModelArtifacts(moduleClassFile);
            logPhase("module-class run start", startNanos, moduleClassFile.className());
            result = invokeRunWithRuntimeStack(moduleClass, moduleClassFile.className());
            logPhase("module-class run done", startNanos, moduleClassFile.className());
        }
        return result;
    }

    private void bindDeclarationClasses(Map<String, Class<?>> declarationClasses) {
        for (Map.Entry<String, Class<?>> entry : declarationClasses.entrySet()) {
            Class<?> declarationClass = entry.getValue();
            JavaEsmGlobal.__qin_bind_global__(declarationClass.getSimpleName(), declarationClass);
            JavaEsmGlobal.__qin_bind_global__(entry.getKey(), declarationClass);
        }
    }

    private Map<String, byte[]> compileDeclarationClassBytes(QinCfaCompileResult compileResult) {
        if (compileResult.loweredProgram().classDeclarations().isEmpty()) {
            return Map.of();
        }
        return new QinJvmDeclarationClassEmitter().compileAllClasses(compileResult.loweredProgram());
    }

    private void bindModuleDeclarationClasses(
            ByteArrayClassLoader classLoader,
            QinCfaModuleClassCompileResult compileResult) {
        QinCfaModuleClassFile initializerClassFile = compileResult.initializerClass();
        if (initializerClassFile != null) {
            bindDeclarationClasses(classLoader.defineAll(compileDeclarationClassBytes(initializerClassFile)));
        }
        for (QinCfaModuleClassFile moduleClassFile : compileResult.moduleClasses()) {
            bindDeclarationClasses(classLoader.defineAll(compileDeclarationClassBytes(moduleClassFile)));
        }
    }

    private Map<String, byte[]> compileDeclarationClassBytes(QinCfaModuleClassFile moduleClassFile) {
        if (moduleClassFile.loweredProgram().classDeclarations().isEmpty()) {
            return Map.of();
        }
        return new QinJvmDeclarationClassEmitter().compileAllClasses(moduleClassFile.loweredProgram());
    }

    private Object invokeRunWithRuntimeStack(Class<?> generatedClass, String className) throws Exception {
        long stackBytes = Long.getLong("qin.runtime.jsRunStackBytes", DEFAULT_JS_RUN_STACK_BYTES);
        if (stackBytes <= 0) {
            return generatedClass.getMethod("run").invoke(null);
        }
        Object[] result = new Object[1];
        Throwable[] failure = new Throwable[1];
        Thread runThread = new Thread(
                null,
                () -> {
                    try {
                        result[0] = generatedClass.getMethod("run").invoke(null);
                    } catch (Throwable error) {
                        failure[0] = error;
                    }
                },
                "qin-js-runtime-" + className,
                stackBytes);
        runThread.start();
        runThread.join();
        if (failure[0] == null) {
            return result[0];
        }
        if (failure[0] instanceof Exception exception) {
            throw exception;
        }
        if (failure[0] instanceof Error error) {
            throw error;
        }
        throw new IllegalStateException("Qin runtime execution failed", failure[0]);
    }

    private void registerFunctionModelArtifacts(QinCfaCompileResult compileResult) {
        registerFunctionModelArtifacts(compileResult.loweredProgram().functionModelArtifacts());
    }

    private void registerFunctionModelArtifacts(QinCfaModuleClassFile moduleClassFile) {
        registerFunctionModelArtifacts(moduleClassFile.loweredProgram().functionModelArtifacts());
    }

    private void registerFunctionModelArtifacts(List<QinIrFunctionModelArtifact> artifacts) {
        if (artifacts.isEmpty()) {
            return;
        }
        for (QinIrFunctionModelArtifact artifact : artifacts) {
            Map<String, Object> astModel = toRuntimeMap(artifact.ast());
            QinFunctionModelRegistry.register(artifact.id(), () -> astModel);
        }
    }

    private Map<String, Object> toRuntimeMap(QinIrObjectLiteral objectLiteral) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (QinIrObjectProperty property : objectLiteral.properties()) {
            map.put(property.key(), toRuntimeValue(property.value()));
        }
        return map;
    }

    private Object toRuntimeValue(QinIrExpression expression) {
        if (expression instanceof QinIrNullLiteral) {
            return null;
        }
        if (expression instanceof QinIrStringLiteral stringLiteral) {
            return stringLiteral.value();
        }
        if (expression instanceof QinIrNumberLiteral numberLiteral) {
            return numberLiteral.value();
        }
        if (expression instanceof QinIrBooleanLiteral booleanLiteral) {
            return booleanLiteral.value();
        }
        if (expression instanceof QinIrObjectLiteral objectLiteral) {
            return toRuntimeMap(objectLiteral);
        }
        if (expression instanceof QinIrArrayLiteral arrayLiteral) {
            List<Object> values = new ArrayList<>();
            for (QinIrExpression element : arrayLiteral.elements()) {
                values.add(toRuntimeValue(element));
            }
            return values;
        }
        if (expression instanceof QinIrFunctionLiteral) {
            throw new IllegalArgumentException("Function model artifact cannot contain nested QinIrFunctionLiteral");
        }
        throw new IllegalArgumentException(
                "Unsupported function model artifact expression: " + expression.getClass().getName());
    }

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinInMemoryJvmRunner] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }

    static Path requireFile(Path file) {
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Missing file: " + file.toAbsolutePath());
        }
        return file;
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private final Map<String, byte[]> pendingClasses = new LinkedHashMap<>();
        private final Map<String, Class<?>> definedClasses = new LinkedHashMap<>();

        private ByteArrayClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Map<String, Class<?>> defineAll(Map<String, byte[]> classes) {
            if (classes == null || classes.isEmpty()) {
                return Map.of();
            }
            pendingClasses.putAll(classes);
            Map<String, Class<?>> defined = new LinkedHashMap<>();
            for (String binaryName : classes.keySet()) {
                defined.put(binaryName, define(binaryName, classes.get(binaryName)));
            }
            return Map.copyOf(defined);
        }

        private Class<?> define(String binaryName, byte[] bytes) {
            Class<?> alreadyDefined = definedClasses.get(binaryName);
            if (alreadyDefined != null) {
                return alreadyDefined;
            }
            pendingClasses.putIfAbsent(binaryName, bytes);
            return definePendingClass(binaryName);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (pendingClasses.containsKey(name)) {
                return definePendingClass(name);
            }
            Class<?> alreadyDefined = definedClasses.get(name);
            if (alreadyDefined != null) {
                return alreadyDefined;
            }
            return super.findClass(name);
        }

        private Class<?> definePendingClass(String binaryName) {
            Class<?> alreadyDefined = definedClasses.get(binaryName);
            if (alreadyDefined != null) {
                return alreadyDefined;
            }
            byte[] bytes = pendingClasses.remove(binaryName);
            if (bytes == null || bytes.length == 0) {
                throw new IllegalArgumentException("Missing generated class bytes: " + binaryName);
            }
            Class<?> defined = defineClass(binaryName, bytes, 0, bytes.length);
            definedClasses.put(binaryName, defined);
            return defined;
        }
    }
}
