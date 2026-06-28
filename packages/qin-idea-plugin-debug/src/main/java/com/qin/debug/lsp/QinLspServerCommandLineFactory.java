package com.qin.debug.lsp;

import com.intellij.execution.configurations.GeneralCommandLine;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

final class QinLspServerCommandLineFactory {
    private QinLspServerCommandLineFactory() {
    }

    static QinLspServerCommandSpec createSpec(Path workspaceRoot, QinLspLanguage language) {
        Path serverPath = language.resolveServerPath(workspaceRoot);
        Path tsdkPath = QinLspLanguageRegistry.resolveTypescriptSdk(workspaceRoot);
        String node = QinLspLanguageRegistry.resolveNodeExecutable();

        return new QinLspServerCommandSpec(
                node,
                List.of(serverPath.toString(), "--stdio"),
                language.resolveServerRoot(workspaceRoot),
                StandardCharsets.UTF_8,
                Map.of("QIN_LSP_TYPESCRIPT_TSDK", tsdkPath.toString()));
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
}
