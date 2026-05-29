package com.qin.runtime.core;

import com.qin.parser.QinParserFacade;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class QinParserProbeMain {
    private QinParserProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("Usage: QinParserProbeMain <root>");
        }
        Path root = Path.of(args[0]).toAbsolutePath().normalize();
        try (var paths = Files.walk(root)) {
            for (Path file : paths
                    .filter(Files::isRegularFile)
                    .filter(QinParserProbeMain::isSourceFile)
                    .sorted(Comparator.comparing(path -> path.toString()))
                    .toList()) {
                String source = Files.readString(file, StandardCharsets.UTF_8);
                try {
                    new QinParserFacade().parseSource(source);
                } catch (Throwable error) {
                    System.out.println("FAILED " + file);
                    error.printStackTrace(System.out);
                    return;
                }
            }
        }
        System.out.println("QinParserProbeMain passed.");
    }

    private static boolean isSourceFile(Path file) {
        String name = file.getFileName() == null ? "" : file.getFileName().toString().toLowerCase();
        return name.endsWith(".js") || name.endsWith(".mjs") || name.endsWith(".ts") || name.endsWith(".qin");
    }
}
