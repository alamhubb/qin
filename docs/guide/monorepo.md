# Monorepo

Qin 原生支持 Monorepo 多项目工作区。

## 目录结构

```
workspace/
├── qin.config.ts          # 工作区配置
├── apps/
│   ├── web-app/
│   │   ├── qin.config.ts
│   │   └── src/
│   └── api-server/
│       ├── qin.config.ts
│       └── src/
└── packages/
    ├── java-base/
    │   ├── qin.config.ts
    │   └── src/
    └── shared-utils/
        ├── qin.config.ts
        └── src/
```

## 工作区配置

```ts
// 根目录 qin.config.ts
export default defineConfig({
  packages: [
    "apps/*",
    "packages/*",
  ],
});
```

支持 glob 模式：

| 模式 | 匹配 |
|------|------|
| `apps/*` | apps 下所有直接子目录 |
| `packages/**` | packages 下所有目录（递归） |
| `libs/java-*` | libs 下以 java- 开头的目录 |

## 本地包引用

在应用中引用本地包：

```ts
// apps/web-app/qin.config.ts
export default defineConfig({
  name: "web-app",
  dependencies: {
    // Maven 依赖
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
    // 本地包（从 packages/ 解析）
    "java-base": "*",
    "shared-utils": "^1.0.0",
  },
});
```

本地包版本规则：
- `*` - 任意版本，直接使用
- `^1.0.0` - 检查本地包版本是否兼容

## 本地包配置

```ts
// packages/java-base/qin.config.ts
export default defineConfig({
  name: "java-base",
  version: "1.0.0",
  
  // 本地包也可以有依赖
  dependencies: {
    "com.google.guava:guava": "32.1.3-jre",
  },
});
```

## 运行命令

### 运行单个项目

```bash
# 在项目目录下
cd apps/web-app
qin run
```

### 从根目录运行

```bash
# 指定项目（计划中）
qin run --project web-app
```

## 依赖解析

Qin 解析依赖时：

1. 检查是否为本地包（在 packages/ 中查找）
2. 如果是本地包，编译并添加到 classpath
3. 如果是 Maven 依赖，从仓库下载

```
web-app 依赖解析:
├── java-base (本地) → packages/java-base/
│   └── guava (Maven) → ~/.qin/repository/
└── spring-boot (Maven) → ~/.qin/repository/
```

## 共享配置

使用 TypeScript 共享配置：

```ts
// shared/config.ts
export const commonDeps = {
  "org.projectlombok:lombok": "1.18.30",
  "org.slf4j:slf4j-api": "2.0.9",
};
```

```ts
// apps/web-app/qin.config.ts
import { commonDeps } from "../../shared/config";

export default defineConfig({
  dependencies: {
    ...commonDeps,
    "org.springframework.boot:spring-boot-starter-web": "3.2.0",
  },
});
```

## 最佳实践

1. **包命名** - 使用有意义的名称：`java-base`, `shared-utils`
2. **版本管理** - 本地包使用语义化版本
3. **依赖提升** - 公共依赖放在共享包中
4. **独立测试** - 每个包应该可以独立测试
