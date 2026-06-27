package com.qin.debug.lsp;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspServerDescriptor;
import com.intellij.platform.lsp.api.LspServerSupportProvider;
import com.intellij.platform.lsp.api.LspServerSupportProvider.LspServerStarter;
import com.qin.debug.QinLogger;
import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
import com.qin.types.QinConfig;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public final class QinLspServerSupportProvider implements LspServerSupportProvider {
    @Override
    public void fileOpened(
            @NotNull Project project,
            @NotNull VirtualFile file,
            @NotNull LspServerStarter serverStarter) {
        Path workspaceRoot = resolveWorkspaceRoot(project);
        QinLspLanguage language = QinLspLanguage.fromExtension(workspaceRoot, file.getExtension());
        if (language == null) {
            return;
        }

        serverStarter.ensureServerStarted(new QinLspServerDescriptor(project, language));
    }

    private static final class QinLspServerDescriptor extends LspServerDescriptor {
        private final Project qinProject;
        private final QinLspLanguage language;

        private QinLspServerDescriptor(Project project, QinLspLanguage language) {
            super(project, language.displayName);
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

    private record QinLspLanguage(
            String id,
            String extension,
            String displayName,
            Path projectRelativePath,
            Path serverBundlePath) {
        private static final List<Path> LANGUAGE_PROJECTS = List.of(
                Path.of("qin", "packages", "qin-language"),
                Path.of("ovsjs", "ovs-language"),
                Path.of("cssts", "cssts-language"));

        private static QinLspLanguage fromExtension(Path workspaceRoot, String extension) {
            if (extension == null) {
                return null;
            }
            for (Path projectRelativePath : LANGUAGE_PROJECTS) {
                QinLspLanguage language = load(workspaceRoot, projectRelativePath);
                if (language.matchesExtension(extension)) {
                    return language;
                }
            }
            return null;
        }

        private static QinLspLanguage load(Path workspaceRoot, Path projectRelativePath) {
            Path projectRoot = workspaceRoot.resolve(projectRelativePath).normalize();
            try {
                QinConfig config = new ConfigLoader(projectRoot.toString()).load();
                LanguageConfig language = config.language();
                if (language == null) {
                    throw new IllegalStateException("Missing language metadata in " + projectRoot.resolve("qin.config.js"));
                }
                if (language.id() == null || language.id().isBlank()) {
                    throw new IllegalStateException("Missing language.id in " + projectRoot.resolve("qin.config.js"));
                }
                if (language.extension() == null || language.extension().isBlank()) {
                    throw new IllegalStateException("Missing language.extension in " + projectRoot.resolve("qin.config.js"));
                }
                if (language.server() == null || language.server().isBlank()) {
                    throw new IllegalStateException("Missing language.server in " + projectRoot.resolve("qin.config.js"));
                }
                if (language.serverBundle() == null || language.serverBundle().isBlank()) {
                    throw new IllegalStateException("Missing language.serverBundle in " + projectRoot.resolve("qin.config.js"));
                }
                return new QinLspLanguage(
                        language.id(),
                        normalizeExtension(language.extension()),
                        language.id().toUpperCase(Locale.ROOT),
                        projectRelativePath,
                        Path.of(language.serverBundle()));
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load language metadata from " + projectRoot.resolve("qin.config.js"), e);
            }
        }

        private static String normalizeExtension(String extension) {
            String value = extension.startsWith(".") ? extension.substring(1) : extension;
            return value.toLowerCase(Locale.ROOT);
        }

        private boolean matchesExtension(String candidate) {
            return candidate != null && extension.equals(candidate.toLowerCase(Locale.ROOT));
        }

        private Path resolveServerPath(Path workspaceRoot) {
            Path serverPath = resolveServerBundle(workspaceRoot);
            if (!Files.isRegularFile(serverPath)) {
                throw new IllegalStateException(displayName + " language server bundle not found: " + serverPath);
            }
            return serverPath;
        }

        private Path resolveServerRoot(Path workspaceRoot) {
            return resolveServerPath(workspaceRoot).getParent().getParent();
        }

        private Path resolveServerBundle(Path workspaceRoot) {
            Path projectRoot = workspaceRoot.resolve(projectRelativePath).normalize();
            Path bundlePath = serverBundlePath;
            return bundlePath.isAbsolute()
                    ? bundlePath.normalize()
                    : projectRoot.resolve(bundlePath).normalize();
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
