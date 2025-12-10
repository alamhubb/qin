# Design Document

## Overview

Qin 是一个基于 Bun 的跨语言构建和包管理系统，核心目标是让开发者能够像使用原生 JavaScript 模块一样使用 Java 代码。系统通过 WebAssembly 作为中间层，实现 Java 到 JavaScript 的无缝互操作。

### 核心设计理念

1. **零配置体验** - 直接 `import { MyClass } from "./MyClass.java"` 即可使用
2. **透明编译** - Bun 自动处理 Java → WASM 的编译和缓存
3. **原生调用感** - Java 类在 JS 中的使用方式与原生 JS 类一致

### 技术选型与抉择

| 组件 | 选择 | 候选方案 | 选择理由 |
|------|------|----------|----------|
| Java → WASM 编译器 | **TeaVM** | CheerpJ, GraalVM, JWebAssembly | 开源免费、轻量、专为 WASM 优化、生成体积小 |
| 运行时 | **Bun** | Node.js, Deno | 原生 WASM 支持、Plugin 系统、性能优秀 |
| .java 加载方式 | **Bun Plugin** | 预处理脚本、运行时编译 | 透明集成、用户无感知 |
| 方法导出策略 | **自动导出 public** | @Export 注解、配置文件 | 零侵入、符合 Java 语义 |
| 缓存策略 | **源文件 hash** | 修改时间、总是重编译 | 准确判断变更、避免无效编译 |
| 类型生成 | **自动生成 .d.ts** | 手动维护、无类型 | TypeScript 类型安全 |

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Qin System                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐       │
│  │   CLI Layer  │    │  Bun Plugin  │    │  TypeScript  │       │
│  │  (qin java)  │    │  (.java      │    │    API       │       │
│  │              │    │   loader)    │    │              │       │
│  └──────┬───────┘    └──────┬───────┘    └──────┬───────┘       │
│         │                   │                   │                │
│         ▼                   ▼                   ▼                │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                    Core Services                         │    │
│  ├─────────────────┬─────────────────┬─────────────────────┤    │
│  │   JavaBuilder   │  JavaPackage    │    WasmBridge       │    │
│  │                 │    Manager      │                     │    │
│  │  - compile()    │  - add()        │  - compileToWasm()  │    │
│  │  - run()        │  - install()    │  - loadModule()     │    │
│  │  - findFiles()  │  - download()   │  - createProxy()    │    │
│  └────────┬────────┴────────┬────────┴──────────┬──────────┘    │
│           │                 │                   │                │
│           ▼                 ▼                   ▼                │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │                   External Tools                         │    │
│  ├─────────────────┬─────────────────┬─────────────────────┤    │
│  │     javac       │  Maven Central  │      TeaVM          │    │
│  │   (compile)     │   (download)    │   (java→wasm)       │    │
│  └─────────────────┴─────────────────┴─────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 数据流

**构建阶段（Build Time）：**
```
┌──────────┐     ┌──────────┐     ┌──────────┐
│  .java   │────▶│  .class  │────▶│  .wasm   │
│  source  │     │ bytecode │     │  module  │
└──────────┘     └──────────┘     └──────────┘
     │                │                │
     │   javac        │    TeaVM       │
     ▼                ▼                ▼
  编写代码         编译字节码       生成WASM
```

**运行阶段（Runtime）- Java 不参与：**
```
┌──────────┐     ┌──────────────┐     ┌──────────┐
│   .ts    │────▶│  Bun WASM    │────▶│  Result  │
│  code    │     │  Runtime     │     │          │
└──────────┘     └──────────────┘     └──────────┘
     │                  │
     │  import .java    │  加载 .wasm
     │  (触发loader)    │  调用导出函数
     ▼                  ▼
  JS 代码           WASM 执行
```

**关键点：运行时只有 JS ↔ WASM 交互，Java/javac 只在构建时使用。**

## Components and Interfaces

### 1. JavaBuilder (已有，需增强)

```typescript
interface JavaBuildConfig {
  srcDir: string;           // 源码目录，默认 "src/main/java"
  outDir: string;           // 输出目录，默认 "build/classes"
  wasmOutDir: string;       // WASM 输出目录，默认 "build/wasm"
  mainClass?: string;       // 主类
  classpath?: string[];     // 类路径
  javaVersion?: string;     // Java 版本，默认 "17"
}

class JavaBuilder {
  compile(files?: string[]): Promise<boolean>;
  run(mainClass?: string, args?: string[]): Promise<void>;
  compileAndRun(mainClass?: string, args?: string[]): Promise<void>;
  compileToWasm(className: string): Promise<WasmCompileResult>;
}

interface WasmCompileResult {
  success: boolean;
  wasmPath?: string;
  jsGluePath?: string;
  dtsPath?: string;
  error?: string;
}
```

### 2. JavaPackageManager (已有，需修复类型)

