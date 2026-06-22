# 余额监控系统

Qin 单端口全栈示例，用 OVS 前端和 Qono 后端监控 xixiapi 数据库里的 apikey 账户余额。

## Run

```powershell
$env:XIXIAPI_DB_URL = "jdbc:postgresql://<host>:<port>/<database>"
$env:XIXIAPI_DB_USER = "<user>"
$env:XIXIAPI_DB_PASSWORD = "<password>"

..\..\qin.bat sync
..\..\qin.bat dev
```

Open:

```text
http://127.0.0.1:19117/
```

## Database Defaults

默认 SQL：

```sql
select id as id, name as name, domain as domain, url as base_url, api_key as api_key
from xixi_accounts
where type = 'apikey'
order by domain, name
```

可通过环境变量适配你的 xixiapi 表结构：

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

如果表结构差异比较大，可以直接覆盖 SQL。自定义 SQL 必须返回这些别名：
`id`, `name`, `domain`, `base_url`, `api_key`。

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
