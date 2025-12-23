# 依赖管理

Qin 支持 Maven 依赖和本地包两种依赖方式。

## Maven 依赖

使用 `groupId:artifactId:version` 格式：

```ts
export default defineConfig({
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    "com.google.guava:guava": "32.1.3-jre",
    "org.projectlombok:lombok": "1.18.30",
  },
});
```

## 版本语法

支持 npm 风格的版本范围：

| 语法 | 含义 |
|------|------|
| `3.2.0` | 精确版本 |
| `^3.2.0` | 兼容版本 (>=3.2.0 <4.0.0) |
| `~3.2.0` | 补丁版本 (>=3.2.0 <3.3.0) |
| `*` | 任意版本 |
| `>=3.0.0` | 大于等于 |

## 本地包

在 Monorepo 中引用本地包：

```ts
// apps/my-app/qin.config.ts
export default defineConfig({
  dependencies: {
    "shared-utils": "*",      // 从 packages/ 查找
    "java-base": "^1.0.0",    // 版本校验
  },
});
```

本地包会自动从工作区的 `packages/` 目录解析。

## 仓库配置

默认使用阿里云镜像加速：

```ts
export default defineConfig({
  repositories: [
    "https://maven.aliyun.com/repository/public",
    "https://repo1.maven.org/maven2",
  ],
});
```

完整配置：

```ts
export default defineConfig({
  repositories: [
    {
      id: "aliyun",
      url: "https://maven.aliyun.com/repository/public",
      releases: true,
      snapshots: false,
    },
    {
      id: "central",
      url: "https://repo1.maven.org/maven2",
    },
  ],
});
```

## 依赖存储

Qin 采用类似 pnpm 的存储策略，支持全局缓存和本地可见两种模式。

### 全局存储（默认）

默认情况下，所有依赖存储在全局目录，多个项目共享：

```
~/.qin/repository/
└── org/
    └── springframework/
        └── boot/
            └── spring-boot-3.2.0.jar
```

优点：
- 节省磁盘空间（多项目共享）
- 下载一次，到处使用

### 本地存储（localRep）

如果你希望在项目内直接看到依赖（类似 `node_modules`），可以启用本地存储：

```ts
export default defineConfig({
  localRep: true,  // 依赖安装到项目本地 ./repository
});
```

启用后，项目结构：

```
my-app/
├── qin.config.ts
├── src/
│   └── server/
│       └── Main.java
├── repository/              # 本地依赖目录（类似 node_modules）
│   └── org/
│       └── springframework/
│           └── boot/
│               └── spring-boot-3.2.0.jar
└── .qin/
    └── classes/             # 编译输出
```

优点：
- 依赖在项目内可见，方便查看源码
- 项目自包含，便于打包分发
- 离线开发更方便

::: tip 何时使用 localRep？
- 需要查看/调试依赖源码时
- 项目需要离线运行时
- 团队协作需要锁定依赖版本时
:::

### 存储对比

| 特性 | 全局存储 (默认) | 本地存储 (localRep: true) |
|------|----------------|--------------------------|
| 存储位置 | `~/.qin/repository/` | `./repository/` |
| 磁盘占用 | 低（共享） | 高（每项目独立） |
| 依赖可见 | ❌ 需要去全局目录查看 | ✅ 项目内直接可见 |
| 离线支持 | 需要先下载 | 项目自包含 |
| 类似于 | Maven `~/.m2/repository` | npm `node_modules` |

## 同步依赖

```bash
# 下载并缓存所有依赖
qin sync
```

## 查看依赖

```bash
# 查看依赖树（计划中）
qin deps
```

## 常见问题

### 依赖下载慢？

确保使用国内镜像：

```ts
repositories: [
  "https://maven.aliyun.com/repository/public",
],
```

### 找不到依赖？

1. 检查 `groupId:artifactId:version` 格式是否正确
2. 确认版本号存在
3. 尝试添加其他仓库源
