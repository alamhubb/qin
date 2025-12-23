/**
 * Qin Configuration for GraalVM JavaScript Test Project
 * 
 * This project demonstrates GraalVM Node.js support in Qin.
 * 
 * Prerequisites:
 * 1. Install GraalVM: https://www.graalvm.org/downloads/
 * 2. Set GRAALVM_HOME environment variable
 * 3. Install Node.js component: gu install nodejs
 */

import { defineConfig } from "qin";
// import { graalvmJs } from "qin-plugin-graalvm-js";

export default defineConfig({
  name: "graalvm-js-test",
  version: "0.1.0",
  description: "Test project for GraalVM JavaScript support",

  // GraalVM JavaScript configuration
  graalvm: {
    js: {
      entry: "src/server/index.js",
      hotReload: true,
      javaInterop: false, // Set to true to enable Java interop
    },
  },

  // Uncomment to use plugin directly:
  // plugins: [
  //   graalvmJs({
  //     entry: "src/server/index.js",
  //     hotReload: true,
  //   }),
  // ],
});
