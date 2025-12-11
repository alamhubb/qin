/**
 * Frontend Plugin for Qin
 * Integrates Vite for frontend development and building
 * 自动检测前端目录：client/, src/client/
 */

import { join } from "path";
import { mkdir, cp, access, writeFile } from "fs/promises";
import { existsSync } from "fs";
import chalk from "chalk";
import type { QinConfig, FrontendConfig } from "../types";

export interface FrontendPluginOptions {
  cwd?: string;
  debug?: boolean;
}

export class FrontendPlugin {
  private config: QinConfig;
  private frontendConfig: FrontendConfig;
  private cwd: string;
  private debug: boolean;
  private detectedSrcDir: string | null = null;

  constructor(config: QinConfig, options: FrontendPluginOptions = {}) {
    this.config = config;
    this.frontendConfig = config.frontend || {};
    this.cwd = options.cwd || process.cwd();
    this.debug = options.debug || false;
    
    // 自动检测前端目录
    this.detectedSrcDir = this.detectFrontendDir();
  }

  /**
   * 自动检测前端目录
   * 检测顺序：配置值 > client/ > src/client/
   */
  private detectFrontendDir(): string | null {
    // 如果配置了 srcDir，直接使用
    if (this.frontendConfig.srcDir) {
      const configuredDir = join(this.cwd, this.frontendConfig.srcDir);
      if (existsSync(configuredDir)) {
        return this.frontendConfig.srcDir;
      }
    }
    
    // 自动检测
    const candidates = ["client", "src/client"];
    for (const dir of candidates) {
      const fullPath = join(this.cwd, dir);
      if (existsSync(fullPath)) {
        return dir;
      }
    }
    
    return null;
  }

  /**
   * Check if frontend is enabled (auto-detect)
   */
  isEnabled(): boolean {
    return this.detectedSrcDir !== null;
  }

  /**
   * Get frontend source directory
   */
  getSrcDir(): string {
    return join(this.cwd, this.detectedSrcDir || "client");
  }

  /**
   * Get frontend output directory
   */
  getOutDir(): string {
    return join(this.cwd, this.frontendConfig.outDir || "dist/static");
  }

  /**
   * Check if frontend source directory exists
   */
  async hasFrontend(): Promise<boolean> {
    return this.isEnabled();
  }

  /**
   * Build frontend using Vite (or copy static files)
   */
  async build(): Promise<{ success: boolean; error?: string }> {
    if (!this.isEnabled()) {
      return { success: true };
    }

    const srcDir = this.getSrcDir();
    const outDir = this.getOutDir();

    // Check if source directory exists
    if (!(await this.hasFrontend())) {
      console.log(chalk.yellow(`⚠ 前端目录不存在: ${srcDir}`));
      return { success: true };
    }

    console.log(chalk.blue("→ 构建前端资源..."));

    try {
      // Check if Vite is available
      const hasVite = await this.checkVite();

      if (hasVite) {
        // Use Vite to build
        await this.buildWithVite(srcDir, outDir);
      } else {
        // Fallback: copy static files directly
        await this.copyStaticFiles(srcDir, outDir);
      }

      console.log(chalk.green(`✓ 前端构建完成: ${outDir}`));
      return { success: true };
    } catch (error) {
      const message = error instanceof Error ? error.message : "Unknown error";
      console.error(chalk.red(`✗ 前端构建失败: ${message}`));
      return { success: false, error: message };
    }
  }

  /**
   * Check if Vite is available
   */
  private async checkVite(): Promise<boolean> {
    try {
      const proc = Bun.spawn(["npx", "vite", "--version"], {
        cwd: this.cwd,
        stdout: "pipe",
        stderr: "pipe",
      });
      await proc.exited;
      return proc.exitCode === 0;
    } catch {
      return false;
    }
  }

  /**
   * Build frontend with Vite
   */
  private async buildWithVite(srcDir: string, outDir: string): Promise<void> {
    // Create a temporary vite config if needed
    const viteConfigPath = join(this.cwd, "vite.config.frontend.js");
    const viteConfig = `
import { defineConfig } from 'vite';

export default defineConfig({
  root: '${srcDir.replace(/\\/g, "/")}',
  build: {
    outDir: '${outDir.replace(/\\/g, "/")}',
    emptyOutDir: true,
  },
  server: {
    proxy: {
      '/api': 'http://localhost:3000'
    }
  }
});
`;
    await writeFile(viteConfigPath, viteConfig);

    try {
      const proc = Bun.spawn(["npx", "vite", "build", "--config", viteConfigPath], {
        cwd: this.cwd,
        stdout: this.debug ? "inherit" : "pipe",
        stderr: "inherit",
      });

      await proc.exited;

      if (proc.exitCode !== 0) {
        throw new Error("Vite build failed");
      }
    } finally {
      // Clean up temp config
      if (!this.debug) {
        try {
          await Bun.file(viteConfigPath).exists() && 
            await Bun.write(viteConfigPath, ""); // Clear file
        } catch {
          // Ignore cleanup errors
        }
      }
    }
  }

  /**
   * Copy static files directly (fallback when Vite is not available)
   */
  private async copyStaticFiles(srcDir: string, outDir: string): Promise<void> {
    console.log(chalk.gray("  (Vite 不可用，直接复制静态文件)"));
    
    // Ensure output directory exists
    await mkdir(outDir, { recursive: true });

    // Copy all files from srcDir to outDir
    await cp(srcDir, outDir, { recursive: true });
  }

  /**
   * Start Vite dev server
   * 注意：如果后端（如 Spring Boot）已经能服务静态文件，则不需要启动 Vite
   * 返回 null 表示不启动独立的前端服务器
   */
  async startDevServer(): Promise<{ port: number; stop: () => void } | null> {
    // 默认不启动 Vite 开发服务器
    // 因为 Spring Boot 等后端框架可以直接服务静态文件
    // 如果需要 Vite 的热更新功能，可以手动运行 npx vite
    return null;
  }

  /**
   * Generate Vite config for the project
   */
  async generateViteConfig(): Promise<void> {
    const viteConfigPath = join(this.cwd, "vite.config.js");
    
    try {
      await access(viteConfigPath);
      console.log(chalk.yellow("⚠ vite.config.js 已存在，跳过生成"));
      return;
    } catch {
      // File doesn't exist, create it
    }

    const config = `import { defineConfig } from 'vite';

export default defineConfig({
  root: '${this.frontendConfig.srcDir || "client"}',
  build: {
    outDir: '../${this.frontendConfig.outDir || "dist/static"}',
    emptyOutDir: true,
  },
  server: {
    port: ${this.frontendConfig.devPort || 5173},
    proxy: {
      '/api': {
        target: 'http://localhost:3000',
        changeOrigin: true,
      }
    }
  }
});
`;

    await writeFile(viteConfigPath, config);
    console.log(chalk.green("✓ 已生成 vite.config.js"));
  }
}

/**
 * Create frontend plugin instance
 */
export function createFrontendPlugin(
  config: QinConfig,
  options?: FrontendPluginOptions
): FrontendPlugin {
  return new FrontendPlugin(config, options);
}
