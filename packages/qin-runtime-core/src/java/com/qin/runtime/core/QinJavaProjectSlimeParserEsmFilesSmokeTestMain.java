package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QinJavaProjectSlimeParserEsmFilesSmokeTestMain {
    private QinJavaProjectSlimeParserEsmFilesSmokeTestMain() {
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
                .resolve("esm");
        List<QinJavaProjectJsCompiler.EsmFileOutput> outputs = new QinJavaProjectJsCompiler()
                .compileSuperclassClosureEsmFiles(sourceRoots, "com.slime.parser.SlimeParser", outputRoot);

        Map<String, QinJavaProjectJsCompiler.EsmFileOutput> byBinaryName = outputs.stream()
                .collect(Collectors.toMap(QinJavaProjectJsCompiler.EsmFileOutput::binaryName, output -> output));
        QinJavaProjectJsCompiler.EsmFileOutput parserOutput = byBinaryName.get("com.slime.parser.SlimeParser");
        require(parserOutput != null, "SlimeParser ESM output");
        require(parserOutput.outputFile().endsWith(Path.of("com", "slime", "parser", "SlimeParser.js")),
                "SlimeParser Java file maps to matching JS path");
        require(parserOutput.js().contains(
                        "import { com_slime_parser_typescript_SlimeTSDeclarationParser } from \"./typescript/SlimeTSDeclarationParser.js\";"),
                "SlimeParser imports direct superclass module");
        require(parserOutput.js().contains(
                        "class com_slime_parser_SlimeParser extends com_slime_parser_typescript_SlimeTSDeclarationParser"),
                "SlimeParser class declaration remains local");
        require(parserOutput.js().contains("export { com_slime_parser_SlimeParser };"),
                "SlimeParser named ESM export");
        require(byBinaryName.containsKey("com.subhuti.struct.SubhutiCst"),
                "source dependency emits its own ESM file");
        require(byBinaryName.get("com.subhuti.struct.SubhutiCst")
                        .outputFile()
                        .endsWith(Path.of("com", "subhuti", "struct", "SubhutiCst.js")),
                "SubhutiCst Java file maps to matching JS path");

        System.out.println("Generated ESM JS files: " + outputRoot);
        System.out.println("QinJavaProjectSlimeParserEsmFilesSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
