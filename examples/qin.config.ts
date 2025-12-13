import { defineConfig } from "../src/types";

/**
 * Qin Workspace 配置
 * Monorepo 多项目管理
 */
export default defineConfig({
  name: "qin-examples",

  // 多项目配置
  packages: ["apps/*", "packages/*"],
});
