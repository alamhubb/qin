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

// 别名：使用 qin-cli 的通用常量
import static com.qin.constants.QinConstants.*;

/**
 * 项目启动监听器
 * 自动检测 Qin 项目并执行 sync
 * 支持 Monorepo：自动扫描所有子项目
 */
public class DebugStartup implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project,
            @NotNull Continuation<? super Unit> cont) {

        String basePath = project.getBasePath();
        if (basePath == null)
            return Unit.INSTANCE;

        // 初始化静态日志器
        QinLogger.init(basePath);
        QinLogger.info("[STARTUP] Qin 插件启动 - " + project.getName());
        QinLogger.info("[STARTUP] 项目路径: " + basePath);

        // 立即配置 Project SDK（在 EDT 线程中）
        ApplicationManager.getApplication().invokeLater(() -> {
            configureProjectSdk(project);
        });

        // 在后台线程扫描和执行 sync
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                // 发现所有 Qin 项目（包括当前目录和子目录）
                List<Path> qinProjects = discoverQinProjects(Paths.get(basePath));

                if (qinProjects.isEmpty()) {
                    QinLogger.info("未检测到 Qin 项目");
                    return;
                }

                QinLogger.info("检测到 " + qinProjects.size() + " 个 Qin 项目:");
                for (Path p : qinProjects) {
                    QinLogger.info("  - " + p.toString());
                }

                // 1. 先为所有项目生成 .iml（快速，让 IDEA 立即识别源代码）
                Path ideaDir = Paths.get(basePath, ".idea");
                for (Path projectPath : qinProjects) {
                    generateImlFile(projectPath, logger, false, ideaDir); // 启动时：已存在就跳过，但会注册模块
                }

                // 2. 再为每个项目执行 sync（可能较慢）
                for (Path projectPath : qinProjects) {
                    String relativePath = Paths.get(basePath).relativize(projectPath).toString();
                    if (relativePath.isEmpty()) {
                        relativePath = CURRENT_DIR;
                    }
                    QinLogger.info("同步项目: " + relativePath);

                    try {
                        runQinSync(projectPath.toString(), logger);
                        // sync 完成后，重新生成 .iml（此时会读取 classpath.json 添加依赖）
                        generateImlFile(projectPath, logger, true, ideaDir); // 强制更新以添加依赖
                    } catch (Exception e) {
                        QinLogger.error("同步失败 [" + relativePath + "]: " + e.getMessage());
                    }
                }

                QinLogger.info("所有项目同步完成");

                // 刷新项目，让 IDEA 重新加载库配置
                ApplicationManager.getApplication().invokeLater(() -> {
                    try {
                        com.intellij.openapi.vfs.VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
                        QinLogger.info("项目模型已刷新");

                        // 自动配置 Project SDK
                        configureProjectSdk(project, logger);
                    } catch (Exception e) {
                        QinLogger.error("刷新项目失败: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                QinLogger.error("自动同步失败: " + e.getMessage());
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
     * 使用 qin-cli 的 LocalProjectResolver
     */
    private List<Path> discoverQinProjects(Path ideaProjectDir) {
        return com.qin.core.LocalProjectResolver.scanAllProjects(ideaProjectDir.toString());
    }

    /**
     * 检查子目录中是否有 Qin 项目（快速检查，只看一层）
     */
    private boolean hasQinProjectInSubdirs(Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();
                if (!EXCLUDED_DIRS.contains(dirName) && !dirName.startsWith(HIDDEN_PREFIX)) {
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
        ProcessBuilder pb = new ProcessBuilder(CMD_PREFIX, CMD_FLAG, QIN_CMD,
                "sync");
        pb.directory(new File(projectPath));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // 读取输出
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                QinLogger.info("[sync] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            QinLogger.error("qin sync 退出码: " + exitCode);
        }
    }

    /**
     * 自动配置 Project SDK
     * 检测系统 JDK 并设置为项目 SDK
     */
    private static void configureProjectSdk(Project project, QinLogger logger) {
        try {
            // 获取当前 Project SDK
            com.intellij.openapi.projectRoots.ProjectJdkTable jdkTable = com.intellij.openapi.projectRoots.ProjectJdkTable
                    .getInstance();
            com.intellij.openapi.projectRoots.Sdk[] allJdks = jdkTable.getAllJdks();

            // 获取项目的 SDK 配置
            com.intellij.openapi.roots.ProjectRootManager rootManager = com.intellij.openapi.roots.ProjectRootManager
                    .getInstance(project);
            com.intellij.openapi.projectRoots.Sdk currentSdk = rootManager.getProjectSdk();

            if (currentSdk != null) {
                QinLogger.info("[SDK] 已配置 Project SDK: " + currentSdk.getName());
                return;
            }

            // 没有 SDK，尝试查找合适的 JDK
            QinLogger.info("[SDK] 未配置 Project SDK，尝试自动配置...");

            // 优先查找已注册的 JDK
            com.intellij.openapi.projectRoots.Sdk bestSdk = null;
            int bestVersion = 0;

            for (com.intellij.openapi.projectRoots.Sdk sdk : allJdks) {
                if (sdk.getSdkType() instanceof com.intellij.openapi.projectRoots.JavaSdk) {
                    String versionStr = com.intellij.openapi.projectRoots.JavaSdk.getInstance()
                            .getVersionString(sdk);
                    if (versionStr != null) {
                        // 简单解析版本号
                        int version = parseJavaVersion(versionStr);
                        if (version > bestVersion) {
                            bestVersion = version;
                            bestSdk = sdk;
                        }
                    }
                }
            }

            if (bestSdk != null) {
                final com.intellij.openapi.projectRoots.Sdk sdkToSet = bestSdk;
                // 设置 Project SDK
                ApplicationManager.getApplication().runWriteAction(() -> {
                    rootManager.setProjectSdk(sdkToSet);
                });
                QinLogger.info("[SDK] 已自动配置 Project SDK: " + bestSdk.getName());
            } else {
                // 没有找到已注册的 JDK，尝试从 JAVA_HOME 自动添加
                String javaHome = System.getenv("JAVA_HOME");
                if (javaHome != null && !javaHome.isEmpty() && Files.exists(Paths.get(javaHome))) {
                    QinLogger.info("[SDK] 正在从 JAVA_HOME 添加 JDK: " + javaHome);

                    // 创建新的 JDK
                    com.intellij.openapi.projectRoots.JavaSdk javaSdkType = com.intellij.openapi.projectRoots.JavaSdk
                            .getInstance();

                    // 生成 SDK 名称
                    String sdkName = "JDK-" + System.getProperty("java.version", "auto");

                    // 创建 SDK
                    com.intellij.openapi.projectRoots.Sdk newSdk = javaSdkType.createJdk(sdkName, javaHome, false);

                    if (newSdk != null) {
                        // 添加到 JDK 表
                        ApplicationManager.getApplication().runWriteAction(() -> {
                            jdkTable.addJdk(newSdk);
                            rootManager.setProjectSdk(newSdk);
                        });
                        QinLogger.info("[SDK] 已自动添加并配置 JDK: " + sdkName);
                    } else {
                        QinLogger.error("[SDK] 无法创建 JDK，请手动配置");
                    }
                } else {
                    QinLogger.info("[SDK] 未找到 JAVA_HOME，请手动配置 Project SDK");
                }
            }
        } catch (Exception e) {
            QinLogger.error("[SDK] 配置失败: " + e.getMessage());
        }
    }

    /**
     * 解析 Java 版本号
     */
    private static int parseJavaVersion(String versionStr) {
        try {
            // 匹配版本号，如 "21", "17", "1.8"
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)");
            java.util.regex.Matcher matcher = pattern.matcher(versionStr);
            if (matcher.find()) {
                int version = Integer.parseInt(matcher.group(1));
                // 1.8 -> 8
                if (version == 1 && matcher.find()) {
                    version = Integer.parseInt(matcher.group(1));
                }
                return version;
            }
        } catch (Exception e) {
            // 忽略
        }
        return 0;
    }

    /**
     * 为 Qin 项目生成 .iml 文件
     * 让 IDEA 识别源代码目录
     * 
     * @param forceOverwrite true=强制覆盖（手动 sync），false=已存在跳过（自动启动）
     */
    public static void generateImlFile(Path projectPath, QinLogger logger, boolean forceOverwrite) {
        generateImlFile(projectPath, logger, forceOverwrite, null);
    }

    /**
     * 为 Qin 项目生成 .iml 文件
     * 让 IDEA 识别源代码目录
     * 
     * @param forceOverwrite true=强制覆盖（手动 sync），false=已存在跳过（自动启动）
     * @param ideaDir        IDEA 项目的 .idea 目录路径（用于注册模块）
     */
    public static void generateImlFile(Path projectPath, QinLogger logger, boolean forceOverwrite, Path ideaDir) {
        try {
            // 获取项目名称
            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");

            QinLogger.info("[iml] 处理项目: " + projectPath);
            QinLogger.info("[iml]   iml 路径: " + imlPath);
            QinLogger.info("[iml]   forceOverwrite: " + forceOverwrite);

            // 如果已存在且不强制覆盖，跳过生成但仍注册
            boolean needGenerate = !Files.exists(imlPath) || forceOverwrite;

            if (!needGenerate) {
                QinLogger.info("[iml]   .iml 已存在，跳过生成");
            } else {
                // 使用 BSP 处理器获取项目信息
                com.qin.bsp.BspHandler bspHandler = new com.qin.bsp.BspHandler(projectPath.toString());

                // 获取源代码目录（优先从 qin.config.json）
                String sourceDir = bspHandler.getSourceDir();
                // 检查目录是否实际存在
                if (!Files.exists(projectPath.resolve(sourceDir))) {
                    // 回退到自动检测
                    sourceDir = detectSourceDir(projectPath);
                }
                QinLogger.info("[iml]   sourceDir: " + sourceDir);

                if (sourceDir == null) {
                    QinLogger.info("[iml]   未找到源代码目录");
                    return;
                }

                // 获取输出目录
                String outputDir = bspHandler.getOutputDir();
                QinLogger.info("[iml]   outputDir: " + outputDir);

                // 生成排除目录 XML
                StringBuilder excludeFolders = new StringBuilder();
                for (String excludeDir : com.qin.debug.QinConstants.IML_EXCLUDED_DIRS) {
                    excludeFolders.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                            .append(excludeDir)
                            .append("\" />\n");
                }

                // 通过 BSP 获取依赖（classpath）
                List<String> classpath = bspHandler.getClasspath();
                StringBuilder dependencyEntries = new StringBuilder();

                for (String path : classpath) {
                    String entryPath = path.replace("\\", "/");

                    if (entryPath.endsWith(".jar")) {
                        // JAR 文件依赖
                        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                                .append("      <library>\n")
                                .append("        <CLASSES>\n")
                                .append("          <root url=\"jar://").append(entryPath).append("!/\" />\n")
                                .append("        </CLASSES>\n")
                                .append("      </library>\n")
                                .append("    </orderEntry>\n");
                        QinLogger.info("[iml]   添加 JAR 依赖: " + entryPath);
                    } else {
                        // 本地类目录 - 计算对应的源码目录
                        String sourcePath = computeSourcePath(entryPath);

                        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                                .append("      <library>\n")
                                .append("        <CLASSES>\n")
                                .append("          <root url=\"file://").append(entryPath).append("\" />\n")
                                .append("        </CLASSES>\n");

                        // 如果找到源码目录，添加 SOURCES 配置
                        if (sourcePath != null) {
                            dependencyEntries.append("        <SOURCES>\n")
                                    .append("          <root url=\"file://").append(sourcePath).append("\" />\n")
                                    .append("        </SOURCES>\n");
                            QinLogger.info("[iml]   添加本地类路径: " + entryPath + " (源码: " + sourcePath + ")");
                        } else {
                            QinLogger.info("[iml]   添加本地类路径: " + entryPath + " (无源码)");
                        }

                        dependencyEntries.append("      </library>\n")
                                .append("    </orderEntry>\n");
                    }
                }

                // 生成 .iml 内容
                String imlContent = """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <module type="JAVA_MODULE" version="4">
                          <component name="NewModuleRootManager" inherit-compiler-output="false">
                            <exclude-output />
                            <output url="file://$MODULE_DIR$/%s" />
                            <output-test url="file://$MODULE_DIR$/%s" />
                            <content url="file://$MODULE_DIR$">
                              <sourceFolder url="file://$MODULE_DIR$/%s" isTestSource="false" />
                        %s    </content>
                            <orderEntry type="inheritedJdk" />
                            <orderEntry type="sourceFolder" forTests="false" />
                        %s  </component>
                        </module>
                        """.formatted(outputDir, outputDir.replace("classes", "test-classes"),
                        sourceDir, excludeFolders.toString(), dependencyEntries.toString());

                Files.writeString(imlPath, imlContent);
                QinLogger.info("生成 .iml 文件: " + projectName + ".iml（通过 BSP）");
            }

            // 注册模块到 modules.xml
            if (ideaDir != null) {
                registerModuleToIdeaProject(imlPath, ideaDir, logger);
            }

        } catch (Exception e) {
            QinLogger.error("生成 .iml 失败: " + e.getMessage());
        }
    }

    /**
     * 注册模块到 IDEA 的 modules.xml
     */
    private static void registerModuleToIdeaProject(Path imlPath, Path ideaDir, QinLogger logger) {
        try {
            Path modulesXml = ideaDir.resolve("modules.xml");

            // 计算相对路径
            Path ideaParent = ideaDir.getParent(); // 项目根目录
            Path relativePath = ideaParent.relativize(imlPath);
            String moduleEntry = relativePath.toString().replace("\\", "/");

            String content;
            if (!Files.exists(modulesXml)) {
                // modules.xml 不存在，创建新的
                QinLogger.info("[iml]   modules.xml 不存在，创建新文件");
                content = """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project version="4">
                          <component name="ProjectModuleManager">
                            <modules>
                            </modules>
                          </component>
                        </project>
                        """;
            } else {
                content = Files.readString(modulesXml);
            }

            // 检查是否已经注册
            if (content.contains(moduleEntry)) {
                QinLogger.info("[iml]   模块已在 modules.xml 中注册");
                return;
            }

            // 构建新的 module 条目
            String newModule = String.format(
                    "      <module fileurl=\"file://$PROJECT_DIR$/%s\" filepath=\"$PROJECT_DIR$/%s\" />",
                    moduleEntry, moduleEntry);

            // 在 </modules> 之前插入
            String newContent = content.replace("    </modules>", newModule + "\n    </modules>");

            Files.writeString(modulesXml, newContent);
            QinLogger.info("[iml]   已注册模块到 modules.xml: " + moduleEntry);

        } catch (Exception e) {
            QinLogger.error("[iml]   注册模块失败: " + e.getMessage());
        }
    }

    /**
     * 检测源代码目录
     */
    private static String detectSourceDir(Path projectPath) {
        // 优先检测标准 Maven 结构
        Path mavenSrc = projectPath.resolve("src/main/java");
        if (Files.exists(mavenSrc)) {
            return "src/main/java";
        }
        // 其次检测简单结构
        Path simpleSrc = projectPath.resolve("src");
        if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            return "src";
        }
        // 没找到源代码目录
        return null;
    }

    /**
     * 根据类输出目录计算源码目录
     * 例如: D:/project/subhuti-java/build/classes ->
     * D:/project/subhuti-java/src/main/java
     */
    private static String computeSourcePath(String classPath) {
        try {
            // 将 build/classes 替换为源码目录
            Path classDir = Paths.get(classPath);

            // 向上找到项目根目录（包含 build 的父目录）
            Path current = classDir;
            while (current != null && !current.getFileName().toString().equals("build")) {
                current = current.getParent();
            }

            if (current != null && current.getParent() != null) {
                Path projectRoot = current.getParent();

                // 检查 src/main/java
                Path mavenSrc = projectRoot.resolve("src/main/java");
                if (Files.exists(mavenSrc)) {
                    return mavenSrc.toString().replace("\\", "/");
                }

                // 检查 src
                Path simpleSrc = projectRoot.resolve("src");
                if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
                    return simpleSrc.toString().replace("\\", "/");
                }
            }
        } catch (Exception e) {
            // 忽略
        }
        return null;
    }
}
