package com.qin.runtime.core;

import java.nio.file.Path;

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
                || !mainModule.contains("/@qin-mod/app/controllers/UserController.js")
                || !mainModule.contains("dbReady.className = toClassName(connectedStyle)")) {
            throw new IllegalStateException("User DB main module missing OVS/CSSTS/controller wiring:\n" + mainModule);
        }

        String controllerModule = service.transpileByRequestPath("/@qin-mod/app/controllers/UserController.js");
        if (controllerModule == null
                || controllerModule.contains("@RestController")
                || controllerModule.contains("@GetMapping")
                || !controllerModule.contains("qonoCall(UserController, \"getAll\")")
                || !controllerModule.contains("GetMapping(\"\")(UserController, \"getAll\"")
                || !controllerModule.contains("DeleteMapping(\"/{id}\")(UserController, \"remove\"")) {
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
