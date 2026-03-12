package com.qin.core;

import com.qin.constants.QinConstants;

import java.nio.file.Path;

/**
 * Qin 路径工具。
 * 路径字符串常量统一定义在 QinConstants，这里只保留路径计算方法。
 */
public final class QinPaths {

    private QinPaths() {
    } // 工具类，禁止实例化

    /**
     * 获取绝对输出目录路径
     */
    public static Path getOutputDir(String projectRoot) {
        return java.nio.file.Paths.get(projectRoot, QinConstants.BUILD_CLASSES_DIR);
    }

    /**
     * 获取classpath缓存文件绝对路径
     */
    public static Path getClasspathCache(String projectRoot) {
        return QinConstants.getProjectClasspathCache(projectRoot);
    }

    /**
     * 获取本地libs目录绝对路径
     */
    public static Path getLocalLibsDir(String projectRoot) {
        return QinConstants.getProjectLibsDir(projectRoot);
    }

    /**
     * 获取全局libs目录绝对路径
     * 
     * @return ~/.qin/libs
     */
    public static Path getGlobalLibsDir() {
        return QinConstants.getGlobalLibsDir();
    }

    /**
     * 获取Qin配置目录绝对路径
     */
    public static Path getQinDir(String projectRoot) {
        return QinConstants.getProjectQinDir(projectRoot);
    }
}
