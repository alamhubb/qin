# 插件系统

Qin 提供灵活的插件系统，用于扩展构建功能。

## 使用插件

```ts
import { defineConfig } from "qin";
import vite from "qin-plugin-vite";

export default defineConfig({
  plugins: [
    vite({
      port: 3000,
      proxy: { "/api": "http://localhost:8080" },
    }),
  ],
});
```

## 内置插件

### Web Plugin

自动检测 `src/client` 目录并启用 Vite：

```ts
// 无需手动配置，自动启用
export default defineConfig({
  client: {
    root: "src/client",
    port: 5173,
  },
});
```

## 开发插件

### 插件接口

```ts
interface QinPlugin {
  name: string;
  dev?: (ctx: PluginContext) => Promise<void>;
  build?: (ctx: PluginContext) => Promise<void>;
  cleanup?: () => Promise<void>;
}

interface PluginContext {
  root: string;      // 项目根目录
  isDev: boolean;    // 是否开发模式
  log: (msg: string) => void;
}
```

### 示例：自定义插件

```ts
// my-plugin.ts
import type { QinPlugin, PluginContext } from "qin";

export function myPlugin(options: { message: string }): QinPlugin {
  return {
    name: "my-plugin",
    
    async dev(ctx: PluginContext) {
      ctx.log(`[my-plugin] Dev mode: ${options.message}`);
      // 开发模式逻辑
    },
    
    async build(ctx: PluginContext) {
      ctx.log(`[my-plugin] Building...`);
      // 构建逻辑
    },
    
    async cleanup() {
      // 清理资源
    },
  };
}
```

使用：

```ts
import { myPlugin } from "./my-plugin";

export default defineConfig({
  plugins: [
    myPlugin({ message: "Hello!" }),
  ],
});
```

## 插件生命周期

```
qin dev:
  1. 加载配置
  2. 解析依赖
  3. 编译 Java
  4. 调用 plugin.dev()  ← 插件启动
  5. 启动 Java 进程
  6. 等待退出
  7. 调用 plugin.cleanup()  ← 插件清理

qin build:
  1. 加载配置
  2. 解析依赖
  3. 编译 Java
  4. 调用 plugin.build()  ← 插件构建
  5. 打包 Fat Jar
```

## 官方插件

| 插件 | 描述 |
|------|------|
| `qin-plugin-vite` | Vite 前端集成 |

## 发布插件

1. 创建 npm 包
2. 导出符合 `QinPlugin` 接口的函数
3. 发布到 npm

```json
{
  "name": "qin-plugin-xxx",
  "main": "dist/index.js",
  "types": "dist/index.d.ts",
  "peerDependencies": {
    "qin": "^1.0.0"
  }
}
```
