import { describe, test, expect } from "bun:test";
import { JavaBuilder, type JavaBuildConfig } from "../../src/java/builder";

describe("JavaBuilder", () => {
  describe("configuration", () => {
    test("should use default values when no config provided", () => {
      const builder = new JavaBuilder();
      // Basic test to verify builder instantiation works
      expect(builder).toBeDefined();
    });

    test("should accept custom configuration", () => {
      const config: Partial<JavaBuildConfig> = {
        srcDir: "custom/src",
        outDir: "custom/build",
        mainClass: "com.example.Main",
      };
      const builder = new JavaBuilder(config);
      expect(builder).toBeDefined();
    });
  });
});
