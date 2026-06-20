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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Compiles Qin source to JVM bytecode and executes the generated run() method in-memory.
 */
public final class QinInMemoryJvmRunner {
    private static final long DEFAULT_JS_RUN_STACK_BYTES = 32L * 1024L * 1024L;
    private static final int MODULE_CLASS_DISK_CACHE_VERSION = 1;

    private final QinCfaPipeline cfaPipeline;
    private final QinCompileSnapshotWriter snapshotWriter;
    private final Map<String, QinCfaModuleClassCompileResult> moduleClassCompileCache = new ConcurrentHashMap<>();

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
        return compileAndRunModuleClasses(sourceFile, projectRoot, className, "");
    }

    public Object compileAndRunModuleClasses(
            Path sourceFile,
            Path projectRoot,
            String className,
            String cacheSalt) throws Exception {
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

        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        String cacheKey = moduleClassCacheKey(sourceFile, normalizedProjectRoot, className, source, cacheSalt);
        CachedModuleClassCompileResult diskCached = readModuleClassDiskCache(normalizedProjectRoot, cacheKey);
        if (diskCached != null) {
            logPhase("module-class disk cache hit", startNanos, className);
            return runCachedModuleClasses(diskCached, startNanos);
        }

        QinCfaModuleClassCompileResult compileResult = moduleClassCompileCache.get(cacheKey);
        if (compileResult == null) {
            logPhase("module-class compile start", startNanos, sourceFile.toAbsolutePath().toString());
            QinCfaModuleClassCompileResult compiled = compiler.compileModuleClasses(
                    QinCfaCompileRequest.forJvm(sourceFile, normalizedProjectRoot, className));
            QinCfaModuleClassCompileResult existing = moduleClassCompileCache.putIfAbsent(cacheKey, compiled);
            compileResult = existing == null ? compiled : existing;
            logPhase("module-class compile done", startNanos, className);
            writeModuleClassDiskCache(normalizedProjectRoot, cacheKey, toCachedModuleClassCompileResult(compileResult));
        } else {
            logPhase("module-class compile cache hit", startNanos, className);
        }

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

    private Object runCachedModuleClasses(CachedModuleClassCompileResult compileResult, long startNanos) throws Exception {
        ByteArrayClassLoader classLoader = new ByteArrayClassLoader(getClass().getClassLoader());
        Object result = null;
        if (compileResult.initializerClass != null) {
            result = runCachedModuleClass(classLoader, compileResult.initializerClass, startNanos);
        }
        for (CachedModuleClassFile moduleClassFile : compileResult.moduleClasses) {
            result = runCachedModuleClass(classLoader, moduleClassFile, startNanos);
        }
        return result;
    }

    private Object runCachedModuleClass(
            ByteArrayClassLoader classLoader,
            CachedModuleClassFile moduleClassFile,
            long startNanos) throws Exception {
        bindDeclarationClasses(classLoader.defineAll(moduleClassFile.declarationClassBytes));
        registerFunctionModelArtifacts(moduleClassFile.functionModels);
        Class<?> moduleClass = classLoader.define(moduleClassFile.className, moduleClassFile.classBytes);
        logPhase("module-class run start", startNanos, moduleClassFile.className);
        Object result = invokeRunWithRuntimeStack(moduleClass, moduleClassFile.className);
        logPhase("module-class run done", startNanos, moduleClassFile.className);
        return result;
    }

    private CachedModuleClassCompileResult toCachedModuleClassCompileResult(
            QinCfaModuleClassCompileResult compileResult) {
        CachedModuleClassFile initializerClass = compileResult.initializerClass() == null
                ? null
                : toCachedModuleClassFile(compileResult.initializerClass());
        List<CachedModuleClassFile> moduleClasses = new ArrayList<>();
        for (QinCfaModuleClassFile moduleClassFile : compileResult.moduleClasses()) {
            moduleClasses.add(toCachedModuleClassFile(moduleClassFile));
        }
        return new CachedModuleClassCompileResult(
                MODULE_CLASS_DISK_CACHE_VERSION,
                initializerClass,
                moduleClasses);
    }

    private CachedModuleClassFile toCachedModuleClassFile(QinCfaModuleClassFile moduleClassFile) {
        return new CachedModuleClassFile(
                moduleClassFile.className(),
                moduleClassFile.classBytes(),
                compileDeclarationClassBytes(moduleClassFile),
                functionModelArtifacts(moduleClassFile.loweredProgram().functionModelArtifacts()));
    }

    private Map<String, Map<String, Object>> functionModelArtifacts(List<QinIrFunctionModelArtifact> artifacts) {
        if (artifacts.isEmpty()) {
            return Map.of();
        }
        Map<String, Map<String, Object>> models = new LinkedHashMap<>();
        for (QinIrFunctionModelArtifact artifact : artifacts) {
            models.put(artifact.id(), toRuntimeMap(artifact.ast()));
        }
        return models;
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
            QinFunctionModelRegistry.register(artifact.id(), () -> toRuntimeMap(artifact.ast()));
        }
    }

    private void registerFunctionModelArtifacts(Map<String, Map<String, Object>> models) {
        if (models == null || models.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Map<String, Object>> entry : models.entrySet()) {
            QinFunctionModelRegistry.register(entry.getKey(), () -> deepCopyRuntimeMap(entry.getValue()));
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

    private Map<String, Object> deepCopyRuntimeMap(Map<String, Object> source) {
        Map<String, Object> copy = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            copy.put(entry.getKey(), deepCopyRuntimeValue(entry.getValue()));
        }
        return copy;
    }

    @SuppressWarnings("unchecked")
    private Object deepCopyRuntimeValue(Object value) {
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> copy = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                copy.put(String.valueOf(entry.getKey()), deepCopyRuntimeValue(entry.getValue()));
            }
            return copy;
        }
        if (value instanceof List<?> list) {
            List<Object> copy = new ArrayList<>();
            for (Object element : list) {
                copy.add(deepCopyRuntimeValue(element));
            }
            return copy;
        }
        return value;
    }

    private void logPhase(String phase, long startNanos, String detail) {
        long elapsedMs = (System.nanoTime() - startNanos) / 1_000_000L;
        System.out.println("[QinInMemoryJvmRunner] " + phase + " +" + elapsedMs + "ms :: " + detail);
    }

    private String moduleClassCacheKey(
            Path sourceFile,
            Path projectRoot,
            String className,
            String source,
            String cacheSalt) {
        return projectRoot.toAbsolutePath().normalize()
                + "\n"
                + sourceFile.toAbsolutePath().normalize()
                + "\n"
                + className
                + "\n"
                + (cacheSalt == null ? "" : cacheSalt)
                + "\n"
                + sha256(source);
    }

    private CachedModuleClassCompileResult readModuleClassDiskCache(Path projectRoot, String cacheKey) {
        Path cacheFile = moduleClassDiskCacheFile(projectRoot, cacheKey);
        if (!Files.isRegularFile(cacheFile)) {
            return null;
        }
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(cacheFile))) {
            Object value = input.readObject();
            if (value instanceof CachedModuleClassCompileResult cached
                    && cached.version == MODULE_CLASS_DISK_CACHE_VERSION) {
                return cached;
            }
        } catch (IOException | ClassNotFoundException error) {
            System.err.println("[WARN] failed to read Qin module-class disk cache: " + error.getMessage());
        }
        return null;
    }

    private void writeModuleClassDiskCache(
            Path projectRoot,
            String cacheKey,
            CachedModuleClassCompileResult compileResult) {
        Path cacheFile = moduleClassDiskCacheFile(projectRoot, cacheKey);
        Path tempFile = cacheFile.resolveSibling(cacheFile.getFileName() + ".tmp");
        try {
            Files.createDirectories(cacheFile.getParent());
            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(tempFile))) {
                output.writeObject(compileResult);
            }
            Files.move(tempFile, cacheFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException error) {
            System.err.println("[WARN] failed to write Qin module-class disk cache: " + error.getMessage());
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException ignored) {
                // Best effort cleanup only.
            }
        }
    }

    private Path moduleClassDiskCacheFile(Path projectRoot, String cacheKey) {
        return projectRoot.toAbsolutePath().normalize()
                .resolve(".qin")
                .resolve("cache")
                .resolve("jvm-module-classes")
                .resolve(sha256(cacheKey) + ".bin");
    }

    private static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(text.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException("SHA-256 is not available", error);
        }
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

    private static final class CachedModuleClassCompileResult implements Serializable {
        private static final long serialVersionUID = 1L;

        private final int version;
        private final CachedModuleClassFile initializerClass;
        private final List<CachedModuleClassFile> moduleClasses;

        private CachedModuleClassCompileResult(
                int version,
                CachedModuleClassFile initializerClass,
                List<CachedModuleClassFile> moduleClasses) {
            this.version = version;
            this.initializerClass = initializerClass;
            this.moduleClasses = List.copyOf(moduleClasses);
        }
    }

    private static final class CachedModuleClassFile implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String className;
        private final byte[] classBytes;
        private final Map<String, byte[]> declarationClassBytes;
        private final Map<String, Map<String, Object>> functionModels;

        private CachedModuleClassFile(
                String className,
                byte[] classBytes,
                Map<String, byte[]> declarationClassBytes,
                Map<String, Map<String, Object>> functionModels) {
            this.className = className;
            this.classBytes = classBytes == null ? null : classBytes.clone();
            this.declarationClassBytes = Map.copyOf(declarationClassBytes);
            this.functionModels = Map.copyOf(functionModels);
        }
    }
}
