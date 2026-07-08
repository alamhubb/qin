package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Compares the smallest CSSTS input across parser/compiler execution routes.
 */
public final class QinCsstsRouteComparisonProbeMain {
    private static final int TIMEOUT_SECONDS = 10;
    private static final CaseInput ONE_ATOM = new CaseInput(
            "one-atom",
            "const s = css { colorRed }\n");

    private QinCsstsRouteComparisonProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && "--qin-compiler-child".equals(args[0])) {
            runQinCompilerChild(args);
            return;
        }
        if (args.length > 0 && "--ts-class-child".equals(args[0])) {
            runTsToClassChild(args);
            return;
        }

        Path workspaceRoot = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : discoverWorkspaceRoot();
        runSnapshotNode(workspaceRoot, ONE_ATOM);
        runChildRoute("qin-js-on-jvm-cssts-compiler", "--qin-compiler-child", ONE_ATOM, workspaceRoot);
        runChildRoute("ts-to-class", "--ts-class-child", ONE_ATOM, workspaceRoot);
        System.out.println("QinCsstsRouteComparisonProbeMain OK");
    }

    private static void runSnapshotNode(Path workspaceRoot, CaseInput input) throws Exception {
        Path snapshotRoot = workspaceRoot.resolve("parser-source-snapshot");
        Path snapshotParser = snapshotRoot.resolve("csstsparser/src/parser/CssTsParser.ts");
        Path tsx = workspaceRoot.resolve("ovsjs/ovs/ovs-compiler/node_modules/tsx/dist/cli.mjs");
        if (!Files.isRegularFile(snapshotParser)) {
            throw new IllegalStateException("Missing parser-source-snapshot CSSTS parser: " + snapshotParser);
        }
        if (!Files.isRegularFile(tsx)) {
            throw new IllegalStateException("Missing tsx runner: " + tsx);
        }

        String script = """
                import CssTsParser from %s;
                const source = %s;
                const started = performance.now();
                const parser = new CssTsParser(source);
                parser.cache(true);
                const cst = parser.Program();
                const elapsed = performance.now() - started;
                if (!cst || parser.parserFail || !parser.isEof) {
                  throw new Error('snapshot CSSTS parse failed');
                }
                console.log('case=%s route=snapshot-node stage=parse coldMs=' + elapsed.toFixed(3));
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(snapshotParser.toUri().toString()),
                QinJsPackageRunner.renderJsLiteral(input.source()),
                input.name());
        ScriptResult result = runNodeScript(tsx, snapshotRoot, script, "cssts-snapshot");
        if (result.timedOut()) {
            System.out.println("case=" + input.name()
                    + " route=snapshot-node stage=parse status=timeout timeoutSeconds=" + TIMEOUT_SECONDS);
            return;
        }
        if (result.exitCode() != 0) {
            System.out.println("case=" + input.name()
                    + " route=snapshot-node stage=parse status=failed detail="
                    + quoteDetail(String.join(" | ", result.lines())));
            return;
        }
        result.lines().forEach(System.out::println);
    }

    private static ScriptResult runNodeScript(Path tsx, Path workingDirectory, String script, String label) throws Exception {
        Path scriptFile = Files.createTempFile("qin-" + label + "-", ".ts");
        Path outputFile = Files.createTempFile("qin-" + label + "-", ".log");
        Files.writeString(scriptFile, script, StandardCharsets.UTF_8);
        ProcessBuilder builder = new ProcessBuilder(nodeCommand(), tsx.toString(), scriptFile.toString());
        builder.directory(workingDirectory.toFile());
        builder.redirectErrorStream(true);
        builder.redirectOutput(outputFile.toFile());
        Process process = builder.start();
        try {
            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                process.waitFor(5, TimeUnit.SECONDS);
                return new ScriptResult(-1, true, readLinesIfExists(outputFile));
            }
            return new ScriptResult(process.exitValue(), false, readLinesIfExists(outputFile));
        } finally {
            deleteIfExistsQuietly(scriptFile);
            deleteIfExistsQuietly(outputFile);
        }
    }

    private static void runChildRoute(String route, String childMode, CaseInput input, Path workspaceRoot) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-route-comparison-");
        Path sourceFile = root.resolve(input.name() + ".cssts");
        Files.writeString(sourceFile, input.source(), StandardCharsets.UTF_8);

        ProcessBuilder builder = new ProcessBuilder(
                javaCommand(),
                "-cp",
                System.getProperty("java.class.path"),
                QinCsstsRouteComparisonProbeMain.class.getName(),
                childMode,
                input.name(),
                sourceFile.toString(),
                workspaceRoot.toString());
        builder.redirectErrorStream(true);
        Path outputFile = Files.createTempFile("qin-cssts-child-", ".log");
        builder.redirectOutput(outputFile.toFile());
        Process process = builder.start();
        try {
            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                process.waitFor(5, TimeUnit.SECONDS);
                System.out.println("case=" + input.name()
                        + " route=" + route
                        + " status=timeout timeoutSeconds=" + TIMEOUT_SECONDS);
                return;
            }
            List<String> lines = readLinesIfExists(outputFile);
            if (process.exitValue() != 0) {
                System.out.println("case=" + input.name()
                        + " route=" + route
                        + " status=failed detail=" + quoteDetail(String.join(" | ", lines)));
                return;
            }
            List<String> routeLines = lines.stream()
                    .filter(line -> line.startsWith("case="))
                    .toList();
            if (routeLines.isEmpty()) {
                System.out.println("case=" + input.name()
                        + " route=" + route
                        + " status=failed detail=" + quoteDetail(String.join(" | ", lines)));
                return;
            }
            routeLines.forEach(System.out::println);
        } finally {
            deleteIfExistsQuietly(outputFile);
            deleteIfExistsQuietly(sourceFile);
            deleteIfExistsQuietly(root.resolve("qin.config.js"));
            deleteIfExistsQuietly(root);
        }
    }

    private static void runQinCompilerChild(String[] args) throws Exception {
        String name = args[1];
        Path sourceFile = Path.of(args[2]).toAbsolutePath().normalize();
        Path projectRoot = sourceFile.getParent();
        Files.writeString(projectRoot.resolve("qin.config.js"), "{ \"name\": \"qin-cssts-route-probe\" }\n", StandardCharsets.UTF_8);
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8);
        QinCsstsCompiler compiler = new QinCsstsCompiler();

        long coldStarted = System.nanoTime();
        QinCsstsCompiler.QinCsstsCompileResult cold = compiler.compile(projectRoot, source);
        long coldMs = elapsedMs(coldStarted);

        long warmStarted = System.nanoTime();
        QinCsstsCompiler.QinCsstsCompileResult warm = compiler.compile(projectRoot, source);
        long warmMs = elapsedMs(warmStarted);

        System.out.println("case=" + name
                + " route=qin-js-on-jvm-cssts-compiler"
                + " stage=transform"
                + " coldMs=" + coldMs
                + " warmMs=" + warmMs
                + " codeBytes=" + cold.code().getBytes(StandardCharsets.UTF_8).length
                + " cssBytes=" + warm.css().getBytes(StandardCharsets.UTF_8).length);
    }

    private static void runTsToClassChild(String[] args) throws Exception {
        String name = args[1];
        Path sourceFile = Path.of(args[2]).toAbsolutePath().normalize();
        Path projectRoot = sourceFile.getParent();
        String source = Files.readString(sourceFile, StandardCharsets.UTF_8)
                + "\nexport const result = 1\n";
        Path entry = projectRoot.resolve("entry.ts");
        Files.writeString(entry, source, StandardCharsets.UTF_8);
        try {
            long started = System.nanoTime();
            Object value = new QinInMemoryJvmRunner().compileAndRunModuleClasses(
                    entry,
                    projectRoot,
                    "probe.QinCsstsRouteComparison" + sanitizeJavaName(name));
            System.out.println("case=" + name
                    + " route=ts-to-class stage=module-class coldMs=" + elapsedMs(started)
                    + " status=ok value=" + value);
        } catch (Throwable error) {
            System.out.println("case=" + name
                    + " route=ts-to-class stage=module-class status=failed detail="
                    + quoteDetail(error.getClass().getSimpleName() + ": " + error.getMessage()));
        } finally {
            deleteIfExistsQuietly(entry);
        }
    }

    private static Path discoverWorkspaceRoot() {
        Path cursor = Path.of("").toAbsolutePath().normalize();
        while (cursor != null) {
            if (Files.isDirectory(cursor.resolve("qin"))
                    && Files.isDirectory(cursor.resolve("parser-source-snapshot"))) {
                return cursor;
            }
            cursor = cursor.getParent();
        }
        return Path.of("D:/project/qkyproject/qinall").toAbsolutePath().normalize();
    }

    private static String nodeCommand() {
        Path bundledNode = Path.of("D:/devlang/nodejs/node.exe");
        return Files.isRegularFile(bundledNode) ? bundledNode.toString() : "node";
    }

    private static String javaCommand() {
        Path java = Path.of(System.getProperty("java.home"), "bin", isWindows() ? "java.exe" : "java");
        return Files.isRegularFile(java) ? java.toString() : "java";
    }

    private static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase(java.util.Locale.ROOT).contains("win");
    }

    private static List<String> readLinesIfExists(Path file) throws Exception {
        if (!Files.isRegularFile(file)) {
            return List.of();
        }
        return Files.readAllLines(file, StandardCharsets.UTF_8);
    }

    private static void deleteIfExistsQuietly(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (Exception ignored) {
            // Temporary diagnostic files must not mask the benchmark result.
        }
    }

    private static long elapsedMs(long started) {
        return (System.nanoTime() - started) / 1_000_000L;
    }

    private static String sanitizeJavaName(String text) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            out.append(Character.isLetterOrDigit(ch) ? ch : '_');
        }
        if (out.isEmpty() || !Character.isJavaIdentifierStart(out.charAt(0))) {
            out.insert(0, '_');
        }
        return out.toString();
    }

    private static String quoteDetail(String text) {
        if (text == null || text.isBlank()) {
            return "\"\"";
        }
        String compact = text.replace('\r', ' ').replace('\n', ' ').trim();
        if (compact.length() > 240) {
            compact = compact.substring(0, 240) + "...";
        }
        return "\"" + compact
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                + "\"";
    }

    private record CaseInput(String name, String source) {
    }

    private record ScriptResult(int exitCode, boolean timedOut, List<String> lines) {
    }
}
