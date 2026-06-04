package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeParserJsParseSmokeTestMain {
    private QinJavaProjectSlimeParserJsParseSmokeTestMain() {
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

        Path outputFile = qinRoot.resolve(".qin")
                .resolve("generated")
                .resolve("slime-parser")
                .resolve("slime-parser.bundle.js");
        String generated = new QinJavaProjectJsCompiler()
                .compileSuperclassClosure(sourceRoots, "com.slime.parser.SlimeParser", outputFile);

        Path root = Files.createTempDirectory("qin-java-slime-parser-js-parse-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-slime-parser-js-parse\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        const SourceType = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeJavascriptParser$SourceType"];
                        if (typeof SlimeParser !== "function") {
                          throw new Error("Generated SlimeParser export is missing");
                        }
                        const parser = new SlimeParser("const answer = 42;");
                        const programValue = parser.Program(SourceType.MODULE);
                        const parseValue = parser.parse();
                        "slime=" + typeof SlimeParser
                          + ";sourceType=" + typeof SourceType
                          + ";module=" + SourceType.MODULE
                          + ";index=" + parser.getCurrentIndex()
                          + ";fail=" + parser.isParserFail()
                          + ";error=" + parser.getErrorInfo()
                          + ";parsed=" + parser.getParsedTokens().size()
                          + ";unparsed=" + parser.getUnparsedTokens().size()
                          + ";programNull=" + (programValue == null)
                          + ";parseNull=" + (parseValue == null);
                        """,
                "java_project_slime_parser_js_parse");
        if (!(result instanceof String value) || !value.startsWith("Program:")) {
            throw new IllegalStateException("Expected generated SlimeParser to parse JS Program, got: " + result);
        }
        System.out.println("Generated JS bundle: " + outputFile);
        System.out.println("QinJavaProjectSlimeParserJsParseSmokeTestMain OK");
    }
}
