package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.lowering.jvm.QinEsmJvmLoweringContext;
import com.qin.lang.lowering.jvm.QinStrictEsmJvmLowerer;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * End-to-end ESM Qin test runner:
 * - local .qin module import resolution
 * - semantic validation
 * - IR -> .class bytecode
 * - in-memory run() invocation
 */
public final class QinEsmRuntimeTestMain {
    private static final String GENERATED_CLASS_NAME = "com.qin.runtime.generated.EsmRuntimeTest";

    private QinEsmRuntimeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path projectRoot = resolveProjectRoot();
        Path entryFile = projectRoot.resolve("main/main.qin").normalize();
        if (!Files.exists(entryFile)) {
            throw new IllegalArgumentException("Missing entry file: " + entryFile.toAbsolutePath());
        }

        QinFrontendCompiler frontendCompiler = new QinFrontendCompiler();
        QinFrontendCompileResult frontendResult = frontendCompiler.compile(entryFile, projectRoot);

        QinIrProgram program = new QinStrictEsmJvmLowerer().lower(
                frontendResult.program(),
                frontendResult.semanticModel(),
                new QinEsmJvmLoweringContext(
                        frontendResult.linkedSource().entryFile(),
                        frontendResult.linkedSource().modules()));

        QinIrValidator irValidator = new QinIrValidator();
        irValidator.validate(program, QinBuildTarget.JVM);

        byte[] classBytes = new QinJvmClassFileBackend().compileProgram(program, GENERATED_CLASS_NAME);
        Class<?> generated = new ByteArrayClassLoader(QinEsmRuntimeTestMain.class.getClassLoader())
                .define(GENERATED_CLASS_NAME, classBytes);
        Object result = generated.getMethod("run").invoke(null);

        System.out.println("project root: " + projectRoot.toAbsolutePath());
        System.out.println("entry file: " + entryFile.toAbsolutePath());
        System.out.println("run() returned: " + result);
    }

    private static Path resolveProjectRoot() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve("packages/qin-runtime-core/examples/esm-runtime"),
                cwd.resolve("qin/packages/qin-runtime-core/examples/esm-runtime"),
                cwd.resolve("examples/esm-runtime")
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate examples/esm-runtime directory.");
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
