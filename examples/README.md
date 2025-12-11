# Qin Examples - Monorepo 示例

这是一个 Qin Monorepo 示例，展示多项目管理功能。

## 目录结构

```
examples/
├── qin.config.ts          # Workspace 配置
├── apps/
│   └── hello-java/        # 主应用（Spring Boot）
│       ├── qin.config.ts
│       └── src/
│           ├── server/Main.java
│           └── client/    # 前端
└── packages/
    └── java-base/         # 共享库
        ├── qin.config.ts
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

### 3. 构建

```bash
cd apps/hello-java
qin build
```

## 本地包依赖

`hello-java` 依赖 `java-base` 包：

```typescript
// apps/hello-java/qin.config.ts
dependencies: {
  "org.springframework.boot:spring-boot-starter-web": "3.2.0",
  "java-base": "*",  // 自动从 packages/java-base 加载
}
```

Qin 会自动检测 `packages` 配置中的本地项目，优先使用本地编译产物。
