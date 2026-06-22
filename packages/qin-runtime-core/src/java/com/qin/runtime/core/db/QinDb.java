package com.qin.runtime.core.db;

import com.qin.runtime.core.QinJson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public final class QinDb {
    private final String url;
    private final String user;
    private final String password;

    private QinDb(String url, String user, String password) {
        this.url = Objects.requireNonNull(url, "url");
        this.user = Objects.requireNonNull(user, "user");
        this.password = Objects.requireNonNull(password, "password");
    }

    public static QinDb fromSystemProperties() {
        String passwordEnv = property("qin.database.passwordEnv", "QIN_DEMO_DB_PASSWORD");
        String password = env(passwordEnv, property("qin.database.password", ""));
        if (password.isBlank()) {
            throw new QinDbConfigException("Set database.password or " + passwordEnv + " before using QinDb.");
        }
        String url = env("QIN_DEMO_DB_URL", property("qin.database.url", "jdbc:postgresql://localhost:5432/qin_demo"));
        String user = env("QIN_DEMO_DB_USER", property("qin.database.user", "postgres"));
        return new QinDb(url, user, password);
    }

    public static QinDbTable table(String name) {
        return new QinDbTable(name);
    }

    public void ensure(QinDbTable table) {
        try (Connection connection = connect(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSql(table));
        } catch (SQLException error) {
            throw new QinDbException("Failed to ensure table " + table.name(), error);
        }
    }

    public String selectJson(String arrayProperty, QinDbTable table, String orderColumn, String direction) {
        ensure(table);
        String orderSql = table.column(orderColumn).sqlName();
        String sort = "desc".equalsIgnoreCase(direction) ? "desc" : "asc";
        String sql = "select " + selectColumns(table) + " from " + table.name() + " order by " + orderSql + " " + sort;
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rows = statement.executeQuery()) {
            StringBuilder json = new StringBuilder("{\"").append(QinJson.escape(arrayProperty)).append("\":[");
            boolean first = true;
            while (rows.next()) {
                if (!first) {
                    json.append(',');
                }
                first = false;
                appendRow(json, table, rows);
            }
            json.append("]}");
            return json.toString();
        } catch (SQLException error) {
            throw new QinDbException("Failed to select from " + table.name(), error);
        }
    }

    public String insertJson(String objectProperty, QinDbTable table, String inputJson, String insertColumnsCsv) {
        ensure(table);
        List<QinDbColumn> insertColumns = columnsFromCsv(table, insertColumnsCsv);
        if (insertColumns.isEmpty()) {
            throw new IllegalArgumentException("insert columns cannot be empty");
        }
        StringJoiner names = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");
        for (QinDbColumn column : insertColumns) {
            names.add(column.sqlName());
            placeholders.add("?");
        }
        String sql = "insert into " + table.name() + " (" + names + ") values (" + placeholders + ") returning "
                + selectColumns(table);
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < insertColumns.size(); i++) {
                QinDbColumn column = insertColumns.get(i);
                String value = jsonStringField(inputJson, column.jsonName());
                if (value == null || value.isBlank()) {
                    throw new QinDbValidationException(column.jsonName() + " is required");
                }
                statement.setString(i + 1, value.trim());
            }
            try (ResultSet rows = statement.executeQuery()) {
                rows.next();
                StringBuilder json = new StringBuilder("{\"").append(QinJson.escape(objectProperty)).append("\":");
                appendRow(json, table, rows);
                json.append('}');
                return json.toString();
            }
        } catch (QinDbValidationException error) {
            throw error;
        } catch (SQLException error) {
            throw new QinDbException("Failed to insert into " + table.name(), error);
        }
    }

    public String deleteByIdJson(QinDbTable table, String inputJson) {
        ensure(table);
        QinDbColumn id = table.primaryKeyColumn();
        String idText = deleteIdText(inputJson, id.jsonName());
        long idValue;
        try {
            idValue = Long.parseLong(Objects.requireNonNullElse(idText, ""));
        } catch (NumberFormatException error) {
            throw new QinDbValidationException("invalid " + id.jsonName());
        }
        String sql = "delete from " + table.name() + " where " + id.sqlName() + " = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, idValue);
            int deleted = statement.executeUpdate();
            if (deleted == 0) {
                throw new QinDbNotFoundException("row not found");
            }
            return "{\"deleted\":" + idValue + "}";
        } catch (QinDbNotFoundException error) {
            throw error;
        } catch (SQLException error) {
            throw new QinDbException("Failed to delete from " + table.name(), error);
        }
    }

    static String deleteIdText(String inputJson, String idJsonName) {
        String idText = jsonScalarField(inputJson, idJsonName);
        if (idText == null || idText.isBlank()) {
            idText = jsonScalarField(inputJson, "id");
        }
        if ((idText == null || idText.isBlank()) && inputJson != null) {
            String raw = inputJson.trim();
            if (!raw.startsWith("{") && !raw.startsWith("[")) {
                idText = raw;
            }
        }
        return idText;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private String createTableSql(QinDbTable table) {
        StringJoiner columns = new StringJoiner(", ");
        for (QinDbColumn column : table.columns()) {
            columns.add(column.definitionSql());
        }
        return "create table if not exists " + table.name() + " (" + columns + ")";
    }

    private String selectColumns(QinDbTable table) {
        StringJoiner columns = new StringJoiner(", ");
        for (QinDbColumn column : table.columns()) {
            columns.add(column.sqlName());
        }
        return columns.toString();
    }

    private List<QinDbColumn> columnsFromCsv(QinDbTable table, String csv) {
        List<QinDbColumn> columns = new ArrayList<>();
        for (String part : Objects.requireNonNullElse(csv, "").split(",")) {
            String name = part.trim();
            if (!name.isBlank()) {
                columns.add(table.column(name));
            }
        }
        return columns;
    }

    private void appendRow(StringBuilder json, QinDbTable table, ResultSet rows) throws SQLException {
        json.append('{');
        boolean first = true;
        for (QinDbColumn column : table.columns()) {
            if (!first) {
                json.append(',');
            }
            first = false;
            json.append('"').append(QinJson.escape(column.jsonName())).append("\":");
            Object value = rows.getObject(column.sqlName());
            if (value == null) {
                json.append("null");
            } else if (value instanceof Number || value instanceof Boolean) {
                json.append(value);
            } else {
                json.append(QinJson.string(String.valueOf(value)));
            }
        }
        json.append('}');
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String property(String name, String fallback) {
        String value = System.getProperty(name);
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String jsonStringField(String json, String field) {
        String needle = "\"" + field + "\"";
        int key = Objects.requireNonNullElse(json, "").indexOf(needle);
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

    private static String jsonScalarField(String json, String field) {
        String needle = "\"" + field + "\"";
        int key = Objects.requireNonNullElse(json, "").indexOf(needle);
        if (key < 0) {
            return null;
        }
        int colon = json.indexOf(':', key + needle.length());
        if (colon < 0) {
            return null;
        }
        int valueStart = colon + 1;
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }
        if (valueStart >= json.length()) {
            return null;
        }
        if (json.charAt(valueStart) == '"') {
            return jsonStringField(json, field);
        }
        int valueEnd = valueStart;
        while (valueEnd < json.length()) {
            char c = json.charAt(valueEnd);
            if (c == ',' || c == '}') {
                break;
            }
            valueEnd++;
        }
        String value = json.substring(valueStart, valueEnd).trim();
        return value.isEmpty() || "null".equals(value) ? null : value;
    }

    static String normalizeIdentifier(String value, String label) {
        String text = value == null ? "" : value.trim();
        if (!text.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException(label + " is not a safe SQL identifier: " + value);
        }
        return text;
    }

    static String toJsonName(String sqlName) {
        StringBuilder out = new StringBuilder();
        boolean upperNext = false;
        for (char c : sqlName.toCharArray()) {
            if (c == '_') {
                upperNext = true;
            } else if (upperNext) {
                out.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                out.append(Character.toLowerCase(c));
            }
        }
        return out.toString();
    }
}
