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
app/api/
  users-api.js
qin.config.js
```

`app/api/users-api.js` is the shared route contract used by both the Qin/TS backend and the browser:

```js
export const UserApi = {
    getAll: { method: "GET", path: "/api/users" },
    create: { method: "POST", path: "/api/users" },
    delete: { method: "DELETE", path: "/api/users/{id}" }
}
```

`main/main.ts` binds the shared contract to backend handlers and exports a `QinHttpApp` through Qono:

```ts
export const app = Qono.create()
    .health()
    .route(UserApi.getAll.method, UserApi.getAll.path, request => getAll(request))
    .route(UserApi.create.method, UserApi.create.path, request => create(request))
    .route(UserApi.delete.method, UserApi.delete.path, request => remove(request))
    .toHttpApp()
```

The browser calls the backend as direct methods generated from the same contract:

```js
const UserController = createQonoClient(UserApi)

await UserController.getAll()
await UserController.create({ name, email })
await UserController.delete({ id })
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
Invoke-RestMethod http://127.0.0.1:19116/api/users
Invoke-RestMethod -Method Post http://127.0.0.1:19116/api/users -ContentType application/json -Body '{"name":"Ada","email":"ada@example.com"}'
Invoke-RestMethod -Method Delete http://127.0.0.1:19116/api/users/1
```
