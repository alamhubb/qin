/**
 * qin-plugin-graalvm-js
 * GraalVM JavaScript/Node.js support for Qin build tool
 *
 * Features:
 * - Run JavaScript with GraalVM Node.js runtime
 * - Full Node.js API support
 * - npm ecosystem compatibility
 * - Java interop via --polyglot --jvm
 * - Hot reload support
 */

import { join } from "path";
import { existsSync } from "fs";
import { spawn, spawnSync, type ChildProcess } from "child_process";
import {
  graalvm,
  detectGraalVM,
  GraalVMNotFoundError,
  ComponentNotInstalledError,
  type GraalVMInfo,
  type GraalVMDetectionResult,
  type GraalVMPluginOptions,
} from "qin-plugin-graalvm";

// ============================================================================
// Types
// ============================================================================

/**
 * GraalVM JavaScript 插件配置
 */
export interface GraalVMJsPluginOptions {
  /** JavaScript 入口文件 */
  entry?: string;
  /** 热重载配置 */
  hotReload?: boolean | { debounce?: number; verbose?: boolean };
  /** 额外的 Node.js 参数 */
  nodeArgs?: string[];
  /** 是否启用 Java 互操作 */
  javaInterop?: boolean;
  /** GraalVM 配置 */
  graalvm?: GraalVMPluginOptions;
}

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
 * 热重载状态
 */
export interface HotReloadState {
  /** 是否启用 */
  enabled: boolean;
  /** 监听的文件模式 */
  watchPatterns: string[];
  /** 当前进程 */
  process?: ChildProcess;
  /** 环境变量 */
  env: Record<string, string>;
  /** 命令行参数 */
  args: string[];
  /** 工作目录 */
  cwd: string;
}

/**
 * JavaScript 编译结果
 */
export interface JsCompileResult {
  /** 是否成功 */
  success: boolean;
  /** 语法错误列表 */
  syntaxErrors?: Array<{
    file: string;
    line: number;
    column: number;
    message: string;
  }>;
  /** 验证的文件数量 */
  validatedFiles: number;
}

/**
 * 插件上下文
 */
interface PluginContext {
  root: string;
  config: any;
  isDev: boolean;
  log: (msg: string) => void;
  warn: (msg: string) => void;
  error: (msg: string) => void;
}

/**
 * 编译上下文
 */
interface CompileContext extends PluginContext {
  sourceFiles: string[];
  outputDir: string;
  classpath: string;
}

/**
 * 运行上下文
 */
interface RunContext extends PluginContext {
  args: string[];
  classpath: string;
}

/**
 * 语言支持接口
 */
interface LanguageSupport {
  name: string;
  extensions: string[];
  compile?(ctx: CompileContext): Promise<JsCompileResult>;
  run?(ctx: RunContext): Promise<void>;
}

/**
 * Qin 插件接口
 */
interface QinPlugin {
  name: string;
  language?: LanguageSupport;
  plugins?: QinPlugin[];
  config?: (config: any) => any;
  devServer?: (ctx: any) => Promise<void>;
}

// ============================================================================
// GraalVM Node.js Executor
// ============================================================================

/**
 * 获取 GraalVM Node.js 路径
 */
export function getGraalVMNodePath(graalvmHome?: string): string | null {
  const exe = process.platform === "win32" ? ".exe" : "";

  // 1. 使用指定的 home
  if (graalvmHome) {
    const nodePath = join(graalvmHome, "bin", `node${exe}`);
    if (existsSync(nodePath)) {
      return nodePath;
    }
  }

  // 2. 检查 GRAALVM_HOME
  const envGraalvmHome = process.env.GRAALVM_HOME;
  if (envGraalvmHome) {
    const nodePath = join(envGraalvmHome, "bin", `node${exe}`);
    if (existsSync(nodePath)) {
      return nodePath;
    }
  }

  // 3. 检查 JAVA_HOME（可能是 GraalVM）
  const javaHome = process.env.JAVA_HOME;
  if (javaHome) {
    const nodePath = join(javaHome, "bin", `node${exe}`);
    if (existsSync(nodePath)) {
      return nodePath;
    }
  }

  return null;
}

/**
 * 检查是否是 GraalVM Node.js
 */
export function isGraalVMNode(nodePath: string): boolean {
  try {
    const result = spawnSync(nodePath, ["--help"], { encoding: "utf-8" });
    const output = result.stdout || "";
    return output.includes("--polyglot") || output.includes("--jvm");
  } catch {
    return false;
  }
}

/**
 * 获取 Node.js 版本
 */
export function getNodeVersion(nodePath: string): string | null {
  try {
    const result = spawnSync(nodePath, ["--version"], { encoding: "utf-8" });
    if (result.status === 0) {
      return (result.stdout || "").trim();
    }
  } catch {
    // 忽略错误
  }
  return null;
}

