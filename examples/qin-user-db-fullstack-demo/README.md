# Qin User DB Fullstack Demo

This demo is a single-port Qin fullstack app:

- Frontend: Vue plus local OVS and CSSTS source modules.
- Backend authoring: Qin/TS modules under `main/`.
- HTTP runtime: `Qono`, a Qin-owned lightweight RPC layer.
- Database runtime: `QinDb`, a Qin-owned PostgreSQL helper with schema and query builders.

## Structure

```text
app/
  main.js
  qono-client.js
  UserRuntimeBadge.ovs
  tokens.cssts
main/
  main.ts
  controllers/UserController.ts
  db/schema.ts
  db/queries.ts
qin.config.js
```

`main/main.ts` exports a `QinHttpApp` through Qono:

```ts
export const app = Qono.create()
    .health()
    .query("users.getAll", request => getAll(request))
    .mutation("users.create", request => create(request))
    .mutation("users.delete", request => remove(request))
    .toHttpApp()
```

The browser calls the backend through `app/qono-client.js`:

```js
await rpc.call("users.getAll")
await rpc.call("users.create", { name, email })
await rpc.call("users.delete", { id })
```

The database schema lives in `main/db/schema.ts`:

```ts
export const users = QinDb.table("qin_demo_users")
users.bigserial("id").primaryKey()
users.text("name").notNull()
users.text("email").notNull().unique()
users.timestamptz("created_at").notNull().defaultNow()
```

## Run

```powershell
..\..\qin.bat sync
..\..\qin.bat dev
```

Open:

```text
http://127.0.0.1:19116/
```

The checked-in `qin.config.js` contains the private test PostgreSQL connection for this demo. Environment variables can still override it:

```powershell
$env:QIN_DEMO_DB_URL = "jdbc:postgresql://<host>:<port>/<database>"
$env:QIN_DEMO_DB_USER = "<user>"
$env:QIN_DEMO_DB_PASSWORD = "<password>"
```

## API

```powershell
Invoke-RestMethod http://127.0.0.1:19116/api/health
Invoke-RestMethod -Method Post http://127.0.0.1:19116/api/rpc/users.getAll -ContentType application/json -Body "{}"
Invoke-RestMethod -Method Post http://127.0.0.1:19116/api/rpc/users.create -ContentType application/json -Body '{"name":"Ada","email":"ada@example.com"}'
Invoke-RestMethod -Method Post http://127.0.0.1:19116/api/rpc/users.delete -ContentType application/json -Body '{"id":1}'
```
