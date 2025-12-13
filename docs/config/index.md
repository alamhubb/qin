# 配置文件

Qin 使用 `qin.config.ts` 作为配置文件，提供完整的类型支持。

## 基本结构

```ts
// qin.config.ts
import { defineConfig } from "qin";

export default defineConfig({
  name: "my-app",
  version: "1.0.0",
  port: 8080,
  
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
  },
  
  client: {
    root: "src/client",
    port: 5173,
  },
});
```

## defineConfig

`defineConfig` 是一个辅助函数，提供类型提示和自动补全：

```ts
import { defineConfig } from "qin";

export default defineConfig({
  // IDE 会提供自动补全
});
```

## 配置选项

| 选项 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `name` | `string` | - | 项目名称 |
| `version` | `string` | - | 项目版本 |
| `port` | `number` | `8080` | 后端端口 |
| `entry` | `string` | 自动检测 | Java 入口文件 |
| `dependencies` | `object` | `{}` | 依赖配置 |
| `client` | `object` | - | 前端配置 |
| `plugins` | `array` | `[]` | 插件列表 |
| `localRep` | `boolean` | `false` | 使用本地仓库 |
| `output` | `object` | - | 输出配置 |
| `repositories` | `array` | 阿里云镜像 | Maven 仓库 |
| `packages` | `array` | - | Monorepo 项目 |

## 最小配置

```ts
export default defineConfig({
  name: "my-app",
});
```

Qin 会自动：
- 查找 `src/Main.java` 或 `src/server/Main.java`
- 使用默认端口 8080
- 使用全局依赖仓库

## 完整配置示例

```ts
import { defineConfig } from "qin";

export default defineConfig({
  // 基本信息
  name: "my-app",
  version: "1.0.0",
  description: "My awesome app",
  
  // 服务器
  port: 8080,
  entry: "src/server/Main.java",
  
  // 依赖
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    "org.projectlombok:lombok": "1.18.30",
  },
  
  // 仓库
  repositories: [
    "https://maven.aliyun.com/repository/public",
  ],
  localRep: false,
  
  // 前端
  client: {
    root: "src/client",
    port: 5173,
    proxy: {
      "/api": "http://localhost:8080",
    },
    outDir: "dist/static",
  },
  
  // 输出
  output: {
    dir: "dist",
    jarName: "my-app.jar",
  },
  
  // Java
  java: {
    version: "17",
    sourceDir: "src",
  },
});
```

## 环境变量

配置文件是 TypeScript，可以使用环境变量：

```ts
export default defineConfig({
  port: parseInt(process.env.PORT || "8080"),
});
```

## 配置继承

使用 TypeScript 的展开运算符：

```ts
import { baseConfig } from "../shared/config";

export default defineConfig({
  ...baseConfig,
  name: "my-app",
});
```
