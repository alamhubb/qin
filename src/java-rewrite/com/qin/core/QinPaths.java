package com.qin.core;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Qin路径常量配置
 * 所有路径相关的常量都在这里定义，避免硬编码
 * 这些常量不可修改，各模块统一使用
 * 
 * 目录结构:
 * 
 * 项目目录/
 * ├── build/
 * │ └── classes/ # OUTPUT_DIR - 编译输出
 * ├── .qin/
 * │ ├── classpath.json # CLASSPATH_CACHE - 依赖缓存
 * │ └── libs/ # LOCAL_LIBS_DIR - 本地项目依赖jar
 * └── src/
 * 
 * 全局目录 (~/.qin/):
 * └── libs/ # GLOBAL_LIBS_DIR - 全局依赖jar缓存
 */
public final class QinPaths {

    private QinPaths() {
    } // 工具类，禁止实例化

    // ==================== 编译输出目录 ====================

    /**
     * 编译输出目录 (相对于项目根目录)
     * 遵循行业标准: build/classes
     */
    public static final String OUTPUT_DIR = "build/classes";

    // ==================== Qin配置目录 ====================

    /**
     * Qin配置目录 (相对于项目根目录)
     */
    public static final String QIN_DIR = ".qin";

    /**
     * 依赖classpath缓存文件 (相对于项目根目录)
     */
    public static final String CLASSPATH_CACHE = ".qin/classpath.json";

    // ==================== 依赖库目录 ====================

    /**
     * 依赖库目录 (相对路径)
     * 本地: {projectRoot}/.qin/libs
     * 全局: ~/.qin/libs
     */
    public static final String LIBS_DIR = ".qin/libs";

    // ==================== 便捷方法 ====================

    /**
     * 获取绝对输出目录路径
     */
    public static Path getOutputDir(String projectRoot) {
        return Paths.get(projectRoot, OUTPUT_DIR);
    }

    /**
     * 获取classpath缓存文件绝对路径
     */
    public static Path getClasspathCache(String projectRoot) {
        return Paths.get(projectRoot, CLASSPATH_CACHE);
    }

    /**
     * 获取本地libs目录绝对路径
     */
    public static Path getLocalLibsDir(String projectRoot) {
        return Paths.get(projectRoot, LIBS_DIR);
    }

    /**
     * 获取全局libs目录绝对路径
     * 
     * @return ~/.qin/libs
     */
    public static Path getGlobalLibsDir() {
        return Paths.get(System.getProperty("user.home"), LIBS_DIR);
    }

    /**
     * 获取Qin配置目录绝对路径
     */
    public static Path getQinDir(String projectRoot) {
        return Paths.get(projectRoot, QIN_DIR);
    }
}
