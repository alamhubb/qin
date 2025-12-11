# Design Document

## Overview

Qin 是一个基于 Bun + Coursier + JDK 的现代化 Java 构建工具，定位为 "Java 的 Vite"。核心设计理念：

1. **零 XML 配置** - 使用 TypeScript 配置文件，享受类型安全和 IDE 支持
2. **极速启动** - 利用 Bun 的高性能运行时和 Coursier 的快速依赖解析
3. **脚本化 Java** - 像运行脚本一样运行 Java，一键编译执行
4. **内置打包** - Fat Jar 打包能力，无需额外插件

### 技术选型

| 组件 | 选择 | 选择理由 |
|------|------|----------|
| 运行时 | **Bun** | 高性能、原生 TypeScript 支持、内置 Shell API |
| 依赖解析 | **Coursier** | 快速、支持完整 Maven 生态、命令行友好 |
| CLI 解析 | **Commander** | 成熟稳定、API 简洁 |
| 终端美化 | **Chalk** | 轻量、跨平台颜色支持 |
| Java 编译 | **javac** | 标准工具链、无额外依赖 |

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Qin System                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                      CLI Layer                            │   │
│  │  qin init | qin run | qin build                          │   │
│  └──────────────────────────┬───────────────────────────────┘   │
│                             │                                    │
│                             ▼                                    │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    Core Services                          │   │
│  ├────────────────┬─────────────────┬───────────────────────┤   │
│  │  ConfigLoader  │  DependencyResolver  │  JavaRunner      │   │
│  │                │                      │                  │   │
│  │  - load()      │  - resolve()         │  - compile()     │   │
│  │  - validate()  │  - getClasspath()    │  - run()         │   │
│  │                │                      │  - compileAndRun()│   │
│  └───────┬────────┴──────────┬───────────┴────────┬─────────┘   │
│          │                   │                    │              │
│          ▼                   ▼                    ▼              │
│  ┌───────────────┐   ┌───────────────┐   ┌───────────────┐      │
│  │ qin.config.ts │   │   Coursier    │   │    javac      │      │
│  │               │   │   (cs fetch)  │   │    java       │      │
│  └───────────────┘   └───────────────┘   └───────────────┘      │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │                    Build Services                         │   │
│  ├──────────────────────────────────────────────────────────┤   │
│  │  FatJarBuilder                                            │   │
│  │                                                           │   │
│  │  - createTempDir()     - extractJars()                   │   │
│  │  - cleanSignatures()   - compileSource()                 │   │
│  │  - generateManifest()  - packageJar()                    │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 数据流

**开发模式 (qin run):**
```
qin.config.ts → ConfigLoader → DependencyResolver → JavaRunner
                    │                  │                 │
                    ▼                  ▼                 ▼
              解析 entry          cs fetch           javac + java
              解析 deps           返回 classpath      编译并运行
```

**打包模式 (qin build):**
```
qin.config.ts → ConfigLoader → DependencyResolver → FatJarBuilder
                    │                  │                 │
                    ▼                  ▼                 ▼
              解析配置            获取 JAR 路径      解压 → 清理签名
                                                    → 编译 → 打包
```

## Components and Interfaces

### 1. QinConfig (配置接口)

```typescript
interface QinConfig {
  entry: string;           // 入口文件路径，如 "src/Main.java"
  dependencies?: string[]; // 依赖列表，如 ["com.google.guava:guava:32.1.3-jre"]
  output?: {
    dir?: string;          // 输出目录，默认 "dist"
    jarName?: string;      // JAR 文件名，默认 "app.jar"
  };
  java?: {
    version?: string;      // Java 版本，默认 "17"
    sourceDir?: string;    // 源码目录，默认从 entry 推断
  };
}
```

### 2. ConfigLoader

```typescript
class ConfigLoader {
  // 从当前目录加载 qin.config.ts
  async load(): Promise<QinConfig>;
  
  // 验证配置有效性
  validate(config: QinConfig): ValidationResult;
  
  // 从 entry 路径解析源目录和类名
  parseEntry(entry: string): { srcDir: string; className: string };
}

interface ValidationResult {
  valid: boolean;
  errors: string[];
}
```

### 3. EnvironmentChecker

```typescript
class EnvironmentChecker {
  // 检查 Coursier 是否安装
  async checkCoursier(): Promise<boolean>;
  
  // 检查 javac 是否安装
  async checkJavac(): Promise<boolean>;
  
  // 运行所有环境检查
  async checkAll(): Promise<EnvironmentStatus>;
  
  // 获取安装指南
  getInstallGuide(tool: "coursier" | "javac"): string;
}

interface EnvironmentStatus {
  coursier: boolean;
  javac: boolean;
  ready: boolean;
}
```

### 4. DependencyResolver

```typescript
class DependencyResolver {
  // 使用 Coursier 解析依赖并返回 classpath
  async resolve(deps: string[]): Promise<string>;
  
  // 获取所有 JAR 文件路径列表
  async getJarPaths(deps: string[]): Promise<string[]>;
  
  // 构建完整的 classpath（包含输出目录）
  buildClasspath(jarPaths: string[], outputDir: string): string;
}
```

### 5. JavaRunner

```typescript
class JavaRunner {
  constructor(config: QinConfig, classpath: string);
  
  // 编译 Java 源文件
  async compile(): Promise<CompileResult>;
  
  // 运行 Java 程序
  async run(args?: string[]): Promise<void>;
  
  // 编译并运行
  async compileAndRun(args?: string[]): Promise<void>;
}

interface CompileResult {
  success: boolean;
  error?: string;
  compiledFiles: number;
}
```

### 6. FatJarBuilder

