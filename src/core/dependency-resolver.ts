/**
 * Dependency Resolver for Qin
 * Uses Coursier to resolve Maven dependencies
 */

import type { ResolveResult, RepositoryConfig } from "../types";

// 阿里云 Maven 镜像
const ALIYUN_MIRROR = "https://maven.aliyun.com/repository/public";

// 默认仓库
const DEFAULT_REPOS = [
  ALIYUN_MIRROR,  // 国内镜像优先
  "https://repo1.maven.org/maven2",  // Maven Central 备用
];

export class DependencyResolver {
  private csCommand: string;
  private repositories: string[];

  constructor(csCommand: string = "cs", repoConfig?: RepositoryConfig) {
    this.csCommand = csCommand;
    
    // 配置仓库
    if (repoConfig?.urls && repoConfig.urls.length > 0) {
      // 使用自定义仓库
      this.repositories = repoConfig.urls;
    } else if (repoConfig?.useChinaMirror === false) {
      // 明确禁用国内镜像
      this.repositories = ["https://repo1.maven.org/maven2"];
    } else {
      // 默认使用阿里云镜像
      this.repositories = DEFAULT_REPOS;
    }
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
