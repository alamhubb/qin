package com.qin.runtime.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * Stage-1 single-process Qin dev/fullstack server.
 *
 * <p>The server hosts:
 * - backend API execution
 * - static HTML/CSS/JS assets
 * - Qin frontend bootstrap/module endpoints in dev mode
 * - browser auto-reload support for rebuild-based development
 */
final class QinDevServer {
    private static final String INDEX_HTML = "index.html";
    private static final String INDEX = "index";

    private QinDevServer() {
    }

    static void serve(RuntimeView runtime, boolean devMode, int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext("/api/health", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 405, "{\"error\":\"method not allowed\"}");
                return;
            }
            sendJson(exchange, 200, "{\"ok\":true}");
        });

        server.createContext("/api/result", exchange -> {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendJson(exchange, 405, "{\"error\":\"method not allowed\"}");
                return;
            }
            try {
                Object result = runtime.currentRunMethod().invoke(null);
                sendJson(exchange, 200, toJson(result));
            } catch (Exception e) {
                Throwable root = unwrapInvocationError(e);
                root.printStackTrace(System.err);
                String escaped = escapeJson(Objects.toString(root.getMessage(), root.getClass().getName()));
                sendJson(exchange, 500, "{\"error\":\"" + escaped + "\"}");
            }
        });

        if (devMode) {
            server.createContext("/@qin/version", exchange -> {
                if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
                    return;
                }
                sendText(exchange, 200, Long.toString(runtime.version()), "text/plain; charset=utf-8");
            });
            server.createContext("/@qin/dev-client.js", exchange -> {
                if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
                    return;
                }
                sendText(exchange, 200, devClientScript(), "application/javascript; charset=utf-8");
            });
            server.createContext("/app.js", exchange -> serveFrontendBootstrap(exchange, runtime));
            server.createContext("/@qin-mod/", exchange -> serveFrontendQinModule(exchange, runtime));
        }

        server.createContext("/", exchange -> serveStatic(exchange, runtime, devMode));
        server.start();

        System.out.println("Qin dev server started on http://localhost:" + port);
        System.out.println("Health endpoint: http://localhost:" + port + "/api/health");
        System.out.println("Result endpoint: http://localhost:" + port + "/api/result");
        if (devMode) {
            System.out.println("Dev mode enabled: browser applies Qin HMR updates when possible, then falls back to reload.");
        }
        System.out.println("Press Ctrl+C to stop.");
    }

    private static void serveStatic(HttpExchange exchange, RuntimeView runtime, boolean devMode) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().set("Allow", "GET");
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        String rawPath = exchange.getRequestURI().getPath();
        String requestPath = rawPath == null || rawPath.isBlank() ? "/" : rawPath;
        String decoded = URLDecoder.decode(requestPath, StandardCharsets.UTF_8);
        String relative = decoded.startsWith("/") ? decoded.substring(1) : decoded;

        Path webRoot = runtime.staticRoot();
        Path resolved = resolveStaticFile(webRoot, relative);
        Path normalizedRoot = webRoot.toAbsolutePath().normalize();
        Path normalizedResolved = resolved.toAbsolutePath().normalize();
        if (!normalizedResolved.startsWith(normalizedRoot)) {
            sendText(exchange, 403, "forbidden", "text/plain; charset=utf-8");
            return;
        }

        if (!Files.exists(normalizedResolved)) {
            sendText(exchange, 404, "not found", "text/plain; charset=utf-8");
            return;
        }

        byte[] bytes = Files.readAllBytes(normalizedResolved);
        if (devMode) {
            bytes = injectDevClientIfNeeded(normalizedResolved, bytes);
            exchange.getResponseHeaders().set("Cache-Control", "no-store");
        }
        exchange.getResponseHeaders().set("Content-Type", contentType(normalizedResolved));
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
        exchange.close();
    }

    private static void serveFrontendBootstrap(HttpExchange exchange, RuntimeView runtime) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
            return;
        }
        QinFrontendEsmService service = runtime.frontendEsmService();
        if (service == null) {
            sendText(exchange, 404, "not found", "text/plain; charset=utf-8");
            return;
        }
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        sendText(exchange, 200, service.bootstrapJs(), "application/javascript; charset=utf-8");
    }

    private static void serveFrontendQinModule(HttpExchange exchange, RuntimeView runtime) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
            return;
        }
        QinFrontendEsmService service = runtime.frontendEsmService();
        if (service == null) {
            sendText(exchange, 404, "not found", "text/plain; charset=utf-8");
            return;
        }
        String requestPath = frontendModuleRequestPath(exchange);
        String js = service.transpileByRequestPath(requestPath);
        if (js == null) {
            sendText(exchange, 404, "not found", "text/plain; charset=utf-8");
            return;
        }
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        sendText(exchange, 200, js, "application/javascript; charset=utf-8");
    }

    private static String frontendModuleRequestPath(HttpExchange exchange) {
        return frontendModuleRequestPath(exchange.getRequestURI());
    }

    static String frontendModuleRequestPath(URI uri) {
        if (uri == null) {
            return null;
        }
        String path = uri.getPath();
        String query = uri.getRawQuery();
        if (query == null || query.isBlank()) {
            return path;
        }
        return path + "?" + query;
    }

    private static Path resolveStaticFile(Path webRoot, String relativePath) {
        String normalized = relativePath == null ? "" : relativePath.trim();
        if (normalized.isEmpty() || "/".equals(normalized)) {
            return resolveIndexFile(webRoot);
        }

        String clean = normalized.startsWith("/") ? normalized.substring(1) : normalized;
        if (clean.isEmpty() || INDEX.equals(clean)) {
            return resolveIndexFile(webRoot);
        }

        Path direct = webRoot.resolve(clean).normalize();
        if (Files.exists(direct) && !Files.isDirectory(direct)) {
            return direct;
        }
        if (Files.isDirectory(direct)) {
            Path dirIndex = resolveIndexFile(direct);
            if (Files.exists(dirIndex)) {
                return dirIndex;
            }
        }

        if (!clean.contains(".")) {
            Path html = webRoot.resolve(clean + ".html").normalize();
            if (Files.exists(html) && !Files.isDirectory(html)) {
                return html;
            }
        }

        return resolveIndexFile(webRoot);
    }

    private static Path resolveIndexFile(Path root) {
        Path indexHtml = root.resolve(INDEX_HTML).normalize();
        if (Files.exists(indexHtml) && !Files.isDirectory(indexHtml)) {
            return indexHtml;
        }

        Path index = root.resolve(INDEX).normalize();
        if (Files.exists(index) && !Files.isDirectory(index)) {
            return index;
        }

        return indexHtml;
    }

    private static String contentType(Path file) {
        String name = file.getFileName().toString().toLowerCase();
        if (name.endsWith(".html")) {
            return "text/html; charset=utf-8";
        }
        if (name.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        if (name.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (name.endsWith(".json")) {
            return "application/json; charset=utf-8";
        }
        if (name.endsWith(".svg")) {
            return "image/svg+xml";
        }
        return "application/octet-stream";
    }

    private static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        sendText(exchange, status, json, "application/json; charset=utf-8");
    }

    private static void sendText(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
        exchange.close();
    }

    @SuppressWarnings("unchecked")
    private static String toJson(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof String s) {
            return "\"" + escapeJson(s) + "\"";
        }
        if (value instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : ((Map<Object, Object>) map).entrySet()) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append("\"").append(escapeJson(String.valueOf(entry.getKey()))).append("\":")
                        .append(toJson(entry.getValue()));
            }
            sb.append("}");
            return sb.toString();
        }
        if (value instanceof Iterable<?> iterable) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            boolean first = true;
            for (Object item : iterable) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(toJson(item));
            }
            sb.append("]");
            return sb.toString();
        }
        return "\"" + escapeJson(String.valueOf(value)) + "\"";
    }

    private static String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static Throwable unwrapInvocationError(Throwable error) {
        Throwable current = error;
        while (current instanceof InvocationTargetException invocationTargetException
                && invocationTargetException.getCause() != null) {
            current = invocationTargetException.getCause();
        }
        return current;
    }

    private static byte[] injectDevClientIfNeeded(Path file, byte[] bytes) {
        String name = file.getFileName().toString().toLowerCase();
        if (!name.endsWith(".html") && !name.equals("index")) {
            return bytes;
        }

        String html = new String(bytes, StandardCharsets.UTF_8);
        if (html.contains("/@qin/dev-client.js")) {
            return bytes;
        }

        String scriptTag = "\n<script type=\"module\" src=\"/@qin/dev-client.js\"></script>\n";
        int bodyClose = html.toLowerCase().lastIndexOf("</body>");
        String withClient = bodyClose >= 0
                ? html.substring(0, bodyClose) + scriptTag + html.substring(bodyClose)
                : html + scriptTag;
        return withClient.getBytes(StandardCharsets.UTF_8);
    }

    private static String devClientScript() {
        return """
                const POLL_INTERVAL = 1000;
                let currentVersion = null;
                let entryModuleUrl = null;

                async function fetchVersion() {
                  const response = await fetch('/@qin/version?ts=' + Date.now(), { cache: 'no-store' });
                  if (!response.ok) return null;
                  return (await response.text()).trim();
                }

                async function fetchEntryModuleUrl() {
                  const response = await fetch('/app.js?ts=' + Date.now(), { cache: 'no-store' });
                  if (!response.ok) return null;
                  const source = await response.text();
                  const match = source.match(/import\\s*["']([^"']+)["']/);
                  return match ? match[1] : null;
                }

                function withQinHmr(url, version) {
                  const separator = url.includes('?') ? '&' : '?';
                  return url + separator + 'qin-hmr=' + encodeURIComponent(version);
                }

                async function applyQinHmr(nextVersion) {
                  entryModuleUrl = entryModuleUrl || await fetchEntryModuleUrl();
                  if (!entryModuleUrl) {
                    window.location.reload();
                    return;
                  }
                  try {
                    const module = await import(withQinHmr(entryModuleUrl, nextVersion));
                    if (module && typeof module.__qinMountVue === 'function') {
                      module.__qinMountVue();
                      console.info('[Qin HMR] updated', entryModuleUrl);
                      return;
                    }
                    window.location.reload();
                  } catch (error) {
                    console.warn('[Qin HMR] update failed, reloading page', error);
                    window.location.reload();
                  }
                }

                async function tick() {
                  try {
                    const next = await fetchVersion();
                    if (!next) return;
                    if (currentVersion === null) {
                      currentVersion = next;
                      return;
                    }
                    if (next !== currentVersion) {
                      currentVersion = next;
                      await applyQinHmr(next);
                    }
                  } catch (_) {
                    // keep polling
                  }
                }

                setInterval(tick, POLL_INTERVAL);
                tick();
                """;
    }

    interface RuntimeView {
        Path staticRoot();

        Method currentRunMethod();

        QinFrontendEsmService frontendEsmService();

        long version();
    }
}
