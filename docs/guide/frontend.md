# 前端集成

Qin 内置 Vite 支持，自动检测 `src/client` 目录并启用前端开发服务器。

## 自动检测

当 `src/client/index.html` 存在时，Qin 自动启用 Vite：

```
my-app/
├── qin.config.ts
└── src/
    ├── server/
    │   └── Main.java
    └── client/           # 检测到此目录
        ├── index.html    # 入口 HTML
        └── main.ts       # TypeScript 入口
```

## 配置

### 基本配置

```ts
export default defineConfig({
  port: 8080,  // 后端端口
  
  client: {
    root: "src/client",  // 前端目录
    port: 5173,          // 前端端口
  },
});
```

### 代理配置

默认自动代理 `/api` 到后端：

```ts
export default defineConfig({
  port: 8080,
  
  client: {
    proxy: {
      "/api": "http://localhost:8080",
    },
  },
});
```

自定义代理规则：

```ts
client: {
  proxy: {
    "/api": "http://localhost:8080",
    "/ws": "ws://localhost:8080",
    "/static": "http://cdn.example.com",
  },
}
```

## 开发模式

```bash
qin dev
```

启动两个服务：
- 后端: http://localhost:8080
- 前端: http://localhost:5173

前端通过代理访问后端 API：

```ts
// src/client/main.ts
const res = await fetch('/api/users');
const users = await res.json();
```

## 构建

```bash
qin build
```

前端构建产物输出到 `dist/static/`，并打包进 Fat Jar。

### 自定义输出目录

```ts
client: {
  outDir: "dist/public",
}
```

## 框架支持

### React

```bash
bun add react react-dom
bun add -D @types/react @types/react-dom @vitejs/plugin-react
```

```ts
// src/client/main.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';

ReactDOM.createRoot(document.getElementById('root')!).render(<App />);
```

### Vue

```bash
bun add vue
bun add -D @vitejs/plugin-vue
```

```ts
// src/client/main.ts
import { createApp } from 'vue';
import App from './App.vue';

createApp(App).mount('#app');
```

## 与 Hono 的区别

| 特性 | Qin (Spring Boot) | Hono |
|------|-------------------|------|
| 后端运行时 | JVM | Bun/Node |
| 前端集成 | 代理模式 | 同进程 |
| 开发端口 | 两个端口 | 单端口 |
| 构建产物 | Fat Jar | Bundle |

Qin 使用代理模式，前后端分离运行，更适合传统 Java 项目。

## 禁用前端

如果不需要前端，删除 `src/client` 目录或不配置 `client`：

```ts
export default defineConfig({
  name: "api-only",
  // 不配置 client，纯后端项目
});
```
