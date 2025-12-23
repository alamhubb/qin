/**
 * JavaScript Error Formatter
 * 解析和格式化 JavaScript 执行错误
 */

import chalk from "chalk";

/**
 * JavaScript 错误类型
 */
export type JsErrorType = "syntax" | "runtime" | "file" | "graalvm";

/**
 * 解析后的 JavaScript 错误
 */
export interface JsError {
  /** 错误类型 */
  type: JsErrorType;
  /** 错误消息 */
  message: string;
  /** 文件路径 */
  file?: string;
  /** 行号 */
  line?: number;
  /** 列号 */
  column?: number;
  /** 堆栈跟踪 */
  stack?: string;
}

/**
 * 解析 Java 进程的错误输出
 * @param stderr 标准错误输出
 * @returns 解析后的错误对象
 */
export function parseJsError(stderr: string): JsError {
  if (!stderr || stderr.trim().length === 0) {
    return {
      type: "runtime",
      message: "Unknown error occurred",
    };
  }

  const lines = stderr.split("\n").map((l) => l.trim()).filter((l) => l.length > 0);

  // 检测 GraalVM 不可用
  if (
    stderr.includes("java: command not found") ||
    stderr.includes("'java' is not recognized") ||
    stderr.includes("Java not found")
  ) {
    return {
      type: "graalvm",
      message: "GraalVM/Java is not installed or not in PATH",
    };
  }

  // 检测文件不存在
  if (stderr.includes("File not found:") || stderr.includes("not found:")) {
    const fileMatch = stderr.match(/(?:File not found|not found):\s*(.+)/i);
    return {
      type: "file",
      message: "File not found",
      file: fileMatch?.[1]?.trim(),
    };
  }

  // 检测文件读取错误
  if (stderr.includes("Cannot read file:")) {
    const fileMatch = stderr.match(/Cannot read file:\s*(.+)/i);
    return {
      type: "file",
      message: "Cannot read file",
      file: fileMatch?.[1]?.trim(),
    };
  }

  // 检测语法错误
  if (stderr.includes("SyntaxError") || stderr.includes("syntax error")) {
    const error = parseSyntaxError(stderr, lines);
    return error;
  }

  // 检测运行时错误
  return parseRuntimeError(stderr, lines);
}

/**
 * 解析语法错误
 */
function parseSyntaxError(stderr: string, _lines: string[]): JsError {
  const error: JsError = {
    type: "syntax",
    message: "Syntax error",
  };

  // 尝试提取文件名
  // 格式: "SyntaxError in /path/to/file.js"
  const fileMatch = stderr.match(/SyntaxError in\s+(.+\.(?:js|mjs))/i);
  if (fileMatch?.[1]) {
    error.file = fileMatch[1];
  }

  // 尝试提取位置
  // 格式: "File: xxx" 和 "Line: N, Column: M"
  const fileLineMatch = stderr.match(/File:\s*(.+)/);
  if (fileLineMatch?.[1]) {
    error.file = fileLineMatch[1].trim();
  }

  const locationMatch = stderr.match(/Line:\s*(\d+)(?:,\s*Column:\s*(\d+))?/);
  if (locationMatch?.[1]) {
    error.line = parseInt(locationMatch[1], 10);
    if (locationMatch[2]) {
      error.column = parseInt(locationMatch[2], 10);
    }
  }

  // 尝试提取消息
  const messageMatch = stderr.match(/Message:\s*(.+)/);
  if (messageMatch?.[1]) {
    error.message = messageMatch[1].trim();
  } else {
    // 尝试从 SyntaxError 行提取
    const syntaxMatch = stderr.match(/SyntaxError[:\s]+(.+)/i);
    if (syntaxMatch?.[1]) {
      error.message = syntaxMatch[1].trim();
    }
  }

  return error;
}

/**
 * 解析运行时错误
 */
