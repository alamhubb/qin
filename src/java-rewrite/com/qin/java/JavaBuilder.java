package com.qin.java;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Java Builder - compiles Java source files
 */
public class JavaBuilder {
    private final String srcDir;
    private final String outDir;

    public JavaBuilder(String srcDir, String outDir) {
        this.srcDir = srcDir;
        this.outDir = outDir;
    }

    /**
     * Compile Java files
     */
    public boolean compile(List<String> javaFiles) throws Exception {
        if (javaFiles == null || javaFiles.isEmpty()) {
            return false;
        }

        Files.createDirectories(Paths.get(outDir));

        List<String> args = new ArrayList<>();
        args.add("javac");
        args.add("-d");
        args.add(outDir);
        args.addAll(javaFiles);

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        Process proc = pb.start();

        String output = readStream(proc.getInputStream());
        int exitCode = proc.waitFor();

        if (exitCode != 0) {
            System.err.println(output);
            return false;
        }

        return true;
    }

    /**
     * Compile all Java files in source directory
     */
    public boolean compileAll() throws Exception {
        return compileAll(null);
    }

    /**
     * Compile all Java files with classpath
     */
    public boolean compileAll(String classpath) throws Exception {
        Path srcPath = Paths.get(srcDir);
        if (!Files.exists(srcPath)) {
            return false;
        }

        List<String> javaFiles;
        try (Stream<Path> walk = Files.walk(srcPath)) {
            javaFiles = walk
                .filter(p -> p.toString().endsWith(".java"))
                .map(Path::toString)
                .collect(Collectors.toList());
        }

        if (javaFiles.isEmpty()) {
            return false;
        }

        Files.createDirectories(Paths.get(outDir));

        List<String> args = new ArrayList<>();
        args.add("javac");
        args.add("-d");
        args.add(outDir);

        if (classpath != null && !classpath.isEmpty()) {
            args.add("-cp");
            args.add(classpath);
        }

        args.addAll(javaFiles);

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        Process proc = pb.start();

        String output = readStream(proc.getInputStream());
        int exitCode = proc.waitFor();

        if (exitCode != 0) {
            System.err.println(output);
            return false;
        }

        return true;
    }

    /**
     * Find all Java files in source directory
     */
    public List<String> findJavaFiles() throws IOException {
        Path srcPath = Paths.get(srcDir);
        if (!Files.exists(srcPath)) {
            return new ArrayList<>();
        }

        try (Stream<Path> walk = Files.walk(srcPath)) {
            return walk
                .filter(p -> p.toString().endsWith(".java"))
                .map(Path::toString)
                .collect(Collectors.toList());
        }
    }

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
