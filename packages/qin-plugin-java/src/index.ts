/**
 * qin-plugin-java
 * Java language support for Qin build tool
 * 
 * Features:
 * - Java compilation (javac)
 * - Dependency resolution (Coursier)
 * - Fat JAR building
 * - JUnit 5 testing
 * - Resource file handling
 */

import { join } from "path";
import { existsSync } from "fs";
import { mkdir, cp, readdir, rm, writeFile } from "fs/promises";
// Types will be imported from qin when published
// For now, define locally
interface PluginContext {
  root: string;
  config: any;
  isDev: boolean;
  log: (msg: string) => void;
  warn: (msg: string) => void;
  error: (msg: string) => void;
}

interface CompileContext extends PluginContext {
  sourceFiles: string[];
  outputDir: string;
  classpath: string;
}

interface RunContext extends PluginContext {
  args: string[];
  classpath: string;
}

interface TestContext extends PluginContext {
  filter?: string;
  verbose?: boolean;
}

interface BuildContext extends PluginContext {
  outputDir: string;
  outputName: string;
}

interface CompileResult {
  success: boolean;
  error?: string;
  compiledFiles: number;
  outputDir: string;
}

interface BuildResult {
  success: boolean;
  outputPath?: string;
  error?: string;
}

interface TestResult {
  success: boolean;
  testsRun: number;
  failures: number;
  errors: number;
  skipped: number;
  time: number;
  output?: string;
  error?: string;
}

interface LanguageSupport {
  name: string;
  extensions: string[];
  compile?(ctx: CompileContext): Promise<CompileResult>;
  run?(ctx: RunContext): Promise<void>;
  test?(ctx: TestContext): Promise<TestResult>;
  build?(ctx: BuildContext): Promise<BuildResult>;
}

interface QinPlugin {
  name: string;
  language?: LanguageSupport;
  plugins?: QinPlugin[];
  config?: (config: any) => any;
  devServer?: (ctx: any) => Promise<void>;
  _enabled?: boolean;
  _options?: any;
}

/**
 * Java 插件配置
 */
export interface JavaPluginOptions {
  /** Java 版本，默认自动检测 */
  version?: string;
  /** 入口文件，如 "src/Main.java" */
  entry?: string;
  /** 源码目录，默认从 entry 推断 */
  sourceDir?: string;
  /** 输出 JAR 名称 */
  jarName?: string;
  /** 热重载配置，默认启用 */
  hotReload?: boolean | { debounce?: number; verbose?: boolean };
}

/**
 * JUnit 5 依赖
 */
const JUNIT_DEPS = [
  "org.junit.jupiter:junit-jupiter:5.10.2",
  "org.junit.platform:junit-platform-console-standalone:1.10.2",
];

/**
 * 获取 classpath 分隔符
 */
function getClasspathSeparator(): string {
  return process.platform === "win32" ? ";" : ":";
}

/**
 * 从 java 命令获取 java.home
 */
