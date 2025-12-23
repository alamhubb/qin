package com.qin.types;

import java.util.List;
import java.util.Map;

/**
 * Qin - Java-Vite Build Tool
 * Main configuration interface for qin.config.ts
 */
public class QinConfig {
    /** Project name */
    private String name;
    
    /** Project version */
    private String version;
    
    /** Project description */
    private String description;

    /** 当前包被其他项目引用时的默认 scope */
    private DependencyScope scope = DependencyScope.COMPILE;

    /** 后端服务器端口，默认 8080 */
    private int port = 8080;

    /** 使用项目本地 repository（./repository） */
    private boolean localRep = false;

    /** 前端配置 */
    private ClientConfig client;
    
    /** 插件列表 */
    private List<QinPlugin> plugins;
    
    /** Java 入口文件路径 */
    private String entry;
    
    /** 依赖配置 */
    private Map<String, String> dependencies;

    /** 开发依赖 */
    private Map<String, String> devDependencies;
    
    /** Monorepo 多项目配置 */
    private List<String> packages;
    
    /** Output configuration */
    private OutputConfig output;
    
    /** Java-specific configuration */
    private JavaConfig java;

    /** GraalVM 配置 */
    private GraalVMConfig graalvm;
    
    /** Frontend configuration */
    private FrontendConfig frontend;
    
    /** Custom scripts */
    private Map<String, String> scripts;
    
    /** Maven 仓库配置 */
    private List<Repository> repositories;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public DependencyScope getScope() { return scope; }
    public void setScope(DependencyScope scope) { this.scope = scope; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public boolean isLocalRep() { return localRep; }
    public void setLocalRep(boolean localRep) { this.localRep = localRep; }

    public ClientConfig getClient() { return client; }
    public void setClient(ClientConfig client) { this.client = client; }

    public List<QinPlugin> getPlugins() { return plugins; }
    public void setPlugins(List<QinPlugin> plugins) { this.plugins = plugins; }

    public String getEntry() { return entry; }
    public void setEntry(String entry) { this.entry = entry; }

    public Map<String, String> getDependencies() { return dependencies; }
    public void setDependencies(Map<String, String> dependencies) { this.dependencies = dependencies; }

    public Map<String, String> getDevDependencies() { return devDependencies; }
    public void setDevDependencies(Map<String, String> devDependencies) { this.devDependencies = devDependencies; }

    public List<String> getPackages() { return packages; }
    public void setPackages(List<String> packages) { this.packages = packages; }

    public OutputConfig getOutput() { return output; }
    public void setOutput(OutputConfig output) { this.output = output; }

    public JavaConfig getJava() { return java; }
    public void setJava(JavaConfig java) { this.java = java; }

    public GraalVMConfig getGraalvm() { return graalvm; }
    public void setGraalvm(GraalVMConfig graalvm) { this.graalvm = graalvm; }

    public FrontendConfig getFrontend() { return frontend; }
    public void setFrontend(FrontendConfig frontend) { this.frontend = frontend; }

    public Map<String, String> getScripts() { return scripts; }
    public void setScripts(Map<String, String> scripts) { this.scripts = scripts; }

    public List<Repository> getRepositories() { return repositories; }
    public void setRepositories(List<Repository> repositories) { this.repositories = repositories; }
}
