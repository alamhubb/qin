# 快速开始

5 分钟上手 Qin。

## 前置要求

- [Bun](https://bun.sh/) v1.0+

::: tip
Qin 是跨语言构建工具。使用 Java 功能时会自动检测本地 JDK，无需额外配置。
:::

## 安装

<CodeTabs :tabs="['npm', 'bun', '源码']">
<template #tab-0>

```bash
npm install -g qin
```

</template>
<template #tab-1>

```bash
bun install -g qin
```

</template>
<template #tab-2>

```bash
git clone https://github.com/user/qin
cd qin
bun install
bun link
```

</template>
</CodeTabs>

## 创建项目

```bash
qin init my-app
cd my-app
```

生成的项目结构：

```
my-app/
├── qin.config.ts      # 配置文件
├── src/
│   └── server/
│       └── Main.java  # Java 入口
└── .gitignore
```

## 运行

```bash
qin run
```

访问 http://localhost:8080

## 添加依赖

编辑 `qin.config.ts`：

```ts
export default defineConfig({
  name: "my-app",
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
  },
});
```

同步依赖：

```bash
qin sync
```

## 添加前端

创建 `src/client/index.html`：

```html
<!DOCTYPE html>
<html>
<head>
  <title>My App</title>
</head>
<body>
  <div id="app"></div>
  <script type="module" src="/main.ts"></script>
</body>
</html>
```

创建 `src/client/main.ts`：

```ts
document.getElementById('app')!.innerHTML = '<h1>Hello Qin!</h1>';

// 调用后端 API
const res = await fetch('/api/hello');
console.log(await res.text());
```

启动开发服务器：

```bash
qin dev
```

- 后端: http://localhost:8080
- 前端: http://localhost:5173

## 构建

```bash
qin build
```

生成 `dist/my-app.jar`，包含所有依赖和前端资源。

运行：

```bash
java -jar dist/my-app.jar
```

## 下一步

- [项目结构](/guide/project-structure) - 目录约定
- [依赖管理](/guide/dependencies) - Maven 依赖和本地包
- [前端集成](/guide/frontend) - Vite 配置详解
