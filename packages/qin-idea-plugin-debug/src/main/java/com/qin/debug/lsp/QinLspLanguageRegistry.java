package com.qin.debug.lsp;

import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
import com.qin.types.LanguageServerConfig;
import com.qin.types.QinConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

final class QinLspLanguageRegistry {
    static final List<Path> LANGUAGE_PROJECTS = List.of(
            Path.of("qin", "packages", "qin-language"),
            Path.of("ovsjs", "ovs-language"),
            Path.of("cssts", "cssts-language"));

    private QinLspLanguageRegistry() {
    }

    static QinLspLanguage fromExtension(Path workspaceRoot, String extension) {
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

    static QinLspLanguage load(Path workspaceRoot, Path projectRelativePath) {
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
            LanguageServerConfig languageServer = config.languageServer();
            if (languageServer == null) {
                throw new IllegalStateException("Missing languageServer metadata in " + projectRoot.resolve("qin.config.js"));
            }
            if (languageServer.sourceExtension() == null || languageServer.sourceExtension().isBlank()) {
                throw new IllegalStateException(
                        "Missing languageServer.sourceExtension in " + projectRoot.resolve("qin.config.js"));
            }
            if (languageServer.serviceExtension() == null || languageServer.serviceExtension().isBlank()) {
                throw new IllegalStateException(
                        "Missing languageServer.serviceExtension in " + projectRoot.resolve("qin.config.js"));
            }
            if (languageServer.generatedParserTarget() == null || languageServer.generatedParserTarget().isBlank()) {
                throw new IllegalStateException(
                        "Missing languageServer.generatedParserTarget in " + projectRoot.resolve("qin.config.js"));
            }
            String languageExtension = normalizeExtension(language.extension());
            String serverSourceExtension = normalizeExtension(languageServer.sourceExtension());
            if (!languageExtension.equals(serverSourceExtension)) {
                throw new IllegalStateException(
                        "language.extension and languageServer.sourceExtension must match in "
                                + projectRoot.resolve("qin.config.js"));
            }
            return new QinLspLanguage(
                    language.id(),
                    serverSourceExtension,
                    languageServer.serviceExtension(),
                    languageServer.generatedParserTarget(),
                    languageServer.parserPackage(),
                    languageServer.compilerPackage(),
                    language.id().toUpperCase(Locale.ROOT),
                    projectRelativePath,
                    Path.of(language.serverBundle()));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load language metadata from " + projectRoot.resolve("qin.config.js"), e);
        }
    }

    static Path resolveWorkspaceRoot(Path projectBasePath) {
        Path current = projectBasePath.toAbsolutePath().normalize();
        while (current != null) {
            if (isWorkspaceRoot(current)) {
                return current;
            }
            current = current.getParent();
        }

        throw new IllegalStateException("Cannot find qinall workspace root from " + projectBasePath);
    }

    private static boolean isWorkspaceRoot(Path candidate) {
        for (Path projectRelativePath : LANGUAGE_PROJECTS) {
            if (!Files.isRegularFile(candidate.resolve(projectRelativePath).resolve("qin.config.js"))) {
                return false;
            }
        }
        return true;
    }

    static Path resolveTypescriptSdk(Path workspaceRoot) {
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

    static String resolveNodeExecutable() {
        return resolveNodeExecutable(resolveWorkspaceRoot(Path.of(".")));
    }

    static String resolveNodeExecutable(Path workspaceRoot) {
        String configured = System.getenv("QIN_LSP_NODE");
        if (configured != null && !configured.isBlank()) {
            return configured;
        }

        boolean windows = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
        String commandName = windows ? "node.exe" : "node";
        Path pathNode = findExecutableOnPath(commandName);
        if (pathNode != null) {
            return pathNode.toString();
        }

        for (Path candidate : windowsNodeCandidates(workspaceRoot)) {
            if (Files.isRegularFile(candidate)) {
                return candidate.toString();
            }
        }

        return commandName;
    }

    private static Path findExecutableOnPath(String executableName) {
        String path = System.getenv("PATH");
        if (path == null || path.isBlank()) {
            return null;
        }
        for (String entry : path.split(java.io.File.pathSeparator)) {
            if (entry == null || entry.isBlank()) {
                continue;
            }
            Path candidate = Path.of(entry).resolve(executableName).toAbsolutePath().normalize();
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private static List<Path> windowsNodeCandidates(Path workspaceRoot) {
        String userProfile = System.getenv("USERPROFILE");
        String localAppData = System.getenv("LOCALAPPDATA");
        return List.of(
                workspaceRoot.resolve("node").resolve("node.exe"),
                Path.of("D:", "devlang", "nodejs", "node.exe"),
                Path.of("C:", "Program Files", "nodejs", "node.exe"),
                Path.of("C:", "Program Files (x86)", "nodejs", "node.exe"),
                userProfile == null || userProfile.isBlank()
                        ? Path.of("__missing_userprofile_node__")
                        : Path.of(userProfile).resolve("scoop").resolve("apps").resolve("nodejs").resolve("current").resolve("node.exe"),
                localAppData == null || localAppData.isBlank()
                        ? Path.of("__missing_localappdata_node__")
                        : Path.of(localAppData).resolve("Programs").resolve("nodejs").resolve("node.exe"));
    }

    private static String normalizeExtension(String extension) {
        String value = extension.startsWith(".") ? extension.substring(1) : extension;
        return value.toLowerCase(Locale.ROOT);
    }
}
