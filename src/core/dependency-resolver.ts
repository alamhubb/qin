/**
 * Dependency Resolver for Qin
 * Uses Coursier to resolve Maven dependencies
 * Supports local workspace packages
 * 
 * JAR 包下载到项目的 repository/ 目录（类似 node_modules）
 */

import semver from "semver";
import { join, basename } from "path";
import { existsSync, mkdirSync, copyFileSync } from "fs";
import type { ResolveResult, Repository } from "../types";
import type { WorkspacePackage } from "./workspace-loader";

// 默认仓库（阿里云镜像优先）
const DEFAULT_REPOS = [
  "https://maven.aliyun.com/repository/public",
  "https://repo1.maven.org/maven2",
];

/**
 * 获取全局 repository 目录
 * ~/.qin/repository (类似 Maven 的 ~/.m2/repository)
 */
function getGlobalRepoDir(): string {
  const home = process.env.HOME || process.env.USERPROFILE || "";
  return join(home, ".qin", "repository");
}

export class DependencyResolver {
  private csCommand: string;
  private repositories: string[];
  private localPackages: Map<string, WorkspacePackage>;
  private projectRoot: string;
  private repoDir: string;
  private useLocalRep: boolean;

  constructor(
    csCommand: string = "cs", 
    repos?: Repository[],
    localPackages?: Map<string, WorkspacePackage>,
    projectRoot: string = process.cwd(),
    localRep: boolean = false
  ) {
    this.csCommand = csCommand;
    this.localPackages = localPackages || new Map();
    this.projectRoot = projectRoot;
    this.useLocalRep = localRep;
    
    // localRep: true -> 项目本地 ./repository
    // localRep: false -> 全局 ~/.qin/repository
    this.repoDir = localRep 
      ? join(projectRoot, "repository")
      : getGlobalRepoDir();
    
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
   * JAR 包会被复制到项目的 repository/ 目录
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
      const globalJarPaths = this.parseClasspath(classpath);

      // 复制 JAR 到项目的 repository/ 目录
      const localJarPaths = this.copyToRepository(globalJarPaths);

      return {
        success: true,
        classpath: this.buildClasspath(localJarPaths),
        jarPaths: localJarPaths,
      };
    } catch (error) {
      return {
        success: false,
        error: error instanceof Error ? error.message : "Unknown error during dependency resolution",
      };
    }
  }

  /**
   * 复制 JAR 文件到项目的 repository/ 目录
   * 按 Maven 风格组织：org/springframework/boot/spring-boot-starter-web-3.2.0.jar
   */
  private copyToRepository(globalPaths: string[]): string[] {
    // 确保 repository 目录存在
    if (!existsSync(this.repoDir)) {
      mkdirSync(this.repoDir, { recursive: true });
    }

    const localPaths: string[] = [];

    for (const globalPath of globalPaths) {
      if (!globalPath.endsWith(".jar")) continue;

      // 从路径中提取 groupId（Maven 缓存路径格式）
      // 例如: ~/.cache/coursier/v1/https/repo1.maven.org/maven2/org/springframework/boot/spring-boot-starter-web/3.2.0/spring-boot-starter-web-3.2.0.jar
      const groupPath = this.extractGroupPath(globalPath);
      const jarName = basename(globalPath);
      
      // 创建目录结构：repository/org/springframework/boot/
      const targetDir = join(this.repoDir, groupPath);
      if (!existsSync(targetDir)) {
        mkdirSync(targetDir, { recursive: true });
      }

      const localPath = join(targetDir, jarName);

      // 如果本地不存在，复制过来
      if (!existsSync(localPath)) {
        copyFileSync(globalPath, localPath);
      }

      localPaths.push(localPath);
    }

    return localPaths;
  }

  /**
   * 从 Maven 缓存路径提取 groupId 路径
   * 输入: .../org/springframework/boot/spring-boot-starter-web/3.2.0/xxx.jar
   * 输出: org/springframework/boot
   */
  private extractGroupPath(jarPath: string): string {
    // 标准化路径分隔符
    const normalized = jarPath.replace(/\\/g, "/");
    
    // 查找 maven2/ 或 public/ 后面的路径
    const patterns = ["/maven2/", "/public/", "/repository/"];
    
    for (const pattern of patterns) {
      const idx = normalized.indexOf(pattern);
      if (idx !== -1) {
        // 获取 pattern 后面的路径
        const afterPattern = normalized.substring(idx + pattern.length);
        // 路径格式: org/springframework/boot/spring-boot-starter-web/3.2.0/xxx.jar
        // 我们需要: org/springframework/boot
        const parts = afterPattern.split("/");
        // 移除最后3个部分（artifactId/version/filename）
        if (parts.length >= 4) {
          return parts.slice(0, -3).join("/");
        }
      }
    }

    // 回退：从 JAR 名称猜测
    const jarName = basename(jarPath, ".jar");
    // spring-boot-starter-web-3.2.0 -> 无法确定 groupId，放在根目录
    return "";
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
