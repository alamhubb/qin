/**
 * gral 命令 - 使用 GraalVM Node.js 运行 JavaScript
 * 
 * 用法:
 *   qin gral <file.js> [args...]
 *   qin gral --polyglot <file.js>  # 启用 Java 互操作
 * 
 * 这是方案 2: GraalVM Node.js Runtime
 * - 完整的 Node.js API 支持
 * - npm 生态兼容
 * - 需要安装 GraalVM 并运行 `gu install nodejs`
 * 
 * 对比方案 1 (qin run xxx.js):
 * - 方案 1 使用 Polyglot API，任何 Java 11+ 都能运行
 * - 方案 1 不支持 Node.js 内置模块
 */

import chalk from "chalk";
import { join } from "path";
import { existsSync } from "fs";
import { spawnSync } from "child_process";

interface GralOptions {
  polyglot?: boolean;
  jvm?: boolean;
  verbose?: boolean;
}

/**
 * GraalVM 检测结果
 */
interface GraalVMDetection {
  found: boolean;
  nodePath?: string;
  home?: string;
  version?: string;
  components?: string[];
  error?: string;
}

/**
 * 获取可执行文件扩展名
 */
function getExeExtension(): string {
  return process.platform === "win32" ? ".exe" : "";
}

/**
 * 检测 GraalVM 安装
 */
async function detectGraalVM(): Promise<GraalVMDetection> {
  const exe = getExeExtension();

  // 1. 检查 GRAALVM_HOME
  const graalvmHome = process.env.GRAALVM_HOME;
  if (graalvmHome && existsSync(graalvmHome)) {
    const nodePath = join(graalvmHome, "bin", `node${exe}`);
    const guPath = join(graalvmHome, "bin", `gu${exe}`);
    
    if (existsSync(nodePath)) {
      const version = await getNodeVersion(nodePath);
      const components = existsSync(guPath) ? getInstalledComponents(guPath) : [];
      return { found: true, nodePath, home: graalvmHome, version, components };
    }
  }

  // 2. 检查 JAVA_HOME 是否是 GraalVM
  const javaHome = process.env.JAVA_HOME;
  if (javaHome && existsSync(javaHome)) {
    const nodePath = join(javaHome, "bin", `node${exe}`);
    const guPath = join(javaHome, "bin", `gu${exe}`);
    
    // 只有当 gu 命令存在时才认为是 GraalVM
    if (existsSync(guPath) && existsSync(nodePath)) {
      const version = await getNodeVersion(nodePath);
      const components = getInstalledComponents(guPath);
      return { found: true, nodePath, home: javaHome, version, components };
    }
  }

  return {
    found: false,
    error: "GraalVM Node.js 未找到。请设置 GRAALVM_HOME 环境变量并安装 nodejs 组件 (gu install nodejs)"
  };
}

/**
 * 获取已安装的 GraalVM 组件
 */
function getInstalledComponents(guPath: string): string[] {
  try {
    const result = spawnSync(guPath, ["list"], { encoding: "utf-8" });
    if (result.status === 0) {
      const components: string[] = [];
      const lines = (result.stdout || "").split("\n");
      for (const line of lines) {
        if (!line.trim() || line.includes("ComponentId") || line.startsWith("-")) {
          continue;
        }
        const parts = line.trim().split(/\s+/);
        if (parts.length > 0 && parts[0]) {
          components.push(parts[0]);
        }
      }
      return components;
    }
  } catch {
    // 忽略错误
  }
  return [];
}

/**
 * 获取 Node.js 版本
 */
async function getNodeVersion(nodePath: string): Promise<string | undefined> {
  try {
    const proc = Bun.spawn([nodePath, "--version"], { stdout: "pipe", stderr: "pipe" });
    const stdout = await new Response(proc.stdout).text();
    await proc.exited;
    return stdout.trim();
  } catch {
    return undefined;
  }
}

/**
 * 检查是否是 GraalVM Node.js
 */
async function isGraalVMNode(nodePath: string): Promise<boolean> {
  try {
    // GraalVM Node.js 支持 --polyglot 参数
    const proc = Bun.spawn([nodePath, "--help"], { stdout: "pipe", stderr: "pipe" });
    const stdout = await new Response(proc.stdout).text();
    await proc.exited;
    return stdout.includes("--polyglot") || stdout.includes("--jvm");
  } catch {
    return false;
  }
}

/**
 * 运行 gral 命令
 */
