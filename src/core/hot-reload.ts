/**
 * Java Hot Reload for Qin
 * 监听 Java 文件变化，自动重新编译和重启
 * 使用 chokidar 实现跨平台稳定的文件监听
 */

import * as chokidar from "chokidar";
import { join } from "path";
import chalk from "chalk";
import type { QinConfig } from "../types";
import { JavaRunner } from "./java-runner";

type FSWatcher = ReturnType<typeof chokidar.watch>;

export interface HotReloadOptions {
  /** 监听的目录，默认 src */
  watchDir?: string;
  /** 防抖延迟（毫秒），默认 300 */
  debounce?: number;
  /** 是否显示详细日志 */
  verbose?: boolean;
}

export interface HotReloadContext {
  config: QinConfig;
  classpath: string;
  onRestart?: () => void;
  onError?: (error: Error) => void;
}

/**
 * Java 热重载管理器（使用 chokidar）
 */
export class HotReloadManager {
  private config: QinConfig;
  private classpath: string;
  private options: HotReloadOptions;
  private cwd: string;
  private javaProcess: ReturnType<typeof Bun.spawn> | null = null;
  private watcher: FSWatcher | null = null;
  private debounceTimer: ReturnType<typeof setTimeout> | null = null;
  private isRestarting = false;
  private onRestart?: () => void;
  private onError?: (error: Error) => void;

  constructor(ctx: HotReloadContext, options: HotReloadOptions = {}) {
    this.config = ctx.config;
    this.classpath = ctx.classpath;
    this.options = {
      watchDir: options.watchDir || "src",
      debounce: options.debounce || 300,
      verbose: options.verbose || false,
    };
    this.cwd = process.cwd();
    this.onRestart = ctx.onRestart;
    this.onError = ctx.onError;
  }

  /**
   * 启动热重载
   */
  async start(): Promise<void> {
    // 首次编译和启动
    await this.compileAndStart();

    // 开始监听文件变化
    this.startWatching();

    this.log("热重载已启动，监听文件变化...");
  }

  /**
   * 停止热重载
   */
  async stop(): Promise<void> {
    // 停止监听
    if (this.watcher) {
      await this.watcher.close();
      this.watcher = null;
    }

    // 清除防抖定时器
    if (this.debounceTimer) {
      clearTimeout(this.debounceTimer);
      this.debounceTimer = null;
    }

    // 停止 Java 进程
    await this.stopJavaProcess();

    this.log("热重载已停止");
  }

  /**
   * 编译并启动 Java 进程
   */
  private async compileAndStart(): Promise<void> {
    try {
      // 编译
      const runner = new JavaRunner(this.config, this.classpath);
      const compileResult = await runner.compile();

      if (!compileResult.success) {
        this.logError(`编译失败: ${compileResult.error}`);
        this.onError?.(new Error(compileResult.error));
        return;
      }

      this.logVerbose(`编译成功: ${compileResult.compiledFiles} 个文件`);

      // 启动 Java 进程
      await this.startJavaProcess(runner);
    } catch (error) {
      const err = error instanceof Error ? error : new Error(String(error));
      this.logError(`启动失败: ${err.message}`);
      this.onError?.(err);
    }
  }

  /**
   * 启动 Java 进程
   */
  private async startJavaProcess(runner: JavaRunner): Promise<void> {
    // 先停止旧进程
    await this.stopJavaProcess();

    const fullClasspath = runner["buildFullClasspath"]();
    const { ConfigLoader } = await import("./config-loader");
    const configLoader = new ConfigLoader(this.cwd);
    const parsed = configLoader.parseEntry(this.config.entry);

    this.javaProcess = Bun.spawn(["java", "-cp", fullClasspath, parsed.className], {
      cwd: this.cwd,
      stdout: "inherit",
      stderr: "inherit",
    });

    this.logVerbose(`Java 进程已启动 (PID: ${this.javaProcess.pid})`);

    // 监听进程退出
    this.javaProcess.exited.then((code) => {
      if (!this.isRestarting) {
        this.logVerbose(`Java 进程退出，代码: ${code}`);
      }
    });
  }

  /**
   * 停止 Java 进程
   */
  private async stopJavaProcess(): Promise<void> {
    if (this.javaProcess) {
      this.isRestarting = true;
      this.javaProcess.kill();
      
      // 等待进程退出
      try {
        await Promise.race([
          this.javaProcess.exited,
          new Promise((resolve) => setTimeout(resolve, 3000)),
        ]);
      } catch {
        // 强制杀死
        this.javaProcess.kill(9);
      }

      this.javaProcess = null;
      this.isRestarting = false;
    }
  }

  /**
   * 开始监听文件变化（使用 chokidar）
   */
  private startWatching(): void {
    const watchPattern = join(this.cwd, this.options.watchDir!, "**/*.java");

    this.watcher = chokidar.watch(watchPattern, {
      ignored: /(^|[\/\\])\../, // 忽略隐藏文件
      persistent: true,
      ignoreInitial: true, // 不触发初始文件的 add 事件
      awaitWriteFinish: {
        stabilityThreshold: 100,
        pollInterval: 100,
      },
    });

    // 监听变化事件
    this.watcher.on("change", (path: string) => {
      this.logVerbose(`检测到变化: ${path}`);
      this.scheduleRecompile(path);
    });

    this.watcher.on("add", (path: string) => {
      this.logVerbose(`新增文件: ${path}`);
      this.scheduleRecompile(path);
    });

    this.watcher.on("unlink", (path: string) => {
      this.logVerbose(`删除文件: ${path}`);
      this.scheduleRecompile(path);
    });

    this.watcher.on("error", (error: Error) => {
      this.logError(`监听错误: ${error.message}`);
    });
  }

  /**
   * 防抖调度重编译
   */
  private scheduleRecompile(filename: string): void {
    if (this.debounceTimer) {
      clearTimeout(this.debounceTimer);
    }

    this.debounceTimer = setTimeout(() => {
      this.handleFileChange(filename);
    }, this.options.debounce);
  }

  /**
   * 处理文件变化
   */
  private async handleFileChange(filename: string): Promise<void> {
    this.log(`文件变化: ${filename}，重新编译...`);

    try {
      await this.compileAndStart();
      this.log(chalk.green("✓ 重启完成"));
      this.onRestart?.();
    } catch (error) {
      const err = error instanceof Error ? error : new Error(String(error));
      this.logError(`重启失败: ${err.message}`);
      this.onError?.(err);
    }
  }

  /**
   * 日志输出
   */
  private log(message: string): void {
    console.log(chalk.cyan("[hot-reload]"), message);
  }

  private logVerbose(message: string): void {
    if (this.options.verbose) {
      console.log(chalk.gray("[hot-reload]"), message);
    }
  }

  private logError(message: string): void {
    console.error(chalk.red("[hot-reload]"), message);
  }
}

/**
 * 创建热重载管理器的便捷函数
 */
export function createHotReload(
  ctx: HotReloadContext,
  options?: HotReloadOptions
): HotReloadManager {
  return new HotReloadManager(ctx, options);
}
