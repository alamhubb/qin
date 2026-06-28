package com.qin.parser;

import com.slime.ast.nodes.misc.Program;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Batch parser probe used by generated TypeScript parser parity tests.
 */
public final class QinParserBatchProbeMain {
    private QinParserBatchProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Usage: QinParserBatchProbeMain <Program|QinModule> <source-file>...");
        }

        String rule = args[0];
        if (!"Program".equals(rule) && !"QinModule".equals(rule)) {
            throw new IllegalArgumentException(
                    "Unsupported packaged probe rule: " + rule
                            + ". Batch probes intentionally support only Program/QinModule via QinParserFacade.");
        }

        QinParserFacade facade = new QinParserFacade();
        System.out.println("rule=" + rule);
        for (int i = 1; i < args.length; i++) {
            Path sourcePath = Path.of(args[i]);
            String source = Files.readString(sourcePath, StandardCharsets.UTF_8);
            try {
                Program program = facade.parseProgram(source);
                System.out.println("case=" + (i - 1)
                        + "\tsuccess=true"
                        + "\tfile=" + sourcePath.getFileName()
                        + "\tprogram=" + (program == null ? "null" : program.getClass().getName()));
            } catch (Exception e) {
                String message = e.getClass().getSimpleName() + ": " + e.getMessage();
                System.out.println("case=" + (i - 1)
                        + "\tsuccess=false"
                        + "\tfile=" + sourcePath.getFileName()
                        + "\terror=" + sanitize(message));
            }
        }
    }

    private static String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
    }
}
