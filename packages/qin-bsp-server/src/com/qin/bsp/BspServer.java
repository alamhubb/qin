package com.qin.bsp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.Scanner;

/**
 * Qin BSP Server 主入口
 * 通过 stdio 与 IDE 进行 JSON-RPC 通信
 */
public class BspServer {
    private final Gson gson = new Gson();
    private final BspHandler handler;

    public BspServer(String workDir) {
        this.handler = new BspHandler(workDir);
    }

    /**
     * 启动服务器，监听 stdin
     */
    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            String response = processRequest(line);
            if (response != null) {
                System.out.println(response);
                System.out.flush();
            }
        }
    }

    private String processRequest(String json) {
        try {
            JsonObject req = gson.fromJson(json, JsonObject.class);
            String method = req.get("method").getAsString();
            Object id = req.has("id") ? req.get("id") : null;
            JsonObject params = req.has("params")
                    ? req.getAsJsonObject("params")
                    : null;

            Object result = handler.handle(method, params);
            return buildResponse(id, result);
        } catch (Exception e) {
            return buildError(null, -32603, e.getMessage());
        }
    }

    private String buildResponse(Object id, Object result) {
        JsonObject res = new JsonObject();
        res.addProperty("jsonrpc", "2.0");
        res.add("id", gson.toJsonTree(id));
        res.add("result", gson.toJsonTree(result));
        return gson.toJson(res);
    }

    private String buildError(Object id, int code, String msg) {
        JsonObject res = new JsonObject();
        res.addProperty("jsonrpc", "2.0");
        res.add("id", gson.toJsonTree(id));
        JsonObject err = new JsonObject();
        err.addProperty("code", code);
        err.addProperty("message", msg);
        res.add("error", err);
        return gson.toJson(res);
    }

    public static void main(String[] args) throws IOException {
        String workDir = System.getProperty("user.dir");
        new BspServer(workDir).run();
    }
}
