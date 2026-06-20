package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class QinHttpAppRoutingSmokeTestMain {
    private QinHttpAppRoutingSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinHttpApp app = QinHttpApp.create()
                .get("/api/users", request -> QinHttpResponse.json("{\"users\":[]}"))
                .post("/api/users", request -> QinHttpResponse.json(201, "{\"body\":" + quote(request.bodyText()) + "}"))
                .delete("/api/users/{id}", request -> QinHttpResponse.json("{\"deleted\":\"" + request.param("id") + "\"}"));

        QinHttpResponse list = app.handle(request("GET", "/api/users", "", ""));
        require(list != null && list.status() == 200 && text(list).contains("\"users\""), "GET /api/users failed");

        QinHttpResponse create = app.handle(request("POST", "/api/users", "", "{\"name\":\"Ada\"}"));
        require(create != null && create.status() == 201 && text(create).contains("Ada"), "POST /api/users body failed");

        QinHttpResponse delete = app.handle(request("DELETE", "/api/users/42", "", ""));
        require(delete != null && delete.status() == 200 && text(delete).contains("\"42\""), "DELETE route param failed");

        QinHttpResponse methodNotAllowed = app.handle(request("PUT", "/api/users", "", ""));
        require(methodNotAllowed != null && methodNotAllowed.status() == 405, "405 route match failed");
        require("GET, POST".equals(methodNotAllowed.headers().get("Allow")), "Allow header mismatch: " + methodNotAllowed.headers());

        QinHttpResponse missing = app.handle(request("GET", "/api/missing", "", ""));
        require(missing == null, "Missing route should return null");

        System.out.println("QinHttpAppRoutingSmokeTestMain passed.");
    }

    private static QinHttpRequest request(String method, String path, String query, String body) {
        return new QinHttpRequest(method, path, query, Map.of(), body.getBytes(StandardCharsets.UTF_8), Map.of());
    }

    private static String text(QinHttpResponse response) {
        return new String(response.body(), StandardCharsets.UTF_8);
    }

    private static String quote(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
