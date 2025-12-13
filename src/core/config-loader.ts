/**
 * Configuration Loader for Qin
 * 使用 c12 加载配置，支持多种格式和继承
 * 支持自动检测项目类型和插件
 */

import { loadConfig } from "c12";
import { join } from "path";
import { existsSync, readdirSync, statSync } from "fs";
import type { QinConfig, ParsedEntry, ValidationResult } from "../types";
import { PluginDetector, type DetectionResult } from "./plugin-detector";

export class ConfigLoader {
  private cwd: string;

  constructor(cwd?: string) {
    this.cwd = cwd || process.cwd();
  }

  /**
   * Load configuration using c12
   * 支持：qin.config.ts, qin.config.js, qin.config.json, .qinrc
   * 支持：extends 继承配置
   * 支持：自动检测项目类型
   */
  async load(): Promise<QinConfig & { _detected?: DetectionResult }> {
    try {
      const { config } = await loadConfig<QinConfig>({
        name: "qin",
        cwd: this.cwd,
        defaults: {},
      });

      // 自动检测项目类型
      const detector = new PluginDetector(this.cwd);
      const detection = await detector.detect();

      // 如果没有配置文件但检测到了项目内容，使用检测结果
      const hasConfig = config && Object.keys(config).length > 0;
      const hasDetection = detection.languages.length > 0 || detection.features.length > 0;

      if (!hasConfig && !hasDetection) {
        throw new Error(
          "No project detected. Create src/Main.java or run 'qin init' to start a new project."
        );
      }

      // 零配置模式：没有配置文件但检测到了项目
      if (!hasConfig && hasDetection) {
        // 完全依赖自动检测，这是零配置的核心！
      }

      // 合并配置：用户配置优先，检测结果作为默认值
      const mergedConfig: QinConfig = {
        ...config,
        // 自动设置入口（用户配置优先）
        entry: config?.entry || detection.entry || this.findEntryOptional(),
        // 自动设置前端配置
        client: config?.client || (detection.clientDir ? { root: detection.clientDir } : undefined),
      };

      // 如果有 Java 文件但没有入口，尝试查找
      if (detection.languages.includes("java") && !mergedConfig.entry) {
        mergedConfig.entry = detection.entry;
      }

      // 验证配置（对于纯前端项目，entry 可以为空）
      const validation = this.validateWithDetection(mergedConfig, detection);
      if (!validation.valid) {
        throw new Error(`Invalid configuration: ${validation.errors.join(", ")}`);
      }

      const finalConfig = this.applyDefaults(mergedConfig);
      
      // 附加检测结果供 CLI 使用
      return {
        ...finalConfig,
        _detected: detection,
      };
    } catch (error) {
      if (error instanceof Error && error.message.includes("not found")) {
        throw error;
      }
      throw error;
    }
  }

  /**
   * 可选的入口查找（不抛出错误）
   */
  private findEntryOptional(): string | undefined {
    try {
      return this.findEntry();
    } catch {
      return undefined;
    }
  }

  /**
   * 带检测结果的验证
   */
  private validateWithDetection(config: QinConfig, detection: DetectionResult): ValidationResult {
    const errors: string[] = [];

    if (!config) {
      errors.push("Configuration is empty");
      return { valid: false, errors };
    }

    // 如果检测到 Java 但没有入口，报错
    if (detection.languages.includes("java") && !config.entry) {
      errors.push("Detected Java files but no entry point found. Please specify 'entry' in qin.config.ts");
    }

    // 如果指定了入口，验证格式
    if (config.entry && !config.entry.endsWith(".java")) {
      errors.push("'entry' must be a .java file");
    }

    if (config.dependencies && typeof config.dependencies !== "object") {
      errors.push("'dependencies' must be an object");
    }

    // 纯前端项目（没有 Java）是允许的
    if (!detection.languages.includes("java") && detection.features.includes("frontend")) {
      // 纯前端项目，不需要 entry
      return { valid: true, errors: [] };
    }

    return {
      valid: errors.length === 0,
      errors,
    };
  }

