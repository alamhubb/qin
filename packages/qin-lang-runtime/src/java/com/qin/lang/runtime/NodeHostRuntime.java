package com.qin.lang.runtime;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal Java-backed Node-style host builtins needed by current Qin npm source execution.
 */
final class NodeHostRuntime {
    private NodeHostRuntime() {
    }

    static Object fsNamespace() {
        return "node:fs";
    }

    static Object pathNamespace() {
        return "node:path";
    }

    static Object urlNamespace() {
        return "node:url";
    }

    static Object utilNamespace() {
        return "node:util";
    }

    static Object processNamespace() {
        return "node:process";
    }

    static Object diagnosticsChannelNamespace() {
        return "node:diagnostics_channel";
    }

    static Object globalThis() {
        return JavaEsmGlobal.__qin_global__("globalThis");
    }

    static Object existsSync(Object pathLike) {
        return Files.exists(asPath(pathLike));
    }

    static Object writeFileSync(Object pathLike, Object content) {
        Path path = asPath(pathLike);
        ensureParentDirectory(path);
        try {
            Files.writeString(path, String.valueOf(content), StandardCharsets.UTF_8);
            return null;
        } catch (IOException error) {
            throw new IllegalArgumentException("fs.writeFileSync failed: " + path, error);
        }
    }

    static Object appendFileSync(Object pathLike, Object content) {
        Path path = asPath(pathLike);
        ensureParentDirectory(path);
        try {
            Files.writeString(
                    path,
                    String.valueOf(content),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            return null;
        } catch (IOException error) {
            throw new IllegalArgumentException("fs.appendFileSync failed: " + path, error);
        }
    }

    static Object mkdirSync(Object pathLike) {
        return mkdirSync(pathLike, null);
    }

    static Object mkdirSync(Object pathLike, Object options) {
        Path path = asPath(pathLike);
        boolean recursive = isRecursiveEnabled(options);
        try {
            if (recursive) {
                Files.createDirectories(path);
            } else {
                Files.createDirectory(path);
            }
            return null;
        } catch (IOException error) {
            throw new IllegalArgumentException("fs.mkdirSync failed: " + path, error);
        }
    }

    static Object createWriteStream(Object pathLike) {
        return createWriteStream(pathLike, null);
    }

    static Object createWriteStream(Object pathLike, Object options) {
        Path path = asPath(pathLike);
        ensureParentDirectory(path);
        String flags = extractStringOption(options, "flags", "w");
        boolean append = "a".equals(flags) || "as".equals(flags);
        return new NodeWriteStream(path, append);
    }

    static Object dirname(Object pathLike) {
        Path path = asPath(pathLike);
        Path parent = path.getParent();
        return parent == null ? path.toString() : parent.toString();
    }

    static Object join(Object... parts) {
        if (parts == null || parts.length == 0) {
            return "";
        }
        Path result = Paths.get(String.valueOf(parts[0]));
        for (int i = 1; i < parts.length; i++) {
            result = result.resolve(String.valueOf(parts[i]));
        }
        return result.normalize().toString();
    }

    static Object resolve(Object... parts) {
        if (parts == null || parts.length == 0) {
            return Paths.get("").toAbsolutePath().normalize().toString();
        }
        Path result = Paths.get(String.valueOf(parts[0]));
        for (int i = 1; i < parts.length; i++) {
            result = result.resolve(String.valueOf(parts[i]));
        }
        return result.toAbsolutePath().normalize().toString();
    }

    static Object fileURLToPath(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("url.fileURLToPath requires a value");
        }
        String text = String.valueOf(value);
        if ("import.meta.url".equals(text)) {
            return Paths.get("").toAbsolutePath().normalize().resolve("module.js").toString();
        }
        if (text.startsWith("file:")) {
            return Paths.get(URI.create(text)).toString();
        }
        return Paths.get(text).toString();
    }

    static Object cwd() {
        return Paths.get("").toAbsolutePath().normalize().toString();
    }

    static Object deprecate(Object function, Object message) {
        return function;
    }

    static Object channel(Object name) {
        return new DiagnosticsChannel(String.valueOf(name));
    }

    static Object tracingChannel(Object name) {
        return new DiagnosticsChannel(String.valueOf(name));
    }

    private static Path asPath(Object pathLike) {
        if (pathLike == null) {
            throw new IllegalArgumentException("path cannot be null");
        }
        String text = String.valueOf(pathLike);
        if (text.startsWith("file:")) {
            return Paths.get(URI.create(text)).toAbsolutePath().normalize();
        }
        return Paths.get(text).toAbsolutePath().normalize();
    }

    private static void ensureParentDirectory(Path path) {
        Path parent = path.getParent();
        if (parent == null) {
            return;
        }
        try {
            Files.createDirectories(parent);
        } catch (IOException error) {
            throw new IllegalArgumentException("Failed to create parent directories for " + path, error);
        }
    }

    private static boolean isRecursiveEnabled(Object options) {
        if (!(options instanceof Map<?, ?> map)) {
            return false;
        }
        Object recursive = map.get("recursive");
        return Boolean.TRUE.equals(recursive);
    }

    private static String extractStringOption(Object options, String key, String defaultValue) {
        if (!(options instanceof Map<?, ?> map)) {
            return defaultValue;
        }
        Object value = map.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }

    static final class NodeWriteStream {
        private final Path path;
        private final OutputStream stream;
        private boolean destroyed;

        NodeWriteStream(Path path, boolean append) {
            this.path = path;
            try {
                OpenOption[] options = append
                        ? new OpenOption[] {
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND
                }
                        : new OpenOption[] {
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING
                };
                this.stream = Files.newOutputStream(path, options);
            } catch (IOException error) {
                throw new IllegalArgumentException("fs.createWriteStream failed: " + path, error);
            }
        }

        public Object write(Object chunk) {
            if (destroyed) {
                return null;
            }
            try {
                stream.write(String.valueOf(chunk).getBytes(StandardCharsets.UTF_8));
                stream.flush();
                return null;
            } catch (IOException error) {
                throw new IllegalArgumentException("writeStream.write failed: " + path, error);
            }
        }

        public Object end() {
            if (destroyed) {
                return null;
            }
            try {
                destroyed = true;
                stream.close();
                return null;
            } catch (IOException error) {
                throw new IllegalArgumentException("writeStream.end failed: " + path, error);
            }
        }

        public Object on(Object eventName, Object handler) {
            // Current host path does not emit asynchronous stream events yet.
            return this;
        }

        public boolean destroyed() {
            return destroyed;
        }
    }

    public static final class DiagnosticsChannel {
        public final boolean hasSubscribers = false;
        private final String name;

        DiagnosticsChannel(String name) {
            this.name = name;
        }

        public Object name() {
            return name;
        }

        public Object publish(Object message) {
            return null;
        }

        public Object subscribe(Object handler) {
            return this;
        }

        public Object unsubscribe(Object handler) {
            return this;
        }

        public Object traceSync(Object callback, Object context) {
            return JavaEsmGlobal.callRuntimeCallable(callback);
        }

        public Object tracePromise(Object callback, Object context) {
            return JavaEsmGlobal.callRuntimeCallable(callback);
        }
    }
}
