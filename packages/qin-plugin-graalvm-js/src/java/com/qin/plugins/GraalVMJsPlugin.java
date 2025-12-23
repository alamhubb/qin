package com.qin.plugins;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * qin-plugin-graalvm-js
 * GraalVM JavaScript/Node.js support for Qin build tool
 *
 * Features:
 * - Run JavaScript with GraalVM Node.js runtime
 * - Full Node.js API support
 * - npm ecosystem compatibility
 * - Java interop via --polyglot --jvm
 * - Hot reload support
 */
public class GraalVMJsPlugin implements QinPlugin {
    private final GraalVMJsPluginOptions options;
    private final GraalVMJsLanguageSupport languageSupport;
    private final GraalVMPlugin graalvmPlugin;

    public GraalVMJsPlugin() {
        this(new GraalVMJsPluginOptions());
    }

    public GraalVMJsPlugin(GraalVMJsPluginOptions options) {
        this.options = options;
        this.languageSupport = new GraalVMJsLanguageSupport(options);
        this.graalvmPlugin = new GraalVMPlugin(options.getGraalvm());
    }

    @Override
    public String getName() {
        return "qin-plugin-graalvm-js";
    }

    @Override
    public LanguageSupport getLanguage() {
        return languageSupport;
    }

    @Override
    public List<QinPlugin> getPlugins() {
        return List.of(graalvmPlugin);
    }

    @Override
    public Map<String, Object> config(Map<String, Object> config) {
        if (options.getEntry() != null && !config.containsKey("entry")) {
            Map<String, Object> newConfig = new HashMap<>(config);
            newConfig.put("entry", options.getEntry());
            return newConfig;
        }
        return config;
    }

    /**
     * 创建 GraalVM JavaScript 插件
     */
    public static GraalVMJsPlugin create() {
        return new GraalVMJsPlugin();
    }

    public static GraalVMJsPlugin create(GraalVMJsPluginOptions options) {
        return new GraalVMJsPlugin(options);
    }
}

/**
 * GraalVM JavaScript 插件配置
 */
class GraalVMJsPluginOptions {
    private String entry;
    private boolean hotReloadEnabled = true;
    private HotReloadOptions hotReloadOptions;
    private List<String> nodeArgs;
    private boolean javaInterop = false;
    private GraalVMPluginOptions graalvm;

    public String getEntry() { return entry; }
    public void setEntry(String entry) { this.entry = entry; }

    public boolean isHotReloadEnabled() { return hotReloadEnabled; }
    public void setHotReloadEnabled(boolean enabled) { this.hotReloadEnabled = enabled; }

    public HotReloadOptions getHotReloadOptions() { return hotReloadOptions; }
    public void setHotReloadOptions(HotReloadOptions options) { this.hotReloadOptions = options; }

    public List<String> getNodeArgs() { return nodeArgs; }
    public void setNodeArgs(List<String> nodeArgs) { this.nodeArgs = nodeArgs; }

    public boolean isJavaInterop() { return javaInterop; }
    public void setJavaInterop(boolean javaInterop) { this.javaInterop = javaInterop; }

    public GraalVMPluginOptions getGraalvm() { return graalvm; }
    public void setGraalvm(GraalVMPluginOptions graalvm) { this.graalvm = graalvm; }
}

/**
 * GraalVM JavaScript 语言支持
 */
class GraalVMJsLanguageSupport implements LanguageSupport {
    private final GraalVMJsPluginOptions options;
    private GraalVMInfo graalvmInfo;

    public GraalVMJsLanguageSupport(GraalVMJsPluginOptions options) {
        this.options = options;

        // 检测 GraalVM
        GraalVMDetectionResult detection = GraalVMPlugin.detectGraalVM(
            options.getGraalvm() != null ? options.getGraalvm().getHome() : null
        );
        if (detection.isFound()) {
            this.graalvmInfo = detection.getInfo();
        }
    }

    @Override
    public String getName() {
        return "graalvm-js";
    }

    @Override
    public List<String> getExtensions() {
        return List.of(".js", ".mjs");
    }

