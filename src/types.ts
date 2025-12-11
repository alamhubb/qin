/**
 * Qin - Java-Vite Build Tool
 * Type definitions for configuration and core interfaces
 */

/**
 * Main configuration interface for qin.config.ts
 */
export interface QinConfig {
  /** Project name */
  name?: string;
  
  /** Project version */
  version?: string;
  
  /** Project description */
  description?: string;
  
  /** 
   * Java 入口文件路径，如 "src/Main.java"
   * 可选，不指定时按以下顺序自动查找：
   * 1. src/Main.java
   * 2. src/server/Main.java  
   * 3. src/*/Main.java (任意子目录)
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
