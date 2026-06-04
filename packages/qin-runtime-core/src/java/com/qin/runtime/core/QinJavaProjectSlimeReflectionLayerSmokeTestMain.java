package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeReflectionLayerSmokeTestMain {
    private QinJavaProjectSlimeReflectionLayerSmokeTestMain() {
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

        Path root = Files.createTempDirectory("qin-java-slime-reflection-layer-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-slime-reflection-layer\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(500_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    generated
                            + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        const parser = new SlimeParser("const answer = 42;");
                        const intClass = ({ getName() { return "int"; }, getSimpleName() { return "int"; }, toString() { return "class int"; } });
                        const findMethod = parser.__qin_field_tokenConsumer.findMethod(parser.getClass(), "LA", intClass);
                        const requireMethod = parser.__qin_field_tokenConsumer.requireMethod(parser.getClass(), "LA", intClass);
                        "find=" + (findMethod == null ? "null" : String(findMethod))
                          + ";require=" + (requireMethod == null ? "null" : String(requireMethod));
                        """,
                    "java_project_slime_reflection_layer");
            System.out.println(result);
            if (!String.valueOf(result).contains("require=")) {
                throw new IllegalStateException("Expected requireMethod to return LA(int), got: " + result);
            }
            System.out.println("Generated JS bundle: " + outputFile);
            System.out.println("QinJavaProjectSlimeReflectionLayerSmokeTestMain OK");
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }
    }
}
