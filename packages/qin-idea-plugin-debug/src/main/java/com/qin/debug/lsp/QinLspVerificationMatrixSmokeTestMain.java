package com.qin.debug.lsp;

import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
import com.qin.types.LanguageServerConfig;
import com.qin.types.QinConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QinLspVerificationMatrixSmokeTestMain {
    private static final String GENERATED_QIN_PARSER_PACKAGE = "@qin/generated-qin-parser-ts";
    private static final Pattern LOCAL_TYPESCRIPT_IMPORT = Pattern.compile(
            "\\bfrom\\s+['\"](\\.{1,2}/[^'\"]+)['\"]|\\bimport\\s*\\(\\s*['\"](\\.{1,2}/[^'\"]+)['\"]\\s*\\)");

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
                        "tsx tests/test-java-source-symbols.ts",
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
            if (!"qin".equals(matrixCase.id())) {
                verifyPackageJsonIsNotScriptEntrypoint(matrixCase.id(), projectRoot);
            }
            for (String requiredPart : matrixCase.requiredTestScriptParts()) {
                require(testScript.contains(requiredPart),
                        matrixCase.id() + " scripts.test missing " + requiredPart + ": " + testScript);
            }
            verifyLanguageServerFeatureAssertions(matrixCase, projectRoot);

            if ("qin".equals(matrixCase.id())) {
                verifyQinLanguagePluginFeatureMappings(projectRoot);
                verifyQinLanguageServicePluginFeatureMappings(projectRoot);
                verifyQinJavaSourceSymbolSmoke(projectRoot);
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
        QinConfig ideaClientConfig = new ConfigLoader(ideaClientPath.toString()).load();
        verifyIdeaClientQinScripts(ideaClientPath, ideaClientConfig);
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
        require(buildSource.contains("register<JavaExec>(\"qinToolWindowConfigTreeSmoke\")"),
                "Gradle must declare qinToolWindowConfigTreeSmoke");
        require(buildSource.contains("QinToolWindowConfigTreeSmokeTestMain"),
                "qinToolWindowConfigTreeSmoke must verify Qin tool window config tree metadata");
        String localDependencyBuildBlock = taskBlock(
                buildSource,
                "register<Exec>(\"qinLanguageLocalDependencyBuildSmoke\")");
        require(localDependencyBuildBlock.contains("workspaceRoot.resolve(\"qin\")"),
                "qinLanguageLocalDependencyBuildSmoke must run from the Qin project root");
        require(localDependencyBuildBlock.contains("QinCliLanguageLocalDependencyBuildSmokeTestMain"),
                "qinLanguageLocalDependencyBuildSmoke must verify local file dependency builds through Qin CLI");
        verifyLocalDependencyBuildSmokeCoverage(workspaceRoot);
        verifyRuntimeFeatureValidatorParserScanCoverage(workspaceRoot);
        require(buildSource.contains("register<Exec>(\"qinJavaRunnerNoCompilerFallbackSmoke\")"),
                "Gradle must declare qinJavaRunnerNoCompilerFallbackSmoke");
        String noCompilerFallbackBlock = taskBlock(
                buildSource,
                "register<Exec>(\"qinJavaRunnerNoCompilerFallbackSmoke\")");
        require(noCompilerFallbackBlock.contains("workspaceRoot.resolve(\"qin\")"),
                "qinJavaRunnerNoCompilerFallbackSmoke must run from the Qin project root");
        require(noCompilerFallbackBlock.contains("JavaRunnerNoCompilerFallbackSmokeTestMain"),
                "qinJavaRunnerNoCompilerFallbackSmoke must verify JavaRunner has no compiler fallback");
        require(buildSource.contains("register<Exec>(\"qinCsstsCompilerNoFallbackSmoke\")"),
                "Gradle must declare qinCsstsCompilerNoFallbackSmoke");
        String csstsNoFallbackBlock = taskBlock(
                buildSource,
                "register<Exec>(\"qinCsstsCompilerNoFallbackSmoke\")");
        require(csstsNoFallbackBlock.contains("qin/packages/qin-runtime-core"),
                "qinCsstsCompilerNoFallbackSmoke must run from qin/packages/qin-runtime-core");
        require(csstsNoFallbackBlock.contains("QinCsstsCompilerNoFallbackSmokeTestMain"),
                "qinCsstsCompilerNoFallbackSmoke must verify Qin CSSTS compiler fallback removal");
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
        require(buildSource.contains("register(\"lspQinMatrix\")"),
                "Gradle must declare lspQinMatrix");
        String lspQinMatrixBlock = taskBlock(buildSource, "register(\"lspQinMatrix\")");
        for (String qinMatrixTask : List.of(
                "qinGeneratedParserDryRun",
                "qinLanguageTest",
                "lspQinRegistrySmoke",
                "lspQinServerCommandLineSmoke",
                "lspQinServerDiagnosticsSmoke",
                "lspPluginDescriptorSmoke",
                "lspNoLocalParserSmoke",
                "qinToolWindowConfigTreeSmoke")) {
            require(lspQinMatrixBlock.contains("dependsOn(\"" + qinMatrixTask + "\")"),
                    "lspQinMatrix must depend on " + qinMatrixTask);
        }
        String lspUnifiedMatrixBlock = taskBlock(buildSource, "register(\"lspUnifiedMatrix\")");
        require(lspUnifiedMatrixBlock.contains("dependsOn(\"qinLanguageLocalDependencyBuildSmoke\")"),
                "lspUnifiedMatrix must include Qin local file dependency build smoke");
        require(lspUnifiedMatrixBlock.contains("dependsOn(\"qinJavaRunnerNoCompilerFallbackSmoke\")"),
                "lspUnifiedMatrix must include Qin JavaRunner no compiler fallback smoke");
        require(lspUnifiedMatrixBlock.contains("dependsOn(\"qinCsstsCompilerNoFallbackSmoke\")"),
                "lspUnifiedMatrix must include Qin CSSTS compiler no fallback smoke");
        require(lspUnifiedMatrixBlock.contains("dependsOn(\"qinToolWindowConfigTreeSmoke\")"),
                "lspUnifiedMatrix must include Qin tool window config tree smoke");
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
        verifyLanguageToolingDocumentation(workspaceRoot);
        verifyLanguageCliSmokeCoverage(ideaClientPath);
        verifyIdeaDiagnosticsSmokeFeatureAssertions(ideaClientPath);

        System.out.println("Qin LSP verification matrix smoke passed");
    }

    private static void verifyIdeaClientQinScripts(Path ideaClientPath, QinConfig config) {
        require(Files.isRegularFile(ideaClientPath.resolve("qin.config.js")),
                "IDEA LSP client must be Qin-managed through qin.config.js");
        require("com.qin:qin-idea-plugin-debug".equals(config.name()),
                "IDEA LSP client qin.config.js must use com.qin:qin-idea-plugin-debug name");
        require("tooling".equals(config.type()),
                "IDEA LSP client qin.config.js must classify the plugin as tooling");
        for (String scriptName : List.of(
                "check",
                "lspQinMatrix",
                "lspUnifiedMatrix",
                "lspVerificationMatrixSmoke",
                "runIdeLspFixture",
                "buildPlugin")) {
            String script = config.scripts().get(scriptName);
            require(script != null && !script.isBlank(),
                    "IDEA LSP client qin.config.js must expose scripts." + scriptName);
            require(script.contains("gradlew.bat"),
                    "IDEA LSP client scripts." + scriptName
                            + " must call the IntelliJ Platform Gradle boundary");
            require(script.contains("-Dfile.encoding=UTF-8"),
                    "IDEA LSP client scripts." + scriptName + " must force UTF-8");
            require(!script.contains("npm run"),
                    "IDEA LSP client scripts." + scriptName
                            + " must not forward through package scripts");
        }
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
        for (String generatedParserNeedle : List.of(
                "expectGeneratedParserDryRun",
                "\"generate-parser\", \"--dry-run\"",
                "generate java parser com.qin.parser.QinParser",
                "packageName: @qin/generated-qin-parser-ts",
                "language.parser: generated/qin-parser-ts")) {
            require(smokeSource.contains(generatedParserNeedle),
                    "lspLanguageCliSmoke must verify Qin Java parser -> TypeScript parser dry-run needle "
                            + generatedParserNeedle);
        }
    }

    private static void verifyLocalDependencyBuildSmokeCoverage(Path workspaceRoot) throws Exception {
        Path smokePath = workspaceRoot.resolve("qin")
                .resolve("src")
                .resolve("com")
                .resolve("qin")
                .resolve("cli")
                .resolve("QinCliLanguageLocalDependencyBuildSmokeTestMain.java")
                .normalize();
        require(Files.isRegularFile(smokePath),
                "Qin local dependency build smoke source must exist: " + smokePath);
        String smokeSource = Files.readString(smokePath);
        for (String requiredNeedle : List.of(
                "value = 1",
                "value = 2",
                "local file dependency rebuild marker from changed source",
                "verifyLanguageScriptDryRunGeneratesParserFirst",
                "verifyNamedLanguageScriptRunDryRun",
                "qin language run dev:mp-weixin --dry-run",
                "generate java parser com.qin.parser.QinParser",
                "qin language test --dry-run must generate parser before running scripts",
                "CountDownLatch",
                "AtomicReference<Throwable>",
                "concurrent-qin-build",
                "violation.txt",
                "language local dependency build lock prevents concurrent dist clean/build entry")) {
            require(smokeSource.contains(requiredNeedle),
                    "Qin local dependency build smoke must verify changed-source rebuild coverage: "
                            + requiredNeedle);
        }
        Path cliPath = workspaceRoot.resolve("qin")
                .resolve("src")
                .resolve("com")
                .resolve("qin")
                .resolve("cli")
                .resolve("QinCli.java")
                .normalize();
        require(Files.isRegularFile(cliPath), "Qin CLI source must exist: " + cliPath);
        String cliSource = Files.readString(cliPath);
        for (String requiredNeedle : List.of(
                "LANGUAGE_BUILD_LOCK_FILE",
                "language-build.lock",
                "ensureGeneratedLanguageParser(config, dryRun)",
                "case \"run\" -> runNamedLanguageScript(actionArgs)",
                "Missing script name for qin language run <script>",
                "runWithLanguageBuildLock",
                "acquireLanguageBuildLock",
                "OverlappingFileLockException",
                "Another language dependency build is in progress")) {
            require(cliSource.contains(requiredNeedle),
                    "Qin CLI must serialize local language dependency builds with a real file lock: "
                            + requiredNeedle);
        }
    }

    private static void verifyRuntimeFeatureValidatorParserScanCoverage(Path workspaceRoot) throws Exception {
        Path validatorPath = workspaceRoot.resolve("qin")
                .resolve("packages")
                .resolve("qin-lang-sema-esm")
                .resolve("src")
                .resolve("java")
                .resolve("com")
                .resolve("qin")
                .resolve("lang")
                .resolve("sema")
                .resolve("esm")
                .resolve("QinEsmRuntimeFeatureValidator.java")
                .normalize();
        Path smokePath = validatorPath.resolveSibling("QinEsmRuntimeFeatureParserScanSmokeTestMain.java");
        require(Files.isRegularFile(validatorPath),
                "Qin ESM runtime feature validator must exist: " + validatorPath);
        require(Files.isRegularFile(smokePath),
                "Qin ESM runtime feature parser-scan smoke must exist: " + smokePath);
        String validatorSource = Files.readString(validatorPath);
        String smokeSource = Files.readString(smokePath);
        require(validatorSource.contains("new QinParserFacade().parseSource(module.source())"),
                "Qin ESM runtime feature validator must inspect syntax through QinParserFacade");
        require(validatorSource.contains("ImportExpression"),
                "Qin ESM runtime feature validator must detect dynamic import through AST");
        require(validatorSource.contains("MetaProperty"),
                "Qin ESM runtime feature validator must detect import.meta through AST");
        require(!validatorSource.contains("DYNAMIC_IMPORT_PATTERN"),
                "Qin ESM runtime feature validator must not keep dynamic import regex scanning");
        require(!validatorSource.contains("Pattern.compile(\"\\\\bimport\\\\s*\\\\.\\\\s*meta\\\\b\")"),
                "Qin ESM runtime feature validator must not keep import.meta regex scanning");
        for (String smokeNeedle : List.of(
                "QinEsmRuntimeFeatureParserScanSmokeTestMain",
                "ESM3001",
                "QIN_JS_UNSUPPORTED_IMPORT_META",
                "expectNoDiagnostic",
                "import('./not-code.js') import.meta.url")) {
            require(smokeSource.contains(smokeNeedle),
                    "Qin ESM runtime feature parser-scan smoke must cover " + smokeNeedle);
        }
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
                "textDocument/codeAction",
                "textDocument/completion",
                "textDocument/hover",
                "textDocument/signatureHelp",
                "textDocument/definition",
                "textDocument/declaration",
                "textDocument/typeDefinition",
                "textDocument/references",
                "textDocument/documentHighlight",
                "textDocument/formatting",
                "textDocument/inlayHint",
                "textDocument/documentLink",
                "volar/client/findFileReference",
                "workspace/willRenameFiles",
                "textDocument/linkedEditingRange",
                "textDocument/rename",
                "textDocument/prepareRename",
                "textDocument/documentSymbol",
                "textDocument/foldingRange",
                "textDocument/selectionRange",
                "workspace/symbol",
                "textDocument/semanticTokens/full")) {
            require(smokeSource.contains(method),
                    "IDEA diagnostics smoke must request " + method);
        }
        for (String assertionNeedle : List.of(
                "codeAction missing remove forbidden java import quickfix",
                "completion missing",
                "hover missing",
                "signatureHelp missing",
                "definition did not resolve",
                "declaration did not resolve",
                "typeDefinition did not resolve currentUser to source interface",
                "references did not include declaration and usage",
                "documentHighlight did not include declaration and usage",
                "formatting did not return TypeScript formatter edits through source mappings",
                "inlayHint did not include parameter or variable type hints through source mappings",
                "documentLink missing local import target",
                "fileReferences missing local import usage",
                "fileRenameEdits did not update local import specifier",
                "linkedEditingRange missing object declaration and usage",
                "rename did not return workspace edits",
                "prepareRename did not return symbol range",
                "documentSymbol missing",
                "foldingRange missing source object block",
                "selectionRange missing object name and declaration chain",
                "workspaceSymbol missing source object symbol",
                "QIN1001",
                "QIN1002",
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
        verifyPackageJsonIsNotScriptEntrypoint(matrixCase.id() + " compiler", compilerRoot);
        verifyGeneratedParserDependency(
                matrixCase.id() + " compiler",
                compilerRoot,
                generatedQinParserRoot,
                compilerConfig);
        verifyCompilerLegacyDependencyBoundary(matrixCase, compilerConfig);
    }

    private static void verifyCompilerLegacyDependencyBoundary(MatrixCase matrixCase, QinConfig compilerConfig) {
        for (String requiredDependency : List.of("subhuti", "slime-ast")) {
            require(compilerConfig.hasDependency(requiredDependency),
                    matrixCase.id() + " compiler must keep required PEG/CST/AST dependency "
                            + requiredDependency);
        }
        for (String unusedDependency : List.of("slime-generator", "slime-parser", "slime-token")) {
            require(!compilerConfig.hasDependency(unusedDependency),
                    matrixCase.id() + " compiler must not declare unused legacy parser dependency "
                            + unusedDependency);
        }
    }

    private static void verifyPackageJsonIsNotScriptEntrypoint(String id, Path projectRoot) throws Exception {
        Path packageJson = projectRoot.resolve("package.json").normalize();
        require(Files.isRegularFile(packageJson),
                id + " package.json must exist for dependency metadata");
        String source = Files.readString(packageJson);
        require(!source.contains("\"scripts\""),
                id + " package.json must not define scripts; qin.config.js is the command entrypoint");
        for (String forbidden : List.of("npm run", "npx ", "pnpm ", "yarn ")) {
            require(!source.contains(forbidden),
                    id + " package.json must not forward commands through " + forbidden);
        }
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
        Path csstsCstToAstPath = csstsCompilerRoot.resolve("src")
                .resolve("factory")
                .resolve("CssTsCstToAstUtils.ts")
                .normalize();
        Path ovsParserPath = ovsCompilerRoot.resolve("src")
                .resolve("parser")
                .resolve("OvsParser.ts")
                .normalize();
        Path ovsIndexPath = ovsCompilerRoot.resolve("src")
                .resolve("index.ts")
                .normalize();
        Path ovsCstToAstPath = ovsCompilerRoot.resolve("src")
                .resolve("factory")
                .resolve("OvsCstToSlimeAst")
                .resolve("OvsCstToSlimeAst.ts")
                .normalize();
        Path ovsStatementCstToAstPath = ovsCompilerRoot.resolve("src")
                .resolve("factory")
                .resolve("OvsCstToSlimeAst")
                .resolve("OvsCstToSlimeAst.Statement.ts")
                .normalize();
        Path forbiddenOvsAdapterPath = ovsCompilerRoot.resolve("src")
                .resolve("parser")
                .resolve("generated-runtime-adapter.ts")
                .normalize();

        for (Path requiredPath : List.of(
                cssTsParserPath,
                cssTsAdapterPath,
                csstsTransformPath,
                csstsCstToAstPath,
                ovsParserPath,
                ovsIndexPath,
                ovsCstToAstPath,
                ovsStatementCstToAstPath)) {
            require(requiredPath.startsWith(workspaceRoot),
                    "Generated parser chain source must stay inside workspace: " + requiredPath);
            require(Files.isRegularFile(requiredPath),
                    "Generated parser chain source must exist: " + requiredPath);
        }

        String cssTsParser = Files.readString(cssTsParserPath);
        String cssTsAdapter = Files.readString(cssTsAdapterPath);
        String csstsTransform = Files.readString(csstsTransformPath);
        String csstsCstToAst = Files.readString(csstsCstToAstPath);
        String ovsParser = Files.readString(ovsParserPath);
        String ovsIndex = Files.readString(ovsIndexPath);
        String ovsCstToAst = Files.readString(ovsCstToAstPath);
        String ovsStatementCstToAst = Files.readString(ovsStatementCstToAstPath);

        require(cssTsParser.contains("from \"@qin/generated-qin-parser-ts\""),
                "CSSTS parser must import the shared generated Qin parser package");
        require(!cssTsParser.contains("slime-parser"),
                "CSSTS parser must not import legacy slime-parser; parser inheritance uses generated Qin parser");
        require(cssTsParser.contains("QinParser"),
                "CSSTS parser must use the generated QinParser export");
        require(cssTsParser.contains("extends QinParser"),
                "CSSTS parser must extend the generated Qin parser base");
        require(cssTsParser.contains("normalizeGeneratedTokens"),
                "CSSTS parser must normalize generated parser tokens");
        require(cssTsParser.contains("this.Or("),
                "CSSTS parser must use generated parser Or semantics");
        require(cssTsParser.contains("__qin_yield")
                        && cssTsParser.contains("__qin_await")
                        && cssTsParser.contains("__qin_in"),
                "CSSTS parser params must expose generated Qin parser __qin_* accessors");
        require(!cssTsParser.contains("alt:"),
                "CSSTS parser must not use legacy { alt } fallback alternatives");
        require(!cssTsParser.contains("fallback"),
                "CSSTS parser source must not carry fallback parser concepts");

        require(cssTsAdapter.contains("normalizeGeneratedCst"),
                "CSSTS generated runtime adapter must expose normalizeGeneratedCst");
        require(cssTsAdapter.contains("javaListToArray"),
                "CSSTS generated runtime adapter must bridge generated Java list values");
        require(csstsTransform.contains("normalizeGeneratedCst(parser.Program())"),
                "CSSTS transform must normalize CST from the generated parser chain");
        require(csstsTransform.contains("from '@qin/generated-qin-parser-ts/SlimeCstToAstBridge'"),
                "CSSTS transform must register the CST-to-AST extension through the generated bridge");
        require(csstsCstToAst.contains("import { SlimeCstToAst, SlimeCstToAstUtils, registerSlimeCstToAstUtil } from \"@qin/generated-qin-parser-ts/SlimeCstToAstBridge\""),
                "CSSTS CST-to-AST extension must inherit the generated SlimeCstToAst bridge");
        require(csstsCstToAst.contains("extends SlimeCstToAst"),
                "CSSTS CST-to-AST extension must stay on the generated SlimeCstToAst extension boundary");

        require(ovsParser.contains("from \"@qin/generated-qin-parser-ts\""),
                "OVS parser must import the shared generated Qin parser package");
        require(!ovsParser.contains("slime-parser"),
                "OVS parser must not import legacy slime-parser; parser inheritance uses generated Qin parser");
        require(ovsParser.contains("from \"cssts-compiler\""),
                "OVS parser must inherit CSSTS compiler parser support");
        require(ovsParser.contains("extends CssTsParser"),
                "OVS parser must extend CssTsParser");
        require(ovsParser.contains("QinObjectDeclaration(params)"),
                "OVS parser must preserve Qin declarations through the CSSTS/Qin parser chain");
        require(ovsParser.contains("super.Declaration(params)"),
                "OVS parser must delegate inherited TypeScript declarations to the generated Qin parser chain");
        require(ovsParser.contains("normalizeGeneratedTokens"),
                "OVS parser must normalize generated parser tokens");
        require(ovsParser.contains("Alternative.of("),
                "OVS parser must use generated parser Alternative.of semantics");
        require(!ovsParser.contains("alt:"),
                "OVS parser must not use legacy { alt } fallback alternatives");
        require(!ovsParser.contains("fallback"),
                "OVS parser source must not carry fallback parser concepts");
        require(ovsIndex.contains("normalizeGeneratedCst"),
                "OVS compiler transform must normalize CST from the generated parser chain");
        require(ovsIndex.contains("from \"@qin/generated-qin-parser-ts/SlimeCstToAstBridge\""),
                "OVS transform must register the CST-to-AST extension through the generated bridge");
        require(ovsCstToAst.contains("import { QinParser as SlimeParser } from \"@qin/generated-qin-parser-ts\""),
                "OVS CST-to-AST extension must use the generated QinParser for inherited rule names");
        require(ovsCstToAst.contains("import { SlimeCstToAst, registerSlimeCstToAstUtil } from \"@qin/generated-qin-parser-ts/SlimeCstToAstBridge\""),
                "OVS CST-to-AST extension must inherit the generated SlimeCstToAst bridge");
        require(ovsCstToAst.contains("Object.getPrototypeOf(SlimeCstToAst.prototype)"),
                "OVS CST-to-AST extension must keep the generated SlimeCstToAst extension boundary visible");
        require(ovsStatementCstToAst.contains("import { QinParser as SlimeParser } from \"@qin/generated-qin-parser-ts\""),
                "OVS statement CST-to-AST helper must use generated QinParser rule names");
        require(!Files.exists(forbiddenOvsAdapterPath),
                "OVS must inherit the generated runtime adapter from cssts-compiler, not keep a local copy");
        verifyLegacyAstImportsStayInCstToAstBoundary(workspaceRoot, csstsCompilerRoot, ovsCompilerRoot);
        verifyGeneratedParserChainRuntimeSmoke(
                "cssts-language",
                workspaceRoot,
                workspaceRoot.resolve("cssts").resolve("cssts-language")
                        .resolve("tests").resolve("test-generated-parser-chain.ts").normalize(),
                List.of(
                        "new CssTsParser(",
                        "parser instanceof SlimeJavascriptParser",
                        "object NestedLabeler",
                        "export interface ChainUser",
                        "export type ChainPair<T, U>",
                        "class ChainService",
                        "constructor(name: string)",
                        "const { name: destructuredName, values: [firstValue] } = config",
                        "token.tokenValue === 'ChainUser'",
                        "token.tokenValue === 'ChainPair'",
                        "token.tokenValue === 'ChainService'",
                        "token.tokenValue === 'constructor'",
                        "token.tokenValue === 'destructuredName'",
                        "token.tokenValue === 'firstValue'",
                        "token.tokenValue === 'premium'",
                        "token.tokenValue === 'standard'",
                        "token.tokenValue === 'try'",
                        "token.tokenValue === 'catch'",
                        "token.tokenValue === 'throw'",
                        "let total = 0",
                        "while (total < limit)",
                        "total = total + 1",
                        "token.tokenValue === 'while'",
                        "token.tokenValue === 'total'",
                        "token.tokenValue === '='",
                        "for (let i = 0; i < limit; i = i + 1)",
                        "token.tokenValue === 'for'",
                        "token.tokenValue === 'continue'",
                        "token.tokenValue === 'break'",
                        "do {",
                "} while (i < limit)",
                "token.tokenValue === 'do'",
                "token.tokenValue === 'countAtLeastOnce'",
                "collect(values: List)",
                "for (const item of values)",
                "token.tokenValue === 'of'",
                "token.tokenValue === 'item'",
                "switchStatus(status: string)",
                "switch (status)",
                "case \"ready\":",
                "default:",
                "token.tokenValue === 'switch'",
                "token.tokenValue === 'case'",
                "token.tokenValue === 'default'",
                "token.tokenValue === 'switchStatus'",
                "const moduleUrl = import.meta.url",
                        "const loadedModule = import(\"./dep.qin\")",
                        "token.tokenValue === 'import'",
                        "token.tokenValue === 'meta'",
                        "String(token.tokenValue).includes('./dep.qin')",
                        "const optionalName = user?.profile?.name",
                        "token.tokenName === 'QuestionDot'",
                        "token.tokenValue === 'optionalName'",
                        "const fallbackName = user.name ?? \"anonymous\"",
                        "token.tokenName === 'NullishCoalescing'",
                        "token.tokenValue === 'fallbackName'",
                        "const templateName = `hello ${name}`",
                        "token.tokenName === 'TemplateHead'",
                        "token.tokenValue === 'templateName'",
                        "css { colorRed, displayFlex }",
                        "parsedTokens.some((token: any) => token.tokenValue === 'object')",
                        "parsedTokens.some((token: any) => token.tokenValue === 'css')",
                        "requireNoDependency",
                        "'slime-ast', 'slime-parser', 'slime-token', 'subhuti'"));
        verifyGeneratedParserChainRuntimeSmoke(
                "cssts-compiler",
                workspaceRoot,
                csstsCompilerRoot.resolve("tests").resolve("test-generated-parser-chain.ts").normalize(),
                List.of(
                        "new CssTsParser(",
                        "parser instanceof SlimeJavascriptParser",
                        "object NestedLabeler",
                        "export interface ChainUser",
                        "export type ChainPair<T, U>",
                        "class ChainService",
                        "constructor(name: string)",
                        "const { name: destructuredName, values: [firstValue] } = config",
                        "token.tokenValue === 'ChainUser'",
                        "token.tokenValue === 'ChainPair'",
                        "token.tokenValue === 'ChainService'",
                        "token.tokenValue === 'constructor'",
                        "token.tokenValue === 'destructuredName'",
                        "token.tokenValue === 'firstValue'",
                        "token.tokenValue === 'premium'",
                        "token.tokenValue === 'standard'",
                        "token.tokenValue === 'try'",
                        "token.tokenValue === 'catch'",
                        "token.tokenValue === 'throw'",
                        "let total = 0",
                        "while (total < limit)",
                        "total = total + 1",
                        "token.tokenValue === 'while'",
                        "token.tokenValue === 'total'",
                        "token.tokenValue === '='",
                        "for (let i = 0; i < limit; i = i + 1)",
                        "token.tokenValue === 'for'",
                        "token.tokenValue === 'continue'",
                        "token.tokenValue === 'break'",
                        "do {",
                "} while (i < limit)",
                "token.tokenValue === 'do'",
                "token.tokenValue === 'countAtLeastOnce'",
                "collect(values: List)",
                "for (const item of values)",
                "token.tokenValue === 'of'",
                "token.tokenValue === 'item'",
                "switchStatus(status: string)",
                "switch (status)",
                "case \"ready\":",
                "default:",
                "token.tokenValue === 'switch'",
                "token.tokenValue === 'case'",
                "token.tokenValue === 'default'",
                "token.tokenValue === 'switchStatus'",
                "const moduleUrl = import.meta.url",
                        "const loadedModule = import(\"./dep.qin\")",
                        "token.tokenValue === 'import'",
                        "token.tokenValue === 'meta'",
                        "String(token.tokenValue).includes('./dep.qin')",
                        "const optionalName = user?.profile?.name",
                        "token.tokenName === 'QuestionDot'",
                        "token.tokenValue === 'optionalName'",
                        "const fallbackName = user.name ?? \"anonymous\"",
                        "token.tokenName === 'NullishCoalescing'",
                        "token.tokenValue === 'fallbackName'",
                        "const templateName = `hello ${name}`",
                        "token.tokenName === 'TemplateHead'",
                        "token.tokenValue === 'templateName'",
                        "css { colorRed, displayFlex }",
                        "parsedTokens.some((token: any) => token.tokenValue === 'object')",
                        "parsedTokens.some((token: any) => token.tokenValue === 'css')"));
        verifyGeneratedParserChainRuntimeSmoke(
                "ovs-language",
                workspaceRoot,
                workspaceRoot.resolve("ovsjs").resolve("ovs-language")
                        .resolve("tests").resolve("test-generated-parser-chain.ts").normalize(),
                List.of(
                        "new OvsParser(",
                        "parser instanceof CssTsParser",
                        "parser instanceof SlimeJavascriptParser",
                        "object NestedLabeler",
                        "export interface ChainUser",
                        "export type ChainPair<T, U>",
                        "class ChainService",
                        "constructor(name: string)",
                        "const { name: destructuredName, values: [firstValue] } = config",
                        "token.tokenValue === 'ChainUser'",
                        "token.tokenValue === 'ChainPair'",
                        "token.tokenValue === 'ChainService'",
                        "token.tokenValue === 'constructor'",
                        "token.tokenValue === 'destructuredName'",
                        "token.tokenValue === 'firstValue'",
                        "token.tokenValue === 'premium'",
                        "token.tokenValue === 'standard'",
                        "token.tokenValue === 'try'",
                        "token.tokenValue === 'catch'",
                        "token.tokenValue === 'throw'",
                        "let total = 0",
                        "while (total < limit)",
                        "total = total + 1",
                        "token.tokenValue === 'while'",
                        "token.tokenValue === 'total'",
                        "token.tokenValue === '='",
                        "for (let i = 0; i < limit; i = i + 1)",
                        "token.tokenValue === 'for'",
                        "token.tokenValue === 'continue'",
                        "token.tokenValue === 'break'",
                        "do {",
                "} while (i < limit)",
                "token.tokenValue === 'do'",
                "token.tokenValue === 'countAtLeastOnce'",
                "collect(values: List)",
                "for (const item of values)",
                "token.tokenValue === 'of'",
                "token.tokenValue === 'item'",
                "switchStatus(status: string)",
                "switch (status)",
                "case \"ready\":",
                "default:",
                "token.tokenValue === 'switch'",
                "token.tokenValue === 'case'",
                "token.tokenValue === 'default'",
                "token.tokenValue === 'switchStatus'",
                "const moduleUrl = import.meta.url",
                        "const loadedModule = import(\"./dep.qin\")",
                        "token.tokenValue === 'import'",
                        "token.tokenValue === 'meta'",
                        "String(token.tokenValue).includes('./dep.qin')",
                        "const optionalName = user?.profile?.name",
                        "token.tokenName === 'QuestionDot'",
                        "token.tokenValue === 'optionalName'",
                        "const fallbackName = user.name ?? \"anonymous\"",
                        "token.tokenName === 'NullishCoalescing'",
                        "token.tokenValue === 'fallbackName'",
                        "const templateName = `hello ${name}`",
                        "token.tokenName === 'TemplateHead'",
                        "token.tokenValue === 'templateName'",
                        "div(class = css { displayFlex })",
                        "parsedTokens.some((token: any) => token.tokenValue === 'object')",
                        "parsedTokens.some((token: any) => token.tokenValue === 'css')",
                        "requireNoDependency",
                        "'ovs-language-server', 'package.json'",
                        "'slime-ast', 'slime-parser', 'slime-token', 'subhuti'"));
        verifyGeneratedParserChainRuntimeSmoke(
                "ovs-compiler",
                workspaceRoot,
                ovsCompilerRoot.resolve("tests").resolve("test-generated-parser-chain.ts").normalize(),
                List.of(
                        "new OvsParser(",
                        "parser instanceof CssTsParser",
                        "parser instanceof SlimeJavascriptParser",
                        "object NestedLabeler",
                        "export interface ChainUser",
                        "export type ChainPair<T, U>",
                        "class ChainService",
                        "constructor(name: string)",
                        "const { name: destructuredName, values: [firstValue] } = config",
                        "token.tokenValue === 'ChainUser'",
                        "token.tokenValue === 'ChainPair'",
                        "token.tokenValue === 'ChainService'",
                        "token.tokenValue === 'constructor'",
                        "token.tokenValue === 'destructuredName'",
                        "token.tokenValue === 'firstValue'",
                        "token.tokenValue === 'premium'",
                        "token.tokenValue === 'standard'",
                        "token.tokenValue === 'try'",
                        "token.tokenValue === 'catch'",
                        "token.tokenValue === 'throw'",
                        "let total = 0",
                        "while (total < limit)",
                        "total = total + 1",
                        "token.tokenValue === 'while'",
                        "token.tokenValue === 'total'",
                        "token.tokenValue === '='",
                        "for (let i = 0; i < limit; i = i + 1)",
                        "token.tokenValue === 'for'",
                        "token.tokenValue === 'continue'",
                        "token.tokenValue === 'break'",
                        "do {",
                "} while (i < limit)",
                "token.tokenValue === 'do'",
                "token.tokenValue === 'countAtLeastOnce'",
                "collect(values: List)",
                "for (const item of values)",
                "token.tokenValue === 'of'",
                "token.tokenValue === 'item'",
                "switchStatus(status: string)",
                "switch (status)",
                "case \"ready\":",
                "default:",
                "token.tokenValue === 'switch'",
                "token.tokenValue === 'case'",
                "token.tokenValue === 'default'",
                "token.tokenValue === 'switchStatus'",
                "const moduleUrl = import.meta.url",
                        "const loadedModule = import(\"./dep.qin\")",
                        "token.tokenValue === 'import'",
                        "token.tokenValue === 'meta'",
                        "String(token.tokenValue).includes('./dep.qin')",
                        "const optionalName = user?.profile?.name",
                        "token.tokenName === 'QuestionDot'",
                        "token.tokenValue === 'optionalName'",
                        "const fallbackName = user.name ?? \"anonymous\"",
                        "token.tokenName === 'NullishCoalescing'",
                        "token.tokenValue === 'fallbackName'",
                        "const templateName = `hello ${name}`",
                        "token.tokenName === 'TemplateHead'",
                        "token.tokenValue === 'templateName'",
                        "div(class = css { displayFlex })",
                        "parsedTokens.some((token: any) => token.tokenValue === 'object')",
                        "parsedTokens.some((token: any) => token.tokenValue === 'css')"));
    }

    private static void verifyLegacyAstImportsStayInCstToAstBoundary(
            Path workspaceRoot,
            Path csstsCompilerRoot,
            Path ovsCompilerRoot) throws Exception {
        for (Path root : List.of(csstsCompilerRoot, ovsCompilerRoot)) {
            require(root.startsWith(workspaceRoot),
                    "Legacy parser import scan root must stay inside workspace: " + root);
        }
        Set<String> allowed = Set.of(
                "cssts/cssts/cssts-compiler/src/factory/CssTsCstToAstUtils.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.Statement.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.View.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.Property.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.Judgement.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.Import.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.IIFE.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/OvsCstToSlimeAst/OvsCstToSlimeAst.Helpers.ts",
                "ovsjs/ovs/ovs-compiler/src/factory/helpers/html-tags.ts",
                "ovsjs/ovs/ovs-compiler/src/index.ts");
        Set<String> legacyAstImports = new LinkedHashSet<>();
        Set<String> legacyParserImports = new LinkedHashSet<>();
        for (Path root : List.of(csstsCompilerRoot, ovsCompilerRoot)) {
            try (var paths = Files.find(
                    root.resolve("src"),
                    12,
                    (path, attributes) -> attributes.isRegularFile()
                            && path.getFileName().toString().endsWith(".ts"))) {
                for (Path sourcePath : paths.toList()) {
                    String source = Files.readString(sourcePath);
                    String relative = workspaceRoot.relativize(sourcePath.normalize()).toString().replace('\\', '/');
                    if (containsPackageImport(source, "slime-ast")) {
                        legacyAstImports.add(relative);
                    }
                    if (containsPackageImport(source, "slime-parser")) {
                        legacyParserImports.add(relative);
                    }
                }
            }
        }
        require(legacyParserImports.isEmpty(),
                "Legacy TS slime-parser imports must be removed; generated SlimeCstToAstBridge owns CST-to-AST base. actual="
                        + legacyParserImports);
        require(legacyAstImports.equals(allowed),
                "Legacy TS slime-ast imports must stay confined to explicit AST construction boundaries. expected="
                        + allowed + " actual=" + legacyAstImports);
    }

    private static boolean containsPackageImport(String source, String packageName) {
        return source.contains("from \"" + packageName + "\"")
                || source.contains("from '" + packageName + "'")
                || source.contains("import(\"" + packageName + "\")")
                || source.contains("import('" + packageName + "')");
    }

    private static void verifyGeneratedParserChainRuntimeSmoke(
            String id,
            Path workspaceRoot,
            Path smokePath,
            List<String> requiredNeedles) throws Exception {
        require(smokePath.startsWith(workspaceRoot),
                id + " generated parser chain smoke must stay inside workspace: " + smokePath);
        require(Files.isRegularFile(smokePath),
                id + " generated parser chain smoke must exist: " + smokePath);
        String source = Files.readString(smokePath);
        String searchableSource = source + "\n" + readLocalTypeScriptImports(smokePath, workspaceRoot, source);
        for (String requiredNeedle : requiredNeedles) {
            require(searchableSource.contains(requiredNeedle),
                    id + " generated parser chain smoke must assert " + requiredNeedle);
        }
    }

    private static String readLocalTypeScriptImports(Path sourcePath, Path workspaceRoot, String source) throws Exception {
        StringBuilder builder = new StringBuilder();
        Set<Path> visited = new LinkedHashSet<>();
        collectLocalTypeScriptImports(sourcePath, workspaceRoot, source, visited, builder);
        return builder.toString();
    }

    private static void collectLocalTypeScriptImports(
            Path sourcePath,
            Path workspaceRoot,
            String source,
            Set<Path> visited,
            StringBuilder builder) throws Exception {
        Matcher matcher = LOCAL_TYPESCRIPT_IMPORT.matcher(source);
        while (matcher.find()) {
            String importPath = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            Path resolved = resolveLocalTypeScriptImport(sourcePath.getParent(), importPath);
            if (resolved == null || !resolved.startsWith(workspaceRoot) || !Files.isRegularFile(resolved) || !visited.add(resolved)) {
                continue;
            }
            String importedSource = Files.readString(resolved);
            builder.append("\n// ").append(resolved).append("\n").append(importedSource);
            collectLocalTypeScriptImports(resolved, workspaceRoot, importedSource, visited, builder);
        }
    }

    private static Path resolveLocalTypeScriptImport(Path parent, String importPath) {
        Path base = parent.resolve(importPath).normalize();
        if (Files.isRegularFile(base)) {
            return base;
        }
        for (String extension : List.of(".ts", ".mts", ".cts", ".js", ".mjs", ".cjs")) {
            Path candidate = parent.resolve(importPath + extension).normalize();
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        Path indexTs = parent.resolve(importPath).resolve("index.ts").normalize();
        return Files.isRegularFile(indexTs) ? indexTs : null;
    }

    private static void verifyLanguageToolingDocumentation(Path workspaceRoot) throws Exception {
        verifyLanguageTsdownConfig(
                "OVS language",
                workspaceRoot.resolve("ovsjs").resolve("ovs-language").normalize());
        verifyLanguageTsdownConfig(
                "CSSTS language",
                workspaceRoot.resolve("cssts").resolve("cssts-language").normalize());
        verifyDocumentationNeedles(
                "OVS workspace README",
                workspaceRoot.resolve("ovsjs").resolve("README.md").normalize(),
                List.of(
                        "generated user app",
                        "Qin-managed through `qin.config.js`",
                        "Generated Qin Parser",
                        "Java QinParser generated to TypeScript",
                        "..\\qin\\qin.bat language build --root ovs/ovs-compiler",
                        "..\\qin\\qin.bat language test --root ovs-language"));
        verifyOvsCompilerGeneratedParserDocumentation(workspaceRoot);
        verifyDocumentationNeedles(
                "OVS language README",
                workspaceRoot.resolve("ovsjs").resolve("ovs-language").resolve("README.md").normalize(),
                List.of(
                        "Toolchain Development",
                        "..\\..\\qin\\qin.bat language build",
                        "..\\..\\qin\\qin.bat language test",
                        "..\\..\\qin\\qin.bat language server --dry-run",
                        "generated Qin parser",
                        "do not add a separate parser",
                        "fallback"));
        verifyDocumentationNeedles(
                "CSSTS language README",
                workspaceRoot.resolve("cssts").resolve("cssts-language").resolve("README.md").normalize(),
                List.of(
                        "..\\..\\qin\\qin.bat language build",
                        "..\\..\\qin\\qin.bat language test",
                        "..\\..\\qin\\qin.bat language server --dry-run",
                        "generated Qin parser",
                        "do not add a separate parser",
                        "fallback path"));
        verifyDocumentationNeedles(
                "CSSTS architecture README",
                workspaceRoot.resolve("cssts").resolve("ARCHITECTURE.md").normalize(),
                List.of(
                        "..\\..\\qin\\qin.bat language build",
                        "..\\..\\qin\\qin.bat language test",
                        "..\\..\\qin\\qin.bat language server --dry-run",
                        "IDEA LSP client",
                        "不再作为独立 VSCode 扩展发布"));
        verifyCsstsCompilerGeneratedParserDocumentation(workspaceRoot);
    }

    private static void verifyCsstsCompilerGeneratedParserDocumentation(Path workspaceRoot) throws Exception {
        for (Path documentationPath : List.of(
                workspaceRoot.resolve("cssts").resolve("cssts").resolve("cssts-compiler").resolve("README.md").normalize(),
                workspaceRoot.resolve("cssts").resolve("cssts").resolve("cssts-compiler").resolve("llms.txt").normalize())) {
            require(Files.isRegularFile(documentationPath),
                    "CSSTS compiler generated parser documentation must exist: " + documentationPath);
            String source = Files.readString(documentationPath);
            require(source.contains("generated Qin parser"),
                    "CSSTS compiler documentation must describe parser inheritance through generated Qin parser: "
                            + documentationPath);
            require(source.contains("QinParser"),
                    "CSSTS compiler documentation must name QinParser as the parser base: " + documentationPath);
            for (String forbiddenNeedle : List.of(
                    "继承 slime-parser",
                    "继承 SlimeParser",
                    "CssTsParser 继承 SlimeParser",
                    "slime-parser (JS/TS 解析器)",
                    "npm run build",
                    "mono test-compiler.mjs",
                    "`mono`")) {
                require(!source.contains(forbiddenNeedle),
                        "CSSTS compiler documentation must not keep legacy parser or non-Qin tooling directive: "
                                + documentationPath);
            }
        }
    }

    private static void verifyOvsCompilerGeneratedParserDocumentation(Path workspaceRoot) throws Exception {
        Path documentationPath = workspaceRoot.resolve("ovsjs")
                .resolve("ovs")
                .resolve("ovs-compiler")
                .resolve("README.md")
                .normalize();
        require(Files.isRegularFile(documentationPath),
                "OVS compiler generated parser documentation must exist: " + documentationPath);
        String source = Files.readString(documentationPath);
        for (String requiredNeedle : List.of(
                "Parser Authority",
                "OvsParser extends CssTsParser",
                "CssTsParser extends QinParser",
                "Java 版 QinParser 生成到 TypeScript",
                "@SubhutiRule",
                "不要用正则扫描、字符串补丁或 fallback transform",
                "slime-parser` 只在 CST-to-AST 转换注册边界保留")) {
            require(source.contains(requiredNeedle),
                    "OVS compiler documentation must describe generated parser authority: " + requiredNeedle);
        }
    }

    private static void verifyLanguageTsdownConfig(String label, Path languageRoot) {
        Path esmConfig = languageRoot.resolve("tsdown.config.mts").normalize();
        Path typelessConfig = languageRoot.resolve("tsdown.config.ts").normalize();
        require(Files.isRegularFile(esmConfig),
                label + " must use explicit ESM tsdown.config.mts: " + esmConfig);
        require(!Files.exists(typelessConfig),
                label + " must not use typeless tsdown.config.ts for Volar tooling: " + typelessConfig);
    }

    private static void verifyDocumentationNeedles(
            String label,
            Path documentationPath,
            List<String> requiredNeedles) throws Exception {
        require(Files.isRegularFile(documentationPath),
                label + " must exist: " + documentationPath);
        String source = Files.readString(documentationPath);
        for (String requiredNeedle : requiredNeedles) {
            require(source.contains(requiredNeedle),
                    label + " must document " + requiredNeedle);
        }
        for (String forbiddenNeedle : List.of("npm run build", "npm run package")) {
            require(!source.contains(forbiddenNeedle),
                    label + " must keep Qin as the documented language tooling entrypoint, not "
                            + forbiddenNeedle);
        }
    }

    private static void verifyLanguageServerFeatureAssertions(MatrixCase matrixCase, Path projectRoot) throws Exception {
        Path testFile = projectRoot.resolve(matrixCase.languageServerTest()).normalize();
        require(Files.isRegularFile(testFile),
                matrixCase.id() + " language server test must exist: " + testFile);
        String testSource = Files.readString(testFile);
        for (String method : List.of(
                "textDocument/completion",
                "textDocument/definition",
                "textDocument/declaration",
                "textDocument/references",
                "textDocument/documentSymbol",
                "textDocument/semanticTokens/full")) {
            require(testSource.contains(method),
                    matrixCase.id() + " language server test must request " + method);
        }
        for (String assertionNeedle : List.of(
                "completion did not include",
                "definition did not resolve",
                "declaration did not resolve",
                "references did not include",
                "documentSymbol did not include",
                "semanticTokens did not return token data")) {
            require(testSource.contains(assertionNeedle),
                    matrixCase.id() + " language server test must assert " + assertionNeedle);
        }
        if ("qin".equals(matrixCase.id())) {
            require(testSource.contains("textDocument/codeAction"),
                    "Qin language server test must request textDocument/codeAction");
            require(testSource.contains("codeAction/resolve"),
                    "Qin language server test must request codeAction/resolve");
            require(testSource.contains("codeAction resolve did not preserve import-policy quickfix edit"),
                    "Qin language server test must assert codeAction resolve quickfix coverage");
            require(testSource.contains("completionItem/resolve"),
                    "Qin language server test must request completionItem/resolve");
            require(testSource.contains("completionItem resolve did not preserve label and detail"),
                    "Qin language server test must assert completionItem resolve detail coverage");
            require(testSource.contains("Remove forbidden java import"),
                    "Qin language server test must assert import-policy codeAction coverage");
            require(testSource.contains("Qin import-policy codeAction resolve response"),
                    "Qin language server test must assert import-policy codeAction resolve response");
            require(testSource.contains("Remove forbidden shared import"),
                    "Qin language server test must assert shared import-policy codeAction coverage");
            require(testSource.contains("import-policy hover did not explain app java: boundary"),
                    "Qin language server test must assert import-policy hover explanation coverage");
            require(testSource.contains("shared import-policy hover did not explain bare import boundary"),
                    "Qin language server test must assert shared bare import-policy hover explanation coverage");
            require(testSource.contains("textDocument/formatting"),
                    "Qin language server test must request textDocument/formatting");
            require(testSource.contains("formatting did not return TypeScript formatter edits through source mappings"),
                    "Qin language server test must assert formatting source-map coverage");
            require(testSource.contains("textDocument/rangeFormatting"),
                    "Qin language server test must request textDocument/rangeFormatting");
            require(testSource.contains("rangeFormatting did not return TypeScript formatter edits through source mappings"),
                    "Qin language server test must assert rangeFormatting source-map coverage");
            require(testSource.contains("textDocument/onTypeFormatting"),
                    "Qin language server test must request textDocument/onTypeFormatting");
            require(testSource.contains("onTypeFormatting did not return TypeScript formatter edits through source mappings"),
                    "Qin language server test must assert onTypeFormatting source-map coverage");
            require(testSource.contains("textDocument/semanticTokens/range"),
                    "Qin language server test must request textDocument/semanticTokens/range");
            require(testSource.contains("semanticTokens range did not return token data"),
                    "Qin language server test must assert semanticTokens range coverage");
            require(testSource.contains("textDocument/typeDefinition"),
                    "Qin language server test must request textDocument/typeDefinition");
            require(testSource.contains("typeDefinition did not resolve currentUser to source interface"),
                    "Qin language server test must assert typeDefinition source-map coverage");
            require(testSource.contains("textDocument/declaration"),
                    "Qin language server test must request textDocument/declaration");
            require(testSource.contains("declaration did not resolve alphaNumber declaration"),
                    "Qin language server test must assert declaration source-map coverage");
            require(testSource.contains("textDocument/implementation"),
                    "Qin language server test must request textDocument/implementation");
            require(testSource.contains("implementation did not resolve interface to source class"),
                    "Qin language server test must assert implementation source-map coverage");
            require(testSource.contains("textDocument/prepareCallHierarchy"),
                    "Qin language server test must request textDocument/prepareCallHierarchy");
            require(testSource.contains("callHierarchy/incomingCalls"),
                    "Qin language server test must request callHierarchy/incomingCalls");
            require(testSource.contains("callHierarchy/outgoingCalls"),
                    "Qin language server test must request callHierarchy/outgoingCalls");
            require(testSource.contains("incomingCalls did not resolve source caller and callsite"),
                    "Qin language server test must assert incoming call hierarchy source-map coverage");
            require(testSource.contains("outgoingCalls did not resolve source callee and callsite"),
                    "Qin language server test must assert outgoing call hierarchy source-map coverage");
            require(testSource.contains("textDocument/inlayHint"),
                    "Qin language server test must request textDocument/inlayHint");
            require(testSource.contains("inlayHint did not include parameter or variable type hints through source mappings"),
                    "Qin language server test must assert inlayHint source-map coverage");
            require(testSource.contains("textDocument/foldingRange"),
                    "Qin language server test must request textDocument/foldingRange");
            require(testSource.contains("foldingRange did not include"),
                    "Qin language server test must assert foldingRange source coverage");
            require(testSource.contains("textDocument/selectionRange"),
                    "Qin language server test must request textDocument/selectionRange");
            require(testSource.contains("selectionRange did not include"),
                    "Qin language server test must assert selectionRange source coverage");
            require(testSource.contains("textDocument/linkedEditingRange"),
                    "Qin language server test must request textDocument/linkedEditingRange");
            require(testSource.contains("linkedEditingRange did not include"),
                    "Qin language server test must assert linkedEditingRange source coverage");
            require(testSource.contains("textDocument/documentLink"),
                    "Qin language server test must request textDocument/documentLink");
            require(testSource.contains("documentLink did not include local import target"),
                    "Qin language server test must assert documentLink local import coverage");
            require(testSource.contains("volar/client/findFileReference"),
                    "Qin language server test must request volar/client/findFileReference");
            require(testSource.contains("fileReferences did not include provider import usage"),
                    "Qin language server test must assert fileReferences local import coverage");
            require(testSource.contains("workspace/willRenameFiles"),
                    "Qin language server test must request workspace/willRenameFiles");
            require(testSource.contains("fileRenameEdits did not update local import specifier"),
                    "Qin language server test must assert fileRenameEdits local import coverage");
            require(testSource.contains("qin-import-policy"),
                    "Qin language server test must assert app/shared import-policy diagnostics");
            require(testSource.contains("app code cannot import java modules"),
                    "Qin language server test must assert app java: import rejection");
            require(testSource.contains("shared code cannot import java modules"),
                    "Qin language server test must assert shared java: import rejection");
            require(testSource.contains("shared code cannot import bare/non-local modules"),
                    "Qin language server test must assert shared bare import rejection");
            require(testSource.contains("workspace/symbol"),
                    "Qin language server test must request workspace/symbol");
            require(testSource.contains("workspaceSymbol did not include"),
                    "Qin language server test must assert workspaceSymbol source coverage");
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
        for (String diagnosticNeedle : languageDiagnosticFeatureNeedles(matrixCase.id(), matrixCase.extension())) {
            require(testSource.contains(diagnosticNeedle),
                    matrixCase.id() + " language server test must cover diagnostic feature: "
                            + diagnosticNeedle);
        }
        require(testSource.contains("requireSemanticTokenAt"),
                matrixCase.id() + " language server test must assert semantic token source positions");
        for (String semanticNeedle : languageSemanticTokenNeedles(matrixCase.id())) {
            require(testSource.contains(semanticNeedle),
                    matrixCase.id() + " language server test must cover semantic token position: "
                            + semanticNeedle);
        }
    }

    private static void verifyQinLanguagePluginFeatureMappings(Path qinLanguageRoot) throws Exception {
        Path pluginPath = qinLanguageRoot.resolve("qin-language-server")
                .resolve("src")
                .resolve("QinLanguagePlugin.ts")
                .normalize();
        require(Files.isRegularFile(pluginPath),
                "QinLanguagePlugin source must exist: " + pluginPath);
        String source = Files.readString(pluginPath);
        for (String mappingNeedle : List.of(
                "completion: true",
                "format: true",
                "inlayHints: true",
                "navigation: true",
                "semantic: item.semantic ?? true",
                "verification: true")) {
            require(source.contains(mappingNeedle),
                    "Qin virtual code mappings must enable " + mappingNeedle);
        }
    }

    private static void verifyQinJavaSourceSymbolSmoke(Path qinLanguageRoot) throws Exception {
        Path smokePath = qinLanguageRoot.resolve("tests")
                .resolve("test-java-source-symbols.ts")
                .normalize();
        require(Files.isRegularFile(smokePath),
                "Qin Java source symbol smoke must exist: " + smokePath);
        String source = Files.readString(smokePath);
        for (String requiredNeedle : List.of(
                "buildJavaSourceSymbolDts(URI.file(qinSourcePath), qinSource)",
                "static readonly DEFAULT_NAME: string;",
                "static greet(name: string): string;",
                "static COUNT: number;",
                "static count(): number;",
                "Java source symbol model leaked Counter members into Greeter",
                "Java source symbol model leaked Greeter members into Counter")) {
            require(source.contains(requiredNeedle),
                    "Qin Java source symbol smoke must cover " + requiredNeedle);
        }
    }

    private static void verifyQinLanguageServicePluginFeatureMappings(Path qinLanguageRoot) throws Exception {
        Path pluginPath = qinLanguageRoot.resolve("qin-language-server")
                .resolve("src")
                .resolve("QinLanguageServicePlugin.ts")
                .normalize();
        require(Files.isRegularFile(pluginPath),
                "QinLanguageServicePlugin source must exist: " + pluginPath);
        String source = Files.readString(pluginPath);
        for (String requiredNeedle : List.of(
                "resolveCodeAction",
                "isQinImportPolicyCodeAction",
                "source: 'qin-import-policy'")) {
            require(source.contains(requiredNeedle),
                    "Qin language service plugin must keep import-policy codeAction resolve support: "
                            + requiredNeedle);
        }
    }

    private static List<String> languageSyntaxFeatureNeedles(String languageId) {
        return switch (languageId) {
            case "qin" -> List.of(
                    "Qin object completion response",
                    "Qin completionItem resolve response",
                    "Qin object extends completion response",
                    "Qin object extends definition response",
                    "Qin cross-file import completion response",
                    "Qin cross-file import symbol definition response",
                    "Qin cross-file import member definition response",
                    "Qin TS-subset signatureHelp response",
                    "Qin typeDefinition response",
                    "Qin implementation response",
                    "Qin prepareCallHierarchy response",
                    "Qin incomingCalls response",
                    "Qin outgoingCalls response",
                    "Qin TS-subset documentHighlight response",
                    "Qin formatting response",
                    "Qin rangeFormatting response",
                    "Qin onTypeFormatting response",
                    "Qin object semanticTokens range response",
                    "Qin TS-subset rename response",
                    "Qin TS-subset prepareRename response",
                    "Qin object definition response",
                    "Qin object references response",
                    "Qin object documentSymbol response",
                    "objectSymbolNames.includes('__QinObject_Counter')",
                    "generated object");
            case "ovs" -> List.of(
                    "OVS syntax completion response",
                    "OVS syntax definition response",
                    "OVS syntax references response",
                    "OVS syntax documentSymbol response",
                    "OVS for...of completion response",
                    "OVS for...of definition response",
                    "OVS for...of references response",
                    "OVS for...of documentSymbol response",
                    "css { displayFlex }",
                    "labelText");
            case "cssts" -> List.of(
                    "CSSTS css syntax completion response",
                    "CSSTS css syntax definition response",
                    "CSSTS css syntax references response",
                    "CSSTS css syntax documentSymbol response",
                    "CSSTS for...of completion response",
                    "CSSTS for...of definition response",
                    "CSSTS for...of references response",
                    "CSSTS for...of documentSymbol response",
                    "css { colorRed, displayFlex }",
                    "derivedStyle");
            default -> throw new IllegalStateException("Unsupported language id: " + languageId);
        };
    }

    private static List<String> languageDiagnosticFeatureNeedles(String languageId, String extension) {
        return switch (languageId) {
            case "qin" -> List.of();
            case "ovs", "cssts" -> List.of(
                    "qin-rich-valid" + extension,
                    "object NestedLabeler",
                    "const label = \"vip \"",
                    "const standard = \"std \"",
                    languageId.equals("ovs")
                            ? "OVS Qin-rich valid diagnostic response"
                            : "CSSTS Qin-rich valid diagnostic response",
                    languageId.equals("ovs")
                            ? "Qin-rich valid OVS source produced transform diagnostics"
                            : "Qin-rich valid CSSTS source produced transform diagnostics");
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
                    "OVS syntax labelText render usage",
                    "OVS for...of item declaration",
                    "OVS for...of item render usage");
            case "cssts" -> List.of(
                    "CSSTS css syntax baseStyle declaration",
                    "CSSTS css syntax baseStyle usage",
                    "CSSTS for...of row declaration",
                    "CSSTS for...of row usage");
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
        Path parserFacadePath = qinLanguageRoot.getParent()
                .resolve("qin-parser")
                .resolve("src")
                .resolve("java")
                .resolve("com")
                .resolve("qin")
                .resolve("parser")
                .resolve("QinParserFacade.java")
                .normalize();
        Path parserFacadeSmokePath = qinLanguageRoot.getParent()
                .resolve("qin-parser")
                .resolve("src")
                .resolve("java")
                .resolve("com")
                .resolve("qin")
                .resolve("parser")
                .resolve("QinParserFacadeUnifiedEntrySmokeTestMain.java")
                .normalize();
        Path parserRuntimeNamesPath = qinLanguageRoot.getParent()
                .resolve("qin-parser")
                .resolve("src")
                .resolve("java")
                .resolve("com")
                .resolve("qin")
                .resolve("parser")
                .resolve("QinParserRuntimeNames.java")
                .normalize();
        Path frontendAdapterPath = qinLanguageRoot.getParent()
                .resolve("qin-lang-frontend-adapter")
                .resolve("src")
                .resolve("java")
                .resolve("com")
                .resolve("qin")
                .resolve("lang")
                .resolve("frontend")
                .resolve("adapter")
                .resolve("QinSlimeFrontendAdapter.java")
                .normalize();
        Path importMetaShimSmokePath = frontendAdapterPath.resolveSibling(
                "QinRuntimeImportMetaShimRemovalSmokeTestMain.java").normalize();
        require(Files.isRegularFile(parityTestPath),
                "Qin generated parser parity test must exist: " + parityTestPath);
        require(Files.isRegularFile(parserFacadePath),
                "Qin parser facade must exist: " + parserFacadePath);
        require(Files.isRegularFile(parserFacadeSmokePath),
                "Qin parser facade smoke must exist: " + parserFacadeSmokePath);
        require(Files.isRegularFile(parserRuntimeNamesPath),
                "Qin parser runtime names must exist: " + parserRuntimeNamesPath);
        require(Files.isRegularFile(frontendAdapterPath),
                "Qin frontend adapter must exist: " + frontendAdapterPath);
        require(Files.isRegularFile(importMetaShimSmokePath),
                "Qin import.meta shim removal smoke must exist: " + importMetaShimSmokePath);
        String paritySource = Files.readString(parityTestPath);
        String parserFacadeSource = Files.readString(parserFacadePath);
        String parserFacadeSmokeSource = Files.readString(parserFacadeSmokePath);
        String parserRuntimeNamesSource = Files.readString(parserRuntimeNamesPath);
        String frontendAdapterSource = Files.readString(frontendAdapterPath);
        String importMetaShimSmokeSource = Files.readString(importMetaShimSmokePath);
        require(parserFacadeSource.contains("SubhutiParser.create(QinParser.class, source)")
                        && !parserFacadeSource.contains("new QinParser("),
                "QinParserFacade must create QinParser through SubhutiParser.create, not raw construction");
        require(!parserFacadeSource.contains("rewriteSimpleSwitchStatements")
                        && !parserFacadeSource.contains("__qin_switch_"),
                "QinParserFacade must not lower switch syntax with string rewrites");
        require(!parserFacadeSource.contains("SOURCE_IMPORT_META_URL_PATTERN")
                        && !parserFacadeSource.contains("SOURCE_DYNAMIC_IMPORT_PATTERN")
                        && !parserFacadeSource.contains("SOURCE_TYPEOF_DYNAMIC_IMPORT_SHIM_PATTERN")
                        && !parserFacadeSource.contains("IMPORT_META_URL_SHIM")
                        && !parserFacadeSource.contains("DYNAMIC_IMPORT_SHIM"),
                "QinParserFacade must not lower import.meta.url or dynamic import with string rewrites");
        require(parserFacadeSmokeSource.contains("switchParsed.effectiveSource().contains(\"switch (value)\")")
                        && parserFacadeSmokeSource.contains("Switch syntax must not be lowered by QinParserFacade"),
                "QinParserFacade smoke must prove switch syntax stays in parser input");
        require(parserFacadeSmokeSource.contains("runtimeSyntaxParsed.effectiveSource().contains(\"import.meta.url\")")
                        && parserFacadeSmokeSource.contains("runtimeSyntaxParsed.effectiveSource().contains(\"import(\\\"./dep.qin\\\")\")")
                        && parserFacadeSmokeSource.contains("Runtime ESM syntax must not be lowered by QinParserFacade"),
                "QinParserFacade smoke must prove import.meta.url and dynamic import stay in parser input");
        require(!parserRuntimeNamesSource.contains("IMPORT_META_URL_SHIM")
                        && !parserRuntimeNamesSource.contains("__qin_import_meta_url__"),
                "QinParserRuntimeNames must not keep old import.meta.url parser shim names");
        require(!frontendAdapterSource.contains("IMPORT_META_URL_SHIM")
                        && !frontendAdapterSource.contains("__qin_import_meta_url__"),
                "QinSlimeFrontendAdapter must not lower old import.meta.url shim identifiers");
        require(importMetaShimSmokeSource.contains("QinRuntimeImportMetaShimRemovalSmokeTestMain")
                        && importMetaShimSmokeSource.contains("new Identifier(\"__qin_import_meta_url__\"")
                        && importMetaShimSmokeSource.contains("QinIrIdentifierReference")
                        && importMetaShimSmokeSource.contains("new MetaProperty(")
                        && importMetaShimSmokeSource.contains("\"import.meta.url\""),
                "Qin import.meta shim removal smoke must prove the old shim is ordinary identifier while formal AST still lowers");
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
        require(paritySource.contains("qin object nested method body control flow"),
                "Qin generated parser parity corpus must include object nested method-body control flow");
        require(paritySource.contains("export object NestedLabeler"),
                "Qin generated parser parity corpus must include a nested-control Qin object declaration");
        require(paritySource.contains("if (active)") && paritySource.contains("if (premium)"),
                "Qin generated parser parity corpus must include nested object method if branches");
        require(paritySource.contains("const label = \"vip \"")
                        && paritySource.contains("const standard = \"std \""),
                "Qin generated parser parity corpus must include nested object method branch-local bindings");
        require(paritySource.contains("qin object method body exception flow"),
                "Qin generated parser parity corpus must include object method exception flow");
        require(paritySource.contains("export object ResilientLabeler"),
                "Qin generated parser parity corpus must include exception-flow Qin object declaration");
        require(paritySource.contains("try {")
                        && paritySource.contains("throw new Error(\"boom\")")
                        && paritySource.contains("} catch (error) {"),
                "Qin generated parser parity corpus must include try/catch/throw syntax");
        require(paritySource.contains("while loop break continue control flow"),
                "Qin generated parser parity corpus must include while-loop control flow");
        require(paritySource.contains("while (index < values.length)")
                        && paritySource.contains("continue")
                        && paritySource.contains("break"),
                "Qin generated parser parity corpus must include while/break/continue syntax");
        require(paritySource.contains("switch statement control flow"),
                "Qin generated parser parity corpus must include switch statement control flow");
        require(paritySource.contains("switch (status)")
                        && paritySource.contains("case \"ready\":")
                        && paritySource.contains("default:"),
                "Qin generated parser parity corpus must include switch/case/default syntax");
        require(paritySource.contains("import meta url expression")
                        && paritySource.contains("import.meta.url"),
                "Qin generated parser parity corpus must include import.meta.url syntax");
        require(paritySource.contains("dynamic import expression")
                        && paritySource.contains("await import(\"./dep.qin\")"),
                "Qin generated parser parity corpus must include dynamic import syntax");
        for (String caseName : List.of(
                "qin object nested method body control flow",
                "qin object method body exception flow",
                "decorated qin object",
                "default export qin object",
                "object keyword in type alias",
                "java import class extends",
                "local class extends",
                "interface and type exports",
                "generic function",
                "generic interface and object type alias",
                "namespace export",
                "enum export",
                "import export variants",
                "type import export variants",
                "class method expressions",
                "class fields and constructor",
                "decorated class and method",
                "control flow in function body",
                "while loop break continue control flow",
                "switch statement control flow",
                "optional chaining expression",
                "nullish coalescing expression",
                "template literal expression",
                "import meta url expression",
                "dynamic import expression",
                "destructuring declarations",
                "async await function",
                "invalid unclosed import",
                "invalid unclosed decorator",
                "invalid unclosed class")) {
            require(paritySource.contains("name: '" + caseName + "'"),
                    "Qin generated parser parity corpus must include case: " + caseName);
        }
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
        Path nestedBranchPath = corpusPath.resolveSibling("QinJvmParsedNestedBranchMethodBodySmokeTestMain.java");
        Path selfMethodCallPath = corpusPath.resolveSibling("QinJvmParsedSelfMethodCallSmokeTestMain.java");
        Path tryCatchPath = corpusPath.resolveSibling("QinJvmParsedTryCatchMethodBodySmokeTestMain.java");
        Path whilePath = corpusPath.resolveSibling("QinJvmParsedWhileMethodBodySmokeTestMain.java");
        Path whileMutableLocalPath = corpusPath.resolveSibling("QinJvmParsedWhileMutableLocalSmokeTestMain.java");
        Path forBreakContinuePath = corpusPath.resolveSibling("QinJvmParsedForBreakContinueSmokeTestMain.java");
        Path doWhilePath = corpusPath.resolveSibling("QinJvmParsedDoWhileSmokeTestMain.java");
        Path forOfPath = corpusPath.resolveSibling("QinJvmParsedForOfSmokeTestMain.java");
        Path switchPath = corpusPath.resolveSibling("QinJvmParsedSwitchSmokeTestMain.java");
        Path sequencePath = corpusPath.resolveSibling("QinJvmSequenceConsoleReturnSmokeTestMain.java");
        Path slimeParserExtendsPath = corpusPath.resolveSibling("QinJvmJavaSlimeParserExtendsSmokeTestMain.java");
        require(Files.isRegularFile(corpusPath),
                "Qin JVM class declaration corpus smoke must exist: " + corpusPath);
        require(Files.isRegularFile(earlyReturnPath),
                "Qin JVM parsed early-return method-body smoke must exist: " + earlyReturnPath);
        require(Files.isRegularFile(nestedBranchPath),
                "Qin JVM parsed nested-branch method-body smoke must exist: " + nestedBranchPath);
        require(Files.isRegularFile(selfMethodCallPath),
                "Qin JVM parsed self-method-call smoke must exist: " + selfMethodCallPath);
        require(Files.isRegularFile(tryCatchPath),
                "Qin JVM parsed try/catch method-body smoke must exist: " + tryCatchPath);
        require(Files.isRegularFile(whilePath),
                "Qin JVM parsed while method-body smoke must exist: " + whilePath);
        require(Files.isRegularFile(whileMutableLocalPath),
                "Qin JVM parsed while mutable-local smoke must exist: " + whileMutableLocalPath);
        require(Files.isRegularFile(forBreakContinuePath),
                "Qin JVM parsed for/break/continue smoke must exist: " + forBreakContinuePath);
        require(Files.isRegularFile(doWhilePath),
                "Qin JVM parsed do-while smoke must exist: " + doWhilePath);
        require(Files.isRegularFile(forOfPath),
                "Qin JVM parsed for...of smoke must exist: " + forOfPath);
        require(Files.isRegularFile(switchPath),
                "Qin JVM parsed switch smoke must exist: " + switchPath);
        require(Files.isRegularFile(sequencePath),
                "Qin JVM sequence expression smoke must exist: " + sequencePath);
        require(Files.isRegularFile(slimeParserExtendsPath),
                "Qin JVM Java SlimeParser inheritance smoke must exist: " + slimeParserExtendsPath);

        String corpusSource = Files.readString(corpusPath);
        String earlyReturnSource = Files.readString(earlyReturnPath);
        String nestedBranchSource = Files.readString(nestedBranchPath);
        String selfMethodCallSource = Files.readString(selfMethodCallPath);
        String tryCatchSource = Files.readString(tryCatchPath);
        String whileSource = Files.readString(whilePath);
        String whileMutableLocalSource = Files.readString(whileMutableLocalPath);
        String forBreakContinueSource = Files.readString(forBreakContinuePath);
        String doWhileSource = Files.readString(doWhilePath);
        String forOfSource = Files.readString(forOfPath);
        String switchSource = Files.readString(switchPath);
        String sequenceSource = Files.readString(sequencePath);
        String slimeParserExtendsSource = Files.readString(slimeParserExtendsPath);
        require(corpusSource.contains("QinJvmParsedEarlyReturnMethodBodySmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed early-return method-body smoke");
        require(corpusSource.contains("QinJvmParsedNestedBranchMethodBodySmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed nested-branch method-body smoke");
        require(corpusSource.contains("QinJvmParsedSelfMethodCallSmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed self-method-call smoke");
        require(corpusSource.contains("QinJvmParsedTryCatchMethodBodySmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed try/catch method-body smoke");
        require(corpusSource.contains("QinJvmParsedWhileMethodBodySmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed while method-body smoke");
        require(corpusSource.contains("QinJvmParsedWhileMutableLocalSmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed while mutable-local smoke");
        require(corpusSource.contains("QinJvmParsedForBreakContinueSmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed for/break/continue smoke");
        require(corpusSource.contains("QinJvmParsedDoWhileSmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed do-while smoke");
        require(corpusSource.contains("QinJvmParsedForOfSmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed for...of smoke");
        require(corpusSource.contains("QinJvmParsedSwitchSmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include parsed switch smoke");
        require(corpusSource.contains("QinJvmSequenceConsoleReturnSmokeTestMain.main(args)"),
                "Qin JVM class declaration corpus must include sequence expression bytecode smoke");
        require(corpusSource.contains("24 cases"),
                "Qin JVM class declaration corpus count must cover the current 24-case set");
        require(earlyReturnSource.contains("const prefix = \"hello \""),
                "Parsed early-return smoke must cover Qin local binding in a method body");
        require(earlyReturnSource.contains("if (flag)"),
                "Parsed early-return smoke must cover Qin if branch lowering");
        require(earlyReturnSource.contains("return prefix + name"),
                "Parsed early-return smoke must cover Qin early return from a block");
        require(earlyReturnSource.contains("return \"bye \" + name"),
                "Parsed early-return smoke must cover Qin fallthrough return after early return");
        require(nestedBranchSource.contains("if (active)") && nestedBranchSource.contains("if (premium)"),
                "Parsed nested-branch smoke must cover nested Qin if branch lowering");
        require(nestedBranchSource.contains("const label = \"vip \"")
                        && nestedBranchSource.contains("const standard = \"std \""),
                "Parsed nested-branch smoke must cover branch-local Qin bindings");
        require(nestedBranchSource.contains("return standard + name")
                        && nestedBranchSource.contains("return base + name"),
                "Parsed nested-branch smoke must cover branch and fallthrough returns");
        require(selfMethodCallSource.contains("this.prefix() + name"),
                "Parsed self-method-call smoke must cover same-class method invocation from parsed Qin source");
        require(selfMethodCallSource.contains("getDeclaredMethod(\"label\", String.class)"),
                "Parsed self-method-call smoke must execute the generated JVM method with an argument");
        require(tryCatchSource.contains("throw new RuntimeException(\"boom\")"),
                "Parsed try/catch smoke must cover parsed Qin throw statement lowering");
        require(tryCatchSource.contains("catch (e)") && tryCatchSource.contains("return \"caught\""),
                "Parsed try/catch smoke must cover catch branch JVM execution");
        require(whileSource.contains("while (active)")
                        && whileSource.contains("QinIrWhileStatementNode")
                        && whileSource.contains("return \"loop\"")
                        && whileSource.contains("return \"done\""),
                "Parsed while smoke must cover Qin while statement lowering and both execution paths");
        require(whileMutableLocalSource.contains("let total = 0")
                        && whileMutableLocalSource.contains("while (total < limit)")
                        && whileMutableLocalSource.contains("total = total + 1")
                        && whileMutableLocalSource.contains("QinIrLocalDeclarationStatement")
                        && whileMutableLocalSource.contains("QinIrAssignmentExpression")
                        && whileMutableLocalSource.contains("Double.valueOf(3.0d)")
                        && whileMutableLocalSource.contains("Double.valueOf(0.0d)"),
                "Parsed while mutable-local smoke must cover local declaration, assignment, loop update, and execution");
        require(forBreakContinueSource.contains("for (let i = 0; i < limit; i = i + 1)")
                        && forBreakContinueSource.contains("continue")
                        && forBreakContinueSource.contains("break")
                        && forBreakContinueSource.contains("QinIrForStatement")
                        && forBreakContinueSource.contains("QinIrContinueStatement")
                        && forBreakContinueSource.contains("QinIrBreakStatement")
                        && forBreakContinueSource.contains("Double.valueOf(8.0d)")
                        && forBreakContinueSource.contains("Double.valueOf(1.0d)"),
                "Parsed for/break/continue smoke must cover loop initializer, update, control flow, and execution");
        require(doWhileSource.contains("do {")
                        && doWhileSource.contains("} while (i < limit)")
                        && doWhileSource.contains("continue")
                        && doWhileSource.contains("break")
                        && doWhileSource.contains("QinIrDoWhileStatementNode")
                        && doWhileSource.contains("QinIrContinueStatement")
                        && doWhileSource.contains("QinIrBreakStatement")
                        && doWhileSource.contains("Double.valueOf(8.0d)")
                        && doWhileSource.contains("Double.valueOf(1.0d)"),
                "Parsed do-while smoke must cover loop body-first execution, control flow, and execution");
        require(forOfSource.contains("import { List } from 'java:java.util'")
                        && forOfSource.contains("for (const item of values)")
                        && forOfSource.contains("QinIrForEachStatement")
                        && forOfSource.contains("QinIrContinueStatement")
                        && forOfSource.contains("QinIrBreakStatement")
                        && forOfSource.contains("Double.valueOf(4.0d)"),
                "Parsed for...of smoke must cover java: imported Iterable, control flow, and execution");
        require(switchSource.contains("switch (status)")
                        && switchSource.contains("case \"ready\":")
                        && switchSource.contains("default:")
                        && switchSource.contains("QinIrSwitchStatement")
                        && switchSource.contains("QinIrBreakStatement")
                        && switchSource.contains("\"Ready\".equals(readyLabel)")
                        && switchSource.contains("\"Done\".equals(doneLabel)")
                        && switchSource.contains("\"Other\".equals(otherLabel)")
                        && switchSource.contains("\"Ready\".equals(readyCode)")
                        && switchSource.contains("\"Done\".equals(doneCode)")
                        && switchSource.contains("\"Other\".equals(otherCode)"),
                "Parsed switch smoke must cover case/default lowering, break, and JVM execution");
        require(sequenceSource.contains("QinIrSequenceExpression")
                        && sequenceSource.contains("QinIrBuiltinCallExpression")
                        && sequenceSource.contains("\"console\"")
                        && sequenceSource.contains("\"log\"")
                        && sequenceSource.contains("\"hello\"")
                        && sequenceSource.contains("getDeclaredMethod(\"message\")"),
                "Sequence expression smoke must cover console side effect plus JVM return execution");
        require(slimeParserExtendsSource.contains("classfile inheritance proof, not a production parser entry")
                        && slimeParserExtendsSource.contains("QinParserFacade uses SubhutiParser.create"),
                "Direct SlimeParser construction in the JVM inheritance smoke must stay documented as non-production");
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
        require(audit.contains("24-case class-declaration corpus"),
                "LSP completion audit must record the current JVM class declaration corpus size");
        require(audit.contains("local binding plus early-return `if`"),
                "LSP completion audit must record parsed method-body early-return coverage");
        require(audit.contains("nested `if` branches, branch-local bindings, and fallthrough returns"),
                "LSP completion audit must record parsed method-body nested-branch coverage");
        require(audit.contains("parsed Qin try/catch/throw exception flow"),
                "LSP completion audit must record parsed method-body exception-flow coverage");
        require(audit.contains("parsed Qin `while` statement execution through JVM loop control flow"),
                "LSP completion audit must record parsed while method-body coverage");
        require(audit.contains("parsed Qin mutable loop locals plus assignment execution through JVM while bytecode"),
                "LSP completion audit must record parsed mutable while local/assignment JVM coverage");
        require(audit.contains("parsed Qin `for`/`break`/`continue` execution through JVM loop bytecode"),
                "LSP completion audit must record parsed for/break/continue JVM coverage");
        require(audit.contains("parsed Qin `do while` execution through JVM loop bytecode"),
                "LSP completion audit must record parsed do-while JVM coverage");
        require(audit.contains("parsed Qin `for...of` execution through JVM iterator bytecode"),
                "LSP completion audit must record parsed for...of JVM coverage");
        require(audit.contains("parsed Qin `switch`/`case`/`default` execution through JVM control flow"),
                "LSP completion audit must record parsed switch JVM coverage");
        require(audit.contains("classifies OVS/CSSTS top-level package-only directories as explicit legacy/external exceptions"),
                "LSP completion audit must record OVS/CSSTS package-only inventory classification");
        require(audit.contains("demo apps must be Qin-managed"),
                "LSP completion audit must record demo apps are Qin-managed");
        require(audit.contains("unmanaged package projects cannot appear silently"),
                "LSP completion audit must record unmanaged package project gate");
        require(audit.contains("must not claim `.qin`/`.ovs`/`.cssts` or `@qin/generated-qin-parser-ts`"),
                "LSP completion audit must record package-only exceptions cannot claim the active generated-parser chain");
        require(audit.contains("legacy `language-plugin-testts` identity fallback visibly isolated"),
                "LSP completion audit must record the legacy TestTS identity fallback is isolated from the active path");
        require(audit.contains("identity-fallback behavior") && audit.contains("into the mainline by accident"),
                "LSP completion audit must record identity fallback cannot be promoted into mainline inventory");
        require(audit.contains("Qin object method bodies with local binding plus early-return `if`"),
                "LSP completion audit must record object method-body parser parity coverage");
        require(audit.contains("Qin object method bodies with nested `if` branches and branch-local bindings"),
                "LSP completion audit must record nested object method-body parser parity coverage");
        require(audit.contains("Qin object method bodies with try/catch/throw"),
                "LSP completion audit must record OVS/CSSTS parser-chain exception syntax coverage");
        require(audit.contains("diagnostics, completion, definition, references, document symbols, and semantic tokens"),
                "LSP completion audit must record IDEA fixture references coverage");
        require(audit.contains("parsed same-class method invocation through `this.method()`"),
                "LSP completion audit must record parsed self-method-call JVM coverage");
        require(audit.contains("Qin IR sequence expression bytecode with a `console.log` side effect plus returned value"),
                "LSP completion audit must record sequence expression JVM bytecode coverage");
        require(audit.contains("`lspLanguageCliSmoke` now runs `qin language generate-parser --dry-run`"),
                "LSP completion audit must record CLI generate-parser dry-run coverage");
        require(audit.contains("`com.qin.parser.QinParser` entry"),
                "LSP completion audit must record generated Qin parser entry coverage");
        require(audit.contains("`@qin/generated-qin-parser-ts` package metadata"),
                "LSP completion audit must record generated Qin parser package metadata coverage");
        require(audit.contains("additional Slime CST/AST entry metadata"),
                "LSP completion audit must record additional Slime parser metadata coverage");
        require(audit.contains("Handwritten TS `slime-parser` is deprecated for mainline parser paths"),
                "LSP completion audit must record TS slime-parser deprecation for mainline parser paths");
        require(audit.contains("Remaining `slime-parser` references are treated as legacy tests, migration comparisons, or old demos"),
                "LSP completion audit must classify remaining TS slime-parser references as legacy-only");
        require(audit.contains("direct legacy TS `slime-parser` imports are rejected"),
                "LSP completion audit must record OVS/CSSTS legacy slime-parser removal");
        require(audit.contains("Compiler AST lowering still uses legacy")
                        && audit.contains("`slime-ast` plus `subhuti`"),
                "LSP completion audit must record remaining OVS/CSSTS AST/token legacy dependency");
        require(audit.contains("Move the remaining OVS/CSSTS AST/token construction boundary off legacy TS"),
                "LSP completion audit must keep the remaining AST/token migration hardening step");
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
        require(indexSource.contains("com_subhuti_parser_Alternative as Alternative"),
                "Generated Qin parser index.ts must expose Subhuti Alternative for parser combinators");

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
