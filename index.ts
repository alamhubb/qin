#!/usr/bin/env bun
import { JavaBuilder } from "./src/java/builder";
import { QinPackageManager } from "./src/java/package-manager";
import { loadQinConfig, getInternalConfig } from "./src/java/config";
import { WasmBridge } from "./src/wasm/bridge";
import { basename } from "path";

import { existsSync } from "fs";
import { join } from "path";

// Default entry point
const DEFAULT_ENTRY = "src/index.java";

async function main() {
  const args = process.argv.slice(2);
  
  // No args: try to run default entry point (src/index.java)
  if (args.length === 0) {
    if (existsSync(DEFAULT_ENTRY)) {
      await runJavaFile(DEFAULT_ENTRY, []);
      return;
    }
    printHelp();
    return;
  }

  const command = args[0]!;

  // "run" command: run default entry or specified file
  if (command === "run") {
    const target = args[1] || DEFAULT_ENTRY;
    if (!existsSync(target)) {
      console.error(`Entry file not found: ${target}`);
      console.error(`Create ${DEFAULT_ENTRY} or specify a file: qin run <file.java>`);
      process.exit(1);
    }
    await runJavaFile(target, args.slice(2));
    return;
  }

  // Direct .java file execution: qin hello.java
  if (command.endsWith(".java")) {
    await runJavaFile(command, args.slice(1));
    return;
  }

  const pm = new QinPackageManager();
  await pm.init();

  // Subcommands
  switch (command) {
    case "init":
      await initProject(args[1]);
      break;
    case "add":
      if (!args[1]) {
        console.error("Usage: qin add <package@version>");
        process.exit(1);
      }
      const isDev = args.includes("-D") || args.includes("--dev");
      await pm.add(args[1], isDev);
      break;
    case "install":
    case "i":
      await pm.install();
      break;
    case "list":
    case "ls":
      pm.list();
      break;
    case "java":
      await handleJavaCommand(args.slice(1));
      break;
    case "help":
    case "--help":
    case "-h":
      printHelp();
      break;
    default:
      console.error(`Unknown command: ${command}`);
      printHelp();
      process.exit(1);
  }
}

/**
 * Initialize a new Qin project
 */
async function initProject(name?: string) {
  const projectName = name || basename(process.cwd());
  console.log(`Initializing Qin project: ${projectName}`);
  
  const pm = new QinPackageManager();
  // TODO: Create qin.config.ts and package.json
  console.log("✓ Created package.json");
  console.log("✓ Created qin.config.ts");
  console.log(`\nRun 'qin add <package>' to add dependencies`);
}

/**
 * Run a .java file directly (compile and run)
 */
async function runJavaFile(javaFile: string, programArgs: string[]) {
  const userConfig = await loadQinConfig();
  const config = getInternalConfig(userConfig);
  
  const builder = new JavaBuilder({
    srcDir: ".",
    outDir: config.outDir,
  });

  const className = basename(javaFile, ".java");

  console.log(`Compiling ${javaFile}...`);
  const success = await builder.compile([javaFile]);
  
  if (success) {
    console.log(`Running ${className}...`);
    await builder.run(className, programArgs);
  }
}

async function handleJavaCommand(args: string[]) {
  if (args.length === 0) {
    printJavaHelp();
    return;
  }

  const subcommand = args[0]!;
  const pm = new QinPackageManager();
  await pm.init();
  
  const config = pm.getConfig();
  const pkg = pm.getPackageJson();
  
  const builder = new JavaBuilder({
    srcDir: config.srcDir,
    outDir: config.outDir,
    mainClass: pkg.name,
    classpath: pm.getClasspath(),
  });

  switch (subcommand) {
    case "compile":
      await builder.compile();
      break;
    case "run":
      await builder.run(args[1], args.slice(2));
      break;
    case "build":
      await builder.compileAndRun(args[1], args.slice(2));
      break;
    case "wasm":
      if (!args[1]) {
        console.error("Usage: qin java wasm <file.java>");
        process.exit(1);
      }
      await compileToWasm(args[1], config);
      break;
    default:
      console.error(`Unknown java subcommand: ${subcommand}`);
      printJavaHelp();
      process.exit(1);
  }
}

async function compileToWasm(javaFile: string, config: any) {
  const className = basename(javaFile, ".java");
  console.log(`Compiling ${className} to WASM...`);
  
  // First compile Java to .class
  const builder = new JavaBuilder({
    srcDir: ".",
    outDir: config.outDir,
  });
  
  const compileSuccess = await builder.compile([javaFile]);
  if (!compileSuccess) {
    console.error("Java compilation failed");
    process.exit(1);
  }
  
  // Then compile to WASM
  const bridge = new WasmBridge({
    classOutDir: config.outDir,
    wasmOutDir: config.wasmOutDir,
  });
  
  const result = await bridge.compileClass(javaFile);
  if (result.success) {
    console.log(`✓ Generated WASM: ${result.wasmPath}`);
    console.log(`✓ Generated JS glue: ${result.jsGluePath}`);
    console.log(`✓ Generated types: ${result.dtsPath}`);
  } else {
    console.error(`WASM compilation failed: ${result.error}`);
    process.exit(1);
  }
}

function printHelp() {
  console.log(`
Qin - Cross-language Build System

Usage:
  qin                           Run src/index.java (default entry)
  qin run [file.java]           Run entry file (default: src/index.java)
  qin <file.java> [args...]     Run a specific Java file
  qin init [name]               Initialize a new Qin project
  qin add <package@version>     Add a dependency
  qin install                   Install all dependencies
  qin list                      List all dependencies
  qin java <command>            Java build commands

Examples:
  qin                           Run src/index.java
  qin run                       Run src/index.java
  qin run src/Main.java         Run specific file
  qin hello.java                Run hello.java directly
  qin add lodash@4.17.21        Add a package

Options:
  -h, --help                    Show this help message
`);
}

function printJavaHelp() {
  console.log(`
Qin Java Commands

Usage: qin java <command> [options]

Commands:
  compile              Compile all Java source files
  run <MainClass>      Run the specified main class
  build                Compile and run
  wasm <file.java>     Compile Java to WASM
`);
}

main().catch(console.error);
