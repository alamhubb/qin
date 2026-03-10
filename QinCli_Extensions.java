package com.qin.cli;

/**
 * QinCli 扩展方法
 *
 * 使用说明：
 * 1. 将下面的方法复制到 QinCli.java 中（在 printHelp() 方法之前）
 * 2. 在 switch 语句中添加对应的 case 分支
 * 3. 确保已导入必要的类：
 *    import com.qin.core.BuildLifecycle;
 *    import com.qin.types.JarResult;
 */

// ==================== 新增命令实现方法 ====================

/**
 * qin jar - 打包普通 JAR（不含依赖）
 */
private static void jarProject(String[] args) throws Exception {
    System.out.println(blue("→ Loading configuration..."));
    ConfigLoader configLoader = new ConfigLoader();
    QinConfig config = configLoader.load();

    BuildLifecycle lifecycle = new BuildLifecycle(QinConstants.getCwd(), config);
    JarResult result = lifecycle.jar();

    if (result.isSuccess()) {
        System.out.println(green("✓ JAR created: " + result.getJarPath()));
        System.out.println(gray("  Size: " + formatSize(result.getJarSize())));
    } else {
        System.err.println(red("✗ Failed: " + result.getError()));
        System.exit(1);
    }
}

/**
 * qin fatjar - 打包 Fat JAR（包含所有依赖）
 */
private static void fatjarProject(String[] args) throws Exception {
    System.out.println(blue("→ Loading configuration..."));
    ConfigLoader configLoader = new ConfigLoader();
    QinConfig config = configLoader.load();

    BuildLifecycle lifecycle = new BuildLifecycle(QinConstants.getCwd(), config);
    JarResult result = lifecycle.fatjar();

    if (result.isSuccess()) {
        System.out.println(green("✓ Fat JAR created: " + result.getJarPath()));
        System.out.println(gray("  Size: " + formatSize(result.getJarSize())));
    } else {
        System.err.println(red("✗ Failed: " + result.getError()));
        System.exit(1);
    }
}

/**
 * qin deps - 显示依赖树
 */
private static void showDependencies(String[] args) throws Exception {
    System.out.println(blue("→ Loading configuration..."));
    ConfigLoader configLoader = new ConfigLoader();
    QinConfig config = configLoader.load();

    System.out.println(blue("→ Dependencies:"));

    Map<String, String> allDeps = new HashMap<>();
    if (config.dependencies() != null) allDeps.putAll(config.dependencies());
    if (config.devDependencies() != null) allDeps.putAll(config.devDependencies());

    if (allDeps.isEmpty()) {
        System.out.println(gray("  No dependencies"));
        return;
    }

    for (Map.Entry<String, String> dep : allDeps.entrySet()) {
        System.out.println("  • " + dep.getKey() + " : " + dep.getValue());
    }
}

/**
 * 格式化文件大小（辅助方法）
 */
private static String formatSize(long bytes) {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
    return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
}

// ==================== switch 语句中需要添加的 case 分支 ====================

/*
在 main() 方法的 switch 语句中，添加以下 case 分支：

case "jar" -> jarProject(cmdArgs);           // 🆕 打包普通 JAR
case "fatjar" -> fatjarProject(cmdArgs);     // 🆕 打包 Fat JAR
case "deps" -> showDependencies(cmdArgs);    // 🆕 显示依赖树

建议顺序：
    case "init" -> initProject();
    case "run" -> runProject(cmdArgs);
    case "compile" -> compileProject(cmdArgs);
    case "test" -> runTests(cmdArgs);
    case "jar" -> jarProject(cmdArgs);           // 🆕
    case "fatjar" -> fatjarProject(cmdArgs);     // 🆕
    case "build" -> buildProject(cmdArgs);
    case "clean" -> cleanProject();
    case "sync" -> syncDependencies(cmdArgs);
    case "deps" -> showDependencies(cmdArgs);    // 🆕
    case "dev" -> devMode(cmdArgs);
    case "dist" -> distProject();
    case "help", "-h", "--help" -> printHelp();
    case "version", "-v", "--version" -> System.out.println("qin " + VERSION);
*/

// ==================== 可选：使用 BuildLifecycle 重构 buildProject ====================

/*
// 替换现有的 buildProject() 方法（可选）
private static void buildProject(String[] args) throws Exception {
    boolean skipTests = Arrays.asList(args).contains("--skip-tests");
    boolean clean = Arrays.asList(args).contains("--clean");

    if (clean) {
        cleanProject();
        System.out.println();
    }

    System.out.println(blue("→ Loading configuration..."));
    ConfigLoader configLoader = new ConfigLoader();
    QinConfig config = configLoader.load();

    // 设置跳过测试标志
    if (skipTests) {
        System.setProperty("skipTests", "true");
    }

    BuildLifecycle lifecycle = new BuildLifecycle(QinConstants.getCwd(), config);
    BuildResult result = lifecycle.build();

    if (skipTests) {
        System.clearProperty("skipTests");
    }

    if (!result.isSuccess()) {
        System.err.println(red("✗ BUILD FAILED: " + result.getError()));
        System.exit(1);
    }
}
*/

// ==================== 可选：使用增强版解析器优化 sync ====================

/*
在 syncDependenciesCore() 方法中，找到这段代码：

    LocalProjectResolver localResolver = new LocalProjectResolver(QinConstants.getCwd());
    LocalProjectResolver.ResolutionResult localResult = localResolver.resolveDependencies(deps);

替换为（需要导入 LocalProjectResolverEnhanced）：

    // 使用增强版解析器（支持自动编译本地项目）
    LocalProjectResolverEnhanced localResolver = new LocalProjectResolverEnhanced(QinConstants.getCwd());
    LocalProjectResolverEnhanced.ResolutionResult localResult = localResolver.resolveDependencies(deps);

然后在显示输出的地方添加：

    if (localResult.autoCompiledCount > 0) {
        System.out.println(green("  ✓ Auto-compiled " + localResult.autoCompiledCount + " local project(s)"));
    }
*/
