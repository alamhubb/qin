/**
 * Fat Jar Builder for Qin
 * Creates Uber JARs containing all dependencies
 */

import { join } from "path";
import { mkdir, rm, readdir, writeFile, cp, readFile } from "fs/promises";
import { existsSync } from "fs";
import type { QinConfig, BuildResult } from "../types";
import { ConfigLoader } from "./config-loader";
import { DependencyResolver } from "./dependency-resolver";

/**
 * 从 java 命令获取 java.home 路径（Maven 的做法）
 */
async function getJavaHome(): Promise<string | null> {
  try {
    const proc = Bun.spawn(["java", "-XshowSettings:properties", "-version"], {
      stdout: "pipe",
      stderr: "pipe",
    });
    
    // java.home 输出在 stderr
    const stderr = await new Response(proc.stderr).text();
    await proc.exited;
    
    const match = stderr.match(/java\.home\s*=\s*(.+)/);
    if (match && match[1]) {
      return match[1].trim();
    }
  } catch {}
  return null;
}

/**
 * 查找 jar 命令路径
 */
async function findJarCommand(): Promise<string> {
  const jarExe = process.platform === "win32" ? "jar.exe" : "jar";

  // 1. 先尝试直接使用 jar
  try {
    const proc = Bun.spawn(["jar", "--version"], { stdout: "pipe", stderr: "pipe" });
    await proc.exited;
    if (proc.exitCode === 0) return "jar";
  } catch {}

  // 2. 从 JAVA_HOME 环境变量查找
  const envJavaHome = process.env.JAVA_HOME;
  if (envJavaHome) {
    const jarPath = join(envJavaHome, "bin", jarExe);
    if (existsSync(jarPath)) return jarPath;
  }

  // 3. 从 java -XshowSettings 获取 java.home（Maven 的做法）
  const javaHome = await getJavaHome();
  if (javaHome) {
    const jarPath = join(javaHome, "bin", jarExe);
    if (existsSync(jarPath)) return jarPath;
  }

  throw new Error("找不到 jar 命令。请设置 JAVA_HOME 环境变量或确保 JDK bin 目录在 PATH 中");
}

export interface FatJarBuilderOptions {
  debug?: boolean;
}

export class FatJarBuilder {
  private config: QinConfig;
  private options: FatJarBuilderOptions;
  private cwd: string;
  private tempDir: string;
  private outputDir: string;
  private jarCommand: string = "jar";

  constructor(config: QinConfig, options: FatJarBuilderOptions = {}, cwd?: string) {
    this.config = config;
    this.options = options;
    this.cwd = cwd || process.cwd();
    this.tempDir = join(this.cwd, "build", "temp");
    this.outputDir = join(this.cwd, config.output?.dir || "dist");
  }

  /**
   * 初始化 jar 命令路径
   */
  private async initJarCommand(): Promise<void> {
    this.jarCommand = await findJarCommand();
  }

  /**
   * Build Fat Jar - main entry point
   */
  async build(): Promise<BuildResult> {
    try {
      // 0. 初始化 jar 命令
      await this.initJarCommand();

      // 1. Create temp directory
      await this.createTempDir();

      // 2. Resolve and extract dependencies
      // 优先使用缓存的 classpath（由 sync 命令生成）
      let jarPaths: string[] = [];
      const classpathCache = join(this.cwd, "build", ".cache", "classpath.json");
      
      if (existsSync(classpathCache)) {
        const cachedData = JSON.parse(await readFile(classpathCache, "utf-8"));
        jarPaths = cachedData.classpath || [];
      } else {
        // 回退到实时解析
        const resolver = new DependencyResolver("cs", this.config.repositories, undefined, this.cwd, this.config.localRep);
        const deps = this.config.dependencies || {};
        const depStrings = Object.entries(deps).map(([name, version]) => `${name}:${version}`);
        jarPaths = await resolver.getJarPaths(depStrings);
      }
      
      if (jarPaths.length > 0) {
        await this.extractJars(jarPaths);
        await this.cleanSignatures();
      }

      // 3. Compile source code to temp directory
      await this.compileSource();

      // 4. Generate manifest
      const configLoader = new ConfigLoader(this.cwd);
      const parsed = configLoader.parseEntry(this.config.entry);
      await this.generateManifest(parsed.className);

      // 5. Package final JAR
      const jarName = this.config.output?.jarName || "app.jar";
      const outputPath = join(this.outputDir, jarName);
      await this.packageJar(outputPath);

      // 6. Cleanup (unless debug mode)
      if (!this.options.debug) {
        await this.cleanup();
      }

      return {
        success: true,
        outputPath,
      };
    } catch (error) {
      return {
        success: false,
        error: error instanceof Error ? error.message : "Unknown build error",
      };
    }
  }

  /**
   * Create temporary working directory
   */
  async createTempDir(): Promise<void> {
    await rm(this.tempDir, { recursive: true, force: true });
    await mkdir(this.tempDir, { recursive: true });
  }

  /**
   * Extract all dependency JARs into temp directory
   */
  async extractJars(jarPaths: string[]): Promise<void> {
    for (const jarPath of jarPaths) {
      const proc = Bun.spawn([this.jarCommand, "-xf", jarPath], {
        cwd: this.tempDir,
        stdout: "pipe",
        stderr: "pipe",
      });

      await proc.exited;

      if (proc.exitCode !== 0) {
        const stderr = await new Response(proc.stderr).text();
        throw new Error(`Failed to extract ${jarPath}: ${stderr}`);
      }
    }
  }

