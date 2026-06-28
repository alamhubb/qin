package com.qin.debug.lsp;

import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
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
                        "tsx tests/test-language-plugin.ts",
                        "tsx tests/test-language-server.ts"),
                new MatrixCase("ovs", ".ovs", Path.of("ovsjs", "ovs-language"),
                        Path.of("ovsjs", "ovs", "ovs-compiler"),
                        "dist/language-server.js", "@qin/generated-qin-parser-ts", "../ovs/ovs-compiler",
                        "tsx tests/test-generated-parser-chain.ts",
                        "tsx tests/test-language-server.ts --source",
                        "tsx tests/test-language-server.ts --dist"),
                new MatrixCase("cssts", ".cssts", Path.of("cssts", "cssts-language"),
                        Path.of("cssts", "cssts", "cssts-compiler"),
                        "dist/language-server.cjs", "@qin/generated-qin-parser-ts", "../cssts/cssts-compiler",
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

            if ("qin".equals(matrixCase.id())) {
                verifyGeneratedQinParserPackage(projectRoot, config);
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
        require(buildSource.contains("register(\"languageProjectsTest\")"),
                "Gradle must declare languageProjectsTest");
        require(buildSource.contains("register<Exec>(\"qinJvmClassTargetSmoke\")"),
                "Gradle must declare qinJvmClassTargetSmoke");
        require(buildSource.contains("qin/packages/qin-lang-cli"),
                "qinJvmClassTargetSmoke must run from qin/packages/qin-lang-cli");
        require(buildSource.contains("\"run\", \"com.qin.lang.cli.SmokeTestMain\""),
                "qinJvmClassTargetSmoke must run the Qin CLI JVM .class smoke");
        for (String smokeTask : List.of(
                "languageProjectsTest",
                "qinJvmClassTargetSmoke",
                "qinGeneratedParserDryRun",
                "lspRegistrySmoke",
                "lspServerCommandLineSmoke",
                "lspServerDiagnosticsSmoke",
                "lspVerificationMatrixSmoke")) {
            require(buildSource.contains("dependsOn(\"" + smokeTask + "\")"),
                    "Gradle check must depend on " + smokeTask);
        }

        System.out.println("Qin LSP verification matrix smoke passed");
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
        require(!testScript.contains("npm run"),
                matrixCase.id() + " compiler scripts.test must run checks directly through Qin scripts");
        verifyGeneratedParserDependency(
                matrixCase.id() + " compiler",
                compilerRoot,
                generatedQinParserRoot,
                compilerConfig);
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
        require(languageConfigSource.contains("generatedParserTarget: \"@qin/generated-qin-parser-ts\""),
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

    private record MatrixCase(
            String id,
            String extension,
            Path projectRelativePath,
            Path compilerProjectRelativePath,
            String serverBundle,
            String expectedParser,
            String expectedCompiler,
            String... requiredTestScriptParts) {
    }
}
