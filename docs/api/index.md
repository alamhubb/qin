# CLI 命令

Qin 命令行工具参考。

## 安装

```bash
npm install -g qin
# 或
bun install -g qin
```

## 命令列表

| 命令 | 说明 |
|------|------|
| `qin run` | 编译并运行 Java 应用 |
| `qin dev` | 开发模式（热重载） |
| `qin build` | 构建 Fat Jar |
| `qin sync` | 同步依赖 |
| `qin init` | 创建新项目 |

## qin run

编译并运行 Java 应用。

```bash
qin run
```

流程：
1. 加载 `qin.config.ts`
2. 解析依赖（Coursier）
3. 编译 Java 源码
4. 运行 Main 类

选项：

| 选项 | 说明 |
|------|------|
| `--port <n>` | 覆盖端口 |

## qin dev

开发模式，启动后端和前端开发服务器。

```bash
qin dev
```

流程：
1. 执行 `qin run` 流程
2. 启动 Vite 开发服务器（如果配置了 client）
3. 监听文件变化

输出：
```
[qin] Backend running at http://localhost:8080
[qin] Frontend running at http://localhost:5173
```

## qin build

构建可执行的 Fat Jar。

```bash
qin build
```

流程：
1. 编译 Java 源码
2. 构建前端（如果有）
3. 打包 Fat Jar

输出：
```
dist/
├── my-app.jar
└── static/
```

## qin sync

下载并缓存所有依赖。

```bash
qin sync
```

依赖存储位置：
- 全局: `~/.qin/repository/`
- 本地: `./repository/`（需配置 `localRep: true`）

## qin init

创建新项目（计划中）。

```bash
qin init my-app
cd my-app
```

生成：
```
my-app/
├── qin.config.ts
├── src/
│   └── server/
│       └── Main.java
└── .gitignore
```

## 全局选项

| 选项 | 说明 |
|------|------|
| `--help` | 显示帮助 |
| `--version` | 显示版本 |

## 退出码

| 码 | 含义 |
|----|------|
| 0 | 成功 |
| 1 | 一般错误 |
| 2 | 配置错误 |
| 3 | 编译错误 |

## 环境变量

| 变量 | 说明 |
|------|------|
| `JAVA_HOME` | Java 安装目录 |
| `QIN_REPO` | 全局仓库目录 |
