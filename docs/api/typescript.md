# TypeScript API

Qin 导出的 TypeScript 类型和函数。

## defineConfig

配置辅助函数，提供类型提示。

```ts
import { defineConfig } from "qin";

export default defineConfig({
  name: "my-app",
  // IDE 自动补全
});
```

类型签名：

```ts
function defineConfig(config: QinConfig): QinConfig;
```

## QinConfig

主配置接口。

```ts
interface QinConfig {
  name?: string;
  version?: string;
  description?: string;
  port?: number;
  entry?: string;
  dependencies?: Record<string, string>;
  client?: ClientConfig;
  plugins?: QinPlugin[];
  localRep?: boolean;
  output?: OutputConfig;
  repositories?: Repository[];
  packages?: string[];
  java?: JavaConfig;
  scripts?: Record<string, string>;
}
```

## ClientConfig

前端配置接口。

```ts
interface ClientConfig {
  root?: string;      // 默认 "src/client"
  port?: number;      // 默认 5173
  proxy?: Record<string, string>;
  outDir?: string;    // 默认 "dist/static"
}
```

## QinPlugin

插件接口。

```ts
interface QinPlugin {
  name: string;
  dev?: (ctx: PluginContext) => Promise<void>;
  build?: (ctx: PluginContext) => Promise<void>;
  cleanup?: () => Promise<void>;
}
```

## PluginContext

插件上下文。

```ts
interface PluginContext {
  root: string;       // 项目根目录
  isDev: boolean;     // 是否开发模式
  log: (msg: string) => void;
}
```

## Repository

仓库配置类型。

```ts
type Repository = string | RepositoryConfig;

interface RepositoryConfig {
  id?: string;
  url: string;
  name?: string;
  releases?: boolean;   // 默认 true
  snapshots?: boolean;  // 默认 false
}
```

## OutputConfig

输出配置。

```ts
interface OutputConfig {
  dir?: string;      // 默认 "dist"
  jarName?: string;  // 默认 "{name}.jar"
}
```

## JavaConfig

Java 配置。

```ts
interface JavaConfig {
  version?: string;    // 默认 "17"
  sourceDir?: string;  // 从 entry 推断
}
```

## 使用示例

### 类型导入

```ts
import type { QinConfig, QinPlugin, ClientConfig } from "qin";
```

### 创建插件

```ts
import type { QinPlugin, PluginContext } from "qin";

export function myPlugin(): QinPlugin {
  return {
    name: "my-plugin",
    async dev(ctx: PluginContext) {
      ctx.log("Starting...");
    },
  };
}
```

### 配置继承

```ts
import { defineConfig, type QinConfig } from "qin";

const baseConfig: Partial<QinConfig> = {
  java: { version: "21" },
  repositories: ["https://maven.aliyun.com/repository/public"],
};

export default defineConfig({
  ...baseConfig,
  name: "my-app",
});
```
