/**
 * Tests for DependencyResolver
 * **Feature: java-vite-build-tool, Property 3: Classpath 分隔符平台一致性**
 */

import { describe, test, expect } from "bun:test";
import fc from "fast-check";
import { 
  DependencyResolver, 
  getClasspathSeparator, 
  buildClasspath, 
  parseClasspath,
  isWindows 
} from "../../src/core/dependency-resolver";

describe("DependencyResolver", () => {
  describe("isValidDependency", () => {
    const resolver = new DependencyResolver();

    test("accepts valid dependency format", () => {
      expect(resolver.isValidDependency("com.google.guava:guava:32.1.3-jre")).toBe(true);
      expect(resolver.isValidDependency("org.slf4j:slf4j-api:2.0.9")).toBe(true);
    });

    test("accepts extended dependency format", () => {
      expect(resolver.isValidDependency("com.example:artifact:1.0:sources")).toBe(true);
    });

    test("rejects invalid dependency format", () => {
      expect(resolver.isValidDependency("invalid")).toBe(false);
      expect(resolver.isValidDependency("group:artifact")).toBe(false);
      expect(resolver.isValidDependency("")).toBe(false);
    });

    test("rejects dependency with empty parts", () => {
      expect(resolver.isValidDependency(":artifact:1.0")).toBe(false);
      expect(resolver.isValidDependency("group::1.0")).toBe(false);
      expect(resolver.isValidDependency("group:artifact:")).toBe(false);
    });
  });

  describe("classpath operations", () => {
    /**
     * **Feature: java-vite-build-tool, Property 3: Classpath 分隔符平台一致性**
     * **Validates: Requirements 8.1, 8.2**
     * 
     * For any list of JAR paths, the classpath separator should be
     * semicolon (;) on Windows and colon (:) on Unix.
     */
    test("property: classpath uses correct platform separator", () => {
      const expectedSeparator = isWindows() ? ";" : ":";
      
      fc.assert(
        fc.property(
          fc.array(fc.stringMatching(/^[a-zA-Z0-9\/\\_.-]+\.jar$/), { minLength: 1, maxLength: 10 }),
          (paths) => {
            const classpath = buildClasspath(paths);
            
            // Should use correct separator
            if (paths.length > 1) {
              expect(classpath).toContain(expectedSeparator);
              
              // Should not contain the wrong separator
              const wrongSeparator = isWindows() ? ":" : ";";
              // Note: Windows paths may contain : in drive letters, so we check differently
              if (!isWindows()) {
                expect(classpath).not.toContain(wrongSeparator);
              }
            }
            
            // Should contain all paths
            for (const path of paths) {
              expect(classpath).toContain(path);
            }
          }
        ),
        { numRuns: 100 }
      );
    });

    /**
     * Property: Classpath round-trip consistency
     */
    test("property: buildClasspath and parseClasspath are inverse operations", () => {
      fc.assert(
        fc.property(
          fc.array(fc.stringMatching(/^[a-zA-Z0-9\/._-]+$/), { minLength: 1, maxLength: 10 }),
          (paths) => {
            // Filter out empty strings
            const validPaths = paths.filter(p => p.length > 0);
            if (validPaths.length === 0) return;
            
            const classpath = buildClasspath(validPaths);
            const parsed = parseClasspath(classpath);
            
            expect(parsed).toEqual(validPaths);
          }
        ),
        { numRuns: 100 }
      );
    });

    test("getClasspathSeparator returns correct value", () => {
      const separator = getClasspathSeparator();
      if (isWindows()) {
        expect(separator).toBe(";");
      } else {
        expect(separator).toBe(":");
      }
    });

    test("parseClasspath handles empty string", () => {
      expect(parseClasspath("")).toEqual([]);
    });

    test("buildClasspath handles single path", () => {
      const result = buildClasspath(["/path/to/file.jar"]);
      expect(result).toBe("/path/to/file.jar");
    });
  });

  describe("buildFullClasspath", () => {
    test("includes output directory first", () => {
      const resolver = new DependencyResolver();
      const result = resolver.buildFullClasspath("build/classes", ["/lib/a.jar", "/lib/b.jar"]);
      
      const separator = getClasspathSeparator();
      expect(result.startsWith("build/classes")).toBe(true);
      expect(result).toContain(`build/classes${separator}`);
    });
  });
});
