package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsNumberStringCoercionSmokeTestMain {
    private QinJsNumberStringCoercionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-number-string-coercion-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-number-string-coercion\" };\n",
                StandardCharsets.UTF_8);

        String source = """
                const tokenName = "StatementListItem";
                [
                  tokenName == 0,
                  tokenName < 0,
                  isNaN(tokenName),
                  isFinite(tokenName),
                  "1e3" == 1000,
                  ".5" == 0.5,
                  isFinite("Infinity")
                ].join(",");
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_number_string_coercion");
        if (!"false,false,true,false,true,true,false".equals(result)) {
            throw new IllegalStateException("Expected JS number string coercion result, got: " + result);
        }
        System.out.println("QinJsNumberStringCoercionSmokeTestMain OK");
    }
}
