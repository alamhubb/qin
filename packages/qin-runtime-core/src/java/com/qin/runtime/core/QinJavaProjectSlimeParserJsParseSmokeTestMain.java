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
                        const moduleSourceType = SourceType.__qin_field_MODULE;
                        const JavaScriptTokens = globalThis.__qinJavaProjectExports["com.slime.token.JavaScriptTokens"];
                        if (typeof SlimeParser !== "function") {
                          throw new Error("Generated SlimeParser export is missing");
                        }
                        const tokenCount = JavaScriptTokens.getTokens().size();
                        const programParser = new SlimeParser("const answer = 42;");
                        const parseParser = new SlimeParser("const answer = 42;");
                        (() => {
                              const programValue = programParser.Program(moduleSourceType);
                              const parseValue = parseParser.parse();
                              return "slime=" + typeof SlimeParser
                                + ";tokensPositive=" + (tokenCount > 0)
                                + ";sourceType=" + typeof SourceType
                                + ";moduleNull=" + (moduleSourceType == null)
                                + ";programIndex=" + programParser.getCurrentIndex()
                                + ";programFail=" + programParser.isParserFail()
                                + ";programError=" + programParser.getErrorInfo()
                                + ";programParsed=" + programParser.getParsedTokens().size()
                                + ";programUnparsed=" + programParser.getUnparsedTokens().size()
                                + ";parseIndex=" + parseParser.getCurrentIndex()
                                + ";parseFail=" + parseParser.isParserFail()
                                + ";parseError=" + parseParser.getErrorInfo()
                                + ";parseParsed=" + parseParser.getParsedTokens().size()
                                + ";parseUnparsed=" + parseParser.getUnparsedTokens().size()
                                + ";programNull=" + (programValue == null)
                                + ";parseNull=" + (parseValue == null);
                            })();
                        """,
                "java_project_slime_parser_js_parse");
        String expected = "slime=function;tokensPositive=true;sourceType=function;moduleNull=false"
                + ";programIndex=18;programFail=false;programError=null;programParsed=5;programUnparsed=0"
                + ";parseIndex=18;parseFail=false;parseError=null;parseParsed=5;parseUnparsed=0"
                + ";programNull=false;parseNull=false";
        if (!expected.equals(result)) {
            throw new IllegalStateException("Expected generated SlimeParser to parse JS Program, got: " + result);
        }
        System.out.println("Generated JS bundle: " + outputFile);
        System.out.println("QinJavaProjectSlimeParserJsParseSmokeTestMain OK");
    }
}
