# Qin - Cross-language Build System

Qin 是一个基于 Bun 的跨语言构建和包管理系统，让你可以像使用原生 JavaScript 模块一样使用 Java 代码。

## 特性

- 🚀 **直接运行 Java** - `qin hello.java` 一键编译运行
- 📦 **npm 风格包管理** - `qin add package@version`
- 🔄 **Java → WASM** - 将 Java 编译为 WebAssembly
- 🔌 **Bun Plugin** - 直接 `import { MyClass } from "./MyClass.java"`
- 📝 **TypeScript 支持** - 自动生成类型定义

## 安装

```bash
bun install
```

## 快速开始

### 运行 Java 文件

```bash
# 直接运行 Java 文件
qin src/hello.java

# 带参数运行
qin src/hello.java arg1 arg2
```

### 包管理

```bash
# 添加依赖
qin add lodash@4.17.21

# 添加开发依赖
qin add -D typescript

# 安装所有依赖
qin install

# 列出依赖
qin list
```

### Java 构建命令

```bash
# 编译所有 Java 文件
qin java compile

# 运行指定主类
qin java run MainClass

# 编译并运行
qin java build

# 编译为 WASM
qin java wasm src/Hello.java
```

## 项目结构

```
my-project/
├── src/
│   └── Hello.java
├── package.json          # 依赖配置
└── qin.config.ts         # Qin 配置
```

### qin.config.ts

```typescript
import type { QinConfig } from "qin";

// Most settings use sensible defaults, only configure what you need
const config: QinConfig = {
  // Java version (default: "17")
  javaVersion: "17",
  // Main class for `qin run` (default: "Main")
  mainClass: "Main",
};

export default config;
```

默认路径（无需配置）：
- 源码目录: `src/`
- 编译输出: `.qin/classes/`
- WASM 输出: `.qin/wasm/`

## 在 TypeScript 中使用 Java

```typescript
// 使用 Bun Plugin 直接导入 Java 类
import { Hello } from "./Hello.java";

// 调用静态方法
const result = await Hello.add(1, 2);
console.log(result); // 3

// 调用实例方法
const greeting = await Hello.greet("World");
console.log(greeting); // "Hello, World!"
```

## API

### JavaBuilder

```typescript
import { JavaBuilder } from "qin";

const builder = new JavaBuilder({
  srcDir: "src",
  outDir: "build/classes",
  mainClass: "Main",
});

await builder.compile();
await builder.run("Main", ["arg1"]);
```

### QinPackageManager

```typescript
import { QinPackageManager } from "qin";

const pm = new QinPackageManager();
await pm.add("lodash@4.17.21");
await pm.install();
pm.list();
```

### WasmBridge

```typescript
import { WasmBridge } from "qin";

const bridge = new WasmBridge({
  wasmOutDir: "build/wasm",
});

const result = await bridge.compileClass("src/Hello.java");
console.log(result.wasmPath);
```

## 开发

```bash
# 运行测试
bun test

# 运行 CLI
bun run qin --help
```

## License

MIT
