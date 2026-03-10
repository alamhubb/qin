package com.qin.bsp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qin.constants.QinConstants;
import com.qin.types.QinConfig;
import com.qin.core.ConfigLoader;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * BSP 请求处理器
 * 实现标准 BSP 2.1.0 协议方法
 */
public class QinBspHandler {

    private final String workDir;
    private final QinBspServer server;
    private final Gson gson = new Gson();

    private QinConfig config;
    private List<String> classpath;
    private String targetUri;

    // 任务 ID 计数器
    private final AtomicInteger taskIdCounter = new AtomicInteger(0);

    public QinBspHandler(String workDir, QinBspServer server) {
        this.workDir = workDir;
        this.server = server;
        this.targetUri = Paths.get(workDir).toUri().toString();
    }

    /**
     * 初始化：加载项目配置
     */
    public void initialize() {
        loadConfig();
        loadClasspath();
        server.logToStderr("Project initialized: " + workDir);
    }

    private void loadConfig() {
        try {
            ConfigLoader loader = new ConfigLoader(workDir);
            config = loader.load();
        } catch (Exception e) {
            server.logToStderr("Failed to load config: " + e.getMessage());
            config = null;
        }
    }

    private void loadClasspath() {
        classpath = new ArrayList<>();
        try {
            Path cpPath = Paths.get(workDir, ".qin", "classpath.json");
            if (Files.exists(cpPath)) {
                String json = Files.readString(cpPath);
                JsonObject obj = gson.fromJson(json, JsonObject.class);
                if (obj.has("classpath")) {
                    obj.getAsJsonArray("classpath")
                       .forEach(e -> classpath.add(e.getAsString()));
                }
            }
        } catch (IOException e) {
            server.logToStderr("Failed to load classpath: " + e.getMessage());
        }
    }

    // ==================== workspace 方法 ====================

    /**
     * workspace/buildTargets - 返回所有构建目标
     */
    public Object handleBuildTargets() {
        JsonObject target = new JsonObject();

        // Build Target ID
        JsonObject targetId = new JsonObject();
        targetId.addProperty("uri", targetUri);
        target.add("id", targetId);

        // 基本信息
        String name = config != null && config.name() != null
            ? config.name() : "qin-project";
        target.addProperty("displayName", name);

        // 语言
        JsonArray languages = new JsonArray();
        languages.add("java");
        target.add("languageIds", languages);

        // 标签
        JsonArray tags = new JsonArray();
        tags.add("application");
        target.add("tags", tags);

        // 能力
        JsonObject capabilities = new JsonObject();
        capabilities.addProperty("canCompile", true);
        capabilities.addProperty("canRun", true);
        capabilities.addProperty("canTest", true);
        target.add("capabilities", capabilities);

        // 基础目录
        target.addProperty("baseDirectory", targetUri);

        // JVM 数据
        target.addProperty("dataKind", "jvm");
        JsonObject data = new JsonObject();
        data.addProperty("javaHome", System.getProperty("java.home"));
        data.addProperty("javaVersion", getJavaVersion());
        target.add("data", data);

        // 返回结果
        JsonObject result = new JsonObject();
        JsonArray targets = new JsonArray();
        targets.add(target);
        result.add("targets", targets);

        return result;
    }

    /**
     * workspace/reload - 重新加载项目
     */
    public Object handleReload() {
        server.logToStderr("Reloading project...");
        loadConfig();
        loadClasspath();
        return new JsonObject(); // 返回空对象表示成功
    }

    // ==================== buildTarget 方法 ====================

