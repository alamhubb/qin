/**
 * 内置 Web 插件 - 自动检测 src/client 并启用 Vite
 */

import { existsSync } from "fs";
import { join } from "path";
import chalk from "chalk";
import type { QinConfig, QinPlugin, PluginContext } from "../types";

/**
 * 检测是否有前端代码
 */
export function hasWebClient(cwd: string, config: QinConfig): boolean {
  const clientDir = config.client?.root || "src/client";
  const fullPath = join(cwd, clientDir);
  
  if (!existsSync(fullPath)) return false;
  
  // 检查是否有 index.html
  const indexPath = join(fullPath, "index.html");
  return existsSync(indexPath);
}

/**
 * 创建内置 Web 插件
 */
export function createWebPlugin(config: QinConfig, cwd: string = process.cwd()): QinPlugin | null {
  if (!hasWebClient(cwd, config) && !config.client) {
    return null;
  }

  const clientDir = config.client?.root || "src/client";
  const clientPort = config.client?.port || 5173;
  const backendPort = config.port || 8080;
  const outDir = config.client?.outDir || "dist/static";
  
  // 默认代理配置：/api -> 后端
  const defaultProxy = { "/api": `http://localhost:${backendPort}` };
  const proxy = config.client?.proxy || defaultProxy;

  let server: any = null;

  return {
    name: "qin-web",

    async dev(ctx: PluginContext) {
      const { createServer } = await import("vite");

      ctx.log(`[web] 启动前端开发服务器 (端口 ${clientPort})...`);

      const viteConfig = {
        root: join(ctx.root, clientDir),
        server: {
          port: clientPort,
          proxy: normalizeProxy(proxy),
        },
      };

      server = await createServer(viteConfig);
      await server.listen();

      ctx.log(`[web] 前端服务器: http://localhost:${clientPort}`);
    },

    async build(ctx: PluginContext) {
      const { build: viteBuild } = await import("vite");

      ctx.log(`[web] 构建前端...`);

      const viteConfig = {
        root: join(ctx.root, clientDir),
        build: {
          outDir: join(ctx.root, outDir),
          emptyOutDir: true,
        },
      };

      await viteBuild(viteConfig);

      ctx.log(`[web] 前端构建完成: ${outDir}`);
    },

    async cleanup() {
      if (server) {
        await server.close();
        server = null;
      }
    },
  };
}

/**
 * 标准化代理配置
 */
function normalizeProxy(proxy: Record<string, string>): Record<string, any> {
  const result: Record<string, any> = {};

  for (const [path, target] of Object.entries(proxy)) {
    result[path] = {
      target,
      changeOrigin: true,
    };
  }

  return result;
}

/**
 * 获取 Client 配置信息（用于显示）
 */
export function getClientInfo(config: QinConfig): {
  root: string;
  port: number;
  backendPort: number;
  proxy: Record<string, string>;
} {
  const backendPort = config.port || 8080;
  return {
    root: config.client?.root || "src/client",
    port: config.client?.port || 5173,
    backendPort,
    proxy: config.client?.proxy || { "/api": `http://localhost:${backendPort}` },
  };
}
