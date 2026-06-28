package com.qin.debug.lsp;

import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
import com.qin.types.QinConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class QinLspVerificationMatrixSmokeTestMain {
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

        List<MatrixCase> cases = List.of(
                new MatrixCase("qin", ".qin", Path.of("qin", "packages", "qin-language"),
                        "dist/language-server.cjs", "tsx tests/test-language-plugin.ts",
                        "tsx tests/test-language-server.ts"),
                new MatrixCase("ovs", ".ovs", Path.of("ovsjs", "ovs-language"),
                        "dist/language-server.js", "tsx tests/test-generated-parser-chain.ts",
                        "tsx tests/test-language-server.ts --source",
                        "tsx tests/test-language-server.ts --dist"),
                new MatrixCase("cssts", ".cssts", Path.of("cssts", "cssts-language"),
                        "dist/language-server.cjs", "tsx tests/test-generated-parser-chain.ts",
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
        }

        Path buildFile = ideaClientPath.resolve("build.gradle.kts");
        String buildSource = Files.readString(buildFile);
        for (String smokeTask : List.of(
                "lspRegistrySmoke",
                "lspServerCommandLineSmoke",
                "lspServerDiagnosticsSmoke",
                "lspVerificationMatrixSmoke")) {
            require(buildSource.contains("dependsOn(\"" + smokeTask + "\")"),
                    "Gradle check must depend on " + smokeTask);
        }

        System.out.println("Qin LSP verification matrix smoke passed");
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
            String serverBundle,
            String... requiredTestScriptParts) {
    }
}
