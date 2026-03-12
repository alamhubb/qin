package com.qin.npm;

import com.google.gson.*;
import com.qin.constants.QinConstants;
import com.qin.utils.QinUtils;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

/**
 * Qin NPM Package Manager
 * 从 npm 镜像获取包，不依赖 npm/node
 * 
 * 包存储位置: ~/.qin/npm-cache/ 或项目内 node_modules/
 */
public class NpmPackageManager {
    // npm 镜像源
    private static final String[] NPM_REGISTRIES = {
            "https://registry.npmmirror.com", // 淘宝镜像（国内快）
            "https://registry.npmjs.org" // 官方源
    };

    private final String projectRoot;
    private final String cacheDir;
    private final String nodeModulesDir;
    private final Gson gson;
    private String activeRegistry;

    public NpmPackageManager() {
        this(QinConstants.getCwd());
    }

    public NpmPackageManager(String projectRoot) {
        this.projectRoot = projectRoot;
        this.cacheDir = QinConstants.getGlobalNpmCacheDir().toString();
        this.nodeModulesDir = Paths.get(projectRoot, QinConstants.NODE_MODULES).toString();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.activeRegistry = NPM_REGISTRIES[0];
    }

    /**
     * 安装包
     */
    public boolean install(String packageName, String version) {
        try {
            System.out.println("📦 Installing " + packageName + "@" + version + "...");

            // 1. 获取包信息
            JsonObject pkgInfo = fetchPackageInfo(packageName, version);
            if (pkgInfo == null) {
                System.err.println("✗ Package not found: " + packageName);
                return false;
            }

            String resolvedVersion = pkgInfo.get("version").getAsString();
            String tarballUrl = pkgInfo.getAsJsonObject("dist").get("tarball").getAsString();

            System.out.println("  → Resolved version: " + resolvedVersion);

            // 2. 下载并解压
            Path targetDir = Paths.get(nodeModulesDir, packageName);
            if (Files.exists(targetDir)) {
                // 检查版本
                Path pkgJsonPath = targetDir.resolve(QinConstants.PACKAGE_JSON);
                if (Files.exists(pkgJsonPath)) {
                    JsonObject existing = JsonParser.parseString(Files.readString(pkgJsonPath)).getAsJsonObject();
                    if (existing.has("version") && existing.get("version").getAsString().equals(resolvedVersion)) {
                        System.out.println("  ✓ Already installed");
                        return true;
                    }
                }
                QinUtils.deleteDir(targetDir);
            }

            downloadAndExtract(tarballUrl, targetDir);
            System.out.println("  ✓ Installed " + packageName + "@" + resolvedVersion);

            // 3. 安装依赖
            JsonObject deps = pkgInfo.has("dependencies")
                    ? pkgInfo.getAsJsonObject("dependencies")
                    : null;
            if (deps != null && deps.size() > 0) {
                System.out.println("  → Installing dependencies...");
                for (String depName : deps.keySet()) {
                    String depVersion = deps.get(depName).getAsString();
                    install(depName, depVersion);
                }
            }

            return true;
        } catch (Exception e) {
            System.err.println("✗ Failed to install " + packageName + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * 从 package.json 安装所有依赖
     */
    public boolean installAll() {
        try {
            Path pkgJsonPath = Paths.get(projectRoot, QinConstants.PACKAGE_JSON);
            if (!Files.exists(pkgJsonPath)) {
                System.err.println("✗ package.json not found");
                return false;
            }

            JsonObject pkgJson = JsonParser.parseString(Files.readString(pkgJsonPath)).getAsJsonObject();

            int count = 0;

            // 安装 dependencies
            if (pkgJson.has("dependencies")) {
                JsonObject deps = pkgJson.getAsJsonObject("dependencies");
                for (String name : deps.keySet()) {
                    if (install(name, deps.get(name).getAsString())) {
                        count++;
                    }
                }
            }

            // 安装 devDependencies
            if (pkgJson.has("devDependencies")) {
                JsonObject deps = pkgJson.getAsJsonObject("devDependencies");
                for (String name : deps.keySet()) {
                    if (install(name, deps.get(name).getAsString())) {
                        count++;
                    }
                }
            }

            System.out.println("\n✓ Installed " + count + " packages");
            return true;
        } catch (Exception e) {
            System.err.println("✗ Install failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取包信息
     */
    private JsonObject fetchPackageInfo(String packageName, String version) throws Exception {
        // 处理版本范围
        String resolvedVersion = version;
        if (version.startsWith("^") || version.startsWith("~") || version.equals("latest") || version.equals("*")) {
            // 获取所有版本，选择匹配的最新版
            JsonObject allVersions = fetchJson(activeRegistry + "/" + encodePackageName(packageName));
            if (allVersions == null || !allVersions.has("versions")) {
                return null;
            }

            JsonObject versions = allVersions.getAsJsonObject("versions");
            resolvedVersion = findMatchingVersion(versions.keySet(), version);
            if (resolvedVersion == null) {
                return null;
            }
        }

        // 获取特定版本信息
        String url = activeRegistry + "/" + encodePackageName(packageName) + "/" + resolvedVersion;
        return fetchJson(url);
    }

    /**
     * 查找匹配的版本
     */
    private String findMatchingVersion(Set<String> versions, String range) {
        if (range.equals("latest") || range.equals("*")) {
            // 返回最新版本
            return versions.stream()
                    .filter(v -> !v.contains("-")) // 排除预发布版本
                    .max(this::compareVersions)
                    .orElse(null);
        }

        String prefix = range.substring(1); // 去掉 ^ 或 ~
        String[] parts = prefix.split("\\.");

        if (range.startsWith("^")) {
            // ^1.2.3 匹配 >=1.2.3 <2.0.0
            String major = parts[0];
            return versions.stream()
                    .filter(v -> v.startsWith(major + ".") && !v.contains("-"))
                    .max(this::compareVersions)
                    .orElse(null);
        } else if (range.startsWith("~")) {
            // ~1.2.3 匹配 >=1.2.3 <1.3.0
            String majorMinor = parts[0] + "." + (parts.length > 1 ? parts[1] : "0");
            return versions.stream()
                    .filter(v -> v.startsWith(majorMinor + ".") && !v.contains("-"))
                    .max(this::compareVersions)
                    .orElse(null);
        }

        // 精确匹配
        return versions.contains(range) ? range : null;
    }

    /**
     * 比较版本号
     */
    private int compareVersions(String v1, String v2) {
        String[] p1 = v1.split("\\.");
        String[] p2 = v2.split("\\.");

        for (int i = 0; i < Math.max(p1.length, p2.length); i++) {
            int n1 = i < p1.length ? parseVersionPart(p1[i]) : 0;
            int n2 = i < p2.length ? parseVersionPart(p2[i]) : 0;
            if (n1 != n2)
                return n1 - n2;
        }
        return 0;
    }

    private int parseVersionPart(String part) {
        try {
            return Integer.parseInt(part.replaceAll("[^0-9].*", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 下载并解压 tarball
     */
    private void downloadAndExtract(String tarballUrl, Path targetDir) throws Exception {
        // 下载到缓存
        String fileName = tarballUrl.substring(tarballUrl.lastIndexOf('/') + 1);
        Path cachePath = Paths.get(cacheDir, fileName);
        Files.createDirectories(cachePath.getParent());

        if (!Files.exists(cachePath)) {
            System.out.println("  → Downloading...");
            downloadFile(tarballUrl, cachePath);
        }

        // 解压 .tgz
        System.out.println("  → Extracting...");
        Files.createDirectories(targetDir);
        extractTgz(cachePath, targetDir);
    }

    /**
     * 下载文件
     */
    private void downloadFile(String urlStr, Path target) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Qin-Package-Manager/1.0");
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(60000);

        // 处理重定向
        int status = conn.getResponseCode();
        if (status == HttpURLConnection.HTTP_MOVED_TEMP ||
                status == HttpURLConnection.HTTP_MOVED_PERM ||
                status == HttpURLConnection.HTTP_SEE_OTHER) {
            String newUrl = conn.getHeaderField("Location");
            conn = (HttpURLConnection) new URL(newUrl).openConnection();
            conn.setRequestProperty("User-Agent", "Qin-Package-Manager/1.0");
        }

        try (InputStream in = conn.getInputStream();
                OutputStream out = Files.newOutputStream(target)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * 解压 .tgz 文件
     */
    private void extractTgz(Path tgzFile, Path targetDir) throws Exception {
        try (InputStream fis = Files.newInputStream(tgzFile);
                GZIPInputStream gzis = new GZIPInputStream(fis);
                TarInputStream tis = new TarInputStream(gzis)) {

            TarEntry entry;
            while ((entry = tis.getNextEntry()) != null) {
                // npm 包的内容在 package/ 目录下
                String name = entry.getName();
                if (name.startsWith("package/")) {
                    name = name.substring("package/".length());
                }
                if (name.isEmpty())
                    continue;

                Path entryPath = targetDir.resolve(name);

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    Files.createDirectories(entryPath.getParent());
                    try (OutputStream out = Files.newOutputStream(entryPath)) {
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = tis.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取 JSON
     */
    private JsonObject fetchJson(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("User-Agent", "Qin-Package-Manager/1.0");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(30000);

        if (conn.getResponseCode() != 200) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private String encodePackageName(String name) {
        // @scope/package -> @scope%2Fpackage
        return name.replace("/", "%2F");
    }

    /**
     * 设置镜像源
     */
    public void setRegistry(String registry) {
        this.activeRegistry = registry;
    }

    public String getRegistry() {
        return activeRegistry;
    }

    /**
     * 列出已安装的包
     */
    public void list() {
        Path nmDir = Paths.get(nodeModulesDir);
        if (!Files.exists(nmDir)) {
            System.out.println("No packages installed");
            return;
        }

        System.out.println("\nInstalled packages:");
        try {
            Files.list(nmDir)
                    .filter(Files::isDirectory)
                    .filter(p -> !p.getFileName().toString().startsWith("."))
                    .forEach(p -> {
                        try {
                            Path pkgJson = p.resolve(QinConstants.PACKAGE_JSON);
                            if (Files.exists(pkgJson)) {
                                JsonObject pkg = JsonParser.parseString(Files.readString(pkgJson)).getAsJsonObject();
                                String name = pkg.has("name") ? pkg.get("name").getAsString()
                                        : p.getFileName().toString();
                                String version = pkg.has("version") ? pkg.get("version").getAsString() : "unknown";
                                System.out.println("  " + name + "@" + version);
                            }
                        } catch (Exception e) {
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error listing packages: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Qin NPM Package Manager");
            System.out.println("Usage:");
            System.out.println("  install <package>[@version]  Install a package");
            System.out.println("  install                      Install all from package.json");
            System.out.println("  list                         List installed packages");
            System.out.println("Options:");
            System.out.println("  --dir <path>                 Project directory");
            return;
        }

        // 解析参数
        String projectDir = QinConstants.getCwd();
        List<String> cmdArgs = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if ("--dir".equals(args[i]) && i + 1 < args.length) {
                projectDir = args[++i];
                // 转为绝对路径
                projectDir = Paths.get(projectDir).toAbsolutePath().toString();
            } else {
                cmdArgs.add(args[i]);
            }
        }

        NpmPackageManager npm = new NpmPackageManager(projectDir);

        if (cmdArgs.isEmpty()) {
            npm.installAll();
            return;
        }

        String cmd = cmdArgs.get(0);
        switch (cmd) {
            case "install":
            case "i":
                if (cmdArgs.size() > 1) {
                    String pkg = cmdArgs.get(1);
                    // 处理 @scope/package@version 格式
                    String name, version;
                    if (pkg.startsWith("@")) {
                        // @scope/package@version
                        int lastAt = pkg.lastIndexOf('@');
                        if (lastAt > 0 && lastAt != pkg.indexOf('@')) {
                            name = pkg.substring(0, lastAt);
                            version = pkg.substring(lastAt + 1);
                        } else {
                            name = pkg;
                            version = "latest";
                        }
                    } else {
                        String[] parts = pkg.split("@");
                        name = parts[0];
                        version = parts.length > 1 ? parts[1] : "latest";
                    }
                    npm.install(name, version);
                } else {
                    npm.installAll();
                }
                break;
            case "list":
            case "ls":
                npm.list();
                break;
            default:
                System.err.println("Unknown command: " + cmd);
        }
    }
}
