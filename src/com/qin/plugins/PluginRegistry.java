package com.qin.plugins;

import java.nio.file.Path;
import java.util.*;

/**
 * 插件注册表
 * 管理所有运行器插件，根据文件扩展名查找对应插件
 */
public class PluginRegistry {

    private static final PluginRegistry INSTANCE = new PluginRegistry();

    private final Map<String, RunnerPlugin> extensionMap = new HashMap<>();
    private final List<RunnerPlugin> plugins = new ArrayList<>();

    private PluginRegistry() {
        // 注册内置插件
        register(new JsPlugin());
        // JavaPlugin 将在后续添加
    }

    public static PluginRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * 注册插件
     */
    public void register(RunnerPlugin plugin) {
        plugins.add(plugin);
        for (String ext : plugin.supportedExtensions()) {
            extensionMap.put(ext.toLowerCase(), plugin);
        }
    }

    /**
     * 根据文件路径获取对应的插件
     * 
     * @param file 文件路径
     * @return 插件，如果没有找到则返回 null
     */
    public RunnerPlugin getPlugin(Path file) {
        String fileName = file.getFileName().toString();
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot == -1) {
            return null;
        }
        String ext = fileName.substring(lastDot).toLowerCase();
        return extensionMap.get(ext);
    }

    /**
     * 根据扩展名获取对应的插件
     * 
     * @param extension 扩展名（如 ".js"）
     * @return 插件，如果没有找到则返回 null
     */
    public RunnerPlugin getPluginByExtension(String extension) {
        return extensionMap.get(extension.toLowerCase());
    }

    /**
     * 获取所有支持的扩展名
     */
    public Set<String> getSupportedExtensions() {
        return new HashSet<>(extensionMap.keySet());
    }

    /**
     * 获取所有注册的插件
     */
    public List<RunnerPlugin> getAllPlugins() {
        return new ArrayList<>(plugins);
    }
}
