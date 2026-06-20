package demo;

import com.qin.runtime.http.QinHttpApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Main {
    private static final String DEFAULT_DB_URL = "jdbc:postgresql://43.143.220.49:5432/qin_demo";
    private static final String DEFAULT_DB_USER = "qin_user";

    private Main() {
    }

    public static Object run() {
        ensureSchema();
        return QinHttpApp.create()
                .get("/api/users", context -> context.json(listUsers()))
                .post("/api/users", context -> {
                    String name = trimToNull(context.jsonString("name"));
                    String email = trimToNull(context.jsonString("email"));
                    if (name == null || email == null) {
                        return context.json(Map.of("error", "name and email are required"), 400);
                    }
                    try {
                        return context.json(createUser(name, email), 201);
                    } catch (SQLException error) {
                        if ("23505".equals(error.getSQLState())) {
                            return context.json(Map.of("error", "email already exists"), 409);
                        }
                        throw error;
                    }
                })
                .delete("/api/users/:id", context -> {
                    long id = Long.parseLong(context.param("id"));
                    deleteUser(id);
                    return context.noContent();
                });
    }

    private static List<Map<String, Object>> listUsers() throws SQLException {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, email from users order by id");
             ResultSet resultSet = statement.executeQuery()) {
            List<Map<String, Object>> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(user(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")));
            }
            return users;
        }
    }

    private static Map<String, Object> createUser(String name, String email) throws SQLException {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "insert into users (name, email) values (?, ?) returning id, name, email")) {
            statement.setString(1, name);
            statement.setString(2, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("insert did not return a row");
                }
                return user(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
            }
        }
    }

    private static void deleteUser(long id) throws SQLException {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement("delete from users where id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private static void ensureSchema() {
        try (Connection connection = openConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    create table if not exists users (
                      id bigserial primary key,
                      name varchar(120) not null,
                      email varchar(240) not null unique,
                      created_at timestamptz not null default now()
                    )
                    """);
        } catch (SQLException error) {
            throw new IllegalStateException("Failed to initialize users table", error);
        }
    }

    private static Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
                env("QIN_DEMO_DB_URL", DEFAULT_DB_URL),
                env("QIN_DEMO_DB_USER", DEFAULT_DB_USER),
                requiredEnv("QIN_DEMO_DB_PASSWORD"));
    }

    private static Map<String, Object> user(long id, String name, String email) {
        Map<String, Object> user = new LinkedHashMap<>();
        user.put("id", id);
        user.put("name", name);
        user.put("email", email);
        return user;
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String requiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
