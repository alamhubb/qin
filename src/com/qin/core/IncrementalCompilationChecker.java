package com.qin.core;

import com.qin.constants.QinConstants;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * 增量编译检测器
 * 使用文件哈希缓存实现精确的增量编译检测
 *
 * 两种检测模式：
 * 1. 快速模式（时间戳）- 用于本地依赖项目检测
 * 2. 精确模式（哈希）- 用于当前项目编译
 */
public class IncrementalCompilationChecker {

    private final FileHashCache hashCache;

    public IncrementalCompilationChecker() {
        this.hashCache = null; // 快速模式，不使用哈希
    }

    public IncrementalCompilationChecker(String projectDir) {
        this.hashCache = new FileHashCache(projectDir);
        this.hashCache.load();
    }

    /**
     * 检查项目是否需要重新编译（快速模式 - 时间戳）
     * 用于检测本地依赖项目
     */
    public boolean needsRecompilation(Path projectDir) {
        try {
            Path srcDir = projectDir.resolve("src");
            Path classesDir = projectDir.resolve(QinConstants.BUILD_CLASSES_DIR);

            // 如果没有 classes 目录，肯定需要编译
            if (!Files.exists(classesDir)) {
                return true;
            }

            // 获取最新的源文件修改时间
            long latestSrcTime = getLatestModificationTime(srcDir, ".java");

            // 获取最旧的 class 文件修改时间
            long oldestClassTime = getOldestModificationTime(classesDir, ".class");

            // 如果没有 class 文件，需要编译
            if (oldestClassTime == 0) {
                return true;
            }

            // 如果源文件比 class 文件新，需要重新编译
            return latestSrcTime > oldestClassTime;

        } catch (IOException e) {
            return true;
        }
    }

    /**
     * 获取需要重新编译的文件（精确模式 - 哈希）
     * 返回哈希值变化的文件列表
     */
    public List<Path> getChangedFiles(String sourceDir) throws IOException {
        if (hashCache == null) {
            throw new IllegalStateException("Hash cache not initialized");
        }
        return hashCache.getChangedFiles(sourceDir);
    }

    /**
     * 检查是否需要编译（精确模式 - 哈希）
     */
    public boolean needsCompilationByHash(String sourceDir) throws IOException {
        if (hashCache == null) {
            return true;
        }
        return hashCache.needsCompilation(sourceDir);
    }

    /**
     * 更新文件哈希（编译成功后调用）
     */
    public void updateHashes(List<Path> files) {
        if (hashCache != null) {
            hashCache.updateHashes(files);
        }
    }

    /**
     * 更新整个源目录的哈希
     */
    public void updateAllHashes(String sourceDir) throws IOException {
        if (hashCache != null) {
            hashCache.updateAllHashes(sourceDir);
        }
    }

    /**
     * 保存哈希缓存
     */
    public void saveCache() throws IOException {
        if (hashCache != null) {
            hashCache.save();
        }
    }

    /**
     * 清除缓存
     */
    public void clearCache() throws IOException {
        if (hashCache != null) {
            hashCache.clear();
        }
    }

    /**
     * 获取缓存统计信息
     */
    public FileHashCache.CacheStats getCacheStats() {
        if (hashCache != null) {
            return hashCache.getStats();
        }
        return null;
    }

    /**
     * 获取目录下指定扩展名文件的最新修改时间
     */
    private long getLatestModificationTime(Path dir, String extension) throws IOException {
        if (!Files.exists(dir)) {
            return 0;
        }

        final long[] latestTime = { 0 };

        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(extension)) {
                    long mtime = attrs.lastModifiedTime().toMillis();
                    if (mtime > latestTime[0]) {
                        latestTime[0] = mtime;
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return latestTime[0];
    }

    /**
     * 获取目录下指定扩展名文件的最旧修改时间
     */
    private long getOldestModificationTime(Path dir, String extension) throws IOException {
        if (!Files.exists(dir)) {
            return 0;
        }

        final long[] oldestTime = { Long.MAX_VALUE };
        final boolean[] found = { false };

        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(extension)) {
                    found[0] = true;
                    long mtime = attrs.lastModifiedTime().toMillis();
                    if (mtime < oldestTime[0]) {
                        oldestTime[0] = mtime;
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return found[0] ? oldestTime[0] : 0;
    }

    /**
     * 获取所有需要重新编译的依赖项目
     * 
     * @param graph         依赖图
     * @param localProjects 本地项目信息
     * @return 需要重新编译的项目名称列表（按依赖顺序）
     */
    public List<String> getProjectsNeedingRecompilation(
            DependencyGraphBuilder.DependencyGraph graph,
            Map<String, LocalProjectResolver.ProjectInfo> localProjects) {

        List<String> needsRecompile = new ArrayList<>();

        // 检查每个项目
        for (DependencyGraphBuilder.DependencyNode node : graph.getAllNodes()) {
            LocalProjectResolver.ProjectInfo projectInfo = localProjects.get(node.projectName);
            if (projectInfo != null && needsRecompilation(projectInfo.projectDir)) {
                needsRecompile.add(node.projectName);
            }
        }

        return needsRecompile;
    }
}
