import type { QinConfig } from "../src/types.ts";

/**
 * Qin Workspace 配置
 * Monorepo 多项目管理（不需要 entry）
 */
const config: QinConfig = {
  name: "qin-examples",

  // 多项目配置
  packages: [
    "apps/*",
    "packages/*",
  ],
};

export default config;
