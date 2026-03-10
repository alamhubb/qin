package com.qin.bsp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qin.constants.QinConstants;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Qin BSP Server - 标准 Build Server Protocol 实现
 *
 * 遵循 BSP 2.1.0 规范：https://build-server-protocol.github.io/
 *
 * 通信方式：JSON-RPC 2.0 over stdio (使用 Content-Length header)
 *
 * 支持的方法：
 * - build/initialize, build/initialized, build/shutdown, build/exit
 * - workspace/buildTargets, workspace/reload
 * - buildTarget/sources, buildTarget/dependencySources
 * - buildTarget/compile, buildTarget/run, buildTarget/test
 * - buildTarget/javacOptions, buildTarget/jvmRunEnvironment
 */
public class QinBspServer {

    private static final String BSP_VERSION = "2.1.0";
    private static final String SERVER_NAME = "Qin BSP Server";
    private static final String SERVER_VERSION = "0.3.0";

    private final Gson gson;
    private final QinBspHandler handler;
    private final ExecutorService executor;

    private BufferedReader reader;
    private PrintWriter writer;
    private boolean running = true;
    private boolean initialized = false;

    public QinBspServer(String workDir) {
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
        this.handler = new QinBspHandler(workDir, this);
        this.executor = Executors.newCachedThreadPool();
    }

