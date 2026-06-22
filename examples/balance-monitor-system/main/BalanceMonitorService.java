import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qin.runtime.core.QinJson;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BalanceMonitorService {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(intEnv("XIXIAPI_BALANCE_CONNECT_TIMEOUT_SECONDS", 8)))
            .build();

    public String configJson() {
        return "{"
                + "\"configured\":" + isDbConfigured()
                + ",\"table\":" + json(env("XIXIAPI_ACCOUNT_TABLE", "xixi_accounts"))
                + ",\"typeValue\":" + json(env("XIXIAPI_ACCOUNT_TYPE_VALUE", "apikey"))
                + ",\"balancePaths\":" + json(env("XIXIAPI_BALANCE_PATHS", defaultBalancePaths()))
                + "}";
    }

    public String balanceReportJson() {
        if (!isDbConfigured()) {
            return "{\"configured\":false,\"checkedAt\":" + json(Instant.now().toString())
                    + ",\"accounts\":[],\"error\":\"Set XIXIAPI_DB_URL, XIXIAPI_DB_USER and XIXIAPI_DB_PASSWORD.\"}";
        }
        List<ApiAccount> accounts = loadAccounts();
        List<BalanceRow> rows = new ArrayList<>();
        for (ApiAccount account : accounts) {
            rows.add(checkBalance(account));
        }
        StringBuilder json = new StringBuilder();
        json.append("{\"configured\":true,\"checkedAt\":").append(json(Instant.now().toString()))
                .append(",\"total\":").append(rows.size())
                .append(",\"accounts\":[");
        for (int i = 0; i < rows.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendBalanceRow(json, rows.get(i));
        }
        json.append("]}");
        return json.toString();
    }

    private List<ApiAccount> loadAccounts() {
        String sql = env("XIXIAPI_ACCOUNT_SQL", "");
        boolean customSql = !sql.isBlank();
        if (!customSql) {
            sql = defaultAccountSql();
        }
        List<ApiAccount> accounts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(
                requireEnv("XIXIAPI_DB_URL"),
                requireEnv("XIXIAPI_DB_USER"),
                requireEnv("XIXIAPI_DB_PASSWORD"));
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (!customSql) {
                statement.setString(1, env("XIXIAPI_ACCOUNT_TYPE_VALUE", "apikey"));
            }
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    String id = column(rows, "id");
                    String name = column(rows, "name");
                    String domain = column(rows, "domain");
                    String baseUrl = column(rows, "base_url");
                    String apiKey = column(rows, "api_key");
                    if (baseUrl != null && !baseUrl.isBlank() && apiKey != null && !apiKey.isBlank()) {
                        accounts.add(new ApiAccount(id, name, domainFrom(domain, baseUrl), baseUrl, apiKey));
                    }
                }
            }
        } catch (Exception error) {
            throw new IllegalStateException("Failed to load xixiapi API key accounts: " + error.getMessage(), error);
        }
        return accounts;
    }

    private String defaultAccountSql() {
        String table = identifier(env("XIXIAPI_ACCOUNT_TABLE", "xixi_accounts"), "table");
        String id = identifier(env("XIXIAPI_ACCOUNT_ID_COLUMN", "id"), "id column");
        String name = identifier(env("XIXIAPI_ACCOUNT_NAME_COLUMN", "name"), "name column");
        String domain = identifier(env("XIXIAPI_ACCOUNT_DOMAIN_COLUMN", "domain"), "domain column");
        String url = identifier(env("XIXIAPI_ACCOUNT_URL_COLUMN", "url"), "url column");
        String key = identifier(env("XIXIAPI_ACCOUNT_KEY_COLUMN", "api_key"), "api key column");
        String type = identifier(env("XIXIAPI_ACCOUNT_TYPE_COLUMN", "type"), "type column");
        return "select " + id + " as id, " + name + " as name, " + domain + " as domain, "
                + url + " as base_url, " + key + " as api_key from " + table
                + " where " + type + " = ? order by " + domain + ", " + name;
    }

    private BalanceRow checkBalance(ApiAccount account) {
        List<String> paths = balancePaths();
        String lastError = "";
        for (String path : paths) {
            try {
                URI uri = URI.create(joinUrl(account.baseUrl(), path));
                HttpRequest request = HttpRequest.newBuilder(uri)
                        .timeout(Duration.ofSeconds(intEnv("XIXIAPI_BALANCE_TIMEOUT_SECONDS", 20)))
                        .header("Authorization", "Bearer " + account.apiKey())
                        .header("X-API-Key", account.apiKey())
                        .header("Accept", "application/json")
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    BalanceValue value = parseBalance(response.body());
                    return new BalanceRow(account, "ok", value.amount(), value.currency(), path, response.statusCode(), "");
                }
                lastError = "HTTP " + response.statusCode() + " from " + path;
            } catch (Exception error) {
                lastError = error.getMessage();
            }
        }
        return new BalanceRow(account, "error", "", "", paths.isEmpty() ? "" : paths.get(0), 0, lastError);
    }

    private BalanceValue parseBalance(String body) {
        JsonElement root = JsonParser.parseString(Objects.requireNonNullElse(body, "{}"));
        String amount = findFirstNumber(root,
                "total_available",
                "available_balance",
                "balance",
                "credit",
                "quota",
                "remaining",
                "remain",
                "totalAvailable");
        String currency = findFirstString(root, "currency", "currency_code", "unit");
        return new BalanceValue(amount == null ? "" : amount, currency == null ? "" : currency);
    }

    private String findFirstNumber(JsonElement element, String... names) {
        if (element == null || element.isJsonNull()) {
            return null;
        }
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            for (String name : names) {
                JsonElement value = object.get(name);
                if (value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) {
                    return value.getAsBigDecimal().stripTrailingZeros().toPlainString();
                }
                if (value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
                    String text = value.getAsString();
                    if (looksNumeric(text)) {
                        return new BigDecimal(text).stripTrailingZeros().toPlainString();
                    }
                }
            }
            for (JsonElement child : object.asMap().values()) {
                String found = findFirstNumber(child, names);
                if (found != null) {
                    return found;
                }
            }
        } else if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                String found = findFirstNumber(child, names);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private String findFirstString(JsonElement element, String... names) {
        if (element == null || element.isJsonNull()) {
            return null;
        }
        if (element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            for (String name : names) {
                JsonElement value = object.get(name);
                if (value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
                    return value.getAsString();
                }
            }
            for (JsonElement child : object.asMap().values()) {
                String found = findFirstString(child, names);
                if (found != null) {
                    return found;
                }
            }
        } else if (element.isJsonArray()) {
            for (JsonElement child : element.getAsJsonArray()) {
                String found = findFirstString(child, names);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private List<String> balancePaths() {
        List<String> paths = new ArrayList<>();
        for (String part : env("XIXIAPI_BALANCE_PATHS", defaultBalancePaths()).split(",")) {
            String path = part.trim();
            if (!path.isBlank()) {
                paths.add(path);
            }
        }
        return paths;
    }

    private static void appendBalanceRow(StringBuilder json, BalanceRow row) {
        json.append('{')
                .append("\"id\":").append(json(row.account().id())).append(',')
                .append("\"name\":").append(json(row.account().name())).append(',')
                .append("\"domain\":").append(json(row.account().domain())).append(',')
                .append("\"baseUrl\":").append(json(row.account().baseUrl())).append(',')
                .append("\"keyPreview\":").append(json(maskKey(row.account().apiKey()))).append(',')
                .append("\"status\":").append(json(row.status())).append(',')
                .append("\"balance\":").append(json(row.balance())).append(',')
                .append("\"currency\":").append(json(row.currency())).append(',')
                .append("\"path\":").append(json(row.path())).append(',')
                .append("\"httpStatus\":").append(row.httpStatus()).append(',')
                .append("\"error\":").append(json(row.error()))
                .append('}');
    }

    private static String column(ResultSet rows, String name) {
        try {
            return rows.getString(name);
        } catch (Exception ignored) {
            return "";
        }
    }

    private static String domainFrom(String domain, String baseUrl) {
        if (domain != null && !domain.isBlank()) {
            return domain;
        }
        try {
            URI uri = URI.create(baseUrl);
            return uri.getHost() == null ? baseUrl : uri.getHost();
        } catch (Exception ignored) {
            return baseUrl;
        }
    }

    private static String joinUrl(String baseUrl, String path) {
        String base = Objects.requireNonNullElse(baseUrl, "").trim();
        String suffix = Objects.requireNonNullElse(path, "").trim();
        if (suffix.startsWith("http://") || suffix.startsWith("https://")) {
            return suffix;
        }
        while (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        if (!suffix.startsWith("/")) {
            suffix = "/" + suffix;
        }
        return base + suffix;
    }

    private static String maskKey(String key) {
        String text = Objects.requireNonNullElse(key, "");
        if (text.length() <= 10) {
            return "****";
        }
        return text.substring(0, 6) + "..." + text.substring(text.length() - 4);
    }

    private static boolean looksNumeric(String text) {
        return text != null && text.trim().matches("-?\\d+(\\.\\d+)?");
    }

    private static boolean isDbConfigured() {
        return !env("XIXIAPI_DB_URL", "").isBlank()
                && !env("XIXIAPI_DB_USER", "").isBlank()
                && !env("XIXIAPI_DB_PASSWORD", "").isBlank();
    }

    private static String requireEnv(String name) {
        String value = env(name, "");
        if (value.isBlank()) {
            throw new IllegalStateException("Missing environment variable: " + name);
        }
        return value;
    }

    private static String identifier(String value, String label) {
        String text = Objects.requireNonNullElse(value, "").trim();
        if (!text.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException("Unsafe " + label + ": " + value);
        }
        return text;
    }

    private static int intEnv(String name, int fallback) {
        try {
            return Integer.parseInt(env(name, String.valueOf(fallback)));
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            value = System.getProperty(name);
        }
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String defaultBalancePaths() {
        return "/dashboard/billing/credit_grants,/v1/dashboard/billing/credit_grants,/v1/user/balance,/api/user/self";
    }

    private static String json(String value) {
        return QinJson.string(value);
    }

    private record ApiAccount(String id, String name, String domain, String baseUrl, String apiKey) {
    }

    private record BalanceValue(String amount, String currency) {
    }

    private record BalanceRow(
            ApiAccount account,
            String status,
            String balance,
            String currency,
            String path,
            int httpStatus,
            String error) {
    }
}
