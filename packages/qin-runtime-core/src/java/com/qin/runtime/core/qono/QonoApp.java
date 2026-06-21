package com.qin.runtime.core.qono;

import com.qin.runtime.core.QinHttpApp;
import com.qin.runtime.core.QinHttpRequest;
import com.qin.runtime.core.QinHttpResponse;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class QonoApp {
    private final Map<String, QonoHandler> queries = new LinkedHashMap<>();
    private final Map<String, QonoHandler> mutations = new LinkedHashMap<>();
    private final QinHttpApp httpApp = QinHttpApp.create();
    private String rpcPath = "/api/rpc/{method}";

    public QonoApp query(String name, QonoHandler handler) {
        queries.put(normalizeName(name), Objects.requireNonNull(handler, "handler"));
        return this;
    }

    public QonoApp mutation(String name, QonoHandler handler) {
        mutations.put(normalizeName(name), Objects.requireNonNull(handler, "handler"));
        return this;
    }

    public QonoApp health() {
        httpApp.get("/api/health", request -> QinHttpResponse.json("{\"ok\":true}"));
        return this;
    }

    public QonoApp get(String path, QonoHandler handler) {
        return route("GET", path, handler);
    }

    public QonoApp post(String path, QonoHandler handler) {
        return route("POST", path, handler);
    }

    public QonoApp delete(String path, QonoHandler handler) {
        return route("DELETE", path, handler);
    }

    public QonoApp route(String method, String path, QonoHandler handler) {
        String verb = normalizeMethod(method);
        QonoHandler requiredHandler = Objects.requireNonNull(handler, "handler");
        switch (verb) {
            case "GET" -> httpApp.get(path, request -> safeHandle(requiredHandler, request));
            case "POST" -> httpApp.post(path, request -> safeHandle(requiredHandler, request));
            case "DELETE" -> httpApp.delete(path, request -> safeHandle(requiredHandler, request));
            default -> throw new IllegalArgumentException("Unsupported Qono route method: " + method);
        }
        return this;
    }

    public QonoApp rpcPath(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("RPC path cannot be blank");
        }
        rpcPath = path.contains("{method}") ? path : path + "/{method}";
        return this;
    }

    public QinHttpApp toHttpApp() {
        httpApp.post(rpcPath, this::handleRpc);
        return httpApp;
    }

    private QinHttpResponse handleRpc(QinHttpRequest request) throws Exception {
        String method = normalizeName(request.param("method"));
        QonoHandler handler = queries.get(method);
        if (handler == null) {
            handler = mutations.get(method);
        }
        if (handler == null) {
            return QinHttpResponse.json(404, "{\"error\":\"unknown rpc method\",\"method\":\""
                    + QonoJson.escape(method) + "\"}");
        }
        return safeHandle(handler, request);
    }

    private QinHttpResponse safeHandle(QonoHandler handler, QinHttpRequest request) {
        try {
            return toResponse(handler.handle(request), 200);
        } catch (RuntimeException error) {
            return errorResponse(error);
        } catch (Exception error) {
            return QinHttpResponse.json(500, "{\"error\":\"internal server error\",\"detail\":\""
                    + QonoJson.escape(error.getMessage()) + "\"}");
        }
    }

    private QinHttpResponse errorResponse(RuntimeException error) {
        String type = error.getClass().getSimpleName();
        int status = switch (type) {
            case "QinDbConfigException" -> 503;
            case "QinDbValidationException" -> 400;
            case "QinDbNotFoundException" -> 404;
            case "QinDbException" -> 503;
            default -> 500;
        };
        String code = switch (type) {
            case "QinDbConfigException" -> "database config missing";
            case "QinDbValidationException" -> "validation failed";
            case "QinDbNotFoundException" -> "not found";
            case "QinDbException" -> "database unavailable";
            default -> "internal server error";
        };
        return QinHttpResponse.json(status, "{\"error\":\"" + QonoJson.escape(code)
                + "\",\"detail\":\"" + QonoJson.escape(error.getMessage()) + "\"}");
    }

    private QinHttpResponse toResponse(Object value, int defaultStatus) {
        if (value instanceof QinHttpResponse response) {
            return response;
        }
        if (value instanceof QonoResult result) {
            return QinHttpResponse.json(result.status(), result.json());
        }
        if (value instanceof CharSequence text) {
            return QinHttpResponse.json(defaultStatus, QonoJson.string(String.valueOf(text)));
        }
        if (value instanceof Number || value instanceof Boolean) {
            return QinHttpResponse.json(defaultStatus, String.valueOf(value));
        }
        if (value == null) {
            return QinHttpResponse.json(defaultStatus, "null");
        }
        return QinHttpResponse.json(defaultStatus, QonoJson.string(String.valueOf(value)));
    }

    private static String normalizeName(String name) {
        String value = name == null ? "" : name.trim();
        if (value.isBlank()) {
            throw new IllegalArgumentException("Qono RPC method name cannot be blank");
        }
        return value;
    }

    private static String normalizeMethod(String method) {
        String value = method == null ? "" : method.trim().toUpperCase(Locale.ROOT);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Qono route method cannot be blank");
        }
        return value;
    }
}
