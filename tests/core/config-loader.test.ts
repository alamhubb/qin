/**
 * Tests for ConfigLoader
 * **Feature: java-vite-build-tool, Property 2: Entry 路径解析正确性**
 */

import { describe, test, expect } from "bun:test";
import fc from "fast-check";
import { ConfigLoader, parseEntryPath } from "../../src/core/config-loader";

describe("ConfigLoader", () => {
  describe("parseEntry", () => {
    test("parses simple entry path", () => {
      const loader = new ConfigLoader();
      const result = loader.parseEntry("src/Main.java");
      
      expect(result.srcDir).toBe("src");
      expect(result.className).toBe("Main");
      expect(result.filePath).toBe("src/Main.java");
    });

    test("parses nested entry path", () => {
      const loader = new ConfigLoader();
      const result = loader.parseEntry("src/com/example/App.java");
      
      expect(result.srcDir).toBe("src/com/example");
      expect(result.className).toBe("App");
    });

    test("parses entry path without directory", () => {
      const loader = new ConfigLoader();
      const result = loader.parseEntry("Main.java");
      
      expect(result.srcDir).toBe(".");
      expect(result.className).toBe("Main");
    });

    test("handles Windows-style paths", () => {
      const loader = new ConfigLoader();
      const result = loader.parseEntry("src\\com\\example\\App.java");
      
      expect(result.srcDir).toBe("src/com/example");
      expect(result.className).toBe("App");
    });

    /**
     * **Feature: java-vite-build-tool, Property 2: Entry 路径解析正确性**
     * **Validates: Requirements 5.2**
     * 
     * For any valid Java file path, parsing should correctly extract
     * the source directory and class name.
     */
    test("property: entry path parsing extracts correct srcDir and className", () => {
      // Generate valid Java file paths
      const validClassName = fc.stringMatching(/^[A-Z][a-zA-Z0-9]*$/);
      const validDirSegment = fc.stringMatching(/^[a-z][a-z0-9]*$/);
      
      fc.assert(
        fc.property(
          fc.array(validDirSegment, { minLength: 1, maxLength: 5 }),
          validClassName,
          (dirSegments, className) => {
            const path = [...dirSegments, `${className}.java`].join("/");
            const result = parseEntryPath(path);
            
            // Class name should match
            expect(result.className).toBe(className);
            
            // Source directory should be the path without the file
            const expectedSrcDir = dirSegments.join("/");
            expect(result.srcDir).toBe(expectedSrcDir);
            
            // File path should be preserved
            expect(result.filePath).toBe(path);
          }
        ),
        { numRuns: 100 }
      );
    });

    /**
     * Property: Class name extraction removes .java extension
     */
    test("property: class name never contains .java extension", () => {
      const validClassName = fc.stringMatching(/^[A-Z][a-zA-Z0-9]*$/);
      
      fc.assert(
        fc.property(validClassName, (className) => {
          const path = `src/${className}.java`;
          const result = parseEntryPath(path);
          
          expect(result.className).not.toContain(".java");
          expect(result.className).toBe(className);
        }),
        { numRuns: 100 }
      );
    });
  });

  describe("validate", () => {
    test("validates valid config", () => {
      const loader = new ConfigLoader();
      const result = loader.validate({
        entry: "src/Main.java",
        dependencies: ["com.google.guava:guava:32.1.3-jre"],
      });
      
      expect(result.valid).toBe(true);
      expect(result.errors).toHaveLength(0);
    });

    test("rejects config without entry", () => {
      const loader = new ConfigLoader();
      const result = loader.validate({} as any);
      
      expect(result.valid).toBe(false);
      expect(result.errors).toContain("'entry' field is required");
    });

    test("rejects non-.java entry", () => {
      const loader = new ConfigLoader();
      const result = loader.validate({
        entry: "src/Main.class",
      });
      
      expect(result.valid).toBe(false);
      expect(result.errors).toContain("'entry' must be a .java file");
    });

    test("rejects non-array dependencies", () => {
      const loader = new ConfigLoader();
      const result = loader.validate({
        entry: "src/Main.java",
        dependencies: "not-an-array" as any,
      });
      
      expect(result.valid).toBe(false);
      expect(result.errors).toContain("'dependencies' must be an array");
    });
  });
});