  /**
   * Auto-detect entry file
   * Search order:
   * 1. src/Main.java
   * 2. src/server/Main.java
   * 3. src/{subdir}/Main.java (any subdirectory)
   */
  findEntry(): string {
    const candidates = ["src/Main.java", "src/server/Main.java"];

    // Check fixed paths first
    for (const candidate of candidates) {
      if (existsSync(join(this.cwd, candidate))) {
        return candidate;
      }
    }

    // Search src/*/Main.java
    const srcDir = join(this.cwd, "src");
    if (existsSync(srcDir)) {
      try {
        const entries = readdirSync(srcDir);
        for (const entry of entries) {
          const subDir = join(srcDir, entry);
          if (statSync(subDir).isDirectory()) {
            const mainJava = join(subDir, "Main.java");
            if (existsSync(mainJava)) {
              return `src/${entry}/Main.java`;
            }
          }
        }
      } catch {
        // Ignore errors
      }
    }

    throw new Error(
      "No entry file found. Please specify 'entry' in qin.config.ts or create src/Main.java"
    );
  }

  /**
   * Validate configuration
   */
  validate(config: QinConfig): ValidationResult {
    const errors: string[] = [];

    if (!config) {
      errors.push("Configuration is empty");
      return { valid: false, errors };
    }

    if (!config.entry) {
      errors.push("'entry' field is required");
    } else if (!config.entry.endsWith(".java")) {
      errors.push("'entry' must be a .java file");
    }

    if (config.dependencies && typeof config.dependencies !== "object") {
      errors.push("'dependencies' must be an object");
    }

    return {
      valid: errors.length === 0,
      errors,
    };
  }

  /**
   * Apply default values to configuration
   */
  private applyDefaults(config: QinConfig): QinConfig {
    const entry = config.entry;
    // jarName: 优先使用配置值，否则根据 name 生成，最后使用 app.jar
    const jarName =
      config.output?.jarName || (config.name ? `${config.name}.jar` : "app.jar");

    const result: QinConfig = {
      ...config,
      output: {
        dir: config.output?.dir || "dist",
        jarName,
      },
    };

    // 只有在有 Java 入口时才设置 Java 相关配置
    if (entry) {
      result.entry = entry;
      result.java = {
        version: config.java?.version || "17",
        sourceDir: config.java?.sourceDir || this.parseEntry(entry).srcDir,
      };
    }

    return result;
  }

  /**
   * Parse entry path to extract source directory and class name
   */
  parseEntry(entry?: string): ParsedEntry {
    if (!entry) {
      return { srcDir: "src", className: "Main", filePath: "src/Main.java" };
    }

    // Normalize path separators
    const normalized = entry.replace(/\\/g, "/");

    // Extract file name and directory
    const lastSlash = normalized.lastIndexOf("/");
    const fileName =
      lastSlash >= 0 ? normalized.substring(lastSlash + 1) : normalized;
    const srcDir = lastSlash >= 0 ? normalized.substring(0, lastSlash) : ".";

    // Remove .java extension to get simple class name
    const simpleClassName = fileName.replace(/\.java$/, "");

    // Try to read package declaration from file
    let className = simpleClassName;
    try {
      const filePath = join(this.cwd, entry);
      const content = require("fs").readFileSync(filePath, "utf-8");
      const packageMatch = content.match(/^\s*package\s+([\w.]+)\s*;/m);
      if (packageMatch) {
        className = `${packageMatch[1]}.${simpleClassName}`;
      }
    } catch {
      // If file can't be read, use simple class name
    }

    return {
      srcDir,
      className,
      filePath: entry,
    };
  }
}

/**
 * Standalone function for parsing entry path
 */
export function parseEntryPath(entry: string): ParsedEntry {
  const loader = new ConfigLoader();
  return loader.parseEntry(entry);
}
