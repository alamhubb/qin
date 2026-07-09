package com.qin.parser;

import com.slime.ast.nodes.misc.Program;
import com.subhuti.aop.ByteBuddyParserFactory;
import com.subhuti.parser.SubhutiParser;
import com.subhuti.struct.SubhutiCst;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class QinMinimalParserLayerTimingProbeMain {
    private static final String SOURCE = "export const result = 1\n";

    private QinMinimalParserLayerTimingProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        String mode = args.length == 0 ? "enhanced" : args[0];
        if (args.length >= 2) {
            Path file = Path.of(args[1]).toAbsolutePath().normalize();
            int rounds = args.length >= 3 ? Integer.parseInt(args[2]) : 30;
            runFileBenchmark(mode, file, rounds);
            return;
        }
        runRound(mode, "cold", SOURCE, true);
        runRound(mode, "warm", SOURCE, true);
        System.out.println("QinMinimalParserLayerTimingProbeMain OK");
    }

    private static void runFileBenchmark(String mode, Path file, int rounds) throws Exception {
        String source = Files.readString(file, StandardCharsets.UTF_8);
        List<Double> createMs = new ArrayList<>();
        List<Double> cstMs = new ArrayList<>();
        String route = routeName(mode);
        String stats = "";
        String cacheStats = "";
        String coreStats = "";
        String coreHotRules = "";
        int tokenCount = 0;

        for (int i = 0; i < rounds; i++) {
            RoundResult result = runMeasuredParse(mode, source);
            createMs.add(result.createMs);
            cstMs.add(result.cstMs);
            stats = result.orPredictionStats;
            cacheStats = result.cacheStats;
            coreStats = result.coreStats;
            coreHotRules = result.coreHotRules;
            tokenCount = result.tokenCount;
        }

        Stats create = stats(createMs);
        Stats cst = stats(cstMs);
        System.out.println("QinMinimalParserLayerTimingProbeMain"
                + " file=" + file.getFileName()
                + " mode=" + mode
                + " route=" + route
                + " chars=" + source.length()
                + " tokens=" + tokenCount
                + " createWarmAvgMs=" + format(create.avg)
                + " createBestMs=" + format(create.best)
                + " createColdMs=" + format(create.cold)
                + " cstWarmAvgMs=" + format(cst.avg)
                + " cstBestMs=" + format(cst.best)
                + " cstColdMs=" + format(cst.cold));
        System.out.println("orPredictionStats=" + stats);
        System.out.println("packratStats=" + cacheStats);
        System.out.println("coreStats=" + coreStats);
        System.out.println("coreHotRules=" + coreHotRules);
    }

    private static void runRound(String mode, String label, String source, boolean includeAst) {
        long createStarted = System.nanoTime();
        QinParser parser = createParser(mode, source);
        parser.cache(true);
        long createMs = elapsedMs(createStarted);

        long cstStarted = System.nanoTime();
        SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        long cstMs = elapsedMs(cstStarted);
        if (cst == null) {
            throw new IllegalStateException("Expected CST for " + label);
        }

        long astMs = 0;
        if (includeAst) {
        long astStarted = System.nanoTime();
        Program ast = new QinProgramCstToAst().createProgramAst(cst);
            astMs = elapsedMs(astStarted);
        if (ast == null) {
            throw new IllegalStateException("Expected AST Program for " + label);
        }
        }

        System.out.println("QinMinimalParserLayerTimingProbeMain " + mode + "-" + label
                + " createMs=" + createMs
                + " cstMs=" + cstMs
                + " astMs=" + astMs);
    }

    private static RoundResult runMeasuredParse(String mode, String source) {
        long createStarted = System.nanoTime();
        QinParser parser = createParser(mode, source);
        parser.cache(true);
        boolean recognizer = "recognizer".equals(mode);
        if (recognizer) {
            parser.cst(false);
        }
        double createMs = elapsedDoubleMs(createStarted);

        long cstStarted = System.nanoTime();
        SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        double cstMs = elapsedDoubleMs(cstStarted);
        if (!recognizer && cst == null) {
            throw new IllegalStateException("Expected CST for benchmark");
        }
        String coreStats = parser.getCoreProfileStats();
        return new RoundResult(
                createMs,
                cstMs,
                parsedTokenCount(coreStats, parser.getParsedTokens().size()),
                parser.getOrPredictionStats(),
                parser.getCacheStats(),
                coreStats,
                parser.getCoreHotRuleStats(12));
    }

    private static QinParser createParser(String mode, String source) {
        if ("raw".equals(mode)) {
            return ByteBuddyParserFactory.createRaw(QinParser.class, source);
        }
        if ("static".equals(mode)) {
            return QinParserStaticEnhanced.create(source);
        }
        if ("recognizer".equals(mode)) {
            return QinParserStaticEnhanced.create(source);
        }
        if ("enhanced".equals(mode)) {
            return SubhutiParser.create(QinParser.class, source);
        }
        throw new IllegalArgumentException("Unsupported mode: " + mode);
    }

    private static String routeName(String mode) {
        if ("recognizer".equals(mode)) {
            return "qin-parser-recognizer-only";
        }
        return "qin-parser-cst-only";
    }

    private static int parsedTokenCount(String coreStats, int fallback) {
        String key = "parsedTokenCount=";
        int start = coreStats.indexOf(key);
        if (start < 0) {
            return fallback;
        }
        start += key.length();
        int end = start;
        while (end < coreStats.length() && Character.isDigit(coreStats.charAt(end))) {
            end++;
        }
        return Integer.parseInt(coreStats.substring(start, end));
    }

    private static long elapsedMs(long started) {
        return (System.nanoTime() - started) / 1_000_000L;
    }

    private static double elapsedDoubleMs(long started) {
        return (System.nanoTime() - started) / 1_000_000.0;
    }

    private static Stats stats(List<Double> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("values cannot be empty");
        }
        List<Double> warm = values.size() > 1 ? values.subList(1, values.size()) : values;
        double sum = 0;
        double best = Double.MAX_VALUE;
        for (double value : warm) {
            sum += value;
            best = Math.min(best, value);
        }
        return new Stats(values.get(0), sum / warm.size(), best);
    }

    private static String format(double value) {
        return String.format(java.util.Locale.ROOT, "%.3f", value);
    }

    private record RoundResult(
            double createMs,
            double cstMs,
            int tokenCount,
            String orPredictionStats,
            String cacheStats,
            String coreStats,
            String coreHotRules) {
    }

    private record Stats(double cold, double avg, double best) {
    }
}
