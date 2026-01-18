package com.qin.core;

import com.qin.constants.QinConstants;

import java.io.IOException;
import java.nio.file.*;

/**
 * 缓存验证工具类
 * 用于检查 .qin/classpath.json 缓存是否有效
 * 
 * 被以下模块复用：
 * - qin-cli (QinCli.java)
 * - qin-idea-plugin (QinProjectSync.java)
 */
public final class CacheValidator {

    private CacheValidator() {} // 工具类，禁止实例化

    /**
     * 检查项目缓存是否有效
     * 
     * 缓存有效条件：
     * 1. .qin/classpath.json 存在
     * 2. classpath.json 的修改时间晚于 qin.config.json
     * 3. classpath.json 格式有效（非空且包含 classpath 字段）
     * 4. classpath 中的所有文件都存在
     * 
     * @param projectPath 项目根目录路径
     * @return true=缓存有效，false=缓存无效需要重新同步
     */
    public static boolean isCacheValid(String projectPath) {
        try {
            Path configFile = Paths.get(projectPath, QinConstants.CONFIG_FILE);
            Path cacheFile = QinPaths.getClasspathCache(projectPath);

            // 条件1: 缓存文件和配置文件都存在
            if (!Files.exists(cacheFile) || !Files.exists(configFile)) {
                return false;
            }

            // 条件2: 缓存时间 > 配置时间
            if (Files.getLastModifiedTime(cacheFile).compareTo(
                    Files.getLastModifiedTime(configFile)) <= 0) {
                return false;
            }

            // 条件3: 缓存文件格式有效
            String cacheContent = Files.readString(cacheFile);
            if (cacheContent.trim().isEmpty() || !cacheContent.contains("classpath")) {
                return false;
            }

            // 条件4: 解析并验证所有文件存在
            String classpath = parseClasspathFromJson(cacheContent);
            if (classpath.isEmpty()) {
                return false;
            }

            return validateClasspathFiles(classpath);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查项目缓存是否有效（Path 版本）
     */
    public static boolean isCacheValid(Path projectPath) {
        return isCacheValid(projectPath.toString());
    }

    /**
     * 验证 classpath 中的所有文件是否存在
     * 
     * @param classpath 用分隔符连接的路径字符串
     * @return true=所有文件都存在，false=有文件缺失
     */
    public static boolean validateClasspathFiles(String classpath) {
        if (classpath == null || classpath.isEmpty()) {
            return true;
        }
        
        String sep = QinConstants.getClasspathSeparator();
        String[] paths = classpath.split(sep);
        
        for (String path : paths) {
            if (path.isEmpty()) {
                continue;
            }
            if (!Files.exists(Paths.get(path))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从 JSON 缓存文件中解析 classpath
     * 
     * @param json classpath.json 文件内容
     * @return classpath 字符串，解析失败返回空字符串
     */
    public static String parseClasspathFromJson(String json) {
        try {
            // 简单解析 JSON 中的 classpath 字段
            // 格式: {"classpath": "path1;path2;path3", ...}
            int start = json.indexOf("\"classpath\"");
            if (start < 0) {
                return "";
            }
            
            int colonIndex = json.indexOf(":", start);
            if (colonIndex < 0) {
                return "";
            }
            
            int valueStart = json.indexOf("\"", colonIndex);
            if (valueStart < 0) {
                return "";
            }
            
            int valueEnd = json.indexOf("\"", valueStart + 1);
            if (valueEnd < 0) {
                return "";
            }
            
            return json.substring(valueStart + 1, valueEnd);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取缓存的 classpath（如果有效）
     * 
     * @param projectPath 项目根目录路径
     * @return 有效的 classpath 字符串，无效返回 null
     */
    public static String getCachedClasspath(String projectPath) {
        try {
            Path cacheFile = QinPaths.getClasspathCache(projectPath);
            if (!Files.exists(cacheFile)) {
                return null;
            }

            String cacheContent = Files.readString(cacheFile);
            String classpath = parseClasspathFromJson(cacheContent);
            
            if (!classpath.isEmpty() && validateClasspathFiles(classpath)) {
                return classpath;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
