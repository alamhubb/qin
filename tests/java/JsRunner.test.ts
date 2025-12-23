/**
 * Tests for JsRunner Java Bridge
 * **Feature: graalvm-polyglot-js-runner**
 * 
 * 注意：这些测试需要 GraalVM 环境才能运行
 * 如果没有 GraalVM，测试会被跳过
 */

import { describe, test, expect, beforeAll, afterAll } from "bun:test";
import fc from "fast-check";
import { mkdirSync, writeFileSync, rmSync, existsSync } from "fs";
import { join } from "path";
import { spawnSync } from "child_process";

// 测试用临时目录
const TEST_DIR = join(process.cwd(), ".test-jsrunner");
const JSRUNNER_CLASS = "lib/java/qin/runtime/JsRunner.java";

// 检查 GraalVM 是否可用
function checkGraalVM(): boolean {
  try {
    const result = spawnSync("java", ["--version"], { encoding: "utf-8" });
    return result.status === 0 && result.stdout.toLowerCase().includes("graalvm");
  } catch {
    return false;
  }
}

// 编译 JsRunner
function compileJsRunner(): boolean {
  if (!existsSync(JSRUNNER_CLASS)) {
    return false;
  }
  
  const result = spawnSync("javac", [
    "-d", join(TEST_DIR, "classes"),
    JSRUNNER_CLASS
  ], { encoding: "utf-8" });
  
  return result.status === 0;
}

// 运行 JavaScript 文件
function runJs(scriptPath: string, args: string[] = []): { stdout: string; stderr: string; exitCode: number } {
  const result = spawnSync("java", [
    "-cp", join(TEST_DIR, "classes"),
    "qin.runtime.JsRunner",
    scriptPath,
    ...args
  ], { encoding: "utf-8", cwd: TEST_DIR });
  
  return {
    stdout: result.stdout || "",
    stderr: result.stderr || "",
    exitCode: result.status ?? 1
  };
}

const hasGraalVM = checkGraalVM();

