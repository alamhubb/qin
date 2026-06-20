import com.qin.runtime.core.QinHttpApp;
import com.qin.runtime.core.QinHttpRequest;
import com.qin.runtime.core.QinHttpResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public final class Main {
    private static final QinHttpApp APP = QinHttpApp.create()
            .get("/api/users", Main::listUsers)
            .post("/api/users", Main::createUser)
            .delete("/api/users/{id}", Main::deleteUser);

    private Main() {
    }

    public static Object run() {
        return "qin-user-db-fullstack-demo";
    }

    public static QinHttpApp app() {
        return APP;
    }

    private static QinHttpResponse listUsers(QinHttpRequest request) {
        try (Connection connection = connect()) {
            ensureSchema(connection);
            StringBuilder json = new StringBuilder("{\"users\":[");
            try (PreparedStatement statement = connection.prepareStatement(
                    "select id, name, email, created_at from qin_demo_users order by id asc");
                 ResultSet rows = statement.executeQuery()) {
                boolean first = true;
                while (rows.next()) {
                    if (!first) {
                        json.append(',');
                    }
                    first = false;
                    appendUser(json, rows);
                }
            }
            json.append("]}");
            return QinHttpResponse.json(json.toString());
        } catch (DemoConfigException error) {
            return configError(error);
        } catch (SQLException error) {
            return dbError(error);
        }
    }

    private static QinHttpResponse createUser(QinHttpRequest request) {
        String name = jsonStringField(request.bodyText(), "name");
        String email = jsonStringField(request.bodyText(), "email");
        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            return QinHttpResponse.json(400, "{\"error\":\"name and email are required\"}");
        }

        try (Connection connection = connect()) {
            ensureSchema(connection);
            try (PreparedStatement statement = connection.prepareStatement(
                    "insert into qin_demo_users (name, email) values (?, ?) returning id, name, email, created_at")) {
                statement.setString(1, name.trim());
                statement.setString(2, email.trim());
                try (ResultSet rows = statement.executeQuery()) {
                    rows.next();
                    StringBuilder json = new StringBuilder("{\"user\":");
                    appendUser(json, rows);
                    json.append('}');
                    return QinHttpResponse.json(201, json.toString());
                }
            }
        } catch (DemoConfigException error) {
            return configError(error);
        } catch (SQLException error) {
            return dbError(error);
        }
    }

    private static QinHttpResponse deleteUser(QinHttpRequest request) {
        long id;
        try {
            id = Long.parseLong(Objects.requireNonNullElse(request.param("id"), ""));
        } catch (NumberFormatException error) {
            return QinHttpResponse.json(400, "{\"error\":\"invalid user id\"}");
        }

        try (Connection connection = connect()) {
            ensureSchema(connection);
            try (PreparedStatement statement = connection.prepareStatement("delete from qin_demo_users where id = ?")) {
                statement.setLong(1, id);
                int deleted = statement.executeUpdate();
                if (deleted == 0) {
                    return QinHttpResponse.json(404, "{\"error\":\"user not found\"}");
                }
                return QinHttpResponse.json("{\"deleted\":" + id + "}");
            }
        } catch (DemoConfigException error) {
            return configError(error);
        } catch (SQLException error) {
            return dbError(error);
        }
    }

    private static Connection connect() throws SQLException {
        String passwordEnv = property("qin.database.passwordEnv", "QIN_DEMO_DB_PASSWORD");
        String password = env(passwordEnv, "");
        if (password.isBlank()) {
            throw new DemoConfigException("Set " + passwordEnv + " before using /api/users.");
        }
        String url = env("QIN_DEMO_DB_URL", property("qin.database.url", "jdbc:postgresql://localhost:5432/qin_demo"));
        String user = env("QIN_DEMO_DB_USER", property("qin.database.user", "postgres"));
        return DriverManager.getConnection(url, user, password);
    }

    private static void ensureSchema(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    create table if not exists qin_demo_users (
                        id bigserial primary key,
                        name text not null,
                        email text not null unique,
                        created_at timestamptz not null default now()
                    )
                    """);
        }
    }

    private static QinHttpResponse configError(DemoConfigException error) {
        return QinHttpResponse.json(503, "{\"error\":\"database config missing\",\"detail\":\""
                + escapeJson(error.getMessage()) + "\"}");
    }

    private static QinHttpResponse dbError(SQLException error) {
        return QinHttpResponse.json(503, "{\"error\":\"database unavailable\",\"detail\":\""
                + escapeJson(error.getMessage()) + "\"}");
    }

    private static void appendUser(StringBuilder json, ResultSet rows) throws SQLException {
        json.append('{')
                .append("\"id\":").append(rows.getLong("id")).append(',')
                .append("\"name\":\"").append(escapeJson(rows.getString("name"))).append("\",")
                .append("\"email\":\"").append(escapeJson(rows.getString("email"))).append("\",")
                .append("\"createdAt\":\"").append(escapeJson(rows.getString("created_at"))).append("\"")
                .append('}');
    }

    private static String jsonStringField(String json, String field) {
        String needle = "\"" + field + "\"";
        int key = json.indexOf(needle);
        if (key < 0) {
            return null;
        }
        int colon = json.indexOf(':', key + needle.length());
        if (colon < 0) {
            return null;
        }
        int quote = json.indexOf('"', colon + 1);
        if (quote < 0) {
            return null;
        }
        StringBuilder value = new StringBuilder();
        boolean escaped = false;
        for (int i = quote + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escaped) {
                value.append(switch (c) {
                    case '"' -> '"';
                    case '\\' -> '\\';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    default -> c;
                });
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                return value.toString();
            } else {
                value.append(c);
            }
        }
        return null;
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String property(String name, String fallback) {
        String value = System.getProperty(name);
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String escapeJson(String value) {
        return Objects.requireNonNullElse(value, "")
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static final class DemoConfigException extends RuntimeException {
        private DemoConfigException(String message) {
            super(message);
        }
    }
}
