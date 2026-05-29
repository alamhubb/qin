package com.qin.parser;

import com.subhuti.parser.SubhutiParser;
import com.subhuti.struct.SubhutiCst;
import com.subhuti.struct.SubhutiMatchToken;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinParserFileProbeMain {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("Usage: QinParserFileProbeMain <source-file>");
        }

        Path file = Path.of(args[0]).toAbsolutePath().normalize();
        String source = Files.readString(file, StandardCharsets.UTF_8);

        QinParser parser = SubhutiParser.create(QinParser.class, source);
        parser.cache(true);

        try {
            SubhutiCst cst = parser.Program(QinParser.SourceType.MODULE);
            if (cst == null) {
                cst = parser.getCst();
            }
            printState(file, parser, cst);
        } catch (Exception e) {
            printFailure(file, parser, e);
        }
    }

    private static void printState(Path file, QinParser parser, SubhutiCst cst) {
        SubhutiMatchToken token = parser.curToken();
        System.out.println("file=" + file);
        System.out.println("success=" + !parser.isParserFail());
        System.out.println("index=" + parser.getCurrentIndex());
        System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
        System.out.println("cst=" + (cst == null ? "null" : cst.getName()));
    }

    private static void printFailure(Path file, QinParser parser, Exception e) {
        SubhutiMatchToken token = parser.curToken();
        System.out.println("file=" + file);
        System.out.println("success=false");
        System.out.println("index=" + parser.getCurrentIndex());
        System.out.println("next=" + (token == null ? "null" : token.tokenName() + ":" + token.value()));
        System.out.println("error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace(System.out);
    }
}
