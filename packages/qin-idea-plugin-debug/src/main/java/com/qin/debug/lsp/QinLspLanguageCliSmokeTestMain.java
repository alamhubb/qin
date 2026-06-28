package com.qin.debug.lsp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class QinLspLanguageCliSmokeTestMain {
    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    private QinLspLanguageCliSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path workspaceRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of("."));
        Path qinCommand = workspaceRoot.resolve("qin")
                .resolve(isWindows() ? "qin.bat" : "qin")
                .normalize();
        require(Files.isRegularFile(qinCommand), "Qin command not found: " + qinCommand);

        List<LanguageCliCase> cases = List.of(
                new LanguageCliCase(
                        "qin",
                        workspaceRoot.resolve("qin").resolve("packages").resolve("qin-language").normalize(),
                        "dist/language-server.cjs",
                        Map.of(
                                "build", "tsdown",
                                "test", "tests/test-generated-parser-parity.ts",
                                "dev", "qin-language-server/src/index.ts --stdio")),
                new LanguageCliCase(
                        "ovs",
                        workspaceRoot.resolve("ovsjs").resolve("ovs-language").normalize(),
                        "dist/language-server.js",
                        Map.of(
                                "build", "tsdown",
                                "test", "tests/test-generated-parser-chain.ts",
                                "dev", "ovs-language-server/src/index.ts --stdio")),
                new LanguageCliCase(
                        "cssts",
                        workspaceRoot.resolve("cssts").resolve("cssts-language").normalize(),
                        "dist/language-server.cjs",
                        Map.of(
                                "build", "tsdown",
                                "test", "tests/test-generated-parser-chain.ts",
                                "dev", "cssts-language-server/src/index.ts --stdio")));

        for (LanguageCliCase testCase : cases) {
            verifyLanguageCli(qinCommand, testCase);
        }

        System.out.println("Qin language CLI smoke passed");
    }

    private static void verifyLanguageCli(Path qinCommand, LanguageCliCase testCase) throws Exception {
        require(Files.isDirectory(testCase.projectRoot()),
                testCase.id() + " project root not found: " + testCase.projectRoot());
        require(Files.isRegularFile(testCase.projectRoot().resolve("qin.config.js")),
                testCase.id() + " must be managed by qin.config.js");

        CommandResult check = runQinLanguage(qinCommand, testCase.projectRoot(), "check");
        require(check.stdout().contains("[OK] Language metadata is valid"),
                testCase.id() + " qin language check did not validate metadata: " + check);
        require(check.stdout().contains("id: " + testCase.id()),
                testCase.id() + " qin language check did not print expected language id: " + check);

        CommandResult bundle = runQinLanguage(qinCommand, testCase.projectRoot(), "bundle");
        Path expectedBundle = testCase.projectRoot().resolve(testCase.serverBundle()).normalize();
        require(bundle.stdout().trim().equals(expectedBundle.toString()),
                testCase.id() + " qin language bundle mismatch: " + bundle.stdout());

        CommandResult server = runQinLanguage(qinCommand, testCase.projectRoot(), "server", "--dry-run");
        require(server.stdout().contains(expectedBundle.toString()),
                testCase.id() + " qin language server --dry-run did not use language.serverBundle: " + server);
        require(server.stdout().contains("--stdio"),
                testCase.id() + " qin language server --dry-run must pass --stdio: " + server);

        for (Map.Entry<String, String> expectedScript : testCase.expectedScriptNeedles().entrySet()) {
            CommandResult script = runQinLanguage(
                    qinCommand,
                    testCase.projectRoot(),
                    expectedScript.getKey(),
                    "--dry-run");
            require(script.stdout().contains(expectedScript.getValue()),
                    testCase.id() + " qin language " + expectedScript.getKey()
                            + " --dry-run did not resolve scripts." + expectedScript.getKey()
                            + " from qin.config.js: " + script);
        }
    }

    private static CommandResult runQinLanguage(
            Path qinCommand,
            Path workingDirectory,
            String command,
            String... extraArgs) throws Exception {
        List<String> args = new java.util.ArrayList<>();
        if (isWindows()) {
            args.add("cmd.exe");
            args.add("/c");
            args.add(qinCommand.toString());
        } else {
            args.add(qinCommand.toString());
        }
        args.add("language");
        args.add(command);
        args.addAll(List.of(extraArgs));

        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.directory(workingDirectory.toFile());
        processBuilder.redirectErrorStream(false);
        Process process = processBuilder.start();
        boolean finished = process.waitFor(TIMEOUT.toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
        if (!finished) {
            process.destroyForcibly();
            throw new IllegalStateException("Timed out running " + String.join(" ", args));
        }
        String stdout = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String stderr = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
        CommandResult result = new CommandResult(process.exitValue(), stdout, stderr);
        require(result.exitCode() == 0,
                "Command failed: " + String.join(" ", args) + System.lineSeparator() + result);
        return result;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private record LanguageCliCase(
            String id,
            Path projectRoot,
            String serverBundle,
            Map<String, String> expectedScriptNeedles) {
    }

    private record CommandResult(int exitCode, String stdout, String stderr) {
    }
}
