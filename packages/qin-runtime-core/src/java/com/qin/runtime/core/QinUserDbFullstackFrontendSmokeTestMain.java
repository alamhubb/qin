package com.qin.runtime.core;

import java.nio.file.Path;
import java.nio.file.Files;

public final class QinUserDbFullstackFrontendSmokeTestMain {
    private QinUserDbFullstackFrontendSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("examples", "qin-user-db-fullstack-demo").toAbsolutePath().normalize();
        QinFrontendEsmService service = QinFrontendEsmService.create(root, root.resolve("app/main.js"));

        String mainModule = service.transpileByRequestPath("/@qin-mod/app/main.js");
        if (mainModule == null
                || !mainModule.contains("/@qin-mod/app/UserRuntimeBadge.ovs.js")
                || !mainModule.contains("/@qin-mod/app/tokens.cssts.js")
                || !mainModule.contains("/@qin-mod/app/style.css.js")
                || !mainModule.contains("/@qin-mod/main/controllers/UserController.js")
                || !mainModule.contains("await UserController.getAll()")
                || !mainModule.contains("await UserController.create({")
                || !mainModule.contains("await UserController.remove({ id:")
                || !mainModule.contains("dbReady.className = toClassName(connectedStyle)")
                || !mainModule.contains("name: data.get(\"name\")")
                || mainModule.contains("requestJson(\"/api/users")
                || mainModule.contains("email: data.get(\"email\")")) {
            throw new IllegalStateException("User DB main module missing OVS/CSSTS/controller wiring:\n" + mainModule);
        }

        if (Files.exists(root.resolve("app/controllers/UserController.js"))
                || Files.exists(root.resolve("app/qono-rpc.js"))) {
            throw new IllegalStateException("User DB demo should not check in browser RPC proxy/helper files");
        }

        String controllerModule = service.transpileByRequestPath("/@qin-mod/main/controllers/UserController.js");
        if (controllerModule == null
                || controllerModule.contains("@RestController")
                || controllerModule.contains("java:")
                || controllerModule.contains("@GetMapping")
                || !controllerModule.contains("export const UserController = {")
                || !controllerModule.contains("getAll(input = {})")
                || !controllerModule.contains("create(input = {})")
                || !controllerModule.contains("remove(input = {})")
                || !controllerModule.contains("encodeURIComponent(methodName)")
                || !controllerModule.contains("JSON.stringify(input ?? {})")) {
            throw new IllegalStateException("User DB frontend controller is not browser-compatible:\n" + controllerModule);
        }

        String styleModule = service.transpileByRequestPath("/@qin-mod/app/style.css.js");
        if (styleModule == null
                || !styleModule.contains("data-qin-css")
                || !styleModule.contains(".shell")) {
            throw new IllegalStateException("User DB CSS module missing style injection:\n" + styleModule);
        }

        String ovsModule = service.transpileByRequestPath("/@qin-mod/app/UserRuntimeBadge.ovs.js");
        if (ovsModule == null
                || !ovsModule.contains("?qin-vue-cssts=style")
                || !ovsModule.contains("__qinMountVue")) {
            throw new IllegalStateException("User DB OVS module missing CSSTS style wiring:\n" + ovsModule);
        }

        System.out.println("QinUserDbFullstackFrontendSmokeTestMain passed.");
    }
}
