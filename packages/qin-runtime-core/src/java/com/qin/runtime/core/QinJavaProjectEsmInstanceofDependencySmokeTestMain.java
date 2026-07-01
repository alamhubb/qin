package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class QinJavaProjectEsmInstanceofDependencySmokeTestMain {
    private QinJavaProjectEsmInstanceofDependencySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = findQinRoot();
        Path workspaceRoot = qinRoot.getParent();
        List<Path> sourceRoots = List.of(
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-parser")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-token")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-ast")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("subhuti-java")
                        .resolve("src").resolve("main").resolve("java"));
        Path outputRoot = Files.createTempDirectory("qin-java-project-esm-instanceof-output-");

        QinJavaProjectJsCompiler compiler = new QinJavaProjectJsCompiler();
        Map<String, Path> sourceFiles = compiler.superclassSourceFiles(
                sourceRoots,
                "com.slime.parser.cstToAst.SlimeCstToAstUtils");
        if (!sourceFiles.containsKey("com.slime.parser.cstToAst.SlimeCstToAstUtils")) {
            throw new IllegalStateException("Expected SlimeCstToAstUtils source discovery from roots "
                    + sourceRoots + ", got: " + sourceFiles.keySet());
        }
        require(sourceFiles.containsKey("com.slime.ast.nodes.declarations.VariableDeclaration"),
                "instanceof pattern target source discovery");

        List<QinJavaProjectJsCompiler.EsmFileOutput> outputs = compiler
                .compileSuperclassClosureEsmTsFiles(
                        sourceRoots,
                        "com.slime.parser.cstToAst.SlimeCstToAstUtils",
                        outputRoot);
        Map<String, QinJavaProjectJsCompiler.EsmFileOutput> byBinaryName = outputs.stream()
                .collect(Collectors.toMap(QinJavaProjectJsCompiler.EsmFileOutput::binaryName, output -> output));

        QinJavaProjectJsCompiler.EsmFileOutput utils = byBinaryName.get(
                "com.slime.parser.cstToAst.SlimeCstToAstUtils");
        if (utils == null) {
            throw new IllegalStateException("Expected SlimeCstToAstUtils generated output, got: "
                    + byBinaryName.keySet());
        }
        require(byBinaryName.containsKey("com.slime.ast.nodes.declarations.VariableDeclaration"),
                "instanceof pattern target source file included in generated ESM outputs");
        QinJavaProjectJsCompiler.EsmFileOutput importDeclaration = byBinaryName.get(
                "com.slime.ast.nodes.modules.ImportDeclaration");
        if (importDeclaration == null) {
            throw new IllegalStateException("Expected ImportDeclaration generated output, got: "
                    + byBinaryName.keySet());
        }
        require(importDeclaration.code().contains(
                        "__qin_java_interfaces = [\"com.slime.ast.ModuleItem\", \"com.slime.ast.AstNode\"]"),
                "generated record interface metadata expands parent interfaces for ModuleItem");
        require(utils.code().contains(
                        "import { com_slime_ast_nodes_declarations_VariableDeclaration"),
                "SlimeCstToAstUtils imports wildcard instanceof pattern target module");
        require(utils.code().contains("__qin_pattern_value instanceof com_slime_ast_nodes_declarations_VariableDeclaration"),
                "instanceof pattern uses imported VariableDeclaration binding");

        System.out.println("QinJavaProjectEsmInstanceofDependencySmokeTestMain OK");
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

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
