package com.qin.debug.lsp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class QinLspPluginDescriptorSmokeTestMain {
    private QinLspPluginDescriptorSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path pluginXml = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("src", "main", "resources", "META-INF", "plugin.xml").toAbsolutePath().normalize();
        require(Files.isRegularFile(pluginXml), "plugin.xml not found: " + pluginXml);

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pluginXml.toFile());
        document.getDocumentElement().normalize();

        assertPureLspDescriptor(document);

        System.out.println("Qin IDEA LSP plugin descriptor smoke passed");
    }

    static void assertPureLspDescriptor(Document document) {
        assertDepends(document, "com.intellij.modules.platform");
        assertDepends(document, "com.intellij.modules.java");
        assertDepends(document, "com.intellij.modules.lsp");
        assertFileTypes(document);
        assertQinToolWindow(document);
        assertQinSyntaxHighlighter(document);
        assertQinParserShell(document);
        assertLspProvider(document);
        assertAutoPopupTypedHandler(document);
        assertLookupEnterHandler(document);
        assertQinReferenceContributors(document);
        assertQinObjectMemberCompletion(document);
        assertQinStubIndexes(document);
        assertQinAnnotators(document);
        assertQinInspections(document);
        assertNoQinObjectNameFileBasedIndex(document);
        assertNoLocalSemanticExtensions(document);
    }

    private static void assertDepends(Document document, String expected) {
        NodeList depends = document.getElementsByTagName("depends");
        for (int i = 0; i < depends.getLength(); i++) {
            if (expected.equals(depends.item(i).getTextContent().trim())) {
                return;
            }
        }
        throw new IllegalStateException("Missing plugin dependency: " + expected);
    }

    private static void assertFileTypes(Document document) {
        Map<String, String> expected = Map.of(
                "qin", "com.qin.debug.lsp.QinLspFileType",
                "ovs", "com.qin.debug.lsp.OvsLspFileType",
                "cssts", "com.qin.debug.lsp.CsstsLspFileType");
        Map<String, String> expectedNames = Map.of(
                "qin", "Qin",
                "ovs", "OVS",
                "cssts", "CSSTS");
        Map<String, String> expectedLanguages = Map.of(
                "qin", "Qin",
                "ovs", "OVS",
                "cssts", "CSSTS");
        Map<String, String> actual = new HashMap<>();
        Map<String, String> actualNames = new HashMap<>();
        Map<String, String> actualLanguages = new HashMap<>();
        NodeList fileTypes = document.getElementsByTagName("fileType");
        for (int i = 0; i < fileTypes.getLength(); i++) {
            Element fileType = (Element) fileTypes.item(i);
            actual.put(fileType.getAttribute("extensions"), fileType.getAttribute("implementationClass"));
            actualNames.put(fileType.getAttribute("extensions"), fileType.getAttribute("name"));
            actualLanguages.put(fileType.getAttribute("extensions"), fileType.getAttribute("language"));
        }
        for (Map.Entry<String, String> entry : expected.entrySet()) {
            require(entry.getValue().equals(actual.get(entry.getKey())),
                    "Missing fileType for ." + entry.getKey() + ": " + entry.getValue());
            require(expectedNames.get(entry.getKey()).equals(actualNames.get(entry.getKey())),
                    "fileType name for ." + entry.getKey()
                            + " must match FileType.getName(): expected "
                            + expectedNames.get(entry.getKey()) + ", got "
                            + actualNames.get(entry.getKey()));
            require(expectedLanguages.get(entry.getKey()).equals(actualLanguages.get(entry.getKey())),
                    "fileType language for ." + entry.getKey()
                            + " must match Language ID: expected "
                            + expectedLanguages.get(entry.getKey()) + ", got "
                            + actualLanguages.get(entry.getKey()));
        }
    }

    private static void assertLspProvider(Document document) {
        NodeList providers = document.getElementsByTagName("platform.lsp.serverSupportProvider");
        require(providers.getLength() == 1, "Expected exactly one LSP serverSupportProvider");
        Element provider = (Element) providers.item(0);
        require("com.qin.debug.lsp.QinLspServerSupportProvider".equals(provider.getAttribute("implementation")),
                "Unexpected LSP provider: " + provider.getAttribute("implementation"));
    }

    private static void assertQinToolWindow(Document document) {
        NodeList toolWindows = document.getElementsByTagName("toolWindow");
        for (int i = 0; i < toolWindows.getLength(); i++) {
            Element toolWindow = (Element) toolWindows.item(i);
            if ("Qin".equals(toolWindow.getAttribute("id"))) {
                require("right".equals(toolWindow.getAttribute("anchor")),
                        "Qin toolWindow must be anchored on the right");
                require("com.qin.debug.QinToolWindowFactory".equals(toolWindow.getAttribute("factoryClass")),
                        "Unexpected Qin toolWindow factory: " + toolWindow.getAttribute("factoryClass"));
                return;
            }
        }
        throw new IllegalStateException("Missing Qin toolWindow registration");
    }

    private static void assertQinSyntaxHighlighter(Document document) {
        NodeList highlighters = document.getElementsByTagName("lang.syntaxHighlighterFactory");
        require(highlighters.getLength() == 1, "Expected exactly one Qin syntaxHighlighterFactory");
        Element highlighter = (Element) highlighters.item(0);
        require("Qin".equals(highlighter.getAttribute("language")),
                "Unexpected syntax highlighter language: " + highlighter.getAttribute("language"));
        require("com.qin.debug.lsp.QinSyntaxHighlighterFactory".equals(
                        highlighter.getAttribute("implementationClass")),
                "Unexpected Qin syntax highlighter: " + highlighter.getAttribute("implementationClass"));
    }

    private static void assertQinParserShell(Document document) {
        NodeList parsers = document.getElementsByTagName("lang.parserDefinition");
        require(parsers.getLength() == 1, "Expected exactly one Qin parserDefinition for PSI file identity");
        Element parser = (Element) parsers.item(0);
        require("Qin".equals(parser.getAttribute("language")),
                "Unexpected parserDefinition language: " + parser.getAttribute("language"));
        require("com.qin.debug.lsp.QinParserDefinition".equals(parser.getAttribute("implementationClass")),
                "Unexpected Qin parserDefinition: " + parser.getAttribute("implementationClass"));
    }

    private static void assertAutoPopupTypedHandler(Document document) {
        assertExtensionImplementation(
                document,
                "typedHandler",
                "implementation",
                "com.qin.debug.lsp.QinLspAutoPopupTypedHandler");
    }

    private static void assertLookupEnterHandler(Document document) {
        assertExtensionImplementation(
                document,
                "enterHandlerDelegate",
                "implementation",
                "com.qin.debug.lsp.QinLspLookupEnterHandler");
    }

    private static void assertQinReferenceContributors(Document document) {
        Set<String> expected = Set.of(
                "com.qin.debug.lsp.QinJavaReferenceContributor",
                "com.qin.debug.lsp.QinObjectReferenceContributor",
                "com.qin.debug.lsp.QinObjectMethodReferenceContributor",
                "com.qin.debug.lsp.QinObjectFieldReferenceContributor",
                "com.qin.debug.lsp.QinImportAliasReferenceContributor");
        Set<String> actual = new HashSet<>();
        int qinContributorCount = 0;
        NodeList contributors = document.getElementsByTagName("psi.referenceContributor");
        for (int i = 0; i < contributors.getLength(); i++) {
            Element contributor = (Element) contributors.item(i);
            if ("Qin".equals(contributor.getAttribute("language"))) {
                qinContributorCount++;
                actual.add(contributor.getAttribute("implementation"));
            }
        }
        require(qinContributorCount == expected.size(),
                "Expected exactly " + expected.size() + " Qin reference contributors, got "
                        + qinContributorCount);
        require(actual.equals(expected),
                "Qin reference contributors must be exactly " + expected + ", got " + actual);
    }

    private static void assertQinObjectMemberCompletion(Document document) {
        NodeList contributors = document.getElementsByTagName("completion.contributor");
        require(contributors.getLength() == 1, "Expected exactly one Qin PSI completion contributor");
        Element contributor = (Element) contributors.item(0);
        require("Qin".equals(contributor.getAttribute("language")),
                "Unexpected completion contributor language: " + contributor.getAttribute("language"));
        require("com.qin.debug.lsp.QinObjectMemberCompletionContributor".equals(
                        contributor.getAttribute("implementationClass")),
                "Unexpected Qin completion contributor: " + contributor.getAttribute("implementationClass"));
    }

    private static void assertQinAnnotators(Document document) {
        Set<String> expected = Set.of(
                "com.qin.debug.lsp.QinUnresolvedReferenceAnnotator",
                "com.qin.debug.lsp.QinSymbolHighlightAnnotator");
        Set<String> actual = new HashSet<>();
        int qinAnnotatorCount = 0;
        NodeList annotators = document.getElementsByTagName("annotator");
        for (int i = 0; i < annotators.getLength(); i++) {
            Element annotator = (Element) annotators.item(i);
            if ("Qin".equals(annotator.getAttribute("language"))) {
                qinAnnotatorCount++;
                actual.add(annotator.getAttribute("implementationClass"));
            }
        }
        require(qinAnnotatorCount == expected.size(),
                "Expected exactly " + expected.size() + " Qin annotators, got " + qinAnnotatorCount);
        require(actual.equals(expected),
                "Qin annotators must be exactly " + expected + ", got " + actual);
    }

    private static void assertQinInspections(Document document) {
        NodeList inspections = document.getElementsByTagName("localInspection");
        require(inspections.getLength() == 1, "Expected exactly one Qin localInspection");
        Element inspection = (Element) inspections.item(0);
        require("Qin".equals(inspection.getAttribute("language")),
                "Unexpected Qin inspection language: " + inspection.getAttribute("language"));
        require("QinUnresolvedReference".equals(inspection.getAttribute("shortName")),
                "Unexpected Qin inspection shortName: " + inspection.getAttribute("shortName"));
        require("Unresolved Qin reference".equals(inspection.getAttribute("displayName")),
                "Unexpected Qin inspection displayName: " + inspection.getAttribute("displayName"));
        require("Qin".equals(inspection.getAttribute("groupName")),
                "Unexpected Qin inspection groupName: " + inspection.getAttribute("groupName"));
        require("true".equals(inspection.getAttribute("enabledByDefault")),
                "Qin unresolved-reference inspection should be enabled by default");
        require("ERROR".equals(inspection.getAttribute("level")),
                "Qin unresolved-reference inspection should report errors");
        require("com.qin.debug.lsp.QinUnresolvedReferenceInspection".equals(
                        inspection.getAttribute("implementationClass")),
                "Unexpected Qin inspection: " + inspection.getAttribute("implementationClass"));
    }

    private static void assertQinStubIndexes(Document document) {
        Set<String> expected = Set.of(
                "com.qin.debug.lsp.QinObjectNameStubIndex",
                "com.qin.debug.lsp.QinObjectFieldNameStubIndex",
                "com.qin.debug.lsp.QinObjectMethodNameStubIndex");
        Set<String> actual = new HashSet<>();
        NodeList indexes = document.getElementsByTagName("stubIndex");
        for (int i = 0; i < indexes.getLength(); i++) {
            Element index = (Element) indexes.item(i);
            actual.add(index.getAttribute("implementation"));
        }
        require(actual.equals(expected),
                "Qin StubIndexes must be exactly " + expected + ", got " + actual);
    }

    private static void assertNoQinObjectNameFileBasedIndex(Document document) {
        NodeList indexes = document.getElementsByTagName("fileBasedIndex");
        for (int i = 0; i < indexes.getLength(); i++) {
            Element index = (Element) indexes.item(i);
            require(!"com.qin.debug.lsp.QinObjectNameIndex".equals(index.getAttribute("implementation")),
                    "Qin object-name indexing must use StubIndex, not the transitional FileBasedIndex");
        }
    }

    private static void assertNoLocalSemanticExtensions(Document document) {
        Set<String> forbiddenTags = Set.of(
                "lang.psiStructureViewFactory");
        for (String tag : forbiddenTags) {
            require(document.getElementsByTagName(tag).getLength() == 0,
                    "Qin LSP mode must not register local IDEA semantic extension: " + tag);
        }
    }

    private static void assertExtensionImplementation(
            Document document,
            String tagName,
            String attributeName,
            String expectedImplementation) {
        NodeList extensions = document.getElementsByTagName(tagName);
        for (int i = 0; i < extensions.getLength(); i++) {
            Element extension = (Element) extensions.item(i);
            if (expectedImplementation.equals(extension.getAttribute(attributeName))) {
                return;
            }
        }
        throw new IllegalStateException("Missing " + tagName + ": " + expectedImplementation);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
