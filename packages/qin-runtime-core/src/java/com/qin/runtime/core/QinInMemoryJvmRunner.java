package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.lowering.jvm.QinEsmJvmLoweringContext;
import com.qin.lang.lowering.jvm.QinStrictEsmJvmLowerer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Compiles Qin source to JVM bytecode and executes the generated run() method in-memory.
 */
public final class QinInMemoryJvmRunner {
    private final QinSlimeFrontendAdapter adapter;
    private final QinJvmClassFileBackend backend;
    private final QinCompileSnapshotWriter snapshotWriter;
    private final QinFrontendCompiler frontendCompiler;
    private final QinStrictEsmJvmLowerer lowerer;

    public QinInMemoryJvmRunner() {
        this(new QinSlimeFrontendAdapter(), new QinJvmClassFileBackend());
    }

    public QinInMemoryJvmRunner(QinSlimeFrontendAdapter adapter, QinJvmClassFileBackend backend) {
        this.adapter = adapter;
        this.backend = backend;
        this.snapshotWriter = new QinCompileSnapshotWriter();
        this.frontendCompiler = new QinFrontendCompiler();
        this.lowerer = new QinStrictEsmJvmLowerer();
    }

    public Object compileAndRun(Path sourceFile, String className) throws Exception {
        sourceFile = requireFile(sourceFile);
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        Path projectRoot = sourceFile.getParent() == null
                ? Path.of("").toAbsolutePath().normalize()
                : sourceFile.getParent().toAbsolutePath().normalize();
        QinFrontendCompileResult frontendResult = frontendCompiler.compile(sourceFile, projectRoot);
        QinIrProgram irBeforeLowering = frontendResult.program();
        QinIrProgram loweredProgram = lowerer.lower(
                irBeforeLowering,
                frontendResult.semanticModel(),
                new QinEsmJvmLoweringContext(
                        frontendResult.linkedSource().entryFile(),
                        frontendResult.linkedSource().modules()));

        byte[] classBytes = backend.compileProgram(loweredProgram, className);
        snapshotWriter.writeSnapshot(
                sourceFile,
                source,
                frontendResult.linkedSource().source(),
                frontendResult.astText(),
                irBeforeLowering,
                loweredProgram,
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