    /**
     * 启动 BSP Server
     * 使用 Content-Length 头部的标准 JSON-RPC 通信
     */
    public void start() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        writer = new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true);

        logToStderr("Qin BSP Server started");

        while (running) {
            try {
                String message = readMessage();
                if (message == null) {
                    break;
                }

                // 异步处理请求
                executor.submit(() -> handleMessage(message));

            } catch (IOException e) {
                logToStderr("Error reading message: " + e.getMessage());
                break;
            }
        }

        executor.shutdown();
        logToStderr("Qin BSP Server stopped");
    }

    /**
     * 读取 JSON-RPC 消息（带 Content-Length 头部）
     */
    private String readMessage() throws IOException {
        // 读取头部
        String line;
        int contentLength = -1;

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break; // 头部结束
            }
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.substring(15).trim());
            }
        }

        if (contentLength == -1) {
            return null;
        }

        // 读取消息体
        char[] buffer = new char[contentLength];
        int read = 0;
        while (read < contentLength) {
            int r = reader.read(buffer, read, contentLength - read);
            if (r == -1) {
                return null;
            }
            read += r;
        }

        return new String(buffer);
    }

    /**
     * 发送 JSON-RPC 消息
     */
    public synchronized void sendMessage(JsonObject message) {
        String json = gson.toJson(message);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        writer.print("Content-Length: " + bytes.length + "\r\n");
        writer.print("\r\n");
        writer.print(json);
        writer.flush();
    }

    /**
     * 发送响应
     */
    public void sendResponse(Object id, Object result) {
        JsonObject response = new JsonObject();
        response.addProperty("jsonrpc", "2.0");
        response.add("id", gson.toJsonTree(id));
        response.add("result", gson.toJsonTree(result));
        sendMessage(response);
    }

    /**
     * 发送错误响应
     */
    public void sendError(Object id, int code, String message) {
        JsonObject response = new JsonObject();
        response.addProperty("jsonrpc", "2.0");
        response.add("id", gson.toJsonTree(id));

        JsonObject error = new JsonObject();
        error.addProperty("code", code);
        error.addProperty("message", message);
        response.add("error", error);

        sendMessage(response);
    }

    /**
     * 发送通知（无需响应）
     */
    public void sendNotification(String method, Object params) {
        JsonObject notification = new JsonObject();
        notification.addProperty("jsonrpc", "2.0");
        notification.addProperty("method", method);
        notification.add("params", gson.toJsonTree(params));
        sendMessage(notification);
    }

    /**
     * 处理收到的消息
     */
    private void handleMessage(String json) {
        try {
            JsonObject request = gson.fromJson(json, JsonObject.class);
            String method = request.get("method").getAsString();
            Object id = request.has("id") ? request.get("id") : null;
            JsonObject params = request.has("params")
                ? request.getAsJsonObject("params")
                : new JsonObject();

            logToStderr("Received: " + method);

            // 分发请求
            Object result = dispatchMethod(method, params);

            // 如果有 id，发送响应（通知没有 id）
            if (id != null && result != null) {
                sendResponse(id, result);
            }

        } catch (Exception e) {
            logToStderr("Error handling message: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * 方法分发
     */
    private Object dispatchMethod(String method, JsonObject params) {
        return switch (method) {
            // 生命周期方法
            case "build/initialize" -> handleInitialize(params);
            case "build/initialized" -> { initialized = true; yield null; }
            case "build/shutdown" -> { running = false; yield new JsonObject(); }
            case "build/exit" -> { System.exit(0); yield null; }

            // 工作区方法
            case "workspace/buildTargets" -> handler.handleBuildTargets();
            case "workspace/reload" -> handler.handleReload();

            // 构建目标方法
            case "buildTarget/sources" -> handler.handleSources(params);
            case "buildTarget/inverseSources" -> handler.handleInverseSources(params);
            case "buildTarget/dependencySources" -> handler.handleDependencySources(params);
            case "buildTarget/resources" -> handler.handleResources(params);
            case "buildTarget/compile" -> handler.handleCompile(params);
            case "buildTarget/run" -> handler.handleRun(params);
            case "buildTarget/test" -> handler.handleTest(params);
            case "buildTarget/cleanCache" -> handler.handleCleanCache(params);

            // Java 特定方法
            case "buildTarget/javacOptions" -> handler.handleJavacOptions(params);
            case "buildTarget/jvmRunEnvironment" -> handler.handleJvmRunEnvironment(params);
            case "buildTarget/jvmTestEnvironment" -> handler.handleJvmTestEnvironment(params);

            default -> {
                logToStderr("Unknown method: " + method);
                yield null;
            }
        };
    }

    /**
     * 处理 build/initialize
     */
    private Object handleInitialize(JsonObject params) {
        handler.initialize();

        JsonObject capabilities = new JsonObject();

        // 编译能力
        JsonObject compileProvider = new JsonObject();
        JsonArray compileLanguages = new JsonArray();
        compileLanguages.add("java");
        compileProvider.add("languageIds", compileLanguages);
        capabilities.add("compileProvider", compileProvider);

        // 运行能力
        JsonObject runProvider = new JsonObject();
        JsonArray runLanguages = new JsonArray();
        runLanguages.add("java");
        runProvider.add("languageIds", runLanguages);
        capabilities.add("runProvider", runProvider);

        // 测试能力
        JsonObject testProvider = new JsonObject();
        JsonArray testLanguages = new JsonArray();
        testLanguages.add("java");
        testProvider.add("languageIds", testLanguages);
        capabilities.add("testProvider", testProvider);

        // 其他能力
        capabilities.addProperty("canReload", true);
        capabilities.addProperty("jvmRunEnvironmentProvider", true);
        capabilities.addProperty("jvmTestEnvironmentProvider", true);

        JsonObject result = new JsonObject();
        result.addProperty("displayName", SERVER_NAME);
        result.addProperty("version", SERVER_VERSION);
        result.addProperty("bspVersion", BSP_VERSION);
        result.add("capabilities", capabilities);

        return result;
    }

    /**
     * 日志输出到 stderr（不干扰 stdio 通信）
     */
    public void logToStderr(String message) {
        System.err.println("[QinBSP] " + message);
    }

    /**
     * 主入口
     */
    public static void main(String[] args) throws IOException {
        String workDir = args.length > 0 ? args[0] : QinConstants.getCwd();
        new QinBspServer(workDir).start();
    }
}
