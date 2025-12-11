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
import { JavaRunner } from "./core/java-runner";
import { FatJarBuilder } from "./core/fat-jar-builder";
import { initProject } from "./commands/init";
import { FrontendPlugin } from "./plugins/frontend-plugin";

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

// qin run - Compile and run Java program
program
  .command("run")
  .description("Compile and run the Java program")
  .argument("[args...]", "Arguments to pass to the Java program")
  .action(async (args: string[]) => {
    try {
      // Load config first to check if we need Coursier
      console.log(chalk.blue("→ Loading configuration..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      const hasDependencies = config.dependencies && config.dependencies.length > 0;

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
        const resolver = new DependencyResolver(csCommand, config.repositories);
        classpath = await resolver.resolve(config.dependencies || []);
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

// qin build - Build Fat Jar
program
  .command("build")
  .description("Build a Fat Jar (Uber Jar) containing all dependencies")
  .option("--debug", "Keep temporary files for debugging")
  .action(async (options: { debug?: boolean }) => {
    try {
      // Load config first to check if we need Coursier
      console.log(chalk.blue("→ Loading configuration..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      const hasDependencies = config.dependencies && config.dependencies.length > 0;

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

      // Build frontend if enabled
      const frontendPlugin = new FrontendPlugin(config, { debug: options.debug });
      if (frontendPlugin.isEnabled()) {
        const frontendResult = await frontendPlugin.build();
        if (!frontendResult.success) {
          console.error(chalk.red("Frontend build failed:"), frontendResult.error);
          process.exit(1);
        }
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
  .description("Start development server (Java backend + Vite frontend)")
  .action(async () => {
    try {
      // Load config first to check if we need Coursier
      console.log(chalk.blue("→ 加载配置..."));
      const configLoader = new ConfigLoader();
      const config = await configLoader.load();
      const hasDependencies = config.dependencies && config.dependencies.length > 0;

      // Check environment
      const envStatus = await envChecker.checkAll();
      if (!envStatus.javac) {
        console.error(chalk.red("Error: javac is not installed."));
        console.log(envChecker.getInstallGuide("javac"));
        process.exit(1);
      }

      // Resolve dependencies (only if there are any)
      let classpath = "";
      if (hasDependencies) {
        console.log(chalk.blue("→ 解析依赖..."));
        const csCommand = await ensureCoursier();
        const resolver = new DependencyResolver(csCommand, config.repositories);
        classpath = await resolver.resolve(config.dependencies || []);
      }

      // Compile Java
      console.log(chalk.blue("→ 编译 Java..."));
      const runner = new JavaRunner(config, classpath);
      const compileResult = await runner.compile();
      
      if (!compileResult.success) {
        console.error(chalk.red("编译失败:"), compileResult.error);
        process.exit(1);
      }

      // Start Java backend
      console.log(chalk.blue("→ 启动 Java 后端..."));
      const javaProc = Bun.spawn(["java", "-cp", runner["buildFullClasspath"](), configLoader.parseEntry(config.entry).className], {
        cwd: process.cwd(),
        stdout: "inherit",
        stderr: "inherit",
      });

      // Start frontend dev server if enabled
      const frontendPlugin = new FrontendPlugin(config);
      if (frontendPlugin.isEnabled() && await frontendPlugin.hasFrontend()) {
        console.log(chalk.blue("→ 启动前端开发服务器..."));
        const devServer = await frontendPlugin.startDevServer();
        if (devServer) {
          console.log(chalk.green(`✓ 前端服务器: http://localhost:${devServer.port}`));
        }
      }

      console.log(chalk.green("✓ 开发服务器已启动"));
      console.log(chalk.gray("  按 Ctrl+C 停止"));

      // Wait for Java process
      await javaProc.exited;
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
      const hasDependencies = config.dependencies && config.dependencies.length > 0;

      if (!hasDependencies) {
        console.log(chalk.green("✓ 无依赖需要同步"));
        return;
      }

      // Ensure Coursier is available
      console.log(chalk.blue("→ 检查环境..."));
      const csCommand = await ensureCoursier();

      // Resolve and cache dependencies
      console.log(chalk.blue("→ 同步依赖..."));
      const resolver = new DependencyResolver(csCommand, config.repositories);
      const classpath = await resolver.resolve(config.dependencies || []);

      // Save classpath to cache file
      const { mkdir, writeFile } = await import("fs/promises");
      const { join } = await import("path");
      const qinDir = join(process.cwd(), ".qin");
      await mkdir(qinDir, { recursive: true });
      await writeFile(join(qinDir, "classpath.txt"), classpath);

      // Count dependencies
      const jarCount = classpath.split(process.platform === "win32" ? ";" : ":").filter(p => p.endsWith(".jar")).length;
      
      console.log(chalk.green(`✓ 依赖同步完成 (${jarCount} 个 JAR)`));
      console.log(chalk.gray(`  缓存: .qin/classpath.txt`));
    } catch (error) {
      console.error(chalk.red("Error:"), error instanceof Error ? error.message : error);
      process.exit(1);
    }
  });

program.parse();
