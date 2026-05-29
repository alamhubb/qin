# 示例项目

Qin 官方示例项目。

## 基础示例

### Hello Java

最简单的 `.qin + Spring Boot` 后端应用。

当前后端示例能力：

- `.qin` controller + `.qin` service
- `GET /api/hello`
- `GET /api/hello/detail`
- `GET /api/ping`
- `POST /api/greet`，请求体 `{"name":"  qin  "}`
- `POST /api/greet/loud`，请求体 `{"name":"qin"}`

```bash
git clone https://github.com/user/qin
cd qin/examples/apps/hello-java
qin run
```

运行说明：

- `src/server/HelloController.qin` 是 controller 业务代码
- `src/server/HelloService.qin` 是 service 业务代码
- `src/server/Main.java` 只是很薄的 Spring Boot host shell，用来启动并注册 `.qin` 生成的 Spring bean
- 默认使用随机空闲端口启动，启动成功后会在控制台打印实际 URL
- 根路径 `/` 会返回 demo 说明；主要接口在 `/api/*`
- 如果想固定端口，可以运行 `qin run --jvm-args=-Dserver.port=8080`

```ts
// qin.config.ts
export default defineConfig({
  name: "hello-java",
  port: 0,
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6",
  },
});
```

### 全栈应用

Spring Boot + Vite 前端。

```
fullstack-app/
├── qin.config.ts
└── src/
    ├── server/
    │   └── Main.java
    └── client/
        ├── index.html
        └── main.ts
```

```ts
export default defineConfig({
  name: "fullstack-app",
  port: 8080,
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6",
  },
  client: {
    root: "src/client",
    port: 5173,
  },
});
```

## Monorepo 示例

### 工作区结构

```
workspace/
├── qin.config.ts
├── apps/
│   └── web-app/
│       ├── qin.config.ts
│       └── src/
└── packages/
    └── java-base/
        ├── qin.config.ts
        └── src/
```

### 工作区配置

```ts
// 根目录 qin.config.ts
export default defineConfig({
  packages: ["apps/*", "packages/*"],
});
```

### 应用配置

```ts
// apps/web-app/qin.config.ts
export default defineConfig({
  name: "web-app",
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6",
    "java-base": "*",  // 本地包
  },
});
```

## 框架集成

### React 前端

```ts
export default defineConfig({
  name: "react-app",
  client: {
    root: "src/client",
  },
});
```

```tsx
// src/client/main.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';

function App() {
  const [data, setData] = React.useState(null);
  
  React.useEffect(() => {
    fetch('/api/hello')
      .then(res => res.json())
      .then(setData);
  }, []);
  
  return <div>{data?.message}</div>;
}

ReactDOM.createRoot(document.getElementById('root')!).render(<App />);
```

### Vue 前端

```ts
export default defineConfig({
  name: "vue-app",
  client: {
    root: "src/client",
  },
});
```

```ts
// src/client/main.ts
import { createApp } from 'vue';
import App from './App.vue';

createApp(App).mount('#app');
```

## 更多示例

查看 [GitHub 仓库](https://github.com/user/qin/tree/main/examples) 获取更多示例。
