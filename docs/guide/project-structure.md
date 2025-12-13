# 项目结构

Qin 使用约定优于配置的原则，遵循简单的目录结构。

## 基本结构

```
my-app/
├── qin.config.ts          # 配置文件
├── src/
│   ├── server/            # Java 后端代码
│   │   └── Main.java      # 入口类
│   └── client/            # 前端代码（可选）
│       ├── index.html
│       └── main.ts
├── repository/            # 本地依赖（localRep: true）
├── dist/                  # 构建输出
│   ├── my-app.jar
│   └── static/            # 前端构建产物
├── build/                 # 编译输出
│   └── classes/           # class 文件
└── .cache/                # 缓存目录
    └── classpath.txt      # 依赖缓存
```

## 入口文件

Qin 按以下顺序自动查找 Java 入口：

1. `src/Main.java`
2. `src/server/Main.java`
3. `src/{任意子目录}/Main.java`

也可以在配置中显式指定：

```ts
export default defineConfig({
  entry: "src/app/Application.java",
});
```

## 前端目录

默认前端目录是 `src/client`。Qin 会检测该目录下是否有 `index.html`，有则自动启用 Vite。

自定义前端目录：

```ts
export default defineConfig({
  client: {
    root: "web",  // 使用 web/ 目录
  },
});
```

## 依赖存储

### 全局模式（默认）

依赖存储在 `~/.qin/repository`，多项目共享：

```ts
export default defineConfig({
  // localRep: false,  // 默认
});
```

### 本地模式

依赖存储在项目的 `./repository`，类似 `node_modules`：

```ts
export default defineConfig({
  localRep: true,
});
```

## Monorepo 结构

```
workspace/
├── qin.config.ts          # 工作区配置
├── apps/
│   └── my-app/
│       ├── qin.config.ts
│       └── src/
└── packages/
    └── shared/
        ├── qin.config.ts
        └── src/
```

工作区配置：

```ts
// 根目录 qin.config.ts
export default defineConfig({
  packages: ["apps/*", "packages/*"],
});
```

应用配置：

```ts
// apps/my-app/qin.config.ts
export default defineConfig({
  dependencies: {
    "shared": "*",  // 引用本地包
  },
});
```

## .gitignore 建议

```gitignore
# Qin
.cache/
build/
dist/
repository/

# IDE
.idea/
.vscode/
*.iml
```