```typescript
interface JavaDependency {
  groupId: string;
  artifactId: string;
  version: string;
  scope?: "compile" | "runtime" | "test" | "provided";
}

interface JavaProject {
  name: string;
  version: string;
  mainClass?: string;
  dependencies: JavaDependency[];
  repositories?: string[];
}

class JavaPackageManager {
  add(dep: string): Promise<boolean>;
  install(): Promise<boolean>;
  list(): void;
  getClasspath(): string[];
}
```

### 3. WasmBridge (新组件)

```typescript
interface WasmBridgeConfig {
  cacheDir: string;         // WASM 缓存目录
  teavmPath: string;        // TeaVM JAR 路径
  debug: boolean;           // 调试模式
}

interface JavaClassProxy {
  // 静态方法通过类名直接调用
  [methodName: string]: (...args: any[]) => any;
  // 构造函数
  new(...args: any[]): JavaInstanceProxy;
}

interface JavaInstanceProxy {
  // 实例方法
  [methodName: string]: (...args: any[]) => any;
  // 实例字段
  [fieldName: string]: any;
}

class WasmBridge {
  constructor(config?: Partial<WasmBridgeConfig>);
  
  // 编译 Java 类到 WASM
  compileClass(javaFilePath: string): Promise<WasmCompileResult>;
  
  // 加载 WASM 模块并返回代理对象
  loadClass(className: string): Promise<JavaClassProxy>;
  
  // 检查是否需要重新编译
  needsRecompile(javaFilePath: string): Promise<boolean>;
  
  // 清理缓存
  clearCache(): Promise<void>;
}
```

### 4. Bun Plugin (新组件)

```typescript
// 注册 .java 文件的 loader
const javaPlugin: BunPlugin = {
  name: "java-loader",
  setup(build) {
    build.onLoad({ filter: /\.java$/ }, async (args) => {
      const bridge = new WasmBridge();
      
      // 检查缓存
      if (await bridge.needsRecompile(args.path)) {
        await bridge.compileClass(args.path);
      }
      
      // 返回加载 WASM 的 JS 代码
      return {
        contents: generateLoaderCode(args.path),
        loader: "js",
      };
    });
  },
};
```

### 5. CLI Interface

```
qin java <command> [options]

Commands:
  compile              编译所有 Java 源文件
  run <MainClass>      运行指定的主类
  build                编译并运行
  wasm <ClassName>     将指定类编译为 WASM
  
  add <dependency>     添加 Maven 依赖
  install              安装所有依赖
  list                 列出所有依赖

Options:
  --src <dir>          源码目录 (默认: src/main/java)
  --out <dir>          输出目录 (默认: build)
  --java <version>     Java 版本 (默认: 17)
```

## Data Models

### 项目配置文件 (qin.java.json)

```json
{
  "name": "my-project",
  "version": "1.0.0",
  "mainClass": "com.example.Main",
  "java": {
    "version": "17",
    "srcDir": "src/main/java",
    "outDir": "build/classes",
    "wasmOutDir": "build/wasm"
  },
  "dependencies": [
    {
      "groupId": "com.google.guava",
      "artifactId": "guava",
      "version": "32.1.3-jre"
    }
  ],
  "repositories": [
    "https://repo1.maven.org/maven2"
  ]
}
```

### WASM 模块元数据 (.wasm.meta.json)

```json
{
  "className": "hello",
  "sourceHash": "abc123...",
  "compiledAt": "2025-12-11T10:00:00Z",
  "exports": {
    "staticMethods": [
      {
        "name": "main",
        "params": [{ "name": "args", "type": "String[]" }],
        "returnType": "void"
      }
    ],
    "instanceMethods": [],
    "constructors": [],
    "fields": []
  }
}
```

### 类型映射表 (JS ↔ WASM)

运行时类型转换发生在 JS 和 WASM 之间，不涉及 Java：

| 原 Java Type | JavaScript Type | WASM Type | 转换方式 |
|--------------|-----------------|-----------|----------|
| byte, short, int | number | i32 | 直接 |
| long | bigint | i64 | 直接 |
| float | number | f32 | 直接 |
| double | number | f64 | 直接 |
| boolean | boolean | i32 | 0/1 |
| char | string | i32 | charCodeAt |
| String | string | i32 (ptr) | WASM 线性内存读写 |
| Object | Proxy | i32 (ptr) | JS 代理包装 WASM 指针 |
| int[] | Int32Array | i32 (ptr) | TypedArray 视图 |
| Object[] | Array | i32 (ptr) | 逐元素转换 |

**注意：** 类型信息在编译时从 Java 字节码提取，生成 TypeScript 类型定义文件 (.d.ts)，运行时 JS 直接与 WASM 交互。



## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

基于需求分析，以下是系统必须满足的正确性属性：

### Property 1: 项目配置序列化往返一致性
*For any* 有效的 JavaProject 对象，序列化为 JSON 后再反序列化，应该得到等价的对象。
**Validates: Requirements 4.5, 4.6**

