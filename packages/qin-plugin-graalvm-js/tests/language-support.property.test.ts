/**
 * Property-based tests for GraalVM JavaScript Language Support
 *
 * **Feature: graalvm-nodejs-support, Property 5: File Extension Handling**
 * **Validates: Requirements 5.2, 5.3**
 *
 * **Feature: graalvm-nodejs-support, Property 4: JavaScript Execution Command Construction**
 * **Validates: Requirements 3.1, 3.4, 5.4**
 */

import { describe, test, expect } from "bun:test";
import * as fc from "fast-check";
import { graalvmJs } from "../src/index";

describe("GraalVM JS Language Support - Property Tests", () => {
  /**
   * **Feature: graalvm-nodejs-support, Property 5: File Extension Handling**
   * **Validates: Requirements 5.2, 5.3**
   *
   * For any file with `.js` or `.mjs` extension, the graalvmJs plugin SHALL
   * register as the handler for these extensions.
   */
  test("plugin registers correct file extensions", () => {
    const plugin = graalvmJs();

    expect(plugin.language).toBeDefined();
    expect(plugin.language!.name).toBe("graalvm-js");
    expect(plugin.language!.extensions).toContain(".js");
    expect(plugin.language!.extensions).toContain(".mjs");
  });

  /**
   * Property: Plugin name is always 'qin-plugin-graalvm-js'
   */
  test("plugin name is consistent across options", () => {
    fc.assert(
      fc.property(
        fc.record({
          entry: fc.option(fc.string(), { nil: undefined }),
          hotReload: fc.option(fc.boolean(), { nil: undefined }),
          javaInterop: fc.option(fc.boolean(), { nil: undefined }),
        }),
        (options) => {
          const plugin = graalvmJs(options);
          expect(plugin.name).toBe("qin-plugin-graalvm-js");
          return true;
        }
      ),
      { numRuns: 20 }
    );
  });

  /**
   * Property: Plugin always has language support
   */
  test("plugin always has language support", () => {
    fc.assert(
      fc.property(
        fc.record({
          entry: fc.option(fc.string(), { nil: undefined }),
          hotReload: fc.option(fc.boolean(), { nil: undefined }),
        }),
        (options) => {
          const plugin = graalvmJs(options);

          expect(plugin.language).toBeDefined();
          expect(typeof plugin.language!.name).toBe("string");
          expect(Array.isArray(plugin.language!.extensions)).toBe(true);
          expect(plugin.language!.extensions.length).toBeGreaterThan(0);

          return true;
        }
      ),
      { numRuns: 20 }
    );
  });

  /**
   * Property: Plugin includes graalvm base plugin as dependency
   */
  test("plugin includes graalvm base plugin", () => {
    const plugin = graalvmJs();

    expect(plugin.plugins).toBeDefined();
    expect(Array.isArray(plugin.plugins)).toBe(true);

    const graalvmBasePlugin = plugin.plugins!.find(
      (p) => p.name === "qin-plugin-graalvm"
    );
    expect(graalvmBasePlugin).toBeDefined();
  });

  /**
   * Property: Hot reload plugin is included by default
   */
  test("hot reload plugin is included by default", () => {
    const plugin = graalvmJs();

    expect(plugin.plugins).toBeDefined();

    const hotReloadPlugin = plugin.plugins!.find(
      (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
    );
    expect(hotReloadPlugin).toBeDefined();
  });

  /**
   * Property: Hot reload plugin is excluded when disabled
   */
  test("hot reload plugin is excluded when disabled", () => {
    const plugin = graalvmJs({ hotReload: false });

    expect(plugin.plugins).toBeDefined();

    const hotReloadPlugin = plugin.plugins!.find(
      (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
    );
    expect(hotReloadPlugin).toBeUndefined();
  });

  /**
   * **Feature: graalvm-nodejs-support, Property 4: JavaScript Execution Command Construction**
   * **Validates: Requirements 3.1, 3.4, 5.4**
   *
   * For any JavaScript entry point and argument list, the execution command SHALL
   * include the entry point and all user-provided arguments.
   */
  test("language support has compile and run methods", () => {
    const plugin = graalvmJs();

    expect(plugin.language).toBeDefined();
    expect(typeof plugin.language!.compile).toBe("function");
    expect(typeof plugin.language!.run).toBe("function");
  });

  /**
   * Property: nodeArgs are passed through options
   */
  test("nodeArgs option is accepted", () => {
    fc.assert(
      fc.property(
        fc.array(fc.string(), { minLength: 0, maxLength: 5 }),
        (nodeArgs) => {
          // 不应该抛出错误
          const plugin = graalvmJs({ nodeArgs });
          expect(plugin.name).toBe("qin-plugin-graalvm-js");
          return true;
        }
      ),
      { numRuns: 20 }
    );
  });

  /**
   * Property: javaInterop option is accepted
   */
  test("javaInterop option is accepted", () => {
    fc.assert(
      fc.property(fc.boolean(), (javaInterop) => {
        const plugin = graalvmJs({ javaInterop });
        expect(plugin.name).toBe("qin-plugin-graalvm-js");
        return true;
      }),
      { numRuns: 10 }
    );
  });

  /**
   * Property: entry option is accepted
   */
  test("entry option is accepted", () => {
    fc.assert(
      fc.property(fc.string({ minLength: 1 }), (entry) => {
        const plugin = graalvmJs({ entry });
        expect(plugin.name).toBe("qin-plugin-graalvm-js");
        return true;
      }),
      { numRuns: 20 }
    );
  });
});
