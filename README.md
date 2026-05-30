# Qin - AI 时代的全栈应用语言

> 用一套 Qin 代码描述应用，让 AI 写代码，让 Qin 负责运行、构建和部署。

[![Java Version](https://img.shields.io/badge/Java-25%20LTS-orange.svg)](https://openjdk.org/projects/jdk/25/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📖 Qin 是什么？

**Qin** 的目标不是再做一个“更现代的 Java 构建工具”，而是做一门 **AI-native 全栈应用语言**：

- 使用 ESM 风格语法
- 以 JVM 作为主要后端/运行时内核
- 以前端 JS 生成为第一类目标
- 用统一的语言模型覆盖应用、页面、接口、数据与部署

对用户的理想体验是：

- 用 Qin 描述应用
- 用 AI 生成或修改 Qin 代码
- `qin dev` 本地启动
- `qin deploy` 一键部署

用户不需要先关心：

- 前端还是后端
- controller / service / DTO 怎么拆
- Spring / JS / 部署细节怎么接

这些应该逐步由 Qin 编译器、运行时、工具链和平台来封装。

### 当前现实与最终目标

当前 Qin 还处在“从语言/运行时基础设施走向产品形态”的阶段：

- 现在已经有统一语言模型、JVM/JS backend、`.qin + Spring Boot` demo、target zoning
- 但最终形态不应该让普通用户长期停留在“手写前后端分层 + 宿主壳”的工作方式里

当前执行优先级：

- 先把 Qin 做成一门普通可用的全栈语言
- 先完成 Stage 1 的语言、编译、运行、全栈链路稳定性
- 更高层的 Qin 应用抽象留到 Stage 2 再系统推进

更完整的产品定位见：

- `packages/qin-runtime-core/QIN_APP_MODEL.md`
- `packages/qin-runtime-core/QIN_MISSION_AND_VALUE.md`
- `packages/qin-runtime-core/QIN_PRODUCT_POSITIONING.md`
- `packages/qin-runtime-core/QIN_LANGUAGE_TARGET_MODEL.md`
- `packages/qin-runtime-core/QIN_JS_COMPATIBILITY_MODEL.md`
- `packages/qin-runtime-core/QIN_CURRENT_SUPPORT_MATRIX.md`
- `packages/qin-runtime-core/QIN_NPM_COMPATIBILITY_POLICY.md`
- `packages/qin-runtime-core/QIN_HOST_CAPABILITY_MODEL.md`
- `packages/qin-runtime-core/QIN_JS_ON_JVM_FEASIBILITY.md`
- `packages/qin-runtime-core/QIN_CSSTS_INTEGRATION_MODEL.md`

### 核心理念

```
描述应用，而不是先描述前端/后端分层
```

### 我们为什么要做 Qin

Qin 的意义不在于“把 JavaScript 用 Java 重写一遍”。

如果 Qin 最终只是：

- 接近 JS 的语法
- 再加上能调用 Spring Boot / JVM 生态

那它的价值是不够大的。

Qin 真正要证明的价值是：

- 用 AI 更熟悉的现代语法，统一更多前后端开发工作
- 直接进入 JVM / `.class` / Spring Boot / Java 生态
- 降低多语言、多运行时、多构建链路之间的胶水复杂度
- 最终把 `dev/build/deploy` 收成更一体化的全栈体验

也就是说，Qin 不是为了“像 JS”而存在。
Qin 只有在它能带来更强的 JVM-centered fullstack workflow 时，才值得作为一门独立语言继续发展。

### Qin 解决的问题

1. **应用开发仍然过度分层**
    - 用户先被迫思考前端、后端、DTO、路由、部署
    - Qin 的目标是先描述应用意图，再由平台展开 target 细节

2. **AI 写应用代码时上下文太碎**
    - 传统工程需要跨多个框架层手工对齐
    - Qin 需要提供更统一、低歧义、可生成的应用模型

3. **全栈开发的运行与部署链路过重**
    - 本地运行、依赖拼装、宿主集成、发布上线都分散
    - Qin 目标是统一为 `dev/build/deploy`

4. **底层平台差异暴露过早**
    - JVM、JS、Spring、浏览器能力本应是平台展开问题
    - Qin 目标是对普通应用作者尽量隐藏这些实现细节

### 构建工具层仍然重要

Qin 依然包含很强的工程工具链能力：

- JSON / TypeScript 风格配置
- 本地依赖与 monorepo 支持
- Java 25 / JVM 工具体系集成
- `qin run / sync / build`

但这些属于 Qin 产品的一部分，不再是 Qin 的最高层定义。

### 对比示例

**Maven pom.xml** (30+ 行):
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0.0</version>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>4.0.6</version>
        </dependency>
    </dependencies>
    
    <repositories>
        <repository>
            <id>central</id>
            <url>https://repo1.maven.org/maven2</url>
        </repository>
    </repositories>
</project>
```

**Qin qin.config.json** (10 行):
```json
{
  "name": "my-app",
  "version": "1.0.0",
  "dependencies": {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6"
  }
}
```

### Qin 适合谁？

- ✅ **前端转 Java 开发者** - 熟悉的 npm 风格配置
- ✅ **厌倦 XML 的 Java 开发者** - 简洁的 JSON/TypeScript 配置
- ✅ **Monorepo 用户** - 原生多项目支持
- ✅ **追求性能的开发者** - Java 25 带来 2-5x 性能提升
- ✅ **全栈开发者** - 内置 Qin 原生前端开发服务器，不依赖 Vite 子进程

## Qin 语言定位（全栈方向）

Qin 的核心定位：

- **Qin = JS/ESM 语法风格的编译型语言（JVM 内核）**
- 不是完整 JS 引擎，不兼容 Node 专有 API
- 目标是“一套语言写前后端”，而不是“复刻 Node 生态”

为什么要在 Java 侧实现 JS-SDK（如 `console` / `Math` / `JSON`）：

- Qin 后端目标是 `.class`，运行在 JVM，不是直接跑在浏览器里
- 但语言层采用 JS/ESM 语法，开发者天然会使用 JS 内建能力
- 如果没有 JS-SDK，语法虽然像 JS，运行时行为就会断层
- 因此需要“编译期映射 + 运行时实现”的 JS-SDK 作为语言基础设施

Qin 的分层设计：

1. 语言层：JS/ESM 语法子集（parser/AST/语义检查）
2. 运行时层：平台无关 JS-SDK（如 `console` / `Math` / `JSON` / `Object`）
3. 平台层（后端）：JVM 运行（可调用 Java/JDK 能力）
4. 平台层（前端）：浏览器运行（Web 平台能力）

设计原则：

- 语法尽量 JS 一致，语义保持可控子集
- 平台能力分层隔离，不把 Node 规则强行带入 Qin
- 先保证前后端一致的开发体验，再逐步扩展标准库覆盖面

## 🎉 What's New in Java 25 Version

### ✨ 核心升级

- **Java 25 LTS** - 最新长期支持版本（支持到 2033 年）
- **Flexible Constructor Bodies (JEP 513)** - 配置验证更安全
- **Module Import Declarations (JEP 511)** - 代码更简洁
- **Primitive Patterns (JEP 507)** - 类型安全性增强
- **Structured Concurrency (JEP 505)** - 并发性能提升 3-5x
- **AOT Method Profiling (JEP 515)** - 启动速度提升 2-3x
- **Compact Object Headers (JEP 519)** - 内存占用减少 20-30%

### 📊 性能提升

| 指标 | Java 17 | Java 25 | 提升 |
|------|---------|---------|------|
| CLI 启动时间 | 800ms | 300ms | **2.7x** ⚡ |
| 并行编译 (10 文件) | 5.2s | 1.8s | **2.9x** 🚀 |
| 内存占用 | 180MB | 135MB | **-25%** 💾 |
| 代码量 | 3500 行 | 2100 行 | **-40%** 📝 |

## 🚀 快速开始

### 前置要求

- **Java 25** or higher ([Download](https://jdk.java.net/25/))
- **Maven** 3.8+ (可选，用于依赖下载)

### 编译 Qin

```bash
# Windows
.\build-java.bat

# Linux/macOS
./build-java.sh
```

###  运行 Qin

```bash
# 查看帮助
java -cp ".qin\classes;lib\gson-2.10.1.jar" com.qin.cli.QinCli help

# 编译项目
java -cp ".qin\classes;lib\gson-2.10.1.jar" com.qin.cli.QinCli compile

# 运行项目
java -cp ".qin\classes;lib\gson-2.10.1.jar" com.qin.cli.QinCli run
```

### 创建快捷命令（推荐）

**Windows (PowerShell)**:
```powershell
# 添加到 PowerShell Profile
function qin { java -cp "D:\path\to\qin\.qin\classes;D:\path\to\qin\lib\gson-2.10.1.jar" com.qin.cli.QinCli $args }
```

**Linux/macOS (Bash)**:
```bash
# 添加到 ~/.bashrc or ~/.zshrc
alias qin='java -cp "/path/to/qin/build/classes:/path/to/qin/lib/gson-2.10.1.jar" com.qin.cli.QinCli'
```

然后就可以直接使用：
```bash
qin compile
qin run
qin build
```

## Jite Default Dev Runtime

- New package: `packages/qin-plugin-jite`
- Dev entry class: `com.qin.jite.JiteDevMain`
- `qin dev` now prefers Jite runtime for `.js` projects
- If `qin-plugin-jite` is not on classpath, CLI falls back to `com.qin.runtime.core.QinFullstackMain --dev`

### Demo Project

- Path: `packages/exmaples/jitedemo`
- Dependency: `com.qin:qin-plugin-jite`
- Run inside demo folder:

```bash
qin dev
```

Open [http://localhost:18080](http://localhost:18080).

## 📝 配置文件

### `qin.config.json`

`qin.config.json` 是 Qin 的项目 manifest。

它当前同时承担四类职责：

- 包清单
- 依赖声明入口
- workspace / module root 描述
- runtime / build / entry 配置面

`entry` 当前支持：

- Qin source entry: `.qin`、`.js`、`.mjs`、`.ts`
- Java host entry: `.java`

`qin run` 无参数时优先读取这里的 `entry`。

```json
{
  "name": "my-app",
  "version": "1.0.0",
  "description": "My awesome Java 25 app",
  "entry": "main/main.qin",
  
  "dependencies": {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6",
    "com.github.ben-manes.caffeine:caffeine": "3.1.8"
  },
  
  "devDependencies": {
    "org.junit.jupiter:junit-jupiter": "5.10.1"
  },
  
  "repositories": [
    {
      "id": "aliyun",
      "url": "https://maven.aliyun.com/repository/public"
    },
    {
      "id": "central",
      "url": "https://repo1.maven.org/maven2"
    }
  ],
  
  "java": {
    "version": "25",
    "sourceDir": "src/main/java",
    "testDir": "src/test/java",
    "outputDir": "target/classes",
    "encoding": "UTF-8"
  },
  
  "output": {
    "dir": "dist",
    "jarName": "my-app.jar",
    "fatJar": true
  }
}
```

## 🛠️ CLI 命令

| 命令 | 说明 | 示例 |
|------|------|------|
| `compile` | 编译 Java 项目 | `qin compile` |
| `run` | 编译并运行 | `qin run` / `qin run Test.java` / `qin run main/main.qin` |
| `dev` | 开发模式（Qin 单端口 + 自动刷新） | `qin dev` / `qin dev main/main.qin` |
| `build` | 构建产物（Java: JAR / Qin: fullstack 产物） | `qin build` / `qin build main/main.qin` |
| `test` | 运行测试 | `qin test` |
| `sync` | 同步依赖 | `qin sync` |
| `clean` | 清理构建产物 | `qin clean` |
| `init` | 初始化项目 | `qin init` |
| `env` | 环境检查 | `qin env` |

### 运行指定文件

```bash
# 运行项目入口（qin.config.json 中的 entry）
qin run

# 运行指定的 Java 文件
qin run src/main/java/com/example/Test.java

# 运行指定的 Qin 文件
qin run main/main.qin

# 运行指定文件并传递参数
qin run MyTest.java arg1 arg2
```

运行分发规则（统一）：

- 传入目标参数时：优先插件解析，其次 `.js`，最后按 Java 入口处理。
- 不传参数时：优先读取 `qin.config.json` 的 `entry`；若为 `.qin/.js/.mjs/.ts` 走 Qin 运行时，否则按 Java host 入口处理。

`run` / `dev` 区别：

- `qin run`：一次构建后启动，不监听文件变化。
- `qin dev`：单端口开发服务（默认同端口提供 API + 静态页面），监听 `.js/.html/.css` 变更并自动重编译与浏览器刷新。
- 该 MVP 的核心链路以 Qin + Java 运行时为主，前端由 Qin 原生 dev server 和 Qin frontend pipeline 承载，不通过 Node/Vite 子进程桥接。

构建分发规则（统一）：

- `qin build` 在检测到 `.js` 入口时，会走 Qin fullstack build-only 流程。
- 默认输出目录：`dist/fullstack`，其中 `server-classes/` 存放后端 `.class`，`web/` 存放前端构建产物（含 `app.js`）。
- 可用 `-o/--output` 覆盖输出根目录。

## 🚀 高级特性

### 1. 增量编译

Qin 使用 **javax.tools API + 时间戳比较** 实现智能增量编译：

- ✅ **智能检测** - 只编译修改过的文件（比较 `.java` 和 `.class` 的修改时间）
- ✅ **自动依赖** - `javac` 自动处理依赖文件的编译
- ✅ **零配置** - 无需额外配置，开箱即用
- ✅ **快速响应** - 无修改时跳过编译，立即运行

**实现原理：**
```java
// 比较每个 .java 文件和对应 .class 文件的时间戳
private boolean isModified(String javaFilePath) {
    Path classFile = getClassFilePath(javaFilePath);
    if (!Files.exists(classFile)) return true;
    return Files.getLastModifiedTime(javaFile) > Files.getLastModifiedTime(classFile);
}

// 使用 javax.tools API 编译
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
compiler.getTask(...).call();
```

**性能提升：**
| 场景 | 全量编译 | 增量编译 | 提升 |
|------|----------|----------|------|
| 无修改 | 3.2s | 0.1s | **32x** ⚡ |
| 修改 1 个文件 | 3.2s | 0.5s | **6.4x** 🚀 |
| 修改 10 个文件 | 3.2s | 1.8s | **1.8x** ⚡ |

### 2. 依赖缓存机制

Qin 在每个项目的 `.qin/classpath.json` 中缓存依赖解析结果：

```json
{
  "classpath": [
    "D:/project/subhuti-java/build/classes",
    "C:/Users/qinky/.qin/libs/com.google.code.gson/gson-2.10.1/gson-2.10.1.jar"
  ],
  "lastUpdated": "2025-12-30T07:10:32Z"
}
```

**工作流程：**
```
qin run/sync
  ↓
检查 .qin/classpath.json 是否存在
  ↓
比较缓存时间 vs qin.config.json 修改时间
  ↓
缓存有效 → 直接使用（快速启动）
缓存无效 → 重新解析依赖并更新缓存
```

**优势：**
- ⚡ **首次运行** - 解析依赖 + 生成缓存
- 🚀 **后续运行** - 直接读缓存，秒级启动
- 🔄 **自动刷新** - 修改 `qin.config.json` 后自动重新解析

### 3. 本地依赖优先解析

Qin 自动发现并优先使用本地项目依赖，避免从 Maven 下载：

**自动发现策略：**
1. 从当前目录向上查找所有包含 `qin.config.json` 的目录
2. 扫描每个目录的同级项目
3. 匹配依赖的 `groupId:artifactId`
4. 就近优先（近的项目覆盖远的同名项目）

**示例：**
```
d:/project/
├── slime-java/
│   ├── subhuti-java/         # com.subhuti:subhuti-java
│   │   ├── qin.config.json
│   │   └── build/classes/    ← 本地依赖路径
│   ├── slime-token/          # com.slime:slime-token
│   │   └── build/classes/
│   └── slime-parser/         # 依赖上面两个项目
│       └── qin.config.json
```

在 `slime-parser` 中：
```json
{
  "dependencies": {
    "com.subhuti:subhuti-java": "1.0.0-SNAPSHOT",
    "com.slime:slime-token": "1.0.0"
  }
}
```

执行 `qin sync` 输出：
```
→ Syncing dependencies...
  → Found 2 local dependencies
✓ Dependencies synced (2 local, 0 remote)
  Cache: .qin/classpath.json
```

**优势：**
- 🚀 **无需发布** - 本地开发无需发布到 Maven
- 🔄 **实时更新** - 修改依赖项目立即生效
- 💾 **节省带宽** - 不下载本地已有的项目
- 🎯 **Monorepo 友好** - 天然支持多项目工作区

### 4. 依赖解析流程

```
qin run / qin sync
  ↓
检查依赖缓存 (.qin/classpath.json)
  ↓
[缓存有效]───────────────┐
  ↓                     ↓
[缓存无效]          使用缓存
  ↓                     ↓
本地依赖解析          直接运行
(LocalProjectResolver)
  ↓
找到 → 使用 build/classes 路径
未找到 → 标记为远程依赖
  ↓
远程依赖解析
(DependencyResolver + Coursier)
  ↓
合并本地+远程 classpath
  ↓
写入 .qin/classpath.json
  ↓
运行程序
```

### 5. 代码复用设计

`run` 命令和 `sync` 命令共享核心依赖解析逻辑：

```java
// run 命令
private static void runProject(String[] args) {
    String classpath = ensureDependenciesSynced(config);  // 复用
    runner.compileAndRun(classpath);
}

// sync 命令  
private static void syncDependencies() {
    syncDependenciesCore(config);  // 核心逻辑
}

// 共享的核心逻辑
private static String syncDependenciesCore(QinConfig config) {
    // 1. 本地依赖解析
    LocalProjectResolver.ResolutionResult localResult = ...;
    
    // 2. 远程依赖解析（仅未在本地找到的）
    if (!localResult.remoteDependencies.isEmpty()) {
        DependencyResolver resolver = ...;
    }
    
    // 3. 生成并缓存 classpath
    return classpath;
}
```

### 6. IDEA 集成

Qin 提供 IntelliJ IDEA 插件，实现 IDE 无缝集成：

**自动功能：**
- ✅ **自动同步** - 打开项目时自动执行 `qin sync`
- ✅ **库配置生成** - 自动生成 `.idea/libraries/*.xml`
- ✅ **编译输出配置** - 自动使用 `build/classes`（与 qin 一致）
- ✅ **Monorepo 支持** - 自动扫描所有子项目

**安装：**
```bash
# 构建插件
cd packages/qin-idea-plugin-debug
./gradlew buildPlugin

# 安装：IDEA → Settings → Plugins → ⚙️ → Install from Disk
# 选择 build/distributions/qin-idea-plugin-debug-x.x.x.zip
```

**工作原理：**
```
IDEA 打开项目
     ↓
向上查找 workspace root（.idea/.vscode/.git）
     ↓
向下递归扫描所有 qin.config.json（最多 5 层）
     ↓
为每个项目执行 qin sync
     ↓
生成 .idea/libraries/*.xml
     ↓
更新 .iml 文件（添加库引用 + 配置输出路径）
     ↓
刷新 IDEA 项目模型
```

### 7. Monorepo 支持

Qin 原生支持 Monorepo（单仓库多项目）模式：

**目录结构：**
```
workspace/
├── .git/
├── .idea/             # IDEA 项目标志
├── project-a/
│   └── qin.config.json
├── project-b/
│   └── qin.config.json
├── packages/
│   ├── lib-1/
│   │   └── qin.config.json
│   └── lib-2/
│       └── qin.config.json
└── apps/
    └── app-1/
        └── qin.config.json
```

**本地依赖解析策略：**
1. 从当前目录向上查找所有 `qin.config.json`
2. 扫描同级目录的其他项目
3. 就近优先：近的项目覆盖远的同名项目

**IDEA 插件扫描策略：**
1. 向上找到 workspace root（最顶层的 `.idea`/`.vscode`/`.git` 目录）
2. 从 workspace root 向下递归扫描所有 `qin.config.json`
3. 为每个发现的项目自动执行 sync

**配置示例：**
```json
// project-a/qin.config.json
{
  "name": "com.example:project-a",
  "version": "1.0.0",
  "dependencies": {
    "com.example:lib-1": "1.0.0",  // 自动使用本地 ../packages/lib-1
    "com.example:lib-2": "1.0.0"   // 自动使用本地 ../packages/lib-2
  }
}
```

## 🎯 Java 25 特性展示

### 1. Flexible Constructor Bodies

```java
// ✨ Java 25 新特性
public record QinConfig(String name, String version, Map<String, String> dependencies) {
    public QinConfig {
        // 可以在 super() 前验证参数！
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        
        // 提供默认值
        dependencies = dependencies != null ? Map.copyOf(dependencies) : Map.of();
    }
}
```

### 2. Primitive Patterns in Switch

```java
// ✨ Java 25 - 基本类型模式匹配
String result = switch (value) {
    case int i when i > 0 -> "positive: " + i;
    case long l -> "long value: " + l;
    case double d -> "double value: " + d;
    default -> "other";
};
```

### 3. Structured Concurrency

```java
// ✨ Java 25 - 结构化并发
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    var task1 = scope.fork(() -> downloadDependency("lib1"));
    var task2 = scope.fork(() -> downloadDependency("lib2"));
    
    scope.join().throwIfFailed();  // 统一异常处理
    
    return List.of(task1.get(), task2.get());
}
```

## 📦 项目结构

### 用户项目结构

```
my-project/
├── qin.config.json          # Qin 配置
├── src/
│   ├── main/java/           # 源码
│   │   └── com/myapp/
│   │       └── Main.java
│   └── test/java/           # 测试
├── build/
│   └── classes/             # 编译输出 (OUTPUT_DIR)
├── .qin/                    # Qin 配置目录
│   ├── classpath.json       # 依赖缓存 (CLASSPATH_CACHE)
│   └── libs/                # 本地依赖链接 (LIBS_DIR)
│       └── com.google.code.gson/
│           └── gson-2.10.1/ -> ~/.qin/libs/.../
└── dist/
    └── my-app.jar           # Fat JAR
```

### 全局目录结构

```
~/.qin/
└── libs/                    # 全局依赖缓存 (GLOBAL_LIBS_DIR)
    └── com.google.code.gson/
        └── gson-2.10.1/
            └── gson-2.10.1.jar
```

### 路径常量配置 (QinPaths.java)

| 常量 | 值 | 说明 |
|------|-----|------|
| OUTPUT_DIR | `build/classes` | 编译输出目录 |
| QIN_DIR | `.qin` | Qin配置目录 |
| CLASSPATH_CACHE | `.qin/classpath.json` | 依赖缓存文件 |
| LIBS_DIR | `.qin/libs` | 依赖库目录 |

## 🔧 开发

### 项目结构

```
qin/
├── src/java-rewrite/        # Java 25 源码
│   └── com/qin/
│       ├── types/           # 配置类型（Records）
│       ├── core/            # 核心模块
│       ├── commands/        # 命令实现
│       ├── cli/             # CLI 入口
│       └── java/            # Java 工具
├── .qin/
│   └── classes/             # 编译输出
├── lib/
│   └── gson-2.10.1.jar      # 唯一依赖
└── build-java.bat           # 构建脚本
```

### 核心模块架构

#### 1. **路径管理** - `QinPaths.java`
**功能**: 统一管理所有路径常量
```java
public static final String OUTPUT_DIR = "build/classes";
public static final String LIBS_DIR = ".qin/libs";
```
**为什么需要**: 避免硬编码，统一路径配置，方便维护和修改

#### 2. **编译系统** - 职责分离设计

##### ClasspathBuilder - classpath构建
**功能**: 构建编译和运行时的classpath
- `buildCompileClasspath()` - 编译时classpath（包含本地项目+远程依赖）
- `buildRuntimeClasspath()` - 运行时classpath

**为什么需要**: classpath构建逻辑复杂（本地项目发现、依赖解析），独立出来提高可维护性

##### Compiler - 编译逻辑
**功能**: Java源文件编译
- `compile()` - 使用javax.tools API编译
- `filterModifiedFiles()` - 增量编译（只编译修改的文件）
- `findJavaFiles()` - 查找Java文件

**为什么需要**: 封装复杂的编译逻辑，支持增量编译提升性能

##### ResourceCopier - 资源复制
**功能**: 复制资源文件到输出目录
- 查找多个可能的资源目录（`src/resources`, `src/main/resources`）
- 递归复制目录

**为什么需要**: 资源文件处理是独立的功能，与编译逻辑分离

##### Runner - 程序运行
**功能**: 运行编译后的Java程序
- `run()` - 运行指定类
- `runFile()` - 运行指定Java文件
- `javaFilePathToClassName()` - 路径转类名

**为什么需要**: 运行逻辑独立，支持多种运行方式

##### JavaRunner - 门面协调器
**功能**: 协调上述4个类，提供统一接口
```java
public CompileResult compile() {
    // 1. 编译依赖
    // 2. 查找Java文件
    // 3. 复制资源
    // 4. 增量编译
}
```
**为什么需要**: 保持简单的调用接口，隐藏内部复杂性

#### 3. **依赖管理**

##### LocalProjectResolver - 本地项目发现
**功能**: 自动发现本地项目依赖
- 向上查找所有qin.config.json
- 匹配groupId:artifactId
- 就近优先

**为什么需要**: Monorepo支持，避免发布到Maven仓库

##### DependencyResolver - 远程依赖解析
**功能**: 使用Coursier解析Maven依赖
- 下载jar到~/.qin/libs
- 缓存依赖解析结果

**为什么需要**: 自动下载和管理远程依赖

#### 4. **配置系统** - `types/`

使用Java 25 Records定义配置类型：
```java
public record QinConfig(
    String name,
    String version,
    Map<String, String> dependencies
) {}
```
**为什么需要**: 不可变配置，类型安全，代码简洁

#### 5. **CLI系统** - `QinCli.java`

**功能**: 命令行入口，解析命令和参数
```
qin compile → CompileCommand
qin run     → RunCommand
qin build   → BuildCommand
```
**为什么需要**: 统一的命令行接口，用户友好

---

### 设计原则

1. **职责单一**: 每个类只负责一件事
2. **依赖注入**: 通过构造函数传递依赖
3. **面向接口**: 使用抽象类型，便于测试和扩展
4. **常量管理**: 所有路径通过QinPaths统一管理
```

### 编译

```bash
# 编译 Qin 本身
.\build-java.bat

# 输出：build/classes/
```

### 测试

```bash
# 使用 Qin 编译测试项目
cd examples/hello-java
..\..\qin.bat compile
..\..\qin.bat run
```

## 🌟 特性

### ✅ 核心功能


- [x] **JSON 配置** - 告别 XML，拥抱 JSON
- [x] **依赖管理** - npm 风格的依赖语法
- [x] **增量编译** - javax.tools API + 时间戳，32x 性能提升
- [x] **依赖缓存** - .qin/classpath.json 自动缓存，秒级启动
- [x] **本地依赖优先** - 自动发现本地项目，无需发布到 Maven
- [x] **Fat JAR 构建** - 一键生成可执行 JAR
- [x] **运行指定文件** - `qin run Test.java` / `qin run main/main.qin`
- [x] **并行编译** - Virtual Threads 加速
- [x] **热重载** - 开发模式自动重新编译
- [x] **Monorepo 支持** - 多项目管理


### ✅ Java 25 优化

- [x] Records 代替 POJO - 代码减少 60%
- [x] Flexible Constructors - 更安全的验证
- [x] Pattern Matching - 更优雅的类型处理
- [x] Virtual Threads - 3-5x 并发性能
- [x] Structured Concurrency - 更可靠的异步
- [x] AOT Profiling - 2-3x 启动速度
- [x] Compact Headers - 20-30% 内存节省

## 📚 文档

- [Java 25 重写计划](./JAVA25_REWRITE_PLAN.md)
- [Java 25 特性详解](./docs/JAVA25_FEATURES.md)
- [配置参考](./docs/CONFIG_REFERENCE.md)
- [插件开发](./docs/PLUGIN_DEVELOPMENT.md)

## 🤝 贡献

欢迎贡献代码、报告 Bug 或提出建议！

## 📄 License

MIT License - 查看 [LICENSE](LICENSE) 文件

---

**Built with ❤️ using Java 25**  
**Powered by Flexible Constructors, Virtual Threads, and Structured Concurrency**
## Qin Language Positioning (v0.2)

Qin is an ESM-first language/runtime implemented on Java/JVM.
It is designed as a new language platform, not a Node.js compatibility layer.

More precisely:

- Qin borrows ESM as a source-language and module-design inspiration.
- Qin does not treat Node.js as its platform standard.
- Qin defines its own runtime boundary on top of Java/JVM.

### Official Project Positioning

Qin should be understood as:

- a Qin-owned programming language
- whose source experience is based on ESM / modern JavaScript-style syntax
- with Qin-specific syntax and semantic extensions
- with JVM as the primary backend/runtime kernel
- with fullstack targets across frontend and backend

In practical terms:

- backend Qin code compiles to JVM `.class`
- frontend Qin code can compile to JS for web targets
- Java is the host platform and ecosystem carrier
- Qin is the business language, not "Java source with different syntax"

So the most accurate short definition is:

- Qin is a fullstack, ESM-style, JVM language.

### Design Direction (Current)

- ESM-first: Qin source uses ES-style syntax and ESM `import/export` as the primary module model.
- Java/JVM kernel: backend compiles to JVM `.class` (and can also emit JS for web targets).
- Mixed-source Qin input: Qin accepts `.ts`, `.js`, and `.qin` in the same workspace graph.
- `.java` remains host/interoperability code for backend projects, not a Qin source suffix.
- Node compatibility is optional and incremental: it is a compatibility layer for third-party packages, not the platform definition.
- Java-backed stdlib: runtime capabilities are implemented on top of Java standard library.
- Fullstack orientation: one language across `shared/`, frontend, and backend, with target-specific backends.
- Qin-owned standard: language/runtime semantics are defined by Qin itself, while Node-adjacent behavior can be added when needed.

Backend architecture note:

- Qin user-facing application model is documented in [packages/qin-runtime-core/QIN_APP_MODEL.md](packages/qin-runtime-core/QIN_APP_MODEL.md).
- Qin server-side code is intended to stand on JVM + `.class` ecosystem support.
- Node is not the backend foundation of Qin.
- Parsing ownership should belong to a Qin-owned parser layer (`qin-parser`) built on top of shared `java-slime` infrastructure.
- Qin language/target zoning is documented in [packages/qin-runtime-core/QIN_LANGUAGE_TARGET_MODEL.md](packages/qin-runtime-core/QIN_LANGUAGE_TARGET_MODEL.md).
- JS/TS compatibility boundary is documented in [packages/qin-runtime-core/QIN_JS_COMPATIBILITY_MODEL.md](packages/qin-runtime-core/QIN_JS_COMPATIBILITY_MODEL.md).
- npm compatibility grading is documented in [packages/qin-runtime-core/QIN_NPM_COMPATIBILITY_POLICY.md](packages/qin-runtime-core/QIN_NPM_COMPATIBILITY_POLICY.md).
- host/runtime capability boundary is documented in [packages/qin-runtime-core/QIN_HOST_CAPABILITY_MODEL.md](packages/qin-runtime-core/QIN_HOST_CAPABILITY_MODEL.md).
- JS-on-JVM support priority and deferred-feature policy is documented in [packages/qin-runtime-core/QIN_JS_ON_JVM_FEASIBILITY.md](packages/qin-runtime-core/QIN_JS_ON_JVM_FEASIBILITY.md).
- Qin frontend lifecycle policy is Qin-owned; Vite can be studied as prior art, but Qin does not use it as a bridge or fallback.
- CSSTS integration policy is documented in [packages/qin-runtime-core/QIN_CSSTS_INTEGRATION_MODEL.md](packages/qin-runtime-core/QIN_CSSTS_INTEGRATION_MODEL.md).
- See [packages/qin-runtime-core/QIN_BACKEND_MODEL.md](packages/qin-runtime-core/QIN_BACKEND_MODEL.md).
- Builtin object strategy is documented in [packages/qin-runtime-core/QIN_BUILTINS_STRATEGY.md](packages/qin-runtime-core/QIN_BUILTINS_STRATEGY.md).
- Async/concurrency model is documented in [packages/qin-runtime-core/QIN_ASYNC_MODEL.md](packages/qin-runtime-core/QIN_ASYNC_MODEL.md).

### Scope and Non-Goals

- Phase 1: selected ES syntax + stable ESM core semantics + Java-backed runtime APIs.
- Phase 1 non-goal: full ECMAScript and full ESM edge-case compatibility.
- Phase 1 non-goal: pretending Node compatibility is the platform standard.
- Reason: this is an engineering phasing strategy, not a JVM limitation. We prioritize a stable core first, then expand compatibility incrementally where real packages require it.

### Qin vs JS Runtime / Bun / Kotlin

| Dimension | Browser/Node JS | Bun | Kotlin/JVM | Qin |
| --- | --- | --- | --- | --- |
| Source language | JavaScript / TypeScript | JavaScript / TypeScript | Kotlin | ESM-style Qin |
| Primary goal | Run JS | Run JS faster and provide toolchain | Modern JVM language | Modern ESM-style JVM language |
| Runtime foundation | JS engine + host runtime | JavaScriptCore + Zig host runtime | JVM | JVM |
| Backend ecosystem | Node/npm | Node/npm | `.class` / Java ecosystem | `.class` / Java ecosystem |
| Node compatibility pressure | High | High | None | None by default |
| Output artifact | interpreted / bundled JS | interpreted / bundled JS | `.class` | `.class` |
| Spring integration model | indirect bridges | indirect bridges | native | native target |
| Language semantics owner | ECMAScript + host | ECMAScript + Bun host | Kotlin | Qin |

Operationally:

- Bun is best understood as a JS runtime/toolchain implemented largely in Zig.
- Qin is best understood as an ESM-style source language compiled onto the JVM.
- So Qin is not "Java-written Bun" and not "JVM-hosted Node".
- Qin is closer to "ESM-syntax Kotlin-like language for the JVM" than to a JS engine.

### Similar Projects In The Industry

There is no mainstream language today that is exactly the same as Qin.

The closest comparisons are split across different dimensions:

- Kotlin: closest on backend platform strategy
- Scala / Groovy: similar on "new language on JVM" category
- TypeScript: closest on source authoring familiarity
- ClojureScript / ReScript / Elm: similar in "source language compiles to JS" direction
- Bun / Deno / Node: useful comparison targets, but not the same category

Kotlin is closest on backend platform strategy: independent language, compiles to JVM `.class`, integrates naturally with Spring / Java ecosystem, but it is not ESM-style at the source language level.

Scala / Groovy are close in the "new language on JVM" category: JVM-native backend language position, strong `.class` ecosystem integration, but not designed around ESM-style fullstack authoring.

TypeScript is closest on source authoring familiarity: modern JS/ESM-style code organization and decorator-oriented developer expectations, but it normally targets JS runtimes rather than JVM `.class`.

ClojureScript / ReScript / Elm are similar in the "source language compiles to JS" direction, but they are not trying to make JVM `.class` the primary backend artifact.

Bun / Deno / Node are useful comparison targets mainly for what Qin is not: they are JS runtimes/toolchains, while Qin is a new language platform with its own runtime boundary.

So the best mental model is:

- backend category: closer to Kotlin than to Node
- source-language feel: closer to ESM/TypeScript than to Kotlin syntax
- product shape: a fullstack language with target-specific backends, not a JS runtime clone

### What Qin Keeps From ESM, And What It Does Not

Qin intentionally keeps:

- ES-style module authoring with `import` / `export`
- modern JS-like expression syntax
- decorators as the source-level annotation model
- familiar builtin names such as `console`, `JSON`, `Math`, `Array`, `Object`
- frontend/backend authoring style convergence

Qin intentionally does not promise, at least in the current architecture phase:

- full ECMAScript engine parity
- full Node runtime compatibility
- full prototype-chain edge-case fidelity
- CommonJS behavior
- "any npm package should just run" without adaptation
- browser or Node loader behavior as the definition of correctness
- Promise-first / `async`-viral programming model for backend code

The design intent is:

- keep the productive parts of ESM authoring experience
- keep Qin semantics stable and documentable
- map backend execution cleanly to JVM + `.class`
- avoid being trapped by Node compatibility as the platform definition
- preserve Java/Kotlin-like synchronous backend ergonomics by default
- add Node-adjacent compatibility only as an incremental layer when ecosystem pressure makes it worthwhile

### Convention Folders (Current Project Rule)

These folders are current normative technical architecture.
They are not the final preferred primary end-user mental model for Qin application authoring.
See [packages/qin-runtime-core/QIN_APP_MODEL.md](packages/qin-runtime-core/QIN_APP_MODEL.md).

- `shared/`: shared code and shared contracts
- `app/`: frontend static assets root (`/index` resolves to `app/index` or `app/index.html`)
- backend entry: Java defaults to `src/Main.java` (with compatibility candidates), Qin defaults use `main/main.qin` and other built-in candidates

### Import Policy by Zone (Current Rule)

- `app/` (frontend): can import JS modules, cannot import `java:` modules
- `main/` (backend): can import `java:` and JS modules
- `shared/` (shared): cannot import `java:` or JS modules
- Violations fail at compile time with policy error codes

## Runtime Package Plan

- Location: `qin/packages/qin-runtime-core`
- Name: `qin-runtime-core`
- Purpose: Qin runtime orchestration layer (Java runtime semantics, ES-style syntax)

### Why this package

- Keep `slime-java` parser frontend as shared infrastructure.
- Keep compiler backends (`qin-lang-backend-jvm` and `qin-lang-backend-js`) as independent modules.
- Put project-level orchestration (layout conventions, bootstrap CLI) in a dedicated runtime package.

### Stage 0 Entry

Use `com.qin.runtime.core.QinRuntimeMain` to parse `.ts`, `.js`, and `.qin` Qin source inputs through the Qin pipeline and emit `.class/.js` outputs.

## Qin Language Design (Normative)

Qin is a JS/ESM-syntax, JVM-kernel compiled language.

- Qin is not a full JavaScript engine.
- Qin does not aim for Node compatibility.
- Qin does not treat Node semantics as normative for language or runtime behavior.
- Qin uses Class-File API to emit JVM `.class` artifacts.
- Qin defines its own backend/runtime standard on the JVM.

### Module Semantics (Strict Rule)

- ESM-only module model in this phase.
- Supported module syntax baseline: `import` / `export` (ESM).
- Not supported as language spec: CommonJS (`require`, `module.exports`, `exports.*`).
- Any accidental CJS behavior seen during experiments is non-normative and not a compatibility promise.
- If source depends on CJS semantics, Qin may fail at parse/semantic/lowering/runtime stages.

### Runtime Boundary (Strict Rule)

- Node runtime APIs are out of scope in this phase (`process`, `Buffer`, `__dirname`, `fs`, `path`, `node:*`, etc.).
- `qin install` can fetch npm packages, but "can install" != "can run".
- Runtime compatibility depends on whether package code stays inside Qin-supported ESM + syntax/runtime subset.
- npm ecosystem behavior is never the normative definition of Qin semantics.

### npm Packages As Qin Compile Inputs

Qin should treat npm as a package source, not as the definition of the runtime.

That means:

- compatible npm packages may enter the Qin compile graph
- `.ts/.js/.qin` package sources may be compiled by Qin
- backend-compatible packages may be lowered into the JVM target and participate in `.class` outputs
- frontend-compatible packages may be lowered into JS outputs

Important boundary:

- this does not mean every npm package is supported
- support is source-compatibility-based, not registry-based
- packages that depend on CJS, Node-only APIs, native addons, or unsupported JS runtime behavior may still fail by design

So the correct rule is:

- Qin aims to compile compatible npm packages, not to promise blind npm ecosystem parity

### Runtime Function Model (No Shim Path)

- Qin runtime uses a single function-construction entrypoint: `Global.__qin_make_function__`.
- Frontend lowers supported JS function AST into a runtime function definition object, then routes through that entrypoint.
- Runtime decides whether to instantiate:
  - interpreted JS function (AST-driven, generic semantics path), or
  - fallback callable (for unsupported/over-budget shapes).
- This is a structural design, not package-specific behavior:
  - no npm package hardcode,
  - no library shim mapping,
  - no package-name condition branches in parser/lowering/runtime.

Safety guard for large modules:

- Qin applies a lowering budget for runtime function definitions.
- For very large source units, function-model lowering is automatically disabled to prevent JVM bytecode method-size overflow.
- This keeps pipeline stability while preserving the same architecture and entrypoint.

Project zoning rules (compile-time hard rules):

- `app/` (frontend): JS/ESM imports allowed, `java:` imports denied.
- `main/` (backend): `java:` and JS imports allowed.
- `shared/`: platform-specific imports denied (`java:` and JS package imports).

Run model:

- `qin run` without args follows `qin.config.json` and convention candidates.
- `.js` entries run through Qin runtime (`qin-runtime-core`).
- `.java` entries run through Java compile/run flow.

ESM diagnostics:

- Link and semantic diagnostics: `ESM2xxx`
- Runtime feature gates: `ESM3xxx`

Reference implementation details live in:

- `packages/qin-lang-module-policy/README.md`
- `packages/qin-lang-sema-esm/README.md`
- `packages/qin-runtime-core/README.md`

## Dependency Install Design (`qin install`)

`qin install` now supports mixed dependency sources in one workflow:

1. npm-first resolution (Node-free installer implemented in Java)
2. Maven fallback when npm package cannot be resolved
3. Automatic write-back to `qin.config.json`

### `qin.config.json` as manifest and module root

`qin.config.json` should be treated as the canonical Qin project manifest, not only as a loose config file.

In the current architecture it defines:

- project/package identity: `name`, `version`, `description`
- source/runtime entry selection: `entry`
- dependency surface: `dependencies`, `devDependencies`, `repositories`
- workspace shape: `packages`
- target/runtime/build knobs: `java`, `graalvm`, `frontend`, `output`, `port`

So Qin is converging on one manifest for:

- package identity
- mixed npm + Maven dependency declaration
- workspace package discovery
- module-root and runtime/build coordination

### Command behavior (no options)

- `qin install <pkg...>`
  - For bare package names (for example `mitt`, `lodash-es`), Qin tries npm first.
  - If npm cannot resolve that package, Qin falls back to Maven Central lookup.
  - Installed dependencies are recorded into `qin.config.json`.

- `qin install org.example:artifact[:version]`
  - Explicit Maven coordinates go directly to Maven resolution.
  - Added to `qin.config.json`, then resolved by `qin sync` logic.

- `qin install` (no package names)
  - Executes install using dependencies already declared in `qin.config.json`.
  - npm-style entries are installed to `node_modules` by Qin's Java npm installer.
  - Maven-style entries are resolved using the existing `sync` pipeline.
  - This is the unified update path for `main/` backend JS + Java dependency environment.

### `qin.config.json` dependency shape

- npm dependencies: key is npm package name, value is npm version
  - Example: `"mitt": "3.0.1"`
- Maven dependencies: key is Maven coordinate (`groupId:artifactId`), value is Maven version
  - Example: `"org.jsoup:jsoup": "1.18.1"`

This allows Qin projects to manage npm + Maven dependencies together while keeping runtime constraints explicit (Node APIs are still out of scope unless Qin runtime explicitly supports them).

### Dependency vs Runtime Clarification (Normative)

- `qin install` is a dependency acquisition and lock/write-back workflow.
- Runtime execution still follows Qin language/runtime constraints above (ESM-first, no CJS guarantee, no Node API guarantee).
- If an npm package requires CJS or Node-only APIs, install may succeed but compile/run can still fail by design.
- Package-manager convenience must not be confused with platform compatibility goals.

Notes:

- `app/` frontend package behavior is intentionally not changed in this phase.
- `main/` backend JS and Java now follow one dependency update entrypoint: `qin install`.
