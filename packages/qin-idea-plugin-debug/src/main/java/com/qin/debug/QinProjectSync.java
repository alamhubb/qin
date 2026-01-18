package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.LanguageLevelProjectExtension;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.pom.java.LanguageLevel;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static com.qin.constants.QinConstants.*;

/**
 * Qin 项目同步工具类
 * 统一处理所有同步逻辑，被以下场景调用：
 * - 插件启动时（静默模式）
 * - qin.config.json 变化时
 * - 手动点击刷新时（非静默模式）
 */
public class QinProjectSync {

    private final Project project;
    private final String basePath;

    /**
     * 静默模式：不显示通知弹窗，只写日志
     * 默认为 true（静默模式）
     */
    private boolean silentMode = true;

    public QinProjectSync(@NotNull Project project) {
        this.project = project;
        this.basePath = project.getBasePath();
    }

    /**
     * 设置通知模式
     * @param silent true=静默模式（不弹通知），false=正常模式（显示通知）
     * @return this，支持链式调用
     */
    public QinProjectSync setSilentMode(boolean silent) {
        this.silentMode = silent;
        return this;
    }

    /**
     * 检查缓存是否有效
     * 使用通用的 CacheValidator 工具类
     */
    public boolean isCacheValid(Path projectPath) {
        boolean valid = com.qin.core.CacheValidator.isCacheValid(projectPath);
        if (valid) {
            QinLogger.info("[Sync] 缓存有效，跳过同步: " + projectPath.getFileName());
        }
        return valid;
    }

    /**
     * 同步单个项目
     * 统一入口，调用所有需要更新的子任务
     */
    public void syncProject(Path projectPath) {
        QinLogger.info("[Sync] ========== 开始同步项目: " + projectPath + " ==========");
        String projectName = projectPath.getFileName().toString();

        try {
            // 1. 读取配置文件
            Path configPath = projectPath.resolve(CONFIG_FILE);
            if (!Files.exists(configPath)) {
                QinLogger.info("[Sync] 配置文件不存在，跳过");
                return;
            }

            QinConfig config = QinConfig.load(projectPath.toString());
            if (config == null) {
                QinLogger.error("[Sync] 无法解析配置文件");
                if (!silentMode) {
                    QinLogger.notifyError("Qin Sync Failed", "无法解析 " + projectName + " 的配置文件");
                }
                return;
            }

            // 2. 执行所有同步任务
            syncDependencies(projectPath);
            updateImlFile(projectPath, config);
            updateLanguageLevel(config);
            updateMiscXml(config);

            // 3. 刷新 IDEA 项目结构
            refreshProject();

            QinLogger.info("[Sync] ========== 项目同步完成 ==========");
            if (!silentMode) {
                QinLogger.notifySuccess("Qin Sync Complete", projectName + " 同步成功");
            }

        } catch (Exception e) {
            QinLogger.error("[Sync] 同步失败: " + e.getMessage());
            if (!silentMode) {
                QinLogger.notifyError("Qin Sync Failed", projectName + " 同步失败: " + e.getMessage());
            }
        }
    }

    /**
     * 同步所有检测到的 Qin 项目
     * 默认会检查缓存，缓存有效则跳过同步
     */
    public void syncAllProjects() {
        syncAllProjects(true);
    }

    /**
     * 同步所有检测到的 Qin 项目
     * @param checkCache true=检查缓存有效性，有效则跳过；false=强制同步
     */
    public void syncAllProjects(boolean checkCache) {
        if (basePath == null)
            return;

        try {
            List<Path> qinProjects = DebugStartup.discoverQinProjects(Paths.get(basePath));

            if (qinProjects.isEmpty()) {
                QinLogger.info("[Sync] 未检测到 Qin 项目");
                return;
            }

            QinLogger.info("[Sync] 检测到 " + qinProjects.size() + " 个 Qin 项目");

            for (Path projectPath : qinProjects) {
                // 检查缓存是否有效
                if (checkCache && isCacheValid(projectPath)) {
                    continue; // 缓存有效，跳过同步
                }
                syncProject(projectPath);
            }

        } catch (Exception e) {
            QinLogger.error("[Sync] 同步所有项目失败: " + e.getMessage());
        }
    }

    /**
     * 仅刷新语言级别（场景 2: .iml 变化后使用）
     * 用于 CLI qin sync 执行后，.iml 文件已更新，只需刷新 IDEA 内存中的设置
     * 不重新执行 sync，避免循环和重复工作
     */
    public void refreshLanguageLevel(QinConfig config) {
        QinLogger.info("[Sync] → 仅刷新语言级别...");
        updateLanguageLevel(config); // T3: 更新 IDEA 内存
        refreshProject(); // T5: 刷新项目结构
        QinLogger.info("[Sync] ✓ 语言级别刷新完成");
    }

