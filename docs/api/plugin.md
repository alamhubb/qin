# 插件 API

开发 Qin 插件的 API 参考。

## 插件结构

```ts
import type { QinPlugin, PluginContext } from "qin";

export function myPlugin(options?: MyOptions): QinPlugin {
  return {
    name: "my-plugin",
    
    async dev(ctx) {
      // 开发模式启动
    },
    
    async build(ctx) {
      // 构建时执行
    },
    
    async cleanup() {
      // 清理资源
    },
  };
}
```

## QinPlugin

### name

插件名称，用于日志输出。

- 类型: `string`
- 必需: 是

```ts
{
  name: "my-plugin",
}
```

### dev

开发模式钩子，`qin dev` 时调用。

- 类型: `(ctx: PluginContext) => Promise<void>`
- 必需: 否

```ts
{
  async dev(ctx) {
    ctx.log("[my-plugin] Starting dev server...");
    // 启动开发服务器
  },
}
```

### build

构建钩子，`qin build` 时调用。

- 类型: `(ctx: PluginContext) => Promise<void>`
- 必需: 否

```ts
{
  async build(ctx) {
    ctx.log("[my-plugin] Building...");
    // 执行构建
  },
}
```

### cleanup

清理钩子，进程退出时调用。

- 类型: `() => Promise<void>`
- 必需: 否

```ts
{
  async cleanup() {
    // 关闭服务器、清理临时文件等
  },
}
```

## PluginContext

### root

项目根目录绝对路径。

- 类型: `string`

```ts
async dev(ctx) {
  const configPath = path.join(ctx.root, "qin.config.ts");
}
```

### isDev

是否为开发模式。

- 类型: `boolean`

```ts
async dev(ctx) {
  if (ctx.isDev) {
    // 开发模式特定逻辑
  }
}
```

### log

日志输出函数。

- 类型: `(msg: string) => void`

```ts
async dev(ctx) {
  ctx.log("[my-plugin] Server started");
}
```

## 生命周期

```
qin dev:
┌─────────────────────────────────────┐
│ 1. 加载配置                          │
│ 2. 解析依赖                          │
│ 3. 编译 Java                         │
│ 4. plugin.dev(ctx)  ← 插件启动       │
│ 5. 启动 Java 进程                    │
│ 6. 等待退出信号                      │
│ 7. plugin.cleanup() ← 插件清理       │
└─────────────────────────────────────┘

qin build:
┌─────────────────────────────────────┐
│ 1. 加载配置                          │
│ 2. 解析依赖                          │
│ 3. 编译 Java                         │
│ 4. plugin.build(ctx) ← 插件构建      │
│ 5. 打包 Fat Jar                      │
└─────────────────────────────────────┘
```

## 示例：Vite 插件

```ts
import type { QinPlugin } from "qin";
import { createServer, build } from "vite";

interface VitePluginOptions {
  port?: number;
  proxy?: Record<string, string>;
}

export function vitePlugin(options: VitePluginOptions = {}): QinPlugin {
  let server: any;
  
  return {
    name: "vite",
    
    async dev(ctx) {
      server = await createServer({
        root: ctx.root,
        server: {
          port: options.port || 5173,
          proxy: options.proxy,
        },
      });
      await server.listen();
      ctx.log(`[vite] Dev server at http://localhost:${options.port || 5173}`);
    },
    
    async build(ctx) {
      await build({
        root: ctx.root,
        build: {
          outDir: "dist/static",
        },
      });
      ctx.log("[vite] Build complete");
    },
    
    async cleanup() {
      if (server) {
        await server.close();
      }
    },
  };
}
```

## 发布插件

### package.json

```json
{
  "name": "qin-plugin-xxx",
  "version": "1.0.0",
  "main": "dist/index.js",
  "types": "dist/index.d.ts",
  "exports": {
    ".": {
      "import": "./dist/index.js",
      "types": "./dist/index.d.ts"
    }
  },
  "peerDependencies": {
    "qin": "^1.0.0"
  }
}
```

### 导出

```ts
// src/index.ts
export { myPlugin } from "./plugin";
export type { MyPluginOptions } from "./types";
```
