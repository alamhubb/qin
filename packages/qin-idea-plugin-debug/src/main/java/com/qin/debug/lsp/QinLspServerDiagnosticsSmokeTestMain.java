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
        Path tsdkPath = QinLspLanguageRegistry.resolveTypescriptSdk(workspaceRoot);
        String node = QinLspLanguageRegistry.resolveNodeExecutable();

        List<LanguageCase> cases = List.of(
                new LanguageCase("qin", "export object Broken { value = }", "qin-parser"),
                new LanguageCase("ovs", "div { h1 { 'Broken' }", "OVS transform failed"),
                new LanguageCase("cssts", "import { css } from 'cssts-ts'\nconst broken = css { displayFlex,\n", "CSSTS transform failed"));

        for (LanguageCase testCase : cases) {
            QinLspLanguage language = QinLspLanguageRegistry.fromExtension(workspaceRoot, testCase.extension());
            require(language != null, "Missing language for ." + testCase.extension());
            runLanguageCase(workspaceRoot, tsdkPath, node, language, testCase);
        }

        System.out.println("Qin IDEA LSP server diagnostics smoke passed");
    }

    private static void runLanguageCase(
            Path workspaceRoot,
            Path tsdkPath,
            String node,
            QinLspLanguage language,
            LanguageCase testCase) throws Exception {
        Path serverPath = language.resolveServerPath(workspaceRoot);
        Process process = new ProcessBuilder(node, serverPath.toString(), "--stdio")
                .directory(language.resolveServerRoot(workspaceRoot).toFile())
                .redirectErrorStream(false)
                .start();

        LspSession session = new LspSession(process);
        try {
            int initializeId = session.request("initialize", Map.of(
                    "processId", ProcessHandle.current().pid(),
                    "capabilities", Map.of(
                            "textDocument", Map.of(
                                    "completion", Map.of(),
                                    "hover", Map.of(),
                                    "publishDiagnostics", Map.of())),
                    "rootUri", workspaceRoot.toUri().toString(),
                    "initializationOptions", Map.of(
                            "typescript", Map.of("tsdk", tsdkPath.toString()))));
            session.awaitResponse(initializeId);
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

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private record LanguageCase(String extension, String invalidSource, String expectedDiagnosticText) {
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

        void awaitResponse(int id) {
            await("response " + id, message -> {
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

        private void await(String description, MessagePredicate predicate) {
            long deadline = System.nanoTime() + TIMEOUT.toNanos();
            synchronized (messages) {
                while (System.nanoTime() < deadline) {
                    for (Map<String, Object> message : messages) {
                        if (predicate.test(message)) {
                            return;
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
