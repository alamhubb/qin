package com.qin.plugins;

import java.nio.file.Path;
import java.util.Set;

/**
 * 运行器插件接口
 * 每个插件负责处理特定类型的文件
 */
public interface RunnerPlugin {

    /**
     * 插件名称
     */
    String name();

    /**
     * 支持的文件扩展名（包含点号，如 ".js"）
     */
    Set<String> supportedExtensions();

    /**
     * 运行文件
     * 
     * @param file    要运行的文件路径
     * @param args    传递给脚本的额外参数
     * @param workDir 工作目录
     * @throws Exception 运行失败时抛出异常
     */
    void run(Path file, String[] args, Path workDir) throws Exception;
}
