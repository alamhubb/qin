# Qin Dependency Sync Hook

当 `qin.config.ts` 文件保存时，自动同步 Java 依赖。

## 触发条件
- 文件保存事件
- 匹配 `**/qin.config.ts`

## 执行动作
运行 `bun run src/cli.ts sync` 同步依赖到本地缓存。

---

```yaml
name: qin-sync
description: 自动同步 Qin 项目依赖
trigger:
  type: onFileSave
  pattern: "**/qin.config.ts"
action:
  type: command
  command: "bun run src/cli.ts sync"
```
