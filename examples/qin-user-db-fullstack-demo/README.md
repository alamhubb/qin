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
  qono-class.js
  controllers/UserController.js
  UserRuntimeBadge.ovs
  tokens.cssts
main/
  main.ts
  controllers/UserController.ts
  db/schema.ts
  qono-class.ts
qin.config.js
```

`main/controllers/UserController.ts` uses the decorator controller authoring style:

```ts
@RestController
@RequestMapping("/api/users")
export class UserController {
    static basePath = "/api/users"

    @GetMapping("")
    static getAll(request) {
        return Qono.jsonRaw(db.selectJson("users", users, "id", "asc"))
    }

    @PostMapping("")
    static create(request) {
        return Qono.jsonRaw(201, db.insertJson("user", users, request.bodyText(), "name,email"))
    }

    @DeleteMapping("/{id}")
    static remove(request) {
        return Qono.jsonRaw(db.deleteByIdJson(users, request.param("id")))
    }
}
```

`main/main.ts` mounts the controller and exports a `QinHttpApp` through Qono:

```ts
export const app = useQonoController(Qono.create()
    .health()
    , UserController)
    .toHttpApp()
```

The browser uses the same route metadata shape, but applies the decorator functions explicitly so the client proxy remains native browser ESM:

```js
export class UserController {
    static basePath = "/api/users"

    static getAll() {
        return qonoCall(UserController, "getAll")
    }

    static create(input) {
        return qonoCall(UserController, "create", input)
    }

    static remove(input) {
        return qonoCall(UserController, "remove", input)
    }
}

RestController(UserController)
RequestMapping("/api/users")(UserController)
GetMapping("")(UserController, "getAll", Object.getOwnPropertyDescriptor(UserController, "getAll"))
PostMapping("")(UserController, "create", Object.getOwnPropertyDescriptor(UserController, "create"))
DeleteMapping("/{id}")(UserController, "remove", Object.getOwnPropertyDescriptor(UserController, "remove"))

await UserController.getAll()
await UserController.create({ name, email })
await UserController.remove({ id })
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
