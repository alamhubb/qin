/**
 * Qin - Java-Vite Build Tool
 * Type definitions for configuration and core interfaces
 */

/**
 * Qin 插件接口（简化版，完整版见 core/plugin-system.ts）
 */
export interface QinPlugin {
  /** 插件名称 */
  name: string;
  /** 配置钩子 */
  config?: (config: QinConfig) => QinConfig | void | Promise<QinConfig | void>;
  /** 开发模式启动 */
  dev?: (ctx: PluginContext) => Promise<void>;
  /** 开发服务器钩子 */
  devServer?: (ctx: PluginContext) => Promise<void>;
  /** 构建前钩子 */
  beforeBuild?: (ctx: PluginContext) => Promise<void>;
  /** 构建 */
  build?: (ctx: PluginContext) => Promise<void>;
  /** 清理 */
  cleanup?: () => Promise<void>;
}

/**
 * 插件上下文
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
 * 前端配置（内置 Vite 支持）
 * 自动检测 src/client 目录，有内容则启用
 */
export interface ClientConfig {
  /** 
   * 前端源码目录，默认 "src/client"
   */
  root?: string;
  /** 
   * 前端开发服务器端口，默认 5173
   */
  port?: number;
  /** 
   * API 代理配置，默认代理到后端 port
   * @example
   * proxy: { "/api": "http://localhost:8080" }
   */
  proxy?: Record<string, string>;
  /** 
   * 构建输出目录，默认 "dist/static"
   */
  outDir?: string;
}

/**
 * Main configuration interface for qin.config.ts
 */
/**
 * Maven 依赖范围
 * - compile: 编译和运行时都需要（默认）
 * - provided: 编译时需要，运行时由容器提供（如 servlet-api）
 * - runtime: 编译时不需要，运行时需要（如 JDBC 驱动）
 * - test: 仅测试时需要
 */
export type DependencyScope = "compile" | "provided" | "runtime" | "test";

export interface QinConfig {
  /** Project name */
  name?: string;
  
  /** Project version */
  version?: string;
  
  /** Project description */
  description?: string;

  /**
   * 当前包被其他项目引用时的默认 scope
   * 默认 "compile"
   * 
   * @example
   * // servlet-api 这类包应该设置为 provided
   * scope: "provided"
   * 
   * // lombok 编译时注解处理器
   * scope: "provided"
   */
  scope?: DependencyScope;

  /**
   * 后端服务器端口，默认 8080
   */
  port?: number;

  /**
   * 使用项目本地 repository（./repository）
   * 默认 false，使用全局 ~/.qin/repository
   * 
   * @example
   * localRep: true  // 依赖安装到 ./repository
   */
  localRep?: boolean;

  /**
   * 前端配置（内置 Vite）
   * 自动检测 src/client 目录，有内容则启用
   * 
   * @example
   * // 最简配置（使用默认值）
   * client: {}
   * 
   * // 自定义配置
   * client: {
   *   root: "src/client",
   *   port: 5173,
   *   proxy: { "/api": "http://localhost:8080" },
   * }
   */
  client?: ClientConfig;
  
  /**
   * 插件列表（高级用法）
   * 
   * @example
   * import vite from "qin-plugin-vite";
   * 
   * plugins: [
   *   vite({ port: 3000, proxy: { "/api": "http://localhost:8080" } }),
   * ]
   */
  plugins?: QinPlugin[];
  
  /** 
   * Java 入口文件路径，如 "src/Main.java"
   * 可选，不指定时按以下顺序自动查找：
   * 1. src/Main.java
   * 2. src/server/Main.java  
   * 3. src/{subdir}/Main.java (任意子目录)
   */
  entry?: string;
  
  /** 
   * 依赖配置，支持 Maven 依赖和本地项目
   * 
   * @example
   * dependencies: {
   *   "org.springframework.boot:spring-boot-starter-web": "3.2.0",
   *   "base-java": "*",  // 本地项目（在 packages 中查找）
   * }
   */
  dependencies?: Record<string, string>;

  /**
   * 开发依赖（测试框架、开发工具等）
   * 不会打包进最终产物
   * 
   * @example
   * devDependencies: {
   *   "org.junit.jupiter:junit-jupiter": "5.10.2",
   *   "org.junit.platform:junit-platform-console-standalone": "1.10.2",
   * }
   */
  devDependencies?: Record<string, string>;
  
  /**
   * Monorepo 多项目配置
   * 支持 glob 模式
   * 
   * @example
   * packages: [
   *   "packages/*",
   *   "apps/hello-java",
   * ]
   */
  packages?: string[];
  
  /** Output configuration */
  output?: {
    /** Output directory for built artifacts, default "dist" */
    dir?: string;
    /** JAR file name, default "app.jar" */
    jarName?: string;
  };
  
  /** Java-specific configuration */
  java?: {
    /** Java version, default "17" */
    version?: string;
    /** Source directory, inferred from entry if not specified */
    sourceDir?: string;
  };

