/**
 * GraalVM Configuration Validator
 * Validates graalvm configuration in qin.config.ts
 */

import { existsSync } from "fs";
import { join, isAbsolute } from "path";

/**
 * 配置验证错误
 */
export interface ConfigValidationError {
  field: string;
  message: string;
  suggestion?: string;
}

/**
 * 配置验证结果
 */
export interface ConfigValidationResult {
  valid: boolean;
  errors: ConfigValidationError[];
}

/**
 * GraalVM 配置接口（与 QinConfig.graalvm 对应）
 */
export interface GraalVMConfigInput {
  home?: string;
  js?: boolean | {
    entry?: string;
    hotReload?: boolean | { debounce?: number; verbose?: boolean };
    nodeArgs?: string[];
    javaInterop?: boolean;
  };
}

/**
 * 验证 GraalVM 配置
 */
export function validateGraalVMConfig(
  config: GraalVMConfigInput | undefined,
  projectRoot: string
): ConfigValidationResult {
  const errors: ConfigValidationError[] = [];

  if (!config) {
    return { valid: true, errors: [] };
  }

  // 验证 home 路径
  if (config.home !== undefined) {
    if (typeof config.home !== "string") {
      errors.push({
        field: "graalvm.home",
        message: "graalvm.home must be a string",
        suggestion: 'graalvm: { home: "/path/to/graalvm" }',
      });
    } else if (config.home.trim() === "") {
      errors.push({
        field: "graalvm.home",
        message: "graalvm.home cannot be empty",
        suggestion: "Remove graalvm.home to use auto-detection, or provide a valid path",
      });
    } else {
      const homePath = isAbsolute(config.home)
        ? config.home
        : join(projectRoot, config.home);
      
      if (!existsSync(homePath)) {
        errors.push({
          field: "graalvm.home",
          message: `GraalVM home path does not exist: ${homePath}`,
          suggestion: "Check the path or set GRAALVM_HOME environment variable",
        });
      }
    }
  }

  // 验证 js 配置
  if (config.js !== undefined) {
    if (typeof config.js === "boolean") {
      // 布尔值是有效的
    } else if (typeof config.js === "object") {
      // 验证 entry
      if (config.js.entry !== undefined) {
        if (typeof config.js.entry !== "string") {
          errors.push({
            field: "graalvm.js.entry",
            message: "graalvm.js.entry must be a string",
            suggestion: 'graalvm: { js: { entry: "src/server/index.js" } }',
          });
        } else if (config.js.entry.trim() === "") {
          errors.push({
            field: "graalvm.js.entry",
            message: "graalvm.js.entry cannot be empty",
            suggestion: 'Provide a valid entry file path like "src/server/index.js"',
          });
        } else {
          const entryPath = isAbsolute(config.js.entry)
            ? config.js.entry
            : join(projectRoot, config.js.entry);
          
          if (!existsSync(entryPath)) {
            errors.push({
              field: "graalvm.js.entry",
              message: `Entry file does not exist: ${entryPath}`,
              suggestion: `Create the file at ${config.js.entry} or update the path`,
            });
          }
        }
      }

      // 验证 hotReload
      if (config.js.hotReload !== undefined) {
        if (typeof config.js.hotReload === "boolean") {
          // 布尔值是有效的
        } else if (typeof config.js.hotReload === "object") {
          if (config.js.hotReload.debounce !== undefined) {
            if (typeof config.js.hotReload.debounce !== "number" || config.js.hotReload.debounce < 0) {
              errors.push({
                field: "graalvm.js.hotReload.debounce",
                message: "graalvm.js.hotReload.debounce must be a non-negative number",
                suggestion: "graalvm: { js: { hotReload: { debounce: 300 } } }",
              });
            }
          }
          if (config.js.hotReload.verbose !== undefined && typeof config.js.hotReload.verbose !== "boolean") {
            errors.push({
              field: "graalvm.js.hotReload.verbose",
              message: "graalvm.js.hotReload.verbose must be a boolean",
            });
          }
        } else {
          errors.push({
            field: "graalvm.js.hotReload",
            message: "graalvm.js.hotReload must be a boolean or object",
            suggestion: "graalvm: { js: { hotReload: true } } or graalvm: { js: { hotReload: { debounce: 300 } } }",
          });
        }
      }

      // 验证 nodeArgs
      if (config.js.nodeArgs !== undefined) {
        if (!Array.isArray(config.js.nodeArgs)) {
          errors.push({
            field: "graalvm.js.nodeArgs",
            message: "graalvm.js.nodeArgs must be an array of strings",
            suggestion: 'graalvm: { js: { nodeArgs: ["--max-old-space-size=4096"] } }',
          });
        } else {
          for (let i = 0; i < config.js.nodeArgs.length; i++) {
            if (typeof config.js.nodeArgs[i] !== "string") {
              errors.push({
                field: `graalvm.js.nodeArgs[${i}]`,
                message: "All nodeArgs must be strings",
              });
            }
          }
        }
      }

      // 验证 javaInterop
      if (config.js.javaInterop !== undefined && typeof config.js.javaInterop !== "boolean") {
        errors.push({
          field: "graalvm.js.javaInterop",
          message: "graalvm.js.javaInterop must be a boolean",
          suggestion: "graalvm: { js: { javaInterop: true } }",
        });
      }
    } else {
      errors.push({
        field: "graalvm.js",
        message: "graalvm.js must be a boolean or object",
        suggestion: 'graalvm: { js: true } or graalvm: { js: { entry: "src/server/index.js" } }',
      });
    }
  }

  return {
    valid: errors.length === 0,
    errors,
  };
}

/**
 * 格式化验证错误为可读字符串
 */
export function formatValidationErrors(errors: ConfigValidationError[]): string {
  return errors
    .map((e) => {
      let msg = `[${e.field}] ${e.message}`;
      if (e.suggestion) {
        msg += `\n  Suggestion: ${e.suggestion}`;
      }
      return msg;
    })
    .join("\n\n");
}
