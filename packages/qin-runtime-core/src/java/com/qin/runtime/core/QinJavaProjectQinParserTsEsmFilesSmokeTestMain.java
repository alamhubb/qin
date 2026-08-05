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
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-ast")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-token")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-java")
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
                                "com.slime.parser.cstToAst.SlimeAstCreateUtils",
                                "com.slime.java.ast.JavaCstToAst"),
                        outputRoot);
        QinGeneratedTsStaticAdmissionAudit.Result staticAdmissionAudit =
                QinGeneratedTsStaticAdmissionAudit.audit(outputs);
        QinGeneratedTsStaticAdmissionAudit.assertRejectsUnprovenDynamicShapes();
        require(staticAdmissionAudit.allowedDynamicWrapperCount() > 0,
                "generated Qin parser TS static admission audit classifies proven wrapper shapes");
        require(staticAdmissionAudit.legacyAllowedDynamicWrapperCount() == 0,
                "generated Qin parser TS static admission audit has no legacy dynamic wrapper admissions");

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
        require(packageJsonText.contains("\"@qin/java-sdk-js\": \"file:../java-sdk-js\""),
                "generated package depends on sibling java-sdk-js package");
        require(!packageJsonText.contains("node_modules/@qin/java-sdk-js"),
                "generated package must not depend on embedded node_modules java-sdk-js");
        require(qinConfigText.contains("entry: \"./index.ts\""),
                "generated qin.config.js points to TS package entry");
        require(indexText.contains("export default com_qin_parser_QinParser"),
                "generated package index exports raw QinParser class as default");
        require(!indexText.contains("com_subhuti_parser_Alternative as Alternative"),
                "generated package index does not root-export legacy Subhuti Alternative");
        require(!indexText.contains("SlimeCstToAstUtils"),
                "generated package index does not eager-export SlimeCstToAstUtils");
        require(!indexText.contains("SlimeAstCreateUtils"),
                "generated package index does not eager-export SlimeAstCreateUtils");
        require(!indexText.contains("JavaCstToAst"),
                "generated package index does not eager-export JavaCstToAst");
        require(packageJsonText.contains("\"./SlimeCstToAstUtils\""),
                "generated package subpath-exports SlimeCstToAstUtils");
        require(packageJsonText.contains("\"./SlimeAstCreateUtils\""),
                "generated package subpath-exports SlimeAstCreateUtils");
        require(packageJsonText.contains("\"./JavaCstToAst\""),
                "generated package subpath-exports JavaCstToAst");
        require(packageJsonText.contains("\"./SlimeCstToAstBridge\""),
                "generated package subpath-exports SlimeCstToAstBridge");
        require(packageJsonText.contains("\"./SubhutiCst\""),
                "generated package subpath-exports SubhutiCst");
        require(packageJsonText.contains("\"./SubhutiSourceLocation\""),
                "generated package subpath-exports SubhutiSourceLocation");
        require(packageJsonText.contains("\"./SubhutiPosition\""),
                "generated package subpath-exports SubhutiPosition");
        require(Files.isRegularFile(outputRoot.resolve("com").resolve("slime").resolve("parser")
                        .resolve("cstToAst").resolve("SlimeCstToAstUtils.ts")),
                "generated SlimeCstToAstUtils TS output");
        require(Files.isRegularFile(outputRoot.resolve("com").resolve("slime").resolve("parser")
                        .resolve("cstToAst").resolve("SlimeAstCreateUtils.ts")),
                "generated SlimeAstCreateUtils TS output");
        String slimeAstCreateUtilsText = Files.readString(outputRoot.resolve("com").resolve("slime").resolve("parser")
                        .resolve("cstToAst").resolve("SlimeAstCreateUtils.ts"),
                StandardCharsets.UTF_8);
        require(slimeAstCreateUtilsText.contains(
                        "static resolveSourceLocation(cst: com_subhuti_struct_SubhutiCst): com_slime_ast_SourceLocation"),
                "generated SlimeAstCreateUtils preserves SourceLocation return type");
        require(slimeAstCreateUtilsText.contains(
                        "let location: com_slime_ast_SourceLocation = com_slime_parser_cstToAst_SlimeAstCreateUtils.resolveSourceLocation(cst);"),
                "generated SlimeAstCreateUtils preserves SourceLocation local type");
        require(!slimeAstCreateUtilsText.contains("let location: any = com_slime_parser_cstToAst_SlimeAstCreateUtils.resolveSourceLocation(cst);"),
                "generated SlimeAstCreateUtils does not widen SourceLocation local to any");
        require(Files.isRegularFile(outputRoot.resolve("com").resolve("slime").resolve("java")
                        .resolve("ast").resolve("JavaCstToAst.ts")),
                "generated JavaCstToAst TS output");
        require(Files.isRegularFile(outputRoot.resolve("SlimeCstToAstBridge.ts")),
                "generated SlimeCstToAstBridge TS output");

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
                jsPath(outputRoot.getParent().resolve("java-sdk-js"))),
                StandardCharsets.UTF_8);

        Object result = new QinJsPackageRunner().runModuleSource(smokeRoot, """
                import QinParser from "@qin/generated-qin-parser-ts";
                import {
                  com_slime_parser_SlimeJavascriptParser$SourceType as SourceType
                } from "@qin/generated-qin-parser-ts";
                import { com_slime_parser_cstToAst_SlimeCstToAstUtils as SlimeCstToAstUtils } from "@qin/generated-qin-parser-ts/SlimeCstToAstUtils";
                import { com_slime_parser_cstToAst_SlimeAstCreateUtils as SlimeAstCreateUtils } from "@qin/generated-qin-parser-ts/SlimeAstCreateUtils";
                import { com_slime_java_ast_JavaCstToAst as JavaCstToAst } from "@qin/generated-qin-parser-ts/JavaCstToAst";
                import { com_subhuti_struct_SubhutiCst as SubhutiCst } from "@qin/generated-qin-parser-ts/SubhutiCst";
                import { com_subhuti_struct_SubhutiSourceLocation as SubhutiSourceLocation } from "@qin/generated-qin-parser-ts/SubhutiSourceLocation";
                import {
                  SlimeCstToAst,
                  SlimeCstToAstUtils as SlimeCstToAstBridgeUtils,
                  registerSlimeCstToAstUtil
                } from "@qin/generated-qin-parser-ts/SlimeCstToAstBridge";

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
                class CustomCstToAst extends SlimeCstToAst {
                  createPrimaryExpressionAst(cst: SubhutiCst) {
                    return { type: "CustomPrimaryExpression", name: cst.getName() };
                  }
                }
                const custom = new CustomCstToAst();
                registerSlimeCstToAstUtil(custom);
                const bridgeSmokeCst = SubhutiCst.builder().name("BridgeSmokePrimaryExpression").build();
                const bridgeAst = SlimeCstToAstBridgeUtils.createPrimaryExpressionAst(bridgeSmokeCst);
                const javaProgram = JavaCstToAst.parse("package demo; public class Greeter { public static String DEFAULT_NAME = \\\"Qin\\\"; public static String greet(String name) { return name; } }");
                const javaClass = javaProgram.classes().get(0);
                const names = collectNames(cst, []);
                ({
                  cstName: cst ? cst.getName() : null,
                  hasQinObjectDeclaration: names.includes("QinObjectDeclaration") || names.includes("QinObjectDeclarationBody"),
                  hasQinObjectBody: names.includes("QinObjectDeclarationBody"),
                  hasSlimeCstToAstUtilsExport: typeof SlimeCstToAstUtils === "function",
                  hasSlimeAstCreateUtilsExport: typeof SlimeAstCreateUtils === "function",
                  hasJavaCstToAstExport: typeof JavaCstToAst === "function",
                  javaClassName: javaClass.name(),
                  javaStaticFieldName: javaClass.fields().get(0).name(),
                  javaStaticMethodName: javaClass.methods().get(0).name(),
                  hasSubhutiSourceLocationExport: typeof SubhutiSourceLocation === "function",
                  hasSlimeCstToAstBridgeExport: typeof SlimeCstToAst === "function"
                    && typeof registerSlimeCstToAstUtil === "function",
                  bridgeDispatchType: bridgeAst.type,
                  bridgeDispatchName: bridgeAst.name,
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
        require(Boolean.TRUE.equals(map.get("hasJavaCstToAstExport")),
                "generated Qin parser package exports JavaCstToAst");
        require("Greeter".equals(map.get("javaClassName")),
                "generated JavaCstToAst parses Java class name");
        require("DEFAULT_NAME".equals(map.get("javaStaticFieldName")),
                "generated JavaCstToAst parses Java static field name");
        require("greet".equals(map.get("javaStaticMethodName")),
                "generated JavaCstToAst parses Java static method name");
        require(Boolean.TRUE.equals(map.get("hasSubhutiSourceLocationExport")),
                "generated Qin parser package exports SubhutiSourceLocation");
        require(Boolean.TRUE.equals(map.get("hasSlimeCstToAstBridgeExport")),
                "generated Qin parser package exports SlimeCstToAstBridge");
        require("CustomPrimaryExpression".equals(map.get("bridgeDispatchType")),
                "generated SlimeCstToAstBridge facade dispatches to registered subclass");
        require("BridgeSmokePrimaryExpression".equals(map.get("bridgeDispatchName")),
                "generated SlimeCstToAstBridge forwards arguments to registered subclass");

        System.out.println("Generated ESM TS files: " + outputRoot);
        System.out.println("Generated ESM TS npm package: @qin/generated-qin-parser-ts");
        System.out.println("Generated Qin parser TS static admission wrappers: "
                + staticAdmissionAudit.allowedDynamicWrapperCount());
        System.out.println("Generated Qin parser TS static admission contract wrappers: "
                + staticAdmissionAudit.contractAllowedDynamicWrapperCount());
        System.out.println("Generated Qin parser TS static admission legacy wrappers: "
                + staticAdmissionAudit.legacyAllowedDynamicWrapperCount());
        System.out.println("Generated Qin parser TS static admission legacy reasons: "
                + staticAdmissionAudit.legacyAllowedDynamicWrapperReasons());
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
