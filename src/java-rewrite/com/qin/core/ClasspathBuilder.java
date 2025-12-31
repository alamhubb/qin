package com.qin.core;

import com.qin.types.QinConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classpath构建器
 * 负责构建编译时和运行时的classpath
 */
public class ClasspathBuilder {
    private final String cwd;
    private final String outputDir;
    private final String externalClasspath;
    private final QinConfig config;

    public ClasspathBuilder(String cwd, String outputDir, String externalClasspath, QinConfig config) {
        this.cwd = cwd;
        this.outputDir = outputDir;
        this.externalClasspath = externalClasspath;
        this.config = config;
    }

    /**
     * 构建编译时classpath
     * 包含: 当前项目输出目录 + 本地项目依赖 + 远程依赖
     */
    public String buildCompileClasspath() {
        List<String> cpParts = new ArrayList<>();

        // Add current project's output directory (for incremental compilation)
        // 添加当前项目的输出目录，这样编译时可以找到已编译的类
        if (outputDir != null && !outputDir.isEmpty()) {
            Path outputPath = Paths.get(outputDir);
            if (Files.exists(outputPath)) {
                cpParts.add(outputDir);
            }
        }

        // Add local project dependencies using auto-discovery
        Map<String, String> deps = config.dependencies();
        if (deps != null && !deps.isEmpty()) {
            LocalProjectResolver localResolver = new LocalProjectResolver(cwd);
            LocalProjectResolver.ResolutionResult result = localResolver.resolveDependencies(deps);

            // 添加本地classpath
            if (result.localClasspath != null && !result.localClasspath.isEmpty()) {
                cpParts.add(result.localClasspath);
            }
        }

        // Add resolved remote dependencies (from Maven/Coursier)
        // 这个classpath参数由调用方传入,已经包含了远程依赖
        if (externalClasspath != null && !externalClasspath.isEmpty()) {
            cpParts.add(externalClasspath);
        }

        if (cpParts.isEmpty()) {
            return "";
        }

        String sep = DependencyResolver.getClasspathSeparator();
        return String.join(sep, cpParts);
    }

    /**
     * 构建运行时classpath
     * 包含: 当前项目输出目录 + 外部依赖classpath
     */
    public String buildRuntimeClasspath() {
        String sep = DependencyResolver.getClasspathSeparator();
        if (externalClasspath != null && !externalClasspath.isEmpty()) {
            return outputDir + sep + externalClasspath;
        }
        return outputDir;
    }

    /**
     * 获取classpath分隔符
     */
    public static String getClasspathSeparator() {
        return DependencyResolver.getClasspathSeparator();
    }
}
