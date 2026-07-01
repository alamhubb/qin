package com.qin.debug.lsp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class QinLspServerCommandLineSmokeTestMain {
    private QinLspServerCommandLineSmokeTestMain() {
    }

    public static void main(String[] args) {
        Path workspaceRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of("."));
        String languageFilter = args.length > 1 ? args[1] : null;
        Path tsdkPath = QinLspLanguageRegistry.resolveTypescriptSdk(workspaceRoot);
        String node = QinLspLanguageRegistry.resolveNodeExecutable(workspaceRoot);

        Map<String, String> expectedBundleNames = Map.of(
                "qin", "language-server.cjs",
                "ovs", "language-server.js",
                "cssts", "language-server.cjs");

        boolean checkedAnyLanguage = false;
        for (Map.Entry<String, String> expected : expectedBundleNames.entrySet()) {
            if (!matchesLanguageFilter(expected.getKey(), languageFilter)) {
                continue;
            }
            checkedAnyLanguage = true;
            QinLspLanguage language = QinLspLanguageRegistry.fromExtension(workspaceRoot, expected.getKey());
            require(language != null, "Missing language for ." + expected.getKey());

            QinLspServerCommandSpec commandSpec = QinLspServerCommandLineFactory.createSpec(workspaceRoot, language);
            List<String> arguments = commandSpec.arguments();
            require(arguments.size() == 2, language.id() + " arguments must have server and --stdio: " + arguments);
            require(node.equals(commandSpec.executable()), language.id() + " command must use resolved Node executable");
            require("--stdio".equals(arguments.get(1)), language.id() + " command must pass --stdio");

            Path serverPath = Path.of(arguments.get(0)).toAbsolutePath().normalize();
            require(serverPath.endsWith(expected.getValue()), language.id() + " unexpected server bundle: " + serverPath);
            require(serverPath.equals(language.resolveServerPath(workspaceRoot)),
                    language.id() + " command server path differs from registry metadata");

            Path workDir = commandSpec.workDirectory().toAbsolutePath().normalize();
            require(workDir.equals(language.resolveServerRoot(workspaceRoot)),
                    language.id() + " command work directory differs from server root");

            require(StandardCharsets.UTF_8.equals(commandSpec.charset()), language.id() + " command must use UTF-8");
            require(tsdkPath.toString().equals(commandSpec.environment().get("QIN_LSP_TYPESCRIPT_TSDK")),
                    language.id() + " command must pass TypeScript SDK environment");
            require(("." + language.extension()).equals(commandSpec.environment().get("QIN_LSP_SOURCE_EXTENSION")),
                    language.id() + " command must pass languageServer.sourceExtension environment");
            require(language.serviceExtension().equals(commandSpec.environment().get("QIN_LSP_SERVICE_EXTENSION")),
                    language.id() + " command must pass languageServer.serviceExtension environment");
            require(language.generatedParserTarget()
                    .equals(commandSpec.environment().get("QIN_LSP_GENERATED_PARSER_TARGET")),
                    language.id() + " command must pass languageServer.generatedParserTarget environment");
            if (language.parserPackage() != null && !language.parserPackage().isBlank()) {
                require(language.parserPackage().equals(commandSpec.environment().get("QIN_LSP_PARSER_PACKAGE")),
                        language.id() + " command must pass languageServer.parserPackage environment");
            }
            if (language.compilerPackage() != null && !language.compilerPackage().isBlank()) {
                require(language.compilerPackage().equals(commandSpec.environment().get("QIN_LSP_COMPILER_PACKAGE")),
                        language.id() + " command must pass languageServer.compilerPackage environment");
            }
        }

        require(checkedAnyLanguage, "No LSP language matched filter: " + languageFilter);
        System.out.println("Qin IDEA LSP server command line smoke passed");
    }

    private static boolean matchesLanguageFilter(String extension, String languageFilter) {
        return languageFilter == null
                || languageFilter.isBlank()
                || extension.equals(normalizedExtension(languageFilter));
    }

    private static String normalizedExtension(String extension) {
        return extension.startsWith(".") ? extension.substring(1) : extension;
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
