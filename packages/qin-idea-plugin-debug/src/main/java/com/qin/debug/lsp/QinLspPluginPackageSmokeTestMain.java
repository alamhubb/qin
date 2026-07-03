package com.qin.debug.lsp;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class QinLspPluginPackageSmokeTestMain {
    private static final Set<String> REQUIRED_CLASSES = Set.of(
            "com/qin/debug/lsp/QinLspFileType.class",
            "com/qin/debug/lsp/QinLanguage.class",
            "com/qin/debug/lsp/QinLexer.class",
            "com/qin/debug/lsp/QinLexicalScanner.class",
            "com/qin/debug/lsp/QinLexicalToken.class",
            "com/qin/debug/lsp/QinTokenTypes.class",
            "com/qin/debug/lsp/QinSyntaxHighlighter.class",
            "com/qin/debug/lsp/QinSyntaxHighlighterFactory.class",
            "com/qin/debug/lsp/QinNamedPsiElement.class",
            "com/qin/debug/lsp/QinObjectNamePsiElement.class",
            "com/qin/debug/lsp/QinMethodNamePsiElement.class",
            "com/qin/debug/lsp/QinObjectSymbols.class",
            "com/qin/debug/lsp/QinObjectReference.class",
            "com/qin/debug/lsp/QinObjectReferenceContributor.class",
            "com/qin/debug/lsp/QinObjectMethodReference.class",
            "com/qin/debug/lsp/QinObjectMethodReferenceContributor.class",
            "com/qin/debug/lsp/QinReferenceElements.class",
            "com/qin/debug/lsp/OvsLspFileType.class",
            "com/qin/debug/lsp/CsstsLspFileType.class",
            "com/qin/debug/lsp/OvsLanguage.class",
            "com/qin/debug/lsp/CsstsLanguage.class",
            "com/qin/debug/lsp/QinLspLanguage.class",
            "com/qin/debug/lsp/QinLspLanguageRegistry.class",
            "com/qin/debug/lsp/QinLspServerCommandLineFactory.class",
            "com/qin/debug/lsp/QinLspServerCommandSpec.class",
            "com/qin/debug/lsp/QinLspServerSupportProvider.class",
            "com/qin/debug/lsp/QinLspStartupProbe.class",
            "com/slime/token/JavaScriptTokens.class",
            "com/slime/token/TokenUtils.class",
            "com/subhuti/lexer/SubhutiLexer.class",
            "com/subhuti/struct/SubhutiMatchToken.class",
            "com/qin/debug/QinToolWindowFactory.class");

    private QinLspPluginPackageSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path zip = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : latestDistributionZip();
        require(Files.isRegularFile(zip), "Plugin distribution zip not found: " + zip);

        try (ZipFile distribution = new ZipFile(zip.toFile())) {
            ZipEntry pluginJarEntry = distribution.stream()
                    .filter(entry -> entry.getName().matches("[^/]+/lib/[^/]+\\.jar"))
                    .filter(entry -> fileName(entry.getName()).startsWith("qin-idea-plugin-debug"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Plugin jar not found in " + zip));

            Path extractedJar = Files.createTempFile("qin-idea-plugin-debug-", ".jar");
            try {
                try (InputStream input = distribution.getInputStream(pluginJarEntry)) {
                    Files.copy(input, extractedJar, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
                assertPluginJar(extractedJar);
            } finally {
                Files.deleteIfExists(extractedJar);
            }
        }

        System.out.println("Qin IDEA LSP plugin package smoke passed");
    }

    private static String fileName(String entryName) {
        int slash = entryName.lastIndexOf('/');
        return slash >= 0 ? entryName.substring(slash + 1) : entryName;
    }

    private static Path latestDistributionZip() throws Exception {
        Path distributions = Path.of("build", "distributions").toAbsolutePath().normalize();
        try (var stream = Files.list(distributions)) {
            return stream
                    .filter(path -> path.getFileName().toString().endsWith(".zip"))
                    .max(Comparator.comparing(path -> {
                        try {
                            return Files.getLastModifiedTime(path);
                        } catch (Exception e) {
                            throw new IllegalStateException(e);
                        }
                    }))
                    .orElseThrow(() -> new IllegalStateException("No plugin zip in " + distributions));
        }
    }

    private static void assertPluginJar(Path jar) throws Exception {
        try (ZipFile pluginJar = new ZipFile(jar.toFile())) {
            ZipEntry pluginXml = pluginJar.getEntry("META-INF/plugin.xml");
            require(pluginXml != null, "META-INF/plugin.xml missing in plugin jar");
            try (InputStream input = pluginJar.getInputStream(pluginXml)) {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
                document.getDocumentElement().normalize();
                QinLspPluginDescriptorSmokeTestMain.assertPureLspDescriptor(document);
            }

            for (String requiredClass : REQUIRED_CLASSES) {
                require(pluginJar.getEntry(requiredClass) != null, "Missing LSP class in plugin jar: " + requiredClass);
            }
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
