/**
 * Workspace Loader for Qin
 * Handles monorepo/multi-project configurations
 */

import { join, dirname } from "path";
import { findUp } from "find-up";
import { glob } from "tinyglobby";
import type { QinConfig } from "../types";

export interface WorkspacePackage {
  name: string;
  path: string;
  config: QinConfig;
  classesDir: string;
}

export interface WorkspaceInfo {
  root: string;
  config: QinConfig;
  packages: Map<string, WorkspacePackage>;
}

export class WorkspaceLoader {
  private cwd: string;
  private packages: Map<string, WorkspacePackage> = new Map();
  private workspaceRoot: string | null = null;

  constructor(cwd?: string) {
    this.cwd = cwd || process.cwd();
  }

  /**
   * Find workspace root by looking for qin.config.ts with packages field
   * 使用 find-up 库向上查找
   */
  async findWorkspaceRoot(): Promise<string | null> {
    // 先找到所有 qin.config.ts 文件
    const configPath = await findUp("qin.config.ts", { cwd: this.cwd });
    
    if (!configPath) {
      return null;
    }

    // 从找到的配置文件开始，向上查找有 packages 字段的配置
    let dir = dirname(configPath);
    
    while (dir) {
      const configFile = join(dir, "qin.config.ts");
      try {
        const module = await import(configFile);
        const config = module.default as QinConfig;
        if (config.packages && config.packages.length > 0) {
          this.workspaceRoot = dir;
          return dir;
        }
      } catch {
        // Continue searching
      }
      
      // 向上一级
      const parentDir = dirname(dir);
      if (parentDir === dir) break; // 到达根目录
      
      // 检查父目录是否有 qin.config.ts
      const parentConfig = await findUp("qin.config.ts", { cwd: parentDir });
      if (!parentConfig || dirname(parentConfig) === dir) break;
      
      dir = dirname(parentConfig);
    }

    return null;
  }

  /**
   * Load workspace packages - auto-discovers workspace root
   */
  async loadPackages(projectConfig: QinConfig): Promise<Map<string, WorkspacePackage>> {
    // First check if current config has packages (is workspace root)
    if (projectConfig.packages && projectConfig.packages.length > 0) {
      this.workspaceRoot = this.cwd;
      return this.loadPackagesFromRoot(projectConfig);
    }

    // Otherwise, search for workspace root
    const workspaceRoot = await this.findWorkspaceRoot();
    if (!workspaceRoot) {
      return this.packages; // No workspace found
    }

    // Load workspace root config
    const configPath = join(workspaceRoot, "qin.config.ts");
    const module = await import(configPath);
    const rootConfig = module.default as QinConfig;

    return this.loadPackagesFromRoot(rootConfig, workspaceRoot);
  }

  /**
   * Load packages from workspace root config
   */
  private async loadPackagesFromRoot(
    rootConfig: QinConfig, 
    rootDir?: string
  ): Promise<Map<string, WorkspacePackage>> {
    const baseDir = rootDir || this.cwd;

    if (!rootConfig.packages || rootConfig.packages.length === 0) {
      return this.packages;
    }

    // 使用 tinyglobby 查找所有匹配的 qin.config.ts 文件
    const patterns = rootConfig.packages.map(p => `${p}/qin.config.ts`);
    const configFiles = await glob(patterns, {
      cwd: baseDir,
      absolute: true,
      onlyFiles: true,
    });

    // 加载每个找到的包
    for (const configFile of configFiles) {
      const pkgPath = dirname(configFile);
      await this.loadPackage(pkgPath);
    }

    return this.packages;
  }

  /**
   * Load a single package
   */
  private async loadPackage(pkgPath: string): Promise<void> {
    const configPath = join(pkgPath, "qin.config.ts");
    
    try {
      const module = await import(configPath);
      const config = module.default as QinConfig;
      
      if (config.name) {
        this.packages.set(config.name, {
          name: config.name,
          path: pkgPath,
          config,
          classesDir: join(pkgPath, "build", "classes"),
        });
      }
    } catch (error) {
      // Skip packages that fail to load
      console.warn(`Warning: Failed to load package at ${pkgPath}`);
    }
  }

  /**
   * Check if a dependency is a local package
   */
  isLocalPackage(name: string): boolean {
    return this.packages.has(name);
  }

  /**
   * Get local package info
   */
  getPackage(name: string): WorkspacePackage | undefined {
    return this.packages.get(name);
  }

  /**
   * Get all local packages
   */
  getAllPackages(): WorkspacePackage[] {
    return Array.from(this.packages.values());
  }

  /**
   * Get workspace root directory
   */
  getWorkspaceRoot(): string | null {
    return this.workspaceRoot;
  }
}