```typescript
class FatJarBuilder {
  constructor(config: QinConfig);
  
  // 创建临时工作目录
  async createTempDir(): Promise<string>;
  
  // 解压所有依赖 JAR
  async extractJars(jarPaths: string[], tempDir: string): Promise<void>;
  
  // 清理签名文件
  async cleanSignatures(tempDir: string): Promise<void>;
  
  // 编译源码到临时目录
  async compileSource(tempDir: string, classpath: string): Promise<void>;
  
  // 生成 MANIFEST.MF
  async generateManifest(mainClass: string, tempDir: string): Promise<void>;
  
  // 打包最终 JAR
  async packageJar(tempDir: string, outputPath: string): Promise<void>;
  
  // 完整构建流程
  async build(): Promise<BuildResult>;
  
  // 清理临时目录
  async cleanup(tempDir: string): Promise<void>;
}

interface BuildResult {
  success: boolean;
  outputPath?: string;
  error?: string;
}
```

### 7. CLI Commands

```
qin <command> [options]

Commands:
  init                 初始化新项目
  run [args...]        编译并运行 Java 程序
  build                构建 Fat Jar

Options:
  --debug              保留临时文件用于调试
  --help               显示帮助信息
  --version            显示版本号
```

## Data Models

### 项目配置文件 (qin.config.ts)

```typescript
// qin.config.ts
import type { QinConfig } from "qin";

export default {
  entry: "src/Main.java",
  dependencies: [
    "com.google.guava:guava:32.1.3-jre",
    "org.slf4j:slf4j-api:2.0.9"
  ],
  output: {
    dir: "dist",
    jarName: "app.jar"
  }
} satisfies QinConfig;
```

### 模板文件 (Main.java)

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Qin!");
    }
}
```

### 目录结构

```
my-project/
├── qin.config.ts      # 项目配置
├── src/
│   └── Main.java      # 源代码
├── .qin/
│   ├── classes/       # 编译输出
│   └── temp/          # 构建临时目录
└── dist/
    └── app.jar        # Fat Jar 输出
```

## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system-essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

基于需求分析和 prework，以下是系统必须满足的正确性属性：

### Property 1: 配置序列化往返一致性
*For any* 有效的 QinConfig 对象，序列化为 TypeScript 配置文件后再反序列化，应该得到等价的配置对象。
**Validates: Requirements 2.4, 2.5**

### Property 2: Entry 路径解析正确性
*For any* 有效的 Java 文件路径（如 "src/Main.java"），解析后应该正确提取出源目录（"src"）和类名（"Main"）。
**Validates: Requirements 5.2**

### Property 3: Classpath 分隔符平台一致性
*For any* JAR 路径列表，在 Windows 上构建的 classpath 字符串应该使用分号 (;) 分隔，在 Unix 系统上应该使用冒号 (:) 分隔。
**Validates: Requirements 8.1, 8.2**

### Property 4: 签名文件清理完整性
*For any* 包含签名文件的 JAR 包集合，解压并清理后，临时目录的 META-INF 中不应该存在任何 *.SF、*.DSA、*.RSA 文件。
**Validates: Requirements 6.3**

### Property 5: Manifest 主类正确性
*For any* 主类名，生成的 MANIFEST.MF 文件应该包含正确的 Main-Class 条目。
**Validates: Requirements 6.5**

### Property 6: 依赖 Classpath 完整性
*For any* 依赖列表，Coursier 解析后返回的 classpath 应该包含所有依赖的 JAR 路径。
**Validates: Requirements 4.2**

## Error Handling

### 环境错误
- Coursier 未安装：显示平台特定的安装指南
- javac 未安装：显示 JDK 安装指南
- Java 版本不兼容：提示升级或降级

### 配置错误
- 配置文件不存在：提示运行 `qin init`
- 配置格式错误：显示具体的解析错误位置
- entry 文件不存在：提示检查路径

### 依赖错误
- 依赖坐标格式错误：显示正确格式示例
- 依赖不存在：显示 Coursier 的错误信息
- 网络超时：提示检查网络并重试

### 构建错误
- 编译失败：显示 javac 错误输出
- JAR 解压失败：显示具体文件和错误
- 打包失败：显示 jar 命令错误

### 错误码定义

| 错误码 | 含义 | 处理建议 |
|--------|------|----------|
| E001 | 配置文件不存在 | 运行 qin init |
| E002 | 配置格式错误 | 检查 qin.config.ts 语法 |
| E003 | Coursier 未安装 | 按提示安装 Coursier |
| E004 | javac 未安装 | 安装 JDK |
| E005 | 依赖解析失败 | 检查依赖坐标 |
| E006 | 编译失败 | 检查 Java 代码语法 |
| E007 | 打包失败 | 检查磁盘空间和权限 |

## Testing Strategy

### 测试框架选择

- **单元测试**: Bun 内置测试框架 (`bun test`)
- **属性测试**: fast-check (JavaScript 属性测试库)

### 单元测试覆盖

1. **ConfigLoader**
   - 配置文件加载
   - Entry 路径解析
   - 配置验证

2. **DependencyResolver**
   - Classpath 构建
   - 平台分隔符处理

3. **FatJarBuilder**
   - 签名文件清理
   - Manifest 生成

### 属性测试覆盖

每个属性测试必须：
- 运行至少 100 次迭代
- 使用注释标记对应的正确性属性
- 格式：`**Feature: java-vite-build-tool, Property {number}: {property_text}**`

关键属性测试：
1. 配置序列化往返 (Property 1)
2. Entry 路径解析 (Property 2)
3. Classpath 分隔符 (Property 3)
4. 签名文件清理 (Property 4)
5. Manifest 主类 (Property 5)

### 集成测试

1. 端到端 `qin init` 流程
2. 端到端 `qin run` 流程
3. 端到端 `qin build` 流程

