package com.qin.plugins;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * qin-plugin-graalvm
 * GraalVM runtime support for Qin build tool
 *
 * Features:
 * - GraalVM environment detection
 * - Component management (nodejs, python, etc.)
 * - Version information
 * - Installation guidance
 */
public class GraalVMPlugin implements QinPlugin {
    private final GraalVMPluginOptions options;
    private GraalVMDetectionResult detectionResult;

    public GraalVMPlugin() {
        this(new GraalVMPluginOptions());
    }

    public GraalVMPlugin(GraalVMPluginOptions options) {
        this.options = options;
        this.detectionResult = detectGraalVM(options.getHome());
    }

    @Override
    public String getName() {
        return "qin-plugin-graalvm";
    }

    @Override
    public Map<String, Object> config(Map<String, Object> config) {
        Map<String, Object> newConfig = new HashMap<>(config);
        newConfig.put("_graalvm", detectionResult);
        return newConfig;
    }

    /**
     * 获取 GraalVM 信息
     */
    public GraalVMInfo getInfo() {
        return detectionResult.getInfo();
    }

    /**
     * 检查组件是否已安装
     */
    public boolean isComponentInstalled(String name) {
        GraalVMInfo info = detectionResult.getInfo();
        if (info == null) return false;
        return info.getComponents().contains(name);
    }

    /**
     * 获取 GraalVM Home 路径
     */
    public String getGraalVMHome() {
        GraalVMInfo info = detectionResult.getInfo();
        return info != null ? info.getHome() : null;
    }

    /**
     * 获取已安装组件列表
     */
    public List<String> getInstalledComponents() {
        GraalVMInfo info = detectionResult.getInfo();
        return info != null ? info.getComponents() : Collections.emptyList();
    }

    /**
     * 获取检测结果
     */
    public GraalVMDetectionResult getDetectionResult() {
        return detectionResult;
    }

    /**
     * 检测 GraalVM 安装
     */
    public static GraalVMDetectionResult detectGraalVM(String customHome) {
        String exe = isWindows() ? ".exe" : "";

        // 1. 使用自定义路径
        if (customHome != null && Files.exists(Paths.get(customHome))) {
            GraalVMDetectionResult result = detectFromHome(customHome);
            if (result.isFound()) {
                result.setDetectedBy("env");
                return result;
            }
        }

        // 2. 检查 GRAALVM_HOME 环境变量
        String graalvmHome = System.getenv("GRAALVM_HOME");
        if (graalvmHome != null && Files.exists(Paths.get(graalvmHome))) {
            GraalVMDetectionResult result = detectFromHome(graalvmHome);
            if (result.isFound()) {
                result.setDetectedBy("env");
                return result;
            }
        }

        // 3. 检查 JAVA_HOME 是否是 GraalVM
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null && Files.exists(Paths.get(javaHome))) {
            Path guPath = Paths.get(javaHome, "bin", "gu" + exe);
            if (Files.exists(guPath)) {
                GraalVMDetectionResult result = detectFromHome(javaHome);
                if (result.isFound()) {
                    result.setDetectedBy("env");
                    return result;
                }
            }
        }

        // 4. 尝试通过 gu 命令检测
        try {
            ProcessBuilder pb = new ProcessBuilder("gu", "--version");
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            String output = readStream(proc.getInputStream());
            int exitCode = proc.waitFor();

            if (exitCode == 0) {
                GraalVMDetectionResult result = new GraalVMDetectionResult();
                result.setFound(true);
                result.setDetectedBy("gu");
                
                GraalVMInfo info = new GraalVMInfo();
                info.setVersion(parseGuVersion(output));
                info.setComponents(getInstalledComponentsFromGu());
                result.setInfo(info);
                
                return result;
            }
        } catch (Exception e) {
            // gu 命令不可用
        }

