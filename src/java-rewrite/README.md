# Qin Java Rewrite

这是 Qin 构建工具的 Java 重写版本。

## 项目结构

```
src/java-rewrite/
├── com/qin/
│   ├── cli/                    # CLI 入口
│   │   └── QinCli.java         # 主命令行入口
│   ├── commands/               # 命令实现
│   │   ├── InitCommand.java    # init 命令
│   │   └── EnvCommand.java     # env 命令
│   ├── core/                   # 核心模块
│   │   ├── ConfigLoader.java   # 配置加载器
│   │   ├── DependencyResolver.java  # 依赖解析器
│   │   ├── EnvironmentChecker.java  # 环境检查器
│   │   ├── FatJarBuilder.java  # Fat Jar 构建器
│   │   ├── JavaRunner.java     # Java 运行器
│   │   ├── PluginDetector.java # 插件检测器
│   │   ├── PluginManager.java  # 插件管理器
│   │   ├── WorkspaceLoader.java # 工作区加载器
│   │   ├── WorkspacePackage.java # 工作区包
│   │   └── DetectionResult.java # 检测结果
│   ├── java/                   # Java 工具
│   │   ├── ClasspathUtils.java # Classpath 工具
│   │   ├── JavaBuilder.java    # Java 构建器
│   │   └── PackageManager.java # 包管理器
│   └── types/                  # 类型定义
│       ├── QinConfig.java      # 配置类
│       ├── QinPlugin.java      # 插件接口
│       ├── PluginContext.java  # 插件上下文
│       ├── BuildResult.java    # 构建结果
│       ├── CompileResult.java  # 编译结果
│       ├── ResolveResult.java  # 解析结果
│       └── ...                 # 其他类型
└── README.md
```

## 与 TypeScript 版本的对应关系

| TypeScript 文件 | Java 文件 |
|----------------|-----------|
| src/types.ts | com/qin/types/*.java |
| src/cli.ts | com/qin/cli/QinCli.java |
| src/core/config-loader.ts | com/qin/core/ConfigLoader.java |
| src/core/dependency-resolver.ts | com/qin/core/DependencyResolver.java |
| src/core/environment.ts | com/qin/core/EnvironmentChecker.java |
| src/core/fat-jar-builder.ts | com/qin/core/FatJarBuilder.java |
| src/core/java-runner.ts | com/qin/core/JavaRunner.java |
| src/core/plugin-system.ts | com/qin/core/PluginManager.java |
| src/core/plugin-detector.ts | com/qin/core/PluginDetector.java |
| src/core/workspace-loader.ts | com/qin/core/WorkspaceLoader.java |
| src/java/package-manager.ts | com/qin/java/PackageManager.java |
| src/java/classpath.ts | com/qin/java/ClasspathUtils.java |
| src/java/builder.ts | com/qin/java/JavaBuilder.java |
| src/commands/init.ts | com/qin/commands/InitCommand.java |
| src/commands/env.ts | com/qin/commands/EnvCommand.java |

## 编译和运行

### 依赖

需要 Gson 库用于 JSON 解析：
- com.google.code.gson:gson:2.10.1

### 编译

```bash
# 创建输出目录
mkdir -p build/classes

# 下载 Gson
curl -L -o lib/gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar

# 编译
javac -d build/classes -cp lib/gson-2.10.1.jar $(find src/java-rewrite -name "*.java")
```

### 运行

```bash
java -cp build/classes:lib/gson-2.10.1.jar com.qin.cli.QinCli help
```

## 功能对比

| 功能 | TypeScript 版本 | Java 版本 |
|------|----------------|-----------|
| 配置加载 | ✅ c12 多格式 | ✅ JSON 格式 |
| 依赖解析 | ✅ Coursier | ✅ Coursier |
| Java 编译 | ✅ | ✅ |
| Fat Jar 构建 | ✅ | ✅ |
| 热重载 | ✅ | ⏳ 待实现 |
| 插件系统 | ✅ | ✅ 基础实现 |
| Monorepo | ✅ | ✅ |
| 前端集成 | ✅ Vite | ⏳ 待实现 |
| GraalVM JS | ✅ | ⏳ 待实现 |

## 配置文件格式

Java 版本使用 `qin.config.json` 格式：

```json
{
  "name": "my-app",
  "version": "1.0.0",
  "entry": "src/Main.java",
  "dependencies": {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0"
  },
  "repositories": [
    "https://maven.aliyun.com/repository/public"
  ]
}
```

## 命令

```bash
qin init      # 初始化项目
qin run       # 编译并运行
qin build     # 构建 Fat Jar
qin dev       # 开发模式
qin compile   # 仅编译
qin clean     # 清理构建
qin sync      # 同步依赖
qin test      # 运行测试
```
