# QinWeb

QinWeb is Qin's lightweight HTTP and RPC layer.

It is designed as a small Qin-native framework rather than a wrapper around a
large Java web stack. The public API should feel close to Hono: direct routes,
middleware-friendly composition, and generated RPC clients. The implementation
should stay friendly to GraalVM Native Image: static route metadata, no required
runtime classpath scanning, minimal reflection, and explicit JSON boundaries.

## Package

```js
dependencies: {
    "com.qin:qin-web": "0.1.0"
}
```

Server Qin/TS code imports QinWeb from Java:

```ts
import { QinWeb } from "java:com.qin.web"
```

## Basic App

```ts
import { QinWeb } from "java:com.qin.web"

export const app = QinWeb.create()
    .health()
    .get("/api/users", request => QinWeb.jsonRaw("{\"users\":[]}"))
    .post("/api/users", request => QinWeb.jsonRaw(201, request.bodyText()))
    .toHttpApp()
```

## RPC

```ts
export const app = QinWeb.create()
    .query("UserController.getAll", request => QinWeb.jsonRaw("{\"users\":[]}"))
    .mutation("UserController.create", request => QinWeb.jsonRaw(201, request.bodyText()))
    .toHttpApp()
```

Qin fullstack can generate browser RPC clients from server controller imports,
so frontend code can call server controllers like normal objects:

```js
import { UserController } from "../main/controllers/UserController.qin"

await UserController.getAll()
await UserController.create({ name })
```

## Native Image Direction

QinWeb should keep the runtime small and predictable:

- Generate controller route tables at Qin compile time.
- Generate browser RPC clients inside Qin instead of checking in proxy files.
- Avoid runtime classpath scanning as a required route-discovery mechanism.
- Avoid required dynamic proxies and reflection-heavy JSON serialization.
- Keep `QinHttpApp` as the transport/runtime boundary and let QinWeb provide the
  ergonomic API above it.

