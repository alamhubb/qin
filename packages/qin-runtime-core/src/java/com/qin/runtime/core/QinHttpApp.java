package com.qin.runtime.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public final class QinHttpApp {
    private final List<Route> routes = new ArrayList<>();

    public static QinHttpApp create() {
        return new QinHttpApp();
    }

    public QinHttpApp get(String path, QinHttpHandler handler) {
        return route("GET", path, handler);
    }

    public QinHttpApp post(String path, QinHttpHandler handler) {
        return route("POST", path, handler);
    }

    public QinHttpApp put(String path, QinHttpHandler handler) {
        return route("PUT", path, handler);
    }

    public QinHttpApp delete(String path, QinHttpHandler handler) {
        return route("DELETE", path, handler);
    }

    public QinHttpApp route(String method, String path, QinHttpHandler handler) {
        routes.add(new Route(normalizeMethod(method), normalizePath(path), Objects.requireNonNull(handler, "handler")));
        return this;
    }

    public QinHttpResponse handle(QinHttpRequest request) throws Exception {
        List<String> allowed = new ArrayList<>();
        for (Route route : routes) {
            Map<String, String> params = route.matchPath(request.path());
            if (params == null) {
                continue;
            }
            if (!route.method().equals(request.method())) {
                allowed.add(route.method());
                continue;
            }
            return route.handler().handle(request.withParams(params));
        }
        if (!allowed.isEmpty()) {
            return QinHttpResponse.methodNotAllowed(joinAllowed(allowed));
        }
        return null;
    }

    private static String normalizeMethod(String method) {
        if (method == null || method.isBlank()) {
            throw new IllegalArgumentException("HTTP method must not be blank");
        }
        return method.toUpperCase(Locale.ROOT);
    }

    private static String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Route path must not be blank");
        }
        return path.startsWith("/") ? path : "/" + path;
    }

    private static String joinAllowed(List<String> allowed) {
        StringJoiner joiner = new StringJoiner(", ");
        allowed.stream().distinct().forEach(joiner::add);
        return joiner.toString();
    }

    @FunctionalInterface
    public interface QinHttpHandler {
        QinHttpResponse handle(QinHttpRequest request) throws Exception;
    }

    private record Route(String method, String path, QinHttpHandler handler) {
        private Map<String, String> matchPath(String requestPath) {
            String[] routeParts = split(path);
            String[] requestParts = split(requestPath);
            if (routeParts.length != requestParts.length) {
                return null;
            }
            Map<String, String> params = new LinkedHashMap<>();
            for (int i = 0; i < routeParts.length; i++) {
                String routePart = routeParts[i];
                String requestPart = requestParts[i];
                if (routePart.startsWith("{") && routePart.endsWith("}") && routePart.length() > 2) {
                    params.put(routePart.substring(1, routePart.length() - 1), requestPart);
                } else if (!routePart.equals(requestPart)) {
                    return null;
                }
            }
            return params;
        }

        private static String[] split(String path) {
            String normalized = path == null || path.isBlank() ? "/" : path;
            String trimmed = normalized.startsWith("/") ? normalized.substring(1) : normalized;
            if (trimmed.isBlank()) {
                return new String[0];
            }
            return trimmed.split("/");
        }
    }
}
