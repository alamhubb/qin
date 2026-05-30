package com.qin.parser;

import com.slime.ast.nodes.misc.Program;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Packaged parser smoke/probe entry.
 *
 * <p>This class intentionally uses the stable Qin parser facade. Low-level
 * grammar-rule probes depend on Slime internals and belong in parser-dev
 * workspaces, not in the production package compile graph.
 */
public final class QinParserRuleProbeMain {
    private QinParserRuleProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Usage: QinParserRuleProbeMain <Program|QinModule> <source>|--file <path>");
        }

        String rule = args[0];
        String source;
        if ("--file".equals(args[1])) {
            if (args.length < 3) {
                throw new IllegalArgumentException("Usage: QinParserRuleProbeMain <Program|QinModule> --file <path>");
            }
            source = Files.readString(Path.of(args[2]), StandardCharsets.UTF_8);
        } else {
            source = args[1];
        }

        try {
            Program program = parseSupportedRule(rule, source);
            printState(rule, source, program);
        } catch (Exception e) {
            printFailure(rule, source, e);
        }
    }

    private static Program parseSupportedRule(String rule, String source) {
        if (!"Program".equals(rule) && !"QinModule".equals(rule)) {
            throw new IllegalArgumentException(
                    "Unsupported packaged probe rule: " + rule
                            + ". Packaged probes intentionally support only Program/QinModule via QinParserFacade.");
        }
        return new QinParserFacade().parseProgram(source);
    }

    private static void printState(String rule, String source, Program program) {
        System.out.println("rule=" + rule);
        System.out.println("success=true");
        System.out.println("sourceLength=" + (source == null ? 0 : source.length()));
        System.out.println("program=" + (program == null ? "null" : program.getClass().getName()));
    }

    private static void printFailure(String rule, String source, Exception e) {
        System.out.println("rule=" + rule);
        System.out.println("success=false");
        System.out.println("sourceLength=" + (source == null ? 0 : source.length()));
        System.out.println("error=" + e.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace(System.out);
    }
}
