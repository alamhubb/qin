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
        require(parserOutput.code().contains("__qin_subhuti_rule_cache_key([params])"),
                "Subhuti rule cache key uses explicit TS parameter array");
        require(!parserOutput.code().contains("__qin_subhuti_rule_cache_key(arguments)"),
                "Subhuti rule cache key avoids runtime arguments object");
        String subhutiCoreCode = byBinaryName.get("com.subhuti.parser.SubhutiParserCore").code();
        require(subhutiCoreCode.contains("__qin_targetFun.call(this, ...__qin_ruleArgs)"),
                "Subhuti TS runtime accepts external decorator rule args");
        require(subhutiCoreCode.contains("__qin_args[3] === null || typeof __qin_args[3] === \"string\""),
                "Subhuti cacheKey overload keeps string guard");
        String allGeneratedCode = outputs.stream()
                .map(QinJavaProjectJsCompiler.EsmFileOutput::code)
                .collect(Collectors.joining("\n"));
        require(allGeneratedCode.contains("typeof __qin_arg.alt === \"function\""),
                "Subhuti Alternative varargs guard accepts local TS Alternative.alt");
        require(allGeneratedCode.contains("typeof __qin_arg.run === \"function\""),
                "Subhuti Alternative varargs guard accepts local TS Alternative.run");
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
        Path sourceIndexTs = outputRoot.resolve("src").resolve("index.ts");
        Path sourceSlimeParserTs = outputRoot.resolve("src").resolve("SlimeParser.ts");
        Path sourceSlimeTokensTs = outputRoot.resolve("src").resolve("SlimeTokens.ts");
        Path sourceSlimeTokenConsumerTs = outputRoot.resolve("src").resolve("SlimeTokenConsumer.ts");
        Path es2025SlimeParserTs = outputRoot.resolve("src").resolve("language").resolve("es2025")
                .resolve("SlimeParser.ts");
        Path es2025SlimeTokensTs = outputRoot.resolve("src").resolve("language").resolve("es2025")
                .resolve("SlimeTokens.ts");
        Path es2025SlimeTokenConsumerTs = outputRoot.resolve("src").resolve("language").resolve("es2025")
                .resolve("SlimeTokenConsumer.ts");
        require(Files.isRegularFile(packageJson), "generated TS ESM npm package.json");
        require(Files.isRegularFile(qinConfig), "generated TS ESM qin.config.js");
        require(Files.isRegularFile(indexTs), "generated TS ESM package index.ts");
        require(Files.isRegularFile(sourceIndexTs), "generated TS ESM src/index.ts compatibility entry");
        require(Files.isRegularFile(sourceSlimeParserTs), "generated TS ESM src/SlimeParser.ts compatibility entry");
        require(Files.isRegularFile(sourceSlimeTokensTs), "generated TS ESM src/SlimeTokens.ts compatibility entry");
        require(Files.isRegularFile(sourceSlimeTokenConsumerTs),
                "generated TS ESM src/SlimeTokenConsumer.ts compatibility entry");
        require(Files.isRegularFile(es2025SlimeParserTs),
                "generated TS ESM src/language/es2025/SlimeParser.ts compatibility entry");
        require(Files.isRegularFile(es2025SlimeTokensTs),
                "generated TS ESM src/language/es2025/SlimeTokens.ts compatibility entry");
        require(Files.isRegularFile(es2025SlimeTokenConsumerTs),
                "generated TS ESM src/language/es2025/SlimeTokenConsumer.ts compatibility entry");
        String packageJsonText = Files.readString(packageJson, StandardCharsets.UTF_8);
        String qinConfigText = Files.readString(qinConfig, StandardCharsets.UTF_8);
        String indexText = Files.readString(indexTs, StandardCharsets.UTF_8);
        require(packageJsonText.contains("\"name\": \"@qin/generated-slime-parser-ts\""),
                "generated package has stable npm name");
        require(packageJsonText.contains("\"entryBinaryName\": \"com.slime.parser.SlimeParser\""),
                "generated package records Java entry binary name");
        require(packageJsonText.contains("\"./src/language/es2025/SlimeParser.ts\""),
                "generated package exports legacy SlimeParser deep import");
        require(packageJsonText.contains("\"src\""),
                "generated package includes source compatibility entries in files list");
        require(qinConfigText.contains("entry: \"./index.ts\""),
                "generated qin.config.js points to TS package entry");
        require(indexText.contains("class SlimeParser extends __QinGeneratedSlimeParserBase"),
                "generated package index wraps SlimeParser for JS/TS package ABI");
        require(indexText.contains("this.parsedTokens = __qinToJsArray(super.parsedTokens())"),
                "generated package index exposes parsedTokens as a synced JS property");
        require(indexText.contains("export { __QinGeneratedSlimeParserBase as com_slime_parser_SlimeParser }"),
                "generated package index keeps raw Java class export");
        require(indexText.contains("export default SlimeParser;"),
                "generated package index exports SlimeParser wrapper as default");
        require(indexText.contains("export const SlimeTokensObj = Object.fromEntries"),
                "generated package index exposes slime token object");
        require(indexText.contains("export const SlimeJavascriptTokensObj = SlimeTokensObj;"),
                "generated package index exposes Slime JavaScript token compatibility alias");
        require(Files.readString(es2025SlimeParserTs, StandardCharsets.UTF_8)
                        .contains("export { default } from \"../../../index.ts\""),
                "legacy SlimeParser deep import re-exports generated package index");

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
