# Qin 文档

Qin 是一个现代化的 Java 构建工具，定位为 "Java 的 Vite"。

## 目录

- [快速开始](./getting-started.md) - 5 分钟上手 Qin
- [配置指南](./configuration.md) - qin.config.ts 完整配置说明
- [CLI 命令](./cli.md) - 命令行工具使用
- [依赖管理](./dependencies.md) - Maven 依赖和本地包
- [前端集成](./frontend.md) - Vite 前端开发
- [插件系统](./plugins.md) - 扩展 Qin 功能
- [API 参考](./api.md) - TypeScript API 文档
- [常见问题](./faq.md) - FAQ

## 特性

- 🚀 **零 XML 配置** - TypeScript 配置文件，告别 pom.xml
- ⚡ **极速启动** - Bun + Coursier 高性能组合
- 📦 **一键运行** - `qin run` 编译并运行
- 🎁 **Fat Jar 打包** - `qin build` 生成可执行 JAR
- 🎨 **内置 Vite** - 前端开发零配置
- 🔧 **Monorepo 支持** - 多项目工作区管理

## 快速体验

```bash
# 创建新项目
qin init

# 运行
qin run

# 构建
qin build
```

## 链接

- [GitHub](https://github.com/user/qin)
- [示例项目](../examples)
- [更新日志](./changelog.md)
