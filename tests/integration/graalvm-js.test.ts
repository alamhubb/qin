/**
 * Integration tests for GraalVM JavaScript support
 *
 * Tests for Requirements 8.3
 */

import { describe, test, expect, beforeAll } from "bun:test";
import { existsSync } from "fs";
import { join } from "path";

describe("GraalVM JS Test Project - Integration Tests", () => {
  const testProjectPath = join(process.cwd(), "examples", "apps", "graalvm-js-test");

  beforeAll(() => {
    // 确保测试项目存在
    expect(existsSync(testProjectPath)).toBe(true);
  });

  test("test project has qin.config.ts", () => {
    const configPath = join(testProjectPath, "qin.config.ts");
    expect(existsSync(configPath)).toBe(true);
  });

  test("test project has entry file", () => {
    const entryPath = join(testProjectPath, "src", "server", "index.js");
    expect(existsSync(entryPath)).toBe(true);
  });

  test("test project has README", () => {
    const readmePath = join(testProjectPath, "README.md");
    expect(existsSync(readmePath)).toBe(true);
  });

  test("entry file contains expected content", async () => {
    const entryPath = join(testProjectPath, "src", "server", "index.js");
    const content = await Bun.file(entryPath).text();

    // 验证文件包含预期内容
    expect(content).toContain("GraalVM JavaScript Test Server");
    expect(content).toContain("console.log");
    expect(content).toContain("function");
  });

  test("config file contains graalvm configuration", async () => {
    const configPath = join(testProjectPath, "qin.config.ts");
    const content = await Bun.file(configPath).text();

    // 验证配置包含 graalvm 设置
    expect(content).toContain("graalvm");
    expect(content).toContain("js:");
    expect(content).toContain("entry:");
  });
});
