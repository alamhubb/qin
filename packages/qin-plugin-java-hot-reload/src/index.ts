/**
 * qin-plugin-java-hot-reload
 * Java 热重载插件（使用 chokidar）
 * 
 * 使用方式：
 * - java 插件默认启用
 * - plugins: [hotReload(false)] 可关闭
 * - plugins: [hotReload({ debounce: 500 })] 可配置
 */

import * as chokidar from "chokidar";
import { join } from "path";

type FSWatcher = ReturnType<typeof chokidar.watch>;

// 全局状态：是否启用热重载
let hotReloadEnabled = true;

/**
 * 热重载配置
 */
export interface HotReloadOptions {
  /** 监听目录，默认 src */
  watchDir?: string;
  /** 防抖延迟（毫秒），默认 300 */
  debounce?: number;
  /** 详细日志 */
  verbose?: boolean;
}

/**
 * 热重载管理器（使用 chokidar）
 */
class HotReloadManager {
  private options: HotReloadOptions;
  private cwd: string;
  private watcher: FSWatcher | null = null;
  private debounceTimer: ReturnType<typeof setTimeout> | null = null;
  private onRecompile?: () => Promise<void>;

  constructor(options: HotReloadOptions = {}) {
    this.options = {
      watchDir: options.watchDir || "src",
      debounce: options.debounce || 300,
      verbose: options.verbose || false,
    };
    this.cwd = process.cwd();
  }

  /**
   * 设置重编译回调
   */
  setRecompileCallback(callback: () => Promise<void>) {
    this.onRecompile = callback;
  }

  /**
   * 开始监听
   */
  start(): void {
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

    this.watcher.on("error", (error: unknown) => {
      const msg = error instanceof Error ? error.message : String(error);
      this.log(`监听错误: ${msg}`);
    });

    this.watcher.on("ready", () => {
      this.log("热重载已启动 (chokidar)");
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
      this.handleChange(filename);
    }, this.options.debounce);
  }

  /**
   * 停止监听
   */
  async stop(): Promise<void> {
    if (this.watcher) {
      await this.watcher.close();
      this.watcher = null;
    }
    if (this.debounceTimer) {
      clearTimeout(this.debounceTimer);
      this.debounceTimer = null;
    }
  }

  private async handleChange(filename: string) {
    this.log(`文件变化: ${filename}，重新编译...`);
    if (this.onRecompile) {
      await this.onRecompile();
    }
  }

  private log(msg: string) {
    console.log(`[hot-reload] ${msg}`);
  }

  private logVerbose(msg: string) {
    if (this.options.verbose) {
      console.log(`[hot-reload] ${msg}`);
    }
  }
}

// 单例管理器
let manager: HotReloadManager | null = null;

/**
 * 获取热重载状态
 */
export function isHotReloadEnabled(): boolean {
  return hotReloadEnabled;
}

/**
 * 获取热重载管理器
 */
export function getHotReloadManager(): HotReloadManager | null {
  return manager;
}

/**
 * 热重载插件
 * 
 * @param enabled - 是否启用，默认 true
 * @param options - 配置选项
 * 
 * @example
 * // 启用（默认）
 * plugins: [hotReload()]
 * 
 * // 关闭
 * plugins: [hotReload(false)]
 * 
 * // 自定义配置
 * plugins: [hotReload({ debounce: 500 })]
 */
export function hotReload(
  enabledOrOptions?: boolean | HotReloadOptions
): {
  name: string;
  configResolved?: (config: any) => void;
  devServer?: (ctx: any) => Promise<void>;
  cleanup?: () => Promise<void>;
} {
  // 解析参数
  if (typeof enabledOrOptions === "boolean") {
    hotReloadEnabled = enabledOrOptions;
    return {
      name: "qin-plugin-java-hot-reload",
      configResolved() {
        // 仅设置状态，不做其他事
      },
    };
  }

  const options = enabledOrOptions || {};
  hotReloadEnabled = true;

  return {
    name: "qin-plugin-java-hot-reload",

    configResolved() {
      if (hotReloadEnabled) {
        manager = new HotReloadManager(options);
      }
    },

    async devServer(ctx: any) {
      if (!hotReloadEnabled || !manager) return;
      manager.start();
    },

    async cleanup() {
      if (manager) {
        manager.stop();
        manager = null;
      }
    },
  };
}

export default hotReload;
