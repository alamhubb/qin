/**
 * Property-based tests for GraalVM Plugin API
 *
 * **Feature: graalvm-nodejs-support, Property 3: Plugin API Consistency**
 * **Validates: Requirements 4.3, 4.4, 4.5**
 */

import { describe, test, expect } from "bun:test";
import * as fc from "fast-check";
import { graalvm, type GraalVMPlugin, type GraalVMInfo } from "../src/index";

describe("GraalVM Plugin API - Property Tests", () => {
  /**
   * **Feature: graalvm-nodejs-support, Property 3: Plugin API Consistency**
   * **Validates: Requirements 4.3, 4.4, 4.5**
   *
   * For any GraalVM installation state, the plugin API methods SHALL return
   * consistent results where:
   * - getGraalVMHome() returns the installation path or null if not found
   * - getInstalledComponents() returns an array of component names (possibly empty)
   * - isComponentInstalled(name) returns true if and only if the component exists
   */
  test("plugin API methods return consistent types", async () => {
    const plugin = graalvm() as GraalVMPlugin;

    // 模拟 configResolved 调用
    if (plugin.configResolved) {
      await plugin.configResolved({});
    }

    // getGraalVMHome 返回 string 或 null
    const home = plugin.getGraalVMHome();
    expect(home === null || typeof home === "string").toBe(true);

    // getInstalledComponents 返回数组
    const components = plugin.getInstalledComponents();
    expect(Array.isArray(components)).toBe(true);

    // getInfo 返回 GraalVMInfo 或 null
    const info = plugin.getInfo();
    expect(info === null || typeof info === "object").toBe(true);

    // getDetectionResult 返回有效结构
    const detection = plugin.getDetectionResult();
    expect(typeof detection.found).toBe("boolean");
  });

  /**
   * Property: isComponentInstalled is consistent with getInstalledComponents
   *
   * For any component name, isComponentInstalled(name) returns true
   * if and only if name is in getInstalledComponents().
   */
  test("isComponentInstalled is consistent with getInstalledComponents", async () => {
    const plugin = graalvm() as GraalVMPlugin;

    // 模拟 configResolved 调用
    if (plugin.configResolved) {
      await plugin.configResolved({});
    }

    const components = plugin.getInstalledComponents();

    await fc.assert(
      fc.asyncProperty(fc.string({ minLength: 1, maxLength: 50 }), async (componentName) => {
        const isInstalled = plugin.isComponentInstalled(componentName);
        const inList = components.includes(componentName);

        // isComponentInstalled 应该与 includes 结果一致
        expect(isInstalled).toBe(inList);

        return true;
      }),
      { numRuns: 50 }
    );
  });

  /**
   * Property: All components in getInstalledComponents are reported as installed
   */
  test("all listed components are reported as installed", async () => {
    const plugin = graalvm() as GraalVMPlugin;

    if (plugin.configResolved) {
      await plugin.configResolved({});
    }

    const components = plugin.getInstalledComponents();

    for (const component of components) {
      expect(plugin.isComponentInstalled(component)).toBe(true);
    }
  });

  /**
   * Property: Plugin name is always 'qin-plugin-graalvm'
   */
  test("plugin name is consistent", () => {
    fc.assert(
      fc.property(
        fc.record({
          home: fc.option(fc.string(), { nil: undefined }),
          autoInstall: fc.option(fc.boolean(), { nil: undefined }),
        }),
        (options) => {
          const plugin = graalvm(options);
          expect(plugin.name).toBe("qin-plugin-graalvm");
          return true;
        }
      ),
      { numRuns: 20 }
    );
  });

  /**
   * Property: getInfo and getGraalVMHome are consistent
   *
   * If getInfo() returns non-null, getGraalVMHome() should return info.home
   */
  test("getInfo and getGraalVMHome are consistent", async () => {
    const plugin = graalvm() as GraalVMPlugin;

    if (plugin.configResolved) {
      await plugin.configResolved({});
    }

    const info = plugin.getInfo();
    const home = plugin.getGraalVMHome();

    if (info !== null) {
      expect(home).toBe(info.home);
    } else {
      expect(home).toBeNull();
    }
  });

  /**
   * Property: getInfo and getInstalledComponents are consistent
   *
   * If getInfo() returns non-null, getInstalledComponents() should return info.components
   */
  test("getInfo and getInstalledComponents are consistent", async () => {
    const plugin = graalvm() as GraalVMPlugin;

    if (plugin.configResolved) {
      await plugin.configResolved({});
    }

    const info = plugin.getInfo();
    const components = plugin.getInstalledComponents();

    if (info !== null) {
      expect(components).toEqual(info.components);
    } else {
      expect(components).toEqual([]);
    }
  });
});
