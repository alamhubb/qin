package com.qin.core;

import com.qin.types.DatabaseConfig;
import com.qin.types.QinConfig;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinDatabaseConfigSmokeTestMain {
    private QinDatabaseConfigSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-database-config-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-database-config-smoke",
                  version: "0.1.0",
                  database: {
                    url: "jdbc:postgresql://db.example.test:5432/qin_demo",
                    user: "qin_user",
                    passwordEnv: "QIN_DEMO_DB_PASSWORD"
                  }
                }
                """, StandardCharsets.UTF_8);

        QinConfig config = new ConfigLoader(root.toString()).load();
        DatabaseConfig database = config.database();
        require(database != null, "database config");
        require("jdbc:postgresql://db.example.test:5432/qin_demo".equals(database.url()), "database url");
        require("qin_user".equals(database.user()), "database user");
        require("QIN_DEMO_DB_PASSWORD".equals(database.passwordEnv()), "database password env");

        System.out.println("QinDatabaseConfigSmokeTestMain passed.");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
