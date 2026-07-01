package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinLanguageGenerateParserSmokeTestMain {
    private QinLanguageGenerateParserSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("D:/project/qkyproject/qinall/qin").toAbsolutePath().normalize();
        Path outputRoot = root.resolve("packages/qin-language/generated/qin-parser-ts");
        List<Path> sourceRoots = List.of(
                root.resolve("../slime/java-slime/subhuti-java/src/main/java").normalize(),
                root.resolve("../slime/java-slime/slime-token/src/main/java").normalize(),
                root.resolve("../slime/java-slime/slime-ast/src/main/java").normalize(),
                root.resolve("../slime/java-slime/slime-parser/src/main/java").normalize(),
                root.resolve("packages/qin-parser/src/java").normalize());
        List<String> additionalEntries = List.of(
                "com.slime.parser.cstToAst.SlimeCstToAstUtils",
                "com.slime.parser.cstToAst.SlimeAstCreateUtils");

        List<QinJavaProjectJsCompiler.EsmFileOutput> outputs = new QinJavaProjectJsCompiler()
                .compileSuperclassClosureEsmTsFiles(
                        sourceRoots,
                        "com.qin.parser.QinParser",
                        additionalEntries,
                        outputRoot);
        if (outputs.isEmpty()) {
            throw new IllegalStateException("No generated parser files were emitted");
        }
        Path typeCstToAst = outputRoot.resolve("com")
                .resolve("slime")
                .resolve("parser")
                .resolve("cstToAst")
                .resolve("typescript")
                .resolve("SlimeTSTypeCstToAst.ts");
        String typeCstToAstSource = Files.readString(typeCstToAst, StandardCharsets.UTF_8);
        if (!typeCstToAstSource.contains("com_slime_ast_AstNodeType as AstNodeType")) {
            throw new IllegalStateException("Generated SlimeTSTypeCstToAst.ts must import AstNodeType");
        }
        System.out.println("QinLanguageGenerateParserSmokeTestMain OK " + outputs.size());
    }
}
