# 构建部署

Qin 支持构建可执行的 Fat Jar，包含所有依赖和前端资源。

## 构建命令

```bash
qin build
```

输出：

```
dist/
├── my-app.jar      # 可执行 Fat Jar
└── static/         # 前端构建产物（如果有）
```

## Fat Jar

Fat Jar 包含：
- 编译后的 Java 类
- 所有 Maven 依赖
- 前端静态资源（如果配置了 client）

运行：

```bash
java -jar dist/my-app.jar
```

## 配置输出

```ts
export default defineConfig({
  output: {
    dir: "build",           // 输出目录，默认 "dist"
    jarName: "server.jar",  // JAR 名称，默认 "{name}.jar"
  },
});
```

## 前端资源

构建时，前端资源会：

1. 由 Vite 构建到 `dist/static/`
2. 打包进 Fat Jar 的 `static/` 目录

Spring Boot 配置静态资源：

```java
// 默认从 classpath:/static/ 提供静态文件
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

## 环境变量

构建时可以注入环境变量：

```bash
# 设置生产环境
NODE_ENV=production qin build
```

## Docker 部署

```dockerfile
# Dockerfile
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY dist/my-app.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

构建镜像：

```bash
qin build
docker build -t my-app .
docker run -p 8080:8080 my-app
```

## 多阶段构建

```dockerfile
# 构建阶段
FROM oven/bun:1 AS builder

WORKDIR /app
COPY . .
RUN bun install
RUN bun run qin build

# 运行阶段
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=builder /app/dist/my-app.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

## JVM 参数

运行时配置 JVM：

```bash
java -Xmx512m -jar dist/my-app.jar
```

常用参数：

| 参数 | 说明 |
|------|------|
| `-Xmx512m` | 最大堆内存 |
| `-Xms256m` | 初始堆内存 |
| `-XX:+UseG1GC` | 使用 G1 垃圾回收器 |
| `-Dserver.port=9090` | 修改端口 |

## CI/CD

### GitHub Actions

```yaml
name: Build

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      
      - uses: oven-sh/setup-bun@v1
      
      - uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
      
      - run: bun install
      - run: bun run qin build
      
      - uses: actions/upload-artifact@v4
        with:
          name: jar
          path: dist/*.jar
```
