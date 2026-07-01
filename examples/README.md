# Qin Examples - Monorepo 示例

这是一个 Qin Monorepo 示例，展示多项目管理功能。

## 目录结构

```
examples/
├── qin.config.js          # Workspace 配置
├── apps/
│   └── hello-java/        # 主应用（Spring Boot）
│       ├── qin.config.js
│       └── src/
│           ├── server/Main.java
│           └── client/    # 前端
│   └── qin-hello-web/     # 最小 Web hello 示例
│       ├── qin.config.js
│       └── main/main.ts
└── packages/
    └── java-base/         # 共享库
        ├── qin.config.js
        └── src/Utils.java
```

## 使用方法

### 1. 同步依赖

```bash
cd examples
qin sync
```

### 2. 运行应用

```bash
cd apps/hello-java
qin run
```

运行说明：

- `src/server/HelloController.qin` 是 controller 业务代码
- `src/server/HelloService.qin` 是 service 业务代码
- `src/server/Main.java` 只是 Spring Boot host shell
- 默认使用随机空闲端口，启动成功后控制台会打印实际 URL
- 根路径 `/` 返回 demo 说明，主要接口在 `/api/*`

示例接口：

- `GET /api/hello`
- `GET /api/hello/detail`
- `GET /api/ping`
- `POST /api/greet`
- `POST /api/greet/loud`

### 3. 构建

```bash
cd apps/hello-java
qin build
```

## 本地包依赖

`hello-java` 依赖 `java-base` 包：

```javascript
// apps/hello-java/qin.config.js
dependencies: {
  "org.springframework.boot:spring-boot-starter-web": "4.0.6",
  "java-base": "*",  // 自动从 packages/java-base 加载
}
```

Qin 会自动检测 `packages` 配置中的本地项目，优先使用本地编译产物。

## 最小 Web Hello

如果只是想验证 IDEA Qin 插件能识别项目、显示脚本并启动一个 Web 服务，可以打开：

```text
examples/apps/qin-hello-web
```

右侧 Qin 面板会显示 `dev`、`start`、`check` 脚本。点击 `dev` 后访问：

```text
http://127.0.0.1:19131/
```

响应内容是：

```text
hello
```
