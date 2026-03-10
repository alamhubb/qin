package com.qin.lang.cli;

import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simplest file-based Qin compiler entry:
 * reads test.qin and writes one .class file.
 */
public final class SimpleQinCompilerMain {
    private SimpleQinCompilerMain() {
    }

    public static void main(String[] args) throws Exception {
        Path sourceFile = Path.of("test.qin");
        if (!Files.exists(sourceFile)) {
            throw new IllegalArgumentException("Missing file: " + sourceFile.toAbsolutePath());
        }

        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        String className = "com.qin.generated.TestQin";
        Path outputDir = Path.of("build", "generated-classes");

        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        QinIrProgram program = adapter.parseConstObjectDeclaration(source);

        QinJvmClassFileBackend backend = new QinJvmClassFileBackend();
        byte[] classBytes = backend.compileProgram(program, className);
        Path classFile = QinClassFileWriter.writeClassFile(outputDir, className, classBytes);

        System.out.println("Input: " + sourceFile.toAbsolutePath());
        System.out.println("Generated .class: " + classFile.toAbsolutePath());
    }
}
