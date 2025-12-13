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

### 全局存储（默认）

```
~/.qin/repository/
└── org/
    └── springframework/
        └── boot/
            └── spring-boot-3.2.0.jar
```

### 本地存储

```ts
export default defineConfig({
  localRep: true,  // 启用本地存储
});
```

```
my-app/
└── repository/
    └── org/
        └── springframework/
            └── boot/
                └── spring-boot-3.2.0.jar
```

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
