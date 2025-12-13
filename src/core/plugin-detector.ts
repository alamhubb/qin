/**
 * Plugin Auto-Detector
 * 根据项目内容自动检测需要启用的插件
 * 实现零配置体验
 */

import { join } from "path";
import { existsSync } from "fs";
import type { QinConfig, QinPlugin } from "../types";

/**
 * 检测结果
 */
export interface DetectionResult {
  /** 检测到的语言 */
  languages: string[];
  /** 检测到的特性 */
  features: string[];
  /** 建议的插件 */
  suggestedPlugins: string[];
  /** 检测到的入口文件 */
  entry?: string;
  /** 检测到的前端目录 */
  clientDir?: string;
}

/**
 * 插件检测器
 */
export class PluginDetector {
  private cwd: string;

  constructor(cwd?: string) {
    this.cwd = cwd || process.cwd();
  }

  /**
   * 检测项目并返回建议的插件配置
   */
  async detect(): Promise<DetectionResult> {
    const result: DetectionResult = {
      languages: [],
      features: [],
      suggestedPlugins: [],
    };

    // 检测 Java
    if (await this.hasJavaFiles()) {
      result.languages.push("java");
      result.suggestedPlugins.push("qin-plugin-java");
      result.entry = await this.findJavaEntry();
    }

    // 检测 Kotlin
    if (await this.hasKotlinFiles()) {
      result.languages.push("kotlin");
      result.suggestedPlugins.push("qin-plugin-kotlin");
    }

    // 检测前端 (Vite)
    const clientDir = this.findClientDir();
    if (clientDir) {
      result.features.push("frontend");
      result.suggestedPlugins.push("qin-plugin-vite");
      result.clientDir = clientDir;
    }

    // 检测 Spring Boot
    if (await this.hasSpringBoot()) {
      result.features.push("spring-boot");
      // Spring Boot 支持可以作为 java 插件的增强
    }

    // 检测 Lombok
    if (await this.hasLombok()) {
      result.features.push("lombok");
    }

    return result;
  }

  /**
   * 检测是否有 Java 文件
   */
  private async hasJavaFiles(): Promise<boolean> {
    const srcDir = join(this.cwd, "src");
    if (!existsSync(srcDir)) return false;

    try {
      const glob = new Bun.Glob("**/*.java");
      for await (const _ of glob.scan({ cwd: srcDir })) {
        return true;
      }
    } catch {}
    return false;
  }

  /**
   * 检测是否有 Kotlin 文件
   */
  private async hasKotlinFiles(): Promise<boolean> {
    const srcDir = join(this.cwd, "src");
    if (!existsSync(srcDir)) return false;

    try {
      const glob = new Bun.Glob("**/*.kt");
      for await (const _ of glob.scan({ cwd: srcDir })) {
        return true;
      }
    } catch {}
    return false;
  }

  /**
   * 查找 Java 入口文件
   */
  private async findJavaEntry(): Promise<string | undefined> {
    const candidates = [
      "src/Main.java",
      "src/App.java",
      "src/Application.java",
      "src/server/Main.java",
      "src/main/java/Main.java",
    ];

    for (const candidate of candidates) {
      if (existsSync(join(this.cwd, candidate))) {
        return candidate;
      }
    }

    // 查找包含 main 方法的文件
    const srcDir = join(this.cwd, "src");
    if (!existsSync(srcDir)) return undefined;

    try {
      const glob = new Bun.Glob("**/*.java");
      for await (const file of glob.scan({ cwd: srcDir })) {
        const content = await Bun.file(join(srcDir, file)).text();
        if (content.includes("public static void main")) {
          return `src/${file}`;
        }
      }
    } catch {}

    return undefined;
  }

  /**
   * 查找前端目录
   */
  private findClientDir(): string | undefined {
    const candidates = [
      { dir: "src/client", check: "index.html" },
      { dir: "client", check: "index.html" },
      { dir: "web", check: "index.html" },
      { dir: "frontend", check: "index.html" },
      { dir: "src/client", check: "package.json" },
      { dir: "client", check: "package.json" },
    ];

    for (const { dir, check } of candidates) {
      const fullDir = join(this.cwd, dir);
      if (existsSync(fullDir) && existsSync(join(fullDir, check))) {
        return dir;
      }
    }

    return undefined;
  }

  /**
   * 检测是否使用 Spring Boot
   */
  private async hasSpringBoot(): Promise<boolean> {
    // 检查配置文件
    const configFiles = [
      "src/resources/application.yml",
      "src/resources/application.yaml",
      "src/resources/application.properties",
      "src/main/resources/application.yml",
      "src/main/resources/application.properties",
    ];

    for (const file of configFiles) {
      if (existsSync(join(this.cwd, file))) {
        return true;
      }
    }

    // 检查 qin.config.ts 中的依赖
    // TODO: 解析配置文件检查 spring-boot 依赖

    return false;
  }

  /**
   * 检测是否使用 Lombok
   */
  private async hasLombok(): Promise<boolean> {
    const srcDir = join(this.cwd, "src");
    if (!existsSync(srcDir)) return false;

    try {
      const glob = new Bun.Glob("**/*.java");
      for await (const file of glob.scan({ cwd: srcDir })) {
        const content = await Bun.file(join(srcDir, file)).text();
        if (content.includes("import lombok.") || content.includes("@Data") || content.includes("@Getter")) {
          return true;
        }
      }
    } catch {}

    return false;
  }
}

/**
 * 根据检测结果自动配置插件
 * 这个函数会在 ConfigLoader 中调用
 */
export async function autoConfigurePlugins(
  config: QinConfig,
  cwd?: string
): Promise<QinConfig> {
  // 如果用户已经配置了 plugins，不自动检测
  if (config.plugins && config.plugins.length > 0) {
    return config;
  }

  const detector = new PluginDetector(cwd);
  const detection = await detector.detect();

  // 构建自动配置
  const autoConfig: Partial<QinConfig> = {};

  // 自动设置入口
  if (detection.entry && !config.entry) {
    autoConfig.entry = detection.entry;
  }

  // 自动设置前端配置
  if (detection.clientDir && !config.client) {
    autoConfig.client = {
      root: detection.clientDir,
    };
  }

  // 返回合并后的配置
  // 注意：实际的插件实例化在 CLI 中处理
  return {
    ...config,
    ...autoConfig,
    // 存储检测结果供 CLI 使用
    _detected: detection,
  } as QinConfig & { _detected: DetectionResult };
}

/**
 * 获取检测结果的友好描述
 */
export function describeDetection(detection: DetectionResult): string {
  const parts: string[] = [];

  if (detection.languages.length > 0) {
    parts.push(`语言: ${detection.languages.join(", ")}`);
  }

  if (detection.features.length > 0) {
    parts.push(`特性: ${detection.features.join(", ")}`);
  }

  if (detection.entry) {
    parts.push(`入口: ${detection.entry}`);
  }

  if (detection.clientDir) {
    parts.push(`前端: ${detection.clientDir}`);
  }

  return parts.join(" | ");
}
