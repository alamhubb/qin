---
layout: home

hero:
  name: Qin
  text: 新一代跨语言构建工具
  tagline: 用 TypeScript 配置取代 XML，让 Java 开发像前端一样简单
  image:
    src: /logo.svg
    alt: Qin
  actions:
    - theme: brand
      text: 一键安装
      link: '#install'
    - theme: alt
      text: 快速开始
      link: /getting-started
    - theme: alt
      text: GitHub
      link: https://github.com/user/qin

features:
  - icon: ⚡
    title: 极速体验
    details: 基于 Bun 运行时，依赖解析和构建速度极快，告别漫长等待
  - icon: 🚀
    title: 零配置启动
    details: 自动检测项目类型，无需 qin.config.ts 也能直接运行
  - icon: 📝
    title: TypeScript 配置
    details: 告别 XML，用熟悉的 TypeScript 编写配置，享受类型提示
  - icon: 🎨
    title: 全栈开发
    details: 内置 Vite，自动检测前端代码，一个命令启动全栈开发环境
  - icon: 🔌
    title: 插件生态
    details: Vite 风格的插件系统，Spring Boot、热重载等开箱即用
  - icon: 🌍
    title: 跨语言
    details: 不止 Java，未来支持 Kotlin、Scala 等更多 JVM 语言
---

<script setup>
import { ref } from 'vue'

const os = ref('unix')
if (typeof navigator !== 'undefined') {
  os.value = navigator.platform.toLowerCase().includes('win') ? 'windows' : 'unix'
}
</script>

<div id="install" class="install-section">

## 安装

<div class="install-tabs">
  <button :class="{ active: os === 'unix' }" @click="os = 'unix'">macOS / Linux</button>
  <button :class="{ active: os === 'windows' }" @click="os = 'windows'">Windows</button>
</div>

<div v-if="os === 'unix'" class="install-code">

```bash
curl -fsSL https://qinjs.dev/install.sh | bash
```

</div>

<div v-if="os === 'windows'" class="install-code">

```powershell
irm https://qinjs.dev/install.ps1 | iex
```

</div>

<p class="install-note">自动安装 Bun（如果未安装）和 Qin CLI</p>

</div>

## 30 秒上手

```bash
# 创建项目
qin create my-app

# 进入目录
cd my-app

# 启动开发
qin dev
```

## 配置示例

```ts
// qin.config.ts - 可选，零配置也能运行
import { defineConfig } from "qin";

export default defineConfig({
  name: "my-app",
  
  dependencies: {
    "org.springframework.boot:spring-boot-starter-web": "4.0.6",
  },
});
```

<style>
.install-section {
  max-width: 600px;
  margin: 2rem auto;
  padding: 0 1rem;
}

.install-tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.install-tabs button {
  padding: 0.5rem 1rem;
  border: 1px solid var(--vp-c-divider);
  border-radius: 6px;
  background: var(--vp-c-bg-soft);
  cursor: pointer;
  transition: all 0.2s;
}

.install-tabs button.active {
  background: var(--vp-c-brand);
  color: white;
  border-color: var(--vp-c-brand);
}

.install-note {
  font-size: 0.9rem;
  color: var(--vp-c-text-2);
  text-align: center;
}
</style>
