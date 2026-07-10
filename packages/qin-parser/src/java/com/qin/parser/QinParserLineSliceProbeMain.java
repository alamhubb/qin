package com.qin.parser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.lang.reflect.Field;
import java.util.List;

import com.subhuti.struct.SubhutiCst;
import com.subhuti.struct.SubhutiMatchToken;

public final class QinParserLineSliceProbeMain {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 3) {
            throw new IllegalArgumentException("Usage: QinParserLineSliceProbeMain <source-file> <start-line> <end-line> [force-qin]");
        }

        Path file = Path.of(args[0]).toAbsolutePath().normalize();
        int startLine = Integer.parseInt(args[1]);
        int endLine = Integer.parseInt(args[2]);
        boolean forceQin = args.length > 3 && "force-qin".equalsIgnoreCase(args[3]);
        boolean noCache = args.length > 4 && "no-cache".equalsIgnoreCase(args[4]);
        boolean noPrediction = args.length > 5 && "no-prediction".equalsIgnoreCase(args[5]);
        if (startLine <= 0 || endLine < startLine) {
            throw new IllegalArgumentException("Invalid line range: " + startLine + ".." + endLine);
        }

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        int startIndex = Math.max(0, startLine - 1);
        int endIndex = Math.min(lines.size(), endLine);
        if (startIndex >= endIndex) {
            throw new IllegalArgumentException("Line range is outside file: " + startLine + ".." + endLine);
        }

        String source = String.join(System.lineSeparator(), lines.subList(startIndex, endIndex));
        try {
            if (forceQin) {
                probeWithQinParser(file, startLine, endLine, source, noCache, noPrediction);
                return;
            }
            QinParsedSource parsed = new QinParserFacade().parseSource(source);
            System.out.println("file=" + file);
            System.out.println("range=" + startLine + ".." + endLine);
            System.out.println("success=true");
            System.out.println("program=" + (parsed.programAst() == null ? "null" : parsed.programAst().getClass().getSimpleName()));
        } catch (Exception e) {
            System.out.println("file=" + file);
            System.out.println("range=" + startLine + ".." + endLine);
            System.out.println("success=false");
            System.out.println("error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void probeWithQinParser(
            Path file,
            int startLine,
            int endLine,
            String source,
            boolean noCache,
            boolean noPrediction) {
        QinParser parser = QinParserStaticEnhanced.create(source);
        parser.cache(!noCache);
        if (noPrediction) {
            disableOrPrediction(parser);
        }
        try {
            SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
            if (cst == null) {
                cst = parser.getCst();
            }
            SubhutiMatchToken token = parser.curToken();
            System.out.println("file=" + file);
            System.out.println("range=" + startLine + ".." + endLine);
            System.out.println("success=" + !parser.isParserFail());
            System.out.println("mode=force-qin");
            System.out.println("cache=" + !noCache);
            System.out.println("prediction=" + !noPrediction);
            System.out.println("index=" + parser.getCurrentIndex());
            System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
            System.out.println("orPredictionStats=" + parser.getOrPredictionStats());
            System.out.println("orPredictionGrammar=" + parser.getLastOrPredictionGrammar());
            System.out.println("cst=" + (cst == null ? "null" : cst.getName()));
        } catch (Exception e) {
            SubhutiMatchToken token = parser.curToken();
            System.out.println("file=" + file);
            System.out.println("range=" + startLine + ".." + endLine);
            System.out.println("success=false");
            System.out.println("mode=force-qin");
            System.out.println("cache=" + !noCache);
            System.out.println("prediction=" + !noPrediction);
            System.out.println("index=" + parser.getCurrentIndex());
            System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
            System.out.println("orPredictionStats=" + parser.getOrPredictionStats());
            System.out.println("orPredictionGrammar=" + parser.getLastOrPredictionGrammar());
            System.out.println("error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void disableOrPrediction(QinParser parser) {
        try {
            Field field = com.subhuti.parser.SubhutiParserState.class.getDeclaredField("enableOrPrediction");
            field.setAccessible(true);
            field.setBoolean(parser, false);
        } catch (ReflectiveOperationException error) {
            throw new IllegalStateException("Failed to disable Subhuti OR prediction", error);
        }
    }
}
