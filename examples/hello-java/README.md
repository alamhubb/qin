# Qin 全栈示例

这是一个使用 Qin 构建的全栈 Java 应用示例，展示了：

- 🚀 Spring Boot 后端 API 服务
- 🎨 原生前端（HTML + CSS + JavaScript）
- 📦 一键打包部署

## 项目结构

```
hello-java/
├── qin.config.ts          # Qin 配置（替代 pom.xml + package.json）
├── src/
│   ├── server/            # Java 后端
│   │   └── Main.java      # Spring Boot 应用
│   └── client/            # 前端源码
│       ├── index.html
│       ├── style.css
│       └── main.js
└── dist/
    ├── hello-app.jar      # Fat Jar
    └── static/            # 前端静态资源
```

## 快速开始

### 1. 运行开发服务器

```bash
cd examples/hello-java

# 编译并运行 Spring Boot 后端
qin run
```

然后在浏览器打开 `http://localhost:8080`

### 2. 构建生产版本

```bash
qin build
```

生成的 `dist/hello-app.jar` 可以直接运行：

```bash
java -jar dist/hello-app.jar
```

## API 接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/time` | GET | 获取服务器时间 |
| `/api/greet` | POST | 问候接口，接收 `{name: string}` |
| `/api/users` | GET | 获取用户列表 |

## 技术栈

- **后端**: Spring Boot 3.2 (Spring Web)
- **前端**: 原生 HTML/CSS/JavaScript
- **构建**: Qin (Java-Vite)
- **依赖管理**: Coursier (Maven 依赖解析)

## 配置说明

所有配置都在 `qin.config.ts` 中，无需 `pom.xml` 或 `package.json`：

```typescript
const config: QinConfig = {
  name: "qin-hello-java",
  version: "1.0.0",
  entry: "src/server/Main.java",
  dependencies: [
    "org.springframework.boot:spring-boot-starter-web:3.2.0",
  ],
  frontend: {
    enabled: true,
    srcDir: "src/client",
  },
};
```

## 特点

1. **Spring Boot 注解** - 使用标准 `@RestController`、`@GetMapping` 等注解
2. **零 XML 配置** - 用 TypeScript 配置替代 pom.xml
3. **原生前端** - 使用原生 `fetch` API，无需框架
4. **中文界面** - 完整的中文用户界面
5. **一键部署** - 打包成单个 Fat Jar，包含前端静态资源
