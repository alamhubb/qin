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
        assertReferenceContributorRegistrationUsesSharedReferenceElements(javaRoot);
        assertReferenceContributorsUseSharedProviderWrapper(javaRoot);
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
        assertSymbolHighlightingUsesSharedHelper(javaRoot);
        assertUnresolvedReferenceAnnotationIsUnified(javaRoot);
        assertRenameUsesSharedPsiHelper(javaRoot);
        assertObjectMemberCompletionUsesSharedHelper(javaRoot);

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

    private static void assertReferenceContributorRegistrationUsesSharedReferenceElements(Path javaRoot) throws Exception {
        Path referenceElements = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinReferenceElements.java"));
        require(Files.isRegularFile(referenceElements),
                "QinReferenceElements source not found: " + referenceElements);
        String helperSource = Files.readString(referenceElements);
        require(helperSource.contains("registerReferenceProvider(")
                        && helperSource.contains("registerMemberReferenceProvider(")
                        && helperSource.contains("PlatformPatterns.psiElement(type)"),
                "QinReferenceElements must own reference contributor token pattern registration: "
                        + referenceElements);

        try (var files = Files.walk(javaRoot)) {
            for (Path file : files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith("ReferenceContributor.java"))
                    .toList()) {
                String source = Files.readString(file);
                require(source.contains("QinReferenceElements.register")
                                && !source.contains("PlatformPatterns.psiElement(QinTokenTypes."),
                        "Qin reference contributors must register through QinReferenceElements "
                                + "instead of owning reference token patterns: " + file);
            }
        }
    }

    private static void assertReferenceContributorsUseSharedProviderWrapper(Path javaRoot) throws Exception {
        Path referenceElements = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinReferenceElements.java"));
        require(Files.isRegularFile(referenceElements),
                "QinReferenceElements source not found: " + referenceElements);
        String helperSource = Files.readString(referenceElements);
        require(helperSource.contains("referenceProvider(")
                        && helperSource.contains("element.getContainingFile() instanceof QinPsiFile")
                        && helperSource.contains("referenceElement(element)")
                        && helperSource.contains("PsiReference.EMPTY_ARRAY"),
                "QinReferenceElements must own reference provider Qin-file filtering, "
                        + "reference-element bridging, and empty-reference fallback: " + referenceElements);

        try (var files = Files.walk(javaRoot)) {
            for (Path file : files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith("ReferenceContributor.java"))
                    .toList()) {
                String source = Files.readString(file);
                require(source.contains("QinReferenceElements.referenceProvider(")
                                && !source.contains("getContainingFile()")
                                && !source.contains("QinReferenceElements.referenceElement(")
                                && !source.contains("PsiReference.EMPTY_ARRAY"),
                        "Qin reference contributors must use QinReferenceElements.referenceProvider "
                                + "instead of owning Qin-file filtering or reference-element bridging: "
                                + file);
            }
        }
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

    private static void assertSymbolHighlightingUsesSharedHelper(Path javaRoot) throws Exception {
        Path symbolHighlights = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinSymbolHighlights.java"));
        require(Files.isRegularFile(symbolHighlights),
                "Qin symbol highlight helper source not found: " + symbolHighlights);
        String helperSource = Files.readString(symbolHighlights);
        require(helperSource.contains("declarationHighlight(")
                        && helperSource.contains("referenceHighlight(")
                        && helperSource.contains("DefaultLanguageHighlighterColors.CLASS_NAME")
                        && helperSource.contains("QinTokenTypes.OBJECT_NAME")
                        && helperSource.contains("QinObjectMethodReference")
                        && helperSource.contains("QinJavaReference"),
                "QinSymbolHighlights must own declaration/reference highlight facts: "
                        + symbolHighlights);

        Path annotator = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinSymbolHighlightAnnotator.java"));
        require(Files.isRegularFile(annotator),
                "Qin symbol highlight annotator source not found: " + annotator);
        String annotatorSource = Files.readString(annotator);
        require(annotatorSource.contains("QinSymbolHighlights.declarationHighlight(")
                        && annotatorSource.contains("QinSymbolHighlights.referenceHighlight(")
                        && !annotatorSource.contains("DefaultLanguageHighlighterColors")
                        && !annotatorSource.contains("QinTokenTypes.")
                        && !annotatorSource.contains("instanceof QinObject")
                        && !annotatorSource.contains("instanceof QinJavaReference")
                        && !annotatorSource.contains("instanceof QinImportAliasReference"),
                "QinSymbolHighlightAnnotator must consume QinSymbolHighlights instead of "
                        + "owning declaration token or reference-type highlight mappings: " + annotator);
    }

    private static void assertUnresolvedReferenceAnnotationIsUnified(Path javaRoot) throws Exception {
        Path annotator = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinUnresolvedReferenceAnnotator.java"));
        require(Files.isRegularFile(annotator),
                "Unified Qin unresolved-reference annotator source not found: " + annotator);
        String annotatorSource = Files.readString(annotator);
        require(annotatorSource.contains("QinUnresolvedReferenceMessages.messageFor(element)")
                        && annotatorSource.contains("HighlightSeverity.ERROR")
                        && !annotatorSource.contains("javaMessageFor(")
                        && !annotatorSource.contains("objectMethodMessageFor(")
                        && !annotatorSource.contains("objectFieldMessageFor("),
                "QinUnresolvedReferenceAnnotator must use the shared message helper instead of "
                        + "owning Java/object unresolved-reference branches: " + annotator);

        for (String removedAnnotator : List.of(
                "QinJavaInteropAnnotator.java",
                "QinObjectMethodAnnotator.java",
                "QinObjectFieldAnnotator.java")) {
            Path oldSource = javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", removedAnnotator));
            require(!Files.exists(oldSource),
                    "Qin unresolved-reference annotations must flow through "
                            + "QinUnresolvedReferenceAnnotator, not " + oldSource);
        }
    }

    private static void assertRenameUsesSharedPsiHelper(Path javaRoot) throws Exception {
        Path renameHelper = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiRenames.java"));
        require(Files.isRegularFile(renameHelper),
                "Qin PSI rename helper source not found: " + renameHelper);
        String helperSource = Files.readString(renameHelper);
        require(helperSource.contains("replaceLeafText(")
                        && helperSource.contains("LeafElement")
                        && helperSource.contains("replaceWithText("),
                "QinPsiRenames must own leaf-token rename replacement: " + renameHelper);

        for (Path file : List.of(
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinJavaReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectMethodReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectFieldReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinImportAliasReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinNamedPsiElement.java")))) {
            require(Files.isRegularFile(file), "Qin rename consumer source not found: " + file);
            String source = Files.readString(file);
            require(source.contains("QinPsiRenames.replaceLeafText(")
                            && !source.contains("LeafElement")
                            && !source.contains("replaceWithText("),
                    "Qin rename consumers must use QinPsiRenames instead of owning leaf "
                            + "replacement plumbing: " + file);
        }
    }

    private static void assertObjectMemberCompletionUsesSharedHelper(Path javaRoot) throws Exception {
        Path completionHelper = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectMemberCompletions.java"));
        require(Files.isRegularFile(completionHelper),
                "Qin object member completion helper source not found: " + completionHelper);
        String helperSource = Files.readString(completionHelper);
        require(helperSource.contains("memberElements(")
                        && helperSource.contains("QinReferenceElements.referenceElement(")
                        && helperSource.contains("QinJavaReference.isJavaReferenceCandidate(")
                        && helperSource.contains("QinObjectSymbols.memberElementsForThis(")
                        && helperSource.contains("QinObjectSymbols.memberElementsForObject("),
                "QinObjectMemberCompletions must own object-member completion context "
                        + "selection: " + completionHelper);

        Path contributor = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectMemberCompletionContributor.java"));
        require(Files.isRegularFile(contributor),
                "Qin object member completion contributor source not found: " + contributor);
        String contributorSource = Files.readString(contributor);
        require(contributorSource.contains("QinObjectMemberCompletions.memberElements(")
                        && !contributorSource.contains("QinJavaReference.isJavaReferenceCandidate(")
                        && !contributorSource.contains("QinReferenceElements.previousQualifierName(")
                        && !contributorSource.contains("QinObjectSymbols.memberElementsFor"),
                "QinObjectMemberCompletionContributor must consume QinObjectMemberCompletions "
                        + "instead of owning qualifier, Java-boundary, or member lookup decisions: "
                        + contributor);
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
