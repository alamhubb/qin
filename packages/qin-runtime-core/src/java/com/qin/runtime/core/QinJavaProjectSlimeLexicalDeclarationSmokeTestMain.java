package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeLexicalDeclarationSmokeTestMain {
    private QinJavaProjectSlimeLexicalDeclarationSmokeTestMain() {
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

        Path root = Files.createTempDirectory("qin-java-slime-lexical-declaration-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-slime-lexical-declaration\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(5_000_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    generated
                            + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        const ExpressionParams = globalThis.__qinJavaProjectExports[
                          "com.slime.parser.base.SlimeJavascriptParserBase$ExpressionParams"
                        ];
                        const StatementParams = globalThis.__qinJavaProjectExports[
                          "com.slime.parser.base.SlimeJavascriptParserBase$StatementParams"
                        ];
                        const parser = new SlimeParser("const answer = 42;");
                        parser.LexicalDeclaration(new ExpressionParams(true, false, true));
                        const lexical = "lexicalFail=" + parser.isParserFail() + ";lexicalIndex=" + parser.getCurrentIndex();
                        parser.reset();
                        parser.StatementListItem(new StatementParams(false, true, false));
                        lexical + ";statementFail=" + parser.isParserFail() + ";statementIndex=" + parser.getCurrentIndex();
                        """,
                    "java_project_slime_lexical_declaration");
            if (!"lexicalFail=false;lexicalIndex=18;statementFail=false;statementIndex=18".equals(result)) {
                throw new IllegalStateException("Expected const lexical declaration parse, got: " + result);
            }
            System.out.println("Generated JS bundle: " + outputFile);
            System.out.println("QinJavaProjectSlimeLexicalDeclarationSmokeTestMain OK");
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }
    }
}
