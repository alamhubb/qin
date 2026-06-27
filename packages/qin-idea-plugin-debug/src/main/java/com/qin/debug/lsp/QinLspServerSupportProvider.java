package com.qin.debug.lsp;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspServerDescriptor;
import com.intellij.platform.lsp.api.LspServerSupportProvider;
import com.intellij.platform.lsp.api.LspServerSupportProvider.LspServerStarter;
import com.qin.debug.QinLogger;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class QinLspServerSupportProvider implements LspServerSupportProvider {
    @Override
    public void fileOpened(
            @NotNull Project project,
            @NotNull VirtualFile file,
            @NotNull LspServerStarter serverStarter) {
        Path workspaceRoot = resolveWorkspaceRoot(project);
        QinLspLanguage language = QinLspLanguageRegistry.fromExtension(workspaceRoot, file.getExtension());
        if (language == null) {
            return;
        }

        serverStarter.ensureServerStarted(new QinLspServerDescriptor(project, language));
    }

    private static final class QinLspServerDescriptor extends LspServerDescriptor {
        private final Project qinProject;
        private final QinLspLanguage language;

        private QinLspServerDescriptor(Project project, QinLspLanguage language) {
            super(project, language.displayName());
            this.qinProject = project;
            this.language = language;
        }

        @Override
        public boolean isSupportedFile(@NotNull VirtualFile file) {
            return language.matchesExtension(file.getExtension());
        }

        @Override
        public @NotNull GeneralCommandLine createCommandLine() {
            Path workspaceRoot = resolveWorkspaceRoot(qinProject);
            Path serverPath = language.resolveServerPath(workspaceRoot);
            Path tsdkPath = QinLspLanguageRegistry.resolveTypescriptSdk(workspaceRoot);
            String node = QinLspLanguageRegistry.resolveNodeExecutable();

            QinLogger.info("[LSP] Starting " + language.displayName() + " server: " + serverPath);

            GeneralCommandLine commandLine = new GeneralCommandLine(
                    node,
                    serverPath.toString(),
                    "--stdio");
            commandLine.setWorkDirectory(language.resolveServerRoot(workspaceRoot).toFile());
            commandLine.setCharset(StandardCharsets.UTF_8);
            commandLine.withEnvironment("QIN_LSP_TYPESCRIPT_TSDK", tsdkPath.toString());
            return commandLine;
        }
    }

    private static Path resolveWorkspaceRoot(Project project) {
        if (project.getBasePath() == null || project.getBasePath().isBlank()) {
            throw new IllegalStateException("Cannot resolve workspace root for Qin LSP");
        }

        return QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of(project.getBasePath()));
    }

}
