package com.qin.parser;

import com.slime.ast.nodes.misc.Program;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Batch parser probe used by generated TypeScript parser parity tests.
 */
public final class QinParserBatchProbeMain {
    private QinParserBatchProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("Usage: QinParserBatchProbeMain <Program|QinModule> <source-file>... | --dir <source-dir>");
        }

        String rule = args[0];
        if (!"Program".equals(rule) && !"QinModule".equals(rule)) {
            throw new IllegalArgumentException(
                    "Unsupported packaged probe rule: " + rule
                            + ". Batch probes intentionally support only Program/QinModule via QinParserFacade.");
        }

        QinParserFacade facade = new QinParserFacade();
        List<Path> sourcePaths = sourcePaths(args);
        System.out.println("rule=" + rule);
        for (int i = 0; i < sourcePaths.size(); i++) {
            Path sourcePath = sourcePaths.get(i);
            String source = Files.readString(sourcePath, StandardCharsets.UTF_8);
            System.out.println("case=" + i + "\tstart=true\tfile=" + sourcePath);
            try {
                Program program = facade.parseProgram(source);
                System.out.println("case=" + i
                        + "\tsuccess=true"
                        + "\tfile=" + sourcePath
                        + "\tprogram=" + (program == null ? "null" : program.getClass().getName()));
            } catch (Throwable e) {
                String message = e.getClass().getSimpleName() + ": " + e.getMessage();
                System.out.println("case=" + i
                        + "\tsuccess=false"
                        + "\tfile=" + sourcePath
                        + "\terror=" + sanitize(message));
            }
        }
    }

    private static List<Path> sourcePaths(String[] args) throws Exception {
        if (args.length == 3 && "--dir".equals(args[1])) {
            Path root = Path.of(args[2]).toAbsolutePath().normalize();
            try (Stream<Path> stream = Files.walk(root)) {
                return stream
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".ts"))
                        .sorted(Comparator.comparing(Path::toString))
                        .toList();
            }
        }
        List<Path> paths = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            paths.add(Path.of(args[i]).toAbsolutePath().normalize());
        }
        return paths;
    }

    private static String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
    }
}
