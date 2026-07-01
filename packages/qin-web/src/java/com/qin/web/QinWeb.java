package com.qin.web;

import com.qin.runtime.core.QinHttpResponse;

public final class QinWeb {
    private QinWeb() {
    }

    public static QinWebApp create() {
        return new QinWebApp();
    }

    public static QinWebResult jsonRaw(String json) {
        return jsonRaw(200, json);
    }

    public static QinWebResult jsonRaw(int status, String json) {
        return QinWebResult.jsonRaw(status, json);
    }

    public static QinHttpResponse text(String text) {
        return QinHttpResponse.text(text);
    }

    public static QinHttpResponse text(int status, String text) {
        return QinHttpResponse.text(status, text);
    }

    public static QinHttpResponse html(String html) {
        return QinHttpResponse.text(200, html, "text/html; charset=utf-8");
    }

    public static QinHttpResponse html(int status, String html) {
        return QinHttpResponse.text(status, html, "text/html; charset=utf-8");
    }
}
