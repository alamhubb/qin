/**
 * Integration tests for polyglot-js-test example project
 * **Feature: graalvm-polyglot-js-runner**
 */

import { describe, test, expect } from "bun:test";
import { existsSync } from "fs";
import { join } from "path";
import { spawnSync } from "child_process";

// 示例项目路径
const EXAMPLE_DIR = join(process.cwd(), "examples", "apps", "polyglot-js-test");
const CLI_PATH = join(process.cwd(), "src", "cli.ts");

// 检查 GraalVM 是否可用
function checkGraalVM(): boolean {
  try {
    const result = spawnSync("java", ["--version"], { encoding: "utf-8" });
    return result.status === 0 && result.stdout.toLowerCase().includes("graalvm");
  } catch {
    return false;
  }
}

// 运行 CLI 命令
function runCli(args: string[], cwd: string = EXAMPLE_DIR): { stdout: string; stderr: string; exitCode: number } {
  const result = spawnSync("bun", ["run", CLI_PATH, ...args], {
    cwd,
    encoding: "utf-8",
    env: { ...process.env },
  });
  
  return {
    stdout: result.stdout || "",
    stderr: result.stderr || "",
    exitCode: result.status ?? 1,
  };
}

const hasGraalVM = checkGraalVM();

describe("Polyglot JS Example Project", () => {
  describe("Project Structure", () => {
    test("example project exists", () => {
      expect(existsSync(EXAMPLE_DIR)).toBe(true);
    });

    test("qin.config.ts exists", () => {
      expect(existsSync(join(EXAMPLE_DIR, "qin.config.ts"))).toBe(true);
    });

    test("hello.js exists", () => {
      expect(existsSync(join(EXAMPLE_DIR, "src", "hello.js"))).toBe(true);
    });

    test("java-interop.js exists", () => {
      expect(existsSync(join(EXAMPLE_DIR, "src", "java-interop.js"))).toBe(true);
    });

    test("README.md exists", () => {
      expect(existsSync(join(EXAMPLE_DIR, "README.md"))).toBe(true);
    });
  });

  describe("Command Recognition", () => {
    test("recognizes hello.js as JavaScript file", () => {
      const result = runCli(["run", "src/hello.js"]);
      const output = result.stdout + result.stderr;
      
      // 应该识别为 JavaScript 执行
      const isJsExecution = output.includes("JavaScript") || 
                           output.includes("GraalVM") ||
                           output.includes("java");
      
      expect(isJsExecution).toBe(true);
    });

    test("recognizes java-interop.js as JavaScript file", () => {
      const result = runCli(["run", "src/java-interop.js"]);
      const output = result.stdout + result.stderr;
      
      // 应该识别为 JavaScript 执行
      const isJsExecution = output.includes("JavaScript") || 
                           output.includes("GraalVM") ||
                           output.includes("java");
      
      expect(isJsExecution).toBe(true);
    });

    test("shorthand form works", () => {
      const result = runCli(["src/hello.js"]);
      const output = result.stdout + result.stderr;
      
      // 应该识别为 JavaScript 执行
      const isJsExecution = output.includes("JavaScript") || 
                           output.includes("GraalVM") ||
                           output.includes("java");
      
      expect(isJsExecution).toBe(true);
    });
  });

  describe("Execution with GraalVM", () => {
    test.skipIf(!hasGraalVM)("executes hello.js successfully", () => {
      const result = runCli(["run", "src/hello.js"]);
      
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("GraalVM Polyglot JavaScript Demo");
      expect(result.stdout).toContain("Hello from GraalJS!");
    });

    test.skipIf(!hasGraalVM)("executes hello.js with arguments", () => {
      const result = runCli(["run", "src/hello.js", "--", "test-arg1", "test-arg2"]);
      
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("test-arg1");
      expect(result.stdout).toContain("test-arg2");
    });

    test.skipIf(!hasGraalVM)("executes java-interop.js successfully", () => {
      const result = runCli(["run", "src/java-interop.js"]);
      
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("Java Interop Demo");
      expect(result.stdout).toContain("ArrayList");
      expect(result.stdout).toContain("HashMap");
    });

    test.skipIf(!hasGraalVM)("captures console output", () => {
      const result = runCli(["run", "src/hello.js"]);
      
      expect(result.exitCode).toBe(0);
      // 验证 console.log 输出被捕获
      expect(result.stdout.length).toBeGreaterThan(0);
    });
  });
});
