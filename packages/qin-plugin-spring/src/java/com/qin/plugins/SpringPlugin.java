package com.qin.plugins;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * qin-plugin-spring
 * Spring Boot 支持插件
 * 
 * 功能：
 * - 生成 application.yml（如果不存在且有配置）
 * - TypeScript 配置与 Spring yml 格式一致
 * - 检测 DevTools 依赖，通知 Qin 禁用热重载
 */
public class SpringPlugin implements QinPlugin {
    private static final String SPRING_BOOT_PREFIX = "org.springframework.boot:spring-boot";
    
    private final SpringBootPluginOptions options;
    private final String cwd;
    private boolean isSpringBoot = false;
    private boolean devToolsEnabled = false;

    public SpringPlugin() {
        this(new SpringBootPluginOptions());
    }

    public SpringPlugin(SpringBootPluginOptions options) {
        this.options = options;
        this.cwd = System.getProperty("user.dir");
    }

    @Override
    public String getName() {
        return "qin-plugin-spring";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> config(Map<String, Object> config) {
        Map<String, String> deps = (Map<String, String>) config.get("dependencies");
        
        // 检测是否是 Spring Boot 项目
        isSpringBoot = isSpringBootProject(deps);
        
        if (!isSpringBoot) {
            System.out.println("[spring] 未检测到 Spring Boot 依赖，插件不生效");
            return config;
        }

        // 检测用户是否声明了 DevTools
        devToolsEnabled = hasDevTools(deps);

        // 获取端口配置
        int port = options.getServer() != null && options.getServer().getPort() != null
            ? options.getServer().getPort() : 8080;

        Map<String, Object> newConfig = new HashMap<>(config);
        newConfig.put("port", port);
        // 标记使用 DevTools，让 Qin 热重载插件知道应该禁用
        newConfig.put("_useDevTools", devToolsEnabled);

        return newConfig;
    }

    @Override
    public void configResolved(Map<String, Object> config) {
        if (!isSpringBoot) return;

        try {
            // 如果没有 application.yml，根据插件配置生成
            if (!hasApplicationConfig()) {
                boolean hasSpringConfig = options.getServer() != null 
                    || options.getSpring() != null 
                    || options.getLogging() != null;
                
                if (hasSpringConfig) {
                    System.out.println("[spring] 生成 application.yml");
                    
                    // 确保 resources 目录存在
                    Path resourcesDir = Paths.get(cwd, "src", "resources");
                    Files.createDirectories(resourcesDir);
                    
                    // 生成 yml 文件
                    String ymlContent = generateApplicationYml();
                    Files.writeString(resourcesDir.resolve("application.yml"), ymlContent);
                }
            }
        } catch (IOException e) {
            System.err.println("[spring] 生成配置文件失败: " + e.getMessage());
        }
    }

    private boolean isSpringBootProject(Map<String, String> dependencies) {
        if (dependencies == null) return false;
        return dependencies.keySet().stream()
            .anyMatch(dep -> dep.startsWith(SPRING_BOOT_PREFIX));
    }

    private boolean hasDevTools(Map<String, String> dependencies) {
        if (dependencies == null) return false;
        return dependencies.keySet().stream()
            .anyMatch(dep -> dep.contains("spring-boot-devtools"));
    }

    private boolean hasApplicationConfig() {
        String[] candidates = {
            "src/resources/application.yml",
            "src/resources/application.yaml",
            "src/resources/application.properties",
            "src/main/resources/application.yml",
            "src/main/resources/application.yaml",
            "src/main/resources/application.properties"
        };
        
        for (String candidate : candidates) {
            if (Files.exists(Paths.get(cwd, candidate))) {
                return true;
            }
        }
        return false;
    }

    private String generateApplicationYml() {
        StringBuilder yml = new StringBuilder();

        // Server 配置
        if (options.getServer() != null) {
            yml.append("server:\n");
            ServerConfig server = options.getServer();
            if (server.getPort() != null) {
                yml.append("  port: ").append(server.getPort()).append("\n");
            }
            if (server.getContextPath() != null) {
                yml.append("  servlet:\n");
                yml.append("    context-path: ").append(server.getContextPath()).append("\n");
            }
        }

        // Spring 配置
        if (options.getSpring() != null) {
            yml.append("spring:\n");
            SpringConfig spring = options.getSpring();
            
            // Datasource
            if (spring.getDatasource() != null) {
                yml.append("  datasource:\n");
                DatasourceConfig ds = spring.getDatasource();
                if (ds.getUrl() != null) {
                    yml.append("    url: ").append(ds.getUrl()).append("\n");
                }
                if (ds.getUsername() != null) {
                    yml.append("    username: ").append(ds.getUsername()).append("\n");
                }
                if (ds.getPassword() != null) {
                    yml.append("    password: ").append(ds.getPassword()).append("\n");
                }
                if (ds.getDriverClassName() != null) {
                    yml.append("    driver-class-name: ").append(ds.getDriverClassName()).append("\n");
                }
            }

            // JPA
            if (spring.getJpa() != null) {
                yml.append("  jpa:\n");
                JpaConfig jpa = spring.getJpa();
                if (jpa.getDdlAuto() != null) {
                    yml.append("    hibernate:\n");
                    yml.append("      ddl-auto: ").append(jpa.getDdlAuto()).append("\n");
                }
                if (jpa.isShowSql()) {
                    yml.append("    show-sql: true\n");
                }
            }
        }

        // Logging 配置
        if (options.getLogging() != null) {
            yml.append("logging:\n");
            LoggingConfig logging = options.getLogging();
            if (logging.getLevel() != null && !logging.getLevel().isEmpty()) {
                yml.append("  level:\n");
                for (Map.Entry<String, String> entry : logging.getLevel().entrySet()) {
                    yml.append("    ").append(entry.getKey()).append(": ")
                       .append(entry.getValue()).append("\n");
                }
            }
        }

        return yml.toString();
    }

    /**
     * 创建 Spring 插件
     */
    public static SpringPlugin create() {
        return new SpringPlugin();
    }

    public static SpringPlugin create(SpringBootPluginOptions options) {
        return new SpringPlugin(options);
    }
}

/**
 * Spring Boot 插件配置
 */
class SpringBootPluginOptions {
    private ServerConfig server;
    private SpringConfig spring;
    private LoggingConfig logging;

