/**
 * Qin Plugin System
 * Vite-style plugin architecture with language support
 */

import type { QinConfig } from "../types";

/**
 * 插件上下文 - 传递给插件钩子的信息
 */
export interface PluginContext {
  /** 项目根目录 */
  root: string;
  /** 当前配置 */
  config: QinConfig;
  /** 是否开发模式 */
  isDev: boolean;
  /** 日志函数 */
  log: (msg: string) => void;
  /** 警告函数 */
  warn: (msg: string) => void;
  /** 错误函数 */
  error: (msg: string) => void;
}

/**
 * 编译上下文
 */
export interface CompileContext extends PluginContext {
  /** 源文件列表 */
  sourceFiles: string[];
  /** 输出目录 */
  outputDir: string;
  /** classpath */
  classpath: string;
}

/**
 * 运行上下文
 */
export interface RunContext extends PluginContext {
  /** 运行参数 */
  args: string[];
  /** classpath */
  classpath: string;
}

/**
 * 构建上下文
 */
export interface BuildContext extends PluginContext {
  /** 输出目录 */
  outputDir: string;
  /** 输出文件名 */
  outputName: string;
}

/**
 * 测试上下文
 */
export interface TestContext extends PluginContext {
  /** 测试过滤器 */
  filter?: string;
  /** 是否详细输出 */
  verbose?: boolean;
}

/**
 * 测试结果
 */
export interface TestResult {
  success: boolean;
  testsRun: number;
  failures: number;
  errors: number;
  skipped: number;
  time: number;
  output?: string;
  error?: string;
}

/**
 * 编译结果
 */
export interface CompileResult {
  success: boolean;
  error?: string;
  compiledFiles: number;
  outputDir: string;
}

/**
 * 构建结果
 */
export interface BuildResult {
  success: boolean;
  outputPath?: string;
  error?: string;
}

/**
 * 语言插件接口 - 提供语言特定的编译/运行/测试能力
 */
export interface LanguageSupport {
  /** 语言名称 */
  name: string;
  /** 支持的文件扩展名 */
  extensions: string[];
  /** 编译源代码 */
  compile?(ctx: CompileContext): Promise<CompileResult>;
  /** 运行程序 */
  run?(ctx: RunContext): Promise<void>;
  /** 运行测试 */
  test?(ctx: TestContext): Promise<TestResult>;
  /** 构建产物（如 JAR） */
  build?(ctx: BuildContext): Promise<BuildResult>;
  /** 解析依赖 */
  resolveDependencies?(deps: Record<string, string>): Promise<string>;
}

/**
 * Qin 插件接口 - Vite 风格
 */
export interface QinPlugin {
  /** 插件名称（必须） */
  name: string;

  /**
   * 子插件列表（可选）
   * 插件可以包含其他插件，会被扁平化处理
   * 例如：java 插件默认包含 hot-reload 插件
   */
  plugins?: QinPlugin[];

  /** 
   * 语言支持（可选）
   * 如果提供，该插件将作为语言插件处理对应扩展名的文件
   */
  language?: LanguageSupport;

  /**
   * 配置钩子 - 修改或扩展配置
   * @returns 返回的配置会与原配置合并
   */
  config?(config: QinConfig): QinConfig | void | Promise<QinConfig | void>;

  /**
   * 配置解析完成钩子
   */
  configResolved?(config: QinConfig): void | Promise<void>;

  /**
   * 编译前钩子
   */
  beforeCompile?(ctx: PluginContext): void | Promise<void>;

  /**
   * 编译后钩子
   */
  afterCompile?(ctx: PluginContext): void | Promise<void>;

  /**
   * 运行前钩子
   */
  beforeRun?(ctx: PluginContext): void | Promise<void>;

  /**
   * 运行后钩子
   */
  afterRun?(ctx: PluginContext): void | Promise<void>;

  /**
   * 构建前钩子
   */
  beforeBuild?(ctx: PluginContext): void | Promise<void>;

  /**
   * 构建后钩子
   */
  afterBuild?(ctx: PluginContext): void | Promise<void>;

  /**
   * 开发服务器钩子
   */
  devServer?(ctx: PluginContext): void | Promise<void>;

  /**
   * 清理钩子
   */
  cleanup?(): void | Promise<void>;
}

