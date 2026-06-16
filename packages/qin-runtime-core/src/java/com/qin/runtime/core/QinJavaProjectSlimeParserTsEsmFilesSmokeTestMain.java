package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain {
    private QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = findQinRoot();
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
        Path packageJson = outputRoot.resolve("package.json");
        Path qinConfig = outputRoot.resolve("qin.config.js");
        Path indexTs = outputRoot.resolve("index.ts");
        require(Files.isRegularFile(packageJson), "generated TS ESM npm package.json");
        require(Files.isRegularFile(qinConfig), "generated TS ESM qin.config.js");
        require(Files.isRegularFile(indexTs), "generated TS ESM package index.ts");
        String packageJsonText = Files.readString(packageJson, StandardCharsets.UTF_8);
        String qinConfigText = Files.readString(qinConfig, StandardCharsets.UTF_8);
        String indexText = Files.readString(indexTs, StandardCharsets.UTF_8);
        require(packageJsonText.contains("\"name\": \"@qin/generated-slime-parser-ts\""),
                "generated package has stable npm name");
        require(packageJsonText.contains("\"entryBinaryName\": \"com.slime.parser.SlimeParser\""),
                "generated package records Java entry binary name");
        require(qinConfigText.contains("entry: \"./index.ts\""),
                "generated qin.config.js points to TS package entry");
        require(indexText.contains("export { com_slime_parser_SlimeParser, com_slime_parser_SlimeParser as SlimeParser }"),
                "generated package index exports friendly SlimeParser alias");
        require(indexText.contains("export const SlimeTokensObj = Object.fromEntries"),
                "generated package index exposes slime token object");

        System.out.println("Generated ESM TS files: " + outputRoot);
        System.out.println("Generated ESM TS npm package: @qin/generated-slime-parser-ts");
        System.out.println("QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK");
    }

    private static Path findQinRoot() {
        Path search = Path.of("").toAbsolutePath().normalize();
        while (search != null) {
            if (Files.isDirectory(search.resolve("packages").resolve("qin-runtime-core"))
                    && Files.isRegularFile(search.resolve("qin.config.js"))) {
                return search;
            }
            search = search.getParent();
        }
        throw new IllegalStateException("Cannot find Qin repo root");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