    public ServerConfig getServer() { return server; }
    public void setServer(ServerConfig server) { this.server = server; }

    public SpringConfig getSpring() { return spring; }
    public void setSpring(SpringConfig spring) { this.spring = spring; }

    public LoggingConfig getLogging() { return logging; }
    public void setLogging(LoggingConfig logging) { this.logging = logging; }
}

/**
 * 服务器配置
 */
class ServerConfig {
    private Integer port;
    private String contextPath;

    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public String getContextPath() { return contextPath; }
    public void setContextPath(String contextPath) { this.contextPath = contextPath; }
}

/**
 * Spring 配置
 */
class SpringConfig {
    private DatasourceConfig datasource;
    private JpaConfig jpa;

    public DatasourceConfig getDatasource() { return datasource; }
    public void setDatasource(DatasourceConfig datasource) { this.datasource = datasource; }

    public JpaConfig getJpa() { return jpa; }
    public void setJpa(JpaConfig jpa) { this.jpa = jpa; }
}

/**
 * 数据源配置
 */
class DatasourceConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDriverClassName() { return driverClassName; }
    public void setDriverClassName(String driverClassName) { this.driverClassName = driverClassName; }
}

/**
 * JPA 配置
 */
class JpaConfig {
    private String ddlAuto;
    private boolean showSql;

    public String getDdlAuto() { return ddlAuto; }
    public void setDdlAuto(String ddlAuto) { this.ddlAuto = ddlAuto; }

    public boolean isShowSql() { return showSql; }
    public void setShowSql(boolean showSql) { this.showSql = showSql; }
}

/**
 * 日志配置
 */
class LoggingConfig {
    private Map<String, String> level;

    public Map<String, String> getLevel() { return level; }
    public void setLevel(Map<String, String> level) { this.level = level; }
}
