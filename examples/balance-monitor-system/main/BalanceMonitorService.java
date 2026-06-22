import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qin.runtime.core.QinJson;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BalanceMonitorService {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(intEnv("XIXIAPI_BALANCE_CONNECT_TIMEOUT_SECONDS", 8)))
            .build();

    public String configJson() {
        DbConfig db = dbConfig();
        return "{"
                + "\"configured\":" + db.configured()
                + ",\"table\":" + json(env("XIXIAPI_ACCOUNT_TABLE", "accounts"))
                + ",\"typeValue\":" + json(env("XIXIAPI_ACCOUNT_TYPE_VALUE", "apikey"))
                + ",\"balancePaths\":" + json(env("XIXIAPI_BALANCE_PATHS", defaultBalancePaths()))
                + "}";
    }

    public String balanceReportJson() {
        DbConfig db = dbConfig();
        if (!db.configured()) {
            return "{\"configured\":false,\"checkedAt\":" + json(Instant.now().toString())
                    + ",\"accounts\":[],\"error\":\"Set XIXIAPI_DB_URL, XIXIAPI_DB_USER and XIXIAPI_DB_PASSWORD, or provide DB_HOST/DB_USER/DB_PASSWORD aliases.\"}";
        }
        List<ApiAccount> accounts = loadAccounts(db);
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

    private List<ApiAccount> loadAccounts(DbConfig db) {
        String sql = env("XIXIAPI_ACCOUNT_SQL", "");
        boolean customSql = !sql.isBlank();
        if (!customSql) {
            sql = defaultAccountSql();
        }
        List<ApiAccount> accounts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(db.url(), db.user(), db.password());
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
        String table = identifier(env("XIXIAPI_ACCOUNT_TABLE", "accounts"), "table");
        if ("accounts".equals(table)
                && env("XIXIAPI_ACCOUNT_URL_COLUMN", "").isBlank()
                && env("XIXIAPI_ACCOUNT_KEY_COLUMN", "").isBlank()) {
            return "select id::text as id, "
                    + "coalesce(nullif(name, ''), 'account-' || id::text) as name, "
                    + "coalesce(nullif(credentials->>'domain', ''), nullif(credentials->>'host', ''), nullif(platform, ''), '') as domain, "
                    + "coalesce(nullif(credentials->>'base_url', ''), nullif(credentials->>'url', ''), nullif(credentials->>'api_base', ''), nullif(credentials->>'endpoint', '')) as base_url, "
                    + "coalesce(nullif(credentials->>'api_key', ''), nullif(credentials->>'key', ''), nullif(credentials->>'token', '')) as api_key "
                    + "from accounts where type = ? and coalesce(status, 'active') = 'active' "
                    + "order by domain, name";
        }
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

    private static DbConfig dbConfig() {
        String url = firstEnv("XIXIAPI_DB_URL");
        String user = firstEnv("XIXIAPI_DB_USER", "DB_USER", "DATABASE_USER", "POSTGRES_USER");
        String password = firstEnv("XIXIAPI_DB_PASSWORD", "DB_PASSWORD", "DATABASE_PASSWORD", "POSTGRES_PASSWORD");
        String databaseUrl = firstEnv("DATABASE_URL");
        if (url.isBlank() && !databaseUrl.isBlank()) {
            ParsedDatabaseUrl parsed = parseDatabaseUrl(databaseUrl);
            url = parsed.url();
            if (user.isBlank()) {
                user = parsed.user();
            }
            if (password.isBlank()) {
                password = parsed.password();
            }
        }
        if (url.isBlank()) {
            String host = firstEnv("DB_HOST", "DATABASE_HOST", "POSTGRES_HOST");
            String port = firstEnv("DB_PORT", "DATABASE_PORT", "POSTGRES_PORT");
            String name = firstEnv("DB_NAME", "DATABASE_DBNAME", "POSTGRES_DB");
            if (!host.isBlank()) {
                url = "jdbc:postgresql://" + host + ":" + (port.isBlank() ? "5432" : port)
                        + "/" + (name.isBlank() ? "sub2api" : name);
            }
        }
        return new DbConfig(url, user, password);
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
        if (value == null || value.isBlank()) {
            value = dotEnv().get(name);
        }
        return value == null || value.isBlank() ? fallback : value;
    }

    private static String firstEnv(String... names) {
        for (String name : names) {
            String value = env(name, "");
            if (!value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private static ParsedDatabaseUrl parseDatabaseUrl(String value) {
        if (value.startsWith("jdbc:postgresql:")) {
            return new ParsedDatabaseUrl(value, "", "");
        }
        try {
            URI uri = URI.create(value);
            if (!"postgres".equals(uri.getScheme()) && !"postgresql".equals(uri.getScheme())) {
                return new ParsedDatabaseUrl("", "", "");
            }
            StringBuilder url = new StringBuilder("jdbc:postgresql://").append(uri.getHost());
            if (uri.getPort() > 0) {
                url.append(':').append(uri.getPort());
            }
            url.append(uri.getPath() == null || uri.getPath().isBlank() ? "/sub2api" : uri.getPath());
            if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                url.append('?').append(uri.getQuery());
            }
            String user = "";
            String password = "";
            String userInfo = uri.getUserInfo();
            if (userInfo != null && !userInfo.isBlank()) {
                int split = userInfo.indexOf(':');
                user = decode(split >= 0 ? userInfo.substring(0, split) : userInfo);
                password = split >= 0 ? decode(userInfo.substring(split + 1)) : "";
            }
            return new ParsedDatabaseUrl(url.toString(), user, password);
        } catch (Exception ignored) {
            return new ParsedDatabaseUrl("", "", "");
        }
    }

    private static String decode(String text) {
        return URLDecoder.decode(text, StandardCharsets.UTF_8);
    }

    private static Map<String, String> dotEnv() {
        return DotEnvHolder.VALUES;
    }

    private static final class DotEnvHolder {
        private static final Map<String, String> VALUES = loadDotEnv();

        private static Map<String, String> loadDotEnv() {
            Map<String, String> values = new LinkedHashMap<>();
            String[] files = {
                    System.getenv("XIXIAPI_ENV_FILE"),
                    ".env.local",
                    ".env",
                    "/root/sub2api-deploy/.env"
            };
            for (String file : files) {
                if (file == null || file.isBlank()) {
                    continue;
                }
                loadDotEnvFile(values, Path.of(file));
            }
            return values;
        }

        private static void loadDotEnvFile(Map<String, String> values, Path path) {
            if (!Files.isRegularFile(path)) {
                return;
            }
            try {
                for (String rawLine : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                    String line = rawLine.trim();
                    if (line.isBlank() || line.startsWith("#")) {
                        continue;
                    }
                    if (line.startsWith("export ")) {
                        line = line.substring("export ".length()).trim();
                    }
                    int split = line.indexOf('=');
                    if (split <= 0) {
                        continue;
                    }
                    String key = line.substring(0, split).trim();
                    String value = stripQuotes(line.substring(split + 1).trim());
                    values.putIfAbsent(key, value);
                }
            } catch (Exception ignored) {
            }
        }

        private static String stripQuotes(String value) {
            if (value.length() >= 2) {
                char first = value.charAt(0);
                char last = value.charAt(value.length() - 1);
                if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                    return value.substring(1, value.length() - 1);
                }
            }
            return value;
        }
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

    private record DbConfig(String url, String user, String password) {
        boolean configured() {
            return !url.isBlank() && !user.isBlank() && !password.isBlank();
        }
    }

    private record ParsedDatabaseUrl(String url, String user, String password) {
    }
}
