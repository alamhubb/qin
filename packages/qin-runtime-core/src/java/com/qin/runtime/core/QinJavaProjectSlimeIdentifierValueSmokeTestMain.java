package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeIdentifierValueSmokeTestMain {
    private QinJavaProjectSlimeIdentifierValueSmokeTestMain() {
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

        Path root = Files.createTempDirectory("qin-java-slime-identifier-value-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-slime-identifier-value\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        const parser = new SlimeParser("const answer = 42;");
                        const token = parser.LA(1);
                        const consumed = parser.consumeIdentifierValue("async");
                        "token=" + token.tokenName() + ":" + token.value()
                          + ";consumed=" + consumed
                          + ";fail=" + parser.isParserFail()
                          + ";index=" + parser.getCurrentIndex();
                        """,
                "java_project_slime_identifier_value");
        if (!"token=Const:const;consumed=false;fail=true;index=0".equals(result)) {
            throw new IllegalStateException("Expected async identifier value miss at index 0, got: " + result);
        }
        System.out.println("Generated JS bundle: " + outputFile);
        System.out.println("QinJavaProjectSlimeIdentifierValueSmokeTestMain OK");
    }
}
