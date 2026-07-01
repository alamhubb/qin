package com.qin.web;

public record QinWebResult(int status, String json) {
    public QinWebResult {
        if (status < 100 || status > 599) {
            throw new IllegalArgumentException("HTTP status out of range: " + status);
        }
        json = json == null ? "null" : json;
    }

    public static QinWebResult jsonRaw(int status, String json) {
        return new QinWebResult(status, json);
    }
}
