package com.qin.qono;

import com.qin.runtime.core.QinJson;

public final class QonoJson {
    private QonoJson() {
    }

    public static String string(String value) {
        return QinJson.string(value);
    }

    public static String escape(String value) {
        return QinJson.escape(value);
    }
}
