# QinWeb Hello

最小 QinWeb 全栈示例，使用 `src/app.qin` 作为唯一入口。前端启动代码和后端 `@WebRoot + controllers` 清单都在这个文件里，不需要 `frontend.entry` 或 `main/main.ts`。

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
http://127.0.0.1:19131/api/hello
```

两个地址都会返回：

```text
hello
```
