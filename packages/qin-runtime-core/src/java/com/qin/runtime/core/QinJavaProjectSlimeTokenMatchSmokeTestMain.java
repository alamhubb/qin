package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeTokenMatchSmokeTestMain {
    private QinJavaProjectSlimeTokenMatchSmokeTestMain() {
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

        Path root = Files.createTempDirectory("qin-java-slime-token-match-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-slime-token-match\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + """

                        const JavaScriptTokens = globalThis.__qinJavaProjectExports["com.slime.token.JavaScriptTokens"];
                        const tokens = JavaScriptTokens.getTokens();
                        const code = "const answer = 42;";
                        let hit = null;
                        for (const token of tokens) {
                          const pattern = token.getPattern();
                          if (pattern == null) {
                            continue;
                          }
                          const matcher = pattern.matcher(code);
                          matcher.region(0, code.length);
                          if (matcher.lookingAt()) {
                            hit = token.getName() + ":" + matcher.group();
                            break;
                          }
                        }
                        hit == null ? "NO_MATCH tokens=" + tokens.size() : hit;
                        """,
                "java_project_slime_token_match");
        if (!"Const:const".equals(result)) {
            throw new IllegalStateException("Expected Const token match, got: " + result);
        }
        System.out.println("Generated JS bundle: " + outputFile);
        System.out.println("QinJavaProjectSlimeTokenMatchSmokeTestMain OK");
    }
}
