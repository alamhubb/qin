package com.qin.cli;

import com.qin.types.DatabaseConfig;
import com.qin.types.QinConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class QinCliDatabaseConfigJvmArgsSmokeTestMain {
    private QinCliDatabaseConfigJvmArgsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinConfig config = new QinConfig(
                "qin-cli-database-config-smoke",
                "0.1.0",
                null,
                null,
                null,
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new DatabaseConfig(
                        "jdbc:postgresql://db.example.test:5432/qin_demo",
                        "qin_user",
                        "test-password",
                        "QIN_DEMO_DB_PASSWORD"),
                null,
                null,
                null,
                null,
                null);

        List<String> command = new ArrayList<>();
        Method method = QinCli.class.getDeclaredMethod("appendDatabaseSystemProperties", List.class, QinConfig.class);
        method.setAccessible(true);
        method.invoke(null, command, config);

        require(command.equals(List.of(
                "-Dqin.database.url=jdbc:postgresql://db.example.test:5432/qin_demo",
                "-Dqin.database.user=qin_user",
                "-Dqin.database.password=test-password",
                "-Dqin.database.passwordEnv=QIN_DEMO_DB_PASSWORD")), "database JVM args");

        System.out.println("QinCliDatabaseConfigJvmArgsSmokeTestMain passed.");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
