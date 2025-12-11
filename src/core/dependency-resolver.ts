/**
 * Dependency Resolver for Qin
 * Uses Coursier to resolve Maven dependencies
 * Supports local workspace packages
 */

import semver from "semver";
import type { ResolveResult, Repository } from "../types";
import type { WorkspacePackage } from "./workspace-loader";

// 默认仓库（阿里云镜像优先）
const DEFAULT_REPOS = [
  "https://maven.aliyun.com/repository/public",
  "https://repo1.maven.org/maven2",
];

export class DependencyResolver {
  private csCommand: string;
  private repositories: string[];
  private localPackages: Map<string, WorkspacePackage>;

  constructor(
    csCommand: string = "cs", 
    repos?: Repository[],
    localPackages?: Map<string, WorkspacePackage>
  ) {
    this.csCommand = csCommand;
    this.localPackages = localPackages || new Map();
    
    // 解析仓库配置
    if (repos && repos.length > 0) {
      this.repositories = repos.map(repo => 
        typeof repo === "string" ? repo : repo.url
      );
    } else {
      // 默认使用阿里云镜像
      this.repositories = DEFAULT_REPOS;
    }
  }

  /**
   * Resolve dependencies (object format) and return classpath
   * Supports both Maven dependencies and local packages
   * 
   * 本地包版本规则：
   * - "*" 匹配任意版本
   * - "1.0.0" 精确匹配版本
   */
  async resolveFromObject(deps: Record<string, string>): Promise<string> {
    if (!deps || Object.keys(deps).length === 0) {
      return "";
    }

    const mavenDeps: string[] = [];
    const localPaths: string[] = [];

    for (const [name, version] of Object.entries(deps)) {
      // Check if it's a local package
      if (this.localPackages.has(name)) {
        const pkg = this.localPackages.get(name)!;
        
        // 版本校验
        if (version !== "*") {
          const pkgVersion = pkg.config.version || "0.0.0";
          if (!this.checkVersionMatch(version, pkgVersion)) {
            throw new Error(
              `本地包 "${name}" 版本不匹配: 需要 ${version}, 实际 ${pkgVersion}`
            );
          }
        }
        
        localPaths.push(pkg.classesDir);
      } else {
        // Maven dependency: convert to groupId:artifactId:version format
        mavenDeps.push(`${name}:${version}`);
      }
    }

    // Resolve Maven dependencies
    let mavenClasspath = "";
    if (mavenDeps.length > 0) {
      mavenClasspath = await this.resolve(mavenDeps);
    }

    // Combine local and Maven classpaths
    const allPaths = [...localPaths];
    if (mavenClasspath) {
      allPaths.push(...this.parseClasspath(mavenClasspath));
    }

    return this.buildClasspath(allPaths);
  }

  /**
   * 检查版本是否匹配
   * 使用 semver 库支持完整的 npm 版本语法
   * 
   * 支持的语法：
   * - "*" 或 "x" - 任意版本
   * - "1.0.0" - 精确匹配
   * - "^1.0.0" - 兼容版本 (>=1.0.0 <2.0.0)
   * - "~1.0.0" - 补丁版本 (>=1.0.0 <1.1.0)
   * - ">=1.0.0" - 大于等于
   * - "1.0.0 - 2.0.0" - 范围
   * - "1.x" 或 "1.*" - 主版本匹配
   */
  private checkVersionMatch(required: string, actual: string): boolean {
    // 清理版本号（移除可能的 v 前缀）
    const cleanActual = semver.clean(actual) || actual;
    
    // 使用 semver.satisfies 检查版本是否满足范围
    return semver.satisfies(cleanActual, required);
  }

  /**
   * Resolve dependencies using Coursier and return classpath
   */
  async resolve(deps: string[]): Promise<string> {
    if (!deps || deps.length === 0) {
      return "";
    }

    const result = await this.resolveWithDetails(deps);
    if (!result.success) {
      throw new Error(result.error || "Failed to resolve dependencies");
    }

    return result.classpath || "";
  }

  /**
   * Resolve dependencies and return detailed result
   */
  async resolveWithDetails(deps: string[]): Promise<ResolveResult> {
    if (!deps || deps.length === 0) {
      return { success: true, classpath: "", jarPaths: [] };
    }

    try {
      // Validate dependency format
      for (const dep of deps) {
        if (!this.isValidDependency(dep)) {
          return {
            success: false,
            error: `Invalid dependency format: "${dep}". Expected format: groupId:artifactId:version`,
          };
        }
      }

      // Build Coursier arguments with repository configuration
      const args = ["fetch", ...deps, "--classpath"];
      
      // Add repository arguments
      for (const repo of this.repositories) {
        args.push("-r", repo);
      }

      const proc = Bun.spawn([this.csCommand, ...args], {
        stdout: "pipe",
        stderr: "pipe",
      });

      const [stdout, stderr] = await Promise.all([
        new Response(proc.stdout).text(),
        new Response(proc.stderr).text(),
      ]);

      await proc.exited;

      if (proc.exitCode !== 0) {
        return {
          success: false,
          error: stderr.trim() || "Coursier failed to resolve dependencies",
        };
      }

      const classpath = stdout.trim();
      const jarPaths = this.parseClasspath(classpath);

      return {
        success: true,
        classpath,
        jarPaths,
      };
    } catch (error) {
      return {
        success: false,
        error: error instanceof Error ? error.message : "Unknown error during dependency resolution",
      };
    }
  }

  /**
   * Get individual JAR paths from dependencies
   */
  async getJarPaths(deps: string[]): Promise<string[]> {
    const result = await this.resolveWithDetails(deps);
    return result.jarPaths || [];
  }

  /**
   * Validate dependency coordinate format (groupId:artifactId:version)
   */
  isValidDependency(dep: string): boolean {
    const parts = dep.split(":");
    // Basic format: groupId:artifactId:version
    // Extended format: groupId:artifactId:version:classifier or groupId:artifactId:packaging:version
    return parts.length >= 3 && parts.every(part => part.length > 0);
  }

  /**
   * Parse classpath string into individual paths
   */
  parseClasspath(classpath: string): string[] {
    if (!classpath) return [];
    const separator = getClasspathSeparator();
    return classpath.split(separator).filter(p => p.length > 0);
  }

  /**
   * Build classpath string from paths
   */
  buildClasspath(paths: string[]): string {
    const separator = getClasspathSeparator();
    return paths.join(separator);
  }

  /**
   * Build full classpath including output directory
   */
  buildFullClasspath(outputDir: string, jarPaths: string[]): string {
    const allPaths = [outputDir, ...jarPaths];
    return this.buildClasspath(allPaths);
  }
}

/**
 * Get platform-specific classpath separator
 */
export function getClasspathSeparator(): string {
  return process.platform === "win32" ? ";" : ":";
}

/**
 * Check if running on Windows
 */
export function isWindows(): boolean {
  return process.platform === "win32";
}

/**
 * Build classpath from paths (standalone function)
 */
export function buildClasspath(paths: string[]): string {
  const separator = getClasspathSeparator();
  return paths.join(separator);
}

/**
 * Parse classpath string into paths (standalone function)
 */
export function parseClasspath(classpath: string): string[] {
  if (!classpath) return [];
  const separator = getClasspathSeparator();
  return classpath.split(separator).filter(p => p.length > 0);
}
