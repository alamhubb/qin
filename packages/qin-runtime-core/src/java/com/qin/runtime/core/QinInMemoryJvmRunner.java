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
    private static final long DEFAULT_JS_RUN_TIMEOUT_MS = 0L;
    private static final int MODULE_CLASS_DISK_CACHE_VERSION = 5;

    private final QinCfaPipeline cfaPipeline;
    private final QinCompileSnapshotWriter snapshotWriter;
    private final Map<String, CachedModuleClassCompileResult> moduleClassCompileCache = new ConcurrentHashMap<>();
    private final Map<String, ModuleClassRuntimeSession> moduleClassRuntimeSessionCache = new ConcurrentHashMap<>();

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
        return compileAndRunModuleClasses(sourceFile, projectRoot, className, cacheSalt, null);
    }

    public Object compileAndRunModuleClasses(
            Path sourceFile,
            Path projectRoot,
            String className,
            String cacheSalt,
            Path cacheRoot) throws Exception {
        return compileAndRunModuleClasses(sourceFile, projectRoot, className, cacheSalt, cacheRoot, null);
    }

    public Object compileAndRunModuleClasses(
            Path sourceFile,
            Path projectRoot,
            String className,
            String cacheSalt,
            Path cacheRoot,
            String stableCacheIdentity) throws Exception {
        long startNanos = System.nanoTime();
        sourceFile = requireFile(sourceFile);
        Path normalizedProjectRoot = projectRoot == null
                ? (sourceFile.getParent() == null
                ? Path.of("").toAbsolutePath().normalize()
                : sourceFile.getParent().toAbsolutePath().normalize())
                : projectRoot.toAbsolutePath().normalize();
        Path normalizedCacheRoot = cacheRoot == null
                ? normalizedProjectRoot
                : cacheRoot.toAbsolutePath().normalize();
        if (!(cfaPipeline instanceof QinSlimeCfaCompiler compiler)) {
            throw new IllegalStateException("Module-class execution requires QinSlimeCfaCompiler");
        }

        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        String cacheKey = moduleClassCacheKey(
                sourceFile,
                normalizedProjectRoot,
                className,
                source,
                cacheSalt,
                stableCacheIdentity);
        CachedModuleClassCompileResult compileResult = moduleClassCompileCache.get(cacheKey);
        if (compileResult != null) {
            logPhase("module-class compile cache hit", startNanos, className);
            return runCachedModuleClasses(cacheKey, compileResult, startNanos);
        }

        CachedModuleClassCompileResult diskCached = readModuleClassDiskCache(normalizedCacheRoot, cacheKey);
        if (diskCached != null) {
            moduleClassCompileCache.putIfAbsent(cacheKey, diskCached);
            logPhase("module-class disk cache hit", startNanos, className);
            return runCachedModuleClasses(cacheKey, diskCached, startNanos);
        }

        logPhase("module-class compile start", startNanos, sourceFile.toAbsolutePath().toString());
        QinCfaModuleClassCompileResult compiled = compiler.compileModuleClasses(
                QinCfaCompileRequest.forJvm(sourceFile, normalizedProjectRoot, className));
        CachedModuleClassCompileResult compact = toCachedModuleClassCompileResult(compiled);
        compiled = null;
        CachedModuleClassCompileResult existing = moduleClassCompileCache.putIfAbsent(cacheKey, compact);
        compileResult = existing == null ? compact : existing;
        logPhase("module-class compile done", startNanos, className);
        writeModuleClassDiskCache(normalizedCacheRoot, cacheKey, compileResult);
        return runCachedModuleClasses(cacheKey, compileResult, startNanos);
    }

    private Object runCachedModuleClasses(
            String cacheKey,
            CachedModuleClassCompileResult compileResult,
            long startNanos) throws Exception {
        ModuleClassRuntimeSession session = moduleClassRuntimeSessionCache.computeIfAbsent(
                cacheKey,
                ignored -> new ModuleClassRuntimeSession(compileResult));
        return session.run(startNanos);
    }

    private Object runCachedModuleClass(
            ByteArrayClassLoader classLoader,
            CachedModuleClassFile moduleClassFile,
            long startNanos,
            boolean traceModules) throws Exception {
        bindDeclarationClasses(classLoader.defineAll(moduleClassFile.declarationClassBytes));
        registerFunctionModelArtifacts(moduleClassFile.functionModels);
        Class<?> moduleClass = classLoader.define(moduleClassFile.className, moduleClassFile.classBytes);
        if (traceModules) {
            logPhase("module-class run start", startNanos, moduleClassDetail(moduleClassFile));
        }
        Object result = invokeRunWithRuntimeStack(moduleClass, moduleClassFile.className);
        if (traceModules) {
            logPhase("module-class run done", startNanos, moduleClassDetail(moduleClassFile));
        }
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
                compileResult.sourceFile(),
                initializerClass,
                moduleClasses);
    }

    private CachedModuleClassFile toCachedModuleClassFile(QinCfaModuleClassFile moduleClassFile) {
        return new CachedModuleClassFile(
                moduleClassFile.sourceFile(),
                moduleClassFile.moduleIndex(),
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
        long timeoutMs = Long.getLong("qin.runtime.jsRunTimeoutMs", DEFAULT_JS_RUN_TIMEOUT_MS);
        long interpretedCallLimit = Long.getLong("qin.runtime.interpretedCallCountLimit", 0L);
        if (stackBytes <= 0) {
            return generatedClass.getMethod("run").invoke(null);
        }
        Object[] result = new Object[1];
        Throwable[] failure = new Throwable[1];
        Thread runThread = new Thread(
                null,
                () -> {
                    try {
                        if (interpretedCallLimit > 0L) {
                            JavaEsmGlobal.setInterpretedCallCountLimit(interpretedCallLimit);
                        }
                        result[0] = generatedClass.getMethod("run").invoke(null);
                    } catch (Throwable error) {
                        failure[0] = error;
                    } finally {
                        if (interpretedCallLimit > 0L) {
                            JavaEsmGlobal.clearInterpretedCallCountLimit();
                        }
                    }
                },
                "qin-js-runtime-" + className,
                stackBytes);
        runThread.setDaemon(true);
        runThread.start();
        if (timeoutMs > 0) {
            runThread.join(timeoutMs);
            if (runThread.isAlive()) {
                StringBuilder trace = new StringBuilder();
                trace.append("Qin runtime execution timed out after ")
                        .append(timeoutMs)
                        .append("ms while running ")
                        .append(className)
                        .append(System.lineSeparator());
                for (StackTraceElement frame : runThread.getStackTrace()) {
                    trace.append("  at ").append(frame).append(System.lineSeparator());
                }
                runThread.interrupt();
                throw new IllegalStateException(trace.toString());
            }
        } else {
            runThread.join();
        }
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

    private static boolean traceModuleClassRuns() {
        return Boolean.getBoolean("qin.moduleClass.trace")
                || "1".equals(System.getenv("QIN_MODULE_CLASS_TRACE"))
                || "true".equalsIgnoreCase(System.getenv("QIN_MODULE_CLASS_TRACE"));
    }

    private String moduleClassDetail(QinCfaModuleClassFile moduleClassFile) {
        return moduleClassFile.className()
                + " [moduleIndex="
                + moduleClassFile.moduleIndex()
                + ", source="
                + moduleClassFile.sourceFile().toAbsolutePath().normalize()
                + "]";
    }

    private String moduleClassDetail(CachedModuleClassFile moduleClassFile) {
        return moduleClassFile.className
                + " [moduleIndex="
                + moduleClassFile.moduleIndex
                + ", source="
                + moduleClassFile.sourceFile
                + "]";
    }

    private String moduleClassCacheKey(
            Path sourceFile,
            Path projectRoot,
            String className,
            String source,
            String cacheSalt,
            String stableCacheIdentity) {
        boolean stableIdentity = stableCacheIdentity != null && !stableCacheIdentity.isBlank();
        String sourceIdentity = stableIdentity
                ? "stable\n" + stableCacheIdentity.strip()
                : "path\n"
                + projectRoot.toAbsolutePath().normalize()
                + "\n"
                + sourceFile.toAbsolutePath().normalize();
        return sourceIdentity
                + "\n"
                + (stableIdentity ? "" : className)
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
        private final String sourceFile;
        private final CachedModuleClassFile initializerClass;
        private final List<CachedModuleClassFile> moduleClasses;

        private CachedModuleClassCompileResult(
                int version,
                Path sourceFile,
                CachedModuleClassFile initializerClass,
                List<CachedModuleClassFile> moduleClasses) {
            this.version = version;
            this.sourceFile = sourceFile == null ? "" : sourceFile.toAbsolutePath().normalize().toString();
            this.initializerClass = initializerClass;
            this.moduleClasses = List.copyOf(moduleClasses);
        }
    }

    private final class ModuleClassRuntimeSession {
        private final CachedModuleClassCompileResult compileResult;
        private final ByteArrayClassLoader classLoader;
        private boolean dependencyGraphInitialized;

        private ModuleClassRuntimeSession(CachedModuleClassCompileResult compileResult) {
            this.compileResult = compileResult;
            this.classLoader = new ByteArrayClassLoader(QinInMemoryJvmRunner.class.getClassLoader());
        }

        private synchronized Object run(long startNanos) throws Exception {
            boolean traceModules = traceModuleClassRuns();
            CachedModuleClassFile entryModuleClass = entryModuleClass();
            Object result = null;

            if (!dependencyGraphInitialized) {
                int dependencyCount = dependencyModuleCount(entryModuleClass);
                logPhase("module-class dependency session start", startNanos, "modules=" + dependencyCount);
                if (compileResult.initializerClass != null) {
                    result = runCachedModuleClass(classLoader, compileResult.initializerClass, startNanos, traceModules);
                }
                for (CachedModuleClassFile moduleClassFile : compileResult.moduleClasses) {
                    if (moduleClassFile == entryModuleClass) {
                        continue;
                    }
                    result = runCachedModuleClass(classLoader, moduleClassFile, startNanos, traceModules);
                }
                dependencyGraphInitialized = true;
                logPhase("module-class dependency session ready", startNanos, "modules=" + dependencyCount);
            } else {
                logPhase("module-class dependency session hit", startNanos, "modules=0");
            }

            if (entryModuleClass != null) {
                logPhase("module-class run batch start", startNanos, "modules=1");
                result = runCachedModuleClass(classLoader, entryModuleClass, startNanos, traceModules);
                logPhase("module-class run batch done", startNanos, "modules=1");
            }
            return result;
        }

        private int dependencyModuleCount(CachedModuleClassFile entryModuleClass) {
            int count = compileResult.initializerClass == null ? 0 : 1;
            for (CachedModuleClassFile moduleClassFile : compileResult.moduleClasses) {
                if (moduleClassFile != entryModuleClass) {
                    count++;
                }
            }
            return count;
        }

        private CachedModuleClassFile entryModuleClass() {
            if (compileResult.moduleClasses.isEmpty()) {
                return null;
            }
            for (int i = compileResult.moduleClasses.size() - 1; i >= 0; i--) {
                CachedModuleClassFile moduleClassFile = compileResult.moduleClasses.get(i);
                if (moduleClassFile.sourceFile.equals(compileResult.sourceFile)) {
                    return moduleClassFile;
                }
            }
            return compileResult.moduleClasses.get(compileResult.moduleClasses.size() - 1);
        }
    }

    private static final class CachedModuleClassFile implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String sourceFile;
        private final int moduleIndex;
        private final String className;
        private final byte[] classBytes;
        private final Map<String, byte[]> declarationClassBytes;
        private final Map<String, Map<String, Object>> functionModels;

        private CachedModuleClassFile(
                Path sourceFile,
                int moduleIndex,
                String className,
                byte[] classBytes,
                Map<String, byte[]> declarationClassBytes,
                Map<String, Map<String, Object>> functionModels) {
            this.sourceFile = sourceFile == null ? "" : sourceFile.toAbsolutePath().normalize().toString();
            this.moduleIndex = moduleIndex;
            this.className = className;
            this.classBytes = classBytes == null ? null : classBytes.clone();
            this.declarationClassBytes = Map.copyOf(declarationClassBytes);
            this.functionModels = Map.copyOf(functionModels);
        }
    }
}
