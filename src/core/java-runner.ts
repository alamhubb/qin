/**
 * Java Runner for Qin
 * Compiles and runs Java programs
 */

import { join } from "path";
import { mkdir } from "fs/promises";
import type { QinConfig, CompileResult, ParsedEntry } from "../types";
import { ConfigLoader } from "./config-loader";
import { getClasspathSeparator } from "./dependency-resolver";

export class JavaRunner {
  private config: QinConfig;
  private classpath: string;
  private cwd: string;
  private outputDir: string;

  constructor(config: QinConfig, classpath: string, cwd?: string) {
    this.config = config;
    this.classpath = classpath;
    this.cwd = cwd || process.cwd();
    this.outputDir = join(this.cwd, ".qin", "classes");
  }

  /**
   * Compile Java source files
   */
  async compile(): Promise<CompileResult> {
    // Ensure output directory exists
    await mkdir(this.outputDir, { recursive: true });

    const configLoader = new ConfigLoader(this.cwd);
    const parsed = configLoader.parseEntry(this.config.entry);
    const srcDir = join(this.cwd, parsed.srcDir);

    // Find all Java files in source directory
    const javaFiles = await this.findJavaFiles(srcDir);
    
    if (javaFiles.length === 0) {
      return {
        success: false,
        error: `No Java files found in ${srcDir}`,
        compiledFiles: 0,
        outputDir: this.outputDir,
      };
    }

    // Build javac command
    const args = this.buildCompileArgs(javaFiles);

    try {
      const proc = Bun.spawn(["javac", ...args], {
        cwd: this.cwd,
        stdout: "pipe",
        stderr: "pipe",
      });

      const stderr = await new Response(proc.stderr).text();
      await proc.exited;

      if (proc.exitCode !== 0) {
        return {
          success: false,
          error: stderr.trim() || "Compilation failed",
          compiledFiles: 0,
          outputDir: this.outputDir,
        };
      }

      return {
        success: true,
        compiledFiles: javaFiles.length,
        outputDir: this.outputDir,
      };
    } catch (error) {
      return {
        success: false,
        error: error instanceof Error ? error.message : "Unknown compilation error",
        compiledFiles: 0,
        outputDir: this.outputDir,
      };
    }
  }

  /**
   * Run compiled Java program
   */
  async run(args: string[] = []): Promise<void> {
    const configLoader = new ConfigLoader(this.cwd);
    const parsed = configLoader.parseEntry(this.config.entry);
    
    // Build full classpath including output directory
    const fullClasspath = this.buildFullClasspath();
    
    // Build java command
    const javaArgs = ["-cp", fullClasspath, parsed.className, ...args];

    const proc = Bun.spawn(["java", ...javaArgs], {
      cwd: this.cwd,
      stdout: "inherit",
      stderr: "inherit",
    });

    await proc.exited;

    if (proc.exitCode !== 0) {
      throw new Error(`Java program exited with code ${proc.exitCode}`);
    }
  }

  /**
   * Compile and run in one step
   */
  async compileAndRun(args: string[] = []): Promise<void> {
    const compileResult = await this.compile();
    
    if (!compileResult.success) {
      throw new Error(`Compilation failed: ${compileResult.error}`);
    }

    await this.run(args);
  }

  /**
   * Build javac arguments
   */
  private buildCompileArgs(javaFiles: string[]): string[] {
    const args: string[] = [];

    // Output directory
    args.push("-d", this.outputDir);

    // Classpath (if any dependencies)
    if (this.classpath) {
      args.push("-cp", this.classpath);
    }

    // Source files
    args.push(...javaFiles);

    return args;
  }

  /**
   * Build full classpath including output directory
   */
  private buildFullClasspath(): string {
    const separator = getClasspathSeparator();
    if (this.classpath) {
      return `${this.outputDir}${separator}${this.classpath}`;
    }
    return this.outputDir;
  }

  /**
   * Find all Java files in a directory recursively
   */
  private async findJavaFiles(dir: string): Promise<string[]> {
    const files: string[] = [];
    
    try {
      const glob = new Bun.Glob("**/*.java");
      for await (const file of glob.scan({ cwd: dir, absolute: true })) {
        files.push(file);
      }
    } catch {
      // Directory doesn't exist or can't be read
    }

    return files;
  }
}
