package com.qin.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Qin 工具类 - 通用文件和系统操作工具方法
 */
public class QinUtils {

    /**
     * 递归删除目录及其所有内容
     *
     * @param dir 要删除的目录
     * @throws IOException 如果删除失败
     */
    public static void deleteDir(Path dir) throws IOException {
        if (dir == null || !Files.exists(dir)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            // 忽略删除失败（可能是权限问题或文件被占用）
                        }
                    });
        }
    }

    /**
     * 递归删除目录及其所有内容（字符串路径版本）
     *
     * @param dirPath 要删除的目录路径
     * @throws IOException 如果删除失败
     */
    public static void deleteDir(String dirPath) throws IOException {
        if (dirPath == null || dirPath.isEmpty()) {
            return;
        }
        deleteDir(Paths.get(dirPath));
    }

    /**
     * 确保目录存在，如果不存在则创建
     *
     * @param dir 目录路径
     * @throws IOException 如果创建失败
     */
    public static void ensureDir(Path dir) throws IOException {
        if (dir != null && !Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }

    /**
     * 确保目录存在，如果不存在则创建（字符串路径版本）
     *
     * @param dirPath 目录路径
     * @throws IOException 如果创建失败
     */
    public static void ensureDir(String dirPath) throws IOException {
        if (dirPath != null && !dirPath.isEmpty()) {
            ensureDir(Paths.get(dirPath));
        }
    }

    /**
     * 安全读取文件内容，如果文件不存在返回 null
     *
     * @param path 文件路径
     * @return 文件内容，如果文件不存在返回 null
     */
    public static String readFileOrNull(Path path) {
        if (path == null || !Files.exists(path)) {
            return null;
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 安全读取文件内容，如果文件不存在返回默认值
     *
     * @param path         文件路径
     * @param defaultValue 默认值
     * @return 文件内容或默认值
     */
    public static String readFileOrDefault(Path path, String defaultValue) {
        String content = readFileOrNull(path);
        return content != null ? content : defaultValue;
    }

    private QinUtils() {
        // 工具类，禁止实例化
    }
}
