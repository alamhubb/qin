package com.qin.core;

import com.qin.types.*;

import java.util.*;

/**
 * Plugin Manager for Qin
 * Vite-style plugin architecture
 */
public class PluginManager {
    private final List<QinPlugin> plugins = new ArrayList<>();
    private final Map<String, QinPlugin> languagePlugins = new HashMap<>();

    public PluginManager() {
    }

    public PluginManager(List<QinPlugin> plugins) {
        if (plugins != null) {
            List<QinPlugin> flattened = flattenPlugins(plugins);
            for (QinPlugin plugin : flattened) {
                register(plugin);
            }
        }
    }

    /**
     * Flatten plugin list (expand sub-plugins)
     */
    public static List<QinPlugin> flattenPlugins(List<QinPlugin> plugins) {
        List<QinPlugin> flattened = new ArrayList<>();

        for (QinPlugin plugin : plugins) {
            flattened.add(plugin);

            List<QinPlugin> subPlugins = plugin.getPlugins();
            if (subPlugins != null && !subPlugins.isEmpty()) {
                flattened.addAll(flattenPlugins(subPlugins));
            }
        }

        // Deduplicate by name (later overrides earlier)
        Map<String, QinPlugin> seen = new LinkedHashMap<>();
        for (QinPlugin plugin : flattened) {
            seen.put(plugin.getName(), plugin);
        }

        return new ArrayList<>(seen.values());
    }

    private void register(QinPlugin plugin) {
        plugins.add(plugin);

        LanguageSupport language = plugin.getLanguage();
        if (language != null) {
            for (String ext : language.getExtensions()) {
                languagePlugins.put(ext, plugin);
            }
        }
    }

    /**
     * Add a plugin
     */
    public void add(QinPlugin plugin) {
        List<QinPlugin> flattened = flattenPlugins(Collections.singletonList(plugin));
        for (QinPlugin p : flattened) {
            // Replace existing plugin with same name
            plugins.removeIf(existing -> existing.getName().equals(p.getName()));
            register(p);
        }
    }

    public List<QinPlugin> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }

    public QinPlugin getLanguagePlugin(String extension) {
        return languagePlugins.get(extension);
    }

    public List<QinPlugin> getLanguagePlugins() {
        return new ArrayList<>(new HashSet<>(languagePlugins.values()));
    }

    /**
     * Run config hooks
     */
    public QinConfig runConfigHooks(QinConfig config) {
        QinConfig result = config;
        for (QinPlugin plugin : plugins) {
            QinConfig modified = plugin.config(result);
            if (modified != null) {
                result = modified;
            }
        }
        return result;
    }

    /**
     * Run configResolved hooks
     */
    public void runConfigResolvedHooks(QinConfig config) {
        for (QinPlugin plugin : plugins) {
            plugin.configResolved(config);
        }
    }

    /**
     * Run lifecycle hooks
     */
    public void runHook(String hookName, PluginContext ctx) {
        for (QinPlugin plugin : plugins) {
            switch (hookName) {
                case "beforeCompile" -> plugin.beforeCompile(ctx);
                case "afterCompile" -> plugin.afterCompile(ctx);
                case "beforeRun" -> plugin.beforeRun(ctx);
                case "afterRun" -> plugin.afterRun(ctx);
                case "beforeBuild" -> plugin.beforeBuild(ctx);
                case "afterBuild" -> plugin.afterBuild(ctx);
                case "devServer" -> plugin.devServer(ctx);
            }
        }
    }

    /**
     * Run cleanup hooks
     */
    public void runCleanup() {
        for (QinPlugin plugin : plugins) {
            plugin.cleanup();
        }
    }

    /**
     * Create plugin context
     */
    public PluginContext createContext(QinConfig config, boolean isDev) {
        return new PluginContext(System.getProperty("user.dir"), config, isDev);
    }
}
