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
        assertQualifierLookupUsesSharedReferenceElements(javaRoot);
        assertReferenceTokenChecksUseSharedReferenceElements(javaRoot);
        assertImportBindingsUseSourceStructureSpecifierLookup(javaRoot);
        assertImportBindingsUseSourceStructureAliasLookup(javaRoot);
        assertImportAliasPsiBridgeUsesQinPsiTree(javaRoot);
        assertObjectSymbolsUseSourceStructureDeclarationLookup(javaRoot);
        assertObjectDeclarationPsiBridgeUsesQinPsiTree(javaRoot);
        assertObjectNamePsiBridgeUsesQinPsiTree(javaRoot);
        assertObjectDeclarationAncestryUsesQinPsiTree(javaRoot);
        assertObjectSymbolsUseSourceStructureMemberLookup(javaRoot);
        assertObjectSymbolsUseSourceStructureMemberKind(javaRoot);
        assertObjectMemberPsiBridgeUsesQinPsiTree(javaRoot);
        assertStubIndexUsesSourceStructureMemberIndexEntries(javaRoot);
        assertMemberStubIndexKeySelectionIsShared(javaRoot);

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

    private static void assertQualifierLookupUsesSharedReferenceElements(Path javaRoot) throws Exception {
        Path referenceElements = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinReferenceElements.java"));
        require(Files.isRegularFile(referenceElements),
                "QinReferenceElements source not found: " + referenceElements);
        String helperSource = Files.readString(referenceElements);
        require(helperSource.contains("previousQualifierName(")
                        && helperSource.contains("QinTokenTypes.MEMBER_ACCESS")
                        && helperSource.contains("QinPsiTokenStream.previousQualifierName("),
                "QinReferenceElements must own shared Qin qualifier lookup: " + referenceElements);

        Path javaReference = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinJavaReference.java"));
        require(Files.isRegularFile(javaReference),
                "QinJavaReference source not found: " + javaReference);
        String javaReferenceSource = Files.readString(javaReference);
        require(!javaReferenceSource.contains("static @Nullable String previousQualifierName("),
                "QinJavaReference must not own generic Qin qualifier lookup: " + javaReference);

        try (var files = Files.walk(javaRoot)) {
            for (Path file : files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> !path.getFileName().toString().equals("QinLspNoLocalParserSmokeTestMain.java"))
                    .toList()) {
                String source = Files.readString(file);
                require(!source.contains("QinJavaReference.previousQualifierName("),
                        "Qin qualifier lookup must flow through QinReferenceElements, not QinJavaReference: "
                                + file);
            }
        }
    }

    private static void assertReferenceTokenChecksUseSharedReferenceElements(Path javaRoot) throws Exception {
        Path referenceElements = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinReferenceElements.java"));
        require(Files.isRegularFile(referenceElements),
                "QinReferenceElements source not found: " + referenceElements);
        String helperSource = Files.readString(referenceElements);
        require(helperSource.contains("isReferenceIdentifier(")
                        && helperSource.contains("QinTokenTypes.REFERENCE_IDENTIFIER")
                        && helperSource.contains("isImportAliasDeclaration(")
                        && helperSource.contains("QinTokenTypes.IMPORT_ALIAS_NAME"),
                "QinReferenceElements must own shared reference token checks: "
                        + referenceElements);

        Path unresolvedMessages = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinUnresolvedReferenceMessages.java"));
        require(Files.isRegularFile(unresolvedMessages),
                "QinUnresolvedReferenceMessages source not found: " + unresolvedMessages);
        String unresolvedMessagesSource = Files.readString(unresolvedMessages);
        require(unresolvedMessagesSource.contains("QinReferenceElements.isReferenceIdentifier(")
                        && !unresolvedMessagesSource.contains("QinTokenTypes.REFERENCE_IDENTIFIER"),
                "QinUnresolvedReferenceMessages must use QinReferenceElements for reference "
                        + "identifier checks instead of owning the token mapping: " + unresolvedMessages);

        Path importAliasReference = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinImportAliasReference.java"));
        require(Files.isRegularFile(importAliasReference),
                "QinImportAliasReference source not found: " + importAliasReference);
        String importAliasReferenceSource = Files.readString(importAliasReference);
        require(importAliasReferenceSource.contains("QinReferenceElements.isImportAliasDeclaration(")
                        && !importAliasReferenceSource.contains("QinTokenTypes.IMPORT_ALIAS_NAME"),
                "QinImportAliasReference must use QinReferenceElements for import alias "
                        + "declaration checks instead of owning the token mapping: " + importAliasReference);
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

    private static void assertImportAliasPsiBridgeUsesQinPsiTree(Path javaRoot) throws Exception {
        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("importAliasNameElement(")
                        && psiTreeSource.contains("QinTokenTypes.IMPORT_ALIAS_NAME"),
                "QinPsiTree must own import alias source range to PSI name bridging: "
                        + psiTree);

        Path importBindings = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinImportBindings.java"));
        require(Files.isRegularFile(importBindings),
                "QinImportBindings source not found: " + importBindings);
        String importBindingsSource = Files.readString(importBindings);
        require(importBindingsSource.contains("QinPsiTree.importAliasNameElement(")
                        && !importBindingsSource.contains("QinTokenTypes.IMPORT_ALIAS_NAME"),
                "QinImportBindings must ask QinPsiTree to bridge import alias ranges "
                        + "instead of owning the IMPORT_ALIAS_NAME token mapping: " + importBindings);
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

    private static void assertObjectDeclarationPsiBridgeUsesQinPsiTree(Path javaRoot) throws Exception {
        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("sourceObjectDeclaration(")
                        && psiTreeSource.contains(".objectDeclarationAtKeywordOffset(startOffset)"),
                "QinPsiTree must own OBJECT_DECLARATION PSI to QinSourceStructure object bridging: "
                        + psiTree);

        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String objectSymbolsSource = Files.readString(objectSymbols);
        require(objectSymbolsSource.contains("QinPsiTree.sourceObjectDeclaration(")
                        && !objectSymbolsSource.contains("objectDeclarationAtKeywordOffset(startOffset)")
                        && !objectSymbolsSource.contains("objectDeclaration.getTextRange().getStartOffset()"),
                "QinObjectSymbols must ask QinPsiTree to bridge OBJECT_DECLARATION PSI "
                        + "to QinSourceStructure instead of owning offset lookup: " + objectSymbols);
    }

    private static void assertObjectNamePsiBridgeUsesQinPsiTree(Path javaRoot) throws Exception {
        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("objectNameElement(")
                        && psiTreeSource.contains("QinTokenTypes.OBJECT_NAME"),
                "QinPsiTree must own Qin object name source range to PSI name bridging: "
                        + psiTree);

        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String objectSymbolsSource = Files.readString(objectSymbols);
        require(objectSymbolsSource.contains("QinPsiTree.objectNameElement(")
                        && !objectSymbolsSource.contains("QinTokenTypes.OBJECT_NAME"),
                "QinObjectSymbols must ask QinPsiTree to bridge object name ranges to PSI names "
                        + "instead of owning the OBJECT_NAME token mapping: " + objectSymbols);
    }

    private static void assertObjectDeclarationAncestryUsesQinPsiTree(Path javaRoot) throws Exception {
        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("containingObjectDeclaration(")
                        && psiTreeSource.contains("QinTokenTypes.OBJECT_DECLARATION"),
                "QinPsiTree must own OBJECT_DECLARATION PSI ancestry lookup: "
                        + psiTree);

        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String objectSymbolsSource = Files.readString(objectSymbols);
        require(objectSymbolsSource.contains("QinPsiTree.containingObjectDeclaration(")
                        && !objectSymbolsSource.contains("QinTokenTypes.OBJECT_DECLARATION"),
                "QinObjectSymbols must ask QinPsiTree for containing object declarations "
                        + "instead of owning the OBJECT_DECLARATION token mapping: " + objectSymbols);
    }

    private static void assertObjectSymbolsUseSourceStructureMemberLookup(Path javaRoot) throws Exception {
        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String source = Files.readString(objectSymbols);
        require(source.contains(".memberDeclarationNamed(")
                        && source.contains(".memberDeclarations()")
                        && !source.contains("declaration.fields()")
                        && !source.contains("declaration.methods()")
                        && !source.contains("member.name().equals(memberName)")
                        && !source.contains("memberType == QinTokenTypes.FIELD_NAME"),
                "QinObjectSymbols must use QinSourceStructure member declaration lookup helpers "
                        + "and member declarations instead of matching, flattening, or deriving "
                        + "source-structure member kind from PSI token types: " + objectSymbols);
    }

    private static void assertObjectSymbolsUseSourceStructureMemberKind(Path javaRoot) throws Exception {
        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String source = Files.readString(objectSymbols);
        require(source.contains("QinSourceStructure.ObjectMemberKind")
                        && !source.contains("private enum MemberKind")
                        && !containsWholeMarker(source, "MemberKind")
                        && !source.contains("memberKind("),
                "QinObjectSymbols must use QinSourceStructure.ObjectMemberKind "
                        + "instead of defining or deriving a local member kind: " + objectSymbols);
    }

    private static void assertObjectMemberPsiBridgeUsesQinPsiTree(Path javaRoot) throws Exception {
        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("objectMemberNameElement(")
                        && psiTreeSource.contains("objectMemberNameType(")
                        && psiTreeSource.contains("QinTokenTypes.FIELD_NAME")
                        && psiTreeSource.contains("QinTokenTypes.METHOD_NAME"),
                "QinPsiTree must own Qin object member source range to PSI name token bridging: "
                        + psiTree);

        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String objectSymbolsSource = Files.readString(objectSymbols);
        require(objectSymbolsSource.contains("QinPsiTree.objectMemberNameElement(")
                        && !objectSymbolsSource.contains("tokenTypeForMemberKind(")
                        && !objectSymbolsSource.contains("QinTokenTypes.FIELD_NAME")
                        && !objectSymbolsSource.contains("QinTokenTypes.METHOD_NAME"),
                "QinObjectSymbols must ask QinPsiTree to bridge object member ranges to PSI names "
                        + "instead of owning member kind to token mapping: " + objectSymbols);
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

    private static void assertMemberStubIndexKeySelectionIsShared(Path javaRoot) throws Exception {
        Path memberStubIndexes = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectMemberStubIndexes.java"));
        require(Files.isRegularFile(memberStubIndexes),
                "Qin object member StubIndex helper source not found: " + memberStubIndexes);
        String helperSource = Files.readString(memberStubIndexes);
        require(helperSource.contains("QinSourceStructure.ObjectMemberKind.FIELD")
                        && helperSource.contains("QinObjectFieldNameStubIndex.KEY")
                        && helperSource.contains("QinObjectMethodNameStubIndex.KEY"),
                "QinObjectMemberStubIndexes must own member kind to StubIndexKey mapping: "
                        + memberStubIndexes);

        for (Path file : List.of(
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinFileElementType.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectSymbols.java")))) {
            require(Files.isRegularFile(file), "Qin member StubIndex consumer source not found: " + file);
            String source = Files.readString(file);
            require(source.contains("QinObjectMemberStubIndexes.keyFor(")
                            && !source.contains("QinObjectFieldNameStubIndex.KEY")
                            && !source.contains("QinObjectMethodNameStubIndex.KEY"),
                    "Qin member StubIndex consumers must use QinObjectMemberStubIndexes.keyFor "
                            + "instead of owning field/method index selection: " + file);
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
