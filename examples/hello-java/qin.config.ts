import type { QinConfig } from "../../src/types";

// Qin configuration for hello-java example
const config: QinConfig = {
  // Entry point Java file
  entry: "src/Hello.java",
  
  // Maven dependencies (optional)
  dependencies: [],
  
  // Output configuration
  output: {
    dir: "dist",
    jarName: "hello.jar",
  },
};

export default config;
