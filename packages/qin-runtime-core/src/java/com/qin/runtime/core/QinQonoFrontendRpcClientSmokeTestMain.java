package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinQonoFrontendRpcClientSmokeTestMain {
    private QinQonoFrontendRpcClientSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-qono-frontend-rpc-");
        Files.createDirectories(root.resolve("app"));
        Files.createDirectories(root.resolve("main/controllers"));

        Files.writeString(root.resolve("app/main.js"), """
                import { UserController } from "../main/controllers/UserController.qin"

                export async function loadUsers() {
                    return await UserController.getAll()
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/qono-class.ts"), """
                export function RestController(target) {
                    target.__qonoController = true
                }
                export function GetMapping(path) {
                    return (target, propertyKey, descriptor) => descriptor
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("main/controllers/UserController.qin"), """
                import { RestController, GetMapping } from "../qono-class"
                import { Qono } from "java:com.qin.qono"

                @RestController
                export object UserController {
                    controllerName = "UserController"

                    @GetMapping("")
                    getAll(request) {
                        return Qono.jsonRaw("{\\"users\\":[]}")
                    }
                }
                """, StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, root.resolve("app/main.js"));
        String mainModule = service.transpileByRequestPath("/@qin-mod/app/main.js");
        require(mainModule != null
                        && mainModule.contains("/@qin-mod/main/controllers/UserController.js")
                        && mainModule.contains("await UserController.getAll()"),
                "frontend main module should import generated controller client:\n" + mainModule);

        String controllerModule = service.transpileByRequestPath("/@qin-mod/main/controllers/UserController.js");
        require(controllerModule != null
                        && controllerModule.contains("export const UserController = {")
                        && controllerModule.contains("getAll(input = {})")
                        && controllerModule.contains("/api/rpc/")
                        && controllerModule.contains("JSON.stringify(input ?? {})")
                        && !controllerModule.contains("java:com.qin.qono")
                        && !controllerModule.contains("Qono.jsonRaw"),
                "generated controller client should be browser-compatible:\n" + controllerModule);

        System.out.println("QinQonoFrontendRpcClientSmokeTestMain passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
