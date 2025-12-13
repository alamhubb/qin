/**
 * Plugin Runner - 统一管理插件执行
 */

import type { QinPlugin, PluginContext, QinConfig } from "../types";
import chalk from "chalk";
import { createWebPlugin, hasWebClient } from "./web-plugin";

export class PluginRunner {
  private plugins: QinPlugin[];
  private root: string;

  constructor(config: QinConfig, root: string = process.cwd()) {
    this.plugins = [...(config.plugins || [])];
    this.root = root;

    // 自动添加内置 Web 插件（如果有 src/client 或配置了 web）
    const hasExternalVite = this.plugins.some(p => p.name.includes("vite"));
    if (!hasExternalVite) {
      const webPlugin = createWebPlugin(config, root);
      if (webPlugin) {
        this.plugins.push(webPlugin);
      }
    }
  }

  /**
   * 创建插件上下文
   */
  private createContext(isDev: boolean): PluginContext {
    return {
      root: this.root,
      isDev,
      log: (msg: string) => console.log(chalk.cyan(msg)),
    };
  }

  /**
   * 执行所有插件的 dev 钩子
   */
  async runDev(): Promise<void> {
    const ctx = this.createContext(true);

    for (const plugin of this.plugins) {
      if (plugin.dev) {
        console.log(chalk.blue(`→ [${plugin.name}] Starting...`));
        await plugin.dev(ctx);
      }
    }
  }

  /**
   * 执行所有插件的 build 钩子
   */
  async runBuild(): Promise<void> {
    const ctx = this.createContext(false);

    for (const plugin of this.plugins) {
      if (plugin.build) {
        console.log(chalk.blue(`→ [${plugin.name}] Building...`));
        await plugin.build(ctx);
      }
    }
  }

  /**
   * 清理所有插件
   */
  async cleanup(): Promise<void> {
    for (const plugin of this.plugins) {
      if (plugin.cleanup) {
        await plugin.cleanup();
      }
    }
  }

  /**
   * 是否有插件
   */
  hasPlugins(): boolean {
    return this.plugins.length > 0;
  }

  /**
   * 获取插件列表
   */
  getPlugins(): QinPlugin[] {
    return this.plugins;
  }
}
