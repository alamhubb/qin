package com.qin.core;

import com.qin.types.LanguageConfig;
import com.qin.types.QinConfig;
import com.qin.types.ValidationResult;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinLanguageConfigSmokeTestMain {
    private QinLanguageConfigSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-language-config-");
        Files.createDirectories(root.resolve("src"));
        Files.writeString(root.resolve("src").resolve("language-server.ts"), "export {}\n", StandardCharsets.UTF_8);
        Files.writeString(root.resolve("src").resolve("compiler.ts"), "export {}\n", StandardCharsets.UTF_8);
        Files.createDirectories(root.resolve("parser"));
        Files.createDirectories(root.resolve("idea-client"));
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: 'com.qin.demo:language-config',
                  version: '0.1.0',
                  language: {
                    id: 'demo',
                    extension: '.demo',
                    server: 'src/language-server.ts',
                    serverBundle: 'src/language-server.ts',
                    parser: 'parser',
                    compiler: 'src/compiler.ts',
                    ideaLspClient: 'idea-client'
                  }
                }
                """, StandardCharsets.UTF_8);

        ConfigLoader loader = new ConfigLoader(root.toString());
        QinConfig config = loader.load();
        LanguageConfig language = config.language();
        require(language != null, "language metadata");
        require("demo".equals(language.id()), "language.id");
        require(".demo".equals(language.extension()), "language.extension");
        require("src/language-server.ts".equals(language.server()), "language.server");
        require("src/language-server.ts".equals(language.serverBundle()), "language.serverBundle");
        require("parser".equals(language.parser()), "language.parser");
        require("src/compiler.ts".equals(language.compiler()), "language.compiler");
        require("idea-client".equals(language.ideaLspClient()), "language.ideaLspClient");
        require(loader.validate(config).isValid(), "valid language metadata");

        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: 'com.qin.demo:bad-language-config',
                  language: {
                    id: 'bad',
                    extension: 'bad',
                    server: 'missing-server.ts',
                    parser: 'missing/parser'
                  }
                }
                """, StandardCharsets.UTF_8);
        ValidationResult invalid = loader.validate(loader.load());
        require(!invalid.isValid(), "invalid language metadata");
        require(invalid.getErrors().stream().anyMatch(error -> error.contains("language.extension")), "extension error");
        require(invalid.getErrors().stream().anyMatch(error -> error.contains("language.server")), "server path error");
        require(invalid.getErrors().stream().anyMatch(error -> error.contains("language.parser")), "parser path error");

        System.out.println("QinLanguageConfigSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
