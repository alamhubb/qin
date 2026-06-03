package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaSlimeParserJsBackendSmokeTestMain {
    private QinJavaSlimeParserJsBackendSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram minimalProgram = new QinJavaAstIrLowerer().lowerSource("""
                package com.slime.parser;
                public class SlimeParser extends SlimeTSDeclarationParser {
                }
                """);
        String minimalGenerated = new QinJsBackend().compileProgram(minimalProgram);
        require(minimalGenerated.contains("class SlimeParser extends SlimeTSDeclarationParser"),
                "minimal public SlimeParser inheritance");
        QinIrProgram constructorProgram = new QinJavaAstIrLowerer().lowerSource("""
                package com.slime.parser;
                public class SlimeParser extends SlimeTSDeclarationParser {
                    public SlimeParser(String sourceCode) {
                        super(sourceCode);
                    }
                }
                """);
        String constructorGenerated = new QinJsBackend().compileProgram(constructorProgram);
        require(constructorGenerated.contains("constructor("), "generated SlimeParser constructor");

        Path qinRoot = Path.of("").toAbsolutePath();
        Path workspaceRoot = qinRoot.getParent();
        Path slimeParser = workspaceRoot
                .resolve("slime")
                .resolve("java-slime")
                .resolve("slime-parser")
                .resolve("src")
                .resolve("main")
                .resolve("java")
                .resolve("com")
                .resolve("slime")
                .resolve("parser")
                .resolve("SlimeParser.java");
        if (!Files.isRegularFile(slimeParser)) {
            throw new IllegalStateException("Missing sibling SlimeParser.java: " + slimeParser);
        }

        String source = Files.readString(slimeParser, StandardCharsets.UTF_8);
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource(source);
        String generated = new QinJsBackend().compileProgram(program);
        Path generatedOutput = qinRoot
                .resolve(".qin")
                .resolve("generated")
                .resolve("slime-parser")
                .resolve("SlimeParser.js");
        Files.createDirectories(generatedOutput.getParent());
        Files.writeString(generatedOutput, generated, StandardCharsets.UTF_8);

        require(generated.contains("class SlimeParser"), "generated SlimeParser class");
        require(generated.contains("extends SlimeTSDeclarationParser"), "generated SlimeParser inheritance");
        require(generated.contains("BindingIdentifier("), "generated overridden parser method");
        require(generated.contains("() =>"), "generated parser lambda callbacks");
        require(generated.contains("this.tokenConsumer.Question()"), "generated inherited tokenConsumer receiver");
        require(generated.contains("class ExpressionParams"), "generated inherited Java record runtime");
        require(generated.contains("ExpressionParams.DEFAULT"), "generated Java record static field access");

        System.out.println("Generated JS: " + generatedOutput);
        System.out.println("QinJavaSlimeParserJsBackendSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
