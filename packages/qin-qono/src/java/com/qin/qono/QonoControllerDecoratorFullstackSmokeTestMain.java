package com.qin.qono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class QonoControllerDecoratorFullstackSmokeTestMain {
    private static final int PORT = 18117;

    private QonoControllerDecoratorFullstackSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qono-controller-decorator-");
        writeProject(root);
        Process process = startServer(root);
        try {
            waitForServer();
            verifyApi();
            System.out.println("QonoControllerDecoratorFullstackSmokeTestMain passed.");
        } finally {
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private static void writeProject(Path root) throws Exception {
        Files.createDirectories(root.resolve("main/controllers"));
        Files.createDirectories(root.resolve("app"));
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                    name: "qono-controller-decorator-smoke",
                    port: 18117,
                    backend: { entry: "main/main.ts" },
                    frontend: { srcDir: "app", staticDir: "app" },
                    dependencies: {
                        "com.qin:qin-runtime-core": "0.1.0",
                        "com.qin:qin-qono": "0.1.0"
                    }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("app/main.js"), "console.log('qono decorator smoke')\n", StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/qono-class.ts"), """
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
                        routes.push({ method, path, handler: propertyKey })
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
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/controllers/UserController.qin"), """
                import { RestController, RequestMapping, GetMapping, PostMapping, DeleteMapping } from "../qono-class"
                import { Qono } from "java:com.qin.qono"

                @RestController
                @RequestMapping("/api/users")
                export object UserController {
                    controllerName = "UserController"
                    basePath = "/api/users"

                    @GetMapping("")
                    getAll(request) {
                        return Qono.jsonRaw("{\\"users\\":[]}")
                    }

                    @PostMapping("")
                    create(request) {
                        return Qono.jsonRaw(201, "{\\"user\\":" + request.bodyText() + "}")
                    }

                    @DeleteMapping("/{id}")
                    remove(request) {
                        return Qono.jsonRaw("{\\"deleted\\":\\"" + request.param("id") + "\\"}")
                    }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/main.ts"), """
                import { Qono } from "java:com.qin.qono"
                import { useQonoController } from "./qono-class"
                import { UserController } from "./controllers/UserController"

                export const app = useQonoController(Qono.create().health(), UserController).toHttpApp()

                "qono-controller-decorator-smoke"
                """, StandardCharsets.UTF_8);
    }

    private static Process startServer(Path root) throws Exception {
        String javaBin = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String classpath = System.getProperty("java.class.path");
        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add("com.qin.runtime.core.QinFullstackMain");
        command.add("--root");
        command.add(root.toAbsolutePath().normalize().toString());
        command.add("--port");
        command.add(String.valueOf(PORT));
        command.add("--dev");

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(root.toFile());
        pb.inheritIO();
        return pb.start();
    }

    private static void waitForServer() throws Exception {
        Instant deadline = Instant.now().plus(Duration.ofSeconds(25));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + PORT + "/api/health");
        while (Instant.now().isBefore(deadline)) {
            try {
                HttpRequest request = HttpRequest.newBuilder(uri).GET().timeout(Duration.ofSeconds(2)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return;
                }
            } catch (Exception ignored) {
                Thread.sleep(250);
            }
            Thread.sleep(250);
        }
        throw new IllegalStateException("Server did not become healthy in time: " + uri);
    }

    private static void verifyApi() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        requireResponse(client, "GET", "/api/users", null, 200, "\"users\"");
        requireResponse(client, "POST", "/api/users", "{\"name\":\"Ada\"}", 201, "\"Ada\"");
        requireResponse(client, "DELETE", "/api/users/7", null, 200, "\"7\"");
        requireResponse(client, "POST", "/api/rpc/UserController.getAll", "{}", 200, "\"users\"");
        requireResponse(client, "POST", "/api/rpc/UserController.create", "{\"name\":\"RpcAda\"}", 201, "\"RpcAda\"");
        requireResponse(client, "POST", "/api/rpc/UserController.remove", "{\"id\":8}", 200, "\"8\"");
    }

    private static void requireResponse(
            HttpClient client,
            String method,
            String path,
            String body,
            int status,
            String expected) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create("http://localhost:" + PORT + path))
                .timeout(Duration.ofSeconds(5));
        if (body == null) {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            builder.method(method, HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json");
        }
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != status || !response.body().contains(expected)) {
            throw new IllegalStateException("Unexpected " + method + " " + path + " response: "
                    + response.statusCode() + " " + response.body());
        }
    }
}
