# 余额监控系统

Qin 单端口全栈示例：用 OVS 前端和 Qono 后端读取 xixiapi/sub2api 数据库里的 `apikey` 类型账户，并按账户的 URL + key 探测余额。

## Run

```powershell
$env:XIXIAPI_DB_URL = "jdbc:postgresql://<host>:<port>/<database>"
$env:XIXIAPI_DB_USER = "<user>"
$env:XIXIAPI_DB_PASSWORD = "<password>"

..\..\qin.bat sync
..\..\qin.bat dev
```

也可以放到 `.env.local`，或者直接复用服务器常见变量名：

```powershell
$env:DB_HOST = "<host>"
$env:DB_PORT = "5432"
$env:DB_NAME = "sub2api"
$env:DB_USER = "sub2api"
$env:DB_PASSWORD = "<password>"
```

打开：

```text
http://127.0.0.1:19117/
```

## UI Direction

本 demo 采用这条 Qin UI 演进路线：

```text
Reka UI primitives
  -> shadcn-vue component style
  -> Qin UI components
```

当前没有直接引入完整 Tailwind 构建链，而是在 `app/qin-ui.js` 中先沉淀一层轻量组件：`AppShell`, `Button`, `Card`, `StatCard`, `Alert`, `Badge`, `DataTable`。视觉结构参考 `shadcn-vue-admin` 这类 Vue admin dashboard，后续可以把这些组件替换为真正基于 Reka UI/shadcn-vue 的实现。

## Database Defaults

默认按 lucen/sub2api 的 `accounts` 表读取：

```sql
select id::text as id,
       name,
       credentials->>'base_url' as base_url,
       credentials->>'api_key' as api_key
from accounts
where type = 'apikey' and coalesce(status, 'active') = 'active'
order by domain, name
```

也支持平铺表结构。可以通过环境变量覆盖表和字段：

```powershell
$env:XIXIAPI_ACCOUNT_TABLE = "xixi_accounts"
$env:XIXIAPI_ACCOUNT_TYPE_COLUMN = "type"
$env:XIXIAPI_ACCOUNT_TYPE_VALUE = "apikey"
$env:XIXIAPI_ACCOUNT_ID_COLUMN = "id"
$env:XIXIAPI_ACCOUNT_NAME_COLUMN = "name"
$env:XIXIAPI_ACCOUNT_DOMAIN_COLUMN = "domain"
$env:XIXIAPI_ACCOUNT_URL_COLUMN = "url"
$env:XIXIAPI_ACCOUNT_KEY_COLUMN = "api_key"
```

如果表结构差异比较大，可以直接覆盖 SQL。自定义 SQL 必须返回这些别名：`id`, `name`, `domain`, `base_url`, `api_key`。

```powershell
$env:XIXIAPI_ACCOUNT_SQL = "select id, name, domain, base_url, key as api_key from accounts where kind = 'apikey'"
```

## Balance Probe

默认依次尝试这些路径：

```text
/dashboard/billing/credit_grants
/v1/dashboard/billing/credit_grants
/v1/user/balance
/api/user/self
```

可覆盖：

```powershell
$env:XIXIAPI_BALANCE_PATHS = "/dashboard/billing/credit_grants,/v1/user/balance"
```

每个请求会同时带：

```text
Authorization: Bearer <api_key>
X-API-Key: <api_key>
```
