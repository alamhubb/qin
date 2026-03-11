package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Qin runtime bootstrap entry.
 *
 * Current stage:
 * 1) Parse Qin source via java-slime frontend adapter.
 * 2) Lower to Qin IR.
 * 3) Emit JVM .class and/or JS output.
 */
public final class QinRuntimeMain {
    private QinRuntimeMain() {
    }

    public static void main(String[] args) throws Exception {
        Options options = parseArgs(args);
        if (options.showHelp) {
            printHelp();
            return;
        }

        Path root = resolveRoot(options.rootDir);
        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        Path sourceFile = resolveSourceFile(options, layout);
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);

        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        QinIrProgram program = adapter.parseProgram(source);

        if (options.printIr) {
            System.out.println("IR declarations: " + program.declarations().size());
            System.out.println("IR console logs: " + program.consoleLogs().size());
            System.out.println("IR java imports: " + program.javaImports().size());
            System.out.println("IR java static console logs: " + program.javaStaticConsoleLogs().size());
        }

        if (options.target.emitJvm()) {
            QinJvmClassFileBackend jvmBackend = new QinJvmClassFileBackend();
            byte[] classBytes = jvmBackend.compileProgram(program, options.className);
            Path classFile = QinClassFileWriter.writeClassFile(options.classOutputDir, options.className, classBytes);
            System.out.println("Generated .class: " + classFile.toAbsolutePath());
        }

        if (options.target.emitJs()) {
            QinJsBackend jsBackend = new QinJsBackend();
            String jsCode = jsBackend.compileProgram(program);
            Path parent = options.jsOutputFile.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(options.jsOutputFile, jsCode, StandardCharsets.UTF_8);
            System.out.println("Generated .js: " + options.jsOutputFile.toAbsolutePath());
        }

        System.out.println("Project root: " + root.toAbsolutePath());
        System.out.println("Source file: " + sourceFile.toAbsolutePath());
        System.out.println("Layout shared: " + layout.sharedDir().toAbsolutePath());
        System.out.println("Layout app: " + layout.appDir().toAbsolutePath());
        System.out.println("Layout backend entry: " + layout.backendEntry().toAbsolutePath());
    }

    private static Path resolveRoot(Path rootDir) {
        if (rootDir != null) {
            return rootDir.toAbsolutePath().normalize();
        }
        return Path.of("").toAbsolutePath().normalize();
    }

    private static Path resolveSourceFile(Options options, QinRuntimeProjectLayout layout) {
        if (options.sourceFile != null) {
            Path file = options.sourceFile;
            if (!file.isAbsolute()) {
                file = layout.root().resolve(file).normalize();
            }
            requireFile(file, "--file");
            return file;
        }

        Path detected = layout.resolveDefaultQinSource();
        if (detected != null) {
            return detected;
        }

        throw new IllegalArgumentException(
                "No qin source found. Use --file, or provide one of: shared/main.qin, shared/shared.qin, main/main.qin, app/main.qin");
    }

    private static void requireFile(Path file, String from) {
        Objects.requireNonNull(file, "file cannot be null");
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Missing source file from " + from + ": " + file.toAbsolutePath());
        }
    }

    private static Options parseArgs(String[] args) {
        Options options = new Options();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--help" -> options.showHelp = true;
                case "--print-ir" -> options.printIr = true;
                case "--root" -> options.rootDir = Path.of(nextValue(args, ++i, "--root"));
                case "--file" -> options.sourceFile = Path.of(nextValue(args, ++i, "--file"));
                case "--target" -> options.target = Target.parse(nextValue(args, ++i, "--target"));
                case "--class" -> options.className = nextValue(args, ++i, "--class");
                case "--class-out" -> options.classOutputDir = Path.of(nextValue(args, ++i, "--class-out"));
                case "--js-out" -> options.jsOutputFile = Path.of(nextValue(args, ++i, "--js-out"));
                default -> throw new IllegalArgumentException("Unknown arg: " + arg);
            }
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
        System.out.println("QinRuntimeMain - parse Qin source with java-slime and emit targets");
        System.out.println("Usage:");
        System.out.println("  --root <dir>                 Project root (default: current dir)");
        System.out.println("  --file <path.qin>            Qin source path (relative to root)");
        System.out.println("  --target jvm|js|both         Output target (default: both)");
        System.out.println("  --class <binary.name>        JVM class name (default: com.qin.runtime.generated.App)");
        System.out.println("  --class-out <dir>            JVM .class output dir (default: build/runtime-classes)");
        System.out.println("  --js-out <file>              JS output file (default: build/runtime-web/app.js)");
        System.out.println("  --print-ir                   Print IR summary");
    }

    private static final class Options {
        private Path rootDir;
        private Path sourceFile;
        private Target target = Target.BOTH;
        private String className = "com.qin.runtime.generated.App";
        private Path classOutputDir = Path.of("build", "runtime-classes");
        private Path jsOutputFile = Path.of("build", "runtime-web", "app.js");
        private boolean printIr;
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
}
