package com.qin.web;

import com.qin.runtime.core.QinJson;

public final class QinWebJson {
    private QinWebJson() {
    }

    public static String string(String value) {
        return QinJson.string(value);
    }

    public static String escape(String value) {
        return QinJson.escape(value);
    }
}
