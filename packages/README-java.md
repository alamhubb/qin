# Qin Packages - Java 实现

这是 Qin 各个插件包的 Java 重写版本。

## 编译状态

✅ 所有 Java 文件已通过编译验证（Java 25）

## 包结构

```
packages/
├── create-qin/src/java/
│   └── com/qin/create/
│       └── CreateQin.java          # 项目脚手架工具
│
├── qin-plugin-java/src/java/
│   └── com/qin/plugins/
│       ├── JavaPlugin.java         # Java 语言支持插件
│       └── PluginInterfaces.java   # 插件接口定义
│
├── qin-plugin-java-hot-reload/src/java/
│   └── com/qin/plugins/
│       └── HotReloadPlugin.java    # Java 热重载插件
│
├── qin-plugin-spring/src/java/
│   └── com/qin/plugins/
│       └── SpringPlugin.java       # Spring Boot 支持插件
│
├── qin-plugin-vite/src/java/
│   └── com/qin/plugins/
│       └── VitePlugin.java         # Vite 前端集成插件
│
├── qin-plugin-graalvm/src/java/
│   └── com/qin/plugins/
│       └── GraalVMPlugin.java      # GraalVM 运行时支持
│
└── qin-plugin-graalvm-js/src/java/
    └── com/qin/plugins/
        └── GraalVMJsPlugin.java    # GraalVM JavaScript 支持
```

## 与 TypeScript 版本的对应关系

| TypeScript 包 | Java 类 | 功能 |
|--------------|---------|------|
| create-qin | CreateQin | 交互式项目创建 |
| qin-plugin-java | JavaPlugin | Java 编译/运行/构建 |
| qin-plugin-java-hot-reload | HotReloadPlugin | 文件监听和热重载 |
| qin-plugin-spring | SpringPlugin | Spring Boot 配置生成 |
| qin-plugin-vite | VitePlugin | Vite 开发服务器集成 |
| qin-plugin-graalvm | GraalVMPlugin | GraalVM 环境检测 |
| qin-plugin-graalvm-js | GraalVMJsPlugin | GraalVM JS 执行 |

## 功能对比

### create-qin

| 功能 | TypeScript | Java |
|------|------------|------|
| 交互式创建 | ✅ | ✅ |
| 多语言支持 | ✅ Java/Bun/Node | ✅ Java/Bun/Node |
| 多模板支持 | ✅ fullstack/monorepo | ✅ fullstack/monorepo |
| 命令行参数 | ✅ | ✅ |

### qin-plugin-java

| 功能 | TypeScript | Java |
|------|------------|------|
| Java 编译 | ✅ | ✅ |
| 运行程序 | ✅ | ✅ |
| Fat JAR 构建 | ✅ | ✅ |
| JUnit 测试 | ✅ | ✅ 基础实现 |
| 资源文件处理 | ✅ | ✅ |
| 热重载集成 | ✅ | ✅ 通过插件组合 |

### qin-plugin-java-hot-reload

| 功能 | TypeScript | Java |
|------|------------|------|
| 文件监听 | ✅ chokidar | ✅ WatchService |
| 防抖处理 | ✅ | ✅ |
| 自动重编译 | ✅ | ✅ |
| DevTools 检测 | ✅ | ✅ |

### qin-plugin-spring

| 功能 | TypeScript | Java |
|------|------------|------|
| Spring Boot 检测 | ✅ | ✅ |
| DevTools 检测 | ✅ | ✅ |
| application.yml 生成 | ✅ | ✅ |
| 配置转换 | ✅ camelCase→kebab | ✅ |

### qin-plugin-vite

| 功能 | TypeScript | Java |
|------|------------|------|
| 前端目录检测 | ✅ | ✅ |
| Vite 配置生成 | ✅ | ✅ |
| 开发服务器启动 | ✅ | ✅ |
| API 代理配置 | ✅ | ✅ |
| 生产构建 | ✅ | ✅ |

### qin-plugin-graalvm

| 功能 | TypeScript | Java |
|------|------------|------|
| GraalVM 检测 | ✅ | ✅ |
| 组件列表 | ✅ | ✅ |
| 版本信息 | ✅ | ✅ |
| 安装指南 | ✅ | ✅ |

### qin-plugin-graalvm-js

| 功能 | TypeScript | Java |
|------|------------|------|
| JS 执行 | ✅ | ✅ |
| Java 互操作 | ✅ --polyglot --jvm | ✅ |
| 语法验证 | ✅ | ✅ |
| 热重载 | ✅ | ✅ |
| 错误格式化 | ✅ | ✅ 基础实现 |

## 编译

### 快速编译（推荐）

使用项目根目录的编译脚本：

```bash
# Windows
build-java.bat

# Linux/macOS
chmod +x build-java.sh
./build-java.sh
```

### 手动编译

每个包可以独立编译：

```bash
# 编译 create-qin
cd packages/create-qin
javac -d build/classes src/java/com/qin/create/*.java

# 编译 qin-plugin-java
cd packages/qin-plugin-java
javac -d build/classes src/java/com/qin/plugins/*.java

# 编译所有插件（需要先编译接口定义）
```

## 使用示例

### 创建项目

```java
// 使用 CreateQin
CreateQin.main(new String[]{"my-app", "-java", "-t", "fullstack"});
```

### 使用插件

```java
// 创建 Java 插件
JavaPlugin javaPlugin = JavaPlugin.create();

// 创建 Spring 插件
SpringBootPluginOptions springOptions = new SpringBootPluginOptions();
ServerConfig server = new ServerConfig();
server.setPort(8080);
springOptions.setServer(server);
SpringPlugin springPlugin = SpringPlugin.create(springOptions);

// 创建 Vite 插件
VitePluginOptions viteOptions = new VitePluginOptions();
viteOptions.setPort(5173);
VitePlugin vitePlugin = VitePlugin.create(viteOptions);
```

## 注意事项

1. Java 版本使用 `WatchService` 替代 `chokidar` 进行文件监听
2. 配置文件格式为 JSON（`qin.config.json`）而非 TypeScript
3. 部分高级功能（如完整的 JUnit 集成）仍在开发中
4. GraalVM 相关功能需要安装 GraalVM 运行时
