package com.qin.plugins;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * qin-plugin-java-hot-reload
 * Java 热重载插件
 * 
 * 功能：
 * - 监听 Java 文件变化
 * - 自动重新编译
 * - 防抖处理
 */
public class HotReloadPlugin implements QinPlugin {
    private final HotReloadOptions options;
    private HotReloadManager manager;
    private boolean enabled = true;

    public HotReloadPlugin() {
        this(new HotReloadOptions());
    }

    public HotReloadPlugin(HotReloadOptions options) {
        this.options = options != null ? options : new HotReloadOptions();
    }

    @Override
    public String getName() {
        return "qin-plugin-java-hot-reload";
    }

    @Override
    public void configResolved(Map<String, Object> config) {
        // 如果使用 DevTools，禁用 Qin 热重载
        if (Boolean.TRUE.equals(config.get("_useDevTools"))) {
            enabled = false;
            System.out.println("[hot-reload] 检测到 DevTools，Qin 热重载已禁用");
        }
    }

    @Override
    public void devServer(PluginContext ctx) {
        if (!enabled) return;
        
        manager = new HotReloadManager(options);
        manager.start();
    }

    @Override
    public void cleanup() {
        if (manager != null) {
            manager.stop();
            manager = null;
        }
    }

    /**
     * 设置重编译回调
     */
    public void setRecompileCallback(Runnable callback) {
        if (manager != null) {
            manager.setRecompileCallback(callback);
        }
    }

    /**
     * 创建热重载插件
     */
    public static HotReloadPlugin create() {
        return new HotReloadPlugin();
    }

    public static HotReloadPlugin create(HotReloadOptions options) {
        return new HotReloadPlugin(options);
    }

    /**
     * 创建禁用的热重载插件
     */
    public static HotReloadPlugin disabled() {
        HotReloadPlugin plugin = new HotReloadPlugin();
        plugin.enabled = false;
        return plugin;
    }
}

/**
 * 热重载配置
 */
class HotReloadOptions {
    /** 监听目录，默认 src */
    private String watchDir = "src";
    
    /** 防抖延迟（毫秒），默认 300 */
    private int debounce = 300;
    
    /** 详细日志 */
    private boolean verbose = false;

    public String getWatchDir() { return watchDir; }
    public void setWatchDir(String watchDir) { this.watchDir = watchDir; }

    public int getDebounce() { return debounce; }
    public void setDebounce(int debounce) { this.debounce = debounce; }

    public boolean isVerbose() { return verbose; }
    public void setVerbose(boolean verbose) { this.verbose = verbose; }
}

/**
 * 热重载管理器
 */
class HotReloadManager {
    private final HotReloadOptions options;
    private final String cwd;
    private WatchService watchService;
    private Thread watchThread;
    private volatile boolean running = false;
    private ScheduledExecutorService debounceExecutor;
    private ScheduledFuture<?> debounceTask;
    private Runnable recompileCallback;

    public HotReloadManager(HotReloadOptions options) {
        this.options = options;
        this.cwd = System.getProperty("user.dir");
    }

    /**
     * 设置重编译回调
     */
    public void setRecompileCallback(Runnable callback) {
        this.recompileCallback = callback;
    }

    /**
     * 开始监听
     */
    public void start() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            debounceExecutor = Executors.newSingleThreadScheduledExecutor();

            Path watchPath = Paths.get(cwd, options.getWatchDir());
            if (!Files.exists(watchPath)) {
                log("监听目录不存在: " + watchPath);
                return;
            }

            // 递归注册所有目录
            registerAll(watchPath);

            running = true;
            watchThread = new Thread(this::watchLoop, "hot-reload-watcher");
            watchThread.setDaemon(true);
            watchThread.start();

            log("热重载已启动，监听: " + watchPath);
        } catch (IOException e) {
            log("启动热重载失败: " + e.getMessage());
        }
    }

    /**
     * 停止监听
     */
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
    }

    private void registerAll(Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, java.nio.file.attribute.BasicFileAttributes attrs) throws IOException {
                dir.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void watchLoop() {
        while (running) {
            try {
                WatchKey key = watchService.poll(100, TimeUnit.MILLISECONDS);
                if (key == null) continue;

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    // 只处理 .java 文件
                    if (filename.toString().endsWith(".java")) {
                        logVerbose("检测到变化: " + filename);
                        scheduleRecompile();
                    }
                }

                key.reset();
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void scheduleRecompile() {
        if (debounceTask != null) {
            debounceTask.cancel(false);
        }

        debounceTask = debounceExecutor.schedule(() -> {
            log("文件变化，重新编译...");
            if (recompileCallback != null) {
                recompileCallback.run();
            }
        }, options.getDebounce(), TimeUnit.MILLISECONDS);
    }

    private void log(String msg) {
        System.out.println("[hot-reload] " + msg);
    }

    private void logVerbose(String msg) {
        if (options.isVerbose()) {
            System.out.println("[hot-reload] " + msg);
        }
    }
}
