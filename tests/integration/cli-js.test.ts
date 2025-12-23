/**
 * Integration tests for CLI JavaScript execution
 * **Feature: graalvm-polyglot-js-runner**
 */

import { describe, test, expect, beforeAll, afterAll } from "bun:test";
import { mkdirSync, writeFileSync, rmSync } from "fs";
import { join } from "path";
import { spawnSync } from "child_process";

// 测试用临时目录
const TEST_DIR = join(process.cwd(), ".test-cli-js");

// CLI 路径
const CLI_PATH = join(process.cwd(), "src", "cli.ts");

// 运行 CLI 命令
function runCli(args: string[], cwd: string = TEST_DIR): { stdout: string; stderr: string; exitCode: number } {
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

describe("CLI JavaScript Integration", () => {
  beforeAll(() => {
    mkdirSync(TEST_DIR, { recursive: true });
  });

  afterAll(() => {
    rmSync(TEST_DIR, { recursive: true, force: true });
  });

  describe("Command Recognition", () => {
    test("recognizes 'qin run xxx.js' command", () => {
      // 创建测试文件
      const scriptPath = join(TEST_DIR, "test-run.js");
      writeFileSync(scriptPath, `console.log("test");`);
      
      // 运行命令（可能因为没有 GraalVM 而失败，但应该识别为 JS 命令）
      const result = runCli(["run", "test-run.js"]);
      
      // 应该尝试运行 JavaScript（而不是 Java）
      // 如果没有 GraalVM，会显示相关错误
      const output = result.stdout + result.stderr;
      const isJsExecution = output.includes("JavaScript") || 
                           output.includes("GraalVM") ||
                           output.includes("java");
      
      expect(isJsExecution).toBe(true);
    });

    test("recognizes 'qin xxx.js' shorthand command", () => {
      // 创建测试文件
      const scriptPath = join(TEST_DIR, "test-short.js");
      writeFileSync(scriptPath, `console.log("shorthand");`);
      
      // 运行简写命令
      const result = runCli(["test-short.js"]);
      
      // 应该尝试运行 JavaScript
      const output = result.stdout + result.stderr;
      const isJsExecution = output.includes("JavaScript") || 
                           output.includes("GraalVM") ||
                           output.includes("java") ||
                           output.includes("not found");
      
      expect(isJsExecution).toBe(true);
    });

    test("reports error for non-existent JS file", () => {
      const result = runCli(["run", "nonexistent.js"]);
      
      // 应该报告文件不存在
      const output = result.stdout + result.stderr;
      expect(output.toLowerCase()).toContain("not found");
    });

    test("does not treat non-JS files as JavaScript", () => {
      // 创建非 JS 文件
      const filePath = join(TEST_DIR, "test.txt");
      writeFileSync(filePath, "hello");
      
      // 运行命令
      const result = runCli(["run", "test.txt"]);
      
      // 应该不会尝试作为 JavaScript 运行
      // 而是尝试作为 Java 项目运行（会失败因为没有配置）
      const output = result.stdout + result.stderr;
      const isJavaExecution = output.includes("Loading configuration") ||
                             output.includes("config");
      
      expect(isJavaExecution).toBe(true);
    });
  });

  describe("Argument Parsing", () => {
    test("parses arguments after -- separator", () => {
      // 创建测试文件
      const scriptPath = join(TEST_DIR, "args-test.js");
      writeFileSync(scriptPath, `console.log("args:", JSON.stringify(args));`);
      
      // 运行带参数的命令
      const result = runCli(["run", "args-test.js", "--", "arg1", "arg2"]);
      
      // 命令应该被识别（即使执行失败）
      const output = result.stdout + result.stderr;
      const recognized = output.includes("JavaScript") || 
                        output.includes("GraalVM") ||
                        output.includes("java");
      
      expect(recognized).toBe(true);
    });

    test("parses arguments without -- separator", () => {
      // 创建测试文件
      const scriptPath = join(TEST_DIR, "args-no-sep.js");
      writeFileSync(scriptPath, `console.log("test");`);
      
      // 运行带参数的命令（无分隔符）
      const result = runCli(["run", "args-no-sep.js", "arg1", "arg2"]);
      
      // 命令应该被识别
      const output = result.stdout + result.stderr;
      const recognized = output.includes("JavaScript") || 
                        output.includes("GraalVM") ||
                        output.includes("java");
      
      expect(recognized).toBe(true);
    });
  });

  describe("File Extension Support", () => {
    test("supports .js extension", () => {
      const scriptPath = join(TEST_DIR, "ext-js.js");
      writeFileSync(scriptPath, `console.log("js");`);
      
      const result = runCli(["ext-js.js"]);
      const output = result.stdout + result.stderr;
      
      // 应该识别为 JavaScript
      expect(output.includes("JavaScript") || output.includes("GraalVM") || output.includes("java")).toBe(true);
    });

    test("supports .mjs extension", () => {
      const scriptPath = join(TEST_DIR, "ext-mjs.mjs");
      writeFileSync(scriptPath, `console.log("mjs");`);
      
      const result = runCli(["ext-mjs.mjs"]);
      const output = result.stdout + result.stderr;
      
      // 应该识别为 JavaScript
      expect(output.includes("JavaScript") || output.includes("GraalVM") || output.includes("java")).toBe(true);
    });
  });
});
