package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Compiles Qin source to JVM bytecode and executes the generated run() method in-memory.
 */
public final class QinInMemoryJvmRunner {
    private final QinSlimeFrontendAdapter adapter;
    private final QinJvmClassFileBackend backend;

    public QinInMemoryJvmRunner() {
        this(new QinSlimeFrontendAdapter(), new QinJvmClassFileBackend());
    }

    public QinInMemoryJvmRunner(QinSlimeFrontendAdapter adapter, QinJvmClassFileBackend backend) {
        this.adapter = adapter;
        this.backend = backend;
    }

    public Object compileAndRun(Path sourceFile, String className) throws Exception {
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        QinIrProgram program = adapter.parseProgram(source);
        byte[] classBytes = backend.compileProgram(program, className);
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
