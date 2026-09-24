package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QinJavaProjectStringInstanceMethodEsmSmokeTestMain {
    private QinJavaProjectStringInstanceMethodEsmSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = findQinRoot();
        Path workspaceRoot = qinRoot.getParent();
        List<Path> sourceRoots = List.of(
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("subhuti-java")
                        .resolve("src").resolve("main").resolve("java"));
        Path root = Files.createTempDirectory("qin-java-string-instance-esm-");
        Path outputRoot = root.resolve("generated");
        List<QinJavaProjectJsCompiler.EsmFileOutput> outputs = new QinJavaProjectJsCompiler()
                .compileSuperclassClosureEsmTsFiles(
                        sourceRoots,
                        "com.subhuti.lexer.SubhutiLexer",
                        outputRoot);
        Map<String, QinJavaProjectJsCompiler.EsmFileOutput> byBinaryName = outputs.stream()
                .collect(Collectors.toMap(QinJavaProjectJsCompiler.EsmFileOutput::binaryName, output -> output));
        QinJavaProjectJsCompiler.EsmFileOutput createTokenOutput = byBinaryName.get("com.subhuti.struct.SubhutiCreateToken");
        require(createTokenOutput != null, "SubhutiCreateToken output in " + byBinaryName.keySet());
        String createTokenCode = createTokenOutput.code();
        require(createTokenCode.contains("__QinJavaLangString.isBlank(name)"),
                "String.isBlank instance call must use java-sdk-js");
        require(!createTokenCode.contains("name.isBlank()"),
                "String.isBlank must not emit a non-standard JS string method");
        QinJavaProjectJsCompiler.EsmFileOutput lexerOutput = byBinaryName.get("com.subhuti.lexer.SubhutiLexer");
        require(lexerOutput != null, "SubhutiLexer output in " + byBinaryName.keySet());
        String lexerCode = lexerOutput.code();
        require(lexerCode.contains("__QinJavaLangString.regionMatches(code, index, fixedValue"),
                "String.regionMatches instance call must use java-sdk-js");
        require(!lexerCode.contains("code.regionMatches("),
                "String.regionMatches must not emit a non-standard JS string method");
        QinJavaProjectJsCompiler.EsmFileOutput vocabularyOutput =
                byBinaryName.get("com.subhuti.lexer.SubhutiLexerVocabulary");
        require(vocabularyOutput != null, "SubhutiLexerVocabulary output in " + byBinaryName.keySet());
        String vocabularyCode = vocabularyOutput.code();
        require(vocabularyCode.contains("code.codePointAt(index)"),
                "lexer vocabulary candidate lookup must use numeric code point indexing");
        require(vocabularyCode.contains("fixedValue.codePointAt(0.0)"),
                "lexer vocabulary fixed-token candidate table must compare numeric code points");
        System.out.println("QinJavaProjectStringInstanceMethodEsmSmokeTestMain OK");
    }

    private static Path findQinRoot() {
        Path search = Path.of("").toAbsolutePath().normalize();
        while (search != null) {
            if (Files.isDirectory(search.resolve("packages").resolve("qin-runtime-core"))
                    && Files.isDirectory(search.resolve("packages").resolve("qin-language"))
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