function parseRuntimeError(stderr: string, _lines: string[]): JsError {
  const error: JsError = {
    type: "runtime",
    message: "Runtime error",
  };

  // 尝试提取文件名
  const fileMatch = stderr.match(/Error in\s+(.+\.(?:js|mjs))/i);
  if (fileMatch?.[1]) {
    error.file = fileMatch[1];
  }

  // 尝试提取位置
  const fileLineMatch = stderr.match(/File:\s*(.+)/);
  if (fileLineMatch?.[1]) {
    error.file = fileLineMatch[1].trim();
  }

  const locationMatch = stderr.match(/Line:\s*(\d+)(?:,\s*Column:\s*(\d+))?/);
  if (locationMatch?.[1]) {
    error.line = parseInt(locationMatch[1], 10);
    if (locationMatch[2]) {
      error.column = parseInt(locationMatch[2], 10);
    }
  }

  // 尝试提取消息
  const messageMatch = stderr.match(/Message:\s*(.+)/);
  if (messageMatch?.[1]) {
    error.message = messageMatch[1].trim();
  } else {
    // 尝试从 Error: 行提取
    const errorMatch = stderr.match(/Error[:\s]+(.+)/i);
    if (errorMatch?.[1]) {
      error.message = errorMatch[1].trim();
    }
  }

  // 提取堆栈跟踪
  const stackIndex = stderr.indexOf("Stack trace:");
  if (stackIndex >= 0) {
    error.stack = stderr.substring(stackIndex + "Stack trace:".length).trim();
  }

  return error;
}

/**
 * 格式化错误为用户友好的输出
 * @param error 解析后的错误对象
 * @returns 格式化的错误字符串
 */
export function formatJsError(error: JsError): string {
  const lines: string[] = [];

  // 错误类型标题
  switch (error.type) {
    case "syntax":
      lines.push(chalk.red("✗ JavaScript Syntax Error"));
      break;
    case "runtime":
      lines.push(chalk.red("✗ JavaScript Runtime Error"));
      break;
    case "file":
      lines.push(chalk.red("✗ File Error"));
      break;
    case "graalvm":
      lines.push(chalk.red("✗ GraalVM Not Available"));
      break;
  }

  // 文件位置
  if (error.file) {
    let location = `  ${chalk.gray("File:")} ${error.file}`;
    if (error.line !== undefined) {
      location += `:${error.line}`;
      if (error.column !== undefined) {
        location += `:${error.column}`;
      }
    }
    lines.push(location);
  }

  // 错误消息
  lines.push(`  ${chalk.gray("Message:")} ${error.message}`);

  // 堆栈跟踪
  if (error.stack) {
    lines.push("");
    lines.push(chalk.gray("  Stack trace:"));
    const stackLines = error.stack.split("\n").filter((l) => l.trim().length > 0);
    for (const stackLine of stackLines.slice(0, 10)) {
      lines.push(chalk.gray(`    ${stackLine.trim()}`));
    }
    if (stackLines.length > 10) {
      lines.push(chalk.gray(`    ... and ${stackLines.length - 10} more`));
    }
  }

  // GraalVM 安装提示
  if (error.type === "graalvm") {
    lines.push("");
    lines.push(chalk.yellow("  To install GraalVM:"));
    lines.push(chalk.gray("    1. Download from https://www.graalvm.org/downloads/"));
    lines.push(chalk.gray("    2. Set GRAALVM_HOME environment variable"));
    lines.push(chalk.gray("    3. Add $GRAALVM_HOME/bin to PATH"));
  }

  return lines.join("\n");
}

/**
 * 检查错误输出是否包含文件路径
 * @param stderr 标准错误输出
 * @param filePath 要检查的文件路径
 * @returns 是否包含文件路径
 */
export function errorContainsPath(stderr: string, filePath: string): boolean {
  if (!stderr || !filePath) {
    return false;
  }
  
  // 标准化路径分隔符
  const normalizedPath = filePath.replace(/\\/g, "/");
  const normalizedStderr = stderr.replace(/\\/g, "/");
  
  // 检查完整路径
  if (normalizedStderr.includes(normalizedPath)) {
    return true;
  }
  
  // 检查文件名
  const fileName = normalizedPath.split("/").pop();
  if (fileName && normalizedStderr.includes(fileName)) {
    return true;
  }
  
  return false;
}
