package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain {
    private QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = Path.of("").toAbsolutePath();
        Path workspaceRoot = qinRoot.getParent();
        List<Path> sourceRoots = List.of(
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-parser")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-token")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("subhuti-java")
                        .resolve("src").resolve("main").resolve("java"));

        Path outputRoot = qinRoot.resolve(".qin")
                .resolve("generated")
                .resolve("slime-parser")
                .resolve("ts-esm");
        List<QinJavaProjectJsCompiler.EsmFileOutput> outputs = new QinJavaProjectJsCompiler()
                .compileSuperclassClosureEsmTsFiles(sourceRoots, "com.slime.parser.SlimeParser", outputRoot);

        Map<String, QinJavaProjectJsCompiler.EsmFileOutput> byBinaryName = outputs.stream()
                .collect(Collectors.toMap(QinJavaProjectJsCompiler.EsmFileOutput::binaryName, output -> output));
        QinJavaProjectJsCompiler.EsmFileOutput parserOutput = byBinaryName.get("com.slime.parser.SlimeParser");
        require(parserOutput != null, "SlimeParser TS ESM output");
        require(parserOutput.outputFile().endsWith(Path.of("com", "slime", "parser", "SlimeParser.ts")),
                "SlimeParser Java file maps to matching TS path");
        require(parserOutput.code().contains(
                        "import { com_slime_parser_typescript_SlimeTSDeclarationParser } from \"./typescript/SlimeTSDeclarationParser.ts\";"),
                "SlimeParser imports direct superclass TS module");
        require(parserOutput.code().contains(
                        "class com_slime_parser_SlimeParser extends com_slime_parser_typescript_SlimeTSDeclarationParser"),
                "SlimeParser class declaration remains local");
        require(parserOutput.code().contains("constructor(...__qin_args: any[])"),
                "TypeScript backend emits constructor type annotations");
        require(parserOutput.code().contains("export { com_slime_parser_SlimeParser };"),
                "SlimeParser named ESM export");
        require(byBinaryName.containsKey("com.subhuti.struct.SubhutiCst"),
                "source dependency emits its own TS ESM file");
        require(byBinaryName.get("com.subhuti.struct.SubhutiCst")
                        .outputFile()
                        .endsWith(Path.of("com", "subhuti", "struct", "SubhutiCst.ts")),
                "SubhutiCst Java file maps to matching TS path");

        System.out.println("Generated ESM TS files: " + outputRoot);
        System.out.println("QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
