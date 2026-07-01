package com.qin.debug.lsp;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspServerDescriptor;
import com.intellij.platform.lsp.api.LspServerSupportProvider;
import com.intellij.platform.lsp.api.LspServerSupportProvider.LspServerStarter;
import com.qin.debug.QinLogger;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class QinLspServerSupportProvider implements LspServerSupportProvider {
    @Override
    public void fileOpened(
            @NotNull Project project,
            @NotNull VirtualFile file,
            @NotNull LspServerStarter serverStarter) {
        Path workspaceRoot = resolveWorkspaceRoot(project);
        QinLogger.ensureInitialized(project, workspaceRoot.toString());
        QinLogger.info("[LSP] fileOpened path=" + file.getPath()
                + " extension=" + file.getExtension()
                + " workspaceRoot=" + workspaceRoot);
        QinLspLanguage language = QinLspLanguageRegistry.fromExtension(workspaceRoot, file.getExtension());
        if (language == null) {
            QinLogger.info("[LSP] No registered language for extension=" + file.getExtension());
            return;
        }

        QinLogger.info("[LSP] Matched language id=" + language.id()
                + " displayName=" + language.displayName()
                + " serverBundle=" + language.serverBundlePath());
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
            boolean supported = language.matchesExtension(file.getExtension());
            QinLogger.debug("[LSP] isSupportedFile path=" + file.getPath()
                    + " extension=" + file.getExtension()
                    + " language=" + language.id()
                    + " supported=" + supported);
            return supported;
        }

        @Override
        public @NotNull GeneralCommandLine createCommandLine() {
            Path workspaceRoot = resolveWorkspaceRoot(qinProject);
            Path serverPath = language.resolveServerPath(workspaceRoot);
            QinLogger.info("[LSP] Starting " + language.displayName() + " server: " + serverPath);
            return QinLspServerCommandLineFactory.create(workspaceRoot, language);
        }
    }

    private static Path resolveWorkspaceRoot(Project project) {
        if (project.getBasePath() == null || project.getBasePath().isBlank()) {
            throw new IllegalStateException("Cannot resolve workspace root for Qin LSP");
        }

        return QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of(project.getBasePath()));
    }

}