export async function runGral(file: string, args: string[], options: GralOptions): Promise<void> {
  // 检测 GraalVM
  const detection = await detectGraalVM();
  
  if (!detection.found || !detection.nodePath) {
    console.error(chalk.red("✗ " + detection.error));
    console.log();
    console.log(chalk.yellow("安装指南:"));
    console.log("  1. 下载 GraalVM: https://www.graalvm.org/downloads/");
    console.log("  2. 设置环境变量: set GRAALVM_HOME=<graalvm路径>");
    console.log("  3. 安装 Node.js 组件: gu install nodejs");
    process.exit(1);
  }

  // 验证是 GraalVM Node.js
  const isGraal = await isGraalVMNode(detection.nodePath);
  if (!isGraal) {
    console.error(chalk.red("✗ 检测到的 Node.js 不是 GraalVM 版本"));
    console.log(chalk.gray(`  路径: ${detection.nodePath}`));
    process.exit(1);
  }

  // 检查文件是否存在
  const filePath = join(process.cwd(), file);
  if (!existsSync(filePath) && !existsSync(file)) {
    console.error(chalk.red(`✗ 文件不存在: ${file}`));
    process.exit(1);
  }

  const targetFile = existsSync(filePath) ? filePath : file;

  // 构建命令参数
  const nodeArgs: string[] = [];
  
  if (options.polyglot || options.jvm) {
    nodeArgs.push("--polyglot");
    nodeArgs.push("--jvm");
  }

  nodeArgs.push(targetFile);
  nodeArgs.push(...args);

  // 显示信息
  if (options.verbose) {
    console.log(chalk.blue("→ GraalVM Node.js 信息:"));
    console.log(chalk.gray(`  路径: ${detection.nodePath}`));
    console.log(chalk.gray(`  版本: ${detection.version}`));
    console.log(chalk.gray(`  GRAALVM_HOME: ${detection.home}`));
    console.log();
  }

  console.log(chalk.blue(`→ 运行: ${detection.nodePath} ${nodeArgs.join(" ")}`));
  console.log();

  // 运行
  const proc = Bun.spawn([detection.nodePath, ...nodeArgs], {
    cwd: process.cwd(),
    stdout: "inherit",
    stderr: "inherit",
    env: process.env,
  });

  const exitCode = await proc.exited;
  
  if (exitCode !== 0) {
    process.exit(exitCode);
  }
}

/**
 * 显示 GraalVM 环境信息
 */
export async function showGralInfo(): Promise<void> {
  console.log(chalk.blue("GraalVM Node.js 环境检测"));
  console.log(chalk.gray("(方案 2: 完整 Node.js 运行时)"));
  console.log();

  const detection = await detectGraalVM();

  if (detection.found && detection.nodePath) {
    const isGraal = await isGraalVMNode(detection.nodePath);
    
    console.log(chalk.green("✓ GraalVM Node.js 已安装"));
    console.log(chalk.gray(`  路径: ${detection.nodePath}`));
    console.log(chalk.gray(`  版本: ${detection.version}`));
    console.log(chalk.gray(`  GRAALVM_HOME: ${detection.home}`));
    console.log(chalk.gray(`  Polyglot 支持: ${isGraal ? "是" : "否"}`));
    
    // 显示已安装组件
    if (detection.components && detection.components.length > 0) {
      console.log();
      console.log(chalk.blue("已安装组件:"));
      for (const comp of detection.components) {
        const isNodejs = comp === "nodejs" || comp.includes("js");
        console.log(`  ${isNodejs ? chalk.green("✓") : chalk.gray("•")} ${comp}`);
      }
    }
    
    // 检查 nodejs 组件
    if (detection.components && !detection.components.some(c => c === "nodejs" || c.includes("js"))) {
      console.log();
      console.log(chalk.yellow("⚠ Node.js 组件可能未安装"));
      console.log(chalk.gray("  运行: gu install nodejs"));
    }
  } else {
    console.log(chalk.red("✗ GraalVM Node.js 未安装"));
    console.log();
    console.log(chalk.yellow("安装指南:"));
    console.log("  1. 下载 GraalVM: https://www.graalvm.org/downloads/");
    console.log("  2. 设置环境变量:");
    if (process.platform === "win32") {
      console.log("     set GRAALVM_HOME=C:\\path\\to\\graalvm");
      console.log("     set PATH=%GRAALVM_HOME%\\bin;%PATH%");
    } else {
      console.log("     export GRAALVM_HOME=/path/to/graalvm");
      console.log("     export PATH=$GRAALVM_HOME/bin:$PATH");
    }
    console.log("  3. 安装 Node.js 组件:");
    console.log("     gu install nodejs");
  }
  
  console.log();
  console.log(chalk.gray("提示: 如果只需要简单的 JavaScript 执行和 Java 互操作，"));
  console.log(chalk.gray("      可以使用 'qin run xxx.js' (方案 1: Polyglot API)"));
}
