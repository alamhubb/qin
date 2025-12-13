/**
 * Error Formatter for Qin
 * 格式化 Java 编译错误，提供更好的开发体验
 */

import chalk from "chalk";
import { readFileSync, existsSync } from "fs";
import { join, relative } from "path";

/**
 * 解析后的错误信息
 */
export interface ParsedError {
  file: string;
  line: number;
  column?: number;
  message: string;
  type: "error" | "warning" | "note";
  code?: string;
}

/**
 * 解析 javac 错误输出
 */
export function parseJavacErrors(output: string, cwd?: string): ParsedError[] {
  const errors: ParsedError[] = [];
  const lines = output.split("\n");
  
  // javac 错误格式: file.java:line: error: message
  const errorRegex = /^(.+\.java):(\d+):\s*(error|warning|注|note):\s*(.+)$/;
  
  let i = 0;
  while (i < lines.length) {
    const line = lines[i] || "";
    const match = line.match(errorRegex);
    
    if (match) {
      const [, file, lineNum, type, message] = match;
      if (file && lineNum && message) {
        const error: ParsedError = {
          file: cwd ? relative(cwd, file) : file,
          line: parseInt(lineNum, 10),
          message: message.trim(),
          type: type === "warning" ? "warning" : type === "note" || type === "注" ? "note" : "error",
        };
        
        // 尝试获取代码行和列指示器
        const nextLine = lines[i + 1];
        if (i + 1 < lines.length && nextLine && !nextLine.match(errorRegex)) {
          error.code = nextLine;
          i++;
          
          // 检查是否有 ^ 指示器
          const caretLine = lines[i + 1];
          if (i + 1 < lines.length && caretLine && caretLine.includes("^")) {
            error.column = caretLine.indexOf("^") + 1;
            i++;
          }
        }
        
        errors.push(error);
      }
    }
    i++;
  }
  
  return errors;
}

/**
 * 格式化单个错误
 */
export function formatError(error: ParsedError, cwd?: string): string {
  const lines: string[] = [];
  
  // 错误类型颜色
  const typeColor = error.type === "error" ? chalk.red : error.type === "warning" ? chalk.yellow : chalk.gray;
  const typeLabel = error.type === "error" ? "错误" : error.type === "warning" ? "警告" : "注意";
  
  // 文件位置
  const location = error.column 
    ? `${error.file}:${error.line}:${error.column}`
    : `${error.file}:${error.line}`;
  
  lines.push(`${typeColor.bold(`${typeLabel}:`)} ${error.message}`);
  lines.push(`  ${chalk.cyan("-->")} ${chalk.gray(location)}`);
  
  // 显示代码上下文
  if (cwd) {
    const filePath = join(cwd, error.file);
    if (existsSync(filePath)) {
      try {
        const content = readFileSync(filePath, "utf-8");
        const sourceLines = content.split("\n");
        const lineIndex = error.line - 1;
        
        // 显示上下文（前后各1行）
        const startLine = Math.max(0, lineIndex - 1);
        const endLine = Math.min(sourceLines.length - 1, lineIndex + 1);
        
        lines.push("   |");
        
        for (let i = startLine; i <= endLine; i++) {
          const lineNum = String(i + 1).padStart(4);
          const isErrorLine = i === lineIndex;
          const prefix = isErrorLine ? typeColor(">") : " ";
          const lineContent = sourceLines[i] || "";
          
          if (isErrorLine) {
            lines.push(`${prefix}${chalk.gray(lineNum)} | ${lineContent}`);
            
            // 显示错误位置指示器
            if (error.column) {
              const spaces = " ".repeat(error.column - 1);
              lines.push(`   ${" ".repeat(4)} | ${spaces}${typeColor("^")}`);
            }
          } else {
            lines.push(`${prefix}${chalk.gray(lineNum)} | ${chalk.gray(lineContent)}`);
          }
        }
        
        lines.push("   |");
      } catch {
        // 无法读取文件，跳过代码显示
      }
    }
  }
  
  return lines.join("\n");
}

/**
 * 格式化所有错误
 */
export function formatErrors(errors: ParsedError[], cwd?: string): string {
  if (errors.length === 0) {
    return "";
  }
  
  const formatted = errors.map(e => formatError(e, cwd)).join("\n\n");
  
  // 统计
  const errorCount = errors.filter(e => e.type === "error").length;
  const warningCount = errors.filter(e => e.type === "warning").length;
  
  const summary: string[] = [];
  if (errorCount > 0) {
    summary.push(chalk.red(`${errorCount} 个错误`));
  }
  if (warningCount > 0) {
    summary.push(chalk.yellow(`${warningCount} 个警告`));
  }
  
  return `${formatted}\n\n${summary.join(", ")}`;
}

/**
 * 格式化 javac 输出的便捷函数
 */
export function formatJavacOutput(output: string, cwd?: string): string {
  const errors = parseJavacErrors(output, cwd);
  
  if (errors.length === 0) {
    // 如果没有解析到结构化错误，返回原始输出
    return output;
  }
  
  return formatErrors(errors, cwd);
}
