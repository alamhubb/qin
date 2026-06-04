package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeParserJsConstructSmokeTestMain {
    private QinJavaProjectSlimeParserJsConstructSmokeTestMain() {
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

        Path root = Files.createTempDirectory("qin-java-slime-parser-js-construct-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-slime-parser-js-construct\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        const parser = new SlimeParser("const answer = 42;");
                        "cstStack=" + (parser.__qin_field_cstStack == null ? "null" : typeof parser.__qin_field_cstStack)
                          + ";parseSuccess=" + parser.__qin_field_parseSuccess
                          + ";lookahead=" + parser.__qin_field__parseSuccess;
                        """,
                "java_project_slime_parser_js_construct");
        String value = String.valueOf(result);
        if (!value.startsWith("cstStack=object;")) {
            throw new IllegalStateException("Expected generated SlimeParser constructor fields, got: " + result);
        }
        System.out.println("Generated JS bundle: " + outputFile);
        System.out.println("QinJavaProjectSlimeParserJsConstructSmokeTestMain OK");
    }
}
