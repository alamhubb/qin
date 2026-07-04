package com.qin.debug.lsp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinLspNoLocalParserSmokeTestMain {
    private static final List<String> FORBIDDEN_SOURCE_MARKERS = List.of(
            "PsiStructureViewFactory");
    private static final List<String> FORBIDDEN_DIRECT_REFERENCE_MARKERS = List.of(
            "ReferenceProvidersRegistry",
            "getReferencesFromProviders");
    private static final List<String> FORBIDDEN_PLUGIN_XML_MARKERS = List.of(
            "lang.psiStructureViewFactory");
    private static final List<String> ALLOWED_SOURCE_FILES = List.of(
            "QinLspNoLocalParserSmokeTestMain.java",
            "QinLspPluginDescriptorSmokeTestMain.java",
            "QinPsiReferences.java",
            "QinSyntaxHighlighter.java",
            "QinSyntaxHighlighterFactory.java",
            "QinLexer.java");

    private QinLspNoLocalParserSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path projectRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of(".").toAbsolutePath().normalize();
        Path sourceRoot = projectRoot.resolve("src").resolve("main").normalize();
        Path javaRoot = sourceRoot.resolve("java").normalize();
        Path pluginXml = sourceRoot.resolve("resources").resolve("META-INF").resolve("plugin.xml").normalize();

        require(Files.isDirectory(javaRoot), "Java source root not found: " + javaRoot);
        require(Files.isRegularFile(pluginXml), "plugin.xml not found: " + pluginXml);

        assertNoForbiddenPluginXmlMarkers(pluginXml);
        assertNoForbiddenSourceMarkers(javaRoot);
        assertNoDirectReferenceRegistryAccess(javaRoot);

        System.out.println("Qin IDEA LSP no-local-parser smoke passed");
    }

    private static void assertNoForbiddenPluginXmlMarkers(Path pluginXml) throws Exception {
        String source = Files.readString(pluginXml);
        for (String marker : FORBIDDEN_PLUGIN_XML_MARKERS) {
            require(!source.contains("<" + marker) && !source.contains("</" + marker),
                    "Pure LSP mode must not register local IDEA language extension " + marker
                            + " in " + pluginXml);
        }
    }

    private static void assertNoForbiddenSourceMarkers(Path javaRoot) throws Exception {
        try (var files = Files.walk(javaRoot)) {
            for (Path file : files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList()) {
                if (isAllowedSourceFile(file)) {
                    continue;
                }
                String source = Files.readString(file);
                for (String marker : FORBIDDEN_SOURCE_MARKERS) {
                    require(!containsWholeMarker(source, marker),
                            "Pure LSP mode must not add local IDEA language implementation marker "
                                    + marker + " in " + file);
                }
            }
        }
    }

    private static void assertNoDirectReferenceRegistryAccess(Path javaRoot) throws Exception {
        try (var files = Files.walk(javaRoot)) {
            for (Path file : files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList()) {
                if (isAllowedSourceFile(file)) {
                    continue;
                }
                String source = Files.readString(file);
                for (String marker : FORBIDDEN_DIRECT_REFERENCE_MARKERS) {
                    require(!containsWholeMarker(source, marker),
                            "Qin IDEA references must flow through QinPsiReferences, not direct "
                                    + marker + " access in " + file);
                }
            }
        }
    }

    private static boolean isAllowedSourceFile(Path file) {
        String fileName = file.getFileName().toString();
        return ALLOWED_SOURCE_FILES.contains(fileName);
    }

    private static boolean containsWholeMarker(String source, String marker) {
        int index = source.indexOf(marker);
        while (index >= 0) {
            boolean leftBoundary = index == 0 || !isIdentifierPart(source.charAt(index - 1));
            int end = index + marker.length();
            boolean rightBoundary = end >= source.length() || !isIdentifierPart(source.charAt(end));
            if (leftBoundary && rightBoundary) {
                return true;
            }
            index = source.indexOf(marker, end);
        }
        return false;
    }

    private static boolean isIdentifierPart(char value) {
        return Character.isLetterOrDigit(value) || value == '_' || value == '$';
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
