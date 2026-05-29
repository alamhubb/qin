package com.qin.runtime.core;

import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaPipeline;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;
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
        Class<?> generatedClass = new ByteArrayClassLoader(getClass().getClassLoader()).define(className, classBytes);
        registerFunctionModelArtifacts(compileResult);
        logPhase("run start", startNanos, className);
        Object result = generatedClass.getMethod("run").invoke(null);
        logPhase("run done", startNanos, className);
        return result;
    }

    private void registerFunctionModelArtifacts(QinCfaCompileResult compileResult) {
        List<QinIrFunctionModelArtifact> artifacts = compileResult.loweredProgram().functionModelArtifacts();
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
        private ByteArrayClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] bytes) {
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
