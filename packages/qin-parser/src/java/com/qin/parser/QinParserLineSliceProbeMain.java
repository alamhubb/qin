package com.qin.parser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.subhuti.parser.SubhutiParser;
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
                probeWithQinParser(file, startLine, endLine, source, noCache);
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

    private static void probeWithQinParser(Path file, int startLine, int endLine, String source, boolean noCache) {
        QinParser parser = SubhutiParser.create(QinParser.class, source);
        parser.cache(!noCache);
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
            System.out.println("index=" + parser.getCurrentIndex());
            System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
            System.out.println("cst=" + (cst == null ? "null" : cst.getName()));
        } catch (Exception e) {
            SubhutiMatchToken token = parser.curToken();
            System.out.println("file=" + file);
            System.out.println("range=" + startLine + ".." + endLine);
            System.out.println("success=false");
            System.out.println("mode=force-qin");
            System.out.println("cache=" + !noCache);
            System.out.println("index=" + parser.getCurrentIndex());
            System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
            System.out.println("error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }
}