    @Override
    public CompileResult compile(CompileContext ctx) {
        // JavaScript 不需要编译，但可以验证语法
        List<String> jsFiles = new ArrayList<>();
        for (String file : ctx.getSourceFiles()) {
            if (file.endsWith(".js") || file.endsWith(".mjs")) {
                jsFiles.add(file);
            }
        }

        if (jsFiles.isEmpty()) {
            return CompileResult.success(0, ctx.getOutputDir());
        }

        String nodePath = graalvmInfo != null ? graalvmInfo.getNodePath() : null;
        if (nodePath == null) {
            return CompileResult.failure("GraalVM Node.js not found");
        }

        // 使用 --check 验证语法
        List<String> errors = new ArrayList<>();
        for (String file : jsFiles) {
            try {
                ProcessBuilder pb = new ProcessBuilder(nodePath, "--check", file);
                pb.redirectErrorStream(true);
                Process proc = pb.start();
                String output = readStream(proc.getInputStream());
                int exitCode = proc.waitFor();

                if (exitCode != 0) {
                    errors.add(file + ": " + output.trim());
                }
            } catch (Exception e) {
                errors.add(file + ": " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            return CompileResult.failure(String.join("\n", errors));
        }

        return CompileResult.success(jsFiles.size(), ctx.getOutputDir());
    }

    @Override
    public void run(RunContext ctx) throws Exception {
        String entry = options.getEntry();
        if (entry == null) {
            entry = (String) ctx.getConfig().get("entry");
        }
        if (entry == null) {
            throw new RuntimeException("No entry point specified");
        }

        String nodePath = graalvmInfo != null ? graalvmInfo.getNodePath() : null;
        if (nodePath == null) {
            throw new GraalVMNotFoundError("GraalVM Node.js not found");
        }

        // 检查 nodejs 组件是否安装
        if (graalvmInfo != null && !graalvmInfo.getComponents().contains("nodejs")) {
            throw new ComponentNotInstalledError("nodejs");
        }

        Path filePath = Paths.get(ctx.getRoot(), entry);
        if (!Files.exists(filePath)) {
            throw new RuntimeException("Entry file not found: " + filePath);
        }

        // 构建命令参数
        List<String> args = buildNodeArgs(filePath.toString(), ctx.getArgs());

        ProcessBuilder pb = new ProcessBuilder();
        List<String> command = new ArrayList<>();
        command.add(nodePath);
        command.addAll(args);
        pb.command(command);
        pb.directory(new File(ctx.getRoot()));
        pb.inheritIO();

        Process proc = pb.start();
        int exitCode = proc.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("JavaScript execution failed with code " + exitCode);
        }
    }

    private List<String> buildNodeArgs(String file, List<String> scriptArgs) {
        List<String> args = new ArrayList<>();

        // 添加额外的 Node.js 参数
        if (options.getNodeArgs() != null) {
            args.addAll(options.getNodeArgs());
        }

        // 启用 Java 互操作
        if (options.isJavaInterop()) {
            args.add("--polyglot");
            args.add("--jvm");
        }

        // 添加入口文件
        args.add(file);

        // 添加脚本参数
        if (scriptArgs != null) {
            args.addAll(scriptArgs);
        }

        return args;
    }

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }
}

/**
 * JavaScript 热重载管理器
 */
class JsHotReloadManager {
    private final String nodePath;
    private final String entry;
    private final String cwd;
    private final Map<String, String> env;
    private final List<String> args;
    private final boolean javaInterop;
    private final List<String> nodeArgs;
    private final int debounce;
    private final boolean verbose;

    private Process process;
    private WatchService watchService;
    private Thread watchThread;
    private volatile boolean running = false;
    private ScheduledExecutorService debounceExecutor;
    private ScheduledFuture<?> debounceTask;

    public JsHotReloadManager(String nodePath, String entry, String cwd,
                               Map<String, String> env, List<String> args,
                               boolean javaInterop, List<String> nodeArgs,
                               int debounce, boolean verbose) {
        this.nodePath = nodePath;
        this.entry = entry;
        this.cwd = cwd != null ? cwd : System.getProperty("user.dir");
        this.env = env != null ? env : new HashMap<>();
        this.args = args != null ? args : new ArrayList<>();
        this.javaInterop = javaInterop;
        this.nodeArgs = nodeArgs;
        this.debounce = debounce > 0 ? debounce : 500;
        this.verbose = verbose;
    }

    public void start() throws Exception {
        startProcess();
        startWatcher();
    }

    public void stop() {
        running = false;

        if (debounceTask != null) {
            debounceTask.cancel(false);
        }

        if (debounceExecutor != null) {
            debounceExecutor.shutdown();
        }

        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                // 忽略
            }
        }

        if (watchThread != null) {
            watchThread.interrupt();
        }

        stopProcess();
    }

    private void startProcess() throws Exception {
        List<String> command = new ArrayList<>();
        command.add(nodePath);

        if (nodeArgs != null) {
            command.addAll(nodeArgs);
        }

        if (javaInterop) {
            command.add("--polyglot");
            command.add("--jvm");
        }

        command.add(entry);
        command.addAll(args);

        if (verbose) {
            System.out.println("[hot-reload] Starting: " + String.join(" ", command));
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(cwd));
        pb.environment().putAll(env);
        pb.inheritIO();

        process = pb.start();
    }

    private void stopProcess() {
        if (process != null) {
            process.destroy();
            process = null;
        }
    }

    private void restart() throws Exception {
        if (verbose) {
            System.out.println("[hot-reload] Restarting...");
        }
        stopProcess();
        startProcess();
    }

    private void startWatcher() throws IOException {
        watchService = FileSystems.getDefault().newWatchService();
        debounceExecutor = Executors.newSingleThreadScheduledExecutor();

        Path watchDir = Paths.get(cwd, entry).getParent();
        if (watchDir == null) {
            watchDir = Paths.get(cwd);
        }

        watchDir.register(watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY);

        running = true;
        final Path finalWatchDir = watchDir;

        watchThread = new Thread(() -> {
            while (running) {
                try {
                    WatchKey key = watchService.poll(100, TimeUnit.MILLISECONDS);
                    if (key == null) continue;

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }

                        Path filename = (Path) event.context();
                        String name = filename.toString();

                        if (name.endsWith(".js") || name.endsWith(".mjs")) {
                            if (verbose) {
                                System.out.println("[hot-reload] File changed: " + name);
                            }
                            scheduleRestart();
                        }
                    }

                    key.reset();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "js-hot-reload-watcher");

        watchThread.setDaemon(true);
        watchThread.start();

        if (verbose) {
            System.out.println("[hot-reload] Watching " + finalWatchDir + " for changes...");
        }
    }

    private void scheduleRestart() {
        if (debounceTask != null) {
            debounceTask.cancel(false);
        }

        debounceTask = debounceExecutor.schedule(() -> {
            try {
                restart();
            } catch (Exception e) {
                System.err.println("[hot-reload] Restart failed: " + e.getMessage());
            }
        }, debounce, TimeUnit.MILLISECONDS);
    }
}
