package com.qin.core;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

/**
 * 增量编译检测器
 * 检测本地项目是否需要重新编译
 */
public class IncrementalCompilationChecker {

    /**
     * 检查项目是否需要重新编译
     * 
     * @param projectDir 项目目录
     * @return true 如果源文件比 class 文件新，或没有 class 文件
     */
    public boolean needsRecompilation(Path projectDir) {
        try {
            Path srcDir = projectDir.resolve("src");
            Path classesDir = projectDir.resolve(".qin/classes");

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
            // 出错时保守起见，认为需要编译
            return true;
        }
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
