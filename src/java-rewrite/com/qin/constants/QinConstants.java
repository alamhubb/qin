package com.qin.constants;

/**
 * Qin 构建系统常量
 */
public class QinConstants {

    // 依赖坐标分隔符
    /**
     * Qin 配置文件中使用的分隔符
     * 例如: com.google.code.gson@gson
     */
    public static final String QIN_COORDINATE_SEPARATOR = "@";

    /**
     * Maven/Coursier 使用的分隔符
     * 例如: com.google.code.gson:gson:2.10.1
     */
    public static final String MAVEN_COORDINATE_SEPARATOR = ":";

    /**
     * 版本分隔符
     * 例如: gson-2.10.1
     */
    public static final String VERSION_SEPARATOR = "-";

    /**
     * 将 Qin 坐标转换为 Maven 坐标
     * 
     * @param qinCoordinate Qin 格式: groupId@artifactId
     * @return Maven 格式: groupId:artifactId
     */
    public static String toMavenCoordinate(String qinCoordinate) {
        return qinCoordinate.replace(QIN_COORDINATE_SEPARATOR, MAVEN_COORDINATE_SEPARATOR);
    }

    /**
     * 将 Maven 坐标转换为 Qin 坐标
     * 
     * @param mavenCoordinate Maven 格式: groupId:artifactId
     * @return Qin 格式: groupId@artifactId
     */
    public static String toQinCoordinate(String mavenCoordinate) {
        return mavenCoordinate.replace(MAVEN_COORDINATE_SEPARATOR, QIN_COORDINATE_SEPARATOR);
    }

    // ==================== 扫描配置 ====================

    /**
     * 配置文件名
     */
    public static final String CONFIG_FILE = "qin.config.json";

    /**
     * 最大扫描深度
     */
    public static final int MAX_SCAN_DEPTH = 20;

    /**
     * 排除的目录（扫描时跳过）
     */
    public static final java.util.Set<String> EXCLUDED_DIRS = java.util.Set.of(
            "node_modules", ".git", ".qin", "dist", "build", ".cache",
            ".vscode", ".idea", "out", "target", "libs");

    /**
     * 项目根目录标志
     */
    public static final java.util.Set<String> PROJECT_ROOT_MARKERS = java.util.Set.of(
            ".idea", ".vscode", ".git");

    // ==================== 目录和文件常量 ====================

    /**
     * Qin 缓存目录名
     */
    public static final String QIN_DIR = ".qin";

    /**
     * 日志子目录名
     */
    public static final String LOG_SUBDIR = "logs";

    /**
     * 日志文件扩展名
     */
    public static final String LOG_FILE_EXT = ".log";

    /**
     * Qin CLI 命令名
     */
    public static final String QIN_CMD = "qin";

    /**
     * 字符编码
     */
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * 隐藏文件/目录前缀
     */
    public static final String HIDDEN_PREFIX = ".";

    /**
     * 当前目录符号
     */
    public static final String CURRENT_DIR = ".";

    // ==================== 命令相关（跨平台）====================

    /**
     * 命令前缀（Windows）
     */
    public static final String CMD_PREFIX = "cmd";

    /**
     * 命令参数（Windows）
     */
    public static final String CMD_FLAG = "/c";

    private QinConstants() {
        // 工具类，禁止实例化
    }
}
