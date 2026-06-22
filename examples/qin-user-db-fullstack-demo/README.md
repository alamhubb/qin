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
  UserRuntimeBadge.ovs
  tokens.cssts
main/
  main.ts
  controllers/UserController.qin
  db/schema.ts
  qono-class.ts
qin.config.js
```

`main/controllers/UserController.qin` uses the Qin `object` singleton controller style:

```ts
@RestController
@RequestMapping("/api/users")
export object UserController {
    controllerName = "UserController"
    basePath = "/api/users"
    dbRef = null
    usersRef = null

    @GetMapping("")
    getAll(request) {
        return Qono.jsonRaw(this.dbRef.selectJson("users", this.usersRef, "id", "asc"))
    }

    @PostMapping("")
    create(request) {
        return Qono.jsonRaw(201, this.dbRef.insertJson("user", this.usersRef, request.bodyText(), "name"))
    }

    @DeleteMapping("/{id}")
    remove(request) {
        return Qono.jsonRaw(this.dbRef.deleteByIdJson(this.usersRef, request.param("id")))
    }
}

UserController.setDbRef(db)
UserController.setUsersRef(users)
```

`main/main.ts` mounts the controller and exports a `QinHttpApp` through Qono:

```ts
export const app = useQonoController(Qono.create()
    .health()
    , UserController)
    .toHttpApp()
```

The browser calls the mounted Qono REST routes directly from `app/main.js`.
The browser-facing client controller calls the same backend through Qono RPC:

```js
import { UserController } from "../main/controllers/UserController.qin"

await UserController.getAll()
await UserController.create({ name })
await UserController.remove({ id })
```

Qin generates the browser RPC client internally for that server-controller import, so page code talks to
`UserController` like a normal object without a checked-in proxy file.
The server-side controller remains the single source of route behavior.

The database schema lives in `main/db/schema.ts`:

```ts
export const users = QinDb.table("qin_demo_user_names")
users.bigserial("id").primaryKey()
users.text("name").notNull()
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
Invoke-RestMethod -Method Post http://127.0.0.1:19116/api/users -ContentType application/json -Body '{"name":"Ada"}'
Invoke-RestMethod -Method Delete http://127.0.0.1:19116/api/users/1
```
