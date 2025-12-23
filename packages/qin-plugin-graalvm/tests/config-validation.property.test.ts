/**
 * Property-based tests for GraalVM Configuration Validation
 *
 * **Feature: graalvm-nodejs-support, Property 2: Configuration Parsing Validity**
 * **Validates: Requirements 2.1, 2.2, 2.3, 2.4**
 */

import { describe, test, expect, beforeAll, afterAll } from "bun:test";
import * as fc from "fast-check";
import { mkdirSync, writeFileSync, rmSync, existsSync } from "fs";
import { join } from "path";
import {
  validateGraalVMConfig,
  formatValidationErrors,
  type GraalVMConfigInput,
} from "../src/index";

describe("GraalVM Config Validation - Property Tests", () => {
  const testDir = join(process.cwd(), "test-temp");
  const testEntryFile = join(testDir, "src", "server", "index.js");

  beforeAll(() => {
    // 创建测试目录和文件
    mkdirSync(join(testDir, "src", "server"), { recursive: true });
    writeFileSync(testEntryFile, 'console.log("test");');
  });

  afterAll(() => {
    // 清理测试目录
    if (existsSync(testDir)) {
      rmSync(testDir, { recursive: true, force: true });
    }
  });

  /**
   * **Feature: graalvm-nodejs-support, Property 2: Configuration Parsing Validity**
   * **Validates: Requirements 2.1, 2.2, 2.3, 2.4**
   *
   * For any valid qin.config.ts configuration object containing graalvm settings,
   * the configuration parser SHALL return a valid result structure.
   */
  test("validation result always has correct structure", () => {
    fc.assert(
      fc.property(
        fc.oneof(
          fc.constant(undefined),
          fc.record({
            home: fc.option(fc.string(), { nil: undefined }),
            js: fc.option(
              fc.oneof(
                fc.boolean(),
                fc.record({
                  entry: fc.option(fc.string(), { nil: undefined }),
                  hotReload: fc.option(
                    fc.oneof(
                      fc.boolean(),
                      fc.record({
                        debounce: fc.option(fc.integer({ min: 0, max: 10000 }), { nil: undefined }),
                        verbose: fc.option(fc.boolean(), { nil: undefined }),
                      })
                    ),
                    { nil: undefined }
                  ),
                  nodeArgs: fc.option(fc.array(fc.string()), { nil: undefined }),
                  javaInterop: fc.option(fc.boolean(), { nil: undefined }),
                })
              ),
              { nil: undefined }
            ),
          })
        ),
        (config) => {
          const result = validateGraalVMConfig(config as GraalVMConfigInput, testDir);

          // 结果必须有 valid 和 errors 字段
          expect(typeof result.valid).toBe("boolean");
          expect(Array.isArray(result.errors)).toBe(true);

          // 如果 valid 为 true，errors 应该为空
          if (result.valid) {
            expect(result.errors.length).toBe(0);
          }

          // 每个错误都应该有 field 和 message
          for (const error of result.errors) {
            expect(typeof error.field).toBe("string");
            expect(typeof error.message).toBe("string");
          }

          return true;
        }
      ),
      { numRuns: 100 }
    );
  });

  /**
   * Property: undefined config is always valid
   */
  test("undefined config is always valid", () => {
    const result = validateGraalVMConfig(undefined, testDir);
    expect(result.valid).toBe(true);
    expect(result.errors).toEqual([]);
  });

  /**
   * Property: empty config is always valid
   */
  test("empty config is always valid", () => {
    const result = validateGraalVMConfig({}, testDir);
    expect(result.valid).toBe(true);
    expect(result.errors).toEqual([]);
  });

  /**
   * Property: js: true is always valid
   */
  test("js: true is always valid", () => {
    const result = validateGraalVMConfig({ js: true }, testDir);
    expect(result.valid).toBe(true);
  });

  /**
   * Property: js: false is always valid
   */
  test("js: false is always valid", () => {
    const result = validateGraalVMConfig({ js: false }, testDir);
    expect(result.valid).toBe(true);
  });

  /**
   * Property: valid entry path passes validation
   */
  test("valid entry path passes validation", () => {
    const result = validateGraalVMConfig(
      { js: { entry: "src/server/index.js" } },
      testDir
    );
    expect(result.valid).toBe(true);
  });

  /**
   * Property: invalid entry path fails validation with specific error
   */
  test("invalid entry path fails validation", () => {
    fc.assert(
      fc.property(
        fc.string({ minLength: 1 }).filter((s) => !existsSync(join(testDir, s))),
        (invalidPath) => {
          const result = validateGraalVMConfig(
            { js: { entry: invalidPath } },
            testDir
          );

          // 应该失败
          expect(result.valid).toBe(false);

          // 应该有关于 entry 的错误
          const entryError = result.errors.find((e) => e.field === "graalvm.js.entry");
          expect(entryError).toBeDefined();
          expect(entryError!.message).toContain("does not exist");

          return true;
        }
      ),
      { numRuns: 20 }
    );
  });

  /**
   * Property: negative debounce fails validation
   */
  test("negative debounce fails validation", () => {
    fc.assert(
      fc.property(fc.integer({ min: -1000, max: -1 }), (negativeDebounce) => {
        const result = validateGraalVMConfig(
          { js: { hotReload: { debounce: negativeDebounce } } },
          testDir
        );

        expect(result.valid).toBe(false);
        const debounceError = result.errors.find((e) =>
          e.field.includes("debounce")
        );
        expect(debounceError).toBeDefined();

        return true;
      }),
      { numRuns: 20 }
    );
  });

  /**
   * Property: valid debounce passes validation
   */
  test("valid debounce passes validation", () => {
    fc.assert(
      fc.property(fc.integer({ min: 0, max: 10000 }), (validDebounce) => {
        const result = validateGraalVMConfig(
          { js: { hotReload: { debounce: validDebounce } } },
          testDir
        );

        // 不应该有 debounce 相关的错误
        const debounceError = result.errors.find((e) =>
          e.field.includes("debounce")
        );
        expect(debounceError).toBeUndefined();

        return true;
      }),
      { numRuns: 50 }
    );
  });

  /**
   * Property: nodeArgs with all strings is valid
   */
  test("nodeArgs with all strings is valid", () => {
    fc.assert(
      fc.property(fc.array(fc.string(), { minLength: 0, maxLength: 10 }), (args) => {
        const result = validateGraalVMConfig({ js: { nodeArgs: args } }, testDir);

        // 不应该有 nodeArgs 相关的错误
        const nodeArgsError = result.errors.find((e) =>
          e.field.includes("nodeArgs")
        );
        expect(nodeArgsError).toBeUndefined();

        return true;
      }),
      { numRuns: 50 }
    );
  });

  /**
   * Property: formatValidationErrors produces non-empty string for errors
   */
  test("formatValidationErrors produces readable output", () => {
    const result = validateGraalVMConfig(
      { js: { entry: "nonexistent.js" } },
      testDir
    );

    if (result.errors.length > 0) {
      const formatted = formatValidationErrors(result.errors);
      expect(typeof formatted).toBe("string");
      expect(formatted.length).toBeGreaterThan(0);

      // 应该包含字段名
      for (const error of result.errors) {
        expect(formatted).toContain(error.field);
      }
    }
  });
});
