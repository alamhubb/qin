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
        QinJavaProjectJsCompiler.EsmFileOutput parserOutput = findOutput(
                outputs,
                byBinaryName,
                "com.slime.parser.SlimeParser",
                Path.of("com", "slime", "parser", "SlimeParser.ts"));
        require(parserOutput.outputFile().endsWith(Path.of("com", "slime", "parser", "SlimeParser.ts")),
                "SlimeParser Java file maps to matching TS path");
        require(parserOutput.code().contains(
                        "from \"./SlimeParserRuntimeBase.ts\";"),
                "SlimeParser imports direct runtime base TS module");
        require(parserOutput.code().contains(
                        "class com_slime_parser_SlimeParser extends com_slime_parser_SlimeParserRuntimeBase"),
                "SlimeParser class declaration remains local and extends runtime base");
        require(parserOutput.code().contains("constructor(...__qin_args: any[])"),
                "TypeScript backend emits constructor type annotations");
        require(parserOutput.code().contains("__qin_subhuti_rule_cache_key([params])"),
                "Subhuti rule cache key uses explicit TS parameter array");
        require(!parserOutput.code().contains("__qin_subhuti_rule_cache_key(arguments)"),
                "Subhuti rule cache key avoids runtime arguments object");
        require(parserOutput.code().contains(
                        "import { __qin_subhuti_rule_cache_key } from \"@qin/java-sdk-js/tooling\";"),
                "SlimeParser TS rule wrappers import the static cache-key array helper");
        String subhutiCoreCode = byBinaryName.get("com.subhuti.parser.SubhutiParserCore").code();
        String subhutiCombinatorsCode = byBinaryName.get("com.subhuti.parser.SubhutiParserCombinators").code();
        require(subhutiCoreCode.contains(
                        "import { com_subhuti_parser_SubhutiRuleCacheKey"),
                "Subhuti TS runtime imports generated local cache-key class");
        require(subhutiCoreCode.contains(
                        "return new com_subhuti_parser_SubhutiRuleCacheKey(ruleName, cacheKeyExtra, cursorStamp, mode, lastTokenName);"),
                "Subhuti TS runtime builds cache keys through explicit generated class constructor");
        require(!subhutiCoreCode.contains("__qin_subhuti_rule_cache_key(arguments)"),
                "Subhuti TS runtime does not pass runtime arguments object into cache-key helper");
        require(subhutiCoreCode.contains("__qin_args.length === 4")
                        && subhutiCoreCode.contains("typeof __qin_args[3] !== \"undefined\""),
                "Subhuti cacheKey overload keeps explicit arity and definedness guard");
        String allGeneratedCode = outputs.stream()
                .map(QinJavaProjectJsCompiler.EsmFileOutput::code)
                .collect(Collectors.joining("\n"));
        QinGeneratedTsStaticAdmissionAudit.Result staticAdmissionAudit =
                QinGeneratedTsStaticAdmissionAudit.audit(outputs);
        QinGeneratedTsStaticAdmissionAudit.assertRejectsUnprovenDynamicShapes();
        require(staticAdmissionAudit.allowedDynamicWrapperCount() > 0,
                "generated TS static admission audit classifies proven wrapper shapes");
        require(staticAdmissionAudit.legacyAllowedDynamicWrapperCount() == 0,
                "generated TS static admission audit has no legacy dynamic wrapper admissions");
        require(!allGeneratedCode.contains("__qinSubhutiAlternative")
                        && !allGeneratedCode.contains("com_subhuti_parser_Alternative.of"),
                "Subhuti generated TS no longer depends on legacy dynamic Alternative adapter shape");
        require(subhutiCombinatorsCode.contains("executeFiniteProgramStaticOr(")
                        && subhutiCombinatorsCode.contains("executeAdaptiveStaticOr(")
                        && subhutiCombinatorsCode.contains("executeStaticSharedPrefixOr("),
                "Subhuti generated TS keeps static Or execution families");
        require(subhutiCombinatorsCode.contains("Sequence(...rules: QinJavaSupplier[])")
                        && subhutiCombinatorsCode.contains("__qin_functional_rules_0.add(__qin_java_functional(__qin_arg));"),
                "Subhuti supplier varargs overload adapts each Java functional element explicitly");
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
        require(indexText.contains("export const SlimeTokenType = {")
                        && indexText.contains("RBrace: \"RBrace\""),
                "generated package index exposes static slime token type string map");
        require(indexText.contains("export const SlimeJavascriptTokensObj = SlimeTokensObj;"),
                "generated package index exposes Slime JavaScript token compatibility alias");
        require(Files.readString(es2025SlimeParserTs, StandardCharsets.UTF_8)
                        .contains("export { default } from \"../../../index.ts\""),
                "legacy SlimeParser deep import re-exports generated package index");

        System.out.println("Generated ESM TS files: " + outputRoot);
        System.out.println("Generated ESM TS npm package: @qin/generated-slime-parser-ts");
        System.out.println("Generated TS static admission wrappers: "
                + staticAdmissionAudit.allowedDynamicWrapperCount());
        System.out.println("Generated TS static admission contract wrappers: "
                + staticAdmissionAudit.contractAllowedDynamicWrapperCount());
        System.out.println("Generated TS static admission legacy wrappers: "
                + staticAdmissionAudit.legacyAllowedDynamicWrapperCount());
        System.out.println("Generated TS static admission legacy reasons: "
                + staticAdmissionAudit.legacyAllowedDynamicWrapperReasons());
        System.out.println("QinJavaProjectSlimeParserTsEsmFilesSmokeTestMain OK");
    }

    private static QinJavaProjectJsCompiler.EsmFileOutput findOutput(
            List<QinJavaProjectJsCompiler.EsmFileOutput> outputs,
            Map<String, QinJavaProjectJsCompiler.EsmFileOutput> byBinaryName,
            String binaryName,
            Path outputSuffix) {
        QinJavaProjectJsCompiler.EsmFileOutput byName = byBinaryName.get(binaryName);
        if (byName != null) {
            return byName;
        }
        return outputs.stream()
                .filter(output -> output.outputFile().endsWith(outputSuffix))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Expected output for " + binaryName + " or path suffix " + outputSuffix));
    }

    private static Path findQinRoot() {
        Path search = Path.of("").toAbsolutePath().normalize();
        while (search != null) {
            if (Files.isDirectory(search.resolve("packages").resolve("qin-runtime-core"))
                    && Files.isDirectory(search.resolve("packages").resolve("qin-parser"))
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
