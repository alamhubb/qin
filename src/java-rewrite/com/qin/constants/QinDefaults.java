package com.qin.constants;

import com.qin.types.DependencyScope;
import com.qin.types.Repository;
import java.util.List;

/**
 * Qin 默认配置常量
 */
public class QinDefaults {

    // === 项目配置 ===
    public static final String DEFAULT_VERSION = "1.0.0";
    public static final DependencyScope DEFAULT_SCOPE = DependencyScope.COMPILE;
    public static final int DEFAULT_PORT = 8080;
    public static final boolean DEFAULT_LOCAL_REP = false;

    // === Java 配置 ===
    public static final String DEFAULT_JAVA_VERSION = "21";
    public static final String DEFAULT_SOURCE_DIR = "src";
    public static final String DEFAULT_OUTPUT_DIR = "build/classes";
    public static final String DEFAULT_ENCODING = "UTF-8";

    // === 输出配置 ===
    public static final String DEFAULT_BUILD_DIR = "build";
    public static final String DEFAULT_JAR_NAME = "app.jar";

    // === Maven 仓库 ===
    public static final List<Repository> DEFAULT_REPOSITORIES = List.of(
            new Repository("aliyun", "https://maven.aliyun.com/repository/public"),
            new Repository("central", "https://repo1.maven.org/maven2"));

    // === 文件名 ===
    public static final String CONFIG_FILE_NAME = "qin.config.json";

    // === 目录名 ===
    public static final String QIN_DIR = ".qin";
    public static final String CLASSES_DIR = "classes";
    public static final String LIBS_DIR = "libs"; // 项目根目录，存放依赖符号链接（应加入 .gitignore）
    public static final String CACHE_DIR = "cache";

    private QinDefaults() {
        // 工具类，禁止实例化
    }
}
