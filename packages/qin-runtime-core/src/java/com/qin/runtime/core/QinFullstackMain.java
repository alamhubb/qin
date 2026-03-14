package com.qin.runtime.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.Executors;

/**
 * Minimal fullstack entry:
 * 1) compile backend .js -> JVM .class
 * 2) compile frontend .js -> app.js
 * 3) serve static files + API on one port
 */
public final class QinFullstackMain {
    private static final String INDEX_HTML = "index.html";
    private static final String INDEX = "index";
    private static final List<String> DEV_WATCH_EXTENSIONS = List.of(".html", ".js", ".css");
    private static final List<String> DEV_WATCH_IGNORED_DIRS = List.of(".git", ".qin", "build", "dist", "target", "node_modules", "out");

    private QinFullstackMain() {
    }

    public static void main(String[] args) throws Exception {
        Options options = parseArgs(args);
        if (options.showHelp) {
            printHelp();
            return;
        }

        BuildArtifacts artifacts = build(options);
        if (options.buildOnly) {
            System.out.println("Build only mode finished.");
            return;
        }

        if (options.dev) {
            startDevRebuildLoop(artifacts, options);
        }
        serve(artifacts, options);
    }

    private static BuildArtifacts build(Options options) throws Exception {
        Path root = resolveRoot(options.rootDir);
        QinRuntimeProjectLayout layout = QinRuntimeProjectLayout.discover(root);
        QinBuildCoordinator coordinator = new QinBuildCoordinator();

        Path backendSource = resolveBackendSource(layout, options.backendSourceFile);
        Path frontendSource = resolveFrontendSource(layout, options.frontendSourceFile);
        Path classOutputDir = resolvePath(root, options.classOutputDir);
        Path staticRoot = resolveStaticRoot(layout, root, options.staticDir);
        Path jsOutputFile = staticRoot.resolve("app.js").normalize();

        QinBuildRequest backendRequest = new QinBuildRequest(
                root,
                backendSource,
                QinBuildTarget.JVM,
                options.className,
                classOutputDir,
                jsOutputFile,
                options.printIr);
        QinBuildResult backendResult = coordinator.build(backendRequest);
        Method runMethod = loadRunMethod(classOutputDir, options.className);
        QinFrontendEsmService frontendEsmService = null;

        if (frontendSource != null) {
            frontendEsmService = QinFrontendEsmService.create(root, frontendSource);
            if (!options.dev || options.buildOnly) {
                frontendEsmService.emitProduction(staticRoot);
            }
        } else {
            Files.createDirectories(staticRoot);
            if (!Files.exists(jsOutputFile)) {
                Files.writeString(jsOutputFile, "console.log('Qin frontend source not found.');", StandardCharsets.UTF_8);
            }
        }

        ensureIndexFile(staticRoot);

        System.out.println("Project root: " + root.toAbsolutePath());
        System.out.println("Backend source: " + backendSource.toAbsolutePath());
        if (frontendSource != null) {
            System.out.println("Frontend source: " + frontendSource.toAbsolutePath());
        } else {
            System.out.println("Frontend source: <none>");
        }
        System.out.println("Generated server class: " + backendResult.classFile().toAbsolutePath());
        if (frontendSource != null) {
            if (!options.dev || options.buildOnly) {
                System.out.println("Generated frontend js: " + jsOutputFile.toAbsolutePath());
            } else {
                System.out.println("Frontend js mode: in-memory dev transform (no disk emit)");
            }
        } else {
            System.out.println("Generated frontend js: " + jsOutputFile.toAbsolutePath());
        }
        System.out.println("Static root: " + staticRoot.toAbsolutePath());

        return new BuildArtifacts(root, staticRoot, runMethod, frontendEsmService);
    }

