# QinWeb Hello

最小 QinWeb 示例，适合用 IDEA Qin 插件打开并从右侧 Qin 面板启动。

## 启动

1. 用 IDEA 打开 `examples/apps/qin-hello-web`。
2. 右侧 Qin 面板会读取 `qin.config.js`，显示 `dev`、`start`、`check` 脚本。
3. 点击 `dev` 或运行：

```powershell
..\..\..\qin.bat script dev
```

启动后访问：

```text
http://127.0.0.1:19131/
```

返回：

```text
hello
```
