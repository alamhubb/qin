package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeAsyncFunctionRuleSmokeTestMain {
    private QinJavaProjectSlimeAsyncFunctionRuleSmokeTestMain() {
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

        Path root = Files.createTempDirectory("qin-java-slime-async-function-rule-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-slime-async-function-rule\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(100_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    generated
                            + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        const DeclarationParams = globalThis.__qinJavaProjectExports[
                          "com.slime.parser.base.SlimeJavascriptParserBase$DeclarationParams"
                        ];
                        const parser = new SlimeParser("const answer = 42;");
                        parser.AsyncFunctionDeclaration(new DeclarationParams(false, true, false));
                        "fail=" + parser.isParserFail() + ";index=" + parser.getCurrentIndex();
                        """,
                    "java_project_slime_async_function_rule");
            if (!"fail=true;index=0".equals(result)) {
                throw new IllegalStateException("Expected AsyncFunctionDeclaration to fail at index 0, got: " + result);
            }
            System.out.println("Generated JS bundle: " + outputFile);
            System.out.println("QinJavaProjectSlimeAsyncFunctionRuleSmokeTestMain OK");
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }
    }
}
