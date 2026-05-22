import { defineConfig } from "../../../src/types";

export default defineConfig({
  name: "hello-java",
  port: 8080,
  entry: "src/server/Main.java",
  java: {
    sourceDir: "src/server",
  },
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6",
    "java-base": "^0.0.1",
  },
});
