package com.qin.runtime.http;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinHttpResponse {
    private final int status;
    private final String contentType;
    private final byte[] body;
    private final Map<String, String> headers;

    public QinHttpResponse(int status, String contentType, byte[] body, Map<String, String> headers) {
        this.status = status;
        this.contentType = contentType == null || contentType.isBlank()
                ? "application/octet-stream"
                : contentType;
        this.body = body == null ? new byte[0] : body.clone();
        this.headers = headers == null ? Map.of() : Map.copyOf(headers);
    }

    public static QinHttpResponse json(Object value) {
        return json(value, 200);
    }

    public static QinHttpResponse json(Object value, int status) {
        return text(QinJson.stringify(value), status, "application/json; charset=utf-8");
    }

    public static QinHttpResponse text(String value) {
        return text(value, 200);
    }

    public static QinHttpResponse text(String value, int status) {
        return text(value, status, "text/plain; charset=utf-8");
    }

    public static QinHttpResponse text(String value, int status, String contentType) {
        return new QinHttpResponse(
                status,
                contentType,
                (value == null ? "" : value).getBytes(StandardCharsets.UTF_8),
                Map.of());
    }

    public static QinHttpResponse noContent() {
        return new QinHttpResponse(204, "text/plain; charset=utf-8", new byte[0], Map.of());
    }

    public QinHttpResponse header(String name, String value) {
        Map<String, String> next = new LinkedHashMap<>(headers);
        next.put(name, value);
        return new QinHttpResponse(status, contentType, body, next);
    }

    public int status() {
        return status;
    }

    public String contentType() {
        return contentType;
    }

    public byte[] body() {
        return body.clone();
    }

    public Map<String, String> headers() {
        return headers;
    }
}
