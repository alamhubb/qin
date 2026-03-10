package com.qin.lang.backend.jvm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility to write generated class files.
 */
public final class QinClassFileWriter {
    private QinClassFileWriter() {
    }

    public static Path writeClassFile(Path outputDir, String binaryClassName, byte[] classBytes) throws IOException {
        Objects.requireNonNull(outputDir, "outputDir cannot be null");
        Objects.requireNonNull(binaryClassName, "binaryClassName cannot be null");
        Objects.requireNonNull(classBytes, "classBytes cannot be null");

        Path classFile = outputDir.resolve(binaryClassName.replace('.', '/') + ".class");
        Path parent = classFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(classFile, classBytes);
        return classFile;
    }
}

