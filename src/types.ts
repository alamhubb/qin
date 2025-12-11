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
  
  /** Entry point Java file path, e.g. "src/Main.java" */
  entry: string;
  
  /** Maven dependencies in format "groupId:artifactId:version" */
  dependencies?: string[];
  
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
  
  /** Maven repository configuration */
  repositories?: RepositoryConfig;
}

/**
 * Maven repository configuration
 */
export interface RepositoryConfig {
  /** Use China mirror (aliyun), default true */
  useChinaMirror?: boolean;
  /** Custom repository URLs */
  urls?: string[];
}

/**
 * Frontend plugin configuration
 */
export interface FrontendConfig {
  /** Enable frontend support */
  enabled: boolean;
  /** Frontend source directory, default "client" */
  srcDir?: string;
  /** Frontend build output directory, default "dist/static" */
  outDir?: string;
  /** Vite dev server port, default 5173 */
  devPort?: number;
  /** Proxy API requests to Java backend in dev mode */
  proxyApi?: boolean;
  /** NPM dependencies for frontend */
  dependencies?: Record<string, string>;
  /** NPM dev dependencies for frontend */
  devDependencies?: Record<string, string>;
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
