package com.qin.lang.cli;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

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

        String source = readSource(options);
        String className = options.className;
        Path outputDir = options.outputDir;

        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        QinIrProgram program = adapter.parseConstObjectDeclaration(source);

        System.out.println("Source: " + source);

        byte[] classBytes = null;
        Path classFile = null;
        Path jsFile = null;

        if (options.target.emitJvm()) {
            QinJvmClassFileBackend backend = new QinJvmClassFileBackend();
            classBytes = backend.compileProgram(program, className);
            classFile = QinClassFileWriter.writeClassFile(outputDir, className, classBytes);
            System.out.println("Generated .class: " + classFile.toAbsolutePath());
            System.out.println("Class name: " + className);
        }

        if (options.target.emitJs()) {
            QinJsBackend jsBackend = new QinJsBackend();
            String jsCode = jsBackend.compileProgram(program);
            Path parent = options.jsOutFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(options.jsOutFile, jsCode, StandardCharsets.UTF_8);
            jsFile = options.jsOutFile;
            System.out.println("Generated .js: " + jsFile.toAbsolutePath());
        }

        if (options.runAfterCompile) {
            if (options.target.emitJvm()) {
                Class<?> generatedClass = new ByteArrayClassLoader(QinCompileMain.class.getClassLoader())
                        .define(className, classBytes);
                Object runResult = generatedClass.getMethod("run").invoke(null);
                System.out.println("run() result: " + runResult);
            } else {
                throw new IllegalArgumentException("--run currently requires --target jvm or --target both");
            }
        }
    }

    private static String readSource(Options options) throws Exception {
        if (options.inlineSource != null) {
            return options.inlineSource;
        }
        if (options.sourceFile != null) {
            return Files.readString(options.sourceFile, StandardCharsets.UTF_8);
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
                "  --file path/to/input.qin [--target jvm|js|both] [--class com.qin.generated.Demo] [--out build/generated-classes] [--js-out build/generated-js/app.js] [--run]");
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
