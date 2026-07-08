package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QinOvsCompilerBatchProfileProbeMain {
    private QinOvsCompilerBatchProfileProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0])
                : Path.of("");
        root = root.toAbsolutePath().normalize();
        Path sourceRoot = args.length > 1 && !args[1].isBlank() && !args[1].startsWith("--")
                ? root.resolve(args[1]).toAbsolutePath().normalize()
                : root.resolve("app").toAbsolutePath().normalize();
        boolean freshMarker = List.of(args).contains("--fresh-marker");
        boolean profile = List.of(args).contains("--profile");

        Map<Path, String> sources = new LinkedHashMap<>();
        String marker = freshMarker ? "\n/* qin-profile-marker=" + System.nanoTime() + " */\n" : "";
        try (var stream = Files.walk(sourceRoot)) {
            List<Path> files = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".ovs"))
                    .map(path -> path.toAbsolutePath().normalize())
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
            for (Path file : files) {
                sources.put(file, Files.readString(file, StandardCharsets.UTF_8) + marker);
            }
        }

        if (sources.isEmpty()) {
            throw new IllegalStateException("No .ovs files found under " + sourceRoot);
        }

        if (profile) {
            System.setProperty("qin.profile", "true");
        } else {
            System.clearProperty("qin.profile");
        }
        long started = System.nanoTime();
        Map<Path, QinOvsCompiler.QinOvsCompileResult> results = new QinOvsCompiler().compileAll(root, sources);
        long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
        System.out.println("[QinOvsCompilerBatchProfileProbe] modules=" + sources.size()
                + ", results=" + results.size()
                + ", freshMarker=" + freshMarker
                + ", profile=" + profile
                + ", elapsedMs=" + elapsedMs);
    }
}
