/**
 * qin-plugin-graalvm
 * GraalVM runtime support for Qin build tool
 *
 * Features:
 * - GraalVM environment detection
 * - Component management (nodejs, python, etc.)
 * - Version information
 * - Installation guidance
 */

import { join } from "path";
import { existsSync } from "fs";
import { spawnSync } from "child_process";

// ============================================================================
// Types
// ============================================================================

/**
 * GraalVM 信息
 */
export interface GraalVMInfo {
  /** GraalVM 安装路径 */
  home: string;
  /** GraalVM 版本 */
  version: string;
  /** 已安装的组件列表 */
  components: string[];
  /** Node.js 可执行文件路径 */
  nodePath?: string;
  /** Java 可执行文件路径 */
  javaPath?: string;
  /** gu 命令路径 */
  guPath?: string;
}

/**
 * GraalVM 检测结果
 */
export interface GraalVMDetectionResult {
  /** 是否检测到 GraalVM */
  found: boolean;
  /** 检测方式: 'env' | 'path' | 'gu' */
  detectedBy?: "env" | "path" | "gu";
  /** GraalVM 信息 */
  info?: GraalVMInfo;
  /** 错误信息 */
  error?: string;
}

/**
 * GraalVM 插件配置
 */
export interface GraalVMPluginOptions {
  /** 自定义 GraalVM 路径，默认自动检测 */
  home?: string;
  /** 是否在缺少组件时自动安装 */
  autoInstall?: boolean;
}

/**
 * 插件上下文
 */
interface PluginContext {
  root: string;
  config: any;
  isDev: boolean;
  log: (msg: string) => void;
  warn: (msg: string) => void;
  error: (msg: string) => void;
}

/**
 * Qin 插件接口
 */
interface QinPlugin {
  name: string;
  language?: any;
  plugins?: QinPlugin[];
  config?: (config: any) => any;
  devServer?: (ctx: any) => Promise<void>;
}

/**
 * GraalVM 插件接口
 */
export interface GraalVMPlugin extends QinPlugin {
  /** 获取 GraalVM 信息 */
  getInfo(): GraalVMInfo | null;
  /** 检查组件是否已安装 */
  isComponentInstalled(name: string): boolean;
  /** 获取 GraalVM Home 路径 */
  getGraalVMHome(): string | null;
  /** 获取已安装组件列表 */
  getInstalledComponents(): string[];
  /** 获取检测结果 */
  getDetectionResult(): GraalVMDetectionResult;
}

// ============================================================================
// Detection Functions
// ============================================================================

/**
 * 获取可执行文件扩展名
 */
function getExeExtension(): string {
  return process.platform === "win32" ? ".exe" : "";
}

/**
 * 检测 GraalVM 安装
 */
export function detectGraalVM(customHome?: string): GraalVMDetectionResult {
  const exe = getExeExtension();

  // 1. 使用自定义路径
  if (customHome && existsSync(customHome)) {
    const result = detectFromHome(customHome);
    if (result.found) {
      return { ...result, detectedBy: "env" };
    }
  }

  // 2. 检查 GRAALVM_HOME 环境变量
  const graalvmHome = process.env.GRAALVM_HOME;
  if (graalvmHome && existsSync(graalvmHome)) {
    const result = detectFromHome(graalvmHome);
    if (result.found) {
      return { ...result, detectedBy: "env" };
    }
  }

  // 3. 检查 JAVA_HOME 是否是 GraalVM
  const javaHome = process.env.JAVA_HOME;
  if (javaHome && existsSync(javaHome)) {
    const guPath = join(javaHome, "bin", `gu${exe}`);
    if (existsSync(guPath)) {
      const result = detectFromHome(javaHome);
      if (result.found) {
        return { ...result, detectedBy: "env" };
      }
    }
  }

  // 4. 尝试通过 gu 命令检测
  try {
    const guResult = spawnSync("gu", ["--version"], { encoding: "utf-8" });
    if (guResult.status === 0) {
      // 尝试获取 GraalVM home
      const homeResult = spawnSync("gu", ["--help"], { encoding: "utf-8" });
      const output = homeResult.stdout || homeResult.stderr || "";
      // 解析输出获取 home 路径（这是一个简化的实现）
      return {
        found: true,
        detectedBy: "gu",
        info: {
          home: "",
          version: parseGuVersion(guResult.stdout || guResult.stderr || ""),
          components: getInstalledComponentsFromGu(),
        },
      };
    }
  } catch {
    // gu 命令不可用
  }

  return {
    found: false,
    error: "GraalVM not found. Please set GRAALVM_HOME environment variable or install GraalVM.",
  };
}

/**
 * 从 GraalVM home 目录检测
 */
function detectFromHome(home: string): GraalVMDetectionResult {
  const exe = getExeExtension();
  const guPath = join(home, "bin", `gu${exe}`);
  const javaPath = join(home, "bin", `java${exe}`);
  const nodePath = join(home, "bin", `node${exe}`);

  // 检查 gu 命令是否存在（GraalVM 特有）
  if (!existsSync(guPath)) {
    return {
      found: false,
      error: `Not a valid GraalVM installation: ${home} (gu command not found)`,
    };
  }

  // 获取版本
  const version = getGraalVMVersion(guPath);

  // 获取已安装组件
  const components = getInstalledComponentsFromPath(guPath);

  return {
    found: true,
    info: {
      home,
      version,
      components,
      javaPath: existsSync(javaPath) ? javaPath : undefined,
      nodePath: existsSync(nodePath) ? nodePath : undefined,
      guPath,
    },
  };
}

