package com.qin.core;

import com.qin.types.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Java Runner for Qin
 * Compiles and runs Java programs
 */
public class JavaRunner {
    private final QinConfig config;
    private final String classpath;
    private final String cwd;
    private final String outputDir;

    public JavaRunner(QinConfig config, String classpath) {
        this(config, classpath, System.getProperty("user.dir"));
    }

    public JavaRunner(QinConfig config, String classpath, String cwd) {
        this.config = config;
        this.classpath = classpath;
        this.cwd = cwd;
        this.outputDir = Paths.get(cwd, "build", "classes").toString();
    }

    /**
     * Compile Java source files
     */
    public CompileResult compile() {
        try {
            Files.createDirectories(Paths.get(outputDir));

            ConfigLoader configLoader = new ConfigLoader(cwd);
            ParsedEntry parsed = configLoader.parseEntry(config.entry());
            Path srcDir = Paths.get(cwd, parsed.srcDir());

            List<String> javaFiles = findJavaFiles(srcDir);
            if (javaFiles.isEmpty()) {
                return CompileResult.failure("No Java files found in " + srcDir);
            }

            // Copy resources
            copyResources(parsed.srcDir());

            // Build javac command
            List<String> args = buildCompileArgs(javaFiles);

            ProcessBuilder pb = new ProcessBuilder(args);
            pb.directory(new File(cwd));
            pb.redirectErrorStream(false);
            Process proc = pb.start();

            String stderr = readStream(proc.getErrorStream());
            int exitCode = proc.waitFor();

            if (exitCode != 0) {
                return CompileResult.failure(stderr.isEmpty() ? "Compilation failed" : stderr.trim());
            }

            return CompileResult.success(javaFiles.size(), outputDir);
        } catch (Exception e) {
            return CompileResult.failure(e.getMessage());
        }
    }

    /**
     * Run compiled Java program
     */
    public void run(List<String> args) throws Exception {
        ConfigLoader configLoader = new ConfigLoader(cwd);
        ParsedEntry parsed = configLoader.parseEntry(config.entry());

        String fullClasspath = buildFullClasspath();

        List<String> javaArgs = new ArrayList<>();
        javaArgs.add("java");
        javaArgs.add("-cp");
        javaArgs.add(fullClasspath);
        javaArgs.add(parsed.className());
        if (args != null) {
            javaArgs.addAll(args);
        }

        ProcessBuilder pb = new ProcessBuilder(javaArgs);
        pb.directory(new File(cwd));
        pb.inheritIO();
        Process proc = pb.start();

        int exitCode = proc.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Java program exited with code " + exitCode);
        }
    }

    /**
     * Compile and run in one step
     */
    public void compileAndRun(List<String> args) throws Exception {
        CompileResult result = compile();
        if (!result.isSuccess()) {
            throw new RuntimeException("Compilation failed: " + result.getError());
        }
        run(args);
    }

    private List<String> buildCompileArgs(List<String> javaFiles) {
        List<String> args = new ArrayList<>();
        args.add("javac");
        args.add("-d");
        args.add(outputDir);

        if (classpath != null && !classpath.isEmpty()) {
            args.add("-cp");
            args.add(classpath);
        }

        args.addAll(javaFiles);
        return args;
    }

    private String buildFullClasspath() {
        String sep = DependencyResolver.getClasspathSeparator();
        if (classpath != null && !classpath.isEmpty()) {
            return outputDir + sep + classpath;
        }
        return outputDir;
    }

    private List<String> findJavaFiles(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return new ArrayList<>();
        }

        try (Stream<Path> walk = Files.walk(dir)) {
            return walk
                .filter(p -> p.toString().endsWith(".java"))
                .map(Path::toString)
                .collect(Collectors.toList());
        }
    }

    private void copyResources(String srcDir) throws IOException {
        String[] resourceDirs = {
            Paths.get(cwd, "src", "resources").toString(),
            Paths.get(cwd, "src", "main", "resources").toString(),
            Paths.get(cwd, srcDir, "resources").toString()
        };

        for (String resourceDir : resourceDirs) {
            Path resPath = Paths.get(resourceDir);
            if (Files.exists(resPath)) {
                copyDir(resPath, Paths.get(outputDir));
            }
        }
    }

    private void copyDir(Path src, Path dest) throws IOException {
        try (Stream<Path> walk = Files.walk(src)) {
            walk.forEach(source -> {
                try {
                    Path target = dest.resolve(src.relativize(source));
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(target);
                    } else {
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
