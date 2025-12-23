/**
 * JavaScript Command Parser
 * 解析 qin run xxx.js 和 qin xxx.js 命令
 */

import { resolve, isAbsolute } from "path";
import { existsSync } from "fs";

/**
 * JavaScript 命令选项
 */
export interface JsCommandOptions {
  /** JavaScript 文件路径 */
  file: string;
  /** 传递给 JavaScript 的参数 */
  args: string[];
  /** 工作目录 */
  cwd: string;
}

/**
 * 命令解析结果
 */
export interface ParseResult {
  /** 是否是 JavaScript 执行命令 */
  isJsCommand: boolean;
  /** 解析后的选项 */
  options?: JsCommandOptions;
  /** 解析错误 */
  error?: string;
}

/**
 * 支持的 JavaScript 文件扩展名
 */
const JS_EXTENSIONS = [".js", ".mjs"];

/**
 * 检查文件是否是 JavaScript 文件
 * @param filePath 文件路径
 * @returns 是否是 JavaScript 文件
 */
export function isJavaScriptFile(filePath: string): boolean {
  if (!filePath || typeof filePath !== "string") {
    return false;
  }
  const lowerPath = filePath.toLowerCase();
  return JS_EXTENSIONS.some((ext) => lowerPath.endsWith(ext));
}

/**
 * 解析相对路径为绝对路径
 * @param filePath 文件路径（相对或绝对）
 * @param cwd 当前工作目录
 * @returns 绝对路径
 */
export function resolveFilePath(filePath: string, cwd: string): string {
  if (!filePath) {
    return "";
  }
  if (isAbsolute(filePath)) {
    return filePath;
  }
  return resolve(cwd, filePath);
}

/**
 * 解析命令行参数，识别 JavaScript 执行命令
 * 支持两种形式：
 * - qin run xxx.js [-- args...]
 * - qin xxx.js [-- args...]
 * 
 * @param argv 命令行参数数组（不包含 node 和脚本路径）
 * @param cwd 当前工作目录，默认 process.cwd()
 * @returns 解析结果
 */
export function parseJsCommand(argv: string[], cwd: string = process.cwd()): ParseResult {
  if (!argv || argv.length === 0) {
    return { isJsCommand: false };
  }

  let jsFile: string | undefined;
  let scriptArgs: string[] = [];

  // 查找 -- 分隔符位置
  const separatorIndex = argv.indexOf("--");
  const commandArgs = separatorIndex >= 0 ? argv.slice(0, separatorIndex) : argv;
  scriptArgs = separatorIndex >= 0 ? argv.slice(separatorIndex + 1) : [];

  // 情况 1: qin run xxx.js
  if (commandArgs[0] === "run" && commandArgs.length >= 2) {
    const potentialFile = commandArgs[1]!;
    if (isJavaScriptFile(potentialFile)) {
      jsFile = potentialFile;
      // run 命令后的其他参数（-- 之前）也作为脚本参数
      if (commandArgs.length > 2) {
        scriptArgs = [...commandArgs.slice(2), ...scriptArgs];
      }
    }
  }
  // 情况 2: qin xxx.js（简写形式）
  else if (commandArgs.length >= 1 && isJavaScriptFile(commandArgs[0]!)) {
    jsFile = commandArgs[0]!;
    // 文件后的其他参数（-- 之前）也作为脚本参数
    if (commandArgs.length > 1) {
      scriptArgs = [...commandArgs.slice(1), ...scriptArgs];
    }
  }

  // 不是 JavaScript 命令
  if (!jsFile) {
    return { isJsCommand: false };
  }

  // 解析文件路径
  const resolvedPath = resolveFilePath(jsFile, cwd);

  // 检查文件是否存在
  if (!existsSync(resolvedPath)) {
    return {
      isJsCommand: true,
      error: `JavaScript file not found: ${resolvedPath}`,
    };
  }

  return {
    isJsCommand: true,
    options: {
      file: resolvedPath,
      args: scriptArgs,
      cwd,
    },
  };
}

/**
 * 从完整的 process.argv 解析 JavaScript 命令
 * 自动跳过 node/bun 和脚本路径
 * 
 * @param processArgv process.argv
 * @param cwd 当前工作目录
 * @returns 解析结果
 */
export function parseJsCommandFromProcessArgv(
  processArgv: string[],
  cwd: string = process.cwd()
): ParseResult {
  // 跳过 node/bun 可执行文件和脚本路径
  const argv = processArgv.slice(2);
  return parseJsCommand(argv, cwd);
}
