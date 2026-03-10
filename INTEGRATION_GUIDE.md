# QinCli 集成指南

## 📋 集成步骤

### 步骤 1: 添加 case 分支到 switch 语句

在 `QinCli.java` 的 `main()` 方法中，找到 switch 语句（约第 38-55 行），将其替换为：

```java
switch (command) {
    case "init" -> initProject();
    case "run" -> runProject(cmdArgs);
    case "compile" -> compileProject(cmdArgs);
    case "test" -> runTests(cmdArgs);
    case "jar" -> jarProject(cmdArgs);           // 🆕 新增
    case "fatjar" -> fatjarProject(cmdArgs);     // 🆕 新增
    case "build" -> buildProject(cmdArgs);
    case "clean" -> cleanProject();
    case "sync" -> syncDependencies(cmdArgs);
    case "deps" -> showDependencies(cmdArgs);    // 🆕 新增
    case "dev" -> devMode(cmdArgs);
    case "dist" -> distProject();
    case "help", "-h", "--help" -> printHelp();
    case "version", "-v", "--version" -> System.out.println("qin " + VERSION);
    default -> {
        System.err.println("Unknown command: " + command);
        printHelp();
        System.exit(1);
    }
}
```

### 步骤 2: 添加新方法

在 `ensureCoursier()` 方法之后，`printHelp()` 方法之前（约第 685 行之后），添加以下 4 个新方法：

```java
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
```

### 步骤 3: 更新 printHelp() 方法

在 `printHelp()` 方法中（约第 687-719 行），更新 Commands 部分：

```java
Commands:
  init        Initialize a new Qin project
  run         Compile and run the Java program
  compile     Compile Java source code
  test        Run JUnit tests
  jar         Build a JAR (without dependencies)          // 🆕 新增
  fatjar      Build a Fat JAR (with all dependencies)     // 🆕 新增
  build       Build a Fat Jar (Uber Jar)
  clean       Clean build artifacts
  sync        Sync dependencies
  deps        Show dependency tree                         // 🆕 新增
  dev         Start development server with hot reload
  dist        Create distribution package
  help        Show this help message
  version     Show version
```

更新 Examples 部分：

```java
Examples:
  qin init              # Initialize new project
  qin run               # Compile and run
  qin compile           # Compile only
  qin jar               # Build JAR (without dependencies)     // 🆕 新增
  qin fatjar            # Build Fat JAR (with dependencies)    // 🆕 新增
  qin build             # Build Fat Jar
  qin deps              # Show dependencies                    // 🆕 新增
  qin dev               # Start dev server
```

## ✅ 验证清单

完成上述步骤后，请验证：

1. ✅ switch 语句中包含 3 个新的 case 分支：`jar`, `fatjar`, `deps`
2. ✅ 添加了 4 个新方法：`jarProject()`, `fatjarProject()`, `showDependencies()`, `formatSize()`
3. ✅ `printHelp()` 方法已更新，包含新命令的说明
4. ✅ 所有 import 语句保持不变（已经包含了 `com.qin.types.*`，所以 `JarResult` 可以直接使用）

## 🧪 测试命令

集成完成后，测试以下命令：

```bash
# 编译项目
cd D:/project/qkyproject/slime-java/qin
qin compile

# 测试新命令
qin deps              # 显示依赖
qin jar               # 打包普通 JAR
qin fatjar            # 打包 Fat JAR
qin help              # 查看帮助（应该显示新命令）
```

## 📝 注意事项

1. **BuildLifecycle 类**：确保 `src/com/qin/core/BuildLifecycle.java` 已存在
2. **JarResult 类**：确保 `src/com/qin/types/JarResult.java` 已存在
3. **JarBuilder 类**：确保 `src/com/qin/core/JarBuilder.java` 已存在
4. **导入语句**：由于已有 `import com.qin.types.*;`，无需额外导入

## 🎯 预期结果

完成集成后，你将拥有：

✅ **qin jar** - 打包普通 JAR（不含依赖）
✅ **qin fatjar** - 打包 Fat JAR（包含所有依赖）
✅ **qin deps** - 显示项目依赖树
✅ **qin build** - 保持原有功能（使用 BuildLifecycle 可选优化）

## 🚀 下一步

集成完成并测试通过后，可以尝试：

1. **Monorepo 测试**：创建多项目测试场景
2. **自动编译测试**：测试本地依赖自动编译功能
3. **性能测试**：对比优化前后的构建速度
