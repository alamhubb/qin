package com.qin.runtime.core;

import com.qin.parser.QinParser;
import com.qin.parser.QinParserStaticEnhanced;
import com.qin.parser.QinParserFacade;
import com.slime.ast.nodes.misc.Program;
import com.subhuti.struct.SubhutiCst;
import com.subhuti.struct.SubhutiMatchToken;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Compares the same TS/ESM input across Qin's current parser/runtime routes.
 */
public final class QinParserRouteComparisonProbeMain {
    private static final int ROUNDS = 3;

    private QinParserRouteComparisonProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path workspaceRoot = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : discoverWorkspaceRoot();
        List<CaseInput> cases = List.of(
                new CaseInput(
                        "minimal-export",
                        "MinimalExport",
                        "export const result = 1\n",
                        1.0d),
                new CaseInput(
                        "object-literal-member",
                        "ObjectLiteralMember",
                        """
                                const item = { label: "qin", value: 2 }
                                export const result = item.value
                                """,
                        2.0d),
                new CaseInput(
                        "class-constructor-method",
                        "ClassConstructorMethod",
                        """
                                class Counter {
                                  value: number
                                  constructor(value: number) {
                                    this.value = value
                                  }
                                  next(): number {
                                    return this.value + 1
                                  }
                                }
                                export const result = new Counter(4).next()
                                """,
                        5.0d));

