/**
 * Tests for FatJarBuilder
 * **Feature: java-vite-build-tool, Property 4: 签名文件清理完整性**
 * **Feature: java-vite-build-tool, Property 5: Manifest 主类正确性**
 */

import { describe, test, expect, beforeEach, afterEach } from "bun:test";
import fc from "fast-check";
import { join } from "path";
import { mkdir, writeFile, readdir, rm, readFile } from "fs/promises";
import { 
  FatJarBuilder, 
  generateManifestContent, 
  parseManifestMainClass 
} from "../../src/core/fat-jar-builder";

describe("FatJarBuilder", () => {
  const testDir = join(process.cwd(), ".test-temp");

  beforeEach(async () => {
    await mkdir(testDir, { recursive: true });
  });

  afterEach(async () => {
    await rm(testDir, { recursive: true, force: true });
  });

  describe("generateManifestContent", () => {
    /**
     * **Feature: java-vite-build-tool, Property 5: Manifest 主类正确性**
     * **Validates: Requirements 6.5**
     * 
     * For any main class name, the generated MANIFEST.MF should contain
     * the correct Main-Class entry.
     */
    test("property: manifest contains correct Main-Class entry", () => {
      const validClassName = fc.stringMatching(/^[A-Z][a-zA-Z0-9]*$/);
      
      fc.assert(
        fc.property(validClassName, (className) => {
          const manifest = generateManifestContent(className);
          
          // Should contain Main-Class entry
          expect(manifest).toContain(`Main-Class: ${className}`);
          
          // Should be parseable
          const parsed = parseManifestMainClass(manifest);
          expect(parsed).toBe(className);
        }),
        { numRuns: 100 }
      );
    });

    /**
     * Property: Manifest contains required headers
     */
    test("property: manifest contains required headers", () => {
      const validClassName = fc.stringMatching(/^[A-Z][a-zA-Z0-9]*$/);
      
      fc.assert(
        fc.property(validClassName, (className) => {
          const manifest = generateManifestContent(className);
          
          expect(manifest).toContain("Manifest-Version: 1.0");
          expect(manifest).toContain("Main-Class:");
          expect(manifest).toContain("Created-By:");
        }),
        { numRuns: 100 }
      );
    });

    test("generates valid manifest for simple class", () => {
      const manifest = generateManifestContent("Main");
      
      expect(manifest).toContain("Manifest-Version: 1.0");
      expect(manifest).toContain("Main-Class: Main");
      expect(manifest).toContain("Created-By: Qin");
    });

    test("generates valid manifest for package class", () => {
      const manifest = generateManifestContent("com.example.App");
      
      expect(manifest).toContain("Main-Class: com.example.App");
    });
  });

  describe("parseManifestMainClass", () => {
    test("parses Main-Class from manifest", () => {
      const manifest = `Manifest-Version: 1.0
Main-Class: com.example.Main
Created-By: Qin
`;
      expect(parseManifestMainClass(manifest)).toBe("com.example.Main");
    });

    test("returns null for manifest without Main-Class", () => {
      const manifest = `Manifest-Version: 1.0
Created-By: Qin
`;
      expect(parseManifestMainClass(manifest)).toBeNull();
    });
  });

  describe("cleanSignatures", () => {
    /**
     * **Feature: java-vite-build-tool, Property 4: 签名文件清理完整性**
     * **Validates: Requirements 6.3**
     * 
     * For any set of signature files in META-INF, after cleaning,
     * no *.SF, *.DSA, or *.RSA files should remain.
     */
    test("property: no signature files remain after cleaning", async () => {
      const signatureExtensions = [".SF", ".DSA", ".RSA", ".sf", ".dsa", ".rsa"];
      
      // Create a test scenario
      const metaInfDir = join(testDir, "META-INF");
      await mkdir(metaInfDir, { recursive: true });

      // Create some signature files
      const signatureFiles = ["CERT.SF", "CERT.DSA", "CERT.RSA", "OTHER.SF"];
      for (const file of signatureFiles) {
        await writeFile(join(metaInfDir, file), "test content");
      }

      // Create a non-signature file that should be preserved
      await writeFile(join(metaInfDir, "MANIFEST.MF"), "Manifest-Version: 1.0");
      await writeFile(join(metaInfDir, "services.txt"), "service content");

      // Create builder and clean signatures
      const config = { entry: "src/Main.java" };
      const builder = new FatJarBuilder(config, {}, testDir);
      
      // Manually set tempDir to testDir for this test
      (builder as any).tempDir = testDir;
      await builder.cleanSignatures();

      // Verify no signature files remain
      const remainingFiles = await readdir(metaInfDir);
      
      for (const file of remainingFiles) {
        const ext = file.toLowerCase();
        expect(ext.endsWith(".sf")).toBe(false);
        expect(ext.endsWith(".dsa")).toBe(false);
        expect(ext.endsWith(".rsa")).toBe(false);
      }

      // Verify non-signature files are preserved
      expect(remainingFiles).toContain("MANIFEST.MF");
      expect(remainingFiles).toContain("services.txt");
    });

    test("handles missing META-INF directory gracefully", async () => {
      const config = { entry: "src/Main.java" };
      const builder = new FatJarBuilder(config, {}, testDir);
      (builder as any).tempDir = testDir;

      // Should not throw
      await expect(builder.cleanSignatures()).resolves.toBeUndefined();
    });
  });

  describe("generateManifest", () => {
    test("creates META-INF directory and MANIFEST.MF", async () => {
      const config = { entry: "src/Main.java" };
      const builder = new FatJarBuilder(config, {}, testDir);
      (builder as any).tempDir = testDir;

      await builder.generateManifest("com.example.Main");

      const manifestPath = join(testDir, "META-INF", "MANIFEST.MF");
      const content = await readFile(manifestPath, "utf-8");

      expect(content).toContain("Main-Class: com.example.Main");
      expect(content).toContain("Manifest-Version: 1.0");
    });
  });
});
