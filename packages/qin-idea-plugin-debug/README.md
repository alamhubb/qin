# Qin IDEA Plugin

IntelliJ IDEA 插件，为 Qin 项目提供完整的 IDE 支持。

## 功能特性

### 🔧 自动项目配置

| 功能 | 说明 |
|---|---|
| **自动识别 Qin 项目** | 扫描并识别所有包含 `qin.config.json` 的项目 |
| **自动执行 qin sync** | 打开项目时自动解析依赖 |
| **生成 .iml 模块文件** | 自动配置 IDEA 模块，无需手动设置 |
| **注册模块** | 自动更新 `modules.xml`，IDEA 立即识别 |

### 📁 源代码支持

| 功能 | 说明 |
|---|---|
| **蓝色源代码目录** | 自动标记 `src/main/java` 或 `src` 为源代码目录 |
| **正确的输出目录** | 从 `qin.config.json` 读取配置 |
| **排除目录** | 自动排除 `.qin`、`build`、`libs` 等目录 |

### 📦 依赖管理

| 功能 | 说明 |
|---|---|
| **自动配置依赖** | 读取 `.qin/classpath.json` 配置库依赖 |
| **源码跳转** | 点击依赖类型时跳转到源码（非 .class 文件） |
| **支持本地模块** | 识别本地项目依赖 |
| **支持 JAR 依赖** | 配置 Maven 仓库下载的 JAR 文件 |

### ☕ JDK 配置

| 功能 | 说明 |
|---|---|
| **自动配置 Project SDK** | 自动选择已注册的 JDK |
| **自动添加 JDK** | 如果没有已注册的 JDK，从 JAVA_HOME 自动添加 |

### 🛠️ 工具窗口

插件提供 **Qin** 工具窗口，支持：
- 查看项目任务和依赖树
- 手动执行 Sync、Compile、Run 命令
- 查看项目配置信息

## 安装方法

1. 打开 IDEA → **Settings** → **Plugins**
2. 点击齿轮图标 → **Install Plugin from Disk**
3. 选择 `qin-idea-plugin-debug-x.x.x.zip`
4. 重启 IDEA

## 使用方法

### 打开 Qin 项目

直接用 IDEA 打开包含 `qin.config.json` 的目录，插件会自动：
1. 检测项目
2. 执行 `qin sync`
3. 配置模块和依赖
4. 设置 Project SDK

### 手动操作

通过 **View → Tool Windows → Qin** 打开工具窗口：
- **Sync**: 重新解析依赖
- **Compile**: 编译项目
- **Run**: 运行项目

## 配置文件

### qin.config.json

```json
{
  "name": "com.example:my-project",
  "version": "1.0.0",
  "entry": "com.example.Main",
  "java": {
    "version": "21",
    "sourceDir": "src/main/java",
    "outputDir": "build/classes"
  },
  "dependencies": {
    "com.google.code.gson:gson": "2.10.1"
  }
}
```

### .qin/classpath.json

由 `qin sync` 自动生成，包含解析后的依赖路径：

```json
{
  "classpath": [
    "D:/path/to/dependency.jar",
    "D:/path/to/local-module/build/classes"
  ],
  "lastUpdated": "2026-01-05T12:00:00Z"
}
```

## 版本历史

| 版本 | 更新内容 |
|---|---|
| 0.1.12 | 自动添加 JDK（从 JAVA_HOME） |
| 0.1.11 | 依赖源码跳转 |
| 0.1.10 | BSP 内嵌逻辑，读取 qin.config.json |
| 0.1.5 | 自动创建 modules.xml |
| 0.1.4 | 自动注册模块 |
| 0.1.3 | 日志记录 |
| 0.1.2 | Java 21 兼容 |
| 0.1.1 | 初始版本 |

## 技术实现

- 使用 IntelliJ Platform SDK
- 内嵌 BSP (Build Server Protocol) 逻辑
- 与 qin-cli 共享常量和配置解析

## 相关项目

- **qin-cli**: Qin 命令行工具
- **qin-bsp-server**: BSP 服务器（可供其他 IDE 使用）
