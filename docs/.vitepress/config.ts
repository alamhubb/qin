import { defineConfig } from 'vitepress'

export default defineConfig({
  title: 'Qin',
  description: '基于 Bun 的新一代跨语言构建工具',
  
  head: [
    ['link', { rel: 'icon', href: '/favicon.ico' }],
    // Cloudflare Web Analytics（部署后替换 token）
    // ['script', { defer: '', src: 'https://static.cloudflareinsights.com/beacon.min.js', 'data-cf-beacon': '{"token": "YOUR_TOKEN"}' }],
  ],

  themeConfig: {
    logo: '/logo.svg',
    
    nav: [
      { text: '快速开始', link: '/getting-started' },
      { text: '指南', link: '/guide/' },
      { text: '配置', link: '/config/' },
      { text: 'API', link: '/api/' },
      {
        text: '更多',
        items: [
          { text: '示例', link: '/examples/' },
          { text: '插件', link: '/plugins/' },
          { text: '更新日志', link: '/changelog' },
          { text: 'GitHub', link: 'https://github.com/user/qin' },
        ],
      },
    ],

    sidebar: {
      '/guide/': [
        {
          text: '入门',
          items: [
            { text: '简介', link: '/guide/' },
            { text: '项目结构', link: '/guide/project-structure' },
          ],
        },
        {
          text: '核心功能',
          items: [
            { text: '依赖管理', link: '/guide/dependencies' },
            { text: '前端集成', link: '/guide/frontend' },
            { text: 'Monorepo', link: '/guide/monorepo' },
          ],
        },
        {
          text: '进阶',
          items: [
            { text: '插件系统', link: '/guide/plugins' },
            { text: '构建部署', link: '/guide/build' },
          ],
        },
      ],
      '/config/': [
        {
          text: '配置',
          items: [
            { text: '配置文件', link: '/config/' },
            { text: '项目配置', link: '/config/project' },
            { text: '依赖配置', link: '/config/dependencies' },
            { text: '前端配置', link: '/config/client' },
            { text: '输出配置', link: '/config/output' },
          ],
        },
      ],
      '/api/': [
        {
          text: 'API 参考',
          items: [
            { text: 'CLI 命令', link: '/api/' },
            { text: 'TypeScript API', link: '/api/typescript' },
            { text: '插件 API', link: '/api/plugin' },
          ],
        },
      ],
    },

    socialLinks: [
      { icon: 'github', link: 'https://github.com/user/qin' },
    ],

    footer: {
      message: 'Released under the MIT License.',
      copyright: 'Copyright © 2024 Qin',
    },

    search: {
      provider: 'local',
    },

    outline: {
      level: [2, 3],
      label: '目录',
    },
  },
})
