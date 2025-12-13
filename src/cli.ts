#!/usr/bin/env bun
/**
 * Qin CLI - Java-Vite Build Tool
 * A modern Java build tool with zero XML configuration
 */

import { Command } from "commander";
import chalk from "chalk";
import { ConfigLoader } from "./core/config-loader";
import { EnvironmentChecker } from "./core/environment";
import { DependencyResolver } from "./core/dependency-resolver";
import { WorkspaceLoader } from "./core/workspace-loader";
import { JavaRunner } from "./core/java-runner";
import { FatJarBuilder } from "./core/fat-jar-builder";
import { PluginRunner } from "./core/plugin-runner";
import { TestRunner } from "./core/test-runner";
import { describeDetection } from "./core/plugin-detector";
import { initProject } from "./commands/init";
import { runGral, showGralInfo } from "./commands/gral";
import { isJavaScriptFile, parseJsCommand } from "./core/js-command-parser";
import { executeJavaScript, checkGraalVMAvailable } from "./core/js-executor";
import { parseJsError, formatJsError } from "./core/js-error-formatter";

const program = new Command();

// Shared environment checker instance
const envChecker = new EnvironmentChecker();

/**
 * Ensure Coursier is available (auto-install if needed)
 */
async function ensureCoursier(): Promise<string> {
  if (await envChecker.checkCoursier()) {
    // Check if global cs works
    try {
      const proc = Bun.spawn(["cs", "--version"], { stdout: "pipe", stderr: "pipe" });
      await proc.exited;
      if (proc.exitCode === 0) return "cs";
    } catch {
      // Use local path
    }
    return envChecker.getCoursierCommand();
  }
  
  // Auto-install
  const installed = await envChecker.installCoursier();
  if (!installed) {
    throw new Error("无法安装 Coursier，请手动安装");
  }
  return envChecker.getCoursierCommand();
}

program
  .name("qin")
  .description("Java-Vite: A modern Java build tool with zero XML configuration")
  .version("0.1.0");