    private static void serve(BuildArtifacts artifacts, Options options) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(options.port), 0);
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
                Object result = artifacts.currentRunMethod().invoke(null);
                sendJson(exchange, 200, toJson(result));
            } catch (Exception e) {
                String escaped = escapeJson(Objects.toString(e.getMessage(), "unknown error"));
                sendJson(exchange, 500, "{\"error\":\"" + escaped + "\"}");
            }
        });

        if (options.dev) {
            server.createContext("/@qin/version", exchange -> {
                if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
                    return;
                }
                sendText(exchange, 200, Long.toString(artifacts.version()), "text/plain; charset=utf-8");
            });
            server.createContext("/@qin/dev-client.js", exchange -> {
                if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
                    return;
                }
                sendText(exchange, 200, devClientScript(), "application/javascript; charset=utf-8");
            });
            server.createContext("/app.js", exchange -> serveFrontendBootstrap(exchange, artifacts));
            server.createContext("/@qin-mod/", exchange -> serveFrontendQinModule(exchange, artifacts));
        }

        server.createContext("/", exchange -> serveStatic(exchange, artifacts, options.dev));
        server.start();

        System.out.println("Qin fullstack server started on http://localhost:" + options.port);
        System.out.println("Health endpoint: http://localhost:" + options.port + "/api/health");
        System.out.println("Result endpoint: http://localhost:" + options.port + "/api/result");
        if (options.dev) {
            System.out.println("Dev mode enabled: browser auto-reloads when source files change.");
        }
        System.out.println("Press Ctrl+C to stop.");
    }

    private static void serveStatic(HttpExchange exchange, BuildArtifacts artifacts, boolean devMode) throws IOException {
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

        Path webRoot = artifacts.staticRoot();
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

    private static void serveFrontendBootstrap(HttpExchange exchange, BuildArtifacts artifacts) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
            return;
        }
        QinFrontendEsmService service = artifacts.frontendEsmService();
        if (service == null) {
            sendText(exchange, 404, "not found", "text/plain; charset=utf-8");
            return;
        }
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        sendText(exchange, 200, service.bootstrapJs(), "application/javascript; charset=utf-8");
    }

    private static void serveFrontendQinModule(HttpExchange exchange, BuildArtifacts artifacts) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendText(exchange, 405, "method not allowed", "text/plain; charset=utf-8");
            return;
        }
        QinFrontendEsmService service = artifacts.frontendEsmService();
        if (service == null) {
            sendText(exchange, 404, "not found", "text/plain; charset=utf-8");
            return;
        }
        String requestPath = exchange.getRequestURI() == null ? null : exchange.getRequestURI().getPath();
        String js = service.transpileByRequestPath(requestPath);
        if (js == null) {
            sendText(exchange, 404, "not found", "text/plain; charset=utf-8");
            return;
        }
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        sendText(exchange, 200, js, "application/javascript; charset=utf-8");
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

    private static Method loadRunMethod(Path classOutputDir, String className) throws Exception {
        Files.createDirectories(classOutputDir);
        URL[] urls = { classOutputDir.toUri().toURL() };
        URLClassLoader classLoader = new URLClassLoader(urls, QinFullstackMain.class.getClassLoader());
        Class<?> serverClass = Class.forName(className, true, classLoader);
        Method runMethod = serverClass.getMethod("run");
        if (!Modifier.isStatic(runMethod.getModifiers()) || runMethod.getParameterCount() != 0) {
            throw new IllegalStateException("Generated run method must be `public static Object run()`");
        }
        return runMethod;
    }

    private static Path resolveBackendSource(QinRuntimeProjectLayout layout, Path fromArgs) {
        if (fromArgs != null) {
            Path resolved = resolvePath(layout.root(), fromArgs);
            requireFile(resolved, "--backend-file");
            return resolved;
        }

        List<Path> candidates = List.of(
                layout.root().resolve("main/main.js"),
                layout.root().resolve("main/Main.js"),
                layout.root().resolve("shared/main.js"),
                layout.root().resolve("shared/shared.js"),
                layout.root().resolve("app/main.js"));
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate.toAbsolutePath().normalize();
            }
        }

        throw new IllegalArgumentException(
                "No backend .js source found. Expected one of: main/main.js, shared/main.js, shared/shared.js, app/main.js");
    }

    private static Path resolveFrontendSource(QinRuntimeProjectLayout layout, Path fromArgs) {
        if (fromArgs != null) {
            Path resolved = resolvePath(layout.root(), fromArgs);
            requireFile(resolved, "--frontend-file");
            return resolved;
        }

        List<Path> candidates = List.of(
                layout.root().resolve("app/main.js"),
                layout.root().resolve("app/Main.js"),
                layout.root().resolve("shared/main.js"),
                layout.root().resolve("shared/shared.js"));
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                return candidate.toAbsolutePath().normalize();
            }
        }

        Path appDir = layout.appDir();
        if (Files.isDirectory(appDir)) {
            try (var stream = Files.walk(appDir)) {
                return stream
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".js"))
                        .sorted(Comparator.comparing(Path::toString))
                        .map(path -> path.toAbsolutePath().normalize())
                        .findFirst()
                        .orElse(null);
            } catch (IOException ignored) {
                return null;
            }
        }
        return null;
    }

    private static Path resolveStaticRoot(QinRuntimeProjectLayout layout, Path root, Path fromArgs) throws IOException {
        Path resolved;
        if (fromArgs != null) {
            resolved = resolvePath(root, fromArgs);
        } else if (Files.isDirectory(layout.appDir())) {
            resolved = layout.appDir().toAbsolutePath().normalize();
        } else {
            resolved = root.resolve("build/fullstack/web").toAbsolutePath().normalize();
        }
        Files.createDirectories(resolved);
        return resolved;
    }

    private static void ensureIndexFile(Path staticRoot) throws IOException {
        Path indexHtml = staticRoot.resolve(INDEX_HTML);
        Path index = staticRoot.resolve(INDEX);
        if (!Files.exists(indexHtml) && !Files.exists(index)) {
            Files.writeString(indexHtml, defaultIndexHtml(), StandardCharsets.UTF_8);
        }
    }

    private static void startDevRebuildLoop(BuildArtifacts artifacts, Options options) {
        Thread thread = Thread.ofPlatform().name("qin-dev-rebuild-loop").daemon(true).start(() -> {
            long lastFingerprint = computeDevSourceFingerprint(artifacts.root());
            while (true) {
                try {
                    Thread.sleep(1000L);
                    long current = computeDevSourceFingerprint(artifacts.root());
                    if (current == lastFingerprint) {
                        continue;
                    }
                    lastFingerprint = current;
                    System.out.println("[dev] source change detected, rebuilding...");
                    BuildArtifacts rebuilt = build(options);
                    artifacts.updateFrom(rebuilt);
                    System.out.println("[dev] rebuild finished (version " + artifacts.version() + ")");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception e) {
                    System.err.println("[dev] rebuild failed: " + Objects.toString(e.getMessage(), "unknown error"));
                }
            }
        });
        System.out.println("[dev] watching source files (thread: " + thread.getName() + ")");
    }

    private static long computeDevSourceFingerprint(Path root) {
        long hash = 0xcbf29ce484222325L;
        try (var stream = Files.walk(root)) {
            List<Path> files = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> isDevSourceFile(root, path))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();

            for (Path file : files) {
                String rel = root.relativize(file).toString().replace('\\', '/');
                long modified = Files.getLastModifiedTime(file).toMillis();
                long size = Files.size(file);
                hash = mix(hash, rel.hashCode());
                hash = mix(hash, modified);
                hash = mix(hash, size);
            }
        } catch (IOException ignored) {
            // ignore and keep current hash
        }
        return hash;
    }

    private static long mix(long hash, long value) {
        long mixed = hash ^ value;
        return mixed * 0x100000001b3L;
    }

    private static boolean isDevSourceFile(Path root, Path file) {
        String rel = root.relativize(file).toString().replace('\\', '/');
        for (String ignored : DEV_WATCH_IGNORED_DIRS) {
            if (rel.startsWith(ignored + "/") || rel.contains("/" + ignored + "/")) {
                return false;
            }
        }

        String name = file.getFileName().toString().toLowerCase();
        if ("app.js".equals(name)) {
            return false;
        }
        for (String ext : DEV_WATCH_EXTENSIONS) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
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

                async function fetchVersion() {
                  const response = await fetch('/@qin/version?ts=' + Date.now(), { cache: 'no-store' });
                  if (!response.ok) return null;
                  return (await response.text()).trim();
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
                      window.location.reload();
                    }
                  } catch (_) {
                    // keep polling
                  }
                }

                setInterval(tick, POLL_INTERVAL);
                tick();
                """;
    }

    private static String defaultIndexHtml() {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Qin Fullstack MVP</title>
                  <style>
                    body { font-family: sans-serif; margin: 2rem; }
                    pre { background: #f6f8fa; padding: 1rem; border-radius: 8px; }
                  </style>
                </head>
                <body>
                  <h1>Qin Fullstack MVP</h1>
                  <p>Backend result from <code>/api/result</code>:</p>
                  <pre id="server-data">loading...</pre>
                  <script src="/app.js"></script>
                  <script>
                    fetch("/api/result")
                      .then(r => r.json())
                      .then(data => {
                        document.getElementById("server-data").textContent = JSON.stringify(data, null, 2);
                      })
                      .catch(err => {
                        document.getElementById("server-data").textContent = "error: " + err;
                      });
                  </script>
                </body>
                </html>
                """;
    }

    private static Path resolveRoot(Path rootDir) {
        if (rootDir == null) {
            return Path.of("").toAbsolutePath().normalize();
        }
        return rootDir.toAbsolutePath().normalize();
    }

    private static Path resolvePath(Path root, Path path) {
        Path p = path;
        if (!p.isAbsolute()) {
            p = root.resolve(p);
        }
        return p.toAbsolutePath().normalize();
    }

    private static void requireFile(Path file, String flag) {
        if (!Files.exists(file) || !Files.isRegularFile(file)) {
            throw new IllegalArgumentException("Missing file for " + flag + ": " + file.toAbsolutePath());
        }
    }

    private static Options parseArgs(String[] args) {
        Options options = new Options();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--help" -> options.showHelp = true;
                case "--dev" -> options.dev = true;
                case "--build-only" -> options.buildOnly = true;
                case "--print-ir" -> options.printIr = true;
                case "--port" -> options.port = parsePort(nextValue(args, ++i, "--port"));
                case "--root" -> options.rootDir = Path.of(nextValue(args, ++i, "--root"));
                case "--class" -> options.className = nextValue(args, ++i, "--class");
                case "--class-out" -> options.classOutputDir = Path.of(nextValue(args, ++i, "--class-out"));
                case "--static-dir" -> options.staticDir = Path.of(nextValue(args, ++i, "--static-dir"));
                case "--backend-file" -> options.backendSourceFile = Path.of(nextValue(args, ++i, "--backend-file"));
                case "--frontend-file" -> options.frontendSourceFile = Path.of(nextValue(args, ++i, "--frontend-file"));
                default -> throw new IllegalArgumentException("Unknown arg: " + arg);
            }
        }
        return options;
    }

    private static int parsePort(String raw) {
        int port = Integer.parseInt(raw);
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port out of range: " + port);
        }
        return port;
    }

    private static String nextValue(String[] args, int index, String flag) {
        if (index >= args.length) {
            throw new IllegalArgumentException("Missing value for " + flag);
        }
        return args[index];
    }

    private static void printHelp() {
        System.out.println("QinFullstackMain - build and serve Qin fullstack project");
        System.out.println("Usage:");
        System.out.println("  --root <path>            Project root (default: current dir)");
        System.out.println("  --port <num>             HTTP port (default: 8080)");
        System.out.println("  --dev                    Enable watch + browser auto reload");
        System.out.println("  --class <binary.name>    Generated backend class (default: com.qin.runtime.generated.ServerApp)");
        System.out.println("  --class-out <dir>        Backend class output (default: build/fullstack/server-classes)");
        System.out.println("  --static-dir <dir>       Static root (default: app/ or build/fullstack/web)");
        System.out.println("  --backend-file <file>    Backend .js source override");
        System.out.println("  --frontend-file <file>   Frontend .js source override");
        System.out.println("  --print-ir               Print IR summaries while building");
        System.out.println("  --build-only             Build outputs only");
        System.out.println("  --help                   Show help");
    }

    private static final class Options {
        private Path rootDir;
        private int port = 8080;
        private String className = "com.qin.runtime.generated.ServerApp";
        private Path classOutputDir = Path.of("build", "fullstack", "server-classes");
        private Path staticDir;
        private Path backendSourceFile;
        private Path frontendSourceFile;
        private boolean printIr;
        private boolean dev;
        private boolean buildOnly;
        private boolean showHelp;
    }

    private static final class BuildArtifacts {
        private final Path root;
        private volatile Path staticRoot;
        private final AtomicReference<Method> runMethodRef;
        private final AtomicReference<QinFrontendEsmService> frontendEsmServiceRef;
        private final AtomicLong version;

        private BuildArtifacts(Path root, Path staticRoot, Method runMethod, QinFrontendEsmService frontendEsmService) {
            this.root = root;
            this.staticRoot = staticRoot;
            this.runMethodRef = new AtomicReference<>(runMethod);
            this.frontendEsmServiceRef = new AtomicReference<>(frontendEsmService);
            this.version = new AtomicLong(System.currentTimeMillis());
        }

        private Path root() {
            return root;
        }

        private Path staticRoot() {
            return staticRoot;
        }

        private Method currentRunMethod() {
            return runMethodRef.get();
        }

        private QinFrontendEsmService frontendEsmService() {
            return frontendEsmServiceRef.get();
        }

        private long version() {
            return version.get();
        }

        private void updateFrom(BuildArtifacts rebuilt) {
            this.staticRoot = rebuilt.staticRoot;
            this.runMethodRef.set(rebuilt.currentRunMethod());
            this.frontendEsmServiceRef.set(rebuilt.frontendEsmService());
            this.version.incrementAndGet();
        }
    }
}
