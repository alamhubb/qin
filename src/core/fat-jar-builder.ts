/**
 * Fat Jar Builder for Qin
 * Creates Uber JARs containing all dependencies
 */

import { join } from "path";
import { mkdir, rm, readdir, writeFile } from "fs/promises";
import type { QinConfig, BuildResult } from "../types";
import { ConfigLoader } from "./config-loader";
import { DependencyResolver } from "./dependency-resolver";

export interface FatJarBuilderOptions {
  debug?: boolean;
}

export class FatJarBuilder {
  private config: QinConfig;
  private options: FatJarBuilderOptions;
  private cwd: string;
  private tempDir: string;
  private outputDir: string;

  constructor(config: QinConfig, options: FatJarBuilderOptions = {}, cwd?: string) {
    this.config = config;
    this.options = options;
    this.cwd = cwd || process.cwd();
    this.tempDir = join(this.cwd, ".qin", "temp");
    this.outputDir = join(this.cwd, config.output?.dir || "dist");
  }

  /**
   * Build Fat Jar - main entry point
   */
  async build(): Promise<BuildResult> {
    try {
      // 1. Create temp directory
      await this.createTempDir();

      // 2. Resolve and extract dependencies
      const resolver = new DependencyResolver("cs", this.config.repositories);
      const jarPaths = await resolver.getJarPaths(this.config.dependencies || []);
      
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
      const proc = Bun.spawn(["jar", "-xf", jarPath], {
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
      ["jar", "-cvfm", outputPath, manifestPath, "-C", this.tempDir, "."],
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
  return match ? match[1].trim() : null;
}
