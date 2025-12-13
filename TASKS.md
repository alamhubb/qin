# Qin 开发任务清单

## P0 - 核心功能（必须）

### 1. 资源文件处理
- [x] 支持 `src/resources/` 目录
- [x] 编译时复制资源文件到 classes 目录
- [x] 打包时将资源文件包含进 JAR
- [x] 支持 `application.yml`, `application.properties` 等 Spring Boot 配置

### 2. 测试支持 `qin test`
- [x] 添加 `qin test` 命令
- [x] 支持 JUnit 5 测试框架
- [x] 自动发现 `src/test/` 或 `tests/` 目录下的测试类
- [x] 运行测试并输出结果
- [x] 支持测试过滤 `qin test --filter UserTest`

## P1 - 开发体验

### 3. Java 热重载
- [x] 监听 Java 文件变化
- [x] 自动重新编译
- [x] 自动重启 Java 进程
- [x] 保持 Vite 前端不受影响

### 4. `qin init` 命令
- [x] CLI 内置项目初始化
- [x] 交互式选择语言和模板
- [x] 不依赖 `create-qin` 包

### 5. 错误信息优化
- [x] 编译错误格式化输出
- [x] 显示错误文件和行号
- [x] 彩色高亮错误信息

## P2 - 完善功能

### 6. 依赖管理增强
- [ ] `qin deps` 查看依赖树
- [ ] `qin deps --why <package>` 查看为什么引入某个依赖
- [ ] 依赖冲突检测和警告

### 7. 增量编译
- [ ] 记录文件修改时间
- [ ] 只编译修改过的 Java 文件
- [ ] 处理依赖关系（A 依赖 B，B 改了 A 也要重编译）

### 8. 环境配置
- [ ] 支持 `qin.config.ts` 中的 `env` 配置
- [ ] `qin run --env production`
- [ ] 环境变量注入

## P3 - 进阶功能

### 9. 发布功能
- [ ] `qin publish` 发布到 Maven 仓库
- [ ] 生成 pom.xml
- [ ] 支持私有仓库认证

### 10. 注解处理器
- [ ] 支持 Lombok
- [ ] 支持 MapStruct
- [ ] 配置 annotation processor

### 11. 多语言支持
- [ ] Bun/Node 项目模板实现
- [ ] Kotlin 支持
- [ ] Scala 支持

---

## 当前进度

- [x] 基础编译和运行
- [x] Maven 依赖解析 (Coursier)
- [x] Fat JAR 打包
- [x] Vite 前端集成
- [x] Monorepo 支持
- [x] 本地包引用
- [x] 全局/本地仓库
- [x] create-qin 脚手架
- [x] VitePress 文档站点
- [x] 资源文件处理 (P0-1)
- [x] 测试支持 `qin test` (P0-2)
- [x] 插件系统架构设计
- [x] 零配置自动检测
- [x] Java 热重载 (P1-3)
- [x] `qin init` 交互式初始化 (P1-4)
- [x] 错误信息优化 (P1-5)

## 插件系统

### 核心插件接口
- [x] `src/core/plugin-system.ts` - 插件管理器和类型定义
- [x] `PluginManager` - 插件注册、钩子调度
- [x] `LanguageSupport` - 语言插件接口
- [x] 生命周期钩子: config, beforeCompile, afterCompile, beforeBuild, afterBuild, devServer, cleanup

### 官方插件
- [x] `qin-plugin-java` - Java 语言支持 (packages/qin-plugin-java)
- [x] `qin-plugin-java-hot-reload` - Java 热重载 (packages/qin-plugin-java-hot-reload)
- [x] `qin-plugin-vite` - Vite 前端集成 (packages/qin-plugin-vite)
- [x] `qin-plugin-spring` - Spring Boot 支持 (packages/qin-plugin-spring)
- [ ] `qin-plugin-kotlin` - Kotlin 语言支持
- [ ] `qin-plugin-lombok` - Lombok 注解处理

## 下一步

1. P2 功能：依赖管理增强、增量编译、环境配置
2. 更多插件：kotlin、lombok、spring-boot
