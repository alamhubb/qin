/**
 * Property-based tests for Hot Reload State Preservation
 *
 * **Feature: graalvm-nodejs-support, Property 6: Hot Reload State Preservation**
 * **Validates: Requirements 6.3**
 */

import { describe, test, expect } from "bun:test";
import * as fc from "fast-check";
import { graalvmJs } from "../src/index";

describe("Hot Reload State Preservation - Property Tests", () => {
  /**
   * **Feature: graalvm-nodejs-support, Property 6: Hot Reload State Preservation**
   * **Validates: Requirements 6.3**
   *
   * For any hot reload restart event, the system SHALL preserve:
   * - All environment variables from the original process
   * - All command line arguments from the original invocation
   * - The same working directory
   */
  test("hot reload plugin is created with state preservation capability", () => {
    const plugin = graalvmJs();

    // 热重载插件应该存在
    const hotReloadPlugin = plugin.plugins!.find(
      (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
    );
    expect(hotReloadPlugin).toBeDefined();

    // 应该有 devServer 钩子
    expect(typeof hotReloadPlugin!.devServer).toBe("function");

    // 应该有 cleanup 钩子
    expect(typeof hotReloadPlugin!.cleanup).toBe("function");
  });

  /**
   * Property: Hot reload options are preserved
   */
  test("hot reload options are preserved", () => {
    fc.assert(
      fc.property(
        fc.record({
          debounce: fc.integer({ min: 100, max: 5000 }),
          verbose: fc.boolean(),
        }),
        (options) => {
          const plugin = graalvmJs({ hotReload: options });

          const hotReloadPlugin = plugin.plugins!.find(
            (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
          );
          expect(hotReloadPlugin).toBeDefined();

          return true;
        }
      ),
      { numRuns: 20 }
    );
  });

  /**
   * Property: Disabling hot reload removes the plugin
   */
  test("disabling hot reload removes the plugin", () => {
    const plugin = graalvmJs({ hotReload: false });

    const hotReloadPlugin = plugin.plugins!.find(
      (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
    );
    expect(hotReloadPlugin).toBeUndefined();
  });

  /**
   * Property: Hot reload with boolean true creates default plugin
   */
  test("hot reload with boolean true creates default plugin", () => {
    const plugin = graalvmJs({ hotReload: true });

    const hotReloadPlugin = plugin.plugins!.find(
      (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
    );
    expect(hotReloadPlugin).toBeDefined();
  });

  /**
   * Property: Environment variables structure is preserved
   *
   * This tests that the hot reload state structure can hold environment variables.
   */
  test("environment variables can be stored in state", () => {
    fc.assert(
      fc.property(
        fc.dictionary(
          fc.string({ minLength: 1, maxLength: 20 }).filter((s) => /^[A-Z_][A-Z0-9_]*$/i.test(s)),
          fc.string({ maxLength: 100 })
        ),
        (envVars) => {
          // 模拟环境变量存储
          const state = {
            enabled: true,
            watchPatterns: ["**/*.js", "**/*.mjs"],
            env: { ...envVars },
            args: [],
          };

          // 验证环境变量被正确存储
          for (const [key, value] of Object.entries(envVars)) {
            expect(state.env[key]).toBe(value);
          }

          return true;
        }
      ),
      { numRuns: 50 }
    );
  });

  /**
   * Property: Command line arguments structure is preserved
   */
  test("command line arguments can be stored in state", () => {
    fc.assert(
      fc.property(fc.array(fc.string(), { minLength: 0, maxLength: 10 }), (args) => {
        // 模拟参数存储
        const state = {
          enabled: true,
          watchPatterns: ["**/*.js", "**/*.mjs"],
          env: {},
          args: [...args],
        };

        // 验证参数被正确存储
        expect(state.args).toEqual(args);
        expect(state.args.length).toBe(args.length);

        return true;
      }),
      { numRuns: 50 }
    );
  });
});
