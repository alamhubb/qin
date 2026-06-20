package com.qin.types;

public record DatabaseConfig(
        String url,
        String user,
        String password,
        String passwordEnv) {

    public DatabaseConfig {
        url = blankToNull(url);
        user = blankToNull(user);
        password = blankToNull(password);
        passwordEnv = blankToNull(passwordEnv);
    }

    public DatabaseConfig() {
        this(null, null, null, null);
    }

    private static String blankToNull(String value) {
        return value != null && !value.isBlank() ? value : null;
    }
}
