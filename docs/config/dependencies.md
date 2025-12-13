# 依赖配置

Maven 依赖和本地包配置。

## dependencies

依赖映射表，键为依赖坐标，值为版本。

```ts
export default defineConfig({
  dependencies: {
    // Maven 依赖
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    "com.google.guava:guava": "32.1.3-jre",
    
    // 本地包
    "java-base": "*",
  },
});
```

## Maven 依赖格式

```
groupId:artifactId:version
```

示例：

| 依赖 | groupId | artifactId |
|------|---------|------------|
| Spring Boot Web | `org.springframework.boot` | `spring-boot-starter-web` |
| Guava | `com.google.guava` | `guava` |
| Lombok | `org.projectlombok` | `lombok` |

## 版本语法

支持 npm 风格的版本范围：

| 语法 | 含义 | 示例 |
|------|------|------|
| `3.2.0` | 精确版本 | 只使用 3.2.0 |
| `^3.2.0` | 兼容版本 | >=3.2.0 <4.0.0 |
| `~3.2.0` | 补丁版本 | >=3.2.0 <3.3.0 |
| `*` | 任意版本 | 最新版本 |
| `>=3.0.0` | 大于等于 | 3.0.0 及以上 |

## repositories

Maven 仓库配置。

- 类型: `(string | RepositoryConfig)[]`
- 默认: 阿里云镜像

### 简单配置

```ts
export default defineConfig({
  repositories: [
    "https://maven.aliyun.com/repository/public",
    "https://repo1.maven.org/maven2",
  ],
});
```

### 完整配置

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

### RepositoryConfig

| 属性 | 类型 | 默认 | 说明 |
|------|------|------|------|
| `id` | `string` | - | 仓库标识 |
| `url` | `string` | - | 仓库地址 |
| `name` | `string` | - | 仓库名称 |
| `releases` | `boolean` | `true` | 启用 release |
| `snapshots` | `boolean` | `false` | 启用 snapshot |

## localRep

使用项目本地仓库。

- 类型: `boolean`
- 默认: `false`

```ts
export default defineConfig({
  localRep: true,  // 依赖存储在 ./repository
});
```

### 全局模式（默认）

```
~/.qin/repository/
└── org/springframework/boot/spring-boot-3.2.0.jar
```

### 本地模式

```
my-app/
└── repository/
    └── org/springframework/boot/spring-boot-3.2.0.jar
```

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

本地包需要在 `packages/` 目录下，并有自己的 `qin.config.ts`。

## 同步依赖

```bash
qin sync
```

下载所有依赖到仓库（全局或本地）。
