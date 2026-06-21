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
    const basePath = controller.basePath || ""
    const routes = controller.__qonoRoutes || []
    for (const routeInfo of routes) {
        const fullPath = joinPath(basePath, routeInfo.path)
        app.route(routeInfo.method, fullPath, request => controller[routeInfo.handler](request))
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