/**
 * 扁平化插件列表
 * 递归展开所有子插件，同名插件后面的覆盖前面的
 */
export function flattenPlugins(plugins: QinPlugin[]): QinPlugin[] {
  const flattened: QinPlugin[] = [];

  for (const plugin of plugins) {
    // 先添加插件本身
    flattened.push(plugin);

    // 如果有子插件，递归展开
    if (plugin.plugins && plugin.plugins.length > 0) {
      flattened.push(...flattenPlugins(plugin.plugins));
    }
  }

  // 同名插件去重：后面的覆盖前面的
  const seen = new Map<string, QinPlugin>();
  for (const plugin of flattened) {
    seen.set(plugin.name, plugin);
  }

  return Array.from(seen.values());
}

/**
 * 插件管理器
 */
export class PluginManager {
  private plugins: QinPlugin[] = [];
  private languagePlugins: Map<string, QinPlugin> = new Map();

  constructor(plugins: QinPlugin[] = []) {
    // 扁平化插件列表
    const flattened = flattenPlugins(plugins);
    
    for (const plugin of flattened) {
      this.register(plugin);
    }
  }

  /**
   * 注册插件（内部使用，不会扁平化）
   */
  private register(plugin: QinPlugin): void {
    this.plugins.push(plugin);

    // 如果是语言插件，按扩展名索引
    if (plugin.language) {
      for (const ext of plugin.language.extensions) {
        this.languagePlugins.set(ext, plugin);
      }
    }
  }

  /**
   * 添加插件（会扁平化处理）
   */
  add(plugin: QinPlugin): void {
    const flattened = flattenPlugins([plugin]);
    for (const p of flattened) {
      // 检查是否已存在同名插件，如果存在则替换
      const existingIndex = this.plugins.findIndex(existing => existing.name === p.name);
      if (existingIndex >= 0) {
        this.plugins[existingIndex] = p;
      } else {
        this.register(p);
      }
    }
  }

  /**
   * 获取所有插件
   */
  getPlugins(): QinPlugin[] {
    return this.plugins;
  }

  /**
   * 根据文件扩展名获取语言插件
   */
  getLanguagePlugin(extension: string): QinPlugin | undefined {
    return this.languagePlugins.get(extension);
  }

  /**
   * 获取所有语言插件
   */
  getLanguagePlugins(): QinPlugin[] {
    return Array.from(new Set(this.languagePlugins.values()));
  }

  /**
   * 运行配置钩子
   */
  async runConfigHooks(config: QinConfig): Promise<QinConfig> {
    let result = { ...config };

    for (const plugin of this.plugins) {
      if (plugin.config) {
        const modified = await plugin.config(result);
        if (modified) {
          result = { ...result, ...modified };
        }
      }
    }

    return result;
  }

  /**
   * 运行 configResolved 钩子
   */
  async runConfigResolvedHooks(config: QinConfig): Promise<void> {
    for (const plugin of this.plugins) {
      if (plugin.configResolved) {
        await plugin.configResolved(config);
      }
    }
  }

  /**
   * 运行生命周期钩子
   */
  async runHook(
    hookName: "beforeCompile" | "afterCompile" | "beforeRun" | "afterRun" | "beforeBuild" | "afterBuild" | "devServer",
    ctx: PluginContext
  ): Promise<void> {
    for (const plugin of this.plugins) {
      const hook = plugin[hookName];
      if (hook) {
        await hook(ctx);
      }
    }
  }

  /**
   * 运行清理钩子
   */
  async runCleanup(): Promise<void> {
    for (const plugin of this.plugins) {
      if (plugin.cleanup) {
        await plugin.cleanup();
      }
    }
  }

  /**
   * 创建插件上下文
   */
  createContext(config: QinConfig, isDev: boolean = false): PluginContext {
    return {
      root: process.cwd(),
      config,
      isDev,
      log: (msg: string) => console.log(`[qin] ${msg}`),
      warn: (msg: string) => console.warn(`[qin] ⚠ ${msg}`),
      error: (msg: string) => console.error(`[qin] ✗ ${msg}`),
    };
  }
}

/**
 * 创建插件的辅助函数
 */
export function definePlugin(plugin: QinPlugin): QinPlugin {
  return plugin;
}
