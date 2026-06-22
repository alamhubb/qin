package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.module.resolver.QinLinkedModuleSection;
import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.module.resolver.QinLinkedModuleSourceEmitter;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmQinObjectExportSmokeTestMain {
    private QinEsmQinObjectExportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-esm-qin-object-export-");
        Files.createDirectories(root.resolve("controllers"));
        Files.createDirectories(root.resolve("db"));
        Files.writeString(root.resolve("qono-class.ts"), """
                export function RestController(target) { target.__qonoController = true }
                export function RequestMapping(path) { return target => { target.basePath = path } }
                export function GetMapping(path) { return route("GET", path) }
                function route(method, path) {
                    return (target, propertyKey, descriptor) => {
                        const routes = target.__qonoRoutes || []
                        routes.push({ method, path, handler: propertyKey })
                        target.__qonoRoutes = routes
                        return descriptor
                    }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("db/schema.ts"), """
                export const db = {}
                export const users = {}
                """, StandardCharsets.UTF_8);
        Files.writeString(root.resolve("controllers/UserController.qin"), """
                import { RestController, RequestMapping, GetMapping } from "../qono-class"
                import { Qono } from "java:com.qin.qono"
                import { db, users } from "../db/schema"

                @RestController
                @RequestMapping("/api/users")
                export object UserController {
                    @GetMapping("")
                    getAll(request) {
                        return Qono.jsonRaw(db.selectJson("users", users, "id", "asc"));
                    }
                }
                """, StandardCharsets.UTF_8);
        Path main = root.resolve("main.ts");
        Files.writeString(main, """
                import { UserController } from "./controllers/UserController"
                export const controller = UserController
                """, StandardCharsets.UTF_8);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(main);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);
        QinLinkedModuleSource linked = new QinLinkedModuleSourceEmitter().emit(graph);
        QinLinkedModuleSection entrySection = linked.moduleSections().get(linked.moduleSections().size() - 1);
        require(
                entrySection.classSource().contains("const UserController = __qin_export_get__("),
                "linked entry should import export object through an export slot");
        System.out.println("QinEsmQinObjectExportSmokeTestMain passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