/**
 * 获取 GraalVM 版本
 */
function getGraalVMVersion(guPath: string): string {
  try {
    const result = spawnSync(guPath, ["--version"], { encoding: "utf-8" });
    if (result.status === 0) {
      return parseGuVersion(result.stdout || result.stderr || "");
    }
  } catch {
    // 忽略错误
  }
  return "unknown";
}

/**
 * 解析 gu --version 输出
 */
function parseGuVersion(output: string): string {
  // 输出格式: "GraalVM Updater 25.0.1" 或类似
  const match = output.match(/(\d+\.\d+(?:\.\d+)?)/);
  return match ? match[1] : "unknown";
}

/**
 * 从 gu 命令获取已安装组件
 */
function getInstalledComponentsFromGu(): string[] {
  try {
    const result = spawnSync("gu", ["list"], { encoding: "utf-8" });
    if (result.status === 0) {
      return parseComponentList(result.stdout || "");
    }
  } catch {
    // 忽略错误
  }
  return [];
}

/**
 * 从指定路径的 gu 获取已安装组件
 */
function getInstalledComponentsFromPath(guPath: string): string[] {
  try {
    const result = spawnSync(guPath, ["list"], { encoding: "utf-8" });
    if (result.status === 0) {
      return parseComponentList(result.stdout || "");
    }
  } catch {
    // 忽略错误
  }
  return [];
}

/**
 * 解析组件列表输出
 */
function parseComponentList(output: string): string[] {
  const components: string[] = [];
  const lines = output.split("\n");

  for (const line of lines) {
    // 跳过标题行和空行
    if (!line.trim() || line.includes("ComponentId") || line.startsWith("-")) {
      continue;
    }

    // 解析组件 ID（通常是第一列）
    const parts = line.trim().split(/\s+/);
    if (parts.length > 0 && parts[0]) {
      components.push(parts[0]);
    }
  }

  return components;
}

/**
 * 获取已安装组件列表
 */
export function getInstalledComponents(graalvmHome?: string): string[] {
  const exe = getExeExtension();

  if (graalvmHome) {
    const guPath = join(graalvmHome, "bin", `gu${exe}`);
    if (existsSync(guPath)) {
      return getInstalledComponentsFromPath(guPath);
    }
  }

  // 尝试使用环境变量
  const envHome = process.env.GRAALVM_HOME || process.env.JAVA_HOME;
  if (envHome) {
    const guPath = join(envHome, "bin", `gu${exe}`);
    if (existsSync(guPath)) {
      return getInstalledComponentsFromPath(guPath);
    }
  }

  // 尝试直接使用 gu
  return getInstalledComponentsFromGu();
}

/**
 * 检查组件是否已安装
 */
export function isComponentInstalled(name: string, graalvmHome?: string): boolean {
  const components = getInstalledComponents(graalvmHome);
  return components.includes(name);
}

// ============================================================================
// Error Classes
// ============================================================================

/**
 * GraalVM 未找到错误
 */
export class GraalVMNotFoundError extends Error {
  constructor(message?: string) {
    super(message || "GraalVM not found");
    this.name = "GraalVMNotFoundError";
  }

  /**
   * 获取安装指南
   */
  getInstallGuide(): string {
    const isWindows = process.platform === "win32";
    return `
GraalVM 安装指南:

1. 下载 GraalVM:
   https://www.graalvm.org/downloads/

2. 设置环境变量:
   ${isWindows ? "set GRAALVM_HOME=C:\\path\\to\\graalvm" : "export GRAALVM_HOME=/path/to/graalvm"}
   ${isWindows ? "set PATH=%GRAALVM_HOME%\\bin;%PATH%" : "export PATH=$GRAALVM_HOME/bin:$PATH"}

3. 验证安装:
   gu --version
`.trim();
  }
}

/**
 * 组件未安装错误
 */
export class ComponentNotInstalledError extends Error {
  componentName: string;

  constructor(componentName: string) {
    super(`GraalVM component '${componentName}' is not installed`);
    this.name = "ComponentNotInstalledError";
    this.componentName = componentName;
  }

  /**
   * 获取安装命令
   */
  getInstallCommand(): string {
    return `gu install ${this.componentName}`;
  }
}

// ============================================================================
// Plugin Factory
// ============================================================================

/**
 * 创建 GraalVM 插件
 */
export function graalvm(options: GraalVMPluginOptions = {}): GraalVMPlugin {
  // 初始化时检测 GraalVM
  let detectionResult: GraalVMDetectionResult = detectGraalVM(options.home);

  return {
    name: "qin-plugin-graalvm",

    /**
     * 获取 GraalVM 信息
     */
    getInfo(): GraalVMInfo | null {
      return detectionResult.info || null;
    },

    /**
     * 检查组件是否已安装
     */
    isComponentInstalled(name: string): boolean {
      if (!detectionResult.info) {
        return false;
      }
      return detectionResult.info.components.includes(name);
    },

    /**
     * 获取 GraalVM Home 路径
     */
    getGraalVMHome(): string | null {
      return detectionResult.info?.home || null;
    },

    /**
     * 获取已安装组件列表
     */
    getInstalledComponents(): string[] {
      return detectionResult.info?.components || [];
    },

    /**
     * 获取检测结果
     */
    getDetectionResult(): GraalVMDetectionResult {
      return detectionResult;
    },

    /**
     * 配置钩子
     */
    config(config: any) {
      // 将 GraalVM 信息添加到配置中
      return {
        ...config,
        _graalvm: detectionResult,
      };
    },
  };
}

export default graalvm;
