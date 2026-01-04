package com.qin.core;

import com.qin.types.*;
import com.qin.constants.QinConstants;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * Dependency Resolver for Qin
 * Uses Coursier to resolve Maven dependencies
 */
public class DependencyResolver {
    private static final List<String> DEFAULT_REPOS = Arrays.asList(
            "https://maven.aliyun.com/repository/public",
            "https://repo1.maven.org/maven2");

    private final String csCommand;
    private final List<String> repositories;
    private final Map<String, WorkspacePackage> localPackages;
    private final String projectRoot;
    private final String repoDir;
    private final boolean useLocalRep;

    public DependencyResolver(String csCommand, List<Repository> repos,
            Map<String, WorkspacePackage> localPackages,
            String projectRoot, boolean localRep) {
        // 如果没有提供csCommand,使用内嵌的coursier.jar
        if (csCommand == null || csCommand.isEmpty()) {
            // 1. 首先尝试提取内嵌的coursier.jar
            Path embeddedJar = extractEmbeddedCoursier();
            if (embeddedJar != null) {
                this.csCommand = "java -jar " + embeddedJar.toString();
            } else {
                // 2. 其次尝试qin安装目录的lib/coursier.jar
                String qinDir = getQinInstallDir();
                Path libJar = Paths.get(qinDir, "lib", "coursier.jar");
                if (Files.exists(libJar)) {
                    this.csCommand = "java -jar " + libJar.toString();
                } else {
                    // 3. 最后fallback到系统cs命令
                    this.csCommand = "cs";
                }
            }
        } else {
            this.csCommand = csCommand;
        }

        this.localPackages = localPackages != null ? localPackages : new HashMap<>();
        this.projectRoot = projectRoot;
        this.useLocalRep = localRep;

        // 全局libs目录：~/.qin/libs（所有项目共享）
        // 本地libs目录：.qin/libs（创建符号链接指向全局）
        this.repoDir = localRep
                ? QinPaths.getLocalLibsDir(projectRoot).toString()
                : QinPaths.getGlobalLibsDir().toString();

        if (repos != null && !repos.isEmpty()) {
            this.repositories = repos.stream()
                    .map(Repository::url)
                    .collect(Collectors.toList());
        } else {
            this.repositories = DEFAULT_REPOS;
        }
    }

    /**
     * 获取qin的安装目录
     */
    private static String getQinInstallDir() {
        // 尝试从环境变量或系统属性获取qin目录
        String qinDir = System.getProperty("qin.home");
        if (qinDir != null) {
            return qinDir;
        }

        // Fallback: 获取当前jar所在目录
        try {
            String jarPath = DependencyResolver.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            // 如果在.qin/classes中,返回上两级
            if (jarPath.contains(".qin")) {
                return Paths.get(jarPath).getParent().getParent().toString();
            }
        } catch (Exception e) {
            // Ignore
        }

        // 最后fallback: 当前目录
        return System.getProperty("user.dir");
    }

