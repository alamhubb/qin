package com.qin.types;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Qin 插件接口
 */
public interface QinPlugin {
    /** 插件名称 */
    String getName();
    
    /** 子插件列表 */
    default List<QinPlugin> getPlugins() { return null; }
    
    /** 语言支持 */
    default LanguageSupport getLanguage() { return null; }
    
    /** 配置钩子 */
    default QinConfig config(QinConfig config) { return config; }
    
    /** 配置解析完成钩子 */
    default void configResolved(QinConfig config) {}
    
    /** 编译前钩子 */
    default void beforeCompile(PluginContext ctx) {}
    
    /** 编译后钩子 */
    default void afterCompile(PluginContext ctx) {}
    
    /** 运行前钩子 */
    default void beforeRun(PluginContext ctx) {}
    
    /** 运行后钩子 */
    default void afterRun(PluginContext ctx) {}
    
    /** 构建前钩子 */
    default void beforeBuild(PluginContext ctx) {}
    
    /** 构建后钩子 */
    default void afterBuild(PluginContext ctx) {}
    
    /** 开发服务器钩子 */
    default void devServer(PluginContext ctx) {}
    
    /** 清理钩子 */
    default void cleanup() {}
}