### Property 2: 依赖去重一致性
*For any* 依赖列表，添加相同 groupId:artifactId 但不同 version 的依赖多次后，列表中应该只有一个该依赖的条目，且版本为最后添加的版本。
**Validates: Requirements 4.2**

### Property 3: 依赖格式验证
*For any* 不符合 `groupId:artifactId:version` 格式的字符串，add 方法应该返回失败并拒绝添加。
**Validates: Requirements 4.4**

### Property 4: Classpath 分隔符平台一致性
*For any* 路径列表，在 Windows 上构建的 classpath 字符串应该使用分号 (;) 分隔，在 Unix 系统上应该使用冒号 (:) 分隔。
**Validates: Requirements 8.1, 8.2**

### Property 5: Classpath 完整性
*For any* 依赖集合，构建的 classpath 应该包含输出目录和所有依赖 JAR 的路径。
**Validates: Requirements 2.4**

### Property 6: 编译文件计数一致性
*For any* 源目录中的 .java 文件集合，编译成功后报告的文件数应该等于实际编译的文件数。
**Validates: Requirements 1.3**

### Property 7: Public 方法自动导出
*For any* Java 类，所有 public 方法应该出现在 WASM 模块的导出列表中，private 和 protected 方法不应该出现。
**Validates: Requirements 9.5, 11.1, 11.2**

### Property 8: Public 字段自动暴露
*For any* Java 类，所有 public 字段应该作为 JavaScript 对象的属性可访问。
**Validates: Requirements 11.5**

### Property 9: 类型转换往返一致性
*For any* 支持的基本类型值 (number, string, boolean)，从 JS 传递到 WASM 再返回后，应该得到等价的值。
**Validates: Requirements 10.2, 10.3, 10.5**

### Property 10: 缓存一致性
*For any* Java 源文件，如果源文件内容未改变，则应该使用缓存的 WASM 模块；如果源文件内容改变，则应该重新编译。
**Validates: Requirements 13.2, 13.3**

### Property 11: WASM 实例共享
*For any* Java 类被多个 TypeScript 文件导入时，应该共享同一个 WASM 实例。
**Validates: Requirements 13.4**

### Property 12: 依赖列表显示完整性
*For any* 项目依赖列表，list 命令的输出应该包含所有依赖，每个依赖都以 `groupId:artifactId:version` 格式显示。
**Validates: Requirements 6.2**

## Error Handling

### 编译错误
- Java 编译失败：捕获 javac 错误输出，格式化后显示给用户
- TeaVM 编译失败：捕获 TeaVM 错误，提供可能的解决方案提示
- 文件不存在：明确提示缺失的文件路径

### 运行时错误
- WASM 加载失败：检查文件完整性，提示重新编译
- 类型转换失败：提供详细的类型不匹配信息
- Java 异常：转换为 JS Error，保留原始堆栈信息

### 依赖管理错误
- 网络错误：提示检查网络连接，支持重试
- 依赖不存在：提示检查依赖坐标是否正确
- 版本冲突：显示冲突的依赖版本

### 错误码定义

| 错误码 | 含义 | 处理建议 |
|--------|------|----------|
| E001 | Java 编译失败 | 检查语法错误 |
| E002 | 找不到 Java 文件 | 检查源目录配置 |
| E003 | 找不到主类 | 指定 mainClass 或配置 |
| E004 | TeaVM 编译失败 | 检查不支持的 API |
| E005 | WASM 加载失败 | 重新编译 |
| E006 | 依赖下载失败 | 检查网络和坐标 |
| E007 | 配置文件解析失败 | 检查 JSON 格式 |
| E008 | 类型转换失败 | 检查参数类型 |

## Testing Strategy

### 测试框架选择

- **单元测试**: Bun 内置测试框架 (`bun test`)
- **属性测试**: fast-check (JavaScript 属性测试库)
- **集成测试**: Bun 测试框架 + 实际 Java 编译

### 单元测试覆盖

1. **JavaBuilder**
   - 文件发现逻辑
   - Classpath 构建
   - 配置合并

2. **JavaPackageManager**
   - 依赖解析
   - 版本更新逻辑
   - 配置序列化/反序列化

3. **WasmBridge**
   - 缓存检查逻辑
   - 类型转换函数
   - 元数据提取

### 属性测试覆盖

每个属性测试必须：
- 运行至少 100 次迭代
- 使用注释标记对应的正确性属性
- 格式：`**Feature: cross-language-build-system, Property {number}: {property_text}**`

关键属性测试：
1. 配置序列化往返 (Property 1)
2. 依赖去重 (Property 2)
3. 依赖格式验证 (Property 3)
4. Classpath 分隔符 (Property 4)
5. 类型转换往返 (Property 9)

### 集成测试

1. 端到端编译流程
2. WASM 模块加载和调用
3. CLI 命令执行
4. 依赖下载和安装
