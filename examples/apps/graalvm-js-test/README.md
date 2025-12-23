# GraalVM JavaScript Test Project

这个项目演示了 Qin 的 GraalVM JavaScript 支持。

## 两种运行方式

### 方案 1: Polyglot API (`qin run`)

```bash
qin run src/server/index.js
```

- ✅ 任何 Java 11+ 都能运行
- ✅ 内置 Java 互操作 (`Java.type()`)
- ❌ 不支持 Node.js 内置模块 (`fs`, `http`, `path` 等)
- ❌ 不支持 npm 生态

### 方案 2: GraalVM Node.js (`qin gral`)

```bash
# 基本运行
qin gral src/server/index.js

# 启用 Java 互操作
qin gral --polyglot src/server/index.js
```

- ✅ 完整的 Node.js API 支持
- ✅ npm 生态兼容
- ✅ Java 互操作（需要 `--polyglot` 参数）
- ❌ 需要安装 GraalVM 并运行 `gu install nodejs`

## 安装 GraalVM Node.js

1. 下载 GraalVM: https://www.graalvm.org/downloads/
2. 设置环境变量:
   ```bash
   # Windows
   set GRAALVM_HOME=C:\path\to\graalvm
   set PATH=%GRAALVM_HOME%\bin;%PATH%
   
   # Linux/macOS
   export GRAALVM_HOME=/path/to/graalvm
   export PATH=$GRAALVM_HOME/bin:$PATH
   ```
3. 安装 Node.js 组件:
   ```bash
   gu install nodejs
   ```

## 检查环境

```bash
# 查看 GraalVM 环境信息
qin gral --info
```

## 配置文件

`qin.config.ts`:

```typescript
import { defineConfig } from "qin";

export default defineConfig({
  name: "graalvm-js-test",
  graalvm: {
    js: {
      entry: "src/server/index.js",
      hotReload: true,
      javaInterop: false, // 设为 true 启用 Java 互操作
    },
  },
});
```

## 使用场景

| 场景 | 推荐方案 |
|------|---------|
| 简单脚本 + Java 互操作 | `qin run` |
| HTTP 服务器 | `qin gral` |
| 使用 npm 包 | `qin gral` |
| Express/Koa 应用 | `qin gral` |
| 纯计算脚本 | `qin run` |
