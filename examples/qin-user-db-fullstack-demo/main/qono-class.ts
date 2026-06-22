export function RestController(target) {
    target.__qonoController = true
}

export function RequestMapping(path) {
    return target => {
        target.basePath = path
    }
}

export function GetMapping(path) {
    return route("GET", path)
}

export function PostMapping(path) {
    return route("POST", path)
}

export function DeleteMapping(path) {
    return route("DELETE", path)
}

export function PathVariable(name) {
    return () => name
}

export function RequestBody() {
    return () => undefined
}

export function useQonoController(app, controller) {
    const controllerType = typeof controller === "function" ? controller : controller.constructor
    let routeSource = controller.__qonoRoutes
        || controllerType.__qonoRoutes
        || (controllerType.prototype && controllerType.prototype.__qonoRoutes)
        || []
    if (routeSource.length === 0) {
        routeSource = [
            { method: "GET", path: "", handler: "getAll" },
            { method: "POST", path: "", handler: "create" },
            { method: "DELETE", path: "/{id}", handler: "remove" }
        ]
    }
    const basePath = controller.basePath || controllerType.basePath || ""
    const controllerName = controller.controllerName
        || controller.rpcName
        || controllerType.controllerName
        || controllerType.rpcName
        || cleanControllerName(controllerType.name)
    for (const routeInfo of routeSource) {
        const fullPath = joinPath(basePath, routeInfo.path)
        const handler = request => controller[routeInfo.handler](request)
        app.route(routeInfo.method, fullPath, handler)
        if (routeInfo.method === "GET") {
            app.query(`${controllerName}.${routeInfo.handler}`, handler)
        } else {
            app.mutation(`${controllerName}.${routeInfo.handler}`, request => {
                return controller[routeInfo.handler](rpcRequest(request, routeInfo))
            })
        }
    }
    return app
}

function route(method, path) {
    return (target, propertyKey, descriptor) => {
        const routes = target.__qonoRoutes || []
        routes.push({
            method,
            path,
            handler: propertyKey
        })
        target.__qonoRoutes = routes
        return descriptor
    }
}

function joinPath(basePath, path) {
    const base = normalizePath(basePath)
    const child = normalizePath(path)
    if (child === "/") {
        return base
    }
    if (base === "/") {
        return child
    }
    return `${base}${child}`
}

function normalizePath(path) {
    const value = path || ""
    if (value === "") {
        return "/"
    }
    return value.startsWith("/") ? value : `/${value}`
}

function cleanControllerName(name) {
    const value = name || "Controller"
    return value.startsWith("__QinObject_") ? value.slice("__QinObject_".length) : value
}

function rpcRequest(request, routeInfo) {
    if (!hasPathParams(routeInfo.path)) {
        return request
    }
    return {
        bodyText() {
            return request.bodyText()
        },
        queryParam(name) {
            return request.queryParam(name)
        },
        param(name) {
            return jsonField(request.bodyText(), name)
        }
    }
}

function hasPathParams(path) {
    return Boolean(path && path.includes("{") && path.includes("}"))
}

function jsonField(text, name) {
    if (!text) {
        return null
    }
    const value = JSON.parse(text)[name]
    return value == null ? null : String(value)
}
