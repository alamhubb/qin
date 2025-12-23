/**
 * Tests for JavaScript Command Parser
 * **Feature: graalvm-polyglot-js-runner**
 */

import { describe, test, expect, beforeAll, afterAll } from "bun:test";
import fc from "fast-check";
import { mkdirSync, writeFileSync, rmSync } from "fs";
import { join } from "path";
import {
  isJavaScriptFile,
  resolveFilePath,
  parseJsCommand,
} from "../../src/core/js-command-parser";

// 测试用临时目录
const TEST_DIR = join(process.cwd(), ".test-js-parser");

beforeAll(() => {
  mkdirSync(TEST_DIR, { recursive: true });
  // 创建测试文件
  writeFileSync(join(TEST_DIR, "test.js"), "console.log('test');");
  writeFileSync(join(TEST_DIR, "module.mjs"), "export default 42;");
});

afterAll(() => {
  rmSync(TEST_DIR, { recursive: true, force: true });
});

describe("isJavaScriptFile", () => {
  test("recognizes .js files", () => {
    expect(isJavaScriptFile("test.js")).toBe(true);
    expect(isJavaScriptFile("path/to/file.js")).toBe(true);
    expect(isJavaScriptFile("FILE.JS")).toBe(true);
  });

  test("recognizes .mjs files", () => {
    expect(isJavaScriptFile("module.mjs")).toBe(true);
    expect(isJavaScriptFile("path/to/module.mjs")).toBe(true);
    expect(isJavaScriptFile("MODULE.MJS")).toBe(true);
  });

  test("rejects non-JavaScript files", () => {
    expect(isJavaScriptFile("test.ts")).toBe(false);
    expect(isJavaScriptFile("test.java")).toBe(false);
    expect(isJavaScriptFile("test.json")).toBe(false);
    expect(isJavaScriptFile("test")).toBe(false);
  });

  test("handles edge cases", () => {
    expect(isJavaScriptFile("")).toBe(false);
    expect(isJavaScriptFile(null as any)).toBe(false);
    expect(isJavaScriptFile(undefined as any)).toBe(false);
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 1: JavaScript File Recognition**
   * **Validates: Requirements 1.1, 1.2**
   * 
   * For any file path ending with .js or .mjs, the function should return true.
   */
  test("property: recognizes all .js and .mjs files", () => {
    // 生成有效的文件名（不含特殊字符）
    const validFilename = fc.stringMatching(/^[a-zA-Z0-9_-]+$/);
    const validPath = fc.array(validFilename, { minLength: 0, maxLength: 3 });
    const jsExtension = fc.constantFrom(".js", ".mjs", ".JS", ".MJS", ".Js", ".Mjs");

    fc.assert(
      fc.property(validPath, validFilename, jsExtension, (pathParts, filename, ext) => {
        const fullPath = [...pathParts, `${filename}${ext}`].join("/");
        expect(isJavaScriptFile(fullPath)).toBe(true);
      }),
      { numRuns: 100 }
    );
  });

  /**
   * Property: Non-JS extensions are rejected
   */
  test("property: rejects non-JavaScript extensions", () => {
    const nonJsExtension = fc.constantFrom(".ts", ".java", ".py", ".json", ".txt", ".jsx", ".tsx");
    const validFilename = fc.stringMatching(/^[a-zA-Z0-9_-]+$/);

    fc.assert(
      fc.property(validFilename, nonJsExtension, (filename, ext) => {
        const fullPath = `${filename}${ext}`;
        expect(isJavaScriptFile(fullPath)).toBe(false);
      }),
      { numRuns: 100 }
    );
  });
});

describe("resolveFilePath", () => {
  const isWindows = process.platform === "win32";

  test("returns absolute path unchanged", () => {
    const absPath = isWindows ? "C:\\Users\\test.js" : "/home/user/test.js";
    expect(resolveFilePath(absPath, isWindows ? "C:\\other" : "/other")).toBe(absPath);
  });

  test("resolves relative path", () => {
    const cwd = isWindows ? "C:\\home\\user" : "/home/user";
    const result = resolveFilePath("test.js", cwd);
    expect(result).toContain("test.js");
    // 在 Windows 上路径包含盘符，在 Unix 上以 / 开头
    expect(isWindows ? result.includes(":") : result.startsWith("/")).toBe(true);
  });

  test("handles empty path", () => {
    expect(resolveFilePath("", "/home")).toBe("");
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 2: Path Resolution Consistency**
   * **Validates: Requirements 1.3**
   * 
   * For any relative path and CWD, the resolved path should be absolute
   * and correctly combine the CWD with the relative path.
   */
  test("property: resolved path is always absolute for non-empty input", () => {
    const validSegment = fc.stringMatching(/^[a-zA-Z0-9_]+$/);
    const relativePath = fc.array(validSegment, { minLength: 1, maxLength: 4 })
      .map(parts => parts.join("/") + ".js");

    fc.assert(
      fc.property(relativePath, (relPath) => {
        // 使用实际的 cwd 来测试
        const resolved = resolveFilePath(relPath, process.cwd());
        // 解析后的路径应该是绝对路径（Windows 有盘符，Unix 以 / 开头）
        const isAbsolute = isWindows ? /^[A-Za-z]:/.test(resolved) : resolved.startsWith("/");
        expect(isAbsolute).toBe(true);
        // 解析后的路径应该包含原始文件名
        expect(resolved.endsWith(".js")).toBe(true);
      }),
      { numRuns: 100 }
    );
  });
});

describe("parseJsCommand", () => {
  test("parses 'qin run xxx.js' command", () => {
    const result = parseJsCommand(["run", "test.js"], TEST_DIR);
    expect(result.isJsCommand).toBe(true);
    expect(result.options?.file).toBe(join(TEST_DIR, "test.js"));
    expect(result.options?.args).toEqual([]);
  });

  test("parses 'qin xxx.js' shorthand command", () => {
    const result = parseJsCommand(["test.js"], TEST_DIR);
    expect(result.isJsCommand).toBe(true);
    expect(result.options?.file).toBe(join(TEST_DIR, "test.js"));
  });

  test("parses arguments after --", () => {
    const result = parseJsCommand(["run", "test.js", "--", "arg1", "arg2"], TEST_DIR);
    expect(result.isJsCommand).toBe(true);
    expect(result.options?.args).toEqual(["arg1", "arg2"]);
  });

  test("parses arguments without -- separator", () => {
    const result = parseJsCommand(["run", "test.js", "arg1", "arg2"], TEST_DIR);
    expect(result.isJsCommand).toBe(true);
    expect(result.options?.args).toEqual(["arg1", "arg2"]);
  });

  test("returns error for non-existent file", () => {
    const result = parseJsCommand(["run", "nonexistent.js"], TEST_DIR);
    expect(result.isJsCommand).toBe(true);
    expect(result.error).toContain("not found");
  });

  test("returns isJsCommand=false for non-JS commands", () => {
    expect(parseJsCommand(["run"], TEST_DIR).isJsCommand).toBe(false);
    expect(parseJsCommand(["build"], TEST_DIR).isJsCommand).toBe(false);
    expect(parseJsCommand(["run", "Main.java"], TEST_DIR).isJsCommand).toBe(false);
  });

  test("handles empty argv", () => {
    expect(parseJsCommand([], TEST_DIR).isJsCommand).toBe(false);
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 1: JavaScript File Recognition**
   * **Validates: Requirements 1.1, 1.2**
   * 
   * For any .js file path in either 'run xxx.js' or 'xxx.js' form,
   * the parser should recognize it as a JavaScript command.
   */
  test("property: both command forms recognize JS files", () => {
    // 创建更多测试文件
    const testFiles = ["a.js", "b.mjs", "test-file.js"];
    testFiles.forEach(f => {
      try {
        writeFileSync(join(TEST_DIR, f), "// test");
      } catch {}
    });

    const jsFile = fc.constantFrom(...testFiles);
    const commandForm = fc.constantFrom("run", "shorthand");

    fc.assert(
      fc.property(jsFile, commandForm, (file, form) => {
        const argv = form === "run" ? ["run", file] : [file];
        const result = parseJsCommand(argv, TEST_DIR);
        expect(result.isJsCommand).toBe(true);
      }),
      { numRuns: 50 }
    );
  });
});
