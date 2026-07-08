package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinParserJavaToTsGenerationProbeMain {
    private QinParserJavaToTsGenerationProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path workspaceRoot = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("D:/project/qkyproject/qinall").toAbsolutePath().normalize();
        Path outputRoot = Files.createTempDirectory("qin-parser-ts-gen-");
        List<Path> sourceRoots = List.of(
                workspaceRoot.resolve("qin/packages/qin-parser/src/java"),
                workspaceRoot.resolve("slime/java-slime/slime-parser/src/main/java"),
                workspaceRoot.resolve("slime/java-slime/slime-ast/src/main/java"),
                workspaceRoot.resolve("slime/java-slime/slime-token/src/main/java"),
                workspaceRoot.resolve("slime/java-slime/subhuti-java/src/main/java"),
                workspaceRoot.resolve("slime/java-slime/slime-java/src/main/java"));
        List<String> additionalEntries = List.of(
                "com.slime.parser.cstToAst.SlimeCstToAstUtils",
                "com.slime.parser.cstToAst.SlimeAstCreateUtils",
                "com.slime.java.ast.JavaCstToAst");

        long started = System.nanoTime();
        List<QinJavaProjectJsCompiler.EsmFileOutput> outputs = new QinJavaProjectJsCompiler()
                .compileSuperclassClosureEsmTsFiles(
                        sourceRoots,
                        "com.qin.parser.QinParser",
                        additionalEntries,
                        outputRoot);
        long elapsedMs = (System.nanoTime() - started) / 1_000_000L;
        if (outputs.isEmpty()) {
            throw new IllegalStateException("QinParser Java -> TS generation produced no files");
        }
        System.out.println("[QinParserJavaToTsGenerationProbe] files=" + outputs.size()
                + " elapsedMs=" + elapsedMs
                + " outputRoot=" + outputRoot);
    }
}
