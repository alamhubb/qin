package com.qin.constants;

import com.qin.types.JavaConfig;
import com.qin.types.OutputConfig;

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
     * 工作区/项目根目录标志（用于识别和向上查找根目录）
     * 优先级: .idea > .vscode > qin.config.json > package.json > .git
     */
    public static final java.util.List<String> WORKSPACE_ROOT_MARKERS = java.util.List.of(
            ".idea", // IDEA 项目
            ".vscode", // VS Code 项目
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
     * Node.js package.json 文件名
     */
    public static final String PACKAGE_JSON = "package.json";

    /**
     * Node.js node_modules 目录名
     */
    public static final String NODE_MODULES = "node_modules";

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

    // ==================== CLI 参数常量 ====================

    /**
     * --all: 同步所有子项目
     */
    public static final String ARG_ALL = "--all";

    /**
     * --force: 强制执行（忽略缓存）
     */
    public static final String ARG_FORCE = "--force";

    /**
     * --compile: 同步后自动编译
     */
    public static final String ARG_COMPILE = "--compile";

    /**
     * --no-sync: 跳过依赖同步
     */
    public static final String ARG_NO_SYNC = "--no-sync";

    /**
     * -o / --output: 指定输出目录
     */
    public static final String ARG_OUTPUT = "--output";
    public static final String ARG_OUTPUT_SHORT = "-o";

    // ==================== 默认值常量 ====================

    /**
     * 默认源代码目录
     */
    public static final String DEFAULT_SOURCE_DIR = "src";
    public static final String MAIN_SOURCE_DIR = "main";
    public static final String SHARED_DIR = "shared";
    public static final String APP_DIR = "app";

    /**
     * Java/Maven 标准源代码目录（优先级高于 DEFAULT_SOURCE_DIR）
     */
    public static final String JAVA_SOURCE_DIR = "src/main/java";

    /**
     * 默认测试目录
     */
    public static final String DEFAULT_TEST_DIR = "src/test/java";

    /**
     * Java 入口文件候选列表（按优先级排序）
     * 用于自动检测项目入口
     */
    public static final String DEFAULT_ENTRY = "src/Main.java";
    public static final java.util.List<String> DEFAULT_ENTRY_CANDIDATES = java.util.List.of(
            DEFAULT_ENTRY,
            "src/App.java",
            "src/Application.java",
            "main/Main.java",
            "main/main.java",
            "Main.java",
            "main.java",
            "src/server/Main.java",
            "src/main/java/Main.java");

    /**
     * 默认 JAR 名称
     */
    public static final String DEFAULT_JAR_NAME = "app.jar";

    /**
     * 默认分发目录
     */
    public static final String DEFAULT_DIST_DIR = "dist";

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
    public static String getSourceDir(JavaConfig javaConfig) {
        if (javaConfig != null && javaConfig.sourceDir() != null && !javaConfig.sourceDir().isEmpty()) {
            return javaConfig.sourceDir();
        }
        return DEFAULT_SOURCE_DIR;
    }

    /**
     * 获取测试目录（安全获取，带默认值）
     */
    public static String getTestDir(JavaConfig javaConfig) {
        if (javaConfig != null && javaConfig.testDir() != null && !javaConfig.testDir().isEmpty()) {
            return javaConfig.testDir();
        }
        return DEFAULT_TEST_DIR;
    }

    /**
     * 获取输出目录（安全获取，带默认值）
     */
    public static String getOutputDir(JavaConfig javaConfig) {
        if (javaConfig != null && javaConfig.outputDir() != null && !javaConfig.outputDir().isEmpty()) {
            return javaConfig.outputDir();
        }
        return BUILD_CLASSES_DIR; // 使用已有常量
    }

    /**
     * 获取 JAR 名称（安全获取，带默认值）
     */
    public static String getJarName(OutputConfig outputConfig) {
        if (outputConfig != null && outputConfig.jarName() != null && !outputConfig.jarName().isEmpty()) {
            return outputConfig.jarName();
        }
        return DEFAULT_JAR_NAME;
    }

    /**
     * 获取分发目录（安全获取，带默认值）
     *
     * @param outputConfig 输出配置对象（可能为 null）
     * @return 分发目录路径
     */
    public static String getDistDir(OutputConfig outputConfig) {
        if (outputConfig != null && outputConfig.dir() != null && !outputConfig.dir().isEmpty()) {
            return outputConfig.dir();
        }
        return DEFAULT_DIST_DIR;
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

    /**
     * 判断是否为 macOS 系统
     */
    public static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    // ==================== 路径工具方法 ====================

    /**
     * 获取当前工作目录
     */
    public static String getCwd() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取当前工作目录（Path 对象）
     */
    public static java.nio.file.Path getCwdPath() {
        return java.nio.file.Paths.get(getCwd());
    }

    /**
     * 获取用户主目录
     */
    public static String getHomeDir() {
        return System.getProperty("user.home");
    }

    /**
     * 获取用户主目录（Path 对象）
     */
    public static java.nio.file.Path getHomeDirPath() {
        return java.nio.file.Paths.get(getHomeDir());
    }

    /**
     * 获取 Qin 全局目录 (~/.qin)
     */
    public static java.nio.file.Path getQinHomeDir() {
        return getHomeDirPath().resolve(QIN_DIR);
    }

    /**
     * 获取项目级 Qin 目录 ({projectDir}/.qin)
     */
    public static java.nio.file.Path getProjectQinDir(String projectDir) {
        return java.nio.file.Paths.get(projectDir, QIN_DIR);
    }

    /**
     * 获取项目级 Qin 目录 ({projectDir}/.qin)
     */
    public static java.nio.file.Path getProjectQinDir(java.nio.file.Path projectDir) {
        return projectDir.resolve(QIN_DIR);
    }

    private QinConstants() {
        // 工具类，禁止实例化
    }
}
