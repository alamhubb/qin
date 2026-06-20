import vue from "@vitejs/plugin-vue"
import vitePluginOvs from "vite-plugin-ovs"

export default {
    name: "com.qin.demo:qin-user-db-fullstack-demo",
    version: "0.1.0",
    description: "Qin single-port fullstack user database demo",
    port: 19116,
    frontend: {
        srcDir: "app",
        entry: "main.js",
        staticDir: "app"
    },
    backend: {
        sourceDir: "main",
        entry: "main/Main.java"
    },
    database: {
        url: "jdbc:postgresql://43.143.220.49:5432/qin_demo",
        user: "qin_user",
        password: "QinDemo_2026!",
        passwordEnv: "QIN_DEMO_DB_PASSWORD"
    },
    packageOverrides: {
        "slime-parser": "../ovs-cssts-demos/qin-ovs-cssts-generated-ts-slime-demo/packages/slime-parser",
        "@qin/java-sdk-js": "../ovs-cssts-demos/qin-ovs-cssts-generated-ts-slime-demo/packages/java-sdk-js",
        "subhuti": "../../../subhuti",
        "cssts-compiler": "../ovs-cssts-demos/qin-ovs-cssts-generated-ts-slime-demo/packages/cssts-compiler",
        "ovs-compiler": "../../../ovsjs/ovs/ovs-compiler"
    },
    dependencies: {
        "com.qin:qin-runtime-core": "0.1.0",
        "org.postgresql:postgresql": "42.7.3",
        "@vitejs/plugin-vue": "6.0.7",
        "@vue/compiler-sfc": "latest",
        "vue": "latest",
        "cssts-compiler": "0.2.87",
        "cssts-ts": "0.2.87",
        "vite-plugin-ovs": "0.2.2",
        "vite-plugin-cssts": "0.2.87",
        "ovs-compiler": "0.2.2",
        "ovsjs": "0.2.2"
    },
    plugins: [
        ...vitePluginOvs({
            cssts: {
                classPrefix: "user-db-"
            }
        }),
        vue()
    ]
}
