package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Demo runner:
 * import { Math } from "java:java.lang";
 * console.log(Math.random());
 */
public final class QinJavaImportMathDemoMain {
    private QinJavaImportMathDemoMain() {
    }

    public static void main(String[] args) throws Exception {
        Path sourceFile = resolveSourceFile(args);
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);

        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        QinIrProgram program = adapter.parseProgram(source);

        QinJvmClassFileBackend backend = new QinJvmClassFileBackend();
        String className = "com.qin.runtime.generated.MathRandomDemo";
        byte[] classBytes = backend.compileProgram(program, className);

        Class<?> generatedClass = new ByteArrayClassLoader(QinJavaImportMathDemoMain.class.getClassLoader())
                .define(className, classBytes);
        Object result = generatedClass.getMethod("run").invoke(null);
        System.out.println("source file: " + sourceFile.toAbsolutePath());
        System.out.println("run() returned: " + result);
    }

    private static Path resolveSourceFile(String[] args) {
        if (args.length > 0 && !args[0].isBlank()) {
            Path file = Path.of(args[0]).toAbsolutePath().normalize();
            requireFile(file);
            return file;
        }
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[]{
                cwd.resolve("qin/packages/qin-runtime-core/examples/java-import-math.qin"),
                cwd.resolve("packages/qin-runtime-core/examples/java-import-math.qin")
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(
                "Cannot find java-import-math.qin. Pass file path as arg[0].");
    }

    private static void requireFile(Path file) {
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Missing file: " + file.toAbsolutePath());
        }
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
