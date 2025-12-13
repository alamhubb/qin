# 简介

Qin 是基于 Bun 开发的**新一代跨语言构建工具**，用 TypeScript 配置取代了 XML，旨在引领 Java 进入全栈时代。

## 为什么选择 Qin？

### 告别 XML 地狱

传统 Java 项目需要维护冗长的 `pom.xml` 或 `build.gradle`：

```xml
<!-- 传统 Maven 配置 -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>my-app</artifactId>
  <version>1.0.0</version>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
      <version>3.2.0</version>
    </dependency>
  </dependencies>
  <!-- 还有更多... -->
</project>
```

使用 Qin，只需要简洁的 TypeScript：

```ts
// qin.config.ts
export default defineConfig({
  name: "my-app",
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
  },
});
```

### 跨语言 & 全栈

- **跨语言** - 不仅支持 Java，未来将支持更多语言
- **全栈友好** - 内置 Vite，前后端统一构建
- **智能检测** - 自动发现入口文件和前端代码

### 基于 Bun

- **极速性能** - 利用 Bun 的高性能运行时
- **依赖解析** - Coursier 秒级解析 Maven 依赖
- **热重载** - 前端修改即时生效

### 全栈友好

Qin 原生支持 Java 后端 + 前端的全栈开发：

```
my-app/
├── qin.config.ts
├── src/
│   ├── server/        # Java 后端
│   │   └── Main.java
│   └── client/        # 前端（自动启用 Vite）
│       ├── index.html
│       └── main.ts
```

## 核心特性

| 特性 | 描述 |
|------|------|
| 跨语言 | 支持 Java，未来更多语言 |
| 零配置 | 智能默认值，开箱即用 |
| TypeScript 配置 | 类型安全，IDE 自动补全 |
| 基于 Bun | 极速性能，现代化体验 |
| 前端集成 | 内置 Vite，全栈开发 |
| Monorepo | 多项目工作区支持 |

## 下一步

- [快速开始](/guide/getting-started) - 5 分钟上手
- [项目结构](/guide/project-structure) - 了解目录约定
- [配置参考](/config/) - 完整配置选项
