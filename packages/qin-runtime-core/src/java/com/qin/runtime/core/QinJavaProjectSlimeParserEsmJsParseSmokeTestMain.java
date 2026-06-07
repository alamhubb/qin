package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QinJavaProjectSlimeParserEsmJsParseSmokeTestMain {
    private QinJavaProjectSlimeParserEsmJsParseSmokeTestMain() {
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
        require(parserOutput.js().contains("from \"@qin/java-sdk-js\""),
                "SlimeParser imports the shared Java SDK JS package");
        require(!parserOutput.js().contains("const __QinJavaLangString ="),
                "SlimeParser does not inline java.lang.String runtime");
        require(!parserOutput.js().contains("class __QinJavaLangStringBuilder"),
                "SlimeParser does not inline StringBuilder runtime");

        Path sdkPackage = outputRoot.resolve("node_modules")
                .resolve("@qin")
                .resolve("java-sdk-js");
        require(Files.isRegularFile(sdkPackage.resolve("package.json")),
                "@qin/java-sdk-js package.json");
        String sdkSource = Files.readString(sdkPackage.resolve("index.js"), StandardCharsets.UTF_8);
        require(sdkSource.contains("const __QinJavaLangString ="),
                "@qin/java-sdk-js owns java.lang.String runtime");
        require(sdkSource.contains("class __QinJavaLangStringBuilder"),
                "@qin/java-sdk-js owns StringBuilder runtime");

        Path smokeFile = outputRoot.resolve("parse-smoke.mjs");
        Files.writeString(
                smokeFile,
                """
                        import { com_slime_parser_SlimeParser } from './com/slime/parser/SlimeParser.js';

                        const parser = new com_slime_parser_SlimeParser('const answer = 42;');
                        const result = parser.parse();
                        console.log(JSON.stringify({
                          parserType: typeof com_slime_parser_SlimeParser,
                          index: parser.getCurrentIndex(),
                          fail: parser.isParserFail(),
                          error: parser.getErrorInfo(),
                          parsed: parser.getParsedTokens().size(),
                          unparsed: parser.getUnparsedTokens().size(),
                          resultNull: result == null
                        }));
                        """,
                StandardCharsets.UTF_8);

        Process process = new ProcessBuilder("node", smokeFile.toAbsolutePath().toString())
                .directory(outputRoot.toFile())
                .redirectErrorStream(true)
                .start();
        String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Node ESM SlimeParser smoke failed with exit " + exitCode + ":\n" + output);
        }
        String expected = """
                {"parserType":"function","index":18,"fail":false,"error":null,"parsed":5,"unparsed":0,"resultNull":false}\
                """;
        if (!expected.equals(output)) {
            throw new IllegalStateException("Expected generated ESM SlimeParser to parse JS, got: " + output);
        }

        System.out.println("Generated ESM JS files: " + outputRoot);
        System.out.println("QinJavaProjectSlimeParserEsmJsParseSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
