package com.qin.parser;

import com.subhuti.parser.SubhutiParser;
import com.subhuti.struct.SubhutiCst;
import com.subhuti.struct.SubhutiMatchToken;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinParserPrefixProbeMain {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Usage: QinParserPrefixProbeMain <source-file> <end-line>");
        }

        Path file = Path.of(args[0]).toAbsolutePath().normalize();
        int endLine = Integer.parseInt(args[1]);
        if (endLine <= 0) {
            throw new IllegalArgumentException("Invalid end line: " + endLine);
        }

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        int endIndex = Math.min(lines.size(), endLine);
        String source = String.join(System.lineSeparator(), lines.subList(0, endIndex));

        QinParser parser = SubhutiParser.create(QinParser.class, source);
        parser.cache(true);
        try {
            SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
            if (cst == null) {
                cst = parser.getCst();
            }
            printState(file, endLine, parser, cst);
        } catch (Exception e) {
            printFailure(file, endLine, parser, e);
        }
    }

    private static void printState(Path file, int endLine, QinParser parser, SubhutiCst cst) {
        SubhutiMatchToken token = parser.curToken();
        System.out.println("file=" + file);
        System.out.println("prefix=1.." + endLine);
        System.out.println("success=" + !parser.isParserFail());
        System.out.println("index=" + parser.getCurrentIndex());
        System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
        System.out.println("cst=" + (cst == null ? "null" : cst.getName()));
    }

    private static void printFailure(Path file, int endLine, QinParser parser, Exception e) {
        SubhutiMatchToken token = parser.curToken();
        System.out.println("file=" + file);
        System.out.println("prefix=1.." + endLine);
        System.out.println("success=false");
        System.out.println("index=" + parser.getCurrentIndex());
        System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
        System.out.println("error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace(System.out);
    }
}
