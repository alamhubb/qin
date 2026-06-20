package com.qin.runtime.http;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public final class QinHttpApp {
    private final List<Route> routes = new ArrayList<>();

    public static QinHttpApp create() {
        return new QinHttpApp();
    }

    public QinHttpApp get(String pattern, QinHttpHandler handler) {
        return route("GET", pattern, handler);
    }

    public QinHttpApp post(String pattern, QinHttpHandler handler) {
        return route("POST", pattern, handler);
    }

    public QinHttpApp put(String pattern, QinHttpHandler handler) {
        return route("PUT", pattern, handler);
    }

    public QinHttpApp patch(String pattern, QinHttpHandler handler) {
        return route("PATCH", pattern, handler);
    }

    public QinHttpApp delete(String pattern, QinHttpHandler handler) {
        return route("DELETE", pattern, handler);
    }

    public QinHttpApp route(String method, String pattern, QinHttpHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("Route handler is required.");
        }
        routes.add(new Route(
                normalizeMethod(method),
                normalizePath(pattern),
                splitPath(pattern),
                handler));
        return this;
    }

    public Optional<QinHttpResponse> handle(QinHttpContext context) throws Exception {
        String method = normalizeMethod(context.method());
        String path = normalizePath(context.path());
        List<String> requestSegments = splitPath(path);
        for (Route route : routes) {
            if (!route.method.equals(method)) {
                continue;
            }
            Map<String, String> params = match(route.segments, requestSegments);
            if (params != null) {
                return Optional.ofNullable(route.handler.handle(QinHttpContext.withParams(context, params)));
            }
        }
        return Optional.empty();
    }

    public int routeCount() {
        return routes.size();
    }

    private static Map<String, String> match(List<String> patternSegments, List<String> requestSegments) {
        if (patternSegments.size() != requestSegments.size()) {
            return null;
        }
        Map<String, String> params = new LinkedHashMap<>();
        for (int i = 0; i < patternSegments.size(); i++) {
            String pattern = patternSegments.get(i);
            String actual = requestSegments.get(i);
            if (pattern.startsWith(":")) {
                if (pattern.length() == 1) {
                    throw new IllegalArgumentException("Route parameter name is empty.");
                }
                params.put(pattern.substring(1), actual);
                continue;
            }
            if (!pattern.equals(actual)) {
                return null;
            }
        }
        return params;
    }

    private static String normalizeMethod(String method) {
        return method == null ? "GET" : method.trim().toUpperCase(Locale.ROOT);
    }

    private static String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "/";
        }
        String result = path.trim();
        if (!result.startsWith("/")) {
            result = "/" + result;
        }
        while (result.length() > 1 && result.endsWith("/")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private static List<String> splitPath(String path) {
        String normalized = normalizePath(path);
        if ("/".equals(normalized)) {
            return List.of();
        }
        String[] parts = normalized.substring(1).split("/");
        List<String> result = new ArrayList<>(parts.length);
        for (String part : parts) {
            if (!part.isBlank()) {
                result.add(part);
            }
        }
        return List.copyOf(result);
    }

    private record Route(
            String method,
            String pattern,
            List<String> segments,
            QinHttpHandler handler) {
    }
}
