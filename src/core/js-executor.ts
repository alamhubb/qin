/**
 * JavaScript Executor
 * 使用 GraalVM Polyglot API 执行 JavaScript 文件
 */

import { spawn, spawnSync } from "child_process";
import { join, dirname } from "path";
import { existsSync } from "fs";

/**
 * 执行结果
 */
export interface ExecutionResult {
  /** 退出码 */
  exitCode: number;
  /** 标准输出 */
  stdout: string;
  /** 标准错误 */
  stderr: string;
}

/**
 * 执行器选项
 */
export interface ExecutorOptions {
  /** JavaScript 文件路径 */
  file: string;
  /** 命令行参数 */
  args: string[];
  /** 工作目录 */
  cwd: string;
  /** 环境变量 */
  env?: Record<string, string>;
  /** 是否继承标准 IO（用于交互式运行） */
  inheritStdio?: boolean;
}

/**
 * Java 命令规格
 */
export interface JavaCommandSpec {
  /** Java 可执行文件路径 */
  javaPath: string;
  /** 类路径 */
  classpath: string[];
  /** 主类 */
  mainClass: string;
  /** JVM 参数 */
  jvmArgs: string[];
  /** 程序参数 */
  programArgs: string[];
}

/**
 * 获取 GraalVM Java 路径
 * 优先使用 GRAALVM_HOME，否则使用系统 java
 */
export function getGraalVMJavaPath(): string {
  const graalvmHome = process.env.GRAALVM_HOME;
  if (graalvmHome) {
    const javaPath = join(graalvmHome, "bin", process.platform === "win32" ? "java.exe" : "java");
    if (existsSync(javaPath)) {
      return javaPath;
    }
  }
  return "java";
}

/**
 * 获取 JsRunner 类路径
 * 查找 lib/java 目录下的编译后的类文件
 */
export function getJsRunnerClasspath(): string {
  // 查找可能的位置
  const possiblePaths = [
    // 开发时：项目根目录下的 lib/java
    join(process.cwd(), "lib", "java"),
    // 开发时：从 src 目录向上查找
    join(dirname(dirname(__dirname)), "lib", "java"),
    // 安装后：node_modules/qin/lib/java
    join(__dirname, "..", "..", "lib", "java"),
  ];

  for (const p of possiblePaths) {
    if (existsSync(p)) {
      return p;
    }
  }

  // 默认返回相对路径
  return "lib/java";
}

/**
 * 获取 GraalVM Polyglot SDK 的类路径
 * 从 Coursier 缓存中查找 GraalJS 依赖
 */
export function getPolyglotClasspath(): string[] {
  const polyglotPaths: string[] = [];
  
  // Coursier 缓存位置
  const coursierCache = process.platform === "win32"
    ? join(process.env.LOCALAPPDATA || "", "Coursier", "cache", "v1", "https", "repo1.maven.org", "maven2", "org", "graalvm")
    : join(process.env.HOME || "", ".cache", "coursier", "v1", "https", "repo1.maven.org", "maven2", "org", "graalvm");
  
  if (existsSync(coursierCache)) {
    // 查找所有 GraalVM 相关的 JAR
    const graalvmDirs = ["polyglot", "js", "truffle", "sdk", "regex", "shadowed"];
    for (const dir of graalvmDirs) {
      const dirPath = join(coursierCache, dir);
      if (existsSync(dirPath)) {
        // 递归查找所有 JAR 文件
        findJarsRecursive(dirPath, polyglotPaths);
      }
    }
  }
  
  // 也检查 GRAALVM_HOME
  const graalvmHome = process.env.GRAALVM_HOME;
  if (graalvmHome) {
    const possibleDirs = [
      join(graalvmHome, "lib", "polyglot"),
      join(graalvmHome, "lib", "truffle"),
    ];
    for (const dir of possibleDirs) {
      if (existsSync(dir)) {
        polyglotPaths.push(join(dir, "*"));
      }
    }
  }

  return polyglotPaths;
}

/**
 * 递归查找目录中的所有 JAR 文件
 */
function findJarsRecursive(dir: string, result: string[]): void {
  try {
    const { readdirSync, statSync } = require("fs");
    const entries = readdirSync(dir);
    for (const entry of entries) {
      const fullPath = join(dir, entry);
      const stat = statSync(fullPath);
      if (stat.isDirectory()) {
        findJarsRecursive(fullPath, result);
      } else if (entry.endsWith(".jar")) {
        result.push(fullPath);
      }
    }
  } catch {
    // 忽略错误
  }
}

/**
 * 构建 Java 执行命令
 */
