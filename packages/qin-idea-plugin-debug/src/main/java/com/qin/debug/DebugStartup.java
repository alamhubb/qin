package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * 项目启动监听器
 * 自动检测 Qin 项目并执行 sync
 * 支持 Monorepo：自动扫描所有子项目
 */
public class DebugStartup implements ProjectActivity {

    // 使用常量类
    private static final Set<String> EXCLUDED_DIRS = QinConstants.EXCLUDED_DIRS;
    private static final String CONFIG_FILE = QinConstants.CONFIG_FILE;

    @Nullable
    @Override
    public Object execute(@NotNull Project project,
            @NotNull Continuation<? super Unit> cont) {

        String basePath = project.getBasePath();
        if (basePath == null)
            return Unit.INSTANCE;

        // 记录日志
        QinLogger logger = new QinLogger(project.getName());
        logger.info("项目打开: " + project.getName());
        logger.info("路径: " + basePath);

        // 在后台线程扫描和执行 sync
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                // 发现所有 Qin 项目（包括当前目录和子目录）
                List<Path> qinProjects = discoverQinProjects(Paths.get(basePath));

                if (qinProjects.isEmpty()) {
                    logger.info("未检测到 Qin 项目");
                    return;
                }

                logger.info("检测到 " + qinProjects.size() + " 个 Qin 项目");

                // 为每个项目执行 sync
                for (Path projectPath : qinProjects) {
                    String relativePath = Paths.get(basePath).relativize(projectPath).toString();
                    if (relativePath.isEmpty()) {
                        relativePath = QinConstants.CURRENT_DIR;
                    }
                    logger.info("同步项目: " + relativePath);

                    try {
                        runQinSync(projectPath.toString(), logger);
                    } catch (Exception e) {
                        logger.error("同步失败 [" + relativePath + "]: " + e.getMessage());
                    }
                }

                logger.info("所有项目同步完成");

                // 刷新项目，让 IDEA 重新加载库配置
                ApplicationManager.getApplication().invokeLater(() -> {
                    try {
                        com.intellij.openapi.vfs.VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                        logger.info("项目模型已刷新");
                    } catch (Exception e) {
                        logger.error("刷新项目失败: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                logger.error("自动同步失败: " + e.getMessage());
            }
        });

        // 自动打开 Qin 工具窗口（如果有任何 Qin 项目）
        ApplicationManager.getApplication().invokeLater(() -> {
            // 检查是否有 qin.config.json（在根目录或子目录）
            try {
                boolean hasQinProject = Files.exists(Paths.get(basePath, CONFIG_FILE)) ||
                        hasQinProjectInSubdirs(Paths.get(basePath));

                if (hasQinProject) {
                    ToolWindowManager manager = ToolWindowManager.getInstance(project);
                    ToolWindow toolWindow = manager.getToolWindow("Qin");
                    if (toolWindow != null) {
                        toolWindow.show();
                    }
                }
            } catch (Exception e) {
                // 忽略
            }
        });

        return Unit.INSTANCE;
    }

    /**
     * 发现所有 Qin 项目
     * 策略：
     * 1. 从 IDEA 打开的目录开始向上查找 workspace root（包含 .idea/.vscode 的最顶层目录）
     * 2. 从 workspace root 向下递归扫描所有 qin.config.json
     */
    private List<Path> discoverQinProjects(Path ideaProjectDir) {
        List<Path> projects = new ArrayList<>();

        // 1. 向上查找 workspace root
        Path workspaceRoot = findWorkspaceRoot(ideaProjectDir);

        // 2. 从 workspace root 向下扫描所有 qin.config.json
        if (Files.exists(workspaceRoot.resolve(CONFIG_FILE))) {
            projects.add(workspaceRoot);
        }
        scanForQinProjects(workspaceRoot, projects, 0, QinConstants.MAX_SCAN_DEPTH);

        return projects;
    }

    /**
     * 向上查找 workspace root
     * 标志：.idea, .vscode, .git（取最顶层的）
     */
    private Path findWorkspaceRoot(Path startDir) {
        Path current = startDir.toAbsolutePath().normalize();
        Path topMost = startDir; // 默认使用起始目录

        while (current != null && current.getParent() != null) {
            // 检查是否有项目标志
            final Path finalCurrent = current; // lambda 需要 final
            boolean isProjectRoot = QinConstants.PROJECT_ROOT_MARKERS.stream()
                    .anyMatch(marker -> Files.exists(finalCurrent.resolve(marker))) ||
                    Files.exists(current.resolve(CONFIG_FILE));

            if (isProjectRoot) {
                topMost = current; // 继续向上，取最顶层的
            }

            current = current.getParent();
        }

        return topMost;
    }

    /**
     * 递归扫描目录查找 qin.config.json
     */
    private void scanForQinProjects(Path dir, List<Path> projects, int depth, int maxDepth) {
        if (depth >= maxDepth) {
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();

                // 跳过排除的目录
                if (EXCLUDED_DIRS.contains(dirName) || dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    continue;
                }

                // 检查是否有 qin.config.json
                Path configPath = subDir.resolve(CONFIG_FILE);
                if (Files.exists(configPath) && !projects.contains(subDir)) {
                    projects.add(subDir);
                }

                // 继续递归
                scanForQinProjects(subDir, projects, depth + 1, maxDepth);
            }
        } catch (IOException e) {
            // 忽略目录遍历错误
        }
    }

    /**
     * 检查子目录中是否有 Qin 项目（快速检查，只看一层）
     */
    private boolean hasQinProjectInSubdirs(Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();
                if (!EXCLUDED_DIRS.contains(dirName) && !dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    if (Files.exists(subDir.resolve(CONFIG_FILE))) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // 忽略
        }
        return false;
    }

    /**
     * 执行 qin sync 命令
     */
    private void runQinSync(String projectPath, QinLogger logger) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(QinConstants.CMD_PREFIX, QinConstants.CMD_FLAG, QinConstants.QIN_CMD,
                "sync");
        pb.directory(new File(projectPath));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // 读取输出
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("[sync] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            logger.error("qin sync 退出码: " + exitCode);
        }
    }
}
