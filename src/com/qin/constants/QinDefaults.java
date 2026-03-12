package com.qin.constants;

import com.qin.types.DependencyScope;
import com.qin.types.Repository;
import java.util.List;

/**
 * @deprecated 常量统一迁移到 QinConstants。这里只保留兼容别名。
 */
@Deprecated
public class QinDefaults {

    // === 项目配置 ===
    public static final String DEFAULT_VERSION = QinConstants.DEFAULT_VERSION;
    public static final DependencyScope DEFAULT_SCOPE = DependencyScope.COMPILE;
    public static final int DEFAULT_PORT = QinConstants.DEFAULT_PORT;
    public static final boolean DEFAULT_LOCAL_REP = QinConstants.DEFAULT_LOCAL_REP;

    // === Java 配置 ===
    public static final String DEFAULT_JAVA_VERSION = QinConstants.DEFAULT_JAVA_VERSION;
    public static final String DEFAULT_SOURCE_DIR = QinConstants.DEFAULT_SOURCE_DIR;
    public static final String DEFAULT_ENCODING = QinConstants.CHARSET_UTF8;

    // === 输出配置 ===
    public static final String DEFAULT_BUILD_DIR = QinConstants.BUILD_DIR;
    public static final String DEFAULT_JAR_NAME = QinConstants.DEFAULT_JAR_NAME;

    // === Maven 仓库 ===
    public static final List<Repository> DEFAULT_REPOSITORIES = List.of(
            new Repository("aliyun", "https://maven.aliyun.com/repository/public"),
            new Repository("central", "https://repo1.maven.org/maven2"));

    // === 文件名 ===
    public static final String CONFIG_FILE_NAME = QinConstants.CONFIG_FILE;

    // === 目录名 ===
    public static final String QIN_DIR = QinConstants.QIN_DIR;
    public static final String CLASSES_DIR = "classes";
    public static final String LIBS_DIR = QinConstants.LIBS_DIR_NAME;
    public static final String CACHE_DIR = QinConstants.CACHE_DIR_NAME;

    private QinDefaults() {
        // 工具类，禁止实例化
    }
}
