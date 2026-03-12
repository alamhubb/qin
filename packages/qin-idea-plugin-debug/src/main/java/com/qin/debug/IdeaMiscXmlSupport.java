package com.qin.debug;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

final class IdeaMiscXmlSupport {

    private static final String PROJECT = "project";
    private static final String COMPONENT = "component";
    private static final String NAME = "name";
    private static final String PROJECT_ROOT_MANAGER = "ProjectRootManager";

    private IdeaMiscXmlSupport() {
    }

    static void updateProjectSdk(Path miscXml, String sdkName) throws Exception {
        Document document = loadOrCreateDocument(miscXml);
        Element rootManager = getOrCreateProjectRootManager(document);
        rootManager.setAttribute("version", "2");
        rootManager.setAttribute("project-jdk-name", sdkName);
        rootManager.setAttribute("project-jdk-type", "JavaSDK");
        ensureOutputElement(document, rootManager);
        writeDocumentAtomically(miscXml, document);
    }

    static void updateLanguageLevel(Path miscXml, String targetVersion) throws Exception {
        Document document = loadOrCreateDocument(miscXml);
        Element rootManager = getOrCreateProjectRootManager(document);
        rootManager.setAttribute("version", "2");
        rootManager.setAttribute("languageLevel", "JDK_" + targetVersion);
        ensureOutputElement(document, rootManager);
        writeDocumentAtomically(miscXml, document);
    }

    private static Document loadOrCreateDocument(Path miscXml) throws Exception {
        Files.createDirectories(miscXml.getParent());
        String content = Files.exists(miscXml) ? Files.readString(miscXml, StandardCharsets.UTF_8) : "";
        if (content.isBlank()) {
            return createDocument();
        }
        try {
            DocumentBuilder builder = newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(content)));
            Element root = document.getDocumentElement();
            if (root == null || !PROJECT.equals(root.getTagName())) {
                return createDocument();
            }
            return document;
        } catch (Exception ex) {
            QinLogger.error("[IDEA] Invalid misc.xml detected, recreating: " + miscXml);
            return createDocument();
        }
    }

    private static Document createDocument() throws Exception {
        Document document = newDocumentBuilder().newDocument();
        Element project = document.createElement(PROJECT);
        project.setAttribute("version", "4");
        document.appendChild(project);
        return document;
    }

    private static DocumentBuilder newDocumentBuilder() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringComments(false);
        factory.setIgnoringElementContentWhitespace(false);
        return factory.newDocumentBuilder();
    }

    private static Element getOrCreateProjectRootManager(Document document) {
        NodeList components = document.getDocumentElement().getElementsByTagName(COMPONENT);
        for (int i = 0; i < components.getLength(); i++) {
            Element element = (Element) components.item(i);
            if (PROJECT_ROOT_MANAGER.equals(element.getAttribute(NAME))) {
                return element;
            }
        }
        Element component = document.createElement(COMPONENT);
        component.setAttribute(NAME, PROJECT_ROOT_MANAGER);
        document.getDocumentElement().appendChild(component);
        return component;
    }

    private static void ensureOutputElement(Document document, Element rootManager) {
        NodeList outputs = rootManager.getElementsByTagName("output");
        if (outputs.getLength() > 0) {
            return;
        }
        Element output = document.createElement("output");
        output.setAttribute("url", "file://$PROJECT_DIR$/out");
        rootManager.appendChild(output);
    }

    private static void writeDocumentAtomically(Path miscXml, Document document) throws Exception {
        String xml = toXml(document);
        Path parent = miscXml.getParent();
        Path tempFile = Files.createTempFile(parent, miscXml.getFileName().toString(), ".tmp");
        try {
            Files.writeString(tempFile, xml, StandardCharsets.UTF_8);
            try {
                Files.move(tempFile, miscXml, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException ignored) {
                Files.move(tempFile, miscXml, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(tempFile);
        }
        if (!Files.exists(miscXml) || Files.size(miscXml) == 0) {
            throw new IOException("misc.xml write produced empty file: " + miscXml);
        }
    }

    private static String toXml(Document document) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        try {
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        } catch (IllegalArgumentException ignored) {
        }
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString().replace("\r\n", "\n");
    }
}
