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

### 🚀 设计理念

**异步优先 + 并行执行**：插件在设计上遵循"不阻塞用户"原则。

```
项目打开时的执行流程：

┌─────────────────────────────────────────────────────────────┐
│ IDEA 启动                                                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─ 主线程 ─────────────────────────────────────────────┐   │
│  │  创建工具窗口 UI（立即完成）                          │   │
│  └──────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─ 后台线程池 ─────────────────────────────────────────┐   │
│  │                                                       │   │
│  │  ┌─────────────────┐   ┌─────────────────────────┐   │   │
│  │  │ 配置 Project SDK │   │ 扫描 Qin 项目            │   │   │
│  │  │ (异步立即执行)    │   │ (并行执行)              │   │   │
│  │  └─────────────────┘   └─────────────────────────┘   │   │
│  │                              │                        │   │
│  │                              ▼                        │   │
│  │                        ┌───────────────┐              │   │
│  │                        │ 生成 .iml 文件 │              │   │
│  │                        │ (每个项目并行) │              │   │
│  │                        └───────────────┘              │   │
│  │                              │                        │   │
│  │                              ▼                        │   │
│  │                     ┌─────────────────┐               │   │
│  │                     │ qin sync (每项目)│               │   │
│  │                     │ (后台串行执行)   │               │   │
│  │                     └─────────────────┘               │   │
│  │                              │                        │   │
│  │                              ▼                        │   │
│  │                        刷新项目模型                    │   │
│  │                                                       │   │
│  └───────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 关键设计决策

| 设计 | 说明 |
|-----|-----|
| **SDK 配置立即执行** | 不等待 sync 完成，用户可以立即开始编码 |
| **UI 不阻塞** | 所有 IO 操作在后台线程执行 |
| **独立任务并行** | 不相互依赖的任务并行执行 |
| **串行任务队列化** | 有依赖关系的任务按顺序执行 |
| **详细日志记录** | 所有操作记录到 `.qin/logs/` 便于调试 |

### 日志系统

日志文件位置：`{项目根目录}/.qin/logs/{yyyy-MM-dd-HH}.log`

日志格式：
```
[2026-01-07 12:10:12] [INFO] [STARTUP] Qin 插件启动
[2026-01-07 12:10:12] [INFO] [SDK] ========== 开始配置 Project SDK ==========
[2026-01-07 12:10:12] [INFO] [SCAN] ✓ 发现 Qin 项目: D:\project\my-project
[2026-01-07 12:10:12] [INFO] [TASK] 用户点击任务: sync
```

### 技术栈

- 使用 IntelliJ Platform SDK
- 内嵌 BSP (Build Server Protocol) 逻辑
- 与 qin-cli 共享常量和配置解析
- Java 21 字节码兼容（用 Java 25 编译，`--release 21`）

## 相关项目

- **qin-cli**: Qin 命令行工具
- **qin-bsp-server**: BSP 服务器（可供其他 IDE 使用）

## 开发注意事项

### ⚠️ UTF-8 BOM 问题

**问题描述**：如果 Java 源文件以 UTF-8 BOM（字节顺序标记，`EF BB BF`）开头，Java 编译器会报错：

```
错误: 非法字符: '\ufeff'
```

**产生原因**：
- 某些编辑器（如 Windows 记事本）保存时会自动添加 BOM
- PowerShell 的 `Out-File` 命令默认使用 UTF-8 with BOM
- 某些工具在修改文件时可能会添加/保留 BOM

**解决方法**：

1. **手动移除**：在 IDEA 中打开文件，选择 **File → Save with Encoding → UTF-8 (without BOM)**

2. **使用 RemoveBOM 工具**：
   ```bash
   # 在插件目录下有 RemoveBOM.java
   javac RemoveBOM.java
   java RemoveBOM src/main/java/com/qin/debug/DebugStartup.java
   ```

3. **检查文件是否有 BOM**：
   ```powershell
   $bytes = [System.IO.File]::ReadAllBytes((Get-Item 'DebugStartup.java').FullName)
   ($bytes[0..2] | ForEach-Object { $_.ToString('X2') }) -join ' '
   # 如果输出 "EF BB BF"，则有 BOM
   ```

**预防措施**：
- 使用 IDEA 编辑 Java 文件（IDEA 默认保存为 UTF-8 无 BOM）
- 避免使用 PowerShell 的 `Out-File` 直接写入 Java 文件
- 如果必须用脚本生成文件，使用 `[System.Text.UTF8Encoding]::new($false)` 或 Java/Python 等能控制 BOM 的工具

### 🔄 重构方法签名的注意事项

**问题描述**：当修改方法签名（如移除参数）时，需要同时更新：
1. 方法定义
2. 所有调用点

**检查方法**：
```bash
# 搜索方法签名
grep -rn "方法名" src/

# 搜索特定参数
grep -rn "QinLogger logger" src/
```

**经验教训**：
- `QinLogger` 从实例模式改为静态单例时，需要修改：
  - `QinLogger.init(basePath)` 替代 `new QinLogger(basePath)`
  - `QinLogger.info()` 替代 `logger.info()`
  - 移除所有方法签名中的 `QinLogger logger` 参数
  - 移除所有方法调用中的 `logger` 参数

### 📝 QinLogger 使用方法

```java
// 初始化（项目打开时调用一次）
QinLogger.init(basePath);

// 静态调用
QinLogger.info("消息");
QinLogger.error("错误消息");
QinLogger.error("错误消息", exception);
```

日志文件路径：`{项目根目录}/.qin/logs/{yyyy-MM-dd-HH}.log`

