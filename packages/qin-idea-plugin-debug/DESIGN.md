# Qin IDEA Plugin 设计文档

## 概述

Qin IDEA Plugin 是一个 IntelliJ IDEA 插件，用于让 IDEA 识别和支持 Qin 项目。它会自动：
1. 检测 Qin 项目（包含 `qin.config.json` 的目录）
2. 生成 `.iml` 模块文件
3. 配置源代码目录、输出目录和依赖
4. 注册模块到 IDEA 项目

## 核心流程

```
┌──────────────────────────────────────────────────────────────────┐
│                        打开 IDEA 项目                              │
│                             ↓                                    │
│              DebugStartup.execute() 自动触发                       │
│                             ↓                                    │
│              扫描所有 Qin 项目 (qin.config.json)                    │
│                             ↓                                    │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │  阶段1: 快速生成 .iml（让 IDEA 立即识别源代码目录）              │ │
│  │    - 生成 {project}.iml                                     │ │
│  │    - 配置 sourceFolder (src/main/java 或 src)               │ │
│  │    - 配置 excludeFolder (.qin, build, libs...)             │ │
│  │    - 注册到 .idea/modules.xml                               │ │
│  └─────────────────────────────────────────────────────────────┘ │
│                             ↓                                    │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │  阶段2: 执行 qin sync（解析依赖）                              │ │
│  │    - 调用 qin sync 命令                                      │ │
│  │    - 生成 .qin/classpath.json                               │ │
│  └─────────────────────────────────────────────────────────────┘ │
│                             ↓                                    │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │  阶段3: 重新生成 .iml（添加依赖配置）                           │ │
│  │    - 读取 .qin/classpath.json                               │ │
│  │    - 添加 module-library 依赖条目                            │ │
│  │    - 支持 JAR 文件和本地类路径                                │ │
│  └─────────────────────────────────────────────────────────────┘ │
│                             ↓                                    │
│                    刷新 IDEA 项目模型                              │
└──────────────────────────────────────────────────────────────────┘
```

## 文件结构

### 生成的文件

```
{qin-project}/
├── qin.config.json          # Qin 配置（源文件）
├── {project-name}.iml       # IDEA 模块文件（生成）
├── .qin/
│   ├── classpath.json       # 依赖路径（qin sync 生成）
│   └── logs/                # 插件日志
│       └── YYYY-MM-DD-HH.log
└── .idea/
    └── modules.xml          # 模块注册（更新）
```

### .iml 文件结构

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="false">
    <exclude-output />
    <output url="file://$MODULE_DIR$/build/classes" />
    <output-test url="file://$MODULE_DIR$/build/test-classes" />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />
      <excludeFolder url="file://$MODULE_DIR$/.qin" />
      <excludeFolder url="file://$MODULE_DIR$/build" />
      <excludeFolder url="file://$MODULE_DIR$/libs" />
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
    <!-- 依赖条目 -->
    <orderEntry type="module-library">
      <library>
        <CLASSES>
          <root url="file://path/to/dependency/classes" />
        </CLASSES>
      </library>
    </orderEntry>
  </component>
</module>
```

## 配置来源对照

| .iml 配置项 | 数据来源 |
|---|---|
| `sourceFolder` | 自动检测 `src/main/java` 或 `src` |
| `output` | `qin.config.json` → `java.outputDir` |
| `excludeFolder` | `QinConstants.IML_EXCLUDED_DIRS` |
| 依赖 | `.qin/classpath.json` |

## 与 Maven 的对比

| 特性 | Maven | Qin |
|---|---|---|
| 配置文件 | `pom.xml` | `qin.config.json` |
| IDEA 支持 | 原生支持 | 自定义插件 |
| 模块文件 | IDEA 自动生成 | 插件生成 |
| 依赖解析 | Maven 仓库 | Coursier |
| 依赖配置 | `External Libraries` | `.iml` 中的 `module-library` |

## 常量配置

### 通用常量（com.qin.constants.QinConstants）

| 常量 | 值 | 说明 |
|---|---|---|
| `CONFIG_FILE` | `qin.config.json` | 配置文件名 |
| `QIN_DIR` | `.qin` | 缓存目录 |
| `LOG_SUBDIR` | `logs` | 日志子目录 |
| `MAX_SCAN_DEPTH` | `20` | 最大扫描深度 |
| `EXCLUDED_DIRS` | Set | 扫描排除目录 |

### IDEA 插件特有常量（com.qin.debug.QinConstants）

| 常量 | 值 | 说明 |
|---|---|---|
| `IML_EXCLUDED_DIRS` | Set | .iml 排除目录 |
| `NODE_TASKS` | `Tasks` | 树节点名 |
| `NODE_DEPENDENCIES` | `Dependencies` | 树节点名 |

## 版本历史

| 版本 | 更新内容 |
|---|---|
| 0.1.1 | 初始版本 |
| 0.1.2 | 修复 Java 版本兼容性（--release 21） |
| 0.1.3 | 添加日志记录 |
| 0.1.4 | 自动注册模块到 modules.xml |
| 0.1.5 | 自动创建 modules.xml |
| 0.1.6 | 读取 classpath.json 配置依赖 |

## 插件位置

```
qin/packages/qin-idea-plugin-debug/build/distributions/qin-idea-plugin-debug-{version}.zip
```

## 安装方法

1. 打开 IDEA → Settings → Plugins
2. 点击齿轮图标 → Install Plugin from Disk
3. 选择 `qin-idea-plugin-debug-{version}.zip`
4. 重启 IDEA
