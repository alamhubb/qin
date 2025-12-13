# qin-plugin-vite

Vite integration plugin for Qin build tool.

## Installation

```bash
npm install qin-plugin-vite vite
```

## Usage

```ts
// qin.config.ts
import { defineConfig } from "qin";
import vite from "qin-plugin-vite";

export default defineConfig({
  name: "my-fullstack-app",
  plugins: [
    vite({
      port: 5173,
      root: "client",
      proxy: {
        "/api": "http://localhost:8080",
      },
    }),
  ],
});
```

## Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `port` | `number` | `5173` | Dev server port |
| `root` | `string` | `"client"` | Frontend source directory |
| `outDir` | `string` | `"dist/static"` | Build output directory |
| `proxy` | `Record<string, string \| ProxyOptions>` | - | Proxy configuration |
| `viteConfig` | `InlineConfig` | - | Custom Vite config |

## Project Structure

```
my-app/
├── qin.config.ts
├── src/
│   └── Main.java        # Java backend
└── client/
    ├── index.html
    └── src/
        └── main.ts      # Frontend entry
```
