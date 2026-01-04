package com.qin.debug;

import java.util.Set;

/**
 * Qin IDEA 插件常量配置
 */
public final class QinConstants {

    private QinConstants() {
    } // 禁止实例化

    // ==================== 配置文件 ====================

    /**
     * Qin 配置文件名
     */
    public static final String CONFIG_FILE = "qin.config.json";

    // ==================== 目录相关 ====================

    /**
     * 排除的目录（扫描时跳过）
     */
    public static final Set<String> EXCLUDED_DIRS = Set.of(
            "node_modules",
            ".git",
            ".qin",
            "dist",
            "build",
            ".cache",
            ".vscode",
            ".idea",
            "out",
            "target");

    /**
     * 项目根目录标志（workspace root 识别）
     */
    public static final Set<String> PROJECT_ROOT_MARKERS = Set.of(
            ".idea",
            ".vscode",
            ".git");

    /**
     * 隐藏文件/目录前缀
     */
    public static final String HIDDEN_PREFIX = ".";

    /**
     * 当前目录符号
     */
    public static final String CURRENT_DIR = ".";

    // ==================== 树节点名称 ====================

    /**
     * 任务节点名称
     */
    public static final String NODE_TASKS = "Tasks";

    /**
     * 依赖节点名称
     */
    public static final String NODE_DEPENDENCIES = "Dependencies";

    // ==================== 日志目录 ====================

    /**
     * IDEA 插件日志目录名
     */
    public static final String LOG_DIR_NAME = ".qin-idea";

    /**
     * 日志文件扩展名
     */
    public static final String LOG_FILE_EXT = ".log";

    /**
     * 日志子目录名
     */
    public static final String LOG_SUBDIR = "logs";

    // ==================== 命令相关 ====================

    /**
     * 命令前缀（Windows）
     */
    public static final String CMD_PREFIX = "cmd";

    /**
     * 命令参数（Windows）
     */
    public static final String CMD_FLAG = "/c";

    /**
     * Qin CLI 命令
     */
    public static final String QIN_CMD = "qin";

    // ==================== 扫描配置 ====================

    /**
     * 最大扫描深度
     */
    public static final int MAX_SCAN_DEPTH = 20;

    /**
     * 字符编码
     */
    public static final String CHARSET_UTF8 = "UTF-8";
}