/**
 * 构建 Node.js 执行命令参数
 */
export function buildNodeArgs(options: {
  file: string;
  args?: string[];
  javaInterop?: boolean;
  nodeArgs?: string[];
}): string[] {
  const args: string[] = [];

  // 添加额外的 Node.js 参数
  if (options.nodeArgs) {
    args.push(...options.nodeArgs);
  }

  // 启用 Java 互操作
  if (options.javaInterop) {
    args.push("--polyglot");
    args.push("--jvm");
  }

  // 添加入口文件
  args.push(options.file);

  // 添加脚本参数
  if (options.args) {
    args.push(...options.args);
  }

  return args;
}

/**
 * 使用 GraalVM Node.js 执行 JavaScript（同步）
 */
export function executeJavaScriptSync(options: {
  nodePath: string;
  file: string;
  args?: string[];
  cwd?: string;
  env?: Record<string, string>;
  javaInterop?: boolean;
  nodeArgs?: string[];
}): ExecutionResult {
  const nodeArgs = buildNodeArgs({
    file: options.file,
    args: options.args,
    javaInterop: options.javaInterop,
    nodeArgs: options.nodeArgs,
  });

  const result = spawnSync(options.nodePath, nodeArgs, {
    cwd: options.cwd || process.cwd(),
    env: { ...process.env, ...options.env },
    encoding: "utf-8",
  });

  return {
    exitCode: result.status ?? 1,
    stdout: result.stdout || "",
    stderr: result.stderr || "",
  };
}

/**
 * 使用 GraalVM Node.js 执行 JavaScript（异步）
 */
