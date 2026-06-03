package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class QinJavaProjectSlimeParserJsBundleSmokeTestMain {
    private QinJavaProjectSlimeParserJsBundleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = Path.of("").toAbsolutePath();
        Path workspaceRoot = qinRoot.getParent();
        List<Path> sourceRoots = List.of(
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-parser")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("subhuti-java")
                        .resolve("src").resolve("main").resolve("java"));
        QinJavaProjectJsCompiler compiler = new QinJavaProjectJsCompiler();
        Map<String, Path> sourceFiles = compiler.superclassSourceFiles(sourceRoots, "com.slime.parser.SlimeParser");
        require(sourceFiles.containsKey("com.slime.parser.base.SlimeJavascriptParserBase"),
                "SlimeJavascriptParserBase source in superclass closure");
        require(sourceFiles.containsKey("com.slime.parser.SlimeParser"),
                "SlimeParser source in superclass closure");
        require(sourceFiles.containsKey("com.subhuti.struct.SubhutiCst"),
                "SubhutiCst source dependency in bundle closure");
        require(sourceFiles.containsKey("com.subhuti.error.SubhutiErrorTypes"),
                "SubhutiErrorTypes enum source dependency in bundle closure");

        Path outputFile = qinRoot.resolve(".qin")
                .resolve("generated")
                .resolve("slime-parser")
                .resolve("slime-parser.bundle.js");
        String generated = compiler.compileSuperclassClosure(sourceRoots, "com.slime.parser.SlimeParser", outputFile);
        require(generated.contains("class SlimeJavascriptParserBase extends SubhutiParser"),
                "generated SlimeJavascriptParserBase");
        require(generated.contains("class SlimeParser extends SlimeTSDeclarationParser"),
                "generated SlimeParser");
        require(generated.indexOf("class SlimeJavascriptParserBase")
                        < generated.indexOf("class SlimeParser"),
                "superclass appears before SlimeParser");
        require(generated.contains("globalThis.__qinJavaProjectExports[\"com.slime.parser.SlimeParser\"] = SlimeParser;"),
                "SlimeParser project export");

        System.out.println("Generated JS bundle: " + outputFile);
        System.out.println("QinJavaProjectSlimeParserJsBundleSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
