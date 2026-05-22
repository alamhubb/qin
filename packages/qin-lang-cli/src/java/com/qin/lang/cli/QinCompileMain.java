package com.qin.lang.cli;

import com.qin.runtime.core.QinBuildCoordinator;
import com.qin.runtime.core.QinBuildRequest;
import com.qin.runtime.core.QinBuildResult;
import com.qin.runtime.core.QinBuildTarget;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Minimal Qin compiler CLI for the current POC grammar.
 */
public final class QinCompileMain {
    private QinCompileMain() {
    }

    public static void main(String[] args) throws Exception {
        Options options = parseArgs(args);
        if (options.showHelp) {
            printHelp();
            return;
        }

        SourceInput sourceInput = resolveSourceInput(options);
        String className = options.className;
        Path outputDir = options.outputDir;
        Path projectRoot = Path.of("").toAbsolutePath().normalize();

        QinBuildCoordinator coordinator = new QinBuildCoordinator();
        QinBuildResult buildResult = coordinator.build(new QinBuildRequest(
                projectRoot,
                sourceInput.sourceFile(),
                options.target.toBuildTarget(),
                className,
                outputDir,
                options.jsOutFile,
                false));

        System.out.println("Source: " + sourceInput.sourceText());

        byte[] classBytes = null;
        Path classFile = buildResult.classFile();
        Path jsFile = buildResult.jsFile();

        if (classFile != null) {
            System.out.println("Generated .class: " + classFile.toAbsolutePath());
            System.out.println("Class name: " + className);
        }

        if (jsFile != null) {
            System.out.println("Generated .js: " + jsFile.toAbsolutePath());
        }

        if (options.runAfterCompile) {
            if (options.target.emitJvm()) {
                if (classFile == null) {
                    throw new IllegalStateException("Missing class output for JVM run");
                }
                classBytes = Files.readAllBytes(classFile);
                Class<?> generatedClass = new ByteArrayClassLoader(QinCompileMain.class.getClassLoader())
                        .define(className, classBytes);
                Object runResult = generatedClass.getMethod("run").invoke(null);
                System.out.println("run() result: " + runResult);
            } else {
                throw new IllegalArgumentException("--run currently requires --target jvm or --target both");
            }
        }
    }

    private static SourceInput resolveSourceInput(Options options) throws Exception {
        if (options.inlineSource != null) {
            Path tempDir = Path.of("build", "generated-sources", "qin-compile-inline");
            Files.createDirectories(tempDir);
            Path tempFile = tempDir.resolve("inline-" + System.nanoTime() + ".js");
            Files.writeString(tempFile, options.inlineSource, StandardCharsets.UTF_8);
            return new SourceInput(options.inlineSource, tempFile);
        }
        if (options.sourceFile != null) {
            Path sourceFile = options.sourceFile;
            String sourceText = Files.readString(sourceFile, StandardCharsets.UTF_8);
            return new SourceInput(sourceText, sourceFile);
        }
        throw new IllegalArgumentException("Missing source. Use --source or --file.");
    }

    private static Options parseArgs(String[] args) {
        Options options = new Options();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--help" -> options.showHelp = true;
                case "--run" -> options.runAfterCompile = true;
                case "--source" -> options.inlineSource = nextValue(args, ++i, "--source");
                case "--file" -> options.sourceFile = Path.of(nextValue(args, ++i, "--file"));
                case "--class" -> options.className = nextValue(args, ++i, "--class");
                case "--out" -> options.outputDir = Path.of(nextValue(args, ++i, "--out"));
                case "--target" -> options.target = Target.parse(nextValue(args, ++i, "--target"));
                case "--js-out" -> options.jsOutFile = Path.of(nextValue(args, ++i, "--js-out"));
                default -> throw new IllegalArgumentException("Unknown arg: " + arg);
            }
        }

        if (options.inlineSource != null && options.sourceFile != null) {
            throw new IllegalArgumentException("Use either --source or --file, not both.");
        }
        return options;
    }

    private static String nextValue(String[] args, int index, String flag) {
        if (index >= args.length) {
            throw new IllegalArgumentException("Missing value for " + flag);
        }
        return args[index];
    }

    private static void printHelp() {
        System.out.println("QinCompileMain - compile Qin source to a .class");
        System.out.println("Usage:");
        System.out.println(
                "  --source \"const a = { age: 1 }\" [--target jvm|js|both] [--class com.qin.generated.Demo] [--out build/generated-classes] [--js-out build/generated-js/app.js] [--run]");
        System.out.println(
                "  --file path/to/input.(qin|js|mjs|ts) [--target jvm|js|both] [--class com.qin.generated.Demo] [--out build/generated-classes] [--js-out build/generated-js/app.js] [--run]");
    }

    private static final class Options {
        private String inlineSource;
        private Path sourceFile;
        private String className = "com.qin.generated.Demo";
        private Path outputDir = Path.of("build", "generated-classes");
        private Path jsOutFile = Path.of("build", "generated-js", "app.js");
        private Target target = Target.JVM;
        private boolean runAfterCompile;
        private boolean showHelp;
    }

    private record SourceInput(String sourceText, Path sourceFile) {
    }

    private enum Target {
        JVM,
        JS,
        BOTH;

        private boolean emitJvm() {
            return this == JVM || this == BOTH;
        }

        private boolean emitJs() {
            return this == JS || this == BOTH;
        }

        private QinBuildTarget toBuildTarget() {
            return switch (this) {
                case JVM -> QinBuildTarget.JVM;
                case JS -> QinBuildTarget.JS;
                case BOTH -> QinBuildTarget.BOTH;
            };
        }

        private static Target parse(String raw) {
            return switch (raw.toLowerCase()) {
                case "jvm" -> JVM;
                case "js" -> JS;
                case "both" -> BOTH;
                default -> throw new IllegalArgumentException("Unknown --target value: " + raw);
            };
        }
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
