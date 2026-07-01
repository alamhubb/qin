package com.qin.debug.lsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QinLspServerDiagnosticsSmokeTestMain {
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    private QinLspServerDiagnosticsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path workspaceRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of("."));
        String languageFilter = args.length > 1 ? args[1] : null;
        List<LanguageCase> cases = List.of(
                new LanguageCase(
                        "qin",
                        "export object Broken { value = }",
                        "qin-parser",
                        """
                                export object Counter {
                                  value = 1
                                }
                                function formatLabel(name: string, count: number): string { return name + count }
                                const formattedLabel = formatLabel("qin", Counter.value)
                                const currentValue = Counter.value
                                Coun
                                """,
                        "Counter",
                        6,
                        4,
                        "Counter",
                        5,
                        23,
                        "formatLabel",
                        4,
                        42,
                        5,
                        23,
                        5,
                        23,
                        0,
                        14,
                        5,
                        21,
                        true,
                        true,
                        true,
                        true,
                        "Counter"),
                new LanguageCase(
                        "ovs",
                        "div { h1 { 'Broken' }",
                        "OVS transform failed",
                        """
                                const alphaNumber = 41
                                const alphaText = alphaNumber.toString()
                                const finalValue = alphaText
                                function formatLabel(name: string, count: number): string { return name + count }
                                const formattedLabel = formatLabel("qin", alphaNumber)
                                al
                                """,
                        "alphaNumber",
                        5,
                        2,
                        "alphaNumber",
                        0,
                        8,
                        "formatLabel",
                        4,
                        42,
                        1,
                        20,
                        0,
                        8,
                        0,
                        6,
                        1,
                        18,
                        true,
                        true,
                        true,
                        true,
                        "alphaNumber"),
                new LanguageCase(
                        "cssts",
                        "import { css } from 'cssts-ts'\nconst broken = css { displayFlex,\n",
                        "CSSTS transform failed",
                        """
                                const alphaNumber = 41
                                const alphaText = alphaNumber.toString()
                                const finalValue = alphaText
                                function formatLabel(name: string, count: number): string { return name + count }
                                const formattedLabel = formatLabel("qin", alphaNumber)
                                al
                                """,
                        "alphaNumber",
                        5,
                        2,
                        "alphaNumber",
                        0,
                        8,
                        "formatLabel",
                        4,
                        42,
                        1,
                        20,
                        0,
                        8,
                        0,
                        6,
                        1,
                        18,
                        true,
                        true,
                        true,
                        true,
                        "alphaNumber"));

        boolean checkedAnyLanguage = false;
        for (LanguageCase testCase : cases) {
            if (!matchesLanguageFilter(testCase.extension(), languageFilter)) {
                continue;
            }
            checkedAnyLanguage = true;
            QinLspLanguage language = QinLspLanguageRegistry.fromExtension(workspaceRoot, testCase.extension());
            require(language != null, "Missing language for ." + testCase.extension());
            runLanguageCase(workspaceRoot, language, testCase);
        }

        require(checkedAnyLanguage, "No LSP language matched filter: " + languageFilter);
        System.out.println("Qin IDEA LSP server diagnostics smoke passed");
    }

    private static boolean matchesLanguageFilter(String extension, String languageFilter) {
        return languageFilter == null
                || languageFilter.isBlank()
                || extension.equals(normalizedExtension(languageFilter));
    }

    private static String normalizedExtension(String extension) {
        return extension.startsWith(".") ? extension.substring(1) : extension;
    }

    private static void runLanguageCase(
            Path workspaceRoot,
            QinLspLanguage language,
            LanguageCase testCase) throws Exception {
        QinLspServerCommandSpec commandSpec = QinLspServerCommandLineFactory.createSpec(workspaceRoot, language);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command().add(commandSpec.executable());
        processBuilder.command().addAll(commandSpec.arguments());
        processBuilder.directory(commandSpec.workDirectory().toFile());
        processBuilder.environment().putAll(commandSpec.environment());
        processBuilder.redirectErrorStream(false);
        Process process = processBuilder.start();

        LspSession session = new LspSession(process);
        try {
            int initializeId = session.request("initialize", Map.of(
                    "processId", ProcessHandle.current().pid(),
                    "capabilities", Map.of(
                            "workspace", Map.of(
                                    "configuration", true,
                                    "symbol", Map.of()),
                            "textDocument", Map.ofEntries(
                                    Map.entry("callHierarchy", Map.of()),
                                    Map.entry("codeAction", Map.of()),
                                    Map.entry("completion", Map.of()),
                                    Map.entry("declaration", Map.of()),
                                    Map.entry("documentLink", Map.of()),
                                    Map.entry("documentHighlight", Map.of()),
                                    Map.entry("formatting", Map.of()),
                                    Map.entry("rangeFormatting", Map.of()),
                                    Map.entry("onTypeFormatting", Map.of()),
                                    Map.entry("foldingRange", Map.of()),
                                    Map.entry("hover", Map.of()),
                                    Map.entry("implementation", Map.of()),
                                    Map.entry("inlayHint", Map.of()),
                                    Map.entry("linkedEditingRange", Map.of()),
                                    Map.entry("rename", Map.of()),
                                    Map.entry("selectionRange", Map.of()),
                                    Map.entry("signatureHelp", Map.of()),
                                    Map.entry("typeDefinition", Map.of()),
                                    Map.entry("publishDiagnostics", Map.of()))),
                    "rootUri", workspaceRoot.toUri().toString(),
                    "initializationOptions", Map.of(
                            "typescript", Map.of("tsdk", commandSpec.environment().get("QIN_LSP_TYPESCRIPT_TSDK")))));
            Map<String, Object> initializeResponse = session.awaitResponse(initializeId);
            assertLanguageCapabilities(language, initializeResponse);
            session.notification("initialized", Map.of());

            String uri = workspaceRoot
                    .resolve("tmp")
                    .resolve("idea-lsp-smoke")
                    .resolve("bad." + testCase.extension())
                    .toUri()
                    .toString();
            session.notification("textDocument/didOpen", Map.of(
                    "textDocument", Map.of(
                            "uri", uri,
                            "languageId", language.id(),
                            "version", 1,
                            "text", testCase.invalidSource())));

            session.awaitDiagnostic(uri, testCase.expectedDiagnosticText());
            runLanguageFeatureAssertions(session, language, testCase, workspaceRoot);
            int shutdownId = session.request("shutdown", null);
            session.awaitResponse(shutdownId);
            session.notification("exit", null);
        } finally {
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private static void runLanguageFeatureAssertions(
            LspSession session,
            QinLspLanguage language,
            LanguageCase testCase,
            Path workspaceRoot) throws IOException {
        if (!testCase.expectCompletion()
                && !testCase.expectDefinitionAndSymbols()
                && !testCase.expectReferences()
                && !testCase.expectSemanticTokens()) {
            return;
        }
        require(testCase.validSource() != null, language.id() + " feature assertions require validSource");
        String uri = workspaceRoot
                .resolve("tmp")
                .resolve("idea-lsp-smoke")
                .resolve("good." + testCase.extension())
                .toUri()
                .toString();
        session.notification("textDocument/didOpen", Map.of(
                "textDocument", Map.of(
                        "uri", uri,
                        "languageId", language.id(),
                        "version", 1,
                        "text", testCase.validSource())));

        if (testCase.expectCompletion()) {
            Map<String, Object> completion = session.awaitResponse(session.request("textDocument/completion", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.completionLine(), "character", testCase.completionCharacter()),
                    "context", Map.of("triggerKind", 1))));
            require(completionLabels(completion).contains(testCase.expectedCompletionLabel()),
                    language.id() + " completion missing " + testCase.expectedCompletionLabel() + ": " + completion);
            Object completionItem = completionItem(completion, testCase.expectedCompletionLabel());
            require(completionItem != null,
                    language.id() + " completion missing item for resolve " + testCase.expectedCompletionLabel() + ": "
                            + completion);
            Map<String, Object> completionResolve = session.awaitResponse(session.request(
                    "completionItem/resolve",
                    completionItem));
            require(hasCompletionDetail(
                            completionResolve.get("result"),
                            testCase.expectedCompletionLabel()),
                    language.id() + " completionItem resolve did not preserve label and detail: "
                            + completionResolve);

            Map<String, Object> hover = session.awaitResponse(session.request("textDocument/hover", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.hoverLine(), "character", testCase.hoverCharacter()))));
            require(hoverText(hover).contains(testCase.expectedHoverText()),
                    language.id() + " hover missing " + testCase.expectedHoverText() + ": " + hover);

            Map<String, Object> signatureHelp = session.awaitResponse(session.request("textDocument/signatureHelp", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.signatureHelpLine(), "character", testCase.signatureHelpCharacter()),
                    "context", Map.of("triggerKind", 1, "isRetrigger", false))));
            require(signatureLabels(signatureHelp).stream().anyMatch(label -> label.contains(testCase.expectedSignatureLabel())),
                    language.id() + " signatureHelp missing " + testCase.expectedSignatureLabel() + ": " + signatureHelp);
        }

        if (testCase.expectDefinitionAndSymbols()) {
            Map<String, Object> definition = session.awaitResponse(session.request("textDocument/definition", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.definitionLine(), "character", testCase.definitionCharacter()))));
            require(hasLocationInUri(definition.get("result"), uri),
                    language.id() + " definition did not resolve inside current document: " + definition);

            Map<String, Object> declaration = session.awaitResponse(session.request("textDocument/declaration", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.definitionLine(), "character", testCase.definitionCharacter()))));
            require(hasLocationInUri(declaration.get("result"), uri),
                    language.id() + " declaration did not resolve inside current document: " + declaration);

            if (testCase.expectReferences()) {
                Map<String, Object> references = session.awaitResponse(session.request("textDocument/references", Map.of(
                        "textDocument", Map.of("uri", uri),
                        "position", Map.of("line", testCase.referencesLine(), "character", testCase.referencesCharacter()),
                        "context", Map.of("includeDeclaration", true))));
                require(hasLocationStartingAt(
                                        references.get("result"),
                                        uri,
                                        testCase.expectedReferenceDeclarationLine(),
                                        testCase.expectedReferenceDeclarationCharacter())
                                && hasLocationStartingAt(
                                        references.get("result"),
                                        uri,
                                        testCase.expectedReferenceUsageLine(),
                                        testCase.expectedReferenceUsageCharacter()),
                        language.id() + " references did not include declaration and usage: " + references);
            }

            Map<String, Object> highlights = session.awaitResponse(session.request("textDocument/documentHighlight", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.referencesLine(), "character", testCase.referencesCharacter()))));
            require(hasRangeStartingAt(
                            highlights.get("result"),
                            testCase.expectedReferenceDeclarationLine(),
                            testCase.expectedReferenceDeclarationCharacter())
                            && hasRangeStartingAt(
                            highlights.get("result"),
                            testCase.expectedReferenceUsageLine(),
                            testCase.expectedReferenceUsageCharacter()),
                    language.id() + " documentHighlight did not include declaration and usage: " + highlights);

            Map<String, Object> symbols = session.awaitResponse(session.request("textDocument/documentSymbol", Map.of(
                    "textDocument", Map.of("uri", uri))));
            require(symbolNames(symbols.get("result")).contains(testCase.expectedDocumentSymbol()),
                    language.id() + " documentSymbol missing " + testCase.expectedDocumentSymbol() + ": " + symbols);

            if ("qin".equals(language.id())) {
                String typeDefinitionUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("type-definition.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", typeDefinitionUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", """
                                        interface User { name: string }
                                        const currentUser: User = { name: "qin" }
                                        const label = currentUser.name
                                        """)));
                Map<String, Object> typeDefinition = session.awaitResponse(session.request("textDocument/typeDefinition", Map.of(
                        "textDocument", Map.of("uri", typeDefinitionUri),
                        "position", Map.of("line", 2, "character", 15))));
                require(hasLocationStartingAt(typeDefinition.get("result"), typeDefinitionUri, 0, 0),
                        language.id() + " typeDefinition did not resolve currentUser to source interface: "
                                + typeDefinition);

                String implementationUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("implementation.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", implementationUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", """
                                        interface Printable { print(): string }
                                        class Label implements Printable {
                                          print(): string { return "qin" }
                                        }
                                        const printable: Printable = new Label()
                                        printable.print()
                                        """)));
                Map<String, Object> implementation = session.awaitResponse(session.request("textDocument/implementation", Map.of(
                        "textDocument", Map.of("uri", implementationUri),
                        "position", Map.of("line", 0, "character", 11))));
                require(hasLocationContaining(implementation.get("result"), implementationUri, 1, 6),
                        language.id() + " implementation did not resolve interface to source class: "
                                + implementation);

                String callHierarchyUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("call-hierarchy.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", callHierarchyUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", """
                                        function targetLabel(name: string): string {
                                          return name
                                        }
                                        function renderLabel(): string {
                                          return targetLabel("qin")
                                        }
                                        renderLabel()
                                        """)));
                Map<String, Object> preparedTargetCalls = session.awaitResponse(session.request(
                        "textDocument/prepareCallHierarchy",
                        Map.of(
                                "textDocument", Map.of("uri", callHierarchyUri),
                                "position", Map.of("line", 0, "character", 11))));
                Object targetCallItem = callHierarchyItem(
                        preparedTargetCalls.get("result"),
                        "targetLabel",
                        callHierarchyUri,
                        0,
                        9);
                require(targetCallItem != null,
                        language.id() + " prepareCallHierarchy did not return source targetLabel item: "
                                + preparedTargetCalls);

                Map<String, Object> incomingCalls = session.awaitResponse(session.request(
                        "callHierarchy/incomingCalls",
                        Map.of("item", targetCallItem)));
                require(hasIncomingCall(
                                incomingCalls.get("result"),
                                "renderLabel",
                                callHierarchyUri,
                                3,
                                9,
                                4,
                                9),
                        language.id() + " incomingCalls did not resolve source caller and callsite: "
                                + incomingCalls);

                Map<String, Object> preparedCallerCalls = session.awaitResponse(session.request(
                        "textDocument/prepareCallHierarchy",
                        Map.of(
                                "textDocument", Map.of("uri", callHierarchyUri),
                                "position", Map.of("line", 3, "character", 11))));
                Object renderCallItem = callHierarchyItem(
                        preparedCallerCalls.get("result"),
                        "renderLabel",
                        callHierarchyUri,
                        3,
                        9);
                require(renderCallItem != null,
                        language.id() + " prepareCallHierarchy did not return source renderLabel item: "
                                + preparedCallerCalls);

                Map<String, Object> outgoingCalls = session.awaitResponse(session.request(
                        "callHierarchy/outgoingCalls",
                        Map.of("item", renderCallItem)));
                require(hasOutgoingCall(
                                outgoingCalls.get("result"),
                                "targetLabel",
                                callHierarchyUri,
                                0,
                                9,
                                4,
                                9),
                        language.id() + " outgoingCalls did not resolve source callee and callsite: "
                                + outgoingCalls);

                String formattingUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("formatting.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", formattingUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", "const messy={value:1}\n")));
                Map<String, Object> formatting = session.awaitResponse(session.request("textDocument/formatting", Map.of(
                        "textDocument", Map.of("uri", formattingUri),
                        "options", Map.of("tabSize", 2, "insertSpaces", true))));
                require(textEditTexts(formatting.get("result")).stream()
                                .anyMatch(text -> text.contains("const messy = { value: 1 }")),
                        language.id() + " formatting did not return TypeScript formatter edits through source mappings: " + formatting);

                Map<String, Object> rangeFormatting = session.awaitResponse(session.request("textDocument/rangeFormatting", Map.of(
                        "textDocument", Map.of("uri", formattingUri),
                        "range", Map.of(
                                "start", Map.of("line", 0, "character", 0),
                                "end", Map.of("line", 0, "character", 22)),
                        "options", Map.of("tabSize", 2, "insertSpaces", true))));
                require(textEditTexts(rangeFormatting.get("result")).stream()
                                .anyMatch(text -> text.contains("const messy = { value: 1 }")),
                        language.id() + " rangeFormatting did not return TypeScript formatter edits through source mappings: "
                                + rangeFormatting);

                String onTypeFormattingUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("on-type-formatting.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", onTypeFormattingUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", """
                                        function wrap(){
                                        const value=1
                                        }
                                        """)));
                Map<String, Object> onTypeFormatting = session.awaitResponse(session.request("textDocument/onTypeFormatting", Map.of(
                        "textDocument", Map.of("uri", onTypeFormattingUri),
                        "position", Map.of("line", 2, "character", 1),
                        "ch", "}",
                        "options", Map.of("tabSize", 2, "insertSpaces", true))));
                List<String> onTypeFormattingTexts = textEditTexts(onTypeFormatting.get("result"));
                require(onTypeFormattingTexts.stream().anyMatch(text -> text.contains("function wrap()"))
                                && onTypeFormattingTexts.stream().anyMatch(text -> text.contains("  const value = 1")),
                        language.id() + " onTypeFormatting did not return TypeScript formatter edits through source mappings: "
                                + onTypeFormatting);

                Map<String, Object> inlayHints = session.awaitResponse(session.request("textDocument/inlayHint", Map.of(
                        "textDocument", Map.of("uri", uri),
                        "range", Map.of(
                                "start", Map.of("line", 0, "character", 0),
                                "end", Map.of("line", 6, "character", 0)))));
                require(hasInlayHintLabel(inlayHints.get("result"), "name")
                                && hasInlayHintLabel(inlayHints.get("result"), "count")
                                && hasInlayHintLabel(inlayHints.get("result"), "string"),
                        language.id() + " inlayHint did not include parameter or variable type hints through source mappings: "
                                + inlayHints);

                Map<String, Object> foldingRanges = session.awaitResponse(session.request("textDocument/foldingRange", Map.of(
                        "textDocument", Map.of("uri", uri))));
                require(hasFoldingRange(foldingRanges.get("result"), 0, 2),
                        language.id() + " foldingRange missing source object block: " + foldingRanges);

                Map<String, Object> selectionRanges = session.awaitResponse(session.request("textDocument/selectionRange", Map.of(
                        "textDocument", Map.of("uri", uri),
                        "positions", List.of(Map.of("line", 0, "character", 16)))));
                require(hasSelectionRangeChain(selectionRanges.get("result"), 0, 14, 0, 7),
                        language.id() + " selectionRange missing object name and declaration chain: " + selectionRanges);

                Map<String, Object> linkedEditingRanges = session.awaitResponse(session.request("textDocument/linkedEditingRange", Map.of(
                        "textDocument", Map.of("uri", uri),
                        "position", Map.of("line", 0, "character", 16))));
                require(hasLinkedEditingRanges(linkedEditingRanges.get("result"), 0, 14, 5, 21),
                        language.id() + " linkedEditingRange missing object declaration and usage: " + linkedEditingRanges);

                String importedUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("imported.qin")
                        .toUri()
                        .toString();
                String importConsumerUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("consumer.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", importedUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", """
                                        export object Counter {
                                          value = 1
                                        }
                                        """)));
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", importConsumerUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", "import { Counter } from './imported.qin'\nconst currentValue = Counter.value\n")));
                Map<String, Object> documentLinks = session.awaitResponse(session.request("textDocument/documentLink", Map.of(
                        "textDocument", Map.of("uri", importConsumerUri))));
                require(hasDocumentLinkTarget(documentLinks.get("result"), importedUri, 0, 25),
                        language.id() + " documentLink missing local import target: " + documentLinks);

                Map<String, Object> fileReferences = session.awaitResponse(session.request("volar/client/findFileReference", Map.of(
                        "textDocument", Map.of("uri", importedUri))));
                require(hasLocationContaining(fileReferences.get("result"), importConsumerUri, 0, 26),
                        language.id() + " fileReferences missing local import usage: " + fileReferences);

                String renamedImportedUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("renamed-imported.qin")
                        .toUri()
                        .toString();
                Map<String, Object> fileRenameEdits = session.awaitResponse(session.request("workspace/willRenameFiles", Map.of(
                        "files", List.of(Map.of(
                                "oldUri", importedUri,
                                "newUri", renamedImportedUri)))));
                require(workspaceEditTexts(fileRenameEdits.get("result")).stream()
                                .anyMatch(text -> text.contains("renamed-imported")),
                        language.id() + " fileRenameEdits did not update local import specifier: " + fileRenameEdits);

                String sharedJavaImportUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("shared")
                        .resolve("policy.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", sharedJavaImportUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", "import { ArrayList } from 'java:java.util'\nexport const sharedValue = ArrayList\n")));
                session.awaitDiagnostic(sharedJavaImportUri, "QIN1002");

                String sharedBareImportUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("shared")
                        .resolve("bare-policy.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", sharedBareImportUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", "import lodash from 'lodash'\nexport const sharedValue = lodash\n")));
                session.awaitDiagnostic(sharedBareImportUri, "QIN1003");

                String appJavaImportUri = workspaceRoot
                        .resolve("tmp")
                        .resolve("idea-lsp-smoke")
                        .resolve("app")
                        .resolve("policy.qin")
                        .toUri()
                        .toString();
                session.notification("textDocument/didOpen", Map.of(
                        "textDocument", Map.of(
                                "uri", appJavaImportUri,
                                "languageId", language.id(),
                                "version", 1,
                                "text", "import { ArrayList } from 'java:java.util'\nexport const appValue = ArrayList\n")));
                session.awaitDiagnostic(appJavaImportUri, "QIN1001");
                Map<String, Object> codeActions = session.awaitResponse(session.request("textDocument/codeAction", Map.of(
                        "textDocument", Map.of("uri", appJavaImportUri),
                        "range", Map.of(
                                "start", Map.of("line", 0, "character", 27),
                                "end", Map.of("line", 0, "character", 41)),
                        "context", Map.of(
                                "diagnostics", List.of(Map.of(
                                        "range", Map.of(
                                                "start", Map.of("line", 0, "character", 27),
                                                "end", Map.of("line", 0, "character", 41)),
                                        "source", "qin-import-policy",
                                        "message", "QIN1001 app code cannot import java modules: java:java.util")),
                                "only", List.of("quickfix")))));
                require(hasQuickFixRemovingImport(codeActions.get("result")),
                        language.id() + " codeAction missing remove forbidden java import quickfix: " + codeActions);
                Object removeJavaImportAction = quickFixAction(codeActions.get("result"), "Remove forbidden java import");
                require(removeJavaImportAction != null,
                        language.id() + " codeAction missing quickfix item for resolve: " + codeActions);
                Map<String, Object> resolvedCodeAction = session.awaitResponse(session.request(
                        "codeAction/resolve",
                        removeJavaImportAction));
                require(hasQuickFixEdit(resolvedCodeAction.get("result"), "Remove forbidden java import"),
                        language.id() + " codeAction resolve did not preserve import-policy quickfix edit: "
                                + resolvedCodeAction);

                Map<String, Object> importPolicyHover = session.awaitResponse(session.request("textDocument/hover", Map.of(
                        "textDocument", Map.of("uri", appJavaImportUri),
                        "position", Map.of("line", 0, "character", 30))));
                String importPolicyHoverText = hoverText(importPolicyHover);
                require(importPolicyHoverText.contains("QIN1001") && importPolicyHoverText.contains("main/"),
                        language.id() + " hover missing import-policy app java boundary: " + importPolicyHover);

                Map<String, Object> sharedBareCodeActions = session.awaitResponse(session.request("textDocument/codeAction", Map.of(
                        "textDocument", Map.of("uri", sharedBareImportUri),
                        "range", Map.of(
                                "start", Map.of("line", 0, "character", 20),
                                "end", Map.of("line", 0, "character", 26)),
                        "context", Map.of(
                                "diagnostics", List.of(Map.of(
                                        "range", Map.of(
                                                "start", Map.of("line", 0, "character", 20),
                                                "end", Map.of("line", 0, "character", 26)),
                                        "source", "qin-import-policy",
                                        "message", "QIN1003 shared code cannot import bare/non-local modules: lodash")),
                                "only", List.of("quickfix")))));
                require(hasQuickFixRemovingImport(sharedBareCodeActions.get("result"), "Remove forbidden shared import"),
                        language.id() + " codeAction missing remove forbidden shared import quickfix: " + sharedBareCodeActions);

                Map<String, Object> sharedBareImportPolicyHover = session.awaitResponse(session.request("textDocument/hover", Map.of(
                        "textDocument", Map.of("uri", sharedBareImportUri),
                        "position", Map.of("line", 0, "character", 21))));
                String sharedBareImportPolicyHoverText = hoverText(sharedBareImportPolicyHover);
                require(sharedBareImportPolicyHoverText.contains("QIN1003")
                                && sharedBareImportPolicyHoverText.contains("local relative modules"),
                        language.id() + " shared import-policy hover did not explain bare import boundary: "
                                + sharedBareImportPolicyHover);

                Map<String, Object> workspaceSymbols = session.awaitResponse(session.request("workspace/symbol", Map.of(
                        "query", testCase.expectedDocumentSymbol())));
                require(hasWorkspaceSymbol(workspaceSymbols.get("result"), testCase.expectedDocumentSymbol(), uri, 0, 14),
                        language.id() + " workspaceSymbol missing source object symbol: " + workspaceSymbols);
            }

            Map<String, Object> rename = session.awaitResponse(session.request("textDocument/rename", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.referencesLine(), "character", testCase.referencesCharacter()),
                    "newName", "renamedSymbol")));
            require(workspaceEditTexts(rename.get("result")).contains("renamedSymbol"),
                    language.id() + " rename did not return workspace edits: " + rename);

            Map<String, Object> prepareRename = session.awaitResponse(session.request("textDocument/prepareRename", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.referencesLine(), "character", testCase.referencesCharacter()))));
            require(
                    hasRangeStartingAt(
                            prepareRename.get("result"),
                            testCase.expectedReferenceDeclarationLine(),
                            testCase.expectedReferenceDeclarationCharacter())
                            || hasRangeStartingAt(
                            prepareRename.get("result"),
                            testCase.expectedReferenceUsageLine(),
                            testCase.expectedReferenceUsageCharacter()),
                    language.id() + " prepareRename did not return symbol range: " + prepareRename);
        }

        if (testCase.expectSemanticTokens()) {
            Map<String, Object> semanticTokens = session.awaitResponse(session.request("textDocument/semanticTokens/full", Map.of(
                    "textDocument", Map.of("uri", uri))));
            Object data = resultMap(semanticTokens).get("data");
            require(data instanceof List<?> list && !list.isEmpty(),
                    language.id() + " semanticTokens returned no token data: " + semanticTokens);

            Map<String, Object> semanticTokenRange = session.awaitResponse(session.request("textDocument/semanticTokens/range", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "range", Map.of(
                            "start", Map.of("line", testCase.expectedReferenceDeclarationLine(), "character", 0),
                            "end", Map.of("line", testCase.expectedReferenceUsageLine() + 1, "character", 0)))));
            Object rangeData = resultMap(semanticTokenRange).get("data");
            require(rangeData instanceof List<?> rangeList && !rangeList.isEmpty(),
                    language.id() + " semanticTokens range returned no token data: " + semanticTokenRange);
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private static void assertLanguageCapabilities(QinLspLanguage language, Map<String, Object> initializeResponse) {
        Object result = initializeResponse.get("result");
        require(result instanceof Map<?, ?>, language.id() + " initialize response missing result");
        Map<?, ?> resultMap = (Map<?, ?>) result;
        Object capabilities = resultMap.get("capabilities");
        require(capabilities instanceof Map<?, ?>, language.id() + " initialize response missing capabilities");
        Map<?, ?> capabilitiesMap = (Map<?, ?>) capabilities;

        require(capabilitiesMap.containsKey("completionProvider"), language.id() + " LSP missing completionProvider");
        Object completionProvider = capabilitiesMap.get("completionProvider");
        require(completionProvider instanceof Map<?, ?> completionProviderMap
                        && Boolean.TRUE.equals(completionProviderMap.get("resolveProvider")),
                language.id() + " completionProvider missing resolveProvider");
        require(capabilitiesMap.containsKey("callHierarchyProvider"), language.id() + " LSP missing callHierarchyProvider");
        require(capabilitiesMap.containsKey("codeActionProvider"), language.id() + " LSP missing codeActionProvider");
        Object codeActionProvider = capabilitiesMap.get("codeActionProvider");
        require(codeActionProvider instanceof Map<?, ?> codeActionProviderMap
                        && Boolean.TRUE.equals(codeActionProviderMap.get("resolveProvider")),
                language.id() + " codeActionProvider missing resolveProvider");
        require(capabilitiesMap.containsKey("hoverProvider"), language.id() + " LSP missing hoverProvider");
        require(capabilitiesMap.containsKey("signatureHelpProvider"), language.id() + " LSP missing signatureHelpProvider");
        require(capabilitiesMap.containsKey("definitionProvider"), language.id() + " LSP missing definitionProvider");
        require(capabilitiesMap.containsKey("declarationProvider"), language.id() + " LSP missing declarationProvider");
        require(capabilitiesMap.containsKey("typeDefinitionProvider"), language.id() + " LSP missing typeDefinitionProvider");
        require(capabilitiesMap.containsKey("implementationProvider"), language.id() + " LSP missing implementationProvider");
        require(capabilitiesMap.containsKey("referencesProvider"), language.id() + " LSP missing referencesProvider");
        require(capabilitiesMap.containsKey("documentHighlightProvider"), language.id() + " LSP missing documentHighlightProvider");
        require(capabilitiesMap.containsKey("documentFormattingProvider"), language.id() + " LSP missing documentFormattingProvider");
        require(capabilitiesMap.containsKey("documentRangeFormattingProvider"), language.id() + " LSP missing documentRangeFormattingProvider");
        require(capabilitiesMap.containsKey("documentOnTypeFormattingProvider"), language.id() + " LSP missing documentOnTypeFormattingProvider");
        require(capabilitiesMap.containsKey("inlayHintProvider"), language.id() + " LSP missing inlayHintProvider");
        require(capabilitiesMap.containsKey("renameProvider"), language.id() + " LSP missing renameProvider");
        require(capabilitiesMap.containsKey("documentSymbolProvider"), language.id() + " LSP missing documentSymbolProvider");
        if ("qin".equals(language.id())) {
            Object experimental = capabilitiesMap.get("experimental");
            require(experimental instanceof Map<?, ?> experimentalMap
                            && Boolean.TRUE.equals(experimentalMap.get("fileReferencesProvider")),
                    language.id() + " LSP missing experimental.fileReferencesProvider");
            require(experimental instanceof Map<?, ?> experimentalMap
                            && Boolean.TRUE.equals(experimentalMap.get("fileRenameEditsProvider")),
                    language.id() + " LSP missing experimental.fileRenameEditsProvider");
            require(capabilitiesMap.containsKey("documentLinkProvider"), language.id() + " LSP missing documentLinkProvider");
            require(capabilitiesMap.containsKey("foldingRangeProvider"), language.id() + " LSP missing foldingRangeProvider");
            require(capabilitiesMap.containsKey("linkedEditingRangeProvider"), language.id() + " LSP missing linkedEditingRangeProvider");
            require(capabilitiesMap.containsKey("selectionRangeProvider"), language.id() + " LSP missing selectionRangeProvider");
            require(capabilitiesMap.containsKey("workspaceSymbolProvider"), language.id() + " LSP missing workspaceSymbolProvider");
        }
        require(capabilitiesMap.containsKey("semanticTokensProvider"), language.id() + " LSP missing semanticTokensProvider");

        Object semanticTokensProvider = capabilitiesMap.get("semanticTokensProvider");
        require(semanticTokensProvider instanceof Map<?, ?>, language.id() + " semanticTokensProvider must be an object");
        Map<?, ?> semanticMap = (Map<?, ?>) semanticTokensProvider;
        require(semanticMap.containsKey("legend"), language.id() + " semanticTokensProvider missing legend");
        require(Boolean.TRUE.equals(semanticMap.get("range")), language.id() + " semanticTokensProvider missing range support");
    }

    private static Map<?, ?> resultMap(Map<String, Object> response) {
        Object result = response.get("result");
        return result instanceof Map<?, ?> map ? map : Map.of();
    }

    private static List<String> completionLabels(Map<String, Object> response) {
        Object result = response.get("result");
        Object items = result instanceof Map<?, ?> map ? map.get("items") : result;
        if (!(items instanceof List<?> list)) {
            return List.of();
        }
        List<String> labels = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map<?, ?> map && map.get("label") != null) {
                labels.add(String.valueOf(map.get("label")));
            }
        }
        return labels;
    }

    private static Object completionItem(Map<String, Object> response, String label) {
        Object result = response.get("result");
        Object items = result instanceof Map<?, ?> map ? map.get("items") : result;
        if (!(items instanceof List<?> list)) {
            return null;
        }
        for (Object item : list) {
            if (item instanceof Map<?, ?> map && label.equals(String.valueOf(map.get("label")))) {
                return item;
            }
        }
        return null;
    }

    private static boolean hasCompletionDetail(Object result, String label) {
        if (!(result instanceof Map<?, ?> map)) {
            return false;
        }
        Object actualLabel = map.get("label");
        Object detail = map.get("detail");
        return label.equals(String.valueOf(actualLabel))
                && detail instanceof String detailText
                && detailText.contains(label);
    }

    private static List<String> signatureLabels(Map<String, Object> response) {
        Object result = response.get("result");
        Object signatures = result instanceof Map<?, ?> map ? map.get("signatures") : null;
        if (!(signatures instanceof List<?> list)) {
            return List.of();
        }
        List<String> labels = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map<?, ?> map && map.get("label") != null) {
                labels.add(String.valueOf(map.get("label")));
            }
        }
        return labels;
    }

    private static String hoverText(Map<String, Object> response) {
        Object result = response.get("result");
        if (!(result instanceof Map<?, ?> map)) {
            return "";
        }
        Object contents = map.get("contents");
        if (contents instanceof String text) {
            return text;
        }
        if (contents instanceof Map<?, ?> contentsMap) {
            Object value = contentsMap.get("value");
            return value == null ? contentsMap.toString() : String.valueOf(value);
        }
        if (contents instanceof List<?> list) {
            return list.toString();
        }
        return contents == null ? "" : String.valueOf(contents);
    }

    private static boolean hasLocationInUri(Object result, String uri) {
        if (result instanceof List<?> list) {
            for (Object item : list) {
                if (hasLocationInUri(item, uri)) {
                    return true;
                }
            }
            return false;
        }
        if (!(result instanceof Map<?, ?> map)) {
            return false;
        }
        Object locationUri = map.get("uri");
        if (locationUri == null) {
            locationUri = map.get("targetUri");
        }
        return locationUri != null && sameUri(uri, String.valueOf(locationUri));
    }

    private static boolean hasLocationStartingAt(Object result, String uri, int line, int character) {
        if (result instanceof List<?> list) {
            for (Object item : list) {
                if (hasLocationStartingAt(item, uri, line, character)) {
                    return true;
                }
            }
            return false;
        }
        if (!(result instanceof Map<?, ?> map)) {
            return false;
        }
        Object locationUri = map.get("uri");
        if (locationUri == null) {
            locationUri = map.get("targetUri");
        }
        if (locationUri == null || !sameUri(uri, String.valueOf(locationUri))) {
            return false;
        }
        Object range = map.get("range");
        if (range == null) {
            range = map.get("targetRange");
        }
        if (!(range instanceof Map<?, ?> rangeMap)) {
            return false;
        }
        Object start = rangeMap.get("start");
        if (!(start instanceof Map<?, ?> startMap)) {
            return false;
        }
        Object actualLine = startMap.get("line");
        Object actualCharacter = startMap.get("character");
        return actualLine instanceof Number lineNumber
                && actualCharacter instanceof Number characterNumber
                && lineNumber.intValue() == line
                && characterNumber.intValue() == character;
    }

    private static boolean hasLocationContaining(Object result, String uri, int line, int character) {
        if (result instanceof List<?> list) {
            for (Object item : list) {
                if (hasLocationContaining(item, uri, line, character)) {
                    return true;
                }
            }
            return false;
        }
        if (!(result instanceof Map<?, ?> map)) {
            return false;
        }
        Object locationUri = map.get("uri");
        if (locationUri == null) {
            locationUri = map.get("targetUri");
        }
        if (locationUri == null || !sameUri(uri, String.valueOf(locationUri))) {
            return false;
        }
        Object range = map.get("range");
        if (range == null) {
            range = map.get("targetRange");
        }
        return rangeContains(range, line, character);
    }

    private static boolean rangeContains(Object range, int line, int character) {
        if (!(range instanceof Map<?, ?> rangeMap)
                || !(rangeMap.get("start") instanceof Map<?, ?> startMap)
                || !(rangeMap.get("end") instanceof Map<?, ?> endMap)
                || !(startMap.get("line") instanceof Number startLine)
                || !(startMap.get("character") instanceof Number startCharacter)
                || !(endMap.get("line") instanceof Number endLine)
                || !(endMap.get("character") instanceof Number endCharacter)) {
            return false;
        }
        int startLineValue = startLine.intValue();
        int startCharacterValue = startCharacter.intValue();
        int endLineValue = endLine.intValue();
        int endCharacterValue = endCharacter.intValue();
        if (line < startLineValue || line > endLineValue) {
            return false;
        }
        if (line == startLineValue && character < startCharacterValue) {
            return false;
        }
        return line != endLineValue || character <= endCharacterValue;
    }

    private static boolean hasRangeStartingAt(Object result, int line, int character) {
        if (result instanceof List<?> list) {
            for (Object item : list) {
                if (hasRangeStartingAt(item, line, character)) {
                    return true;
                }
            }
            return false;
        }
        if (!(result instanceof Map<?, ?> map)) {
            return false;
        }
        Object range = map.get("range");
        if (range == null && map.get("start") != null && map.get("end") != null) {
            range = map;
        }
        if (!(range instanceof Map<?, ?> rangeMap)) {
            return false;
        }
        Object start = rangeMap.get("start");
        if (!(start instanceof Map<?, ?> startMap)) {
            return false;
        }
        Object actualLine = startMap.get("line");
        Object actualCharacter = startMap.get("character");
        return actualLine instanceof Number lineNumber
                && actualCharacter instanceof Number characterNumber
                && lineNumber.intValue() == line
                && characterNumber.intValue() == character;
    }

    private static boolean hasFoldingRange(Object result, int startLine, int minimumEndLine) {
        if (!(result instanceof List<?> list)) {
            return false;
        }
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> map)) {
                continue;
            }
            Object actualStartLine = map.get("startLine");
            Object actualEndLine = map.get("endLine");
            if (actualStartLine instanceof Number startLineNumber
                    && actualEndLine instanceof Number endLineNumber
                    && startLineNumber.intValue() == startLine
                    && endLineNumber.intValue() >= minimumEndLine) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasSelectionRangeChain(
            Object result,
            int childLine,
            int childCharacter,
            int parentLine,
            int parentCharacter) {
        if (!(result instanceof List<?> list) || list.isEmpty()) {
            return false;
        }
        Object first = list.getFirst();
        if (!(first instanceof Map<?, ?> selectionRange)) {
            return false;
        }
        return selectionRangeChainContains(selectionRange, childLine, childCharacter)
                && selectionRangeChainContains(selectionRange, parentLine, parentCharacter);
    }

    private static boolean selectionRangeChainContains(Object selectionRange, int line, int character) {
        Object current = selectionRange;
        while (current instanceof Map<?, ?> currentMap) {
            if (hasRangeStartingAt(currentMap, line, character)) {
                return true;
            }
            current = currentMap.get("parent");
        }
        return false;
    }

    private static boolean hasWorkspaceSymbol(Object result, String name, String uri, int line, int character) {
        if (!(result instanceof List<?> list)) {
            return false;
        }
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> symbol)) {
                continue;
            }
            Object symbolName = symbol.get("name");
            Object location = symbol.get("location");
            if (symbolName != null
                    && name.equals(String.valueOf(symbolName))
                    && hasLocationStartingAt(location, uri, line, character)) {
                return true;
            }
        }
        return false;
    }

    private static Object callHierarchyItem(Object result, String name, String uri, int line, int character) {
        if (!(result instanceof List<?> list)) {
            return null;
        }
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> map)) {
                continue;
            }
            Object itemName = map.get("name");
            Object itemUri = map.get("uri");
            if (name.equals(String.valueOf(itemName))
                    && itemUri != null
                    && sameUri(uri, String.valueOf(itemUri))
                    && hasLocationContaining(item, uri, line, character)) {
                return item;
            }
        }
        return null;
    }

    private static boolean hasIncomingCall(
            Object result,
            String callerName,
            String uri,
            int callerLine,
            int callerCharacter,
            int callsiteLine,
            int callsiteCharacter) {
        if (!(result instanceof List<?> list)) {
            return false;
        }
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> call)) {
                continue;
            }
            Object from = call.get("from");
            if (from instanceof Map<?, ?> fromMap
                    && callerName.equals(String.valueOf(fromMap.get("name")))
                    && hasLocationContaining(from, uri, callerLine, callerCharacter)
                    && hasCallHierarchyCallsite(call.get("fromRanges"), callsiteLine, callsiteCharacter)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasOutgoingCall(
            Object result,
            String calleeName,
            String uri,
            int calleeLine,
            int calleeCharacter,
            int callsiteLine,
            int callsiteCharacter) {
        if (!(result instanceof List<?> list)) {
            return false;
        }
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> call)) {
                continue;
            }
            Object to = call.get("to");
            if (to instanceof Map<?, ?> toMap
                    && calleeName.equals(String.valueOf(toMap.get("name")))
                    && hasLocationContaining(to, uri, calleeLine, calleeCharacter)
                    && hasCallHierarchyCallsite(call.get("fromRanges"), callsiteLine, callsiteCharacter)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCallHierarchyCallsite(Object ranges, int line, int character) {
        if (!(ranges instanceof List<?> list)) {
            return false;
        }
        for (Object range : list) {
            if (rangeContains(range, line, character)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasLinkedEditingRanges(
            Object result,
            int declarationLine,
            int declarationCharacter,
            int usageLine,
            int usageCharacter) {
        if (!(result instanceof Map<?, ?> map)) {
            return false;
        }
        Object ranges = map.get("ranges");
        return hasRangeStartingAt(ranges, declarationLine, declarationCharacter)
                && hasRangeStartingAt(ranges, usageLine, usageCharacter);
    }

    private static boolean hasDocumentLinkTarget(Object result, String uri, int line, int character) {
        if (!(result instanceof List<?> list)) {
            return false;
        }
        for (Object item : list) {
            if (!(item instanceof Map<?, ?> link)) {
                continue;
            }
            Object target = link.get("target");
            if (target != null
                    && sameUri(uri, String.valueOf(target))
                    && hasRangeStartingAt(link, line, character)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasQuickFixRemovingImport(Object result) {
        return hasQuickFixRemovingImport(result, "Remove forbidden java import");
    }

    private static boolean hasQuickFixRemovingImport(Object result, String expectedTitle) {
        if (!(result instanceof List<?> list)) {
            return false;
        }
        for (Object item : list) {
            if (hasQuickFixEdit(item, expectedTitle)) {
                return true;
            }
        }
        return false;
    }

    private static Object quickFixAction(Object result, String expectedTitle) {
        if (!(result instanceof List<?> list)) {
            return null;
        }
        for (Object item : list) {
            if (hasQuickFixEdit(item, expectedTitle)) {
                return item;
            }
        }
        return null;
    }

    private static boolean hasQuickFixEdit(Object action, String expectedTitle) {
        if (!(action instanceof Map<?, ?> map)) {
            return false;
        }
        Object title = map.get("title");
        Object kind = map.get("kind");
        return expectedTitle.equals(String.valueOf(title))
                && "quickfix".equals(String.valueOf(kind))
                && workspaceEditTexts(map.get("edit")).contains("");
    }

    private static List<String> symbolNames(Object result) {
        if (!(result instanceof List<?> list)) {
            return List.of();
        }
        List<String> names = new ArrayList<>();
        collectSymbolNames(list, names);
        return names;
    }

    private static List<String> workspaceEditTexts(Object result) {
        if (!(result instanceof Map<?, ?> edit)) {
            return List.of();
        }
        List<String> texts = new ArrayList<>();
        Object changes = edit.get("changes");
        if (changes instanceof Map<?, ?> changesMap) {
            for (Object value : changesMap.values()) {
                collectTextEditTexts(value, texts);
            }
        }
        Object documentChanges = edit.get("documentChanges");
        if (documentChanges instanceof List<?> documentChangeList) {
            for (Object documentChange : documentChangeList) {
                if (documentChange instanceof Map<?, ?> documentChangeMap) {
                    collectTextEditTexts(documentChangeMap.get("edits"), texts);
                }
            }
        }
        return texts;
    }

    private static List<String> textEditTexts(Object result) {
        List<String> texts = new ArrayList<>();
        collectTextEditTexts(result, texts);
        return texts;
    }

    private static boolean hasInlayHintLabel(Object result, String expectedLabelPart) {
        if (!(result instanceof List<?> list)) {
            return false;
        }
        for (Object item : list) {
            if (item instanceof Map<?, ?> map && String.valueOf(map.get("label")).contains(expectedLabelPart)) {
                return true;
            }
        }
        return false;
    }

    private static void collectTextEditTexts(Object edits, List<String> texts) {
        if (!(edits instanceof List<?> editList)) {
            return;
        }
        for (Object edit : editList) {
            if (edit instanceof Map<?, ?> editMap && editMap.get("newText") != null) {
                texts.add(String.valueOf(editMap.get("newText")));
            }
        }
    }

    private static void collectSymbolNames(List<?> symbols, List<String> names) {
        for (Object symbol : symbols) {
            if (!(symbol instanceof Map<?, ?> map)) {
                continue;
            }
            if (map.get("name") != null) {
                names.add(String.valueOf(map.get("name")));
            }
            Object children = map.get("children");
            if (children instanceof List<?> childList) {
                collectSymbolNames(childList, names);
            }
        }
    }

    private static boolean sameUri(String expectedUri, String actualUri) {
        if (expectedUri.equalsIgnoreCase(actualUri)) {
            return true;
        }
        int expectedSlash = Math.max(expectedUri.lastIndexOf('/'), expectedUri.lastIndexOf('\\'));
        int actualSlash = Math.max(actualUri.lastIndexOf('/'), actualUri.lastIndexOf('\\'));
        String expectedName = expectedSlash >= 0 ? expectedUri.substring(expectedSlash + 1) : expectedUri;
        String actualName = actualSlash >= 0 ? actualUri.substring(actualSlash + 1) : actualUri;
        return expectedName.equalsIgnoreCase(actualName);
    }

    private record LanguageCase(
            String extension,
            String invalidSource,
            String expectedDiagnosticText,
            String validSource,
            String expectedCompletionLabel,
            int completionLine,
            int completionCharacter,
            String expectedHoverText,
            int hoverLine,
            int hoverCharacter,
            String expectedSignatureLabel,
            int signatureHelpLine,
            int signatureHelpCharacter,
            int definitionLine,
            int definitionCharacter,
            int referencesLine,
            int referencesCharacter,
            int expectedReferenceDeclarationLine,
            int expectedReferenceDeclarationCharacter,
            int expectedReferenceUsageLine,
            int expectedReferenceUsageCharacter,
            boolean expectCompletion,
            boolean expectDefinitionAndSymbols,
            boolean expectReferences,
            boolean expectSemanticTokens,
            String expectedDocumentSymbol) {
    }

    private static final class LspSession {
        private final Process process;
        private final BufferedWriter writer;
        private final Thread stdoutThread;
        private final Thread stderrThread;
        private final List<Map<String, Object>> messages = new ArrayList<>();
        private final StringBuilder stderr = new StringBuilder();
        private int nextId = 1;

        private LspSession(Process process) {
            this.process = process;
            this.writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8));
            this.stdoutThread = Thread.ofVirtual().start(this::readStdout);
            this.stderrThread = Thread.ofVirtual().start(this::readStderr);
        }

        int request(String method, Object params) throws IOException {
            int id = nextId++;
            Map<String, Object> message = new LinkedHashMap<>();
            message.put("jsonrpc", "2.0");
            message.put("id", id);
            message.put("method", method);
            message.put("params", params);
            send(QinLspSmokeJson.object(message));
            return id;
        }

        void notification(String method, Object params) throws IOException {
            Map<String, Object> message = new LinkedHashMap<>();
            message.put("jsonrpc", "2.0");
            message.put("method", method);
            message.put("params", params);
            send(QinLspSmokeJson.object(message));
        }

        Map<String, Object> awaitResponse(int id) {
            return await("response " + id, message -> {
                Object value = message.get("id");
                return value instanceof Number number
                        && number.intValue() == id
                        && !message.containsKey("method");
            });
        }

        void awaitDiagnostic(String uri, String expectedText) {
            await("diagnostic containing " + expectedText, message -> {
                if (!"textDocument/publishDiagnostics".equals(message.get("method"))) {
                    return false;
                }
                Object params = message.get("params");
                if (!(params instanceof Map<?, ?> paramsMap)) {
                    return false;
                }
                if (!sameDiagnosticUri(uri, String.valueOf(paramsMap.get("uri")))) {
                    return false;
                }
                Object diagnostics = paramsMap.get("diagnostics");
                if (!(diagnostics instanceof List<?> list)) {
                    return false;
                }
                for (Object item : list) {
                    if (!(item instanceof Map<?, ?> diagnostic)) {
                        continue;
                    }
                    String source = String.valueOf(diagnostic.get("source"));
                    String diagnosticMessage = String.valueOf(diagnostic.get("message"));
                    if (source.contains(expectedText) || diagnosticMessage.contains(expectedText)) {
                        return true;
                    }
                }
                return false;
            });
        }

        private boolean sameDiagnosticUri(String expectedUri, String actualUri) {
            if (expectedUri.equalsIgnoreCase(actualUri)) {
                return true;
            }
            int expectedSlash = Math.max(expectedUri.lastIndexOf('/'), expectedUri.lastIndexOf('\\'));
            int actualSlash = Math.max(actualUri.lastIndexOf('/'), actualUri.lastIndexOf('\\'));
            String expectedName = expectedSlash >= 0 ? expectedUri.substring(expectedSlash + 1) : expectedUri;
            String actualName = actualSlash >= 0 ? actualUri.substring(actualSlash + 1) : actualUri;
            return expectedName.equalsIgnoreCase(actualName);
        }

        private Map<String, Object> await(String description, MessagePredicate predicate) {
            long deadline = System.nanoTime() + TIMEOUT.toNanos();
            synchronized (messages) {
                while (System.nanoTime() < deadline) {
                    for (Map<String, Object> message : messages) {
                        if (predicate.test(message)) {
                            return message;
                        }
                    }
                    if (!process.isAlive()) {
                        throw new IllegalStateException("LSP process exited while waiting for " + description
                                + "\nstderr=" + stderr
                                + "\nmessages=" + messages);
                    }
                    try {
                        messages.wait(Duration.ofMillis(100).toMillis());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("Interrupted while waiting for " + description, e);
                    }
                }
            }
            throw new IllegalStateException("Timed out waiting for " + description
                    + "\nstderr=" + stderr
                    + "\nmessages=" + messages);
        }

        private void send(String body) throws IOException {
            writer.write("Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + "\r\n\r\n");
            writer.write(body);
            writer.flush();
        }

        private void readStdout() {
            try {
                byte[] headerSeparator = "\r\n\r\n".getBytes(StandardCharsets.UTF_8);
                while (process.isAlive()) {
                    String header = readHeader(headerSeparator);
                    if (header == null) {
                        return;
                    }
                    int contentLength = parseContentLength(header);
                    byte[] body = process.getInputStream().readNBytes(contentLength);
                    Map<String, Object> message = QinLspSmokeJson.parseObject(new String(body, StandardCharsets.UTF_8));
                    handleServerRequest(message);
                    synchronized (messages) {
                        messages.add(message);
                        messages.notifyAll();
                    }
                }
            } catch (Exception e) {
                synchronized (messages) {
                    stderr.append("\nstdout reader failed: ").append(e);
                    messages.notifyAll();
                }
            }
        }

        private void handleServerRequest(Map<String, Object> message) throws IOException {
            Object id = message.get("id");
            Object method = message.get("method");
            if (!(id instanceof Number number) || !(method instanceof String methodName)) {
                return;
            }
            if ("client/registerCapability".equals(methodName)) {
                send(response(number.intValue(), null));
            } else if ("workspace/configuration".equals(methodName)) {
                send(response(number.intValue(), configurationResponse(message)));
            }
        }

        private String response(int id, Object result) {
            Map<String, Object> message = new LinkedHashMap<>();
            message.put("jsonrpc", "2.0");
            message.put("id", id);
            message.put("result", result);
            return QinLspSmokeJson.object(message);
        }

        private List<Object> configurationResponse(Map<String, Object> message) {
            Object params = message.get("params");
            if (!(params instanceof Map<?, ?> paramsMap) || !(paramsMap.get("items") instanceof List<?> items)) {
                return List.of();
            }
            List<Object> response = new ArrayList<>();
            for (Object item : items) {
                if (item instanceof Map<?, ?> itemMap) {
                    response.add(configurationForSection(String.valueOf(itemMap.get("section"))));
                } else {
                    response.add(Map.of());
                }
            }
            return response;
        }

        private Map<String, Object> configurationForSection(String section) {
            if ("typescript".equals(section) || "javascript".equals(section)) {
                return Map.of(
                        "suggest", Map.of(
                                "autoImports", false,
                                "includeCompletionsForImportStatements", false),
                        "preferences", Map.of(
                                "includePackageJsonAutoImports", "off"),
                        "inlayHints", Map.of(
                                "parameterNames", Map.of(
                                        "enabled", "all",
                                        "suppressWhenArgumentMatchesName", false),
                                "variableTypes", Map.of(
                                        "enabled", true,
                                        "suppressWhenTypeMatchesName", false),
                                "functionLikeReturnTypes", Map.of(
                                        "enabled", true)));
            }
            return Map.of();
        }

        private String readHeader(byte[] separator) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int matched = 0;
            while (true) {
                int value = process.getInputStream().read();
                if (value < 0) {
                    return null;
                }
                out.write(value);
                if ((byte) value == separator[matched]) {
                    matched++;
                    if (matched == separator.length) {
                        return out.toString(StandardCharsets.UTF_8);
                    }
                } else {
                    matched = 0;
                }
            }
        }

        private int parseContentLength(String header) {
            for (String line : header.split("\\r?\\n")) {
                int colon = line.indexOf(':');
                if (colon > 0 && "content-length".equalsIgnoreCase(line.substring(0, colon).trim())) {
                    return Integer.parseInt(line.substring(colon + 1).trim());
                }
            }
            throw new IllegalStateException("Missing Content-Length header: " + header);
        }

        private void readStderr() {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    synchronized (messages) {
                        stderr.append(line).append('\n');
                        messages.notifyAll();
                    }
                }
            } catch (IOException e) {
                synchronized (messages) {
                    stderr.append("\nstderr reader failed: ").append(e);
                    messages.notifyAll();
                }
            }
        }
    }

    private interface MessagePredicate {
        boolean test(Map<String, Object> message);
    }
}
