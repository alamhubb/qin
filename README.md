# Qin - Java-Vite Build Tool

Qin 是一个基于 Bun + Coursier + JDK 的现代化 Java 构建工具，定位为 "Java 的 Vite"。

## 特性

- 🚀 **零 XML 配置** - 使用 TypeScript 配置文件，告别繁琐的 pom.xml
- ⚡ **极速启动** - 利用 Bun 的高性能和 Coursier 的快速依赖解析
- 📦 **一键运行** - `qin run` 编译并运行 Java 程序
- 🎁 **Fat Jar 打包** - `qin build` 生成包含所有依赖的可执行 JAR
- 🎨 **美观输出** - 彩色终端输出，清晰的进度显示

## 安装

```bash
# 克隆项目
git clone <repo-url>
cd qin

# 安装依赖
bun install

# 链接 CLI（可选）
bun link
```

### 前置要求

- [Bun](https://bun.sh/) - JavaScript 运行时
- [Coursier](https://get-coursier.io/) - Maven 依赖解析器
- [JDK 17+](https://adoptium.net/) - Java 开发工具包

## 快速开始

### 初始化项目

```bash
qin init
```

这会创建：
- `qin.config.ts` - 项目配置文件
- `src/Main.java` - Hello World 示例

### 编译运行

```bash
qin run
```

### 构建 Fat Jar

```bash
qin build
```

生成的 JAR 文件位于 `dist/app.jar`，可以直接运行：

```bash
java -jar dist/app.jar
```

## 配置文件

`qin.config.ts`:

```typescript
import type { QinConfig } from "qin";

export default {
  // 入口文件
  entry: "src/Main.java",
  
  // Maven 依赖
  dependencies: [
    "com.google.guava:guava:32.1.3-jre",
    "org.slf4j:slf4j-api:2.0.9",
  ],
  
  // 输出配置
  output: {
    dir: "dist",
    jarName: "app.jar",
  },
} satisfies QinConfig;
```

## 项目结构

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

## CLI 命令

```bash
qin init              # 初始化新项目
qin run [args...]     # 编译并运行
qin build [--debug]   # 构建 Fat Jar
qin --help            # 显示帮助
```

## API 使用

Qin 也可以作为库使用：

```typescript
import { 
  ConfigLoader, 
  DependencyResolver, 
  JavaRunner, 
  FatJarBuilder 
} from "./src/qin";

// 加载配置
const loader = new ConfigLoader();
const config = await loader.load();

// 解析依赖
const resolver = new DependencyResolver();
const classpath = await resolver.resolve(config.dependencies || []);

// 编译运行
const runner = new JavaRunner(config, classpath);
await runner.compileAndRun();

// 构建 Fat Jar
const builder = new FatJarBuilder(config);
const result = await builder.build();
```

## 开发

```bash
# 运行测试
bun test

# 运行 CLI
bun run src/cli.ts --help
```

## 与 Maven 对比

| 特性 | Maven | Qin |
|------|-------|-----|
| 配置格式 | XML (pom.xml) | TypeScript |
| 启动速度 | 慢 | 快 |
| 依赖解析 | Maven | Coursier |
| Fat Jar | 需要插件 | 内置 |
| 学习曲线 | 陡峭 | 平缓 |

## License

MIT
