package com.qin.runtime.core;

import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaPipeline;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
        sourceFile = requireFile(sourceFile);
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        Path projectRoot = sourceFile.getParent() == null
                ? Path.of("").toAbsolutePath().normalize()
                : sourceFile.getParent().toAbsolutePath().normalize();
        QinCfaCompileResult compileResult = cfaPipeline.compile(
                QinCfaCompileRequest.forJvm(sourceFile, projectRoot, className));
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
        Class<?> generatedClass = new ByteArrayClassLoader(getClass().getClassLoader()).define(className, classBytes);
        return generatedClass.getMethod("run").invoke(null);
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
