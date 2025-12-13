# 快速开始

5 分钟上手 Qin。

## 安装

一条命令安装 Qin（自动安装 Bun）：

::: code-group

```bash [macOS / Linux]
curl -fsSL https://qinjs.dev/install.sh | bash
```

```powershell [Windows]
irm https://qinjs.dev/install.ps1 | iex
```

:::

安装完成后验证：

```bash
qin --version
```

## 前置要求

- **JDK 17+**（Java 项目需要）- [下载 Adoptium](https://adoptium.net/)

::: tip 提示
Bun 和 Coursier 会在安装 Qin 时自动安装，无需手动配置。
:::

## 创建项目

```bash
# 初始化新项目
qin init

# 或手动创建
mkdir my-app && cd my-app
```

创建 `qin.config.ts`：

```ts
import { defineConfig } from "qin";

export default defineConfig({
  name: "my-app",
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
  },
});
```

创建 `src/server/Main.java`：

```java
package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from Qin!";
    }
}
```

## 运行

```bash
# 开发模式
qin dev

# 或直接运行
qin run
```

访问 http://localhost:8080/api/hello

## 添加前端

创建 `src/client/index.html`：

```html
<!DOCTYPE html>
<html>
<head>
  <title>My App</title>
</head>
<body>
  <h1>Hello Qin!</h1>
  <script type="module" src="/main.ts"></script>
</body>
</html>
```

创建 `src/client/main.ts`：

```ts
const res = await fetch("/api/hello");
const text = await res.text();
console.log(text);
```

Qin 会自动检测 `src/client` 并启用 Vite：

```bash
qin dev
# 后端: http://localhost:8080
# 前端: http://localhost:5173
```

## 构建

```bash
qin build
```

生成 `dist/my-app.jar`，包含前端静态资源。

## 下一步

- [配置指南](./configuration.md) - 完整配置选项
- [CLI 命令](./cli.md) - 所有命令详解
- [示例项目](../examples) - 更多示例