describe("JsRunner", () => {
  beforeAll(() => {
    mkdirSync(TEST_DIR, { recursive: true });
    mkdirSync(join(TEST_DIR, "classes"), { recursive: true });
    
    if (hasGraalVM) {
      compileJsRunner();
    }
  });

  afterAll(() => {
    rmSync(TEST_DIR, { recursive: true, force: true });
  });

  describe("Basic Execution", () => {
    test.skipIf(!hasGraalVM)("executes simple JavaScript", () => {
      const scriptPath = join(TEST_DIR, "simple.js");
      writeFileSync(scriptPath, `console.log("Hello from GraalJS!");`);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("Hello from GraalJS!");
    });

    test.skipIf(!hasGraalVM)("handles console.log with multiple arguments", () => {
      const scriptPath = join(TEST_DIR, "multi-arg.js");
      writeFileSync(scriptPath, `console.log("a", "b", 123);`);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("a");
      expect(result.stdout).toContain("b");
      expect(result.stdout).toContain("123");
    });
  });

  describe("Arguments", () => {
    test.skipIf(!hasGraalVM)("passes arguments to script", () => {
      const scriptPath = join(TEST_DIR, "args.js");
      writeFileSync(scriptPath, `console.log("args:", JSON.stringify(args));`);
      
      const result = runJs(scriptPath, ["arg1", "arg2", "arg3"]);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("arg1");
      expect(result.stdout).toContain("arg2");
      expect(result.stdout).toContain("arg3");
    });

    test.skipIf(!hasGraalVM)("handles empty arguments", () => {
      const scriptPath = join(TEST_DIR, "empty-args.js");
      writeFileSync(scriptPath, `console.log("length:", args.length);`);
      
      const result = runJs(scriptPath, []);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("length: 0");
    });
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 7: ES Module Support**
   * **Validates: Requirements 5.1, 5.2, 5.3**
   * 
   * For any JavaScript file using ES6+ syntax, the Polyglot Context
   * should successfully parse and execute the code.
   */
  describe("ES Module Support", () => {
    test.skipIf(!hasGraalVM)("executes ES6 arrow functions", () => {
      const scriptPath = join(TEST_DIR, "es6-arrow.js");
      writeFileSync(scriptPath, `
        const add = (a, b) => a + b;
        console.log("result:", add(2, 3));
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("result: 5");
    });

    test.skipIf(!hasGraalVM)("executes ES6 template literals", () => {
      const scriptPath = join(TEST_DIR, "es6-template.js");
      writeFileSync(scriptPath, `
        const name = "World";
        console.log(\`Hello, \${name}!\`);
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("Hello, World!");
    });

    test.skipIf(!hasGraalVM)("executes ES6 destructuring", () => {
      const scriptPath = join(TEST_DIR, "es6-destruct.js");
      writeFileSync(scriptPath, `
        const obj = { a: 1, b: 2 };
        const { a, b } = obj;
        console.log("a:", a, "b:", b);
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("a: 1");
      expect(result.stdout).toContain("b: 2");
    });

    test.skipIf(!hasGraalVM)("executes async/await", () => {
      const scriptPath = join(TEST_DIR, "es6-async.js");
      writeFileSync(scriptPath, `
        async function test() {
          return await Promise.resolve(42);
        }
        test().then(r => console.log("async result:", r));
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("async result: 42");
    });

    /**
     * Property test: Various ES6+ syntax patterns should execute successfully
     */
    test.skipIf(!hasGraalVM)("property: ES6+ syntax patterns execute successfully", () => {
      const es6Patterns = [
        { code: `const x = 1; console.log(x);`, desc: "const" },
        { code: `let y = 2; console.log(y);`, desc: "let" },
        { code: `const arr = [1,2,3]; console.log(...arr);`, desc: "spread" },
        { code: `const fn = () => 42; console.log(fn());`, desc: "arrow" },
        { code: `class A { m() { return 1; } } console.log(new A().m());`, desc: "class" },
      ];

      for (const pattern of es6Patterns) {
        const scriptPath = join(TEST_DIR, `es6-${pattern.desc}.js`);
        writeFileSync(scriptPath, pattern.code);
        
        const result = runJs(scriptPath);
        expect(result.exitCode).toBe(0);
      }
    });
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 8: Java Interop Functionality**
   * **Validates: Requirements 6.1, 6.2, 6.3**
   * 
   * For any JavaScript code using Java.type(), the Polyglot Context
   * should return a usable reference to that Java class.
   */
  describe("Java Interop", () => {
    test.skipIf(!hasGraalVM)("accesses Java.type for standard classes", () => {
      const scriptPath = join(TEST_DIR, "java-type.js");
      writeFileSync(scriptPath, `
        const ArrayList = Java.type('java.util.ArrayList');
        const list = new ArrayList();
        list.add("hello");
        list.add("world");
        console.log("size:", list.size());
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("size: 2");
    });

    test.skipIf(!hasGraalVM)("calls Java static methods", () => {
      const scriptPath = join(TEST_DIR, "java-static.js");
      writeFileSync(scriptPath, `
        const System = Java.type('java.lang.System');
        const time = System.currentTimeMillis();
        console.log("time is number:", typeof time === 'number');
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("time is number: true");
    });

    test.skipIf(!hasGraalVM)("creates and uses Java objects", () => {
      const scriptPath = join(TEST_DIR, "java-object.js");
      writeFileSync(scriptPath, `
        const StringBuilder = Java.type('java.lang.StringBuilder');
        const sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("Java");
        console.log("result:", sb.toString());
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(0);
      expect(result.stdout).toContain("result: Hello Java");
    });

    /**
     * Property test: Common Java classes should be accessible
     */
    test.skipIf(!hasGraalVM)("property: common Java classes are accessible", () => {
      const javaClasses = [
        "java.util.ArrayList",
        "java.util.HashMap",
        "java.lang.StringBuilder",
        "java.lang.Math",
        "java.util.Date",
      ];

      for (const className of javaClasses) {
        const scriptPath = join(TEST_DIR, `java-class-${className.replace(/\./g, "-")}.js`);
        writeFileSync(scriptPath, `
          const Cls = Java.type('${className}');
          console.log("loaded:", Cls !== undefined);
        `);
        
        const result = runJs(scriptPath);
        expect(result.exitCode).toBe(0);
        expect(result.stdout).toContain("loaded: true");
      }
    });
  });

  describe("Error Handling", () => {
    test.skipIf(!hasGraalVM)("reports syntax errors with location", () => {
      const scriptPath = join(TEST_DIR, "syntax-error.js");
      writeFileSync(scriptPath, `
        const x = {
          // missing closing brace
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(1);
      expect(result.stderr).toContain("syntax-error.js");
    });

    test.skipIf(!hasGraalVM)("reports runtime errors", () => {
      const scriptPath = join(TEST_DIR, "runtime-error.js");
      writeFileSync(scriptPath, `
        const obj = null;
        obj.foo(); // TypeError
      `);
      
      const result = runJs(scriptPath);
      expect(result.exitCode).toBe(1);
      expect(result.stderr.length).toBeGreaterThan(0);
    });

    test.skipIf(!hasGraalVM)("reports file not found", () => {
      const result = runJs(join(TEST_DIR, "nonexistent.js"));
      expect(result.exitCode).toBe(1);
      expect(result.stderr).toContain("not found");
    });
  });
});
