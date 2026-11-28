package com.qin.orm;

import java.util.LinkedHashMap;
import java.util.Map;

final class QinOrmJpqlContext {
    private final Map<String, Object> parameters = new LinkedHashMap<>();
    private int nextSubquery;

    String bind(Object value) {
        String name = "qin_p" + parameters.size();
        parameters.put(name, value);
        return ":" + name;
    }

    Map<String, Object> parameters() {
        return parameters;
    }

    String nextSubqueryAlias() {
        return "qinSubquery" + nextSubquery++;
    }
}
