package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class QinHttpResponse {
    private final int status;
    private final String contentType;
    private final Map<String, String> headers;
    private final byte[] body;

    public QinHttpResponse(int status, String contentType, Map<String, String> headers, byte[] body) {
        this.status = status;
        this.contentType = Objects.requireNonNullElse(contentType, "text/plain; charset=utf-8");
        this.headers = headers == null ? Map.of() : Collections.unmodifiableMap(new LinkedHashMap<>(headers));
        this.body = body == null ? new byte[0] : body.clone();
    }

    public static QinHttpResponse json(String json) {
        return json(200, json);
    }

    public static QinHttpResponse json(int status, String json) {
        return text(status, Objects.requireNonNullElse(json, "null"), "application/json; charset=utf-8");
    }

    public static QinHttpResponse text(String text) {
        return text(200, text, "text/plain; charset=utf-8");
    }

    public static QinHttpResponse text(int status, String text) {
        return text(status, text, "text/plain; charset=utf-8");
    }

    public static QinHttpResponse text(int status, String text, String contentType) {
        return new QinHttpResponse(status, contentType, Map.of(), Objects.requireNonNullElse(text, "").getBytes(StandardCharsets.UTF_8));
    }

    public static QinHttpResponse bytes(int status, byte[] body, String contentType) {
        return new QinHttpResponse(status, contentType, Map.of(), body);
    }

    public static QinHttpResponse noContent() {
        return new QinHttpResponse(204, "text/plain; charset=utf-8", Map.of(), new byte[0]);
    }

    public static QinHttpResponse methodNotAllowed(String allow) {
        return new QinHttpResponse(405, "application/json; charset=utf-8", Map.of("Allow", allow), "{\"error\":\"method not allowed\"}".getBytes(StandardCharsets.UTF_8));
    }

    public static QinHttpResponse error(int status, String message) {
        return json(status, "{\"error\":\"" + escapeJson(Objects.requireNonNullElse(message, "")) + "\"}");
    }

    public QinHttpResponse header(String name, String value) {
        Map<String, String> next = new LinkedHashMap<>(headers);
        next.put(name, value);
        return new QinHttpResponse(status, contentType, next, body);
    }

    public int status() {
        return status;
    }

    public String contentType() {
        return contentType;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public byte[] body() {
        return body.clone();
    }

    private static String escapeJson(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"' -> builder.append("\\\"");
                case '\\' -> builder.append("\\\\");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                default -> {
                    if (c < 0x20) {
                        builder.append(String.format("\\u%04x", (int) c));
                    } else {
                        builder.append(c);
                    }
                }
            }
        }
        return builder.toString();
    }
}