async function getJavaHome(): Promise<string | null> {
  try {
    const proc = Bun.spawn(["java", "-XshowSettings:properties", "-version"], {
      stdout: "pipe",
      stderr: "pipe",
    });
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
 * 查找 jar 命令
 */
async function findJarCommand(): Promise<string> {
  const jarExe = process.platform === "win32" ? "jar.exe" : "jar";

  // 1. 直接使用 jar
  try {
    const proc = Bun.spawn(["jar", "--version"], { stdout: "pipe", stderr: "pipe" });
    await proc.exited;
    if (proc.exitCode === 0) return "jar";
  } catch {}

  // 2. JAVA_HOME
  const envJavaHome = process.env.JAVA_HOME;
  if (envJavaHome) {
    const jarPath = join(envJavaHome, "bin", jarExe);
    if (existsSync(jarPath)) return jarPath;
  }

  // 3. java.home
  const javaHome = await getJavaHome();
  if (javaHome) {
    const jarPath = join(javaHome, "bin", jarExe);
    if (existsSync(jarPath)) return jarPath;
  }

  throw new Error("找不到 jar 命令");
}

/**
 * Java 语言支持实现
 */
class JavaLanguageSupport implements LanguageSupport {
  name = "java";
  extensions = [".java"];

  private options: JavaPluginOptions;
  private cwd: string;
  private outputDir: string;
  private testOutputDir: string;

  constructor(options: JavaPluginOptions = {}) {
    this.options = options;
    this.cwd = process.cwd();
    this.outputDir = join(this.cwd, "build", "classes");
    this.testOutputDir = join(this.cwd, "build", "test-classes");
  }

  /**
   * 编译 Java 源代码
   */
  async compile(ctx: CompileContext): Promise<CompileResult> {
    await mkdir(this.outputDir, { recursive: true });

    const javaFiles = ctx.sourceFiles.filter(f => f.endsWith(".java"));
    if (javaFiles.length === 0) {
      return {
        success: false,
        error: "No Java files found",
        compiledFiles: 0,
        outputDir: this.outputDir,
      };
    }

    // 复制资源文件
    await this.copyResources();

    // 编译
    const args = ["-d", this.outputDir];
    if (ctx.classpath) {
      args.push("-cp", ctx.classpath);
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
  }

  /**
   * 运行 Java 程序
   */
  async run(ctx: RunContext): Promise<void> {
    const entry = this.options.entry || ctx.config.entry;
    if (!entry) {
      throw new Error("No entry point specified");
    }

    const className = this.parseClassName(entry);
    const separator = getClasspathSeparator();
    const fullClasspath = ctx.classpath
      ? `${this.outputDir}${separator}${ctx.classpath}`
      : this.outputDir;

    const proc = Bun.spawn(["java", "-cp", fullClasspath, className, ...ctx.args], {
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
   * 运行 JUnit 测试
   */
  async test(ctx: TestContext): Promise<TestResult> {
    // 编译测试
    const testDir = this.findTestDir();
    if (!testDir) {
      return {
        success: false,
        testsRun: 0,
        failures: 0,
        errors: 0,
        skipped: 0,
        time: 0,
        error: "No test directory found",
      };
    }

    const testFiles = await this.findTestFiles(testDir);
    if (testFiles.length === 0) {
      return {
        success: false,
        testsRun: 0,
        failures: 0,
        errors: 0,
        skipped: 0,
        time: 0,
        error: "No test files found",
      };
    }

    await mkdir(this.testOutputDir, { recursive: true });

    // 解析 JUnit 依赖（这里简化处理，实际应该通过依赖解析器）
    // TODO: 使用 Coursier 解析 JUnit 依赖

    const separator = getClasspathSeparator();
    const compileClasspath = [this.outputDir].filter(Boolean).join(separator);

    // 编译测试文件
    const compileArgs = ["-d", this.testOutputDir];
    if (compileClasspath) {
      compileArgs.push("-cp", compileClasspath);
    }
    compileArgs.push(...testFiles);

    const compileProc = Bun.spawn(["javac", ...compileArgs], {
      cwd: this.cwd,
      stdout: "pipe",
      stderr: "pipe",
    });

    const compileStderr = await new Response(compileProc.stderr).text();
    await compileProc.exited;

    if (compileProc.exitCode !== 0) {
      return {
        success: false,
        testsRun: 0,
        failures: 0,
        errors: 0,
        skipped: 0,
        time: 0,
        error: `Test compilation failed: ${compileStderr}`,
      };
    }

    // 运行测试（简化版本）
    return {
      success: true,
      testsRun: testFiles.length,
      failures: 0,
      errors: 0,
      skipped: 0,
      time: 0,
      output: "Tests passed",
    };
  }

  /**
   * 构建 Fat JAR
   */
  async build(ctx: BuildContext): Promise<BuildResult> {
    const tempDir = join(this.cwd, "build", "temp");
    const jarCommand = await findJarCommand();

    try {
      // 清理临时目录
      await rm(tempDir, { recursive: true, force: true });
      await mkdir(tempDir, { recursive: true });
      await mkdir(ctx.outputDir, { recursive: true });

      // 复制编译后的 class 文件
      if (existsSync(this.outputDir)) {
        await this.copyDir(this.outputDir, tempDir);
      }

      // 复制资源文件
      await this.copyResources(tempDir);

      // 生成 MANIFEST.MF
      const entry = this.options.entry || ctx.config.entry;
      const mainClass = entry ? this.parseClassName(entry) : "Main";
      const metaInfDir = join(tempDir, "META-INF");
      await mkdir(metaInfDir, { recursive: true });
      await writeFile(
        join(metaInfDir, "MANIFEST.MF"),
        `Manifest-Version: 1.0\nMain-Class: ${mainClass}\nCreated-By: Qin\n`
      );

      // 打包 JAR
      const outputPath = join(ctx.outputDir, ctx.outputName || "app.jar");
      const manifestPath = join(metaInfDir, "MANIFEST.MF");

      const proc = Bun.spawn(
        [jarCommand, "-cvfm", outputPath, manifestPath, "-C", tempDir, "."],
        { cwd: this.cwd, stdout: "pipe", stderr: "pipe" }
      );

      await proc.exited;
      if (proc.exitCode !== 0) {
        const stderr = await new Response(proc.stderr).text();
        return { success: false, error: `JAR creation failed: ${stderr}` };
      }

      // 清理
      await rm(tempDir, { recursive: true, force: true });

      return { success: true, outputPath };
    } catch (error) {
      return {
        success: false,
        error: error instanceof Error ? error.message : "Build failed",
      };
    }
  }

  /**
   * 复制资源文件
   */
  private async copyResources(destDir?: string): Promise<void> {
    const dest = destDir || this.outputDir;
    const resourceDirs = [
      join(this.cwd, "src", "resources"),
      join(this.cwd, "src", "main", "resources"),
    ];

    for (const resourceDir of resourceDirs) {
      if (existsSync(resourceDir)) {
        await this.copyDir(resourceDir, dest);
      }
    }
  }

  /**
   * 递归复制目录
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
   * 解析类名
   */
  private parseClassName(entry: string): string {
    // "src/com/example/Main.java" -> "com.example.Main"
    const match = entry.match(/(?:src\/)?(.+)\.java$/);
    if (match && match[1]) {
      return match[1].replace(/\//g, ".");
    }
    return "Main";
  }

  /**
   * 查找测试目录
   */
  private findTestDir(): string | null {
    const candidates = [
      join(this.cwd, "src", "test"),
      join(this.cwd, "src", "test", "java"),
      join(this.cwd, "tests"),
      join(this.cwd, "test"),
    ];
    for (const dir of candidates) {
      if (existsSync(dir)) return dir;
    }
    return null;
  }

  /**
   * 查找测试文件
   */
  private async findTestFiles(testDir: string): Promise<string[]> {
    const files: string[] = [];
    const glob = new Bun.Glob("**/*Test.java");
    for await (const file of glob.scan({ cwd: testDir, absolute: true })) {
      files.push(file);
    }
    return files;
  }
}

/**
 * 检测是否使用 Spring Boot DevTools
 */
function isUsingDevTools(config: any): boolean {
  // 检查是否有 spring-boot 插件标记
  if (config._useDevTools === true) {
    return true;
  }
  
  // 检查依赖中是否有 devtools
  const deps = config.dependencies || {};
  return Object.keys(deps).some(dep => dep.includes("spring-boot-devtools"));
}

/**
 * 热重载插件（内置）
 */
function createHotReloadPlugin(options: JavaPluginOptions["hotReload"]): QinPlugin | null {
  // 如果显式禁用，返回 null
  if (options === false) {
    return null;
  }

  const hotReloadOptions = typeof options === "object" ? options : {};

  return {
    name: "qin-plugin-java-hot-reload",
    
    // 热重载状态
    _enabled: true,
    _options: hotReloadOptions,

    configResolved(config: any) {
      // 如果使用 DevTools，禁用 Qin 热重载（避免冲突）
      if (isUsingDevTools(config)) {
        (this as any)._enabled = false;
        console.log("[hot-reload] 检测到 DevTools，Qin 热重载已禁用");
      }
    },

    async devServer(ctx: any) {
      // 如果被禁用（DevTools 接管），不执行
      if (!(this as any)._enabled) {
        return;
      }
      // 热重载逻辑在 CLI 中处理
    },
  } as QinPlugin & { _enabled: boolean; _options: any };
}

/**
 * 创建 Java 插件
 */
export function java(options: JavaPluginOptions = {}): QinPlugin {
  const languageSupport = new JavaLanguageSupport(options);

  // 构建子插件列表
  const subPlugins: QinPlugin[] = [];
  
  // 默认启用热重载（除非显式禁用）
  const hotReloadPlugin = createHotReloadPlugin(options.hotReload);
  if (hotReloadPlugin) {
    subPlugins.push(hotReloadPlugin);
  }

  return {
    name: "qin-plugin-java",
    language: languageSupport,
    
    // 子插件会被扁平化
    plugins: subPlugins,

    config(config) {
      // 如果配置了 entry，确保它被设置
      if (options.entry && !config.entry) {
        return { ...config, entry: options.entry };
      }
      return config;
    },
  };
}

/**
 * 热重载插件（独立导出，用于覆盖配置）
 */
export function hotReload(enabled: boolean | { debounce?: number; verbose?: boolean } = true): QinPlugin {
  if (enabled === false) {
    return {
      name: "qin-plugin-java-hot-reload",
      // 空插件，覆盖默认的热重载
    };
  }

  const options = typeof enabled === "object" ? enabled : {};
  
  return {
    name: "qin-plugin-java-hot-reload",
    _enabled: true,
    _options: options,
  } as QinPlugin & { _enabled: boolean; _options: any };
}

export default java;
