package com.qin.types;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Qin Build Tool - Java 25 版本
 * 主配置记录（使用 Java 25 Record + Flexible Constructor Bodies）
 * 
 * @param name            项目名称
 * @param version         项目版本
 * @param description     项目描述
 * @param scope           依赖作用域（默认 COMPILE）
 * @param port            后端服务器端口（默认 8080）
 * @param localRep        是否使用项目本地 libs（默认 false）
 * @param client          前端配置
 * @param plugins         插件列表
 * @param entry           Java 入口文件路径
 * @param dependencies    运行时依赖
 * @param devDependencies 开发依赖
 * @param packages        Monorepo 多项目配置
 * @param output          输出配置
 * @param java            Java 特定配置
 * @param graalvm         GraalVM 配置
 * @param frontend        前端配置
 * @param scripts         自定义脚本
 * @param repositories    Maven 仓库列表
 */
public record QinConfig(
        String name,
        String version,
        String description,
        DependencyScope scope,
        int port,
        boolean localRep,
        ClientConfig client,
        List<QinPlugin> plugins,
        String entry,
        Map<String, String> dependencies,
        Map<String, String> devDependencies,
        List<String> packages,
        OutputConfig output,
        JavaConfig java,
        GraalVMConfig graalvm,
        FrontendConfig frontend,
        Map<String, String> scripts,
        List<Repository> repositories) {

    /**
     * Compact Constructor with Flexible Constructor Bodies (JEP 513)
     * 允许在调用父构造器前进行验证和初始化
     */
    public QinConfig {
        // ✨ Java 25 新特性：Flexible Constructor Bodies
        // 可以在 super() 调用前进行验证和参数处理

        // 验证必需字段
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be null or blank");
        }

        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Project version cannot be null or blank");
        }

        // 提供默认值
        scope = scope != null ? scope : DependencyScope.COMPILE;
        port = port > 0 ? port : 8080;

        // 确保集合不可变
        dependencies = dependencies != null ? Map.copyOf(dependencies) : Map.of();
        devDependencies = devDependencies != null ? Map.copyOf(devDependencies) : Map.of();
        packages = packages != null ? List.copyOf(packages) : List.of();
        plugins = plugins != null ? List.copyOf(plugins) : List.of();
        scripts = scripts != null ? Map.copyOf(scripts) : Map.of();
        repositories = repositories != null ? List.copyOf(repositories) : List.of();
    }

    /**
     * 简化构造器 - 只需必需参数
     */
    public QinConfig(String name, String version) {
        this(
                name,
                version,
                null, // description
                DependencyScope.COMPILE, // scope (默认值)
                8080, // port (默认值)
                false, // localRep
                null, // client
                null, // plugins
                null, // entry
                null, // dependencies
                null, // devDependencies
                null, // packages
                null, // output
                null, // java
                null, // graalvm
                null, // frontend
                null, // scripts
                null // repositories
        );
    }

    /**
     * 判断是否包含指定依赖
     */
    public boolean hasDependency(String artifact) {
        return dependencies.containsKey(artifact) ||
                devDependencies.containsKey(artifact);
    }

    /**
     * 获取依赖版本
     */
    public String getDependencyVersion(String artifact) {
        String version = dependencies.get(artifact);
        return version != null ? version : devDependencies.get(artifact);
    }

    /**
     * 是否为 Monorepo 项目
     */
    public boolean isMonorepo() {
        return packages != null && !packages.isEmpty();
    }

    /**
     * 获取有效的仓库列表（如果为空则使用默认仓库）
     */
    public List<Repository> effectiveRepositories() {
        if (repositories == null || repositories.isEmpty()) {
            return List.of(
                    new Repository("aliyun", "https://maven.aliyun.com/repository/public"),
                    new Repository("central", "https://repo1.maven.org/maven2"));
        }
        return repositories;
    }

    @Override
    public String toString() {
        return String.format("QinConfig[name=%s, version=%s, dependencies=%d, devDependencies=%d]",
                name, version, dependencies.size(), devDependencies.size());
    }
}
