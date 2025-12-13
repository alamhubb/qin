import { defineConfig } from "qin";

/**
 * java-common - 共享 Java 库
 */
export default defineConfig({
  name: "java-common",
  version: "1.0.0",

  dependencies: {
    "com.google.guava:guava": "32.1.3-jre",
  },
});
