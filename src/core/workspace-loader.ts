/**
 * Workspace Loader for Qin
 * Handles monorepo/multi-project configurations
 */

import { join, dirname, resolve } from "path";
import { existsSync, readdirSync, statSync } from "fs";
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
   */
  async findWorkspaceRoot(): Promise<string | null> {
    let dir = this.cwd;
    const root = resolve("/");

    while (dir !== root) {
      const configPath = join(dir, "qin.config.ts");
      if (existsSync(configPath)) {
        try {
          const module = await import(configPath);
          const config = module.default as QinConfig;
          if (config.packages && config.packages.length > 0) {
            this.workspaceRoot = dir;
            return dir;
          }
        } catch {
          // Continue searching
        }
      }
      dir = dirname(dir);
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

    for (const pattern of rootConfig.packages) {
      const packagePaths = this.resolveGlob(pattern, baseDir);
      
      for (const pkgPath of packagePaths) {
        await this.loadPackage(pkgPath);
      }
    }

    return this.packages;
  }

  /**
   * Resolve glob pattern to actual paths
   */
  private resolveGlob(pattern: string, baseDir: string): string[] {
    const paths: string[] = [];
    
    if (pattern.includes("*")) {
      // Handle glob pattern like "packages/*"
      const [dirPart] = pattern.split("*");
      const basePath = join(baseDir, dirPart);
      
      if (existsSync(basePath)) {
        const entries = readdirSync(basePath);
        for (const entry of entries) {
          const fullPath = join(basePath, entry);
          if (statSync(fullPath).isDirectory()) {
            // Check if it has qin.config.ts
            if (existsSync(join(fullPath, "qin.config.ts"))) {
              paths.push(fullPath);
            }
          }
        }
      }
    } else {
      // Direct path
      const fullPath = join(baseDir, pattern);
      if (existsSync(fullPath) && existsSync(join(fullPath, "qin.config.ts"))) {
        paths.push(fullPath);
      }
    }

    return paths;
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
          classesDir: join(pkgPath, ".qin", "classes"),
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
