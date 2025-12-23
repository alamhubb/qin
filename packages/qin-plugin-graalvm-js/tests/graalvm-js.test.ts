/**
 * Unit tests for GraalVM JavaScript plugin
 *
 * Tests for Requirements 5.1, 5.2
 */

import { describe, test, expect } from "bun:test";
import { graalvmJs } from "../src/index";

describe("GraalVM JS Plugin - Unit Tests", () => {
  describe("graalvmJs factory", () => {
    test("creates plugin with correct name", () => {
      const plugin = graalvmJs();
      expect(plugin.name).toBe("qin-plugin-graalvm-js");
    });

    test("creates plugin with language support", () => {
      const plugin = graalvmJs();

      expect(plugin.language).toBeDefined();
      expect(plugin.language!.name).toBe("graalvm-js");
      expect(plugin.language!.extensions).toEqual([".js", ".mjs"]);
    });

    test("includes graalvm base plugin", () => {
      const plugin = graalvmJs();

      expect(plugin.plugins).toBeDefined();
      const basePlugin = plugin.plugins!.find((p) => p.name === "qin-plugin-graalvm");
      expect(basePlugin).toBeDefined();
    });

    test("includes hot reload plugin by default", () => {
      const plugin = graalvmJs();

      const hotReloadPlugin = plugin.plugins!.find(
        (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
      );
      expect(hotReloadPlugin).toBeDefined();
    });

    test("excludes hot reload plugin when disabled", () => {
      const plugin = graalvmJs({ hotReload: false });

      const hotReloadPlugin = plugin.plugins!.find(
        (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
      );
      expect(hotReloadPlugin).toBeUndefined();
    });

    test("accepts entry option", () => {
      const plugin = graalvmJs({ entry: "src/server/index.js" });
      expect(plugin.name).toBe("qin-plugin-graalvm-js");
    });

    test("accepts nodeArgs option", () => {
      const plugin = graalvmJs({ nodeArgs: ["--max-old-space-size=4096"] });
      expect(plugin.name).toBe("qin-plugin-graalvm-js");
    });

    test("accepts javaInterop option", () => {
      const plugin = graalvmJs({ javaInterop: true });
      expect(plugin.name).toBe("qin-plugin-graalvm-js");
    });

    test("accepts hotReload object option", () => {
      const plugin = graalvmJs({ hotReload: { debounce: 500, verbose: true } });

      const hotReloadPlugin = plugin.plugins!.find(
        (p) => p.name === "qin-plugin-graalvm-js-hot-reload"
      );
      expect(hotReloadPlugin).toBeDefined();
    });
  });

  describe("language support", () => {
    test("has compile method", () => {
      const plugin = graalvmJs();
      expect(typeof plugin.language!.compile).toBe("function");
    });

    test("has run method", () => {
      const plugin = graalvmJs();
      expect(typeof plugin.language!.run).toBe("function");
    });

    test("handles .js extension", () => {
      const plugin = graalvmJs();
      expect(plugin.language!.extensions).toContain(".js");
    });

    test("handles .mjs extension", () => {
      const plugin = graalvmJs();
      expect(plugin.language!.extensions).toContain(".mjs");
    });
  });

  describe("plugin hooks", () => {
    test("has config hook", () => {
      const plugin = graalvmJs();
      expect(typeof plugin.config).toBe("function");
    });

    test("has configResolved hook", () => {
      const plugin = graalvmJs();
      expect(typeof plugin.configResolved).toBe("function");
    });

    test("config hook sets entry from options", () => {
      const plugin = graalvmJs({ entry: "src/app.js" });

      const result = plugin.config!({});
      expect(result).toBeDefined();
      expect(result!.graalvm?.js?.entry).toBe("src/app.js");
    });

    test("config hook does not override existing entry", () => {
      const plugin = graalvmJs({ entry: "src/app.js" });

      const result = plugin.config!({
        graalvm: { js: { entry: "src/existing.js" } },
      });

      // 当已有 entry 时，返回原配置（不修改）
      if (result) {
        // 如果返回了结果，entry 应该保持原值
        expect(result.graalvm?.js?.entry).toBe("src/existing.js");
      }
    });
  });
});
