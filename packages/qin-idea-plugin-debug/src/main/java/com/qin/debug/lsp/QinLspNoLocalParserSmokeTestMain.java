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
    private static final List<String> FORBIDDEN_DIRECT_JAVA_PSI_MARKERS = List.of(
            "JavaPsiFacade");
    private static final List<String> FORBIDDEN_PLUGIN_XML_MARKERS = List.of(
            "lang.psiStructureViewFactory");
    private static final List<String> ALLOWED_SOURCE_FILES = List.of(
            "QinLspNoLocalParserSmokeTestMain.java",
            "QinLspPluginDescriptorSmokeTestMain.java",
            "QinJavaReference.java",
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
        assertNoDirectJavaPsiAccess(javaRoot);
        assertParserDefinitionUsesSourceRangePredicates(javaRoot);
        assertImportBindingsUseSourceStructureSpecifierLookup(javaRoot);
        assertImportBindingsUseSourceStructureAliasLookup(javaRoot);
        assertObjectSymbolsUseSourceStructureDeclarationLookup(javaRoot);
        assertObjectSymbolsUseSourceStructureMemberLookup(javaRoot);
        assertObjectSymbolsUseSourceStructureMemberKind(javaRoot);
        assertStubIndexUsesSourceStructureMemberIndexEntries(javaRoot);

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

    private static void assertNoDirectJavaPsiAccess(Path javaRoot) throws Exception {
        try (var files = Files.walk(javaRoot)) {
            for (Path file : files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList()) {
                if (isAllowedSourceFile(file)) {
                    continue;
                }
                String source = Files.readString(file);
                for (String marker : FORBIDDEN_DIRECT_JAVA_PSI_MARKERS) {
                    require(!containsWholeMarker(source, marker),
                            "Qin Java interop must resolve Java PSI through QinJavaReference, not direct "
                                    + marker + " access in " + file);
                }
            }
        }
    }

    private static void assertParserDefinitionUsesSourceRangePredicates(Path javaRoot) throws Exception {
        Path parserDefinition = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinParserDefinition.java"));
        require(Files.isRegularFile(parserDefinition),
                "QinParserDefinition source not found: " + parserDefinition);
        String source = Files.readString(parserDefinition);
        require(!source.contains("bodyRange().startOffset()")
                        && !source.contains("bodyRange().endOffset()"),
                "QinParserDefinition must consume QinSourceStructure.SourceRange predicates "
                        + "instead of directly splitting body ranges: " + parserDefinition);
    }

    private static void assertImportBindingsUseSourceStructureSpecifierLookup(Path javaRoot) throws Exception {
        Path importBindings = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinImportBindings.java"));
        require(Files.isRegularFile(importBindings),
                "QinImportBindings source not found: " + importBindings);
        String source = Files.readString(importBindings);
        require(source.contains(".importSpecifierAtNameOffset(offset)")
                        && source.contains(".importSpecifierMatches()")
                        && !source.contains("sourceStructure.importDeclarations()")
                        && !source.contains(".specifiers()")
                        && !source.contains(".specifierAtNameOffset(offset)")
                        && !source.contains("exportedNameRange().startsAt")
                        && !source.contains("localNameRange().startsAt"),
                "QinImportBindings must use QinSourceStructure.importSpecifierAtNameOffset "
                        + "and QinSourceStructure.importSpecifierMatches instead of iterating declarations "
                        + "or splitting named import ranges: " + importBindings);
    }

    private static void assertImportBindingsUseSourceStructureAliasLookup(Path javaRoot) throws Exception {
        Path importBindings = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinImportBindings.java"));
        require(Files.isRegularFile(importBindings),
                "QinImportBindings source not found: " + importBindings);
        String source = Files.readString(importBindings);
        require(source.contains(".importAliasSpecifierNamed(")
                        && !source.contains("specifier.localName().equals(localName)")
                        && !source.contains("specifier.localNameRange().isPresent()"),
                "QinImportBindings must use QinSourceStructure import alias lookup "
                        + "instead of matching alias names itself: " + importBindings);
    }

    private static void assertObjectSymbolsUseSourceStructureDeclarationLookup(Path javaRoot) throws Exception {
        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String source = Files.readString(objectSymbols);
        require(!source.contains(".objectDeclarations()")
                        && !source.contains("keywordRange().startsAt"),
                "QinObjectSymbols must use QinSourceStructure object declaration lookup helpers "
                        + "instead of iterating declarations or matching keyword ranges: " + objectSymbols);
    }

    private static void assertObjectSymbolsUseSourceStructureMemberLookup(Path javaRoot) throws Exception {
        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String source = Files.readString(objectSymbols);
        require(source.contains(".fieldDeclarationNamed(")
                        && source.contains(".methodDeclarationNamed(")
                        && source.contains(".memberDeclarations()")
                        && !source.contains("declaration.fields()")
                        && !source.contains("declaration.methods()")
                        && !source.contains("member.name().equals(memberName)"),
                "QinObjectSymbols must use QinSourceStructure member declaration lookup helpers "
                        + "and member declarations instead of matching or flattening members itself: " + objectSymbols);
    }

    private static void assertObjectSymbolsUseSourceStructureMemberKind(Path javaRoot) throws Exception {
        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String source = Files.readString(objectSymbols);
        require(source.contains("QinSourceStructure.ObjectMemberKind")
                        && !source.contains("private enum MemberKind")
                        && !containsWholeMarker(source, "MemberKind"),
                "QinObjectSymbols must use QinSourceStructure.ObjectMemberKind "
                        + "instead of defining a local member-kind enum: " + objectSymbols);
    }

    private static void assertStubIndexUsesSourceStructureMemberIndexEntries(Path javaRoot) throws Exception {
        Path fileElementType = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinFileElementType.java"));
        require(Files.isRegularFile(fileElementType),
                "QinFileElementType source not found: " + fileElementType);
        String source = Files.readString(fileElementType);
        require(source.contains(".memberIndexEntries()")
                        && !source.contains("for (String field : declaration.fieldNames())")
                        && !source.contains("for (String method : declaration.methodNames())")
                        && !source.contains("static @NotNull String memberKey("),
                "QinFileElementType must consume QinSourceStructure member index entries "
                        + "instead of flattening members or owning object-member key syntax: " + fileElementType);
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
