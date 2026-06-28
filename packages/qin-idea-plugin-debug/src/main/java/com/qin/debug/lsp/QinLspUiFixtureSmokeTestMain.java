package com.qin.debug.lsp;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinLspUiFixtureSmokeTestMain {
    private QinLspUiFixtureSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path fixtureRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("fixtures", "lsp-ui").toAbsolutePath().normalize();
        require(Files.isDirectory(fixtureRoot), "LSP UI fixture directory not found: " + fixtureRoot);

        List<FixtureCase> fixtureCases = List.of(
                new FixtureCase("qin", "good.qin", "bad.qin",
                        "export object Counter", "export object Broken", "value ="),
                new FixtureCase("ovs", "good.ovs", "bad.ovs",
                        "export default div", "'Broken'", "div {"),
                new FixtureCase("cssts", "good.cssts", "bad.cssts",
                        "css {", "const broken = css {", "displayFlex,"));

        for (FixtureCase fixtureCase : fixtureCases) {
            String validSource = readFixture(fixtureRoot, fixtureCase.validFile());
            require(validSource.contains(fixtureCase.validNeedle()),
                    "Unexpected valid fixture content in " + fixtureCase.validFile());

            String invalidSource = readFixture(fixtureRoot, fixtureCase.invalidFile());
            require(invalidSource.contains(fixtureCase.invalidNeedle()),
                    "Unexpected invalid fixture content in " + fixtureCase.invalidFile());
            require(invalidSource.contains(fixtureCase.errorShapeNeedle()),
                    "Invalid fixture must contain a parser-visible error shape in " + fixtureCase.invalidFile());
        }

        Path workspaceRoot = QinLspLanguageRegistry.resolveWorkspaceRoot(fixtureRoot);
        for (String extension : new String[] { "qin", "ovs", "cssts" }) {
            require(QinLspLanguageRegistry.fromExtension(workspaceRoot, extension) != null,
                    "Missing registered language for ." + extension);
        }
        assertDiagnosticsSmokeCoversFixtureLanguages();

        System.out.println("Qin IDEA LSP UI fixture smoke passed");
    }

    private static String readFixture(Path fixtureRoot, String fileName) throws Exception {
        Path file = fixtureRoot.resolve(fileName);
        require(Files.isRegularFile(file), "Missing LSP UI fixture file: " + file);
        return Files.readString(file, StandardCharsets.UTF_8);
    }

    private static void assertDiagnosticsSmokeCoversFixtureLanguages() throws Exception {
        Path diagnosticsSmoke = Path.of("src", "main", "java", "com", "qin", "debug", "lsp",
                "QinLspServerDiagnosticsSmokeTestMain.java");
        require(Files.isRegularFile(diagnosticsSmoke), "Missing diagnostics smoke source: " + diagnosticsSmoke);
        String source = Files.readString(diagnosticsSmoke, StandardCharsets.UTF_8);

        for (String extension : List.of("qin", "ovs", "cssts")) {
            require(source.contains("new LanguageCase(\n                        \"" + extension + "\""),
                    "Diagnostics smoke must open ." + extension + " documents");
            require(source.contains("bad.\" + testCase.extension()"),
                    "Diagnostics smoke must publish invalid fixture-shaped documents");
            require(source.contains("good.\" + testCase.extension()"),
                    "Diagnostics smoke must publish valid fixture-shaped documents");
        }
        for (String method : List.of(
                "textDocument/publishDiagnostics",
                "textDocument/completion",
                "textDocument/definition",
                "textDocument/documentSymbol",
                "textDocument/semanticTokens/full")) {
            require(source.contains(method), "Diagnostics smoke must request or await " + method);
        }
        for (String capability : List.of(
                "completionProvider",
                "definitionProvider",
                "referencesProvider",
                "documentSymbolProvider",
                "semanticTokensProvider")) {
            require(source.contains(capability), "Diagnostics smoke must assert " + capability);
        }
    }

    private record FixtureCase(
            String extension,
            String validFile,
            String invalidFile,
            String validNeedle,
            String invalidNeedle,
            String errorShapeNeedle) {
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
