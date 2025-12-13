/**
 * qin-plugin-vite
 * Vite frontend integration for Qin build tool
 * 
 * Features:
 * - Vite dev server with HMR
 * - API proxy to backend
 * - Production build
 */

import { join } from "path";
import { existsSync } from "fs";
import { writeFile } from "fs/promises";
// Types will be imported from qin when published
// For now, define locally
interface PluginContext {
  root: string;
  config: any;
  isDev: boolean;
  log: (msg: string) => void;
  warn: (msg: string) => void;
  error: (msg: string) => void;
}

interface QinPlugin {
  name: string;
  config?: (config: any) => any;
  devServer?: (ctx: PluginContext) => Promise<void>;
  beforeBuild?: (ctx: PluginContext) => Promise<void>;
  cleanup?: () => Promise<void>;
}

/**
 * Vite 插件配置
 */
export interface VitePluginOptions {
  /** 前端源码目录，默认 "src/client" */
  root?: string;
  /** 开发服务器端口，默认 5173 */
  port?: number;
  /** API 代理配置 */
  proxy?: Record<string, string>;
  /** 构建输出目录，默认 "dist/static" */
  outDir?: string;
}

/**
 * Vite 进程管理
 */
let viteProcess: ReturnType<typeof Bun.spawn> | null = null;

/**
 * 检测前端目录
 */
function detectClientDir(cwd: string): string | null {
  const candidates = [
    join(cwd, "src", "client"),
    join(cwd, "client"),
    join(cwd, "web"),
    join(cwd, "frontend"),
  ];

  for (const dir of candidates) {
    if (existsSync(dir)) {
      // 检查是否有 index.html 或 package.json
      if (existsSync(join(dir, "index.html")) || existsSync(join(dir, "package.json"))) {
        return dir;
      }
    }
  }
  return null;
}

/**
 * 生成 Vite 配置文件
 */
async function generateViteConfig(
  clientDir: string,
  options: VitePluginOptions,
  backendPort: number
): Promise<string> {
  const port = options.port || 5173;
  const outDir = options.outDir || "dist/static";
  
  // 默认代理 /api 到后端
  const proxy = options.proxy || {
    "/api": `http://localhost:${backendPort}`,
  };

  const proxyConfig = Object.entries(proxy)
    .map(([path, target]) => `      "${path}": { target: "${target}", changeOrigin: true }`)
    .join(",\n");

  const config = `
import { defineConfig } from "vite";

export default defineConfig({
  root: "${clientDir.replace(/\\/g, "/")}",
  server: {
    port: ${port},
    proxy: {
${proxyConfig}
    },
  },
  build: {
    outDir: "${outDir}",
    emptyOutDir: true,
  },
});
`;

  const configPath = join(clientDir, "vite.config.js");
  
  // 只在没有配置文件时生成
  if (!existsSync(configPath) && !existsSync(join(clientDir, "vite.config.ts"))) {
    await writeFile(configPath, config);
  }

  return configPath;
}

/**
 * 启动 Vite 开发服务器
 */
async function startViteDevServer(clientDir: string, options: VitePluginOptions): Promise<void> {
  const port = options.port || 5173;

  // 检查是否安装了 vite（用户需要显式安装）
  const viteExists = existsSync(join(clientDir, "node_modules", ".bin", "vite")) ||
                     existsSync(join(process.cwd(), "node_modules", ".bin", "vite"));

  if (!viteExists) {
    throw new Error(
      "未找到 vite。请先安装：\n" +
      "  bun add -D vite\n" +
      "或在 package.json 的 devDependencies 中添加 vite"
    );
  }

  // 启动 Vite
  viteProcess = Bun.spawn(["bunx", "vite", "--port", String(port)], {
    cwd: clientDir,
    stdout: "inherit",
    stderr: "inherit",
  });

  console.log(`[qin-plugin-vite] Dev server started at http://localhost:${port}`);
}

/**
 * 构建前端
 */
async function buildVite(clientDir: string, options: VitePluginOptions): Promise<void> {
  const outDir = options.outDir || "dist/static";

  const proc = Bun.spawn(["bunx", "vite", "build", "--outDir", outDir], {
    cwd: clientDir,
    stdout: "inherit",
    stderr: "inherit",
  });

  await proc.exited;

  if (proc.exitCode !== 0) {
    throw new Error("Vite build failed");
  }
}

/**
 * 创建 Vite 插件
 */
export function vite(options: VitePluginOptions = {}): QinPlugin {
  let clientDir: string | null = null;

  return {
    name: "qin-plugin-vite",

    async config(config) {
      // 检测前端目录
      const cwd = process.cwd();
      clientDir = options.root ? join(cwd, options.root) : detectClientDir(cwd);

      if (clientDir && existsSync(clientDir)) {
        // 生成 Vite 配置
        const backendPort = config.port || 8080;
        await generateViteConfig(clientDir, options, backendPort);
      }

      return config;
    },

    async devServer(ctx: PluginContext) {
      if (!clientDir || !existsSync(clientDir)) {
        ctx.warn("No frontend directory found, skipping Vite dev server");
        return;
      }

      await startViteDevServer(clientDir, options);
    },

    async beforeBuild(ctx: PluginContext) {
      if (!clientDir || !existsSync(clientDir)) {
        return;
      }

      ctx.log("Building frontend with Vite...");
      await buildVite(clientDir, options);
    },

    async cleanup() {
      if (viteProcess) {
        viteProcess.kill();
        viteProcess = null;
      }
    },
  };
}

export default vite;
