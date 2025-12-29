# Qin 项目清理计划

## 目标
删除已经在 Java 中实现的 TypeScript/Bun/Node.js 代码，保持纯 Java 25 实现。

## ✅ 保留的文件和目录

### 构建和文档
- `README.md` - 主文档
- `build-java.bat` - Windows 构建脚本
- `build-java.sh` - Linux/macOS 构建脚本
- `JAVA25_REWRITE_PLAN.md` - Java 25 重写计划
- `JAVA25_PROGRESS.md` - 进度文档
- `FINAL_SUMMARY.md` - 最终总结
- `STATUS_REPORT.md` - 状态报告
- `SUCCESS_REPORT.md` - 成功报告
- `NEXT_STEPS.md` - 下一步计划
- `TASKS.md` - 任务列表

### Java 源码
- `src/java-rewrite/` - **纯 Java 25 实现（保留）**
- `lib/` - Java 依赖（gson）

### 示例和文档
- `examples/` - 示例项目（可能包含 Java 和 TS 示例）
- `docs/` - 文档目录
- `.qin/` - 构建输出目录

## ❌ 删除的文件（已在 Java 中实现）

### TypeScript 源码（src/ 目录下）
- [x] `src/cli.ts` - CLI 入口 → 已有 `QinCli.java`
- [x] `src/types.ts` - 类型定义 → 已有 `com/qin/types/*.java`
- [x] `src/index.ts` - 主入口
- [x] `src/qin.ts`
- [x] `src/plugin.ts` - 插件系统 → 已有 `PluginManager.java`
- [x] `src/A.ts`, `src/B.ts` - 测试文件
- [x] `src/hello.java`, `src/index.java` - 临时文件

### TypeScript 核心模块（src/core/）
- [x] `src/core/config-loader.ts` → `ConfigLoader.java`
- [x] `src/core/dependency-resolver.ts` → `DependencyResolver.java`
- [x] `src/core/environment.ts` → `EnvironmentChecker.java`
- [x] `src/core/fat-jar-builder.ts` → `FatJarBuilder.java`
- [x] `src/core/java-runner.ts` → `JavaRunner.java`
- [x] `src/core/plugin-system.ts` → `PluginManager.java`
- [x] `src/core/plugin-detector.ts` → `PluginDetector.java`
- [x] `src/core/workspace-loader.ts` → `WorkspaceLoader.java`
- [x] 其他 TypeScript 核心模块

### TypeScript 命令（src/commands/）
- [x] `src/commands/init.ts` → `InitCommand.java`
- [x] `src/commands/env.ts` → `EnvCommand.java`
- [x] 其他 TypeScript 命令

### TypeScript Java 工具（src/java/）
- [x] `src/java/package-manager.ts` → `PackageManager.java`
- [x] `src/java/classpath.ts` → `ClasspathUtils.java`
- [x] `src/java/builder.ts` → `JavaBuilder.java`

### Node.js/Bun 配置和依赖
- [x] `package.json` - Node.js 包配置
- [x] `package-lock.json` - npm 锁文件
- [x] `bun.lock` - Bun 锁文件
- [x] `tsconfig.json` - TypeScript 配置
- [x] `node_modules/` - Node.js 依赖目录
- [x] `index.ts` - 根目录的 TS 入口

### 其他待删除
- [x] `src/plugins/` - TS 插件（如果有 Java 版本）
- [x] `src/wasm/` - WebAssembly 相关（非 Java）
- [x] `.vscode/` - VSCode 配置（可选保留）

## 📋 执行步骤

1. **备份**（可选）
   ```bash
   # 创建备份分支
   git branch backup-typescript
   ```

2. **删除 TypeScript 源码**
   ```bash
   rm -rf src/cli.ts src/types.ts src/core/ src/commands/ src/java/
   rm -rf src/*.ts src/plugins/ src/wasm/
   ```

3. **删除 Node.js/Bun 配置**
   ```bash
   rm package.json package-lock.json bun.lock tsconfig.json
   rm -rf node_modules/
   rm index.ts
   ```

4. **更新构建脚本**
   - 确保 `build-java.bat` 和 `build-java.sh` 是最新的
   - 它们应该只编译 `src/java-rewrite/` 下的 Java 代码

5. **更新 README**
   - 已经是纯 Java 25 文档 ✅
   - 无需修改

6. **验证**
   ```bash
   # 编译 Java 版本
   ./build-java.bat
   
   # 运行测试
   java -cp ".qin/classes;lib/gson-2.10.1.jar" com.qin.cli.QinCli help
   ```

## ✅ Java 版本功能清单

已在 Java 中实现的功能：

### CLI 命令
- [x] `qin compile` - 编译项目
- [x] `qin run` - 运行项目
- [x] `qin build` - 构建 Fat JAR
- [x] `qin dev` - 开发模式
- [x] `qin clean` - 清理构建
- [x] `qin sync` - 同步依赖
- [x] `qin test` - 运行测试（基础实现）
- [x] `qin init` - 初始化项目
- [x] `qin env` - 环境检查

### 核心模块
- [x] ConfigLoader - 配置加载
- [x] DependencyResolver - 依赖解析（Coursier）
- [x] EnvironmentChecker - 环境检查
- [x] JavaRunner - Java 运行器
- [x] FatJarBuilder - Fat JAR 构建
- [x] PluginManager - 插件管理
- [x] PluginDetector - 插件检测
- [x] WorkspaceLoader - 工作区加载

### 类型系统
- [x] 41 个 Java Records（`com/qin/types/`）
- [x] 完整的配置类型
- [x] 构建结果类型
- [x] 环境状态类型

## 状态

- 创建时间: 2025-12-29
- 执行人: AI Assistant
- 状态: 待执行