    /**
     * buildTarget/sources - 返回源文件目录
     */
    public Object handleSources(JsonObject params) {
        String sourceDir = getSourceDir();
        Path srcPath = Paths.get(workDir, sourceDir);
        String srcUri = srcPath.toUri().toString();

        JsonObject source = new JsonObject();
        source.addProperty("uri", srcUri);
        source.addProperty("kind", 1); // 1 = directory
        source.addProperty("generated", false);

        JsonArray sources = new JsonArray();
        sources.add(source);

        JsonArray roots = new JsonArray();
        roots.add(srcUri);

        JsonObject item = new JsonObject();
        JsonObject targetId = new JsonObject();
        targetId.addProperty("uri", targetUri);
        item.add("target", targetId);
        item.add("sources", sources);
        item.add("roots", roots);

        JsonObject result = new JsonObject();
        JsonArray items = new JsonArray();
        items.add(item);
        result.add("items", items);

        return result;
    }

    /**
     * buildTarget/inverseSources - 查找包含某文件的构建目标
     */
    public Object handleInverseSources(JsonObject params) {
        JsonObject result = new JsonObject();
        JsonArray targets = new JsonArray();

        // 简单实现：返回主构建目标
        JsonObject targetId = new JsonObject();
        targetId.addProperty("uri", targetUri);
        targets.add(targetId);

        result.add("targets", targets);
        return result;
    }

    /**
     * buildTarget/dependencySources - 返回依赖的源文件
     */
    public Object handleDependencySources(JsonObject params) {
        JsonArray sourceUris = new JsonArray();

        for (String path : classpath) {
            Path p = Paths.get(path);
            if (Files.exists(p)) {
                if (path.endsWith(".jar")) {
                    sourceUris.add("jar:" + p.toUri() + "!/");
                } else {
                    sourceUris.add(p.toUri().toString());
                }
            }
        }

        JsonObject item = new JsonObject();
        JsonObject targetId = new JsonObject();
        targetId.addProperty("uri", targetUri);
        item.add("target", targetId);
        item.add("sources", sourceUris);

        JsonObject result = new JsonObject();
        JsonArray items = new JsonArray();
        items.add(item);
        result.add("items", items);

        return result;
    }

    /**
     * buildTarget/resources - 返回资源目录
     */
    public Object handleResources(JsonObject params) {
        JsonArray resources = new JsonArray();

        Path resourcesPath = Paths.get(workDir, "src/main/resources");
        if (Files.exists(resourcesPath)) {
            resources.add(resourcesPath.toUri().toString());
        }

        JsonObject item = new JsonObject();
        JsonObject targetId = new JsonObject();
        targetId.addProperty("uri", targetUri);
        item.add("target", targetId);
        item.add("resources", resources);

        JsonObject result = new JsonObject();
        JsonArray items = new JsonArray();
        items.add(item);
        result.add("items", items);

        return result;
    }

    // ==================== 编译/运行/测试 方法 ====================

    /**
     * buildTarget/compile - 编译项目
     * 这是核心方法，IDE 检测到文件变化后会调用此方法
     */
    public Object handleCompile(JsonObject params) {
        String taskId = "compile-" + taskIdCounter.incrementAndGet();
        String originId = params.has("originId")
            ? params.get("originId").getAsString() : "";

        // 发送任务开始通知
        sendTaskStart(taskId, "Compiling...");

        try {
            // 执行 qin compile
            ProcessBuilder pb = new ProcessBuilder(
                QinConstants.CMD_PREFIX,
                QinConstants.CMD_FLAG,
                "qin", "compile"
            );
            pb.directory(new File(workDir));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // 读取输出并发送日志
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sendLogMessage(taskId, line);
                }
            }

            int exitCode = process.waitFor();

            // 发送任务完成通知
            sendTaskFinish(taskId, exitCode == 0);

            // 🔑 关键：通知 IDE 构建目标已变化，触发刷新
            if (exitCode == 0) {
                sendBuildTargetDidChange();
            }

            // 返回编译结果
            JsonObject result = new JsonObject();
            result.addProperty("originId", originId);
            result.addProperty("statusCode", exitCode == 0 ? 1 : 2);
            result.addProperty("dataKind", "compile-report");
            result.add("data", new JsonObject());

