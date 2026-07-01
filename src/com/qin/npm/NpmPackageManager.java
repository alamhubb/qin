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
    private static final List<String> DEFAULT_NPM_REGISTRIES = List.of(
            "https://registry.npmmirror.com", // 淘宝镜像（国内快）
            "https://registry.npmjs.org" // 官方源
    );

    private final String projectRoot;
    private final String cacheDir;
    private final String nodeModulesDir;
    private final List<String> registries;
    private final Gson gson;
    private String activeRegistry;

    public NpmPackageManager() {
        this(QinConstants.getCwd());
    }

    public NpmPackageManager(String projectRoot) {
        this(projectRoot, DEFAULT_NPM_REGISTRIES);
    }

    NpmPackageManager(String projectRoot, List<String> registries) {
        this.projectRoot = projectRoot;
        this.cacheDir = QinConstants.getGlobalNpmCacheDir().toString();
        this.nodeModulesDir = Paths.get(projectRoot, QinConstants.NODE_MODULES).toString();
        this.registries = List.copyOf(registries);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.activeRegistry = this.registries.get(0);
    }

    /**
     * 安装包
     */
    public boolean install(String packageName, String version) {
        try {
            installPackage(packageName, version, Paths.get(projectRoot), new LinkedHashSet<>());
            return true;
        } catch (Exception e) {
            System.err.println("✗ Failed to install " + packageName + ": " + e.getMessage());
            return false;
        }
    }

    private void installPackage(String packageName, String version, Path baseDir, Set<String> installed)
            throws Exception {
        System.out.println("📦 Installing " + packageName + "@" + version + "...");

        if (version != null && version.trim().startsWith("file:")) {
            Path sourceDir = resolveFileDependency(version, baseDir);
            String installKey = installKey(baseDir, packageName, "file:" + sourceDir.toAbsolutePath().normalize());
            if (!installed.add(installKey)) {
                return;
            }
            Path targetDir = packageInstallDir(baseDir, packageName);
            if (Files.exists(targetDir)) {
                QinUtils.deleteDir(targetDir);
            }
            linkPackageDirectory(sourceDir, targetDir);
            createBinLinks(targetDir, baseDir);
            installLocalPackageDependencies(sourceDir, installed);
            System.out.println("  ✓ Installed " + packageName + " from " + sourceDir);
            return;
        }

        if (!installed.add(installKey(baseDir, packageName, version))) {
            return;
        }

        // 1. 获取包信息
        JsonObject pkgInfo = fetchPackageInfo(packageName, version);
        if (pkgInfo == null) {
            throw new IOException("Package not found: " + packageName);
        }

        String resolvedVersion = pkgInfo.get("version").getAsString();
        String tarballUrl = pkgInfo.getAsJsonObject("dist").get("tarball").getAsString();

        System.out.println("  → Resolved version: " + resolvedVersion);

        // 2. 下载并解压
        Path targetDir = packageInstallDir(baseDir, packageName);
        if (Files.exists(targetDir)) {
            // 检查版本
            Path pkgJsonPath = targetDir.resolve(QinConstants.PACKAGE_JSON);
            if (Files.exists(pkgJsonPath)) {
                JsonObject existing = JsonParser.parseString(Files.readString(pkgJsonPath)).getAsJsonObject();
                if (existing.has("version") && existing.get("version").getAsString().equals(resolvedVersion)) {
                    createBinLinks(targetDir, baseDir);
                    installDependenciesObject(existing, "dependencies", targetDir, installed, false);
                    installDependenciesObject(existing, "optionalDependencies", targetDir, installed, true);
                    System.out.println("  ✓ Already installed");
                    return;
                }
            }
            QinUtils.deleteDir(targetDir);
        }

        downloadAndExtract(tarballUrl, targetDir);
        createBinLinks(targetDir, baseDir);
        System.out.println("  ✓ Installed " + packageName + "@" + resolvedVersion);

        installDependenciesObject(pkgInfo, "dependencies", targetDir, installed, false);
        installDependenciesObject(pkgInfo, "optionalDependencies", targetDir, installed, true);
    }

    private String installKey(Path baseDir, String packageName, String version) {
        return baseDir.toAbsolutePath().normalize() + "::" + packageName + "@" + version;
    }

    private Path packageInstallDir(Path baseDir, String packageName) {
        return baseDir.resolve(QinConstants.NODE_MODULES).resolve(packageName.replace('/', File.separatorChar));
    }

    private Path resolveFileDependency(String version, Path baseDir) throws IOException {
        String rawPath = version.substring("file:".length()).trim();
        if (rawPath.isBlank()) {
            throw new IOException("file: npm dependency path must not be empty");
        }
        Path path = Paths.get(rawPath);
        if (!path.isAbsolute()) {
            path = baseDir.resolve(path);
        }
        path = path.toAbsolutePath().normalize();
        if (!Files.isRegularFile(path.resolve(QinConstants.PACKAGE_JSON))) {
            throw new IOException("Local npm dependency does not contain package.json: " + path);
        }
        return path;
    }

    private void installLocalPackageDependencies(Path sourceDir, Set<String> installed) throws Exception {
        JsonObject pkgJson = JsonParser.parseString(Files.readString(sourceDir.resolve(QinConstants.PACKAGE_JSON)))
                .getAsJsonObject();
        installDependenciesObject(pkgJson, "dependencies", sourceDir, installed, false);
        installDependenciesObject(pkgJson, "optionalDependencies", sourceDir, installed, true);
    }

    private void installDependenciesObject(
            JsonObject pkgJson,
            String fieldName,
            Path baseDir,
            Set<String> installed,
            boolean optional) throws Exception {
        JsonObject deps = pkgJson.has(fieldName)
                ? pkgJson.getAsJsonObject(fieldName)
                : null;
        if (deps == null || deps.size() == 0) {
            return;
        }
        System.out.println("  → Installing " + fieldName + "...");
        for (String depName : deps.keySet()) {
            if (optional && !supportsCurrentPlatform(depName, deps.get(depName).getAsString(), baseDir)) {
                continue;
            }
            try {
                installPackage(depName, deps.get(depName).getAsString(), baseDir, installed);
            } catch (Exception error) {
                if (!optional) {
                    throw error;
                }
                System.out.println("  → Skipping optional dependency " + depName + ": " + error.getMessage());
            }
        }
    }

    private boolean supportsCurrentPlatform(String packageName, String version, Path baseDir) throws Exception {
        if (version != null && version.trim().startsWith("file:")) {
            Path sourceDir = resolveFileDependency(version, baseDir);
            JsonObject pkgJson = JsonParser.parseString(Files.readString(sourceDir.resolve(QinConstants.PACKAGE_JSON)))
                    .getAsJsonObject();
            return packageSupportsCurrentPlatform(pkgJson);
        }
        JsonObject pkgInfo = fetchPackageInfo(packageName, version);
        return pkgInfo == null || packageSupportsCurrentPlatform(pkgInfo);
    }

    private boolean packageSupportsCurrentPlatform(JsonObject pkgJson) {
        return fieldAllowsValue(pkgJson, "os", currentNpmOs())
                && fieldAllowsValue(pkgJson, "cpu", currentNpmCpu())
                && fieldAllowsValue(pkgJson, "libc", currentNpmLibc());
    }

    private boolean fieldAllowsValue(JsonObject pkgJson, String fieldName, String currentValue) {
        if (!pkgJson.has(fieldName)) {
            return true;
        }
        JsonElement raw = pkgJson.get(fieldName);
        if (!raw.isJsonArray()) {
            return true;
        }
        JsonArray values = raw.getAsJsonArray();
        boolean hasPositive = false;
        for (JsonElement element : values) {
            if (!element.isJsonPrimitive()) {
                continue;
            }
            String value = element.getAsString();
            if (value.startsWith("!")) {
                if (value.substring(1).equals(currentValue)) {
                    return false;
                }
            } else {
                hasPositive = true;
                if (value.equals(currentValue)) {
                    return true;
                }
            }
        }
        return !hasPositive;
    }

    private String currentNpmOs() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            return "win32";
        }
        if (os.contains("mac") || os.contains("darwin")) {
            return "darwin";
        }
        if (os.contains("linux")) {
            return "linux";
        }
        if (os.contains("freebsd")) {
            return "freebsd";
        }
        return os.replaceAll("[^a-z0-9]+", "");
    }

    private String currentNpmCpu() {
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        return switch (arch) {
            case "amd64", "x86_64" -> "x64";
            case "aarch64", "arm64" -> "arm64";
            case "x86", "i386", "i686" -> "ia32";
            default -> arch;
        };
    }

    private String currentNpmLibc() {
        if (!"linux".equals(currentNpmOs())) {
            return "";
        }
        return "glibc";
    }

    private void linkPackageDirectory(Path sourceDir, Path targetDir) throws IOException {
        Files.createDirectories(targetDir.getParent());
        try {
            Files.createSymbolicLink(targetDir, sourceDir);
        } catch (UnsupportedOperationException | IOException error) {
            if (QinConstants.isWindows()) {
                createWindowsJunction(sourceDir, targetDir);
            } else {
                throw error;
            }
        }
    }

    private void createWindowsJunction(Path sourceDir, Path targetDir) throws IOException {
        List<String> command = List.of(
                "cmd",
                "/c",
                "mklink",
                "/J",
                targetDir.toString(),
                sourceDir.toString());
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        String output;
        try (InputStream input = process.getInputStream()) {
            output = new String(input.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Failed to create junction " + targetDir + " -> " + sourceDir + ": " + output);
            }
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while creating junction " + targetDir + " -> " + sourceDir, error);
        }
    }

    private void createBinLinks(Path packageDir, Path baseDir) throws IOException {
        Path pkgJsonPath = packageDir.resolve(QinConstants.PACKAGE_JSON);
        if (!Files.isRegularFile(pkgJsonPath)) {
            return;
        }
        JsonObject pkgJson = JsonParser.parseString(Files.readString(pkgJsonPath)).getAsJsonObject();
        if (!pkgJson.has("bin")) {
            return;
        }
        String packageName = pkgJson.has("name")
                ? pkgJson.get("name").getAsString()
                : packageDir.getFileName().toString();
        JsonElement bin = pkgJson.get("bin");
        if (bin.isJsonPrimitive()) {
            createBinLink(commandNameFromPackage(packageName), packageDir, baseDir, bin.getAsString());
            return;
        }
        if (!bin.isJsonObject()) {
            return;
        }
        JsonObject binObject = bin.getAsJsonObject();
        for (String commandName : binObject.keySet()) {
            createBinLink(commandName, packageDir, baseDir, binObject.get(commandName).getAsString());
        }
    }

    private String commandNameFromPackage(String packageName) {
        int slash = packageName.lastIndexOf('/');
        return slash >= 0 ? packageName.substring(slash + 1) : packageName;
    }

    private void createBinLink(String commandName, Path packageDir, Path baseDir, String binPath) throws IOException {
        if (commandName == null || commandName.isBlank() || binPath == null || binPath.isBlank()) {
            return;
        }
        Path binDir = baseDir.resolve(QinConstants.NODE_MODULES).resolve(".bin");
        Files.createDirectories(binDir);
        Path target = packageDir.resolve(binPath).normalize();
        if (!target.startsWith(packageDir) || !Files.isRegularFile(target)) {
            throw new IOException("Invalid npm bin target for " + commandName + ": " + binPath);
        }
        if (QinConstants.isWindows()) {
            Path cmd = binDir.resolve(commandName + ".cmd");
            Files.writeString(cmd, windowsBinWrapper(binDir, target), java.nio.charset.StandardCharsets.UTF_8);
            return;
        }
        Path command = binDir.resolve(commandName);
        Files.deleteIfExists(command);
        try {
            Files.createSymbolicLink(command, binDir.relativize(target));
        } catch (UnsupportedOperationException | IOException error) {
            Files.writeString(command, unixBinWrapper(binDir, target), java.nio.charset.StandardCharsets.UTF_8);
        }
        command.toFile().setExecutable(true);
    }

    private String windowsBinWrapper(Path binDir, Path target) {
        String relativeTarget = binDir.relativize(target).toString();
        String normalized = relativeTarget.replace('/', '\\');
        String lower = normalized.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".cmd") || lower.endsWith(".bat") || lower.endsWith(".exe")) {
            return "@echo off\n\"%~dp0" + normalized + "\" %*\n";
        }
        return "@echo off\nnode \"%~dp0" + normalized + "\" %*\n";
    }

    private String unixBinWrapper(Path binDir, Path target) {
        String relativeTarget = binDir.relativize(target).toString().replace('\\', '/');
        return """
                #!/bin/sh
                DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
                exec "$DIR/%s" "$@"
                """.formatted(relativeTarget);
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
        Exception lastError = null;
        for (String registry : registryCandidates()) {
            try {
                JsonObject pkgInfo = fetchPackageInfoFromRegistry(registry, packageName, version);
                if (pkgInfo != null) {
                    activeRegistry = registry;
                    return pkgInfo;
                }
            } catch (Exception error) {
                lastError = error;
                System.out.println("  → Registry failed " + registry + ": " + error.getMessage());
            }
        }
        if (lastError != null) {
            throw lastError;
        }
        return null;
    }

    private List<String> registryCandidates() {
        LinkedHashSet<String> candidates = new LinkedHashSet<>();
        if (activeRegistry != null && !activeRegistry.isBlank()) {
            candidates.add(activeRegistry);
        }
        candidates.addAll(registries);
        return List.copyOf(candidates);
    }

    private JsonObject fetchPackageInfoFromRegistry(String registry, String packageName, String version) throws Exception {
        // 处理版本范围
        String resolvedVersion = version;
        if (version.startsWith("^") || version.startsWith("~") || version.equals("latest") || version.equals("*")) {
            // 获取所有版本，选择匹配的最新版
            JsonObject allVersions = fetchJson(registry + "/" + encodePackageName(packageName));
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
        String url = registry + "/" + encodePackageName(packageName) + "/" + resolvedVersion;
        return fetchJson(url);
    }

    /**
     * 查找匹配的版本
     */
    String findMatchingVersion(Set<String> versions, String range) {
        if (range.equals("latest") || range.equals("*")) {
            // 返回最新版本
            return versions.stream()
                    .filter(this::isStableVersion)
                    .max(this::compareVersions)
                    .orElse(null);
        }

        String prefix = range.substring(1); // 去掉 ^ 或 ~
        Version base = parseVersion(prefix);

        if (range.startsWith("^")) {
            return versions.stream()
                    .filter(this::isStableVersion)
                    .filter(version -> versionInCaretRange(parseVersion(version), base))
                    .max(this::compareVersions)
                    .orElse(null);
        } else if (range.startsWith("~")) {
            return versions.stream()
                    .filter(this::isStableVersion)
                    .filter(version -> versionInTildeRange(parseVersion(version), base))
                    .max(this::compareVersions)
                    .orElse(null);
        }

        // 精确匹配
        return versions.contains(range) ? range : null;
    }

    private boolean isStableVersion(String version) {
        return version != null && !version.contains("-");
    }

    private boolean versionInCaretRange(Version version, Version base) {
        if (compareVersions(version, base) < 0) {
            return false;
        }
        Version upper;
        if (base.major > 0) {
            upper = new Version(base.major + 1, 0, 0);
        } else if (base.minor > 0) {
            upper = new Version(0, base.minor + 1, 0);
        } else {
            upper = new Version(0, 0, base.patch + 1);
        }
        return compareVersions(version, upper) < 0;
    }

    private boolean versionInTildeRange(Version version, Version base) {
        if (compareVersions(version, base) < 0) {
            return false;
        }
        Version upper = new Version(base.major, base.minor + 1, 0);
        return compareVersions(version, upper) < 0;
    }

    /**
     * 比较版本号
     */
    private int compareVersions(String v1, String v2) {
        return compareVersions(parseVersion(v1), parseVersion(v2));
    }

    private int compareVersions(Version left, Version right) {
        if (left.major != right.major) {
            return left.major - right.major;
        }
        if (left.minor != right.minor) {
            return left.minor - right.minor;
        }
        return left.patch - right.patch;
    }

    private Version parseVersion(String version) {
        String[] parts = version.split("\\.");
        int major = parts.length > 0 ? parseVersionPart(parts[0]) : 0;
        int minor = parts.length > 1 ? parseVersionPart(parts[1]) : 0;
        int patch = parts.length > 2 ? parseVersionPart(parts[2]) : 0;
        return new Version(major, minor, patch);
    }

    private int parseVersionPart(String part) {
        try {
            return Integer.parseInt(part.replaceAll("[^0-9].*", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private record Version(int major, int minor, int patch) {
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
