import { describe, test, expect } from "bun:test";
import { parseDependency } from "../../src/java/package-manager";

describe("QinPackageManager", () => {
  describe("dependency format validation (npm style)", () => {
    test("should parse name@version format", () => {
      const result = parseDependency("lodash@4.17.21");
      expect(result.valid).toBe(true);
      expect(result.dependency?.name).toBe("lodash");
      expect(result.dependency?.version).toBe("4.17.21");
    });

    test("should parse name without version as latest", () => {
      const result = parseDependency("lodash");
      expect(result.valid).toBe(true);
      expect(result.dependency?.name).toBe("lodash");
      expect(result.dependency?.version).toBe("latest");
    });

    test("should handle scoped packages", () => {
      const result = parseDependency("@types/node@18.0.0");
      expect(result.valid).toBe(true);
      expect(result.dependency?.name).toBe("@types/node");
      expect(result.dependency?.version).toBe("18.0.0");
    });

    test("should reject empty string", () => {
      const result = parseDependency("");
      expect(result.valid).toBe(false);
      expect(result.error).toBeDefined();
    });
  });
});
