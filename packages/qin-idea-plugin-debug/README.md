# Qin IDEA Plugin

> IntelliJ IDEA 插件，为 Qin 构建工具提供 IDE 集成支持。

## 功能特性

### 1. 自动依赖同步
- **项目打开时自动执行 `qin sync`**
- 自动发现 Monorepo 中的所有子项目
- 为每个子项目同步依赖

### 2. IDEA 集成
- **自动生成 `.idea/libraries/*.xml`**：让 IDEA 识别项目依赖
- **配置编译输出路径**：自动设置为 `build/classes`（与 qin 一致）
- **更新 `.iml` 文件**：自动添加库引用

### 3. Monorepo 支持
- **智能扫描策略**：
  1. 从 IDEA 打开的目录向上查找 workspace root
  2. 从 workspace root 向下递归扫描所有 `qin.config.json`
  3. 为每个发现的项目执行 sync

## 设计架构

```
┌─────────────────────────────────────────────────────────┐
│                    DebugStartup.java                     │
│                  (ProjectActivity 实现)                   │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  1. 项目打开时触发                                        │
│     ↓                                                   │
│  2. findWorkspaceRoot() - 向上查找 workspace root        │
│     ↓                                                   │
│  3. scanForQinProjects() - 向下扫描所有 qin.config.json   │
│     ↓                                                   │
│  4. runQinSync() - 为每个项目执行 qin sync               │
│     ↓                                                   │
│  5. VirtualFileManager.refresh() - 刷新 IDEA 项目模型    │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### 核心类说明

| 类 | 功能 |
|---|---|
| `DebugStartup` | 项目启动监听器，自动检测 Qin 项目并执行 sync |
| `QinToolWindowFactory` | Qin 工具窗口面板，提供 Sync/Compile/Run 按钮 |
| `QinLogger` | 日志记录器，按项目和时间组织日志文件 |

### 扫描策略实现

```java
// 1. 向上查找 workspace root（取最顶层）
private Path findWorkspaceRoot(Path startDir) {
    Path current = startDir;
    Path topMost = startDir;
    
    while (current != null) {
        // 标志：.idea, .vscode, .git, qin.config.json
        if (isProjectRoot(current)) {
            topMost = current; // 继续向上，取最顶层
        }
        current = current.getParent();
    }
    return topMost;
}

// 2. 向下递归扫描（最多 5 层深度）
private void scanForQinProjects(Path dir, List<Path> projects, int depth, int maxDepth) {
    if (depth >= maxDepth) return;
    
    for (Path subDir : Files.list(dir)) {
        if (Files.exists(subDir.resolve("qin.config.json"))) {
            projects.add(subDir);
        }
        scanForQinProjects(subDir, projects, depth + 1, maxDepth);
    }
}
```

## 安装

1. 在 IDEA 中打开 **Settings → Plugins → ⚙️ → Install Plugin from Disk**
2. 选择 `build/distributions/qin-idea-plugin-debug-x.x.x.zip`
3. 重启 IDEA

## 构建

```bash
cd packages/qin-idea-plugin-debug
./gradlew buildPlugin
```

输出：`build/distributions/qin-idea-plugin-debug-x.x.x.zip`

## 配置

插件会自动检测包含 `qin.config.json` 的项目，无需额外配置。

### 常量配置

| 常量 | 值 | 说明 |
|---|---|---|
| `CONFIG_FILE` | `qin.config.json` | 配置文件名 |
| `EXCLUDED_DIRS` | `node_modules, .git, build, ...` | 扫描时排除的目录 |
| 扫描深度 | 5 层 | 向下递归扫描的最大深度 |

## 日志

日志文件保存在：`~/.qin/logs/{项目名}/{日期}.log`

## 版本历史

### 0.0.7 (当前)
- 增强 Monorepo 支持：向上找 workspace root，向下扫描所有子项目
- 常量化配置文件名

### 0.0.6
- 自动同步依赖
- 自动生成 IDEA 库配置
- 配置编译输出路径为 `build/classes`

### 0.0.5
- 初始版本
- Qin 工具窗口面板
