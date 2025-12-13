# Qin

> 基于 Bun 的新一代跨语言构建工具，以 TypeScript 取代 XML，引领 Java 进入全栈时代。

> A next-generation cross-language build tool powered by Bun. Replace XML with TypeScript, and lead Java into the full-stack era.

## 愿景

**告别 pom.xml，让 Java 开发像前端一样简单。**

我们相信 Java 开发者不应该被繁琐的 XML 配置所困扰。Qin 的目标是成为下一代 Maven，用现代化的开发体验重新定义 Java 项目管理。

## 核心特性

- 🚀 **零 XML 配置** - 使用 TypeScript 配置文件，类型安全，IDE 友好
- ⚡ **极速启动** - 基于 Bun 运行时，毫秒级响应
- 📦 **Monorepo 支持** - 原生多项目管理，本地包自动解析
- 🔗 **npm 风格依赖** - `"group:artifact": "^1.0.0"` 语法，支持 semver
- 🎨 **前端集成** - Spring Boot + 前端一体化开发
- 🎁 **Fat Jar 打包** - 一键生成可执行 JAR

## 快速开始

```bash
# 安装
bun install

# 初始化项目
qin init

# 开发模式
qin dev

# 构建
qin build
```

## 配置示例

`qin.config.ts`:

```typescript
import type { QinConfig } from "qin";

const config: QinConfig = {
  name: "my-app",
  
  // 依赖配置（npm 风格）
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    "my-local-lib": "^1.0.0",  // 本地包
  },
  
  // Maven 仓库（默认阿里云镜像）
  repositories: [
    "https://maven.aliyun.com/repository/public",
  ],
};

export default config;
```

## Monorepo 多项目

```
my-workspace/
├── qin.config.ts          # workspace 配置
├── apps/
│   └── web-app/           # 主应用
│       └── qin.config.ts
└── packages/
    └── shared-lib/        # 共享库
        └── qin.config.ts
```

Workspace 配置：

```typescript
const config: QinConfig = {
  name: "my-workspace",
  packages: ["apps/*", "packages/*"],
};
```

## CLI 命令

| 命令 | 说明 |
|------|------|
| `qin init` | 初始化新项目 |
| `qin dev` | 启动开发服务器 |
| `qin run` | 编译并运行 |
| `qin build` | 构建 Fat Jar |
| `qin sync` | 同步依赖 |

## 与 Maven 对比

| 特性 | Maven | Qin |
|------|-------|-----|
| 配置格式 | XML | TypeScript |
| 类型检查 | ❌ | ✅ |
| 启动速度 | 慢 | 快 |
| Monorepo | 复杂 | 原生支持 |
| 前端集成 | 需要插件 | 内置 |
| 学习曲线 | 陡峭 | 平缓 |

## 技术栈

- **运行时**: [Bun](https://bun.sh/) - 高性能 JavaScript 运行时
- **依赖解析**: [Coursier](https://get-coursier.io/) - 快速 Maven 依赖解析
- **配置加载**: [c12](https://github.com/unjs/c12) - 支持多格式配置
- **版本匹配**: [semver](https://github.com/npm/node-semver) - npm 语义化版本
- **Glob 匹配**: [tinyglobby](https://github.com/SuperchupuDev/tinyglobby) - 轻量 glob 库

## 开发

```bash
# 安装依赖
bun install

# 运行测试
bun test

# 运行示例
cd examples/apps/hello-java
bun run ../../../src/cli.ts dev
```

## License

MIT