// qin init - Initialize a new project
program
  .command("init")
  .description("Initialize a new Qin project")
  .action(async () => {
    try {
      await initProject();
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

// qin clean - Clean build artifacts
program
  .command("clean")
  .description("Clean build artifacts (build directory)")
  .action(async () => {
    try {
      const { rm } = await import("fs/promises");
      const { join } = await import("path");
      const { existsSync } = await import("fs");
      
      const buildDir = join(process.cwd(), "build");
      
      if (existsSync(buildDir)) {
        console.log(chalk.blue("→ 清理构建目录..."));
        await rm(buildDir, { recursive: true, force: true });
        console.log(chalk.green("✓ 已清理 build/"));
      } else {
        console.log(chalk.gray("✓ 无需清理，build/ 目录不存在"));
      }
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

// qin run - Compile and run Java program or JavaScript file
program
  .command("run")
  .description("Compile and run the Java program, or run a JavaScript file with GraalVM")
  .argument("[fileOrArgs...]", "JavaScript file to run, or arguments to pass to the Java program")
  .action(async (fileOrArgs: string[]) => {
    try {
      // 检查第一个参数是否是 JavaScript 文件
      const firstArg = fileOrArgs[0];
      if (firstArg && isJavaScriptFile(firstArg)) {
        // JavaScript 执行模式
        await runJavaScript(firstArg, fileOrArgs.slice(1));
        return;
      }

      // Java 执行模式
      const args = fileOrArgs;
      
      // Load config first to check if we need Coursier
      console.log(chalk.blue("→ Loading configuration..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      
      // 显示自动检测结果
      if (config._detected) {
        console.log(chalk.gray(`  ${describeDetection(config._detected)}`));
      }
      
      const hasDependencies = config.dependencies && Object.keys(config.dependencies).length > 0;

      // Load workspace packages if configured
      const workspaceLoader = new WorkspaceLoader();
      const localPackages = await workspaceLoader.loadPackages(config);

      // Check environment
      const envStatus = await envChecker.checkAll();
      
      // Only require javac
      if (!envStatus.javac) {
        console.error(chalk.red("Error: javac is not installed."));
        console.log(envChecker.getInstallGuide("javac"));
        process.exit(1);
      }

      // Resolve dependencies (only if there are any)
      let classpath = "";
      if (hasDependencies) {
        console.log(chalk.blue("→ Resolving dependencies..."));
        const csCommand = await ensureCoursier();
        const resolver = new DependencyResolver(csCommand, config.repositories, localPackages, process.cwd(), config.localRep);
        classpath = await resolver.resolveFromObject(config.dependencies || {});
      }

      // Compile and run
      console.log(chalk.blue("→ Compiling and running..."));
      const runner = new JavaRunner(config, classpath);
      await runner.compileAndRun(args);

      console.log(chalk.green("✓ Done!"));
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

/**
 * 使用 GraalVM Polyglot API 运行 JavaScript 文件
 */
async function runJavaScript(file: string, args: string[]): Promise<void> {
  const { resolve } = await import("path");
  const { existsSync } = await import("fs");
  
  // 解析文件路径
  const filePath = resolve(process.cwd(), file);
  
  // 检查文件是否存在
  if (!existsSync(filePath)) {
    console.error(chalk.red(`✗ File not found: ${filePath}`));
    process.exit(1);
  }
  
  // 检查 Java 是否可用（用于 Polyglot API）
  const javaStatus = checkGraalVMAvailable();
  if (!javaStatus.available) {
    console.error(chalk.red("✗ Java is not available"));
    console.error(chalk.gray(`  ${javaStatus.error}`));
    console.log();
    console.log(chalk.yellow("To run JavaScript with Qin, you need Java 11+:"));
    console.log(chalk.gray("  1. Install Java 11 or later"));
    console.log(chalk.gray("  2. Ensure 'java' is in your PATH"));
    process.exit(1);
  }
  
  const runtimeName = javaStatus.isGraalVM ? "GraalVM" : "Java";
  console.log(chalk.blue(`→ Running JavaScript with ${runtimeName} ${javaStatus.version} (Polyglot API)...`));
  console.log(chalk.gray(`  File: ${filePath}`));
  if (args.length > 0) {
    console.log(chalk.gray(`  Args: ${args.join(" ")}`));
  }
  console.log();
  
  // 执行 JavaScript
  const result = await executeJavaScript({
    file: filePath,
    args,
    cwd: process.cwd(),
    inheritStdio: true,
  });
  
  if (result.exitCode !== 0) {
    // 如果有错误输出，解析并格式化
    if (result.stderr) {
      const error = parseJsError(result.stderr);
      console.error(formatJsError(error));
    }
    process.exit(result.exitCode);
  }
  
  console.log();
  console.log(chalk.green("✓ Done!"));
}

// qin compile - Compile source code (for library projects)
program
  .command("compile")
  .description("Compile Java source code (for library projects without main method)")
  .option("-o, --output <dir>", "Output directory for compiled classes", "build/classes")
  .action(async (options: { output: string }) => {
    try {
      console.log(chalk.blue("→ Loading configuration..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      const hasDependencies = config.dependencies && Object.keys(config.dependencies).length > 0;

      // Check environment
      const envStatus = await envChecker.checkAll();
      if (!envStatus.javac) {
        console.error(chalk.red("Error: javac is not installed."));
        console.log(envChecker.getInstallGuide("javac"));
        process.exit(1);
      }

      // Ensure Coursier if needed
      let csCommand = "cs";
      if (hasDependencies) {
        csCommand = await ensureCoursier();
      }

      // Load workspace packages
      const workspaceLoader = new WorkspaceLoader();
      const localPackages = await workspaceLoader.loadPackages(config);

      // Resolve dependencies
      console.log(chalk.blue("→ Resolving dependencies..."));
      const resolver = new DependencyResolver(
        csCommand,
        config.repositories,
        localPackages,
        process.cwd(),
        config.localRep
      );

      let classpath = "";
      if (hasDependencies) {
        classpath = await resolver.resolveFromObject(config.dependencies!);
      }

      // Find source directory
      const { join } = await import("path");
      const { existsSync } = await import("fs");
      const { mkdir } = await import("fs/promises");
      
      const cwd = process.cwd();
      let srcDir = join(cwd, "src");
      
      // Check for Maven-style structure
      if (existsSync(join(cwd, "src", "main", "java"))) {
        srcDir = join(cwd, "src", "main", "java");
      } else if (config.java?.sourceDir) {
        srcDir = join(cwd, config.java.sourceDir);
      }

      if (!existsSync(srcDir)) {
        console.error(chalk.red(`Error: Source directory not found: ${srcDir}`));
        process.exit(1);
      }

      // Find all Java files
      console.log(chalk.blue(`→ Compiling from ${srcDir}...`));
      const javaFiles: string[] = [];
      const glob = new Bun.Glob("**/*.java");
      for await (const file of glob.scan({ cwd: srcDir, absolute: true })) {
        javaFiles.push(file);
      }

      if (javaFiles.length === 0) {
        console.error(chalk.red("Error: No Java files found"));
        process.exit(1);
      }

      console.log(chalk.gray(`  Found ${javaFiles.length} Java files`));

      // Create output directory
      const outputDir = join(cwd, options.output);
      await mkdir(outputDir, { recursive: true });

      // Build javac arguments
      const args = ["-d", outputDir];
      
      // Add classpath
      if (classpath) {
        args.push("-cp", classpath);
      }

      // Add source files
      args.push(...javaFiles);

      // Compile
      const proc = Bun.spawn(["javac", ...args], {
        cwd,
        stdout: "pipe",
        stderr: "pipe",
      });

      const stderr = await new Response(proc.stderr).text();
      await proc.exited;

      if (proc.exitCode !== 0) {
        console.error(chalk.red("Compilation failed:"));
        console.error(stderr);
        process.exit(1);
      }

      // Copy resources
      const resourceDirs = [
        join(cwd, "src", "resources"),
        join(cwd, "src", "main", "resources"),
      ];

      for (const resourceDir of resourceDirs) {
        if (existsSync(resourceDir)) {
          const { cp } = await import("fs/promises");
          await cp(resourceDir, outputDir, { recursive: true });
          console.log(chalk.gray(`  Copied resources from ${resourceDir}`));
        }
      }

      console.log(chalk.green(`✓ Compiled ${javaFiles.length} files to ${options.output}`));
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

// qin build - Build Fat Jar
program
  .command("build")
  .description("Build a Fat Jar (Uber Jar) containing all dependencies")
  .option("--debug", "Keep temporary files for debugging")
  .option("--clean", "Clean build directory before building")
  .action(async (options: { debug?: boolean; clean?: boolean }) => {
    try {
      // Clean if requested
      if (options.clean) {
        const { rm } = await import("fs/promises");
        const { join } = await import("path");
        const { existsSync } = await import("fs");
        const buildDir = join(process.cwd(), "build");
        if (existsSync(buildDir)) {
          console.log(chalk.blue("→ 清理构建目录..."));
          await rm(buildDir, { recursive: true, force: true });
        }
      }

      // Load config first to check if we need Coursier
      console.log(chalk.blue("→ Loading configuration..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      const hasDependencies = config.dependencies && Object.keys(config.dependencies).length > 0;

      // Load workspace packages if configured
      const workspaceLoader = new WorkspaceLoader();
      const localPackages = await workspaceLoader.loadPackages(config);

      // Check environment
      const envStatus = await envChecker.checkAll();
      
      if (!envStatus.javac) {
        console.error(chalk.red("Error: javac is not installed."));
        console.log(envChecker.getInstallGuide("javac"));
        process.exit(1);
      }

      // Ensure Coursier if needed (will auto-install)
      let csCommand = "cs";
      if (hasDependencies) {
        csCommand = await ensureCoursier();
      }

      // Run plugins build (包括内置 Web 插件)
      const pluginRunner = new PluginRunner(config);
      
      if (pluginRunner.hasPlugins()) {
        console.log(chalk.blue("→ 构建插件资源..."));
        await pluginRunner.runBuild();
      }

      // Build Fat Jar
      console.log(chalk.blue("→ Building Fat Jar..."));
      const builder = new FatJarBuilder(config, { debug: options.debug });
      const result = await builder.build();

      if (result.success) {
        console.log(chalk.green(`✓ Fat Jar built successfully: ${result.outputPath}`));
      } else {
        console.error(chalk.red("Build failed:"), result.error);
        process.exit(1);
      }
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

// qin dev - Development mode with hot reload
program
  .command("dev")
  .description("Start development server with hot reload (Java backend + Vite frontend)")
  .option("--no-hot", "Disable hot reload")
  .option("-v, --verbose", "Show verbose output")
  .action(async (options: { hot?: boolean; verbose?: boolean }) => {
    let pluginRunner: PluginRunner | null = null;
    let hotReloadManager: import("./core/hot-reload").HotReloadManager | null = null;
    
    try {
      // Load config
      console.log(chalk.blue("→ 加载配置..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      
      // 显示检测结果
      if (config._detected) {
        console.log(chalk.gray(`  ${describeDetection(config._detected)}`));
      }
      
      const hasDependencies = config.dependencies && Object.keys(config.dependencies).length > 0;
      const hasJava = config._detected?.languages.includes("java") || config.entry;

      // Load workspace packages
      const workspaceLoader = new WorkspaceLoader();
      const localPackages = await workspaceLoader.loadPackages(config);

      // Check environment (only if Java is needed)
      if (hasJava) {
        const envStatus = await envChecker.checkAll();
        if (!envStatus.javac) {
          console.error(chalk.red("Error: javac is not installed."));
          console.log(envChecker.getInstallGuide("javac"));
          process.exit(1);
        }
      }

      // Resolve dependencies
      let classpath = "";
      if (hasDependencies) {
        console.log(chalk.blue("→ 解析依赖..."));
        const csCommand = await ensureCoursier();
        const resolver = new DependencyResolver(csCommand, config.repositories, localPackages, process.cwd(), config.localRep);
        classpath = await resolver.resolveFromObject(config.dependencies || {});
      }

      // Start Java with hot reload (if Java project)
      if (hasJava && config.entry) {
        const { HotReloadManager } = await import("./core/hot-reload");
        
        if (options.hot !== false) {
          console.log(chalk.blue("→ 启动 Java 热重载..."));
          hotReloadManager = new HotReloadManager(
            { config, classpath },
            { verbose: options.verbose }
          );
          await hotReloadManager.start();
        } else {
          // 无热重载模式
          console.log(chalk.blue("→ 编译并启动 Java..."));
          const runner = new JavaRunner(config, classpath);
          const compileResult = await runner.compile();
          
          if (!compileResult.success) {
            console.error(chalk.red("编译失败:"), compileResult.error);
            process.exit(1);
          }
          
          const javaProc = Bun.spawn(["java", "-cp", runner["buildFullClasspath"](), configLoader.parseEntry(config.entry).className], {
            cwd: process.cwd(),
            stdout: "inherit",
            stderr: "inherit",
          });
          
          javaProc.exited.then(() => process.exit(0));
        }
      }

      // Run plugins (Vite etc.)
      pluginRunner = new PluginRunner(config);
      
      if (pluginRunner.hasPlugins()) {
        console.log(chalk.blue("→ 启动插件..."));
        await pluginRunner.runDev();
      }

      console.log(chalk.green("✓ 开发服务器已启动"));
      if (options.hot !== false && hasJava) {
        console.log(chalk.gray("  Java 热重载已启用，修改 .java 文件将自动重启"));
      }
      console.log(chalk.gray("  按 Ctrl+C 停止"));

      // Cleanup on exit
      process.on("SIGINT", async () => {
        console.log(chalk.yellow("\n→ 正在停止..."));
        if (hotReloadManager) await hotReloadManager.stop();
        if (pluginRunner) await pluginRunner.cleanup();
        process.exit(0);
      });

      // Keep process alive
      await new Promise(() => {});
    } catch (error) {
      if (hotReloadManager) await hotReloadManager.stop();
      if (pluginRunner) await pluginRunner.cleanup();
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

// qin test - Run JUnit tests
program
  .command("test")
  .description("Run JUnit 5 tests")
  .option("-f, --filter <pattern>", "Filter tests by class name pattern")
  .option("-v, --verbose", "Show verbose test output")
  .action(async (options: { filter?: string; verbose?: boolean }) => {
    try {
      console.log(chalk.blue("→ 加载配置..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      const hasDependencies = config.dependencies && Object.keys(config.dependencies).length > 0;

      // Load workspace packages if configured
      const workspaceLoader = new WorkspaceLoader();
      const localPackages = await workspaceLoader.loadPackages(config);

      // Check environment
      const envStatus = await envChecker.checkAll();
      if (!envStatus.javac) {
        console.error(chalk.red("Error: javac is not installed."));
        console.log(envChecker.getInstallGuide("javac"));
        process.exit(1);
      }

      // Ensure Coursier for JUnit dependencies
      console.log(chalk.blue("→ 解析依赖..."));
      const csCommand = await ensureCoursier();

      // Resolve main dependencies
      let classpath = "";
      if (hasDependencies) {
        const resolver = new DependencyResolver(csCommand, config.repositories, localPackages, process.cwd(), config.localRep);
        classpath = await resolver.resolveFromObject(config.dependencies || {});
      }

      // Compile main source first
      console.log(chalk.blue("→ 编译源代码..."));
      const runner = new JavaRunner(config, classpath);
      const compileResult = await runner.compile();
      
      if (!compileResult.success) {
        console.error(chalk.red("编译失败:"), compileResult.error);
        process.exit(1);
      }

      // Run tests
      console.log(chalk.blue("→ 解析测试依赖..."));
      const testRunner = new TestRunner(config, classpath);
      
      let testClasspath: string;
      try {
        testClasspath = await testRunner.resolveTestDeps(csCommand);
      } catch (err) {
        console.error(chalk.red("测试依赖解析失败:"));
        console.error(err instanceof Error ? err.message : String(err));
        process.exit(1);
      }

      console.log(chalk.blue("→ 编译测试..."));
      const testCompileResult = await testRunner.compileTests(testClasspath);
      if (!testCompileResult.success) {
        console.error(chalk.red("测试编译失败:"), testCompileResult.error);
        process.exit(1);
      }

      console.log(chalk.blue("→ 运行测试...\n"));
      const result = await testRunner.runTests(testClasspath, {
        filter: options.filter,
        verbose: options.verbose,
      });

      // Print output
      if (result.output) {
        console.log(result.output);
      }

      // Print summary
      console.log();
      if (result.success) {
        console.log(chalk.green(`✓ 测试通过 (${result.testsRun} 个测试, ${result.time.toFixed(2)}s)`));
      } else {
        console.log(chalk.red(`✗ 测试失败 (${result.failures} 失败, ${result.testsRun} 个测试, ${result.time.toFixed(2)}s)`));
        process.exit(1);
      }
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

// qin sync - Sync dependencies (like npm install)
program
  .command("sync")
  .description("Sync dependencies and generate classpath cache")
  .action(async () => {
    try {
      console.log(chalk.blue("→ 加载配置..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      const hasDependencies = config.dependencies && Object.keys(config.dependencies).length > 0;

      if (!hasDependencies) {
        console.log(chalk.green("✓ 无依赖需要同步"));
        return;
      }

      // Load workspace packages if configured
      const workspaceLoader = new WorkspaceLoader();
      const localPackages = await workspaceLoader.loadPackages(config);

      // Ensure Coursier is available
      console.log(chalk.blue("→ 检查环境..."));
      const csCommand = await ensureCoursier();

      // Resolve and cache dependencies
      console.log(chalk.blue("→ 同步依赖..."));
      const resolver = new DependencyResolver(csCommand, config.repositories, localPackages, process.cwd(), config.localRep);
      const classpath = await resolver.resolveFromObject(config.dependencies || {});

      // Save classpath to cache file (JSON format)
      const { mkdir, writeFile } = await import("fs/promises");
      const { join } = await import("path");
      const cacheDir = join(process.cwd(), "build", ".cache");
      await mkdir(cacheDir, { recursive: true });
      
      const jarPaths = classpath.split(process.platform === "win32" ? ";" : ":").filter(p => p.endsWith(".jar"));
      await writeFile(join(cacheDir, "classpath.json"), JSON.stringify({ classpath: jarPaths }, null, 2));

      console.log(chalk.green(`✓ 依赖同步完成 (${jarPaths.length} 个 JAR)`));
      console.log(chalk.gray(`  缓存: build/.cache/classpath.json`));
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

// qin gral - Run JavaScript with GraalVM Node.js
program
  .command("gral")
  .description("Run JavaScript with GraalVM Node.js (supports Java interop)")
  .argument("[file]", "JavaScript file to run")
  .argument("[args...]", "Arguments to pass to the script")
  .option("--polyglot", "Enable Java interop (--polyglot --jvm)")
  .option("--jvm", "Run on JVM mode")
  .option("-v, --verbose", "Show verbose output")
  .option("--info", "Show GraalVM environment info")
  .action(async (file: string | undefined, args: string[], options: { polyglot?: boolean; jvm?: boolean; verbose?: boolean; info?: boolean }) => {
    try {
      if (options.info || !file) {
        await showGralInfo();
        return;
      }
      
      await runGral(file, args, options);
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

// 处理 qin xxx.js 简写形式（在所有命令之前检查）
const rawArgs = process.argv.slice(2);
if (rawArgs.length > 0 && isJavaScriptFile(rawArgs[0]!)) {
  // 直接运行 JavaScript 文件
  const jsResult = parseJsCommand(rawArgs, process.cwd());
  if (jsResult.isJsCommand) {
    if (jsResult.error) {
      console.error(chalk.red(`✗ ${jsResult.error}`));
      process.exit(1);
    }
    if (jsResult.options) {
      runJavaScript(jsResult.options.file, jsResult.options.args)
        .catch((err) => {
          console.error(chalk.red("Error:"), err instanceof Error ? err.message : err);
          process.exit(1);
        });
    }
  }
} else {
  program.parse();
}
