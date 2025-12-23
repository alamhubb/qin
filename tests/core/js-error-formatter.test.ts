/**
 * Tests for JavaScript Error Formatter
 * **Feature: graalvm-polyglot-js-runner**
 */

import { describe, test, expect } from "bun:test";
import fc from "fast-check";
import {
  parseJsError,
  formatJsError,
  errorContainsPath,
  type JsError,
} from "../../src/core/js-error-formatter";

describe("parseJsError", () => {
  test("parses GraalVM not available error", () => {
    const stderr = "'java' is not recognized as an internal or external command";
    const error = parseJsError(stderr);
    
    expect(error.type).toBe("graalvm");
    expect(error.message).toContain("not installed");
  });

  test("parses file not found error", () => {
    const stderr = "Error: File not found: /path/to/script.js";
    const error = parseJsError(stderr);
    
    expect(error.type).toBe("file");
    expect(error.file).toBe("/path/to/script.js");
  });

  test("parses syntax error with location", () => {
    const stderr = `SyntaxError in /test/file.js
  File: file.js
  Line: 10, Column: 5
  Message: Unexpected token '}'`;
    
    const error = parseJsError(stderr);
    
    expect(error.type).toBe("syntax");
    expect(error.file).toBe("file.js");
    expect(error.line).toBe(10);
    expect(error.column).toBe(5);
    expect(error.message).toContain("Unexpected token");
  });

  test("parses runtime error with stack trace", () => {
    const stderr = `Error in /test/file.js
  File: file.js
  Line: 20
  Message: Cannot read property 'foo' of null

Stack trace:
    at bar (file.js:20)
    at main (file.js:30)`;
    
    const error = parseJsError(stderr);
    
    expect(error.type).toBe("runtime");
    expect(error.line).toBe(20);
    expect(error.stack).toContain("at bar");
  });

  test("handles empty stderr", () => {
    const error = parseJsError("");
    expect(error.type).toBe("runtime");
    expect(error.message).toBe("Unknown error occurred");
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 3: Error Message Path Inclusion**
   * **Validates: Requirements 1.4, 4.4**
   * 
   * For any file-related error, the error message SHALL contain the original file path.
   */
  test("property: file errors contain the file path", () => {
    const validPath = fc.stringMatching(/^\/[a-zA-Z0-9_\/]+\.js$/);

    fc.assert(
      fc.property(validPath, (filePath) => {
        const stderr = `Error: File not found: ${filePath}`;
        const error = parseJsError(stderr);
        
        expect(error.type).toBe("file");
        expect(error.file).toBe(filePath);
      }),
      { numRuns: 100 }
    );
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 6: Error Location Formatting**
   * **Validates: Requirements 4.1, 4.2**
   * 
   * For any JavaScript error with location information, the parsed error
   * SHALL contain the file name, line number, and column number.
   */
  test("property: syntax errors with location are parsed correctly", () => {
    const validLine = fc.integer({ min: 1, max: 10000 });
    const validColumn = fc.integer({ min: 1, max: 500 });
    const validFile = fc.stringMatching(/^[a-zA-Z0-9_-]+\.js$/);

    fc.assert(
      fc.property(validFile, validLine, validColumn, (file, line, column) => {
        const stderr = `SyntaxError in ${file}
  File: ${file}
  Line: ${line}, Column: ${column}
  Message: Test error`;
        
        const error = parseJsError(stderr);
        
        expect(error.type).toBe("syntax");
        expect(error.file).toBe(file);
        expect(error.line).toBe(line);
        expect(error.column).toBe(column);
      }),
      { numRuns: 100 }
    );
  });
});

describe("formatJsError", () => {
  test("formats syntax error", () => {
    const error: JsError = {
      type: "syntax",
      message: "Unexpected token",
      file: "test.js",
      line: 10,
      column: 5,
    };
    
    const formatted = formatJsError(error);
    
    expect(formatted).toContain("Syntax Error");
    expect(formatted).toContain("test.js");
    expect(formatted).toContain("10");
    expect(formatted).toContain("5");
    expect(formatted).toContain("Unexpected token");
  });

  test("formats runtime error with stack", () => {
    const error: JsError = {
      type: "runtime",
      message: "TypeError",
      file: "app.js",
      line: 20,
      stack: "at foo (app.js:20)\nat bar (app.js:30)",
    };
    
    const formatted = formatJsError(error);
    
    expect(formatted).toContain("Runtime Error");
    expect(formatted).toContain("app.js");
    expect(formatted).toContain("Stack trace");
    expect(formatted).toContain("at foo");
  });

  test("formats GraalVM error with installation guide", () => {
    const error: JsError = {
      type: "graalvm",
      message: "GraalVM not installed",
    };
    
    const formatted = formatJsError(error);
    
    expect(formatted).toContain("GraalVM");
    expect(formatted).toContain("install");
    expect(formatted).toContain("graalvm.org");
  });

  /**
   * Property: formatted output always contains the error message
   */
  test("property: formatted output contains error message", () => {
    const errorTypes = fc.constantFrom("syntax", "runtime", "file", "graalvm") as fc.Arbitrary<"syntax" | "runtime" | "file" | "graalvm">;
    const errorMessage = fc.stringMatching(/^[a-zA-Z0-9 ]+$/);

    fc.assert(
      fc.property(errorTypes, errorMessage, (type, message) => {
        const error: JsError = { type, message };
        const formatted = formatJsError(error);
        
        expect(formatted).toContain(message);
      }),
      { numRuns: 50 }
    );
  });
});

describe("errorContainsPath", () => {
  test("detects full path in error", () => {
    const stderr = "Error: File not found: /path/to/script.js";
    expect(errorContainsPath(stderr, "/path/to/script.js")).toBe(true);
  });

  test("detects filename in error", () => {
    const stderr = "SyntaxError in script.js at line 10";
    expect(errorContainsPath(stderr, "/some/path/script.js")).toBe(true);
  });

  test("handles Windows paths", () => {
    const stderr = "Error in C:\\Users\\test\\script.js";
    expect(errorContainsPath(stderr, "C:\\Users\\test\\script.js")).toBe(true);
  });

  test("returns false for non-matching path", () => {
    const stderr = "Error in other.js";
    expect(errorContainsPath(stderr, "/path/to/script.js")).toBe(false);
  });

  test("handles empty inputs", () => {
    expect(errorContainsPath("", "/path/to/file.js")).toBe(false);
    expect(errorContainsPath("error", "")).toBe(false);
  });

  /**
   * **Feature: graalvm-polyglot-js-runner, Property 3: Error Message Path Inclusion**
   * **Validates: Requirements 1.4, 4.4**
   * 
   * For any error message containing a file path, errorContainsPath should return true.
   */
  test("property: detects paths in error messages", () => {
    const validPath = fc.stringMatching(/^\/[a-zA-Z0-9_]+\/[a-zA-Z0-9_]+\.js$/);

    fc.assert(
      fc.property(validPath, (filePath) => {
        const stderr = `Error: Something went wrong in ${filePath}`;
        expect(errorContainsPath(stderr, filePath)).toBe(true);
      }),
      { numRuns: 100 }
    );
  });
});
