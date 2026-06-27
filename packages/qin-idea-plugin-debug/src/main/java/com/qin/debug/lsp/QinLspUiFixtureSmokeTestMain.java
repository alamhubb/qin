package com.qin.debug.lsp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinLspUiFixtureSmokeTestMain {
    private QinLspUiFixtureSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path fixtureRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("fixtures", "lsp-ui").toAbsolutePath().normalize();
        require(Files.isDirectory(fixtureRoot), "LSP UI fixture directory not found: " + fixtureRoot);

        Map<String, String> expectedFiles = Map.of(
                "good.qin", "export object Counter",
                "bad.qin", "export object Broken",
                "good.ovs", "export default div",
                "bad.ovs", "'Broken'",
                "good.cssts", "css {",
                "bad.cssts", "const broken = css {");

        for (Map.Entry<String, String> entry : expectedFiles.entrySet()) {
            Path file = fixtureRoot.resolve(entry.getKey());
            require(Files.isRegularFile(file), "Missing LSP UI fixture file: " + file);
            String source = Files.readString(file, StandardCharsets.UTF_8);
            require(source.contains(entry.getValue()), "Unexpected fixture content in " + file);
        }

        Path workspaceRoot = QinLspLanguageRegistry.resolveWorkspaceRoot(fixtureRoot);
        for (String extension : new String[] { "qin", "ovs", "cssts" }) {
            require(QinLspLanguageRegistry.fromExtension(workspaceRoot, extension) != null,
                    "Missing registered language for ." + extension);
        }

        System.out.println("Qin IDEA LSP UI fixture smoke passed");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
