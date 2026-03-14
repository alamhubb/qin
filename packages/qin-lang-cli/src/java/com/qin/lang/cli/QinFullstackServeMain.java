package com.qin.lang.cli;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.backend.jvm.QinClassFileWriter;
import com.qin.lang.backend.jvm.QinJvmClassFileBackend;
import com.qin.lang.frontend.adapter.QinSlimeFrontendAdapter;
import com.qin.lang.ir.QinIrProgram;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * Fullstack serve mode:
 * 1) shared + server -> generated .class
 * 2) shared + web -> generated app.js
 * 3) serve static files + api endpoint via JDK HttpServer
 */
public final class QinFullstackServeMain {
    private static final String APP_DIR = "app";
    private static final String INDEX_HTML = "index.html";
    private static final String INDEX = "index";

    private QinFullstackServeMain() {
    }

    public static void main(String[] args) throws Exception {
        Options options = parseArgs(args);
        if (options.showHelp) {
            printHelp();
            return;
        }

        Path demoRoot = resolveDemoRoot(options.demoRoot);
        BuildArtifacts artifacts = buildArtifacts(demoRoot);

        if (options.buildOnly) {
            System.out.println("Build only mode finished.");
            return;
        }

        serve(artifacts, options.port);
    }

    private static BuildArtifacts buildArtifacts(Path demoRoot) throws Exception {
        Path sharedFile = demoRoot.resolve("packages/shared/src/qin/shared.js");
        Path serverFile = demoRoot.resolve("packages/server/src/qin/server.js");
        Path webFile = demoRoot.resolve("packages/web/src/qin/web.js");

        validateInputFile(sharedFile, "shared");
        validateInputFile(serverFile, "server");
        validateInputFile(webFile, "web");

        String sharedSource = Files.readString(sharedFile, StandardCharsets.UTF_8).trim();
        String serverSource = Files.readString(serverFile, StandardCharsets.UTF_8).trim();
        String webSource = Files.readString(webFile, StandardCharsets.UTF_8).trim();

        String serverProgramSource = sharedSource + System.lineSeparator() + System.lineSeparator() + serverSource;
        String webProgramSource = sharedSource + System.lineSeparator() + System.lineSeparator() + webSource;

        QinSlimeFrontendAdapter adapter = new QinSlimeFrontendAdapter();
        QinIrProgram serverProgram = adapter.parseProgram(serverProgramSource);
        QinIrProgram webProgram = adapter.parseProgram(webProgramSource);

        Path buildDir = demoRoot.resolve("build");
        Path serverOutputDir = buildDir.resolve("server-classes");
        Path webOutputDir = buildDir.resolve("web");
        Files.createDirectories(serverOutputDir);
        Files.createDirectories(webOutputDir);

        String serverClassName = "com.qin.demo.ServerApp";
        QinJvmClassFileBackend jvmBackend = new QinJvmClassFileBackend();
        byte[] classBytes = jvmBackend.compileProgram(serverProgram, serverClassName);
        Path classFile = QinClassFileWriter.writeClassFile(serverOutputDir, serverClassName, classBytes);

        QinJsBackend jsBackend = new QinJsBackend();
        String jsCode = jsBackend.compileProgram(webProgram);
        Path appStaticDir = demoRoot.resolve(APP_DIR);
        Path staticRoot = Files.isDirectory(appStaticDir) ? appStaticDir : webOutputDir;
        Files.createDirectories(staticRoot);

        Path jsFile = staticRoot.resolve("app.js");
        Files.writeString(jsFile, jsCode, StandardCharsets.UTF_8);

        Path indexHtmlFile = staticRoot.resolve(INDEX_HTML);
        Path indexFile = staticRoot.resolve(INDEX);
        if (!Files.exists(indexHtmlFile) && !Files.exists(indexFile)) {
            Files.writeString(indexHtmlFile, defaultIndexHtml(), StandardCharsets.UTF_8);
        }

        Class<?> generatedClass = new ByteArrayClassLoader(QinFullstackServeMain.class.getClassLoader())
                .define(serverClassName, classBytes);
        Method runMethod = generatedClass.getMethod("run");

        System.out.println("Demo root: " + demoRoot.toAbsolutePath());
        System.out.println("Generated server class: " + classFile.toAbsolutePath());
        System.out.println("Generated web js: " + jsFile.toAbsolutePath());
        System.out.println("Static root: " + staticRoot.toAbsolutePath());

        return new BuildArtifacts(demoRoot, staticRoot, runMethod);
    }

