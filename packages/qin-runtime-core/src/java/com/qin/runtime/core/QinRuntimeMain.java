package com.qin.runtime.core;

import java.nio.file.Path;

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

        QinBuildRequest request = toBuildRequest(options);
        QinBuildCoordinator coordinator = new QinBuildCoordinator();
        QinBuildResult result = coordinator.build(request);

        if (options.printIr) {
            System.out.println("IR declarations: " + result.program().declarations().size());
            System.out.println("IR console logs: " + result.program().consoleLogs().size());
            System.out.println("IR java imports: " + result.program().javaImports().size());
            System.out.println("IR js imports: " + result.program().jsImports().size());
            System.out.println("IR java static console logs: " + result.program().javaStaticConsoleLogs().size());
            System.out.println("IR java instance method calls: " + result.program().javaInstanceMethodCalls().size());
            System.out.println("IR java instance console logs: " + result.program().javaInstanceConsoleLogs().size());
        }

        if (result.classFile() != null) {
            System.out.println("Generated .class: " + result.classFile().toAbsolutePath());
        }
        if (result.jsFile() != null) {
            System.out.println("Generated .js: " + result.jsFile().toAbsolutePath());
        }
        System.out.println("Project root: " + result.layout().root().toAbsolutePath());
        System.out.println("Source file: " + result.sourceFile().toAbsolutePath());
        System.out.println("Layout shared: " + result.layout().sharedDir().toAbsolutePath());
        System.out.println("Layout app: " + result.layout().appDir().toAbsolutePath());
        System.out.println("Layout backend entry: " + result.layout().backendEntry().toAbsolutePath());
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
                case "--target" -> options.target = QinBuildTarget.parse(nextValue(args, ++i, "--target"));
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

    private static QinBuildRequest toBuildRequest(Options options) {
        Path root = options.rootDir != null
                ? options.rootDir.toAbsolutePath().normalize()
                : Path.of("").toAbsolutePath().normalize();
        return new QinBuildRequest(
                root,
                options.sourceFile,
                options.target,
                options.className,
                options.classOutputDir,
                options.jsOutputFile,
                options.printIr);
    }

    private static final class Options {
        private Path rootDir;
        private Path sourceFile;
        private QinBuildTarget target = QinBuildTarget.BOTH;
        private String className = "com.qin.runtime.generated.App";
        private Path classOutputDir = Path.of("build", "runtime-classes");
        private Path jsOutputFile = Path.of("build", "runtime-web", "app.js");
        private boolean printIr;
        private boolean showHelp;
    }
}
