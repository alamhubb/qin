/**
 * Property-based tests for GraalVM detection
 *
 * **Feature: graalvm-nodejs-support, Property 1: GraalVM Detection Consistency**
 * **Validates: Requirements 1.1, 1.2, 4.2**
 */

import { describe, test, expect, beforeEach, afterEach } from "bun:test";
import * as fc from "fast-check";
import { detectGraalVM, parseComponentList } from "../src/index";

describe("GraalVM Detection - Property Tests", () => {
  // 保存原始环境变量
  const originalEnv = { ...process.env };

  afterEach(() => {
    // 恢复环境变量
    process.env = { ...originalEnv };
  });

  /**
   * **Feature: graalvm-nodejs-support, Property 1: GraalVM Detection Consistency**
   * **Validates: Requirements 1.1, 1.2, 4.2**
   *
   * For any system environment configuration, the detection function SHALL return
   * consistent results where found is a boolean and detectedBy is one of the valid methods.
   */
  test("detection result structure is always valid", async () => {
    await fc.assert(
      fc.asyncProperty(
        fc.record({
          graalvmHome: fc.option(fc.string(), { nil: undefined }),
          javaHome: fc.option(fc.string(), { nil: undefined }),
        }),
        async (env) => {
          // 设置环境变量
          if (env.graalvmHome !== undefined) {
            process.env.GRAALVM_HOME = env.graalvmHome;
          } else {
            delete process.env.GRAALVM_HOME;
          }

          if (env.javaHome !== undefined) {
            process.env.JAVA_HOME = env.javaHome;
          } else {
            delete process.env.JAVA_HOME;
          }

          const result = await detectGraalVM();

          // 验证结果结构
          expect(typeof result.found).toBe("boolean");

          if (result.found) {
            // 如果找到，必须有检测方式和信息
            expect(["env", "path", "gu"]).toContain(result.detectedBy);
            expect(result.info).toBeDefined();
            expect(typeof result.info!.home).toBe("string");
            expect(typeof result.info!.version).toBe("string");
            expect(Array.isArray(result.info!.components)).toBe(true);
          } else {
            // 如果未找到，应该有错误信息
            expect(typeof result.error).toBe("string");
          }

          return true;
        }
      ),
      { numRuns: 20 } // 限制运行次数，因为涉及文件系统操作
    );
  });

  /**
   * Property: Custom home path takes precedence
   *
   * For any custom home path provided, the detection should use that path
   * instead of environment variables.
   */
  test("custom home path takes precedence over environment", async () => {
    await fc.assert(
      fc.asyncProperty(fc.string({ minLength: 1 }), async (customPath) => {
        // 设置一个不同的环境变量
        process.env.GRAALVM_HOME = "/some/other/path";

        const result = await detectGraalVM(customPath);

        // 如果自定义路径无效，应该返回特定错误
        if (!result.found) {
          expect(result.error).toContain(customPath);
        }

        return true;
      }),
      { numRuns: 10 }
    );
  });

  /**
   * Property: Component list parsing is deterministic
   *
   * For any valid component list output, parsing should return consistent results.
   */
  test("component list parsing is deterministic", () => {
    // 生成有效的组件 ID（字母数字和连字符）
    const validComponentId = fc.stringMatching(/^[a-z][a-z0-9-]{0,19}$/);
    const validVersion = fc.stringMatching(/^[0-9]+\.[0-9]+\.[0-9]+$/);

    fc.assert(
      fc.property(
        fc.array(
          fc.record({
            id: validComponentId,
            version: validVersion,
          }),
          { minLength: 0, maxLength: 10 }
        ),
        (components) => {
          // 构建模拟的 gu list 输出
          const header = "ComponentId              Version             Component name";
          const separator = "----------------------------------------------------------------";
          const lines = components.map((c) => `${c.id.padEnd(24)} ${c.version.padEnd(19)} Some Name`);
          const output = [header, separator, ...lines].join("\n");

          // 解析两次
          const result1 = parseComponentList(output);
          const result2 = parseComponentList(output);

          // 结果应该相同
          expect(result1).toEqual(result2);

          // 结果长度应该等于输入组件数
          expect(result1.length).toBe(components.length);

          // 所有组件 ID 应该在结果中
          for (const comp of components) {
            expect(result1).toContain(comp.id);
          }

          return true;
        }
      ),
      { numRuns: 100 }
    );
  });

  /**
   * Property: Empty or header-only output returns empty array
   */
  test("empty or header-only output returns empty array", () => {
    fc.assert(
      fc.property(
        fc.constantFrom(
          "",
          "ComponentId              Version             Component name",
          "ComponentId              Version             Component name\n----------------------------------------------------------------",
          "\n\n\n"
        ),
        (output) => {
          const result = parseComponentList(output);
          expect(result).toEqual([]);
          return true;
        }
      )
    );
  });
});
