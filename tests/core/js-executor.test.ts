/**
 * Tests for JavaScript Executor
 * **Feature: graalvm-polyglot-js-runner**
 */

import { describe, test, expect } from "bun:test";
import fc from "fast-check";
import { join } from "path";
import {
  buildJavaCommand,
  commandSpecToArgs,
  getJsRunnerClasspath,
  getGraalVMJavaPath,
} from "../../src/core/js-executor";

describe("getGraalVMJavaPath", () => {
  test("returns java path", () => {
    const path = getGraalVMJavaPath();
    expect(path).toBeTruthy();
    // 应该是 java 或包含 java 的路径
    expect(path.includes("java")).toBe(true);
  });
});

describe("getJsRunnerClasspath", () => {
  test("returns a path string", () => {
    const path = getJsRunnerClasspath();
    expect(typeof path).toBe("string");
    expect(path.length).toBeGreaterThan(0);
  });
});

describe("buildJavaCommand", () => {
  test("builds command with file and no args", () => {
    const spec = buildJavaCommand({
      file: "/path/to/script.js",
      args: [],
      cwd: "/work",
    });

    expect(spec.mainClass).toBe("qin.runtime.JsRunner");
    expect(spec.programArgs[0]).toBe("/path/to/script.js");
    expect(spec.programArgs.length).toBe(1);
  });

  test("builds command with file and args", () => {
    const spec = buildJavaCommand({
      file: "/path/to/script.js",
      args: ["arg1", "arg2"],
      cwd: "/work",
    });

    expect(spec.programArgs).toEqual(["/path/to/script.js", "arg1", "arg2"]);
  });

  test("includes cwd in classpath", () => {
    const spec = buildJavaCommand({
      file: "/path/to/script.js",
      args: [],
      cwd: "/my/project",
    });

    expect(spec.classpath).toContain("/my/project");
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 4: Java Command Construction**
   * **Validates: Requirements 2.1, 2.2, 2.4, 2.5**
   * 
   * For any JavaScript execution request, the Java command builder SHALL produce
   * a command that includes JsRunner as the main class and the file path as first argument.
   */
  test("property: command always includes JsRunner and file path", () => {
    const validPath = fc.stringMatching(/^\/[a-zA-Z0-9_\/]+\.js$/);
    const validArgs = fc.array(fc.stringMatching(/^[a-zA-Z0-9_-]+$/), { maxLength: 5 });
    const validCwd = fc.stringMatching(/^\/[a-zA-Z0-9_\/]+$/);

    fc.assert(
      fc.property(validPath, validArgs, validCwd, (file, args, cwd) => {
        const spec = buildJavaCommand({ file, args, cwd });

        // 主类必须是 JsRunner
        expect(spec.mainClass).toBe("qin.runtime.JsRunner");
        
        // 第一个程序参数必须是文件路径
        expect(spec.programArgs[0]).toBe(file);
        
        // 用户参数必须在文件路径之后
        expect(spec.programArgs.slice(1)).toEqual(args);
      }),
      { numRuns: 100 }
    );
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 5: Argument Forwarding**
   * **Validates: Requirements 3.1, 3.2**
   * 
   * For any list of command line arguments, the JavaScript runtime SHALL receive
   * those exact arguments in the same order.
   */
  test("property: arguments are forwarded in order", () => {
    const validArgs = fc.array(fc.stringMatching(/^[a-zA-Z0-9_-]+$/), { minLength: 1, maxLength: 10 });

    fc.assert(
      fc.property(validArgs, (args) => {
        const spec = buildJavaCommand({
          file: "/test.js",
          args,
          cwd: "/work",
        });

        // 参数应该按顺序出现在文件路径之后
        const forwardedArgs = spec.programArgs.slice(1);
        expect(forwardedArgs).toEqual(args);
        expect(forwardedArgs.length).toBe(args.length);
      }),
      { numRuns: 100 }
    );
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 9: Classpath Construction**
   * **Validates: Requirements 7.2, 7.3**
   * 
   * For any JavaScript execution request, the classpath SHALL include
   * the JsRunner class location and the current working directory.
   */
  test("property: classpath includes JsRunner and cwd", () => {
    const validCwd = fc.stringMatching(/^\/[a-zA-Z0-9_]+$/);

    fc.assert(
      fc.property(validCwd, (cwd) => {
        const spec = buildJavaCommand({
          file: "/test.js",
          args: [],
          cwd,
        });

        // 类路径不能为空
        expect(spec.classpath.length).toBeGreaterThan(0);
        
        // 类路径应该包含 cwd
        expect(spec.classpath).toContain(cwd);
      }),
      { numRuns: 100 }
    );
  });
});

describe("commandSpecToArgs", () => {
  test("converts spec to args array", () => {
    const spec = {
      javaPath: "java",
      classpath: ["/lib/java", "/work"],
      mainClass: "qin.runtime.JsRunner",
      jvmArgs: ["-Xmx512m"],
      programArgs: ["/test.js", "arg1"],
    };

    const args = commandSpecToArgs(spec);
    
    // JVM 参数在前
    expect(args[0]).toBe("-Xmx512m");
    
    // 然后是 -cp
    expect(args[1]).toBe("-cp");
    
    // 类路径
    const separator = process.platform === "win32" ? ";" : ":";
    expect(args[2]).toBe(`/lib/java${separator}/work`);
    
    // 主类
    expect(args[3]).toBe("qin.runtime.JsRunner");
    
    // 程序参数
    expect(args[4]).toBe("/test.js");
    expect(args[5]).toBe("arg1");
  });

  test("handles empty classpath", () => {
    const spec = {
      javaPath: "java",
      classpath: [],
      mainClass: "Main",
      jvmArgs: [],
      programArgs: [],
    };

    const args = commandSpecToArgs(spec);
    
    // 没有 -cp 参数
    expect(args).not.toContain("-cp");
    expect(args[0]).toBe("Main");
  });

  /**
   * Property: args array always ends with program args
   */
  test("property: program args are always at the end", () => {
    const validProgramArgs = fc.array(fc.stringMatching(/^[a-zA-Z0-9_-]+$/), { minLength: 1, maxLength: 5 });

    fc.assert(
      fc.property(validProgramArgs, (programArgs) => {
        const spec = {
          javaPath: "java",
          classpath: ["/lib"],
          mainClass: "Main",
          jvmArgs: [],
          programArgs,
        };

        const args = commandSpecToArgs(spec);
        
        // 最后 N 个参数应该是程序参数
        const lastN = args.slice(-programArgs.length);
        expect(lastN).toEqual(programArgs);
      }),
      { numRuns: 100 }
    );
  });
});
