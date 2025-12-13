# create-qin

快速创建 Qin 项目的脚手架工具。

## 使用

```bash
# npm
npm create qin@latest

# bun
bun create qin

# 或直接指定项目名
npm create qin@latest my-app
```

## 模板

- **java** - Java 后端项目 (Spring Boot)
- **fullstack** - 全栈项目 (Java + Vite)
- **minimal** - 最小项目 (纯配置)

## 生成的项目结构

### Java 模板

```
my-app/
├── qin.config.ts
├── src/
│   └── server/
│       └── Main.java
└── .gitignore
```

### Fullstack 模板

```
my-app/
├── qin.config.ts
├── src/
│   ├── server/
│   │   └── Main.java
│   └── client/
│       ├── index.html
│       └── main.ts
└── .gitignore
```

## 开发

```bash
cd packages/create-qin
bun install
bun run dev
```

## 发布

```bash
bun run build
npm publish
```
