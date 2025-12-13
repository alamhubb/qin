# 前端配置

内置 Vite 前端集成配置。

## client

前端配置对象，自动检测 `src/client` 目录。

```ts
export default defineConfig({
  client: {
    root: "src/client",
    port: 5173,
    proxy: {
      "/api": "http://localhost:8080",
    },
    outDir: "dist/static",
  },
});
```

## client.root

前端源码目录。

- 类型: `string`
- 默认: `"src/client"`

```ts
client: {
  root: "web",  // 使用 web/ 目录
}
```

目录结构：

```
my-app/
└── src/
    └── client/        # root: "src/client"
        ├── index.html
        └── main.ts
```

## client.port

前端开发服务器端口。

- 类型: `number`
- 默认: `5173`

```ts
client: {
  port: 3000,
}
```

## client.proxy

API 代理配置。

- 类型: `Record<string, string>`
- 默认: `{ "/api": "http://localhost:{port}" }`

```ts
client: {
  proxy: {
    "/api": "http://localhost:8080",
    "/ws": "ws://localhost:8080",
  },
}
```

代理规则：
- `/api/users` → `http://localhost:8080/api/users`
- `/ws/chat` → `ws://localhost:8080/ws/chat`

## client.outDir

前端构建输出目录。

- 类型: `string`
- 默认: `"dist/static"`

```ts
client: {
  outDir: "dist/public",
}
```

## 自动检测

当以下条件满足时，Qin 自动启用前端：

1. 配置了 `client` 对象
2. `src/client/index.html` 存在

```ts
// 最简配置，使用所有默认值
export default defineConfig({
  client: {},
});
```

## 禁用前端

不配置 `client` 或删除 `src/client` 目录：

```ts
export default defineConfig({
  name: "api-only",
  // 不配置 client
});
```

## 与后端端口

```ts
export default defineConfig({
  port: 8080,        // 后端端口
  
  client: {
    port: 5173,      // 前端端口
    proxy: {
      "/api": "http://localhost:8080",  // 代理到后端
    },
  },
});
```

开发时：
- 后端: http://localhost:8080
- 前端: http://localhost:5173
- 前端 `/api/*` 请求代理到后端

## 完整示例

```ts
export default defineConfig({
  name: "fullstack-app",
  port: 8080,
  
  client: {
    root: "src/client",
    port: 5173,
    proxy: {
      "/api": "http://localhost:8080",
      "/graphql": "http://localhost:8080",
    },
    outDir: "dist/static",
  },
});
```
