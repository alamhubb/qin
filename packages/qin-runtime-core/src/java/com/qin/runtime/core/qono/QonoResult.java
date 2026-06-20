package com.qin.runtime.core.qono;

public record QonoResult(int status, String json) {
    public QonoResult {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("HTTP status out of range: " + status);
        }
        json = json == null || json.isBlank() ? "null" : json;
    }

    public static QonoResult jsonRaw(int status, String json) {
        return new QonoResult(status, json);
    }
}
