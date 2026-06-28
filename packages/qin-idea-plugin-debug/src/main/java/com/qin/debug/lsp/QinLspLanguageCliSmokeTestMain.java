package com.qin.debug.lsp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

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
                        "qin",
                        workspaceRoot.resolve("qin").resolve("packages").resolve("qin-language").normalize(),
                        "dist/language-server.cjs",
                        true,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tests/test-generated-parser-parity.ts",
                                "dev", "qin-language-server/src/index.ts --stdio")),
                new LanguageCliCase(
                        "ovs",
                        "ovs",
                        workspaceRoot.resolve("ovsjs").resolve("ovs-language").normalize(),
                        "dist/language-server.js",
                        true,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tests/test-generated-parser-chain.ts",
                                "dev", "ovs-language-server/src/index.ts --stdio")),
                new LanguageCliCase(
                        "cssts",
                        "cssts",
                        workspaceRoot.resolve("cssts").resolve("cssts-language").normalize(),
                        "dist/language-server.cjs",
                        true,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tests/test-generated-parser-chain.ts",
                                "dev", "cssts-language-server/src/index.ts --stdio")),
                new LanguageCliCase(
                        "ovsjs-workspace",
                        "ovs",
                        workspaceRoot.resolve("ovsjs").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "language build --root ovs/ovs-runtime",
                                "build", "language build --root ovs/ovs-compiler",
                                "build", "language build --root create-ovs",
                                "build", "language build --root vite-plugin-ovs",
                                "build", "language build --root ovs-language",
                                "test", "language test --root ovs/ovs-runtime",
                                "test", "language test --root ovs/ovs-compiler",
                                "test", "language test --root create-ovs",
                                "test", "language test --root vite-plugin-ovs",
                                "test", "language test --root ovs-language")),
                new LanguageCliCase(
                        "cssts-workspace",
                        "cssts",
                        workspaceRoot.resolve("cssts").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "language build --root cssts/cssts-runtime",
                                "build", "language build --root cssts/cssts-compiler",
                                "build", "language build --root vite-plugin-cssts",
                                "build", "language build --root language-plugin-cssts",
                                "build", "language build --root cssts-language",
                                "build", "language build --root create-cssts",
                                "build", "language build --root cssts-theme-element",
                                "test", "language test --root cssts/cssts-runtime",
                                "test", "language test --root cssts/cssts-compiler",
                                "test", "language test --root vite-plugin-cssts",
                                "test", "language test --root language-plugin-cssts",
                                "test", "language test --root cssts-language",
                                "test", "language test --root create-cssts",
                                "test", "language test --root cssts-theme-element")),
                new LanguageCliCase(
                        "ovs-compiler",
                        "ovs",
                        workspaceRoot.resolve("ovsjs").resolve("ovs").resolve("ovs-compiler").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tests/test-generated-parser-chain.ts")),
                new LanguageCliCase(
                        "cssts-compiler",
                        "cssts",
                        workspaceRoot.resolve("cssts").resolve("cssts").resolve("cssts-compiler").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tests/test-generated-parser-chain.ts")),
                new LanguageCliCase(
                        "ovs-runtime",
                        "ovs",
                        workspaceRoot.resolve("ovsjs").resolve("ovs").resolve("ovs-runtime").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tsdown")),
                new LanguageCliCase(
                        "vite-plugin-ovs",
                        "ovs",
                        workspaceRoot.resolve("ovsjs").resolve("vite-plugin-ovs").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tsdown")),
                new LanguageCliCase(
                        "create-ovs",
                        "ovs",
                        workspaceRoot.resolve("ovsjs").resolve("create-ovs").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tsdown")),
                new LanguageCliCase(
                        "cssts-runtime",
                        "cssts",
                        workspaceRoot.resolve("cssts").resolve("cssts").resolve("cssts-runtime").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "vitest run")),
                new LanguageCliCase(
                        "vite-plugin-cssts",
                        "cssts",
                        workspaceRoot.resolve("cssts").resolve("vite-plugin-cssts").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tsdown")),
                new LanguageCliCase(
                        "language-plugin-cssts",
                        "cssts",
                        workspaceRoot.resolve("cssts").resolve("language-plugin-cssts").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "node test-transform-error.cjs")),
                new LanguageCliCase(
                        "create-cssts",
                        "cssts",
                        workspaceRoot.resolve("cssts").resolve("create-cssts").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tsdown")),
                new LanguageCliCase(
                        "cssts-theme-element",
                        "cssts",
                        workspaceRoot.resolve("cssts").resolve("cssts-theme-element").normalize(),
                        null,
                        false,
                        scriptNeedles(
                                "build", "tsdown",
                                "test", "tsdown")));

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
        require(check.stdout().contains("id: " + testCase.expectedLanguageId()),
                testCase.id() + " qin language check did not print expected language id: " + check);

        if (testCase.expectServerCommands()) {
            CommandResult bundle = runQinLanguage(qinCommand, testCase.projectRoot(), "bundle");
            Path expectedBundle = testCase.projectRoot().resolve(testCase.serverBundle()).normalize();
            require(bundle.stdout().trim().equals(expectedBundle.toString()),
                    testCase.id() + " qin language bundle mismatch: " + bundle.stdout());

            CommandResult server = runQinLanguage(qinCommand, testCase.projectRoot(), "server", "--dry-run");
            require(server.stdout().contains(expectedBundle.toString()),
                    testCase.id() + " qin language server --dry-run did not use language.serverBundle: " + server);
            require(server.stdout().contains("--stdio"),
                    testCase.id() + " qin language server --dry-run must pass --stdio: " + server);
        }

        for (ScriptNeedle expectedScript : testCase.expectedScriptNeedles()) {
            CommandResult script = runQinLanguage(
                    qinCommand,
                    testCase.projectRoot(),
                    expectedScript.command(),
                    "--dry-run");
            require(script.stdout().contains(expectedScript.needle()),
                    testCase.id() + " qin language " + expectedScript.command()
                            + " --dry-run did not resolve scripts." + expectedScript.command()
                            + " from qin.config.js with needle " + expectedScript.needle() + ": " + script);
        }
    }

    private static List<ScriptNeedle> scriptNeedles(String... commandAndNeedlePairs) {
        require(commandAndNeedlePairs.length % 2 == 0,
                "Script needle pairs must be provided as command/needle tuples");
        List<ScriptNeedle> needles = new java.util.ArrayList<>();
        for (int index = 0; index < commandAndNeedlePairs.length; index += 2) {
            needles.add(new ScriptNeedle(commandAndNeedlePairs[index], commandAndNeedlePairs[index + 1]));
        }
        return List.copyOf(needles);
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
            String expectedLanguageId,
            Path projectRoot,
            String serverBundle,
            boolean expectServerCommands,
            List<ScriptNeedle> expectedScriptNeedles) {
    }

    private record ScriptNeedle(String command, String needle) {
    }

    private record CommandResult(int exitCode, String stdout, String stderr) {
    }
}
