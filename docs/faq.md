# 常见问题

## 安装

### Coursier 安装失败？

Qin 会自动安装 Coursier。如果失败，手动安装：

```bash
# macOS/Linux
curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz | gzip -d > cs
chmod +x cs
./cs setup

# Windows
# 下载 https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-win32.zip
```

### 找不到 Java？

Qin 会自动检测本地 JDK。如果使用 Java 功能，确保已安装 JDK：

```bash
java -version
```

推荐使用 [Adoptium](https://adoptium.net/) 或 [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)。

::: tip
Qin 是跨语言工具，只有使用 Java 相关功能时才需要 JDK。
:::

## 依赖

### 依赖下载慢？

使用国内镜像：

```ts
export default defineConfig({
  repositories: [
    "https://maven.aliyun.com/repository/public",
  ],
});
```

### 找不到依赖？

1. 检查坐标格式：`groupId:artifactId:version`
2. 确认版本存在
3. 尝试其他仓库源

### 如何查看依赖树？

```bash
qin deps  # 计划中
```

目前可以查看 `.cache/classpath.txt`。

## 前端

### Vite 没有启动？

确保：
1. 配置了 `client` 对象
2. `src/client/index.html` 存在

```ts
export default defineConfig({
  client: {},  // 启用前端
});
```

### 代理不生效？

检查代理配置：

```ts
client: {
  proxy: {
    "/api": "http://localhost:8080",
  },
}
```

确保后端已启动。

## 构建

### Fat Jar 太大？

Fat Jar 包含所有依赖，体积较大是正常的。可以：
1. 排除不需要的依赖
2. 使用 jlink 创建自定义运行时（计划中）

### 构建失败？

查看错误信息，常见原因：
1. Java 编译错误
2. 依赖冲突
3. 前端构建失败

## Monorepo

### 本地包找不到？

确保：
1. 根目录配置了 `packages`
2. 本地包有 `qin.config.ts`
3. 包名匹配

```ts
// 根目录
packages: ["packages/*"]

// packages/my-lib/qin.config.ts
name: "my-lib"

// apps/my-app/qin.config.ts
dependencies: {
  "my-lib": "*"
}
```

## 其他

### 与 Maven/Gradle 的区别？

| 特性 | Qin | Maven | Gradle |
|------|-----|-------|--------|
| 配置格式 | TypeScript | XML | Groovy/Kotlin |
| 学习曲线 | 低 | 中 | 高 |
| 前端集成 | 内置 | 无 | 插件 |
| 启动速度 | 快 | 慢 | 中 |

### Qin 只支持 Java 吗？

不是！Qin 是**跨语言构建工具**，目前主要支持 Java，未来将支持更多语言。目标是用统一的 TypeScript 配置管理多语言项目。

### 生产环境可用吗？

Qin 目前处于早期阶段，建议用于：
- 个人项目
- 原型开发
- 学习用途

生产环境建议等待 1.0 稳定版。

### 如何贡献？

欢迎贡献！查看 [GitHub](https://github.com/user/qin) 了解详情。
