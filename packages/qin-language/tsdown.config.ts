import { defineConfig } from 'tsdown'

export default defineConfig({
  entry: {
    'language-server': 'qin-language-server/src/index.ts',
  },
  format: ['cjs'],
  dts: false,
  clean: true,
  outDir: 'dist',
  target: 'es2022',
  external: ['vscode'],
})
