package com.qin.debug.lsp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

record QinLspLanguage(
        String id,
        String extension,
        String serviceExtension,
        String generatedParserTarget,
        String parserPackage,
        String compilerPackage,
        String displayName,
        Path projectRelativePath,
        Path serverBundlePath) {
    boolean matchesExtension(String candidate) {
        return candidate != null && extension.equals(candidate.toLowerCase(Locale.ROOT));
    }

    Path resolveServerPath(Path workspaceRoot) {
        Path serverPath = resolveServerBundle(workspaceRoot);
        if (!Files.isRegularFile(serverPath)) {
            throw new IllegalStateException(displayName + " language server bundle not found: " + serverPath);
        }
        return serverPath;
    }

    Path resolveServerRoot(Path workspaceRoot) {
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
