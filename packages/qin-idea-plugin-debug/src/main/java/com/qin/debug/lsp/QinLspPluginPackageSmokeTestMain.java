package com.qin.debug.lsp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class QinLspPluginPackageSmokeTestMain {
    private static final Set<String> DESCRIPTOR_CLASS_ATTRIBUTES = Set.of(
            "implementation",
            "implementationClass",
            "factoryClass",
            "serviceImplementation");

    private static final Set<String> REQUIRED_CLASSES = Set.of(
            "com/qin/debug/lsp/QinLspFileType.class",
            "com/qin/debug/lsp/QinLanguage.class",
            "com/qin/debug/lsp/QinFileElementType.class",
            "com/qin/debug/lsp/QinFileStub.class",
            "com/qin/debug/lsp/QinFileStubBuilder.class",
            "com/qin/debug/lsp/QinLexer.class",
            "com/qin/debug/lsp/QinLexicalScanner.class",
            "com/qin/debug/lsp/QinLexicalToken.class",
            "com/qin/debug/lsp/QinParserDefinition.class",
            "com/qin/debug/lsp/QinPsiElement.class",
            "com/qin/debug/lsp/QinPsiFile.class",
            "com/qin/debug/lsp/QinPsiRenames.class",
            "com/qin/debug/lsp/QinPsiReferences.class",
            "com/qin/debug/lsp/QinPsiResolveResult.class",
            "com/qin/debug/lsp/QinPsiToken.class",
            "com/qin/debug/lsp/QinPsiTokenStream.class",
            "com/qin/debug/lsp/QinTokenFacts.class",
            "com/qin/debug/lsp/QinTokenTypes.class",
            "com/qin/debug/lsp/QinSyntaxHighlighter.class",
            "com/qin/debug/lsp/QinSyntaxHighlighterFactory.class",
            "com/qin/debug/lsp/QinNamedPsiElement.class",
            "com/qin/debug/lsp/QinObjectNamePsiElement.class",
            "com/qin/debug/lsp/QinMethodNamePsiElement.class",
            "com/qin/debug/lsp/QinFieldNamePsiElement.class",
            "com/qin/debug/lsp/QinImportAliasNamePsiElement.class",
            "com/qin/debug/lsp/QinPsiTree.class",
            "com/qin/debug/lsp/QinObjectSymbols.class",
            "com/qin/debug/lsp/QinSourceStructure.class",
            "com/qin/debug/lsp/QinSourceStructure$ImportDeclaration.class",
            "com/qin/debug/lsp/QinSourceStructure$ImportSpecifier.class",
            "com/qin/debug/lsp/QinSourceStructure$ImportSpecifierMatch.class",
            "com/qin/debug/lsp/QinSourceStructure$ObjectDeclaration.class",
            "com/qin/debug/lsp/QinSourceStructure$ObjectMemberDeclaration.class",
            "com/qin/debug/lsp/QinSourceStructure$ObjectMemberIndexEntry.class",
            "com/qin/debug/lsp/QinSourceStructure$ObjectMemberKind.class",
            "com/qin/debug/lsp/QinSourceStructure$MemberDeclaration.class",
            "com/qin/debug/lsp/QinSourceStructure$SourceRange.class",
            "com/qin/debug/lsp/QinImportBindings.class",
            "com/qin/debug/lsp/QinImportAliasReference.class",
            "com/qin/debug/lsp/QinImportAliasReferenceContributor.class",
            "com/qin/debug/lsp/QinJavaImportTable.class",
            "com/qin/debug/lsp/QinJavaReference.class",
            "com/qin/debug/lsp/QinJavaReferenceContributor.class",
            "com/qin/debug/lsp/QinModuleSpecifierFacts.class",
            "com/qin/debug/lsp/QinModuleImportTable.class",
            "com/qin/debug/lsp/QinObjectFieldNameStubIndex.class",
            "com/qin/debug/lsp/QinObjectMemberStubIndexes.class",
            "com/qin/debug/lsp/QinObjectMethodNameStubIndex.class",
            "com/qin/debug/lsp/QinObjectNameStubIndex.class",
            "com/qin/debug/lsp/QinObjectReference.class",
            "com/qin/debug/lsp/QinObjectReferenceContributor.class",
            "com/qin/debug/lsp/QinObjectMethodReference.class",
            "com/qin/debug/lsp/QinObjectMethodReferenceContributor.class",
            "com/qin/debug/lsp/QinObjectFieldReference.class",
            "com/qin/debug/lsp/QinObjectFieldReferenceContributor.class",
            "com/qin/debug/lsp/QinObjectMemberCompletions.class",
            "com/qin/debug/lsp/QinObjectMemberCompletionContributor.class",
            "com/qin/debug/lsp/QinSymbolHighlightAnnotator.class",
            "com/qin/debug/lsp/QinSymbolHighlights.class",
            "com/qin/debug/lsp/QinSymbolHighlights$SymbolHighlight.class",
            "com/qin/debug/lsp/QinUnresolvedReferenceAnnotator.class",
            "com/qin/debug/lsp/QinUnresolvedReferenceInspection.class",
            "com/qin/debug/lsp/QinUnresolvedReferenceMessages.class",
            "com/qin/debug/lsp/QinReferenceElements.class",
            "com/qin/debug/lsp/OvsLspFileType.class",
            "com/qin/debug/lsp/CsstsLspFileType.class",
            "com/qin/debug/lsp/OvsLanguage.class",
            "com/qin/debug/lsp/CsstsLanguage.class",
            "com/qin/debug/lsp/QinLspLanguage.class",
            "com/qin/debug/lsp/QinLspLanguageRegistry.class",
            "com/qin/debug/lsp/QinLspAutoPopupTypedHandler.class",
            "com/qin/debug/lsp/QinLspLookupEnterHandler.class",
            "com/qin/debug/lsp/QinLspServerCommandLineFactory.class",
            "com/qin/debug/lsp/QinLspServerCommandSpec.class",
            "com/qin/debug/lsp/QinLspServerSupportProvider.class",
            "com/qin/debug/lsp/QinLspStartupProbe.class",
            "com/qin/debug/run/QinRunConfigurationDefaults.class",
            "com/qin/debug/run/QinJavaRunPsi.class",
            "com/qin/debug/run/QinJavaRunPsi$RunTarget.class",
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
                assertDescriptorImplementationClasses(pluginJar, document);
            }

            for (String requiredClass : REQUIRED_CLASSES) {
                require(pluginJar.getEntry(requiredClass) != null, "Missing LSP class in plugin jar: " + requiredClass);
            }
        }
    }

    private static void assertDescriptorImplementationClasses(ZipFile pluginJar, Document document) {
        NodeList elements = document.getElementsByTagName("*");
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            for (String attribute : DESCRIPTOR_CLASS_ATTRIBUTES) {
                String className = element.getAttribute(attribute).trim();
                if (!className.isEmpty()) {
                    assertPluginClass(pluginJar, className,
                            element.getTagName() + " " + attribute);
                }
            }
        }
    }

    private static void assertPluginClass(ZipFile pluginJar, String className, String source) {
        String entryName = className.replace('.', '/') + ".class";
        require(pluginJar.getEntry(entryName) != null,
                "Descriptor registered class is missing from plugin jar: "
                        + className + " from " + source);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
