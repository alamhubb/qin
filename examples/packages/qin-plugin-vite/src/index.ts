/**
 * qin-plugin-vite
 * Vite integration plugin for Qin build tool
 */

import type { ViteDevServer, InlineConfig, ProxyOptions } from "vite";

/**
 * Vite 插件配置
 */
export interface VitePluginOptions {
  /** Vite 开发服务器端口，默认 5173 */
  port?: number;
  /** 代理配置 */
  proxy?: Record<string, string | ProxyOptions>;
  /** 前端源码目录，默认 "client" */
  root?: string;
  /** 构建输出目录，默认 "dist/static" */
  outDir?: string;
  /** 自定义 Vite 配置 */
  viteConfig?: InlineConfig;
}

/**
 * Qin 插件接口
 */
export interface QinPlugin {
  name: string;
  /** 开发模式启动 */
  dev?: (ctx: PluginContext) => Promise<void>;
  /** 构建 */
  build?: (ctx: PluginContext) => Promise<void>;
  /** 清理 */
  cleanup?: () => Promise<void>;
}

/**
 * 插件上下文
 */
export interface PluginContext {
  /** 项目根目录 */
  root: string;
  /** 是否开发模式 */
  isDev: boolean;
  /** 日志函数 */
  log: (msg: string) => void;
}

/**
 * 创建 Vite 插件
 */
export function vite(options: VitePluginOptions = {}): QinPlugin {
  const {
    port = 5173,
    proxy,
    root = "client",
    outDir = "dist/static",
    viteConfig = {},
  } = options;

  let server: ViteDevServer | null = null;

  return {
    name: "qin-plugin-vite",

    async dev(ctx) {
      const { createServer } = await import("vite");

      ctx.log(`[vite] Starting dev server on port ${port}...`);

      const config: InlineConfig = {
        root: `${ctx.root}/${root}`,
        server: {
          port,
          proxy: proxy ? normalizeProxy(proxy) : undefined,
        },
        ...viteConfig,
      };

      server = await createServer(config);
      await server.listen();

      ctx.log(`[vite] Dev server running at http://localhost:${port}`);
    },

    async build(ctx) {
      const { build: viteBuild } = await import("vite");

      ctx.log(`[vite] Building frontend...`);

      const config: InlineConfig = {
        root: `${ctx.root}/${root}`,
        build: {
          outDir: `${ctx.root}/${outDir}`,
          emptyOutDir: true,
        },
        ...viteConfig,
      };

      await viteBuild(config);

      ctx.log(`[vite] Frontend built to ${outDir}`);
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
function normalizeProxy(
  proxy: Record<string, string | ProxyOptions>
): Record<string, ProxyOptions> {
  const result: Record<string, ProxyOptions> = {};

  for (const [path, config] of Object.entries(proxy)) {
    if (typeof config === "string") {
      result[path] = {
        target: config,
        changeOrigin: true,
      };
    } else {
      result[path] = config;
    }
  }

  return result;
}

export default vite;