export function buildJavaCommand(options: ExecutorOptions): JavaCommandSpec {
  const javaPath = getGraalVMJavaPath();
  const jsRunnerPath = getJsRunnerClasspath();
  const polyglotPaths = getPolyglotClasspath();

  // 构建类路径
  const classpath = [jsRunnerPath, ...polyglotPaths];
  
  // 添加工作目录到类路径（用于加载用户的 Java 类）
  if (options.cwd && !classpath.includes(options.cwd)) {
    classpath.push(options.cwd);
  }

  return {
    javaPath,
    classpath,
    mainClass: "qin.runtime.JsRunner",
    jvmArgs: [
      // 禁用 Polyglot 引擎警告（在非 GraalVM JDK 上运行时）
      "-Dpolyglot.engine.WarnInterpreterOnly=false",
    ],
    programArgs: [options.file, ...options.args],
  };
}

/**
 * 将 JavaCommandSpec 转换为命令行参数数组
 */
export function commandSpecToArgs(spec: JavaCommandSpec): string[] {
  const args: string[] = [];
  
  // JVM 参数
  args.push(...spec.jvmArgs);
  
  // 类路径
  if (spec.classpath.length > 0) {
    const separator = process.platform === "win32" ? ";" : ":";
    args.push("-cp", spec.classpath.join(separator));
  }
  
  // 主类
  args.push(spec.mainClass);
  
  // 程序参数
  args.push(...spec.programArgs);
  
  return args;
}

/**
 * 使用 GraalVM Polyglot API 执行 JavaScript 文件（同步）
 */
export function executeJavaScriptSync(options: ExecutorOptions): ExecutionResult {
  const spec = buildJavaCommand(options);
  const args = commandSpecToArgs(spec);

  const result = spawnSync(spec.javaPath, args, {
    cwd: options.cwd,
    env: { ...process.env, ...options.env },
    encoding: "utf-8",
    stdio: options.inheritStdio ? "inherit" : "pipe",
  });

  return {
    exitCode: result.status ?? 1,
    stdout: result.stdout || "",
    stderr: result.stderr || "",
  };
}

/**
 * 使用 GraalVM Polyglot API 执行 JavaScript 文件（异步）
 */
export async function executeJavaScript(options: ExecutorOptions): Promise<ExecutionResult> {
  const spec = buildJavaCommand(options);
  const args = commandSpecToArgs(spec);

  return new Promise((resolve) => {
    let stdout = "";
    let stderr = "";

    const proc = spawn(spec.javaPath, args, {
      cwd: options.cwd,
      env: { ...process.env, ...options.env },
      stdio: options.inheritStdio ? "inherit" : "pipe",
    });

    if (!options.inheritStdio) {
      proc.stdout?.on("data", (data) => {
        stdout += data.toString();
      });

      proc.stderr?.on("data", (data) => {
        stderr += data.toString();
      });
    }

    proc.on("close", (code) => {
      resolve({
        exitCode: code ?? 1,
        stdout,
        stderr,
      });
    });

    proc.on("error", (err) => {
      resolve({
        exitCode: 1,
        stdout,
        stderr: stderr + "\n" + err.message,
      });
    });
  });
}

/**
 * 检查 Java 是否可用（用于运行 Polyglot API）
 * 注意：我们使用 GraalJS 作为依赖，所以任何 Java 11+ 都可以运行
 */
export function checkGraalVMAvailable(): { available: boolean; version?: string; error?: string; isGraalVM?: boolean } {
  try {
    const javaPath = getGraalVMJavaPath();
    const result = spawnSync(javaPath, ["--version"], { encoding: "utf-8" });
    
    if (result.status !== 0) {
      return { available: false, error: "Java not found or not executable" };
    }

    const output = result.stdout || result.stderr || "";
    const isGraalVM = output.toLowerCase().includes("graalvm");
    
    // 提取版本号
    const versionMatch = output.match(/(\d+)(?:\.\d+)?(?:\.\d+)?/);
    const version = versionMatch ? versionMatch[0] : "unknown";
    
    // 检查 Java 版本是否 >= 11（Polyglot API 需要）
    const majorVersion = versionMatch ? parseInt(versionMatch[1], 10) : 0;
    if (majorVersion < 11) {
      return {
        available: false,
        error: `Java ${majorVersion} is too old. Please use Java 11 or later for JavaScript support.`,
      };
    }

    return { available: true, version, isGraalVM };
  } catch (err) {
    return { 
      available: false, 
      error: `Failed to check Java: ${err instanceof Error ? err.message : String(err)}` 
    };
  }
}
