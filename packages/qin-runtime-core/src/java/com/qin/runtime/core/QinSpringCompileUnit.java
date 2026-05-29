package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Shared compile unit for Qin declaration-oriented Spring/JVM source files.
 */
public final class QinSpringCompileUnit {
    private final QinIrProgram program;
    private final Map<String, byte[]> compiledClasses;

    private QinSpringCompileUnit(QinIrProgram program, Map<String, byte[]> compiledClasses) {
        this.program = Objects.requireNonNull(program, "program cannot be null");
        this.compiledClasses = Map.copyOf(compiledClasses);
    }

    public static QinSpringCompileUnit compile(Path sourceFile) throws Exception {
        return compileAll(sourceFile);
    }

    public static QinSpringCompileUnit compileAll(Path... sourceFiles) throws Exception {
        if (sourceFiles == null || sourceFiles.length == 0) {
            throw new IllegalArgumentException("At least one Qin source file is required");
        }

        List<QinIrClassDeclaration> classDeclarations = new ArrayList<>();
        Map<String, Path> declarationOwners = new LinkedHashMap<>();
        QinFrontendLowerer frontendLowerer = new QinFrontendLowerer();
        for (Path sourceFile : sourceFiles) {
            Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
            Path normalizedSource = sourceFile.toAbsolutePath().normalize();
            if (!Files.exists(normalizedSource) || !Files.isRegularFile(normalizedSource)) {
                throw new IllegalArgumentException("Missing Qin source: " + normalizedSource);
            }

            String source = Files.readString(normalizedSource, StandardCharsets.UTF_8);
            QinIrProgram program = frontendLowerer.lowerSource(source);
            for (QinIrClassDeclaration declaration : program.classDeclarations()) {
                Path previousOwner = declarationOwners.putIfAbsent(declaration.binaryName(), normalizedSource);
                if (previousOwner != null) {
                    throw new IllegalStateException(
                            "Duplicate Qin declaration class `"
                                    + declaration.binaryName()
                                    + "` in "
                                    + previousOwner
                                    + " and "
                                    + normalizedSource);
                }
                classDeclarations.add(declaration);
            }
        }

        QinIrProgram mergedProgram = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                classDeclarations);
        Map<String, byte[]> compiledClasses = new QinJvmDeclarationClassEmitter().compileAllClasses(mergedProgram);
        return new QinSpringCompileUnit(mergedProgram, compiledClasses);
    }

    public static QinSpringCompileUnit compile(Path sourceFile, Path... additionalSourceFiles) throws Exception {
        Objects.requireNonNull(sourceFile, "sourceFile cannot be null");
        if (additionalSourceFiles == null || additionalSourceFiles.length == 0) {
            return compileAll(sourceFile);
        }
        Path[] allSources = new Path[additionalSourceFiles.length + 1];
        allSources[0] = sourceFile;
        System.arraycopy(additionalSourceFiles, 0, allSources, 1, additionalSourceFiles.length);
        return compileAll(allSources);
    }

    public QinIrProgram program() {
        return program;
    }

    public Map<String, byte[]> compiledClasses() {
        return compiledClasses;
    }

    public Map<String, Class<?>> defineAll(ClassLoader parent) {
        Objects.requireNonNull(parent, "parent cannot be null");
        ByteArrayClassLoader loader = new ByteArrayClassLoader(parent);
        Map<String, Class<?>> defined = new LinkedHashMap<>();
        for (Map.Entry<String, byte[]> entry : compiledClasses.entrySet()) {
            defined.put(entry.getKey(), loader.define(entry.getKey(), entry.getValue()));
        }
        return Map.copyOf(defined);
    }

    public QinIrClassDeclaration requireClass(String binaryName) {
        for (QinIrClassDeclaration declaration : program.classDeclarations()) {
            if (binaryName.equals(declaration.binaryName())) {
                return declaration;
            }
        }
        throw new IllegalArgumentException("Missing Qin declaration class: " + binaryName);
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
