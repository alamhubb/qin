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

        for (LanguageCase testCase : cases) {
            QinLspLanguage language = QinLspLanguageRegistry.fromExtension(workspaceRoot, testCase.extension());
            require(language != null, "Missing language for ." + testCase.extension());
            runLanguageCase(workspaceRoot, language, testCase);
        }

        System.out.println("Qin IDEA LSP server diagnostics smoke passed");
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
                            "textDocument", Map.of(
                                    "completion", Map.of(),
                                    "documentHighlight", Map.of(),
                                    "hover", Map.of(),
                                    "rename", Map.of(),
                                    "signatureHelp", Map.of(),
                                    "publishDiagnostics", Map.of())),
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

            Map<String, Object> rename = session.awaitResponse(session.request("textDocument/rename", Map.of(
                    "textDocument", Map.of("uri", uri),
                    "position", Map.of("line", testCase.referencesLine(), "character", testCase.referencesCharacter()),
                    "newName", "renamedSymbol")));
            require(workspaceEditTexts(rename.get("result")).contains("renamedSymbol"),
                    language.id() + " rename did not return workspace edits: " + rename);
        }

        if (testCase.expectSemanticTokens()) {
            Map<String, Object> semanticTokens = session.awaitResponse(session.request("textDocument/semanticTokens/full", Map.of(
                    "textDocument", Map.of("uri", uri))));
            Object data = resultMap(semanticTokens).get("data");
            require(data instanceof List<?> list && !list.isEmpty(),
                    language.id() + " semanticTokens returned no token data: " + semanticTokens);
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
        require(capabilitiesMap.containsKey("hoverProvider"), language.id() + " LSP missing hoverProvider");
        require(capabilitiesMap.containsKey("signatureHelpProvider"), language.id() + " LSP missing signatureHelpProvider");
        require(capabilitiesMap.containsKey("definitionProvider"), language.id() + " LSP missing definitionProvider");
        require(capabilitiesMap.containsKey("referencesProvider"), language.id() + " LSP missing referencesProvider");
        require(capabilitiesMap.containsKey("documentHighlightProvider"), language.id() + " LSP missing documentHighlightProvider");
        require(capabilitiesMap.containsKey("renameProvider"), language.id() + " LSP missing renameProvider");
        require(capabilitiesMap.containsKey("documentSymbolProvider"), language.id() + " LSP missing documentSymbolProvider");
        require(capabilitiesMap.containsKey("semanticTokensProvider"), language.id() + " LSP missing semanticTokensProvider");

        Object semanticTokensProvider = capabilitiesMap.get("semanticTokensProvider");
        require(semanticTokensProvider instanceof Map<?, ?>, language.id() + " semanticTokensProvider must be an object");
        Map<?, ?> semanticMap = (Map<?, ?>) semanticTokensProvider;
        require(semanticMap.containsKey("legend"), language.id() + " semanticTokensProvider missing legend");
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
                return value instanceof Number number && number.intValue() == id;
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
