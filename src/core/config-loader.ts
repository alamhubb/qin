/**
 * Configuration Loader for Qin
 * Dynamically loads qin.config.ts from the current working directory
 */

import { join } from "path";
import { existsSync, readdirSync, statSync } from "fs";
import type { QinConfig, ParsedEntry, ValidationResult } from "../types";

export class ConfigLoader {
  private cwd: string;

  constructor(cwd?: string) {
    this.cwd = cwd || process.cwd();
  }

  /**
   * Load configuration from qin.config.ts
   */
  async load(): Promise<QinConfig> {
    const configPath = join(this.cwd, "qin.config.ts");
    
    try {
      const module = await import(configPath);
      const config = module.default as QinConfig;
      
      // Auto-detect entry if not specified
      const resolvedConfig = {
        ...config,
        entry: config.entry || this.findEntry(),
      };
      
      const validation = this.validate(resolvedConfig);
      if (!validation.valid) {
        throw new Error(`Invalid configuration: ${validation.errors.join(", ")}`);
      }
      
      return this.applyDefaults(resolvedConfig);
    } catch (error) {
      if (error instanceof Error && error.message.includes("Cannot find module")) {
        throw new Error(
          "Configuration file not found. Please run 'qin init' to create a new project."
        );
      }
      throw error;
    }
  }

  /**
   * Auto-detect entry file
   * Search order:
   * 1. src/Main.java
   * 2. src/server/Main.java
   * 3. src/{subdir}/Main.java (any subdirectory)
   */
  findEntry(): string {
    const candidates = [
      "src/Main.java",
      "src/server/Main.java",
    ];

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
    const entry = config.entry!;
    // jarName: 优先使用配置值，否则根据 name 生成，最后使用 app.jar
    const jarName = config.output?.jarName 
      || (config.name ? `${config.name}.jar` : "app.jar");
    
    return {
      ...config,
      entry,
      output: {
        dir: config.output?.dir || "dist",
        jarName,
      },
      java: {
        version: config.java?.version || "17",
        sourceDir: config.java?.sourceDir || this.parseEntry(entry).srcDir,
      },
    };
  }

  /**
   * Parse entry path to extract source directory and class name
   * Example: "src/Main.java" → { srcDir: "src", className: "Main", filePath: "src/Main.java" }
   * Example: "src/server/Main.java" with "package server;" → { srcDir: "src", className: "server.Main", filePath: "..." }
   */
  parseEntry(entry: string): ParsedEntry {
    // Normalize path separators
    const normalized = entry.replace(/\\/g, "/");
    
    // Extract file name and directory
    const lastSlash = normalized.lastIndexOf("/");
    const fileName = lastSlash >= 0 ? normalized.substring(lastSlash + 1) : normalized;
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
 * Useful for testing and external use
 */
export function parseEntryPath(entry: string): ParsedEntry {
  const loader = new ConfigLoader();
  return loader.parseEntry(entry);
}
