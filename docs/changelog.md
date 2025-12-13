# 更新日志

所有重要变更记录。

## [Unreleased]

### 新增
- 内置 Vite 前端支持（`client` 配置）
- 插件系统（`QinPlugin` 接口）
- 本地仓库模式（`localRep` 配置）
- Maven 风格依赖存储结构
- 自动 JDK 发现（兼容 Windows）

### 变更
- `web` 配置重命名为 `client`
- 默认前端目录从 `client/` 改为 `src/client/`
- 全局仓库位置改为 `~/.qin/repository/`

### 修复
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
