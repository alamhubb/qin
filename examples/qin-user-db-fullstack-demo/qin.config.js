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
        passwordEnv: "QIN_DEMO_DB_PASSWORD"
    },
    dependencies: {
        "com.qin:qin-runtime-core": "0.1.0",
        "org.postgresql:postgresql": "42.7.3"
    }
}
