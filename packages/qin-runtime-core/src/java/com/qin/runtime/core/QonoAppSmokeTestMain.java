package com.qin.runtime.core;

import com.qin.runtime.core.qono.Qono;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class QonoAppSmokeTestMain {
    private QonoAppSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinHttpApp app = Qono.create()
                .health()
                .route("GET", "/api/users", request -> Qono.jsonRaw("{\"restUsers\":[]}"))
                .route("POST", "/api/users", request -> Qono.jsonRaw(201, "{\"restUser\":" + request.bodyText() + "}"))
                .route("DELETE", "/api/users/{id}", request -> Qono.jsonRaw("{\"deleted\":\"" + request.param("id") + "\"}"))
                .query("users.getAll", request -> Qono.jsonRaw("{\"users\":[]}"))
                .mutation("users.create", request -> Qono.jsonRaw(201, "{\"user\":" + request.bodyText() + "}"))
                .mutation("users.fail", request -> {
                    throw new IllegalStateException("boom");
                })
                .toHttpApp();

        QinHttpResponse health = app.handle(request("GET", "/api/health", ""));
        require(health != null && health.status() == 200 && text(health).contains("\"ok\":true"), "health route failed");

        QinHttpResponse restList = app.handle(request("GET", "/api/users", ""));
        require(restList != null && restList.status() == 200 && text(restList).contains("\"restUsers\""), "REST query route failed");

        QinHttpResponse restCreate = app.handle(request("POST", "/api/users", "{\"name\":\"Grace\"}"));
        require(restCreate != null && restCreate.status() == 201 && text(restCreate).contains("Grace"), "REST mutation route failed");

        QinHttpResponse restDelete = app.handle(request("DELETE", "/api/users/42", ""));
        require(restDelete != null && restDelete.status() == 200 && text(restDelete).contains("\"42\""), "REST path param route failed");

        QinHttpResponse query = app.handle(request("POST", "/api/rpc/users.getAll", "{}"));
        require(query != null && query.status() == 200 && text(query).contains("\"users\""), "query route failed");

        QinHttpResponse mutation = app.handle(request("POST", "/api/rpc/users.create", "{\"name\":\"Ada\"}"));
        require(mutation != null && mutation.status() == 201 && text(mutation).contains("Ada"), "mutation route failed");

        QinHttpResponse missing = app.handle(request("POST", "/api/rpc/users.missing", "{}"));
        require(missing != null && missing.status() == 404 && text(missing).contains("unknown rpc method"), "missing rpc method failed");

        QinHttpResponse failure = app.handle(request("POST", "/api/rpc/users.fail", "{}"));
        require(failure != null && failure.status() == 500 && text(failure).contains("internal server error"), "rpc error mapping failed");

        System.out.println("QonoAppSmokeTestMain passed.");
    }

    private static QinHttpRequest request(String method, String path, String body) {
        return new QinHttpRequest(method, path, "", Map.of(), body.getBytes(StandardCharsets.UTF_8), Map.of());
    }

    private static String text(QinHttpResponse response) {
        return new String(response.body(), StandardCharsets.UTF_8);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
