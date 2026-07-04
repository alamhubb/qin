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
        Path testJavaRoot = projectRoot.resolve("src").resolve("test").resolve("java").normalize();
        Path pluginXml = sourceRoot.resolve("resources").resolve("META-INF").resolve("plugin.xml").normalize();

        require(Files.isDirectory(javaRoot), "Java source root not found: " + javaRoot);
        require(Files.isDirectory(testJavaRoot), "Java test root not found: " + testJavaRoot);
        require(Files.isRegularFile(pluginXml), "plugin.xml not found: " + pluginXml);

        assertNoForbiddenPluginXmlMarkers(pluginXml);
        assertNoForbiddenSourceMarkers(javaRoot);
        assertNoDirectReferenceRegistryAccess(javaRoot);
        assertNoDirectJavaPsiAccess(javaRoot);
        assertLexerUsesSharedScannerAdapter(javaRoot);
        assertSyntaxHighlighterCoverageUsesTextAttributes(testJavaRoot);
        assertImportContextualKeywordCoverageUsesLexerTokens(testJavaRoot);
        assertReferenceLookupUsesPsiTreeBridge(javaRoot);
        assertReferencePlatformTestsUseSharedHelper(testJavaRoot);
        assertGoToDeclarationCoverageUsesEditorPath(testJavaRoot);
        assertFindUsagesAndRenameCoverageUsesPlatformPath(testJavaRoot);
        assertParserDefinitionUsesSourceRangePredicates(javaRoot);
        assertImportBoundaryUsesSharedTokenFacts(javaRoot);
        assertImportParsingUsesSharedContextualKeywords(javaRoot);
        assertQualifierLookupUsesSharedReferenceElements(javaRoot);
        assertCallBoundaryUsesSharedReferenceElements(javaRoot);
        assertReferenceTokenChecksUseSharedReferenceElements(javaRoot);
        assertReferenceContributorRegistrationUsesSharedReferenceElements(javaRoot);
        assertReferenceContributorsUseSharedProviderWrapper(javaRoot);
        assertObjectReferenceContributorsUseSharedJavaBoundary(javaRoot);
        assertImportBindingsUseSourceStructureSpecifierLookup(javaRoot);
        assertImportBindingsUseSourceStructureAliasLookup(javaRoot);
        assertImportNamePsiBridgeUsesQinPsiTree(javaRoot);
        assertObjectSymbolsUseSourceStructureDeclarationLookup(javaRoot);
        assertObjectDeclarationPsiBridgeUsesQinPsiTree(javaRoot);
        assertObjectNamePsiBridgeUsesQinPsiTree(javaRoot);
        assertObjectDeclarationAncestryUsesQinPsiTree(javaRoot);
        assertObjectSymbolsUseSourceStructureMemberLookup(javaRoot);
        assertObjectSymbolsUseSourceStructureMemberKind(javaRoot);
        assertObjectMemberPsiBridgeUsesQinPsiTree(javaRoot);
        assertNamedPsiElementMappingIsCentralized(javaRoot);
        assertStubIndexUsesSourceStructureMemberIndexEntries(javaRoot);
        assertMemberStubIndexKeySelectionIsShared(javaRoot);
        assertSymbolHighlightingUsesSharedHelper(javaRoot);
        assertUnresolvedReferenceAnnotationIsUnified(javaRoot);
        assertUnresolvedReferenceInspectionUsesSharedMessages(javaRoot);
        assertRenameUsesSharedPsiHelper(javaRoot);
        assertReferenceRenameDoesNotPreserveAliasLocals(javaRoot);
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

    private static void assertLexerUsesSharedScannerAdapter(Path javaRoot) throws Exception {
        Path lexer = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinLexer.java"));
        Path scanner = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinLexicalScanner.java"));
        require(Files.isRegularFile(lexer), "QinLexer source not found: " + lexer);
        require(Files.isRegularFile(scanner), "QinLexicalScanner source not found: " + scanner);

        String lexerSource = Files.readString(lexer);
        require(lexerSource.contains("extends LexerBase")
                        && lexerSource.contains("QinLexicalScanner.scan(buffer, startOffset, endOffset)")
                        && !lexerSource.contains("JavaScriptTokens")
                        && !lexerSource.contains("SubhutiLexer")
                        && !lexerSource.contains("QinParserFacade")
                        && !lexerSource.contains("parseProgram(")
                        && !lexerSource.contains("parseSource("),
                "QinLexer must stay an IntelliJ LexerBase wrapper over QinLexicalScanner, "
                        + "not a token-definition owner or whole-file parser caller: " + lexer);

        String scannerSource = Files.readString(scanner);
        require(scannerSource.contains("new SubhutiLexer(JavaScriptTokens.getTokens())")
                        && scannerSource.contains("TokenUtils.isKeyword(")
                        && !scannerSource.contains("QinParserFacade")
                        && !scannerSource.contains("parseProgram(")
                        && !scannerSource.contains("parseSource("),
                "QinLexicalScanner must own the transitional Slime/Subhuti token adapter "
                        + "without calling the whole-file Qin parser: " + scanner);
    }

    private static void assertReferenceLookupUsesPsiTreeBridge(Path javaRoot) throws Exception {
        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("elementAt(")
                        && psiTreeSource.contains("file.findElementAt(offset)"),
                "QinPsiTree must own raw offset to PSI leaf lookup: " + psiTree);

        Path psiReferences = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiReferences.java"));
        require(Files.isRegularFile(psiReferences),
                "QinPsiReferences source not found: " + psiReferences);
        String psiReferencesSource = Files.readString(psiReferences);
        require(psiReferencesSource.contains("QinPsiTree.elementAt(file, offset)")
                        && !psiReferencesSource.contains("file.findElementAt(offset)"),
                "QinPsiReferences.findReferenceAt must ask QinPsiTree for offset-to-leaf "
                        + "lookup instead of owning PsiFile.findElementAt: " + psiReferences);
    }

    private static void assertSyntaxHighlighterCoverageUsesTextAttributes(Path testJavaRoot) throws Exception {
        Path platformTest = testJavaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinLspCompletionPlatformTest.java"));
        require(Files.isRegularFile(platformTest),
                "Qin platform test source not found: " + platformTest);
        String source = Files.readString(platformTest);

        requireTestContainsAll(source, platformTest,
                "testQinSyntaxHighlighterMapsCoreTokensToIdeaTextAttributes",
                "new QinSyntaxHighlighter()",
                "assertTokenHighlight(",
                "QinTokenTypes.KEYWORD",
                "DefaultLanguageHighlighterColors.KEYWORD",
                "QinTokenTypes.IDENTIFIER",
                "DefaultLanguageHighlighterColors.IDENTIFIER",
                "QinTokenTypes.CLASS_NAME",
                "DefaultLanguageHighlighterColors.CLASS_NAME",
                "QinTokenTypes.FUNCTION_IDENTIFIER",
                "DefaultLanguageHighlighterColors.FUNCTION_CALL",
                "QinTokenTypes.MEMBER_IDENTIFIER",
                "DefaultLanguageHighlighterColors.INSTANCE_METHOD",
                "QinTokenTypes.STRING",
                "DefaultLanguageHighlighterColors.STRING",
                "QinTokenTypes.NUMBER",
                "DefaultLanguageHighlighterColors.NUMBER",
                "QinTokenTypes.LINE_COMMENT",
                "DefaultLanguageHighlighterColors.LINE_COMMENT",
                "QinTokenTypes.BLOCK_COMMENT",
                "DefaultLanguageHighlighterColors.BLOCK_COMMENT",
                "QinTokenTypes.BRACE",
                "DefaultLanguageHighlighterColors.BRACES",
                "QinTokenTypes.OPERATOR",
                "DefaultLanguageHighlighterColors.OPERATION_SIGN");
    }

    private static void assertImportContextualKeywordCoverageUsesLexerTokens(Path testJavaRoot) throws Exception {
        Path platformTest = testJavaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinLspCompletionPlatformTest.java"));
        require(Files.isRegularFile(platformTest),
                "Qin platform test source not found: " + platformTest);
        String source = Files.readString(platformTest);

        requireTestContainsAll(source, platformTest,
                "testQinLexerHighlightsImportContextualKeywordsOnlyInsideImports",
                "collectLexerTokenEntries(source)",
                "as:\" + QinTokenTypes.KEYWORD",
                "from:\" + QinTokenTypes.KEYWORD",
                "from:\" + QinTokenTypes.IDENTIFIER");
        requireTestContainsAll(source, platformTest,
                "testQinLexerStopsImportContextualKeywordHighlightAtNextLine",
                "collectLexerTokenEntries(source)",
                "import { Greeter }",
                "const as = \"local\"",
                "as:\" + QinTokenTypes.IDENTIFIER");
    }

    private static void assertReferencePlatformTestsUseSharedHelper(Path testJavaRoot) throws Exception {
        Path platformTest = testJavaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinLspCompletionPlatformTest.java"));
        require(Files.isRegularFile(platformTest),
                "Qin platform test source not found: " + platformTest);
        String source = Files.readString(platformTest);
        require(source.contains("QinPsiReferences.references(")
                        && !source.contains("ReferenceProvidersRegistry")
                        && !source.contains("getReferencesFromProviders"),
                "Qin platform tests must count registered references through QinPsiReferences "
                        + "instead of calling ReferenceProvidersRegistry directly: " + platformTest);
    }

    private static void assertGoToDeclarationCoverageUsesEditorPath(Path testJavaRoot) throws Exception {
        Path platformTest = testJavaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinLspCompletionPlatformTest.java"));
        require(Files.isRegularFile(platformTest),
                "Qin platform test source not found: " + platformTest);
        String source = Files.readString(platformTest);
        for (String testName : List.of(
                "testQinObjectMethodGoToDeclarationTargetsSameFileMethodName",
                "testQinObjectMethodGoToDeclarationTargetsImportedMethodName",
                "testQinThisMethodGoToDeclarationTargetsCurrentObjectMethodName",
                "testQinObjectFieldGoToDeclarationTargetsSameFileFieldName",
                "testQinObjectFieldGoToDeclarationTargetsImportedFieldName",
                "testQinObjectFieldGoToDeclarationTargetsAliasedImportedFieldName",
                "testQinThisFieldGoToDeclarationTargetsCurrentObjectFieldName",
                "testQinJavaMemberGoToDeclarationTargetsPsiMethod",
                "testQinJavaAliasedMemberGoToDeclarationTargetsPsiMethod",
                "testQinJavaAliasedImportSpecifierGoToDeclarationTargetsPsiClass",
                "testQinJavaImportAliasUsageGoToDeclarationTargetsAliasName",
                "testQinJavaFieldGoToDeclarationTargetsPsiField",
                "testQinJavaAliasedFieldGoToDeclarationTargetsPsiField")) {
            requireTestUsesEditorPath(source, testName, platformTest);
        }
    }

    private static void requireTestUsesEditorPath(
            String source,
            String testName,
            Path platformTest) {
        String body = testBody(source, testName, platformTest);
        require(body.contains("myFixture.getElementAtCaret()"),
                "Qin Go To Declaration test " + testName
                        + " must exercise the IDEA editor path with myFixture.getElementAtCaret(), "
                        + "not only findReferenceAt().resolve(): " + platformTest);
    }

    private static void assertFindUsagesAndRenameCoverageUsesPlatformPath(Path testJavaRoot) throws Exception {
        Path platformTest = testJavaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinLspCompletionPlatformTest.java"));
        require(Files.isRegularFile(platformTest),
                "Qin platform test source not found: " + platformTest);
        String source = Files.readString(platformTest);

        requireTestContainsAll(source, platformTest,
                "testQinObjectMethodReferenceParticipatesInReferencesSearch",
                "ReferencesSearch.search(",
                "this.next()",
                "Counter.next()");
        requireTestContainsAll(source, platformTest,
                "testQinObjectFieldReferenceParticipatesInReferencesSearch",
                "ReferencesSearch.search(",
                "this.value",
                "Counter.value");
        requireTestContainsAll(source, platformTest,
                "testQinObjectMethodRenameProcessorUpdatesReferences",
                "new RenameProcessor(",
                "this.advance()",
                "Counter.advance()");
        requireTestContainsAll(source, platformTest,
                "testQinObjectFieldRenameProcessorUpdatesReferences",
                "new RenameProcessor(",
                "this.total",
                "Counter.total");
        requireTestContainsAll(source, platformTest,
                "testQinObjectMethodRenameProcessorPreservesImportAliasQualifier",
                "new RenameProcessor(",
                "C.advance()",
                "import { Counter as C }",
                "Counter.advance()");
        requireTestContainsAll(source, platformTest,
                "testQinObjectFieldRenameProcessorPreservesImportAliasQualifier",
                "new RenameProcessor(",
                "C.total",
                "import { Counter as C }",
                "Counter.total");
        requireTestContainsAll(source, platformTest,
                "testQinJavaClassReferenceParticipatesInReferencesSearch",
                "ReferencesSearch.search(",
                "Greeter.greet(\"Qin\")");
        requireTestContainsAll(source, platformTest,
                "testQinJavaClassReferencesSearchIncludesAliasedExportedImportName",
                "ReferencesSearch.search(",
                "import { Greeter as G }",
                "G.greet(\"Qin\")",
                "assertReferencesMissingQinElement(");
        requireTestContainsAll(source, platformTest,
                "testQinJavaClassRenameProcessorPreservesImportAliasUsages",
                "new RenameProcessor(",
                "import { Welcomer as G }",
                "G.greet(\"Qin\")");
        requireTestContainsAll(source, platformTest,
                "testQinJavaMethodReferenceParticipatesInReferencesSearch",
                "ReferencesSearch.search(",
                "Greeter.greet(\"Qin\")");
        requireTestContainsAll(source, platformTest,
                "testQinJavaAliasedMethodReferenceParticipatesInReferencesSearch",
                "ReferencesSearch.search(",
                "G.greet(\"Qin\")");
        requireTestContainsAll(source, platformTest,
                "testQinJavaMethodRenameProcessorUpdatesReferences",
                "new RenameProcessor(",
                "Greeter.welcome");
        requireTestContainsAll(source, platformTest,
                "testQinJavaAliasedMethodRenameProcessorPreservesAliasQualifier",
                "new RenameProcessor(",
                "G.welcome",
                "Greeter.welcome");
        requireTestContainsAll(source, platformTest,
                "testQinJavaFieldReferenceParticipatesInReferencesSearch",
                "ReferencesSearch.search(",
                "Greeter.DEFAULT_NAME");
        requireTestContainsAll(source, platformTest,
                "testQinJavaAliasedFieldReferenceParticipatesInReferencesSearch",
                "ReferencesSearch.search(",
                "G.DEFAULT_NAME");
        requireTestContainsAll(source, platformTest,
                "testQinJavaFieldRenameProcessorUpdatesReferences",
                "new RenameProcessor(",
                "Greeter.FALLBACK_NAME");
        requireTestContainsAll(source, platformTest,
                "testQinJavaAliasedFieldRenameProcessorPreservesAliasQualifier",
                "new RenameProcessor(",
                "G.FALLBACK_NAME",
                "Greeter.FALLBACK_NAME");
    }

    private static void requireTestContainsAll(
            String source,
            Path platformTest,
            String testName,
            String... needles) {
        String body = testBody(source, testName, platformTest);
        for (String needle : needles) {
            require(body.contains(needle),
                    "Qin platform test " + testName + " must keep coverage marker `"
                            + needle + "` in " + platformTest);
        }
    }

    private static String testBody(String source, String testName, Path platformTest) {
        String signature = "public void " + testName + "(";
        int start = source.indexOf(signature);
        require(start >= 0, "Missing Qin platform coverage test " + testName + " in " + platformTest);
        int nextTest = source.indexOf("\n    public void ", start + signature.length());
        return nextTest >= 0 ? source.substring(start, nextTest) : source.substring(start);
    }

    private static void assertParserDefinitionUsesSourceRangePredicates(Path javaRoot) throws Exception {
        Path parserDefinition = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinParserDefinition.java"));
        require(Files.isRegularFile(parserDefinition),
                "QinParserDefinition source not found: " + parserDefinition);
        String source = Files.readString(parserDefinition);
        require(!source.contains("bodyRange().startOffset()")
                        && !source.contains("bodyRange().endOffset()")
                        && source.contains("memberDeclarationAtNameOffset(builder.getCurrentOffset(), kind)")
                        && source.contains("QinSourceStructure.ObjectMemberKind.METHOD")
                        && source.contains("QinSourceStructure.ObjectMemberKind.FIELD")
                        && !source.contains("sourceStructure.methodDeclarationAtNameOffset(")
                        && !source.contains("sourceStructure.fieldDeclarationAtNameOffset("),
                "QinParserDefinition must consume QinSourceStructure.SourceRange predicates "
                        + "and ObjectMemberKind member lookup instead of directly splitting "
                        + "body ranges or field/method declaration lookups: " + parserDefinition);
    }

    private static void assertImportBoundaryUsesSharedTokenFacts(Path javaRoot) throws Exception {
        Path tokenFacts = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinTokenFacts.java"));
        require(Files.isRegularFile(tokenFacts), "QinTokenFacts source not found: " + tokenFacts);
        String tokenFactsSource = Files.readString(tokenFacts);
        require(tokenFactsSource.contains("isNewStatementAfterImport(")
                        && tokenFactsSource.contains("hasLineTerminatorBetween("),
                "QinTokenFacts must own import new-statement boundary detection: " + tokenFacts);

        for (Path file : List.of(
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinLexicalScanner.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinSourceStructure.java")))) {
            require(Files.isRegularFile(file), "Qin import boundary consumer source not found: " + file);
            String source = Files.readString(file);
            require(source.contains("QinTokenFacts.isNewStatementAfterImport(")
                            && !source.contains("hasLineTerminatorBetween("),
                    "Qin lexer and source-structure import scanning must share "
                            + "QinTokenFacts.isNewStatementAfterImport instead of owning "
                            + "separate newline-boundary logic: " + file);
        }
    }

    private static void assertImportParsingUsesSharedContextualKeywords(Path javaRoot) throws Exception {
        Path parserDefinition = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinParserDefinition.java"));
        Path sourceStructure = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinSourceStructure.java"));
        require(Files.isRegularFile(parserDefinition),
                "QinParserDefinition source not found: " + parserDefinition);
        require(Files.isRegularFile(sourceStructure),
                "QinSourceStructure source not found: " + sourceStructure);

        String parserSource = Files.readString(parserDefinition);
        require(parserSource.contains("importDeclaration.declarationRange().containsOffset(builder.getCurrentOffset())"),
                "QinParserDefinition import PSI must stop at QinSourceStructure.ImportDeclaration ranges: "
                        + parserDefinition);
        require(parserSource.contains("QinTokenFacts.isContextualKeyword(builder, \"as\")")
                        && !parserSource.contains("QinTokenFacts.isKeyword(builder, \"as\")")
                        && !parserSource.contains("QinTokenFacts.isKeyword(builder, \"from\")"),
                "QinParserDefinition import parsing must use shared contextual keyword facts "
                        + "instead of keyword-only import rules: " + parserDefinition);

        String source = Files.readString(sourceStructure);
        require(source.contains("QinTokenFacts.isContextualKeyword(content, token, \"from\")")
                        && source.contains("QinTokenFacts.isContextualKeyword(content, tokens.get(next), \"as\")"),
                "QinSourceStructure import parsing must use shared contextual keyword facts "
                        + "for from/as: " + sourceStructure);
    }

    private static void assertQualifierLookupUsesSharedReferenceElements(Path javaRoot) throws Exception {
        Path referenceElements = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinReferenceElements.java"));
        require(Files.isRegularFile(referenceElements),
                "QinReferenceElements source not found: " + referenceElements);
        String helperSource = Files.readString(referenceElements);
        require(helperSource.contains("previousQualifierName(")
                        && helperSource.contains("QinTokenTypes.MEMBER_ACCESS")
                        && helperSource.contains("QinPsiTokenStream.previousQualifierName(")
                        && helperSource.contains("isThisQualifier("),
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

        for (Path file : List.of(
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectMethodReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectFieldReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectMemberCompletions.java")))) {
            require(Files.isRegularFile(file), "Qin qualifier consumer source not found: " + file);
            String source = Files.readString(file);
            require(source.contains("QinReferenceElements.isThisQualifier(")
                            && !source.contains("\"this\".equals("),
                    "Qin qualifier consumers must use QinReferenceElements.isThisQualifier "
                            + "instead of direct string checks: " + file);
        }
    }

    private static void assertCallBoundaryUsesSharedReferenceElements(Path javaRoot) throws Exception {
        Path referenceElements = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinReferenceElements.java"));
        require(Files.isRegularFile(referenceElements),
                "QinReferenceElements source not found: " + referenceElements);
        String helperSource = Files.readString(referenceElements);
        require(helperSource.contains("isFollowedByCallParenthesis(")
                        && helperSource.contains("QinPsiTokenStream.isFollowedByCallParenthesis("),
                "QinReferenceElements must own shared call-vs-field reference lookup: "
                        + referenceElements);

        for (Path file : List.of(
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectMethodReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectFieldReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinUnresolvedReferenceMessages.java")))) {
            require(Files.isRegularFile(file), "Qin reference consumer source not found: " + file);
            String source = Files.readString(file);
            require(source.contains("QinReferenceElements.isFollowedByCallParenthesis(")
                            && !source.contains("QinPsiTokenStream.isFollowedByCallParenthesis("),
                    "Qin reference consumers must use QinReferenceElements for call-vs-field "
                            + "member boundaries instead of querying QinPsiTokenStream directly: " + file);
        }
    }

    private static void assertReferenceTokenChecksUseSharedReferenceElements(Path javaRoot) throws Exception {
        Path referenceElements = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinReferenceElements.java"));
        require(Files.isRegularFile(referenceElements),
                "QinReferenceElements source not found: " + referenceElements);
        String helperSource = Files.readString(referenceElements);
        require(helperSource.contains("isReferenceIdentifier(")
                        && helperSource.contains("referenceName(")
                        && helperSource.contains("referenceRange(")
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
                        && unresolvedMessagesSource.contains("QinReferenceElements.referenceName(")
                        && !unresolvedMessagesSource.contains("element.getText()")
                        && !unresolvedMessagesSource.contains("+ element.getText()")
                        && !unresolvedMessagesSource.contains("QinTokenTypes.REFERENCE_IDENTIFIER"),
                "QinUnresolvedReferenceMessages must use QinReferenceElements for reference "
                        + "identifier checks instead of owning the token mapping: " + unresolvedMessages);

        for (Path file : List.of(
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinJavaReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectMethodReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectFieldReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinImportAliasReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinImportBindings.java")))) {
            require(Files.isRegularFile(file), "Qin reference name consumer source not found: " + file);
            String source = Files.readString(file);
            require(source.contains("QinReferenceElements.referenceName(")
                            && !source.contains("element.getText()"),
                    "Qin reference name consumers must use QinReferenceElements.referenceName "
                            + "instead of reading raw PSI text from reference elements: " + file);
            if (!file.getFileName().toString().equals("QinImportBindings.java")) {
                require(source.contains("QinReferenceElements.referenceRange(")
                                && !source.contains("TextRange.from(0, element.getTextLength())"),
                        "Qin references must use QinReferenceElements.referenceRange instead of owning "
                                + "the full-reference token range: " + file);
            }
        }

        Path importAliasReference = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinImportAliasReference.java"));
        require(Files.isRegularFile(importAliasReference),
                "QinImportAliasReference source not found: " + importAliasReference);
        String importAliasReferenceSource = Files.readString(importAliasReference);
        require(importAliasReferenceSource.contains("QinReferenceElements.isImportAliasDeclaration(")
                        && !importAliasReferenceSource.contains("QinTokenTypes.IMPORT_ALIAS_NAME"),
                "QinImportAliasReference must use QinReferenceElements for import alias "
                        + "declaration checks instead of owning the token mapping: " + importAliasReference);

        Path psiTokenStream = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTokenStream.java"));
        require(Files.isRegularFile(psiTokenStream),
                "Qin PSI token stream source not found: " + psiTokenStream);
        String psiTokenStreamSource = Files.readString(psiTokenStream);
        require(psiTokenStreamSource.contains("QinReferenceElements.referenceElement(element)")
                        && !psiTokenStreamSource.contains("QinTokenTypes.REFERENCE_IDENTIFIER"),
                "QinPsiTokenStream must use QinReferenceElements for reference-wrapper token ownership "
                        + "instead of owning the REFERENCE_IDENTIFIER token mapping: " + psiTokenStream);
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
                require((source.contains("QinReferenceElements.referenceProvider(")
                                || source.contains("QinReferenceElements.objectReferenceProvider("))
                                && !source.contains("getContainingFile()")
                                && !source.contains("QinReferenceElements.referenceElement(")
                                && !source.contains("PsiReference.EMPTY_ARRAY"),
                        "Qin reference contributors must use QinReferenceElements.referenceProvider "
                                + "instead of owning Qin-file filtering or reference-element bridging: "
                                + file);
            }
        }
    }

    private static void assertObjectReferenceContributorsUseSharedJavaBoundary(Path javaRoot) throws Exception {
        Path referenceElements = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinReferenceElements.java"));
        require(Files.isRegularFile(referenceElements),
                "QinReferenceElements source not found: " + referenceElements);
        String helperSource = Files.readString(referenceElements);
        require(helperSource.contains("objectReferenceProvider(")
                        && helperSource.contains("QinJavaReference.isJavaReferenceCandidate(")
                        && helperSource.contains("candidate.test(referenceElement)"),
                "QinReferenceElements must own the Java interop exclusion for Qin object "
                        + "reference providers: " + referenceElements);

        for (Path file : List.of(
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectReferenceContributor.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectMethodReferenceContributor.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectFieldReferenceContributor.java")))) {
            require(Files.isRegularFile(file), "Qin object reference contributor source not found: " + file);
            String source = Files.readString(file);
            require(source.contains("QinReferenceElements.objectReferenceProvider(")
                            && !source.contains("QinJavaReference.isJavaReferenceCandidate("),
                    "Qin object reference contributors must use QinReferenceElements.objectReferenceProvider "
                            + "instead of owning the Java interop exclusion: " + file);
        }
    }

    private static void assertImportBindingsUseSourceStructureSpecifierLookup(Path javaRoot) throws Exception {
        Path importBindings = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinImportBindings.java"));
        require(Files.isRegularFile(importBindings),
                "QinImportBindings source not found: " + importBindings);
        String source = Files.readString(importBindings);
        require(source.contains("QinPsiTree.importSpecifierMatchAtNameElement(")
                        && source.contains(".importSpecifierMatches()")
                        && source.contains("QinPsiTree.sourceStructure(")
                        && !source.contains("QinSourceStructure.parse(file.getText())")
                        && !source.contains("getTextRange().getStartOffset()")
                        && !source.contains(".importSpecifierAtNameOffset(")
                        && !source.contains("sourceStructure.importDeclarations()")
                        && !source.contains(".specifiers()")
                        && !source.contains(".specifierAtNameOffset(offset)")
                        && !source.contains("exportedNameRange().startsAt")
                        && !source.contains("localNameRange().startsAt"),
                "QinImportBindings must use QinPsiTree.importSpecifierMatchAtNameElement "
                        + "and QinSourceStructure.importSpecifierMatches instead of owning import-name "
                        + "offset lookup, iterating declarations, or splitting named import ranges: "
                        + importBindings);

        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("importSpecifierMatchAtNameElement(")
                        && psiTreeSource.contains("element.getTextRange().getStartOffset()")
                        && psiTreeSource.contains(".importSpecifierAtNameOffset(offset)"),
                "QinPsiTree must own import-name PSI element to source-structure offset lookup: "
                        + psiTree);
    }

    private static void assertImportBindingsUseSourceStructureAliasLookup(Path javaRoot) throws Exception {
        Path importBindings = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinImportBindings.java"));
        require(Files.isRegularFile(importBindings),
                "QinImportBindings source not found: " + importBindings);
        String source = Files.readString(importBindings);
        require(source.contains("QinPsiTree.importAliasSpecifierNamed(")
                        && !source.contains("QinSourceStructure sourceStructure")
                        && !source.contains("sourceStructure.importAliasSpecifierNamed(")
                        && !source.contains("specifier.localName().equals(localName)")
                        && !source.contains("specifier.localNameRange().isPresent()"),
                "QinImportBindings must use QinPsiTree import alias lookup "
                        + "instead of owning source-structure alias lookup or matching alias names itself: "
                        + importBindings);

        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("importAliasSpecifierNamed(")
                        && psiTreeSource.contains(".importAliasSpecifierNamed(localName)"),
                "QinPsiTree must own import alias local-name to source-structure lookup: "
                        + psiTree);
    }

    private static void assertImportNamePsiBridgeUsesQinPsiTree(Path javaRoot) throws Exception {
        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("importAliasNameElement(")
                        && psiTreeSource.contains("QinTokenTypes.IMPORT_ALIAS_NAME")
                        && psiTreeSource.contains("importExportedNameElement(")
                        && psiTreeSource.contains("QinTokenTypes.REFERENCE_IDENTIFIER"),
                "QinPsiTree must own import exported-name and alias source range to PSI name bridging: "
                        + psiTree);

        Path importBindings = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinImportBindings.java"));
        require(Files.isRegularFile(importBindings),
                "QinImportBindings source not found: " + importBindings);
        String importBindingsSource = Files.readString(importBindings);
        require(importBindingsSource.contains("QinPsiTree.importAliasNameElement(")
                        && importBindingsSource.contains("QinPsiTree.importExportedNameElement(")
                        && !importBindingsSource.contains("QinTokenTypes.IMPORT_ALIAS_NAME"),
                "QinImportBindings must ask QinPsiTree to bridge import exported-name and alias ranges "
                        + "instead of owning import name token mappings: " + importBindings);
    }

    private static void assertObjectSymbolsUseSourceStructureDeclarationLookup(Path javaRoot) throws Exception {
        Path objectNameStubIndex = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectNameStubIndex.java"));
        require(Files.isRegularFile(objectNameStubIndex),
                "Qin object name StubIndex source not found: " + objectNameStubIndex);
        String objectNameStubIndexSource = Files.readString(objectNameStubIndex);
        require(objectNameStubIndexSource.contains("contains(")
                        && objectNameStubIndexSource.contains("StubIndex.getElements(")
                        && objectNameStubIndexSource.contains("GlobalSearchScope.fileScope("),
                "QinObjectNameStubIndex must own object-name StubIndex membership lookup: "
                        + objectNameStubIndex);

        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("psiFile(")
                        && psiTreeSource.contains("PsiManager.getInstance(project).findFile(file)"),
                "QinPsiTree must own VirtualFile to PsiFile lookup: " + psiTree);

        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String source = Files.readString(objectSymbols);
        Path moduleImportTable = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinModuleImportTable.java"));
        require(Files.isRegularFile(moduleImportTable),
                "QinModuleImportTable source not found: " + moduleImportTable);
        String moduleImportTableSource = Files.readString(moduleImportTable);
        Path objectReference = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectReference.java"));
        require(Files.isRegularFile(objectReference),
                "QinObjectReference source not found: " + objectReference);
        String objectReferenceSource = Files.readString(objectReference);
        require(source.contains("QinObjectNameStubIndex.contains(")
                        && source.contains("QinPsiTree.psiFile(")
                        && moduleImportTableSource.contains("Map<String, QinImportBindings.ImportBinding>")
                        && moduleImportTableSource.contains("resolveFile(@NotNull QinImportBindings.ImportBinding binding)")
                        && source.contains("importTable.resolveFile(importBinding)")
                        && !source.contains("new QinModuleImportTable.QinImport")
                        && !source.contains("QinModuleImportTable.QinImport")
                        && !objectReferenceSource.contains("QinModuleImportTable.QinImport")
                        && !moduleImportTableSource.contains("record QinImport")
                        && !source.contains("PsiManager.getInstance(")
                        && !source.contains(".findFile(importedFile)")
                        && !source.contains("StubIndex.getElements(")
                        && !source.contains("QinObjectNameStubIndex.KEY")
                        && !source.contains("GlobalSearchScope.fileScope(")
                        && !source.contains(".objectDeclarations()")
                        && !source.contains("keywordRange().startsAt"),
                "QinObjectSymbols must use QinObjectNameStubIndex and QinSourceStructure "
                        + "object declaration lookup helpers instead of owning StubIndex lookup, "
                        + "VirtualFile-to-PsiFile lookup, Qin module import construction, "
                        + "iterating declarations, or matching keyword ranges: " + objectSymbols);
    }

    private static void assertObjectDeclarationPsiBridgeUsesQinPsiTree(Path javaRoot) throws Exception {
        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("sourceObjectDeclaration(")
                        && psiTreeSource.contains("sourceStructure(")
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
                        && psiTreeSource.contains("sourceObjectDeclarationNamed(")
                        && psiTreeSource.contains(".objectDeclarationNamed(name)")
                        && psiTreeSource.contains("QinTokenTypes.OBJECT_NAME"),
                "QinPsiTree must own Qin object name source lookup and range to PSI name bridging: "
                        + psiTree);

        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String objectSymbolsSource = Files.readString(objectSymbols);
        require(objectSymbolsSource.contains("QinPsiTree.objectNameElement(")
                        && objectSymbolsSource.contains("QinPsiTree.sourceObjectDeclarationNamed(")
                        && !objectSymbolsSource.contains("QinSourceStructure.parse(file.getText())")
                        && !objectSymbolsSource.contains("sourceStructure(file).objectDeclarationNamed(")
                        && !objectSymbolsSource.contains("QinPsiTree.sourceStructure(file).objectDeclarationNamed(")
                        && !objectSymbolsSource.contains("QinTokenTypes.OBJECT_NAME"),
                "QinObjectSymbols must ask QinPsiTree to bridge object name source lookup "
                        + "and ranges to PSI names instead of owning those platform adapter details: "
                        + objectSymbols);
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
        Path sourceStructure = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinSourceStructure.java"));
        require(Files.isRegularFile(sourceStructure),
                "QinSourceStructure source not found: " + sourceStructure);
        String sourceStructureSource = Files.readString(sourceStructure);
        require(sourceStructureSource.contains(
                        "memberDeclarationNamed(@NotNull String name, @NotNull ObjectMemberKind kind)")
                        && sourceStructureSource.contains(
                        "return memberDeclarationNamed(memberDeclarations(kind), name);")
                        && !sourceStructureSource.contains("MemberDeclaration fieldDeclarationNamed(")
                        && !sourceStructureSource.contains("MemberDeclaration methodDeclarationNamed(")
                        && !sourceStructureSource.contains("MemberDeclaration fieldDeclarationAtNameOffset(")
                        && !sourceStructureSource.contains("MemberDeclaration methodDeclarationAtNameOffset("),
                "QinSourceStructure object member lookup must stay behind ObjectMemberKind-aware "
                        + "helpers instead of exposing separate field/method declaration lookups: "
                        + sourceStructure);

        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "QinObjectSymbols source not found: " + objectSymbols);
        String source = Files.readString(objectSymbols);
        require(source.contains("QinPsiTree.sourceObjectMemberDeclarationNamed(")
                        && source.contains("QinPsiTree.sourceObjectMemberDeclarations(")
                        && source.contains("member.declaration().name()")
                        && source.contains("member.name()")
                        && !source.contains(".memberDeclarationNamed(")
                        && !source.contains(".memberDeclarations()")
                        && !source.contains("declaration.fields()")
                        && !source.contains("declaration.methods()")
                        && !source.contains("member.element().getText()")
                        && !source.contains("memberNamesForObject(")
                        && !source.contains("memberNamesForThis(")
                        && !source.contains("member.name().equals(memberName)")
                        && !source.contains("memberType == QinTokenTypes.FIELD_NAME"),
                "QinObjectSymbols must use QinPsiTree member declaration lookup helpers "
                        + "and member declarations instead of matching, flattening, owning "
                        + "source-structure member collection, or deriving member kind from PSI token types: "
                        + objectSymbols);

        Path psiTree = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinPsiTree.java"));
        require(Files.isRegularFile(psiTree), "QinPsiTree source not found: " + psiTree);
        String psiTreeSource = Files.readString(psiTree);
        require(psiTreeSource.contains("sourceObjectMemberDeclarationNamed(")
                        && psiTreeSource.contains(".memberDeclarationNamed(memberName, kind)")
                        && psiTreeSource.contains("sourceObjectMemberDeclarations(")
                        && psiTreeSource.contains(".memberDeclarations()"),
                "QinPsiTree must own object source member lookup and source-order member collection: "
                        + psiTree);

        Path platformTest = javaRoot.getParent().getParent().resolve("test").resolve("java").resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinLspCompletionPlatformTest.java"));
        require(Files.isRegularFile(platformTest),
                "Qin platform test source not found: " + platformTest);
        String testSource = Files.readString(platformTest);
        requireTestContainsAll(testSource, platformTest,
                "testQinSourceStructurePreservesObjectMemberSourceOrder",
                "QinSourceStructure.ObjectMemberKind.METHOD",
                "QinSourceStructure.ObjectMemberKind.FIELD",
                "nameRange().startOffset()");
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

    private static void assertNamedPsiElementMappingIsCentralized(Path javaRoot) throws Exception {
        Path namedElement = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinNamedPsiElement.java"));
        require(Files.isRegularFile(namedElement),
                "Qin named PSI element source not found: " + namedElement);
        String namedElementSource = Files.readString(namedElement);
        require(namedElementSource.contains("static @Nullable QinNamedPsiElement create(")
                        && namedElementSource.contains("QinTokenTypes.OBJECT_NAME")
                        && namedElementSource.contains("QinTokenTypes.METHOD_NAME")
                        && namedElementSource.contains("QinTokenTypes.FIELD_NAME")
                        && namedElementSource.contains("QinTokenTypes.IMPORT_ALIAS_NAME"),
                "QinNamedPsiElement must own named PSI token to element-class mapping: "
                        + namedElement);

        Path parserDefinition = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinParserDefinition.java"));
        require(Files.isRegularFile(parserDefinition),
                "QinParserDefinition source not found: " + parserDefinition);
        String parserSource = Files.readString(parserDefinition);
        String createElementSource = parserSource.substring(parserSource.indexOf("createElement("));
        require(createElementSource.contains("QinNamedPsiElement.create(node)")
                        && !createElementSource.contains("new QinObjectNamePsiElement(")
                        && !createElementSource.contains("new QinMethodNamePsiElement(")
                        && !createElementSource.contains("new QinFieldNamePsiElement(")
                        && !createElementSource.contains("new QinImportAliasNamePsiElement("),
                "QinParserDefinition.createElement must delegate named PSI token mapping to "
                        + "QinNamedPsiElement instead of owning declaration-token branches: "
                        + parserDefinition);
    }

    private static void assertStubIndexUsesSourceStructureMemberIndexEntries(Path javaRoot) throws Exception {
        Path sourceStructure = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinSourceStructure.java"));
        require(Files.isRegularFile(sourceStructure),
                "QinSourceStructure source not found: " + sourceStructure);
        String sourceStructureSource = Files.readString(sourceStructure);
        require(sourceStructureSource.contains("memberNames(@NotNull ObjectMemberKind kind)")
                        && sourceStructureSource.contains("memberDeclarations(@NotNull ObjectMemberKind kind)")
                        && sourceStructureSource.contains("@NotNull List<ObjectMemberDeclaration> members")
                        && sourceStructureSource.contains("members = List.copyOf(members)")
                        && sourceStructureSource.contains("return members;")
                        && !sourceStructureSource.contains("@NotNull List<MemberDeclaration> fields")
                        && !sourceStructureSource.contains("@NotNull List<MemberDeclaration> methods")
                        && !sourceStructureSource.contains("fields = List.copyOf(fields)")
                        && !sourceStructureSource.contains("methods = List.copyOf(methods)")
                        && !sourceStructureSource.contains("addMemberDeclarations(")
                        && !sourceStructureSource.contains("fieldNames()")
                        && !sourceStructureSource.contains("methodNames()"),
                "QinSourceStructure must own object member name lookup and source-order "
                        + "member collection by ObjectMemberKind: "
                        + sourceStructure);

        Path fileElementType = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinFileElementType.java"));
        require(Files.isRegularFile(fileElementType),
                "QinFileElementType source not found: " + fileElementType);
        String source = Files.readString(fileElementType);
        require(source.contains(".memberIndexEntries()")
                        && source.contains(".memberNames(QinSourceStructure.ObjectMemberKind.FIELD)")
                        && source.contains(".memberNames(QinSourceStructure.ObjectMemberKind.METHOD)")
                        && !source.contains("declaration.fieldNames()")
                        && !source.contains("declaration.methodNames()")
                        && !source.contains("for (String field : declaration.fieldNames())")
                        && !source.contains("for (String method : declaration.methodNames())")
                        && !source.contains("static @NotNull String memberKey("),
                "QinFileElementType must consume QinSourceStructure member names and index entries "
                        + "through ObjectMemberKind instead of flattening members or owning "
                        + "object-member key syntax: " + fileElementType);
    }

    private static void assertMemberStubIndexKeySelectionIsShared(Path javaRoot) throws Exception {
        Path memberStubIndexes = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectMemberStubIndexes.java"));
        require(Files.isRegularFile(memberStubIndexes),
                "Qin object member StubIndex helper source not found: " + memberStubIndexes);
        String helperSource = Files.readString(memberStubIndexes);
        require(helperSource.contains("QinSourceStructure.ObjectMemberKind.FIELD")
                        && helperSource.contains("QinObjectFieldNameStubIndex.KEY")
                        && helperSource.contains("QinObjectMethodNameStubIndex.KEY")
                        && helperSource.contains("contains(")
                        && helperSource.contains("QinSourceStructure.objectMemberKey(")
                        && helperSource.contains("StubIndex.getElements("),
                "QinObjectMemberStubIndexes must own member kind to StubIndexKey mapping "
                        + "and object-qualified member lookup: "
                        + memberStubIndexes);

        Path fileElementType = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinFileElementType.java"));
        require(Files.isRegularFile(fileElementType),
                "Qin member StubIndex producer source not found: " + fileElementType);
        String fileElementTypeSource = Files.readString(fileElementType);
        require(fileElementTypeSource.contains("QinObjectMemberStubIndexes.keyFor(")
                        && !fileElementTypeSource.contains("QinObjectFieldNameStubIndex.KEY")
                        && !fileElementTypeSource.contains("QinObjectMethodNameStubIndex.KEY"),
                "Qin file stub indexing must use QinObjectMemberStubIndexes.keyFor "
                        + "instead of owning field/method index selection: " + fileElementType);

        Path objectSymbols = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectSymbols.java"));
        require(Files.isRegularFile(objectSymbols),
                "Qin member StubIndex consumer source not found: " + objectSymbols);
        String objectSymbolsSource = Files.readString(objectSymbols);
        require(objectSymbolsSource.contains("QinObjectMemberStubIndexes.contains(")
                        && !objectSymbolsSource.contains("QinObjectMemberStubIndexes.keyFor(")
                        && !objectSymbolsSource.contains("QinSourceStructure.objectMemberKey(")
                        && !objectSymbolsSource.contains("QinObjectFieldNameStubIndex.KEY")
                        && !objectSymbolsSource.contains("QinObjectMethodNameStubIndex.KEY"),
                "QinObjectSymbols must ask QinObjectMemberStubIndexes to perform "
                        + "object-qualified member lookup instead of owning key construction "
                        + "or field/method index selection: " + objectSymbols);
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

    private static void assertUnresolvedReferenceInspectionUsesSharedMessages(Path javaRoot) throws Exception {
        Path inspection = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinUnresolvedReferenceInspection.java"));
        require(Files.isRegularFile(inspection),
                "Qin unresolved-reference inspection source not found: " + inspection);
        String inspectionSource = Files.readString(inspection);
        require(inspectionSource.contains("QinUnresolvedReferenceMessages.messageFor(element)")
                        && !inspectionSource.contains("javaMessageFor(")
                        && !inspectionSource.contains("objectMethodMessageFor(")
                        && !inspectionSource.contains("objectFieldMessageFor(")
                        && !inspectionSource.contains("QinJavaReference")
                        && !inspectionSource.contains("QinObjectMethodReference")
                        && !inspectionSource.contains("QinObjectFieldReference"),
                "QinUnresolvedReferenceInspection must use the shared message helper instead of "
                        + "owning Java/object reference branches: " + inspection);
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

    private static void assertReferenceRenameDoesNotPreserveAliasLocals(Path javaRoot) throws Exception {
        for (Path file : List.of(
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinJavaReference.java")),
                javaRoot.resolve(Path.of("com", "qin", "debug", "lsp", "QinObjectReference.java")))) {
            require(Files.isRegularFile(file), "Qin reference source not found: " + file);
            String source = Files.readString(file);
            int renameIndex = source.indexOf("handleElementRename(");
            require(renameIndex >= 0, "Qin reference must implement handleElementRename: " + file);
            int candidateIndex = source.indexOf("ReferenceCandidate(");
            String renameSource = source.substring(renameIndex, candidateIndex < 0 ? source.length() : candidateIndex);
            require(!renameSource.contains("isImportedAliasLocalReference(")
                            && !renameSource.contains("return myElement"),
                    "Qin object/class reference rename must not keep a local-alias no-op fallback; "
                            + "local aliases should be excluded at the candidate boundary: " + file);
        }
    }

    private static void assertObjectMemberCompletionUsesSharedHelper(Path javaRoot) throws Exception {
        Path completionHelper = javaRoot.resolve(Path.of(
                "com", "qin", "debug", "lsp", "QinObjectMemberCompletions.java"));
        require(Files.isRegularFile(completionHelper),
                "Qin object member completion helper source not found: " + completionHelper);
        String helperSource = Files.readString(completionHelper);
        require(helperSource.contains("members(")
                        && helperSource.contains("record CompletionMember(")
                        && helperSource.contains("member.name()")
                        && helperSource.contains("member.element()")
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
        require(contributorSource.contains("QinObjectMemberCompletions.members(")
                        && contributorSource.contains("LookupElementBuilder.create(member.name())")
                        && contributorSource.contains(".withPsiElement(member.element())")
                        && !contributorSource.contains("member.getText()")
                        && !contributorSource.contains("PsiNamedElement")
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