  /**
   * Clean signature files from META-INF
   * This is critical to prevent SecurityException when running the Fat Jar
   */
  async cleanSignatures(): Promise<void> {
    const metaInfDir = join(this.tempDir, "META-INF");
    
    try {
      const files = await readdir(metaInfDir);
      
      for (const file of files) {
        const ext = file.toLowerCase();
        if (ext.endsWith(".sf") || ext.endsWith(".dsa") || ext.endsWith(".rsa")) {
          await rm(join(metaInfDir, file), { force: true });
        }
      }
    } catch {
      // META-INF might not exist, that's fine
    }
  }

  /**
   * Compile source code to temp directory
   */
  async compileSource(): Promise<void> {
    const configLoader = new ConfigLoader(this.cwd);
    const parsed = configLoader.parseEntry(this.config.entry);
    const srcDir = join(this.cwd, parsed.srcDir);

    // Find all Java files
    const javaFiles: string[] = [];
    const glob = new Bun.Glob("**/*.java");
    for await (const file of glob.scan({ cwd: srcDir, absolute: true })) {
      javaFiles.push(file);
    }

    if (javaFiles.length === 0) {
      throw new Error(`No Java files found in ${srcDir}`);
    }

    // Build classpath from temp directory (for dependencies)
    const args = ["-d", this.tempDir];
    
    // Add temp dir to classpath for dependency classes
    const depClassesExist = await this.hasClasses(this.tempDir);
    if (depClassesExist) {
      args.push("-cp", this.tempDir);
    }

    args.push(...javaFiles);

    const proc = Bun.spawn(["javac", ...args], {
      cwd: this.cwd,
      stdout: "pipe",
      stderr: "pipe",
    });

    const stderr = await new Response(proc.stderr).text();
    await proc.exited;

    if (proc.exitCode !== 0) {
      throw new Error(`Compilation failed: ${stderr}`);
    }

    // Copy resource files to temp directory
    await this.copyResources(parsed.srcDir);
  }

  /**
   * Copy resource files to temp directory for JAR packaging
   * Searches for resources in multiple locations:
   * 1. src/resources/
   * 2. src/main/resources/ (Maven style)
   * 3. {srcDir}/resources/ (relative to source)
   */
  private async copyResources(srcDir: string): Promise<void> {
    const resourceDirs = [
      join(this.cwd, "src", "resources"),
      join(this.cwd, "src", "main", "resources"),
      join(this.cwd, srcDir, "resources"),
    ];

    for (const resourceDir of resourceDirs) {
      if (existsSync(resourceDir)) {
        await this.copyDir(resourceDir, this.tempDir);
      }
    }
  }

  /**
   * Recursively copy directory contents
   */
  private async copyDir(src: string, dest: string): Promise<void> {
    const entries = await readdir(src, { withFileTypes: true });

    for (const entry of entries) {
      const srcPath = join(src, entry.name);
      const destPath = join(dest, entry.name);

      if (entry.isDirectory()) {
        await mkdir(destPath, { recursive: true });
        await this.copyDir(srcPath, destPath);
      } else {
        await cp(srcPath, destPath, { force: true });
      }
    }
  }

  /**
   * Generate MANIFEST.MF file
   */
  async generateManifest(mainClass: string): Promise<void> {
    const metaInfDir = join(this.tempDir, "META-INF");
    await mkdir(metaInfDir, { recursive: true });

    const manifestContent = `Manifest-Version: 1.0
Main-Class: ${mainClass}
Created-By: Qin (Java-Vite Build Tool)
`;

    await writeFile(join(metaInfDir, "MANIFEST.MF"), manifestContent);
  }

  /**
   * Package all files into final JAR
   */
  async packageJar(outputPath: string): Promise<void> {
    // Ensure output directory exists
    await mkdir(this.outputDir, { recursive: true });

    const manifestPath = join(this.tempDir, "META-INF", "MANIFEST.MF");
    
    const proc = Bun.spawn(
      [this.jarCommand, "-cvfm", outputPath, manifestPath, "-C", this.tempDir, "."],
      {
        cwd: this.cwd,
        stdout: "pipe",
        stderr: "pipe",
      }
    );

    await proc.exited;

    if (proc.exitCode !== 0) {
      const stderr = await new Response(proc.stderr).text();
      throw new Error(`Failed to create JAR: ${stderr}`);
    }
  }

  /**
   * Clean up temporary directory
   */
  async cleanup(): Promise<void> {
    await rm(this.tempDir, { recursive: true, force: true });
  }

  /**
   * Check if directory contains any .class files
   */
  private async hasClasses(dir: string): Promise<boolean> {
    try {
      const glob = new Bun.Glob("**/*.class");
      for await (const _ of glob.scan({ cwd: dir })) {
        return true;
      }
    } catch {
      // Directory doesn't exist
    }
    return false;
  }
}

/**
 * Generate manifest content for a given main class
 * Useful for testing
 */
export function generateManifestContent(mainClass: string): string {
  return `Manifest-Version: 1.0
Main-Class: ${mainClass}
Created-By: Qin (Java-Vite Build Tool)
`;
}

/**
 * Parse manifest content to extract main class
 * Useful for testing
 */
export function parseManifestMainClass(content: string): string | null {
  const match = content.match(/^Main-Class:\s*(.+)$/m);
  return match && match[1] ? match[1].trim() : null;
}