    /**
     * 任务1: 同步依赖（调用 qin sync）
     */
    public void syncDependencies(Path projectPath) {
        try {
            QinLogger.info("[Sync] → 同步依赖...");
            ProcessBuilder pb = new ProcessBuilder(CMD_PREFIX, CMD_FLAG, QIN_CMD, "sync");
            pb.directory(projectPath.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    QinLogger.info("[qin sync] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                QinLogger.info("[Sync] ✓ 依赖同步完成");
            } else {
                QinLogger.error("[Sync] ✗ 依赖同步失败，退出码: " + exitCode);
            }
        } catch (Exception e) {
            QinLogger.error("[Sync] 同步依赖异常: " + e.getMessage());
        }
    }

    /**
     * 任务2: 更新 .iml 文件
     */
    public void updateImlFile(Path projectPath, QinConfig config) {
        try {
            QinLogger.info("[Sync] → 更新 .iml 文件...");

            Path ideaDir = Paths.get(basePath, ".idea");
            DebugStartup.generateImlFile(projectPath, true, ideaDir);

            // 更新模块级别的 LANGUAGE_LEVEL
            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");
            if (Files.exists(imlPath)) {
                updateImlLanguageLevel(imlPath, config);
            }

            QinLogger.info("[Sync] ✓ .iml 文件更新完成");
        } catch (Exception e) {
            QinLogger.error("[Sync] 更新 .iml 失败: " + e.getMessage());
        }
    }

    /**
     * 更新 .iml 文件中的 LANGUAGE_LEVEL 属性
     */
    private void updateImlLanguageLevel(Path imlPath, QinConfig config) {
        try {
            String content = Files.readString(imlPath);
            String targetVersion = config.getJavaVersion();
            String languageLevel = "JDK_" + targetVersion;

            // 检查是否已有 LANGUAGE_LEVEL 属性
            if (content.contains("LANGUAGE_LEVEL=")) {
                // 更新现有属性
                content = content.replaceAll(
                        "LANGUAGE_LEVEL=\"[^\"]*\"",
                        "LANGUAGE_LEVEL=\"" + languageLevel + "\"");
            } else if (content.contains("<component name=\"NewModuleRootManager\"")) {
                // 添加 LANGUAGE_LEVEL 属性
                content = content.replace(
                        "<component name=\"NewModuleRootManager\"",
                        "<component name=\"NewModuleRootManager\" LANGUAGE_LEVEL=\"" + languageLevel + "\"");
            }

            Files.writeString(imlPath, content);
            QinLogger.info("[Sync] ✓ .iml LANGUAGE_LEVEL 已设置为 " + languageLevel);
        } catch (Exception e) {
            QinLogger.error("[Sync] 更新 .iml LANGUAGE_LEVEL 失败: " + e.getMessage());
        }
    }

    /**
     * 任务3: 更新 IDEA 语言级别
     */
    public void updateLanguageLevel(QinConfig config) {
        try {
            QinLogger.info("[Sync] → 更新语言级别...");

            String sourceVersion = config.getSourceVersion();
            LanguageLevel level = parseLanguageLevel(sourceVersion);

            if (level == null) {
                QinLogger.info("[Sync] 无法解析语言级别: " + sourceVersion);
                return;
            }

            final LanguageLevel finalLevel = level;
            ApplicationManager.getApplication().invokeLater(() -> {
                ApplicationManager.getApplication().runWriteAction(() -> {
                    LanguageLevelProjectExtension extension = LanguageLevelProjectExtension.getInstance(project);
                    if (extension != null) {
                        extension.setLanguageLevel(finalLevel);
                        QinLogger.info("[Sync] ✓ 语言级别已设置为 " + finalLevel.name());
                    }
                });
            });
        } catch (Exception e) {
            QinLogger.error("[Sync] 更新语言级别失败: " + e.getMessage());
        }
    }

    /**
     * 任务4: 更新 .idea/misc.xml 中的 languageLevel
     */
    public void updateMiscXml(QinConfig config) {
        try {
            QinLogger.info("[Sync] → 更新 misc.xml...");

            String targetVersion = config.getJavaVersion();

            Path miscXml = Paths.get(basePath, ".idea", "misc.xml");
            if (!Files.exists(miscXml)) {
                QinLogger.info("[Sync] misc.xml 不存在，跳过");
                return;
            }

            String content = Files.readString(miscXml);

            // 更新 languageLevel 属性
            String languageLevelAttr = "languageLevel=\"JDK_" + targetVersion + "\"";
            if (content.contains("languageLevel=")) {
                content = content.replaceAll("languageLevel=\"[^\"]*\"", languageLevelAttr);
            } else if (content.contains("<component name=\"ProjectRootManager\"")) {
                content = content.replace(
                        "<component name=\"ProjectRootManager\"",
                        "<component name=\"ProjectRootManager\" " + languageLevelAttr);
            }

            Files.writeString(miscXml, content);
            QinLogger.info("[Sync] ✓ misc.xml 已更新，languageLevel=JDK_" + targetVersion);
        } catch (Exception e) {
            QinLogger.error("[Sync] 更新 misc.xml 失败: " + e.getMessage());
        }
    }

    /**
     * 刷新 IDEA 项目结构
     */
    public void refreshProject() {
        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                QinLogger.info("[Sync] ✓ 项目结构已刷新");
            } catch (Exception e) {
                QinLogger.error("[Sync] 刷新项目失败: " + e.getMessage());
            }
        });
    }

    /**
     * 解析 Java 版本到 LanguageLevel
     */
    private LanguageLevel parseLanguageLevel(String version) {
        try {
            int ver = Integer.parseInt(version);
            if (ver <= 8) {
                return LanguageLevel.JDK_1_8;
            }
            String levelName = "JDK_" + ver;
            for (LanguageLevel level : LanguageLevel.values()) {
                if (level.name().equals(levelName)) {
                    return level;
                }
            }
            return LanguageLevel.HIGHEST;
        } catch (Exception e) {
            return LanguageLevel.JDK_21;
        }
    }
}
