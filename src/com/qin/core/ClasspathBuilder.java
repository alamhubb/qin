package com.qin.core;

import com.qin.types.QinConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Classpath构建器
 * 负责构建编译时和运行时的classpath
 */
public class ClasspathBuilder {
    private final String cwd;
    private final String outputDir;
    private final String externalClasspath;
    private final QinConfig config;

    public ClasspathBuilder(String cwd, QinConfig config) {
        this(
                cwd,
                config != null && config.java() != null && config.java().outputDir() != null
                        ? Paths.get(cwd, config.java().outputDir()).toString()
                        : Paths.get(cwd, "build", "classes").toString(),
                "",
                config);
    }

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
            cpParts.add(outputDir);
        }

        // Vendored jars in project lib/ are available before dependency sync
        // has populated .qin/classpath.json.
        cpParts.addAll(resolveVendoredJarClasspath());

        // Add local project dependencies using auto-discovery
        Map<String, String> deps = config.dependencies();
        System.err.println("[DEBUG] Config dependencies: " + (deps != null ? deps.keySet() : "null"));
        if (deps != null && !deps.isEmpty()) {
            LocalProjectResolver localResolver = new LocalProjectResolver(cwd);
            LocalProjectResolver.ResolutionResult result = localResolver.resolveDependencies(deps);

            System.err.println("[DEBUG] Local classpath resolved: " +
                    (result.localClasspath != null
                            ? result.localClasspath.substring(0, Math.min(200, result.localClasspath.length()))
                            : "empty"));

            // 添加本地classpath
            if (result.localClasspath != null && !result.localClasspath.isEmpty()) {
                cpParts.add(result.localClasspath);
            }
        }

        // Add resolved remote dependencies (from Maven/Coursier)
        // 这个classpath参数由调用方传入,已经包含了远程依赖
        String remoteClasspath = externalClasspath;
        if (remoteClasspath == null || remoteClasspath.isEmpty()) {
            remoteClasspath = CacheValidator.getCachedClasspath(cwd);
        }
        if (remoteClasspath != null && !remoteClasspath.isEmpty()) {
            cpParts.add(remoteClasspath);
        }

        if (cpParts.isEmpty()) {
            return "";
        }

        String sep = DependencyResolver.getClasspathSeparator();
        return String.join(sep, cpParts);
    }

    private List<String> resolveVendoredJarClasspath() {
        List<String> jars = new ArrayList<>();
        Path libDir = Paths.get(cwd, "lib");
        if (!Files.isDirectory(libDir)) {
            return jars;
        }

        try (Stream<Path> walk = Files.walk(libDir, 1)) {
            walk
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".jar"))
                    .sorted()
                    .map(path -> path.toAbsolutePath().normalize().toString())
                    .forEach(jars::add);
        } catch (Exception e) {
            System.err.println("Warning: Failed to scan project lib jars: " + e.getMessage());
        }
        return jars;
    }

    /**
     * 构建运行时classpath
     * 包含: 当前项目输出目录 + 外部依赖classpath
     */
    public String buildRuntimeClasspath() {
        // Runtime must keep the same local-project closure as compilation,
        // otherwise transitive workspace classes like qin-parser disappear
        // when running Java/Spring host shells.
        return buildCompileClasspath();
    }

    /**
     * 获取classpath分隔符
     */
    public static String getClasspathSeparator() {
        return DependencyResolver.getClasspathSeparator();
    }
}
