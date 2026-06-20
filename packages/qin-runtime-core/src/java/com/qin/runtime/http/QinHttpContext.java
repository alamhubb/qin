package com.qin.runtime.http;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinHttpContext {
    private final String method;
    private final String path;
    private final String rawQuery;
    private final Map<String, String> headers;
    private final byte[] body;
    private final Map<String, String> params;

    public QinHttpContext(
            String method,
            String path,
            String rawQuery,
            Map<String, String> headers,
            byte[] body,
            Map<String, String> params) {
        this.method = method == null ? "GET" : method.toUpperCase();
        this.path = path == null || path.isBlank() ? "/" : path;
        this.rawQuery = rawQuery == null ? "" : rawQuery;
        this.headers = headers == null ? Map.of() : Map.copyOf(headers);
        this.body = body == null ? new byte[0] : body.clone();
        this.params = params == null ? Map.of() : Map.copyOf(params);
    }

    public String method() {
        return method;
    }

    public String path() {
        return path;
    }

    public String rawQuery() {
        return rawQuery;
    }

    public String header(String name) {
        if (name == null) {
            return null;
        }
        String direct = headers.get(name);
        return direct != null ? direct : headers.get(name.toLowerCase());
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String param(String name) {
        return params.get(name);
    }

    public Map<String, String> params() {
        return params;
    }

    public String query(String name) {
        return queryParams().get(name);
    }

    public Map<String, String> queryParams() {
        Map<String, String> result = new LinkedHashMap<>();
        if (rawQuery.isBlank()) {
            return result;
        }
        for (String part : rawQuery.split("&")) {
            if (part.isBlank()) {
                continue;
            }
            int eq = part.indexOf('=');
            String key = eq >= 0 ? part.substring(0, eq) : part;
            String value = eq >= 0 ? part.substring(eq + 1) : "";
            result.put(decode(key), decode(value));
        }
        return result;
    }

    public byte[] bodyBytes() {
        return body.clone();
    }

    public String bodyText() {
        return new String(body, StandardCharsets.UTF_8);
    }

    public String jsonString(String name) {
        return QinJson.stringField(bodyText(), name);
    }

    public long jsonLong(String name) {
        String value = QinJson.numberField(bodyText(), name);
        if (value == null) {
            throw new IllegalArgumentException("Missing JSON number field: " + name);
        }
        return Long.parseLong(value);
    }

    public QinHttpResponse json(Object value) {
        return QinHttpResponse.json(value);
    }

    public QinHttpResponse json(Object value, int status) {
        return QinHttpResponse.json(value, status);
    }

    public QinHttpResponse text(String value) {
        return QinHttpResponse.text(value);
    }

    public QinHttpResponse text(String value, int status) {
        return QinHttpResponse.text(value, status);
    }

    public QinHttpResponse noContent() {
        return QinHttpResponse.noContent();
    }

    public static QinHttpContext withParams(QinHttpContext source, Map<String, String> params) {
        return new QinHttpContext(
                source.method,
                source.path,
                source.rawQuery,
                source.headers,
                source.body,
                params);
    }

    public static QinHttpContext create(
            String method,
            String path,
            String rawQuery,
            Map<String, String> headers,
            byte[] body) {
        return new QinHttpContext(method, path, rawQuery, headers, body, Map.of());
    }

    private static String decode(String text) {
        return URLDecoder.decode(text.replace("+", "%2B"), StandardCharsets.UTF_8);
    }
}
