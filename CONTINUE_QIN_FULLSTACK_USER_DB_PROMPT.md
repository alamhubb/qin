# Continue Qin Fullstack User DB Demo

把下面整段提示词复制到另一台电脑的空目录里给 Codex，用来复现当前 qinall 工作区并继续 Qin 全栈用户数据库 demo。

```text
请在一个空目录里继续 Qin 全栈用户数据库 demo，并复现当前进度。

目标：
- 使用 qin 仓库里的 `examples/qin-user-db-fullstack-demo`。
- 这个 demo 是 Qin 单端口 fullstack 项目，端口 `19116`。
- 前端使用 Vue + OVS + CSSTS。
- 后端使用 Qin/TS decorator controller + QinDb + Qono。
- 当前方向是继续实现“前端直接 import 后端 controller，然后 Qin/Qono 编译期 lowering 成 RPC 调用”，最终用户代码不需要手写前端 proxy controller。

1. 如果当前目录还没有仓库，请 clone 为同级目录：

git clone https://gitee.com/alamhubb/qin.git
git clone https://gitee.com/alamhubb/slime.git
git clone https://gitee.com/alamhubb/subhuti.git
git clone https://gitee.com/alamhubb/ovsjs.git
git clone https://gitee.com/alamhubb/cssts.git
git clone https://gitee.com/alamhubb/glogjs.git
git clone https://gitee.com/alamhubb/java-sdk.git

2. 进入 qin 并拉最新：

cd qin
git pull

确认至少包含这些提交：
- `940c2ca Use decorators for frontend user controller proxy`
- 如果本文件来自更新提交，以最新 master 为准。

3. 同步 Qin：

.\qin.bat sync

4. 当前 demo 路径：

examples\qin-user-db-fullstack-demo

当前关键文件：
- `main/controllers/UserController.ts`
  - 后端真实 controller。
  - 使用 `@RestController`、`@RequestMapping("/api/users")`、`@GetMapping("")`、`@PostMapping("")`、`@DeleteMapping("/{id}")`。
  - 方法体里直接调用数据库：
    - `db.selectJson(...)`
    - `db.insertJson(...)`
    - `db.deleteByIdJson(...)`
  - 使用 `Qono.jsonRaw(...)` 返回 HTTP JSON。
- `main/db/schema.ts`
  - 定义 `qin_demo_users` 表。
  - 使用 `QinDb.fromSystemProperties()`。
- `main/qono-class.ts`
  - 后端 decorator helper。
  - `useQonoController(app, UserController)` 从 method decorator 元数据挂载路由。
- `app/controllers/UserController.js`
  - 当前临时前端 proxy controller。
  - 现在也使用 decorator class 写法，并在方法体里调用 `qonoCall(...)`。
  - 这是过渡方案，下一步要用编译期 lowering 去掉这个文件。
- `app/qono-class.js`
  - 当前临时浏览器侧 decorator/qonoCall helper。
  - 这是过渡方案。

5. 启动 demo：

cd examples\qin-user-db-fullstack-demo
..\..\qin.bat sync
..\..\qin.bat dev

打开：

http://127.0.0.1:19116/

6. 数据库配置：

`qin.config.js` 已包含当前测试 PostgreSQL 配置：
- `jdbc:postgresql://43.143.220.49:5432/qin_demo`
- user: `qin_user`
- password: `QinDemo_2026!`

如需覆盖，可设置：

$env:QIN_DEMO_DB_URL = "jdbc:postgresql://<host>:<port>/<database>"
$env:QIN_DEMO_DB_USER = "<user>"
$env:QIN_DEMO_DB_PASSWORD = "<password>"

7. 验证 API：

Invoke-RestMethod http://127.0.0.1:19116/api/health
Invoke-RestMethod http://127.0.0.1:19116/api/users
Invoke-RestMethod -Method Post http://127.0.0.1:19116/api/users -ContentType application/json -Body '{"name":"Ada","email":"ada@example.com"}'
Invoke-RestMethod -Method Delete http://127.0.0.1:19116/api/users/1

8. 已有验证命令：

.\qin.bat run com.qin.runtime.core.QonoAppSmokeTestMain
.\qin.bat run com.qin.runtime.core.QonoControllerDecoratorFullstackSmokeTestMain

demo build-only：

cd examples\qin-user-db-fullstack-demo
..\..\qin.bat dev --build-only

9. 下一步任务，重点：

实现 Qin/Qono 编译期 RPC lowering，使前端可以直接写：

import { UserController } from "../main/controllers/UserController"

await UserController.getAll()
await UserController.create({ name, email })
await UserController.remove({ id })

要求：
- 前端源码看起来就是直接调用后端 controller 方法。
- 实际不能把后端 `java:`、`QinDb`、数据库配置打进浏览器 bundle。
- Qin/Qono 编译期应识别这个 import 是 server controller boundary。
- 前端调用应 lowering 成 fetch：
  - `UserController.getAll()` -> `GET /api/users`
  - `UserController.create(input)` -> `POST /api/users`
  - `UserController.remove({ id })` -> `DELETE /api/users/{id}`
- 完成后删除临时前端 proxy：
  - `app/controllers/UserController.js`
  - `app/qono-class.js`
- 保留后端真实 controller：
  - `main/controllers/UserController.ts`
  - 方法体直接调用数据库。

10. 工程约束：

- 不要提交 `node_modules`、`.qin`、`build`、`logs`、`libs`、`server.out.log`、`server.err.log`、`app/app.js`、`app/@qin-mod` 等运行产物。
- 如果遇到 Qin/Slime/Parser 能力缺口，优先在正确层修复，不要绕过。
- 对 Qin/Java interop，不要加入隐藏属性映射；Java 对象必须调用真实暴露 API。
- 修改后运行 focused smoke。
- 提交并 push qin 仓库，保持 git 状态干净。
```
