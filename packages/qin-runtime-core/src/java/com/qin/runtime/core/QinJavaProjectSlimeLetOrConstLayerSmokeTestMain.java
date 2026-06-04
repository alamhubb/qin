package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeLetOrConstLayerSmokeTestMain {
    private QinJavaProjectSlimeLetOrConstLayerSmokeTestMain() {
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

        Path root = Files.createTempDirectory("qin-java-slime-let-or-const-layer-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-slime-let-or-const-layer\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(1_000_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    generated
                            + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        function state(label, parser) {
                          return label + ":fail=" + parser.isParserFail() + ",index=" + parser.getCurrentIndex();
                        }
                        const directLet = new SlimeParser("const answer = 42;");
                        directLet.__qin_field_tokenConsumer.Let();
                        const directLetResult = state("let", directLet);

                        const directConst = new SlimeParser("const answer = 42;");
                        directConst.__qin_field_tokenConsumer.Const();
                        const directConstResult = state("const", directConst);

                        const manualFn = new SlimeParser("const answer = 42;");
                        const manualFnRule = __qin_java_functional(() => manualFn.__qin_field_tokenConsumer.Const());
                        manualFnRule.run();
                        const manualFnResult = state("manualFn", manualFn);

                        const directRunnableOverload = new SlimeParser("const answer = 42;");
                        directRunnableOverload.__qin_overload_Or_1_2(
                            __qin_java_functional(() => directRunnableOverload.__qin_field_tokenConsumer.Let()),
                            __qin_java_functional(() => directRunnableOverload.__qin_field_tokenConsumer.Const()));
                        const directRunnableOverloadResult = state("orRunnable", directRunnableOverload);

                        const directOr = new SlimeParser("const answer = 42;");
                        directOr.Or(__qin_java_functional(() => directOr.__qin_field_tokenConsumer.Let()),
                            __qin_java_functional(() => directOr.__qin_field_tokenConsumer.Const()));
                        const directOrResult = state("or", directOr);

                        const directRaw = new SlimeParser("const answer = 42;");
                        directRaw.__qin_subhuti_raw_LetOrConst();
                        const directRawResult = state("raw", directRaw);

                        directLetResult + ";" + directConstResult + ";" + manualFnResult + ";"
                            + directRunnableOverloadResult + ";" + directOrResult + ";" + directRawResult;
                        """,
                    "java_project_slime_let_or_const_layer");
            String expected = "let:fail=true,index=0;const:fail=false,index=5;manualFn:fail=false,index=5;"
                    + "orRunnable:fail=false,index=5;or:fail=false,index=5;raw:fail=false,index=5";
            if (!expected.equals(result)) {
                throw new IllegalStateException("Expected layer results " + expected + ", got: " + result);
            }
            System.out.println("Generated JS bundle: " + outputFile);
            System.out.println("QinJavaProjectSlimeLetOrConstLayerSmokeTestMain OK");
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }
    }
}
