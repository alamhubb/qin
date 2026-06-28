package com.qin.debug.lsp;

import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
import com.qin.types.LanguageServerConfig;
import com.qin.types.QinConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class QinLspVerificationMatrixSmokeTestMain {
    private static final String GENERATED_QIN_PARSER_PACKAGE = "@qin/generated-qin-parser-ts";

    private QinLspVerificationMatrixSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path workspaceRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of("."));
        Path ideaClientPath = workspaceRoot.resolve("qin")
                .resolve("packages")
                .resolve("qin-idea-plugin-debug")
                .normalize();
        Path generatedQinParserRoot = workspaceRoot.resolve("qin")
                .resolve("packages")
                .resolve("qin-language")
                .resolve("generated")
                .resolve("qin-parser-ts")
                .normalize();

        List<MatrixCase> cases = List.of(
                new MatrixCase("qin", ".qin", Path.of("qin", "packages", "qin-language"),
                        null, "dist/language-server.cjs", "generated/qin-parser-ts", null,
                        "com.qin:qin-parser", null,
                        "tests/test-language-server.ts",
                        "tsx tests/test-language-plugin.ts",
                        "tsx tests/test-generated-parser-parity.ts",
                        "tsx tests/test-language-server.ts"),
                new MatrixCase("ovs", ".ovs", Path.of("ovsjs", "ovs-language"),
                        Path.of("ovsjs", "ovs", "ovs-compiler"),
                        "dist/language-server.js", "@qin/generated-qin-parser-ts", "../ovs/ovs-compiler",
                        null, "ovs-compiler",
                        "tests/test-language-server.ts",
                        "tsx tests/test-generated-parser-chain.ts",
                        "tsx tests/test-language-server.ts --source",
                        "tsx tests/test-language-server.ts --dist"),
                new MatrixCase("cssts", ".cssts", Path.of("cssts", "cssts-language"),
                        Path.of("cssts", "cssts", "cssts-compiler"),
                        "dist/language-server.cjs", "@qin/generated-qin-parser-ts", "../cssts/cssts-compiler",
                        null, "cssts-compiler",
                        "tests/test-language-server.ts",
                        "tsx tests/test-generated-parser-chain.ts",
                        "tsx tests/test-language-server.ts"));

        for (MatrixCase matrixCase : cases) {
            Path projectRoot = workspaceRoot.resolve(matrixCase.projectRelativePath()).normalize();
            QinConfig config = new ConfigLoader(projectRoot.toString()).load();
            LanguageConfig language = config.language();
            require(language != null, matrixCase.id() + " must declare language metadata");
            require(matrixCase.id().equals(language.id()), matrixCase.id() + " language.id mismatch");
            require(matrixCase.extension().equals(language.extension()), matrixCase.id() + " language.extension mismatch");
            require(matrixCase.serverBundle().equals(language.serverBundle()),
                    matrixCase.id() + " language.serverBundle mismatch");
            require(projectRoot.resolve(language.serverBundle()).normalize().startsWith(workspaceRoot),
                    matrixCase.id() + " server bundle must stay inside workspace");
            require(Files.isRegularFile(projectRoot.resolve(language.serverBundle()).normalize()),
                    matrixCase.id() + " server bundle must exist before IDEA smoke runs");
            verifyLanguageToolReferences(matrixCase, projectRoot, workspaceRoot, config, language);
            verifyLanguageServerMetadata(matrixCase, config);

            Path resolvedIdeaClient = projectRoot.resolve(language.ideaLspClient()).normalize();
            require(ideaClientPath.equals(resolvedIdeaClient),
                    matrixCase.id() + " ideaLspClient must point to qin-idea-plugin-debug");

            String buildScript = config.scripts().get("build");
            String testScript = config.scripts().get("test");
            require("tsdown".equals(buildScript), matrixCase.id() + " scripts.build must run tsdown directly");
            require(testScript != null && !testScript.isBlank(), matrixCase.id() + " scripts.test is required");
            require(!testScript.contains("npm run"),
                    matrixCase.id() + " scripts.test must run checks directly through Qin scripts");
            for (String requiredPart : matrixCase.requiredTestScriptParts()) {
                require(testScript.contains(requiredPart),
                        matrixCase.id() + " scripts.test missing " + requiredPart + ": " + testScript);
            }
            verifyLanguageServerFeatureAssertions(matrixCase, projectRoot);

            if ("qin".equals(matrixCase.id())) {
                verifyGeneratedQinParserPackage(projectRoot, config);
                verifyGeneratedParserParityCorpus(projectRoot);
            } else {
                verifyGeneratedParserDependency(
                        matrixCase.id(),
                        projectRoot,
                        generatedQinParserRoot,
                        config);
            }
            if (matrixCase.compilerProjectRelativePath() != null) {
                verifyCompilerProjectConfig(matrixCase, workspaceRoot, generatedQinParserRoot);
            }
        }
        verifyOvsCsstsGeneratedParserInheritance(workspaceRoot);

        Path buildFile = ideaClientPath.resolve("build.gradle.kts");
        String buildSource = Files.readString(buildFile);
        Map<String, String> languageTestTasks = Map.of(
                "qinLanguageTest", "qin/packages/qin-language",
                "ovsLanguageTest", "ovsjs/ovs-language",
                "csstsLanguageTest", "cssts/cssts-language");
        for (Map.Entry<String, String> task : languageTestTasks.entrySet()) {
            require(buildSource.contains("register<Exec>(\"" + task.getKey() + "\")"),
                    "Gradle must declare " + task.getKey());
            require(buildSource.contains(task.getValue()),
                    task.getKey() + " must run from " + task.getValue());
        }
        require(buildSource.contains("register<Exec>(\"qinGeneratedParserDryRun\")"),
                "Gradle must declare qinGeneratedParserDryRun");
        require(buildSource.contains("qin/packages/qin-language"),
                "qinGeneratedParserDryRun must run from qin/packages/qin-language");
        require(buildSource.contains("\"language\", \"generate-parser\", \"--dry-run\""),
                "qinGeneratedParserDryRun must verify QinParser Java -> TypeScript generation metadata");
        require(buildSource.contains("register<JavaExec>(\"lspLanguageCliSmoke\")"),
                "Gradle must declare lspLanguageCliSmoke");
        require(buildSource.contains("QinLspLanguageCliSmokeTestMain"),
                "lspLanguageCliSmoke must run QinLspLanguageCliSmokeTestMain");
        verifyStableLspSmokeJvmArgs(buildSource);
        require(buildSource.contains("register<Exec>(\"qinLanguageLocalDependencyBuildSmoke\")"),
                "Gradle must declare qinLanguageLocalDependencyBuildSmoke");
        String localDependencyBuildBlock = taskBlock(
                buildSource,
                "register<Exec>(\"qinLanguageLocalDependencyBuildSmoke\")");
        require(localDependencyBuildBlock.contains("workspaceRoot.resolve(\"qin\")"),
                "qinLanguageLocalDependencyBuildSmoke must run from the Qin project root");
        require(localDependencyBuildBlock.contains("QinCliLanguageLocalDependencyBuildSmokeTestMain"),
                "qinLanguageLocalDependencyBuildSmoke must verify local file dependency builds through Qin CLI");
        require(buildSource.contains("register(\"languageProjectsTest\")"),
                "Gradle must declare languageProjectsTest");
        require(buildSource.contains("register(\"compilerProjectsTest\")"),
                "Gradle must declare compilerProjectsTest");
        require(buildSource.contains("register<Exec>(\"ovsCompilerTest\")"),
                "Gradle must declare ovsCompilerTest");
        require(buildSource.contains("ovsjs/ovs/ovs-compiler"),
                "ovsCompilerTest must run from ovsjs/ovs/ovs-compiler");
        require(buildSource.contains("register<Exec>(\"csstsCompilerTest\")"),
                "Gradle must declare csstsCompilerTest");
        require(buildSource.contains("cssts/cssts/cssts-compiler"),
                "csstsCompilerTest must run from cssts/cssts/cssts-compiler");
        String ovsCompilerBlock = taskBlock(buildSource, "register<Exec>(\"ovsCompilerTest\")");
        require(ovsCompilerBlock.contains("dependsOn(\"csstsCompilerTest\")"),
                "ovsCompilerTest must run after csstsCompilerTest so local cssts-compiler dist is refreshed");
        String ovsLanguageBlock = taskBlock(buildSource, "register<Exec>(\"ovsLanguageTest\")");
        require(ovsLanguageBlock.contains("dependsOn(\"csstsLanguageTest\")"),
                "ovsLanguageTest must run after csstsLanguageTest so local CSSTS language artifacts are refreshed");
        require(buildSource.contains("register(\"lspUnifiedMatrix\")"),
                "Gradle must declare lspUnifiedMatrix");
        String lspUnifiedMatrixBlock = taskBlock(buildSource, "register(\"lspUnifiedMatrix\")");
        require(lspUnifiedMatrixBlock.contains("dependsOn(\"qinLanguageLocalDependencyBuildSmoke\")"),
                "lspUnifiedMatrix must include Qin local file dependency build smoke");
        require(buildSource.contains("register<Exec>(\"qinJvmClassTargetSmoke\")"),
                "Gradle must declare qinJvmClassTargetSmoke");
        require(buildSource.contains("qin/packages/qin-lang-cli"),
                "qinJvmClassTargetSmoke must run from qin/packages/qin-lang-cli");
        require(buildSource.contains("\"run\", \"com.qin.lang.cli.SmokeTestMain\""),
                "qinJvmClassTargetSmoke must run the Qin CLI JVM .class smoke");
        require(buildSource.contains("register<Exec>(\"qinJvmClassDeclarationSmoke\")"),
                "Gradle must declare qinJvmClassDeclarationSmoke");
        require(buildSource.contains("qin/packages/qin-lang-backend-jvm"),
                "qinJvmClassDeclarationSmoke must run from qin/packages/qin-lang-backend-jvm");
        require(buildSource.contains("\"run\", \"com.qin.lang.backend.jvm.QinJvmClassDeclarationCorpusSmokeTestMain\""),
                "qinJvmClassDeclarationSmoke must run the Qin JVM class declaration corpus smoke");
        verifyRuntimeClassSmokeTask(
                buildSource,
                "qinJvmClassTargetSmoke",
                "qin/packages/qin-lang-cli",
                "com.qin.lang.cli.SmokeTestMain");
        verifyRuntimeClassSmokeTask(
                buildSource,
                "qinJvmClassDeclarationSmoke",
                "qin/packages/qin-lang-backend-jvm",
                "com.qin.lang.backend.jvm.QinJvmClassDeclarationCorpusSmokeTestMain");
        verifyRuntimeProjectClassTarget(
                workspaceRoot,
                "qin-lang-cli",
                Path.of("qin", "packages", "qin-lang-cli"),
                "src/java/com/qin/lang/cli/QinCompileMain.java");
        verifyRuntimeProjectClassTarget(
                workspaceRoot,
                "qin-lang-backend-jvm",
                Path.of("qin", "packages", "qin-lang-backend-jvm"),
                "src/java/com/qin/lang/backend/jvm/QinJvmClassFileBackend.java");
        verifyRuntimeProjectClassTarget(
                workspaceRoot,
                "qin-runtime-core",
                Path.of("qin", "packages", "qin-runtime-core"),
                "src/java/com/qin/runtime/core/QinRuntimeMain.java");
        verifyJvmClassDeclarationCorpus(workspaceRoot);
        String checkBlock = taskBlock(buildSource, "named(\"check\")");
        String matrixBlock = taskBlock(buildSource, "register(\"lspUnifiedMatrix\")");
        for (String smokeTask : List.of(
                "qinJvmClassTargetSmoke",
                "qinJvmClassDeclarationSmoke",
                "lspUnifiedMatrix")) {
            require(checkBlock.contains("dependsOn(\"" + smokeTask + "\")"),
                    "Gradle check must depend on " + smokeTask);
        }
        for (String matrixTask : List.of(
                "languageProjectsTest",
                "compilerProjectsTest",
                "qinGeneratedParserDryRun",
                "lspRegistrySmoke",
                "lspServerCommandLineSmoke",
                "lspServerDiagnosticsSmoke",
                "lspLanguageCliSmoke",
                "lspVerificationMatrixSmoke",
                "lspPluginDescriptorSmoke",
                "lspNoLocalParserSmoke",
                "lspWorkspaceInventorySmoke",
                "lspPluginPackageSmoke",
                "lspUiFixtureSmoke")) {
            require(matrixBlock.contains("dependsOn(\"" + matrixTask + "\")"),
                    "lspUnifiedMatrix must depend on " + matrixTask);
        }
        verifyCompletionAuditDocument(ideaClientPath.resolve("LSP_COMPLETION_AUDIT.md"));
        verifyLanguageCliSmokeCoverage(ideaClientPath);
        verifyIdeaDiagnosticsSmokeFeatureAssertions(ideaClientPath);

        System.out.println("Qin LSP verification matrix smoke passed");
    }

    private static void verifyLanguageCliSmokeCoverage(Path ideaClientPath) throws Exception {
        Path smokePath = ideaClientPath.resolve("src")
                .resolve("main")
                .resolve("java")
                .resolve("com")
                .resolve("qin")
                .resolve("debug")
                .resolve("lsp")
                .resolve("QinLspLanguageCliSmokeTestMain.java")
                .normalize();
        require(Files.isRegularFile(smokePath),
                "Qin LSP language CLI smoke source must exist: " + smokePath);
        String smokeSource = Files.readString(smokePath);
        for (String projectNeedle : List.of(
                "ovsjs-workspace",
                "cssts-workspace",
                "ovs-runtime",
                "vite-plugin-ovs",
                "create-ovs",
                "cssts-runtime",
                "vite-plugin-cssts",
                "language-plugin-cssts",
                "create-cssts",
                "cssts-theme-element")) {
            require(smokeSource.contains("\"" + projectNeedle + "\""),
                    "lspLanguageCliSmoke must cover Qin-managed tooling project " + projectNeedle);
        }
        for (String scriptNeedle : List.of(
                "\"build\", \"tsdown\"",
                "\"test\", \"vitest run\"",
                "\"test\", \"node test-transform-error.cjs\"",
                "\"build\", \"language build --root ovs/ovs-runtime\"",
                "\"test\", \"language test --root ovs/ovs-runtime\"",
                "\"build\", \"language build --root cssts/cssts-runtime\"",
                "\"test\", \"language test --root cssts/cssts-runtime\"")) {
            require(smokeSource.contains(scriptNeedle),
                    "lspLanguageCliSmoke must verify tooling script dry-run needle " + scriptNeedle);
        }
        require(smokeSource.contains("private record ScriptNeedle"),
                "lspLanguageCliSmoke must allow multiple script dry-run needles per command");
    }

    private static void verifyStableLspSmokeJvmArgs(String buildSource) {
        require(buildSource.contains("val stableSmokeJvmArgs = listOf("),
                "Gradle must centralize LSP smoke JVM args");
        require(buildSource.contains("\"-Xmx256m\""),
                "LSP smoke JVM args must cap heap size for stable local matrix execution");
        require(buildSource.contains("\"-Dfile.encoding=UTF-8\""),
                "LSP smoke JVM args must force UTF-8 file encoding");
        require(buildSource.contains("\"-Dstdout.encoding=UTF-8\""),
                "LSP smoke JVM args must force UTF-8 stdout encoding");
        require(buildSource.contains("\"-Dstderr.encoding=UTF-8\""),
                "LSP smoke JVM args must force UTF-8 stderr encoding");
        require(buildSource.contains("\"-XX:-UseJVMCICompiler\""),
                "LSP smoke JVM args must disable the GraalVM JVMCI compiler for stable smoke execution");

        for (String taskName : List.of(
                "lspRegistrySmoke",
                "lspServerDiagnosticsSmoke",
                "lspVerificationMatrixSmoke",
                "lspServerCommandLineSmoke",
                "lspLanguageCliSmoke",
                "lspPluginDescriptorSmoke",
                "lspNoLocalParserSmoke",
                "lspWorkspaceInventorySmoke",
                "lspPluginPackageSmoke",
                "lspUiFixtureSmoke")) {
            String taskBlock = taskBlock(buildSource, "register<JavaExec>(\"" + taskName + "\")");
            require(taskBlock.contains("jvmArgs(stableSmokeJvmArgs)"),
                    taskName + " must use stableSmokeJvmArgs");
        }
    }

    private static void verifyIdeaDiagnosticsSmokeFeatureAssertions(Path ideaClientPath) throws Exception {
        Path smokePath = ideaClientPath.resolve("src")
                .resolve("main")
                .resolve("java")
                .resolve("com")
                .resolve("qin")
                .resolve("debug")
                .resolve("lsp")
                .resolve("QinLspServerDiagnosticsSmokeTestMain.java")
                .normalize();
        require(Files.isRegularFile(smokePath),
                "Qin IDEA LSP diagnostics smoke source must exist: " + smokePath);
        String smokeSource = Files.readString(smokePath);
        for (String method : List.of(
                "textDocument/completion",
                "textDocument/definition",
                "textDocument/references",
                "textDocument/documentSymbol",
                "textDocument/semanticTokens/full")) {
            require(smokeSource.contains(method),
                    "IDEA diagnostics smoke must request " + method);
        }
        for (String assertionNeedle : List.of(
                "completion missing",
                "definition did not resolve",
                "references did not include declaration and usage",
                "documentSymbol missing",
                "semanticTokens returned no token data")) {
            require(smokeSource.contains(assertionNeedle),
                    "IDEA diagnostics smoke must assert " + assertionNeedle);
        }
        require(smokeSource.contains("hasLocationStartingAt"),
                "IDEA diagnostics smoke must verify reference source positions");
        require(smokeSource.contains("rangeMap.get(\"start\")"),
                "IDEA diagnostics smoke must inspect LSP reference ranges");
    }

    private static void verifyCompilerProjectConfig(
            MatrixCase matrixCase,
            Path workspaceRoot,
            Path generatedQinParserRoot) throws Exception {
        Path compilerRoot = workspaceRoot.resolve(matrixCase.compilerProjectRelativePath()).normalize();
        require(compilerRoot.startsWith(workspaceRoot),
                matrixCase.id() + " compiler project must stay inside workspace");
        require(Files.isRegularFile(compilerRoot.resolve("qin.config.js")),
                matrixCase.id() + " compiler project must have qin.config.js");

        QinConfig compilerConfig = new ConfigLoader(compilerRoot.toString()).load();
        LanguageConfig compilerLanguage = compilerConfig.language();
        require(compilerLanguage != null, matrixCase.id() + " compiler must declare language metadata");
        require(matrixCase.id().equals(compilerLanguage.id()),
                matrixCase.id() + " compiler language.id mismatch");
        require(matrixCase.extension().equals(compilerLanguage.extension()),
                matrixCase.id() + " compiler language.extension mismatch");
        require(matrixCase.expectedParser().equals(compilerLanguage.parser()),
                matrixCase.id() + " compiler language.parser mismatch");
        require("src/index.ts".equals(compilerLanguage.compiler()),
                matrixCase.id() + " compiler language.compiler must point at src/index.ts");
        verifyPathLikeOrPackageReference(
                matrixCase.id() + " compiler",
                "language.parser",
                compilerLanguage.parser(),
                compilerRoot,
                workspaceRoot,
                compilerConfig);
        verifyPathLikeOrPackageReference(
                matrixCase.id() + " compiler",
                "language.compiler",
                compilerLanguage.compiler(),
                compilerRoot,
                workspaceRoot,
                compilerConfig);

        String buildScript = compilerConfig.scripts().get("build");
        String testScript = compilerConfig.scripts().get("test");
        require("tsdown".equals(buildScript),
                matrixCase.id() + " compiler scripts.build must run tsdown directly");
        require(testScript != null && !testScript.isBlank(),
                matrixCase.id() + " compiler scripts.test is required");
        require(testScript.contains("tests/test-generated-parser-chain.ts"),
                matrixCase.id() + " compiler scripts.test must run the generated parser chain smoke");
        require(!testScript.contains("npm run"),
                matrixCase.id() + " compiler scripts.test must run checks directly through Qin scripts");
        verifyGeneratedParserDependency(
                matrixCase.id() + " compiler",
                compilerRoot,
                generatedQinParserRoot,
                compilerConfig);
    }

    private static void verifyOvsCsstsGeneratedParserInheritance(Path workspaceRoot) throws Exception {
        Path csstsCompilerRoot = workspaceRoot.resolve("cssts")
                .resolve("cssts")
                .resolve("cssts-compiler")
                .normalize();
        Path ovsCompilerRoot = workspaceRoot.resolve("ovsjs")
                .resolve("ovs")
                .resolve("ovs-compiler")
                .normalize();

        Path cssTsParserPath = csstsCompilerRoot.resolve("src")
                .resolve("parser")
                .resolve("CssTsParser.ts")
                .normalize();
        Path cssTsAdapterPath = csstsCompilerRoot.resolve("src")
                .resolve("parser")
                .resolve("generated-runtime-adapter.ts")
                .normalize();
        Path csstsTransformPath = csstsCompilerRoot.resolve("src")
                .resolve("transform")
                .resolve("index.ts")
                .normalize();
        Path ovsParserPath = ovsCompilerRoot.resolve("src")
                .resolve("parser")
                .resolve("OvsParser.ts")
                .normalize();
        Path ovsIndexPath = ovsCompilerRoot.resolve("src")
                .resolve("index.ts")
                .normalize();
        Path forbiddenOvsAdapterPath = ovsCompilerRoot.resolve("src")
                .resolve("parser")
                .resolve("generated-runtime-adapter.ts")
                .normalize();

        for (Path requiredPath : List.of(
                cssTsParserPath,
                cssTsAdapterPath,
                csstsTransformPath,
                ovsParserPath,
                ovsIndexPath)) {
            require(requiredPath.startsWith(workspaceRoot),
                    "Generated parser chain source must stay inside workspace: " + requiredPath);
            require(Files.isRegularFile(requiredPath),
                    "Generated parser chain source must exist: " + requiredPath);
        }

        String cssTsParser = Files.readString(cssTsParserPath);
        String cssTsAdapter = Files.readString(cssTsAdapterPath);
        String csstsTransform = Files.readString(csstsTransformPath);
        String ovsParser = Files.readString(ovsParserPath);
        String ovsIndex = Files.readString(ovsIndexPath);

        require(cssTsParser.contains("from \"@qin/generated-qin-parser-ts\""),
                "CSSTS parser must import the shared generated Qin parser package");
        require(cssTsParser.contains("QinParser"),
                "CSSTS parser must use the generated QinParser export");
        require(cssTsParser.contains("extends QinParser"),
                "CSSTS parser must extend the generated Qin parser base");
        require(cssTsParser.contains("normalizeGeneratedTokens"),
                "CSSTS parser must normalize generated parser tokens");
        require(cssTsParser.contains("this.Or("),
                "CSSTS parser must use generated parser Or semantics");
        require(!cssTsParser.contains("alt:"),
                "CSSTS parser must not use legacy { alt } fallback alternatives");

        require(cssTsAdapter.contains("normalizeGeneratedCst"),
                "CSSTS generated runtime adapter must expose normalizeGeneratedCst");
        require(cssTsAdapter.contains("javaListToArray"),
                "CSSTS generated runtime adapter must bridge generated Java list values");
        require(csstsTransform.contains("normalizeGeneratedCst(parser.Program())"),
                "CSSTS transform must normalize CST from the generated parser chain");

        require(ovsParser.contains("from \"@qin/generated-qin-parser-ts\""),
                "OVS parser must import the shared generated Qin parser package");
        require(ovsParser.contains("from \"cssts-compiler\""),
                "OVS parser must inherit CSSTS compiler parser support");
        require(ovsParser.contains("extends CssTsParser"),
                "OVS parser must extend CssTsParser");
        require(ovsParser.contains("QinObjectDeclaration(params)"),
                "OVS parser must preserve Qin declarations through the CSSTS/Qin parser chain");
        require(ovsParser.contains("normalizeGeneratedTokens"),
                "OVS parser must normalize generated parser tokens");
        require(ovsParser.contains("Alternative.of("),
                "OVS parser must use generated parser Alternative.of semantics");
        require(!ovsParser.contains("alt:"),
                "OVS parser must not use legacy { alt } fallback alternatives");
        require(ovsIndex.contains("normalizeGeneratedCst"),
                "OVS compiler transform must normalize CST from the generated parser chain");
        require(!Files.exists(forbiddenOvsAdapterPath),
                "OVS must inherit the generated runtime adapter from cssts-compiler, not keep a local copy");
    }

    private static void verifyLanguageServerFeatureAssertions(MatrixCase matrixCase, Path projectRoot) throws Exception {
        Path testFile = projectRoot.resolve(matrixCase.languageServerTest()).normalize();
        require(Files.isRegularFile(testFile),
                matrixCase.id() + " language server test must exist: " + testFile);
        String testSource = Files.readString(testFile);
        for (String method : List.of(
                "textDocument/completion",
                "textDocument/definition",
                "textDocument/references",
                "textDocument/documentSymbol",
                "textDocument/semanticTokens/full")) {
            require(testSource.contains(method),
                    matrixCase.id() + " language server test must request " + method);
        }
        for (String assertionNeedle : List.of(
                "completion did not include",
                "definition did not resolve",
                "references did not include",
                "documentSymbol did not include",
                "semanticTokens did not return token data")) {
            require(testSource.contains(assertionNeedle),
                    matrixCase.id() + " language server test must assert " + assertionNeedle);
        }
        for (String metadataNeedle : List.of(
                "initializationOptions",
                "generatedParserTarget: '" + GENERATED_QIN_PARSER_PACKAGE + "'")) {
            require(testSource.contains(metadataNeedle),
                    matrixCase.id() + " language server test must pass generated parser metadata: "
                            + metadataNeedle);
        }
        for (String syntaxNeedle : languageSyntaxFeatureNeedles(matrixCase.id())) {
            require(testSource.contains(syntaxNeedle),
                    matrixCase.id() + " language server test must cover language syntax feature: "
                            + syntaxNeedle);
        }
        require(testSource.contains("requireSemanticTokenAt"),
                matrixCase.id() + " language server test must assert semantic token source positions");
        for (String semanticNeedle : languageSemanticTokenNeedles(matrixCase.id())) {
            require(testSource.contains(semanticNeedle),
                    matrixCase.id() + " language server test must cover semantic token position: "
                            + semanticNeedle);
        }
    }

    private static List<String> languageSyntaxFeatureNeedles(String languageId) {
        return switch (languageId) {
            case "qin" -> List.of(
                    "Qin object completion response",
                    "Qin object definition response",
                    "Qin object references response",
                    "Qin object documentSymbol response",
                    "__QinObject_Counter",
                    "generated object");
            case "ovs" -> List.of(
                    "OVS syntax completion response",
                    "OVS syntax definition response",
                    "OVS syntax references response",
                    "OVS syntax documentSymbol response",
                    "css { displayFlex }",
                    "labelText");
            case "cssts" -> List.of(
                    "CSSTS css syntax completion response",
                    "CSSTS css syntax definition response",
                    "CSSTS css syntax references response",
                    "CSSTS css syntax documentSymbol response",
                    "css { colorRed, displayFlex }",
                    "derivedStyle");
            default -> throw new IllegalStateException("Unsupported language id: " + languageId);
        };
    }

    private static List<String> languageSemanticTokenNeedles(String languageId) {
        return switch (languageId) {
            case "qin" -> List.of(
                    "Qin object declaration Counter",
                    "Qin object usage Counter");
            case "ovs" -> List.of(
                    "OVS syntax labelText declaration",
                    "OVS syntax labelText render usage");
            case "cssts" -> List.of(
                    "CSSTS css syntax baseStyle declaration",
                    "CSSTS css syntax baseStyle usage");
            default -> throw new IllegalStateException("Unsupported language id: " + languageId);
        };
    }

    private static void verifyGeneratedParserDependency(
            String id,
            Path projectRoot,
            Path generatedQinParserRoot,
            QinConfig config) throws Exception {
        String dependency = config.getDependencyVersion(GENERATED_QIN_PARSER_PACKAGE);
        require(dependency != null && !dependency.isBlank(),
                id + " must declare " + GENERATED_QIN_PARSER_PACKAGE + " in qin.config.js dependencies");
        require(dependency.startsWith("file:"),
                id + " must use a file: dependency for " + GENERATED_QIN_PARSER_PACKAGE);

        Path resolvedDependency = projectRoot.resolve(dependency.substring("file:".length())).normalize();
        require(generatedQinParserRoot.equals(resolvedDependency),
                id + " must resolve " + GENERATED_QIN_PARSER_PACKAGE
                        + " to the shared generated Qin parser package: " + resolvedDependency);

        Path packageJson = generatedQinParserRoot.resolve("package.json");
        require(Files.isRegularFile(packageJson),
                "Generated Qin parser package.json must exist before dependency checks");
        String packageJsonSource = Files.readString(packageJson);
        require(packageJsonSource.contains("\"main\": \"./index.ts\""),
                "Generated Qin parser package.json must expose ./index.ts as main");
        require(packageJsonSource.contains("\"module\": \"./index.ts\""),
                "Generated Qin parser package.json must expose ./index.ts as module");
        require(packageJsonSource.contains("\"import\": \"./index.ts\""),
                "Generated Qin parser package.json exports must import ./index.ts");
        require(packageJsonSource.contains("\"default\": \"./index.ts\""),
                "Generated Qin parser package.json exports must default to ./index.ts");
    }

    private static void verifyGeneratedParserParityCorpus(Path qinLanguageRoot) throws Exception {
        Path parityTestPath = qinLanguageRoot.resolve("tests")
                .resolve("test-generated-parser-parity.ts")
                .normalize();
        require(Files.isRegularFile(parityTestPath),
                "Qin generated parser parity test must exist: " + parityTestPath);
        String paritySource = Files.readString(parityTestPath);
        require(paritySource.contains("qin object method body control flow"),
                "Qin generated parser parity corpus must include object method-body control flow");
        require(paritySource.contains("export object Labeler"),
                "Qin generated parser parity corpus must include a Qin object declaration");
        require(paritySource.contains("const prefix = \"hello \""),
                "Qin generated parser parity corpus must include object method local binding");
        require(paritySource.contains("if (flag)"),
                "Qin generated parser parity corpus must include object method early-return if");
        require(paritySource.contains("return prefix + name"),
                "Qin generated parser parity corpus must include object method branch return");
        require(paritySource.contains("return \"bye \" + name"),
                "Qin generated parser parity corpus must include object method fallthrough return");
    }

    private static void verifyLanguageToolReferences(
            MatrixCase matrixCase,
            Path projectRoot,
            Path workspaceRoot,
            QinConfig config,
            LanguageConfig language) {
        require(matrixCase.expectedParser().equals(language.parser()),
                matrixCase.id() + " language.parser mismatch");
        verifyPathLikeOrPackageReference(
                matrixCase.id(),
                "language.parser",
                language.parser(),
                projectRoot,
                workspaceRoot,
                config);

        if (matrixCase.expectedCompiler() == null) {
            require(language.compiler() == null || language.compiler().isBlank(),
                    matrixCase.id() + " language.compiler must stay unset");
        } else {
            require(matrixCase.expectedCompiler().equals(language.compiler()),
                    matrixCase.id() + " language.compiler mismatch");
            verifyPathLikeOrPackageReference(
                    matrixCase.id(),
                    "language.compiler",
                    language.compiler(),
                    projectRoot,
                    workspaceRoot,
                    config);
        }
    }

    private static void verifyLanguageServerMetadata(MatrixCase matrixCase, QinConfig config) {
        LanguageServerConfig languageServer = config.languageServer();
        require(languageServer != null,
                matrixCase.id() + " must declare shared languageServer metadata");
        require(matrixCase.extension().equals(languageServer.sourceExtension()),
                matrixCase.id() + " languageServer.sourceExtension mismatch");
        require(".ts".equals(languageServer.serviceExtension()),
                matrixCase.id() + " languageServer.serviceExtension must be .ts");
        require(GENERATED_QIN_PARSER_PACKAGE.equals(languageServer.generatedParserTarget()),
                matrixCase.id() + " languageServer.generatedParserTarget mismatch");
        if (matrixCase.expectedParserPackage() == null) {
            require(languageServer.parserPackage() == null || languageServer.parserPackage().isBlank(),
                    matrixCase.id() + " languageServer.parserPackage must stay blank");
        } else {
            require(matrixCase.expectedParserPackage().equals(languageServer.parserPackage()),
                    matrixCase.id() + " languageServer.parserPackage mismatch");
        }
        if (matrixCase.expectedCompilerPackage() == null) {
            require(languageServer.compilerPackage() == null || languageServer.compilerPackage().isBlank(),
                    matrixCase.id() + " languageServer.compilerPackage must stay blank");
        } else {
            require(matrixCase.expectedCompilerPackage().equals(languageServer.compilerPackage()),
                    matrixCase.id() + " languageServer.compilerPackage mismatch");
        }
    }

    private static void verifyPathLikeOrPackageReference(
            String id,
            String field,
            String rawReference,
            Path projectRoot,
            Path workspaceRoot,
            QinConfig config) {
        require(rawReference != null && !rawReference.isBlank(), id + " " + field + " is required");
        if (isPackageReference(rawReference)) {
            require(config.hasDependency(rawReference),
                    id + " " + field + " package reference must be declared in qin.config.js dependencies");
            return;
        }
        Path resolved = projectRoot.resolve(rawReference).normalize();
        require(resolved.startsWith(workspaceRoot), id + " " + field + " must stay inside workspace");
        require(Files.exists(resolved), id + " " + field + " must resolve to an existing path: " + resolved);
    }

    private static boolean isPackageReference(String rawReference) {
        return rawReference.startsWith("@")
                || (!rawReference.contains("/")
                && !rawReference.contains("\\")
                && !rawReference.startsWith("."));
    }

    private static void verifyRuntimeClassSmokeTask(
            String buildSource,
            String taskName,
            String expectedWorkingDirectory,
            String expectedMainClass) {
        String taskBlock = taskBlock(buildSource, "register<Exec>(\"" + taskName + "\")");
        require(taskBlock.contains(expectedWorkingDirectory),
                taskName + " must run from " + expectedWorkingDirectory);
        require(taskBlock.contains("\"run\", \"" + expectedMainClass + "\""),
                taskName + " must invoke Qin run for " + expectedMainClass);
        for (String forbidden : List.of(
                "\"language\"",
                "\"test\"",
                "\"server\"",
                "\"node\"",
                "\"tsx\"",
                "\"tsdown\"")) {
            require(!taskBlock.contains(forbidden),
                    taskName + " must stay on the JVM .class path and not use " + forbidden);
        }
    }

    private static void verifyRuntimeProjectClassTarget(
            Path workspaceRoot,
            String id,
            Path projectRelativePath,
            String expectedEntry) throws Exception {
        Path projectRoot = workspaceRoot.resolve(projectRelativePath).normalize();
        require(Files.isRegularFile(projectRoot.resolve("qin.config.js")),
                id + " runtime project must be managed by qin.config.js");
        QinConfig config = new ConfigLoader(projectRoot.toString()).load();
        require(expectedEntry.equals(config.entry()),
                id + " runtime entry must stay on the Java/JVM path: " + config.entry());
        require(config.java() != null,
                id + " runtime project must declare java config for JVM .class output");
        require("UTF-8".equalsIgnoreCase(config.java().encoding()),
                id + " runtime project must use UTF-8 Java source encoding");
        require("build/classes".equals(config.java().outputDir()),
                id + " runtime project must compile to build/classes, got " + config.java().outputDir());

        for (Map.Entry<String, String> script : config.scripts().entrySet()) {
            String command = script.getValue();
            for (String forbidden : List.of("node", "tsx", "tsdown", "language", "server")) {
                require(!command.contains(forbidden),
                        id + " runtime script " + script.getKey()
                                + " must not use editor/LSP tooling command " + forbidden + ": " + command);
            }
        }
    }

    private static void verifyJvmClassDeclarationCorpus(Path workspaceRoot) throws Exception {
        Path corpusPath = workspaceRoot.resolve("qin")
                .resolve("packages")
                .resolve("qin-lang-backend-jvm")
                .resolve("src")
                .resolve("java")
                .resolve("com")
                .resolve("qin")
                .resolve("lang")
                .resolve("backend")
                .resolve("jvm")
                .resolve("QinJvmClassDeclarationCorpusSmokeTestMain.java")
                .normalize();
        Path earlyReturnPath = corpusPath.resolveSibling("QinJvmParsedEarlyReturnMethodBodySmokeTestMain.java");
        require(Files.isRegularFile(corpusPath),
                "Qin JVM class declaration corpus smoke must exist: " + corpusPath);
        require(Files.isRegularFile(earlyReturnPath),
                "Qin JVM parsed early-return method-body smoke must exist: " + earlyReturnPath);

        String corpusSource = Files.readString(corpusPath);
        String earlyReturnSource = Files.readString(earlyReturnPath);
        require(corpusSource.contains("QinJvmParsedEarlyReturnMethodBodySmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed early-return method-body smoke");
        require(corpusSource.contains("14 cases"),
                "Qin JVM class declaration corpus count must cover the parsed early-return case");
        require(earlyReturnSource.contains("const prefix = \"hello \""),
                "Parsed early-return smoke must cover Qin local binding in a method body");
        require(earlyReturnSource.contains("if (flag)"),
                "Parsed early-return smoke must cover Qin if branch lowering");
        require(earlyReturnSource.contains("return prefix + name"),
                "Parsed early-return smoke must cover Qin early return from a block");
        require(earlyReturnSource.contains("return \"bye \" + name"),
                "Parsed early-return smoke must cover Qin fallthrough return after early return");
    }

    private static void verifyCompletionAuditDocument(Path auditPath) throws Exception {
        require(Files.isRegularFile(auditPath), "LSP completion audit document must exist: " + auditPath);
        String audit = Files.readString(auditPath);
        for (String requiredSection : List.of(
                "## Target",
                "## Requirement Audit",
                "## Known Gaps",
                "## Next Hardening Steps")) {
            require(audit.contains(requiredSection),
                    "LSP completion audit must keep section " + requiredSection);
        }
        require(audit.contains("audit map, not a completion claim"),
                "LSP completion audit must not present partial evidence as a completion claim");
        require(audit.contains("These items are not proven complete yet"),
                "LSP completion audit must keep explicit non-completion wording while gaps remain");
        for (String gapNeedle : List.of(
                "Full grammar authority",
                "manual IDE behavior",
                "LSP-critical inventory",
                "JVM `.class` execution",
                "Node/TypeScript boundary")) {
            require(audit.contains(gapNeedle),
                    "LSP completion audit must keep known gap coverage for " + gapNeedle);
        }
        for (String hardeningNeedle : List.of(
                "Expand the unified class-declaration `.class` gate",
                "Keep expanding generated parser parity",
                "Keep this audit updated")) {
            require(audit.contains(hardeningNeedle),
                    "LSP completion audit must keep next hardening step " + hardeningNeedle);
        }
        require(audit.contains("14-case class-declaration corpus"),
                "LSP completion audit must record the current JVM class declaration corpus size");
        require(audit.contains("local binding plus early-return `if`"),
                "LSP completion audit must record parsed method-body early-return coverage");
        require(audit.contains("Qin object method bodies with local binding plus early-return `if`"),
                "LSP completion audit must record object method-body parser parity coverage");
    }

    private static void verifyGeneratedQinParserPackage(Path qinLanguageRoot, QinConfig config) throws Exception {
        Path generatedRoot = qinLanguageRoot.resolve("generated").resolve("qin-parser-ts").normalize();
        Path generatedConfig = generatedRoot.resolve("qin.config.js");
        Path generatedPackageJson = generatedRoot.resolve("package.json");
        Path generatedIndex = generatedRoot.resolve("index.ts");

        require(Files.isRegularFile(generatedConfig),
                "Generated Qin parser package must include qin.config.js");
        require(Files.isRegularFile(generatedPackageJson),
                "Generated Qin parser package must include package.json");
        require(Files.isRegularFile(generatedIndex),
                "Generated Qin parser package must include index.ts");

        String languageConfigSource = Files.readString(qinLanguageRoot.resolve("qin.config.js"));
        String generatedConfigSource = Files.readString(generatedConfig);
        String packageJsonSource = Files.readString(generatedPackageJson);
        String indexSource = Files.readString(generatedIndex);

        require(languageConfigSource.contains("parser: \"generated/qin-parser-ts\""),
                "qin-language must point language.parser at generated/qin-parser-ts");
        require(config.languageServer() != null,
                "qin-language must declare shared languageServer metadata");
        require(".qin".equals(config.languageServer().sourceExtension()),
                "qin-language languageServer.sourceExtension must be .qin");
        require(".ts".equals(config.languageServer().serviceExtension()),
                "qin-language languageServer.serviceExtension must be .ts");
        require("com.qin:qin-parser".equals(config.languageServer().parserPackage()),
                "qin-language languageServer.parserPackage must be com.qin:qin-parser");
        require(GENERATED_QIN_PARSER_PACKAGE.equals(config.languageServer().generatedParserTarget()),
                "qin-language languageServer.generatedParserTarget must be @qin/generated-qin-parser-ts");
        require(config.qinLanguage() != null,
                "qin-language must keep Qin-specific language metadata");
        require(".qin".equals(config.qinLanguage().sourceExtension()),
                "qin-language qinLanguage.sourceExtension must be .qin");
        require(".ts".equals(config.qinLanguage().serviceExtension()),
                "qin-language qinLanguage.serviceExtension must be .ts");
        require("com.qin:qin-parser".equals(config.qinLanguage().parserPackage()),
                "qin-language qinLanguage.parserPackage must be com.qin:qin-parser");
        require(GENERATED_QIN_PARSER_PACKAGE.equals(config.qinLanguage().generatedParserTarget()),
                "qin-language must declare @qin/generated-qin-parser-ts as generated parser target");
        require(generatedConfigSource.contains("name: \"" + GENERATED_QIN_PARSER_PACKAGE + "\""),
                "Generated Qin parser qin.config.js must use " + GENERATED_QIN_PARSER_PACKAGE + " package name");
        require(generatedConfigSource.contains("entry: \"./index.ts\""),
                "Generated Qin parser qin.config.js must expose ./index.ts as entry");
        require(generatedConfigSource.contains("entryBinaryName: \"com.qin.parser.QinParser\""),
                "Generated Qin parser qin.config.js must record com.qin.parser.QinParser entry");
        require(packageJsonSource.contains("\"name\": \"" + GENERATED_QIN_PARSER_PACKAGE + "\""),
                "Generated Qin parser package.json must use " + GENERATED_QIN_PARSER_PACKAGE + " package name");
        require(packageJsonSource.contains("\"entryBinaryName\": \"com.qin.parser.QinParser\""),
                "Generated Qin parser package.json must record com.qin.parser.QinParser entry");
        require(indexSource.contains("export default com_qin_parser_QinParser"),
                "Generated Qin parser index.ts must default-export QinParser");
        require(indexSource.contains("com_qin_parser_QinParser as QinParser"),
                "Generated Qin parser index.ts must named-export QinParser");
        require(indexSource.contains("SlimeJavascriptParser"),
                "Generated Qin parser index.ts must expose SlimeJavascriptParser for parser inheritance");

        require(config.generated() != null, "qin-language must declare generated metadata");
        require("com.qin.parser.QinParser".equals(config.generated().entryBinaryName()),
                "qin-language generated.entryBinaryName must be com.qin.parser.QinParser");
        require("generated/qin-parser-ts".equals(config.generated().outputDir()),
                "qin-language generated.outputDir must be generated/qin-parser-ts");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private static String taskBlock(String source, String marker) {
        int start = source.indexOf(marker);
        require(start >= 0, "Gradle build script must contain " + marker);
        int braceStart = source.indexOf('{', start);
        require(braceStart >= 0, "Gradle task block must open after " + marker);
        int depth = 0;
        for (int index = braceStart; index < source.length(); index++) {
            char current = source.charAt(index);
            if (current == '{') {
                depth++;
            } else if (current == '}') {
                depth--;
                if (depth == 0) {
                    return source.substring(braceStart, index + 1);
                }
            }
        }
        throw new IllegalStateException("Gradle task block must close after " + marker);
    }

    private record MatrixCase(
            String id,
            String extension,
            Path projectRelativePath,
            Path compilerProjectRelativePath,
            String serverBundle,
            String expectedParser,
            String expectedCompiler,
            String expectedParserPackage,
            String expectedCompilerPackage,
            String languageServerTest,
            String... requiredTestScriptParts) {
    }
}
