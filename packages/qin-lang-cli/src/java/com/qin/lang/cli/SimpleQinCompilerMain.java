package com.qin.lang.cli;

import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Simplest file-based Qin compiler entry:
 * reads test.js and writes one .class file.
 */
public final class SimpleQinCompilerMain {
    private SimpleQinCompilerMain() {
    }

    public static void main(String[] args) throws Exception {
        Path sourceFile = resolveSourceFile(args);

        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        String className = "com.qin.generated.TestQin";
        Path outputDir = Path.of("build", "generated-classes");

        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        QinIrProgram program = adapter.parseConstObjectDeclaration(source);

        QinJvmClassFileBackend backend = new QinJvmClassFileBackend();
        byte[] classBytes = backend.compileProgram(program, className);
        Path classFile = QinClassFileWriter.writeClassFile(outputDir, className, classBytes);

        System.out.println("Working dir: " + Path.of("").toAbsolutePath());
        System.out.println("Input: " + sourceFile.toAbsolutePath());
        System.out.println("Generated .class: " + classFile.toAbsolutePath());
    }

    private static Path resolveSourceFile(String[] args) {
        List<Path> candidates = new ArrayList<>();
        if (args.length > 0 && !args[0].isBlank()) {
            candidates.add(Path.of(args[0]));
        }
        candidates.add(Path.of("test.js"));
        candidates.add(Path.of("qin", "packages", "qin-lang-cli", "test.js"));

        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate.normalize();
            }
        }

        StringBuilder message = new StringBuilder("Missing source file. Tried:\n");
        for (Path candidate : candidates) {
            message.append("  - ").append(candidate.toAbsolutePath()).append('\n');
        }
        throw new IllegalArgumentException(message.toString().trim());
    }
}
