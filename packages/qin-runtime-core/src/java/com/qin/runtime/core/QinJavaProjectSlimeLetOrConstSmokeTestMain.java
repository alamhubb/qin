package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeLetOrConstSmokeTestMain {
    private QinJavaProjectSlimeLetOrConstSmokeTestMain() {
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

        Path root = Files.createTempDirectory("qin-java-slime-let-or-const-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-slime-let-or-const\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(5_000_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    generated
                            + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        const topLevelParser = new SlimeParser("const answer = 42;");
                        let topLevelError = null;
                        try {
                          topLevelParser.LetOrConst();
                        } catch (error) {
                          topLevelError = error;
                        }
                        const rawParser = new SlimeParser("const answer = 42;");
                        rawParser.__qin_subhuti_raw_LetOrConst();
                        "topLevelError=" + (topLevelError != null)
                          + ";topLevelIndex=" + topLevelParser.getCurrentIndex()
                          + ";rawFail=" + rawParser.isParserFail()
                          + ";rawIndex=" + rawParser.getCurrentIndex();
                        """,
                    "java_project_slime_let_or_const");
            String expected = "topLevelError=true;topLevelIndex=5;rawFail=false;rawIndex=5";
            if (!expected.equals(result)) {
                throw new IllegalStateException("Expected LetOrConst wrapper/raw parity " + expected + ", got: " + result);
            }
            System.out.println("Generated JS bundle: " + outputFile);
            System.out.println("QinJavaProjectSlimeLetOrConstSmokeTestMain OK");
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }
    }
}