            return result;

        } catch (Exception e) {
            sendTaskFinish(taskId, false);

            JsonObject result = new JsonObject();
            result.addProperty("originId", originId);
            result.addProperty("statusCode", 2); // error
            return result;
        }
    }

    /**
     * buildTarget/run - 运行项目
     */
    public Object handleRun(JsonObject params) {
        String taskId = "run-" + taskIdCounter.incrementAndGet();
        String originId = params.has("originId")
            ? params.get("originId").getAsString() : "";

        sendTaskStart(taskId, "Running...");

        try {
            ProcessBuilder pb = new ProcessBuilder(
                QinConstants.CMD_PREFIX,
                QinConstants.CMD_FLAG,
                "qin", "run"
            );
            pb.directory(new File(workDir));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sendLogMessage(taskId, line);
                }
            }

            int exitCode = process.waitFor();
            sendTaskFinish(taskId, exitCode == 0);

            JsonObject result = new JsonObject();
            result.addProperty("originId", originId);
            result.addProperty("statusCode", exitCode == 0 ? 1 : 2);
            return result;

        } catch (Exception e) {
            sendTaskFinish(taskId, false);
            JsonObject result = new JsonObject();
            result.addProperty("originId", originId);
            result.addProperty("statusCode", 2);
            return result;
        }
    }

    /**
     * buildTarget/test - 运行测试
     */
    public Object handleTest(JsonObject params) {
        String taskId = "test-" + taskIdCounter.incrementAndGet();
        String originId = params.has("originId")
            ? params.get("originId").getAsString() : "";

        sendTaskStart(taskId, "Testing...");

        try {
            ProcessBuilder pb = new ProcessBuilder(
                QinConstants.CMD_PREFIX,
                QinConstants.CMD_FLAG,
                "qin", "test"
            );
            pb.directory(new File(workDir));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sendLogMessage(taskId, line);
                }
            }

            int exitCode = process.waitFor();
            sendTaskFinish(taskId, exitCode == 0);

            JsonObject result = new JsonObject();
            result.addProperty("originId", originId);
            result.addProperty("statusCode", exitCode == 0 ? 1 : 2);
            return result;

        } catch (Exception e) {
            sendTaskFinish(taskId, false);
            JsonObject result = new JsonObject();
            result.addProperty("originId", originId);
            result.addProperty("statusCode", 2);
            return result;
        }
    }

    /**
     * buildTarget/cleanCache - 清理缓存
     */
    public Object handleCleanCache(JsonObject params) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                QinConstants.CMD_PREFIX,
                QinConstants.CMD_FLAG,
                "qin", "clean"
            );
            pb.directory(new File(workDir));
            pb.inheritIO();
            Process process = pb.start();
            process.waitFor();

            JsonObject result = new JsonObject();
            result.addProperty("cleaned", true);
            return result;
        } catch (Exception e) {
            JsonObject result = new JsonObject();
            result.addProperty("cleaned", false);
            return result;
        }
    }

    // ==================== Java 特定方法 ====================

    /**
     * buildTarget/javacOptions - 返回 javac 编译选项
     */
    public Object handleJavacOptions(JsonObject params) {
        String javaVersion = getJavaVersion();

        JsonArray options = new JsonArray();
        options.add("--release");
        options.add(javaVersion);

        JsonArray cpUris = new JsonArray();
        for (String path : classpath) {
            Path p = Paths.get(path);
            if (Files.exists(p)) {
                cpUris.add(p.toUri().toString());
            }
        }

        String classDir = Paths.get(workDir, QinConstants.BUILD_CLASSES_DIR)
            .toUri().toString();

        JsonObject item = new JsonObject();
        JsonObject targetId = new JsonObject();
        targetId.addProperty("uri", targetUri);
        item.add("target", targetId);
        item.add("options", options);
        item.add("classpath", cpUris);
        item.addProperty("classDirectory", classDir);

        JsonObject result = new JsonObject();
        JsonArray items = new JsonArray();
        items.add(item);
        result.add("items", items);

        return result;
    }

    /**
     * buildTarget/jvmRunEnvironment - JVM 运行环境
     */
    public Object handleJvmRunEnvironment(JsonObject params) {
        return buildJvmEnvironment();
    }

    /**
     * buildTarget/jvmTestEnvironment - JVM 测试环境
     */
    public Object handleJvmTestEnvironment(JsonObject params) {
        return buildJvmEnvironment();
    }

    private Object buildJvmEnvironment() {
        JsonArray cpUris = new JsonArray();
        for (String path : classpath) {
            cpUris.add(path);
        }
        // 添加 build/classes
        cpUris.add(Paths.get(workDir, QinConstants.BUILD_CLASSES_DIR).toString());

        JsonObject item = new JsonObject();
        JsonObject targetId = new JsonObject();
        targetId.addProperty("uri", targetUri);
        item.add("target", targetId);
        item.add("classpath", cpUris);
        item.add("jvmOptions", new JsonArray());
        item.addProperty("workingDirectory", workDir);
        item.add("environmentVariables", new JsonObject());

        // 主类
        String mainClass = getMainClass();
        if (mainClass != null) {
            item.addProperty("mainClass", mainClass);
        }

        JsonObject result = new JsonObject();
        JsonArray items = new JsonArray();
        items.add(item);
        result.add("items", items);

        return result;
    }

    // ==================== 通知方法 ====================

    private void sendTaskStart(String taskId, String message) {
        JsonObject params = new JsonObject();
        JsonObject taskIdObj = new JsonObject();
        taskIdObj.addProperty("id", taskId);
        params.add("taskId", taskIdObj);
        params.addProperty("message", message);
        params.addProperty("eventTime", System.currentTimeMillis());
        server.sendNotification("build/taskStart", params);
    }

    private void sendTaskFinish(String taskId, boolean success) {
        JsonObject params = new JsonObject();
        JsonObject taskIdObj = new JsonObject();
        taskIdObj.addProperty("id", taskId);
        params.add("taskId", taskIdObj);
        params.addProperty("eventTime", System.currentTimeMillis());
        params.addProperty("status", success ? 1 : 2); // 1=ok, 2=error
        server.sendNotification("build/taskFinish", params);
    }

    private void sendLogMessage(String taskId, String message) {
        JsonObject params = new JsonObject();
        JsonObject taskIdObj = new JsonObject();
        taskIdObj.addProperty("id", taskId);
        params.add("taskId", taskIdObj);
        params.addProperty("message", message);
        server.sendNotification("build/logMessage", params);
    }

    /**
     * 通知 IDE 构建目标已变化
     * BSP 规范：buildTarget/didChange
     * 这会触发 IDE 重新加载编译输出，解决"刷新问题"
     */
    private void sendBuildTargetDidChange() {
        JsonObject params = new JsonObject();
        JsonArray changes = new JsonArray();

        JsonObject change = new JsonObject();
        JsonObject targetId = new JsonObject();
        targetId.addProperty("uri", targetUri);
        change.add("target", targetId);
        change.addProperty("kind", 1); // 1 = changed

        changes.add(change);
        params.add("changes", changes);

        server.sendNotification("buildTarget/didChange", params);
    }

    // ==================== 辅助方法 ====================

    private String getSourceDir() {
        if (config != null && config.java() != null
            && config.java().sourceDir() != null) {
            return config.java().sourceDir();
        }
        // 检查常用目录
        if (Files.exists(Paths.get(workDir, "src/main/java"))) {
            return "src/main/java";
        }
        return "src";
    }

    private String getJavaVersion() {
        if (config != null && config.java() != null
            && config.java().version() != null) {
            return config.java().version();
        }
        return "21"; // 默认 Java 21
    }

    private String getMainClass() {
        if (config != null && config.entry() != null) {
            String entry = config.entry();
            if (entry.endsWith(".java")) {
                String className = entry.substring(0, entry.length() - 5);
                className = className.replaceFirst("^src/(main/java/)?", "");
                className = className.replace('/', '.').replace('\\', '.');
                return className;
            }
        }
        return null;
    }
}
