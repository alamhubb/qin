package com.qin.runtime.core.qono;

public final class Qono {
    private Qono() {
    }

    public static QonoApp create() {
        return new QonoApp();
    }

    public static QonoResult jsonRaw(String json) {
        return jsonRaw(200, json);
    }

    public static QonoResult jsonRaw(int status, String json) {
        return QonoResult.jsonRaw(status, json);
    }
}
