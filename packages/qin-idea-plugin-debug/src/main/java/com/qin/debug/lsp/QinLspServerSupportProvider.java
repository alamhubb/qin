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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class QinLspServerSupportProvider implements LspServerSupportProvider {
    @Override
    public void fileOpened(
            @NotNull Project project,
            @NotNull VirtualFile file,
            @NotNull LspServerStarter serverStarter) {
        QinLspLanguage language = QinLspLanguage.fromExtension(file.getExtension());
        if (language == null) {
            return;
        }

        serverStarter.ensureServerStarted(new QinLspServerDescriptor(project, language));
    }

    private static final class QinLspServerDescriptor extends LspServerDescriptor {
        private final QinLspLanguage language;

        private QinLspServerDescriptor(Project project, QinLspLanguage language) {
            super(project, language.displayName);
            this.language = language;
        }

        @Override
        public boolean isSupportedFile(@NotNull VirtualFile file) {
            return language == QinLspLanguage.fromExtension(file.getExtension());
        }

        @Override
        public @NotNull GeneralCommandLine createCommandLine() {
            Path workspaceRoot = resolveWorkspaceRoot(project);
            Path serverPath = language.resolveServerPath(workspaceRoot);
            Path tsdkPath = resolveTypescriptSdk(workspaceRoot);
            String node = resolveNodeExecutable();

            QinLogger.info("[LSP] Starting " + language.displayName + " server: " + serverPath);

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

    private enum QinLspLanguage {
        QIN("qin", "Qin", Path.of("qin", "packages", "qin-language", "dist", "language-server.cjs")),
        OVS("ovs", "OVS", Path.of("ovsjs", "ovs-language", "dist", "language-server.cjs")),
        CSSTS("cssts", "CSSTS", Path.of("cssts", "cssts-language", "dist", "language-server.cjs"));

        private final String extension;
        private final String displayName;
        private final Path serverRelativePath;

        QinLspLanguage(String extension, String displayName, Path serverRelativePath) {
            this.extension = extension;
            this.displayName = displayName;
            this.serverRelativePath = serverRelativePath;
        }

        private static QinLspLanguage fromExtension(String extension) {
            if (extension == null) {
                return null;
            }
            String normalized = extension.toLowerCase(Locale.ROOT);
            for (QinLspLanguage language : values()) {
                if (language.extension.equals(normalized)) {
                    return language;
                }
            }
            return null;
        }

        private Path resolveServerPath(Path workspaceRoot) {
            Path serverPath = workspaceRoot.resolve(serverRelativePath).normalize();
            if (!Files.isRegularFile(serverPath)) {
                throw new IllegalStateException(displayName + " language server bundle not found: " + serverPath);
            }
            return serverPath;
        }

        private Path resolveServerRoot(Path workspaceRoot) {
            return resolveServerPath(workspaceRoot).getParent().getParent();
        }
    }

    private static Path resolveWorkspaceRoot(Project project) {
        if (project.getBasePath() == null || project.getBasePath().isBlank()) {
            throw new IllegalStateException("Cannot resolve workspace root for Qin LSP");
        }

        Path current = Path.of(project.getBasePath()).toAbsolutePath().normalize();
        while (current != null) {
            if (Files.isRegularFile(current.resolve("qin")
                    .resolve("packages")
                    .resolve("qin-language")
                    .resolve("dist")
                    .resolve("language-server.cjs"))) {
                return current;
            }
            current = current.getParent();
        }

        throw new IllegalStateException("Cannot find qinall workspace root from " + project.getBasePath());
    }

    private static Path resolveTypescriptSdk(Path workspaceRoot) {
        Path[] candidates = {
                workspaceRoot.resolve("qin").resolve("packages").resolve("qin-language").resolve("node_modules")
                        .resolve("typescript").resolve("lib"),
                workspaceRoot.resolve("ovsjs").resolve("node_modules").resolve("typescript").resolve("lib"),
                workspaceRoot.resolve("cssts").resolve("node_modules").resolve("typescript").resolve("lib"),
                workspaceRoot.resolve("node_modules").resolve("typescript").resolve("lib")
        };

        for (Path candidate : candidates) {
            if (Files.isRegularFile(candidate.resolve("typescript.js"))
                    || Files.isRegularFile(candidate.resolve("tsserverlibrary.js"))) {
                return candidate.normalize();
            }
        }

        throw new IllegalStateException("TypeScript SDK not found under " + workspaceRoot);
    }

    private static String resolveNodeExecutable() {
        String configured = System.getenv("QIN_LSP_NODE");
        if (configured != null && !configured.isBlank()) {
            return configured;
        }

        return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win")
                ? "node.exe"
                : "node";
    }
}
