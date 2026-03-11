package com.qin.core;

import com.qin.types.Repository;
import com.qin.types.ResolveResult;
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
        // 婵″倹鐏夊▽鈩冩箒閹绘劒绶礳sCommand,娴ｈ法鏁ら崘鍛サ閻ㄥ垻oursier.jar
        if (csCommand == null || csCommand.isEmpty()) {
            // 1. 妫ｆ牕鍘涚亸婵婄槸閹绘劕褰囬崘鍛サ閻ㄥ垻oursier.jar
            Path embeddedJar = extractEmbeddedCoursier();
            if (embeddedJar != null) {
                this.csCommand = "java -jar " + embeddedJar.toString();
            } else {
                // 2. 閸忚埖顐肩亸婵婄槸qin鐎瑰顥婇惄顔肩秿閻ㄥ埐ib/coursier.jar
                String qinDir = getQinInstallDir();
                Path libJar = Paths.get(qinDir, "lib", "coursier.jar");
                if (Files.exists(libJar)) {
                    this.csCommand = "java -jar " + libJar.toString();
                } else {
                    // 3. 閺堚偓閸氬穳allback閸掓壆閮寸紒鐒巗閸涙垝鎶?
                    this.csCommand = "cs";
                }
            }
        } else {
            this.csCommand = csCommand;
        }

        this.localPackages = localPackages != null ? localPackages : new HashMap<>();
        this.projectRoot = projectRoot;
        this.useLocalRep = localRep;

        // 閸忋劌鐪琹ibs閻╊喖缍嶉敍娈?.qin/libs閿涘牊澧嶉張澶愩€嶉惄顔煎彙娴滎偓绱?
        // 閺堫剙婀磍ibs閻╊喖缍嶉敍?qin/libs閿涘牆鍨卞铏诡儊閸欑兘鎽奸幒銉﹀瘹閸氭垵鍙忕仦鈧敍?
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
     * 閼惧嘲褰噏in閻ㄥ嫬鐣ㄧ憗鍛窗瑜?
     */
    private static String getQinInstallDir() {
        // 鐏忔繆鐦禒搴ｅ箚婢у啫褰夐柌蹇斿灗缁崵绮虹仦鐐粹偓褑骞忛崣鏉歩n閻╊喖缍?
        String qinDir = System.getProperty("qin.home");
        if (qinDir != null) {
            return qinDir;
        }

        // Fallback: 閼惧嘲褰囪ぐ鎾冲jar閹碘偓閸︺劎娲拌ぐ?
        try {
            String jarPath = DependencyResolver.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            // 婵″倹鐏夐崷?build/classes 娑?鏉╂柨娲栨稉濠佽⒈缁?
            if (jarPath.contains("build")) {
                return Paths.get(jarPath).getParent().getParent().toString();
            }
        } catch (Exception e) {
            // Ignore
        }

        // 閺堚偓閸氬穳allback: 瑜版挸澧犻惄顔肩秿
        return QinConstants.getCwd();
    }

    /**
     * 閹绘劕褰囬崘鍛サ閻ㄥ垻oursier.jar閸掗澶嶉弮鍓佹窗瑜?
     */
    private static Path extractEmbeddedCoursier() {
        try {
            // 妫ｆ牕鍘涚亸婵婄槸娴犲穯lasspath娑擃厾娈憀ib/coursier.jar閸旂姾娴?
            InputStream is = DependencyResolver.class.getResourceAsStream("/lib/coursier.jar");
            if (is == null) {
                // 鐏忔繆鐦惄绋款嚠鐠侯垰绶?
                is = DependencyResolver.class.getClassLoader().getResourceAsStream("lib/coursier.jar");
            }

            if (is != null) {
                // 閹绘劕褰囬崚棰佸閺冨墎娲拌ぐ?
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
        String home = QinConstants.getHomeDir();
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
                                String.format("閺堫剙婀撮崠?\"%s\" 閻楀牊婀版稉宥呭爱闁? 闂団偓鐟?%s, 鐎圭偤妾?%s",
                                        name, version, pkgVersion));
                    }
                }
                localPaths.add(pkg.getClassesDir());
            } else {
                String mavenCoordinate = toResolveCoordinate(name, version);
                if (mavenCoordinate == null) {
                    throw new IOException(
                            String.format(
                                    "Unsupported dependency notation: \"%s\" -> \"%s\". Use groupId:artifactId:version for Maven dependencies or declare a matching local Qin package name.",
                                    name,
                                    version));
                }
                System.out.println("[DEBUG] Resolved dependency: " + name + " -> " + mavenCoordinate);
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
        return resolveWithDetails(deps, false, false);
    }

    /**
     * Resolve dependencies with optional sources and javadoc
     * @param deps List of dependencies
     * @param downloadSources Whether to download sources jars
     * @param downloadJavadoc Whether to download javadoc jars
     */
    public ResolveResult resolveWithDetails(List<String> deps, boolean downloadSources, boolean downloadJavadoc) {
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

            // 娑撳娴囧┃鎰垳
            if (downloadSources) {
                args.add("--sources");
            }

            // 娑撳娴?Javadoc
            if (downloadJavadoc) {
                args.add("--javadoc");
            }

            // 濞ｈ濮炵紓鎾崇摠闁板秶鐤?
            String cacheDir = QinConstants.getHomeDir() + "/.cache/coursier";
            args.add("--cache");
            args.add(cacheDir);

            // 閸氼垳鏁ら獮鎯邦攽娑撳娴?
            args.add("--parallel");
            args.add("8");

            // 濞ｈ濮炴禒鎾崇氨
            for (String repo : repositories) {
                args.add("-r");
                args.add(repo);
            }

            // 鐠佸墽鐤嗘潻娑樺濡€崇础
            args.add("--progress");
            args.add("--ttl");
            args.add("24h"); // 缂傛挸鐡ㄩ張澶嬫櫏閺?24 鐏忓繑妞?

            ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(false);
            Process proc = pb.start();

            // 瀵倹顒炵拠璇插絿鏉╂稑瀹虫穱鈩冧紖
            Thread progressThread = new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(proc.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // 閺勫墽銇氭稉瀣祰鏉╂稑瀹?
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
        // 閸忋劌鐪€涙ê鍋嶉惄顔肩秿
        Path globalLibsDir = QinPaths.getGlobalLibsDir();
        Files.createDirectories(globalLibsDir);

        // 妞ゅ湱娲伴弽鍦窗瑜版洜娈?libs 缁楋箑褰块柧鐐复閻╊喖缍?
        Path projectLibsDir = Paths.get(projectRoot, "libs");
        Files.createDirectories(projectLibsDir);

        List<String> classpathEntries = new ArrayList<>();

        for (String globalPath : globalPaths) {
            if (!globalPath.endsWith(".jar"))
                continue;

            // 閹绘劕褰囬崠鍛繆閹垽绱癵roupId, artifactId 閸?version
            PackageInfo pkgInfo = extractPackageInfo(globalPath);
            if (pkgInfo == null) {
                // 婵″倹鐏夌憴锝嗙€芥径杈Е閿涘瞼娲块幒銉ゅ▏閻?Coursier 娑撳娴囬惃鍕熅瀵?
                // 娴ｅ棗褰ч張澶夊瘜 jar 閸旂姴鍙?classpath閿涘ources/javadoc 娑撳秴濮為崗?
                if (!isSourcesOrJavadoc(globalPath)) {
                    classpathEntries.add(globalPath);
                }
                continue;
            }

            String jarName = Path.of(globalPath).getFileName().toString();

            // 閸ф劖鐖ｉ敍姝漮m.google.code.gson@gson
            String coordinate = pkgInfo.groupId + QinConstants.QIN_COORDINATE_SEPARATOR + pkgInfo.artifactId;
            String coordinateWithVersion = coordinate + QinConstants.VERSION_SEPARATOR + pkgInfo.version;

            // 1. 婢跺秴鍩楅崚鏉垮弿鐏炩偓鐎涙ê鍋?
            Path globalPackageDir = globalLibsDir.resolve(coordinate);
            Path globalVersionDir = globalPackageDir.resolve(coordinateWithVersion);
            Files.createDirectories(globalVersionDir);

            // 閸掋倖鏌囬弰顖氭儊閺?sources 閹?javadoc jar
            String classifier = getClassifier(jarName);
            String targetJarName;
            if (classifier != null) {
                targetJarName = coordinateWithVersion + "-" + classifier + ".jar";
            } else {
                targetJarName = coordinateWithVersion + ".jar";
            }

            Path globalJarPath = globalVersionDir.resolve(targetJarName);
            if (!Files.exists(globalJarPath)) {
                Files.copy(Path.of(globalPath), globalJarPath);
            }

            // 2. 閸︺劑銆嶉惄?libs/ 閸掓稑缂?Junction閿涘牓鎽奸幒銉︽殻娑擃亜瀵橀惄顔肩秿閿?
            // Junction 娑撳秹娓剁憰浣侯吀閻炲棗鎲抽弶鍐閿涘本鐦?Symlink 閺囨潙褰查棃?
            Path projectJunction = projectLibsDir.resolve(coordinate);
            if (!Files.exists(projectJunction)) {
                try {
                    createJunction(projectJunction, globalPackageDir);
                } catch (IOException e) {
                    // Junction 閸掓稑缂撴径杈Е閿涘本澧﹂崡浼存晩鐠囶垯绲炬稉宥呭閸濆秶绱拠?
                    System.err.println("Warning: Failed to create junction for " + coordinate + ": " + e.getMessage());
                }
            }

            // 3. 閸欘亝婀佹稉?jar 閸旂姴鍙?classpath閿涘牅绗夐崠鍛 sources 閸?javadoc閿?
            if (classifier == null) {
                classpathEntries.add(globalJarPath.toString());
            }
        }

        return classpathEntries;
    }

    /**
     * 閸栧懍淇婇幁?
     */
    private static class PackageInfo {
        final String groupId; // com.github.ben-manes.caffeine (. 娑撳秴鐫嶅鈧?
        final String artifactId; // caffeine (娴ｆ粈璐熺€涙劗娲拌ぐ?
        final String version; // 3.1.8

        PackageInfo(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }
    }

    /**
     * 娴?Maven 鐠侯垰绶炴稉顓熷絹閸欐牕瀵樻穱鈩冧紖
     * 
     * 娓氬顩ч敍?
     * - .../com/github/ben-manes/caffeine/caffeine/3.1.8/caffeine-3.1.8.jar
     * - 閹绘劕褰囬敍姝oupId=com.github.ben-manes.caffeine, artifactId=caffeine,
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

                // Maven 鐠侯垰绶為弽鐓庣础閿涙roupId/artifactId/version/artifactId-version.jar
                // 娓氬顩ч敍姝漮m/github/ben-manes/caffeine/caffeine/3.1.8/caffeine-3.1.8.jar
                // parts = ["com", "github", "ben-manes", "caffeine", "caffeine", "3.1.8",
                // "caffeine-3.1.8.jar"]

                if (parts.length >= 4) {
                    // 閺堚偓閸氬簼绔存稉顏呮Ц jar 閺傚洣娆㈤崥宥忕礉閸婃帗鏆熺粭顑跨癌娑擃亝妲搁悧鍫熸拱閿涘苯鈧帗鏆熺粭顑跨瑏娑擃亝妲?artifactId
                    String version = parts[parts.length - 2];
                    String artifactId = parts[parts.length - 3];

                    // groupId 閺勵垯绮犲鈧慨瀣煂 artifactId 娑斿澧犻惃鍕閺堝鍎撮崚?
                    String[] groupParts = Arrays.copyOf(parts, parts.length - 3);
                    String groupId = String.join(".", groupParts);

                    return new PackageInfo(groupId, artifactId, version);
                }
            }
        }

        return null;
    }

    /**
     * 閸掋倖鏌?jar 閺傚洣娆㈤弰顖氭儊閺?sources 閹?javadoc
     */
    private boolean isSourcesOrJavadoc(String jarPath) {
        String fileName = Path.of(jarPath).getFileName().toString().toLowerCase();
        return fileName.contains("-sources") || fileName.contains("-javadoc");
    }

    /**
     * 娴?jar 閺傚洣娆㈤崥宥勮厬閹绘劕褰?classifier (sources, javadoc, 閹?null)
     */
    private String getClassifier(String jarName) {
        if (jarName.contains("-sources.jar") || jarName.contains("-sources-")) {
            return "sources";
        }
        if (jarName.contains("-javadoc.jar") || jarName.contains("-javadoc-")) {
            return "javadoc";
        }
        return null;
    }

    private boolean isValidDependency(String dep) {
        String[] parts = dep.split(":");
        return parts.length >= 3 && Arrays.stream(parts).allMatch(p -> !p.isEmpty());
    }

    private String toResolveCoordinate(String name, String version) {
        if (name == null || name.isBlank()) {
            return null;
        }

        String normalizedName = QinConstants.toMavenCoordinate(name.trim());
        String normalizedVersion = version == null ? "" : version.trim();
        int segmentCount = normalizedName.split(":").length;

        if (segmentCount >= 3) {
            return isProviderMarker(normalizedVersion) || normalizedVersion.isEmpty()
                    ? normalizedName
                    : null;
        }

        if (segmentCount == 2 && !normalizedVersion.isEmpty() && !isProviderMarker(normalizedVersion)) {
            return normalizedName + QinConstants.MAVEN_COORDINATE_SEPARATOR + normalizedVersion;
        }

        return null;
    }

    private boolean isProviderMarker(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return switch (value.toLowerCase(Locale.ROOT)) {
            case "maven", "remote" -> true;
            default -> false;
        };
    }
    private boolean checkVersionMatch(String required, String actual) {
        if ("*".equals(required))
            return true;
        // 缁犫偓閸栨牜澧楅張顒€灏柊宥忕礉鐎瑰本鏆ｇ€圭偟骞囬棁鈧憰?semver 鎼?
        if (required.startsWith("^") || required.startsWith("~")) {
            String base = required.substring(1);
            return actual.startsWith(base.split("\\.")[0]);
        }
        return required.equals(actual);
    }

    public static String getClasspathSeparator() {
        return QinConstants.getClasspathSeparator();
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

    /**
     * 閸?Windows 娑撳﹤鍨卞?Junction閿涘牏娲拌ぐ鏇′粓閹恒儻绱?
     * Junction 娑撳秹娓剁憰浣侯吀閻炲棗鎲抽弶鍐閿涘本鐦?Symlink 閺囨潙褰查棃?
     * 
     * @param link   鐟曚礁鍨卞铏规畱 Junction 鐠侯垰绶?
     * @param target 閻╊喗鐖ｉ惄顔肩秿鐠侯垰绶?
     */
    private void createJunction(Path link, Path target) throws IOException {
        if (!QinConstants.isWindows()) {
            // 闂?Windows 缁崵绮烘担璺ㄦ暏 symlink
            Files.createSymbolicLink(link, target);
            return;
        }

        // Windows: 娴ｈ法鏁?cmd mklink /J 閸掓稑缂?Junction
        ProcessBuilder pb = new ProcessBuilder(
                "cmd", "/c", "mklink", "/J",
                link.toAbsolutePath().toString(),
                target.toAbsolutePath().toString());
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String output = readStream(process.getInputStream());
                throw new IOException("mklink /J failed (exit " + exitCode + "): " + output);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Junction creation interrupted", e);
        }
    }
}