  /**
   * GraalVM 配置
   * 启用 GraalVM 多语言运行时支持
   * 
   * @example
   * // 启用 GraalVM JavaScript 支持
   * graalvm: {
   *   js: {
   *     entry: "src/server/index.js",
   *   }
   * }
   */
  graalvm?: GraalVMConfig;
  
  /** Frontend configuration (optional plugin) */
  frontend?: FrontendConfig;
  
  /** Custom scripts */
  scripts?: Record<string, string>;
  
  /** 
   * Maven 仓库配置
   * 支持字符串或对象数组，默认使用阿里云镜像
   * 
   * @example
   * // 简单字符串数组
   * repositories: [
   *   "https://maven.aliyun.com/repository/public",
   *   "https://repo1.maven.org/maven2",
   * ]
   * 
   * @example
   * // 对象数组（完整配置）
   * repositories: [
   *   { id: "aliyun", url: "https://maven.aliyun.com/repository/public" },
   *   { id: "central", url: "https://repo1.maven.org/maven2", releases: true, snapshots: false },
   * ]
   */
  repositories?: Repository[];
}

/**
 * Maven 仓库配置
 * 可以是简单的 URL 字符串，或完整的仓库配置对象
 */
export type Repository = string | RepositoryConfig;

/**
 * Maven 仓库详细配置（参考 Maven settings.xml）
 */
export interface RepositoryConfig {
  /** 仓库唯一标识 */
  id?: string;
  /** 仓库地址 */
  url: string;
  /** 仓库名称 */
  name?: string;
  /** 是否启用 release 版本，默认 true */
  releases?: boolean;
  /** 是否启用 snapshot 版本，默认 false */
  snapshots?: boolean;
}

/**
 * 前端配置
 * 自动检测：扫描到 client/ 或 src/client/ 目录时自动启用
 * 
 * @example
 * // 最简配置（自动检测）
 * frontend: {}
 * 
 * // 自定义目录
 * frontend: { srcDir: "web" }
 */
export interface FrontendConfig {
  /** 
   * 前端源码目录
   * 自动检测顺序：client/, src/client/
   */
  srcDir?: string;
  /** 构建输出目录，默认 "dist/static" */
  outDir?: string;
  /** Vite 开发服务器端口，默认 5173 */
  devPort?: number;
}

/**
 * Parsed entry point information
 */
export interface ParsedEntry {
  /** Source directory path */
  srcDir: string;
  /** Class name without .java extension */
  className: string;
  /** Full file path */
  filePath: string;
}

/**
 * Validation result for configuration
 */
export interface ValidationResult {
  valid: boolean;
  errors: string[];
}

/**
 * Environment check status
 */
export interface EnvironmentStatus {
  coursier: boolean;
  javac: boolean;
  java: boolean;
  ready: boolean;
}

/**
 * Compilation result
 */
export interface CompileResult {
  success: boolean;
  error?: string;
  compiledFiles: number;
  outputDir: string;
}

/**
 * Build result for Fat Jar
 */
export interface BuildResult {
  success: boolean;
  outputPath?: string;
  error?: string;
}

/**
 * Dependency resolution result
 */
export interface ResolveResult {
  success: boolean;
  classpath?: string;
  jarPaths?: string[];
  error?: string;
}

/**
 * GraalVM 配置
 */
export interface GraalVMConfig {
  /** 
   * GraalVM 安装路径
   * 默认自动检测（GRAALVM_HOME 环境变量）
   */
  home?: string;
  
  /**
   * JavaScript 支持配置
   * 设置为 true 启用默认配置，或提供详细配置对象
   * 
   * @example
   * // 简单启用
   * js: true
   * 
   * // 详细配置
   * js: {
   *   entry: "src/server/index.js",
   *   hotReload: true,
   *   javaInterop: true,
   * }
   */
  js?: boolean | GraalVMJsConfig;
}

/**
 * GraalVM JavaScript 配置
 */
export interface GraalVMJsConfig {
  /** JavaScript 入口文件 */
  entry?: string;
  /** 
   * 热重载配置
   * 默认启用
   */
  hotReload?: boolean | { debounce?: number; verbose?: boolean };
  /** 
   * 额外的 Node.js 参数
   * @example
   * nodeArgs: ["--max-old-space-size=4096"]
   */
  nodeArgs?: string[];
  /** 
   * 是否启用 Java 互操作
   * 启用后可以在 JavaScript 中使用 Java.type() 调用 Java 类
   */
  javaInterop?: boolean;
}

/**
 * Define Qin configuration (Vite-style helper)
 * 提供类型提示和自动补全
 * 
 * @example
 * import { defineConfig } from "qin";
 * 
 * export default defineConfig({
 *   name: "my-app",
 *   dependencies: {
 *     "org.springframework.boot:spring-boot-starter-web": "3.2.0",
 *   },
 * });
 */
export function defineConfig(config: QinConfig): QinConfig {
  return config;
}
