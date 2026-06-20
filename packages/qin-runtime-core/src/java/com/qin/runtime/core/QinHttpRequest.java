package com.qin.runtime.core;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class QinHttpRequest {
    private final String method;
    private final String path;
    private final String query;
    private final Map<String, String> headers;
    private final byte[] body;
    private final Map<String, List<String>> queryParams;
    private final Map<String, String> params;

    public QinHttpRequest(
            String method,
            String path,
            String query,
            Map<String, String> headers,
            byte[] body,
            Map<String, String> params) {
        this.method = Objects.requireNonNullElse(method, "GET").toUpperCase(Locale.ROOT);
        this.path = normalizePath(path);
        this.query = Objects.requireNonNullElse(query, "");
        this.headers = copyHeaders(headers);
        this.body = body == null ? new byte[0] : body.clone();
        this.queryParams = parseQuery(this.query);
        this.params = params == null ? Map.of() : Collections.unmodifiableMap(new LinkedHashMap<>(params));
    }

    public String method() {
        return method;
    }

    public String path() {
        return path;
    }

    public String query() {
        return query;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String header(String name) {
        if (name == null) {
            return null;
        }
        return headers.get(name.toLowerCase(Locale.ROOT));
    }

    public byte[] body() {
        return body.clone();
    }

    public String bodyText() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public Map<String, List<String>> queryParams() {
        return queryParams;
    }

    public String queryParam(String name) {
        List<String> values = queryParams.get(name);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    public Map<String, String> params() {
        return params;
    }

    public String param(String name) {
        return params.get(name);
    }

    QinHttpRequest withParams(Map<String, String> nextParams) {
        return new QinHttpRequest(method, path, query, headers, body, nextParams);
    }

    private static String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "/";
        }
        return path.startsWith("/") ? path : "/" + path;
    }

    private static Map<String, String> copyHeaders(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return Map.of();
        }
        Map<String, String> copy = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (entry.getKey() != null) {
                copy.put(entry.getKey().toLowerCase(Locale.ROOT), entry.getValue());
            }
        }
        return Collections.unmodifiableMap(copy);
    }

    private static Map<String, List<String>> parseQuery(String query) {
        if (query == null || query.isBlank()) {
            return Map.of();
        }
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (String part : query.split("&")) {
            if (part.isEmpty()) {
                continue;
            }
            int equals = part.indexOf('=');
            String key = equals < 0 ? part : part.substring(0, equals);
            String value = equals < 0 ? "" : part.substring(equals + 1);
            result.computeIfAbsent(decode(key), ignored -> new ArrayList<>()).add(decode(value));
        }
        Map<String, List<String>> immutable = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            immutable.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        return Collections.unmodifiableMap(immutable);
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
