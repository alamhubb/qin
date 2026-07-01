package com.qin.debug.lsp;

import com.intellij.execution.configurations.GeneralCommandLine;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class QinLspServerCommandLineFactory {
    private QinLspServerCommandLineFactory() {
    }

    static QinLspServerCommandSpec createSpec(Path workspaceRoot, QinLspLanguage language) {
        Path serverPath = language.resolveServerPath(workspaceRoot);
        Path tsdkPath = QinLspLanguageRegistry.resolveTypescriptSdk(workspaceRoot);
        String node = QinLspLanguageRegistry.resolveNodeExecutable(workspaceRoot);
        Map<String, String> environment = new LinkedHashMap<>();
        environment.put("QIN_LSP_TYPESCRIPT_TSDK", tsdkPath.toString());
        environment.put("QIN_LSP_SOURCE_EXTENSION", "." + language.extension());
        environment.put("QIN_LSP_SERVICE_EXTENSION", language.serviceExtension());
        environment.put("QIN_LSP_GENERATED_PARSER_TARGET", language.generatedParserTarget());
        environment.put("NODE_OPTIONS", mergeNodeOptions(System.getenv("NODE_OPTIONS"), "--max-old-space-size=256"));
        putIfPresent(environment, "QIN_LSP_PARSER_PACKAGE", language.parserPackage());
        putIfPresent(environment, "QIN_LSP_COMPILER_PACKAGE", language.compilerPackage());

        return new QinLspServerCommandSpec(
                node,
                List.of(serverPath.toString(), "--stdio"),
                language.resolveServerRoot(workspaceRoot),
                StandardCharsets.UTF_8,
                Map.copyOf(environment));
    }

    static GeneralCommandLine create(Path workspaceRoot, QinLspLanguage language) {
        QinLspServerCommandSpec spec = createSpec(workspaceRoot, language);
        GeneralCommandLine commandLine = new GeneralCommandLine(spec.executable());
        commandLine.addParameters(spec.arguments());
        commandLine.setWorkDirectory(spec.workDirectory().toFile());
        commandLine.setCharset(spec.charset());
        for (Map.Entry<String, String> entry : spec.environment().entrySet()) {
            commandLine.withEnvironment(entry.getKey(), entry.getValue());
        }
        return commandLine;
    }

    private static void putIfPresent(Map<String, String> environment, String key, String value) {
        if (value != null && !value.isBlank()) {
            environment.put(key, value);
        }
    }

    private static String mergeNodeOptions(String currentValue, String requiredOption) {
        if (currentValue == null || currentValue.isBlank()) {
            return requiredOption;
        }
        if (currentValue.contains(requiredOption)) {
            return currentValue;
        }
        return currentValue + " " + requiredOption;
    }

    static String resolveNodeExecutable(Path workspaceRoot) {
        return QinLspLanguageRegistry.resolveNodeExecutable(workspaceRoot);
    }
}
