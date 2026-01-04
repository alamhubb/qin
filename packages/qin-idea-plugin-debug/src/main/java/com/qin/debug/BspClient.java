package com.qin.debug;

import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * BSP 客户端
 * 与 qin-bsp-server 通信
 */
public class BspClient {
    private final String projectPath;
    private final Gson gson = new Gson();

    public BspClient(String projectPath) {
        this.projectPath = projectPath;
    }

    /**
     * 发送 BSP 请求并获取响应
     */
    public JsonObject sendRequest(String method, JsonObject params) {
        try {
            // 构建 JSON-RPC 请求
            JsonObject request = new JsonObject();
            request.addProperty("jsonrpc", "2.0");
            request.addProperty("id", 1);
            request.addProperty("method", method);
            request.add("params", params != null ? params : new JsonObject());

            String requestJson = gson.toJson(request);

            // 启动 BSP Server
            ProcessBuilder pb = new ProcessBuilder(
                    "cmd", "/c", "qin", "bsp-server");
            pb.directory(new File(projectPath));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // 发送请求
            try (OutputStream os = process.getOutputStream()) {
                os.write(requestJson.getBytes(StandardCharsets.UTF_8));
                os.write('\n');
                os.flush();
            }

            // 读取响应
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line = reader.readLine();
                if (line != null) {
                    return gson.fromJson(line, JsonObject.class);
                }
            }

            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取构建目标
     */
    public JsonArray getBuildTargets() {
        JsonObject response = sendRequest("workspace/buildTargets", null);
        if (response != null && response.has("result")) {
            JsonObject result = response.getAsJsonObject("result");
            if (result.has("targets")) {
                return result.getAsJsonArray("targets");
            }
        }
        return new JsonArray();
    }

    /**
     * 获取 JavacOptions（包含 classpath）
     */
    public JsonArray getClasspath(String targetUri) {
        JsonObject params = new JsonObject();
        JsonArray targets = new JsonArray();
        JsonObject target = new JsonObject();
        target.addProperty("uri", targetUri);
        targets.add(target);
        params.add("targets", targets);

        JsonObject response = sendRequest("buildTarget/javacOptions", params);
        if (response != null && response.has("result")) {
            JsonObject result = response.getAsJsonObject("result");
            if (result.has("items")) {
                JsonArray items = result.getAsJsonArray("items");
                if (!items.isEmpty()) {
                    JsonObject item = items.get(0).getAsJsonObject();
                    if (item.has("classpath")) {
                        return item.getAsJsonArray("classpath");
                    }
                }
            }
        }
        return new JsonArray();
    }
}
