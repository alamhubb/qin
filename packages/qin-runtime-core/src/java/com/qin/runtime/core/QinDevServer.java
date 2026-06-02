package com.qin.runtime.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

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
        if (devMode) {
            QinRawDevServer.serve(runtime, port);
            return;
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newCachedThreadPool());
        QinHmrBroadcaster hmrBroadcaster = new QinHmrBroadcaster(runtime);

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
            server.createContext("/@qin/plugin-vue-export-helper.js", exchange -> {
                if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
                    return;
                }
                sendText(exchange, 200, pluginVueExportHelperScript(), "application/javascript; charset=utf-8");
            });
            server.createContext("/@qin/hmr", hmrBroadcaster::handleWebSocket);
            server.createContext("/app.js", exchange -> serveFrontendBootstrap(exchange, runtime));
            server.createContext("/@qin-mod/", exchange -> serveFrontendQinModule(exchange, runtime));
        }

        server.createContext("/", exchange -> serveStatic(exchange, runtime, devMode));
        server.start();
        if (devMode) {
            hmrBroadcaster.start();
        }

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
            String frontendModule = serveFrontendPublicModuleIfNeeded(exchange, runtime);
            if (frontendModule != null) {
                exchange.getResponseHeaders().set("Cache-Control", "no-store");
                sendText(exchange, 200, frontendModule, "application/javascript; charset=utf-8");
                return;
            }
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

    private static String serveFrontendPublicModuleIfNeeded(HttpExchange exchange, RuntimeView runtime) throws IOException {
        QinFrontendEsmService service = runtime.frontendEsmService();
        if (service == null) {
            return null;
        }
        URI uri = exchange.getRequestURI();
        String path = uri == null ? null : uri.getPath();
        if (!isPublicFrontendScriptRequest(path)) {
            return null;
        }
        return service.transpileByPublicRequestPath(frontendModuleRequestPath(uri));
    }

    private static boolean isPublicFrontendScriptRequest(String path) {
        if (path == null) {
            return false;
        }
        String lower = path.toLowerCase();
        return lower.endsWith(".js")
                || lower.endsWith(".mjs")
                || lower.endsWith(".ts")
                || lower.endsWith(".qin")
                || lower.endsWith(".vue")
                || lower.endsWith(".ovs");
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
        Path publicFile = webRoot.resolve("public").resolve(clean).normalize();
        if (Files.exists(publicFile) && !Files.isDirectory(publicFile)) {
            return publicFile;
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
        if (name.endsWith(".png")) {
            return "image/png";
        }
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (name.endsWith(".gif")) {
            return "image/gif";
        }
        if (name.endsWith(".webp")) {
            return "image/webp";
        }
        if (name.endsWith(".ico")) {
            return "image/x-icon";
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
                const hotCallbacks = new Map();
                const hotData = new Map();
                const hotEventCallbacks = new Map();
                let currentVersion = null;
                let entryModuleUrl = null;

                export function createHotContext(ownerPath) {
                  const normalizedOwnerPath = stripQinHmr(ownerPath);
                  const data = hotData.get(normalizedOwnerPath) || {};
                  hotData.set(normalizedOwnerPath, data);
                  return {
                    data,
                    accept(callback) {
                      hotCallbacks.set(normalizedOwnerPath, typeof callback === 'function' ? callback : null);
                    },
                    dispose(callback) {
                      if (typeof callback === 'function') callback(data);
                    },
                    on(event, callback) {
                      if (typeof callback !== 'function') return;
                      const key = String(event || '');
                      const callbacks = hotEventCallbacks.get(key) || [];
                      callbacks.push(callback);
                      hotEventCallbacks.set(key, callbacks);
                    },
                    invalidate(message) {
                      console.warn('[Qin HMR] invalidated', normalizedOwnerPath, message || '');
                      window.location.reload();
                    }
                  };
                }

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

                function stripQinHmr(url) {
                  return url
                    .replace(/[?&]qin-hmr=[^&]+/g, '')
                    .replace(/[?&]$/, '');
                }

                async function importIfAvailable(url) {
                  try {
                    return await import(url);
                  } catch (error) {
                    if (String(error && error.message || error).includes('not found')) return null;
                    throw error;
                  }
                }

                async function applyQinHmr(nextVersion) {
                  entryModuleUrl = entryModuleUrl || await fetchEntryModuleUrl();
                  if (!entryModuleUrl) {
                    window.location.reload();
                    return;
                  }
                  try {
                    const module = await import(withQinHmr(entryModuleUrl, nextVersion));
                    await importIfAvailable(withQinHmr(entryModuleUrl + '?qin-vue-cssts=style', nextVersion));
                    const callback = hotCallbacks.get(stripQinHmr(entryModuleUrl));
                    if (typeof callback === 'function') {
                      callback(module);
                      console.info('[Qin HMR] accepted', entryModuleUrl);
                      return;
                    }
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

                function handleVersion(next) {
                  if (!next) return;
                  if (currentVersion === null) {
                    currentVersion = String(next);
                    return;
                  }
                  if (String(next) !== currentVersion) {
                    currentVersion = String(next);
                    const callbacks = hotEventCallbacks.get('file-changed') || [];
                    for (const callback of callbacks) {
                      try { callback({ file: entryModuleUrl || '' }); } catch (_) {}
                    }
                    applyQinHmr(currentVersion);
                  }
                }

                async function tick() {
                  try {
                    const next = await fetchVersion();
                    handleVersion(next);
                  } catch (_) {
                    // keep polling
                  }
                }

                function startPollingFallback() {
                  setInterval(tick, POLL_INTERVAL);
                  tick();
                }

                function connectWebSocket() {
                  try {
                    const protocol = location.protocol === 'https:' ? 'wss:' : 'ws:';
                    const socket = new WebSocket(protocol + '//' + location.host + '/@qin/hmr');
                    socket.addEventListener('open', () => console.info('[Qin HMR] websocket connected'));
                    socket.addEventListener('message', event => {
                      try {
                        const payload = JSON.parse(event.data);
                        if (payload && payload.type === 'connected') {
                          handleVersion(payload.version);
                        } else if (payload && payload.type === 'update') {
                          handleVersion(payload.version);
                        } else if (payload && payload.type === 'full-reload') {
                          window.location.reload();
                        }
                      } catch (error) {
                        console.warn('[Qin HMR] invalid websocket payload', error);
                      }
                    });
                    socket.addEventListener('close', startPollingFallback);
                    socket.addEventListener('error', startPollingFallback);
                  } catch (_) {
                    startPollingFallback();
                  }
                }

                connectWebSocket();
                """;
    }

    private static String pluginVueExportHelperScript() {
        return """
                export default function _export_sfc(component, props) {
                  for (const [key, value] of props || []) {
                    component[key] = value;
                  }
                  return component;
                }
                """;
    }

    private static final class QinRawDevServer {
        private QinRawDevServer() {
        }

        private static void serve(RuntimeView runtime, int port) throws IOException {
            QinHmrBroadcaster hmrBroadcaster = new QinHmrBroadcaster(runtime);
            hmrBroadcaster.start();
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));

            System.out.println("Qin dev server started on http://localhost:" + port);
            System.out.println("Health endpoint: http://localhost:" + port + "/api/health");
            System.out.println("Result endpoint: http://localhost:" + port + "/api/result");
            System.out.println("Dev mode enabled: browser applies Qin HMR updates when possible, then falls back to reload.");
            System.out.println("HMR websocket endpoint: ws://localhost:" + port + "/@qin/hmr");
            System.out.println("Press Ctrl+C to stop.");

            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(() -> handle(socket, runtime, hmrBroadcaster), "qin-raw-dev-client");
                thread.setDaemon(false);
                thread.start();
            }
        }

        private static void handle(Socket socket, RuntimeView runtime, QinHmrBroadcaster hmrBroadcaster) {
            try (socket) {
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                RawRequest request = readRequest(input);
                if (request == null) {
                    return;
                }
                URI uri = URI.create(request.target());
                String path = uri.getPath();
                if ("/@qin/hmr".equals(path) && "websocket".equalsIgnoreCase(request.headers().get("upgrade"))) {
                    handleWebSocket(input, output, request, hmrBroadcaster);
                    return;
                }
                handleHttp(output, runtime, request, uri);
            } catch (Exception error) {
                error.printStackTrace(System.err);
            }
        }

        private static void handleHttp(OutputStream output, RuntimeView runtime, RawRequest request, URI uri) throws IOException {
            if (!"GET".equalsIgnoreCase(request.method())) {
                sendRawText(output, 405, "Method Not Allowed", "method not allowed", "text/plain; charset=utf-8", Map.of());
                return;
            }
            String path = uri.getPath();
            if ("/api/health".equals(path)) {
                sendRawText(output, 200, "OK", "{\"ok\":true}", "application/json; charset=utf-8", Map.of());
                return;
            }
            if ("/api/result".equals(path)) {
                try {
                    Object result = runtime.currentRunMethod().invoke(null);
                    sendRawText(output, 200, "OK", toJson(result), "application/json; charset=utf-8", Map.of());
                } catch (Exception error) {
                    Throwable root = unwrapInvocationError(error);
                    root.printStackTrace(System.err);
                    String escaped = escapeJson(Objects.toString(root.getMessage(), root.getClass().getName()));
                    sendRawText(output, 500, "Internal Server Error", "{\"error\":\"" + escaped + "\"}", "application/json; charset=utf-8", Map.of());
                }
                return;
            }
            if ("/@qin/version".equals(path)) {
                sendRawText(output, 200, "OK", Long.toString(runtime.version()), "text/plain; charset=utf-8", noStore());
                return;
            }
            if ("/@qin/dev-client.js".equals(path)) {
                sendRawText(output, 200, "OK", devClientScript(), "application/javascript; charset=utf-8", noStore());
                return;
            }
            if ("/@qin/plugin-vue-export-helper.js".equals(path)) {
                sendRawText(output, 200, "OK", pluginVueExportHelperScript(), "application/javascript; charset=utf-8", noStore());
                return;
            }
            if ("/app.js".equals(path)) {
                QinFrontendEsmService service = runtime.frontendEsmService();
                if (service == null) {
                    sendRawText(output, 404, "Not Found", "not found", "text/plain; charset=utf-8", noStore());
                } else {
                    sendRawText(output, 200, "OK", service.bootstrapJs(), "application/javascript; charset=utf-8", noStore());
                }
                return;
            }
            if (path != null && path.startsWith("/@qin-mod/")) {
                QinFrontendEsmService service = runtime.frontendEsmService();
                if (service == null) {
                    sendRawText(output, 404, "Not Found", "not found", "text/plain; charset=utf-8", noStore());
                    return;
                }
                String js = service.transpileByRequestPath(frontendModuleRequestPath(uri));
                if (js == null) {
                    sendRawText(output, 404, "Not Found", "not found", "text/plain; charset=utf-8", noStore());
                    return;
                }
                sendRawText(output, 200, "OK", js, "application/javascript; charset=utf-8", noStore());
                return;
            }
            QinFrontendEsmService service = runtime.frontendEsmService();
            if (service != null && isPublicFrontendScriptRequest(path)) {
                String js = service.transpileByPublicRequestPath(frontendModuleRequestPath(uri));
                if (js != null) {
                    sendRawText(output, 200, "OK", js, "application/javascript; charset=utf-8", noStore());
                    return;
                }
            }
            serveRawStatic(output, runtime, uri);
        }

        private static void serveRawStatic(OutputStream output, RuntimeView runtime, URI uri) throws IOException {
            String rawPath = uri.getPath();
            String requestPath = rawPath == null || rawPath.isBlank() ? "/" : rawPath;
            String decoded = URLDecoder.decode(requestPath, StandardCharsets.UTF_8);
            String relative = decoded.startsWith("/") ? decoded.substring(1) : decoded;

            Path webRoot = runtime.staticRoot();
            Path resolved = resolveStaticFile(webRoot, relative);
            Path normalizedRoot = webRoot.toAbsolutePath().normalize();
            Path normalizedResolved = resolved.toAbsolutePath().normalize();
            if (!normalizedResolved.startsWith(normalizedRoot)) {
                sendRawText(output, 403, "Forbidden", "forbidden", "text/plain; charset=utf-8", noStore());
                return;
            }
            if (!Files.exists(normalizedResolved)) {
                sendRawText(output, 404, "Not Found", "not found", "text/plain; charset=utf-8", noStore());
                return;
            }
            byte[] bytes = injectDevClientIfNeeded(normalizedResolved, Files.readAllBytes(normalizedResolved));
            sendRawBytes(output, 200, "OK", bytes, contentType(normalizedResolved), noStore());
        }

        private static void handleWebSocket(
                InputStream input,
                OutputStream output,
                RawRequest request,
                QinHmrBroadcaster hmrBroadcaster) throws IOException {
            String key = request.headers().get("sec-websocket-key");
            if (key == null || key.isBlank()) {
                sendRawText(output, 400, "Bad Request", "missing Sec-WebSocket-Key", "text/plain; charset=utf-8", Map.of());
                return;
            }
            String response = "HTTP/1.1 101 Switching Protocols\r\n"
                    + "Upgrade: websocket\r\n"
                    + "Connection: Upgrade\r\n"
                    + "Sec-WebSocket-Accept: " + QinHmrBroadcaster.websocketAccept(key) + "\r\n"
                    + "\r\n";
            output.write(response.getBytes(StandardCharsets.UTF_8));
            output.flush();
            hmrBroadcaster.register(output);
            try {
                while (readWebSocketFrame(input)) {
                    // Qin currently only needs server -> browser update frames.
                }
            } finally {
                hmrBroadcaster.unregister(output);
            }
        }

        private static boolean readWebSocketFrame(InputStream input) throws IOException {
            int first = input.read();
            if (first < 0) {
                return false;
            }
            int second = input.read();
            if (second < 0) {
                return false;
            }
            int opcode = first & 0x0F;
            int length = second & 0x7F;
            if (length == 126) {
                length = (input.read() << 8) | input.read();
            } else if (length == 127) {
                long longLength = 0;
                for (int i = 0; i < 8; i++) {
                    longLength = (longLength << 8) | input.read();
                }
                if (longLength > Integer.MAX_VALUE) {
                    throw new IOException("WebSocket frame too large: " + longLength);
                }
                length = (int) longLength;
            }
            boolean masked = (second & 0x80) != 0;
            byte[] mask = new byte[4];
            if (masked && input.readNBytes(mask, 0, mask.length) != mask.length) {
                return false;
            }
            int remaining = length;
            byte[] skip = new byte[Math.min(8192, Math.max(1, length))];
            while (remaining > 0) {
                int read = input.read(skip, 0, Math.min(skip.length, remaining));
                if (read < 0) {
                    return false;
                }
                remaining -= read;
            }
            return opcode != 0x8;
        }

        private static RawRequest readRequest(InputStream input) throws IOException {
            String headerText = readHeaderText(input);
            if (headerText == null || headerText.isBlank()) {
                return null;
            }
            String[] lines = headerText.split("\\r?\\n");
            if (lines.length == 0) {
                return null;
            }
            String[] requestLine = lines[0].split("\\s+", 3);
            if (requestLine.length < 2) {
                return null;
            }
            Map<String, String> headers = new LinkedHashMap<>();
            for (int i = 1; i < lines.length; i++) {
                int colon = lines[i].indexOf(':');
                if (colon > 0) {
                    headers.put(
                            lines[i].substring(0, colon).trim().toLowerCase(),
                            lines[i].substring(colon + 1).trim());
                }
            }
            return new RawRequest(requestLine[0], requestLine[1], headers);
        }

        private static String readHeaderText(InputStream input) throws IOException {
            byte[] buffer = new byte[8192];
            int count = 0;
            int matched = 0;
            byte[] end = new byte[] {'\r', '\n', '\r', '\n'};
            while (count < buffer.length) {
                int value = input.read();
                if (value < 0) {
                    break;
                }
                buffer[count++] = (byte) value;
                if (value == end[matched]) {
                    matched++;
                    if (matched == end.length) {
                        return new String(buffer, 0, count, StandardCharsets.UTF_8);
                    }
                } else {
                    matched = value == end[0] ? 1 : 0;
                }
            }
            return count == 0 ? null : new String(buffer, 0, count, StandardCharsets.UTF_8);
        }

        private static void sendRawText(
                OutputStream output,
                int status,
                String reason,
                String body,
                String contentType,
                Map<String, String> headers) throws IOException {
            sendRawBytes(output, status, reason, body.getBytes(StandardCharsets.UTF_8), contentType, headers);
        }

        private static void sendRawBytes(
                OutputStream output,
                int status,
                String reason,
                byte[] body,
                String contentType,
                Map<String, String> headers) throws IOException {
            StringBuilder response = new StringBuilder();
            response.append("HTTP/1.1 ").append(status).append(' ').append(reason).append("\r\n");
            response.append("Content-Type: ").append(contentType).append("\r\n");
            response.append("Content-Length: ").append(body.length).append("\r\n");
            response.append("Connection: close\r\n");
            for (Map.Entry<String, String> header : headers.entrySet()) {
                response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
            }
            response.append("\r\n");
            output.write(response.toString().getBytes(StandardCharsets.UTF_8));
            output.write(body);
            output.flush();
        }

        private static Map<String, String> noStore() {
            return Map.of("Cache-Control", "no-store");
        }

        private record RawRequest(String method, String target, Map<String, String> headers) {
        }
    }

    private static final class QinHmrBroadcaster {
        private static final String WEBSOCKET_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

        private final RuntimeView runtime;
        private final Set<OutputStream> clients = ConcurrentHashMap.newKeySet();
        private final AtomicLong observedVersion;

        private QinHmrBroadcaster(RuntimeView runtime) {
            this.runtime = runtime;
            this.observedVersion = new AtomicLong(runtime.version());
        }

        private void start() {
            Thread thread = new Thread(this::watchVersions, "qin-hmr-version-broadcaster");
            thread.setDaemon(true);
            thread.start();
        }

        private void watchVersions() {
            while (true) {
                try {
                    long next = runtime.version();
                    long previous = observedVersion.getAndSet(next);
                    if (next != previous) {
                        broadcast("{\"type\":\"update\",\"version\":" + next + "}");
                    }
                    Thread.sleep(250);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception error) {
                    error.printStackTrace(System.err);
                }
            }
        }

        private void handleWebSocket(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
                return;
            }
            String key = exchange.getRequestHeaders().getFirst("Sec-WebSocket-Key");
            if (key == null || key.isBlank()) {
                sendText(exchange, 400, "missing Sec-WebSocket-Key", "text/plain; charset=utf-8");
                return;
            }
            exchange.getResponseHeaders().set("Upgrade", "websocket");
            exchange.getResponseHeaders().set("Connection", "Upgrade");
            exchange.getResponseHeaders().set("Sec-WebSocket-Accept", websocketAccept(key));
            exchange.sendResponseHeaders(101, 0);

            OutputStream output = exchange.getResponseBody();
            clients.add(output);
            send(output, "{\"type\":\"connected\",\"version\":" + runtime.version() + "}");
            try (InputStream input = exchange.getRequestBody()) {
                while (input.read() != -1) {
                    // Keep the upgraded connection open. Qin does not need client frames yet.
                }
            } finally {
                clients.remove(output);
                exchange.close();
            }
        }

        private void broadcast(String message) {
            for (OutputStream client : clients) {
                try {
                    send(client, message);
                } catch (IOException error) {
                    clients.remove(client);
                    try {
                        client.close();
                    } catch (IOException closeError) {
                        error.addSuppressed(closeError);
                    }
                }
            }
        }

        private void register(OutputStream output) throws IOException {
            clients.add(output);
            send(output, "{\"type\":\"connected\",\"version\":" + runtime.version() + "}");
        }

        private void unregister(OutputStream output) {
            clients.remove(output);
        }

        private static void send(OutputStream output, String message) throws IOException {
            byte[] payload = message.getBytes(StandardCharsets.UTF_8);
            synchronized (output) {
                output.write(0x81);
                if (payload.length < 126) {
                    output.write(payload.length);
                } else if (payload.length <= 0xFFFF) {
                    output.write(126);
                    output.write((payload.length >>> 8) & 0xFF);
                    output.write(payload.length & 0xFF);
                } else {
                    output.write(127);
                    for (int shift = 56; shift >= 0; shift -= 8) {
                        output.write((payload.length >>> shift) & 0xFF);
                    }
                }
                output.write(payload);
                output.flush();
            }
        }

        private static String websocketAccept(String key) {
            try {
                MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                byte[] digest = sha1.digest((key.trim() + WEBSOCKET_GUID).getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(digest);
            } catch (NoSuchAlgorithmException error) {
                throw new IllegalStateException("SHA-1 is required for WebSocket handshake", error);
            }
        }
    }

    interface RuntimeView {
        Path staticRoot();

        Method currentRunMethod();

        QinFrontendEsmService frontendEsmService();

        long version();
    }
}
