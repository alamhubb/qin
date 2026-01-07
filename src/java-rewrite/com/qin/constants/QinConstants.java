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
     * 项目根目录标志（用于扫描时识别）
     */
    public static final java.util.Set<String> PROJECT_ROOT_MARKERS = java.util.Set.of(
            ".idea", ".vscode", ".git");

    /**
     * 工作区根目录标志（用于向上查找根目录）
     * 优先级: .idea > qin.config.json > package.json > .git
     */
    public static final java.util.List<String> WORKSPACE_ROOT_MARKERS = java.util.List.of(
            ".idea", // IDEA 项目
            "qin.config.json", // Qin 项目
            "package.json", // JS/TS 项目
            ".git" // Git 仓库根
    );

    // ==================== 目录和文件常量 ====================

    /**
     * Qin 缓存目录名
     */
    public static final String QIN_DIR = ".qin";

    /**
     * 构建输出目录
     */
    public static final String BUILD_DIR = "build";

    /**
     * 编译输出目录（相对于项目根目录）
     */
    public static final String BUILD_CLASSES_DIR = "build/classes";

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

    // ==================== 默认值常量 ====================

    /**
     * 默认源代码目录
     */
    public static final String DEFAULT_SOURCE_DIR = "src/main/java";

    /**
     * 默认测试目录
     */
    public static final String DEFAULT_TEST_DIR = "src/test/java";

    /**
     * 默认 JAR 名称
     */
    public static final String DEFAULT_JAR_NAME = "app.jar";

    /**
     * 默认入口类
     */
    public static final String DEFAULT_MAIN_CLASS = "Main";

    // ==================== 配置获取工具方法 ====================

    /**
     * 获取源代码目录（安全获取，带默认值）
     * 
     * 【重要】所有需要获取源代码目录的地方都应该使用这个方法，
     * 不要直接使用 parsed.srcDir() 或其他方式。
     * 
     * @param javaConfig Java 配置对象（可能为 null）
     * @return 源代码目录路径
     */
    public static String getSourceDir(com.qin.types.JavaConfig javaConfig) {
        if (javaConfig != null && javaConfig.sourceDir() != null && !javaConfig.sourceDir().isEmpty()) {
            return javaConfig.sourceDir();
        }
        return DEFAULT_SOURCE_DIR;
    }

    /**
     * 获取测试目录（安全获取，带默认值）
     */
    public static String getTestDir(com.qin.types.JavaConfig javaConfig) {
        if (javaConfig != null && javaConfig.testDir() != null && !javaConfig.testDir().isEmpty()) {
            return javaConfig.testDir();
        }
        return DEFAULT_TEST_DIR;
    }

    /**
     * 获取输出目录（安全获取，带默认值）
     */
    public static String getOutputDir(com.qin.types.JavaConfig javaConfig) {
        if (javaConfig != null && javaConfig.outputDir() != null && !javaConfig.outputDir().isEmpty()) {
            return javaConfig.outputDir();
        }
        return BUILD_CLASSES_DIR; // 使用已有常量
    }

    /**
     * 获取 JAR 名称（安全获取，带默认值）
     */
    public static String getJarName(com.qin.types.OutputConfig outputConfig) {
        if (outputConfig != null && outputConfig.jarName() != null && !outputConfig.jarName().isEmpty()) {
            return outputConfig.jarName();
        }
        return DEFAULT_JAR_NAME;
    }

    /**
     * 判断是否为 Windows 系统
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * 获取类路径分隔符（Windows 用 ; ，其他用 :）
     */
    public static String getClasspathSeparator() {
        return isWindows() ? ";" : ":";
    }

    private QinConstants() {
        // 工具类，禁止实例化
    }
}
