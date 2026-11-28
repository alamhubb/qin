package com.qin.subhutijson;

public final class QinJsonModuleSmokeTestMain {
    private QinJsonModuleSmokeTestMain() {
    }

    public static void main(String[] args) {
        String generated = QinJsonModule.toEsm("{\"version\":\"0.45.4\",\"nested\":[true,null,2]}");
        require(generated.contains("export default __qin_json;"), "default JSON module export");
        require(generated.contains("export const version = __qin_json[\"version\"]"), "named JSON export");
        require(generated.contains("export const nested = __qin_json[\"nested\"]"), "named nested export");

        expectInvalid("{'version': 1}");
        expectInvalid("{\"version\": 1,}");
        expectInvalid("{\"version\": 1} trailing");
        expectInvalid("// comment\n{\"version\": 1}");
        expectInvalid("{\"version\": 01}");
        expectInvalid("{\"version\": \"\\x20\"}");

        System.out.println("QinJsonModuleSmokeTestMain OK");
    }

    private static void expectInvalid(String source) {
        try {
            QinJsonModule.toEsm(source);
            throw new IllegalStateException("Expected JSON input to fail: " + source);
        } catch (IllegalArgumentException expected) {
            // Invalid input is rejected by the generated grammar or strict JSON data parser.
        }
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
