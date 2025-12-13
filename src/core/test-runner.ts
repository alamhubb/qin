/**
 * Test Runner for Qin
 * Runs JUnit 5 tests
 * 
 * 注意：测试依赖需要用户在 devDependencies 中显式声明
 * @example
 * devDependencies: {
 *   "org.junit.jupiter:junit-jupiter": "5.10.2",
 *   "org.junit.platform:junit-platform-console-standalone": "1.10.2",
 * }
 */

import { join } from "path";
import { mkdir } from "fs/promises";
import { existsSync } from "fs";
import type { QinConfig } from "../types";
import { DependencyResolver, getClasspathSeparator } from "./dependency-resolver";

export interface TestResult {
  success: boolean;
  testsRun: number;
  failures: number;
  errors: number;
  skipped: number;
  time: number;
  output?: string;
  error?: string;
}

export interface TestRunnerOptions {
  filter?: string;
  verbose?: boolean;
}

export class TestRunner {
  private config: QinConfig;
  private classpath: string;
  private cwd: string;
  private outputDir: string;
  private testOutputDir: string;

  constructor(config: QinConfig, classpath: string, cwd?: string) {
    this.config = config;
    this.classpath = classpath;
    this.cwd = cwd || process.cwd();
    this.outputDir = join(this.cwd, "build", "classes");
    this.testOutputDir = join(this.cwd, "build", "test-classes");
  }

  /**
   * Find test source directory
   */
  private findTestDir(): string | null {
    const candidates = [
      join(this.cwd, "src", "test"),
      join(this.cwd, "src", "test", "java"),
      join(this.cwd, "tests"),
      join(this.cwd, "test"),
    ];

    for (const dir of candidates) {
      if (existsSync(dir)) {
        return dir;
      }
    }
    return null;
  }

  /**
   * Find all test Java files
   */
  private async findTestFiles(testDir: string): Promise<string[]> {
    const files: string[] = [];
    const glob = new Bun.Glob("**/*Test.java");
    
    for await (const file of glob.scan({ cwd: testDir, absolute: true })) {
      files.push(file);
    }

    // Also find files ending with Tests.java
    const glob2 = new Bun.Glob("**/*Tests.java");
    for await (const file of glob2.scan({ cwd: testDir, absolute: true })) {
      if (!files.includes(file)) {
        files.push(file);
      }
    }

    return files;
  }

  /**
   * Resolve test dependencies from devDependencies
   * 用户需要在 devDependencies 中显式声明测试依赖
   */
  async resolveTestDeps(csCommand: string): Promise<string> {
    const devDeps = this.config.devDependencies;
    
    if (!devDeps || Object.keys(devDeps).length === 0) {
      throw new Error(
        "未找到测试依赖。请在 qin.config.ts 的 devDependencies 中声明：\n" +
        "devDependencies: {\n" +
        '  "org.junit.jupiter:junit-jupiter": "5.10.2",\n' +
        '  "org.junit.platform:junit-platform-console-standalone": "1.10.2",\n' +
        "}"
      );
    }

    // 检查是否有 JUnit 相关依赖
    const hasJUnit = Object.keys(devDeps).some(dep => 
      dep.includes("junit") || dep.includes("testng")
    );
    
    if (!hasJUnit) {
      console.warn(
        "[test] 警告：devDependencies 中未检测到测试框架（JUnit/TestNG）"
      );
    }

    const resolver = new DependencyResolver(
      csCommand,
      this.config.repositories,
      undefined,
      this.cwd,
      this.config.localRep
    );
    
    return await resolver.resolveFromObject(devDeps);
  }

