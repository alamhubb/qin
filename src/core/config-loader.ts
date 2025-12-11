/**
 * Configuration Loader for Qin
 * Dynamically loads qin.config.ts from the current working directory
 */

import { join } from "path";
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
      
      const validation = this.validate(config);
      if (!validation.valid) {
        throw new Error(`Invalid configuration: ${validation.errors.join(", ")}`);
      }
      
      return this.applyDefaults(config);
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

    if (config.dependencies && !Array.isArray(config.dependencies)) {
      errors.push("'dependencies' must be an array");
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
    return {
      ...config,
      output: {
        dir: config.output?.dir || "dist",
        jarName: config.output?.jarName || "app.jar",
      },
      java: {
        version: config.java?.version || "17",
        sourceDir: config.java?.sourceDir || this.parseEntry(config.entry).srcDir,
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