    /**
     * 提取内嵌的coursier.jar到临时目录
     */
    private static Path extractEmbeddedCoursier() {
        try {
            // 首先尝试从classpath中的lib/coursier.jar加载
            InputStream is = DependencyResolver.class.getResourceAsStream("/lib/coursier.jar");
            if (is == null) {
                // 尝试相对路径
                is = DependencyResolver.class.getClassLoader().getResourceAsStream("lib/coursier.jar");
            }

            if (is != null) {
                // 提取到临时目录
                Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), ".qin");
                Files.createDirectories(tempDir);
                Path coursierJar = tempDir.resolve("coursier.jar");

                Files.copy(is, coursierJar, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                is.close();

                return coursierJar;
            }
        } catch (Exception e) {
            // Ignore and fallback
        }
        return null;
    }

    private static String getGlobalRepoDir() {
        String home = System.getProperty("user.home");
        return Paths.get(home, ".qin", "libs").toString();
    }

    /**
     * Resolve dependencies and return classpath
     */
    public String resolveFromObject(Map<String, String> deps) throws IOException {
        if (deps == null || deps.isEmpty()) {
            return "";
        }

        List<String> mavenDeps = new ArrayList<>();
        List<String> localPaths = new ArrayList<>();

        for (Map.Entry<String, String> entry : deps.entrySet()) {
            String name = entry.getKey();
            String version = entry.getValue();

            if (localPackages.containsKey(name)) {
                WorkspacePackage pkg = localPackages.get(name);
                if (!"*".equals(version)) {
                    String pkgVersion = pkg.getConfig().version();
                    if (pkgVersion == null)
                        pkgVersion = "0.0.0";
                    if (!checkVersionMatch(version, pkgVersion)) {
                        throw new IOException(
                                String.format("本地包 \"%s\" 版本不匹配: 需要 %s, 实际 %s",
                                        name, version, pkgVersion));
                    }
                }
                localPaths.add(pkg.getClassesDir());
            } else {
                // 支持 Qin 分隔符，转换为 Maven 格式
                String mavenCoordinate = QinConstants.toMavenCoordinate(name) +
                        QinConstants.MAVEN_COORDINATE_SEPARATOR + version;
                mavenDeps.add(mavenCoordinate);
            }
        }

        String mavenClasspath = "";
        if (!mavenDeps.isEmpty()) {
            mavenClasspath = resolve(mavenDeps);
        }

        List<String> allPaths = new ArrayList<>(localPaths);
        if (!mavenClasspath.isEmpty()) {
            allPaths.addAll(parseClasspath(mavenClasspath));
        }

        return buildClasspath(allPaths);
    }

    /**
     * Resolve dependencies using Coursier
     */
    public String resolve(List<String> deps) throws IOException {
        if (deps == null || deps.isEmpty()) {
            return "";
        }

        ResolveResult result = resolveWithDetails(deps);
        if (!result.isSuccess()) {
            throw new IOException(result.getError());
        }

        return result.classpath();
    }

    /**
     * Resolve dependencies and return detailed result
     */
    public ResolveResult resolveWithDetails(List<String> deps) {
        if (deps == null || deps.isEmpty()) {
            return ResolveResult.success("", new ArrayList<>());
        }

        // Validate dependencies
        for (String dep : deps) {
            if (!isValidDependency(dep)) {
                return ResolveResult.failure(
                        String.format("Invalid dependency format: \"%s\". Expected: groupId:artifactId:version", dep));
            }
        }

        try {
            List<String> args = new ArrayList<>();
            args.add(csCommand);
            args.add("fetch");
            args.addAll(deps);
            args.add("--classpath");

            // 添加缓存配置
            String cacheDir = System.getProperty("user.home") + "/.cache/coursier";
            args.add("--cache");
            args.add(cacheDir);

            // 启用并行下载
            args.add("--parallel");
            args.add("8");

            // 添加仓库
            for (String repo : repositories) {
                args.add("-r");
                args.add(repo);
            }

            // 设置进度模式
            args.add("--progress");
            args.add("--ttl");
            args.add("24h"); // 缓存有效期 24 小时

            ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(false);
            Process proc = pb.start();

            // 异步读取进度信息
            Thread progressThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(proc.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // 显示下载进度
                        if (line.contains("Downloading") || line.contains("Downloaded")) {
                            System.out.println("  " + line);
                        }
                    }
                } catch (IOException e) {
                    // Ignore
                }
            });
            progressThread.setDaemon(true);
            progressThread.start();

            String stdout = readStream(proc.getInputStream());
            int exitCode = proc.waitFor();

            if (exitCode != 0) {
                return ResolveResult.failure(
                        "Coursier failed to resolve dependencies");
            }

            String classpath = stdout.trim();
            List<String> globalJarPaths = parseClasspath(classpath);
            List<String> localJarPaths = copyToRepository(globalJarPaths);

            return ResolveResult.success(buildClasspath(localJarPaths), localJarPaths);
        } catch (Exception e) {
            return ResolveResult.failure(e.getMessage());
        }
    }

    private List<String> copyToRepository(List<String> globalPaths) throws IOException {
        // 全局存储目录
        Path globalLibsDir = QinPaths.getGlobalLibsDir();
        Files.createDirectories(globalLibsDir);

        // 项目根目录的 libs 符号链接目录
        Path projectLibsDir = Paths.get(projectRoot, "libs");
        Files.createDirectories(projectLibsDir);

        List<String> classpathEntries = new ArrayList<>();

        for (String globalPath : globalPaths) {
            if (!globalPath.endsWith(".jar"))
                continue;

            // 提取包信息：groupId, artifactId 和 version
            PackageInfo pkgInfo = extractPackageInfo(globalPath);
            if (pkgInfo == null) {
                // 如果解析失败，直接使用 Coursier 下载的路径
                classpathEntries.add(globalPath);
                continue;
            }

            String jarName = Path.of(globalPath).getFileName().toString();

            // 坐标：com.google.code.gson@gson
            String coordinate = pkgInfo.groupId + QinConstants.QIN_COORDINATE_SEPARATOR + pkgInfo.artifactId;
            String coordinateWithVersion = coordinate + QinConstants.VERSION_SEPARATOR + pkgInfo.version;

            // 1. 复制到全局存储
            Path globalPackageDir = globalLibsDir.resolve(coordinate);
            Path globalVersionDir = globalPackageDir.resolve(coordinateWithVersion);
            Files.createDirectories(globalVersionDir);

            Path globalJarPath = globalVersionDir.resolve(coordinateWithVersion + ".jar");
            if (!Files.exists(globalJarPath)) {
                Files.copy(Path.of(globalPath), globalJarPath);
            }

            // 2. 在项目 libs/ 创建符号链接（链接整个包目录）
            Path projectSymlink = projectLibsDir.resolve(coordinate);
            if (!Files.exists(projectSymlink)) {
                try {
                    Files.createSymbolicLink(projectSymlink, globalPackageDir);
                } catch (IOException e) {
                    // Windows 符号链接可能失败，忽略（不影响编译）
                    System.err.println("Warning: Failed to create symlink for " + coordinate + ": " + e.getMessage());
                }
            }

            // 3. Classpath 使用全局真实路径
            classpathEntries.add(globalJarPath.toString());
        }

        return classpathEntries;
    }

    /**
     * 包信息
     */
    private static class PackageInfo {
        final String groupId; // com.github.ben-manes.caffeine (. 不展开)
        final String artifactId; // caffeine (作为子目录)
        final String version; // 3.1.8

        PackageInfo(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }
    }

    /**
     * 从 Maven 路径中提取包信息
     * 
     * 例如：
     * - .../com/github/ben-manes/caffeine/caffeine/3.1.8/caffeine-3.1.8.jar
     * - 提取：groupId=com.github.ben-manes.caffeine, artifactId=caffeine,
     * version=3.1.8
     */
    private PackageInfo extractPackageInfo(String jarPath) {
        String normalized = jarPath.replace("\\", "/");
        String[] patterns = { "/maven2/", "/public/", "/repository/" };

        for (String pattern : patterns) {
            int idx = normalized.indexOf(pattern);
            if (idx != -1) {
                String afterPattern = normalized.substring(idx + pattern.length());
                String[] parts = afterPattern.split("/");

                // Maven 路径格式：groupId/artifactId/version/artifactId-version.jar
                // 例如：com/github/ben-manes/caffeine/caffeine/3.1.8/caffeine-3.1.8.jar
                // parts = ["com", "github", "ben-manes", "caffeine", "caffeine", "3.1.8",
                // "caffeine-3.1.8.jar"]

                if (parts.length >= 4) {
                    // 最后一个是 jar 文件名，倒数第二个是版本，倒数第三个是 artifactId
                    String version = parts[parts.length - 2];
                    String artifactId = parts[parts.length - 3];

                    // groupId 是从开始到 artifactId 之前的所有部分
                    String[] groupParts = Arrays.copyOf(parts, parts.length - 3);
                    String groupId = String.join(".", groupParts);

                    return new PackageInfo(groupId, artifactId, version);
                }
            }
        }

        return null;
    }

    private boolean isValidDependency(String dep) {
        String[] parts = dep.split(":");
        return parts.length >= 3 && Arrays.stream(parts).allMatch(p -> !p.isEmpty());
    }

    private boolean checkVersionMatch(String required, String actual) {
        if ("*".equals(required))
            return true;
        // 简化版本匹配，完整实现需要 semver 库
        if (required.startsWith("^") || required.startsWith("~")) {
            String base = required.substring(1);
            return actual.startsWith(base.split("\\.")[0]);
        }
        return required.equals(actual);
    }

    public static String getClasspathSeparator() {
        return System.getProperty("os.name").toLowerCase().contains("win") ? ";" : ":";
    }

    public static List<String> parseClasspath(String classpath) {
        if (classpath == null || classpath.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(classpath.split(getClasspathSeparator()))
                .filter(p -> !p.isEmpty())
                .collect(Collectors.toList());
    }

    public static String buildClasspath(List<String> paths) {
        return String.join(getClasspathSeparator(), paths);
    }

    private String readStream(InputStream is) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
