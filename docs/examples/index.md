# 示例项目

Qin 官方示例项目。

## 基础示例

### Hello Java

最简单的 Spring Boot 应用。

```bash
git clone https://github.com/user/qin
cd qin/examples/apps/hello-java
qin run
```

```ts
// qin.config.ts
export default defineConfig({
  name: "hello-java",
  port: 8080,
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
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
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
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
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
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
