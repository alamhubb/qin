package com.qin.debug.lsp;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

final class QinLspSmokeJson {
    private static final Gson GSON = new Gson();
    private static final TypeToken<Map<String, Object>> OBJECT_TYPE = new TypeToken<>() {
    };

    private QinLspSmokeJson() {
    }

    static String object(Map<String, Object> value) {
        return GSON.toJson(value);
    }

    static Map<String, Object> parseObject(String value) {
        return GSON.fromJson(value, OBJECT_TYPE.getType());
    }
}
