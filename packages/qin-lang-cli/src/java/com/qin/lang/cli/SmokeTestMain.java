package com.qin.lang.cli;

import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Path;
import java.util.Map;

/**
 * Minimal end-to-end smoke test:
 * source string -> Slime parse -> Qin IR -> JVM class file.
 */
public final class SmokeTestMain {
    private SmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = "const a = () => 1; console.log(a())";
        String className = "com.qin.generated.Demo";
        Path outputDir = Path.of("build", "generated-classes");

        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        QinIrProgram program = adapter.parseProgram(source);

        QinJvmClassFileBackend backend = new QinJvmClassFileBackend();
        byte[] classBytes = backend.compileProgram(program, className);
        Path classFile = QinClassFileWriter.writeClassFile(outputDir, className, classBytes);

        Class<?> generatedClass = new ByteArrayClassLoader(SmokeTestMain.class.getClassLoader())
                .define(className, classBytes);
        Object runResult = generatedClass.getMethod("run").invoke(null);

        System.out.println("Source: " + source);
        System.out.println("Generated .class: " + classFile.toAbsolutePath());
        System.out.println("run() result: " + runResult);
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private ByteArrayClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] classBytes) {
            return defineClass(binaryName, classBytes, 0, classBytes.length);
        }
    }
}