        GraalVMDetectionResult result = new GraalVMDetectionResult();
        result.setFound(false);
        result.setError("GraalVM not found. Please set GRAALVM_HOME environment variable or install GraalVM.");
        return result;
    }

    private static GraalVMDetectionResult detectFromHome(String home) {
        String exe = isWindows() ? ".exe" : "";
        Path guPath = Paths.get(home, "bin", "gu" + exe);
        Path javaPath = Paths.get(home, "bin", "java" + exe);
        Path nodePath = Paths.get(home, "bin", "node" + exe);

        // 检查 gu 命令是否存在
        if (!Files.exists(guPath)) {
            GraalVMDetectionResult result = new GraalVMDetectionResult();
            result.setFound(false);
            result.setError("Not a valid GraalVM installation: " + home + " (gu command not found)");
            return result;
        }

        // 获取版本
        String version = getGraalVMVersion(guPath.toString());

        // 获取已安装组件
        List<String> components = getInstalledComponentsFromPath(guPath.toString());

        GraalVMInfo info = new GraalVMInfo();
        info.setHome(home);
        info.setVersion(version);
        info.setComponents(components);
        if (Files.exists(javaPath)) info.setJavaPath(javaPath.toString());
        if (Files.exists(nodePath)) info.setNodePath(nodePath.toString());
        info.setGuPath(guPath.toString());

        GraalVMDetectionResult result = new GraalVMDetectionResult();
        result.setFound(true);
        result.setInfo(info);
        return result;
    }

    private static String getGraalVMVersion(String guPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(guPath, "--version");
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            String output = readStream(proc.getInputStream());
            proc.waitFor();
            return parseGuVersion(output);
        } catch (Exception e) {
            return "unknown";
        }
    }

    private static String parseGuVersion(String output) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern
            .compile("(\\d+\\.\\d+(?:\\.\\d+)?)")
            .matcher(output);
        return matcher.find() ? matcher.group(1) : "unknown";
    }

    private static List<String> getInstalledComponentsFromGu() {
        try {
            ProcessBuilder pb = new ProcessBuilder("gu", "list");
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            String output = readStream(proc.getInputStream());
            proc.waitFor();
            return parseComponentList(output);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static List<String> getInstalledComponentsFromPath(String guPath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(guPath, "list");
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            String output = readStream(proc.getInputStream());
            proc.waitFor();
            return parseComponentList(output);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private static List<String> parseComponentList(String output) {
        List<String> components = new ArrayList<>();
        for (String line : output.split("\n")) {
            line = line.trim();
            if (line.isEmpty() || line.contains("ComponentId") || line.startsWith("-")) {
                continue;
            }
            String[] parts = line.split("\\s+");
            if (parts.length > 0 && !parts[0].isEmpty()) {
                components.add(parts[0]);
            }
        }
        return components;
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    /**
     * 创建 GraalVM 插件
     */
    public static GraalVMPlugin create() {
        return new GraalVMPlugin();
    }

    public static GraalVMPlugin create(GraalVMPluginOptions options) {
        return new GraalVMPlugin(options);
    }
}

/**
 * GraalVM 插件配置
 */
class GraalVMPluginOptions {
    private String home;
    private boolean autoInstall = false;

    public String getHome() { return home; }
    public void setHome(String home) { this.home = home; }

    public boolean isAutoInstall() { return autoInstall; }
    public void setAutoInstall(boolean autoInstall) { this.autoInstall = autoInstall; }
}

/**
 * GraalVM 信息
 */
class GraalVMInfo {
    private String home;
    private String version;
    private List<String> components = new ArrayList<>();
    private String nodePath;
    private String javaPath;
    private String guPath;

    public String getHome() { return home; }
    public void setHome(String home) { this.home = home; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public List<String> getComponents() { return components; }
    public void setComponents(List<String> components) { this.components = components; }

    public String getNodePath() { return nodePath; }
    public void setNodePath(String nodePath) { this.nodePath = nodePath; }

    public String getJavaPath() { return javaPath; }
    public void setJavaPath(String javaPath) { this.javaPath = javaPath; }

    public String getGuPath() { return guPath; }
    public void setGuPath(String guPath) { this.guPath = guPath; }
}

/**
 * GraalVM 检测结果
 */
class GraalVMDetectionResult {
    private boolean found;
    private String detectedBy;
    private GraalVMInfo info;
    private String error;

    public boolean isFound() { return found; }
    public void setFound(boolean found) { this.found = found; }

    public String getDetectedBy() { return detectedBy; }
    public void setDetectedBy(String detectedBy) { this.detectedBy = detectedBy; }

    public GraalVMInfo getInfo() { return info; }
    public void setInfo(GraalVMInfo info) { this.info = info; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

/**
 * GraalVM 未找到错误
 */
class GraalVMNotFoundError extends RuntimeException {
    public GraalVMNotFoundError() {
        super("GraalVM not found");
    }

    public GraalVMNotFoundError(String message) {
        super(message);
    }

    public String getInstallGuide() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        return String.format("""
            GraalVM 安装指南:
            
            1. 下载 GraalVM:
               https://www.graalvm.org/downloads/
            
            2. 设置环境变量:
               %s
               %s
            
            3. 验证安装:
               gu --version
            """,
            isWindows ? "set GRAALVM_HOME=C:\\path\\to\\graalvm" : "export GRAALVM_HOME=/path/to/graalvm",
            isWindows ? "set PATH=%GRAALVM_HOME%\\bin;%PATH%" : "export PATH=$GRAALVM_HOME/bin:$PATH"
        );
    }
}

/**
 * 组件未安装错误
 */
class ComponentNotInstalledError extends RuntimeException {
    private final String componentName;

    public ComponentNotInstalledError(String componentName) {
        super("GraalVM component '" + componentName + "' is not installed");
        this.componentName = componentName;
    }

    public String getComponentName() { return componentName; }

    public String getInstallCommand() {
        return "gu install " + componentName;
    }
}
