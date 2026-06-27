package com.qin.debug.lsp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinLspLanguageRegistrySmokeTestMain {
    private QinLspLanguageRegistrySmokeTestMain() {
    }

    public static void main(String[] args) {
        Path workspaceRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of("."));

        Map<String, String> expectedIds = Map.of(
                "qin", "qin",
                "ovs", "ovs",
                "cssts", "cssts");

        for (Map.Entry<String, String> expected : expectedIds.entrySet()) {
            QinLspLanguage language = QinLspLanguageRegistry.fromExtension(workspaceRoot, expected.getKey());
            require(language != null, "Missing language for ." + expected.getKey());
            require(expected.getValue().equals(language.id()), "Unexpected language id for ." + expected.getKey());
            require(language.matchesExtension(expected.getKey().toUpperCase()), "Extension match must be case-insensitive");

            Path serverPath = language.resolveServerPath(workspaceRoot);
            require(Files.isRegularFile(serverPath), "Missing server bundle: " + serverPath);
            require(serverPath.startsWith(workspaceRoot), "Server bundle must stay inside workspace: " + serverPath);
        }

        require(QinLspLanguageRegistry.fromExtension(workspaceRoot, "txt") == null, "Unexpected language for .txt");
        System.out.println("Qin LSP language registry smoke passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
