import { defineConfig } from "qin";

export default defineConfig({
  name: "{{name}}",
  port: 8080,

  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6",
  },

  client: {
    root: "src/client",
    port: 5173,
  },
});
