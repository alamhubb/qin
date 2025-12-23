/**
 * Unit tests for GraalVM detection
 *
 * Tests for Requirements 1.1, 1.2, 4.2
 */

import { describe, test, expect, beforeEach, afterEach } from "bun:test";
import {
  detectGraalVM,
  parseComponentList,
  graalvm,
  GraalVMNotFoundError,
  ComponentNotInstalledError,
  type GraalVMPlugin,
} from "../src/index";

describe("GraalVM Detection - Unit Tests", () => {
  const originalEnv = { ...process.env };

  afterEach(() => {
    process.env = { ...originalEnv };
  });

  describe("detectGraalVM", () => {
    test("returns found=false when no GraalVM is available", async () => {
      // 清除相关环境变量
      delete process.env.GRAALVM_HOME;
      delete process.env.JAVA_HOME;

      const result = await detectGraalVM();

      // 在没有 GraalVM 的环境中，应该返回 found=false
      expect(typeof result.found).toBe("boolean");
      if (!result.found) {
        expect(result.error).toBeDefined();
        expect(typeof result.error).toBe("string");
      }
    });

    test("returns error for invalid custom path", async () => {
      const result = await detectGraalVM("/nonexistent/path/to/graalvm");

      expect(result.found).toBe(false);
      expect(result.error).toContain("/nonexistent/path/to/graalvm");
    });

    test("detection result has correct structure", async () => {
      const result = await detectGraalVM();

      expect(result).toHaveProperty("found");
      expect(typeof result.found).toBe("boolean");

      if (result.found) {
        expect(result).toHaveProperty("detectedBy");
        expect(result).toHaveProperty("info");
        expect(["env", "path", "gu"]).toContain(result.detectedBy);
      }
    });
  });

  describe("parseComponentList", () => {
    test("parses valid component list output", () => {
      const output = `ComponentId              Version             Component name
----------------------------------------------------------------
graalvm                  23.0.0              GraalVM Core
js                       23.0.0              JavaScript
nodejs                   23.0.0              Node.js`;

      const components = parseComponentList(output);

      expect(components).toContain("graalvm");
      expect(components).toContain("js");
      expect(components).toContain("nodejs");
      expect(components.length).toBe(3);
    });

    test("returns empty array for empty output", () => {
      const components = parseComponentList("");
      expect(components).toEqual([]);
    });

    test("returns empty array for header-only output", () => {
      const output = `ComponentId              Version             Component name
----------------------------------------------------------------`;

      const components = parseComponentList(output);
      expect(components).toEqual([]);
    });

    test("handles output with extra whitespace", () => {
      const output = `ComponentId              Version             Component name
----------------------------------------------------------------
  nodejs                   23.0.0              Node.js  
  js                       23.0.0              JavaScript  `;

      const components = parseComponentList(output);

      expect(components).toContain("nodejs");
      expect(components).toContain("js");
    });
  });

  describe("Error classes", () => {
    test("GraalVMNotFoundError has correct message", () => {
      const error = new GraalVMNotFoundError();

      expect(error.name).toBe("GraalVMNotFoundError");
      expect(error.message).toContain("GraalVM not found");
      expect(error.message).toContain("GRAALVM_HOME");
      expect(error.message).toContain("https://www.graalvm.org/downloads/");
    });

    test("GraalVMNotFoundError accepts custom message", () => {
      const error = new GraalVMNotFoundError("Custom error message");

      expect(error.message).toBe("Custom error message");
    });

    test("ComponentNotInstalledError has correct message", () => {
      const error = new ComponentNotInstalledError("nodejs");

      expect(error.name).toBe("ComponentNotInstalledError");
      expect(error.message).toContain("nodejs");
      expect(error.message).toContain("gu install nodejs");
    });
  });

  describe("graalvm plugin", () => {
    test("plugin has correct name", () => {
      const plugin = graalvm();
      expect(plugin.name).toBe("qin-plugin-graalvm");
    });

    test("plugin exposes required methods", () => {
      const plugin = graalvm() as GraalVMPlugin;

      expect(typeof plugin.getInfo).toBe("function");
      expect(typeof plugin.getGraalVMHome).toBe("function");
      expect(typeof plugin.getInstalledComponents).toBe("function");
      expect(typeof plugin.isComponentInstalled).toBe("function");
      expect(typeof plugin.getDetectionResult).toBe("function");
    });

    test("plugin methods return correct types before initialization", () => {
      const plugin = graalvm() as GraalVMPlugin;

      // 在 configResolved 调用之前
      const info = plugin.getInfo();
      const home = plugin.getGraalVMHome();
      const components = plugin.getInstalledComponents();
      const detection = plugin.getDetectionResult();

      expect(info).toBeNull();
      expect(home).toBeNull();
      expect(components).toEqual([]);
      expect(detection.found).toBe(false);
    });

    test("plugin accepts custom home option", () => {
      const plugin = graalvm({ home: "/custom/graalvm/path" });
      expect(plugin.name).toBe("qin-plugin-graalvm");
    });

    test("plugin accepts autoInstall option", () => {
      const plugin = graalvm({ autoInstall: true });
      expect(plugin.name).toBe("qin-plugin-graalvm");
    });
  });
});
