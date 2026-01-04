# 更新日志

所有重要变更记录。

## [Unreleased]

### 新增
- **IDEA 插件自动同步** - 打开项目时自动执行 `qin sync`
- **IDEA 库配置生成** - 自动生成 `.idea/libraries/*.xml`，IDEA 自动识别依赖
- **IDEA 编译输出配置** - 自动配置 IDEA 使用 `build/classes` 目录
- **Monorepo 支持增强** - IDEA 插件自动扫描所有子项目（最多 3 层深度）
- **缓存文件验证** - 使用缓存前验证所有 jar 文件是否存在
- 内置 Vite 前端支持（`client` 配置）
- 插件系统（`QinPlugin` 接口）
- 本地仓库模式（`localRep` 配置）
- Maven 风格依赖存储结构
- 自动 JDK 发现（兼容 Windows）

### 变更
- `web` 配置重命名为 `client`
- 默认前端目录从 `client/` 改为 `src/client/`
- 全局仓库位置改为 `~/.qin/repository/`
- **Windows Junction** - 使用 Junction 替代 Symlink（无需管理员权限）

### 修复
- **乱码问题** - Windows 控制台中文输出乱码（通过 `chcp 65001` 和 UTF-8 编码）
- **路径格式** - IDEA 库配置中的 jar:// URL 路径格式
- Windows 下 `jar` 命令找不到的问题
- 依赖解析路径问题

## [0.1.0] - 2024-12-01

### 新增
- 初始版本
- TypeScript 配置文件支持
- Maven 依赖解析（Coursier）
- Java 编译和运行
- Fat Jar 构建
- Monorepo 支持
- 本地包引用

---

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。