        for (CaseInput input : cases) {
            runJavaNative(input);
        }
        runJavaToTsNode(workspaceRoot, cases);
        runSnapshotNode(workspaceRoot, cases);
        runTsToClass(cases);
        System.out.println("QinParserRouteComparisonProbeMain OK");
    }

    private static void runJavaNative(CaseInput input) {
        List<JavaNativeRound> rounds = new ArrayList<>();
        for (int round = 0; round < ROUNDS; round++) {
            long createStarted = System.nanoTime();
            QinParser parser = QinParserStaticEnhanced.create(input.source());
            parser.cache(true);
            long createMs = elapsedMs(createStarted);

            long cstStarted = System.nanoTime();
            SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
            if (cst == null) {
                cst = parser.getCst();
            }
            long cstMs = elapsedMs(cstStarted);
            SubhutiMatchToken token = parser.getCurToken();
            if (cst == null || parser.isParserFail() || !isFullyConsumed(parser, token)) {
                throw new IllegalStateException("java-native parse failed for case " + input.name()
                        + " cst=" + (cst == null ? "null" : cst.getName())
                        + " parserFail=" + parser.isParserFail()
                        + " currentIndex=" + parser.getCurrentIndex()
                        + " sourceLength=" + parser.getSourceCode().length()
                        + " token=" + (token == null ? "null" : token.toShortString()));
            }

            long astStarted = System.nanoTime();
            Program ast = new QinParserFacade().createProgramAst(cst);
            long astMs = elapsedMs(astStarted);
            if (ast == null) {
                throw new IllegalStateException("java-native AST failed for case " + input.name());
            }
            rounds.add(new JavaNativeRound(createMs, cstMs, astMs));
        }

        JavaNativeRound cold = rounds.get(0);
        List<JavaNativeRound> warm = rounds.subList(1, rounds.size());
        System.out.println("case=" + input.name()
                + " route=java-native"
                + " coldCreateMs=" + cold.createMs()
                + " coldCstMs=" + cold.cstMs()
                + " coldAstMs=" + cold.astMs()
                + " warmCreateAvgMs=" + avgLong(warm.stream().mapToLong(JavaNativeRound::createMs).toArray())
                + " warmCstAvgMs=" + avgLong(warm.stream().mapToLong(JavaNativeRound::cstMs).toArray())
                + " warmAstAvgMs=" + avgLong(warm.stream().mapToLong(JavaNativeRound::astMs).toArray()));
    }

    private static void runJavaToTsNode(Path workspaceRoot, List<CaseInput> cases) throws Exception {
        Path generatedRoot = workspaceRoot.resolve("qin/packages/qin-language/generated/qin-parser-ts");
        if (!Files.isRegularFile(generatedRoot.resolve("index.ts"))) {
            throw new IllegalStateException("Missing generated Qin parser TS package: " + generatedRoot);
        }
        Path tsx = workspaceRoot.resolve("ovsjs/ovs/ovs-compiler/node_modules/tsx/dist/cli.mjs");
        if (!Files.isRegularFile(tsx)) {
            throw new IllegalStateException("Missing tsx runner: " + tsx);
        }

        List<Map<String, Object>> payload = new ArrayList<>();
        for (CaseInput input : cases) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", input.name());
            item.put("source", input.source());
            payload.add(item);
        }
        String script = """
                import QinParser, { com_slime_parser_SlimeJavascriptParser$SourceType as SourceType } from %s;
                const cases = %s;
                const rounds = %d;
                function average(values) {
                  let sum = 0;
                  for (const value of values) sum += value;
                  return sum / values.length;
                }
                for (const item of cases) {
                  const times = [];
                  for (let round = 0; round < rounds; round++) {
                    const started = performance.now();
                    const parser = new QinParser(item.source);
                    parser.cache(true);
                    const cst = parser.Program(SourceType.__qin_field_MODULE);
                    const elapsed = performance.now() - started;
                    if (!cst || parser.parserFail || !parser.isEof()) {
                      throw new Error('java-to-ts-node parse failed for case ' + item.name);
                    }
                    times.push(elapsed);
                  }
                  const warm = times.slice(1);
                  console.log('case=' + item.name
                    + ' route=java-to-ts-node'
                    + ' coldMs=' + times[0].toFixed(3)
                    + ' warmAvgMs=' + average(warm).toFixed(3)
                    + ' bestMs=' + Math.min(...times).toFixed(3));
                }
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(generatedRoot.resolve("index.ts").toUri().toString()),
                QinJsPackageRunner.renderJsLiteral(payload),
                ROUNDS);

        ScriptResult result = runTsxScript(tsx, generatedRoot, script, "java-to-ts-node", 30);
        if (result.timedOut()) {
            throw new IllegalStateException("java-to-ts-node route timed out after 30s");
        }
        List<String> lines = result.lines();
        if (result.exitCode() != 0) {
            throw new IllegalStateException("java-to-ts-node route failed: " + String.join("\n", lines));
        }
        long routeLines = lines.stream().filter(line -> line.contains(" route=java-to-ts-node")).count();
        if (routeLines != cases.size()) {
            throw new IllegalStateException("java-to-ts-node route produced " + routeLines
                    + " timing lines for " + cases.size() + " cases: " + String.join("\n", lines));
        }
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static void runSnapshotNode(Path workspaceRoot, List<CaseInput> cases) throws Exception {
        Path snapshotRoot = workspaceRoot.resolve("parser-source-snapshot");
        Path snapshotParser = snapshotRoot.resolve("slimeparser/src/deprecated/SlimeJavascriptParser.ts");
        if (!Files.isRegularFile(snapshotParser)) {
            throw new IllegalStateException("Missing parser-source-snapshot Slime parser: " + snapshotParser);
        }
        Path tsx = workspaceRoot.resolve("ovsjs/ovs/ovs-compiler/node_modules/tsx/dist/cli.mjs");
        if (!Files.isRegularFile(tsx)) {
            throw new IllegalStateException("Missing tsx runner: " + tsx);
        }

        for (CaseInput input : cases) {
            runSnapshotNodeCase(snapshotRoot, snapshotParser, tsx, input);
        }
    }

    private static void runSnapshotNodeCase(
            Path snapshotRoot,
            Path snapshotParser,
            Path tsx,
            CaseInput input) throws Exception {
        List<Map<String, Object>> payload = new ArrayList<>();
        {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", input.name());
            item.put("source", input.source());
            payload.add(item);
        }
        String script = """
                import SlimeJavascriptParser from %s;
                const cases = %s;
                const rounds = %d;
                function average(values) {
                  let sum = 0;
                  for (const value of values) sum += value;
                  return sum / values.length;
                }
                for (const item of cases) {
                  const times = [];
                  for (let round = 0; round < rounds; round++) {
                    const started = performance.now();
                    const parser = new SlimeJavascriptParser(item.source);
                    parser.cache(true);
                    const cst = parser.Program('module');
                    const elapsed = performance.now() - started;
                    if (!cst || parser.parserFail || !parser.isEof) {
                      throw new Error('snapshot-node parse failed for case ' + item.name);
                    }
                    times.push(elapsed);
                  }
                  const warm = times.slice(1);
                  console.log('case=' + item.name
                    + ' route=snapshot-node'
                    + ' coldMs=' + times[0].toFixed(3)
                    + ' warmAvgMs=' + average(warm).toFixed(3)
                    + ' bestMs=' + Math.min(...times).toFixed(3));
                }
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(snapshotParser.toUri().toString()),
                QinJsPackageRunner.renderJsLiteral(payload),
                ROUNDS);

        ScriptResult result = runTsxScript(tsx, snapshotRoot, script, "snapshot-node", 10);
        if (result.timedOut()) {
            System.out.println("case=" + input.name() + " route=snapshot-node status=timeout timeoutSeconds=10");
            return;
        }
        List<String> lines = result.lines();
        if (result.exitCode() != 0) {
            throw new IllegalStateException("snapshot-node route failed: " + String.join("\n", lines));
        }
        long routeLines = lines.stream().filter(line -> line.contains(" route=snapshot-node")).count();
        if (routeLines != 1) {
            throw new IllegalStateException("snapshot-node route produced " + routeLines
                    + " timing lines for " + input.name() + ": " + String.join("\n", lines));
        }
        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static ScriptResult runTsxScript(
            Path tsx,
            Path workingDirectory,
            String script,
            String label,
            int timeoutSeconds) throws Exception {
        Path scriptFile = Files.createTempFile("qin-" + label + "-", ".ts");
        Path outputFile = Files.createTempFile("qin-" + label + "-", ".log");
        Files.writeString(scriptFile, script, StandardCharsets.UTF_8);
        ProcessBuilder builder = new ProcessBuilder(nodeCommand(), tsx.toString(), scriptFile.toString());
        builder.directory(workingDirectory.toFile());
        builder.redirectErrorStream(true);
        builder.redirectOutput(outputFile.toFile());
        Process process = builder.start();
        boolean finished;
        try {
            finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
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

    private static String nodeCommand() {
        Path bundledNode = Path.of("D:/devlang/nodejs/node.exe");
        if (Files.isRegularFile(bundledNode)) {
            return bundledNode.toString();
        }
        return "node";
    }

    private static void deleteIfExistsQuietly(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (Exception ignored) {
            // Temporary diagnostic files must not mask the benchmark result.
        }
    }

    private static List<String> readLinesIfExists(Path file) throws Exception {
        if (!Files.isRegularFile(file)) {
            return List.of();
        }
        return Files.readAllLines(file, StandardCharsets.UTF_8);
    }

    private static boolean isFullyConsumed(QinParser parser, SubhutiMatchToken token) {
        if (token != null) {
            return token.isEof();
        }
        return parser.getSourceCode().substring(parser.getCurrentIndex()).isBlank();
    }

    private static void runTsToClass(List<CaseInput> cases) throws Exception {
        Path root = Files.createTempDirectory("qin-route-comparison-");
        QinInMemoryJvmRunner runner = new QinInMemoryJvmRunner();
        for (CaseInput input : cases) {
            Path caseRoot = root.resolve(input.javaName());
            Files.createDirectories(caseRoot);
            Path entry = caseRoot.resolve("entry.ts");
            Files.writeString(entry, input.source(), StandardCharsets.UTF_8);

            long coldStarted = System.nanoTime();
            Object cold = runner.compileAndRunModuleClasses(
                    entry,
                    caseRoot,
                    "probe.QinParserRouteComparison" + input.javaName() + "Cold");
            long coldMs = elapsedMs(coldStarted);
            assertExpected(input, cold, "cold");

            long warmStarted = System.nanoTime();
            Object warm = runner.compileAndRunModuleClasses(
                    entry,
                    caseRoot,
                    "probe.QinParserRouteComparison" + input.javaName() + "Warm");
            long warmMs = elapsedMs(warmStarted);
            assertExpected(input, warm, "warm-no-cache");

            long cacheStarted = System.nanoTime();
            Object cache = runner.compileAndRunModuleClasses(
                    entry,
                    caseRoot,
                    "probe.QinParserRouteComparison" + input.javaName() + "Warm");
            long cacheMs = elapsedMs(cacheStarted);
            assertExpected(input, cache, "cache-hit");

            System.out.println("case=" + input.name()
                    + " route=ts-to-class"
                    + " coldMs=" + coldMs
                    + " warmNoCacheMs=" + warmMs
                    + " cacheHitMs=" + cacheMs);
        }
    }

    private static void assertExpected(CaseInput input, Object value, String label) {
        if (!(value instanceof Number number)
                || Math.abs(number.doubleValue() - input.expectedResult()) > 0.000_001d) {
            throw new IllegalStateException("Expected " + input.expectedResult()
                    + " for " + input.name() + " " + label + ", got " + value);
        }
    }

    private static Path discoverWorkspaceRoot() {
        Path cursor = Path.of("").toAbsolutePath().normalize();
        while (cursor != null) {
            if (Files.isDirectory(cursor.resolve("qin"))
                    && Files.isDirectory(cursor.resolve("slime"))
                    && Files.isDirectory(cursor.resolve("parser-source-snapshot"))) {
                return cursor;
            }
            cursor = cursor.getParent();
        }
        return Path.of("D:/project/qkyproject/qinall").toAbsolutePath().normalize();
    }

    private static long elapsedMs(long started) {
        return (System.nanoTime() - started) / 1_000_000L;
    }

    private static String avgLong(long[] values) {
        if (values.length == 0) {
            return "0.000";
        }
        long sum = 0;
        for (long value : values) {
            sum += value;
        }
        return String.format(java.util.Locale.ROOT, "%.3f", (double) sum / values.length);
    }

    private record CaseInput(String name, String javaName, String source, double expectedResult) {
    }

    private record JavaNativeRound(long createMs, long cstMs, long astMs) {
    }

    private record ScriptResult(int exitCode, boolean timedOut, List<String> lines) {
    }
}
