package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QinJavaProjectQinParserTsEsmFilesSmokeTestMain {
    private QinJavaProjectQinParserTsEsmFilesSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = findQinRoot();
        Path workspaceRoot = qinRoot.getParent();
        List<Path> sourceRoots = List.of(
                qinRoot.resolve("packages").resolve("qin-parser").resolve("src").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-parser")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-token")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("subhuti-java")
                        .resolve("src").resolve("main").resolve("java"));

        Path outputRoot = qinRoot.resolve(".qin")
                .resolve("generated")
                .resolve("qin-parser")
                .resolve("ts-esm");
        List<QinJavaProjectJsCompiler.EsmFileOutput> outputs = new QinJavaProjectJsCompiler()
                .compileSuperclassClosureEsmTsFiles(
                        sourceRoots,
                        "com.qin.parser.QinParser",
                        List.of(
                                "com.slime.parser.cstToAst.SlimeCstToAstUtils",
                                "com.slime.parser.cstToAst.SlimeAstCreateUtils"),
                        outputRoot);

        Map<String, QinJavaProjectJsCompiler.EsmFileOutput> byBinaryName = outputs.stream()
                .collect(Collectors.toMap(QinJavaProjectJsCompiler.EsmFileOutput::binaryName, output -> output));
        QinJavaProjectJsCompiler.EsmFileOutput parserOutput = byBinaryName.get("com.qin.parser.QinParser");
        require(parserOutput != null, "QinParser TS ESM output");
        require(parserOutput.outputFile().endsWith(Path.of("com", "qin", "parser", "QinParser.ts")),
                "QinParser Java file maps to matching TS path");
        require(parserOutput.code().contains("class com_qin_parser_QinParser extends com_slime_parser_SlimeParser"),
                "QinParser generated class extends generated SlimeParser");
        require(parserOutput.code().contains("QinObjectDeclaration"),
                "Qin object PEG rule is present in generated TS parser");

        Path packageJson = outputRoot.resolve("package.json");
        Path qinConfig = outputRoot.resolve("qin.config.js");
        Path indexTs = outputRoot.resolve("index.ts");
        require(Files.isRegularFile(packageJson), "generated Qin parser package.json");
        require(Files.isRegularFile(qinConfig), "generated Qin parser qin.config.js");
        require(Files.isRegularFile(indexTs), "generated Qin parser index.ts");

        String packageJsonText = Files.readString(packageJson, StandardCharsets.UTF_8);
        String qinConfigText = Files.readString(qinConfig, StandardCharsets.UTF_8);
        String indexText = Files.readString(indexTs, StandardCharsets.UTF_8);
        require(packageJsonText.contains("\"name\": \"@qin/generated-qin-parser-ts\""),
                "generated package has stable Qin parser npm name");
        require(packageJsonText.contains("\"entryBinaryName\": \"com.qin.parser.QinParser\""),
                "generated package records QinParser entry binary name");
        require(qinConfigText.contains("entry: \"./index.ts\""),
                "generated qin.config.js points to TS package entry");
        require(indexText.contains("export default com_qin_parser_QinParser"),
                "generated package index exports raw QinParser class as default");
        require(indexText.contains("com_slime_parser_cstToAst_SlimeCstToAstUtils as SlimeCstToAstUtils"),
                "generated package index named-exports SlimeCstToAstUtils");
        require(indexText.contains("com_slime_parser_cstToAst_SlimeAstCreateUtils as SlimeAstCreateUtils"),
                "generated package index named-exports SlimeAstCreateUtils");
        require(Files.isRegularFile(outputRoot.resolve("com").resolve("slime").resolve("parser")
                        .resolve("cstToAst").resolve("SlimeCstToAstUtils.ts")),
                "generated SlimeCstToAstUtils TS output");
        require(Files.isRegularFile(outputRoot.resolve("com").resolve("slime").resolve("parser")
                        .resolve("cstToAst").resolve("SlimeAstCreateUtils.ts")),
                "generated SlimeAstCreateUtils TS output");

        Path smokeRoot = Files.createTempDirectory("qin-generated-qin-parser-ts-smoke-");
        Files.writeString(smokeRoot.resolve("qin.config.js"), """
                export default {
                  name: "qin-generated-qin-parser-ts-smoke",
                  type: "library",
                  entry: "main.ts",
                  packageOverrides: {
                    "@qin/generated-qin-parser-ts": "%s",
                    "@qin/java-sdk-js": "%s"
                  }
                }
                """.formatted(
                jsPath(outputRoot),
                jsPath(outputRoot.resolve("node_modules").resolve("@qin").resolve("java-sdk-js"))),
                StandardCharsets.UTF_8);

        Object result = new QinJsPackageRunner().runModuleSource(smokeRoot, """
                import QinParser from "@qin/generated-qin-parser-ts";
                import {
                  SlimeAstCreateUtils,
                  SlimeCstToAstUtils,
                  com_slime_parser_SlimeJavascriptParser$SourceType as SourceType
                } from "@qin/generated-qin-parser-ts";

                const parser = new QinParser("export object Counter { value = 1; next() { return this.value + 1; } }");
                const cst = parser.Program(SourceType.__qin_field_MODULE);
                function collectNames(node, names) {
                  if (!node) return names;
                  names.push(node.getName());
                  const children = node.getChildren();
                  if (children) {
                    for (const child of children) collectNames(child, names);
                  }
                  return names;
                }
                const names = collectNames(cst, []);
                ({
                  cstName: cst ? cst.getName() : null,
                  hasQinObjectDeclaration: names.includes("QinObjectDeclaration") || names.includes("QinObjectDeclarationBody"),
                  hasQinObjectBody: names.includes("QinObjectDeclarationBody"),
                  hasSlimeCstToAstUtilsExport: typeof SlimeCstToAstUtils === "function",
                  hasSlimeAstCreateUtilsExport: typeof SlimeAstCreateUtils === "function",
                  names: names.slice(0, 40).join(",")
                });
                """, "generated_qin_parser_object");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected generated Qin parser smoke result object, got: " + result);
        }
        require("Program".equals(map.get("cstName")), "generated Qin parser returns Program CST");
        require(Boolean.TRUE.equals(map.get("hasQinObjectDeclaration")),
                "generated Qin parser recognizes Qin object declaration");
        require(Boolean.TRUE.equals(map.get("hasQinObjectBody")),
                "generated Qin parser recognizes Qin object declaration");
        require(Boolean.TRUE.equals(map.get("hasSlimeCstToAstUtilsExport")),
                "generated Qin parser package exports SlimeCstToAstUtils");
        require(Boolean.TRUE.equals(map.get("hasSlimeAstCreateUtilsExport")),
                "generated Qin parser package exports SlimeAstCreateUtils");

        System.out.println("Generated ESM TS files: " + outputRoot);
        System.out.println("Generated ESM TS npm package: @qin/generated-qin-parser-ts");
        System.out.println("QinJavaProjectQinParserTsEsmFilesSmokeTestMain OK");
    }

    private static Path findQinRoot() {
        Path search = Path.of("").toAbsolutePath().normalize();
        while (search != null) {
            if (Files.isDirectory(search.resolve("packages").resolve("qin-runtime-core"))
                    && Files.isDirectory(search.resolve("packages").resolve("qin-parser"))
                    && Files.isRegularFile(search.resolve("qin.config.js"))) {
                return search;
            }
            search = search.getParent();
        }
        throw new IllegalStateException("Cannot find Qin repo root");
    }

    private static String jsPath(Path path) {
        return path.toAbsolutePath().normalize().toString().replace('\\', '/');
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