export async function executeJavaScript(options: {
  nodePath: string;
  file: string;
  args?: string[];
  cwd?: string;
  env?: Record<string, string>;
  javaInterop?: boolean;
  nodeArgs?: string[];
  inheritStdio?: boolean;
}): Promise<ExecutionResult> {
  const nodeArgs = buildNodeArgs({
    file: options.file,
    args: options.args,
    javaInterop: options.javaInterop,
    nodeArgs: options.nodeArgs,
  });

  return new Promise((resolve) => {
    let stdout = "";
    let stderr = "";

    const proc = spawn(options.nodePath, nodeArgs, {
      cwd: options.cwd || process.cwd(),
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

// ============================================================================
// Error Formatting
// ============================================================================

/**
 * JavaScript 错误信息
 */
export interface JsErrorInfo {
  type: "syntax" | "runtime" | "file" | "unknown";
  message: string;
  file?: string;
  line?: number;
  column?: number;
  stack?: string;
}

/**
 * 解析 JavaScript 错误
 */
export function parseJsError(stderr: string): JsErrorInfo {
  // 语法错误
  const syntaxMatch = stderr.match(/SyntaxError:\s*(.+?)(?:\n|$)/);
  if (syntaxMatch) {
    const locationMatch = stderr.match(/at\s+(.+?):(\d+):(\d+)/);
    return {
      type: "syntax",
      message: syntaxMatch[1] || "Syntax error",
      file: locationMatch?.[1],
      line: locationMatch?.[2] ? parseInt(locationMatch[2], 10) : undefined,
      column: locationMatch?.[3] ? parseInt(locationMatch[3], 10) : undefined,
      stack: stderr,
    };
  }

  // 运行时错误
  const runtimeMatch = stderr.match(/(TypeError|ReferenceError|RangeError|Error):\s*(.+?)(?:\n|$)/);
  if (runtimeMatch) {
    const locationMatch = stderr.match(/at\s+.+?\s+\((.+?):(\d+):(\d+)\)/);
    return {
      type: "runtime",
      message: `${runtimeMatch[1]}: ${runtimeMatch[2]}`,
      file: locationMatch?.[1],
      line: locationMatch?.[2] ? parseInt(locationMatch[2], 10) : undefined,
      column: locationMatch?.[3] ? parseInt(locationMatch[3], 10) : undefined,
      stack: stderr,
    };
  }

  // 文件错误
  if (stderr.includes("Cannot find module") || stderr.includes("ENOENT")) {
    return {
      type: "file",
      message: stderr.trim(),
    };
  }

  return {
    type: "unknown",
    message: stderr.trim() || "Unknown error",
  };
}

/**
 * 格式化 JavaScript 错误
 */
export function formatJsError(error: JsErrorInfo): string {
  let output = "";

  switch (error.type) {
    case "syntax":
      output = `SyntaxError: ${error.message}`;
      break;
    case "runtime":
      output = error.message;
      break;
    case "file":
      output = `File Error: ${error.message}`;
      break;
    default:
      output = `Error: ${error.message}`;
  }

  if (error.file) {
    output += `\n  at ${error.file}`;
    if (error.line !== undefined) {
      output += `:${error.line}`;
      if (error.column !== undefined) {
        output += `:${error.column}`;
      }
    }
  }

  return output;
}

// ============================================================================
// Language Support
// ============================================================================

/**
 * GraalVM JavaScript 语言支持
 */
class GraalVMJsLanguageSupport implements LanguageSupport {
  name = "graalvm-js";
  extensions = [".js", ".mjs"];

  private options: GraalVMJsPluginOptions;
  private graalvmInfo: GraalVMInfo | null = null;

  constructor(options: GraalVMJsPluginOptions = {}) {
    this.options = options;

    // 检测 GraalVM
    const detection = detectGraalVM(options.graalvm?.home);
    if (detection.found && detection.info) {
      this.graalvmInfo = detection.info;
    }
  }

  /**
   * 验证 JavaScript 语法
   */
  async compile(ctx: CompileContext): Promise<JsCompileResult> {
    const jsFiles = ctx.sourceFiles.filter(
      (f) => f.endsWith(".js") || f.endsWith(".mjs")
    );

    if (jsFiles.length === 0) {
      return {
        success: true,
        validatedFiles: 0,
      };
    }

    // 使用 GraalVM Node.js 验证语法
    const nodePath = this.graalvmInfo?.nodePath;
    if (!nodePath) {
      return {
        success: false,
        syntaxErrors: [
          {
            file: "",
            line: 0,
            column: 0,
            message: "GraalVM Node.js not found",
          },
        ],
        validatedFiles: 0,
      };
    }

    const errors: JsCompileResult["syntaxErrors"] = [];

    for (const file of jsFiles) {
      // 使用 --check 参数验证语法
      const result = spawnSync(nodePath, ["--check", file], {
        encoding: "utf-8",
      });

      if (result.status !== 0) {
        const errorInfo = parseJsError(result.stderr || "");
        errors.push({
          file: errorInfo.file || file,
          line: errorInfo.line || 0,
          column: errorInfo.column || 0,
          message: errorInfo.message,
        });
      }
    }

    return {
      success: errors.length === 0,
      syntaxErrors: errors.length > 0 ? errors : undefined,
      validatedFiles: jsFiles.length,
    };
  }

  /**
   * 运行 JavaScript
   */
  async run(ctx: RunContext): Promise<void> {
    const entry = this.options.entry || ctx.config.entry;
    if (!entry) {
      throw new Error("No entry point specified");
    }

    const nodePath = this.graalvmInfo?.nodePath;
    if (!nodePath) {
      throw new GraalVMNotFoundError("GraalVM Node.js not found");
    }

    // 检查 nodejs 组件是否安装
    if (!this.graalvmInfo?.components.includes("nodejs")) {
      throw new ComponentNotInstalledError("nodejs");
    }

    const filePath = join(ctx.root, entry);
    if (!existsSync(filePath)) {
      throw new Error(`Entry file not found: ${filePath}`);
    }

    const result = await executeJavaScript({
      nodePath,
      file: filePath,
      args: ctx.args,
      cwd: ctx.root,
      javaInterop: this.options.javaInterop,
      nodeArgs: this.options.nodeArgs,
      inheritStdio: true,
    });

    if (result.exitCode !== 0) {
      throw new Error(`JavaScript execution failed with code ${result.exitCode}`);
    }
  }
}

// ============================================================================
// Plugin Factory
// ============================================================================

/**
 * 创建 GraalVM JavaScript 插件
 */
export function graalvmJs(options: GraalVMJsPluginOptions = {}): QinPlugin {
  const languageSupport = new GraalVMJsLanguageSupport(options);

  // 自动注册 graalvm 基础插件
  const graalvmPlugin = graalvm(options.graalvm);

  return {
    name: "qin-plugin-graalvm-js",
    language: languageSupport,

    // 依赖 graalvm 基础插件
    plugins: [graalvmPlugin],

    config(config: any) {
      // 如果配置了 entry，确保它被设置
      if (options.entry && !config.entry) {
        return { ...config, entry: options.entry };
      }
      return config;
    },
  };
}

export default graalvmJs;

// Re-export from graalvm plugin
export {
  graalvm,
  detectGraalVM,
  getInstalledComponents,
  isComponentInstalled,
  GraalVMNotFoundError,
  ComponentNotInstalledError,
} from "qin-plugin-graalvm";

export type {
  GraalVMInfo,
  GraalVMDetectionResult,
  GraalVMPluginOptions,
  GraalVMPlugin,
} from "qin-plugin-graalvm";


// ============================================================================
// Hot Reload Support
// ============================================================================

/**
 * 热重载管理器
 */
export class JsHotReloadManager {
  private state: HotReloadState;
  private watcher: any = null;
  private debounceTimer: NodeJS.Timeout | null = null;
  private options: {
    debounce: number;
    verbose: boolean;
    nodePath: string;
    entry: string;
    javaInterop?: boolean;
    nodeArgs?: string[];
  };

  constructor(options: {
    nodePath: string;
    entry: string;
    cwd?: string;
    env?: Record<string, string>;
    args?: string[];
    javaInterop?: boolean;
    nodeArgs?: string[];
    hotReload?: boolean | { debounce?: number; verbose?: boolean };
  }) {
    const hotReloadConfig = typeof options.hotReload === "object" ? options.hotReload : {};
    
    this.options = {
      debounce: hotReloadConfig.debounce || 500,
      verbose: hotReloadConfig.verbose || false,
      nodePath: options.nodePath,
      entry: options.entry,
      javaInterop: options.javaInterop,
      nodeArgs: options.nodeArgs,
    };

    this.state = {
      enabled: options.hotReload !== false,
      watchPatterns: ["**/*.js", "**/*.mjs"],
      env: { ...process.env as Record<string, string>, ...options.env },
      args: options.args || [],
      cwd: options.cwd || process.cwd(),
    };
  }

  /**
   * 启动热重载
   */
  async start(): Promise<void> {
    if (!this.state.enabled) {
      // 直接运行，不启用热重载
      await this.startProcess();
      return;
    }

    // 启动进程
    await this.startProcess();

    // 启动文件监听
    await this.startWatcher();
  }

  /**
   * 停止热重载
   */
  async stop(): Promise<void> {
    // 停止文件监听
    if (this.watcher) {
      this.watcher.close();
      this.watcher = null;
    }

    // 停止进程
    await this.stopProcess();
  }

  /**
   * 启动 Node.js 进程
   */
  private async startProcess(): Promise<void> {
    const nodeArgs = buildNodeArgs({
      file: this.options.entry,
      args: this.state.args,
      javaInterop: this.options.javaInterop,
      nodeArgs: this.options.nodeArgs,
    });

    if (this.options.verbose) {
      console.log(`[hot-reload] Starting: ${this.options.nodePath} ${nodeArgs.join(" ")}`);
    }

    const proc = spawn(this.options.nodePath, nodeArgs, {
      cwd: this.state.cwd,
      env: this.state.env,
      stdio: "inherit",
    });

    this.state.process = proc;

    proc.on("exit", (code) => {
      if (this.options.verbose) {
        console.log(`[hot-reload] Process exited with code ${code}`);
      }
    });
  }

  /**
   * 停止 Node.js 进程
   */
  private async stopProcess(): Promise<void> {
    if (this.state.process) {
      this.state.process.kill();
      this.state.process = undefined;
    }
  }

  /**
   * 重启进程
   */
  private async restart(): Promise<void> {
    if (this.options.verbose) {
      console.log("[hot-reload] Restarting...");
    }

    await this.stopProcess();
    await this.startProcess();
  }

  /**
   * 启动文件监听
   */
  private async startWatcher(): Promise<void> {
    try {
      // 使用 Bun 的文件监听 API
      const { watch } = await import("fs");
      const { dirname } = await import("path");

      const watchDir = dirname(this.options.entry);
      
      this.watcher = watch(watchDir, { recursive: true }, (eventType, filename) => {
        if (!filename) return;
        
        // 只监听 .js 和 .mjs 文件
        if (!filename.endsWith(".js") && !filename.endsWith(".mjs")) {
          return;
        }

        if (this.options.verbose) {
          console.log(`[hot-reload] File changed: ${filename}`);
        }

        // 防抖
        if (this.debounceTimer) {
          clearTimeout(this.debounceTimer);
        }

        this.debounceTimer = setTimeout(() => {
          this.restart();
        }, this.options.debounce);
      });

      if (this.options.verbose) {
        console.log(`[hot-reload] Watching ${watchDir} for changes...`);
      }
    } catch (error) {
      console.warn("[hot-reload] Failed to start file watcher:", error);
    }
  }

  /**
   * 获取当前状态
   */
  getState(): HotReloadState {
    return { ...this.state };
  }
}

/**
 * 创建热重载管理器
 */
export function createHotReloadManager(options: {
  nodePath: string;
  entry: string;
  cwd?: string;
  env?: Record<string, string>;
  args?: string[];
  javaInterop?: boolean;
  nodeArgs?: string[];
  hotReload?: boolean | { debounce?: number; verbose?: boolean };
}): JsHotReloadManager {
  return new JsHotReloadManager(options);
}