  /**
   * Compile test files
   */
  async compileTests(testClasspath: string): Promise<{ success: boolean; error?: string }> {
    const testDir = this.findTestDir();
    if (!testDir) {
      return { success: false, error: "No test directory found (src/test, tests, or test)" };
    }

    const testFiles = await this.findTestFiles(testDir);
    if (testFiles.length === 0) {
      return { success: false, error: "No test files found (*Test.java or *Tests.java)" };
    }

    await mkdir(this.testOutputDir, { recursive: true });

    // Build classpath: main classes + dependencies + test deps
    const separator = getClasspathSeparator();
    const fullClasspath = [this.outputDir, this.classpath, testClasspath]
      .filter(Boolean)
      .join(separator);

    const args = ["-d", this.testOutputDir];
    if (fullClasspath) {
      args.push("-cp", fullClasspath);
    }
    args.push(...testFiles);

    const proc = Bun.spawn(["javac", ...args], {
      cwd: this.cwd,
      stdout: "pipe",
      stderr: "pipe",
    });

    const stderr = await new Response(proc.stderr).text();
    await proc.exited;

    if (proc.exitCode !== 0) {
      return { success: false, error: stderr.trim() || "Test compilation failed" };
    }

    return { success: true };
  }

  /**
   * Run tests using JUnit Platform Console Launcher
   */
  async runTests(testClasspath: string, options: TestRunnerOptions = {}): Promise<TestResult> {
    const separator = getClasspathSeparator();
    
    // Build classpath: test classes + main classes + dependencies + test deps
    const fullClasspath = [this.testOutputDir, this.outputDir, this.classpath, testClasspath]
      .filter(Boolean)
      .join(separator);

    // Find junit-platform-console-standalone jar
    const consoleLauncherJar = testClasspath
      .split(separator)
      .find(p => p.includes("junit-platform-console-standalone"));

    if (!consoleLauncherJar) {
      return {
        success: false,
        testsRun: 0,
        failures: 0,
        errors: 0,
        skipped: 0,
        time: 0,
        error: "未找到 JUnit Platform Console Launcher。请在 devDependencies 中添加：\n" +
               '"org.junit.platform:junit-platform-console-standalone": "1.10.2"',
      };
    }

    const args = [
      "-jar", consoleLauncherJar,
      "--class-path", fullClasspath,
      "--scan-class-path", this.testOutputDir,
    ];

    // Add filter if specified
    if (options.filter) {
      args.push("--include-classname", `.*${options.filter}.*`);
    }

    // Add verbose output
    if (options.verbose) {
      args.push("--details", "verbose");
    }

    const startTime = Date.now();
    const proc = Bun.spawn(["java", ...args], {
      cwd: this.cwd,
      stdout: "pipe",
      stderr: "pipe",
    });

    const stdout = await new Response(proc.stdout).text();
    const stderr = await new Response(proc.stderr).text();
    await proc.exited;

    const endTime = Date.now();
    const output = stdout + stderr;

    // Parse test results from output
    const result = this.parseTestOutput(output);
    result.time = (endTime - startTime) / 1000;
    result.output = output;
    result.success = proc.exitCode === 0;

    return result;
  }

  /**
   * Parse JUnit output to extract test counts
   */
  private parseTestOutput(output: string): TestResult {
    const result: TestResult = {
      success: false,
      testsRun: 0,
      failures: 0,
      errors: 0,
      skipped: 0,
      time: 0,
    };

    // Parse summary line like: "3 tests found" or "3 tests successful"
    const testsMatch = output.match(/(\d+)\s+tests?\s+(found|successful)/i);
    if (testsMatch && testsMatch[1]) {
      result.testsRun = parseInt(testsMatch[1], 10);
    }

    // Parse failures
    const failuresMatch = output.match(/(\d+)\s+tests?\s+failed/i);
    if (failuresMatch && failuresMatch[1]) {
      result.failures = parseInt(failuresMatch[1], 10);
    }

    // Parse skipped/aborted
    const skippedMatch = output.match(/(\d+)\s+tests?\s+(skipped|aborted)/i);
    if (skippedMatch && skippedMatch[1]) {
      result.skipped = parseInt(skippedMatch[1], 10);
    }

    return result;
  }

  /**
   * Run full test cycle: compile main + compile tests + run tests
   */
  async run(testClasspath: string, options: TestRunnerOptions = {}): Promise<TestResult> {
    // Compile tests
    const compileResult = await this.compileTests(testClasspath);
    if (!compileResult.success) {
      return {
        success: false,
        testsRun: 0,
        failures: 0,
        errors: 0,
        skipped: 0,
        time: 0,
        error: compileResult.error,
      };
    }

    // Run tests
    return await this.runTests(testClasspath, options);
  }
}