    private static void serve(BuildArtifacts artifacts, int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        server.createContext("/api/result", exchange -> {
            try {
                if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    sendJson(exchange, 405, "{\"error\":\"method not allowed\"}");
                    return;
                }
                Object result = artifacts.runMethod.invoke(null);
                String response = toJsonObject(result);
                sendJson(exchange, 200, response);
            } catch (Exception e) {
                String escaped = escapeJson(Objects.toString(e.getMessage(), "unknown error"));
                sendJson(exchange, 500, "{\"error\":\"" + escaped + "\"}");
            }
        });

        server.createContext("/", exchange -> serveStatic(exchange, artifacts.staticRoot));
        server.start();

        System.out.println("Qin fullstack server started on http://localhost:" + port);
        System.out.println("API endpoint: http://localhost:" + port + "/api/result");
        System.out.println("Press Ctrl+C to stop.");
    }

    private static void serveStatic(HttpExchange exchange, Path webRoot) throws IOException {
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
        Path resolved = resolveStaticFile(webRoot, relative);

        if (!resolved.startsWith(webRoot.normalize())) {
            sendText(exchange, 403, "forbidden", "text/plain; charset=utf-8");
            return;
        }

        if (!Files.exists(resolved)) {
            sendText(exchange, 404, "not found", "text/plain; charset=utf-8");
            return;
        }

        byte[] bytes = Files.readAllBytes(resolved);
        exchange.getResponseHeaders().set("Content-Type", contentType(resolved));
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
        exchange.close();
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

    private static String toJsonObject(Object value) {
        return toJson(value);
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
                String key = String.valueOf(entry.getKey());
                sb.append("\"").append(escapeJson(key)).append("\":").append(toJson(entry.getValue()));
            }
            sb.append("}");
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

    private static void validateInputFile(Path file, String label) {
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Missing " + label + " source: " + file.toAbsolutePath());
        }
    }

    private static Path resolveDemoRoot(Path fromArgs) {
        if (fromArgs != null) {
            return fromArgs.toAbsolutePath().normalize();
        }
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[]{
                cwd.resolve("qin/examples/qin-fullstack-demo"),
                cwd.resolve("../examples/qin-fullstack-demo"),
                cwd.resolve("examples/qin-fullstack-demo")
        };
        for (Path candidate : candidates) {
            if (Files.exists(candidate.resolve("qin.config.json"))) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(
                "Cannot find demo root. Pass --demo-root <path>, for example: qin/examples/qin-fullstack-demo");
    }

    private static String defaultIndexHtml() {
        return """
                <!doctype html>
                <html lang="en">
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <title>Qin Fullstack Demo</title>
                  <style>
                    body { font-family: sans-serif; margin: 2rem; }
                    pre { background: #f6f8fa; padding: 1rem; border-radius: 8px; }
                  </style>
                </head>
                <body>
                  <h1>Qin Fullstack Demo</h1>
                  <p>Frontend code comes from Qin -> JS output.</p>
                  <p>Server data comes from Qin -> .class output.</p>
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

    private static Options parseArgs(String[] args) {
        Options options = new Options();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--help" -> options.showHelp = true;
                case "--build-only" -> options.buildOnly = true;
                case "--port" -> options.port = parsePort(nextValue(args, ++i, "--port"));
                case "--demo-root" -> options.demoRoot = Path.of(nextValue(args, ++i, "--demo-root"));
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
        System.out.println("QinFullstackServeMain - build and serve Qin fullstack demo");
        System.out.println("Usage:");
        System.out.println("  --demo-root <path>   Demo workspace root (optional)");
        System.out.println("  --port <num>         HTTP port (default: 8080)");
        System.out.println("  --build-only         Build outputs only, do not start server");
        System.out.println("  --help               Show help");
    }

    private static final class Options {
        private Path demoRoot;
        private int port = 8080;
        private boolean buildOnly;
        private boolean showHelp;
    }

    private record BuildArtifacts(Path demoRoot, Path staticRoot, Method runMethod) {
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private ByteArrayClassLoader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] classBytes) {
            return defineClass(binaryName, classBytes, 0, classBytes.length);
        }
    }
}
