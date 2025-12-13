import { defineConfig } from "qin";

/**
 * {{name}} - Monorepo 工作区
 */
export default defineConfig({
  name: "{{name}}",

  packages: [
    "apps/*",
    "packages/*",
  ],
});
